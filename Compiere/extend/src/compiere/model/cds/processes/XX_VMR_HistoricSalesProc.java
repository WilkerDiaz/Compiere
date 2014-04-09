package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/** Proceso que llama a la forma para la consulta el historico 
 * de ventas asociados a departamento, categoria, tienda y fecha
 * @author mvintimilla
 * */
public class XX_VMR_HistoricSalesProc  extends SvrProcess{
	
	private static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {	
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {
			Env.getCtx().setContext( "#XX_HistoricSalesForm_ID", getRecord_ID());
			
			//Displays the Form
			form.setName(Msg.translate(Env.getCtx(), "XX_HistoricSales"));
			form.openForm(getCtx().getContextAsInt("#XX_L_HISTORICSALESFORM_ID"));
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
