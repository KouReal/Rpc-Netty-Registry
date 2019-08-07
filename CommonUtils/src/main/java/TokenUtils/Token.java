package TokenUtils;

import java.util.Date;

public class Token {
	//createtime 创建时间（用于定期销毁)
	private Date createtime;
	//servicename leadername
	private String leadername;
	//tid uuid号码
	private String tid;
	public Token(Date createtime, String leadername, String tid) {
		super();
		this.createtime = createtime;
		this.setLeadername(leadername);
		this.tid = tid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getLeadername() {
		return leadername;
	}
	public void setLeadername(String leadername) {
		this.leadername = leadername;
	}
}
