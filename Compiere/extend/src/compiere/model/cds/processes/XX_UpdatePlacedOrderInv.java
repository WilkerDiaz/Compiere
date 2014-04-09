package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MLocator;
import compiere.model.cds.Utilities;
import compiere.model.cds.MVMROrder;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;


/** Proceso que permite actualizar el inventario de un pedido, en caso de error en el proceso regular.
 * @author ghuchet*/
public class XX_UpdatePlacedOrderInv extends  SvrProcess  {

	private int distributionHeader_ID = 0;
	private int warehouse_ID = 0;
	private int locatorFrom_ID = 0;
	private int locatorTo_ID = 0;
	private int from = 0;

	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("XX_VMR_DistributionHeader_ID")) {
				distributionHeader_ID = element.getParameterAsInt();
			}else if (name.equals("M_Warehouse_ID")) {
				warehouse_ID = element.getParameterAsInt();
			} else if (name.equals("M_LocatorFrom_ID")) {
				locatorFrom_ID = element.getParameterAsInt();
			}else if (name.equals("M_LocatorTo_ID")) {
				locatorTo_ID = element.getParameterAsInt();
			}else if (name.equals("From")) {
				from =  element.getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}

	}

	@Override
	protected String doIt() throws Exception {
		
		Integer XX_VMR_Order_ID =  null;

		//Se busca el pedido de la distribución que corresponda con la tienda
		String sql = " SELECT XX_VMR_Order_ID FROM XX_VMR_Order WHERE XX_VMR_DistributionHeader_ID = " + distributionHeader_ID + 
				" AND M_Warehouse_ID =" + warehouse_ID;
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = DB.prepareStatement(sql, null);
		    rs = ps.executeQuery();

			while (rs.next()){
				XX_VMR_Order_ID = rs.getInt(1);
			}			
		}catch (Exception e) {
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}	
		if(XX_VMR_Order_ID == null){
			return "Pedido No Existe!";
		}
		MVMROrder placedOrder = new MVMROrder(getCtx(), XX_VMR_Order_ID, null);
		
		MLocator locatorFrom =  new MLocator(getCtx(), locatorFrom_ID, null);
		MLocator locatorTo =  new MLocator(getCtx(), locatorTo_ID, null);
		
		if((placedOrder.getXX_OrderRequestStatus().compareTo(X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue())==0 && from == 0) || 
				(placedOrder.getXX_OrderRequestStatus().compareTo(X_Ref_XX_VMR_OrderStatus.EN_TRÁNSITO.getValue())==0 && from == 1)){
			
			new Utilities().ActuaInvPedido(placedOrder, from, locatorFrom_ID, locatorTo_ID, get_Trx());
			
			if(locatorFrom.getM_Warehouse_ID() == Utilities.obtenerDistribucionCD(distributionHeader_ID)
					&& locatorTo.get_ID() == Utilities.obtenerLocatorEnTransito(warehouse_ID).get_ID()){
				placedOrder.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_TRÁNSITO.getValue());
				placedOrder.save();
			}else if (locatorFrom.get_ID() == Utilities.obtenerLocatorEnTransito(warehouse_ID).get_ID()
					&& locatorTo.get_ID() == Utilities.obtenerLocatorEnTienda(warehouse_ID).get_ID()){
				placedOrder.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_TIENDA.getValue());
				placedOrder.save();
			}
		
		}else {
			return "No se puede modificar el Inventario con las condiciones suministradas";
		}
		
		

		return " ";
	}


}
