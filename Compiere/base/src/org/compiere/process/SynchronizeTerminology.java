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
package org.compiere.process;

import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Synchronize Terminology Process
 *	@author Jorg Janke
 */
public class SynchronizeTerminology extends SvrProcess
{

	/**
	 * 	Prepare (NOP)
	 */
	@Override
	protected void prepare()
	{
	}	//	prepare

	boolean result = true;
	/**
	 * 	Process
	 * 	@return info
	 */
	@Override
	protected String doIt() throws Exception
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int steps = 0;

		//Adding missing Elements
		log.info("Adding missing Elements");
		try{
			sql = "SELECT DISTINCT ColumnName, Name, Description, Help, EntityType " +
			"FROM	AD_Column c " +
			"WHERE NOT EXISTS " +
			"	(SELECT * FROM AD_Element e " +
			"	WHERE UPPER(c.ColumnName)=UPPER(e.ColumnName))";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			PreparedStatement pstmt1 = null;
			while (rs.next()){
				try{
					if (pstmt1 == null){
						sql = "INSERT INTO AD_Element"
							+ " (AD_Element_ID, AD_Client_ID, AD_Org_ID,"
							+ " IsActive, Created, CreatedBy, Updated, UpdatedBy,"
							+ " ColumnName, Name, PrintName, Description, Help, EntityType)"
							+ " VALUES"
							+ " (?, 0, 0,"  //1, NextNo
							+ " 'Y', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0,"
							+ " ?, ?, ?, ?, ?, ?)"; //2-7 CC.ColumnName, CC.Name, CC.Name, CC.Description, CC.Help, CC.EntityType
						pstmt1 = DB.prepareStatement(sql, get_TrxName());
					}

					int id = DB.getNextID(getAD_Client_ID(), "AD_Element", get_TrxName());
					if (id <= 0){
						log.severe("Steps " + steps + ", No NextID (" + id + ")");
						return "Steps " + steps + " No NextID for AD_Element";
					}
					pstmt1.setInt(1, id);
					pstmt1.setString(2, rs.getString(1));
					pstmt1.setString(3, rs.getString(2));
					pstmt1.setString(4, rs.getString(2));
					pstmt1.setString(5, rs.getString(3));
					pstmt1.setString(6, rs.getString(4));
					pstmt1.setString(7, rs.getString(5));
					pstmt1.executeUpdate();
				} catch (Exception e) {
					log.log(Level.SEVERE, "Step " + steps + ": " + sql, e);
					return "Steps " + steps + " " + e.getMessage();
				} finally {
					DB.closeStatement(pstmt1);
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Step " + steps + ": " + sql, e);
			result = false;
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		steps++;
		PreparedStatement pstmt1 = null;
		try {
			sql = "SELECT DISTINCT ColumnName, Name, Description, Help, EntityType " +
			"FROM	AD_Process_Para p " +
			"WHERE NOT EXISTS " +
			"	(SELECT * FROM AD_Element e " +
			"	WHERE UPPER(p.ColumnName)=UPPER(e.ColumnName))";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				try {
					if (pstmt1 == null) {
						sql = "INSERT INTO AD_Element"
							+ " (AD_Element_ID, AD_Client_ID, AD_Org_ID,"
							+ " IsActive, Created, CreatedBy, Updated, UpdatedBy,"
							+ " ColumnName, Name, PrintName, Description, Help, EntityType)"
							+ " VALUES"
							+ " (?, 0, 0,"  //1, NextNo
							+ " 'Y', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0,"
							+ " ?, ?, ?, ?, ?, ?)"; //2-7 CC.ColumnName, CC.Name, CC.Name, CC.Description, CC.Help, CC.EntityType
						pstmt1 = DB.prepareStatement(sql, get_TrxName());
					}
					int id = DB.getNextID(getAD_Client_ID(), "AD_Element", get_TrxName());
					if (id <= 0) {
						log.severe("Steps " + steps + ", No NextID (" + id + ")");
						return "Steps " + steps + " No NextID for AD_Element";
					}
					pstmt1.setInt(1, id);
					pstmt1.setString(2, rs.getString(1));
					pstmt1.setString(3, rs.getString(2));
					pstmt1.setString(4, rs.getString(2));
					pstmt1.setString(5, rs.getString(3));
					pstmt1.setString(6, rs.getString(4));
					pstmt1.setString(7, rs.getString(5));
					pstmt1.executeUpdate();
				} catch (Exception e) {
					log.log(Level.SEVERE, "Step " + steps + ": " + sql, e);
					return "Steps " + steps + " " + e.getMessage();
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Step " + steps + ": " + sql, e);
			result = false;
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeStatement(pstmt1);
		}

		sql = "INSERT INTO AD_Element_Trl (AD_Element_ID, AD_Language, AD_Client_ID, AD_Org_ID,"
			+ "IsActive, Created, CreatedBy, Updated, UpdatedBy,"
			+ " Name, PrintName, Description, Help, IsTranslated)"
			+ " SELECT m.AD_Element_ID, l.AD_Language, m.AD_Client_ID, m.AD_Org_ID,"
			+ " m.IsActive, m.Created, m.CreatedBy, m.Updated, m.UpdatedBy,"
			+ " m.Name, m.PrintName, m.Description, m.Help, 'N'"
			+ " FROM	AD_Element m, AD_Language l"
			+ " WHERE	l.IsActive = 'Y' AND l.IsSystemLanguage = 'Y'"
			+ " AND	AD_Element_ID || AD_Language NOT IN"
			+ " (SELECT AD_Element_ID || AD_Language FROM AD_Element_Trl)";
		execute("Adding missing Element Translations", sql,  "  rows added: ");

		sql = "INSERT INTO AD_ElementCTX_Trl (AD_ElementCTX_ID, AD_Language, AD_Client_ID, AD_Org_ID,"
			+ "IsActive, Created, CreatedBy, Updated, UpdatedBy,"
			+ " Name, PrintName, Description, Help, IsTranslated)"
			+ " SELECT m.AD_ElementCTX_ID, l.AD_Language, m.AD_Client_ID, m.AD_Org_ID,"
			+ " m.IsActive, m.Created, m.CreatedBy, m.Updated, m.UpdatedBy,"
			+ " m.Name, m.PrintName, m.Description, m.Help, 'N'"
			+ " FROM	AD_ElementCTX m, AD_Language l"
			+ " WHERE	l.IsActive = 'Y' AND l.IsSystemLanguage = 'Y'"
			+ " AND	AD_ElementCTX_ID || AD_Language NOT IN"
			+ " (SELECT AD_ElementCTX_ID || AD_Language FROM AD_ElementCTX_Trl)";
		execute("Adding missing context specific Element Translations", sql,  "  rows added: ");

		sql = "UPDATE AD_Column c"
			+ " SET AD_Element_id ="
			+ " (SELECT AD_Element_ID FROM AD_Element e"
			+ " WHERE UPPER(c.ColumnName)=UPPER(e.ColumnName))"
			+ " WHERE AD_Element_ID IS NULL";
		execute("Creating link from Element to Column", sql,  "  rows updated: ");

		sql = "DELETE	AD_Element_Trl"
			+ " WHERE	AD_Element_ID IN"
			+ " (SELECT AD_Element_ID FROM AD_Element e"
			+ " WHERE NOT EXISTS"
			+ " (SELECT * FROM AD_Column c WHERE UPPER(e.ColumnName)=UPPER(c.ColumnName))"
			+ " AND NOT EXISTS"
			+ " (SELECT * FROM AD_InfoColumn c WHERE e.AD_Element_ID=c.AD_Element_ID)"
			+ " AND NOT EXISTS"
			+ " (SELECT * FROM AD_Process_Para p WHERE UPPER(e.ColumnName)=UPPER(p.ColumnName)))";
		//not do delete for now
		//executesql("Deleting unused Elements-TRL", sql,  "  rows deleted: ");

		sql = "DELETE	AD_Element"
			+ " WHERE NOT EXISTS"
			+ " (SELECT * FROM AD_Column c WHERE UPPER(e.ColumnName)=UPPER(c.ColumnName))"
			+ " AND NOT EXISTS"
			+ " (SELECT * FROM AD_InfoColumn c WHERE e.AD_Element_ID=c.AD_Element_ID)"
			+ " AND NOT EXISTS"
			+ " (SELECT * FROM AD_Process_Para p WHERE UPPER(e.ColumnName)=UPPER(p.ColumnName)))";
		//not do delete for now
		//executesql("Deleting unused Elements", sql,  "  rows deleted: ");

		sql = "UPDATE	AD_Column c"
			+ " SET	(ColumnName, Name, Description, Help, Updated) ="
			+ " (SELECT ColumnName, Name, Description, Help, CURRENT_TIMESTAMP"
			+ " FROM AD_Element e WHERE c.AD_Element_ID=e.AD_Element_ID)"
			+ " WHERE EXISTS (SELECT * FROM AD_Element e "
			+ "  WHERE c.AD_Element_ID=e.AD_Element_ID"
			+ "   AND (c.ColumnName <> e.ColumnName OR c.Name <> e.Name "
			+ "   OR NVL(c.Description,' ') <> NVL(e.Description,' ') OR NVL(c.Help,' ') <> NVL(e.Help,' ')))";
		execute("Synchronize Column", sql,  "  rows updated: ");

		sql = "UPDATE AD_Field f"
			+ " SET (Name, Description, Help, Updated) = "
			+ "             (SELECT e.Name, e.Description, e.Help, CURRENT_TIMESTAMP"
			+ "             FROM AD_Element e, AD_Column c"
			+ "     	    WHERE e.AD_Element_ID=c.AD_Element_ID AND c.AD_Column_ID=f.AD_Column_ID)"
			+ " 	WHERE f.IsCentrallyMaintained='Y' AND f.IsActive='Y'"
			+ " 	  AND EXISTS (SELECT * FROM AD_Element e, AD_Column c"
			+ " 				WHERE f.AD_Column_ID=c.AD_Column_ID"
			+ " 				  AND c.AD_Element_ID=e.AD_Element_ID AND c.AD_Process_ID IS NULL"
			+ " 				  AND (f.Name <> e.Name OR NVL(f.Description,' ') <> NVL(e.Description,' ') OR NVL(f.Help,' ') <> NVL(e.Help,' ')))"
			+ "AND NOT EXISTS ("
			+ "      SELECT *"
			+ "      FROM AD_Tab t, AD_Window w, AD_Column c, AD_ElementCTX e"
			+ "      WHERE t.AD_Tab_ID=f.AD_Tab_ID AND w.AD_Window_ID=t.AD_Window_ID"
			+ "      AND c.AD_Column_ID=f.AD_Column_ID AND e.AD_Element_ID=c.AD_Element_ID"
			+ "      AND e.AD_CTXArea_ID=COALESCE(t.AD_CTXArea_ID, w.AD_CTXArea_ID))";
		execute("Synchronize Field", sql,  "  rows updated: ");

		sql = "	UPDATE AD_Field_trl trl"
			+ " SET Name = (SELECT e.Name FROM AD_Element_trl e, AD_Column c, AD_Field f"
			+ " 				WHERE e.AD_Language=trl.AD_Language AND e.AD_Element_ID=c.AD_Element_ID"
			+ " 				  AND c.AD_Column_ID=f.AD_Column_ID AND f.AD_Field_ID=trl.AD_Field_ID),"
			+ " 		Description = (SELECT e.Description FROM AD_Element_trl e, AD_Column c, AD_Field f"
			+ " 				WHERE e.AD_Language=trl.AD_Language AND e.AD_Element_ID=c.AD_Element_ID"
			+ " 				  AND c.AD_Column_ID=f.AD_Column_ID AND f.AD_Field_ID=trl.AD_Field_ID),"
			+ " 		Help = (SELECT e.Help FROM AD_Element_trl e, AD_Column c, AD_Field f"
			+ " 				WHERE e.AD_Language=trl.AD_Language AND e.AD_Element_ID=c.AD_Element_ID"
			+ " 				  AND c.AD_Column_ID=f.AD_Column_ID AND f.AD_Field_ID=trl.AD_Field_ID),"
			+ " 		IsTranslated = (SELECT e.IsTranslated FROM AD_Element_trl e, AD_Column c, AD_Field f"
			+ " 				WHERE e.AD_Language=trl.AD_Language AND e.AD_Element_ID=c.AD_Element_ID"
			+ " 				  AND c.AD_Column_ID=f.AD_Column_ID AND f.AD_Field_ID=trl.AD_Field_ID),"
			+ " 		Updated = CURRENT_TIMESTAMP"
			+ " 	WHERE EXISTS (SELECT * FROM AD_Field f, AD_Element_trl e, AD_Column c"
			+ " 				WHERE trl.AD_Field_ID=f.AD_Field_ID"
			+ " 				  AND f.AD_Column_ID=c.AD_Column_ID"
			+ " 				  AND c.AD_Element_ID=e.AD_Element_ID AND c.AD_Process_ID IS NULL"
			+ " 				  AND trl.AD_Language=e.AD_Language"
			+ " 				  AND f.IsCentrallyMaintained='Y' AND f.IsActive='Y'"
			+ " 				  AND (trl.Name <> e.Name OR NVL(trl.Description,' ') <> NVL(e.Description,' ') OR NVL(trl.Help,' ') <> NVL(e.Help,' ') OR NVL(trl.isTranslated,' ') <> NVL(e.isTranslated,' ')))"
			+ "AND NOT EXISTS ("
			+ "      SELECT *"
			+ "      FROM AD_Tab t, AD_Window w, AD_Column c, AD_ElementCTX e, AD_Field f"
			+ "      WHERE t.AD_Tab_ID=f.AD_Tab_ID AND w.AD_Window_ID=t.AD_Window_ID"
			+ "      AND c.AD_Column_ID=f.AD_Column_ID AND e.AD_Element_ID=c.AD_Element_ID"
			+ "      AND e.AD_CTXArea_ID=COALESCE(t.AD_CTXArea_ID, w.AD_CTXArea_ID)"
			+ "      AND f.AD_Field_ID = trl.AD_Field_ID)";
		execute("Synchronize Field Translations", sql,  "  rows updated: ");

		sql = "	UPDATE AD_Field f"
			+ " SET Name = (SELECT e.Name FROM AD_ElementCTX e JOIN AD_Column c ON (e.AD_Element_ID=c.AD_Element_ID)"
			+ " 			WHERE c.AD_Column_ID=f.AD_Column_ID"
			+ "   				AND EXISTS (SELECT * FROM AD_Tab t JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)"
			+ " 					WHERE f.AD_Tab_ID=t.AD_Tab_ID"
			+ " 			  		AND (w.AD_CtxArea_ID IS NOT NULL AND t.AD_CtxArea_ID IS NULL AND e.AD_CtxArea_ID=w.AD_CtxArea_ID"
			+ " 			  			OR t.AD_CtxArea_ID IS NOT NULL AND e.AD_CtxArea_ID=t.AD_CtxArea_ID))),"
			+ " 	Description = (SELECT e.Description FROM AD_ElementCTX e JOIN AD_Column c ON (e.AD_Element_ID=c.AD_Element_ID)"
			+ " 			WHERE c.AD_Column_ID=f.AD_Column_ID"
			+ "   				AND EXISTS (SELECT * FROM AD_Tab t JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)"
			+ " 					WHERE f.AD_Tab_ID=t.AD_Tab_ID"
			+ " 			  		AND (w.AD_CtxArea_ID IS NOT NULL AND t.AD_CtxArea_ID IS NULL AND e.AD_CtxArea_ID=w.AD_CtxArea_ID"
			+ " 			  			OR t.AD_CtxArea_ID IS NOT NULL AND e.AD_CtxArea_ID=t.AD_CtxArea_ID))),"
			+ " 	Help = (SELECT e.Help FROM AD_ElementCTX e JOIN AD_Column c ON (e.AD_Element_ID=c.AD_Element_ID)"
			+ " 			WHERE c.AD_Column_ID=f.AD_Column_ID"
			+ "   				AND EXISTS (SELECT * FROM AD_Tab t JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)"
			+ " 					WHERE f.AD_Tab_ID=t.AD_Tab_ID"
			+ " 			  		AND (w.AD_CtxArea_ID IS NOT NULL AND t.AD_CtxArea_ID IS NULL AND e.AD_CtxArea_ID=w.AD_CtxArea_ID"
			+ " 			  			OR t.AD_CtxArea_ID IS NOT NULL AND e.AD_CtxArea_ID=t.AD_CtxArea_ID))),"
			+ " 	Updated = CURRENT_TIMESTAMP"
			+ " WHERE f.IsCentrallyMaintained='Y' AND f.IsActive='Y'"
			+ "   AND EXISTS (SELECT * FROM AD_ElementCTX e, AD_Column c, AD_Tab t, AD_Window w"
			+ " 			WHERE f.AD_Column_ID=c.AD_Column_ID"
			+ " 			  AND c.AD_Element_ID=e.AD_Element_ID AND c.AD_Process_ID IS NULL"
			+ " 			  AND (f.Name <> e.Name OR NVL(f.Description,' ') <> NVL(e.Description,' ') OR NVL(f.Help,' ') <> NVL(e.Help,' '))"
			+ " 			  AND e.Name IS NOT NULL"
			+ "               AND t.AD_Tab_ID=f.AD_Tab_ID AND w.AD_Window_ID=t.AD_Window_ID"
			+ "		          AND e.AD_CtxArea_ID = COALESCE(t.AD_CtxArea_ID,w.AD_CtxArea_ID))";
		execute("Synchronize Fields with ElementCTX", sql,  "  rows updated: ");

		sql = "	UPDATE AD_Field_trl trl"
			+ " SET Name = (SELECT et.Name FROM AD_ElementCTX_trl et "
			+ " 				JOIN AD_ElementCTX e ON (et.AD_ElementCTX_ID=e.AD_ElementCTX_ID)"
			+ "					JOIN AD_Column c ON (e.AD_Element_ID=c.AD_Element_ID) "
			+ "					JOIN AD_Field f ON (c.AD_Column_ID=f.AD_Column_ID)"
			+ " 			WHERE et.AD_Language=trl.AD_Language AND f.AD_Field_ID=trl.AD_Field_ID"
			+ "   				AND EXISTS (SELECT * FROM AD_Tab t JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)"
			+ " 					WHERE f.AD_Tab_ID=t.AD_Tab_ID"
			+ " 			  		AND (w.AD_CtxArea_ID IS NOT NULL AND t.AD_CtxArea_ID IS NULL AND e.AD_CtxArea_ID=w.AD_CtxArea_ID"
			+ " 			  			OR t.AD_CtxArea_ID IS NOT NULL AND e.AD_CtxArea_ID=t.AD_CtxArea_ID))),"
			+ " 	Description = (SELECT et.Description FROM AD_ElementCTX_trl et "
			+ " 				JOIN AD_ElementCTX e ON (et.AD_ElementCTX_ID=e.AD_ElementCTX_ID)"
			+ "					JOIN AD_Column c ON (e.AD_Element_ID=c.AD_Element_ID) "
			+ "					JOIN AD_Field f ON (c.AD_Column_ID=f.AD_Column_ID)"
			+ " 			WHERE et.AD_Language=trl.AD_Language AND f.AD_Field_ID=trl.AD_Field_ID"
			+ "   				AND EXISTS (SELECT * FROM AD_Tab t JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)"
			+ " 					WHERE f.AD_Tab_ID=t.AD_Tab_ID"
			+ " 			  		AND (w.AD_CtxArea_ID IS NOT NULL AND t.AD_CtxArea_ID IS NULL AND e.AD_CtxArea_ID=w.AD_CtxArea_ID"
			+ " 			  			OR t.AD_CtxArea_ID IS NOT NULL AND e.AD_CtxArea_ID=t.AD_CtxArea_ID))),"
			+ " 	Help = (SELECT et.Help FROM AD_ElementCTX_trl et "
			+ " 				JOIN AD_ElementCTX e ON (et.AD_ElementCTX_ID=e.AD_ElementCTX_ID)"
			+ "					JOIN AD_Column c ON (e.AD_Element_ID=c.AD_Element_ID) "
			+ "					JOIN AD_Field f ON (c.AD_Column_ID=f.AD_Column_ID)"
			+ " 			WHERE et.AD_Language=trl.AD_Language AND f.AD_Field_ID=trl.AD_Field_ID"
			+ "   				AND EXISTS (SELECT * FROM AD_Tab t JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)"
			+ " 					WHERE f.AD_Tab_ID=t.AD_Tab_ID"
			+ " 			  		AND (w.AD_CtxArea_ID IS NOT NULL AND t.AD_CtxArea_ID IS NULL AND e.AD_CtxArea_ID=w.AD_CtxArea_ID"
			+ " 			  			OR t.AD_CtxArea_ID IS NOT NULL AND e.AD_CtxArea_ID=t.AD_CtxArea_ID))),"
			+ " 	IsTranslated = (SELECT et.IsTranslated FROM AD_ElementCTX_trl et "
			+ " 				JOIN AD_ElementCTX e ON (et.AD_ElementCTX_ID=e.AD_ElementCTX_ID)"
			+ "					JOIN AD_Column c ON (e.AD_Element_ID=c.AD_Element_ID) "
			+ "					JOIN AD_Field f ON (c.AD_Column_ID=f.AD_Column_ID)"
			+ " 			WHERE et.AD_Language=trl.AD_Language AND f.AD_Field_ID=trl.AD_Field_ID"
			+ "   				AND EXISTS (SELECT * FROM AD_Tab t JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)"
			+ " 					WHERE f.AD_Tab_ID=t.AD_Tab_ID"
			+ " 			  		AND (w.AD_CtxArea_ID IS NOT NULL AND t.AD_CtxArea_ID IS NULL AND e.AD_CtxArea_ID=w.AD_CtxArea_ID"
			+ " 			  			OR t.AD_CtxArea_ID IS NOT NULL AND e.AD_CtxArea_ID=t.AD_CtxArea_ID))),"
			+ " 	Updated = CURRENT_TIMESTAMP"
			+ " 	WHERE EXISTS (SELECT * FROM AD_Field f, AD_ElementCTX_trl et, AD_ElementCTX e,  AD_Column c, AD_Tab t, AD_Window w"
			+ " 				WHERE trl.AD_Field_ID=f.AD_Field_ID"
			+ " 				  AND f.AD_Column_ID=c.AD_Column_ID"
			+ " 				  AND c.AD_Element_ID=e.AD_Element_ID AND et.AD_ElementCTX_ID=e.AD_ElementCTX_ID AND c.AD_Process_ID IS NULL"
			+ " 				  AND trl.AD_Language=et.AD_Language"
			+ " 				  AND f.IsCentrallyMaintained='Y' AND f.IsActive='Y'"
			+ " 				  AND (trl.Name <> et.Name OR NVL(trl.Description,' ') <> NVL(et.Description,' ') OR NVL(trl.Help,' ') <> NVL(et.Help,' ') OR NVL(trl.isTranslated,' ') <> NVL(et.isTranslated,' '))"
			+ " 				  AND et.Name IS NOT NULL"
			+ "                   AND t.AD_Tab_ID=f.AD_Tab_ID AND w.AD_Window_ID=t.AD_Window_ID"
			+ "                   AND e.AD_CtxArea_ID = COALESCE(t.AD_CtxArea_ID,w.AD_CtxArea_ID))";
		execute("Synchronize fields with context specific element translations", sql,  "  rows updated: ");

		sql = "	UPDATE AD_Field f"
			+ " SET Name = (SELECT p.Name FROM AD_Process p, AD_Column c WHERE p.AD_Process_ID=c.AD_Process_ID"
			+ " 			AND c.AD_Column_ID=f.AD_Column_ID),"
			+ " 	Description = (SELECT p.Description FROM AD_Process p, AD_Column c WHERE p.AD_Process_ID=c.AD_Process_ID"
			+ " 			AND c.AD_Column_ID=f.AD_Column_ID),"
			+ " 	Help = (SELECT p.Help FROM AD_Process p, AD_Column c WHERE p.AD_Process_ID=c.AD_Process_ID"
			+ " 			AND c.AD_Column_ID=f.AD_Column_ID),"
			+ " 	Updated = CURRENT_TIMESTAMP"
			+ " WHERE f.IsCentrallyMaintained='Y' AND f.IsActive='Y' "
			+ "   AND EXISTS (SELECT * FROM AD_Process p, AD_Column c"
			+ " 			WHERE c.AD_Process_ID=p.AD_Process_ID AND f.AD_Column_ID=c.AD_Column_ID"
			+ " 			AND (f.Name<>p.Name OR NVL(f.Description,' ')<>NVL(p.Description,' ') OR NVL(f.Help,' ')<>NVL(p.Help,' ')))";
		execute("Synchronize Field from Process", sql,  "  rows updated: ");

		sql = "UPDATE AD_Field_trl trl"
			+ " SET Name = (SELECT p.Name FROM AD_Process_trl p, AD_Column c, AD_Field f"
			+ " 			WHERE p.AD_Process_ID=c.AD_Process_ID AND c.AD_Column_ID=f.AD_Column_ID"
			+ " 			AND f.AD_Field_ID=trl.AD_Field_ID AND p.AD_Language=trl.AD_Language),"
			+ " 	Description = (SELECT p.Description FROM AD_Process_trl p, AD_Column c, AD_Field f "
			+ " 			WHERE p.AD_Process_ID=c.AD_Process_ID AND c.AD_Column_ID=f.AD_Column_ID"
			+ " 			AND f.AD_Field_ID=trl.AD_Field_ID AND p.AD_Language=trl.AD_Language),"
			+ " 	Help = (SELECT p.Help FROM AD_Process_trl p, AD_Column c, AD_Field f "
			+ " 			WHERE p.AD_Process_ID=c.AD_Process_ID AND c.AD_Column_ID=f.AD_Column_ID"
			+ " 			AND f.AD_Field_ID=trl.AD_Field_ID AND p.AD_Language=trl.AD_Language),"
			+ " 	IsTranslated = (SELECT p.IsTranslated FROM AD_Process_trl p, AD_Column c, AD_Field f "
			+ " 			WHERE p.AD_Process_ID=c.AD_Process_ID AND c.AD_Column_ID=f.AD_Column_ID"
			+ " 			AND f.AD_Field_ID=trl.AD_Field_ID AND p.AD_Language=trl.AD_Language),"
			+ " 	Updated = CURRENT_TIMESTAMP"
			+ " WHERE EXISTS (SELECT * FROM AD_Process_Trl p, AD_Column c, AD_Field f"
			+ " 		WHERE c.AD_Process_ID=p.AD_Process_ID AND f.AD_Column_ID=c.AD_Column_ID"
			+ " 		AND f.AD_Field_ID=trl.AD_Field_ID AND p.AD_Language=trl.AD_Language"
			+ " 		AND f.IsCentrallyMaintained='Y' AND f.IsActive='Y'"
			+ " 		AND (trl.Name<>p.Name OR NVL(trl.Description,' ')<>NVL(p.Description,' ') OR NVL(trl.Help,' ')<>NVL(p.Help,' ') OR NVL(trl.isTranslated,' ') <> NVL(p.isTranslated,' ')))";
		execute("Synchronize Field Translations", sql,  "  rows updated: ");

		sql = "	UPDATE AD_Process_Para f" +
		" SET ColumnName = (SELECT e.ColumnName FROM AD_Element e" +
		" WHERE UPPER(e.ColumnName)=UPPER(f.ColumnName))" +
		" WHERE f.IsCentrallyMaintained='Y' AND f.IsActive='Y'" +
		" AND EXISTS (SELECT * FROM AD_Element e" +
		"  WHERE UPPER(e.ColumnName)=UPPER(f.ColumnName)" +
		"  AND e.ColumnName<>f.ColumnName)";
		execute("Synchronize Parameter ColumnName", sql,  "  rows updated: ");

		sql = "	UPDATE	AD_Process_Para p" +
		" SET	IsCentrallyMaintained = 'N'" +
		" WHERE	IsCentrallyMaintained <> 'N'" +
		" AND NOT EXISTS (SELECT * FROM AD_Element e WHERE p.ColumnName=e.ColumnName)";
		execute("Synchronize Paramenter Fields", sql,  "  rows updated: ");

		sql = "	UPDATE AD_Process_Para f" +
		" SET Name = (SELECT e.Name FROM AD_Element e" +
		" WHERE e.ColumnName=f.ColumnName)," +
		" Description = (SELECT e.Description FROM AD_Element e" +
		" WHERE e.ColumnName=f.ColumnName)," +
		" Help = (SELECT e.Help FROM AD_Element e" +
		" WHERE e.ColumnName=f.ColumnName)," +
		" Updated = CURRENT_TIMESTAMP" +
		" WHERE f.IsCentrallyMaintained='Y' AND f.IsActive='Y'" +
		"   AND EXISTS (SELECT * FROM AD_Element e" +
		" 	  WHERE e.ColumnName=f.ColumnName" +
		"     AND (f.Name <> e.Name OR NVL(f.Description,' ') <> NVL(e.Description,' ') OR NVL(f.Help,' ') <> NVL(e.Help,' ')))" +
		" AND NOT EXISTS (" +
		"   SELECT * FROM AD_Process p, AD_Element e, AD_ElementCTX ec" +
		"   WHERE p.AD_Process_ID=f.AD_Process_ID" +
		"   AND e.ColumnName=f.ColumnName" +
		"   AND ec.AD_Element_ID=e.AD_Element_ID" +
		"   AND ec.AD_CtxArea_ID=p.AD_CtxArea_ID)";
		execute("Synchronize Process Parameter with Element", sql,  "  rows updated: ");

		sql = "	UPDATE AD_Process_Para f" +
		" SET Name = (SELECT e.Name FROM AD_ElementCTX e " +
		"				JOIN AD_Element el ON (e.AD_Element_ID=el.AD_Element_ID)" +
		"				JOIN AD_Process p ON (p.AD_CtxArea_ID=e.AD_CtxArea_ID)" +
		"   			WHERE el.ColumnName=f.ColumnName AND " +
		"   	 			p.AD_Process_ID=f.AD_Process_ID)," +
		" Description = (SELECT e.Description FROM AD_ElementCTX e " +
		"				JOIN AD_Element el ON (e.AD_Element_ID=el.AD_Element_ID)" +
		"				JOIN AD_Process p ON (p.AD_CtxArea_ID=e.AD_CtxArea_ID)" +
		"   			WHERE el.ColumnName=f.ColumnName AND " +
		"   	 			p.AD_Process_ID=f.AD_Process_ID)," +
		" Help = (SELECT e.Help FROM AD_ElementCTX e " +
		"				JOIN AD_Element el ON (e.AD_Element_ID=el.AD_Element_ID)" +
		"				JOIN AD_Process p ON (p.AD_CtxArea_ID=e.AD_CtxArea_ID)" +
		"   			WHERE el.ColumnName=f.ColumnName AND " +
		"   	 			p.AD_Process_ID=f.AD_Process_ID)," +
		" Updated = CURRENT_TIMESTAMP" +
		" WHERE f.IsCentrallyMaintained='Y' AND f.IsActive='Y'" +
		"   AND EXISTS (SELECT * FROM AD_Process p, AD_Element e, AD_ElementCTX ec" +
		" 	  WHERE e.ColumnName=f.ColumnName" +
		"     AND (f.Name <> e.Name OR NVL(f.Description,' ') <> NVL(e.Description,' ') OR NVL(f.Help,' ') <> NVL(e.Help,' '))" +
		"     AND p.AD_Process_ID=f.AD_Process_ID" +
		"     AND ec.AD_Element_ID=e.AD_Element_ID" +
		"     AND ec.AD_CtxArea_ID=p.AD_CtxArea_ID)";
		execute("Synchronize Process Parameter with ElementCTX", sql,  "  rows updated: ");

		sql = "	UPDATE AD_Process_Para_Trl trl" +
		" SET Name = (SELECT et.Name FROM AD_Element_Trl et, AD_Element e, AD_Process_Para f" +
		" 	  WHERE et.AD_Language=trl.AD_Language AND et.AD_Element_ID=e.AD_Element_ID" +
		"     AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID)," +
		"   Description = (SELECT et.Description FROM AD_Element_Trl et, AD_Element e, AD_Process_Para f" +
		" 	  WHERE et.AD_Language=trl.AD_Language AND et.AD_Element_ID=e.AD_Element_ID" +
		"     AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID)," +
		" 	Help = (SELECT et.Help FROM AD_Element_Trl et, AD_Element e, AD_Process_Para f " +
		" 	  WHERE et.AD_Language=trl.AD_Language AND et.AD_Element_ID=e.AD_Element_ID" +
		" 	  AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID)," +
		" 	IsTranslated = (SELECT et.IsTranslated FROM AD_Element_Trl et, AD_Element e, AD_Process_Para f" +
		" 	  WHERE et.AD_Language=trl.AD_Language AND et.AD_Element_ID=e.AD_Element_ID" +
		"     AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID)," +
		" 	Updated = CURRENT_TIMESTAMP" +
		" 	WHERE EXISTS (SELECT * FROM AD_Element_Trl et, AD_Element e, AD_Process_Para f" +
		" 	      WHERE et.AD_Language=trl.AD_Language AND et.AD_Element_ID=e.AD_Element_ID" +
		" 		  AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID" +
		" 		  AND f.IsCentrallyMaintained='Y' AND f.IsActive='Y'" +
		" 		  AND (trl.Name <> et.Name OR NVL(trl.Description,' ') <> NVL(et.Description,' ') OR NVL(trl.Help,' ') <> NVL(et.Help,' ') OR NVL(trl.isTranslated,' ') <> NVL(et.isTranslated,' ')))" +
		"  	AND NOT EXISTS (" +
		"     SELECT * " +
		"     FROM AD_Process_Para f, AD_Process p, AD_Element e, AD_ElementCtx ec" +
		"     WHERE f.AD_Process_Para_ID=trl.AD_Process_Para_ID" +
		"     AND p.AD_Process_ID=f.AD_Process_ID" +
		"     AND e.ColumnName=f.ColumnName" +
		"     AND ec.AD_Element_ID=e.AD_Element_ID" +
		"     AND ec.AD_CtxArea_ID=p.AD_CtxArea_ID)";
		execute("Synchronize Process Parameter Trl with Element Trl", sql,  "  rows updated: ");

		sql = "	UPDATE AD_Process_Para_Trl trl" +
		" SET Name = (SELECT et.Name FROM AD_ElementCTX_Trl et, AD_ElementCTX ec, AD_Element e, AD_Process_Para f, AD_Process p" +
		" WHERE et.AD_Language=trl.AD_Language AND ec.AD_Element_ID=e.AD_Element_ID AND et.AD_ElementCTX_ID=ec.AD_ElementCTX_ID" +
		" AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID " +
		"AND p.AD_CtxArea_ID=ec.AD_CtxArea_ID AND p.AD_Process_ID=f.AD_Process_ID)," +
		"   Description = (SELECT et.Description FROM AD_ElementCTX_Trl et, AD_ElementCTX ec, AD_Element e, AD_Process_Para f, AD_Process p" +
		" WHERE et.AD_Language=trl.AD_Language AND ec.AD_Element_ID=e.AD_Element_ID AND et.AD_ElementCTX_ID=ec.AD_ElementCTX_ID" +
		" AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID " +
		"AND p.AD_CtxArea_ID=ec.AD_CtxArea_ID AND p.AD_Process_ID=f.AD_Process_ID)," +
		" 	Help = (SELECT et.Help FROM AD_ElementCTX_Trl et, AD_ElementCTX ec, AD_Element e, AD_Process_Para f, AD_Process p" +
		" WHERE et.AD_Language=trl.AD_Language AND ec.AD_Element_ID=e.AD_Element_ID AND et.AD_ElementCTX_ID=ec.AD_ElementCTX_ID" +
		" AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID " +
		"AND p.AD_CtxArea_ID=ec.AD_CtxArea_ID AND p.AD_Process_ID=f.AD_Process_ID)," +
		" 	IsTranslated = (SELECT et.IsTranslated FROM AD_ElementCTX_Trl et, AD_ElementCTX ec, AD_Element e, AD_Process_Para f, AD_Process p" +
		" WHERE et.AD_Language=trl.AD_Language AND ec.AD_Element_ID=e.AD_Element_ID AND et.AD_ElementCTX_ID=ec.AD_ElementCTX_ID" +
		" AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID " +
		"AND p.AD_CtxArea_ID=ec.AD_CtxArea_ID AND p.AD_Process_ID=f.AD_Process_ID)," +
		" 	Updated = CURRENT_TIMESTAMP" +
		" 	WHERE EXISTS (SELECT * FROM AD_ElementCTX_Trl et, AD_ElementCTX ec, AD_Element e, AD_Process_Para f, AD_Process p" +
		" 	      WHERE et.AD_Language=trl.AD_Language AND ec.AD_Element_ID=e.AD_Element_ID" +
		"         AND et.AD_ElementCTX_ID=ec.AD_ElementCTX_ID" +
		" 		  AND e.ColumnName=f.ColumnName AND f.AD_Process_Para_ID=trl.AD_Process_Para_ID" +
		" 		  AND p.AD_Process_ID=f.AD_Process_ID AND trl.AD_Process_Para_ID=f.AD_Process_Para_ID" +
		" 		  AND p.AD_CtxArea_ID IS NOT NULL AND p.AD_CtxArea_ID=ec.AD_CtxArea_ID" +
		" 		  AND f.IsCentrallyMaintained='Y' AND f.IsActive='Y'" +
		" 		  AND (trl.Name <> et.Name OR NVL(trl.Description,' ') <> NVL(et.Description,' ') OR NVL(trl.Help,' ') <> NVL(et.Help,' ') OR NVL(trl.isTranslated,' ') <> NVL(et.isTranslated,' ')))";
		execute("Synchronize Process Parameter Trl with ElementCTX Trl", sql,  "  rows updated: ");

		sql = "	UPDATE AD_WF_Node n" +
		" SET Name = (SELECT w.Name FROM AD_Window w" +
		" 	WHERE w.AD_Window_ID=n.AD_Window_ID)," +
		" Description = (SELECT w.Description FROM AD_Window w" +
		" 	WHERE w.AD_Window_ID=n.AD_Window_ID)," +
		" Help = (SELECT w.Help FROM AD_Window w" +
		" 	WHERE w.AD_Window_ID=n.AD_Window_ID)" +
		" WHERE n.IsCentrallyMaintained = 'Y'" +
		" 	  AND EXISTS  (SELECT * FROM AD_Window w" +
		" 		WHERE w.AD_Window_ID=n.AD_Window_ID" +
		" 		  AND (w.Name <> n.Name OR NVL(w.Description,' ') <> NVL(n.Description,' ') OR NVL(w.Help,' ') <> NVL(n.Help,' ')))";
		execute("Synchronize Workflow Node from Window", sql,  "  rows updated: ");

		sql = "	UPDATE AD_WF_Node_Trl trl" +
		" SET Name = (SELECT t.Name FROM AD_Window_trl t, AD_WF_Node n" +
		" 	WHERE trl.AD_WF_Node_ID=n.AD_WF_Node_ID AND n.AD_Window_ID=t.AD_Window_ID" +
		" 		  AND trl.AD_Language=t.AD_Language)," +
		" Description = (SELECT t.Description FROM AD_Window_trl t, AD_WF_Node n" +
		" 	WHERE trl.AD_WF_Node_ID=n.AD_WF_Node_ID AND n.AD_Window_ID=t.AD_Window_ID" +
		" 		  AND trl.AD_Language=t.AD_Language)," +
		" Help = (SELECT t.Help FROM AD_Window_trl t, AD_WF_Node n" +
		" 	WHERE trl.AD_WF_Node_ID=n.AD_WF_Node_ID AND n.AD_Window_ID=t.AD_Window_ID" +
		" 		  AND trl.AD_Language=t.AD_Language)" +
		" WHERE EXISTS (SELECT * FROM AD_Window_Trl t, AD_WF_Node n" +
		" 	WHERE trl.AD_WF_Node_ID=n.AD_WF_Node_ID AND n.AD_Window_ID=t.AD_Window_ID" +
		" 		  AND trl.AD_Language=t.AD_Language AND n.IsCentrallyMaintained='Y' AND n.IsActive='Y'" +
		" 		  AND (trl.Name <> t.Name OR NVL(trl.Description,' ') <> NVL(t.Description,' ') OR NVL(trl.Help,' ') <> NVL(t.Help,' ')))";
		execute("Synchronize Workflow Node Trl from Window Trl", sql,  "  rows updated: ");

		sql = "	UPDATE AD_WF_Node n" +
		" SET (Name, Description, Help) = (SELECT f.Name, f.Description, f.Help " +
		"   FROM AD_Form f" +
		" 	WHERE f.AD_Form_ID=n.AD_Form_ID)" +
		" WHERE n.IsCentrallyMaintained = 'Y'" +
		"   AND EXISTS  (SELECT * FROM AD_Form f" +
		" 			WHERE f.AD_Form_ID=n.AD_Form_ID" +
		" 			  AND (f.Name <> n.Name OR NVL(f.Description,' ') <> NVL(n.Description,' ') OR NVL(f.Help,' ') <> NVL(n.Help,' ')))";
		execute("Synchronize Workflow Node from Form", sql,  "  rows updated: ");

		sql = "	UPDATE AD_WF_Node_Trl trl" +
		" SET (Name, Description, Help) = (SELECT t.Name, t.Description, t.Help" +
		"   FROM AD_Form_trl t, AD_WF_Node n" +
		" 	WHERE trl.AD_WF_Node_ID=n.AD_WF_Node_ID AND n.AD_Form_ID=t.AD_Form_ID" +
		" 	  AND trl.AD_Language=t.AD_Language)" +
		" WHERE EXISTS (SELECT * FROM AD_Form_Trl t, AD_WF_Node n" +
		" 	WHERE trl.AD_WF_Node_ID=n.AD_WF_Node_ID AND n.AD_Form_ID=t.AD_Form_ID" +
		" 	  AND trl.AD_Language=t.AD_Language AND n.IsCentrallyMaintained='Y' AND n.IsActive='Y'" +
		" 	  AND (trl.Name <> t.Name OR NVL(trl.Description,' ') <> NVL(t.Description,' ') OR NVL(trl.Help,' ') <> NVL(t.Help,' ')))";
		execute("Synchronize Workflow Node Trl from Form Trl", sql,  "  rows updated: ");

		sql = "	UPDATE AD_WF_Node n" +
		" SET (Name, Description, Help) = (SELECT f.Name, f.Description, f.Help" +
		" 	FROM AD_Process f" +
		" 	WHERE f.AD_Process_ID=n.AD_Process_ID)" +
		" WHERE n.IsCentrallyMaintained = 'Y'" +
		"   AND EXISTS  (SELECT * FROM AD_Process f" +
		" 		WHERE f.AD_Process_ID=n.AD_Process_ID" +
		" 		  AND (f.Name <> n.Name OR NVL(f.Description,' ') <> NVL(n.Description,' ') OR NVL(f.Help,' ') <> NVL(n.Help,' ')))";
		execute("Synchronize Workflow Node from Process", sql,  "  rows updated: ");


		sql = "	UPDATE AD_WF_Node_Trl trl" +
		" SET (Name, Description, Help) = (SELECT t.Name, t.Description, t.Help" +
		" 		FROM AD_Process_trl t, AD_WF_Node n" +
		" 		WHERE trl.AD_WF_Node_ID=n.AD_WF_Node_ID AND n.AD_Process_ID=t.AD_Process_ID" +
		" 		  AND trl.AD_Language=t.AD_Language)" +
		" WHERE EXISTS (SELECT * FROM AD_Process_Trl t, AD_WF_Node n" +
		" 		WHERE trl.AD_WF_Node_ID=n.AD_WF_Node_ID AND n.AD_Process_ID=t.AD_Process_ID" +
		" 		  AND trl.AD_Language=t.AD_Language AND n.IsCentrallyMaintained='Y' AND n.IsActive='Y'" +
		" 		  AND (trl.Name <> t.Name OR NVL(trl.Description,' ') <> NVL(t.Description,' ') OR NVL(trl.Help,' ') <> NVL(t.Help,' ')))";
		execute("Synchronize Workflow Node Trl from Process Trl", sql,  "  rows updated: ");


		sql = "	UPDATE AD_PrintFormatItem pfi" +
		"	  SET Name = (SELECT e.Name" +
		"		FROM AD_Element e, AD_Column c" +
		"		WHERE e.AD_Element_ID=c.AD_Element_ID" +
		"		  AND c.AD_Column_ID=pfi.AD_Column_ID)" +
		"	WHERE pfi.IsCentrallyMaintained='Y'" +
		"      AND EXISTS (SELECT *" +
		"		FROM AD_Element e, AD_Column c" +
		"		WHERE e.AD_Element_ID=c.AD_Element_ID" +
		"		  AND c.AD_Column_ID=pfi.AD_Column_ID" +
		"		  AND e.Name<>pfi.Name)" +
		"	  AND EXISTS (SELECT * FROM AD_Client" +
		"		WHERE AD_Client_ID=pfi.AD_Client_ID)"; // AND IsMultiLingualDocument='Y')";
		execute("Synchronize PrintFormatItem Name from Element", sql,  "  rows updated: ");


		sql = "	UPDATE AD_PrintFormatItem pfi "
			+ "SET PrintName = (SELECT e.PrintName "
			+ "FROM AD_Element e, AD_Column c "
			+ "WHERE e.AD_Element_ID=c.AD_Element_ID"
			+ " AND c.AD_Column_ID=pfi.AD_Column_ID) "
			+ "WHERE pfi.IsCentrallyMaintained='Y'"
			+ " AND EXISTS (SELECT * "
			+ "FROM AD_Element e, AD_Column c, AD_PrintFormat pf "
			+ "WHERE e.AD_Element_ID=c.AD_Element_ID"
			+ " AND c.AD_Column_ID=pfi.AD_Column_ID"
			+ " AND LENGTH(pfi.PrintName) > 0"
			+ " AND e.PrintName<>pfi.PrintName"
			+ " AND pf.AD_PrintFormat_ID=pfi.AD_PrintFormat_ID"
			+ " AND pf.IsForm='N' AND IsTableBased='Y')"
			+ " AND EXISTS (SELECT * FROM AD_Client"
			+ "	WHERE AD_Client_ID=pfi.AD_Client_ID AND (IsMultiLingualDocument='Y' OR AD_Client_ID IN (0,11)))";
		execute("Synchronize PrintFormatItem PrintName from Element", sql,  "  rows updated: ");


		sql = "	UPDATE AD_PrintFormatItem_Trl trl" +
		"	  SET PrintName = (SELECT e.PrintName" +
		"		FROM AD_Element_Trl e, AD_Column c, AD_PrintFormatItem pfi" +
		"		WHERE e.AD_Language=trl.AD_Language" +
		"		  AND e.AD_Element_ID=c.AD_Element_ID" +
		"		  AND c.AD_Column_ID=pfi.AD_Column_ID" +
		"		  AND pfi.AD_PrintFormatItem_ID=trl.AD_PrintFormatItem_ID)" +
		"	WHERE EXISTS (SELECT *" +
		"		FROM AD_Element_Trl e, AD_Column c, AD_PrintFormatItem pfi, AD_PrintFormat pf" +
		"		WHERE e.AD_Language=trl.AD_Language" +
		"		  AND e.AD_Element_ID=c.AD_Element_ID" +
		"		  AND c.AD_Column_ID=pfi.AD_Column_ID" +
		"		  AND pfi.AD_PrintFormatItem_ID=trl.AD_PrintFormatItem_ID" +
		"          AND pfi.IsCentrallyMaintained='Y'" +
		"		  AND LENGTH(pfi.PrintName) > 0" +
		"		  AND (e.PrintName<>trl.PrintName OR trl.PrintName IS NULL)" +
		"		  AND pf.AD_PrintFormat_ID=pfi.AD_PrintFormat_ID" +
		"		  AND pf.IsForm='N' AND IsTableBased='Y')" +
		"	  AND EXISTS (SELECT * FROM AD_Client" +
		"		WHERE AD_Client_ID=trl.AD_Client_ID AND IsMultiLingualDocument='Y')";
		execute("Synchronize PrintFormatItem Trl from Element Trl (Multi-Lingual)", sql,  "  rows updated: ");


		sql = "	UPDATE AD_PrintFormatItem_Trl trl" +
		"	  SET PrintName = (SELECT pfi.PrintName" +
		"		FROM AD_PrintFormatItem pfi" +
		"		WHERE pfi.AD_PrintFormatItem_ID=trl.AD_PrintFormatItem_ID)" +
		"	WHERE EXISTS (SELECT *" +
		"		FROM AD_PrintFormatItem pfi, AD_PrintFormat pf" +
		"		WHERE pfi.AD_PrintFormatItem_ID=trl.AD_PrintFormatItem_ID" +
		"          AND pfi.IsCentrallyMaintained='Y'" +
		"		  AND LENGTH(pfi.PrintName) > 0" +
		"		  AND pfi.PrintName<>trl.PrintName" +
		"		  AND pf.AD_PrintFormat_ID=pfi.AD_PrintFormat_ID" +
		"		  AND pf.IsForm='N' AND pf.IsTableBased='Y')" +
		"	  AND EXISTS (SELECT * FROM AD_Client" +
		"		WHERE AD_Client_ID=trl.AD_Client_ID AND IsMultiLingualDocument='N')";
		execute("Synchronize PrintFormatItem Trl (Not Multi-Lingual)", sql,  "  rows updated: ");


		sql = "	UPDATE AD_PrintFormatItem_Trl trl" +
		"	  SET PrintName = NULL" +
		"	WHERE PrintName IS NOT NULL" +
		"	  AND EXISTS (SELECT *" +
		"		FROM AD_PrintFormatItem pfi" +
		"		WHERE pfi.AD_PrintFormatItem_ID=trl.AD_PrintFormatItem_ID" +
		"          AND pfi.IsCentrallyMaintained='Y'" +
		"		  AND (LENGTH (pfi.PrintName) = 0 OR pfi.PrintName IS NULL))";
		execute("Synchronize PrintFormatItem Trl where not used in base table", sql,  "  rows updated: ");


		sql = "UPDATE AD_Menu m "
			+ "SET Name = (SELECT Name FROM AD_Window w WHERE m.AD_Window_ID=w.AD_Window_ID),"
			+ "Description = (SELECT Description FROM AD_Window w WHERE m.AD_Window_ID=w.AD_Window_ID) "
			+ "WHERE AD_Window_ID IS NOT NULL"
			+ " AND Action = 'W'";
		execute("Synchronize Menu with Window", sql,  "  rows updated: ");

		sql = "	UPDATE	AD_Menu_Trl mt" +
		"	SET		Name = (SELECT wt.Name FROM AD_Window_Trl wt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Window_ID=wt.AD_Window_ID" +
		"					AND mt.AD_Language=wt.AD_Language)," +
		"		Description = (SELECT wt.Description FROM AD_Window_Trl wt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Window_ID=wt.AD_Window_ID" +
		"					AND mt.AD_Language=wt.AD_Language)," +
		"			IsTranslated = (SELECT wt.IsTranslated FROM AD_Window_Trl wt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Window_ID=wt.AD_Window_ID" +
		"					AND mt.AD_Language=wt.AD_Language)" +
		"	WHERE EXISTS (SELECT * FROM AD_Window_Trl wt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Window_ID=wt.AD_Window_ID" +
		"					AND mt.AD_Language=wt.AD_Language" +
		"					AND m.AD_Window_ID IS NOT NULL" +
		"					AND m.Action = 'W')";
		execute("Synchronize Menu with Window Trl", sql,  "  rows updated: ");

		sql = "	UPDATE	AD_Menu m" +
		"	SET		Name = (SELECT p.Name FROM AD_Process p WHERE m.AD_Process_ID=p.AD_Process_ID)," +
		"			Description = (SELECT p.Description FROM AD_Process p WHERE m.AD_Process_ID=p.AD_Process_ID)" +
		"	WHERE	m.AD_Process_ID IS NOT NULL" +
		"	  AND	m.Action IN ('R', 'P')";
		execute("Synchronize Menu with Processes", sql,  "  rows updated: ");

		sql = "	UPDATE	AD_Menu_Trl mt" +
		"	SET		Name = (SELECT pt.Name FROM AD_Process_Trl pt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Process_ID=pt.AD_Process_ID" +
		"					AND mt.AD_Language=pt.AD_Language)," +
		"			Description = (SELECT pt.Description FROM AD_Process_Trl pt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Process_ID=pt.AD_Process_ID" +
		"					AND mt.AD_Language=pt.AD_Language)," +
		"			IsTranslated = (SELECT pt.IsTranslated FROM AD_Process_Trl pt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Process_ID=pt.AD_Process_ID" +
		"					AND mt.AD_Language=pt.AD_Language)" +
		"	WHERE EXISTS (SELECT * FROM AD_Process_Trl pt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Process_ID=pt.AD_Process_ID" +
		"					AND mt.AD_Language=pt.AD_Language" +
		"					AND m.AD_Process_ID IS NOT NULL" +
		"					AND	Action IN ('R', 'P')" +
		"					AND (NVL(mt.Name,' ')<>NVL(pt.Name,' ') OR NVL(mt.Description,' ')<>NVL(pt.Description,' ') OR NVL(mt.isTranslated,' ')<>NVL(pt.isTranslated,' ')))";
		execute("Synchronize Menu with Processes Translations", sql,  "  rows updated: ");

		sql = "	UPDATE	AD_Menu m" +
		"	SET		Name = (SELECT Name FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID)," +
		"			Description = (SELECT Description FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID)" +
		"	WHERE	AD_Form_ID IS NOT NULL" +
		"	  AND	Action = 'X'";
		execute("Synchronize Menu with Forms", sql,  "  rows updated: ");

		sql = "UPDATE	AD_Menu_Trl mt" +
		"	SET		Name = (SELECT ft.Name FROM AD_Form_Trl ft, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Form_ID=ft.AD_Form_ID" +
		"					AND mt.AD_Language=ft.AD_Language)," +
		"			Description = (SELECT ft.Description FROM AD_Form_Trl ft, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Form_ID=ft.AD_Form_ID" +
		"					AND mt.AD_Language=ft.AD_Language)," +
		"			IsTranslated = (SELECT ft.IsTranslated FROM AD_Form_Trl ft, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Form_ID=ft.AD_Form_ID" +
		"					AND mt.AD_Language=ft.AD_Language)" +
		"	WHERE EXISTS (SELECT * FROM AD_Form_Trl ft, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Form_ID=ft.AD_Form_ID" +
		"					AND mt.AD_Language=ft.AD_Language" +
		"					AND m.AD_Form_ID IS NOT NULL" +
		"					AND	Action = 'X'" +
		"					AND (NVL(mt.Name,' ')<>NVL(ft.Name,' ') OR NVL(mt.Description,' ')<>NVL(ft.Description,' ') OR NVL(mt.isTranslated,' ')<>NVL(ft.isTranslated,' ')))" ;
		execute("Synchronize Menu with Forms Trl", sql,  "  rows updated: ");

		sql = "	UPDATE	AD_Menu m" +
		"	SET		Name = (SELECT p.Name FROM AD_Workflow p WHERE m.AD_Workflow_ID=p.AD_Workflow_ID)," +
		"			Description = (SELECT p.Description FROM AD_Workflow p WHERE m.AD_Workflow_ID=p.AD_Workflow_ID)" +
		"	WHERE	m.AD_Workflow_ID IS NOT NULL" +
		"	  AND	m.Action = 'F'";
		execute("Synchronize Menu with Workflows", sql,  "  rows updated: ");

		sql = "	UPDATE	AD_Menu_Trl mt" +
		"	SET		Name = (SELECT pt.Name FROM AD_Workflow_Trl pt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Workflow_ID=pt.AD_Workflow_ID" +
		"					AND mt.AD_Language=pt.AD_Language)," +
		"			Description = (SELECT pt.Description FROM AD_Workflow_Trl pt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Workflow_ID=pt.AD_Workflow_ID" +
		"					AND mt.AD_Language=pt.AD_Language)," +
		"			IsTranslated = (SELECT pt.IsTranslated FROM AD_Workflow_Trl pt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Workflow_ID=pt.AD_Workflow_ID" +
		"					AND mt.AD_Language=pt.AD_Language)" +
		"	WHERE EXISTS (SELECT * FROM AD_Workflow_Trl pt, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Workflow_ID=pt.AD_Workflow_ID" +
		"					AND mt.AD_Language=pt.AD_Language" +
		"					AND m.AD_Workflow_ID IS NOT NULL" +
		"					AND	Action = 'F'" +
		"					AND (NVL(mt.Name,' ')<>NVL(pt.Name,' ') OR NVL(mt.Description,' ')<>NVL(pt.Description,' ') OR NVL(mt.isTranslated,' ')<>NVL(pt.isTranslated,' ')))" ;
		execute("Synchronize Menu with Workflows Trl", sql,  "  rows updated: ");

		sql = "	UPDATE	AD_Menu m" +
		"	SET		Name = (SELECT Name FROM AD_Task f WHERE m.AD_Task_ID=f.AD_Task_ID)," +
		"			Description = (SELECT Description FROM AD_Task f WHERE m.AD_Task_ID=f.AD_Task_ID)" +
		"	WHERE	AD_Task_ID IS NOT NULL" +
		"	  AND	Action = 'T'";
		execute("Synchronize Menu with Tasks", sql,  "  rows updated: ");

		sql = "	UPDATE	AD_Menu_Trl mt" +
		"	SET		Name = (SELECT ft.Name FROM AD_Task_Trl ft, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Task_ID=ft.AD_Task_ID" +
		"					AND mt.AD_Language=ft.AD_Language)," +
		"			Description = (SELECT ft.Description FROM AD_Task_Trl ft, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Task_ID=ft.AD_Task_ID" +
		"					AND mt.AD_Language=ft.AD_Language)," +
		"			IsTranslated = (SELECT ft.IsTranslated FROM AD_Task_Trl ft, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Task_ID=ft.AD_Task_ID" +
		"					AND mt.AD_Language=ft.AD_Language)" +
		"	WHERE EXISTS (SELECT * FROM AD_Task_Trl ft, AD_Menu m" +
		"					WHERE mt.AD_Menu_ID=m.AD_Menu_ID AND m.AD_Task_ID=ft.AD_Task_ID" +
		"					AND mt.AD_Language=ft.AD_Language" +
		"					AND m.AD_Task_ID IS NOT NULL" +
		"					AND	Action = 'T'" +
		"					AND (NVL(mt.Name,' ')<>NVL(ft.Name,' ') OR NVL(mt.Description,' ')<>NVL(ft.Description,' ') OR NVL(mt.isTranslated,' ')<>NVL(ft.isTranslated,' ')))" ;
		execute("Synchronize Menu with Tasks Trl", sql,  "  rows updated: ");

		sql = "    UPDATE AD_Column c" +
		"      SET (Name,Description,Help) =" +
		"        (SELECT e.Name,e.Description,e.Help" +
		"        FROM AD_Element e WHERE c.AD_Element_ID=e.AD_Element_ID)" +
		"    WHERE EXISTS" +
		"        (SELECT * FROM AD_Element e" +
		"        WHERE c.AD_Element_ID=e.AD_Element_ID" +
		"          AND c.Name<>e.Name)";
		execute("Synchronize Column with Element", sql,  "  rows updated: ");

		sql = "UPDATE AD_Column_Trl ct" +
		"    SET (Name, isTranslated) = (SELECT e.Name, e.isTranslated" +
		"        FROM AD_Column c INNER JOIN AD_Element_Trl e ON (c.AD_Element_ID=e.AD_Element_ID)" +
		"        WHERE ct.AD_Column_ID=c.AD_Column_ID AND ct.AD_Language=e.AD_Language)" +
		"    WHERE EXISTS" +
		"        (SELECT * FROM AD_Column c INNER JOIN AD_Element_Trl e ON (c.AD_Element_ID=e.AD_Element_ID)" +
		"        WHERE ct.AD_Column_ID=c.AD_Column_ID AND ct.AD_Language=e.AD_Language" +
		"          AND (ct.Name<>e.Name OR NVL(ct.isTranslated,' ')<>NVL(e.isTranslated,' ')))";
		execute("Synchronize Column with Element Trl", sql,  "  rows updated: ");

		sql = "    UPDATE AD_Table t" +
		"      SET (Name,Description) = (SELECT e.Name,e.Description FROM AD_Element e" +
		"        WHERE t.TableName||'_ID'=e.ColumnName)" +
		"    WHERE EXISTS (SELECT * FROM AD_Element e" +
		"        WHERE t.TableName||'_ID'=e.ColumnName" +
		"          AND t.Name<>e.Name)";
		execute("Synchronize Table with Element", sql,  "  rows updated: ");

		sql = "    UPDATE AD_Table_Trl tt" +
		"      SET (Name, isTranslated) = (SELECT e.Name, e.isTranslated" +
		"        FROM AD_Table t INNER JOIN AD_Element ex ON (t.TableName||'_ID'=ex.ColumnName)" +
		"          INNER JOIN AD_Element_Trl e ON (ex.AD_Element_ID=e.AD_Element_ID)" +
		"        WHERE tt.AD_Table_ID=t.AD_Table_ID AND tt.AD_Language=e.AD_Language)" +
		"    WHERE EXISTS (SELECT *" +
		"        FROM AD_Table t INNER JOIN AD_Element ex ON (t.TableName||'_ID'=ex.ColumnName)" +
		"          INNER JOIN AD_Element_Trl e ON (ex.AD_Element_ID=e.AD_Element_ID)" +
		"        WHERE tt.AD_Table_ID=t.AD_Table_ID AND tt.AD_Language=e.AD_Language" +
		"          AND (tt.Name<>e.Name OR NVL(tt.isTranslated,' ')<>NVL(e.isTranslated,' ')))";
		execute("Synchronize Table with Element Trl", sql,  "  rows updated: ");

		sql = "    UPDATE AD_Table t" +
		"     SET (Name,Description) = (SELECT e.Name||' Trl', e.Description" +
		"        FROM AD_Element e" +
		"        WHERE SUBSTR(t.TableName,1,LENGTH(t.TableName)-4)||'_ID'=e.ColumnName)" +
		"    WHERE TableName LIKE '%_Trl'" +
		"      AND EXISTS (SELECT * FROM AD_Element e" +
		"        WHERE SUBSTR(t.TableName,1,LENGTH(t.TableName)-4)||'_ID'=e.ColumnName" +
		"          AND t.Name<>e.Name)";
		execute("Synchronize Trl Table Name + Element", sql,  "  rows updated: ");

		sql = "    UPDATE AD_Table_Trl tt" +
		"      SET (Name,isTranslated) = (SELECT e.Name || ' **', e.isTranslated" +
		"        FROM AD_Table t INNER JOIN AD_Element ex ON (SUBSTR(t.TableName,1,LENGTH(t.TableName)-4)||'_ID'=ex.ColumnName)" +
		"          INNER JOIN AD_Element_Trl e ON (ex.AD_Element_ID=e.AD_Element_ID)" +
		"        WHERE tt.AD_Table_ID=t.AD_Table_ID AND tt.AD_Language=e.AD_Language)" +
		"    WHERE EXISTS (SELECT *" +
		"        FROM AD_Table t INNER JOIN AD_Element ex ON (SUBSTR(t.TableName,1,LENGTH(t.TableName)-4)||'_ID'=ex.ColumnName)" +
		"          INNER JOIN AD_Element_Trl e ON (ex.AD_Element_ID=e.AD_Element_ID)" +
		"        WHERE tt.AD_Table_ID=t.AD_Table_ID AND tt.AD_Language=e.AD_Language" +
		"          AND t.TableName LIKE '%_Trl'" +
		"          AND (tt.Name<>(e.Name || ' **') OR NVL(tt.isTranslated,' ')<>NVL(e.isTranslated,' ')))";
		execute("Synchronize AD_Table_Trl", sql,  "  rows updated: ");

		sql = "    UPDATE AD_InfoColumn ic" +
		"     SET (Name,Description,Help) =" +
		"          (SELECT e.Name,e.Description,e.Help" +
		"             FROM AD_Element e " +
		"            WHERE ic.AD_Element_ID=e.AD_Element_ID)" +
		"    WHERE EXISTS" +
		"          (SELECT * FROM AD_Element e" +
		"            WHERE ic.AD_Element_ID=e.AD_Element_ID" +
		" 		       AND (ic.Name<>e.Name OR NVL(ic.Description,' ')<>NVL(e.Description,' ') OR NVL(ic.Help,' ')<>NVL(e.Help,' ')))"+
		"      AND ic.IsCentrallyMaintained='Y' AND ic.IsActive='Y'";
		execute("Synchronize Info Column with Element", sql,  "  rows updated: ");

		sql = "UPDATE AD_InfoColumn_Trl ict" +
		"    SET (Name, Description, Help, isTranslated) =" + 
		"        (SELECT e.Name, e.Description, e.Help, isTranslated " +
		"           FROM AD_InfoColumn ic INNER JOIN AD_Element_Trl e ON (ic.AD_Element_ID=e.AD_Element_ID)" +
		"          WHERE ict.AD_InfoColumn_ID=ic.AD_InfoColumn_ID AND ict.AD_Language=e.AD_Language)" +
		"    WHERE EXISTS" +
		"        (SELECT * FROM AD_InfoColumn ic " +
		"          INNER JOIN AD_Element_Trl e ON (ic.AD_Element_ID=e.AD_Element_ID)" +
		"          WHERE ict.AD_InfoColumn_ID=ic.AD_InfoColumn_ID AND ict.AD_Language=e.AD_Language" +
		"            AND ic.IsCentrallyMaintained='Y' AND ic.IsActive='Y'" +
		"	         AND (ict.Name<>e.Name OR NVL(ict.Description,' ')<>NVL(e.Description,' ') OR NVL(ict.Help,' ')<>NVL(e.Help,' ') OR NVL(ict.isTranslated,' ')<>NVL(e.isTranslated,' ')))" ;
		execute("Synchronize Info Column with Element Trl", sql,  "  rows updated: ");

		if (result)
			return "Sucessful Synchronized";
		else
			return "Synchronized with error(s)";
	}	//	doIt

	private void execute(String msg, String sql, String pmsg)
	{
		PreparedStatement pstmt = null;
		log.info(msg);
		try {
			pstmt = DB.prepareStatement(sql, get_TrxName());
			int no = pstmt.executeUpdate();
			log.fine(pmsg + no);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
			result = false;
		} finally {
			DB.closeStatement(pstmt);
		}
	}

}	//	SynchronizeTerminology
