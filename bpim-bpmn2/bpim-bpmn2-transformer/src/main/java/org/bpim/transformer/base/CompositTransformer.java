package org.bpim.transformer.base;

import java.util.ArrayList;
import java.util.List;

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
	public Object transform() {
		
		for (TransformerUnit transformerUnit : transformerUnits){
			transformerUnit.transform();
		}
		return null;
	}
}
