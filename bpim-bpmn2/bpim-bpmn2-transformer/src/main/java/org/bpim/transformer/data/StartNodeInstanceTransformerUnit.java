package org.bpim.transformer.data;

import java.util.Map;
import java.util.Map.Entry;

import org.bpim.model.data.v1.DataItem;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.DataPoolElementHelper;
import org.bpim.transformer.util.DataSnapshotElementHelper;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.WorkflowProcessInstance;

import com.google.gson.Gson;


public class StartNodeInstanceTransformerUnit extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		WorkflowProcessInstance processInstance = 
				(WorkflowProcessInstance) nodeInstance.getProcessInstance();
		
		//org.bpim.model.data.v1.ObjectFactory objectFactory = new org.bpim.model.data.v1.ObjectFactory();
		VariableScopeInstance variableScopeInstance = (VariableScopeInstance) processInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
		if (variableScopeInstance != null){
    		Map<String, Object> vars = variableScopeInstance.getVariables();
    		
    		DataPoolElement dataPoolElement = null;
    		
    		DataSnapshotElement inputDataSnapshotElement = dataObjectFactory.createDataSnapshotElement();
			inputDataSnapshotElement.setEmpty(true);
			inputDataSnapshotElement.setId(UniqueIdGenerator.nextId());
			
			DataTransition dataTransition = null;
			
			DataSnapshotElement targetDataSnapshotElement = null;
					
    		for (Entry<String, Object> entry : vars.entrySet()){    			    			
    			
    			dataPoolElement = DataPoolElementHelper.create(entry.getValue(), entry.getKey());    			    			
    			transformationResult.getDataPoolElements().add(dataPoolElement);
    			
    			dataTransition = dataObjectFactory.createDataTransition();
    			dataTransition.setId(UniqueIdGenerator.nextId());
    			dataTransition.setName(transformationResult.getExecPathActivity().getName());
    			
    			targetDataSnapshotElement = DataSnapshotElementHelper.create(dataPoolElement);    					
    			dataTransition.setDataSnapshotElement(targetDataSnapshotElement);
    			inputDataSnapshotElement.getDataTransition().add(dataTransition);    		
    		}
    		
    		transformationResult.setInputData(inputDataSnapshotElement);
    	}				
	}
}
