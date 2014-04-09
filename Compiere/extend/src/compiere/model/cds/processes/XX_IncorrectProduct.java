package compiere.model.cds.processes;

import java.awt.Window;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.swing.CPanel;
import org.compiere.util.Env;

import compiere.model.cds.MInOut;
import compiere.model.cds.MInOutLine;
import compiere.model.cds.MVLOUnsolicitedProduct;
import compiere.model.cds.forms.XX_UnsolicitedProductIncorrect_Form;

/**
*
* @author Rosmaira Arvelo
*/
public class XX_IncorrectProduct extends SvrProcess {
	
	private int m_WindowNo = Env.getCtx().getContextAsInt("#XX_L_W_UNSOLICITEDPRODUCT_ID");	
	private CPanel mainPanel = new CPanel();
	
	@Override
	protected String doIt() throws Exception 
	{
		MVLOUnsolicitedProduct product = new MVLOUnsolicitedProduct(getCtx(),getRecord_ID(),null);
		
		if(product.get_ID()!=0 && product.isActive())
		{
			//** Valido si el chequeo esta completado para poder mover el inventario
			MInOutLine inOutLine = new MInOutLine(Env.getCtx(),product.getM_InOutLine_ID(),null);
			MInOut inOut = new MInOut(Env.getCtx(),inOutLine.getM_InOut_ID(),null);
			
			if(inOut.getDocStatus().equals("CO"))
			{				
				Window[] List = AWindow.getWindows();
				
				for (int i = 0; i < List.length; i++) {
					
					if(List[i].getName().equals("UnsolicitedProductIncorrect") && !List[i].isFocused()){
						List[i].dispose();
					}
				}
				
				XX_UnsolicitedProductIncorrect_Form.loadUnsolicitedProduct(product,Env.getCtx());
				
				FormFrame form = new FormFrame();
				form.setName("UnsolicitedProductIncorrect");
				form.openForm(Env.getCtx().getContextAsInt("XX_L_FORMUNSOLPRODINCOR_ID"));
				AEnv.showCenterScreen(form);
				
				while (form.isVisible())
					Thread.sleep(1000);
			}
			else
			{
				ADialog.info(m_WindowNo, this.mainPanel, "ValidateProdError");
			}
			
		}
		
		return "";
	}
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}
}
