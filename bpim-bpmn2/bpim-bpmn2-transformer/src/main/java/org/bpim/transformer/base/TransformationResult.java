package org.bpim.transformer.base;

import java.util.ArrayList;
import java.util.List;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.execpath.v1.Activity;

public class TransformationResult {
	
	private Activity execPathActivity;	
	private List<DataPoolElement> dataPoolElements = null;
	private DataSnapshotElement inputData;
	
	public TransformationResult(){		
		dataPoolElements = new ArrayList<DataPoolElement>();
	}
	
	public Activity getExecPathActivity() {
		return execPathActivity;
	}
	public void setExecPathActivity(Activity execPathElement) {
		this.execPathActivity = execPathElement;
	}

	public List<DataPoolElement> getDataPoolElements() {
		return dataPoolElements;
	}

	public void setDataPoolElements(List<DataPoolElement> dataPoolElements) {
		this.dataPoolElements = dataPoolElements;
	}	
	
	public DataSnapshotElement getInputData() {
		return inputData;
	}

	public void setInputData(DataSnapshotElement inputData) {
		this.inputData = inputData;
	}

}
