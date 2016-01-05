package org.bpim.transformer.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;
import org.bpim.model.data.v1.DataSnapshotPool;
import org.bpim.model.v1.CompositeProcessInstance;
import org.bpim.model.v1.ProcessInstance;
import org.bpim.transformer.base.BPIMDataObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class DataPoolElementHelper {
	
	private static ObjectMapper mapper = new ObjectMapper();		
	
	public static <T> DataPoolElement create(T object, String name){
		org.bpim.model.data.v1.ObjectFactory objectFactory = new org.bpim.model.data.v1.ObjectFactory();
		DataPoolElement dataPoolElement = null;		
		if(object.getClass().isArray() || object instanceof List){
			dataPoolElement = objectFactory.createDataItemArray();			    										
		}else{
			dataPoolElement = objectFactory.createDataItem();			
		}
		
		try {
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			dataPoolElement.setDataObject(mapper.writeValueAsString(object));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		dataPoolElement.setDataObjectType(object.getClass().getName());
		dataPoolElement.setName(name);
		dataPoolElement.setCreationDateTime((new Date()).toString());
		if (object instanceof BPIMDataObject){
			dataPoolElement.setMappingCorrelationId(((BPIMDataObject)object).getObjectId());
		}
		dataPoolElement.setId(UniqueIdGenerator.nextId());
		
		return dataPoolElement;
	}
	
	public static DataPoolElement addToPool(DataPoolElement dataPoolElement, DataSnapshotPool dataSnapshotPool){
		int version = 1;
		String id = null;
		for (DataPoolElement tmpDataPoolElement : dataSnapshotPool.getDataElement()){
			if (tmpDataPoolElement.getMappingCorrelationId() != null && 
					dataPoolElement.getMappingCorrelationId() != null &&
					dataPoolElement.getDataObjectType().equals(tmpDataPoolElement.getDataObjectType()) &&
					tmpDataPoolElement.getMappingCorrelationId().equals(dataPoolElement.getMappingCorrelationId())){
				version++;
				id = tmpDataPoolElement.getId();
			}
		}
		dataPoolElement.setVersion(new Integer(version));
		dataPoolElement.setName(dataPoolElement.getName() + " " + dataPoolElement.getVersion());
		dataSnapshotPool.getDataElement().add(dataPoolElement);
		return dataPoolElement;
	}
	
	public static Object deserialize(String jsonString, Class cls){
		Object result = null;
		try {
			result = mapper.readValue(jsonString, cls);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T deserialize(DataPoolElement dataPoolElement){		
		
		try {

			Class cls = Class.forName(dataPoolElement.getDataObjectType());
			Object result = null;
			try {
				result = mapper.readValue((String)dataPoolElement.getDataObject(), cls);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			if (result instanceof BPIMDataObject){
				((BPIMDataObject)result).setObjectId(dataPoolElement.getMappingCorrelationId());
			}
			return (T) result;
		} catch (JsonSyntaxException e) {			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
		return null;
	}

}
