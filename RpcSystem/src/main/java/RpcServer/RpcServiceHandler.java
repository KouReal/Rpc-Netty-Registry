package RpcServer;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.RpcRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class RpcServiceHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceHandler.class);

    private ExecutorService threadPool;

    public RpcServiceHandler(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        LOGGER.info("服务端收到 rpc request:{}", request);
        //交由业务线程池执行
        threadPool.execute(new RpcTask(ctx, request));
        LOGGER.info("正在处理rpc request:{}",request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }
}