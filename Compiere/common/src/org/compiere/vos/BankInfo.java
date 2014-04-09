package org.compiere.vos;


import org.compiere.util.*;

public class BankInfo extends NamePair
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankInfo(){}
	/**
	 * 	BankInfo
	 *	@param newC_BankAccount_ID
	 *	@param newC_Currency_ID
	 *	@param newName
	 *	@param newCurrency
	 *	@param newBalance
	 *	@param newTransfers
	 */
	public BankInfo (int newC_BankAccount_ID, int newC_Currency_ID,
		String newName, String newCurrency, String newBalance, boolean newTransfers)
	{
		super(newName);
		C_BankAccount_ID = newC_BankAccount_ID;
		C_Currency_ID = newC_Currency_ID;
		Name = newName;
		Currency = newCurrency;
		Balance = newBalance;
	}
	public int C_BankAccount_ID;
	public int C_Currency_ID;
	public String Name;
	public String Currency;
	public String Balance;
	public boolean Transfers;

	/**
	 * 	to String
	 *	@return infoint
	 */
	@Override
	public String toString()
	{
		return Name;
	}

	
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return Integer.toString(C_BankAccount_ID);
	}
}   //  BankInfo

