package asyncutils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;

public class FutureCache {
	private static Logger logger = LoggerFactory.getLogger(FutureCache.class);

	public static ThreadLocal<Map<Channel, Map<String, ResultFuture<?>>>> CACHE = new ThreadLocal<>();
	public static ThreadLocal<Timer> timer = new ThreadLocal<>();
	/**
	 * 调用之后在eventLoop的CACHE中添加future
	 *
	 * @param channel
	 * @param future
	 */
	public static void set(EventLoop eventLoop, Channel channel, ResultFuture<?> future) {

		if (future == null) {
			return;
		}
		eventLoop.execute(() -> {
//        	logger.info("futurecache add future:{} with eventloop{}",future,eventLoop);
			Map<Channel, Map<String, ResultFuture<?>>> cache = checkCache();
			Map<String, ResultFuture<?>> futuremap = cache.computeIfAbsent(channel,
					k -> new HashMap<String, ResultFuture<?>>());
			futuremap.put(future.getFutureid(), future);

		});
	}
	
	public static void checkoldfuture(EventLoop eventLoop){
    	eventLoop.execute(()->{
    		Timer t = checktimer();
    		logger.info("eventloop:{},ThreadLocol->checkoldfuture:{}",eventLoop,t);
    		t.schedule(new TimerTask() {
				@Override
				
				public void run() {
					
					eventLoop.execute(()->{
						logger.info("scheduling......eventloop:{},ThreadLocol->checkoldfuture:{}",eventLoop,t);
						Map<Channel, Map<String, ResultFuture<?>>> cache = checkCache();
						for(Map.Entry<Channel, Map<String, ResultFuture<?>>> entry : cache.entrySet() ){
							if(entry.getKey()!=null){
								Iterator<Entry<String, ResultFuture<?>>> it = entry.getValue().entrySet().iterator();
								while(it.hasNext()) {
									Entry<String, ResultFuture<?>> temp = it.next();
									Long live = System.currentTimeMillis()-temp.getValue().getDate().getTime();
									if(live > ResultFuture.MAX_WAIT_MS) {
										it.remove();
									}
								}
							}
						}
					});
					
				}
			}, 0, ResultFuture.MAX_WAIT_MS);
    	});
    }
	public static Timer checktimer(){
    	Timer t = timer.get();
        if (t == null) {
            t = new Timer();
            timer.set(t);
        }
        return t;
    }
	/**
	 * 调用异常时清除eventLoop中的future
	 *
	 * @param channel
	 * @param future
	 *//*
		 * public static void removefuture(EventLoop eventLoop, Channel channel,
		 * ResultFuture<?> future) { eventLoop.execute(() -> { Map<Channel,
		 * Map<String,ResultFuture<?>>> cache = checkCache(); List<ResultFuture<?>>
		 * futurelist = cache.get(channel); if(futurelist == null){ cache.put(channel,
		 * new ArrayList<>(FUTURE_LIST_INIT_SIZE)); }else{ futurelist.remove(future); }
		 * 
		 * }); }
		 */

	/**
	 * clienthandler收到结果时返回eventLoop中的future
	 *
	 * @param channel
	 * @param id
	 */
	public static ResultFuture<?> getfuture(Channel channel, String id) {
		Map<Channel, Map<String, ResultFuture<?>>> cache = checkCache();

		Map<String, ResultFuture<?>> futuremap = cache.computeIfAbsent(channel, k -> new HashMap<>());
		return futuremap.remove(id);
	}

	/**
	 * 当channel关闭时，清除eventLoop中channel对应的所有future
	 *
	 * @param channel
	 */
	public static void removeChannel(Channel channel) {
		Map<Channel, Map<String, ResultFuture<?>>> cache = checkCache();
		cache.remove(channel);
	}

	/**
	 * 清除eventLoop中所有缓存
	 */
	public static void removeAll() {
		CACHE.remove();
	}

	private static Map<Channel, Map<String, ResultFuture<?>>> checkCache() {
		Map<Channel, Map<String, ResultFuture<?>>> cache = CACHE.get();
		if (cache == null) {
			cache = new HashMap<>();
			CACHE.set(cache);
		}
		return cache;
	}
}
