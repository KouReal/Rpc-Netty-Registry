package RegistryServer;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RegistryCodec.RegistryDecoder;
import RegistryCodec.RegistryEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	Logger logger = LoggerFactory.getLogger(ServerChannelInitializer.class);

    private ChannelHandler serverHandler;

    public ServerChannelInitializer(ChannelHandler serverHandler) {

        this.serverHandler = serverHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
    	logger.info("registryserver initializeer");
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline
        		.addLast(new IdleStateHandler(10,0,0,TimeUnit.SECONDS))
        		//入方向解码
                
                //出方向编码
                .addLast(new RegistryEncoder())
                .addLast(new RegistryDecoder())
                .addLast(new ServerConnectionHandler())
                //业务处理
                .addLast(serverHandler);
    }
}
