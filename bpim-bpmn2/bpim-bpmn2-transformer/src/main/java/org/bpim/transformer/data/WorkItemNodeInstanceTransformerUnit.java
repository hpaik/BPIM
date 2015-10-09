package org.bpim.transformer.data;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.transformer.base.BPIMDataObject;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.DataPoolElementHelper;
import org.bpim.transformer.util.DataSnapshotElementHelper;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;

public class WorkItemNodeInstanceTransformerUnit  extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance,
			TransformationResult transformationResult) {	
		WorkItemNodeInstance workItemNodeInstance = (WorkItemNodeInstance) nodeInstance;
		Object parameter = workItemNodeInstance.getWorkItem().getParameter("Parameter");
		Object result = workItemNodeInstance.getWorkItem().getResult("Result");
		
		if (result == null){
			return;
		}
		
		DataPoolElement dataPoolElement = DataPoolElementHelper.create(result, result.getClass().getSimpleName());
		transformationResult.getDataPoolElements().add(dataPoolElement);
		
		DataSnapshotElement inputDataSnapshotElement = dataObjectFactory.createDataSnapshotElement();
		inputDataSnapshotElement.setMappingCorrelationId(((BPIMDataObject)parameter).getObjectId());
		
		DataTransition dataTransition = dataObjectFactory.createDataTransition();
		dataTransition.setId(UniqueIdGenerator.nextId());
		//dataTransition.setName(transformationResult.getExecPathActivity().getName());
		
		DataSnapshotElement targetDataSnapshotElement = DataSnapshotElementHelper.create(dataPoolElement);    					
		dataTransition.setDataSnapshotElement(targetDataSnapshotElement);
		inputDataSnapshotElement.getDataTransition().add(dataTransition);
		
		
		transformationResult.setInputData(inputDataSnapshotElement);
		
		
	}

}
