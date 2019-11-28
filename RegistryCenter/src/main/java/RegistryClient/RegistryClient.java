package RegistryClient;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import RegistryParamConfigUtil.ParamConfig;
import RegistryThreadUtil.NamedThreadFactory;
import asyncutils.FutureCache;
import asyncutils.ResultFuture;
import exceptionutils.ProtocolException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.ProtocolMap;
import springutils.SpringContextStatic;
import static java.util.Arrays.asList;


@Component("registryClient")
@DependsOn(value={"springContextStatic","tokenTask"})

public class RegistryClient {
	/*private String serverip;
	
    private int serverport;*/
    
    @Autowired
    private ParamConfig paramConfig;




	private static final Logger LOGGER = LoggerFactory.getLogger(RegistryClient.class);


    /**
     * writeAndFlush（）实际是提交一个task到EventLoopGroup，所以channel是可复用的
     */
    private Connection connection = new Connection();

    /**
     * 配置客户端 NIO 线程组
     */
    private static EventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("registry-client", false));
    /**
     * 创建并初始化 Netty 客户端 Bootstrap 对象
     */
    private static Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
            //禁用nagle算法
            .option(ChannelOption.TCP_NODELAY, true);

    private static List<Header> protocol_whitelist = asList(Header.heart_beat,Header.reg_discover,Header.reg_normalconfig,Header.reg_tokenconfig);
    


    @PostConstruct
    private void init() throws ProtocolException {
    	ProtocolMap.setmap();
    	LOGGER.info("SpringContextStatic：{}",SpringContextStatic.getApplicationContext());
        connection = new Connection(paramConfig.getServerip(), paramConfig.getServerport(), bootstrap);
        ReConnectionListener reConnectionListener = new ReConnectionListener(connection);
        RegistryClient.bootstrap.handler(new ClientChannelInitializer(reConnectionListener,protocol_whitelist));
        ChannelFuture future = bootstrap.connect(paramConfig.getServerip(), paramConfig.getServerport());
		connection.bind(future.channel());
		LOGGER.info("registry 启动连接 serverport:{}",paramConfig.getServerport());
        future.awaitUninterruptibly();
        
		try {
			future.sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    public void invokewithfuture(LenPreMsg msg, ResultFuture<?> resultFuture){

        Channel channel = null;
		try {
			channel = this.getChannel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("找不到可用的channel");
			e.printStackTrace();
		}
        FutureCache.set(channel.eventLoop(), channel, resultFuture);
        
        channel.writeAndFlush(msg);
        LOGGER.info("registryclient send msg:{} with future:{}",msg,resultFuture);
        
    }

    public Channel getChannel() throws Exception {
        Channel channel = connection.getChannel();
        
        return channel;
    }


	/*public void discover(RegDiscover reg_dis) throws Exception {
		LenPreMsg msg = new LenPreMsg(Header.reg_discover,1,reg_dis);
		Channel channel = this.getChannel();
        channel.writeAndFlush(msg);
        LOGGER.info("registryclient send discover msg:{} with channel:{}",msg,channel);
	}*/
}
