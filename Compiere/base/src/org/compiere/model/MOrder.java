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
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.compiere.api.UICallout;
import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.common.constants.EnvConstants;
import org.compiere.framework.PO;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
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
import org.compiere.util.QueryUtil.Callback;
import org.compiere.vos.DocActionConstants;

import com.compiere.client.SysEnv;

/**
 *  Order Model.
 * 	Please do not set DocStatus and C_DocType_ID directly.
 * 	They are set in the process() method.
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MOrder.java,v 1.5 2006/10/06 00:42:24 jjanke Exp $
 */
public class MOrder extends X_C_Order implements DocAction
{
    /** Logger for class MOrder */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MOrder.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Create new Order by copying
	 * 	@param from order
	 * 	@param dateDoc date of the document date
	 * 	@param C_DocTypeTarget_ID target document type
	 * 	@param isSOTrx sales order
	 * 	@param counter create counter links
	 *	@param copyASI copy line attributes Attribute Set Instance, Resource Assignment
	 * 	@param trx p_trx
	 *	@return Order
	 */
	public static MOrder copyFrom (MOrder from, Timestamp dateDoc,
			int C_DocTypeTarget_ID,
			boolean counter, boolean copyASI, Trx trx)
	{
		MOrder to = new MOrder (from.getCtx(), 0, trx);
		to.set_Trx(trx);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("C_Order_ID", I_ZERO);
		to.set_ValueNoCheck ("DocumentNo", null);
		//
		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		to.setDocAction(DOCACTION_Complete);
		//
		to.setC_DocType_ID(0);
		to.setC_DocTypeTarget_ID (C_DocTypeTarget_ID, true);
		//
		to.setIsSelected (false);
		to.setDateOrdered (dateDoc);
		to.setDateAcct (dateDoc);
		to.setDatePromised (dateDoc);	//	assumption
		to.setDatePrinted(null);
		to.setIsPrinted (false);
		//
		to.setIsApproved (false);
		to.setIsCreditApproved(false);
		to.setC_Payment_ID(0);
		to.setC_CashLine_ID(0);
		//	Amounts are updated  when adding lines
		to.setGrandTotal(Env.ZERO);
		to.setTotalLines(Env.ZERO);
		//
		to.setIsDelivered(false);
		to.setIsInvoiced(false);
		to.setIsSelfService(false);
		to.setIsTransferred (false);
		to.setPosted (false);
		to.setProcessed (false);
		if (counter)
			to.setRef_Order_ID(from.getC_Order_ID());
		else
			to.setRef_Order_ID(0);
		//
		if (!to.save(trx))
			throw new CompiereStateException("Could not create Order");
		if (counter)
			from.setRef_Order_ID(to.getC_Order_ID());

		if (to.copyLinesFrom(from, counter, copyASI) == 0)
			throw new CompiereStateException("Could not create Order Lines");

		return to;
	}	//	copyFrom


	/**************************************************************************
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Order_ID    order to load, (0 create new order)
	 *  @param trx p_trx name
	 */
	public MOrder(Ctx ctx, int C_Order_ID, Trx trx)
	{
		super (ctx, C_Order_ID, trx);
		//  New
		if (C_Order_ID == 0)
		{
			setDocStatus(DOCSTATUS_Drafted);
			setDocAction (DOCACTION_Prepare);
			//
			setDeliveryRule (DELIVERYRULE_Availability);
			setFreightCostRule (FREIGHTCOSTRULE_FreightIncluded);
			setInvoiceRule (INVOICERULE_Immediate);
			setPaymentRule(PAYMENTRULE_OnCredit);
			setPriorityRule (PRIORITYRULE_Medium);
			setDeliveryViaRule (DELIVERYVIARULE_Pickup);
			//
			setIsDiscountPrinted (false);
			setIsSelected (false);
			setIsTaxIncluded (false);
			setIsSOTrx (true);
			setIsDropShip(false);
			setSendEMail (false);
			//
			setIsApproved(false);
			setIsPrinted(false);
			setIsCreditApproved(false);
			setIsDelivered(false);
			setIsInvoiced(false);
			setIsTransferred(false);
			setIsSelfService(false);
			setIsReturnTrx(false);
			//
			super.setProcessed(false);
			setProcessing(false);
			setPosted(false);

			setDateAcct (new Timestamp(System.currentTimeMillis()));
			setDatePromised (new Timestamp(System.currentTimeMillis()));
			setDateOrdered (new Timestamp(System.currentTimeMillis()));

			setFreightAmt (Env.ZERO);
			setChargeAmt (Env.ZERO);
			setTotalLines (Env.ZERO);
			setGrandTotal (Env.ZERO);
		}
	}	//	MOrder

	public MOrder(X_I_Order imp)
	{
		this (imp.getCtx(), 0, imp.get_Trx());
		PO.copyValues(imp, this, imp.getAD_Client_ID(),imp.getAD_Org_ID());
		

		setC_DocTypeTarget_ID(imp.getC_DocType_ID());
		setM_PriceList_ID(imp.getM_PriceList_ID());
		
		if(imp.getDocumentNo() !=null && imp.getDocumentNo().length()!=0)
			setDocumentNo(imp.getDocumentNo());
		if(getBill_BPartner_ID() == 0)
			setBill_BPartner_ID(imp.getC_BPartner_ID());

		setBill_Location_ID(imp.getBillTo_ID());
		if(getBill_User_ID() == 0 && getAD_User_ID() != 0)
			setBill_User_ID(getAD_User_ID());
		if(getDateAcct() == null)
			setDateAcct (new Timestamp(System.currentTimeMillis()));
		if(getDatePromised() == null)
			setDatePromised (new Timestamp(System.currentTimeMillis()));
		if(getDateOrdered() == null)
			setDateOrdered (new Timestamp(System.currentTimeMillis()));
		
		if( getPaymentRule() == null )
		{
			String docTypeSql = "SELECT d.DocSubTypeSO "
								+ "FROM C_DocType d "
								+ "LEFT OUTER JOIN AD_Sequence s ON (d.DocNoSequence_ID=s.AD_Sequence_ID) "
								+ "WHERE C_DocType_ID=?";
			String docSubTypeSO = QueryUtil.getSQLValueString(get_Trx(), docTypeSql, imp.getC_DocType_ID());
			if (docSubTypeSO != null && docSubTypeSO.equals(MOrder.DocSubTypeSO_POS))								
				setPaymentRule(X_C_Order.PAYMENTRULE_Cash);
			else
				setPaymentRule(X_C_Order.PAYMENTRULE_OnCredit);
		}
	}	//	MOrder

	/**************************************************************************
	 *  Project Constructor
	 *  @param  project Project to create Order from
	 *  @param IsSOTrx sales order
	 * 	@param	DocSubTypeSO if SO DocType Target (default DocSubTypeSO_OnCredit)
	 */
	public MOrder (MProject project, boolean IsSOTrx, String DocSubTypeSO)
	{
		this (project.getCtx(), 0, project.get_Trx());
		setAD_Client_ID(project.getAD_Client_ID());
		setAD_Org_ID(project.getAD_Org_ID());
		setC_Campaign_ID(project.getC_Campaign_ID());
		setSalesRep_ID(project.getSalesRep_ID());
		//
		setC_Project_ID(project.getC_Project_ID());
		setDescription(project.getName());
		Timestamp ts = project.getDateContract();
		if (ts != null)
			setDateOrdered (ts);
		ts = project.getDateFinish();
		if (ts != null)
			setDatePromised (ts);
		//
		setC_BPartner_ID(project.getC_BPartner_ID());
		setC_BPartner_Location_ID(project.getC_BPartner_Location_ID());
		setAD_User_ID(project.getAD_User_ID());
		//
		setM_Warehouse_ID(project.getM_Warehouse_ID());
		setM_PriceList_ID(project.getM_PriceList_ID());
		setC_PaymentTerm_ID(project.getC_PaymentTerm_ID());
		//
		setIsSOTrx(IsSOTrx);
		if (IsSOTrx)
		{
			if ((DocSubTypeSO == null) || (DocSubTypeSO.length() == 0))
				setC_DocTypeTarget_ID(DocSubTypeSO_OnCredit);
			else
				setC_DocTypeTarget_ID(DocSubTypeSO);
		}
		else
			setC_DocTypeTarget_ID();
	}	//	MOrder

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trx transaction
	 */
	public MOrder (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MOrder

	/** Force Creation of order		*/
	private boolean			m_forceCreation = false;

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
	 * 	Set Business Partner (Ship+Bill)
	 *	@param C_BPartner_ID bpartner
	 */
	@Override
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		super.setC_BPartner_ID (C_BPartner_ID);
		super.setBill_BPartner_ID (C_BPartner_ID);
	}	//	setC_BPartner_ID

	/**
	 * 	Set Business Partner Defaults & Details.
	 * 	SOTrx should be set.
	 * 	@param bp business partner
	 */
	public void setBPartner (MBPartner bp)
	{
		if ((bp == null) || !bp.isActive())
			return;

		setC_BPartner_ID(bp.getC_BPartner_ID());
		//	Defaults Payment Term
		int ii = 0;
		if (isSOTrx())
			ii = bp.getC_PaymentTerm_ID();
		else
			ii = bp.getPO_PaymentTerm_ID();
		if (ii != 0)
			setC_PaymentTerm_ID(ii);
		else
			setC_PaymentTerm_ID(getCtx().getContextAsInt("#C_PaymentTerm_ID"));
		//	Default Price List
		if (isSOTrx())
			ii = bp.getM_PriceList_ID();
		else
			ii = bp.getPO_PriceList_ID();
		if (ii != 0)
			setM_PriceList_ID(ii);
		//	Default Delivery/Via Rule
		String ss = bp.getDeliveryRule();
		if (ss != null)
			setDeliveryRule(ss);
		ss = bp.getDeliveryViaRule();
		if (ss != null)
		{
			setDeliveryViaRule(ss);
			if (ss.equals(X_C_Order.DELIVERYVIARULE_Shipper))
				setM_Shipper_ID(bp.getM_Shipper_ID());
		}
		
		//	Default Invoice/Payment Rule
		ss = bp.getInvoiceRule();
		if (ss != null)
			setInvoiceRule(ss);
		if (isSOTrx())
			ss = bp.getPaymentRule();
		else
			ss = bp.getPaymentRulePO();
		if (ss != null)
			setPaymentRule(ss);
		//	Sales Rep
		ii = bp.getSalesRep_ID();
		if (ii != 0)
			setSalesRep_ID(ii);

		setBPartnerLocation(bp);
		
		//	Set Contact
		MUser[] contacts = bp.getContacts(false);
		if ((contacts != null) && (contacts.length == 1))
			setAD_User_ID(contacts[0].getAD_User_ID());
		//
		setC_Project_ID(0);
		
	}	//	setBPartner

	/**
	 * 	Set Business Partner - Callout
	 *	@param oldC_BPartner_ID old BP
	 *	@param newC_BPartner_ID new BP
	 *	@param windowNo window no
	 */
	@UICallout public void setC_BPartner_ID (String oldC_BPartner_ID,
			String newC_BPartner_ID, int windowNo) throws Exception
			{
		if ((newC_BPartner_ID == null) || (newC_BPartner_ID.length() == 0))
			return;
		int C_BPartner_ID = Integer.parseInt(newC_BPartner_ID);
		if (C_BPartner_ID == 0)
			return;


		// Skip these steps for RMA. These fields are copied over from the orignal order instead.
		if (isReturnTrx())
		{
			setM_ReturnPolicy_ID();
			return;
		}

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
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID) "
			+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'" 		//	#1
			+ " ORDER BY lbill.Name ASC,lship.Name ASC ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean IsSOTrx = isSOTrx();
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				super.setC_BPartner_ID(C_BPartner_ID);

				//	PriceList (indirect: IsTaxIncluded & Currency)
				int ii = rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID");
				if (ii != 0)
					setM_PriceList_ID(null, String.valueOf(ii), windowNo);
				else
				{	//	get default PriceList
					ii = getCtx().getContextAsInt("#M_PriceList_ID");
					if (ii != 0)
						setM_PriceList_ID(null, String.valueOf(ii), windowNo);
				}

				//	Bill-To BPartner
				setBill_BPartner_ID(C_BPartner_ID);

				//	Contact - overwritten by InfoBP selection
				int contID = rs.getInt("AD_User_ID");
				if (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID") == C_BPartner_ID)
				{
					String cont = getCtx().getContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contID = Integer.parseInt(cont);
				}
				setAD_User_ID(contID);
				setBill_User_ID(contID);

				/* If user logged in is not a SalesRep, default the SalesRep associated with
				 * the customer.
				 */
				if(!MUser.isSalesRep(getCtx().getAD_User_ID()) && IsSOTrx)
				{
					int SalesRep_ID=rs.getInt("SalesRep_ID");
					if(SalesRep_ID!=0)
						setSalesRep_ID(SalesRep_ID);
				}

				//	CreditAvailable
				if (IsSOTrx)
				{
					BigDecimal CreditLimit = rs.getBigDecimal("SO_CreditLimit");
					//	String SOCreditStatus = rs.getString("SOCreditStatus");
					if ((CreditLimit != null) && (CreditLimit.signum() != 0))
					{
						BigDecimal CreditAvailable = rs.getBigDecimal("CreditAvailable");
						if ((p_changeVO != null)
								&& (CreditAvailable != null) && (CreditAvailable.signum() < 0))
						{
							String msg = Msg.getMsg(getCtx(), "CreditLimitOver",
									DisplayType.getNumberFormat(DisplayTypeConstants.Amount).format(CreditAvailable));
							p_changeVO.addError(msg);
						}
					}
				}

				//	PO Reference
				//	Do not set if already present
				String s = rs.getString("POReference");
				String poRef = getPOReference();
				if ((s != null) && (s.length() != 0) && ((poRef ==null) || (poRef.trim().length() == 0)))
					if(IsSOTrx)		//Do Not set if Purchase Order
						setPOReference(s);

				//	SO Description
				//	Do not set if already present
				s = rs.getString("SO_Description");
				if ((s != null) && (s.trim().length() != 0) && ((poRef ==null) || (poRef.trim().length() == 0)))
					if(IsSOTrx)		//Do Not set if Purchase Order
						setDescription(s);
				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				setIsDiscountPrinted("Y".equals(s));

				//	Defaults, if not Walk-in Receipt or Walk-in Invoice
				String OrderType = getOrderTypeFromTargetDocType(windowNo);
				setInvoiceRule(INVOICERULE_AfterDelivery);
				setDeliveryRule(DELIVERYRULE_Availability);
				setPaymentRule(PAYMENTRULE_OnCredit);
				if (OrderType.equals(DocSubTypeSO_Prepay))
				{
					setInvoiceRule(INVOICERULE_Immediate);
					setDeliveryRule(DELIVERYRULE_AfterReceipt);
				}
				else if (OrderType.equals(MOrder.DocSubTypeSO_POS))	//  for POS
					setPaymentRule(PAYMENTRULE_Cash);
				else
				{
					//	PaymentRule
					s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if ((s != null) && (s.length() != 0))
					{
						if (s.equals("B"))				//	No Cache in Non POS
							s = PAYMENTRULE_OnCredit;	//  Payment Term
						if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
							s = PAYMENTRULE_OnCredit;	//  Payment Term
						setPaymentRule(s);
					}
					//	Payment Term
					ii = rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID");
					if (ii != 0)
						setC_PaymentTerm_ID(ii);
					else
						setC_PaymentTerm_ID(getCtx().getContextAsInt("#C_PaymentTerm_ID"));
					//	InvoiceRule
					s = rs.getString("InvoiceRule");
					if ((s != null) && (s.length() != 0))
						setInvoiceRule(s);
					//	DeliveryRule
					s = rs.getString("DeliveryRule");
					if ((s != null) && (s.length() != 0))
						setDeliveryRule(s);
					//	FreightCostRule
					s = rs.getString("FreightCostRule");
					if ((s != null) && (s.length() != 0))
						setFreightCostRule(s);
					
					//	DeliveryViaRule
					
						s = rs.getString("DeliveryViaRule");
						if ((s != null) && (s.length() != 0))
						{
							setDeliveryViaRule(s);						
							// Freight Carrier
							if (s.equals(X_C_Order.DELIVERYVIARULE_Shipper)) 
								setM_Shipper_ID(rs.getInt("M_Shipper_ID"));
						}
						
					}
				setC_Project_ID(0);
				boolean match = false, overwrite = false;
				int loc = 0;
				if (C_BPartner_ID==getCtx().getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID"))
					loc = getCtx().getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_Location_ID");
				int bill_Location_ID = 0, shipTo_ID = 0;
				if(loc>0)	//	valid location returned from the InfoBP selection
					do{
						// Ship-To Location
						shipTo_ID = rs.getInt("C_BPartner_Location_ID");
						bill_Location_ID = rs.getInt("Bill_Location_ID");
						//	overwritten by InfoBP selection - works only if InfoWindow
						//	was used otherwise creates error (uses last value, may belong to differnt BP)
						if (loc > 0 && (loc==shipTo_ID || loc==bill_Location_ID))
							match=true;
						overwrite = "Y".equals(rs.getString("ShipToIsBillTo"))?true:false;
					}while(rs.next() && !match);
				else{	//	no selection from InfoBP selection
					shipTo_ID = rs.getInt("C_BPartner_Location_ID");
					bill_Location_ID = rs.getInt("Bill_Location_ID");
					overwrite = "Y".equals(rs.getString("ShipToIsBillTo"));
				}

				if(bill_Location_ID == 0)
					p_changeVO.addChangedValue("Bill_Location_ID", (String)null);
				else{
					setBill_Location_ID(bill_Location_ID);
				}

				if (shipTo_ID == 0)
					p_changeVO.addChangedValue("C_BPartner_Location_ID", (String)null);
				else
				{
					super.setC_BPartner_Location_ID(shipTo_ID);
					if (overwrite)	//	set the same
						setBill_Location_ID(shipTo_ID);
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
	}	//	setC_BPartner_ID


	/**
	 * 	Set Bill Business Partner - Callout
	 *	@param oldBill_BPartner_ID old BP
	 *	@param newBill_BPartner_ID new BP
	 *	@param windowNo window no
	 */
	@UICallout public void setBill_BPartner_ID (String oldBill_BPartner_ID,
			String newBill_BPartner_ID, int windowNo) throws Exception
			{
		if ((newBill_BPartner_ID == null) || (newBill_BPartner_ID.length() == 0))
			return;
		int bill_BPartner_ID = Integer.parseInt(newBill_BPartner_ID);
		if (bill_BPartner_ID == 0)
			return;

		// Skip these steps for RMA. These fields are copied over from the orignal order instead.
		if (isReturnTrx())
			return;

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
			+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'"		//	#1
			+ " ORDER BY lbill.Name ";

		boolean IsSOTrx = isSOTrx();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, bill_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				super.setBill_BPartner_ID(bill_BPartner_ID);
				//	PriceList (indirect: IsTaxIncluded & Currency)
				int ii = rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID");
				if (ii != 0)
					setM_PriceList_ID(null, String.valueOf(ii), windowNo);
				else
				{	//	get default PriceList
					ii = getCtx().getContextAsInt("#M_PriceList_ID");
					if (ii != 0)
						setM_PriceList_ID(null, String.valueOf(ii), windowNo);
				}

				int bill_Location_ID = rs.getInt("Bill_Location_ID");
				//	overwritten by InfoBP selection - works only if InfoWindow
				//	was used otherwise creates error (uses last value, may belong to differnt BP)
				if (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID") == bill_BPartner_ID)
				{
					String loc = getCtx().getContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_Location_ID");
					if (loc.length() > 0)
						bill_Location_ID = Integer.parseInt(loc);
				}
				if (bill_Location_ID != 0)
					setBill_Location_ID(bill_Location_ID);

				//	Contact - overwritten by InfoBP selection
				int contID = rs.getInt("AD_User_ID");
				if (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID") == bill_BPartner_ID)
				{
					String cont = getCtx().getContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contID = Integer.parseInt(cont);
				}
				setBill_User_ID(contID);

				//	CreditAvailable
				if (IsSOTrx)
				{
					BigDecimal CreditLimit = rs.getBigDecimal("SO_CreditLimit");
					//	String SOCreditStatus = rs.getString("SOCreditStatus");
					if ((CreditLimit != null) && (CreditLimit.signum() != 0))
					{
						BigDecimal CreditAvailable = rs.getBigDecimal("CreditAvailable");
						if ((p_changeVO != null)
								&& (CreditAvailable != null) && (CreditAvailable.signum() < 0))
						{
							String msg = Msg.getMsg(getCtx(), "CreditLimitOver",
									DisplayType.getNumberFormat(DisplayTypeConstants.Amount).format(CreditAvailable));
							p_changeVO.addError(msg);
						}
					}
				}

				//	PO Reference
				String s = rs.getString("POReference");

				// Order Reference should not be reset by Bill To BPartner; only by BPartner
				/*if (s != null && s.length() != 0)
					setPOReference(s); */
				//	SO Description
				//  if there is an existing description, do not change it.
				s = rs.getString("SO_Description");
				String Description = getDescription();
				if ((s != null) && (s.trim().length() != 0) && ((Description ==null) || (Description.trim().length() == 0)))
					if(IsSOTrx)		//Do Not set if Purchase Order
						setDescription(s);
				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				setIsDiscountPrinted("Y".equals(s));

				//	Defaults, if not Walk-in Receipt or Walk-in Invoice
				//	Defaults, if not Walk-in Receipt or Walk-in Invoice
				String OrderType = getOrderTypeFromTargetDocType(windowNo);
				setInvoiceRule(INVOICERULE_AfterDelivery);
				setPaymentRule(PAYMENTRULE_OnCredit);
				if (OrderType.equals(DocSubTypeSO_Prepay))
					setInvoiceRule(INVOICERULE_Immediate);
				else if (OrderType.equals(MOrder.DocSubTypeSO_POS))	//  for POS
					setPaymentRule(PAYMENTRULE_Cash);
				else
				{
					//	PaymentRule
					s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if ((s != null) && (s.length() != 0))
					{
						if (s.equals("B"))				//	No Cache in Non POS
							s = PAYMENTRULE_OnCredit;	//  Payment Term
						if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
							s = PAYMENTRULE_OnCredit;	//  Payment Term
						setPaymentRule(s);
					}
					//	Payment Term
					ii = rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID");
					if (ii != 0)
						setC_PaymentTerm_ID(ii);
					else
						setC_PaymentTerm_ID(getCtx().getContextAsInt("#C_PaymentTerm_ID"));
					//	InvoiceRule
					s = rs.getString("InvoiceRule");
					if ((s != null) && (s.length() != 0))
						setInvoiceRule(s);
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "bPartnerBill", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	setBill_BPartner_ID


	/**
	 * 	Set Business Partner Location (Ship+Bill)
	 *	@param C_BPartner_Location_ID bp location
	 */
	@Override
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		super.setC_BPartner_Location_ID (C_BPartner_Location_ID);
		super.setBill_Location_ID(C_BPartner_Location_ID);
	}	//	setC_BPartner_Location_ID

	/**
	 * 	Set Business Partner Contact (Ship+Bill)
	 *	@param AD_User_ID user
	 */
	@Override
	public void setAD_User_ID (int AD_User_ID)
	{
		super.setAD_User_ID (AD_User_ID);
		super.setBill_User_ID (AD_User_ID);
	}	//	setAD_User_ID

	/**
	 * 	Set Ship Business Partner
	 *	@param C_BPartner_ID bpartner
	 */
	public void setShip_BPartner_ID (int C_BPartner_ID)
	{
		super.setC_BPartner_ID (C_BPartner_ID);
	}	//	setShip_BPartner_ID

	/**
	 * 	Set Ship Business Partner Location
	 *	@param C_BPartner_Location_ID bp location
	 */
	public void setShip_Location_ID (int C_BPartner_Location_ID)
	{
		super.setC_BPartner_Location_ID (C_BPartner_Location_ID);
	}	//	setShip_Location_ID

	/**
	 * 	Set Ship Business Partner Contact
	 *	@param AD_User_ID user
	 */
	public void setShip_User_ID (int AD_User_ID)
	{
		super.setAD_User_ID (AD_User_ID);
	}	//	setShip_User_ID


	/**
	 * 	Set Warehouse
	 *	@param M_Warehouse_ID warehouse
	 */
	@Override
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		super.setM_Warehouse_ID (M_Warehouse_ID);
	}	//	setM_Warehouse_ID

	/**
	 * 	Set M_Warehouse_ID - Callout
	 *	@param oldM_Warehouse_ID old
	 *	@param newM_Warehouse_ID new
	 *	@param windowNo window no
	 */
	@UICallout public void setM_Warehouse_ID (String oldM_Warehouse_ID,
			String newM_Warehouse_ID, int windowNo) throws Exception
			{
		if ((newM_Warehouse_ID == null) || (newM_Warehouse_ID.length() == 0))
			return;

		int M_Warehouse_ID=Integer.parseInt(newM_Warehouse_ID);
		if (M_Warehouse_ID == 0)
			return;
		setM_Warehouse_ID(M_Warehouse_ID);
		MWarehouse wh = MWarehouse.get(getCtx(), M_Warehouse_ID);
		String DeliveryRule = getDeliveryRule();
		if((wh.isDisallowNegativeInv() && DeliveryRule.equals(X_C_Order.DELIVERYRULE_Force)) ||
				((DeliveryRule == null) || (DeliveryRule.length()==0)))
			setDeliveryRule(DELIVERYRULE_Availability);
		/** Need to set Delivery Rule to itself, because otherwise it gets nullified in webUI.
		 * Since Delivery Rule is dependent on the warehouse (Force is not allowed if Neg Inventory
		 * is disallowed, it gets reset when the warehouse is changed.
		 */
		else
			setDeliveryRule(DeliveryRule);
			}	//	setM_Warehouse_ID

	/**
	 * 	Set Drop Ship
	 *	@param IsDropShip drop ship
	 */
	@Override
	public void setIsDropShip (boolean IsDropShip)
	{
		super.setIsDropShip (IsDropShip);
	}	//	setIsDropShip


	public void setPriceListVersion(int windowNo)
	{
		int M_PriceList_ID = getM_PriceList_ID();
		if (M_PriceList_ID == 0)
			return;

		Timestamp orderDate = getDateOrdered();
		if(orderDate == null)
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
			pstmt.setTimestamp(2, orderDate);
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
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}


	/**
	 * 	Set DateOrdered - Callout
	 *	@param oldDateOrdered old
	 *	@param newDateOrdered new
	 *	@param windowNo window no
	 */
	@UICallout public void setDateOrdered (String oldDateOrdered,
			String newDateOrdered, int windowNo) throws Exception
			{
		if ((newDateOrdered == null) || (newDateOrdered.length() == 0))
			return;
		Timestamp dateOrdered = PO.convertToTimestamp(newDateOrdered);
		if (dateOrdered == null)
			return;
		setDateOrdered(dateOrdered);
		setPriceListVersion(windowNo);

			}	//	setDateOrdered

	/**
	 *	Set Date Ordered and Acct Date
	 */
	@Override
	public void setDateOrdered(Timestamp dateOrdered)
	{
		super.setDateOrdered(dateOrdered);
		super.setDateAcct(dateOrdered);
	}	//	setDateOrdered

	/*************************************************************************/

	/** Sales Order Sub Type - SO	*/
	public static final String		DocSubTypeSO_Standard = "SO";
	/** Sales Order Sub Type - OB	*/
	public static final String		DocSubTypeSO_Quotation = "OB";
	/** Sales Order Sub Type - ON	*/
	public static final String		DocSubTypeSO_Proposal = "ON";
	/** Sales Order Sub Type - PR	*/
	public static final String		DocSubTypeSO_Prepay = "PR";
	/** Sales Order Sub Type - WR	*/
	public static final String		DocSubTypeSO_POS = "WR";
	/** Sales Order Sub Type - WP	*/
	public static final String		DocSubTypeSO_Warehouse = "WP";
	/** Sales Order Sub Type - WI	*/
	public static final String		DocSubTypeSO_OnCredit = "WI";
	/** Sales Order Sub Type - RM	*/
	public static final String		DocSubTypeSO_RMA = "RM";


	/**
	 * 	Set Target Sales Document Type - Callout.
	 * 	Sets OrderType (=DocSubTypeSO), HasCharges [ctx only]
	 * 	IsDropShip, DeliveryRule, InvoiceRule, PaymentRule, IsSOTrx, DocumentNo
	 * 	If BP is changed: PaymentRule, C_PaymentTerm_ID, InvoiceRule, DeliveryRule,
	 * 	FreightCostRule, DeliveryViaRule
	 * 	@param oldC_DocTypeTarget_ID old ID
	 * 	@param newC_DocTypeTarget_ID new ID
	 * 	@param windowNo window
	 */
	@UICallout public void setC_DocTypeTarget_ID (String oldC_DocTypeTarget_ID,
			String newC_DocTypeTarget_ID, int windowNo) throws Exception
			{
		if (Util.isEmpty(newC_DocTypeTarget_ID))
			return;
		int C_DocTypeTarget_ID = convertToInt(newC_DocTypeTarget_ID);
		if (C_DocTypeTarget_ID == 0)
			return;

		//	Re-Create new DocNo, if there is a doc number already
		//	and the existing source used a different Sequence number
		String oldDocNo = getDocumentNo();
		boolean newDocNo = (oldDocNo == null);
		if (!newDocNo && oldDocNo.startsWith("<") && oldDocNo.endsWith(">"))
			newDocNo = true;
		int oldC_DocType_ID = getC_DocType_ID();
		if ((oldC_DocType_ID == 0) && !Util.isEmpty(oldC_DocTypeTarget_ID))
			oldC_DocType_ID = convertToInt(oldC_DocTypeTarget_ID);
		
		if (oldC_DocType_ID!=convertToInt(newC_DocTypeTarget_ID))
			setC_DocType_ID(convertToInt(newC_DocTypeTarget_ID));

		
		String sql = "SELECT d.DocSubTypeSO,d.HasCharges,'N',"			//	1..3
			+ "d.IsDocNoControlled,s.CurrentNext,s.CurrentNextSys,"     //  4..6
			+ "s.AD_Sequence_ID,d.IsSOTrx,d.IsReturnTrx "               //	7..9
			+ "FROM C_DocType d "
			+ "LEFT OUTER JOIN AD_Sequence s ON (d.DocNoSequence_ID=s.AD_Sequence_ID) "
			+ "WHERE C_DocType_ID=?";	//	#1
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int AD_Sequence_ID = 0;

			//	Get old AD_SeqNo for comparison
			if (!newDocNo && (oldC_DocType_ID != 0))
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, oldC_DocType_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
					AD_Sequence_ID = rs.getInt(7);
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_DocTypeTarget_ID);
			rs = pstmt.executeQuery();
			String DocSubTypeSO = "";
			boolean IsSOTrx = true;
			boolean IsReturnTrx = false;
			if (rs.next())		//	we found document type
			{
				super.setC_DocTypeTarget_ID(C_DocTypeTarget_ID);
				//	Set Context:	Document Sub Type for Sales Orders
				DocSubTypeSO = rs.getString(1);
				if (DocSubTypeSO == null)
					DocSubTypeSO = "--";
				if (p_changeVO != null)
					//p_changeVO.setContext(getCtx(), windowNo, "OrderType", DocSubTypeSO);
					p_changeVO.addChangedValue("OrderType", DocSubTypeSO);

				//	No Drop Ship other than Standard
				if (!DocSubTypeSO.equals(DocSubTypeSO_Standard))
					setIsDropShip(false);

				//	IsSOTrx
				if ("N".equals(rs.getString(8)))
					IsSOTrx = false;
				setIsSOTrx(IsSOTrx);

				// IsReturnTrx
				IsReturnTrx = "Y".equals(rs.getString(9));
				setIsReturnTrx(IsReturnTrx);

				if(!IsReturnTrx)
				{
					//	Delivery Rule
					if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS))
						setDeliveryRule(DELIVERYRULE_Force);
					else if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay))
						setDeliveryRule(DELIVERYRULE_AfterReceipt);
					else
						setDeliveryRule(DELIVERYRULE_Availability);

					//	Invoice Rule
					if (DocSubTypeSO.equals(DocSubTypeSO_POS)
							|| DocSubTypeSO.equals(DocSubTypeSO_Prepay)
							|| DocSubTypeSO.equals(DocSubTypeSO_OnCredit) )
						setInvoiceRule(INVOICERULE_Immediate);
					else
						setInvoiceRule(INVOICERULE_AfterDelivery);

					//	Payment Rule - POS Order
					if (DocSubTypeSO.equals(DocSubTypeSO_POS))
						setPaymentRule(PAYMENTRULE_Cash);
					else
						setPaymentRule(PAYMENTRULE_OnCredit);

					//	Set Context: Charges
					if (p_changeVO != null)
						p_changeVO.setContext(getCtx(), windowNo, "HasCharges", rs.getString(2));
				}
				else
				{
					if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS))
						setDeliveryRule(DELIVERYRULE_Force);
					else
						setDeliveryRule(DELIVERYRULE_Manual);
				}

				//	DocumentNo
				if (rs.getString(4).equals("Y"))			//	IsDocNoControlled
				{
					if (!newDocNo && (AD_Sequence_ID != rs.getInt(7)))
						newDocNo = true;
					if (newDocNo)
						if (Ini.isPropertyBool(Ini.P_COMPIERESYS)
								&& (getCtx().getAD_Client_ID() < 1000000))
							setDocumentNo("<" + rs.getString(6) + ">");
						else
							setDocumentNo("<" + rs.getString(5) + ">");
				}
			}
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			// Skip remaining steps for RMA. These are copied over from original order.
			if(IsReturnTrx)
				return;

			//  When BPartner is changed, the Rules are not set if
			//  it is a POS or Credit Order (i.e. defaults from Standard BPartner)
			//  This re-reads the Rules and applies them.
			if (DocSubTypeSO.equals(DocSubTypeSO_POS)
					|| DocSubTypeSO.equals(DocSubTypeSO_Prepay))    //  not for POS/PrePay
				;
			else
			{
				sql = "SELECT PaymentRule,C_PaymentTerm_ID,"            //  1..2
					+ "InvoiceRule,DeliveryRule,"                       //  3..4
					+ "FreightCostRule,DeliveryViaRule, "               //  5..6
					+ "PaymentRulePO,PO_PaymentTerm_ID, M_Shipper_ID "
					+ "FROM C_BPartner "
					+ "WHERE C_BPartner_ID=?";		//	#1
				pstmt = DB.prepareStatement(sql, get_Trx());
				int C_BPartner_ID = getC_BPartner_ID();
				pstmt.setInt(1, C_BPartner_ID);
				//
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					//	PaymentRule
					String paymentRule = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if ((paymentRule != null) && (paymentRule.length() != 0))
					{
						if (IsSOTrx 	//	No Cash/Check/Transfer for SO_Trx
								&& (paymentRule.equals(PAYMENTRULE_Cash)
										|| paymentRule.equals(PAYMENTRULE_Check)
										|| paymentRule.equals(PAYMENTRULE_DirectDeposit)))
							paymentRule = PAYMENTRULE_OnCredit;				//  Payment Term
						if (!IsSOTrx 	//	No Cash for PO_Trx
								&& (paymentRule.equals(PAYMENTRULE_Cash)))
							paymentRule = PAYMENTRULE_OnCredit;				//  Payment Term
						setPaymentRule(paymentRule);
					}
					//	Payment Term
					int C_PaymentTerm_ID = rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID");
					if (C_PaymentTerm_ID != 0)
						setC_PaymentTerm_ID(C_PaymentTerm_ID);
					else
						setC_PaymentTerm_ID(getCtx().getContextAsInt("#C_PaymentTerm_ID"));
					//	InvoiceRule
					String invoiceRule = rs.getString(3);
					if ((invoiceRule != null) && (invoiceRule.length() != 0))
						setInvoiceRule(invoiceRule);
					//	DeliveryRule
					String deliveryRule = rs.getString(4);
					if ((deliveryRule != null) && (deliveryRule.length() != 0))
						setDeliveryRule(deliveryRule);
					//	FreightCostRule
					String freightCostRule = rs.getString(5);
					if ((freightCostRule != null) && (freightCostRule.length() != 0))
						setFreightCostRule(freightCostRule);
									
//					DeliveryViaRule
					String deliveryViaRule = rs.getString("DeliveryViaRule");
					if ((deliveryViaRule != null) && (deliveryViaRule.length() != 0))
					{
						setDeliveryViaRule(deliveryViaRule);
					//Freight Carrier
						if (deliveryViaRule.equals(X_C_Order.DELIVERYVIARULE_Shipper)) 
							setM_Shipper_ID(rs.getInt("M_Shipper_ID"));
					}
					
					
					
				}
			}   //  re-read customer rules
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
	}	//	setC_DocTypeTarget_ID

	/**
	 * 	Set Target Sales Document Type
	 * 	@param DocSubTypeSO_x SO sub type - see DocSubTypeSO_*
	 */
	public void setC_DocTypeTarget_ID (String DocSubTypeSO_x)
	{
		String sql = "SELECT C_DocType_ID FROM C_DocType "
			+ "WHERE AD_Client_ID=? AND AD_Org_ID IN (0,?)"
			+ " AND DocSubTypeSO=? AND IsReturnTrx='N' "
			+ "ORDER BY AD_Org_ID DESC, IsDefault DESC";
		int C_DocType_ID = QueryUtil.getSQLValue(get_Trx(), sql, getAD_Client_ID(),getAD_Org_ID(),DocSubTypeSO_x);
		if (C_DocType_ID <= 0)
			log.severe ("Not found for AD_Client_ID=" + getAD_Client_ID () + ", SubType=" + DocSubTypeSO_x);
		else
		{
			log.fine("(SO) - " + DocSubTypeSO_x);
			setC_DocTypeTarget_ID (C_DocType_ID);
			setIsSOTrx(true);
			setIsReturnTrx(false);
		}
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
	 * 	Set Target Document Type.
	 * 	Standard Order or PO
	 */
	public void setC_DocTypeTarget_ID ()
	{
		if (isSOTrx())		//	SO = Std Order
		{
			setC_DocTypeTarget_ID(DocSubTypeSO_Standard);
			return;
		}
		//	PO
		String sql = "SELECT C_DocType_ID FROM C_DocType "
			+ "WHERE AD_Client_ID=? AND AD_Org_ID IN (0,?)"
			+ " AND DocBaseType='POO' AND IsReturnTrx='N' "
			+ "ORDER BY AD_Org_ID DESC, IsDefault DESC";
		int C_DocType_ID = QueryUtil.getSQLValue(get_Trx(), sql, getAD_Client_ID(),getAD_Org_ID());
		if (C_DocType_ID <= 0)
			log.severe ("No POO found for AD_Client_ID=" + getAD_Client_ID ());
		else
		{
			log.fine("(PO) - " + C_DocType_ID);
			setC_DocTypeTarget_ID (C_DocType_ID);
			setIsReturnTrx(false);
		}
	}	//	setC_DocTypeTarget_ID




	/**
	 * 	Copy Lines From other Order
	 *	@param otherOrder order
	 *	@param counter set counter info
	 *	@param copyASI copy line attributes Attribute Set Instance, Resaouce Assignment
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MOrder otherOrder, boolean counter, boolean copyASI)
	{
		if (isProcessed() || isPosted() || (otherOrder == null))
			return 0;
		MOrderLine[] fromLines = otherOrder.getLines();
		
		ArrayList<MOrderLine> linesToSave = new ArrayList<MOrderLine>(); 
		
		for (MOrderLine element : fromLines) {
			MOrderLine line = new MOrderLine (this);
			PO.copyValues(element, line, getAD_Client_ID(), getAD_Org_ID());
			line.setC_Order_ID(getC_Order_ID());
			line.setOrder(this);
			line.set_ValueNoCheck ("C_OrderLine_ID", I_ZERO);	//	new
			//	References
			if (!copyASI)
			{
				line.setM_AttributeSetInstance_ID(0);
				line.setS_ResourceAssignment_ID(0);
			}
			if (counter)
				line.setRef_OrderLine_ID(element.getC_OrderLine_ID());
			else
				line.setRef_OrderLine_ID(0);
			//
			line.setQtyDelivered(Env.ZERO);
			line.setQtyInvoiced(Env.ZERO);
			line.setQtyReserved(Env.ZERO);
			line.setQtyReturned(Env.ZERO);
			line.setQtyAllocated(Env.ZERO);
			line.setQtyDedicated(Env.ZERO);

			line.setDateDelivered(null);
			line.setDateInvoiced(null);
			//	Tax
			if (getC_BPartner_ID() != otherOrder.getC_BPartner_ID())
				line.setTax();		//	recalculate
			//
			//
			line.setProcessed(false);
			linesToSave.add(line);
			//	Cross Link
			if (counter)
			{
				element.setRef_OrderLine_ID(line.getC_OrderLine_ID());
				linesToSave.add(element);
			}
		}
		
		boolean ok = PO.saveAll(get_Trx(), linesToSave);
		if (!ok)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved");
		return linesToSave.size();
	}	//	copyLinesFrom


	/**************************************************************************
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MOrder[")
		.append(get_ID()).append("-").append(getDocumentNo())
		.append(",IsSOTrx=").append(isSOTrx())
		.append(",C_DocType_ID=").append(getC_DocType_ID())
		.append(", GrandTotal=").append(getGrandTotal())
		.append ("]");
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
		ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.ORDER, getC_Order_ID());
		if (re == null)
			return null;
		return re.getPDF(file);
	}	//	createPDF

	/**
	 * 	Set Price List (and Currency, TaxIncluded) when valid
	 * 	@param M_PriceList_ID price list
	 */
	@Override
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		MPriceList pl = MPriceList.get(getCtx(), M_PriceList_ID, null);
		if (pl.get_ID() == M_PriceList_ID)
		{
			super.setM_PriceList_ID(M_PriceList_ID);
			setC_Currency_ID(pl.getC_Currency_ID());
			setIsTaxIncluded(pl.isTaxIncluded());
		}
	}	//	setM_PriceList_ID

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
		if ((newM_PriceList_ID == null) || (newM_PriceList_ID.length() == 0))
			return;
		int M_PriceList_ID = Integer.parseInt(newM_PriceList_ID);
		if (M_PriceList_ID == 0)
			return;
		setM_PriceList_ID(M_PriceList_ID);

		setPriceListVersion(windowNo);
			}	//	setM_PriceList_ID

	/**
	 * 	Set Return Policy
	 *	@param
	 */
	public void setM_ReturnPolicy_ID ()
	{
		MBPartner bpartner = new MBPartner (getCtx(), getC_BPartner_ID(), null);

		if(bpartner.get_ID() !=0)
		{
			if(isSOTrx())
				super.setM_ReturnPolicy_ID(bpartner.getM_ReturnPolicy_ID());
			else
				super.setM_ReturnPolicy_ID(bpartner.getPO_ReturnPolicy_ID());
		}

	}	//	setM_ReturnPolicy_ID


	/**
	 * 	Set Original Order for RMA
	 * 	SOTrx should be set.
	 * 	@param origOrder MOrder
	 */
	public void setOrigOrder (MOrder origOrder)
	{
		if((origOrder == null) || (origOrder.get_ID()==0))
			return;

		setOrig_Order_ID(origOrder.getC_Order_ID());
		//	Get Details from Original Order
		//	MBPartner bpartner = new MBPartner (getCtx(), origOrder.getC_BPartner_ID(), null);

		// Reset Original Shipment
		setOrig_InOut_ID(-1);
		setPOReference(origOrder.getPOReference()); //setting POreference
		setC_BPartner_ID(origOrder.getC_BPartner_ID());
		setC_BPartner_Location_ID(origOrder.getC_BPartner_Location_ID());
		setAD_User_ID(origOrder.getAD_User_ID());
		setBill_BPartner_ID(origOrder.getBill_BPartner_ID());
		setBill_Location_ID(origOrder.getBill_Location_ID());
		setBill_User_ID(origOrder.getBill_User_ID());
		setM_Warehouse_ID(origOrder.getM_Warehouse_ID());

		setM_ReturnPolicy_ID();

		setM_PriceList_ID(origOrder.getM_PriceList_ID());
		setPaymentRule(origOrder.getPaymentRule());
		setC_PaymentTerm_ID(origOrder.getC_PaymentTerm_ID());
		//setDeliveryRule(X_C_Order.DELIVERYRULE_Manual);

		setBill_Location_ID(origOrder.getBill_Location_ID());
		setInvoiceRule(origOrder.getInvoiceRule());
		setPaymentRule(origOrder.getPaymentRule());
		
		String deliveryviarule=origOrder.getDeliveryViaRule();
		if (deliveryviarule!=null)
		{
			setDeliveryViaRule(deliveryviarule);
			if (deliveryviarule.equals(X_C_Order.DELIVERYVIARULE_Shipper))
				setM_Shipper_ID(origOrder.getM_Shipper_ID());
		}
		
		setFreightCostRule(origOrder.getFreightCostRule());

		return;

	} // setOrigOrder

	/**
	 * 	Set Original Order - Callout
	 *	@param oldOrig_Order_ID old Orig Order
	 *	@param newOrig_Order_ID new Orig Order
	 *	@param windowNo window no
	 */
	@UICallout public void setOrig_Order_ID (String oldOrig_Order_ID,
			String newOrig_Order_ID, int windowNo) throws Exception
			{
		if ((newOrig_Order_ID == null) || (newOrig_Order_ID.length() == 0))
			return;
		int Orig_Order_ID = Integer.parseInt(newOrig_Order_ID);
		if (Orig_Order_ID == 0)
			return;

		//		Get Details
		MOrder origOrder = new MOrder (getCtx(), Orig_Order_ID, null);
		if (origOrder.get_ID() != 0)
			setOrigOrder(origOrder);

			} // setOrig_Order_ID

	@UICallout public void setAD_Org_ID (String oldAD_Org_ID,
			String newAD_Org_ID, int windowNo) throws Exception
			{
		if ((newAD_Org_ID == null) || (newAD_Org_ID.length() == 0))
			return;
		int AD_Org_ID = Integer.parseInt(newAD_Org_ID);
		if (AD_Org_ID == 0)
			return;

		if(getM_Warehouse_ID()!=0)
			return;
		
		int M_Warehouse_ID = MOrg.get(getCtx(), AD_Org_ID).getM_Warehouse_ID();
		if(M_Warehouse_ID == 0)
		{
			MWarehouse[] war = MWarehouse.getForOrg(getCtx(), AD_Org_ID);
			if(war.length >0)
				M_Warehouse_ID = war[0].getM_Warehouse_ID();
		}
		
		if(M_Warehouse_ID !=0)
			setM_Warehouse_ID(M_Warehouse_ID);
			} // setAD_Org_ID

	/**
	 * 	Set Original Shipment for RMA
	 * 	SOTrx should be set.
	 * 	@param origInOut MInOut
	 */
	public void setOrigInOut (MInOut origInOut)
	{
		if((origInOut == null) || (origInOut.get_ID()==0))
			return;

		setOrig_InOut_ID(origInOut.getM_InOut_ID());
		setC_Project_ID(origInOut.getC_Project_ID());
		setC_Campaign_ID(origInOut.getC_Campaign_ID());
		setC_Activity_ID(origInOut.getC_Activity_ID());
		setAD_OrgTrx_ID(origInOut.getAD_OrgTrx_ID());
		setUser1_ID(origInOut.getUser1_ID());
		setUser2_ID(origInOut.getUser2_ID());

		return;

	} // setOrigInOut


	/**
	 * 	Set Original Shipment - Callout
	 *	@param oldOrig_InOut_ID old Orig Order
	 *	@param newOrig_InOut_ID new Orig Order
	 *	@param windowNo window no
	 */
	@UICallout public void setOrig_InOut_ID (String oldOrig_InOut_ID,
			String newOrig_InOut_ID, int windowNo) throws Exception
			{
		if ((newOrig_InOut_ID == null) || (newOrig_InOut_ID.length() == 0))
			return;
		int Orig_InOut_ID = Integer.parseInt(newOrig_InOut_ID);
		if (Orig_InOut_ID == 0)
			return;

		//		Get Details
		MInOut origInOut = new MInOut (getCtx(), Orig_InOut_ID, null);
		if (origInOut.get_ID() != 0)
			setOrigInOut(origInOut);

			} // setOrig_InOut_ID




	/**************************************************************************
	 * 	Get Lines of Order
	 * 	@param whereClause where clause or null (starting with AND)
	 * 	@param orderClause order clause
	 * 	@return lines
	 */
	public MOrderLine[] getLines (String whereClause, String orderClause)
	{
		ArrayList<MOrderLine> list = new ArrayList<MOrderLine> ();
		StringBuffer sql = new StringBuffer("SELECT * FROM C_OrderLine WHERE C_Order_ID=? ");
		if (whereClause != null)
			sql.append(whereClause);
		if (orderClause != null)
			sql.append(" ").append(orderClause);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, getC_Order_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MOrderLine ol = new MOrderLine(getCtx(), rs, get_Trx());
				ol.setHeaderInfo (this);
				list.add(ol);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		//
		MOrderLine[] lines = new MOrderLine[list.size ()];
		list.toArray (lines);
		return lines;
	}	//	getLines

	/**************************************************************************
	 * 	Get Lines of Order for a given product
	 * 	@param orderClause order clause
	 * 	@return lines
	 */
	public MOrderLine[] getLines (int M_Product_ID, String whereClause, String orderClause)
	{
		ArrayList<MOrderLine> list = new ArrayList<MOrderLine> ();
		StringBuffer sql = new StringBuffer("SELECT * FROM C_OrderLine WHERE C_Order_ID=? AND M_Product_ID=? ");

		if (whereClause != null)
			sql.append(" AND ").append(whereClause);

		if (orderClause != null)
			sql.append(" ORDER BY ").append(orderClause);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, getC_Order_ID());
			pstmt.setInt(2, M_Product_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MOrderLine ol = new MOrderLine(getCtx(), rs, get_Trx());
				ol.setHeaderInfo (this);
				list.add(ol);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		MOrderLine[] lines = new MOrderLine[list.size ()];
		list.toArray (lines);
		return lines;
	}	//	getLines

	/**
	 * 	Get Lines of Order
	 * 	@param requery requery
	 * 	@param orderBy optional order by column
	 * 	@return lines
	 */
	@Deprecated
	public MOrderLine[] getLines (boolean requery, String orderBy)
	{
		return getLines(orderBy);
	}	//	getLines

	/**
	 * 	Get Lines of Order
	 * @param orderBy optional order by column
	 * 	@return lines
	 */
	public MOrderLine[] getLines (String orderBy)
	{
		String orderClause = "ORDER BY ";
		if ((orderBy != null) && (orderBy.length() > 0))
			orderClause += orderBy;
		else
			orderClause += "Line";
		return getLines(null, orderClause);
	}	//	getLines

	/**
	 * 	Get Lines of Order.
	 * 	(useb by web store)
	 * 	@return lines
	 */
	public MOrderLine[] getLines()
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
		MOrderLine[] lines = getLines();	//	Line is default
		for (MOrderLine line : lines) {
			line.setLine(number);
			number += step;
		}
		PO.saveAll(get_Trx(), Arrays.asList(lines));
	}	//	renumberLines

	/**
	 * 	Get Taxes of Order
	 *	@param requery requery
	 *	@return array of taxes
	 */
	@Deprecated
	public MOrderTax[] getTaxes(boolean requery)
	{
		return getTaxes();
	}
	
	/**
	 * 	Get Taxes of Order
	 *	@return array of taxes
	 */
	public MOrderTax[] getTaxes()
	{
		ArrayList<MOrderTax> list = new ArrayList<MOrderTax>();
		String sql = "SELECT * FROM C_OrderTax WHERE C_Order_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Order_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MOrderTax(getCtx(), rs, get_Trx()));
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
		//
		MOrderTax[] taxes = new MOrderTax[list.size()];
		list.toArray(taxes);
		return taxes;
	}	//	getTaxes


	/**
	 * 	Get Invoices of Order
	 * 	@param hearderLinkOnly shipments based on header only
	 * 	@return invoices
	 */
	public MInvoice[] getInvoices(boolean hearderLinkOnly)
	{
		//	TODO get invoiced which are linked on line level
		ArrayList<MInvoice> list = new ArrayList<MInvoice>();
		String sql = "SELECT * FROM C_Invoice WHERE C_Order_ID=? ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Order_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MInvoice(getCtx(), rs, get_Trx()));
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
		//
		MInvoice[] retValue = new MInvoice[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getInvoices

	/**
	 * 	Get latest Invoice of Order
	 * 	@return invoice id or 0
	 */
	public int getC_Invoice_ID()
	{
		int C_Invoice_ID = 0;
		String sql = "SELECT C_Invoice_ID FROM C_Invoice "
			+ "WHERE C_Order_ID=? AND DocStatus IN ('CO','CL') "
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Order_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				C_Invoice_ID = rs.getInt(1);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getC_Invoice_ID", e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return C_Invoice_ID;
	}	//	getC_Invoice_ID


	/**
	 * 	Get Latest Shipment for the Order
	 *
	 * 	@return latest shipment
	 */
	public MInOut getOpenInOut (int C_DocType_ID, int M_Warehouse_ID, int C_BPartner_ID, int C_BPartner_Location_ID)
	{
		//	TODO: getShipment if linked on line
		MInOut inout = null;
		String sql = "SELECT M_InOut_ID "+
		"FROM M_InOut WHERE C_Order_ID=? "+
		" AND M_Warehouse_ID=? "+
		" AND C_BPartner_ID=? "+
		" AND C_BPartner_Location_ID=? "+
		" AND C_DocType_ID=? "+
		" AND DocStatus IN ('DR','IP') "+
		" ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Order_ID());
			pstmt.setInt(2, M_Warehouse_ID);
			pstmt.setInt(3, C_BPartner_ID);
			pstmt.setInt(4, C_BPartner_Location_ID);
			pstmt.setInt(5, C_DocType_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				inout = new MInOut(getCtx(), rs.getInt(1), get_Trx());
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
		//
		return inout;
	}	//	getShipments

	/**
	 * 	Get Shipments of Order
	 * 	@param hearderLinkOnly shipments based on header only
	 * 	@return shipments
	 */
	public MInOut[] getShipments (boolean hearderLinkOnly)
	{
		//	TODO: getShipment if linked on line
		ArrayList<MInOut> list = new ArrayList<MInOut>();
		String sql = "SELECT * FROM M_InOut WHERE C_Order_ID=? ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Order_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MInOut(getCtx(), rs, get_Trx()));
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
		//
		MInOut[] retValue = new MInOut[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getShipments

	/**
	 * 	Get RMAs of Order
	 * 	@return RMAs
	 */
	public MOrder[] getRMAs ()
	{
		ArrayList<MOrder> list = new ArrayList<MOrder>();
		String sql = "SELECT * FROM C_Order WHERE Orig_Order_ID=? ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Order_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MOrder(getCtx(), rs, get_Trx()));
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
		//
		MOrder[] retValue = new MOrder[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getRMAs

	/**
	 * 	Get Shipment Lines of Order
	 * 	@return shipments newest first
	 */
	public MInOutLine[] getShipmentLines()
	{
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		String sql = "SELECT * FROM M_InOutLine iol "
			+ "WHERE iol.C_OrderLine_ID IN "
			+ "(SELECT C_OrderLine_ID FROM C_OrderLine WHERE C_Order_ID=?) "
			+ "ORDER BY M_InOutLine_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Order_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MInOutLine(getCtx(), rs, get_Trx()));
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
		//
		MInOutLine[] retValue = new MInOutLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getShipmentLines

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

	/**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
	public String getDocStatusName()
	{
		return MRefList.getListName(getCtx(), 131, getDocStatus());
	}	//	getDocStatusName

	/**
	 * 	Set DocAction
	 *	@param DocAction doc action
	 */
	@Override
	public void setDocAction (String DocAction)
	{
		setDocAction (DocAction, false);
	}	//	setDocAction

	/**
	 * 	Set DocAction
	 *	@param DocAction doc oction
	 *	@param forceCreation force creation
	 */
	public void setDocAction (String DocAction, boolean forceCreation)
	{
		super.setDocAction (DocAction);
		m_forceCreation = forceCreation;
	}	//	setDocAction

	/**
	 * 	Set Processed.
	 * 	Propagate to Lines/Taxes
	 *	@param processed processed
	 */
	@Override
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String set = "SET Processed=? WHERE C_Order_ID=?";
		Object[]params = new Object[] {processed ? "Y" : "N", getC_Order_ID() };
		int noLine = DB.executeUpdate(get_Trx(), "UPDATE C_OrderLine " + set, params);
		int noTax = DB.executeUpdate(get_Trx(), "UPDATE C_OrderTax " + set, params);
		log.fine(processed + " - Lines=" + noLine + ", Tax=" + noTax);
	}	//	setProcessed



	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if(newRecord)
			setC_CashBook_ID(0);
		
		//	Client/Org Check
		if (getAD_Org_ID() == 0)
		{
			int context_AD_Org_ID = getCtx().getAD_Org_ID();
			if (context_AD_Org_ID != 0)
			{
				setAD_Org_ID(context_AD_Org_ID);
				log.warning("Changed Org to Context=" + context_AD_Org_ID);
			}
		}
		if (getAD_Client_ID() == 0)
		{
			m_processMsg = "AD_Client_ID = 0";
			return false;
		}

		//	New Record Doc Type - make sure DocType set to 0
		if (newRecord && (getC_DocType_ID() == 0))
			setC_DocType_ID (0);

		//	Default Warehouse
		if (getM_Warehouse_ID() == 0)
		{
			MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
			setM_Warehouse_ID(org.getM_Warehouse_ID());
		}
		//	Warehouse Org
/**		if (newRecord
				|| is_ValueChanged("AD_Org_ID"))
		{
			MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
			if (wh.getAD_Org_ID() != getAD_Org_ID())
				log.saveWarning("WarehouseOrgConflict", "");
		}
*/		//	Reservations in Warehouse
		if (!newRecord && is_ValueChanged("M_Warehouse_ID"))
		{
			MOrderLine[] lines = getLines();
			for (int i = 0; i < lines.length; i++)
			{
//				if (!lines[i].canChangeWarehouse())		// saves Error
//					return false;
			}
		}

		//	No Partner Info - set Template
		if (getC_BPartner_ID() == 0)
			setBPartner(MBPartner.getTemplate(getCtx(), getAD_Client_ID()));
		if (getC_BPartner_Location_ID() == 0)
			setBPartnerLocation(MBPartner.get(getCtx(), getC_BPartner_ID()));
		//	No Bill - get from Ship
		if (getBill_BPartner_ID() == 0)
		{
			setBill_BPartner_ID(getC_BPartner_ID());
		}
		if (getBill_Location_ID() == 0)
		{
			int bill_loc_id = getC_BPartner_Location_ID();
			MBPartnerLocation loc = new MBPartnerLocation( getCtx(), bill_loc_id, null );
			if( loc.isBillTo() )
				setBill_Location_ID( bill_loc_id );
		}

		//	BP Active check
		if (newRecord || is_ValueChanged("C_BPartner_ID"))
		{
			MBPartner bp = MBPartner.get(getCtx(), getC_BPartner_ID());
			if (!bp.isActive())
			{
				log.saveError("NotActive", Msg.getMsg(getCtx(), "C_BPartner_ID"));
				return false;
			}
		}
		if ((newRecord || is_ValueChanged("Bill_BPartner_ID"))
				&& (getBill_BPartner_ID() != getC_BPartner_ID()))
		{
			MBPartner bp = MBPartner.get(getCtx(), getBill_BPartner_ID());
			if (!bp.isActive())
			{
				log.saveError("NotActive", Msg.getMsg(getCtx(), "Bill_BPartner_ID"));
				return false;
			}
		}

		//	Default Price List
		if (getM_PriceList_ID() == 0)
		{
			int ii = QueryUtil.getSQLValue(get_Trx(),
					"SELECT M_PriceList_ID FROM M_PriceList "
					+ "WHERE AD_Client_ID=? AND IsSOPriceList=? "
					+ "AND IsActive='Y' "
					+ "ORDER BY ASCII(IsDefault) DESC", getAD_Client_ID(), isSOTrx() ? "Y" : "N");
			if (ii != 0)
				setM_PriceList_ID (ii);
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
			pstmt.setTimestamp(2, getDateOrdered());
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

		String docStatus = getDocStatus();
		if (!(DOCSTATUS_Completed.equals(docStatus) || DOCSTATUS_Closed.equals(docStatus) || DOCSTATUS_Voided
				.equals(docStatus))
				&& !validPLV) {
			log.saveError("Error", Msg.getMsg(getCtx(), "PriceListVersionNotFound"));
			return false;
		}


		//	Default Currency
		if (getC_Currency_ID() == 0)
		{
			String sql = "SELECT C_Currency_ID FROM M_PriceList WHERE M_PriceList_ID=?";
			int ii = QueryUtil.getSQLValue (get_Trx(), sql, getM_PriceList_ID());
			if (ii != 0)
				setC_Currency_ID (ii);
			else
				setC_Currency_ID(getCtx().getContextAsInt("#C_Currency_ID"));
		}

		//	Default Sales Rep
		if (getSalesRep_ID() == 0)
		{
			int ii = getCtx().getContextAsInt("#SalesRep_ID");
			if (ii != 0)
				setSalesRep_ID (ii);
		}

		//	Default Document Type
		if (getC_DocTypeTarget_ID() == 0)
			setC_DocTypeTarget_ID(DocSubTypeSO_Standard);

		//	Default Payment Term
		if (getC_PaymentTerm_ID() == 0)
		{
			int ii = getCtx().getContextAsInt("#C_PaymentTerm_ID");
			if (ii != 0)
				setC_PaymentTerm_ID(ii);
			else
			{
				String sql = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID=? AND IsDefault='Y'";
				ii = QueryUtil.getSQLValue(get_Trx(), sql, getAD_Client_ID());
				if (ii != 0)
					setC_PaymentTerm_ID (ii);
			}
		}

		if(isReturnTrx())
		{
			Boolean withinPolicy = true;

			if(getM_ReturnPolicy_ID() == 0)
				setM_ReturnPolicy_ID();

			if (getM_ReturnPolicy_ID() != 0 )
			{
				MInOut origInOut = new MInOut (getCtx(), getOrig_InOut_ID(), null);
				MReturnPolicy rpolicy = new MReturnPolicy (getCtx(), getM_ReturnPolicy_ID(), null);
				log.fine("RMA Date : " + getDateOrdered() + " Shipment Date : " + origInOut.getMovementDate());
				withinPolicy = rpolicy.checkReturnPolicy(origInOut.getMovementDate(),getDateOrdered());
			}
			else
				withinPolicy = false;

			if(!withinPolicy)
			{
				if ( !MRole.getDefault(getCtx(), false).isOverrideReturnPolicy())
				{
					log.saveError("Error", Msg.getMsg(getCtx(), "ReturnPolicyExceeded"));
					return false;
				}
				else
				{
					log.saveWarning("Warning", "ReturnPolicyExceeded");
				}
			}
		}


		return true;
	}	//	beforeSave


	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success || newRecord)
			return success;

		//	Propagate Description changes
		if (is_ValueChanged("Description") || is_ValueChanged("POReference"))
		{
			String sql = "UPDATE C_Invoice i"
				+ " SET (Description,POReference)="
				+ "(SELECT Description, POReference "
				+ "FROM C_Order o WHERE i.C_Order_ID=o.C_Order_ID) "
				+ "WHERE DocStatus NOT IN ('RE','CL') AND C_Order_ID=?";

			int no = DB.executeUpdate(get_Trx(), sql, new Object[]{getC_Order_ID()});
			log.fine("Description -> #" + no);
		}

		//	Propagate Changes of Payment Info to existing (not reversed/closed) invoices
		if (is_ValueChanged("PaymentRule") || is_ValueChanged("C_PaymentTerm_ID")
				|| is_ValueChanged("DateAcct") || is_ValueChanged("C_Payment_ID")
				|| is_ValueChanged("C_CashLine_ID"))
		{
			String sql = "UPDATE C_Invoice i "
				+ "SET (PaymentRule,C_PaymentTerm_ID,DateAcct,C_Payment_ID,C_CashLine_ID)="
				+ "(SELECT PaymentRule,C_PaymentTerm_ID,DateAcct,C_Payment_ID,C_CashLine_ID "
				+ "FROM C_Order o WHERE i.C_Order_ID=o.C_Order_ID) "
				+ "WHERE DocStatus NOT IN ('RE','CL') AND C_Order_ID=?";
			//	Don't touch Closed/Reversed entries
			int no = DB.executeUpdate(get_Trx(), sql, new Object[]{getC_Order_ID()});
			log.fine("Payment -> #" + no);
		}

		//	Sync Lines
		afterSaveSync("AD_Org_ID");
		afterSaveSync("C_BPartner_ID");
		afterSaveSync("C_BPartner_Location_ID");
		afterSaveSync("DateOrdered");
		afterSaveSync("DatePromised");
		afterSaveSync("M_Warehouse_ID");
		afterSaveSync("M_Shipper_ID");
		afterSaveSync("C_Currency_ID");
		//
		//only in batch mode redistribute tax
		if(getCtx().isBatchMode()) {
			recomputeTaxes();
		}

		return true;
	}	//	afterSave

	/**
	 * Recompute all the taxes for the order
	 */
	private void recomputeTaxes() {
		String sql = "SELECT * from C_OrderLine WHERE C_OrderLine_ID in (SELECT MIN(C_OrderLine_ID) from C_OrderLine WHERE C_Order_ID=? GROUP BY C_Tax_ID)"; 
		PreparedStatement pstmt = DB.prepareStatement(sql, get_Trx());
		ResultSet rs = null;
		try {
			pstmt.setInt(1, getC_Order_ID());
			rs = pstmt.executeQuery ();
			while(rs.next()) {
				MOrderLine line = new MOrderLine(getCtx(), rs, get_Trx());
				line.updateHeaderTax();
			}
		}
		catch(SQLException e) {
			log.log(Level.SEVERE, "Batch MOrder save - " + sql.toString(), e);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}


	private void afterSaveSync (String columnName)
	{
		if (is_ValueChanged(columnName))
		{
			String sql = "UPDATE C_OrderLine ol"
				+ " SET " + columnName + " ="
				+ "(SELECT " + columnName
				+ " FROM C_Order o WHERE ol.C_Order_ID=o.C_Order_ID) "
				+ "WHERE C_Order_ID = ?" ;
			int no = DB.executeUpdate(get_Trx(), sql, getC_Order_ID());
			log.fine(columnName + " Lines -> #" + no);
			
			//Since we are updating child record of the order we need to refresh the child rows in the UI
			// call to setRefreshChildRows tells the UI to refresh all the child tabs.
			// This property is used in the gridComponent.getAfterSaveCommand()
			if(p_changeVO != null)
				p_changeVO.setRefreshChildRows(true);
		}
	}	//	afterSaveSync

	/**
	 * 	Before Delete
	 *	@return true of it can be deleted
	 */
	@Override
	protected boolean beforeDelete ()
	{
		if (isProcessed())
			return false;

		for (MOrderLine line : getLines()) {
			if (!line.beforeDelete())
				return false;
		}
		return true;
	}	//	beforeDelete

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
		log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt


	/**************************************************************************
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocTypeTarget_ID());
		setIsReturnTrx(dt.isReturnTrx());
		setIsSOTrx(dt.isSOTrx());


		//	Convert DocType to Target
		if (getC_DocType_ID() != getC_DocTypeTarget_ID() )
		{
			//	Cannot change Std to anything else if different warehouses
			if (getC_DocType_ID() != 0)
			{
				MDocType dtOld = MDocType.get(getCtx(), getC_DocType_ID());
				if (X_C_DocType.DOCSUBTYPESO_StandardOrder.equals(dtOld.getDocSubTypeSO())		//	From SO
						&& !X_C_DocType.DOCSUBTYPESO_StandardOrder.equals(dt.getDocSubTypeSO()))	//	To !SO
				{					
					Object[][] result = QueryUtil.executeQuery(
							get_Trx(), 
							"SELECT M_Warehouse_ID, Line " +
							"FROM C_OrderLine " +
							"WHERE C_Order_ID = ? AND M_Warehouse_ID <> ?", 
							getC_Order_ID(), getM_Warehouse_ID());

					for (Object[] line : result) {
						if (((BigDecimal) line[0]).intValue() != getM_Warehouse_ID()) {
							log.warning("different Warehouse on line " + line[1]);
							m_processMsg = "@CannotChangeDocType@";
							return DocActionConstants.STATUS_Invalid;
						}
					}
				}
			}

			//	New or in Progress/Invalid
			if (DOCSTATUS_Drafted.equals(getDocStatus())
					|| DOCSTATUS_InProgress.equals(getDocStatus())
					|| DOCSTATUS_Invalid.equals(getDocStatus())
					|| (getC_DocType_ID() == 0))
			{
				setC_DocType_ID(getC_DocTypeTarget_ID());
			}
			else	//	convert only if offer
			{
				if (dt.isOffer())
					setC_DocType_ID(getC_DocTypeTarget_ID());
				else
				{
					m_processMsg = "@CannotChangeDocType@";
					return DocActionConstants.STATUS_Invalid;
				}
			}
		}	//	convert DocType

		//	Mandatory Product Attribute Set Instance
		String mandatoryType = "='Y'";	//	IN ('Y','S')
		String sql = "SELECT COUNT(*) "
			+ "FROM C_OrderLine ol"
			+ " INNER JOIN M_Product p ON (ol.M_Product_ID=p.M_Product_ID)"
			+ " INNER JOIN M_AttributeSet pas ON (p.M_AttributeSet_ID=pas.M_AttributeSet_ID) "
			+ "WHERE pas.MandatoryType" + mandatoryType
			+ " AND COALESCE(ol.M_AttributeSetInstance_ID,0) = 0"
			+ " AND ol.C_Order_ID=?"
			+ " AND NOT EXISTS (SELECT 1 "
			+ " FROM M_AttributeSetExclude mase"
			+ " WHERE mase.M_AttributeSet_ID = pas.M_AttributeSet_ID "
			+ " AND mase.IsActive = 'Y'"
			+ " AND mase.AD_Table_ID = ? "
			+ " AND mase.IsSOTrx = ? )";
		int no = QueryUtil.getSQLValue(get_Trx(), sql, getC_Order_ID(), X_C_OrderLine.Table_ID, isSOTrx()?"Y":"N");
		if (no != 0)
		{
			m_processMsg = "@LinesWithoutProductAttribute@ (" + no + ")";
			return DocActionConstants.STATUS_Invalid;
		}

		//	Lines
		explodeBOM();

		if (!reserveStock(dt))
		{
			m_processMsg = "Cannot reserve Stock";
			return DocActionConstants.STATUS_Invalid;
		}
		if (!calculateTaxTotal())
		{
			m_processMsg = "Error calculating tax";
			return DocActionConstants.STATUS_Invalid;
		}

		//	Credit Check
		if (isSOTrx() && !isReturnTrx())
		{
			MBPartner bp = MBPartner.get(getCtx(), getC_BPartner_ID());
			if(!X_C_BPartner.SOCREDITSTATUS_NoCreditCheck.equals(bp.getSOCreditStatus()))
			{
				if (X_C_BPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus()))
				{
					m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@="
						+ bp.getTotalOpenBalance()
						+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
					return DocActionConstants.STATUS_Invalid;
				}
				else if (X_C_BPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus()))
				{
					m_processMsg = "@BPartnerCreditHold@ - @TotalOpenBalance@="
						+ bp.getTotalOpenBalance()
						+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
					return DocActionConstants.STATUS_Invalid;
				}
				BigDecimal grandTotal = MConversionRate.convertBase(getCtx(),
						getGrandTotal(), getC_Currency_ID(), getDateOrdered(),
						getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
				if (X_C_BPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus(grandTotal)))
				{
					m_processMsg = "@BPartnerOverOCreditHold@ - @TotalOpenBalance@="
						+ bp.getTotalOpenBalance() + ", @GrandTotal@=" + grandTotal
						+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
					return DocActionConstants.STATUS_Invalid;
				}
			}
		}

		//	if (!DOCACTION_Complete.equals(getDocAction()))		don't set for just prepare
		//		setDocAction(DOCACTION_Complete);
		return DocActionConstants.STATUS_InProgress;
	}	//	prepareIt

	/**
	 * 	Explode non stocked BOM.
	 * 	@return true if bom exploded
	 */
	private boolean explodeBOM()
	{
		boolean retValue = false;
		String where = "AND IsActive='Y' AND EXISTS "
			+ "(SELECT * FROM M_Product p WHERE C_OrderLine.M_Product_ID=p.M_Product_ID"
			+ " AND	p.IsBOM='Y' AND p.IsVerified='Y' AND p.IsStocked='N')";
		//
		String sql = "SELECT COUNT(*) FROM C_OrderLine "
			+ "WHERE C_Order_ID=? " + where;
		int count = QueryUtil.getSQLValue(get_Trx(), sql, getC_Order_ID());
		while (count != 0)
		{
			retValue = true;
			renumberLines (1000);		//	max 999 bom items

			//	Order Lines with non-stocked BOMs
			MOrderLine[] lines = getLines (where, "ORDER BY Line");
			for (MOrderLine line : lines) {
				MProduct product = MProduct.get (getCtx(), line.getM_Product_ID());
				log.fine(product.getName());
				//	New Lines
				int lineNo = line.getLine ();
				MBOMProduct[] boms = MBOMProduct.getBOMLines (product);
				ArrayList<MOrderLine> newLines = new ArrayList<MOrderLine>();
				for (MBOMProduct bom : boms) {
					MOrderLine newLine = new MOrderLine (this);
					newLine.setLine (++lineNo);
					newLine.setM_Product_ID (bom.getComponent ()
							.getM_Product_ID ());
					newLine.setC_UOM_ID (bom.getComponent ().getC_UOM_ID ());
					newLine.setQty (line.getQtyOrdered ().multiply (
							bom.getBOMQty ()));
					if (bom.getDescription () != null)
						newLine.setDescription (bom.getDescription ());
					//
					newLine.setPrice ();
					newLines.add(newLine);
				}
				PO.saveAll(get_Trx(), newLines);
				//	Convert into Comment Line
				line.setM_Product_ID (0);
				line.setM_AttributeSetInstance_ID (0);
				line.setPrice (Env.ZERO);
				line.setPriceLimit (Env.ZERO);
				line.setPriceList (Env.ZERO);
				line.setLineNetAmt (Env.ZERO);
				line.setFreightAmt (Env.ZERO);
				//
				String description = product.getName ();
				if (product.getDescription () != null)
					description += " " + product.getDescription ();
				if (line.getDescription () != null)
					description += " " + line.getDescription ();
				line.setDescription (description);
			}	//	for all lines with BOM
			PO.saveAll(get_Trx(), Arrays.asList(lines));

			count = QueryUtil.getSQLValue (get_Trx(), sql, getC_Order_ID ());
			renumberLines (10);
		}	//	while count != 0
		return retValue;
	}	//	explodeBOM


	
	/**
	 * 	Reserve Inventory.
	 * 	Counterpart: MInOut.completeIt()
	 * 	@param dt document type or null
	 * @param lines order lines (ordered by M_Product_ID for deadlock prevention)
	 * 	@return true if (un) reserved
	 */
	private boolean reserveStock (MDocType dt)
	{
		if (dt == null)
			dt = MDocType.get(getCtx(), getC_DocType_ID());

		// Reserved quantity and ordered quantity should not be updated for returns
		if (dt.isReturnTrx())
			return true;

		//	Binding
		boolean binding = !dt.isProposal();
		//	Not binding - i.e. Target=0
		if (DOCACTION_Void.equals(getDocAction())
				//	Closing Binding Quotation
				|| (X_C_DocType.DOCSUBTYPESO_Quotation.equals(dt.getDocSubTypeSO())
						&& DOCACTION_Close.equals(getDocAction()))
						|| isDropShip() )
			binding = false;
		boolean isSOTrx = isSOTrx();
		log.fine("Binding=" + binding + " - IsSOTrx=" + isSOTrx);
		//	Force same WH for all but SO/PO
		int header_M_Warehouse_ID = getM_Warehouse_ID();
		if (X_C_DocType.DOCSUBTYPESO_StandardOrder.equals(dt.getDocSubTypeSO())
				|| MDocBaseType.DOCBASETYPE_PurchaseOrder.equals(dt.getDocBaseType()))
			header_M_Warehouse_ID = 0;		//	don't enforce
		if (header_M_Warehouse_ID != 0) // enforce WH
		{
			if( 0 > DB.executeUpdate(get_Trx(),
					"UPDATE C_OrderLine SET M_Warehouse_ID = ?, AD_Org_ID = ? WHERE C_Order_ID = ?",
					new Object[] { header_M_Warehouse_ID, getAD_Org_ID(), getC_Order_ID() }) )
				return false;
		}		

		
		class Record {
			BigDecimal QtyOrdered;
			BigDecimal QtyReserved;
			BigDecimal QtyDelivered;
			int M_Product_ID;
			int M_Warehouse_ID;
			int M_AttributeSetInstance_ID;
		};		
		
		Iterable<Record> lines = QueryUtil.executeQuery(
				get_Trx(), // stocked items only
				"SELECT SUM(L.QtyOrdered), SUM(L.QtyReserved), SUM(L.QtyDelivered), L.M_Product_ID, L.M_Warehouse_ID, L.M_AttributeSetInstance_ID "
				+ "FROM C_OrderLine L, M_Product P "
				+ "WHERE L.C_Order_ID = ? AND P.M_Product_ID = L.M_Product_ID AND P.IsStocked = 'Y' AND P.ProductType = 'I' "
				+ "GROUP BY L.M_Product_ID, L.M_Warehouse_ID, L.M_AttributeSetInstance_ID "
				+ "ORDER BY L.M_Product_ID, L.M_Warehouse_ID, L.M_AttributeSetInstance_ID ",
				new Callback<Record>() {
					@Override
					public Record cast(Object[] row) {
						Record r = new Record();
						r.QtyOrdered = (BigDecimal) row[0];
						r.QtyReserved = (BigDecimal) row[1];
						r.QtyDelivered = (BigDecimal) row[2];
						r.M_Product_ID = ((BigDecimal) row[3]).intValue();
						r.M_Warehouse_ID = ((BigDecimal) row[4]).intValue();
						r.M_AttributeSetInstance_ID = row[5] != null ? ((BigDecimal) row[5]).intValue() : 0;
						return r;
					}
				}, getC_Order_ID());
		
		//	Always check and (un) Reserve Inventory
		List<MStorageDetail> storagesToSave = new ArrayList<MStorageDetail>();
		for (Record line : lines) {

			//	Binding
			BigDecimal target = binding ? line.QtyOrdered : Env.ZERO;
			BigDecimal difference = target.subtract(line.QtyReserved).subtract(line.QtyDelivered);
			if (difference.signum() != 0) {
				log.fine( " - Target=" + target + ",Difference=" + difference
						+ " - Ordered=" + line.QtyOrdered
						+ ",Reserved=" + line.QtyReserved + ",Delivered=" + line.QtyDelivered);

					BigDecimal ordered = isSOTrx ? Env.ZERO : difference;
					int M_Locator_ID = 0;
					//	Get Locator to reserve
					if (line.M_AttributeSetInstance_ID != 0)	//	Get existing Location
						M_Locator_ID = Storage.getLocatorID(line.M_Warehouse_ID,
								line.M_Product_ID, line.M_AttributeSetInstance_ID,
								ordered, get_Trx());
					//	Get default Location
					if (M_Locator_ID == 0)
					{
						MWarehouse wh = MWarehouse.get(getCtx(), line.M_Warehouse_ID);
						M_Locator_ID = wh.getDefaultM_Locator_ID();
					}
					
					MStorageDetail storage = MStorageDetail.getForRead(getCtx(), M_Locator_ID, 
												line.M_Product_ID, line.M_AttributeSetInstance_ID, 
												isSOTrx?X_Ref_Quantity_Type.RESERVED:X_Ref_Quantity_Type.ORDERED, 
												get_Trx());

					if (storage == null) {
						storage = new MStorageDetail(getCtx(), M_Locator_ID, 
										line.M_Product_ID, line.M_AttributeSetInstance_ID, 
										isSOTrx?X_Ref_Quantity_Type.RESERVED:X_Ref_Quantity_Type.ORDERED,
										get_Trx());
					}
					
					storage.setQty(storage.getQty().add(difference));
					storagesToSave.add(storage);
			}
		}	//	reverse inventory
		
		if (!PO.saveAll(get_Trx(), storagesToSave)) {
			m_processMsg = "Could not update storage records";
			return false;
		}
			 
		if( 0 > DB.executeUpdate( get_Trx(),
				"UPDATE C_OrderLine SET QtyReserved = QtyReserved + (CASE ? WHEN 1 THEN QtyOrdered ELSE 0 END) - QtyReserved - QtyDelivered WHERE C_Order_ID = ?",
				new Object[]{ binding ? 1 : 0, getC_Order_ID()} ) )
			return false;
		
		Object[][] volume_weight = QueryUtil.executeQuery(
				get_Trx(), "SELECT SUM(P.Volume * L.QtyOrdered), SUM(P.Weight * L.QtyOrdered) "+
				"FROM C_OrderLine L, M_Product P " + 
				"WHERE L.C_Order_ID = ? AND P.M_Product_ID = L.M_Product_ID",
				getC_Order_ID() );		

		setVolume((BigDecimal) volume_weight[0][0]);
		setWeight((BigDecimal) volume_weight[0][1]);
		return true;
	}	//	reserveStock

	/**
	 * 	Calculate Tax and Total
	 * 	@return true if tax total calculated
	 */
	private boolean calculateTaxTotal()
	{
		log.fine("");
		//	Delete Taxes
		if( 0 > DB.executeUpdate(get_Trx(), "DELETE FROM C_OrderTax WHERE C_Order_ID=?", new Object[]{getC_Order_ID()}) ){
			return false;
		}

		Object[][] taxIDs = QueryUtil.executeQuery(
								get_Trx(), "SELECT DISTINCT C_Tax_ID " +
								"FROM C_OrderLine " +
								"WHERE C_Order_ID = ?",
								getC_Order_ID());
		//	Lines
		ArrayList<MOrderTax> oTaxes = new ArrayList<MOrderTax>();
		for( Object[] taxIDs_row : taxIDs )
		{
			final int taxID = ((BigDecimal) taxIDs_row[0]).intValue();
			MOrderTax oTax = MOrderTax.get(getCtx(), getC_Order_ID(),
					taxID, getPrecision(), get_Trx(), getAD_Client_ID(),
					getAD_Org_ID(), isTaxIncluded());
			oTax.setIsTaxIncluded(isTaxIncluded());
			if (!oTax.calculateTaxFromLines())
				return false;
			oTaxes.add(oTax);
		}
		if (!PO.saveAll(get_Trx(), oTaxes))
			return false;

		Object[][] sumLineNet = QueryUtil.executeQuery(
				get_Trx(), "SELECT SUM(LineNetAmt) FROM C_OrderLine WHERE C_Order_ID = ?", 
				getC_Order_ID());
		

		//	Taxes
		BigDecimal totalLines = (BigDecimal) sumLineNet[0][0];
		BigDecimal grandTotal = totalLines;
		MOrderTax[] taxes = getTaxes();
		for (MOrderTax oTax : taxes) {
			MTax tax = oTax.getTax();
			if (tax.isSummary())
			{
				MTax[] cTaxes = tax.getChildTaxes(false);
				List<MOrderTax> newOTaxes = new ArrayList<MOrderTax>();
				for (MTax cTax : cTaxes) {
					BigDecimal taxAmt = cTax.calculateTax(oTax.getTaxBaseAmt(), isTaxIncluded(), getPrecision());
					//
					MOrderTax newOTax = new MOrderTax(getCtx(), 0, get_Trx());
					newOTax.setClientOrg(this);
					newOTax.setC_Order_ID(getC_Order_ID());
					newOTax.setC_Tax_ID(cTax.getC_Tax_ID());
					newOTax.setPrecision(getPrecision());
					newOTax.setIsTaxIncluded(isTaxIncluded());
					newOTax.setTaxBaseAmt(oTax.getTaxBaseAmt());
					newOTax.setTaxAmt(taxAmt);
					
					newOTaxes.add(newOTax);
					//
					if (!isTaxIncluded())
						grandTotal = grandTotal.add(taxAmt);
				}

				if (!PO.saveAll(get_Trx(), newOTaxes))					
					return false;
				
				if (!oTax.delete(true, get_Trx()))
					return false;
			}
			else
			{
				if (!isTaxIncluded())
					grandTotal = grandTotal.add(oTax.getTaxAmt());
			}
		}

		//
		setTotalLines(totalLines);
		setGrandTotal(grandTotal);
		return true;
	}	//	calculateTaxTotal


	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	public boolean  approveIt()
	{
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt


	/**************************************************************************
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		String DocSubTypeSO = dt.getDocSubTypeSO();

		//	Just prepare
		if (DOCACTION_Prepare.equals(getDocAction()))
		{
			setProcessed(false);
			return DocActionConstants.STATUS_InProgress;
		}

		if(!isReturnTrx())
		{
			//	Offers
			if (X_C_DocType.DOCSUBTYPESO_Proposal.equals(DocSubTypeSO)
					|| X_C_DocType.DOCSUBTYPESO_Quotation.equals(DocSubTypeSO))
			{
				//	Binding
				if (X_C_DocType.DOCSUBTYPESO_Quotation.equals(DocSubTypeSO))
					reserveStock(dt);
				//	User Validation
				return DocActionConstants.STATUS_Completed;
			}
			//	Waiting Payment - until we have a payment
			if (!m_forceCreation
					&& X_C_DocType.DOCSUBTYPESO_PrepayOrder.equals(DocSubTypeSO)
					&& (getC_Payment_ID() == 0) && (getC_CashLine_ID() == 0))
			{
				setProcessed(true);
				return DocActionConstants.STATUS_WaitingPayment;
			}
		}

		//
		log.info(toString());
		StringBuffer info = new StringBuffer();

		/* nnayak - Bug 1720003 - We need to set the processed flag so the Tax Summary Line
		does not get recreated in the afterSave procedure of the MOrderLine class */
		setProcessed(true);

		//	Create SO Shipment - Force Shipment
		MInOut shipment = null;
		if (X_C_DocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO)		//	(W)illCall(I)nvoice
				|| X_C_DocType.DOCSUBTYPESO_WarehouseOrder.equals(DocSubTypeSO)	//	(W)illCall(P)ickup
				|| X_C_DocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO)			//	(W)alkIn(R)eceipt
				|| X_C_DocType.DOCSUBTYPESO_PrepayOrder.equals(DocSubTypeSO))
		{
			if (!DELIVERYRULE_Force.equals(getDeliveryRule()))
			{
				MWarehouse wh = MWarehouse.get (getCtx(), getM_Warehouse_ID());
				if (!wh.isDisallowNegativeInv())
					setDeliveryRule(DELIVERYRULE_Force);
			}
			//
			shipment = generateShipment (dt, null);
			if (shipment == null)
				return DocActionConstants.STATUS_Invalid;
			info.append("@M_InOut_ID@: ").append(shipment.getDocumentNo());
			String msg = shipment.getProcessMsg();
			if ((msg != null) && (msg.length() > 0))
				info.append(" (").append(msg).append(")");
		}	//	Shipment


		//	Create SO Invoice - Always invoice complete Order
		if ( X_C_DocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO)
				|| X_C_DocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO)
				|| X_C_DocType.DOCSUBTYPESO_PrepayOrder.equals(DocSubTypeSO))
		{
			MInvoice invoice = createInvoice (dt, shipment, null);
			if (invoice == null)
				return DocActionConstants.STATUS_Invalid;
			info.append(" - @C_Invoice_ID@: ").append(invoice.getDocumentNo());
			String msg = invoice.getProcessMsg();
			if ((msg != null) && (msg.length() > 0))
				info.append(" (").append(msg).append(")");
		}	//	Invoice

		//	Counter Documents

		if (dt.isCreateCounter())
		{
			MOrder counter = createCounterDoc();
			if (counter != null)
				info.append(" - @CounterDoc@: @Order@=").append(counter.getDocumentNo());
		}
		m_processMsg = info.toString();
		return DocActionConstants.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Create Invoice
	 *	@param dt order document type
	 *	@param shipment optional shipment
	 *	@param invoiceDate invoice date
	 *	@return invoice or null
	 */
	private MInvoice createInvoice (MDocType dt, MInOut shipment, Timestamp invoiceDate)
	{
		log.info(dt.toString());
		MInvoice invoice = new MInvoice (this, dt.getC_DocTypeInvoice_ID(), invoiceDate);
		if (!invoice.save(get_Trx()))
		{
			m_processMsg = "Could not create Invoice";
			return null;
		}

		//	If we have a Shipment - use that as a base
		if (shipment != null)
		{
			if (!INVOICERULE_AfterDelivery.equals(getInvoiceRule()))
				setInvoiceRule(INVOICERULE_AfterDelivery);
			//
			MInOutLine[] sLines = shipment.getLines(false);
			ArrayList<MInvoiceLine> iLines = new ArrayList<MInvoiceLine>();
			for (MInOutLine sLine : sLines)
			{
				MInvoiceLine iLine = new MInvoiceLine(invoice);
				iLine.setShipLine(sLine);
				//	Qty = Delivered
				iLine.setQtyEntered(sLine.getQtyEntered());
				iLine.setQtyInvoiced(sLine.getMovementQty());
				iLines.add(iLine);
				//
				sLine.setIsInvoiced(true);
			}
			if (!PO.saveAll(get_Trx(), iLines))
			{
				m_processMsg = "Could not create Invoice Line from Shipment Line";
				return null;
			}
			if (!PO.saveAll(get_Trx(), Arrays.asList(sLines)))
			{
				log.warning("Could not update Shipment lines");
			}
		}
		else	//	Create Invoice from Order
		{
			if (!INVOICERULE_Immediate.equals(getInvoiceRule()))
				setInvoiceRule(INVOICERULE_Immediate);
			//
			MOrderLine[] oLines = getLines();
			ArrayList<MInvoiceLine> iLines = new ArrayList<MInvoiceLine>();
			for (MOrderLine oLine : oLines) {
				//
				MInvoiceLine iLine = new MInvoiceLine(invoice);
				iLine.setOrderLine(oLine);
				//	Qty = Ordered - Invoiced
				iLine.setQtyInvoiced(oLine.getQtyOrdered().subtract(oLine.getQtyInvoiced()));
				if (oLine.getQtyOrdered().compareTo(oLine.getQtyEntered()) == 0)
					iLine.setQtyEntered(iLine.getQtyInvoiced());
				else
					iLine.setQtyEntered(iLine.getQtyInvoiced().multiply(oLine.getQtyEntered())
							.divide(oLine.getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP));
				iLines.add(iLine);
			}
			if (!PO.saveAll(get_Trx(),iLines))
			{
				m_processMsg = "Could not create Invoice Line from Order Line";
				return null;
			}
		}
		//	Manually Process Invoice
		invoice.setC_CashBook_ID(getC_CashBook_ID());
		boolean successInv = DocumentEngine.processIt(invoice, DocActionConstants.ACTION_Complete);
		invoice.save(get_Trx());
		setC_CashLine_ID(invoice.getC_CashLine_ID());
		if (!successInv)
		{
			m_processMsg = "@C_Invoice_ID@: " + invoice.getProcessMsg();
			return null;
		}
		return invoice;
	}	//	createInvoice

	/**
	 * 	Create Counter Document
	 * 	@return counter order
	 */
	private MOrder createCounterDoc()
	{
		//	Is this itself a counter doc ?
		if (getRef_Order_ID() != 0)
			return null;

		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		//jz int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_TrxName());
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_Trx());
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		//jz MBPartner bp = MBPartner.get (getCtx(), getC_BPartner_ID());
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_Trx());
		int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
		if (counterAD_Org_ID == 0)
			return null;

		//jz MBPartner counterBP = MBPartner.get (getCtx(), counterC_BPartner_ID);
		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, get_Trx());
		MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID, null);
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
		MOrder counter = copyFrom (this, getDateOrdered(),
				C_DocTypeTarget_ID, true, false, get_Trx());
		MDocType dt = MDocType.get(getCtx(), C_DocTypeTarget_ID);
		if (!dt.isDocNoControlled())
			counter.setDocumentNo(getDocumentNo());		//	copy if manual
		//
		counter.setAD_Org_ID(counterAD_Org_ID);
		counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
		//
		counter.setBPartner(counterBP);
		counter.setDatePromised(getDatePromised());		// default is date ordered
		//	Refernces (Should not be required
		counter.setSalesRep_ID(getSalesRep_ID());
		counter.save(get_Trx());

		//	Update copied lines
		MOrderLine[] counterLines = counter.getLines();
		for (MOrderLine counterLine : counterLines) {
			counterLine.setOrder(counter);	//	copies header values (BP, etc.)
			counterLine.setPrice();
			counterLine.setTax();
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
			// touch order to recalculate tax and totals
			counter.setIsActive(counter.isActive());
			counter.save();
		}
		return counter;
	}	//	createCounterDoc

	/**
	 * 	Void Document.
	 * 	Set Qtys to 0 - Sales: reverse all documents
	 * 	@return true if success
	 */
	public boolean voidIt()
	{
		//if it is a purchase order and has a matching receipt or Invoice, it can not be voided.
		if(!isSOTrx())
			if(isPOMatched())
			{
				m_processMsg="Cannot void the PO which has a matched Receipt or Invoice";
				return false;
			}

		log.info(toString());
		if (0 > DB.executeUpdate(get_Trx(), 
				"UPDATE C_OrderLine " 
						+ "SET QtyLostSales = QtyOrdered, "
						+ "QtyEntered = 0, " 
						+ "QtyOrdered = 0, " 
						+ "LineNetAmt = 0, " 
						+ "Description = Description || ' | ' || ? || ' (' || QtyOrdered || ')' "
						+ "WHERE C_Order_ID = ? AND QtyOrdered <> 0", 
				new Object[] { Msg.getMsg(getCtx(), "Voided"), getC_Order_ID() })) {
			return false;
		}
		
		//	Update Taxes to zero
		if (0 > DB.executeUpdate(get_Trx(), 
				"UPDATE C_OrderTax " 
						+ "SET TaxAmt = 0, " 
						+ "TaxBaseAmt = 0 " 
						+ "WHERE C_Order_ID = ? ", 
				new Object[] { getC_Order_ID() })) {
			return false;
		}
		

		addDescription(Msg.getMsg(getCtx(), "Voided"));
		//	Clear Reservations
		if (!reserveStock(null))
		{
			m_processMsg = "Cannot unreserve Stock (void)";
			return false;
		}

		if (!createReversals())
			return false;
		
		setTotalLines(Env.ZERO);
		setGrandTotal(Env.ZERO);

		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt

	/**
	 * 	Create Shipment/Invoice Reversals
	 * 	@return true if success
	 */
	private boolean createReversals()
	{
		//	Cancel only Sales
		if (!isSOTrx())
			return true;

		log.info("");
		StringBuffer info = new StringBuffer();

		//	Reverse All *Shipments*
		info.append("@M_InOut_ID@:");
		MInOut[] shipments = getShipments(false);	//	get all (line based)
		for (MInOut ship : shipments) {
			//	if closed - ignore
			if (X_M_InOut.DOCSTATUS_Closed.equals(ship.getDocStatus())
					|| X_M_InOut.DOCSTATUS_Reversed.equals(ship.getDocStatus())
					|| X_M_InOut.DOCSTATUS_Voided.equals(ship.getDocStatus()) )
				continue;
			ship.set_Trx(get_Trx());

			//	If not completed - void - otherwise reverse it
			if (!X_M_InOut.DOCSTATUS_Completed.equals(ship.getDocStatus()))
			{
				if (DocumentEngine.processIt(ship, DocActionConstants.ACTION_Void))
					ship.setDocStatus(X_M_InOut.DOCSTATUS_Voided);
			}
			//	Create new Reversal with only that order
			else if (!ship.isOnlyForOrder(this))
			{
				ship.reverseCorrectIt(this);
				//	shipLine.setDocStatus(MInOut.DOCSTATUS_Reversed);
				info.append(" Parial ").append(ship.getDocumentNo());
			}
			else if (DocumentEngine.processIt(ship, DocActionConstants.ACTION_Reverse_Correct)) //	completed shipment
			{
				ship.setDocStatus(X_M_InOut.DOCSTATUS_Reversed);
				info.append(" ").append(ship.getDocumentNo());
			}
			else
			{
				m_processMsg = "Could not reverse Shipment " + ship;
				return false;
			}
			ship.setDocAction(X_M_InOut.DOCACTION_None);
		}	//	for all shipments
		PO.saveAll(get_Trx(), Arrays.asList(shipments));

		//	Reverse All *Invoices*
		info.append(" - @C_Invoice_ID@:");
		MInvoice[] invoices = getInvoices(false);	//	get all (line based)
		for (MInvoice invoice : invoices) {
			//	if closed - ignore
			if (X_C_Invoice.DOCSTATUS_Closed.equals(invoice.getDocStatus())
					|| X_C_Invoice.DOCSTATUS_Reversed.equals(invoice.getDocStatus())
					|| X_C_Invoice.DOCSTATUS_Voided.equals(invoice.getDocStatus()) )
				continue;
			invoice.set_Trx(get_Trx());

			//	If not completed - void - otherwise reverse it
			if (!X_C_Invoice.DOCSTATUS_Completed.equals(invoice.getDocStatus()))
			{
				if (DocumentEngine.processIt(invoice, DocActionConstants.ACTION_Void))
					invoice.setDocStatus(X_C_Invoice.DOCSTATUS_Voided);
			}
			else if (DocumentEngine.processIt(invoice, DocActionConstants.ACTION_Reverse_Correct))	//	completed invoice
			{
				invoice.setDocStatus(X_C_Invoice.DOCSTATUS_Reversed);
				info.append(" ").append(invoice.getDocumentNo());
			}
			else
			{
				m_processMsg = "Could not reverse Invoice " + invoice;
				return false;
			}
			invoice.setDocAction(X_C_Invoice.DOCACTION_None);
		}	//	for all shipments
		PO.saveAll(get_Trx(), Arrays.asList(invoices));

		//	Reverse All *RMAs*
		info.append("@C_Order_ID@:");
		MOrder[] rmas = getRMAs();
		for (MOrder rma : rmas) {
			//	if closed - ignore
			if (X_C_Order.DOCSTATUS_Closed.equals(rma.getDocStatus())
					|| X_C_Order.DOCSTATUS_Reversed.equals(rma.getDocStatus())
					|| X_C_Order.DOCSTATUS_Voided.equals(rma.getDocStatus()) )
				continue;
			rma.set_Trx(get_Trx());

			//	If not completed - void - otherwise reverse it
			if (!X_C_Order.DOCSTATUS_Completed.equals(rma.getDocStatus()))
			{
				if (DocumentEngine.processIt(rma, DocActionConstants.ACTION_Void))
					rma.setDocStatus(X_M_InOut.DOCSTATUS_Voided);
			}
			//	Create new Reversal with only that order
			else if (DocumentEngine.processIt(rma, DocActionConstants.ACTION_Reverse_Correct)) //	completed shipment
			{
				rma.setDocStatus(X_C_Order.DOCSTATUS_Reversed);
				info.append(" ").append(rma.getDocumentNo());
			}
			else
			{
				m_processMsg = "Could not reverse RMA " + rma;
				return false;
			}
			rma.setDocAction(X_M_InOut.DOCACTION_None);
		}	//	for all shipments
		PO.saveAll(get_Trx(), Arrays.asList(rmas));

		
		// Reverse incomplete tasks
		if(!reverseTasks(true)) {
			m_processMsg = Msg.getMsg(getCtx(), "CannotReverseTasks");
			return false;
		}
		
		m_processMsg = info.toString();
		return true;
	}	//	createReversals


	/**
	 * 	Close Document.
	 * 	Cancel not delivered Quantities
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info(toString());
		//	Close Not delivered Qty - SO/PO
		MOrderLine[] lines = getLines();
		ArrayList<MOrderLine> linesToSave = new ArrayList<MOrderLine>();
		for (MOrderLine line : lines) {
			BigDecimal old = line.getQtyOrdered();
			if (old.compareTo(line.getQtyDelivered()) != 0)
			{
				line.setQtyLostSales(line.getQtyOrdered().subtract(line.getQtyDelivered()));
				line.setQtyOrdered(line.getQtyDelivered());
				//	QtyEntered unchanged
				line.addDescription("Close (" + old + ")");
				linesToSave.add(line);
			}
		}
		PO.saveAll(get_Trx(), linesToSave);
		
		recomputeTaxes();
		
		
		//	Clear Reservations
		if (!reserveStock(null))
		{
			m_processMsg = "Cannot unreserve Stock (close)";
			return false;
		}
		
		// Reverse incomplete tasks
		if(!reverseTasks(true)) {
			m_processMsg = Msg.getMsg(getCtx(), "CannotReverseTasks");
			return false;
		}
			
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction - same void
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());
		return DocumentEngine.processIt(this, DocActionConstants.ACTION_Void);
	}	//	reverseCorrectionIt

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
	 * 	Re-activate.
	 * 	@return true if success
	 */
	public boolean reActivateIt()
	{
		log.info(toString());

		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		String DocSubTypeSO = dt.getDocSubTypeSO();

		//	Replace Prepay with POS to revert all doc
		if (X_C_DocType.DOCSUBTYPESO_PrepayOrder.equals (DocSubTypeSO))
		{
			MDocType newDT = null;
			MDocType[] dts = MDocType.getOfClient (getCtx());
			for (MDocType type : dts) {
				if (X_C_DocType.DOCSUBTYPESO_PrepayOrder.equals(type.getDocSubTypeSO()))
				{
					if (type.isDefault() || (newDT == null))
						newDT = type;
				}
			}
			if (newDT == null)
				return false;
			else
			{
				setC_DocType_ID (newDT.getC_DocType_ID());
				setIsReturnTrx(newDT.isReturnTrx());
			}
		}

		//	PO - just re-open
		if (!isSOTrx())
			log.info("Existing documents not modified - " + dt);
		//	Reverse Direct Documents
		else if (X_C_DocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO)	//	(W)illCall(I)nvoice
				|| X_C_DocType.DOCSUBTYPESO_WarehouseOrder.equals(DocSubTypeSO)	//	(W)illCall(P)ickup
				|| X_C_DocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO))			//	(W)alkIn(R)eceipt
		{
			if (!createReversals())
				return false;
		}
		else
		{
			log.info("Existing documents not modified - SubType=" + DocSubTypeSO);
		}

		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
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
		append(Msg.translate(getCtx(),"GrandTotal")).append("=").append(getGrandTotal());
		//	 - Description
		if ((getDescription() != null) && (getDescription().length() > 0))
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
	
	/**************************************************************************
	 * 	Lock order for invoicing so two processes cannot invoice simultaneously.
	 * 	@return true if locked
	 */
	public boolean lockForInvoicing()
	{
		String sql = "UPDATE C_Order" 
			+ " SET Invoicing='Y' WHERE Invoicing='N' AND "
			+ " C_Order_ID = ? ";
		boolean success = DB.executeUpdate(get_Trx(), sql, getC_Order_ID()) == 1;	
		if (success)
			log.fine("success");
		else
			log.log(Level.WARNING, "failed");
		return success;
		
	}	//	lockForInvoicing

	/**************************************************************************
	 * 	Release order lock.
	 * 	@return true if unlocked
	 */
	public boolean unlockForInvoicing(Trx trx)
	{
		String sql = "UPDATE C_Order" 
			+ " SET Invoicing='N' WHERE Invoicing='Y' AND "
			+ " C_Order_ID = ? ";
		boolean success = DB.executeUpdate(trx, sql, getC_Order_ID()) == 1;	
		if (success)
			log.fine("success");
		else
			log.log(Level.WARNING, "failed");
		return success;
		
	}	//	unlockForInvoicing


	/**
	 * 	Generate Shipment
	 *	@param dt order document type
	 *	@param movementDate optional movement date (default today)
	 *	@return shipment or null
	 */
	public MInOut generateShipment (MDocType dt, Timestamp movementDate)
	{
		ArrayList<MInOutLine> ioLines = new ArrayList<MInOutLine>();
		
		log.info("For " + dt);
		MInOut shipment = new MInOut (this, dt.getC_DocTypeShipment_ID(), movementDate);
		if (!shipment.save(get_Trx()))
		{
			m_processMsg = "Could not create Shipment";
			return null;
		}

		MClient client = MClient.get(getCtx());
		MOrderLine[] olines = getLines ();
		for (MOrderLine oline : olines) {
			//log.fine("check: " + line);
			BigDecimal toDeliver = oline.getQtyOrdered()
			.subtract(oline.getQtyDelivered());
			MProduct product = oline.getProduct();

			//	Comments & lines w/o product & services
			if (((product == null) || !product.isStocked())
					&& ((oline.getQtyOrdered().signum() == 0 	//	comments
					)
					|| (toDeliver.signum() != 0)))		//	lines w/o product
			{
				ioLines.addAll(createLine (shipment, oline, toDeliver, null, false));
				continue;
			}

			//	Nothing to Deliver
			if ((product != null) && (toDeliver.signum() == 0))
				continue;

			//	Stored Product
			MProductCategory pc = MProductCategory.get(getCtx(), product.getM_Product_Category_ID());
			String MMPolicy = pc.getMMPolicy();
			if ((MMPolicy == null) || (MMPolicy.length() == 0))
				MMPolicy = client.getMMPolicy();
			//
			List<Storage.VO> storages = Storage.getWarehouse(getCtx(),
					oline.getM_Warehouse_ID(), oline.getM_Product_ID(), oline.getM_AttributeSetInstance_ID(),
					product.getM_AttributeSet_ID(), oline.getM_AttributeSetInstance_ID()==0, movementDate,
					X_AD_Client.MMPOLICY_FiFo.equals(MMPolicy), get_Trx());

			//i++;
			/*
			 * BECO
			 */
			if(storages.size() >= 1){
				
				int defaultLocator = 0;
				MWarehouse wh = MWarehouse.get(getCtx(), oline.getM_Warehouse_ID());
				defaultLocator = wh.getDefaultM_Locator_ID();
				
				boolean inStoreLocator = false;
				if(defaultLocator != 0){
					//Verificamos que el m_storage del locator por defecto este creado
					for (Storage.VO storage : storages) {
						if(storage.getM_Locator_ID() == defaultLocator){
							inStoreLocator = true;
							break;
						}
					}
					
					//Si no esta creado el m_storage del locator por defecto lo creamos para poder realizar la transaccion sobre este
					if(!inStoreLocator){
				
								storages = null;
								
								MStorageDetail.getCreate(getCtx(), defaultLocator, 
										oline.getM_Product_ID(), oline.getM_AttributeSetInstance_ID(),X_Ref_Quantity_Type.ON_HAND, get_Trx());
								
								storages = Storage.getWarehouse(getCtx(),
								oline.getM_Warehouse_ID(), oline.getM_Product_ID(), oline.getM_AttributeSetInstance_ID(),
								product.getM_AttributeSet_ID(), oline.getM_AttributeSetInstance_ID()==0, movementDate,
								X_AD_Client.MMPOLICY_FiFo.equals(MMPolicy), get_Trx());
					}
				}
			}
			//Fin BECO
			
			ioLines.addAll(createLine (shipment, oline, toDeliver, storages, true));
		}	//	for all order lines

		if(!PO.saveAll(get_Trx(), ioLines)) {
			m_processMsg = "Could not save shipment lines";
			return null;
		}
		//	Manually Process Shipment
		boolean successShipment = DocumentEngine.processIt(shipment, DocActionConstants.ACTION_Complete);
		shipment.save(get_Trx());
		if (!successShipment)
		{
			m_processMsg = "@M_InOut_ID@: " + shipment.getProcessMsg();
			return null;
		}
		return shipment;
	}	//	generate



	/**************************************************************************
	 * 	Create Line
	 *	@param order order
	 *	@param orderLine line
	 *	@param qty qty
	 *	@param storages storage info
	 *	@param force force delivery
	 */
	private ArrayList<MInOutLine> createLine (MInOut shipment, MOrderLine orderLine, BigDecimal qty,
			List<Storage.VO> storages, boolean force)
	{
		ArrayList<MInOutLine> lines = new ArrayList<MInOutLine>();
		//	Non Inventory Lines
		if (storages == null)
		{
			MInOutLine line = new MInOutLine (shipment);
			line.setOrderLine(orderLine, 0, Env.ZERO);
			line.setQty(qty);	//	Correct UOM
			if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
				line.setQtyEntered(qty
						.multiply(orderLine.getQtyEntered())
						.divide(orderLine.getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP));
			line.setLine(orderLine.getLine());
			//log.fine(line.toString());
			lines.add(line);
			return lines;
		}

		//	Product
		MProduct product = orderLine.getProduct();
		boolean linePerASI = false;
		if (product.getM_AttributeSet_ID() != 0)
		{
			MAttributeSet mas = MAttributeSet.get(getCtx(), product.getM_AttributeSet_ID());
			linePerASI = mas.isInstanceAttribute();
		}

		BigDecimal toDeliver = qty;
		for (Storage.VO storage : storages) {
			BigDecimal deliver = toDeliver;
			BigDecimal qtyAvailable = storage.getAvailableQty();
			if(qtyAvailable.compareTo(Env.ZERO) <= 0)
				break;

			//	Not enough On Hand
			if (deliver.compareTo(qtyAvailable) > 0 )
			{
				deliver = qtyAvailable;
			}
			if ((deliver.signum() == 0) || (qtyAvailable.signum() <= 0))	//	zero deliver
				continue;
			int M_Locator_ID = storage.getM_Locator_ID();
			//
			MInOutLine line = null;
			if (!linePerASI)	//	find line with Locator
			{
				for (int ll = 0; ll < lines.size(); ll++)
				{
					MInOutLine test = lines.get(ll);
					if (test.getM_Locator_ID() == M_Locator_ID)
					{
						line = test;
						break;
					}
				}
			}
			if (line == null)	//	new line
			{
				line = new MInOutLine (shipment);
				line.setOrderLine(orderLine, M_Locator_ID, isSOTrx() ? deliver : Env.ZERO);
				line.setQty(deliver);
				lines.add(line);
			}
			else				//	existing line
				line.setQty(line.getMovementQty().add(deliver));
			if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
				line.setQtyEntered(line.getMovementQty().multiply(orderLine.getQtyEntered())
						.divide(orderLine.getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP));
			if (linePerASI)
				line.setM_AttributeSetInstance_ID(storage.getM_AttributeSetInstance_ID());
			//log.fine("ToDeliver=" + qty + "/" + deliver + " - " + line);
			toDeliver = toDeliver.subtract(deliver);
			//
			if (toDeliver.signum() == 0)
				break;
		}

		// Force remaining quantity to negative
		if (toDeliver.signum() != 0)
		{
			BigDecimal deliver = toDeliver;
			int M_Locator_ID=0;
			if(storages.size()>0)
				M_Locator_ID= storages.get(0).getM_Locator_ID();
			if (M_Locator_ID == 0)		//	Get default Location
			{
				int M_Warehouse_ID = orderLine.getM_Warehouse_ID();
				M_Locator_ID = MProductLocator.getFirstM_Locator_ID (product, M_Warehouse_ID);
				if (M_Locator_ID == 0)
				{
					MWarehouse wh = MWarehouse.get (getCtx(), M_Warehouse_ID);
					M_Locator_ID = wh.getDefaultM_Locator_ID();
				}
			}
			//
			MInOutLine line = null;
			if (!linePerASI)	//	find line with Locator
			{
				for (int ll = 0; ll < lines.size(); ll++)
				{
					MInOutLine test = lines.get(ll);
					if (test.getM_Locator_ID() == M_Locator_ID)
					{
						line = test;
						break;
					}
				}
			}
			if (line == null)	//	new line
			{
				line = new MInOutLine (shipment);
				line.setOrderLine(orderLine, M_Locator_ID, isSOTrx() ? deliver : Env.ZERO);
				line.setQty(deliver);
				lines.add(line);
			}
			else				//	existing line
				line.setQty(line.getMovementQty().add(deliver));
			if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
				line.setQtyEntered(line.getMovementQty().multiply(orderLine.getQtyEntered())
						.divide(orderLine.getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP));

		}
		return lines;
	}	//	createLine


	/**
	 * This retarded method is the GWT equivalent of GridTab.loadDependentInfo().
	 * @return
	 */
	private String getOrderTypeFromTargetDocType(int windowNo)
	{
		String orderType = "--";
		int C_DocTypeTarget_ID = getCtx().getContextAsInt(windowNo, "C_DocTypeTarget_ID");

		if( C_DocTypeTarget_ID != 0 )
		{
			String sql = "SELECT DocSubTypeSO FROM C_DocType WHERE C_DocType_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, C_DocTypeTarget_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
					orderType = rs.getString(1);
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
		}

		if(orderType == null)
			return "";

		return orderType;
	}

	/**
	 * 	Check if the PO is Matched with Receipt or Invoice.
	 * 	@return true if PO is matched
	 */
	private boolean isPOMatched( )
	{
		String sql = "SELECT po.C_Orderline_ID FROM M_MatchPO po INNER JOIN C_Orderline o ON (po.C_Orderline_ID=o.C_Orderline_ID) " +
		"WHERE o.C_Order_ID=? AND po.isActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1,getC_Order_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
				return true;
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
		return false;
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
		return new QueryParams("SELECT DISTINCT AD_Org_ID FROM C_OrderLine WHERE C_Order_ID = ?",
				new Object[] { getC_Order_ID() });
	}

	
	private boolean reverseTasks(boolean onlyIncomplete) {
		/* Check if WMS licensed */
		SysEnv se = SysEnv.get("CWMS");
		if ((se == null) || !se.checkLicense())
			return true;

		Class<?>[] parameterTypes = new Class[]{MOrder.class, boolean.class};
		Object[] args = new Object[]{this, onlyIncomplete};

		try
		{
			Class<?> c = Class.forName("org.compiere.cwms.util.MWarehouseTaskUtil");
			if(c == null)
				return false;
			
			Object instance = c.newInstance();
			Method m = c.getMethod("reverseOrderTasks", parameterTypes);
			return (Boolean) m.invoke(instance, args);
		}
		catch (Exception e)
		{
			log.warning("Error reversing Warehouse Tasks:" + e.toString());
		}
		
		return false;
		
	}
	
	private void setBPartnerLocation(MBPartner bp){
		//		Set Locations
		MBPartnerLocation[] locs = bp.getLocations(false);
		if (locs != null)
		{
			for (MBPartnerLocation element : locs) {
				if (element.isShipTo())
					super.setC_BPartner_Location_ID(element.getC_BPartner_Location_ID());
				if (element.isBillTo())
					setBill_Location_ID(element.getC_BPartner_Location_ID());
			}
			//	set to first
			if ((getC_BPartner_Location_ID() == 0) && (locs.length > 0))
				super.setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
			if ((getBill_Location_ID() == 0) && (locs.length > 0))
				setBill_Location_ID(locs[0].getC_BPartner_Location_ID());
		}
		if (getC_BPartner_Location_ID() == 0)
			log.log(Level.SEVERE, "MOrder.setBPartner - Has no Ship To Address: " + bp);
		if (getBill_Location_ID() == 0)
			log.log(Level.SEVERE, "MOrder.setBPartner - Has no Bill To Address: " + bp);		
	}
	
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass() != o.getClass())
			return false;
		MOrder that = (MOrder)o;
		if(this.getC_Order_ID() == 0 || that.getC_Order_ID() ==0)
			return false;
		super.equals(o);
		return true;
	}

}	//	MOrder
