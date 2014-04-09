package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.model.MWarehouse;
import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 *	
 *  @author Gabrielle Huchet
 */
public class MVMROrder extends X_XX_VMR_Order {

	private static final long serialVersionUID = 1L;
		
	public MVMROrder(Ctx ctx, int XX_VMR_Order_ID, Trx trx) {
			super(ctx, XX_VMR_Order_ID, trx);
			// TODO Auto-generated constructor stub
	}

	public MVMROrder(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
			// TODO Auto-generated constructor stub
	}

	protected boolean afterSave (boolean newRecord, boolean success){
		
		if(newRecord){
			// Se setea el campo de organización con la del almacen de la distribución asociada
			MVMRDistributionHeader dist = new MVMRDistributionHeader(Env.getCtx(), getXX_VMR_DistributionHeader_ID(), get_Trx());
			MWarehouse warehouse = new MWarehouse(Env.getCtx(), dist.getM_Warehouse_ID(), get_Trx());
			setAD_Org_ID(warehouse.getAD_Org_ID());
			save();
		}
		return true;
			
	}//	afterSave
}
