package org.bpim.example.customerjourney.service;

import org.bpim.example.customerjourney.model.CustomerAccount;
import org.bpim.example.customerjourney.model.ImageProcessingResult;
import org.bpim.example.customerjourney.model.JourneyDetails;
import org.bpim.example.customerjourney.model.JourneyMessage;

public class CustomerAccountService {
	
	public CustomerAccount getCustomerAccountByPlateNumber(ImageProcessingResult imageProcessingResult){
		CustomerAccount customerAccount = new CustomerAccount(); 
	    customerAccount.setCustomerId("111111");
	    customerAccount.setAccountId("7050");	        		
		customerAccount.setPlateNum(imageProcessingResult.getPlateNumber());
		return customerAccount;
	}
	
	public JourneyDetails getJourneyDetails(JourneyMessage journeyMessage){
		JourneyDetails journeyDetails = new JourneyDetails();
		journeyDetails.setCreationDTM(journeyMessage.getCreationDTM());
		journeyDetails.setGateId(journeyMessage.getGateId());
		//journeyDetails.setObjectId(journeyMessage.getObjectId());
		return journeyDetails; 
	}
	
	public ImageProcessingResult processImage(JourneyMessage journeyMessage){
		ImageProcessingResult imageProcessingResult = new ImageProcessingResult();
		imageProcessingResult.setConfidenceRate(4);
		imageProcessingResult.setPlateNumber("pki*******");
		return imageProcessingResult;
	}
	
	public void error(){
		
	}
	
	public void extractTransponderId(){
		
	}
	
	public void getCustomerAccountByTransponderId(){
		
	}
	
	
	
}
