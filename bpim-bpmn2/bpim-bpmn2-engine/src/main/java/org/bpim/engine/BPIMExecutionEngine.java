package org.bpim.engine;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.base.TransformationResult;
import org.bpim.transformer.base.Transformer;
import org.bpim.transformer.factory.TranformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BPIMExecutionEngine {
	
	protected static final Logger logger = LoggerFactory.getLogger(BPIMExecutionEngine.class);
	private static ExecutionContext executionContext = null;
	
	static {
		executionContext = new ExecutionContext();
	}
	
	@SuppressWarnings("unused")
	public static void process(org.jbpm.workflow.instance.NodeInstance nodeInstance){		
		
		Transformer transformer = TranformerFactory.createTransformer(nodeInstance.getClass().getSimpleName());
		
		TransformationResult transformationResult = transformer.transform(nodeInstance);
		
		ProcessInstanceContext processInstanceContext  = executionContext.getProcessInstanceContext(
				nodeInstance.getProcessInstance().getProcessId()
				, nodeInstance.getProcessInstance().getProcessName());
		
		processInstanceContext.addTransformationResult(transformationResult);
		
		ProcessInstance processInstance = processInstanceContext.getProcessInstance();
		
		 try {

				JAXBContext jaxbContext = JAXBContext.newInstance(ProcessInstance.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				
				jaxbMarshaller.marshal(new JAXBElement<ProcessInstance>(new QName("uri","ProcessInstance"), ProcessInstance.class, processInstance), System.out);

				//jaxbMarshaller.marshal(processInstance, System.out);

			} catch (JAXBException e) {
				logger.error("Can not serialize process instance", e);
			}

		
		int i = 0;
	}

}
