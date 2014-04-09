package compiere.model.cds.processes;

import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.cds.MOrder;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_DistributionHeader;


/**
* @author Javier A. Pino B. (Computacion6)
*/
public class XX_CreateDistributionHeader {

	/**
	 * This method creates a distribution header for a pre distributed order 
	 */
	public static int create(int c_order_id, int distribution_id, Trx trans, int warehouseID) {
		
		MOrder order = new MOrder(Env.getCtx(), c_order_id, null);		
		X_XX_VMR_DistributionHeader new_header = 
			new X_XX_VMR_DistributionHeader(Env.getCtx(), 0, null);			
		new_header.setC_Order_ID(c_order_id);			
		new_header.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.PENDIENTE.getValue());
		new_header.setXX_VMR_DistributionType_ID(distribution_id);
		new_header.setM_Warehouse_ID(warehouseID);
		if (order.getXX_VMR_DEPARTMENT_ID() != 0) {
			X_XX_VMR_Department department = 
				new X_XX_VMR_Department(Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), trans);	
			int product_class_id  = department.getXX_VMR_ProductClass_ID();			
			if (product_class_id == Env.getCtx().getContextAsInt("#XX_L_PC_COMBINADO_ID") 
					||	product_class_id == Env.getCtx().getContextAsInt("#XX_L_PC_TEXTIL_ID")) 						
				new_header.setXX_HasTextilProducts(true);										
		}
		new_header.save(trans);		
		return new_header.get_ID();
		
	}
	
}
	
