package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MInOut;
import compiere.model.cds.MInvoice;

public class XX_PrintDefinitiveFromInvoice extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		MInvoice mInvoice = new MInvoice(getCtx(), getRecord_ID(), null);
		
		if(mInvoice.getC_Order_ID()!=0){
			
			MInOut mInOut = null;
			String SQL = "Select M_INOUT_ID from M_INOUT " +
			"where C_ORDER_ID = " + mInvoice.getC_Order_ID();
			
			try {
				PreparedStatement pstmt = DB.prepareStatement(SQL, null);
				ResultSet rs = pstmt.executeQuery();
				boolean record=true;
				while (rs.next()){
					record=false;
					mInOut = new MInOut(
							Env.getCtx(), rs.getInt(1), null);				
				}
				rs.close();
				pstmt.close();
				if(record){
					return "";
				}
				
				rs.close();
				pstmt.close();
				
			} catch (SQLException e) {
				log.log(Level.SEVERE, SQL, e);
			}
			
			MPInstance mpi = new MPInstance( getCtx(), 1000157, mInOut.get_ID());
	        mpi.save();
	        
	        ProcessInfo pi = new ProcessInfo("", 1000157);
	        pi.setAD_PInstance_ID(mpi.get_ID());
	        pi.setAD_Process_ID(1000157);        
	        pi.setClassName("");
	        pi.setTitle("Reporte Definitivo");
	        pi.setTable_ID(319);
	        pi.setAD_Client_ID(getCtx().getAD_Client_ID());
	        pi.setAD_User_ID(getCtx().getAD_User_ID());        
	        pi.setRecord_ID(mInOut.get_ID());
	  
	        ProcessCtl proc = new ProcessCtl(null, pi, null);
	        proc.start();
        }
		
		return "";
	}

	@Override
	protected void prepare() {
		
	}

}
