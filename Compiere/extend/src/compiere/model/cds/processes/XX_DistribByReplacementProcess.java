package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;


public class XX_DistribByReplacementProcess extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		FormFrame form = new FormFrame();
		
		//Context Variables are created, they are to be removed in the Form
		Env.getCtx().setContext( "#XX_VMR_Replacement_DistributionHeader_ID", getRecord_ID());
		form.openForm(getCtx().getContextAsInt("#XX_L_FORMDIST_REPLACE_ID"));
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
