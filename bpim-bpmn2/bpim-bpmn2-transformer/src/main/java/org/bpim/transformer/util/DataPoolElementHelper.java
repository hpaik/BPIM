package org.bpim.transformer.util;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
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

}
