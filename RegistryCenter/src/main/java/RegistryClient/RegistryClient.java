package RegistryClient;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import MessageUtils.RegistryMessage;
import MessageUtils.RpcMessage;
import MessageUtils.RpcRequest;
import RegistryUtil.NamedThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


@Component
public class RegistryClient {
	@Value("${registry.serverip}")
	private String targetIP;
	
	@Value("${registry.serverport}")
    private int targetPort;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistryClient.class);


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

    


    @PostConstruct
    private void init() {
        connection = new Connection(targetIP, targetPort, bootstrap);
        ReConnectionListener reConnectionListener = new ReConnectionListener(connection);
        RegistryClient.bootstrap.handler(new ClientChannelInitializer(reConnectionListener));
        ChannelFuture future = bootstrap.connect(targetIP,targetPort);
		future.awaitUninterruptibly();
		try {
			future.sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    public void sendtocenter(RegistryMessage msg) throws Exception {

        Channel channel = this.getChannel();
        channel.writeAndFlush(msg);
        LOGGER.info("registryclient send msg:{} with channel:{}",msg,channel);
 
        
    }

    public Channel getChannel() throws Exception {
        Channel channel = connection.getChannel();
        
        return channel;
    }
}
