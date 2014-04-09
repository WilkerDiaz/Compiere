package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
 


public class XX_DistribByBudgetSalesProcess extends SvrProcess {
	

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}	
	
	static Object m_readLock = new Object();
	
	@Override
	protected String doIt() throws Exception {
		
		FormFrame form = new FormFrame();
		int form_id = 0;
		
		synchronized( m_readLock ) {

			//Context Variables are created, they are to be removed in the Form
			Env.getCtx().setContext("#XX_VMR_SalesBudget_DistributionDetail_ID", getRecord_ID());

			//Displays the Form
			form.setName("Distribution O/C by Sales/Budget");
			form_id = Env.getCtx().getContextAsInt("#XX_L_FORMDIST_SALEBUD_ID");
			form.openForm(form_id);
			}					
		while (form.isVisible())
			Thread.sleep(500);
		AEnv.showCenterScreen(form);
		return "";
	}
	
	
}

