package it.polito.policytoollib.exception.rule;

public class IllegalParamException extends Exception{

	static final long serialVersionUID = 12L;
	
	public IllegalParamException(){}

	public IllegalParamException(String msg)
	{
		super(msg);
	}

}
