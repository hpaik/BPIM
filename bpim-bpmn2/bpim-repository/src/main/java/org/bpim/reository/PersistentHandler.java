package org.bpim.reository;

import org.bpim.model.v1.ProcessInstance;

public interface PersistentHandler {
	void storeProcessInstance(ProcessInstance processInstance);
}
