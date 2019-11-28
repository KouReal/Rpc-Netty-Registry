package performancetest_service;

import com.alibaba.fastjson.JSONObject;
import protocolutils.RpcResponse;

public interface PerformanceTestService {
	public RpcResponse testEcho(JSONObject params);
}
