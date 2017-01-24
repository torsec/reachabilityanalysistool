package it.polito.policytool.ui.wizard.Landscape;

import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.rule.impl.GenericRule;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class InterfaceWizardPage extends WizardPage {

	Interface itf;
	private Composite							container;
	
	protected InterfaceWizardPage(String pageName, Interface itf) {
		super(pageName);
		this.itf=itf;
	}

	@Override
	public void createControl(Composite parent) {
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		container = new Composite(sc, SWT.NONE);
		container.setLayout(new GridLayout(5, false));
		container.setSize(400,400);
		sc.setContent(container);
		
		/*Canvas itfNameCanvas = new Canvas(container, SWT.NONE);
		controlsValidation.put(itfNameCanvas,true);
		final GridData canvasData = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		canvasData.heightHint=ruleName.getLineHeight()*2;
		canvasData.widthHint=ruleName.getLineHeight()*2;
		itfNameCanvas.setLayoutData(canvasData);
		ruleNameCanvasPaintListener = new PaintListener(){
            public void paintControl(PaintEvent e) {
                if(!ruleName.getText().isEmpty())
                {
                	e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, itfNameCanvas.getBounds().width, itfNameCanvas.getBounds().height);
                	controlsValidation.put(itfNameCanvas,true);
                	validateInput();
                }
                else
                {
                	e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, itfNameCanvas.getBounds().width, itfNameCanvas.getBounds().height);
                	controlsValidation.put(itfNameCanvas,false);
                	validateInput();
                }
            }
        };
		itfNameCanvas.addPaintListener(ruleNameCanvasPaintListener);
		ruleName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				
				itfNameCanvas.removePaintListener(ruleNameCanvasPaintListener);
				
		        boolean ruleNameIsGood = true;
		        if(ruleName.getText().isEmpty()) ruleNameIsGood=false;
		        else if(!((ruleName.getText().equals(rule.getName()) || policy.getRuleSet().isEmpty())))
					for(Object r : policy.getRuleSet().toArray())
						if(r instanceof GenericRule)
							if(((GenericRule)r).getName().equals(ruleName.getText()))
							{
								ruleNameIsGood = false;
								break;
							}
		        
				final boolean ruleNameIsGood2 = ruleNameIsGood;
				ruleNameCanvasPaintListener = new PaintListener(){
		            public void paintControl(PaintEvent e) {
		                if(ruleNameIsGood2)
		                {
		                	e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, itfNameCanvas.getBounds().width, itfNameCanvas.getBounds().height);
		                	controlsValidation.put(itfNameCanvas,true);
		                	validateInput();
		                }
		                else
		                {
		                	e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, itfNameCanvas.getBounds().width, itfNameCanvas.getBounds().height);
		                	controlsValidation.put(itfNameCanvas,false);
		                	validateInput();
		                }
		            }
		        };
		        itfNameCanvas.addPaintListener(ruleNameCanvasPaintListener);
		        itfNameCanvas.redraw();		        
			}
		});*/
		
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		setControl(container);
		setPageComplete(true);
		//validateInput();
		//ruleName.setFocus();
	}

}
