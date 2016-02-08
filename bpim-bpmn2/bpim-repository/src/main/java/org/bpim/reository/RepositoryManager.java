package org.bpim.reository;


import org.bpim.model.v1.ProcessInstance;

public class RepositoryManager {
	
	private static RepositoryManager repositoryManager = null;
	
	private org.bpim.reository.file.xml.PersistentHandlerImpl xmlPersistentHandler;
	private org.bpim.reository.neo4j.PersistentHandlerImpl ne4jxmlPersistentHandler; 
	
	private RepositoryManager(){
		xmlPersistentHandler = new org.bpim.reository.file.xml.PersistentHandlerImpl();
		ne4jxmlPersistentHandler = new org.bpim.reository.neo4j.PersistentHandlerImpl();
	}
	
	public static RepositoryManager getInstance(){
		if (repositoryManager == null){
			repositoryManager = new RepositoryManager();
		}
		return repositoryManager;
	}
	
	
	public synchronized void storeProcessInstance(ProcessInstance processInstance){
					
		xmlPersistentHandler.storeProcessInstance(processInstance);				
		ne4jxmlPersistentHandler.storeProcessInstance(processInstance);
	}
	
	public synchronized void cleanRepository(){
		xmlPersistentHandler.cleanRepository();
		ne4jxmlPersistentHandler.cleanRepository();
	}
	

}
