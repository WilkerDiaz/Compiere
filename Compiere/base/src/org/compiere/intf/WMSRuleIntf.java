package org.compiere.intf;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * This is the interface to implement when you want to create a custom rule for
 * WMS.
 * <p>
 * 
 * Once the class is created, the fully qualified Java class name needs to be
 * specified in M_MMRule.RuleClass for the appropriate record.
 * <p>
 * 
 * @author nnayak
 * 
 */
public interface WMSRuleIntf 
{
	/**
	 * Implement this method to return a list of valid locators corresponding to
	 * the custom rule.
	 * 
	 * @param Ctx Context
	 * @param M_Warehouse_ID Warehouse
	 * @param M_Zone_ID Zone
	 * @param M_Product_ID Product
	 * @param trx Transaction of the process
	 * @return MLocator[] List of valid locators
	 */
	public abstract MLocator[] getValidLocators(Ctx ctx, int M_Warehouse_ID, int M_Zone_ID, int M_Product_ID, 
													int C_OrderLine_ID, Trx trx);
}
