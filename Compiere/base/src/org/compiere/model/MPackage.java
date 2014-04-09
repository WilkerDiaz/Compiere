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
 *	Package Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPackage.java,v 1.3 2006/07/30 00:51:04 jjanke Exp $
 */
public class MPackage extends X_M_Package
{
    /** Logger for class MPackage */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPackage.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Create one Package for Shipment 
	 *	@param shipment shipment
	 *	@param shipper shipper
	 *	@param shipDate null for today
	 *	@return package
	 */
	public static MPackage create (MInOut shipment, MShipper shipper, Timestamp shipDate)
	{
		MPackage retValue = new MPackage (shipment, shipper);
		if (shipDate != null)
			retValue.setShipDate(shipDate);
		retValue.save();
		//	Lines
		MInOutLine[] lines = shipment.getLines(false);
		for (MInOutLine sLine : lines) {
			MPackageLine pLine = new MPackageLine (retValue);
			pLine.setInOutLine(sLine);
			pLine.save();
		}	//	lines
		return retValue;
	}	//	create

	
	
	/**************************************************************************
	 * 	MPackage
	 *	@param ctx context
	 *	@param M_Package_ID id
	 *	@param trx transaction
	 */
	public MPackage (Ctx ctx, int M_Package_ID, Trx trx)
	{
		super (ctx, M_Package_ID, trx);
		if (M_Package_ID == 0)
		{
		//	setM_Shipper_ID (0);
		//	setDocumentNo (null);
		//	setM_InOut_ID (0);
			setShipDate (new Timestamp(System.currentTimeMillis()));
		}
	}	//	MPackage

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPackage (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPackage
	
	/**
	 * 	Shipment Constructor
	 *	@param shipment shipment
	 *	@param shipper shipper
	 */
	public MPackage (MInOut shipment, MShipper shipper)
	{
		this (shipment.getCtx(), 0, shipment.get_Trx());
		setClientOrg(shipment);
		setM_InOut_ID(shipment.getM_InOut_ID());
		setM_Shipper_ID(shipper.getM_Shipper_ID());
	}	//	MPackage
	
}	//	MPackage
