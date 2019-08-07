package configutils;

import org.springframework.beans.factory.annotation.Autowired;
import RegistryUtil.Serviceconfig;
import RegistryUtil.Ssoconfig;
import RegistryUtil.Uniformconfig;

public class NormalConfig {
	@Autowired
	public Uniformconfig uniformconfig;
	@Autowired
	public Serviceconfig serviceconfig;
	@Autowired
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
	

}
