package exceptionutils;

public class RpcErrorException extends Exception{
	private static final long serialVersionUID = 1L;
	public RpcErrorException(){}
	public RpcErrorException(String mes){
		super(mes);
	}
}
