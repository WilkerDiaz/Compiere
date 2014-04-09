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

import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 *	Invoice Callouts
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutInvoice.java,v 1.4 2006/07/30 00:51:03 jjanke Exp $
 */
public class CalloutInvoice extends CalloutEngine
{
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());


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
	public String docType (Ctx ctx, int WindowNo, GridTab mTab, GridField mField,
			Object value, Object oldValue)
	{
		Integer C_DocType_ID = (Integer)value;
		if (C_DocType_ID == null || C_DocType_ID.intValue() == 0)
			return "";

		//	Re-Create new DocNo, if there is a doc number already
		//	and the existing source used a different Sequence number
		String oldDocNo = (String)mTab.getValue("DocumentNo");
		boolean newDocNo = oldDocNo == null;
		if (!newDocNo && oldDocNo.startsWith("<") && oldDocNo.endsWith(">"))
			newDocNo = true;
		Integer oldC_DocType_ID = (Integer)mTab.getValue("C_DocType_ID");
		if ((oldC_DocType_ID == null || oldC_DocType_ID.intValue() == 0)
				&& oldValue != null)	//	get old Target
			oldC_DocType_ID = (Integer)oldValue;

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
			if (oldC_DocType_ID.intValue() != 0)
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(1, oldC_DocType_ID.intValue());
				rs = pstmt.executeQuery();
				if (rs.next())
					AD_Sequence_ID = rs.getInt(7);
				rs.close();
				pstmt.close();
			}


			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_DocType_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Charges - Set Context
				ctx.setContext( WindowNo, "HasCharges", rs.getString(1));
				//	DocumentNo
				if (rs.getString(3).equals("Y"))			//	IsDocNoControlled
				{
					if (AD_Sequence_ID != rs.getInt(7))
						newDocNo = true;
					if (newDocNo)
						if (Ini.isPropertyBool(Ini.P_COMPIERESYS) && Env.getCtx().getAD_Client_ID() < 1000000)
							mTab.setValue("DocumentNo", "<" + rs.getString(6) + ">");
						else
							mTab.setValue("DocumentNo", "<" + rs.getString(4) + ">");
				}

				//  DocBaseType - Set Context
				String s = rs.getString(5);
				ctx.setContext( WindowNo, "DocBaseType", s);

				Integer C_BPartner_ID = (Integer) mTab.getValue("C_BPartner_ID");
				MBPartner bpartner = null;
				if(C_BPartner_ID!=null && C_BPartner_ID.intValue() > 0)
					bpartner = new MBPartner(ctx, C_BPartner_ID, null);
				boolean IsSOTrx = ctx.isSOTrx(WindowNo);
				if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0 || (bpartner !=null && IsSOTrx && bpartner.getPaymentRule()==null)
						|| (bpartner!=null && !IsSOTrx && bpartner.getPaymentRulePO()==null)) {
					// AP Check & AR Credit Memo
					if (s.startsWith("AP"))
						mTab.setValue("PaymentRule", "S"); // Check
					else if (s.endsWith("C"))
						mTab.setValue("PaymentRule", "P"); // OnCredit
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "";
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
	public String bPartner (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_BPartner_ID = (Integer)value;
		if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
			return "";

		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
			+ " COALESCE(p.M_PriceList_ID,g.M_PriceList_ID) AS M_PriceList_ID, p.PaymentRule,p.POReference,"
			+ " p.SO_Description,p.IsDiscountPrinted,"
			+ " p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ " l.C_BPartner_Location_ID,c.AD_User_ID,"
			+ " COALESCE(p.PO_PriceList_ID,g.PO_PriceList_ID) AS PO_PriceList_ID, p.PaymentRulePO,p.PO_PaymentTerm_ID "
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID)"
			+ " LEFT OUTER JOIN C_BPartner_Location l ON (p.C_BPartner_ID=l.C_BPartner_ID AND l.IsBillTo='Y' AND l.IsActive='Y')"
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID AND c.IsActive='Y') "
			+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'"		//	#1
			+ " ORDER BY l.Name  ASC ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean IsSOTrx = ctx.isSOTrx(WindowNo);
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID.intValue());
			rs = pstmt.executeQuery();
			//
			if (rs.next())
			{
				//	PriceList & IsTaxIncluded & Currency
				Integer ii = Integer.valueOf(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
				if (!rs.wasNull())
					mTab.setValue("M_PriceList_ID", ii);
				else
				{	//	get default PriceList
					int i = ctx.getContextAsInt( "#M_PriceList_ID");
					if (i != 0)
						mTab.setValue("M_PriceList_ID", Integer.valueOf(i));
				}

				//	PaymentRule
				String s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
				if (s != null && s.length() != 0)
				{
					if (ctx.getContext( WindowNo, "DocBaseType").endsWith("C"))	//	Credits are Payment Term
						s = "P";
					else if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
						s = "P";											//  Payment Term
					mTab.setValue("PaymentRule", s);
				}
				//  Payment Term
				ii = Integer.valueOf(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
				if (!rs.wasNull())
					mTab.setValue("C_PaymentTerm_ID", ii);

				//	Location
				int locID = rs.getInt("C_BPartner_Location_ID");
				//	overwritten by InfoBP selection - works only if InfoWindow
				//	was used otherwise creates error (uses last value, may belong to differnt BP)
				if (C_BPartner_ID.toString().equals(ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID")))
				{
					String loc = ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_Location_ID");
					if (loc.length() > 0)
						locID = Integer.parseInt(loc);
				}
				if (locID == 0)
					mTab.setValue("C_BPartner_Location_ID", null);
				else
					mTab.setValue("C_BPartner_Location_ID", Integer.valueOf(locID));

				//	Contact - overwritten by InfoBP selection
				int contID = rs.getInt("AD_User_ID");
				if (C_BPartner_ID.toString().equals(ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID")))
				{
					String cont = ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contID = Integer.parseInt(cont);
				}
				if (contID == 0)
					mTab.setValue("AD_User_ID", null);
				else
					mTab.setValue("AD_User_ID", Integer.valueOf(contID));

				//	CreditAvailable
				if (IsSOTrx)
				{
					double CreditLimit = rs.getDouble("SO_CreditLimit");
					if (CreditLimit != 0)
					{
						double CreditAvailable = rs.getDouble("CreditAvailable");
						if (!rs.wasNull() && CreditAvailable < 0)
							mTab.fireDataStatusEEvent("CreditLimitOver",
									DisplayType.getNumberFormat(DisplayTypeConstants.Amount).format(CreditAvailable),
									false);
					}
				}

				//	PO Reference
				s = rs.getString("POReference");
				if (s != null && s.length() != 0)
					mTab.setValue("POReference", s);
				else
					mTab.setValue("POReference", null);
				//	SO Description
				s = rs.getString("SO_Description");
				if (s != null && s.trim().length() != 0)
					mTab.setValue("Description", s);
				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				if (s != null && s.length() != 0)
					mTab.setValue("IsDiscountPrinted", s);
				else
					mTab.setValue("IsDiscountPrinted", "N");
				//
				mTab.setValue("C_Project_ID", null);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "bPartner", e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return "";
	}	//	bPartner

	/**
	 *	Set Payment Term.
	 *	Payment Term has changed
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String paymentTerm (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_PaymentTerm_ID = (Integer)value;
		int C_Invoice_ID = ctx.getContextAsInt( WindowNo, "C_Invoice_ID");
		if (C_PaymentTerm_ID == null || C_PaymentTerm_ID.intValue() == 0
				|| C_Invoice_ID == 0)	//	not saved yet
			return "";
		//
		MPaymentTerm pt = new MPaymentTerm (ctx, C_PaymentTerm_ID.intValue(), null);
		if (pt.get_ID() == 0)
			return "PaymentTerm not found";

		boolean valid = pt.apply (C_Invoice_ID);
		mTab.setValue("IsPayScheduleValid", valid ? "Y" : "N");

		return "";
	}	//	paymentTerm


	public String dateInvoiced (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		if (value == null || !(value instanceof Timestamp))
			return "";
		mTab.setValue("DateAcct", value);

		return setPriceListVersion(ctx, WindowNo, mTab);
	}	//	dateAcct

	private String setPriceListVersion(Ctx ctx, int WindowNo, GridTab mTab)
	{
		Integer M_PriceList_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_ID");
		if (M_PriceList_ID == null || M_PriceList_ID.intValue()== 0)
			return "";

		Timestamp invoiceDate = new Timestamp(ctx.getContextAsTime( WindowNo, "DateInvoiced"));
		if (invoiceDate == null)
			return "";

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
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, M_PriceList_ID.intValue());
			pstmt.setTimestamp(2, invoiceDate);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Tax Included
				mTab.setValue("IsTaxIncluded", Boolean.valueOf("Y".equals(rs.getString(1))));
				//	Price Limit Enforce
				ctx.setContext(WindowNo, "EnforcePriceLimit", rs.getString(2));
				//	Currency
				Integer ii = Integer.valueOf(rs.getInt(3));
				mTab.setValue("C_Currency_ID", ii);
				//	PriceList Version
				ctx.setContext(WindowNo, "M_PriceList_Version_ID", rs.getInt(5));
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "";
	}
	/**
	 *	Order Header - PriceList.
	 *	(used also in Invoice)
	 *		- C_Currency_ID
	 *		- IsTaxIncluded
	 *	Window Context:
	 *		- EnforcePriceLimit
	 *		- StdPrecision
	 *		- M_PriceList_Version_ID
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String priceList (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_PriceList_ID = (Integer)value;
		if (M_PriceList_ID == null || M_PriceList_ID.intValue()== 0)
			return "";

		return setPriceListVersion(ctx, WindowNo, mTab);
	}	//	priceList

	/**************************************************************************
	 *	Invoice Line - Product.
	 *		- reset C_Charge_ID / M_AttributeSetInstance_ID
	 *		- PriceList, PriceStd, PriceLimit, C_Currency_ID, EnforcePriceLimit
	 *		- UOM
	 *	Calls Tax
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String product (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		setCalloutActive(true);
		mTab.setValue("C_Charge_ID", null);

		//	Set Attribute
		if (ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
				&& ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID", Integer.valueOf(ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID")));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);

		/*****	Price Calculation see also qty	****/
		boolean IsSOTrx = ctx.getContext(WindowNo, "IsSOTrx").equals("Y");
		int C_BPartner_ID = ctx.getContextAsInt(WindowNo, WindowNo, "C_BPartner_ID");
		BigDecimal Qty = (BigDecimal)mTab.getValue("QtyInvoiced");
		MProductPricing pp = new MProductPricing (ctx.getAD_Client_ID(), ctx.getAD_Org_ID(),
				M_Product_ID.intValue(), C_BPartner_ID, Qty, IsSOTrx);
		//
		int M_PriceList_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_ID");
		pp.setM_PriceList_ID(M_PriceList_ID);
		int M_PriceList_Version_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_Version_ID");
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		long time = ctx.getContextAsTime(WindowNo, "DateInvoiced");
		pp.setPriceDate(time);

		pp.calculatePrice();
		if (!pp.isCalculated())
		{
			mTab.setValue("PriceList", BigDecimal.ZERO);
			mTab.setValue("PriceLimit", BigDecimal.ZERO);
			mTab.setValue("PriceActual", BigDecimal.ZERO);
			mTab.setValue("PriceEntered", BigDecimal.ZERO);
			setCalloutActive(false);
			return Msg.getMsg(ctx, "ProductNotOnPriceList");
		}

		//
		mTab.setValue("PriceList", pp.getPriceList());
		mTab.setValue("PriceLimit", pp.getPriceLimit());
		mTab.setValue("PriceActual", pp.getPriceStd());
		mTab.setValue("PriceEntered", pp.getPriceStd());
		mTab.setValue("C_Currency_ID", Integer.valueOf(pp.getC_Currency_ID()));
		//	mTab.setValue("Discount", pp.getDiscount());
		int newC_UOM_ID = Integer.valueOf(pp.getC_UOM_ID());
		mTab.setValue("C_UOM_ID", newC_UOM_ID);

		// since UOM specified is changed (as a result of tab out from product), recalculate QtyEntered and QtyInvoiced based on new UOM
		BigDecimal QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
		if(newC_UOM_ID != 0)
		{
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, newC_UOM_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + newC_UOM_ID
						+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
		}

		// UOM Conversion
		BigDecimal QtyInvoiced = MUOMConversion.convertProductFrom (ctx, M_Product_ID.intValue(), newC_UOM_ID, QtyEntered);
		if (QtyInvoiced == null)
			QtyInvoiced = QtyEntered;

		// rounding Qty Invoiced. Enforce product UOM
		if(M_Product_ID.intValue() !=0 )
		{
			int productUOMPrecision = MProduct.get(ctx, M_Product_ID.intValue()).getUOMPrecision();
			QtyInvoiced = QtyInvoiced.setScale(productUOMPrecision, BigDecimal.ROUND_HALF_UP);
		}
		mTab.setValue("QtyInvoiced", QtyInvoiced);

		ctx.setContext( WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");
		ctx.setContext( WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		//
		setCalloutActive(false);
		return tax (ctx, WindowNo, mTab, mField, value);
	}	//	product

	/**
	 *	Invoice Line - Charge.
	 * 		- updates PriceActual from Charge
	 * 		- sets PriceLimit, PriceList to zero
	 * 	Calles tax
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String charge (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_Charge_ID = (Integer)value;
		if (C_Charge_ID == null || C_Charge_ID.intValue() == 0)
			return "";

		//	No Product defined
		if (mTab.getValue("M_Product_ID") != null)
		{
			mTab.setValue("C_Charge_ID", null);
			return "ChargeExclusively";
		}
		mTab.setValue("M_AttributeSetInstance_ID", null);
		mTab.setValue("S_ResourceAssignment_ID", null);
		mTab.setValue("C_UOM_ID", Integer.valueOf(100));	//	EA

		ctx.setContext( WindowNo, "DiscountSchema", "N");
		String sql = "SELECT ChargeAmt FROM C_Charge WHERE C_Charge_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_Charge_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				mTab.setValue ("PriceEntered", rs.getBigDecimal (1));
				mTab.setValue ("PriceActual", rs.getBigDecimal (1));
				mTab.setValue ("PriceLimit", Env.ZERO);
				mTab.setValue ("PriceList", Env.ZERO);
				mTab.setValue ("Discount", Env.ZERO);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql + e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		return tax (ctx, WindowNo, mTab, mField, value);
	}	//	charge


	/**
	 *	Invoice Line - Tax.
	 *		- basis: Product, Charge, BPartner Location
	 *		- sets C_Tax_ID
	 *  Calles Amount
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String tax (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String column = mField.getColumnName();
		if (value == null)
			return "";

		//	Check Product
		int M_Product_ID = 0;
		if (column.equals("M_Product_ID"))
			M_Product_ID = ((Integer)value).intValue();
		else
			M_Product_ID = ctx.getContextAsInt( WindowNo, "M_Product_ID");
		int C_Charge_ID = 0;
		if (column.equals("C_Charge_ID"))
			C_Charge_ID = ((Integer)value).intValue();
		else
			C_Charge_ID = ctx.getContextAsInt( WindowNo, "C_Charge_ID");
		log.fine("Product=" + M_Product_ID + ", C_Charge_ID=" + C_Charge_ID);
		if (M_Product_ID == 0 && C_Charge_ID == 0)
			return amt (ctx, WindowNo, mTab, mField, value);	//

		//	Check Partner Location
		int shipC_BPartner_Location_ID = ctx.getContextAsInt( WindowNo, "C_BPartner_Location_ID");
		if (shipC_BPartner_Location_ID == 0)
			return amt (ctx, WindowNo, mTab, mField, value);	//
		log.fine("Ship BP_Location=" + shipC_BPartner_Location_ID);
		int billC_BPartner_Location_ID = shipC_BPartner_Location_ID;
		log.fine("Bill BP_Location=" + billC_BPartner_Location_ID);

		//	Dates
		Timestamp billDate = new Timestamp(ctx.getContextAsTime(WindowNo, "DateInvoiced"));
		log.fine("Bill Date=" + billDate);
		Timestamp shipDate = billDate;
		log.fine("Ship Date=" + shipDate);

		int AD_Org_ID = ctx.getContextAsInt( WindowNo, "AD_Org_ID");
		log.fine("Org=" + AD_Org_ID);

		int M_Warehouse_ID = ctx.getContextAsInt( "#M_Warehouse_ID");
		log.fine("Warehouse=" + M_Warehouse_ID);

		//
		Integer idContract = (Integer)mTab.getValue("XX_Contract_ID");
		if (idContract!= null && idContract <= 0){
			int C_Tax_ID = Tax.get(ctx, M_Product_ID, C_Charge_ID, billDate, shipDate,
					AD_Org_ID, M_Warehouse_ID, billC_BPartner_Location_ID, shipC_BPartner_Location_ID,
					ctx.getContext( WindowNo, "IsSOTrx").equals("Y"));
			log.info("Tax ID=" + C_Tax_ID);
			//
			if (C_Tax_ID == 0)
				mTab.fireDataStatusEEvent(CLogger.retrieveError());
			else
				mTab.setValue("C_Tax_ID", Integer.valueOf(C_Tax_ID));
		}else{
			mTab.setValue("C_Tax_ID", Env.getCtx().getContextAsInt("#XX_L_TAX_IVA_ID"));
		}
		
		//
		return amt (ctx, WindowNo, mTab, mField, value);
	}	//	tax


	/**
	 *	Invoice - Amount.
	 *		- called from QtyInvoiced, PriceActual
	 *		- calculates LineNetAmt
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String amt (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		//	log.log(Level.WARNING,"amt - init");
		int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
		int M_Product_ID = ctx.getContextAsInt( WindowNo, "M_Product_ID");
		int M_PriceList_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_ID");
		//int StdPrecision = MPriceList.getPricePrecision(ctx, M_PriceList_ID);
		// Using Currency Precision. Not PriceList precision
		int C_Invoice_ID = ctx.getContextAsInt( WindowNo, "C_Invoice_ID");
		int StdPrecision = QueryUtil.getSQLValue(null,
				"SELECT c.StdPrecision FROM C_Currency c INNER JOIN C_Invoice x ON (x.C_Currency_ID=c.C_Currency_ID) WHERE x.C_Invoice_ID=?",
				C_Invoice_ID);
		if (StdPrecision < 0)
		{
			log.warning("Precision=" + StdPrecision + " - set to 2");
			StdPrecision = 2;
		}

		BigDecimal QtyEntered, QtyInvoiced, PriceEntered, PriceActual, PriceLimit, Discount, PriceList;
		//	get values
		QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
		QtyInvoiced = (BigDecimal)mTab.getValue("QtyInvoiced");
		log.fine("QtyEntered=" + QtyEntered + ", Invoiced=" + QtyInvoiced + ", UOM=" + C_UOM_To_ID);
		//
		PriceEntered = (BigDecimal)mTab.getValue("PriceEntered");
		PriceActual = (BigDecimal)mTab.getValue("PriceActual");
		//	Discount = (BigDecimal)mTab.getValue("Discount");
		PriceLimit = (BigDecimal)mTab.getValue("PriceLimit");
		PriceList = (BigDecimal)mTab.getValue("PriceList");
		log.fine("PriceList=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
		log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual);// + ", Discount=" + Discount);

		//	Qty changed - recalc price
		if ((mField.getColumnName().equals("QtyInvoiced")
				|| mField.getColumnName().equals("QtyEntered")
				|| mField.getColumnName().equals("M_Product_ID"))
				&& !"N".equals(ctx.getContext( WindowNo, "DiscountSchema")))
		{
			int C_BPartner_ID = ctx.getContextAsInt( WindowNo, "C_BPartner_ID");
			if (mField.getColumnName().equals("QtyEntered"))
				QtyInvoiced = MUOMConversion.convertProductTo (ctx, M_Product_ID,
						C_UOM_To_ID, QtyEntered);
			if (QtyInvoiced == null)
				QtyInvoiced = QtyEntered;
			boolean IsSOTrx = ctx.getContext( WindowNo, "IsSOTrx").equals("Y");
			MProductPricing pp = new MProductPricing (ctx.getAD_Client_ID(), ctx.getAD_Org_ID(),
					M_Product_ID, C_BPartner_ID, QtyInvoiced, IsSOTrx);
			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			long date = ctx.getContextAsTime(WindowNo, "DateInvoiced");
			pp.setPriceDate(date);
			//
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, pp.getPriceStd());
			if (PriceEntered == null)
				PriceEntered = pp.getPriceStd();
			//
			log.fine("amt - QtyChanged -> PriceActual=" + pp.getPriceStd()
					+ ", PriceEntered=" + PriceEntered + ", Discount=" + pp.getDiscount());
			PriceActual = pp.getPriceStd();
			mTab.setValue("PriceActual", PriceActual);
			//	mTab.setValue("Discount", pp.getDiscount());
			mTab.setValue("PriceEntered", PriceEntered);
			ctx.setContext( WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		}
		else if (mField.getColumnName().equals("PriceActual"))
		{
			PriceActual = (BigDecimal)value;
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			//
			log.fine("amt - PriceActual=" + PriceActual
					+ " -> PriceEntered=" + PriceEntered);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		else if (mField.getColumnName().equals("PriceEntered"))
		{
			PriceEntered = (BigDecimal)value;
			PriceActual = MUOMConversion.convertProductTo (ctx, M_Product_ID,
					C_UOM_To_ID, PriceEntered);
			if (PriceActual == null)
				PriceActual = PriceEntered;
			//
			log.fine("amt - PriceEntered=" + PriceEntered
					+ " -> PriceActual=" + PriceActual);
			mTab.setValue("PriceActual", PriceActual);
		}

		/**  Discount entered - Calculate Actual/Entered
		if (mField.getColumnName().equals("Discount"))
		{
			PriceActual = new BigDecimal ((100.0 - Discount.doubleValue()) / 100.0 * PriceList.doubleValue());
			if (PriceActual.scale() > StdPrecision)
				PriceActual = PriceActual.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
				C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			mTab.setValue("PriceActual", PriceActual);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		//	calculate Discount
		else
		{
			if (PriceList.intValue() == 0)
				Discount = Env.ZERO;
			else
				Discount = new BigDecimal ((PriceList.doubleValue() - PriceActual.doubleValue()) / PriceList.doubleValue() * 100.0);
			if (Discount.scale() > 2)
				Discount = Discount.setScale(2, BigDecimal.ROUND_HALF_UP);
			mTab.setValue("Discount", Discount);
		}
		log.fine("amt = PriceEntered=" + PriceEntered + ", Actual" + PriceActual + ", Discount=" + Discount);
		/* */

		//	Check PriceLimit
		String epl = ctx.getContext( WindowNo, "EnforcePriceLimit");
		boolean enforce = ctx.isSOTrx(WindowNo) && epl != null && epl.equals("Y");
		if (enforce && MRole.getDefault().isOverwritePriceLimit())
			enforce = false;
		//	Check Price Limit?
		if (enforce && PriceLimit.doubleValue() != 0.0
				&& PriceActual.compareTo(PriceLimit) < 0)
		{
			PriceActual = PriceLimit;
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, PriceLimit);
			if (PriceEntered == null)
				PriceEntered = PriceLimit;
			log.fine("amt =(under) PriceEntered=" + PriceEntered + ", Actual" + PriceLimit);
			mTab.setValue ("PriceActual", PriceLimit);
			mTab.setValue ("PriceEntered", PriceEntered);
			mTab.fireDataStatusEEvent ("UnderLimitPrice", "", false);
			//	Repeat Discount calc
			if (PriceList.intValue() != 0)
			{
				Discount = new BigDecimal ((PriceList.doubleValue () - PriceActual.doubleValue ()) / PriceList.doubleValue () * 100.0);
				if (Discount.scale () > 2)
					Discount = Discount.setScale (2, BigDecimal.ROUND_HALF_UP);
				//	mTab.setValue ("Discount", Discount);
			}
		}

		//	Line Net Amt
		BigDecimal LineNetAmt = QtyInvoiced.multiply(PriceActual);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		log.info("amt = LineNetAmt=" + LineNetAmt);
		mTab.setValue("LineNetAmt", LineNetAmt);

		//	Calculate Tax Amount for PO
		boolean IsSOTrx = "Y".equals(ctx.getContext( WindowNo, "IsSOTrx"));
		if (!IsSOTrx)
		{
			BigDecimal TaxAmt = Env.ZERO;
			if (mField.getColumnName().equals("TaxAmt"))
			{
				TaxAmt = (BigDecimal)mTab.getValue("TaxAmt");
			}
			else
			{
				Integer taxID = (Integer)mTab.getValue("C_Tax_ID");
				if (taxID != null)
				{
					int C_Tax_ID = taxID.intValue();
					MTax tax = new MTax (ctx, C_Tax_ID, null);
					TaxAmt = tax.calculateTax(LineNetAmt, isTaxIncluded(WindowNo), StdPrecision);
					mTab.setValue("TaxAmt", TaxAmt);
				}
			}
			//	Add it up
			mTab.setValue("LineTotalAmt", LineNetAmt.add(TaxAmt));
		}

		setCalloutActive(false);
		return "";
	}	//	amt

	/**
	 * 	Is Tax Included
	 *	@param WindowNo window no
	 *	@return tax included (default: false)
	 */
	private boolean isTaxIncluded (int WindowNo)
	{
		Ctx ctx = Env.getCtx();
		String ss = ctx.getContext( WindowNo, "IsTaxIncluded");
		//	Not Set Yet
		if (ss.length() == 0)
		{
			int M_PriceList_ID = Env.getCtx().getContextAsInt(WindowNo, "M_PriceList_ID");
			if (M_PriceList_ID == 0)
				return false;
			ss = QueryUtil.getSQLValueString(null,
					"SELECT IsTaxIncluded FROM M_PriceList WHERE M_PriceList_ID=?",
					M_PriceList_ID);
			if (ss == null)
				ss = "N";
			ctx.setContext(WindowNo, "IsTaxIncluded", ss);
		}
		return "Y".equals(ss);
	}	//	isTaxIncluded

	/**
	 *	Invoice Line - Quantity.
	 *		- called from C_UOM_ID, QtyEntered, QtyInvoiced
	 *		- enforces qty UOM relationship
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String qty (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int M_Product_ID = ctx.getContextAsInt( WindowNo, "M_Product_ID");
		//	log.log(Level.WARNING,"qty - init - M_Product_ID=" + M_Product_ID);
		BigDecimal QtyInvoiced, QtyEntered, PriceActual, PriceEntered;

		//	No Product
		/*if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			mTab.setValue("QtyInvoiced", QtyEntered);
		}*/
		//	UOM Changed - convert from Entered -> Product
		/*else*/ if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");

			// Do rounding on Qty Entered only if UOM is specified
			if(C_UOM_To_ID != 0)
			{
				BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
				if (QtyEntered.compareTo(QtyEntered1) != 0)
				{
					log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
							+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
					QtyEntered = QtyEntered1;
					mTab.setValue("QtyEntered", QtyEntered);
				}
			}

			// UOM Conversion
			QtyInvoiced = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, QtyEntered);
			if (QtyInvoiced == null)
				QtyInvoiced = QtyEntered;

			// rounding Qty Invoiced. Enforce product UOM
			if(M_Product_ID !=0 )
			{
				int productUOMPrecision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
				QtyInvoiced = QtyInvoiced.setScale(productUOMPrecision, BigDecimal.ROUND_HALF_UP);
			}

			boolean conversion = QtyEntered.compareTo(QtyInvoiced) != 0;
			PriceActual = (BigDecimal)mTab.getValue("PriceActual");
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			log.fine("qty - UOM=" + C_UOM_To_ID
					+ ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
					+ " -> " + conversion
					+ " QtyInvoiced/PriceEntered=" + QtyInvoiced + "/" + PriceEntered);
			ctx.setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyInvoiced", QtyInvoiced);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		//	QtyEntered changed - calculate QtyInvoiced
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
			QtyEntered = (BigDecimal)value;

			// Do rounding on Qty Entered only if UOM is specified
			if(C_UOM_To_ID != 0)
			{
				BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
				if (QtyEntered.compareTo(QtyEntered1) != 0)
				{
					log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
							+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
					QtyEntered = QtyEntered1;
					mTab.setValue("QtyEntered", QtyEntered);
				}
			}

			// UOM conversion
			QtyInvoiced = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, QtyEntered);
			if (QtyInvoiced == null)
				QtyInvoiced = QtyEntered;

			// rounding Qty Invoiced. Enforce product UOM
			if(M_Product_ID !=0 )
			{
				int productUOMPrecision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
				QtyInvoiced = QtyInvoiced.setScale(productUOMPrecision, BigDecimal.ROUND_HALF_UP);
			}
			boolean conversion = QtyEntered.compareTo(QtyInvoiced) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID
					+ ", QtyEntered=" + QtyEntered
					+ " -> " + conversion
					+ " QtyInvoiced=" + QtyInvoiced);
			ctx.setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyInvoiced", QtyInvoiced);
		}
		//	QtyInvoiced changed - calculate QtyEntered (should not happen)
		else if (mField.getColumnName().equals("QtyInvoiced"))
		{
			int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
			QtyInvoiced = (BigDecimal)value;
			int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
			BigDecimal QtyInvoiced1 = QtyInvoiced.setScale(precision, BigDecimal.ROUND_HALF_UP);
			if (QtyInvoiced.compareTo(QtyInvoiced1) != 0)
			{
				log.fine("Corrected QtyInvoiced Scale "
						+ QtyInvoiced + "->" + QtyInvoiced1);
				QtyInvoiced = QtyInvoiced1;
				mTab.setValue("QtyInvoiced", QtyInvoiced);
			}
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID,
					C_UOM_To_ID, QtyInvoiced);
			if (QtyEntered == null)
				QtyEntered = QtyInvoiced;
			boolean conversion = QtyInvoiced.compareTo(QtyEntered) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID
					+ ", QtyInvoiced=" + QtyInvoiced
					+ " -> " + conversion
					+ " QtyEntered=" + QtyEntered);
			ctx.setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}
		//
		setCalloutActive(false);
		return "";
	}	//	qty

	/**
	 * Busca el ID del producto
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * 					org.compiere.model.CalloutInvoice.productName
	 * @return
	 */
	public String productName (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value){		
		if (isCalloutActive())
			return "";
		
		setCalloutActive(true);	
		
		String sql = "";
		Integer product = (Integer)value;
		Integer idContract = (Integer)mTab.getValue("XX_Contract_ID");
		Integer idOrder = (Integer)mTab.getValue("C_Order_ID");
		if (product == null){
			if (idContract != null && idContract != 0)
				sql = "select M_Product_ID from XX_Contract where XX_Contract_ID = " + idContract;
			else
				sql = "select M_Product_ID from C_OrderLine where C_Order_ID = " + idOrder;
			PreparedStatement pstmt = null; 
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null); 
				rs = pstmt.executeQuery();
				if(rs.next()){
					mTab.setValue("M_Product_ID", rs.getInt(1));
					mTab.setValue("XX_Product_ID", rs.getInt(1));					
					return "";
				}
			}catch (Exception e) {
				log.log(Level.SEVERE, sql);
				e.printStackTrace();
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		
		setCalloutActive(false);
		return "";
	}

}	//	CalloutInvoice
