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
 * 	Request Update Model
 *  @author Jorg Janke
 *  @version $Id: MRequestUpdate.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MRequestUpdate extends X_R_RequestUpdate
{
    /** Logger for class MRequestUpdate */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRequestUpdate.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_RequestUpdate_ID id
	 *	@param trx p_trx
	 */
	public MRequestUpdate (Ctx ctx, int R_RequestUpdate_ID,
		Trx trx)
	{
		super (ctx, R_RequestUpdate_ID, trx);
	}	//	MRequestUpdate

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MRequestUpdate (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MRequestUpdate

	/**
	 * 	Parent Constructor
	 *	@param parent request
	 */
	public MRequestUpdate (MRequest parent)
	{
		super (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setR_Request_ID (parent.getR_Request_ID());
		//
		setStartTime(parent.getStartTime());
		setEndTime(parent.getEndTime());
		setResult(parent.getResult());
		setQtySpent(parent.getQtySpent());
		setQtyInvoiced(parent.getQtyInvoiced());
		setM_ProductSpent_ID(parent.getM_ProductSpent_ID());
		setConfidentialTypeEntry(parent.getConfidentialTypeEntry());
	}	//	MRequestUpdate
	
	/**
	 * 	Do we have new info
	 *	@return true if new info
	 */
	public boolean isNewInfo()
	{
		return getResult() != null;
	}	//	isNewInfo
	
	/**
	 * 	Get Name of creator
	 *	@return name
	 */
	public String getCreatedByName()
	{
		MUser user = MUser.get(getCtx(), getCreatedBy());
		return user.getName();
	}	//	getCreatedByName

	/**
	 * 	Get Confidential Entry Text (for jsp)
	 *	@return text
	 */
	public String getConfidentialEntryText()
	{
		return MRefList.getListName(getCtx(), X_Ref_R_Request_Confidential.AD_Reference_ID, getConfidentialTypeEntry());
	}	//	getConfidentialTextEntry

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getConfidentialTypeEntry() == null)
			setConfidentialTypeEntry(CONFIDENTIALTYPEENTRY_PublicInformation);
		return true;
	}	//	beforeSave
	
}	//	MRequestUpdate
