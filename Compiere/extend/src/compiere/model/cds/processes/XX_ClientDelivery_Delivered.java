package compiere.model.cds.processes;

import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.cds.MVLOClientDelivery;

/**
 *  Proceso de Servicio de Entrega a Cliente: Entregado
 *
 *  @author     Gabrielle Huchet
 *  @version    
 */

public class XX_ClientDelivery_Delivered extends SvrProcess  {
	
	Timestamp dateReception;
	Timestamp timeReception;
	String receivedBy;
	String ci_rif;
	
	
	protected String doIt() throws Exception {
		MVLOClientDelivery cDelivery = new	MVLOClientDelivery(getCtx(),getRecord_ID() , get_TrxName());
		cDelivery.setXX_VLO_Status("EN");
		cDelivery.setXX_VLO_DateReception(dateReception);
		cDelivery.setXX_VLO_TimeReception(timeReception);
		cDelivery.setXX_VLO_ReceivedBy(receivedBy);
		cDelivery.setXX_VLO_CI_RIF_Receiver(ci_rif);
		cDelivery.save();
		commit();
		return "Operación Éxitosa. Estado de la Entrega: Entregado";
		
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
		
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_VLO_DateReception"))
				dateReception= ((Timestamp)element.getParameter());
			else if (name.equals("XX_VLO_TimeReception"))
				timeReception= ((Timestamp)element.getParameter());
			else if (name.equals("XX_VLO_ReceivedBy"))
				receivedBy = ((String)element.getParameter());
			else if (name.equals("XX_VLO_CI_RIF_Receiver"))
				ci_rif = ((String)element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}
}
