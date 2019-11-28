//package testregistryclient;
//
//import java.util.concurrent.ExecutionException;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.internal.verification.api.VerificationDataInOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.stereotype.Component;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import MessageUtils.Header;
//import MessageUtils.RegistryMessage;
//import RegistryClient.ConfigFuture;
//import RegistryClient.RegistryClient;
//import configutils.NormalConfig;
//import configutils.ServiceRegist;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class sendmessage {
//	@Autowired
//	RegistryClient registryClient;
//
//	@Autowired
//	ConfigFuture configFuture;
//
//	@Test
//	public void test1(){
//		System.out.println("in test");
//		Header header = new Header(1, Header.REGISTRY_SERVICE);
//		ServiceRegist serviceRegist = new ServiceRegist("testservice", "127.0.0.1:9200");
//		RegistryMessage registryMessage = new RegistryMessage(header, (Object)serviceRegist);
//		try {
//			registryClient.sendtocenter(registryMessage);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			System.out.println("sleep  ----thread:"+Thread.currentThread());
//			Thread.sleep(100000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void test2(){
//		Thread th = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				System.out.println("in test");
//				Header header = new Header(1, Header.REGISTRY_SERVICE);
//				ServiceRegist serviceRegist = new ServiceRegist("testservice", "127.0.0.1:9200");
//				RegistryMessage registryMessage = new RegistryMessage(header, (Object)serviceRegist);
//				try {
//					registryClient.sendtocenter(registryMessage);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				try {
//					NormalConfig ncfg = configFuture.get();
//					System.out.println(ncfg);
//				} catch (InterruptedException | ExecutionException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				try {
//					System.out.println("sleep  ----thread:"+Thread.currentThread());
//					Thread.sleep(100000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		th.start();
//		try {
//			th.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//}
