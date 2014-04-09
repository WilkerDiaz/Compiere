package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

public class XX_PO_DistribByBudgetProcess extends SvrProcess{

	static Integer m_readlock = 0;
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		FormFrame form = new FormFrame();
		synchronized( m_readlock ) {
			Env.getCtx().setContext("#XX_VMR_BUD_DistributionHeader_ID",getRecord_ID());		
			form.openForm(getCtx().getContextAsInt("#XX_L_FORMDIST_OCBUDGET_ID"));
			AEnv.showCenterScreen(form);
		}	
		while (form.isVisible())
			Thread.sleep(500);	
		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
}




	


