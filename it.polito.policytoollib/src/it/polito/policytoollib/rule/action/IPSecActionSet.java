package it.polito.policytoollib.rule.action;

import java.util.LinkedList;

public class IPSecActionSet implements Action{

	private LinkedList<IPSecAction> ipSecActionList;
	
	public IPSecActionSet(LinkedList<IPSecAction> ipSecActionList){
		this.ipSecActionList = ipSecActionList;
	}
	
	public LinkedList<IPSecAction> getSecActionList(){
		return ipSecActionList;
	}
	
	public String toString(){
		String ret="";
		for(IPSecAction action:ipSecActionList){
			ret+="\n"+action.toString();
		}
		return ret;
	}

	@Override
	public Action actionClone() {
		LinkedList<IPSecAction> new_ipSecActionList = new LinkedList<IPSecAction>();
		for(IPSecAction a:ipSecActionList){
			new_ipSecActionList.add((IPSecAction)a.actionClone());
		}
		return new IPSecActionSet(new_ipSecActionList);
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
