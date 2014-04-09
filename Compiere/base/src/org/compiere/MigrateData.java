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
package org.compiere;

import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;

/**
 * 	Migrate Data
 *  @author Jorg Janke
 *  @version $Id: MigrateData.java,v 1.3 2006/07/30 00:51:06 jjanke Exp $
 */
public class MigrateData
{
	/**
	 * 	Migrate Data.
	 * 	Called from Worker.afterMigration
	 */
	public MigrateData ()
	{
		release252c();
		
		//	Update User Key
		afterMigrationUser(Env.getCtx());
		
		//	Update existing Print Format
		PrintFormatUtil pfu = new PrintFormatUtil (Env.getCtx());
		pfu.addMissingColumns();
	}	//	MigrateData
	
	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (MigrateData.class);
	
	/**
	 * 	Release 252c
	 */
	private void release252c()
	{
		String sql = "SELECT COUNT(*) FROM M_ProductDownload";
		int no = QueryUtil.getSQLValue(null, sql);
		if (no > 0)
		{
			log.finer("No Need - Downloads #" + no);
			return;
		}
		//
		log.info("Setting Product Downloads ...");
		int count = 0;
		sql = "SELECT AD_Client_ID, AD_Org_ID, M_Product_ID, Name, DownloadURL "
			+ "FROM M_Product "
			+ "WHERE DownloadURL IS NOT NULL";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				int AD_Client_ID = rs.getInt(1);
				int AD_Org_ID = rs.getInt(2);
				int M_Product_ID = rs.getInt(3);
				String Name = rs.getString(4);
				String DownloadURL = rs.getString(5);
				//
				Ctx ctx = new Ctx(Env.getCtx());
				ctx.setAD_Client_ID(AD_Client_ID);
				ctx.setAD_Org_ID(AD_Org_ID);
				MProductDownload pdl = new MProductDownload(ctx, 0, null);
				pdl.setM_Product_ID(M_Product_ID);
				pdl.setName(Name);
				pdl.setDownloadURL(DownloadURL);
				if (pdl.save())
				{
					count++;
					String sqlUpdate = "UPDATE M_Product SET DownloadURL = NULL WHERE M_Product_ID= ? " ;
					int updated = DB.executeUpdate((Trx) null, sqlUpdate,M_Product_ID);
					if (updated != 1)
						log.warning("Product not updated");
				}
				else
					log.warning("Product Download not created M_Product_ID=" + M_Product_ID);
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		log.info("#" + count);
	}	//	release252c
	
	
	/**
	 * 	After Migration - Set value for AD_User.Value
	 */
	private void afterMigrationUser(Ctx ctx)
	{
		log.info("Setting User Key ...");
		String sql = "SELECT * FROM AD_User WHERE Value IS NULL";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MUser user = new MUser(ctx, rs, null);
				user.setValue(user.getValue());
				user.save();
			}
		}
		catch (Exception e)
		{
			log.severe(e.getLocalizedMessage());
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	afterMigrationUser

	
	/**
	 * 	Migrate Data
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true, false, "MigrateData");
		new MigrateData();
	}	//	main
	
}	//	MigrateData
