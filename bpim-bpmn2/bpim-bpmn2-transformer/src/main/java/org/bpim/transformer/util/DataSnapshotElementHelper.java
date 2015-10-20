package org.bpim.transformer.util;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;

public class DataSnapshotElementHelper {
	
	public static DataSnapshotElement create(DataPoolElement dataPoolElement){
		org.bpim.model.data.v1.ObjectFactory dataObjectFactory = new org.bpim.model.data.v1.ObjectFactory();
		DataSnapshotElement targetDataSnapshotElement = null;
		targetDataSnapshotElement = dataObjectFactory.createDataSnapshotElement();
		targetDataSnapshotElement.setEmpty(false);
		targetDataSnapshotElement.setId(UniqueIdGenerator.nextId());
		targetDataSnapshotElement.setDataPoolElementId(dataPoolElement.getId());
		targetDataSnapshotElement.setMappingCorrelationId(dataPoolElement.getMappingCorrelationId());
		
		return targetDataSnapshotElement;
	}
	
	public static DataSnapshotElement createEmpty(){
		org.bpim.model.data.v1.ObjectFactory dataObjectFactory = new org.bpim.model.data.v1.ObjectFactory();
		DataSnapshotElement inputDataSnapshotElement = dataObjectFactory.createDataSnapshotElement();
		inputDataSnapshotElement.setEmpty(true);
		inputDataSnapshotElement.setId(UniqueIdGenerator.nextId());
		return inputDataSnapshotElement;
	}

}
