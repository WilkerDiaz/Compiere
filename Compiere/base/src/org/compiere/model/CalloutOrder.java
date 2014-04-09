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
 *	Order Callouts.
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutOrder.java,v 1.5 2006/10/08 06:57:33 comdivision Exp $
 */
public class CalloutOrder extends CalloutEngine
{
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());

	/**	Debug Steps			*/
	private boolean steps = false;

	/**
	 *	Order Header Change - DocType.
	 *		- InvoiceRuld/DeliveryRule/PaymentRule
	 *		- temporary Document
	 *  Context:
	 *  	- DocSubTypeSO
	 *		- HasCharges
	 *	- (re-sets Business Partner info of required)
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String docType (Ctx ctx, int WindowNo, GridTab mTab, GridField mField,
			Object value, Object oldValue)
	{
		Integer C_DocType_ID = (Integer)value;		//	Actually C_DocTypeTarget_ID
		if (C_DocType_ID == null || C_DocType_ID.intValue() == 0)
			return "";

		//	Re-Create new DocNo, if there is a doc number already
		//	and the existing source used a different Sequence number
		String oldDocNo = (String)mTab.getValue("DocumentNo");
		boolean newDocNo = oldDocNo == null;
		if (!newDocNo && oldDocNo.startsWith("<") && oldDocNo.endsWith(">"))
			newDocNo = true;
		Integer oldC_DocType_ID = (Integer)mTab.getValue("C_DocType_ID");
		if ((oldC_DocType_ID == null || oldC_DocType_ID.intValue() == 0 )
				&& oldValue != null)	//	get old Target
				oldC_DocType_ID = (Integer)oldValue;
		if (oldC_DocType_ID!=value)
			mTab.setValue("C_DocType_ID", value);
			

		String sql = "SELECT d.DocSubTypeSO,d.HasCharges,'N',"			//	1..3
			+ "d.IsDocNoControlled,s.CurrentNext,s.CurrentNextSys,"     //  4..6
			+ "s.AD_Sequence_ID,d.IsSOTrx, d.IsReturnTrx "              //	7..9
			+ "FROM C_DocType d "
			+ "LEFT OUTER JOIN AD_Sequence s ON (d.DocNoSequence_ID=s.AD_Sequence_ID) "
			+ "WHERE C_DocType_ID=?";	//	#1
		
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
			String DocSubTypeSO = "";
			boolean IsSOTrx = true;
			boolean isReturnTrx = false;
			if (rs.next())		//	we found document type
			{

				//	Set Context:	Document Sub Type for Sales Orders
				DocSubTypeSO = rs.getString(1);
				if (DocSubTypeSO == null)
					DocSubTypeSO = "--";
				ctx.setContext( WindowNo, "OrderType", DocSubTypeSO);
				//	No Drop Ship other than Standard
				if (!DocSubTypeSO.equals(MOrder.DocSubTypeSO_Standard))
					mTab.setValue ("IsDropShip", "N");

				//	IsSOTrx
				if ("N".equals(rs.getString(8)))
					IsSOTrx = false;

				//IsReturnTrx
				isReturnTrx = "Y".equals(rs.getString(9));

				//	Skip these steps for RMA. These are copied from the Original Order
				if(!isReturnTrx)
				{
					if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS))
						mTab.setValue ("DeliveryRule", X_C_Order.DELIVERYRULE_Force);
					else if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay))
						mTab.setValue ("DeliveryRule", X_C_Order.DELIVERYRULE_AfterReceipt);
					else
						mTab.setValue ("DeliveryRule", X_C_Order.DELIVERYRULE_Availability);

					//	Invoice Rule
					if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS)
							|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay)
							|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_OnCredit) )
						mTab.setValue ("InvoiceRule", X_C_Order.INVOICERULE_Immediate);
					else
						mTab.setValue ("InvoiceRule", X_C_Order.INVOICERULE_AfterDelivery);

					//	Payment Rule - POS Order
					if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS))
						mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_Cash);
					else
						mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_OnCredit);


					//	Set Context:
					ctx.setContext( WindowNo, "HasCharges", rs.getString(2));
				}
				else // Returns
				{
					if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS))
						mTab.setValue ("DeliveryRule", X_C_Order.DELIVERYRULE_Force);
					else
						mTab.setValue ("DeliveryRule", X_C_Order.DELIVERYRULE_Manual);
				}

				//	DocumentNo
				if (rs.getString(4).equals("Y"))			//	IsDocNoControlled
				{
					if (AD_Sequence_ID != rs.getInt(7))
						newDocNo = true;
					if (newDocNo)
						if (Ini.isPropertyBool(Ini.P_COMPIERESYS) && Env.getCtx().getAD_Client_ID() < 1000000)
							mTab.setValue("DocumentNo", "<" + rs.getString(6) + ">");
						else
							mTab.setValue("DocumentNo", "<" + rs.getString(5) + ">");
				}
			}
			rs.close();
			pstmt.close();

			// Skip remaining steps for RMA
			if (isReturnTrx)
				return "";
			//  When BPartner is changed, the Rules are not set if
			//  it is a POS or Credit Order (i.e. defaults from Standard BPartner)
			//  This re-reads the Rules and applies them.
			if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS)
					|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay))    //  not for POS/PrePay
				;
			else
			{
				sql = "SELECT PaymentRule,C_PaymentTerm_ID,"            //  1..2
					+ "InvoiceRule,DeliveryRule,"                       //  3..4
					+ "FreightCostRule,DeliveryViaRule, "               //  5..6
					+ "PaymentRulePO,PO_PaymentTerm_ID, M_Shipper_ID "
					+ "FROM C_BPartner "
					+ "WHERE C_BPartner_ID=?";		//	#1
				pstmt = DB.prepareStatement(sql, (Trx) null);
				int C_BPartner_ID = ctx.getContextAsInt( WindowNo, "C_BPartner_ID");
				pstmt.setInt(1, C_BPartner_ID);
				//
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					//	PaymentRule
					String s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if (s != null && s.length() != 0)
					{
						if (IsSOTrx && (s.equals("B") || s.equals("S") || s.equals("U")))	//	No Cash/Check/Transfer for SO_Trx
							s = "P";										//  Payment Term
						if (!IsSOTrx && s.equals("B"))					//	No Cash for PO_Trx
							s = "P";										//  Payment Term
						mTab.setValue("PaymentRule", s);
					}
					//	Payment Term
					Integer ii =Integer.valueOf(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
					if (!rs.wasNull())
						mTab.setValue("C_PaymentTerm_ID", ii);
					else
						mTab.setValue("C_PaymentTerm_ID", ctx.getContextAsInt( "#C_PaymentTerm_ID"));
					//	InvoiceRule
					s = rs.getString(3);
					if (s != null && s.length() != 0)
						mTab.setValue("InvoiceRule", s);
					//	DeliveryRule
					s = rs.getString(4);
					if (s != null && s.length() != 0)
						mTab.setValue("DeliveryRule", s);
					//	FreightCostRule
					s = rs.getString(5);
					if (s != null && s.length() != 0)
						mTab.setValue("FreightCostRule", s);
					
					//	DeliveryViaRule
					s = rs.getString("DeliveryViaRule");
					if (s != null && s.length() != 0)
					{
						mTab.setValue("DeliveryViaRule", s);			
					//	Freight Carrier
						if (s.equals(X_C_Order.DELIVERYVIARULE_Shipper)) 							
							mTab.setValue("M_Shipper_ID", rs.getInt("M_Shipper_ID"));
					}
					
				}
			}   //  re-read customer rules
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
	 *	Order Header - BPartner.
	 *		- M_PriceList_ID (+ Context)
	 *		- C_BPartner_Location_ID
	 *		- Bill_BPartner_ID/Bill_Location_ID
	 *		- AD_User_ID
	 *		- POReference
	 *		- SO_Description
	 *		- IsDiscountPrinted
	 *		- InvoiceRule/DeliveryRule/PaymentRule/FreightCost/DeliveryViaRule
	 *		- C_PaymentTerm_ID
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String bPartner (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_BPartner_ID = (Integer)value;
		if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
			return "";

		// Skip rest of steps for RMA. These fields are copied over from the orignal order instead.
		boolean isReturnTrx = (Boolean)mTab.getValue("IsReturnTrx");
		if (isReturnTrx)
		{
			MBPartner bpartner = new MBPartner (ctx, C_BPartner_ID, null);

			if(ctx.isSOTrx(WindowNo))
				mTab.setValue("M_ReturnPolicy_ID", bpartner.getM_ReturnPolicy_ID());
			else
				mTab.setValue("M_ReturnPolicy_ID", bpartner.getPO_ReturnPolicy_ID());

			return "";
		}

		setCalloutActive(true);

		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
			+ " COALESCE(p.M_PriceList_ID,g.M_PriceList_ID) AS M_PriceList_ID, p.PaymentRule,p.POReference,"
			+ " p.SO_Description,p.IsDiscountPrinted,p.SalesRep_ID,"
			+ " p.InvoiceRule,p.DeliveryRule,p.FreightCostRule,DeliveryViaRule, p.M_Shipper_ID,"
			+ " p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ " lship.C_BPartner_Location_ID,c.AD_User_ID,"
			+ " COALESCE(p.PO_PriceList_ID,g.PO_PriceList_ID) AS PO_PriceList_ID, p.PaymentRulePO,p.PO_PaymentTerm_ID,"
			+ " lbill.C_BPartner_Location_ID AS Bill_Location_ID, p.SOCreditStatus, lship.IsBillTo ShipToIsBillTo "
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID)"
			+ " LEFT OUTER JOIN C_BPartner_Location lbill ON (p.C_BPartner_ID=lbill.C_BPartner_ID AND lbill.IsBillTo='Y' AND lbill.IsActive='Y')"
			+ " LEFT OUTER JOIN C_BPartner_Location lship ON (p.C_BPartner_ID=lship.C_BPartner_ID AND lship.IsShipTo='Y' AND lship.IsActive='Y')"
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID AND c.IsActive='Y') "
			+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'"		//	#1
		    + " ORDER BY lbill.Name ASC,lship.Name ASC ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		boolean IsSOTrx = ctx.isSOTrx(WindowNo);

		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	PriceList (indirect: IsTaxIncluded & Currency)
				Integer ii = Integer.valueOf(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
				if (!rs.wasNull())
					mTab.setValue("M_PriceList_ID", ii);
				else
				{	//	get default PriceList
					int i = ctx.getContextAsInt( "#M_PriceList_ID");
					if (i != 0)
						mTab.setValue("M_PriceList_ID", Integer.valueOf(i));
				}

				//	Bill-To BPartner
				mTab.setValue("Bill_BPartner_ID", C_BPartner_ID);

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
				{
					mTab.setValue("AD_User_ID", Integer.valueOf(contID));
					mTab.setValue("Bill_User_ID", Integer.valueOf(contID));
				}

				/* If user logged in is not a SalesRep, default the SalesRep associated with
				 * the customer.
				 */
				if(!MUser.isSalesRep(ctx.getAD_User_ID()) && IsSOTrx)
				{
					int SalesRep_ID=rs.getInt("SalesRep_ID");
					if(SalesRep_ID!=0)
						mTab.setValue("SalesRep_ID", SalesRep_ID);
				}

				//	CreditAvailable
				if (IsSOTrx)
				{
					double CreditLimit = rs.getDouble("SO_CreditLimit");
					//	String SOCreditStatus = rs.getString("SOCreditStatus");
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
				//	Do not set if already present
				String s = rs.getString("POReference");
				String poRef = (String) mTab.getValue("POReference");
				if (s != null && s.length() != 0 && (poRef == null || poRef.trim().length() == 0))
					if(IsSOTrx)		//	Do Not set if Purchase Order
						mTab.setValue("POReference", s);
				// should not be reset to null if we entered already value! VHARCQ, accepted YS makes sense that way
				// TODO: should get checked and removed if no longer needed!
				/*else
					mTab.setValue("POReference", null);*/

				//	SO Description
				//	Do not set if already present
				s = rs.getString("SO_Description");
				if (s != null && s.trim().length() != 0 && (poRef == null || poRef.trim().length() == 0))
					if(IsSOTrx)		//	Do Not set if Purchase Order
						mTab.setValue("Description", s);
				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				if (s != null && s.length() != 0)
					mTab.setValue("IsDiscountPrinted", s);
				else
					mTab.setValue("IsDiscountPrinted", "N");

				//	Defaults, if not Walkin Receipt or Walkin Invoice
				String OrderType = ctx.getContext( WindowNo, "OrderType");
				mTab.setValue("InvoiceRule", X_C_Order.INVOICERULE_AfterDelivery);
				mTab.setValue("DeliveryRule", X_C_Order.DELIVERYRULE_Availability);
				mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_OnCredit);
				if (OrderType.equals(MOrder.DocSubTypeSO_Prepay))
				{
					mTab.setValue("InvoiceRule", X_C_Order.INVOICERULE_Immediate);
					mTab.setValue("DeliveryRule", X_C_Order.DELIVERYRULE_AfterReceipt);
				}
				else if (OrderType.equals(MOrder.DocSubTypeSO_POS))	//  for POS
					mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_Cash);
				else
				{
					//	PaymentRule
					s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if (s != null && s.length() != 0)
					{
						if (s.equals("B"))				//	No Cache in Non POS
							s = "P";
						if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
							s = "P";										//  Payment Term
						mTab.setValue("PaymentRule", s);
					}
					//	Payment Term
					ii = Integer.valueOf(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
					if (!rs.wasNull())
						mTab.setValue("C_PaymentTerm_ID", ii);
					else
						mTab.setValue("C_PaymentTerm_ID", ctx.getContextAsInt( "#C_PaymentTerm_ID"));						
					//	InvoiceRule
					s = rs.getString("InvoiceRule");
					if (s != null && s.length() != 0)
						mTab.setValue("InvoiceRule", s);
					//	DeliveryRule
					s = rs.getString("DeliveryRule");
					if (s != null && s.length() != 0)
						mTab.setValue("DeliveryRule", s);
					//	FreightCostRule
					s = rs.getString("FreightCostRule");
					if (s != null && s.length() != 0)
						mTab.setValue("FreightCostRule", s);
					
					//	DeliveryViaRule
					s = rs.getString("DeliveryViaRule");
					if (s != null && s.length() != 0)
					{
						mTab.setValue("DeliveryViaRule", s);
						// Freight Carrier
						if (s.equals(X_C_Order.DELIVERYVIARULE_Shipper))
							mTab.setValue("M_Shipper_ID", rs.getInt("M_Shipper_ID"));
					}
				
					// Project
					mTab.setValue("C_Project_ID", null);
					
				}
				boolean match = false, overwrite = false;
				int shipTo_ID =0, bill_Location_ID = 0;
				String loc = null;
				if (C_BPartner_ID.toString().equals(ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID")))
					loc =ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_Location_ID");
				if(loc !=null && loc.length()>0)
					do{
						// Ship-To Location
						shipTo_ID = rs.getInt("C_BPartner_Location_ID");
						bill_Location_ID = rs.getInt("Bill_Location_ID");
						//	overwritten by InfoBP selection - works only if InfoWindow
						//	was used otherwise creates error (uses last value, may belong to differnt BP)
						if (loc != null && loc.length() > 0 && (Integer.parseInt(loc)==shipTo_ID || Integer.parseInt(loc)==bill_Location_ID))
							match=true;
						overwrite = "Y".equals(rs.getString("ShipToIsBillTo"));
					}while(rs.next() && !match);
				else {
					shipTo_ID = rs.getInt("C_BPartner_Location_ID");
					bill_Location_ID = rs.getInt("Bill_Location_ID");
					overwrite = "Y".equals(rs.getString("ShipToIsBillTo"));
				}

				if (bill_Location_ID == 0)
					mTab.setValue("Bill_Location_ID", null);
				else
					mTab.setValue("Bill_Location_ID", Integer.valueOf(bill_Location_ID));
				if (shipTo_ID == 0)
					mTab.setValue("C_BPartner_Location_ID", null);
				else
				{
					mTab.setValue("C_BPartner_Location_ID", Integer.valueOf(shipTo_ID));
					if (overwrite)	//	set the same
						mTab.setValue("Bill_Location_ID", Integer.valueOf(shipTo_ID));
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			setCalloutActive(false);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		setCalloutActive(false);
		return "";
	}	//	bPartner

	/**
	 *	Order Header - Invoice BPartner.
	 *		- M_PriceList_ID (+ Context)
	 *		- Bill_Location_ID
	 *		- Bill_User_ID
	 *		- POReference
	 *		- SO_Description
	 *		- IsDiscountPrinted
	 *		- InvoiceRule/PaymentRule
	 *		- C_PaymentTerm_ID
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String bPartnerBill (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		Integer bill_BPartner_ID = (Integer)value;
		if (bill_BPartner_ID == null || bill_BPartner_ID.intValue() == 0)
			return "";

		// Skip rest of steps for RMA
		boolean isReturnTrx = (Boolean)mTab.getValue("IsReturnTrx");
		if (isReturnTrx)
			return "";

		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
			+ "p.M_PriceList_ID,p.PaymentRule,p.POReference,"
			+ "p.SO_Description,p.IsDiscountPrinted,"
			+ "p.InvoiceRule,p.DeliveryRule,p.FreightCostRule,DeliveryViaRule,"
			+ "p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ "c.AD_User_ID,"
			+ "p.PO_PriceList_ID, p.PaymentRulePO, p.PO_PaymentTerm_ID,"
			+ "lbill.C_BPartner_Location_ID AS Bill_Location_ID "
			+ "FROM C_BPartner p"
			+ " LEFT OUTER JOIN C_BPartner_Location lbill ON (p.C_BPartner_ID=lbill.C_BPartner_ID AND lbill.IsBillTo='Y' AND lbill.IsActive='Y')"
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID) "
			+ "WHERE p.C_BPartner_ID=?" 
//			+ " AND p.IsActive='Y'"
			;		//	#1

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		boolean IsSOTrx = "Y".equals(ctx.getContext( WindowNo, "IsSOTrx"));

		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, bill_BPartner_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	PriceList (indirect: IsTaxIncluded & Currency)
				Integer ii = Integer.valueOf(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
				if (!rs.wasNull())
					mTab.setValue("M_PriceList_ID", ii);
				else
				{	//	get default PriceList
					int i = ctx.getContextAsInt( "#M_PriceList_ID");
					if (i != 0)
						mTab.setValue("M_PriceList_ID", Integer.valueOf(i));
				}

				int bill_Location_ID = rs.getInt("Bill_Location_ID");
				//	overwritten by InfoBP selection - works only if InfoWindow
				//	was used otherwise creates error (uses last value, may belong to differnt BP)
				if (bill_BPartner_ID.toString().equals(ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID")))
				{
					String loc = ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_Location_ID");
					if (loc.length() > 0)
						bill_Location_ID = Integer.parseInt(loc);
				}
				if (bill_Location_ID == 0)
					mTab.setValue("Bill_Location_ID", null);
				else
					mTab.setValue("Bill_Location_ID", Integer.valueOf(bill_Location_ID));

				//	Contact - overwritten by InfoBP selection
				int contID = rs.getInt("AD_User_ID");
				if (bill_BPartner_ID.toString().equals(ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID")))
				{
					String cont = ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contID = Integer.parseInt(cont);
				}
				if (contID == 0)
					mTab.setValue("Bill_User_ID", null);
				else
					mTab.setValue("Bill_User_ID", Integer.valueOf(contID));

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
				String s = rs.getString("POReference");

				// Order Reference should not be set by Bill To BPartner; only by BPartner.
				/* if (s != null && s.length() != 0)
					mTab.setValue("POReference", s);
				else
					mTab.setValue("POReference", null);*/
				//	SO Description
				//  if there is an existing description, do not change it.
				String Description = (String) mTab.getValue("Description");
				s = rs.getString("SO_Description");
				if (s != null && s.trim().length() != 0 && (Description ==null || Description.trim().length() == 0))
					if(IsSOTrx)		//Do Not set if Purchase Order
						mTab.setValue("Description", s);

				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				if (s != null && s.length() != 0)
					mTab.setValue("IsDiscountPrinted", s);
				else
					mTab.setValue("IsDiscountPrinted", "N");

				//	Defaults, if not Walkin Receipt or Walkin Invoice
				String OrderType = ctx.getContext( WindowNo, "OrderType");
				mTab.setValue("InvoiceRule", X_C_Order.INVOICERULE_AfterDelivery);
				mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_OnCredit);
				if (OrderType.equals(MOrder.DocSubTypeSO_Prepay))
					mTab.setValue("InvoiceRule", X_C_Order.INVOICERULE_Immediate);
				else if (OrderType.equals(MOrder.DocSubTypeSO_POS))	//  for POS
					mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_Cash);
				else
				{
					//	PaymentRule
					s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if (s != null && s.length() != 0)
					{
						if (s.equals("B"))				//	No Cache in Non POS
							s = "P";
						if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
							s = "P";										//  Payment Term
						mTab.setValue("PaymentRule", s);
					}
					//	Payment Term
					ii = Integer.valueOf(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
					if (!rs.wasNull())
						mTab.setValue("C_PaymentTerm_ID", ii);
					else
						mTab.setValue("C_PaymentTerm_ID", ctx.getContextAsInt( "#C_PaymentTerm_ID"));
					//	InvoiceRule
					s = rs.getString("InvoiceRule");
					if (s != null && s.length() != 0)
						mTab.setValue("InvoiceRule", s);
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "bPartnerBill", e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return "";
	}	//	bPartnerBill

	/**
	 *  Set Delivery Rule if Warehouse is changed.
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String warehouse (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";

		Integer M_Warehouse_ID = (Integer)value;
		if (M_Warehouse_ID == null || M_Warehouse_ID.intValue() == 0)
			return "";

		MWarehouse wh = MWarehouse.get(ctx, M_Warehouse_ID);
		String DeliveryRule = mTab.get_ValueAsString("DeliveryRule");
		if(wh.isDisallowNegativeInv() && DeliveryRule.equals(X_C_Order.DELIVERYRULE_Force) ||
				DeliveryRule == null || DeliveryRule.length()==0)
			mTab.setValue("DeliveryRule",X_C_Order.DELIVERYRULE_Availability);

		return "";
	}	//	warehouse

	/**
	 *  Set PriceList parameters based on the date ordered.
	 * 	org.compiere.model.CalloutOrder.dateOrdered
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String dateOrdered (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
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

		Timestamp orderDate = new Timestamp(ctx.getContextAsTime( WindowNo, "DateOrdered"));
		if (orderDate == null)
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
			pstmt.setTimestamp(2, orderDate);
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
		if (steps) log.warning("fini");
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
		if (steps) log.warning("init");

		return setPriceListVersion(ctx, WindowNo, mTab);
	}	//	priceList


	/*************************************************************************
	 *	Order Line - Product.
	 *		- reset C_Charge_ID / M_AttributeSetInstance_ID
	 *		- PriceList, PriceStd, PriceLimit, C_Currency_ID, EnforcePriceLimit
	 *		- UOM
	 *	Calls Tax
	 *
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String product (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
		{
			//Resetting the Quantity, Price, and UOM  fields in case of product field is cleared
			mTab.setValue("QtyOrdered", BigDecimal.ONE);
			mTab.setValue("QtyEntered", BigDecimal.ONE);
			mTab.setValue("PriceList", BigDecimal.ZERO);
			mTab.setValue("PriceLimit", BigDecimal.ZERO);
			mTab.setValue("PriceActual", BigDecimal.ZERO);
			mTab.setValue("PriceEntered", BigDecimal.ZERO);	
			mTab.setValue("Discount",BigDecimal.ZERO);				
			mTab.setValue("C_UOM_ID", MUOM.Each_ID);	
			mTab.setValue("LineNetAmt", BigDecimal.ZERO);
			return "";
		}

		boolean isReturnTrx = "Y".equals(ctx.getContext(WindowNo,"IsReturnTrx"));
		if (isReturnTrx)
			return "";

		setCalloutActive(true);
		if (steps) log.warning("init");
		//
		mTab.setValue("C_Charge_ID", null);
		//	Set Attribute
		if (ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
				&& ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID", Integer.valueOf(ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID")));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);

		/*****	Price Calculation see also qty	****/
		int C_BPartner_ID = ctx.getContextAsInt( WindowNo, "C_BPartner_ID");
		BigDecimal Qty = (BigDecimal)mTab.getValue("QtyOrdered");
		boolean IsSOTrx = ctx.getContext( WindowNo, "IsSOTrx").equals("Y");
		MProductPricing pp = new MProductPricing (ctx.getAD_Client_ID(), ctx.getAD_Org_ID(),
				M_Product_ID.intValue(), C_BPartner_ID, Qty, IsSOTrx);

		setPriceListVersion(ctx, WindowNo, mTab);
		int M_PriceList_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_ID");
		pp.setM_PriceList_ID(M_PriceList_ID);
		MPriceList pl = MPriceList.get(ctx, M_PriceList_ID, null);
		Timestamp orderDate = (Timestamp)mTab.getValue("DateOrdered");
		pp.setPriceDate(orderDate);
		int M_PriceList_Version_ID = pl.getPriceListVersion(orderDate).getM_PriceList_Version_ID();
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);

		pp.setM_PriceList_ID(M_PriceList_ID);
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);


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
		mTab.setValue("Discount", pp.getDiscount());
		mTab.setValue("C_UOM_ID", Integer.valueOf(pp.getC_UOM_ID()));
		mTab.setValue("QtyOrdered", mTab.getValue("QtyEntered"));
		mTab.setValue("M_PriceList_Version_ID", Integer.valueOf(M_PriceList_Version_ID));
		ctx.setContext( WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");
		ctx.setContext( WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");

		//	Check/Update Warehouse Setting
		//	int M_Warehouse_ID = ctx.getContextAsInt( Env.WINDOW_INFO, "M_Warehouse_ID");
		//	Integer wh = (Integer)mTab.getValue("M_Warehouse_ID");
		//	if (wh.intValue() != M_Warehouse_ID)
		//	{
		//		mTab.setValue("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
		//		ADialog.warn(,WindowNo, "WarehouseChanged");
		//	}
		
		
		

		if (ctx.isSOTrx(WindowNo))
		{
			MProduct product = MProduct.get (ctx, M_Product_ID.intValue());
			if (product.isStocked())
			{
				BigDecimal QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
				int M_Warehouse_ID = ctx.getContextAsInt( WindowNo, "M_Warehouse_ID");
				int M_AttributeSetInstance_ID = ctx.getContextAsInt( WindowNo, "M_AttributeSetInstance_ID");
				BigDecimal available = Storage.getQtyAvailable
				(M_Warehouse_ID, M_Product_ID.intValue(), M_AttributeSetInstance_ID, null);
				if (available == null)
					available = Env.ZERO;
				if (available.signum() == 0)
					mTab.fireDataStatusEEvent ("NoQtyAvailable", "0", false);
				else if (available.compareTo(QtyOrdered) < 0)
					mTab.fireDataStatusEEvent ("InsufficientQtyAvailable", available.toString(), false);
				else
				{
					Integer C_OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
					if (C_OrderLine_ID == null)
						C_OrderLine_ID = Integer.valueOf(0);
					BigDecimal notReserved = MOrderLine.getNotReserved(ctx,
							M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID,
							C_OrderLine_ID.intValue());
					if (notReserved == null)
						notReserved = Env.ZERO;
					BigDecimal total = available.subtract(notReserved);
					if (total.compareTo(QtyOrdered) < 0)
					{
						String info = Msg.parseTranslation(ctx, "@QtyAvailable@=" + available
								+ " - @QtyNotReserved@=" + notReserved + " = " + total);
						mTab.fireDataStatusEEvent ("InsufficientQtyAvailable",
								info, false);
					}
				}
			}
		}
		else
		{
			String sql = "SELECT po.C_UOM_ID "
				+ "FROM M_Product_PO po WHERE po.M_Product_ID = ? and po.IsActive='Y'" ;
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement (sql, (Trx) null);
				pstmt.setInt (1, M_Product_ID);
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					Integer ii = Integer.valueOf(rs.getInt(1));
					if (ii!=0)
						mTab.setValue("C_UOM_ID", ii);
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
		}
		//
		setCalloutActive(false);
		if (steps) log.warning("fini");
		return tax (ctx, WindowNo, mTab, mField, value);
	}	//	product

	/*************************************************************************
	 *	Order Line - Warehouse.
	 *		- Checks the availability of product in this warehouse
	 *	Calls Tax
	 *
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String lineWarehouse (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_Warehouse_ID = (Integer)value;
		if (M_Warehouse_ID == null || M_Warehouse_ID.intValue() == 0)
			return "";

		Integer M_Product_ID = ctx.getContextAsInt( WindowNo, "M_Product_ID");
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";

		boolean isReturnTrx = "Y".equals(ctx.getContext(WindowNo,"IsReturnTrx"));
		if (isReturnTrx)
			return "";

		setCalloutActive(true);
		if (steps) log.warning("init");

		if (ctx.isSOTrx(WindowNo))
		{
			MProduct product = MProduct.get (ctx, M_Product_ID.intValue());
			if (product.isStocked())
			{
				BigDecimal QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
				int M_AttributeSetInstance_ID = ctx.getContextAsInt( WindowNo, "M_AttributeSetInstance_ID");
				BigDecimal available = Storage.getQtyAvailable
				(M_Warehouse_ID, M_Product_ID.intValue(), M_AttributeSetInstance_ID, null);
				if (available == null)
					available = Env.ZERO;
				if (available.signum() == 0)
					mTab.fireDataStatusEEvent ("NoQtyAvailable", "0", false);
				else if (available.compareTo(QtyOrdered) < 0)
					mTab.fireDataStatusEEvent ("InsufficientQtyAvailable", available.toString(), false);
				else
				{
					Integer C_OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
					if (C_OrderLine_ID == null)
						C_OrderLine_ID = Integer.valueOf(0);
					BigDecimal notReserved = MOrderLine.getNotReserved(ctx,
							M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID,
							C_OrderLine_ID.intValue());
					if (notReserved == null)
						notReserved = Env.ZERO;
					BigDecimal total = available.subtract(notReserved);
					if (total.compareTo(QtyOrdered) < 0)
					{
						String info = Msg.parseTranslation(ctx, "@QtyAvailable@=" + available
								+ " - @QtyNotReserved@=" + notReserved + " = " + total);
						mTab.fireDataStatusEEvent ("InsufficientQtyAvailable",
								info, false);
					}
				}
			}
		}
		setCalloutActive(false);
		if (steps) log.warning("fini");
		return tax (ctx, WindowNo, mTab, mField, value);
	}	//	lineWarehouse

	/**
	 *	Order Line - Charge.
	 * 		- updates PriceActual from Charge
	 * 		- sets PriceLimit, PriceList to zero
	 * 	Calles tax
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String charge (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_Charge_ID = (Integer)value;
		if (C_Charge_ID == null || C_Charge_ID.intValue() == 0) {
			mTab.setValue("QtyOrdered", BigDecimal.ONE);
			mTab.setValue("QtyEntered", BigDecimal.ONE);
			mTab.setValue("PriceList", BigDecimal.ZERO);
			mTab.setValue("PriceLimit", BigDecimal.ZERO);
			mTab.setValue("PriceActual", BigDecimal.ZERO);
			mTab.setValue("PriceEntered", BigDecimal.ZERO);	
			mTab.setValue("Discount",BigDecimal.ZERO);				
			mTab.setValue("C_UOM_ID", MUOM.Each_ID);	
			mTab.setValue("LineNetAmt", BigDecimal.ZERO);

			return "";
		}

		boolean isReturnTrx = "Y".equals(ctx.getContext("IsReturnTrx"));
		if (isReturnTrx) {
			return "";
		}

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
			log.log(Level.SEVERE, sql, e);
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
	 *	Order Line - Tax.
	 *		- basis: Product, Charge, BPartner Location
	 *		- sets C_Tax_ID
	 *  Calles Amount
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String tax (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String column = mField.getColumnName();
		if (value == null)
			return "";
		if (steps) log.warning("init");

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
			return amt(ctx, WindowNo, mTab, mField, value);		//

		//	Check Partner Location
		int shipC_BPartner_Location_ID = 0;
		if (column.equals("C_BPartner_Location_ID"))
			shipC_BPartner_Location_ID = ((Integer)value).intValue();
		else
			shipC_BPartner_Location_ID = ctx.getContextAsInt( WindowNo, "C_BPartner_Location_ID");
		if (shipC_BPartner_Location_ID == 0)
			return amt(ctx, WindowNo, mTab, mField, value);		//
		log.fine("Ship BP_Location=" + shipC_BPartner_Location_ID);

		//
		Timestamp billDate = new Timestamp(ctx.getContextAsTime( WindowNo, "DateOrdered"));
		log.fine("Bill Date=" + billDate);

		Timestamp shipDate = new Timestamp(ctx.getContextAsTime( WindowNo, "DatePromised"));
		log.fine("Ship Date=" + shipDate);

		int AD_Org_ID = ctx.getContextAsInt( WindowNo, "AD_Org_ID");
		log.fine("Org=" + AD_Org_ID);

		int M_Warehouse_ID = ctx.getContextAsInt( WindowNo, "M_Warehouse_ID");
		log.fine("Warehouse=" + M_Warehouse_ID);

		int billC_BPartner_Location_ID = ctx.getContextAsInt( WindowNo, "Bill_Location_ID");
		if (billC_BPartner_Location_ID == 0)
			billC_BPartner_Location_ID = shipC_BPartner_Location_ID;
		log.fine("Bill BP_Location=" + billC_BPartner_Location_ID);

		//
		int C_Tax_ID = Tax.get (ctx, M_Product_ID, C_Charge_ID, billDate, shipDate,
				AD_Org_ID, M_Warehouse_ID, billC_BPartner_Location_ID, shipC_BPartner_Location_ID,
				"Y".equals(ctx.getContext( WindowNo, "IsSOTrx")));
		log.info("Tax ID=" + C_Tax_ID);
		//
		if (C_Tax_ID == 0)
			mTab.fireDataStatusEEvent(CLogger.retrieveError());
		else
			mTab.setValue("C_Tax_ID", Integer.valueOf(C_Tax_ID));
		//
		if (steps) log.warning("fini");
		return amt(ctx, WindowNo, mTab, mField, value);
	}	//	tax


	/**
	 *	Order Line - Amount.
	 *		- called from QtyOrdered, Discount and PriceActual
	 *		- calculates Discount or Actual Amount
	 *		- calculates LineNetAmt
	 *		- enforces PriceLimit
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String amt (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);


		if (steps) log.warning("init");

		int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
		int M_Product_ID = ctx.getContextAsInt( WindowNo, "M_Product_ID");
		int M_PriceList_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_ID");
		int StdPrecision = MPriceList.getPricePrecision(ctx, M_PriceList_ID);
		BigDecimal QtyEntered, QtyOrdered, PriceEntered, PriceActual, PriceLimit, Discount, PriceList;
		//	get values
		QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
		QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
		log.fine("QtyEntered=" + QtyEntered + ", Ordered=" + QtyOrdered + ", UOM=" + C_UOM_To_ID);
		//
		PriceEntered = (BigDecimal)mTab.getValue("PriceEntered");
		PriceActual = (BigDecimal)mTab.getValue("PriceActual");
		Discount = (BigDecimal)mTab.getValue("Discount");
		PriceLimit = (BigDecimal)mTab.getValue("PriceLimit");
		PriceList = (BigDecimal)mTab.getValue("PriceList");
		log.fine("PriceList=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
		log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual + ", Discount=" + Discount);

		//	Qty changed - recalc price
		if ((mField.getColumnName().equals("QtyOrdered")
				|| mField.getColumnName().equals("QtyEntered")
				|| mField.getColumnName().equals("M_Product_ID"))
				&& !"N".equals(ctx.getContext( WindowNo, "DiscountSchema")))
		{
			int C_BPartner_ID = ctx.getContextAsInt( WindowNo, "C_BPartner_ID");
			if (mField.getColumnName().equals("QtyEntered"))
				QtyOrdered = MUOMConversion.convertProductTo (ctx, M_Product_ID,
						C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean IsSOTrx = ctx.getContext( WindowNo, "IsSOTrx").equals("Y");
			MProductPricing pp = new MProductPricing (ctx.getAD_Client_ID(), ctx.getAD_Org_ID(),
					M_Product_ID, C_BPartner_ID, QtyOrdered, IsSOTrx);
			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			Timestamp date = (Timestamp)mTab.getValue("DateOrdered");
			pp.setPriceDate(date);
			//
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, pp.getPriceStd());
			if (PriceEntered == null)
				PriceEntered = pp.getPriceStd();
			//
			log.fine("QtyChanged -> PriceActual=" + pp.getPriceStd()
					+ ", PriceEntered=" + PriceEntered + ", Discount=" + pp.getDiscount());
			PriceActual = pp.getPriceStd();
			mTab.setValue("PriceActual", PriceActual);
			mTab.setValue("Discount", pp.getDiscount());
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
			log.fine("PriceActual=" + PriceActual
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
			log.fine("PriceEntered=" + PriceEntered
					+ " -> PriceActual=" + PriceActual);
			mTab.setValue("PriceActual", PriceActual);
		}

		//  Discount entered - Calculate Actual/Entered
		if (mField.getColumnName().equals("Discount"))
		{
			//Do not apply discount for charge lines
			if(!(mTab.getValue("C_Charge_ID") != null))
			{
				PriceActual = new BigDecimal ((100.0 - Discount.doubleValue())
						/ 100.0 * PriceList.doubleValue());
				if (PriceActual.scale() > StdPrecision)
					PriceActual = PriceActual.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
				PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
						C_UOM_To_ID, PriceActual);
				if (PriceEntered == null)
					PriceEntered = PriceActual;
				mTab.setValue("PriceActual", PriceActual);
				mTab.setValue("PriceEntered", PriceEntered);
			}
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
		log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual + ", Discount=" + Discount);

		//	Check PriceLimit
		String epl = ctx.getContext( WindowNo, "EnforcePriceLimit");
		boolean enforce = ctx.isSOTrx(WindowNo) && epl != null && epl.equals("Y");
		boolean isReturnTrx = "Y".equals(ctx.getContext(WindowNo, "IsReturnTrx"));
		if (enforce && (MRole.getDefault().isOverwritePriceLimit() || isReturnTrx))
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
			log.fine("(under) PriceEntered=" + PriceEntered + ", Actual" + PriceLimit);
			mTab.setValue ("PriceActual", PriceLimit);
			mTab.setValue ("PriceEntered", PriceEntered);
			mTab.fireDataStatusEEvent ("UnderLimitPrice", "", false);
			//	Repeat Discount calc
			if (PriceList.intValue() != 0)
			{
				Discount = new BigDecimal ((PriceList.doubleValue () - PriceActual.doubleValue ()) / PriceList.doubleValue () * 100.0);
				if (Discount.scale () > 2)
					Discount = Discount.setScale (2, BigDecimal.ROUND_HALF_UP);
				mTab.setValue ("Discount", Discount);
			}
		}

		//	Line Net Amt
		BigDecimal LineNetAmt = QtyOrdered.multiply(PriceActual);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		log.info("LineNetAmt=" + LineNetAmt);
		mTab.setValue("LineNetAmt", LineNetAmt);
		//
		setCalloutActive(false);
		return "";
	}	//	amt

	/**
	 * 	Get InOut open quantity
	 *	@param ctx context
	 *	@param M_InOutLine_ID shipment line
	 *	@param excludeC_OrderLine_ID exclude C_OrderLine_ID
	 *	@return QtyReturn
	 */
	public BigDecimal getInOutOpenQty(Ctx ctx, int M_InOutLine_ID, int C_Order_ID, int excludeC_OrderLine_ID)
	{
		MInOutLine inOutLine = new MInOutLine (ctx, M_InOutLine_ID, null);
		BigDecimal shippedQty = inOutLine.getMovementQty();

		BigDecimal retValue = Env.ZERO;
		String sql = "SELECT SUM(QtyOrdered) "
			+ "FROM C_OrderLine ol"
			+ " INNER JOIN C_Order o ON (ol.C_Order_ID=o.C_Order_ID) "
			+ "WHERE ol.M_InOutLine_ID=?"	//	#1
			+ " AND o.IsReturnTrx='Y' "
			+ " AND (o.DocStatus IN ('CO','CL') OR o.C_Order_ID=?) "
			+ " AND ol.C_OrderLine_ID<>?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, M_InOutLine_ID);
			pstmt.setInt (2, C_Order_ID);
			pstmt.setInt (3, excludeC_OrderLine_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = rs.getBigDecimal(1);
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		if (retValue == null)
			log.fine("-");
		else
			log.fine(retValue.toString());

		if (retValue != null)
			return shippedQty.subtract(retValue);

		return shippedQty;
	}	//	getQtyRMA

	/**
	 *	Order Line - Quantity.
	 *		- called from C_UOM_ID, QtyEntered, QtyOrdered
	 *		- enforces qty UOM relationship
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String qty (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int M_Product_ID = ctx.getContextAsInt( WindowNo, "M_Product_ID");
		if (steps) log.warning("init - M_Product_ID=" + M_Product_ID + " - " );
		BigDecimal QtyOrdered = Env.ZERO;
		BigDecimal QtyEntered = Env.ZERO;
		BigDecimal PriceActual, PriceEntered;

		// Check for RMA
		boolean isReturnTrx = "Y".equals(ctx.getContext(WindowNo, "IsReturnTrx"));

		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			QtyOrdered = QtyEntered;
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
						+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			PriceActual = (BigDecimal)mTab.getValue("PriceActual");
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			log.fine("UOM=" + C_UOM_To_ID
					+ ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
					+ " -> " + conversion
					+ " QtyOrdered/PriceEntered=" + QtyOrdered + "/" + PriceEntered);
			ctx.setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		//	QtyEntered changed - calculate QtyOrdered
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
			QtyEntered = (BigDecimal)value;
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
						+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			log.fine("UOM=" + C_UOM_To_ID
					+ ", QtyEntered=" + QtyEntered
					+ " -> " + conversion
					+ " QtyOrdered=" + QtyOrdered);
			ctx.setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	QtyOrdered changed - calculate QtyEntered (should not happen)
		else if (mField.getColumnName().equals("QtyOrdered"))
		{
			int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
			QtyOrdered = (BigDecimal)value;
			int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
			BigDecimal QtyOrdered1 = QtyOrdered.setScale(precision, BigDecimal.ROUND_HALF_UP);
			if (QtyOrdered.compareTo(QtyOrdered1) != 0)
			{
				log.fine("Corrected QtyOrdered Scale "
						+ QtyOrdered + "->" + QtyOrdered1);
				QtyOrdered = QtyOrdered1;
				mTab.setValue("QtyOrdered", QtyOrdered);
			}
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID,
					C_UOM_To_ID, QtyOrdered);
			if (QtyEntered == null)
				QtyEntered = QtyOrdered;
			boolean conversion = QtyOrdered.compareTo(QtyEntered) != 0;
			log.fine("UOM=" + C_UOM_To_ID
					+ ", QtyOrdered=" + QtyOrdered
					+ " -> " + conversion
					+ " QtyEntered=" + QtyEntered);
			ctx.setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}
		else
		{
			//	QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
		}

		if(M_Product_ID != 0
				&& isReturnTrx)
		{
			Integer inOutLine_ID = (Integer)mTab.getValue("Orig_InOutLine_ID");
			if(inOutLine_ID != 0)
			{
				Integer C_Order_ID = (Integer)mTab.getValue("C_Order_ID");
				if (C_Order_ID == null)
					C_Order_ID = Integer.valueOf(0);
				Integer C_OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
				if (C_OrderLine_ID == null)
					C_OrderLine_ID = Integer.valueOf(0);
				BigDecimal shippedQty = MOrderLine.getInOutOpenQty(ctx, inOutLine_ID, C_Order_ID, C_OrderLine_ID);

				QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
				if(shippedQty.compareTo(QtyOrdered)<0)
				{
					if(ctx.isSOTrx(WindowNo))
						mTab.fireDataStatusEEvent ("ReturnQtyExceedsShippedQty", "", false);
					else
						mTab.fireDataStatusEEvent ("ReturnQtyExceedsReceivedQty", "", false);
					mTab.setValue ("QtyOrdered",shippedQty);
					QtyOrdered = shippedQty;

					int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
					QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID,
							C_UOM_To_ID, QtyOrdered);
					if (QtyEntered == null)
						QtyEntered = QtyOrdered;
					mTab.setValue ("QtyEntered",QtyEntered);
					log.fine("QtyEntered : "+ QtyEntered.toString() +
							"QtyOrdered : " + QtyOrdered.toString());
				}
			}
		}

		//	Storage
		if (M_Product_ID != 0
				&& ctx.isSOTrx(WindowNo)
				&& QtyOrdered.signum() > 0
				&& !isReturnTrx)		//	no negative (returns)
		{
			MProduct product = MProduct.get (ctx, M_Product_ID);
			if (product.isStocked())
			{
				int M_Warehouse_ID = ctx.getContextAsInt( WindowNo, "M_Warehouse_ID");
				int M_AttributeSetInstance_ID = ctx.getContextAsInt( WindowNo, "M_AttributeSetInstance_ID");
				BigDecimal available = Storage.getQtyAvailable
				(M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID, null);
				if (available == null)
					available = Env.ZERO;
				if (available.signum() == 0)
					mTab.fireDataStatusEEvent ("NoQtyAvailable", "0", false);
				else if (available.compareTo(QtyOrdered) < 0)
					mTab.fireDataStatusEEvent ("InsufficientQtyAvailable", available.toString(), false);
				else
				{
					Integer C_OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
					if (C_OrderLine_ID == null)
						C_OrderLine_ID = Integer.valueOf(0);
					BigDecimal notReserved = MOrderLine.getNotReserved(ctx,
							M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID,
							C_OrderLine_ID.intValue());
					if (notReserved == null)
						notReserved = Env.ZERO;
					BigDecimal total = available.subtract(notReserved);
					if (total.compareTo(QtyOrdered) < 0)
					{
						String info = Msg.parseTranslation(ctx, "@QtyAvailable@=" + available
								+ "  -  @QtyNotReserved@=" + notReserved + "  =  " + total);
						mTab.fireDataStatusEEvent ("InsufficientQtyAvailable",
								info, false);
					}
				}
			}
		}

		//
		setCalloutActive(false);
		return "";
	}	//	qty

	/**
	 * 	Orig_Order - Orig Order Defaults.
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String Orig_Order (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_Order_ID = (Integer)value;
		if (C_Order_ID == null || C_Order_ID.intValue() == 0)
			return "";

		//	Get Details from Original Order
		MOrder order = new MOrder (ctx, C_Order_ID, null);

		// Reset Orig Shipment
		mTab.setValue("Orig_InOut_ID", null);
		mTab.setValue("POReference", order.getPOReference());//setting POreference
		mTab.setValue("C_BPartner_ID", order.getC_BPartner_ID());
		mTab.setValue("C_BPartner_Location_ID", order.getC_BPartner_Location_ID());
		mTab.setValue("Bill_BPartner_ID",order.getBill_BPartner_ID());
		mTab.setValue("Bill_Location_ID", order.getBill_Location_ID());
		mTab.setValue("M_Warehouse_ID", order.getM_Warehouse_ID());

		if(order.getAD_User_ID()!=0)
			mTab.setValue("AD_User_ID", order.getAD_User_ID());

		if(order.getBill_User_ID()!=0)
			mTab.setValue("Bill_User_ID", order.getBill_User_ID());

		//mTab.setValue("DateOrdered", order.getDateOrdered());
		mTab.setValue("M_PriceList_ID", order.getM_PriceList_ID());
		mTab.setValue("PaymentRule", order.getPaymentRule());
		mTab.setValue("C_PaymentTerm_ID", order.getC_PaymentTerm_ID());
		//mTab.setValue ("DeliveryRule", X_C_Order.DELIVERYRULE_Manual);

		mTab.setValue("Bill_Location_ID", order.getBill_Location_ID());
		mTab.setValue("InvoiceRule", order.getInvoiceRule());
		mTab.setValue("PaymentRule", order.getPaymentRule());
		
		String deliveryviarule =order.getDeliveryViaRule();
		{
		mTab.setValue("DeliveryViaRule",deliveryviarule );
		
		if (deliveryviarule.equals(X_C_Order.DELIVERYVIARULE_Shipper))
			mTab.setValue("M_Shipper_ID",order.getM_Shipper_ID() );
		}
		
		mTab.setValue("FreightCostRule",order.getFreightCostRule());

		return "";
	} /* Orig Order */

	/**
	 * 	Orig_InOut - Shipment Line Defaults.
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String Orig_InOut (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer Orig_InOut_ID = (Integer)value;
		if (Orig_InOut_ID == null || Orig_InOut_ID.intValue() == 0)
			return "";

		//	Get Details from Original Shipment
		MInOut io = new MInOut (ctx, Orig_InOut_ID, null);
		mTab.setValue("C_Project_ID", io.getC_Project_ID());
		mTab.setValue("C_Campaign_ID", io.getC_Campaign_ID());
		mTab.setValue("C_Activity_ID", io.getC_Activity_ID());
		mTab.setValue("AD_OrgTrx_ID", io.getAD_OrgTrx_ID());
		mTab.setValue("User1_ID", io.getUser1_ID());
		mTab.setValue("User2_ID", io.getUser2_ID());

		return "";
	}

	/**
	 * 	Orig_Order - Orig Order Defaults.
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String Orig_OrderLine (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer Orig_OrderLine_ID = (Integer)value;
		if (Orig_OrderLine_ID == null || Orig_OrderLine_ID.intValue() == 0)
			return "";

		MOrderLine orderline = new MOrderLine (ctx, Orig_OrderLine_ID, null);
		mTab.setValue("Orig_InOutLine_ID", null);
		mTab.setValue("C_Tax_ID", orderline.getC_Tax_ID());
		mTab.setValue("PriceList", orderline.getPriceList());
		mTab.setValue("PriceLimit", orderline.getPriceLimit());
		mTab.setValue("PriceActual", orderline.getPriceActual());
		mTab.setValue("PriceEntered", orderline.getPriceEntered());
		mTab.setValue("C_Currency_ID", orderline.getC_Currency_ID());
		mTab.setValue("Discount", orderline.getDiscount());
		mTab.setValue("Discount",orderline.getDiscount());

		return "";

	}

	/**
	 * 	Orig_InOutLine - Shipment Line Defaults.
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String Orig_InOutLine (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer Orig_InOutLine_ID = (Integer)value;
		if (Orig_InOutLine_ID == null || Orig_InOutLine_ID.intValue() == 0)
			return "";

		//	Get Details
		MInOutLine Orig_InOutLine = new MInOutLine (ctx, Orig_InOutLine_ID.intValue(), null);

		if (Orig_InOutLine != null && Orig_InOutLine.get_ID()!=0)
		{
			mTab.setValue("C_Project_ID",Orig_InOutLine.getC_Project_ID());
			mTab.setValue("C_Campaign_ID",Orig_InOutLine.getC_Campaign_ID());
			mTab.setValue("M_Product_ID", Orig_InOutLine.getM_Product_ID());
			mTab.setValue("M_AttributeSetInstance_ID", Orig_InOutLine.getM_AttributeSetInstance_ID());
			mTab.setValue("C_UOM_ID", Orig_InOutLine.getC_UOM_ID());

		}

		return "";
	}	//	Orig_InOutLine
	
	public String ADOrg (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer AD_Org_ID = (Integer)value;
		if (AD_Org_ID == null || AD_Org_ID.intValue() == 0)
			return "";
		if(mTab.getValue("M_Warehouse_ID") != null)
			return "";
		int M_Warehouse_ID = MOrg.get(ctx, AD_Org_ID).getM_Warehouse_ID();
		if(M_Warehouse_ID == 0)
		{
			MWarehouse[] war = MWarehouse.getForOrg(ctx, AD_Org_ID);
			if(war.length >0)
				M_Warehouse_ID = war[0].getM_Warehouse_ID();
		}
		
		if(M_Warehouse_ID !=0)
			mTab.setValue("M_Warehouse_ID", M_Warehouse_ID);
		
		return "";
	}

}	//	CalloutOrder

