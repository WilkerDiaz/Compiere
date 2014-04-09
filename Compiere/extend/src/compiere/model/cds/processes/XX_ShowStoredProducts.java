package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.framework.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.MInOut;

public class XX_ShowStoredProducts extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		MInOut mInOut = new MInOut( getCtx(), getRecord_ID(), null);
		
	   	AWindow window_StoredProducts = new AWindow();
    	Query query = Query.getEqualQuery("C_Order_ID", mInOut.getC_Order_ID());
    	window_StoredProducts.initWindow(Env.getCtx().getContextAsInt("#XX_L_WINDOWSTOREDPRODUCT_ID"),query);
    	AEnv.showCenterScreen(window_StoredProducts);
		
		return "";
	}

	@Override
	protected void prepare() {
	}

}
