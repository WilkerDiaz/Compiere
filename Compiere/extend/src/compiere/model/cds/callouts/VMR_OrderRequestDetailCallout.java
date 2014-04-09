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


import compiere.model.cds.callouts.VME_PriceProductCallout;

/**
*
* @author José G. Trías
*/
public class VMR_OrderRequestDetailCallout extends CalloutEngine {

	
	public String ModifyUnitPurchasePrice (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{
/**
		if (((BigDecimal)value).compareTo((BigDecimal)oldValue)==0)
			return "";
			*/
		if (isCalloutActive() || value == null)
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

		BigDecimal Margin = (BigDecimal)mTab.getValue("XX_Margin");
		if (0==(Margin.compareTo(new BigDecimal(100)))) { Margin = new BigDecimal(99); mTab.setValue("XX_Margin", Margin);}

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
		
		
		Integer PurchaseUnit = (Integer)mTab.getValue("XX_PiecesBy");

		Integer SaleUnit = (Integer)mTab.getValue("XX_PiecesBySale");
		
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


		String OrderType_ID = (String)ctx.getContext(WindowNo, "XX_OrderType");
		BigDecimal FactorEstimado = new BigDecimal(ctx.getContext(WindowNo, "XX_EstimatedFactor"));
		BigDecimal FactorReposicion = new BigDecimal(ctx.getContext(WindowNo, "XX_ReplacementFactor"));
		
		CostoNacional = CostoNacional.setScale (2,BigDecimal.ROUND_HALF_UP);
		 mTab.setValue("PriceActual", CostoNacional);
		
		if (OrderType_ID.equals("Importada"))	// Si la orden de compra es Internacional
		{
			BigDecimal costoMostrado = CostoNacional.multiply(FactorEstimado);
			costoMostrado = costoMostrado.setScale (2,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("PriceActual", costoMostrado);
			CostoNacional = CostoNacional.multiply(FactorReposicion);
			CostoNacional = CostoNacional.setScale (2,BigDecimal.ROUND_HALF_UP);
		}

		BigDecimal Costo = (CostoNacional.divide(new BigDecimal(PurchaseUnit), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(SaleUnit));
		Costo = Costo.setScale (2,BigDecimal.ROUND_HALF_UP);
		
		BigDecimal SalePrice = Costo.divide((new BigDecimal(1.00)).subtract(Margin.divide(new BigDecimal(100.00), 2, RoundingMode.HALF_UP)), 2, RoundingMode.HALF_UP);
		SalePrice = SalePrice.setScale (2,BigDecimal.ROUND_HALF_UP);
		
		BigDecimal SalePricePlusTax = SalePrice.add(SalePrice.multiply(Tax.multiply(new BigDecimal(0.01))));
		SalePricePlusTax = SalePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_SalePricePlusTax", SalePricePlusTax);

		setCalloutActive(false);
		return ModifyPVP(ctx,  WindowNo, mTab, mField, value);

	}	//	amt		
	
	public String priceBeco (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{			
		try 
		{
				BigDecimal precio = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
				
				VME_PriceProductCallout banda = new VME_PriceProductCallout();
				
				String priceRuleSQL = "select xx_lowrank,xx_highrank,xx_termination,xx_increase,xx_infinitevalue from xx_vme_pricerule order by (xx_lowrank)";
				PreparedStatement priceRulePstmt = DB.prepareStatement(priceRuleSQL, null);
				ResultSet priceRuleRs = priceRulePstmt.executeQuery();
	
				Integer precioInt = precio.intValue();
				BigDecimal precioBig = new BigDecimal(precioInt);
				while(priceRuleRs.next())
				{	
					 if(precioBig.compareTo(priceRuleRs.getBigDecimal("xx_lowrank"))>=0 && precioBig.compareTo(priceRuleRs.getBigDecimal("xx_highrank"))<=0) 
				     {
				    	 Integer incremento = priceRuleRs.getInt("xx_increase");
				    	  
				    	 for(Integer i=priceRuleRs.getInt("xx_lowrank")-1;i<=priceRuleRs.getInt("xx_highrank");i=i+incremento)
				    	 {
				    		 BigDecimal var =new BigDecimal(i);
				    		 
				    		 if(precioBig.compareTo(var) <= 0)
				    		 {
				    			  BigDecimal beco=var;
				    			  
				    			 BigDecimal terminacion = priceRuleRs.getBigDecimal("xx_termination");
				    			 if(terminacion.intValue()==0)
				    			 {
				    				 beco = var.add(terminacion);
				    			 }
				    			 else
				    			 {
				    				var = var.divide(new BigDecimal(10));
				    				Integer aux= var.intValue()*10;
				    				beco = new BigDecimal(aux).add(terminacion);
				    			 }
				    			 //mTab.setValue("PriceList", beco);
				    			 priceRuleRs.close();
				 				 priceRulePstmt.close();
				 				 
				 				 if(beco.compareTo(precio)==0)
				 				 {
				 					return "";//ModifyPVP(ctx, WindowNo, mTab, mField,value);
				 				 }
				 				 else
				 				 {
				 					 mTab.setValue("XX_SalePricePlusTax", beco);
				 					 return banda.priceBandBeco(ctx, WindowNo, mTab, mField, value, oldValue);//banda.priceBandBeco(ctx, WindowNo, mTab, mField, value, oldValue);
				 				 }
				    		 }
 
				    	 }
				     }
				}
				priceRuleRs.close();
				priceRulePstmt.close();
				
				
				return "";//ModifyPVP(ctx, WindowNo, mTab, mField,value);
		}		

		catch(Exception e)
		{	
			return e.getMessage();
		}
	}
	
	public String ModifyPVP (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		BigDecimal SalePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
		SalePricePlusTax = SalePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_SalePricePlusTax", SalePricePlusTax);

		if (0==(SalePricePlusTax.compareTo(new BigDecimal(0))))
		{
			setCalloutActive(false);
			return "";
		}

		BigDecimal CostoNacional = (BigDecimal)mTab.getValue("PriceActual");
		
		
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
		
		System.out.println("XX_SalePricePlusTax"+SalePricePlusTax);
		String mensaje = priceBeco (ctx, WindowNo,mTab, mField, value, value);
		SalePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
		SalePricePlusTax = SalePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
		System.out.println("XX_SalePricePlusTax"+SalePricePlusTax);
		
		SalePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
			
		if (0==(SalePricePlusTax.compareTo(new BigDecimal(0))))
		{
			setCalloutActive(false);
			return "";
		}
		
		BigDecimal SalePrice = (SalePricePlusTax.multiply(new BigDecimal(100))).divide((Tax.add(new BigDecimal(100))),2,RoundingMode.HALF_UP);
		SalePrice = SalePrice.setScale (2,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_SalePrice", SalePrice);	

		
		BigDecimal TaxAmount = SalePricePlusTax.subtract(SalePrice);
		TaxAmount = TaxAmount.setScale (2,BigDecimal.ROUND_HALF_UP);
		mTab.setValue("XX_TaxAmount", TaxAmount);		
		

			
		// Esta parte actualiza los valores del detalle para la cabecera
		

		
		setCalloutActive(false);
		return mensaje;
	}	//	amt
	
		
}
