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
package org.compiere.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.api.UICallout;
import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.common.constants.EnvConstants;
import org.compiere.framework.PO;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Msg;
import org.compiere.util.QueryUtil;
import org.compiere.util.Trx;
import org.compiere.util.Util;
import org.compiere.util.Env.QueryParams;
import org.compiere.vos.DocActionConstants;


/**
 *	Invoice Model.
 * 	Please do not set DocStatus and C_DocType_ID directly.
 * 	They are set in the process() method.
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MInvoice.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MInvoice extends X_C_Invoice implements DocAction
{
    /** Logger for class MInvoice */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInvoice.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Payments Of BPartner
	 *	@param ctx context
	 *	@param C_BPartner_ID id
	 *	@param trx transaction
	 *	@return array
	 */
	public static MInvoice[] getOfBPartner (Ctx ctx, int C_BPartner_ID, Trx trx)
	{
		ArrayList<MInvoice> list = new ArrayList<MInvoice>();
		String sql = "SELECT * FROM C_Invoice WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, C_BPartner_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MInvoice(ctx,rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		MInvoice[] retValue = new MInvoice[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfBPartner

	/**
	 * 	Create new Invoice by copying
	 * 	@param from invoice
	 * 	@param dateDoc date of the document date
	 * 	@param C_DocTypeTarget_ID target doc type
	 * 	@param isSOTrx sales order
	 * 	@param counter create counter links
	 * 	@param trx p_trx
	 * 	@param setOrder set Order links
	 *	@return Invoice
	 */
	public static MInvoice copyFrom (MInvoice from, Timestamp dateDoc,
			int C_DocTypeTarget_ID,
			boolean counter, Trx trx, boolean setOrder)
	{
		MInvoice to = new MInvoice (from.getCtx(), 0, null);
		to.set_Trx(trx);
		PO.copyValues (from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("C_Invoice_ID", I_ZERO);
		to.set_ValueNoCheck ("DocumentNo", null);
		//
		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		to.setDocAction(DOCACTION_Complete);
		//
		to.setC_DocType_ID(0);
		to.setC_DocTypeTarget_ID (C_DocTypeTarget_ID, true);
		//
		to.setDateInvoiced (dateDoc);
		to.setDateAcct (dateDoc);
		to.setDatePrinted(null);
		to.setIsPrinted (false);
		//
		to.setIsApproved (false);
		to.setC_Payment_ID(0);
		to.setC_CashLine_ID(0);
		to.setIsPaid (false);
		to.setIsInDispute(false);
		//
		//	Amounts are updated by trigger when adding lines
		to.setGrandTotal(Env.ZERO);
		to.setTotalLines(Env.ZERO);
		//
		to.setIsTransferred (false);
		to.setPosted (false);
		to.setProcessed (false);
		//	delete references
		to.setIsSelfService(false);
		if (!setOrder)
			to.setC_Order_ID(0);
		if (counter)
		{
			to.setRef_Invoice_ID(from.getC_Invoice_ID());
			//	Try to find Order link
			if (from.getC_Order_ID() != 0)
			{
				MOrder peer = new MOrder (from.getCtx(), from.getC_Order_ID(), from.get_Trx());
				if (peer.getRef_Order_ID() != 0)
					to.setC_Order_ID(peer.getRef_Order_ID());
			}
		}
		else
			to.setRef_Invoice_ID(0);

		if (!to.save(trx))
			throw new CompiereStateException("Could not create Invoice");
		if (counter)
			from.setRef_Invoice_ID(to.getC_Invoice_ID());

		//	Lines
		if (to.copyLinesFrom(from, counter, setOrder) == 0)
			throw new CompiereStateException("Could not create Invoice Lines");

		return to;
	}	//	copyFrom

	/**
	 * 	Get PDF File Name
	 *	@param documentDir directory
	 * 	@param C_Invoice_ID invoice
	 *	@return file name
	 */
	public static String getPDFFileName (String documentDir, int C_Invoice_ID)
	{
		StringBuffer sb = new StringBuffer (documentDir);
		if (sb.length() == 0)
			sb.append(".");
		if (!sb.toString().endsWith(File.separator))
			sb.append(File.separator);
		sb.append("C_Invoice_ID_")
		.append(C_Invoice_ID)
		.append(".pdf");
		return sb.toString();
	}	//	getPDFFileName


	/**
	 * 	Get MInvoice from Cache
	 *	@param ctx context
	 *	@param C_Invoice_ID id
	 *	@return MInvoice
	 */
	public static MInvoice get (Ctx ctx, int C_Invoice_ID)
	{
		Integer key = Integer.valueOf (C_Invoice_ID);
		MInvoice retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MInvoice (ctx, C_Invoice_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static final CCache<Integer,MInvoice>	s_cache	= new CCache<Integer,MInvoice>("C_Invoice", 20, 2);	//	2 minutes


	/**************************************************************************
	 * 	Invoice Constructor
	 * 	@param ctx context
	 * 	@param C_Invoice_ID invoice or 0 for new
	 * 	@param trx p_trx name
	 */
	public MInvoice (Ctx ctx, int C_Invoice_ID, Trx trx)
	{
		super (ctx, C_Invoice_ID, trx);
		if (C_Invoice_ID == 0)
		{
			setDocStatus (DOCSTATUS_Drafted);		//	Draft
			setDocAction (DOCACTION_Complete);
			//
			setPaymentRule(PAYMENTRULE_OnCredit);	//	Payment Terms

			setDateInvoiced (new Timestamp (System.currentTimeMillis ()));
			setDateAcct (new Timestamp (System.currentTimeMillis ()));
			//
			setChargeAmt (Env.ZERO);
			setTotalLines (Env.ZERO);
			setGrandTotal (Env.ZERO);
			//
			setIsSOTrx (true);
			setIsTaxIncluded (false);
			setIsApproved (false);
			setIsDiscountPrinted (false);
			setIsPaid (false);
			setSendEMail (false);
			setIsPrinted (false);
			setIsTransferred (false);
			setIsSelfService(false);
			setIsPayScheduleValid(false);
			setIsInDispute(false);
			setPosted(false);
			setIsReturnTrx(false);
			super.setProcessed (false);
			setProcessing(false);
		}
	}	//	MInvoice

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *	@param trx transaction
	 */
	public MInvoice (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MInvoice

	/**
	 * 	Create Invoice from Order
	 *	@param order order
	 *	@param C_DocTypeTarget_ID target document type
	 *	@param invoiceDate date or null
	 */
	public MInvoice (MOrder order, int C_DocTypeTarget_ID, Timestamp invoiceDate)
	{
		this (order.getCtx(), 0, order.get_Trx());
		setClientOrg(order);
		setOrder(order);	//	set base settings
		//
		if (C_DocTypeTarget_ID == 0)
			C_DocTypeTarget_ID =  QueryUtil.getSQLValue(get_Trx(),
					"SELECT C_DocTypeInvoice_ID FROM C_DocType WHERE C_DocType_ID=?",
					order.getC_DocType_ID());
		setC_DocTypeTarget_ID(C_DocTypeTarget_ID, true);
		if (invoiceDate != null)
			setDateInvoiced(invoiceDate);
		setDateAcct(getDateInvoiced());
		//
		setSalesRep_ID(order.getSalesRep_ID());
		//
		setC_BPartner_ID(order.getBill_BPartner_ID());
		setC_BPartner_Location_ID(order.getBill_Location_ID());
		setAD_User_ID(order.getBill_User_ID());
		setC_BPartnerSR_ID(order.getC_BPartnerSR_ID());
		setC_CashBook_ID(order.getC_CashBook_ID());
	}	//	MInvoice

	/**
	 * 	Create Invoice from Shipment
	 *	@param ship shipment
	 *	@param invoiceDate date or null
	 */
	public MInvoice (MInOut ship, Timestamp invoiceDate)
	{
		this (ship.getCtx(), 0, ship.get_Trx());
		setClientOrg(ship);
		setShipment(ship);	//	set base settings
		//
		setC_DocTypeTarget_ID();
		if (invoiceDate != null)
			setDateInvoiced(invoiceDate);
		setDateAcct(getDateInvoiced());
		//
		setSalesRep_ID(ship.getSalesRep_ID());
		setAD_User_ID(ship.getAD_User_ID());
	}	//	MInvoice

	/**
	 * 	Create Invoice from Batch Line
	 *	@param batch batch
	 *	@param line batch line
	 */
	public MInvoice (MInvoiceBatch batch, MInvoiceBatchLine line)
	{
		this (line.getCtx(), 0, line.get_Trx());
		setClientOrg(line);
		setDocumentNo(line.getDocumentNo());
		//
		setIsSOTrx(batch.isSOTrx());
		MBPartner bp = new MBPartner (line.getCtx(), line.getC_BPartner_ID(), line.get_Trx());
		setBPartner(bp);	//	defaults
		//
		setIsTaxIncluded(line.isTaxIncluded());
		//	May conflict with default price list
		setC_Currency_ID(batch.getC_Currency_ID());
		setC_ConversionType_ID(batch.getC_ConversionType_ID());
		//
		//	setPaymentRule(order.getPaymentRule());
		//	setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
		//	setPOReference("");
		setDescription(batch.getDescription());
		//	setDateOrdered(order.getDateOrdered());
		//
		setAD_OrgTrx_ID(line.getAD_OrgTrx_ID());
		setC_Project_ID(line.getC_Project_ID());
		//	setC_Campaign_ID(line.getC_Campaign_ID());
		setC_Activity_ID(line.getC_Activity_ID());
		setUser1_ID(line.getUser1_ID());
		setUser2_ID(line.getUser2_ID());
		//
		setC_DocTypeTarget_ID(line.getC_DocType_ID(), true);
		setDateInvoiced(line.getDateInvoiced());
		setDateAcct(line.getDateAcct());
		//
		setSalesRep_ID(batch.getSalesRep_ID());
		//
		setC_BPartner_ID(line.getC_BPartner_ID());
		setC_BPartner_Location_ID(line.getC_BPartner_Location_ID());
		setAD_User_ID(line.getAD_User_ID());
	}	//	MInvoice
	
	public MInvoice ( X_I_Invoice imp) {
		this (imp.getCtx(), 0, imp.get_Trx());
		PO.copyValues(imp, this, imp.getAD_Client_ID(), imp.getAD_Org_ID());

		if(getDateInvoiced() == null)
			setDateInvoiced (new Timestamp (System.currentTimeMillis ()));
		
		if(getDateAcct() == null)
			setDateAcct (new Timestamp (System.currentTimeMillis ()));
		
		
		setC_DocTypeTarget_ID(getC_DocType_ID(), true);
		setM_PriceList_ID(imp.getM_PriceList_ID());

	}

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MInvoice.class);

	/**
	 * 	Overwrite Client/Org if required
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	@Override
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	Set Business Partner Defaults & Details
	 * 	@param bp business partner
	 */
	public void setBPartner (MBPartner bp)
	{
		if (bp == null)
			return;

		setC_BPartner_ID(bp.getC_BPartner_ID());
		//	Set Defaults
		int ii = 0;
		if (isSOTrx())
			ii = bp.getC_PaymentTerm_ID();
		else
			ii = bp.getPO_PaymentTerm_ID();
		if (ii != 0)
			setC_PaymentTerm_ID(ii);
		//
		if (isSOTrx())
			ii = bp.getM_PriceList_ID();
		else
			ii = bp.getPO_PriceList_ID();
		if (ii != 0)
			setM_PriceList_ID(ii);
		//
		String ss = null;
		if (isSOTrx())
			ss = bp.getPaymentRule();
		else
			ss = bp.getPaymentRulePO();
		if (ss != null)
			setPaymentRule(ss);


		//	Set Locations
		MBPartnerLocation[] locs = bp.getLocations(false);
		if (locs != null)
		{
			for (MBPartnerLocation element : locs) {
				if (element.isBillTo() && isSOTrx()
						|| element.isPayFrom() && !isSOTrx())
					setC_BPartner_Location_ID(element.getC_BPartner_Location_ID());
			}
			//	set to first
			if (getC_BPartner_Location_ID() == 0 && locs.length > 0)
				setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
		}
		if (getC_BPartner_Location_ID() == 0)
			log.log(Level.SEVERE, "Has no To Address: " + bp);

		//	Set Contact
		MUser[] contacts = bp.getContacts(false);
		if (contacts != null && contacts.length > 0)	//	get first User
			setAD_User_ID(contacts[0].getAD_User_ID());
		//
		setC_Project_ID(0);
	}	//	setBPartner

	/**
	 * 	Set Order References
	 * 	@param order order
	 */
	public void setOrder (MOrder order)
	{
		if (order == null)
			return;

		setC_Order_ID(order.getC_Order_ID());
		setIsSOTrx(order.isSOTrx());
		setIsDiscountPrinted(order.isDiscountPrinted());
		setIsSelfService(order.isSelfService());
		setSendEMail(order.isSendEMail());
		//
		setM_PriceList_ID(order.getM_PriceList_ID());
		setIsTaxIncluded(order.isTaxIncluded());
		setC_Currency_ID(order.getC_Currency_ID());
		setC_ConversionType_ID(order.getC_ConversionType_ID());
		//
		setPaymentRule(order.getPaymentRule());
		setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
		setPOReference(order.getPOReference());
		setDescription(order.getDescription());
		setDateOrdered(order.getDateOrdered());
		setC_BP_BankAccount_ID(order.getC_BP_BankAccount_ID());
		//
		setAD_OrgTrx_ID(order.getAD_OrgTrx_ID());
		setC_Project_ID(order.getC_Project_ID());
		setC_Campaign_ID(order.getC_Campaign_ID());
		setC_Activity_ID(order.getC_Activity_ID());
		setUser1_ID(order.getUser1_ID());
		setUser2_ID(order.getUser2_ID());
	}	//	setOrder

	/**
	 * 	Set Shipment References
	 * 	@param ship shipment
	 */
	public void setShipment (MInOut ship)
	{
		if (ship == null)
			return;

		setIsSOTrx(ship.isSOTrx());
		//
		MBPartner bp = new MBPartner (getCtx(), ship.getC_BPartner_ID(), null);
		setBPartner (bp);
		//
		setSendEMail(ship.isSendEMail());
		//
		setPOReference(ship.getPOReference());
		setDescription(ship.getDescription());
		setDateOrdered(ship.getDateOrdered());
		//
		setAD_OrgTrx_ID(ship.getAD_OrgTrx_ID());
		setC_Project_ID(ship.getC_Project_ID());
		setC_Campaign_ID(ship.getC_Campaign_ID());
		setC_Activity_ID(ship.getC_Activity_ID());
		setUser1_ID(ship.getUser1_ID());
		setUser2_ID(ship.getUser2_ID());
		//
		if (ship.getC_Order_ID() != 0)
		{
			setC_Order_ID(ship.getC_Order_ID());
			MOrder order = new MOrder (getCtx(), ship.getC_Order_ID(), get_Trx());
			setIsDiscountPrinted(order.isDiscountPrinted());
			setM_PriceList_ID(order.getM_PriceList_ID());
			setIsTaxIncluded(order.isTaxIncluded());
			setC_Currency_ID(order.getC_Currency_ID());
			setC_ConversionType_ID(order.getC_ConversionType_ID());
			setPaymentRule(order.getPaymentRule());
			setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
			//
			MDocType dt = MDocType.get(getCtx(), order.getC_DocType_ID());
			if (dt.getC_DocTypeInvoice_ID() != 0)
				setC_DocTypeTarget_ID(dt.getC_DocTypeInvoice_ID(), true);
			//	Overwrite Invoice Address
			setC_BPartner_Location_ID(order.getBill_Location_ID());
		}
	}	//	setShipment

	/**
	 * 	Set Target Document Type
	 * 	@param DocBaseType doc type MDocBaseType.DOCBASETYPE_
	 */
	public void setC_DocTypeTarget_ID (String DocBaseType)
	{
		String sql = "SELECT C_DocType_ID FROM C_DocType "
			+ "WHERE AD_Client_ID=? AND DocBaseType=?"
			+ " AND IsActive='Y' "
			+ "ORDER BY ASCII(IsDefault) DESC";
		int C_DocType_ID = QueryUtil.getSQLValue(get_Trx(), sql, getAD_Client_ID(), DocBaseType);
		if (C_DocType_ID <= 0)
			log.log(Level.SEVERE, "Not found for AC_Client_ID="
					+ getAD_Client_ID() + " - " + DocBaseType);
		else
		{
			log.fine(DocBaseType);
			setC_DocTypeTarget_ID (C_DocType_ID);
			boolean isSOTrx = MDocBaseType.DOCBASETYPE_ARInvoice.equals(DocBaseType)
			|| MDocBaseType.DOCBASETYPE_ARCreditMemo.equals(DocBaseType);
			setIsSOTrx (isSOTrx);
			boolean isReturnTrx = MDocBaseType.DOCBASETYPE_ARCreditMemo.equals(DocBaseType)
			|| MDocBaseType.DOCBASETYPE_APCreditMemo.equals(DocBaseType);
			setIsReturnTrx(isReturnTrx);
		}
	}	//	setC_DocTypeTarget_ID

	/**
	 * 	Set Target Document Type.
	 * 	Based on SO flag AP/AP Invoice
	 */
	public void setC_DocTypeTarget_ID ()
	{
		if (getC_DocTypeTarget_ID() > 0)
			return;
		if (isSOTrx())
			setC_DocTypeTarget_ID(MDocBaseType.DOCBASETYPE_ARInvoice);
		else
			setC_DocTypeTarget_ID(MDocBaseType.DOCBASETYPE_APInvoice);
	}	//	setC_DocTypeTarget_ID

	/**
	 * 	Set Target Document Type
	 *	@param C_DocTypeTarget_ID id
	 *	@param setReturnTrx if true set ReturnTrx and SOTrx
	 */
	public void setC_DocTypeTarget_ID(int C_DocTypeTarget_ID, boolean setReturnTrx)
	{
		super.setC_DocTypeTarget_ID(C_DocTypeTarget_ID);
		if (setReturnTrx)
		{
			MDocType dt = MDocType.get(getCtx(), C_DocTypeTarget_ID);
			setIsSOTrx(dt.isSOTrx());
			setIsReturnTrx(dt.isReturnTrx());
		}
	}	//	setC_DocTypeTarget_ID

	/**
	 * 	Get Grand Total
	 * 	@param creditMemoAdjusted adjusted for CM (negative)
	 *	@return grand total
	 */
	public BigDecimal getGrandTotal (boolean creditMemoAdjusted)
	{
		if (!creditMemoAdjusted)
			return super.getGrandTotal();
		//
		BigDecimal amt = getGrandTotal();
		if (isCreditMemo())
			return amt.negate();
		return amt;
	}	//	getGrandTotal


	/**
	 * 	Get Invoice Lines of Invoice
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MInvoiceLine[] getLines (String whereClause)
	{
		ArrayList<MInvoiceLine> list = new ArrayList<MInvoiceLine>();
		String sql = "SELECT * FROM C_InvoiceLine WHERE C_Invoice_ID=? ";
		if (whereClause != null)
			sql += whereClause;
		sql += " ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Invoice_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MInvoiceLine il = new MInvoiceLine(getCtx(), rs, get_Trx());
				il.setInvoice(this);
				list.add(il);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//
		MInvoiceLine[] lines = new MInvoiceLine[list.size()];
		list.toArray(lines);
		return lines;
	}	//	getLines

	/**
	 * 	Get Invoice Lines
	 * 	@param requery
	 * 	@return lines
	 */
	@Deprecated
	public MInvoiceLine[] getLines (boolean requery)
	{
		return getLines();
	}	//	getLines

	/**
	 * 	Get Lines of Invoice
	 * 	@return lines
	 */
	public MInvoiceLine[] getLines()
	{
		return getLines(null);
	}	//	getLines


	/**
	 * 	Renumber Lines
	 *	@param step start and step
	 */
	public void renumberLines (int step)
	{
		int number = step;
		MInvoiceLine[] lines = getLines();
		for (MInvoiceLine line : lines) {
			line.setLine(number);
			line.save();
			number += step;
		}
	}	//	renumberLines

	/**
	 * 	Copy Lines From other Invoice.
	 *	@param otherInvoice invoice
	 * 	@param counter create counter links
	 * 	@param setOrder set order links
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MInvoice otherInvoice, boolean counter, boolean setOrder)
	{
		if (isProcessed() || isPosted() || otherInvoice == null)
			return 0;
		MInvoiceLine[] fromLines = otherInvoice.getLines();
		int count = 0;
		for (MInvoiceLine fromLine : fromLines) {
			MInvoiceLine line = new MInvoiceLine (getCtx(), 0, get_Trx());
			if (counter)	//	header
				PO.copyValues (fromLine, line, getAD_Client_ID(), getAD_Org_ID());
			else
				PO.copyValues (fromLine, line, fromLine.getAD_Client_ID(), fromLine.getAD_Org_ID());
			line.setC_Invoice_ID(getC_Invoice_ID());
			line.setInvoice(this);
			line.set_ValueNoCheck ("C_InvoiceLine_ID", I_ZERO);	// new
			//	Reset
			if (!setOrder)
				line.setC_OrderLine_ID(0);
			line.setRef_InvoiceLine_ID(0);
			line.setM_InOutLine_ID(0);
			line.setA_Asset_ID(0);
			line.setM_AttributeSetInstance_ID(0);
			line.setS_ResourceAssignment_ID(0);
			//	New Tax
			if (getC_BPartner_ID() != otherInvoice.getC_BPartner_ID())
				line.setTax();	//	recalculate
			//
			if (counter)
			{
				line.setRef_InvoiceLine_ID(fromLine.getC_InvoiceLine_ID());
				if (fromLine.getC_OrderLine_ID() != 0)
				{
					MOrderLine peer = new MOrderLine (getCtx(), fromLine.getC_OrderLine_ID(), get_Trx());
					if (peer.getRef_OrderLine_ID() != 0)
						line.setC_OrderLine_ID(peer.getRef_OrderLine_ID());
				}
				line.setM_InOutLine_ID(0);
				if (fromLine.getM_InOutLine_ID() != 0)
				{
					MInOutLine peer = new MInOutLine (getCtx(), fromLine.getM_InOutLine_ID(), get_Trx());
					if (peer.getRef_InOutLine_ID() != 0)
						line.setM_InOutLine_ID(peer.getRef_InOutLine_ID());
				}				
			}
			//
			line.setProcessed(false);
			if (line.save(get_Trx()))
				count++;
			//	Cross Link
			if (counter)
			{
				fromLine.setRef_InvoiceLine_ID(line.getC_InvoiceLine_ID());
				fromLine.save(get_Trx());
			}
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

	/** Reversal Flag		*/
	private boolean m_reversal = false;

	/**
	 * 	Set Reversal
	 *	@param reversal reversal
	 */
	private void setReversal(boolean reversal)
	{
		m_reversal = reversal;
	}	//	setReversal
	/**
	 * 	Is Reversal
	 *	@return reversal
	 */
	private boolean isReversal()
	{
		return m_reversal;
	}	//	isReversal

	/**
	 * 	Get Taxes
	 *	@param requery requery
	 *	@return array of taxes
	 */
	@Deprecated
	public MInvoiceTax[] getTaxes (boolean requery)
	{
		return getTaxes();
	}
	
	/**
	 * 	Get Taxes
	 *	@return array of taxes
	 */
	public MInvoiceTax[] getTaxes()
	{
		String sql = "SELECT * FROM C_InvoiceTax WHERE C_Invoice_ID=?";
		ArrayList<MInvoiceTax> list = new ArrayList<MInvoiceTax>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_Invoice_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MInvoiceTax(getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getTaxes", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MInvoiceTax[] taxes = new MInvoiceTax[list.size()];
		list.toArray(taxes);
		return taxes;
	}	//	getTaxes

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 * 	Is it a Credit Memo?
	 *	@return true if CM
	 */
	public boolean isCreditMemo()
	{
		MDocType dt = MDocType.get(getCtx(),
				getC_DocType_ID()==0 ? getC_DocTypeTarget_ID() : getC_DocType_ID());
		return MDocBaseType.DOCBASETYPE_APCreditMemo.equals(dt.getDocBaseType())
		|| MDocBaseType.DOCBASETYPE_ARCreditMemo.equals(dt.getDocBaseType());
	}	//	isCreditMemo

	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	@Override
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String set = "SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_Invoice_ID= ?";
		int noLine = DB.executeUpdate(get_Trx(), "UPDATE C_InvoiceLine " + set,getC_Invoice_ID());
		int noTax = DB.executeUpdate(get_Trx(), "UPDATE C_InvoiceTax " + set,getC_Invoice_ID());
		log.fine(processed + " - Lines=" + noLine + ", Tax=" + noTax);
	}	//	setProcessed

	/**
	 * 	Validate Invoice Pay Schedule
	 *	@return pay schedule is valid
	 */
	public boolean validatePaySchedule()
	{
		MInvoicePaySchedule[] schedule = MInvoicePaySchedule.getInvoicePaySchedule
		(getCtx(), getC_Invoice_ID(), 0, get_Trx());
		log.fine("#" + schedule.length);
		if (schedule.length == 0)
		{
			setIsPayScheduleValid(false);
			return false;
		}
		//	Add up due amounts
		BigDecimal total = Env.ZERO;
		for (MInvoicePaySchedule element : schedule) {
			element.setParent(this);
			BigDecimal due = element.getDueAmt();
			if (due != null)
				total = total.add(due);
		}
		boolean valid = getGrandTotal().compareTo(total) == 0;
		setIsPayScheduleValid(valid);

		//	Update Schedule Lines
		for (MInvoicePaySchedule element : schedule) {
			if (element.isValid() != valid)
			{
				element.setIsValid(valid);
				element.save(get_Trx());
			}
		}
		return valid;
	}	//	validatePaySchedule


	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if(newRecord)
			setC_CashBook_ID(0);
		//	No Partner Info - set Template
		if (getC_BPartner_ID() == 0)
			setBPartner(MBPartner.getTemplate(getCtx(), getAD_Client_ID()));
		if (getC_BPartner_Location_ID() == 0)
			setBPartner(new MBPartner(getCtx(), getC_BPartner_ID(), null));

		//	Price List
		if (getM_PriceList_ID() == 0)
		{
			int ii = getCtx().getContextAsInt( "#M_PriceList_ID");
			if (ii != 0)
				setM_PriceList_ID(ii);
			else
			{
				String sql = "SELECT M_PriceList_ID FROM M_PriceList WHERE AD_Client_ID=? AND IsDefault='Y'";
				ii = QueryUtil.getSQLValue (get_Trx(), sql, getAD_Client_ID());
				if (ii != 0)
					setM_PriceList_ID (ii);
			}
		}

		boolean validPLV = false;
		// Verify that price list has a valid version for the date
		String sql1 = "SELECT 1 "
			+ "FROM M_PriceList pl,M_PriceList_Version plv "
			+ "WHERE pl.M_PriceList_ID=plv.M_PriceList_ID"
			+ " AND pl.M_PriceList_ID=? "						//	1
			+ " AND plv.ValidFrom <=? "							//  2
			+ " AND pl.IsActive='Y' "
			+ " AND plv.IsActive='Y' ";
		//	Use newest price list - may not be future
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql1, get_Trx());
			pstmt.setInt(1, getM_PriceList_ID());
			pstmt.setTimestamp(2, getDateInvoiced());
			rs = pstmt.executeQuery();
			if (rs.next())
				validPLV = true;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql1, e);
			return false;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if(!validPLV)
		{
			log.saveError("Error", Msg.getMsg(getCtx(), "PriceListVersionNotFound"));
			return false;
		}

		//	Currency
		if (getC_Currency_ID() == 0)
		{
			String sql = "SELECT C_Currency_ID FROM M_PriceList WHERE M_PriceList_ID=?";
			int ii = QueryUtil.getSQLValue (get_Trx(), sql, getM_PriceList_ID());
			if (ii != 0)
				setC_Currency_ID (ii);
			else
				setC_Currency_ID(getCtx().getContextAsInt( "#C_Currency_ID"));
		}

		//	Sales Rep
		if (getSalesRep_ID() == 0)
		{
			int ii = getCtx().getContextAsInt( "#SalesRep_ID");
			if (ii != 0)
				setSalesRep_ID (ii);
		}

		//	Document Type
		if (getC_DocType_ID() == 0)
			setC_DocType_ID (0);	//	make sure it's set to 0
		if (getC_DocTypeTarget_ID() == 0)
			setC_DocTypeTarget_ID(isSOTrx() ? MDocBaseType.DOCBASETYPE_ARInvoice : MDocBaseType.DOCBASETYPE_APInvoice);

		//	Payment Term
		if (getC_PaymentTerm_ID() == 0)
		{
			int ii = getCtx().getContextAsInt( "#C_PaymentTerm_ID");
			if (ii != 0)
				setC_PaymentTerm_ID (ii);
			else
			{
				String sql = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID=? AND IsDefault='Y'";
				ii = QueryUtil.getSQLValue(null, sql, getAD_Client_ID());
				if (ii != 0)
					setC_PaymentTerm_ID (ii);
			}
		}

		//	BPartner Active
		if (newRecord || is_ValueChanged("C_BPartner_ID"))
		{
			MBPartner bp = MBPartner.get(getCtx(), getC_BPartner_ID());
			if (!bp.isActive())
			{
				log.saveWarning("NotActive", Msg.getMsg(getCtx(), "C_BPartner_ID"));
				return false;
			}
		}

		/*	Duplicate Document Number (AP)		*/
		String documentNo = getDocumentNo();
		if (newRecord
				&& !Util.isEmpty(documentNo) && !documentNo.startsWith("<"))
		{
			boolean duplicate = false;
			String sql2 = "SELECT 1 FROM C_Invoice "
				+ "WHERE C_BPartner_ID=?	"
				+ " AND (C_DocTypeTarget_ID=? OR C_DocType_ID=?)"
				+ " AND DocumentNo=?";
			try
			{
				pstmt = DB.prepareStatement(sql2, get_Trx());
				pstmt.setInt(1, getC_BPartner_ID());
				pstmt.setInt(2, getC_DocTypeTarget_ID());
				pstmt.setInt(3, getC_DocTypeTarget_ID());
				pstmt.setString(4, documentNo);
				rs = pstmt.executeQuery();
				if (rs.next())
					duplicate = true;
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql2, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			if (duplicate)
			{
				log.saveError("SaveErrorNotUnique", Msg.getMsg(getCtx(), "DocumentNo"));
				return false;
			}
		}
		/* */
		return true;
	}	//	beforeSave

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	@Override
	protected boolean beforeDelete ()
	{
		if (getC_Order_ID() != 0)
		{
			log.saveError("Error", Msg.getMsg(getCtx(), "CannotDelete"));
			return false;
		}
		return true;
	}	//	beforeDelete

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInvoice[")
		.append(get_ID()).append("-").append(getDocumentNo())
		.append(",GrandTotal=").append(getGrandTotal());
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo


	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success || newRecord)
			return success;

		if (is_ValueChanged("AD_Org_ID"))
		{
			String sql = "UPDATE C_InvoiceLine ol"
				+ " SET AD_Org_ID ="
				+ "(SELECT AD_Org_ID"
				+ " FROM C_Invoice o WHERE ol.C_Invoice_ID=o.C_Invoice_ID) "
				+ "WHERE C_Invoice_ID=? ";
			int no = DB.executeUpdate(get_Trx(), sql,getC_Invoice_ID());
			log.fine("Lines -> #" + no);
		}
		
		//only in batch mode redistribute tax
		if(getCtx().isBatchMode()) {
			recomputeTaxesAndTotals();
		}
		
		return true;
	}	//	afterSave


	/**
	 * Recompute all the taxes for the invoice
	 */
	private void recomputeTaxesAndTotals() {
		String sql = "SELECT * from C_InvoiceLine WHERE C_InvoiceLine_ID in (SELECT MIN(C_InvoiceLine_ID) from C_InvoiceLine WHERE C_Invoice_ID=? GROUP BY C_Tax_ID)"; 
		PreparedStatement pstmt = DB.prepareStatement(sql, get_Trx());
		ResultSet rs = null;
		try {
			pstmt.setInt(1, getC_Invoice_ID());
			rs = pstmt.executeQuery ();
			while(rs.next()) {
				MInvoiceLine line = new MInvoiceLine(getCtx(), rs, get_Trx());
				line.updateHeaderTax();
			}
		}
		catch(SQLException e) {
			log.log(Level.SEVERE, "Batch MOrder save - " + sql.toString(), e);
		} 
		finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
	/**
	 * 	Set Price List (and Currency) when valid
	 * 	@param M_PriceList_ID price list
	 */
	@Override
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		String sql = "SELECT M_PriceList_ID, C_Currency_ID, IsTaxIncluded "
			+ "FROM M_PriceList WHERE M_PriceList_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, M_PriceList_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				super.setM_PriceList_ID (rs.getInt(1));
				setC_Currency_ID (rs.getInt(2));
				setIsTaxIncluded(rs.getString(3).equals("Y"));
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	setM_PriceList_ID


	/**
	 * 	Get Allocated Amt in Invoice Currency
	 *	@return pos/neg amount or null
	 */
	public BigDecimal getAllocatedAmt ()
	{
		BigDecimal retValue = null;
		String sql = "SELECT SUM(currencyConvert(al.Amount+al.DiscountAmt+al.WriteOffAmt,"
			+ "ah.C_Currency_ID, i.C_Currency_ID,ah.DateTrx,COALESCE(i.C_ConversionType_ID,0), al.AD_Client_ID,al.AD_Org_ID)) " //jz
			+ "FROM C_AllocationLine al"
			+ " INNER JOIN C_AllocationHdr ah ON (al.C_AllocationHdr_ID=ah.C_AllocationHdr_ID)"
			+ " INNER JOIN C_Invoice i ON (al.C_Invoice_ID=i.C_Invoice_ID) "
			+ "WHERE al.C_Invoice_ID=?"
			+ " AND ah.IsActive='Y' AND al.IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Invoice_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getBigDecimal(1);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	log.fine("getAllocatedAmt - " + retValue);
		//	? ROUND(NVL(v_AllocatedAmt,0), 2);
		return retValue;
	}	//	getAllocatedAmt

	/**
	 * 	Test Allocation (and set paid flag)
	 *	@return true if updated
	 */
	public boolean testAllocation()
	{
		BigDecimal alloc = getAllocatedAmt();	//	absolute
		if (alloc == null)
			alloc = Env.ZERO;
		BigDecimal total = getGrandTotal();
		if (!isSOTrx())
			total = total.negate();
		if (isCreditMemo())
			total = total.negate();
		boolean test = total.compareTo(alloc) <= 0;
		boolean change = test != isPaid();
		if (change)
			setIsPaid(test);
		log.fine("Paid=" + test
				+ " (" + alloc + "=" + total + ")");
		return change;
	}	//	testAllocation

	/**
	 * 	Set Paid Flag for invoices
	 * 	@param ctx context
	 *	@param C_BPartner_ID if 0 all
	 *	@param trx transaction
	 */
	public static void setIsPaid (Ctx ctx, int C_BPartner_ID, Trx trx)
	{
		int counter = 0;
		String sql = "SELECT * FROM C_Invoice "
			+ "WHERE IsPaid='N' AND DocStatus IN ('CO','CL')";
		if (C_BPartner_ID > 1)
			sql += " AND C_BPartner_ID=?";
		else
			sql += " AND AD_Client_ID=" + ctx.getAD_Client_ID();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			if (C_BPartner_ID > 1)
				pstmt.setInt (1, C_BPartner_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MInvoice invoice = new MInvoice(ctx, rs, trx);
				if (invoice.testAllocation())
					if (invoice.save())
						counter++;
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		s_log.config("#" + counter);
		/**/
	}	//	setIsPaid

	/**
	 * 	Get Open Amount.
	 * 	Used by web interface
	 * 	@return Open Amt
	 */
	public BigDecimal getOpenAmt ()
	{
		return getOpenAmt (true, null);
	}	//	getOpenAmt

	/**
	 * 	Get Open Amount
	 * 	@param creditMemoAdjusted adjusted for CM (negative)
	 * 	@param paymentDate ignored Payment Date
	 * 	@return Open Amt
	 */
	public BigDecimal getOpenAmt (boolean creditMemoAdjusted, Timestamp paymentDate)
	{
		/**	Open Amount				*/

		if (isPaid())
			return Env.ZERO;

		BigDecimal openAmt = getGrandTotal();
		if (paymentDate != null)
		{
			//	Payment Discount
			//	Payment Schedule
		}
		BigDecimal allocated = getAllocatedAmt();
		if (allocated != null)
		{
			if (!isSOTrx() )
				allocated = allocated.negate();
			openAmt = openAmt.subtract(allocated);
		}

		//
		if (!creditMemoAdjusted)
			return openAmt;
		if (isCreditMemo())
			return openAmt.negate();
		return openAmt;
	}	//	getOpenAmt


	/**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
	public String getDocStatusName()
	{
		return MRefList.getListName(getCtx(), 131, getDocStatus());
	}	//	getDocStatusName


	/**************************************************************************
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
		ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
		if (re == null)
			return null;
		return re.getPDF(file);
	}	//	createPDF

	/**
	 * 	Get PDF File Name
	 *	@param documentDir directory
	 *	@return file name
	 */
	public String getPDFFileName (String documentDir)
	{
		return getPDFFileName (documentDir, getC_Invoice_ID());
	}	//	getPDFFileName

	/**
	 *	Get ISO Code of Currency
	 *	@return Currency ISO
	 */
	public String getCurrencyISO()
	{
		return MCurrency.getISO_Code (getCtx(), getC_Currency_ID());
	}	//	getCurrencyISO

	/**
	 * 	Get Currency Precision
	 *	@return precision
	 */
	public int getPrecision()
	{
		return MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
	}	//	getPrecision


	/**	Process Message 			*/
	private String		m_processMsg = null;

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocTypeTarget_ID());
		setIsReturnTrx(dt.isReturnTrx());
		setIsSOTrx(dt.isSOTrx());

		//	No Cash Book
		if (PAYMENTRULE_Cash.equals(getPaymentRule())
				&& MCashBook.get(getCtx(), getAD_Org_ID(), getC_Currency_ID()) == null)
		{
			m_processMsg = "@NoCashBook@";
			return DocActionConstants.STATUS_Invalid;
		}

		//	Convert/Check DocType
		if (getC_DocType_ID() != getC_DocTypeTarget_ID())
			setC_DocType_ID(getC_DocTypeTarget_ID());
		if (getC_DocType_ID() == 0)
		{
			m_processMsg = "No Document Type";
			return DocActionConstants.STATUS_Invalid;
		}

		explodeBOM();
		
		if (!calculateTaxTotal())	//	setTotals
		{
			m_processMsg = "Error calculating Tax";
			return DocActionConstants.STATUS_Invalid;
		}

		createPaySchedule();

		//	Credit Status
		if (isSOTrx() && !isReversal())
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
			if (X_C_BPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocActionConstants.STATUS_Invalid;
			}
		}

		//	Landed Costs
		if (!isSOTrx())
		{
			//	Lines
			MInvoiceLine[] lines = getLines();
			for (MInvoiceLine line : lines)
			{
				String error = line.allocateLandedCosts();
				if (error != null && error.length() > 0)
				{
					m_processMsg = error;
					return DocActionConstants.STATUS_Invalid;
				}
			}
		}

		//	Add up Amounts
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocActionConstants.STATUS_InProgress;
	}	//	prepareIt

	/**
	 * 	Explode non stocked BOM.
	 */
	private void explodeBOM ()
	{
		String where = "AND IsActive='Y' AND EXISTS "
			+ "(SELECT * FROM M_Product p WHERE C_InvoiceLine.M_Product_ID=p.M_Product_ID"
			+ " AND	p.IsBOM='Y' AND p.IsVerified='Y' AND p.IsStocked='N')";
		//
		String sql = "SELECT COUNT(*) FROM C_InvoiceLine "
			+ "WHERE C_Invoice_ID=? " + where;
		int count = QueryUtil.getSQLValue(get_Trx(), sql, getC_Invoice_ID());
		while (count != 0)
		{
			renumberLines (1000);		//	max 999 bom items

			//	Order Lines with non-stocked BOMs
			MInvoiceLine[] lines = getLines (where);
			for (MInvoiceLine line : lines) {
				MProduct product = MProduct.get (getCtx(), line.getM_Product_ID());
				log.fine(product.getName());
				//	New Lines
				int lineNo = line.getLine ();
				MBOMProduct[] boms = MBOMProduct.getBOMLines (product);
				for (MBOMProduct bom : boms) {
					MInvoiceLine newLine = new MInvoiceLine (this);
					newLine.setLine (++lineNo);
					newLine.setM_Product_ID (bom.getComponent().getM_Product_ID(),
							bom.getComponent().getC_UOM_ID());
					newLine.setQty (line.getQtyInvoiced().multiply(
							bom.getBOMQty ()));		//	Invoiced/Entered
					if (bom.getDescription () != null)
						newLine.setDescription (bom.getDescription ());
					//
					newLine.setPrice ();
					newLine.save (get_Trx());
				}
				//	Convert into Comment Line
				line.setM_Product_ID (0);
				line.setM_AttributeSetInstance_ID (0);
				line.setPriceEntered (Env.ZERO);
				line.setPriceActual (Env.ZERO);
				line.setPriceLimit (Env.ZERO);
				line.setPriceList (Env.ZERO);
				line.setLineNetAmt (Env.ZERO);
				//
				String description = product.getName ();
				if (product.getDescription () != null)
					description += " " + product.getDescription ();
				if (line.getDescription () != null)
					description += " " + line.getDescription ();
				line.setDescription (description);
				line.save (get_Trx());
			} //	for all lines with BOM

			count = QueryUtil.getSQLValue (get_Trx(), sql, getC_Invoice_ID ());
			renumberLines (10);
		}	//	while count != 0
	}	//	explodeBOM

	/**
	 * 	Calculate Tax and Total
	 * 	@return true if calculated
	 */
	private boolean calculateTaxTotal()
	{
		log.fine("");
		//	Delete Taxes
		if( 0 > DB.executeUpdate(get_Trx(), "DELETE FROM C_InvoiceTax WHERE C_Invoice_ID=?", new Object[]{getC_Invoice_ID()})){
			return false;
		}

		Object[][] taxIDs = QueryUtil.executeQuery(
								get_Trx(), "SELECT DISTINCT C_Tax_ID " +
								"FROM C_InvoiceLine WHERE C_Invoice_ID = ? " +
								"AND C_Tax_ID IS NOT NULL",
								getC_Invoice_ID());
		//	Lines
		for( Object[] taxIDs_row : taxIDs )
		{
			/**	Sync ownership for SO
			if (isSOTrx() && line.getAD_Org_ID() != getAD_Org_ID())
			{
				line.setAD_Org_ID(getAD_Org_ID());
				line.save();
			}	**/
			final int taxID = ((BigDecimal) taxIDs_row[0]).intValue();
			MInvoiceTax iTax = MInvoiceTax.get(getCtx(), getC_Invoice_ID(), taxID, getPrecision(), get_Trx(),
					getAD_Client_ID(), getAD_Org_ID(), isTaxIncluded()); // current Tax
			if (iTax != null) {
				iTax.setIsTaxIncluded(isTaxIncluded());
				if (!iTax.calculateTaxFromLines())
					return false;
				if (!iTax.save())
					return false;
			}
		}

		Object[][] sumLineNet = QueryUtil.executeQuery(
				get_Trx(), "SELECT SUM(LineNetAmt) FROM C_InvoiceLine WHERE C_Invoice_ID = ?", 
				getC_Invoice_ID());
		
		//	Taxes
		BigDecimal totalLines = (BigDecimal) sumLineNet[0][0];
		BigDecimal grandTotal = totalLines;
		MInvoiceTax[] taxes = getTaxes();
		for (MInvoiceTax iTax : taxes)
		{
			MTax tax = iTax.getTax();
			if (tax.isSummary())
			{
				MTax[] cTaxes = tax.getChildTaxes(false);	//	Multiple taxes
				for (MTax cTax : cTaxes)
				{
					BigDecimal taxAmt = cTax.calculateTax(iTax.getTaxBaseAmt(), isTaxIncluded(), getPrecision());
					//
					MInvoiceTax newITax = new MInvoiceTax(getCtx(), 0, get_Trx());
					newITax.setClientOrg(this);
					newITax.setC_Invoice_ID(getC_Invoice_ID());
					newITax.setC_Tax_ID(cTax.getC_Tax_ID());
					newITax.setPrecision(getPrecision());
					newITax.setIsTaxIncluded(isTaxIncluded());
					newITax.setTaxBaseAmt(iTax.getTaxBaseAmt());
					newITax.setTaxAmt(taxAmt);
					if (!newITax.save(get_Trx()))
						return false;
					//
					if (!isTaxIncluded())
						grandTotal = grandTotal.add(taxAmt);
				}
				if (!iTax.delete(true, get_Trx()))
					return false;
			}
			else
			{
				if (!isTaxIncluded())
					grandTotal = grandTotal.add(iTax.getTaxAmt());
			}
		}
		//
		setTotalLines(totalLines);
		setGrandTotal(grandTotal);
		return true;
	}	//	calculateTaxTotal


	/**
	 * 	(Re) Create Pay Schedule
	 *	@return true if valid schedule
	 */
	private boolean createPaySchedule()
	{
		if (getC_PaymentTerm_ID() == 0)
			return false;
		MPaymentTerm pt = new MPaymentTerm(getCtx(), getC_PaymentTerm_ID(), null);
		log.fine(pt.toString());
		return pt.apply(this);		//	calls validate pay schedule
	}	//	createPaySchedule


	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt

	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		StringBuffer info = new StringBuffer();

		//	Create Cash
		if (PAYMENTRULE_Cash.equals(getPaymentRule()))
		{
			MCash cash = MCash.get (getCtx(), getAD_Org_ID(),
					getDateInvoiced(), getC_Currency_ID(), getC_CashBook_ID(),get_Trx());
			if (cash == null || cash.get_ID() == 0)
			{
				m_processMsg = "@NoCashBook@";
				return DocActionConstants.STATUS_Invalid;
			}
			MCashLine cl = new MCashLine (cash);
			cl.setInvoice(this);
			if (!cl.save(get_Trx()))
			{
				m_processMsg = "Could not save Cash Journal Line";
				return DocActionConstants.STATUS_Invalid;
			}
			info.append("@C_Cash_ID@: " + cash.getName() +  " #" + cl.getLine());
			setC_CashLine_ID(cl.getC_CashLine_ID());
		}	//	CashBook


		String sql = "UPDATE C_OrderLine OL "
				+ "SET QtyInvoiced = QtyInvoiced + COALESCE((SELECT SUM(QtyInvoiced) FROM C_InvoiceLine IL WHERE OL.C_OrderLine_ID = IL.C_OrderLine_ID AND C_Invoice_ID = ?), 0) "
				+ "WHERE OL.C_OrderLine_ID IS NOT NULL AND OL.C_OrderLine_ID IN (SELECT C_OrderLine_ID FROM C_InvoiceLine WHERE C_Invoice_ID = ?) AND OL.QtyInvoiced IS NOT NULL AND (? = 'Y' OR OL.M_Product_ID IS NULL)";
		if (0 > DB.executeUpdate(get_Trx(), sql,
				new Object[] { getC_Invoice_ID(), getC_Invoice_ID(), isSOTrx() ? "Y" : "N" })) {
			m_processMsg = "Could not update Order Line";
			return DocActionConstants.STATUS_Invalid;
		}
		
		sql = "UPDATE C_Order o"
			+ " SET IsInvoiced = CASE WHEN ("
				+ "SELECT COALESCE(SUM(QtyOrdered),0)-COALESCE(SUM(QtyInvoiced),0) "
				+ "FROM C_OrderLine ol WHERE ol.C_Order_ID=o.C_Order_ID) = 0 THEN 'Y' ELSE 'N' END "
			+ "WHERE C_Order_ID IN "
			+ "(SELECT C_Order_ID FROM C_InvoiceLine il INNER JOIN C_OrderLine ol ON (il.C_OrderLine_id=ol.C_OrderLine_ID) WHERE C_Invoice_ID = ?) "; 
		if(0 > DB.executeUpdate(get_Trx(), sql, new Object[] { getC_Invoice_ID()}) ){
			m_processMsg = "Could not update Order";
			return DocActionConstants.STATUS_Invalid;		
		}
		
		//	Update Order & Match
		MInvoiceLine[] lines = getLines();
		
		if (!isSOTrx() && !isReversal()) {
			ArrayList<MMatchPO> matchPOs = new ArrayList<MMatchPO>();
			for (MInvoiceLine line : lines) {
				// Update Order Line
				// Order Invoiced Qty updated via Matching Inv-PO
				if (line.getC_OrderLine_ID() != 0 && line.getM_Product_ID() != 0) {
					// MatchPO is created also from MInOut when Invoice exists
					// before Shipment
					BigDecimal matchQty = line.getQtyInvoiced();
					MMatchPO po = MMatchPO.create(line, null, getDateInvoiced(), matchQty);
					matchPOs.add(po);
				}
			}
			if (!PO.saveAll(get_Trx(), matchPOs)) {
				m_processMsg = "Could not create PO Matching";
				return DocActionConstants.STATUS_Invalid;
			}
			if (matchPOs.size() > 0)
				info.append(" @M_MatchPO_ID@#").append(matchPOs.size()).append(" ");

			
			ArrayList<MMatchInv> matchInvs = new ArrayList<MMatchInv>();
			for (MInvoiceLine line : lines) {
				// Matching - Inv-Shipment
				if (line.getM_InOutLine_ID() != 0 && line.getM_Product_ID() != 0) {
					MInOutLine receiptLine = new MInOutLine(getCtx(), line.getM_InOutLine_ID(), get_Trx());
					BigDecimal matchQty = line.getQtyInvoiced();

					if (receiptLine.getMovementQty().compareTo(matchQty) < 0)
						matchQty = receiptLine.getMovementQty();

					MMatchInv inv = new MMatchInv(line, getDateInvoiced(), matchQty);
					matchInvs.add(inv);
				}
			}
			if (!PO.saveAll(get_Trx(), matchInvs)) {
				m_processMsg = "Could not create Invoice Matching";
				return DocActionConstants.STATUS_Invalid;
			}
			if (matchInvs.size() > 0)
				info.append(" @M_MatchInv_ID@#").append(matchInvs.size()).append(" ");
		}
		
		//	Lead/Request
		for (MInvoiceLine line : lines)
		{
			line.createLeadRequest(this);
		}	//	for all lines



		//	Update BP Statistics
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_Trx());
		//	Update total revenue and balance / credit limit (reversed on AllocationLine.processIt)
		BigDecimal invAmt = MConversionRate.convertBase(getCtx(), getGrandTotal(true),	//	CM adjusted
				getC_Currency_ID(), getDateAcct(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
		if (invAmt == null)
		{
			m_processMsg = "Could not convert C_Currency_ID=" + getC_Currency_ID()
			+ " to base C_Currency_ID=" + MClient.get(getCtx()).getC_Currency_ID();
			return DocActionConstants.STATUS_Invalid;
		}
		//	Total Balance
		BigDecimal newBalance = bp.getTotalOpenBalance(false);
		if (newBalance == null)
			newBalance = Env.ZERO;
		if (isSOTrx())
		{
			newBalance = newBalance.add(invAmt);
			//
			if (bp.getFirstSale() == null)
				bp.setFirstSale(getDateInvoiced());
			BigDecimal newLifeAmt = bp.getActualLifeTimeValue();
			if (newLifeAmt == null)
				newLifeAmt = invAmt;
			else
				newLifeAmt = newLifeAmt.add(invAmt);
			BigDecimal newCreditAmt = bp.getSO_CreditUsed();
			if (newCreditAmt == null)
				newCreditAmt = invAmt;
			else
				newCreditAmt = newCreditAmt.add(invAmt);
			//
			log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
					+ ") BP Life=" + bp.getActualLifeTimeValue() + "->" + newLifeAmt
					+ ", Credit=" + bp.getSO_CreditUsed() + "->" + newCreditAmt
					+ ", Balance=" + bp.getTotalOpenBalance(false) + " -> " + newBalance);
			bp.setActualLifeTimeValue(newLifeAmt);
			bp.setSO_CreditUsed(newCreditAmt);
		}	//	SO
		else
		{
			newBalance = newBalance.subtract(invAmt);
			log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
					+ ") Balance=" + bp.getTotalOpenBalance(false) + " -> " + newBalance);
		}
		bp.setTotalOpenBalance(newBalance);
		bp.setSOCreditStatus();
		if (!bp.save(get_Trx()))
		{
			m_processMsg = "Could not update Business Partner";
			return DocActionConstants.STATUS_Invalid;
		}

		//	User - Last Result/Contact
		if (getAD_User_ID() != 0)
		{
			MUser user = new MUser (getCtx(), getAD_User_ID(), get_Trx());
			user.setLastContact(new Timestamp(System.currentTimeMillis()));
			user.setLastResult(Msg.translate(getCtx(), "C_Invoice_ID") + ": " + getDocumentNo());
			if (!user.save(get_Trx()))
			{
				m_processMsg = "Could not update Business Partner User";
				return DocActionConstants.STATUS_Invalid;
			}
		}	//	user

		//	Update Project
		if (isSOTrx() && getC_Project_ID() != 0)
		{
			MProject project = new MProject (getCtx(), getC_Project_ID(), get_Trx());
			BigDecimal amt = getGrandTotal(true);
			int C_CurrencyTo_ID = project.getC_Currency_ID();
			if (C_CurrencyTo_ID != getC_Currency_ID())
				amt = MConversionRate.convert(getCtx(), amt, getC_Currency_ID(), C_CurrencyTo_ID,
						getDateAcct(), 0, getAD_Client_ID(), getAD_Org_ID());
			if (amt == null)
			{
				m_processMsg = "Could not convert C_Currency_ID=" + getC_Currency_ID()
				+ " to Project C_Currency_ID=" + C_CurrencyTo_ID;
				return DocActionConstants.STATUS_Invalid;
			}
			BigDecimal newAmt = project.getInvoicedAmt();
			if (newAmt == null)
				newAmt = amt;
			else
				newAmt = newAmt.add(amt);
			log.fine("GrandTotal=" + getGrandTotal(true) + "(" + amt
					+ ") Project " + project.getName()
					+ " - Invoiced=" + project.getInvoicedAmt() + "->" + newAmt);
			project.setInvoicedAmt(newAmt);
			if (!project.save(get_Trx()))
			{
				m_processMsg = "Could not update Project";
				return DocActionConstants.STATUS_Invalid;
			}
		}	//	project

		//	Counter Documents
		MDocType docType = MDocType.get(getCtx(), getC_DocType_ID());
		if (docType.isCreateCounter()) {
			MInvoice counter = createCounterDoc();
			if (counter != null)
				info.append(" - @CounterDoc@: @C_Invoice_ID@=").append(
						counter.getDocumentNo());
		}

		m_processMsg = info.toString().trim();
		return DocActionConstants.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Create Counter Document
	 * 	@return counter invoice
	 */
	private MInvoice createCounterDoc()
	{
		//	Is this a counter doc ?
		if (getRef_Invoice_ID() != 0)
			return null;

		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_Trx()); //jz
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
		int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
		if (counterAD_Org_ID == 0)
			return null;

		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, null);
		//	MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID, null);
		log.info("Counter BP=" + counterBP.getName());

		//	Document Type
		int C_DocTypeTarget_ID = 0;
		MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
		if (counterDT != null)
		{
			log.fine(counterDT.toString());
			if (!counterDT.isCreateCounter() || !counterDT.isValid() || !counterDT.isActive())
				return null;
			C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
		}
		else	//	indirect
		{
			C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
			if (C_DocTypeTarget_ID <= 0)
				return null;

			counterDT = MDocTypeCounter.getCounterDocType(getCtx(), C_DocTypeTarget_ID);	
			if (counterDT != null) {
				log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
				if (!counterDT.isCreateCounter() || !counterDT.isValid()
						|| !counterDT.isActive())
					return null;
			} else
				return null;
		}

		//	Deep Copy
		//SR: 10022297 When creating couter doc, don't set the Order details.
		MInvoice counter = copyFrom(this, getDateInvoiced(),
				C_DocTypeTarget_ID, true, get_Trx(), false);
		MDocType dt = MDocType.get(getCtx(), C_DocTypeTarget_ID);
		if (!dt.isDocNoControlled())
			counter.setDocumentNo(getDocumentNo());		//	copy if manual
		//
		counter.setAD_Org_ID(counterAD_Org_ID);
		//	counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
		//
		counter.setBPartner(counterBP);
		//	Refernces (Should not be required
		counter.setSalesRep_ID(getSalesRep_ID());
		counter.save(get_Trx());

		//	Update copied lines
		MInvoiceLine[] counterLines = counter.getLines();
		for (MInvoiceLine counterLine : counterLines) {
			counterLine.setClientOrg(counter);
			counterLine.setInvoice(counter);	//	copies header values (BP, etc.)
			//SR: 10022504 - Do not set Price and Tax for counter doc
			//It gets updated from Price List which is not correct
			//The correct values are already copied into it.
			//counterLine.setPrice();
			//counterLine.setTax();			
			counterLine.save(get_Trx());
		}

		log.fine(counter.toString());

		//	Document Action
		if (counterDT != null)
		{
			if (counterDT.getDocAction() != null)
			{
				counter.setDocAction(counterDT.getDocAction());
				DocumentEngine.processIt(counter, counterDT.getDocAction());
				counter.setProcessing(false);
				counter.save(get_Trx());
			}
		}
		else
		{
			counter.setIsActive(counter.isActive());
			counter.save(get_Trx());
		}
		return counter;
	}	//	createCounterDoc

	/**
	 * 	Void Document.
	 * 	@return true if success
	 */
	public boolean voidIt()
	{
		log.info(toString());
		if (DOCSTATUS_Closed.equals(getDocStatus())
				|| DOCSTATUS_Reversed.equals(getDocStatus())
				|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
				|| DOCSTATUS_Invalid.equals(getDocStatus())
				|| DOCSTATUS_InProgress.equals(getDocStatus())
				|| DOCSTATUS_Approved.equals(getDocStatus())
				|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			//	Set lines to 0
			MInvoiceLine[] lines = getLines();
			for (MInvoiceLine line : lines) {
				BigDecimal old = line.getQtyInvoiced();
				if (old.compareTo(Env.ZERO) != 0)
				{
					line.setQty(Env.ZERO);
					line.setTaxAmt(Env.ZERO);
					line.setLineNetAmt(Env.ZERO);
					line.setLineTotalAmt(Env.ZERO);
					line.addDescription(Msg.getMsg(getCtx(), "Voided") + " (" + old + ")");
					//	Unlink Shipment
					if (line.getM_InOutLine_ID() != 0)
					{
						MInOutLine ioLine = new MInOutLine(getCtx(), line.getM_InOutLine_ID(), get_Trx());
						ioLine.setIsInvoiced(false);
						ioLine.save(get_Trx());
						line.setM_InOutLine_ID(0);
					}
					line.save(get_Trx());
				}
			}
			addDescription(Msg.getMsg(getCtx(), "Voided"));
			setIsPaid(true);
			setC_Payment_ID(0);
			recomputeTaxesAndTotals();
		}
		else
		{
			return DocumentEngine.processIt(this, DocActionConstants.ACTION_Reverse_Correct);
		}

		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info(toString());
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction - same date
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());
		//
		m_processMsg = DocumentEngine.isPeriodOpen(this);
		if (m_processMsg != null)
			return false;

		//	Don't touch allocation for cash as that is handled in CashJournal
		boolean isCash = PAYMENTRULE_Cash.equals(getPaymentRule());

		if (!isCash)
		{
			MAllocationHdr[] allocations = MAllocationHdr.getOfInvoice(getCtx(),
					getC_Invoice_ID(), get_Trx());
			for (MAllocationHdr alloc : allocations) {
				alloc.setDocAction(DocActionConstants.ACTION_Reverse_Correct);
				DocumentEngine.processIt(alloc, DocActionConstants.ACTION_Reverse_Correct);
				alloc.save(get_Trx());
			}
		}
		//	Reverse/Delete Matching
		if (!isSOTrx())
		{
			MMatchInv[] mInv = MMatchInv.getInvoice(getCtx(), getC_Invoice_ID(), get_Trx());
			for (MMatchInv element : mInv)
				element.delete(true);
			MMatchPO[] mPO = MMatchPO.getInvoice(getCtx(), getC_Invoice_ID(), get_Trx());
			for (MMatchPO element : mPO) {
				if (element.getM_InOutLine_ID() == 0)
					element.delete(true);
				else
				{
					element.setC_InvoiceLine_ID(null);
					element.save(get_Trx());
				}
			}
		}
		//
		load(get_Trx());	//	reload allocation reversal info

		//	Deep Copy
		MInvoice reversal = copyFrom (this, getDateInvoiced(),
				getC_DocType_ID(), false, get_Trx(), true);
		if (reversal == null)
		{
			m_processMsg = "Could not create Invoice Reversal";
			return false;
		}
		reversal.setReversal(true);

		//	Reverse Line Qty
		MInvoiceLine[] rLines = reversal.getLines();
		for (MInvoiceLine rLine : rLines) {
			rLine.setQtyEntered(rLine.getQtyEntered().negate());
			rLine.setQtyInvoiced(rLine.getQtyInvoiced().negate());
			rLine.setLineNetAmt(rLine.getLineNetAmt().negate());
			if (rLine.getTaxAmt() != null && rLine.getTaxAmt().compareTo(Env.ZERO) != 0)
				rLine.setTaxAmt(rLine.getTaxAmt().negate());
			if (rLine.getLineTotalAmt() != null && rLine.getLineTotalAmt().compareTo(Env.ZERO) != 0)
				rLine.setLineTotalAmt(rLine.getLineTotalAmt().negate());
			if (!rLine.save(get_Trx()))
			{
				m_processMsg = "Could not correct Invoice Reversal Line";
				return false;
			}
		}
		reversal.setC_Order_ID(getC_Order_ID());
		reversal.addDescription("(->" + getDocumentNo() + ")");
		//
		if (!DocumentEngine.processIt(reversal, DocActionConstants.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return false;
		}
		reversal.setC_Payment_ID(0);
		reversal.setIsPaid(true);
		DocumentEngine.processIt(reversal, DocActionConstants.ACTION_Close);
		reversal.setProcessing (false);
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.save(get_Trx());
		m_processMsg = reversal.getDocumentNo();
		//
		addDescription("(" + reversal.getDocumentNo() + "<-)");

		//	Clean up Reversed (this)
		MInvoiceLine[] iLines = getLines();
		for (MInvoiceLine iLine : iLines) {
			if (iLine.getM_InOutLine_ID() != 0)
			{
				MInOutLine ioLine = new MInOutLine(getCtx(), iLine.getM_InOutLine_ID(), get_Trx());
				ioLine.setIsInvoiced(false);
				ioLine.save(get_Trx());
				//	Reconciliation
				iLine.setM_InOutLine_ID(0);
				iLine.save(get_Trx());
			}
		}
		setProcessed(true);
		setDocStatus(DOCSTATUS_Reversed);	//	may come from void
		setDocAction(DOCACTION_None);
		setC_Payment_ID(0);
		setIsPaid(true);

		//	Create Allocation
		if (!isCash)
		{
			MAllocationHdr alloc = new MAllocationHdr(getCtx(), false, getDateAcct(),
					getC_Currency_ID(),
					Msg.translate(getCtx(), "C_Invoice_ID")	+ ": " + getDocumentNo() + "/" + reversal.getDocumentNo(),
					get_Trx());
			alloc.setAD_Org_ID(getAD_Org_ID());
			if (alloc.save())
			{
				//	Amount
				BigDecimal gt = getGrandTotal(true);
				if (!isSOTrx())
					gt = gt.negate();
				//	Orig Line
				MAllocationLine aLine = new MAllocationLine (alloc, gt,
						Env.ZERO, Env.ZERO, Env.ZERO);
				aLine.setAD_Org_ID(getAD_Org_ID());
				aLine.setC_Invoice_ID(getC_Invoice_ID());
				aLine.save();
				//	Reversal Line
				MAllocationLine rLine = new MAllocationLine (alloc, gt.negate(),
						Env.ZERO, Env.ZERO, Env.ZERO);
				rLine.setAD_Org_ID(reversal.getAD_Org_ID());
				rLine.setC_Invoice_ID(reversal.getC_Invoice_ID());
				rLine.save();
				//	Process It
				if (DocumentEngine.processIt(alloc, DocActionConstants.ACTION_Complete))
					alloc.save();
			}
		}	//	notCash

		//	Explicitly save for balance calc.
		save();
		//	Update BP Balance
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_Trx());
		bp.save();

		return true;
	}	//	reverseCorrectIt

	/**
	 * 	Reverse Accrual - none
	 * 	@return false
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		return false;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate
	 * 	@return false
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		return false;
	}	//	reActivateIt


	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Grand Total = 123.00 (#1)
		sb.append(": ").
		append(Msg.translate(getCtx(),"GrandTotal")).append("=").append(getGrandTotal())
		.append(" (#").append(getLines().length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg

	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getSalesRep_ID();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return getGrandTotal();
	}	//	getApprovalAmt

	/**
	 * 	Set Price List - Callout
	 *	@param oldM_PriceList_ID old value
	 *	@param newM_PriceList_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setM_PriceList_ID (String oldM_PriceList_ID,
			String newM_PriceList_ID, int windowNo) throws Exception
			{
		if (newM_PriceList_ID == null || newM_PriceList_ID.length() == 0)
			return;
		int M_PriceList_ID = Integer.parseInt(newM_PriceList_ID);
		if (M_PriceList_ID == 0)
			return;
		setM_PriceList_ID(M_PriceList_ID);

		setPriceListVersion(windowNo);
			}	//	setM_PriceList_ID




	/**
	 *
	 * @param oldC_PaymentTerm_ID
	 * @param newC_PaymentTerm_ID
	 * @param windowNo
	 * @throws Exception
	 */
	@UICallout
	public void setC_PaymentTerm_ID( String oldC_PaymentTerm_ID, String newC_PaymentTerm_ID, int windowNo ) throws Exception
	{
		if( newC_PaymentTerm_ID == null || newC_PaymentTerm_ID.length() == 0 )
			return;
		int C_PaymentTerm_ID = Integer.parseInt( newC_PaymentTerm_ID );
		int C_Invoice_ID = getC_Invoice_ID();
		if ( C_PaymentTerm_ID == 0 || C_Invoice_ID == 0)	//	not saved yet
			return;
		//
		MPaymentTerm pt = new MPaymentTerm (getCtx(), C_PaymentTerm_ID, null);
		if (pt.get_ID() == 0)
		{
			addError( Msg.getMsg( getCtx(), "PaymentTerm not found" ) );
		}

		boolean valid = pt.apply (C_Invoice_ID);
		setIsPayScheduleValid( valid );

		return;

	}



	/**
	 *	Invoice Header - DocType.
	 *		- PaymentRule
	 *		- temporary Document
	 *  Context:
	 *  	- DocSubTypeSO
	 *		- HasCharges
	 *	- (re-sets Business Partner info of required)
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */

	@UICallout public void setC_DocTypeTarget_ID (String oldC_DocTypeTarget_ID,
			String newC_DocTypeTarget_ID, int WindowNo) throws Exception
			{
		if (Util.isEmpty(newC_DocTypeTarget_ID))
			return;
		int C_DocTypeTarget_ID = convertToInt(newC_DocTypeTarget_ID);
		if (C_DocTypeTarget_ID == 0)
			return;

		//	Re-Create new DocNo, if there is a doc number already
		//	and the existing source used a different Sequence number
		String oldDocNo = getDocumentNo();
		boolean newDocNo = oldDocNo == null;
		if (!newDocNo && oldDocNo.startsWith("<") && oldDocNo.endsWith(">"))
			newDocNo = true;
		int oldC_DocType_ID = getC_DocType_ID();
		if (oldC_DocType_ID == 0 && !Util.isEmpty(oldC_DocTypeTarget_ID))
			oldC_DocType_ID = convertToInt(oldC_DocTypeTarget_ID);

		String sql = "SELECT d.HasCharges,'N',d.IsDocNoControlled,"
			+ "s.CurrentNext, d.DocBaseType, s.CurrentNextSys, s.AD_Sequence_ID "
			+ "FROM C_DocType d "
			+ "LEFT OUTER JOIN AD_Sequence s ON (d.DocNoSequence_ID=s.AD_Sequence_ID) "
			+ "WHERE C_DocType_ID=?";		//	1
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int AD_Sequence_ID = 0;

			//	Get old AD_SeqNo for comparison
			if (!newDocNo && oldC_DocType_ID != 0)
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, oldC_DocType_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
					AD_Sequence_ID = rs.getInt(7);
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
			}

			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_DocTypeTarget_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Charges - Set Context
				setContext( WindowNo, "HasCharges", rs.getString(1));
				//	DocumentNo
				if (rs.getString(3).equals("Y"))			//	IsDocNoControlled
				{
					if (!newDocNo && AD_Sequence_ID != rs.getInt(7))
						newDocNo = true;
					if (newDocNo)
						if (Ini.isPropertyBool(Ini.P_COMPIERESYS)
								&& getCtx().getAD_Client_ID() < 1000000)
							setDocumentNo("<" + rs.getString(6) + ">");
						else
							setDocumentNo("<" + rs.getString(4) + ">");
				}
				//  DocBaseType - Set Context
				String s = rs.getString(5);
				setContext( WindowNo, "DocBaseType", s);

				MBPartner bpartner = null;
				if(getC_BPartner_ID() > 0) 
					bpartner = new MBPartner(getCtx(), getC_BPartner_ID(), null);
				if (getC_BPartner_ID() == 0 || (bpartner!=null && isSOTrx() && bpartner.getPaymentRule()==null)
						|| (bpartner!=null && !isSOTrx() && bpartner.getPaymentRulePO() == null)) {
					// AP Check & AR Credit Memo
					if (s.startsWith("AP"))
						setPaymentRule("S"); // Check
					else if (s.endsWith("C"))
						setPaymentRule("P"); // OnCredit
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return;
	}	//	docType



	/**
	 *	Invoice Header- BPartner.
	 *		- M_PriceList_ID (+ Context)
	 *		- C_BPartner_Location_ID
	 *		- AD_User_ID
	 *		- POReference
	 *		- SO_Description
	 *		- IsDiscountPrinted
	 *		- PaymentRule
	 *		- C_PaymentTerm_ID
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	@UICallout public void setC_BPartner_ID (String oldC_BPartner_ID,
			String newC_BPartner_ID, int WindowNo) throws Exception
			{
		if( newC_BPartner_ID == null || newC_BPartner_ID.length() == 0 )
			return;
		Integer C_BPartner_ID = Integer.parseInt( newC_BPartner_ID );
		if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
			return;

		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
			+ " COALESCE(p.M_PriceList_ID,g.M_PriceList_ID) AS M_PriceList_ID, p.PaymentRule,p.POReference,"
			+ " p.SO_Description,p.IsDiscountPrinted,"
			+ " p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ " l.C_BPartner_Location_ID,c.AD_User_ID,"
			+ " COALESCE(p.PO_PriceList_ID,g.PO_PriceList_ID) AS PO_PriceList_ID, p.PaymentRulePO,p.PO_PaymentTerm_ID "
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID)"
			+ " LEFT OUTER JOIN C_BPartner_Location l ON (p.C_BPartner_ID=l.C_BPartner_ID AND l.IsBillTo='Y' AND l.IsActive='Y')"
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID) "
			+ " WHERE p.C_BPartner_ID=? AND p.IsActive='Y'"		//	#1
			+ " ORDER BY l.Name ASC";

		boolean IsSOTrx = isSOTrx();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_BPartner_ID.intValue());
			rs = pstmt.executeQuery();
			//
			if (rs.next())
			{
				//	PriceList & IsTaxIncluded & Currency
				Integer ii = Integer.valueOf(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
				if (!rs.wasNull())
					setM_PriceList_ID( ii);
				else
				{	//	get default PriceList
					int i = getCtx().getContextAsInt( "#M_PriceList_ID");
					if (i != 0)
						setM_PriceList_ID( Integer.valueOf(i));
				}

				//	PaymentRule
				String s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
				if (s != null && s.length() != 0)
				{
					if (getCtx().getContext( WindowNo, "DocBaseType").endsWith("C"))	//	Credits are Payment Term
						s = "P";
					else if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
						s = "P";											//  Payment Term
					setPaymentRule( s);
				}
				//  Payment Term
				ii = Integer.valueOf(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
				if (!rs.wasNull())
					setC_PaymentTerm_ID( ii);

				//	Location
				int locID = rs.getInt("C_BPartner_Location_ID");
				//	overwritten by InfoBP selection - works only if InfoWindow
				//	was used otherwise creates error (uses last value, may belong to differnt BP)
				if (C_BPartner_ID.toString().equals(getCtx().getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID")))
				{
					String loc = getCtx().getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_Location_ID");
					if (loc.length() > 0)
						locID = Integer.parseInt(loc);
				}
				if(locID == 0)
					p_changeVO.addChangedValue("C_BPartner_Location_ID", (String)null);
				else
					setC_BPartner_Location_ID(locID);

				//	Contact - overwritten by InfoBP selection
				int contID = rs.getInt("AD_User_ID");
				if (C_BPartner_ID.toString().equals(getCtx().getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID")))
				{
					String cont = getCtx().getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contID = Integer.parseInt(cont);
				}
				setAD_User_ID( contID );

				//	CreditAvailable
				if (IsSOTrx)
				{
					double CreditLimit = rs.getDouble("SO_CreditLimit");
					if (CreditLimit != 0)
					{
						double CreditAvailable = rs.getDouble("CreditAvailable");
						if (!rs.wasNull() && CreditAvailable < 0)
						{
							String msg = Msg.getMsg(getCtx(), "CreditLimitOver",
									DisplayType.getNumberFormat(DisplayTypeConstants.Amount).format(CreditAvailable));
							addError(msg);
						}
					}
				}

				//	PO Reference
				s = rs.getString("POReference");
				if (s != null && s.length() != 0)
					setPOReference( s);
				else
					setPOReference( null);
				//	SO Description
				s = rs.getString("SO_Description");
				if (s != null && s.trim().length() != 0)
					setDescription( s);
				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				setIsDiscountPrinted("Y".equals(s));
				//
				setC_Project_ID(0);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "bPartner", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	bPartner


	/**
	 * 	Set DateInvoiced - Callout
	 *	@param oldDateInvoiced old
	 *	@param newDateInvoiced new
	 *	@param windowNo window no
	 */
	@UICallout public void setDateInvoiced (String oldDateInvoiced ,
			String newDateInvoiced , int windowNo) throws Exception
			{
		if (newDateInvoiced == null || newDateInvoiced.length() == 0)
			return;
		Timestamp dateInvoiced = PO.convertToTimestamp(newDateInvoiced );
		if (dateInvoiced == null)
			return;
		setDateInvoiced (dateInvoiced );
		setPriceListVersion(windowNo);
			}	//	setDateOrdered

	public void setPriceListVersion(int windowNo)
	{
		int M_PriceList_ID = getM_PriceList_ID();
		if (M_PriceList_ID == 0)
			return;

		Timestamp invoiceDate = getDateInvoiced();
		if(invoiceDate == null)
			return;

		String sql = "SELECT pl.IsTaxIncluded,pl.EnforcePriceLimit,pl.C_Currency_ID,c.StdPrecision,"
			+ "plv.M_PriceList_Version_ID,plv.ValidFrom "
			+ "FROM M_PriceList pl,C_Currency c,M_PriceList_Version plv "
			+ "WHERE pl.C_Currency_ID=c.C_Currency_ID"
			+ " AND pl.M_PriceList_ID=plv.M_PriceList_ID"
			+ " AND pl.M_PriceList_ID=? "						//	1
			+ " AND plv.ValidFrom <=? "							//  2
			+ " AND plv.IsActive='Y' "
			+ "ORDER BY plv.ValidFrom DESC";
		//	Use newest price list - may not be future
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, M_PriceList_ID);
			pstmt.setTimestamp(2, invoiceDate);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Tax Included
				setIsTaxIncluded("Y".equals(rs.getString(1)));
				//	Price Limit Enforce
				if (p_changeVO != null)
					p_changeVO.setContext(getCtx(), windowNo, "EnforcePriceLimit", rs.getString(2));
				//	Currency
				Integer ii = Integer.valueOf(rs.getInt(3));
				setC_Currency_ID(ii);
				//	PriceList Version
				if (p_changeVO != null)
					p_changeVO.setContext(getCtx(), windowNo, "M_PriceList_Version_ID", rs.getInt(5));
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void setProcessMsg(String processMsg) {
		m_processMsg = processMsg;
	}

	@Override
	public String getDocBaseType() {
		MDocType dt = MDocType.get(getCtx(), getC_DocTypeTarget_ID());
		return dt.getDocBaseType();
	}

	@Override
	public Timestamp getDocumentDate() {
		return getDateAcct();
	}

	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return new QueryParams("SELECT DISTINCT AD_Org_ID FROM C_InvoiceLine WHERE C_Invoice_ID = ?",
				new Object[] { getC_Invoice_ID() });
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass() != o.getClass())
			return false;
		MInvoice that = (MInvoice)o;
		if(this.getC_Invoice_ID() == 0 || that.getC_Invoice_ID() ==0)
			return false;
		super.equals(o);
		return true;
	}

}	//	MInvoice
