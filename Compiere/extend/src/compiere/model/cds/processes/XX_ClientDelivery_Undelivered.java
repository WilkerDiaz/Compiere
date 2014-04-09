package compiere.model.cds.processes;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.cds.MVLOClientDelivery;

/**
 *  Proceso de Servicio de Entrega a Cliente: No Entregado
 *
 *  @author     Gabrielle Huchet
 *  @version    
 */

public class XX_ClientDelivery_Undelivered extends SvrProcess  {
	
	String motive;

	protected String doIt() throws Exception {
		MVLOClientDelivery cDelivery = new	MVLOClientDelivery(getCtx(),getRecord_ID() , get_TrxName());
	
		try {
			cDelivery.setXX_VLO_Status("NE");
		
		cDelivery.setXX_VLO_Motive(motive);
		cDelivery.save();
		commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "Operación No Éxitosa.";
		}
		return "Operación Éxitosa. Estado de la Entrega: No Entregado";
		
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_VLO_Motive")){
				motive= ((String)element.getParameter());
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}
}
