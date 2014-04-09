package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;

public class XX_CheckupUnsolicitedProduct extends SvrProcess {

	static Integer sync = new Integer(0);
	
	@Override
	protected String doIt() throws Exception {
	
FormFrame form = new FormFrame();
		
		synchronized (sync) {
			getCtx().setContext("#XX_CheckupInOutLine",getRecord_ID());
			form.openForm(1000030);
		}
		
		AEnv.showCenterScreen(form);
		
		while (form.isVisible())
			Thread.sleep(1000);
		
		return "";
	}

	@Override
	protected void prepare() {
	}

}
