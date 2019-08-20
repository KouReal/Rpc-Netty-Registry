package RegistryServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;

@Component("channelCache")
public class ChannelCache {
	private Map<String, Channel> cache=new HashMap<String, Channel>();
	private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
	public boolean addchannel(String servicename, Channel channel){
		if(servicename==null){
			return false;
		}
		try{
			rwlock.writeLock().lock();
			for(Map.Entry<String, Channel> entry : cache.entrySet()){
				if(servicename.equals(entry.getKey())){
					cache.replace(servicename, channel);
					return true;
				}
			}
			cache.put(servicename, channel);
			return true;
		}finally{
			rwlock.writeLock().unlock();
		}

	}
	public Channel findchannelbyservicename(String servicename){
		if(servicename==null){
			return null;
		}
		try{
			rwlock.readLock().lock();
			for(Map.Entry<String, Channel> entry : cache.entrySet()){
				if(servicename.equals(entry.getKey())){
					return entry.getValue();
				}
			}
		}finally{
			rwlock.readLock().unlock();
		}
		return null;
	}

	
}
