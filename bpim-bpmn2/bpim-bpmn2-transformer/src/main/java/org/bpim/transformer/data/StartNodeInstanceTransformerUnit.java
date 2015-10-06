package org.bpim.transformer.data;

import java.util.Map;
import java.util.Map.Entry;

import org.bpim.model.data.v1.DataItem;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.WorkflowProcessInstance;


public class StartNodeInstanceTransformerUnit extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		WorkflowProcessInstance processInstance = 
				(WorkflowProcessInstance) nodeInstance.getProcessInstance();
		//WorkflowProcessInstance processInstance = nodeInstance.getProcessInstance();
		
		org.bpim.model.data.v1.ObjectFactory objectFactory = new org.bpim.model.data.v1.ObjectFactory();
		VariableScopeInstance variableScopeInstance = (VariableScopeInstance) processInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
		if (variableScopeInstance != null){
    		Map<String, Object> vars = variableScopeInstance.getVariables();
    		
    		DataPoolElement dataPoolElement = null;
    		
    		DataSnapshotElement inputDataSnapshotElement = objectFactory.createDataSnapshotElement();
			inputDataSnapshotElement.setEmpty(true);
			inputDataSnapshotElement.setId(UniqueIdGenerator.nextId());
			
			DataTransition dataTransition = objectFactory.createDataTransition();
			
			DataSnapshotElement targetDataSnapshotElement = null;
					
    		for (Entry<String, Object> entry : vars.entrySet()){    			
    			
    			if(entry.getValue().getClass().isArray()){
    				dataPoolElement = objectFactory.createDataItemArray();    			
    				dataPoolElement.setDataObject(entry.getValue());
    				dataPoolElement.setName(entry.getKey());
    				
    			}else{
    				dataPoolElement = objectFactory.createDataItem();    			
    				dataPoolElement.setDataObject(entry.getValue());
    				dataPoolElement.setName(entry.getKey());
    			}
    			dataPoolElement.setId(UniqueIdGenerator.nextId());
    			transformationResult.getDataPoolElements().add(dataPoolElement);
    			
    			
    			dataTransition = objectFactory.createDataTransition();
    			dataTransition.setId(UniqueIdGenerator.nextId());
    			dataTransition.setName(transformationResult.getExecPathActivity().getName());
    			
    			targetDataSnapshotElement = objectFactory.createDataSnapshotElement();
    			targetDataSnapshotElement.setEmpty(false);
    			targetDataSnapshotElement.setId(UniqueIdGenerator.nextId());
    			targetDataSnapshotElement.setDataPoolElementId(dataPoolElement.getId());
    			dataTransition.setDataSnapshotElement(targetDataSnapshotElement);
    			inputDataSnapshotElement.getDataTransition().add(dataTransition);    		
    		}
    		
    		transformationResult.setInputData(inputDataSnapshotElement);
    	}				
	}
}
