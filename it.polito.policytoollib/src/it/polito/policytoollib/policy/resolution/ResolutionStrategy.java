package it.polito.policytoollib.policy.resolution;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.DuplicatedRuleException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;

import java.util.Collection;

public abstract class ResolutionStrategy {
	
	public abstract ResolutionStrategy cloneResolutionStrategy();

	public abstract ResolutionComparison compare(GenericRule r1, GenericRule r2) throws NoExternalDataException, DuplicatedRuleException, UnmanagedRuleException;

	public Action composeActions(Collection<GenericRule> rules) throws NoExternalDataException, InvalidActionException{
		return composeActions(rules.toArray(new GenericRule[rules.size()]));
	}
	
	public Action composeActions(Collection<GenericRule> rules, GenericRule r)	throws NoExternalDataException, InvalidActionException {
		GenericRule[] rule_set = rules.toArray(new GenericRule[rules.size()+1]);
		rule_set[rules.size()]=r;
		return composeActions(rule_set);
	}

	public abstract Action composeActions(GenericRule r1, GenericRule r2) throws NoExternalDataException, IncompatibleExternalDataException, InvalidActionException;

	public abstract Action composeActions(GenericRule[] rules) throws NoExternalDataException, InvalidActionException;

	public GenericRule composeRules(Collection<GenericRule> rules) throws NoExternalDataException, InvalidActionException{
		return composeRules((GenericRule[])rules.toArray());
	}

	public GenericRule composeRules(GenericRule r1, GenericRule r2) throws NoExternalDataException, DuplicateExternalDataException, IncompatibleExternalDataException, InvalidActionException {

		ConditionClause cc_clone = r1.getConditionClause().conditionClauseClone();

		cc_clone.intersection(r2.getConditionClause());

		return new GenericRule(this.composeActions(r1, r2), cc_clone, r1.getName() + "-" + r2.getName());
	}

	public GenericRule composeRules(GenericRule[] rules) throws InvalidActionException, NoExternalDataException {
		int length = rules.length;
		ConditionClause cc_clone = rules[0].getConditionClause().conditionClauseClone();

		String name = rules[0].getName();

		for (int i = 1; i < length; i++) {
			cc_clone.intersection(rules[i].getConditionClause());
			name = name + "-" + rules[i].getName();
		}

		return new GenericRule(this.composeActions(rules), cc_clone, name);
	}

	public abstract boolean isActionEquivalent(GenericRule r1, GenericRule r2);

	@Override
	public abstract String toString();
}