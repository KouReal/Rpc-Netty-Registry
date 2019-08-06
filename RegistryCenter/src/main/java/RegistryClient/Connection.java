package RegistryClient;



import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import net.bytebuddy.agent.builder.AgentBuilder.CircularityLock.Default;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptionutils.RpcServiceDisconnectException;

/**
 * @author jsj
 * @date 2018-10-24
 */
public class Connection {

    private Channel channel;
    private ReentrantReadWriteLock channel_rwlock; 
    private ReentrantReadWriteLock thread_rwlock;

    public static final int DEFAULT_RECONNECT_TRY = 20;
    public static final int DEFAULT_CONNECT_TIMEOUT = 3000;
    public static Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private int count;

    private String targetIP;
    private int targetPort;
    private Bootstrap bootstrap;
    private List<Thread> waitingthreads;

    public Connection(String targetIP, int targetPort, Bootstrap bootstrap) {
        this.targetIP = targetIP;
        this.targetPort = targetPort;
        this.bootstrap = bootstrap;
        this.channel_rwlock = new ReentrantReadWriteLock();
        this.thread_rwlock = new ReentrantReadWriteLock();
        this.waitingthreads = new ArrayList<Thread>();
    }

    public Connection() {
    }

    public Channel getChannel() throws RpcServiceDisconnectException{
    	if(count>DEFAULT_RECONNECT_TRY){
    		LOGGER.debug("连续重连次数超过{}次，服务器关闭了，不再生成channel",DEFAULT_RECONNECT_TRY);
    	}
    	Channel ch = Channel();
    	if(ch!=null){
    		LOGGER.debug("当前线程:{}找到了不为空的channel",Thread.currentThread().toString());
    		return ch; 
    	}else{
    		try{
    			thread_rwlock.writeLock().lock();
    			LOGGER.debug("当前线程:{}进入wait等待channel队列,最多等待{}毫秒",Thread.currentThread().toString(),DEFAULT_CONNECT_TIMEOUT*5);
    			ch = Channel();
    			if(ch!=null)return ch;
    			
    			waitingthreads.add(Thread.currentThread());
    		}finally{
    			thread_rwlock.writeLock().unlock();
    		}
    		long nanos = TimeUnit.MILLISECONDS.toNanos(DEFAULT_CONNECT_TIMEOUT*5);
    		LockSupport.parkNanos(Thread.currentThread(), nanos);
    		LOGGER.debug("当前线程:{}被eventloop从waitchannel队列唤醒,然后查channel是否更新",Thread.currentThread().toString());
    		ch = Channel();
    		if(ch!=null){
    			LOGGER.debug("当前线程:{}经过等待之后等到了不为null的channel:{}",Thread.currentThread().toString(),ch);
    			return ch;
    		}else{
    			LOGGER.debug("当前线程:{}经过等待之后也没有等到能用的channel",Thread.currentThread().toString());
    			String erromsg = "调用客户端的业务线程遭遇远程服务若干次掉线异常："+Thread.currentThread().toString();
    			throw new RpcServiceDisconnectException(erromsg);
    		}
    	}
    }
    public Channel Channel() {
    	
    	try{
    		LOGGER.debug("thread:{} in Connection.getChannel trying readlock",
    				Thread.currentThread().toString());
    		channel_rwlock.readLock();
    		
    		return channel;
    	}finally{
    		channel_rwlock.readLock().unlock();
    	}
    }

    public void unbind() {
    	try{
    		LOGGER.debug("thread:{} unbind channel:{} Connection",
    				Thread.currentThread().toString(),channel);
    		channel_rwlock.writeLock().lock();
    		channel=null;
    		count=0;
    	}finally{
    		channel_rwlock.writeLock().unlock();
    	}
        
    }

    public void bind(Channel channel) {
    	try{
    		channel_rwlock.writeLock().lock();
    		this.channel = channel;
    		this.count = 0;
    	}finally{
    		channel_rwlock.writeLock().unlock();
    	}
    	try{
    		thread_rwlock.writeLock().lock();
    		for(Thread t : this.waitingthreads){
    			LockSupport.unpark(t);
    		}
    		this.waitingthreads.clear();
    	}finally{
    		thread_rwlock.writeLock().unlock();
    	}
    }

    public void addRetryCount() {
        this.count += 1;
    }

    public int getCount() {
        return count;
    }

    public String getTargetIP() {
        return targetIP;
    }

    public int getTargetPort() {
        return targetPort;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }
}

