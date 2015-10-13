package org.bpim.transformer.util;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.base.BPIMDataObject;

import com.google.gson.Gson;

public class DataPoolElementHelper {
	
	public static DataPoolElement create(Object object, String name){
		org.bpim.model.data.v1.ObjectFactory objectFactory = new org.bpim.model.data.v1.ObjectFactory();
		DataPoolElement dataPoolElement = null;
		Gson gson = new Gson();
		if(object.getClass().isArray()){
			dataPoolElement = objectFactory.createDataItemArray();			    				
			dataPoolElement.setDataObject(gson.toJson(object));
			dataPoolElement.setName(name);
			
		}else{
			dataPoolElement = objectFactory.createDataItem();    			
			dataPoolElement.setDataObject(gson.toJson(object));
			dataPoolElement.setName(name);
		}
		
		dataPoolElement.setMappingCorrelationId(((BPIMDataObject)object).getObjectId());		
		dataPoolElement.setId(UniqueIdGenerator.nextId());
		
		return dataPoolElement;
	}
	
	public static DataPoolElement addToPool(DataPoolElement dataPoolElement, ProcessInstance processInstance){
		int version = 1;
		String id = null;
		for (DataPoolElement tmpDataPoolElement : processInstance.getData().getDataSnapshotPool().getDataElement()){
			if (tmpDataPoolElement.getMappingCorrelationId() != null && 
					dataPoolElement.getMappingCorrelationId() != null &&
					tmpDataPoolElement.getMappingCorrelationId().equals(dataPoolElement.getMappingCorrelationId())){
				version++;
				id = tmpDataPoolElement.getId();
			}
		}
//		if (version == 1){
//			dataPoolElement.setId(UniqueIdGenerator.nextId());
//		}else{
//			dataPoolElement.setId(id);
//		}
		dataPoolElement.setVersion(new Integer(version));
		processInstance.getData().getDataSnapshotPool().getDataElement().add(dataPoolElement);
		return dataPoolElement;
	}

}
