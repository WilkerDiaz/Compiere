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
package org.compiere.framework;

import java.io.*;
import java.math.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 *	PO Info Column Info Value Object
 *	
 *  @author Jorg Janke
 *  @version $Id: POInfoColumn.java 8953 2010-06-16 08:04:16Z ragrawal $
 */
public class POInfoColumn implements Serializable
{
	/** Used by Remote FinReport			*/
	static final long serialVersionUID = -3983585608504631958L;
	
	/**
	 *  Constructor
	 *	@param ad_Column_ID Column ID
	 *	@param columnName Column name
	 *	@param columnSQL virtual column
	 *	@param displayType Display Type
	 *	@param isMandatory Mandatory
	 *	@param isUpdateable Updateable
	 *	@param defaultLogic Default Logic
	 *	@param columnLabel Column Label
	 *	@param columnDescription Column Description
	 *	@param isKey true if key
	 *	@param isParent true if parent
	 *	@param ad_Reference_Value_ID reference value
	 *	@param validationCode sql validation code
	 *	@param fieldLength Field Length
	 * 	@param valueMin minimal value
	 * 	@param valueMax maximal value
	 * 	@param isTranslated translated
	 * 	@param isEncrypted encrypted 
	 */
	public POInfoColumn (int ad_Column_ID, String columnName, String columnSQL, int displayType,
		boolean isMandatory, boolean isUpdateable, String defaultLogic,
		String columnLabel, String columnDescription,
		boolean isKey, boolean isParent,
		int ad_Reference_Value_ID, String validationCode,
		int fieldLength, String valueMin, String valueMax,
		boolean isTranslated, boolean isEncrypted)
	{
		AD_Column_ID = ad_Column_ID;
		ColumnName = columnName;
		ColumnSQL = columnSQL;
		DisplayType = displayType;
		if (columnName.equals("AD_Language") 
			|| columnName.equals("EntityType")
			|| columnName.equals("DocBaseType"))
		{
			DisplayType = DisplayTypeConstants.String;
			ColumnClass = String.class;
		}
		else if (columnName.equals("Posted") 
			|| columnName.equals("Processed")
			|| columnName.equals("Processing"))
		{
			ColumnClass = Boolean.class;
		}
		else if (columnName.equals("Record_ID") || columnName.equals("AD_OrgBP_ID"))
		{
			DisplayType = DisplayTypeConstants.ID;
			ColumnClass = Integer.class;
		}
		else
			ColumnClass = org.compiere.util.DisplayType.getClass(displayType, true);
		IsMandatory = isMandatory;
		IsUpdateable = isUpdateable;
		DefaultLogic = defaultLogic;
		ColumnLabel = columnLabel;
		ColumnDescription = columnDescription;
		IsKey = isKey;
		IsParent = isParent;
		//
		AD_Reference_Value_ID = ad_Reference_Value_ID;
		ValidationCode = validationCode;
		//
		FieldLength = fieldLength;
		ValueMin = valueMin;
		try
		{
			if (valueMin != null && valueMin.length() > 0)
				ValueMin_BD = new BigDecimal(valueMin);
		}
		catch (Exception ex)
		{
			CLogger.get().log(Level.SEVERE, "ValueMin=" + valueMin, ex);
		}
		ValueMax = valueMax;
		try
		{
			if (valueMax != null && valueMax.length() > 0)
				ValueMax_BD = new BigDecimal(valueMax);
		}
		catch (Exception ex)
		{
			CLogger.get().log(Level.SEVERE, "ValueMax=" + valueMax, ex);
		}
		IsTranslated = isTranslated;
		IsEncrypted = isEncrypted;
	}   //  Column

	/** Column ID		*/
	public int          AD_Column_ID;
	/** Column Name		*/
	public String       ColumnName;
	/** Virtual Column 	*/
	public String       ColumnSQL;
	/** Display Type	*/
	public int          DisplayType;
	/**	Data Type		*/
	public Class<?>		ColumnClass;
	/**	Mandatory		*/
	public boolean      IsMandatory;
	/**	Default Value	*/
	public String       DefaultLogic;
	/**	Updateable		*/
	public boolean      IsUpdateable;
	/**	Label			*/
	public String       ColumnLabel;
	/**	Description		*/
	public String       ColumnDescription;
	/**	PK				*/
	public boolean		IsKey;
	/**	FK to Parent	*/
	public boolean		IsParent;
	/**	Translated		*/
	public boolean		IsTranslated;
	/**	Encryoted		*/
	public boolean		IsEncrypted;
	
	/** Reference Value	*/
	public int			AD_Reference_Value_ID;
	/** Validation		*/
	public String		ValidationCode;
	
	/** Field Length	*/
	public int			FieldLength;
	/**	Min Value		*/
	public String		ValueMin;
	/**	Max Value		*/
	public String		ValueMax;
	/**	Min Value		*/
	public BigDecimal	ValueMin_BD = null;
	/**	Max Value		*/
	public BigDecimal	ValueMax_BD = null;

	/**
	 * 	String representation
	 *  @return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("POInfo.Column[");
		sb.append(ColumnName).append(",ID=").append(AD_Column_ID)
			.append(",DisplayType=").append(DisplayType)
			.append(",ColumnClass=").append(ColumnClass);
		sb.append("]");
		return sb.toString();
	}	//	toString

}	//	POInfoColumn
