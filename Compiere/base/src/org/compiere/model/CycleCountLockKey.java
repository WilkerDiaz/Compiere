package org.compiere.model;

/* Composite key to be used for caching the lock on product-locator combination */

public class CycleCountLockKey {

	private int M_Product_ID =0;
	private int M_Locator_ID = 0;
	
	public CycleCountLockKey(int M_Product_ID, int M_Locator_ID)
	{
		this.M_Product_ID = M_Product_ID;
		this.M_Locator_ID = M_Locator_ID;
	}
	
	public int getM_Product_ID()
	{
		return M_Product_ID;
	}
	public int getM_Locator_ID()
	{
		return M_Locator_ID;
	}
	public void setM_Product_ID(int M_Product_ID)
	{
		this.M_Product_ID= M_Product_ID;
	}
	public void setM_Locator_ID(int M_Locator_ID)
	{
		this.M_Locator_ID = M_Locator_ID;
	}
	
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass() != o.getClass())
			return false;
		CycleCountLockKey that = (CycleCountLockKey)o;
		if(this.M_Product_ID != that.M_Product_ID || this.M_Locator_ID !=that.M_Locator_ID)
			return false;
		return true;
	}
	
	public int hashCode()
	{
		int result = 3*this.M_Product_ID + 7*this.M_Locator_ID; // try to return a unique hashcode
		return result;
	}
}
