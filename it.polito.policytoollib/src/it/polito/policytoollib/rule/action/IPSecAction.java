package it.polito.policytoollib.rule.action;

import it.polito.policytoollib.rule.impl.ConditionClause;

public class IPSecAction extends TransformatonAction{
	
	private String key;
	private String hash_key;
	private IPSecActionType type;
	
	public IPSecAction(String key, String hash_key, IPSecActionType type, ConditionClause tunnel){
		super(tunnel);
		this.key=key;
		this.hash_key=hash_key;
		this.type=type;
	}
	
	public String getKey(){
		return key;
	}
	
	public String getHashKey(){
		return hash_key;
	}
	
	public IPSecActionType getType(){
		return type;
	}
	
	public boolean isEqual(IPSecAction ipSecAction){
		if(this.key.equals(ipSecAction.getKey()) && 
		   this.hash_key.equals(ipSecAction.getHashKey()) &&
		   this.type.equals(ipSecAction.getType()))
			return true;
		return false;
	}
	
	public boolean isInvertEqual(IPSecAction ipSecAction) {
		if((this.type==IPSecActionType.AH && ipSecAction.getType()==IPSecActionType.INVERT_AH) ||
			(this.type==IPSecActionType.INVERT_AH && ipSecAction.getType()==IPSecActionType.AH))
			if(this.hash_key.equals(ipSecAction.getHashKey()))
					return true;
		if((this.type==IPSecActionType.ESP && ipSecAction.getType()==IPSecActionType.INVERT_ESP) ||
			(this.type==IPSecActionType.INVERT_ESP && ipSecAction.getType()==IPSecActionType.ESP))
			if(this.key.equals(ipSecAction.getKey()) && this.hash_key.equals(ipSecAction.getHashKey()))
					return true;
		return false;
	}
	
	public String toString(){
		if(type==IPSecActionType.AH)
			return "AH("+hash_key+")";
		if(type==IPSecActionType.INVERT_AH)
			return "INVERT_AH("+hash_key+")";
		if(type==IPSecActionType.ESP)
			return "ESP("+hash_key+","+key+")";
		if(type==IPSecActionType.INVERT_ESP)
			return "INVERT_ESP("+hash_key+","+key+")";
		return "IPSEC("+hash_key+","+key+")";
	}
	
	public String toStringWithoutKeys(){
		if(type==IPSecActionType.AH)
			return "AH";
		if(type==IPSecActionType.INVERT_AH)
			return "INVERT_AH";
		if(type==IPSecActionType.ESP)
			return "ESP";
		if(type==IPSecActionType.INVERT_ESP)
			return "INVERT_ESP";
		return "";
	}

	@Override
	public Action actionClone() {
		return new IPSecAction(key, hash_key, type, getTransformation().conditionClauseClone());
	}

	@Override
	public ActionData[] getActionData() {
		ActionData[] data;
		if(type == IPSecActionType.ESP || type == IPSecActionType.INVERT_ESP)
		{
			data = new ActionData[2];
			data[0] = new ActionData("Key",key);
			data[1] = new ActionData("HashKey",hash_key);
		}
		else
		{
			data = new ActionData[1];
			data[0] = new ActionData("HashKey",hash_key);
		}
		return data;
	}

	@Override
	public void setActionData(ActionData[] actionData) {
		for(ActionData ad : actionData)
		{
			if(ad.getName().equals("Key")) key=ad.getValue();
			else if(ad.getName().equals("HashKey")) hash_key=ad.getValue();
		}
	}
}
