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
import MessageUtils.Ssoconfig;
import MessageUtils.ssogroup;
import TokenUtils.Token;
import configutils.NormalConfig;
import configutils.ServiceRegist;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.protostuff.Service;
import springutils.SpringContextStatic;

public class RegistryTask implements Runnable {
	private static Logger log = LoggerFactory.getLogger(RegistryTask.class);

    private ChannelHandlerContext ctx;
    private RegistryMessage msg;
    private ChannelCache channelCache = null;
    private ServiceAddrCache serviceAddrCache = null;
    private NormalConfig normalConfig = null;
    
    /*private ChannelCache channelCache = SpringContextStatic.getBean(ChannelCache.class);
    
    private ServiceAddrCache serviceAddrCache = SpringContextStatic.getBean(ServiceAddrCache.class);
    
    private NormalConfig normalConfig = SpringContextStatic.getBean(NormalConfig.class);*/

    public RegistryTask(ChannelHandlerContext ctx, RegistryMessage msg) {
    	System.out.println("constructing registrytask,appctx:"+SpringContextStatic.getApplicationContext());
    	log.info("doRunServer:appctx:{}",SpringContextStatic.getApplicationContext());
    	log.info("normalconfig:{}",SpringContextStatic.getBean("normalConfig"));
    	/*log.info("channelcache:{}",SpringContextStatic.getBean(ChannelCache.class));
    	log.info("serviceaddrcache:{}",SpringContextStatic.getBean(ServiceAddrCache.class));*/
        
    	this.ctx = ctx;
        this.msg = msg;
        /*this.channelCache = SpringContextStatic.getBean(ChannelCache.class);
        this.serviceAddrCache = SpringContextStatic.getBean(ServiceAddrCache.class);
        this.normalConfig = SpringContextStatic.getBean(NormalConfig.class);*/
        
        this.channelCache = (ChannelCache) SpringContextStatic.getBean("channelCache");
        this.serviceAddrCache = (ServiceAddrCache) SpringContextStatic.getBean("serviceAddrCache");
        this.normalConfig = (NormalConfig) SpringContextStatic.getBean("normalConfig");
    }

    @Override
    public void run() {
    	log.info("registryserver get msg:{}",msg);
//        System.out.println("registrytask:  "+msg);
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
        }else if(msgtype==Header.REGISTRY_DISCOVER){
        	String servicename = (String)msg.getBody();
        	log.info("收到discover消息，servicename：{}",servicename);
        	String addr = serviceAddrCache.findaddrbyservicename(servicename);
        	log.info("servicename：{},对应地址为：{}",servicename,addr);
        	RegistryMessage drmsg = new RegistryMessage(new Header(1, Header.REGISTRY_DISCOVER_REPLY), (Object)addr);
        	ctx.writeAndFlush(drmsg);
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