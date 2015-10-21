package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.ReferenceProcessInstance;
import org.bpim.model.execpath.v1.Wait;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.ExecutionPathHelper;
import org.jbpm.workflow.instance.NodeInstance;

public class ReceiveTaskNodeInstanceTransformerUnit extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		ReferenceProcessInstance referenceProcessInstance = ExecutionPathHelper.createReferenceProcessInstance(nodeInstance);
		Wait wait = ExecutionPathHelper.createWait(nodeInstance);
		wait.getOutputTransition().get(0).setTo(referenceProcessInstance);
		transformationResult.setExecPathActivity(wait);
	}

}
