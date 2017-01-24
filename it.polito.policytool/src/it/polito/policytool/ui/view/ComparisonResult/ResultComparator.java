package it.polito.policytool.ui.view.ComparisonResult;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.rule.impl.GenericRule;

public class ResultComparator extends ViewerComparator {

	/** The property index. */
	private int propertyIndex;
	
	/** The Constant DESCENDING. */
	private static final int DESCENDING = 1;
	
	/** The direction. */
	private int direction = DESCENDING;
	
	private HashMap<Integer, String> selectorNames;
	private HashMap<Integer, String> propertyNames;
	
	/**
	 * Instantiates a new f policy view comparator.
	 */
	public ResultComparator() {
		this.selectorNames = new HashMap<>();
		this.propertyNames = new HashMap<>();
		this.propertyIndex = 0;
		direction = DESCENDING;
	}
	
	/**
	 * Gets the direction.
	 * 
	 * @return the direction
	 */
	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}
	
	/**
	 * Sets the column.
	 * 
	 * @param column
	 *           the new column
	 */
	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}
	
	public void addSelector(int i, String selectorName){
		selectorNames.put(i, selectorName);
	}
	
	public void removeSelector(int i){
		selectorNames.remove(i);
	}
	
	public void addProperty(int i, String propertyName){
		propertyNames.put(i, propertyName);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		GenericRule r1 = (GenericRule) e1;
		GenericRule r2 = (GenericRule) e2;
		String s1 = "";
		String s2 = "";
		int rc = 0;
		
		if(selectorNames.containsKey(propertyIndex)){
			String selectorName = selectorNames.get(propertyIndex);
			if (r1.getConditionClause().get(selectorName) == null)
				s1 = "*";
			else
				s1 = r1.getConditionClause().get(selectorName).toString();
			if (r2.getConditionClause().get(selectorName) == null)
				s2 = "*";
			else
				s2 = r2.getConditionClause().get(selectorName).toString();
		}
		if(propertyNames.containsKey(propertyIndex)){
		
			String propertyName = propertyNames.get(propertyIndex);
			
			if(propertyName.equals("Name")){
				s1 = r1.getName();
				s2 = r2.getName();
			}
			
			if(propertyName.equals("Action")){
				s1 = r1.getAction().toString();
				s2 = r2.getAction().toString();
			}
		}
		
		rc = s1.compareTo(s2);
		// If descending order, flip the direction
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
	
}
