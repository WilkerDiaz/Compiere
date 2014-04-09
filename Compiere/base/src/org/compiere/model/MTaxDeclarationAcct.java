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
 * 	Tax Tax Declaration Accounting Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTaxDeclarationAcct.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MTaxDeclarationAcct extends X_C_TaxDeclarationAcct
{
    /** Logger for class MTaxDeclarationAcct */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTaxDeclarationAcct.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx ctx
	 *	@param C_TaxDeclarationAcct_ID id
	 *	@param trx trc
	 */
	public MTaxDeclarationAcct (Ctx ctx, int C_TaxDeclarationAcct_ID, Trx trx)
	{
		super (ctx, C_TaxDeclarationAcct_ID, trx);
	}	//	MTaxDeclarationAcct

	/**
	 * 	Load Constructor
	 *	@param ctx ctx
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MTaxDeclarationAcct (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MTaxDeclarationAcct

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param fact fact
	 */
	public MTaxDeclarationAcct (MTaxDeclaration parent, MFactAcct fact)
	{
		super (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(fact);
		setC_TaxDeclaration_ID(parent.getC_TaxDeclaration_ID());
		//
		setFact_Acct_ID (fact.getFact_Acct_ID());
		setC_AcctSchema_ID (fact.getC_AcctSchema_ID());
	}	//	MTaxDeclarationAcct

}	//	MTaxDeclarationAcct
