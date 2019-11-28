package performancetest_service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import TokenUtils.TokenCache;
import annotationutils.MyService;
import protocolutils.RpcResponse;

@MyService(value = "performancetest")
@Component("performancetest")
@DependsOn("tokenCache")
public class PerformanceTestServiceImpl implements PerformanceTestService {
	public static Logger logger = LoggerFactory.getLogger(PerformanceTestServiceImpl.class);
	@Autowired
	public TokenCache tokenCache;
	
	@PostConstruct
	public void canceltokentimer() {
		tokenCache.timer.cancel();
	}

	@Override
	public RpcResponse testEcho(JSONObject params) {
		String msgstr = (String) params.get("testmsg");
		logger.info("PerformanceTestService get testmsg:{}",msgstr);
		JSONObject resultjson = new JSONObject();
		resultjson.put("echomsg", msgstr);
		return new RpcResponse(RpcResponse.RESOURCE, null, resultjson.toJSONString());
	}
}
