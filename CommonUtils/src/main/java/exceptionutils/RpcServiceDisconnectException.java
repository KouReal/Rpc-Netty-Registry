package exceptionutils;

public class RpcServiceDisconnectException extends Exception{
	private static final long serialVersionUID = 2L;
	public RpcServiceDisconnectException(){}
	public RpcServiceDisconnectException(String mes){
		super(mes);
	}
}
