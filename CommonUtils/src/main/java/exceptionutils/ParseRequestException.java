package exceptionutils;

public class ParseRequestException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ParseRequestException(){}
	public ParseRequestException(String mes){
		super(mes);
	}
}
