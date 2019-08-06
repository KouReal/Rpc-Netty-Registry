package httpserver;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

@Configuration
@PropertySource(value="classpath:conf.properties")
public class HttpServerConfiguration {
	   
	@Value("${boss.thread.count}")  
    private int bossCount;  
  
    @Value("${worker.thread.count}")  
    private int workerCount;  
  
    @Value("${tcp.port}")  
    private int tcpPort;  
  
    @Value("${so.keepalive}")  
    private boolean keepAlive;  
  
    @Value("${so.backlog}")  
    private int backlog;  
    
    @Value("${file.url}")
    private String url;
    
    
    @Autowired  
    @Qualifier("fileServerInitializer")  
    private HttpServerInitializer httpserverinitializer;
    
    
  
    @SuppressWarnings("unchecked")  
    @Bean(name = "serverBootstrap")  
    public ServerBootstrap bootstrap() {  
        ServerBootstrap b = new ServerBootstrap();  
        b.group(bossGroup(), workerGroup())  
                .channel(NioServerSocketChannel.class)  
                .childHandler(new HttpServerInitializer());  
        Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();  
        Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();  
        for (@SuppressWarnings("rawtypes")  
        ChannelOption option : keySet) {  
            b.option(option, tcpChannelOptions.get(option));  
        }  
        return b;  
    }  
  
    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")  
    public NioEventLoopGroup bossGroup() {  
        return new NioEventLoopGroup(bossCount);  
    }  
  
    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")  
    public NioEventLoopGroup workerGroup() {  
        return new NioEventLoopGroup(workerCount);  
    }  
    @Bean(name = "tcpSocketAddress")  
    public InetSocketAddress tcpPort() {  
        return new InetSocketAddress(tcpPort);  
    }  
  
    @Bean(name = "tcpChannelOptions")  
    @Scope("prototype")
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {  
        Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();  
        options.put(ChannelOption.SO_KEEPALIVE, keepAlive);  
        options.put(ChannelOption.SO_BACKLOG, backlog);  
        return options;  
    }  
  
      
}
