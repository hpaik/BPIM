package org.bpim.transformer.base;

import java.util.ArrayList;
import java.util.List;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.model.execpath.v1.FlowNode;

public class TransformationResult {
	
	private FlowNode execPathActivity;
	private FlowNode execPathTransition;	
	private List<DataPoolElement> dataPoolElements = null;
	private DataSnapshotElement inputData;
	
	public TransformationResult(){		
		dataPoolElements = new ArrayList<DataPoolElement>();
	}
	
	public FlowNode getExecPathActivity() {
		return execPathActivity;
	}
	public void setExecPathActivity(FlowNode execPathElement) {
		this.execPathActivity = execPathElement;
	}

	public List<DataPoolElement> getDataPoolElements() {
		return dataPoolElements;
	}

	public void setDataPoolElements(List<DataPoolElement> dataPoolElements) {
		this.dataPoolElements = dataPoolElements;
	}	

	public FlowNode getExecPathTransition() {
		return execPathTransition;
	}

	public void setExecPathTransition(FlowNode execPathTransition) {
		this.execPathTransition = execPathTransition;
	}

	public DataSnapshotElement getInputData() {
		return inputData;
	}

	public void setInputData(DataSnapshotElement inputData) {
		this.inputData = inputData;
	}

}
