package org.bpim.engine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.bpim.model.v1.CompositeProcessInstance;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.Transformer;
import org.bpim.transformer.factory.TranformerFactory;
import org.jbpm.workflow.instance.node.CompositeContextNodeInstance;
import org.jbpm.workflow.instance.node.EndNodeInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BPIMExecutionEngine {
	
	protected static final Logger logger = LoggerFactory.getLogger(BPIMExecutionEngine.class);
	private static ExecutionContext executionContext = null;
	
	static {
		executionContext = new ExecutionContext();
	}
		
	public static void process(org.jbpm.workflow.instance.NodeInstance nodeInstance){
		String transformerUnitType = nodeInstance.getClass().getSimpleName(); 
		if (nodeInstance instanceof WorkItemNodeInstance){
			WorkItemNodeInstance workItemNodeInstance  = (WorkItemNodeInstance) nodeInstance;
			transformerUnitType = workItemNodeInstance.getWorkItem().getName().replace(" ", "");
			transformerUnitType += "NodeInstance";
		}
		
		if (nodeInstance instanceof EndNodeInstance){
			if (nodeInstance.getNodeInstanceContainer() instanceof CompositeContextNodeInstance){
				return;
			}
		}
		
		Transformer transformer = TranformerFactory.createTransformer(transformerUnitType);
		
		TransformationResult transformationResult = transformer.transform(nodeInstance);
		
		ProcessInstanceContext processInstanceContext  = executionContext.getProcessInstanceContext(
				  String.valueOf(nodeInstance.getProcessInstance().getId())
				, nodeInstance.getProcessInstance().getProcessName());
		
		processInstanceContext.addTransformationResult(transformationResult);
		
		//ProcessInstance processInstance = processInstanceContext.getProcessInstance();
		CompositeProcessInstance compositeProcessInstance = processInstanceContext.getCompositeProcessInstance();
		
		 try {

				JAXBContext jaxbContext = JAXBContext.newInstance(ProcessInstance.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				
//				jaxbMarshaller.marshal(new JAXBElement<CompositeProcessInstance>(new QName("uri","CompositeProcessInstance")
//					, CompositeProcessInstance.class, compositeProcessInstance), System.out);								


			} catch (JAXBException e) {
				logger.error("Can not serialize process instance", e);
			}
		 
		
		 
	}
	

}
