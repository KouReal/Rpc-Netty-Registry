package testwithspringboot;

import java.util.UUID;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

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

@SpringBootApplication
@ComponentScan(basePackages={"testwithspringboot","RegistryClient","RpcClient","springutils"})
public class JmeterTest extends AbstractJavaSamplerClient{

	public static void main(String[] args) {
		SpringApplication.run(JmeterTest.class, args);
	}
	private RpcClient rpcClient;
	private LenPreMsg lenPreMsg;
	private ResultFuture<RpcResponse> future;
	private NormalConfig normalConfig;
	static {
		try {
			ProtocolMap.setmap();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

// 设置传入的参数，可以设置多个，已设置的参数会显示到Jmeter的参数列表中
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("testmsg", "hellorpcserver");// 设置参数，并赋予默认值0
		params.addArgument("b", "thisisjmeterworld");// 设置参数，并赋予默认值0
		return params;
	}

	// 初始化方法，实际运行时每个线程仅执行一次，在测试方法运行前执行
	public void setupTest(JavaSamplerContext arg0) {
		String testmsg = arg0.getParameter("testmsg");
		JSONObject msgjson = new JSONObject();
		msgjson.put("testmsg", testmsg);
		RpcRequest rpcRequest = new RpcRequest(null, "performancetest", "testEcho", null, msgjson.toJSONString());
		lenPreMsg = LenPreMsg.buildsimplemsg(Header.rpc_request, rpcRequest);

		// set normalconfig
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

	// 测试执行的循环体，根据线程数和循环次数的不同可执行多次
	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {
		SampleResult results = new SampleResult();
		results.sampleStart();// jmeter 开始统计响应时间标记
		try {
			String msgid = UUID.randomUUID().toString();
			lenPreMsg.setMsgid(msgid);
			future = new ResultFuture<RpcResponse>(msgid);
			rpcClient.invokeWithFuture(lenPreMsg, future);
			RpcResponse rpcResponse = future.get();
			String content = rpcResponse.getContent();
			results.setResponseData(content, "utf-8");
			results.setDataType(SampleResult.TEXT);
			results.setSuccessful(true);
		} catch (Throwable e) {
			results.setSuccessful(false);
			e.printStackTrace();
		} finally {
			// jmeter 结束统计响应时间标记
			results.sampleEnd();
		}
		return results;
	}

// 结束方法，实际运行时每个线程仅执行一次，在测试方法运行结束后执行
	public void teardownTest(JavaSamplerContext arg0) {
		rpcClient.Stop();
//		ExecutorService es = FutureCache.scheduler;
//		es.shutdown();
//		while (!es.isTerminated()) {
//
//		}
////			sLOGGER.info("已经关闭FutureCache的scheduler...");

	}
}
