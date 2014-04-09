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

import java.lang.reflect.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Data Migration Preview
 *	@author Jorg Janke
 */
public class DataMigrationPreview extends SvrProcess
{
	/** Parent							*/
	private int 		p_AD_DataMigration_ID = 0;
	/** Include all with entity type	*/
	private boolean 	p_IsIncludeAllEntityTypeData = false;
	
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
		//	log.fine("prepare - " + para[i]);
			if (element.getParameter() == null)
				;
			else if (name.equals("IsIncludeAllEntityTypeData"))
				p_IsIncludeAllEntityTypeData = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);		
		}
		p_AD_DataMigration_ID = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Process It
	 * 	@return summary
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_DataMigration_ID=" + p_AD_DataMigration_ID 
			+ ",IsIncludeAllEntityTypeData=" + p_IsIncludeAllEntityTypeData);
		if (p_AD_DataMigration_ID == 0)
			throw new CompiereUserException("@AD_DataMigration_ID@");
		MDataMigration dm = new MDataMigration(getCtx(), p_AD_DataMigration_ID, null);
		if (dm.get_ID() != p_AD_DataMigration_ID)
			throw new CompiereUserException("@NotFound@ @AD_DataMigration_ID@");

		if (p_IsIncludeAllEntityTypeData && dm.getAD_Client_ID() != 0)
		{
			p_IsIncludeAllEntityTypeData = false;
			log.warning("IsIncludeAllEntityTypeData set to false - running from Tenant");
		}

		//	Clean up
		int no = MDataMigrationPreview.delete(dm.getAD_DataMigration_ID(), null);
		addLog("Old Records Deleted: " + no);

		//	Call
		Class<?>[] parameterTypes = new Class[]{MDataMigration.class, Boolean.class};
		Object[] args = new Object[]{dm, Boolean.valueOf(p_IsIncludeAllEntityTypeData)};
		Object info = null;
		try
		{
			Class<?> clazz = Class.forName("com.compiere.client.StartComponent");
			Object instance = clazz.newInstance();
			Method method = clazz.getMethod ("createDataMigrationPreview", parameterTypes);
			info = method.invoke (instance, args);
		}
		catch (Exception e)
		{
			throw new CompiereSystemException(e.toString());
		}
		
		if (info != null)
		{
			addLog(info.toString());
			return info.toString();
		}
		throw new CompiereSystemException("Please check Log");
	}	//	doIt

}	//	DataMigrationPreview
