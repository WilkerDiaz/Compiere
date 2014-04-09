package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class XX_VMR_HistoricWeekSales  extends SvrProcess{
	@Override
	protected String doIt() throws Exception {	
		String msj = "";
		int maxWeek = 0;
		PreparedStatement psSales = null;
		ResultSet rsSales = null;
		String SQLSales = "";
		String SQLDelete = "";
		String SQLWeek = " SELECT CAST(TO_CHAR(SYSDATE-1,'IW') AS INT) " +
						" FROM DUAL";
		

		
		// Obtener la semana mas nueva en la tabla de historico
		try{
			psSales = DB.prepareStatement(SQLWeek, null);
			rsSales = psSales.executeQuery();
			while (rsSales.next()) {
				maxWeek = rsSales.getInt(1);
			}
		}//try		
		catch(Exception e){	
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rsSales);
			DB.closeStatement(psSales);
		}
		
		// En caso que la semana de creación de los registros sea mayor que la semana maxima en el historico de ventas
		// los registros de la tabla deben eliminarse
		
		SQLDelete = " DELETE FROM XX_VMR_HISTORICWEEKSALES WHERE TO_CHAR(CREATED,'IW') <  "+maxWeek;	
	
		int updated =  DB.executeUpdate(null, SQLDelete);
		System.out.println("SQL Deleted: "+SQLDelete);
		System.out.println("Deleted: "+updated);
		deleteInventoryTemp();
		createInventoryTemp();
		//Se inserta la información de las ventas en la tabla XX_VMR_HISTORICSALES
		int idMax = findMaxID("XX_VMR_HISTORICWEEKSALES");
		
		SQLSales = "\n INSERT INTO XX_VMR_HISTORICWEEKSALES (" +
				"\n XX_VMR_HISTORICWEEKSALES_ID, Value, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, " +
				"\n Updated, UpdatedBy, "+
				"\n M_WAREHOUSE_ID, XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID," +	
				"\n XX_VMR_BRAND_ID, XX_SALES, XX_VMR_COLLECTION_ID, XX_INITIALINVENTORY, M_PRODUCT_ID," +
				"\n XX_VMR_VENDORPRODREF_ID)  " +
				"\n (SELECT "+ idMax + " + MAX(rownum) , TO_CHAR("+ idMax + " + MAX(rownum) ),  P.AD_Client_ID Client_ID, " +
				"\n P.AD_Org_ID Org_ID, 'Y' ISACTIVE, TRUNC(SYSDATE-1) TODAYDATE1,  P.CreatedBy CreatedToday, TRUNC(SYSDATE-1) TODAYDATE2,  " +
				"\n P.UpdatedBy UpdatedToday, L.M_WAREHOUSE_ID WAREHOUSE_ID, P.XX_VMR_CATEGORY_ID CATEGORY_ID,  P.XX_VMR_DEPARTMENT_ID DEPARTMENT_ID, " +
				"\n P.XX_VMR_LINE_ID LINE_ID, P.XX_VMR_SECTION_ID SECTION_ID,  P.XX_VMR_BRAND_ID BRAND_ID, " +
				"\n ROUND(SUM((L.PRICEACTUAL+L.XX_EMPLOYEEDISCOUNT)*L.QTYORDERED),2) SALES,  C.XX_VMR_COLLECTION_ID COLLECTION_ID,   " +
				"\n (CASE WHEN  NVL((SELECT SUM(H.XX_INITIALINVENTORY) FROM XX_VMR_HISTORICWEEKSALES H     " +
				"\n WHERE H.M_PRODUCT_ID = L.M_PRODUCT_ID AND  H.M_WAREHOUSE_ID = L.M_WAREHOUSE_ID),0) != 0 THEN 0    " +  
				"\n ELSE ROUND((SELECT INVENTORYQTY    " +
				"\n FROM XX_INVENTORYPRODUCT_TEMP INV    " +
				"\n WHERE INV.M_PRODUCT_ID = L.M_PRODUCT_ID  AND INV.M_WAREHOUSE_ID = L.M_WAREHOUSE_ID    " +
				"\n ),2) END) INVENTORY,   " +
				"\n L.M_PRODUCT_ID PRODUCT, R.XX_VMR_VENDORPRODREF_ID PRODREF   " +
				"\n FROM C_ORDER C  " +
				"\n JOIN C_ORDERLINE L ON (C.C_ORDER_ID = L.C_ORDER_ID)  " +
				"\n JOIN M_PRODUCT P ON (L.M_PRODUCT_ID = P.M_PRODUCT_ID)  " +
				"\n JOIN XX_VMR_VENDORPRODREF R ON (P.XX_VMR_VENDORPRODREF_ID = R.XX_VMR_VENDORPRODREF_ID)  " +
				"\n WHERE C.ISSOTRX= 'Y' AND C.DOCSTATUS = 'CO'  AND TRUNC(C.DATEORDERED) =  TRUNC(SYSDATE-1)  "+
				"\n GROUP BY P.AD_Client_ID, P.AD_Org_ID,  P.CreatedBy, P.UpdatedBy, L.M_WAREHOUSE_ID,  " +
				"\n P.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID, " +
				"\n P.XX_VMR_BRAND_ID, C.XX_VMR_COLLECTION_ID, L.M_PRODUCT_ID, R.XX_VMR_VENDORPRODREF_ID, C.DATEORDERED) ";
			
		System.out.println(SQLSales);
		updated =  DB.executeUpdate(null, SQLSales);
		System.out.println("UPDATED: "+updated);
		return msj;
	}//doIt

	private void createInventoryTemp() {
		try{
			String sql = "CREATE TABLE XX_INVENTORYPRODUCT_TEMP AS (SELECT INV.M_PRODUCT_ID, INV.M_WAREHOUSE_ID, " +
					" SUM(INV.XX_INITIALINVENTORYAMOUNT + INV.XX_SHOPPINGAMOUNT  + INV.XX_MOVEMENTAMOUNT - INV.XX_SALESAMOUNT) INVENTORYQTY " +
					" FROM XX_VCN_INVENTORY INV" +
					" WHERE  INV.XX_INVENTORYYEAR = TO_CHAR( TRUNC(SYSDATE-2)  ,'YYYY')      " +
					" AND INV.XX_INVENTORYMONTH = TO_CHAR( TRUNC(SYSDATE-2)  ,'MM') " +
					" GROUP BY  INV.M_PRODUCT_ID, INV.M_WAREHOUSE_ID)";
			DB.executeUpdate(null, sql);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteInventoryTemp() {
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DROP FROM XX_INVENTORYPRODUCT_TEMP";
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DB.closeStatement(psDelete);
		}
		
	}

	@Override
	protected void prepare() {
		
	}//prepare	
	
	/** findMaxID
	 * Funcion auxiliar que permite determinar el id maximo existente en una tabla, para así asignar
	 * el siguiente valor al insertar nuevos datos. 
	 * @return IDMax Máximo id en XX_VMR_HISTORICSALES */	
	private static Integer findMaxID(String tabla) {
		String ultimoId = " SELECT MAX("+tabla+"_ID) FROM "+tabla;
		PreparedStatement ultDet = DB.prepareStatement(ultimoId, null);
		ResultSet maximo = null;
		Integer idResQty = 0;
		
		try {
			maximo = ultDet.executeQuery();
			while (maximo.next()) {
				idResQty = maximo.getInt(1); 
			}
		} 
		catch (SQLException e1) { e1.printStackTrace(); }
		finally{
			DB.closeResultSet(maximo);
			DB.closeStatement(ultDet);
		}
		return idResQty;
	} // findMaxID

} 

