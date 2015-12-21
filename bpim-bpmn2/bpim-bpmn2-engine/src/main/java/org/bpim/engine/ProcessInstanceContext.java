package org.bpim.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataSnapshotPool;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.model.execpath.v1.Activity;
import org.bpim.model.execpath.v1.End;
import org.bpim.model.execpath.v1.Start;
import org.bpim.model.execpath.v1.TransitionBase;
import org.bpim.model.v1.CompositeProcessInstance;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.util.DataPoolElementHelper;
import org.bpim.transformer.util.DataSnapshotElementHelper;

public class ProcessInstanceContext {
	
	private ProcessInstance processInstance = null;
	private CompositeProcessInstance compositeProcessInstance = null;
	private org.bpim.model.v1.ObjectFactory objectFatory = null;
	private org.bpim.model.data.v1.ObjectFactory dataObjectFatory = null;
	private Activity currentExecPathActivity = null;
	private List<Long> corelatedProcessInstances = null;
	private ExecutionContext executionContext = null;
	
	public ProcessInstanceContext(ExecutionContext executionContext){
		objectFatory = new org.bpim.model.v1.ObjectFactory();
		dataObjectFatory = new org.bpim.model.data.v1.ObjectFactory();
		this.executionContext = executionContext;
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
		for (DataPoolElement tmpDataPoolElement : getReadOnlyDataSnapshotPool().getDataElement()){			
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
 		
 		if(!transformationResult.getCorelatedProcessInstances().isEmpty()){
 			for(Long processId : transformationResult.getCorelatedProcessInstances()){
 				if(!getCorelatedProcessInstances().contains(processId)){
 					getCorelatedProcessInstances().add(processId);
 				}
 			}
 		}
 		
 		if (transformationResult.getFlowNode() instanceof TransitionBase){
 			currentExecPathActivity.getOutputTransition().clear();
 			currentExecPathActivity.getOutputTransition().add(
 					(TransitionBase)transformationResult.getFlowNode());
 			
 			transformationResult.setFlowNode(((TransitionBase)transformationResult.getFlowNode()).getTo());
 		}
 		
 		if (transformationResult.getFlowNode() instanceof Start && currentExecPathActivity != null){
 			return;
 		}
 		
 		if (transformationResult.getFlowNode() instanceof End){
 			processInstance.setState("FINISHED");
 			processInstance.setEndDateTime((new Date()).toString());
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
		
		DataSnapshotElement sourceDataSnapshotElement = null;
		DataPoolElement dataPoolElement = null;
		DataSnapshotElement targetDataSnapshotElement = null;
		for (DataSnapshotElement tmpDataSnapshotElement: transformationResult.getSourceDataSnapshotElement()){
			if (!tmpDataSnapshotElement.isEmpty()){
				dataPoolElement = getDataPoolElement(tmpDataSnapshotElement.getMappingCorrelationId());
				sourceDataSnapshotElement = getDataSnapshotElement(dataPoolElement);
				if (sourceDataSnapshotElement == null){
					sourceDataSnapshotElement = DataSnapshotElementHelper.create(dataPoolElement);
					processInstance.getData().getDataSnapshotGraphs().getDataSnapshotElement().add(sourceDataSnapshotElement);	
				}
				
				for (DataTransition dataTransition : tmpDataSnapshotElement.getDataTransition()){
					targetDataSnapshotElement = getDataSnapshotElement(dataTransition.getDataSnapshotElement().getDataPoolElementId());
					if (targetDataSnapshotElement != null){
						dataTransition.setDataSnapshotElement(targetDataSnapshotElement); 
					}
				}
				sourceDataSnapshotElement.getDataTransition().addAll(tmpDataSnapshotElement.getDataTransition());
			}else{
				processInstance.getData().getDataSnapshotGraphs().getDataSnapshotElement().add(transformationResult.getSourceDataSnapshotElement().get(0));
			}
		}		
		
		for (DataPoolElement tmpDataPoolElement: transformationResult.getDataPoolElements()){
			DataPoolElementHelper.addToPool(tmpDataPoolElement, getEditableDataSnapshotPool());
		}
		
		for (DataSnapshotElement tmpDataSnapshotElement: transformationResult.getSourceDataSnapshotElement()){
			for (DataTransition dataTransition : tmpDataSnapshotElement.getDataTransition()){
				for (DataPoolElement tmpDataPoolElement: transformationResult.getDataPoolElements()){
					if(dataTransition.getDataSnapshotElement() != null && 
							dataTransition.getDataSnapshotElement().getDataPoolElementId() != null &&
							tmpDataPoolElement.getId().equals(dataTransition.getDataSnapshotElement().getDataPoolElementId())){
						
						dataTransition.getDataSnapshotElement().setName(tmpDataPoolElement.getName());
//						tmpDataPoolElement.setCreationDateTime(tmpDataPoolElement.getCreationDateTime());
					}
				}
			}
		}
		
	}
 	
	private DataPoolElement getDataPoolElement(String objectId){
		DataPoolElement dataPoolElement = null;
		for (DataPoolElement tmpDataPoolElement : getReadOnlyDataSnapshotPool().getDataElement()){
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
		
	private DataSnapshotElement getDataSnapshotElement(String dataPoolElementId){
		DataSnapshotElement dataSnapshotElement = null;

		List<DataSnapshotElement> dataSnapshotElementList = processInstance.getData().getDataSnapshotGraphs().getDataSnapshotElement();
		for (DataSnapshotElement root:dataSnapshotElementList){
			if (!root.isEmpty() && root.getDataPoolElementId().equals(dataPoolElementId)){
				dataSnapshotElement = root;
			}else{
				dataSnapshotElement = getDataSnapshotElement(root.getDataTransition(), dataPoolElementId);
			}
			
			if (dataSnapshotElement != null){
				break;
			}
		}
		return dataSnapshotElement;
	}
	
	private DataSnapshotElement getDataSnapshotElement(DataPoolElement dataPoolElement){

		return getDataSnapshotElement(dataPoolElement.getId());
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

	public CompositeProcessInstance getCompositeProcessInstance() {
		return compositeProcessInstance;
	}

	public void setCompositeProcessInstance(CompositeProcessInstance compositeProcessInstance) {
		this.compositeProcessInstance = compositeProcessInstance;		
		
	}
	
	public DataSnapshotPool getReadOnlyDataSnapshotPool(){
		if (compositeProcessInstance != null){
			return compositeProcessInstance.getDataSnapshotPool();
		}else{
			org.bpim.model.data.v1.ObjectFactory objectFactory = new org.bpim.model.data.v1.ObjectFactory();
			DataSnapshotPool result = objectFactory.createDataSnapshotPool();
			result.getDataElement().addAll(this.getProcessInstance().getData().getDataSnapshotPool().getDataElement());
			for (Long processId : getCorelatedProcessInstances()){
				result.getDataElement().addAll(
						executionContext.getProcessInstanceContext(
								processId.toString()).getReadOnlyDataSnapshotPool().getDataElement());
			}
			return this.getProcessInstance().getData().getDataSnapshotPool();
		}
	}
	
	private DataSnapshotPool getEditableDataSnapshotPool(){
		if (compositeProcessInstance != null){
			return compositeProcessInstance.getDataSnapshotPool();
		}else{
			return this.getProcessInstance().getData().getDataSnapshotPool();
		}
	}
	
	private List<Long> getCorelatedProcessInstances() {
		if (corelatedProcessInstances == null){
			corelatedProcessInstances = new ArrayList<Long>();
		}
		return corelatedProcessInstances;
	}
}
