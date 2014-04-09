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
 *  para el tipo de distribucion por presupuesto
 *  (modificación de código de Javier Pino para distribuciones completas)
 *  @author Gabrielle Huchet
 */
public class XX_BudgetDetail extends XX_DistributionDetail  {
	
	//Almacenan los porcentajes por tienda
	private Hashtable<Integer, XX_StoreAmounts> departamentos ;
	private Hashtable<Integer, XX_StoreAmounts> lineas ;
	private Hashtable<Integer, XX_StoreAmounts> secciones ;
	String añomes = "";
	
	/** Constructor por defecto */
	public XX_BudgetDetail(int detalleID, Ctx contexto, Trx trxNombre, int mes, int año) {
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
		
		//Inicializar presupuesto secciones
		secciones = new Hashtable<Integer, XX_StoreAmounts>();
		llenarHash("XX_VMR_SECTION_ID", añomes, secciones);
		
		//Inicializar los hash en vacios
		lineas = new Hashtable<Integer, XX_StoreAmounts>();		
		departamentos = new Hashtable<Integer, XX_StoreAmounts>();

		if (secciones.isEmpty()) {
			inicializado = false;
			ADialog.error(1, new Container(), Msg.translate(ctx, "XX_NoBudgetForSelection"));
		} else {
			//Marcarlo como inicializado
			inicializado = true;
		}
	
	}
	
	/** Con este metodo lleno los hashes al momento de inicializar 
	 * */ 
	public void llenarHash (String nombreColumna, String añomes, 
			Hashtable<Integer, XX_StoreAmounts> hash) {
		
		//Query que se utiliza para llenar las hashtables
		String sql = " SELECT IP." + nombreColumna + ", IP.M_WAREHOUSE_ID, SUM(IP.XX_SALESAMOUNTBUD2) " +
		" FROM XX_VMR_PRLD01 IP WHERE IP.XX_BUDGETYEARMONTH = '" + añomes + "' " +
		" GROUP BY IP." + nombreColumna + ", IP.M_WAREHOUSE_ID " +
		" HAVING SUM(IP.XX_SALESAMOUNTBUD2) > 0 " +
		" ORDER BY IP." + nombreColumna ;
		int departamentoAux, departamentoAnterior;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			departamentoAux = 0;
			departamentoAnterior = -1;
			XX_StoreAmounts porcentajeTienda = null;
			while (rs.next()) {			
				departamentoAux = rs.getInt(1);			
				if (hash.containsKey(departamentoAux)) {
					
					//Si lo contiene se agrega el valor de la tienda					
					hash.get(departamentoAux).agregarTienda(rs.getInt(2), rs.getFloat(3));					
				} else {
					
					//Sino lo contiene entonces crea un nueva estructura de porcentajes tienda
					porcentajeTienda = new XX_StoreAmounts();
					porcentajeTienda.agregarTienda(rs.getInt(2), rs.getFloat(3));
					hash.put(departamentoAux, porcentajeTienda);
					if (departamentoAnterior == -1) {
						departamentoAnterior = departamentoAux;
					} else if (departamentoAnterior != departamentoAux){
						
						//Las cantidades deben convertirse en procentajes
						hash.get(departamentoAnterior).convertirAPorcentajes();
						departamentoAnterior = departamentoAux;
					}
				}			
			}			
			if (departamentoAnterior != -1) {
				//Este paso quedó sin hacerse en la iteracion
				hash.get(departamentoAnterior).convertirAPorcentajes();
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
	 * Debido a que no hay presupuesto por productos se calcula por depto, linea, seccion
	 * El result set debe incluir las columnas (asi sean nulas) XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID
	 */
	@Override
	public XX_StoreAmounts obtenerPorcentaje(ResultSet rsDatosProducto) throws SQLException {
		
		int seccion = 0, linea = 0, departamento = 0;		
		seccion = rsDatosProducto.getInt("XX_VMR_SECTION_ID");
		linea = rsDatosProducto.getInt("XX_VMR_LINE_ID");
		departamento = rsDatosProducto.getInt("XX_VMR_DEPARTMENT_ID");
		
		//Si hay presupuesto para la seccion, sino el de la linea, sino el del departamento		
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
		return str.toString();
	}
	
}
