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
 * 	Scheduler Parameter Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MSchedulerPara.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MSchedulerPara extends X_AD_Scheduler_Para
{
    /** Logger for class MSchedulerPara */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSchedulerPara.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Scheduler_Para_ID id
	 *	@param trx transaction
	 */
	public MSchedulerPara (Ctx ctx, int AD_Scheduler_Para_ID,
		Trx trx)
	{
		super (ctx, AD_Scheduler_Para_ID, trx);
	}	//	MSchedulerPara

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MSchedulerPara (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MSchedulerPara
	
	/** Parameter Column Name		*/
	private MProcessPara	m_parameter = null;
	
	/**
	 * 	Get Parameter Column Name 
	 *	@return column name
	 */
	public String getColumnName()
	{
		if (m_parameter == null)
			m_parameter = MProcessPara.get(getCtx(), getAD_Process_Para_ID());
		return m_parameter.getColumnName();
	}	//	getColumnName
	
	/**
	 * 	Get Display Type
	 *	@return display type
	 */
	public int getDisplayType()
	{
		if (m_parameter == null)
			m_parameter = MProcessPara.get(getCtx(), getAD_Process_Para_ID());
		return m_parameter.getAD_Reference_ID();
	}	//	getDisplayType

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer("MSchedulerPara[");
		sb.append(get_ID()).append("-")
			.append(getColumnName()).append("=").append(getParameterDefault())
			.append("]");
		return sb.toString();
	} //	toString
	
}	//	MSchedulerPara
