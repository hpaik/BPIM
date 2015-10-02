package org.bpim.transformer.util;

import java.util.UUID;

public class UniqueIdGenerator {
	
	public static String nextId(){
		return UUID.randomUUID().toString();
	}

}
