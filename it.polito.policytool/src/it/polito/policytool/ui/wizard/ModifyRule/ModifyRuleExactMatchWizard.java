package it.polito.policytool.ui.wizard.ModifyRule;

import java.util.BitSet;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.InterfaceSelector;
import it.polito.policytoollib.rule.selector.impl.StateSelector;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.MessageBox;

public class ModifyRuleExactMatchWizard extends Wizard{

	protected ModifyRuleExactMatchWizardPage	page;

	private ExactMatchSelector  sel;

	public ModifyRuleExactMatchWizard(ConditionClause cc, String selName, Policy policy) throws InstantiationException, IllegalAccessException, IncompatibleSelectorException{
		super();
		//if(this.sel==null)
		if(cc.get(selName)==null)
			cc.addSelector(selName, PolicyToolUIModel.getInstance().getSelectorTypes().getAllSelectorTypes().get(selName).getClass().newInstance());
		this.sel=(ExactMatchSelector) cc.get(selName);
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page = new ModifyRuleExactMatchWizardPage("Modify "+sel.getLabel()+" Selector "+sel.getName(), sel);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		sel.empty();
		if(sel instanceof StateSelector)
		{
			for(Combo c : page.getCombos())
			{
				try {
					switch(c.getSelectionIndex())
					{
						case 0: break;
						case 1: ((StateSelector) sel).addRange((String) c.getData("state"), 0); break;
						case 2: ((StateSelector) sel).addRange((String) c.getData("state"), 1); break;
						default: break;
					}
				} catch (InvalidRangeException e) {
					System.err.println("Invalid range exact match");
				}
			}
		}
		else
		{
			for(Button b : page.getButtons())
			{
				if(b.getSelection())
				{
					try {
						sel.addRange(b.getText());
					} catch (InvalidRangeException e) {
						System.err.println("Invalid range exact match");
					}
				}
			}
		}
		return true;
		/*System.out.println(page.getRuleName());
		GenericRule newRule = new GenericRule(page.getAction(), page.getConditionClause(), page.getRuleName());
		try {
			if (policy.getResolutionStrategy() instanceof FMRResolutionStrategy)
				policy.insertRule(newRule, page.getExternalData());
			else
				policy.insertRule(newRule);
			tableViewer.refresh();
			policy.removeRule(rule);
			tableViewer.refresh();
		} catch (IncompatibleExternalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DuplicateExternalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationNotPermittedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedSelectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoExternalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnmanagedRuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;*/
	}


}
