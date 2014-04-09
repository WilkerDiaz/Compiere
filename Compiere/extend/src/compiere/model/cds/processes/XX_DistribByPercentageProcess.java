package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;

import compiere.model.cds.forms.XX_DistribByPercentageForm;

public class XX_DistribByPercentageProcess extends SvrProcess{

	
	
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		FormFrame form = new FormFrame();
				
		XX_DistribByPercentageForm.setContext(getCtx());
		XX_DistribByPercentageForm.setRecord(getRecord_ID());
		XX_DistribByPercentageForm.setTransaction(get_Trx());
		
		form.openForm(getCtx().getContextAsInt("#XX_L_FORMDIST_PERCENT_ID"));
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
