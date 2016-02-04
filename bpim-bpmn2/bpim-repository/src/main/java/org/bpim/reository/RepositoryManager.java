package org.bpim.reository;


import org.bpim.model.v1.ProcessInstance;

public class RepositoryManager {
	
	private static RepositoryManager repositoryManager = null;
	
	
	private RepositoryManager(){
		
	}
	
	public static RepositoryManager getInstance(){
		if (repositoryManager == null){
			repositoryManager = new RepositoryManager();
		}
		return repositoryManager;
	}
	
	
	public synchronized void storeProcessInstance(ProcessInstance processInstance){
			
		org.bpim.reository.file.xml.PersistentHandlerImpl xmlPersistentHandler = new org.bpim.reository.file.xml.PersistentHandlerImpl();
		xmlPersistentHandler.storeProcessInstance(processInstance);
		
		org.bpim.reository.neo4j.PersistentHandlerImpl ne4jxmlPersistentHandler = new org.bpim.reository.neo4j.PersistentHandlerImpl();
		ne4jxmlPersistentHandler.storeProcessInstance(processInstance);
	}
	

}
