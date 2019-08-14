/*package httpserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import rpcutils.NamedThreadFactory;

@Configuration
public class StartupConfiguration implements ApplicationContextAware{
	private static ApplicationContext act;
	@Autowired
	private HttpMessageUtil httpMessageUtil;
	
	@Autowired
	private NamedThreadFactory namedThreadFactory;
	
	@Bean(name="executorservice")
	private ExecutorService executorService(){
		return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
	            Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<>(1000), namedThreadFactory);
	}
	
	@Bean(name="httpServerHandler")
	@Scope("prototype")
	public HttpServerHandler httpServerHandler(){
		return new HttpServerHandler(httpMessageUtil, executorService());
	}
	
	@Bean(name="httpServerInitializer")
	@Scope("prototype")
	public ChannelInitializer<SocketChannel> httpServerInitializer(){
		return new ChannelInitializer<SocketChannel>(){

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();  
		        pipeline.addLast("http-decoder",new HttpRequestDecoder());
		        pipeline.addLast("http-aggregator",new HttpObjectAggregator(65536));
		        pipeline.addLast("http-encoder",new HttpResponseEncoder());
		        pipeline.addLast("http-chunked",new ChunkedWriteHandler());
		        pipeline.addLast("fileServerHandler",httpServerHandler());
			}
			
		};
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		act = applicationContext;
	}
	public static  Object getBean(String name){
		return act.getBean(name);
	}
}
*/