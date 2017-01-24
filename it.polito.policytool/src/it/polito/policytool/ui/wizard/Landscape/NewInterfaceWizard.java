package it.polito.policytool.ui.wizard.Landscape;

import it.polito.policytoollib.landscape.SecurityControl;

import org.eclipse.jface.wizard.Wizard;

public class NewInterfaceWizard extends Wizard {

	InterfaceWizardPage page;
	Interface itf;
	
	public NewInterfaceWizard(SecurityControl f) {
		super();
		this.itf = new Interface(f);
	}
	
	@Override
	public void addPages() {
		page = new InterfaceWizardPage("New Interface",itf);
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
