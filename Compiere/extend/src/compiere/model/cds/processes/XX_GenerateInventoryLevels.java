package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import compiere.model.cds.X_XX_VMR_FaultCounter;
import compiere.model.cds.X_XX_VMR_InventoryLevels;

/**
 * Proceso que genera los niveles de inventario para cada tipo de basico 
 * @author Trinimar Acevedo.
 *
 */
public class XX_GenerateInventoryLevels extends SvrProcess{
	
	protected String doIt() throws Exception {
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Calendar cal = Calendar.getInstance();
		int mes = cal.get(Calendar.MONTH);
		int anio = cal.get(Calendar.YEAR);
		if(mes == 0){
			mes = 12;
			anio = anio - 1;
		}
		deleteInv();

		sql = 
			"WITH inventario AS (select v.xx_vmr_vendorProdRef_id, SUM(CASE WHEN ml.isDefault='Y' AND ml.value <> 'CENTRO DE DISTRIBUCION BOLEITA - CHEQUEADO' THEN s.qty ELSE 0 END) qtyTienda, "+
			"SUM(CASE WHEN ml.value = 'CENTRO DE DISTRIBUCION BOLEITA - CHEQUEADO' THEN s.qty ELSE 0 END) qtyCD "+
			"FROM M_storageDetail s JOIN m_product p ON (p.m_product_id = s.m_product_id) "+ 
			"JOIN m_locator ml ON (s.m_locator_id = ml.m_locator_id) JOIN XX_VMR_VENDORPRODREF v "+ 
			"ON (p.XX_VMR_VENDORPRODREF_ID = v.XX_VMR_VENDORPRODREF_ID) WHERE s.qtytype = 'H' "+
			"AND P.C_BPARTNER_ID = V.C_BPARTNER_ID AND P.XX_VMR_SECTION_ID = V.XX_VMR_SECTION_ID "+
			"GROUP BY v.xx_vmr_vendorProdRef_id),   "+
			"Aprobadas AS (SELECT V.XX_VMR_VENDORPRODREF_ID, SUM(CASE WHEN OL.QTYORDERED IS NOT NULL "+
			"AND O.XX_ORDERSTATUS NOT IN ('AN', 'PRO', 'RE', 'CH', 'SIT') THEN OL.QTYORDERED ELSE 0 END) qtyAP  "+
			"FROM C_ORDERLINE OL JOIN C_ORDER O ON (O.C_ORDER_ID = OL.C_ORDER_ID) "+
			"JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = OL.M_PRODUCT_ID) "+
			"JOIN XX_VMR_VENDORPRODREF V on (P.XX_VMR_VENDORPRODREF_ID = V.XX_VMR_VENDORPRODREF_ID) "+
			"WHERE ISSOTRX = 'Y' AND O.DATEORDERED = sysdate "+
			"AND P.C_BPARTNER_ID = V.C_BPARTNER_ID "+ 
			"AND P.XX_VMR_SECTION_ID = V.XX_VMR_SECTION_ID  "+
			"GROUP BY V.XX_VMR_VENDORPRODREF_ID), " +
			"Ventas AS (SELECT V.XX_VMR_VENDORPRODREF_ID, " +
			"SUM(CASE WHEN OL.QTYENTERED IS NOT NULL THEN OL.QTYENTERED ELSE 0 END) QTYV " +
			"FROM C_ORDERLINE OL JOIN C_ORDER O ON (O.C_ORDER_ID = OL.C_ORDER_ID) " +
			"JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = OL.M_PRODUCT_ID) JOIN XX_VMR_VENDORPRODREF V " +
			"ON (P.XX_VMR_VENDORPRODREF_ID = V.XX_VMR_VENDORPRODREF_ID) WHERE ISSOTRX = 'Y' AND OL.QTYENTERED <> 0 " +
			"AND O.DATEORDERED <= sysdate AND TO_CHAR(O.DATEORDERED, 'mm') = "+mes+"  "+
			"AND TO_CHAR(O.DATEORDERED, 'yyyy') = "+anio+" "+
			"AND P.C_BPARTNER_ID = V.C_BPARTNER_ID "+ 
			"AND P.XX_VMR_SECTION_ID = V.XX_VMR_SECTION_ID  "+
			"GROUP BY V.XX_VMR_VENDORPRODREF_ID) "+
			"SELECT v.xx_vmr_vendorProdRef_id, inventario.qtyTienda, inventario.qtyCD, Aprobadas.qtyAP, " +
			"Ventas.qtyV, (T001+T002+T003+T007+T009+T010+T015+T016+T017) "+ 
			"FROM XX_VMR_VENDORPRODREF v "+
			"LEFT JOIN inventario ON (inventario.xx_vmr_vendorProdRef_id = v.xx_vmr_vendorProdRef_id) "+
			"LEFT JOIN Aprobadas ON (v.xx_vmr_vendorProdRef_id = Aprobadas.xx_vmr_vendorProdRef_id)  "+
			"LEFT JOIN Ventas ON (v.xx_vmr_vendorProdRef_id = Ventas.xx_vmr_vendorProdRef_id)  "+
			"LEFT JOIN xx_vmr_securityinventory i ON (v.xx_vmr_vendorProdRef_id = i.xx_vmr_vendorProdRef_id) "+ 
			"JOIN XX_VMR_TypeBasic tp ON (v.XX_VMR_TypeBasic_ID = tp.XX_VMR_TypeBasic_ID) "+
			"WHERE v.XX_VMR_TypeBasic_ID IS NOT NULL AND tp.value <> 'Z' ";
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			X_XX_VMR_InventoryLevels inv = null;
			while (rs.next()) {
				inv = new X_XX_VMR_InventoryLevels(Env.getCtx(), 0, null);
				inv.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				inv.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
				inv.setXX_VMR_VendorProdRef_ID(rs.getInt(1));
				inv.setinventoryWarehouse(rs.getInt(2));
				inv.setInventoryCD(rs.getInt(3));
				inv.setapprovedOC(rs.getInt(4));
				inv.setSales(rs.getInt(5));
				inv.setsecurityInventory(rs.getInt(6));
				inv.save();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return "";	
	}
	
	protected void deleteInv()
	{
		PreparedStatement pstmt = null;
		String SQL = "DELETE FROM XX_VMR_InventoryLevels";
		
		try {
			pstmt = DB.prepareStatement(SQL, null);
			pstmt.executeQuery();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
}
