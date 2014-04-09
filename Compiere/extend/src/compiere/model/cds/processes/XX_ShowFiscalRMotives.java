package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/*
 * Created by: JTrias
 */
public class XX_ShowFiscalRMotives extends SvrProcess {

	static Integer sync = new Integer(0);
	
	@Override
	protected String doIt() throws Exception {
		
		FormFrame form = new FormFrame();
		
		synchronized (sync) {
			Env.getCtx().setContext("#XX_SHOWFISCALMOTIVE_ORDER_ID",getRecord_ID());
			form.openForm(1000029);
		}
		
		AEnv.showCenterScreen(form);
		
		return "";
	}

	@Override
	protected void prepare() {
	
	}

}
