package RpcServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import MessageUtils.RpcRequest;
import MessageUtils.RpcResponse;
import exceptionutils.RpcErrorException;
import io.netty.channel.ChannelHandlerContext;
import reflectionutils.ServiceProxy;
import springutils.SpringContextStatic;

public class RpcTask implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(RpcTask.class);
	

    private ChannelHandlerContext ctx;
    private RpcRequest request;
    
    private ServiceProxy serviceProxy;

    public RpcTask(ChannelHandlerContext ctx, RpcRequest request) {
        this.ctx = ctx;
        this.request = request;
        this.serviceProxy = (ServiceProxy) SpringContextStatic.getBean("serviceProxy");
    }

    @Override
    public void run() {
        // 创建并初始化 RPC 响应对象
        RpcResponse response = new RpcResponse();
        response.setRequestid(request.getRequestId());
        logger.info("收到rpcrequest:{}",request);
            //调用服务，获取服务结果
            String serviceResult = this.handle(request);
            //结果添加到响应
            response.setContent(serviceResult);
      
        // 写入 RPC 响应对象
            logger.info("得到rpcresponse:{}",response);
        ctx.writeAndFlush(response);
    }

    /**
     * 根据请求调用已经注册的远程服务
     *
     * @param request
     * @return
     * @throws Exception
     */
    private String handle(RpcRequest request) {
        // 获取服务实例对象
        String serviceName = request.getServicename();
       
        //利用反射调用服务
        String methodName = request.getMethodName();
        String paramjsonstr = request.getParamjsonstr();
        JSONObject params = JSONObject.parseObject(paramjsonstr);
        
        Object result;
		try {
			result = serviceProxy.callservice(serviceName, methodName, params);
		} catch (RpcErrorException e) {
			// TODO Auto-generated catch block
			logger.error("调用服务：{},方法：{}出错，错误信息：{}",serviceName,methodName,e.getMessage());
			e.printStackTrace();
			return null;
		}
        /*Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        return this.invokeByReflect(serviceBean, methodName, parameterTypes, parameters);
    */
        return (String)result;
    }

}