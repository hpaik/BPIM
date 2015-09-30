package org.bpim.transformer.data;

import java.util.Map;
import java.util.Map.Entry;

import org.bpim.model.base.v1.ElementBase;
import org.bpim.transformer.base.TransformerUnit;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.workflow.instance.NodeInstance;


public class StartNodeInstanceTransformerUnit extends TransformerUnit {

	@Override
	public ElementBase transform(NodeInstance nodeInstance) {
		org.jbpm.workflow.instance.WorkflowProcessInstance processInstance = 
				(org.jbpm.workflow.instance.WorkflowProcessInstance) nodeInstance.getProcessInstance();
		
		VariableScopeInstance variableScopeInstance = (VariableScopeInstance) processInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
		if (variableScopeInstance != null){
    		Map<String, Object> vars = variableScopeInstance.getVariables();
    		for (Entry<String, Object> entry : vars.entrySet()){
    			
    		}
    		//Object employee = vars.get("employee");
    	}
		
		return null;
	}

}
