package org.bpim.transformer.base;


import org.jbpm.workflow.instance.NodeInstance;

import com.google.gson.Gson;


public abstract class TransformerUnit {
	
	protected org.bpim.model.execpath.v1.ObjectFactory executionPathObjectFactory = null;
	protected org.bpim.model.data.v1.ObjectFactory dataObjectFactory = null;
	protected Gson gson = null;
	
	public TransformerUnit(){
		executionPathObjectFactory = new org.bpim.model.execpath.v1.ObjectFactory();
		dataObjectFactory = new org.bpim.model.data.v1.ObjectFactory();
		gson = new Gson();
	}

	public abstract void transform(NodeInstance nodeInstance, TransformationResult transformationResult);
}
