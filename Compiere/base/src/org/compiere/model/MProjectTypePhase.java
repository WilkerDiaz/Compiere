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
 * 	Project Type Phase Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProjectTypePhase.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MProjectTypePhase extends X_C_Phase
{
    /** Logger for class MProjectTypePhase */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProjectTypePhase.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Phase_ID id
	 *	@param trx p_trx
	 */
	public MProjectTypePhase (Ctx ctx, int C_Phase_ID, Trx trx)
	{
		super (ctx, C_Phase_ID, trx);
		if (C_Phase_ID == 0)
		{
		//	setC_Phase_ID (0);			//	PK
		//	setC_ProjectType_ID (0);	//	Parent
		//	setName (null);
			setSeqNo (0);
			setStandardQty (Env.ZERO);
		}
	}	//	MProjectTypePhase

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx
	 */
	public MProjectTypePhase (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProjectTypePhase

	/**
	 * 	Get Project Type Phases
	 *	@return Array of phases
	 */
	public MProjectTypeTask[] getTasks()
	{
		ArrayList<MProjectTypeTask> list = new ArrayList<MProjectTypeTask>();
		String sql = "SELECT * FROM C_Task WHERE C_Phase_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Phase_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MProjectTypeTask (getCtx(), rs, get_Trx()));
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MProjectTypeTask[] retValue = new MProjectTypeTask[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getPhases

}	//	MProjectTypePhase
