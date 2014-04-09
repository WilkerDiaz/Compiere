package compiere.model.cds.processes;
 
import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.MPayment;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_C_Payment;

import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.cds.X_Ref_XX_Ref_TypeDelivery;
import compiere.model.cds.X_XX_VMR_CancellationMotive;
import compiere.model.cds.X_XX_VMR_Category;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;
import compiere.model.suppliesservices.X_XX_ProductPercentDistrib;

public class XX_VoidPurchaseOrderProcess extends SvrProcess {
	
	Integer p_CancellationMotive_ID = 0;

	@Override
	protected String doIt() throws Exception {
		
		MOrder aux = new MOrder(getCtx(),getRecord_ID(),get_TrxName());

		String Status = (String)aux.get_Value("XX_OrderStatus");
		
//		String anticipoOld = "";
//		String anticipoNew = "";
		if(aux.getXX_ComesFromSITME()== true){
			aux.set_Value("XX_Void", "N");
			ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_SitmeVoidNotAllowed"));
			return Msg.getMsg(Env.getCtx(), "XX_SitmeVoidNotAllowed");
		}
			
		//Si la ordes esta recibida o chequeada no puedo anular
		if (Status.equals("RE") || (Status.equals("CH") && !aux.getXX_PurchaseType().equals("SE")))
		{
			aux.set_Value("XX_Void", "N");
			ADialog.info(1, new Container(), "No se pueden anular ordenes de compra con status Recibida o Chequeada");
			return "No se pueden anular ordenes de compra con status Recibida o Chequeada";
		}
	
		//Si la ordes esta aprobada y es importada no puedo anular
		if(Status.equalsIgnoreCase("AP") && aux.getXX_OrderType().equalsIgnoreCase("Importada")){
			ADialog.info(1, new Container(), "No se pueden anular ordenes de compra importadas y aprobadas");
			return "No se pueden anular ordenes de compra importadas y aprobadas";
		}
		
		
		if(orderIsInBoardingGuide()){
			
			ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_CantAnullOrderBG"));
			return  Msg.getMsg(Env.getCtx(), "XX_CantAnullOrderBG");
		}
		
		if(!aux.getXX_OrderStatus().equalsIgnoreCase("PRO") && !aux.getXX_OrderStatus().equalsIgnoreCase("EP")){
			
			if(aux.getXX_POType().equals("POM") &&  
					Env.getCtx().getContextAsInt("#AD_Role_ID") != Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID") &&
						Env.getCtx().getContextAsInt("#AD_Role_ID") != Env.getCtx().getContextAsInt("#XX_L_ROLECATEGORYMANAGER_ID")){
				ADialog.info(1, new Container(), "Solo el Jefe de Planificación puede anular una O/C de Mercancia en este estado");
				return "Solo el Jefe de Planificación puede anular una O/C de Mercancia en este estado";
			}
			else if(aux.getXX_POType().equals("POA") || aux.getXX_POType().equals("POA")){
				
				if(!Env.getCtx().getContext("#AD_Role_Name").toUpperCase().contains("GERENTE")){
					ADialog.info(1, new Container(), "Solo un Gerente puede anular una O/C de Bienes y Servicios en este estado");
					return "Solo un Gerente puede anular una O/C de Bienes y Servicios en este estado";
				}
			}
		}
			
		
		//Agregado por Javier Pino - Si ésta es Pre-distribuida  
		if (aux.getXX_VLO_TypeDelivery().equals(X_Ref_XX_Ref_TypeDelivery.PRE__DISTRIBUIDA.getValue())){			
			//Si tiene una distribución asociada, entonces anularla también
			int header = Utilities.getDistributionHeader(getRecord_ID(), null);
			if (header > 0) { 
				X_XX_VMR_DistributionHeader headerPO = new X_XX_VMR_DistributionHeader(getCtx(), header, get_TrxName());
				headerPO.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.ANULADA.getValue());		
				headerPO.save();
			}
		} //Fin Javier Pino
		
		// JTrias Obtengo los anticipos
		Vector<Integer> advances = getAdvances();
		
		Boolean opcion;
		if(advances.size()>0){
			opcion = true;
		}else{
			opcion = ADialog.ask(1, new Container(), "XX_OrderReplaceAsk");
			deleteEstimatedAPayable();
		}
		//Fin Jtrias
		
		int C_OrderLine_ID = 0;
		// Purchase of Supplies and Services 
		// Maria Vintimilla Funcion 012
		String docType = aux.getXX_POType();
		if (opcion)
		{			
			MOrder newaux = new MOrder(Env.getCtx(), 0, get_TrxName());
			newaux.setAD_Org_ID(aux.getAD_Org_ID());
			
			newaux.setDocStatus("DR");
			newaux.setDocAction("CO");
			newaux.setC_Activity_ID(aux.getC_Activity_ID()); newaux.setC_BPartner_ID(aux.getC_BPartner_ID()); 
			newaux.setC_Campaign_ID(aux.getC_Campaign_ID()); newaux.setC_CashLine_ID(aux.getC_CashLine_ID()); newaux.setC_Charge_ID(aux.getC_Charge_ID()); newaux.setC_ConversionType_ID(aux.getC_ConversionType_ID()); newaux.setC_Currency_ID(aux.getC_Currency_ID());			
			newaux.setC_DocTypeTarget_ID(aux.getC_DocTypeTarget_ID()); newaux.setC_DocType_ID(aux.getC_DocType_ID()); newaux.setC_PaymentTerm_ID(aux.getC_PaymentTerm_ID()); /**newaux.setC_Payment_ID(aux.getC_Payment_ID());*/
			newaux.setC_Project_ID(aux.getC_Project_ID()); newaux.setChargeAmt(aux.getChargeAmt()); newaux.setCopyFrom(aux.getCopyFrom()); 
			newaux.setDateAcct(aux.getDateAcct()); newaux.setDateOrdered(aux.getDateOrdered()); newaux.setDatePrinted(aux.getDatePrinted()); newaux.setDatePromised(aux.getDatePromised()); 
			newaux.setDeliveryViaRule(aux.getDeliveryViaRule()); newaux.setDescription(aux.getDescription());
			newaux.setFreightAmt(aux.getFreightAmt()); newaux.setFreightCostRule(aux.getFreightCostRule()); newaux.setGrandTotal(aux.getGrandTotal()); newaux.setInvoiceRule(aux.getInvoiceRule()); newaux.setIsActive(aux.isActive());
			newaux.setIsApproved(aux.isApproved()); newaux.setIsCreditApproved(aux.isCreditApproved()); newaux.setIsDelivered(aux.isDelivered()); newaux.setIsDiscountPrinted(aux.isDiscountPrinted()); newaux.setIsDropShip(aux.isDropShip());
			newaux.setIsInvoiced(aux.isInvoiced()); newaux.setIsPrinted(aux.isPrinted()); newaux.setIsReturnTrx(aux.isReturnTrx()); newaux.setIsSOTrx(aux.isSOTrx()); newaux.setIsSelected(aux.isSelected());
			newaux.setIsSelfService(aux.isSelfService()); newaux.setIsTaxIncluded(aux.isTaxIncluded()); newaux.setIsTransferred(aux.isTransferred()); newaux.setM_PriceList_ID(aux.getM_PriceList_ID()); newaux.setM_RMACategory_ID(aux.getM_RMACategory_ID());
			newaux.setM_ReturnPolicy_ID(aux.getM_ReturnPolicy_ID()); newaux.setM_Shipper_ID(aux.getM_Shipper_ID()); newaux.setOrig_InOut_ID(aux.getOrig_InOut_ID()); newaux.setOrig_Order_ID(aux.getOrig_Order_ID());
			newaux.setPOReference(aux.getPOReference()); newaux.setPay_BPartner_ID(aux.getPay_BPartner_ID()); newaux.setPay_Location_ID(aux.getPay_Location_ID()); newaux.setPosted(aux.isPosted());
			newaux.setPriorityRule(aux.getPriorityRule()); newaux.setProcessed(false); newaux.setProcessing(false); newaux.setRef_Order_ID(aux.getRef_Order_ID()); 
			newaux.setSendEMail(aux.isSendEMail()); newaux.setTotalLines(aux.getTotalLines());  newaux.setUser1_ID(aux.getUser1_ID());
			newaux.setUser2_ID(aux.getUser2_ID()); newaux.setVolume(aux.getVolume()); newaux.setWeight(aux.getWeight()); newaux.setC_BPartnerSR_ID(aux.getC_BPartnerSR_ID()); newaux.setC_BP_BankAccount_ID(aux.getC_BP_BankAccount_ID());
			newaux.setXX_Department(aux.getXX_Department());  newaux.setXX_ConsigneeName(aux.getXX_ConsigneeName()); newaux.setC_Country_ID(aux.getC_Country_ID()); newaux.setXX_OrderType(aux.getXX_OrderType()); newaux.setXX_Discount1(aux.getXX_Discount1());
			newaux.setXX_Discount2(aux.getXX_Discount2()); newaux.setXX_Discount3(aux.getXX_Discount3()); newaux.setXX_Discount4(aux.getXX_Discount4()); newaux.setXX_StoreDistribution(aux.getXX_StoreDistribution()); newaux.setXX_BuyersComments(aux.getXX_BuyersComments());
			newaux.setXX_PurchaseOrderComments(aux.getXX_PurchaseOrderComments()); newaux.setXX_VLO_TypeDelivery(aux.getXX_VLO_TypeDelivery()); newaux.setXX_VMR_Subject_ID(aux.getXX_VMR_Subject_ID()); newaux.setXX_Season_ID(aux.getXX_Season_ID());
			newaux.setXX_Brochure_ID(aux.getXX_Brochure_ID()); newaux.setXX_Category_ID(aux.getXX_Category_ID()); newaux.setXX_ConversionRate_ID(aux.getXX_ConversionRate_ID()); newaux.setXX_VLO_ShippingCondition_ID(aux.getXX_VLO_ShippingCondition_ID()); 
			newaux.setXX_VLO_ArrivalPort_ID(aux.getXX_VLO_ArrivalPort_ID()); newaux.setXX_VLO_DeliveryLocation_ID(aux.getXX_VLO_DeliveryLocation_ID()); 
			newaux.setXX_EstimatedMargin(aux.getXX_EstimatedMargin()); newaux.setXX_EstimatedMargin(aux.getXX_EstimatedMargin()); newaux.setXX_MontLimit(aux.getXX_MontLimit()); newaux.setXX_ProductQuantity(aux.getXX_ProductQuantity());
			newaux.setTotalPVP(aux.getTotalPVP()); newaux.setXX_TotalPVPPlusTax(aux.getXX_TotalPVPPlusTax()); newaux.setXX_EstimatedFactor(aux.getXX_EstimatedFactor());
			newaux.setXX_DefinitiveFactor(aux.getXX_DefinitiveFactor()); newaux.setXX_ReplacementFactor(aux.getXX_ReplacementFactor());
			newaux.setXX_UserBuyer_ID(aux.getXX_UserBuyer_ID()); newaux.setAD_User_ID(aux.getAD_User_ID()); newaux.setC_Currency_ID(aux.getC_Currency_ID()); newaux.setC_ConversionType_ID(aux.getC_ConversionType_ID());
			if (aux.getDeliveryRule()!=null) {newaux.setDeliveryRule(aux.getDeliveryRule());}
			if (aux.getM_Warehouse_ID()!=0) {newaux.setM_Warehouse_ID(aux.getM_Warehouse_ID());}
			if (aux.getXX_OrderType().equals("Importada")) {newaux.setXX_VLO_DispatchRoute_ID(aux.getXX_VLO_DispatchRoute_ID());}
			if (aux.getXX_VMR_CancellationMotive_ID()!=0) {newaux.setXX_VMR_CancellationMotive_ID(aux.getXX_VMR_CancellationMotive_ID());}
			newaux.setXX_VMR_Package_ID(aux.getXX_VMR_Package_ID());
			newaux.setXX_ArrivalDate(null); 
			newaux.setXX_EstimatedDate(null);
			newaux.setSalesRep_ID(aux.getSalesRep_ID());
			newaux.setXX_OrderStatus("PRO"); 
			newaux.setXX_Void(false);  
			newaux.setPaymentRule(aux.getPaymentRule());
			newaux.setXX_POType(aux.getXX_POType());
			// Purchase of Supplies and Services 
			// Maria Vintimilla Funcion 012
	    	if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
	     		newaux.setXX_PurchaseType(aux.getXX_PurchaseType());
	    		newaux.setXX_IVA(aux.getXX_IVA());
	    		newaux.setXX_Department(0);
	    		newaux.setXX_VMR_Package_ID(0); 
	    		newaux.setXX_UserBuyer_ID(0); 
	    		newaux.setXX_Collection_ID(0); 
	    		newaux.setXX_VMR_Subject_ID(0); 
	    		newaux.setXX_Season_ID(0);
	    		newaux.setXX_Brochure_ID(0); 
	    		newaux.setXX_Category_ID(0);
	    		newaux.setXX_ConsigneeName(null);
	    		newaux.setXX_ReplacementFactor(new BigDecimal(0));
	    		newaux.setTotalPVP(new BigDecimal(0)); 
	    		newaux.setXX_TotalPVPPlusTax(new BigDecimal(0));
	    	}// Fin If DocType
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
	    	}//Else
			newaux.setXX_Alert("ESTA ORDEN REEMPLAZA A LA ORDEN DE COMPRA NUMERO " + aux.getDocumentNo());
			newaux.save();
			
			//Se copia el(los) anticipo(s) correspondientes (en caso de tener)
			for(int i=0; i<advances.size(); i++){
				
				MPayment newAdv = new MPayment( Env.getCtx(), 0, get_TrxName());
				MPayment oldAdv = new MPayment( Env.getCtx(), advances.get(i), get_TrxName());
				MPayment.copyValues(oldAdv, newAdv);
				
				newAdv.setAD_Org_ID(oldAdv.getAD_Org_ID());
				newAdv.setDescription("ESTE ANTICIPO REEMPLAZA AL NUMERO " + oldAdv.getDocumentNo());
				newAdv.setC_Order_ID(newaux.get_ID());
				newAdv.save();
			
				// Notificación por correo sobre anulación de oc con anticipo a finanzas
				//Maria Vintimilla
//				anticipoOld += oldAdv.getDocumentNo()+ " ";
//				anticipoNew += newAdv.getDocumentNo()+ " ";

				oldAdv.setDocAction(X_C_Payment.DOCACTION_Void);
				DocumentEngine.processIt( oldAdv, X_C_Payment.DOCACTION_Void);
				oldAdv.setDescription("ANTICIPO DE PAGO SUSTITUIDO POR " + newAdv.getDocumentNo() + ", DEBIDO A ANULACIÓN DE ORDEN DE COMPRA NUMERO " + aux.getDocumentNo());
				oldAdv.save();
				
			}
		
			commit();
			newaux.load(get_TrxName());
			
			ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_OrderReplaced",new String[] {newaux.getDocumentNo()}));

			aux.setXX_Alert("ORDEN SUSTITUIDA POR LA ORDEN DE COMPRA NUMERO " + newaux.getDocumentNo());
			
			// Notificación por correo sobre anulación de oc con anticipo a finanzas
			// Maria Vintimilla
//			sendMailFinance(Env.getCtx().getAD_User_ID(), aux.getDocumentNo(), 
//					newaux.getDocumentNo(), anticipoOld, anticipoNew, p_CancellationMotive_ID);
			
			// Purchase of Supplies and Services 
			// Maria Vintimilla Funcion 012
		    if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
			   	// Copy order lines from the last order
			   	String sql1 = "SELECT * " +
			   			"FROM C_OrderLine " +
			   			"WHERE C_Order_ID=" + aux.getC_Order_ID();
			   	//System.out.println(sql1);
			   	PreparedStatement prst1 = DB.prepareStatement(sql1,null);
			   	ResultSet rs1 = null;
			   	try {
			   		rs1 = prst1.executeQuery();
			   		while (rs1.next()){
			   			C_OrderLine_ID = rs1.getInt("C_OrderLine_ID");
			   			//System.out.println("C_OrderLine_Id del detalle viejo: "+C_OrderLine_ID);
			   			MOrderLine detail = new MOrderLine(getCtx(),0, get_TrxName());
						// Set order lines for the PO newaux
			   			detail.setC_Order_ID(newaux.getC_Order_ID());
						detail.setC_BPartner_ID(rs1.getInt("C_BPartner_ID"));
						detail.setM_Warehouse_ID(rs1.getInt("M_Warehouse_ID"));
						detail.setM_Product_ID(rs1.getInt("M_Product_ID"));
						detail.setC_UOM_ID(rs1.getInt("C_UOM_ID"));
						detail.setC_Tax_ID(rs1.getInt("C_Tax_ID"));
						detail.setDateOrdered(rs1.getTimestamp("DateOrdered"));
						detail.setDatePromised(rs1.getTimestamp("DatePromised"));
						detail.setLine(rs1.getInt("Line"));
						detail.setQtyOrdered(rs1.getBigDecimal("QtyOrdered"));
						detail.setQtyEntered(rs1.getBigDecimal("QtyEntered"));
						detail.setQtyReserved(rs1.getBigDecimal("QtyReserved"));
						detail.setQtyInvoiced(rs1.getBigDecimal("QtyInvoiced"));
						detail.setQtyReturned(rs1.getBigDecimal("QtyReturned"));
						detail.setPriceEntered(rs1.getBigDecimal("PriceEntered"));
						detail.setPriceActual(rs1.getBigDecimal("PriceActual"));
						detail.setTaxAmt(rs1.getBigDecimal("TaxAmt"));
						detail.setLine(rs1.getInt("LineNetAmt"));
						detail.setXX_DistributionType(rs1.getString("XX_DistributionType"));
						if(rs1.getString("XX_IsPiecesPercentage").equals("Y")){
							detail.setXX_IsPiecesPercentage(true);
						}
						else{
							detail.setXX_IsPiecesPercentage(false);
						}	
						if(rs1.getString("XX_IsDistribApplied").equals("Y")){
							detail.setXX_IsDistribApplied(true);
						}
						else{
							detail.setXX_IsDistribApplied(false);
						}						
						if(rs1.getString("XX_ClearedDistrib").equals("Y")){
							detail.setXX_ClearedDistrib(true);
						}
						else{
							detail.setXX_ClearedDistrib(false);
						}
						//detail.setDescription(rs1.getString("Description"));
						detail.save();
						detail.load(get_TrxName());
						
						// Copy Line's Distribution if any 
					   	String sql2 = "SELECT *"
					   		+ " FROM XX_ProductPercentDistrib"
					   		+ " WHERE C_OrderLine_ID = " + C_OrderLine_ID;
					   	System.out.println(sql2);
					   	PreparedStatement prst2 = DB.prepareStatement(sql2,null);
					   	ResultSet rs2 = null;
					   	try {
					   		rs2 = prst2.executeQuery();
					   		while (rs2.next()){
					   			X_XX_ProductPercentDistrib dist = 
					   				new X_XX_ProductPercentDistrib(Env.getCtx(),0, get_TrxName());
								// Set line's Distributions
					   			dist.setC_OrderLine_ID(detail.getC_OrderLine_ID());
					   			dist.setC_Order_ID(detail.getC_Order_ID());
					   			dist.setXX_Org_ID(rs2.getInt("XX_Org_ID"));
					   			dist.setM_Warehouse_ID(rs2.getInt("M_Warehouse_ID"));
					   			dist.setXX_QuantityPerCC(rs2.getBigDecimal("XX_QuantityPerCC"));
					   			dist.setXX_PercentagePerCC(rs2.getBigDecimal("XX_PercentagePerCC"));
					   			dist.setXX_AmountPerCC(rs2.getBigDecimal("XX_AmountPerCC"));
					   			dist.save();
					   		}// Fin While
					   	} 
					   	catch (Exception e){
							log.saveError("ErrorSql distribucion: ", Msg.getMsg(getCtx(), e.getMessage()));
						}
					   	finally {
					   		rs2.close();
							prst2.close();
					   	}
			   		}// Fin While
			   	}//try 
			   	catch (Exception e){
					log.saveError("ErrorSql detalle: ", Msg.getMsg(Env.getCtx(), e.getMessage()));
				}
			   	finally {
			   		rs1.close();
					prst1.close();
			   	}
			}// If docType
		    else {	
		    	// Esta parte copia todos los detalles	
		    	String sql3 = "SELECT *"
					+ " FROM XX_VMR_PO_LINEREFPROV"
					+ " WHERE C_ORDER_ID=" + aux.getC_Order_ID();	
				PreparedStatement prst3 = DB.prepareStatement(sql3,null);
				try {
					ResultSet rs3 = prst3.executeQuery();
					while (rs3.next()){
						MVMRPOLineRefProv detalle = new MVMRPOLineRefProv(getCtx(),0,get_TrxName());
						MVMRVendorProdRef referencia = new MVMRVendorProdRef(getCtx(),rs3.getInt("XX_VMR_VENDORPRODREF_ID"), null);
						if (referencia.isActive()){
							// Setea cada uno de los detalles de la orden de compra
							detalle.setXX_IsDefinitive(rs3.getString("XX_ISDEFINITIVE").equals("Y")); 
							detalle.setXX_WithCharacteristic(rs3.getString("XX_WITHCHARACTERISTIC").equals("Y")); 
							detalle.setXX_SaleUnit_ID(rs3.getInt("XX_SaleUnit_ID")); 
							detalle.setXX_PiecesBySale_ID(rs3.getInt("XX_PiecesBySale_ID"));  
							detalle.setXX_VME_ConceptValue_ID(rs3.getInt("XX_VME_ConceptValue_ID"));
							detalle.setIsActive(rs3.getString("ISACTIVE").equals("Y")); 
							detalle.setDescription(rs3.getString("DESCRIPTION")); 
							detalle.setXX_SalePricePlusTax(rs3.getBigDecimal("XX_SALEPRICEPLUSTAX")); 
							detalle.setXX_VMR_UnitPurchase_ID(rs3.getInt("XX_VMR_UnitPurchase_ID")); 
							detalle.setXX_VMR_UnitConversion_ID(rs3.getInt("XX_VMR_UnitConversion_ID")); 
							detalle.setXX_Characteristic1_ID(rs3.getInt("XX_CHARACTERISTIC1_ID")); 
							detalle.setXX_Characteristic2_ID(rs3.getInt("XX_CHARACTERISTIC2_ID"));
							detalle.setXX_LinePlusTaxAmount(rs3.getBigDecimal("XX_LINEPLUSTAXAMOUNT")); 
							detalle.setXX_LinePVPAmount(rs3.getBigDecimal("XX_LINEPVPAMOUNT")); 
							detalle.setXX_Margin(rs3.getBigDecimal("XX_MARGIN")); 
							detalle.setXX_SalePrice(rs3.getBigDecimal("XX_SALEPRICE")); 
							detalle.setXX_CostPerOrder(rs3.getBigDecimal("XX_COSTPERORDER")); 
							detalle.setQty(rs3.getInt("QTY")); 
							detalle.setPriceActual(rs3.getBigDecimal("PRICEACTUAL")); 
							detalle.setXX_VMR_VendorProdRef_ID(rs3.getInt("XX_VMR_VENDORPRODREF_ID"));
							detalle.setXX_PackageMultiple(rs3.getInt("XX_PACKAGEMULTIPLE")); 
							detalle.setXX_LastSalePrice(rs3.getBigDecimal("XX_LASTSALEPRICE"));
							detalle.setLineNetAmt(rs3.getBigDecimal("LINENETAMT")); 
							detalle.setXX_VMR_Line_ID(rs3.getInt("XX_VMR_Line_ID")); 
							detalle.setXX_VMR_Section_ID(rs3.getInt("XX_VMR_Section_ID")); 
							detalle.setC_Order_ID(newaux.getC_Order_ID()); 
							detalle.setLine(rs3.getBigDecimal("LINE")); 
							detalle.setXX_Characteristic1Value1_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE1_ID")); 
							detalle.setXX_Characteristic1Value2_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE2_ID")); 
							detalle.setC_TaxCategory_ID(rs3.getInt("C_TAXCATEGORY_ID")); 
							detalle.setXX_Characteristic1Value3_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE3_ID")); 
							detalle.setXX_Characteristic1Value4_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE4_ID")); 
							detalle.setXX_Characteristic1Value5_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE5_ID"));
							detalle.setXX_Characteristic1Value6_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE6_ID"));
							detalle.setXX_Characteristic1Value7_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE7_ID")); 
							detalle.setXX_Characteristic1Value8_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE8_ID")); 
							detalle.setXX_Characteristic1Value9_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE9_ID")); 
							detalle.setXX_Characteristic1Value10_ID(rs3.getInt("XX_CHARACTERISTIC1VALUE10_ID")); 
							detalle.setXX_Characteristic2Value1_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE1_ID")); 
							detalle.setXX_Characteristic2Value2_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE2_ID"));
							detalle.setXX_Characteristic2Value3_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE3_ID")); 
							detalle.setXX_Characteristic2Value4_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE4_ID")); 
							detalle.setXX_Characteristic2Value5_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE5_ID")); 
							detalle.setXX_Characteristic2Value6_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE6_ID")); 
							detalle.setXX_Characteristic2Value7_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE7_ID")); 
							detalle.setXX_Characteristic2Value8_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE8_ID"));
							detalle.setXX_Characteristic2Value9_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE9_ID")); 
							detalle.setXX_Characteristic2Value10_ID(rs3.getInt("XX_CHARACTERISTIC2VALUE10_ID")); 
							detalle.setXX_UnitPurchasePrice(rs3.getBigDecimal("XX_UNITPURCHASEPRICE")); 
							detalle.setXX_LineQty(rs3.getInt("XX_LINEQTY")); 
							detalle.setXX_GiftsQty(rs3.getInt("XX_GIFTSQTY"));	
							detalle.setXX_Rebate4(rs3.getBigDecimal("XX_REBATE4")); 
							detalle.setXX_Rebate3(rs3.getBigDecimal("XX_REBATE3")); 
							detalle.setXX_Rebate2(rs3.getBigDecimal("XX_REBATE2")); 
							detalle.setXX_Rebate1(rs3.getBigDecimal("XX_REBATE1")); 
							detalle.setXX_TaxAmount(rs3.getBigDecimal("XX_TaxAmount")); 
							detalle.setSaleQty(rs3.getInt("SaleQty")); 
							detalle.setXX_AssociateReference(rs3.getString("XX_AssociateReference"));	
							detalle.setXX_VMR_LongCharacteristic_ID(rs3.getInt("XX_VMR_LongCharacteristic_ID")); 
							detalle.setXX_VMR_Brand_ID(rs3.getInt("XX_VMR_Brand_ID")); 
							detalle.setXX_ReferenceIsAssociated(rs3.getString("XX_ReferenceIsAssociated").equals("Y"));
							detalle.setXX_CostWithDiscounts(rs3.getBigDecimal("XX_CostWithDiscounts"));
							detalle.setXX_GenerateMatrix(rs3.getString("XX_GenerateMatrix"));
							detalle.setXX_DeleteMatrix(rs3.getString("XX_DeleteMatrix"));
							detalle.setXX_ShowMatrix(rs3.getString("XX_ShowMatrix"));
							detalle.setXX_IsGeneratedCharac1Value1(rs3.getString("XX_IsGeneratedCharac1Value1").equals("Y"));
							detalle.setXX_IsGeneratedCharac1Value2(rs3.getString("XX_IsGeneratedCharac1Value2").equals("Y"));
							detalle.setXX_IsGeneratedCharac1Value3(rs3.getString("XX_IsGeneratedCharac1Value3").equals("Y"));
							detalle.setXX_IsGeneratedCharac1Value4(rs3.getString("XX_IsGeneratedCharac1Value4").equals("Y"));
							detalle.setXX_IsGeneratedCharac1Value5(rs3.getString("XX_IsGeneratedCharac1Value5").equals("Y"));
							detalle.setXX_IsGeneratedCharac1Value6(rs3.getString("XX_IsGeneratedCharac1Value6").equals("Y"));
							detalle.setXX_IsGeneratedCharac1Value7(rs3.getString("XX_IsGeneratedCharac1Value7").equals("Y"));
							detalle.setXX_IsGeneratedCharac1Value8(rs3.getString("XX_IsGeneratedCharac1Value8").equals("Y"));
							detalle.setXX_IsGeneratedCharac1Value9(rs3.getString("XX_IsGeneratedCharac1Value9").equals("Y"));
							detalle.setXX_IsGeneratedCharac1Value10(rs3.getString("XX_IsGeneratedCharac1Value10").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value1(rs3.getString("XX_IsGeneratedCharac2Value1").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value2(rs3.getString("XX_IsGeneratedCharac2Value2").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value3(rs3.getString("XX_IsGeneratedCharac2Value3").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value4(rs3.getString("XX_IsGeneratedCharac2Value4").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value5(rs3.getString("XX_IsGeneratedCharac2Value5").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value6(rs3.getString("XX_IsGeneratedCharac2Value6").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value7(rs3.getString("XX_IsGeneratedCharac2Value7").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value8(rs3.getString("XX_IsGeneratedCharac2Value8").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value9(rs3.getString("XX_IsGeneratedCharac2Value9").equals("Y"));
							detalle.setXX_IsGeneratedCharac2Value10(rs3.getString("XX_IsGeneratedCharac2Value10").equals("Y"));
							detalle.save();				
								
							sql3 = "SELECT *"
								+ " FROM XX_VMR_REFERENCEMATRIX"
								+ " WHERE XX_VMR_PO_LINEREFPROV_ID=" + rs3.getInt("XX_VMR_PO_LineRefProv_ID");	
							
							PreparedStatement prst4 = DB.prepareStatement(sql3,null);
							ResultSet rs4 = prst4.executeQuery();
							while (rs4.next()){
								// Esta parte setea la matriz de referencias
								X_XX_VMR_ReferenceMatrix matriz = new X_XX_VMR_ReferenceMatrix(getCtx(),0,get_TrxName());
								matriz.setXX_VALUE1(rs4.getInt("XX_VALUE1"));
								matriz.setXX_VALUE2(rs4.getInt("XX_VALUE2"));
								matriz.setXX_COLUMN(rs4.getInt("XX_COLUMN"));
								matriz.setXX_ROW(rs4.getInt("XX_ROW"));
								matriz.setXX_QUANTITYC(rs4.getInt("XX_QUANTITYC"));
								matriz.setXX_QUANTITYV(rs4.getInt("XX_QUANTITYV"));
								matriz.setXX_QUANTITYO(rs4.getInt("XX_QUANTITYO"));
								matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
								matriz.setM_Product(rs4.getInt("M_PRODUCT"));
								matriz.save();
							}// Fin while
							rs4.close();
							prst4.close();
						}// if referencia
					}//while
					rs3.close();
					prst3.close();
				}// try 
				catch (SQLException e){
					System.out.println("Error al copiar los detalles o la matriz de referencias " + e);			
				}	
		    }// Fin Else DocType
		}//Fin if opcion

		Calendar fecha = Calendar.getInstance();		
		Timestamp recienteSrv = new Timestamp(fecha.getTime().getTime());
		aux.setXX_InsertedStatusDate(recienteSrv);
		aux.setXX_VMR_CancellationMotive_ID(p_CancellationMotive_ID);

	
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		aux.set_Value("XX_Void", "Y");
		aux.set_Value("XX_OrderStatus", "AN");
		
		
		// Inicio Victor Lo Monaco
		// Debo colocar la orden y los detalles como no sincronizados para que se vuelvan a exportar
		aux.setXX_Status_Sinc(false);
		// Fin Victor Lo Monaco
		
		
		
		if(Status.equalsIgnoreCase("AP") && aux.getXX_OrderType().equalsIgnoreCase("Nacional") && getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID")==getCtx().getAD_Role_ID())
		{	
			aux.setDocAction(X_C_Order.DOCACTION_Void);
			DocumentEngine.processIt(aux, X_C_Order.DOCACTION_Void);

	    }
		
		//modificado por JPires
		//si salva envio los diferentes correos. 
		if(aux.save()){
			if(aux.getXX_OrderType().equals(X_Ref_XX_OrderType.NACIONAL.getValue())){
				String Attachment = null;
				// Purchase of Supplies and Services 
				// Maria Vintimilla Funcion 012
				if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
					X_XX_VMR_Category Categoria =  
						new X_XX_VMR_Category(Env.getCtx(), aux.getXX_Category_ID(), null);
					MBPartner JefeCategoria = 
						new MBPartner(Env.getCtx(), Categoria.getXX_CategoryManager_ID(), null) ;
					EnviarCorreosCambioStatusUser(JefeCategoria.get_ID(), aux.getDocumentNo(), p_CancellationMotive_ID, Attachment);
				}// Fin If DocType
				MBPartner Comprador = 
					new MBPartner(getCtx(), aux.getXX_UserBuyer_ID(), null) ;
				EnviarCorreosCambioStatusBPartner(aux.getC_BPartner_ID(), aux.getDocumentNo(), p_CancellationMotive_ID, Attachment);
				EnviarCorreosCambioStatusUser(Comprador.get_ID(), aux.getDocumentNo(), p_CancellationMotive_ID, Attachment);
				
			}
			else if(aux.getXX_OrderType().equals(X_Ref_XX_OrderType.IMPORTADA.getValue())){
				String Attachment = null;
				// Purchase of Supplies and Services 
				// Maria Vintimilla Funcion 012
				if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
					X_XX_VMR_Category Categoria = 
						new X_XX_VMR_Category(Env.getCtx(), aux.getXX_Category_ID(), null);
					MBPartner JefeCategoria = 
						new MBPartner(Env.getCtx(), Categoria.getXX_CategoryManager_ID(), null) ;
					EnviarCorreosCambioStatusUser(JefeCategoria.get_ID(), aux.getDocumentNo(), p_CancellationMotive_ID, Attachment);
				}// Fin If DocType
				MBPartner Comprador = new MBPartner(getCtx(), aux.getXX_UserBuyer_ID(), null) ;
				EnviarCorreosCambioStatusUser(Comprador.get_ID(), aux.getDocumentNo(), p_CancellationMotive_ID, Attachment);

				EnviarCorreosCambioStatusBPartner(aux.getC_BPartner_ID(), aux.getDocumentNo(), p_CancellationMotive_ID, Attachment);
				EnviarCorreosCambioStatusJimport(aux.getDocumentNo(), p_CancellationMotive_ID, Attachment);
				
				//Coordinador de Cuentas por Pagar
				sendMailStatusChangeByRole( getCtx().getContextAsInt("#XX_L_ROLE_ACCOUNTTOPAYCOORD_ID"),aux.getDocumentNo(), p_CancellationMotive_ID, Attachment); 
			}
		}
		
		return "";
	}

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null) ;
			else if (name.equals("XX_VMR_CancellationMotive_ID"))
				p_CancellationMotive_ID = element.getParameterAsInt();
			else
				log.log(null, "Unknown Parameter: " + name);
			}

	}
	
	/**
	 * Jorge E. Pires G. 
	 * 
	 **/	
	private boolean EnviarCorreosCambioStatusUser(Integer User, String NumeroOC, Integer motive ,String Attachment) {	
		String motiveAnull = null;		
		
		try{	
			String SQL = ("SELECT Name AS MOTIVE " +
					  	   "FROM XX_VMR_CancellationMotive WHERE XX_VMR_CancellationMotive_ID = '"+motive+"'");
	
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				motiveAnull = rs.getString("MOTIVE");
				//System.out.println(motiveAnull); 
			}
			rs.close();
			pstmt.close();
			
			String sql3 = "SELECT u.AD_User_ID "
					   	+ "FROM AD_User u "
					   	+ "WHERE IsActive = 'Y' " 
					   	+ "AND C_BPARTNER_ID = " + User;
			
			PreparedStatement pstmt3 = DB.prepareStatement(sql3, null);
			ResultSet rs3 = pstmt3.executeQuery();
		
			String Mensaje = Msg.getMsg( getCtx(), 
					 					"XX_CancellationMotive", 
					 					new String[]{NumeroOC, 
								 					"Anulada",
								 					motiveAnull});
			
			
			while(rs3.next()){
				Integer UserAuxID = rs3.getInt("AD_User_ID");
				Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
				f.ejecutarMail();   
				f = null;
				
			}
			rs3.close();
			pstmt3.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}
	/**
	 * Fin Jorge E. Pires G.
	 * */
	
	
	/**
	 * Daniel Pellegrino --> Funcion 164. 
	 * 
	 **/
	
	private boolean EnviarCorreosCambioStatusBPartner(Integer BPartner, String NumeroOC, Integer motive ,String Attachment) {	
		String SQL = ("SELECT Name AS MOTIVE " +
					  "FROM XX_VMR_CancellationMotive WHERE XX_VMR_CancellationMotive_ID = '"+motive+"'");
	
		String motiveanull = null;
	
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL,null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				motiveanull = rs.getString("MOTIVE");
			}
			rs.close();
			pstmt.close();
			String Mensaje = Msg.getMsg( getCtx(), 
					 "XX_CancellationMotive", 
					 new String[]{NumeroOC, 
								 "Anulada",
								 motiveanull
								 });
			
			Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , BPartner, -1, Attachment);
			f.ejecutarMail();
			f = null;
	
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}//Enviar
	
	
	private boolean EnviarCorreosCambioStatusJimport(String NumeroOC, Integer motive ,String Attachment) {	
		String motiveAnull = null;
		Integer jImport = 0;
		
		String sql = "SELECT B.C_BPARTNER_ID AS JIMPORT " +
					 "FROM C_BPARTNER B " +
					 "WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
					 "AND B.C_JOB_ID = "+getCtx().getContextAsInt("#XX_L_JOBPOSITION_IMPORMAN_ID")+" ";
		
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				jImport = rs.getInt("JIMPORT");
				
				String SQL2 = ("SELECT Name AS MOTIVE " +
						  	   "FROM XX_VMR_CancellationMotive WHERE XX_VMR_CancellationMotive_ID = '"+motive+"'");
		
				PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null);
				ResultSet rs2 = pstmt2.executeQuery();
				
				if(rs2.next()){
					motiveAnull = rs2.getString("MOTIVE");
					//System.out.println(motiveAnull); 
				}
				rs2.close();
				pstmt2.close();
				
				String sql3 = "SELECT u.AD_User_ID "
						   	+ "FROM AD_User u "
						   	+ "WHERE IsActive = 'Y' " 
						   	+ "AND C_BPARTNER_ID = " + jImport;
				
				PreparedStatement pstmt3 = DB.prepareStatement(sql3, null);
				ResultSet rs3 = pstmt3.executeQuery();
			
				String Mensaje = Msg.getMsg( getCtx(), 
						 					"XX_CancellationMotive", 
						 					new String[]{NumeroOC, 
									 					"Anulada",
									 					motiveAnull});
				
				
				while(rs3.next()){
					//System.out.println("Entra al while");
					Integer UserAuxID = rs3.getInt("AD_User_ID");
					Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
					
				}
				rs3.close();
				pstmt3.close();
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}//Enviar
	
	
	/*
	 * JTrias (07/04/2011)
	 */
	private void sendMailStatusChangeByRole(Integer role, String NumeroOC, Integer motive ,String Attachment) {	

		Integer user = 0;
		
		String sql = "SELECT select AD_USER_ID from AD_USER_ROLES " +
					 "WHERE AD_ROLE_ID = " + role;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			X_XX_VMR_CancellationMotive cancel = new X_XX_VMR_CancellationMotive( getCtx(), motive, null);
			
			String Mensaje = Msg.getMsg( getCtx(), 
 					"XX_CancellationMotive", 
 					new String[]{NumeroOC, 
			 					"Anulada",
			 					cancel.getName()});
			
			while(rs.next()){
				
				user = rs.getInt("AD_USER_ID");
					
				Utilities f = new Utilities(getCtx(), get_TrxName().getTrxName(),getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, user, Attachment);
				f.ejecutarMail();
				f = null;
			}

		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}//Enviar
	
	
/**
 * Fin Daniel Pellegrino
 * */
	
	private boolean orderIsInBoardingGuide(){
		
		String sql = "SELECT NVL(XX_VLO_BOARDINGGUIDE_ID,0) " +
					 "FROM C_ORDER WHERE C_ORDER_ID = " + getRecord_ID();

		try{
		
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
		
			while(rs.next()){
				
				if(rs.getInt(1)!=0){
					rs.close();
					pstmt.close();
					return true;
				}
			}
			
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		
		return false;
	}
	
	 /* 
	  * JTrias (Obtener Anticipos)
	  */
	private Vector<Integer> getAdvances() {	

		Vector<Integer> advances = new Vector<Integer>();
		
		String sql = "SELECT C_PAYMENT_ID " +
					 "FROM C_PAYMENT " +
					 "WHERE XX_ISADVANCE = 'Y' AND C_ORDER_ID = " + getRecord_ID();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
				advances.add(rs.getInt(1));
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return advances;	
	}
	
	/*
	 * Elimina la cuentas por pagar estimadas de la O/C
	 */
	private void deleteEstimatedAPayable(){
		
		String sql = "DELETE FROM XX_VCN_ESTIMATEDAPAYABLE where C_ORDER_ID = " + getRecord_ID() ;

		PreparedStatement pstmt = DB.prepareStatement(sql, get_Trx());
		ResultSet del;
		try {
			del = pstmt.executeQuery();
			del.close();
			pstmt.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage());
		}					
	}
	
	/**
	 * sendMailFinance
	 * Notificación por correo sobre anulación de oc con anticipo a finanzas
	 * @param message Mensaje del correo electronico
	 * @param motive Motivo de anulación
	 * 
	 * @author Maria Vintimilla
	 * 
	 * @return
	 */
	private void sendMailFinance(Integer userAnull, String oldPO, String newPO, 
			String oldPay, String newPay, Integer motive) {	
		String motiveAnull = null;
		String userAnullName = "";
		String SQLMotive = "";
		String SQLUser = "";
		ResultSet rsMotive = null;
		ResultSet rsUser = null;
		PreparedStatement pstmtMotive = null;
		PreparedStatement pstmtUser = null;
		int RoleFinance = Env.getCtx().getContextAsInt("#XX_L_ROLEFINANCIALMANAGER_ID");
		
		// Se obtiene el motivo de anulación de OC
		SQLMotive = " SELECT Name AS MOTIVE " +
				" FROM XX_VMR_CancellationMotive " +
				" WHERE XX_VMR_CancellationMotive_ID = " + motive;

		try {
			pstmtMotive = DB.prepareStatement(SQLMotive, null);
			rsMotive = pstmtMotive.executeQuery();
			if(rsMotive.next()){
				motiveAnull = rsMotive.getString("MOTIVE");
			}
		
		}//try
		catch (Exception e){
			log.saveError("ErrorSql Correo anulacion OC", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rsMotive);
			DB.closeStatement(pstmtMotive);
		}//finally
		
		// Se obtiene el nombre del usuario de anulación
		SQLUser = " SELECT Name AS USERANULL " +
				" FROM AD_USER " +
				" WHERE AD_USER_ID = " + userAnull;

		try {
			pstmtUser = DB.prepareStatement(SQLUser, null);
			rsUser = pstmtUser.executeQuery();
			if(rsUser.next()){
				userAnullName = rsUser.getString("USERANULL");
			}

		}//try
		catch (Exception e){
			log.saveError("ErrorSql Correo anulacion OC", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rsUser);
			DB.closeStatement(pstmtUser);
		}//finally
		
		// Se Anulo con copia la O/C # {0}, por el usuario {1} - Motivo de la Anulación {2}, 
		//la cual tiene un adelanto de pago # {3}, y el nuevo numero de O/C es {4} y Adelanto de Pago # {5}
		String message = Msg.getMsg( Env.getCtx(), "XX_AnullPO", 
				new String[]{oldPO,userAnullName,motiveAnull,oldPay,newPay});

		Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_ANULLPO_ID"), 
				message, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, RoleFinance, null);
		try {
			f.ejecutarMail();
		} catch (Exception e) {
			e.printStackTrace();
		}   
		f = null;

	} // Fin sendMailFinance
	
}
