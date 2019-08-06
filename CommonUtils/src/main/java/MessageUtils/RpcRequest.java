package MessageUtils;


public class RpcRequest {
	/**
     * 请求id，通过uuid生成
     */
    private String requestId;

    /**
     * token
     */
    private String tokenid;
    /**
     * service名称:Customer
     */
    private String servicename;
    /**
     * 方法名:searchcustmer或者addcustomer
     */
    private String methodName;
    /**
    *post  get  ,,,
    */
    private String httpmethod;
    /**
     * 参数
     *{username=kourui, password=123, cartid=100, sex=man, country=中国}
     */
    private String paramjsonstr;
    
	public RpcRequest(String requestId, String tokenid, String servicename, String methodName, String httpmethod,
			String paramjsonstr) {
		super();
		this.requestId = requestId;
		this.tokenid = tokenid;
		this.servicename = servicename;
		this.methodName = methodName;
		this.httpmethod = httpmethod;
		this.paramjsonstr = paramjsonstr;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getHttpmethod() {
		return httpmethod;
	}
	public void setHttpmethod(String httpmethod) {
		this.httpmethod = httpmethod;
	}
	public String getParamjsonstr() {
		return paramjsonstr;
	}
	public void setParamjsonstr(String paramjsonstr) {
		this.paramjsonstr = paramjsonstr;
	}
	public String getTokenid() {
		return tokenid;
	}
	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}
    
}
