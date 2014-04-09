package compiere.model.cds.processes;

import java.awt.Container;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class XX_CreateCreditNote extends SvrProcess{
	//Persona que  retira la mercancia
	String p_DocNumber = "";
	@Override
	protected String doIt() throws Exception {
		//El registro actual de la Devolucion
		compiere.model.cds.MCreditNotifyReturn RegRetAct = new compiere.model.cds.MCreditNotifyReturn(getCtx(), getRecord_ID(), null);
		Boolean opcion = ADialog.ask(1, new Container(), "Confirm Create Credit Note? ");
		if (opcion){
			String Status = (String)RegRetAct.get_Value("XX_Status");
			if (Status.equals("ACT"))
			{
				//actualizar estado del aviso de credito a Cerrado
				RegRetAct.set_Value("XX_Status", "CER");
				//generar movimiento en la tabla de factura
				//Que campos van a la factura? 
			}
		}
		return null;
	}

	@Override
	protected void prepare() {
		//System.out.println("Entre al proceso Generar nota de credito");
		log.info("Entre al proceso Generar nota de credito");
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null) ;
				else if (name.equals("XX_DOCUMENTNO"))
					p_DocNumber = element.getParameter().toString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

}
