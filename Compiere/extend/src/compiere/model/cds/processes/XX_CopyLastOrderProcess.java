package compiere.model.cds.processes;

import java.sql.PreparedStatement;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;
import compiere.model.suppliesservices.X_XX_ProductPercentDistrib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.compiere.model.MRole;
import org.compiere.model.X_AD_User;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class XX_CopyLastOrderProcess extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		MOrder aux = new MOrder(getCtx(),getRecord_ID(),get_TrxName());
		/** Purchase of Supplies and Services 
		 * Maria Vintimilla Funcion 014 
		 * Copy details an it's distribution for this PO header **/
		String docType = aux.getXX_POType();
		Integer Line_Id = new Integer(0); 
		//System.out.println("DocType es: "+docType);
		
    	if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
    		// Get last approved purchase order  ID
    		String sql3 = "SELECT C_ORDER_ID "
    			+ " FROM C_ORDER "
    			+ " WHERE XX_APPROVALDATE= "
    				+ " (SELECT MAX(XX_APPROVALDATE) "
    				+ " FROM C_ORDER "
    				+ " WHERE C_BPARTNER_ID = " + aux.getC_BPartner_ID() 
    				+ " AND XX_POTYPE = " + aux.getXX_POType()
    			+ " AND XX_ORDERSTATUS = 'AP') " ;
    		//System.out.println("Selecciona la orden: "+sql3);
    		int LastAPPOrder_ID = 0;
    		PreparedStatement prst = DB.prepareStatement(sql3,null);
    		ResultSet rs3 = null;
    		try {
    			rs3 = prst.executeQuery();
    			while (rs3.next()){
    				LastAPPOrder_ID = rs3.getInt("C_ORDER_ID");		
    			}
    		} 
    		catch (SQLException e){
    			System.out.println("Error buscando la última orden de compra aprobada " + e);			
    		}
    		finally {
    			rs3.close();
    			prst.close();
    		}
    		//System.out.println("LastAPPOrder_ID: "+LastAPPOrder_ID);
			String sql4 = "SELECT * "
				+ " FROM C_ORDERLINE "
				+ " WHERE C_ORDER_ID = " + LastAPPOrder_ID;				
			sql4 = MRole.getDefault().addAccessSQL(
					sql4, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);					
			//System.out.println("Selecciona la línea: "+sql4);
			PreparedStatement prst2 = DB.prepareStatement(sql4,null);
			ResultSet rs4 = null;
			try {
				rs4 = prst2.executeQuery();
				while (rs4.next()){
					MOrderLine detail = new MOrderLine(Env.getCtx(),0,get_TrxName());	
					// Set order lines
					Line_Id = rs4.getInt("C_OrderLine_ID");
					detail.setC_Order_ID(aux.getC_Order_ID());
					detail.setC_BPartner_ID(rs4.getInt("C_BPartner_ID"));	
					detail.setIsActive(rs4.getString("ISACTIVE").equals("Y")); 
					detail.setC_Currency_ID(rs4.getInt("C_Currency_ID"));
					detail.setPriceList(rs4.getBigDecimal("PriceList"));
					detail.setC_BPartner_Location_ID(rs4.getInt("C_BPartner_Location_ID"));
					detail.setM_Warehouse_ID(rs4.getInt("M_WAREHOUSE_ID"));
					detail.setDateOrdered(rs4.getTimestamp("DateOrdered"));
					detail.setDatePromised(rs4.getTimestamp("DatePromised"));
					detail.setM_Product_ID(rs4.getInt("M_Product_ID"));
					detail.setC_UOM_ID(rs4.getInt("C_UOM_ID"));
					detail.setLine(rs4.getInt("Line"));
					detail.setQtyOrdered(rs4.getBigDecimal("QtyOrdered"));
					detail.setQtyEntered(rs4.getBigDecimal("QtyEntered"));
					detail.setQtyReserved(rs4.getBigDecimal("QtyReserved"));
					detail.setQtyInvoiced(rs4.getBigDecimal("QtyInvoiced"));
					detail.setQtyReturned(rs4.getBigDecimal("QtyReturned"));
					detail.setPriceEntered(rs4.getBigDecimal("PriceEntered"));
					detail.setPriceActual(rs4.getBigDecimal("PriceActual"));
					detail.setC_Tax_ID(rs4.getInt("C_Tax_ID"));
					detail.setTaxAmt(rs4.getBigDecimal("TaxAmt"));
					detail.setLine(rs4.getInt("LineNetAmt"));
					detail.setXX_DistributionType(rs4.getString("XX_DistributionType"));
					if(rs4.getString("XX_IsPiecesPercentage").equals("Y")){
						detail.setXX_IsPiecesPercentage(true);
					}
					else{
						detail.setXX_IsPiecesPercentage(false);
					}	
					if(rs4.getString("XX_IsDistribApplied").equals("Y")){
						detail.setXX_IsDistribApplied(true);
					}
					else{
						detail.setXX_IsDistribApplied(false);
					}						
					if(rs4.getString("XX_ClearedDistrib").equals("Y")){
						detail.setXX_ClearedDistrib(true);
					}
					else{
						detail.setXX_ClearedDistrib(false);
					}
					detail.save();
					detail.load(get_TrxName());
					// Copy Line's Distribution if any 
				   	String sql5 = "SELECT * "
				   		+ " FROM XX_PRODUCTPERCENTDISTRIB "
				   		+ " WHERE C_ORDERLINE_ID = " + Line_Id;
				   	System.out.println("Selecciona las distribuciones: "+sql5);
				   	PreparedStatement prst5 = DB.prepareStatement(sql5,null);
				   	ResultSet rs5 = null;
				   	try {
				   		rs5 = prst5.executeQuery();
				   		while (rs5.next()){
				   			X_XX_ProductPercentDistrib dist = 
				   				new X_XX_ProductPercentDistrib(Env.getCtx(),0, get_TrxName());
							// Set line's Distributions
				   			dist.setC_Order_ID(detail.getC_Order_ID());
				   			dist.setC_OrderLine_ID(detail.getC_OrderLine_ID());
				   			dist.setXX_Org_ID(rs5.getInt("XX_Org_ID"));
				   			dist.setM_Warehouse_ID(rs5.getInt("M_Warehouse_ID"));
				   			dist.setXX_QuantityPerCC(rs5.getBigDecimal("XX_QuantityPerCC"));
				   			dist.setXX_PercentagePerCC(rs5.getBigDecimal("XX_PercentagePerCC"));
				   			dist.setXX_AmountPerCC(rs5.getBigDecimal("XX_AmountPerCC"));
				   			dist.save();
				   		}// Fin While distrib
				   	}// try distrib 
				   	catch (Exception e){
						log.saveError("ErrorSql distribucion", Msg.getMsg(Env.getCtx(), e.getMessage()));
					}
				   	finally {
				   		rs5.close();
						prst5.close();
				   	}
				}// while detalle
			}//try 
			catch (SQLException e){
				System.out.println("Error Copiando detalles de orden de compra " + e);			
			}
			finally {
				rs4.close();
				prst2.close();
			}
    	}//If doctype Assets/ Services
    	
    	else {
			// conseguir el Id de la ultima orden aprobada para ese departamento y seccion
			String sql1 = "SELECT c_order_id, XX_APPROVALDATE " +
					" FROM C_ORDER " +
					" WHERE C_BPARTNER_ID="+ aux.getC_BPartner_ID()+
					" AND XX_VMR_DEPARTMENT_ID="+aux.getXX_Department()+
					" AND XX_ORDERSTATUS='AP' order by XX_APPROVALDATE DESC";

			int LastOrder_ID = 0;
			PreparedStatement prst = DB.prepareStatement(sql1,null);
			ResultSet rs = null;
			try {
				rs = prst.executeQuery();
				if (rs.next()){
					LastOrder_ID = rs.getInt("C_ORDER_ID");		
				}
			} catch (SQLException e){
				System.out.println("Error al buscar la ultima orden de compra " + e);			
			}	
			finally {
				rs.close();
				prst.close();
			}
			
			sql1 = "SELECT *"
				+ " FROM XX_VMR_PO_LINEREFPROV"
				+ " WHERE C_ORDER_ID=" + LastOrder_ID;	

			prst = DB.prepareStatement(sql1,null);

			try {
				rs = prst.executeQuery();
				while (rs.next()){
					
					MVMRPOLineRefProv detalle = new MVMRPOLineRefProv(Env.getCtx(),0,get_TrxName());
					
					// setea cada uno de los detalles de la orden de compra
					detalle.setXX_IsDefinitive(rs.getString("XX_ISDEFINITIVE").equals("Y")); detalle.setXX_WithCharacteristic(rs.getString("XX_WITHCHARACTERISTIC").equals("Y")); detalle.setXX_SaleUnit_ID(rs.getInt("XX_SaleUnit_ID")); detalle.setXX_PiecesBySale_ID(rs.getInt("XX_PiecesBySale_ID"));  detalle.setXX_VME_ConceptValue_ID(rs.getInt("XX_VME_ConceptValue_ID"));
					detalle.setIsActive(rs.getString("ISACTIVE").equals("Y")); detalle.setDescription(rs.getString("DESCRIPTION")); detalle.setXX_SalePricePlusTax(rs.getBigDecimal("XX_SALEPRICEPLUSTAX")); detalle.setXX_VMR_UnitPurchase_ID(rs.getInt("XX_VMR_UnitPurchase_ID")); detalle.setXX_VMR_UnitConversion_ID(rs.getInt("XX_VMR_UnitConversion_ID")); detalle.setXX_Characteristic1_ID(rs.getInt("XX_CHARACTERISTIC1_ID")); detalle.setXX_Characteristic2_ID(rs.getInt("XX_CHARACTERISTIC2_ID"));
					detalle.setXX_LinePlusTaxAmount(rs.getBigDecimal("XX_LINEPLUSTAXAMOUNT")); detalle.setXX_LinePVPAmount(rs.getBigDecimal("XX_LINEPVPAMOUNT")); detalle.setXX_Margin(rs.getBigDecimal("XX_MARGIN")); detalle.setXX_SalePrice(rs.getBigDecimal("XX_SALEPRICE")); detalle.setXX_CostPerOrder(rs.getBigDecimal("XX_COSTPERORDER")); detalle.setQty(rs.getInt("QTY")); detalle.setPriceActual(rs.getBigDecimal("PRICEACTUAL")); detalle.setXX_VMR_VendorProdRef_ID(rs.getInt("XX_VMR_VendorProdRef_ID")); detalle.setXX_PackageMultiple(rs.getInt("XX_PACKAGEMULTIPLE")); detalle.setXX_LastSalePrice(rs.getBigDecimal("XX_LASTSALEPRICE"));
					detalle.setLineNetAmt(rs.getBigDecimal("LINENETAMT")); detalle.setXX_VMR_Line_ID(rs.getInt("XX_VMR_Line_ID")); detalle.setXX_VMR_Section_ID(rs.getInt("XX_VMR_Section_ID")); detalle.setC_Order_ID(aux.getC_Order_ID()); detalle.setLine(rs.getBigDecimal("LINE")); 
					detalle.setXX_Characteristic1Value1_ID(rs.getInt("XX_CHARACTERISTIC1VALUE1_ID")); detalle.setXX_Characteristic1Value2_ID(rs.getInt("XX_CHARACTERISTIC1VALUE2_ID")); detalle.setC_TaxCategory_ID(rs.getInt("C_TAXCATEGORY_ID")); detalle.setXX_Characteristic1Value3_ID(rs.getInt("XX_CHARACTERISTIC1VALUE3_ID")); detalle.setXX_Characteristic1Value4_ID(rs.getInt("XX_CHARACTERISTIC1VALUE4_ID")); detalle.setXX_Characteristic1Value5_ID(rs.getInt("XX_CHARACTERISTIC1VALUE5_ID")); detalle.setXX_Characteristic1Value6_ID(rs.getInt("XX_CHARACTERISTIC1VALUE6_ID"));
					detalle.setXX_Characteristic1Value7_ID(rs.getInt("XX_CHARACTERISTIC1VALUE7_ID")); detalle.setXX_Characteristic1Value8_ID(rs.getInt("XX_CHARACTERISTIC1VALUE8_ID")); detalle.setXX_Characteristic1Value9_ID(rs.getInt("XX_CHARACTERISTIC1VALUE9_ID")); detalle.setXX_Characteristic1Value10_ID(rs.getInt("XX_CHARACTERISTIC1VALUE10_ID")); detalle.setXX_Characteristic2Value1_ID(rs.getInt("XX_CHARACTERISTIC2VALUE1_ID")); detalle.setXX_Characteristic2Value2_ID(rs.getInt("XX_CHARACTERISTIC2VALUE2_ID"));
					detalle.setXX_Characteristic2Value3_ID(rs.getInt("XX_CHARACTERISTIC2VALUE3_ID")); detalle.setXX_Characteristic2Value4_ID(rs.getInt("XX_CHARACTERISTIC2VALUE4_ID")); detalle.setXX_Characteristic2Value5_ID(rs.getInt("XX_CHARACTERISTIC2VALUE5_ID")); detalle.setXX_Characteristic2Value6_ID(rs.getInt("XX_CHARACTERISTIC2VALUE6_ID")); detalle.setXX_Characteristic2Value7_ID(rs.getInt("XX_CHARACTERISTIC2VALUE7_ID")); detalle.setXX_Characteristic2Value8_ID(rs.getInt("XX_CHARACTERISTIC2VALUE8_ID"));
					detalle.setXX_Characteristic2Value9_ID(rs.getInt("XX_CHARACTERISTIC2VALUE9_ID")); detalle.setXX_Characteristic2Value10_ID(rs.getInt("XX_CHARACTERISTIC2VALUE10_ID")); detalle.setXX_UnitPurchasePrice(rs.getBigDecimal("XX_UNITPURCHASEPRICE")); detalle.setXX_LineQty(rs.getInt("XX_LINEQTY")); detalle.setXX_GiftsQty(rs.getInt("XX_GIFTSQTY"));	
					detalle.setXX_Rebate4(rs.getBigDecimal("XX_REBATE4")); detalle.setXX_Rebate3(rs.getBigDecimal("XX_REBATE3")); detalle.setXX_Rebate2(rs.getBigDecimal("XX_REBATE2")); detalle.setXX_Rebate1(rs.getBigDecimal("XX_REBATE1")); detalle.setXX_TaxAmount(rs.getBigDecimal("XX_TaxAmount")); detalle.setSaleQty(rs.getInt("SaleQty")); detalle.setXX_AssociateReference(rs.getString("XX_AssociateReference"));	
					detalle.setXX_VMR_LongCharacteristic_ID(rs.getInt("XX_VMR_LongCharacteristic_ID")); detalle.setXX_VMR_Brand_ID(rs.getInt("XX_VMR_Brand_ID"));
					detalle.setXX_ReferenceIsAssociated(rs.getString("XX_ReferenceIsAssociated").equals("Y"));
					detalle.setXX_GenerateMatrix(rs.getString("XX_GenerateMatrix"));
					detalle.setXX_DeleteMatrix(rs.getString("XX_DeleteMatrix"));
					detalle.setXX_ShowMatrix(rs.getString("XX_ShowMatrix"));

					detalle.setXX_IsGeneratedCharac1Value1(rs.getString("XX_IsGeneratedCharac1Value1").equals("Y"));
					detalle.setXX_IsGeneratedCharac1Value2(rs.getString("XX_IsGeneratedCharac1Value2").equals("Y"));
					detalle.setXX_IsGeneratedCharac1Value3(rs.getString("XX_IsGeneratedCharac1Value3").equals("Y"));
					detalle.setXX_IsGeneratedCharac1Value4(rs.getString("XX_IsGeneratedCharac1Value4").equals("Y"));
					detalle.setXX_IsGeneratedCharac1Value5(rs.getString("XX_IsGeneratedCharac1Value5").equals("Y"));
					detalle.setXX_IsGeneratedCharac1Value6(rs.getString("XX_IsGeneratedCharac1Value6").equals("Y"));
					detalle.setXX_IsGeneratedCharac1Value7(rs.getString("XX_IsGeneratedCharac1Value7").equals("Y"));
					detalle.setXX_IsGeneratedCharac1Value8(rs.getString("XX_IsGeneratedCharac1Value8").equals("Y"));
					detalle.setXX_IsGeneratedCharac1Value9(rs.getString("XX_IsGeneratedCharac1Value9").equals("Y"));
					detalle.setXX_IsGeneratedCharac1Value10(rs.getString("XX_IsGeneratedCharac1Value10").equals("Y"));

					detalle.setXX_IsGeneratedCharac2Value1(rs.getString("XX_IsGeneratedCharac2Value1").equals("Y"));
					detalle.setXX_IsGeneratedCharac2Value2(rs.getString("XX_IsGeneratedCharac2Value2").equals("Y"));
					detalle.setXX_IsGeneratedCharac2Value3(rs.getString("XX_IsGeneratedCharac2Value3").equals("Y"));
					detalle.setXX_IsGeneratedCharac2Value4(rs.getString("XX_IsGeneratedCharac2Value4").equals("Y"));
					detalle.setXX_IsGeneratedCharac2Value5(rs.getString("XX_IsGeneratedCharac2Value5").equals("Y"));
					detalle.setXX_IsGeneratedCharac2Value6(rs.getString("XX_IsGeneratedCharac2Value6").equals("Y"));
					detalle.setXX_IsGeneratedCharac2Value7(rs.getString("XX_IsGeneratedCharac2Value7").equals("Y"));
					detalle.setXX_IsGeneratedCharac2Value8(rs.getString("XX_IsGeneratedCharac2Value8").equals("Y"));
					detalle.setXX_IsGeneratedCharac2Value9(rs.getString("XX_IsGeneratedCharac2Value9").equals("Y"));
					detalle.setXX_IsGeneratedCharac2Value10(rs.getString("XX_IsGeneratedCharac2Value10").equals("Y"));
					
					detalle.save();				
					
					sql1 = "SELECT *"
						+ " FROM XX_VMR_REFERENCEMATRIX"
						+ " WHERE XX_VMR_PO_LINEREFPROV_ID=" + rs.getInt("XX_VMR_PO_LineRefProv_ID");	
					
					PreparedStatement prst2 = DB.prepareStatement(sql1,null);
					ResultSet rs2 = prst2.executeQuery();
					String sql2 = "";
					while (rs2.next()){
										X_XX_VMR_ReferenceMatrix matriz = new X_XX_VMR_ReferenceMatrix(getCtx(),0,get_TrxName());
						
						matriz.setXX_VALUE1(rs2.getInt("XX_VALUE1"));
						matriz.setXX_VALUE2(rs2.getInt("XX_VALUE2"));
						matriz.setXX_COLUMN(rs2.getInt("XX_COLUMN"));
						matriz.setXX_ROW(rs2.getInt("XX_ROW"));
						matriz.setXX_QUANTITYC(rs2.getInt("XX_QUANTITYC"));
						matriz.setXX_QUANTITYV(rs2.getInt("XX_QUANTITYV"));
						matriz.setXX_QUANTITYO(rs2.getInt("XX_QUANTITYO"));
						matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
						matriz.setM_Product(rs2.getInt("M_PRODUCT"));
						matriz.save();
			
					}
					rs2.close();
					prst2.close();
			
				}//while
				rs.close();
				prst.close();
			} 
			catch (SQLException e){
				System.out.println("Error al copiar los detalles o la matriz de referencias " + e);			
			}	

    	}//else 
		return "";
	
	}// Fin doIt

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}// Fin prepare

}//Fin XX_CopyLastOrderProcess
