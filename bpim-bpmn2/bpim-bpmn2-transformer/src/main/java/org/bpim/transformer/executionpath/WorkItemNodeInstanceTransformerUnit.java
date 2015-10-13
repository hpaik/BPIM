package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.AutomatedTask;
import org.bpim.model.execpath.v1.NormalTransition;
import org.bpim.model.execpath.v1.Start;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.workflow.instance.NodeInstance;

public class WorkItemNodeInstanceTransformerUnit  extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance,
			TransformationResult transformationResult) {	
		AutomatedTask automatedTask = executionPathObjectFactory.createAutomatedTask();
		automatedTask.setId(UniqueIdGenerator.nextId());
		automatedTask.setName(nodeInstance.getNodeName());
		NormalTransition normalTransition = executionPathObjectFactory.createNormalTransition();
		normalTransition.setId(UniqueIdGenerator.nextId());
		automatedTask.getOutputTransition().add(normalTransition);
		transformationResult.setExecPathActivity(automatedTask);
	}

}
