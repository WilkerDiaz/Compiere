package compiere.model.cds.callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

public class VCN_TradeAgreementsCountryCallout extends CalloutEngine{

	
	/*
	 * Método que obtiene el país, el tipo de proveedor y tipo de pago de acuerdo al país
	 * y se muestran en los respectivos campos
	 */
	public String setTradeAgreementCountry (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		String SQL = "select cco.C_COUNTRY_ID, cbp.XX_VENDORTYPE_ID " +
				" from C_BPARTNER cbp, C_COUNTRY cco, C_BPARTNER_LOCATION cbl, C_LOCATION clo" +
				" where cbp.C_Bpartner_ID="+ (Integer) mTab.getValue("C_BPartner_ID")+ "and cbp.C_Bpartner_ID=cbl.C_Bpartner_ID and" +
				" clo.C_LOCATION_ID=cbl.C_LOCATION_ID and cco.C_COUNTRY_ID=clo.C_COUNTRY_ID and cbp.AD_CLIENT_ID="+ ctx.getAD_Client_ID();

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
		
			if (rs.next()){
				mTab.setValue("XX_VendorCountry_ID", rs.getInt(1));

				if (rs.getInt(1)==339){
					mTab.setValue("XX_PAYMENTRULE", "NAC");
					mTab.setValue("C_CURRENCY_ID", 205); //Moneda en Bs.
				}
			
				else{
					mTab.setValue("XX_PAYMENTRULE", "INT");
					mTab.setValue("C_CURRENCY_ID", 100); //Moneda en Dólares
				}
				mTab.setValue("XX_VendorType_ID", rs.getInt(2));
				
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.getMessage();
		}
		setCalloutActive(false);
		return "";
	}
}
