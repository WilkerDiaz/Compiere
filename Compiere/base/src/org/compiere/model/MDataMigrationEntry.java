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
import java.util.*;
import java.util.logging.*;

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 * 	Data Migration Entry Model
 *	@author Jorg Janke
 */
public class MDataMigrationEntry extends X_AD_DataMigrationEntry
{
    /** Logger for class MDataMigrationEntry */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDataMigrationEntry.class);

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_DataMigrationEntry_ID id
	 *	@param trx p_trx
	 */
	public MDataMigrationEntry(Ctx ctx, int AD_DataMigrationEntry_ID,
			Trx trx)
	{
		super(ctx, AD_DataMigrationEntry_ID, trx);
		if (AD_DataMigrationEntry_ID == 0)
		{
			setDataMigrationEntryType (DATAMIGRATIONENTRYTYPE_Record);
		//	setAD_Table_ID (0);
		}
	}	//	MDataMigrationEntry

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MDataMigrationEntry(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDataMigrationEntry

	/**
	 * 	Parent Record Constructor
	 *	@param parent migration
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 */
	public MDataMigrationEntry(MDataMigration parent, int AD_Table_ID, int Record_ID)
	{
		this(parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_DataMigration_ID(parent.getAD_DataMigration_ID());
		setDataMigrationEntryType (DATAMIGRATIONENTRYTYPE_Record);
		setAD_Table_ID(AD_Table_ID);
		setRecord_ID(Record_ID);
	}	//	MDataMigrationEntry
	
	/**
	 * 	Before Save
	 * 	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord) 
	{
		String et = getDataMigrationEntryType();
		if (DATAMIGRATIONENTRYTYPE_Record.equals(et))
		{
			if (getWhereClause() != null)
				setWhereClause(null);
		}
		else if (getRecord_ID() != 0)
			setRecord_ID(0);
		
		return true;
	}	//	beforeSave
	
	/**
	 * 	Get POs for Entry
	 * 	@param table table
	 *	@return array of POs
	 */
	public PO[] getEntryPOs(MTable table)
	{
		if (table == null || table.getAD_Table_ID() != getAD_Table_ID())
			table = new MTable(getCtx(), getAD_Table_ID(), null);
		
		//	Single Value
		String et = getDataMigrationEntryType();
		if (DATAMIGRATIONENTRYTYPE_Record.equals(et))
		{
			int Record_ID = getRecord_ID();
			if (Record_ID == 0)
			{
				log.warning("No Record_ID");
				return new PO[]{};
			}
			PO po = table.getPO(getCtx(), Record_ID, get_Trx());
			if (po == null || po.get_ID() != Record_ID)
			{
				log.warning("Not found: Record_ID=" + Record_ID);
				return new PO[]{};
			}
			return new PO[]{po};
		}
			
		//	Where Clause
		MDataMigration data = MDataMigration.get(getCtx(), getAD_DataMigration_ID());
		ArrayList<PO> list = new ArrayList<PO>();
		StringBuffer sql = new StringBuffer("SELECT * FROM ").append(table.getTableName())
			.append(" WHERE ").append(getWhereClause())
			.append(data.getSecurityWhereClause());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(table.getPO(getCtx(), rs, get_Trx()));
		} 
		catch (Exception e) 
		{
			log.log(Level.WARNING, sql.toString());
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		PO[] retValue = new PO[list.size()];
		list.toArray(retValue);
		return retValue;
	}
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MDataMigrationEntry[")
	    	.append(get_ID())
	    	.append(",AD_Table_ID=").append(getAD_Table_ID());
	    String s = getWhereClause();
	    if (!Util.isEmpty(s))
	    	sb.append(",Where=").append(s);
	    if (getRecord_ID() != 0)
	    	sb.append(",Record_ID=").append(getRecord_ID());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
	
}	//	MDataMigrationEntry
