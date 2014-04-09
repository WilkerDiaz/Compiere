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
 *	Version Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MVersion.java 8756 2010-05-12 21:21:27Z nnayak $
 */
public class MVersion extends X_AD_Version
{
    /** Logger for class MVersion */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVersion.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Execute Migration
	 *	@param EntityType entity type
	 *	@param fromVersion from (can be null
	 *	@param toVersion to
	 *	@param timingType Migration Step timing type
	 *	@param DBType database type
	 *	@return Summary
	 */
	public static String executeMigration (Ctx ctx, String EntityType, 
		String fromVersion, String toVersion, String timingType, String DBType)
	{
		if (fromVersion == null)
			fromVersion = "";
		if (toVersion == null)
			return "NO VERSION TO";
		if (timingType == null)
			return "NO TIMING TYPE";
		//
		String sql = "SELECT * FROM AD_Version "
			+ "WHERE EntityType=? AND Value BETWEEN ? AND ? ORDER BY Value";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, EntityType);
			pstmt.setString (2, fromVersion);
			pstmt.setString (3, toVersion);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MVersion version = new MVersion (ctx, rs, null);
				version.execute (timingType, DBType);
			}
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return "";
	}	//	executeMigration
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MVersion.class);

	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Version_ID version
	 *	@param trx p_trx
	 */
	public MVersion(Ctx ctx, int AD_Version_ID, Trx trx)
	{
		super (ctx, AD_Version_ID, trx);
	}	//	MVersion

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MVersion(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MVersion
	
	/**	The Lines						*/
	private MMigrationStep[] m_steps = null;

	/**
	 * 	Get Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MMigrationStep[] getSteps(boolean reload)
	{
		if (m_steps != null && !reload)
			return m_steps;
		ArrayList<MMigrationStep> list = new ArrayList<MMigrationStep>();
		String sql = "SELECT * FROM AD_MigrationStep WHERE AD_Version_ID=? ORDER BY TimingType, SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getAD_Version_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMigrationStep (getCtx(), rs, get_Trx()));
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_steps = new MMigrationStep[list.size ()];
		list.toArray (m_steps);
		return m_steps;
	}	//	getSteps

	
	/**
	 * 	Execute Steps for TimingType
	 *	@param timingType migration step timing type
	 *	@return Summary
	 */
	public String execute (String timingType, String DBType)
	{
		int success = 0;
		int errors = 0;
		int failures = 0;
		MMigrationStep[] steps = getSteps(false);
		for (MMigrationStep step : steps) {
			if (!step.getTimingType().equals(timingType))
				continue;
			step.getDBType();
			//	DB Check comes here
			try
			{
				String error = step.execute();
				if (error == null)
					success++;
				else
					errors++;
			}
			catch (Exception e)
			{
				log.warning (e.toString());
				failures++;
			}
		}
		return "Success=" + success + ", Errors=" + errors + ", Failures=" + failures;
	}	//	execute
	
}	//	MVersion
