package compiere.model.cds.processes;


import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VLO_DispatchGuide;


public class XX_ReceiveDispatchGuideProcess extends SvrProcess {
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	@Override 
	// Realizado por Wdiaz y JPires
	// Cambia el pedido a En Tienda y actualiza todos los productos el inventario de Compiere 
	protected String doIt() throws Exception{
		X_XX_VLO_DispatchGuide guiaDespacho = new X_XX_VLO_DispatchGuide(getCtx(), getRecord_ID(), get_Trx());
		
		Utilities procesoRecepcion = new Utilities();
		procesoRecepcion.recibirGuiaDespacho(guiaDespacho.get_ID(), get_Trx());
		
		return "";
	}
	
}