package org.bpim.transformer.util;

import org.bpim.model.execpath.v1.AutomatedTask;
import org.bpim.model.execpath.v1.CallProcessInstance;
import org.bpim.model.execpath.v1.MessageTransition;
import org.bpim.model.execpath.v1.NormalTransition;
import org.bpim.model.execpath.v1.ObjectFactory;
import org.bpim.model.execpath.v1.ReferenceProcessInstance;
import org.bpim.model.execpath.v1.Wait;
import org.jbpm.workflow.instance.NodeInstance;

public class ExecutionPathHelper {
	
	public static AutomatedTask createAutomatedTask(NodeInstance nodeInstance){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		AutomatedTask automatedTask = executionPathObjectFactory.createAutomatedTask();
		automatedTask.setId(UniqueIdGenerator.nextId());
		automatedTask.setName(nodeInstance.getNodeName());
		NormalTransition normalTransition = executionPathObjectFactory.createNormalTransition();
		normalTransition.setId(UniqueIdGenerator.nextId());
		automatedTask.getOutputTransition().add(normalTransition);
		return automatedTask;
	}
	 
	public static CallProcessInstance createCallProcessInstance(NodeInstance nodeInstance){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		CallProcessInstance callProcessInstance = executionPathObjectFactory.createCallProcessInstance();
		callProcessInstance.setId(UniqueIdGenerator.nextId());
		callProcessInstance.setName(nodeInstance.getNodeName());
		NormalTransition normalTransition = executionPathObjectFactory.createNormalTransition();
		normalTransition.setId(UniqueIdGenerator.nextId());
		callProcessInstance.getOutputTransition().add(normalTransition);
		return callProcessInstance;
	}
	
	public static MessageTransition createMessageTransition(){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		MessageTransition messageTransition = executionPathObjectFactory.createMessageTransition();
		messageTransition.setId(UniqueIdGenerator.nextId());
		return messageTransition;
	}
	
	public static ReferenceProcessInstance createReferenceProcessInstance(NodeInstance nodeInstance){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		ReferenceProcessInstance referenceProcessInstance = executionPathObjectFactory.createReferenceProcessInstance();
		referenceProcessInstance.setId(UniqueIdGenerator.nextId());
		referenceProcessInstance.setName(nodeInstance.getNodeName());
		NormalTransition normalTransition = executionPathObjectFactory.createNormalTransition();
		normalTransition.setId(UniqueIdGenerator.nextId());
		referenceProcessInstance.getOutputTransition().add(normalTransition);
		return referenceProcessInstance;
	}
	
	public static Wait createWait(NodeInstance nodeInstance){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		Wait wait = executionPathObjectFactory.createWait();
		wait.setId(UniqueIdGenerator.nextId());
		wait.setName("Wait");
		MessageTransition messageTransition = executionPathObjectFactory.createMessageTransition();
		messageTransition.setId(UniqueIdGenerator.nextId());
		wait.getOutputTransition().add(messageTransition);
		return wait;
	}

}
