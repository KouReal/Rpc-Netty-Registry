package RpcClient;



import io.netty.bootstrap.Bootstrap;
import static java.util.Arrays.asList;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import rpcutils.NamedThreadFactory;

import java.util.List;

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
    private static EventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("Rpc-netty-client", false));
    /**
     * 创建并初始化 Netty 客户端 Bootstrap 对象
     */
    private static Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
            //禁用nagle算法
            .option(ChannelOption.TCP_NODELAY, true);
    private static List<Header> protocol_whitelitst = asList(Header.heart_beat,Header.rpc_response);
    

    public RpcClient(String targetIP, int targetPort) {
        this.targetIP = targetIP;
        this.targetPort = targetPort;
        this.init();
    }

    private void init() {
        connection = new Connection(targetIP, targetPort, bootstrap);
        ReConnectionListener reConnectionListener = new ReConnectionListener(connection);
        RpcClient.bootstrap.handler(new ClientChannelInitializer(reConnectionListener,protocol_whitelitst));
        ChannelFuture future = bootstrap.connect(targetIP,targetPort);
        connection.bind(future.channel());
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

    /**
     * 异步调用
     *
     * @param method
     * @param parameters
     * @return
     * @throws Exception
     */
    public void invokeWithFuture(LenPreMsg msg, ResultFuture<?> future){
        Channel channel = this.getChannel();
        
        //注册到futureMap
        FutureCache.set(channel.eventLoop(), channel, future);
        //写请求并直接返回
        LOGGER.info("rpcclient invokewithfuture lenpremsg:{},with future:{}", msg,future);
        channel.writeAndFlush(msg);
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
