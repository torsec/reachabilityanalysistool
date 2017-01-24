package it.polito.policytoollib.policy.impl;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.NotPointException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.IllegalParamException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.policy.resolution.ExternalDataResolutionStrategy;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.ApacheOrderAllowDenyResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.ApacheOrderDenyAllowResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.utils.BlockList;
import it.polito.policytoollib.policy.utils.PointList;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.policy.utils.RegexBlockList;
import it.polito.policytoollib.policy.utils.RuleClassifier;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.RegExpSelector;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.TotalOrderedSelector;
import it.polito.policytoollib.rule.selector.impl.StandardRegExpSelector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

public class PolicyImpl implements Policy {
	
	private final Logger LOGGER = Logger.getLogger(RuleClassifier.class.getName());

	private ResolutionStrategy resolutionStrategy;
	private Action defaultAction;
	private LinkedHashSet<GenericRule> rules;
	private String name;
	private boolean usesExternalData = false;
	private HashSet<String> selectorNames;
	private RuleClassifier ruleClassifier;
	private PolicyType policyType;

	public PolicyImpl(ResolutionStrategy resolutionStrategy, Action defaultAction, PolicyType policyType, String name) {
		this.name = name;
		this.defaultAction = defaultAction;
		// Valutare se mantenere Clone
		this.resolutionStrategy = resolutionStrategy.cloneResolutionStrategy();
		this.rules = new LinkedHashSet<GenericRule>();
		this.selectorNames = new HashSet<String>();
		this.policyType = policyType;

		if (resolutionStrategy instanceof ExternalDataResolutionStrategy)
		usesExternalData = true;

		if (resolutionStrategy instanceof ApacheOrderDenyAllowResolutionStrategy && defaultAction != FilteringAction.DENY || resolutionStrategy instanceof ApacheOrderAllowDenyResolutionStrategy && defaultAction != FilteringAction.ALLOW)
			System.err.println("The policy is correct, however it is not a valid Apache Policy.\n The default action is not DENY.");
		ruleClassifier = new RuleClassifier(this);
	}

	@Override
	public void insertRule(GenericRule rule) throws NoExternalDataException, UnsupportedSelectorException {
		if (usesExternalData)
			throw new NoExternalDataException();
		
		insert(rule);
	}
	
	public RuleClassifier getRuleClassifier(){
		return ruleClassifier;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> void insertRule(GenericRule rule, S externalData) throws IncompatibleExternalDataException, DuplicateExternalDataException, UnsupportedSelectorException {
		if (!usesExternalData)
			throw new IncompatibleExternalDataException();

		((ExternalDataResolutionStrategy<GenericRule, S>) resolutionStrategy).setExternalData(rule, externalData);
		
		insert(rule);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <S> void insertRule(GenericRule rule, String externalData) throws IncompatibleExternalDataException, DuplicateExternalDataException, UnsupportedSelectorException {
		if (!usesExternalData)
			throw new IncompatibleExternalDataException();

		((ExternalDataResolutionStrategy<GenericRule, S>) resolutionStrategy).setExternalData(rule, externalData);
		
		insert(rule);
	}
	
	@SuppressWarnings("unchecked")
	public void insertRulesOnTop(List<GenericRule> new_rules) {
		if(resolutionStrategy instanceof FMRResolutionStrategy)
		{
			int position = 0;
			try {
				((FMRResolutionStrategy) resolutionStrategy).shiftAll(new_rules.size());
				for (GenericRule r : new_rules)
					((ExternalDataResolutionStrategy<GenericRule, Integer>) resolutionStrategy).setExternalData(r, position++);
			} catch (IncompatibleExternalDataException e) {
				//unreachable catch
			} catch (DuplicateExternalDataException e) {
				//unreachable catch
			}
			this.rules.addAll(new_rules);
			for (GenericRule rule : new_rules)
				selectorNames.addAll(rule.getConditionClause().getSelectorsNames());
		}
	}

	private void insert(GenericRule rule) throws UnsupportedSelectorException{
		rules.add(rule);
		
		for(String sn:rule.getConditionClause().getSelectorsNames()){
			if(!selectorNames.contains(sn)){
				selectorNames.add(sn);
				BlockList blockList = null;
				Selector s = rule.getConditionClause().get(sn).selectorClone();
				s.full();
				if(s instanceof ExactMatchSelector || s instanceof TotalOrderedSelector){
					try {
						blockList = new PointList(s, sn);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				
				if(s instanceof RegExpSelector || s instanceof StandardRegExpSelector){
					try {
						blockList = new RegexBlockList((RegExpSelector)s);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				if(blockList == null){
					LOGGER.severe(sn);
				}
				ruleClassifier.addSelector(sn, blockList);
			}
		}
		
		
		try {
			ruleClassifier.addRule(rule);
		} catch (UnsupportedSelectorException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertAll(Collection<GenericRule> rules) throws NoExternalDataException, UnsupportedSelectorException {
		if (usesExternalData)
			throw new NoExternalDataException();

		for(GenericRule r : rules)
			insertRule(r);
		for (GenericRule rule : rules) {
			selectorNames.addAll(rule.getConditionClause().getSelectorsNames());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> void insertAll(HashMap<GenericRule, S> rules) throws NoExternalDataException, IncompatibleExternalDataException, DuplicateExternalDataException, UnsupportedSelectorException {
		if (!usesExternalData)
			throw new IncompatibleExternalDataException();

		// Come verificare che S sia compatibile?
		for (GenericRule r : rules.keySet())
			((ExternalDataResolutionStrategy<GenericRule, S>) resolutionStrategy).setExternalData(r, rules.get(r));

		for(Entry<GenericRule,S> e : rules.entrySet())
			insertRule(e.getKey(), e.getValue());
		//this.rules.addAll(rules.keySet());
		for (GenericRule rule : rules.keySet()) {
			selectorNames.addAll(rule.getConditionClause().getSelectorsNames());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void removeRule(GenericRule rule) throws UnmanagedRuleException {
		if (usesExternalData)
			((ExternalDataResolutionStrategy<GenericRule, Object>) resolutionStrategy).clearExternalData(rule);

		if (rules.contains(rule)) {
			rules.remove(rule);
		} else
			throw new UnmanagedRuleException();
		// System.err.println("Rule\n"+ rule + "not contained in the DB");

	}

	@Override
	@SuppressWarnings("unchecked")
	public void removeAll(Collection<GenericRule> rules) throws UnmanagedRuleException {
		this.rules.removeAll(rules);

		for (GenericRule rule : rules) {
			if (rules.contains(rule)) {
				// this.rules.remove(rule);
				if (usesExternalData)
					((ExternalDataResolutionStrategy<GenericRule, Object>) resolutionStrategy).clearExternalData(rule);

			} else
				throw new UnmanagedRuleException();
			// System.err.println("Rule\n"+ rule + "not contained in the DB");
		}
	}

	@Override
	public boolean containsRule(GenericRule rule) {
		return rules.contains(rule);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void clearRules() {
		for (GenericRule rule : rules) {
			if (usesExternalData)
				((ExternalDataResolutionStrategy<GenericRule, Object>) resolutionStrategy).clearExternalData(rule);
		}

		rules.clear();
		// rules = new HashSet<GenericRule>();
	}

	/**
	 * @return the defaultAction
	 */
	@Override
	public Action getDefaultAction() {
		return defaultAction;
	}

	/**
	 * @return the resolutionStrategy
	 */
	@Override
	public ResolutionStrategy getResolutionStrategy() {
		return resolutionStrategy;
	}

	/**
	 * @return the rules
	 */
	@Override
	public Set<GenericRule> getRuleSet() {
		return rules;
	}

	@Override
	public int size() {
		return rules.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("----BEGIN POLICY---------------------------------\n");
		buf.append("Resolution Strategy: " + resolutionStrategy + "\n");
		buf.append("-------------------------------------------------\n");
		buf.append("Default Action: " + defaultAction + "\n");
		buf.append("-------------------------------------------------\n");
		buf.append("Rules (Total " + rules.size() + "): \n");
		if (resolutionStrategy instanceof ExternalDataResolutionStrategy) {
		{
			if(resolutionStrategy instanceof FMRResolutionStrategy)
			{
				GenericRule[] orderedRules = new GenericRule[rules.size()];
				for (Entry<GenericRule, Integer> entry : (((FMRResolutionStrategy) resolutionStrategy).getPriorities()).getData().entrySet())
					orderedRules[entry.getValue()] = entry.getKey();
				for (GenericRule rule : orderedRules)
				{
					buf.append("\nRule "+(((FMRResolutionStrategy)resolutionStrategy).getExternalData(rule)));
					buf.append("\n"+rule+"\n");
				}
			} 
			else
			{
				ExternalDataResolutionStrategy<GenericRule, ?> ext = (ExternalDataResolutionStrategy<GenericRule, ?>) resolutionStrategy;
				for (GenericRule rule : rules) {
					buf.append(rule + "-->" + (ext.getExternalData(rule)) + "\n");
				}
			}
		}
		} else
			for (GenericRule rule : rules) {
				buf.append(rule);
			}
		buf.append("\n----END POLICY-----------------------------------\n");
		return buf.toString();
	}

	@Override
	public Action evalAction(ConditionClause c) throws NotPointException, SecurityException, IllegalArgumentException, IllegalParamException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, NoExternalDataException, InvalidActionException {

		if (!c.isPoint(selectorNames)) {
			throw new NotPointException();
		}

		HashSet<GenericRule> ruleSet = match(c);

		if (ruleSet.size() == 0)
			return defaultAction;

		return resolutionStrategy.composeActions(ruleSet.toArray(new GenericRule[ruleSet.size()]));
	}

	@Override
	public HashSet<GenericRule> match(ConditionClause c) throws NotPointException, SecurityException, IllegalArgumentException, IllegalParamException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {

		if (!c.isPoint(selectorNames))
			throw new NotPointException();

		HashSet<GenericRule> ruleSet = new HashSet<GenericRule>();
		for (GenericRule r : rules) {
			if (r.isIntersecting(c)) {
				ruleSet.add(r);
			}
		}

		return ruleSet;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public HashSet<String> getSelectorNames() {
		return selectorNames;
	}

	@Override
	public Policy policyClone() {
		Policy p = null;
		try {
			p = new PolicyImpl(resolutionStrategy.cloneResolutionStrategy(), defaultAction, policyType, name);
			if (resolutionStrategy instanceof ExternalDataResolutionStrategy) {
				for (GenericRule rule : rules)
					p.insertRule(rule.ruleClone(), ((ExternalDataResolutionStrategy) resolutionStrategy).getExternalData(rule));
			} else {
				for (GenericRule rule : rules)
					p.insertRule(rule.ruleClone());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public PolicyType getPolicyType() {
		return this.policyType;
	}

	@Override
	public <S> void insertRuleNoCheck(GenericRule rule, S externalData)
			throws IncompatibleExternalDataException,
			OperationNotPermittedException, UnsupportedSelectorException {
		if (!usesExternalData)
			throw new IncompatibleExternalDataException();

		((ExternalDataResolutionStrategy<GenericRule, S>) resolutionStrategy).setExternalDataNoCheck(rule, externalData);
		
		insert(rule);
	}

}