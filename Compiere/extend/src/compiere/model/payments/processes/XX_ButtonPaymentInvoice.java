package compiere.model.payments.processes;

import java.sql.PreparedStatement;
import java.util.logging.Level;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MInvoice;

/**
 * Modifica el estado de las cuentas por pagar en la factura al ejecutar el proceso
 * @author Jessica Mendoza
 *
 */
public class XX_ButtonPaymentInvoice extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		
		MInvoice invoice = new MInvoice(Env.getCtx(), getRecord_ID(),null);
		invoice.setXX_AccountPayableStatus("A");
		
		String SQL = "update C_Invoice " +
					 "set XX_AccountPayableStatus = 'A' " +
					 " where (C_Order_ID = "+invoice.getC_Order_ID()+")";
		PreparedStatement pstmt = null; 
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			pstmt.executeQuery();
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		}finally{
			pstmt.close();
		}
		
		invoice.save();
		return null;
	}

	@Override
	protected void prepare() {

	}

}
