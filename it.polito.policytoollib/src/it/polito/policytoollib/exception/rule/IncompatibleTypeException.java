package it.polito.policytoollib.exception.rule;

public class IncompatibleTypeException extends Exception{

	static final long serialVersionUID = 13L;
	
	public IncompatibleTypeException(){}

	public IncompatibleTypeException(String msg)
	{
		super(msg);
	}

}