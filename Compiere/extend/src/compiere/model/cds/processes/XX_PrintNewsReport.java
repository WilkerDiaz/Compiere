package compiere.model.cds.processes;

import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.MInOut;
import compiere.model.cds.X_XX_VLO_NewsReport;

/**
 * 
 * @author Trinimar Acevedo
 *
 */
public class XX_PrintNewsReport extends SvrProcess{
	
static Integer sync = new Integer(0);
		
	@Override
	protected String doIt() throws Exception {
		
		int table_ID = 0;
		
		MInOut mID = new MInOut(Env.getCtx(), getRecord_ID(), null);
		Query q = new Query("C_Order_ID");
		q.addRestriction("C_Order_ID", Query.EQUAL, ""+mID.getC_Order_ID());
		table_ID = X_XX_VLO_NewsReport.Table_ID;
		
		// Crear la instancia del Process Info para generar el reporte
		PrintInfo i = new PrintInfo("Reporte de Novedades", table_ID, mID.getC_Order_ID(), 0);
		MPrintFormat f = MPrintFormat.get (Env.getCtx(), 1002443, false);
		
		
		// Crear el reporte
		ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
		new Viewer(re);
		
		return "";
	}
	@Override
	protected void prepare() {
	}

}
