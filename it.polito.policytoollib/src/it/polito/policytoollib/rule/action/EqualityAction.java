package it.polito.policytoollib.rule.action;

public enum EqualityAction implements Action{
	EQUAL, DIFFERENT;
	
	public String toString(){
		if(this == EQUAL)
			return "EQUAL";
		else
			return "DIFFERENT";
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
