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
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Natural Account
 *
 *  @author Jorg Janke
 *  @version $Id: MElementValue.java 8898 2010-06-06 16:14:49Z ragrawal $
 */
public class MElementValue extends X_C_ElementValue
{
    /** Logger for class MElementValue */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MElementValue.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Account of Element
	 *	@param ctx context
	 *	@param C_Element_ID parent id
	 *	@param Value key
	 *	@return account element value or null
	 */
	public static MElementValue get(Ctx ctx, int C_Element_ID, String Value)
	{
		MElementValue retValue = null;
		String sql = "SELECT * FROM C_ElementValue "
			+ "WHERE C_Element_ID=? AND Value=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, (Trx) null);
	        pstmt.setInt(1, C_Element_ID);
	        pstmt.setString(2, Value);
	        rs = pstmt.executeQuery();
	        if (rs.next())
	        	retValue = new MElementValue(ctx, rs, null);
        }
        catch (Exception e) {
        	s_log.log(Level.SEVERE, sql, e);
        }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get
	
	/**	Logger	*/
    private static CLogger s_log = CLogger.getCLogger(MElementValue.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ElementValue_ID ID or 0 for new
	 *	@param trx transaction
	 */
	public MElementValue(Ctx ctx, int C_ElementValue_ID, Trx trx)
	{
		super(ctx, C_ElementValue_ID, trx);
		if (C_ElementValue_ID == 0)
		{
		//	setC_Element_ID (0);	//	Parent
		//	setName (null);
		//	setValue (null);
			setIsSummary (false);
			setAccountSign (ACCOUNTSIGN_Natural);
			setAccountType (ACCOUNTTYPE_Expense);
			setIsDocControlled(false);
			setIsForeignCurrency(false);
			setIsBankAccount(false);
			//
			setPostActual (true);
			setPostBudget (true);
			setPostEncumbrance (true);
			setPostStatistical (true);
		}
	}	//	MElementValue

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MElementValue(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MElementValue

	/**
	 * 	Full Constructor.
	 * 	(Need to set Org & Element_ID)
	 *	@param ctx context
	 *	@param Value value
	 *	@param Name name
	 *	@param Description description
	 *	@param AccountType account type
	 *	@param AccountSign account sign
	 *	@param IsDocControlled doc controlled
	 *	@param IsSummary summary
	 *	@param trx transaction
	 */
	public MElementValue (Ctx ctx, String Value, String Name, String Description,
		String AccountType, String AccountSign,
		boolean IsDocControlled, boolean IsSummary, Trx trx)
	{
		this (ctx, 0, trx);
		setValue(Value);
		setName(Name);
		setDescription(Description);
		setAccountType(AccountType);
		setAccountSign(AccountSign);
		setIsDocControlled(IsDocControlled);
		setIsSummary(IsSummary);
	}	//	MElementValue
	
	/**
	 * 	Import Constructor
	 *	@param imp import
	 */
	public MElementValue (X_I_ElementValue imp)
	{
		this (imp.getCtx(), 0, imp.get_Trx());
		setClientOrg(imp);
		set(imp);
	}	//	MElementValue

	/**
	 * 	Set/Update Settings from import
	 *	@param imp import
	 */
	public void set (X_I_ElementValue imp)
	{
		setValue(imp.getValue());
		setName(imp.getName());
		setDescription(imp.getDescription());
		setAccountType(imp.getAccountType());
		setAccountSign(imp.getAccountSign());
		setIsSummary(imp.isSummary());
		setIsDocControlled(imp.isDocControlled());
		setC_Element_ID(imp.getC_Element_ID());
		//
		setPostActual(imp.isPostActual());
		setPostBudget(imp.isPostBudget());
		setPostEncumbrance(imp.isPostEncumbrance());
		setPostStatistical(imp.isPostStatistical());
		//
	//	setC_BankAccount_ID(imp.getC_BankAccount_ID());
	//	setIsForeignCurrency(imp.isForeignCurrency());
	//	setC_Currency_ID(imp.getC_Currency_ID());
	//	setIsBankAccount(imp.isIsBankAccount());
	//	setValidFrom(null);
	//	setValidTo(null);
	}	//	set
	
	
	
	/**
	 * Is this a Balance Sheet Account
	 * @return boolean
	 */
	public boolean isBalanceSheet()
	{
		String accountType = getAccountType();
		return (ACCOUNTTYPE_Asset.equals(accountType)
			|| ACCOUNTTYPE_Liability.equals(accountType)
			|| ACCOUNTTYPE_OwnerSEquity.equals(accountType));
	}	//	isBalanceSheet

	/**
	 * Is this an Activa Account
	 * @return boolean
	 */
	public boolean isActiva()
	{
		return ACCOUNTTYPE_Asset.equals(getAccountType());
	}	//	isActive

	/**
	 * Is this a Passiva Account
	 * @return boolean
	 */
	public boolean isPassiva()
	{
		String accountType = getAccountType();
		return (ACCOUNTTYPE_Liability.equals(accountType)
			|| ACCOUNTTYPE_OwnerSEquity.equals(accountType));
	}	//	isPassiva

	/**
	 * 	User String Representation
	 *	@return info value - name
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ();
		sb.append(getValue()).append(" - ").append(getName());
		return sb.toString ();
	}	//	toString

	/**
	 * 	Extended String Representation
	 *	@return info
	 */
	@Override
	public String toStringX ()
	{
		StringBuffer sb = new StringBuffer ("MElementValue[");
		sb.append(get_ID()).append(",").append(getValue()).append(" - ").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toStringX
	
	
	
	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if ir can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		//
		if (!newRecord && isSummary() 
			&& is_ValueChanged("IsSummary"))
		{
			String sql = "SELECT COUNT(*) FROM Fact_Acct WHERE Account_ID=?";
			int no = QueryUtil.getSQLValue(get_Trx(), sql, getC_ElementValue_ID());
			if (no != 0)
			{
				log.saveError("Error", "Already posted to");
				return false;
			}
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Value/Name change
		if (!newRecord && (is_ValueChanged("Value") || is_ValueChanged("Name")))
		{
			MAccount.updateValueDescription(getCtx(), "Account_ID= ? " ,
					get_Trx(),new Object[]{getC_ElementValue_ID()});
			if ("Y".equals(getCtx().getContext("$Element_U1"))) 
				MAccount.updateValueDescription(getCtx(), "User1_ID= ? ",
						get_Trx(),new Object[]{getC_ElementValue_ID()});
			if ("Y".equals(getCtx().getContext("$Element_U2"))) 
				MAccount.updateValueDescription(getCtx(), "User2_ID= ? " ,
						get_Trx(),new Object[]{getC_ElementValue_ID()});
		}

		return success;
	}	//	afterSave
	
}	//	MElementValue
