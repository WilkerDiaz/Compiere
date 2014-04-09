package compiere.model;

import org.compiere.util.Ctx;

import compiere.model.cds.X_XX_CreditNotifyReturn;

public class MCreditNotifyReturn extends X_XX_CreditNotifyReturn {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param ADUser_ID
	 * @throws Exception
	 */

	/**
	 * 
	 */
	public MCreditNotifyReturn(Ctx ctx, int XX_CREDITNOTIFYRETURN_ID,
			String trxName) {
		super(ctx, XX_CREDITNOTIFYRETURN_ID, null);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		
		boolean save = super.afterSave(newRecord, success);
			if(save){
				
			}
		return save;
	}
}
