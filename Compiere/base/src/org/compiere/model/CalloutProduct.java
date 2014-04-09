package org.compiere.model;

import org.compiere.util.*;

/**
 *	Product Callouts
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutProject.java,v 1.3 2006/07/30 00:51:04 jjanke Exp $
 */
public class CalloutProduct extends CalloutEngine {

	/**
	 *	Product Category
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String productCategory (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_Product_Category_ID = (Integer)value;
		if (M_Product_Category_ID == null || M_Product_Category_ID.intValue() == 0
			|| M_Product_Category_ID == 0)
			return "";

		MProductCategory pc = new MProductCategory (ctx, M_Product_Category_ID, null);
		mTab.setValue("IsPurchasedToOrder",pc.isPurchasedToOrder());

		return "";
	}	//	product

	
	/**
	 *	Resource Group
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String resourceGroup (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String resgrp = (String)value;
		if (resgrp == null || resgrp.length() == 0)
			return "";	

		if ("O".equals(resgrp))
			mTab.setValue("BasisType", null);
		else
			mTab.setValue("BasisType", "I");
		return "";
	}	//	product

	
	/**
	 *  Organization
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */


	public String Organization (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer AD_Org_ID =(Integer)value;
        if (AD_Org_ID == null)
              return "";
		MLocator defaultLocator = MLocator.getDefaultLocatorOfOrg(ctx, AD_Org_ID);
		if (defaultLocator!=null)
			mTab.setValue("M_Locator_ID", defaultLocator.get_ID());
		return "";

	}  // default locator

}
