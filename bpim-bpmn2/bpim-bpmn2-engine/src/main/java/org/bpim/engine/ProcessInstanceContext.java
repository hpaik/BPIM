package org.bpim.engine;

import java.util.List;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.model.execpath.v1.Activity;
import org.bpim.model.execpath.v1.Start;
import org.bpim.model.execpath.v1.TransitionBase;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.util.DataPoolElementHelper;

public class ProcessInstanceContext {
	
	private ProcessInstance processInstance = null;
	private org.bpim.model.v1.ObjectFactory objectFatory = null;
	private org.bpim.model.data.v1.ObjectFactory dataObjectFatory = null;
	private Activity currentExecPathActivity = null;
	
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
	
	public DataPoolElement getDataPoolElementByType(String objectType){
		DataPoolElement dataPoolElement = null;
		for (DataPoolElement tmpDataPoolElement : processInstance.getData().getDataSnapshotPool().getDataElement()){			
				if (objectType.equals(tmpDataPoolElement.getDataObjectType())){
					if (dataPoolElement != null && dataPoolElement.getVersion() >= tmpDataPoolElement.getVersion()){
						continue;
					}
					dataPoolElement = tmpDataPoolElement;
				} 											
		}
		return dataPoolElement;
	}
	
 	public void addTransformationResult(TransformationResult transformationResult){
 		if (transformationResult.getFlowNode() instanceof TransitionBase){
 			currentExecPathActivity.getOutputTransition().clear();
 			currentExecPathActivity.getOutputTransition().add(
 					(TransitionBase)transformationResult.getFlowNode());
 			
 			transformationResult.setFlowNode(((TransitionBase)transformationResult.getFlowNode()).getTo());
 		}
 			
		if (currentExecPathActivity == null){
			processInstance.getExecutionPath().setStart((Start) 
					transformationResult.getFlowNode());
			currentExecPathActivity = (Activity) transformationResult.getFlowNode();
		}else {
			
			for (TransitionBase outputTransition: currentExecPathActivity.getOutputTransition()){
				outputTransition.setTo((Activity) transformationResult.getFlowNode());
			}
			
			if(transformationResult.getFlowNode() != null){
				currentExecPathActivity = (Activity) transformationResult.getFlowNode();
				List<TransitionBase> outputTransitions = null; 
				while (true){
					outputTransitions = currentExecPathActivity.getOutputTransition();
					if (!outputTransitions.isEmpty() && 
							currentExecPathActivity.getOutputTransition().get(0).getTo() != null){
						currentExecPathActivity = ((Activity)transformationResult.getFlowNode()).getOutputTransition().get(0).getTo();
					}else{
						break;
					}					
				}
			}			
		}		
		
		if (processInstance.getData().getDataSnapshotPool().getDataElement().isEmpty() && 
				!transformationResult.getDataPoolElements().isEmpty()){			
			processInstance.getData().getDataSnapshotGraphs().setDataSnapshotElement(
					transformationResult.getSourceDataSnapshotElement().get(0));
			
		}else{
			DataSnapshotElement sourceDataSnapshotElement = null;
			for (DataSnapshotElement tmpDataSnapshotElement: transformationResult.getSourceDataSnapshotElement()){
				sourceDataSnapshotElement = getDataSnapshotElement(tmpDataSnapshotElement.getMappingCorrelationId());
				sourceDataSnapshotElement.getDataTransition().addAll(tmpDataSnapshotElement.getDataTransition());
			}				
		}
		
		for (DataPoolElement dataPoolElement: transformationResult.getDataPoolElements()){
			DataPoolElementHelper.addToPool(dataPoolElement, processInstance);
		}
		
	}
 	
	private DataPoolElement getDataPoolElement(String objectId){
		DataPoolElement dataPoolElement = null;
		for (DataPoolElement tmpDataPoolElement : processInstance.getData().getDataSnapshotPool().getDataElement()){
			if (tmpDataPoolElement.getMappingCorrelationId() != null && tmpDataPoolElement.getMappingCorrelationId().equals(objectId)){
				if (dataPoolElement !=  null 
						&& dataPoolElement.getDataObjectType().equals(tmpDataPoolElement.getDataObjectType())
						&&  (dataPoolElement.getVersion() >= tmpDataPoolElement.getVersion())){
					continue;
				} 
				dataPoolElement = tmpDataPoolElement;				
			}
		}
		return dataPoolElement;
	}
	private DataSnapshotElement getDataSnapshotElement(String objectId){
		DataSnapshotElement dataSnapshotElement = null;
		DataPoolElement dataPoolElement = getDataPoolElement(objectId);

		DataSnapshotElement root = processInstance.getData().getDataSnapshotGraphs().getDataSnapshotElement();
		if (root != null && dataPoolElement != null){
			if (!root.isEmpty() && root.getDataPoolElementId().equals(dataPoolElement.getId())){
				return root;
			}else{
				dataSnapshotElement = getDataSnapshotElement(root.getDataTransition(), dataPoolElement.getId());
			}			
		}
		return dataSnapshotElement;
	}	
	private DataSnapshotElement getDataSnapshotElement(List<DataTransition> dataTransitions, String dataPoolElementId){
		DataSnapshotElement dataSnapshotElement = null;
		for (DataTransition dTransition: dataTransitions){
			if (!dTransition.getDataSnapshotElement().isEmpty() && 
					dTransition.getDataSnapshotElement().getDataPoolElementId().equals(dataPoolElementId)){
				dataSnapshotElement = dTransition.getDataSnapshotElement();
				break;
			}else{
				if (!dTransition.getDataSnapshotElement().getDataTransition().isEmpty()){
					dataSnapshotElement = getDataSnapshotElement(dTransition.getDataSnapshotElement().getDataTransition(), dataPoolElementId);
					if (dataSnapshotElement != null){
						break;
					}
				}
			}			
		}
		return dataSnapshotElement;
	}
}
