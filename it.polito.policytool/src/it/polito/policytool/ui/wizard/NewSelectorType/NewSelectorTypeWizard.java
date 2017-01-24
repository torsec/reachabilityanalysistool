package it.polito.policytool.ui.wizard.NewSelectorType;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.selector.Selector;

import java.util.LinkedList;

import org.eclipse.jface.wizard.Wizard;

public class NewSelectorTypeWizard extends Wizard{
	
	protected NewSelectorTypeWizardPage page;

	  public NewSelectorTypeWizard() {
	    super();
	    setNeedsProgressMonitor(true);
	  }

	  @Override
	  public void addPages() {
	    page = new NewSelectorTypeWizardPage("New Selector Type", null);
	    addPage(page);
	  }

	@Override
	public boolean performFinish() {
		Selector s = page.getSelectorType();
		s.setLabel(page.getSelectorTypeName());
		PolicyToolUIModel.getInstance().getSelectorTypes().addSelectorType(page.getSelectorTypeName(), s);
		PolicyAnalysisModel model = PolicyToolUIModel.getInstance().getPolicyToolModel();
		LinkedList<Policy> policies = new LinkedList<Policy>();
		policies.addAll(model.getPolicyList());
		policies.addAll(model.getNATPolicyList());
		policies.addAll(model.getVPNPolicyList());
		for(Policy p : policies)
			p.getSelectorNames().add(page.getSelectorTypeName());
		PolicyToolUIModel.getInstance().getSelectorTypesEditorView().refresh();
		for(PolicyEditorView v : PolicyToolUIModel.getInstance().getPolicyEditorView()) v.refresh();
	    return true;
	}
}
