
package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

/**
 * 
 * @author Luis E. García
 */

public class VCN_TradeAgreementPayment extends CalloutEngine {
	
	//Valor del IVA
	static final int IVA = 12;

	
	/*
	 * Método que introduce el monto del impuesto calculado dependiendo del tipo de impuesto seleccionado
	 */
	public String setTradeAgreementTax (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		BigDecimal taxAmount= BigDecimal.ZERO;
		BigDecimal total= BigDecimal.ZERO;
		BigDecimal iva= BigDecimal.ZERO;
		int IvaID= ctx.getContextAsInt("#XX_L_TAX_IVA_ID");

		//Obtengo el id del tipo de moneda que se seleccionó		
		Integer currencyID = (Integer) mTab.getValue("C_CURRENCY_ID");
		//Obtengo el monto del aporte a publicidad
		BigDecimal pubAmount = (BigDecimal) mTab.getValue("XX_PubAmount");
		//Si el impuesto es IVA se calcula
		if (currencyID==205){
			mTab.setValue("XX_TypeTax_ID", IvaID);
			String SQL = "SELECT A.RATE, A.C_TAX_ID, B.C_TAXCATEGORY_ID " +
			  "FROM C_TAX A, C_TAXCATEGORY B " +
			  "WHERE C_TAX_ID= "+ IvaID+
			  " AND A.AD_CLIENT_ID="+  ctx.getAD_Client_ID();
			try
			{	
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
				ResultSet rs = pstmt.executeQuery();
			   
				while(rs.next())
				{
			    	iva = rs.getBigDecimal("RATE");
			    	iva = iva.divide(new BigDecimal(100));		    	
				}
				rs.close();
				pstmt.close();
			   
			}
			catch (Exception e) {
				System.out.println("ERROR CALCULANDO EL IVA");
			}
		}
		else
			mTab.setValue("XX_TypeTax_ID", ctx.getContextAsInt("#XX_L_TAX_EXENTO_ID"));
		taxAmount = pubAmount.multiply(iva);
		total = taxAmount.add(pubAmount);
		mTab.setValue("XX_TaxAmount", taxAmount);
		mTab.setValue("XX_TotalTax", total);		

		return "";
	}

	
	
}
