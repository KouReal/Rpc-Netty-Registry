package RpcClient;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocolutils.LenPreMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asyncutils.FutureCache;
import asyncutils.ResultFuture;


@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<LenPreMsg> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
        LOGGER.info("rpc client handler receive msg:{}",msg);
        ResultFuture<?> resultFuture = FutureCache.getfuture(ctx.channel(), msg.getMsgid());
        if(resultFuture!=null && !resultFuture.isCancelled()){
        	resultFuture.done(msg.getBody());
        }
        
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("api caught exception", cause);
        ctx.channel().close();
    }
}
