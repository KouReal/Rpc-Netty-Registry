/*package RpcTrans_deprecated;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import protocolutils.RpcResponse;

public class RpcFuture implements Future<RpcResponse> {
	Logger LOGGER = LoggerFactory.getLogger(RpcFuture.class);

    *//**
     * get方法的最大等待时间
     *//*
    public static int MAX_WAIT_MS = 10000;

    *//**
     * 表示异步调用是否完成
     *//*
    private volatile boolean done = false;
    private volatile boolean cancelled = false;

    private Thread thread;
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    *//**
     * 返回响应
     *//*
    private RpcResponse rpcResponse;

    private final String requestId;

    public RpcFuture(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
    	LOGGER.info("rpc调用服务被取消，rpc客户端尝试重连，唤醒在此等待的线程：{}",this.thread);
        cancelled = true;
        LockSupport.unpark(this.thread);
        return cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public RpcResponse get() throws InterruptedException, ExecutionException {
        RpcResponse rpcResponse;
        try {
            rpcResponse = this.get(MAX_WAIT_MS, TimeUnit.MILLISECONDS);
        } catch (TimeoutException t) {
            throw new ExecutionException(t.getCause());
        }
        return rpcResponse;
    }

    @Override
    public RpcResponse get(long timeout, TimeUnit unit) 
    		throws InterruptedException,
    		ExecutionException, 
    		TimeoutException {
    	if(isDone()){
    		return rpcResponse;
    	}else{
    		long nanos = unit.toNanos(timeout);
    		try {
				rwlock.writeLock().lock();
				if(isDone()){
					return rpcResponse;
				}
				this.thread=Thread.currentThread();
			} finally {
				// TODO: handle finally clause
				rwlock.writeLock().unlock();
			}
    		LockSupport.parkNanos(nanos);
    		if (done) {
                return rpcResponse;
            }
            LOGGER.info("调用rpc服务超时,唤醒在此等待的线程{}",this.thread);
            throw new TimeoutException("调用超时");
    	}
    }

    *//**
     * RpcFuture获得RpcResponse后调用此方法
     *
     * @param rpcResponse
     *//*
    public void done(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
        this.done = true;
        if(this.thread!=null){
        	LockSupport.unpark(this.thread);
        }
        
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return "RpcFuture{" +
                "done=" + done +
                ", cancelled=" + cancelled +
                ", thread=" + thread +
                ", rpcResponse=" + rpcResponse +
                ", requestId=" + requestId +
                '}';
    }
}
*/