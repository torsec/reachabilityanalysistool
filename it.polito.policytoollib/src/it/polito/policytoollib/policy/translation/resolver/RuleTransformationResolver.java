package it.polito.policytoollib.policy.translation.resolver;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.policy.impl.MultiPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.ExternalDataResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.DummyAction;
import it.polito.policytoollib.rule.action.IPSecAction;
import it.polito.policytoollib.rule.action.IPSecActionType;
import it.polito.policytoollib.rule.action.NATAction;
import it.polito.policytoollib.rule.action.NATActionType;
import it.polito.policytoollib.rule.action.TransformatonAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.impl.NATRule;
import it.polito.policytoollib.rule.selector.Selector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.text.ZoneView;

public class RuleTransformationResolver {

	public LinkedList<LinkedList<Policy>> getPolicyList(LinkedList<LinkedList<SecurityControl>> firewall_list) throws Exception {
		LinkedList<LinkedList<Policy>> new_policy_list = new LinkedList<LinkedList<Policy>>();

		LinkedList<GenericRule> pre_transformation = new LinkedList<GenericRule>();
		LinkedList<GenericRule> post_transformation = new LinkedList<GenericRule>();
		HashMap<GenericRule, GenericRule> security_transformation = new HashMap<GenericRule, GenericRule>();
		for (LinkedList<SecurityControl> fw_list : firewall_list) {
			pre_transformation.clear();
			post_transformation.clear();
			security_transformation.clear();
			LinkedList<Policy> new_p_list = new LinkedList<Policy>();
			for (SecurityControl fw : fw_list) {
				//Policy new_p_org = fw.getPolicy().policyClone();
				Policy new_p_org = fw.getPolicy();
				LinkedList<Policy> multi_policy_list = new LinkedList<Policy>();
				multi_policy_list.add(new_p_org);
//				System.out.println("--------------------------------------");
//				System.out.println("multi policy");
//				System.out.println(multi_policy_list);
//				System.out.println("--------------------------------------");
//				System.out.println("fw");
//				System.out.println(fw.getName());
//				System.out.println("--------------------------------------");
//				System.out.println("getPolicy");
//				System.out.println(fw.getPolicy());
//				System.out.println("--------------------------------------");
//				System.out.println("getDefaultAction");
//				System.out.println(fw.getPolicy().getDefaultAction());
//				System.out.println("--------------------------------------");
//				System.out.println("getName");
//				System.out.println(fw.getPolicy().getName());
				Policy new_p = new MultiPolicy(multi_policy_list, fw.getPolicy().getDefaultAction(), PolicyType.FILTERING, fw.getPolicy().getName());

				Policy new_p_trans_da = new PolicyImpl(new FMRResolutionStrategy(), new_p_org.getDefaultAction(), PolicyType.FILTERING, "");
				((MultiPolicy) new_p).getPolicyList().addFirst(new_p_trans_da);

				Policy new_p_trans = new PolicyImpl(new_p_org.getResolutionStrategy().cloneResolutionStrategy(), new_p_org.getDefaultAction(), PolicyType.FILTERING, "");
				((MultiPolicy) new_p).getPolicyList().addFirst(new_p_trans);
				
				if (fw.getVPN() != null) {
					for (GenericRule rule : fw.getVPN().getRuleSet()) {
						if (rule.getAction() instanceof IPSecAction) {
							if (((IPSecAction) rule.getAction()).getType() == IPSecActionType.INVERT_ESP) {
								for (GenericRule r : security_transformation.keySet()) {
									if (((IPSecAction) r.getAction()).getTransformation().isConditionEquivalent(rule.getConditionClause())) {
										pre_transformation.remove(security_transformation.get(r));
									}
								}
							}
						}
					}
				}

				if (fw.getNAT() != null) {
					for (GenericRule rule : fw.getNAT().getRuleSet()) {
						if (rule.getAction() instanceof NATAction) {
							if (((NATAction) rule.getAction()).getNATAction() == NATActionType.PRENAT) {
								pre_transformation.add(rule);
							}
							if (((NATAction) rule.getAction()).getNATAction() == NATActionType.POSTNAT) {
								post_transformation.add(rule);
							}
							for (GenericRule r : security_transformation.keySet()) {
								if (((IPSecAction) r.getAction()).getTransformation().isConditionSubsetOrEquivalent(rule.getConditionClause())) {
									for(String sn:((NATAction)rule.getAction()).getTransformation().getSelectorsNames()){
										((IPSecAction) r.getAction()).getTransformation().setSelector(sn, ((NATAction)rule.getAction()).getTransformation().get(sn));
									}
								}
							}
						}
					}
				}
				
				if (fw.getVPN() != null) {
					for (GenericRule rule : fw.getVPN().getRuleSet()) {
						if (rule.getAction() instanceof IPSecAction) {
							if (((IPSecAction) rule.getAction()).getType() == IPSecActionType.ESP){
								post_transformation.add(rule);
								security_transformation.put(rule.ruleClone(),rule);
							}
						}
					}
				}

				Iterator<GenericRule> it = pre_transformation.descendingIterator();
				int i = 1;
				while (it.hasNext()) {
					
					GenericRule r = it.next();
					ConditionClause filter_condition = r.getConditionClause();
					ConditionClause transformation_condition = ((TransformatonAction) r.getAction()).getTransformation();
					insert_rule(new_p_trans_da, new_p_trans_da, new_p_trans, new_p_org, new_p, r, filter_condition, transformation_condition, i++);

				}
				pre_transformation.addAll(post_transformation);
				post_transformation.clear();
				new_p_list.add(new_p);

			}
			new_policy_list.add(new_p_list);
		}
		
		return new_policy_list;
	}

	private void insert_rule(Policy new_p_trans_da_org, Policy new_p_trans_da, Policy new_p_trans, Policy new_p_org, Policy new_p, GenericRule r, ConditionClause filter_condition, ConditionClause transformation_condition, int i) throws NoExternalDataException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, IncompatibleExternalDataException, DuplicateExternalDataException, OperationNotPermittedException, IncompatibleSelectorException, UnsupportedSelectorException {

		String transType = "TRANS";
		if (r.getAction() instanceof NATAction)
			transType = "NAT";
		if (r.getAction() instanceof IPSecAction)
			transType = "VPN";

		LinkedList<GenericRule> rule_list = new LinkedList<GenericRule>();

		Action a = new_p_org.getDefaultAction();

		if (transformation_condition != null) {
			int ii = 1;
			for (GenericRule rr : new_p_trans_da_org.getRuleSet()) {
				if (rr.isIntersecting(transformation_condition)) {
					Policy new_p_nat_da2 = new PolicyImpl(new FMRResolutionStrategy(), new_p_org.getDefaultAction(), PolicyType.FILTERING, "");
					((MultiPolicy) new_p).getPolicyList().addFirst(new_p_nat_da2);

					Policy new_p_nat2 = new PolicyImpl(new_p_org.getResolutionStrategy().cloneResolutionStrategy(), new_p_org.getDefaultAction(), PolicyType.FILTERING, "");
					((MultiPolicy) new_p).getPolicyList().addFirst(new_p_nat2);

					ConditionClause cc = rr.getConditionClause().conditionClauseClone();
					cc.intersection(transformation_condition);
					NATRule rrr = new NATRule(rr.getAction(), cc, rr.getName());
					for (GenericRule rule : ((NATRule) rr).getOriginalRules()) {
						rrr.addOriginalRule(rule);
					}

					insert_rule(new_p_trans_da_org, new_p_nat_da2, new_p_nat2, new_p_org, new_p, rrr, filter_condition, ((NATRule) rr).getNATRule(), ii++);
				}
			}

			// calculate action
			for (GenericRule rr : new_p_org.getRuleSet()) {

				if (rr.isIntersecting(transformation_condition)) {
					Action aa = rr.getAction();
					ConditionClause new_cc = rr.getConditionClause().conditionClauseClone();
					new_cc.intersection(transformation_condition);
					for (String s : filter_condition.getSelectorsNames()) {
						new_cc.setSelector(s, filter_condition.get(s));
					}

					NATRule new_rr = new NATRule(aa, new_cc, transType + "-(" + r.getName() + ")-!(" + rr.getName() + ")");
					new_rr.setNATRule(transformation_condition);
					new_rr.addOriginalRule(rr);
					for (String s : filter_condition.getSelectorsNames()) {
						new_rr.getConditionClause().setSelector(s, filter_condition.get(s));
					}

					if (new_p_org.getResolutionStrategy() instanceof ExternalDataResolutionStrategy)
						new_p_trans.insertRuleNoCheck(new_rr, ((ExternalDataResolutionStrategy) (new_p_org.getResolutionStrategy())).getExternalData(rr));
					else
						new_p_trans.insertRule(new_rr);

					rule_list.add(rr);
				}
			}
		}

		NATRule new_pre_rule = new NATRule(DummyAction.DUMMY, filter_condition, transType + "DA-(" + r.getName() + ")");

		new_pre_rule.setNATRule(transformation_condition);
		new_pre_rule.addAllOriginalRules(rule_list);

		new_p_trans_da.insertRule(new_pre_rule, i++);
	}
}
