/**
 * 
 */
package compiere.model.cds.processes;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 * @author Jorge Pires
 *
 */
public class XX_DistributionOCPercentualProc extends SvrProcess {

	FormFrame form = new FormFrame();
	static Integer m_readLock = new Integer(0); //Object that is in charge of locking

	public XX_DistributionOCPercentualProc() {
	
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
		int form_id = 0;
		
		synchronized( m_readLock ) {
			Env.getCtx().setContext("#XX_VMR_DISTRIBUTIONHEADER_ID",getRecord_ID());			
			form_id = Env.getCtx().getContextAsInt("#XX_L_FORMDIST_OCPERCENT_ID");
			Env.getCtx().setContext("#ADJUSTMENT_AUTOMATIC",0);	//AGREGADO POR GHUCHET
			form.openForm(form_id);
			AEnv.showCenterScreen(form);			
		}
		while (form.isVisible())
			Thread.sleep(500);
		return "";
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {

	}

}
