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
package org.compiere.framework;

import java.math.*;
import java.sql.*;

import org.compiere.controller.*;
import org.compiere.util.*;


/**
 *	Field Value Validator
 *	
 *  @author Jorg Janke
 *  @version $Id: FieldvalueValidator.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class FieldvalueValidator
{
	
	public FieldvalueValidator (UIField field)
	{
		field.getAD_Reference_ID();
		field.getAD_Reference_Value_ID();
		field.getFieldLength();
		field.getValueMin();
		field.getValueMax();
		field.getVFormat();
	}
	
	public FieldvalueValidator (POInfoColumn info)
	{
	}
	
	/**************************************************************************
	 * 	Validate new Value as String
	 *	@param newValue new value
	 *	@return validated value or null
	 *	@throws CompiereValueException
	 */
	public String validateAsString (String newValue)
		throws CompiereValueException
	{
		return newValue;
	}	//	validateAsString

	public Object validate (Object newValue)
		throws CompiereValueException
	{
		return newValue;
	}	//	validate
	
	public String validate (String newValue)
		throws CompiereValueException
	{
		return newValue;
	}	//	validate
	
	public Integer validate (Integer newValue)
		throws CompiereValueException
	{
		return newValue;
	}	//	validate

	public BigDecimal validate (BigDecimal newValue)
		throws CompiereValueException
	{
		return newValue;
	}	//	validate

	public Timestamp validate (Timestamp newValue)
		throws CompiereValueException
	{
		return newValue;
	}	//	validate

}	//	FieldvalueValidator
