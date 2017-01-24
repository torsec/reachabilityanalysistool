package it.polito.policytoollib.rule.action;

public enum NATActionType implements Action{
	PRENAT, POSTNAT, BRANCH;
	
	@Override
	public String toString(){
		if(this == PRENAT)
			return "PRENAT";
		else if (this == POSTNAT)
			return "POSTNAT";
		return "";
	}
	
	@Override
	public Action actionClone() {
		return this;
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
