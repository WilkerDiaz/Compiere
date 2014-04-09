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

public class XX_DistribSales extends SvrProcess{

	private static Object m_readLock = new Object();
	private boolean hasLines = false;
	
	@Override
	protected void prepare() {
	
	}
	
	@Override
	protected String doIt() throws Exception {
		/** Purchase of Supplies and Services
		 * Maria Vintimilla Funcion 31 **/
		FormFrame form = new FormFrame();
		MOrder order = null;
		MOrderLine OrderLine = null;
		int currentTable = getProcessInfo().getTable_ID();
		
		if(currentTable == Env.getCtx().getContextAsInt("#XX_L_CORDERTABLE_ID")) {
			order = new MOrder(Env.getCtx(), getRecord_ID(), get_Trx());
			Env.getCtx().setContext( "#Tipo", 1);
			if(order.getXX_NumLinesOrder() == 0){
				ADialog.error(1, new Container(),Msg.translate(Env.getCtx(), "XX_OrderLines"));
			}// else distribucion
		}
		else if (currentTable == Env.getCtx().getContextAsInt("#XX_L_CORDERLINETABLE_ID")){
			OrderLine = new MOrderLine(Env.getCtx(), getRecord_ID(), get_Trx());
			Env.getCtx().setContext( "#Tipo", 2);
		}
		
		synchronized( m_readLock ) {
			//Context Variables are created, they have to be removed in the Form
			Env.getCtx().setContext( "#C_OrderLineForm_ID", getRecord_ID());
			
			//Displays the Form
			form.setName(Msg.translate(Env.getCtx(), "XX_DistribSales"));
			form.openForm(getCtx().getContextAsInt("#XX_L_DISTSALESFORM_ID"));			
		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return null;
	}// Fin doIt
	
}// Fin XX_DistribSales
