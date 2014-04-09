package compiere.model.payments.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.MPaySelection;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class XX_RevertPaySelection extends SvrProcess {

	@Override
	protected void prepare() {
		//DO NOTHING :)
	}

	@Override
	protected String doIt() throws Exception {
		
		if(hasPayment()){
			ADialog.info(1, new Container(), Msg.translate(Env.getCtx(), "XX_HasPayment"));
			return Msg.translate(Env.getCtx(), "XX_HasPayment");
		}
		
		int paySelectionID = getRecord_ID();
		
		String updateInvoice = "UPDATE C_INVOICE SET XX_ACCOUNTPAYABLESTATUS = 'A' " +
								"WHERE C_Invoice_ID IN " +
								"(SELECT C_INVOICE_ID FROM C_PaySelectionLine WHERE C_PaySelection_ID = " + paySelectionID + ")";
		
		DB.executeUpdate(null, updateInvoice);
		
		//Actualiza los Anticipos
		String updateAdvances = "UPDATE C_PAYMENT SET XX_ACCOUNTPAYABLESTATUS = 'A' " +
								"WHERE C_Payment_ID IN "+
								"(SELECT C_Payment_ID FROM C_PaySelectionLine WHERE C_PaySelection_ID = " + paySelectionID + ")";

		DB.executeUpdate(null, updateAdvances);

		//Borra las lines de la Seleccion de Pago
		String deleteLines = "DELETE FROM C_PaySelectionLine "+
							 "WHERE C_PaySelection_ID = " + paySelectionID;

		DB.executeUpdate(null, deleteLines);
		
		
		String deleteCheck = "DELETE FROM C_PAYSELECTIONCHECK "+
							 "WHERE C_PaySelection_ID = " + paySelectionID;

		DB.executeUpdate(null, deleteCheck);
		
		MPaySelection paySelection = new MPaySelection( Env.getCtx(), paySelectionID, null);
		paySelection.setProcessed(false);
		
		if(paySelection.delete(false))
			ADialog.info(1, new Container(), Msg.translate(Env.getCtx(), "XX_PaySelectionDeleted"));

		return "Proceso Finalizado";
	}
	
	/*
	 * Verifica si tiene pago asociado
	 */
	private boolean hasPayment(){
		
		
		String sqlCategoria = "SELECT * FROM C_PaySelectionCheck a, C_Payment b " +
							  "WHERE a.C_Payment_ID = b.C_Payment_ID " +
							  "AND C_PaySelection_ID = "+ getRecord_ID();
		
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    
		try {
			pstmt = DB.prepareStatement(sqlCategoria, null);
			rs = pstmt.executeQuery();
			
			if (rs.next()){
				return true;
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlCategoria, e);
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		} 
		
		return false;
	}
	
}
