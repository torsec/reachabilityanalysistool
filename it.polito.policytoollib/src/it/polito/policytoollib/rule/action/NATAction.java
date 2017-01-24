package it.polito.policytoollib.rule.action;

import it.polito.policytoollib.rule.impl.ConditionClause;

public class NATAction extends TransformatonAction{
	
	private NATActionType NATAction;

	public NATAction(NATActionType NATAction, ConditionClause transformation) {
		super(transformation);
		this.NATAction = NATAction;
	}

	public NATActionType getNATAction() {
		return NATAction;
	}
	
	@Override
	public Action actionClone() {
		return new NATAction(NATAction, getTransformation().conditionClauseClone());
	}
	
	public String toString(){
		if(NATAction==NATActionType.PRENAT)
			return "PRENAT";
		if(NATAction==NATActionType.POSTNAT)
			return "POSTNAT";
		return "Unknown NAT action";
	}

	@Override
	public ActionData[] getActionData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActionData(ActionData[] actionData) {
		// TODO Auto-generated method stub
		
	}
}
