package compiere.model.payments.callout;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

public class VLO_PBInvoiceType extends CalloutEngine{

	/**
	 * A partir del identificador de la fatura, busca el tipo de factura
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value 
	 * @param oldValue
	 * 						compiere.model.payments.callout.VLO_PBInvoiceType.getInvoiceType
	 * @return
	 */
	public String getInvoiceType(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		String tipoFactura = null;
		PreparedStatement priceRulePstmt = null;
		ResultSet rs = null;
		try{
			if ((mField.getValue() == null) && (mField.getValue().equals(""))){
				String sql = "select XX_InvoiceType " +
						 "from C_Invoice " +
						 "where C_Invoice_ID = " + mTab.getValue("C_Invoice_ID");
				priceRulePstmt = DB.prepareStatement(sql, null);
				rs = priceRulePstmt.executeQuery();
			
				if (rs.next()){
					tipoFactura = rs.getString(1);

				}

				mTab.setValue("XX_InvoiceType",tipoFactura);
			}
		}catch (Exception e){
			e.printStackTrace();
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

	/**
	 * Buscar el número de la factura, el número de control y la fecha de 
	 * la facturación, de un documento específico
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value 
	 * @param oldValue
	 * 						compiere.model.payments.callout.VLO_PBInvoiceType.invoiceOC
	 * @return
	 */
	public String invoiceOC(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		Integer idInvoice = (Integer) mTab.getValue("C_Invoice_ID");
		if (idInvoice != null){
			String sql = "select a.documentNo, XX_ControlNumber, DateInvoiced, a.C_BPARTNER_ID, a.C_PaymentTerm_ID, " +
					     "b.XX_VMR_DEPARTMENT_ID, b.M_WAREHOUSE_ID from C_Invoice a, C_Order b " +
					     "where a.C_Order_ID=b.C_Order_ID AND a.C_Invoice_ID = " + idInvoice;
			PreparedStatement priceRulePstmt = null;
			ResultSet rs = null;
			try{
				priceRulePstmt = DB.prepareStatement(sql, null);
				rs = priceRulePstmt.executeQuery();
				if (rs.next()){
					mTab.setValue("XX_InvoiceNro", rs.getString("documentNo"));
					mTab.setValue("XX_ControlNumber", rs.getString("XX_ControlNumber"));
					mTab.setValue("XX_InvoiceDate", rs.getTimestamp("DateInvoiced"));
					mTab.setValue("C_BPARTNER_ID", rs.getInt("C_BPARTNER_ID"));
					mTab.setValue("C_PaymentTerm_ID", rs.getInt("C_PaymentTerm_ID"));
					mTab.setValue("XX_VMR_DEPARTMENT_ID", rs.getInt("XX_VMR_DEPARTMENT_ID"));
					mTab.setValue("M_WAREHOUSE_ID", rs.getInt("M_WAREHOUSE_ID"));
					//mTab.setValue("DataType", "T");
				}
			}catch (Exception e){
				e.printStackTrace();
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
		}
		return "";
	}
	
	/**
	 * Se encarga de buscar y setear el socio de negocio asociado 
	 * a un contrato específico
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * 						compiere.model.payments.callout.VLO_PBInvoiceType.invoiceContract
	 * @return
	 */
	public String invoiceContract(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		Integer idContract = (Integer) mTab.getValue("XX_Contract_ID");
		if (idContract != null){
			String sql = "select C_BPartner_ID, C_Currency_ID " +
						 "from XX_Contract " +
						 "where XX_Contract_ID = " + idContract;
			PreparedStatement priceRulePstmt = null;
			ResultSet rs = null;
			try{
				priceRulePstmt = DB.prepareStatement(sql, null);
				rs = priceRulePstmt.executeQuery();
				if (rs.next()){
					mTab.setValue("C_BPartner_ID", rs.getInt("C_BPartner_ID"));
					mTab.setValue("C_Currency_ID", rs.getInt("C_Currency_ID"));
				}
			}catch (Exception e){
				e.printStackTrace();
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
						 
		}
		return "";
	}
}
