package compiere.model.cds.processes;

import java.util.logging.Level;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

public class XX_ViewBenefitVendorProcess extends SvrProcess {
	
	static Integer sync = new Integer(0);

	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		
		FormFrame form = new FormFrame();
		synchronized (sync) {
			Env.getCtx().setContext("#vendorRating_ID", getRecord_ID());
			form.openForm(getCtx().getContextAsInt("#XX_L_FORMVIEWBENEFITVENDOR_ID"));
		}
		AEnv.showCenterScreen(form);
		
		while (form.isVisible())
			Thread.sleep(1000);
		
		return "";
	}
}
