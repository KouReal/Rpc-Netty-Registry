package RegistryServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import MessageUtils.RpcRequest;
import MessageUtils.RpcResponse;
import configutils.ServiceConfig;
import io.netty.channel.ChannelHandlerContext;
import io.protostuff.Service;

public class RegistryTask implements Runnable {
	private Logger log = LoggerFactory.getLogger(RegistryTask.class);

    private ChannelHandlerContext ctx;
    private RegistryMessage msg;
    @Autowired
    private ChannelCache channelCache;

    public RegistryTask(ChannelHandlerContext ctx, RegistryMessage msg) {
        this.ctx = ctx;
        this.msg = msg;
    }

    @Override
    public void run() {
        // 创建并初始化 RPC 响应对象
        RpcResponse response = new RpcResponse();
        byte msgtype = msg.getHeader().getType();
        if(msgtype==Header.REGISTRY_SERVICECONFIG){
        	
        	ServiceConfig serviceConfig = (ServiceConfig)msg.getBody();
        	log.info("收到ServiceConfig,正在注册:{}",serviceConfig.getServicename());
        	channelCache.addchannel(serviceConfig.getServicename(), ctx.channel());
        }else{
        	log.info("收到类型不是ServiceConfig,类型为:{}",msgtype);
        	return ;
        }
        
    }

    
}