package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.X_XX_VMR_FaultCounter;
/**
 * @author Trinimar Acevedo.
 *
 */
public class XX_FaultCounters extends SvrProcess{
	protected String doIt() throws Exception {
		String sql = "";
		PreparedStatement prst = null;
		ResultSet rs = null;

		System.out.println("Corriendo New CD\n");
		sql = "select p.m_product_id, w.M_Warehouse_id from m_product p  " +
				"join xx_vmr_vendorprodref v " +
				"on (v.xx_vmr_vendorprodref_id = p.xx_vmr_vendorprodref_id) " +
				"join XX_VMR_TypeBasic t on (t.XX_VMR_TypeBasic_id = v.XX_VMR_TypeBasic_id), M_Warehouse w " +
				"where v.C_BPARTNER_ID = p.C_BPARTNER_ID " +
				"and v.xx_vmr_department_id = p.xx_vmr_department_id " +
				"and v.xx_vmr_line_id = p.xx_vmr_line_id " +
				"and v.xx_vmr_section_id = p.xx_vmr_section_id " +
				"and v.xx_vmr_typebasic_id is not null " +
				"and t.value <> 'Z'";
		
		try{
			prst = DB.prepareStatement(sql,null);
			rs = prst.executeQuery();
			while(rs.next()){
				X_XX_VMR_FaultCounter counter = new X_XX_VMR_FaultCounter(Env.getCtx(), 0, null);
				counter.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				counter.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
				counter.setM_Product_ID(rs.getInt(1));
				counter.setM_Warehouse_ID(rs.getInt(2));
				counter.save();
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
