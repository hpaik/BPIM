package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.CallProcessInstance;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.ExecutionPathHelper;
import org.jbpm.workflow.instance.NodeInstance;

public class SubProcessNodeInstanceTransformerUnit  extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance,
			TransformationResult transformationResult) {		
		CallProcessInstance callProcessInstance = ExecutionPathHelper.createCallProcessInstance(nodeInstance);		
		transformationResult.setFlowNode(callProcessInstance);
	}

}
