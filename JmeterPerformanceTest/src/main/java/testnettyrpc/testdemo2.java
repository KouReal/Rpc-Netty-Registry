package testnettyrpc;

import java.util.UUID;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.Test;
import org.slf4j.Logger;

import com.alibaba.fastjson.JSONObject;

import RpcClient.RpcClient;
import asyncutils.ResultFuture;
import configutils.Uniformconfig;
import exceptionutils.ProtocolException;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.NormalConfig;
import protocolutils.ProtocolMap;
import protocolutils.RpcRequest;
import protocolutils.RpcResponse;

public class testdemo2{
	RpcClient rpcClient;
	LenPreMsg lenPreMsg;
	ResultFuture<RpcResponse> future;
	private NormalConfig normalConfig;

	
	public void setparam() {
		
		JSONObject msgjson = new JSONObject();
		msgjson.put("testmsg", "junitmsg");
		RpcRequest rpcRequest = new RpcRequest(null, "performancetest", "testEcho", null, msgjson.toJSONString());
		lenPreMsg = LenPreMsg.buildsimplemsg(Header.rpc_request, rpcRequest);
		
		//set normalconfig
		Uniformconfig ufc = new Uniformconfig();
		ufc.setClientConnectTimeout(5000);
		ufc.setClientReconnectTry(20);
		ufc.setClientWriteIdle(5000);
		ufc.setServerReadIdle(10000);
		ufc.setShowHeartBeat(false);
		ufc.setTokenLife(60000);
		normalConfig = new NormalConfig(ufc, null, null);
		rpcClient = new RpcClient("127.0.0.1", 8004, normalConfig);
	}
	@Test
	public void test2() throws ProtocolException{ 
		setparam();
		ProtocolMap.setmap();
		String msgid = UUID.randomUUID().toString();
		lenPreMsg.setMsgid(msgid);
		future = new ResultFuture<RpcResponse>(msgid);
		rpcClient.invokeWithFuture(lenPreMsg, future);
		RpcResponse rpcResponse = future.get();
		if(rpcResponse==null) {
			System.out.println("futureerror");
		}
		String content = rpcResponse.getContent();
		System.out.println("thread:"+Thread.currentThread()+" get replymsg: "+content);
		rpcClient.Stop();
	}
	
}
