package org.bpim.transformer.base;


import org.bpim.model.base.v1.ElementBase;
import org.jbpm.workflow.instance.NodeInstance;


public abstract class TransformerUnit {
	
	protected org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = null;
	protected org.bpim.model.data.v1.ObjectFactory dataObjectFactory = null;
	public TransformerUnit(){
		executionPathObjectFactory = new org.bpim.model.execpath.v1.ObjectFactory();
		dataObjectFactory = new org.bpim.model.data.v1.ObjectFactory(); 
	}

	public abstract void transform(NodeInstance nodeInstance, TransformationResult transformationResult);
}
