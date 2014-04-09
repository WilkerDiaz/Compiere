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

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 * 	Data Migration Preview Model
 *	@author Jorg Janke
 */
public class MDataMigrationPreview extends X_AD_DataMigrationPreview
{
    /** Logger for class MDataMigrationPreview */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDataMigrationPreview.class);
	/** */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Delete Preview Data
	 *	@param AD_DataMigration_ID data migration
	 *	@param trx transaction
	 *	@return records deleted
	 */
	public static int delete (int AD_DataMigration_ID, Trx trx)
	{
		String sql = "DELETE FROM AD_DataMigrationPreview WHERE AD_DataMigration_ID=?";
		int count = DB.executeUpdate(trx, sql, AD_DataMigration_ID);
		return count;
	}	//	delete


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_DataMigrationPreview_ID id
	 *	@param trx p_trx
	 */
	public MDataMigrationPreview(Ctx ctx, int AD_DataMigrationPreview_ID,
			Trx trx)
	{
		super(ctx, AD_DataMigrationPreview_ID, trx);
	}	//	MDataMigrationPreview

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MDataMigrationPreview(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDataMigrationPreview

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MDataMigrationPreview(MDataMigration parent)
	{
		super(parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_DataMigration_ID(parent.getAD_DataMigration_ID());
	}	//	MDataMigrationPreview

	/**
	 * 	Set PO info
	 *	@param po po
	 */
	public void setPOInfo (PO po)
	{
		setAD_Table_ID(po.get_Table_ID());
		setRecord_ID(po.get_ID());
		//
		String dd = po.toStringX();
		if (dd.endsWith("]"))
		{
			int index = dd.indexOf("[");
			if (index != -1)
				dd = dd.substring(index+1, dd.length()-1);
		}
		if (dd.length() > 255)
			dd = dd.substring(0,255);
		setDescription(dd);
		//
		MTable table = MTable.get(po.getCtx(), po.get_Table_ID());
		setTableUID(UniqueID.getUniqueID(table, po));
	}	//	setPOInfo

	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MDataMigrationPreview[")
	    	.append(get_ID())
	    	.append(",LevelNo=").append(getLevelNo())
	        .append(",AD_Table_ID=").append(getAD_Table_ID())
	        .append(",Info=").append(getInfo())
	        .append(",Description=").append(getDescription());
	    sb.append("]");
	    return sb.toString();
    }	//	toString

}	//	MDataMigrationPreview
