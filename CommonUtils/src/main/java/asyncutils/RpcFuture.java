package asyncutils;


import java.util.function.BiConsumer;
import protocolutils.RpcResponse;

public class RpcFuture extends ResultFuture<RpcResponse>{
	private BiConsumer<String,Object> responsehandler;
	public RpcFuture() {
		super();
	}
	public RpcFuture(String futureid, BiConsumer<String,Object> rh) {
		super(futureid);
		this.responsehandler = rh;
	}
	/**
     * 重写父类ResultFuture的done方法，在done中直接将msg消息传给httptask类
     *
     * @param result
     */
    public void done(Object result) {
        responsehandler.accept(this.getFutureid(),result);
        
    }
}
