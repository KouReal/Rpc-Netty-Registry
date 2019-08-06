package configutils;

public class ServiceConfig {
	private String servicename;
	private String addr;
	
	public ServiceConfig(String servicename, String addr) {
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
	
}
