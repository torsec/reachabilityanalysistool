package it.polito.policytool.ui.view.PolicyEditor;

import it.polito.policytoollib.rule.impl.GenericRule;

import org.eclipse.jface.viewers.ColumnLabelProvider;

public class SelectorColumnLabelProvider extends ColumnLabelProvider {
	
	private String selectore_name;
	
	public SelectorColumnLabelProvider(String selectore_name){
		this.selectore_name = selectore_name;
	}

	@Override
	public String getText(Object element) {
		GenericRule exp = (GenericRule) element;
		if (exp.getConditionClause().get(selectore_name) == null)
			return "*";
		return exp.getConditionClause().get(selectore_name).toSimpleString();
	}


}
