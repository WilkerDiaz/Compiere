package compiere.model.cds.processes;

import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
//import java.util.Iterator;
//import java.util.Vector;
//import java.util.zip.*;

//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRichTextString;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import org.compiere.excel.Excel;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

//import jxl.Cell;
import jxl.Workbook;
import jxl.format.Alignment;
//import jxl.format.Border;
//import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
//import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import compiere.model.cds.As400DbManager;

/*
 * Proceso que sincroniza el inventario y las ventas del día anterior (Tablas XX_VMR_INVENTORYFILE 
 *  y XX_VMR_SALESFILE, respectivamente,
 * para crear el Archivo de Inventario y el Archivo de Ventas en la ruta señalada por las variables
 * srcInventario y srcVentas, respectivamente.
 * 
 * @author Gabriela Marques.
 */

public class XX_VMR_ExportInventory extends SvrProcess {
	
	static String srcInventario = "\\\\boleita\\sistemas\\Computacion\\Proyectos_Desarrollo\\Cadena_de_Suministros\\INVENTARIO\\";
	//static String srcVentas = "\\\\boleita\\sistemas\\Computacion\\Proyectos_Desarrollo\\Cadena_de_Suministros\\VENTAS\\";
	//static String srcInventario = "\\\\192.168.1.3\\merchandising\\Planificacion\\INVENTARIOS\\";
	static String srcVentas = "\\\\boleita\\todobeco\\";
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static NumberFormat formatter = new DecimalFormat("00");
	
	@Override
	protected String doIt() throws Exception {
		
		String res = "";
		//Inicio
		System.out.println ("Inicio: "+new Date());
		
		//Actualizar el inventario
		updateInventory();
		
		//Actualizar las ventas
		updateSales();

		// *** Generar los archivos xls ***
		
		//Archivo de inventario (separado por categorías)
//		System.out.println("\n Exportando archivos de Inventario... ");
//		List<String> categorias = listaCategorias();
//		int i = 0;
//		boolean exportInv = false;
//		while (i<categorias.size()) {
//			exportInv = exportInventoryTable(srcInventario, categorias.get(i++), categorias.get(i));
//			System.out.println("\n Categoría " + categorias.get(i++));
//		}
//		
//		//Archivo de ventas
//		boolean exportSal = exportSalesTable(srcVentas);
//		
//		if (!exportInv) { res += " *** No se pudo generar el archivo de Inventario. "; }
//		else { res += " Generado el archivo de Inventario. "; }
//		if (!exportSal) { res += " *** No se pudo generar el archivo de Ventas. "; } 
//		else { res += " Generado el archivo de Ventas. "; }
//		
		//Finalización
		System.out.println ("Fin: "+new Date());
		
		return res + "\nSincronización Completa. ";
	}

	@Override
	protected void prepare() {
		
	}
	
	/**
	 * 		Actualiza el inventario del día anterior en la tabla XX_VMR_INVENTORYFILE
	 * @throws SQLException
	 */
	protected void updateInventory() throws Exception {
		//Se eliminan los registros de la tabla XX_VMR_INVENTORYFILE
		eliminarRegTabla("XX_VMR_INVENTORYFILE");
		
		//Se actualizan las cantidades apartadas en la tabla temporal de Compiere XX_VMR_RESERVEDQTY
		//obteniendo la información del AS400
		actualizarCantApartadas();
		
		Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        if (day == 1 ) { // En caso de ser el primero de mes, interesa el mes anterior
        	cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        int mes = cal.get(Calendar.MONTH) + 1;
        int anio = cal.get(Calendar.YEAR);
		
		//Se inserta el inventario actual en la tabla XX_VMR_INVENTORYFILE
		int idMax = findMaxID("XX_VMR_INVENTORYFILE");
		
		String queryUpdate = " INSERT INTO XX_VMR_INVENTORYFILE (" +
		"\n       XX_VMR_INVENTORYFILE_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
		"\n       Created, CreatedBy, Updated, UpdatedBy, Name, Value," +
		"\n       xx_warehouseBecoNumber, xx_inventorymonth, xx_inventoryyear," +
		"\n       XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID," +
		"\n       xx_productCode, xx_priceconsecutive, m_ATTRIBUTESETINSTANCE_ID, " +
		"\n       xx_entrancemonth," +
		"\n       xx_entranceyear," +
		"\n       xx_product_name, xx_vmr_vendorprodref_id, XX_VMR_BRAND_ID,  " +
		"\n       C_BPARTNER_ID, " +
		"\n       xx_typeInventory," +
		"\n       xx_vme_conceptvalue_id," +
		"\n       XX_CONSECUTIVEORIGIN," +
		"\n       xx_initialinventoryquantity, xx_initialinventoryamount," +
		"\n       xx_shoppingquantity, xx_shoppingamount," +
		"\n       xx_salesquantity, xx_salesamount," +
		"\n       xx_movementquantity, xx_movementamount," +
		"\n       xx_finalInventoryQuantity, xx_finalInventoryAmount," +
		"\n       XX_ADJUSTMENTSQUANTITY, XX_ADJUSTMENTSAMOUNT," +
		"\n       xx_longCharName," +
		"\n       xx_reservedQty, " +
		"\n       xx_margin, xx_promotionExpenses, xx_characteristic1, xx_characteristic2, xx_characteristic3" +
		"\n  ) ( " +
		"\n  select "+ idMax + " + rownum, a.* from ( SELECT " +
		"\n      "+ m_AD_Client_ID + " cli, "+ m_AD_Org_ID + " org, 'Y'," +
		"\n       SYSDATE d1, "+ m_by +" cre_by, SYSDATE d2, "+ m_by +" up_by, p.Name, p.Value," +
		"\n 	  w.value w, i.xx_inventorymonth, i.xx_inventoryyear," +
		"\n       i.XX_VMR_CATEGORY_ID cat, i.XX_VMR_DEPARTMENT_ID dep, i.XX_VMR_LINE_ID lin, i.XX_VMR_SECTION_ID sec," +
		"\n 	  p.value p, max(i.xx_consecutiveprice), NVL(i.m_ATTRIBUTESETINSTANCE_ID,0), " +
		"\n       TO_CHAR(c.CREATED,'MM')," +
		"\n       TO_CHAR(c.CREATED,'YYYY')," +
		"\n       p.name pn, p.xx_vmr_vendorprodref_id pv, p.XX_VMR_BRAND_ID bv, " +
		"\n       p.C_BPARTNER_ID partn," +
		"\n       typeInv.name tinv," +
		"\n       p.xx_vme_conceptvalue_id conc," +
		"\n       c.XX_CONSECUTIVEORIGIN," +
		"\n       xx_initialinventoryquantity, xx_initialinventoryamount," +
		"\n       xx_shoppingquantity, xx_shoppingamount," +
		"\n       xx_salesquantity, xx_salesamount," +
		"\n       xx_movementquantity, xx_movementamount ," +
		"\n 	  xx_initialinventoryquantity + xx_shoppingquantity + xx_movementquantity - xx_salesquantity," +
		"\n       xx_initialinventoryamount + xx_shoppingamount + xx_movementamount - xx_salesamount," +
		"\n       XX_ADJUSTMENTSQUANTITY, XX_ADJUSTMENTSAMOUNT," +
		"\n       lngc.name longcn," +
		"\n       xx_reservedqty," +
		"\n       CASE c.XX_SalePrice WHEN 0 THEN 0 ELSE ROUND( ((c.XX_SalePrice - c.XX_UnitPurchasePrice)/c.XX_SalePrice) * 100, 2) END," +
		"\n       ROUND( (c.XX_SalePrice * xx_salesquantity) - xx_salesamount, 2), " +
		"\n       max(case when seq=1 then caracterist end) caract1, " +
		"\n       max(case when seq=2 then caracterist end) caract2, " +
		"\n       max(case when seq=3 then caracterist end) caract3 " +
		"\n  FROM" +
		"\n       XX_VCN_Inventory i" +
		"\n 	  join M_PRODUCT p on (p.m_product_id = i.m_product_id)" +
		"\n 	  join m_WAREHOUSE w on (i.M_WAREHOUSE_ID = w.M_WAREHOUSE_ID)" +
		"\n       join XX_VMR_TYPEINVENTORY typeInv on (typeInv.XX_VMR_TYPEINVENTORY_ID = p.XX_VMR_TYPEINVENTORY_ID)" +
		"\n       left join XX_VMR_PRICECONSECUTIVE c on (c.xx_priceconsecutive = i.xx_consecutiveprice and c.m_product_id = i.m_product_id" +
		"\n       			and nvl(c.M_ATTRIBUTESETINSTANCE_ID,0) = nvl(i.M_ATTRIBUTESETINSTANCE_ID,0))" +
		"\n       left join XX_VMR_LONGCHARACTERISTIC lngc on (lngc.XX_VMR_LONGCHARACTERISTIC_ID = p.XX_VMR_LONGCHARACTERISTIC_ID)" +
		"\n       left join XX_VMR_RESERVEDQTY rqt on (rqt.xx_warehousebeconumber = w.Value " +
		"\n       			and rqt.xx_priceconsecutive = i.xx_consecutiveprice and rqt.xx_productCode = p.Value)" +
		"\n       left join (SELECT m_attributesetinstance_id, value caracterist, " +
		"\n						ROW_NUMBER() over(partition by m_attributesetinstance_id ORDER BY m_attributesetinstance_id) AS seq " +
		"\n					FROM m_attributeinstance) " +
		"\n       			att  on (NVL(att.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(p.m_ATTRIBUTESETINSTANCE_ID,0) ) " +
		"\n  WHERE" +
		"\n       XX_INVENTORYYEAR = '"+anio+"' and XX_INVENTORYMONTH = '"+mes+"'" +
		
		"\n  GROUP BY " +
		"\n           "+ m_AD_Client_ID + ", "+ m_AD_Org_ID + ", 'Y'," +
		"\n       	  SYSDATE, "+ m_by +", SYSDATE, "+ m_by +", p.Name, p.Value," +
		"\n        	  w.value , i.xx_inventorymonth, i.xx_inventoryyear, " +
		"\n              i.XX_VMR_CATEGORY_ID , i.XX_VMR_DEPARTMENT_ID , i.XX_VMR_LINE_ID , i.XX_VMR_SECTION_ID , " +
		"\n        	  p.value , i.xx_consecutiveprice, NVL(i.m_ATTRIBUTESETINSTANCE_ID,0), " +
		"\n              TO_CHAR(c.CREATED,'MM'), " +
		"\n              TO_CHAR(c.CREATED,'YYYY'), " +
		"\n              p.name , p.xx_vmr_vendorprodref_id , p.XX_VMR_BRAND_ID ,  " +
		"\n              p.C_BPARTNER_ID , " +
		"\n              typeInv.name , " +
		"\n              p.xx_vme_conceptvalue_id , " +
		"\n              c.XX_CONSECUTIVEORIGIN, " +
		"\n              xx_initialinventoryquantity, xx_initialinventoryamount, " +
		"\n              xx_shoppingquantity, xx_shoppingamount, " +
		"\n              xx_salesquantity, xx_salesamount, " +
		"\n              xx_movementquantity, xx_movementamount , " +
		"\n        	  xx_initialinventoryquantity + xx_shoppingquantity + xx_movementquantity - xx_salesquantity, " +
		"\n              xx_initialinventoryamount + xx_shoppingamount + xx_movementamount - xx_salesamount, " +
		"\n              XX_ADJUSTMENTSQUANTITY, XX_ADJUSTMENTSAMOUNT, " +
		"\n              lngc.name , " +
		"\n              xx_reservedqty, " +
		"\n              CASE c.XX_SalePrice WHEN 0 THEN 0 ELSE ROUND( ((c.XX_SalePrice - c.XX_UnitPurchasePrice)/c.XX_SalePrice) * 100, 2) END, " +
		"\n              ROUND( (c.XX_SalePrice * xx_salesquantity) - xx_salesamount, 2) " +
		"\n       ) a " +
		"\n    ) " ;

		System.out.println("\nActualizando inventario... ");
//		System.out.println("Query: "+ queryUpdate);
//		PreparedStatement psInventario = null;
//		ResultSet rsInventario = null;	
		 
		try {
//			psInventario = DB.prepareStatement(queryUpdate, null);
//			rsInventario = psInventario.executeQuery();
			DB.executeUpdate(null, queryUpdate);
			System.out.println("Inventario actualizado. ");
		} catch (Exception e) {
			e.printStackTrace();	
			System.out.println("No se pudo actualizar el inventario. ");
		} finally {
			eliminarRegTabla("XX_VMR_RESERVEDQTY"); //Vaciar la tabla temporal de cantidades apartadas
//			DB.closeResultSet(rsInventario);
//			DB.closeStatement(psInventario);
		}
		
	}
	
	
	/**
	 * 		Actualiza las ventas del día anterior en la tabla XX_VMR_SALESFILE
	 * @throws Exception 
	 */
	protected void updateSales() throws Exception {
		
		//Se eliminan los registros de la tabla XX_VMR_SALESFILE (Ventas anteriores)
		eliminarRegTabla("XX_VMR_SALESFILE");

		Calendar cal = java.util.Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1); //Dia anterior
		int anioActual = cal.get(Calendar.YEAR);
		String mesActual = formatter.format(cal.get(Calendar.MONTH)+1);
		
		//Se inserta la información de las ventas en la tabla XX_VMR_SALESFILE
		int idMax = findMaxID("XX_VMR_SALESFILE");
		String queryUpdate = "INSERT INTO XX_VMR_SALESFILE (" +
			"\n      XX_VMR_SALESFILE_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
			"\n      Created, CreatedBy, Updated, UpdatedBy, Name, Value," +
			"\n      xx_warehouseBecoNumber, xx_vmr_salesdate," +
			"\n      xx_categoryCode, xx_departmentCode, xx_lineCode, xx_sectionCode," +
			"\n      xx_productCode, xx_priceconsecutive," +
			"\n      xx_product_name, XX_VendorProdRef, XX_Brand_Name, " +
			"\n      xx_bPartnerCode, XX_BPartnerName," +
			"\n      xx_salesreg, xx_salesquantity," +
			"\n      xx_promotionExpenses, xx_promotionQty" +
			"\n ) ( SELECT "+ idMax + " + rownum," +
			"\n      AD_Client_ID, AD_Org_ID, 'Y'," +
			"\n      d1, CreatedBy, d2, UpdatedBy, Name, Value," +
			"\n      w, dateordered, cat, dep, lin, sec, p, pcons, pn, pv, bn, part, partn," +
			"\n		 VTA_REG," +
			"\n		 CANT_VTA," +
			"\n      PROM, " +
			"\n		 CANT_PROM" + 
			"\n		FROM (" +
			"\n 	SELECT " +
			"\n      p.AD_Client_ID, p.AD_Org_ID, 'Y'," +
			"\n      SYSDATE d1, p.CreatedBy, SYSDATE d2, p.UpdatedBy, p.Name, p.Value," +
			"\n      w.value w, ord.DATEORDERED dateordered, " +
			"\n      cat.value cat, dep.value dep, lin.value lin, sec.value sec," +
			"\n      p.value p, ol.XX_PRICECONSECUTIVE pcons," +
			"\n      p.name pn, prodRef.value pv, bra.name bn," +
			"\n      part.value part, part.Name partn," +
			"\n      sum(TRUNC(ol.PriceList * ol.QtyEntered)) VTA_REG, " +
			"\n 	 SUM(ol.QtyEntered) CANT_VTA," +
			"\n		 TRUNC((ol.PriceList-ol.PriceActual) * ol.QtyEntered) PROM, " +
			"\n      CASE WHEN ol.PriceActual <> ol.PriceList THEN ol.Qtyentered ELSE 0 END CANT_PROM " +
			"\n 	FROM" +
			"\n      c_order ord" +
			"\n      join c_orderline ol on (ord.c_order_id = ol.c_order_id)" +
			"\n      join m_WAREHOUSE w on (ord.M_WAREHOUSE_ID = w.M_WAREHOUSE_ID)" +
			"\n      join M_PRODUCT p on (p.m_product_id = ol.m_product_id)" +
			"\n      join XX_VMR_CATEGORY cat on (cat.XX_VMR_CATEGORY_ID = p.XX_VMR_CATEGORY_ID)" +
			"\n      join XX_VMR_DEPARTMENT dep on (dep.XX_VMR_DEPARTMENT_ID = p.XX_VMR_DEPARTMENT_ID)" +
			"\n      join XX_VMR_LINE lin on (lin.XX_VMR_LINE_ID = p.XX_VMR_LINE_ID)" +
			"\n      join XX_VMR_SECTION sec on (sec.XX_VMR_SECTION_ID = p.XX_VMR_SECTION_ID)" +
			"\n      join C_BPARTNER part on (part.C_BPARTNER_ID = p.C_BPARTNER_ID)" +
			"\n      join XX_VMR_VENDORPRODREF prodRef on (prodRef.xx_vmr_vendorprodref_id = p.xx_vmr_vendorprodref_id)" +
			"\n      join XX_VMR_BRAND bra on (bra.XX_VMR_BRAND_ID = p.XX_VMR_BRAND_ID)" +
			"\n 	WHERE" +
			"\n      TO_CHAR(ord.DateOrdered,'YYYY') = '"+anioActual+"' and TO_CHAR(ord.DATEORDERED,'MM') = '"+mesActual+"'" +
			"\n      and issotrx = 'Y' " +
//"			and p.value = '1290188' "+ //(prueba)
			"\n 	GROUP BY " +
			"\n      p.AD_Client_ID, p.AD_Org_ID, 'Y'," +
			"\n      SYSDATE, p.CreatedBy, SYSDATE, p.UpdatedBy, p.Name, p.Value," +
			"\n      w.value, ord.DATEORDERED," +
			"\n      cat.value, dep.value, lin.value, sec.value," +
			"\n      p.value, ol.XX_PRICECONSECUTIVE," +
			"\n      p.name, prodRef.value, " +
			"\n      part.value, part.Name, bra.name, " + //TRUNC(ol.PriceList * ol.QtyEntered), " +
			"\n		 TRUNC((ol.PriceList-ol.PriceActual) * ol.QtyEntered), " +
			"\n		 CASE WHEN ol.PriceActual <> ol.PriceList THEN ol.Qtyentered ELSE 0 END" +
			"\n 	) " +
			"\n )" ;

		System.out.println("\nActualizando las ventas...");
		//System.out.println("Query ventas: \n"+ queryUpdate);
		//PreparedStatement psVentas = null;
		//ResultSet rsVentas = null;	
		 
		try {
			//psVentas = DB.prepareStatement(queryUpdate, null);
			//rsVentas = psVentas.executeQuery();
			DB.executeUpdate(null, queryUpdate);
			System.out.println("Ventas actualizadas.");
			//commit();
		} catch (Exception e) {
			 e.printStackTrace();	
			 System.out.println("No se pudieron actualizar las ventas.");
		} finally {
			//DB.closeResultSet(rsVentas);
			//DB.closeStatement(psVentas);
		}
		
	}
	
	
	/**
	 * 	Función que obtiene las cantidades apartadas de la tabla ATCD30 del esquema BECOFILE en el AS/400
	 * y las almacena en la tabla XX_VMR_RESERVEDQTY de Compiere
	 * @throws SQLException 
	 * 
	 */
	private void actualizarCantApartadas() throws SQLException {
		
		// En caso de no haberse ejecutado antes
		eliminarRegTabla("XX_VMR_RESERVEDQTY");
		
		String queryAS400 = "\n SELECT TIENDA, CONPRE, CODPRO, sum(CANPRO) reservedQty from becofile.ATCD30 " +
				"\n	group by TIENDA, CONPRE, CODPRO " ;
		//System.out.println("Query AS400 apartadas: "+queryAS400);
		
		ResultSet rs = null;	
		Statement ps_s = null;
		As400DbManager as400 = new As400DbManager();
		as400.conectar();
		Statement pstmtProds = DB.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, null);
		try {
			ps_s = as400.conexion.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			rs= as400.realizarConsulta(queryAS400, ps_s);
			
			int idMax = findMaxID("XX_VMR_RESERVEDQTY")+1; //En caso de que no se hayan borrado en la última ejecución
			System.out.println("\nInsertando cantidades apartadas en Compiere... ");
			while (rs.next()) {
				//Crear querys a insertar en compiere
				String queryInsert = "INSERT INTO XX_VMR_RESERVEDQTY (" +
						"XX_VMR_RESERVEDQTY_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
						"\n Created, CreatedBy, Updated, UpdatedBy, Name, Value," +
						"\n xx_warehouseBecoNumber, xx_productCode, xx_priceconsecutive, xx_reservedQty) " +
						"\n VALUES (" +
						"\n "+ idMax + ", '0', '0', 'Y'," +
						"\n SYSDATE, '0', SYSDATE, '0', '0', '0', '" +
						rs.getInt("TIENDA") + "', '" + rs.getInt("CODPRO") + "', '" + 
						rs.getInt("CONPRE") + "', '" + rs.getInt("reservedQty") + "' )";
				//System.out.println("Query Insert Compiere: "+ queryInsert);
				idMax++;
				//Agregar batch
				try { 
					pstmtProds.addBatch(queryInsert); 
				} 
				catch (SQLException e) { 
					e.printStackTrace(); 
				}
			}
			
			//Ejecutar batch
			try { 
				int[] updCnt = pstmtProds.executeBatch();
				System.out.println("Total de registros insertados: "+ updCnt[0]);
				pstmtProds.close();
			} 
			catch (SQLException e) { 
				e.printStackTrace(); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			as400.desconectar();
			DB.closeResultSet(rs);
			DB.closeStatement(ps_s);
		}
		
	}
	
	
	
	/** findMaxID
	 * 	Funcion auxiliar que permite determinar el id maximo existente en una tabla, para así asignar
	 * 		el siguiente valor al insertar nuevos datos. 
	 * return IDMax Máximo id en XX_VMR_RESERVEDQTY */	
	private static Integer findMaxID(String tabla) {
		String ultimoId = " SELECT MAX("+tabla+"_ID) FROM "+tabla;
		PreparedStatement ultDet = DB.prepareStatement(ultimoId, null);
		ResultSet maximo = null;
		Integer idResQty = 0;
		//System.out.println(ultimoId);
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
	
	
	
	/**
	 * 	Funcion auxiliar que permite eliminar todos los registros de una tabla
	 */
	private void eliminarRegTabla(String tabla) throws SQLException {
		String queryDelete = "\n DELETE FROM "+tabla ;
		//System.out.println("Query eliminar: "+queryDelete);
		
		System.out.println("\nEliminando registros de la tabla "+tabla +"... ");
		
		int res = DB.executeUpdate(null, queryDelete);
		System.out.println("Eliminados "+res+" registros." );
	}
	
	// ************** FIN SINCRONIZACION
	
	// ************** INICIO CREACIÓN DE ARCHIVOS
	
	
	protected static List<String> listaCategorias() {

		List<String> categs = new ArrayList<String>();
		String sql = "\n SELECT DISTINCT XX_VMR_CATEGORY_ID, c.name name FROM xx_vmr_inventoryfile i " +
				" join xx_vmr_category c using (XX_VMR_CATEGORY_ID)"  ;
		
		System.out.println(sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			// Cabeceras del XLS
			while (rs.next()) {
				categs.add(rs.getString("XX_VMR_CATEGORY_ID"));
				categs.add(rs.getString("name"));
			}
		} catch (SQLException e) {			
			//log.log(Level.SEVERE, sql, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return categs;
	}

	
	/**
	 * Exporta la información del inventario contenida en la tabla XX_VMR_INVENTORYFILE en un archivo CSV.
	 * @param path El directorio donde almacenar el archivo. 
	 */
	protected static boolean exportInventoryTable(String path, String categoria, String categname) {
		
		String sql = "\n SELECT     " +
				"\n    xx_warehouseBecoNumber, xx_inventorymonth, xx_inventoryyear, " +
				"\n    cat.value cat, dep.value dep, lin.value lin, sec.value sec," +
				"\n    xx_productCode, xx_priceconsecutive, " +
				"\n    nvl(xx_entrancemonth, (select to_char(created, 'MM') " + 
				"\n    		from m_attributesetinstance att where att.m_attributesetinstance_id = i.m_attributesetinstance_id)) xx_entrancemonth, " + 
				"\n    nvl(xx_entranceyear, (select to_char(created, 'YYYY') " +
				"\n    		from m_attributesetinstance att where att.m_attributesetinstance_id = i.m_attributesetinstance_id)) xx_entranceyear,  " +
				"\n    xx_product_name, prodRef.value pv, bra.name bn,  " +
				"\n    part.value part, part.Name partn, " +
				"\n    xx_typeInventory, " +
				"\n    conc.name conc, " +
				"\n    XX_CONSECUTIVEORIGIN, " +
				"\n    xx_initialinventoryquantity, xx_initialinventoryamount, " +
				"\n    xx_shoppingquantity, xx_shoppingamount, " +
				"\n    xx_salesquantity, xx_salesamount, " +
				"\n    xx_movementquantity, xx_movementamount, " +
				"\n    xx_finalInventoryQuantity, xx_finalInventoryAmount, " +
				"\n    XX_ADJUSTMENTSQUANTITY, XX_ADJUSTMENTSAMOUNT, " +
				"\n    xx_longCharName, " +
				"\n    xx_reservedQty,  " +
				"\n    xx_margin, xx_promotionExpenses," +
				"\n    XX_CHARACTERISTIC1, XX_CHARACTERISTIC2, XX_CHARACTERISTIC3 " +
				"\n  FROM xx_vmr_inventoryfile i" +
				"\n 	join XX_VMR_CATEGORY cat on (cat.XX_VMR_CATEGORY_ID = i.XX_VMR_CATEGORY_ID)" +
				"\n       join XX_VMR_DEPARTMENT dep on (dep.XX_VMR_DEPARTMENT_ID = i.XX_VMR_DEPARTMENT_ID)" +
				"\n       join XX_VMR_LINE lin on (lin.XX_VMR_LINE_ID = i.XX_VMR_LINE_ID)" +
				"\n       join XX_VMR_SECTION sec on (sec.XX_VMR_SECTION_ID = i.XX_VMR_SECTION_ID)" +
				"\n       join XX_VMR_VENDORPRODREF prodRef on (prodRef.xx_vmr_vendorprodref_id = i.xx_vmr_vendorprodref_id)" +
				"\n       join XX_VMR_BRAND bra on (bra.XX_VMR_BRAND_ID = i.XX_VMR_BRAND_ID)" +
				"\n 	  join C_BPARTNER part on (part.C_BPARTNER_ID = i.C_BPARTNER_ID)" +
				"\n       join XX_VME_CONCEPTVALUE conc on (conc.xx_vme_conceptvalue_id = i.xx_vme_conceptvalue_id)" +
				"\n   where XX_VMR_CATEGORY_ID = '" + categoria + "' " + //  and xx_warehouseBecoNumber <> 1 
				"\n    ORDER BY cat.value , dep.value , lin.value , sec.value , xx_priceconsecutive, " +
				"\n    xx_warehouseBecoNumber, prodRef.value,  xx_productCode " ;
		
		System.out.println(sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
				
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			try {
				Calendar cal = java.util.Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -1); //Dia anterior
				String mesActual = formatter.format(cal.get(Calendar.MONTH)+1);
				String diaActual = formatter.format(cal.get(Calendar.DAY_OF_MONTH));
				String nombre = path + categname + "\\" + "Inventario " + diaActual + "-" + mesActual ;
				File direct = new File(path+categname+"\\");
				if (!direct.exists()) {
					direct.mkdir();
				}
				
				// Cabeceras del XLS
				List<String> titulos = new ArrayList<String>();
				Collections.addAll(titulos,"TIENDA", "MESINV", "AÑOINV", "CODCAT", "CODDEP", "CODLIN", 
						"SECCIO", "CODPRO", "CONPRE", "MESENT", "AÑOENT", "NOMPRO",  "REFPRO",  
						"DESMARCA", "COEMPRE", "DENOMI", "TIPINV", "CONCEP", "ORIPRE", "CANT_INICI", 
						"MTOS_INICI", "CANT_COMPR", "MTOS_COMPR", "CANT_VENTA", "MTOS_VENTA", "CANT_OTROS", 
						"MTOS_OTROS", "CANT_INVF", "MTOS_INVF", "CANT_AJUST", "MTOS_AJUST", "CARACT_PPAL", 
						"CANT_APART", "MARGEN", "GASTOS_PRO", "CARACT1", "CARACT2", "CARACT3");
				// Columnas en el XLS a redimensionar (numColumna, tamaño)
				List<Integer> sizes = new ArrayList<Integer>();
				Collections.addAll(sizes, 
										11, 40, //NOMPRO
										13, 40, //DESMARCA
										15, 40, //DENOMI
										24, 15, //MTOS_VENTA
										25, 15, //CANT_OTROS
										26, 15, //MTOS_OTROS
										27, 15, //CANT_INVF
										28, 15, //MTOS_INVF
										29, 15, //CANT_AJUST
										30, 15, //MTOS_AJUST
										31, 30, //CARACT_PPAL
										32, 15, //CANT_APART
										34, 15); // GASTOS_PRO

				// Crear archivo
				crearXLS(rs, nombre, "Inventario", titulos, sizes);
				//crearXLSX(rs, nombre, "Inventario", titulos, sizes);
				//crearCSV(rs, nombre, titulos);
				//crearXLSX(path, rs);
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} catch (SQLException e) {			
			//log.log(Level.SEVERE, sql, e);
			e.printStackTrace();
			return false;
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return true;
	}
	

	/**
	Exporta la información del inventario contenida en la tabla XX_VMR_SALESFILE en un archivo CSV.
	 * @param path El directorio donde almacenar el archivo.
	 */
	protected static boolean exportSalesTable(String path) {

		String sql = "\n SELECT " +
				"\n      xx_warehouseBecoNumber, TO_CHAR(xx_vmr_salesdate,'DD/MM/YY'), " +
				"\n      TO_CHAR(xx_vmr_salesdate,'FMDAY')," +
				"\n      xx_categoryCode, xx_departmentCode, xx_lineCode, xx_sectionCode," +
				"\n      xx_productCode, xx_priceconsecutive," +
				"\n      xx_product_name, XX_VendorProdRef, " +
				"\n      xx_bPartnerCode, XX_BPartnerName," +
				"\n      xx_salesreg, xx_salesquantity," +
				"\n      xx_promotionExpenses, xx_promotionQty, xx_brand_name" +
				"\n    FROM xx_vmr_salesfile " +
				"\n order by xx_categoryCode, xx_departmentCode, xx_lineCode, xx_sectionCode," +
				"\n 	xx_warehouseBecoNumber, TO_CHAR(xx_vmr_salesdate,'DD/MM/YY'), xx_productCode, xx_priceconsecutive" ;
		
		//System.out.println(sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			try {
				System.out.println("\nExportando archivo de ventas... ");
				// Cabeceras del XLS
				List<String> titulos = new ArrayList<String>();
				Collections.addAll(titulos,"TIENDA", "FECHA", "DIA_SEMANA", "CODCAT", "CODDEP", "CODLIN", 
						"SECCIO", "CODPRO", "CONPRE", "NOMPRO",  "REFPRO", "COEMPE", "DENOMI", "VTA_REG",  
						"CANT_VTA", "PROMOCION", "CANT_PROM", "DESMARCA");
				// Columnas en el XLS a redimensionar (numColumna, tamaño)
				List<Integer> sizes = new ArrayList<Integer>();
				Collections.addAll(sizes, 
										2, 20, //DIA_SEMANA
										9, 40, //NOMPRO
										10, 30, //REFPRO
										12, 40, //DENOMI
										17, 40); //DESMARCA
				
				// Crear archivo excel
				Calendar cal = java.util.Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -1); //Dia anterior
				String mesActual = formatter.format(cal.get(Calendar.MONTH)+1);
				String diaActual = formatter.format(cal.get(Calendar.DAY_OF_MONTH));
				
				crearXLS(rs, path + "Ventas " +  diaActual + "-" + mesActual , "vent02 ", titulos, sizes);
				//crearXLSX(rs, path + "Ventas " +  diaActual + "-" + mesActual , "vent02 ", titulos, sizes);
				//crearCSV(rs, nombre, titulos);
				//crearXLSX(path, rs);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} catch (SQLException e) {			
			//log.log(Level.SEVERE, sql, e);
			e.printStackTrace();
			return false;
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return true;
		
	}
	
	
	/**
	 * Exporta el contenido dado por ResultSet en el archivo CSV de nombre señalado.
	 * @param rs Datos a exportar.
	 * @param nombre Nombre del archivo.
	 * @param titulos Cabecera de la tabla.
	 * @return True si se creó el archivo, false en caso contrario.
	 * @throws SQLException
	 */
//	private static boolean crearCSV(ResultSet rs, String nombre, List<String> titulos) throws SQLException {
//		BufferedWriter bw1;
//		String nombre2 = nombre;
//		//Cabecera
//		try {
//			File file = new File(nombre +".csv");
//			int i = 1; 
//			while (!file.createNewFile()) {
//				nombre2 = nombre+"("+ ++i +")";
//				file = new File(nombre2 +".csv");
//				System.out.println("No se pudo reescribir el archivo. "+i);
//			}
//			nombre = nombre2;
//			FileWriter fstream = new FileWriter(nombre + ".csv");
//			bw1 = new BufferedWriter(fstream);
//			
//			for (int li=0;li<titulos.size();li++) {
//				bw1.write(titulos.get(li)+";");
//			}
//			
//			bw1.write("\r\n");
//			bw1.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//		
//		//Contenido
//		int count=1;
//		while (rs.next()) {
//			try {
//				bw1 = new BufferedWriter(new FileWriter(nombre + ".csv", true)); // Concatenar
//				for (int c = 1; c<=titulos.size(); c++) {
//					if (rs.getString(c)==null) {
//						bw1.write(";");
//					} else {
//						bw1.write(rs.getString(c)+";");
//					}
//				}
//				bw1.write("\r\n");
//				//System.out.println(count);
//				count++;
//				bw1.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//		return true;
//		
//	}
	
	
	
	// Crea XLSX... desborda la memoria de java con archivos demasiado grandes
//	public static void crearXLSX(ResultSet rs, String nombreArchivo, String nombreTab, 
//							List<String> titulos, List<Integer> sizes) throws Exception {
//				
//		XSSFWorkbook wb = new XSSFWorkbook();
//		XSSFSheet sheet = wb.createSheet(nombreTab);
//				
//		// row numbering starts from 0
//		Row row = sheet.createRow(0);
//		for (int li=0;li<titulos.size();li++) {
//			Cell cell = row.createCell(li);
//			cell.setCellValue(titulos.get(li));
//		}
//		
//		int fila = 1; 
//		try {
//			while (rs.next()) {
//				row = sheet.createRow(fila++);
//				for (int c = 0; c<titulos.size(); c++) {
//					Cell cell = row.createCell(c);
//					Object obj = rs.getObject(c+1);
//					if (obj == null) {
//						cell.setCellValue("");
//					}
//					else if (obj instanceof java.lang.Number) {					
//						cell.setCellValue(rs.getDouble(c+1)); 
//					}
//					else {
//						cell.setCellValue(obj.toString());
//					}
//				}
//				//System.out.println(fila);
//			}
//			
//			String nombre2 = nombreArchivo;
//			File ff = new File(nombreArchivo +".xlsx");
//			FileOutputStream foss;
//			int i = 1; 
//			try {
//				foss = new FileOutputStream(ff);
//			} catch (Exception e) {
//				e.printStackTrace();
//				while (ff.exists()) {
//					nombre2 = nombreArchivo+" ("+ ++i +")";
//					ff = new File(nombre2 +".xlsx");
//					System.out.println("No se pudo reescribir el archivo. "+i);
//				}
//				foss = new FileOutputStream(ff);
//			}
//			
//			wb.write(foss);
//			foss.flush();
//			foss.close();
//			System.out.println("Archivo creado en: "+nombreArchivo);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		///*************************
		
			
		/// OTRA
// 		int fila = 1; 
// 		try {
// 			while (rs.next()) {
// 				row = sheet.createRow(fila);
// 				for (int c = 0; c<38; c++) {
// 					Cell cell = row.createCell(c);
// 					Object obj = rs.getObject(c+1);
// 					if (obj == null) {
// 						cell.setCellValue("");
// 					}
// 					else if (obj instanceof java.lang.Number) {					
// 						cell.setCellValue(rs.getDouble(c+1)); 
// 					}
// 					else {
// 						cell.setCellValue(obj.toString());
// 					}
// 				}
// 				System.out.println(fila);
// 				fila++;
// 				fos = new FileOutputStream(excelFileName, append);
// 				wb.write(fos);
// 				fos.flush();
// 				fos.close();
// 				append = true;
// 			}
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 		}
		// ******************* fin otra
		
//	}
	

	/** Crear un archivo xls
	 * Se crea el archivo con el contenido del reporte de acuerdo a los valores
	 * obtenidos previamente
	 * @param rs Tabla a exportar.
	 * @param nombreArchivo Nombre del archivo a generar.
	 * @param titulos Cabecera de la tabla.
	 * @param sizes Tamaños de las columnas a modificar.
	 */
	public static void crearXLS (ResultSet rs, String nombreArchivo, String nombreTab, 
								List<String> titulos, List<Integer> sizes) throws Exception {
		String nombre2 = nombreArchivo;
		File archivo = new File(nombreArchivo +".xls");
		int i = 1; 
		
		WritableWorkbook workbook = null;
		
		try {
			workbook = Workbook.createWorkbook(archivo);
		} catch (Exception e) {
			e.printStackTrace();
			while (archivo.exists()) {
				nombre2 = nombreArchivo+" ("+ ++i +")";
				archivo = new File(nombre2 +".xls");
				System.out.println("No se pudo reescribir el archivo. "+i);
			}
			workbook = Workbook.createWorkbook(archivo);
		}
		System.out.println("Archivo creado en: "+nombreArchivo);
		WritableSheet s = workbook.createSheet(nombreTab,0);
		
		// Definición del formato de las celdas cabecera 
		WritableFont boldf = new WritableFont(WritableFont.ARIAL,10, WritableFont.BOLD);
		boldf.setColour(Colour.BLACK);
		WritableCellFormat cf1 = new WritableCellFormat(boldf);
		cf1.setBackground(Colour.GRAY_25);
		//cf1.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
		cf1.setWrap(false);
		cf1.setAlignment(Alignment.CENTRE);
		
		// Definición del formato de las celdas de contenido
		WritableFont noboldf = new WritableFont(WritableFont.ARIAL,10, WritableFont.NO_BOLD);
		WritableCellFormat cf2 = new WritableCellFormat(noboldf);
		//cf2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		cf2.setWrap(false);
		
		// Escribir el archivo
		int fila = 1;
		int row = -1;int tab = 2;
		
		while (rs.next() & row <=200000) {
			if (row % 65535 == 65535 - 1) { // Nuevo tab
				s = workbook.createSheet(nombreTab + tab,tab++);
				fila = 1; 
			}
			
			if (fila == 1 || row % 65535 == 65535 - 1) { // Cabecera del nuevo tab
				// Cabecera
				for (int li=0;li<titulos.size();li++) {
					s.addCell(new Label(li, 0, titulos.get(li), cf1));
				}
				// Redimensión de algunas columnas
				for (int li=0;li<sizes.size();li++) {
					s.setColumnView(sizes.get(li++), sizes.get(li));
				}
			}
			
			//Contenido
			for (int c = 0; c<titulos.size(); c++) {
				Object obj = rs.getObject(c+1);
				if (obj == null) {
					s.addCell(new Label(c, fila, "", cf2));
				}
				else if (obj instanceof java.lang.Number) {					
					s.addCell(new Number(c, fila, rs.getDouble(c+1), cf2)); 
				}
				else {
					if (obj.toString().startsWith("'")) {
						s.addCell(new Label(c, fila, obj.toString().substring(1), cf2));
					} else {
						s.addCell(new Label(c, fila, obj.toString(), cf2));
					}
				}
			}
			
			fila++;
			row++;
			
		} // end while

		workbook.write();
		workbook.close();

	} // Fin crearXLS
	
}