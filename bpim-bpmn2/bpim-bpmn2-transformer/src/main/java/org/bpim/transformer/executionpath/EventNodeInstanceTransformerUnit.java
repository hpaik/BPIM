package org.bpim.transformer.executionpath;

import java.util.Map;

import org.bpim.model.execpath.v1.EventTransition;
import org.bpim.model.execpath.v1.MessageTransition;
import org.bpim.model.execpath.v1.ReferenceProcessInstance;
import org.bpim.model.execpath.v1.Wait;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.ExecutionPathHelper;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.node.EventNodeInstance;

public class EventNodeInstanceTransformerUnit  extends TransformerUnit {
	
	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {		
		EventNodeInstance eventNodeInstance = (EventNodeInstance) nodeInstance;
		Object sourceProcessId = eventNodeInstance.getVariable("SourceProcessId");
		ReferenceProcessInstance referenceProcessInstance = null;
		if (sourceProcessId != null){
			referenceProcessInstance = ExecutionPathHelper.createReferenceProcessInstance(nodeInstance);			
		}
		
		if (!eventNodeInstance.getNode().getIncomingConnections().isEmpty()){
			Wait wait = ExecutionPathHelper.createWaitWithEventTransition(nodeInstance);
			if (referenceProcessInstance != null){
				wait.getOutputTransition().get(0).setTo(referenceProcessInstance);
			}
			transformationResult.setFlowNode(wait);
			
		}else{
			EventTransition eventTransition = ExecutionPathHelper.createEventTransition();
			if (referenceProcessInstance != null){
				eventTransition.setTo(referenceProcessInstance);
			}
			transformationResult.setFlowNode(eventTransition);
		}
	}

}
