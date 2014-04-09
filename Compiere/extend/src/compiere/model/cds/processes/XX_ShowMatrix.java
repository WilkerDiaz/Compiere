package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
*
* @author José G. Trías
*/
public class XX_ShowMatrix extends SvrProcess {
	
	int PackageMultiple=0;
	Object dataAux[][];
	static Integer sync = new Integer(0);
	
	@Override
	protected String doIt() throws Exception {
		
	
		FormFrame form = new FormFrame();
		
		synchronized (sync) {
			Env.getCtx().setContext("#XX_RM_LINEREFPROV_ID",getRecord_ID());
			form.openForm(1000010);
		}
		
		AEnv.showCenterScreen(form);
		
		while (form.isVisible())
			Thread.sleep(1000);
		
		return "";
	
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}
}
