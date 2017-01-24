package it.polito.policytool;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import it.polito.policytool.ui.view.AnomalyAnalyzerResult.AnomalyAnalyzerResultView;
import it.polito.policytool.ui.view.ComparisonResult.ComparisonResultView;
import it.polito.policytool.ui.view.LandscapeEditor.LandscapeEditorView;
import it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView;
import it.polito.policytool.ui.view.ProjectExplorer.ProjectExplorerView;
import it.polito.policytool.ui.view.QueryResult.QueryResultView;
import it.polito.policytool.ui.view.SelectorTypesEditor.SelectorTypesEditorView;
import it.polito.policytoollib.landscape.Landscape;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.util.ClassList;

public class PolicyToolUIModel {
	
	private static PolicyToolUIModel policyToolUIModel = null;
	
	private ProjectExplorerView projectExplorerView = null;
	private SelectorTypesEditorView selectorTypesEditorView = null;
	private AnomalyAnalyzerResultView anomalyAnalyzerResultView = null;
	private QueryResultView queryResultView = null;
	private ComparisonResultView comparisonResultView = null;
	private LandscapeEditorView landscapeEditorView = null;
	private Vector<PolicyEditorView> policyEditorView = null;
	private HashMap<String, Class<?>> classes;
	private HashMap<String, Class<?>> userClasses = new HashMap<String, Class<?>>();
	
	private PolicyAnalysisModel policyToolModel = null;
	
	public PolicyAnalysisModel getPolicyToolModel() {
		return policyToolModel;
	}

	private PolicyToolUIModel(){
		policyEditorView = new Vector<PolicyEditorView>();
//		String classpath = System.getProperty("java.class.path");
//		File f = new File(classpath);
//		System.out.println(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
//		File dir = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
//		System.out.println(dir.getAbsolutePath());
//		File[] files = dir.listFiles(new FilenameFilter() {
//		    public boolean accept(File dir, String name) {
//		        return name.toLowerCase().startsWith("it.polito.policytoollib");//name.toLowerCase().endsWith(".txt");
//		    }
//		});
//		try {
//			classes = ClassList.getFromJARFile2(files[0].getAbsolutePath(), "it/polito/policytoollib/rule/selector/impl");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public static PolicyToolUIModel getInstance(){
		if(policyToolUIModel == null)
			policyToolUIModel = new PolicyToolUIModel();
		return policyToolUIModel;
	}

	public ProjectExplorerView getProjectExplorerView() {
		return projectExplorerView;
	}

	public void setProjectExplorerView(ProjectExplorerView projectExplorerView) {
		this.projectExplorerView = projectExplorerView;
	}


	public void setQueryResultView(QueryResultView queryResultView) {
		this.queryResultView = queryResultView;
	}
	
	public void setComparisonResultView(ComparisonResultView comparisonResultView) {
		this.comparisonResultView = comparisonResultView;
	}

	public void setPolicyToolModel(PolicyAnalysisModel policyToolModel) {
		this.policyToolModel = policyToolModel;
		this.projectExplorerView.setInput(policyToolModel);
		if(this.selectorTypesEditorView!=null) this.selectorTypesEditorView.refresh();
		if(this.landscapeEditorView!=null) this.landscapeEditorView.refresh(true);
	}
	
	public SelectorTypesEditorView getSelectorTypesEditorView(){
		return selectorTypesEditorView;
	}
	
	public QueryResultView getQueryResultView(){
		return queryResultView;
	}
	
	public ComparisonResultView getComparisonResultView(){
		return comparisonResultView;
	}

	public void setSelectorTypesEditorView(SelectorTypesEditorView selectorTypesEditorView) {
		this.selectorTypesEditorView = selectorTypesEditorView;
	}
	
	public AnomalyAnalyzerResultView getAnomalyAnalyzerResultView(){
		return this.anomalyAnalyzerResultView;
	}

	public void setAnomalyAnalyzerResultView(AnomalyAnalyzerResultView anomalyAnalyzerResultView) {
		this.anomalyAnalyzerResultView = anomalyAnalyzerResultView;
		
	}

	public LandscapeEditorView getLandscapeEditorView() {
		return landscapeEditorView;
	}

	public void setLandscapeEditorView(LandscapeEditorView landscapeEditorView) {
		this.landscapeEditorView = landscapeEditorView;
	}

	public static PolicyToolUIModel getPolicyToolUIModel() {
		return policyToolUIModel;
	}

	public static void setPolicyToolUIModel(PolicyToolUIModel policyToolUIModel) {
		PolicyToolUIModel.policyToolUIModel = policyToolUIModel;
	}

	public Vector<PolicyEditorView> getPolicyEditorView() {
		return policyEditorView;
	}

	public void setPolicyEditorView(Vector<PolicyEditorView> policyEditorView) {
		this.policyEditorView = policyEditorView;
	}

	public Set<PolicyAnomaly> getSingleAnomalies(String policy) {
		return policyToolModel.getSingleAnomalies(policy);
	}

	public void addPolicy(PolicyImpl policy) {
		policyToolModel.addPolicy(policy);
		projectExplorerView.setInput(policyToolModel);
	}
	
	public void addNATPolicy(PolicyImpl policy) {
		policyToolModel.addNATPolicy(policy);
		projectExplorerView.setInput(policyToolModel);
	}
	
	public void addVPNPolicy(PolicyImpl policy) {
		policyToolModel.addVPNPolicy(policy);
		projectExplorerView.setInput(policyToolModel);
	}
	
	public void addRoutingPolicy(PolicyImpl policy) {
		policyToolModel.addRoutingPolicy(policy);
		projectExplorerView.setInput(policyToolModel);
	}
	
	public void removePolicy(Policy policy) {
		policyToolModel.removePolicy(policy);
		projectExplorerView.setInput(policyToolModel);
	}
	
	public HashSet<GenericRule> executeQuerry(ConditionClause zone_rule) throws Exception{
		return policyToolModel.executeQuerry(zone_rule);
	}

	public Policy getPolicy(String name) {
		return policyToolModel.getPolicy(name);
	}

	public Collection<Policy> getPolicyList() {
		return policyToolModel.getPolicyList();
	}
	
	public Collection<Policy> getNATPolicyList() {
		return policyToolModel.getNATPolicyList();
	}

	
	public Collection<Policy> getVPNPolicyList() {
		return policyToolModel.getVPNPolicyList();
	}

	public Collection<Policy> getRoutingPolicyList() {
		return policyToolModel.getRoutingPolicyList();
	}

	public SelectorTypes getSelectorTypes() {
		return policyToolModel.getSelectorTypes();
	}

	public Landscape getLandscape() {
		return policyToolModel.getLandscape();
	}

	public HashMap<String, Class<?>> getClasses() {
		return classes;
	}

	public HashMap<String, Class<?>> getUserClasses() {
		return userClasses;
	}

	public void setClasses(HashMap<String, Class<?>> classes) {
		this.classes = classes;
	}

	public void setUserClasses(HashMap<String, Class<?>> userClasses) {
		this.userClasses = userClasses;
	}

	
	
	
}
