package RegistryClient;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lenprecodec.LenPreMsgDecoder;
import lenprecodec.LenPreMsgEncoder;
import lenprecodec.MessageFilter;
import protocolutils.Header;

import java.util.List;
import java.util.concurrent.TimeUnit;



public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	
    private ChannelHandler clientHandler = new ClientHandler();
    private ConnectionWatchDog connectionWatchDog;
    private MessageFilter messageFilter;
    public ClientChannelInitializer(ReConnectionListener reConnectionListener, List<Header> whitelist) {
    	this.messageFilter = new MessageFilter(whitelist);
        this.connectionWatchDog = new ConnectionWatchDog(reConnectionListener);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS))
                //入方向解码
                .addLast(new LenPreMsgDecoder())
                //出方向编码
                .addLast(new LenPreMsgEncoder())
                
                .addLast(messageFilter)
                //前置连接监视处理器
                .addLast(connectionWatchDog)
                //业务处理
                .addLast(clientHandler);
    }
}
