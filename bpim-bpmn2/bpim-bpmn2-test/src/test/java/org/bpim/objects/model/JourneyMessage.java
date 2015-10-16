package org.bpim.objects.model;

import java.util.Date;

import org.bpim.objects.ETollDataObject;

public class JourneyMessage extends ETollDataObject{
	private String gateId;
	private Date creationDTM;
	private String messageType;
	private Object data;
	
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
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
