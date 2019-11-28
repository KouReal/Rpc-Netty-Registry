package TokenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
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


@Component("tokenCache")
@DependsOn("normalConfig")
public class TokenCache {
	//可以转移到springboot的configurations类中
		public static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);
		public ReentrantReadWriteLock  rwlock = new ReentrantReadWriteLock ();
		public static int TOKEN_LIFE = 10000;
		public static Timer timer;
		
		
//		public static List<Token> tokencache = new ArrayList<>();
		public static ConcurrentHashMap<String, Token> tokenmap = new ConcurrentHashMap<>();
		@PostConstruct
		public void init(){	
			this.rwlock = new ReentrantReadWriteLock();
			TOKEN_LIFE = ((NormalConfig)SpringContextStatic.getBean("normalConfig")).getUniformconfig().getTokenLife();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					checkoldtoken();
				}
			}, 0, TOKEN_LIFE);
			/*ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);  
	        scheduledThreadPool.schedule(new Runnable() {  
	           public void run() {  
	            	checkoldtoken();
	           }  
	        }, TOKEN_LIFE, TimeUnit.MILLISECONDS); */ 
		}

		public Token gettokenbyid(String id){
			if(tokenmap.isEmpty()||id==null||id.equals("")){
				return null;
			}
			try {
				rwlock.readLock().lock();
				return tokenmap.get(id);
			} finally {
				rwlock.readLock().unlock();
			}
			
		}

		public boolean authtoken(String tokenid){
//			LOGGER.info("auth token");
			if(tokenmap.isEmpty()||tokenid == null){
				return false;
			}
			try{
				rwlock.readLock().lock();
				return tokenmap.containsKey(tokenid);
			}finally{
				rwlock.readLock().unlock();
			}	
		}

		public boolean addtoken(Token token){
			LOGGER.info("addtoken, token:{}",token);
			try{
//				LOGGER.info("try");
				rwlock.writeLock().lock();
				tokenmap.put(token.getTid(), token);
				return true;
			}finally{
				rwlock.writeLock().unlock();
			}

		}
		public void checkoldtoken(){
			if(tokenmap.isEmpty()){
				return;
			}
			try{
				rwlock.writeLock().lock();
				LOGGER.info("tokencache check old token");
				Token token;
				for(Map.Entry<String, Token> entry : tokenmap.entrySet()){
					token = entry.getValue();
					Long live = System.currentTimeMillis() - token.getCreatetime().getTime();
					if(live > TOKEN_LIFE){
						LOGGER.info("tokencache remove old token with id:{}",token.getTid());
						tokenmap.remove(token.getTid());
					}
				}
			}finally{
				rwlock.writeLock().unlock();
			}

		}
}
