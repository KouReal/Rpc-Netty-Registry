package RegistryClient;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import RegistryUtil.NamedThreadFactory;
import TokenUtils.Token;
import configutils.NormalConfig;




@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<RegistryMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    
    private ConfigFuture configFuture;
    private TokenTask tokenTask;
   
    public ClientHandler(ConfigFuture configFuture,TokenTask tokenTask) {
    	this.configFuture = configFuture;
    	this.tokenTask = tokenTask;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RegistryMessage msg) throws Exception {
        byte headertype = msg.getHeader().getType();
    	if(headertype==Header.REGISTRY_NORMALCONFIG){
    		LOGGER.info("registryclient 接收到NormalConfig");
        	configFuture.done((NormalConfig)msg.getBody());
        }else if(headertype==Header.REGISTRY_TOKEN){
        	LOGGER.info("registryclient 接收到Token");
        	tokenTask.settoken((Token)msg.getBody());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("api caught exception", cause);
        ctx.channel().close();
    }
}
