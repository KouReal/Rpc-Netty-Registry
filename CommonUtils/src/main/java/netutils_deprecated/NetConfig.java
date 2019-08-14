/*package netutils_deprecated;

import java.security.KeyStore.PrivateKeyEntry;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.jmx.export.notification.NotificationPublisher;
import configutils.NormalConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import springutils.SpringContextUtil;

@Configuration
@SuppressWarnings("unused")
public class NetConfig extends SpringContextUtil{
	
	public NamedThreadFactory namedThreadFactory(String name,Boolean daemon){
		return new NamedThreadFactory(name,daemon);
	}

	public ExecutorService threadPool(NamedThreadFactory namedThreadFactory){
		return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
	            Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<>(1000), namedThreadFactory);
	}

	public EventLoopGroup eventLoopGroup(int n, ThreadFactory tf){
		return new NioEventLoopGroup(n, tf);
	}

	public ChannelInitializer<SocketChannel> channelInitializer(List<Class<? extends ChannelHandler>> chrlist){
		return new ChannelInitializer<SocketChannel>(){
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				for (Class<? extends ChannelHandler> chr : chrlist) {
					pipeline.addLast((ChannelHandler)SpringContextUtil.getBean(chr));
				}
			}
			
		};
	}

	@Autowired
	private NetDataConfig ndc;
	

	public Bootstrap getClientBootStrap(String clientname,ChannelInitializer<SocketChannel> chinitial){
		LocalConfig lcf = ndc.getConfigmap().get(clientname);
		NamedThreadFactory nametf = namedThreadFactory(lcf.getThreadname(),lcf.isDaemon());
		EventLoopGroup worker = eventLoopGroup(lcf.getWorkernum(),nametf);
		return new Bootstrap()
			.group(worker)
			.channel(NioSocketChannel.class)
			.handler(chinitial);

	}

	public ServerBootstrap getServerBootStrap(String servername, ChannelInitializer<SocketChannel> chinitial){
		LocalConfig lcf = ndc.getConfigmap().get(servername);
		NamedThreadFactory nametf = namedThreadFactory(lcf.getThreadname(),lcf.isDaemon());
		EventLoopGroup boss = eventLoopGroup(lcf.getBossnum(),nametf);
		EventLoopGroup worker = eventLoopGroup(lcf.getWorkernum(),nametf);
		return new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(chinitial);
    
	}
	
	
	
}
*/