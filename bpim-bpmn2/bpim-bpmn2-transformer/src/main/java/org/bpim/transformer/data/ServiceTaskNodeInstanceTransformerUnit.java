package org.bpim.transformer.data;

import java.util.Map;
import java.util.Map.Entry;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.transformer.base.BPIMDataObject;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.DataPoolElementHelper;
import org.bpim.transformer.util.DataSnapshotElementHelper;
import org.bpim.transformer.util.DataSnapshotGraphHelper;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;

public class ServiceTaskNodeInstanceTransformerUnit  extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance,
			TransformationResult transformationResult) {	
		WorkItemNodeInstance workItemNodeInstance = (WorkItemNodeInstance) nodeInstance;
		Map<String, Object> parameters = workItemNodeInstance.getWorkItem().getParameters();
		Map<String, Object> results = workItemNodeInstance.getWorkItem().getResults();
		
		DataSnapshotGraphHelper.createDataSnapshotWithMultiParamsandResults(parameters, results, transformationResult);
	}
	
	

}
