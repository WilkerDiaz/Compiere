package compiere.model.cds.processes;

import java.awt.Window;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;

import compiere.model.cds.X_XX_VMR_PO_LineRefProv;
import compiere.model.cds.forms.XX_AssociateReferenceWith_Form;
import compiere.model.cds.forms.XX_AssociateReference_Form;

/**
*
* @author José G. Trías
*/
public class XX_AssociateReference extends SvrProcess {

	private static X_XX_VMR_PO_LineRefProv aux = null;
	FormFrame form = new FormFrame();
	static Ctx ctx_aux = new Ctx();
	static String trx_aux;

	
	@Override
	protected String doIt() throws Exception {
		
		aux = new X_XX_VMR_PO_LineRefProv(getCtx(),getRecord_ID(),null);
		
		if(!aux.isXX_WithCharacteristic()){
		
			XX_AssociateReference_Form.loadLineRefProv(aux,getCtx());
			
			Window[] List = AWindow.getWindows();
			
			for (int i = 0; i < List.length; i++) {
				
				if(List[i].getName().equals("Associate") && !List[i].isFocused()){
					List[i].dispose();
				}
			}
			
			form.setName("Associate");
			form.openForm(1000004);
			AEnv.showCenterScreen(form);
			
			while (form.isVisible())
				Thread.sleep(1000);
			
		}
		else
		{
			XX_AssociateReferenceWith_Form.loadLineRefProv(aux,getCtx());
	
			Window[] List = AWindow.getWindows();
			
			for (int i = 0; i < List.length; i++) {
				
				if(List[i].getName().equals("Associate With") && !List[i].isFocused()){
					List[i].dispose();
				}
			}
			
			form.setName("Associate With");
			form.openForm(1000005);
			AEnv.showCenterScreen(form);
			
			while (form.isVisible())
				Thread.sleep(1000);
		}
		
		
		return "";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

}