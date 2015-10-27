package org.bpim.objects;

import java.util.ArrayList;

import org.bpim.transformer.base.BPIMDataObject;

public class ETollDataObjectList<T> extends ArrayList<T> implements BPIMDataObject{

	private static final long serialVersionUID = 1L;
	//public static int id = 50000;
	
	protected String objectId;
	
	public ETollDataObjectList(){
		objectId = String.valueOf(ETollDataObject.id);
		ETollDataObject.id ++;
	}
	
	
	public String getObjectId() {
		
		return objectId;
	}

	public void setObjectId(String objectId) {
		
		this.objectId = objectId;
	}
	
	
}
