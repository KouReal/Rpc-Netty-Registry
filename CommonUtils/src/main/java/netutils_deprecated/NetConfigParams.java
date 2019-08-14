/*package netutils_deprecated;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import configutils.NormalConfig;
import io.netty.channel.ChannelHandler;


public class NetConfigParams {
	public List<Class<? extends ChannelHandler>> channelHandlers;
	public NormalConfig normalConfig;
	public NetDataConfig netDataConfig;
	public List<Class<? extends ChannelHandler>> getChannelHandlers() {
		return channelHandlers;
	}
	public void setChannelHandlers(List<Class<? extends ChannelHandler>> channelHandlers) {
		this.channelHandlers = channelHandlers;
	}
	public NormalConfig getNormalConfig() {
		return normalConfig;
	}
	public void setNormalConfig(NormalConfig normalConfig) {
		this.normalConfig = normalConfig;
	}
	public NetDataConfig getNetDataConfig() {
		return netDataConfig;
	}
	public void setNetDataConfig(NetDataConfig netDataConfig) {
		this.netDataConfig = netDataConfig;
	}
	@Override
	public String toString() {
		return "NetConfigParams [channelHandlers=" + channelHandlers + ", normalConfig=" + normalConfig
				+ ", netDataConfig=" + netDataConfig + "]";
	}
	

	
	
}

@Configuration
@ActiveProfiles("netlocalconf")
@ConfigurationProperties(prefix="netdata")
class NetDataConfig{
	public Map<String, LocalConfig> configmap;

	public Map<String, LocalConfig> getConfigmap() {
		return configmap;
	}

	public void setConfigmap(Map<String, LocalConfig> configmap) {
		this.configmap = configmap;
	}

	@Override
	public String toString() {
		return "NetDataConfig [configmap=" + configmap + "]";
	}

	
	
}
class LocalConfig {
	private String threadname;
	private boolean daemon;
	private int bossnum;
	private int workernum;
	public String getThreadname() {
		return threadname;
	}
	public void setThreadname(String threadname) {
		this.threadname = threadname;
	}
	public boolean isDaemon() {
		return daemon;
	}
	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}
	public int getBossnum() {
		return bossnum;
	}
	public void setBossnum(int bossnum) {
		this.bossnum = bossnum;
	}
	public int getWorkernum() {
		return workernum;
	}
	public void setWorkernum(int workernum) {
		this.workernum = workernum;
	}
	@Override
	public String toString() {
		return "NetInfo [threadname=" + threadname + ", daemon=" + daemon + ", bossnum=" + bossnum + ", workernum="
				+ workernum + "]";
	}
}

*/