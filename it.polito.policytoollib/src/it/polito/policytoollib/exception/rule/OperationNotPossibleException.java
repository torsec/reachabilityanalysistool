package it.polito.policytoollib.exception.rule;

public class OperationNotPossibleException extends Exception{

	static final long serialVersionUID = 19L;
	
	public OperationNotPossibleException(){}

	public OperationNotPossibleException(String msg)
	{
		super(msg);
	}

}