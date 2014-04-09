package compiere.model.dynamic.processes;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_XX_VMA_BrochurePage;
import compiere.model.dynamic.X_XX_VME_Elements;

/**
 * XX_VME_ChangeElementPage
 * Está clase representa el proceso que se ejecuta a la hora de querer
 * mover un elemento entre páginas de un mismo folleto.
 * @author mvintimilla
 * @version 1.0
 */
public class XX_VME_ChangeElementPage extends SvrProcess{

	// Elemento que se desea mover
	private int p_XX_VME_Element_ID = 0;
	
	// Pagina que contiene al elemento
	private int PageNum = 0;
	
	// booleano para determinar el cambio de pagina
	boolean cambiar = false;
	
	/**
	 * prepare
	 * Se toman los parámetros del proceso, en este caso sólo se captura la 
	 * página a la cual se quiere mover el elemento.
	 */
	protected void prepare() {	
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("XX_VME_PageNum"))
				PageNum = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_XX_VME_Element_ID = getRecord_ID();
	} // Fin prepare
	
	/**
	 * doIt
	 * Está función cambia un elemento de una página a otra del folleto.
	 * No se pueden agregar elementos de tipo imagen a una página de productos.
	 * Por otro lado no se pueden agregar elementos de tipo producto a páginas
	 * de tipo imagen.
	 * 
	 * @return String mensaje de notificación que se le envía al usuario
	 */
	protected String doIt() throws Exception {
		boolean bool;
		X_XX_VME_Elements element = 
			new X_XX_VME_Elements(Env.getCtx(), p_XX_VME_Element_ID, null);
		
		// Pagina a donde va el elemento
		X_XX_VMA_BrochurePage page = 
			new X_XX_VMA_BrochurePage(Env.getCtx(), PageNum, null);
		
		String typePage = page.getXX_VMA_PageType();
		String typeElement = element.getXX_VME_Type();
		
		/* Se verifica que se haya elegido un pagina a donde mover el elemento */
		if(element.getXX_VMA_BrochurePage_ID() == PageNum || PageNum == 0){
			addLog(Msg.translate(Env.getCtx(), "XX_MoveElement"));
			throw new Exception(Msg.translate(Env.getCtx(), "XX_MoveItem"));
		} // if pagenum

		/* Se verifica si el elemento es producto o es una imagen */
		if(typeElement.equals("P") || typeElement.equals("M")){
			if(typePage.equals("P")||typePage.equals("M")){
				/* Se verifica si el departamento existe en la página a donde 
				 * se va a mover el elemento de lo contrario se agrega el 
				 * departamento a la página */
				String SQLReference = " SELECT DISTINCT R.XX_VMR_DEPARTMENT_ID DEP" +
						" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
						" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
						" WHERE E.XX_VME_ELEMENTS_ID = " + p_XX_VME_Element_ID;
				//System.out.println("SQLREF : "+SQLReference);
				PreparedStatement pstmtRef = null;
				ResultSet rsRef = null;
				try{
					pstmtRef = DB.prepareStatement(SQLReference,null);
					rsRef = pstmtRef.executeQuery();
					while(rsRef.next()){
						bool = XX_VME_GeneralFunctions.agregarDept(PageNum, 
								rsRef.getInt("DEP"),Env.getCtx());
					}
				} // try
				catch(SQLException e){ throw new Exception("Error referencia " + e); }
				finally{
					DB.closeResultSet(rsRef);
					DB.closeStatement(pstmtRef);
				}
				element.setXX_VMA_BrochurePage_ID(PageNum);
				element.save();
			} // if
			else{
				/* No puede agregar un producto a una página de imágenes */
				addLog(Msg.translate(Env.getCtx(), "XX_ImagePage"));
				throw new Exception(Msg.translate(Env.getCtx(), "XX_MoveItem"));
			} // else
			return Msg.translate(Env.getCtx(), "XX_MovedCorrectly");
		}
		else {
			/* El elemento es de tipo imagen la página a donde se va a mover 
			 * tiene que ser de tipo imagen o mixta.
			 */
			if(typePage.equals("I")||typePage.equals("M")){
				element.setXX_VMA_BrochurePage_ID(PageNum);
				element.save();
			}
			else{
				addLog(Msg.translate(Env.getCtx(), "XX_ImageProdPage"));
				throw new Exception(Msg.translate(Env.getCtx(), "XX_MoveItem"));
			}
			return Msg.translate(Env.getCtx(), "XX_MovedCorrectly");
		} // else

	} // Fin doIt
	
} // Fin XX_VME_ChangeElementPage