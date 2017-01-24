package it.polito.policytool.ui.view.LandscapeEditor;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.wizard.Landscape.ModifyFirewallWizard;
import it.polito.policytool.ui.wizard.Landscape.NewFirewallWizard;
import it.polito.policytool.ui.wizard.ModifyRule.ModifyRuleWizard;
import it.polito.policytool.ui.wizard.NewSelectorType.NewSelectorTypeWizard;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.rule.impl.GenericRule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

public class LandscapeEditorView extends ViewPart{
	
	public static final String ID = "it.polito.policytool.ui.view.LandscapeEditor.LandscapeEditorView";
	
	private GraphViewer networkGraph;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Form form;
	Composite composite;
	
	public LandscapeEditorView(){
		PolicyToolUIModel.getInstance().setLandscapeEditorView(this);
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.addControlListener(new ControlAdapter() {
	        @Override
	        public void controlResized(final ControlEvent e) {
	            refresh(true);
	        }
	    });
		
		form = formToolkit.createForm(parent);
		form.getBody().setLayout(new GridLayout(1, false));
		
		composite = new Composite(form.getBody(), SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		FillLayout tableColumnLayout = new FillLayout();
		composite.setLayout(tableColumnLayout);
		
		networkGraph = new GraphViewer(composite, SWT.BORDER);
		networkGraph.setLabelProvider(new NetworkLabelProvider());
		networkGraph.setContentProvider(new NetworkContentProvider());
		
		networkGraph.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				System.out.println("Selection Changed: " + selectionToString((StructuredSelection) event.getSelection()));
			}

			private String selectionToString(StructuredSelection selection) {
				StringBuffer stringBuffer = new StringBuffer();
				Iterator<Object> iterator = selection.iterator();
				boolean first = true;
				while (iterator.hasNext()) {
					if (first) {
						first = false;
					} else {
						stringBuffer.append(" : ");
					}
						stringBuffer.append(iterator.next().toString());
				}
				return stringBuffer.toString();
			}
		});
		
		networkGraph.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object obj = event.getSource();
			    if (obj instanceof GraphViewer) {
			    	GraphViewer viewer = (GraphViewer) obj;
			        ISelection selection = viewer.getSelection();
			        if (selection instanceof IStructuredSelection) {
			            Object item = ((IStructuredSelection) selection).getFirstElement();
			            if(item!=null)
			            {
			            	if(item instanceof SecurityControl)
			            	{
			            		WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(), new ModifyFirewallWizard((SecurityControl) item));
			            		if (wizardDialog.open() == Window.OK) {
									
								} else {
									System.out.println("Cancel pressed");
								}
			            	}
			            }
			        }
			    }
			}
		});
		
		//tbtmNetworkGraph.setControl(networkGraph.getControl());
		
		SpringLayoutAlgorithm networkAlgorithm = new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		networkAlgorithm.setSpringGravitation(5);
		networkAlgorithm.setSpringStrain(5);
		networkGraph.setLayoutAlgorithm(networkAlgorithm, false);
		
		if(PolicyToolUIModel.getInstance().getPolicyToolModel()!=null) refresh(false);
		//tabFolder.setSelection(0);
		
		Composite buttons = new Composite(form.getBody(), SWT.NONE);
		buttons.setLayout(new GridLayout(4, false));
		
		Button newFirewallBtn = new Button(buttons, SWT.NONE);
		newFirewallBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(), new NewFirewallWizard());
				if (wizardDialog.open() == Window.OK) {
					
				} else {
					System.out.println("Cancel pressed");
				}
			}
		});
		formToolkit.adapt(newFirewallBtn, true, true);
		newFirewallBtn.setText("New Firewall");
		newFirewallBtn.setEnabled(true);
		
		Button newHostBtn = new Button(buttons, SWT.NONE);
		newHostBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				System.out.println("fakeBtn Click");
			}
		});
		formToolkit.adapt(newHostBtn, true, true);
		newHostBtn.setText("New Host");
		newHostBtn.setEnabled(true);

		composite.layout();
		form.layout();
	}

	@Override
	public void setFocus() {
		refresh(false);
	}
	
	public void refresh(boolean redraw){
		if(PolicyToolUIModel.getInstance().getPolicyToolModel()==null) return;
		
		Collection<Object> targets = new ArrayList<Object>();
		for(SecurityControl fw:PolicyToolUIModel.getInstance().getLandscape().getFirewallList().values()){
			targets.add(fw);
			for(FilteringZone fz:fw.getFilteringZones()){
				targets.add(fz);
				for(Host h:fz.getHostList()){
					targets.add(h);
				}
			}
		}
		if(networkGraph.getInput()!=null && !redraw) if(networkGraph.getInput().equals(targets)) return;
		
		networkGraph.setInput(targets);
		networkGraph.applyLayout();
	}

}
