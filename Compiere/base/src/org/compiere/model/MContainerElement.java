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
public class MContainerElement extends X_CM_Container_Element
{
    /** Logger for class MContainerElement */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MContainerElement.class);
	/**	serialVersionUID	*/
	private static final long serialVersionUID = 7230036377422361941L;

	/** Logger */
	private static CLogger s_log = CLogger.getCLogger (MContainer.class);

	/**
	 * 	get Container Element by ID
	 *	@param ctx
	 *	@param CM_ContainerElement_ID
	 *	@param trx
	 *	@return ContainerElement
	 */
	public static MContainerElement get(Ctx ctx, int CM_ContainerElement_ID, Trx trx) {
		MContainerElement thisContainerElement = null;
		String sql = "SELECT * FROM CM_Container_Element WHERE CM_Container_Element_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, CM_ContainerElement_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				thisContainerElement = (new MContainerElement(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return thisContainerElement;
	}

	/***************************************************************************
     * Standard Constructor
     * 
     * @param ctx context
     * @param CM_Container_Element_ID id
     * @param trx transaction
     */
	public MContainerElement (Ctx ctx, int CM_Container_Element_ID, Trx trx)
	{
		super (ctx, CM_Container_Element_ID, trx);
		if (CM_Container_Element_ID == 0)
		{
			setIsValid(false);
		}
	}	// MContainerElement

	/**
     * Load Constructor
     * 
     * @param ctx context
     * @param rs result set
     * @param trx transaction
     */
	public MContainerElement (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	// MContainerElement

	/** Parent				*/
	private MContainer m_parent = null;
	
	/**
	 * 	Get Container get's related Container
	 *	@return MContainer
	 */
	public MContainer getParent()
	{
		if (m_parent == null)
			m_parent = new MContainer (getCtx(), getCM_Container_ID(), get_Trx());
		return m_parent;

		/** No reason to do this ?? - should never return null - always there - JJ
		int[] thisContainer = MContainer.getAllIDs("CM_Container","CM_Container_ID=" + this.getCM_Container_ID(), get_TrxName());
		if (thisContainer != null) 
		{
			if (thisContainer.length==1)
				return new MContainer(getCtx(), thisContainer[0], get_TrxName());
		}
		return null;
		**/
	}	//	getContainer
	
	/**
	 * 	After Save.
	 * 	Insert
	 * 	- create / update index
	 *	@param newRecord insert
	 *	@param success save success
	 *	@return true if saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		reIndex(newRecord);
		return success;
	}	//	afterSave
	
	/**
	 * 	reIndex
	 *	@param newRecord
	 */
	public void reIndex(boolean newRecord)
	{
		if (getParent().isIndexed ()) {
			int CMWebProjectID = 0;
			if (getParent()!=null)
				CMWebProjectID = getParent().getCM_WebProject_ID();
			String [] toBeIndexed = new String[3];
			toBeIndexed[0] = this.getName();
			toBeIndexed[1] = this.getDescription();
			toBeIndexed[2] = this.getContentHTML();
			MIndex.reIndex (newRecord, toBeIndexed, getCtx(), 
				getAD_Client_ID(), get_Table_ID(), get_ID(), CMWebProjectID, this.getUpdated());
		}
		if (!getParent().isIndexed () && !newRecord)
			MIndex.cleanUp (get_Trx(), getAD_Client_ID(), get_Table_ID(), get_ID());
	}	// reIndex
	
}	//	MContainerElement
