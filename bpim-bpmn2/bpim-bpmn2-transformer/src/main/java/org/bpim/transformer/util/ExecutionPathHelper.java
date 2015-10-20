package org.bpim.transformer.util;

import org.bpim.model.execpath.v1.AutomatedTask;
import org.bpim.model.execpath.v1.CallProcessInstance;
import org.bpim.model.execpath.v1.NormalTransition;
import org.bpim.model.execpath.v1.ObjectFactory;
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

}
