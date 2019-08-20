package RegistryServer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lenprecodec.LenPreMsgDecoder;
import lenprecodec.LenPreMsgEncoder;
import lenprecodec.MessageFilter;
import protocolutils.Header;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	Logger logger = LoggerFactory.getLogger(ServerChannelInitializer.class);

    private ChannelHandler serverHandler;
    private MessageFilter messageFilter;

    public ServerChannelInitializer(ChannelHandler serverHandler,List<Header> whitelist) {
    	this.messageFilter = new MessageFilter(whitelist);
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
        		.addLast(new LenPreMsgDecoder())
                //出方向编码
                .addLast(new LenPreMsgEncoder())
                
                .addLast(messageFilter)
                
                .addLast(new ServerConnectionHandler())
                //业务处理
                .addLast(serverHandler);
    }
}
