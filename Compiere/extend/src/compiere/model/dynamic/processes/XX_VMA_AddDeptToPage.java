package compiere.model.dynamic.processes;

import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.X_XX_VMA_PageDepartment;

/**
 * 
 * Esta clase contiene el proceso que se va a ejecutar al momento de agregar 
 * un departamento a una página de un folleto.
 * 
 * @author Alejandro Prieto
 * @version 1.0
 */

public class XX_VMA_AddDeptToPage extends SvrProcess{

	// contiene el identificador del registro donde se llama al proceso
	private int p_XX_VMA_BrochurePage_ID = 0;
	
	// contiene el identificador del departamento que se pasa como parámetro
	private int Department = 0;
	
	/**
	 * Se toman los parámetros del proceso, en este caso sólo se captura la 
	 * página a la cual se quiere mover el elemento.
	 */
	protected void prepare() {	
		
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("XX_VMA_PageDept"))
				Department = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_XX_VMA_BrochurePage_ID = getRecord_ID();
		
	}//prepare
	
	/**
	 * está función es la encargada de asociar un departamento a una página del folleto
	 */
	protected String doIt() throws Exception {
		
		//se crea una nueva relación página-departamento
		X_XX_VMA_PageDepartment pd = 
			new X_XX_VMA_PageDepartment(Env.getCtx(),0,null);
		boolean bool = true;
		pd.setXX_VMR_Department_ID(Department);
		pd.setXX_VMA_BrochurePage_ID(p_XX_VMA_BrochurePage_ID);
		
		//se verifica si se salvó correctamente
		if(pd.save()){
			//return "Se agregó el departamento correctamente";
			return Msg.translate(Env.getCtx(), "XX_AddDepartment");
		}
		else{
			ADialog.error(Env.WINDOW_INFO, null, "Error", 
					Msg.translate(Env.getCtx(), "XX_DeptPage"));
			throw new Exception(Msg.translate(Env.getCtx(), "XX_DeptNotAdded"));
		}	
		
	}//doIt
	
}//XX_VMA_AddDeptToPage
