package it.polito.policytool.ui.view.ProjectExplorer;


import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.view.ProjectExplorer.menu.AnalyzePolicyAction;
import it.polito.policytool.ui.view.ProjectExplorer.menu.DeletePolicyAction;
import it.polito.policytool.ui.view.ProjectExplorer.menu.ModifyPolicyAction;
import it.polito.policytool.ui.view.ProjectExplorer.menu.NewPolicyAction;
import it.polito.policytoollib.landscape.Landscape;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.utils.PolicyType;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;


public class ProjectExplorerView extends ViewPart{
	
	public static final String ID = "it.polito.policytool.ui.view.ProjectExplorer.ProjectExplorerView";

  
	  private TreeViewer treeViewer;

	  
	  public ProjectExplorerView(){
		  PolicyToolUIModel.getInstance().setProjectExplorerView(this);
	  }


	@Override
	public void createPartControl(final Composite parent) {
    	parent.setLayout(new FillLayout());
	    treeViewer = new TreeViewer(parent);
	    treeViewer.setContentProvider(new ProjectExplorerContentProvider());
	    treeViewer.setLabelProvider(new ProjectExplorerLabelProvider());
		
	    MenuManager menuMgr = new MenuManager();

        Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
        menuMgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                // IWorkbench wb = PlatformUI.getWorkbench();
                // IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
                if (treeViewer.getSelection().isEmpty()) {
                    return;
                }

                if (treeViewer.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                    Object object = selection.getFirstElement();
                    
                    if (object instanceof Policy) {
                        manager.add(new DeletePolicyAction((Policy) object, parent.getShell()));
                        manager.add(new ModifyPolicyAction((Policy)object));
                        if(((Policy)object).getPolicyType() == PolicyType.FILTERING)
                        	manager.add(new AnalyzePolicyAction((Policy)object));
                    }
                    
                    if (object instanceof NamedSet){
                    	manager.add(new NewPolicyAction(((NamedSet)object).getPolicyType()));
                    }
                }
                
            }
        });
        
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (treeViewer.getSelection().isEmpty()) {
                    return;
                }

                if (treeViewer.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                    Object object = selection.getFirstElement();
                    
                    if (object instanceof Policy) {
                        ModifyPolicyAction a = new ModifyPolicyAction((Policy) object);
                        a.runWithEvent(new Event());
                    }
                    
                    if (object instanceof NamedSet){
                    	if(!treeViewer.getExpandedState(object))
                    		treeViewer.expandToLevel(object, AbstractTreeViewer.ALL_LEVELS);
                    	else treeViewer.collapseToLevel(object, AbstractTreeViewer.ALL_LEVELS);
                    }
                    
                    if(object instanceof Landscape)
                    {
                    	try {
							PolicyToolUIModel.getInstance().getLandscapeEditorView().getViewSite().getPage().showView(PolicyToolUIModel.getInstance().getLandscapeEditorView().ID);
						} catch (PartInitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                    
                    if(object instanceof SelectorTypes)
                    {
                    	try {
							PolicyToolUIModel.getInstance().getSelectorTypesEditorView().getViewSite().getPage().showView(PolicyToolUIModel.getInstance().getSelectorTypesEditorView().ID);
						} catch (PartInitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
			}
		});
        
        menuMgr.setRemoveAllWhenShown(true);
        treeViewer.getControl().setMenu(menu);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
	  
	public void setInput(PolicyAnalysisModel model) {
		treeViewer.setInput(model);
	}
	 
}
