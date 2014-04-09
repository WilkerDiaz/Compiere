package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Calendar;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.X_XX_VMR_InventoryFault;

/**
 * Cálculo de fallas de inventario por productos para una referencia básica
 * @author Trinimar Acevedo.
 *
 */
public class XX_CalculateInventoryFault extends SvrProcess {

	private Calendar cal;
	private int year = 0;
	private int month = 0;
	private int lastDay = 0;
	private int warehouses = 0;
	private int m = 0;
	@Override
	
	protected String doIt() throws Exception {
		String sql1 = "";
		PreparedStatement prst = null;
		ResultSet rs = null;
		warehouses = countWarehouse();
		cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		
		if(month == 0){
			month = 11;
			year = year - 1;
		}else{
			month = month - 1;
		}
		m = month + 1;
		
		cal.set(year, month, 1);
		lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		/*Crear consulta para obtener el codigo de los productos con referencias básicas
		 * agrupados por referencia y tienda*/
		
		sql1 = "WITH p AS (SELECT v.XX_VMR_VendorProdRef_ID, count(DISTINCT f.M_product_ID) as numprod " +
				"FROM XX_VMR_FaultCounter f " +
				"JOIN M_Product mp ON (mp.M_Product_ID = f.M_product_ID) " +
				"JOIN XX_VMR_VendorProdRef v ON (v.XX_VMR_VendorProdRef_ID = mp.XX_VMR_VendorProdRef_ID) " +
				"WHERE v.C_BPARTNER_ID = mp.C_BPARTNER_ID " +
				"AND v.XX_VMR_Department_ID = mp.XX_VMR_Department_ID " +
				"AND v.XX_VMR_Line_ID = mp.XX_VMR_Line_ID " +
				"AND mp.XX_VMR_Section_ID = v.XX_VMR_Section_ID " +
				"GROUP BY v.XX_VMR_VendorProdRef_ID) " +
				"SELECT v.XX_VMR_VendorProdRef_ID, f.M_warehouse_ID, v.XX_VMR_TypeBasic_ID, sum(f.xx_counter), p.numprod " +
				"FROM XX_VMR_FaultCounter f " +
				"JOIN M_Product mp ON (mp.M_Product_ID = f.M_product_ID) " +
				"JOIN XX_VMR_VendorProdRef v ON (v.XX_VMR_VendorProdRef_ID = mp.XX_VMR_VendorProdRef_ID) " +
				"JOIN p ON (p.XX_VMR_VendorProdRef_ID = v.XX_VMR_VendorProdRef_ID) " +
				"WHERE v.C_BPARTNER_ID = mp.C_BPARTNER_ID " +
				"AND v.XX_VMR_Department_ID = mp.XX_VMR_Department_ID " +
				"AND v.XX_VMR_Line_ID = mp.XX_VMR_Line_ID " +
				"AND mp.XX_VMR_Section_ID = v.XX_VMR_Section_ID " +
				//"AND v.XX_VMR_TypeBasic_ID IS NOT NULL "+
				"GROUP BY f.M_warehouse_ID, v.XX_VMR_VendorProdRef_ID, v.XX_VMR_TypeBasic_ID, p.numprod ";
		try{
			prst = DB.prepareStatement(sql1,null);
			rs = prst.executeQuery();
            BigDecimal p = null;
			/*Por cada referencia crear una falla de tipo 1*/
			while(rs.next()){
                p = BigDecimal.valueOf(((float)rs.getInt(4)/(float)lastDay)/(float)rs.getInt(5));
                //System.out.println(p.setScale(5, RoundingMode.HALF_UP));
				X_XX_VMR_InventoryFault fault = new X_XX_VMR_InventoryFault(Env.getCtx(), 0, null);
				fault.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				fault.setAD_Client_ID(getAD_Client_ID());
				fault.setXX_VMR_VendorProdRef_ID(rs.getInt(1));
				fault.setM_Warehouse_ID(rs.getInt(2));
				fault.setXX_VMR_TypeBasic_ID(rs.getInt(3));
				fault.setClosedMonth(m);
				fault.setInventoryYear(year);
				fault.setFaultPercentage(p);
				fault.setTypeInventoryFault("1");
				fault.save();
			}
		}catch(SQLException e){
			System.out.println(e);
		}finally{
			prst.close();
			rs.close();
		}
		calculateFault5();
		calculateFault2();
		calculateFault4();
		calculateFault3();
		calculateFault6();
		
		return null;
	}
	
	/*Crear consulta para obtener el codigo de los productos con referencias básicas
	 * agrupados por referencia/suma de todas las tiendas*/
	private void calculateFault2(){
		String sql2 = "";
		PreparedStatement prst = null;
		ResultSet rs = null;
		
		sql2 = "SELECT i.XX_VMR_VendorProdRef_ID, i.XX_VMR_TypeBasic_ID, sum(i.FaultPercentage) suma " +
		"FROM XX_VMR_InventoryFault i  WHERE i.typeinventoryfault = '1' " +
		"AND ClosedMonth = "+m+" "+
		//"AND i.XX_VMR_TypeBasic_ID IS NOT NULL "+
		"GROUP BY i.XX_VMR_VendorProdRef_ID, i.XX_VMR_TypeBasic_ID ";
		try{
			prst = DB.prepareStatement(sql2,null);
			rs = prst.executeQuery();
            BigDecimal p = null;
			/*Por cada producto crear una falla de tipo 2*/
			while(rs.next()){
                p = BigDecimal.valueOf((rs.getFloat(3)/(float)warehouses));
				X_XX_VMR_InventoryFault fault = new X_XX_VMR_InventoryFault(Env.getCtx(), 0, null);
				fault.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				fault.setAD_Client_ID(getAD_Client_ID());
				fault.setXX_VMR_VendorProdRef_ID(rs.getInt(1));
				fault.setXX_VMR_TypeBasic_ID(rs.getInt(2));
				fault.setClosedMonth(m);
				fault.setInventoryYear(year);
				fault.setFaultPercentage(p);
				fault.setTypeInventoryFault("2");
				fault.save();
			}
		}catch(SQLException e){
			System.out.println(e);
		}finally{
			try {
				prst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*Crear consulta para obtener los tipos de referencias básicas
	 * consolidado compañía*/
	private void calculateFault3(){
		String sql3 = "";
		PreparedStatement prst = null;
		ResultSet rs = null;
		sql3 = "SELECT xx_vmr_typebasic_id, sum(faultpercentage)/ count(xx_vmr_vendorprodref_id) as sumv " +
				"FROM xx_vmr_inventoryfault WHERE typeinventoryfault = '2' " +
				"AND ClosedMonth = "+m+" "+
				//"AND XX_VMR_TypeBasic_ID IS NOT NULL "+
				"GROUP BY xx_vmr_typebasic_id";
		try{
			prst = DB.prepareStatement(sql3,null);
			rs = prst.executeQuery();
            BigDecimal p = null;
			/*Por cada tipo de básico crear una falla de tipo 4*/
			
			while(rs.next()){
                p = rs.getBigDecimal(2);
				X_XX_VMR_InventoryFault fault = new X_XX_VMR_InventoryFault(Env.getCtx(), 0, null);
				fault.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				fault.setAD_Client_ID(getAD_Client_ID());
				fault.setXX_VMR_TypeBasic_ID(rs.getInt(1));
				fault.setFaultPercentage(p);
				fault.setClosedMonth(m);
				fault.setInventoryYear(year);
				fault.setTypeInventoryFault("3");
				fault.save();
				
			}
		}catch(SQLException e){
			System.out.println(e);
		}finally{
			try {
				prst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*Crear consulta para obtener los tipos de referencias básicas
	 * agrupados por tienda*/
	private void calculateFault4(){
		String sql4 = "";
		PreparedStatement prst = null;
		ResultSet rs = null;
		sql4 = "SELECT xx_vmr_typebasic_id, m_warehouse_id, SUM(faultpercentage)/COUNT(xx_vmr_vendorprodref_id) as sumv " +
				"FROM xx_vmr_inventoryfault WHERE typeinventoryfault = '1' " +
				"AND ClosedMonth = "+m+" "+
				"AND XX_VMR_TypeBasic_ID IS NOT NULL "+
				"GROUP BY xx_vmr_typebasic_id, m_warehouse_id";
		try{
			prst = DB.prepareStatement(sql4,null);
			rs = prst.executeQuery();
            BigDecimal p = null;
			/*Por cada tipo de básico crear una falla de tipo 3*/
			while(rs.next()){
                p = rs.getBigDecimal(3);
				X_XX_VMR_InventoryFault fault = new X_XX_VMR_InventoryFault(Env.getCtx(), 0, null);
				fault.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				fault.setAD_Client_ID(getAD_Client_ID());
				fault.setXX_VMR_TypeBasic_ID(rs.getInt(1));
				fault.setM_Warehouse_ID(rs.getInt(2));
				fault.setClosedMonth(m);
				fault.setInventoryYear(year);
				fault.setFaultPercentage(p);
				fault.setTypeInventoryFault("4");
				fault.save();
			}
		}catch(SQLException e){
			System.out.println(e);
		}finally{
			try {
				prst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*Crear consulta para obtener los tipos de referencias básicas
	 * agrupados por tienda*/
	private void calculateFault5(){
		String sql5 = "";
		PreparedStatement prst = null;
		ResultSet rs = null;

		sql5 = "SELECT f.M_Product_ID, f.M_warehouse_ID, mp.XX_VMR_VendorProdRef_ID, f.xx_counter  " +
		"FROM XX_VMR_FaultCounter f " +
		"JOIN M_Product mp ON (mp.M_Product_ID = f.M_product_ID) ";
		
		try{
			prst = DB.prepareStatement(sql5,null);
			rs = prst.executeQuery();
            BigDecimal p = null;
			/*Por cada tipo de básico crear una falla de tipo 3*/
			while(rs.next()){
                p = BigDecimal.valueOf((float)rs.getInt(4)/(float)lastDay);
				X_XX_VMR_InventoryFault fault = new X_XX_VMR_InventoryFault(Env.getCtx(), 0, null);
				fault.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				fault.setAD_Client_ID(getAD_Client_ID());
				fault.setM_Product_ID(rs.getInt(1));
				fault.setM_Warehouse_ID(rs.getInt(2));
				//fault.setXX_VMR_VendorProdRef_ID(rs.getInt(3));
				fault.setClosedMonth(m);
				fault.setInventoryYear(year);
				fault.setFaultPercentage(p);
				fault.setTypeInventoryFault("5");
				fault.save();
			}
		}catch(SQLException e){
			System.out.println(e);
		}finally{
			try {
				prst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*Crear consulta para obtener los tipos de referencias básicas
	 * agrupados por tienda*/
	private void calculateFault6(){
		String sql6 = "";
		PreparedStatement prst = null;
		ResultSet rs = null;
		
		sql6 = "SELECT i.M_Product_ID, sum(i.FaultPercentage) suma " +
		"FROM XX_VMR_InventoryFault i  WHERE i.typeinventoryfault = '5' " +
		"AND ClosedMonth = "+m+" "+
		"GROUP BY i.M_Product_ID ";
		try{
			prst = DB.prepareStatement(sql6,null);
			rs = prst.executeQuery();
            BigDecimal p = null;
			/*Por cada tipo de básico crear una falla de tipo 3*/
			while(rs.next()){
                p = BigDecimal.valueOf((rs.getFloat(2)/(float)warehouses));
				X_XX_VMR_InventoryFault fault = new X_XX_VMR_InventoryFault(Env.getCtx(), 0, null);
				fault.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				fault.setAD_Client_ID(getAD_Client_ID());
				fault.setM_Product_ID(rs.getInt(1));
				//fault.setXX_VMR_VendorProdRef_ID(rs.getInt(2));
				fault.setClosedMonth(m);
				fault.setInventoryYear(year);
				fault.setFaultPercentage(p);
				fault.setTypeInventoryFault("6");
				fault.save();
			}
		}catch(SQLException e){
			System.out.println(e);
		}finally{
			try {
				prst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected int countWarehouse()
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int warehouses = 0;
		String SQL = "SELECT count(Distinct M_Warehouse_ID) FROM XX_VMR_FaultCounter";
		
		try {
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				warehouses = rs.getInt(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return warehouses;
	}
	
//	protected void deleteFault()
//	{
//		PreparedStatement pstmt = null;
//		String SQL = "DELETE FROM XX_VMR_InventoryFault";
//		
//		try {
//			pstmt = DB.prepareStatement(SQL, null);
//			pstmt.executeQuery();
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				pstmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	@Override
	protected void prepare() {

		
	}

}
