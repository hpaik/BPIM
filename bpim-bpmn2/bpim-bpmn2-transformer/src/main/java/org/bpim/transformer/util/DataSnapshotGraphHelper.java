package org.bpim.transformer.util;

import java.util.Map;
import java.util.Map.Entry;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.model.data.v1.ObjectFactory;
import org.bpim.transformer.base.BPIMDataObject;
import org.bpim.transformer.base.TransformationResult;

public class DataSnapshotGraphHelper {
	
	public static void createDataSnapshotWithMultiParamsandResults(Map<String, Object> parameters,
			Map<String, Object> results,
			TransformationResult transformationResult){
		
		DataPoolElement dataPoolElement = null;
		for (Entry<String, Object> entry : results.entrySet()){
			dataPoolElement = DataPoolElementHelper.create((BPIMDataObject)entry.getValue(), entry.getValue().getClass().getSimpleName());
			transformationResult.getDataPoolElements().add(dataPoolElement);
		}
		
		boolean isVoid = false;
		if (results == null || results.isEmpty()){
			isVoid = true;
		}
		ObjectFactory dataObjectFactory = new ObjectFactory();  
		
		if (parameters == null || parameters.isEmpty()){
			DataSnapshotElement sourceDataSnapshotElement = DataSnapshotElementHelper.createEmpty();
			DataTransition dataTransition = null;
			DataSnapshotElement targetDataSnapshotElement = null;
			for (Entry<String, Object> entry : results.entrySet()){
				dataTransition = dataObjectFactory.createDataTransition();
				dataTransition.setId(UniqueIdGenerator.nextId());
				dataTransition.setName(transformationResult.getFlowNode().getName());
				dataPoolElement = getDataPoolElement(((BPIMDataObject)entry.getValue()).getObjectId(),transformationResult);				
				targetDataSnapshotElement = DataSnapshotElementHelper.create(dataPoolElement);    					
				dataTransition.setDataSnapshotElement(targetDataSnapshotElement);
				sourceDataSnapshotElement.getDataTransition().add(dataTransition);				
			}
			transformationResult.getSourceDataSnapshotElement().add(sourceDataSnapshotElement);
			return;
		}
							 					
		DataSnapshotElement sourceDataSnapshotElement = null;
		DataTransition dataTransition = null;
		DataSnapshotElement targetDataSnapshotElement = null;
		for (Entry<String, Object> parameter : parameters.entrySet()){
			sourceDataSnapshotElement = dataObjectFactory.createDataSnapshotElement();
			if (!(parameter.getValue() instanceof BPIMDataObject)){
				continue;
			}
			sourceDataSnapshotElement.setMappingCorrelationId(((BPIMDataObject)parameter.getValue()).getObjectId());
			sourceDataSnapshotElement.setEmpty(false);
			if(isVoid){
				dataTransition = dataObjectFactory.createDataTransition();
				dataTransition.setId(UniqueIdGenerator.nextId());
				dataTransition.setName(transformationResult.getFlowNode().getName());				
				targetDataSnapshotElement = DataSnapshotElementHelper.createEmpty();    					
				dataTransition.setDataSnapshotElement(targetDataSnapshotElement);
				sourceDataSnapshotElement.getDataTransition().add(dataTransition);
				transformationResult.getSourceDataSnapshotElement().add(sourceDataSnapshotElement);
			}else {
				for (Entry<String, Object> entry : results.entrySet()){
					dataTransition = dataObjectFactory.createDataTransition();
					dataTransition.setId(UniqueIdGenerator.nextId());
					dataTransition.setName(transformationResult.getFlowNode().getName());
					dataPoolElement = getDataPoolElement(((BPIMDataObject)entry.getValue()).getObjectId(),transformationResult);
					targetDataSnapshotElement = DataSnapshotElementHelper.create(dataPoolElement);    					
					dataTransition.setDataSnapshotElement(targetDataSnapshotElement);
					sourceDataSnapshotElement.getDataTransition().add(dataTransition);
					transformationResult.getSourceDataSnapshotElement().add(sourceDataSnapshotElement);
				}
			}
		}
		
	}
	
	private static DataPoolElement getDataPoolElement(String objectId, TransformationResult transformationResult){
		for (DataPoolElement dataPoolElement: transformationResult.getDataPoolElements()){
			if (dataPoolElement.getMappingCorrelationId().equals(objectId)){
				return dataPoolElement;
			}
		}
		return null;
	}

}
