package compiere.model.cds.processes;

import java.awt.Container;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.X_XX_VMR_DistributionDetail;

public class XX_DistributionRedistShowPerc extends SvrProcess{

	static Integer m_readLock = new Integer(0);  //Object that is in charge of locking
	FormFrame form = new FormFrame(); //A form
	
	@Override
	protected String doIt() throws Exception {
		Ctx enviroment = Env.getCtx();		
		X_XX_VMR_DistributionDetail detail = 
			new X_XX_VMR_DistributionDetail(getCtx(), getRecord_ID(), get_TrxName());
		
		if (!detail.isXX_DistributionApplied()) {
			ADialog.error(1, new Container(), "XX_NoPercentages");
			detail.setXX_VMR_DistributionType_ID(0);
			detail.save();
			commit();
			return "";
		}
		
		/*Avoid two processes to start displaying a Form at the same time, so they  
			 * don´t conflict their values */
		synchronized( m_readLock ) {	
			
			//Context Variables are created, they are to be removed in the Form  
			enviroment.setContext( "#XX_VMR_QTY_DistributionDetail_ID", getRecord_ID());
			enviroment.setContext( "#XX_VMR_QTY_ShowPercentages", 1);
			form.setName(
					Msg.getMsg(Env.getCtx(), "XX_PercentageDistribution")
					);
			//Displays the Form
			form.openForm(getCtx().getContextAsInt("#XX_L_FORMDIST_PIECES_ID"));
			AEnv.showCenterScreen(form);					
	    }		
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return "";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
}	


