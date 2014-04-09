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
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *  BP Bank Account Model
 *
 *  @author Jorg Janke
 *  @version $Id: MBPBankAccount.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MBPBankAccount extends X_C_BP_BankAccount
{
    /** Logger for class MBPBankAccount */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPBankAccount.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 	Get BP Bank Account
	 *	@param ctx context
	 *	@param C_BP_BankAccount_ID bpartner
	 *	@return account or null
	 */
	public static MBPBankAccount get (Ctx ctx, int C_BP_BankAccount_ID)
	{
		MBPBankAccount retValue = null;
		String sql = "SELECT * FROM C_BP_BankAccount WHERE C_BP_BankAccount_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BP_BankAccount_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MBPBankAccount(ctx, rs, null);
		} 
		catch (Exception e) 
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return retValue;
	}	//	get

	/**
	 * 	Get Bank Account Of BPartner
	 *	@param ctx context
	 *	@param C_BPartner_ID bpartner
	 *	@return
	 */
	public static MBPBankAccount[] getOfBPartner (Ctx ctx, int C_BPartner_ID)
	{
		String sql = "SELECT * FROM C_BP_BankAccount WHERE C_BPartner_ID=? AND IsActive='Y'";
		ArrayList<MBPBankAccount> list = new ArrayList<MBPBankAccount>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				list.add(new MBPBankAccount(ctx, rs, null));
			}
		} 
		catch (Exception e) 
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		MBPBankAccount[] retValue = new MBPBankAccount[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfBPartner

	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger(MBPBankAccount.class);
	
	
	/**************************************************************************
	 * 	Constructor
	 *	@param ctx context
	 *	@param C_BP_BankAccount_ID BP bank account
	 *	@param trx transaction
	 */
	public MBPBankAccount (Ctx ctx, int C_BP_BankAccount_ID, Trx trx)
	{
		super (ctx, C_BP_BankAccount_ID, trx);
		if (C_BP_BankAccount_ID == 0)
		{
		//	setC_BPartner_ID (0);
			setIsACH (false);
			setBPBankAcctUse(BPBANKACCTUSE_Both);
		}
	}	//	MBP_BankAccount

	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MBPBankAccount (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBP_BankAccount

	/**
	 * 	Constructor
	 *	@param ctx context
	 * 	@param bp BP
	 *	@param bpc BP Contact
	 * 	@param location Location
	 */
	public MBPBankAccount (Ctx ctx, MBPartner bp, MUser bpc, MLocation location)
	{
		this(ctx, 0, bp.get_Trx());
		setIsACH (false);
		//
		setC_BPartner_ID(bp.getC_BPartner_ID());
		//
		setA_Name(bp.getName());
		setA_EMail(bpc.getEMail());
		//
		setA_Street(location.getAddress1());
		setA_City(location.getCity());
		setA_Zip(location.getPostal());
		setA_State(location.getRegionName(true));
		setA_Country(location.getCountryName());
	}	//	MBP_BankAccount

	/**
	 * 	Detail New Constructor
	 *	@param ctx context
	 *	@param C_BPartner_ID parent
	 *	@param AccountNo account
	 *	@param RoutingNo routing
	 *	@param IBAN iban
	 *	@param trx transaction
	 */
	public MBPBankAccount (Ctx ctx, int C_BPartner_ID, 
		String AccountNo, String RoutingNo, String IBAN,
		Trx trx)
	{
		super (ctx, 0, trx);
		setC_BPartner_ID (C_BPartner_ID);
		setIsACH (true);
		setBPBankAcctUse(BPBANKACCTUSE_Both);
		setAccountNo(AccountNo);
		setRoutingNo(RoutingNo);
		setIBAN(IBAN);
	}	//	MBP_BankAccount

	
	/** Bank Link			*/
	private MBank		m_bank = null;

	/**
	 * 	Is Direct Deposit
	 *	@return true if dd
	 */
	public boolean isDirectDeposit()
	{
		if (!isACH())
			return false;
		String s = getBPBankAcctUse();
		if (s == null)
			return true;
		return (s.equals(BPBANKACCTUSE_Both) || s.equals(BPBANKACCTUSE_DirectDeposit));
	}	//	isDirectDeposit
	
	/**
	 * 	Is Direct Debit
	 *	@return true if dd
	 */
	public boolean isDirectDebit()
	{
		if (!isACH())
			return false;
		String s = getBPBankAcctUse();
		if (s == null)
			return true;
		return (s.equals(BPBANKACCTUSE_Both) || s.equals(BPBANKACCTUSE_DirectDebit));
	}	//	isDirectDebit
	
	
	/**
	 * 	Get Bank
	 *	@return bank
	 */
	public MBank getBank()
	{
		int C_Bank_ID = getC_Bank_ID();
		if (C_Bank_ID == 0)
			return null;
		if (m_bank == null)
			m_bank = new MBank (getCtx(), C_Bank_ID, get_Trx());
		return m_bank;
	}	//	getBank
	
	/**
	 * 	Get Routing No
	 *	@return routing No
	 */
	@Override
	public String getRoutingNo() 
	{
		MBank bank = getBank();
		String rt = super.getRoutingNo();
		if (bank != null)
			rt = bank.getRoutingNo();
		return rt;
	}	//	getRoutingNo

	/**
	 * 	Update Bank Info
	 *	@param AccountNo account
	 *	@param RoutingNo routing
	 *	@param IBAN iban
	 *	@return true if changed
	 */
	public boolean updateInfo(String AccountNo, String RoutingNo, String IBAN)
	{
		boolean change = false;
		if (!Util.isEmpty(AccountNo) && !Util.isEqual(AccountNo, getAccountNo()))
		{
			setAccountNo(AccountNo);
			change = true;
		}
		if (!Util.isEmpty(RoutingNo) && !Util.isEqual(RoutingNo, getRoutingNo()))
		{
			setRoutingNo(RoutingNo);
			change = true;
		}
		if (!Util.isEmpty(IBAN) && !Util.isEqual(IBAN, getIBAN()))
		{
			setIBAN(IBAN);
			change = true;
		}
		return change;
	}	//	updateInfo
	
	/**
	 * 	Get Account Name
	 *	@param fromBP if true and not existing, get from BP
	 *	@return account name
	 */
	public String getA_Name(boolean fromBP)
	{
		String name = super.getA_Name();
		if (Util.isEmpty(name))
		{
			MBPartner bp = MBPartner.get(getCtx(), getC_BPartner_ID());
			name = bp.getName();
		}
		return name;
	}	//	getA_Name
	
	/**
	 * 	Get Account City 
	 *	@param fromBP if true and not existing, get from BP
	 *	@return account city
	 */
	public String getA_City(boolean fromBP)
	{
		String city = super.getA_City();
		if (Util.isEmpty(city))
		{
			MBPartner bp = MBPartner.get(getCtx(), getC_BPartner_ID());
			MBPartnerLocation[] locs = bp.getLocations(false);
			if (locs.length > 0)
			{
				MLocation addr = MLocation.get(getCtx(), locs[0].getC_Location_ID(), null);
				city = addr.getCity();
			}
		}
		return city;
	}	//	getA_City
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord) 
	{
		//	maintain routing on bank level
		if (isACH() && getBank() != null)
			setRoutingNo(null);
		//	Verify Bank
		MBank bank = getBank();
		if (bank != null)
		{
			BankVerificationInterface verify = bank.getVerificationClass();
			if (verify != null)
			{
				String errorMsg = verify.verifyRoutingNo (bank.getC_Country_ID(), getRoutingNo());
				if (errorMsg != null)
				{
					log.saveError("Error", "@Invalid@ @RoutingNo@ " + errorMsg);
					return false;
				}
				//
				errorMsg = verify.verifyAccountNo (bank, getAccountNo());
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
		}
		//
		return true;
	}	//	beforeSave
	
	/**
	 *	String Representation
	 * 	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MBP_BankAccount[")
			.append (get_ID ())
			.append(", Name=").append(getA_Name())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MBPBankAccount
