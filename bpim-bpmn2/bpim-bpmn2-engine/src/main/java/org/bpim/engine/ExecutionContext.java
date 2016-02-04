package org.bpim.engine;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.bpim.model.base.v1.ElementBase;
import org.bpim.model.base.v1.MetaDataBase;
import org.bpim.model.base.v1.Server;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.model.execpath.v1.Activity;
import org.bpim.model.execpath.v1.Start;
import org.bpim.model.execpath.v1.TransitionBase;
import org.bpim.model.v1.ObjectFactory;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.util.MetaDataHelper;
import org.bpim.transformer.util.UniqueIdGenerator;

public class ExecutionContext {
	private static Map<String, ProcessInstanceContext> processInstances = null;
	
//	private static CompositeProcessInstance compositeProcessInstance;
	
	
	public ExecutionContext(){
		if (processInstances == null){
			processInstances = new HashMap<String, ProcessInstanceContext>();
		}
	}
	
//	public CompositeProcessInstance startCompositeProcessInstance(String name){
//		org.bpim.model.v1.ObjectFactory objectFatory = new ObjectFactory();
//		compositeProcessInstance = objectFatory.createCompositeProcessInstance();
//		compositeProcessInstance.setName(name);
//		compositeProcessInstance.setId(UniqueIdGenerator.nextId());
//		org.bpim.model.data.v1.ObjectFactory dataObjectFatory = new org.bpim.model.data.v1.ObjectFactory();
//		compositeProcessInstance.setDataSnapshotPool(dataObjectFatory.createDataSnapshotPool());
//		return compositeProcessInstance;
//	}
	
	public ProcessInstanceContext getProcessInstanceContext(String bpmnInstanceId, String bpmnInstanceName){
		ProcessInstanceContext processInstanceContext = null;
		
		if(processInstances.containsKey(bpmnInstanceId)){
			processInstanceContext = getProcessInstanceContext(bpmnInstanceId);
		}else{
			processInstanceContext = new ProcessInstanceContext(this);
			org.bpim.model.v1.ObjectFactory objectFatory = new ObjectFactory();
			ProcessInstance processInstance = objectFatory.createProcessInstance();
			processInstance.setId(UniqueIdGenerator.nextId());
			processInstance.setName(bpmnInstanceName);
			processInstance.setMappingCorrelationId(bpmnInstanceId);
			processInstance.setServer(MetaDataHelper.createServer());
			processInstance.setCreationDateTime((new Date()).toString());
			processInstance.setState("STARTED");
			processInstanceContext.setProcessInstance(processInstance);
//			if (compositeProcessInstance != null){
//				compositeProcessInstance.getProcessInstance().add(processInstance);
//				processInstanceContext.setCompositeProcessInstance(compositeProcessInstance);
//			}
			processInstances.put(bpmnInstanceId, processInstanceContext);			
		}
		return processInstanceContext;
	}
	
	public ProcessInstanceContext getProcessInstanceContext(String bpmnInstanceId){
		return processInstances.get(bpmnInstanceId);		
	}
	
}
