package org.bpim.transformer.executionpath;

import java.util.Map;

import org.bpim.model.execpath.v1.MessageTransition;
import org.bpim.model.execpath.v1.ReferenceProcessInstance;
import org.bpim.model.execpath.v1.Wait;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.ExecutionPathHelper;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;

public class ReceiveTaskNodeInstanceTransformerUnit extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		WorkItemNodeInstance workItemNodeInstance = (WorkItemNodeInstance) nodeInstance;
		Map<String, Object> results = workItemNodeInstance.getWorkItem().getResults();
		ReferenceProcessInstance referenceProcessInstance = null;
		if (results.containsKey("SourceProcessId")){
			referenceProcessInstance = ExecutionPathHelper.createReferenceProcessInstance(nodeInstance);			
		}
		
		if (!workItemNodeInstance.getNode().getIncomingConnections().isEmpty()){
			Wait wait = ExecutionPathHelper.createWaitWithMessageTransition(nodeInstance);
			if (referenceProcessInstance != null){
				wait.getOutputTransition().get(0).setTo(referenceProcessInstance);
			}
			transformationResult.setFlowNode(wait);
			
		}else{
			MessageTransition messageTransition = ExecutionPathHelper.createMessageTransition();
			if (referenceProcessInstance != null){
				messageTransition.setTo(referenceProcessInstance);
			}
			transformationResult.setFlowNode(messageTransition);
		}
	}

}
