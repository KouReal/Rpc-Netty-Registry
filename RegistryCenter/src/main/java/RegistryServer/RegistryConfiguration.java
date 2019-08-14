package RegistryServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import MessageUtils.Serviceconfig;
import MessageUtils.Ssoconfig;
import MessageUtils.Uniformconfig;
import configutils.NormalConfig;

@Configuration

public class RegistryConfiguration {
	private static Logger logger = LoggerFactory.getLogger(RegistryConfiguration.class);
	@Autowired
	private Uniformconfig ufc;
	
	@Autowired
	private Serviceconfig sec;
	
	@Autowired
	private Ssoconfig soc;
	/*//very lazy
	@Autowired
	private ChannelCache channelCache;
	
	@Autowired
	private ServiceAddrCache serviceAddrCache;*/
	
	@Bean(name="normalConfig")
	public NormalConfig normalConfig(){
		logger.info("ufc:{}",ufc);
		logger.info("sec:{}",sec);
		logger.info("soc:{}",soc);
		return new NormalConfig(ufc,sec,soc);
	}
}
