package org.bpim.transformer.data;

import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.TransformerUnit;
import org.bpim.transformer.util.DataSnapshotGraphHelper;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance;

public class HumanTaskNodeInstanceTransformerUnit  extends TransformerUnit {

	@Override
	public void transform(NodeInstance nodeInstance, TransformationResult transformationResult) {
		HumanTaskNodeInstance humanTaskNodeInstance = (HumanTaskNodeInstance) nodeInstance;
		DataSnapshotGraphHelper.createDataSnapshotWithMultiParamsandResults(humanTaskNodeInstance.getWorkItem().getParameters()
				, humanTaskNodeInstance.getWorkItem().getResults(), transformationResult);
	}

}
