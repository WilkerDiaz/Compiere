package compiere.model.suppliesservices.callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.suppliesservices.X_XX_ProductName;

/** Purchase of Supplies and Services
 * Maria Vintimilla Funcion 04 
 * Set name according the product name**/
public class XX_ProductName extends CalloutEngine{

	public String ProdName (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		Integer ProductNameID = (Integer) mField.getValue();
		String ProductName = "";
		ResultSet rs1 = null;
		PreparedStatement pstmt1 = null;
		
		if (isCalloutActive() || value==null)
			return "";

		if(ProductNameID != null){
			String sql = " Select Name From XX_ProductName " +
					" Where XX_ProductName_ID = " + ProductNameID+
					" AND AD_Client_ID = " + 
					Env.getCtx().getAD_Client_ID();
			try{
				pstmt1 = DB.prepareStatement(sql, null); 
				rs1 = pstmt1.executeQuery();
				if(rs1.next()){
					ProductName = rs1.getString("Name");
				}
				rs1.close();
				pstmt1.close();
							    
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				DB.closeResultSet(rs1);
				DB.closeStatement(pstmt1);
			}//finally
			
			mTab.setValue("Name",ProductName);
		}//if
		
		setCalloutActive(false);
		return "";
	}// Fin ProdName
	
}// Fin XX_ProductName
