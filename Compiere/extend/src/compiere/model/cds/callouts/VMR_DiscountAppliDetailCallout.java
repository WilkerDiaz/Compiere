package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDiscountRequest;
import compiere.model.cds.MVMRDiscountType;
import compiere.model.cds.MVMRProductClass;
import compiere.model.cds.X_XX_VMR_DiscountType;
import compiere.model.cds.X_XX_VMR_PriceConsecutive;

/**
 * 
 * @author Realizado por Rosmaira Arvelo
 *
 */

public class VMR_DiscountAppliDetailCallout extends CalloutEngine {
			
	//Muestra la linea y la seccion al seleccionar el producto
	public String ShowLineSection (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer product = (Integer) mField.getValue();
		
		if(product != null){ 
			try 
			{
				MProduct p = new MProduct(ctx, product, null);
				mTab.setValue("XX_VMR_Line_ID", p.getXX_VMR_Line_ID());
				mTab.setValue("XX_VMR_Section_ID", p.getXX_VMR_Section_ID());
			
			}//try
			catch(Exception e)
			{	
				return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
			}//catch
		}//if
		
		return "";
	}//fin ShowLine
	
	//Muestra el Precio base del consecutivo a rebajar + Impuesto al seleccionar el consecutivo de precio
	public String ShowPriceBeforeDiscount (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{			
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		
		Integer priceConsecutive = (Integer) mField.getValue();		
		BigDecimal salePrice = new BigDecimal(0);
		BigDecimal porcentaje = new BigDecimal(0);
		//BigDecimal total = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
		String SQL;
		
		if(priceConsecutive != null)
		{
			try 
			{
				X_XX_VMR_PriceConsecutive pc = new X_XX_VMR_PriceConsecutive(ctx, priceConsecutive, null);
				
				SQL = "SELECT Rate "
					+ "FROM C_Tax t, M_Product p "
					+ "WHERE t.C_TaxCategory_ID=p.C_TaxCategory_ID and M_Product_ID="+pc.getM_Product_ID()
					+ " AND t.VALIDFROM = (select MAX(VALIDFROM) FROM C_TAX where C_TaxCategory_ID=p.C_TaxCategory_ID)";
				
				try {
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();

					while (rs.next()) {
						porcentaje = rs.getBigDecimal("Rate");
					} 
					rs.close();
					pstmt.close();
				}catch (Exception e) {
						System.out.println(e + " " + SQL);
				}
				
				mTab.setValue("XX_PriceBeforeDiscount", pc.getXX_SalePrice());
				
				
				
				
				
				
/**				if(porcentaje!=(new BigDecimal(0)))//Si el producto tiene impuesto se le suma al precio 
				{
					SQL = "SELECT ((c.XX_SalePrice)+(c.XX_SalePrice*t.Rate*0.01)) AS Total "
					    + "FROM XX_VMR_PriceConsecutive c, C_Tax t, M_Product p "
					    + "WHERE c.XX_VMR_PriceConsecutive_ID="+priceConsecutive 
					    + " AND t.C_TaxCategory_ID=p.C_TaxCategory_ID "
					    + "AND p.M_Product_ID="+pc.getM_Product_ID()
	  			        + " AND t.VALIDFROM = (SELECT MAX(VALIDFROM) FROM C_TAX WHERE C_TaxCategory_ID=p.C_TaxCategory_ID)";
	
					try {
						PreparedStatement pstmt = DB.prepareStatement(SQL, null);
						ResultSet rs = pstmt.executeQuery();
	
						while (rs.next()) {
							salePrice = rs.getBigDecimal("Total");
						} 
						rs.close();
						pstmt.close();
					}catch (Exception e) {
							System.out.println(e + " " + SQL);
					}
	
					mTab.setValue("XX_PriceBeforeDiscount", salePrice);
				}
				else //Si el producto no tiene impuesto se deja el precio como esta 
				{
					mTab.setValue("XX_PriceBeforeDiscount", pc.getXX_SalePrice());
				}
*/			
				//if(!total.equals(new BigDecimal(0)))
				//{					
//					ShowPorcDesDiscPrice (ctx,WindowNo,mTab,mField,value,new Object());
			//	}
								
			
			}//try
			catch(Exception e)
			{			
				setCalloutActive(false);
				return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
			}//catch
		}		
		setCalloutActive(false);	
		return "";
		
	}//fin ShowSalePrice
	

	public String ShowPorcDesDiscPrice (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		Integer discRequest = (Integer)mTab.getValue("XX_VMR_DiscountRequest_ID");
		Integer priceConsecutive = (Integer)mTab.getValue("XX_VMR_PriceConsecutive_ID");		
		BigDecimal discountPrice = new BigDecimal(0);
		BigDecimal precioViejo = (BigDecimal)mTab.getValue("XX_PriceBeforeDiscount");
		BigDecimal salePricePlusTax = new BigDecimal(0);
		BigDecimal porcentaje = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		String SQL;
		
		MVMRDiscountRequest discountRequest = new MVMRDiscountRequest(Env.getCtx(),discRequest,null);		
		MVMRDiscountType discType = new MVMRDiscountType(Env.getCtx(),(Integer)value,null);
		BigDecimal porcentajeDescuento = new BigDecimal(discType.getPorcDescuento()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
		MProduct producto = new MProduct(Env.getCtx(), (Integer)mTab.getValue("M_Product_ID"), null);

		if (discType.getPorcDescuento()==100) // si se trata de rebaja a cero
		{
			mTab.setValue("XX_SalePricePlusTax", new BigDecimal(0));
			mTab.setValue("XX_DiscountPrice", new BigDecimal(0));
			mTab.setValue("XX_Tax", new BigDecimal(0));			
		} else
		{
			discountPrice = precioViejo.subtract(precioViejo.multiply(porcentajeDescuento));
			// aca calculo el iva y se lo sumo a discount price y lo guardo en XX_SalePricePlusTax
			
			
			BigDecimal impuesto = new BigDecimal(0);
			
			if (producto.getC_TaxCategory_ID() != 0) {

				String sql_rate = " SELECT (RATE/100) FROM C_TAX " +
				" WHERE C_TaxCategory_ID= ? " +
				" AND ValidFrom <= sysdate " +
				" and rownum = 1 " +
				" order by ValidFrom desc ";
				try {
					PreparedStatement prst_tax = DB.prepareStatement(sql_rate,null);
					prst_tax.setInt(1, producto.getC_TaxCategory_ID() );
					ResultSet rs_tax = prst_tax.executeQuery();
					if (rs_tax.next()){
		
					impuesto = rs_tax.getBigDecimal(1);

					}
					rs_tax.close();
					prst_tax.close();
				} catch (Exception e){
					System.out.println("error al calcular el impuesto");
				}
			}

			mTab.setValue("XX_SalePricePlusTax", discountPrice.add(discountPrice.multiply(impuesto)));
			
			// aca veo si el XX_SalePricePlusTax es precio beco y lo transformo a precio beco
			VME_PriceProductCallout priceBecoG = new VME_PriceProductCallout();		
			priceBecoG.priceBecoGlobal(ctx, WindowNo, mTab, mField, value, oldValue, 1, Env.ZERO);
			
			salePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
			salePricePlusTax = salePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
			
			// inicio victor lo monaco
			// Esta parte envia al precio beco inmediatamente inferior en lugar del superior
			BigDecimal salePricePlusTax2 = salePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
			BigDecimal i = Env.ONE;
			while (salePricePlusTax2.compareTo(salePricePlusTax)==0)
			{
				salePricePlusTax = salePricePlusTax.subtract(i);
				mTab.setValue("XX_SalePricePlusTax", salePricePlusTax);
				priceBecoG = new VME_PriceProductCallout();		
				priceBecoG.priceBecoGlobal(ctx, WindowNo, mTab, mField, value, oldValue, 1, Env.ZERO);
				salePricePlusTax = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
				salePricePlusTax = salePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
				i = i.add(Env.ONE);
			}
			// fin
			
			mTab.setValue("XX_SalePricePlusTax", salePricePlusTax);
			// Le quitamos el monto del impuesto
			discountPrice = salePricePlusTax.divide(impuesto.add(new BigDecimal(1)), 4, RoundingMode.HALF_UP);
			discountPrice = discountPrice.setScale (2,BigDecimal.ROUND_HALF_UP);
			mTab.setValue("XX_DiscountPrice", discountPrice);
			mTab.setValue("XX_Tax", (salePricePlusTax.multiply(impuesto)).setScale (2,BigDecimal.ROUND_HALF_UP));	
		}
		

		
		
			
			/**		// Si es rebaja a cero coloco todos los precios finales en cero
		if (porcentajeDescuento==0)
		{
			mTab.setValue("XX_DiscountPrice", new BigDecimal(0));
			mTab.setValue("XX_SalePricePlusTax", new BigDecimal(0));
		} else
		{
			
		}
*/		
		
/**		
		//Muestra el Precio base del consecutivo a rebajar + Impuesto al seleccionar el consecutivo de precio
		if((priceConsecutive!=null) && (discType.get_ID()!=0))
		{
			try 
			{
				SQL = "SELECT ((c.XX_SalePrice)-(c.XX_SalePrice*d.PorcDescuento*0.01)) AS Total " 
				    + "FROM XX_VMR_PriceConsecutive c, XX_VMR_DiscountType d "
					+ "WHERE c.XX_VMR_PriceConsecutive_ID="+priceConsecutive
					+ " AND d.XX_VMR_DiscountType_ID="+discType.get_ID();

				try {
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();

					while (rs.next()) {
						discountPrice = rs.getBigDecimal("Total");
					} 
					rs.close();
					pstmt.close();
				}catch (Exception e) {
						System.out.println(e + " " + SQL);
				}

				mTab.setValue("XX_DiscountPrice", discountPrice);
			
			}//try
			catch(Exception e)
			{	
				return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
			}//catch
			
			//Busca el impuesto del producto
			if(priceConsecutive != null)
			{
				try 
				{
					X_XX_VMR_PriceConsecutive pc = new X_XX_VMR_PriceConsecutive(ctx, priceConsecutive, null);
					
					SQL = "SELECT Rate "
						+ "FROM C_Tax t, M_Product p "
						+ "WHERE t.C_TaxCategory_ID=p.C_TaxCategory_ID and M_Product_ID="+pc.getM_Product_ID()
						+ " AND t.VALIDFROM = (select MAX(VALIDFROM) FROM C_TAX where C_TaxCategory_ID=p.C_TaxCategory_ID)";
					
					try {
						PreparedStatement pstmt = DB.prepareStatement(SQL, null);
						ResultSet rs = pstmt.executeQuery();

						while (rs.next()) {
							porcentaje = rs.getBigDecimal("Rate");
						} 
						rs.close();
						pstmt.close();
					}catch (Exception e) {
							System.out.println(e + " " + SQL);
					}
				}//try
				catch(Exception e)
				{	
					return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
				}//catch
				
				//Cálculo del monto del impuesto
				BigDecimal montoImpuesto = discountPrice.multiply(porcentaje).divide(new BigDecimal(100));				
				mTab.setValue("XX_Tax", montoImpuesto);
				
				//Cálculo del precio total
				total = discountPrice.add(discountPrice.multiply(porcentaje).divide(new BigDecimal(100)));
				
				//Cálculo del precio beco 
				if(total.equals(new BigDecimal(0)))
				{
					mTab.setValue("XX_SalePricePlusTax", total);
				}
				else
				{
					VME_PriceProductCallout priceBecoG = new VME_PriceProductCallout();
					return priceBecoG.priceBecoGlobal(ctx, WindowNo, mTab, mField, value, oldValue, 2,total);
				}				
			}
		}	
*/		
		setCalloutActive(false);
		return "";
		
	}//fin ShowPorcDescuento
	
	//Calcula el Precio Total y el gasto de la línea
	public String TotalPriceCalculation(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		Integer qty = (Integer)mTab.getValue("XX_LoweringQuantity");
		BigDecimal total = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
		BigDecimal priceDiscount = (BigDecimal)mTab.getValue("XX_DiscountPrice");
		BigDecimal priceBeforeDis = (BigDecimal)mTab.getValue("XX_PriceBeforeDiscount");
		
		//Cálculo del precio total de la línea
		BigDecimal totalPrice = total.multiply(new BigDecimal(qty));
		mTab.setValue("XX_TotalPrice", totalPrice);
		
		//Cálculo de gasto de rebaja de la línea
		BigDecimal totalGasto = priceBeforeDis.subtract(priceDiscount).multiply(new BigDecimal(qty));
		mTab.setValue("XX_SpendingOfDiscount", totalGasto);
		
		return "";
	}
	
	//Muestra en la cabecera, el Porcentaje de descuento al seleccionar el tipo de rebaja
	public String ShowPorcDiscount(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer discountType = (Integer) mTab.getValue("XX_VMR_DiscountType_ID");
				
		if(discountType != null){ //Muestra el Porcentaje de Descuento al seleccionar el tipo de rebaja
			try 
			{
				X_XX_VMR_DiscountType dt = new X_XX_VMR_DiscountType(ctx, discountType, null);
				mTab.setValue("PorcDescuento", dt.getPorcDescuento());
			
			}
			catch(Exception e)
			{	
				return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
			}
		}
		
		return "";
	}
}
