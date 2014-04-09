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

import javax.servlet.http.*;

import org.compiere.util.*;

/**
 *  Asset Delivery Model
 *
 *  @author Jorg Janke
 *  @version $Id: MAssetDelivery.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MAssetDelivery extends X_A_Asset_Delivery
{
    /** Logger for class MAssetDelivery */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAssetDelivery.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param A_Asset_Delivery_ID id or 0
	 * 	@param trx p_trx
	 */
	public MAssetDelivery (Ctx ctx, int A_Asset_Delivery_ID, Trx trx)
	{
		super (ctx, A_Asset_Delivery_ID, trx);
		if (A_Asset_Delivery_ID == 0)
		{
			setMovementDate (new Timestamp (System.currentTimeMillis ()));
		}
	}	//	MAssetDelivery

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *	@param trx transaction
	 */
	public MAssetDelivery (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAssetDelivery

	/**
	 * 	Create Asset Delivery for HTTP Request
	 * 	@param asset asset
	 * 	@param request request
	 * 	@param AD_User_ID BP Contact
	 */
	public MAssetDelivery (MAsset asset, 
		HttpServletRequest request, int AD_User_ID)
	{
		super (asset.getCtx(), 0, asset.get_Trx());
		setClientOrg(asset);
		//	Asset Info
		setA_Asset_ID (asset.getA_Asset_ID());
		setLot(asset.getLot());
		setSerNo(asset.getSerNo());
		setVersionNo(asset.getVersionNo());
		//
		setMovementDate (new Timestamp (System.currentTimeMillis ()));
		//	Request
		setURL(request.getRequestURL().toString());
		setReferrer(request.getHeader("Referer"));
		setRemote_Addr(request.getRemoteAddr());
		setRemote_Host(request.getRemoteHost());
		//	Who
		setAD_User_ID(AD_User_ID);
		//
		save();
	}	//	MAssetDelivery

	/**
	 * 	Create Asset Delivery for EMail
	 * 	@param asset asset
	 * 	@param email optional email
	 * 	@param messageID access ID
	 * 	@param AD_User_ID BP Contact
	 */
	public MAssetDelivery (MAsset asset, EMail email, String messageID, int AD_User_ID)
	{
		super (asset.getCtx(), 0, asset.get_Trx());
		setClientOrg(asset);
		//	Asset Info
		setA_Asset_ID (asset.getA_Asset_ID());
		setLot(asset.getLot());
		setSerNo(asset.getSerNo());
		setVersionNo(asset.getVersionNo());
		//
		setMovementDate (new Timestamp (System.currentTimeMillis ()));
		//	EMail
		if (email != null)
		{
			setEMail(email.getTo().toString());
			setMessageID(email.getMessageID());
		}
		else
			setMessageID(messageID);
		//	Who
		setAD_User_ID(AD_User_ID);
		//
		save();
	}	//	MAssetDelivery

	/**
	 * 	String representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAssetDelivery[")
			.append (get_ID ())
			.append(",A_Asset_ID=").append(getA_Asset_ID())
			.append(",MovementDate=").append(getMovementDate())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MAssetDelivery

