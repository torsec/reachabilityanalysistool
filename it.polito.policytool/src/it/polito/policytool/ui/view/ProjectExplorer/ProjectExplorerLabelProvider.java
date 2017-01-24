package it.polito.policytool.ui.view.ProjectExplorer;

import it.polito.policytoollib.landscape.Landscape;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;

import org.eclipse.jface.viewers.LabelProvider;

public class ProjectExplorerLabelProvider extends LabelProvider {

	   @Override
	   public String getText(Object element) {
	     if(element instanceof PolicyAnalysisModel){
	    	 return ((PolicyAnalysisModel)element).getName();
	     }
	     if(element instanceof Policy){
	    	 return ((Policy)element).getName();
	     }
	     if(element instanceof NamedSet){
	    	 return ((NamedSet)element).getPolicyType().toString();
	     }
	     if(element instanceof Landscape){
	    	 return "Landscape";
	     }
	     if(element instanceof SelectorTypes){
	    	 return "SelectorTypes";
	     }
	    	
	     return null;
	   }

}
