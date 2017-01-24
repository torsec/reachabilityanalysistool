package it.polito.policytool.ui.view.PolicyEditor;

import it.polito.policytoollib.policy.impl.Policy;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class PolicyEditorInput implements IEditorInput {
	
	private final Policy policy;
	
	public PolicyEditorInput(Policy policy){
		this.policy = policy;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return policy.getName()+" ("+policy.getPolicyType()+")";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return policy.getName();
	}

	public Policy getPolicy() {
		return policy;
	}

}
