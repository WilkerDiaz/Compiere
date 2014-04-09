package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;
import org.compiere.util.Env;

import compiere.model.cds.X_XX_VMR_PO_LineRefProv;

/**
*
* @author José G. Trías
*/
public class XX_GenerateMatrix extends SvrProcess {

	
	int PackageMultiple=0;
	Object dataAux[][];
	static Ctx ctx_aux = new Ctx();
	static String trx_aux;
	static Integer sync = new Integer(0);
	
	@Override
	protected String doIt() throws Exception {
		
		/*
		 * Código realizado por  José G. Trías
		 * Matriz de Características dinámicas 
		 */ 
		X_XX_VMR_PO_LineRefProv aux = new X_XX_VMR_PO_LineRefProv(getCtx(),getRecord_ID(),get_TrxName());
		
		FormFrame form = new FormFrame();
		
		synchronized (sync) {
			Env.getCtx().setContext("#XX_RM_LINEREFPROV_ID",getRecord_ID());
			Env.getCtx().setContext("#GenerateMatrix","Y");
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
