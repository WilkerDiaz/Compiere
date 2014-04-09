package compiere.model.payments;

import java.math.BigDecimal;

import org.compiere.util.Env;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.ProgramCallDocument;

/**
 * 
 * @author Jessica Mendoza
 *
 */
public class pmgBx011 {

	public String runI5Program(BigDecimal codCiaPar, BigDecimal nroProPar, String nomProPar,
			String nroRifPar, String dirProPar, String tipProPar, String tlfProPar, 
			String faxProPar, String perCo1Par, String perCo2Par, String tipPerPar, 
			String estProPar, String ctaProPar, String opeRegPar, String sisOriPar, 
			String email,  String EstreGPar) throws Exception {
		
		/****Se crea el objeto AS400****/
		AS400 myi5 = new AS400(Env.getCtx().getContext("#XX_L_HOST"), 
				Env.getCtx().getContext("#XX_L_USERBANK"), 
				Env.getCtx().getContext("#XX_L_PASSWORDBANK")); 
		ProgramCallDocument pcd = null;
		
		try {
			/****Se invoca a la interfaz, y se le asignan los parámetros que requiere****/
			pcd =  new ProgramCallDocument(myi5, "compiere.model.payments.processes.bx011");
			
			pcd.setValue("BX011.CODCIAPAR", codCiaPar);
			pcd.setValue("BX011.NROPROPAR", nroProPar);
			pcd.setValue("BX011.NOMPROPAR", nomProPar);
			pcd.setValue("BX011.NRORIFPAR", nroRifPar);
			pcd.setValue("BX011.DIRPROPAR", dirProPar);
			pcd.setValue("BX011.TIPPROPAR", tipProPar);
			pcd.setValue("BX011.TLFPROPAR", tlfProPar);
			pcd.setValue("BX011.FAXPROPAR", faxProPar);
			pcd.setValue("BX011.PERCO1PAR", perCo1Par);
			pcd.setValue("BX011.PERCO2PAR", perCo2Par);
			pcd.setValue("BX011.TIPPERPAR", tipPerPar);
			pcd.setValue("BX011.ESTPROPAR", estProPar);
			pcd.setValue("BX011.CTAPROPAR", ctaProPar);
			pcd.setValue("BX011.OPEREGPAR", opeRegPar);
			pcd.setValue("BX011.SISORIPAR", sisOriPar);
			pcd.setValue("BX011.CORREOPAR", email);
			pcd.setValue("BX011.ESTREGPAR", EstreGPar);
			

			
			pcd.callProgram("BX011");			

		}catch (Exception e){

			/****Si se genera una excepción se propaga la misma para que pueda ser
			 *  manejada en la clase principal
			 ****/
			throw new Exception(e.getMessage());
		} 
		return pcd.getValue("BX011.ESTREGPAR").toString();
	}
}
