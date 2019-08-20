package asyncutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;

public class FutureCache {
	private static Logger logger = LoggerFactory.getLogger(FutureCache.class);
    public static int FUTURE_LIST_INIT_SIZE = 128;
    public static int CHANNEL_INIT_SIZE = 4;

    public static ThreadLocal<Map<Channel, List<ResultFuture<?>>>> CACHE = new ThreadLocal<>();



    /**
     * 调用之后在eventLoop的CACHE中添加future
     *
     * @param channel
     * @param future
     */
    public static void set(EventLoop eventLoop, Channel channel, ResultFuture<?> future) {
    	if(future==null){
    		return ;
    	}
        eventLoop.execute(() -> {
//        	logger.info("futurecache add future:{} with eventloop{}",future,eventLoop);
        	Map<Channel, List<ResultFuture<?>>> cache = checkCache();
        	List<ResultFuture<?>> futurelist = cache.computeIfAbsent(channel, k->new ArrayList<>(FUTURE_LIST_INIT_SIZE));
            futurelist.add(future);
        	
        });
    }

    /**
     * 调用异常时清除eventLoop中的future
     *
     * @param channel
     * @param future
     */
    public static void removefuture(EventLoop eventLoop, Channel channel, ResultFuture<?> future) {
        eventLoop.execute(() -> {
        	Map<Channel, List<ResultFuture<?>>> cache = checkCache();
        	List<ResultFuture<?>> futurelist = cache.get(channel);
        	if(futurelist == null){
        		cache.put(channel, new ArrayList<>(FUTURE_LIST_INIT_SIZE));
        	}else{
        		futurelist.remove(future);
        	}
        	
        });
    }

    /**
     * clienthandler收到结果时返回eventLoop中的future
     *
     * @param channel
     * @param id
     */
    public static ResultFuture<?> getfuture(Channel channel, String id) {
    	Map<Channel, List<ResultFuture<?>>> cache = checkCache();
    	
        List<ResultFuture<?>> futurelist = cache.computeIfAbsent(channel, k->new ArrayList<>(FUTURE_LIST_INIT_SIZE));
//        logger.info("get futurelist:{}",futurelist);
        String futureid = null;
        ResultFuture<?> future = null;
    	for(int index=0; index<futurelist.size(); ++index){
//    		logger.info("poll future:{}",futurelist.get(index));
    		futureid = futurelist.get(index).getFutureid();
//    		logger.info("futureid:{},id:{}",futureid,id);
    		if(futureid!=null && futureid.equals(id)){
//    			logger.info("get future:{}",futurelist.get(index));
    			future = futurelist.remove(index);
    			return future;
    		}
    	}
        return future;
    	
    }

    /**
     * 当channel关闭时，清除eventLoop中channel对应的所有future
     *
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        Map<Channel, List<ResultFuture<?>>> cache = checkCache();
        cache.remove(channel);
    }

    /**
     * 清除eventLoop中所有缓存
     */
    public static void removeAll() {
        CACHE.remove();
    }

    private static Map<Channel, List<ResultFuture<?>>> checkCache() {
    	Map<Channel, List<ResultFuture<?>>> cache = CACHE.get();
        if (cache == null) {
            cache = new HashMap<>(CHANNEL_INIT_SIZE);
            CACHE.set(cache);
        }
        return cache;
    }
}
