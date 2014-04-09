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

import org.compiere.common.CompiereStateException;
import org.compiere.util.*;


/**
 *	User Defined Tab Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MUserDefTab.java 8756 2010-05-12 21:21:27Z nnayak $
 */
public class MUserDefTab extends X_AD_UserDef_Tab
{
    /** Logger for class MUserDefTab */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MUserDefTab.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_UserDef_Tab_ID id
	 *	@param trx p_trx
	 */
	public MUserDefTab(Ctx ctx, int AD_UserDef_Tab_ID, Trx trx)
	{
		super (ctx, AD_UserDef_Tab_ID, trx);
	}	//	MUserDefTab

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MUserDefTab(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MUserDefTab
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MUserDefTab (MUserDefWin parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg (parent);
		setAD_UserDef_Win_ID (parent.getAD_UserDef_Win_ID());
	}	//	MUserDefTab
	
	
	/**	The Fields						*/
	private MUserDefField[] m_fields = null;
	/** The base Tab					*/
	private MTab			m_tab = null;
	
	/**
	 * 	Get Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MUserDefField[] getFields(boolean reload)
	{
		if (m_fields != null && !reload)
			return m_fields;
		String sql = "SELECT * FROM AD_UserDef_Field "
			+ "WHERE AD_UserDef_Tab_ID=?";
		ArrayList<MUserDefField> list = new ArrayList<MUserDefField>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getAD_UserDef_Tab_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MUserDefField (getCtx(), rs, get_Trx()));
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_fields = new MUserDefField[list.size ()];
		list.toArray (m_fields);
		return m_fields;
	}	//	getFields
	
	/**
	 * 	Get Field with ColumnName
	 *	@param columnName name
	 *	@return field or null
	 */
	public MUserDefField getField (String columnName)
	{
		MTab tab = getTab();
		if (tab == null)
			return null;
		int AD_Field_ID = tab.getAD_Field_ID(columnName);
		return getField (AD_Field_ID);
	}	//	getField

	/**
	 * 	Get Field with ID
	 *	@param AD_Field_ID id
	 *	@return field or null
	 */
	public MUserDefField getField (int AD_Field_ID)
	{
		if (AD_Field_ID == 0)
			return null;
		MUserDefField[] fields = getFields(false);
		for (MUserDefField element : fields) {
	        if (element.getAD_Field_ID() == AD_Field_ID)
	        	return element;
        }
		return null;
	}	//	getField

	
	/**
	 * 	Get Tab
	 *	@return tab
	 */
	public MTab getTab()
	{
		if (m_tab == null)
			m_tab = MTab.get(getCtx(), getAD_Tab_ID());
		return m_tab;
	}	//	getTab
	
	/**
	 * 	Create from Tab (incl. Fields)
	 *	@param tab tab
	 *	@return true if created
	 */
	public boolean create (MTab tab)
	{
		if (getAD_UserDef_Tab_ID() != 0)
			throw new CompiereStateException("Needs to be new");
		m_tab = tab;
		setAD_Tab_ID(tab.getAD_Tab_ID());
	//	setSeqNo(tab.getSeqNo());
		if (!save())
			return false;
		
		//	Tabs
		MField[] fields = tab.getFields (false, get_Trx());
		for (int i = 0; i < fields.length; i++)
		{
			MUserDefField ff = new MUserDefField(this);
			if (!ff.create(fields[i]))
				return false;
			m_fields = null;
		}
		return true;
	}	//	create
	
}	//	MUserDefTab

