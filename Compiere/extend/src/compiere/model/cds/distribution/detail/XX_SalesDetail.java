package compiere.model.cds.distribution.detail;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.distribution.XX_StoreAmounts;


/**
 *  Clase concreta que almacena los métodos de distribucion de detalle
 *  para el tipo de distribucion por ventas
 *  (modificación de código de Javier Pino para distribuciones completas)
 *  @author Gabrielle Huchet
 */
public class XX_SalesDetail extends XX_DistributionDetail  {
	
	//Almacenan los porcentajes por tienda
	private Hashtable<Integer, XX_StoreAmounts> departamentos ;
	private Hashtable<Integer, XX_StoreAmounts> lineas ;
	private Hashtable<Integer, XX_StoreAmounts> secciones ;
	private Hashtable<Integer, XX_StoreAmounts> productos ;
	private String añomes = "";

	/** Constructor por defecto */
	public XX_SalesDetail(int detalleID, Ctx contexto, Trx trxNombre, int mes, int año) {
		super(detalleID, contexto, trxNombre, mes, año);		
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
		
		if(mes < 10)
			añomes = año + "0" + mes;
		else
			añomes = "" + año + mes;
		
		//Inicializar ventas productos
		productos = new Hashtable<Integer, XX_StoreAmounts>();
		llenarHashProducto(añomes, productos);

		//Inicializar los hash en vacios
		secciones = new Hashtable<Integer, XX_StoreAmounts>();
		lineas = new Hashtable<Integer, XX_StoreAmounts>();		
		departamentos = new Hashtable<Integer, XX_StoreAmounts>();

		if (productos.isEmpty()) {
			inicializado = false;
			ADialog.error(1, new Container(), Msg.translate(ctx, "XX_NoSalesForSelection"));
		} else {
			//Marcarlo como inicializado
			inicializado = true;
		}
	}
	
	/** LLenar hash de producto
	 * Metodo necesario porque es el query mas sencillo*/
	public void llenarHashProducto(String añomes, Hashtable<Integer, XX_StoreAmounts> hash) {
		
		//Query que se utiliza para llenar las hashtables
		String sql = "SELECT L.M_PRODUCT_ID , L.M_WAREHOUSE_ID, SUM(L.LINENETAMT) " +
				" FROM C_ORDERLINE L , C_ORDER C WHERE L.C_ORDER_ID = C.C_ORDER_ID AND C.ISSOTRX= 'Y' " +
				" AND TO_CHAR(C.DATEORDERED, 'YYYY')||TO_CHAR(C.DATEORDERED, 'MM') = " + añomes +
				" GROUP BY L.M_PRODUCT_ID, L.M_WAREHOUSE_ID " +
				" ORDER BY L.M_PRODUCT_ID "  ;
		int productoAux, productoAnterior;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			productoAux = 0;
			productoAnterior = -1;
			XX_StoreAmounts porcentajeTienda = null;
			while (rs.next()) {			
				productoAux = rs.getInt(1);			
				if (hash.containsKey(productoAux)) {
					
					//Si lo contiene se agrega el valor de la tienda					
					hash.get(productoAux).agregarTienda(rs.getInt(2), rs.getFloat(3));					
				} else {
					
					//Sino lo contiene entonces crea un nueva estructura de porcentajes tienda
					porcentajeTienda = new XX_StoreAmounts();
					porcentajeTienda.agregarTienda(rs.getInt(2), rs.getFloat(3));
					hash.put(productoAux, porcentajeTienda);
					if (productoAnterior == -1) {
						productoAnterior = productoAux;
					} else if (productoAnterior != productoAux){
						
						//Las cantidades deben convertirse en procentajes
						hash.get(productoAnterior).convertirAPorcentajes();
						productoAnterior = productoAux;
					}
				}			
			}			
			if (productoAnterior != -1) {
				//Este paso quedó sin hacerse en la iteracion
				hash.get(productoAnterior).convertirAPorcentajes();
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
	
	
	/** Con este metodo lleno los hashes al momento de inicializar 
	 * */ 
	public void llenarHash (String nombreColumna, String añomes, Hashtable<Integer, XX_StoreAmounts> hash) {
		
		//Query que se utiliza para llenar las hashtables
		String sql = 
			" WITH VENTA AS (SELECT " +
			" L.M_PRODUCT_ID , L.M_WAREHOUSE_ID, SUM(L.LINENETAMT) PZAS " +
			" FROM C_ORDERLINE L , C_ORDER C " +
			" WHERE L.C_ORDER_ID = C.C_ORDER_ID " +
			" AND C.ISSOTRX= 'Y' AND TO_CHAR(C.DATEORDERED, 'YYYY')||TO_CHAR(C.DATEORDERED, 'MM') = " + añomes + 
			" GROUP BY L.M_PRODUCT_ID, L.M_WAREHOUSE_ID) " +
			" SELECT P." + nombreColumna + ", VENTA.M_WAREHOUSE_ID, SUM(VENTA.PZAS) " +
			" FROM VENTA JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = VENTA.M_PRODUCT_ID) " +
			" GROUP BY P." + nombreColumna + ", VENTA.M_WAREHOUSE_ID " +
			" ORDER BY P." + nombreColumna ;

		int nombreColumnaAux, nombreColumnaAnterior;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			nombreColumnaAux = 0;
			nombreColumnaAnterior = -1;
			XX_StoreAmounts porcentajeTienda = null;
			while (rs.next()) {			
				nombreColumnaAux = rs.getInt(1);			
				if (hash.containsKey(nombreColumnaAux)) {
					
					//Si lo contiene se agrega el valor de la tienda					
					hash.get(nombreColumnaAux).agregarTienda(rs.getInt(2), rs.getFloat(3));					
				} else {
					
					//Sino lo contiene entonces crea un nueva estructura de porcentajes tienda
					porcentajeTienda = new XX_StoreAmounts();
					porcentajeTienda.agregarTienda(rs.getInt(2), rs.getFloat(3));
					hash.put(nombreColumnaAux, porcentajeTienda);
					if (nombreColumnaAnterior == -1) {
						nombreColumnaAnterior = nombreColumnaAux;
					} else if (nombreColumnaAnterior != nombreColumnaAux){
						
						//Las cantidades deben convertirse en procentajes
						hash.get(nombreColumnaAnterior).convertirAPorcentajes();
						nombreColumnaAnterior = nombreColumnaAux;
					}
				}			
			}			
			if (nombreColumnaAnterior != -1) {
				//Este paso quedó sin hacerse en la iteracion
				hash.get(nombreColumnaAnterior).convertirAPorcentajes();
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
		
		int producto = 0, seccion = 0, linea = 0, departamento = 0;	
		producto = rsDatosProducto.getInt("M_PRODUCT");
		seccion = rsDatosProducto.getInt("XX_VMR_SECTION_ID");
		linea = rsDatosProducto.getInt("XX_VMR_LINE_ID");
		departamento = rsDatosProducto.getInt("XX_VMR_DEPARTMENT_ID");
		
		//Si hay ventas para el producto, sino la seccion, sino el de la linea, sino el del departamento
		if (productos.containsKey(producto)) {
			return productos.get(producto);
		}		
		//Verificar en secciones, si no se han calculado, calcular
		if (secciones.isEmpty()) {
			//Inicializar ventas secciones		
			llenarHash("XX_VMR_SECTION_ID", añomes, secciones);
		}
		if (secciones.containsKey(seccion)) {
			return secciones.get(seccion); 
		}
		
		//Verificar lineas, si vacio calcular
		if (lineas.isEmpty()) {
			//Inicializar ventas lineas
			llenarHash("XX_VMR_LINE_ID", añomes, lineas);
		}
		if (lineas.containsKey(linea)) {
			return lineas.get(linea);
		} 
		
		//Verificar departamentos, si vacio calcular
		if (departamentos.isEmpty()) {
			llenarHash("XX_VMR_DEPARTMENT_ID", añomes, departamentos);
		}
		if (departamentos.containsKey(departamento)) {
			return departamentos.get(departamento);
		}		
		return null;
	}

	/** Modificado en caso de que queramos ver la informacion de la distribucion */
	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		
		//Los departamentos
		str.append("Departamentos\n");
		for (Entry<Integer, XX_StoreAmounts> elemento:departamentos.entrySet()) {
			str.append("Depto " + elemento.getKey());
			str.append(elemento.getValue());			
		}
		//Las lineas
		str.append("Lineas\n");
		for (Entry<Integer, XX_StoreAmounts> elemento:lineas.entrySet()) {
			str.append("Linea " + elemento.getKey());
			str.append(elemento.getValue());			
		}
		//Las secciones
		str.append("Secciones\n");
		for (Entry<Integer, XX_StoreAmounts> elemento:secciones.entrySet()) {
			str.append("Seccion " + elemento.getKey());
			str.append(elemento.getValue());			
		}
		str.append("Productos\n");
		for (Entry<Integer, XX_StoreAmounts> elemento:productos.entrySet()) {
			str.append("Producto " + elemento.getKey());
			str.append(elemento.getValue());			
		}
		return str.toString();
	}
	
}
