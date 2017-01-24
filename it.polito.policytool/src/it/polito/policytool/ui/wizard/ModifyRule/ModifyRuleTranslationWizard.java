package it.polito.policytool.ui.wizard.ModifyRule;

import java.util.Vector;

import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.TotalOrderedSelector;

import org.eclipse.jface.wizard.Wizard;

public class ModifyRuleTranslationWizard extends Wizard {

	protected ModifyRuleTranslationWizardPage	page;

	private ConditionClause		cc;
	private Policy				policy;

	public ModifyRuleTranslationWizard(ConditionClause cc, Policy policy) {
		super();
		this.cc=cc;
		this.policy=policy;
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page = new ModifyRuleTranslationWizardPage("Modify Translation", cc, policy);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		
		try {
			Vector<Selector> selectors = new Vector<Selector>();
			for(java.util.Map.Entry<String, Selector> e : cc.getSelectors().entrySet())
			{
				if(e.getValue() instanceof TotalOrderedSelector) selectors.add(e.getValue());
			}
			for(Selector s : selectors)
				cc.getSelectors().remove(s);
			for(java.util.Map.Entry<String, Selector> e : page.getConditionClause().getSelectors().entrySet())
			{
				if(e.getValue() instanceof TotalOrderedSelector) cc.addSelector(e.getKey(), e.getValue());
			}
		} catch (IncompatibleSelectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*cc.empty();
		try {
			cc.addSelectorsFromConditionClause(page.getConditionClause());
		} catch (IncompatibleSelectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return true;
	}

}
