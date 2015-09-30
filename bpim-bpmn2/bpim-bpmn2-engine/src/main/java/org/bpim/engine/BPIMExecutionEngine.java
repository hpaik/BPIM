package org.bpim.engine;



import org.bpim.transformer.base.Transformer;
import org.bpim.transformer.factory.TranformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BPIMExecutionEngine {
	
	protected static final Logger logger = LoggerFactory.getLogger(BPIMExecutionEngine.class);
	
	public static void process(org.jbpm.workflow.instance.NodeInstance nodeInstance){
		
//		org.jbpm.workflow.instance.WorkflowProcessInstance processInstance = 
//				(org.jbpm.workflow.instance.WorkflowProcessInstance) nodeInstance.getProcessInstance();
		
		Transformer transformer = TranformerFactory.createTransformer(nodeInstance.getClass().getSimpleName());
		
		transformer.transform(nodeInstance);
		
//		VariableScopeInstance variableScopeInstance = (VariableScopeInstance) processInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
//    	if (variableScopeInstance != null){
//    		Map<String, Object> vars = variableScopeInstance.getVariables();
//    		Object employee = vars.get("employee");
//    	}
	}

}
