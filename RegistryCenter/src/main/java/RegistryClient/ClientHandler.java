package RegistryClient;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.Token;
import springutils.SpringContextStatic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asyncutils.FutureCache;
import asyncutils.ResultFuture;





@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<LenPreMsg> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    
    private TokenTask tokenTask = (TokenTask) SpringContextStatic.getBean("tokenTask");
   


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
        
    	Header protocoltype = msg.getHeader();
    	if(protocoltype==Header.reg_tokenconfig){
    		LOGGER.info("registryclient 接收到Token");
        	tokenTask.settoken((Token)msg.getBody());
        	return ;
    	}
    	ResultFuture<?> resultFuture = FutureCache.getfuture(ctx.channel(), msg.getMsgid());
    	resultFuture.done(msg.getBody());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("api caught exception", cause);
        ctx.channel().close();
    }
}
