package compiere.model.dynamic.processes;

import java.awt.Container;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.X_XX_VMA_RelCampaign;

/**
 * 
 * Esta clase contiene el proceso que se va a ejecutar al momento de agregar 
 * una colección a una campaña.
 * 
 * @author María Vintimilla
 * @version 1.0
 */

public class XX_VMA_AddCollection extends SvrProcess{

	private int C_Campaign_ID = 0;
	private int Collection_ID = 0;
	
	protected void prepare() {	
		
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("XX_VMR_Collection_ID"))
				Collection_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		C_Campaign_ID = getRecord_ID();
		
	}//prepare
	
	/**
	 * Función que se encarga de asociar una colección a una campaña
	 */
	protected String doIt() throws Exception {
		X_XX_VMA_RelCampaign cc = 
			new X_XX_VMA_RelCampaign(Env.getCtx(),0,null);

		cc.setXX_VMR_Collection_ID(Collection_ID);
		cc.setC_Campaign_ID(C_Campaign_ID);
		
		//se verifica si se salvó correctamente
		if(cc.save()){
			ADialog.info(1, new Container(),
					Msg.translate(Env.getCtx(), "XX_RelCampaign"));
			return "";
		}
		else{
			ADialog.error(Env.WINDOW_INFO, null, "Error", 
					Msg.translate(Env.getCtx(), "XX_ErrorCollection"));
			throw new Exception("No se agregó la colección");
		}	
		
	} // doIt
	
} // XX_VMA_AddCollection
