package RpcServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import exceptionutils.RpcErrorException;
import io.netty.channel.ChannelHandlerContext;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.RpcRequest;
import protocolutils.RpcResponse;
import reflectionutils.ServiceProxy;
import springutils.SpringContextStatic;

public class RpcTask implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(RpcTask.class);
	

    private ChannelHandlerContext ctx;
    private LenPreMsg msg;
    
    private ServiceProxy serviceProxy;

    public RpcTask(ChannelHandlerContext ctx, LenPreMsg msg) {
        this.ctx = ctx;
        this.msg = msg;
        this.serviceProxy = (ServiceProxy) SpringContextStatic.getBean("serviceProxy");
    }

    @Override
    public void run() {
    	RpcRequest rpcRequest = null;
    	if(msg.getHeader()==Header.rpc_request){
    		rpcRequest = (RpcRequest) msg.getBody();
    	}
        logger.info("rpctask收到rpcrequest:{}",rpcRequest);
            //调用服务，获取服务结果
            RpcResponse rpcResponse = this.handle(rpcRequest);
            if(rpcResponse==null){
            	logger.info("处理得到的rpcresponse为null,request为：{}",rpcRequest);
            	rpcResponse = new RpcResponse(RpcResponse.FAILURE, "服务发生异常", "null rpcresponse");
            }
            // 写入 RPC 响应对象
            logger.info("得到rpcresponse:{}",rpcResponse);
            LenPreMsg resultmsg = new LenPreMsg(Header.rpc_response, msg.getMsgid(), 1, rpcResponse);
//            RpcMessage msg = new RpcMessage(new Header(1, Header.RPC_RESPONSE), (Object)rpcResponse);
            logger.info("构造lenpremsg:{}",resultmsg);
            ctx.writeAndFlush(resultmsg);
		/*
		 * ctx.channel().eventLoop().execute(()->{ ctx.writeAndFlush(resultmsg); });
		 */
    }


    private RpcResponse handle(RpcRequest request) {
        // 获取服务实例对象
        String serviceName = request.getServicename();
       
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
			return new RpcResponse(RpcResponse.FAILURE, "服务发生异常"+e.getMessage(), e.getMessage());
			
		}
        /*Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        return this.invokeByReflect(serviceBean, methodName, parameterTypes, parameters);
    */
        return (RpcResponse)result;
    }

}