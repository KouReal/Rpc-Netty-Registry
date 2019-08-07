package configutils;

public class ServiceRegist {
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
	
}
