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

import org.compiere.util.*;

/**
 *  Product Attribute
 *
 *	@author Jorg Janke
 *	@version $Id: MAttribute.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MAttribute extends X_M_Attribute
{
    /** Logger for class MAttribute */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttribute.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Attributes Of Client
	 *	@param ctx Properties
	 *	@param onlyProductAttributes only Product Attributes
	 *	@param onlyListAttributes only List Attributes
	 *	@return array of attributes
	 */
	public static MAttribute[] getOfClient(Ctx ctx, 
		boolean onlyProductAttributes, boolean onlyListAttributes)
	{
		ArrayList<MAttribute> list = new ArrayList<MAttribute>();
		int AD_Client_ID = ctx.getAD_Client_ID();
		String sql = "SELECT * FROM M_Attribute "
			+ "WHERE AD_Client_ID=? AND IsActive='Y'";
		if (onlyProductAttributes)
			sql += " AND IsInstanceAttribute='N'";
		if (onlyListAttributes)
			sql += " AND AttributeValueType='L'";
		sql += " ORDER BY Name";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAttribute (ctx, rs, null));
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MAttribute[] retValue = new MAttribute[list.size ()];
		list.toArray (retValue);
		s_log.fine("AD_Client_ID=" + AD_Client_ID + " - #" + retValue.length);
		return retValue;
	}	//	getOfClient
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MAttribute.class);

	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Attribute_ID id
	 *	@param trx transaction
	 */
	public MAttribute (Ctx ctx, int M_Attribute_ID, Trx trx)
	{
		super (ctx, M_Attribute_ID, trx);
		if (M_Attribute_ID == 0)
		{
			setAttributeValueType(ATTRIBUTEVALUETYPE_StringMax40);
			setIsInstanceAttribute (false);
			setIsMandatory (false);
		}
	}	//	MAttribute

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAttribute (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAttribute

	/**	Values						*/
	private MAttributeValue[]		m_values = null;

	/**
	 *	Get Values if List
	 *	@return Values or null if not list
	 */
	public MAttributeValue[] getMAttributeValues()
	{
		if (m_values == null && ATTRIBUTEVALUETYPE_List.equals(getAttributeValueType()))
		{
			ArrayList<MAttributeValue> list = new ArrayList<MAttributeValue>();
			if (!isMandatory())
				list.add (null);
			//
			String sql = "SELECT * FROM M_AttributeValue "
				+ "WHERE M_Attribute_ID=? "
				+ "ORDER BY Value";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getM_Attribute_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
					list.add(new MAttributeValue (getCtx(), rs, get_Trx()));
			}
			catch (SQLException ex)
			{
				log.log(Level.SEVERE, sql, ex);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			m_values = new MAttributeValue[list.size()];
			list.toArray(m_values);
		}
		return m_values;
	}	//	getValues

	
	/**************************************************************************
	 * 	Get Attribute Instance
	 *	@param M_AttributeSetInstance_ID attribute set instance
	 *	@return Attribute Instance or null
	 */
	public MAttributeInstance getMAttributeInstance (int M_AttributeSetInstance_ID)
	{
		MAttributeInstance retValue = null;
		String sql = "SELECT * "
			+ "FROM M_AttributeInstance "
			+ "WHERE M_Attribute_ID=? AND M_AttributeSetInstance_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_Attribute_ID());
			pstmt.setInt(2, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MAttributeInstance (getCtx(), rs, get_Trx());
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	//	getAttributeInstance

	/**
	 * 	Set Attribute Instance
	 * 	@param value value
	 * 	@param M_AttributeSetInstance_ID id
	 */
	public void setMAttributeInstance (int M_AttributeSetInstance_ID, MAttributeValue value)
	{
		MAttributeInstance instance = getMAttributeInstance(M_AttributeSetInstance_ID);
		if (instance == null)
		{
			if (value != null)
				instance = new MAttributeInstance (getCtx (), getM_Attribute_ID (),
					M_AttributeSetInstance_ID, value.getM_AttributeValue_ID (),
					value.getName (), get_Trx()); 					//	Cached !!
			else
				instance = new MAttributeInstance (getCtx(), getM_Attribute_ID(),
					M_AttributeSetInstance_ID, 0, null, get_Trx());
		}
		else
		{
			if (value != null)
			{
				instance.setM_AttributeValue_ID (value.getM_AttributeValue_ID ());
				instance.setValue (value.getName()); 	//	Cached !!
			}
			else
			{
				instance.setM_AttributeValue_ID (0);
				instance.setValue (null);
			}
		}
		instance.save();
	}	//	setAttributeInstance

	/**
	 * 	Set Attribute Instance
	 * 	@param value string value
	 * 	@param M_AttributeSetInstance_ID id
	 */
	public void setMAttributeInstance (int M_AttributeSetInstance_ID, String value)
	{
		MAttributeInstance instance = getMAttributeInstance(M_AttributeSetInstance_ID);
		if (instance == null)
			instance = new MAttributeInstance (getCtx(), getM_Attribute_ID(), 
				M_AttributeSetInstance_ID, value, get_Trx());
		else
			instance.setValue(value);
		instance.save();
	}	//	setAttributeInstance

	/**
	 * 	Set Attribute Instance
	 * 	@param value number value
	 * 	@param M_AttributeSetInstance_ID id
	 */
	public void setMAttributeInstance (int M_AttributeSetInstance_ID, BigDecimal value)
	{
		MAttributeInstance instance = getMAttributeInstance(M_AttributeSetInstance_ID);
		if (instance == null)
			instance = new MAttributeInstance (getCtx(), getM_Attribute_ID(), 
				M_AttributeSetInstance_ID, value, get_Trx());
		else
			instance.setValueNumber(value);
		instance.save();
	}	//	setAttributeInstance
	
	public String getName(String language){
		String name = getName();
		if (language != null && !language.isEmpty()) { 
			String translatedName = get_Translation("Name", language);
			if (translatedName != null && !translatedName.isEmpty())
				name = translatedName;
		}
		return name;	
	}
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAttribute[");
		sb.append (get_ID()).append ("-").append (getName())
			.append(",Type=").append(getAttributeValueType())
			.append(",Instance=").append(isInstanceAttribute())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	AfterSave
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Changed to Instance Attribute
		if (!newRecord && is_ValueChanged("IsInstanceAttribute") && isInstanceAttribute())
		{
			String sql = "UPDATE M_AttributeSet mas "
				+ "SET IsInstanceAttribute='Y' "
				+ "WHERE IsInstanceAttribute='N'"
				+ " AND EXISTS (SELECT * FROM M_AttributeUse mau "
					+ "WHERE mas.M_AttributeSet_ID=mau.M_AttributeSet_ID"
					+ " AND mau.M_Attribute_ID= ?)";
			int no = DB.executeUpdate(get_Trx(), sql,getM_Attribute_ID());
			log.fine("AttributeSet Instance set #" + no);
		}
		return success;
	}	//	afterSave
	
}	//	MAttribute
