package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.NormalTransition;
import org.bpim.model.execpath.v1.Start;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.workflow.instance.NodeInstance;


public class StartNodeInstanceTransformerUnit extends TransformerUnit {
	
	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		Start  start = executionPathObjectFactory.createStart();
		start.setId(UniqueIdGenerator.nextId());
		start.setName(nodeInstance.getNodeName());
		NormalTransition normalTransition = executionPathObjectFactory.createNormalTransition();
		normalTransition.setId(UniqueIdGenerator.nextId());
		start.getOutputTransition().add(normalTransition);
		transformationResult.setFlowNode(start);
	}

}
