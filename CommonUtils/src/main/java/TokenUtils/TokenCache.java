package TokenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import protocolutils.Token;
import springutils.SpringContextStatic;
import protocolutils.NormalConfig;


@Component
@DependsOn("normalConfig")
public class TokenCache {
	//可以转移到springboot的configurations类中
		public static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);
		public ReentrantReadWriteLock  rwlock = new ReentrantReadWriteLock ();
		public static int TOKEN_LIFE = 10000;
		
		
		public static List<Token> tokencache;
		@PostConstruct
		public void init(){	
			this.rwlock = new ReentrantReadWriteLock ();
			tokencache = new ArrayList<Token>();
			TOKEN_LIFE = ((NormalConfig)SpringContextStatic.getBean("normalConfig")).getUniformconfig().getTokenLife();
			ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);  
	          scheduledThreadPool.schedule(new Runnable() {  
	           public void run() {  
	            	checkoldtoken();
	           }  
	          }, TOKEN_LIFE, TimeUnit.MILLISECONDS);  
		}

		public Token gettokenbyid(String id){
			if(tokencache==null||id==null||id.equals("")){
				return null;
			}
			for (Token token : tokencache) {
				if(id.equals(token.getTid())){
					return token;
				}
			}
			return null;
		}

		public boolean authtoken(String tokenid){
//			LOGGER.info("auth token");
			if(tokenid == null)return false;
			try{
				rwlock.readLock().lock();
//				LOGGER.info("get readlock");
				if(tokencache==null)return false;
				for(Token token : tokencache){
					if(tokenid.equals(token.getTid())){
						return true;
					}
				}
//				LOGGER.info("release readlock");
				return false;
			}finally{
				rwlock.readLock().unlock();
			}	
		}

		public boolean addtoken(Token token){
//			LOGGER.info("addtoken, token:{}",token);
			if(authtoken(token.getTid())){
//				LOGGER.info("the Token with id={} is already exsists in the {} service",token.getTid());
				return false;
			}
			try{
//				LOGGER.info("try");
				rwlock.writeLock().lock();
//				LOGGER.info("get writelock");
				tokencache.add(token);
//				LOGGER.info("add with tokenid:{}",token.getTid());
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
