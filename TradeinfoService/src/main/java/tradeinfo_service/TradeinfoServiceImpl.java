package tradeinfo_service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import RegistryClient.RegistryClient;
import TokenUtils.TokenCache;
import annotationutils.AuthToken;
import annotationutils.MyService;
import configutils.SsoGroup;
import io.netty.handler.codec.json.JsonObjectDecoder;
import mapper.CardTradeMapper;
import mapper.CardinfoMapper;
import mapper.CustomerMapper;
import mapper.TradeinfoMapper;
import mapper.UserCardMapper;
import pojo.CardTrade;
import pojo.Cardinfo;
import pojo.CardinfoExample;
import pojo.Customer;
import pojo.Tradeinfo;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.NormalConfig;
import protocolutils.RpcResponse;
import protocolutils.Token;
import springutils.SpringContextStatic;
import sqlsessionmanage.BaseMapper;
import typehandler.EnableCard;

@MyService(value = "tradeinfo")
@Component("tradeinfo")
@DependsOn(value={"registryClient","tokenCache","normalConfig"})
public class TradeinfoServiceImpl extends BaseMapper implements TradeinfoService {
	public static Logger logger = LoggerFactory.getLogger(TradeinfoServiceImpl.class);	
	@Autowired
	private TokenCache tokenCache;
	
	
	private NormalConfig normalConfig = (NormalConfig) SpringContextStatic.getBean("normalConfig");
	
	@Override
	@AuthToken
	public RpcResponse showtradeinfo(JSONObject params){
		String tokenid = (String)params.get("tokenid");
		JSONObject resultjson = new JSONObject();
		Token token = tokenCache.gettokenbyid(tokenid);
		if(token==null){
			resultjson.put("error", "token已经失效");
			return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
		}
		Long customerid = Long.valueOf(token.getAttachinfo().get("customerid"));
		if(customerid == null){
			resultjson.put("error", "此Token没有附加用户信息");
			return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
		}
		Customer customer = getcustomerbyid(customerid);
		Long cardid = getcardidbycustomerid(customerid);
		if(cardid==null){
			resultjson.put("customer", customer);
			resultjson.put("error", "此用户没有创建银行卡");
			return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
		}
		Cardinfo cardinfo = getcardinfobyid(cardid);
		SqlSession sqlSession = getSqlSession();
		try {
			TradeinfoMapper tradeinfoMapper = sqlSession.getMapper(TradeinfoMapper.class);
			List<Tradeinfo> tradeinfos = tradeinfoMapper.selectAllTradeInfoById(cardid);
			resultjson.put("customer", customer);
			resultjson.put("cardinfo", cardinfo);
			resultjson.put("交易记录", tradeinfos);
			return new RpcResponse(RpcResponse.RESOURCE, null, resultjson.toJSONString());
		}catch(Exception e){
			resultjson.put("error", e.getMessage());
			return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
		}finally {
			sqlSession.close();
		}
		
		
	}
	
	public Long getcardidbycustomerid(Long customerid){
		SqlSession session = getSqlSession();
		try {
			UserCardMapper userCardMapper = session.getMapper(UserCardMapper.class);
			Long cardid = userCardMapper.selectCardidByCustomerid(customerid);
			return cardid;
		} catch (Exception e) {
			session.rollback();
			return null;
		}finally {
			session.close();
		}
	}
	public Cardinfo getcardinfobyid(Long id){
		SqlSession session = getSqlSession();
		try {
			CardinfoMapper cardinfoMapper = session.getMapper(CardinfoMapper.class);
			Cardinfo cardinfo = cardinfoMapper.selectByPrimaryKey(id);
			return cardinfo;
		} catch (Exception e) {
			return null;
		}finally {
			session.close();
		}
	}
	
	public Customer getcustomerbyid(Long id){
		SqlSession session = getSqlSession();
		try{
			CustomerMapper customerMapper = session.getMapper(CustomerMapper.class);
			Customer customer = customerMapper.selectByPrimaryKey(id);
			return customer;
		}catch(Exception e){
			return null;
		}finally {
			session.close();
		}
	}
	
	@Override
	public RpcResponse onauthfail(){
		JSONObject resultjson = new JSONObject();
		SsoGroup ssoGroup = normalConfig.getSsoGroup();
		if(ssoGroup!=null){
			resultjson.put("登录地址", ssoGroup.getLoginAddr());
		}
		resultjson.put("error", "token验证失败，权限不足");
		return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
	}
	
}
