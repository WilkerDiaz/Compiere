package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MOrder;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_XX_VMR_DistributionHeader;

/** Clase instancia de proceso que anula las redistribuciones de inventario
 * */
public class XX_VoidDistribution extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		X_XX_VMR_DistributionHeader cabecera =
			new X_XX_VMR_DistributionHeader(getCtx(), getRecord_ID(), get_TrxName());		
		
		//Verificar que no sea de Despacho Directo
		if (cabecera.getC_Order_ID()!=0){
			MOrder order = new MOrder( Env.getCtx(), cabecera.getC_Order_ID(), null);
				
			if(order.getXX_VLO_TypeDelivery().equals("DD")){
				ADialog.error(1, new Container(), "XX_CantAnullDistributionDD");
				return Msg.getMsg(Env.getCtx(), "XX_CantAnullDistributionDD");
			}else if(order.getXX_VLO_TypeDelivery().equals("PD")){
				ADialog.error(1, new Container(), "No se puede anular la distribución (Predistribuida)");
				return Msg.getMsg(Env.getCtx(), "No se puede anular la distribución (Predistribuida)");
			}
		}
		
		if(cabecera.getXX_DistributionStatus().equals("AP")){
			
			if(canAnull()){
			
				//Anular los pedidos
				String update = "UPDATE XX_VMR_ORDER SET XX_ORDERREQUESTSTATUS = 'AN' " +
							 	"WHERE XX_VMR_DistributionHeader_ID = " + getRecord_ID();
			
				try {
					DB.executeUpdate(get_TrxName(),update);
	
				} catch (Exception e1) {
					e1.printStackTrace();
					ADialog.error(1, new Container(), "XX_DatabaseAccessError");			
				}
				
			}
			else{
				ADialog.error(1, new Container(), "XX_CantAnullDistributionLate");
				return Msg.getMsg(Env.getCtx(), "XX_CantAnullDistributionLate");
			}
		}
		
		if (cabecera.getC_Order_ID() == 0) {
			
			//Es una redistribución de inventario Se debe liberar el espacio
			String select = " DELETE FROM XX_VMR_DISTRIBDETAILTEMP " +
			" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID IN ( " +
				" SELECT XX_VMR_DISTRIBUTIONDETAIL_ID FROM XX_VMR_DISTRIBUTIONDETAIL " +
					" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + getRecord_ID() + " )";
		
			try {
				DB.executeUpdate(get_TrxName(),select);

			} catch (Exception e1) {
				e1.printStackTrace();
				ADialog.error(1, new Container(), "XX_DatabaseAccessError");			
			} 
				
			}
		
		//Colocar la orden como anulada
		cabecera.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.ANULADA.getValue());
		cabecera.save();			
		commit();
		
		//Mostrar mensaje de exito
		String mss = Msg.getMsg(Env.getCtx(), "XX_DistributionVoided", 
				//Order					
				new String[] {"" + cabecera.getXX_VMR_DistributionHeader_ID()});
		ADialog.info(1, new Container(), mss);
		return mss;	
	}

	@Override
	protected void prepare() {
				
	}
	
	private boolean canAnull(){
		
		String SQL = "SELECT XX_VMR_ORDER_ID, XX_ORDERREQUESTSTATUS FROM XX_VMR_ORDER " +
		 "WHERE XX_VMR_DistributionHeader_ID = " + getRecord_ID();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Vector<Integer> placedOrders = new Vector<Integer>();
		Vector<String> status = new Vector<String>();
		
		try
		{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			int i=0;
			while (rs.next())
			{
				placedOrders.add(rs.getInt("XX_VMR_ORDER_ID"));
				status.add(rs.getString("XX_ORDERREQUESTSTATUS"));
				i++;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
		
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		for(int i=0; i<status.size(); i++){
		
			if(!status.get(i).equals("PE"))
				return false;
		}
	
		return true;
	}

}
