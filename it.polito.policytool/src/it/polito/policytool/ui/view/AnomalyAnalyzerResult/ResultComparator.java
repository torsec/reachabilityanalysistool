package it.polito.policytool.ui.view.AnomalyAnalyzerResult;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;

public class ResultComparator extends ViewerComparator {

	/** The property index. */
	private int propertyIndex;
	  
  	/** The Constant DESCENDING. */
  	private static final int DESCENDING = 1;
	  
  	/** The direction. */
  	private int direction = DESCENDING;

	  /**
	 * Instantiates a new result comparator.
	 */
  	public ResultComparator() {
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

	  /* (non-Javadoc)
  	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
  	 */
  	@Override
	  public int compare(Viewer viewer, Object e1, Object e2) {
		PolicyAnomaly a1 = (PolicyAnomaly)  e1;
		PolicyAnomaly a2 = (PolicyAnomaly)  e2;
		String s1="";
		String s2="";
	    int rc = 0;
	    switch (propertyIndex) {
	    case 1:
	      s1 = a1.getRule_set()[0].getName();
	      s2 = a2.getRule_set()[0].getName();
	      break;
	    case 2:
		      s1 = a1.getRule_set()[1].getName();
		      s2 = a2.getRule_set()[1].getName();
		      break;
	    case 0:
		      s1 = a1.getConflict().toString();
		      s2 = a2.getConflict().toString();
		      break;
	    default:
	      rc = 0;
	    }
	    rc=s1.compareTo(s2);
	    // If descending order, flip the direction
	    if (direction == DESCENDING) {
	      rc = -rc;
	    }
	    return rc;
	  }
	
}
