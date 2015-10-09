package org.bpim.objects;

import org.bpim.transformer.base.BPIMDataObject;

public abstract class ETollDataObject implements BPIMDataObject {

	protected String objectId;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
}
