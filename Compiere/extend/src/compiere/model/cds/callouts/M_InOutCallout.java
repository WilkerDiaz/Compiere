package compiere.model.cds.callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.CalloutInOut;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MOrder;
import compiere.model.cds.forms.XX_AssociateReference_Form;

/**
 *  Callouts extendidos de InOut (Recepción)
 *
 *  @author     José Trías
 *  @version    
 */
public class M_InOutCallout extends CalloutEngine {
		
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_AssociateReference_Form.class);
	
	/**
	 * 	C_Order - Order Defaults.
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String order (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		CalloutInOut X = new CalloutInOut();
		String oldCallout = X.order(ctx, WindowNo, mTab, mField, value);
		
		Integer C_Order_ID = (Integer)value;
		if (C_Order_ID == null || C_Order_ID.intValue() == 0)
			return "";
		//	No Callout Active to fire dependent values
		if (isCalloutActive())	//	prevent recursive
			return "";
				
		setCalloutActive(true);
		
		MOrder order = new MOrder (ctx, C_Order_ID.intValue(), null);
		
		if (order.get_ID() != 0)
		{
			mTab.setValue("XX_VMA_Season_ID", order.getXX_Season_ID());
			mTab.setValue("XX_VMR_Collection_ID", order.getXX_Collection_ID());
			mTab.setValue("XX_VMR_Package_ID", order.getXX_VMR_Package_ID());
			mTab.setValue("XX_EntranceDate", order.getXX_EntranceDate());
			mTab.setValue("XX_EntranceHour", order.getXX_EntranceDate());
			mTab.setValue("XX_OrderType", order.getXX_OrderType());
			mTab.setValue("XX_Returns", returns(order.getC_BPartner_ID()));
			mTab.setValue("C_PaymentTerm_ID", order.getC_PaymentTerm_ID());
		}
		
		setCalloutActive(false);
		
		return  oldCallout + "";		
	}	//	order
	
	/**
	 * 	returns
	 *	Verifica si el proveedor indicado posee devoluciones
	 */
	public String returns(int BPartner)
	{
		String returns="NO";
		
		String SQL = "SELECT " +
				"(CASE count(*) WHEN 0 THEN 'NO' " +
				"ELSE 'SI' END) return FROM XX_VLO_RETURNOFPRODUCT " +
				"WHERE C_BPartner_ID=111111 AND XX_STATUS='DPR'";

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
		
			while (rs.next())
			{
				returns = rs.getString("return");
			}
		
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		
		return returns;
	}
	
	/**
	 * 	XX_CheckAssistant_ID Defaults.
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String checkAssistant (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		
		Integer checkAssistant = (Integer)value;
		if (checkAssistant == null || checkAssistant.intValue() == 0){
			mTab.setValue("XX_PrintCheckup", "N");
			return "";
		}else{
			
			GridField auxiliary = mTab.getField("XX_CheckAuxiliary_ID");
			if(auxiliary.getValue()!=null){
				mTab.setValue("XX_PrintCheckup", "Y");
			}else{
				mTab.setValue("XX_PrintCheckup", "N");
			}
		}
		
		//	No Callout Active to fire dependent values
		if (isCalloutActive())	//	prevent recursive
			return "";
			
		
		return "";
	}	//	order
	
	
	/**
	 * 	XX_CheckAssistant_ID Defaults.
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String checkAuxiliary (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		
		Integer checkAuxiliary = (Integer)value;
		if (checkAuxiliary == null || checkAuxiliary.intValue() == 0){
			mTab.setValue("XX_PrintCheckup", "N");
			return "";
		}else{
			
			GridField assistant = mTab.getField("XX_CheckAssistant_ID");
			if(assistant.getValue()!=null){
				mTab.setValue("XX_PrintCheckup", "Y");
			}else{
				mTab.setValue("XX_PrintCheckup", "N");
			}
		}
		
		//	No Callout Active to fire dependent values
		if (isCalloutActive())	//	prevent recursive
			return "";
			
		
		return "";
	}	//	order
	
	/**
	 * 	countedPackagesQty Defaults.
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String countedPackagesQty (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		
		Integer countedPackagesQty = (Integer)value;
		if (countedPackagesQty == null || countedPackagesQty.intValue() == 0){
			return "";
		}
		
		//	No Callout Active to fire dependent values
		if (isCalloutActive())	//	prevent recursive
			return "";
		
		setCalloutActive(true);
		
		GridField PO = mTab.getField("C_Order_ID");
		if(PO.getValue()==null){
			setCalloutActive(false);
			return "";
		}
		
		Integer orderID = (Integer) PO.getValue();
		MOrder order = new MOrder( ctx, orderID, null);
	
		//Se coloca la hora en que se esta indicando los bultos (antes solo 1 vez, ahora cada vez que se coloca cantidad)
		/*//if(order.getXX_ReceptionUser_ID()==0){
			order.setXX_ReceptionUser_ID(Env.getCtx().getAD_User_ID());
			Calendar now = Calendar.getInstance();
			order.setXX_ReceptionDate(new Timestamp(now.getTimeInMillis()));
			order.save();
		//}*/
		
		//Se coloca la hora en que se esta indicando los bultos (antes solo 1 vez, ahora cada vez que se coloca cantidad)
		// Se cambio a Update porque se estaba tardando mucho por el Log de OC
		Calendar now = Calendar.getInstance();
		Timestamp actual = new Timestamp(now.getTimeInMillis());
		String sql = "UPDATE C_ORDER SET XX_ReceptionUser_ID = "+Env.getCtx().getAD_User_ID()+", " +
			        "XX_ReceptionDate = (TIMESTAMP '"+actual+"') WHERE C_ORDER_ID = "+order.get_ID();
		
		try{
			   DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//}
		setCalloutActive(false);
		
		return "";
	}	//	order
}
