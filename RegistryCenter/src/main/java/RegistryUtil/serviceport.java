package RegistryUtil;

public class serviceport {
	private String name;
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
	@Override
	public String toString() {
		return "serviceport [name=" + name + ", port=" + port + "]";
	}
	
}
