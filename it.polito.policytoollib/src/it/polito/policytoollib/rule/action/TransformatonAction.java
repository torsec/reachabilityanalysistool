package it.polito.policytoollib.rule.action;

import it.polito.policytoollib.rule.impl.ConditionClause;

public abstract class TransformatonAction implements Action  {
	
	private ConditionClause transformation;
	
	public TransformatonAction(ConditionClause transformation){
		this.transformation=transformation;
	}
	
	public ConditionClause getTransformation(){
		return transformation;
	}

	public void setTransformation(ConditionClause transformation) {
		this.transformation = transformation;
	}

	
}
