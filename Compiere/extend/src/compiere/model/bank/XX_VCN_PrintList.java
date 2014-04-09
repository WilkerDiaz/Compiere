package compiere.model.bank;

import java.math.BigDecimal;

public class XX_VCN_PrintList {
	
	int m_C_BankAccount_ID;
	String fBank;
	String fCurrency;
	BigDecimal fBalance;
	int i;
	
	public XX_VCN_PrintList(int m_C_BankAccount_ID, String fBank, String fCurrency, BigDecimal fBalance, int i) {
		this.m_C_BankAccount_ID = m_C_BankAccount_ID;
		this.fBank = fBank;
		this.fCurrency = fCurrency;
		this.fBalance = fBalance;
		this.i = i;
	}

	public int getM_C_BankAccount_ID() {
		return m_C_BankAccount_ID;
	}

	public void setM_C_BankAccount_ID(int m_C_BankAccount_ID) {
		this.m_C_BankAccount_ID = m_C_BankAccount_ID;
	}
	
	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}


	public String getfBank() {
		return fBank;
	}

	public void setfBank(String fBank) {
		this.fBank = fBank;
	}

	public String getfCurrency() {
		return fCurrency;
	}

	public void setfCurrency(String fCurrency) {
		this.fCurrency = fCurrency;
	}

	public BigDecimal getfBalance() {
		return fBalance;
	}

	public void setfBalance(BigDecimal fBalance) {
		this.fBalance = fBalance;
	}
	
	
	
	
	

}
