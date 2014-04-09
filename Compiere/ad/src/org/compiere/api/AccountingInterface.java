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
package org.compiere.api;

import java.math.*;

/**
 * 	Compiere Accounting Interface for Base Document Types.
 * 	
 *	@author Jorg Janke
 */
public interface AccountingInterface
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trx p_trx
	 */
	//	must implement - cannot enforce here
//	public <init> (MAcctSchema[] ass, ResultSet rs, Trx trx);
	
	/**
	 *  Load Document Details
	 *  @return error message or null
	 */
	public String loadDocumentDetails();
	
	/**
	 *  Get Source Currency Balance - subtracts line (and tax) amounts from total - no rounding
	 *  @return positive amount, if total header is bigger than lines
	 */
	public BigDecimal getBalance();
	
	/**
	 *  Create Facts (the accounting logic)
	 *  @param as accounting schema
	 *  @return Facts
	 */
	//	made to comment as Fact is not a class in AD - must be implemented	
//	public ArrayList<Fact> createFacts (MAcctSchema as);
		
}	//	AccountingInterface
