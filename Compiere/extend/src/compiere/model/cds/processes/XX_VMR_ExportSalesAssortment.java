package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/*
 * Proceso que sincroniza los totales en Compiere correspondientes a las ventas del día anterior 
 * y el acumulado del mes hasta el día anterior, con la tabla XX_VMR_SalesAssortment y elimina los valores
 * registrados el día anterior a ese. 
 * 
 * @author Gabriela Marques.
 */

public class XX_VMR_ExportSalesAssortment extends SvrProcess {


	@Override
	protected String doIt() throws Exception {

		Date fechaActual = new Date();
		Date fechaAyer = new Date( fechaActual.getTime()-86400000);
		SimpleDateFormat dateFormatBD = new SimpleDateFormat("yyyyMMdd");
		String fechaBD= dateFormatBD.format(fechaActual);
		String fechaBDAyer = dateFormatBD.format(fechaAyer);
		String anio = fechaBD.charAt(0)+""+fechaBD.charAt(1)+""+fechaBD.charAt(2)+""+fechaBD.charAt(3);
		String mes = fechaBD.charAt(4)+""+fechaBD.charAt(5);
		
		// Primero eliminar los registros del día anterior
		//eliminarRegistroAnterior(dateFormatBD.format(fechaAyer.getTime()-86400000));
		eliminarRegistroAnterior(); //Eliminar todos los registros
		
		// Obtener la informacion de ventas del día y acumulado del mes
		int idMax = findIDSalesAssort();
		String sqlVentas = "INSERT INTO XX_VMR_SALESASSORTMENT (" +
				"\n             XX_VMR_SalesAssortment_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
				"\n             Created, CreatedBy, Updated, UpdatedBy, Name, Value," +
				"\n             XX_VMR_Category_ID, XX_VMR_Department_ID, XX_VMR_Line_ID, " +
				"\n             XX_VMR_Section_ID, XX_VMR_TypeInventory_ID, M_ATTRIBUTESETINSTANCE_ID, XX_VMR_Collection_ID," +
				"\n             XX_VMR_Package_ID, XX_VMR_Brand_ID, XX_VMA_Season_ID, M_Warehouse_ID, XX_VMR_SalesDate," +
				"\n             M_Product_ID, XX_VMR_DaySalesPieces, XX_VMR_DaySalesAmount, XX_VMR_MonthSalesPieces," +
				"\n             XX_VMR_MonthSalesAmount) " +
				"\n         ( select "+ idMax + " + rownum, a.* from (SELECT  " +
				"\n             p.AD_Client_ID, p.AD_Org_ID, 'Y'," +
				"\n             SYSDATE d1, p.CreatedBy, SYSDATE d2, p.UpdatedBy, p.Name, p.Value," +
				"\n             p.XX_VMR_Category_ID,  " +
				"\n             p.XX_VMR_DEPARTMENT_ID,  " +
				"\n             p.XX_VMR_LINE_ID,  " +
				"\n             p.XX_VMR_SECTION_ID, " +
				"\n             p.XX_VMR_TYPEINVENTORY_ID, ol.M_ATTRIBUTESETINSTANCE_ID," +
				"\n             pack.XX_VMR_COLLECTION_ID,  " +
				"\n             atri.XX_VMR_PACKAGE_ID, " +
				"\n             p.XX_VMR_BRAND_ID," +
				"\n             col.XX_VMA_SEASON_ID, " +
				"\n             ord.M_WAREHOUSE_ID, " +
				"\n             ord.dateordered, " +
				"\n             ol.m_product_id, " +
				"\n             coalesce(sum(ol.QTYORDERED), 0) as xx_vmr_daysalespieces, " +
				"\n             coalesce(sum((ol.PRICEACTUAL + ol.XX_EMPLOYEEDISCOUNT)*ol.QTYORDERED), 0) as xx_vmr_daysalesamount," +
				"\n             coalesce(XX_SALESQUANTITY, 0) as xx_vmr_monthsalespieces," +
				"\n             coalesce(XX_SALESAMOUNT, 0) as xx_vmr_monthsalesamount " +
				"\n          FROM c_order ord  join  c_orderline ol ON (ord.C_ORDER_ID = ol.C_ORDER_ID) " + //--Para las ventas del día actual
				"\n             join m_product p  on (p.M_PRODUCT_ID = ol.M_PRODUCT_ID)" +
				"\n             left join M_ATTRIBUTESETINSTANCE atri on (atri.M_ATTRIBUTESETINSTANCE_ID = ol.M_ATTRIBUTESETINSTANCE_ID)" +
				"\n             left outer join XX_VMR_PACKAGE pack on (pack.XX_VMR_PACKAGE_ID=atri.XX_VMR_PACKAGE_ID)" +
				"\n       		left outer join XX_VMR_COLLECTION col on (col.XX_VMR_COLLECTION_ID=pack.XX_VMR_COLLECTION_ID)" +
				"\n       		left outer join XX_VCN_INVENTORY I on (ol.m_product_id = i.m_product_id " +
				"\n		                            and i.M_WAREHOUSE_ID = ord.M_WAREHOUSE_ID" +
				"\n		                            and i.M_ATTRIBUTESETINSTANCE_ID = ol.M_ATTRIBUTESETINSTANCE_ID" +
				"\n		                            and p.XX_VMR_Category_ID = I.XX_VMR_Category_ID" +
				"\n		                            and p.XX_VMR_DEPARTMENT_ID = I.XX_VMR_DEPARTMENT_ID" +
				"\n		                            and p.XX_VMR_LINE_ID = I.XX_VMR_LINE_ID" +
				"\n		                            and p.XX_VMR_SECTION_ID = I.XX_VMR_SECTION_ID" +
				"\n		                            and ol.xx_priceconsecutive = i.xx_consecutiveprice" +
				"\n		                            and i.xx_inventoryyear = '"+anio+"' and i.xx_inventorymonth = '"+mes+"' " + //--Acumulado hasta el día actual
		//		"\n		                            and i.xx_inventoryyear = '2011' and i.xx_inventorymonth = '09' " + //--Acumulado hasta el día actual
				"\n		                            )" +
				"\n          WHERE ord.issotrx='Y'" +
		//		"\n       		and p.ISACTIVE='Y' and TO_CHAR(ord.dateordered,'YYYYMMDD') = 20110911"+//+fechaBD; //"+//anniomes+"01 and "+ fechaBD +
				"\n       		and p.ISACTIVE='Y' and TO_CHAR(ord.dateordered,'YYYYMMDD') = "+fechaBDAyer + //"+//anniomes+"01 and "+ fechaBD +
				"\n          GROUP BY (" +
				"\n             p.AD_Client_ID, p.AD_Org_ID, 'Y'," +
				"\n             SYSDATE, p.CreatedBy, SYSDATE, p.UpdatedBy, p.Name, p.Value," +
				"\n             p.XX_VMR_Category_ID," +
				"\n             p.XX_VMR_DEPARTMENT_ID," +
				"\n             p.XX_VMR_LINE_ID," +
				"\n             p.XX_VMR_SECTION_ID," +
				"\n             p.XX_VMR_TYPEINVENTORY_ID, ol.M_ATTRIBUTESETINSTANCE_ID," +
				"\n             pack.XX_VMR_COLLECTION_ID," +
				"\n             atri.XX_VMR_PACKAGE_ID," +
				"\n       		p.XX_VMR_BRAND_ID," +
				"\n       		col.XX_VMA_SEASON_ID," +
				"\n       		ord.M_WAREHOUSE_ID," +
				"\n       		ord.dateordered," +
				"\n             ol.m_product_id, XX_SALESQUANTITY, XX_SALESAMOUNT)" +
				"\n             ) a " +
				"\n           ) " ;
		        
		//System.out.println("Query: "+ sqlVentas);
		PreparedStatement psVentas = null;
		ResultSet rsVentas = null;	
		 
		try {
			 psVentas = DB.prepareStatement(sqlVentas, null);
			 rsVentas = psVentas.executeQuery();
		} catch (Exception e) {
			 e.printStackTrace();	
		} finally {
			 rsVentas.close();
			 psVentas.close();	 
		}
		
		return "Sincronización Completa";
	}


	@Override
	protected void prepare() {
		//NO
	}
	
	/** findIDSalesAssort
	 * Funcion auxiliar que permite determinar el id maximo existente en la tabla, para así asignar
	 * el siguiente valor al insertar nuevos datos. 
	 * return IDMax Máximo id en XX_VMR_SALESASSORTMENT */	
	public static Integer findIDSalesAssort() {
		String ultimoId = " SELECT MAX(XX_VMR_SALESASSORTMENT_ID) FROM XX_VMR_SALESASSORTMENT";
		PreparedStatement ultDet = DB.prepareStatement(ultimoId, null);
		ResultSet maximo = null;
		Integer idSalesAssort = 0;
		try {
			maximo = ultDet.executeQuery();
			while (maximo.next()) {
				idSalesAssort = maximo.getInt(1); 
			}
		} 
		catch (SQLException e1) { e1.printStackTrace(); }
		finally{
			DB.closeResultSet(maximo);
			DB.closeStatement(ultDet);
		}
		return idSalesAssort;
	} // findIDSalesAssort
	
	
	/**
	 * 		eliminarRegistroAnterior elimina el inventario registrado del día anterior (fechaAyer)
	 * @throws Exception
	 */
	//private void eliminarRegistroAnterior(String fechaAnteAyer) throws Exception { //Eliminar todos los registros
	private void eliminarRegistroAnterior() throws Exception {
		String queryDelete = "\n DELETE FROM XX_VMR_SALESASSORTMENT " ; //+
					//"\n WHERE TO_CHAR(XX_VMR_SALESDATE,'YYYYMMDD') = " + fechaAnteAyer; //--Ventas del día anterior
		//System.out.println("Eliminar: "+queryDelete);
		
		try {
			DB.executeUpdate(null,queryDelete);
		} catch (Exception e) {
			//ADialog.error(Env.getWindowNo(null), null, "XX_DatabaseAccessError");
			e.printStackTrace();
		} 
			
	}
	

}