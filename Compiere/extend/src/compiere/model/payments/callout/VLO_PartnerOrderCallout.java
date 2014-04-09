package compiere.model.payments.callout;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;

/**
 * Se encarga de buscar el nombre del Socio de Negocio, 
 * a partir del identificador de la OC
 * @author Jessica Mendoza
 *
 */
public class VLO_PartnerOrderCallout  extends CalloutEngine {

	public String getPartner(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		
		int socio = 0;
		int pais = 0;
		int tienda = 0;
		int departamento = 0;
		int condicion = 0;
		PreparedStatement priceRulePstmt = null;
		ResultSet rs = null; 
		try{

			if ((mField.getValue()!=null) && (!(mField.getValue().equals("")))){
				String sql = "select par.C_BPartner_ID, con.C_Country_ID, ord.M_Warehouse_ID, " +
							 "ord.XX_VMR_Department_ID, par.PO_PaymentTerm_ID " +
							 "from C_Order ord, C_BPartner par, XX_VCN_VendorCountryDistri vcd, C_Country con " +
							 "where ord.C_Order_ID = " + value + " " + 
							 "and ord.C_BPartner_ID = par.C_BPartner_ID " +
							 "and con.C_Country_ID = vcd.XX_Country_ID " +
							 "and vcd.C_BPartner_ID = par.C_BPartner_ID ";

				priceRulePstmt = DB.prepareStatement(sql, null);
				rs = priceRulePstmt.executeQuery();
				
				if (rs.next()){
					socio = rs.getInt(1);
					pais = rs.getInt(2);
					tienda = rs.getInt(3);
					departamento = rs.getInt(4);
					condicion = rs.getInt(5);
				}
				
				
				mTab.setValue("C_BPartner_ID",socio);
				mTab.setValue("C_Country_ID", pais);
				mTab.setValue("M_Warehouse_ID", tienda);
				mTab.setValue("XX_VMR_Department_ID", departamento);
				mTab.setValue("C_PaymentTerm_ID", condicion);
				
			}else{
				mTab.setValue("C_BPartner_ID",socio);
				mTab.setValue("C_Country_ID", pais);
				mTab.setValue("M_Warehouse_ID", tienda);
				mTab.setValue("XX_VMR_Department_ID", departamento);
				mTab.setValue("C_PaymentTerm_ID", condicion);
			}
		}catch (Exception e) {
			return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
		}finally{
			try {
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				priceRulePstmt.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}	
		return "";
		
	}
}
