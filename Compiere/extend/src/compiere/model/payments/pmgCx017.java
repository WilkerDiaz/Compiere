package compiere.model.payments;

import java.math.BigDecimal;
import com.ibm.as400.access.AS400;
import com.ibm.as400.data.ProgramCallDocument;

/**
 * Se encarga de transferir la información a la CX017, y retornar el resultado de la transferencia
 * @author Jessica Mendoza
 *
 */
public class pmgCx017 {

	public String runI5Program(BigDecimal codCiaPar, BigDecimal codSucPar, String nomArcPar, 
			String sisOriPar, BigDecimal nroCmpPar, BigDecimal nroLinPar, String estreGPar, 
			String userName, String host) throws Exception {
		
		/****Se crea el objeto AS400****/
		AS400 myi5 = new AS400(host, userName, userName); 
		ProgramCallDocument pcd = null;
		
		try {
			/****Se invoca a la interfaz, y se le asignan los parámetros que requiere****/
			pcd =  new ProgramCallDocument(myi5, "compiere.model.payments.processes.cx017");
			
			pcd.setValue("CX017.CODCIAPAR", codCiaPar);
			pcd.setValue("CX017.CODSUCPAR", codSucPar);
			pcd.setValue("CX017.NOMARCPAR", nomArcPar);
			pcd.setValue("CX017.SISORIPAR", sisOriPar);
			pcd.setValue("CX017.NROCMPPAR", nroCmpPar);
			pcd.setValue("CX017.NROLINPAR", nroLinPar);
			pcd.setValue("CX017.ESTREGPAR", estreGPar);			
			pcd.callProgram("CX017");			

		}catch (Exception e){

			/****Si se genera una excepción se propaga la misma para que pueda ser
			 *  manejada en la clase principal
			 ****/
			throw new Exception(e.getMessage());
		} 
		return pcd.getValue("CX017.ESTREGPAR").toString();
	}
}
