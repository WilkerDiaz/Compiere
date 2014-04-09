/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.util;

import org.compiere.model.*;


/**
 *	Bank Data Verification Interface
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public interface BankVerificationInterface
{
	/**
	 * 	Verify Routing No
	 * 	@param C_Country_ID country
	 *	@param RoutingNo Routing Number
	 *	@return error message or null
	 */
	public String verifyRoutingNo (int C_Country_ID, String RoutingNo);

	/**
	 * 	Verify Swift Code or BIC
	 *	@param SwiftCode Swift Code
	 *	@return error message or null
	 */
	public String verifySwiftCode (String SwiftCode);

	
	/**
	 * 	Verify Basic Bank Account Number
	 * 	@oaram bank the bank
	 *	@param BBAN Basic Bank Account Number
	 *	@return error message or null
	 */
	public String verifyBBAN (MBank bank, String BBAN);

	/**
	 * 	Verify International Bank Account Number
	 * 	@oaram bank the bank
	 *	@param IBAN International Bank Account Number
	 *	@return error message or null
	 */
	public String verifyIBAN (MBank bank, String IBAN);
	
	/**
	 * 	Verify Account Number.
	 * 	@oaram bank the bank
	 *	@param AccountNo Bank Account Number
	 *	@return error message or null
	 */
	public String verifyAccountNo (MBank bank, String AccountNo);
	
	
	/**
	 * 	Validate Credit Card Number.
	 * 	(not used at the moment)
	 *	@param creditCardNumber credit card number
	 *	@param creditCardType credit card type X_C_Payment.CREDITCARDTYPE_
	 *	@return error message or null
	 */
	public String validateCreditCardNumber (String creditCardNumber, String creditCardType);
	
	
}	//	BankVerificationInterface
