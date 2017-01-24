package it.polito.policytool.ui.wizard.ExecuteQuery;

import java.util.HashSet;
import java.util.Set;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.tools.ReachabilityAnalyser;
import it.polito.policytoollib.rule.impl.GenericRule;

import org.eclipse.jface.wizard.Wizard;

public class ExecuteQueryWizard extends Wizard {

	protected ExecuteQueryWizardPage	page;

	public ExecuteQueryWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page = new ExecuteQueryWizardPage("Execute Query");
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		try {
			System.out.println();
			HashSet<GenericRule> result = PolicyToolUIModel.getInstance().executeQuerry(page.getConditionClause());
			PolicyToolUIModel.getPolicyToolUIModel().getQueryResultView().setInput(result);
			PolicyToolUIModel.getInstance().getQueryResultView().getViewSite().getPage().showView(PolicyToolUIModel.getInstance().getQueryResultView().ID);
			PolicyToolUIModel.getInstance().getQueryResultView().refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
