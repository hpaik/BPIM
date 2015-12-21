package org.bpim.example.customerjourney.handler;

import org.bpim.engine.ExecutionContext;
import org.bpim.engine.ProcessInstanceContext;
import org.bpim.example.customerjourney.model.CustomerAccount;
import org.bpim.example.customerjourney.model.JourneyDetails;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.transformer.util.DataPoolElementHelper;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class CustomerAccountSendTaskHandler  implements WorkItemHandler {
    
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        
    	ExecutionContext executionContext = new ExecutionContext();
        ProcessInstanceContext processInstanceContext = executionContext.getProcessInstanceContext(
        		 String.valueOf(workItem.getProcessInstanceId()));
        
        DataPoolElement journeyDetailsElement = processInstanceContext.getDataPoolElementByType(JourneyDetails.class.getName());
        JourneyDetails journeyDetails = DataPoolElementHelper.deserialize(journeyDetailsElement);
        
        workItem.getParameters().put("journeyDetails", journeyDetails);
        
        DataPoolElement customerAccountElement = processInstanceContext.getDataPoolElementByType(CustomerAccount.class.getName());
        CustomerAccount customerAccount = DataPoolElementHelper.deserialize(customerAccountElement);
        workItem.getParameters().put("customerAccount", customerAccount);
        
        workItem.getParameters().put("SourceProcessId", workItem.getProcessInstanceId());
        
        manager.completeWorkItem(workItem.getId(), null);
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        // Do nothing, cannot be aborted
    }

}
