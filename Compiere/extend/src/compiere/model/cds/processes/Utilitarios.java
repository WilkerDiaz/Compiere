package compiere.model.cds.processes; 

/**
 * =============================================================================
 * Proyecto   : Utilitarios
 * Paquete    : 
 * Programa   : Utilitarios.java
 * Creado por : mmiyazono
 * Creado en  : 29/11/2008 - 12:30:44 AM
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

public class Utilitarios {
	
	
	
	/**
	 * Metodo escapear.
	 * 		Sustituye el parámetro en una cadena con el caracter ' precedido del caracter \
	 * @param mensaje Mensaje a ser modificado
	 * @return String - Nueva cadena con la modificación
	 */
	public String escapear(String mensaje) {
		String result = "";
		
		if (mensaje != null) {
			for (int i=0; i<mensaje.length(); i++) {
				if (mensaje.substring(i,i+1).equals("'"))
					result += "\\";
				result += mensaje.substring(i,i+1);
			}
		}
		return result;
	}
	
	/**
	 * Método redondeo redondea el número doble a los decimales deseados 
	 * int decimales indica a cuantos decimales se redondeará 
	 * int factor = sistema decimal	 
	 * @param numero
	 * @param factor
	 * @param decimales
	 * @return double
	 */
	public double redondear(double numero, int factor, int decimales){
		double factoreo = Math.pow(factor, decimales);
		double resultado = Math.round(numero * factoreo) / factoreo;
		return resultado;
	}




}
