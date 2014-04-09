package org.compiere.process;

import org.compiere.esb.*;



public class CacheResetWeb extends SvrProcess {

	
	@Override
	protected String doIt() throws Exception {
		log.info("Clearning Web Cache");
		GwtServer.resetWinDefCache();
		return "@OK@";
	}


	@Override
	protected void prepare() {
	}

}
