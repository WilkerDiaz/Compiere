package compiere.model.cds.processes;

import java.math.BigDecimal;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MOrder;


public class XX_ImportarExcelPRLD01 extends SvrProcess {
	
	String file = "";
	int fechaInicio = 0;
	int fechaFin = 0;
	

	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("File") ) {
					file = element.getParameter().toString();
				}
				if (element.getParameterName().equals("DateBegin") ) {
					fechaInicio = (Integer) element.getParameter();
				}
				if (element.getParameterName().equals("DateEnd") ) {
					fechaFin = (Integer) element.getParameter();
				}
			}			
		}
	}
	
	@Override
	protected String doIt() throws Exception {
		int estado = 0;
		ManejadorBD presupuesto = new ManejadorBD();
		presupuesto.realizarComandoAS400("CHGDTAARA DTAARA(PRLA01) VALUE('1')");
		int fechaInicio = 201212;
		int fechaFin = 201306;
		
		estado = presupuesto.montarPresupuestoDeArchivo(fechaInicio, fechaFin, getAD_Client_ID(), getCtx(), get_Trx()); 
	 	System.out.println("Leyó y actualizó desde Presupuesto.csv  " + estado + " lineas");
		
		//*** SOLO SE CORRIO LOS CONSOLIDADOS QUE ESTAN DESCOMNETADOS PARA EL CASO DE LA ACTUALIZACION DEL PRESUPUESTO
		//*** DE UN SOLO MES. ESTOS CONSOLIDADOS SE CORRIERON DIRECTAMNETE HACIA PRODUCCION.SI EMBARGO LA CARAGA DE DATOS
		//*** SE REALZO EN DESARROLLO SOLO CORRIENDO LA CARGA DEL ARCHIVO (NO SE CORRIO NINGUNO CONSOLIDADO) Y LUEGO SE PASO 
		//*** A PRODUCCION. LUEGO YA COIPADOS EN EL PRDL01 DE PRODUCCION SE CORRIERON LOS CONSOLIDADOS QUE MENCIONO ARRIBA 

/**		estado = presupuesto.consolidadosXSeccion(fechaInicio, fechaFin);
		estado = presupuesto.consolidadosXLinea(fechaInicio, fechaFin);
		estado = presupuesto.consolidadosXTiendaLinea(fechaInicio, fechaFin);
		estado = presupuesto.consolidadosXTiendaCategoriaDepartamento(fechaInicio, fechaFin);
		estado = presupuesto.consolidadosXTiendaCategoria(fechaInicio, fechaFin);
		estado = presupuesto.consolidadosXDepartamento(fechaInicio, fechaFin);
		estado = presupuesto.consolidadosXCategoria(fechaInicio, fechaFin);
		estado = presupuesto.consolidadosXTienda(fechaInicio, fechaFin);
		estado = presupuesto.consolidadosXCompania(fechaInicio, fechaFin);
*/		presupuesto.realizarComandoAS400("CHGDTAARA DTAARA(PRLA01) VALUE('0')");
		return "Presupuesto importado correctamente";
	}

}

