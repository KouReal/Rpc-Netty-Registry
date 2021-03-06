package RpcServer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import exceptionutils.ProtocolException;
import exceptionutils.RpcErrorException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import protocolutils.Header;
import protocolutils.NormalConfig;
import protocolutils.ProtocolMap;
import rpcutils.NamedThreadFactory;
import static java.util.Arrays.asList;

@Component("rpcServer")
@DependsOn(value={"normalConfig","springContextStatic","serviceProxy"})
public class RpcServer{

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);


    @Autowired
    private NormalConfig normalConfig;
    

    /**
     * Netty 的连接线程池
     */
    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1, new NamedThreadFactory(
            "Rpc-netty-server-boss", false));
    /**
     * Netty 的Task执行线程池
     */
    private static EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2,
            new NamedThreadFactory("Rpc-netty-server-worker", true));

    /**
     * 用户线程池，用于处理实际rpc业务
     */
    private static ExecutorService threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1000), new NamedThreadFactory());
    
    private static List<Header> protocol_whitelist = asList(Header.heart_beat,Header.rpc_request);
    
    /**
     * 启动 Netty RPC服务器服务端
     * @throws RpcErrorException 
     * @throws ProtocolException 
     */
    @PostConstruct
    private void doRunServer() throws RpcErrorException{
    	int port = normalConfig.getServiceAddr().getPort();
//    	ProtocolMap.setmap();
      
           try {
                //创建并初始化 Netty 服务端辅助启动对象 ServerBootstrap
                ServerBootstrap serverBootstrap = RpcServer.this.initServerBootstrap(bossGroup, workerGroup);
                //绑定对应ip和端口，同步等待成功
                ChannelFuture future = serverBootstrap.bind(port).sync();
                LOGGER.info("rpc server 已启动，端口：{}", port);
                //等待服务端监听端口关闭
                future.channel().closeFuture().sync();
            } catch (InterruptedException i) {
                LOGGER.error("rpc server 出现异常，端口：{}, cause:", port, i.getMessage());
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
                .childHandler(new ServerChannelInitializer(new RpcServiceHandler(threadPool),protocol_whitelist));
    }


}