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

import javax.servlet.http.*;

import org.compiere.util.*;

/**
 *	Asset Registration Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRegistration.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MRegistration extends X_A_Registration
{
    /** Logger for class MRegistration */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRegistration.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param A_Registration_ID id
	 */
	public MRegistration(Ctx ctx, int A_Registration_ID, Trx trx)
	{
		super(ctx, A_Registration_ID, trx);
		if (A_Registration_ID == 0)
			setIsRegistered (true);
	}	//	MRegistration

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param Name name
	 *	@param IsAllowPublish allow publication
	 *	@param IsInProduction production
	 *	@param AssetServiceDate start date
	 *	@param trx p_trx
	 */
	public MRegistration (Ctx ctx, String Name, boolean IsAllowPublish,
		boolean IsInProduction, Timestamp AssetServiceDate, Trx trx)
	{
		this (ctx, 0, trx);
		setName (Name);
		setIsAllowPublish (IsAllowPublish);
		setIsInProduction (IsInProduction);
		setAssetServiceDate (AssetServiceDate);
	}	//	MRegistration
	
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRegistration(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRegistration

	/**	All Attributes						*/
	private MRegistrationAttribute[] m_allAttributes = null;

	/**
	 * 	Get All Attributes
	 *	@return Registration Attributes
	 */
	public MRegistrationAttribute[] getAttributes()
	{
		if (m_allAttributes == null)
			m_allAttributes = MRegistrationAttribute.getAll(getCtx());
		return m_allAttributes;
	}	//	getAttributes

	/**
	 * 	Get All active Self Service Attribute Values
	 *	@return Registration Attribute Values
	 */
	public MRegistrationValue[] getValues()
	{
		return getValues (true);
	}	//	getValues
	
	/**
	 * 	Get All Attribute Values
	 * 	@param onlySelfService only Active Self Service
	 *	@return sorted Registration Attribute Values
	 */
	public MRegistrationValue[] getValues (boolean onlySelfService)
	{
		createMissingValues();
		//
		String sql = "SELECT * FROM A_RegistrationValue rv "
			+ "WHERE A_Registration_ID=?";
		if (onlySelfService)
			sql += " AND EXISTS (SELECT * FROM A_RegistrationAttribute ra WHERE rv.A_RegistrationAttribute_ID=ra.A_RegistrationAttribute_ID"
				+ " AND ra.IsActive='Y' AND ra.IsSelfService='Y')";
				
		ArrayList<MRegistrationValue> list = new ArrayList<MRegistrationValue>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getA_Registration_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MRegistrationValue(getCtx(), rs, get_Trx()));
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
		//	Convert and Sort
		MRegistrationValue[] retValue = new MRegistrationValue[list.size()];
		list.toArray(retValue);
		Arrays.sort(retValue);
		return retValue;
	}	//	getValues
	
	/**
	 * 	Create Missing Attribute Values
	 */
	private void createMissingValues()
	{
		String sql = "SELECT ra.A_RegistrationAttribute_ID "
			+ "FROM A_RegistrationAttribute ra"
			+ " LEFT OUTER JOIN A_RegistrationProduct rp ON (rp.A_RegistrationAttribute_ID=ra.A_RegistrationAttribute_ID)"
			+ " LEFT OUTER JOIN A_Registration r ON (r.M_Product_ID=rp.M_Product_ID) "
			+ "WHERE r.A_Registration_ID=?"
			//	Not in Registration
			+ " AND NOT EXISTS (SELECT A_RegistrationAttribute_ID FROM A_RegistrationValue v "
				+ "WHERE ra.A_RegistrationAttribute_ID=v.A_RegistrationAttribute_ID AND r.A_Registration_ID=v.A_Registration_ID)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getA_Registration_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MRegistrationValue v = new MRegistrationValue (this, rs.getInt(1), "?");
				v.save();
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, null, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
	}	//	createMissingValues
	
	/**
	 * 	Load Attributes from Request
	 *	@param request request
	 *	@return number of attributes read
	 */
	public int loadAttributeValues (HttpServletRequest request)
	{
		//	save if not saved
		if (get_ID() == 0)
			save();
		int updatecount = 0;
		int insertcount = 0;
		
		//	read values for all attributes
		MRegistrationAttribute[] attributes = getAttributes();
		MRegistrationValue[] values = getValues(false);

		for (MRegistrationAttribute attribute : attributes) {
			String value = WebUtil.getParameter (request, attribute.getName());
			if (value == null)
				continue;
			boolean insert = true;
			for (MRegistrationValue regValue : values) {
				String attributeName = regValue.getRegistrationAttribute();
				if (attributeName==attribute.getName()){
					insert = false;
					String prev = regValue.getName();
					if (prev == null || (prev!=null && !prev.equals(value)))
						regValue.setDescription("Previous=" + prev);
					regValue.setName(value);
					if (regValue.save())
						updatecount++;
					break;
				}
			}
			if (insert){
				MRegistrationValue regValue = new MRegistrationValue (this, 
						attribute.getA_RegistrationAttribute_ID(), value);
					if (regValue.save())
						insertcount++;
				
			}
		}
		log.fine("loaded " + insertcount + " (of " + attributes.length + ") new attributes");
		log.fine("updated "+ updatecount + " (of " + values.length + ") existing attributes");
		return updatecount+insertcount;
	}	//	loadAttributeValues

}	//	MRegistration
