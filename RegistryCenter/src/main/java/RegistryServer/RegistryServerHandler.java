package RegistryServer;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.RegistryMessage;
import MessageUtils.RpcRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class RegistryServerHandler extends SimpleChannelInboundHandler<RegistryMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryServerHandler.class);

    private ExecutorService threadPool;

    public RegistryServerHandler(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, RegistryMessage msg) throws Exception {
        LOGGER.info("服务端收到 registrymsg", msg.toString());
        //交由业务线程池执行
        threadPool.execute(new RegistryTask(ctx, msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }
}