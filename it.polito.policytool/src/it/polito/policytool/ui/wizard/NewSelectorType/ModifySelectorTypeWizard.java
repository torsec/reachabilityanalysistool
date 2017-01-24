package it.polito.policytool.ui.wizard.NewSelectorType;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView;
import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jface.wizard.Wizard;

public class ModifySelectorTypeWizard extends Wizard{
	
	protected NewSelectorTypeWizardPage page;
	String selectorName;
	String oldSelectorType;

	  public ModifySelectorTypeWizard(String selectorName) {
	    super();
	    this.selectorName=selectorName;
	    oldSelectorType = PolicyToolUIModel.getInstance().getSelectorTypes().getSelectorType(selectorName).getName();
	    setNeedsProgressMonitor(true);
	  }

	  @Override
	  public void addPages() {
	    page = new NewSelectorTypeWizardPage("New Selector Type", selectorName);
	    addPage(page);
	  }

	@Override
	public boolean performFinish() {
		Selector s = page.getSelectorType();
		s.setLabel(page.getSelectorTypeName());
		PolicyToolUIModel.getInstance().getPolicyToolModel().getSelectorTypes().modifySelectorType(selectorName, page.getSelectorTypeName(), s);
		PolicyToolUIModel.getInstance().getSelectorTypesEditorView().refresh();
		for(PolicyEditorView v : PolicyToolUIModel.getInstance().getPolicyEditorView()) v.refresh();
	    return true;
	}
}
