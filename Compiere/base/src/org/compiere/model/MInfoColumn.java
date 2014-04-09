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
 * 	Info Window Column Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInfoColumn.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MInfoColumn extends X_AD_InfoColumn
{
    /** Logger for class MInfoColumn */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInfoColumn.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_InfoColumn_ID id
	 *	@param trx transaction
	 */
	public MInfoColumn (Ctx ctx, int AD_InfoColumn_ID, Trx trx)
	{
		super (ctx, AD_InfoColumn_ID, trx);
		if (AD_InfoColumn_ID == 0)
		{
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsDisplayed (true);	// Y
			setIsQueryCriteria (false);
			setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_InfoColumn WHERE AD_InfoWindow_ID=@AD_InfoWindow_ID@
		}
	}	//	MInfoColumn

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MInfoColumn (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MInfoColumn
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		//	Sync Terminology
		if ((newRecord || is_ValueChanged ("AD_Element_ID")) 
			&& getAD_Element_ID() != 0 
			&& isCentrallyMaintained())
		{
			M_Element element = new M_Element (getCtx(), getAD_Element_ID (), null);
			setName (element.getName ());
			setDescription (element.getDescription ());
			setHelp (element.getHelp());
		}
		//	Auto Numbering
		if (newRecord && getSeqNo() == 0)
		{
			String sql = "SELECT COALESCE(MAX(SeqNo),0)+10 FROM AD_InfoColumn WHERE AD_InfoWindow_ID=?";
			int no = QueryUtil.getSQLValue(get_Trx(), sql, getAD_InfoWindow_ID());
			setSeqNo(no);
		}
		
		return true;
	}	//	beforeSave

	
}	//	MInfoColumn
