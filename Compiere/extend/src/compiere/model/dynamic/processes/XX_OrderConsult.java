package compiere.model.dynamic.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/** Proceso que llama a la forma para la consulta de O/C y Pedidos en Dinámica 
 * Comercial
 * @author mvintimilla
 * */
public class XX_OrderConsult  extends SvrProcess{
	
	private static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {			
			//Context Variables are created, they have to be removed in the Form
			Env.getCtx().setContext( "#XX_ConsultPOForm_ID", getRecord_ID());
			
			//Displays the Form
			form.setName(Msg.translate(Env.getCtx(), "XX_ConsultPO"));
			form.openForm(getCtx().getContextAsInt("#XX_L_ORDERCONSULTFORM_ID"));

		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return null;
	}//doIt

	@Override
	protected void prepare() {
		
	}//prepare	

}//Fin XX_ConsultPO
