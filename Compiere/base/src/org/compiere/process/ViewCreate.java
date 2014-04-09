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
import java.util.*;
import java.util.logging.*;

import org.compiere.db.CConnection;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Create Views
 *	
 *  @author Jinglun Zhang
 *  
 *  
 */
public class ViewCreate extends SvrProcess implements ASyncProcess
{
	/** Entity Type			*/
	private String	p_EntityType = "D";	//	ENTITYTYPE_Customization
	private int p_AD_Table_ID = 0;
	private int m_count = 0;

	
	/**
	 * 	Construct
	 */
	public ViewCreate ()
	{		
	}
	
	/**
	 * 	Construct
	 * @param entityType Entity Type
	 */
	public ViewCreate (String entityType)
	{
		if (entityType == null)
		{
			p_EntityType = "";
			log.log(Level.SEVERE, "Entity Type is NULL!");
		}
		else
			p_EntityType = entityType;
	}
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	public void prepare ()
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

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	public String doIt () throws Exception
	{
		ArrayList<MTable> list = new ArrayList<MTable>();
		boolean retval = true;
		p_AD_Table_ID = (getProcessInfo()!=null)?getRecord_ID():0;
		
		if (p_AD_Table_ID > 0) {
			// create one specific view
			log.info("Creating a view for AD_Table_ID=" + p_AD_Table_ID);
			MTable table = MTable.get(Env.getCtx(), p_AD_Table_ID);
			list.add(table);
		} else {
			// process all views for the entity type		
			log.info("Creating views for EntityType=" + p_EntityType);

			//get views from AD
			PreparedStatement pstmt = null;
			String sql = "SELECT * FROM AD_Table WHERE IsActive='Y' AND IsView='Y'";

			if (p_EntityType != null)
			{
				// Currently support only single entity type
				sql += " AND EntityType in ('" + p_EntityType + "')";
			}
			sql += " ORDER BY LoadSeq";
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					MTable table = new MTable (Env.getCtx(), rs, null);
					list.add(table);
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
				return "Fail. Error to get view definition.";
			}
			try
			{
				if (pstmt != null)
					pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				pstmt = null;
			}
		}		
		
		String errViews = "";

		Trx trx = Trx.get("getDatabaseMetaData");
		DatabaseMetaData md = trx.getConnection().getMetaData();
		String schema = CConnection.get().getDbUid();
		
		for (MTable mt : list)
		{
			String vcsql = mt.getViewCreate(true);
			if (vcsql != null && vcsql.length()>0)
			{
				try
				{
					int no = -1;
					if (DB.isMSSQLServer() || DB.isPostgreSQL())
					{
						String view = mt.getTableName();
						if (md.storesUpperCaseIdentifiers()){
							schema = schema.toUpperCase();
							view = view.toUpperCase();
						} else {
							schema = schema.toLowerCase();
							view = view.toLowerCase ();
						}						
						ResultSet trs = md.getTables(null, schema, view, new String[] {"VIEW"});	
						if (trs!=null){
							if (trs.next()){
								if (DB.isMSSQLServer())
									no = DB.executeUpdateIgnoreError((Trx) null, "DROP VIEW " + mt.getTableName(), (Object[])null);
								else
									no = DB.executeUpdateIgnoreError((Trx) null, "DROP VIEW " + mt.getTableName() +" CASCADE", (Object[])null);
							}
							trs.close();
						}
						vcsql = vcsql.replace(" OR REPLACE ", " ");
					}
					
					no = DB.executeUpdate((Trx) null, vcsql);
					if (no != -1)
					{
						m_count++;
						log.log (Level.INFO, "Created view " + mt.getTableName());
					}
					else
					{
						//if (!DB.isMSSQLServer()){
							log.log (Level.SEVERE, vcsql);
							retval = false;
						//}
							errViews = errViews + mt.getTableName() + ", ";
					}
					
					/*
					//create table columns
					MColumn[] mcs = mt.getColumns(false);
					//for (MColumn mc : mcs)
					//	mc.delete(true);
					if (mcs==null || mcs.length==0)
					{
						//TODO jz temp disable until fix it
						//TableCreateColumns tcc = new TableCreateColumns(p_EntityType, mt.get_ID());
						//tcc.doIt();	
						ProcessInfo pi = new ProcessInfo ("", 173, mt.get_Table_ID(), mt.get_ID());
						pi.setAD_User_ID (Env.getAD_User_ID(Env.getCtx()));
						pi.setAD_Client_ID (Env.getAD_Client_ID(Env.getCtx()));
						pi.setIsBatch(false);
						//jz ProcessCtl.process(this, 0, pi, get_Trx()); //  calls lockUI, unlockUI
					}
					else
						log.warning("View " + mt.get_TableName() + " has had columns already.");
						*/
				}
				catch (Exception e)
				{
					log.log (Level.SEVERE, vcsql, e);
					retval = false;
				}
			}
			else
			{
				log.log (Level.SEVERE, "Fail to get view create sql for " + mt.getTableName());
				retval = false;
			}
		}

		if (trx!=null)
			trx.close();
		
		if (!retval)
			return "Fail. Error to create views: " + errViews + " created view #" + m_count;
		else
			return "Created view #" + m_count;
	}	//	doIt
	
	/**
	 *  Lock User Interface
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi process info
	 */
	public void unlockUI (ProcessInfo pi)
	{
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return false;
	}   //  isLoacked

	/**
	 *  Method to be executed async.
	 *  Called from the ASyncProcess worker
	 *  @param pi process info
	 */
	public void executeASync (ProcessInfo pi)
	{
		log.config("-");
	}   //  executeASync

}	//	ViewCreate
