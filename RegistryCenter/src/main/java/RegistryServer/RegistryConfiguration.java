package RegistryServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import configutils.CenterConfig;
import configutils.ServiceConfig;
import configutils.SsoConfig;
import configutils.Uniformconfig;

@Configuration

public class RegistryConfiguration {
	private static Logger logger = LoggerFactory.getLogger(RegistryConfiguration.class);
	@Autowired
	private Uniformconfig ufc;
	
	@Autowired
	private ServiceConfig sec;
	
	@Autowired
	private SsoConfig soc;
	/*//very lazy
	@Autowired
	private ChannelCache channelCache;
	
	@Autowired
	private ServiceAddrCache serviceAddrCache;*/
	
	@Bean(name="centerConfig")
	public CenterConfig centerConfig(){
		logger.info("ufc:{}",ufc);
		logger.info("sec:{}",sec);
		logger.info("soc:{}",soc);
		return new CenterConfig(ufc,sec,soc);
	}
}
