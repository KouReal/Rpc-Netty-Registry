package RpcClient;



import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asyncutils.FutureCache;

import java.util.concurrent.TimeUnit;



@ChannelHandler.Sharable
public class ConnectionWatchDog extends SimpleChannelInboundHandler<LenPreMsg> implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionWatchDog.class);
    private ReConnectionListener listener;

    public ConnectionWatchDog(ReConnectionListener reConnectionListener) {
        this.listener = reConnectionListener;
        
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LenPreMsg message) throws Exception {
        //若是心跳响应则直接返回，否则交给下一handler处理
    	
    	if(message.getHeader() == Header.heart_beat){
    		LOGGER.info("rpcclient收到心跳请求");
    		return;
    	}
    	ctx.fireChannelRead(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //当channel关闭时，清除eventLoop中channel对应的所有future
    	listener.getConnection().unbind();
        FutureCache.removeChannel(ctx.channel());
        LOGGER.info("the link is disconnected with channel:{},"
        		+ "will reconnect in eventloop:{},"
        		+ "will remove channel in rpcfutureholder",
        		ctx.channel(),ctx.channel().eventLoop()
        		);
        
        //线程开启定时任务，准备尝试重连
        ctx.channel().eventLoop().schedule(this, 3L, TimeUnit.SECONDS);
        ctx.fireChannelInactive();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state()==IdleState.WRITER_IDLE){
            	ctx.writeAndFlush(LenPreMsg.buildsimplemsg(Header.heart_beat, null));
            	LOGGER.debug("send heartbeat with channel:{}, in eventloop:{}",ctx.channel(),ctx.channel().eventLoop());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    @Override
    public void run() {
        Connection connection = listener.getConnection();
        Bootstrap bootstrap = connection.getBootstrap();
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Connection.DEFAULT_CONNECT_TIMEOUT);
        ChannelFuture future = bootstrap.connect(connection.getTargetIP(), connection.getTargetPort());
        //不能在EventLoop中进行同步调用，这样会导致调用线程即EventLoop阻塞
        future.addListener(listener);
    }
}
