package org.bpim.example.customerjourney.model;

import java.util.ArrayList;

import org.bpim.transformer.base.BPIMDataObject;

public class DiscountEntitlementList<T> extends ArrayList<T> implements BPIMDataObject{

	private static final long serialVersionUID = 1L;
	
	protected String objectId;
	protected String label;
	
	public DiscountEntitlementList(){
		objectId = String.valueOf(ETollDataObject.id);
		ETollDataObject.id ++;
		label = "Discount Entitlement List";
	}
	
	
	public String getObjectId() {
		
		return objectId;
	}

	public void setObjectId(String objectId) {
		
		this.objectId = objectId;
	}


	@Override
	public String getLabel() {
	
		return label;
	}
	
	
}
