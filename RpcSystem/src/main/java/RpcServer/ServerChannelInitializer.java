package RpcServer;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.Uniformconfig;
import RpcCodec.RpcDecoder;
import RpcCodec.RpcEncoder;
import configutils.NormalConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import springutils.SpringContextStatic;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	private static Logger log = LoggerFactory.getLogger(ServerChannelInitializer.class);
    private ChannelHandler serverHandler;
    
    private Uniformconfig uniformconfig;

    public ServerChannelInitializer(ChannelHandler serverHandler) {

        this.serverHandler = serverHandler;
        this.uniformconfig = ((NormalConfig)SpringContextStatic.getBean("normalConfig")).getUniformconfig();
        log.info("serverinitializer get uniformconfig:{}",uniformconfig);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline
        		.addLast(new IdleStateHandler(uniformconfig.getServerReadIdle(),0,0,TimeUnit.MILLISECONDS))
        		//入方向编码
                
                //出方向解码
                .addLast(new RpcEncoder())
                .addLast(new RpcDecoder())
                .addLast(new ServerConnectionHandler())
                //业务处理
                .addLast(serverHandler);
    }
}
