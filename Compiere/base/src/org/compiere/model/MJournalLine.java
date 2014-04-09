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

import org.compiere.api.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *  Journal Line Model
 *
 *	@author Jorg Janke
 *	@version $Id: MJournalLine.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MJournalLine extends X_GL_JournalLine
{
    /** Logger for class MJournalLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MJournalLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int I_GLJournal_ID=0;
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param GL_JournalLine_ID id
	 *	@param trx transaction
	 */
	public MJournalLine (Ctx ctx, int GL_JournalLine_ID, Trx trx)
	{
		super (ctx, GL_JournalLine_ID, trx);
		if (GL_JournalLine_ID == 0)
		{
		//	setGL_JournalLine_ID (0);		//	PK
		//	setGL_Journal_ID (0);			//	Parent
		//	setC_Currency_ID (0);
		//	setC_ValidCombination_ID (0);
			setLine (0);
			setAmtAcctCr (Env.ZERO);
			setAmtAcctDr (Env.ZERO);
			setAmtSourceCr (Env.ZERO);
			setAmtSourceDr (Env.ZERO);
			setCurrencyRate (Env.ONE);
		//	setC_ConversionType_ID (0);
			setDateAcct (new Timestamp(System.currentTimeMillis()));
			setIsGenerated (true);
		}
	}	//	MJournalLine
	
	public MJournalLine(X_I_GLJournal imp)
	{
		this(imp.getCtx(), 0, imp.get_Trx());	
	    PO.copyValues(imp, this, imp.getAD_Client_ID(), imp.getAD_Org_ID());
		I_GLJournal_ID=imp.getI_GLJournal_ID();
		
		if (imp.getDateAcct()==null)
			setDateAcct (new Timestamp(System.currentTimeMillis()));

		setC_Currency_ID(imp.getC_Currency_ID());
		setCurrencyRate(imp.getCurrencyRate());

	}

	public int getI_GLJournal_ID()
	{
		return I_GLJournal_ID;
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MJournalLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MJournalLine

	/**
	 * 	Parent Constructor
	 *	@param parent journal
	 */
	public MJournalLine (MJournal parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setGL_Journal_ID(parent.getGL_Journal_ID());
		setC_Currency_ID(parent.getC_Currency_ID());
		setC_ConversionType_ID(parent.getC_ConversionType_ID());
		setDateAcct(parent.getDateAcct());
		
	}	//	MJournalLine

	/**	Currency Precision		*/
	private int					m_precision = -1;
	/**	Account Combination		*/
	private MAccount		 	m_account = null;
	/** Account Element			*/
	private MElementValue		m_accountElement = null;
	
	/**
	 * 	Set Currency Info
	 *	@param C_Currency_ID currenct
	 *	@param C_ConversionType_ID type
	 *	@param CurrencyRate rate
	 */
	public void setCurrency (int C_Currency_ID, int C_ConversionType_ID, BigDecimal CurrencyRate)
	{
		setC_Currency_ID(C_Currency_ID);
		if (C_ConversionType_ID != 0)
			setC_ConversionType_ID(C_ConversionType_ID);
		if (CurrencyRate != null && CurrencyRate.signum() == 0)
			setCurrencyRate(CurrencyRate);
	}	//	setCurrency

	/**
	 * 	Set C_Currency_ID and precision
	 *	@param C_Currency_ID currency
	 */
	@Override
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID == 0)
			return;
		super.setC_Currency_ID (C_Currency_ID);		
	}	//	setC_Currency_ID
	
	/**
	 * 	Get Acct Schema Currency Precision
	 *	@return precision
	 */
	public int getPrecision()
	{
		if (m_precision < 0)
		{
			MJournal journal = new MJournal (getCtx(), getGL_Journal_ID(), get_Trx());
			MAcctSchema acctSchema = new MAcctSchema (getCtx(), journal.getC_AcctSchema_ID(), get_Trx());
			m_precision = acctSchema.getStdPrecision();
		}
		return m_precision;
	}	//	getPrecision
	
	/**
	 * 	Set Currency Rate
	 *	@param CurrencyRate check for null (->one)
	 */
	@Override
	public void setCurrencyRate (BigDecimal CurrencyRate)
	{
		if (CurrencyRate == null)
		{
			log.warning("was NULL - set to 1");
			super.setCurrencyRate (Env.ONE);
		}
		else if (CurrencyRate.signum() < 0)
		{
			log.warning("negative - " + CurrencyRate + " - set to 1");
			super.setCurrencyRate (Env.ONE);
		}
		else
			super.setCurrencyRate (CurrencyRate);
	}	//	setCurrencyRate
	

	/**
	 * 	Set Rate - Callout.
	 *	@param oldC_ConversionType_ID old
	 *	@param newC_ConversionType_ID new
	 *	@param windowNo window no
	 */
	@UICallout public void setC_ConversionType_ID (String oldC_ConversionType_ID, 
			String newC_ConversionType_ID, int windowNo) throws Exception
	{
		if (newC_ConversionType_ID == null || newC_ConversionType_ID.length() == 0)
			return;
		int C_ConversionType_ID = Integer.parseInt(newC_ConversionType_ID);
		if (C_ConversionType_ID == 0)
			return;
		setC_ConversionType_ID(C_ConversionType_ID);
		setRate(windowNo);
	}	//	setC_ConversionType_ID
	
	/**
	 * 	Set Rate - Callout.
	 *	@param oldC_ConversionType_ID old
	 *	@param newC_ConversionType_ID new
	 *	@param windowNo window no
	 */
	@UICallout public void setC_AccountAlias_ID (String oldC_AccountAlias_ID, 
			String newC_AccountAlias_ID, int windowNo) throws Exception
	{
		if (newC_AccountAlias_ID == null || newC_AccountAlias_ID.length() == 0)
			return;
		int C_AccountAlias_ID = Integer.parseInt(newC_AccountAlias_ID);
		if (C_AccountAlias_ID == 0)
			return;
		setC_AccountAlias_ID(C_AccountAlias_ID);
		setC_ValidCombination_ID(C_AccountAlias_ID);
	}	//	setC_ConversionType_ID

	/**
	 * 	Set Rate - Callout.
	 *	@param oldC_ConversionType_ID old
	 *	@param newC_ConversionType_ID new
	 *	@param windowNo window no
	 */
	@UICallout public void setC_ValidCombination_ID (String oldC_ValidCombination_ID, 
			String newC_ValidCombination_ID, int windowNo) throws Exception
	{
		if (newC_ValidCombination_ID == null || newC_ValidCombination_ID.length() == 0)
			return;
		int C_ValidCombination_ID = Integer.parseInt(newC_ValidCombination_ID);
		if (C_ValidCombination_ID == 0)
			return;
		setC_AccountAlias_ID(C_ValidCombination_ID);
		setC_ValidCombination_ID(C_ValidCombination_ID);
	}
	/**
	 * 	Set DateAcct - Callout.
	 *	@param oldDateAcct old
	 *	@param newDateAcct new
	 *	@param windowNo window no
	 */
	@UICallout public void setDateAcct (String oldDateAcct, 
			String newDateAcct, int windowNo) throws Exception
	{
		if (newDateAcct == null || newDateAcct.length() == 0)
			return;
		Timestamp dateAcct = PO.convertToTimestamp(newDateAcct);
		setDateAcct(dateAcct);
		setRate(windowNo);
	}	//	setDateAcct(String, String, int)

	/**
	 * 	Set Currency - Callout.
	 *	@param oldC_Currency_ID old
	 *	@param newC_Currency_ID new
	 *	@param windowNo window no
	 */
	@UICallout public void setC_Currency_ID (String oldC_Currency_ID, 
			String newC_Currency_ID, int windowNo) throws Exception
	{
		if (newC_Currency_ID == null || newC_Currency_ID.length() == 0)
			return;
		int C_Currency_ID = Integer.parseInt(newC_Currency_ID);
		if (C_Currency_ID == 0)
			return;
		setC_Currency_ID(C_Currency_ID);
		setRate(windowNo);
	}	//	setC_Currency_ID

	/**
	 * 	Set Rate
	 */
	private void setRate(int windowNo)
	{
		//  Source info
		int C_Currency_ID = getC_Currency_ID();
		int C_ConversionType_ID = getC_ConversionType_ID();
		if (C_Currency_ID == 0 || C_ConversionType_ID == 0)
			return;
		Timestamp DateAcct = getDateAcct();
		if (DateAcct == null)
			DateAcct = new Timestamp(System.currentTimeMillis());
		//
		int C_AcctSchema_ID = getCtx().getContextAsInt(windowNo, "C_AcctSchema_ID");
		MAcctSchema as = MAcctSchema.get (getCtx(), C_AcctSchema_ID);
		int AD_Client_ID = getAD_Client_ID();
		int AD_Org_ID = getAD_Org_ID();

		BigDecimal CurrencyRate = MConversionRate.getRate(C_Currency_ID, as.getC_Currency_ID(), 
			DateAcct, C_ConversionType_ID, AD_Client_ID, AD_Org_ID);
		log.fine("rate = " + CurrencyRate);
		if (CurrencyRate == null)
			CurrencyRate = Env.ZERO;
		setCurrencyRate(CurrencyRate);
		setAmt(windowNo);
	}	//	setRate


	/**
	 * 	Set Accounted Amounts only if not 0.
	 * 	Amounts overwritten in beforeSave - set conversion rate
	 *	@param AmtAcctDr Dr
	 *	@param AmtAcctCr Cr
	 */
	public void setAmtAcct (BigDecimal AmtAcctDr, BigDecimal AmtAcctCr)
	{
		//	setConversion
		double rateDR = 0;
		if (AmtAcctDr != null && AmtAcctDr.signum() != 0)
		{
			rateDR = AmtAcctDr.doubleValue() / getAmtSourceDr().doubleValue();
			super.setAmtAcctDr(AmtAcctDr);
		}
		double rateCR = 0;
		if (AmtAcctCr != null && AmtAcctCr.signum() != 0)
		{
			rateCR = AmtAcctCr.doubleValue() / getAmtSourceCr().doubleValue();
			super.setAmtAcctCr(AmtAcctCr);
		}
		if (rateDR != 0 && rateCR != 0 && rateDR != rateCR)
		{
			log.warning("Rates Different DR=" + rateDR + "(used) <> CR=" + rateCR + "(ignored)");
			rateCR = 0;
		}
		if (rateDR < 0 || Double.isInfinite(rateDR) || Double.isNaN(rateDR))
		{
			log.warning("DR Rate ignored - " + rateDR);
			return;
		}
		if (rateCR < 0 || Double.isInfinite(rateCR) || Double.isNaN(rateCR))
		{
			log.warning("CR Rate ignored - " + rateCR);
			return;
		}
		
		if (rateDR != 0)
			setCurrencyRate(new BigDecimal(rateDR));
		if (rateCR != 0)
			setCurrencyRate(new BigDecimal(rateCR));
	}	//	setAmtAcct

	/**
	 * 	Set AmtSourceCr - Callout
	 *	@param oldAmtSourceCr old value
	 *	@param newAmtSourceCr new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setAmtSourceCr (String oldAmtSourceCr, 
			String newAmtSourceCr, int windowNo) throws Exception
	{
		if (newAmtSourceCr == null || newAmtSourceCr.length() == 0)
			return;
		BigDecimal AmtSourceCr = new BigDecimal(newAmtSourceCr);
		super.setAmtSourceCr(AmtSourceCr);
		setAmt(windowNo);
	}	//	setAmtSourceCr
	
	/**
	 * 	Set AmtSourceDr - Callout
	 *	@param oldAmtSourceDr old value
	 *	@param newAmtSourceDr new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setAmtSourceDr (String oldAmtSourceDr, 
			String newAmtSourceDr, int windowNo) throws Exception
	{
		if (newAmtSourceDr == null || newAmtSourceDr.length() == 0)
			return;
		BigDecimal AmtSourceDr = new BigDecimal(newAmtSourceDr);
		super.setAmtSourceDr(AmtSourceDr);
		setAmt(windowNo);
	}	//	setAmtSourceDr
	
	/**
	 * 	Set CurrencyRate - Callout
	 *	@param oldCurrencyRate old value
	 *	@param newCurrencyRate new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setCurrencyRate (String oldCurrencyRate, 
			String newCurrencyRate, int windowNo) throws Exception
	{
		if (newCurrencyRate == null || newCurrencyRate.length() == 0)
			return;
		BigDecimal CurrencyRate = new BigDecimal(newCurrencyRate);
		super.setCurrencyRate(CurrencyRate);
		setAmt(windowNo);
	}	//	setCurrencyRate

	
	/**
	 * 	Set Accounted Amounts
	 *	@param windowNo window
	 */
	private void setAmt(int windowNo)
	{
		//  Get Target Currency & Precision from C_AcctSchema.C_Currency_ID
		int C_AcctSchema_ID = getCtx().getContextAsInt(windowNo, "C_AcctSchema_ID");
		MAcctSchema as = MAcctSchema.get(getCtx(), C_AcctSchema_ID);
		int Precision = as.getStdPrecision();

		BigDecimal CurrencyRate = getCurrencyRate();
		if (CurrencyRate == null)
		{
			CurrencyRate = Env.ONE;
			setCurrencyRate(CurrencyRate);
		}

		//  AmtAcct = AmtSource * CurrencyRate  ==> Precision
		BigDecimal AmtSourceDr = getAmtSourceDr();
		if (AmtSourceDr == null)
			AmtSourceDr = Env.ZERO;
		BigDecimal AmtSourceCr = getAmtSourceCr();
		if (AmtSourceCr == null)
			AmtSourceCr = Env.ZERO;

		BigDecimal AmtAcctDr = AmtSourceDr.multiply(CurrencyRate);
		AmtAcctDr = AmtAcctDr.setScale(Precision, BigDecimal.ROUND_HALF_UP);
		setAmtAcctDr(AmtAcctDr);
		BigDecimal AmtAcctCr = AmtSourceCr.multiply(CurrencyRate);
		AmtAcctCr = AmtAcctCr.setScale(Precision, BigDecimal.ROUND_HALF_UP);
		setAmtAcctCr(AmtAcctCr);
	}	//	setAmt
	
	
	/**
	 * 	Set C_ValidCombination_ID
	 *	@param C_ValidCombination_ID id
	 */
	@Override
	public void setC_ValidCombination_ID (int C_ValidCombination_ID)
	{
		super.setC_ValidCombination_ID (C_ValidCombination_ID);
		m_account = null;
		m_accountElement = null;
	}	//	setC_ValidCombination_ID
	
	/**
	 * 	Set C_ValidCombination_ID
	 *	@param acct account
	 */
	public void setC_ValidCombination_ID (MAccount acct)
	{
		if (acct == null)
			throw new IllegalArgumentException("Account is null");
		super.setC_ValidCombination_ID (acct.getC_ValidCombination_ID());
		m_account = acct;
		m_accountElement = null;
	}	//	setC_ValidCombination_ID

	/**
	 * 	Get Account (Valid Combination)
	 *	@return combination or null
	 */
	public MAccount getAccount()
	{
		if (m_account == null && getC_ValidCombination_ID() != 0)
			m_account = new MAccount (getCtx(), getC_ValidCombination_ID(), get_Trx());
		return m_account;
	}	//	getValidCombination
	
	/**
	 * 	Get Natural Account Element Value
	 *	@return account
	 */
	public MElementValue getAccountElementValue()
	{
		if (m_accountElement == null)
		{
			MAccount vc = getAccount();
			if (vc != null && vc.getAccount_ID() != 0)
				m_accountElement = new MElementValue (getCtx(), vc.getAccount_ID(), get_Trx()); 
		}
		return m_accountElement;
	}	//	getAccountElement
	
	/**
	 * 	Is it posting to a Control Acct
	 *	@return true if control acct
	 */
	public boolean isDocControlled()
	{
		MElementValue acct = getAccountElementValue();
		if (acct == null)
		{
			log.warning ("Account not found for C_ValidCombination_ID=" + getC_ValidCombination_ID());
			return false;
		}
		return acct.isDocControlled();
	}	//	isDocControlled
	
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Acct Amts
		BigDecimal rate = getCurrencyRate();
		BigDecimal amt = rate.multiply(getAmtSourceDr());
		if (amt.scale() > getPrecision())
			amt = amt.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP);
		setAmtAcctDr(amt);
		amt = rate.multiply(getAmtSourceCr());
		if (amt.scale() > getPrecision())
			amt = amt.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP);
		setAmtAcctCr(amt);
		//	Set Line Org to Acct Org
		if (newRecord 
			|| is_ValueChanged("C_ValidCombination_ID")
			|| is_ValueChanged("AD_Org_ID"))
			setAD_Org_ID(getAccount().getAD_Org_ID());
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Update Journal/Batch Total
	 *	@param newRecord true if new record
	 *	@param success true if success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		if (!getCtx().isBatchMode())
			return updateJournalTotal();
		
		return success;
		
	}	//	afterSave
	
	
	/**
	 * 	After Delete
	 *	@param success true if deleted
	 *	@return true if success
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return updateJournalTotal();
	}	//	afterDelete

	
	/**
	 * 	Update Journal and Batch Total
	 *	@return true if success
	 */
	private boolean updateJournalTotal()
	{
		//	Update Journal Total
		String sql = "UPDATE GL_Journal j"
			+ " SET TotalDr = (SELECT COALESCE(SUM(AmtSourceDr),0)" 
				           + " FROM GL_JournalLine jl "
				           + " WHERE jl.IsActive='Y' "
				           + " AND j.GL_Journal_ID=jl.GL_Journal_ID), "
				+ " TotalCr = (SELECT COALESCE(SUM(AmtSourceCr),0)" 
				           + " FROM GL_JournalLine jl "
				           + " WHERE jl.IsActive='Y' "
				           + " AND j.GL_Journal_ID=jl.GL_Journal_ID) "
			+ " WHERE GL_Journal_ID= ? ";
		int no = DB.executeUpdate(get_Trx(), sql,getGL_Journal_ID());
		if (no != 1)
			log.warning("afterSave - Update Journal #" + no);
		
		MJournal journal = new MJournal (getCtx(), getGL_Journal_ID(), get_Trx());
		if (journal.getGL_JournalBatch_ID()!=0)
		{
			//	Update Batch Total
			sql = "UPDATE GL_JournalBatch jb"
				+ " SET TotalDr = (SELECT COALESCE(SUM(TotalDr),0) " 
					           + " FROM GL_Journal j "
					           + " WHERE jb.GL_JournalBatch_ID=j.GL_JournalBatch_ID), "
					+ " TotalCr = (SELECT COALESCE(SUM(TotalCr),0) " 
					           + " FROM GL_Journal j "
					           + " WHERE jb.GL_JournalBatch_ID=j.GL_JournalBatch_ID) "
				+ "WHERE GL_JournalBatch_ID="
					+ "(SELECT DISTINCT GL_JournalBatch_ID FROM GL_Journal WHERE GL_Journal_ID= ?)";
			no = DB.executeUpdate(get_Trx(), sql,getGL_Journal_ID());
			if (no != 1)
				log.warning("Update Batch #" + no);
		}
		return no == 1;
	}	//	updateJournalTotal
	
}	//	MJournalLine
