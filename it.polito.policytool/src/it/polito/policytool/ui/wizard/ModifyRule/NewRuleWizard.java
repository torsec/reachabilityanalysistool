package it.polito.policytool.ui.wizard.ModifyRule;

import java.util.Map.Entry;
import java.util.Vector;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.ExternalDataResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.ActionData;
import it.polito.policytoollib.rule.action.TransformatonAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Text;

public class NewRuleWizard extends Wizard {

	protected ModifyRuleWizardPage	page;

	private Policy				policy;
	
	private GenericRule			rule;
	
	private TableViewer 		tableViewer;

	public NewRuleWizard(Policy policy, TableViewer tableViewer) {
		super();
		this.policy = policy;
		this.tableViewer = tableViewer;
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		rule = new GenericRule(null, new ConditionClause(), null);
		if (policy.getResolutionStrategy() instanceof ExternalDataResolutionStrategy<?, ?>)
			page = new ModifyRuleWizardPage("New "+policy.getPolicyType()+" Rule", true, policy, rule);
		else
			page = new ModifyRuleWizardPage("New "+policy.getPolicyType()+" Rule", false, policy, rule);
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
		if(a instanceof TransformatonAction)
			((TransformatonAction) a).setTransformation(page.getTransformation());
		rule = new GenericRule(a, page.getConditionClause(), page.getRuleName());
		rule.setName(page.getRuleName());
		try {
			if (policy.getResolutionStrategy() instanceof FMRResolutionStrategy)
				policy.insertRule(rule, page.getExternalData());
			else
				policy.insertRule(rule);
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
		}
		return true;
	}
}
