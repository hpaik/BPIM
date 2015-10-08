package org.bpim.engine;

import org.bpim.model.data.v1.DataSnapshotElement;
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
			}
			processInstance.getData().getDataSnapshotPool().getDataElement().addAll(
					transformationResult.getDataPoolElements());
		}		
	}

}
