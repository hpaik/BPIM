package org.bpim.objects;

import org.bpim.engine.ExecutionContext;
import org.bpim.engine.ProcessInstanceContext;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.objects.model.JourneyDetails;
import org.bpim.transformer.util.DataPoolElementHelper;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.google.gson.Gson;


public class SendTaskHandler  implements WorkItemHandler {
    
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        
    	ExecutionContext executionContext = new ExecutionContext();
        ProcessInstanceContext processInstanceContext = executionContext.getProcessInstanceContext(
        		 String.valueOf(workItem.getProcessInstanceId()));
        
        DataPoolElement journeyDetailsElement = processInstanceContext.getDataPoolElementByType(JourneyDetails.class.getName());
        JourneyDetails journeyDetails = DataPoolElementHelper.deserialize(journeyDetailsElement);
        		
        		//gson.fromJson(journeyDetailsElement.getDataObject().toString(), JourneyDetails.class);        
        workItem.getParameters().put("journeyDetails", journeyDetails);
        
        DataPoolElement customerAccountElement = processInstanceContext.getDataPoolElementByType(CustomerAccount.class.getName());
        CustomerAccount customerAccount = DataPoolElementHelper.deserialize(customerAccountElement);
        		//gson.fromJson(customerAccountElement.getDataObject().toString(), CustomerAccount.class);        
        workItem.getParameters().put("customerAccount", customerAccount);
        
        manager.completeWorkItem(workItem.getId(), null);
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        // Do nothing, cannot be aborted
    }

}
