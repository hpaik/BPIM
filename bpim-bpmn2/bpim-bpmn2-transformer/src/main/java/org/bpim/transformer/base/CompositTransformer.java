package org.bpim.transformer.base;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.workflow.instance.NodeInstance;

public class CompositTransformer extends Transformer {

	private List<TransformerUnit> transformerUnits = null;
			
	public void registerTransformerUnit(TransformerUnit transformerUnit){
		getTransformerUnits().add(transformerUnit);
	}		
	
	public List<TransformerUnit> getTransformerUnits(){
		if(transformerUnits == null){
			transformerUnits = new ArrayList<TransformerUnit>();
		}
		return transformerUnits;
	}
	
	@Override
	public TransformationResult transform(NodeInstance nodeInstance) {
		TransformationResult transformationResult = new TransformationResult();
		for (TransformerUnit transformerUnit : transformerUnits){
			transformerUnit.transform(nodeInstance, transformationResult);
		}
		return transformationResult;
	}
}
