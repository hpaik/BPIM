package org.bpim.objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bpim.engine.ExecutionContext;
import org.bpim.engine.ProcessInstanceContext;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.objects.model.JourneyDetails;
import org.bpim.transformer.util.DataPoolElementHelper;
import org.jbpm.bpmn2.handler.WorkItemHandlerRuntimeException;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ExtendedServiceTaskHandler implements WorkItemHandler {
	private static final Logger logger = LoggerFactory.getLogger(ExtendedServiceTaskHandler.class);
	    
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
            	
    	ExecutionContext executionContext = new ExecutionContext();
        ProcessInstanceContext processInstanceContext = executionContext.getProcessInstanceContext(
        		 String.valueOf(workItem.getProcessInstanceId()));
        
    	
    	String service = (String) workItem.getParameter("Interface");
        String interfaceImplementationRef = (String) workItem.getParameter("interfaceImplementationRef"); 
        String operation = (String) workItem.getParameter("Operation");
        String parameterTypesInput = (String) workItem.getParameter("ParameterType");
        workItem.getParameters().remove("Parameter");
       // Object parameter = workItem.getParameter("Parameter");
        String[] paramTypes = null;
        if(parameterTypesInput != null){
        	paramTypes = parameterTypesInput.split(",");        	
        }
        
        String[] services = {service, interfaceImplementationRef};
        Class<?> c = null;
        
        for(String serv : services) {
            try {
                c = Class.forName(serv);
                break;
            } catch (ClassNotFoundException cnfe) {
                if(serv.compareTo(services[services.length - 1]) == 0) {
                    handleException(cnfe, service, interfaceImplementationRef, operation, parameterTypesInput, null);
                }
            }
        }
        
        try {
            Object instance = c.newInstance();
            Class<?>[] classes = null;
            Object[] params = null;
            if (paramTypes != null) {
            	classes = new Class<?>[paramTypes.length] ;
            	int counter = 0;
            	DataPoolElement dataPoolElement = null;
            	params = new Object[paramTypes.length];
            	for (String paramType: paramTypes){
            		classes[counter] = Class.forName(paramType);
            		dataPoolElement = processInstanceContext.getDataPoolElementByType(paramType);            		
            		params[counter] = DataPoolElementHelper.deserialize(dataPoolElement);
            		workItem.getParameters().put("Param" + counter, params[counter]);
            		counter++;
            	}
//                classes = new Class<?>[] {
//                    Class.forName(parameterType)
//                };
//                params = new Object[] {
//                    parameter
//                };
            }
           
            Method method = c.getMethod(operation, classes);
            Object result = method.invoke(instance, params);
            Map<String, Object> results = new HashMap<String, Object>();
            results.put("Result", result);
            manager.completeWorkItem(workItem.getId(), results);
        } catch (ClassNotFoundException cnfe) {
            handleException(cnfe, service, interfaceImplementationRef, operation, parameterTypesInput, null);
        } catch (InstantiationException ie) {
            handleException(ie, service, interfaceImplementationRef, operation, parameterTypesInput, null);
        } catch (IllegalAccessException iae) {
            handleException(iae, service, interfaceImplementationRef, operation, parameterTypesInput, null);
        } catch (NoSuchMethodException nsme) {
            handleException(nsme, service, interfaceImplementationRef, operation, parameterTypesInput, null);
        } catch (InvocationTargetException ite) {
            handleException(ite, service, interfaceImplementationRef, operation, parameterTypesInput, null);
        } catch( Throwable cause ) { 
            handleException(cause, service, interfaceImplementationRef, operation, parameterTypesInput, null);
        }
    }

    private void handleException(Throwable cause, String service, String interfaceImplementationRef, String operation, String paramType, Object param) { 
        logger.debug("Handling exception {} inside service {} or {} and operation {} with param type {} and value {}",
                cause.getMessage(), service, operation, paramType, param);
        WorkItemHandlerRuntimeException wihRe;
        if( cause instanceof InvocationTargetException ) { 
            Throwable realCause = cause.getCause();
            wihRe = new WorkItemHandlerRuntimeException(realCause);
            wihRe.setStackTrace(realCause.getStackTrace());
        } else { 
            wihRe = new WorkItemHandlerRuntimeException(cause);
            wihRe.setStackTrace(cause.getStackTrace());
        }
        wihRe.setInformation("Interface", service);
        wihRe.setInformation("InterfaceImplementationRef", interfaceImplementationRef);
        wihRe.setInformation("Operation", operation);
        wihRe.setInformation("ParameterType", paramType);
        wihRe.setInformation("Parameter", param);
        wihRe.setInformation(WorkItemHandlerRuntimeException.WORKITEMHANDLERTYPE, this.getClass().getSimpleName());
        throw wihRe;
        
    }
    
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        // Do nothing, cannot be aborted
    }
}
