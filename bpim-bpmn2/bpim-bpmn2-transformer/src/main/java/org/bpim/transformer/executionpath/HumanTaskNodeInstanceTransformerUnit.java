package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.AutomatedTask;
import org.bpim.model.execpath.v1.ManualTask;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.ExecutionPathHelper;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance;

public class HumanTaskNodeInstanceTransformerUnit  extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		
		ManualTask manualTask = ExecutionPathHelper.createManualTask(nodeInstance);		
		transformationResult.setFlowNode(manualTask);
	}

}
