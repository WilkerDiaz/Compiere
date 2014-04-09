package compiere.model.payments;

import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.ProgramCallDocument;

/**
 * 
 * @author Jessica Mendoza
 *
 */
public class pmgBx013 {

	public Vector<String> runI5Program() throws Exception {
		
		/****Se crea el objeto AS400****/
		AS400 myi5 = new AS400("192.168.1.2", "USRCENINT", "USRCENINT"); //cambiar a la IP del servidor
		ProgramCallDocument pcd = null;
		Vector<String> respuesta = new Vector<String>(6); 
		
		try {
			/****Se invoca a la interfaz, y se recibe la información****/
			pcd =  new ProgramCallDocument(myi5, "compiere.model.payments.processes.Bx013");			
			pcd.callProgram("BX013");			

		}catch (Exception e){

			/****Si se genera una excepción se propaga la misma para que pueda ser
			 *  manejada en la clase principal
			 ****/
			throw new Exception(e.getMessage());
		} 
		
		respuesta.add(pcd.getValue("BX013.RCDMOVPAR.CTABANPAR").toString());
		respuesta.add(pcd.getValue("BX013.RCDMOVPAR.TIPMOVPAR").toString());
		respuesta.add(pcd.getValue("BX013.RCDMOVPAR.NROREFPAR").toString());
		respuesta.add(pcd.getValue("BX013.RCDMOVPAR.NROODPPAR").toString());
		respuesta.add(pcd.getValue("BX013.ESTREGPAR").toString());
		//return pcd.getValue("Bx006.RCDMOVPAR").toString()+pcd.getValue("Bx006.ESTREGPAR").toString();
		return respuesta;
	}
}
