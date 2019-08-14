package RegistryClient;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import RegistryParamConfigUtil.ParamConfig;
import RegistryThreadUtil.NamedThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import springutils.SpringContextStatic;


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

    


    @PostConstruct
    private void init() {
    	LOGGER.info("serverip:{}",paramConfig.getServerip());
    	LOGGER.info("SpringContextStatic：{}",SpringContextStatic.getApplicationContext());
        connection = new Connection(paramConfig.getServerip(), paramConfig.getServerport(), bootstrap);
        ReConnectionListener reConnectionListener = new ReConnectionListener(connection);
        RegistryClient.bootstrap.handler(new ClientChannelInitializer(reConnectionListener));
        ChannelFuture future = bootstrap.connect(paramConfig.getServerip(), paramConfig.getServerport());
		connection.bind(future.channel());
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


	public void discover(String servicename) throws Exception {
		RegistryMessage msg = new RegistryMessage(new Header(1, Header.REGISTRY_DISCOVER), (Object)servicename);
		Channel channel = this.getChannel();
        channel.writeAndFlush(msg);
        LOGGER.info("registryclient send msg:{} with channel:{}",msg,channel);
	}
}
