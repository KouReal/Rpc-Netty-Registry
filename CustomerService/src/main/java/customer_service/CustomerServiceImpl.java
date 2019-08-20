package customer_service;

import java.util.Date;
import java.util.HashMap;
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
import mapper.CustomerMapper;
import pojo.Customer;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.NormalConfig;
import protocolutils.RpcResponse;
import protocolutils.Token;
import springutils.SpringContextStatic;
import sqlsessionmanage.BaseMapper;
import typehandler.EnableCard;

@MyService(value = "customer")
@Component("customer")
@DependsOn(value={"registryClient","tokenCache","normalConfig"})
public class CustomerServiceImpl extends BaseMapper implements CustomerService {
	public static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);	
	@Autowired
	private RegistryClient registryClient;
	@Autowired
	private TokenCache tokenCache;
	
	
	private NormalConfig normalConfig = (NormalConfig) SpringContextStatic.getBean("normalConfig");
	
	@Override
	public RpcResponse regist(JSONObject params){
		String username = (String)params.get("username");
		String password = (String)params.get("password");
		String country = (String)params.get("country");
		String tel = (String)params.get("tel");
		Customer customer = new Customer(null, username, password, country, tel, EnableCard.nocard);
		SqlSession session = getSqlSession();
		try {
			CustomerMapper customerMapper = session.getMapper(CustomerMapper.class);
			int id = customerMapper.insertSelective(customer);
			params.put("id", String.valueOf(id));
			params.put("result", "注册成功");
			session.commit();
			return new RpcResponse(RpcResponse.RESOURCE, null, params.toJSONString());
		} catch (Exception e) {
			session.rollback();
			params.put("result", "注册失败"+e.getMessage());
			return new RpcResponse(RpcResponse.FAILURE, e.getMessage(), params.toJSONString());
		}finally {
			session.close();
		}
	}
	@Override
	public RpcResponse login(JSONObject params){
		String username = (String)params.get("username");
		String password = (String)params.get("password");
		logger.info("name:{},pawd:{}",username,password);
		SqlSession session = getSqlSession();
		JSONObject resultjson = new JSONObject();
		try {
			CustomerMapper customerMapper = session.getMapper(CustomerMapper.class);
			int num = customerMapper.searchByIdAndPassword(username, password);
			logger.info("login num:{}",num);
			
			if(num>0){
				Customer customer = customerMapper.selectByUserName(username);
				Long id = customer.getId();
//				logger.info("id:{}",id);
				String tokenid = updatelogintoken(id);
//				logger.info("tokenid:{}",tokenid);
				resultjson.put("tokenid", tokenid);
				resultjson.put("result", "登录成功");
//				logger.info("customer:{}",customer);
				resultjson.put("customer",customer);
				return new RpcResponse(RpcResponse.TOKEN, null, resultjson.toJSONString());
			}else{
				resultjson.put("result", "登录失败");
				return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			resultjson.put("result", "登录失败:"+e.getMessage());
			return new RpcResponse(RpcResponse.FAILURE, resultjson.toJSONString(), null);
		}finally {
			session.close();
		}
	}
	@Override
	@AuthToken
	public RpcResponse showpersonalinfo(JSONObject params){
		String tokenid = (String)params.get("tokenid");
		SqlSession session = getSqlSession();
		JSONObject resultjson = new JSONObject();
		Token token = tokenCache.gettokenbyid(tokenid);
		Long customerid = Long.valueOf(token.getAttachinfo().get("customerid"));
		try {
			CustomerMapper customerMapper = session.getMapper(CustomerMapper.class);
			Customer customer = customerMapper.selectByPrimaryKey(customerid);
			resultjson.put("customer", customer);
			return new RpcResponse(RpcResponse.RESOURCE, null, resultjson.toJSONString());
		} catch (Exception e) {
			// TODO: handle exception
			resultjson.put("errormsg", "获取信息失败:"+e.getMessage());
			return new RpcResponse(RpcResponse.FAILURE,resultjson.toJSONString(),null);
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
	
	
	private String updatelogintoken(Long id) {
		// TODO Auto-generated method stub
		Map<String, String> attach = new HashMap<String, String>();
		attach.put("customerid", String.valueOf(id));
		String tid = UUID.randomUUID().toString();
		Token token = new Token(new Date(), "customer", attach, tid);
		tokenCache.addtoken(token);
		if(normalConfig.getSsoGroup()!=null){
			logger.info("register token:{} from leader:Customer",token);
			LenPreMsg lenPreMsg = LenPreMsg.buildsimplemsg(Header.reg_tokenconfig, token);
	        logger.info("customer构造注册token的lenpremsg信息:{}",lenPreMsg);
	        try {
				registryClient.invokewithfuture(lenPreMsg, null);
			} catch (Exception e) {
				logger.info("registryclient 发送token信息失败：{}",e.getMessage());
			}
		}
		return tid;
        
	}
}
