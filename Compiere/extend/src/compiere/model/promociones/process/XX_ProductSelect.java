package compiere.model.promociones.process;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class XX_ProductSelect extends SvrProcess{
	private static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {
		// Proceso que llama a la forma para la consulta de O/C y Pedidos
		// en Dinámica Comercial
		
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {
			
			
			//Context Variables are created, they have to be removed in the Form
			Env.getCtx().setContext( "#promotion_ID", getRecord_ID());
			
			//Displays the Form
			//form.setName(Msg.translate(Env.getCtx(), "XX_ConsultPO"));
			form.openForm(getCtx().getContextAsInt("#XX_L_FORMPRODUCTSELECT_ID"));
			
			/*			
			XX_ProductOCForm.setContext(getCtx());
			XX_ProductOCForm.setRecord(getRecord_ID());
			XX_ProductOCForm.setTrxName(get_TrxName());
			*/

		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return null;
	}//doIt

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}//prepare	
}
