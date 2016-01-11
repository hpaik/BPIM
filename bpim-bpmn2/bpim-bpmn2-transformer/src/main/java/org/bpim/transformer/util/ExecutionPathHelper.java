package org.bpim.transformer.util;

import java.util.Date;

import org.bpim.model.execpath.v1.AutomatedTask;
import org.bpim.model.execpath.v1.CallProcessInstance;
import org.bpim.model.execpath.v1.EventTransition;
import org.bpim.model.execpath.v1.ManualTask;
import org.bpim.model.execpath.v1.MessageTransition;
import org.bpim.model.execpath.v1.NormalTransition;
import org.bpim.model.execpath.v1.ObjectFactory;
import org.bpim.model.execpath.v1.ReferenceProcessInstance;
import org.bpim.model.execpath.v1.Wait;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance;

public class ExecutionPathHelper {
	
	public static AutomatedTask createAutomatedTask(NodeInstance nodeInstance){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		AutomatedTask automatedTask = executionPathObjectFactory.createAutomatedTask();
		automatedTask.setId(UniqueIdGenerator.nextId());
		automatedTask.setName(nodeInstance.getNodeName());
		NormalTransition normalTransition = executionPathObjectFactory.createNormalTransition();
		normalTransition.setId(UniqueIdGenerator.nextId());
		automatedTask.getOutputTransition().add(normalTransition);
		automatedTask.setExecutionDateTime((new Date()).toString());
		
		return automatedTask;
	}
	
	public static ManualTask createManualTask(NodeInstance nodeInstance){
		HumanTaskNodeInstance humanTaskNodeInstance = (HumanTaskNodeInstance) nodeInstance;
		Object role = humanTaskNodeInstance.getWorkItem().getParameter("GroupId");
		Object userId = humanTaskNodeInstance.getWorkItem().getParameter("ActorId");
		Object userName = humanTaskNodeInstance.getWorkItem().getParameter("Content");
		Object comment = humanTaskNodeInstance.getWorkItem().getParameter("Comment");
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		ManualTask manualTask = executionPathObjectFactory.createManualTask();
		manualTask.setId(UniqueIdGenerator.nextId());
		manualTask.setName(nodeInstance.getNodeName());
		if (role != null)
			manualTask.setRole(role.toString());
		if (userId != null)
			manualTask.setUserId(userId.toString());
		if (userName != null)
			manualTask.setUserName(userName.toString());
		if (comment != null)
			manualTask.setComments(comment.toString());
		
		NormalTransition normalTransition = executionPathObjectFactory.createNormalTransition();
		normalTransition.setId(UniqueIdGenerator.nextId());
		manualTask.getOutputTransition().add(normalTransition);
		manualTask.setExecutionDateTime((new Date()).toString());
		
		return manualTask;
	}
	 
	public static CallProcessInstance createCallProcessInstance(NodeInstance nodeInstance){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		CallProcessInstance callProcessInstance = executionPathObjectFactory.createCallProcessInstance();
		callProcessInstance.setId(UniqueIdGenerator.nextId());
		callProcessInstance.setName(nodeInstance.getNodeName());
		callProcessInstance.setExecutionDateTime((new Date()).toString());
		
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
	
	public static NormalTransition createNormalTransition(){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		NormalTransition normalTransition = executionPathObjectFactory.createNormalTransition();
		normalTransition.setId(UniqueIdGenerator.nextId());
		return normalTransition;
	}
	
	public static EventTransition createEventTransition(){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		EventTransition eventTransition = executionPathObjectFactory.createEventTransition();
		eventTransition.setId(UniqueIdGenerator.nextId());
		return eventTransition;
	}
	
	public static ReferenceProcessInstance createReferenceProcessInstance(NodeInstance nodeInstance){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		ReferenceProcessInstance referenceProcessInstance = executionPathObjectFactory.createReferenceProcessInstance();
		referenceProcessInstance.setId(UniqueIdGenerator.nextId());
		referenceProcessInstance.setName(nodeInstance.getNodeName());
		referenceProcessInstance.setExecutionDateTime((new Date()).toString());
		
		NormalTransition normalTransition = createNormalTransition();
		referenceProcessInstance.getOutputTransition().add(normalTransition);
		return referenceProcessInstance;
	}
	
	public static Wait createWaitWithMessageTransition(NodeInstance nodeInstance){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		Wait wait = executionPathObjectFactory.createWait();
		wait.setId(UniqueIdGenerator.nextId());
		wait.setName("Wait");
		wait.setExecutionDateTime((new Date()).toString());
		
		MessageTransition messageTransition = executionPathObjectFactory.createMessageTransition();
		messageTransition.setId(UniqueIdGenerator.nextId());
		wait.getOutputTransition().add(messageTransition);
		return wait;
	}
	
	public static Wait createWaitWithEventTransition(NodeInstance nodeInstance){
		org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = new ObjectFactory();
		Wait wait = executionPathObjectFactory.createWait();
		wait.setId(UniqueIdGenerator.nextId());
		wait.setName("Wait");
		wait.setExecutionDateTime((new Date()).toString());
		
		EventTransition eventTransition = executionPathObjectFactory.createEventTransition();
		eventTransition.setId(UniqueIdGenerator.nextId());
		wait.getOutputTransition().add(eventTransition);
		return wait;
	}

}
