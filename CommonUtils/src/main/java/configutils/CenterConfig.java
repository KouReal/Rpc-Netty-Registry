package configutils;

public class CenterConfig {
	public Uniformconfig uniformconfig;
	public ServiceConfig serviceconfig;
	public SsoConfig ssoconfig;
	public Uniformconfig getUniformconfig() {
		return uniformconfig;
	}
	public void setUniformconfig(Uniformconfig uniformconfig) {
		this.uniformconfig = uniformconfig;
	}
	public ServiceConfig getServiceconfig() {
		return serviceconfig;
	}
	public void setServiceconfig(ServiceConfig serviceconfig) {
		this.serviceconfig = serviceconfig;
	}
	public SsoConfig getSsoconfig() {
		return ssoconfig;
	}
	public void setSsoconfig(SsoConfig ssoconfig) {
		this.ssoconfig = ssoconfig;
	}
	public CenterConfig(Uniformconfig uniformconfig, ServiceConfig serviceconfig, SsoConfig ssoconfig) {
		super();
		this.uniformconfig = uniformconfig;
		this.serviceconfig = serviceconfig;
		this.ssoconfig = ssoconfig;
	}
	public CenterConfig() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "NormalConfig [uniformconfig=" + uniformconfig + ", serviceconfig=" + serviceconfig + ", ssoconfig="
				+ ssoconfig + "]";
	}
	
}
