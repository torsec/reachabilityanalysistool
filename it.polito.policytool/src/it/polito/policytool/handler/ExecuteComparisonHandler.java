/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package it.polito.policytool.handler;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.translation.semilattice.Semilattice;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.GenericRule;

import java.io.IOException;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.core.commands.AbstractHandler;

public class ExecuteComparisonHandler extends AbstractHandler{

	@Override
	  public Object execute(ExecutionEvent event) throws  ExecutionException{
		Shell shell = HandlerUtil.getActiveShell(event);
		FileDialog dialog = new FileDialog(shell,SWT.OPEN);
		if(dialog.open()==null) return null;
		
		
		
		PolicyAnalysisModel model = null;
		
		try {
			model = new PolicyAnalysisModel(dialog.getFilterPath()+"/"+dialog.getFileName(),dialog.getFileName());
			
			Policy mergedPolicy = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "");
			int i=0;
			for(FilteringZone z1 : model.getEquivalentFW_list().keySet()){
				for(FilteringZone z2 : model.getEquivalentFW_list().keySet()){
					Policy p =model.getEquivalentFW_list().get(z1).get(z2).getFMRPolicy();
					for(GenericRule rule:p.getRuleSet()){
						int ii = ((FMRResolutionStrategy)p.getResolutionStrategy()).getExternalData(rule);
						mergedPolicy.insertRule(rule, i+ii);
					}
					i=i+p.getRuleSet().size()+1;
				}
			}
			
			Set<Semilattice<GenericRule>> result = PolicyToolUIModel.getInstance().getPolicyToolModel().compare(mergedPolicy);
			
			PolicyToolUIModel.getPolicyToolUIModel().getComparisonResultView().setInput(result);
			PolicyToolUIModel.getInstance().getComparisonResultView().getViewSite().getPage().showView(PolicyToolUIModel.getInstance().getComparisonResultView().ID);
			PolicyToolUIModel.getInstance().getComparisonResultView().refresh();
			
		} catch (IOException e) {
			MessageBox message = new MessageBox(shell); 
			message.setMessage("Error in opening file IO");
			message.open();
			e.printStackTrace();
		} catch (NoExternalDataException e) {
			MessageBox message = new MessageBox(shell); 
			message.setMessage("Error in opening file External Data");
			message.open();
			e.printStackTrace();
		}catch (Exception e) {
			MessageBox message = new MessageBox(shell); 
			message.setMessage("Error in opening file Ex");
			message.open();
			e.printStackTrace();
		}
		
		
		return model;
			
		
		
//			logger.info("info test");
//			logger.error("error test");
//			logger.debug("debug test");
//			logger.warn("warn test");
		
	}
}
