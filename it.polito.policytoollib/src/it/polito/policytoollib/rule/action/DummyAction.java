package it.polito.policytoollib.rule.action;

public enum DummyAction implements Action{
	DUMMY;
	
	public String toString(){
		if(this == DUMMY)
			return "DUMMY";
		else
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
