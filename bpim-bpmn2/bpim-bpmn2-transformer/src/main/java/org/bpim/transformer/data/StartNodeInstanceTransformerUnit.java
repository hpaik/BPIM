package org.bpim.transformer.data;

import java.util.Map;
import java.util.Map.Entry;

import org.bpim.model.data.v1.DataItem;
import org.bpim.model.data.v1.DataOutput;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.workflow.instance.NodeInstance;


public class StartNodeInstanceTransformerUnit extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		org.jbpm.workflow.instance.WorkflowProcessInstance processInstance = 
				(org.jbpm.workflow.instance.WorkflowProcessInstance) nodeInstance.getProcessInstance();
		org.bpim.model.data.v1.ObjectFactory objectFactory = new org.bpim.model.data.v1.ObjectFactory();
		
		VariableScopeInstance variableScopeInstance = (VariableScopeInstance) processInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
		if (variableScopeInstance != null){
    		Map<String, Object> vars = variableScopeInstance.getVariables();
    		DataPoolElement dataPoolElement = null;
    		DataOutput dataOutput = null;
    		for (Entry<String, Object> entry : vars.entrySet()){
    			dataOutput = objectFactory.createDataOutput();
    			dataOutput.setId(UniqueIdGenerator.nextId());
    			
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
    			
    			dataOutput.getDataElementIs().add(dataPoolElement.getId());    			
    		}
    		if (dataOutput != null){
    			transformationResult.setDataTransition(objectFactory.createDataTransition());
    			transformationResult.getDataTransition().setId(UniqueIdGenerator.nextId());
    			transformationResult.getDataTransition().setName(transformationResult.getExecPathElement().getName());
    			transformationResult.getDataTransition().setDataOutput(dataOutput);
    		}
    	}				
	}
}
