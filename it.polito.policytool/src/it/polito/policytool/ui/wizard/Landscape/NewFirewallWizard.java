package it.polito.policytool.ui.wizard.Landscape;

import java.util.HashMap;

import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.rule.selector.impl.IpSelector;

import org.eclipse.jface.wizard.Wizard;

public class NewFirewallWizard extends Wizard {

	FirewallWizardPage page;

	@Override
	public void addPages() {
		page = new FirewallWizardPage("New Firewall", new SecurityControl(new HashMap<String,IpSelector>(),new HashMap<String,IpSelector>(),""));
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		// TODO Aggiungi il firewall al modello
		return false;
	}

}
