package RegistryServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import MessageUtils.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerConnectionHandler extends SimpleChannelInboundHandler<RegistryMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectionHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RegistryMessage message) throws Exception {
        LOGGER.info("receive msg:{}",message);
        System.out.println("receive msg:{}"+message);
    	Header header = message.getHeader();
        //若是心跳请求则直接返回，否则交给下一handler处理
        if (Header.HEART_BEAT_REQUEST == header.getType()) {
            LOGGER.info("注册中心收到心跳请求，channel:{}", channelHandlerContext.channel());
        } else {
            channelHandlerContext.fireChannelRead(message);
        }
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state()==IdleState.READER_IDLE){
            	LOGGER.info("服务中心not receive heartbeat timeout, will close server");
                ctx.close();
            }
            
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
