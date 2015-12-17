package org.bpim.example.customerjourney.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bpim.example.customerjourney.model.CustomerAccount;
import org.bpim.example.customerjourney.model.DiscountEntitlement;
import org.bpim.example.customerjourney.model.DiscountEntitlementList;
import org.bpim.example.customerjourney.model.FairAmount;
import org.bpim.example.customerjourney.model.JourneyDetails;
import org.bpim.example.customerjourney.model.PaymentResponse;
import org.bpim.transformer.util.DataPoolElementHelper;

public class CustomerPaymentService {
	
	private static int discountEntitlementCounter = 0;
	
	public FairAmount calculateFareAmount(CustomerAccount customerAccount, JourneyDetails journeyDetails){
		FairAmount fairAmount = new FairAmount();
		fairAmount.setDiscounted(false);
		fairAmount.setPrice(150);
		return fairAmount;
	}
	
	public DiscountEntitlementList<DiscountEntitlement> getDiscountEntitlements(CustomerAccount customerAccount){
		DiscountEntitlementList<DiscountEntitlement> discountEntitlements = new DiscountEntitlementList<DiscountEntitlement>();
		
		DiscountEntitlement discountEntitlement = new DiscountEntitlement();
		discountEntitlement.setAmount(10);
		discountEntitlement.setType("Gold Customer");		
		discountEntitlements.add(discountEntitlement);		
		
		discountEntitlement = new DiscountEntitlement();
		discountEntitlement.setAmount(12);
		discountEntitlement.setType("Off-Peak");		
		discountEntitlements.add(discountEntitlement);
				
		discountEntitlement = new DiscountEntitlement();
		discountEntitlement.setAmount(8);
		discountEntitlement.setType("Journey Award");		
		discountEntitlements.add(discountEntitlement);
		return discountEntitlements;
	}
	
	public FairAmount applyDiscount(FairAmount fairAmount, org.bpim.example.customerjourney.model.DiscountEntitlementList discountEntitlements){
	    Map<String, Object> jsonMap = (Map<String, Object>)discountEntitlements.get(discountEntitlementCounter);
	   
	    String jsonstr = "{";
	    for (Entry<String, Object> entry : jsonMap.entrySet()){
	    	jsonstr +="\"" + entry.getKey() + "\"" + ":\"" + entry.getValue() + "\",";
	    }
	    jsonstr = jsonstr.substring(0, jsonstr.length()-1);
	    jsonstr += "}";
	    DiscountEntitlement discountEntitlement = (DiscountEntitlement)DataPoolElementHelper.deserialize(jsonstr, DiscountEntitlement.class);
		fairAmount.setDiscounted(true);
		fairAmount.setPrice(fairAmount.getPrice() - discountEntitlement.getAmount());
		discountEntitlementCounter++;
		return fairAmount;
	}
	
	public PaymentResponse customerPayment(CustomerAccount customerAccount, FairAmount fairAmount){
		PaymentResponse response = new PaymentResponse();
		response.setObjectId("4443");
		response.setMessage("Can not connect to the payment gateway");
		response.setResponseCode("41");
		return response;
	}

}
