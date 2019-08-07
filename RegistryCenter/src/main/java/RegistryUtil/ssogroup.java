package RegistryUtil;

import java.util.List;

public class ssogroup {

    private String leaderService;
	private String loginAddr;
	private String visitorName;
	private List<String> groupMembers;
	public String getLeaderService() {
		return leaderService;
	}
	public void setLeaderService(String leaderService) {
		this.leaderService = leaderService;
	}
	public String getLoginAddr() {
		return loginAddr;
	}
	public void setLoginAddr(String loginAddr) {
		this.loginAddr = loginAddr;
	}
	public String getVisitorName() {
		return visitorName;
	}
	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}
	public List<String> getGroupMembers() {
		return groupMembers;
	}
	public void setGroupMembers(List<String> groupMembers) {
		this.groupMembers = groupMembers;
	}
	@Override
	public String toString() {
		return "ssogroup [leaderService=" + leaderService + ", loginAddr=" + loginAddr + ", visitorName=" + visitorName
				+ ", groupMembers=" + groupMembers + "]";
	}
	
}
