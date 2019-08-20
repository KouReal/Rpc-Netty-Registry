package RpcServer;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.RpcRequest;

@ChannelHandler.Sharable
public class RpcServiceHandler extends SimpleChannelInboundHandler<LenPreMsg> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceHandler.class);

    private ExecutorService threadPool;

    public RpcServiceHandler(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
    	
        LOGGER.info("rpc服务端收到 msg:{}", msg);
        //交由业务线程池执行
        if(msg.getHeader()==Header.rpc_request){
        	threadPool.execute(new RpcTask(ctx, msg));
        }
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }
}