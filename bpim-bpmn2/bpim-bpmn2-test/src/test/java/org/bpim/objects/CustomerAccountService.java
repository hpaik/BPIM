package org.bpim.objects;

public class CustomerAccountService {
	
	public CustomerAccount updateCustomerAccount(CustomerAccount customerAccount){
		customerAccount.setPlateNum("xxxxx");
		return customerAccount;
	}

}
