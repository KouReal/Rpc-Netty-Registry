package configutils;

public class ServiceAddr {
	private String name;
	private String ip;
	private int port;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		return "ServiceAddr [name=" + name + ", ip=" + ip + ", port=" + port + "]";
	}
	
	
}