package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.EventTransition;
import org.bpim.model.execpath.v1.NormalTransition;
import org.bpim.model.execpath.v1.ReferenceProcessInstance;
import org.bpim.model.execpath.v1.Start;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.ExecutionPathHelper;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.workflow.instance.NodeInstance;


public class StartNodeInstanceTransformerUnit extends TransformerUnit {
	
	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		Start  start = executionPathObjectFactory.createStart();
		start.setId(UniqueIdGenerator.nextId());
		start.setName(nodeInstance.getNodeName());
		EventTransition eventTransition = ExecutionPathHelper.createEventTransition();
		eventTransition.setId(UniqueIdGenerator.nextId());
		start.getOutputTransition().add(eventTransition);
		transformationResult.setFlowNode(start);
		
		long parentProcessId = nodeInstance.getProcessInstance().getParentProcessInstanceId();
		if(parentProcessId != 0){
		   RuleFlowProcessInstance rpi = (RuleFlowProcessInstance)nodeInstance.getProcessInstance();
		   String parentProcessName =  rpi.getKnowledgeRuntime().getProcessInstance(parentProcessId).getProcessName();
		   transformationResult.getCorelatedProcessInstances().add(parentProcessId);
		   ReferenceProcessInstance referenceProcessInstance = ExecutionPathHelper.createReferenceProcessInstance(nodeInstance);
		   referenceProcessInstance.setName(parentProcessName);
		   eventTransition.setTo(referenceProcessInstance);
		}
		
		
	}

}
