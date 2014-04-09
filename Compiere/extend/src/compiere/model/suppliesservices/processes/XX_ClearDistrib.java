package compiere.model.suppliesservices.processes;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MOrderLine;

public class XX_ClearDistrib extends SvrProcess{
	/** Purchase of Supplies and Services 
	 * Maria Vintimilla Funcion 19
	 * Clear distribution for a PO Line **/
	@Override
	protected String doIt() throws Exception {
		try{
			MOrderLine line = new MOrderLine(Env.getCtx(),getRecord_ID(),null);
			
			String sql = "DELETE FROM XX_PRODUCTPERCENTDISTRIB " +
						 "WHERE C_OrderLine_ID = "+ line.getC_OrderLine_ID();
			DB.executeUpdateEx(sql, get_Trx());
			
			line.setXX_ClearedDistrib(false);
			line.setXX_IsPiecesPercentage(false);
			line.setXX_IsDistribApplied(false);
			line.save();
			return "";
		}//try
		catch(Exception e){
			return e.getMessage();
		}
	}// Fin doIt
	
	@Override
	protected void prepare() {

	}
	
}// Fin XX_ClearDistrib
