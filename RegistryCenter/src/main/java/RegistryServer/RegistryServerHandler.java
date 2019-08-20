package RegistryServer;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocolutils.LenPreMsg;

@ChannelHandler.Sharable
public class RegistryServerHandler extends SimpleChannelInboundHandler<LenPreMsg> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryServerHandler.class);

    private ExecutorService threadPool;

    public RegistryServerHandler(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
        LOGGER.info("服务端收到 Lenpremsg{},in thread{}", msg,Thread.currentThread());
        //交由业务线程池执行
        threadPool.execute(new RegistryTask(ctx, msg));
        //RegistryTask task = new RegistryTask(ctx, msg);
        //threadPool.submit(task);
//        new Thread(new RegistryTask(ctx,msg)).start();
//        System.out.println("卡了吗？");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }
}