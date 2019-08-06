package RegistryServer;

import java.util.concurrent.TimeUnit;

import RegistryCodec.RegistryDecoder;
import RegistryCodec.RegistryEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelHandler serverHandler;

    public ServerChannelInitializer(ChannelHandler serverHandler) {

        this.serverHandler = serverHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline
        		.addLast(new IdleStateHandler(10,0,0,TimeUnit.SECONDS))
        		//入方向编码
                .addLast(new RegistryDecoder())
                //出方向解码
                .addLast(new RegistryEncoder())
                .addLast(new ServerConnectionHandler())
                //业务处理
                .addLast(serverHandler);
    }
}
