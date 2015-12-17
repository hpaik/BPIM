package org.bpim.engine;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


























import org.bpim.model.base.v1.ElementBase;
import org.bpim.model.base.v1.MetaDataBase;
import org.bpim.model.base.v1.Server;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.model.execpath.v1.Activity;
import org.bpim.model.execpath.v1.Start;
import org.bpim.model.execpath.v1.TransitionBase;
import org.bpim.model.v1.CompositeProcessInstance;
import org.bpim.model.v1.ObjectFactory;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.util.MetaDataHelper;
import org.bpim.transformer.util.UniqueIdGenerator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecutionContext {
	private static Map<String, ProcessInstanceContext> processInstances = null;
	
	private static CompositeProcessInstance compositeProcessInstance;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
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
			processInstance.setServer(MetaDataHelper.createServer());
			processInstance.setCreationDateTime((new Date()).toString());
			processInstance.setState("STARTED");
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

		 GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		 GraphDatabaseService db= dbFactory.newEmbeddedDatabase(dbFile);
		 		 
		 try (Transaction tx = db.beginTx()) {
			 for (Relationship relationship: GlobalGraphOperations.at(db).getAllRelationships()){
				 relationship.delete();
			 }
			 for(Node node: GlobalGraphOperations.at(db).getAllNodes()){
				node.delete();
			 }
			 
			 
			 Node compositPINode = createNode(db, compositeProcessInstance, "CompositeProcessInstance", "");
			 
			 Node dataPoolNode = createNode(db, "Data Pool", "DataPoolRoot", "");
			 createRelationship(compositPINode, dataPoolNode, "Data");
			 Node dataPoolElementNode = null;
			 for(DataPoolElement dataPoolElement: compositeProcessInstance.getDataSnapshotPool().getDataElement()){
				 dataPoolElementNode = createNode(db, dataPoolElement, "DataPoolElement", "");
				 createRelationship(dataPoolNode, dataPoolElementNode, "Contains");
			 }
			 
			 Node piNode = null;
			 Node execPathNode = null;
			 Node dataSnapshotGraphNode = null;
			 Node startNode = null;
			 Node dataSnapshotNode = null;
			for(final ProcessInstance pi :compositeProcessInstance.getProcessInstance()){							
				piNode = createNode(db, pi, "ProcessInstance", "");
				createRelationship(compositPINode, piNode, "Contains");												
				
				execPathNode = createNode(db, "Execution Path", "ExecutionPathRoot", "");
				createRelationship(piNode, execPathNode, "Activities");
				
				Activity start = pi.getExecutionPath().getStart();
				startNode = createNode(db, start, "Activity", "");
				createRelationship(execPathNode, startNode, "Begins");
				transformExecPath(db, startNode, start.getOutputTransition());
				
				dataSnapshotGraphNode = createNode(db, "Data Snapshot Graphs", "DataSnapshotGraphRoot", "");
				createRelationship(piNode, dataSnapshotGraphNode, "Snapshot Graph");
				
				for (DataSnapshotElement dse: pi.getData().getDataSnapshotGraphs().getDataSnapshotElement()){
					dataSnapshotNode = createNode(db, dse, "DataSnapshotElement", " Snapshot");
					createRelationship(dataSnapshotGraphNode, dataSnapshotNode, "Begins");
					transformSnapshotGraph(db, dataSnapshotNode, dse.getDataTransition());
				}				
			}			
			tx.success();
		} catch (Exception e) {			
			e.printStackTrace();
		} 
		
		db.shutdown();
	}
	
	private void transformSnapshotGraph(GraphDatabaseService db, Node parentNode, List<DataTransition> dtList) throws Exception{
		String snapshotPostfix = " Snapshot";
		String label = "DataSnapshotElement";
		DataSnapshotElement child = null;
		Node childNode = null;
		for (DataTransition dt: dtList){
			child = dt.getDataSnapshotElement();
			if (child != null){
				childNode = getNode(db, child, label, snapshotPostfix);
				if (childNode == null){
					childNode = createNode(db, child, label, snapshotPostfix);
				}

				Iterable<Relationship> relationships = getRelationShips(parentNode, dt.getName());
				boolean flag = false;
				for (Relationship relationship: relationships){
					if (relationship.getEndNode().getId() == childNode.getId()){
						flag = true;
					}
				}
				if (!flag){
					createRelationship(parentNode, childNode, dt.getName());
				}
				if (!child.getDataTransition().isEmpty()){
					transformSnapshotGraph(db, childNode, child.getDataTransition());
				}
			}
		}
	}
	
	private void transformExecPath(GraphDatabaseService db, Node parentNode, List<TransitionBase> transList) throws Exception{
		Node childNode = null;
		Activity child = null;
		for (TransitionBase trans: transList){
			child = trans.getTo();
			if (child != null){
				childNode = createNode(db, child, "Activity", "");
				createRelationship(parentNode, childNode, "Transition");
				if (!child.getOutputTransition().isEmpty()){
					transformExecPath(db, childNode, child.getOutputTransition());
				}
			}
		}
	}
	
	private Node getNode(GraphDatabaseService db, final ElementBase bpimElement, final String labelName, final String postfix) throws Exception{
		Label label = new Label() {			
			@Override
			public String name() {
				return labelName;
			}
		};
		return db.findNode(label, "Id", bpimElement.getId());
	}
	
	
	private Node createNode(GraphDatabaseService db, final String caption, final String labelName, final String postfix) throws Exception{
		
		Node node = db.createNode(
				new Label() {			
			@Override
			public String name() {
				if(caption != null){					
					return labelName;
				}else{
					return "Empty";
				}
			}
		});
		if(caption != null){
			String normalizedName = caption;
			int lenghtLimit = 15;
			if (normalizedName.length() > lenghtLimit){
				lenghtLimit--;
				if (!normalizedName.substring(0, lenghtLimit).contains(" "))
					normalizedName = normalizedName.substring(0, lenghtLimit) + " " + caption.substring(lenghtLimit);
			}
			node.setProperty("Caption", normalizedName);
			node.setProperty("Name", normalizedName);
		}
		return node;
	}
	
	
	
	private Node createNode(GraphDatabaseService db, final ElementBase bpimElement, final String labelName, String postfix) throws Exception{
		
		Node node = createNode(db, bpimElement.getName(), labelName, postfix);
		Object value = null;
		
		for (Method method :bpimElement.getClass().getMethods()){
			try{
				
				if (method.getName().startsWith("get") && 
						(method.getReturnType().isPrimitive() || 
					     method.getReturnType().getName().equals(String.class.getName()) ||
					     method.getReturnType().getName().equals(Object.class.getName()) ||
					     MetaDataBase.class.isAssignableFrom(method.getReturnType())
						)
				   ){
					value = method.invoke(bpimElement, null);
					if (value instanceof MetaDataBase){
						mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
						value = mapper.writeValueAsString(value);
					}
					if(value != null){
						node.setProperty(method.getName().replace("get", "")
								, value);
					}
				}
			}catch (Exception exp){
				throw exp;
			}
		}
		
		return node;
	}
	
	private void createRelationship(Node from, Node to, final String relationshipType){
		from.createRelationshipTo(to, new RelationshipType() {
			
			@Override
			public String name() {
				
				return relationshipType;
			}
		});
	}
	
	private Iterable<Relationship> getRelationShips(Node node, final String relationshipType){
		RelationshipType type = new RelationshipType() {
			
			@Override
			public String name() {
				
				return relationshipType;
			}
		};
		return node.getRelationships(type, Direction.OUTGOING);
	}
}
