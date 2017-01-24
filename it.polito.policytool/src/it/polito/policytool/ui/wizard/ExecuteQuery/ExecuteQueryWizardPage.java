package it.polito.policytool.ui.wizard.ExecuteQuery;

import java.util.logging.Logger;



import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.wizard.ModifyRule.ModifyRuleExactMatchWizard;
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

public class ExecuteQueryWizardPage extends WizardPage {

	private final Logger LOGGER	= Logger.getLogger(ExecuteQueryWizardPage.class.getName());
	
	private Composite							container;
	private Vector<Canvas>						selectorsCanvas;
	
	private LinkedHashMap<Control,Boolean>		controlsValidation;
	
	Image okImage;
	Image wrongImage;
	
	private Canvas								dimensionReferenceCanvas;
	private Text								firstText;
	
	private HashMap<String, LinkedList<Text[]>> SelectorSet;
	
	private LinkedHashMap<String, Selector> selectorMap;
	private ConditionClause cc;

	protected ExecuteQueryWizardPage(String pageName) {
		super(pageName);
		this.SelectorSet = new HashMap<String, LinkedList<Text[]>>();
		setTitle(pageName);
		selectorMap = new LinkedHashMap<>();
		setDescription("Execute reachability query");
		okImage = Figure.OK.load();
		wrongImage = Figure.ERROR.load();
		selectorsCanvas = new Vector<Canvas>();
		controlsValidation = new LinkedHashMap<Control, Boolean>();		
		cc = new ConditionClause();
	}

	@Override
	public void createControl(Composite parent) {

		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		container = new Composite(sc, SWT.NONE);
		container.setLayout(new GridLayout(5, false));
		container.setSize(400,400);
		sc.setContent(container);
		final GridData canvasData = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		boolean canvasDataSet = false;
		
		for (final Selector sel : PolicyToolUIModel.getInstance().getSelectorTypes().getAllSelectorTypes().values()) {
			String selectorName = sel.getLabel();
			final Label selectorLabel = new Label(container, SWT.NONE);
			selectorLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			selectorLabel.setText(selectorName+" :");
			if(sel instanceof TotalOrderedSelector || sel instanceof IntMatcherSelector)
			{
				
				final LinkedList<Text[]> rangesText = new LinkedList<Text[]>();
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
				if(!canvasDataSet)
				{
					canvasData.heightHint=rangeText[0].getLineHeight()*2;
					canvasData.widthHint=rangeText[0].getLineHeight()*2;
					dimensionReferenceCanvas=selectorCanvas;
				}
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
								e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
								e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
								e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
								e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
								e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
								e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
								controlsValidation.put(selectorCanvas,false);
			                	validateInput();
								return;
							}
		            	}
						e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
						controlsValidation.put(selectorCanvas,true);
	                	validateInput();
		            }
		        });
				
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
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
											e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
											controlsValidation.put(selectorCanvas,false);
						                	validateInput();
											return;
										}
					            	}
				            	}
								e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, dimensionReferenceCanvas.getBounds().width, dimensionReferenceCanvas.getBounds().height);
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
								wiz = new ModifyRuleExactMatchWizard(cc,selectorLabel.getText().substring(0, selectorLabel.getText().length()-2), null);
								WizardDialog wizardDialog = new WizardDialog
										(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(), wiz);
								if (wizardDialog.open() == Window.OK) {
									exactMatchText.setText(cc.get(selectorLabel.getText().substring(0, selectorLabel.getText().length()-2)).toSimpleString());
								} else {
									System.out.println("Cancel pressed");
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
				(new Label(container, SWT.NONE)).moveAbove(selectorLabel);
				final Text[] rangeText = new Text[1];
				rangesText.add(rangeText);
				GridData data =  new GridData();
				data.horizontalSpan=2;
				data.horizontalAlignment = SWT.FILL;
				data.grabExcessHorizontalSpace = true;
				rangeText[0] = new Text(container, SWT.BORDER | SWT.FILL);
				rangeText[0].setLayoutData(data);
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
		if(cc!=null)
			for(Entry<String, Selector> e : cc.getSelectors().entrySet())
				if(e.getValue() instanceof ExactMatchSelectorImpl)
					selectorMap.put(e.getKey(), e.getValue());
		return new ConditionClause(selectorMap);
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

