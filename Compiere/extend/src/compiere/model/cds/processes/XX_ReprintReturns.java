package compiere.model.cds.processes;

import java.util.logging.Level;
import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.X_XX_VLO_ReturnOfProduct;

public class XX_ReprintReturns extends SvrProcess {

	Integer returnPO = 0;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_VLO_ReturnOfProduct_ID"))
				returnPO = new Integer(element.getParameter().toString());
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}

	@Override
	protected String doIt() throws Exception {
		
		MPrintFormat f = null;
		
		Query q = new Query("XX_VLO_RETURNOFPRODUCT");
		q.addRestriction("XX_VLO_RETURNOFPRODUCT_ID", Query.EQUAL, Integer.valueOf(returnPO));
		int table_ID = X_XX_VLO_ReturnOfProduct.Table_ID;
		
		// Create the Process Info Instance to generate the report
		PrintInfo i = new PrintInfo("Return", table_ID, returnPO, 0);
		
		f = MPrintFormat.get (Env.getCtx(), 1011343, false);
		
		ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i); 
		new Viewer(re);
		
		return "";
	}

}
