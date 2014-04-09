package compiere.model.payments;

import java.math.BigDecimal;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.ProgramCallDocument;

/**
 * Proceso que se encarga de llamar a la Interfaz de Pago BX014
 * @author Jessica Mendoza
 *
 */
public class pmgBx014 {

	public String runI5Program(BigDecimal codCiaPar, BigDecimal codSucPar, String nomArcPar, 
			//String opeRegPar, 
			String sisOriPar, String estreGPar, 
			BigDecimal totRegPar, String txtErrPar, String user, String password,
			String host) throws Exception {
		
		/****Se crea el objeto AS400****/
		AS400 myi5 = new AS400(host, user, password);
		ProgramCallDocument pcd = null;
		
		try {
			/****Se invoca a la interfaz, y se le asignan los parámetros que requiere****/
			pcd =  new ProgramCallDocument(myi5, "compiere.model.payments.processes.bx014");
			
			pcd.setValue("BX014V2.CODCIAPAR", codCiaPar);
			pcd.setValue("BX014V2.CODSUCPAR", codSucPar);
			pcd.setValue("BX014V2.NOMARCPAR", nomArcPar);
		//	pcd.setValue("BX014V2.OPEREGPAR", opeRegPar);
			pcd.setValue("BX014V2.SISORIPAR", sisOriPar);
			pcd.setValue("BX014V2.ESTREGPAR", estreGPar);
			pcd.setValue("BX014V2.TOTREGPAR", totRegPar);
			pcd.setValue("BX014V2.TXTERRPAR", txtErrPar);
			
			pcd.callProgram("BX014V2");			

		}catch (Exception e){

			/****Si se genera una excepción se propaga la misma para que pueda ser
			 *  manejada en la clase principal
			 ****/
			throw new Exception(e.getMessage());
		} 

		return pcd.getValue("BX014V2.ESTREGPAR").toString();
	}
}
