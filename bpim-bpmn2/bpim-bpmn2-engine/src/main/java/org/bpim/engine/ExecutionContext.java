package org.bpim.engine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;




import org.bpim.model.v1.CompositeProcessInstance;
import org.bpim.model.v1.ObjectFactory;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class ExecutionContext {
	private static Map<String, ProcessInstanceContext> processInstances = null;
	
	private static CompositeProcessInstance compositeProcessInstance;
	
	public ExecutionContext(){
		if (processInstances == null){
			processInstances = new HashMap<String, ProcessInstanceContext>();
		}
	}
	
	public CompositeProcessInstance startCompositeProcessInstance(String name){
		org.bpim.model.v1.ObjectFactory objectFatory = new ObjectFactory();
		compositeProcessInstance = objectFatory.createCompositeProcessInstance();
		compositeProcessInstance.setName(name);
		compositeProcessInstance.setId(UniqueIdGenerator.nextId());
		org.bpim.model.data.v1.ObjectFactory dataObjectFatory = new org.bpim.model.data.v1.ObjectFactory();
		compositeProcessInstance.setDataSnapshotPool(dataObjectFatory.createDataSnapshotPool());
		return compositeProcessInstance;
	}
	
	public ProcessInstanceContext getProcessInstanceContext(String bpmnInstanceId, String bpmnInstanceName){
		ProcessInstanceContext processInstanceContext = null;
		
		if(processInstances.containsKey(bpmnInstanceId)){
			processInstanceContext = getProcessInstanceContext(bpmnInstanceId);
		}else{
			processInstanceContext = new ProcessInstanceContext();
			org.bpim.model.v1.ObjectFactory objectFatory = new ObjectFactory();
			ProcessInstance processInstance = objectFatory.createProcessInstance();
			processInstance.setId(UniqueIdGenerator.nextId());
			processInstance.setName(bpmnInstanceName);
			processInstance.setMappingCorrelationId(bpmnInstanceId);
			processInstanceContext.setProcessInstance(processInstance);
			if (compositeProcessInstance != null){
				compositeProcessInstance.getProcessInstance().add(processInstance);
				processInstanceContext.setCompositeProcessInstance(compositeProcessInstance);
			}
			processInstances.put(bpmnInstanceId, processInstanceContext);			
		}
		return processInstanceContext;
	}
	
	public ProcessInstanceContext getProcessInstanceContext(String bpmnInstanceId){
		return processInstances.get(bpmnInstanceId);		
	}
	
	public void storeProcessInstance(){
		 File dbFile = new File("C:\\work\\TPNeo4jDB");
		 if (dbFile.exists()){
			 dbFile.delete();
			 dbFile.mkdir();
		 }
		 GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		 GraphDatabaseService db= dbFactory.newEmbeddedDatabase(dbFile);
		 /*
		 try (Transaction tx = db.beginTx()) {
				// Perform DB operations
			 
			 Node javaNode = db.createNode(Tutorials.JAVA);
			 
			 Node scalaNode = db.createNode(Tutorials.SCALA);
				tx.success();
			javaNode.setProperty("TutorialID", "JAVA001");
			javaNode.setProperty("Title", "Learn Java");
			javaNode.setProperty("NoOfChapters", "25");
			javaNode.setProperty("Status", "Completed");	
				
			scalaNode.setProperty("TutorialID", "SCALA001");
			scalaNode.setProperty("Title", "Learn Scala");
			scalaNode.setProperty("NoOfChapters", "20");
			scalaNode.setProperty("Status", "Completed");
		 }	
		 */
		 
		 Node piNode = null;
		for(final ProcessInstance pi :compositeProcessInstance.getProcessInstance()){
			
			try (Transaction tx = db.beginTx()) {
				piNode = db.createNode();
				piNode.addLabel(new Label() {
					
					@Override
					public String name() {
						
						return pi.getName();
					}
				});
				
			}
		}
		db.shutdown();
	}
	
	
	public enum Tutorials implements Label {
		JAVA,SCALA,SQL,NEO4J;
	}

}
