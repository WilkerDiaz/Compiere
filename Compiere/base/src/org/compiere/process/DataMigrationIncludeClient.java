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

import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Set Include Client for Data Migration
 *	@author Jorg Janke
 */
public class DataMigrationIncludeClient extends SvrProcess 
{
	/** Data Migration		*/
	private int		p_AD_DataMigration_ID = 0;

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
		log.info("AD_DataMigration_ID=" + p_AD_DataMigration_ID 
			+ ", AD_Client_ID=" + AD_Client_ID);
		if (p_AD_DataMigration_ID == 0)
			throw new CompiereSystemException("No AD_DataMigration_ID");
		MDataMigration dm = new MDataMigration(getCtx(), p_AD_DataMigration_ID, get_TrxName());
		if (dm.get_ID() != p_AD_DataMigration_ID)
			throw new CompiereSystemException("@NotFound@ @AD_DataMigration_ID@ ID=" + p_AD_DataMigration_ID);
		
		if (!X_AD_DataMigration.DATAMIGRATIONTYPE_SystemAndTenant.equals(dm.getDataMigrationType()))
			throw new CompiereSystemException("@NotFound@ @DataMigrationType@ A<>" + dm.getDataMigrationType());

		if (dm.getAD_Client_ID() != 0)
			throw new CompiereSystemException("@NotFound@ @AD_Client_ID@ 0<>" + dm.getAD_Client_ID());
		
		if (dm.getAD_ClientInclude_ID() != 0)
			throw new CompiereSystemException("@AD_ClientInclude_ID@ Already set to " + dm.getAD_ClientInclude_ID());

		dm.setAD_ClientInclude_ID(AD_Client_ID);
		if (!dm.save())
			throw new CompiereSystemException("Not saved");
		
		return "OK";
	}	//	doIt


}	//	DataMigrationIncludeClient
