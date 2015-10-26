package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.CallProcessInstance;
import org.bpim.model.execpath.v1.MessageTransition;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.ExecutionPathHelper;
import org.jbpm.workflow.instance.NodeInstance;

public class SendTaskNodeInstanceTransformerUnit   extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance,
			TransformationResult transformationResult) {
		MessageTransition messageTransition = ExecutionPathHelper.createMessageTransition();		
		CallProcessInstance callProcessInstance = ExecutionPathHelper.createCallProcessInstance(nodeInstance);
		messageTransition.setTo(callProcessInstance);
		transformationResult.setFlowNode(messageTransition);
	}

}
