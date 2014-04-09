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

import org.compiere.util.*;


/**
 *	Field Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MField.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MField extends X_AD_Field
{
    /** Logger for class MField */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MField.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Field_ID id
	 *	@param trx transaction
	 */
	public MField (Ctx ctx, int AD_Field_ID, Trx trx)
	{
		super (ctx, AD_Field_ID, trx);
		if (AD_Field_ID == 0)
		{
		//	setAD_Tab_ID (0);	//	parent
		//	setAD_Column_ID (0);
		//	setName (null);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsCentrallyMaintained (true);	// Y
			setIsDisplayed (true);	// Y
			setIsEncrypted (false);
			setIsFieldOnly (false);
			setIsHeading (false);
			setIsReadOnly (false);
			setIsSameLine (false);
		//	setObscureType(OBSCURETYPE_ObscureDigitsButLast4);
		//	setIsMandatory (null);
		}	
	}	//	MField

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MField (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MField

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MField (MTab parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_Tab_ID(parent.getAD_Tab_ID());
	}	//	MField
	
	/**
	 * 	Copy Constructor
	 *	@param parent parent
	 *	@param from copy from
	 */
	public MField (MTab parent, MField from)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		copyValues(from, this);
		setClientOrg(parent);
		setAD_Tab_ID(parent.getAD_Tab_ID());
		setEntityType(parent.getEntityType());
	}	//	M_Field
	
	/**	Column 		*/
	private MColumn m_column = null;
	
	/**
	 * 	Set Column Values
	 *	@param column column
	 */
	public void setColumn (MColumn column)
	{
		m_column = column;
		setAD_Column_ID (column.getAD_Column_ID());
		setName (column.getName());
		setDescription(column.getDescription());
		setHelp(column.getHelp());
		setDisplayLength(column.getFieldLength());
		setEntityType(column.getEntityType());
	}	//	setColumn
	
	/**
	 * 	Get Column
	 *	@return column
	 */
	public MColumn getColumn()
	{
		if (m_column == null 
			|| m_column.getAD_Column_ID() != getAD_Column_ID())
			m_column = MColumn.get (getCtx(), getAD_Column_ID());
		return m_column;
	}	//	getColumn
	
	/**
	 * 	Set AD_Column_ID
	 *	@param AD_Column_ID column
	 */
	@Override
	public void setAD_Column_ID(int AD_Column_ID)
	{
		if (m_column != null && m_column.getAD_Column_ID() != AD_Column_ID)
			m_column = null;
		super.setAD_Column_ID (AD_Column_ID);
	}	//	setAD_Column_ID
	
	
	/**
	 * 	Mandatory UI
	 *	@return true if mandatory
	 */
	protected boolean isMandatoryUI()
	{
		String m = getIsMandatoryUI();
		if (m == null)
			m = getColumn().isMandatoryUI() ? "Y" : "N";
		return m.equals("Y");
	}	//	isMandatoryUI
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		//	Sync Terminology
		if ((newRecord || is_ValueChanged("AD_Column_ID")) 
			&& isCentrallyMaintained())
		{
			M_Element element = M_Element.getOfColumn(getCtx(), getAD_Column_ID());
			setName (element.getName ());
			setDescription (element.getDescription ());
			setHelp (element.getHelp());
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	String Info
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MField[");
		sb.append(get_ID())
			.append("-").append(getName())
			.append("]");
		return sb.toString();
	}	//	toString

	
}	//	MField
