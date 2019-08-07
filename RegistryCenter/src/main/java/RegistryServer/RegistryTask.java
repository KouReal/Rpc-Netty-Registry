package RegistryServer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.BooleanLiteral;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import MessageUtils.RpcRequest;
import MessageUtils.RpcResponse;
import RegistryUtil.Ssoconfig;
import RegistryUtil.ssogroup;
import TokenUtils.Token;
import configutils.NormalConfig;
import configutils.ServiceConfig;
import configutils.ServiceRegist;
import configutils.TokenConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.protostuff.Service;

public class RegistryTask implements Runnable {
	private Logger log = LoggerFactory.getLogger(RegistryTask.class);

    private ChannelHandlerContext ctx;
    private RegistryMessage msg;
    @Autowired
    private ChannelCache channelCache;
    
    @Autowired
    private NormalConfig normalConfig;

    public RegistryTask(ChannelHandlerContext ctx, RegistryMessage msg) {
        this.ctx = ctx;
        this.msg = msg;
    }

    @Override
    public void run() {
        // 创建并初始化 RPC 响应对象
        RpcResponse response = new RpcResponse();
        byte msgtype = msg.getHeader().getType();
        if(msgtype==Header.REGISTRY_SERVICE){
        	
        	ServiceRegist serviceRegist = (ServiceRegist)msg.getBody();
        	String servicename = serviceRegist.getServicename();
        	
        	if(servicename==null||servicename.equals("")){
        		log.info("收到的ServiceRegist中servicename为空，无法注册");
        		return;
        	}
        	log.info("收到ServiceRegist,正在注册:{}",servicename);
        	channelCache.addchannel(servicename, ctx.channel());
        	log.info("注册服务：{}成功，正在向此服务回复NormalConfig配置信息");
        	sendconfig(servicename,Header.REGISTRY_NORMALCONFIG);
        }else if(msgtype==Header.REGISTRY_TOKEN){
        	Token token = (Token)msg.getBody();
        	log.info("收到Token,ssoleader是:{},正在向sso组内成员广播Token:{}",token.getLeadername(),token);
        	sendtoken(token);
        }else{
        	log.info("注册中心收到消息类型无法解析,类型为:{}",msgtype);
        	return ;
        }
        
    }
    public void sendtoken(Token token){
    	String leadername = token.getLeadername();
    	if(leadername==null)return ;
    	Ssoconfig ssoconfig = normalConfig.getSsoconfig();
    	if(ssoconfig!=null){
    		RegistryMessage registryMessage = new RegistryMessage(
    				new Header(1, Header.REGISTRY_TOKEN),
    				(Object)token);
    		List<ssogroup> ssogroups = ssoconfig.getSsoconfig();
    		for (ssogroup ssogroup : ssogroups) {
				if(leadername.equals(ssogroup.getLeaderService())){
					List<String> groupMembers = ssogroup.getGroupMembers();
					Channel ch = null;
					for (String mem : groupMembers) {
						ch = channelCache.findchannelbyservicename(mem);
						if(ch==null){
							log.info("在发送token时，发现组成员：{}和注册中心已断开连接",mem);
						}else{
							ch.writeAndFlush(registryMessage);
						}
					}
					break;
				}
			}
    	}
    }
    public Boolean checkssogroup(String servicename){
    	Boolean flag = false;
    	Ssoconfig ssoconfig = normalConfig.ssoconfig;
    	List<ssogroup> ssogroups = ssoconfig.getSsoconfig();
    	for (ssogroup ssogroup : ssogroups) {
			if(servicename.equals(ssogroup.getLeaderService())){
				flag = true;
				break;
			}else{
				List<String> members = ssogroup.getGroupMembers();
				for (String mem : members) {
					if(servicename.equals(mem)){
						flag = true;
						break;
					}
				}
				if(flag){
					break;
				}
			}
		}
    	return flag;
    }
    public void sendconfig(String servicename,byte headertype){
    	Header header = new Header(1,headertype);
    	
    	Boolean ssoflag = checkssogroup(servicename);
    	if(headertype==Header.REGISTRY_NORMALCONFIG){
    		NormalConfig ncfg = new NormalConfig();
        	ncfg.setUniformconfig(normalConfig.getUniformconfig());
    		ncfg.setServiceconfig(normalConfig.getServiceconfig());
        	if(ssoflag){
        		ncfg.setSsoconfig(normalConfig.getSsoconfig());
        	}
        	RegistryMessage registryMessage = new RegistryMessage(header, (Object)ncfg);
        	ctx.writeAndFlush(registryMessage);
    	}

    	
    }

    
}