package compiere.model.cds.callouts;

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

import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.M_ProductCallout;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.X_XX_VMR_Line;
import compiere.model.cds.X_XX_VMR_UnitConversion;
import compiere.model.cds.callouts.VME_PriceProductCallout;
import compiere.model.cds.processes.XX_VMR_ChangeStoreQuantities;

public class XXMRDistributionCallout extends CalloutEngine {


	public String PriceCallout (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		String message = "";
		if (isCalloutActive() || value==null) {
			return "";
		}
		setCalloutActive(true);
			
		//Si se modifico el margen
		if (mField.getColumnName().equals("XX_Margin") ) {
			
	
			Integer mproduct_id = (Integer)mTab.getValue("M_Product_ID"); 
			MProduct product = new MProduct(ctx, mproduct_id, null);
			MVMRVendorProdRef ref = new MVMRVendorProdRef(ctx, product.getXX_VMR_VendorProdRef_ID(), null);
			//GHUCHET - Se comenta codigo que tiene que ver con la unidad de compra/venta, ya que el costo en el campo PriceActual ya 
			//fue calculado tomando en cuenta las unidad de compra/venta
			//X_XX_VMR_UnitConversion conversion = new X_XX_VMR_UnitConversion(ctx, ref.getXX_VMR_UnitConversion_ID(), null);
			//int unidad_compra = conversion.getXX_UnitConversion();

			BigDecimal SaleUnitCost = ((BigDecimal)mTab.getValue("PriceActual")); //.divide(new BigDecimal(unidad_compra), RoundingMode.HALF_UP);

			BigDecimal Margin = (BigDecimal)mTab.getValue("XX_Margin");
			//Actualizar el valor del margen, lo cual puede generar recursividad
			if (Margin.compareTo(Env.ONEHUNDRED) == 0) {
				Margin = new BigDecimal(99);
				Margin = Margin.setScale(2);				
				mTab.setValue("XX_Margin", Margin);
			}
			
			//Calcular las cantidades de precio de venta y precio de venta con impuesto
			BigDecimal calcAux = (Env.ONE).subtract(Margin.divide(Env.ONEHUNDRED, 2, RoundingMode.HALF_UP));			
			BigDecimal SalePrice = SaleUnitCost.divide(calcAux, 2, RoundingMode.HALF_UP);						
			mTab.setValue("XX_SalePrice", SalePrice);
			
			//Calcular el impuesto		
			Integer TaxCategory_ID = (Integer)mTab.getValue("C_TaxCategory_ID");		
			String sql = "SELECT rate"
						+ " FROM C_Tax"
						+ " WHERE ValidFrom="			
						+ " (SELECT MAX(ValidFrom)"											
						+ " FROM C_Tax"
						+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+")";				
			PreparedStatement prst = DB.prepareStatement(sql,null);
			BigDecimal Tax = Env.ONE;
			try {
				ResultSet rs = prst.executeQuery();
				if (rs.next()){
					Tax = rs.getBigDecimal("rate");
				}
				rs.close();
				prst.close();
			} catch (SQLException e){
				return "Error al buscar la tasa de impuesto";			
			}
			BigDecimal SalePricePlusTax = SalePrice.add(SalePrice.multiply(Tax.multiply(new BigDecimal(0.01))));
			SalePricePlusTax = SalePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
			//Actualizar			
			mTab.setValue("XX_SalePricePlusTax", SalePricePlusTax);

			//Calcularle el precio beco	

			X_XX_VMR_Department department = 
				new X_XX_VMR_Department(ctx, product.getXX_VMR_Department_ID(), null);
			X_XX_VMR_Line line = new X_XX_VMR_Line(ctx, product.getXX_VMR_Line_ID(), null);
			
			//Buscar los departamentos a los que no se le aplica precio beco: 45,46,47
			// Y la linea 31 en los departamentos 59 y 26
			
			Integer dep_value = Integer.parseInt(department.getValue());
			Integer lin_value = Integer.parseInt(line.getValue());
			
			if ((dep_value == 59 || dep_value == 26) && (lin_value == 31)) {
				//No lleva precio
			} else if (dep_value == 45 || dep_value == 46 || dep_value == 47) {
				//No lleva precio
			} else if (dep_value == 10 && lin_value == 37) {
				//No lleva precio
			} else {				
				//Calcularle el precio beco				
				VME_PriceProductCallout priceBecoG = new VME_PriceProductCallout();					
				message = priceBecoG.priceBecoGlobal(ctx, WindowNo, mTab, mField, SalePricePlusTax, oldValue, 1, Env.ZERO);
				
				if (message.equals("")) {
					mTab.setValue("XX_CanSetDefinitive", "Y");
				} else {
					//Si el precio no esta entre las bandas entonces no lo dejo colocar el precio como definitivo
					mTab.setValue("XX_CanSetDefinitive", "N");
				}
			}			
			SalePrice = 				
				(SalePricePlusTax.multiply(Env.ONEHUNDRED)).divide((Tax.add(Env.ONEHUNDRED)),2,RoundingMode.HALF_UP);
			SalePrice = SalePrice.setScale (2,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("XX_SalePrice", SalePrice);

			BigDecimal TaxAmount = SalePricePlusTax.subtract(SalePrice);
			TaxAmount = TaxAmount.setScale (2,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("XX_TaxAmount", TaxAmount);		 			
			
			if(SalePricePlusTax != mTab.getValue("XX_SalePricePlusTax")){

				recalculateSalePrice( ctx, mTab, WindowNo, mField, mTab.getValue("XX_SalePricePlusTax"));
			}
			
		} else if (mField.getColumnName().equals("XX_SalePricePlusTax")) {
			//Si se modificó el precio de venta mas impuesto
		
			
			BigDecimal SalePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
			if (SalePricePlusTax.compareTo(Env.ZERO) == 0) {
				setCalloutActive(false);
				return "";			
			} 
			Integer mproduct_id = (Integer)mTab.getValue("M_Product_ID"); 
			MProduct product = new MProduct(ctx, mproduct_id, null);
			X_XX_VMR_Department department = 
				new X_XX_VMR_Department(ctx, product.getXX_VMR_Department_ID(), null);
			X_XX_VMR_Line line = new X_XX_VMR_Line(ctx, product.getXX_VMR_Line_ID(), null);
			
			//Buscar los departamentos a los que no se le aplica precio beco: 45,46,47
			// Y la linea 31 en los departamentos 59 y 26
			
			Integer dep_value = Integer.parseInt(department.getValue());
			Integer lin_value = Integer.parseInt(line.getValue());
					
			if ((dep_value == 59 || dep_value == 26) && (lin_value == 31)) {
				//No lleva precio
			} else if (dep_value == 45 || dep_value == 46 || dep_value == 47) {
				//No lleva precio
			} else if (dep_value == 10 && lin_value == 37) {
				//No lleva precio
			} else {				
				//Calcularle el precio beco				
				VME_PriceProductCallout priceBecoG = new VME_PriceProductCallout();					
				message = priceBecoG.priceBecoGlobal(ctx, WindowNo, mTab, mField, SalePricePlusTax, oldValue, 1, Env.ZERO);
				
				if (message.equals("")) {
					mTab.setValue("XX_CanSetDefinitive", "Y");
				} else {
					//Si el precio no esta entre las bandas entonces no lo dejo colocar el precio como definitivo
					mTab.setValue("XX_CanSetDefinitive", "N");
				}
			}

			//Calcular el impuesto		
			Integer TaxCategory_ID = (Integer)mTab.getValue("C_TaxCategory_ID");		
			String sql = "SELECT rate as rate"
						+ " FROM C_Tax"
						+ " WHERE ValidFrom="			
						+ " (SELECT MAX(ValidFrom)"											
						+ " FROM C_Tax"
						+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+")";	

			PreparedStatement prst = DB.prepareStatement(sql,null);
			BigDecimal Tax = new BigDecimal(1);
			try {
				ResultSet rs = prst.executeQuery();
				if (rs.next()){
					Tax = rs.getBigDecimal("rate");
				}
				rs.close();
				prst.close();
			} catch (SQLException e){
				
				return "Error al buscar la tasa de impuesto";			
			}

			BigDecimal SalePrice = (BigDecimal)mTab.getValue("XX_SalePrice");
			//MVMRVendorProdRef ref = new MVMRVendorProdRef(ctx, product.getXX_VMR_VendorProdRef_ID(), null);
			//X_XX_VMR_UnitConversion conversion = new X_XX_VMR_UnitConversion(ctx, ref.getXX_VMR_UnitConversion_ID(), null);
			//int unidad_compra = conversion.getXX_UnitConversion();
			//X_XX_VMR_UnitConversion conversionVenta = new X_XX_VMR_UnitConversion(ctx, ref.getXX_PiecesBySale_ID(), null);
			//int unidad_venta = conversionVenta.getXX_UnitConversion();

			//GHUCHET - Se comenta codigo que tiene que ver con la unidad de compra/venta, ya que el costo en el campo PriceActual ya 
			//fue calculado tomando en cuenta las unidad de compra/venta
			BigDecimal UnitSaleCost = ((BigDecimal)mTab.getValue("PriceActual")); //.divide(new BigDecimal(unidad_compra), RoundingMode.HALF_UP).multiply(new BigDecimal(unidad_venta));

			//Actualizar los valores en la ventana
			SalePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
			SalePrice = 				
				(SalePricePlusTax.multiply(Env.ONEHUNDRED)).divide((Tax.add(Env.ONEHUNDRED)),2,RoundingMode.HALF_UP);
			SalePrice = SalePrice.setScale (2,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("XX_SalePrice", SalePrice);

			BigDecimal TaxAmount = SalePricePlusTax.subtract(SalePrice);
			TaxAmount = TaxAmount.setScale (2,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("XX_TaxAmount", TaxAmount);		
			
			BigDecimal Margin = ((SalePrice.subtract(UnitSaleCost)).divide(SalePrice,4, RoundingMode.HALF_UP)).multiply(Env.ONEHUNDRED);		
			Margin = Margin.setScale (2,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("XX_Margin", Margin);	
			BigDecimal oldvalue = (BigDecimal)mField.getOldValue();	
			if (oldvalue.compareTo(SalePricePlusTax) != 0) {
				mTab.setValue("XX_SalePricePlusTax", SalePricePlusTax);
			} 			
		}
		setCalloutActive(false);
		return message;
	}
	
	private void recalculateSalePrice(Ctx ctx, GridTab mTab, int WindowNo, GridField mField, Object oldValue){
		
		String message = "";
		BigDecimal SalePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
	 
		Integer mproduct_id = (Integer)mTab.getValue("M_Product_ID"); 
		MProduct product = new MProduct( ctx, mproduct_id, null);
		X_XX_VMR_Department department = 
			new X_XX_VMR_Department( ctx, product.getXX_VMR_Department_ID(), null);
		X_XX_VMR_Line line = new X_XX_VMR_Line(ctx, product.getXX_VMR_Line_ID(), null);
		
		//Buscar los departamentos a los que no se le aplica precio beco: 45,46,47
		// Y la linea 31 en los departamentos 59 y 26
		
		Integer dep_value = Integer.parseInt(department.getValue());
		Integer lin_value = Integer.parseInt(line.getValue());
				
		if ((dep_value == 59 || dep_value == 26) && (lin_value == 31)) {
			//No lleva precio
		} else if (dep_value == 45 || dep_value == 46 || dep_value == 47) {
			//No lleva precio
		} else if (dep_value == 10 && lin_value == 37) {
			//No lleva precio
		} else {				
			//Calcularle el precio beco				
			VME_PriceProductCallout priceBecoG = new VME_PriceProductCallout();					
			message = priceBecoG.priceBecoGlobal(ctx, WindowNo, mTab, mField, SalePricePlusTax, oldValue, 1, Env.ZERO);
			
			if (message.equals("")) {
				mTab.setValue("XX_CanSetDefinitive", "Y");
			} else {
				//Si el precio no esta entre las bandas entonces no lo dejo colocar el precio como definitivo
				mTab.setValue("XX_CanSetDefinitive", "N");
			}
		}

		//Calcular el impuesto		
		Integer TaxCategory_ID = (Integer)mTab.getValue("C_TaxCategory_ID");		
		String sql = "SELECT rate as rate"
					+ " FROM C_Tax"
					+ " WHERE ValidFrom="			
					+ " (SELECT MAX(ValidFrom)"											
					+ " FROM C_Tax"
					+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+")";	

		PreparedStatement prst = DB.prepareStatement(sql,null);
		BigDecimal Tax = new BigDecimal(1);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				Tax = rs.getBigDecimal("rate");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			
			return;			
		}

		BigDecimal SalePrice = (BigDecimal)mTab.getValue("XX_SalePrice");
//		MVMRVendorProdRef ref = new MVMRVendorProdRef(ctx, product.getXX_VMR_VendorProdRef_ID(), null);
//		X_XX_VMR_UnitConversion conversion = new X_XX_VMR_UnitConversion(ctx, ref.getXX_VMR_UnitConversion_ID(), null);
//		int unidad_compra = conversion.getXX_UnitConversion();
//		X_XX_VMR_UnitConversion conversionVenta = new X_XX_VMR_UnitConversion(ctx, ref.getXX_PiecesBySale_ID(), null);
//		int unidad_venta = conversionVenta.getXX_UnitConversion();
		//GHUCHET - Se comenta codigo que tiene que ver con la unidad de compra/venta, ya que el costo en el campo PriceActual ya 
		//fue calculado tomando en cuenta las unidad de compra/venta
		BigDecimal UnitSaleCost = ((BigDecimal)mTab.getValue("PriceActual")); //.divide(new BigDecimal(unidad_compra), RoundingMode.HALF_UP).multiply(new BigDecimal(unidad_venta));

		//Actualizar los valores en la ventana
		SalePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
		SalePrice = 				
			(SalePricePlusTax.multiply(Env.ONEHUNDRED)).divide((Tax.add(Env.ONEHUNDRED)),2,RoundingMode.HALF_UP);
		SalePrice = SalePrice.setScale (2,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_SalePrice", SalePrice);

		BigDecimal TaxAmount = SalePricePlusTax.subtract(SalePrice);
		TaxAmount = TaxAmount.setScale (2,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_TaxAmount", TaxAmount);		
		
		BigDecimal Margin = ((SalePrice.subtract(UnitSaleCost)).divide(SalePrice,4, RoundingMode.HALF_UP)).multiply(Env.ONEHUNDRED);		
		Margin = Margin.setScale (2,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_Margin", Margin);	
		BigDecimal oldvalue = (BigDecimal)mField.getOldValue();	
		if (oldvalue.compareTo(SalePricePlusTax) != 0) {
			mTab.setValue("XX_SalePricePlusTax", SalePricePlusTax);
		} 			
		
	}
}
