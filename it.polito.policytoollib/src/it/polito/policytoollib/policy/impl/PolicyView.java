// TODO: evaluate if this class is still needed

package it.polito.policytoollib.policy.impl;

import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.impl.ConditionClause;

public class PolicyView extends PolicyImpl {

	private ConditionClause	conditionClause;

	public PolicyView(ResolutionStrategy resolutionStrategy, Action defaultAction, ConditionClause conditionClause, PolicyType policyType, String name) throws NoExternalDataException {

		super(resolutionStrategy, defaultAction, policyType, name);
		this.conditionClause = conditionClause;
	}

	public ConditionClause getConditionClause() {
		return this.conditionClause;
	}

}
