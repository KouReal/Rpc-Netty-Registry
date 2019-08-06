package RpcClient;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import RpcCodec.RpcDecoder;
import RpcCodec.RpcEncoder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static ChannelHandler clientHandler = new ClientHandler();
    private ConnectionWatchDog connectionWatchDog;

    public ClientChannelInitializer(ReConnectionListener reConnectionListener) {

        this.connectionWatchDog = new ConnectionWatchDog(reConnectionListener);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline.addLast(new IdleStateHandler(0, RpcProxy.CONNECTION_WRITE_IDLE, 0, TimeUnit.MILLISECONDS))
                //入方向解码
                .addLast(new RpcDecoder())
                //出方向编码
                .addLast(new RpcEncoder())
                //前置连接监视处理器
                .addLast(connectionWatchDog)
                //业务处理
                .addLast(clientHandler);
    }
}
