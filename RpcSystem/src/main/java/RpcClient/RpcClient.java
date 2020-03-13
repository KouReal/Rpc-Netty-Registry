package RpcClient;



import io.netty.bootstrap.Bootstrap;
import static java.util.Arrays.asList;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.NormalConfig;
import rpcutils.NamedThreadFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asyncutils.FutureCache;
import asyncutils.ResultFuture;
import exceptionutils.RpcServiceDisconnectException;



public class RpcClient {
    private final String targetIP;
    private final int targetPort;

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);


    /**
     * writeAndFlush（）实际是提交一个task到EventLoopGroup，所以channel是可复用的
     */
    private Connection connection = new Connection();

    /**
     * 配置客户端 NIO 线程组
     */
    private  EventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("Rpc-netty-client", false));
    /**
     * 创建并初始化 Netty 客户端 Bootstrap 对象
     */
    private  Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
            //禁用nagle算法
            .option(ChannelOption.TCP_NODELAY, true);
    private static List<Header> protocol_whitelitst = asList(Header.heart_beat,Header.rpc_response);
    

    public RpcClient(String targetIP, int targetPort) {
    	bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class);


        this.targetIP = targetIP;
        this.targetPort = targetPort;
        this.init(null);
    }
    
    public RpcClient(String targetIP, int targetPort, NormalConfig normalConfig) {
    	bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class);
    	
        this.targetIP = targetIP;
        this.targetPort = targetPort;
        this.init(normalConfig);
    }

    private void init(NormalConfig normalConfig) {
    	if(normalConfig==null) {
    		connection = new Connection(targetIP, targetPort, bootstrap);
    	}else {
    		connection = new Connection(targetIP, targetPort, bootstrap,normalConfig);
    	}
        ReConnectionListener reConnectionListener = new ReConnectionListener(connection);
        bootstrap.handler(new ClientChannelInitializer(reConnectionListener,protocol_whitelitst));
        ChannelFuture future = bootstrap.connect(targetIP,targetPort);
        connection.bind(future.channel());
        Integer soLingerOption = future.channel().config().getOption(ChannelOption.SO_LINGER);
        LOGGER.info("soLingerOption={}",soLingerOption);
        EventLoop eventLoop = future.channel().eventLoop();
        LOGGER.info("client startup: eventloop:{}",eventLoop);
        FutureCache.checkoldfuture(eventLoop);
		future.awaitUninterruptibly();
		try {
			future.sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void Stop() {
    	LOGGER.info("正在关闭RpcClient....");
    	EventLoop eventLoop = getChannel().eventLoop();
    	eventLoop.execute(()->{
    		FutureCache.removeAll();
        	FutureCache.closescheduler();
    		LOGGER.info("已经关闭FutureCache的scheduler...");
    	});
    	eventLoop.shutdownGracefully();
    	
    	if(group != null && !group.isShutdown()) {
    		group.shutdownGracefully();
    		group = null;
    	}
    	LOGGER.info("RpcClient已关闭....");
    }


    public void invokeWithFuture(LenPreMsg msg, ResultFuture<?> future){
        Channel channel = this.getChannel();
        
        //注册到futureMap
        FutureCache.set(channel.eventLoop(), channel, future);
        //写请求并直接返回
        LOGGER.info("rpcclient invokewithfuture lenpremsg:{},with future:{}", msg,future);
        channel.writeAndFlush(msg);
//        channel.eventLoop().execute(()->{
//        	channel.writeAndFlush(msg);
//        });
    }

    public Channel getChannel(){
        Channel channel = null;
		try {
			channel = connection.getChannel();
		} catch (RpcServiceDisconnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return channel;
    }

    

    
}
