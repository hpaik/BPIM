package org.bpim.transformer.data;

import java.util.Map;

import org.bpim.model.execpath.v1.Activity;
import org.bpim.model.execpath.v1.FlowNode;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.DataSnapshotGraphHelper;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;

public class ReceiveTaskNodeInstanceTransformerUnit extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		
		WorkItemNodeInstance workItemNodeInstance = (WorkItemNodeInstance) nodeInstance;
		Map<String, Object> results = workItemNodeInstance.getWorkItem().getResults();
		//Checks if message was sent from another process instance
		if (results.containsKey("SourceProcessId")){
		
			Long processId = Long.parseLong(results.get("SourceProcessId").toString());
			results.remove("SourceProcessId");
			transformationResult.getCorelatedProcessInstances().add(processId);
			FlowNode tmpFlowNode = transformationResult.getFlowNode();
			transformationResult.setFlowNode(((Activity)transformationResult.getFlowNode()).getOutputTransition().get(0).getTo());
			//Creates Data Snapshot elements for Reference Process Instance node
			DataSnapshotGraphHelper.createDataSnapshotWithMultiParamsandResults(null, results, transformationResult);
			transformationResult.setFlowNode(tmpFlowNode);
		}
	}

}
