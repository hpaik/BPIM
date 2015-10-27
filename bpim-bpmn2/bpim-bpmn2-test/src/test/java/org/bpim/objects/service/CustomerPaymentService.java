package org.bpim.objects.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bpim.objects.CustomerAccount;
import org.bpim.objects.DiscountEntitlement;
import org.bpim.objects.ETollDataObjectList;
import org.bpim.objects.FairAmount;
import org.bpim.objects.model.JourneyDetails;
import org.bpim.transformer.util.DataPoolElementHelper;

public class CustomerPaymentService {
	
	public FairAmount calculateFareAmount(CustomerAccount customerAccount
			, JourneyDetails journeyDetails){
		FairAmount fairAmount = new FairAmount();
		fairAmount.setDiscounted(false);
		fairAmount.setPrice(150);
		return fairAmount;
	}
	
	public ETollDataObjectList<DiscountEntitlement> getDiscountEntitlements(CustomerAccount customerAccount){
		ETollDataObjectList<DiscountEntitlement> discountEntitlements = new ETollDataObjectList<DiscountEntitlement>();
		
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
	
	public FairAmount applyDiscount(FairAmount fairAmount, org.bpim.objects.ETollDataObjectList discountEntitlements){
	    Map<String, Object> jsonMap = (Map<String, Object>)discountEntitlements.get(counter);
	   
	    String jsonstr = "{";
	    for (Entry<String, Object> entry : jsonMap.entrySet()){
	    	jsonstr +="\"" + entry.getKey() + "\"" + ":\"" + entry.getValue() + "\",";
	    }
	    jsonstr = jsonstr.substring(0, jsonstr.length()-1);
	    jsonstr += "}";
	    DiscountEntitlement discountEntitlement = (DiscountEntitlement)DataPoolElementHelper.deserialize(jsonstr, DiscountEntitlement.class);
		fairAmount.setDiscounted(true);
		fairAmount.setPrice(fairAmount.getPrice() - discountEntitlement.getAmount());
		counter++;
		return fairAmount;
	}
	
	private static int counter = 0;

}
