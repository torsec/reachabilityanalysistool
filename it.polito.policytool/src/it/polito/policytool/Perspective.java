package it.polito.policytool;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import it.polito.policytool.ui.view.QueryResult.QueryResultView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
		layout.addView("it.polito.policytool.ui.view.ProjectExplorer.ProjectExplorerView", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		layout.getViewLayout("it.polito.policytool.ui.view.ProjectExplorer.ProjectExplorerView").setCloseable(false);
		
//		layout.addView("it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView", IPageLayout.RIGHT, 0.7f, IPageLayout.ID_EDITOR_AREA);
//		layout.getViewLayout("it.polito.policytool.ui.view.PolicyEditor.PolicyEditorView").setCloseable(false);
		
		IFolderLayout rightFolder = layout.createFolder("it.polito.policytool.rightfolder", IPageLayout.RIGHT, 0.8f, IPageLayout.ID_EDITOR_AREA);
		
		rightFolder.addView("it.polito.policytool.ui.view.LandscapeEditor.LandscapeEditorView");
		layout.getViewLayout("it.polito.policytool.ui.view.LandscapeEditor.LandscapeEditorView").setCloseable(false);
		
		rightFolder.addView("it.polito.policytool.ui.view.SelectorTypesEditor.SelectorTypesEditorView");
		layout.getViewLayout("it.polito.policytool.ui.view.SelectorTypesEditor.SelectorTypesEditorView").setCloseable(false);
		
		rightFolder.addView("it.polito.policytool.ui.view.AnomalyAnalyzerResult.AnomalyAnalyzerResultView");
		layout.getViewLayout("it.polito.policytool.ui.view.AnomalyAnalyzerResult.AnomalyAnalyzerResultView").setCloseable(false);
		
		rightFolder.addView("it.polito.policytool.ui.view.QueryResult.QueryResultView");
		layout.getViewLayout("it.polito.policytool.ui.view.QueryResult.QueryResultView").setCloseable(false);
		
		rightFolder.addView("it.polito.policytool.ui.view.ComparisonResult.ComparisonResultView");
		layout.getViewLayout("it.polito.policytool.ui.view.ComparisonResult.ComparisonResultView").setCloseable(false);
	}

}
