package performancetest_service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import TokenUtils.TokenCache;
import configutils.ServiceAddr;
import configutils.ServiceConfig;
import configutils.SsoConfig;
import configutils.Uniformconfig;
import exceptionutils.ProtocolException;
import protocolutils.NormalConfig;
import protocolutils.ProtocolMap;

@Configuration("performancetestServiceConfig")
public class PerformanceTestServiceConfig {
	Logger logger = LoggerFactory.getLogger(PerformanceTestServiceConfig.class);
	
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
	
	
	@Bean(name="normalConfig")
	@DependsOn("springContextStatic")
	public NormalConfig normalConfig(){
		logger.info("ufc:{}",ufc);
		logger.info("sec:{}",sec);
		logger.info("soc:{}",soc);
		try {
			ProtocolMap.setmap();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ServiceAddr> serviceaddrs = sec.getServiceaddrs();
		ServiceAddr mysa=new ServiceAddr();
		for (ServiceAddr sa : serviceaddrs) {
			if("performancetest".equals(sa.getName())) {
				mysa = sa;
			}
		}
		NormalConfig normalConfig = new NormalConfig(ufc, mysa, null);
		return normalConfig;
	}
	
}
