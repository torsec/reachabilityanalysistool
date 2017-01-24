package it.polito.policytool.ui.view.QueryResult;

import java.util.HashMap;
import java.util.HashSet;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView;
import it.polito.policytool.ui.view.PolicyEditor.PolicyViewComparator;
import it.polito.policytool.ui.view.PolicyEditor.SelectorColumnLabelProvider;
import it.polito.policytool.ui.view.QueryResult.ResultComparator;
import it.polito.policytool.ui.wizard.ModifyRule.ModifyRuleWizard;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

public class QueryResultView  extends ViewPart{
	
	public static final String ID = "it.polito.policytool.ui.view.QueryResult.QueryResultView";
	
	private HashSet<GenericRule> result;
	
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	private Form form;
	
	private TableViewer tableViewer;
	
	private ResultComparator comparator;
	private QueryResultView		thisView;

	private Composite				parent;
	
	public QueryResultView() {
		PolicyToolUIModel.getInstance().setQueryResultView(this);
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
		
		this.setPartName("Reachability Results");
		if(this.getContentDescription().isEmpty()) this.setContentDescription("Execute an analysis to obtain the results!");
		//createControl();
	}
	
	public void createControl(){
		
		
		form = formToolkit.createForm(parent);
		form.getBody().setLayout(new GridLayout(2, false));
		
		Composite composite = new Composite(form.getBody(), SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		final Table table_1 = tableViewer.getTable();
	 
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.setContentProvider(new ArrayContentProvider());
		Menu headerMenu = new Menu(parent.getShell(), SWT.POP_UP);
		tableViewer.getTable().setMenu(headerMenu);
		comparator = new ResultComparator();
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
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
	
	public void refresh(){
		if(PolicyToolUIModel.getInstance().getPolicyToolModel()==null) return;
		form.dispose();
		setInput(result);
		parent.layout();
		parent.getShell().layout(true);
	}
	
	public void refreshParent(){
		parent.layout();
		parent.getShell().layout(true);
	}
	
	public void setInput(HashSet<GenericRule> result) {
		createControl();
		this.result = result;
		tableViewer.setInput(result);
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
	
//	public void setInput(SelectorTypes selectorTypes){
//		this.selectorTypes = selectorTypes;
//		tableViewer.setInput(selectorTypes.getSelectorNames());
//		
//		btnAddSelectorType.setEnabled(true);
//	}

	
}
