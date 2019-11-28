package RpcClient;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lenprecodec.LenPreMsgDecoder;
import lenprecodec.LenPreMsgEncoder;
import lenprecodec.MessageFilter;
import protocolutils.Header;
import protocolutils.NormalConfig;
import springutils.SpringContextStatic;

import java.util.List;
import java.util.concurrent.TimeUnit;


import configutils.Uniformconfig;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	
    private ChannelHandler clientHandler;
    private ConnectionWatchDog connectionWatchDog;
    private Uniformconfig uniformconfig = null;
    private MessageFilter messageFilter;

    public ClientChannelInitializer(ReConnectionListener reConnectionListener,List<Header> whitelist) {
    	this.messageFilter = new MessageFilter(whitelist);
        this.connectionWatchDog = new ConnectionWatchDog(reConnectionListener);
        
        
        this.uniformconfig = reConnectionListener.getConnection().getUniformconfig();
//        this.uniformconfig = ((NormalConfig)SpringContextStatic.getBean("normalConfig")).getUniformconfig();
        this.clientHandler = new ClientHandler();
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline.addLast(new IdleStateHandler(0, uniformconfig.getClientWriteIdle(), 0, TimeUnit.MILLISECONDS))
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
