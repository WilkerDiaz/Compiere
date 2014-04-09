package compiere.model.cds.distribution;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;



/**
 *  Clase concreta que almacena los métodos de distribución automática para el tipo de distribución por piezas.
 *  @author Gabrielle Huchet
 */
public class XX_PiecesAutomatic extends XX_Distribution {

	private Hashtable<Integer, XX_StoreAmounts> productos ;
	/** Constructor por defecto */
	public XX_PiecesAutomatic(int cabeceraID, Ctx contexto, Trx trxNombre,
			int mes, int año) {
		super(cabeceraID, contexto, trxNombre, mes, año);
	}

	protected void finalizar() {
		finalizado = true;
		
	}
	
	/** Metodo heredado que indica los pasos necesarios para inicializar esta clase
	 * */
	protected void inicializar() {
		if (inicializado)
			return;
		isPiecesAuto = true;
		//Inicializar porcentajes de todos los productos para cada tienda
		productos = new Hashtable<Integer, XX_StoreAmounts>();
		llenarHashProducto(productos);
		if (productos.isEmpty()) {
			inicializado = false;
		} else {
			//Marcarlo como inicializado
			inicializado = true;
		}
	}
	
	/** LLenar hash de producto con las cantidades que tiene cada tienda */
	public void llenarHashProducto(Hashtable<Integer, XX_StoreAmounts> hash) {
		
		//Query que se utiliza para llenar las hashtables
		String sql = "\nSELECT PD.M_PRODUCT_ID, DD.M_WAREHOUSE_ID, DD.XX_PRODUCTQUANTITY " +
				"\nFROM XX_VMR_PO_DISTRIBDETAIL DD " +
				"\nJOIN XX_VMR_PO_PRODUCTDISTRIB PD ON (PD.XX_VMR_PO_PRODUCTDISTRIB_ID = DD.XX_VMR_PO_PRODUCTDISTRIB_ID) "+
				"\nWHERE PD.XX_VMR_DISTRIBUTIONHEADER_ID  = " +cabecera.get_ID() +
				"\nAND PD.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
				"\nORDER BY PD.M_PRODUCT_ID "  ;
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

	@Override
	protected XX_StoreAmounts obtenerPorcentaje(ResultSet rsProductos)
			throws SQLException {
	
		int producto = rsProductos.getInt("M_PRODUCT");
		
		//Si hay ventas para el producto, sino la seccion, sino el de la linea, sino el del departamento
		if (productos.containsKey(producto)) {
			return productos.get(producto);
		}		
		return null;
	}
	
	
	
}
