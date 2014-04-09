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

import org.compiere.util.*;

/**
 * CStage Element
 * 
 * @author Yves Sandfort
 * @version $Id$
 */
public class MCStageElement extends X_CM_CStage_Element
{
    /** Logger for class MCStageElement */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCStageElement.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/***************************************************************************
     * Standard Constructor
     * 
     * @param ctx  context
     * @param CM_CStage_Element_ID id
     * @param trx transaction
     */
	public MCStageElement (Ctx ctx, int CM_CStage_Element_ID, Trx trx)
	{
		super (ctx, CM_CStage_Element_ID, trx);
		if (CM_CStage_Element_ID == 0)
		{
			setIsValid(false);
		}
	}	// MCStageElement

	/**
     * Load Constructor
     * 
     * @param ctx context
     * @param rs result set
     * @param trx transaction
     */
	public MCStageElement (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	} 	// MCStageElement
	
	/** Logger								*/
	private static CLogger		s_log = CLogger.getCLogger (MCStageElement.class);
	
	/**
	 * 	get By ElementName
	 *  @param ctx 
	 *	@param CM_CStage_ID
	 *	@param elementName
	 *  @param trx 
	 *	@return Element by Name
	 */
	public static MCStageElement getByName (Ctx ctx, int CM_CStage_ID, String elementName, Trx trx)
	{
		String sql = "SELECT * FROM CM_CStage_Element WHERE CM_CStage_ID=? AND Name LIKE ?";
		MCStageElement thisElement = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, CM_CStage_ID);
			pstmt.setString (2, elementName);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				thisElement = new MCStageElement(ctx, rs, trx);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getByName", e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return thisElement;
	}	//	getByName
	
	/**	m_parent					*/
	private MCStage m_parent	= null;
	
	/**
	 * 	getParent MCStage Object
	 *	@return Container Stage
	 */
	public MCStage getParent() 
	{
		if (m_parent!=null)
			return m_parent;
		m_parent = new MCStage(getCtx(), getCM_CStage_ID (), get_Trx ());
		return m_parent;
	}
	
	/**
	 * 	After Save.
	 *	@param newRecord insert
	 *	@param success save success
	 *	@return true if saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (!getParent().isModified ()) 
		{
			getParent().setIsModified (true);
			getParent().save ();
		}
		return success;
	}	//	afterSave


}	//	MCStageElement
