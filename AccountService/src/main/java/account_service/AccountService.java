package account_service;

import com.alibaba.fastjson.JSONObject;

import annotationutils.AuthToken;
import protocolutils.RpcResponse;

public interface AccountService {
	@AuthToken
	public RpcResponse openaccount(JSONObject params);
	@AuthToken
	public RpcResponse transferaccount(JSONObject params);
	@AuthToken
	public RpcResponse showcardinfo(JSONObject params);
	
	public RpcResponse onauthfail();
}
