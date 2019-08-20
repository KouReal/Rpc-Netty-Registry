package tradeinfo_service;

import com.alibaba.fastjson.JSONObject;

import annotationutils.AuthToken;
import protocolutils.RpcResponse;

public interface TradeinfoService {
	@AuthToken
	public RpcResponse showtradeinfo(JSONObject params);
	
	public RpcResponse onauthfail();
}
