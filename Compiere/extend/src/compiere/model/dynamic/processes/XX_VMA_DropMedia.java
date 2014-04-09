package compiere.model.dynamic.processes;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.X_XX_VMA_MarketingActivity;
import compiere.model.dynamic.X_XX_VMA_MediaType;

/**
 * 
 * Esta clase contiene el proceso que se va a ejecutar al momento de querer
 * eliminar un medio de publicación asociado a una acción de mercadeo
 * 
 * @author Alejandro Prieto
 * @version 1.0
 */

public class XX_VMA_DropMedia extends SvrProcess {

	private int p_XX_VMA_MarketingActivity_ID = 0;
	//private int Medio = 0;
	
	protected void prepare() {	

		/*ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;	
			else if (name.equals("XX_VMA_MediaType_ID"))
				Medio = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}*/
		p_XX_VMA_MarketingActivity_ID = getRecord_ID();
	} // Fin prepare
	
	/**
	 * doIt
	 * Esta función ejecuta el proceso de eliminar o desasociar un medio de publicación
	 * de una acción de mercadeo
	 */
	protected String doIt() throws Exception {
		
		X_XX_VMA_MarketingActivity accion = 
			new X_XX_VMA_MarketingActivity(Env.getCtx(),p_XX_VMA_MarketingActivity_ID,null);
		int Medio = accion.getXX_VMA_MediaType_ID();
		//System.out.println("el medio es el número "+Medio);
		X_XX_VMA_MediaType medio = 
			new X_XX_VMA_MediaType(Env.getCtx(), Medio, null);
		boolean bool = false;
		
		// Se elimina la relación medio de publicación-acción de mercadeo
		bool = medio.delete(true);
		
		// Se verifica si se eliminó correctamente o no
		if(bool){
			accion.setXX_VMA_MediaType_ID(0);
			accion.save();
			return Msg.translate(Env.getCtx(), "XX_PubliMediaRemoved");
		}
		else{
			ADialog.error(Env.WINDOW_INFO, null, "Error", 
					Msg.translate(Env.getCtx(), "XX_PubliMediaError"));
			throw new Exception(Msg.translate(Env.getCtx(), "XX_MediaNot"));
		}
	} // Fin doIt
} // Fin XX_VMA_DropMedia
