package org.bpim.engine;

import java.util.HashMap;
import java.util.Map;

import org.bpim.model.v1.ObjectFactory;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.util.UniqueIdGenerator;

public class ExecutionContext {
	private Map<String, ProcessInstanceContext> processInstances = null;
	
	
	public ExecutionContext(){
		processInstances = new HashMap<String, ProcessInstanceContext>();
	}
	
	public ProcessInstanceContext getProcessInstanceContext(String bpmnInstanceId, String bpmnInstanceName){
		ProcessInstanceContext processInstanceContext = null;
		
		if(processInstances.containsKey(bpmnInstanceId)){
			processInstanceContext = processInstances.get(bpmnInstanceId);
		}else{
			processInstanceContext = new ProcessInstanceContext();
			org.bpim.model.v1.ObjectFactory objectFatory = new ObjectFactory();
			ProcessInstance processInstance = objectFatory.createProcessInstance();
			processInstance.setId(UniqueIdGenerator.nextId());
			processInstance.setName(bpmnInstanceName);
			processInstanceContext.setProcessInstance(processInstance);
			
			processInstances.put(bpmnInstanceId, processInstanceContext);			
		}
		return processInstanceContext;
	}

}
