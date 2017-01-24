package it.polito.policytool.ui.wizard.ExecuteDistributedAnalysis;

import java.util.HashMap;
import java.util.logging.Logger;

import it.polito.policytool.PolicyToolUIModel;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ExecuteDistributedAnalysisWizardPage extends WizardPage {

	private final Logger LOGGER	= Logger.getLogger(ExecuteDistributedAnalysisWizardPage.class.getName());
	
	private Composite	container;
	
	private HashMap<String, Text>				SelectorSet;
	Combo fz1Combo;
	Combo fz2Combo;

	protected ExecuteDistributedAnalysisWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		this.SelectorSet = new HashMap<String, Text>();
//		setDescription("Fake Wizard. First page");
	}

	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		
		
		Label fz1Label = new Label(container,SWT.NONE);
		fz1Combo = new Combo(container,SWT.READ_ONLY | SWT.DROP_DOWN);
		fz1Combo.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(fz1Combo.getSelectionIndex()!=-1 && fz2Combo.getSelectionIndex()!=-1) setPageComplete(true);
			}
		});
		
		Label fz2Label = new Label(container,SWT.NONE);
		fz2Combo = new Combo(container,SWT.READ_ONLY | SWT.DROP_DOWN);
		fz2Combo.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(fz1Combo.getSelectionIndex()!=-1 && fz2Combo.getSelectionIndex()!=-1) setPageComplete(true);
			}
		});
		for(String fz : PolicyToolUIModel.getInstance().getPolicyToolModel().getLandscape().getZoneList().keySet())
		{
			fz1Combo.add(fz);
			fz2Combo.add(fz);
		}

		setControl(container);
		setPageComplete(false);
	}
	
	public String[] getSelectedZones()
	{
		if(fz1Combo==null || fz2Combo==null) return null;
		String[] result = new String[2];
		result[0]=fz1Combo.getText();
		result[1]=fz2Combo.getText();
		return result;
	}
	
}
