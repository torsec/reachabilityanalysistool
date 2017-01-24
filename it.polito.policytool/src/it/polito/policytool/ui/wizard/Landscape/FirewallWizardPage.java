package it.polito.policytool.ui.wizard.Landscape;

import java.util.List;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.policy.impl.Policy;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FirewallWizardPage extends WizardPage {

	SecurityControl f;
	Combo policyCombo;
	Combo vpnCombo;
	Combo natCombo;
	Combo routingCombo;
	
	protected FirewallWizardPage(String pageName, SecurityControl f) {
		super(pageName);
		this.f = f;
	}

	@Override
	public void createControl(Composite parent) {
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		Composite container = new Composite(sc, SWT.NONE);
		container.setLayout(new GridLayout(5, false));
		container.setSize(400,400);
		sc.setContent(container);
		
		Label firewallNameLabel = new Label(container, SWT.NONE);
		firewallNameLabel.setText("Name : ");

		Text firewallName = new Text(container, SWT.BORDER | SWT.SINGLE);
		firewallName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		firewallName.setText(f.getName());
		
		Label policyLabel = new Label(container, SWT.NONE);
		policyLabel.setText("Filtering Policy : ");
		
		Button policyComboEnabler = new Button(container, SWT.CHECK);
		policyComboEnabler.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				policyCombo.setEnabled(!policyCombo.getEnabled());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		policyCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		policyCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		int policyIndex = -1;
		for(Policy p : PolicyToolUIModel.getInstance().getPolicyList())
		{
			policyCombo.add(p.getName());
			if(f.getPolicy()!=null && f.getPolicy().equals(p)) policyIndex=policyCombo.getItemCount()-1;
		}
		if(f.getPolicy()==null)
			policyCombo.setEnabled(false);
		else
		{
			policyCombo.setEnabled(true);
			policyComboEnabler.setSelection(true);
			policyCombo.select(policyIndex);
		}
		
		Label natLabel = new Label(container, SWT.NONE);
		natLabel.setText("NAT Policy : ");
		
		Button natComboEnabler = new Button(container, SWT.CHECK);
		natComboEnabler.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				natCombo.setEnabled(!natCombo.getEnabled());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		natCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		natCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		int natIndex = -1;
		for(Policy p : PolicyToolUIModel.getInstance().getNATPolicyList())
		{	
			natCombo.add(p.getName());
			if(f.getNAT()!=null && f.getNAT().equals(p)) natIndex=natCombo.getItemCount()-1;
		}
		if(f.getNAT()==null)
			natCombo.setEnabled(false);
		else
		{
			natCombo.setEnabled(true);
			natComboEnabler.setSelection(true);
			natCombo.select(natIndex);
		}
		
		Label vpnLabel = new Label(container, SWT.NONE);
		vpnLabel.setText("VPN Policy : ");
		
		Button vpnComboEnabler = new Button(container, SWT.CHECK);
		vpnComboEnabler.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				vpnCombo.setEnabled(!vpnCombo.getEnabled());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		vpnCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		vpnCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		int vpnIndex = -1;
		for(Policy p : PolicyToolUIModel.getInstance().getVPNPolicyList())
		{
			vpnCombo.add(p.getName());
			if(f.getVPN()!=null && f.getVPN().equals(p)) vpnIndex=vpnCombo.getItemCount()-1;
		}
		if(f.getVPN()==null)
			vpnCombo.setEnabled(false);
		else
		{
			vpnCombo.setEnabled(true);
			vpnComboEnabler.setSelection(true);
			vpnCombo.select(vpnIndex);
		}
		
		Label routingLabel = new Label(container, SWT.NONE);
		routingLabel.setText("Routing Policy : ");
		
		Button routingComboEnabler = new Button(container, SWT.CHECK);
		routingComboEnabler.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				routingCombo.setEnabled(!routingCombo.getEnabled());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		int routingIndex = -1;
		routingCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		routingCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		for(Policy p : PolicyToolUIModel.getInstance().getRoutingPolicyList())
		{
			routingCombo.add(p.getName());
			if(f.getRouting()!=null && f.getRouting().equals(p)) routingIndex=routingCombo.getItemCount()-1;
		}
		if(f.getRouting()==null)
			routingCombo.setEnabled(false);
		else
		{
			routingCombo.setEnabled(true);
			routingComboEnabler.setSelection(true);
			routingCombo.select(routingIndex);
		}
		
		for(String itf : f.getInterfaces())
		{
			(new Label(container, SWT.NONE)).setText("Interface "+itf);
			(new Label(container, SWT.NONE)).setText("IP : ");
			Text itfIP = new Text(container, SWT.SINGLE | SWT.BORDER);
			itfIP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			itfIP.setText(f.getInterfacesIp().get(itf).toSingleIpString());
			itfIP.setEnabled(false);
			final Button editButton = new Button(container, SWT.PUSH);
			editButton.setText("Edit");
			editButton.setData("itf", itf);
			editButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO 
					System.out.println(editButton.getData("itf")+" edited");
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			final Button removeButton = new Button(container, SWT.PUSH);
			removeButton.setText("Remove");
			removeButton.setData("itf", itf);
			removeButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO 
					System.out.println(removeButton.getData("itf")+" removed");
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		Button addItfButton = new Button(container, SWT.PUSH);
		addItfButton.setText("Add new interface");
		addItfButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 5, 1));
		addItfButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*if(f.getInterfaces().isEmpty())
		{
			(new Label(container, SWT.NONE)).setText("Interface name: ");
			Text itfName = new Text(container, SWT.BORDER | SWT.SINGLE);
			itfName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
			
			(new Label(container, SWT.NONE)).setText("Interface IP: ");
			Text itfIp = new Text(container, SWT.BORDER | SWT.SINGLE);
			itfIp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
			
			(new Label(container, SWT.NONE)).setText("Interface to Firewall: ");
			
			itfToFw = new Button(container, SWT.RADIO);
			itfToFw.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					itfToFz.setSelection(false);
					itfToFw.setSelection(true);
					connFzCombo.setEnabled(false);
					selectFw.setEnabled(true);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			Text connectedFwNames = new Text(container, SWT.BORDER | SWT.SINGLE);
			connectedFwNames.setEnabled(false);
			connectedFwNames.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			//itfToFw.setText(f.getFirewallsByInterface(itf));
			selectFw = new Button(container, SWT.PUSH);
			selectFw.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					//TODO creare finestra a parte per selezionare i fw
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			selectFw.setText("Select Firewalls");
			selectFw.setEnabled(false);
			(new Label(container, SWT.NONE)).setText("Interface to Filtering Zone: ");
			itfToFz = new Button(container, SWT.RADIO);
			itfToFz.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					itfToFz.setSelection(true);
					itfToFw.setSelection(false);
					connFzCombo.setEnabled(true);
					selectFw.setEnabled(false);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			Text fzName = new Text(container, SWT.SINGLE | SWT.BORDER);
			Text fzSubnet = new Text(container, SWT.SINGLE | SWT.BORDER);
			Button fzHosts = new Button(container, SWT.PUSH);
			fzHosts.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		else for(String itf : f.getInterfaces())
		{
			(new Label(container, SWT.NONE)).setText("Interface name: ");
			Text itfName = new Text(container, SWT.BORDER | SWT.SINGLE);
			itfName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
			itfName.setText(itf);
			
			(new Label(container, SWT.NONE)).setText("Interface IP: ");
			Text itfIp = new Text(container, SWT.BORDER | SWT.SINGLE);
			itfIp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
			itfIp.setText(f.getInterfacesIp().get(itf).toSingleIpString());
			
			List<Firewall> connFw = f.getFirewallsByInterface(itf);
			(new Label(container, SWT.NONE)).setText("Interface to Firewall: ");
			itfToFw = new Button(container, SWT.RADIO);
			itfToFw.setSelection(connFw!=null);
			itfToFw.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					itfToFz.setSelection(false);
					itfToFw.setSelection(true);
					connFzCombo.setEnabled(false);
					selectFw.setEnabled(false);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			Text connectedFwNames = new Text(container, SWT.BORDER | SWT.SINGLE);
			connectedFwNames.setEnabled(false);
			connectedFwNames.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			//itfToFw.setText(f.getFirewallsByInterface(itf));
			
			selectFw = new Button(container, SWT.PUSH);
			selectFw.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					//TODO creare finestra a parte per selezionare i fw
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			selectFw.setText("Select Firewalls");
			
			FilteringZone connFz = f.getFilteringZonesWithInterfaces().get(itf);
			(new Label(container, SWT.NONE)).setText("Interface to Filtering Zone: ");
			itfToFz = new Button(container, SWT.RADIO);
			itfToFz.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					itfToFz.setSelection(true);
					itfToFw.setSelection(false);
					connFzCombo.setEnabled(true);
					selectFw.setEnabled(false);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			connFzCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
			connFzCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
			for(String fz : PolicyToolUIModel.getInstance().getLandscape().getZoneList().keySet())
				connFzCombo.add(fz);
			if(connFz==null)
				connFzCombo.setEnabled(false);
			else
			{
				connFzCombo.setEnabled(true);
				itfToFz.setEnabled(true);
			}
		}*/
		
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		setControl(container);
		setPageComplete(true);
		//validateInput();
		firewallName.setFocus();
		sc.layout();
	}

}
