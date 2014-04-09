/**
 * 
 */
package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.model.MWarehouse;
import org.compiere.model.X_I_Locator;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

/**
 * @author soporte
 *
 */
public class MLocator extends org.compiere.model.MLocator {

	/**
	 * @param ctx
	 * @param M_Locator_ID
	 * @param trxName
	 */
	public MLocator(Ctx ctx, int M_Locator_ID, Trx trx) {
		super(ctx, M_Locator_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param warehouse
	 * @param Value
	 */
	public MLocator(MWarehouse warehouse, String Value) {
		super(warehouse, Value);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MLocator(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param loc
	 */
	public MLocator(X_I_Locator loc) {
		super(loc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param M_Locator_ID
	 * @param loc
	 */
	public MLocator(int M_Locator_ID, X_I_Locator loc) {
		super(M_Locator_ID, loc);
		// TODO Auto-generated constructor stub
	}
	public boolean beforeSave(boolean newRecord){
		if(super.beforeSave(newRecord)){
			
		setValue(getWarehouseName()+" - "+getValue());
		}
		
		return true;
	}

}
