package protocolutils;

import java.util.UUID;

public class LenPreMsg {
	//protocol   Header 0x1234a00_ ;
	public static final int BASE_PRE_LEN = 44;
	
	//4bytes
	private Header header;
	
	//36bytes
	private String msgid;
	
	//4bytes
	private int datalength;
	
	
	private Object body;
	public LenPreMsg(Header header,String msgid, int datalength, Object body) {
		super();
		this.header = header;
		this.msgid = msgid;
		this.datalength = datalength;
		
		this.body = body;
	}
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public int getDatalength() {
		return datalength;
	}
	public void setDatalength(int datalength) {
		this.datalength = datalength;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public static LenPreMsg buildsimplemsg(Header header, Object body){
		return new LenPreMsg(header, UUID.randomUUID().toString(), 1, body);
	}
	@Override
	public String toString() {
		return "LenPreMsg [header=" + header + ", datalength=" + datalength + ", msgid=" + msgid + ", body=" + body + "]";
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	
}
