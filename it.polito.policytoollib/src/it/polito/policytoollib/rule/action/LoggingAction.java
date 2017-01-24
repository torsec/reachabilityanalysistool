package it.polito.policytoollib.rule.action;

public enum LoggingAction implements Action{
	LOGGING;
	
	public String toString(){
		if(this == LOGGING)
			return "LOGGING";
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
