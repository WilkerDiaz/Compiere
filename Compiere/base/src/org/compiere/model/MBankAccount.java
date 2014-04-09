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
 *  Bank Account Model
 *
 *  @author Jorg Janke
 *  @version $Id: MBankAccount.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MBankAccount extends X_C_BankAccount
{
    /** Logger for class MBankAccount */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBankAccount.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get BankAccount from Cache
	 *	@param ctx context
	 *	@param C_BankAccount_ID id
	 *	@return MBankAccount
	 */
	public static MBankAccount get (Ctx ctx, int C_BankAccount_ID)
	{
		Integer key = Integer.valueOf (C_BankAccount_ID);
		MBankAccount retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MBankAccount (ctx, C_BankAccount_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static final CCache<Integer,MBankAccount>	s_cache
		= new CCache<Integer,MBankAccount>("C_BankAccount", 5);
	
	/**
	 * 	Bank Account Model
	 *	@param ctx context
	 *	@param C_BankAccount_ID bank account
	 *	@param trx transaction
	 */
	public MBankAccount (Ctx ctx, int C_BankAccount_ID, Trx trx)
	{
		super (ctx, C_BankAccount_ID, trx);
		if (C_BankAccount_ID == 0)
		{
			setIsDefault (false);
			setBankAccountType (BANKACCOUNTTYPE_Checking);
			setCurrentBalance (Env.ZERO);
		//	setC_Currency_ID (0);
			setCreditLimit (Env.ZERO);
		//	setC_BankAccount_ID (0);
		}
	}	//	MBankAccount

	/**
	 * 	Bank Account Model
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MBankAccount (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBankAccount

	/**
	 * 	String representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MBankAccount[")
			.append (get_ID())
			.append("-").append(getAccountNo())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Bank
	 *	@return bank parent
	 */
	public MBank getBank()
	{
		return MBank.get(getCtx(), getC_Bank_ID());
	}	//	getBank
	
	/**
	 * 	Get Bank Name and Account No
	 *	@return Bank/Account
	 */
	public String getName()
	{
		return getBank().getName() + " " + getAccountNo();
	}	//	getName
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if valid
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		MBank bank = getBank();
		BankVerificationInterface verify = bank.getVerificationClass();
		if (verify != null)
		{
			String errorMsg = verify.verifyAccountNo (bank, getAccountNo());
			if (errorMsg != null)
			{
				log.saveError("Error", "@Invalid@ @AccountNo@ " + errorMsg);
				return false;
			}
			errorMsg = verify.verifyBBAN (bank, getBBAN());
			if (errorMsg != null)
			{
				log.saveError("Error", "@Invalid@ @BBAN@ " + errorMsg);
				return false;
			}
			errorMsg = verify.verifyIBAN (bank, getIBAN());
			if (errorMsg != null)
			{
				log.saveError("Error", "@Invalid@ @IBAN@ " + errorMsg);
				return false;
			}
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new record
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			return insert_Accounting("C_BankAccount_Acct", "C_AcctSchema_Default", null);
		return success;
	}	//	afterSave
	
	/**
	 * 	Before Delete
	 *	@return true
	 */
	@Override
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_BankAccount_Acct");
	}	//	beforeDelete

}	//	MBankAccount
