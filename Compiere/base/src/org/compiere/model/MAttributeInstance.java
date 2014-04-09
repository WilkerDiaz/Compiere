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

import org.compiere.util.*;

/**
 *  Product Attribute Set
 *
 *	@author Jorg Janke
 *	@version $Id: MAttributeInstance.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MAttributeInstance extends X_M_AttributeInstance
{
    /** Logger for class MAttributeInstance */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeInstance.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MAttributeInstance (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MAttributeInstance

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAttributeInstance (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAttributeInstance

	/**
	 * 	String Value Constructior
	 *	@param ctx context
	 *	@param M_Attribute_ID attribute
	 *	@param M_AttributeSetInstance_ID instance
	 *	@param Value string value
	 *	@param trx transaction
	 */
	public MAttributeInstance (Ctx ctx, int M_Attribute_ID, 
		int M_AttributeSetInstance_ID, String Value, Trx trx)
	{
		super(ctx, 0, trx);
		setM_Attribute_ID (M_Attribute_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		setValue (Value);
	}	//	MAttributeInstance
	
	/**
	 * 	Number Value Constructior
	 *	@param ctx context
	 *	@param M_Attribute_ID attribute
	 *	@param M_AttributeSetInstance_ID instance
	 *	@param BDValue number value
	 *	@param trx transaction
	 */
	public MAttributeInstance (Ctx ctx, int M_Attribute_ID, 
		int M_AttributeSetInstance_ID, BigDecimal BDValue, Trx trx)
	{
		super(ctx, 0, trx);
		setM_Attribute_ID (M_Attribute_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		setValueNumber(BDValue);
	}	//	MAttributeInstance

	/**
	 * 	Selection Value Constructior
	 *	@param ctx context
	 *	@param M_Attribute_ID attribute
	 *	@param M_AttributeSetInstance_ID instance
	 *	@param M_AttributeValue_ID selection
	 * 	@param Value String representation for fast display
	 *	@param trx transaction
	 */
	public MAttributeInstance (Ctx ctx, int M_Attribute_ID, 
		int M_AttributeSetInstance_ID, int M_AttributeValue_ID, String Value, Trx trx)
	{
		super(ctx, 0, trx);
		setM_Attribute_ID (M_Attribute_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		setM_AttributeValue_ID (M_AttributeValue_ID);
		setValue (Value);
	}	//	MAttributeInstance

	
	/**
	 * 	Set ValueNumber
	 *	@param ValueNumber number
	 */
	@Override
	public void setValueNumber (BigDecimal ValueNumber)
	{
		super.setValueNumber (ValueNumber);
		if (ValueNumber == null)
		{
			setValue(null);
			return;
		}
		if (ValueNumber.signum() == 0)
		{
			setValue("0");
			return;
		}
		//	Display number w/o decimal 0
		char[] chars = ValueNumber.toString().toCharArray();
		StringBuffer display = new StringBuffer();
		boolean add = false;
		for (int i = chars.length-1; i >= 0; i--)
		{
			char c = chars[i];
			if (add)
				display.insert(0, c);
			else
			{
				if (c == '0')
					continue;
				else if (c == '.')	//	decimal point
					add = true;
				else
				{
					display.insert(0, c);
					add = true;
				}
			}
		}			
		setValue(display.toString());
	}	//	setValueNumber
	
	
	/**
	 *	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		return getValue();
	}	//	toString

}	//	MAttributeInstance
