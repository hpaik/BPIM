package org.bpim.transformer.executionpath;

import org.bpim.model.execpath.v1.End;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.jbpm.workflow.instance.NodeInstance;

public class EndNodeInstanceTransformerUnit extends TransformerUnit {
		
	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		End  end = executionPathObjectFactory.createEnd();
		end.setId(UniqueIdGenerator.nextId());
		end.setName(nodeInstance.getNodeName());		
		transformationResult.setFlowNode(end);
	}

}
