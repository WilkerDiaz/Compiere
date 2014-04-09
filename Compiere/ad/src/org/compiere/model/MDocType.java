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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Document Type Model
 *
 *  @author Jorg Janke
 *  @version $Id: MDocType.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MDocType extends X_C_DocType
{
    /** Logger for class MDocType */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDocType.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Client Document Type with DocBaseType
	 *	@param ctx context
	 *	@param DocBaseType base document type
	 *	@return array of doc types
	 */
	static public MDocType[] getOfDocBaseType (Ctx ctx, String DocBaseType)
	{
		ArrayList<MDocType> list = new ArrayList<MDocType>();
		String sql = "SELECT * FROM C_DocType "
			+ "WHERE AD_Client_ID=? AND DocBaseType=? AND IsActive='Y'"
			+ "ORDER BY C_DocType_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, ctx.getAD_Client_ID());
			pstmt.setString(2, DocBaseType);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDocType(ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MDocType[] retValue = new MDocType[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfDocBaseType

	/**
	 * 	Get Client Document Types
	 *	@param ctx context
	 *	@return array of doc types
	 */
	static public MDocType[] getOfClient (Ctx ctx)
	{
		ArrayList<MDocType> list = new ArrayList<MDocType>();
		String sql = "SELECT * FROM C_DocType WHERE AD_Client_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, ctx.getAD_Client_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDocType(ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MDocType[] retValue = new MDocType[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfClient

	/**
	 * 	Get Document Type (cached)
	 *	@param ctx context
	 *	@param C_DocType_ID id
	 *	@return document type
	 */
	static public MDocType get (Ctx ctx, int C_DocType_ID)
	{
		Integer key = Integer.valueOf(C_DocType_ID);
		MDocType retValue = s_cache.get(ctx, key);
		if (retValue == null)
		{
			retValue = new MDocType (ctx, C_DocType_ID, null);
			s_cache.put(key, retValue);
		}
		return retValue;
	} 	//	get

	/**	Cache					*/
	static private final CCache<Integer,MDocType>	s_cache = new CCache<Integer,MDocType>("C_DocType", 20);
	/**	Static Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MDocType.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DocType_ID id
	 *	@param trx transaction
	 */
	public MDocType(Ctx ctx, int C_DocType_ID, Trx trx)
	{
		super(ctx, C_DocType_ID, trx);
		if (C_DocType_ID == 0)
		{
		//	setName (null);
		//	setPrintName (null);
		//	setDocBaseType (null);
		//	setGL_Category_ID (0);
			setDocumentCopies (0);
			setHasCharges (false);
			setIsDefault (false);
			setIsDocNoControlled (false);
			setIsSOTrx (false);
			setIsPickQAConfirm(false);
			setIsShipConfirm(false);
			setIsSplitWhenDifference(false);
			setIsReturnTrx(false);
			//
			setIsCreateCounter(true);
			setIsDefaultCounterDoc(false);
			setIsIndexed(true);
			setIsInTransit(false);
		}
	}	//	MDocType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDocType(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDocType

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param DocBaseType document base type
	 *	@param Name name
	 *	@param trx transaction
	 */
	public MDocType (Ctx ctx, String DocBaseType, String Name, Trx trx)
	{
		this (ctx, 0, trx);
		setAD_Org_ID(0);
		setDocBaseType (DocBaseType);
		setName (Name);
		setPrintName (Name);
		setGL_Category_ID ();
	}	//	MDocType

	/**
	 * 	Set Default GL Category
	 */
	public void setGL_Category_ID()
	{
		String sql = "SELECT * FROM GL_Category WHERE AD_Client_ID=? AND IsDefault='Y'";
		int GL_Category_ID = QueryUtil.getSQLValue(get_Trx(), sql, getAD_Client_ID());
		if (GL_Category_ID == 0)
		{
			sql = "SELECT * FROM GL_Category WHERE AD_Client_ID=?";
			GL_Category_ID = QueryUtil.getSQLValue(get_Trx(), sql, getAD_Client_ID());
		}
		setGL_Category_ID(GL_Category_ID);
	}	//	setGL_Category_ID


	/**
	 * 	Set SOTrx based on document base type
	 */
	public void setIsSOTrx()
	{
		boolean isSOTrx = MDocBaseType.DOCBASETYPE_SalesOrder.equals(getDocBaseType())
			|| MDocBaseType.DOCBASETYPE_MaterialDelivery.equals(getDocBaseType())
			|| getDocBaseType().startsWith("AR");
		super.setIsSOTrx (isSOTrx);
	}	//	setIsSOTrx

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MDocType[");
		sb.append(get_ID()).append("-").append(getName())
			.append(",DocNoSequence_ID=").append(getDocNoSequence_ID())
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Is this a Quotation (Binding)
	 *	@return true if Quotation
	 */
	public boolean isQuotation()
	{
		return DOCSUBTYPESO_Quotation.equals(getDocSubTypeSO())
			&& MDocBaseType.DOCBASETYPE_SalesOrder.equals(getDocBaseType());
	}	//	isQuotation

	/**
	 * 	Is this a Proposal (Not binding)
	 *	@return true if proposal
	 */
	public boolean isProposal()
	{
		return DOCSUBTYPESO_Proposal.equals(getDocSubTypeSO())
			&& MDocBaseType.DOCBASETYPE_SalesOrder.equals(getDocBaseType());
	}	//	isProposal

	/**
	 * 	Is this a Proposal or Quotation
	 *	@return true if proposal or quotation
	 */
	public boolean isOffer()
	{
		return (DOCSUBTYPESO_Proposal.equals(getDocSubTypeSO())
				|| DOCSUBTYPESO_Quotation.equals(getDocSubTypeSO()))
			&& MDocBaseType.DOCBASETYPE_SalesOrder.equals(getDocBaseType());
	}	//	isOffer


	/**
	 * 	Get Print Name
	 * 	@param AD_Language language
	 *	@return print Name if available translated
	 */
	public String getPrintName (String AD_Language)
	{
		if (AD_Language == null || AD_Language.length() == 0)
			return super.getPrintName();
		String retValue = get_Translation ("PrintName", AD_Language);
		if (retValue != null)
			return retValue;
		return super.getPrintName();
	}	//	getPrintName


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);

		//	Sync DocBaseType && Return Trx
	//	if (newRecord || is_ValueChanged("DocBaseType"))
		setIsSOTrx();

		MDocType invDocType = null;
		MDocType shipDocType = null;
		try
		{
			if(getC_DocTypeInvoice_ID() != 0)
				invDocType = new MDocType(getCtx(),this.getC_DocTypeInvoice_ID(), get_Trx());
			if(getC_DocTypeShipment_ID() != 0)
				shipDocType = new MDocType(getCtx(),this.getC_DocTypeShipment_ID(), get_Trx());
		}
		catch(Exception ex)
		{
			invDocType = null;
			shipDocType = null;
		}

		if (isSOTrx() &&
				invDocType != null && !invDocType.isSOTrx())
		{
			String invmsg = Msg.getMsg(Env.getAD_Language(Env.getCtx()),"Invoice");
			String msg = Msg.getMsg(Env.getAD_Language(Env.getCtx()),
					"NotSalesTransactionDocument",new Object[]{invDocType.getName(),invmsg});
			log.saveError("Error", msg);
			return false;
		}
		else if (isSOTrx() &&
				shipDocType != null && !shipDocType.isSOTrx())
		{
			String shipmsg = Msg.getMsg(Env.getAD_Language(Env.getCtx()),"Shipment");
			String msg = Msg.getMsg(Env.getAD_Language(Env.getCtx()),
					"NotSalesTransactionDocument",new Object[]{shipDocType.getName(),shipmsg});
			log.saveError("Error", msg);
			return false;
		}

		return true;
	}	//	beforeSave

}	//	MDocType
