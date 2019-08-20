package protocolutils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import annotationutils.MyMessage;

@MyMessage("reg_tokenconfig")
public class Token implements Serializable{
	//createtime 创建时间（用于定期销毁)
	private Date createtime;
	//servicename leadername
	private String leadername;
	
	//附加信息json字符串，如：{"customerid" : "1"}
	private Map<String, String> attachinfo;
	//tid uuid号码
	private String tid;
	
	public Token(Date createtime, String leadername, Map<String, String> attachinfo, String tid) {
		super();
		this.createtime = createtime;
		this.leadername = leadername;
		this.attachinfo = attachinfo;
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
	public Map<String, String> getAttachinfo() {
		return attachinfo;
	}
	public void setAttachinfo(Map<String, String> attachinfo) {
		this.attachinfo = attachinfo;
	}
	@Override
	public String toString() {
		return "Token [createtime=" + createtime + ", leadername=" + leadername + ", attachinfo=" + attachinfo
				+ ", tid=" + tid + "]";
	}
	
}
