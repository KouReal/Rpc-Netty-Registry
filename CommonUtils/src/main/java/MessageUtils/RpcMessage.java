package MessageUtils;

import java.io.Serializable;

public class RpcMessage implements Serializable{
	private Header header;
	private Object body;
	public RpcMessage(Header header, Object body) {
		super();
		this.header = header;
		this.body = body;
	}
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	
	
}
