package RpcClient;



import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpcutils.NamedThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.RpcMessage;
import MessageUtils.RpcRequest;
import RpcTrans.RpcFuture;
import RpcTrans.RpcFutureCache;


/**
 * RPC 客户端（用于发送 RPC 请求,对于同一目标IP+目标端口，RpcClient唯一）
 *
 * @author jsj
 * @date 2018-10-10
 */
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

    

    public RpcClient(String targetIP, int targetPort) {
        this.targetIP = targetIP;
        this.targetPort = targetPort;
        this.init();
    }

    private void init() {
        connection = new Connection(targetIP, targetPort, bootstrap);
        ReConnectionListener reConnectionListener = new ReConnectionListener(connection);
        RpcClient.bootstrap.handler(new ClientChannelInitializer(reConnectionListener));
        ChannelFuture future = bootstrap.connect(targetIP,targetPort);
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
    public RpcFuture invokeWithFuture(RpcRequest rpcrequest) throws Exception {
        //注册到futureMap
        
        RpcFuture future = new RpcFuture(rpcrequest.getRequestId());
        Channel channel = this.getChannel();
        RpcFutureCache.set(channel.eventLoop(), channel, future);
 
        try {
            //写请求并直接返回
        	RpcMessage rpcMessage = new RpcMessage(header, body)
            channel.writeAndFlush(rpcrequest);
            LOGGER.debug("rpcclient invokewithfuture rpcrequest:{},with channel:{}", rpcrequest.toString(),channel);
        } catch (Exception e) {
        	future.cancel(true);
        	RpcFutureCache.remove(channel.eventLoop(),channel,future);
        	
            throw e;
        }
        return future;
    }

    public Channel getChannel() throws Exception {
        Channel channel = connection.getChannel();
        
        return channel;
    }

    

    
}
