package compiere.model.dynamic.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/** XX_VME_ModifyVendorReference
 * Proceso que llama a la forma de modifiación de cantidades de productos y 
 * referencias asociadas a un elemento en folleto
 * @author Maria Vintimilla
 * @version 1.0 
 * */
public class XX_VME_ModifyVendorRef extends SvrProcess {

	private static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {	
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {
			Env.getCtx().setContext( "#XX_ModifyQtyForm_ID", getRecord_ID());
			
			//Displays the Form
			form.setName(Msg.translate(Env.getCtx(), "XX_ModifyQty"));
			form.openForm(getCtx().getContextAsInt("#XX_L_QUANTITYPRICEFORM_ID"));
		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return null;
	}//doIt

	@Override
	protected void prepare() {
		
	}//prepare	
	
} // Fin XX_VME_ModifyVendorReference
