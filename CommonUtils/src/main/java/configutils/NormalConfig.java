package configutils;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import MessageUtils.Serviceconfig;
import MessageUtils.Ssoconfig;
import MessageUtils.Uniformconfig;


public class NormalConfig implements Serializable{
	public Uniformconfig uniformconfig;
	public Serviceconfig serviceconfig;
	public Ssoconfig ssoconfig;
	public Uniformconfig getUniformconfig() {
		return uniformconfig;
	}
	public void setUniformconfig(Uniformconfig uniformconfig) {
		this.uniformconfig = uniformconfig;
	}
	public Serviceconfig getServiceconfig() {
		return serviceconfig;
	}
	public void setServiceconfig(Serviceconfig serviceconfig) {
		this.serviceconfig = serviceconfig;
	}
	public Ssoconfig getSsoconfig() {
		return ssoconfig;
	}
	public void setSsoconfig(Ssoconfig ssoconfig) {
		this.ssoconfig = ssoconfig;
	}
	public NormalConfig(Uniformconfig uniformconfig, Serviceconfig serviceconfig, Ssoconfig ssoconfig) {
		super();
		this.uniformconfig = uniformconfig;
		this.serviceconfig = serviceconfig;
		this.ssoconfig = ssoconfig;
	}
	public NormalConfig() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "NormalConfig [uniformconfig=" + uniformconfig + ", serviceconfig=" + serviceconfig + ", ssoconfig="
				+ ssoconfig + "]";
	}
	

}
