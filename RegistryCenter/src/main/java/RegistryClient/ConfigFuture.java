package RegistryClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import configutils.NormalConfig;

@Component("configFuture")
public class ConfigFuture implements Future<NormalConfig>{
	private Logger log = LoggerFactory.getLogger(ConfigFuture.class);

	private NormalConfig ncfg;
	private boolean done=false;
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public NormalConfig get() throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		log.info("阻塞等待接收NormalConfig");
		while(!isDone()){
			//
		}
		log.info("已经接收到NormalConfig");
		return ncfg;
	}

	@Override
	public NormalConfig get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void done(NormalConfig ncfg){
		log.info("future done:ncfg:{}",ncfg);
		this.ncfg = ncfg;
		this.done=true;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return done;
	}
	
}
