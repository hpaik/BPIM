package org.bpim.example.customerjourney.model;

public class CustomerAccount extends ETollDataObject{
	
	private String accountId = null;
	private String customerId = null;
	private String plateNum = null;
	
	public CustomerAccount(){
		label = "Customer Account";
	}
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPlateNum() {
		return plateNum;
	}
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}

}
