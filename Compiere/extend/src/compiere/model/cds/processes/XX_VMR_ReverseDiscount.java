package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_Inventory;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MVMRDiscountType;
import compiere.model.cds.X_XX_VMR_DiscountAppliDetail;
import compiere.model.cds.X_XX_VMR_DiscountRequest;
import compiere.model.cds.X_XX_VMR_PriceConsecutive;

public class XX_VMR_ReverseDiscount extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		
		String DDetail = "";
		String Description = "";
		String DiscDetail = "";
		String DiscLocator = "";
		X_XX_VMR_DiscountAppliDetail discDetail = null;
		MInventory inventory = null;
		MInventoryLine inventoryLine = null;
		X_XX_VMR_PriceConsecutive oldPriceConsecutive = null;
		X_XX_VMR_PriceConsecutive priceConsecutiveToRevert = null;
		XX_ApproveApplication approveApp = new XX_ApproveApplication();
		PreparedStatement prst = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int doctype = 1000336;
		int locator = 0;
		int i=0;
		boolean save=false;
		BigDecimal qtyBook = BigDecimal.ZERO;
		
		/**Obtengo los datos del movimiento a reversar*/
		X_XX_VMR_DiscountRequest discount = new X_XX_VMR_DiscountRequest(Env.getCtx(), getRecord_ID(), get_TrxName());
		X_XX_VMR_DiscountRequest newDisc = new X_XX_VMR_DiscountRequest(Env.getCtx(), 0, get_TrxName());
		Description = "Reverso de la solicitud de rebaja número "+discount.getValue();
		
		/**Otener datos de la solicitud de rebaja*/
		newDisc.setDescription(Description);
		newDisc.setAD_Org_ID(discount.getAD_Org_ID());
		newDisc.setM_Warehouse_ID(discount.getM_Warehouse_ID());
		newDisc.setTotalQty(discount.getTotalQty());
		newDisc.setXX_DateRequest(discount.getXX_DateRequest());
		newDisc.setXX_PrintLabel(discount.getXX_PrintLabel());
		newDisc.set_ValueNoCheck("XX_Status", "RV");
		newDisc.setXX_TotalPVPPlusTax(discount.getXX_TotalPVPPlusTax());
		newDisc.setXX_TotalSpendingOfDiscount(discount.getXX_TotalSpendingOfDiscount());
		newDisc.setXX_VMR_Department_ID(discount.getXX_VMR_Department_ID());
		newDisc.setXX_VMR_DiscountType_ID(discount.getXX_VMR_DiscountType_ID());
		newDisc.setProcessing(true);
		newDisc.save();
		
		/**Se anula la solicitud de rebaja original*/
		discount.set_ValueNoCheck("XX_Status", "AN");
		discount.save();
		commit();
		
		/**Copiar detalle de la rebaja*/
		
		DDetail = "SELECT * FROM XX_VMR_DiscountAppliDetail "
			   + "WHERE XX_VMR_DiscountRequest_ID="+discount.get_ID();
		
		prst = DB.prepareStatement(DDetail,null);
		rs = prst.executeQuery();
		try{
 			while(rs.next()){
 				
				oldPriceConsecutive = new X_XX_VMR_PriceConsecutive(Env.getCtx(),rs.getInt("XX_PriceConsecutive_ID"),get_TrxName());
				
				discDetail = new X_XX_VMR_DiscountAppliDetail(Env.getCtx(), 0, get_TrxName());
				discDetail.setM_Product_ID(rs.getInt("M_Product_ID"));
				discDetail.setXX_AmountRebated((rs.getBigDecimal("XX_DiscountPrice")).subtract((rs.getBigDecimal("XX_PriceBeforeDiscount")).multiply(rs.getBigDecimal("XX_LoweringQuantity"))));
				discDetail.setXX_DiscountPrice(rs.getBigDecimal("XX_PriceBeforeDiscount"));
				discDetail.setXX_LoweringQuantity(rs.getInt("XX_LoweringQuantity"));
				discDetail.setXX_PriceBeforeDiscount(rs.getBigDecimal("XX_DiscountPrice"));
				discDetail.setXX_PriceConsecutive_ID(rs.getInt("XX_VMR_PriceConsecutive_ID"));
				discDetail.setXX_VMR_PriceConsecutive_ID(rs.getInt("XX_PriceConsecutive_ID"));
				discDetail.setXX_SalePricePlusTax(rs.getBigDecimal("XX_SalePricePlusTax"));
				discDetail.setXX_SpendingOfDiscount(rs.getBigDecimal("XX_SpendingOfDiscount"));
				discDetail.setXX_Tax(rs.getBigDecimal("XX_Tax"));
				discDetail.setXX_TotalPrice(rs.getBigDecimal("XX_TotalPrice"));
				System.out.println("Anterior - total: "+rs.getBigDecimal("XX_PriceBeforeDiscount")+" "+rs.getBigDecimal("XX_TotalPrice")+"\n");
				discDetail.setXX_VMR_DiscountRequest_ID(newDisc.getXX_VMR_DiscountRequest_ID());
				discDetail.setXX_VMR_DiscountType_ID(rs.getInt("XX_VMR_DiscountType_ID"));
				discDetail.setXX_VMR_Line_ID(rs.getInt("XX_VMR_Line_ID"));
				discDetail.setXX_VMR_Section_ID(rs.getInt("XX_VMR_Section_ID"));
				discDetail.save();

				/**Desactivar el consecutivo de precio de la rebaja anulada*/
				oldPriceConsecutive.setIsActive(false);
				oldPriceConsecutive.save();
			
			}
		}catch(SQLException e){
			System.out.println(e);
		}finally{
			rs.close();
			prst.close();
		}	
		
		/**Buscar el locator predeterminado para el warehouse*/
		DiscLocator = "SELECT M_Locator_ID "
				    + "FROM M_Locator "
				    + "WHERE IsDefault='Y' AND M_Warehouse_ID=" + newDisc.getM_Warehouse_ID();
		
		try
		{
			pstmt = DB.prepareStatement(DiscLocator, null); 
		    rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{
				locator = rs.getInt("M_Locator_ID");
			}
		}				
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		finally{
			rs.close();
			pstmt.close();
		}
		
		/**Crear registro en inventario fisico para cada rebaja a cero*/
		DiscDetail = "select * from XX_VMR_DiscountAppliDetail where XX_VMR_DiscountRequest_id = " + newDisc.getXX_VMR_DiscountRequest_ID();
		
		try
		{
			pstmt = DB.prepareStatement(DiscDetail, get_TrxName());
			rs = pstmt.executeQuery();
			/**Crear las lineas del inventory*/
			while (rs.next())
			{
				/**Unicamente se reducen las cantidades si se trata de una rebaja a cero*/
				MVMRDiscountType tipoDescuento = new MVMRDiscountType(Env.getCtx(), rs.getInt("XX_VMR_DiscountType_ID"), get_TrxName());
				if (tipoDescuento.getName().contains("CERO"))
				{	
					if(!save){
						/**Cabecera del inventory*/
						inventory = new MInventory( getCtx(), 0, get_TrxName());
						inventory.setC_DocType_ID(doctype);
						inventory.setAD_Org_ID(newDisc.getAD_Org_ID());
						inventory.setM_Warehouse_ID(newDisc.getM_Warehouse_ID());
						inventory.setDescription("Reverso de movimiento de inventario inducido por Rebaja a cero numero " + discount.getValue());
						inventory.save();
						commit();
						save=true;
					}
				
					i++;
					
					inventoryLine = new MInventoryLine( Env.getCtx(), 0, null);
					inventoryLine.setLine(i);
					
					priceConsecutiveToRevert = new X_XX_VMR_PriceConsecutive(Env.getCtx(),rs.getInt("XX_PriceConsecutive_ID"),get_TrxName());
					
					inventoryLine.setM_AttributeSetInstance_ID(priceConsecutiveToRevert.getM_AttributeSetInstance_ID());
					inventoryLine.setAD_Org_ID(newDisc.getAD_Org_ID());
					inventoryLine.setM_Inventory_ID(inventory.get_ID());
					inventoryLine.setM_Locator_ID(locator);
					inventoryLine.setInventoryType("D");
					inventoryLine.setM_Product_ID(rs.getInt("M_Product_ID"));
					
					qtyBook = approveApp.setQtyBook( priceConsecutiveToRevert.getM_AttributeSetInstance_ID(), rs.getInt("M_Product_ID"), locator);
					 
						
					inventoryLine.setQtyBook(qtyBook);
					inventoryLine.setQtyCount(qtyBook.add(rs.getBigDecimal("XX_LoweringQuantity")));
					inventoryLine.save();
					commit();
					System.out.println("CANTIDADES: "+inventoryLine.getQtyBook()+" "+inventoryLine.getQtyCount()+"\n");
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error al actualizar cantidades en inventario");
		}
		finally{
			rs.close();
			pstmt.close();
		}
		
		if(save){
			
			inventory.setDocAction(X_M_Inventory.DOCACTION_Complete);
		    DocumentEngine.processIt(inventory, X_M_Inventory.DOCACTION_Complete);
			inventory.save();
		}
		
		return null;
	}

	@Override
	protected void prepare() {
		
	}

}
