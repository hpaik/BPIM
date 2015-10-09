package org.bpim.transformer.util;

import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.model.data.v1.DataSnapshotElement;

public class DataSnapshotElementHelper {
	
	public static DataSnapshotElement create(DataPoolElement dataPoolElement){
		org.bpim.model.data.v1.ObjectFactory objectFactory = new org.bpim.model.data.v1.ObjectFactory();
		DataSnapshotElement targetDataSnapshotElement = null;
		targetDataSnapshotElement = objectFactory.createDataSnapshotElement();
		targetDataSnapshotElement.setEmpty(false);
		targetDataSnapshotElement.setId(UniqueIdGenerator.nextId());
		targetDataSnapshotElement.setDataPoolElementId(dataPoolElement.getId());
		targetDataSnapshotElement.setMappingCorrelationId(dataPoolElement.getMappingCorrelationId());
		
		return targetDataSnapshotElement;
	}

}
