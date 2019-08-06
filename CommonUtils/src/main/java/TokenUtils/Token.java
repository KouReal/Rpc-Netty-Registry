package TokenUtils;

import java.util.Date;

public class Token {
	//createtime 创建时间（用于定期销毁)
	private Date createtime;
	//servicename 认证范围
	private String servicename;
	//tid uuid号码
	private String tid;
	public Token(Date createtime, String servicename, String tid) {
		super();
		this.createtime = createtime;
		this.servicename = servicename;
		this.tid = tid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
}
