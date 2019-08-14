package RpcClient;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import springutils.SpringContextStatic;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import MessageUtils.Uniformconfig;
import RpcCodec.RpcDecoder;
import RpcCodec.RpcEncoder;
import configutils.NormalConfig;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	
    private static ChannelHandler clientHandler = new ClientHandler();
    private ConnectionWatchDog connectionWatchDog;
    private Uniformconfig uniformconfig = null;

    public ClientChannelInitializer(ReConnectionListener reConnectionListener) {

        this.connectionWatchDog = new ConnectionWatchDog(reConnectionListener);
        this.uniformconfig = (Uniformconfig) SpringContextStatic.getBean("uniformConfig");
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline.addLast(new IdleStateHandler(0, uniformconfig.getClientWriteIdle(), 0, TimeUnit.MICROSECONDS))
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
