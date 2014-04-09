package compiere.model.suppliesservices.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class XX_DistribMeters extends SvrProcess{
	private static Object m_readLock = new Object();
	
	@Override
	protected String doIt() throws Exception {
		/** Purchase of Supplies and Services
		 * Maria Vintimilla Funcion 22 **/
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {

			//Context Variables are created, they have to be removed in the Form
			Env.getCtx().setContext( "#C_OrderLineForm_ID", getRecord_ID());
			//Displays the Form
			form.setName(Msg.translate(Env.getCtx(), "XX_DistribMeters"));
			form.openForm(getCtx().getContextAsInt("#XX_L_DISTMETERSFORM_ID"));			
			
		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return "";
	}// Fin doIt

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
}// Fin XX_DistribMeters
