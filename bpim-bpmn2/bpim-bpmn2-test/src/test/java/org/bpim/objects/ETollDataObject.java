package org.bpim.objects;

import org.bpim.transformer.base.BPIMDataObject;

public abstract class ETollDataObject implements BPIMDataObject {

	protected String objectId;
	private static int id = 1000;

	public String getObjectId() {
		if (objectId == null){
			objectId = String.valueOf(id);
			id++;
		}
		return objectId;
	}

	public void setObjectId(String objectId) {
		
		this.objectId = objectId;
	}
	
}
