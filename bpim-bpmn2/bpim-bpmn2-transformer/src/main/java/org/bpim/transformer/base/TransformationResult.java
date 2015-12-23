package org.bpim.transformer.base;

import java.util.ArrayList;
import java.util.List;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.execpath.v1.Activity;
import org.bpim.model.execpath.v1.FlowNode;

public class TransformationResult {
	
	private FlowNode flowNode;
	private List<DataPoolElement> dataPoolElements = null;
	private List<DataSnapshotElement> sourceDataSnapshotElement = null;
	private List<Long> corelatedProcessInstances = null;
	private boolean addToPool = true;
	
	public TransformationResult(){		
		dataPoolElements = new ArrayList<DataPoolElement>();
		sourceDataSnapshotElement = new  ArrayList<DataSnapshotElement>();
	}

	public List<DataPoolElement> getDataPoolElements() {
		return dataPoolElements;
	}

	public void setDataPoolElements(List<DataPoolElement> dataPoolElements) {
		this.dataPoolElements = dataPoolElements;
	}	
	
	public List<DataSnapshotElement> getSourceDataSnapshotElement() {
		return sourceDataSnapshotElement;
	}

	public FlowNode getFlowNode() {
		return flowNode;
	}

	public void setFlowNode(FlowNode flowNode) {
		this.flowNode = flowNode;
	}

	public List<Long> getCorelatedProcessInstances() {
		if (corelatedProcessInstances == null){
			corelatedProcessInstances = new ArrayList<Long>();
		}
		return corelatedProcessInstances;
	}

	public void setCorelatedProcessInstances(
			List<Long> corelatedProcessInstances) {
		this.corelatedProcessInstances = corelatedProcessInstances;
	}

	public boolean isAddToPool() {
		return addToPool;
	}

	public void setAddToPool(boolean addToPool) {
		this.addToPool = addToPool;
	}	

}
