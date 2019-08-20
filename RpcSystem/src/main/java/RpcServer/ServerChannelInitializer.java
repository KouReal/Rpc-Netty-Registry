package RpcServer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import configutils.Uniformconfig;
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

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	private static Logger log = LoggerFactory.getLogger(ServerChannelInitializer.class);
    private ChannelHandler serverHandler;
    
    private Uniformconfig uniformconfig;
    private MessageFilter messageFilter;

    public ServerChannelInitializer(ChannelHandler serverHandler,List<Header> whitelist) {
    	this.messageFilter = new MessageFilter(whitelist);
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
                .addLast(new LenPreMsgDecoder())
                .addLast(new LenPreMsgEncoder())
                
                //出方向解码
                .addLast(messageFilter)
                .addLast(new ServerConnectionHandler())
                //业务处理
                .addLast(serverHandler);
    }
}
