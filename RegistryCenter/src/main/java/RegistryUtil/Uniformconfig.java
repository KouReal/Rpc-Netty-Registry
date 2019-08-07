package RegistryUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration

@ConfigurationProperties(prefix="rpcconfig.dataconfig.uniformconfig")


public class Uniformconfig {
	
	private int clientReconnectTry;
	private int clientConnectTimeout;
	private int tokenLife;
	private int clientWriteIdle;
	private int serverReadIdle;
	public int getClientReconnectTry() {
		return clientReconnectTry;
	}
	public void setClientReconnectTry(int clientReconnectTry) {
		this.clientReconnectTry = clientReconnectTry;
	}
	public int getClientConnectTimeout() {
		return clientConnectTimeout;
	}
	public void setClientConnectTimeout(int clientConnectTimeout) {
		this.clientConnectTimeout = clientConnectTimeout;
	}
	public int getTokenLife() {
		return tokenLife;
	}
	public void setTokenLife(int tokenLife) {
		this.tokenLife = tokenLife;
	}
	public int getClientWriteIdle() {
		return clientWriteIdle;
	}
	public void setClientWriteIdle(int clientWriteIdle) {
		this.clientWriteIdle = clientWriteIdle;
	}
	public int getServerReadIdle() {
		return serverReadIdle;
	}
	public void setServerReadIdle(int serverReadIdle) {
		this.serverReadIdle = serverReadIdle;
	}
	@Override
	public String toString() {
		return "uniformconfig [clientReconnectTry=" + clientReconnectTry + ", clientConnectTimeout="
				+ clientConnectTimeout + ", tokenLife=" + tokenLife + ", clientWriteIdle=" + clientWriteIdle
				+ ", serverReadIdle=" + serverReadIdle + "]";
	}
	
}
