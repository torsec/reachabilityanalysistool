package it.polito.policytool.ui.wizard.ModifyRule;

import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.impl.StateSelector;

import java.util.BitSet;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ModifyRuleExactMatchWizardPage extends WizardPage{

	private ExactMatchSelector					sel;
	private Composite							container;
	private Button[]							buttons;
	private Combo[]								combos;
	
	public Button[] getButtons() {
		return buttons;
	}
	
	public Combo[] getCombos() {
		return combos;
	}

	protected ModifyRuleExactMatchWizardPage(String pageName, ExactMatchSelector sel) {
		super(pageName);
		this.sel=sel;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		if(sel instanceof StateSelector)
		{
			GridLayout layout = new GridLayout();
			layout.makeColumnsEqualWidth=true;
			layout.numColumns=2;
			container.setLayout(layout);
			String[] setNames = sel.getPossibleValues();
			combos = new Combo[setNames.length];
			for(int i=0;i<setNames.length;i++)
			{	
				(new Label(container, SWT.NONE)).setText(setNames[i]);
				combos[i] = new Combo(container, SWT.READ_ONLY);
				combos[i].setData("state", setNames[i]);
				combos[i].add("Don't care");
				combos[i].add("Is not set");
				combos[i].add("Is set");
				combos[i].select(((StateSelector) sel).getFlag(setNames[i])+1);
			}
		}
		else
		{
			GridLayout layout = new GridLayout();
			layout.makeColumnsEqualWidth=true;
			layout.numColumns=5;
			container.setLayout(layout);
			BitSet set = sel.getPointSet();
			String[] setNames = sel.getPossibleValues();
			buttons = new Button[setNames.length];
			for(int i=0;i<setNames.length;i++)
			{	
				buttons[i] = new Button(container, SWT.CHECK);
				buttons[i].setText(setNames[i]);
				buttons[i].setSelection(set.get(i));
			}
		}
	    setControl(container);
		setPageComplete(true);
	}
	
}