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
 *	Migration Step
 *	
 *  @author Jorg Janke
 *  @version $Id: MMigrationStep.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MMigrationStep extends X_AD_MigrationStep
{
    /** Logger for class MMigrationStep */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMigrationStep.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param AD_MigrationStep_ID id
	 *	@param trx p_trx
	 */
	public MMigrationStep(Ctx ctx, int AD_MigrationStep_ID, Trx trx)
	{
		super (ctx, AD_MigrationStep_ID, trx);
		if (AD_MigrationStep_ID == 0)
		{
			setIsOkToFail (true);	// Y
			setSeqNo (0);	// @SQL=SELECT COALESCE(Max(SeqNo),0)+10 FROM AD_MigrationStep WHERE AD_Version_ID=@AD_Version_ID@
			setTimingType (TIMINGTYPE_BeforeData);	// B
			setType (TYPE_SQL);	// S
		}
	}	//	MMigrationStep

	/**
	 * 	Load Constructor
	 *	@param ctx ctx
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MMigrationStep(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MMigrationStep
	
	/**
	 * 	Execute Step
	 *	@return error message or null
	 */
	public String execute()
	{
		String type = getType();
		if (type.equals (TYPE_JavaLanguage))
			return executeJava();
		if (type.equals (TYPE_SQL))
			return executeSQL();
		return "Invalid Step: " + toString();
	}	//	execute

	/**
	 * 	Execute Java Step
	 *	@return error message or null
	 */
	private String executeJava()
	{
		String classMethodName = getClassname();
		if (classMethodName == null || classMethodName.length() == 0)
			return "No ClassName: " + toString();
		return "Not Implemented Yet (Java)";
	}	//	executeJava
	
	/**
	 * 	Execute SQL Step
	 *	@return error message or null
	 */
	private String executeSQL()
	{
		if (!isActive())
			return null;  //jz not execute inactive SQL
		String code = getCode();
		if (code == null || code.length() == 0)
			return "No Code: " + toString();
		//
		String error = null;
		StringTokenizer st = new StringTokenizer(code, ";");
		while (st.hasMoreTokens())
		{
			error = executeSQL(st.nextToken());
			if (error != null)
				break;
		}
		return error;		
	}	// executeSQL

	/**
	 * 	Execute SQL Command
	 * 	@param sql sql command
	 *	@return error message or null
	 */
	private String executeSQL(String sql)
	{
		String error = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			int no = pstmt.executeUpdate();
			log.finer ("#" + no + " - " + getName());
		}
		catch (Exception e)
		{
			error = e.getLocalizedMessage();
			if (error == null)
				error = e.toString();
			log.log (Level.WARNING, sql + " - " + error);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return error;
	}	//	executeSQL
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MMigrationStep[")
			.append (get_ID()).append ("-").append (getName())
			.append ("]");
		return sb.toString ();
	} //	toString
}	//	MMigrationStep
