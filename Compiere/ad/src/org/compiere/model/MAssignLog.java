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

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Assignment Log Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAssignLog.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MAssignLog extends X_AD_AssignLog
{
    /** Logger for class MAssignLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAssignLog.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AssignLog_ID id
	 *	@param trx p_trx
	 */
	public MAssignLog(Ctx ctx, int AD_AssignLog_ID, Trx trx)
	{
		super(ctx, AD_AssignLog_ID, trx);
	}	//	MAssignLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MAssignLog(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAssignLog
	
	/**
	 * 	Parent constructor
	 *	@param parent parent
	 */
	public MAssignLog (PO po, MAssignTarget parent)
	{
		this (po.getCtx(), 0, po.get_Trx());
		setClientOrg(po);
		setAD_AssignTarget_ID(parent.getAD_AssignTarget_ID());
		int id = po.get_ID();
		if (id != 0)		//	could be null for new
			setRecord_ID(id);
	}	//	MAssignLog
	
	/**
	 * 	Add to Help/Comment
	 *	@param add addl info
	 */
	public void addHelp(String add)
	{
		if (Util.isEmpty(add))
			return;
		String old = getHelp();
		if (Util.isEmpty(old))
			setHelp(add);
		else
			setHelp(old + "\n" + add);
	}	//	addHelp
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MAssignLog[").append(get_ID())
	        .append(",AD_AssignTarget_ID=").append(getAD_AssignTarget_ID())
	    	.append(",Record_ID=").append(getRecord_ID());
	    if (getHelp() != null)
	    	sb.append("-").append(getHelp());
	    sb.append("]");
	    return sb.toString();
    } //	toString
}	//	MAssignLog
