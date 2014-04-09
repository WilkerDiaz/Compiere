package compiere.model.cds.callouts;

import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.X_C_Conversion_Rate;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;


import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDistribProductDetail;
import compiere.model.cds.X_XX_VMR_UnitConversion;
import compiere.model.cds.callouts.VME_PriceProductCallout;

/**
*
* @author José G. Trías   SI, CLARO!
*/
public class XXMRLinerefprovCallout extends CalloutEngine {

	public String Characteristic1 (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value==null)
			return "";
		
		mTab.setValue("XX_Characteristic2_ID", null);
		mTab.setValue("XX_Characteristic1Value2_ID", null);
		mTab.setValue("XX_Characteristic1Value3_ID", null);
		mTab.setValue("XX_Characteristic1Value4_ID", null);
		mTab.setValue("XX_Characteristic1Value5_ID", null);
		mTab.setValue("XX_Characteristic1Value6_ID", null);
		mTab.setValue("XX_Characteristic1Value7_ID", null);
		mTab.setValue("XX_Characteristic1Value8_ID", null);
		mTab.setValue("XX_Characteristic1Value9_ID", null);
		mTab.setValue("XX_Characteristic1Value10_ID", null);
		
		setCalloutActive(false);
		return "";
	}
	
	public String Characteristic2 (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value==null)
			return "";
		
		setCalloutActive(true);
		
		mTab.setValue("XX_Characteristic2Value2_ID", null);
		mTab.setValue("XX_Characteristic2Value3_ID", null);
		mTab.setValue("XX_Characteristic2Value4_ID", null);
		mTab.setValue("XX_Characteristic2Value5_ID", null);
		mTab.setValue("XX_Characteristic2Value6_ID", null);
		mTab.setValue("XX_Characteristic2Value7_ID", null);
		mTab.setValue("XX_Characteristic2Value8_ID", null);
		mTab.setValue("XX_Characteristic2Value9_ID", null);
		mTab.setValue("XX_Characteristic2Value10_ID", null);
		
		setCalloutActive(false);
		return "";
	}
	
	public String WithCharacteristic (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (value==null)
			return "";

		setCalloutActive(true);
		
		mTab.setValue("XX_Characteristic1_ID", null);
		mTab.setValue("XX_Characteristic2_ID", null);
		mTab.setValue("XX_Characteristic1Value1_ID", null);
		mTab.setValue("XX_Characteristic1Value2_ID", null);
		mTab.setValue("XX_Characteristic1Value3_ID", null);
		mTab.setValue("XX_Characteristic1Value4_ID", null);
		mTab.setValue("XX_Characteristic1Value5_ID", null);
		mTab.setValue("XX_Characteristic1Value6_ID", null);
		mTab.setValue("XX_Characteristic1Value7_ID", null);
		mTab.setValue("XX_Characteristic1Value8_ID", null);
		mTab.setValue("XX_Characteristic1Value9_ID", null);
		mTab.setValue("XX_Characteristic1Value10_ID", null);
		mTab.setValue("XX_Characteristic2Value1_ID", null);
		mTab.setValue("XX_Characteristic2Value2_ID", null);
		mTab.setValue("XX_Characteristic2Value3_ID", null);
		mTab.setValue("XX_Characteristic2Value4_ID", null);
		mTab.setValue("XX_Characteristic2Value5_ID", null);
		mTab.setValue("XX_Characteristic2Value6_ID", null);
		mTab.setValue("XX_Characteristic2Value7_ID", null);
		mTab.setValue("XX_Characteristic2Value8_ID", null);
		mTab.setValue("XX_Characteristic2Value9_ID", null);
		mTab.setValue("XX_Characteristic2Value10_ID", null);
		

		mTab.setValue("XX_IsGeneratedCharac1Value1", "N");
		mTab.setValue("XX_IsGeneratedCharac1Value2", "N");
		mTab.setValue("XX_IsGeneratedCharac1Value3", "N");
		mTab.setValue("XX_IsGeneratedCharac1Value4", "N");
		mTab.setValue("XX_IsGeneratedCharac1Value5", "N");
		mTab.setValue("XX_IsGeneratedCharac1Value6", "N");
		mTab.setValue("XX_IsGeneratedCharac1Value7", "N");
		mTab.setValue("XX_IsGeneratedCharac1Value8", "N");
		mTab.setValue("XX_IsGeneratedCharac1Value9", "N");
		mTab.setValue("XX_IsGeneratedCharac1Value10", "N");
		
		mTab.setValue("XX_IsGeneratedCharac2Value1", "N");
		mTab.setValue("XX_IsGeneratedCharac2Value2", "N");
		mTab.setValue("XX_IsGeneratedCharac2Value3", "N");
		mTab.setValue("XX_IsGeneratedCharac2Value4", "N");
		mTab.setValue("XX_IsGeneratedCharac2Value5", "N");
		mTab.setValue("XX_IsGeneratedCharac2Value6", "N");
		mTab.setValue("XX_IsGeneratedCharac2Value7", "N");
		mTab.setValue("XX_IsGeneratedCharac2Value8", "N");
		mTab.setValue("XX_IsGeneratedCharac2Value9", "N");
		mTab.setValue("XX_IsGeneratedCharac2Value10", "N");
		
		mTab.setValue("XX_GenerateMatrix", "N");
		mTab.setValue("XX_ShowMatrix", "N");
		mTab.setValue("XX_DeleteMatrix", "N"); 
		
		mTab.setValue("Qty", 0); 
		mTab.setValue("SaleQty", 0); 
		mTab.setValue("XX_GiftsQty", 0); 
		
		
		if(mTab.getValue("XX_VMR_PO_LineRefProv_ID")!=null){
			
			String SQL =
				"Delete XX_VMR_REFERENCEMATRIX " 
				+"where XX_VMR_PO_LINEREFPROV_ID="+(Integer)mTab.getValue("XX_VMR_PO_LineRefProv_ID");
			
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			
			try 
			{
				pstmt = DB.prepareStatement(SQL, null);
				rs = pstmt.executeQuery();					
				rs.close();
				pstmt.close();
								
			}
			catch(Exception e)
			{	
				return "Error al borrar Matriz de Caracteristicas";
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		
		setCalloutActive(false);
		
		return "";
	}
	
	
	public String CharacteristicXValueX (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{   
		if (isCalloutActive() || value==null)
			return "";
		
		setCalloutActive(true);
		
		value=mField.getValue();
		
		if(value!=null){
			mTab.setValue("XX_GenerateMatrix","Y");
			mTab.setValue("XX_ShowMatrix","N");
		}
		
		setCalloutActive(false);
		return "";
	}
	
	public String ModifyUnitPurchasePrice (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		if (isCalloutActive() || value==null)
			return "";
		setCalloutActive(true);
		
		BigDecimal UnitPurchasePrice = (BigDecimal)mTab.getValue("XX_UnitPurchasePrice");
		
		if (0==(UnitPurchasePrice.compareTo(new BigDecimal(0))))
		{
			setCalloutActive(false);
			return "";
			
		}
		
		UnitPurchasePrice = UnitPurchasePrice.setScale (4,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_UnitPurchasePrice", UnitPurchasePrice);
		
		Integer TaxCategory_ID = (Integer)mTab.getValue("C_TaxCategory_ID");
		
		String sql = "SELECT rate"
					+ " FROM C_Tax"
					+ " WHERE ValidFrom="			
					+ " (SELECT MAX(ValidFrom)"											
					+ " FROM C_Tax"
					+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+")";	


		PreparedStatement prst = DB.prepareStatement(sql,null);
		BigDecimal Tax = new BigDecimal(1);
		try {
			ResultSet rs = prst.executeQuery();

			while (rs.next()){
				Tax = rs.getBigDecimal("rate");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al bucar la tasa de impuesto " + e);
		}	

		sql = "SELECT XX_UnitConversion"											//////////////////////
			+ " FROM xx_vmr_unitconversion"
			+ " WHERE XX_VMR_UNITCONVERSION_ID=" + (Integer)mTab.getValue("XX_VMR_UnitConversion_ID");						//////////////////////
		
		Integer PurchaseUnit = 0;
		
		prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				PurchaseUnit = rs.getInt("XX_UnitConversion");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al obtener la cantidad de compra");
		}	

		
		sql = "SELECT XX_UnitConversion"											//////////////////////
			+ " FROM xx_vmr_unitconversion"
			+ " WHERE XX_VMR_UNITCONVERSION_ID=" + (Integer)mTab.getValue("XX_PiecesBySale_ID");		
		
		Integer SaleUnit = 0;
		
		prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				SaleUnit = rs.getInt("XX_UnitConversion");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al obtener la cantidad de venta");
		}	
		
		BigDecimal Descuento1 = (BigDecimal)mTab.getValue("XX_Rebate1");
		
		BigDecimal Descuento2 = (BigDecimal)mTab.getValue("XX_Rebate2");
		
		BigDecimal Descuento3 = (BigDecimal)mTab.getValue("XX_Rebate3");
		
		BigDecimal Descuento4 = (BigDecimal)mTab.getValue("XX_Rebate4");
		
		BigDecimal CostoNacional = UnitPurchasePrice;
		

		if (Descuento1!=null && 0!=(Descuento1.compareTo(new BigDecimal(0))) )
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento1.multiply(new BigDecimal(0.01))));
		if (Descuento2!=null && 0!=(Descuento2.compareTo(new BigDecimal(0))) )
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento2.multiply(new BigDecimal(0.01))));
		if (Descuento3!=null && 0!=(Descuento3.compareTo(new BigDecimal(0))))
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento3.multiply(new BigDecimal(0.01))));
		if (Descuento4!=null &&  0!=(Descuento4.compareTo(new BigDecimal(0))))
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento4.multiply(new BigDecimal(0.01))));

		mTab.setValue("XX_CostWithDiscounts", CostoNacional.setScale (4,BigDecimal.ROUND_HALF_UP));
		
		// Es el costo que le voy a cancelar realmente al proveedor en su moneda y con los descuentos
		BigDecimal CostoConDescuento = CostoNacional;
		
		String OrderType_ID = (String)ctx.getContext(WindowNo, "XX_OrderType");
		BigDecimal FactorEstimado = new BigDecimal(ctx.getContext(WindowNo, "XX_EstimatedFactor"));
		BigDecimal FactorReposicion = new BigDecimal(ctx.getContext(WindowNo, "XX_ReplacementFactor"));

		
		CostoNacional = CostoNacional.setScale (4,BigDecimal.ROUND_HALF_UP);
		 mTab.setValue("PriceActual", CostoNacional);
		 

		 
		
		if (OrderType_ID.equals("Importada"))	// Si la orden de compra es Internacional
		{
			Integer ConversionRate_ID = new Integer(ctx.getContext(WindowNo, "XX_ConversionRate_ID"));
			X_C_Conversion_Rate  cr = new X_C_Conversion_Rate(ctx, ConversionRate_ID, null);
			BigDecimal conversion = cr.getMultiplyRate();
			System.out.println("conversion " + conversion); 
			
			BigDecimal costoMostrado = CostoNacional.multiply(FactorEstimado);
			costoMostrado = costoMostrado.setScale (2,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("PriceActual", costoMostrado);
			CostoNacional = CostoNacional.multiply(FactorReposicion);
			CostoNacional = CostoNacional.setScale (4,BigDecimal.ROUND_HALF_UP);
		}

		BigDecimal Costo = (CostoNacional.divide(new BigDecimal(PurchaseUnit), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(SaleUnit));
		Costo = Costo.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		 //Agregado GHUCHET - Costo comercial
		 mTab.setValue("XX_TradeCost", Costo);
		 
		//BigDecimal LineQty = new BigDecimal(Qty*PurchaseUnit);
		Integer Qty = (Integer)mTab.getValue("Qty");	
		BigDecimal LineQty = new BigDecimal(Qty);
		mTab.setValue("XX_LineQty", LineQty);
		
		BigDecimal LineNetAmt = new BigDecimal(0);

		LineNetAmt = CostoConDescuento.multiply(new BigDecimal(Qty));
		LineNetAmt = LineNetAmt.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		if(LineNetAmt.compareTo(BigDecimal.ZERO)>0)
			mTab.setValue("LineNetAmt", LineNetAmt);
		
		
		if (((BigDecimal)mTab.getValue("XX_SalePricePlusTax")).intValue()==0 | mField.getColumnName().equals("XX_Margin"))
		{
			
			BigDecimal Margin = (BigDecimal)mTab.getValue("XX_Margin");
			if (0==(Margin.compareTo(new BigDecimal(100)))) { Margin = new BigDecimal(99); mTab.setValue("XX_Margin", Margin);}
			
			BigDecimal SalePrice = Costo.divide((new BigDecimal(1.00)).subtract(Margin.divide(new BigDecimal(100.00), 4, RoundingMode.HALF_UP)), 4, RoundingMode.HALF_UP);
			SalePrice = SalePrice.setScale (2,BigDecimal.ROUND_HALF_UP);
			
			BigDecimal SalePricePlusTax = SalePrice.add(SalePrice.multiply(Tax.multiply(new BigDecimal(0.01))));
			SalePricePlusTax = SalePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("XX_SalePricePlusTax", SalePricePlusTax);
	
			setCalloutActive(false);
			return ModifyPVP(ctx,  WindowNo, mTab, mField, value);
			
		} else
		{
			
			BigDecimal SalePrice = (BigDecimal)mTab.getValue("XX_SalePrice");
			
			BigDecimal Margin = ((SalePrice.subtract(Costo)).divide(SalePrice, 4, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
			Margin = Margin.setScale (2,BigDecimal.ROUND_HALF_UP);
			
			mTab.setValue("XX_Margin", Margin);		
	
			setCalloutActive(false);
			
			return "";
		}
		
		
		///////////////////////////////////////////////////////////////////////////////////////
		// Esta parte de abajo que esta comentada es como lo tenia en desarrollo. La parte de arriba es
		// como lo tenia en produccion
		//////////////////////////////////////////////////////////////////////////////////////
/**
		if (isCalloutActive() || value==null)
			return "";
		setCalloutActive(true);
			
		
		
		BigDecimal UnitPurchasePrice = (BigDecimal)mTab.getValue("XX_UnitPurchasePrice");
		
		if (0==(UnitPurchasePrice.compareTo(new BigDecimal(0))))
		{
			setCalloutActive(false);
			return "";
			
		}
		
		UnitPurchasePrice = UnitPurchasePrice.setScale (4,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_UnitPurchasePrice", UnitPurchasePrice);
		
		Integer TaxCategory_ID = (Integer)mTab.getValue("C_TaxCategory_ID");
		
		String sql = "SELECT rate"
					+ " FROM C_Tax"
					+ " WHERE ValidFrom="			
					+ " (SELECT MAX(ValidFrom)"											
					+ " FROM C_Tax"
					+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+")";	


		PreparedStatement prst = DB.prepareStatement(sql,null);
		BigDecimal Tax = new BigDecimal(1);
		try {
			ResultSet rs = prst.executeQuery();

			while (rs.next()){
				Tax = rs.getBigDecimal("rate");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al bucar la tasa de impuesto " + e);
		}	

		sql = "SELECT XX_UnitConversion"											//////////////////////
			+ " FROM xx_vmr_unitconversion"
			+ " WHERE XX_VMR_UNITCONVERSION_ID=" + (Integer)mTab.getValue("XX_VMR_UnitConversion_ID");						//////////////////////
		
		Integer PurchaseUnit = 0;
		
		prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				PurchaseUnit = rs.getInt("XX_UnitConversion");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al obtener la cantidad de compra");
		}	

		
		sql = "SELECT XX_UnitConversion"											//////////////////////
			+ " FROM xx_vmr_unitconversion"
			+ " WHERE XX_VMR_UNITCONVERSION_ID=" + (Integer)mTab.getValue("XX_PiecesBySale_ID");		
		
		Integer SaleUnit = 0;
		
		prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				SaleUnit = rs.getInt("XX_UnitConversion");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al obtener la cantidad de venta");
		}	
		
		BigDecimal Descuento1 = (BigDecimal)mTab.getValue("XX_Rebate1");
		
		BigDecimal Descuento2 = (BigDecimal)mTab.getValue("XX_Rebate2");
		
		BigDecimal Descuento3 = (BigDecimal)mTab.getValue("XX_Rebate3");
		
		BigDecimal Descuento4 = (BigDecimal)mTab.getValue("XX_Rebate4");
		
		BigDecimal CostoNacional = UnitPurchasePrice;

		if (Descuento1!=null && 0!=(Descuento1.compareTo(new BigDecimal(0))) )
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento1.multiply(new BigDecimal(0.01))));
		if (Descuento2!=null && 0!=(Descuento2.compareTo(new BigDecimal(0))) )
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento2.multiply(new BigDecimal(0.01))));
		if (Descuento3!=null && 0!=(Descuento3.compareTo(new BigDecimal(0))))
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento3.multiply(new BigDecimal(0.01))));
		if (Descuento4!=null &&  0!=(Descuento4.compareTo(new BigDecimal(0))))
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento4.multiply(new BigDecimal(0.01))));

		mTab.setValue("XX_CostWithDiscounts", CostoNacional);
		
		String OrderType_ID = (String)ctx.getContext(WindowNo, "XX_OrderType");
		BigDecimal FactorEstimado = new BigDecimal(ctx.getContext(WindowNo, "XX_EstimatedFactor"));
		BigDecimal FactorReposicion = new BigDecimal(ctx.getContext(WindowNo, "XX_ReplacementFactor"));
		
		CostoNacional = CostoNacional.setScale (4,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("PriceActual", CostoNacional);
		
		if (OrderType_ID.equals("Importada"))	// Si la orden de compra es Internacional
		{
			System.out.println("Importada");
			BigDecimal costoMostrado = CostoNacional.multiply(FactorEstimado);
			costoMostrado = costoMostrado.setScale (4,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("PriceActual", costoMostrado);
			CostoNacional = CostoNacional.multiply(FactorReposicion);
			CostoNacional = CostoNacional.setScale (4,BigDecimal.ROUND_HALF_UP);
		}

		BigDecimal Costo = (CostoNacional.divide(new BigDecimal(PurchaseUnit), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(SaleUnit));
		Costo = Costo.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		if (((BigDecimal)mTab.getValue("XX_SalePricePlusTax")).intValue()==0 | mField.getColumnName().equals("XX_Margin") )
		{
			
			BigDecimal Margin = (BigDecimal)mTab.getValue("XX_Margin");
		
			if (0==(Margin.compareTo(new BigDecimal(100)))) { 
				Margin = new BigDecimal(99); 
				mTab.setValue("XX_Margin", Margin);
			}
			
			BigDecimal SalePrice = Costo.divide((new BigDecimal(1.00)).subtract(Margin.divide(new BigDecimal(100.00), 2, RoundingMode.HALF_UP)), 2, RoundingMode.HALF_UP);
			SalePrice = SalePrice.setScale (4,BigDecimal.ROUND_HALF_UP);
			
			BigDecimal SalePricePlusTax = SalePrice.add(SalePrice.multiply(Tax.multiply(new BigDecimal(0.01))));
			SalePricePlusTax = SalePricePlusTax.setScale (4,BigDecimal.ROUND_HALF_UP);
			
			System.out.println("columname: "+SalePricePlusTax);
			mTab.setValue("XX_SalePricePlusTax", SalePricePlusTax);
	
			setCalloutActive(false);
			return ModifyPVP(ctx,  WindowNo, mTab, mField, value);
			
		} else
		{
			
			BigDecimal SalePrice = (BigDecimal)mTab.getValue("XX_SalePrice");
			
			BigDecimal Margin = ((SalePrice.subtract(Costo)).divide(SalePrice, 4, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
			Margin = Margin.setScale (4,BigDecimal.ROUND_HALF_UP);
			
			mTab.setValue("XX_Margin", Margin);		
			
			//BigDecimal LineQty = new BigDecimal(Qty*PurchaseUnit);
			Integer Qty = (Integer)mTab.getValue("Qty");	
			BigDecimal LineQty = new BigDecimal(Qty);
			mTab.setValue("XX_LineQty", LineQty);
			
			BigDecimal LineNetAmt = new BigDecimal(0);
			BigDecimal CostoConDescuento = (BigDecimal)mTab.getValue("XX_CostWithDiscounts");

			LineNetAmt = CostoConDescuento.multiply(new BigDecimal(Qty));
			System.out.println("LineNetAmt " + LineNetAmt);
			LineNetAmt = LineNetAmt.setScale (4,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("LineNetAmt", LineNetAmt);
	
			setCalloutActive(false);
			return "";
		}
*/
	}	//	amt		
	
	//TODO
	public String priceBeco (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{		
		
		System.out.println("PriceBECO");
		VME_PriceProductCallout priceBecoG = new VME_PriceProductCallout();
		
		return priceBecoG.priceBecoGlobal(ctx, WindowNo, mTab, mField, value, oldValue, 1, Env.ZERO);
	}
	
	public String ModifyPVP (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		System.out.println("ModifyPVP");
		BigDecimal SalePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
		SalePricePlusTax = SalePricePlusTax.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		System.out.println("saleplustaxotra vez:"+SalePricePlusTax);
		mTab.setValue("XX_SalePricePlusTax", SalePricePlusTax);

		if (0==(SalePricePlusTax.compareTo(new BigDecimal(0))))
		{
			setCalloutActive(false);
			return "";
		}

		BigDecimal CostoNacional = (BigDecimal)mTab.getValue("PriceActual");
		
		String OrderType_ID = (String)ctx.getContext(WindowNo, "XX_OrderType");
		BigDecimal FactorEstimado = new BigDecimal(ctx.getContext(WindowNo, "XX_EstimatedFactor"));
		BigDecimal FactorReposicion = new BigDecimal(ctx.getContext(WindowNo, "XX_ReplacementFactor"));
		
		
		if (OrderType_ID.equals("Importada"))	// Si la orden de compra es Internacional
		{
			CostoNacional = CostoNacional.divide(FactorEstimado, 2, RoundingMode.HALF_UP);
			CostoNacional = CostoNacional.multiply(FactorReposicion);
			CostoNacional = CostoNacional.setScale (4,BigDecimal.ROUND_HALF_UP);
		}
		
		Integer Qty = (Integer)mTab.getValue("Qty");		
		Integer TaxCategory_ID = (Integer)mTab.getValue("C_TaxCategory_ID");

		String sql = "SELECT rate"
					+ " FROM C_Tax"
					+ " WHERE ValidFrom="			
					+ " (SELECT MAX(ValidFrom)"											
					+ " FROM C_Tax"
					+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+")";	


		PreparedStatement prst = DB.prepareStatement(sql,null);
		BigDecimal Tax = new BigDecimal(1);
		try {
			ResultSet rs = prst.executeQuery();

			while (rs.next()){
				Tax = rs.getBigDecimal("rate");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al bucar la tasa de impuesto " + e);
		}	

		sql = "SELECT XX_UnitConversion"											//////////////////////
			+ " FROM xx_vmr_unitconversion"
			+ " WHERE XX_VMR_UNITCONVERSION_ID=" + (Integer)mTab.getValue("XX_VMR_UnitConversion_ID");						//////////////////////
		
		Integer PurchaseUnit = 0;
		
		prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				PurchaseUnit = rs.getInt("XX_UnitConversion");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al hacer query de unit conversion en la funcion OnSelectReference");
		}	

		
		sql = "SELECT XX_UnitConversion"											//////////////////////
			+ " FROM xx_vmr_unitconversion"
			+ " WHERE XX_VMR_UNITCONVERSION_ID=" + (Integer)mTab.getValue("XX_PiecesBySale_ID");		
		
		Integer SaleUnit = 0;
		
		prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				SaleUnit = rs.getInt("XX_UnitConversion");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al hacer query de unit conversion en la funcion OnSelectReference");
		}				
		
		BigDecimal Costo = (CostoNacional.divide(new BigDecimal(PurchaseUnit), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(SaleUnit));
		Costo = Costo.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		String mensaje = "";
		// Aca busco los ids de los departamentos a los que no le aplica precio Beco 45,46,47
		// y luego la linea 31 en los departamentos 59 y 26
		sql = "SELECT value "											
			+ "FROM XX_VMR_DEPARTMENT "
			+ "WHERE XX_VMR_DEPARTMENT_id=" + (Integer)ctx.getContextAsInt(WindowNo, "XX_VMR_Department_ID");
		
		Integer departamento = 0;
		
		prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				departamento = rs.getInt("value");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al buscar el codigo del departamento de la orden");
		}	
		Integer linea = 0;

		if (departamento==59 || departamento==26 || departamento==10)
		{
			sql = "SELECT value "											
				+ "FROM XX_VMR_LINE "
				+ "WHERE XX_VMR_LINE_id=" + mTab.getValue("XX_VMR_LINE_ID");
			
			prst = DB.prepareStatement(sql,null);
			try {
				ResultSet rs = prst.executeQuery();
				if (rs.next()){
					linea = rs.getInt("value");
				}
				rs.close();
				prst.close();
			} catch (SQLException e){
				System.out.println("Error al buscar el codigo de la linea de la orden");
			}	
		}

		if (departamento!=45 && departamento!=46 && departamento!=47 && 
				!((departamento==59 || departamento==26) && linea==31 ) && !(departamento==10 && linea==37))
			mensaje = priceBeco (ctx, WindowNo,mTab, mField, value, value);	
		
		SalePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
		System.out.println("SalePricePlusTax mio 1 " + SalePricePlusTax);
			
		if (0==(SalePricePlusTax.compareTo(new BigDecimal(0))))
		{
			setCalloutActive(false);
			return "";
		}
		
		BigDecimal SalePrice = (SalePricePlusTax.multiply(new BigDecimal(100))).divide((Tax.add(new BigDecimal(100))),4,RoundingMode.HALF_UP);
		SalePrice = SalePrice.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		System.out.println("Saleprice2: "+SalePrice);
		mTab.setValue("XX_SalePrice", SalePrice);	

		
		BigDecimal TaxAmount = SalePricePlusTax.subtract(SalePrice);
		TaxAmount = TaxAmount.setScale (4,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_TaxAmount", TaxAmount);	
		System.out.println("TaxAmount3: "+TaxAmount);
		
		BigDecimal Margin = ((SalePrice.subtract(Costo)).divide(SalePrice,4, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));		
		Margin = Margin.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		System.out.println("recalculo Margen 4: "+Margin);
		mTab.setValue("XX_MARGIN", Margin);
			
		// Esta parte actualiza los valores del detalle para la cabecera
		

		Integer SaleQty = (Qty*PurchaseUnit)/SaleUnit;				//	cantidad de productos comprados a vender
		
		BigDecimal LinePVPAmount = (SalePrice.multiply(new BigDecimal(SaleQty)));				////////
		LinePVPAmount = LinePVPAmount.setScale (4,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_LinePVPAmount", LinePVPAmount);		
		
		BigDecimal LinePlusTaxAmount = (SalePricePlusTax.multiply(new BigDecimal(SaleQty)));				/////
		LinePlusTaxAmount = LinePlusTaxAmount.setScale (4,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_LinePlusTaxAmount", LinePlusTaxAmount);	
		
		//BigDecimal LineQty = new BigDecimal(Qty*PurchaseUnit);
		  BigDecimal LineQty = new BigDecimal(Qty);

	    mTab.setValue("XX_LineQty", LineQty);
		
		BigDecimal LineNetAmt = new BigDecimal(0);
		
		BigDecimal Descuento1 = (BigDecimal)mTab.getValue("XX_Rebate1");
		
		BigDecimal Descuento2 = (BigDecimal)mTab.getValue("XX_Rebate2");
		
		BigDecimal Descuento3 = (BigDecimal)mTab.getValue("XX_Rebate3");
		
		BigDecimal Descuento4 = (BigDecimal)mTab.getValue("XX_Rebate4");
		
		BigDecimal CostoConDescuento = (BigDecimal)mTab.getValue("XX_CostWithDiscounts");
		
/**		Esta parte de abajo comentada no hace falta y se debe de borrar
		if (Descuento1!=null && 0!=(Descuento1.compareTo(new BigDecimal(0))) )
			CostoConDescuento = CostoConDescuento.subtract(CostoConDescuento.multiply(Descuento1.multiply(new BigDecimal(0.01))));
		if (Descuento2!=null && 0!=(Descuento2.compareTo(new BigDecimal(0))) )
			CostoConDescuento = CostoConDescuento.subtract(CostoConDescuento.multiply(Descuento2.multiply(new BigDecimal(0.01))));
		if (Descuento3!=null && 0!=(Descuento3.compareTo(new BigDecimal(0))))
			CostoConDescuento = CostoConDescuento.subtract(CostoConDescuento.multiply(Descuento3.multiply(new BigDecimal(0.01))));
		if (Descuento4!=null &&  0!=(Descuento4.compareTo(new BigDecimal(0))))
			CostoConDescuento = CostoConDescuento.subtract(CostoConDescuento.multiply(Descuento4.multiply(new BigDecimal(0.01))));

		LineNetAmt = CostoConDescuento.multiply(new BigDecimal(Qty));
		LineNetAmt = LineNetAmt.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		//TODO Revisar (provisional) el original no tenia el IF
		if(LineNetAmt.compareTo(BigDecimal.ZERO)>0)
			mTab.setValue("LineNetAmt", LineNetAmt);
*/		
		// Mensaje de alerta en caso de que el margen sea mayor a 80%
		if (((BigDecimal)mTab.getValue("XX_Margin")).intValue()>80)
			ADialog.info(1, new Container(), "XX_BigMargin");
		
		//
		setCalloutActive(false);
		return mensaje;
	}	//	amt
	
	public String ModifyQtyPackageMultiple (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{
/**
		if (((BigDecimal)value).compareTo((BigDecimal)oldValue)==0)
			return "";
			*/
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		Integer Qty = (Integer)mTab.getValue("Qty");
		if (Qty==0)
		{
			setCalloutActive(false);
			return "";
		}

		int StdPrecision = ctx.getStdPrecision();
		Integer PackageMultiple = (Integer)mTab.getValue("XX_PACKAGEMULTIPLE");
//		BigDecimal CostoNacional = (BigDecimal)mTab.getValue("PriceActual");
		BigDecimal SalePrice = (BigDecimal)mTab.getValue("XX_SALEPRICE");
		BigDecimal UnitPurchasePrice = (BigDecimal)mTab.getValue("XX_UnitPurchasePrice");
		BigDecimal TaxAmount = (BigDecimal)mTab.getValue("XX_TaxAmount");		
		
		if ((Qty.intValue()%PackageMultiple.intValue())!=0)						/////////////////////
		{ 	
			mTab.setValue("Qty", 0);
			setCalloutActive(false);
			return "Cantidad no corresponde con Multiplo de Empaque";
//			log.saveError("Error", Msg.getMsg(ctx, "Cantidad no corresponde con Multiplo de Empaque")); 	////			

		}
		
		String sql = "SELECT XX_UnitConversion"											//////////////////////
			+ " FROM xx_vmr_unitconversion"
			+ " WHERE XX_VMR_UNITCONVERSION_ID=" + (Integer)mTab.getValue("XX_VMR_UnitConversion_ID");						//////////////////////
		
		Integer PurchaseUnit = 0;
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				PurchaseUnit = rs.getInt("XX_UnitConversion");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al hacer query de unit conversion en la funcion OnSelectReference");
		}	

		
		sql = "SELECT XX_UnitConversion"											//////////////////////
			+ " FROM xx_vmr_unitconversion"
			+ " WHERE XX_VMR_UNITCONVERSION_ID=" + (Integer)mTab.getValue("XX_PiecesBySale_ID");		
		
		Integer SaleUnit = 0;
		
		prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				SaleUnit = rs.getInt("XX_UnitConversion");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al hacer query de unit conversion en la funcion OnSelectReference");
		}
		
		if (SaleUnit==0)
			SaleUnit=1;
		Integer SaleQty = (Qty*PurchaseUnit)/SaleUnit;				//	cantidad individual de productos comprados a vender

		mTab.setValue("SaleQty", SaleQty);
		
		
		BigDecimal Costo = (UnitPurchasePrice.divide(new BigDecimal(PurchaseUnit), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(SaleUnit));
		Costo = Costo.setScale(4, BigDecimal.ROUND_HALF_UP);	
		

		String OrderType_ID = (String)ctx.getContext(WindowNo, "XX_OrderType");	
		
		BigDecimal LineQty = new BigDecimal(Qty);
		mTab.setValue("XX_LineQty", LineQty);
		
		BigDecimal LineNetAmt = new BigDecimal(0);
		
		BigDecimal Descuento1 = (BigDecimal)mTab.getValue("XX_Rebate1");
		
		BigDecimal Descuento2 = (BigDecimal)mTab.getValue("XX_Rebate2");
		
		BigDecimal Descuento3 = (BigDecimal)mTab.getValue("XX_Rebate3");
		
		BigDecimal Descuento4 = (BigDecimal)mTab.getValue("XX_Rebate4");
		
		BigDecimal CostoNacional = UnitPurchasePrice;
		

		if (Descuento1!=null && 0!=(Descuento1.compareTo(new BigDecimal(0))) )
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento1.multiply(new BigDecimal(0.01))));
		if (Descuento2!=null && 0!=(Descuento2.compareTo(new BigDecimal(0))) )
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento2.multiply(new BigDecimal(0.01))));
		if (Descuento3!=null && 0!=(Descuento3.compareTo(new BigDecimal(0))))
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento3.multiply(new BigDecimal(0.01))));
		if (Descuento4!=null &&  0!=(Descuento4.compareTo(new BigDecimal(0))))
			CostoNacional = CostoNacional.subtract(CostoNacional.multiply(Descuento4.multiply(new BigDecimal(0.01))));
	

		LineNetAmt = CostoNacional.multiply(LineQty);
		LineNetAmt = LineNetAmt.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		if(LineNetAmt.compareTo(BigDecimal.ZERO)>0)
			mTab.setValue("LineNetAmt", LineNetAmt);
		
		
		
		
		
/**		Esta parte creo que no va: susceptible de borrar
		 if (OrderType_ID.equals("Importada"))	
		{
			LineNetAmt = UnitPurchasePrice.multiply(new BigDecimal(Qty));
			LineNetAmt = LineNetAmt.setScale (4,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("LineNetAmt", LineNetAmt);
		}		
		else
		{
			LineNetAmt = CostoNacional.multiply(new BigDecimal(Qty));
			LineNetAmt = LineNetAmt.setScale (4,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("LineNetAmt", LineNetAmt);
		}
	*/	
		BigDecimal LinePVPAmount = SalePrice.multiply(new BigDecimal(SaleQty));
		LinePVPAmount = LinePVPAmount.setScale (4,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_LinePVPAmount", LinePVPAmount);
		
		BigDecimal SalePricePlusTax = SalePrice.add(TaxAmount); 
		SalePricePlusTax = SalePricePlusTax.setScale (4,BigDecimal.ROUND_HALF_UP);
		
		BigDecimal LinePlusTaxAmount = (SalePricePlusTax.multiply(new BigDecimal(SaleQty)));
		LinePlusTaxAmount = LinePlusTaxAmount.setScale (4,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_LinePlusTaxAmount", LinePlusTaxAmount);			
	
		//
		setCalloutActive(false);
		return "";
	}	//	amt	
	
	public String onSelectLine(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		mTab.setValue("XX_VMR_VendorProdRef_ID", 0);
		
		setCalloutActive(false);
		return "";
	}
	
	public String onSelectSection(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		Integer Section_ID = (Integer)mTab.getValue("XX_VMR_Section_ID");
		BigDecimal RefPrice = new BigDecimal(0);
		
		String sql = "select avg(P.pricelist)as valor" 
						+ " from m_productprice P, m_product M"
						+ " where M.XX_VMR_Section_id ="+ Section_ID + " and P.m_product_id = M.m_product_id";
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				RefPrice = rs.getBigDecimal("valor");
			}
			rs.close();
			prst.close();
		
		} catch (SQLException e){
			System.out.println("Error al buscar el precio referencial " + e);
		}		

		if (RefPrice==null)
		{
			mTab.setValue("XX_LASTSALEPRICE", new BigDecimal(0.00));	
			setCalloutActive(false);
			return "";
		}
		
		RefPrice = RefPrice.setScale (4,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_LASTSALEPRICE", RefPrice);	
		
		setCalloutActive(false);
		return "";
	}
	
	public String OnSelectReference (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		Integer VendorProdRef_ID = (Integer)mTab.getValue("XX_VMR_VendorProdRef_ID");

		String sql = "SELECT v.XX_VMR_LINE_ID, v.XX_VMR_SECTION_ID, " +
				"v.M_AttributeSet_ID, v.C_TAXCATEGORY_ID, v.XX_PACKAGEMULTIPLE, " +
				"v.XX_VMR_UNITPURCHASE_ID, v.XX_SALEUNIT_ID, v.XX_VMR_UnitConversion_ID, " +
				"v.XX_PiecesBySale_ID, v.XX_VMR_LongCharacteristic_ID, " +
				"(SELECT co.XX_VME_CONCEPTVALUE_ID FROM XX_VMR_CONCEPTVALDPTBRAND co WHERE co.XX_VMR_DEPARTMENT_ID = v.XX_VMR_DEPARTMENT_ID AND co.XX_VMR_BRAND_ID = v.XX_VMR_BRAND_ID AND co.ISACTIVE='Y' AND ROWNUM=1) as XX_VME_CONCEPTVALUE_ID" +
				", XX_VMR_Brand_ID"										
			+ " FROM xx_vmr_vendorprodref v"
			+ " WHERE v.XX_VMR_VendorProdRef_ID=" + VendorProdRef_ID;	
		

		Integer AttributeSet = 0;
		Integer TaxCategory_ID = 0;
		Integer Package = 0;
		Integer Purchase_ID = 0;
		Integer Sale_ID = 0;
		Integer PiecesPurchase_ID = 0;
		Integer PiecesSale_ID = 0;
		Integer LongCharacteristic_ID = 0;
		Integer ConceptValue_ID = 0;
		Integer Brand_ID = 0;
		Integer Line_ID = 0;
		Integer Section_ID = 0;
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				AttributeSet = rs.getInt("M_AttributeSet_ID");
				TaxCategory_ID = rs.getInt("C_TaxCategory_ID");
				Package = rs.getInt("XX_PackageMultiple");
				Purchase_ID = rs.getInt("XX_VMR_UnitPurchase_ID");
				Sale_ID = rs.getInt("XX_SaleUnit_ID");
				PiecesSale_ID = rs.getInt("XX_PiecesBySale_ID");
				PiecesPurchase_ID = rs.getInt("XX_VMR_UnitConversion_ID");
				LongCharacteristic_ID = rs.getInt("XX_VMR_LongCharacteristic_ID");
				ConceptValue_ID = rs.getInt("XX_VME_ConceptValue_ID");
				Brand_ID = rs.getInt("XX_VMR_Brand_ID");
				Line_ID = rs.getInt("XX_VMR_Line_ID");
				Section_ID = rs.getInt("XX_VMR_Section_ID");
			}
			
			rs.close();
			prst.close();
			
		} catch (SQLException e){
			System.out.println("Error al hacer query en la funcion OnSelectReference " + e);
		}

		if (AttributeSet!=0)
		{	
			mTab.setValue("XX_WithCharacteristic", "Y");
		}
		else
		{
			mTab.setValue("XX_WithCharacteristic", "N");
		}	
		mTab.setValue("C_TaxCategory_ID", TaxCategory_ID);
		mTab.setValue("XX_PackageMultiple", Package);
		mTab.setValue("XX_VMR_UnitPurchase_ID", Purchase_ID);
		mTab.setValue("XX_SaleUnit_ID", Sale_ID);		
		mTab.setValue("XX_PiecesBySale_ID", PiecesSale_ID);	
		if (LongCharacteristic_ID!=0)
			mTab.setValue("XX_VMR_LongCharacteristic_ID", LongCharacteristic_ID);
		else
			mTab.setValue("XX_VMR_LongCharacteristic_ID", null);
		mTab.setValue("XX_VME_ConceptValue_ID", ConceptValue_ID);	
		mTab.setValue("XX_VMR_Brand_ID", Brand_ID);
		mTab.setValue("XX_VMR_Line_ID", Line_ID);
		mTab.setValue("XX_VMR_Section_ID", Section_ID);
		mTab.setValue("XX_VMR_UnitConversion_ID", PiecesPurchase_ID);
		
		
		// Seteo del precio referencial
		// Debe ser el ultimo precio de venta en el consecutivo de precio
		
		BigDecimal RefPrice = new BigDecimal(0);
		
		//AGREGADO GHUCHET
		String sqlPrice = "SELECT MAX(C.XX_SALEPRICE) "+
		"\nFROM XX_VMR_PRICECONSECUTIVE C JOIN  M_PRODUCT  P  ON (P.M_PRODUCT_ID = C.M_PRODUCT_ID) "+
		"\nWHERE  P.XX_VMR_VENDORPRODREF_ID = "+VendorProdRef_ID+ 
		"\nAND C.CREATED IN (SELECT MAX(C2.CREATED) "+
		"\nFROM XX_VMR_PRICECONSECUTIVE C2 JOIN  M_PRODUCT  P2  ON (P2.M_PRODUCT_ID = C2.M_PRODUCT_ID) "+
		"\nWHERE  P2.XX_VMR_VENDORPRODREF_ID = "+VendorProdRef_ID+
		"\nAND C2.XX_CONSECUTIVEORIGIN = 'P')";
			
		PreparedStatement pstmtPrice = null;
		ResultSet rsPrice = null;
		try{
			//System.out.println(sqlPrice);
			pstmtPrice = DB.prepareStatement(sqlPrice, null);
			rsPrice = pstmtPrice.executeQuery();
			
			if(rsPrice.next()){
				RefPrice = rsPrice.getBigDecimal(1);
			}		
		}
		catch (SQLException e){
			System.out.println("Error al buscar el precio referencial " + e);
		}finally{
			try {
				rsPrice.close();
				pstmtPrice.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		//FIN GHUCHET
		
		//COMENTADO GHUCHET
		//		sql = "select avg(P.pricelist)as valor" 
		//						+ " from m_productprice P, m_product M"
		//						+ " where M.XX_VMR_Section_ID ="+ Section_ID + " and P.m_product_id = M.m_product_id";
		//
		//	
		//		prst = DB.prepareStatement(sql,null);
		//		try {
		//			ResultSet rs = prst.executeQuery();
		//			if (rs.next()){
		//				RefPrice = rs.getBigDecimal("valor");
		//			}
		//			rs.close();
		//			prst.close();
		//			
		//		} catch (SQLException e){
		//			System.out.println("Error al buscar el precio referencial " + e);
		//		}	
		//FIN COMENTADO GHUCHET
		if (RefPrice==null)
		{
			mTab.setValue("XX_LastSalePrice", new BigDecimal(0.00));	
		}
		else
		{
			RefPrice = RefPrice.setScale (4,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("XX_LastSalePrice", RefPrice);
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try 
		{
			//Margen Estimado (O/C mas reciente del proveedor)
			BigDecimal margin_Aux = null;

			String SQL = "Select a.margin "
					+ "FROM (Select XX_EstimatedMargin margin from C_Order where C_BPartner_ID="
					+ ctx.getContextAsInt(WindowNo, "C_BPartner_ID")
					+ " and XX_EstimatedMargin<>0 Order by Created desc) a " + "WHERE rownum<2";

			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				mTab.setValue("XX_Margin", rs.getBigDecimal("margin"));
				margin_Aux = rs.getBigDecimal("margin");
			}


			rs.close();
			pstmt.close();

			//Si el proveedor no tiene margen anterior (por departamento y mes/año)
			if(margin_Aux==null | margin_Aux.intValue()==0){

				SQL = "SELECT XX_MARGACCORDINGBUDPURCH "
				+"FROM XX_VMR_PRLD01 B, XX_VMR_DEPARTMENT C, XX_VMR_CATEGORY D "
				+"WHERE TO_CHAR (SYSDATE, 'YYYYMM') = B.XX_BUDGETYEARMONTH "
				
				+"AND C.XX_VMR_DEPARTMENT_ID = "+ctx.getContextAsInt(WindowNo, "XX_VMR_Department_ID")+" "
				+"AND B.XX_CODEDEPARTMENT = C.VALUE "
				
				+"AND D.XX_VMR_CATEGORY_ID = "+ctx.getContextAsInt(WindowNo, "XX_VMR_Category_ID")+" "
				+"AND B.XX_CATEGORYCODE = D.VALUE "

				+"AND B.XX_CODESTORE=999 "
				+"AND B.XX_LINECODE=99 "
				+"AND B.XX_CODESECTION=99 ";
				
				pstmt = DB.prepareStatement(SQL, null);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					mTab.setValue("XX_Margin", rs
							.getBigDecimal("XX_MARGACCORDINGBUDPURCH"));
				}

				rs.close();
				pstmt.close();
			}

		} catch (Exception e) {
			e.getStackTrace();
			
		}
		//AGREGADO POR GHUCHET
		/**	Actualiza el mínimo precio de la competencia asociado a la línea y concepto de valor de la referencia de proveedor*/
		BigDecimal minCompetitionPrice = new BigDecimal(0);
		String sqlPriceComp = "SELECT MIN(C.XX_MINPRICE) "+
				"\nFROM XX_VMR_VENDORPRODREF  P  " +
				"\nJOIN XX_VMR_CONCEPTVALDPTBRAND CO ON (CO.XX_VMR_DEPARTMENT_ID = P.XX_VMR_DEPARTMENT_ID "+
				"\nAND CO.XX_VMR_BRAND_ID = P.XX_VMR_BRAND_ID AND CO.ISACTIVE='Y') "+
				"\nLEFT JOIN XX_VMR_COMPETITIONPRICE C  " +
				"\nON (C.XX_VMR_LINE_ID = P.XX_VMR_LINE_ID AND C.XX_VME_CONCEPTVALUE_ID = CO.XX_VME_CONCEPTVALUE_ID) " +
				"\nWHERE P.XX_VMR_VENDORPRODREF_ID = " +VendorProdRef_ID;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try{
			pstmt2 = DB.prepareStatement(sqlPriceComp, null);
			rs2 = pstmt2.executeQuery();			
			if(rs2.next())
				minCompetitionPrice = rs2.getBigDecimal(1);
		}catch (SQLException e){
		}finally{
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
		}	

		if (minCompetitionPrice==null)
			mTab.setValue("XX_MinCompetitionPrice", new BigDecimal(0.00));	
		else {
			minCompetitionPrice = minCompetitionPrice.setScale (4,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("XX_MinCompetitionPrice", minCompetitionPrice);
		}
		System.out.println(minCompetitionPrice);
		//FIN GHUCHET
	
/**		
		Integer Qty = (Integer)mTab.getValue("Qty");
		Integer SaleQty = (Qty*PiecesPurchase)/PiecesSale;				//	cantidad individual de productos comprados a vender

		mTab.setVa("SaleQty", SaleQty);
*/		
		
		//
		setCalloutActive(false);
		return "";
	}	//	amt	
	
}
