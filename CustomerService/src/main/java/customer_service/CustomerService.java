package customer_service;

import com.alibaba.fastjson.JSONObject;

import annotationutils.AuthToken;

public interface CustomerService {
	public String regist(JSONObject params);
	public String login(JSONObject params);
	@AuthToken
	public String showpersonalinfo(JSONObject params);
}
