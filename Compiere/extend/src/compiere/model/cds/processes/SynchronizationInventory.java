package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;

import org.compiere.model.MClient;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;

import compiere.model.cds.X_E_XX_VCN_INVD53;
import compiere.model.cds.X_Ref_XX_MovementType;
import compiere.model.cds.X_Ref_XX_OrderRequestType;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VCN_Inventory;
import compiere.model.cds.X_XX_VCN_InventoryMovDetail;

/**
 * Sincronización de Inventario
 * @author Gabrielle Huchet.
 *
 */

public class SynchronizationInventory extends SvrProcess {

	private Integer Warehouse_CDBoleita = Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"); 
	private Integer DocTypeTraspaso = Env.getCtx().getContextAsInt("#XX_L_DOCTYPETRANSFER_ID"); 
	private Integer DocTypeDevolucion = Env.getCtx().getContextAsInt("#XX_L_DOCTYPERETURN_ID"); 
	private Integer DocTypeMovEntreCD = 1000335;
	private Integer PriceConsecutive_CD =0;
	private Date ini = Calendar.getInstance().getTime();
	private Date end =Calendar.getInstance().getTime();
	private int UbicacionTienda = 3;
	private int UbicacionTransito = 2;
	private int InitialFiscalYearMonth = 7;
	
//	/** ID del Proceso XX_VMR_ExportSalesAssortment de GMARQUES*/ 
//	private Integer IDPROCESO1 = Env.getCtx().getContextAsInt("#XX_L_PROCESSEXPORTSALESASSO_ID");
	
	protected String doIt() throws Exception {
		
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, -1);
		int month = date.get(Calendar.MONTH) + 1;
		int monthPrevious =  0,  yearPrevious = 0; 
		int year = date.get(Calendar.YEAR);

		//month =8; 
		//year = 2013;
		
		date =  Calendar.getInstance();
		System.out.println("\nInicial inventario mes/año "+month+"/"+year+": "+date.getTime());

		/*Antes de iniciar el proceso, el campo XX_Synchronizer se coloca en falso para todos los registros del mes a calcular */
		updateSynchronized("N", month, year);

		try {	
			//Si no se ha inicializado previamente el mes, se creara el inventario inicial
			if (!isInitialized(month, year)) {	
				System.out.println("NO Inicializado anteriormente");
				if (month == 1) {
					monthPrevious = 12;
					yearPrevious = year - 1;				
				} else {
					monthPrevious = month - 1;
					yearPrevious = year;
				}
				//Se eliminan los registros del mes a calcular en caso de que existan
				borrarInventarioMensual(month, year);
				borrarDetalleInvMensual(month, year);
				
				//Se crea el inventario inicial 
				if(!inicializarInventario(monthPrevious, yearPrevious, month, year)){
					borrarInventarioMensual(month, year);
					//Se envía correo indicando que el proceso no se pudo completar
					sendMailCompleteProcess(false, "Error al Inicializar Inventario", month, year);
					return "No se Pudo Completar Sincronización de Inventario Periodo "+month+"/"+year;	
				}
				System.out.println("\nInicializado Inventario mes "+month+" año "+year+" - "+Calendar.getInstance().getTime());
			}else {
				System.out.println("Inicializado anteriormente");
				/*Limpiar el Inventario (excepto los campos XX_INITIALINVENTORYQUANTITY y XX_INITIALINVENTORYAMOUNT) 
				 * y detalle de movimientos para que se re-calcule el inventario acumulado del mes */
				borrarInventarioAcumulado(month, year);
				borrarDetalleInvMensual(month, year);
				limpiarInventario(month, year);
				limpiarDetalleInventario(month, year);
			}
			 /*Compras Despacho Directo */
			ComprasDespachoDirecto(year, month);
		    System.out.println("\nListo Compras Despacho Directo : "+Calendar.getInstance().getTime());
			
			
			/*Compras */
			Compras(year, month);
		    System.out.println("\nListo Compras: "+Calendar.getInstance().getTime());
		    
		   
		    /*Traspasos Entrantes  */
			TraspasosEntrantes(year, month);
			System.out.println("\nListo Traspasos Entrantes: "+Calendar.getInstance().getTime());
			
			/*Traspasos Salientes */
			TraspasosSalientes(year, month);
			System.out.println("\nListo Traspasos Salientes: "+Calendar.getInstance().getTime());
	
		    /*Traspasos Entre CD Entrantes  */
			TraspasosEntreCDEntrantes(year, month);
			System.out.println("\nListo Traspasos entre CD Entrantes: "+Calendar.getInstance().getTime());
			
			/*Traspasos Entre CD Salientes */
			TraspasosEntreCDSalientes(year, month);
			System.out.println("\nListo Traspasos entre CD Salientes: "+Calendar.getInstance().getTime());
	
			/*Rebajas Definitivas*/
			RebajasDefinitivasMontoRebajado(year, month);
			System.out.println("\nListo Rebajas Definitivas - monto rebajado: "+Calendar.getInstance().getTime());

			RebajasDefinitivasMontoNuevo(year, month);
			System.out.println("\nListo Rebajas Definitivas - monto nuevo: "+Calendar.getInstance().getTime());

			/*Rebajas Definitivas Reversadas*/
			RebajasDefinitivasMontoRebajadoRV(year, month);
			System.out.println("\nListo Reverso de Rebajas Definitivas - monto rebajado: "+Calendar.getInstance().getTime());

			RebajasDefinitivasMontoNuevoRV(year, month);
			System.out.println("\nListo Reverso Rebajas Definitivas - monto nuevo: "+Calendar.getInstance().getTime());
			
			/*Devoluciones  */
			Devoluciones(year, month);
			System.out.println("\nListo Devoluciones: "+Calendar.getInstance().getTime());
			
		    /*Ventas de las Tiendas*/
		    Ventas(year, month);
			System.out.println("\nListo Ventas y Rebajas Promocionales: "+Calendar.getInstance().getTime());
			
		    /*Pedidos Entrantes */
			PedidosEntrantes(year, month);
			System.out.println("\nListo Pedidos Entrantes: "+Calendar.getInstance().getTime());
			
		    /*Pedidos Salientes*/
			PedidosSalientesDetalle(year, month);
			System.out.println("\nListo Pedidos Salientes - Detalle: "+Calendar.getInstance().getTime());	
			
			PedidosSalientesAjusteCompra(year, month);
			System.out.println("\nListo Pedidos Salientes - Ajuste Compra: "+Calendar.getInstance().getTime());			

			
			/* Calcular Costos Faltantes */
			calcularCostosFaltantes(month, year);
			System.out.println("\nListo Costos Faltantes: "+Calendar.getInstance().getTime());

			//Se borran los datos de la tabla E_XX_INVD53C de Compiere
			deleteInv(month,year);
			
			//Se llena la tabla E_XX_INVD53C de Compiere con los datos de XX_VCN_Inventory
			fillXX_VCN_Inventory(month,year);
			
			//Actualiza la ubicación de aquellos productos que tengan pedidos de despacho directo en Transito
			updateDDPlacedOrders(month,year);

		} catch (Exception e) {
			e.printStackTrace();
			//Se envía correo indicando que el proceso no se pudo completar
			sendMailCompleteProcess(false, e.getMessage()+"\n"+e.getStackTrace()+"\n"+e.getCause(), month, year);
			return "No se Pudo Completar Sincronización de Inventario Periodo "+month+"/"+year;		
		}
		
		
		end = Calendar.getInstance().getTime();
		
		//Se envía correo indicando que se completo el proceso
		sendMailCompleteProcess(true, null, month, year);
		
		//Al completar el proceso el campo XX_Synchronizer se coloca en true para todos los registros del mes a calcular
		updateSynchronized("Y", month, year);
		
		System.out.println("\nFinal inventario mes/año"+month+"/"+year+": "+Calendar.getInstance().getTime());
		
//		 /*
//		 * LLama Proceso que sincroniza los totales en Compiere correspondientes a las ventas del día anterior 
//		 * y el acumulado del mes hasta el día anterior, con la tabla XX_VMR_SalesAssortment y elimina los valores
//		 * registrados el día anterior a ese. 
//		 * 
//		 * @author Gabriela Marques.
//		 */
//		try{
//			callProcess(IDPROCESO1);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		return "Se Completo con Éxito Sincronización de Inventario Periodo "+month+"/"+year;
	}

	//Indica si se ha inicializado previamente la tabla para el mes a calcular
	private boolean isInitialized(int month, int year) {
		String sql = "\nSELECT count(*) FROM XX_VCN_INVENTORY " +
				"\nWHERE XX_INVENTORYYEAR = " + year + " AND XX_INVENTORYMONTH = " + month+
				"\nAND (XX_INITIALINVENTORYQUANTITY != 0 OR XX_INITIALINVENTORYAMOUNT !=0) ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//System.out.println(""+sql);
		boolean initialized = false;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if(rs.getInt(1)>0) {
					initialized = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return initialized;
	}

	//Cambia el estado de la sincronización a 'Y' o 'N' dependiendo de si se completo el proceso de sincronización o no.
	private void updateSynchronized(String sync, int month, int year) throws Exception{
		
		String sql =  "update XX_VCN_Inventory set XX_SYNCHRONIZED = '"+sync+"' where XX_INVENTORYMONTH = "+month+" and XX_INVENTORYYEAR = "+year+"";

		try {
			DB.executeUpdate(get_TrxName(),sql);
			commit();
		
		} catch (Exception e) {	
			throw new Exception("Error Actualizando la Columna XX_SYNCHRONIZED de la tabla VCN Inventory: " +e.getMessage());
		}
		
		String sql2 = "update XX_VCN_InventoryMovDetail set XX_SYNCHRONIZED = '"+sync+"' where XX_INVENTORYMONTH = "+month+" and XX_INVENTORYYEAR = "+year+"";
		try {
			DB.executeUpdate(get_TrxName(),sql2);
			commit();
		
		} catch (Exception e) {	
			throw new Exception("Error Actualizando la Columna XX_SYNCHRONIZED de la tabla VCN Inventory: " +e.getMessage());
		}
		
	}

	private void sendMailCompleteProcess(boolean complete, String error, int month, int year) {
		
		end = Calendar.getInstance().getTime();
		String emailTo = "ghuchet@beco.com.ve";
		String subject = "", msg = "";
		if(complete){
			subject = "Sincronización de Inventario Completada";		
			msg = "Se Completo la sincronización de Inventario para mes-año: "+month+"-"+year+"."+
				"\nInicio Proceso:"+ini+ 
				"\nFin Proceso: "+end ;
		}
		else {
			subject = "Sincronización de Inventario Falló";		
			msg = "No se pudo completar la sincronización de Inventario para mes-año: "+month+"-"+year+"." +
				"\n" +error+
				"\nInicio Proceso:"+ini+ 
				"\nFin Proceso: "+end ;
		}
		if(emailTo.contains("@")){
			MClient m_client = MClient.get(Env.getCtx());
				
			EMail email = m_client.createEMail(null, emailTo, " ", subject, msg);
			
			if (email != null){		
				String status = email.send();
				log.info("Email Send status: " + status);
				if (email.isSentOK()){}
			}
		}
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}
	
	/** Inserta un registro en la generacion del iventario inicial */
	public boolean InsertarVcnInventory(X_XX_VCN_Inventory invenBecoAnt, int month, int year, 
			BigDecimal inventarioInicialPz, BigDecimal inventarioInicialPVP) throws Exception{
		
		X_XX_VCN_Inventory invenBeco = new X_XX_VCN_Inventory(getCtx(), 0, get_TrxName());
		invenBeco.setM_Product_ID(invenBecoAnt.getM_Product_ID());
		invenBeco.setXX_VMR_Category_ID(invenBecoAnt.getXX_VMR_Category_ID());
		invenBeco.setXX_VMR_Department_ID(invenBecoAnt.getXX_VMR_Department_ID());
		invenBeco.setXX_VMR_Line_ID(invenBecoAnt.getXX_VMR_Line_ID());
		invenBeco.setXX_VMR_Section_ID(invenBecoAnt.getXX_VMR_Section_ID());
		invenBeco.setM_Warehouse_ID(invenBecoAnt.getM_Warehouse_ID());
		invenBeco.setXX_INVENTORYYEAR(new BigDecimal(year));
		invenBeco.setXX_INVENTORYMONTH(new BigDecimal(month));
		invenBeco.setM_AttributeSetInstance_ID(invenBecoAnt.getM_AttributeSetInstance_ID());
		invenBeco.setXX_ConsecutivePrice(invenBecoAnt.getXX_ConsecutivePrice()); 		
		invenBeco.setXX_INITIALINVENTORYAMOUNT(inventarioInicialPVP);
		invenBeco.setXX_INITIALINVENTORYQUANTITY(inventarioInicialPz);
//		invenBeco.setXX_PREVIOUSADJUSTMENTSAMOUNT(invenBecoAnt.getXX_AdjustmentsAmount());
//		invenBeco.setXX_PREVIOUSADJUSTMENTSQUANTITY(invenBecoAnt.getXX_ADJUSTMENTSQUANTITY());
		invenBeco.setXX_InitialInventoryCostPrice(invenBecoAnt.getXX_InitialInventoryCostPrice());
		
		if (!(invenBeco.save())) {
			return false;
		} else {
			return true;
		}
	}


	/* Actualizamos en los registros las columnas de cantidad y monto de movimiento en 0 para que se re-calcule todos los días. */
	public void limpiarDetalleInventario(int month, int year) throws Exception{

		String sql =  "update XX_VCN_InventoryMovDetail set XX_MOVEMENTQUANTITY = 0,XX_MOVEMENTAMOUNT = 0 " +
				" where XX_INVENTORYMONTH = "+month+" and XX_INVENTORYYEAR = "+year+"";
		try {
			DB.executeUpdate(get_TrxName(),sql);
			commit();
		
		} catch (Exception e) {	
			throw new Exception("Error Actualizando las Columnas de la tabla VCN Inventory: " +e.getMessage());
		}
	}
	
	/* Actualizamos todas las columnas de los Registros en 0 menos el inventario Inicial que fue calculado al principio del mes
	  para que se re-calcule todos los días. */
	public void limpiarInventario(int month, int year) throws Exception{

		String sql =  "update XX_VCN_Inventory set XX_SHOPPINGQUANTITY = 0,XX_SHOPPINGAMOUNT = 0,XX_SALESQUANTITY = 0," +
			 "XX_SALESAMOUNT = 0,XX_MOVEMENTQUANTITY = 0,XX_MOVEMENTAMOUNT = 0,XX_ADJUSTMENTSQUANTITY = 0," +
			 "XX_ADJUSTMENTSAMOUNT = 0 where XX_INVENTORYMONTH = "+month+" and XX_INVENTORYYEAR = "+year+"";
		try {
			DB.executeUpdate(get_TrxName(),sql);
			commit();
		
		} catch (Exception e) {	
			throw new Exception("Error Actualizando las Columnas de la tabla VCN Inventory: " +e.getMessage());
		}
	}
	
	/** Crea un inventario inicial */
	public boolean inicializarInventario (int monthPrevious, int yearPrevious, int month, int year) throws Exception {
		//Selecciona todas las transacciones del mes pasado del inventario para crearlas en el mes actual con su inventario inicial
		String sql = " SELECT * FROM XX_VCN_INVENTORY WHERE XX_INVENTORYYEAR = " + yearPrevious + " AND XX_INVENTORYMONTH = " + monthPrevious ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//System.out.println(sql);
		boolean correcto = true;
		BigDecimal inventarioInicialPVP = new BigDecimal(0), inventarioInicialPz = new BigDecimal(0);
		X_XX_VCN_Inventory invenBecoMesAnt = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next() && correcto) {
				invenBecoMesAnt= new X_XX_VCN_Inventory(getCtx(), rs, null);
				inventarioInicialPz = invenBecoMesAnt.getXX_INITIALINVENTORYQUANTITY().add(invenBecoMesAnt.getXX_SHOPPINGQUANTITY()
						.subtract(invenBecoMesAnt.getXX_SALESQUANTITY()).add(invenBecoMesAnt.getXX_MOVEMENTQUANTITY()))
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				inventarioInicialPVP = invenBecoMesAnt.getXX_INITIALINVENTORYAMOUNT().add(invenBecoMesAnt.getXX_SHOPPINGAMOUNT()
						.subtract(invenBecoMesAnt.getXX_SALESAMOUNT()).add(invenBecoMesAnt.getXX_MOVEMENTAMOUNT()))
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				
				if(month == InitialFiscalYearMonth){
					inventarioInicialPz = 	inventarioInicialPz.add(invenBecoMesAnt.getXX_ADJUSTMENTSQUANTITY());
					inventarioInicialPVP = inventarioInicialPVP.add(invenBecoMesAnt.getXX_AdjustmentsAmount());
				}
				
				
				//Si las piezas restantes de la linea de inventario del mes anterior es diferente a 0 lo agrego al mes actual
				//Insertar en el nuevo mes todos los productos del inventario
				if(inventarioInicialPz != new BigDecimal(0) || inventarioInicialPVP != new BigDecimal(0))
					correcto = InsertarVcnInventory(invenBecoMesAnt, month, year, inventarioInicialPz, inventarioInicialPVP);
			}

			if (!correcto) {				
				rollback();
				ADialog.error(0, new Container(), "DatabaseError");
			}else {
				commit();
			}
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
			 throw new Exception("Error inicializando inventario"+	e.getMessage());
		}finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return correcto;
	}
	
	/*MODIFICADO CD VALENCIA*/
	/** Agregar las compras 
	 * @throws Exception */
	private void Compras (int year, int month) throws Exception {
	
		 /* Buscar Compras a CD y tiendas */		
		 String sqlCompras = "\nSELECT O.M_WAREHOUSE_ID M_WAREHOUSE_ID, "+
		 		"\nDECODE(INV.XX_VCN_INVENTORY_ID,NULL,0,INV.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORY_ID, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID, IOL.M_PRODUCT_ID," +
		 		"\nDECODE(IOL.M_ATTRIBUTESETINSTANCE_ID,NULL,0,IOL.M_ATTRIBUTESETINSTANCE_ID) M_ATTRIBUTESETINSTANCE_ID, " +
		 		"\nSUM(IOL.MOVEMENTQTY) CANTIDAD, " +
		 		"\nSUM(CASE WHEN ASI.XX_SALEPRICE IS NULL THEN 0.01*IOL.MOVEMENTQTY  " +
		 		"\nELSE ASI.XX_SALEPRICE*IOL.MOVEMENTQTY END) MONTO, " +
		 		"\n(CASE WHEN ASI.PRICEACTUAL IS NULL THEN  (0.01)  " +
		 		"\nELSE (ASI.PRICEACTUAL) END) COSTO " +
		 		"\nFROM M_INOUT IO " +
		 		"\nJOIN C_ORDER O ON (IO.C_ORDER_ID = O.C_ORDER_ID) " +
		 		"\nJOIN M_INOUTLINE IOL ON (IO.M_INOUT_ID = IOL.M_INOUT_ID) " +
		 		"\nJOIN M_PRODUCT  P ON (P.M_PRODUCT_ID = IOL.M_PRODUCT_ID) " +
		 		"\nJOIN  C_ORDERLINE LI ON (LI.C_ORDERLINE_ID = IOL.C_ORDERLINE_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = IOL.M_ATTRIBUTESETINSTANCE_ID) " +
				"\nLEFT JOIN XX_VCN_Inventory INV ON (INV.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INV.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INV.XX_CONSECUTIVEPRICE = " + PriceConsecutive_CD +
				"\nAND  O.M_WAREHOUSE_ID = INV.M_WAREHOUSE_ID AND INV.XX_INVENTORYMONTH = "+ month +" AND INV.XX_INVENTORYYEAR = "+ year +") " +
		 		"\nWHERE O.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
		 		"\nAND TO_CHAR(O.XX_CHECKUPDATE, 'YYYY') = " + year + "	AND TO_CHAR(O.XX_CHECKUPDATE,'MM') = " + month+
		 		"\nAND IOL.MOVEMENTQTY > 0 AND O.ISSOTRX = 'N' AND IO.ISSOTRX = 'N' and O.XX_POTYPE = 'POM' and  O.XX_VLO_TYPEDELIVERY != 'DD' " +
		 		"\nGROUP BY INV.XX_VCN_INVENTORY_ID, O.M_WAREHOUSE_ID, P.XX_VMR_CATEGORY_ID,P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID, " +
		 		"\nIOL.M_PRODUCT_ID, IOL.M_ATTRIBUTESETINSTANCE_ID, ASI.PRICEACTUAL";
		 
		 PreparedStatement psCompras = null;
		 ResultSet rsCompras = null;	
		 int registroInv;
		
		try {
			 System.out.println("\nCompras: "+sqlCompras);
			 psCompras = DB.prepareStatement(sqlCompras, null);
			 rsCompras = psCompras.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 while (rsCompras.next() ) {
					registroInv = rsCompras.getInt("XX_VCN_INVENTORY_ID");
//					if(registroInv<1){
//						registroInv = existeRegistro(rsCompras.getInt("M_PRODUCT_ID"), 
//								rsCompras.getInt("M_ATTRIBUTESETINSTANCE_ID"), 
//								PriceConsecutive_CD, Warehouse_CD, month ,year);
//					}
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto de Compra
						lineaInventario.setXX_SHOPPINGAMOUNT(lineaInventario.getXX_SHOPPINGAMOUNT().
								add(rsCompras.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Compra
						lineaInventario.setXX_SHOPPINGQUANTITY(lineaInventario.getXX_SHOPPINGQUANTITY().
								add(rsCompras.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();
					 }else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsCompras.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsCompras.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsCompras.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsCompras.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsCompras.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsCompras.getInt("M_WAREHOUSE_ID"));
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsCompras.getInt("M_AttributeSetInstance_ID"));
						 lineaInventario.setXX_ConsecutivePrice(new BigDecimal(PriceConsecutive_CD)); 
						 lineaInventario.setXX_SHOPPINGQUANTITY(rsCompras.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_SHOPPINGAMOUNT(rsCompras.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsCompras.getBigDecimal("COSTO").setScale(2, RoundingMode.HALF_EVEN));
						 
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro de  compras " + lineaInventario);
						 }
					}
			}
		}catch (Exception e) {
			 throw new Exception("Error en ajuste de compras "+	e.getMessage());
		}finally {
			rsCompras.close();
			psCompras.close();	 
		 }
	}
	
	/** Agregar las compras de despacho directo
	 * @throws Exception */
	private void ComprasDespachoDirecto(int year, int month) throws Exception {
	
		 /* Buscar Compras a CD y tiendas */		
		 String sqlCompras = "\nSELECT O.M_WAREHOUSE_ID M_WAREHOUSE_ID, "+
			 		"\nDECODE(INV.XX_VCN_INVENTORY_ID,NULL,0,INV.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORY_ID, " +
			 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID, P.M_PRODUCT_ID," +
					"\nDECODE(ASI.M_ATTRIBUTESETINSTANCE_ID,NULL,0,ASI.M_ATTRIBUTESETINSTANCE_ID) M_ATTRIBUTESETINSTANCE_ID, " +
			 		"\nDECODE(DE.XX_PRICECONSECUTIVE,NULL,0,DE.XX_PRICECONSECUTIVE) CONSECUTIVOPRECIO, " +
			 		"\nSUM(DE.XX_PRODUCTQUANTITY) CANTIDAD, " +
			 		"\nSUM(CASE WHEN DE.XX_SALEPRICE IS NULL THEN 0.01*DE.XX_PRODUCTQUANTITY  " +
			 		"\nELSE DE.XX_SALEPRICE*DE.XX_PRODUCTQUANTITY END) MONTO, " +
			 		"\n(CASE WHEN ASI.PRICEACTUAL IS NULL THEN  (0.01)  " +
			 		"\nELSE (ASI.PRICEACTUAL) END) COSTO " +
			 		"\nFROM C_ORDER O " +
			 		"\nJOIN XX_VMR_ORDER PE ON (PE.C_ORDER_ID = O.C_ORDER_ID)" +
			 		"\nJOIN XX_VMR_ORDERREQUESTDETAIL DE ON (DE.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID) " +
			 		"\nJOIN M_PRODUCT  P ON (P.M_PRODUCT_ID = DE.M_PRODUCT_ID) " +
			 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = DE.XX_PRODUCTBATCH_ID) " +
					"\nLEFT JOIN XX_VCN_Inventory INV ON (INV.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
					"\nNVL(INV.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INV.XX_CONSECUTIVEPRICE = DE.XX_PRICECONSECUTIVE " +
					"\nAND  O.M_WAREHOUSE_ID = INV.M_WAREHOUSE_ID AND INV.XX_INVENTORYMONTH = "+ month +" AND INV.XX_INVENTORYYEAR = "+ year +") " +
			 		"\nWHERE O.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
			 		"\nAND TO_CHAR(O.XX_CHECKUPDATE, 'YYYY') = " + year + "	AND TO_CHAR(O.XX_CHECKUPDATE,'MM') = " + month+
			 		"\nAND  DE.XX_PRODUCTQUANTITY > 0  AND O.ISSOTRX = 'N' and O.XX_POTYPE = 'POM' and  O.XX_VLO_TYPEDELIVERY = 'DD' " +
			 		"\nGROUP BY INV.XX_VCN_INVENTORY_ID, O.M_WAREHOUSE_ID, P.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID, " +
			 		"\nDE.XX_PRICECONSECUTIVE, P.M_PRODUCT_ID, ASI.M_ATTRIBUTESETINSTANCE_ID, ASI.PRICEACTUAL";
		 
		 PreparedStatement psCompras = null;
		 ResultSet rsCompras = null;	
		 int registroInv;
		
		try {
			 System.out.println("\nCompras Despacho Directo: "+sqlCompras);
			 psCompras = DB.prepareStatement(sqlCompras, null);
			 rsCompras = psCompras.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 while (rsCompras.next() ) {
					registroInv = rsCompras.getInt("XX_VCN_INVENTORY_ID");
//					if(registroInv<1){
//						registroInv = existeRegistro(rsCompras.getInt("M_PRODUCT_ID"), 
//								rsCompras.getInt("M_ATTRIBUTESETINSTANCE_ID"), 
//								rsCompras.getBigDecimal("CONSECUTIVOPRECIO") Warehouse_CD, month ,year);
//					}
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto de Compra Despacho Directo
						lineaInventario.setXX_SHOPPINGAMOUNT(lineaInventario.getXX_SHOPPINGAMOUNT().
								add(rsCompras.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Compra Despacho Directo
						lineaInventario.setXX_SHOPPINGQUANTITY(lineaInventario.getXX_SHOPPINGQUANTITY().
								add(rsCompras.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();
					 }else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsCompras.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsCompras.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsCompras.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsCompras.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsCompras.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsCompras.getInt("M_WAREHOUSE_ID"));
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsCompras.getInt("M_AttributeSetInstance_ID"));
						 lineaInventario.setXX_ConsecutivePrice(rsCompras.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInventario.setXX_SHOPPINGQUANTITY(rsCompras.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_SHOPPINGAMOUNT(rsCompras.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsCompras.getBigDecimal("COSTO").setScale(2, RoundingMode.HALF_EVEN));
						 
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro de compras Despacho Directo" + lineaInventario);
						 }
					}
			}
		}catch (Exception e) {
			 throw new Exception("Error en ajuste de compras Despacho Directo "+	e.getMessage());
		}finally {
			rsCompras.close();
			psCompras.close();	 
		 }
	}
	
	/**Agrega ventas y sus respectivas rebajas promocionales al inventario de un mes y año específico */
	private void Ventas(int year, int month) throws Exception {
		 /* Buscar Ventas Y rebajas promocionales*/		
		 String sqlVentas = "\nSELECT DECODE(INV.XX_VCN_INVENTORY_ID,NULL,0,INV.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORY_ID, " +
				"\nDECODE(INVMOV.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOV.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVDETAIL_ID, " +
		 		"\nDECODE(OL.XX_PRICECONSECUTIVE,NULL,0,OL.XX_PRICECONSECUTIVE) XX_PRICECONSECUTIVE, OL.M_WAREHOUSE_ID, " +
		 		"\nOL.M_PRODUCT_ID, " +
		 		"\nDECODE(OL.M_ATTRIBUTESETINSTANCE_ID,NULL,0,OL.M_ATTRIBUTESETINSTANCE_ID) M_ATTRIBUTESETINSTANCE_ID, " +
		 		"\nSUM(CASE WHEN OL.PRICEACTUAL IS NOT NULL AND OL.QTYENTERED IS NOT NULL  " +
		 		"\nTHEN (OL.PRICEACTUAL+OL.XX_EMPLOYEEDISCOUNT)*OL.QTYENTERED  ELSE 0.01 END)  MONTO, " +
		 		"\nSUM(CASE WHEN OL.PRICEACTUAL IS NOT NULL AND OL.PRICELIST IS NOT NULL AND OL.QTYENTERED IS NOT NULL  " +
		 		"\nTHEN (OL.PRICEACTUAL+OL.XX_EMPLOYEEDISCOUNT-OL.PRICELIST)*OL.QTYENTERED  ELSE 0 END)  MONTOREBAJAPROMO, " +
		 		"\n(CASE WHEN ASI.PRICEACTUAL IS NOT NULL " +
		 		"\nTHEN ASI.PRICEACTUAL ELSE 0.01 END) COSTO, " +
		 		"\nSUM(CASE WHEN OL.QTYENTERED IS NOT NULL THEN OL.QTYENTERED ELSE 0 END) CANTIDAD, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_SECTION_ID, P.XX_VMR_LINE_ID " +
		 		"\nFROM C_ORDERLINE OL " +
		 		"\nJOIN C_ORDER O ON (O.C_ORDER_ID = OL.C_ORDER_ID) " +
		 		"\nJOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = OL.M_ATTRIBUTESETINSTANCE_ID) " +
				"\nJOIN M_PRODUCT P ON (P.M_PRODUCT_ID = OL.M_PRODUCT_ID) " +
				"\nLEFT JOIN XX_VCN_Inventory INV ON (INV.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INV.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) " +
				"\nAND INV.XX_CONSECUTIVEPRICE = NVL(OL.XX_PRICECONSECUTIVE,0) " +
				"\nAND INV.M_WAREHOUSE_ID = OL.M_WAREHOUSE_ID AND INV.XX_INVENTORYMONTH = "+ month +
				"\nAND INV.XX_INVENTORYYEAR = "+ year +") " +
				"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOV ON (INVMOV.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOV.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0)" +
				"\nAND INVMOV.XX_CONSECUTIVEPRICE = OL.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOV.M_WAREHOUSE_ID = OL.M_WAREHOUSE_ID AND INVMOV.XX_INVENTORYMONTH = "+ month +
				"\nAND INVMOV.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOV.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.REBAJAS_PROMOCIONALES.getValue()+") " +
		 		"\nWHERE O.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID()+
		 		"\nAND O.ISSOTRX = 'Y' AND O.DOCSTATUS = 'CO' " +
		 		"\nAND OL.QTYENTERED <> 0 "+
		 		"\nAND TO_CHAR(O.DATEORDERED,'YYYY') = " + year + 
		 		"\nAND TO_CHAR(O.DATEORDERED,'MM') = " + month+
		 	    "\nGROUP BY INV.XX_VCN_INVENTORY_ID, INVMOV.XX_VCN_INVENTORYMOVDETAIL_ID, OL.XX_PRICECONSECUTIVE, " +
		 	    "\nOL.M_WAREHOUSE_ID, OL.M_PRODUCT_ID, OL.M_ATTRIBUTESETINSTANCE_ID,  " +
		 	    "\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_SECTION_ID, P.XX_VMR_LINE_ID, ASI.PRICEACTUAL " ;
		 
		 PreparedStatement psVentas = null;
		 ResultSet rsVentas = null;	
		 int registroInv, registroInvMov;
		 try {
			System.out.println("\nVentas: "+sqlVentas);
			 psVentas = DB.prepareStatement(sqlVentas, null);
			 rsVentas = psVentas.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			 while (rsVentas.next()) {
				registroInv = rsVentas.getInt("XX_VCN_INVENTORY_ID");
//				if(registroInv<1){
//						registroInv = existeRegistro(rsVentas.getInt("M_PRODUCT_ID"),
//								rsVentas.getInt("M_ATTRIBUTESETINSTANCE_ID"),
//								PriceConsecutive_CD, Warehouse_CD, month ,year);
//				}
				if (registroInv > 0) {
					lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
						//Monto de Venta
					lineaInventario.setXX_SALESAMOUNT(lineaInventario.getXX_SALESAMOUNT().
							add(rsVentas.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
						//Cantidad de Venta
					lineaInventario.setXX_SALESQUANTITY(lineaInventario.getXX_SALESQUANTITY().
							add(rsVentas.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						//Monto de Rebaja Promocional
					lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
							add(rsVentas.getBigDecimal("MONTOREBAJAPROMO")).setScale(2, RoundingMode.HALF_EVEN));
	
					lineaInventario.save();
				} else {
					//Si el producto no existe crearlo con el resultado
					lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
					lineaInventario.setM_Product_ID(rsVentas.getInt("M_Product_ID"));
					lineaInventario.setXX_VMR_Category_ID(rsVentas.getInt("XX_VMR_Category_ID"));
					lineaInventario.setXX_VMR_Department_ID(rsVentas.getInt("XX_VMR_Department_ID"));
					lineaInventario.setXX_VMR_Line_ID(rsVentas.getInt("XX_VMR_Line_ID"));
					lineaInventario.setXX_VMR_Section_ID(rsVentas.getInt("XX_VMR_Section_ID"));
					lineaInventario.setM_Warehouse_ID(rsVentas.getInt("M_Warehouse_ID"));
					lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
					lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
					lineaInventario.setM_AttributeSetInstance_ID(rsVentas.getInt("M_AttributeSetInstance_ID"));
					lineaInventario.setXX_ConsecutivePrice(rsVentas.getBigDecimal("XX_priceconsecutive")); 
					lineaInventario.setXX_SALESQUANTITY(rsVentas.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
					lineaInventario.setXX_SALESAMOUNT(rsVentas.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN));
					lineaInventario.setXX_MOVEMENTAMOUNT(rsVentas.getBigDecimal("MONTOREBAJAPROMO").setScale(2, RoundingMode.HALF_EVEN));
					lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
					lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
					lineaInventario.setXX_InitialInventoryCostPrice(rsVentas.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
					if (!lineaInventario.save()) {
						throw new Exception("Error guardando las ventas " + lineaInventario);
					}
				}
				//Agregar registro a la tabla de detalle de movimiento de rebaja definitiva en el inventario XX_VCN_InventoryMovDetail
				 registroInvMov = rsVentas.getInt("XX_VCN_INVENTORYMOVDETAIL_ID");
//				 if(registroInvMov<1){
//						registroInvMov = existeRegistroDetalle(rsVentas.getInt("M_PRODUCT_ID"),
//								rsVentas.getInt("M_ATTRIBUTESETINSTANCE_ID"),
//								PriceConsecutive_CD, Warehouse_CD, month ,year,
//								X_Ref_XX_MovementType.REBAJAS_PROMOCIONALES.getValue());
//				 }
				 if(rsVentas.getBigDecimal("MONTOREBAJAPROMO").compareTo(new BigDecimal(0))!=0){
					if(registroInvMov>0){
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
						//Monto de Rebaja Promocional
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								add(rsVentas.getBigDecimal("MONTOREBAJAPROMO")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();					
					 } else {
						 //Si el registro no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(), 0, null);
						 lineaInvMov.setM_Product_ID(rsVentas.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsVentas.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsVentas.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsVentas.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsVentas.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsVentas.getInt("M_Warehouse_ID"));
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsVentas.getInt("M_AttributeSetInstance_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(rsVentas.getBigDecimal("XX_priceconsecutive")); 
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsVentas.getBigDecimal("MONTOREBAJAPROMO").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.REBAJAS_PROMOCIONALES.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro de detalle de movimiento de rebaja promocional" + lineaInvMov);
						 }		 
					}
				}
			}
		}catch (Exception e) {
			 throw new Exception("Error en ajuste de ventas "+e.getMessage());
	
		}finally {
			 rsVentas.close();
			 psVentas.close();	 
		 }
		
	}
	
	private void PedidosEntrantes(int year, int month) throws Exception {
		 /* Buscar Pedidos*/		
		 String sqlPedidos = "\nSELECT DECODE(INV.XX_VCN_INVENTORY_ID,NULL,0,INV.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORY_ID, " +
	 		"\nDECODE(INVMOV.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOV.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVDETAIL_ID, " +
	 		"\nDECODE(DE.M_PRODUCT_ID,NULL,0,DE.M_PRODUCT_ID) M_PRODUCT_ID, " +
	 		"\nDECODE(DE.XX_PRODUCTBATCH_ID,NULL,0,DE.XX_PRODUCTBATCH_ID) LOTE_ID, " +
	 		"\nPE.M_WAREHOUSE_ID, " +
	 		"\nSUM(DE.XX_PRODUCTQUANTITY) CANTIDAD, " +
	 		"\nSUM(CASE WHEN DE.XX_SALEPRICE IS NOT NULL THEN DE.XX_SALEPRICE*DE.XX_PRODUCTQUANTITY " +
	 		"\nELSE 0.01*DE.XX_PRODUCTQUANTITY END) MONTO, " +
	 		"\n(CASE WHEN ASI.PRICEACTUAL IS NOT NULL THEN ASI.PRICEACTUAL ELSE 0.01 END) COSTO, " +
	 		"\nDECODE(DE.XX_PRICECONSECUTIVE,NULL,0,DE.XX_PRICECONSECUTIVE) CONSECUTIVOPRECIO, " +
	 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
	 		"\nFROM XX_VMR_ORDER PE " +
	 		"\nINNER JOIN XX_VMR_ORDERREQUESTDETAIL DE ON (DE.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID) " +
	 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = DE.XX_PRODUCTBATCH_ID) " +
			"\nLEFT JOIN XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID) " +
			"\nLEFT JOIN XX_VLO_DISPATCHGUIDE D ON (DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID) " +	"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DE.M_PRODUCT_ID) " +
	 		"\nLEFT JOIN XX_VCN_INVENTORY INV ON (INV.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
			"\nNVL(INV.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INV.XX_CONSECUTIVEPRICE = NVL(DE.XX_PRICECONSECUTIVE,0) "  +
			"\nAND INV.M_WAREHOUSE_ID = PE.M_WAREHOUSE_ID AND INV.XX_INVENTORYMONTH = "+ month +" AND INV.XX_INVENTORYYEAR = "+ year +") " +	
			"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOV ON (INVMOV.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
			"\nNVL(INVMOV.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INVMOV.XX_CONSECUTIVEPRICE = DE.XX_PRICECONSECUTIVE "  +
			"\nAND INVMOV.M_WAREHOUSE_ID = PE.M_WAREHOUSE_ID AND INVMOV.XX_INVENTORYMONTH = "+ month +" AND INVMOV.XX_INVENTORYYEAR = "+ year +" " +
			"\nAND INVMOV.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.REDISTRIBUCIONES_PEDIDOS.getValue()+") " +
	 		"\nWHERE PE.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
	 		"\nAND PE.XX_ORDERREQUESTSTATUS = 'TI' AND PE.XX_OrderRequestType <> '" + X_Ref_XX_OrderRequestType.DESPACHO_DIRECTO.getValue() +"'"+
	 		"\nAND DE.XX_PRODUCTQUANTITY > 0 AND DE.XX_PRODUCTQUANTITY IS NOT NULL " +
			"\nAND nvl(TO_CHAR(PE.XX_DATESTATUSONSTORE,'YYYY'),TO_CHAR(D.XX_DISPATCHDATE,'YYYY')) =" +year+
	 		"\nAND nvl(TO_CHAR(PE.XX_DATESTATUSONSTORE,'MM'),TO_CHAR(D.XX_DISPATCHDATE,'MM'))  =" + month+
			"\nAND DD.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
			"\nAND D.XX_VLO_DISPATCHGUIDE_ID = (SELECT MAX(D2.XX_VLO_DISPATCHGUIDE_ID) FROM XX_VLO_DETAILDISPATCHGUIDE DD2  " +
			"\nJOIN XX_VLO_DISPATCHGUIDE D2 ON (DD2.XX_VLO_DISPATCHGUIDE_ID = D2.XX_VLO_DISPATCHGUIDE_ID" +
			"\nAND DD2.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D2.XX_DISPATCHGUIDESTATUS <> 'SUG') " +
			"\nWHERE  DD2.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID)" +
	 		//"\nAND TO_CHAR(PE.XX_DATESTATUSONSTORE,'YYYY') =" +year+
	 		//"\nAND TO_CHAR(PE.XX_DATESTATUSONSTORE,'MM') =" + month+
	 		"\nGROUP BY INV.XX_VCN_INVENTORY_ID, INVMOV.XX_VCN_INVENTORYMOVDETAIL_ID, " +
	 		"\nDE.M_PRODUCT_ID, DE.XX_PRODUCTBATCH_ID, PE.M_WAREHOUSE_ID, "+
	 		"\nASI.PRICEACTUAL, DE.XX_PRICECONSECUTIVE, P.XX_VMR_CATEGORY_ID, "+
	 		"\nP.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID ";

		 PreparedStatement psPedidos = null;
		 ResultSet rsPedidos = null;	
		 int registroInv, registroInvMov;

		 try {
			 System.out.println("\nPedidos Entrantes: "+sqlPedidos);
			 psPedidos = DB.prepareStatement(sqlPedidos, null);
			 rsPedidos = psPedidos.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			while (rsPedidos.next()) {
					registroInv = rsPedidos.getInt("XX_VCN_INVENTORY_ID");
//					if(registroInv<1){
//						registroInv = existeRegistro(rsPedidos.getInt("M_PRODUCT_ID"), 
//								rsPedidos.getInt("M_LOTE_ID"), 
//								rsPedidos.getInt("CONSECUTIVOPRECIO"), rsPedidos.getInt("M_Warehouse_ID"), month ,year);
//					}
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto de Pedido
						lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
								add(rsPedidos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Pedido
						lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
								add(rsPedidos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();					
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsPedidos.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsPedidos.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsPedidos.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsPedidos.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsPedidos.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsPedidos.getInt("M_Warehouse_ID"));
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsPedidos.getInt("LOTE_ID"));
						 lineaInventario.setXX_ConsecutivePrice(rsPedidos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInventario.setXX_MOVEMENTQUANTITY(rsPedidos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_MOVEMENTAMOUNT(rsPedidos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsPedidos.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro con pedido entrante" + lineaInventario);
						 }		 
					 }
					//Agregar registro a la tabla de detalle de movimiento en el inventario XX_VCN_InventoryMovDetail
					//Sumando a Tienda
					 registroInvMov = rsPedidos.getInt("XX_VCN_INVENTORYMOVDETAIL_ID");
//					if(registroInvMov<1){
//						registroInvMov = existeRegistroDetalle(rsPedidos.getInt("M_PRODUCT_ID"), 
//								rsPedidos.getInt("M_LOTE_ID"), 
//								rsPedidos.getInt("CONSECUTIVOPRECIO"), rsPedidos.getInt("M_Warehouse_ID"), month ,year,
//								X_Ref_XX_MovementType.REDISTRIBUCIONES_PEDIDOS.getValue());
//					}
					if(registroInvMov>0){
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
						//Monto de Pedido
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								add(rsPedidos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
						//Cantidad de Pedido
						lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
								add(rsPedidos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();					
					 } else {
						 //Si el registro no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(), 0, null);
						 lineaInvMov.setM_Product_ID(rsPedidos.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsPedidos.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsPedidos.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsPedidos.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsPedidos.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsPedidos.getInt("M_Warehouse_ID"));
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsPedidos.getInt("LOTE_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(rsPedidos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInvMov.setXX_MOVEMENTQUANTITY(rsPedidos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsPedidos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.REDISTRIBUCIONES_PEDIDOS.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro de detalle de movimiento de pedido entrante " + lineaInvMov);
						 }		 
					}
			 }
		 }catch (Exception e) {
			 throw new Exception("Error guardando los pedidos entrantes"+e.getMessage());
	
		 }finally {
			 rsPedidos.close();
			 psPedidos.close();	 
		 }
	
	}

	/*MODIFICADO CD VALENCIA*/
	private void PedidosSalientesAjusteCompra(int year, int month) throws Exception {
		 /* Buscar Pedidos*/		
		 String sqlPedidos = "\nSELECT W.M_WAREHOUSE_ID, " +
		 	"\nDECODE(INVCOMPRA.XX_VCN_INVENTORY_ID,NULL,0,INVCOMPRA.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORYCOMPRA_ID, " +
	 		"\nDECODE(DE.M_PRODUCT_ID,NULL,0,DE.M_PRODUCT_ID) M_PRODUCT_ID, " +
	 		"\nDECODE(DE.XX_PRODUCTBATCH_ID,NULL,0,DE.XX_PRODUCTBATCH_ID) LOTE_ID, " +
	 		"\nSUM(DE.XX_PRODUCTQUANTITY) CANTIDAD, " +
	 	//	"\n(CASE WHEN DE.XX_SALEPRICE IS NOT NULL THEN DE.XX_SALEPRICE ELSE 0.01 END) PRECIO, " +
	 		"\nSUM(DECODE(DE.XX_SALEPRICE,NULL,0.01,DE.XX_SALEPRICE)*DE.XX_PRODUCTQUANTITY) MONTO, " +
	 		"\nDECODE(ASI.PRICEACTUAL,NULL,0.01,ASI.PRICEACTUAL) COSTO, " +
	 		"\nSUM((DECODE(DE.XX_SALEPRICE,NULL,0.01,DE.XX_SALEPRICE) - " +
	 		"\nDECODE(ASI.XX_SALEPRICE,NULL,0.01,ASI.XX_SALEPRICE))*DE.XX_PRODUCTQUANTITY) MONTOAJUSTE," +
	 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
	 		"\nFROM XX_VMR_ORDER PE " +
	 		"\nINNER JOIN XX_VMR_ORDERREQUESTDETAIL DE ON (DE.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID) " +
	 		"\nJOIN M_WAREHOUSE W  ON (W.AD_ORG_ID = PE.AD_ORG_ID) " +
	 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = DE.XX_PRODUCTBATCH_ID) "+
			"\nLEFT JOIN XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID) " +
			"\nLEFT JOIN XX_VLO_DISPATCHGUIDE D ON (DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID) " +	
	 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DE.M_PRODUCT_ID) " +
	 		"\nLEFT JOIN XX_VCN_INVENTORY INVCOMPRA ON (INVCOMPRA.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
			"\nNVL(INVCOMPRA.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) " +
			"\nAND INVCOMPRA.XX_CONSECUTIVEPRICE =  " +PriceConsecutive_CD+
			"\nAND INVCOMPRA.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID AND INVCOMPRA.XX_INVENTORYMONTH = "+ month +" " +
			"\nAND INVCOMPRA.XX_INVENTORYYEAR = "+ year +") " +
	 		"\nWHERE PE.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
	 		"\nAND PE.XX_ORDERREQUESTSTATUS = 'TI'  AND PE.XX_OrderRequestType <> '" + X_Ref_XX_OrderRequestType.DESPACHO_DIRECTO.getValue() +"'"+
	 		"\nAND DE.XX_PRODUCTQUANTITY > 0 AND DE.XX_PRODUCTQUANTITY IS NOT NULL " +
			"\nAND nvl(TO_CHAR(PE.XX_DATESTATUSONSTORE,'YYYY'),TO_CHAR(D.XX_DISPATCHDATE,'YYYY')) =" +year+
	 		"\nAND nvl(TO_CHAR(PE.XX_DATESTATUSONSTORE,'MM'),TO_CHAR(D.XX_DISPATCHDATE,'MM'))  =" + month+
			"\nAND DD.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
			"\nAND D.XX_VLO_DISPATCHGUIDE_ID = (SELECT MAX(D2.XX_VLO_DISPATCHGUIDE_ID) FROM XX_VLO_DETAILDISPATCHGUIDE DD2  " +
			"\nJOIN XX_VLO_DISPATCHGUIDE D2 ON (DD2.XX_VLO_DISPATCHGUIDE_ID = D2.XX_VLO_DISPATCHGUIDE_ID" +
			"\nAND DD2.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D2.XX_DISPATCHGUIDESTATUS <> 'SUG') " +
			"\nWHERE  DD2.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID)" +
	 		"\nGROUP BY INVCOMPRA.XX_VCN_INVENTORY_ID, W.M_WAREHOUSE_ID,  " + 
	 		"\nDE.M_PRODUCT_ID, DE.XX_PRODUCTBATCH_ID, "+
	 		"\nASI.PRICEACTUAL, P.XX_VMR_CATEGORY_ID, "+
	 		"\nP.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID ";

		 PreparedStatement psPedidos = null;
		 ResultSet rsPedidos = null;	
		 int registroInv;
		 int prod =0;
		 int pricecons = 0;
		 int lote =0;
		 try {
			 System.out.println("\nPedidos Salientes-Ajuste Compra: "+sqlPedidos);
			 psPedidos = DB.prepareStatement(sqlPedidos, null);
			 rsPedidos = psPedidos.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			while (rsPedidos.next()) {
				prod=rsPedidos.getInt("M_Product_ID");
				 pricecons =PriceConsecutive_CD;
				 lote =rsPedidos.getInt("LOTE_ID");
				 registroInv = rsPedidos.getInt("XX_VCN_INVENTORYCOMPRA_ID");
				 if(registroInv<1){
					 registroInv = existeRegistro(rsPedidos.getInt("M_Product_ID"), 
							rsPedidos.getInt("LOTE_ID"),PriceConsecutive_CD,
							rsPedidos.getInt("M_WAREHOUSE_ID"),month ,year);
				 }
				 if(registroInv>0){
					 lineaInventario = new X_XX_VCN_Inventory(getCtx(), registroInv, null);
					 	//Monto de Ajuste de compra
					 lineaInventario.setXX_SHOPPINGAMOUNT(lineaInventario.getXX_SHOPPINGAMOUNT().
							 add(rsPedidos.getBigDecimal("MONTOAJUSTE").setScale(2, RoundingMode.HALF_EVEN)));
						//Monto de Pedido
					 lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
							 subtract(rsPedidos.getBigDecimal("MONTO")));
								//subtract(rsPedidos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
						//Cantidad de Pedido
					 lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
								subtract(rsPedidos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
					 lineaInventario.save();					
					 }
				 if(registroInv<1){
						 //Si el registro no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsPedidos.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsPedidos.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsPedidos.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsPedidos.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsPedidos.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsPedidos.getInt("M_WAREHOUSE_ID"));
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsPedidos.getInt("LOTE_ID"));
						 lineaInventario.setXX_ConsecutivePrice(new BigDecimal(PriceConsecutive_CD)); 
						 lineaInventario.setXX_SHOPPINGQUANTITY(new BigDecimal(0));
						 lineaInventario.setXX_SHOPPINGAMOUNT(rsPedidos.getBigDecimal("MONTOAJUSTE").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_MOVEMENTQUANTITY(rsPedidos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_MOVEMENTAMOUNT(rsPedidos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsPedidos.getBigDecimal("COSTO").setScale(2, RoundingMode.HALF_EVEN));
						 
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro de  Pedidos Salientes-Ajuste Compra " + lineaInventario);
						 }
						 registroInv = lineaInventario.get_ID();
					}	
//				 //Se realiza el ajuste de precio en las compras
//				 AjusteCompras(rsPedidos.getBigDecimal("PRECIO").setScale(2, RoundingMode.HALF_EVEN), 
//							 rsPedidos.getBigDecimal("PRECIOINICIAL").setScale(2, RoundingMode.HALF_EVEN),
//							 rsPedidos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN),
//							 registroInv, year, month);
			 }
		 }catch (Exception e) {
			 throw new Exception("Error guardando Pedidos Salientes-Ajuste Compra: "+e.getMessage()+"\n prod: "+prod+"\n consec: "+pricecons+"\n lote: "+lote);
	
		 }finally {
			 rsPedidos.close();
			 psPedidos.close();	 
		 }
		
	}
	
	public void AjusteCompras(BigDecimal precioPedido, BigDecimal precioInicial, BigDecimal cantidad, int registro, int año, int mes) throws Exception
	{
		X_XX_VCN_Inventory invenBecoAjus = null;
		BigDecimal diferenciaPrecio = new BigDecimal(0);
		try{
			diferenciaPrecio = precioPedido.subtract(precioInicial);
			invenBecoAjus = new X_XX_VCN_Inventory(getCtx(),registro, null);
			if (diferenciaPrecio.compareTo(Env.ZERO) != 0){
		//		System.out.println("\nprecio pedido:"+precioPedido+" precio inicial: "+precioInicial);
			   invenBecoAjus.setXX_SHOPPINGAMOUNT(invenBecoAjus.getXX_SHOPPINGAMOUNT().add(diferenciaPrecio.multiply(cantidad)));
			}
		    invenBecoAjus.setXX_MOVEMENTAMOUNT(invenBecoAjus.getXX_MOVEMENTAMOUNT().subtract(precioPedido.multiply(cantidad)));
		    //add(new BigDecimal(-1).multiply(precioPedido.multiply(cantidad))));
		    invenBecoAjus.setXX_MOVEMENTQUANTITY(invenBecoAjus.getXX_MOVEMENTQUANTITY().subtract(cantidad).setScale(2, RoundingMode.HALF_EVEN));
		    //add(new BigDecimal(-1).multiply(cantidad)));
		    invenBecoAjus.save();
		}catch (Exception e) {
	    	System.out.println(e.getStackTrace());
	    	throw new Exception("Error realizando ajustes de compras "+e.getStackTrace());
		}
	    
	}
	
	/*MODIFICADO CD VALENCIA*/
	private void PedidosSalientesDetalle(int year, int month) throws Exception {
		 /* Buscar Pedidos*/		
		 String sqlPedidos = "\nSELECT W.M_WAREHOUSE_ID, "+
			"\nDECODE(INVMOVCD.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOVCD.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVDETAILCD_ID, " +
	 		"\nDECODE(DE.M_PRODUCT_ID,NULL,0,DE.M_PRODUCT_ID) M_PRODUCT_ID, " +
	 		"\nDECODE(DE.XX_PRODUCTBATCH_ID,NULL,0,DE.XX_PRODUCTBATCH_ID) LOTE_ID, " +
	 		"\nSUM(DE.XX_PRODUCTQUANTITY) CANTIDAD, " +
	 		"\nSUM(CASE WHEN DE.XX_SALEPRICE IS NOT NULL THEN DE.XX_SALEPRICE*DE.XX_PRODUCTQUANTITY " +
	 		"\nELSE 0.01*DE.XX_PRODUCTQUANTITY END) MONTO, " +
	 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
	 		"\nFROM XX_VMR_ORDER PE " +
	 		"\nINNER JOIN XX_VMR_ORDERREQUESTDETAIL DE ON (DE.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID) " +
	 		"\nJOIN M_WAREHOUSE W  ON (W.AD_ORG_ID = PE.AD_ORG_ID) " +
	 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = DE.XX_PRODUCTBATCH_ID) "+
			"\nLEFT JOIN XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID) " +
			"\nLEFT JOIN XX_VLO_DISPATCHGUIDE D ON (DD.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID) " +	
	 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DE.M_PRODUCT_ID) " +
			"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOVCD ON (INVMOVCD.M_PRODUCT_ID = P.M_PRODUCT_ID AND "+
			"\nNVL(INVMOVCD.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(DE.XX_PRODUCTBATCH_ID,0) AND INVMOVCD.XX_CONSECUTIVEPRICE = " +PriceConsecutive_CD+
			"\nAND INVMOVCD.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID AND INVMOVCD.XX_INVENTORYMONTH =  "+ month +"  AND INVMOVCD.XX_INVENTORYYEAR =  "+ year +" " + 
			"\nAND INVMOVCD.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.REDISTRIBUCIONES_PEDIDOS.getValue()+") " +
	 		"\nWHERE PE.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
	 		"\nAND PE.XX_ORDERREQUESTSTATUS = 'TI'  AND PE.XX_OrderRequestType <> '" + X_Ref_XX_OrderRequestType.DESPACHO_DIRECTO.getValue() +"'"+
	 		"\nAND DE.XX_PRODUCTQUANTITY > 0 AND DE.XX_PRODUCTQUANTITY IS NOT NULL " +
			"\nAND nvl(TO_CHAR(PE.XX_DATESTATUSONSTORE,'YYYY'),TO_CHAR(D.XX_DISPATCHDATE,'YYYY')) =" +year+
	 		"\nAND nvl(TO_CHAR(PE.XX_DATESTATUSONSTORE,'MM'),TO_CHAR(D.XX_DISPATCHDATE,'MM'))  =" + month+
			"\nAND DD.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' " +
			"\nAND D.XX_VLO_DISPATCHGUIDE_ID = (SELECT MAX(D2.XX_VLO_DISPATCHGUIDE_ID) FROM XX_VLO_DETAILDISPATCHGUIDE DD2  " +
			"\nJOIN XX_VLO_DISPATCHGUIDE D2 ON (DD2.XX_VLO_DISPATCHGUIDE_ID = D2.XX_VLO_DISPATCHGUIDE_ID" +
			"\nAND DD2.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' AND D2.XX_DISPATCHGUIDESTATUS <> 'SUG') " +
			"\nWHERE  DD2.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID)" +
	 		"\nGROUP BY INVMOVCD.XX_VCN_INVENTORYMOVDETAIL_ID, W.M_WAREHOUSE_ID,  " + 
	 		"\nDE.M_PRODUCT_ID, DE.XX_PRODUCTBATCH_ID, "+
	 		"\nP.XX_VMR_CATEGORY_ID, "+
	 		"\nP.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID ";

		 PreparedStatement psPedidos = null;
		 ResultSet rsPedidos = null;	
		 int registroInvMov;
		 int prod =0;
		 int pricecons = 0;
		 int lote =0;
		 try {
			 System.out.println("\nPedidos Salientes: "+sqlPedidos);
			 psPedidos = DB.prepareStatement(sqlPedidos, null);
			 rsPedidos = psPedidos.executeQuery();
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			while (rsPedidos.next()) {
				prod=rsPedidos.getInt("M_Product_ID");
				 pricecons =PriceConsecutive_CD;
				 lote =rsPedidos.getInt("LOTE_ID");
	
					//Agregar registro a la tabla de detalle de movimiento en el inventario XX_VCN_InventoryMovDetail
					//Restando a cd 
					 registroInvMov = rsPedidos.getInt("XX_VCN_INVENTORYMOVDETAILCD_ID");
//					if(registroInvMov<1){
//						registroInvMov = existeRegistroDetalle(rsPedidos.getInt("M_PRODUCT_ID"), 
//								rsPedidos.getInt("M_LOTE_ID"), 
//								PriceConsecutive_CD, 
//								Warehouse_CD, month ,year,
//								X_Ref_XX_MovementType.REDISTRIBUCIONES_PEDIDOS.getValue());
//					}
					if(registroInvMov>0){
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
						//Monto de Pedido
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								subtract(rsPedidos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
						//Cantidad de Pedido
						lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
								subtract(rsPedidos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();					
					 } else {
						 //Si el registro no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(), 0, null);
						 lineaInvMov.setM_Product_ID(rsPedidos.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsPedidos.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsPedidos.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsPedidos.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsPedidos.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsPedidos.getInt("M_Warehouse_ID"));
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsPedidos.getInt("LOTE_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(new BigDecimal(PriceConsecutive_CD)); 
						 lineaInvMov.setXX_MOVEMENTQUANTITY(rsPedidos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsPedidos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.REDISTRIBUCIONES_PEDIDOS.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro de detalle de movimiento de pedido saliente" + lineaInvMov);
						 }		 
					}
			 }
		 }catch (Exception e) {
			 throw new Exception("Error guardando los pedidos salientes: "+e.getMessage()+"\n prod: "+prod+"\n consec: "+pricecons+"\n lote: "+lote);
	
		 }finally {
			 rsPedidos.close();
			 psPedidos.close();	 
		 }
		
	}

	private void TraspasosEntrantes(int year, int month) throws Exception {
		 /* Buscar Traspasos Entrantes*/		
		 String sqlTraspasos = "\nSELECT DECODE(INVTO.XX_VCN_INVENTORY_ID,NULL,0,INVTO.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORYTO_ID," +		 	
		 		"\nDECODE(INVMOVTO.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOVTO.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVTO_ID, " +
		 		"\nDECODE(ML.M_PRODUCT_ID,NULL,0,ML.M_PRODUCT_ID) M_PRODUCT_ID, " +
		 		"\nDECODE(ML.M_ATTRIBUTESETINSTANCE_ID,NULL,0,ML.M_ATTRIBUTESETINSTANCE_ID) LOTE_ID, " +
		 		"\nM.M_WAREHOUSETO_ID WAREHOUSETO, " +
		 		"\nSUM(ML.XX_APPROVEDQTY) CANTIDAD, " +
		 		"\nSUM(CASE WHEN ML.XX_SALEPRICE IS NOT NULL " +
		 		"\nTHEN ML.XX_SALEPRICE*ML.XX_APPROVEDQTY ELSE 0.01 END) MONTO, " +
		 		"\n(CASE WHEN ASI.PRICEACTUAL IS NOT NULL THEN ASI.PRICEACTUAL ELSE 0.01 END) COSTO, " +
		 		"\nDECODE(ML.XX_PRICECONSECUTIVE,NULL,0,ML.XX_PRICECONSECUTIVE) CONSECUTIVOPRECIO, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
		 		"\nFROM M_MOVEMENT M " +
		 		"\nINNER JOIN M_MOVEMENTLINE ML ON (ML.M_MOVEMENT_ID = M.M_MOVEMENT_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID) " +
		 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = ML.M_PRODUCT_ID) " +
		 		"\nINNER JOIN XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.M_MOVEMENTT_ID = M.M_MOVEMENT_ID) " +
		 		"\nINNER JOIN XX_VLO_DISPATCHGUIDE D ON (D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID) " +
		 		"\nLEFT JOIN XX_VCN_INVENTORY INVTO ON (INVTO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVTO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVTO.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE "  +
				"\nAND INVTO.M_WAREHOUSE_ID = M.M_WAREHOUSETO_ID AND " +
				"\nINVTO.XX_INVENTORYMONTH = "+ month +" AND INVTO.XX_INVENTORYYEAR = "+ year +") " +				
				"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOVTO ON (INVMOVTO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOVTO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INVMOVTO.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOVTO.M_WAREHOUSE_ID = M.M_WAREHOUSETO_ID AND INVMOVTO.XX_INVENTORYMONTH = "+ month +" AND INVMOVTO.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOVTO.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue()+") " +
		 		"\nWHERE M.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
		 		"\nAND ML.XX_APPROVEDQTY > 0 AND ML.XX_APPROVEDQTY IS NOT NULL " +
		 		"\nAND D.XX_DISPATCHGUIDESTATUS = 'TIE' " +
				"\nAND TO_CHAR(D.XX_DISPATCHDATE,'YYYY') =" +year+
		 		"\nAND TO_CHAR(D.XX_DISPATCHDATE,'MM') =" + month+
//		 		"\nAND TO_CHAR(D.XX_DATESTATUSONSTORE,'YYYY') = " + year +
//		 		"\nAND TO_CHAR(D.XX_DATESTATUSONSTORE,'MM') = " + month +
		 		"\nAND DD.XX_TYPEDETAILDISPATCHGUIDE = 'TRA'" + //TRASPASO
		 		"\nAND M.XX_Status = 'AT' "+  //Aprobado en tienda
		 		"\nAND M.C_DOCTYPE_ID ="+DocTypeTraspaso + //movimiento de tipo traspasos entre tiendas
		 		"\nGROUP BY INVTO.XX_VCN_INVENTORY_ID,  INVMOVTO.XX_VCN_INVENTORYMOVDETAIL_ID,  ML.M_PRODUCT_ID, " +
		 		"\nML.M_ATTRIBUTESETINSTANCE_ID, M.M_WAREHOUSETO_ID, ASI.PRICEACTUAL, ML.XX_PRICECONSECUTIVE, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID";

		 PreparedStatement psTraspasos = null;
		 ResultSet rsTraspasos = null;	
		 int registroInv, registroInvMov;
		 try {
			 System.out.println("\nTraspasos Entrantes: "+sqlTraspasos);
			 psTraspasos = DB.prepareStatement(sqlTraspasos, null);
			 rsTraspasos = psTraspasos.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			 while (rsTraspasos.next() ) {
				// Traspaso Entrante (se suma)
					registroInv = rsTraspasos.getInt("XX_VCN_INVENTORYTO_ID");
//					 if(registroInv<1){
//						 registroInv = existeRegistro(rsTraspasos.getInt("M_Product_ID"), 
//								rsTraspasos.getInt("LOTE_ID"), rsTraspasos.getInt("CONSECUTIVOPRECIO"),
//								rsTraspasos.getInt("WAREHOUSETO"),month ,year);
//					 }
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto del Traspaso
						lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
								add(rsTraspasos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
								add(rsTraspasos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();			
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsTraspasos.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsTraspasos.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsTraspasos.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsTraspasos.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsTraspasos.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsTraspasos.getInt("WAREHOUSETO")); //Tienda Recibidora
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsTraspasos.getInt("LOTE_ID"));
						 lineaInventario.setXX_ConsecutivePrice(rsTraspasos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInventario.setXX_MOVEMENTQUANTITY(rsTraspasos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_MOVEMENTAMOUNT(rsTraspasos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsTraspasos.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro con traspaso entrante" + lineaInventario);
						 }		 
					 }

				// Traspaso Entrante (se suma)
					registroInvMov = rsTraspasos.getInt("XX_VCN_INVENTORYMOVTO_ID");
//					 if(registroInvMov<1){
//						 registroInvMov = existeRegistroDetalle(rsTraspasos.getInt("M_Product_ID"), 
//								rsTraspasos.getInt("LOTE_ID"), rsTraspasos.getInt("CONSECUTIVOPRECIO"),
//								rsTraspasos.getInt("WAREHOUSETO"),month ,year, 
//								X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue());
//					 }
					if (registroInvMov> 0) {
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
							//Monto del Traspaso
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								add(rsTraspasos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
								add(rsTraspasos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();			
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),0, null);
						 lineaInvMov.setM_Product_ID(rsTraspasos.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsTraspasos.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsTraspasos.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsTraspasos.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsTraspasos.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsTraspasos.getInt("WAREHOUSETO")); //Tienda Recibidora
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsTraspasos.getInt("LOTE_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(rsTraspasos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInvMov.setXX_MOVEMENTQUANTITY(rsTraspasos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsTraspasos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro con traspaso entrante" + lineaInvMov);
						 }		 
					 }
			 }
		}catch (Exception e) {
			 System.out.println(e.getMessage());
			 throw new Exception("Error guardando los traspasos entrante"+e.getMessage());
	
		}finally {
			 rsTraspasos.close();
			 psTraspasos.close();	 
		 }
		
	}
	
	
	private void TraspasosSalientes(int year, int month) throws Exception {
		 /* Buscar Traspasos Salientes*/		
		 String sqlTraspasos = "\nSELECT DECODE(INVFROM.XX_VCN_INVENTORY_ID,NULL,0,INVFROM.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORYFROM_ID, " +	 	
		 		"\nDECODE(INVMOVFROM.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOVFROM.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVFROM_ID, " +		 		
		 		"\nDECODE(ML.M_PRODUCT_ID,NULL,0,ML.M_PRODUCT_ID) M_PRODUCT_ID, " +
		 		"\nDECODE(ML.M_ATTRIBUTESETINSTANCE_ID,NULL,0,ML.M_ATTRIBUTESETINSTANCE_ID) LOTE_ID, " +
		 		"\nM.M_WAREHOUSEFROM_ID WAREHOUSEFROM, " +
		 		"\nSUM(ML.XX_APPROVEDQTY) CANTIDAD, " +
		 		"\nSUM(CASE WHEN ML.XX_SALEPRICE IS NOT NULL " +
		 		"\nTHEN ML.XX_SALEPRICE*ML.XX_APPROVEDQTY ELSE 0.01 END) MONTO, " +
		 		"\n(CASE WHEN ASI.PRICEACTUAL IS NOT NULL THEN ASI.PRICEACTUAL ELSE 0.01 END) COSTO, " +
		 		"\nDECODE(ML.XX_PRICECONSECUTIVE,NULL,0,ML.XX_PRICECONSECUTIVE) CONSECUTIVOPRECIO, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
		 		"\nFROM M_MOVEMENT M " +
		 		"\nINNER JOIN M_MOVEMENTLINE ML ON (ML.M_MOVEMENT_ID = M.M_MOVEMENT_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID) " +
		 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = ML.M_PRODUCT_ID) " +
		 		"\nINNER JOIN XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.M_MOVEMENTT_ID = M.M_MOVEMENT_ID) " +
		 		"\nINNER JOIN XX_VLO_DISPATCHGUIDE D ON (D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID) " +
		 		"\nLEFT JOIN XX_VCN_INVENTORY INVFROM ON (INVFROM.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVFROM.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVFROM.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE "  +
				"\nAND INVFROM.M_WAREHOUSE_ID = M.M_WAREHOUSEFROM_ID AND " +
				"\nINVFROM.XX_INVENTORYMONTH = "+ month +" AND INVFROM.XX_INVENTORYYEAR = "+ year +") " +		 		
				"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOVFROM ON (INVMOVFROM.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOVFROM.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INVMOVFROM.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOVFROM.M_WAREHOUSE_ID = M.M_WAREHOUSEFROM_ID AND INVMOVFROM.XX_INVENTORYMONTH = "+ month +" AND INVMOVFROM.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOVFROM.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue()+") " +				
		 		"\nWHERE M.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
		 		"\nAND ML.XX_APPROVEDQTY > 0 AND ML.XX_APPROVEDQTY IS NOT NULL " +
		 		"\nAND D.XX_DISPATCHGUIDESTATUS = 'TIE' " +
				"\nAND TO_CHAR(D.XX_DISPATCHDATE,'YYYY') =" +year+
		 		"\nAND TO_CHAR(D.XX_DISPATCHDATE,'MM') =" + month+
//		 		"\nAND TO_CHAR(D.XX_DATESTATUSONSTORE,'YYYY') = " + year +
//		 		"\nAND TO_CHAR(D.XX_DATESTATUSONSTORE,'MM') = " + month +
		 		"\nAND DD.XX_TYPEDETAILDISPATCHGUIDE = 'TRA'" + //TRASPASO
		 		"\nAND M.XX_Status = 'AT' "+  //Aprobado en tienda
		 		"\nAND M.C_DOCTYPE_ID ="+DocTypeTraspaso + //movimiento de tipo traspasos entre tiendas
		 		"\nGROUP BY INVFROM.XX_VCN_INVENTORY_ID,  INVMOVFROM.XX_VCN_INVENTORYMOVDETAIL_ID,  ML.M_PRODUCT_ID, " +
		 		"\nML.M_ATTRIBUTESETINSTANCE_ID, M.M_WAREHOUSEFROM_ID, ASI.PRICEACTUAL, ML.XX_PRICECONSECUTIVE, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID";

		 PreparedStatement psTraspasos = null;
		 ResultSet rsTraspasos = null;	
		 int registroInv, registroInvMov;
		 try {
			 System.out.println("\nTraspasos Salientes: "+sqlTraspasos);
			 psTraspasos = DB.prepareStatement(sqlTraspasos, null);
			 rsTraspasos = psTraspasos.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			 while (rsTraspasos.next() ) {
				 //Traspaso Saliente (se resta)
					registroInv = rsTraspasos.getInt("XX_VCN_INVENTORYFROM_ID");
//					 if(registroInv<1){
//						 registroInv = existeRegistro(rsTraspasos.getInt("M_Product_ID"), 
//								rsTraspasos.getInt("LOTE_ID"), rsTraspasos.getInt("CONSECUTIVOPRECIO"),
//								rsTraspasos.getInt("WAREHOUSEFROM"),month ,year);
//					 }
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto del Traspaso
						lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
								subtract(rsTraspasos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
								subtract(rsTraspasos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();				
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsTraspasos.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsTraspasos.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsTraspasos.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsTraspasos.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsTraspasos.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsTraspasos.getInt("WAREHOUSEFROM")); 	//Tienda despachadora
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsTraspasos.getInt("LOTE_ID"));
						 lineaInventario.setXX_ConsecutivePrice(rsTraspasos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInventario.setXX_MOVEMENTQUANTITY(rsTraspasos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_MOVEMENTAMOUNT(rsTraspasos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsTraspasos.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro con traspaso saliente " + lineaInventario);
						 }		 
					 }
									
					//Agregar registros a la tabla de detalle de movimiento de traspaso en el inventario XX_VCN_InventoryMovDetail
					 //Traspaso Saliente (se resta)
					registroInvMov = rsTraspasos.getInt("XX_VCN_INVENTORYMOVFROM_ID");
//					 if(registroInvMov<1){
//						 registroInvMov = existeRegistroDetalle(rsTraspasos.getInt("M_Product_ID"), 
//								rsTraspasos.getInt("LOTE_ID"), rsTraspasos.getInt("CONSECUTIVOPRECIO"),
//								rsTraspasos.getInt("WAREHOUSEFROM"),month ,year, 
//								X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue());
//					 }
					if (registroInvMov > 0) {
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
							//Monto del Traspaso
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								subtract(rsTraspasos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
								subtract(rsTraspasos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();				
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),0, null);
						 lineaInvMov.setM_Product_ID(rsTraspasos.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsTraspasos.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsTraspasos.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsTraspasos.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsTraspasos.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsTraspasos.getInt("WAREHOUSEFROM")); 	//Tienda despachadora
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsTraspasos.getInt("LOTE_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(rsTraspasos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInvMov.setXX_MOVEMENTQUANTITY(rsTraspasos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsTraspasos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro con traspaso saliente " + lineaInvMov);
						 }		 
					 }
			 }
		}catch (Exception e) {
			 System.out.println(e.getMessage());
			 throw new Exception("Error guardando los traspasos salientes"+e.getMessage());
	
		}finally {
			 rsTraspasos.close();
			 psTraspasos.close();	 
		 }
		
	}
	
	
	private void TraspasosEntreCDEntrantes(int year, int month) throws Exception {
		 /* Buscar Traspasos Entrantes*/		
		 String sqlTraspasos = "\nSELECT DECODE(INVTO.XX_VCN_INVENTORY_ID,NULL,0,INVTO.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORYTO_ID," +		 	
		 		"\nDECODE(INVMOVTO.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOVTO.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVTO_ID, " +
		 		"\nDECODE(ML.M_PRODUCT_ID,NULL,0,ML.M_PRODUCT_ID) M_PRODUCT_ID, " +
		 		"\nDECODE(ML.M_ATTRIBUTESETINSTANCE_ID,NULL,0,ML.M_ATTRIBUTESETINSTANCE_ID) LOTE_ID, " +
		 		"\nM.M_WAREHOUSETO_ID WAREHOUSETO, " +
		 		"\nSUM(ML.XX_APPROVEDQTY) CANTIDAD, " +
		 		"\nSUM(CASE WHEN ASI.XX_SALEPRICE IS NOT NULL " +
		 		"\nTHEN ASI.XX_SALEPRICE*ML.XX_APPROVEDQTY ELSE 0.01 END) MONTO, " +
		 		"\n(CASE WHEN ASI.PRICEACTUAL IS NOT NULL THEN ASI.PRICEACTUAL ELSE 0.01 END) COSTO, " +
		 		"\nDECODE(ML.XX_PRICECONSECUTIVE,NULL,0,ML.XX_PRICECONSECUTIVE) CONSECUTIVOPRECIO, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
		 		"\nFROM M_MOVEMENT M " +
		 		"\nINNER JOIN M_MOVEMENTLINE ML ON (ML.M_MOVEMENT_ID = M.M_MOVEMENT_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID) " +
		 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = ML.M_PRODUCT_ID) " +
		 		"\nINNER JOIN XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.M_MOVEMENTM_ID = M.M_MOVEMENT_ID) " +
		 		"\nINNER JOIN XX_VLO_DISPATCHGUIDE D ON (D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID) " +
		 		"\nLEFT JOIN XX_VCN_INVENTORY INVTO ON (INVTO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVTO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVTO.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE "  +
				"\nAND INVTO.M_WAREHOUSE_ID = M.M_WAREHOUSETO_ID AND " +
				"\nINVTO.XX_INVENTORYMONTH = "+ month +" AND INVTO.XX_INVENTORYYEAR = "+ year +") " +				
				"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOVTO ON (INVMOVTO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOVTO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INVMOVTO.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOVTO.M_WAREHOUSE_ID = M.M_WAREHOUSETO_ID AND INVMOVTO.XX_INVENTORYMONTH = "+ month +" AND INVMOVTO.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOVTO.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue()+") " +
		 		"\nWHERE M.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
		 		"\nAND ML.XX_APPROVEDQTY > 0 AND ML.XX_APPROVEDQTY IS NOT NULL " +
		 		"\nAND D.XX_DISPATCHGUIDESTATUS = 'TIE' " +
				"\nAND TO_CHAR(D.XX_DISPATCHDATE,'YYYY') =" +year+
		 		"\nAND TO_CHAR(D.XX_DISPATCHDATE,'MM') =" + month+
//		 		"\nAND TO_CHAR(D.XX_DATESTATUSONSTORE,'YYYY') = " + year +
//		 		"\nAND TO_CHAR(D.XX_DATESTATUSONSTORE,'MM') = " + month +
		 		"\nAND DD.XX_TYPEDETAILDISPATCHGUIDE = 'IMV' "+
		 		"\nAND M.XX_Status = 'AC' "+  //Aprobado en CD
		 		"\nAND M.C_DOCTYPE_ID = "+DocTypeMovEntreCD + //1000335 Movimiento de Inventario entre CD"
		 		"\n GROUP BY INVTO.XX_VCN_INVENTORY_ID,  INVMOVTO.XX_VCN_INVENTORYMOVDETAIL_ID,  ML.M_PRODUCT_ID, " +
		 		"\nML.M_ATTRIBUTESETINSTANCE_ID, M.M_WAREHOUSETO_ID, ASI.PRICEACTUAL, ML.XX_PRICECONSECUTIVE, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID";

		 PreparedStatement psTraspasos = null;
		 ResultSet rsTraspasos = null;	
		 int registroInv, registroInvMov;
		 try {
			 System.out.println("\nTraspasos Entre CD Entrantes: "+sqlTraspasos);
			 psTraspasos = DB.prepareStatement(sqlTraspasos, null);
			 rsTraspasos = psTraspasos.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			 while (rsTraspasos.next() ) {
				// Traspaso Entrante (se suma)
					registroInv = rsTraspasos.getInt("XX_VCN_INVENTORYTO_ID");
//					 if(registroInv<1){
//						 registroInv = existeRegistro(rsTraspasos.getInt("M_Product_ID"), 
//								rsTraspasos.getInt("LOTE_ID"), rsTraspasos.getInt("CONSECUTIVOPRECIO"),
//								rsTraspasos.getInt("WAREHOUSETO"),month ,year);
//					 }
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto del Traspaso
						lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
								add(rsTraspasos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
								add(rsTraspasos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();			
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsTraspasos.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsTraspasos.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsTraspasos.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsTraspasos.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsTraspasos.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsTraspasos.getInt("WAREHOUSETO")); //Tienda Recibidora
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsTraspasos.getInt("LOTE_ID"));
						 lineaInventario.setXX_ConsecutivePrice(rsTraspasos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInventario.setXX_MOVEMENTQUANTITY(rsTraspasos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_MOVEMENTAMOUNT(rsTraspasos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsTraspasos.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro con traspaso entrante" + lineaInventario);
						 }		 
					 }

				// Traspaso Entrante (se suma)
					registroInvMov = rsTraspasos.getInt("XX_VCN_INVENTORYMOVTO_ID");
//					 if(registroInvMov<1){
//						 registroInvMov = existeRegistroDetalle(rsTraspasos.getInt("M_Product_ID"), 
//								rsTraspasos.getInt("LOTE_ID"), rsTraspasos.getInt("CONSECUTIVOPRECIO"),
//								rsTraspasos.getInt("WAREHOUSETO"),month ,year, 
//								X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue());
//					 }
					if (registroInvMov> 0) {
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
							//Monto del Traspaso
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								add(rsTraspasos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
								add(rsTraspasos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();			
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),0, null);
						 lineaInvMov.setM_Product_ID(rsTraspasos.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsTraspasos.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsTraspasos.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsTraspasos.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsTraspasos.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsTraspasos.getInt("WAREHOUSETO")); //Tienda Recibidora
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsTraspasos.getInt("LOTE_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(rsTraspasos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInvMov.setXX_MOVEMENTQUANTITY(rsTraspasos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsTraspasos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro con traspaso entrante" + lineaInvMov);
						 }		 
					 }
			 }
		}catch (Exception e) {
			 System.out.println(e.getMessage());
			 throw new Exception("Error guardando los traspasos entrante"+e.getMessage());
	
		}finally {
			 rsTraspasos.close();
			 psTraspasos.close();	 
		 }
		
	}
	
	
	private void TraspasosEntreCDSalientes(int year, int month) throws Exception {
		 /* Buscar Traspasos Salientes*/		
		 String sqlTraspasos = "\nSELECT DECODE(INVFROM.XX_VCN_INVENTORY_ID,NULL,0,INVFROM.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORYFROM_ID, " +	 	
		 		"\nDECODE(INVMOVFROM.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOVFROM.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVFROM_ID, " +		 		
		 		"\nDECODE(ML.M_PRODUCT_ID,NULL,0,ML.M_PRODUCT_ID) M_PRODUCT_ID, " +
		 		"\nDECODE(ML.M_ATTRIBUTESETINSTANCE_ID,NULL,0,ML.M_ATTRIBUTESETINSTANCE_ID) LOTE_ID, " +
		 		"\nM.M_WAREHOUSEFROM_ID WAREHOUSEFROM, " +
		 		"\nSUM(ML.XX_APPROVEDQTY) CANTIDAD, " +
		 		"\nSUM(CASE WHEN ASI.XX_SALEPRICE IS NOT NULL " +
		 		"\nTHEN ASI.XX_SALEPRICE*ML.XX_APPROVEDQTY ELSE 0.01 END) MONTO, " +
		 		"\n(CASE WHEN ASI.PRICEACTUAL IS NOT NULL THEN ASI.PRICEACTUAL ELSE 0.01 END) COSTO, " +
		 		"\nDECODE(ML.XX_PRICECONSECUTIVE,NULL,0,ML.XX_PRICECONSECUTIVE) CONSECUTIVOPRECIO, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
		 		"\nFROM M_MOVEMENT M " +
		 		"\nINNER JOIN M_MOVEMENTLINE ML ON (ML.M_MOVEMENT_ID = M.M_MOVEMENT_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID) " +
		 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = ML.M_PRODUCT_ID) " +
		 		"\nINNER JOIN XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.M_MOVEMENTM_ID = M.M_MOVEMENT_ID) " +
		 		"\nINNER JOIN XX_VLO_DISPATCHGUIDE D ON (D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID) " +
		 		"\nLEFT JOIN XX_VCN_INVENTORY INVFROM ON (INVFROM.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVFROM.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVFROM.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE "  +
				"\nAND INVFROM.M_WAREHOUSE_ID = M.M_WAREHOUSEFROM_ID AND " +
				"\nINVFROM.XX_INVENTORYMONTH = "+ month +" AND INVFROM.XX_INVENTORYYEAR = "+ year +") " +		 		
				"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOVFROM ON (INVMOVFROM.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOVFROM.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INVMOVFROM.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOVFROM.M_WAREHOUSE_ID = M.M_WAREHOUSEFROM_ID AND INVMOVFROM.XX_INVENTORYMONTH = "+ month +" AND INVMOVFROM.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOVFROM.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue()+") " +				
		 		"\nWHERE M.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
		 		"\nAND ML.XX_APPROVEDQTY > 0 AND ML.XX_APPROVEDQTY IS NOT NULL " +
		 		"\nAND D.XX_DISPATCHGUIDESTATUS = 'TIE' " +
				"\nAND TO_CHAR(D.XX_DISPATCHDATE,'YYYY') =" +year+
		 		"\nAND TO_CHAR(D.XX_DISPATCHDATE,'MM') =" + month+
//		 		"\nAND TO_CHAR(D.XX_DATESTATUSONSTORE,'YYYY') = " + year +
//		 		"\nAND TO_CHAR(D.XX_DATESTATUSONSTORE,'MM') = " + month +
		 		"\nAND DD.XX_TYPEDETAILDISPATCHGUIDE = 'IMV' "+
		 		"\nAND M.XX_Status = 'AC' "+  //Aprobado en CD
		 		"\nAND M.C_DOCTYPE_ID = "+DocTypeMovEntreCD + //1000335 Movimiento de Inventario entre CD"
		 		"\nGROUP BY INVFROM.XX_VCN_INVENTORY_ID,  INVMOVFROM.XX_VCN_INVENTORYMOVDETAIL_ID,  ML.M_PRODUCT_ID, " +
		 		"\nML.M_ATTRIBUTESETINSTANCE_ID, M.M_WAREHOUSEFROM_ID, ASI.PRICEACTUAL, ML.XX_PRICECONSECUTIVE, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID";

		 PreparedStatement psTraspasos = null;
		 ResultSet rsTraspasos = null;	
		 int registroInv, registroInvMov;
		 try {
			 System.out.println("\nTraspasos  Entre CD Salientes: "+sqlTraspasos);
			 psTraspasos = DB.prepareStatement(sqlTraspasos, null);
			 rsTraspasos = psTraspasos.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			 while (rsTraspasos.next() ) {
				 //Traspaso Saliente (se resta)
					registroInv = rsTraspasos.getInt("XX_VCN_INVENTORYFROM_ID");
//					 if(registroInv<1){
//						 registroInv = existeRegistro(rsTraspasos.getInt("M_Product_ID"), 
//								rsTraspasos.getInt("LOTE_ID"), rsTraspasos.getInt("CONSECUTIVOPRECIO"),
//								rsTraspasos.getInt("WAREHOUSEFROM"),month ,year);
//					 }
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto del Traspaso
						lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
								subtract(rsTraspasos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
								subtract(rsTraspasos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();				
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsTraspasos.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsTraspasos.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsTraspasos.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsTraspasos.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsTraspasos.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsTraspasos.getInt("WAREHOUSEFROM")); 	//Tienda despachadora
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsTraspasos.getInt("LOTE_ID"));
						 lineaInventario.setXX_ConsecutivePrice(rsTraspasos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInventario.setXX_MOVEMENTQUANTITY(rsTraspasos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_MOVEMENTAMOUNT(rsTraspasos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsTraspasos.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro con traspaso saliente " + lineaInventario);
						 }		 
					 }
									
					//Agregar registros a la tabla de detalle de movimiento de traspaso en el inventario XX_VCN_InventoryMovDetail
					 //Traspaso Saliente (se resta)
					registroInvMov = rsTraspasos.getInt("XX_VCN_INVENTORYMOVFROM_ID");
//					 if(registroInvMov<1){
//						 registroInvMov = existeRegistroDetalle(rsTraspasos.getInt("M_Product_ID"), 
//								rsTraspasos.getInt("LOTE_ID"), rsTraspasos.getInt("CONSECUTIVOPRECIO"),
//								rsTraspasos.getInt("WAREHOUSEFROM"),month ,year, 
//								X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue());
//					 }
					if (registroInvMov > 0) {
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
							//Monto del Traspaso
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								subtract(rsTraspasos.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
								subtract(rsTraspasos.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();				
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),0, null);
						 lineaInvMov.setM_Product_ID(rsTraspasos.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsTraspasos.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsTraspasos.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsTraspasos.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsTraspasos.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsTraspasos.getInt("WAREHOUSEFROM")); 	//Tienda despachadora
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsTraspasos.getInt("LOTE_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(rsTraspasos.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInvMov.setXX_MOVEMENTQUANTITY(rsTraspasos.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsTraspasos.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro con traspaso saliente " + lineaInvMov);
						 }		 
					 }
			 }
		}catch (Exception e) {
			 System.out.println(e.getMessage());
			 throw new Exception("Error guardando los traspasos salientes"+e.getMessage());
	
		}finally {
			 rsTraspasos.close();
			 psTraspasos.close();	 
		 }
		
	}
	
	private void RebajasDefinitivasMontoRebajado(int year, int month) throws Exception {

		 /* Buscar Rebajas Definitivas*/		
		 String sqlRebajasDef = "\nSELECT DECODE(INVVIEJO.XX_VCN_INVENTORY_ID,NULL,0,INVVIEJO.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORYVIEJO_ID, " +
		 		"\nDECODE(INVMOVVIEJO.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOVVIEJO.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVVIEJO_ID," +
		 		"\nDR.M_WAREHOUSE_ID, " +
		 		"\nDECODE(DAD.M_PRODUCT_ID,NULL,0,DAD.M_PRODUCT_ID) M_PRODUCT_ID, " +
		 		"\nDECODE(PC.XX_PRICECONSECUTIVE,NULL,0,PC.XX_PRICECONSECUTIVE) PCVIEJO, " +
		 		"\nDECODE(PC.M_ATTRIBUTESETINSTANCE_ID,NULL,0,PC.M_ATTRIBUTESETINSTANCE_ID) LOTE_ID, " + 
		 		"\nSUM(CASE WHEN DAD.XX_DISCOUNTPRICE IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_DISCOUNTPRICE*DAD.XX_LOWERINGQUANTITY ELSE 0 END) MONTONUEVO, " +
		 		"\nSUM(CASE WHEN DAD.XX_PRICEBEFOREDISCOUNT IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_PRICEBEFOREDISCOUNT* DAD.XX_LOWERINGQUANTITY ELSE 0 END) MONTOAREBAJAR, " +
		 		"\nSUM(DAD.XX_LOWERINGQUANTITY) CANTIDAD, " +
		 		"\nDECODE(ASI.PRICEACTUAL,NULL,0.01,ASI.PRICEACTUAL) COSTO, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
		 		"\nFROM XX_VMR_DISCOUNTREQUEST DR " +
		 		"\nINNER JOIN XX_VMR_DISCOUNTAPPLIDETAIL DAD ON " +
		 		"\n(DR.XX_VMR_DISCOUNTREQUEST_ID = DAD.XX_VMR_DISCOUNTREQUEST_ID) " +
		 		"\nLEFT JOIN XX_VMR_PRICECONSECUTIVE PC ON (PC.XX_VMR_PRICECONSECUTIVE_ID = DAD.XX_VMR_PRICECONSECUTIVE_ID) " +
		 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DAD.M_PRODUCT_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = PC.M_ATTRIBUTESETINSTANCE_ID) " +
		 		"\nLEFT JOIN XX_VCN_INVENTORY INVVIEJO ON (INVVIEJO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVVIEJO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVVIEJO.XX_CONSECUTIVEPRICE = PC.XX_PRICECONSECUTIVE "  +
				"\nAND INVVIEJO.M_WAREHOUSE_ID = DR.M_WAREHOUSE_ID AND " +
				"\nINVVIEJO.XX_INVENTORYMONTH = "+ month +" AND INVVIEJO.XX_INVENTORYYEAR = "+ year +") " +
				"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOVVIEJO ON (INVMOVVIEJO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOVVIEJO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVMOVVIEJO.XX_CONSECUTIVEPRICE = PC.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOVVIEJO.M_WAREHOUSE_ID = DR.M_WAREHOUSE_ID AND " +
				"\nINVMOVVIEJO.XX_INVENTORYMONTH = "+ month +" AND INVMOVVIEJO.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOVVIEJO.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue()+") " +		 		
		 		"\nWHERE DR.XX_Status IN ('AP','AN') "+ // <> 'RV' " +
		 		"\nAND DAD.XX_LOWERINGQUANTITY IS NOT NULL" +
		 		"\nAND NVL(DR.XX_YEARUPDATE,TO_CHAR(DR.CREATED,'YYYY')) = " + year +
		 		"\nAND NVL(DR.XX_MONTHUPDATE,TO_CHAR(DR.CREATED,'MM')) = " + month+
		 		"\nGROUP BY INVVIEJO.XX_VCN_INVENTORY_ID,  " +
		 		"\nINVMOVVIEJO.XX_VCN_INVENTORYMOVDETAIL_ID, ASI.PRICEACTUAL," +
		 		"\nDR.M_WAREHOUSE_ID, DAD.M_PRODUCT_ID, " +
		 		"\nPC.XX_PRICECONSECUTIVE, PC.M_ATTRIBUTESETINSTANCE_ID, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID ";
		 		
		 		
		 
		 PreparedStatement psRebajasDef = null;
		 ResultSet rsRebajasDef = null;	
		 int registroInv, registroInvMov;

		try {
			 System.out.println("\nRebajas Definitivas : "+sqlRebajasDef);
			 psRebajasDef = DB.prepareStatement(sqlRebajasDef, null);
			 rsRebajasDef = psRebajasDef.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			 while (rsRebajasDef.next()) {
				 //Registro al que se rebajara (se resta)
					registroInv = rsRebajasDef.getInt("XX_VCN_INVENTORYVIEJO_ID");
//					 if(registroInv<1){
//						 registroInv = existeRegistro(rsRebajasDef.getInt("M_Product_ID"), 
//								 rsRebajasDef.getInt("LOTE_ID"), rsRebajasDef.getInt("PCVIEJO"),
//								 rsRebajasDef.getInt("M_WAREHOUSE_ID"),month ,year);
//					 }
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto del Traspaso
						lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
								subtract(rsRebajasDef.getBigDecimal("MONTOAREBAJAR")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
								subtract(rsRebajasDef.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();			
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsRebajasDef.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsRebajasDef.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsRebajasDef.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsRebajasDef.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsRebajasDef.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsRebajasDef.getInt("M_WAREHOUSE_ID")); 	//Tienda 
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsRebajasDef.getInt("LOTE_ID"));
						 lineaInventario.setXX_ConsecutivePrice(rsRebajasDef.getBigDecimal("PCVIEJO")); 
						 lineaInventario.setXX_MOVEMENTQUANTITY(rsRebajasDef.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_MOVEMENTAMOUNT(rsRebajasDef.getBigDecimal("MONTOAREBAJAR").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsRebajasDef.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro con rebaja definitiva " + lineaInventario);
						 }		 
					 }
				
					//Agregar registro a la tabla de detalle de movimiento de rebaja definitiva en el inventario XX_VCN_InventoryMovDetail
					 //Registro al que se rebajara (se resta)
					registroInvMov = rsRebajasDef.getInt("XX_VCN_INVENTORYMOVVIEJO_ID");
//					if(registroInvMov<1){
//						 registroInvMov = existeRegistroDetalle(rsRebajasDef.getInt("M_Product_ID"), 
//								 rsRebajasDef.getInt("LOTE_ID"), rsRebajasDef.getInt("PCVIEJO"),
//								 rsRebajasDef.getInt("M_WAREHOUSE_ID"),month ,year,
//								 X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue());
//					}
					if (registroInvMov > 0) {
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
							//Monto del Traspaso
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								subtract(rsRebajasDef.getBigDecimal("MONTOAREBAJAR")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
								subtract(rsRebajasDef.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();			
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(), 0, null);
						 lineaInvMov.setM_Product_ID(rsRebajasDef.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsRebajasDef.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsRebajasDef.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsRebajasDef.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsRebajasDef.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsRebajasDef.getInt("M_WAREHOUSE_ID")); 	//Tienda 
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsRebajasDef.getInt("LOTE_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(rsRebajasDef.getBigDecimal("PCVIEJO")); 
						 lineaInvMov.setXX_MOVEMENTQUANTITY(rsRebajasDef.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsRebajasDef.getBigDecimal("MONTOAREBAJAR").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro con rebaja definitiva " + lineaInvMov);
						 }		 
					 }
			 }
		}catch (Exception e) {
			 System.out.println(e.getMessage());
			 throw new Exception("Error guardando las rebajas definitivas "+e.getMessage());
	
		}finally {
			 rsRebajasDef.close();
			 psRebajasDef.close();	 
		 }
	}
	
	
	private void RebajasDefinitivasMontoNuevo(int year, int month) throws Exception {

		 /* Buscar Rebajas Definitivas*/		
		 String sqlRebajasDef = "\nSELECT " +
		 		"\nDECODE(INVNUEVO.XX_VCN_INVENTORY_ID,NULL,0,INVNUEVO.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORYNUEVO_ID," +
		 		"\nDECODE(INVMOVNUEVO.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOVNUEVO.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVNUEVO_ID," +
		 		"\nDR.M_WAREHOUSE_ID, " +
		 		"\nDECODE(DAD.M_PRODUCT_ID,NULL,0,DAD.M_PRODUCT_ID) M_PRODUCT_ID, " +
		 		"\nDECODE(PCNUEVO.XX_PRICECONSECUTIVE,NULL,0,PCNUEVO.XX_PRICECONSECUTIVE) PCNUEVO, " +
		 		"\nDECODE(PCNUEVO.M_ATTRIBUTESETINSTANCE_ID,NULL,0,PCNUEVO.M_ATTRIBUTESETINSTANCE_ID) LOTE_ID, " + 
		 		"\nSUM(CASE WHEN DAD.XX_DISCOUNTPRICE IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_DISCOUNTPRICE*DAD.XX_LOWERINGQUANTITY ELSE 0 END) MONTONUEVO, " +
		 		"\nSUM(CASE WHEN DAD.XX_PRICEBEFOREDISCOUNT IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_PRICEBEFOREDISCOUNT* DAD.XX_LOWERINGQUANTITY ELSE 0 END) MONTOAREBAJAR, " +
		 		"\nSUM(DAD.XX_LOWERINGQUANTITY) CANTIDAD, " +
		 		"\nDECODE(ASI.PRICEACTUAL,NULL,0.01,ASI.PRICEACTUAL) COSTO, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
		 		"\nFROM XX_VMR_DISCOUNTREQUEST DR " +
		 		"\nINNER JOIN XX_VMR_DISCOUNTAPPLIDETAIL DAD ON " +
		 		"\n(DR.XX_VMR_DISCOUNTREQUEST_ID = DAD.XX_VMR_DISCOUNTREQUEST_ID) " +
		 		"\nLEFT JOIN XX_VMR_PRICECONSECUTIVE PCNUEVO ON (PCNUEVO.XX_VMR_PRICECONSECUTIVE_ID = DAD.XX_PRICECONSECUTIVE_ID) " +
		 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DAD.M_PRODUCT_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = PCNUEVO.M_ATTRIBUTESETINSTANCE_ID) " +
		 		"\nLEFT JOIN XX_VCN_INVENTORY INVNUEVO ON (INVNUEVO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVNUEVO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVNUEVO.XX_CONSECUTIVEPRICE = PCNUEVO.XX_PRICECONSECUTIVE "  +
				"\nAND INVNUEVO.M_WAREHOUSE_ID = DR.M_WAREHOUSE_ID AND " +
				"\nINVNUEVO.XX_INVENTORYMONTH = "+ month +" AND INVNUEVO.XX_INVENTORYYEAR = "+ year +") " +
		 		"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOVNUEVO ON (INVMOVNUEVO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOVNUEVO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVMOVNUEVO.XX_CONSECUTIVEPRICE = PCNUEVO.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOVNUEVO.M_WAREHOUSE_ID = DR.M_WAREHOUSE_ID AND " +
				"\nINVMOVNUEVO.XX_INVENTORYMONTH = "+ month +" AND INVMOVNUEVO.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOVNUEVO.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue()+") " +
		 		"\nWHERE DR.XX_Status IN ('AP','AN') "+ // <> 'RV' " +
		 		"\nAND DAD.XX_LOWERINGQUANTITY IS NOT NULL" +
		 		"\nAND NVL(DR.XX_YEARUPDATE,TO_CHAR(DR.CREATED,'YYYY')) = " + year +
		 		"\nAND NVL(DR.XX_MONTHUPDATE,TO_CHAR(DR.CREATED,'MM')) = " + month+
		 		"\nGROUP BY  INVNUEVO.XX_VCN_INVENTORY_ID, " +
		 		"\nASI.PRICEACTUAL," +
		 		"\nINVMOVNUEVO.XX_VCN_INVENTORYMOVDETAIL_ID, DR.M_WAREHOUSE_ID, DAD.M_PRODUCT_ID, " +
		 		"\nPCNUEVO.XX_PRICECONSECUTIVE, PCNUEVO.M_ATTRIBUTESETINSTANCE_ID, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
				"\nHAVING SUM(CASE WHEN DAD.XX_DISCOUNTPRICE IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_DISCOUNTPRICE*DAD.XX_LOWERINGQUANTITY ELSE 0 END) != 0  " ;
		 		
		 		
		 
		 PreparedStatement psRebajasDef = null;
		 ResultSet rsRebajasDef = null;	
		 int registroInv, registroInvMov;

		try {
			 System.out.println("\nRebajas Definitivas : "+sqlRebajasDef);
			 psRebajasDef = DB.prepareStatement(sqlRebajasDef, null);
			 rsRebajasDef = psRebajasDef.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			 while (rsRebajasDef.next()) {
				//Nuevo Registro rebajado (se suma)	
					//if(rsRebajasDef.getBigDecimal("MONTONUEVO").compareTo(new BigDecimal(0))!=0){
						registroInv = rsRebajasDef.getInt("XX_VCN_INVENTORYNUEVO_ID");
//						 if(registroInv<1){
//							 registroInv = existeRegistro(rsRebajasDef.getInt("M_Product_ID"), 
//									 rsRebajasDef.getInt("LOTE_ID"), rsRebajasDef.getInt("PCNUEVO"),
//									 rsRebajasDef.getInt("M_WAREHOUSE_ID"),month ,year);
//						 }
						if (registroInv > 0) {
							lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
								//Monto del Traspaso
							lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
									add(rsRebajasDef.getBigDecimal("MONTONUEVO")).setScale(2, RoundingMode.HALF_EVEN));
								//Cantidad del Traspaso
							lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
									add(rsRebajasDef.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
							lineaInventario.save();							
						 } else {
							 //Si el producto no existe crearlo con el resultado
							 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
							 lineaInventario.setM_Product_ID(rsRebajasDef.getInt("M_Product_ID"));
							 lineaInventario.setXX_VMR_Category_ID(rsRebajasDef.getInt("XX_VMR_Category_ID"));
							 lineaInventario.setXX_VMR_Department_ID(rsRebajasDef.getInt("XX_VMR_Department_ID"));
							 lineaInventario.setXX_VMR_Line_ID(rsRebajasDef.getInt("XX_VMR_Line_ID"));
							 lineaInventario.setXX_VMR_Section_ID(rsRebajasDef.getInt("XX_VMR_Section_ID"));
							 lineaInventario.setM_Warehouse_ID(rsRebajasDef.getInt("M_WAREHOUSE_ID")); //Tienda Recibidora
							 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
							 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
							 lineaInventario.setM_AttributeSetInstance_ID(rsRebajasDef.getInt("LOTE_ID"));
							 lineaInventario.setXX_ConsecutivePrice(rsRebajasDef.getBigDecimal("PCNUEVO")); 
							 lineaInventario.setXX_MOVEMENTQUANTITY(rsRebajasDef.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
							 lineaInventario.setXX_MOVEMENTAMOUNT(rsRebajasDef.getBigDecimal("MONTONUEVO").setScale(2, RoundingMode.HALF_EVEN));
							 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
							 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
							 lineaInventario.setXX_InitialInventoryCostPrice(rsRebajasDef.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
							 if (!lineaInventario.save()) {
								 throw new Exception("Error guardando registro con rebaja definitiva" + lineaInventario);
							 }		 
						 }
					//}
					//Agregar registro a la tabla de detalle de movimiento de rebaja definitiva en el inventario XX_VCN_InventoryMovDetail
				 //Nuevo Registro rebajado (se suma)	
					//if(rsRebajasDef.getBigDecimal("MONTONUEVO").compareTo(new BigDecimal(0))!=0){
						registroInvMov = rsRebajasDef.getInt("XX_VCN_INVENTORYMOVNUEVO_ID");
//						 if(registroInvMov<1){
//							 registroInvMov = existeRegistroDetalle(rsRebajasDef.getInt("M_Product_ID"), 
//									 rsRebajasDef.getInt("LOTE_ID"), rsRebajasDef.getInt("PCNUEVO"),
//									 rsRebajasDef.getInt("M_WAREHOUSE_ID"),month ,year,
//									 X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue());
//						 }
						if (registroInvMov > 0) {
							lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
								//Monto del Traspaso
							lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
									add(rsRebajasDef.getBigDecimal("MONTONUEVO")).setScale(2, RoundingMode.HALF_EVEN));
								//Cantidad del Traspaso
							lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
									add(rsRebajasDef.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
							lineaInvMov.save();							
						 } else {
							 //Si el producto no existe crearlo con el resultado
							 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(), 0, null);
							 lineaInvMov.setM_Product_ID(rsRebajasDef.getInt("M_Product_ID"));
							 lineaInvMov.setXX_VMR_Category_ID(rsRebajasDef.getInt("XX_VMR_Category_ID"));
							 lineaInvMov.setXX_VMR_Department_ID(rsRebajasDef.getInt("XX_VMR_Department_ID"));
							 lineaInvMov.setXX_VMR_Line_ID(rsRebajasDef.getInt("XX_VMR_Line_ID"));
							 lineaInvMov.setXX_VMR_Section_ID(rsRebajasDef.getInt("XX_VMR_Section_ID"));
							 lineaInvMov.setM_Warehouse_ID(rsRebajasDef.getInt("M_WAREHOUSE_ID")); //Tienda Recibidora
							 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
							 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
							 lineaInvMov.setM_AttributeSetInstance_ID(rsRebajasDef.getInt("LOTE_ID"));
							 lineaInvMov.setXX_ConsecutivePrice(rsRebajasDef.getBigDecimal("PCNUEVO")); 
							 lineaInvMov.setXX_MOVEMENTQUANTITY(rsRebajasDef.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
							 lineaInvMov.setXX_MOVEMENTAMOUNT(rsRebajasDef.getBigDecimal("MONTONUEVO").setScale(2, RoundingMode.HALF_EVEN));
							 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue());
							 if (!lineaInvMov.save()) {
								 throw new Exception("Error guardando registro con rebaja definitiva" + lineaInvMov);
							 }		 
						 }
					//}
			 }
		}catch (Exception e) {
			 System.out.println(e.getMessage());
			 throw new Exception("Error guardando las rebajas definitivas "+e.getMessage());
	
		}finally {
			 rsRebajasDef.close();
			 psRebajasDef.close();	 
		 }
	}
	
	
private void RebajasDefinitivasMontoRebajadoRV(int year, int month) throws Exception {

		 /* Buscar Rebajas Definitivas*/		
		 String sqlRebajasDef = "\nSELECT DECODE(INVVIEJO.XX_VCN_INVENTORY_ID,NULL,0,INVVIEJO.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORYVIEJO_ID, " +
		 		"\nDECODE(INVMOVVIEJO.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOVVIEJO.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVVIEJO_ID," +
		 		"\nDR.M_WAREHOUSE_ID, " +
		 		"\nDECODE(DAD.M_PRODUCT_ID,NULL,0,DAD.M_PRODUCT_ID) M_PRODUCT_ID, " +
		 		"\nDECODE(PC.XX_PRICECONSECUTIVE,NULL,0,PC.XX_PRICECONSECUTIVE) PCVIEJO, " +
		 		"\nDECODE(PC.M_ATTRIBUTESETINSTANCE_ID,NULL,0,PC.M_ATTRIBUTESETINSTANCE_ID) LOTE_ID, " + 
		 		"\nSUM(CASE WHEN DAD.XX_DISCOUNTPRICE IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_DISCOUNTPRICE*DAD.XX_LOWERINGQUANTITY ELSE 0 END) MONTOAREBAJAR, " +
		 		"\nSUM(CASE WHEN DAD.XX_PRICEBEFOREDISCOUNT IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_PRICEBEFOREDISCOUNT* DAD.XX_LOWERINGQUANTITY ELSE 0 END) MONTONUEVO, " +
		 		"\nSUM(DAD.XX_LOWERINGQUANTITY) CANTIDAD, " +
		 		"\nDECODE(ASI.PRICEACTUAL,NULL,0.01,ASI.PRICEACTUAL) COSTO, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
		 		"\nFROM XX_VMR_DISCOUNTREQUEST DR " +
		 		"\nINNER JOIN XX_VMR_DISCOUNTAPPLIDETAIL DAD ON " +
		 		"\n(DR.XX_VMR_DISCOUNTREQUEST_ID = DAD.XX_VMR_DISCOUNTREQUEST_ID) " +
		 		"\nLEFT JOIN XX_VMR_PRICECONSECUTIVE PC ON (PC.XX_VMR_PRICECONSECUTIVE_ID = DAD.XX_PRICECONSECUTIVE_ID) " +
		 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DAD.M_PRODUCT_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = PC.M_ATTRIBUTESETINSTANCE_ID) " +
		 		"\nLEFT JOIN XX_VCN_INVENTORY INVVIEJO ON (INVVIEJO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVVIEJO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVVIEJO.XX_CONSECUTIVEPRICE = PC.XX_PRICECONSECUTIVE "  +
				"\nAND INVVIEJO.M_WAREHOUSE_ID = DR.M_WAREHOUSE_ID AND " +
				"\nINVVIEJO.XX_INVENTORYMONTH = "+ month +" AND INVVIEJO.XX_INVENTORYYEAR = "+ year +") " +
				"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOVVIEJO ON (INVMOVVIEJO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOVVIEJO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVMOVVIEJO.XX_CONSECUTIVEPRICE = PC.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOVVIEJO.M_WAREHOUSE_ID = DR.M_WAREHOUSE_ID AND " +
				"\nINVMOVVIEJO.XX_INVENTORYMONTH = "+ month +" AND INVMOVVIEJO.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOVVIEJO.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue()+") " +
		 		"\nWHERE DR.XX_Status = 'RV' " +
		 		"\nAND DAD.XX_LOWERINGQUANTITY IS NOT NULL" +
		 		"\nAND TO_CHAR(DR.CREATED,'YYYY') = " + year +
		 		"\nAND TO_CHAR(DR.CREATED,'MM') = " + month+
		 		"\nGROUP BY INVVIEJO.XX_VCN_INVENTORY_ID," +
		 		"\nINVMOVVIEJO.XX_VCN_INVENTORYMOVDETAIL_ID, ASI.PRICEACTUAL," +
		 		"\nDR.M_WAREHOUSE_ID, DAD.M_PRODUCT_ID, " +
		 		"\nPC.XX_PRICECONSECUTIVE, PC.M_ATTRIBUTESETINSTANCE_ID, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID ";
		 		
		 		
		 
		 PreparedStatement psRebajasDef = null;
		 ResultSet rsRebajasDef = null;	
		 int registroInv, registroInvMov;

		try {
			 System.out.println("\nRebajas Definitivas Reversadas : "+sqlRebajasDef);
			 psRebajasDef = DB.prepareStatement(sqlRebajasDef, null);
			 rsRebajasDef = psRebajasDef.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			 while (rsRebajasDef.next()) {
				 //Registro al que se reversara la rebaja (se suma)
					registroInv = rsRebajasDef.getInt("XX_VCN_INVENTORYVIEJO_ID");
//					if(registroInv<1){
//						 registroInv = existeRegistro(rsRebajasDef.getInt("M_Product_ID"), 
//								 rsRebajasDef.getInt("LOTE_ID"), rsRebajasDef.getInt("PCVIEJO"),
//								 rsRebajasDef.getInt("M_WAREHOUSE_ID"),month ,year);
//					 }
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto del Traspaso
						lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
								add(rsRebajasDef.getBigDecimal("MONTOAREBAJAR")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad del Traspaso
						lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
								add(rsRebajasDef.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();			
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsRebajasDef.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsRebajasDef.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsRebajasDef.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsRebajasDef.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsRebajasDef.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsRebajasDef.getInt("M_WAREHOUSE_ID")); 	//Tienda 
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsRebajasDef.getInt("LOTE_ID"));
						 lineaInventario.setXX_ConsecutivePrice(rsRebajasDef.getBigDecimal("PCVIEJO")); 
						 lineaInventario.setXX_MOVEMENTQUANTITY(rsRebajasDef.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_MOVEMENTAMOUNT(rsRebajasDef.getBigDecimal("MONTOAREBAJAR").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsRebajasDef.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro con rebaja definitiva reversada" + lineaInventario);
						 }		 
					 }
					//Agregar registro a la tabla de detalle de movimiento de rebaja definitiva en el inventario XX_VCN_InventoryMovDetail
					 //Registro al que se quitara la rebaja  (se suma)
					registroInvMov = rsRebajasDef.getInt("XX_VCN_INVENTORYMOVVIEJO_ID");
//					if(registroInvMov<1){
//						 registroInvMov = existeRegistroDetalle(rsRebajasDef.getInt("M_Product_ID"), 
//								 rsRebajasDef.getInt("LOTE_ID"), rsRebajasDef.getInt("PCVIEJO"),
//								 rsRebajasDef.getInt("M_WAREHOUSE_ID"),month ,year,
//								 X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue());
//					 }
					if (registroInvMov > 0) {
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
							//Monto de Rebaja
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								add(rsRebajasDef.getBigDecimal("MONTOAREBAJAR")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Rebaja
						lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
								add(rsRebajasDef.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();			
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(), 0, null);
						 lineaInvMov.setM_Product_ID(rsRebajasDef.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsRebajasDef.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsRebajasDef.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsRebajasDef.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsRebajasDef.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsRebajasDef.getInt("M_WAREHOUSE_ID")); 	//Tienda 
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsRebajasDef.getInt("LOTE_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(rsRebajasDef.getBigDecimal("PCVIEJO")); 
						 lineaInvMov.setXX_MOVEMENTQUANTITY(rsRebajasDef.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsRebajasDef.getBigDecimal("MONTOAREBAJAR").setScale(2, RoundingMode.HALF_EVEN));
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro con rebaja definitiva reversada " + lineaInvMov);
						 }		 
					 }
			 }
		}catch (Exception e) {
			 System.out.println(e.getMessage());
			 throw new Exception("Error guardando las rebajas definitivas reversadas "+e.getMessage());
	
		}finally {
			 rsRebajasDef.close();
			 psRebajasDef.close();	 
		 }
	}
	
	
private void RebajasDefinitivasMontoNuevoRV(int year, int month) throws Exception {

		 /* Buscar Rebajas Definitivas*/		
		 String sqlRebajasDef = "\nSELECT  " +
		 		"\nDECODE(INVNUEVO.XX_VCN_INVENTORY_ID,NULL,0,INVNUEVO.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORYNUEVO_ID," +
		 		"\nDECODE(INVMOVNUEVO.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOVNUEVO.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVNUEVO_ID," +
		 		"\nDR.M_WAREHOUSE_ID, " +
		 		"\nDECODE(DAD.M_PRODUCT_ID,NULL,0,DAD.M_PRODUCT_ID) M_PRODUCT_ID, " +
		 		"\nDECODE(PCNUEVO.XX_PRICECONSECUTIVE,NULL,0,PCNUEVO.XX_PRICECONSECUTIVE) PCNUEVO, " +
		 		"\nDECODE(PCNUEVO.M_ATTRIBUTESETINSTANCE_ID,NULL,0,PCNUEVO.M_ATTRIBUTESETINSTANCE_ID) LOTE_ID, " + 
		 		"\nSUM(CASE WHEN DAD.XX_DISCOUNTPRICE IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_DISCOUNTPRICE*DAD.XX_LOWERINGQUANTITY ELSE 0 END) MONTOAREBAJAR, " +
		 		"\nSUM(CASE WHEN DAD.XX_PRICEBEFOREDISCOUNT IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_PRICEBEFOREDISCOUNT* DAD.XX_LOWERINGQUANTITY ELSE 0 END) MONTONUEVO, " +
		 		"\nSUM(DAD.XX_LOWERINGQUANTITY) CANTIDAD, " +
		 		"\nDECODE(ASI.PRICEACTUAL,NULL,0.01,ASI.PRICEACTUAL) COSTO, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
		 		"\nFROM XX_VMR_DISCOUNTREQUEST DR " +
		 		"\nINNER JOIN XX_VMR_DISCOUNTAPPLIDETAIL DAD ON " +
		 		"\n(DR.XX_VMR_DISCOUNTREQUEST_ID = DAD.XX_VMR_DISCOUNTREQUEST_ID) " +
		 		"\nLEFT JOIN XX_VMR_PRICECONSECUTIVE PCNUEVO ON (PCNUEVO.XX_VMR_PRICECONSECUTIVE_ID = DAD.XX_VMR_PRICECONSECUTIVE_ID) " +
		 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DAD.M_PRODUCT_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = PCNUEVO.M_ATTRIBUTESETINSTANCE_ID) " +
		 		"\nLEFT JOIN XX_VCN_INVENTORY INVNUEVO ON (INVNUEVO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVNUEVO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVNUEVO.XX_CONSECUTIVEPRICE = PCNUEVO.XX_PRICECONSECUTIVE "  +
				"\nAND INVNUEVO.M_WAREHOUSE_ID = DR.M_WAREHOUSE_ID AND " +
				"\nINVNUEVO.XX_INVENTORYMONTH = "+ month +" AND INVNUEVO.XX_INVENTORYYEAR = "+ year +") " +
		 		"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOVNUEVO ON (INVMOVNUEVO.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOVNUEVO.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND " +
				"\nINVMOVNUEVO.XX_CONSECUTIVEPRICE = PCNUEVO.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOVNUEVO.M_WAREHOUSE_ID = DR.M_WAREHOUSE_ID AND " +
				"\nINVMOVNUEVO.XX_INVENTORYMONTH = "+ month +" AND INVMOVNUEVO.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOVNUEVO.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue()+") " +
		 		"\nWHERE DR.XX_Status = 'RV' " +
		 		"\nAND DAD.XX_LOWERINGQUANTITY IS NOT NULL" +
		 		"\nAND TO_CHAR(DR.CREATED,'YYYY') = " + year +
		 		"\nAND TO_CHAR(DR.CREATED,'MM') = " + month+
		 		"\nGROUP BY  INVNUEVO.XX_VCN_INVENTORY_ID, " +
		 		"\nASI.PRICEACTUAL," +
		 		"\nINVMOVNUEVO.XX_VCN_INVENTORYMOVDETAIL_ID, DR.M_WAREHOUSE_ID, DAD.M_PRODUCT_ID, " +
		 		"\nPCNUEVO.XX_PRICECONSECUTIVE, PCNUEVO.M_ATTRIBUTESETINSTANCE_ID, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID "+ 
		 		"\nHAVING SUM(CASE WHEN DAD.XX_PRICEBEFOREDISCOUNT IS NOT NULL AND DAD.XX_LOWERINGQUANTITY IS NOT NULL " +
		 		"\nTHEN DAD.XX_PRICEBEFOREDISCOUNT* DAD.XX_LOWERINGQUANTITY ELSE 0 END) != 0";
		 		
		 		
		 
		 PreparedStatement psRebajasDef = null;
		 ResultSet rsRebajasDef = null;	
		 int registroInv, registroInvMov;

		try {
			 System.out.println("\nRebajas Definitivas Reversadas : "+sqlRebajasDef);
			 psRebajasDef = DB.prepareStatement(sqlRebajasDef, null);
			 rsRebajasDef = psRebajasDef.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;
			 while (rsRebajasDef.next()) {
				 //Nuevo Registro rebajado que se reversara (se resta)	
					//if(rsRebajasDef.getBigDecimal("MONTONUEVO").compareTo(new BigDecimal(0))!=0){
						registroInv = rsRebajasDef.getInt("XX_VCN_INVENTORYNUEVO_ID");
//						if(registroInv<1){
//							 registroInv = existeRegistro(rsRebajasDef.getInt("M_Product_ID"), 
//									 rsRebajasDef.getInt("LOTE_ID"), rsRebajasDef.getInt("PCNUEVO"),
//									 rsRebajasDef.getInt("M_WAREHOUSE_ID"),month ,year);
//						 }
						if (registroInv > 0) {
							lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
								//Monto del Traspaso
							lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
									subtract(rsRebajasDef.getBigDecimal("MONTONUEVO")).setScale(2, RoundingMode.HALF_EVEN));
								//Cantidad del Traspaso
							lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
									subtract(rsRebajasDef.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
							lineaInventario.save();							
						 } else {
							 //Si el producto no existe crearlo con el resultado
							 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
							 lineaInventario.setM_Product_ID(rsRebajasDef.getInt("M_Product_ID"));
							 lineaInventario.setXX_VMR_Category_ID(rsRebajasDef.getInt("XX_VMR_Category_ID"));
							 lineaInventario.setXX_VMR_Department_ID(rsRebajasDef.getInt("XX_VMR_Department_ID"));
							 lineaInventario.setXX_VMR_Line_ID(rsRebajasDef.getInt("XX_VMR_Line_ID"));
							 lineaInventario.setXX_VMR_Section_ID(rsRebajasDef.getInt("XX_VMR_Section_ID"));
							 lineaInventario.setM_Warehouse_ID(rsRebajasDef.getInt("M_WAREHOUSE_ID")); //Tienda Recibidora
							 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
							 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
							 lineaInventario.setM_AttributeSetInstance_ID(rsRebajasDef.getInt("LOTE_ID"));
							 lineaInventario.setXX_ConsecutivePrice(rsRebajasDef.getBigDecimal("PCNUEVO")); 
							 lineaInventario.setXX_MOVEMENTQUANTITY(rsRebajasDef.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
							 lineaInventario.setXX_MOVEMENTAMOUNT(rsRebajasDef.getBigDecimal("MONTONUEVO").setScale(2, RoundingMode.HALF_EVEN).negate());
							 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
							 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
							 lineaInventario.setXX_InitialInventoryCostPrice(rsRebajasDef.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
							 if (!lineaInventario.save()) {
								 throw new Exception("Error guardando registro con rebaja definitiva reversada " + lineaInventario);
							 }		 
						 }
					//}
				//Agregar registro a la tabla de detalle de movimiento de rebaja definitiva en el inventario XX_VCN_InventoryMovDetail
				 //Nuevo Registro rebajado que se reversara (se resta)	
					//if(rsRebajasDef.getBigDecimal("MONTONUEVO").compareTo(new BigDecimal(0))!=0){
						registroInvMov = rsRebajasDef.getInt("XX_VCN_INVENTORYMOVNUEVO_ID");
//						if(registroInvMov<1){
//							 registroInvMov = existeRegistroDetalle(rsRebajasDef.getInt("M_Product_ID"), 
//									 rsRebajasDef.getInt("LOTE_ID"), rsRebajasDef.getInt("PCNUEVO"),
//									 rsRebajasDef.getInt("M_WAREHOUSE_ID"),month ,year,
//									 X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue());
//						 }
						if (registroInvMov > 0) {
							lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
								//Monto del Rebaja
							lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
									subtract(rsRebajasDef.getBigDecimal("MONTONUEVO")).setScale(2, RoundingMode.HALF_EVEN));
								//Cantidad del Rebaja
							lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
									subtract(rsRebajasDef.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
							lineaInvMov.save();							
						 } else {
							 //Si el producto no existe crearlo con el resultado
							 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(), 0, null);
							 lineaInvMov.setM_Product_ID(rsRebajasDef.getInt("M_Product_ID"));
							 lineaInvMov.setXX_VMR_Category_ID(rsRebajasDef.getInt("XX_VMR_Category_ID"));
							 lineaInvMov.setXX_VMR_Department_ID(rsRebajasDef.getInt("XX_VMR_Department_ID"));
							 lineaInvMov.setXX_VMR_Line_ID(rsRebajasDef.getInt("XX_VMR_Line_ID"));
							 lineaInvMov.setXX_VMR_Section_ID(rsRebajasDef.getInt("XX_VMR_Section_ID"));
							 lineaInvMov.setM_Warehouse_ID(rsRebajasDef.getInt("M_WAREHOUSE_ID")); //Tienda Recibidora
							 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
							 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
							 lineaInvMov.setM_AttributeSetInstance_ID(rsRebajasDef.getInt("LOTE_ID"));
							 lineaInvMov.setXX_ConsecutivePrice(rsRebajasDef.getBigDecimal("PCNUEVO")); 
							 lineaInvMov.setXX_MOVEMENTQUANTITY(rsRebajasDef.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
							 lineaInvMov.setXX_MOVEMENTAMOUNT(rsRebajasDef.getBigDecimal("MONTONUEVO").setScale(2, RoundingMode.HALF_EVEN).negate());
							 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue());
							 if (!lineaInvMov.save()) {
								 throw new Exception("Error guardando registro con rebaja definitiva reversada " + lineaInvMov);
							 }		 
						 }
					//}
			 }
		}catch (Exception e) {
			 System.out.println(e.getMessage());
			 throw new Exception("Error guardando las rebajas definitivas reversadas "+e.getMessage());
	
		}finally {
			 rsRebajasDef.close();
			 psRebajasDef.close();	 
		 }
	}
	
	private void Devoluciones(int year, int month) throws Exception {
		 // Buscar Devoluciones	
		 String sqlDevoluciones = "\nSELECT DECODE(INV.XX_VCN_INVENTORY_ID,NULL,0,INV.XX_VCN_INVENTORY_ID) XX_VCN_INVENTORY_ID," +
		 		"\nDECODE(INVMOV.XX_VCN_INVENTORYMOVDETAIL_ID,NULL,0,INVMOV.XX_VCN_INVENTORYMOVDETAIL_ID) XX_VCN_INVENTORYMOVDETAIL_ID, " +
		 		"\nDECODE(ML.M_PRODUCT_ID,NULL,0,ML.M_PRODUCT_ID) M_PRODUCT_ID, " +
		 		"\nDECODE(ML.M_ATTRIBUTESETINSTANCE_ID,NULL,0,ML.M_ATTRIBUTESETINSTANCE_ID) LOTE_ID, " +
		 		"\nM.M_WAREHOUSEFROM_ID M_WAREHOUSE_ID, " +
		 		"\nSUM(ML.XX_QUANTITYRECEIVED) CANTIDAD," +
		 		"\nSUM(CASE WHEN ML.XX_SALEPRICE IS NOT NULL " +
		 		"\nTHEN ML.XX_SALEPRICE*ML.XX_QUANTITYRECEIVED ELSE 0.01 END) MONTO, " +
		 		"\n(CASE WHEN ASI.PRICEACTUAL IS NOT NULL THEN ASI.PRICEACTUAL ELSE 0.01 END) COSTO, " +
		 		"\nDECODE(ML.XX_PRICECONSECUTIVE,NULL,0,ML.XX_PRICECONSECUTIVE) CONSECUTIVOPRECIO, " +
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID " +
		 		"\nFROM M_MOVEMENT M " +
		 		"\nINNER JOIN M_MOVEMENTLINE ML ON (ML.M_MOVEMENT_ID = M.M_MOVEMENT_ID) " +
		 		"\nLEFT JOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = ML.M_ATTRIBUTESETINSTANCE_ID) " +
		 		"\nLEFT JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = ML.M_PRODUCT_ID) " +			
				"\nINNER JOIN XX_VLO_DETAILDISPATCHGUIDE DD ON (DD.M_MOVEMENTR_ID = M.M_MOVEMENT_ID) " +
		 		"\nINNER JOIN XX_VLO_DISPATCHGUIDE D ON (D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID) " +
		 		"\nINNER JOIN XX_VLO_RETURNOFPRODUCT RP ON (RP.M_MOVEMENT_ID = M.M_MOVEMENT_ID) " +
		 		"\nLEFT JOIN XX_VCN_INVENTORY INV ON (INV.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INV.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INV.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE " +
				"\nAND INV.M_WAREHOUSE_ID = M.M_WAREHOUSEFROM_ID AND INV.XX_INVENTORYMONTH = "+ month +" AND INV.XX_INVENTORYYEAR = "+ year +") " +
				"\nLEFT JOIN XX_VCN_INVENTORYMOVDETAIL INVMOV ON (INVMOV.M_PRODUCT_ID = P.M_PRODUCT_ID AND " +
				"\nNVL(INVMOV.M_ATTRIBUTESETINSTANCE_ID,0) = NVL(ASI.M_ATTRIBUTESETINSTANCE_ID,0) AND INVMOV.XX_CONSECUTIVEPRICE = ML.XX_PRICECONSECUTIVE "  +
				"\nAND INVMOV.M_WAREHOUSE_ID = M.M_WAREHOUSEFROM_ID AND INVMOV.XX_INVENTORYMONTH = "+ month +" AND INVMOV.XX_INVENTORYYEAR = "+ year +" " +
				"\nAND INVMOV.XX_MOVEMENTTYPE = "+X_Ref_XX_MovementType.DEVOLUCIÓN.getValue()+") " +
		 		"\nWHERE M.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
		 		"\nAND ML.XX_QUANTITYRECEIVED >0 AND ML.XX_QUANTITYRECEIVED IS NOT NULL "+
				"\nAND D.XX_DISPATCHGUIDESTATUS = 'TIE' " +
		 		"\nAND M.XX_Status = 'AC' "+
		 		"\nAND TO_CHAR(RP.CREATED,'YYYY') = " + year +
		 		"\nAND TO_CHAR(RP.CREATED,'MM') = " + month +
		 		"\nAND M.C_DOCTYPE_ID = "+DocTypeDevolucion+
		 		"\nAND DD.XX_TYPEDETAILDISPATCHGUIDE = 'DEV' "+
		 		"\nGROUP BY INV.XX_VCN_INVENTORY_ID,INVMOV.XX_VCN_INVENTORYMOVDETAIL_ID,ML.M_PRODUCT_ID,M.M_WAREHOUSEFROM_ID, "+
		 		"\nASI.PRICEACTUAL, ML.M_ATTRIBUTESETINSTANCE_ID, ML.XX_PRICECONSECUTIVE,  "+
		 		"\nP.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID,  P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID";

		 PreparedStatement psDevoluciones = null;
		 ResultSet rsDevoluciones = null;	
		 int registroInv, registroInvMov;
		try {
			 System.out.println("\nDevoluciones: "+sqlDevoluciones);
			 psDevoluciones= DB.prepareStatement(sqlDevoluciones, null);
			 rsDevoluciones = psDevoluciones.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 X_XX_VCN_InventoryMovDetail lineaInvMov = null;

			 while (rsDevoluciones.next()) {
				 //Devolución Saliente (se resta)
					registroInv = rsDevoluciones.getInt("XX_VCN_INVENTORY_ID");
//					if(registroInv<1){
//						 registroInv = existeRegistro(rsDevoluciones.getInt("M_Product_ID"), 
//								 rsDevoluciones.getInt("LOTE_ID"), rsDevoluciones.getInt("CONSECUTIVOPRECIO"),
//								 rsDevoluciones.getInt("M_WAREHOUSE_ID"),month ,year);
//					 }
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto de Devolucion
						lineaInventario.setXX_MOVEMENTAMOUNT(lineaInventario.getXX_MOVEMENTAMOUNT().
								subtract(rsDevoluciones.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Devolucion
						lineaInventario.setXX_MOVEMENTQUANTITY(lineaInventario.getXX_MOVEMENTQUANTITY().
								subtract(rsDevoluciones.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();						
					 } else {
						 //Si el producto no existe crearlo con el resultado
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(rsDevoluciones.getInt("M_Product_ID"));
						 lineaInventario.setXX_VMR_Category_ID(rsDevoluciones.getInt("XX_VMR_Category_ID"));
						 lineaInventario.setXX_VMR_Department_ID(rsDevoluciones.getInt("XX_VMR_Department_ID"));
						 lineaInventario.setXX_VMR_Line_ID(rsDevoluciones.getInt("XX_VMR_Line_ID"));
						 lineaInventario.setXX_VMR_Section_ID(rsDevoluciones.getInt("XX_VMR_Section_ID"));
						 lineaInventario.setM_Warehouse_ID(rsDevoluciones.getInt("M_WAREHOUSE_ID")); 	//Tienda 
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(rsDevoluciones.getInt("LOTE_ID"));
						 lineaInventario.setXX_ConsecutivePrice(rsDevoluciones.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInventario.setXX_MOVEMENTQUANTITY(rsDevoluciones.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_MOVEMENTAMOUNT(rsDevoluciones.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(rsDevoluciones.getBigDecimal("COSTO").setScale(2, BigDecimal.ROUND_HALF_UP));
						 if (!lineaInventario.save()) {
							 throw new Exception("Error guardando registro con devolucion " + lineaInventario);
						 }		 
					 }

					//Agregar registro a la tabla de detalle de movimiento en el inventario XX_VCN_InventoryMovDetail
					 registroInvMov = rsDevoluciones.getInt("XX_VCN_INVENTORYMOVDETAIL_ID");
//						if(registroInvMov<1){
//							 registroInvMov = existeRegistroDetalle(rsDevoluciones.getInt("M_Product_ID"), 
//									 rsDevoluciones.getInt("LOTE_ID"), rsDevoluciones.getInt("CONSECUTIVOPRECIO"),
//									 rsDevoluciones.getInt("M_WAREHOUSE_ID"),month ,year,
//									 X_Ref_XX_MovementType.DEVOLUCIÓN.getValue());
//						 }
					if(registroInvMov>0){
						lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(),registroInvMov, null);
						//Monto de Devolucion
						lineaInvMov.setXX_MOVEMENTAMOUNT(lineaInvMov.getXX_MOVEMENTAMOUNT().
								subtract(rsDevoluciones.getBigDecimal("MONTO")).setScale(2, RoundingMode.HALF_EVEN));
						//Cantidad de Devolucion
						lineaInvMov.setXX_MOVEMENTQUANTITY(lineaInvMov.getXX_MOVEMENTQUANTITY().
								subtract(rsDevoluciones.getBigDecimal("CANTIDAD")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInvMov.save();					
					 } else {
						 //Si el registro no existe crearlo con el resultado
						 lineaInvMov = new X_XX_VCN_InventoryMovDetail(getCtx(), 0, null);
						 lineaInvMov.setM_Product_ID(rsDevoluciones.getInt("M_Product_ID"));
						 lineaInvMov.setXX_VMR_Category_ID(rsDevoluciones.getInt("XX_VMR_Category_ID"));
						 lineaInvMov.setXX_VMR_Department_ID(rsDevoluciones.getInt("XX_VMR_Department_ID"));
						 lineaInvMov.setXX_VMR_Line_ID(rsDevoluciones.getInt("XX_VMR_Line_ID"));
						 lineaInvMov.setXX_VMR_Section_ID(rsDevoluciones.getInt("XX_VMR_Section_ID"));
						 lineaInvMov.setM_Warehouse_ID(rsDevoluciones.getInt("M_Warehouse_ID"));
						 lineaInvMov.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInvMov.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInvMov.setM_AttributeSetInstance_ID(rsDevoluciones.getInt("LOTE_ID"));
						 lineaInvMov.setXX_ConsecutivePrice(rsDevoluciones.getBigDecimal("CONSECUTIVOPRECIO")); 
						 lineaInvMov.setXX_MOVEMENTQUANTITY(rsDevoluciones.getBigDecimal("CANTIDAD").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MOVEMENTAMOUNT(rsDevoluciones.getBigDecimal("MONTO").setScale(2, RoundingMode.HALF_EVEN).negate());
						 lineaInvMov.setXX_MovementType(X_Ref_XX_MovementType.DEVOLUCIÓN.getValue());
						 if (!lineaInvMov.save()) {
							 throw new Exception("Error guardando registro de detalle de movimiento de devolucion " + lineaInvMov);
						 }		 
					}
			 }
		}catch (Exception e) {
			 System.out.println(e.getMessage());
			 throw new Exception("Error guardando las devoluviones "+e.getMessage());
	
		}finally {
			 rsDevoluciones.close();
			 psDevoluciones.close();	 
		 }
		
	}
	
	/** Borra el inventario del mes seleccionado */
	public void borrarInventarioMensual(int month, int year) throws Exception {
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VCN_INVENTORY WHERE XX_INVENTORYYEAR = " + year + " AND XX_INVENTORYMONTH = " + month;
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error borrando inventario mensual" +e.getMessage());
		}finally{
			psDelete.close();
		}
		
	}
	
	/** Borra el inventario del mes seleccionado */
	public void borrarInventarioAcumulado(int month, int year) throws Exception {
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VCN_INVENTORY WHERE XX_INVENTORYYEAR = " + year + " AND XX_INVENTORYMONTH = " + month+
			" AND XX_INITIALINVENTORYQUANTITY = 0 AND XX_INITIALINVENTORYAMOUNT = 0";
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error borrando inventario mensual" +e.getMessage());
		}finally{
			psDelete.close();
		}
		
	}
	
	/** Borra el detalle de movimiento del inventario del mes seleccionado */
	public void borrarDetalleInvMensual(int month, int year) throws Exception {
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VCN_INVENTORYMOVDETAIL WHERE XX_INVENTORYYEAR = " + year + " AND XX_INVENTORYMONTH = " + month;
	
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error borrando detalle de movimientos de inventario mensual" +e.getMessage());
		}finally{
			psDelete.close();
		}
		
	}
	
	/** Actualiza costos faltantes en productos del inventario del mes seleccionado */
	public void calcularCostosFaltantes(int month, int year) throws Exception {
		PreparedStatement psUpdate =null;
		try {
			String sqlUpdate = "\nUPDATE XX_VCN_INVENTORY I SET I.XX_INITIALINVENTORYCOSTPRICE = "+
				"\nNVL((SELECT MAX(PC.XX_UNITPURCHASEPRICE) "+
					"\nFROM  XX_VMR_PRICECONSECUTIVE PC  " +
					"\nWHERE PC.XX_PRICECONSECUTIVE =I.XX_CONSECUTIVEPRICE " +
					"\nAND PC.M_PRODUCT_ID = I.M_PRODUCT_ID),0.01) "+
				"\nWHERE (I.XX_INITIALINVENTORYCOSTPRICE IS NULL OR I.XX_INITIALINVENTORYCOSTPRICE = 0.01) "+
				"\nAND I.XX_INVENTORYYEAR = " + year + " AND I.XX_INVENTORYMONTH = " + month;
	
			psUpdate = DB.prepareStatement(sqlUpdate,get_TrxName());
			psUpdate.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error actualizando costos faltantes de inventario mensual" +e.getMessage());
		}finally{
			psUpdate.close();
		}
		
		try {
			String sqlUpdate = "\nUPDATE XX_VCN_INVENTORY I SET I.XX_INITIALINVENTORYCOSTPRICE = "+
				"\nNVL((SELECT MAX(PC.XX_UNITPURCHASEPRICE) "+
					"\nFROM  XX_VMR_PRICECONSECUTIVE PC  " +
					"\nWHERE I.M_ATTRIBUTESETINSTANCE_ID = PC.M_ATTRIBUTESETINSTANCE_ID " +
					"\nAND PC.M_PRODUCT_ID = I.M_PRODUCT_ID),0.01) "+
				"\nWHERE (I.XX_INITIALINVENTORYCOSTPRICE IS NULL OR I.XX_INITIALINVENTORYCOSTPRICE = 0.01) "+
				"\nAND I.XX_INVENTORYYEAR = " + year + " AND I.XX_INVENTORYMONTH = " + month;
	
			psUpdate = DB.prepareStatement(sqlUpdate,get_TrxName());
			psUpdate.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error actualizando costos faltantes de inventario mensual" +e.getMessage());
		}finally{
			psUpdate.close();
		}
		
		try {
			String sqlUpdate ="\nupdate XX_VCN_INVENTORY I SET I.XX_INITIALINVENTORYCOSTPRICE = "+
					"\n(select priceactual from M_ATTRIBUTESETINSTANCE asi where I.M_ATTRIBUTESETINSTANCE_ID = asi.M_ATTRIBUTESETINSTANCE_ID) "+
					"\nWHERE (I.XX_INITIALINVENTORYCOSTPRICE IS NULL OR I.XX_INITIALINVENTORYCOSTPRICE = 0.01) and "+
					"\nI.XX_INVENTORYYEAR = " + year + " AND I.XX_INVENTORYMONTH = " + month;
			
			psUpdate = DB.prepareStatement(sqlUpdate,get_TrxName());
			psUpdate.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error actualizando costos faltantes de inventario mensual" +e.getMessage());
		}finally{
			psUpdate.close();
		}
	}
	
	private int existeRegistro(int productID, int loteID, Integer priceConsecutive, Integer warehouseID, int month, int year) throws Exception{
		
		PreparedStatement ps =null; 
		ResultSet rs =null;
		int resultado = 0;
		try {
			String sql = "\nSELECT XX_VCN_INVENTORY_ID FROM XX_VCN_INVENTORY I "+
					"\nWHERE I.XX_CONSECUTIVEPRICE = " + priceConsecutive +
					"\nAND I.M_PRODUCT_ID = " + productID +
					"\nAND NVL(I.M_ATTRIBUTESETINSTANCE_ID,0) = " + loteID +
					"\nAND I.M_WAREHOUSE_ID = " + warehouseID +
					"\nAND I.XX_INVENTORYYEAR = " + year + " AND I.XX_INVENTORYMONTH = " + month;
			
			//System.out.println("Existe registro "+sql);
			
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if(rs.next()){
				resultado =rs.getInt("XX_VCN_INVENTORY_ID");
			}
				
		}catch (Exception e) {
			 throw new Exception("Error buscando si existe registro " +e.getMessage());
		}finally{
			 rs.close();
			 ps.close();	 
		}
		return resultado;
	}
	
	private int existeRegistroDetalle(int productID, int loteID, Integer priceConsecutive, Integer warehouseID,
			int month, int year, String movType) throws Exception{
		PreparedStatement ps =null; 
		ResultSet rs =null;
		int resultado = 0;
		try {
			String sql = "\nSELECT XX_VCN_INVENTORYMOVDETAIL_ID FROM XX_VCN_INVENTORYMOVDETAIL I "+
					"\nWHERE I.XX_CONSECUTIVEPRICE = " + priceConsecutive +
					"\nAND I.M_PRODUCT_ID = " + productID +
					"\nAND NVL(I.M_ATTRIBUTESETINSTANCE_ID,0) = " + loteID +
					"\nAND I.XX_MOVEMENTTYPE = " + movType +
					"\nAND I.M_WAREHOUSE_ID = " + warehouseID +
					"\nAND I.XX_INVENTORYYEAR = " + year + " AND I.XX_INVENTORYMONTH = " + month;
			
			System.out.println("Existe registro detalle "+sql);
			
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if(rs.next()){
				resultado =rs.getInt("XX_VCN_INVENTORY_ID");
			}
				
		}catch (Exception e) {
			 throw new Exception("Error buscando si existe registro de detalle " +e.getMessage());
		}finally{
			 rs.close();
			 ps.close();	 
		}
		return resultado;
	}
	
	private void fillXX_VCN_Inventory(int month, int year) throws Exception{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL_Compiere="";
    	
		SQL_Compiere = "SELECT NVL(WAR.VALUE,0) AS TIENDA,"
       		 + "NVL(CAT.VALUE,0) AS CODCAT, NVL(DEP.VALUE,0) AS CODDEP, NVL(LIN.VALUE,0) AS CODLIN, NVL(SEC.VALUE,0) AS CODSEC, "
       		 + "NVL(PRO.VALUE,0) AS CODPRO, " 
       		 + "(CASE WHEN XX_CONSECUTIVEPRICE = 0 THEN TO_NUMBER(NVL(ATT.LOT,0)) ELSE XX_CONSECUTIVEPRICE END) AS CONPRE, "  
       		 + "XX_INVENTORYMONTH AS MESINV, XX_INVENTORYYEAR AS AÑOINV, "  
       		 + "SUM(XX_INITIALINVENTORYQUANTITY + XX_SHOPPINGQUANTITY  + XX_MOVEMENTQUANTITY - XX_SALESQUANTITY) CANTIDAD, "
       		 + "ROUND(SUM(XX_INITIALINVENTORYAMOUNT + XX_SHOPPINGAMOUNT  + XX_MOVEMENTAMOUNT - XX_SALESAMOUNT), 2) AS MONTO, "
       		 + "SUM(XX_ADJUSTMENTSQUANTITY) AS CANTAJUSTE, SUM(XX_ADJUSTMENTSAMOUNT) MONTOAJUSTE "    
       		 + "FROM "
         	 + "XX_VCN_Inventory inv JOIN XX_VMR_DEPARTMENT dep ON (inv.XX_VMR_DEPARTMENT_ID = dep.XX_VMR_DEPARTMENT_ID) "
			 + "JOIN M_WAREHOUSE war ON (inv.M_WAREHOUSE_ID = war.M_WAREHOUSE_ID) "
			 + "JOIN XX_VMR_CATEGORY cat ON (inv.XX_VMR_CATEGORY_ID = cat.XX_VMR_CATEGORY_ID) "
			 + "JOIN XX_VMR_LINE lin ON (inv.XX_VMR_LINE_ID = lin.XX_VMR_LINE_ID) "
			 + "JOIN XX_VMR_SECTION sec ON (inv.XX_VMR_SECTION_ID = sec.XX_VMR_SECTION_ID) "
			 + "JOIN M_PRODUCT pro ON (inv.M_PRODUCT_ID = pro.M_PRODUCT_ID) " 
			 + "LEFT JOIN M_ATTRIBUTESETINSTANCE att ON (att.M_ATTRIBUTESETINSTANCE_ID = inv.M_ATTRIBUTESETINSTANCE_ID) "
			 + "WHERE XX_INVENTORYMONTH = "+month+" AND XX_INVENTORYYEAR = "+year
			 + " GROUP BY NVL(WAR.VALUE,0),"
       		 + "NVL(CAT.VALUE,0), NVL(DEP.VALUE,0), NVL(LIN.VALUE,0), NVL(SEC.VALUE,0), NVL(PRO.VALUE,0), "
       		 + "(CASE WHEN XX_CONSECUTIVEPRICE = 0 THEN TO_NUMBER(NVL(ATT.LOT,0)) ELSE XX_CONSECUTIVEPRICE END), " 
       		 + "XX_INVENTORYMONTH, XX_INVENTORYYEAR ";
		ps = DB.prepareStatement(SQL_Compiere,null);
		System.out.println(SQL_Compiere);
    	try{
			rs = ps.executeQuery();
		
			int i=0;
			while(rs.next()){
				X_E_XX_VCN_INVD53 INVD53 = new X_E_XX_VCN_INVD53(getCtx(), 0, null);
		      	INVD53.setM_Warehouse_ID(rs.getInt("TIENDA"));
	        	INVD53.setXX_VMR_Category_ID(rs.getInt("CODCAT"));
	        	INVD53.setXX_VMR_Department_ID(rs.getInt("CODDEP"));
	        	INVD53.setXX_VMR_Line_ID(rs.getInt("CODLIN"));
	        	INVD53.setXX_VMR_Section_ID(rs.getInt("CODSEC"));
	        	INVD53.setM_Product_ID(rs.getInt("CODPRO"));
	        	INVD53.setXX_INVENTORYMONTH(month);
	        	INVD53.setXX_INVENTORYYEAR(year);
	        	INVD53.setXX_ADJUSTMENTSQUANTITY(rs.getInt("CANTAJUSTE"));
	        	INVD53.setXX_AdjustmentsAmount(rs.getBigDecimal("MONTOAJUSTE"));
	        	INVD53.setXX_ConsecutivePrice(rs.getInt("CONPRE"));
	        	INVD53.setQty(rs.getInt("CANTIDAD"));
	        	INVD53.setXX_Amount(rs.getBigDecimal("MONTO"));
	        	INVD53.setXX_Status(UbicacionTienda);
	        	INVD53.setName(" ");
	        	INVD53.save();
			}
    	}catch (Exception e) {
    		throw new Exception("Error llenando tabla E_XX_VCN_INVD53: " +e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);	
		}
	}

	private void updateDDPlacedOrders(int month, int year) throws Exception{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL_Compiere="";
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, 1);
		
		SQL_Compiere = "\nSELECT  NVL(E_XX_VCN_INVD53_ID,0) INVD53_ID, NVL(WAR.VALUE,0) AS TIENDA," + 
		"NVL(CAT.VALUE,0) AS CODCAT, NVL(DEP.VALUE,0) AS CODDEP, NVL(LIN.VALUE,0) AS CODLIN, NVL(SEC.VALUE,0) AS CODSEC, " +
		"NVL(P.VALUE,0) AS CODPRO, " +
 		"\nSUM(DE.XX_PRODUCTQUANTITY) CANTIDAD, " +
 		"\nSUM(CASE WHEN DE.XX_SALEPRICE IS NOT NULL THEN DE.XX_SALEPRICE*DE.XX_PRODUCTQUANTITY " +
 		"\nELSE 0.01*DE.XX_PRODUCTQUANTITY END) MONTO, " +
 		"\nNVL(DE.XX_PRICECONSECUTIVE,0) CONPRE, " +
 		"\nINV.XX_AMOUNT MONTOTIENDA, " +
 		"\nINV.Qty CANTIENDA, " +
 		"\nINV.XX_ADJUSTMENTSAMOUNT MONTOAJUSTE, " +
 		"\nINV.XX_ADJUSTMENTSQUANTITY CANTAJUSTE " +
 		"\nFROM XX_VMR_ORDER PE " +
 		"\nJOIN XX_VMR_ORDERREQUESTDETAIL DE ON (DE.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID) " +
		"\nJOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DE.M_PRODUCT_ID) " +
 		"\nJOIN C_ORDER O ON (O.C_ORDER_ID = PE.C_ORDER_ID)" +
		"\nJOIN XX_VMR_DEPARTMENT DEP ON (P.XX_VMR_DEPARTMENT_ID = DEP.XX_VMR_DEPARTMENT_ID) " +
		"\nJOIN M_WAREHOUSE WAR ON (PE.M_WAREHOUSE_ID = WAR.M_WAREHOUSE_ID) " +
		"\nJOIN XX_VMR_CATEGORY CAT ON (P.XX_VMR_CATEGORY_ID = CAT.XX_VMR_CATEGORY_ID) " +
		"\nJOIN XX_VMR_LINE LIN ON (P.XX_VMR_LINE_ID = LIN.XX_VMR_LINE_ID) " +
		"\nJOIN XX_VMR_SECTION SEC ON (P.XX_VMR_SECTION_ID = SEC.XX_VMR_SECTION_ID) " +
 		"\nLEFT JOIN E_XX_VCN_INVD53 INV ON (INV.M_PRODUCT_ID = P.VALUE " +
		"\nAND INV.XX_CONSECUTIVEPRICE = NVL(DE.XX_PRICECONSECUTIVE,0) "  +
		"\nAND INV.M_WAREHOUSE_ID = WAR.VALUE AND INV.XX_INVENTORYMONTH = "+ month +" AND INV.XX_INVENTORYYEAR = "+ year +"and xx_status = 3) " +	
 		"\nWHERE  O.ISSOTRX = 'N' AND PE.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
 		"\nAND (PE.XX_ORDERREQUESTSTATUS = '"+X_Ref_XX_VMR_OrderStatus.EN_TRÁNSITO.getValue()+"' " +
 				"\nOR TO_CHAR(PE.XX_DATESTATUSONSTORE,'YYYYMM') > " + new SimpleDateFormat("yyyyMM").format(cal.getTime()) + ")" +
 		"\nAND PE.XX_OrderRequestType = '" + X_Ref_XX_OrderRequestType.DESPACHO_DIRECTO.getValue() +"'"+
 		"\nAND TO_CHAR(O.XX_CHECKUPDATE, 'YYYYMM') >= 201304 AND TO_CHAR(O.XX_CHECKUPDATE, 'YYYYMM') <= " + new SimpleDateFormat("yyyyMM").format(cal.getTime()) +
 		"\nGROUP BY  NVL(E_XX_VCN_INVD53_ID,0) , NVL(WAR.VALUE,0)," +
        "\nNVL(CAT.VALUE,0), NVL(DEP.VALUE,0), NVL(LIN.VALUE,0), NVL(SEC.VALUE,0), NVL(P.VALUE,0), "+ 
 		"\nNVL(DE.XX_PRICECONSECUTIVE,0), XX_INVENTORYMONTH, XX_INVENTORYYEAR,INV.XX_AMOUNT,INV.QTY, INV.XX_ADJUSTMENTSAMOUNT, INV.XX_ADJUSTMENTSQUANTITY ";

		ps = DB.prepareStatement(SQL_Compiere,null);
    	try{
			rs = ps.executeQuery();
			System.out.println(SQL_Compiere);
			while(rs.next()){
				if(rs.getInt("INVD53_ID") > 0){
					int can= rs.getInt("CANTIENDA")-rs.getInt("CANTIDAD")+rs.getInt("CANTAJUSTE");
					BigDecimal monto =  rs.getBigDecimal("MONTOTIENDA").subtract(rs.getBigDecimal("MONTO").add(rs.getBigDecimal("MONTOAJUSTE")));
					X_E_XX_VCN_INVD53 INVD53OLD = new X_E_XX_VCN_INVD53(getCtx(), rs.getInt("INVD53_ID"), null);
					System.out.println("ID: " +INVD53OLD.get_ID());
					if(can != 0 || !monto.equals(new BigDecimal(0))){
					 	INVD53OLD.setQty(can);
			        	INVD53OLD.setXX_Amount(monto);
					}else {
						System.out.println("borrando id : " +rs.getInt("INVD53_ID"));
						System.out.println("PRODUCTO: "+rs.getInt("CODPRO")+", TIENDA:"+rs.getInt("TIENDA")+", CONPRE: "+rs.getInt("CONPRE"));
						INVD53OLD.delete(true);
					}
					X_E_XX_VCN_INVD53 INVD53 = new X_E_XX_VCN_INVD53(getCtx(), 0, null);
			      	INVD53.setM_Warehouse_ID(rs.getInt("TIENDA"));
		        	INVD53.setXX_VMR_Category_ID(rs.getInt("CODCAT"));
		        	INVD53.setXX_VMR_Department_ID(rs.getInt("CODDEP"));
		        	INVD53.setXX_VMR_Line_ID(rs.getInt("CODLIN"));
		        	INVD53.setXX_VMR_Section_ID(rs.getInt("CODSEC"));
		        	INVD53.setM_Product_ID(rs.getInt("CODPRO"));
		        	INVD53.setXX_INVENTORYMONTH(month);
		        	INVD53.setXX_INVENTORYYEAR(year);
		        	INVD53.setXX_ADJUSTMENTSQUANTITY(rs.getInt("CANTAJUSTE"));
		        	INVD53.setXX_AdjustmentsAmount(rs.getBigDecimal("MONTOAJUSTE"));
		        	INVD53.setXX_ConsecutivePrice(rs.getInt("CONPRE"));
		        	INVD53.setQty(rs.getInt("CANTIDAD"));
		        	INVD53.setXX_Amount(rs.getBigDecimal("MONTO"));
		        	INVD53.setXX_Status(UbicacionTransito);
		        	INVD53.setName(" ");
		        	INVD53.save();
		        	System.out.println("Creando id : " +INVD53.get_ID());
					System.out.println("PRODUCTO: "+rs.getInt("CODPRO")+", TIENDA:"+rs.getInt("TIENDA")+", CONPRE: "+rs.getInt("CONPRE"));
				}else{
					System.out.println("NO EXISTE PRODUCTO: "+rs.getInt("CODPRO")+", TIENDA:"+rs.getInt("TIENDA")+", CONPRE: "+rs.getInt("CONPRE"));
				}
				
			}
    	}catch (Exception e) {
    		throw new Exception("Error actualizando tabla E_XX_VCN_INVD53: " +e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);	
		}
	}
	
	/** Borra el inventario del mes seleccionado */
	public void deleteInv(int month, int year) throws Exception {
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM E_XX_VCN_INVD53 WHERE XX_INVENTORYYEAR = " + year + " AND XX_INVENTORYMONTH = " + month;
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error borrando inventario mensual" +e.getMessage());
		}finally{
			psDelete.close();
		}
		
	}
	
	
	private void callProcess(int processID)
	{
		
		MPInstance mpi = new MPInstance( Env.getCtx(), processID, 0); 
		mpi.save();
		ProcessInfo pi = new ProcessInfo("", processID); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(processID); 
		pi.setClassName(""); 
		pi.setTitle(""); 
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
	
	}//
	
}
