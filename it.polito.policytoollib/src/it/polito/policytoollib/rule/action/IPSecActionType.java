package it.polito.policytoollib.rule.action;

public enum IPSecActionType implements Action{
	AH, INVERT_AH, ESP, INVERT_ESP;
	
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
