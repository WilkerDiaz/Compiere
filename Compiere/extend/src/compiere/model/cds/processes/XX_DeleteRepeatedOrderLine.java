package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;

import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;


public class XX_DeleteRepeatedOrderLine extends SvrProcess {
	
	@Override
	protected String doIt() throws Exception {
		
		String SQL1 = "select DISTINCT (a.documentno),a.docstatus, a.c_order_id as orden, " +
		" (select o.documentno from c_order o where o.c_order_id = a.c_order_id) ,count(*) " + 
		" from c_order a, c_orderline b where " + 
		" b.c_order_id = a.c_order_id and a.issotrx='N' and docstatus <> 'VO' and documentno not in ('80099','80812') " + 
		" group by m_product_id, a.documentno,a.docstatus,a.c_order_id " + 
		" having count(*)>1 " +
		" order by a.documentno ";
		
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs3 = null;
		int orden = 0;
		try
		{
						
			pstmt1 = DB.prepareStatement(SQL1, null);
			rs1 = pstmt1.executeQuery();
			
			while (rs1.next())
			{
				orden = rs1.getInt("orden");
	
				String SQL2 = "select created, c_orderline_id as corderline, m_product_id as producto from c_orderline where m_product_id in " +  
					" (SELECT M_PRODUCT_ID FROM C_ORDERLINE WHERE C_ORDER_ID=" + orden + " GROUP BY M_PRODUCT_ID  having count(*)>1) " +
					" order by m_product_id ";
				pstmt2 = DB.prepareStatement(SQL2, null);
				rs2 = pstmt2.executeQuery();

				int productoViejo = 0;
				boolean ingresoPrimero = false;
				while (rs2.next())
				{
					int productoNuevo = rs2.getInt("producto");
					if (productoNuevo==productoViejo && ingresoPrimero)
					{
						// aca borro
						String sql3 = "delete from M_MATCHPO where c_orderline_id= " + rs2.getInt("corderline");						
						DB.executeUpdate(null, sql3);
						sql3 = "delete from c_orderline where c_orderline_id= " + rs2.getInt("corderline");						
						DB.executeUpdate(null, sql3);
						ingresoPrimero = true;
					}
					if (productoNuevo!=productoViejo)
					{
						productoViejo = productoNuevo;
						ingresoPrimero = true;
					}
				}
				
			}

		}
		catch (Exception e){
			log.saveError("Error al borrar los c_orderlines duplicados de la orden " + orden, Msg.getMsg(getCtx(), e.getMessage()));
		} finally {
			DB.closeResultSet(rs1);
			DB.closeStatement(pstmt1);
		}
		
		return "";
	
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

}

