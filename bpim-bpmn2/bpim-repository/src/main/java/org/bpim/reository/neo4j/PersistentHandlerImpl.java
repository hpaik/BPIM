package org.bpim.reository.neo4j;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.bpim.model.base.v1.ElementBase;
import org.bpim.model.base.v1.MetaDataBase;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataTransition;
import org.bpim.model.execpath.v1.Activity;
import org.bpim.model.execpath.v1.TransitionBase;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.reository.PersistentHandler;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PersistentHandlerImpl implements PersistentHandler{
	
	protected static final Logger logger = LoggerFactory.getLogger(PersistentHandlerImpl.class);
	private ObjectMapper mapper = new ObjectMapper();
	private final String neo4jDbPath = "C:\\work\\TPNeo4jDB";
	
	
	
	public synchronized void storeProcessInstance(ProcessInstance processInstance){
		
		 GraphDatabaseService db= getGraphDatabaseService();
		 		 
		 try (Transaction tx = db.beginTx()) {			
			 
			 final String rootNodeType = "RootNode";
			 Node piNode = null;
			 Node execPathNode = null;
			 Node dataSnapshotGraphNode = null;
			 Node startNode = null;
			 Node dataSnapshotNode = null;
			 Node dataPoolNode = null;
			 piNode = createNode(db, processInstance, "ProcessInstance", "");
			 dataPoolNode = createNode(db, "Data Pool", "DataPoolRoot", "", rootNodeType);
			 createRelationship(piNode, dataPoolNode, "Data");
			 Node dataPoolElementNode = null;
			 for(DataPoolElement dataPoolElement: processInstance.getData().getDataSnapshotPool().getDataElement()){
				 dataPoolElementNode = createNode(db, dataPoolElement, "DataPoolElement", "");
				 createRelationship(dataPoolNode, dataPoolElementNode, "Contains");
			 }
			
			execPathNode = createNode(db, "Execution Path", "ExecutionPathRoot", "", rootNodeType);
			createRelationship(piNode, execPathNode, "Activities");
			
			Activity start = processInstance.getExecutionPath().getStart();
			startNode = createNode(db, start, "Activity", "");
			createRelationship(execPathNode, startNode, "Begins");
			transformExecPath(db, startNode, start.getOutputTransition());
			
			dataSnapshotGraphNode = createNode(db, "Data Snapshot Graphs", "DataSnapshotGraphRoot", "", rootNodeType);
			createRelationship(piNode, dataSnapshotGraphNode, "Snapshot Graph");
			
			for (DataSnapshotElement dse: processInstance.getData().getDataSnapshotGraphs().getDataSnapshotElement()){
				dataSnapshotNode = createNode(db, dse, "DataSnapshotElement", " Snapshot");
				createRelationship(dataSnapshotGraphNode, dataSnapshotNode, "Begins");
				transformSnapshotGraph(db, dataSnapshotNode, dse.getDataTransition());
			}							
			tx.success();
		} catch (Exception e) {			
			e.printStackTrace();
		} 
		
		db.shutdown();
	}
	
	@Override
	public void cleanRepository() {
		GraphDatabaseService db= getGraphDatabaseService();
		 try (Transaction tx = db.beginTx()) {
			 for (Relationship relationship: GlobalGraphOperations.at(db).getAllRelationships()){
				 relationship.delete();
			 }
			 for(Node node: GlobalGraphOperations.at(db).getAllNodes()){
				node.delete();
			 }
			 tx.success();
			 
		 }catch (Exception e) {			
			e.printStackTrace();
		 } 
		db.shutdown();
	}
	
	private GraphDatabaseService getGraphDatabaseService(){
		 File dbFile = new File(neo4jDbPath);			
		 GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		 return dbFactory.newEmbeddedDatabase(dbFile);
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
				createRelationship(parentNode, childNode, trans.getClass().getSimpleName());
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
		
	private Node createNode(GraphDatabaseService db, final String caption, final String labelName
			, final String postfix, final String type) throws Exception{
		
		Node node = db.createNode(
				new Label() {			
			@Override
			public String name() {
				if(caption != null){					
					return type;
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
			node.setProperty("Type", type);
		}
		return node;
	}
		
	private Node createNode(GraphDatabaseService db, final ElementBase bpimElement, final String labelName, String postfix) throws Exception{
		
		Node node = createNode(db, bpimElement.getName(), labelName, postfix, bpimElement.getClass().getSimpleName());
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
		Relationship relationship = from.createRelationshipTo(to, new RelationshipType() {
			
			@Override
			public String name() {
				
				return relationshipType;
			}
		});
		relationship.setProperty("Caption", relationshipType.replace("Transition", ""));
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
