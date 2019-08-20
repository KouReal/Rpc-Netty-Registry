package configutils;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix="rpcconfig")
public class SsoConfig implements Serializable{
	private List<SsoGroup> ssogroups;
	public List<SsoGroup> getSsogroups() {
		return ssogroups;
	}

	public void setSsogroups(List<SsoGroup> ssogroups) {
		this.ssogroups = ssogroups;
	}

	@Override
	public String toString() {
		return "SsoConfig [ssogroups=" + ssogroups + "]";
	}
	
	public SsoGroup getgroup(String servicename){
		if(servicename==null)return null;
		for (SsoGroup sso : ssogroups) {
			String templeader = sso.getLeaderService();
			if(servicename.equals(templeader)){
				return sso;
			}
			if(sso.getGroupMembers().contains(servicename)){
				return sso;
			}
		}
		return null;
	}
	
	public String getloginaddr(String servicename){
		SsoGroup sso = getgroup(servicename);
		if(sso==null){
			return null;
		}else{
			return sso.getLoginAddr();
		}
	}
	
}
