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
import org.compiere.util.*;

/**
 *	Tab Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTab.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MTab extends X_AD_Tab
{
    /** Logger for class MTab */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTab.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 	Get MTab from Cache
     *	@param ctx context
     *	@param AD_Tab_ID id
     *	@return MTab
     */
    public static MTab get(Ctx ctx, int AD_Tab_ID)
    {
	    Integer key = Integer.valueOf(AD_Tab_ID);
	    MTab retValue = s_cache.get(ctx, key);
	    if (retValue != null)
		    return retValue;
	    retValue = new MTab(ctx, AD_Tab_ID, null);
	    if (retValue.get_ID() != 0)
		    s_cache.put(key, retValue);
	    return retValue;
    } //	get

    /**	Cache						*/
    private static final CCache<Integer, MTab> s_cache 
    	= new CCache<Integer, MTab>("AD_Tab", 20);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Tab_ID id
	 *	@param trx transaction
	 */
	public MTab (Ctx ctx, int AD_Tab_ID, Trx trx)
	{
		super (ctx, AD_Tab_ID, trx);
		if (AD_Tab_ID == 0)
		{
		//	setAD_Window_ID (0);
		//	setAD_Table_ID (0);
		//	setName (null);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setHasTree (false);
			setIsReadOnly (false);
			setIsSingleRow (false);
			setIsSortTab (false);	// N
			setIsTranslationTab (false);
			setSeqNo (0);
			setTabLevel (0);
			setIsInsertRecord(true);
			setIsAdvancedTab(false);
		}
	}	//	M_Tab

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MTab (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	M_Tab

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MTab (MWindow parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_Window_ID(parent.getAD_Window_ID());
		setEntityType(parent.getEntityType());
	}	//	M_Tab

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param from copy from
	 */
	public MTab (MWindow parent, MTab from)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		copyValues(from, this);
		setClientOrg(parent);
		setAD_Window_ID(parent.getAD_Window_ID());
		setEntityType(parent.getEntityType());
	}	//	M_Tab
	
	
	/**	The Fields						*/
	private MField[]				m_fields = null;
	/** Map of ColumnName and AD_Field_ID	*/
	private HashMap<String,Integer>	m_columnNameField = null;

	/**
	 * 	Get Fields
	 *	@param reload reload data
	 *	@return array of lines
	 *	@param trx transaction
	 */
	public MField[] getFields (boolean reload, Trx trx)
	{
		if (m_fields != null && !reload)
			return m_fields;
		String sql = "SELECT * FROM AD_Field WHERE AD_Tab_ID=? ORDER BY SeqNo";
		ArrayList<MField> list = new ArrayList<MField>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, getAD_Tab_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MField (getCtx(), rs, trx));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_fields = new MField[list.size ()];
		list.toArray (m_fields);
		return m_fields;
	}	//	getFields

	/**
	 * 	Get Field with ID
	 *	@param AD_Field_ID id
	 *	@return field or null
	 */
	public MField getField (int AD_Field_ID)
	{
		if (AD_Field_ID == 0)
			return null;
		MField[] fields = getFields(false, get_Trx());
		for (MField element : fields) {
			if (element.getAD_Field_ID() == AD_Field_ID)
				return element;
        }
		return null;
	}	//	getField
	
	/**
	 * 	Get Field with name
	 *	@param columnName name
	 *	@return field or null
	 */
	public MField getField (String columnName)
	{
		int AD_Field_ID = getAD_Field_ID(columnName);
		return getField(AD_Field_ID);
	}	//	getField
	
	/**
	 * 	Get AD_Field_ID in tab
	 *	@param columnName name
	 *	@return id
	 */
	public int getAD_Field_ID (String columnName)
	{
		if (m_columnNameField == null)
			fillColumnNameField();
		Integer AD_Field_ID = m_columnNameField.get(columnName);
		if (AD_Field_ID == null)
			return 0;
		return AD_Field_ID.intValue();
	}	//	getAD_Field_ID
	
	/**
	 * 	Fill ColumnName Field map
	 */
	private void fillColumnNameField()
	{
		m_columnNameField = new HashMap<String,Integer>();
		String sql = "SELECT ColumnName, f.AD_Field_ID "
			+ "FROM AD_Field f"
			+ " INNER JOIN AD_Column c ON (f.AD_Column_ID=c.AD_Column_ID) "
			+ "WHERE f.AD_Tab_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, (Trx) null);
	        pstmt.setInt(1, getAD_Tab_ID());
	        rs = pstmt.executeQuery();
	        while (rs.next())
	        {
	        	String columnName = rs.getString(1);
	        	int AD_Field_ID = rs.getInt(2);
	        	m_columnNameField.put(columnName, AD_Field_ID);
	        }
        }
        catch (Exception e) {
	        log.log(Level.SEVERE, sql, e);
        }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	fillColumnNameField

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
	//	UPDATE AD_Tab SET IsInsertRecord='N' WHERE IsInsertRecord='Y' AND IsReadOnly='Y'
		if (isReadOnly() && isInsertRecord())
			setIsInsertRecord(false);
		return true;
	}
	
	/**
	 * 	String Info
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MTab[");
		sb.append(get_ID())
			.append("-").append(getName())
			.append("]");
		return sb.toString();
	}	//	toString

}	//	M_Tab
