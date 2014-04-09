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
import java.util.*;
import java.util.logging.*;
import java.util.regex.*;

import org.compiere.common.CompiereStateException;
import org.compiere.startup.*;
import org.compiere.util.*;

/**
 *  Convert SQL to Target DB
 *
 *  @version    $Id: Convert.java 8414 2010-02-09 19:42:16Z freyes $
 */
public class Convert
{
	/**
	 *  Cosntructor
	 *  @param type Database.DB_
	 */
	public Convert (String type)
	{
		if (Environment.DBTYPE_ORACLE.equals(type))
			m_isOracle = true;
		else if (Environment.DBTYPE_DB2.equals(type))
			m_map = ConvertMap.getDB2Map();
		else if (Environment.DBTYPE_PG.equals(type))
			m_map = ConvertMap.getPGMap();
		else if (Environment.DBTYPE_MS.equals(type))
			m_map = ConvertMap.getMSMap();
		else
			throw new UnsupportedOperationException ("Unsupported database: " + type);
	}   //  Convert

	/** RegEx: insensitive and dot to include line end characters   */
	public static final int         REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;

	/** Is Oracle                       */
	private boolean                 m_isOracle = false;
	/** Used Resorce Bundle             */
	private TreeMap<String,String> m_map;

	/** Statement used                  */
	private Statement               m_stmt = null;

	/** Last Conversion Error           */
	private String                  m_conversionError = null;
	/** Last Execution Error            */
	private Exception               m_exception = null;
	/** Verbose Messages                */
	private boolean                 m_verbose = true;

	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (Convert.class);
	
	/**
	 *  Set Verbose
	 *  @param verbose
	 */
	public void setVerbose (boolean verbose)
	{
		m_verbose = verbose;
	}   //  setVerbose

	/**
	 *  Is Oracle DB
	 *  @return true if connection is Oracle DB
	 */
	public boolean isOracle()
	{
		return m_isOracle;
	}   //  isOracle

	
	/**************************************************************************
	 *  Execute SQL Statement (stops at first error).
	 *  If an error occured hadError() returns true.
	 *  You can get details via getConversionError() or getException()
	 *  @param sqlStatements
	 *  @param conn connection
	 *  @return true if success
	 *  @throws IllegalStateException if no connection
	 */
	public boolean execute (String sqlStatements, Connection conn)
	{
		if (conn == null)
			throw new CompiereStateException ("Require connection");
		//
		String[] sql = convert (sqlStatements);
		m_exception = null;
		if (m_conversionError != null || sql == null)
			return false;

		boolean ok = true;
		int i = 0;
		String statement = null;
		try
		{
			if (m_stmt == null)
				m_stmt = conn.createStatement();
			//
			for (i = 0; ok && i < sql.length; i++)
			{
				statement = sql[i];
				if (statement.length() == 0)
				{
					if (m_verbose)
						log.finer("Skipping empty (" + i + ")");
				}
				else
				{
					if (m_verbose)
						log.info("Executing (" + i + ") <<" + statement + ">>");
					else
						log.info("Executing " + i);
					try
					{
						m_stmt.clearWarnings();
						int no = m_stmt.executeUpdate(statement);
						SQLWarning warn = m_stmt.getWarnings();
						if (warn != null)
						{
							if (m_verbose)
								log.info("- " + warn);
							else
							{
								log.info("Executing (" + i + ") <<" + statement + ">>");
								log.info("- " + warn);
							}
						}
						if (m_verbose)
							log.fine("- ok " + no);
					}
					catch (SQLException ex)
					{
						//  Ignore Drop Errors
						if (!statement.startsWith("DROP "))
						{
							ok = false;
							m_exception = ex;
						}
						if (!m_verbose)
							log.info("Executing (" + i + ") <<" + statement + ">>");
						log.info("Error executing " + i + "/" + sql.length + " = " + ex);
					}
				}
			}   //  for all statements
		}
		catch (SQLException e)
		{
			m_exception = e;
			if (!m_verbose)
				log.info("Executing (" + i + ") <<" + statement + ">>");
			log.info("Error executing " + i + "/" + sql.length + " = " + e);
			return false;
		}
		return ok;
	}   //  execute

	/**
	 *  Return last execution exception
	 *  @return execution exception
	 */
	public Exception getException()
	{
		return m_exception;
	}   //  getException

	/**
	 *  Returns true if a conversion or execution error had occured.
	 *  Get more details via getConversionError() or getException()
	 *  @return true if error had occured
	 */
	public boolean hasError()
	{
		return (m_exception != null) | (m_conversionError != null);
	}   //  hasError

	/**
	 *  Convert SQL Statement (stops at first error).
	 *  Statements are delimited by /
	 *  If an error occured hadError() returns true.
	 *  You can get details via getConversionError()
	 *  @param sqlStatements
	 *  @return converted statement as a string
	 */
	public String convertAll (String sqlStatements)
	{
		String[] sql = convert (sqlStatements);
		StringBuffer sb = new StringBuffer (sqlStatements.length() + 10);
		for (int i = 0; i < sql.length; i++)
		{
			//  line.separator
			sb.append(sql[i]).append("\n/\n");
			if (m_verbose)
				log.info("Statement " + i + ": " + sql[i]);
		}
		return sb.toString();
	}   //  convertAll

	/**
	 *  Convert SQL Statement (stops at first error).
	 *  If an error occured hadError() returns true.
	 *  You can get details via getConversionError()
	 *  @param sqlStatements
	 *  @return Array of converted Statements
	 */
	public String[] convert (String sqlStatements)
	{
		m_conversionError = null;
		if (sqlStatements == null || sqlStatements.length() == 0)
		{
			m_conversionError = "SQL_Statement is null or has zero length";
			log.info(m_conversionError);
			return null;
		}
		//
		return convertIt (sqlStatements);
	}   //  convert

	/**
	 *  Return last conversion error or null.
	 *  @return lst conversion error
	 */
	public String getConversionError()
	{
		return m_conversionError;
	}   //  getConversionError

	
	/**************************************************************************
	 *  Conversion routine (stops at first error).
	 *  <pre>
	 *  - mask / in Strings
	 *  - break into single statement
	 *  - unmask statements
	 *  - for each statement: convertStatement
	 *      - remove comments
	 *          - process FUNCTION/TRIGGER/PROCEDURE
	 *          - process Statement: convertSimpleStatement
	 *              - based on ConvertMap
	 *              - convertComplexStatement
	 *                  - decode, sequence, exception
	 *  </pre>
	 *  @param sqlStatements
	 *  @return array of converted statements
	 */
	private String[] convertIt (String sqlStatements)
	{
		//  Need to mask / in SQL Strings !
		final char MASK = '\u001F';      //  Unit Separator
		StringBuffer masked = new StringBuffer(sqlStatements.length());
		Matcher m = Pattern.compile("'[^']+'", Pattern.DOTALL).matcher(sqlStatements);
		while (m.find())
		{
			String group = m.group();       //  SQL string
			if (group.indexOf("/") != -1)   //  / in string
				group = group.replace('/', MASK);
			// EDB jdbc bug that has to escape \
			if (group.indexOf('\\') != -1)   //  \ needs to be escaped
				group = Util.replace(group, "\\", "\\\\\\\\");
			if (group.indexOf('$') != -1)   //  Group character needs to be escaped
				group = Util.replace(group, "$", "\\$");
			m.appendReplacement(masked, group);
		}
		m.appendTail(masked);
		String tempResult = masked.toString();
		/** @todo Need to mask / in comments */
		
		//  Statements ending with /
		String[] sql = tempResult.split("\\s/\\s");  // ("(;\\s)|(\\s/\\s)");
		ArrayList<String> result = new ArrayList<String> (sql.length);
		//  process statements
		for (String statement : sql) {
			if (statement.indexOf(MASK) != -1)
				statement = statement.replace(MASK, '/');
			result.addAll(convertStatement(statement));     //  may return more than one target statement
		}
		//  convert to array
		sql = new String[result.size()];
		result.toArray(sql);
		return sql;
	}   //  convertIt

	/**
	 *  Convert single Statements.
	 *  - remove comments
	 *      - process FUNCTION/TRIGGER/PROCEDURE
	 *      - process Statement
	 *  @param sqlStatement
	 *  @return converted statement
	 */
	private ArrayList<String> convertStatement (String sqlStatement)
	{
		ArrayList<String> result = new ArrayList<String>();
		if (m_isOracle)
		{
			result.add(sqlStatement);
			return result;
		}

		// Temp comment out this since trimmed extra space for any insert string
		//  remove comments
//		String statement = removeComments (sqlStatement);
		String statement = sqlStatement;
	//	log.info("------------------------------------------------------------");
	//	log.info(statement);
	//	log.info("------------------->");

		String cmpString = statement.toUpperCase();
		boolean isCreate = cmpString.startsWith("CREATE ");

		//  Process
		if (isCreate && cmpString.indexOf(" FUNCTION ") != -1)
			result.addAll(convertFunction(statement));

		else if (isCreate && cmpString.indexOf(" TRIGGER ") != -1)
			result.addAll(convertTrigger(statement));

		else if (isCreate && cmpString.indexOf(" PROCEDURE ") != -1)
			result.addAll(convertProcedure(statement));

		else if (isCreate && cmpString.indexOf(" VIEW ") != -1)
			result.addAll(convertView(statement));

		//  Simple Statement
		else
			result.add(converSimpleStatement(statement));
		//
	//	log.info("<-------------------");
	//	for (int i = 0; i < result.size(); i++)
	//		log.info(result.get(i));
	//	log.info("------------------------------------------------------------");

		return result;
	}   //  convertStatement

	/**
	 *  Convert simple SQL Statement.
	 *  Based on ConvertMap
	 *
	 *  @param sqlStatement
	 *  @return converted Statement
	 */
	private String converSimpleStatement (String sqlStatement)
	{
		//  Error Checks
		if (sqlStatement.toUpperCase().indexOf("EXCEPTION WHEN") != -1)
		{
			String error = "Exception clause needs to be converted: " + sqlStatement;
			log.info (error);
			m_conversionError = error;
			return sqlStatement;
		}

		//  Standard Statement
		String retValue = sqlStatement;
		Iterator<String> iter = m_map.keySet().iterator();
		while (iter.hasNext())
		{
			String regex = iter.next();
			String replacement = m_map.get(regex);
			try
			{
				Pattern p = Pattern.compile(regex, REGEX_FLAGS);
				Matcher m = p.matcher(retValue);
				retValue = m.replaceAll(replacement);
			}
			catch (Exception e)
			{
				String error = "Error expression: " + regex + " - " + e;
				log.info(error);
				m_conversionError = error;
			}
		}

		//  Convert Decode, Sequence, Join, ..
		return convertComplexStatement(retValue);
	}   //  convertSimpleStatement

	/**
	 *  Clean up Statement.
	 *  Remove all comments and while spaces
	 *  Database specific functionality can me tagged as follows:
	 *  <pre>
	 *	&#047;*ORACLE&gt;*&#047;
	 *      Oracle Specific Statement
	 *	&#047;*&lt;ORACLE*&#047;
	 *	&#047;*POSTGRESQL&gt;
	 *      PostgreSQL Specicic Statements
	 *	&lt;POSTGRESQL*&#047;
	 *  </pre>
	 *  @param statement
	 *  @return sql statement
	 */
	protected String removeComments (String statement)
	{
		String clean = statement.trim();

		//  Remove /*ORACLE>*/ /*<ORACLE*/
		Matcher m = Pattern.compile("\\/\\*ORACLE>.*<ORACLE\\*\\/", Pattern.DOTALL).matcher(clean);
		clean = m.replaceAll("");

		//  Remove /.POSTGRESQL>
		m = Pattern.compile("\\/\\*POSTGRESQL>").matcher(clean);
		clean = m.replaceAll("");
		//	Remove <POSTGRESQL./
		m = Pattern.compile("<POSTGRESQL\\*\\/").matcher(clean);
		clean = m.replaceAll("");

		//  Remove /* */
		m = Pattern.compile("\\/\\*.*\\*\\/", Pattern.DOTALL).matcher(clean);
		clean = m.replaceAll("");
/**
		//  Remove --
		m = Pattern.compile("--.*$").matcher(clean);        //  up to EOL
		clean = m.replaceAll("");
		m = Pattern.compile("--.*[\\n\\r]").matcher(clean); //  -- at BOL
		clean = m.replaceAll("");
**/
		//  Convert cr/lf/tab to single space
		m = Pattern.compile("\\s+").matcher(clean);
		clean = m.replaceAll(" ");

		clean = clean.trim();
		return clean;
	}   //  removeComments

	/**
	 *  Convert Function.
	 *  <pre>
	 *      CREATE OR REPLACE FUNCTION AD_Message_Get
	 *      (p_AD_Message IN VARCHAR, p_AD_Language IN VARCHAR)
	 *      RETURN VARCHAR AS
	 *      ...
	 *      END AD_Message_Get;
	 *  =>
	 *      CREATE FUNCTION AD_Message_Get
	 *      (VARCHAR, VARCHAR)
	 *      RETURNS VARCHAR AS '
	 *      DECLARE
	 *      p_AD_Message ALIAS FOR $1;
	 *      p_AD_Language ALIAS FOR $2;
	 *      ....
	 *      END;
	 *      ' LANGUAGE 'plpgsql';
	 *  </pre>
	 *  @param sqlStatement
	 *  @return CREATE and DROP Function statement
	 */
	private ArrayList<String> convertFunction (String sqlStatement)
	{
		ArrayList<String> result = new ArrayList<String>();
		//  Convert statement - to avoid handling contents of comments
		String stmt = converSimpleStatement(sqlStatement);
		//  Double quotes '
		stmt = Pattern.compile("'").matcher(stmt).replaceAll("''");
		//  remove OR REPLACE
		int orReplacePos = stmt.toUpperCase().indexOf(" OR REPLACE ");
		if (orReplacePos != -1)
			stmt = "CREATE" + stmt.substring(orReplacePos+11);

		//  Line separators
		String match =
			  "(\\([^\\)]*\\))"                 //  (.) Parameter
			+ "|(\\bRETURN \\w+ (AS)|(IS))"     //  RETURN CLAUSE
			+ "|(;)"                            //  Statement End
			//  Nice to have - for readability
			+ "|(\\bBEGIN\\b)"                  //  BEGIN
			+ "|(\\bTHEN\\b)"
			+ "|(\\bELSE\\b)"
			+ "|(\\bELSIF\\b)";
		Matcher m = Pattern.compile(match, Pattern.CASE_INSENSITIVE).matcher(stmt);

		StringBuffer sb = new StringBuffer();
		//  First group -> ( )
		//  CREATE OR REPLACE FUNCTION AD_Message_Get ( p_AD_Message IN VARCHAR, p_AD_Language IN VARCHAR)
		//  CREATE FUNCTION AD_Message_Get (VARCHAR, VARCHAR)
		m.find();
		m.appendReplacement(sb, "");
		String name = sb.substring(6).trim();
		StringBuffer signature = new StringBuffer();
		//
		String group = m.group().trim();
	//	log.info("Group: " + group);
		StringBuffer alias = new StringBuffer();
		//  Parameters
		if (group.startsWith("(") && group.endsWith(")"))
		{
			//  Default not supported
			if (group.toUpperCase().indexOf(" DEFAULT ") != -1)
			{
				String error = "DEFAULT in Parameter not supported";
				log.info (error);
				m_conversionError = error;
				return result;
			}
			signature.append("(");
			if (group.length() > 2)
			{
				group = group.substring(1,group.length()-1);
				//  Paraneters are delimited by ,
				String[] parameters = group.split(",");
				for (int i = 0; i < parameters.length; i++)
				{
					if (i != 0)
						signature.append(", ");
					//  name ALIAS FOR $1
					String p = parameters[i].trim();
					alias.append(p.substring(0,p.indexOf(" ")))
						.append(" ALIAS FOR $").append(i+1).append(";\n");
					//  Datatape
					signature.append(p.substring(p.lastIndexOf(" ")+1));
				}
			}
			signature.append(")");
			sb.append(signature);
		//	log.info("Alias: " + alias.toString());
		//	log.info("Signature: " + signature.toString());
		}
		//  No Parameters
		else
		{
			String error = "Missing Parameter ()";
			log.info (error);
			m_conversionError = error;
			return result;
		}
		sb.append("\n");
		//  Need to create drop statement
		if (orReplacePos != -1)
		{
			String drop = "DROP " + name + signature.toString();
		//	log.info(drop);
			result.add(drop);
		}
	//	log.info("1>" + sb.toString() + "<1");

		//  Second Group -> RETURN VARCHAR AS
		//  RETURNS VARCHAR AS
		m.find();
		group = m.group();
		m.appendReplacement(sb, "");
		if (group.startsWith("RETURN"))
			sb.append("RETURNS").append(group.substring(group.indexOf(" ")));
		sb.append(" '\nDECLARE\n")
			.append(alias);         //  add aliases here
	//	log.info("2>" + sb.toString() + "<2");

		//  remainder statements
		while (m.find())
		{
			String group2 = m.group();
			if (group2.indexOf('$') != -1)   //  Group character needs to be escaped
				group2 = Util.replace(group2, "$", "\\$");
			m.appendReplacement(sb, group2);
			sb.append("\n");
		}
		m.appendTail(sb);

		//  finish
		sb.append("' LANGUAGE 'plpgsql';");
	//	log.info(">" + sb.toString() + "<");
		result.add(sb.toString());
		//
		return result;
	}   //  convertFunction

	/**
	 *  Convert Procedure.
	 *  <pre>
	 *      CREATE OR REPLACE PROCEDURE AD_Message_X
	 *      (p_AD_Message IN VARCHAR, p_AD_Language IN VARCHAR)
	 *      ...
	 *      END AD_Message_X;
	 *  =>
	 *      CREATE FUNCTION AD_Message_X
	 *      (VARCHAR, VARCHAR)
	 *      RETURNS VARCHAR AS '
	 *      DECLARE
	 *      p_AD_Message ALIAS FOR $1;
	 *      p_AD_Language ALIAS FOR $2;
	 *      ....
	 *      END;
	 *      ' LANGUAGE 'plpgsql';
	 *  </pre>
	 *  @param sqlStatement
	 *  @return CREATE and DROP Function statement
	 */
	private ArrayList<String> convertProcedure (String sqlStatement)
	{
		ArrayList<String> result = new ArrayList<String>();
		//  Convert statement - to avoid handling contents of comments
		String stmt = converSimpleStatement(sqlStatement);
		//  Double quotes '
		stmt = Pattern.compile("'").matcher(stmt).replaceAll("''");
		//  remove OR REPLACE
		int orReplacePos = stmt.toUpperCase().indexOf(" OR REPLACE ");
		if (orReplacePos != -1)
			stmt = "CREATE" + stmt.substring(orReplacePos+11);

		//  Line separators
		String match =
			  "(\\([^\\)]*\\))"                 //  (.) Parameter
			+ "|(\\bRETURN \\w+ (AS)|(IS))"     //  RETURN CLAUSE
			+ "|(;)"                            //  Statement End
			//  Nice to have - for readability
			+ "|(\\bBEGIN\\b)"                  //  BEGIN
			+ "|(\\bTHEN\\b)"
			+ "|(\\bELSE\\b)"
			+ "|(\\bELSIF\\b)";
		Matcher m = Pattern.compile(match, Pattern.CASE_INSENSITIVE).matcher(stmt);

		StringBuffer sb = new StringBuffer();
		//  First group -> ( )
		//  CREATE OR REPLACE FUNCTION AD_Message_Get ( p_AD_Message IN VARCHAR, p_AD_Language IN VARCHAR)
		//  CREATE FUNCTION AD_Message_Get (VARCHAR, VARCHAR)
		m.find();
		m.appendReplacement(sb, "");
		String name = sb.substring(6).trim();
		StringBuffer signature = new StringBuffer();
		//
		String group = m.group().trim();
	//	log.info("Group: " + group);
		StringBuffer alias = new StringBuffer();
		//  Parameters
		if (group.startsWith("(") && group.endsWith(")"))
		{
			//  Default not supported
			if (group.toUpperCase().indexOf(" DEFAULT ") != -1)
			{
				String error = "DEFAULT in Parameter not supported";
				log.info (error);
				m_conversionError = error;
				return result;
			}
			signature.append("(");
			if (group.length() > 2)
			{
				group = group.substring(1,group.length()-1);
				//  Paraneters are delimited by ,
				String[] parameters = group.split(",");
				for (int i = 0; i < parameters.length; i++)
				{
					if (i != 0)
						signature.append(", ");
					//  name ALIAS FOR $1
					String p = parameters[i].trim();
					alias.append(p.substring(0,p.indexOf(" ")))
						.append(" ALIAS FOR $").append(i+1).append(";\n");
					//  Datatape
					signature.append(p.substring(p.lastIndexOf(" ")+1));
				}
			}
			signature.append(")");
			sb.append(signature);
		//	log.info("Alias: " + alias.toString());
		//	log.info("Signature: " + signature.toString());
		}
		//  No Parameters
		else
		{
			String error = "Missing Parameter ()";
			log.info (error);
			m_conversionError = error;
			return result;
		}
		sb.append("\n");
		//  Need to create drop statement
		if (orReplacePos != -1)
		{
			String drop = "DROP " + name + signature.toString();
		//	log.info(drop);
			result.add(drop);
		}
	//	log.info("1>" + sb.toString() + "<1");

		//  Second Group -> RETURN VARCHAR AS
		//  RETURNS VARCHAR AS
		m.find();
		group = m.group();
		m.appendReplacement(sb, "");
		if (group.startsWith("RETURN"))
			sb.append("RETURNS").append(group.substring(group.indexOf(" ")));
		sb.append(" '\nDECLARE\n")
			.append(alias);         //  add aliases here
	//	log.info("2>" + sb.toString() + "<2");

		//  remainder statements
		while (m.find())
		{
			String group2 = m.group();
			if (group2.indexOf('$') != -1)   //  Group character needs to be escaped
				group2 = Util.replace(group2, "$", "\\$");
			m.appendReplacement(sb, group2);
			sb.append("\n");
		}
		m.appendTail(sb);

		//  finish
		sb.append("' LANGUAGE 'plpgsql';");
	//	log.info(">" + sb.toString() + "<");
		result.add(sb.toString());
		//
		return result;
	}   //  convertProcedure

	/**
	 *  Convert Trigger.
	 *  <pre>
	 *      DROP FUNCTION emp_trgF();
	 *      CREATE FUNCTION emp_trg () RETURNS OPAQUE AS '....
	 *          RETURN NEW; ...
	 *          ' LANGUAGE 'plpgsql';
	 *      DROP TRIGGER emp_trg ON emp;
	 *      CREATE TRIGGER emp_trg BEFORE INSERT OR UPDATE ON emp
	 *      FOR EACH ROW EXECUTE PROCEDURE emp_trgF();
	 *  </pre>
	 *  @param sqlStatement
	 *  @return CREATE and DROP TRIGGER and associated Function statement
	 */
	private ArrayList<String> convertTrigger (String sqlStatement)
	{
		ArrayList<String> result = new ArrayList<String>();
		//  Convert statement - to avoid handling contents of comments
		String stmt = converSimpleStatement(sqlStatement);

		//  Trigger specific replacements
		stmt = Pattern.compile("\\bINSERTING\\b").matcher(stmt).replaceAll("TG_OP='INSERT'");
		stmt = Pattern.compile("\\bUPDATING\\b").matcher(stmt).replaceAll("TG_OP='UPDATE'");
		stmt = Pattern.compile("\\bDELETING\\b").matcher(stmt).replaceAll("TG_OP='DELETE'");
		stmt = Pattern.compile(":new.").matcher(stmt).replaceAll("NEW.");
		stmt = Pattern.compile(":old.").matcher(stmt).replaceAll("OLD.");

		//  Double quotes '
		stmt = Pattern.compile("'").matcher(stmt).replaceAll("''");
		//  remove OR REPLACE
		int orReplacePos = stmt.toUpperCase().indexOf(" OR REPLACE ");
		//  trigger Name
		int triggerPos = stmt.toUpperCase().indexOf(" TRIGGER ") + 9;
		String triggerName = stmt.substring(triggerPos);
		triggerName = triggerName.substring(0, triggerName.indexOf(" "));
		//  table name
		String tableName = stmt.substring(stmt.toUpperCase().indexOf(" ON ")+4);
		tableName = tableName.substring(0, tableName.indexOf(" "));

		//  Function Drop
		if (orReplacePos != -1)
		{
			String drop = "DROP FUNCTION " + triggerName + "F()";
		//	log.info(drop);
			result.add(drop);
		}

		//  Function & Trigger
		int pos = stmt.indexOf("DECLARE ");
		if (pos == -1)
			pos = stmt.indexOf("BEGIN ");
		String functionCode = stmt.substring(pos);
		StringBuffer triggerCode = new StringBuffer ("CREATE TRIGGER ");
		triggerCode.append(triggerName).append("\n")
			.append(stmt.substring(triggerPos+triggerName.length(), pos))
			.append("\nEXECUTE PROCEDURE ").append(triggerName).append("F();");

		//  Add NEW to existing Return   --> DELETE Trigger ?
		functionCode = Pattern.compile("\\bRETURN;", Pattern.CASE_INSENSITIVE)
			.matcher(functionCode)
			.replaceAll("RETURN NEW;");
		//  Add final return and change name
		functionCode = Pattern.compile("\\bEND " + triggerName + ";", Pattern.CASE_INSENSITIVE)
			.matcher(functionCode)
			.replaceAll("\nRETURN NEW;\nEND " + triggerName + "F;");

		//  Line separators
		String match =
			  "(\\(.*\\))"                      //  (.) Parameter
			+ "|(;)"                            //  Statement End
			//  Nice to have - for readability
			+ "|(\\bBEGIN\\b)"                  //  BEGIN
			+ "|(\\bTHEN\\b)"
			+ "|(\\bELSE\\b)"
			+ "|(\\bELSIF\\b)";
		Matcher m = Pattern.compile(match, Pattern.CASE_INSENSITIVE).matcher(functionCode);

		//  Function Header
		StringBuffer sb = new StringBuffer("CREATE FUNCTION ");
		sb.append(triggerName).append("F() RETURNS OPAQUE AS '\n");

		//  remainder statements
		while (m.find())
		{
			String group = m.group();
			if (group.indexOf('$') != -1)   //  Group character needs to be escaped
				group = Util.replace(group, "$", "\\$");
			m.appendReplacement(sb, group);
			sb.append("\n");
		}
		m.appendTail(sb);

		//  finish Function
		sb.append("' LANGUAGE 'plpgsql';");
	//	log.info(">" + sb.toString() + "<");
		result.add(sb.toString());

		//  Trigger Drop
		if (orReplacePos != -1)
		{
			String drop = "DROP TRIGGER " + triggerName.toLowerCase() + " ON " + tableName;
	//		log.info(drop);
			result.add(drop);
		}

		//  Trigger
		//  Remove Column references OF ... ON
		String trigger = Pattern.compile("\\sOF.*ON\\s")
			.matcher(triggerCode)
			.replaceAll(" ON ");
	//	log.info(trigger);
		result.add(trigger);

		//
		return result;
	}   //  convertTrigger

	/**
	 *  Convert View.
	 *  Handle CREATE OR REPLACE
	 *  @param sqlStatement
	 *  @return converted statement(s)
	 */
	private ArrayList<String> convertView (String sqlStatement)
	{
		ArrayList<String> result = new ArrayList<String>();
		String stmt = converSimpleStatement(sqlStatement);

		//  remove OR REPLACE
		int orReplacePos = stmt.toUpperCase().indexOf(" OR REPLACE ");
		if (orReplacePos != -1)
		{
			int index = stmt.indexOf(" VIEW ");
			int space = stmt.indexOf(' ', index+6);
			String drop = "DROP VIEW " + stmt.substring(index+6, space);
			result.add(drop);
			//
			String create = "CREATE" + stmt.substring(index);
			result.add(create);
		}
		else    //  simple statement
			result.add(stmt);
		return result;
	}   //  convertView

	
	/**************************************************************************
	 *  Converts Decode, Outer Join and Sequence.
	 *  <pre>
	 *      DECODE (a, 1, 'one', 2, 'two', 'none')
	 *       => CASE WHEN a = 1 THEN 'one' WHEN a = 2 THEN 'two' ELSE 'none' END
	 *
	 *      AD_Error_Seq.nextval
	 *       => nextval('AD_Error_Seq')
	 *
	 *      RAISE_APPLICATION_ERROR (-20100, 'Table Sequence not found')
	 *       => RAISE EXCEPTION 'Table Sequence not found'
	 *
	 *  </pre>
	 *  @param sqlStatement
	 *  @return converted statement
	 */
	private String convertComplexStatement(String sqlStatement)
	{
		String retValue = sqlStatement;
		StringBuffer sb = null;

		//  Convert all decode parts
		while (retValue.indexOf("DECODE(") != -1 || retValue.indexOf("DECODE (") != -1) //jz temp fix //TODO literal analysis
			retValue = convertDecode(retValue);

		/**
		 * Sequence Handling --------------------------------------------------
		 *  AD_Error_Seq.nextval
		 *  => nextval('AD_Error_Seq')
		 */
		Matcher m = Pattern.compile("\\w+\\.(nextval)|(curval)", Pattern.CASE_INSENSITIVE)
			.matcher(retValue);
		sb = new StringBuffer();
		while (m.find())
		{
			String group = m.group();
		//	System.out.print("-> " + group);
			int pos = group.indexOf(".");
			String seqName = group.substring(0,pos);
			String funcName = group.substring(pos+1);
			group = funcName + "('" + seqName + "')";
		//	log.info(" => " + group);
			if (group.indexOf('$') != -1)   //  Group character needs to be escaped
				group = Util.replace(group, "$", "\\$");
			m.appendReplacement(sb, group);
		}
		m.appendTail(sb);
		retValue = sb.toString();

		/**
		 * RAISE --------------------------------------------------------------
		 *  RAISE_APPLICATION_ERROR (-20100, 'Table Sequence not found')
		 *  => RAISE EXCEPTION 'Table Sequence not found'
		 */
		m = Pattern.compile("RAISE_APPLICATION_ERROR\\s*\\(.+'\\)", Pattern.CASE_INSENSITIVE)
			.matcher(retValue);
		sb = new StringBuffer();
		while (m.find())
		{
			String group = m.group();
			System.out.print("-> " + group);
			String result = "RAISE EXCEPTION " + group.substring(group.indexOf('\''), group.lastIndexOf('\'')+1);
			log.info(" => " + result);

			if (result.indexOf('$') != -1)   //  Group character needs to be escaped
				result = Util.replace(result, "$", "\\$");
			m.appendReplacement(sb, result);
		}
		m.appendTail(sb);
		retValue = sb.toString();

		//  Truncate Handling -------------------------------------------------
		//jz dead loop if no need to convert it
		int it = retValue.indexOf("TRUNC(");
		while (it != -1)
		{
			retValue = convertTrunc (retValue);
			it = retValue.indexOf("TRUNC(", it + 1);
		}

		//  Outer Join Handling -----------------------------------------------
		int index = retValue.indexOf("SELECT ");
		if (index != -1 && retValue.indexOf("(+)", index) != -1)
			retValue = convertOuterJoin(retValue);

		return retValue;
	}   //  convertComplexStatement

	
	/**************************************************************************
	 *  Converts Decode.
	 *  <pre>
	 *      DECODE (a, 1, 'one', 2, 'two', 'none')
	 *       => CASE WHEN a = 1 THEN 'one' WHEN a = 2 THEN 'two' ELSE 'none' END
	 *  </pre>
	 *  @param sqlStatement
	 *  @return converted statement
	 */
	private String convertDecode(String sqlStatement)
	{
	//	log.info("DECODE<== " + sqlStatement);
		String statement = sqlStatement;
		StringBuffer sb = new StringBuffer("CASE");

		int index = statement.indexOf("DECODE");
		String firstPart = statement.substring(0,index);

		//  find the opening (
		index = statement.indexOf('(', index);
		statement = statement.substring(index+1);

		//  find the expression "a" - find first , ignoring ()
		index = Util.findIndexOf (statement, ',');
		String expression = statement.substring(0, index).trim();
	//	log.info("Expression=" + expression);

		//  Pairs "1, 'one',"
		statement = statement.substring(index+1);
		index = Util.findIndexOf (statement, ',');
		while (index != -1)
		{
			String first = statement.substring(0, index);
			char cc = statement.charAt(index);
			statement = statement.substring(index+1);
		//	log.info("First=" + first + ", Char=" + cc);
			//
			boolean error = false;
			if (cc == ',')
			{
				index = Util.findIndexOf (statement, ',',')');
				if (index == -1)
					error = true;
				else
				{
					String second = statement.substring(0, index);
					sb.append(" WHEN ").append(expression).append("=").append(first.trim())
						.append(" THEN ").append(second.trim());
		//			log.info(">>" + sb.toString());
					statement = statement.substring(index+1);
					index = Util.findIndexOf (statement, ',',')');
				}
			}
			else if (cc == ')')
			{
				sb.append(" ELSE ").append(first.trim()).append(" END");
		//		log.info(">>" + sb.toString());
				index = -1;
			}
			else
				error = true;
			if (error)
			{
				log.log(Level.SEVERE, "SQL=(" + sqlStatement
					+ ")\n====Result=(" + sb.toString()
					+ ")\n====Statement=(" + statement
					+ ")\n====First=(" + first
					+ ")\n====Index=" + index);
				m_conversionError = "Decode conversion error";
			}
		}
		sb.append(statement);
		sb.insert(0, firstPart);
	//	log.info("DECODE==> " + sb.toString());
		return sb.toString();
	}	//  convertDecode

	
	/**************************************************************************
	 *  Convert Outer Join.
	 *  Converting joins can ve very complex when multiple tables/keys are involved.
	 *  The main scenarios supported are two tables with multiple key columns
	 *  and multiple tables with single key columns.
	 *  <pre>
	 *      SELECT a.Col1, b.Col2 FROM tableA a, tableB b WHERE a.ID=b.ID(+)
	 *      => SELECT a.Col1, b.Col2 FROM tableA a LEFT OUTER JOIN tableB b ON (a.ID=b.ID)
	 *
	 *      SELECT a.Col1, b.Col2 FROM tableA a, tableB b WHERE a.ID(+)=b.ID
	 *      => SELECT a.Col1, b.Col2 FROM tableA a RIGHT OUTER JOIN tableB b ON (a.ID=b.ID)
	 *  Assumptions:
	 *  - No outer joins in sub queries (ignores sub-queries)
	 *  - OR condition ignored (not sure what to do, should not happen)
	 *  Limitations:
	 *  - Parameters for outer joins must be first - as sequence of parameters changes
	 *  </pre>
	 *  @param sqlStatement
	 *  @return converted statement
	 */
	private String convertOuterJoin (String sqlStatement)
	{
		boolean trace = false;
		//
		int fromIndex = Util.findIndexOf (sqlStatement.toUpperCase(), " FROM ");
		int whereIndex = Util.findIndexOf(sqlStatement.toUpperCase(), " WHERE ");
		int endWhereIndex = Util.findIndexOf(sqlStatement.toUpperCase(), " GRPUP BY ");
		if (endWhereIndex == -1)
			endWhereIndex = Util.findIndexOf(sqlStatement.toUpperCase(), " ORDER BY ");
		if (endWhereIndex == -1)
			endWhereIndex = sqlStatement.length();
		//
		if (trace)
		{
			log.info("OuterJoin<== " + sqlStatement);
		//	log.info("From=" + fromIndex + ", Where=" + whereIndex + ", End=" + endWhereIndex + ", Length=" + sqlStatement.length());
		}
		//
		String selectPart = sqlStatement.substring(0, fromIndex);
		String fromPart = sqlStatement.substring(fromIndex, whereIndex);
		String wherePart = sqlStatement.substring(whereIndex, endWhereIndex);
		String rest = sqlStatement.substring(endWhereIndex);

		//  find/remove all (+) from WHERE clase ------------------------------
		String newWherePart = wherePart;
		ArrayList<String> joins = new ArrayList<String>();
		int pos = newWherePart.indexOf("(+)");
		while (pos != -1)
		{
			//  find starting point
			int start = newWherePart.lastIndexOf(" AND ", pos);
			int startOffset = 5;
			if (start == -1)
			{
				start = newWherePart.lastIndexOf(" OR ", pos);
				startOffset = 4;
			}
			if (start == -1)
			{
				start = newWherePart.lastIndexOf("WHERE ", pos);
				startOffset = 6;
			}
			if (start == -1)
			{
				String error = "Start point not found in clause " + wherePart;
				log.severe(error);
				m_conversionError = error;
				return sqlStatement;
			}
			//  find end point
			int end = newWherePart.indexOf(" AND ", pos);
			if (end == -1)
				end = newWherePart.indexOf(" OR ", pos);
			if (end == -1)
				end = newWherePart.length();
		//	log.info("<= " + newWherePart + " - Start=" + start + "+" + startOffset + ", End=" + end);

			//  extract condition
			String condition = newWherePart.substring(start+startOffset, end);
			joins.add(condition);
			if (trace)
				log.info("->" + condition);
			//  new WHERE clause
			newWherePart = newWherePart.substring(0, start) + newWherePart.substring(end);
		//	log.info("=> " + newWherePart);
			//
			pos = newWherePart.indexOf("(+)");
		}
		//  correct beginning
		newWherePart = newWherePart.trim();
		if (newWherePart.startsWith("AND "))
			newWherePart = "WHERE" + newWherePart.substring(3);
		else if (newWherePart.startsWith("OR "))
			newWherePart = "WHERE" + newWherePart.substring(2);
		if (trace)
			log.info("=> " + newWherePart);

		//  Correct FROM clause -----------------------------------------------
		//  Disassemble FROM
		String[] fromParts = fromPart.trim().substring(4).split(",");
		HashMap<String,String> fromAlias = new HashMap<String,String>();      //  tables to be processed
		HashMap<String,String> fromLookup = new HashMap<String,String>();     //  used tabled
		for (String element : fromParts) {
			String entry = element.trim();
			String alias = entry;   //  no alias
			String table = entry;
			int aPos = entry.lastIndexOf(' ');
			if (aPos != -1)
			{
				alias = entry.substring(aPos+1);
				table = entry.substring(0, entry.indexOf(' ')); // may have AS
			}
			fromAlias.put(alias, table);
			fromLookup.put(alias, table);
			if (trace)
				log.info("Alias=" + alias + ", Table=" + table);
		}

		/** Single column
			SELECT t.TableName, w.Name FROM AD_Table t, AD_Window w
			WHERE t.AD_Window_ID=w.AD_Window_ID(+)
			--	275 rows
			SELECT t.TableName, w.Name FROM AD_Table t
			LEFT OUTER JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)

			SELECT t.TableName, w.Name FROM AD_Table t, AD_Window w
			WHERE t.AD_Window_ID(+)=w.AD_Window_ID
			--	239 rows
			SELECT t.TableName, w.Name FROM AD_Table t
			RIGHT OUTER JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)

		**  Multiple columns
			SELECT tn.Node_ID,tn.Parent_ID,tn.SeqNo,tb.IsActive
			FROM AD_TreeNode tn, AD_TreeBar tb
			WHERE tn.AD_Tree_ID=tb.AD_Tree_ID(+) AND tn.Node_ID=tb.Node_ID(+)
			  AND tn.AD_Tree_ID=10
			--  235 rows
			SELECT	tn.Node_ID,tn.Parent_ID,tn.SeqNo,tb.IsActive
			FROM AD_TreeNode tn LEFT OUTER JOIN AD_TreeBar tb
			  ON (tn.Node_ID=tb.Node_ID AND tn.AD_Tree_ID=tb.AD_Tree_ID AND tb.AD_User_ID=0)
			WHERE tn.AD_Tree_ID=10

			SELECT tn.Node_ID,tn.Parent_ID,tn.SeqNo,tb.IsActive
			FROM AD_TreeNode tn, AD_TreeBar tb
			WHERE tn.AD_Tree_ID=tb.AD_Tree_ID(+) AND tn.Node_ID=tb.Node_ID(+)
			 AND tn.AD_Tree_ID=10 AND tb.AD_User_ID(+)=0
			--  214 rows
			SELECT tn.Node_ID,tn.Parent_ID,tn.SeqNo,tb.IsActive
			FROM AD_TreeNode tn LEFT OUTER JOIN AD_TreeBar tb
			  ON (tn.Node_ID=tb.Node_ID AND tn.AD_Tree_ID=tb.AD_Tree_ID AND tb.AD_User_ID=0)
			WHERE tn.AD_Tree_ID=10

		 */
		StringBuffer newFrom = new StringBuffer ();
		for (int i = 0; i < joins.size(); i++)
		{
			Join first = new Join (joins.get(i));
			first.setMainTable(fromLookup.get(first.getMainAlias()));
			fromAlias.remove(first.getMainAlias());     //  remove from list
			first.setJoinTable(fromLookup.get(first.getJoinAlias()));
			fromAlias.remove(first.getJoinAlias());     //  remove from list
			if (trace)
				log.info("-First: " + first);
			//
			if (newFrom.length() == 0)
				newFrom.append(" FROM ");
			else
				newFrom.append(", ");
			newFrom.append(first.getMainTable()).append(" ").append(first.getMainAlias())
				.append(first.isLeft() ? " LEFT" : " RIGHT").append(" OUTER JOIN ")
				.append(first.getJoinTable()).append(" ").append(first.getJoinAlias())
				.append(" ON (").append(first.getCondition());
			//  keep it open - check for other key comparisons
			for (int j = i+1; j < joins.size(); j++)
			{
				Join second = new Join (joins.get(j));
				second.setMainTable(fromLookup.get(second.getMainAlias()));
				second.setJoinTable(fromLookup.get(second.getJoinAlias()));
				if ((first.getMainTable().equals(second.getMainTable())
						&& first.getJoinTable().equals(second.getJoinTable()))
					|| second.isConditionOf(first) )
				{
					if (trace)
						log.info("-Second/key: " + second);
					newFrom.append(" AND ").append(second.getCondition());
					joins.remove(j);                        //  remove from join list
					fromAlias.remove(first.getJoinAlias()); //  remove from table list
					//----
					for (int k = i+1; k < joins.size(); k++)
					{
						Join third = new Join (joins.get(k));
						third.setMainTable(fromLookup.get(third.getMainAlias()));
						third.setJoinTable(fromLookup.get(third.getJoinAlias()));
						if (third.isConditionOf(second))
						{
							if (trace)
								log.info("-Third/key: " + third);
							newFrom.append(" AND ").append(third.getCondition());
							joins.remove(k);                            //  remove from join list
							fromAlias.remove(third.getJoinAlias());     //  remove from table list
						}
						else if (trace)
							log.info("-Third/key-skip: " + third);
					}
				}
				else if (trace)
					log.info("-Second/key-skip: " + second);
			}
			newFrom.append(")");    //  close ON
			//  check dependency on first table
			for (int j = i+1; j < joins.size(); j++)
			{
				Join second = new Join (joins.get(j));
				second.setMainTable(fromLookup.get(second.getMainAlias()));
				second.setJoinTable(fromLookup.get(second.getJoinAlias()));
				if (first.getMainTable().equals(second.getMainTable()))
				{
					if (trace)
						log.info("-Second/dep: " + second);
					//   FROM (AD_Field f LEFT OUTER JOIN AD_Column c ON (f.AD_Column_ID = c.AD_Column_ID))
					//  LEFT OUTER JOIN AD_FieldGroup fg ON (f.AD_FieldGroup_ID = fg.AD_FieldGroup_ID),
					newFrom.insert(6, '(');     //  _FROM ...
					newFrom.append(')');        //  add parantesis on previous relation
					//
					newFrom.append(second.isLeft() ? " LEFT" : " RIGHT").append(" OUTER JOIN ")
						.append(second.getJoinTable()).append(" ").append(second.getJoinAlias())
						.append(" ON (").append(second.getCondition());
					joins.remove(j);                            //  remove from join list
					fromAlias.remove(second.getJoinAlias());    //  remove from table list
					//  additional join colums would come here
					newFrom.append(")");    //  close ON
					//----
					for (int k = i+1; k < joins.size(); k++)
					{
						Join third = new Join (joins.get(k));
						third.setMainTable(fromLookup.get(third.getMainAlias()));
						third.setJoinTable(fromLookup.get(third.getJoinAlias()));
						if (second.getJoinTable().equals(third.getMainTable()))
						{
							if (trace)
								log.info("-Third-dep: " + third);
							//   FROM ((C_BPartner p LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID))
							//  LEFT OUTER JOIN C_BPartner_Location l ON (p.C_BPartner_ID=l.C_BPartner_ID))
							//  LEFT OUTER JOIN C_Location a ON (l.C_Location_ID=a.C_Location_ID)
							newFrom.insert(6, '(');     //  _FROM ...
							newFrom.append(')');        //  add parantesis on previous relation
							//
							newFrom.append(third.isLeft() ? " LEFT" : " RIGHT").append(" OUTER JOIN ")
								.append(third.getJoinTable()).append(" ").append(third.getJoinAlias())
								.append(" ON (").append(third.getCondition());
							joins.remove(k);                            //  remove from join list
							fromAlias.remove(third.getJoinAlias());     //  remove from table list
							//  additional join colums would come here
							newFrom.append(")");    //  close ON
						}
						else if (trace)
							log.info("-Third-skip: " + third);
					}
				}
				else if (trace)
					log.info("-Second/dep-skip: " + second);
			}   //  dependency on first table
		}
		//  remaining Tables
		Iterator<String> it = fromAlias.keySet().iterator();
		while (it.hasNext())
		{
			Object alias = it.next();
			Object table = fromAlias.get(alias);
			newFrom.append(", ").append(table);
			if (!table.equals(alias))
				newFrom.append(" ").append(alias);
		}
		if (trace)
			log.info(newFrom.toString());
		//
		StringBuffer retValue = new StringBuffer (sqlStatement.length()+20);
		retValue.append(selectPart)
			.append(newFrom).append(" ")
			.append(newWherePart).append(rest);
		//
		if (trace)
			log.info("OuterJoin==> " + retValue.toString());
		return retValue.toString();
	}   //  convertOuterJoin

	/**
	 *  Convert TRUNC.
	 *  Assumed that it is used for date only!
	 *  @param sqlStatement
	 *  @return converted statement
	 */
	private String convertTrunc (String sqlStatement)
	{
		//jz   impl trunc() in sqlj    return Util.replace(sqlStatement, "TRUNC(", "convert(date,");
		return sqlStatement;
		/**
	 *  <pre>
	 *      TRUNC(myDate)
	 *      => DATE_Trunc('day',myDate)
	 *
	 *      TRUNC(myDate,'oracleFormat')
	 *      => DATE_Trunc('pgFormat',myDate)
	 *
	 *      Oracle          =>  PostgreSQL  (list not complete!)
	 *          Q               quarter
	 *          MM              month
	 *          DD              day
	 *          DAY (week)
	 *      Spacial handling of DAY,DY  (Starting dat of the week)
	 *      => DATE_Trunc('day',($1-DATE_PART('dow',$1)));
	 *  </pre>
		int index = sqlStatement.indexOf("TRUNC");
		String beforeStatement = sqlStatement.substring(0, index);
		String afterStatement = sqlStatement.substring(index);
		afterStatement = afterStatement.substring(afterStatement.indexOf('(')+1);
		index = Util.findIndexOf(afterStatement, ')');
		String temp = afterStatement.substring(0, index).trim();
		afterStatement = afterStatement.substring(index+1);
	//	log.info("Trunc<== " + temp);
		StringBuffer retValue = new StringBuffer ("DATE_Trunc(");   //  lower case otherwise endless-loop
		if (temp.indexOf(',') == -1)
			retValue.append("'day',").append(temp);
		else    //  with format
		{
			int pos = temp.indexOf(',');
			String variable = temp.substring(0, pos).trim();
			String format = temp.substring(pos+1).trim();
			if (format.equals("'Q'"))
				retValue.append("'quarter',").append(variable);
			else if (format.equals("'MM'"))
				retValue.append("'month',").append(variable);
			else if (format.equals("'DD'"))
				retValue.append("'day',").append(variable);
			else if (format.equals("'DY'") || format.equals("'DAY'"))
				retValue.append("'day',(").append(variable)
					.append("-DATE_PART('dow',").append(variable).append("))");
			else
			{
				log.severe("TRUNC format not supported: " + format);
				retValue.append("'day',").append(variable);
			}
		}
		retValue.append(')');
	//	log.info("Trunc==> " + retValue.toString());
		//
		retValue.insert(0, beforeStatement);
		retValue.append(afterStatement);
		return retValue.toString();
		**/
	}   //  convertTrunc
}   //  Convert
