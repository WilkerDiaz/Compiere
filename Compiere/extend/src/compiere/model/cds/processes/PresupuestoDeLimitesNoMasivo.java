/**
 * =============================================================================
 * Proyecto   : PresupuestoDeLimites
 * Paquete    : com.beco.merchandising.presupuesto
 * Programa   : PresupuestoDeLimites.java
 * Creado por : mmiyazono
 * Creado en  : 29/11/2008 - 02:36:18 AM
 *
 * (c) CENTROBECO, C.A.
 * -----------------------------------------------------------------------------
 * 				Actualizaciones
 * -----------------------------------------------------------------------------
 * Versión     : 
 * Fecha       : 
 * Analista    : 
 * Descripción : 
 * =============================================================================
 */

package compiere.model.cds.processes;

 
/**
 *	Esta clase refiere a los objetos que representan PresupuestoDeLimites. 
 */
public class PresupuestoDeLimitesNoMasivo {


	public static void metodomain(String[] args) {
		
	int estado = 0;
	ManejadorBD presupuesto = new ManejadorBD();
	presupuesto.realizarComandoAS400("CHGDTAARA DTAARA(PRLA01) VALUE('1')");
	int fechaInicio = 201012;
	int fechaFin = 201106;
	
	//estado = presupuesto.montarPresupuestoDeArchivo(fechaInicio, fechaFin); 
 	//System.out.println("Leyó y actualizó desde Presupuesto.csv  " + estado + " lineas");
	
	//*** SOLO SE CORRIO LOS CONSOLIDADOS QUE ESTAN DESCOMNETADOS PARA EL CASO DE LA ACTUALIZACION DEL PRESUPUESTO
	//*** DE UN SOLO MES. ESTOS CONSOLIDADOS SE CORRIERON DIRECTAMNETE HACIA PRODUCCION.SI EMBARGO LA CARAGA DE DATOS
	//*** SE REALZO EN DESARROLLO SOLO CORRIENDO LA CARGA DEL ARCHIVO (NO SE CORRIO NINGUNO CONSOLIDADO) Y LUEGO SE PASO 
	//*** A PRODUCCION. LUEGO YA COIPADOS EN EL PRDL01 DE PRODUCCION SE CORRIERON LOS CONSOLIDADOS QUE MENCIONO ARRIBA 
	
	estado = presupuesto.consolidadosXSeccion(fechaInicio, fechaFin);
	estado = presupuesto.consolidadosXLinea(fechaInicio, fechaFin);
	estado = presupuesto.consolidadosXTiendaLinea(fechaInicio, fechaFin);
	estado = presupuesto.consolidadosXTiendaCategoriaDepartamento(fechaInicio, fechaFin);
	estado = presupuesto.consolidadosXTiendaCategoria(fechaInicio, fechaFin);
	estado = presupuesto.consolidadosXDepartamento(fechaInicio, fechaFin);
	estado = presupuesto.consolidadosXCategoria(fechaInicio, fechaFin);
	estado = presupuesto.consolidadosXTienda(fechaInicio, fechaFin);
	estado = presupuesto.consolidadosXCompania(fechaInicio, fechaFin);
	presupuesto.realizarComandoAS400("CHGDTAARA DTAARA(PRLA01) VALUE('0')");
	 
	}
}
