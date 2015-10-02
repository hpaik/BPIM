package org.bpim.engine;



import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.Transformer;
import org.bpim.transformer.factory.TranformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BPIMExecutionEngine {
	
	protected static final Logger logger = LoggerFactory.getLogger(BPIMExecutionEngine.class);
	
	@SuppressWarnings("unused")
	public static void process(org.jbpm.workflow.instance.NodeInstance nodeInstance){		
		
		Transformer transformer = TranformerFactory.createTransformer(nodeInstance.getClass().getSimpleName());
		
		TransformationResult transformationResult = transformer.transform(nodeInstance);
		
		int i = 0;
	}

}
