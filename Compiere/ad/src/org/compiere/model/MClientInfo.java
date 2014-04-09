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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Client Info Model
 *
 *  @author Jorg Janke
 *  @version $Id: MClientInfo.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MClientInfo extends X_AD_ClientInfo
{
    /** Logger for class MClientInfo */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MClientInfo.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Client Info
	 * 	@param ctx context
	 * 	@param AD_Client_ID id
	 * 	@return Client Info
	 */
	public static MClientInfo get (Ctx ctx, int AD_Client_ID)
	{
		return get(ctx, AD_Client_ID, null);
	}	//	get
	
	/**
	 * 	Get Client Info
	 * 	@param ctx context
	 * 	@param AD_Client_ID id
	 * 	@param trx optional p_trx
	 * 	@return Client Info
	 */
	public static MClientInfo get (Ctx ctx, int AD_Client_ID, Trx trx)
	{
		Integer key = Integer.valueOf (AD_Client_ID);
		MClientInfo info = s_cache.get(ctx, key);
		if (info != null)
			return info;
		//
		String sql = "SELECT * FROM AD_ClientInfo WHERE AD_Client_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				info = new MClientInfo (ctx, rs, null);
				if (trx == null)
					s_cache.put (key, info);
			}
		}
		catch (SQLException ex) {
			s_log.log(Level.SEVERE, sql, ex);
		}
        finally {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }

		return info;
	}	//	get
	
	/**
	 * 	Get optionally cached client
	 *	@param ctx context
	 *	@return client
	 */
	public static MClientInfo get (Ctx ctx)
	{
		return get (ctx, ctx.getAD_Client_ID(), null);
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer,MClientInfo> s_cache = new CCache<Integer,MClientInfo>("AD_ClientInfo", 2);
	/**	Logger						*/
	private static final CLogger		s_log = CLogger.getCLogger (MClientInfo.class);

	
	/**************************************************************************
	 *	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Client_ID ignored
	 *	@param trx transaction
	 */
	public MClientInfo (Ctx ctx, int AD_Client_ID, Trx trx)
	{
		super (ctx, AD_Client_ID, trx);
	}	//	MClientInfo
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MClientInfo (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MClientInfo

	/**
	 * 	Parent Constructor
	 *	@param client client
	 *	@param AD_Tree_Org_ID org tree
	 *	@param AD_Tree_BPartner_ID bp tree
	 *	@param AD_Tree_Project_ID project tree
	 *	@param AD_Tree_SalesRegion_ID sr tree
	 *	@param AD_Tree_Product_ID product tree
	 *	@param AD_Tree_Campaign_ID campaign tree
	 *	@param AD_Tree_Activity_ID activity tree
	 *	@param trx transaction
	 */
	public MClientInfo (MClient client, int AD_Tree_Org_ID, int AD_Tree_BPartner_ID,
		int AD_Tree_Project_ID, int AD_Tree_SalesRegion_ID, int AD_Tree_Product_ID,
		int AD_Tree_Campaign_ID, int AD_Tree_Activity_ID, Trx trx)
	{
		super (client.getCtx(), 0, trx);
		setAD_Client_ID(client.getAD_Client_ID());	//	to make sure
		setAD_Org_ID(0);
		setIsDiscountLineAmt (false);
		
		int AD_Tree_ID = QueryUtil.getSQLValue(null,
				"SELECT AD_Tree_ID " +
				"FROM AD_Tree "+
				"WHERE AD_Tree.TreeType='MM' AND AD_Tree.IsAllNodes='Y'"+ 
				"AND IsDefault='Y'");
	
		if (AD_Tree_ID <= 0)
			AD_Tree_ID = 121;	//	Menu2
		//
		setAD_Tree_Menu_ID(AD_Tree_ID);		
		//
		setAD_Tree_Org_ID(AD_Tree_Org_ID);
		setAD_Tree_BPartner_ID(AD_Tree_BPartner_ID); 
		setAD_Tree_Project_ID(AD_Tree_Project_ID);		
		setAD_Tree_SalesRegion_ID(AD_Tree_SalesRegion_ID);  
		setAD_Tree_Product_ID(AD_Tree_Product_ID);
		setAD_Tree_Campaign_ID(AD_Tree_Campaign_ID);
		setAD_Tree_Activity_ID(AD_Tree_Activity_ID);
		//
		setMatchRequirementI (MATCHREQUIREMENTI_None);
		setMatchRequirementR (MATCHREQUIREMENTR_None);
		m_createNew = true;
	}	//	MClientInfo


	/**	Account Schema				*/
	private MAcctSchema 		m_acctSchema = null;
	/** New Record					*/
	private boolean				m_createNew = false;

	/**
	 * 	Get primary Acct Schema
	 *	@return acct schema
	 */
	public MAcctSchema getMAcctSchema1()
	{
		if (m_acctSchema == null && getC_AcctSchema1_ID() != 0)
			m_acctSchema = new MAcctSchema (getCtx(), getC_AcctSchema1_ID(), null);
		return m_acctSchema;
	}	//	getMAcctSchema1

	/**
	 *	Get Default Accounting Currency
	 *	@return currency or 0
	 */
	public int getC_Currency_ID()
	{
		if (m_acctSchema == null)
			getMAcctSchema1();
		if (m_acctSchema != null)
			return m_acctSchema.getC_Currency_ID();
		return 0;
	}	//	getC_Currency_ID

	/**
	 * 	Get Match Requirement I
	 *	@return invoice Match Req
	 */
	@Override
	public String getMatchRequirementI()
	{
		String s = super.getMatchRequirementI();
		if (s == null)
		{
			setMatchRequirementI (MATCHREQUIREMENTI_None);
			return MATCHREQUIREMENTI_None;
		}
		return s;
	}	//	getMatchRequirementI
	
	/**
	 * 	Get Match Requirement R
	 *	@return receipt matcg Req
	 */
	@Override
	public String getMatchRequirementR()
	{
		String s = super.getMatchRequirementR();
		if (s == null)
		{
			setMatchRequirementR (MATCHREQUIREMENTR_None);
			return MATCHREQUIREMENTR_None;
		}
		return s;
	}	//	getMatchRequirementR
	
	
	/**
	 * 	Overwrite Save
	 * 	@overwrite
	 *	@return true if saved
	 */
	@Override
	public boolean save ()
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		getMatchRequirementI();
		getMatchRequirementR();
		if (m_createNew)
			return super.save ();
		return saveUpdate();
	}	//	save
	@Override
	protected boolean afterDelete(boolean success) {
		return super.afterDelete(success);
	}

	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		MCurrency.clearCurrencyCache();
		return super.afterSave(newRecord, success);
	}

}	//	MClientInfo
