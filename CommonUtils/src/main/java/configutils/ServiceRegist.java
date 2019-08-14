package configutils;

import java.io.Serializable;

public class ServiceRegist implements Serializable{
	private String servicename;
	private String addr;
	
	public ServiceRegist(String servicename, String addr) {
		super();
		this.servicename = servicename;
		this.addr = addr;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	@Override
	public String toString() {
		return "ServiceRegist [servicename=" + servicename + ", addr=" + addr + "]";
	}
	
}
