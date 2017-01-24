package it.polito.policytool.ui.view.ProjectExplorer;

import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.utils.PolicyType;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ProjectExplorerContentProvider implements ITreeContentProvider{

	private static final Object[] EMPTY_ARRAY = new Object[0];

    @Override
    public Object[] getElements(Object inputElement) {
    	return getChildren(inputElement);
    }

    //Queried to know if the current node has children
    @Override
    public boolean hasChildren(Object element) {
      if (element instanceof PolicyAnalysisModel) {
        return true;
      }
      if(element instanceof NamedSet)
    	  return true;
      return false;
    }

    //Queried to load the children of a given node
    @Override
    public Object[] getChildren(Object parentElement) {
    	if(parentElement instanceof PolicyAnalysisModel){
    		Object[] ret = new Object[5];
    		PolicyAnalysisModel ptm = (PolicyAnalysisModel)parentElement;
    		ret[0] = new NamedSet(PolicyType.FILTERING, ptm.getPolicyList());
    		ret[1] = new NamedSet(PolicyType.NAT, ptm.getNATPolicyList());
    		ret[2] = new NamedSet(PolicyType.VPN, ptm.getVPNPolicyList());
    		ret[3] = ptm.getLandscape();
    		ret[4] = ptm.getSelectorTypes();
	    	return ret;
	     }
    	if(parentElement instanceof NamedSet){
    		return ((NamedSet)parentElement).getSet().toArray();
    	}
      return EMPTY_ARRAY;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    @Override
    public Object getParent(Object element) {
      return null;
    }

}
