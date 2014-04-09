package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 * Distribution Process for Purchase Order by Sales + Budget
 * @author Javier Pino.
 *
 */
public class XX_DistributionOCSalesProcess extends SvrProcess {
	
	static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {
		
		FormFrame form = new FormFrame();
		int form_id = 0;
		
		synchronized( m_readLock ) {

			//Context Variables are created, they are to be removed in the Form
			Env.getCtx().setContext( "#XX_VMR_Sales_DistributionHeader_ID", getRecord_ID());			

			//Displays the Form
			form.setName("Distribution O/C by Sales");
			form_id = Env.getCtx().getContextAsInt("#XX_L_FORMDIST_OCSALES_ID");			
			form.openForm(form_id);
			}
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);		
		
		return null;
	}

	@Override
	protected void prepare() {
		
	}

}
