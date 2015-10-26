package org.bpim.objects.service;

import org.bpim.objects.CustomerAccount;
import org.bpim.objects.FairAmount;
import org.bpim.objects.model.JourneyDetails;

public class CustomerPaymentService {
	
	public FairAmount calculateFareAmount(CustomerAccount customerAccount
			, JourneyDetails journeyDetails){
		FairAmount fairAmount = new FairAmount();
		fairAmount.setDiscounted(false);
		fairAmount.setPrice(150);
		return fairAmount;
	}
	
	public void getDiscountEntitlements(){
		
	}

}
