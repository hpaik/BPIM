package org.bpim.transformer.data;

import java.util.Map;
import java.util.Map.Entry;

import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.DataSnapshotGraphHelper;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;

public class ReceiveTaskNodeInstanceTransformerUnit extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
//		WorkItemNodeInstance workItemNodeInstance = (WorkItemNodeInstance) nodeInstance;
//		Map<String, Object> parameters = workItemNodeInstance.getWorkItem().getParameters();
//		parameters.clear();
//		Map<String, Object> results = workItemNodeInstance.getWorkItem().getResults();
//		WorkflowProcessInstance processInstance = 
//				(WorkflowProcessInstance) nodeInstance.getProcessInstance();
//		VariableScopeInstance variableScopeInstance = (VariableScopeInstance) processInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
//		if (variableScopeInstance != null){
//			for (Entry<String, Object> entry: results.entrySet()){
//				variableScopeInstance.setVariable(entry.getKey(), entry.getValue());
//			}
//		}
//		
//		DataSnapshotGraphHelper.createDataSnapshotWithMultiParamsandResults(parameters, results, transformationResult);
	}

}
