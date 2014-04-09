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
package org.compiere.impexp;

import java.sql.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Import Format Row Model 
 *	
 *  @author Jorg Janke
 *  @version $Id: MImpFormatRow.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MImpFormatRow extends X_AD_ImpFormat_Row
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_ImpFormat_Row_ID id
	 *  @param trx transaction
	 */
	public MImpFormatRow (Ctx ctx, int AD_ImpFormat_Row_ID, Trx trx)
	{
		super (ctx, AD_ImpFormat_Row_ID, trx);
		if (AD_ImpFormat_Row_ID == 0)
		{
		//	setAD_ImpFormat_ID (0);		Parent
		//	setAD_Column_ID (0);
		//	setDataType (null);
		//	setName (null);
		//	setSeqNo (10);
			setDecimalPoint (".");
			setDivideBy100 (false);
		}
	}	//	MImpFormatRow

	/**
	 * 	Load Construcor
	 *	@param ctx context
	 *	@param rs result set
	 *  @param trx transaction
	 */
	public MImpFormatRow (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MImpFormatRow
	
	/**
	 * 	Parent Construcor
	 *	@param format format parent
	 */
	public MImpFormatRow (MImpFormat format)
	{
		this (format.getCtx(), 0, format.get_Trx());
		setAD_ImpFormat_ID(format.getAD_ImpFormat_ID());
	}	//	MImpFormatRow
	
	/**
	 * 	Parent/Copy Construcor
	 *	@param parent format parent
	 *	@param original to copy
	 */
	public MImpFormatRow (MImpFormat parent, MImpFormatRow original)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		copyValues(original, this);
		setClientOrg(parent);
		setAD_ImpFormat_ID(parent.getAD_ImpFormat_ID());
	}	//	MImpFormatRow
	
}	//	MImpFormatRow
