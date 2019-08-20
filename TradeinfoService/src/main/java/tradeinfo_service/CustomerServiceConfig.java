package tradeinfo_service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import RegistryClient.RegistryClient;
import asyncutils.ResultFuture;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.NormalConfig;
import protocolutils.RegService;

@Configuration("customerServiceConfig")
@DependsOn("registryClient")
public class CustomerServiceConfig {
	Logger logger = LoggerFactory.getLogger(CustomerServiceConfig.class);
	@Autowired
	private RegistryClient registryClient;
		
	@Bean("normalConfig")
	@DependsOn(value={"registryClient"})
//	@Order(1)
	public NormalConfig normalConfig(){
		logger.info("registryclient:{}",registryClient);
		RegService regService = new RegService("customer", "127.0.0.1:xx");
		LenPreMsg msg = LenPreMsg.buildsimplemsg(Header.reg_addservice, regService);
		ResultFuture<NormalConfig> future = new ResultFuture<>(msg.getMsgid());
		try {
			registryClient.invokewithfuture(msg, future);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NormalConfig normalConfig;
		normalConfig = future.get();
//		System.out.println("i get normalconfig");
		if(normalConfig==null){
			//关闭springboot
			logger.error("接收到的normalconfig为空，无法继续进行，即将关闭应用");
			System.exit(1);
		}
		return normalConfig;
	}

      
}
