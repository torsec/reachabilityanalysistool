package it.polito.policytoollib.policy.resolution.impl;

import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.resolution.ResolutionComparison;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.GenericRule;

public class ATPResolutionStrategy extends ResolutionStrategy {

	private static final String label = "Allow Take Precedence (ATP)";

	@Override
	public Action composeActions(GenericRule r1, GenericRule r2) throws InvalidActionException {
		Action a1 = r1.getAction();
		Action a2 = r2.getAction();

		if (a1 == FilteringAction.ALLOW) {
			if (a2 == FilteringAction.DENY || a2 == FilteringAction.ALLOW)
				return FilteringAction.ALLOW;
			else {
				System.err.println("AZIONI:" + a1 + "\n" + a2);
				throw new InvalidActionException();
			}
		} else if (a1 == FilteringAction.DENY) {
			if (a2 == FilteringAction.DENY)
				return FilteringAction.DENY;
			else if (a2 == FilteringAction.ALLOW)
				return FilteringAction.ALLOW;
			else {
				System.err.println("AZIONI:" + a1 + "\n" + a2);
				throw new InvalidActionException();
			}
		} else {
			System.err.println("AZIONI:" + a1 + "\n" + a2);
			throw new InvalidActionException();
		}

	}

	@Override
	public Action composeActions(GenericRule[] rules) throws InvalidActionException {
		boolean allow = false;
		for (GenericRule rule : rules) {
			Action a = rule.getAction();
			if (a == FilteringAction.ALLOW)
				allow = true;
			else if (a != FilteringAction.DENY)
				throw new InvalidActionException();
		}
		if (allow)
			return FilteringAction.ALLOW;
		else
			return FilteringAction.DENY;
	}

	@Override
	public boolean isActionEquivalent(GenericRule r1, GenericRule r2) {
		return r1.getAction() == r2.getAction();
	}

	@Override
	public ResolutionComparison compare(GenericRule r1, GenericRule r2) throws NoExternalDataException {
		if (r1.getAction() == FilteringAction.ALLOW)
			if (r2.getAction() == FilteringAction.DENY)
				return ResolutionComparison.UNIVERSALLY_GREATER;
			else
				return ResolutionComparison.EQUIVALENT;
		else if (r2.getAction() == FilteringAction.ALLOW)
			return ResolutionComparison.UNIVERSALLY_LESS;
		else
			return ResolutionComparison.EQUIVALENT;
	}

	@Override
	public ResolutionStrategy cloneResolutionStrategy() {
		return new ATPResolutionStrategy();
	}

	@Override
	public String toString() {
		return label;
	}

}
