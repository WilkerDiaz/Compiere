package compiere.model.cds.distribution;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Map.Entry;
import org.compiere.apps.ADialog;
import org.compiere.model.X_Ref_Quantity_Type;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 *  Clase concreta que almacena los métodos de distribucion para el tipo de distribucion por reposición
 *  @author Javier Pino
 */
public class XX_Replacement extends XX_Distribution  {
	
	//Almacena el inventario por producto
	private Hashtable<Integer, XX_StoreAmounts> inventarioProducto = null;
	
	//Aqui se almacena el presupuesto
	private XX_Budget presupuesto = null;
	private Vector<Integer> tiendas = null;
	
	/** Constructor por defecto */
	public XX_Replacement(int cabeceraID, Ctx contexto, Trx trxNombre, int mes, int año) {
		super(cabeceraID, contexto, trxNombre, mes, año);		
	}
	
	@Override
	public void finalizar() {
		finalizado = true;
	}

	/** Metodo heredado que indica los pasos necesarios para inicializar esta clase
	 * */
	@Override	
	public void inicializar()  {
		
		if (inicializado)
			return;
		
		//Inicializar presupuesto usando clase hermana XX_Budget
		presupuesto = new XX_Budget(super.cabecera.get_ID(), ctx, nombreTrx, mes, año);
		presupuesto.inicializar();
		if (!presupuesto.inicializado) {
			inicializado = false;
			return;
		}
		
		//LLenar el hash con los inventarios de productos
		inventarioProducto = new Hashtable<Integer, XX_StoreAmounts>();
		llenarHashInventario(inventarioProducto);
		
		tiendas = new Vector<Integer>();
		llenarTiendas();
		
		if (inventarioProducto.isEmpty() || tiendas.isEmpty()) {
			inicializado = false;
			ADialog.error(1, new Container(), "XX_TotalInventoryZero");
		} else {
			inicializado = true;
		}		
	}
	
	/** Buscar tiendas para llenar vector */
	private void llenarTiendas () {
		
		String sql = "SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE WHERE ISACTIVE = 'Y' AND XX_ISSTORE = 'Y' AND XX_NotReceiveOrder = 'N' ";		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next()) {			
				tiendas.add(rs.getInt(1));			
			}						
		} catch (SQLException e) {			
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {}
		}
	}
	
	/** LLenar hash de producto
	 * Metodo necesario porque es el query mas sencillo*/
	private void llenarHashInventario( Hashtable<Integer, XX_StoreAmounts> hash) {
		
		//El inventario es actual en tienda
		String sql = " SELECT IV.M_PRODUCT_ID, WAR.M_WAREHOUSE_ID, SUM(IV.QTY) AS DISP FROM M_STORAGEDETAIL IV " +
				" JOIN (SELECT L.M_LOCATOR_ID, L.M_WAREHOUSE_ID FROM M_WAREHOUSE W JOIN M_LOCATOR L ON (L.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID) " +
				" WHERE XX_ISSTORE = 'Y' AND XX_NotReceiveOrder = 'N' ) WAR " +
				" ON (WAR.M_LOCATOR_ID = IV.M_LOCATOR_ID) " +
				" WHERE QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"'" +
				" AND IV.M_AttributeSetInstance_ID>=0" +
				" AND IV.M_lOCATOR_ID >= 0" +
				" GROUP BY IV.M_PRODUCT_ID, WAR.M_WAREHOUSE_ID HAVING SUM(IV.QTY) > 0 " ;
		int productoAux;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			productoAux = 0;
			XX_StoreAmounts cantidadTienda = null;
			while (rs.next()) {			
				productoAux = rs.getInt(1);			
				if (hash.containsKey(productoAux)) {					
					//Si lo contiene se agrega el valor de la tienda					
					hash.get(productoAux).agregarTienda(rs.getInt(2), rs.getFloat(3));					
				} else {					
					//Sino lo contiene entonces crea un nueva estructura de tienda
					cantidadTienda = new XX_StoreAmounts();
					cantidadTienda.agregarTienda(rs.getInt(2), rs.getFloat(3));
					hash.put(productoAux, cantidadTienda);	
				}			
			}						
		} catch (SQLException e) {			
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {}
		}
	} 

	/**
	 * Este metodo obtiene los procentajes asociados
	 * Debido si no hay ventas por productos se calcula por depto, linea, seccion
	 * El result set debe incluir las columnas (asi sean nulas) XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID,
	 *  XX_VMR_SECTION_ID, M_PRODUCT
	 */
	@Override
	public XX_StoreAmounts obtenerPorcentaje(ResultSet rsDatosProducto) throws SQLException {
		
		int producto = rsDatosProducto.getInt("M_PRODUCT");
		float cantidadADistribuir = rsDatosProducto.getFloat("XX_QTY");
		
		//Calcular el presupuesto del producto de acuerdo a las reglas de seccion, linea, depto
		XX_StoreAmounts presupuestoProducto = presupuesto.obtenerPorcentaje(rsDatosProducto);
		
		//Esto almacena el inventario actual
		XX_StoreAmounts inventarioTiendas = null;

		//En este objeto se almacenará el resultado
		XX_StoreAmounts resultado = new XX_StoreAmounts();
		
		if (presupuestoProducto == null) 
			return null;
		
		//Si hay ventas para el producto, sino la seccion, sino el de la linea, sino el del departamento
		if (inventarioProducto.containsKey(producto)) {
			inventarioTiendas = inventarioProducto.get(producto);
		}		

		float invDisponibleTotal = cantidadADistribuir;
		if (inventarioTiendas != null) {
			//El inventario total es la suma de lo distribuido más lo que hay en tiendas
			invDisponibleTotal += inventarioTiendas.total;						
		}
		
		//LLenar el objeto resultado 
		int tienda; float inventarioTda, presupuestoTda, piezasTda;
		for (int i = 0; i < tiendas.size() ; i++) {
			tienda = tiendas.elementAt(i);
			inventarioTda = 0.0f;
			presupuestoTda = 0.0f;
			
			//Obtener presupuesto de la tienda
			for (int j = 0; j < presupuestoProducto.tiendas.size() ; j++) { 
				if (tienda == presupuestoProducto.tiendas.elementAt(j)) {
					presupuestoTda = presupuestoProducto.cantidades.elementAt(j);   					
					break;
				} 
			}	
			if (inventarioTiendas != null ) {
				//Obtener cantidad en inventario de la tienda
				for (int k = 0 ; k < inventarioTiendas.tiendas.size() ; k++) {
					if (tienda == inventarioTiendas.tiendas.elementAt(k)) {
						inventarioTda = inventarioTiendas.cantidades.elementAt(k);
						break;
					}
				}
			}
						
			//Formula de reposicion
			piezasTda = (float) (Math.round(invDisponibleTotal * presupuestoTda) - inventarioTda);
			if (piezasTda > 0) {
				resultado.agregarTienda(tienda, piezasTda);
			}
		}

		
		//Distribuyo piezas sobrantes usando el porcentaje de presupuesto correspondiente a cada tienda
		if (cantidadADistribuir > resultado.total) {
			
			float sobrante = cantidadADistribuir - resultado.total;
			for (int i = 0; i < tiendas.size() ; i++) {
				tienda = tiendas.elementAt(i);
				presupuestoTda = 0.0f;
				boolean exists = false;
				int index = 0;
				//Obtener presupuesto de la tienda
				for (int j = 0; j < presupuestoProducto.tiendas.size() ; j++) { 
					if (tienda == presupuestoProducto.tiendas.elementAt(j)) {
						presupuestoTda = presupuestoProducto.cantidades.elementAt(j);   					
						break;
					} 
				}	
				for (int j = 0; j < resultado.tiendas.size() ; j++) { 
					if (tienda == resultado.tiendas.elementAt(j)) {
						exists = true;		
						index = j;
						break;
					} 
				}	
				piezasTda = sobrante * presupuestoTda;

				if (piezasTda > 0) {
					if(exists){
						resultado.cantidades.set(index, resultado.cantidades.get(index) + piezasTda);
						resultado.total += piezasTda;
					}else resultado.agregarTienda(tienda, piezasTda);
				}
			}	
		} 	
		
		//Si aun hay sobrantes, se divide el sobrante entre el numero de tiendas y se distribuye el resultado a cada tienda
		if (cantidadADistribuir > resultado.total) {
			float sobrante = cantidadADistribuir - resultado.total;
			for (int i = 0; i < tiendas.size() ; i++) {
				tienda = tiendas.elementAt(i);			
				piezasTda = sobrante/tiendas.size();
				int index = 0;
				boolean exists = false;
				
				for (int j = 0; j < resultado.tiendas.size() ; j++) { 
					if (tienda == resultado.tiendas.elementAt(j)) {
						index =j;
						exists = true;				
						break;
					} 
				}	
				if (piezasTda > 0) {
					if(exists){
						resultado.cantidades.set(index, resultado.cantidades.get(index) + piezasTda);
						resultado.total += piezasTda;
					}else resultado.agregarTienda(tienda, piezasTda);
				}
			}	
		}
		resultado.convertirAPorcentajes();	
		return resultado;
	}

	/** Modificado en caso de que queramos ver la informacion de la distribucion */
	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		
		//El Inventario
		str.append("Inventario\n");
		for (Entry<Integer, XX_StoreAmounts> elemento:inventarioProducto.entrySet()) {
			str.append("Producto " + elemento.getKey());
			str.append(elemento.getValue());			
		}
		return str.toString();
	}
	
}
