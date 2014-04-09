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
 * 	Change Request Model
 *  @author Jorg Janke
 *  @version $Id: MChangeRequest.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MChangeRequest extends X_M_ChangeRequest
{
    /** Logger for class MChangeRequest */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MChangeRequest.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_ChangeRequest_ID ix
	 *	@param trx p_trx
	 */
	public MChangeRequest (Ctx ctx, int M_ChangeRequest_ID, Trx trx)
	{
		super (ctx, M_ChangeRequest_ID, trx);
		if (M_ChangeRequest_ID == 0)
		{
		//	setName (null);
			setIsApproved(false);
			setProcessed(false);
		}
	}	//	MChangeRequest

	/**
	 * 	CRM Request Constructor
	 *	@param request request
	 *	@param group request group
	 */
	public MChangeRequest (MRequest request, MGroup group)
	{
		this (request.getCtx(), 0, request.get_Trx());
		setClientOrg(request);
		setName(Msg.getElement(getCtx(), "R_Request_ID") + ": " + request.getDocumentNo());
		setHelp(request.getSummary());
		//
		setM_BOM_ID(group.getM_BOM_ID());
		setM_ChangeNotice_ID(group.getM_ChangeNotice_ID());
	}	//	MChangeRequest
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MChangeRequest (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MChangeRequest

	/**
	 * 	Get CRM Requests of Change Requests
	 *	@return requests
	 */
	public MRequest[] getRequests()
	{
		return null;
	}	//	getRequests
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true/false
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Have at least one
		if (getM_BOM_ID() == 0 && getM_ChangeNotice_ID() == 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@: @M_BOM_ID@ / @M_ChangeNotice_ID@"));
			return false;
		}
		
		//	Derive ChangeNotice from BOM if defined
		if (newRecord && getM_BOM_ID() != 0 && getM_ChangeNotice_ID() == 0)
		{
			MBOM bom = new MBOM (getCtx(), getM_BOM_ID(), get_Trx());
			if (bom.getM_ChangeNotice_ID() != 0)
				setM_BOM_ID(bom.getM_ChangeNotice_ID());
		}
		
		return true;
	}	//	beforeSave
	
}	//	MChangeRequest
