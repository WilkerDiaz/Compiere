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
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Import Data Migration file
 *	@author Jorg Janke
 */
public class DataMigrationImport extends SvrProcess 
{
	/** File Name		*/
	private String		p_FileName = null;

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
		int AD_Client_ID = getCtx().getAD_Client_ID();
		log.info("FileName=" + p_FileName + ",AD_Client_ID=" + AD_Client_ID);
		if (p_FileName == null)
			throw new CompiereSystemException("No File");
		File file = new File (p_FileName);
		if (!file.exists())
			throw new CompiereSystemException("@NotFound@ @FileName@ " + p_FileName);
		
		//	Call
		Class<?>[] parameterTypes = new Class[]{String.class, Integer.class};
		Object[] args = new Object[]{p_FileName, Integer.valueOf(AD_Client_ID)};
		Object info = null;
		try
		{
			Class<?> clazz = Class.forName("com.compiere.client.StartComponent");
			Object instance = clazz.newInstance();
			Method method = clazz.getMethod ("importDataMigration", parameterTypes);
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


}	//	DataMigrationImport
