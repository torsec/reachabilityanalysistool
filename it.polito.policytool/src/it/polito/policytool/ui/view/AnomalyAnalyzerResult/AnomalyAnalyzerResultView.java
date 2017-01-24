package it.polito.policytool.ui.view.AnomalyAnalyzerResult;

import java.util.ArrayList;
import java.util.Set;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.rule.action.IPSecAction;
import it.polito.policytoollib.rule.impl.GenericRule;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

public class AnomalyAnalyzerResultView  extends ViewPart{

public static final String ID = "it.polito.policytool.ui.view.AnomalyAnalyzerResult.AnomalyAnalyzerResultView";
	
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	private Form form;
	
	private TableViewer tableViewer;
	
	private ResultComparator comparator;
	
	public AnomalyAnalyzerResultView() {
		PolicyToolUIModel.getInstance().setAnomalyAnalyzerResultView(this);
	}

	@Override
	public void createPartControl(Composite parent) {
		this.setPartName("Analyzer Results");
		if(this.getContentDescription().isEmpty()) this.setContentDescription("Execute an analysis to obtain the results!");
		
		form = formToolkit.createForm(parent);
		form.getBody().setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(form.getBody(), SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table_1 = tableViewer.getTable();
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.setContentProvider(new ArrayContentProvider());
		Menu headerMenu = new Menu(parent.getShell(), SWT.POP_UP);
		tableViewer.getTable().setMenu(headerMenu);
		
		comparator = new ResultComparator();
		tableViewer.setComparator(comparator);

		ColumnViewerToolTipSupport.enableFor(tableViewer); 
		
		TableViewerColumn tableViewerColumn_0 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName_0 = tableViewerColumn_0.getColumn();
		tableColumnLayout.setColumnData(tblclmnName_0, new ColumnWeightData(20, 100, true));
		tblclmnName_0.setText("Conflict");
		tblclmnName_0.addSelectionListener(getSelectionAdapter(tblclmnName_0, 0));
		
		tableViewerColumn_0.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((PolicyAnomaly)element).getConflict().toString();
			}
		});
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName_1 = tableViewerColumn_1.getColumn();
		tableColumnLayout.setColumnData(tblclmnName_1, new ColumnWeightData(20, 100, true));
		tblclmnName_1.setText("Rule 1");
		tblclmnName_1.addSelectionListener(getSelectionAdapter(tblclmnName_1, 1));
		
		tableViewerColumn_1.setLabelProvider(new CellLabelProvider() {			
			@Override
			  public void update(ViewerCell cell) {
			    cell.setText(((PolicyAnomaly) cell.getElement()).getRule_set()[0].getName());
			  }
			
			@Override
			  public String getToolTipText(Object element) {
				PolicyAnomaly a = (PolicyAnomaly) element;
				GenericRule r = a.getRule_set()[0];
				String ret = r.getName()+"\n\n";
				for(String s:r.getConditionClause().getSelectorsNames()){
					ret+=s+" : "+r.getConditionClause().get(s).toString()+"\n";
				}
				if(r.getAction() instanceof IPSecAction)
					ret+=r.getAction();
				else
					ret+="Action : "+r.getAction();
			    return ret;
			  }
			
			@Override
			  public Point getToolTipShift(Object object) {
			    return new Point(5, 5);
			  }

			  @Override
			  public int getToolTipDisplayDelayTime(Object object) {
			    return 100; //msec
			  }

			  @Override
			  public int getToolTipTimeDisplayed(Object object) {
			    return 5000; //msec
			  }
		});
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName_2 = tableViewerColumn_2.getColumn();
		tableColumnLayout.setColumnData(tblclmnName_2, new ColumnWeightData(20, 100, true));
		tblclmnName_2.setText("Rule 2");
		tblclmnName_2.addSelectionListener(getSelectionAdapter(tblclmnName_2, 2));
		
		tableViewerColumn_2.setLabelProvider(new CellLabelProvider() {			
			@Override
			  public void update(ViewerCell cell) {
			    cell.setText(((PolicyAnomaly) cell.getElement()).getRule_set()[0].getName());
			  }
			
			@Override
			  public String getToolTipText(Object element) {
				PolicyAnomaly a = (PolicyAnomaly) element;
				GenericRule r = a.getRule_set()[1];
				String ret = r.getName()+"\n\n";
				for(String s:r.getConditionClause().getSelectorsNames()){
					ret+=s+" : "+r.getConditionClause().get(s).toString()+"\n";
				}
				if(r.getAction() instanceof IPSecAction)
					ret+=r.getAction();
				else
					ret+="Action : "+r.getAction();
			    return ret;
			  }
			
			@Override
			  public Point getToolTipShift(Object object) {
			    return new Point(5, 5);
			  }

			  @Override
			  public int getToolTipDisplayDelayTime(Object object) {
			    return 100; //msec
			  }

			  @Override
			  public int getToolTipTimeDisplayed(Object object) {
			    return 5000; //msec
			  }
		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	public void setInput(Set<PolicyAnomaly> policyAnomalys, String policyOrZone1, String zone2){
		tableViewer.setInput(policyAnomalys);
		if(zone2==null)
			this.setContentDescription("Single analysis of policy "+policyOrZone1);
		else
			this.setContentDescription("Distributed analysis between zone "+policyOrZone1+" and zone "+zone2);
	}
	
	/**
	 * Clear input.
	 */
	public void clearInput() {
		tableViewer.setInput(new ArrayList<PolicyAnomaly>());
	}

	
	/**
	 * Gets the selection adapter.
	 * 
	 * @param column
	 *           the column
	 * @param index
	 *           the index
	 * @return the selection adapter
	 */
	private SelectionAdapter getSelectionAdapter(final TableColumn column,
		      final int index) {
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
	
}
