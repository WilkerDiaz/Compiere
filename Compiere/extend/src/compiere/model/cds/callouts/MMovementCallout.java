package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.CalloutEngine;
import org.compiere.model.CalloutMovement;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MDocType;
import org.compiere.model.MWarehouse;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MLocator;
import compiere.model.cds.MMovement;
import compiere.model.cds.MProduct;
import compiere.model.cds.Utilities;

public class MMovementCallout extends CalloutEngine{
	
	protected transient CLogger	log = CLogger.getCLogger (getClass());
	
	/**
	 * Actualiza la categoria dado un departamento
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String department (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
	
			//Actualizar la categoria cuando se cambie un departamento
			
		Integer category = 0;
		Integer department = 0;		
		if (mField.getValue() == null)
			return "";
		else department = (Integer)mField.getValue();
		
		String SQL = " SELECT XX_VMR_CATEGORY_ID FROM XX_VMR_DEPARTMENT " +
				" WHERE XX_VMR_DEPARTMENT_ID = "+ department ;
		try {
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				category = rs.getInt("XX_VMR_CATEGORY_ID");
			} 
			rs.close();
			pstmt.close();
		}catch (Exception e) {
				return "Error-Callout";
		}
		mTab.setValue("XX_VMR_Category_ID", category);
		return "";
	}
	
	/**
	 * Actualiza la categoria dado un departamento
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	@SuppressWarnings("deprecation")
	public String product (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
	
			//Actualizar los datos de productos
		
		CalloutMovement call = new CalloutMovement();
		String result = call.product(ctx, WindowNo, mTab, mField, value);
		MMovement mov = new MMovement(ctx, (Integer)mTab.getValue("M_Movement_ID"), null);
		MDocType docType = new MDocType(ctx, mov.getC_DocType_ID(), null);
			
		Integer brand = 0, section = 0, line = 0;
		Integer product = 0;		
		if (mField.getValue() == null) {
			mTab.setValue("XX_VMR_Brand_ID", 0);
			mTab.setValue("XX_VMR_Line_ID", 0);
			mTab.setValue("XX_VMR_Section_ID", 0);			
			return "";
		}
		else product = (Integer)mField.getValue();
		
		String SQL = " SELECT XX_VMR_BRAND_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID FROM M_PRODUCT " +
				" WHERE M_PRODUCT_ID = "+ product ;
		try {
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				brand = rs.getInt(1);				
				line = rs.getInt(2);
				section = rs.getInt(3);				
			} 
			rs.close();
			pstmt.close();
		}catch (Exception e) {
				result += "Error-Callout";
		}
		mTab.setValue("XX_VMR_Brand_ID", brand);
		mTab.setValue("XX_VMR_Line_ID", line);
		mTab.setValue("XX_VMR_Section_ID", section);
		
		result += priceConsecutive(ctx, WindowNo, mTab, mField, value);
		if (!docType.getName().equals("Movimiento de Inventario"))
			return result; 
		else
			return "";
	}
	
	public String priceConsecutive (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
	
			//Obtener los datos necesarios para el calculo
					
		mTab.setValue("XX_SalePrice", new BigDecimal(0.01));
		mTab.setValue("PriceActual", new BigDecimal(0.01));
		mTab.setValue("M_AttributeSetInstance_ID", 0 );
		mTab.setValue("M_AttributeSetInstanceTo_ID", 0);
		mTab.setValue("C_TaxCategory_ID", 0);
		mTab.setValue("TaxAmt", Env.ZERO);
		
		Integer product, priceconsecutive;
		BigDecimal saleprice = Env.ZERO, priceactual = Env.ZERO;
	
		
		//Verificar que estén todos los argumentos
		if ( mTab.getValue("M_Product_ID") == null) {
			mTab.setValue("C_TaxCategory_ID", null);
			mTab.setValue("TaxAmt", Env.ZERO);
			return "";
		} else {
			product = (Integer)mTab.getValue("M_Product_ID");
		}
	
		//Verificar consecutivo de precios
		if (mTab.getValue("XX_PriceConsecutive") == null)
			return "";
		else priceconsecutive = (Integer)mTab.getValue("XX_PriceConsecutive");
		
		if (priceconsecutive == 0) 
			return "";
						
		//Usando el priceconsecutive y el precio deberia calcularse el costo
		String sql = " SELECT XX_SALEPRICE, XX_UNITPURCHASEPRICE " +				
			" FROM XX_VMR_PRICECONSECUTIVE " + 
			" WHERE M_PRODUCT_ID = " + product + 
			" AND XX_PRICECONSECUTIVE = " + priceconsecutive +
			" AND  ROWNUM = 1 ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {				
				saleprice = rs.getBigDecimal("XX_SALEPRICE");
				priceactual = rs.getBigDecimal("XX_UNITPURCHASEPRICE");
				mTab.setValue("XX_SalePrice", saleprice);
				mTab.setValue("PriceActual", priceactual);					
			} else {
				return Msg.translate(ctx, "XX_ProductPConsecNotFound");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return Msg.getMsg(ctx, "XX_DatabaseAccessError");
		} finally {			
			try {
				pstmt.close();
				rs.close();
			} catch (SQLException e) {}
		}

		MProduct pr = new MProduct( Env.getCtx(), product, null);
		if (pr.getC_TaxCategory_ID() > 0) {
			String sql_rate = " SELECT (RATE/100) FROM C_TAX " +
			" WHERE C_TaxCategory_ID= ? " + 
			" AND  ValidFrom <= sysdate " +
			" and rownum = 1 " +
			" order by ValidFrom desc ";	
			PreparedStatement prst_tax = null;
			ResultSet rs_tax = null;
			try { 
				
				prst_tax = DB.prepareStatement(sql_rate,null);
				prst_tax.setInt(1, pr.getC_TaxCategory_ID());
				rs_tax = prst_tax.executeQuery();
				if (rs_tax.next()){
												
					BigDecimal tax = rs_tax.getBigDecimal(1);
					tax = tax.multiply(priceactual);
					tax = tax.setScale(2, RoundingMode.HALF_DOWN);
					mTab.setValue("TaxAmt", tax);
					mTab.setValue("C_TaxCategory_ID", pr.getC_TaxCategory_ID());							
				} else {
					return Msg.translate(ctx, "XX_ProductPConsecNotFound");
				}

			} catch (SQLException e){
				return "Error al buscar la tasa de impuesto";			
			} finally {
				try {
					rs_tax.close();
					prst_tax.close();
				} catch (SQLException e) {}
			}			
		}
		return "";
	}	
	
	
	public String approvedQty (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		
		BigDecimal approvedQty = Env.ZERO;		
		if (mField.getValue() == null) {
			return "";
		}
		approvedQty = (BigDecimal)mField.getValue();
		if (approvedQty.compareTo((BigDecimal)mTab.getValue("QtyRequired")) == 1) {
			return Msg.getMsg(Env.getCtx(), "XX_AppLessThanMov");			
		}
		return "";
	}
	
	public String C_DocType_ID (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		int doctype = 0;
		if (mField.getValue() == null) {
			return "";
		}
		doctype = (Integer) mField.getValue();
		if (doctype == Env.getCtx().getContextAsInt("XX_L_DOCTYPERETURN_ID")) {
			mTab.setValue("M_WarehouseTo_ID", Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"));			
		}		
//		// si es un movimiento de inventario entre centros de distribucion
//		if (doctype == 1000335) {
//			// si es de tienda 01, asigarle la tienda 20
//			if ((Integer)mTab.getValue("M_WarehouseFrom_ID")==1000053)
//				mTab.setValue("M_WarehouseTo_ID", 1000062);		
//			// si es de tienda 20 asignarle la tienda 01
//			if ((Integer)mTab.getValue("M_WarehouseFrom_ID")==1000062)
//				mTab.setValue("M_WarehouseTo_ID", 1000053);	
//		}
		
		return "";
	}
	
	/** Callout que configura el campo de locator to*/
	public String configurarLocator(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		
		if (value == null) {
			return "";
		}		
		if (mField.getColumnName().equals("M_WarehouseTo_ID")) {
			MLocator en_transito = Utilities.obtenerLocatorEnTransito((Integer)value);
			if (en_transito != null) {
				mTab.setValue("M_LocatorTo_ID", en_transito.get_ID());
			}
		} 		
		return "";
	}
	
	/** Callout que configura el campo de locator to*/
	public String configurarWarehouse(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		
		if (value == null) {
			return "";
		}		
		if (mField.getColumnName().equals("M_Locator_ID")) {
			MWarehouse almacen = Utilities.obtenerWarehouse((Integer)value);
			if (almacen != null) {
				mTab.setValue("M_WarehouseFrom_ID", almacen.get_ID());
			}			
		} 		
		return "";
	}
	
	
	
	
}
