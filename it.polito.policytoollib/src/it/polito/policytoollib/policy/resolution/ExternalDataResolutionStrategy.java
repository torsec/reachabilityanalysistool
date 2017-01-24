package it.polito.policytoollib.policy.resolution;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.policy.externaldata.ExternalDataManager;

public abstract class ExternalDataResolutionStrategy<GenericRule, S> extends ResolutionStrategy
{
	protected ExternalDataManager<GenericRule, S> priorities;
	
	public abstract S composeExternalData(GenericRule r1, GenericRule r2) throws NoExternalDataException;
	public abstract S composeExternalData(GenericRule[] rules) throws NoExternalDataException;
	public abstract S getExternalData(GenericRule rule);
	public abstract void clearExternalData(GenericRule rule);
	public abstract boolean isRuleManaged(GenericRule rule);
	public abstract String getExternalDataClassName();
	public abstract boolean isExternalDataValid(String externalData);
	public abstract void setExternalData(GenericRule rule, S externalData) throws DuplicateExternalDataException;
	public abstract void setExternalData(GenericRule rule, String externalData) throws DuplicateExternalDataException, IncompatibleExternalDataException;
	public abstract void setExternalDataNoCheck(GenericRule rule, S externalData);
	public abstract void setExternalDataNoCheck(GenericRule rule, String externalData) throws IncompatibleExternalDataException;
	
	public boolean isExternalDataUnique()
	{
		return priorities.isUnique();
	}
}