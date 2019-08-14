package RegistryServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Component;


@Component("serviceAddrCache")
public class ServiceAddrCache {
	private Map<String, String> cache=new HashMap<String, String>();
	private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
	public boolean addservice(String servicename, String addr){
		if(servicename==null){
			return false;
		}
		try{
			rwlock.writeLock().lock();
			for(Map.Entry<String, String> entry : cache.entrySet()){
				if(servicename.equals(entry.getKey())){
					cache.replace(servicename, addr);
					return true;
				}
			}
			cache.put(servicename, addr);
			return true;
		}finally{
			rwlock.writeLock().unlock();
		}
		

	}
	public String findaddrbyservicename(String servicename){
		if(servicename==null){
			return null;
		}
		try{
			rwlock.readLock().lock();
			for(Map.Entry<String, String> entry : cache.entrySet()){
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