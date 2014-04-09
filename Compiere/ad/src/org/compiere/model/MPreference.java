/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;


/**
 *	Preference Model	
 *	
 *  @author Jorg Janke
 *  @version $Id: MPreference.java 8776 2010-05-19 17:50:56Z nnayak $
 */
public class MPreference extends X_AD_Preference
{
    /** Logger for class MPreference */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPreference.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Delete Preferences with Attribute & Value
	 *	@param Attribute attribute
	 *	@param Value value
	 *	@return number of records deleted
	 */
	public static int delete (String Attribute, String Value)
	{
		StringBuffer sql = new StringBuffer("DELETE FROM AD_Preference WHERE Attribute='")
			.append(Attribute).append("' AND Value='").append(Value).append("'");
		return DB.executeUpdate((Trx) null, sql.toString());
	}	//	delete
	
	
	/** 
	 * Standard Constructor
	 * @param ctx context
	 * @param AD_Preference_ID id
	 * @param trx transaction
	 */
	public MPreference (Ctx ctx, int AD_Preference_ID, Trx trx)
	{
		super (ctx, AD_Preference_ID, trx);
		if (AD_Preference_ID == 0)
		{
		//	setAttribute (null);
		//	setValue (null);
		}
	}	//	MPreference
	
	/** 
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set
	 * 	@param trx transaction
	 */
	public MPreference (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MPreference	

	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param Attribute attribute
	 *	@param Value value
	 *	@param trx p_trx
	 */
	public MPreference (Ctx ctx, String Attribute, String Value, Trx trx)
	{
		this (ctx, 0, trx);
		setAttribute (Attribute);
		setValue (Value);
	}	//	MPreference

	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		String value = getValue();
		//	NULL
		if (value == null)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "Value"));
			return false;
		}
		//	Don't allow variables in Preferences
		else if (value.indexOf('@') != -1)
		{
			log.saveError("Error", "Invalid Value: @");
			return false;
		}
	//	if (value.equals("-1"))
	//		setValue("");
		return true;
	}	//	beforeSave

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPreference[");
		sb.append (get_ID()).append("-")
			.append(getAttribute()).append("-").append(getValue())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MPreference
