package RpcClient;




import MessageUtils.RpcRequest;
import RegistryClient.ConfigFuture;
import RegistryClient.DiscoverFuture;
import RegistryClient.RegistryClient;
import RpcTrans.RpcFuture;
import configutils.NormalConfig;
import exceptionutils.RpcErrorException;
import io.netty.channel.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;



@Component("rpcProxy")
@DependsOn(value={"registryClient","discoverFuture"})
public class RpcProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);



    /**
     * rpc服务的远程地址的本地缓存，减少查询注册中心
     */
    private ConcurrentHashMap<String, String> addressCache = new ConcurrentHashMap<>(16);

    /**
     * rpc服务 remoteaddr(ip:port)->rpcClient 映射列表,避免重复创建
     */
    private ConcurrentHashMap<String, RpcClient> rpcClientMap = new ConcurrentHashMap<>(16);
    
    @Autowired
    private RegistryClient registryClient; 
    
    @Autowired
    private DiscoverFuture discoverFuture;

 
    /**
     * 异步调用
     *
     * @param interfaceClass
     * @param method
     * @param parameters
     * @return
     * @throws Exception
     */
    public RpcFuture call(RpcRequest rpcRequest)throws RpcErrorException{
    	
    	String servicename = rpcRequest.getServicename();
    	
        
        String serviceAddress = findServiceAddress(servicename);
        //调用异步方法
        RpcFuture future=null;
        RpcClient client=null;
        try {
            // 创建 RPC 客户端对象并发送 RPC 请求
            client = this.getRpcClient(servicename, serviceAddress);
            
            future = client.invokeWithFuture(rpcRequest);
        } catch (ConnectTimeoutException c) {
        	LOGGER.info("调用服务{}超时,client:{}",servicename,client);
            throw new RpcErrorException("调用服务超时");
        } catch (Exception r) {
            throw new RpcErrorException(r.getMessage());
        }
        return future;
    }


    /**
     * 获取已经初始化的RpcClient
     *
     * @param serviceAddress
     * @return
     */
    private RpcClient getRpcClient(String servicename, String serviceAddress) throws RpcErrorException {
        if(servicename==null||servicename.equals("")){
        	throw new RpcErrorException("服务名不能为空");
        }
        RpcClient client = rpcClientMap.get(serviceAddress);
        //若不存在则创建和初始化，并进行缓存
        if (client == null) {
            // 从 RPC 服务地址中解析主机名与端口号
            String[] addressArray = serviceAddress.split(":");
            
            String ip = addressArray[0];
            int port = Integer.parseInt(addressArray[1]);
            LOGGER.info("解析出地址ip:{},port:{}",ip,port);
            client = new RpcClient(ip, port);
            rpcClientMap.put(serviceAddress, client);
            addressCache.put(servicename, serviceAddress);
        }
        return client;
    }

    /**
     * 发现rpc服务地址，先从本地缓存找，找不到再查询zookeeper
     *
     * @param interfaceClassName
     * @return
     */
    private String findServiceAddress(String servicename) throws RpcErrorException {
        String serviceAddress = addressCache.get(servicename);
        if (serviceAddress != null) {
            return serviceAddress;
        }
        if (registryClient == null) {
            throw new RpcErrorException("registryClient isnot exists.");
        }
       
            try {
				registryClient.discover(servicename);
				serviceAddress = discoverFuture.get();
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				return null;
			}
        
        LOGGER.info("discover service: {} => {}", servicename, serviceAddress);
        if(serviceAddress==null||serviceAddress.equals("")){
        	throw new RpcErrorException(servicename+"服务不存在");
        	
        }
        return serviceAddress;
    }
}
