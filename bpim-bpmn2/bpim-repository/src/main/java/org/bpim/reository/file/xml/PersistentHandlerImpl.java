package org.bpim.reository.file.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.bpim.model.v1.ProcessInstance;
import org.bpim.reository.PersistentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PersistentHandlerImpl implements PersistentHandler{
	
	protected static final Logger logger = LoggerFactory.getLogger(PersistentHandlerImpl.class);
	
	public void storeProcessInstance(ProcessInstance processInstance){
		File file = new File(processInstance.getName() + ".xml");
		
		try {
			OutputStream outputStream = new FileOutputStream(file);	
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessInstance.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			jaxbMarshaller.marshal(new JAXBElement<ProcessInstance>(new QName("uri","ProcessInstance")
				, ProcessInstance.class, processInstance), outputStream);								


			} catch (Exception e) {
				logger.error("Can not serialize process instance", e);
			}
	}

	@Override
	public void cleanRepository() {
		
	}

}
