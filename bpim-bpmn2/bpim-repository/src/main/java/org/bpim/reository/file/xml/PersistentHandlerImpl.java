package org.bpim.reository.file.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.bpim.model.v1.ProcessInstance;
import org.bpim.reository.PersistentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PersistentHandlerImpl implements PersistentHandler{
	
	protected static final Logger logger = LoggerFactory.getLogger(PersistentHandlerImpl.class);
	
	public void storeProcessInstance(ProcessInstance processInstance){
		 try {
				
				JAXBContext jaxbContext = JAXBContext.newInstance(ProcessInstance.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				
				jaxbMarshaller.marshal(new JAXBElement<ProcessInstance>(new QName("uri","ProcessInstance")
					, ProcessInstance.class, processInstance), System.out);								


			} catch (JAXBException e) {
				logger.error("Can not serialize process instance", e);
			}
	}

}
