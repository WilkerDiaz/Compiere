package compiere.model.dynamic.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/** Proceso que llama a la forma de Filtro de Busqueda de referencias para ser
 * agregadas posteriormente como parte de un elemento en Folleto (Funcion 4)
 * @author Maria Vintimilla
 * @version 1.0
 * */
public class XX_VME_AddVendorReference extends SvrProcess {

	private static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {	
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {
			Env.getCtx().setContext( "#XX_SearchFilterForm_ID", getRecord_ID());
			Env.getCtx().setContext( "#XX_VME_Table_ID", getTable_ID());
			ProcessInfo process =  getProcessInfo();
			Env.getCtx().setContext( "#XX_VME_Process_ID", process.getAD_Process_ID());
			//Displays the Form
			form.setName(Msg.translate(Env.getCtx(), "XX_VME_SearchFilterForm"));
			form.openForm(getCtx().getContextAsInt("#XX_VME_ADDREFERENCEFORM_ID"));
		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return null;
	}//doIt

	@Override
	protected void prepare() {
		
	}//prepare	
	
} // Fin XX_VME_AddVendorReference
