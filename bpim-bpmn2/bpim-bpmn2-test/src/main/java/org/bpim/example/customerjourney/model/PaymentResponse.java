package org.bpim.example.customerjourney.model;

public class PaymentResponse extends ETollDataObject{
	
	private String responseCode;
	private String paymentReciept;
	private String message;
	
	public PaymentResponse(){
		label = "Payment Response";
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPaymentReciept() {
		return paymentReciept;
	}
	public void setPaymentReciept(String paymentReciept) {
		this.paymentReciept = paymentReciept;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

}
