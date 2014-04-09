package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.X_E_XX_VCN_INVD53;
import compiere.model.cds.X_E_XX_VCN_INVM14;

/*
* Proceso que sincroniza los registros relacionados con inventario de productos de Compiere
* a las tablas E_XX_VCN_INVM14 y E_XX_VCN_INVD53.
* 
* @author Claudia Afonso.
*/

public class XX_ExportInventory extends SvrProcess{

	private X_E_XX_VCN_INVM14 INVM14 = null;
	private X_E_XX_VCN_INVD53 INVD53 = null;
	
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		
		String sqlConsulta = null;
		PreparedStatement pstmtConsulta = null;
		ResultSet rsConsulta = null;
		
		String statusINVM14, statusINVD53;
		
		Integer codigoTienda, codigoCategoria, codigoDepartamento, codigoSeccion, codigoProducto, consecPrecio, codigoLinea, 
				mesInventario, añoInventario, cantInventInicial, cantidadCompras, cantidadVentas, cantidadMovimientos,
				cantidadAjustes, cantidadAjustesAnt, codigoUbicacion, cantidad;
		Integer IDtienda = null, IDcategoria = null, IDdepartamento = null, IDseccion = null, IDlinea = null, IDproducto = null, IDinventory = null;
						
		BigDecimal montoInventInicial = new BigDecimal(0.000);
		BigDecimal montoCompras = new BigDecimal(0.000);
		BigDecimal montoVentas = new BigDecimal(0.000);
		BigDecimal montoMovimientos = new BigDecimal(0.000);
		BigDecimal montoAjustes = new BigDecimal(0.000);
		BigDecimal montoAjustesAnt = new BigDecimal(0.000);
		BigDecimal monto = new BigDecimal(0.000);
		
		sql = "select M_WAREHOUSE_ID, XX_VMR_Category_ID, XX_VMR_Department_ID, XX_VMR_Line_ID, XX_VMR_Section_ID, " +
				"M_PRODUCT_ID, XX_CONSECUTIVEPRICE, XX_INVENTORYMONTH, XX_INVENTORYYEAR, XX_INITIALINVENTORYQUANTITY, " +
				"XX_INITIALINVENTORYAMOUNT, XX_SHOPPINGQUANTITY, XX_SHOPPINGAMOUNT, XX_SALESQUANTITY, XX_SALESAMOUNT, " +
				"XX_MOVEMENTQUANTITY, XX_MOVEMENTAMOUNT, XX_ADJUSTMENTSQUANTITY, XX_AdjustmentsAmount, " +
				"XX_PREVIOUSADJUSTMENTSQUANTITY, XX_PREVIOUSADJUSTMENTSAMOUNT, XX_VCN_INVENTORY_ID, XX_SyncronizedToINVD53, XX_Synchronized " +
				"from XX_VCN_INVENTORY " +
				"where XX_SYNCHRONIZED = 'N'";  

		pstmt = DB.prepareStatement(sql, null);
		
		try {
			
			rs = pstmt.executeQuery();
						
			while (rs.next()){
				
				codigoTienda = rs.getInt("M_WAREHOUSE_ID");
				codigoCategoria = rs.getInt("XX_VMR_Category_ID");
				codigoDepartamento = rs.getInt("XX_VMR_Department_ID");
				codigoSeccion = rs.getInt("XX_VMR_Section_ID");
				codigoLinea = rs.getInt("XX_VMR_Line_ID");
				codigoProducto = rs.getInt("M_PRODUCT_ID");
				consecPrecio = rs.getInt("XX_CONSECUTIVEPRICE");
				mesInventario = rs.getInt("XX_INVENTORYMONTH");
				añoInventario = rs.getInt("XX_INVENTORYYEAR");
				cantInventInicial = rs.getInt("XX_INITIALINVENTORYQUANTITY");
				montoInventInicial = rs.getBigDecimal("XX_INITIALINVENTORYAMOUNT");
				cantidadCompras = rs.getInt("XX_SHOPPINGQUANTITY");
				montoCompras = rs.getBigDecimal("XX_SHOPPINGAMOUNT");
				cantidadVentas = rs.getInt("XX_SALESQUANTITY");
				montoVentas = rs.getBigDecimal("XX_SALESAMOUNT");
				cantidadMovimientos = rs.getInt("XX_MOVEMENTQUANTITY");
				montoMovimientos = rs.getBigDecimal("XX_MOVEMENTAMOUNT");
				cantidadAjustes = rs.getInt("XX_ADJUSTMENTSQUANTITY");
				montoAjustes = rs.getBigDecimal("XX_AdjustmentsAmount");
				cantidadAjustesAnt = rs.getInt("XX_PREVIOUSADJUSTMENTSQUANTITY");
				montoAjustesAnt = rs.getBigDecimal("XX_PREVIOUSADJUSTMENTSAMOUNT");
				IDinventory = rs.getInt("XX_VCN_INVENTORY_ID");
				statusINVM14 = rs.getString("XX_Synchronized");
				statusINVD53 = rs.getString("XX_SyncronizedToINVD53");

				/*
				 * Inicialiando las variables si algo de la base de datos esta NULL.
				 * */
				
				if (IDinventory == null){
					
					IDinventory = 0;
				}
				if (codigoTienda == null){
					
					codigoTienda = 0;
				}
				if (codigoCategoria == null){
					
					codigoCategoria = 0;
				}
				if (codigoDepartamento == null){
					
					codigoDepartamento = 0;
				}
				if (codigoSeccion == null){
					
					codigoSeccion = 0;
				}
				if (codigoLinea == null){
					
					codigoLinea = 0;
				}
				if (codigoProducto == null){
					
					codigoProducto = 0;
				}
				if (cantInventInicial == null){
					
					cantInventInicial = 0;
				}
				if (montoInventInicial == null){
					
					montoInventInicial = Env.ZERO;
				}
				if (cantidadCompras == null){
					
					cantidadCompras = 0;
				}
				if (montoCompras == null){
					
					montoCompras = Env.ZERO;
				}
				if (cantidadVentas == null){
					
					cantidadVentas = 0;
				}
				if (montoVentas == null){
					
					montoVentas = Env.ZERO;
				}
				if (cantidadMovimientos == null){
					
					cantidadMovimientos = 0;
				}
				if (montoMovimientos == null){
					
					montoMovimientos = Env.ZERO;
				}
				if (cantidadAjustes == null){
					
					cantidadAjustes = 0;
				}
				if (montoAjustes == null){
					
					montoAjustes = Env.ZERO;
				}
				if (cantidadAjustesAnt == null){
					
					cantidadAjustesAnt = 0;
				}
				if (montoAjustesAnt == null){
					
					montoAjustesAnt = Env.ZERO;
				}

								
				/*
				 * Sentencia que devuelve el ID del AS de la tienda.
				 * */
				
				sqlConsulta = "SELECT VALUE FROM M_WAREHOUSE WHERE M_WAREHOUSE_ID ="+codigoTienda+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
			
				while (rsConsulta.next()){
					
					 IDtienda = rsConsulta.getInt("VALUE");
				}
				rsConsulta.close();
				pstmtConsulta.close();
				
				
				/*
				 * Sentencia que devuelve el ID del AS de la categoria.
				 * */
				
				sqlConsulta = "SELECT VALUE FROM XX_VMR_Category WHERE XX_VMR_Category_ID ="+codigoCategoria+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
			
				while (rsConsulta.next()){
					
					 IDcategoria = rsConsulta.getInt("VALUE");
				}
				rsConsulta.close();
				pstmtConsulta.close();
				
				/*
				 * Sentencia que devuelve el ID del AS del departamento.
				 * */
				
				sqlConsulta = "SELECT VALUE FROM XX_VMR_Department WHERE XX_VMR_Department_ID ="+codigoDepartamento+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
			
				while (rsConsulta.next()){
					
					 IDdepartamento = rsConsulta.getInt("VALUE");
				}
				rsConsulta.close();
				pstmtConsulta.close();

				/*
				 * Sentencia que devuelve el ID del AS de la Sección.
				 * */
				
				sqlConsulta = "SELECT VALUE FROM XX_VMR_Section WHERE XX_VMR_Section_ID ="+codigoSeccion+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
			
				while (rsConsulta.next()){
					
					 IDseccion = rsConsulta.getInt("VALUE");
				}
				rsConsulta.close();
				pstmtConsulta.close();

				/*
				 * Sentencia que devuelve el ID del AS de la Línea.
				 * */
				
				sqlConsulta = "SELECT VALUE FROM XX_VMR_Line WHERE XX_VMR_Line_ID ="+codigoLinea+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
			
				while (rsConsulta.next()){
					
					 IDlinea = rsConsulta.getInt("VALUE");
				}
				rsConsulta.close();
				pstmtConsulta.close();

				/*
				 * Sentencia que devuelve el ID del AS del Producto.
				 * */
				
				sqlConsulta = "SELECT VALUE FROM M_PRODUCT WHERE M_PRODUCT_ID ="+codigoProducto+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
			
				while (rsConsulta.next()){
					
					 IDproducto = rsConsulta.getInt("VALUE");
				}
				rsConsulta.close();
				pstmtConsulta.close();
				
				/*
				 * Se realiza la sumatoria de las cantidades y montos para asignarselos al detalle de la ubicación del producto.
				 * */
				
				cantidad = cantInventInicial + cantidadCompras - cantidadVentas + cantidadMovimientos + cantidadAjustes + cantidadAjustesAnt;
				monto = montoInventInicial.add(montoCompras).subtract(montoVentas).add(montoMovimientos).add(montoAjustes).add(montoAjustesAnt);

				if (consecPrecio != 0){
					
					/*
					 * Sentencia que devuelve el código de ubicación del producto según el consecutivo para asignarselos al detalle de la ubación del producto.
					 * */
					
					codigoUbicacion = buscarCodigoUbicacion(codigoProducto, consecPrecio, IDtienda);
				}else{
					
					codigoUbicacion = 1;
				}
				
				


				if(codigoUbicacion != 0){
					/*
					 * Se insertan los registros en la tabla de E_XX_VCN_INVM14 que contiene el maestro de inventario y
					 * se inserta también en la tabla E_XX_VCN_INVD53 para especificar el detalle de la ubicación del producto.
					 * */
						
					if (statusINVM14.equalsIgnoreCase("N") && statusINVD53.equalsIgnoreCase("N")){ // Si el registro no ha sido sincronizado al tabla Maestro de Inventario ni a la del Detalle de Ubicación del producto.
		
						insertarINVM14(IDinventory, IDtienda, IDcategoria, IDdepartamento, IDlinea, IDseccion, 
								IDproducto, consecPrecio, mesInventario, añoInventario, cantInventInicial,
								montoInventInicial, cantidadCompras, montoCompras, cantidadVentas, 
								montoVentas, cantidadMovimientos, montoMovimientos, cantidadAjustes, 
								montoAjustes, cantidadAjustesAnt, montoAjustesAnt);
							
						INVM14.save();
							
						insertarINVD53(IDtienda, IDcategoria, IDdepartamento, IDlinea, IDseccion, IDproducto,
								mesInventario, añoInventario, cantidadAjustes, montoAjustes, consecPrecio, cantidad, monto, 
								codigoUbicacion , IDinventory);
							
						INVD53.save();
					}
						
					if (statusINVM14.equalsIgnoreCase("Y") && statusINVD53.equalsIgnoreCase("N")){
							
						insertarINVD53(IDtienda, IDcategoria, IDdepartamento, IDlinea, IDseccion, IDproducto,
								mesInventario, añoInventario, cantidadAjustes, montoAjustes, consecPrecio, cantidad, monto, 
								codigoUbicacion , IDinventory);
							
						INVD53.save();
					}
				}

				/*
				 * Inicializo las variables para el proximo registro.
				 * */
								
				codigoTienda = null;
				codigoCategoria = null;
				codigoDepartamento = null;
				codigoSeccion = null;
				codigoLinea = null;
				codigoProducto = null;
				consecPrecio = null;
				mesInventario = null;
				añoInventario = null;
				cantInventInicial = null;
				montoInventInicial = null;
				cantidadCompras = null;
				montoCompras = null;
				cantidadVentas = null;
				montoVentas = null;
				cantidadMovimientos = null;
				montoMovimientos = null;
				cantidadAjustes = null;
				montoAjustes = null;
				cantidadAjustesAnt = null;
				montoAjustesAnt = null;
				IDinventory = null;
				IDcategoria = null;
				IDdepartamento = null;
				IDlinea = null;
				IDseccion = null;
				IDproducto = null;
				cantidad = null;
				monto = null;
				codigoUbicacion = null;
				statusINVD53 = null;
				statusINVM14 = null;
				
			}
			rs.close();
			pstmt.close();
			
		}catch (SQLException e) {
			
			e.printStackTrace();
			return "Fallo en Sincronización";	
			// TODO: handle exception
		}

		return "Sincronización Completa";
	}

	private Integer buscarCodigoUbicacion(Integer IDproducto, Integer IDconsecutivo, Integer IDtienda) {
		
		// TODO Auto-generated method stub
		
		String sqlConsulta = null;
		PreparedStatement pstmtConsulta = null;
		ResultSet rsConsulta = null;
		String origenConsecutivo = null, status = null;
		Integer IDpedido = null, codigo = null;
		
		/*
		 * Sentencia que devuelve origen del consecutivo del producto.
		 * */
		
		try {
			
			sqlConsulta = "select XX_consecutiveorigin, XX_VMR_GENERATEDBY_ID " +
					"from XX_VMR_PRICECONSECUTIVE " +
					"where M_PRODUCT_ID = "+IDproducto+" " +
					"and XX_PRICECONSECUTIVE = "+IDconsecutivo+"";
			
			pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
			rsConsulta = pstmtConsulta.executeQuery();

			while (rsConsulta.next()){
				
				 origenConsecutivo = rsConsulta.getString("XX_consecutiveorigin");
				 IDpedido = rsConsulta.getInt("XX_VMR_GENERATEDBY_ID");
			}
			rsConsulta.close();
			pstmtConsulta.close();
			
			if (origenConsecutivo.equals("R") && (IDtienda != 1)){ // El origen es "Rebajas" por lo que el producto ya se encuentra en tienda.
					
				codigo = 2;
			}else{
				codigo = 1;
			}
			if (origenConsecutivo.equals("P")){ // El producto se encuentra en tránsito o en Centro de Distribución.
					
				/*
				 *  Sentencia que devuelve el status del pedido que genero el consecutivo del producto.
				 * */

				sqlConsulta = "select XX_ORDERREQUESTSTATUS " +
						"from XX_VMR_ORDER " +
						"where XX_VMR_ORDER_ID = "+IDpedido+"";
		
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();

				while (rsConsulta.next()){
					
					status = rsConsulta.getString("XX_ORDERREQUESTSTATUS");
					System.out.println("status:"+status);
				}
				rsConsulta.close();
				pstmtConsulta.close();


				if(status != null){

					if (status.equalsIgnoreCase("TI")){
						
						codigo = 3;
					}else if (status.equalsIgnoreCase("ET")){
						
						codigo = 2;
					}else{
						codigo = 1;
					}
				}
				else{
					codigo = 0;
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return codigo;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Procedimiento en el cuál se realiza el set de los datos para la tabla E_XX_VCN_INVM14.
	 * 
	 *  @param IDinventory código del registro en la tabla.
	 *  @param IDtienda código del AS de la tienda.
	 *  @param IDcategoria código del AS de la categoria del producto.
	 *  @param IDdepartamento código del AS del departamento del producto.
	 *  @param IDlinea código del AS de la línea del producto.
	 *  @param IDseccion código del AS de la sección del producto.
	 *  @param IDproducto código del AS del producto.
	 *  @param consecPrecio consecutivo del precio del producto.
	 *  @param mesInventario 
	 *  @param añoInventario
	 *  @param cantInventInicial cantidad de inventario inicial.
	 *  @param montoInventInicial monto del inventario inicial.
	 *  @param cantidadCompras
	 *  @param montoCompras
	 *  @param cantidadVentas
	 *  @param montoVentas
	 *  @param cantidadMovimientos
	 *  @param montoMovimientos
	 *  @param cantidadAjustes
	 *  @param montoAjustes
	 *  @param cantidadAjustesAnt cantidad de ajustes anteriores.
	 *  @param montoAjustesAnt monto de ajustes anteriores.
	 *  
	 *    
	 */
	
	public void insertarINVM14 (int IDinventory, int IDtienda, int IDcategoria, int IDdepartamento, 
			int IDlinea, int IDseccion, int IDproducto, int consecPrecio, int mesInventario, 
			int añoInventario, int cantInventInicial, BigDecimal montoInventInicial, 
			int cantidadCompras, BigDecimal montoCompras, int cantidadVentas, 
			BigDecimal montoVentas, int cantidadMovimientos, BigDecimal montoMovimientos, 
			int cantidadAjustes, BigDecimal montoAjustes, int cantidadAjustesAnt, 
			BigDecimal montoAjustesAnt){
		
		INVM14  = new X_E_XX_VCN_INVM14(getCtx(), 0, null);
				
        try{
        	
        	INVM14.setM_Warehouse_ID(IDtienda);
        	INVM14.setXX_VMR_Category_ID(IDcategoria);
        	INVM14.setXX_VMR_Department_ID(IDdepartamento);
        	INVM14.setXX_VMR_Line_ID(IDlinea);
        	INVM14.setXX_VMR_Section_ID(IDseccion);
        	INVM14.setM_Product_ID(IDproducto);
        	INVM14.setXX_ConsecutivePrice(consecPrecio);
        	INVM14.setXX_INVENTORYMONTH(mesInventario);
        	INVM14.setXX_INVENTORYYEAR(añoInventario);
        	INVM14.setXX_INITIALINVENTORYQUANTITY(cantInventInicial);
        	INVM14.setXX_INITIALINVENTORYAMOUNT(montoInventInicial);
        	INVM14.setXX_SHOPPINGQUANTITY(cantidadCompras);
        	INVM14.setXX_SHOPPINGAMOUNT(montoCompras);
        	INVM14.setXX_SALESQUANTITY(cantidadVentas);
        	INVM14.setXX_SALESAMOUNT(montoVentas);
        	INVM14.setXX_MOVEMENTQUANTITY(cantidadMovimientos);
        	INVM14.setXX_MOVEMENTAMOUNT(montoMovimientos);
        	INVM14.setXX_ADJUSTMENTSQUANTITY(cantidadAjustes);
        	INVM14.setXX_AdjustmentsAmount(montoAjustes);
        	INVM14.setXX_PREVIOUSADJUSTMENTSQUANTITY(cantidadAjustesAnt);
        	INVM14.setXX_PREVIOUSADJUSTMENTSAMOUNT(montoAjustesAnt);
        	INVM14.setAD_Org_ID(0);
			
        	actualizarRegistroCompiereINVM14 (IDinventory);
			
        }
        catch (Exception e) {
        	
        	e.printStackTrace();   
        }
	}
	
	/*
	 * Procedimiento en el cuál se realiza el set de los datos para la tabla E_XX_VCN_INVM14.
	 * 
	 *  @param IDinventory ID del registro en la tabla.
	 *  @param IDtienda ID del AS de la tienda.
	 *  @param IDcategoria ID de la categoria del producto en el AS.
	 *  @param IDdepartamento ID del departamento del producto en el AS.
	 *  @param IDlinea ID de la línea del producto en el AS.
	 *  @param IDseccion ID de la sección del producto en el AS.
	 *  @param IDproducto ID del producto en el AS.
	 *  @param consecPrecio consecutivo del precio del producto.
	 *  @param mesInventario
	 *  @param añoInventario
	 *  @param cantidadAjustes
	 *  @param montoAjustes
	 *  @param consecPrecio consecutivo de precio del Producto.
	 *  @param codigoUbicacion indica si el producto esta en Tienda, Tránsito o Centro de Distribución.
	 *  @param cantidad cantidad de productos que se estan registrando.
	 *  @param monto monto total de la cantidad de productos que se estan registrando.
	 *    
	 */
	
	public void insertarINVD53 (int codigoTienda, int IDcategoria, int IDdepartamento, int IDlinea, int IDseccion, 
								int IDproducto, int mesInventario, int añoInventario, 
								int cantidadAjustes, BigDecimal montoAjustes, int consecPrecio, int cantidad, 
								BigDecimal monto, int codigoUbicacion, int IDinventory){
		
		INVD53  = new X_E_XX_VCN_INVD53(getCtx(), 0, null);
				
        try{
        	
        	INVD53.setM_Warehouse_ID(codigoTienda);
        	INVD53.setXX_VMR_Category_ID(IDcategoria);
        	INVD53.setXX_VMR_Department_ID(IDdepartamento);
        	INVD53.setXX_VMR_Line_ID(IDlinea);
        	INVD53.setXX_VMR_Section_ID(IDseccion);
        	INVD53.setM_Product_ID(IDproducto);
        	INVD53.setXX_INVENTORYMONTH(mesInventario);
        	INVD53.setXX_INVENTORYYEAR(añoInventario);
        	INVD53.setXX_ADJUSTMENTSQUANTITY(cantidadAjustes);
        	INVD53.setXX_AdjustmentsAmount(montoAjustes);
        	INVD53.setXX_ConsecutivePrice(consecPrecio);
        	INVD53.setQty(cantidad);
        	INVD53.setXX_Amount(monto);
        	INVD53.setXX_Status(codigoUbicacion);
        	INVD53.setAD_Org_ID(0);
        	
        	actualizarRegistroCompiereINVD53(IDinventory);
        				
        }
        catch (Exception e) {
        	
        	e.printStackTrace();   
        }
	}
	
	/*
	 * Procedimiento que actualiza en la tabla XX_VCN_INVENTORY el campo XX_SYNCHRONIZED a 'Y' para indicar que el registro ya
	 * fue exportado a la tabla E_XX_VCN_INVM14.
	 * 
	 *  @param IDinventory es el ID deL registro en la tabla XX_VCN_INVENTORY.
	 */
	
	public void actualizarRegistroCompiereINVM14(int IDinventory)
	{
		String sqlUpdate = null;

		try{
			sqlUpdate = "UPDATE XX_VCN_INVENTORY SET XX_SYNCHRONIZED ='Y' WHERE XX_VCN_INVENTORY_ID= " + IDinventory;
			DB.executeUpdate(null,sqlUpdate);
		}
	    catch (Exception e) {
	        	
	    	e.printStackTrace();   
		}
	}
	
	/*
	 * Procedimiento que actualiza en la tabla XX_VCN_INVENTORY el campo XX_SYNCHRONIZED a 'Y' para indicar que el registro ya
	 * fue exportado a la tabla E_XX_VCN_INVD53.
	 * 
	 *  @param IDinventory es el ID deL registro en la tabla XX_VCN_INVENTORY.
	 */
	
	public void actualizarRegistroCompiereINVD53(int IDinventory)
	{
		String sqlUpdate = null;
		PreparedStatement pstmtUpdate = null;

		try{
			sqlUpdate = "UPDATE XX_VCN_INVENTORY SET XX_SYNCRONIZEDTOINVD53 ='Y' WHERE XX_VCN_INVENTORY_ID= " + IDinventory;
			DB.executeUpdate(null,sqlUpdate);
		}
	    catch (Exception e) {
	        	
	    	e.printStackTrace();   
		}
	}

}
