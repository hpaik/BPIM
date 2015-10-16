/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.bpim.objects;

import org.bpim.objects.model.ImageProcessingResult;
import org.kie.api.runtime.process.*;

public class TestWorkItemHandler implements WorkItemHandler {

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {    	
                        
        ImageProcessingResult imageProcessingResult = (ImageProcessingResult) workItem.getParameter("Parameter");
        imageProcessingResult.setPlateNumber("111111");
        imageProcessingResult.setConfidenceRate(10);
        imageProcessingResult.setReviewedByHuman(true);
        imageProcessingResult.setSuccessful(true);                       
        workItem.getResults().put("Result", imageProcessingResult);        
        
        manager.completeWorkItem(workItem.getId(), null);       
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
    	manager.abortWorkItem(workItem.getId());
    }   

}
