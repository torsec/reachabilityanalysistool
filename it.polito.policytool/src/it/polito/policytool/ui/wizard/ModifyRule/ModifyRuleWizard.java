package it.polito.policytool.ui.wizard.ModifyRule;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.ExternalDataResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.ActionData;
import it.polito.policytoollib.rule.action.TransformatonAction;
import it.polito.policytoollib.rule.impl.GenericRule;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Text;

public class ModifyRuleWizard extends Wizard {

	protected ModifyRuleWizardPage	page;

	private Policy				policy;
	private GenericRule			rule;
	private TableViewer 		tableViewer;

	public ModifyRuleWizard(Policy policy, GenericRule rule, TableViewer tableViewer) {
		super();
		this.policy = policy;
		this.rule=rule;
		this.tableViewer = tableViewer;
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		if (policy.getResolutionStrategy() instanceof FMRResolutionStrategy)
			page = new ModifyRuleWizardPage("Modify "+policy.getPolicyType()+" Rule "+rule.getName(), true, policy, rule);
		else
			page = new ModifyRuleWizardPage("Modify "+policy.getPolicyType()+" Rule "+rule.getName(), false, policy, rule);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		System.out.println(page.getRuleName());
		Action a = page.getAction();
		ActionData[] adVector = new ActionData[page.getActionDataTextMap().size()];
		int i=0;
		for(Entry<String, Text> e : page.getActionDataTextMap().entrySet())
			adVector[i++] = new ActionData(e.getKey(), e.getValue().getText());
		if(adVector.length!=0)
			a.setActionData(adVector);
		GenericRule newRule = new GenericRule(a, page.getConditionClause(), page.getRuleName());
		String oldpriority = null;
		if (policy.getResolutionStrategy() instanceof ExternalDataResolutionStrategy)
			oldpriority = ((ExternalDataResolutionStrategy<GenericRule, ?>)(policy.getResolutionStrategy())).getExternalData(rule).toString();
		try {
			policy.removeRule(rule);
			if (policy.getResolutionStrategy() instanceof FMRResolutionStrategy)
				policy.insertRule(newRule, page.getExternalData());
			else
				policy.insertRule(newRule);
			tableViewer.refresh();
			return true;
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
		try {
			if (policy.getResolutionStrategy() instanceof FMRResolutionStrategy)
				policy.insertRule(rule, oldpriority);
			else
				policy.insertRule(rule);
		} catch (IncompatibleExternalDataException
				| DuplicateExternalDataException
				| OperationNotPermittedException | UnsupportedSelectorException
				| NoExternalDataException e1) {
			// unreachable catch
		}
		return false;
	}

}
