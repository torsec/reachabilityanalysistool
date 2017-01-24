package it.polito.policytoollib.policy.resolution.impl;

import it.polito.policytoollib.exception.policy.DuplicatedRuleException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.resolution.ResolutionComparison;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.impl.GenericRule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class MultiTypeResolutionStrategy extends
		ResolutionStrategy {

	private static final String label = "MultiTypeResolutionStrategy";

	private LinkedList<Policy> policy_list;

	private Action defaultAction;

	public MultiTypeResolutionStrategy(LinkedList<Policy> policy_list,
			Action defaultAction) {
		this.policy_list = policy_list;
		this.defaultAction = defaultAction;
	}

	@Override
	public Action composeActions(GenericRule r1, GenericRule r2)
			throws NoExternalDataException, IncompatibleExternalDataException,
			InvalidActionException {
		GenericRule[] rules = new GenericRule[2];
		rules[1] = r1;
		rules[2] = r2;
		return composeActions(rules);
	}

	@Override
	public Action composeActions(GenericRule[] rules)
			throws NoExternalDataException, InvalidActionException {
		if (rules.length == 0)
			return defaultAction;

		HashMap<Policy, HashSet<GenericRule>> rp = new HashMap<Policy, HashSet<GenericRule>>();

		for (Policy p : policy_list) {
			HashSet<GenericRule> hs = new HashSet<GenericRule>();
			rp.put(p, hs);
		}

		boolean found = false;
		for (GenericRule r : rules) {
			found = false;
			for (Policy p : policy_list) {
				HashSet<GenericRule> rs = rp.get(p);
				if (p.containsRule(r)) {
					rs.add(r);
					found = true;
				}
			}
			if (!found) {
				System.out.println("E:" + r.getName() + " : " + r.hashCode());
				throw new NoExternalDataException();
			}
		}

		for (Policy p : policy_list) {
			if (!rp.get(p).isEmpty()) {
				// System.out.println();
				// System.out.println(p.getName());
				// System.out.println(rp.get(p));
				// System.out.println(p.getResolutionStrategy().composeActions(rp.get(p)));
				// System.out.println();
				return p.getResolutionStrategy().composeActions(rp.get(p));
			}
		}

		return defaultAction;

	}

	@Override
	public ResolutionComparison compare(GenericRule r1, GenericRule r2)
			throws NoExternalDataException, DuplicatedRuleException,
			UnmanagedRuleException {
		int p1 = 0, p2 = 0;
		int i = 0;
		for (Policy p : policy_list) {
			i++;
			if (p.containsRule(r1))
				p1 = i;
			if (p.containsRule(r2))
				p2 = i;
			if (p.containsRule(r1) && p.containsRule(r2))
				return p.getResolutionStrategy().compare(r1, r2);
		}

		if (p1 < p2)
			return ResolutionComparison.DIFFERENT_SET_LESS;
		if (p1 > p2)
			return ResolutionComparison.DIFFERENT_SET_GREATER;
		return ResolutionComparison.DIFFERENT_SET;

	}

	@Override
	public boolean isActionEquivalent(GenericRule r1, GenericRule r2) {
		return r1.getAction().equals(r2.getAction());
	}

	@Override
	public ResolutionStrategy cloneResolutionStrategy() {
		MultiTypeResolutionStrategy res = new MultiTypeResolutionStrategy(
				policy_list, defaultAction);

		return res;
	}

	@Override
	public String toString() {
		return label;
	}
}
