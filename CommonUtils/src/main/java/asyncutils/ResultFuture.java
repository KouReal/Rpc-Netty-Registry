package asyncutils;


import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ResultFuture<T> implements Future<T> {
	Logger LOGGER = LoggerFactory.getLogger(ResultFuture.class);

    /**
     * get方法的最大等待时间
     */
    public static int MAX_WAIT_MS = 10000;

    /**
     * 表示异步调用是否完成
     */
    private volatile boolean done = false;
    private volatile boolean cancelled = false;
    
    //创建时间, date+MAX_WAIT_MS是最晚销毁日期
    private Date date;

    private Thread waitthread;
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    /**
     * 返回响应
     */
    private Object result;

    private String futureid;

    public ResultFuture(){}
    
    public ResultFuture(String futureid) {
    	this.date = new Date();
    	this.futureid = futureid;
    }
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
    	LOGGER.info("resultfuture被取消，唤醒在此等待的线程：{}",this.waitthread);
        cancelled = true;
        LockSupport.unpark(this.waitthread);
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
    public T get(){
        return this.get(MAX_WAIT_MS, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("unchecked")
	@Override
    public T get(long timeout, TimeUnit unit){
    	if(isDone()){
    		return (T)result;
    	}else{
    		long nanos = unit.toNanos(timeout);
    		try {
				rwlock.writeLock().lock();
				if(isDone()){
					return (T)result;
				}
				this.waitthread = Thread.currentThread();
			} finally {
				// TODO: handle finally clause
				rwlock.writeLock().unlock();
			}
    		LockSupport.parkNanos(nanos);
    		LOGGER.info("resultfuture时间到,唤醒在此等待的线程{}",this.waitthread);
    		if (isDone()) {
                return (T)result;
            }
    		return null;
            
    	}
    }

    /**
     * ResultFuture获得LenPreMsg后调用此方法
     *
     * @param result
     */
    public void done(Object result) {
        this.result = result;
        
        this.done = true;
        if(this.waitthread != null){
        	LockSupport.unpark(this.waitthread);
        }
        
    }

    public String getFutureid() {
        return futureid;
    }
	@Override
	public String toString() {
		return "ResultFuture [done=" + done + ", cancelled=" + cancelled + ", waitthread="
				+ waitthread + ", rwlock=" + rwlock + ", result=" + result + ", futureid=" + futureid + "]";
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

    
}
