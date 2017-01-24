package it.polito.policytool.ui.view.SelectorTypesEditor;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView;
import it.polito.policytool.ui.wizard.ModifyRule.ModifyRuleWizard;
import it.polito.policytool.ui.wizard.NewSelectorType.ModifySelectorTypeWizard;
import it.polito.policytool.ui.wizard.NewSelectorType.NewSelectorTypeWizard;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class SelectorTypesEditorView  extends ViewPart{
	
	public static final String ID = "it.polito.policytool.ui.view.SelectorTypesEditor.SelectorTypesEditorView";
	
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	private Form form;
	Composite composite;
	private TableViewer tableViewer;
	TableViewerColumn tableViewerColumn_1;
	
	private Button btnAddSelectorType;
	private Button btnMoveUp;
	private Button btnMoveDown;
	private Button btnDelSelectorType;

	public SelectorTypesEditorView() {
		PolicyToolUIModel.getInstance().setSelectorTypesEditorView(this);
	}

	@Override
	public void createPartControl(Composite parent) {
		Boolean modelLoaded = PolicyToolUIModel.getInstance().getPolicyToolModel()!=null;
		
		form = formToolkit.createForm(parent);
		form.getBody().setLayout(new GridLayout(1, false));
		
		composite = new Composite(form.getBody(), SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		
		final Table table_1 = tableViewer.getTable();
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		class UnresizableWizardDialog extends WizardDialog
		{
			private SelectionAdapter cancelListener;
			public UnresizableWizardDialog(Shell parentShell, IWizard newWizard) {
				super(parentShell, newWizard);
				setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER
						| SWT.APPLICATION_MODAL | getDefaultOrientation());
				setWizard(newWizard);
				// since VAJava can't initialize an instance var with an anonymous
				// class outside a constructor we do it here:
				cancelListener = new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						cancelPressed();
					}
				};
			}
			
		}
		
		table_1.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event e) {
	        TableItem[] selection = table_1.getSelection();
	        Shell shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.BORDER |
	        		SWT.APPLICATION_MODAL | SWT.MIN ) ;
	        		shell.setSize(500, 375);
	        		shell.setText("Selector Types"); 
	        WizardDialog wizardDialog = new UnresizableWizardDialog(shell, new ModifySelectorTypeWizard(selection[0].getText()));
	         
	        if (wizardDialog.open() == Window.OK) {
					
			} else {
			}
			}
		});
		
		table_1.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(table_1.getSelectionIndex()==-1)
				{
					btnMoveUp.setEnabled(false);
					btnMoveDown.setEnabled(false);
					btnDelSelectorType.setEnabled(false);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(table_1.getSelectionIndex()!=-1)
				{
					btnMoveUp.setEnabled(true);
					btnMoveDown.setEnabled(true);
					btnDelSelectorType.setEnabled(true);
				}
			}
		});
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		Menu headerMenu = new Menu(parent.getShell(), SWT.POP_UP);
		tableViewer.getTable().setMenu(headerMenu);
		
		TableViewerColumn tableViewerColumn_0 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName_0 = tableViewerColumn_0.getColumn();
		tableColumnLayout.setColumnData(tblclmnName_0, new ColumnWeightData(20, 100, true));
		tblclmnName_0.setText("Selector Name");
		
		tableViewerColumn_0.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return (String)element;
			}
		});
		
		tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName_1 = tableViewerColumn_1.getColumn();
		tableColumnLayout.setColumnData(tblclmnName_1, new ColumnWeightData(20, 100, true));
		tblclmnName_1.setText("Selector Type");
		
		Composite buttons = new Composite(form.getBody(), SWT.NONE);
		buttons.setLayout(new GridLayout(4, false));
		
		btnAddSelectorType = new Button(buttons, SWT.NONE);
		btnAddSelectorType.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				
				WizardDialog wizardDialog = new UnresizableWizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(),
					      new NewSelectorTypeWizard());
					    if (wizardDialog.open() == Window.OK) {
					    	
					      
					    } else {
					      System.out.println("Cancel pressed");
					       }
			}
		});
		formToolkit.adapt(btnAddSelectorType, true, true);
		btnAddSelectorType.setText("Add Selector Type");
		btnAddSelectorType.setEnabled(modelLoaded);
		
		btnDelSelectorType = new Button(buttons, SWT.NONE);
		btnDelSelectorType.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				
				int selected = tableViewer.getTable().getSelectionIndex();
				if(selected==-1) return;
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(composite.getShell(), style);
		        messageBox.setText("Delete selector type");
		        messageBox.setMessage("Do you really want to delete the selected selector type?");
		        if(messageBox.open() == SWT.YES)
		        {
		        	PolicyToolUIModel.getPolicyToolUIModel().getSelectorTypes().removeSelectorType((String) tableViewer.getTable().getItem(selected).getData());
		        	refresh();
					for(PolicyEditorView v : PolicyToolUIModel.getInstance().getPolicyEditorView()) v.refresh();
		        }
			}
		});
		formToolkit.adapt(btnDelSelectorType, true, true);
		btnDelSelectorType.setText("Delete Selector Type");
		btnDelSelectorType.setEnabled(false);
		
		btnMoveUp = new Button(buttons, SWT.NONE);
		btnMoveUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				
				int selected = tableViewer.getTable().getSelectionIndex();
				if(selected==-1) return;
				PolicyToolUIModel.getPolicyToolUIModel().getSelectorTypes().moveSelectorUp(selected);
				refresh();
				for(PolicyEditorView v : PolicyToolUIModel.getInstance().getPolicyEditorView()) v.refresh();
			}
		});
		formToolkit.adapt(btnMoveUp, true, true);
		btnMoveUp.setText("Move Up");
		btnMoveUp.setEnabled(false);
		
		btnMoveDown = new Button(buttons, SWT.NONE);
		btnMoveDown.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				
				int selected = tableViewer.getTable().getSelectionIndex();
				if(selected==-1) return;
				PolicyToolUIModel.getPolicyToolUIModel().getSelectorTypes().moveSelectorDown(selected);
				refresh();
				for(PolicyEditorView v : PolicyToolUIModel.getInstance().getPolicyEditorView()) v.refresh();
			}
		});
		formToolkit.adapt(btnMoveDown, true, true);
		btnMoveDown.setText("Move Down");
		btnMoveDown.setEnabled(false);
		
		refresh();
		for(PolicyEditorView v : PolicyToolUIModel.getInstance().getPolicyEditorView()) v.refresh();
	}

	@Override
	public void setFocus() {
		
	}
	
	public void refresh(){
		if(PolicyToolUIModel.getInstance().getPolicyToolModel()==null) return;
		
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return PolicyToolUIModel.getInstance().getSelectorTypes().getSelectorType((String)element).getClass().getName();
			}
		});
		
		tableViewer.setInput(PolicyToolUIModel.getInstance().getSelectorTypes().getSelectorNames());
		
		Boolean modelLoaded = PolicyToolUIModel.getInstance().getPolicyToolModel()!=null;
		System.out.println("Model Loaded = "+modelLoaded);
		btnAddSelectorType.setEnabled(modelLoaded);
		
		tableViewer.refresh();
		composite.layout();
	}
}