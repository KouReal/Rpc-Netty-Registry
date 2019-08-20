package RegistryServer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import configutils.CenterConfig;
import configutils.ServiceAddr;
import configutils.SsoConfig;
import configutils.SsoGroup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.NormalConfig;
import protocolutils.RegDiscover;
import protocolutils.RegService;
import protocolutils.Token;
import springutils.SpringContextStatic;

public class RegistryTask implements Runnable {
	private static Logger log = LoggerFactory.getLogger(RegistryTask.class);

	private ChannelHandlerContext ctx;
	private LenPreMsg msg;
	private ChannelCache channelCache = null;
	private ServiceAddrCache serviceAddrCache = null;
	private CenterConfig centerConfig = null;

	public RegistryTask(ChannelHandlerContext ctx, LenPreMsg msg) {

		this.ctx = ctx;
		this.msg = msg;

		this.channelCache = (ChannelCache) SpringContextStatic.getBean("channelCache");
		this.serviceAddrCache = (ServiceAddrCache) SpringContextStatic.getBean("serviceAddrCache");
		this.centerConfig = (CenterConfig) SpringContextStatic.getBean("centerConfig");
	}

	@Override
	public void run() {
		log.info("registrytask handle msg:{}", msg);
		Header header = msg.getHeader();
		Object body = msg.getBody();
		LenPreMsg resultmsg = null;
		
		switch (header) {
		case reg_addservice:
			String servicename = ((RegService)body).getServicename();
			if (servicename == null || servicename.equals("")) {
				log.info("收到的RegService中servicename为空，无法注册");
				return;
			}
			log.info("收到RegService,正在注册:{}", servicename);
			channelCache.addchannel(servicename, ctx.channel());
			log.info("注册服务：{}成功，正在向此服务回复NormalConfig配置信息");
			ServiceAddr serviceAddr = getserviceaddr(servicename);
			SsoGroup ssoGroup = getssogroup(servicename);
			NormalConfig normalConfig = new NormalConfig(centerConfig.getUniformconfig(), serviceAddr, ssoGroup);
			resultmsg = new LenPreMsg(Header.reg_normalconfig, msg.getMsgid(), 1, normalConfig);
			ctx.writeAndFlush(resultmsg);
			break;
		case reg_tokenconfig:
			Token token = (Token)body;
			log.info("收到Token,ssoleader是:{},正在向sso组内成员广播Token:{}", token.getLeadername(), token);
			broadcasttoken(token);
			break;
		case reg_discover:
			RegDiscover regDiscover = ((RegDiscover)body);
			String name = regDiscover.getServicename();
			log.info("收到discover消息，servicename：{}", name);
			String addr = serviceAddrCache.findaddrbyservicename(name);
			log.info("servicename：{},对应地址为：{}", name, addr);
			regDiscover.setServiceaddr(addr);
			resultmsg = new LenPreMsg(Header.reg_discover, msg.getMsgid(), 1, regDiscover);
			ctx.writeAndFlush(resultmsg);
			break;
		default:
			log.info("注册中心收到消息类型无法解析,协议类型为:{}", header);
			break;
		}


	}

	public void broadcasttoken(Token token) {
		String leadername = token.getLeadername();
		LenPreMsg resultmsg = LenPreMsg.buildsimplemsg(Header.reg_tokenconfig, token);
		if (leadername == null){
			log.info("broadcast token getleadername is null");
			return;
		}
		SsoGroup ssoGroup = getssogroup(leadername);
		Channel ch = null;
		for(String mem : ssoGroup.getGroupMembers()){
			ch = channelCache.findchannelbyservicename(mem);
			if (ch == null) {
				log.info("在发送token时，发现组成员：{}和注册中心已断开连接", mem);
			} else {
				log.info("向leader:{}的组成员{}发送token",leadername,mem);
				ch.writeAndFlush(resultmsg);
			}
		}
	}
	
	public ServiceAddr getserviceaddr(String servicename){
		ServiceAddr serviceAddr = null;
		List<ServiceAddr> serviceaddrs = centerConfig.getServiceconfig().getServiceaddrs();
		for (ServiceAddr sa : serviceaddrs) {
			if(servicename.equals(sa.getName())){
				serviceAddr = sa;
				break;
			}
		}
		return serviceAddr;
	}

	public SsoGroup getssogroup(String servicename) {
		Boolean flag = false;
		SsoConfig ssoconfig = centerConfig.getSsoconfig();
		SsoGroup sso_group = null;
		List<SsoGroup> ssogroups = ssoconfig.getSsogroups();
		for (SsoGroup ssogroup : ssogroups) {
			if (servicename.equals(ssogroup.getLeaderService())) {
				sso_group = ssogroup;
				break;
			} else {
				List<String> members = ssogroup.getGroupMembers();
				for (String mem : members) {
					if (servicename.equals(mem)) {
						flag = true;
						sso_group = ssogroup;
						break;
					}
				}
				if (flag) {
					break;
				}
			}
		}
		return sso_group;
	}

	
}