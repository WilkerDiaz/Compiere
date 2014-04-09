package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 * Distribution Process for Purchase Order by Sales 
 * @author Patricia Ayuso
 *
 */
public class XX_DistributionOCSalesBudgetPro extends SvrProcess {
	
	static Object m_readLock = new Object();
	
	@Override
	protected String doIt() throws Exception {
		
		FormFrame form = new FormFrame();
		int form_id = 0;
		
		synchronized( m_readLock ) {

			//Context Variables are created, they are to be removed in the Form
			Env.getCtx().setContext( "#XX_VMR_SalesBudget_DistributionHeader_ID", getRecord_ID());			

			//Displays the Form
			form.setName("Distribution O/C by Sales/Budget");
			form_id = Env.getCtx().getContextAsInt("#XX_L_FORMDIST_OCSALBUD_ID");
			form.openForm(form_id);
			}					
		while (form.isVisible())
			Thread.sleep(500);
		AEnv.showCenterScreen(form);
		return "";
	}

	@Override
	protected void prepare() {
		
	}

}
