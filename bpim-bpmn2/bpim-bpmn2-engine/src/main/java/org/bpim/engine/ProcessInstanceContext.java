package org.bpim.engine;

import java.util.List;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.model.execpath.v1.FlowNode;
import org.bpim.model.execpath.v1.Start;
import org.bpim.model.v1.ObjectFactory;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.base.TransformationResult;

public class ProcessInstanceContext {
	
	private ProcessInstance processInstance = null;
	private org.bpim.model.v1.ObjectFactory objectFatory = null;
	private org.bpim.model.data.v1.ObjectFactory dataObjectFatory = null;
	private FlowNode currentExecPathNode = null;
	
	public ProcessInstanceContext(){
		objectFatory = new org.bpim.model.v1.ObjectFactory();
		dataObjectFatory = new org.bpim.model.data.v1.ObjectFactory();
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
		if (processInstance.getData() == null){
			processInstance.setData(objectFatory.createData());
			processInstance.getData().setDataSnapshotGraphs(dataObjectFatory.createDataSnapshotGraphs());
			processInstance.getData().setDataSnapshotPool(dataObjectFatory.createDataSnapshotPool());
		}
		if (processInstance.getExecutionPath() == null){
			processInstance.setExecutionPath(objectFatory.createExecutionPath());
		}
	}
	
	public void addTransformationResult(TransformationResult transformationResult){
		if (currentExecPathNode == null){
			processInstance.getExecutionPath().setStart((Start) 
					transformationResult.getExecPathActivity());
			currentExecPathNode = transformationResult.getExecPathActivity();
		}
		
		if (!transformationResult.getDataPoolElements().isEmpty()){
			
			if (processInstance.getData().getDataSnapshotPool().getDataElement().isEmpty()){
				
				processInstance.getData().getDataSnapshotGraphs().setDataSnapshotElement(
						transformationResult.getInputData());
			}else{
				//getDataSnapshotElement
			}
			processInstance.getData().getDataSnapshotPool().getDataElement().addAll(
					transformationResult.getDataPoolElements());
		}
	}
	
	public DataSnapshotElement getDataSnapshotElement(String objectId){
		DataSnapshotElement dataSnapshotElement = null;
		DataPoolElement result = null;
		for (DataPoolElement dataPoolElement : processInstance.getData().getDataSnapshotPool().getDataElement()){
			if (dataPoolElement.getMappingCorrelationId() != null && dataPoolElement.getMappingCorrelationId().equals(objectId)){
				if (result !=  null && (result.getVersion() >= dataPoolElement.getVersion())){
					continue;
				} 
				result = dataPoolElement;				
			}
		}
		DataSnapshotElement root = processInstance.getData().getDataSnapshotGraphs().getDataSnapshotElement();
		if (root != null){
			if (root.getDataPoolElementId().equals(result.getId())){
				return root;
			}
			List<DataTransition> dataTransitions = root.getDataTransition();
			while (dataTransitions != null){
				for (DataTransition dataTransition: dataTransitions){
					
				}
				
			}
		}
		//processInstance.getData().dataSnapshotElement.getDataTransition().
		return dataSnapshotElement;
	}

}
