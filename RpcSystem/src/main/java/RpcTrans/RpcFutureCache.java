package RpcTrans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;

public class RpcFutureCache {

    public static int FUTURE_LIST_INIT_SIZE = 128;
    public static int CHANNEL_INIT_SIZE = 4;

    public static ThreadLocal<Map<Channel, List<RpcFuture>>> CACHE = new ThreadLocal<>();



    /**
     * 在eventLoop的CACHE中添加future
     *
     * @param channel
     * @param future
     */
    public static void set(EventLoop eventLoop, Channel channel, RpcFuture future) {
        eventLoop.execute(() -> {
        	Map<Channel, List<RpcFuture>> cache = checkCache();
        	List<RpcFuture> futurelist = cache.computeIfAbsent(channel, k->new ArrayList<>(FUTURE_LIST_INIT_SIZE));
            futurelist.add(future);
        	
        });
    }

    /**
     * 调用异常时清除eventLoop中的future
     *
     * @param channel
     * @param future
     */
    public static void remove(EventLoop eventLoop, Channel channel, RpcFuture future) {
        eventLoop.execute(() -> {
        	Map<Channel, List<RpcFuture>> cache = checkCache();
        	List<RpcFuture> futurelist = cache.get(channel);
        	if(futurelist == null){
        		cache.put(channel, new ArrayList<>(FUTURE_LIST_INIT_SIZE));
        	}else{
        		futurelist.remove(future);
        	}
        	
        });
    }

    /**
     * 调用异常时清除eventLoop中的future
     *
     * @param channel
     * @param id
     */
    public static RpcFuture removeFuture(Channel channel, String id) {
        Map<Channel, List<RpcFuture>> cache = checkCache();
        List<RpcFuture> futurelist = cache.get(channel);
        if (futurelist == null) {
            cache.put(channel, new ArrayList<>(FUTURE_LIST_INIT_SIZE));
        } else {
        	String requestid = null;
        	int index = 0;
        	for(index=0; index<futurelist.size(); ++index){
        		requestid = futurelist.get(index).getRequestId();
        		if(requestid!=null && requestid.equals(id)){
        			break;
        		}
        	}
        	return futurelist.remove(index);
        }
        return null;
    }

    /**
     * 当channel关闭时，清除eventLoop中channel对应的所有future
     *
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        Map<Channel, List<RpcFuture>> cache = checkCache();
        cache.remove(channel);
    }

    /**
     * 清除eventLoop中所有缓存
     */
    public static void removeAll() {
        CACHE.remove();
    }

    private static Map<Channel, List<RpcFuture>> checkCache() {
    	Map<Channel, List<RpcFuture>> cache = CACHE.get();
        if (cache == null) {
            cache = new HashMap<>(CHANNEL_INIT_SIZE);
            CACHE.set(cache);
        }
        return cache;
    }
}
