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
import java.lang.reflect.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Create Data Migration file
 *	@author Jorg Janke
 */
public class DataMigrationCreate extends SvrProcess 
{
	/** Data Migration		*/
	private int		p_AD_DataMigration_ID = 0;
	/** File Name			*/
	private String	p_FileName = null;

	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_DataMigration_ID"))
				p_AD_DataMigration_ID = element.getParameterAsInt();
			else if (name.equals("FileName"))
				p_FileName = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 * 	@return info
	 */
	@Override
	protected String doIt() throws Exception 
	{
		log.info("AD_DataMigration_ID=" + p_AD_DataMigration_ID);
		if (p_AD_DataMigration_ID == 0)
			throw new CompiereSystemException("No AD_DataMigration_ID");
		MDataMigration dm = new MDataMigration(getCtx(), p_AD_DataMigration_ID, get_TrxName());
		if (dm.get_ID() != p_AD_DataMigration_ID)
			throw new CompiereSystemException("@NotFound@ @AD_DataMigration_ID@ ID=" + p_AD_DataMigration_ID);

		if (p_FileName == null)
		{	//	yyyy-mm-dd hh:mm:ss.fffffffff
			Timestamp now = new Timestamp(System.currentTimeMillis());
			p_FileName = dm.getName() + "_" + now.toString().substring(0,10);
			p_FileName = Util.remove(p_FileName, " ", false);
		}
		String targetDir = Ini.getCompiereHome() + File.separator + "data";
		log.config(targetDir + " " + p_FileName);

		//	Call
		Class<?>[] parameterTypes = new Class[]{MDataMigration.class, String.class, String.class};
		Object[] args = new Object[]{dm, targetDir, p_FileName};
		Object info = null;
		try
		{
			Class<?> clazz = Class.forName("com.compiere.client.StartComponent");
			Object instance = clazz.newInstance();
			Method method = clazz.getMethod ("createDataMigration", parameterTypes);
			info = method.invoke (instance, args);
		}
		catch (Exception e)
		{
			throw new CompiereSystemException(e.toString());
		}
		
		if (info != null)
			return info.toString();
		throw new CompiereSystemException("Please check Log");
	}	//	doIt


}	//	DataMigrationCreate
