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

import java.math.*;
import java.sql.*;
import java.util.*;

import org.compiere.*;
import org.compiere.common.*;
import org.compiere.model.*;
import org.compiere.util.*;

//import com.compiere.migrate.DBImport;


/**
 *	Database Utilities
 *	
 *  @author Jinglun Zhang  a few common functions, some merged from existing classes so that we do not need to maintain multiple copies
 *  @version $Id: DBUtils.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class DBUtils
{

	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (DBUtils.class);

	
	/**************************************************************************
	 * jz
	 * 
	 * 	rewrite SQL if there is ROWNUM=1 in a query with updated in select max(updated) from ...
	 *  this is not a generic solution, with
	 *  assume: 1. only ROWNUM=1
	 *  		2. it is in a query with 1 table and the table has a column called "UPDATED"
	 *  		3. key words in upper case
	 *  
	 *	@param oraS oracle style statement
	 *	@return statement
	 */
	public static String repRownum (String oraS)
	{
		if (oraS == null || oraS.length()==0)
			return oraS;
		
		int irn = oraS.indexOf(" ROWNUM=1");
		if (irn == -1)
			return oraS;
		
		//get all indexes needed
		int isel = 0;
		int ire = 0;
		int iFrom = 0;
		String cid = null;
		
		ire = oraS.indexOf(")", irn);
		if (ire == -1)
			ire = oraS.length();
		
		String sq = oraS.substring(0, ire+1);
		int iselt = isel;
		while (iselt < ire && isel != -1)
		{
			iselt = sq.indexOf("SELECT");
			if (iselt != -1)
				isel = iselt;
		}
		
		iFrom = sq.indexOf(" FROM ", isel);
		
		sq = sq.substring(iFrom+5, ire);
		
		//make right correlated ID
		String[] tabIDs = sq.split(" ", 3);
		if (tabIDs[1].equals(",")||tabIDs[1].equals("WHERE"))
			;
		else
			cid = tabIDs[1]+".";
		
		sq = oraS.substring(iFrom, oraS.charAt(ire)==')'?ire:ire+1);
		String cid1 = null;
		if (cid != null)
		{
			cid1 = cid.substring(0,cid.length())+"1.";
			sq=sq.replaceAll(cid,cid1);
		}

		//construct new SQL
		StringBuffer sql = new StringBuffer(oraS.substring(0,irn));
		sql.append(cid + "UPDATED in ( SELECT MAX(" + cid1 + "UPDATED) " + sq + ") ");
		if (ire<oraS.length())
			sql.append(oraS.substring(ire+1, oraS.length()));
		
		//
		return sql.toString();
	}	
		


	/**
	 *  Create SQL TO Date String from Timestamp
	 *
	 *  @param  time Date to be converted
	 *  @param  dayOnly true if time set to 00:00:00
	 *  @return date function
	 */
	public static String TO_DATE (Timestamp time, boolean dayOnly)
	{
		if (time == null)
		{
			if (dayOnly)
				//jz   return "convert(date,getdate())";
				return "trunc(getdate(), 'DD')"; //jz default to 'DD'
			return "getdate()";
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(time);
		//
		//jz StringBuffer dateString = new StringBuffer("convert(datetime,'");
		StringBuffer dateString = new StringBuffer("TIMESTAMP('");
		//	yyyy.mm.dd	- format 2 p.411 
		/* jz 
		if (dayOnly)
		{
			int yyyy = cal.get(Calendar.YEAR);
			String format = "102";	//	"SQL Standard" format
			if (yyyy < 100)
				format = "2";
			dateString.append(yyyy).append(".")
				.append(getXX(cal.get(Calendar.MONTH)+1)).append(".")
				.append(getXX(cal.get(Calendar.DAY_OF_MONTH)))
				.append("',").append(format).append(")");
		}
		//	mon dd yyy hh:mi:ss - format 116
		else
		{
			int yyyy = cal.get(Calendar.YEAR);
			String format = "116";	//	n/a format
			if (yyyy < 100)
				format = "16";
			dateString.append(MONTHS[cal.get(Calendar.MONTH)]).append(" ")
				.append(getXX(cal.get(Calendar.DAY_OF_MONTH))).append(" ")
				.append(getXX(cal.get(Calendar.YEAR))).append(" ")
				.append(getXX(cal.get(Calendar.HOUR_OF_DAY))).append(":")
				.append(getXX(cal.get(Calendar.MINUTE))).append(":")
				.append(getXX(cal.get(Calendar.SECOND)))
				.append("',").append(format).append(")");
		}
		  jz */
		if (dayOnly)
		{
			int yyyy = cal.get(Calendar.YEAR);
			dateString.append(yyyy)
				.append(getXX(cal.get(Calendar.MONTH)+1))
				.append(getXX(cal.get(Calendar.DAY_OF_MONTH)))
				.append("000000").append("')");
		}
		//	mon dd yyy hh:mi:ss - format 116
		else
		{
			int yyyy = cal.get(Calendar.YEAR);
			dateString.append(yyyy)
			.append(getXX(cal.get(Calendar.MONTH)+1))
			.append(getXX(cal.get(Calendar.DAY_OF_MONTH)))
				.append(getXX(cal.get(Calendar.HOUR_OF_DAY)))
				.append(getXX(cal.get(Calendar.MINUTE)))
				.append(getXX(cal.get(Calendar.SECOND)))
				.append("')");
		}
		return dateString.toString();
	}	//	TO_DATE

	/**
	 * 	Get integer as two string digits (leading zero)
	 *	@param x integer
	 *	@return string of x
	 */
	private static String getXX (int x)
	{
		if (x < 10)
			return "0" + x;
		return String.valueOf(x);
	}	//	getXX
	
	/**
	 *  Use user defined SQL functions for formatted Date, Number
	 *
	 *  @param  columnName  the column name in the SQL
	 *  @param  displayType Display Type
	 *  @param  AD_Language 6 character language setting (from Env.LANG_*)
	 *
	 *  @return TRIM(TO_CHAR(columnName,'9G999G990D00','NLS_NUMERIC_CHARACTERS='',.'''))
	 *      or TRIM(TO_CHAR(columnName,'TM9')) depending on DisplayType and Language
	 *  @see org.compiere.util.DisplayType
	 *  @see org.compiere.util.Env
	 *
	 **/
	public static String TO_CHAR (String columnName, int displayType, String AD_Language)
	{
		//TODO //jz need more work later 
		if (FieldType.isDate(displayType))
		{
			return " Time2Chars("+columnName+") ";
		}
		//  number
		else if (FieldType.isNumeric(displayType))
		{
			return " getChars("+columnName+") ";
		}
		//  String

		return "";
	}	//	TO_CHAR


	/**
	 *  Get a string representation of literal used in SQL clause
	 *
	 *  @param  sqlClause "S", "U","I", "W"
	 *  @param  dataType java.sql.Types
	 *
	 *  @return db2: nullif(x,x)
	 */
	public static String nullValue (String sqlClause, int dataType)
	{
		if ("I".equals(sqlClause))
		{
			switch (dataType)
			{
			case Types.INTEGER:
				return "nullif(0,0)";
				
			case Types.DECIMAL:
				return "nullif(0.1,0.1)";
				
			case Types.TIMESTAMP:
				return "nullif(Timestamp('20061012000000'),Timestamp('20061012000000'))";
				
			case Types.VARCHAR:
				return "nullif('a','a')";
				
			default:
				return "''";
			}
		}
		else
			return "''";
	}   //  nullValue


	
	
	/**
	 * 	Return number as string for INSERT statements with correct precision
	 *	@param number number
	 *	@param displayType display Type
	 *	@return number as string
	 */
	public static String TO_NUMBER (BigDecimal number, int displayType)
	{
		if (number == null)
			return "NULL";
		BigDecimal result = number;
		int scale = FieldType.getDefaultPrecision(displayType);
		if (number.scale() > scale)
		{
			try
			{
				result = number.setScale(scale, BigDecimal.ROUND_HALF_UP);
			}
			catch (Exception e)
			{
				log.severe("Number=" + number + ", Scale=" + " - " + e.getMessage());
			}
		}
		return result.toString();
	}	//	TO_NUMBER
	
	


	
	

	/**
	 * 
	 * jz: NOT a generic one
	 * 
	 *  change WHERE (...) IN/NOT IN (select ... from ) to one column at a time
	 *  Warning: This may not translate an equivalent sql 
	 *
	 *  @param  sql update clause
	 *
	 *  @return new sql
	 */
	
	public static String whereSelectList (String sql)
	{
		String s = whereSelectList1(sql, "IN");
		s = whereSelectList1(s,"NOT IN");
		return s;
	}
	
	private static String whereSelectList1 (String sql, String notIn)
	{
		String keyword = null;
		if (notIn.equals("IN"))
			keyword = ") IN (";
		else
			keyword = ") NOT IN (";
				
		int ixIn = sql.indexOf(keyword+"SELECT");
 		
		if (ixIn==-1)
			return sql;
		
		StringBuffer newSQL = new StringBuffer();
		int copyBegin = 0;
		int isql = sql.length();
		
		while (ixIn > -1)
		{
			int iColEnd = ixIn;
			int iCol = iColEnd-1;
			int iSelect = ixIn+keyword.length();
			int il = 1;
			int ir = 0;
			
			iSelect += 6;
			int iFrom = sql.indexOf(" FROM ", iSelect);
			
			//get column list beginning
			ir = 1;
			il = 0;
			while (il < ir && --iCol >= 0)
			{
				if (sql.charAt(iCol)==')')
					ir++;
				else if (sql.charAt(iCol)=='(')
					il++;
			}			
			
			//copy first part
			if (copyBegin < iCol)
				newSQL = newSQL.append(sql.substring(copyBegin,iCol));
			
			String[] colList = sql.substring(iCol+1,iColEnd).split(",");
			String[] tableNames = sql.substring(iFrom,isql).split(" ");
			String[] selectList = sql.substring(iSelect,iFrom).split(",");
			StringBuffer colList1 = new StringBuffer();
			StringBuffer selectList1 = new StringBuffer();
			MTable mt = MTable.get(Env.getCtx(), tableNames[2].trim());

			for (int i=0; i<colList.length; i++)
			{
				if (i>0)
				{
					colList1.append(" || ");
					selectList1.append(" || ");
				}
				colList[i]=colList[i].trim();
				selectList[i]=selectList[i].trim();
				MColumn mc = mt.getColumn(colList[i]);
				if (mc==null)
				{
					log.severe("Wrong column:" + colList[i] + ": in sql: " + sql);
					return sql;
				}
				int dt = mt.getColumn(colList[i]).getAD_Reference_ID(); //getElement().getDisplayType();
				colList1.append(DB.TO_CHAR(colList[i], dt, Env.getAD_Language(Env.getCtx())));
				selectList1.append(DB.TO_CHAR(selectList[i], dt, Env.getAD_Language(Env.getCtx())));
			}//for
			newSQL.append(" " + colList1 + keyword.substring(1) + "SELECT " +selectList1);
			copyBegin = iFrom;
			ixIn = sql.indexOf(keyword+"SELECT", copyBegin);			
		}//while

		if (copyBegin <isql)
			newSQL.append(sql.substring(copyBegin, isql));
		return newSQL.toString();
	}   //  


	/**
	 * 
	 * jz NOT a generic one
	 * 
	 *  change update set (...) = (select ... from ) standard format 
	 *
	 *  @param  sql update clause
	 *
	 *  @return new sql
	 */
	
	public static String updateSetSelectList (String sql)
	{
		StringBuffer newSQL = null;
		int iSet = sql.indexOf(" SET (");
		int iSetEnd = sql.indexOf(")",iSet);
		int iSelect = sql.indexOf("(SELECT ",iSetEnd);
		int iSelectEnd = sql.indexOf(" FROM ",iSelect);
		int il = 1;
		int ir = 0;
		int isql = sql.length();
		
		if (iSet == -1 || iSetEnd == -1 || iSelect == -1 || iSelectEnd == -1)
			return sql;
		
		//get lists
		String[] setList = sql.substring(iSet+6,iSetEnd).split(",");
		
		String[] selectList = null;
		String selListStr = sql.substring(iSelect+7,iSelectEnd);
		//if (selListStr.indexOf("), ")>0)
		if (selListStr.indexOf(")")>0)
			selectList = selListStr.split(", ");
		else
			selectList = selListStr.split(",");
		
		//get subQuery end
		int end = iSelectEnd+5;
		while (il > ir && ++end < isql)
		{
			if (sql.charAt(end)==')')
				ir++;
			else if (sql.charAt(end)=='(')
				il++;
		}
		if (end < isql)
			end++;
		
		//construct new sql
		newSQL = new StringBuffer(sql.substring(0,iSet+5));
		
		for (int i=0; i<setList.length; i++)
		{
			if (i>0)
				newSQL.append(" , ");
			newSQL.append(setList[i]+" = (SELECT "+selectList[i]+" "+sql.substring(iSelectEnd, end));
		}
		
		if (end <=isql)
			newSQL.append(sql.substring(end, isql));

		return newSQL.toString();		
	}   //  

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String s = updateSetSelectList("UPDATE T_Report SET Col_0 = (SELECT  COALESCE(SUM(Col_0),0)  FROM T_Report WHERE AD_PInstance_ID=1000014 AND PA_ReportLine_ID IN (0,0) AND ABS(LevelNo)<1) WHERE AD_PInstance_ID=1000014 AND PA_ReportLine_ID=100 AND ABS(LevelNo)<1");
		System.out.println(s);
		Compiere.startup(true);

		s = whereSelectList("DELETE FROM C_PeriodControl WHERE (C_Period_ID, DocBaseType) IN (SELECT C_Period_ID, DocBaseType FROM C_PeriodControl pc2 GROUP BY C_Period_ID, DocBaseType HAVING COUNT(*) > 1) AND C_PeriodControl_ID NOT IN (SELECT MIN(C_PeriodControl_ID) FROM C_PeriodControl pc3 GROUP BY C_Period_ID, DocBaseType)");
		System.out.println(s);
		s = whereSelectList("DELETE FROM C_PeriodControl WHERE (C_Period_ID, DocBaseType) NOT IN (SELECT C_Period_ID, DocBaseType FROM C_PeriodControl pc2 GROUP BY C_Period_ID, DocBaseType HAVING COUNT(*) > 1) AND C_PeriodControl_ID NOT IN (SELECT MIN(C_PeriodControl_ID) FROM C_PeriodControl pc3 GROUP BY C_Period_ID, DocBaseType)");
		System.out.println(s);
	}  // main()
	
}	//	DBUtils
