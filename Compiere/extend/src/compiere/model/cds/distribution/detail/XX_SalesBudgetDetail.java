package compiere.model.cds.distribution.detail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

import compiere.model.cds.distribution.XX_StoreAmounts;

/**
 *  Clase concreta que almacena los métodos de distribucion de detalle
 *  para el tipo de distribucion por presupuesto 
 *  (modificación de código de Javier Pino para distribuciones completas)
 *  @author Gabrielle Huchet
 */
public class XX_SalesBudgetDetail extends XX_DistributionDetail  {
	
	//Almacenan los porcentajes y ventas por tienda
	private XX_BudgetDetail presupuesto = null;
	private XX_SalesDetail ventas = null;
	private boolean usarVentasAñoPasado = false;
	
	/** Constructor por defecto */
	public XX_SalesBudgetDetail(int detalleID, Ctx contexto, Trx trxNombre, int mes, int año, boolean ventasPasadas) {
		super(detalleID, contexto, trxNombre, mes, año);		
		usarVentasAñoPasado = ventasPasadas;
	}
	
	@Override
	public void finalizar() {
		finalizado = true;
	}

	/** Metodo heredado que indica los pasos necesarios para inicializar esta clase
	 * */
	@Override	
	public void inicializar()  {
		
		int añoVentas, mesVentas ;
		
		if (inicializado)
			return;
		if (usarVentasAñoPasado) {
			//Usar las venats del mismo mes del año pasado
			mesVentas = mes;
			añoVentas = año - 1;
		} else {						
			//Entonces usar las ventas del mes pasado 
			if (mes == 1) {
				añoVentas = año - 1; 
				mesVentas = 12;
			} else {
				añoVentas = año;
				mesVentas = mes - 1;
			}
		}
		//añoVentas = 2010; //TODO ELIMINAR
		//mesVentas = 11; //TODO ELIMINAR
		
		//Inicializar presupuesto usando clase hermana XX_Budget
		presupuesto = new XX_BudgetDetail(detalle.get_ID(), ctx, nombreTrx, mes, año);
		presupuesto.inicializar();
		if (!presupuesto.inicializado) {
			inicializado = false;
			return;
		}		
		//Inicializar presupuesto usando clase hermana XX_Budget
		ventas = new XX_SalesDetail(detalle.get_ID(), ctx, nombreTrx, mesVentas, añoVentas);
		ventas.inicializar();
		if (!ventas.inicializado) {
			inicializado = false;
			return;
		}				
		inicializado = true;	
	}

	/**
	 * Este metodo obtiene los procentajes asociados
	 * Calcula los porcentajes por presupuesto ventas
	 * El result set debe incluir las columnas (asi sean nulas) XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID
	 */
	@Override
	public XX_StoreAmounts obtenerPorcentaje(ResultSet rsDatosProducto) throws SQLException {
		
		//Calcular el presupuesto del producto de acuerdo a las reglas de seccion, linea, depto
		XX_StoreAmounts presupuestoProducto = presupuesto.obtenerPorcentaje(rsDatosProducto);
		if (presupuestoProducto == null) 
			return null;
		//Calcular las ventas
		XX_StoreAmounts ventasProducto = ventas.obtenerPorcentaje(rsDatosProducto);
		if (ventasProducto == null) 
			return null;
	
		//Aqui almacenaré el resultado mientras realizo el calculo
		Hashtable<Integer, Float> porcentajesTienda = new Hashtable<Integer, Float>(10) ;
		
		//Agregar presupuesto al hash
		for (int i = 0; i < presupuestoProducto.tiendas.size() ; i++) {									
			porcentajesTienda.put(presupuestoProducto.tiendas.elementAt(i), 
				presupuestoProducto.cantidades.elementAt(i));		
		}		
		//Agregar las ventas al hash
		Float porcentaje = 0.0f;
		int tienda = 0;
		for (int i = 0; i < ventasProducto.tiendas.size() ; i++) {
			tienda = ventasProducto.tiendas.elementAt(i);
			porcentaje = ventasProducto.cantidades.elementAt(i);
			if (porcentajesTienda.containsKey(tienda)) { 
				porcentaje += porcentajesTienda.get(tienda);				
			}
			porcentajesTienda.put(tienda, porcentaje/2);			
		}
		
		//Iteramos sobre la tabla de hash y creamos el resultado 
		XX_StoreAmounts resultado = new XX_StoreAmounts();
		for (Entry<Integer, Float> elemento:porcentajesTienda.entrySet()) {
			resultado.agregarTienda(elemento.getKey(), elemento.getValue());			
		}
		return resultado;
	}

	/** Modificado en caso de que queramos ver la informacion de la distribucion */
	@Override
	public String toString() {
		return "Presupuesto " + presupuesto.toString() + 
			" \n Ventas " + ventas.toString();
	}
		
		
	

}
