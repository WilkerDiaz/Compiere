package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

public class XX_DistribBySalesProcess extends SvrProcess {

	private static Object m_readLock = new Object();
	
	@Override
	protected String doIt() throws Exception {
		
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {

			//Context Variables are created, they are to be removed in the Form
			Env.getCtx().setContext( "#XX_VMR_Sales_DistributionDetail_ID", getRecord_ID());
				//Displays the Form
			form.setName("Distribution by Sales");
			form.openForm(getCtx().getContextAsInt("#XX_L_FORMDIST_SALES_ID"));			
			}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
