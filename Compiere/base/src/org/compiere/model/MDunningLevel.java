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
 *	Dunning Level Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDunningLevel.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MDunningLevel extends X_C_DunningLevel
{
    /** Logger for class MDunningLevel */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDunningLevel.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Logger								*/
	private static CLogger		s_log = CLogger.getCLogger (MDunningLevel.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DunningLevel_ID id
	 *	@param trx transaction
	 */
	public MDunningLevel (Ctx ctx, int C_DunningLevel_ID, Trx trx)
	{
		super (ctx, C_DunningLevel_ID, trx);
	}	//	MDunningLevel

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDunningLevel (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDunningLevel
	
	private MDunning m_dunning = null;
	
	/**
	 * 	get Parent
	 *	@return Parent Dunning
	 */
	public MDunning getParent() 
	{
		if (m_dunning==null) 
			m_dunning = new MDunning(getCtx(), getC_Dunning_ID(), get_Trx());
		return m_dunning;
	}
	
	/**
	 * 	get Previous Levels
	 *	@return Array of previous DunningLevels
	 */
	public MDunningLevel[] getPreviousLevels() 
	{
		// Prevent generation if not Sequentially
		if (!getParent().isCreateLevelsSequentially ())
			return null;
		ArrayList<MDunningLevel> list = new ArrayList<MDunningLevel>();
		String sql = "SELECT * FROM C_DunningLevel WHERE C_Dunning_ID=? AND DaysAfterDue+DaysBetweenDunning<?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getParent().get_ID ());
			int totalDays = getDaysAfterDue ().intValue ()+getDaysBetweenDunning ();
			pstmt.setInt(2, totalDays);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MDunningLevel(getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		//
		MDunningLevel[] retValue = new MDunningLevel[list.size()];
		list.toArray(retValue);
		return retValue;
	}
}	//	MDunningLevel
