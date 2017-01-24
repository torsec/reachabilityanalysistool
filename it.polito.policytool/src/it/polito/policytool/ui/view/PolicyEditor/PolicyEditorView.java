package it.polito.policytool.ui.view.PolicyEditor;


import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.wizard.ModifyRule.ModifyRuleWizard;
import it.polito.policytool.ui.wizard.ModifyRule.NewRuleWizard;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class PolicyEditorView extends EditorPart {
	public PolicyEditorView() {
	}

	public static final String		ID			= "it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView";

	private Policy					policy;
	

	private PolicyEditorInput		policyEditorInput;

	private final FormToolkit		formToolkit	= new FormToolkit(Display.getDefault());

	/** The form. */
	private Form					form;
	private PolicyEditorView		thisView;
	private TableViewer				tableViewer;
	private Composite				composite;
	private Composite				parent;
	private PolicyViewComparator	comparator;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof PolicyEditorInput)) {
			throw new RuntimeException("Wrong input");
		}
		PolicyToolUIModel.getInstance().getPolicyEditorView().add(this);
		this.policyEditorInput = (PolicyEditorInput) input;
		setSite(site);
		setInput(input);
		policy = policyEditorInput.getPolicy();
		setPartName(input.getName());
		thisView=this;
		this.addListenerObject(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				PolicyToolUIModel.getInstance().getPolicyEditorView().removeElement(thisView);
			}
		});
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent=parent;
		parent.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				PolicyToolUIModel.getInstance().getPolicyEditorView().remove(thisView);
			}
		});
		createControl();
	}
	
	public void createControl(){
		form = formToolkit.createForm(parent);
		form.getBody().setLayout(new GridLayout(2, false));

		composite = new Composite(form.getBody(), SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		composite.addFocusListener(new FocusListener() {
			
			@Override
			public void focusGained(FocusEvent e) {
				tableViewer.refresh();
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		final Table table_1 = tableViewer.getTable();
		table_1.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event e) {
	        	try{
					TableItem[] selection = table_1.getSelection();
					ConditionClause oldCC = ((GenericRule) selection[0].getData()).getConditionClause().conditionClauseClone();
		    		Action oldAction = ((GenericRule) selection[0].getData()).getAction();
					WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(), new ModifyRuleWizard(policy, (GenericRule) selection[0].getData(), tableViewer));
					if (wizardDialog.open() == Window.OK) {
						
					} else {
						System.out.println("Cancel pressed");
						((GenericRule) selection[0].getData()).setConditionClause(oldCC);
						((GenericRule) selection[0].getData()).setAction(oldAction);
					}
				}
	        	catch(ArrayIndexOutOfBoundsException e2)
	        	{
	        		return;
	        	}
			}
		});
	 
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.setContentProvider(new ArrayContentProvider());
		Menu headerMenu = new Menu(parent.getShell(), SWT.POP_UP);
		tableViewer.getTable().setMenu(headerMenu);
		comparator = new PolicyViewComparator();
		tableViewer.setComparator(comparator);

		int i = 0;

		comparator.addProperty(i, "Name");
		TableViewerColumn tableViewerColumn_0 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName_0 = tableViewerColumn_0.getColumn();
		tableColumnLayout.setColumnData(tblclmnName_0, new ColumnWeightData(20, 100, true));
		tblclmnName_0.setText("Name");
		tblclmnName_0.addSelectionListener(getSelectionAdapter(tblclmnName_0, i++));
		createMenuItem(headerMenu, tblclmnName_0);

		tableViewerColumn_0.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				GenericRule exp = (GenericRule) element;
				return exp.getName();
			}
		});

		if (policy.getResolutionStrategy() instanceof FMRResolutionStrategy) {
			comparator.addProperty(i, "Priority");
			TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tblclmnName_1 = tableViewerColumn_1.getColumn();
			tableColumnLayout.setColumnData(tblclmnName_1, new ColumnWeightData(20, 100, true));
			// tblclmnName_11.setWidth(100);
			tblclmnName_1.setText("Priority");
			tblclmnName_1.addSelectionListener(getSelectionAdapter(tblclmnName_1, i++));
			createMenuItem(headerMenu, tblclmnName_1);

			tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
				public String getText(Object element) {
					GenericRule rule = (GenericRule) element;
					try {
						FMRResolutionStrategy rsm = (FMRResolutionStrategy) policy.getResolutionStrategy();
						return rsm.getExternalData(rule).toString();
					} catch (Exception e) {

					}

					return "";
				}
			});
		}

		comparator.addProperty(i, "Action");
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName_2 = tableViewerColumn_2.getColumn();
		tableColumnLayout.setColumnData(tblclmnName_2, new ColumnWeightData(20, 100, true));
		// tblclmnName_10.setWidth(100);
		tblclmnName_2.setText("Action");
		tblclmnName_2.addSelectionListener(getSelectionAdapter(tblclmnName_2, i++));
		createMenuItem(headerMenu, tblclmnName_2);

		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				GenericRule exp = (GenericRule) element;
				return exp.getAction().toString();
			}
		});

		Button btnAddRule = new Button(form.getBody(), SWT.NONE);
		btnAddRule.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

				WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(), new NewRuleWizard(policy, tableViewer));
				if (wizardDialog.open() == Window.OK) {

				} else {
					System.out.println("Cancel pressed");
				}
			}
		});
		formToolkit.adapt(btnAddRule, true, true);
		btnAddRule.setText("Add Rule");

		Button btnDelRule = new Button(form.getBody(), SWT.NONE);
		btnDelRule.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				int selected = tableViewer.getTable().getSelectionIndex();
				if(selected==-1) return;
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(composite.getShell(), style);
		        messageBox.setText("Delete rule");
		        messageBox.setMessage("Do you really want to delete the selected rule?");
		        if(messageBox.open() == SWT.YES)
		        {
		        	try {
						policy.removeRule((GenericRule) tableViewer.getTable().getItem(selected).getData());
					} catch (UnmanagedRuleException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (OperationNotPermittedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        	refresh();
					for(PolicyEditorView v : PolicyToolUIModel.getInstance().getPolicyEditorView()) v.refresh();
		        }
			}
		});
		formToolkit.adapt(btnDelRule, true, true);
		btnDelRule.setText("Delete Rule");
		
		for (String selectorName : PolicyToolUIModel.getInstance().getSelectorTypes().getSelectorNames()) {

			comparator.addSelector(i, selectorName);

			TableViewerColumn tableViewerColumn_i = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tblclmnName_i = tableViewerColumn_i.getColumn();
			tableColumnLayout.setColumnData(tblclmnName_i, new ColumnWeightData(20, 100, true));
			tblclmnName_i.setText(selectorName);
			tblclmnName_i.addSelectionListener(getSelectionAdapter(tblclmnName_i, i++));
			createMenuItem(headerMenu, tblclmnName_i);

			tableViewerColumn_i.setLabelProvider(new SelectorColumnLabelProvider(selectorName));
		}
		
		setInput(policy);
	}

	@Override
	public void setFocus() {
		tableViewer.refresh();
		composite.layout();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setInput(Policy policy) {
		this.policy = policy;
		comparator.setPolicy(policy);
		tableViewer.setInput(policy.getRuleSet());
	}

	private void createMenuItem(Menu parent, final TableColumn column) {
		final MenuItem itemName = new MenuItem(parent, SWT.CHECK);
		itemName.setText(column.getText());
		itemName.setSelection(column.getResizable());
		itemName.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (itemName.getSelection()) {
					column.setWidth(100);
					column.setResizable(true);
				} else {
					column.setWidth(0);
					column.setResizable(false);
				}
			}
		});
	}

	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int dir = comparator.getDirection();
				tableViewer.getTable().setSortDirection(dir);
				tableViewer.getTable().setSortColumn(column);
				tableViewer.refresh();
			}
		};
		return selectionAdapter;
	}

	public void refresh(){
		if(PolicyToolUIModel.getInstance().getPolicyToolModel()==null) return;
		form.dispose();
		createControl();
		parent.layout();
		parent.getShell().layout(true);
	}
	
	public void refreshParent(){
		parent.layout();
		parent.getShell().layout(true);
	}
	
	public Policy getPolicy() {
		return policy;
	}
}
