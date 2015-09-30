package org.bpim.transformer.executionpath;

import org.bpim.model.base.v1.ElementBase;
import org.bpim.model.execpath.v1.Start;
import org.bpim.transformer.base.TransformerUnit;
import org.jbpm.workflow.instance.NodeInstance;


public class StartNodeInstanceTransformerUnit extends TransformerUnit {
	
	@Override
	public ElementBase transform(NodeInstance nodeInstance) {
		Start  start = executionPathObjectFactory.createStart();
		return start;
	}

}
