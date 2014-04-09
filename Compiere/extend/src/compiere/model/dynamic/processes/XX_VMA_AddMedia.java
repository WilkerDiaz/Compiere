package compiere.model.dynamic.processes;

import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.X_C_Channel;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.X_XX_VMA_MediaType;

/**
 * 
 * Esta clase contiene el proceso que se va a ejecutar al momento de asociar 
 * un medio de publicación a una acción de mercadeo
 * 
 * @author Alejandro Prieto
 * @version 1.0
 */

public class XX_VMA_AddMedia extends SvrProcess{
	
	//identificador de la acción de mercadeo
	private int p_XX_VMA_MarketingActivity_ID = 0;
	
	//medio de publicación exitente que se va a asociar
	private int Medio = 0;
	
	// Nuevo medio de publicación que se quiere asociar, pero que no existe 
	// actualmente en compiere
	private String MedioValue = null;
	
	/**
	 * este es un procedimiento para tomar los valores de los parámetros
	 * necesarios para ejecutar el proceso de asociación del medio de
	 * publicación
	 */
	protected void prepare() {	
		
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;	
			else if (name.equals("C_Channel_ID"))
				Medio = para[i].getParameterAsInt();
			else if (name.equals("XX_VMA_NewMedia"))
				MedioValue = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_XX_VMA_MarketingActivity_ID = getRecord_ID();
	}
	
	/**
	 * Esta función asocia un medio de publicación existente o nuevo
	 * a una acción de mercadeo.
	 */
	protected String doIt() throws Exception {
		
		X_XX_VMA_MediaType medio = new X_XX_VMA_MediaType(Env.getCtx(), 0, null);
		//X_XX_VMA_MarketingActivity accion = new X_XX_VMA_MarketingActivity(getCtx(),p_XX_VMA_MarketingActivity_ID,null);
		//X_C_Channel canal = new X_C_Channel(getCtx(),Medio,null);
		boolean bool = false;
		//String lista = accion.getXX_VMA_MediaList();
		//System.out.println("el medio es el número "+Medio);
		medio.setXX_VMA_MarketingActivity_ID(p_XX_VMA_MarketingActivity_ID);
		//si no se eligió un medio existente o se introdujo uno nuevo, fracasa el proceso
		//de asociación
		if(Medio==0 && MedioValue==null){
			ADialog.error(Env.WINDOW_INFO, null, "Error",
					Msg.translate(Env.getCtx(), "XX_Media"));
			throw new Exception(Msg.translate(Env.getCtx(), "XX_MediaNot"));
		}//si se eligió un medio existente se asocia a la acción de mercadeo
		else if(Medio!=0 && MedioValue==null){
			medio.setC_Channel_ID(Medio);
			bool=medio.save();
			if(bool){
				return Msg.translate(Env.getCtx(), "XX_PubliMedia");
			}
			else{
				ADialog.error(Env.WINDOW_INFO, null, 
						Msg.translate(Env.getCtx(), "XX_MediaActivity"));
				throw new Exception(Msg.translate(Env.getCtx(), "XX_MediaNot"));
			}
			//si no se eligió un medio existente y se coloco uno nuevo, este se crea
			//primero como un canal de publicación y luego se asocia a la acción de mercadeo
		}
		else if(Medio==0 && MedioValue!=null){
			//creación del nuevo medio de publicación
			X_C_Channel canal = new X_C_Channel(Env.getCtx(),0,null);
			canal.setName(MedioValue);
			canal.save();
			//se asocia el medio de publicación a la acción de mercadeo
			medio.setC_Channel_ID(canal.getC_Channel_ID());
			bool=medio.save();
			if(bool)
				return Msg.translate(Env.getCtx(), "XX_PubliMedia");
			else{
				ADialog.error(Env.WINDOW_INFO, null, "Error", 
						Msg.translate(Env.getCtx(), "XX_MediaActivity"));
				throw new Exception(Msg.translate(Env.getCtx(), "XX_MediaNot"));
			}			
		}
		else{
			ADialog.error(Env.WINDOW_INFO, null, "Error", 
					Msg.translate(Env.getCtx(), "XX_ChooseChannel"));
			throw new Exception(Msg.translate(Env.getCtx(), "XX_MediaNot"));
		}
		
	}// Fin doIt
	
} // Fin XX_VMA_AddMedia
