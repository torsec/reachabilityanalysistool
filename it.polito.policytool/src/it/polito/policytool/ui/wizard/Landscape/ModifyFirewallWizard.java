package it.polito.policytool.ui.wizard.Landscape;

import it.polito.policytoollib.landscape.SecurityControl;

import org.eclipse.jface.wizard.Wizard;

public class ModifyFirewallWizard extends Wizard {

	FirewallWizardPage page;
	SecurityControl f;
	
	public ModifyFirewallWizard(SecurityControl f) {
		super();
		this.f = f;
	}

	@Override
	public void addPages() {
		page = new FirewallWizardPage("New Firewall", f);
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		// TODO Aggiungi il firewall al modello
		return false;
	}

}
