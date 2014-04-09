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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;


/**
 *  Payment Processor Model
 *
 *  @author Jorg Janke
 *  @version $Id: MPaymentProcessor.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MPaymentProcessor extends X_C_PaymentProcessor
{
    /** Logger for class MPaymentProcessor */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPaymentProcessor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get BankAccount & PaymentProcessor
	 * 	@param ctx context
	 *  @param tender optional Tender see TENDER_
	 *  @param CCType optional CC Type see CC_
	 *  @param AD_Client_ID Client
	 *  @param C_Currency_ID Currency (ignored)
	 *  @param Amt Amount (ignored)
	 *	@param trx transaction
	 *  @return Array of BankAccount[0] & PaymentProcessor[1] or null
	 */
	protected static MPaymentProcessor[] find (Ctx ctx,
		String tender, String CCType,
		int AD_Client_ID, int C_Currency_ID, BigDecimal Amt, Trx trx)
	{
		ArrayList<MPaymentProcessor> list = new ArrayList<MPaymentProcessor>();
		StringBuffer sql = new StringBuffer("SELECT * "
			+ "FROM C_PaymentProcessor "
			+ "WHERE AD_Client_ID=? AND IsActive='Y'"				//	#1
			+ " AND (C_Currency_ID IS NULL OR C_Currency_ID=?)"		//	#2
			+ " AND (MinimumAmt IS NULL OR MinimumAmt = 0 OR MinimumAmt <= ?)");	//	#3
		if (X_C_Payment.TENDERTYPE_DirectDeposit.equals(tender))
			sql.append(" AND AcceptDirectDeposit='Y'");
		else if (X_C_Payment.TENDERTYPE_DirectDebit.equals(tender))
			sql.append(" AND AcceptDirectDebit='Y'");
		else if (X_C_Payment.TENDERTYPE_Check.equals(tender))
			sql.append(" AND AcceptCheck='Y'");
		//  CreditCards
		else if (X_C_Payment.CREDITCARDTYPE_ATM.equals(CCType))
			sql.append(" AND AcceptATM='Y'");
		else if (X_C_Payment.CREDITCARDTYPE_Amex.equals(CCType))
			sql.append(" AND AcceptAMEX='Y'");
		else if (X_C_Payment.CREDITCARDTYPE_Visa.equals(CCType))
			sql.append(" AND AcceptVISA='Y'");
		else if (X_C_Payment.CREDITCARDTYPE_MasterCard.equals(CCType))
			sql.append(" AND AcceptMC='Y'");
		else if (X_C_Payment.CREDITCARDTYPE_Diners.equals(CCType))
			sql.append(" AND AcceptDiners='Y'");
		else if (X_C_Payment.CREDITCARDTYPE_Discover.equals(CCType))
			sql.append(" AND AcceptDiscover='Y'");
		else if (X_C_Payment.CREDITCARDTYPE_PurchaseCard.equals(CCType))
			sql.append(" AND AcceptCORPORATE='Y'");
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trx);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, C_Currency_ID);
			pstmt.setBigDecimal(3, Amt);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MPaymentProcessor (ctx, rs, trx));
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "find - " + sql, e);
			return null;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		if (list.size() == 0)
			s_log.warning("find - not found - AD_Client_ID=" + AD_Client_ID
				+ ", C_Currency_ID=" + C_Currency_ID + ", Amt=" + Amt);
		else
			s_log.fine("find - #" + list.size() + " - AD_Client_ID=" + AD_Client_ID
				+ ", C_Currency_ID=" + C_Currency_ID + ", Amt=" + Amt);
		MPaymentProcessor[] retValue = new MPaymentProcessor[list.size()];
		list.toArray(retValue);
		return retValue;
	}   //  find
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MPaymentProcessor.class);

	
	/**************************************************************************
	 *	Payment Processor Model
	 * 	@param ctx context
	 * 	@param C_PaymentProcessor_ID payment processor
	 *	@param trx transaction
	 */
	public MPaymentProcessor (Ctx ctx, int C_PaymentProcessor_ID, Trx trx)
	{
		super (ctx, C_PaymentProcessor_ID, trx);
		if (C_PaymentProcessor_ID == 0)
		{
		//	setC_BankAccount_ID (0);		//	Parent
		//	setUserID (null);
		//	setPassword (null);
		//	setHostAddress (null);
		//	setHostPort (0);
			setCommission (Env.ZERO);
			setAcceptVisa (false);
			setAcceptMC (false);
			setAcceptAMEX (false);
			setAcceptDiners (false);
			setCostPerTrx (Env.ZERO);
			setAcceptCheck (false);
			setRequireVV (false);
			setAcceptCorporate (false);
			setAcceptDiscover (false);
			setAcceptATM (false);
			setAcceptDirectDeposit(false);
			setAcceptDirectDebit(false);
		//	setName (null);
		}
	}	//	MPaymentProcessor

	/**
	 *	Payment Processor Model
	 * 	@param ctx context
	 * 	@param rs result set
	 *	@param trx transaction
	 */
	public MPaymentProcessor (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPaymentProcessor

	/**
	 * 	String representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPaymentProcessor[")
			.append(get_ID ()).append("-").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Does Payment Processor accepts tender / CC
	 *	@param TenderType tender type
	 *	@param CreditCardType credit card type
	 *	@return true if acceptes
	 */
	public boolean accepts (String TenderType, String CreditCardType)
	{
		if ((X_C_Payment.TENDERTYPE_DirectDeposit.equals(TenderType) && isAcceptDirectDeposit())
			|| (X_C_Payment.TENDERTYPE_DirectDebit.equals(TenderType) && isAcceptDirectDebit())
			|| (X_C_Payment.TENDERTYPE_Check.equals(TenderType) && isAcceptCheck())
			//
			|| (X_C_Payment.CREDITCARDTYPE_ATM.equals(CreditCardType) && isAcceptATM())
			|| (X_C_Payment.CREDITCARDTYPE_Amex.equals(CreditCardType) && isAcceptAMEX())
			|| (X_C_Payment.CREDITCARDTYPE_PurchaseCard.equals(CreditCardType) && isAcceptCorporate())
			|| (X_C_Payment.CREDITCARDTYPE_Diners.equals(CreditCardType) && isAcceptDiners())
			|| (X_C_Payment.CREDITCARDTYPE_Discover.equals(CreditCardType) && isAcceptDiscover())
			|| (X_C_Payment.CREDITCARDTYPE_MasterCard.equals(CreditCardType) && isAcceptMC())
			|| (X_C_Payment.CREDITCARDTYPE_Visa.equals(CreditCardType) && isAcceptVisa()))
			return true;
		return false;
	}	//	accepts

}	//	MPaymentProcessor
