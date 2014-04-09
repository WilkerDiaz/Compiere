package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;
import compiere.model.suppliesservices.X_XX_ProductPercentDistrib;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;


public class XX_CopyPurchaseOrderProcess extends SvrProcess {
	
	@Override
	protected String doIt() throws Exception {
		
		MOrder aux = new MOrder(getCtx(),getRecord_ID(), null);
		MOrder newaux = new MOrder(getCtx(), 0, get_Trx());
		/** Purchase of Supplies and Services 
		 * Maria Vintimilla Funcion 013 
		 * Modificaciones para permitir copiar O/C de Bienes y Servicios **/
		String docType = aux.getXX_POType();
		
		// Esta parte setea los campos de la cabecera iguales a los de las anterior a excepcion del ID y el document No
	
		newaux.setDocStatus("DR");
		newaux.setDocAction("CO");
		newaux.setAD_Org_ID(aux.getAD_Org_ID());
		newaux.setC_Activity_ID(aux.getC_Activity_ID()); 
		newaux.setC_BPartner_ID(aux.getC_BPartner_ID()); 
		newaux.set_Value("XX_VMR_TradeRepresentative_ID",aux.get_Value("XX_VMR_TradeRepresentative_ID"));  	/**AGREGADO POR GHUCHET */
		newaux.setC_DocType_ID(aux.getC_DocType_ID());
		newaux.setC_Campaign_ID(aux.getC_Campaign_ID()); 
		newaux.setC_CashLine_ID(aux.getC_CashLine_ID()); 
		newaux.setC_Charge_ID(aux.getC_Charge_ID()); 
		newaux.setC_ConversionType_ID(aux.getC_ConversionType_ID()); 
		newaux.setC_Currency_ID(aux.getC_Currency_ID());			
		newaux.setC_DocTypeTarget_ID(aux.getC_DocTypeTarget_ID()); 
		newaux.setC_PaymentTerm_ID(aux.getC_PaymentTerm_ID()); 
		//newaux.setC_Payment_ID(aux.getC_Payment_ID());
		newaux.setC_Project_ID(aux.getC_Project_ID()); 
		newaux.setChargeAmt(aux.getChargeAmt()); 
		newaux.setCopyFrom(aux.getCopyFrom()); 
		newaux.setDateAcct(aux.getDateAcct()); 
		newaux.setDateOrdered(aux.getDateOrdered()); 
		newaux.setDatePrinted(aux.getDatePrinted()); 
		newaux.setDatePromised(aux.getDatePromised()); 
		newaux.setDeliveryViaRule(aux.getDeliveryViaRule()); 
		newaux.setDescription(aux.getDescription()); 
		newaux.setFreightAmt(aux.getFreightAmt()); 
		newaux.setFreightCostRule(aux.getFreightCostRule()); 
		newaux.setGrandTotal(aux.getGrandTotal()); 
		newaux.setInvoiceRule(aux.getInvoiceRule()); 
		newaux.setIsActive(aux.isActive());
		newaux.setIsApproved(aux.isApproved()); 
		newaux.setIsCreditApproved(aux.isCreditApproved()); 
		newaux.setIsDelivered(aux.isDelivered()); 
		newaux.setIsDiscountPrinted(aux.isDiscountPrinted()); 
		newaux.setIsDropShip(aux.isDropShip());
		newaux.setIsInvoiced(aux.isInvoiced()); 
		newaux.setIsPrinted(aux.isPrinted()); 
		newaux.setIsReturnTrx(aux.isReturnTrx()); 
		newaux.setIsSOTrx(aux.isSOTrx()); 
		newaux.setIsSelected(aux.isSelected());
		newaux.setIsSelfService(aux.isSelfService()); 
		newaux.setIsTaxIncluded(aux.isTaxIncluded()); 
		newaux.setIsTransferred(aux.isTransferred()); 
		newaux.setM_PriceList_ID(aux.getM_PriceList_ID()); 
		newaux.setM_RMACategory_ID(aux.getM_RMACategory_ID());
		newaux.setM_ReturnPolicy_ID(aux.getM_ReturnPolicy_ID()); 
		newaux.setM_Shipper_ID(aux.getM_Shipper_ID()); 
		newaux.setOrig_InOut_ID(aux.getOrig_InOut_ID()); 
		newaux.setPOReference(aux.getPOReference()); 
		newaux.setPay_BPartner_ID(aux.getPay_BPartner_ID()); 
		newaux.setPay_Location_ID(aux.getPay_Location_ID()); 
		newaux.setPosted(aux.isPosted());
		newaux.setPriorityRule(aux.getPriorityRule()); 
		newaux.setProcessed(false); 
		newaux.setProcessing(false); 
		newaux.setRef_Order_ID(aux.getRef_Order_ID()); 
		newaux.setSendEMail(aux.isSendEMail()); 
		newaux.setTotalLines(aux.getTotalLines());  
		newaux.setUser1_ID(aux.getUser1_ID());
		newaux.setUser2_ID(aux.getUser2_ID()); 
		newaux.setVolume(aux.getVolume()); 
		newaux.setWeight(aux.getWeight()); 
		newaux.setC_BPartnerSR_ID(aux.getC_BPartnerSR_ID()); 
		newaux.setC_BP_BankAccount_ID(aux.getC_BP_BankAccount_ID());
		newaux.setC_Country_ID(aux.getC_Country_ID()); 
		newaux.setXX_OrderType(aux.getXX_OrderType()); 
		newaux.setXX_Discount1(aux.getXX_Discount1());
		newaux.setXX_Discount2(aux.getXX_Discount2()); 
		newaux.setXX_Discount3(aux.getXX_Discount3()); 
		newaux.setXX_Discount4(aux.getXX_Discount4()); 
		newaux.setXX_BuyersComments(aux.getXX_BuyersComments());
		newaux.setXX_PurchaseOrderComments(aux.getXX_PurchaseOrderComments()); 
		newaux.setXX_VLO_TypeDelivery(aux.getXX_VLO_TypeDelivery());  
		
		newaux.setXX_VLO_ShippingCondition_ID(aux.getXX_VLO_ShippingCondition_ID());
		newaux.setXX_VLO_ArrivalPort_ID(aux.getXX_VLO_ArrivalPort_ID()); 
		newaux.setXX_VLO_DeliveryLocation_ID(aux.getXX_VLO_DeliveryLocation_ID());
//		newaux.setXX_ChangeStatus(aux.getXX_ChangeStatus()); 
		newaux.setXX_MontLimit(aux.getXX_MontLimit()); 
		newaux.setXX_ProductQuantity(aux.getXX_ProductQuantity());
		newaux.setXX_EstimatedFactor(aux.getXX_EstimatedFactor());
		newaux.setXX_DefinitiveFactor(aux.getXX_DefinitiveFactor()); 
		newaux.setAD_User_ID(aux.getAD_User_ID()); 
		newaux.setC_Currency_ID(aux.getC_Currency_ID()); 
		newaux.setC_ConversionType_ID(aux.getC_ConversionType_ID());
		if (aux.getDeliveryRule()!=null) {
			newaux.setDeliveryRule(aux.getDeliveryRule());
		}
		if (aux.getM_Warehouse_ID()!=0) {
			newaux.setM_Warehouse_ID(aux.getM_Warehouse_ID());
		}
		if (aux.getXX_OrderType().equals("Importada")) {
			newaux.setXX_VLO_DispatchRoute_ID(aux.getXX_VLO_DispatchRoute_ID());

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "Select C_CONVERSION_RATE_ID, MultiplyRate " +
			"FROM C_CONVERSION_RATE " +
			"WHERE XX_PERIOD_ID = (Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+getCtx().getAD_Client_ID()+" AND C_CURRENCY_ID="+aux.getC_Currency_ID()+" AND TRUNC(STARTDATE) <= TRUNC(SYSDATE) AND TRUNC( ENDDATE) >=TRUNC(SYSDATE))";
			System.out.println(SQL);
			try 
			{	
				pstmt = DB.prepareStatement(SQL, null);
				rs = pstmt.executeQuery();

				int i = 0;
				while (rs.next()) {
					newaux.setXX_ConversionRate_ID(rs.getInt("C_Conversion_Rate_ID"));
					newaux.setXX_RealExchangeRate(rs.getBigDecimal("MultiplyRate"));
					i++;
				}

				if (i == 0) {
					//return "No existe tasa de conversion para la fecha Y monedas seleccionadas.";
				}

			} catch (Exception e) {
				log.saveError("ErrorSql Tasa de Conversion", Msg.getMsg(getCtx(), e.getMessage()));
			} finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		
		if (aux.getXX_VMR_CancellationMotive_ID()!=0) {
			newaux.setXX_VMR_CancellationMotive_ID(aux.getXX_VMR_CancellationMotive_ID());
		}
		newaux.setXX_OrderStatus("PRO"); 
		newaux.setXX_Void(false); 
		newaux.setPaymentRule(aux.getPaymentRule());
		newaux.setXX_POType(aux.getXX_POType());
		
		// Purchase of Supplies and Services 
		// Maria Vintimilla Funcion 013
    	if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
     		newaux.setXX_PurchaseType(aux.getXX_PurchaseType());
    		newaux.setXX_IVA(aux.getXX_IVA());
    		newaux.setXX_Department(0);
    		newaux.setXX_VMR_Package_ID(0); 
    		newaux.setXX_Collection_ID(0); 
    		newaux.setXX_VMR_Subject_ID(0); 
    		newaux.setXX_Season_ID(0);
    		newaux.setXX_Brochure_ID(0); 
    		newaux.setXX_Category_ID(0);
    		newaux.setXX_ConsigneeName(null);
    		//newaux.setXX_UserBuyer_ID(0);
    		newaux.setXX_ReplacementFactor(new BigDecimal(0));
    		newaux.setXX_EstimatedMargin(new BigDecimal(0)); 
    		newaux.setTotalPVP(new BigDecimal(0)); 
    		newaux.setXX_TotalPVPPlusTax(new BigDecimal(0));
    	}//If doctype
    	else {
    		newaux.setXX_Department(aux.getXX_Department());
    		newaux.setXX_VMR_Package_ID(aux.getXX_VMR_Package_ID()); 
    		newaux.setXX_Collection_ID(aux.getXX_Collection_ID()); 
    		newaux.setXX_VMR_Subject_ID(aux.getXX_VMR_Subject_ID()); 
    		newaux.setXX_Season_ID(aux.getXX_Season_ID());
    		newaux.setXX_Brochure_ID(aux.getXX_Brochure_ID()); 
    		newaux.setXX_Category_ID(aux.getXX_Category_ID());
    		newaux.setXX_ReplacementFactor(aux.getXX_ReplacementFactor());
    		newaux.setXX_ConsigneeName(aux.getXX_ConsigneeName()); 
    		newaux.setXX_UserBuyer_ID(aux.getXX_UserBuyer_ID());
    		newaux.setXX_EstimatedMargin(aux.getXX_EstimatedMargin());
    		newaux.setTotalPVP(aux.getTotalPVP()); 
    		newaux.setXX_TotalPVPPlusTax(aux.getXX_TotalPVPPlusTax()); 
    	}//else doctype
		newaux.setXX_ArrivalDate(null); 
		newaux.setXX_EstimatedDate(null);
		newaux.setSalesRep_ID(aux.getSalesRep_ID());
		newaux.setOrig_Order_ID(aux.get_ID());
		newaux.setXX_ComesFromCopy(true);

		newaux.save();
		commit();
		newaux.load(get_TrxName());
		
		// Purchase of Supplies and Services 
		// Maria Vintimilla Funcion 013 Copy the PO Lines
    	if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
    		// Copy order lines from the last order
    		String sql4 = "SELECT *"
    			+ " FROM C_OrderLine"
    			+ " WHERE C_ORDER_ID=" + aux.getC_Order_ID();
    		System.out.println(sql4);
    		PreparedStatement prst = DB.prepareStatement(sql4,null);

    		try {
    			ResultSet rs4 = prst.executeQuery();
    			while (rs4.next()){
    				int C_OrderLine_ID = rs4.getInt("C_OrderLine_ID");
    				MOrderLine detail = new MOrderLine(Env.getCtx(),0, get_Trx());
					// Set order lines
    				detail.setC_Order_ID(newaux.getC_Order_ID());
    				detail.setAD_Org_ID(newaux.getAD_Org_ID());
    				detail.setAD_Client_ID(newaux.getAD_Client_ID());
					detail.setC_BPartner_ID(rs4.getInt("C_Bpartner_ID"));
					detail.setM_Warehouse_ID(rs4.getInt("M_Warehouse_ID"));
					detail.setC_Currency_ID(rs4.getInt("C_Currency_ID"));
					detail.setM_Product_ID(rs4.getInt("M_Product_ID"));
					detail.setC_UOM_ID(rs4.getInt("C_Uom_ID"));
					detail.setC_Tax_ID(rs4.getInt("C_Tax_ID"));
					detail.setC_BPartner_Location_ID(rs4.getInt("C_BPartner_Location_ID"));
					//detail.setDescription(rs4.getString("Description"));
					detail.setDateOrdered(rs4.getTimestamp("DateOrdered"));
					detail.setDatePromised(rs4.getTimestamp("DatePromised"));
					detail.setLine(rs4.getInt("Line"));
					detail.setQtyOrdered(rs4.getBigDecimal("QtyOrdered"));
					detail.setQtyEntered(rs4.getBigDecimal("QtyEntered"));
					//detail.setQtyReserved(rs4.getBigDecimal("QtyReserved"));
					//detail.setQtyInvoiced(rs4.getBigDecimal("QtyInvoiced"));
					//detail.setQtyReturned(rs4.getBigDecimal("QtyReturned"));
					detail.setPriceEntered(rs4.getBigDecimal("PriceEntered"));
					detail.setPriceActual(rs4.getBigDecimal("PriceActual"));
					detail.setTaxAmt(rs4.getBigDecimal("TaxAmt"));
					detail.setLine(rs4.getInt("LineNetAmt"));
					detail.setIsActive(rs4.getString("IsActive").equals("Y")); 
					detail.setPriceList(rs4.getBigDecimal("PriceList"));
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
					commit();
					
					
					// Copy Line's Distribution if any 
				   	String sql2 = "SELECT *"
				   		+ " FROM XX_ProductPercentDistrib"
				   		+ " WHERE C_OrderLine_ID = " + C_OrderLine_ID;
				   	System.out.println(sql2);
				   	PreparedStatement prst2 = DB.prepareStatement(sql2,null);
				   	try {
				   		ResultSet rs2 = prst2.executeQuery();
				   		while (rs2.next()){
				   			X_XX_ProductPercentDistrib dist = new X_XX_ProductPercentDistrib(Env.getCtx(),0, get_Trx());
							// Set line's Distributions
				   			System.out.println("OrderLineID new Dist:"+detail.getC_OrderLine_ID());
				   			dist.setAD_Client_ID(detail.getAD_Client_ID());
				   			dist.setAD_Org_ID(detail.getAD_Org_ID());
				   			dist.setC_Order_ID(detail.getC_Order_ID());
				   			dist.setC_OrderLine_ID(detail.getC_OrderLine_ID());
				   			dist.setXX_Org_ID(rs2.getInt("XX_Org_ID"));
				   			dist.setM_Warehouse_ID(rs2.getInt("M_Warehouse_ID"));
				   			dist.setXX_QuantityPerCC(rs2.getBigDecimal("XX_QuantityPerCC"));
				   			dist.setXX_PercentagePerCC(rs2.getBigDecimal("XX_PercentagePerCC"));
				   			dist.setXX_AmountPerCC(rs2.getBigDecimal("XX_AmountPerCC"));
				   			dist.save();
				   			commit();
				   		}// Fin While
				   		rs2.close();
						prst2.close();
				   	}// try distrib 
				   	catch (Exception e){
						log.saveError("ErrorSql distribucion", Msg.getMsg(getCtx(), e.getMessage()));
					}
    			}// Fin While
    		}//try detalles 
    		catch (Exception e){
				log.saveError("ErrorSql detalle", Msg.getMsg(getCtx(), e.getMessage()));
			}
		}else{ //if detalle PSS
			String sql1 = "SELECT *"
				+ " FROM XX_VMR_PO_LINEREFPROV"
				+ " WHERE C_ORDER_ID=" + aux.getC_Order_ID();
	
			PreparedStatement prst = DB.prepareStatement(sql1,null);
			ResultSet rs = prst.executeQuery();
			PreparedStatement prst2 = null;
			ResultSet rs2 = null;
			try {
				
				while (rs.next()){
	
					MVMRVendorProdRef referencia = new MVMRVendorProdRef(getCtx(),rs.getInt("XX_VMR_VENDORPRODREF_ID"), null);
					MVMRPOLineRefProv detalle = new MVMRPOLineRefProv(getCtx(),0, get_Trx());
					
					if (referencia.isActive())
					{
						// setea cada uno de los detalles de la orden de compra
						detalle.setXX_IsDefinitive(rs.getString("XX_ISDEFINITIVE").equals("Y")); 
						detalle.setXX_WithCharacteristic(rs.getString("XX_WITHCHARACTERISTIC").equals("Y")); 
						detalle.setXX_SaleUnit_ID(rs.getInt("XX_SaleUnit_ID")); 
						detalle.setXX_PiecesBySale_ID(rs.getInt("XX_PiecesBySale_ID")); 
						detalle.setXX_VME_ConceptValue_ID(rs.getInt("XX_VME_ConceptValue_ID"));
						detalle.setIsActive(rs.getString("ISACTIVE").equals("Y")); 
						detalle.setDescription(rs.getString("DESCRIPTION")); 
						detalle.setXX_SalePricePlusTax(rs.getBigDecimal("XX_SALEPRICEPLUSTAX")); 
						detalle.setXX_VMR_UnitPurchase_ID(rs.getInt("XX_VMR_UnitPurchase_ID")); 
						detalle.setXX_VMR_UnitConversion_ID(rs.getInt("XX_VMR_UnitConversion_ID")); 
						detalle.setXX_Characteristic1_ID(rs.getInt("XX_CHARACTERISTIC1_ID")); 
						detalle.setXX_Characteristic2_ID(rs.getInt("XX_CHARACTERISTIC2_ID"));
						detalle.setXX_LinePlusTaxAmount(rs.getBigDecimal("XX_LINEPLUSTAXAMOUNT")); 
						detalle.setXX_LinePVPAmount(rs.getBigDecimal("XX_LINEPVPAMOUNT")); 
						detalle.setXX_Margin(rs.getBigDecimal("XX_MARGIN")); 
						detalle.setXX_SalePrice(rs.getBigDecimal("XX_SALEPRICE")); 
						detalle.setXX_CostPerOrder(rs.getBigDecimal("XX_COSTPERORDER")); 
						detalle.setQty(rs.getInt("QTY")); 
						detalle.setPriceActual(rs.getBigDecimal("PRICEACTUAL")); 
						detalle.setXX_VMR_VendorProdRef_ID(rs.getInt("XX_VMR_VENDORPRODREF_ID")); 
						detalle.setXX_PackageMultiple(rs.getInt("XX_PACKAGEMULTIPLE")); 
						detalle.setXX_LastSalePrice(rs.getBigDecimal("XX_LASTSALEPRICE"));
						detalle.setLineNetAmt(rs.getBigDecimal("LINENETAMT")); 
						detalle.setXX_VMR_Line_ID(rs.getInt("XX_VMR_Line_ID")); 
						detalle.setXX_VMR_Section_ID(rs.getInt("XX_VMR_SECTION_ID")); 
						detalle.setC_Order_ID(newaux.getC_Order_ID()); 
						detalle.setLine(rs.getBigDecimal("LINE"));
						detalle.setXX_Characteristic1Value1_ID(rs.getInt("XX_CHARACTERISTIC1VALUE1_ID")); 
						detalle.setXX_Characteristic1Value2_ID(rs.getInt("XX_CHARACTERISTIC1VALUE2_ID")); 
						detalle.setC_TaxCategory_ID(rs.getInt("C_TAXCATEGORY_ID")); 
						detalle.setXX_Characteristic1Value3_ID(rs.getInt("XX_CHARACTERISTIC1VALUE3_ID")); 
						detalle.setXX_Characteristic1Value4_ID(rs.getInt("XX_CHARACTERISTIC1VALUE4_ID")); 
						detalle.setXX_Characteristic1Value5_ID(rs.getInt("XX_CHARACTERISTIC1VALUE5_ID")); 
						detalle.setXX_Characteristic1Value6_ID(rs.getInt("XX_CHARACTERISTIC1VALUE6_ID"));
						detalle.setXX_Characteristic1Value7_ID(rs.getInt("XX_CHARACTERISTIC1VALUE7_ID")); 
						detalle.setXX_Characteristic1Value8_ID(rs.getInt("XX_CHARACTERISTIC1VALUE8_ID")); 
						detalle.setXX_Characteristic1Value9_ID(rs.getInt("XX_CHARACTERISTIC1VALUE9_ID")); 
						detalle.setXX_Characteristic1Value10_ID(rs.getInt("XX_CHARACTERISTIC1VALUE10_ID")); 
						detalle.setXX_Characteristic2Value1_ID(rs.getInt("XX_CHARACTERISTIC2VALUE1_ID")); 
						detalle.setXX_Characteristic2Value2_ID(rs.getInt("XX_CHARACTERISTIC2VALUE2_ID"));
						detalle.setXX_Characteristic2Value3_ID(rs.getInt("XX_CHARACTERISTIC2VALUE3_ID")); 
						detalle.setXX_Characteristic2Value4_ID(rs.getInt("XX_CHARACTERISTIC2VALUE4_ID")); 
						detalle.setXX_Characteristic2Value5_ID(rs.getInt("XX_CHARACTERISTIC2VALUE5_ID")); 
						detalle.setXX_Characteristic2Value6_ID(rs.getInt("XX_CHARACTERISTIC2VALUE6_ID")); 
						detalle.setXX_Characteristic2Value7_ID(rs.getInt("XX_CHARACTERISTIC2VALUE7_ID")); 
						detalle.setXX_Characteristic2Value8_ID(rs.getInt("XX_CHARACTERISTIC2VALUE8_ID"));
						detalle.setXX_Characteristic2Value9_ID(rs.getInt("XX_CHARACTERISTIC2VALUE9_ID")); 
						detalle.setXX_Characteristic2Value10_ID(rs.getInt("XX_CHARACTERISTIC2VALUE10_ID")); 
						detalle.setXX_UnitPurchasePrice(rs.getBigDecimal("XX_UNITPURCHASEPRICE")); 
						detalle.setXX_LineQty(rs.getInt("XX_LINEQTY")); 
						detalle.setXX_GiftsQty(rs.getInt("XX_GIFTSQTY"));
						detalle.setXX_Rebate4(rs.getBigDecimal("XX_REBATE4")); 
						detalle.setXX_Rebate3(rs.getBigDecimal("XX_REBATE3")); 
						detalle.setXX_Rebate2(rs.getBigDecimal("XX_REBATE2")); 
						detalle.setXX_Rebate1(rs.getBigDecimal("XX_REBATE1")); 
						detalle.setXX_TaxAmount(rs.getBigDecimal("XX_TaxAmount")); 
						detalle.setSaleQty(rs.getInt("SaleQty")); 
						detalle.setXX_AssociateReference(rs.getString("XX_AssociateReference"));
						detalle.setXX_VMR_LongCharacteristic_ID(rs.getInt("XX_VMR_LongCharacteristic_ID")); 
						detalle.setXX_VMR_Brand_ID(rs.getInt("XX_VMR_Brand_ID"));
						detalle.setXX_ReferenceIsAssociated(rs.getString("XX_ReferenceIsAssociated").equals("Y"));
						detalle.setXX_GenerateMatrix(rs.getString("XX_GenerateMatrix"));
						detalle.setXX_DeleteMatrix(rs.getString("XX_DeleteMatrix"));
						detalle.setXX_ShowMatrix(rs.getString("XX_ShowMatrix"));
						detalle.setXX_CostWithDiscounts(rs.getBigDecimal("XX_CostWithDiscounts"));
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
						commit();
						
						sql1 = "SELECT *"
							+ " FROM XX_VMR_REFERENCEMATRIX"
							+ " WHERE XX_VMR_PO_LINEREFPROV_ID=" + rs.getInt("XX_VMR_PO_LineRefProv_ID");	
						
						prst2 = DB.prepareStatement(sql1,null);
						rs2 = prst2.executeQuery();
						
						while (rs2.next()){
							
							// Esta parte setea la matriz de referencias
							
							X_XX_VMR_ReferenceMatrix matriz = new X_XX_VMR_ReferenceMatrix(getCtx(),0,get_Trx());
									
							matriz.setXX_VALUE1(rs2.getInt("XX_VALUE1"));
							matriz.setXX_VALUE2(rs2.getInt("XX_VALUE2"));
							matriz.setXX_COLUMN(rs2.getInt("XX_COLUMN"));
							matriz.setXX_ROW(rs2.getInt("XX_ROW"));
							matriz.setXX_QUANTITYC(rs2.getInt("XX_QUANTITYC"));
							matriz.setXX_QUANTITYV(rs2.getInt("XX_QUANTITYV"));
							matriz.setXX_QUANTITYO(rs2.getInt("XX_QUANTITYO"));
							matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
							if (rs2.getInt("M_PRODUCT")!=0)
							{	
								matriz.setM_Product(rs2.getInt("M_PRODUCT"));
							}
							matriz.save();
							commit();
				
						}
						rs2.close();
						prst2.close();
					}
				}
	
			} catch (SQLException e){
				System.out.println("Error al copiar los detalles o la matriz de referencias " + e);			
			}	finally 
			{
				DB.closeStatement(prst);
				DB.closeResultSet(rs);
				DB.closeStatement(prst2);
				DB.closeResultSet(rs2);
			}
		}
		ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_OrderCopied",new String[] {newaux.getDocumentNo()}));
	
		return "";
	
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

}
