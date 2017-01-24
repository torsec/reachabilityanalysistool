package it.polito.policytoollib.exception.rule;

public class OperationNotPermittedException extends Exception{

	static final long serialVersionUID = 18L;
	
	public OperationNotPermittedException(){}

	public OperationNotPermittedException(String msg)
	{
		super(msg);
	}

}
