package it.polito.policytoollib.rule.action;

public class RoutingAction implements Action {

	int metric;

	public int getMetric() {
		return metric;
	}

	public void setMetric(int metric) {
		this.metric = metric;
	}

	public RoutingAction(int metric) {
		this.metric=metric;
	}

	@Override
	public Action actionClone() {
		return new RoutingAction(this.metric);
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
