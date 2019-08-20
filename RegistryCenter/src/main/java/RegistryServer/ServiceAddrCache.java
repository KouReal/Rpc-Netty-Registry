package RegistryServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import configutils.ServiceAddr;
import configutils.ServiceConfig;



@Component("serviceAddrCache")

public class ServiceAddrCache {
	@Autowired
	private ServiceConfig serviceconfig;
	
	private static Logger logger = LoggerFactory.getLogger(ServiceAddrCache.class);
	private Map<String, String> cache=new HashMap<String, String>();
	private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
	
	@PostConstruct
	public void init(){
		List<ServiceAddr> serviceaddrs = serviceconfig.getServiceaddrs();
		String name = null;
		String addr = null;
		for (ServiceAddr serviceaddr : serviceaddrs) {
			name = serviceaddr.getName();
			addr = serviceaddr.getIp()+":"+serviceaddr.getPort();
			logger.info("add serviceaddr {}->{} to cache",name,addr);
			cache.put(name,addr);
		}
	}
	
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
			String addr = cache.get(servicename);
			return addr;
		}finally{
			rwlock.readLock().unlock();
		}
	}

	
}