package javatestdemo;


import org.junit.Test;

import exceptionutils.ProtocolException;
import protocolutils.ProtocolMap;

public class testmanager {
	@Test
	public void test1() throws InterruptedException, ProtocolException {
		ProtocolMap.setmap();
		for(int i=0;i<5;++i) {
			new Thread(new testsample(4)).start();
		}
		Thread.sleep(60000);
		
	}
	
	
}
