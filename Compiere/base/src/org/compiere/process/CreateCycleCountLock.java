package org.compiere.process;

import org.compiere.model.MCycleCountLock;
import org.compiere.model.MInventory;

public class CreateCycleCountLock extends SvrProcess{
	
	private int M_Inventory_ID = 0;
	@Override
	protected void prepare ()
	{
	 M_Inventory_ID = getRecord_ID();	
	}
	
	@Override
	protected String doIt() throws Exception
	{
		MInventory inv = new MInventory(getCtx(),M_Inventory_ID,get_Trx());
		boolean lock = inv.getIsLocked().equals("Y");
		
		if(lock)
		{
			if(MCycleCountLock.unLock(inv,null))
			{
				inv.save(get_Trx());
				return "Success";
			}
			else
				return "Can not unlock the document";
		}
		else
		{
			if(MCycleCountLock.lock(inv))
			{
				inv.save(get_Trx());
				return "Success";
			}
			else
				return "Can not lock the document";
		}
	}

}
