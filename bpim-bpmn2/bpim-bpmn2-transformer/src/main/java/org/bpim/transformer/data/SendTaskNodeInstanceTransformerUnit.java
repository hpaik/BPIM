package org.bpim.transformer.data;

import java.util.Map;

import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.DataSnapshotGraphHelper;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;

public class SendTaskNodeInstanceTransformerUnit extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		WorkItemNodeInstance workItemNodeInstance = (WorkItemNodeInstance) nodeInstance;
		Map<String, Object> parameters = workItemNodeInstance.getWorkItem().getParameters();
		Map<String, Object> results = workItemNodeInstance.getWorkItem().getResults();
		
		DataSnapshotGraphHelper.createDataSnapshotWithMultiParamsandResults(parameters, results, transformationResult);
	}

}
