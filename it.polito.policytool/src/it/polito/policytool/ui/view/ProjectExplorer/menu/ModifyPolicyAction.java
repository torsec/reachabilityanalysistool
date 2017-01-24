package it.polito.policytool.ui.view.ProjectExplorer.menu;

import it.polito.policytool.ui.view.PolicyEditor.PolicyEditorInput;
import it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView;
import it.polito.policytoollib.policy.impl.Policy;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.EditorReference;

public class ModifyPolicyAction implements IAction {
	
	private Policy policy;
	
	public ModifyPolicyAction(Policy policy){
		this.policy = policy;
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getAccelerator() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getActionDefinitionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageDescriptor getDisabledImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HelpListener getHelpListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageDescriptor getHoverImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMenuCreator getMenuCreator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getText() {
		return "Modify";
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void runWithEvent(Event event) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		
		for(IEditorReference editorRef : page.getEditorReferences())
		{
			if(editorRef.getEditor(false).getEditorInput().getName().equals(policy.getName()+" ("+policy.getPolicyType()+")"))
			{
				page.activate(editorRef.getEditor(false));
				return;
			}
		}
		
		PolicyEditorInput input = new PolicyEditorInput(this.policy);
        try {
          page.openEditor(input, PolicyEditorView.ID);

        } catch (PartInitException e) {
          throw new RuntimeException(e);
        }
	}

	@Override
	public void setActionDefinitionId(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDescription(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisabledImageDescriptor(ImageDescriptor newImage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHelpListener(HelpListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHoverImageDescriptor(ImageDescriptor newImage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setImageDescriptor(ImageDescriptor newImage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMenuCreator(IMenuCreator creator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setToolTipText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAccelerator(int keycode) {
		// TODO Auto-generated method stub

	}

}
