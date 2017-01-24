package it.polito.policytool.ui.wizard.NewPolicy;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.view.PolicyEditor.PolicyEditorInput;
import it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class NewPolicyWizard extends Wizard {

	protected NewPolicyWizardPage	page;
	private PolicyType				policyType;

	public NewPolicyWizard(PolicyType policyType) {
		super();
		setNeedsProgressMonitor(true);
		this.policyType = policyType;
	}

	@Override
	public void addPages() {
		page = new NewPolicyWizardPage("New " + policyType + "Policy", policyType);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		PolicyImpl p = null;
		if (policyType == PolicyType.FILTERING)
			PolicyToolUIModel.getInstance().addPolicy(p=new PolicyImpl(page.getResolutionStartegy(), page.getDefaultAction(), policyType, page.getPolicyName()));
		if (policyType == PolicyType.NAT)
			PolicyToolUIModel.getInstance().addNATPolicy(p=new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DUMMY, policyType, page.getPolicyName()));
		if (policyType == PolicyType.VPN)
			PolicyToolUIModel.getInstance().addVPNPolicy(p=new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DUMMY, policyType, page.getPolicyName()));
		if(p==null) return false;
		PolicyEditorInput input = new PolicyEditorInput(p);
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page2 = window.getActivePage();
		
        try {
          page2.openEditor(input, PolicyEditorView.ID);

        } catch (PartInitException e) {
          throw new RuntimeException(e);
        }
		return true;
	}

}
