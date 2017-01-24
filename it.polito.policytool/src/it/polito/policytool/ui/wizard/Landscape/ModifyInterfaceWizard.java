package it.polito.policytool.ui.wizard.Landscape;

import it.polito.policytoollib.landscape.SecurityControl;

import org.eclipse.jface.wizard.Wizard;

public class ModifyInterfaceWizard extends Wizard {

	InterfaceWizardPage page;
	Interface itf;
	
	public ModifyInterfaceWizard(SecurityControl f, String itf_name) {
		super();
		this.itf = new Interface(f,itf_name);
	}
	
	@Override
	public void addPages() {
		page = new InterfaceWizardPage("Modify Interface",itf);
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
