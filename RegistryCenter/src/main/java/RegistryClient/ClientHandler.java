package RegistryClient;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import springutils.SpringContextStatic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import TokenUtils.Token;
import configutils.NormalConfig;




@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<RegistryMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    
    private ConfigFuture configFuture = (ConfigFuture) SpringContextStatic.getBean("configFuture");
    private TokenTask tokenTask = (TokenTask) SpringContextStatic.getBean("tokenTask");
   


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RegistryMessage msg) throws Exception {
        byte headertype = msg.getHeader().getType();
    	if(headertype==Header.REGISTRY_NORMALCONFIG){
    		LOGGER.info("registryclient 接收到NormalConfig");
        	configFuture.done((NormalConfig)msg.getBody());
        }else if(headertype==Header.REGISTRY_TOKEN){
        	LOGGER.info("registryclient 接收到Token");
        	tokenTask.settoken((Token)msg.getBody());
        }else if(headertype==Header.REGISTRY_DISCOVER_REPLY){
        	
        }else{
        	LOGGER.info("registryclient收到不明确的响应消息：{}",msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("api caught exception", cause);
        ctx.channel().close();
    }
}
