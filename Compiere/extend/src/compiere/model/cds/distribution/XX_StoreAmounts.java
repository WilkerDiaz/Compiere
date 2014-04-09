package compiere.model.cds.distribution;

import java.util.Vector;

/** Clase que representa cantidades por tienda por tienda*/
public class XX_StoreAmounts {
	
	public Float total = 0.0f;
	private int indiceMayorPorcentaje = 0;
	public Vector<Float> cantidades = new Vector<Float>();
	public Vector<Integer> tiendas = new Vector<Integer>();
	
	/** Esto convierte en porcentajes las cantidades calculadas */
	public void convertirAPorcentajes () {
		
		Float valor = 0.0f, mayor = 0.0f;
		for (int i = 0; i < cantidades.size() ; i++) {
			valor = cantidades.elementAt(i);
			valor = valor / total;
			cantidades.setElementAt(valor, i);
		}
		//Determinar el mayor porcentaje
		for (int i = 0 ; i < cantidades.size() ; i++) {
			if (cantidades.elementAt(i).doubleValue() > mayor) {
				indiceMayorPorcentaje = i;
				mayor = cantidades.elementAt(i).floatValue();
			}
		}
	}
	
	/** Con esto agrego una tienda a la estructura*/
	public void agregarTienda(Integer tienda, Float qty) {
		tiendas.add(tienda);
		cantidades.add(qty);
		total += qty;		
	}

	
	/** Sobreescribiendo el metodo para que muestre la informacion relevante */
	public String toString () {
		StringBuilder str = new StringBuilder("[\n");
		for (int i = 0 ; i < cantidades.size() ; i++) {
			str.append("" + tiendas.elementAt(i) + "\t" + cantidades.elementAt(i) + "\n");
		}
		str.append("]\n");
		return str.toString();
		
	}

	/** Obtener el indice del que tiene mayor porcentaje */
	public int obtIndiceMayorPorcentaje() {
		return indiceMayorPorcentaje;
	}
	
	/** Obtener el una lista con los indice de las tiendas en orden de mayor a menor porcentaje */
	public Vector<Integer>  obtPrioridadesAgregar() {
		
		Vector<Integer> priorityToAdd= new Vector<Integer>();
		int most = 0; double most_value_percentage = 0;
		for (int j = 0 ; j < cantidades.size() ; j++) {
			for (int i = 0 ; i < cantidades.size() ; i++) {
				if (cantidades.elementAt(i).doubleValue() >  most_value_percentage && !priorityToAdd.contains(i)) {
					most = i;
					most_value_percentage = cantidades.elementAt(i).doubleValue();
				}
			}
			most_value_percentage = 0;
			priorityToAdd.add(most);
		}
		return priorityToAdd;
	}

	/** Devuelve un vector con los índices de las tiendas en orden de mayor a menor prioridad para agregar */
	public static Vector<Integer>  obtPrioridadesDecimalAgregar(Vector<? extends Number> percentages, int total, int divisor, int paquete, int distPrev) {
		
		Vector<Integer> priorityToAdd= new Vector<Integer>();
		int most = 0; double most_value_percentage = 0;
		int entero = 0; double decimal, cantidad = 0.0;
		for (int j = 0 ; j < percentages.size() ; j++) {
			for (int i = 0 ; i < percentages.size() ; i++) {
				cantidad =(percentages.elementAt(i).doubleValue()*total/divisor/paquete) - distPrev;
				entero = (int)(cantidad) ;
				decimal = cantidad - entero;
				if (decimal >  most_value_percentage && !priorityToAdd.contains(i)) {
					most = i;
					most_value_percentage = decimal;
				}
			}
			most_value_percentage = 0;
			priorityToAdd.add(most);
		}
		return priorityToAdd;
	}
}
