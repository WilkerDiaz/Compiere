package org.compiere.model;

import org.compiere.util.Ctx;
import org.compiere.util.QueryUtil;
import org.compiere.util.Trx;

public class CalloutAccount extends CalloutEngine 
{
	public String Acct (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_AcctSchema_ID = (Integer)value;
		if(C_AcctSchema_ID == null || C_AcctSchema_ID == 0)
			return "";
		
		mTab.setValue("Element_AY","N");
		mTab.setValue("Element_BP","N");
		mTab.setValue("Element_LF","N");
		mTab.setValue("Element_LT","N");
		mTab.setValue("Element_MC","N");
		mTab.setValue("Element_OT","N");
		mTab.setValue("Element_PJ","N");
		mTab.setValue("Element_PR","N");
		mTab.setValue("Element_SA","N");
		mTab.setValue("Element_SR","N");
		mTab.setValue("Element_U1","N");
		mTab.setValue("Element_U2","N");
		mTab.setValue("Element_X1","N");
		mTab.setValue("Element_X2","N");
		
		//	Accounting Elements
		String sql = " SELECT ElementType "
			       + " FROM C_AcctSchema_Element "
			       + " WHERE C_AcctSchema_ID=?"
			       + " AND IsActive='Y'";
		
		Object result[][]= QueryUtil.executeQuery((Trx)null, sql, C_AcctSchema_ID);
		for (Object r[] : result)
			mTab.setValue("Element_"+r[0],"Y");
		return "";

	}
}
