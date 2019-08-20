package exceptionutils;

public class FutureException extends Exception{
	private static final long serialVersionUID = 1L;
	public FutureException(){}
	public FutureException(String mes){
		super(mes);
	}
}
