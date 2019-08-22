package RegistryServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import configutils.Uniformconfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import springutils.SpringContextStatic;

public class ServerConnectionHandler extends SimpleChannelInboundHandler<LenPreMsg> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectionHandler.class);
    private Uniformconfig uniformconfig = SpringContextStatic.getBean(Uniformconfig.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
//        LOGGER.info("receive msg:{}",msg);
//        System.out.println("receive msg:{}"+message);
        Header header = msg.getHeader();
        if(Header.heart_beat == header){
        	if(uniformconfig.isShowHeartBeat()==true){
        		LOGGER.info("注册中心channel:{},收到心跳请求", ctx.channel());
        	}
        	return ;
        }
    	ctx.fireChannelRead(msg);
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state()==IdleState.READER_IDLE){
            	LOGGER.info("服务中心not receive heartbeat timeout, will close this channel");
                ctx.close();
            }
            
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
