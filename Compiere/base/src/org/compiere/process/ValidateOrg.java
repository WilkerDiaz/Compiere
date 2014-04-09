package org.compiere.process;

import java.util.ArrayList;
import org.compiere.model.*;

public class ValidateOrg extends SvrProcess{

	private MOrg org;
	
	@Override
	protected void prepare(){
		int orgID = getRecord_ID();
		org = MOrg.get(getCtx(), orgID);
	}
	
	@Override
	protected String doIt () throws Exception {
		
		ArrayList<MProduct> product = new ArrayList<MProduct>();
		product = MProduct.findAll(getCtx(), null, null);
		for (int i=0;i<product.size();i++)
		{
			MCost.createForOrg(product.get(i), org);
		}
		org.setIsValid("Y");
		org.save();
		return "Generated missing cost records for organization " + org.getName();
	}
}
