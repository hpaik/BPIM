package org.bpim.example.customerjourney.model;

public class DiscountEntitlement extends ETollDataObject{
	private long amount;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}

}
