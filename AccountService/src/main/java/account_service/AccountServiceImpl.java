package account_service;

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

@MyService(value = "account")
@Component("account")
@DependsOn(value={"registryClient","tokenCache","normalConfig"})
public class AccountServiceImpl extends BaseMapper implements AccountService {
	public static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);	
	@Autowired
	private RegistryClient registryClient;
	@Autowired
	private TokenCache tokenCache;
	
	
	private NormalConfig normalConfig = (NormalConfig) SpringContextStatic.getBean("normalConfig");
	
	@Override
	@AuthToken
	public RpcResponse openaccount(JSONObject params){
		String password = (String)params.get("password");
		String balance = (String)params.get("balance");
		String tokenid = (String)params.get("tokenid");
		Token token = tokenCache.gettokenbyid(tokenid);
		JSONObject resultjson = new JSONObject();
		if(token==null){
			resultjson.put("error", "Token已经失效");
			return new RpcResponse(RpcResponse.FAILURE,resultjson.toJSONString(),null);
		}
		Long customerid = Long.valueOf(token.getAttachinfo().get("customerid"));
		Customer customer = getcustomerbyid(customerid);
		Long cardid = getcardidbycustomerid(customerid);
		if(cardid==null){
			logger.info("当前customer:{}没有创建银行卡,即将创建银行卡",customerid);
			SqlSession session = getSqlSession();
			try {
				CardinfoMapper cardinfoMapper = session.getMapper(CardinfoMapper.class);
				Cardinfo cardinfo = new Cardinfo(null, password, Long.valueOf(balance), new Date(), customer.getUserName());
				cardinfoMapper.insert(cardinfo);
				resultjson.put("result", "开户成功");
				resultjson.put("cardinfo", cardinfo);
				session.commit();
				return new RpcResponse(RpcResponse.RESOURCE, null, resultjson.toJSONString());
			} catch (Exception e) {
				session.rollback();
				resultjson.put("result", "开户失败");
				resultjson.put("error", e.getMessage());
				return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(),null);
			}finally {
				session.close();
			}
		}else{
			logger.info("当前customer:{}已经创建银行卡",customerid);
			Cardinfo cardinfo = getcardinfobyid(cardid);
			resultjson.put("cardinfo", cardinfo);
			return new RpcResponse(RpcResponse.RESOURCE, null, resultjson.toJSONString());
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
	@AuthToken
	public RpcResponse transferaccount(JSONObject params){
		Long payer = Long.valueOf((String)params.get("payer"));
		Long receiver = Long.valueOf((String)params.get("receiver"));
		String password = (String)params.get("password");
		Long amount = Long.valueOf((String)params.get("amount"));
		JSONObject resultjson = new JSONObject();
		SqlSession sqlSession = getSqlSession();
		try{
			CardinfoMapper cardinfoMapper = sqlSession.getMapper(CardinfoMapper.class);
			TradeinfoMapper tradeinfoMapper = sqlSession.getMapper(TradeinfoMapper.class);
			CardTradeMapper cardTradeMapper = sqlSession.getMapper(CardTradeMapper.class);
			CardinfoExample cardinfoExample = new CardinfoExample();
			CardinfoExample.Criteria  cardinfocriteria= cardinfoExample.createCriteria();
			cardinfocriteria.andIdEqualTo(payer);
			cardinfocriteria.andPasswordEqualTo(password);
			List<Cardinfo> cardinfolist = cardinfoMapper.selectByExample(cardinfoExample);
			if(cardinfolist==null||cardinfolist.size()==0){
				resultjson.put("error", "卡号或密码错误");
				return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
			}
			
			Cardinfo payercard = cardinfoMapper.selectByPrimaryKey(payer);
			Long paybalance = payercard.getBalance();
			if(paybalance<amount){
				resultjson.put("error", "余额不足");
				return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
			}
			Cardinfo receivercard = cardinfoMapper.selectByPrimaryKey(receiver);
			if(receiver==null){
				resultjson.put("error", "对方账户不存在");
				return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
			}
			Long receiverbalance = receivercard.getBalance();
			payercard.setBalance(paybalance-amount);
			receivercard.setBalance(receiverbalance+amount);
			
			cardinfoMapper.updateByPrimaryKeySelective(payercard);
			cardinfoMapper.updateByPrimaryKeySelective(receivercard);
			
			Tradeinfo tradeinfo = new Tradeinfo();
			tradeinfo.setPayer(payer);
			tradeinfo.setReceiver(receiver);
			tradeinfo.setAmount(amount);
			tradeinfo.setCreateTime(new Date());
			long tradeId = tradeinfoMapper.insert(tradeinfo);
			//tradeinfoMapper.insert(tradeinfo);
			
			CardTrade cardTrade1 = new CardTrade();
			cardTrade1.setCardId(payer);
			cardTrade1.setTradeId(tradeId);
			cardTrade1.setTradeType(0);
			cardTradeMapper.insert(cardTrade1);
			
			CardTrade cardTrade2 = new CardTrade();
			cardTrade2.setCardId(receiver);
			cardTrade2.setTradeId(tradeId);
			cardTrade2.setTradeType(1);
			cardTradeMapper.insert(cardTrade2);
			sqlSession.commit();
			resultjson.put("result", "转账成功");
			resultjson.put("银行卡信息", payercard);
			resultjson.put("交易信息", tradeinfo);
			return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
		}catch(Exception e){
			sqlSession.rollback();
			resultjson.put("error", e.getMessage());
			return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
		}finally {
			sqlSession.close();
		}
	}
	@Override
	@AuthToken
	public RpcResponse showcardinfo(JSONObject params){
		String tokenid = (String)params.get("tokenid");
		Token token = tokenCache.gettokenbyid(tokenid);
		JSONObject resultjson = new JSONObject();
		if(token==null){
			resultjson.put("error", "Token已经失效");
			return new RpcResponse(RpcResponse.FAILURE,resultjson.toJSONString(),null);
		}
		Long customerid = Long.valueOf(token.getAttachinfo().get("customerid"));
		Long cardid = getcardidbycustomerid(customerid);
		
		if(cardid==null){
			resultjson.put("result", "当前用户没有银行卡");
			Customer customer = getcustomerbyid(customerid);
			resultjson.put("用户信息", customer);
			return new RpcResponse(RpcResponse.RESOURCE, null, resultjson.toJSONString());
		}else{
			Cardinfo cardinfo = getcardinfobyid(cardid);
			resultjson.put("cardinfo", cardinfo);
			return new RpcResponse(RpcResponse.RESOURCE, null, resultjson.toJSONString());
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
