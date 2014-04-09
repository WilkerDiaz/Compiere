package compiere.model.cds.processes;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;


/*
 * Proceso que permite descargar en un archivos excel, el inventario y las ventas del día anterior.
 * 
 * @author Gabriela Marques.
 */

public class XX_VMR_ExportInventoryExcel extends SvrProcess {
	
	String srcInventario = "";
	String srcVentas = "";
	
	@Override
	protected String doIt() throws Exception {

		//Inicio
		System.out.println ("Inicio: "+new Date());
		String res = "";
		
		//Archivo de inventario (separado por categorías)
		if (!srcInventario.equals("")) {
			System.out.println("\n Exportando archivos de Inventario... ");
			List<String> categorias = XX_VMR_ExportInventory.listaCategorias();
			int i = 0;
			boolean exportInv = false;
			while (i<categorias.size()) {
				exportInv = XX_VMR_ExportInventory.exportInventoryTable(srcInventario, categorias.get(i++), categorias.get(i));
				System.out.println("\n Categoría " + categorias.get(i++));
			}
			if (!exportInv) { res += " *** No se pudo generar el archivo de Inventario. "; }
			else { res += " Generado el archivo de Inventario. "; }
		}
		
		if (!srcVentas.equals("")) {
			boolean exportSal = XX_VMR_ExportInventory.exportSalesTable(srcVentas);
			if (!exportSal) { res += " *** No se pudo generar el archivo de Ventas. "; } 
			else { res += " Generado el archivo de Ventas. "; }
		}

		//Finalización
		System.out.println ("Fin: "+new Date());
		
		return res + "Archivos creados en " + srcInventario + " y " + srcVentas;
		
	}

	@Override
	protected void prepare() {
		
		// Manejo de parámetros basado en XX_ExporPlacedOrderExcelProcess
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (element.getParameterName().equals("InventoryFile") ) {
				srcInventario = (String)element.getParameter();
				if (!srcInventario.isEmpty()) {
					srcInventario = srcInventario.concat("\\");
				}
				System.out.println("Inventario src: "+srcInventario);
			} else if (element.getParameterName().equals("SalesFile") ) {
				srcVentas = (String)element.getParameter();
				if (!srcVentas.isEmpty()) {
					srcVentas = srcVentas.concat("\\");
				}
				System.out.println("Ventas src: "+srcVentas);
				
			} else {
				log.log(Level.SEVERE, "Unknown Parameter: " + name);		
			}
		}
	}
	

}