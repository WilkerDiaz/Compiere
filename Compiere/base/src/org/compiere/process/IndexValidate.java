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

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Validate Index
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class IndexValidate extends SvrProcess
{
	private int		p_AD_TableIndex_ID = 0; 
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare()
	{
		p_AD_TableIndex_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return summary
	 *	@throws Exception error
	 */
	@Override
	protected String doIt() throws Exception
	{
		MTableIndex index = new MTableIndex (getCtx(), p_AD_TableIndex_ID, get_TrxName());
		log.info (index.toString ());
		Trx trx = Trx.get("getDatabaseMetaData");
		DatabaseMetaData md = trx.getConnection().getMetaData();
		String tableName = index.getTableName();
		if (md.storesUpperCaseIdentifiers())
			tableName = tableName.toUpperCase();
		else if (md.storesLowerCaseIdentifiers())
			tableName = tableName.toLowerCase();
		String catalog = "REFERENCE";
		String schema = null;
		
		String returnMesg = "";
		String[] indexColsFromDB = new String[30];
		int numIndexColsFromDB = 0;
		boolean indexNUniqueInDB = true;
		

		boolean found = false;
		ResultSet rs = md.getIndexInfo (catalog, schema, 
			tableName, false, true);
		while (rs.next())
		{
			String dbIndexName = rs.getString ("INDEX_NAME");			
			if (dbIndexName == null)
				continue;
			if (index.getName().trim().equalsIgnoreCase(dbIndexName))
			{
				found = true;
				String columnName=rs.getString("COLUMN_NAME");
				int pos = (rs.getShort("ORDINAL_POSITION"));				
				if(pos>0)
				{
					indexColsFromDB[pos-1]=columnName;
				}
				indexNUniqueInDB=rs.getBoolean("NON_UNIQUE");
				//break;
			}
		}
		rs.close();
		trx.close();
		
		
		MIndexColumn [] indexCols  = index.getColumns(true);
		boolean modified = false;
		
		if(indexCols.length<=0)
		{
			returnMesg = "No Index columns specified";
		}		
		else if(!found)
		{
			int rvalue = DB.executeUpdateIgnoreError(get_TrxName(), index.getDDL(), (Object[])null);
			if(rvalue == -1)
				returnMesg = "Failed to create index";
			else
				returnMesg = "Created index succesfully";
			
		}
		else
		{
			//Found the index in DB
			for(int i=0;i<30;i++)
			{
				if(indexColsFromDB[i] != null)
					numIndexColsFromDB++;
				else
					break;
			}		
			
						
			if(numIndexColsFromDB != indexCols.length)
			{
				modified = true;
			}
			else if(!indexNUniqueInDB != index.isUnique())
			{
				modified = true;
			}
			else
			{
				for(int j=0;j<indexCols.length;j++)
				{
					/*what if they are returned in a diff sequence ?*/
					if (indexCols[j].getColumnName().equalsIgnoreCase(indexColsFromDB[j]))						
						continue;					
					else if((indexColsFromDB[j].startsWith("\""))&&(indexColsFromDB[j].endsWith("\"")))
					{
						/* EDB returns varchar index columns wrapped with double quotes, hence comparing
						 * after stripping the quotes
						 */
						String cname = indexColsFromDB[j].substring(1,indexColsFromDB[j].length()-1);
						if(cname.equalsIgnoreCase(indexCols[j].getColumnName()))
							continue;
						else
						{
							modified=true;
							break;
						}						
					}
					else
					{
						modified=true;
						break;
					}				
				}
			}
			
			if(modified)
			{
				int rvalue = DB.executeUpdateIgnoreError(get_TrxName(), "DROP INDEX "+index.getName(), (Object[])null);							
				rvalue = DB.executeUpdateIgnoreError(get_TrxName(), index.getDDL(), (Object[])null);
				if(rvalue == -1)
					returnMesg = "Failed to modify index";
				else
					returnMesg = "Modified index successfully";
			}
			else
				returnMesg = "No Changes to the index";
		}
		
		return returnMesg;
	}	//	doIt

}
