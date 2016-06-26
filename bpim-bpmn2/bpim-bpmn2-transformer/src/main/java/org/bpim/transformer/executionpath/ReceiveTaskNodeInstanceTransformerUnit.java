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
		//Checks if message was sent from another process instance
		if (results.containsKey("SourceProcessId")){
			//Creates a Reference Process Instance element
			referenceProcessInstance = ExecutionPathHelper.createReferenceProcessInstance(nodeInstance);			
		}
		//Checks if Receive Task is part of execution flow (i.e., Execution engine has reached this node and waiting for a message)
		if (!workItemNodeInstance.getNode().getIncomingConnections().isEmpty()){
			// Creates a Wait element with Message Transition
			Wait wait = ExecutionPathHelper.createWaitWithMessageTransition(nodeInstance);
			if (referenceProcessInstance != null){
				// Sets the target for Wait elelement's Message Transition to Reference Process Instance 
				wait.getOutputTransition().get(0).setTo(referenceProcessInstance);
			}
			transformationResult.setFlowNode(wait);
			
		}else{
			//It is not part of the main flow and transforms to a Message Transition
			MessageTransition messageTransition = ExecutionPathHelper.createMessageTransition();
			if (referenceProcessInstance != null){
				// Sets the target for Message Transition to Reference Process Instance Node 
				messageTransition.setTo(referenceProcessInstance);
			}
			transformationResult.setFlowNode(messageTransition);
		}
		
		
	}

}
