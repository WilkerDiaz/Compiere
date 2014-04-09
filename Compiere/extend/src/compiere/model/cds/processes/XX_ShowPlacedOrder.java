package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.framework.Query;
import org.compiere.process.SvrProcess;

import compiere.model.cds.MInOut;

public class XX_ShowPlacedOrder extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		MInOut mInOut = new MInOut( getCtx(), getRecord_ID(), null);
		
	   	AWindow window_PlacedOrder = new AWindow();
    	Query query = Query.getEqualQuery("C_Order_ID", mInOut.getC_Order_ID());
    	window_PlacedOrder.initWindow(1000178,query);
    	AEnv.showCenterScreen(window_PlacedOrder);
		
		return "";
	}

	@Override
	protected void prepare() {
	}

}
