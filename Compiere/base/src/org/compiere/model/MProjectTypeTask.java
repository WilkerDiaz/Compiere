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
package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;

/**
 * 	Project Type Phase Task Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProjectTypeTask.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MProjectTypeTask extends X_C_Task
{
    /** Logger for class MProjectTypeTask */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProjectTypeTask.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MProjectTypeTask (Ctx ctx, int C_Task_ID, Trx trx)
	{
		super (ctx, C_Task_ID, trx);
		if (C_Task_ID == 0)
		{
		//	setC_Task_ID (0);		//	PK
		//	setC_Phase_ID (0);		//	Parent
		//	setName (null);
			setSeqNo (0);
			setStandardQty (Env.ZERO);
		}
	}	//	MProjectTypeTask

	public MProjectTypeTask (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProjectTypeTask

}	//	MProjectTypeTask
