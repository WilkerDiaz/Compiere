package compiere.model.cds.processes;

import java.util.logging.Level;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 * Distribution Process for Purchase Order by Sales + Budget
 * @author Javier Pino.
 *
 */
public class XX_PlacedOrderSearch extends SvrProcess {
	
	static Object m_readLock = new Object();
	static int glued = -1, hanging = -1;
	static int check_assistant = 0;
	

	@Override
	protected String doIt() throws Exception {
		
		FormFrame form = new FormFrame();
		int form_id = 0;
		
		synchronized( m_readLock ) {
			
				

			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Hanging",hanging);
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Glued", glued);
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Reprint", 1);
			form.setName("Print Product Labels");
			form_id = Env.getCtx().getContextAsInt("#XX_L_FPLACEDORDERSEARCH_ID");			
			form.openForm(form_id);	
		}
			AEnv.showCenterScreen(form);
			while (form.isVisible())
				Thread.sleep(500);
		return null;
	}
	
	

	@Override
	protected void prepare() {
		
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("Printer_Hanging")) {
				for(int i=0; i<services.length; i++){
					if(services[i].getName().equals(element.getParameter())){
						hanging = i;
						break;
					}
				}
			} else if (name.equals("Printer_Glued")) {
				for(int i=0; i<services.length; i++){
					if(services[i].getName().equals(element.getParameter())){
						glued = i;
						break;
					}
				}
			} else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}
		
	}
}
