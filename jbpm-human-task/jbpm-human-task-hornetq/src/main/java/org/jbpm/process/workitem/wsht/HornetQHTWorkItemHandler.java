/*
 * Copyright 2012 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.process.workitem.wsht;

import org.jbpm.task.utils.OnErrorAction;
import org.drools.runtime.KnowledgeRuntime;
import org.jbpm.task.service.AsyncTaskServiceWrapper;
import org.jbpm.task.service.TaskClient;
import org.drools.SystemEventListenerFactory;
import org.jbpm.task.service.hornetq.HornetQTaskClientConnector;
import org.jbpm.task.service.hornetq.HornetQTaskClientHandler;
/**
 *
 * This class provides the default configurations for a HornetQ WorkItem Handler
 */
public class HornetQHTWorkItemHandler extends GenericHTWorkItemHandler{

    public HornetQHTWorkItemHandler(KnowledgeRuntime session) {
        super(session);
        init();
    }

    public HornetQHTWorkItemHandler(KnowledgeRuntime session, OnErrorAction action) {
        super(session, action);
        init();
    }

    private void init(){
        setClient(new AsyncTaskServiceWrapper(new TaskClient(new HornetQTaskClientConnector("client 1",
                new HornetQTaskClientHandler(SystemEventListenerFactory.getSystemEventListener())))));
        if(getPort() <= 0){
            setPort(5446);
        }
        if(getIpAddress() == null || getIpAddress().equals("")){
            setIpAddress("127.0.0.1");
        }
    }
   
    
}
