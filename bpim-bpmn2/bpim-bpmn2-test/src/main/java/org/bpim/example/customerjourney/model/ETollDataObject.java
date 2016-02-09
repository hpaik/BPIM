package org.bpim.example.customerjourney.model;

import org.bpim.transformer.base.BPIMDataObject;

public abstract class ETollDataObject implements BPIMDataObject {

	protected String objectId;
	protected String label;
	
	public static int id = 5000;
	
	public ETollDataObject(){
		objectId = String.valueOf(id);
		id++;
	}

	public String getObjectId() {
		
		return objectId;
	}

	public void setObjectId(String objectId) {
		
		this.objectId = objectId;
	}
	
	public String getLabel(){
		return label;
	}
	
}
