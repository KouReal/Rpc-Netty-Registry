package RegistryClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import RegistryUtil.NamedThreadFactory;
import TokenUtils.Token;
import TokenUtils.TokenCache;

@Component
public class TokenTask {
	@Autowired
	private TokenCache tokenCache;
	
	private Logger log = LoggerFactory.getLogger(TokenTask.class);
	 /**
     * 用户线程池，用于处理实际业务
     */
    private static ExecutorService threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1000), new NamedThreadFactory());
    
    public void settoken(Token token){
    	threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				log.info("tokentask执行添加token任务，线程：{}",Thread.currentThread());
				tokenCache.addtoken(token);
				
			}
		});
    }
}
