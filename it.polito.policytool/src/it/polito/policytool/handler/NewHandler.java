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
import it.polito.policytoollib.model.PolicyAnalysisModel;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.AbstractHandler;

public class NewHandler extends AbstractHandler{

	@Override
	  public Object execute(ExecutionEvent event) throws  ExecutionException{
		
		
		PolicyAnalysisModel model = new PolicyAnalysisModel();
		
		
		PolicyToolUIModel.getInstance().setPolicyToolModel(model);
		
		
		
		return model;
			
		
		
//			logger.info("info test");
//			logger.error("error test");
//			logger.debug("debug test");
//			logger.warn("warn test");
		
	}
}
