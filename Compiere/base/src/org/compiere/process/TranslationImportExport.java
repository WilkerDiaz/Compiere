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

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 * 	Translation Import / Export
 *	@author Jorg Janke
 */
public class TranslationImportExport extends SvrProcess
{
	/** The Language				*/
	private String		p_AD_Language = null;

	private static final String	Mode_Import = "I";
	private static final String	Mode_Export = "E";
	/**	Export or Import Mode		*/
	private String 		p_ImportExport = Mode_Export;

	static final String	ExportScope_System = "S";
	/** System Data viewed by Tenants	*/
	static final String	ExportScope_SystemUser = "U";
	static final String	ExportScope_Tenant = "T";
	/** Export Scope		*/
	private String		p_ExportScope = ExportScope_System;
	/** Optional Specific Table		*/
	private int			p_AD_Table_ID = 0;

	static final String	TranslationLevel_All = "A";
	static final String	TranslationLevel_LabelOnly = "L";
	static final String	TranslationLevel_LabelDescriptionOnly = "D";
	/** Translation Level			*/
	private String		p_TranslationLevel = TranslationLevel_All;

	/** Optional Directory			*/
	private String		p_Directory = null;


	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para)
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Language"))
				p_AD_Language = (String)element.getParameter();
			else if (name.equals("ImportExport"))
				p_ImportExport = (String)element.getParameter();
			else if (name.equals("ExportScope"))
				p_ExportScope = (String)element.getParameter();
			else if (name.equals("AD_Table_ID"))
				p_AD_Table_ID = element.getParameterAsInt();
			else if (name.equals("TranslationLevel"))
				p_TranslationLevel = (String)element.getParameter();
			else if (name.equals("Directory"))
				p_Directory = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 * 	Process
	 * 	@return summary
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_Language=" + p_AD_Language
			+ ",Mode=" + p_ImportExport
			+ ",Scope=" + p_ExportScope
			+ ",AD_Table_ID=" + p_AD_Table_ID
			+ ",Level=" + p_TranslationLevel
			+ ",Directory=" + p_Directory);
		//

		TranslationMgr trl = new TranslationMgr(Env.getCtx());
		String msg = trl.validateLanguage(p_AD_Language);
		if (msg.length() > 0)
			throw new CompiereUserException("@LanguageSetupError@ - " + msg);

		//	Mode
		boolean importMode = Mode_Import.equals(p_ImportExport);
		//	Client
		int AD_Client_ID = 0;
		if (ExportScope_Tenant.equals(p_ExportScope))
			AD_Client_ID = getCtx().getAD_Client_ID();
		trl.setExportScope(p_ExportScope, AD_Client_ID);

		//	Directory
		if (Util.isEmpty(p_Directory))
			p_Directory = Ini.getCompiereHome() + File.separator + "data";

		int noWords = 0;
		//	All Tables
		if (p_AD_Table_ID == 0)
		{
			String sql = "SELECT * FROM AD_Table WHERE IsActive='Y' AND IsView='N'"
				+ " AND TableName LIKE '%_Trl' AND TableName<>'AD_Column_Trl'";
			if (ExportScope_Tenant.equals(p_ExportScope))
				sql += " AND AccessLevel<>'4'";	//	System Only
			else
				sql += " AND AccessLevel NOT IN ('1','2','3')";	// Org/Client/Both
			sql += " ORDER BY TableName";
			ArrayList<MTable> tables = MTable.getTablesByQuery(getCtx(), sql);
			for (MTable table : tables)
			{
				String tableName = table.getTableName();
				msg = null;
				msg = importMode
					? trl.importTrl (p_Directory, tableName)
					: trl.exportTrl (p_Directory, tableName, p_TranslationLevel);
				addLog(msg);
			}
			noWords = trl.getWordCount();
		}
		else	//	single table
		{
			MTable table = MTable.get(getCtx(), p_AD_Table_ID);
			msg = null;
			msg = importMode
				? trl.importTrl (p_Directory, table.getTableName())
				: trl.exportTrl (p_Directory, table.getTableName(), p_TranslationLevel);
			addLog(msg);
			noWords = trl.getWordCount();
		}
		//
		return ("Word Count = " + noWords);
	}	//	doIt

}	//	TranslationImportExport
