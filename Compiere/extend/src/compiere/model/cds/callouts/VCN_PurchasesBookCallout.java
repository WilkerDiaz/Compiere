package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MTax;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

import compiere.model.cds.MInvoice;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVCNApplicationNumber;

/**
 * @author Patricia Ayuso
 * @author ghuchet
 *
 */
public class VCN_PurchasesBookCallout extends CalloutEngine {
	
	/**	Logger							*/
	protected transient CLogger	log = CLogger.getCLogger (getClass());
	
	/**
	 * Callout when invoice is change in Purchase's Book
	 * 
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String invoice(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int C_Invoice_ID = ctx.getContextAsInt( WindowNo, "C_Invoice_ID");
		MInvoice mInvoice = new MInvoice(ctx, C_Invoice_ID, null);
		int C_Order_ID = mInvoice.getC_Order_ID();
		mTab.setValue("C_Order_ID", C_Order_ID);
		mTab.setValue("M_Warehouse_ID", mInvoice.getM_Warehouse_ID());
		int C_BPartner_ID = mInvoice.getC_BPartner_ID();
		mTab.setValue("C_BPartner_ID", C_BPartner_ID);

		BigDecimal percent = null;
		String sql = "select p.XX_PERCENRETEN from XX_VCN_PERCENRETEN p, C_BPARTNER b " 
				+ " where b.c_BPartner_ID = " + mInvoice.getC_BPartner_ID() 
				+ " and b.xx_percentajeretention_id = p.XX_VCN_PERCENRETEN_ID";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				percent = rs.getBigDecimal(1);
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		if (percent != null)
			mTab.setValue("XX_WithholdingTax", mInvoice.getXX_TaxAmount().multiply(percent));
		else
			mTab.setValue("XX_WithholdingTax", new BigDecimal(0));
		
		mTab.setValue("XX_TaxAmount", mInvoice.getXX_TaxAmount());
		mTab.setValue("XX_TotalInvCost", mInvoice.getGrandTotal());
		mTab.setValue("XX_TaxableBase", mInvoice.getGrandTotal().subtract(mInvoice.getXX_TaxAmount()));
		
		BigDecimal sum = null;
		sql = "select SUM(LineTotalAmt) from c_invoiceline where c_invoice_id = " 
				+ mInvoice.getC_Invoice_ID() +" and TaxAmt = 0 ";
		
		try {
			PreparedStatement pstmt2 = DB.prepareStatement(sql, null);
			pstmt2.setInt(1, mInvoice.getC_Invoice_ID());
			ResultSet rs2 = pstmt2.executeQuery();
			if (rs2.next())
				sum = rs2.getBigDecimal(1);
			rs2.close();
			pstmt2.close();
			
		} catch (SQLException e) {
			//
		}
		if(sum != null)
			mTab.setValue("XX_ExemptBase", sum);
		else
			mTab.setValue("XX_ExemptBase", new BigDecimal(0));
		
		//mTab.setValue("XX_NotSubjectBase", );
		
		Date utilDate = new Date(); //actual date
		long lnMilisegundos = utilDate.getTime();
		Timestamp fechaActual = new Timestamp(lnMilisegundos);
		mTab.setValue("XX_DATE", fechaActual);
		
		mTab.setValue("XX_DocumentDate", mInvoice.getDateInvoiced());
		mTab.setValue("XX_ControlNumber", mInvoice.getXX_ControlNumber());
		if(percent != null){
			MVCNApplicationNumber applNum = new MVCNApplicationNumber(ctx, 0, null);
			//mTab.setValue("XX_VCN_APPLICATIONNUMBER_ID", applNum.generateApplicationNumber(fechaActual, 0, C_BPartner_ID)); 
			mTab.setValue("XX_Withholding", applNum.generateApplicationNumber(fechaActual, C_Order_ID, false, null));
		}
		
		setCalloutActive(false);
		return "";		
	}
	
	/**
	 * Callout when debit or credit notify is change in Purchase's Book
	 * 
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String notify(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		String DocumentNo = ctx.getContext(WindowNo, "DocumentNo");
		int C_Invoice_ID = 0;
		String sql = "select i.c_invoice_id from m_matchpo m " 
			+ "join c_invoiceline i on i.c_invoiceline_id = m.c_invoiceline_id " 
			+ "where documentno = '" + DocumentNo + "' group by i.c_invoice_id";
	
		try {
			PreparedStatement pstmt0 = DB.prepareStatement(sql, null);
			ResultSet rs0 = pstmt0.executeQuery();
			if (rs0.next())
				C_Invoice_ID = rs0.getInt(1);
			rs0.close();
			pstmt0.close();
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		
		if (C_Invoice_ID != 0){
			MInvoice mInvoice = new MInvoice(ctx, C_Invoice_ID, null);
			mTab.setValue("C_Invoice_ID", C_Invoice_ID);
			int C_Order_ID = mInvoice.getC_Order_ID();
			mTab.setValue("C_Order_ID", C_Order_ID);
			MOrder mOrder = new MOrder(ctx, mInvoice.getC_Order_ID(), null);
			mTab.setValue("M_Warehouse_ID", mInvoice.getM_Warehouse_ID());
			int C_BPartner_ID = mInvoice.getC_BPartner_ID();
			mTab.setValue("C_BPartner_ID", C_BPartner_ID);	

			BigDecimal 	percent = null, 
						priceDif = null, 
						priceInv = null, 
						d1 = null, 
						d2 = null, 
						d3 = null, 
						d4 = null, 
						rate = null,
						taxAmt = null,
						totalCost = null,
						exempt = null;
			int qtyDif = 0, qtyOC = 0;
			sql = "select p.XX_PERCENRETEN from XX_VCN_PERCENRETEN p, C_BPARTNER b " 
					+ " where b.c_BPartner_ID = " + C_BPartner_ID 
					+ " and b.xx_percentajeretention_id = p.XX_VCN_PERCENRETEN_ID";
			
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					percent = rs.getBigDecimal(1);
				rs.close();
				pstmt.close();
				
			} catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			}
			// OJO Qué hacer cuando hay diferencia de precios y cantidad?¡?¡?¡?¡? hacer este query dos veces y crear dos asientos(no se pueden crear dos asientos)????
			sql = "Select i.PRICEACTUAL - i.PRICEENTERED as PriceDif, i.QTYINVOICED, " 
				+ " i.QTYINVOICED - i.QTYENTERED - r.QTYENTERED as QtyDif, t.RATE, "  
				+ " i.XX_DISCOUNT1, i.XX_DISCOUNT2, i.XX_DISCOUNT3, i.XX_DISCOUNT4, "
				+ " i.PRICEENTERED from M_MATCHPO m "
				+ " left join C_INVOICELINE i on i.C_INVOICELINE_ID = m.C_INVOICELINE_ID "
				+ " left join M_INOUTLINE r on r.M_INOUTLINE_ID = m.M_INOUTLINE_ID "
				+ " join C_TAX t on i.C_Tax_ID = t.C_Tax_ID "
				+ " where m.DocumentNo = '" + DocumentNo + "'";
		
			try {
				PreparedStatement pstmt1 = DB.prepareStatement(sql, null);
				ResultSet rs1 = pstmt1.executeQuery();
				while (rs1.next()){
					rate = rs1.getBigDecimal(4);			rate = rate.divide(new BigDecimal(100));
					d1 = rs1.getBigDecimal(5);				d1 = d1.divide(new BigDecimal(100));
					d2 = rs1.getBigDecimal(6);				d2 = d2.divide(new BigDecimal(100));
					d3 = rs1.getBigDecimal(7);				d3 = d3.divide(new BigDecimal(100));
					d4 = rs1.getBigDecimal(8);				d4 = d4.divide(new BigDecimal(100));
					
					// Cuando hay diferencia en cantidad
					qtyDif = rs1.getInt(3);
					priceInv = rs1.getBigDecimal(9);
					if (!priceInv.equals(null)){
						BigDecimal blah = priceInv.multiply(new BigDecimal(qtyDif));
						if (!d1.equals(null)) blah = (blah.multiply(d1)).add(blah);
						if (!d2.equals(null)) blah = (blah.multiply(d2)).add(blah);
						if (!d3.equals(null)) blah = (blah.multiply(d3)).add(blah);
						if (!d4.equals(null)) blah = (blah.multiply(d4)).add(blah);
						//
						if(!rate.equals(new BigDecimal(0))){
							taxAmt = taxAmt.add((blah.multiply(rate)));
							totalCost = totalCost.add(blah.add(taxAmt));	
						}else{
							totalCost = totalCost.add(blah);
							exempt = exempt.add(blah);
						}
					}
					
					// Cuando hay diferencia en el precio
					priceDif = rs1.getBigDecimal(1);
					qtyOC = rs1.getInt(2);
					if (!priceDif.equals(null)){
						BigDecimal blah2 = priceDif.multiply(new BigDecimal(qtyOC));
						// No se calculan descuentos... me imagino
						if(!rate.equals(new BigDecimal(0))){
							taxAmt = taxAmt.add((blah2.multiply(rate)));
							totalCost = totalCost.add(blah2.add(taxAmt));	
						}else{
							totalCost = totalCost.add(blah2);
							exempt = exempt.add(blah2);
						}
					}
				}
				rs1.close();
				pstmt1.close();
				
			} catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			}
			
			// Calculando el porcentaje de retencion
			if (percent != null)
				mTab.setValue("XX_WithholdingTax", taxAmt.multiply(percent));
			else
				mTab.setValue("XX_WithholdingTax", new BigDecimal(0));
			
			mTab.setValue("XX_TaxAmount", taxAmt);
			mTab.setValue("XX_TotalInvCost", totalCost);
			mTab.setValue("XX_TaxableBase", totalCost.subtract(taxAmt));
			mTab.setValue("XX_ExemptBase", exempt);
			//mTab.setValue("XX_NotSubjectBase", );
			
			Date utilDate = new Date(); //actual date
			long lnMilisegundos = utilDate.getTime();
			Timestamp fechaActual = new Timestamp(lnMilisegundos);
			mTab.setValue("XX_DATE", fechaActual);
			
			mTab.setValue("XX_DocumentDate", mOrder.getXX_ReceptionDate());
			mTab.setValue("XX_ControlNumber", mInvoice.getXX_ControlNumber());
			
			// Generando el número de comprobante de retención
			if(percent != null){
				MVCNApplicationNumber applNum = new MVCNApplicationNumber(ctx, 0, null);
				//mTab.setValue("XX_VCN_APPLICATIONNUMBER_ID", applNum.generateApplicationNumber(fechaActual, 0, C_BPartner_ID));
				mTab.setValue("XX_Withholding", applNum.generateApplicationNumber(fechaActual, C_Order_ID, false, null));
			}
		}
		setCalloutActive(false);
		return "";		
	}
	
	/**
	 * Callout when document tax is change in Purchase's Book
	 * 
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String tax(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int boardingGuide_ID = ctx.getContextAsInt( WindowNo, "XX_VLO_BoardingGuide_ID");
		if(boardingGuide_ID != 0){
			
		int C_Tax_ID = (Integer) mTab.getValue("C_Tax_ID");
		BigDecimal taxableBase = (BigDecimal) mTab.getValue("XX_TaxableBase");
		MTax mTax =  new MTax(ctx,C_Tax_ID, null);
		mTab.setValue("XX_TaxAmount", taxableBase.multiply(mTax.getRate()).divide(new BigDecimal(100)));
		
		BigDecimal exemptBase = (BigDecimal) mTab.getValue("XX_ExemptBase");
		BigDecimal notSubjectBase = (BigDecimal) mTab.getValue("XX_NotSubjectBase");
		BigDecimal taxAmount = (BigDecimal) mTab.getValue("XX_TaxAmount");

		mTab.setValue("XX_TotalInvCost", exemptBase.add(notSubjectBase).add(taxableBase).add(taxAmount));
		}
		setCalloutActive(false);
		return "";		
	}

	/**
	 * Callout when some amount is change in Purchase's Book
	 * 
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String amount(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int boardingGuide_ID = ctx.getContextAsInt( WindowNo, "XX_VLO_BoardingGuide_ID");
		if(boardingGuide_ID != 0){
			BigDecimal taxableBase = (BigDecimal) mTab.getValue("XX_TaxableBase");
			int C_Tax_ID = (Integer) mTab.getValue("C_Tax_ID");
			MTax mTax =  new MTax(ctx,C_Tax_ID, null);
			mTab.setValue("XX_TaxAmount", taxableBase.multiply(mTax.getRate()).divide(new BigDecimal(100)));
			
			BigDecimal exemptBase = (BigDecimal) mTab.getValue("XX_ExemptBase");
			BigDecimal notSubjectBase = (BigDecimal) mTab.getValue("XX_NotSubjectBase");
			BigDecimal taxAmount = (BigDecimal) mTab.getValue("XX_TaxAmount");

			mTab.setValue("XX_TotalInvCost", exemptBase.add(notSubjectBase).add(taxableBase).add(taxAmount));

		}
		
		setCalloutActive(false);
		return "";		
	}

}
