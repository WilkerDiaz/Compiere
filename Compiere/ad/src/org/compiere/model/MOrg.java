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

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Organization Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MOrg.java 8898 2010-06-06 16:14:49Z ragrawal $
 */
public class MOrg extends X_AD_Org
{
    /** Logger for class MOrg */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MOrg.class);
	/** */
    private static final long serialVersionUID = 3338197460909599374L;


	/**
	 * 	Get Organizations Of Client
	 *	@param po persistent object
	 *	@return array of orgs
	 */
	public static MOrg[] getOfClient (PO po)
	{
		return getOfClient(po.getCtx(), po.getAD_Client_ID());
	}	//	getOfClient
	
	/**
	 * 	Get Organizations Of Client
	 *	@param ctx contest
	 *	@param AD_Client_ID client
	 *	@return array of orgs
	 */
	public static MOrg[] getOfClient (Ctx ctx, int AD_Client_ID)
	{
		ArrayList<MOrg> list = new ArrayList<MOrg>();
		String sql = "SELECT * FROM AD_Org WHERE AD_Client_ID=? ORDER BY Value";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MOrg (ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		MOrg[] retValue = new MOrg[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfClient
	
	/**
	 * 	Get Org from Cache
	 *	@param ctx context
	 *	@param AD_Org_ID id
	 *	@return MOrg
	 */
	public static MOrg get (Ctx ctx, int AD_Org_ID)
	{
		Integer key = Integer.valueOf (AD_Org_ID);
		MOrg retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MOrg (ctx, AD_Org_ID, null);
		if (AD_Org_ID == 0)
			retValue.load((Trx)null);
		if (retValue.get_ID () != AD_Org_ID)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Logger	*/
	private static final CLogger 			s_log = CLogger.getCLogger (MOrg.class);
	/**	Cache						*/
	private static final CCache<Integer,MOrg>	s_cache	= new CCache<Integer,MOrg>("AD_Org", 20);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Org_ID id
	 *	@param trx transaction
	 */
	public MOrg (Ctx ctx, int AD_Org_ID, Trx trx)
	{
		super(ctx, AD_Org_ID, trx);
		if (AD_Org_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setIsSummary (false);
		}
	}	//	MOrg

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MOrg (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MOrg

	/**
	 * 	Parent Constructor
	 *	@param client client
	 *	@param name name
	 */
	public MOrg (MClient client, String name)
	{
		this (client.getCtx(), 0, client.get_Trx());
		setAD_Client_ID (client.getAD_Client_ID());
		setValue (name);
		setName (name);
	}	//	MOrg

	/**	Org Info						*/
	private MOrgInfo	m_info = null;
	/**	Linked Business Partner			*/
	private Integer 	m_linkedBPartner = null;

	/**
	 *	Get Org Info
	 *	@return Org Info
	 */
	public MOrgInfo getInfo()
	{
		if (m_info == null)
			m_info = MOrgInfo.get (getCtx(), getAD_Org_ID(), get_Trx());
		return m_info;
	}	//	getMOrgInfo


	
	/**
	 * 	After Save
	 *	@param newRecord new Record
	 *	@param success save success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (newRecord)
		{
			//	Info
			m_info = new MOrgInfo (this);
			m_info.save();
			//	Access
			MRoleOrgAccess.createForOrg (this);
			MRole.getDefault(getCtx(), true);	//	reload
		}
		//	Value/Name change
		if (!newRecord && (is_ValueChanged("Value") || is_ValueChanged("Name")))
		{
			MAccount.updateValueDescription(getCtx(), "AD_Org_ID= ? " , 
					get_Trx(),new Object[]{getAD_Org_ID()});
			if ("Y".equals(getCtx().getContext("$Element_OT"))) 
				MAccount.updateValueDescription(getCtx(), "AD_OrgTrx_ID= ? ",
						get_Trx(),new Object[]{getAD_Org_ID()});
		}
		
		return true;
	}	//	afterSave
	

	/**
	 * 	Get Linked BPartner
	 *	@return C_BPartner_ID
	 */
	public int getLinkedC_BPartner_ID()
	{
		return getLinkedC_BPartner_ID(null);
	}	//	getLinkedC_BPartner_ID
	
	/**
	 * 	Get Linked BP
	 * 	@param trx
	 * 	@return C_BPartner_ID or 0
	 */
	public int getLinkedC_BPartner_ID(Trx trx)
	{
		if (m_linkedBPartner == null)
		{
			//jz int C_BPartner_ID = DB.getSQLValue(null,
			int C_BPartner_ID = QueryUtil.getSQLValue(trx,
				"SELECT C_BPartner_ID FROM C_BPartner WHERE AD_OrgBP_ID=?",
				getAD_Org_ID());
			if (C_BPartner_ID < 0)	//	not found = -1
				C_BPartner_ID = 0;
			m_linkedBPartner = Integer.valueOf (C_BPartner_ID);
		}
		return m_linkedBPartner.intValue();
	}	//	getLinkedC_BPartner_ID
	
	/**
	 * 	Get Default Org Warehouse
	 *	@return warehouse
	 */
	public int getM_Warehouse_ID()
	{
		return getInfo().getM_Warehouse_ID();
	}	//	getM_Warehouse_ID
	
}	//	MOrg
