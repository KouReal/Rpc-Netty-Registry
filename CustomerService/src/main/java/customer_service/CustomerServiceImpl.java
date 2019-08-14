package customer_service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import MessageUtils.Ssoconfig;
import RegistryClient.RegistryClient;
import TokenUtils.Token;
import TokenUtils.TokenCache;
import annotationutils.AuthToken;
import annotationutils.MyService;
import configutils.NormalConfig;
import configutils.ServiceRegist;
import mapper.CustomerMapper;
import pojo.Customer;
import sqlsessionmanage.BaseMapper;
import typehandler.EnableCard;

@MyService(value = "Customer")
@Component("customerServiceImpl")
@DependsOn(value={"registryClient","tokenCache","normalConfig"})
public class CustomerServiceImpl extends BaseMapper implements CustomerService {
	public static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);	
	@Autowired
	private RegistryClient registryClient;
	@Autowired
	private TokenCache tokenCache;
	
	@Autowired
	private NormalConfig normalConfig;
	
	@Override
	public String regist(JSONObject params){
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
			
			return params.toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			JSONObject ej = new JSONObject();
			ej.put("errormsg", "注册失败:"+e.getMessage());
			return ej.toJSONString();
		}finally {
			session.commit();
			session.close();
		}
	}
	@Override
	public String login(JSONObject params){
		String username = (String)params.get("username");
		String password = (String)params.get("password");
		SqlSession session = getSqlSession();
		try {
			CustomerMapper customerMapper = session.getMapper(CustomerMapper.class);
			int num = customerMapper.searchByIdAndPassword(username, password);
			JSONObject ej = new JSONObject();
			if(num>0){
				ej.put("result", "登录成功");
				Long id = customerMapper.selectByUserName(username).getId();
				updatelogintoken(id);
			}else{
				ej.put("result", "登录失败");
			}
			return ej.toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			JSONObject ej = new JSONObject();
			ej.put("errormsg", "注册失败:"+e.getMessage());
			return ej.toJSONString();
		}finally {
			session.close();
		}
	}
	@Override
	@AuthToken
	public String showpersonalinfo(JSONObject params){
		Long id = Long.valueOf((String)params.get("customerid"));
		SqlSession session = getSqlSession();
		try {
			CustomerMapper customerMapper = session.getMapper(CustomerMapper.class);
			Customer customer = customerMapper.selectByPrimaryKey(id);
			String jstr = JSON.toJSONString(customer);
			return jstr;
			
		} catch (Exception e) {
			// TODO: handle exception
			JSONObject ej = new JSONObject();
			ej.put("errormsg", "注册失败:"+e.getMessage());
			return ej.toJSONString();
		}finally {
			session.close();
		}
	}
	
	private boolean checksso(){
		Ssoconfig ssoconfig = normalConfig.getSsoconfig();
		if(ssoconfig!=null){
			return true;
		}
		return false;
	}
	
	private void updatelogintoken(Long id) {
		// TODO Auto-generated method stub
		Map<String, String> attach = new HashMap<String, String>();
		attach.put("customerid", String.valueOf(id));
		String tid = UUID.randomUUID().toString();
		Token token = new Token(new Date(), "Customer", attach, tid);
		tokenCache.addtoken(token);
		if(checksso()){
			logger.info("register token:{} from leader:Customer",token);
			Header header = new Header(1, Header.REGISTRY_TOKEN);
	        RegistryMessage msg = new RegistryMessage(header, (Object)token);
	        logger.info("customer构造注册token的RegistryMessage信息:{}",msg);
	        try {
				registryClient.sendtocenter(msg);
			} catch (Exception e) {
				logger.info("registryclient 发送token信息失败：{}",e.getMessage());
			}
		}
        
	}
}
