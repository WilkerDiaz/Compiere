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

import org.compiere.api.UICallout;
import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;


/**
 *  Process Parameter Model
 *
 *  @author Jorg Janke
 *  @version $Id: MProcessPara.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MProcessPara extends X_AD_Process_Para
{
    /** Logger for class MProcessPara */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProcessPara.class);
	/** */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MProcessPara from Cache
	 *	@param ctx context
	 *	@param AD_Process_Para_ID id
	 *	@return MProcessPara
	 */
	public static MProcessPara get (Ctx ctx, int AD_Process_Para_ID)
	{
		Integer key = Integer.valueOf (AD_Process_Para_ID);
		MProcessPara retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MProcessPara (ctx, AD_Process_Para_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer, MProcessPara> s_cache 
		= new CCache<Integer, MProcessPara> ("AD_Process_Para", 20);
	
	
	/**************************************************************************
	 * 	Constructor
	 *	@param ctx context
	 *	@param AD_Process_Para_ID id
	 *	@param trx transaction
	 */
	public MProcessPara (Ctx ctx, int AD_Process_Para_ID, Trx trx)
	{
		super (ctx, AD_Process_Para_ID, trx);
		if (AD_Process_Para_ID == 0)
		{
		//	setAD_Process_ID (0);	Parent
		//	setName (null);
		//	setColumnName (null);
			
			setFieldLength (0);
			setSeqNo (0);
		//	setAD_Reference_ID (0);
			setIsCentrallyMaintained (true);
			setIsRange (false);
			setIsMandatory (false);
			setEntityType (ENTITYTYPE_UserMaintained);
		}
	}	//	MProcessPara

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MProcessPara (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProcessPara

	/** Virtual Window No - 999	*/
	public static final int		WINDOW_NO = 999;
	/** Virtual Tab No - 0		*/
	public static final int		TAB_NO = 0;
	
	/**	The Lookup				*/
	private Lookup		m_lookup = null;
	

	/**
	 *  Is this field a Lookup?.
	 *  @return true if lookup field
	 */
	public boolean isLookup()
	{
		boolean retValue = false;
		int displayType = getAD_Reference_ID(); 
		if (FieldType.isLookup(displayType))
			retValue = true;
		else if (displayType == DisplayTypeConstants.Location
			|| displayType == DisplayTypeConstants.Locator
			|| displayType == DisplayTypeConstants.Account
			|| displayType == DisplayTypeConstants.PAttribute)
			retValue = true;
		return retValue;
	}   //  isLookup

	/**
	 *  Set Lookup for columns with lookup
	 */
	public void loadLookup()
	{
		if (!isLookup())
			return;
		log.fine("(" + getColumnName() + ")");
		int displayType = getAD_Reference_ID();
		if (FieldType.isLookup(displayType))
		{
			MLookup ml = new MLookup (getCtx(), 0, getAD_Reference_ID());
			MLookupInfo  lookupInfo = MLookupFactory.getLookupInfo(ml, 
				getAD_Process_Para_ID(),  
				Env.getLanguage(getCtx()), getColumnName(), 
				getAD_Reference_Value_ID(), false, "");
			if (lookupInfo == null)
			{
				log.log(Level.SEVERE, "(" + getColumnName() + ") - No LookupInfo");
				return;
			}
			//	Prevent loading of CreatedBy/UpdatedBy
			if (displayType == DisplayTypeConstants.Table
				&& (getColumnName().equals("CreatedBy") || getColumnName().equals("UpdatedBy")) )
			{
				lookupInfo.IsCreadedUpdatedBy = true;
				ml.setDisplayType(DisplayTypeConstants.Search);
			}
			//
			m_lookup = ml.initialize(lookupInfo);
		}
		else if (displayType == DisplayTypeConstants.Location)   //  not cached
		{
			MLocationLookup ml = new MLocationLookup (getCtx(), WINDOW_NO);
			m_lookup = ml;
		}
		else if (displayType == DisplayTypeConstants.Locator)
		{
			MLocatorLookup ml = new MLocatorLookup (getCtx(), WINDOW_NO);
			m_lookup = ml;
		}
		else if (displayType == DisplayTypeConstants.Account)    //  not cached
		{
			MAccountLookup ma = new MAccountLookup (getCtx(), WINDOW_NO);
			m_lookup = ma;
		}
		else if (displayType == DisplayTypeConstants.PAttribute)    //  not cached
		{
			MPAttributeLookup pa = new MPAttributeLookup (getCtx(), WINDOW_NO);
			m_lookup = pa;
		}
		//
		if (m_lookup != null)
			m_lookup.loadComplete();
	}   //  loadLookup

	/**
	 * 	Get Lookup for Parameter
	 *	@return lookup or null
	 */
	public Lookup getLookup()
	{
		if (m_lookup == null && isLookup())
			loadLookup();
		return m_lookup;
	}	//	getLookup
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MProcessPara[")
			.append (get_ID()).append("-").append(getSeqNo())
			.append(":").append(getColumnName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	@UICallout public void setAD_BView_Field_ID (String oldAD_BView_Field_ID, 
			String newAD_BView_Field_ID, int windowNo) throws Exception
	{
		if (newAD_BView_Field_ID == null || newAD_BView_Field_ID.length() == 0)
			return;
		int AD_BView_Field_ID = Integer.parseInt(newAD_BView_Field_ID);
		if (AD_BView_Field_ID == 0)
			return;			

		String sql = "SELECT bvf.Name,bvsm.AD_BView_Source_Field_ID,c.ColumnName,c.AD_Reference_ID "
					+"FROM AD_BView_Source_Mapping  bvsm, AD_BView_Source bvs, AD_BView_Field bvf, AD_Column c "
					+"WHERE  bvs.AD_BView_Source_Id = bvsm.AD_BView_Source_ID and bvs.IsDefault ='Y' AND bvf.AD_BView_Field_id = bvsm.AD_BView_Field_ID "
					+"and bvf.AD_BView_ID = bvs.AD_BView_ID and c.AD_Column_ID = bvsm.AD_BView_Source_Field_ID "
					+"and bvf.AD_BView_Field_ID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_BView_Field_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				super.setAD_BView_Field_ID(AD_BView_Field_ID);
				setColumnName(rs.getString(3));
				setAD_Reference_ID(rs.getInt(4));				
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	setAD_BView_Field_ID
	
}	//	MProcessPara
