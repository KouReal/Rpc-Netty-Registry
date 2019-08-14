package TokenUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import configutils.NormalConfig;


@Component
public class TokenCache {
	//可以转移到springboot的configurations类中
		public static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);
		public ReentrantReadWriteLock  rwlock = new ReentrantReadWriteLock ();
		public static int TOKEN_LIFE = 300000;
		
		
		public List<Token> tokencache;
		@PostConstruct
		public void init(){			
			ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);  
	          scheduledThreadPool.schedule(new Runnable() {  
	           public void run() {  
	            	checkoldtoken();
	           }  
	          }, TOKEN_LIFE, TimeUnit.MILLISECONDS);  
		}


		public boolean authtoken(String tokenid){
			if(tokenid == null)return false;
			try{
				rwlock.readLock().lock();
				for(Token token : tokencache){
					if(tokenid.equals(token.getTid())){
						return true;
					}
				}
				return false;
			}finally{
				rwlock.readLock().unlock();
			}	
		}

		public boolean addtoken(Token token){
			if(authtoken(token.getTid())){
				LOGGER.info("the Token with id={} is already exsists in the {} service",token.getTid());
				return false;
			}
			try{
				rwlock.writeLock().lock();
				tokencache.add(token);
				LOGGER.info("add with tokenid:{}",token.getTid());
				return true;
			}finally{
				rwlock.writeLock().unlock();
			}

		}
		public void checkoldtoken(){
			try{
				rwlock.writeLock().lock();
				for(Token token : tokencache){
					Long live = System.currentTimeMillis() - token.getCreatetime().getTime();
					if(live > TOKEN_LIFE){
						LOGGER.info("tokencache remove old token with id:{}",token.getTid());
						tokencache.remove(token);
					}
				}
			}finally{
				rwlock.writeLock().unlock();
			}

		}
}
