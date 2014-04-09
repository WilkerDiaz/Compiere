/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.process;

//import java.io.BufferedReader;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.db.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *  Import view definition from DB into Compiere Application Dictionary
 *  This code is similar to ViewImport.java in that they both create view
 *  definition in Compiere Application Dictionary based on information from
 *  an external source. The difference is that ViewImport's source is a SQL
 *  file. 
 */
public class ViewImportFromDB extends SvrProcess
{
	
    private static String replace(String sin, char oldChar, char newChar) {
    	if (oldChar != newChar) {
    	    int len = sin.length();
    	    int i = -1;
    	    char[] val = sin.toCharArray(); /* avoid getfield opcode */
    	    int off = 0;   /* avoid getfield opcode */

    	    while (++i < len) {
    		if (val[off + i] == oldChar) {
    		    break;
    		}
    	    }
    	    if (i < len) {
    		char buf[] = new char[len];
    		for (int j = 0 ; j < i ; j++) {
    		    buf[j] = val[off+j];
    		}
    		while (i < len) {
    		    char c = val[off + i];
    		    buf[i] = (c == oldChar) ? newChar : c;
    		    i++;
    		}
    		return new String(buf);
    	    }
    	}
    	return sin;
        }

	
	//private static CLogger	log	= CLogger.getCLogger (CObjectSetImpl.class);

	//private String dbType = null;
	//private String dbHost = null;
	//private String dbPort = null;
	//private String dbName = null;
	//private String userId = null;
	//private String passwd = null;
//	private String SQLfile = null;
	private String p_EntityType = null;
	private int p_AD_Table_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	public void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("EntityType"))
				p_EntityType = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


    private String trim(String s) 
    {
    	int len = s.length();
    	int count = len;
    	int st = 0;
    	int off = 0;      /* avoid getfield opcode */
    	char[] val = s.toCharArray();    /* avoid getfield opcode */

    	while ((st < len) && (val[off + st] <= ' ')) {
    	    st++;
    	}
    	while ((st < len) && (val[off + len - 1] <= ' ')) {
    	    len--;
    	}
    	return ((st > 0) || (len < count)) ? s.substring(st, len) : s;
  }



	/**
	*   findNext
 	*   A utility method to find the first occurence of a keyword within a SQL
	*   query. When searching for key, this method will ignore the values that 
    *   are surrounded by comment tags and quotes. It will also ignore values
    *   that are surrounded by parantheses (subqueries).
	*	@param query - the SQL query
	*   @param keyword - the keyword that we are trying to find 
	*	@return the index of source at which key is found
	*	@throws
	*/
	private int findNext(String query, String keyword){
		
		int Comments = 0;
		int Quotes = 0;
		int Parantheses = 0;
				
		for (int j=0; j<query.length(); j++){
			if (Comments > 0){
				if (!query.startsWith("*/",j)) continue;
				Comments--; j++; continue;
			}
			if (Quotes > 0){
				if (!query.startsWith("'",j)) continue;
				Quotes --; continue;
			}
			if (query.startsWith("/*",j)) {Comments ++; j++; continue;}
			if (query.startsWith("'",j)) {Quotes ++; continue;}

			if (query.charAt(j)=='(') {Parantheses++; continue;}
			if (query.charAt(j)==')') {Parantheses--; continue;}
			if (Parantheses==0 && query.startsWith(keyword, j)){
				return j;
			}
		}

		return -1;
	}
	
	/**
	*   splitUnions
 	*   A utility method to split the unions in a view query. The method looks
 	*   for '('s before the first 'select' keyword indicating that the view
 	*   definition has multiple unions enclosed within the sets of '(' and ')'
	*   The method replaces each such '(' with ' ', also the corresponding
	*   ending ')' with ' ', thus enabling the SQL query to be split by word UNION
	*   without having mismatching ')' or '(' in the individual split parts. 
    *   The method ignores the '(' or ')' enclosed withing quotes or comments.          
	*	@param query - the SQL query
	*	@return array of queries to be 'union' ed
	*	@throws
	*/
	private String[] splitUnions(String query){
		
		String origQuery = query;
		int iSelect = query.toLowerCase().indexOf("select ");
		int iPara=  query.indexOf('(');
		
		if(!((iPara>=0) && (iSelect>0) && (iSelect>iPara)))
		{
			String[] selects = query.split("UNION ");
			return selects;			
		}	
		
		boolean err=false;
		int Comments = 0;
		int Quotes = 0;	
		/* Starting at the index of keyword 'SELECT' in the query, traverse
		 * the String backwards to find a '(' that is not part of a comment or
		 * quotes
		 */
		for(int j=iSelect-1;j>=0;j--)
		{		
						
		
			if (Comments > 0){
				if (!((j >0) && query.startsWith("/*",j-1)))						 
					continue;
				Comments--;	j--;continue;
			}
			if (Quotes > 0){
				if (!query.startsWith("'",j)) continue;
				Quotes --; continue;
			}
			if ((j >0) && query.startsWith("*/",j-1))
			{
				Comments ++; 
				j--; 
				continue;
			}
			if (query.startsWith("'",j)) 
			{
				Quotes ++; 
				continue;
			}

			if (query.charAt(j)==')') {
				//unexpected closing paranthesis before first SELECT ???			
				err=true; break;
			}			
			if (query.charAt(j)=='(') 
			{
				/* Found the opening '(', now look for closing ')' by 
				 * traversing the string forward from position of the 
				 * keyword 'SELECT' in the query				 
				 */
				int iWorkPara=j;
				int iWorkParaMatch=-1;
				int Comments2 = 0;
				int Quotes2 = 0;
				int Parantheses2 = 0;
				//Find matching closing parantheses
				for(int k=iSelect+8;k<query.length();k++)
				{		
					
				
					if (Comments2 > 0){
						if (!((k < query.length()-1) && query.startsWith("*/",k))) 
							continue;
						Comments2--; 
						k++; 
						continue;
					}
					if (Quotes2 > 0){
						if (!query.startsWith("'",k)) continue;
						Quotes2 --; 
						continue;
					}		
					
					
					if ((k < query.length()-1) && query.startsWith("/*",k)) 
					{
						Comments2++; 
						k++; 
						continue;
					}
					if (query.startsWith("'",k)) 
					{
						Quotes2 ++; 
						continue;
					}
					
					if (query.charAt(k)=='(') {Parantheses2++; continue;}
					if (query.charAt(k)==')')
					{
						if (Parantheses2==0)
						{	
							iWorkParaMatch=k;
							break;
						}
						else
						{
							Parantheses2--; continue;
						}
					}
							
				}
				
				if(iWorkPara>=0 && iWorkParaMatch>0 && iWorkPara < iWorkParaMatch)
				{
					StringBuffer sb= new StringBuffer(query);			
					sb.setCharAt(iWorkPara, ' ');
					sb.setCharAt(iWorkParaMatch, ' ');					
					query=sb.toString();
					continue;
				}
				else
				{	err=true;					
					break;	
				}
			}			
		}		
		if(err)
		{
			//use the origQuery to split by "UNION" as variable query might have been modified
			String[] selects = origQuery.split("UNION ");
			return selects;	
		}		
		String[] selects = query.split("UNION ");
		return selects;		
	}
	
	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	public String doIt() throws Exception
	{
		p_AD_Table_ID = getRecord_ID();
		if (p_AD_Table_ID == 0)
			throw new CompiereSystemException("@NotFound@ @AD_Table_ID@ " + p_AD_Table_ID);

		log.info("AD_Table_ID="+p_AD_Table_ID+", EntityType="+p_EntityType);
		
		// get view definition from DB
		CompiereDatabase db = DB.getDatabase();
		Trx trx = Trx.get("getDatabaseMetaData");
		DatabaseMetaData md = trx.getConnection().getMetaData();
		String catalog = db.getCatalog();
		String schema = db.getSchema();
		MTable view = new MTable (getCtx(), p_AD_Table_ID, get_TrxName());
		if (view == null || view.get_ID() == 0)
			throw new CompiereSystemException("@NotFound@ @AD_Table_ID@ " + p_AD_Table_ID);
		String tableName = view.getTableName();		
		if (view!=null && !view.isView())
		{
			throw new CompiereSystemException("Cannot create view "+tableName+" because there already exists a table with the same name");
		}		
		String DBTableName = tableName;
		p_EntityType = view.get_EntityType();
		log.info("View="+tableName +", EntityType=" + p_EntityType);
		if (md.storesUpperCaseIdentifiers())
			DBTableName = tableName.toUpperCase();
		if (md.storesLowerCaseIdentifiers())
			DBTableName = tableName.toLowerCase();

		String[] types = {"VIEW"};		
		ResultSet rs = md.getTables(catalog, schema, DBTableName, types);	
		
		if (rs==null)
			log.severe("There is no database view with a name of "+DBTableName);
		
		// get view column definition from DB and create/update column definition in AD
		// populate colName with the list of view column name 
		rs = md.getColumns(catalog, schema, DBTableName, null);
		TableCreateColumns tcc = new TableCreateColumns();
		ArrayList<String> colName = tcc.addTableColumn(getCtx(), rs, view, p_EntityType);  

		trx.close();
		
		// get the view definition 
		String command = null;		
		PreparedStatement pstmt = null;
		ResultSet rsv = null;
		try {
			String sql = null;
			if (DB.isOracle() || DB.isOracleXE()){
				sql = "SELECT TEXT FROM USER_VIEWS WHERE VIEW_NAME LIKE ?";
			} else if (DB.isPostgreSQL()) {
				sql = "SELECT DEFINITION FROM PG_VIEWS WHERE VIEWNAME LIKE ?";
			} else {
				log.severe("This database is not yet supported");
			}
			pstmt = DB.prepareStatement(sql,(Trx) null);
			pstmt.setString(1, DBTableName);			
			rsv = pstmt.executeQuery();
			if (rsv.next()){
				command = rsv.getString(1);
			} else {	
				log.severe("There is no database view with a name of "+DBTableName);
			}
		}
		catch (Exception e){ 
			log.severe("Unable to retrieve view definition for "+DBTableName);
		}
		finally {
			if (pstmt!=null) pstmt.close();
			if (rsv!=null) rsv.close();
		}
				
		// parse the view definition and create the corresponding Compiere Application
		// Dictionary entries. This portion is similar to the one from ViewImport.java.
		// TODO: Refactor this portion of the code so it can be shared with ViewImport.java
		int viewNum = 0;		
		try
		{
			// clean the view definition
			int ir = command.indexOf('\t');
			while (ir>0)
			{
				command = replace(command, '\t', ' ');
				ir = command.indexOf('\t');
			}		

			ir = command.indexOf('\n');
			while (ir>0)
			{
				command = replace(command, '\n', ' ');
				ir = command.indexOf('\n');
			}
			
			//Remove the ending semicolon
			if (DB.isPostgreSQL())
			{
				command=command.trim();
				if(command.endsWith(";"))
				{
					command = command.substring(0,command.length()-1);				
				}
			}

			//clean view components and their columns
			String vcdel = "DELETE FROM AD_ViewComponent WHERE (AD_Table_ID, AD_Client_ID) IN (SELECT AD_Table_ID, AD_Client_ID FROM AD_Table WHERE TableName = '"+tableName+"')";
			try
			{
				DB.executeUpdate((Trx) null, vcdel);
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, vcdel, e);
				throw new Exception();
			}

			//insert into ad_viewComponent for each union part
			String[] selects;
			
			if(DB.isPostgreSQL())
			{
				selects=splitUnions(command);// currently does not handle other set operators (e.g. INTERSECT)				
			}
			else
				selects = command.split("UNION "); // currently does not handle other set operators (e.g. INTERSECT)
			
			for (int i=0; i<selects.length; i++)
			{
				int iFrom = findNext(selects[i]," FROM ");
				if (iFrom < 0)
				{
					log.severe("No from clause from the SQL: "+command);
					throw new Exception();
				}
				ArrayList<String> colSQL = new ArrayList<String>();
				ArrayList<String> colType = new ArrayList<String>();
				int isel = selects[i].indexOf("SELECT ");
				String colstr = selects[i].substring(isel+7, iFrom);
				colstr = colstr.concat(",");
				int iComma = findNext (colstr, ",");
				if (iComma == 0)	
				{
					log.severe("No view column from the SQL: "+command);
					throw new Exception();
				}
				int iPrevComma = -1;
				while (iComma != iPrevComma)
				{
					String column = colstr.substring(iPrevComma+1, iComma);
					int iAS = column.toUpperCase().lastIndexOf(" AS ");
					String cs = null;
					if (iAS>0)
					{
						cs = column.substring(0, iAS);
					}
					else
					{
						cs = column;
					}							
					cs = cs.trim();							
					if (cs.equalsIgnoreCase("NULLIF(1,1)"))
					{
						cs = null;
						colType.add("I");
					}
					else if (cs.equalsIgnoreCase("NULLIF('A','A')"))
					{
						cs = null;
						colType.add("V");
					}
					else
						colType.add(null);

					colSQL.add(cs);	

					iPrevComma = iComma;
					if (iPrevComma+1 < colstr.length()){
						int next = findNext (colstr.substring(iPrevComma+1),",");
						if (next > 0)
							iComma = iPrevComma+1 + next;
					}
				} // while (iComma != iPrevComma)

				String from = selects[i].substring(iFrom+1, selects[i].length());
				int iWH = from.indexOf(" WHERE ");
				String where = null;
				String others = null;
				int iGROUP = -1;
				int iORDER = -1;
				if (iWH>0)
				{
					where = from.substring(iWH+1, from.length());
					from = from.substring(0, iWH);
					iGROUP = where.indexOf(" GROUP BY ");
					iORDER = where.indexOf(" ORDER BY ");
					if (iORDER>0 && iGROUP==-1)
						iGROUP = iORDER;
					if (iGROUP>0)
					{
						others = where.substring(iGROUP+1, where.length());
						where = where.substring(0, iGROUP);
					}
				}
				else
				{
					iGROUP = from.indexOf(" GROUP BY ");
					iORDER = from.indexOf(" ORDER BY ");
					if (iORDER>0 && iGROUP==-1)
						iGROUP = iORDER;
					if (iGROUP>0)
					{
						others = from.substring(iGROUP+1, from.length());
						from = from.substring(0, iGROUP);
					}							
				}

				if (from==null || from.length()==0)
				{
					log.severe("No from clause from the SQL: "+command);
					throw new Exception();
				}

				//insert into AD_ViewComponent
				MViewComponent mvc = new MViewComponent(Env.getCtx(), 0, null);
				mvc.setName("VC_"+tableName);
				mvc.setAD_Table_ID(p_AD_Table_ID);
				mvc.setSeqNo((i+1)*10);
				mvc.setIsActive(true);
				mvc.setEntityType(p_EntityType);
				String from1 = from.substring(5);
				from1 = trim(from1);
				int rtix = from1.indexOf(' ');
				if (rtix < 0)
					rtix = from1.length();
				String refTab = from1.substring(0, rtix);
				
				if (DB.isPostgreSQL())
				{
					//Postgresql adding extra '('s whenver there are join clauses. remove them
					refTab=refTab.replace('(', ' ').replace(')',' ');					
				}
				
				refTab = trim(refTab);
				MTable rt = MTable.get(Env.getCtx(), refTab);
				if (rt!=null)
					mvc.setReferenced_Table_ID(rt.get_ID());
				else
					mvc.setReferenced_Table_ID(0);

				mvc.setFromClause(from);
				mvc.setWhereClause(where);
				mvc.setOtherClause(others);
				if (!mvc.save())
				{
					log.severe("unable to create view component " + i + ": "+command);
					throw new Exception();
				}

				//insert into AD_ViewColumn
				MViewColumn mvcol = null;
				for (int j=0; j<colName.size(); j++)
				{
					mvcol = new MViewColumn(Env.getCtx(), 0, null);
					mvcol.setAD_ViewComponent_ID(mvc.get_ID());
					mvcol.setIsActive(true);
					mvcol.setEntityType(p_EntityType);
					log.info("Importing View " + tableName + "(i,j) = (" + i + ", " + j + ")");
					mvcol.setDBDataType(colType.get(j));
					mvcol.setColumnName(colName.get(j));
					mvcol.setColumnSQL(colSQL.get(j));

					if (!mvcol.save())
					{
						log.severe("unable to create view component " + i + " column: "+ colName.get(j) + " in " + command);
						throw new Exception();
					}
				}
			} //for selects

			log.info("Imported view: " + tableName);
		
			viewNum++;
		}
		catch (Exception e)
		{
			log.severe("Error at importing view SQL: "+command+" \n " + e);
		}

		if (viewNum == 0)
			return ("Not able to import view "+tableName+" from the database");
		else
			return ("Imported view "+tableName+" from the database");
		
	}

	/*
	private void printUsage()
	{
		log.severe("Invalid arguments");
		log.severe("Usage: args[0] -t <database type>");
		log.severe("               -h <database host>");
		log.severe("               -p <database port>");
		log.severe("               -n <database name>");
		log.severe("               -U <database userId>");
		log.severe("               -P <database userId passwd>");
		log.severe("               -e <entity type>");
		log.severe("               -f <create view sql file (full path)>");
	}  // printUsage()

	private boolean setArgs(String[] args)
	{
		if (args.length != 16)
		{
			printUsage();
			return false;
		}
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].compareTo("-t") == 0)
				dbType = args[++i];
			else if (args[i].compareTo("-h") == 0)
				dbHost = args[++i];
			else if (args[i].compareTo("-p") == 0)
				dbPort = args[++i];
			else if (args[i].compareTo("-n") == 0)
				dbName = args[++i];
			else if (args[i].compareTo("-U") == 0)
				userId = args[++i];
			else if (args[i].compareTo("-P") == 0)
				passwd = args[++i];
			else if (args[i].compareTo("-f") == 0)
				SQLfile = args[++i];
			else if (args[i].compareTo("-e") == 0)
				entityType = args[++i];
			else 
			{
				log.severe("Invalid option: " + args[i]);
				printUsage();
				return false;
			}
		}
		return true;
	}  // setArgs()
	
	// Initialize the compiere environment
	private void init()
	{
		// Reset the connection info
		DB.setDBTarget(CConnection.get(dbType, dbHost, Integer.parseInt(dbPort), 
										dbName, userId, passwd));
	}  // init()

	

	public static void main(String[] args) 
	{
		// All compiere client has to do this
		Compiere.startup(true);
		CLogMgt.setLevel(Level.FINE);
		CLogErrorBuffer.get(false).setIssueError(false);
		ViewImport vi = new ViewImport();
		if (!vi.setArgs(args))
			return;
		vi.init();
		vi.doIt();
	}
	*/

}   //  ViewImport

