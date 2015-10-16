package org.bpim.objects.model;

import java.util.Date;

import org.bpim.objects.ETollDataObject;

public class JourneyDetails extends ETollDataObject{
	private String gateId;
	private Date creationDTM;
	
	public String getGateId() {
		return gateId;
	}
	public void setGateId(String gateId) {
		this.gateId = gateId;
	}
	public Date getCreationDTM() {
		return creationDTM;
	}
	public void setCreationDTM(Date creationDTM) {
		this.creationDTM = creationDTM;
	}
}
