package protocolutils;

import java.io.Serializable;

import annotationutils.MyMessage;

@MyMessage("rpc_response")
public class RpcResponse implements Serializable {
//	private String requestid;
	
	private byte responsetype;
	
	public static byte RESOURCE=1;
	public static byte REDIRECT=2;
	public static byte FAILURE=3;
	public static byte TOKEN=4;
	
	private String errormsg;
	//json str
	private String content;
	/*public String getRequestid() {
		return requestid;
	}
	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}*/
	public byte getResponsetype() {
		return responsetype;
	}
	public void setResponsetype(byte responsetype) {
		this.responsetype = responsetype;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public RpcResponse(byte responsetype, String errormsg, String content) {
		super();
//		this.requestid = requestid;
		this.responsetype = responsetype;
		this.errormsg = errormsg;
		this.content = content;
	}
	public RpcResponse() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "RpcResponse [responsetype=" + responsetype + ", errormsg=" + errormsg
				+ ", content=" + content + "]";
	}
	
	

}
