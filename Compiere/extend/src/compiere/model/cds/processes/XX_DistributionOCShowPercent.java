package compiere.model.cds.processes;

import java.awt.Container;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.X_XX_VMR_DistributionHeader;

/**
 * Allows the user to change the store's product distribution
 * @author Javier Pino
 * */
public class XX_DistributionOCShowPercent extends SvrProcess {
		
	static Integer m_readLock = new Integer(0);  //Object that is in charge of locking
	FormFrame form = new FormFrame(); //A form	
		
	@Override //Executes the process
	protected String doIt() throws Exception {
			
		Ctx enviroment = Env.getCtx();
		int form_id = 0;
		X_XX_VMR_DistributionHeader header = 
			new X_XX_VMR_DistributionHeader(getCtx(), getRecord_ID(), get_TrxName());
		
		if (!header.isXX_CalculatedPOSPercentages()
				&& !header.isXX_CalculatedPOSQuantities()) {
			ADialog.error(1, new Container(), "XX_NoPercentages");
			header.setXX_VMR_DistributionType_ID(0);
			header.save();
			commit();
			return "";
		}
			/*Avoid two processes to start displaying a Form at the same time, so they  
			 * don´t conflict their values */
		synchronized( m_readLock ) {	
			
				//Context Variables are created, they are to be removed in the Form 
			enviroment.setContext( "#XX_VMR_QTY_DistributionHeader_ID", getRecord_ID());	
			enviroment.setContext( "#XX_VMR_QTY_ShowPercentages", 1);
			
				//Displays the Form 
			form.setName(
					Msg.getMsg(Env.getCtx(), "XX_PercentageDistribution")
					);
			form_id = Env.getCtx().getContextAsInt("#XX_L_FORMDIST_OCPIECES_ID");
			form.openForm(form_id);
			AEnv.showCenterScreen(form);			
	    }	
		while (form.isVisible())
			Thread.sleep(500);
		return "";
	}

	@Override
	protected void prepare() {
		
		//Unnecessary, there are no parameters
	}

}
