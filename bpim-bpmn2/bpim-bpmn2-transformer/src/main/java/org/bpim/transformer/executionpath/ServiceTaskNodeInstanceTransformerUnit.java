package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.AutomatedTask;
import org.bpim.model.execpath.v1.NormalTransition;
import org.bpim.model.execpath.v1.Start;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.ExecutionPathHelper;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.workflow.instance.NodeInstance;

public class ServiceTaskNodeInstanceTransformerUnit  extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance,
			TransformationResult transformationResult) {	
		AutomatedTask automatedTask = ExecutionPathHelper.createAutomatedTask(nodeInstance);		
		transformationResult.setExecPathActivity(automatedTask);
	}

}
