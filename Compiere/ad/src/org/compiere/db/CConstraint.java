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
package org.compiere.db;

import java.sql.*;

import org.compiere.util.*;

/**
 *	Processing additional tasks for constraints
 *
 * 	@author 	Jinglun Zhang
 */
public class CConstraint 
{
	/**
	 * 	Constructor.
	 *
	 */
	protected CConstraint ()
	{
	}	//	CConstraint

	
	/**
	 *	save constriant information for later to use
	 *
	 *  CREATE TABLE "COMPIERE"."AD_FOREIGNKEYINFO"  (
	 *  		  "CONSTRAINTNAME" VARCHAR(126) NOT NULL , 
	 *  		  "TABLENAME" VARCHAR(62) NOT NULL , 
	 *  		  "P_TABLENAME" VARCHAR(62) , 
	 *  		  "C_TYPE" CHAR(1) NOT NULL WITH DEFAULT 'F' , 
	 *  		  "C_STATEMENT" VARCHAR(2000) , 
	 *  		  "ORIGINALNAME" VARCHAR(128) )   
	 *  		 IN "USERSPACE1" ; 
	 *  
	 *  COMMENT ON COLUMN "COMPIERE"."AD_FOREIGNKEYINFO"."C_TYPE" IS 'F: FK, P:PK, T:Trigger';
	 *
	 *
	 * 	@param conn Connection
	 *  @param sql constraint sql
	 *  @param oriName Original constraint name, usually longer than 18 chars
	 *  @return -1: fail 
	 *
	 */
	
	//TODO jz: save create trigger??
	public static int save(Connection conn, String sql, String oriName)
	{
		if (conn == null || sql == null)
			return -1;
		
		StringBuffer sb = new StringBuffer("INSERT INTO AD_ForeignKeyInfo VALUES('");
		String delSQL = "DELETE FROM AD_ForeignKeyInfo WHERE ";

		int ic = -1;
		int ice = -1;
		int it2 = -1;
		int it2e = -1;
		int it1 = -1;
		int it1e = -1;
		
		String ctype = "F";
		
		if (sql.startsWith("ALTER TABLE ") && sql.indexOf(" PRIMARY KEY ")==-1)
		{
			it1 = sql.indexOf("ALTER TABLE ")+12;
			it1e = sql.indexOf(" ADD CONSTRAINT ", it1);
			ic = it1e+16;
			ice = sql.indexOf(" FOREIGN KEY ");
			it2 = sql.indexOf("REFERENCES ")+11;
			it2e = sql.indexOf("(", it2);						
		}
		else	if (sql.startsWith("CREATE TRIGGER "))
		{
			ic = sql.indexOf("CREATE TRIGGER ")+15;
			ice = sql.indexOf(" AFTER ", ic);
			it2 = sql.indexOf(" ON ", ice)+4;
			it2e = sql.indexOf(" REFERENCING ", it2);
			it1 = sql.indexOf(" DELETE FROM ", it2e) + 13;
			it1e = 0;
			if (it1>13)
			{
				it1e = sql.indexOf(" WHERE ", it1);
			}
			else
			{
				it1  = sql.indexOf(" UPDATE ", it2e) + 8;
				it1e = sql.indexOf(" SET ", it1);
			}
			ctype = "T";			
		}		
		
		if (it1 == -1 || it1e==-1 ||it2==-1 || it2e==-1 || ic==-1)
			return 0;
		
		sb.append(sql.substring(ic, ice).trim() + "', '");
		sb.append(sql.substring(it1, it1e).trim() + "', '");
		sb.append(sql.substring(it2, it2e).trim() + "', '");
		sb.append(ctype + "', '");
		sb.append(sql + "', ");
		if (oriName == null)
			sb.append("NULL)");
		else
			sb.append("'" + oriName + "')");
		
		if (oriName == null)
		{
			delSQL += " TableName = '" + sql.substring(it1, it1e).trim()  
						+ "' AND P_TableName = '" + sql.substring(it2, it2e).trim()
						+ "' C_Type = '" + ctype + "' AND OriginalName IS NULL";
		}
		else
		{
			delSQL += " OriginalName = '" + oriName + "'";
		}
		try
		{
			Statement stmt = conn.createStatement();
			try
			{
				stmt.executeUpdate(delSQL);
			}
			catch (SQLException e)
			{
				//ok if not success
			}
			
			stmt.executeUpdate(sb.toString());
			stmt.close();
			return 0;
		}
		catch (SQLException ex)
		{
			return -1;
		}
	}
	
	/**
	 *	get constriant information 
	 *
	 * 	@param conn Connection
	 *  @param oriName Original constraint name, usually longer than 18 chars
	 *  @return constraint name
	 *
	 */
	
	public static String getConstraintName(Connection conn, String oriName)
	{
		if (conn == null || oriName == null)
			return null;
		
		String sql = "SELECT ConstraintName FROM AD_ForeignKeyInfo WHERE OriginalName = '" + oriName + "'";
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs!=null && rs.next())
			{
				String cn = rs.getString(1);
				rs.close();
				stmt.close();
				return cn;
			}
		}
		catch (SQLException ex)
		{
			return null;
		}
		return null;
	}
	
	/**
	 *	get constriant name from original name 
	 *
	 * 	@param conn Connection
	 *  @param cname constraint name
	 *  @return sql constraint sql
	 *
	 */
	
	public static String get(Connection conn, String cname)
	{
		
		
		return null;
	}

	
	/**
	 *	get constriant names of the table 
	 *
	 * 	@param conn Connection
	 *  @param table table name
	 *  @return sql constraint names
	 *
	 */
	
	public static String[] getConstrainNames(Connection conn, String table)
	{
		
		
		return null;
	}

	
	final static private int KEYCOLNO = 16;  //enough, although derby limits columns in a table is 1012
	public static String forTrigger(int reExNo, String msg, String sql)
	{
		if (sql == null || sql.length() == 0)
			return null;
		
		
		int ix = sql.indexOf("ADD (") ;
		if (sql.startsWith("ALTER TABLE ") &&  ix > 0)
		{
			//add column without ()
			return "ALTER TABLE " + sql.substring(12,ix) + " ADD " + sql.substring(ix+5, sql.length()-1 );
		}
			
		ix = sql.indexOf("DROP FOREIGN KEY ") ;
		if (sql.startsWith("ALTER TABLE ") &&  ix > 0)
		{
			//add column without ()
			return "DROP TRIGGER " + sql.substring(ix+17, sql.length());
		}
			
		if (!sql.startsWith("ALTER TABLE ") || sql.indexOf("FOREIGN KEY") == -1)
			return null; // FK

		//handle alter table foreign key only at this moment
		String[] keywords = {"ALTER", "TABLE", "ADD", "CONSTRAINT", "FOREIGN", "KEY", "(", ",", ")", "REFERENCES", 
							 "ON", "DELETE", "SET", "NULL", "DEFAULT", "NO", "ACTION", "RISTRICT", "CASCADE"
		};
		String fTabName = null;
		String pTabName = null;
		String[] fkCols = new String[KEYCOLNO];
		String[] pkCols = new String[KEYCOLNO];
		int fColNo = 0;
		int pColNo = 0;
		String delRule = null;
		String constraintName = null;
		
		StringBuffer newSQL = null;
		
		int i = 0;
		String[] st = sql.split(" ");
		int ist = st.length;
		
	
		if (keywords[0].indexOf(st[0])== -1 || keywords[1].indexOf(st[1])== -1)
			return null;
		else
			fTabName = st[2];
		
		if (keywords[2].indexOf(st[3])== -1 || keywords[3].indexOf(st[4])== -1)
			return null;
		else
			constraintName = st[5];
		
		if (keywords[4].indexOf(st[6])== -1 || keywords[5].indexOf(st[7])== -1)
			return null;
		
		i = 8;
		if (st[8].equals(keywords[6]))
			i++;
		
		while (!st[i].equals(keywords[9]))
		{
			int s = 0;
			int e = st[i].length();
			if (st[i].startsWith(keywords[6]))
				s = 1;
			if (st[i].endsWith(keywords[7]) || st[i].endsWith(keywords[8]))
				e--;
			fkCols[fColNo++] = st[i++].substring(s, e);
		}
		
		pTabName = st[++i];
		i++;
		
		while (i<ist && !st[i].equals(keywords[10]))
		{
			int s = 0;
			int e = st[i].length();
			if (st[i].startsWith(keywords[6]))
				s = 1;
			if (st[i].endsWith(keywords[7]) || st[i].endsWith(keywords[8]))
				e--;
			pkCols[pColNo++] = st[i++].substring(s, e);
		}
		
		if (i<ist-1 && st[i].equals(keywords[10]) && st[i+1].equals(keywords[11]))
		{
			i+=2;
			if (i<ist)
			{
				delRule = st[i++];
			}
			if (i<ist)
			{
				delRule = delRule + " " + st[i];
			}
		}
		
		
		if (fTabName == null || pTabName == null || constraintName == null || fColNo == 0 || pColNo > 0 && fColNo != pColNo)
			return null; //something wrong
		
		if (DB.isDB2())
		{
			//construct an alternative SQL: create trigger
			newSQL = new StringBuffer("CREATE TRIGGER " + constraintName + " AFTER DELETE ON " + pTabName + " REFERENCING OLD AS OLD FOR EACH ROW MODE DB2SQL ");
			//jz assume there is now if (constraintName == null || constraintName.length() == 0)
	
			if (delRule == null || delRule.equals("NO ACTION") || delRule.equals("RESTRICTS"))
			{
				//if the dependant row exists, raise error to cause rollback
				String[] kCols = pkCols;
				if (pColNo == 0)
					kCols = fkCols;
				
				newSQL.append(" UPDATE " + pTabName + " SET  " + kCols[0] + " = null ");
				int ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(", " + kCols[ic] + " = null ");
					ic++;
				}
				
				newSQL.append(" EXISTS (SELECT 1 FROM " + fTabName + " WHERE "
						+ fkCols[0] + " = OLD.");
				if (pColNo == 0)
					newSQL.append(fkCols[0]);
				else
					newSQL.append(pkCols[0]);
	
				ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(" AND " + fkCols[ic] + " = OLD.");
					if (pColNo == 0)
						newSQL.append(fkCols[ic]);
					else
						newSQL.append(pkCols[ic]);
					ic++;
				}
				newSQL.append(")");
			}
			else if (delRule.equals("CASCADE"))
			{
				//goto delete
				newSQL.append(" DELETE FROM " + fTabName + " WHERE " + fkCols[0] + " = OLD.");
				if (pColNo == 0)
					newSQL.append(fkCols[0]);
				else
					newSQL.append(pkCols[0]);
					
					
				int ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(" AND " + fkCols[ic] + " = OLD.");
					if (pColNo == 0)
						newSQL.append(fkCols[ic]);
					else
						newSQL.append(pkCols[ic]);
					ic++;
				}
			}
			else if (delRule.equals("SET NULL"))
			{
				//goto update, assume
				newSQL.append(" UPDATE " + fTabName + " SET  " + fkCols[0] + " = null ");
				int ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(", " + fkCols[ic] + " = null ");
					ic++;
				}
				
				newSQL.append(" WHERE " + fkCols[0] + " = OLD.");
				if (pColNo == 0)
					newSQL.append(fkCols[0]);
				else
					newSQL.append(pkCols[0]);
	
				ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(" AND " + fkCols[ic] + " = OLD.");
					if (pColNo == 0)
						newSQL.append(fkCols[ic]);
					else
						newSQL.append(pkCols[ic]);
					ic++;
				}
			}
			else if (delRule.equals("SET DEFAULT"))
			{
				//goto update
				newSQL.append(" UPDATE " + fTabName + " SET  " + fkCols[0] + " = default ");
				int ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(", " + fkCols[ic] + " = default ");
					ic++;
				}
				
				newSQL.append(" WHERE " + fkCols[0] + " = OLD.");
				if (pColNo == 0)
					newSQL.append(fkCols[0]);
				else
					newSQL.append(pkCols[0]);
	
				ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(" AND " + fkCols[ic] + " = OLD.");
					if (pColNo == 0)
						newSQL.append(fkCols[ic]);
					else
						newSQL.append(pkCols[ic]);
	
					ic++;
				}
			}
			else 
				return null;
				
		}//DB2
		else if (DB.isMSSQLServer())
		{
			//TODO
			//construct an alternative SQL: create trigger
			newSQL = new StringBuffer("CREATE TRIGGER " + constraintName + " ON " + pTabName + " AFTER DELETE AS BEGIN SET NOCOUNT ON; ");
			//jz assume there is now if (constraintName == null || constraintName.length() == 0)
	
			if (delRule == null || delRule.equals("NO ACTION") || delRule.equals("RESTRICTS")) //done
			{
				if (pColNo == 0) {
				}
				
				newSQL.append(" UPDATE t SET  VIP_COL = NULL ");
				newSQL.append(" FROM C_NOTNULL t JOIN deleted d ON EXISTS (SELECT 1 FROM " + fTabName + "ct WHERE ct."
						+ fkCols[0] + " = d.");
				if (pColNo == 0)
					newSQL.append(fkCols[0]);
				else
					newSQL.append(pkCols[0]);
	
				int ic = 0;
				while (++ic < fColNo)
				{
					newSQL.append(" AND ct." + fkCols[ic] + " = d.");
					if (pColNo == 0)
						newSQL.append(fkCols[ic]);
					else
						newSQL.append(pkCols[ic]);
				}
				newSQL.append(")");
			}
			else if (delRule.equals("CASCADE")) //done
			{
				//goto delete
				newSQL.append(" DELETE FROM ct FROM " + fTabName + " ct JOIN deleted d ON ct." + fkCols[0] + " = d.");
				if (pColNo == 0)
					newSQL.append(fkCols[0]);
				else
					newSQL.append(pkCols[0]);
					
					
				int ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(" AND ct." + fkCols[ic] + " = d.");
					if (pColNo == 0)
						newSQL.append(fkCols[ic]);
					else
						newSQL.append(pkCols[ic]);
					ic++;
				}
			}
			else if (delRule.equals("SET NULL"))  //done
			{
				//goto update, assume
				newSQL.append(" UPDATE ct SET  " + fkCols[0] + " = NULL ");
				int ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(", " + fkCols[ic] + " = NULL ");
					ic++;
				}
				
				newSQL.append(" FROM " + fTabName + " ct JOIN deleted d ON ct." + fkCols[0] + " = d.");
				if (pColNo == 0)
					newSQL.append(fkCols[0]);
				else
					newSQL.append(pkCols[0]);
	
				ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(" AND ct." + fkCols[ic] + " = d.");
					if (pColNo == 0)
						newSQL.append(fkCols[ic]);
					else
						newSQL.append(pkCols[ic]);
					ic++;
				}
			}
			else if (delRule.equals("SET DEFAULT"))
			{
				//goto update
				newSQL.append(" UPDATE ct SET  " + fkCols[0] + " = DEFAULT ");
				int ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(", " + fkCols[ic] + " = DEFAULT ");
					ic++;
				}
				
				newSQL.append(" FROM " + fTabName + " ct JOIN deleted d ON ct." + fkCols[0] + " = d.");
				if (pColNo == 0)
					newSQL.append(fkCols[0]);
				else
					newSQL.append(pkCols[0]);
	
				ic = 1;
				while (ic < fColNo)
				{
					newSQL.append(" AND ct." + fkCols[ic] + " = d.");
					if (pColNo == 0)
						newSQL.append(fkCols[ic]);
					else
						newSQL.append(pkCols[ic]);
	
					ic++;
				}
			}
			else 
				return null;
			newSQL.append(" END");				
		}
		else 
			return null;
		
		return newSQL.toString(); //alternative SQL for this sql
	}
}	//	CConstraint
