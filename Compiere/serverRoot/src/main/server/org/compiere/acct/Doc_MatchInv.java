/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.acct;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Post MatchInv Documents.
 *  <pre>
 *  Table:              M_MatchInv (472)
 *  Document Types:     MXI
 *  </pre>
 *  Update Costing Records
 *  @author Jorg Janke
 *  @version  $Id: Doc_MatchInv.java 9168 2010-08-05 07:16:23Z ragrawal $
 */
public class Doc_MatchInv extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trx p_trx
	 */
	public Doc_MatchInv (MAcctSchema[] ass, ResultSet rs, Trx trx)
	{
		super(ass, MMatchInv.class, rs, MDocBaseType.DOCBASETYPE_MatchInvoice, trx);
	}   //  Doc_MatchInv

	/** Invoice Line			*/
	private MInvoiceLine	m_invoiceLine = null;
	/** Material Receipt		*/
	private MInOutLine		m_receiptLine = null;
	
	private ProductCost		m_pc = null;
	
	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	@Override
	public String loadDocumentDetails()
	{
		setC_Currency_ID (Doc.NO_CURRENCY);
		MMatchInv matchInv = (MMatchInv)getPO();
		setDateDoc(matchInv.getDateTrx());
		setQty (matchInv.getQty());
		//	Invoice Info
		int C_InvoiceLine_ID = matchInv.getC_InvoiceLine_ID();
		m_invoiceLine = new MInvoiceLine (getCtx(), C_InvoiceLine_ID, null);
		//		BP for NotInvoicedReceipts
		int C_BPartner_ID = m_invoiceLine.getParent().getC_BPartner_ID(); 
		setC_BPartner_ID(C_BPartner_ID);
		//
		int M_InOutLine_ID = matchInv.getM_InOutLine_ID();
		m_receiptLine = new MInOutLine (getCtx(), M_InOutLine_ID, null);		
		//
		m_pc = new ProductCost (Env.getCtx(), 
			getM_Product_ID(), matchInv.getM_AttributeSetInstance_ID(), null);
		m_pc.setQty(getQty());
		
		return null;
	}   //  loadDocumentDetails


	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return Zero (always balanced)
	 */
	@Override
	public BigDecimal getBalance()
	{
		return Env.ZERO;
	}   //  getBalance

	
	/**
	 *  Create Facts (the accounting logic) for
	 *  MXI.
	 * 	(single line)
	 *  <pre>
	 *      NotInvoicedReceipts     DR			(Receipt Org)
	 *      InventoryClearing               CR
	 *      InvoicePV               DR      CR  (difference)
	 *  Commitment
	 * 		Expense							CR
	 * 		Offset					DR
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	@Override
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//  Nothing to do
		if (getM_Product_ID() == 0								//	no Product
			|| getQty().signum() == 0
			|| m_receiptLine.getMovementQty().signum() == 0)	//	Qty = 0
		{
			log.fine("No Product/Qty - M_Product_ID=" + getM_Product_ID()
				+ ",Qty=" + getQty() + ",InOutQty=" + m_receiptLine.getMovementQty());
			return facts;
		}
		MMatchInv matchInv = (MMatchInv)getPO();
		
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		setC_Currency_ID (as.getC_Currency_ID());

		//  NotInvoicedReceipt      DR
		//  From Receipt
		FactLine dr = fact.createLine (null,
			getAccount(Doc.ACCTTYPE_NotInvoicedReceipts, as),
			as.getC_Currency_ID(), Env.ONE, null);			// updated below
		if (dr == null)
		{
			p_Error = "No Product Costs";
			return null;
		}
		dr.setQty(getQty());
		BigDecimal temp = dr.getAcctBalance();
		//if the Receipt match requirement is Invoice at client level, then matching invoice has to be posted before receipt
		if (!MClientInfo.get(this.getCtx(),this.getAD_Client_ID()).getMatchRequirementR().equals(X_M_InOut.MATCHREQUIREMENTR_Invoice))
		{
			//	Set AmtAcctCr/Dr from Receipt (sets also Project)
			if (!dr.updateReverseLine (X_M_InOut.Table_ID, 		//	Amt updated
				m_receiptLine.getM_InOut_ID(), m_receiptLine.getM_InOutLine_ID(),
				getQty(), m_receiptLine.getMovementQty(), as.getC_Currency_ID()))
			{
				p_Error = "Mat.Receipt not posted yet";
				return null;
			}
		}
		MInOutLine l  = new MInOutLine(this.getCtx(), matchInv.getM_InOutLine_ID(), this.getTrx());
		MOrderLine ol = new MOrderLine(this.getCtx(),l.getC_OrderLine_ID(),this.getTrx());
		BigDecimal price = ol.getLineNetAmt();
		BigDecimal Qty = ol.getQtyOrdered();
		DocLine line = new DocLine(l,this);
		BigDecimal LineDrAmount = price.multiply(dr.getQty()).divide(Qty,10,BigDecimal.ROUND_HALF_UP);
		MProduct product = line.getProduct();
		if (price == null || price.signum() == 0)
		{
			p_Error = "Resubmit - No Costs for " + product.getName();
			return null;
		}
		dr.setAmtSource(as.getC_Currency_ID(), LineDrAmount , null);
		dr.convert();
		dr.setM_Locator_ID(line.getM_Locator_ID());
		dr.setLocationFromBPartner(getC_BPartner_Location_ID(), true);   // from Loc
		dr.setLocationFromLocator(line.getM_Locator_ID(), false);  

		log.fine("CR - Amt(" + temp + "->" + dr.getAcctBalance() 
			+ ") - " + dr.toString());

		//  InventoryClearing               CR
		//  From Invoice
		MAccount expense = m_pc.getAccount(ProductCost.ACCTTYPE_P_InventoryClearing, as);
		if (m_pc.isService())
			expense = m_pc.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
		BigDecimal LineNetAmt = m_invoiceLine.getLineNetAmt();
		BigDecimal multiplier = getQty()
			.divide(m_invoiceLine.getQtyInvoiced(), 12, BigDecimal.ROUND_HALF_UP)
			.abs();
		if (multiplier.compareTo(Env.ONE) != 0)
			LineNetAmt = LineNetAmt.multiply(multiplier);
		if (m_pc.isService())
			LineNetAmt = dr.getAcctBalance();	//	book out exact receipt amt
		FactLine cr = null;
		if (as.isAccrual())
		{
			// The createLine parameter "createZero = true" will cause a zero accounting line
			// to be generated. This ensures that the fact will be balanced by IPV.
			cr = fact.createLine (null, expense,
				as.getC_Currency_ID(), null, LineNetAmt, true);		//	updated below
			if (cr == null)
			{
				log.fine("Line Net Amt=0 - M_Product_ID=" + getM_Product_ID()
					+ ",Qty=" + getQty() + ",InOutQty=" + m_receiptLine.getMovementQty());
				facts.add(fact);
				return facts;
			}
			cr.setQty(getQty().negate());
			temp = cr.getAcctBalance();
			//	Set AmtAcctCr/Dr from Invoice (sets also Project)
			if (as.isAccrual() && !cr.updateReverseLine (X_C_Invoice.Table_ID, 		//	Amt updated
				m_invoiceLine.getC_Invoice_ID(), m_invoiceLine.getC_InvoiceLine_ID(), 
				getQty(), m_invoiceLine.getQtyInvoiced(), as.getC_Currency_ID()))
			{
				p_Error = "Invoice not posted yet";
				return null;
			}
			log.fine("DR - Amt(" + temp + "->" + cr.getAcctBalance() 
				+ ") - " + cr.toString());
		}
		else	//	Cash Acct
		{
			MInvoice invoice = m_invoiceLine.getParent();
			if (as.getC_Currency_ID() == invoice.getC_Currency_ID())
				LineNetAmt = MConversionRate.convert(getCtx(), LineNetAmt, 
					invoice.getC_Currency_ID(), as.getC_Currency_ID(),
					invoice.getDateAcct(), invoice.getC_ConversionType_ID(),
					invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
			cr = fact.createLine (null, expense,
				as.getC_Currency_ID(), null, LineNetAmt);
			cr.setQty(getQty().multiply(multiplier).negate());
		}
		cr.setC_Activity_ID(m_invoiceLine.getC_Activity_ID());
		cr.setC_Campaign_ID(m_invoiceLine.getC_Campaign_ID());
		cr.setC_Project_ID(m_invoiceLine.getC_Project_ID());
		cr.setC_UOM_ID(m_invoiceLine.getC_UOM_ID());
		cr.setUser1_ID(m_invoiceLine.getUser1_ID());
		cr.setUser2_ID(m_invoiceLine.getUser2_ID());

		//  Invoice Price Variance 	difference
		BigDecimal ipv = cr.getAcctBalance().add(dr.getAcctBalance()).negate();
		if (ipv.signum() != 0)
		{
			FactLine pv = fact.createLine(null,
				m_pc.getAccount(ProductCost.ACCTTYPE_P_IPV, as),
				as.getC_Currency_ID(), ipv);
			pv.setC_Activity_ID(m_invoiceLine.getC_Activity_ID());
			pv.setC_Campaign_ID(m_invoiceLine.getC_Campaign_ID());
			pv.setC_Project_ID(m_invoiceLine.getC_Project_ID());
			pv.setC_UOM_ID(m_invoiceLine.getC_UOM_ID());
			pv.setUser1_ID(m_invoiceLine.getUser1_ID());
			pv.setUser2_ID(m_invoiceLine.getUser2_ID());
		}
		log.fine("IPV=" + ipv + "; Balance=" + fact.getSourceBalance());
		
		MInOut inOut = m_receiptLine.getParent(); 
		boolean isReturnTrx = inOut.isReturnTrx();
		
		//	Cost Detail Record - data from Expense/IncClearing (CR) record
		MCostDetail.createInvoice(as, getAD_Org_ID(), 
			getM_Product_ID(), matchInv.getM_AttributeSetInstance_ID(),
			m_invoiceLine.getC_InvoiceLine_ID(), 0,		//	No cost element
			cr.getAcctBalance().negate(), isReturnTrx? getQty().negate() : getQty(),		//	correcting
			getDescription(), getTrx());

		//  Update Costing
		updateProductInfo(as.getC_AcctSchema_ID(), 
			X_C_AcctSchema.COSTINGMETHOD_StandardCosting.equals(as.getCostingMethod()));
		//
		facts.add(fact);
		
		/** Commitment release										****/
		if (as.isAccrual() && as.isCreateCommitment())
		{
			fact = Doc_Order.getCommitmentRelease(as, this, 
				getQty(), m_invoiceLine.getC_InvoiceLine_ID(), Env.ONE);
			if (fact == null)
				return null;
			facts.add(fact);
		}	//	Commitment
		
		return facts;
	}   //  createFact

	/**
	 *  Update Product Info (old).
	 *  - Costing (CostStandardCumQty, CostStandardCumAmt, CostAverageCumQty, CostAverageCumAmt)
	 *  @param C_AcctSchema_ID accounting schema
	 *  @param standardCosting true if std costing
	 *  @return true if updated
	 *  @deprecated old costing
	 */
	@Deprecated
	private boolean updateProductInfo (int C_AcctSchema_ID, boolean standardCosting)
	{
		log.fine("M_MatchInv_ID=" + get_ID());

		// Modified the query because of issues in EDB with the earlier query with the bind variables
		StringBuffer sql = new StringBuffer (
			"UPDATE M_Product_Costing pc "
		    + " SET CostStandardCumQty = (SELECT pc.CostStandardCumQty + m.Qty "
  									  + " FROM M_MatchInv m"
									  + " INNER JOIN C_InvoiceLine il ON (m.C_InvoiceLine_ID=il.C_InvoiceLine_ID)"
									  + " INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID),"
									  + " C_AcctSchema a "
									  + " WHERE pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
									  + " AND pc.M_Product_ID=m.M_Product_ID"
									  + " AND m.M_MatchInv_ID= ? ), "
			    + " CostStandardCumAmt = (SELECT pc.CostStandardCumAmt + "
			                               + " currencyConvert(il.PriceActual,i.C_Currency_ID,a.C_Currency_ID,"
			                                               + " i.DateInvoiced,i.C_ConversionType_ID, "
			                                               + " i.AD_Client_ID,i.AD_Org_ID)*m.Qty "
  									  + " FROM M_MatchInv m"
									  + " INNER JOIN C_InvoiceLine il ON (m.C_InvoiceLine_ID=il.C_InvoiceLine_ID)"
									  + " INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID),"
									  + " C_AcctSchema a "
									  + " WHERE pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
									  + " AND pc.M_Product_ID=m.M_Product_ID"
									  + " AND m.M_MatchInv_ID= ? ), "
               + " CostAverageCumQty =   (SELECT pc.CostAverageCumQty + m.Qty "
  									  + " FROM M_MatchInv m"
									  + " INNER JOIN C_InvoiceLine il ON (m.C_InvoiceLine_ID=il.C_InvoiceLine_ID)"
									  + " INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID),"
									  + " C_AcctSchema a "
									  + " WHERE pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
									  + " AND pc.M_Product_ID=m.M_Product_ID"
									  + " AND m.M_MatchInv_ID= ? ), "
			   + " CostAverageCumAmt =   (SELECT pc.CostAverageCumAmt + " 
			                              + " currencyConvert(il.PriceActual,i.C_Currency_ID,a.C_Currency_ID,"
			                                              + " i.DateInvoiced,i.C_ConversionType_ID, "
			                                              + " i.AD_Client_ID,i.AD_Org_ID)*m.Qty "
  									  + " FROM M_MatchInv m"
									  + " INNER JOIN C_InvoiceLine il ON (m.C_InvoiceLine_ID=il.C_InvoiceLine_ID)"
									  + " INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID),"
									  + " C_AcctSchema a "
									  + " WHERE pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
									  + " AND pc.M_Product_ID=m.M_Product_ID"
									  + " AND m.M_MatchInv_ID= ? ) "
			+ " WHERE pc.C_AcctSchema_ID= ? "
			+ " AND EXISTS (SELECT * FROM M_MatchInv m "
				        + " WHERE pc.M_Product_ID=m.M_Product_ID"
				        + " AND m.M_MatchInv_ID= ?)"); 
		
		Object[] params = new Object[]{get_ID(),get_ID(),get_ID(),get_ID(),C_AcctSchema_ID,get_ID()};
		int no = DB.executeUpdate(getTrx(), sql.toString(),params);
		log.fine("M_Product_Costing - Qty/Amt Updated #=" + no);

		//  Update Average Cost
		sql = new StringBuffer (
			  " UPDATE M_Product_Costing "
			+ " SET CostAverage = CostAverageCumAmt/DECODE(CostAverageCumQty, 0,1, CostAverageCumQty) "
			+ " WHERE C_AcctSchema_ID= ? "
			+ " AND M_Product_ID= ? ");
		params = new Object[]{C_AcctSchema_ID,getM_Product_ID()};
		no = DB.executeUpdate(getTrx(), sql.toString(),params);
		log.fine("M_Product_Costing - AvgCost Updated #=" + no);
		

		//  Update Current Cost
		if (!standardCosting)
		{
			sql = new StringBuffer (
				  " UPDATE M_Product_Costing "
				+ " SET CurrentCostPrice = CostAverage "
				+ " WHERE C_AcctSchema_ID= ? "
				+ " AND M_Product_ID= ? ");
			params = new Object[]{C_AcctSchema_ID,getM_Product_ID()};
			no = DB.executeUpdate(getTrx(), sql.toString(),params);
			log.fine("M_Product_Costing - CurrentCost Updated=" + no);
		}
		return true;
	}   //  updateProductInfo

}   //  Doc_MatchInv
