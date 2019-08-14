package MessageUtils;

import java.io.Serializable;

public class RegistryMessage implements Serializable{
	Header header;
	Object Body;
	
	public RegistryMessage(Header header, Object body) {
		super();
		this.header = header;
		Body = body;
	}
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getBody() {
		return Body;
	}
	public void setBody(Object body) {
		Body = body;
	}
	@Override
	public String toString() {
		return "RegistryMessage [header=" + header + ", Body=" + Body + "]";
	}
	
}
