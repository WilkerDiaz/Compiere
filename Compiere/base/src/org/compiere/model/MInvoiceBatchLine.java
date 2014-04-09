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

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;


/**
 *	Invoice Batch Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInvoiceBatchLine.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MInvoiceBatchLine extends X_C_InvoiceBatchLine
{
    /** Logger for class MInvoiceBatchLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInvoiceBatchLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_InvoiceBatchLine_ID id
	 *	@param trx p_trx
	 */
	public MInvoiceBatchLine (Ctx ctx, int C_InvoiceBatchLine_ID,
		Trx trx)
	{
		super (ctx, C_InvoiceBatchLine_ID, trx);
		if (C_InvoiceBatchLine_ID == 0)
		{
		//	setC_InvoiceBatch_ID (0);
			/**
			setC_BPartner_ID (0);
			setC_BPartner_Location_ID (0);
			setC_Charge_ID (0);
			setC_DocType_ID (0);	// @C_DocType_ID@
			setC_Tax_ID (0);
			setDocumentNo (null);
			setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_InvoiceBatchLine WHERE C_InvoiceBatch_ID=@C_InvoiceBatch_ID@
			**/
			setDateAcct (new Timestamp(System.currentTimeMillis()));	// @DateDoc@
			setDateInvoiced (new Timestamp(System.currentTimeMillis()));	// @DateDoc@
			setIsTaxIncluded (false);
			setLineNetAmt (Env.ZERO);
			setLineTotalAmt (Env.ZERO);
			setPriceEntered (Env.ZERO);
			setQtyEntered (Env.ONE);	// 1
			setTaxAmt (Env.ZERO);
			setProcessed (false);
		}
	}	//	MInvoiceBatchLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MInvoiceBatchLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MInvoiceBatchLine
	
	
	/**
	 * 	Set Document Type - Callout.
	 * 	@param oldC_DocType_ID old ID
	 * 	@param newC_DocType_ID new ID
	 * 	@param windowNo window
	 */
	@UICallout public void setC_DocType_ID (String oldC_DocType_ID, 
			String newC_DocType_ID, int windowNo) throws Exception
	{
		if (newC_DocType_ID == null || newC_DocType_ID.length() == 0)
			return;
		int C_DocType_ID = Integer.parseInt(newC_DocType_ID);
		setC_DocType_ID(C_DocType_ID);
		setDocumentNo();
	}	//	setC_DocType_ID
	
	/**
	 * 	Set DateInvoiced - Callout
	 *	@param oldDateInvoiced old
	 *	@param newDateInvoiced new
	 *	@param windowNo window no
	 */
	@UICallout public void setDateInvoiced (String oldDateInvoiced, 
			String newDateInvoiced, int windowNo) throws Exception
	{
		if (newDateInvoiced == null || newDateInvoiced.length() == 0)
			return;
		Timestamp dateInvoiced = PO.convertToTimestamp(newDateInvoiced);
		if (dateInvoiced == null)
			return;
		setDateInvoiced(dateInvoiced);
		setDocumentNo();
	}	//	setDateInvoiced
	
	/**
	 *	Set Date Invoiced and Acct Date
	 */
	@Override
	public void setDateInvoiced(Timestamp dateOrdered)
	{
		super.setDateInvoiced(dateOrdered);
		super.setDateAcct(dateOrdered);
	}	//	setDateInvoiced

	/**
	 *	Set Document No (increase existing)
	 */
	private void setDocumentNo()
	{
		//	Get last line
		int C_InvoiceBatch_ID = getC_InvoiceBatch_ID();
		String sql = "SELECT COALESCE(MAX(C_InvoiceBatchLine_ID),0) FROM C_InvoiceBatchLine WHERE C_InvoiceBatch_ID=?";
		int C_InvoiceBatchLine_ID = QueryUtil.getSQLValue(get_Trx(), sql, C_InvoiceBatch_ID);
		if (C_InvoiceBatchLine_ID == 0)
			return;
		MInvoiceBatchLine last = new MInvoiceBatchLine(Env.getCtx(), C_InvoiceBatchLine_ID, null);
		
		//	Need to Increase when different DocType or BP
		int C_DocType_ID = getC_DocType_ID();
		int C_BPartner_ID = getC_BPartner_ID();
		if (C_DocType_ID == last.getC_DocType_ID()
			&& C_BPartner_ID == last.getC_BPartner_ID())
			return;

		//	New Number
		String oldDocNo = last.getDocumentNo();
		if (oldDocNo == null)
			return;
		int docNo = 0;
		try
		{
			docNo = Integer.parseInt(oldDocNo);
		}
		catch (Exception e)
		{
		}
		if (docNo == 0)
			return;
		String newDocNo = String.valueOf(docNo+1);
		setDocumentNo(newDocNo);
	}	//	setDocumentNo

	/**
	 * 	Set Business Partner - Callout
	 *	@param oldC_BPartner_ID old BP
	 *	@param newC_BPartner_ID new BP
	 *	@param windowNo window no
	 */
	@UICallout public void setC_BPartner_ID (String oldC_BPartner_ID, 
			String newC_BPartner_ID, int windowNo) throws Exception
	{
		if (newC_BPartner_ID == null || newC_BPartner_ID.length() == 0)
			return;
		int C_BPartner_ID = Integer.parseInt(newC_BPartner_ID);
		if (C_BPartner_ID == 0)
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
			+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'";		//	#1

		boolean IsSOTrx = getCtx().isSOTrx(windowNo);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_BPartner_ID);
			rs = pstmt.executeQuery();
			//
			if (rs.next())
			{
				//	Location
				int C_BPartner_Location_ID = rs.getInt("C_BPartner_Location_ID");
				//	overwritten by InfoBP selection - works only if InfoWindow
				//	was used otherwise creates error (uses last value, may belong to differnt BP)
				if (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID") == C_BPartner_ID)
					C_BPartner_Location_ID = getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_Location_ID");
				if (C_BPartner_Location_ID != 0)
					setC_BPartner_Location_ID(C_BPartner_Location_ID);
				//	Contact - overwritten by InfoBP selection
				int AD_User_ID = rs.getInt("AD_User_ID");
				if (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID") == C_BPartner_ID)
					AD_User_ID = getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "AD_User_ID");
				setAD_User_ID(AD_User_ID);

				//	CreditAvailable
				if (IsSOTrx)
				{
					BigDecimal CreditLimit = rs.getBigDecimal("SO_CreditLimit");
				//	String SOCreditStatus = rs.getString("SOCreditStatus");
					if (CreditLimit != null && CreditLimit.signum() != 0)
					{
						BigDecimal CreditAvailable = rs.getBigDecimal("CreditAvailable");
						if (p_changeVO != null 
							&& CreditAvailable != null && CreditAvailable.signum() < 0)
						{
							String msg = Msg.getMsg(getCtx(), "CreditLimitOver", 
								DisplayType.getNumberFormat(DisplayTypeConstants.Amount).format(CreditAvailable));
							p_changeVO.addError(msg);
						}
					}
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
		//
		setDocumentNo();
		setTax (windowNo, "C_BPartner_ID");
	}	//	setC_BPartner_ID
	
	/**
	 * 	Set Partner Location - Callout
	 *	@param oldC_BPartner_Location_ID old value
	 *	@param newC_BPartner_Location_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setC_BPartner_Location_ID (String oldC_BPartner_Location_ID, 
			String newC_BPartner_Location_ID, int windowNo) throws Exception
	{
		if (newC_BPartner_Location_ID == null || newC_BPartner_Location_ID.length() == 0)
			return;
		int C_BPartner_Location_ID = Integer.parseInt(newC_BPartner_Location_ID);
		if (C_BPartner_Location_ID == 0)
			return;
		//
		super.setC_BPartner_Location_ID(C_BPartner_Location_ID);
		setTax(windowNo, "C_BPartner_Location_ID");
	}	//	setC_BPartner_Location_ID

	/**
	 * 	Set Charge - Callout
	 *	@param oldC_Charge_ID old value
	 *	@param newC_Charge_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setC_Charge_ID (String oldC_Charge_ID, 
			String newC_Charge_ID, int windowNo) throws Exception
	{
		if (newC_Charge_ID == null || newC_Charge_ID.length() == 0)
			return;
		int C_Charge_ID = Integer.parseInt(newC_Charge_ID);
		super.setC_Charge_ID(C_Charge_ID);
		
		MCharge charge = MCharge.get(getCtx(), C_Charge_ID);
		setPriceEntered(charge.getChargeAmt());
		setTax(windowNo, "C_Charge_ID");
	}	//	setC_Charge_ID

	/**
	 * 	Calculate Tax
	 */
	private void setTax(int windowNo, String columnName)
	{
		int C_Charge_ID = getC_Charge_ID();
		log.fine("C_Charge_ID=" + C_Charge_ID);
		if (C_Charge_ID == 0)
		{
			setAmt (windowNo, columnName);
			return;
		}

		//	Check Partner Location
		int C_BPartner_Location_ID = getC_BPartner_Location_ID();
		log.fine("BP_Location=" + C_BPartner_Location_ID);
		if (C_BPartner_Location_ID == 0)
		{
			setAmt (windowNo, columnName);
			return;
		}

		//	Dates
		Timestamp billDate = getDateInvoiced();
		log.fine("Bill Date=" + billDate);
		Timestamp shipDate = billDate;
		log.fine("Ship Date=" + shipDate);

		int AD_Org_ID = getAD_Org_ID();
		log.fine("Org=" + AD_Org_ID);
		MOrg org = MOrg.get(getCtx(), AD_Org_ID);
		int M_Warehouse_ID = org.getM_Warehouse_ID();
		log.fine("Warehouse=" + M_Warehouse_ID);

		boolean isSOTrx = getCtx().isSOTrx(windowNo);
		//
		int C_Tax_ID = Tax.get(getCtx(), 0, C_Charge_ID, billDate, shipDate,
			AD_Org_ID, M_Warehouse_ID, C_BPartner_Location_ID, C_BPartner_Location_ID,
			isSOTrx);
		log.info("Tax ID=" + C_Tax_ID + " - SOTrx=" + isSOTrx);

		if (C_Tax_ID == 0)
		{
			ValueNamePair pp = CLogger.retrieveError();
			if (pp != null)
				p_changeVO.addError(pp.getValue());
			else
				p_changeVO.addError("Tax Error");
		}
		else
			super.setC_Tax_ID(C_Tax_ID);
		//
		setAmt (windowNo, columnName);
	}	//	setTax
	
	/**
	 * 	Set Tax - Callout
	 *	@param oldC_Tax_ID old value
	 *	@param newC_Tax_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setC_Tax_ID (String oldC_Tax_ID, 
			String newC_Tax_ID, int windowNo) throws Exception
	{
		if (newC_Tax_ID == null || newC_Tax_ID.length() == 0)
			return;
		int C_Tax_ID = Integer.parseInt(newC_Tax_ID);
		setC_Tax_ID(C_Tax_ID);
		setAmt(windowNo, "C_Tax_ID");
	}	//	setC_Tax_ID

	/**
	 * 	Set Tax Included - Callout
	 *	@param oldIsTaxIncluded old value
	 *	@param newIsTaxIncluded new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setIsTaxIncluded (String oldIsTaxIncluded, 
			String newIsTaxIncluded, int windowNo) throws Exception
	{
		boolean IsTaxIncluded = "Y".equals(newIsTaxIncluded);
		setIsTaxIncluded(IsTaxIncluded);
		setAmt(windowNo, "IsTaxIncluded");
	}	//	setIsTaxIncluded

	/**
	 * 	Set PriceEntered - Callout
	 *	@param oldPriceEntered old value
	 *	@param newPriceEntered new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setPriceEntered (String oldPriceEntered, 
			String newPriceEntered, int windowNo) throws Exception
	{
		if (newPriceEntered == null || newPriceEntered.length() == 0)
			return;
		BigDecimal PriceEntered = new BigDecimal(newPriceEntered);
		super.setPriceEntered(PriceEntered);
		setAmt(windowNo, "PriceEntered");
	}	//	setPriceEntered

	/**
	 * 	Set QtyEntered - Callout
	 *	@param oldQtyEntered old value
	 *	@param newQtyEntered new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setQtyEntered (String oldQtyEntered, 
			String newQtyEntered, int windowNo) throws Exception
	{
		if (newQtyEntered == null || newQtyEntered.length() == 0)
			return;
		BigDecimal QtyEntered = new BigDecimal(newQtyEntered);
		super.setQtyEntered(QtyEntered);
		setAmt(windowNo, "QtyEntered");
	}	//	setQtyEntered

	/**
	 * 	Set Amount (Callout)
	 *	@param windowNo window
	 *	@param columnName changed column
	 */
	private void setAmt(int windowNo, String columnName)
	{
		//	get values
		BigDecimal QtyEntered = getQtyEntered();
		BigDecimal PriceEntered = getPriceEntered();
		log.fine("QtyEntered=" + QtyEntered + ", PriceEntered=" + PriceEntered);
		if (QtyEntered == null)
			QtyEntered = Env.ZERO;
		if (PriceEntered == null)
			PriceEntered = Env.ZERO;

		//	Line Net Amt
		BigDecimal LineNetAmt = QtyEntered.multiply(PriceEntered);
		int StdPrecision = getCtx().getStdPrecision();
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);

		//	Calculate Tax Amount
		boolean IsTaxIncluded = isTaxIncluded();
		BigDecimal TaxAmt = null;
		if (columnName.equals("TaxAmt"))
		{
			TaxAmt = getTaxAmt();
		}
		else
		{
			int C_Tax_ID = getC_Tax_ID();
			if (C_Tax_ID != 0)
			{
				MTax tax = new MTax (getCtx(), C_Tax_ID, null);
				TaxAmt = tax.calculateTax(LineNetAmt, IsTaxIncluded, StdPrecision);
				setTaxAmt(TaxAmt);
			}
		}
		
		//	
		if (IsTaxIncluded)
		{
			setLineTotalAmt(LineNetAmt);
			setLineNetAmt(LineNetAmt.subtract(TaxAmt));
		}
		else
		{
			setLineNetAmt(LineNetAmt);
			setLineTotalAmt(LineNetAmt.add(TaxAmt));
		}
	}	//	setAmt
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		// Amount
		if (getPriceEntered().signum() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "PriceEntered"));
			return false;
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Update Header
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (success)
		{
			String sql = "UPDATE C_InvoiceBatch h "
				+ "SET DocumentAmt = NVL((SELECT SUM(LineTotalAmt) FROM C_InvoiceBatchLine l "
					+ "WHERE h.C_InvoiceBatch_ID=l.C_InvoiceBatch_ID AND l.IsActive='Y'),0) "
				+ "WHERE C_InvoiceBatch_ID= ? ";
			DB.executeUpdate(get_Trx(), sql,getC_InvoiceBatch_ID());
		}
		return success;
	}	//	afterSave
	
}	//	MInvoiceBatchLine
