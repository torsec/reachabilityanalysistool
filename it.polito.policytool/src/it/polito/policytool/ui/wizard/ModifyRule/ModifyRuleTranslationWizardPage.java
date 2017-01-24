package it.polito.policytool.ui.wizard.ModifyRule;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.util.Figure;
import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.TotalOrderedSelector;
import it.polito.policytoollib.rule.selector.impl.InterfaceSelector;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Map.Entry;
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
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class ModifyRuleTranslationWizardPage extends WizardPage {

	private final Logger						LOGGER	= Logger.getLogger(ModifyRuleTranslationWizardPage.class.getName());

	private Composite							container;
	
	private HashMap<String, LinkedList<Text[]>> SelectorSet;
	private ConditionClause						cc;
	private LinkedHashMap<String, Selector> 	selectorMap;
	private Vector<Canvas>						selectorsCanvas;
	private HashMap<Control,Boolean>			controlValid;

	private Image 								okImage;
	private Image 								wrongImage;
	
	private Policy 								policy;


	protected ModifyRuleTranslationWizardPage(String pageName, ConditionClause cc, Policy policy) {
		super(pageName);
		this.SelectorSet = new HashMap<String, LinkedList<Text[]>>();
		this.cc=cc;
		setTitle(pageName);
		selectorMap = new LinkedHashMap<>();
		setDescription("Modify Translation");
		selectorsCanvas = new Vector<Canvas>();
		okImage = Figure.OK.load();
		wrongImage = Figure.ERROR.load();
		controlValid = new HashMap<Control, Boolean>();
		this.policy = policy;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createControl(Composite parent) {

		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		container = new Composite(sc, SWT.NONE);
		container.setLayout(new GridLayout(5, false));
		container.setSize(400,400);
		sc.setContent(container);
		
		for(Canvas c : selectorsCanvas)
			if(!c.isDisposed())
				c.dispose();
		selectorsCanvas.clear();
		
		for (final Selector sel : PolicyToolUIModel.getInstance().getSelectorTypes().getAllSelectorTypes().values()) {
			String selectorName = sel.getLabel();
			final Label selectorLabel = new Label(container, SWT.NONE);
			selectorLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			selectorLabel.setText(selectorName+" :");
			if(sel instanceof TotalOrderedSelector)
			{
				final LinkedList<Text[]> rangesText = new LinkedList<Text[]>();
				if(cc!=null && cc.get(selectorName)!=null)
				{
					String text = cc.get(selectorName).toString();
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
						final GridData canvasData = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
						Label lab = null;
						if(selText!=sel_texts[0]){ lab = new Label(container, SWT.NONE); }
						String[] ranges =selText.split("-");
						final Text[] rangeText = new Text[2];
						rangesText.add(rangeText);
						GridData data =  new GridData();
						data.horizontalAlignment = SWT.FILL;
						data.grabExcessHorizontalSpace = true;
						rangeText[0] = new Text(container, SWT.BORDER);
						rangeText[0].setText(ranges[0]);
						rangeText[0].setLayoutData(data);
						rangeText[1] = new Text(container, SWT.BORDER);
						rangeText[1].setText(ranges[1]);
						rangeText[1].setLayoutData(data);
						canvasData.heightHint=rangeText[0].getLineHeight()*2;
						canvasData.widthHint=rangeText[0].getLineHeight()*2;
						selectorCanvas.setLayoutData(canvasData);
						if(lab!=null) selectorCanvas.moveAbove(lab);
						else selectorCanvas.moveAbove(selectorLabel);
						controlValid.put(selectorCanvas, true);
						selectorCanvas.addPaintListener(new PaintListener(){
				            public void paintControl(PaintEvent e) {
				            	if(rangeText[0].getText().isEmpty() && rangeText[1].getText().isEmpty())
				            	{
				            		controlValid.put(selectorCanvas, true);
									validateInput();
				            		return;
				            	}
				            	else
				            	{
					            	TotalOrderedSelector fakeSel = (TotalOrderedSelector) sel.selectorClone();
									try {
										fakeSel.addRange(rangeText[0].getText(), rangeText[0].getText());
										fakeSel.empty();
										controlValid.put(selectorCanvas, true);
										validateInput();
									} catch (InvalidRangeException e1) {
										e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
										controlValid.put(selectorCanvas, false);
										validateInput();
										return;
									}
									try {
										fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
										fakeSel.empty();
										controlValid.put(selectorCanvas, true);
										validateInput();
									} catch (InvalidRangeException e1) {
										controlValid.put(selectorCanvas, false);
										validateInput();
										e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
										return;
									}
									try {
										fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
										fakeSel.empty();
										controlValid.put(selectorCanvas, true);
										validateInput();
									} catch (InvalidRangeException e1) {
										controlValid.put(selectorCanvas, false);
										validateInput();
										e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
										return;
									}
				            	}
								e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
				            }
				        });
						
						lastLabel = new Label(container, SWT.NONE);
					}
					lastLabel.dispose();
					
				}
				else
				{
					final Canvas selectorCanvas = new Canvas(container, SWT.NONE);
					selectorsCanvas.add(selectorCanvas);
					final GridData canvasData = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
					final Text[] rangeText = new Text[2];
					rangesText.add(rangeText);
					GridData data =  new GridData();
					data.horizontalAlignment = SWT.FILL;
					data.grabExcessHorizontalSpace = true;
					rangeText[0] = new Text(container, SWT.BORDER | SWT.FILL);
					rangeText[0].setLayoutData(data);
					rangeText[1] = new Text(container, SWT.BORDER | SWT.FILL);
					rangeText[1].setLayoutData(data);
					canvasData.heightHint=rangeText[0].getLineHeight()*2;
					canvasData.widthHint=rangeText[0].getLineHeight()*2;
					selectorCanvas.setLayoutData(canvasData);
					selectorCanvas.moveAbove(selectorLabel);
					selectorCanvas.addPaintListener(new PaintListener(){
						public void paintControl(PaintEvent e) {
			            	if(rangeText[0].getText().isEmpty() && rangeText[1].getText().isEmpty())
			            	{
			            		controlValid.put(selectorCanvas, true);
								validateInput();
			            		return;
			            	}
			            	else
			            	{
				            	TotalOrderedSelector fakeSel = (TotalOrderedSelector) sel.selectorClone();
								try {
									fakeSel.addRange(rangeText[0].getText(), rangeText[0].getText());
									fakeSel.empty();
									controlValid.put(selectorCanvas, true);
									validateInput();
								} catch (InvalidRangeException e1) {
									e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
									controlValid.put(selectorCanvas, false);
									validateInput();
									return;
								}
								try {
									fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
									fakeSel.empty();
									controlValid.put(selectorCanvas, true);
									validateInput();
								} catch (InvalidRangeException e1) {
									controlValid.put(selectorCanvas, false);
									validateInput();
									e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
									return;
								}
								try {
									fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
									fakeSel.empty();
									controlValid.put(selectorCanvas, true);
									validateInput();
								} catch (InvalidRangeException e1) {
									controlValid.put(selectorCanvas, false);
									validateInput();
									e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
									return;
								}
			            	}
			            	controlValid.put(selectorCanvas, true);
							validateInput();
			            	e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
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
				final Button b = new Button(container, SWT.PUSH | SWT.BORDER | SWT.SINGLE);
				b.setText("Add Range");
				b.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						final Canvas selectorCanvas = new Canvas(container, SWT.NONE);
						selectorsCanvas.add(selectorCanvas);
						final GridData canvasData = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
						final Text[] rangeText = new Text[2];
						rangesText.add(rangeText);
						GridData data =  new GridData();
						data.horizontalAlignment = SWT.FILL;
						data.grabExcessHorizontalSpace = true;
						Label l = new Label(container, SWT.NONE);
						l.moveAbove(b);
						Label l2 = new Label(container, SWT.NONE);
						l2.moveBelow(l);
						rangeText[0] = new Text(container, SWT.BORDER | SWT.SINGLE);
						rangeText[0].moveBelow(l2);
						rangeText[0].setLayoutData(data);
						rangeText[1] = new Text(container, SWT.BORDER | SWT.SINGLE);
						rangeText[1].setLayoutData(data);
						rangeText[1].moveBelow(rangeText[0]);
						canvasData.heightHint=rangeText[0].getLineHeight()*2;
						canvasData.widthHint=rangeText[0].getLineHeight()*2;
						selectorCanvas.setLayoutData(canvasData);
						selectorCanvas.moveAbove(l2);
						selectorCanvas.addPaintListener(new PaintListener(){
							public void paintControl(PaintEvent e) {
				            	if(rangeText[0].getText().isEmpty() && rangeText[1].getText().isEmpty())
				            	{
				            		controlValid.put(selectorCanvas, true);
									validateInput();
				            		return;
				            	}
				            	else
				            	{
					            	TotalOrderedSelector fakeSel = (TotalOrderedSelector) sel.selectorClone();
									try {
										fakeSel.addRange(rangeText[0].getText(), rangeText[0].getText());
										fakeSel.empty();
										controlValid.put(selectorCanvas, true);
										validateInput();
									} catch (InvalidRangeException e1) {
										e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
										controlValid.put(selectorCanvas, false);
										validateInput();
										return;
									}
									try {
										fakeSel.addRange(rangeText[1].getText(), rangeText[1].getText());
										fakeSel.empty();
										controlValid.put(selectorCanvas, true);
										validateInput();
									} catch (InvalidRangeException e1) {
										controlValid.put(selectorCanvas, false);
										validateInput();
										e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
										return;
									}
									try {
										fakeSel.addRange(rangeText[0].getText(), rangeText[1].getText());
										fakeSel.empty();
										controlValid.put(selectorCanvas, true);
										validateInput();
									} catch (InvalidRangeException e1) {
										controlValid.put(selectorCanvas, false);
										validateInput();
										e.gc.drawImage(wrongImage, 0, 0, wrongImage.getBounds().width, wrongImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
										return;
									}
				            	}
				            	controlValid.put(selectorCanvas, true);
								validateInput();
				            	e.gc.drawImage(okImage, 0, 0, okImage.getBounds().width, okImage.getBounds().height, 0, 0, selectorCanvas.getBounds().width, selectorCanvas.getBounds().height);
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
			else if(sel instanceof ExactMatchSelector)
			{
				Label l = new Label(container, SWT.NONE);
				l.moveAbove(selectorLabel);
				final Text exactMatchText = new Text(container, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL);
				
				GridData data =  new GridData();
				data.horizontalAlignment = SWT.FILL;
				data.grabExcessHorizontalSpace = true;
				data.horizontalSpan=2;
				
				exactMatchText.setEnabled(true);
				Listener scrollBarListener = new Listener (){
				    @Override
				    public void handleEvent(Event event) {
				      Text t = (Text)event.widget;
				      Rectangle r1 = t.getClientArea();
				      Rectangle r2 = t.computeTrim(r1.x, r1.y, r1.width, r1.height);
				      Point p = t.computeSize(SWT.DEFAULT,  SWT.DEFAULT,  true);
				      t.getHorizontalBar().setVisible(r2.width <= p.x);
				      t.getHorizontalBar().setEnabled(true);
				      if (event.type == SWT.Modify){
				        t.getParent().layout(true);
				      t.showSelection();
				      }
				    }};
				    exactMatchText.addListener(SWT.Resize, scrollBarListener);
				    exactMatchText.addListener(SWT.Modify, scrollBarListener);
				    exactMatchText.setLayoutData(data);
				try{
					exactMatchText.setText(cc.get(selectorLabel.getText().substring(0, selectorLabel.getText().length()-2)).toSimpleString());
					
				}
				catch(NullPointerException e){
				}
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
									if(f.getPolicy().equals(policy))
									{
										cc.addSelector(selectorLabel.getText().substring(0, selectorLabel.getText().length()-2), new InterfaceSelector(f.getInterfacesArray()));
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
								wiz = new ModifyRuleExactMatchWizard(cc,selectorLabel.getText().substring(0, selectorLabel.getText().length()-2), policy);
								WizardDialog wizardDialog = new WizardDialog
										(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(), wiz);
								if (wizardDialog.open() == Window.OK) {
									exactMatchText.setText(cc.get(selectorLabel.getText().substring(0, selectorLabel.getText().length()-2)).toSimpleString());
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
		}

		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		setControl(container);
		setPageComplete(true);
	}

	public void validateInput()
	{
		for(Entry<Control, Boolean> e : controlValid.entrySet())
		{
			if(!e.getValue())
			{
				setPageComplete(false);
				return;
			}
			setPageComplete(true);
		}
	}
	
	public ConditionClause getConditionClause() {

		for (String selectorName : SelectorSet.keySet()) {
			if (!SelectorSet.get(selectorName).get(0)[0].getText().equals("")) {
				Selector selector = PolicyToolUIModel.getInstance().getSelectorTypes().getSelectorType(selectorName).selectorClone();
				if (selector instanceof TotalOrderedSelector)
					try {
						for(Text[] ranges: SelectorSet.get(selectorName))
							((TotalOrderedSelector) selector).addRange(ranges[0].getText(),ranges[1].getText());
					} catch (InvalidRangeException e) {
						e.printStackTrace();
					}

				selectorMap.put(selectorName, selector);
			}
		}
		if(cc!=null)
			for(Entry<String, Selector> e : cc.getSelectors().entrySet())
				if(e.getValue() instanceof ExactMatchSelector)
					selectorMap.put(e.getKey(), e.getValue());
		return new ConditionClause(selectorMap);
	}
}
