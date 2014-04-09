package compiere.model.suppliesservices.processes;

import java.awt.Container;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;

public class XX_DistribPercenta extends SvrProcess{
	private static Object m_readLock = new Object();
	private boolean hasLines = false;
	
	@Override
	protected String doIt() throws Exception {
		/** Purchase of Supplies and Services
		 * Maria Vintimilla Funcion 35 **/
		FormFrame form = new FormFrame();
		MOrder order = null;
		MOrderLine OrderLine = null;
		int currentTable = getProcessInfo().getTable_ID();
		
		// Funcion 105 Dist OC
		// Se establece si la distribucion se realiza desde la cabecera o el detalle
		if(currentTable == Env.getCtx().getContextAsInt("#XX_L_CORDERTABLE_ID")) {
			order = new MOrder(Env.getCtx(), getRecord_ID(), get_Trx());
			Env.getCtx().setContext( "#Tipo", 1);
			if(order.getXX_NumLinesOrder() == 0){
				ADialog.error(1, new Container(),Msg.translate(Env.getCtx(), "XX_OrderLines"));
			}
		}
		else if (currentTable == Env.getCtx().getContextAsInt("#XX_L_CORDERLINETABLE_ID")){
			OrderLine = new MOrderLine(Env.getCtx(), getRecord_ID(), get_Trx());
			Env.getCtx().setContext( "#Tipo", 2);
		}
		
		synchronized( m_readLock ) {
			Env.getCtx().setContext( "#C_OrderLineForm_ID", getRecord_ID());
			
			//Displays the Form
			form.setName(Msg.translate(Env.getCtx(), "XX_DistribPercentage"));
			form.openForm(getCtx().getContextAsInt("#XX_L_DISTPERCENTAGEFORM_ID"));			
		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return "";
	}// Fin doIt

	@Override
	protected void prepare() {

	}
	
}// fin XX_DistribPercenta
