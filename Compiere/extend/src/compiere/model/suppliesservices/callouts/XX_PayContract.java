package compiere.model.suppliesservices.callouts;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class XX_PayContract extends CalloutEngine {

	/**
	 * Verificación de la fecha del pago de contratoVerifyDate from a Contract's payment
	 * @param ctx context
	 * @param WindowNo window no
	 * @param mTab tab
	 * @param mField field
	 * @param value value
	 */
	public String VerifyDate(Ctx ctx, int WindowNo, GridTab mTab,GridField mField, Object value){
		/** Purchase of Supplies and Services
		 * Maria Vintimilla Funcion 29 **/
		// Contract's ID
		Integer Contract_ID = (Integer)mTab.getValue("XX_Contract_ID");
		// Contract's Payment Date To
		Timestamp dateToPayment = (Timestamp)mTab.getValue("XX_DateTo"); 
		// Contract's Date To
		Timestamp dateToContract; 
		String error_msg = "Error: La fecha ingresada es mayor a la fecha " +
				" final del contrato";
		
		if (isCalloutActive() || value==null)
			return "";
		
		String SQL1 = " SELECT XX_DateTo " +
					" FROM XX_Contract " +
					" Where XX_Contract_ID = " + Contract_ID +
					" AND AD_Client_ID = " + 
					Env.getCtx().getAD_Client_ID();
		//System.out.println("SQL DateTo Callout"+SQL1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQL1, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				dateToContract = rs.getTimestamp("XX_DateTo");
				if(dateToPayment.after(dateToContract)){
					ADialog.info(1, new Container(),error_msg);
				}
			}
		}//try
		catch (SQLException e){
			e.printStackTrace();			
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		setCalloutActive(false);
		return "";
	}//VerifyDate
	
}// Fin XX_PayContract
