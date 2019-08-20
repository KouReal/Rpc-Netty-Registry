package protocolutils;

import java.io.Serializable;

import annotationutils.MyMessage;

@MyMessage("reg_discover")
public class RegDiscover implements Serializable{
	private String servicename;
	private String serviceaddr;
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getServiceaddr() {
		return serviceaddr;
	}
	public void setServiceaddr(String serviceaddr) {
		this.serviceaddr = serviceaddr;
	}
	@Override
	public String toString() {
		return "RegDiscover [servicename=" + servicename + ", serviceaddr=" + serviceaddr + "]";
	}
	public RegDiscover(String servicename, String serviceaddr) {
		super();
		this.servicename = servicename;
		this.serviceaddr = serviceaddr;
	}
	
}
