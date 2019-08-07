package RegistryClient;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import RegistryCodec.RegistryDecoder;
import RegistryCodec.RegistryEncoder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	@Autowired
	private ConfigFuture configFuture;
	
	@Autowired
	private TokenTask tokenTask;
	
    private ChannelHandler clientHandler = new ClientHandler(configFuture,tokenTask);
    private ConnectionWatchDog connectionWatchDog;

    public ClientChannelInitializer(ReConnectionListener reConnectionListener) {

        this.connectionWatchDog = new ConnectionWatchDog(reConnectionListener);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline.addLast(new IdleStateHandler(0, 8000, 0, TimeUnit.MILLISECONDS))
                //入方向解码
                .addLast(new RegistryDecoder())
                //出方向编码
                .addLast(new RegistryEncoder())
                //前置连接监视处理器
                .addLast(connectionWatchDog)
                //业务处理
                .addLast(clientHandler);
    }
}
