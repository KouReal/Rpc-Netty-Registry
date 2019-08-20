package RpcServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import protocolutils.Header;
import protocolutils.LenPreMsg;

public class ServerConnectionHandler extends SimpleChannelInboundHandler<LenPreMsg> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectionHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
        if(msg.getHeader() == Header.heart_beat){
        	LOGGER.info("服务端收到心跳请求,channel:{}",ctx.channel());
        	return ;
        }
        ctx.fireChannelRead(msg);
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state()==IdleState.READER_IDLE){
            	LOGGER.info("not receive heartbeat timeout, will close link");
                ctx.close();
            }
            
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
