package httpserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import configutils.ServiceAddr;
import exceptionutils.ProtocolException;
import exceptionutils.RpcErrorException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import protocolutils.NormalConfig;
import protocolutils.ProtocolMap;
import rpcutils.NamedThreadFactory;
import springutils.SpringContextStatic;

@Component("httpServer")
//@DependsOn("httpserverconfig")
@DependsOn(value={"normalConfig","springContextStatic","rpcProxy"})
//@DependsOn(value={"httpserverconfig","springContextStatic"})
public class HttpServer{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);


//    @Autowired
    private NormalConfig normalConfig;
    

    /**
     * Netty 的连接线程池
     */
    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1, new NamedThreadFactory(
            "httpserver-boss", false));
    /**
     * Netty 的Task执行线程池
     */
    private static EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2,
            new NamedThreadFactory("httpserver-worker", true));

    public HttpServer() {
		this.normalConfig = (NormalConfig) SpringContextStatic.getBean("normalConfig");
		try {
			doRunServer();
		} catch (RpcErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**
     * 启动 Netty RPC服务器服务端
     * @throws RpcErrorException 
     * @throws ProtocolException 
     */
//    @PostConstruct
    private void doRunServer() throws RpcErrorException, ProtocolException {
    	LOGGER.info("starting httpserver");
    	ProtocolMap.setmap();
       ServiceAddr serviceAddr = normalConfig.getServiceAddr();
       int port = serviceAddr.getPort();
       HttpMessageUtil.httpaddr = "localhost"+":"+port;
           try {
                //创建并初始化 Netty 服务端辅助启动对象 ServerBootstrap
                ServerBootstrap serverBootstrap = HttpServer.this.initServerBootstrap(bossGroup, workerGroup);
                //绑定对应ip和端口，同步等待成功
                ChannelFuture future = serverBootstrap.bind(port).sync();
                LOGGER.info("http server 已启动，端口：{}", port);
                //等待服务端监听端口关闭
                future.channel().closeFuture().sync();
            } catch (InterruptedException i) {
                LOGGER.error("httpserver 出现异常，端口：{}, cause:", port, i.getMessage());
            } finally {
                //优雅退出，释放 NIO 线程组
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }

    }

    /**
     * 创建并初始化 Netty 服务端辅助启动对象 ServerBootstrap
     *
     * @param bossGroup
     * @param workerGroup
     * @return
     */
    private ServerBootstrap initServerBootstrap(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new HttpServerInitializer());
        
    }


}