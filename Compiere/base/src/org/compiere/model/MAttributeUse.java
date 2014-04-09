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
 *	Attribute Use Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAttributeUse.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MAttributeUse extends X_M_AttributeUse
{
    /** Logger for class MAttributeUse */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MAttributeUse (Ctx ctx, int ignored, Trx trx)
	{
		super (ctx, ignored, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MAttributeUse

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAttributeUse (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAttributeUse

	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	also used for afterDelete
		String sql = "UPDATE M_AttributeSet mas"
			+ " SET IsInstanceAttribute='Y' "
			+ "WHERE M_AttributeSet_ID= ? "
			+ " AND IsInstanceAttribute='N'"
			+ " AND (IsSerNo='Y' OR IsLot='Y' OR IsGuaranteeDate='Y'"
				+ " OR EXISTS (SELECT * FROM M_AttributeUse mau"
					+ " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
					+ "WHERE mau.M_AttributeSet_ID=mas.M_AttributeSet_ID"
					+ " AND mau.IsActive='Y' AND ma.IsActive='Y'"
					+ " AND ma.IsInstanceAttribute='Y')"
					+ ")";
		int no = DB.executeUpdate(get_Trx(), sql,getM_AttributeSet_ID());
		if (no != 0)
			log.fine("afterSave - Set Instance Attribute");
		//
		sql = "UPDATE M_AttributeSet mas"
			+ " SET IsInstanceAttribute='N' "
			+ "WHERE M_AttributeSet_ID=? "
			+ " AND IsInstanceAttribute='Y'"
			+ "	AND IsSerNo='N' AND IsLot='N' AND IsGuaranteeDate='N'"
			+ " AND NOT EXISTS (SELECT * FROM M_AttributeUse mau"
				+ " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
				+ "WHERE mau.M_AttributeSet_ID=mas.M_AttributeSet_ID"
				+ " AND mau.IsActive='Y' AND ma.IsActive='Y'"
				+ " AND ma.IsInstanceAttribute='Y')";
		no = DB.executeUpdate(get_Trx(), sql,getM_AttributeSet_ID());
		if (no != 0)
			log.fine("afterSave - Reset Instance Attribute");
		
		return success;
	}	//	afterSave
	
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		afterSave(false, success);
		return success;
	}	//	afterDelete
	
}	//	MAttributeUse
