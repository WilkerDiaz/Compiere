package compiere.model.suppliesservices.callouts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class IVACallout extends CalloutEngine {
	/**
	 * CalculateIVA. Set XX_TotalIVA according the tax selected
	 * @param ctx context
	 * @param WindowNo window no
	 * @param mTab tab
	 * @param mField field
	 * @param value value
	 * @throws SQLException 
	 */
	public String CalculateIVA(Ctx ctx, int WindowNo, GridTab mTab,GridField mField, Object value){
		/** Purchase of Supplies and Services
		 * Maria Vintimilla Funcion 008 **/
		BigDecimal TaxRate = new BigDecimal(0);
		BigDecimal TaxAmount = new BigDecimal(0);
		BigDecimal Amount = (BigDecimal)mTab.getValue("PriceActual");
		Integer Order_ID = (Integer)mTab.getValue("C_Order_ID");
		
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		String sql1 = " Select Rate From C_Tax Where C_Tax_ID = " +
							" (Select distinct C_Tax_ID From C_Orderline " +
							" Where C_Order_ID = "+Order_ID+ 
							" AND Updated In " +
								" (Select Max(Updated) " +
								" From C_OrderLine" +
								" Where C_Order_ID = "+Order_ID+
								")) " +
							" AND AD_Client_ID = " + 
									Env.getCtx().getAD_Client_ID();
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		//System.out.println(sql1);
		try{
			pstmt1 = DB.prepareStatement(sql1, null); 
			rs1 = pstmt1.executeQuery();
			if(rs1.next()){
				TaxRate = rs1.getBigDecimal("RATE");
			}			    
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rs1);
			DB.closeStatement(pstmt1);
		}
		
		TaxAmount = (Amount.multiply(TaxRate)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);	
		mTab.setValue("XX_TotalIVA",TaxAmount);
		setCalloutActive(false);
		
		return "";
	}//Fin CalculateIVA
	
}// Fin IVACallout
