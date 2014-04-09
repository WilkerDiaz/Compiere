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
import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *  Import create view SQL file into Compiere AD
 *
 *  06-18-07  Jinglun Zhang   - created
 *  
 */
public class ViewImport extends SvrProcess
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
	private String SQLfile = null;
	private String entityType = null;
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
				entityType = (String)element.getParameter();
			else if (name.equals("SQLfile"))
				SQLfile = (String)element.getParameter();
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

		return 0;
	}
	
	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	public String doIt() throws Exception
	{
		int viewNum = 0;
		//get SQLs
		InputStream in = null;
		String targetViewName = null;
		p_AD_Table_ID = (getProcessInfo()!=null)?getRecord_ID():0;
		if (p_AD_Table_ID>0){
			MTable targetTable = MTable.get(Env.getCtx(), p_AD_Table_ID); 
			targetViewName = targetTable.getTableName();
			entityType = targetTable.getEntityType();
		}
		try
		{
			in = new FileInputStream(SQLfile);
		}
		catch (IOException e)
		{
			log.severe(e.getMessage());
			throw new CompiereUserException("SQL file error. file name = " + SQLfile);
		}
		if (in == null)
		{
			throw new CompiereUserException("SQL file error. file name = " + SQLfile);
		}
		
		ArrayList<String> commands = ViewUtil.readSqlFromFile(in);
		if (commands == null)
		{
			throw new CompiereUserException("SQL file error. file name = " + SQLfile);
		}
		
		
		//put SQL into AD
		// Initialization
		String tableName = null;
		ArrayList<String> colName = new ArrayList<String>();
		ArrayList<String> colSQL = new ArrayList<String>();
		ArrayList<String> colType = new ArrayList<String>();

		Trx myTrx = Trx.get("ImportView");
		String Acommand = null;
		endSQL:
		try
		{
			for (String command : commands)
			{
				Acommand = command;
				boolean colNameDone = false;
				int ir = command.indexOf('\t');
				while (ir>0)
				{
					command = replace(command, '\t', ' ');
					ir = command.indexOf('\t');
				}
				// Not care about "exit"
				if (command.equalsIgnoreCase("exit"))
					break endSQL;
				
				if (command.startsWith("DROP VIEW "))
					continue;
				
				tableName = null;
				colName.clear();
				
				int iView = command.indexOf(" VIEW ");
				eachSQL:
				if (command.startsWith("CREATE") && iView>0)
				{
					int iSel = command.indexOf("SELECT ");
					String tc = command.substring(iView+6, iSel);
					int ileft = tc.indexOf('(');
					int iright = tc.indexOf(')');
					if (ileft>0)
					{
						tableName = tc.substring(0, ileft);
						String[] colNames = tc.substring(ileft+1, iright).split(",");
						for (String cn:colNames)
						{
							colName.add(trim(cn));
						}
						colNameDone=true;
					}
					else
					{
						tableName = tc.substring(0, tc.indexOf(" AS "));
					}				
					if (tableName!=null)
						//jz String trim() doesn't remove tail space tableName.trim();
						tableName = trim(tableName);
					if (tableName==null || tableName.length()==0)
					{
						log.severe("No view name from the SQL: "+command);
						continue;
					}
					if (p_AD_Table_ID!=0 && !targetViewName.equalsIgnoreCase(tableName)){						
						log.fine("Skipping view "+targetViewName);
						continue;
					}
					//int itc = tc.substring(tc.length()-1).hashCode();
					
					//insert into/update ad_table for each view
					MTable mt = MTable.get(Env.getCtx(), tableName);
					if (mt!=null && !mt.isView())
					{
						log.severe("Duplicated view name with an existing table for the SQL: "+command);
						//myTrx.rollback();
						break eachSQL;
					}
					
					if (mt==null)
						//mt.delete(true,  null);
					{
						mt = new MTable(Env.getCtx(), 0, null);
						//mt.delete(true,  myTrx.getTrxName());
						//mt = new MTable(Env.getCtx(), 0, myTrx.getTrxName());
						//MTable mt = MTable.get(Env.getCtx(), 0);
						mt.setTableName(tableName);
						//mt.setAD_Org_ID(0);
						//mt.setAD_Client_ID(0); also updatedby, createdby. jz: all default is 0 in PO
						mt.setAccessLevel(X_AD_Table.ACCESSLEVEL_TenantPlusOrganization);
						mt.setEntityType(entityType);
						mt.setIsActive(true);
						mt.setIsView(true);
						mt.setName("View_" + tableName);
						mt.setLoadSeq(900);
						mt.setImportTable(null);
						if (!mt.save())
						{
							log.severe("Unable to insert into AD_Table for the SQL: "+command);
							//myTrx.rollback();
							break eachSQL;
						}
						log.info("Add " + tableName + " into AD_Table for the SQL: "+command);
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
					}

					
					//insert into ad_viewComponent for each union part
					command = command.substring(iSel);
					String[] selects = command.split("UNION "); // currently does not handle other set operators (e.g. INTERSECT)
					
					for (int i=0; i<selects.length; i++)
					{
						int iFrom = findNext(selects[i]," FROM ");
						if (iFrom < 0)
						{
							log.severe("No from clause from the SQL: "+command);
							break eachSQL;
						}
						colSQL.clear();
						colType.clear();
						int isel = selects[i].indexOf("SELECT ");
						String colstr = selects[i].substring(isel+7, iFrom);
						colstr = colstr.concat(",");
						int iComma = findNext (colstr, ",");
						if (iComma == 0)	
						{
							log.severe("No view column from the SQL: "+command);
							break eachSQL;
						}
						int iPrevComma = -1;
						while (iComma != iPrevComma)
						{
							String column = colstr.substring(iPrevComma+1, iComma);
							int iAS = column.lastIndexOf(" AS ");
							String cs = null;
							if (iAS>0)
							{
								if (i==0 && !colNameDone)
									colName.add(column.substring(iAS+4, column.length()).trim());
								cs = column.substring(0, iAS);
								//colSQL.add(cols[j].substring(0, iAS));
							}
							else
							{
								cs = column;
								if (i==0 && !colNameDone)
								{
									int iDot = column.indexOf('.');
									String cn = column;
									if (iDot>0)
										cn = column.substring(iDot+1, column.length()).trim();
									colName.add(cn);
								}
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
							if (iPrevComma+1 < colstr.length())
								iComma = iPrevComma + 1 + findNext (colstr.substring(iPrevComma+1),",");
						}// while (iComma != iPrevComma)
						
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
							//myTrx.rollback();
							break eachSQL;
						}
						
						//insert into AD_ViewComponent
						//MViewComponent mvc = new MViewComponent(Env.getCtx(), 0, myTrx.getTrxName());
						MViewComponent mvc = new MViewComponent(Env.getCtx(), 0, null);
						mvc.setName("VC_"+tableName);
						mvc.setAD_Table_ID(mt.get_ID());
						mvc.setSeqNo((i+1)*10);
						mvc.setIsActive(true);
						mvc.setEntityType(entityType);
						//mvc.setAD_Org_ID(0);
						//mvc.setReferenced_Table_ID(mt.get_ID());
						String from1 = from.substring(5);
						from1 = trim(from1);
						int rtix = from1.indexOf(' ');
						if (rtix < 0)
							rtix = from1.length();
						String refTab = from1.substring(0, rtix);
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
							//myTrx.rollback();
							break eachSQL;
						}
						
						//insert into AD_ViewColumn
						MViewColumn mvcol = null;
						for (int j=0; j<colName.size(); j++)
						{
							//mvcol = new MViewColumn(Env.getCtx(), 0, myTrx.getTrxName());
							mvcol = new MViewColumn(Env.getCtx(), 0, null);
							//mvcol.setAD_Org_ID(0);
							mvcol.setAD_ViewComponent_ID(mvc.get_ID());
							mvcol.setIsActive(true);
							mvcol.setEntityType(entityType);
							log.info("Importing View " + tableName + "(i,j) = (" + i + ", " + j + ")");
							mvcol.setDBDataType(colType.get(j));
							mvcol.setColumnName(colName.get(j));
							mvcol.setColumnSQL(colSQL.get(j));
							
							if (!mvcol.save())
							{
								log.severe("unable to create view component " + i + " column: "+ colName.get(j) + " in " + command);
								//myTrx.rollback();
								break eachSQL;
							}
						}
					}//for selects
					
					//myTrx.commit();
					log.info("Impored view: " + tableName);
				}//handle create view
				else
				{
					log.warning("Ignore non create view SQL: "+command);
					continue;
				}
				
				viewNum++;
			}  // for (String command : commands)
		}
		catch (Exception e)
		{
			log.severe("Error at importing view SQL: "+Acommand+" \n " + e);
		}
		finally
		{
			if (myTrx!= null)
			{
				myTrx.rollback();
				myTrx.close();
			}
		}
		
		if (p_AD_Table_ID > 0) {
			if (viewNum == 0)
				return ("Not able to import view "+targetViewName+" from "+SQLfile);
			else
				return ("Created view "+targetViewName);
		}
		return "Imported View #" + viewNum;
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

