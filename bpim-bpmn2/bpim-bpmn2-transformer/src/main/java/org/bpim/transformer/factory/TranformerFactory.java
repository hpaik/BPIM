package org.bpim.transformer.factory;

import java.util.HashMap;
import java.util.Map;

import org.bpim.transformer.base.CompositTransformer;
import org.bpim.transformer.base.Transformer;
import org.bpim.transformer.base.TransformerUnit;
import org.jbpm.workflow.instance.NodeInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranformerFactory {
	
	protected static final Logger logger = LoggerFactory.getLogger(TranformerFactory.class);
	
	private static Map<String, Transformer> transformersCache = null;
	
	public static Transformer createTransformer(NodeInstance nodeInstance){		
		return getTransformer(nodeInstance.getClass().getSimpleName());
	}
	
	
	private static Transformer getTransformer(String transformerUnitType){		
		if (transformersCache == null){
			transformersCache = new HashMap<String, Transformer>(); 
		}
		if (transformersCache.containsKey(transformerUnitType)){
			return transformersCache.get(transformerUnitType);
		}else{
			Transformer transformer = createTransformer(transformerUnitType);
			transformersCache.put(transformerUnitType, transformer);
			return transformer;
		}
	}
	
	
	private static Transformer createTransformer(String transformerUnitType) {
		CompositTransformer compositTransformer = new CompositTransformer();
		try {
			compositTransformer.registerTransformerUnit((TransformerUnit) 
					Class.forName("org.bpim.transformer.data." + transformerUnitType + "TransformerUnit").newInstance());
			compositTransformer.registerTransformerUnit((TransformerUnit) 
					Class.forName("org.bpim.transformer.executionpath." + transformerUnitType + "TransformerUnit").newInstance());
			
		} catch (Exception e) {
			logger.error("Can not create TransformerUnit", e);
		}
		return compositTransformer;
	}
	
	
}
