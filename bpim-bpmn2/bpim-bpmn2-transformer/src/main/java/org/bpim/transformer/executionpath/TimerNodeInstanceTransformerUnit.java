package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.Wait;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.ExecutionPathHelper;
import org.jbpm.workflow.instance.NodeInstance;

public class TimerNodeInstanceTransformerUnit  extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		Wait wait = ExecutionPathHelper.createWaitWithEventTransition(nodeInstance);
		transformationResult.setFlowNode(wait);
	}

}
