package it.polito.policytoollib.rule.action;

/**
 * 
 * This enum implements a filter action of a device like a router or a firewall.
 * Admitted values are ALLOW, DENY, DUMMY and INCONSISTENT that is used as default action for
 * the semilattice TOP element. 
 * 
 */
public enum FilteringAction implements Action {
	DENY, ALLOW, DUMMY, INCONSISTENT, HIDDEN_INCONSISTENT, NAT, BRANCH;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){
		if(this == DENY)
			return "DENY";
		else if (this == ALLOW)
			return "ALLOW";
		else if (this == DUMMY)
			return "DUMMY";
		else if (this == NAT)
			return "NAT";
		else if (this == HIDDEN_INCONSISTENT)
			return "HIDDEN_INCONSISTENT";
		else if (this == BRANCH)
			return "BRANCH";
		else return "INCONSISTENT";
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
