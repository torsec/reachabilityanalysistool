package it.polito.policytoollib.rule.action;
/**
 * 
 * This interface represents a generic Action of a network device (e.g., allow, deny, drop, encrypt
 * decrypt...), provides only the method toString().
 *
 */
public interface Action {
	
	/**
	 * @return   The action's printable value
	 */
	public String toString();

	public Action actionClone();
	
	public ActionData[] getActionData();
	
	public void setActionData(ActionData[] actionData);
}
