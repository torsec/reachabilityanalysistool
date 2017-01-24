package it.polito.policytool.ui.wizard.ExecuteDistributedAnalysis;

import java.util.HashMap;
import java.util.Set;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;

import org.eclipse.jface.wizard.Wizard;

public class ExecuteDistributedAnalysisWizard extends Wizard {

	protected ExecuteDistributedAnalysisWizardPage	page;

	public ExecuteDistributedAnalysisWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page = new ExecuteDistributedAnalysisWizardPage("Execute Query");
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		String[] zones = page.getSelectedZones();
		try {
			Set<PolicyAnomaly> anomalies = PolicyToolUIModel.getInstance().getPolicyToolModel().getDistributedConflicts(zones[0], zones[1]);
			PolicyToolUIModel.getPolicyToolUIModel().getAnomalyAnalyzerResultView().setInput(anomalies,zones[0],zones[1]);
			PolicyToolUIModel.getInstance().getAnomalyAnalyzerResultView().getViewSite().getPage().showView(PolicyToolUIModel.getInstance().getAnomalyAnalyzerResultView().ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
