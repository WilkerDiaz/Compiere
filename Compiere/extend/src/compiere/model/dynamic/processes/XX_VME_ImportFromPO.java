package compiere.model.dynamic.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MOrder;

/** Proceso que llama a la forma para la inserción de productos desde una
 * orden de compra a un folleto determinado 
 * @author mvintimilla
 * */
public class XX_VME_ImportFromPO  extends SvrProcess{
	
	private static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {	
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {
			Env.getCtx().setContext( "#XX_COrder_ID", getRecord_ID());
			MOrder order = new MOrder(getCtx(), getRecord_ID(), get_Trx());
			Env.getCtx().setContext( "#Brochure_ID", order.getXX_Brochure_ID());
			
			//Displays the Form
			form.setName(Msg.translate(Env.getCtx(), "Importar referencias desde OC"));
			form.openForm(getCtx().getContextAsInt("#XX_L_IMPORTFROMPO_ID"));
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
