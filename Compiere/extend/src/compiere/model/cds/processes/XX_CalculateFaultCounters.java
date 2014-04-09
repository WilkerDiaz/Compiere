package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.X_XX_VMR_FaultCounter;

/**
 * Cálculo de contador de fallas por productos de una referencia básica
 * @author Trinimar Acevedo.
 *
 */
public class XX_CalculateFaultCounters extends SvrProcess{
	
	protected String doIt() throws Exception {
		String sql = "";
		PreparedStatement prst = null;
		ResultSet rs = null;
		
		sql = "WITH inv AS (SELECT s.M_Product_ID , l.m_warehouse_id, SUM(s.qty) qty FROM M_StorageDetail s  " +
				"JOIN xx_vmr_faultcounter f ON (s.M_Product_ID = f.M_Product_ID) " +
				"JOIN m_locator l ON (l.m_locator_id = s.m_locator_id) WHERE s.qtytype = 'H' " +
				"AND (l.value LIKE '%EN TIENDA%' OR l.value = 'CENTRO DE DISTRIBUCION BOLEITA - CHEQUEADO') " +
				//"AND l.value = 'CENTRO DE DISTRIBUCION - CHEQUEADO' " +
				"GROUP BY s.M_Product_ID , l.m_warehouse_id) " +
				"SELECT DISTINCT f.XX_VMR_FaultCounter_ID, f.M_Product_ID, inv.M_Warehouse_ID, r.level_min, inv.qty " +
				"FROM XX_VMR_FaultCounter f  JOIN inv ON (inv.m_product_id = f.m_product_id) " +
				"LEFT JOIN M_Replenish r ON (f.M_Product_ID = r.M_Product_ID) " +
				"WHERE inv.m_warehouse_id = f.m_warehouse_id AND inv.qty<=0 ";
		//System.out.println("Corriendo solo CD");
		try{
			prst = DB.prepareStatement(sql,null);
			rs = prst.executeQuery();
			while(rs.next()){
				//if(rs.getInt(4) <= rs.getInt(3)){
					String sum = "UPDATE XX_VMR_FaultCounter SET XX_Counter = XX_Counter + 1 WHERE " +
							"XX_VMR_FaultCounter_ID ="+rs.getInt(1);
								
					DB.executeUpdate(null, sum );
				//}
			}
		}catch(SQLException e){
			System.out.println(e);
		}finally{
			prst.close();
			rs.close();
		}
		return "";	
	}
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
}
