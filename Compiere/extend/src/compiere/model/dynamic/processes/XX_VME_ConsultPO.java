package compiere.model.dynamic.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;


/** Proceso que llama a la forma para la consulta de O/C y Pedidos en Dinámica 
 * Comercial desde una pagina de un folleto bajo la nueva estructura de dinamica
 * comercial para folleto (Funcion 14)
 * @author mvintimilla
 * */
public class XX_VME_ConsultPO  extends SvrProcess{
	
	private static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {	
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {
			Env.getCtx().setContext( "#Element_ID", getRecord_ID());
			form.openForm(getCtx().getContextAsInt("#XX_L_VME_CONSULTPOFORM_ID"));
		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return null;
	}//doIt

	@Override
	protected void prepare() {
		
	}//prepare	

}//Fin XX_VME_ConsultPO
