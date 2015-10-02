package org.bpim.transformer.base;

import java.util.ArrayList;
import java.util.List;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.model.execpath.v1.FlowNode;

public class TransformationResult {
	
	private FlowNode execPathElement;
	private DataTransition dataTransition = null;
	private List<DataPoolElement> dataPoolElements = null;
	
	public TransformationResult(){		
		dataPoolElements = new ArrayList<DataPoolElement>();
	}
	
	public FlowNode getExecPathElement() {
		return execPathElement;
	}
	public void setExecPathElement(FlowNode execPathElement) {
		this.execPathElement = execPathElement;
	}

	public List<DataPoolElement> getDataPoolElements() {
		return dataPoolElements;
	}

	public void setDataPoolElements(List<DataPoolElement> dataPoolElements) {
		this.dataPoolElements = dataPoolElements;
	}

	public DataTransition getDataTransition() {
		return dataTransition;
	}

	public void setDataTransition(DataTransition dataTransition) {
		this.dataTransition = dataTransition;
	}

}
