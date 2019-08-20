package customer_service;

import com.alibaba.fastjson.JSONObject;

import annotationutils.AuthToken;
import protocolutils.RpcResponse;

public interface CustomerService {
	public RpcResponse regist(JSONObject params);
	public RpcResponse login(JSONObject params);
	@AuthToken
	public RpcResponse showpersonalinfo(JSONObject params);
	
	public RpcResponse onauthfail();
}
