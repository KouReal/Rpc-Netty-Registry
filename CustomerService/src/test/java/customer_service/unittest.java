package customer_service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;

import exceptionutils.RpcErrorException;
import reflectionutils.ServiceProxy;

@RunWith(SpringRunner.class)
@SpringBootTest

public class unittest {
	Logger log = LoggerFactory.getLogger(unittest.class);
	
	@Autowired
	private ServiceProxy sp;
	
	@Autowired
	private testyml ty;

    @Test
    public void testuniformconfig(){
    	log.info("get:"+ty.getServerip());
		
		JSONObject params = new JSONObject();
		params.put("username", "trump");
		params.put("password", "222");
		params.put("country", "us");
		params.put("tel", "123");
    	try {
			String result = (String)sp.callservice("Customer", "regist",params);
			System.out.println(result);
		} catch (RpcErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
