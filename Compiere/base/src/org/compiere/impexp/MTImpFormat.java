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

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Temporaray data during Import Format Loader 
 *	
 *  @author Jorg Janke
 *  @version $Id: MTImpFormat.java,v 1.0 2007/10/29 00:51:05 jjanke Exp $
 */
public class MTImpFormat extends X_T_ImpFormat{
	/**
	 * 
	 */
	private static final long serialVersionUID = -699557359197394394L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param T_ImpFormat_ID id
	 *  @param trx transaction
	 */
	public MTImpFormat (Ctx ctx, int T_ImpFormat_ID, Trx trx){
		super(ctx, T_ImpFormat_ID, trx);
	}	
}
