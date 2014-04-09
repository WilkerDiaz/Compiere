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
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.framework.PO;
import org.compiere.util.*;

/**
 *	Business Partner Model
 *
 *  @author Jorg Janke
 *  @version $Id: MBPartner.java,v 1.5 2006/09/23 19:38:07 comdivision Exp $
 */
public class MBPartner extends X_C_BPartner
{
    /** Logger for class MBPartner */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartner.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get All Business Partner matching the criteria.
	 * 	If you don't use % in the parameter, it must match exactly
	 *	@param ctx context (for sql security)
	 *	@param bpValue business partner value
	 *	@param bpName business partner name
	 *	@param taxID business partner tax id or ssn
	 *	@param postal location postal
	 *	@param city location city
	 *	@param uValue user value
	 *	@param uName user name
	 *	@param uPhone user phone
	 *	@param email user email
	 *	@param birthday user birthday
	 *	@param andLogic if more than one parameter, use AND logic if true otherwise OR
	 *	@param maxRows max numbers to return
	 *	@return array of business partners
	 */
	public static ArrayList<MBPartner> findAll (Ctx ctx, String bpValue, String bpName, String taxID,
		String postal, String city, 
		String uValue, String uName, String uPhone, String email, Timestamp birthday,
		boolean andLogic, int maxRows)
	{
		ArrayList<MBPartner> list = new ArrayList<MBPartner>();
		int AD_Client_ID = ctx.getAD_Client_ID();
		StringBuffer sql = new StringBuffer("SELECT * FROM C_BPartner bp WHERE bp.AD_Client_ID=?");
		StringBuffer where = new StringBuffer();
		if ("%".equals(bpValue))
			bpValue = null;
		if (!Util.isEmpty(bpValue))
			where.append(DB.getSqlWhere("UPPER(bp.Value)", bpValue.toUpperCase()));
		if ("%".equals(bpName))
			bpName = null;
		if (!Util.isEmpty(bpName))
		{
			if (where.length() > 0)
				where.append(andLogic ? " AND " : " OR ");
			where.append(DB.getSqlWhere("UPPER(bp.Name)", bpName.toUpperCase()));
		}
		if ("%".equals(taxID))
			taxID = null;
		if (!Util.isEmpty(taxID))
		{
			if (where.length() > 0)
				where.append(andLogic ? " AND " : " OR ");
			where.append(DB.getSqlWhere("bp.TaxID", taxID));
		}
		//	Location
		if ("%".equals(postal))
			postal = null;
		if ("%".equals(city))
			city = null;
		if (!Util.isEmpty(postal) || !Util.isEmpty(city))
		{
			if (where.length() > 0)
				where.append(andLogic ? " AND " : " OR ");
			where.append("EXISTS (SELECT * FROM C_BPartner_Location bpl INNER JOIN C_Location loc ON (bpl.C_Location_ID=loc.C_Location_ID) WHERE bp.C_BPartner_ID=bpl.C_BPartner_ID	AND (");
			if (!Util.isEmpty(postal))
				where.append(DB.getSqlWhere("loc.Postal", postal));
			if (!Util.isEmpty(postal) && !Util.isEmpty(city))
				where.append(andLogic ? " AND " : " OR ");
			if (!Util.isEmpty(postal))
				where.append(DB.getSqlWhere("UPPER(loc.City)", city));
			where.append("))");
		}
		//	User
		if ("%".equals(uValue))
			uValue = null;
		if ("%".equals(uName))
			uName = null;
		if ("%".equals(email))
			email = null;
		if (!Util.isEmpty(uValue) || !Util.isEmpty(uName) || !Util.isEmpty(email) || birthday != null)
		{
			if (where.length() > 0)
				where.append(andLogic ? " AND " : " OR ");
			where.append("EXISTS (SELECT * FROM AD_User u WHERE bp.C_BPartner_ID=u.C_BPartner_ID AND (");
			boolean needAndOr = false;
			if (!Util.isEmpty(uValue))
			{
				where.append(DB.getSqlWhere("UPPER(u.Value)", uValue.toUpperCase()));
				needAndOr = true;
			}
			if (!Util.isEmpty(uName))
			{
				if (needAndOr)
					where.append(andLogic ? " AND " : " OR ");
				where.append(DB.getSqlWhere("UPPER(u.Name)", uName.toUpperCase()));
				needAndOr = true;
			}
			if (!Util.isEmpty(email))
			{
				if (needAndOr)
					where.append(andLogic ? " AND " : " OR ");
				where.append(DB.getSqlWhere("UPPER(u.EMail)", email.toUpperCase()));
				needAndOr = true;
			}
			if (birthday != null)
			{
				if (needAndOr)
					where.append(andLogic ? " AND " : " OR ");
				where.append(DB.getSqlWhere("u.Birthday", birthday));
			}
			where.append("))");
		}
		if (where.length() > 0)
			sql.append(" AND (").append(where).append(")");
		
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
		String sql1 = role.addAccessSQL(sql.toString(), "bp", true, false);	// ro
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql1, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add(new MBPartner(ctx, rs, null));
				if (list.size() >= maxRows)
					break;
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql1, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		s_log.fine("#" + list.size());
        return list;
	}	//	getAll
	
	/**
	 * 	Get Empty Template Business Partner
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@return Template Business Partner or null
	 */
	public static MBPartner getTemplate (Ctx ctx, int AD_Client_ID)
	{
		MBPartner template = getBPartnerCashTrx (ctx, AD_Client_ID);
		if (template == null)
			template = new MBPartner (ctx, 0, null);
		//	Reset
		if (template != null)
		{
			template.set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(0));
			template.setValue ("");
			template.setName ("");
			template.setName2 (null);
			template.setDUNS("");
			template.setFirstSale(null);
			//
			template.setSO_CreditLimit (Env.ZERO);
			template.setSO_CreditUsed (Env.ZERO);
			template.setTotalOpenBalance (Env.ZERO);
		//	s_template.setRating(null);
			//
			template.setActualLifeTimeValue(Env.ZERO);
			template.setPotentialLifeTimeValue(Env.ZERO);
			template.setAcqusitionCost(Env.ZERO);
			template.setShareOfCustomer(0);
			template.setSalesVolume(0);
		}
		return template;
	}	//	getTemplate

	/**
	 * 	Get Cash Trx Business Partner
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@return Cash Trx Business Partner or null
	 */
	public static MBPartner getBPartnerCashTrx (Ctx ctx, int AD_Client_ID)
	{
		MBPartner retValue = null;
		String sql= "SELECT AD_CLIENT_ID,AD_LANGUAGE,AD_ORGBP_ID,AD_ORG_ID,ACQUSITIONCOST,ACTUALLIFETIMEVALUE," 
			+ "BPARTNER_PARENT_ID,C_BP_GROUP_ID,C_BP_SIZE_ID,C_BP_STATUS_ID,C_BPARTNER_ID,"
			+ "C_CONSOLIDATIONREFERENCE_ID,C_DUNNING_ID,C_GREETING_ID,C_INDUSTRYCODE_ID,C_INVOICESCHEDULE_ID,"
			+ "C_PAYMENTTERM_ID,DUNS,DELIVERYRULE,DELIVERYVIARULE,DESCRIPTION,DOCUMENTCOPIES,FIRSTSALE,FLATDISCOUNT,"
			+ "FREIGHTCOSTRULE,INVOICERULE,INVOICE_PRINTFORMAT_ID,ISACTIVE,ISCUSTOMER,ISDISCOUNTPRINTED,ISEMPLOYEE,"
			+ "ISONETIME,ISPROSPECT,ISSALESREP,ISSUMMARY,ISTAXEXEMPT,ISVENDOR,M_DISCOUNTSCHEMA_ID,M_PRICELIST_ID,"
			+ "M_RETURNPOLICY_ID,NAICS,NAME,NAME2,NUMBEREMPLOYEES,ORDER_PRINTFORMAT_ID,POREFERENCE,"
			+ "PO_DISCOUNTSCHEMA_ID,PO_PAYMENTTERM_ID,PO_PRICELIST_ID,PO_RETURNPOLICY_ID,PAYMENTRULE,PAYMENTRULEPO,"
			+ "POTENTIALLIFETIMEVALUE,RATING,REFERENCENO,SOCREDITSTATUS,SO_CREDITLIMIT,SO_CREDITUSED,SO_DESCRIPTION,"
			+ "SALESREP_ID,SALESVOLUME,SENDEMAIL,SHAREOFCUSTOMER,SHELFLIFEMINPCT,TAXID,TOTALOPENBALANCE,URL,VALUE "
			+ "FROM C_BPartner WHERE C_BPartner_ID IN (SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo WHERE AD_Client_ID=?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MBPartner (ctx, rs, null);
			else
				s_log.log(Level.SEVERE, "Not found for AD_Client_ID=" + AD_Client_ID);
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
		return retValue;
	}	//	getBPartnerCashTrx

	/**
	 * 	Get BPartner with Value
	 *	@param ctx context 
	 *	@param Value value
	 *	@return BPartner or null
	 */
	public static MBPartner get (Ctx ctx, String Value)
	{
		if (Value == null || Value.length() == 0)
			return null;
		MBPartner retValue = null;
		int AD_Client_ID = ctx.getAD_Client_ID();
		String sql = "SELECT * FROM C_BPartner WHERE Value=? AND AD_Client_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, Value);
			pstmt.setInt(2, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MBPartner(ctx, rs, null);
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
		return retValue;
	}	//	get

	/**
	 * 	Get BPartner with Value (not cached)
	 *	@param ctx context 
	 *	@param Value value
	 *	@return BPartner or null
	 */
	public static MBPartner get (Ctx ctx, int C_BPartner_ID)
	{
		MBPartner retValue = null;
		int AD_Client_ID = ctx.getAD_Client_ID();
		String sql = "SELECT * FROM C_BPartner WHERE C_BPartner_ID=? AND AD_Client_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID);
			pstmt.setInt(2, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MBPartner(ctx, rs, null);
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
		return retValue;
	}	//	get

	/**
	 * 	Get Not Invoiced Shipment Value
	 *	@param C_BPartner_ID partner
	 *	@return value in accounting currency
	 */
	public static BigDecimal getNotInvoicedAmt (int C_BPartner_ID)
	{
		BigDecimal retValue = null;
		String sql = "SELECT SUM(COALESCE("
			+ "currencyBase((ol.QtyDelivered-ol.QtyInvoiced)*ol.PriceActual,o.C_Currency_ID,o.DateOrdered, o.AD_Client_ID,o.AD_Org_ID) ,0)) "
			+ "FROM C_OrderLine ol"
			+ " INNER JOIN C_Order o ON (ol.C_Order_ID=o.C_Order_ID) "
			+ "WHERE o.IsSOTrx='Y' AND Bill_BPartner_ID=? "
			+ "AND o.IsInvoiced = 'N' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, C_BPartner_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = rs.getBigDecimal(1);
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
		return retValue;
	}	//	getNotInvoicedAmt

	
	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MBPartner.class);

	
	/**************************************************************************
	 * 	Constructor for new BPartner from Template
	 * 	@param ctx context
	 * 	@param trx p_trx
	 */
	public MBPartner (Ctx ctx, Trx trx)
	{
		this (ctx, -1, trx);
	}	//	MBPartner

	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 * 	@param trx transaction
	 */
	public MBPartner (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBPartner

	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param C_BPartner_ID partner or 0 or -1 (load from template)
	 * 	@param trx transaction
	 */
	public MBPartner (Ctx ctx, int C_BPartner_ID, Trx trx)
	{
		super (ctx, C_BPartner_ID, trx);
		//
		if (C_BPartner_ID == -1)
		{
			initTemplate (ctx.getContextAsInt( "AD_Client_ID"));
			C_BPartner_ID = 0;
		}
		if (C_BPartner_ID == 0)
		{
		//	setValue ("");
		//	setName ("");
		//	setName2 (null);
		//	setDUNS("");
			//
			setIsCustomer (true);
			setIsProspect (true);
			//
			setSendEMail (false);
			setIsOneTime (false);
			setIsVendor (false);
			setIsSummary (false);
			setIsEmployee (false);
			setIsSalesRep (false);
			setIsTaxExempt(false);
			setIsDiscountPrinted(false);
			//
			setSO_CreditLimit (Env.ZERO);
			setSO_CreditUsed (Env.ZERO);
			setTotalOpenBalance (Env.ZERO);
			setSOCreditStatus(SOCREDITSTATUS_NoCreditCheck);
			//
			setFirstSale(null);
			setActualLifeTimeValue(Env.ZERO);
			setPotentialLifeTimeValue(Env.ZERO);
			setAcqusitionCost(Env.ZERO);
			setShareOfCustomer(0);
			setSalesVolume(0);
		}
		log.fine(toString());
	}	//	MBPartner

	/**
	 * 	Import Contstructor
	 *	@param impBP import
	 */
	public MBPartner (X_I_BPartner impBP)
	{
		this (impBP.getCtx(), 0, impBP.get_Trx());
		
		setUpdatedBy(impBP.getUpdatedBy());
		//
		String value = impBP.getValue();
		if (value == null || value.length() == 0)
			value = impBP.getEMail();
		if (value == null || value.length() == 0)
			value = impBP.getContactName();
		impBP.setValue(value);
		String name = impBP.getName();
		if (name == null || name.length() == 0)
			name = impBP.getContactName();
		if (name == null || name.length() == 0)
			name = impBP.getEMail();
		impBP.setName(name);
		
		PO.copyValues(impBP, this, impBP.getAD_Client_ID(), impBP.getAD_Org_ID());
		setValue(value);
		setName(name);
	}	//	MBPartner
	
	
	/** Users							*/
	private MUser[]					m_contacts = null;
	/** Addressed						*/
	private MBPartnerLocation[]		m_locations = null;
	/** BP Bank Accounts				*/
	private MBPBankAccount[]		m_accounts = null;
	/** Prim Address					*/
	private Integer					m_primaryC_BPartner_Location_ID = null;
	/** Prim User						*/
	private Integer					m_primaryAD_User_ID = null;
	/** Credit Limit recently calcualted		*/
	private boolean 				m_TotalOpenBalanceSet = false;
	/** BP Group						*/
	private MBPGroup				m_group = null;
	
	/**
	 * 	Load Default BPartner
	 * 	@param AD_Client_ID client
	 * 	@return true if loaded
	 */
	private boolean initTemplate (int AD_Client_ID)
	{
		if (AD_Client_ID == 0)
			throw new IllegalArgumentException ("Client_ID=0");

		boolean success = true;
		String sql = "SELECT * FROM C_BPartner "
			+ "WHERE C_BPartner_ID=(SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo WHERE AD_Client_ID=?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				success = load (rs);
			else
			{
				load(0, null);
				success = false;
				log.severe ("None found");
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
		setStandardDefaults();
		//	Reset
		set_ValueNoCheck ("C_BPartner_ID", I_ZERO);
		setValue ("");
		setName ("");
		setName2(null);
		return success;
	}	//	getTemplate


	/**
	 * 	Get All Contacts
	 * 	@param reload if true users will be requeried
	 *	@return contacts
	 */
	public MUser[] getContacts (boolean reload)
	{
		if (reload || m_contacts == null || m_contacts.length == 0)
			;
		else
			return m_contacts;
		//
		ArrayList<MUser> list = new ArrayList<MUser>();
		String sql = "SELECT * FROM AD_User WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_BPartner_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MUser (getCtx(), rs, get_Trx()));
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

		m_contacts = new MUser[list.size()];
		list.toArray(m_contacts);
		return m_contacts;
	}	//	getContacts

	/**
	 * 	Get specified or first Contact
	 * 	@param AD_User_ID optional user
	 *	@return contact or null
	 */
	public MUser getContact (int AD_User_ID)
	{
		MUser[] users = getContacts(false);
		if (users.length == 0)
			return null;
		for (int i = 0; AD_User_ID != 0 && i < users.length; i++)
		{
			if (users[i].getAD_User_ID() == AD_User_ID)
				return users[i];
		}
		return users[0];
	}	//	getContact
	
	
	/**
	 * 	Get All Locations
	 * 	@param reload if true locations will be requeried
	 *	@return locations
	 */
	public MBPartnerLocation[] getLocations (boolean reload)
	{
		if (reload || m_locations == null || m_locations.length == 0)
			;
		else
			return m_locations;
		//
		ArrayList<MBPartnerLocation> list = new ArrayList<MBPartnerLocation>();
		String sql = "SELECT * FROM C_BPartner_Location WHERE C_BPartner_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_BPartner_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPartnerLocation (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}

		m_locations = new MBPartnerLocation[list.size()];
		list.toArray(m_locations);
		return m_locations;
	}	//	getLocations

	/**
	 * 	Get explicit or first bill Location
	 * 	@param C_BPartner_Location_ID optional explicit location
	 *	@return location or null
	 */
	public MBPartnerLocation getLocation(int C_BPartner_Location_ID)
	{
		MBPartnerLocation[] locations = getLocations(false);
		if (locations.length == 0)
			return null;
		MBPartnerLocation retValue = null;
		for (MBPartnerLocation element : locations) {
			if (element.getC_BPartner_Location_ID() == C_BPartner_Location_ID)
				return element;
			if (retValue == null && element.isBillTo())
				retValue = element;
		}
		if (retValue == null)
			return locations[0];
		return retValue;
	}	//	getLocation
	
	
	/**
	 * 	Get Bank Accounts
	 * 	@param requery requery
	 *	@return Bank Accounts
	 */
	public MBPBankAccount[] getBankAccounts (boolean requery)
	{
		if (m_accounts != null && m_accounts.length >= 0 && !requery)	//	re-load
			return m_accounts;
		//
		ArrayList<MBPBankAccount> list = new ArrayList<MBPBankAccount>();
		String sql = "SELECT * FROM C_BP_BankAccount WHERE C_BPartner_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_BPartner_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPBankAccount (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}

		m_accounts = new MBPBankAccount[list.size()];
		list.toArray(m_accounts);
		return m_accounts;
	}	//	getBankAccounts

	
	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MBPartner[")
			.append(get_ID())
			.append("-").append(getValue())
			.append(",Name=").append(getName())
			.append(",OpenBalance=").append(getTotalOpenBalance())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Set Client/Org
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 */
	@Override
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	Set Linked Organization.
	 * 	(is Button)
	 *	@param AD_OrgBP_ID 
	 */
	public void setAD_OrgBP_ID (int AD_OrgBP_ID)
	{
		if (AD_OrgBP_ID == 0)
			super.set_Value("AD_OrgBP_ID",null);
		else
			super.setAD_OrgBP_ID(AD_OrgBP_ID);
	}	//	setAD_OrgBP_ID
	
	/** 
	 * 	Get Linked Organization.
	 * 	(is Button)
	 * 	The Business Partner is another Organization 
	 * 	for explicit Inter-Org transactions 
	 * 	@return AD_Org_ID if BP
	 */
	public int getAD_OrgBP_ID_Int() 
	{
		return super.getAD_OrgBP_ID();
	}	//	getAD_OrgBP_ID_Int

	/**
	 * 	Get Primary C_BPartner_Location_ID
	 *	@return C_BPartner_Location_ID
	 */
	public int getPrimaryC_BPartner_Location_ID()
	{
		if (m_primaryC_BPartner_Location_ID == null)
		{
			MBPartnerLocation[] locs = getLocations(false);
			for (int i = 0; m_primaryC_BPartner_Location_ID == null && i < locs.length; i++)
			{
				if (locs[i].isBillTo())
				{
					setPrimaryC_BPartner_Location_ID (locs[i].getC_BPartner_Location_ID());
					break;
				}
			}
			//	get first
			if (m_primaryC_BPartner_Location_ID == null && locs.length > 0)
				setPrimaryC_BPartner_Location_ID (locs[0].getC_BPartner_Location_ID()); 
		}
		if (m_primaryC_BPartner_Location_ID == null)
			return 0;
		return m_primaryC_BPartner_Location_ID.intValue();
	}	//	getPrimaryC_BPartner_Location_ID
	
	/**
	 * 	Get Primary C_BPartner_Location
	 *	@return C_BPartner_Location
	 */
	public MBPartnerLocation getPrimaryC_BPartner_Location()
	{
		if (m_primaryC_BPartner_Location_ID == null)
		{
			m_primaryC_BPartner_Location_ID = getPrimaryC_BPartner_Location_ID();
		}
		if (m_primaryC_BPartner_Location_ID == null)
			return null;
		return new MBPartnerLocation(getCtx(), m_primaryC_BPartner_Location_ID, null);
	}	//	getPrimaryC_BPartner_Location
	
	/**
	 * 	Get Primary AD_User_ID
	 *	@return AD_User_ID
	 */
	public int getPrimaryAD_User_ID()
	{
		if (m_primaryAD_User_ID == null)
		{
			MUser[] users = getContacts(false);
		//	for (int i = 0; i < users.length; i++)
		//	{
		//	}
			if (m_primaryAD_User_ID == null && users.length > 0)
				setPrimaryAD_User_ID(users[0].getAD_User_ID());
		}
		if (m_primaryAD_User_ID == null)
			return 0;
		return m_primaryAD_User_ID.intValue();
	}	//	getPrimaryAD_User_ID

	/**
	 * 	Set Primary C_BPartner_Location_ID
	 *	@param C_BPartner_Location_ID id
	 */
	public void setPrimaryC_BPartner_Location_ID(int C_BPartner_Location_ID)
	{
		m_primaryC_BPartner_Location_ID = Integer.valueOf (C_BPartner_Location_ID);
	}	//	setPrimaryC_BPartner_Location_ID
	
	/**
	 * 	Set Primary AD_User_ID
	 *	@param AD_User_ID id
	 */
	public void setPrimaryAD_User_ID(int AD_User_ID)
	{
		m_primaryAD_User_ID = Integer.valueOf (AD_User_ID);
	}	//	setPrimaryAD_User_ID
	
	
	/**
	 * 	Calculate Total Open Balance and SO_CreditUsed.
	 *  (includes drafted invoices)
	 */
	public void setTotalOpenBalance ()
	{
		// SO Credit Used = SO Invoices - All SO Allocations - (All Receipts - All Receipt Allocations)
		// Open Balance = All Invoices - All Invoice Allocations - (All Receipts - All Receipt Allocations)
		BigDecimal SO_CreditUsed = null;
		BigDecimal TotalOpenBalance = null;
		String sql = "SELECT "
			//	SO Credit Used	= SO Invoices
			+ "COALESCE((SELECT SUM(currencyBaseSpecific(i.GrandTotal,i.C_Currency_ID,i.DateInvoiced,COALESCE(i.C_ConversionType_ID, 0),i.AD_Client_ID,i.AD_Org_ID)) "
				+ "FROM C_Invoice_v i "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID"
				+ " AND i.IsSOTrx='Y' AND i.IsPaid='N' AND i.DocStatus IN('CO','CL')),0) "
			//					- All SO Allocations
			+ "-COALESCE((SELECT SUM(currencyBaseSpecific(a.Amount+a.DiscountAmt+a.WriteOffAmt,h.C_Currency_ID,h.DateAcct,COALESCE(i.C_ConversionType_ID, 0),a.AD_Client_ID,a.AD_Org_ID)) "
				+ "FROM C_AllocationLine a INNER JOIN C_Invoice i ON (a.C_Invoice_ID=i.C_Invoice_ID) "
				+ "INNER JOIN C_AllocationHdr h ON (a.C_AllocationHdr_ID = h.C_AllocationHdr_ID) "
				+ "WHERE a.C_BPartner_ID=bp.C_BPartner_ID AND a.IsActive='Y'"
				+ " AND i.IsSOTrx='Y' AND i.IsPaid='N' AND h.DocStatus IN('CO','CL')),0) "
			//					- Unallocated Receipts	= (All Receipts
			+ "-(SELECT COALESCE(SUM(currencyBaseSpecific(p.PayAmt+p.DiscountAmt+p.WriteOffAmt,p.C_Currency_ID,p.DateTrx,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID)),0) " 
				+ "FROM C_Payment_v p "
				+ "WHERE p.C_BPartner_ID=bp.C_BPartner_ID"
				+ " AND p.IsAllocated='N' "
				+ " AND p.IsReceipt='Y' AND p.DocStatus IN('CO','CL')"
				+ " AND p.C_Charge_ID IS NULL)" 
			//											- All Receipt Allocations
			+ "+(SELECT COALESCE(SUM(currencyBaseSpecific(a.Amount+a.DiscountAmt+a.WriteOffAmt,h.C_Currency_ID,h.DateAcct,p.C_ConversionType_ID,a.AD_Client_ID,a.AD_Org_ID)),0) "
				+ "FROM C_AllocationLine a INNER JOIN C_Payment p ON (a.C_Payment_ID = p.C_Payment_ID) " 
				+ "INNER JOIN C_AllocationHdr h ON (a.C_AllocationHdr_ID = h.C_AllocationHdr_ID) "
				+ "WHERE a.C_BPartner_ID=bp.C_BPartner_ID "
				+ " AND p.IsAllocated='N' "
				+ " AND a.IsActive='Y' "
				+ " AND p.IsReceipt='Y' AND h.DocStatus IN('CO','CL')), "

			//	Open Balance			= All Invoices
			+ "COALESCE((SELECT SUM(currencyBaseSpecific(i.GrandTotal*MultiplierAP,i.C_Currency_ID,i.DateInvoiced,COALESCE(i.C_ConversionType_ID, 0),i.AD_Client_ID,i.AD_Org_ID)) "
				+ "FROM C_Invoice_v i "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID"
				+ " AND i.IsPaid='N' AND i.DocStatus IN('CO','CL')),0) "
			//					- All Invoice Allocations
			+ "-COALESCE((SELECT SUM(currencyBaseSpecific(a.Amount+a.DiscountAmt+a.WriteOffAmt,h.C_Currency_ID,h.DateAcct,COALESCE(i.C_ConversionType_ID, 0),a.AD_Client_ID,a.AD_Org_ID)) "
				+ "FROM C_AllocationLine a INNER JOIN C_Invoice i ON (a.C_Invoice_ID=i.C_Invoice_ID) "
				+ "INNER JOIN C_AllocationHdr h ON (a.C_AllocationHdr_ID = h.C_AllocationHdr_ID) "
				+ "WHERE a.C_BPartner_ID=bp.C_BPartner_ID AND i.IsPaid='N' AND a.IsActive='Y' AND h.DocStatus IN('CO','CL')),0) "
			//					- Unallocated Receipts	= (All Receipts
			+ "-(SELECT COALESCE(SUM(currencyBaseSpecific(p.PayAmt+p.DiscountAmt+p.WriteOffAmt,p.C_Currency_ID,p.DateTrx,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID)),0) " 
				+ "FROM C_Payment_v p "
				+ "WHERE p.C_BPartner_ID=bp.C_BPartner_ID"
				+ " AND p.IsAllocated='N' "
				+ " AND p.DocStatus IN('CO','CL')"
				+ " AND p.C_Charge_ID IS NULL)" 
			//											- All Allocations
			+ "+(SELECT COALESCE(SUM(currencyBaseSpecific(a.Amount+a.DiscountAmt+a.WriteOffAmt,p.C_Currency_ID,h.DateAcct,p.C_ConversionType_ID,a.AD_Client_ID,a.AD_Org_ID)),0) "
				+ "FROM C_AllocationLine a INNER JOIN C_Payment p ON (a.C_Payment_ID = p.C_Payment_ID) "
				+ "INNER JOIN C_AllocationHdr h ON (a.C_AllocationHdr_ID = h.C_AllocationHdr_ID) "
				+ "WHERE a.C_BPartner_ID=bp.C_BPartner_ID"
				+ " AND p.IsAllocated='N' "
				+ " AND a.IsActive='Y' AND h.DocStatus IN('CO','CL')) "
			//
			+ "FROM C_BPartner bp "
			+ "WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_BPartner_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				SO_CreditUsed = rs.getBigDecimal(1);
				TotalOpenBalance = rs.getBigDecimal(2);
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
		//
		m_TotalOpenBalanceSet = true;
		String info = null;
		if (SO_CreditUsed != null)
		{
			info = "SO_CreditUsed=" + SO_CreditUsed;
			super.setSO_CreditUsed (SO_CreditUsed);
		}
		
		if (TotalOpenBalance != null)
		{
			if (info != null)
				info += ", ";
			info += "TotalOpenBalance=" + TotalOpenBalance;
			super.setTotalOpenBalance(TotalOpenBalance);
		}
		log.fine(info);
		setSOCreditStatus();
	}	//	setTotalOpenBalance

	/**
	 * 	Set Actual Life Time Value from DB
	 */
	public void setActualLifeTimeValue()
	{
		BigDecimal ActualLifeTimeValue = null;
		String sql = "SELECT "
			+ "COALESCE ((SELECT SUM(currencyBaseSpecific(i.GrandTotal,i.C_Currency_ID,i.DateInvoiced,COALESCE(i.C_ConversionType_ID, 0),i.AD_Client_ID,i.AD_Org_ID)) "
				+ "FROM C_Invoice_v i "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID AND i.IsSOTrx='Y' AND i.DocStatus IN('CO','CL')),0) " 
			+ "FROM C_BPartner bp "
			+ "WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_BPartner_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				ActualLifeTimeValue = rs.getBigDecimal(1);
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
		if (ActualLifeTimeValue != null)
			super.setActualLifeTimeValue (ActualLifeTimeValue);
	}	//	setActualLifeTimeValue
	
	/**
	 * 	Get Total Open Balance
	 * 	@param calculate if null calculate it
	 *	@return Open Balance
	 */
	public BigDecimal getTotalOpenBalance (boolean calculate)
	{
		if (getTotalOpenBalance().signum() == 0 && calculate)
			setTotalOpenBalance();
		return super.getTotalOpenBalance ();
	}	//	getTotalOpenBalance
	
	
	/**
	 * 	Set Credit Status
	 */
	public void setSOCreditStatus ()
	{
		BigDecimal creditLimit = getSO_CreditLimit(); 
		//	Nothing to do
		if (SOCREDITSTATUS_NoCreditCheck.equals(getSOCreditStatus())
			|| SOCREDITSTATUS_CreditStop.equals(getSOCreditStatus())
			|| Env.ZERO.compareTo(creditLimit) == 0)
			return;

		//	Above Credit Limit
		if (creditLimit.compareTo(getTotalOpenBalance(!m_TotalOpenBalanceSet)) < 0)
			setSOCreditStatus(SOCREDITSTATUS_CreditHold);
		else
		{
			//	Above Watch Limit
			BigDecimal watchAmt = creditLimit.multiply(getCreditWatchRatio());
			if (watchAmt.compareTo(getTotalOpenBalance()) < 0)
				setSOCreditStatus(SOCREDITSTATUS_CreditWatch);
			else	//	is OK
				setSOCreditStatus(SOCREDITSTATUS_CreditOK);
		}
		log.fine("SOCreditStatus=" + getSOCreditStatus());
	}	//	setSOCreditStatus
	
	
	/**
	 * 	Get SO CreditStatus with additional amount
	 * 	@param additionalAmt additional amount in Accounting Currency
	 *	@return simulated credit status
	 */
	public String getSOCreditStatus (BigDecimal additionalAmt)
	{
		if (additionalAmt == null || additionalAmt.signum() == 0)
			return getSOCreditStatus();
		//
		BigDecimal creditLimit = getSO_CreditLimit(); 
		//	Nothing to do
		if (SOCREDITSTATUS_NoCreditCheck.equals(getSOCreditStatus())
			|| SOCREDITSTATUS_CreditStop.equals(getSOCreditStatus())
			|| Env.ZERO.compareTo(creditLimit) == 0)
			return getSOCreditStatus();

		//	Above (reduced) Credit Limit
		creditLimit = creditLimit.subtract(additionalAmt);
		if (creditLimit.compareTo(getTotalOpenBalance(!m_TotalOpenBalanceSet)) < 0)
			return SOCREDITSTATUS_CreditHold;
		
		//	Above Watch Limit
		BigDecimal watchAmt = creditLimit.multiply(getCreditWatchRatio());
		if (watchAmt.compareTo(getTotalOpenBalance()) < 0)
			return SOCREDITSTATUS_CreditWatch;
		
		//	is OK
		return SOCREDITSTATUS_CreditOK;
	}	//	getSOCreditStatus
	
	/**
	 * 	Get Credit Watch Ratio
	 *	@return BP Group ratio or 0.9
	 */
	public BigDecimal getCreditWatchRatio()
	{
		return getBPGroup().getCreditWatchRatio();
	}	//	getCreditWatchRatio
		
	/**
	 * 	Credit Status is Stop or Hold.
	 *	@return true if Stop/Hold
	 */
	public boolean isCreditStopHold()
	{
		String status = getSOCreditStatus();
		return SOCREDITSTATUS_CreditStop.equals(status)
			|| SOCREDITSTATUS_CreditHold.equals(status);
	}	//	isCreditStopHold
	
	/**
	 * 	Set Total Open Balance
	 *	@param TotalOpenBalance
	 */
	@Override
	public void setTotalOpenBalance (BigDecimal TotalOpenBalance)
	{
		m_TotalOpenBalanceSet = false;
		super.setTotalOpenBalance (TotalOpenBalance);
	}	//	setTotalOpenBalance
	
	/**
	 * 	Get BP Group
	 *	@return group
	 */
	public MBPGroup getBPGroup()
	{
		if (m_group == null)
		{
			if (getC_BP_Group_ID() == 0)
				m_group = MBPGroup.getDefault(getCtx());
			else
				m_group = MBPGroup.get(getCtx(), getC_BP_Group_ID());
		}
		return m_group;
	}	//	getBPGroup

	/**
	 * 	Get BP Group
	 *	@param group group
	 */
	public void setBPGroup(MBPGroup group)
	{
		m_group = group;
		if (m_group == null)
			return;
		setC_BP_Group_ID(m_group.getC_BP_Group_ID());
		if (m_group.getC_Dunning_ID() != 0)
			setC_Dunning_ID(m_group.getC_Dunning_ID());
		if (m_group.getM_PriceList_ID() != 0)
			setM_PriceList_ID(m_group.getM_PriceList_ID());
		if (m_group.getPO_PriceList_ID() != 0)
			setPO_PriceList_ID(m_group.getPO_PriceList_ID());
		if (m_group.getM_DiscountSchema_ID() != 0)
			setM_DiscountSchema_ID(m_group.getM_DiscountSchema_ID());
		if (m_group.getPO_DiscountSchema_ID() != 0)
			setPO_DiscountSchema_ID(m_group.getPO_DiscountSchema_ID());
		if (m_group.getPO_ReturnPolicy_ID() != 0)
			setPO_ReturnPolicy_ID(m_group.getPO_ReturnPolicy_ID());
	}	//	setBPGroup

	/**
	 * 	Get PriceList
	 *	@return price List
	 */
	@Override
	public int getM_PriceList_ID ()
	{
		int ii = super.getM_PriceList_ID();
		if (ii == 0)
			ii = getBPGroup().getM_PriceList_ID();
		return ii;
	}	//	getM_PriceList_ID
	
	/**
	 * 	Get PO PriceList
	 *	@return price list
	 */
	@Override
	public int getPO_PriceList_ID ()
	{
		int ii = super.getPO_PriceList_ID();
		if (ii == 0)
			ii = getBPGroup().getPO_PriceList_ID();
		return ii;
	}	//
	
	/**
	 * 	Get DiscountSchema
	 *	@return Discount Schema
	 */
	@Override
	public int getM_DiscountSchema_ID ()
	{
		int ii = super.getM_DiscountSchema_ID ();
		if (ii == 0)
			ii = getBPGroup().getM_DiscountSchema_ID();
		return ii;
	}	//	getM_DiscountSchema_ID
	
	/**
	 * 	Get PO DiscountSchema
	 *	@return po discount
	 */
	@Override
	public int getPO_DiscountSchema_ID ()
	{
		int ii = super.getPO_DiscountSchema_ID ();
		if (ii == 0)
			ii = getBPGroup().getPO_DiscountSchema_ID();
		return ii;
	}	//	getPO_DiscountSchema_ID
	
	/**
	 * 	Get ReturnPolicy
	 *	@return Return Policy
	 */
	@Override
	public int getM_ReturnPolicy_ID ()
	{
		int ii = super.getM_ReturnPolicy_ID();
		if (ii == 0)
			ii = getBPGroup().getM_ReturnPolicy_ID();
		if (ii == 0)
			ii = MReturnPolicy.getDefault(getCtx());
		return ii;
	}	//	getM_ReturnPolicy_ID
	
	/**
	 * 	Get Vendor ReturnPolicy
	 *	@return Return Policy
	 */
	@Override
	public int getPO_ReturnPolicy_ID ()
	{
		int ii = super.getPO_ReturnPolicy_ID();
		if (ii == 0)
			ii = getBPGroup().getPO_ReturnPolicy_ID();
		if (ii == 0)
			ii = MReturnPolicy.getDefault(getCtx());
		return ii;
	}	//	getPO_ReturnPolicy_ID
	
	/**
	 * 	Get Consolidation Reference
	 *	@return Consolidation Reference
	 */
	@Override
	public int getC_ConsolidationReference_ID ()
	{
		int ii = super.getC_ConsolidationReference_ID();
		if (ii == 0)
			ii = getBPGroup().getC_ConsolidationReference_ID();
		return ii;
	}	//	getPO_ReturnPolicy_ID
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord || is_ValueChanged("C_BP_Group_ID"))
		{
			MBPGroup grp = getBPGroup();
			if (grp == null)
			{
				log.saveWarning("Error", Msg.parseTranslation(getCtx(), "@NotFound@:  @C_BP_Group_ID@"));
				return false;
			}
			setBPGroup(grp);	//	setDefaults
		}
		return true;
	}	//	beforeSave
	
	/**************************************************************************
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(!getCtx().isBatchMode())
		{
			if (success && newRecord)
			{
				//	Accounting
				success = insert_Accounting("C_BP_Customer_Acct", 
					"C_BP_Group_Acct", "p.C_BP_Group_ID=" + getC_BP_Group_ID());
				if (success)
					success = insert_Accounting("C_BP_Vendor_Acct", 
						"C_BP_Group_Acct", "p.C_BP_Group_ID=" + getC_BP_Group_ID());
				if (success)
					success = insert_Accounting("C_BP_Employee_Acct", 
						"C_AcctSchema_Default", null);
			}
			
			if (success && !newRecord  && is_ValueChanged("C_BP_Group_ID"))
			{
				//	Accounting
				success = update_Accounting("C_BP_Customer_Acct", 
					"C_BP_Group_Acct", "p.C_BP_Group_ID=" + getC_BP_Group_ID());
				if (success)
					success = update_Accounting("C_BP_Vendor_Acct", 
						"C_BP_Group_Acct", "p.C_BP_Group_ID=" + getC_BP_Group_ID());
				if (success)
					success = update_Accounting("C_BP_Employee_Acct", 
						"C_AcctSchema_Default", null);
			}
		}
		//	Value/Name change
		if (success && !newRecord 
			&& (is_ValueChanged("Value") || is_ValueChanged("Name")))
			MAccount.updateValueDescription(getCtx(), "C_BPartner_ID= ? ", 
					get_Trx(),new Object[]{getC_BPartner_ID()});

		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	@Override
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_BP_Customer_Acct") 
			&& delete_Accounting("C_BP_Vendor_Acct")
			&& delete_Accounting("C_BP_Employee_Acct");
	}	//	beforeDelete	
	
	/**
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
    {
	    Compiere.startup(true);
	    Ctx ctx = Env.getCtx();
		ctx.setAD_Client_ID(11);	//	GardenWorld
		ctx.setAD_Role_ID(102);		//	GardenAdmin
		ctx.setAD_User_ID(101);		//	GardenAdmin
	    String bpValue = "%";
	    String bpName = "%";
	    String taxID = null;
		String postal = "%";
		String city = "%"; 
		String uValue = "%";
		String uName = "%";
		String uPhone = "%";
		String email = "%";
		Timestamp birthday = null;	//	new Timestamp(System.currentTimeMillis());
		boolean andLogic = false;
		int maxRows = 3;
	    ArrayList<MBPartner> bps = findAll(ctx, bpValue, bpName, taxID, postal, city, 
	    	uValue, uName, uPhone, email, birthday, andLogic, maxRows);
	    //
	    MBPartner bp = bps.get(0);
	    
	    org.w3c.dom.Document doc = bp.get_xmlDocument(false, false);
	    System.out.println(doc);

	    StringBuffer bpXml = bp.get_xmlString(null, false);
	    System.out.println(bpXml.toString());
	 
	//	VO vo = bp.getVO();
	    
    }
	
}	//	MBPartner
