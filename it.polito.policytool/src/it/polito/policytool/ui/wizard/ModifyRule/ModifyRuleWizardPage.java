package it.polito.policytool.ui.wizard.ModifyRule;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.util.Figure;
import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.ExternalDataResolutionStrategy;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.ActionData;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.action.IPSecAction;
import it.polito.policytoollib.rule.action.IPSecActionType;
import it.polito.policytoollib.rule.action.NATAction;
import it.polito.policytoollib.rule.action.NATActionType;
import it.polito.policytoollib.rule.action.TransformatonAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.RegExpSelector;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.TotalOrderedSelector;
import it.polito.policytoollib.rule.selector.impl.DomainSelector;
import it.polito.policytoollib.rule.selector.impl.ExactMatchSelectorImpl;
import it.polito.policytoollib.rule.selector.impl.IntMatcherSelector;
import it.polito.policytoollib.rule.selector.impl.InterfaceSelector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.StringMatcherSelector;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Logger;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class ModifyRuleWizardPage extends WizardPage {

	private final Logger						LOGGER	= Logger.getLogger(ModifyRuleWizardPage.class.getName());

	private Boolean								externalData;
	private PolicyType							policyType;
	private Policy								policy;
	private GenericRule							rule;
	
	private Composite							container;
	private Label								ruleNameLabel;
	private Text								ruleName;
	private Canvas								ruleNameCanvas;
	private PaintListener 						ruleNameCanvasPaintListener;
	private Canvas								priorityCanvas;
	private Label								priorityLabel;
	private Text								priority;
	private Label								actionLabel;
	private Combo								action;
	private Vector<Control>						actionDataControls;
	private ConditionClause 					transformation;
	private Button								trans_button;
	private Label								tunnelS_label;
	private Text								tunnelS;
	private String								tunnelS_text;
	private PaintListener 						tunnelSourcePaintListener;
	private Label								tunnelD_label;
	private Text								tunnelD;
	private String								tunnelD_text;
	private PaintListener 						tunnelDestPaintListener;
	private Vector<Canvas>						selectorsCanvas;
	
	private LinkedHashMap<Control,Boolean>		controlsValidation;
	
	Image okImage;
	Image wrongImage;
	
	private Control[]							tunnel;

	private ResolutionStrategy[]	RSSet;
	private Action[]							ActionSet;

	private HashMap<String, LinkedList<Text[]>> SelectorSet;
	private HashMap<String, Text>				actionDataTextMap;
	
	private LinkedHashMap<String, Selector> selectorMap;


	protected ModifyRuleWizardPage(String pageName, boolean externalData, Policy policy, GenericRule rule) {
		super(pageName);
		this.externalData = externalData;
		this.policy = policy;
		this.rule = rule;
		this.policyType = policy.getPolicyType();
		this.SelectorSet = new HashMap<String, LinkedList<Text[]>>();
		this.actionDataTextMap = new HashMap<String, Text>();
		setTitle(pageName);
		selectorMap = new LinkedHashMap<>();
		setDescription("Modify Rule "+rule.getName());
		actionDataControls = new Vector<Control>();
		okImage = Figure.OK.load();
		wrongImage = Figure.ERROR.load();
		selectorsCanvas = new Vector<Canvas>();
		controlsValidation = new LinkedHashMap<Control, Boolean>();			
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createControl(Composite parent) {

		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		container = new Composite(sc, SWT.NONE);
		container.setLayout(new GridLayout(5, false));
		container.setSize(400,400);
		sc.setContent(container);
		
		
		ruleNameLabel = new Label(container, SWT.NONE);
		ruleNameLabel.setText("Name : ");

		ruleName = new Text(container, SWT.BORDER | SWT.SINGLE);
		ruleName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		ruleName.setText(rule.getName());
		//final Color defaultTextBackgroundColor = ruleName.getBackground();
		//final Color defaultTextForegroundColor = ruleName.getForeground();
		/*ruleName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
					setPageComplete(true);
				} else {
					setPageComplete(false);
				}
			}
		});*/
		
		ruleNameCanvas = new Canvas(container, SWT.NONE);
		ruleNameCanvas.moveAbove(ruleNameLabel);
		controlsValidation.put(ruleNameCanvas,true);
		final GridData canvasData = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		canvasData.heightHint=ruleName.getLineHeight()*2;
		canvasData.widthHint=ruleName.getLineHeight()*2;
		ruleNameCanvas.setLayoutData(canvasData);
		ruleNameCanvasPaintListener = new PaintListener(){
            public void paintControl(PaintEvent e) {
                if(!ruleName.getText().isEmpty())
                {
                	e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
                	controlsValidation.put(ruleNameCanvas,true);
                	validateInput();
                }
                else
                {
                	e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
                	controlsValidation.put(ruleNameCanvas,false);
                	validateInput();
                }
            }
        };
		ruleNameCanvas.addPaintListener(ruleNameCanvasPaintListener);
		
		ruleName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				
				ruleNameCanvas.removePaintListener(ruleNameCanvasPaintListener);
				
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
		                	e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
		                	controlsValidation.put(ruleNameCanvas,true);
		                	validateInput();
		                }
		                else
		                {
		                	e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
		                	controlsValidation.put(ruleNameCanvas,false);
		                	validateInput();
		                }
		            }
		        };
		        ruleNameCanvas.addPaintListener(ruleNameCanvasPaintListener);
		        ruleNameCanvas.redraw();		        
			}
		});
        
		new Label(container, SWT.NONE);
		actionLabel = new Label(container, SWT.NONE);
		actionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		actionLabel.setText("Action : ");

		action = new Combo(container, SWT.READ_ONLY);
		
		action.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if(trans_button!=null && !trans_button.isDisposed()) trans_button.dispose();
				action.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
				Action newAction = ActionSet[action.getSelectionIndex()];
				if(rule.getAction()==null) rule.setAction(ActionSet[action.getSelectionIndex()].actionClone());
				if(rule.getAction() instanceof TransformatonAction)
				{
					if(((TransformatonAction)(rule.getAction())).getTransformation()==null)
					{
						transformation = new ConditionClause();
						((TransformatonAction)(rule.getAction())).setTransformation(transformation);
					}
					else
					{
						transformation = ((TransformatonAction)(rule.getAction())).getTransformation();
					}
				}	
				newAction.setActionData(rule.getAction().getActionData());
				Control last = action;
				HashMap<String, Text> actionDataTextMap2 = new HashMap<String, Text>();
				Vector<Control> actionDataControls2 = new Vector<Control>();
				if(newAction.getActionData()!=null)
					for(ActionData ad : newAction.getActionData())
					{
						Label actionDataFakeLabel = new Label(container, SWT.NONE);
						actionDataFakeLabel.moveBelow(last);
						actionDataControls2.add(actionDataFakeLabel);
						Label actionDataLabel = new Label(container, SWT.NONE);
						actionDataLabel.setText(ad.getName()+" :");
						actionDataLabel.moveBelow(actionDataFakeLabel);
						actionDataControls2.add(actionDataLabel);
						
						Text actionDataText = new Text(container, SWT.BORDER | SWT.SINGLE);
						actionDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
						if(actionDataTextMap.get(ad.getName())!=null)
							actionDataText.setText(actionDataTextMap.get(ad.getName()).getText());
						else
							actionDataText.setText(ad.getValue());
						actionDataText.moveBelow(actionDataLabel);
						actionDataControls2.add(actionDataText);
						
						last = actionDataText;
						container.layout();
						/*actionDataText.addKeyListener(new KeyListener() {
	
							@Override
							public void keyPressed(KeyEvent e) {
							}
	
							@Override
							public void keyReleased(KeyEvent e) {
								if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
									setPageComplete(true);
								} else {
									setPageComplete(false);
								}
							}
						});*/
						actionDataTextMap2.put(ad.getName(), actionDataText);
					}
				actionDataTextMap = actionDataTextMap2;
				
				for(Control control :  actionDataControls)
					control.dispose();
				actionDataControls.clear();
				actionDataControls = actionDataControls2;
				
				/*if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
					setPageComplete(true);
				} else {
					setPageComplete(false);
				}*/
				
				if(tunnelS!=null)
					if(!tunnelS.isDisposed())
						tunnelS_text=tunnelS.getText();
				
				if(tunnelD!=null)
					if(!tunnelD.isDisposed())
						tunnelD_text=tunnelD.getText();
				
				if(tunnel!=null)
					for(Control c : tunnel)
						if(c!=null)
							if(!c.isDisposed())
								c.dispose();
				
				if (policyType == PolicyType.VPN && (action.getSelectionIndex() == 0 || action.getSelectionIndex() == 1))
				{
					tunnel = new Control[6];
					
					GridData data =  new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
					data.horizontalAlignment = SWT.FILL;
					data.grabExcessHorizontalSpace = true;
					
					GridData canvasData = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
					canvasData.heightHint=ruleName.getLineHeight()*2;
					canvasData.widthHint=ruleName.getLineHeight()*2;
				
					
					tunnel[0] = new Canvas(container, SWT.NONE);
					tunnel[0].setLayoutData(canvasData);
					controlsValidation.put(tunnel[0],true);
                	tunnelSourcePaintListener = new PaintListener(){
			            public void paintControl(PaintEvent e) {
			                if(tunnel[2]!=null)
			                {
				                if(((Text)tunnel[2]).getText().isEmpty() && ((Text)tunnel[5]).getText().isEmpty())
			                	{
				                	//e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
				                	controlsValidation.put(tunnel[0],true);
				                	validateInput();
				                }
				                else if(((Text)tunnel[2]).getText().isEmpty())
				                {
				                	e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
				                	controlsValidation.put(tunnel[0],false);
				                	validateInput();
				                }
				                else
				                {
				                	IpSelector fakeSel2 = new IpSelector();
				                	try {
										fakeSel2.addRange(((Text)tunnel[2]).getText());
										e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
										controlsValidation.put(tunnel[0],true);
					                	validateInput();
				                	} catch (InvalidIpAddressException
											| InvalidRangeException e1) {
				                		controlsValidation.put(tunnel[0],false);
					                	validateInput();
				                		e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
									}
				                }
			                }
			            }
			        };
			        tunnel[0].addPaintListener(tunnelSourcePaintListener);
			        tunnel[0].moveBelow(last);
					
					tunnelS_label = new Label(container, SWT.NONE);
					tunnelS_label.setText("Tunnel Source Address :");
					tunnelS_label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
					tunnelS_label.moveBelow(tunnel[0]);
					tunnel[1]=tunnelS_label;
					
					tunnelS = new Text(container, SWT.BORDER);
					tunnelS.setLayoutData(data);
					if(tunnelS_text!=null) tunnelS.setText(tunnelS_text);
					tunnelS.moveBelow(tunnelS_label);
					tunnel[2]=tunnelS;
					((Text)tunnel[2]).addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							tunnel[0].redraw();    
							tunnel[3].redraw();
						}
					});
					
					tunnel[3] = new Canvas(container, SWT.NONE);
					tunnel[3].setLayoutData(canvasData);
					controlsValidation.put(tunnel[3],true);
                	
					tunnelDestPaintListener = new PaintListener(){
						public void paintControl(PaintEvent e) {
							if(tunnel[5]!=null)
							{
							    if(((Text)tunnel[2]).getText().isEmpty() && ((Text)tunnel[5]).getText().isEmpty())
								{
							    	//e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
							    	controlsValidation.put(tunnel[3],true);
				                	validateInput();
							    }
							    else if(((Text)tunnel[5]).getText().isEmpty())
							    {
							    	e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
							    	controlsValidation.put(tunnel[3],false);
				                	validateInput();
							    }
							    else
							    {
							    	IpSelector fakeSel3 = new IpSelector();
							    	try {
										fakeSel3.addRange(((Text)tunnel[5]).getText());
										e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
										controlsValidation.put(tunnel[3],true);
					                	validateInput();
							    	} catch (InvalidIpAddressException
											| InvalidRangeException e1) {
							    		controlsValidation.put(tunnel[3],false);
					                	validateInput();
							    		e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
									}
							    }
							}
						}
			        };
			        tunnel[3].addPaintListener(tunnelDestPaintListener);
					tunnel[3].moveBelow(tunnelS);
					
					tunnelD_label = new Label(container, SWT.NONE);
					tunnelD_label.setText("Tunnel Destination Address :");
					tunnelD_label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
					tunnelD_label.moveBelow(tunnel[3]);
					tunnel[4]=tunnelD_label;
					
					tunnelD = new Text(container, SWT.BORDER);
					tunnelD.setLayoutData(data);
					if(tunnelD_text!=null) tunnelD.setText(tunnelD_text);
					tunnelD.moveBelow(tunnelD_label);
					
					tunnel[5]=tunnelD;
					last=tunnelD;
					((Text)tunnel[5]).addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							tunnel[3].redraw();
							tunnel[0].redraw();
						}
					});
					
					ConditionClause tunnel = ((IPSecAction)rule.getAction()).getTransformation();
					if(tunnel!=null)
					{
						IpSelector tunnelSourceIP, tunnelDestinationIP;
						if((tunnelSourceIP = (IpSelector) tunnel.get("Source Address")) !=null) 
							tunnelS.setText(tunnelSourceIP.toSingleIpString()); 
						if((tunnelDestinationIP = (IpSelector) tunnel.get("Destination Address")) !=null) 
							tunnelD.setText(tunnelDestinationIP.toSingleIpString());
					}
					
					/*tunnelS.addKeyListener(new KeyListener() {

						@Override
						public void keyPressed(KeyEvent e) {
						}

						@Override
						public void keyReleased(KeyEvent e) {
							if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
								setPageComplete(true);
							} else {
								setPageComplete(false);
							}
						}
					});
					
					tunnelD.addKeyListener(new KeyListener() {

						@Override
						public void keyPressed(KeyEvent e) {
						}

						@Override
						public void keyReleased(KeyEvent e) {
							if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
								setPageComplete(true);
							} else {
								setPageComplete(false);
							}
						}
					});*/
				}
				
				if (policyType == PolicyType.NAT)
				{
					action.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
					if(trans_button == null || trans_button.isDisposed()) trans_button = new Button(container, SWT.PUSH | SWT.BORDER | SWT.SINGLE);
					trans_button.moveBelow(action);
					trans_button.setText("Translation");
					trans_button.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							ConditionClause oldTrans = transformation.conditionClauseClone();
							ModifyRuleTranslationWizard wiz = new ModifyRuleTranslationWizard(transformation, policy);
							WizardDialog wizardDialog = new WizardDialog
									(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(), wiz);
							if (wizardDialog.open() == Window.OK) {
								((NATAction)rule.getAction()).setTransformation(transformation);
							} else {
								((NATAction)rule.getAction()).setTransformation(oldTrans);
								System.out.println("Cancel pressed");
							}
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				
				container.layout();
			}
		});
		
		if (policyType == PolicyType.FILTERING) {
			ActionSet = new Action[2];
			action.add("ALLOW");
			ActionSet[0] = FilteringAction.ALLOW;
			action.add("DENY");
			ActionSet[1] = FilteringAction.DENY;
			int i = 1;
			if(rule.getAction()==FilteringAction.ALLOW) i = 0;
			action.select(i);
		}
		
		if (policyType == PolicyType.NAT) {
			ActionSet = new Action[2];
			action.add("PRE");
			ActionSet[0] = new NATAction(NATActionType.PRENAT, transformation);
			action.add("POST");
			ActionSet[1] = new NATAction(NATActionType.POSTNAT, transformation);
			if(rule.getAction()!=null)
			{
				int i = 1;
				if(((NATAction)rule.getAction()).getNATAction()==NATActionType.PRENAT) i = 0;
				action.select(i);
			}
			else
			{
				rule.setAction(new NATAction(NATActionType.PRENAT, transformation));
				action.select(0);
			}
		}
		
		if (policyType == PolicyType.VPN) {
			ActionSet = new Action[4];
			action.add("AH");
			ActionSet[0] = new IPSecAction(null, "", IPSecActionType.AH, null);
			action.add("ESP");
			ActionSet[1] = new IPSecAction("", "", IPSecActionType.ESP, transformation);
			action.add("INVERT_AH");
			ActionSet[2] = new IPSecAction(null, "", IPSecActionType.INVERT_AH, null);
			action.add("INVERT_ESP");
			ActionSet[3] = new IPSecAction("", "", IPSecActionType.INVERT_ESP, null);
			if((IPSecAction)rule.getAction()!=null)
			{
				switch(((IPSecAction)rule.getAction()).getType())
				{
					case AH: action.select(0); break;
					case ESP: action.select(1); break;
					case INVERT_AH: action.select(2); break;
					case INVERT_ESP: action.select(3); break;
				}
			}
			else
			{
				rule.setAction(new IPSecAction(null, "", IPSecActionType.AH, null));
				action.select(0);
			}
		} 
		
		if (externalData) {
			priorityCanvas = new Canvas(container, SWT.NONE);
			priorityCanvas.setLayoutData(canvasData);
			priorityCanvas.addPaintListener(new PaintListener(){
	            public void paintControl(PaintEvent e) {
	                if(priority!=null)
	                {
	                	if(priority.getText().isEmpty() || !((ExternalDataResolutionStrategy<GenericRule, ?>)(policy.getResolutionStrategy())).isExternalDataValid(priority.getText()))
	                	{
	                		e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
	                		controlsValidation.put(priority,false);
		                	validateInput();
		                	return;
	                	}
	                	else if(policy.containsRule(rule))
	                	{
	                		if((((ExternalDataResolutionStrategy<GenericRule, ?>)(policy.getResolutionStrategy())).getExternalData(rule).toString().equals(priority.getText())))
	                		{
	                			e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
	                			controlsValidation.put(priority,true);
			                	validateInput();
			                	return;
	                		}
	                	}
	                	for(GenericRule r : policy.getRuleSet())
	                	{
	                		if(((ExternalDataResolutionStrategy<GenericRule, ?>)policy.getResolutionStrategy()).getExternalData(r).toString().equals(priority.getText()))
	                		{
	                			controlsValidation.put(priority,false);
			                	validateInput();
			                	e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
			                	return;
	                		}
	                	}
	                	e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
            			controlsValidation.put(priority,true);
	                	validateInput();
	                }
	            }
	        });
			priorityLabel = new Label(container, SWT.NONE);
			priorityLabel.setText("Priority : ");
			priority = new Text(container, SWT.BORDER | SWT.SINGLE);
			priority.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false, 3, 1));
			if(((ExternalDataResolutionStrategy<GenericRule, ?>)(policy.getResolutionStrategy())).getExternalData(rule)!=null)
				priority.setText(((ExternalDataResolutionStrategy<GenericRule, ?>)(policy.getResolutionStrategy())).getExternalData(rule).toString());
			else
				priority.setText("");
			priority.addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					priorityCanvas.redraw();
				}
			});
			
			/*priority.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
						setPageComplete(true);
					} else {
						setPageComplete(false);
					}
				}
			});*/
		}

		
		for (final Selector sel : PolicyToolUIModel.getInstance().getSelectorTypes().getAllSelectorTypes().values()) {
			String selectorName = sel.getLabel();
			final Label selectorLabel = new Label(container, SWT.NONE);
			selectorLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			selectorLabel.setText(selectorName+" :");
			if(sel instanceof TotalOrderedSelector || sel instanceof IntMatcherSelector)
			{
				final LinkedList<Text[]> rangesText = new LinkedList<Text[]>();
				if(rule.getConditionClause()!=null && rule.getConditionClause().get(selectorName)!=null)
				{
					String text = "";
					if(sel instanceof TotalOrderedSelector)
						text = rule.getConditionClause().get(selectorName).toString();
					else if (sel instanceof IntMatcherSelector)
						text = ((IntMatcherSelector)(rule.getConditionClause().get(selectorName))).toRangeString();
					text = text.trim();
					text = text.substring(1, text.length()-1);
					text = text.replaceAll("\\s","");
					text = text.replaceAll("\\[","");
					text = text.replaceAll("\\]"," ");
					String[] sel_texts = text.split(" ");
					Label lastLabel = null;
					for(String selText: sel_texts)
					{
						final Canvas selectorCanvas = new Canvas(container, SWT.NONE);
						selectorsCanvas.add(selectorCanvas);
						selectorCanvas.setLayoutData(canvasData);
						
						if(selText!=sel_texts[0]){ new Label(container, SWT.NONE); }
						else{ selectorCanvas.moveAbove(selectorLabel);}
						
						String[] ranges = null;
						if(sel instanceof TotalOrderedSelector)
							ranges = selText.split("-");
						else if (sel instanceof IntMatcherSelector)
							ranges = selText.split(",");
						final Text[] rangeText = new Text[2];
						rangesText.add(rangeText);
						GridData data =  new GridData();
						data.horizontalAlignment = SWT.FILL;
						data.grabExcessHorizontalSpace = true;
						rangeText[0] = new Text(container, SWT.BORDER);
						rangeText[0].setLayoutData(data);
						rangeText[1] = new Text(container, SWT.BORDER);
						rangeText[1].setLayoutData(data);
						
						rangeText[0].setText(ranges[0]);
						rangeText[1].setText(ranges[1]);
						controlsValidation.put(selectorCanvas,true);
	                	validateInput();
	                	selectorCanvas.addPaintListener(new PaintListener(){
				            public void paintControl(PaintEvent e) {
				            	if(rangeText[0].getText().isEmpty() && rangeText[1].getText().isEmpty())
				            	{
				            		controlsValidation.put(selectorCanvas,true);
				                	validateInput();
				                	return;
				            	}
				            	else
				            	{
				            		if(sel instanceof TotalOrderedSelector)
				            		{
				            			TotalOrderedSelector fakeSel = (TotalOrderedSelector) sel.selectorClone();
										try {
											fakeSel.addRange(rangeText[0].getText(), rangeText[0].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
						                	return;
										}
										try {
											fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
						                	return;
											
										}
										try {
											fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
											return;
										}
				            		}
					            	else if(sel instanceof IntMatcherSelector)
					            	{
					            		IntMatcherSelector fakeSel = (IntMatcherSelector) sel.selectorClone();
					            		try {
											fakeSel.addRange(rangeText[0].getText(), rangeText[0].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
						                	return;
										}
										try {
											fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
						                	return;
											
										}
										try {
											fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
											return;
										}
					            	}
				            	}
								e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
								controlsValidation.put(selectorCanvas,true);
			                	validateInput();
				            }
				        });
						
						/*rangeText[0].addKeyListener(new KeyListener() {

							@Override
							public void keyPressed(KeyEvent e) {
							}

							@Override
							public void keyReleased(KeyEvent e) {
								if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
									setPageComplete(true);
								} else {
									setPageComplete(false);
								}
							}
						});

						rangeText[1].addKeyListener(new KeyListener() {

							@Override
							public void keyPressed(KeyEvent e) {
							}

							@Override
							public void keyReleased(KeyEvent e) {
								if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
									setPageComplete(true);
								} else {
									setPageComplete(false);
								}
							}
						});*/
						
						lastLabel = new Label(container, SWT.NONE);

						
						rangeText[0].addModifyListener(new ModifyListener() {
							
							@Override
							public void modifyText(ModifyEvent e) {
								selectorCanvas.redraw();
							}
						});
						
						rangeText[1].addModifyListener(new ModifyListener() {
							
							@Override
							public void modifyText(ModifyEvent e) {
								selectorCanvas.redraw();
							}
						});
					}
					lastLabel.dispose();
				}
				else
				{
					final Canvas selectorCanvas = new Canvas(container, SWT.NONE);
					selectorsCanvas.add(selectorCanvas);
					selectorCanvas.setLayoutData(canvasData);
					selectorCanvas.moveAbove(selectorLabel);
					
					final Text[] rangeText = new Text[2];
					rangesText.add(rangeText);
					GridData data =  new GridData();
					data.horizontalAlignment = SWT.FILL;
					data.grabExcessHorizontalSpace = true;
					rangeText[0] = new Text(container, SWT.BORDER | SWT.FILL);
					rangeText[0].setLayoutData(data);
					rangeText[1] = new Text(container, SWT.BORDER | SWT.FILL);
					rangeText[1].setLayoutData(data);
					/*Override
						public void keyPressed(KeyEvent e) {
						}

						@Override
						public void keyReleased(KeyEvent e) {
							if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
								setPageComplete(true);
							} else {
								setPageComplete(false);
							}
						}
					});
					rangeText[1].addKeyListener(new KeyListener() {

						@Override
						public void keyPressed(KeyEvent e) {
						}

						@Override
						public void keyReleased(KeyEvent e) {
							if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
								setPageComplete(true);
							} else {
								setPageComplete(false);
							}
						}
					});*/
					rangeText[0].addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							selectorCanvas.redraw();
						}
					});
					
					rangeText[1].addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							selectorCanvas.redraw();
						}
					});
					selectorCanvas.addPaintListener(new PaintListener(){
						public void paintControl(PaintEvent e) {
			            	if(rangeText[0].getText().isEmpty() && rangeText[1].getText().isEmpty())
			            	{
			            		controlsValidation.put(selectorCanvas,true);
			                	validateInput();
			                	return;
			            	}
			            	else if(sel instanceof TotalOrderedSelector)
			            	{
				            	TotalOrderedSelector fakeSel = (TotalOrderedSelector) sel.selectorClone();
								try {
									fakeSel.addRange(rangeText[0].getText(), rangeText[0].getText());
									fakeSel.empty();
									controlsValidation.put(selectorCanvas,true);
				                	validateInput();
								} catch (InvalidRangeException e1) {
									e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
									controlsValidation.put(selectorCanvas,false);
				                	validateInput();
				                	return;
								}
								try {
									fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
									fakeSel.empty();
									controlsValidation.put(selectorCanvas,true);
				                	validateInput();
								} catch (InvalidRangeException e1) {
									e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
									controlsValidation.put(selectorCanvas,false);
				                	validateInput();
				                	return;
									
								}
								try {
									fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
									fakeSel.empty();
									controlsValidation.put(selectorCanvas,true);
				                	validateInput();
								} catch (InvalidRangeException e1) {
									e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
									controlsValidation.put(selectorCanvas,false);
				                	validateInput();
									return;
								}
			            	}
			            	else if(sel instanceof IntMatcherSelector)
			            	{
			            		IntMatcherSelector fakeSel = (IntMatcherSelector) sel.selectorClone();
								try {
									fakeSel.addRange(rangeText[0].getText(), rangeText[0].getText());
									fakeSel.empty();
									controlsValidation.put(selectorCanvas,true);
				                	validateInput();
								} catch (InvalidRangeException e1) {
									e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
									controlsValidation.put(selectorCanvas,false);
				                	validateInput();
				                	return;
								}
								try {
									fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
									fakeSel.empty();
									controlsValidation.put(selectorCanvas,true);
				                	validateInput();
								} catch (InvalidRangeException e1) {
									e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
									controlsValidation.put(selectorCanvas,false);
				                	validateInput();
				                	return;
									
								}
								try {
									fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
									fakeSel.empty();
									controlsValidation.put(selectorCanvas,true);
				                	validateInput();
								} catch (InvalidRangeException e1) {
									e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
									controlsValidation.put(selectorCanvas,false);
				                	validateInput();
									return;
								}
			            	}
							e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
							controlsValidation.put(selectorCanvas,true);
		                	validateInput();
			            }
			        });
					/*rangeText[1].addFocusListener(new FocusListener() {
						
						@Override
						public void focusLost(FocusEvent e) {
							TotalOrderedSelector fakeSel = (TotalOrderedSelector) sel.selectorClone();
							try {
								fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
								fakeSel.empty();
								rangeText[0].setBackground( rangeText[1].getDisplay().getSystemColor(SWT.COLOR_GREEN));
								rangeText[0].setForeground( rangeText[1].getDisplay().getSystemColor(SWT.COLOR_BLACK));
							} catch (Exception e1) {
								rangeText[1].setBackground( rangeText[1].getDisplay().getSystemColor(SWT.COLOR_RED));
								rangeText[1].setForeground( rangeText[1].getDisplay().getSystemColor(SWT.COLOR_WHITE));
							}

							/*try{
								fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
								fakeSel.empty();
								rangeText[1].setBackground( rangeText[1].getDisplay().getSystemColor(SWT.COLOR_GREEN));
								rangeText[1].setForeground( rangeText[1].getDisplay().getSystemColor(SWT.COLOR_BLACK));
							} catch(Exception e2){
								//TODO: cambiare in bordi rossi
								//rangeText[0].setForeground( new Color(rangeText[0].getDisplay(), 0, 0, 255));
								//rangeText[1].setForeground( new Color(rangeText[0].getDisplay(), 0, 0, 255));
							}
						}
						
						@Override
						public void focusGained(FocusEvent e) {
							rangeText[1].setBackground(defaultTextBackgroundColor);
							rangeText[1].setForeground(defaultTextForegroundColor);
						}
					});*/
				}
				final Button b = new Button(container, SWT.PUSH | SWT.BORDER | SWT.SINGLE);
				b.setText("Add Range");
				b.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						
						final Text[] rangeText = new Text[2];
						rangesText.add(rangeText);
						GridData data =  new GridData();
						data.horizontalAlignment = SWT.FILL;
						data.grabExcessHorizontalSpace = true;
						Label l = new Label(container, SWT.NONE);
						l.moveAbove(b);
						final Canvas selectorCanvas = new Canvas(container, SWT.NONE);
						selectorsCanvas.add(selectorCanvas);
						selectorCanvas.setLayoutData(canvasData);
						selectorCanvas.moveBelow(l);
						Label l2 = new Label(container, SWT.NONE);
						l2.moveBelow(selectorCanvas);
						rangeText[0] = new Text(container, SWT.BORDER | SWT.SINGLE);
						rangeText[0].moveBelow(l2);
						rangeText[0].setLayoutData(data);
						rangeText[1] = new Text(container, SWT.BORDER | SWT.SINGLE);
						rangeText[1].setLayoutData(data);
						rangeText[1].moveBelow(rangeText[0]);;
						container.layout();
						sc.setExpandHorizontal(true);
						sc.setExpandVertical(true);
						sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
						sc.layout();
						/*rangeText[0].addKeyListener(new KeyListener() {

							@Override
							public void keyPressed(KeyEvent e) {
							}

							@Override
							public void keyReleased(KeyEvent e) {
								if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
									setPageComplete(true);
								} else {
									setPageComplete(false);
								}
							}
						});
						rangeText[1].addKeyListener(new KeyListener() {

							@Override
							public void keyPressed(KeyEvent e) {
							}

							@Override
							public void keyReleased(KeyEvent e) {
								if (!ruleName.getText().isEmpty() && action.getSelectionIndex() != -1) {
									setPageComplete(true);
								} else {
									setPageComplete(false);
								}
							}
						});*/
						selectorCanvas.addPaintListener(new PaintListener(){
							public void paintControl(PaintEvent e) {
				            	if(rangeText[0].getText().isEmpty() && rangeText[1].getText().isEmpty())
				            	{
				            		controlsValidation.put(selectorCanvas,true);
				                	validateInput();
				                	return;
				            	}
				            	else
				            	{
					            	if(sel instanceof TotalOrderedSelector)
					            	{
					            		TotalOrderedSelector fakeSel = (TotalOrderedSelector) sel.selectorClone();
										try {
											fakeSel.addRange(rangeText[0].getText(), rangeText[0].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
						                	return;
										}
										try {
											fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
						                	return;
											
										}
										try {
											fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
											return;
										}
					            	}
					            	else if(sel instanceof IntMatcherSelector)
					            	{
					            		IntMatcherSelector fakeSel = (IntMatcherSelector) sel.selectorClone();
										try {
											fakeSel.addRange(rangeText[0].getText(), rangeText[0].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
						                	return;
										}
										try {
											fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
						                	return;
											
										}
										try {
											fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
											fakeSel.empty();
											controlsValidation.put(selectorCanvas,true);
						                	validateInput();
										} catch (InvalidRangeException e1) {
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
											return;
										}
					            	}
				            	}
								e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, ruleNameCanvas.getBounds().width, ruleNameCanvas.getBounds().height);
								controlsValidation.put(selectorCanvas,true);
			                	validateInput();
				            }
				        });
						
						rangeText[0].addModifyListener(new ModifyListener() {
							
							@Override
							public void modifyText(ModifyEvent e) {
								selectorCanvas.redraw();
							}
						});
						
						rangeText[1].addModifyListener(new ModifyListener() {
							
							@Override
							public void modifyText(ModifyEvent e) {
								selectorCanvas.redraw();
							}
						});
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
				
				
				SelectorSet.put(selectorName, rangesText);
				
			}
			else if(sel instanceof ExactMatchSelectorImpl)
			{
				Label l = new Label(container, SWT.NONE);
				l.moveAbove(selectorLabel);
				//final Text exactMatchText = new Text(container, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.READ_ONLY);
				final Text exactMatchText = new Text(container, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL);
				final GridData data =  new GridData();
				data.horizontalAlignment = SWT.FILL;
				data.widthHint = selectorLabel.getSize().x - selectorLabel.getLocation().x;
				data.heightHint = selectorLabel.getSize().y - selectorLabel.getLocation().y;
				data.grabExcessHorizontalSpace = true;
				data.horizontalSpan=2;
				
				exactMatchText.setEnabled(true);
				Listener scrollBarListener = new Listener (){
				    @Override
				    public void handleEvent(Event event) {
				      Text t = (Text)event.widget;
				      GC gc = new GC(t);
					  gc.setFont(t.getFont());
					  final int textWidth = gc.stringExtent(t.getText()).x;
					  if(t.getSize().x <= textWidth) 
					  {
						  //data.heightHint = t.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
						  t.getHorizontalBar().setVisible(true);//r2.width <= p.x);
				    	  t.getHorizontalBar().setEnabled(true);
						  data.heightHint = gc.getFontMetrics().getHeight();
					  }
					  else
					  {
						  data.heightHint = selectorLabel.getSize().y - selectorLabel.getLocation().y;
						  t.getHorizontalBar().setVisible(false);
						  t.getHorizontalBar().setEnabled(false);
					  }
					  gc.dispose();
					  
					  exactMatchText.setLayoutData(data);
				      if (event.type == SWT.Modify){
				        t.getParent().layout(true);
				      t.showSelection();
				      container.layout();
				      }
				    }
				};
				exactMatchText.addListener(SWT.Activate, scrollBarListener);
				exactMatchText.addListener(SWT.Resize, scrollBarListener);
			    exactMatchText.addListener(SWT.Modify, scrollBarListener);
			    exactMatchText.setLayoutData(data);
			    exactMatchText.setSize(l.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				try{
					exactMatchText.setText(rule.getConditionClause().get(selectorLabel.getText().substring(0, selectorLabel.getText().length()-2)).toSimpleString());
				}
				catch(NullPointerException e){
				}
				container.layout(true);
				GC gc = new GC(exactMatchText);
				gc.setFont(exactMatchText.getFont());
				final int textWidth = gc.stringExtent(exactMatchText.getText()).x;
				if(exactMatchText.getSize().x <= textWidth) 
				{
				 //data.heightHint = t.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
					exactMatchText.getHorizontalBar().setVisible(true);//r2.width <= p.x);
					exactMatchText.getHorizontalBar().setEnabled(true);
					data.heightHint = gc.getFontMetrics().getHeight();
				}
				else
				{
					data.heightHint = selectorLabel.getSize().y - selectorLabel.getLocation().y;
					exactMatchText.getHorizontalBar().setVisible(false);
					exactMatchText.getHorizontalBar().setEnabled(false);
			  	}
				gc.dispose();
				container.layout(true);
				exactMatchText.setLayoutData(data);
				Button exact_button = new Button(container, SWT.PUSH | SWT.BORDER | SWT.SINGLE);
				exact_button.setText("Modify");
				exact_button.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						
						ModifyRuleExactMatchWizard wiz;
						try {
							boolean found = false;
							if(PolicyToolUIModel.getInstance().getSelectorTypes().getAllSelectorTypes().get(selectorLabel.getText().substring(0, selectorLabel.getText().length()-2)) instanceof InterfaceSelector)
							{
								// trovare firewall legato alla policy e creare selettore con i nomi delle sue interfacce
								for(SecurityControl f : PolicyToolUIModel.getInstance().getLandscape().getFirewallList().values())
									if(f.getPolicy().equals(policy) || f.getNAT().equals(policy) || f.getVPN().equals(policy) || f.getRouting().equals(policy))
									{
										rule.getConditionClause().addSelector(selectorLabel.getText().substring(0, selectorLabel.getText().length()-2), new InterfaceSelector(f.getInterfacesArray()));
										found = true;
										break;
									}
								if(!found)
								{
									MessageBox messageBox = new MessageBox(container.getShell(), SWT.ICON_WARNING | SWT.OK);
							        messageBox.setText("Error");
							        messageBox.setMessage("To use interface selector you must assign the policy to a firewall!");
							        int buttonID = messageBox.open();
							        switch(buttonID) {
							          case SWT.YES:
							        	  return;
							        }
								}
							}
							else found = true;
							if(found)
							{
								wiz = new ModifyRuleExactMatchWizard(rule.getConditionClause(),selectorLabel.getText().substring(0, selectorLabel.getText().length()-2), policy);
								WizardDialog wizardDialog = new WizardDialog
										(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(), wiz);
								if (wizardDialog.open() == Window.OK) {
									exactMatchText.setText(rule.getConditionClause().get(selectorLabel.getText().substring(0, selectorLabel.getText().length()-2)).toSimpleString());
								} else {
									System.out.println("Cancel pressed");
								}
							}
						} catch (InstantiationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IncompatibleSelectorException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
			else if(sel instanceof StringMatcherSelector)
			{
				final LinkedList<Text[]> rangesText = new LinkedList<Text[]>();
				if(rule.getConditionClause()!=null && rule.getConditionClause().get(selectorName)!=null)
				{
					String[] sel_texts = ((StringMatcherSelector)rule.getConditionClause().get(selectorName)).toStringVector();
					Control lastLabel = selectorLabel;
					(new Label(container, SWT.NONE)).moveAbove(lastLabel);
					for(String selText: sel_texts)
					{
						final Text[] rangeText = new Text[1];
						rangesText.add(rangeText);
						GridData data =  new GridData();
						data.horizontalSpan=2;
						data.horizontalAlignment = SWT.FILL;
						data.grabExcessHorizontalSpace = true;
						rangeText[0] = new Text(container, SWT.BORDER);
						rangeText[0].setLayoutData(data);
						rangeText[0].setText(selText);
						lastLabel = new Label(container, SWT.NONE);
						lastLabel.setLayoutData(new GridData(SWT.DEFAULT,SWT.DEFAULT,false,false,2,1));
					}
					lastLabel.dispose();
				}
				else
				{
					(new Label(container, SWT.NONE)).moveAbove(selectorLabel);
					final Text[] rangeText = new Text[1];
					rangesText.add(rangeText);
					GridData data =  new GridData();
					data.horizontalSpan=2;
					data.horizontalAlignment = SWT.FILL;
					data.grabExcessHorizontalSpace = true;
					rangeText[0] = new Text(container, SWT.BORDER | SWT.FILL);
					rangeText[0].setLayoutData(data);
				}
				final Button b = new Button(container, SWT.PUSH | SWT.BORDER | SWT.SINGLE);
				b.setText("Add Range");
				b.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						final Text[] rangeText = new Text[1];
						rangesText.add(rangeText);
						GridData data =  new GridData();
						data.horizontalAlignment = SWT.FILL;
						data.grabExcessHorizontalSpace = true;
						data.horizontalSpan=2;
						Label l = new Label(container, SWT.NONE);
						l.moveAbove(b);
						final Canvas selectorCanvas = new Canvas(container, SWT.NONE);
						selectorsCanvas.add(selectorCanvas);
						selectorCanvas.setLayoutData(canvasData);
						selectorCanvas.moveBelow(l);
						Label l2 = new Label(container, SWT.NONE);
						l2.moveBelow(selectorCanvas);
						rangeText[0] = new Text(container, SWT.BORDER | SWT.SINGLE);
						rangeText[0].moveBelow(l2);
						rangeText[0].setLayoutData(data);
						container.layout();
						sc.setExpandHorizontal(true);
						sc.setExpandVertical(true);
						sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
						sc.layout();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
				
				
				SelectorSet.put(selectorName, rangesText);
				
			}
		}
		
		
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		setControl(container);
		setPageComplete(true);
		validateInput();
		ruleName.setFocus();
	}

	public String getRuleName() {
		return ruleName.getText();
	}

	public Action getAction() {
		Action a = ActionSet[action.getSelectionIndex()];
		if(tunnelS!=null || tunnelD!=null)
		{
			transformation = new ConditionClause();
			if(tunnelS!=null) if(!tunnelS.isDisposed())
			{
				IpSelector tunnelSource = new IpSelector();
				try {
					tunnelSource.addRange(tunnelS.getText());
					transformation.addSelector("Source Address", tunnelSource);
				} catch (InvalidIpAddressException | InvalidRangeException | IncompatibleSelectorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(tunnelD!=null) if(!tunnelD.isDisposed())
			{
				IpSelector tunnelDest = new IpSelector();
				try {
					tunnelDest.addRange(tunnelD.getText());
					transformation.addSelector("Destination Address", tunnelDest);
				} catch (InvalidIpAddressException | InvalidRangeException | IncompatibleSelectorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(transformation!=null) if(!transformation.isEmpty()) ((TransformatonAction)a).setTransformation(transformation);
		return a;
	}
	
	public HashMap<String, Text> getActionDataTextMap() {
		return actionDataTextMap;
	}

	public ConditionClause getConditionClause() {

		for (String selectorName : SelectorSet.keySet()) {
			if (!SelectorSet.get(selectorName).get(0)[0].getText().equals("")) {
				Selector selector = PolicyToolUIModel.getInstance().getSelectorTypes().getSelectorType(selectorName).selectorClone();
				if (selector instanceof TotalOrderedSelector || selector instanceof IntMatcherSelector || selector instanceof StringMatcherSelector)
					try {
						for(Text[] ranges: SelectorSet.get(selectorName))
							if(selector instanceof StringMatcherSelector)
							{
								if(!(ranges[0].getText().isEmpty()))
									((StringMatcherSelector) selector).addRange(ranges[0].getText());
							}
							else if(!(ranges[0].getText().isEmpty() && ranges[1].getText().isEmpty()))
							{
								if(selector instanceof TotalOrderedSelector)
									((TotalOrderedSelector) selector).addRange(ranges[0].getText(),ranges[1].getText());
								else if(selector instanceof IntMatcherSelector)
									((IntMatcherSelector) selector).addRange(ranges[0].getText(),ranges[1].getText());
								
							}
					} catch (InvalidRangeException e) {
						e.printStackTrace();
					}
				
				selectorMap.put(selectorName, selector);
			}
		}
		if(rule.getConditionClause()!=null)
			for(Entry<String, Selector> e : rule.getConditionClause().getSelectors().entrySet())
				if(e.getValue() instanceof ExactMatchSelectorImpl)
					selectorMap.put(e.getKey(), e.getValue());
		return new ConditionClause(selectorMap);
	}

	public Integer getExternalData() {
		return Integer.decode(priority.getText());
	}
	
	public ConditionClause getTransformation() {
		return transformation;
	}
	
	public void validateInput()
	{
		for(Entry<Control, Boolean> e : controlsValidation.entrySet())
		{
			if(!e.getValue())
			{
				setPageComplete(false);
				return;
			}
			setPageComplete(true);
		}
	}
}
