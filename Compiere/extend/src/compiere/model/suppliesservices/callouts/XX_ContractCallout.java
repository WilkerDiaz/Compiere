package compiere.model.suppliesservices.callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class XX_ContractCallout extends CalloutEngine{
/**
 * VerifyCurrency
 * LLamada que se encarga de seleccionar la moneda del contrato de acuerdo al país
 * del socio de negocios seleccionado
 * @author Maria Vintimilla
 * @since 04/08/2012
 * @param ctx
 * @param WindowNo
 * @param mTab
 * @param mField
 * @param value
 * @return
 */
	public String VerifyCurrency(Ctx ctx, int WindowNo, GridTab mTab,GridField mField, Object value){
		String SQLCounty = "";
		PreparedStatement psCountry = null;
		ResultSet rsCountry = null;
		
		if (isCalloutActive() || value==null)
			return "";
		
		SQLCounty = " SELECT XX_COUNTRY_ID ID" +
				" FROM XX_VCN_VENDORCOUNTRYDISTRI " +
				" WHERE C_BPARTNER_ID = " + value;
		
		try{
			psCountry = DB.prepareStatement(SQLCounty, null);
			rsCountry = psCountry.executeQuery();
			while(rsCountry.next()){
				// Internacional
				if (rsCountry.getInt("ID") != Env.getCtx().getContextAsInt("#XX_L_VENCOUNTRY_ID")) {
					Integer inter = Env.getCtx().getContextAsInt("#XX_L_INTERCURRENCY_ID"); // Moneda Dollar (100)
					mTab.setValue("C_Currency_ID", inter);
				}
				// Nacional
				else{
					Integer nac = Env.getCtx().getContextAsInt("#XX_L_VENCURRENCY_ID"); // Moneda Bolívares (100)
					mTab.setValue("C_Currency_ID", nac);
				}
			} // while
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rsCountry); 
			DB.closeStatement(psCountry);
		}// finally

		
		setCalloutActive(false);
		return "";
	}//VerifyDate

	
	
} // Fin
