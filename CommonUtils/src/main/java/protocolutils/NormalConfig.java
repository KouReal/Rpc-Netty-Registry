package protocolutils;

import java.io.Serializable;


import annotationutils.MyMessage;
import configutils.ServiceAddr;
import configutils.SsoGroup;
import configutils.Uniformconfig;

@MyMessage("reg_normalconfig")
public class NormalConfig implements Serializable{
	public Uniformconfig uniformconfig;
	public ServiceAddr serviceAddr;
	public SsoGroup ssoGroup;
	public Uniformconfig getUniformconfig() {
		return uniformconfig;
	}
	public void setUniformconfig(Uniformconfig uniformconfig) {
		this.uniformconfig = uniformconfig;
	}
	public ServiceAddr getServiceAddr() {
		return serviceAddr;
	}
	public void setServiceAddr(ServiceAddr serviceAddr) {
		this.serviceAddr = serviceAddr;
	}
	public SsoGroup getSsoGroup() {
		return ssoGroup;
	}
	public void setSsoGroup(SsoGroup ssoGroup) {
		this.ssoGroup = ssoGroup;
	}
	
	public NormalConfig(Uniformconfig uniformconfig, ServiceAddr serviceAddr, SsoGroup ssoGroup) {
		super();
		this.uniformconfig = uniformconfig;
		this.serviceAddr = serviceAddr;
		this.ssoGroup = ssoGroup;
	}
	@Override
	public String toString() {
		return "NormalConfig [uniformconfig=" + uniformconfig + ", serviceAddr=" + serviceAddr + ", ssoGroup="
				+ ssoGroup + "]";
	}
	

}
