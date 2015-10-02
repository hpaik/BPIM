package org.bpim.transformer.base;

import org.jbpm.workflow.instance.NodeInstance;

public abstract class Transformer {
	
	public abstract TransformationResult transform(NodeInstance nodeInstance);

}
