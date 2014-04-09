package compiere.model.bank.processes;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.MBankAccount;

import compiere.model.bank.MBankAccountDoc;
import compiere.model.bank.MVCNCheck;
import compiere.model.bank.X_XX_VCN_Check;

public class XX_VCN_DeactiveAccount extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		MBankAccount aux = new MBankAccount(getCtx(),getRecord_ID(),get_TrxName());
		
		// Int que tiene el Id de la cuenta bancaria
		int Id = (Integer) aux.get_Value("C_BankAccount_ID");
		
		//Verifico si la cuenta tiene chequeras en uso
		
	   			String sql= "Select COUNT(*) as CUENTA"
		   		+ " FROM C_BANKACCOUNTDOC DOC, C_BANKACCOUNT ACC"
		   		+ " WHERE DOC.C_BANKACCOUNT_ID = ACC.C_BANKACCOUNT_ID AND DOC.XX_VCN_STATEOFCHECKSBOOK='1' AND ACC.C_BANKACCOUNT_ID = " + Id;
	   	
		   	PreparedStatement prst = DB.prepareStatement(sql,null);
		   	ResultSet rs = null;

   			// int cuenta tiene la cantidad de chequeras en uso
   			//si es igual a 0 puedo desactivar, en caso que sea mayor a 0 no puedo desactivar 
		   	int Cuenta=0;
		   	
		   	//ejecuto la sentencia SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			Cuenta = rs.getInt("Cuenta");
		   		}// Fin if
		   	} 
		   	catch (Exception e){
				log.saveError("ErrorSql distribucion: ", Msg.getMsg(getCtx(), e.getMessage()));
			}
		   	finally {
			   	//CERRAR CONEXION
					DB.closeResultSet(rs);
					DB.closeStatement(prst);
				}
		   	
		   	//Si no tiene chequeras en uso
		   	if( Cuenta == 0 ){
		   		//DESACTIVO LAS CHEQUERAS Y SUS CHEQUES
		   		String sqlcheq=
		   		"SELECT C_BANKACCOUNTDOC_ID AS CHEQUERA"
		   		+" FROM C_BANKACCOUNTDOC"
		   		+" WHERE C_BANKACCOUNT_ID = "+Id;
		   		PreparedStatement prstcheq = DB.prepareStatement(sqlcheq,null);
			   	ResultSet rscheq = null;
			   	try {
			   		rscheq = prstcheq.executeQuery();
			   		while (rscheq.next()){

			   			int chequera = rscheq.getInt("CHEQUERA");
			   			
			   			//DESACTIVO LOS CHEQUES
			   			String sqlcheque=
						   		"SELECT XX_VCN_CHECK_ID"
						   		+" FROM  XX_VCN_CHECK"
						   		+" WHERE C_BANKACCOUNTDOC_ID = "+chequera;
							   	int result = 0;
							   	PreparedStatement prstcheque = DB.prepareStatement(sqlcheque,null);
							   	ResultSet rscheque = null;
							  //EJECUTO LA SENTENCIA SQL
							   	try {
							   		rscheque = prstcheque.executeQuery();
							   		while (rscheque.next()){
							   				//DESACTIVO EL CHEQUE
							   				desactivarCheque(rscheque.getInt("XX_VCN_CHECK_ID"));
							   		}// Fin while
							   	}catch (Exception e){ System.out.println(e); }
							   	finally {
							   	//CERRAR CONEXION
									DB.closeResultSet(rscheque);
									DB.closeStatement(prstcheque);
								}   	

						//DESACTIVO LA CHEQUERA
					   			MBankAccountDoc chequerades = new MBankAccountDoc(getCtx(), chequera, get_TrxName());
					   			chequerades.setIsActive(false);
					   			chequerades.setXX_VCN_Stateofchecksbook("4");
					   			chequerades.save();

			   		}// Fin while
			   	} 
			   	catch (Exception e){
					log.saveError("ErrorSql distribucion: ", Msg.getMsg(getCtx(), e.getMessage()));
				}
			   	finally {
				   	//CERRAR CONEXION
						DB.closeResultSet(rscheq);
						DB.closeStatement(prstcheq);
				}
		   		
		   		
			  //Desactivo la cuenta bancaria
			   	MBankAccount auxiliar = new MBankAccount(Env.getCtx(),getRecord_ID(),get_Trx());
			   	aux.setIsActive(false);
			   	aux.save();
			   	
				
				//Mensaje= "Se desactivo la cuenta bancaria";
				ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_VCN_DisableAccount"));
	   			return Msg.getMsg(Env.getCtx(), "XX_VCN_DisableAccount");
		   	}//fin if si tiene chequeras en uso
		   	else{
		   		ADialog.info(1, new Container(),  Msg.getMsg(Env.getCtx(), "XX_VCN_NoDeactivateAccount"));
	   			return Msg.getMsg(Env.getCtx(), "XX_VCN_NoDeactivateAccount");
		   	}
	}
	
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
	private void desactivarCheque (int Id){
		//DESACTIVAR EL CHEQUE
				//CREO EL OBJETO
				MVCNCheck aux = new MVCNCheck(Env.getCtx(),Id,get_Trx());
				
				aux.setIsActive(false);
				aux.setXX_VCN_LocationofChecks("6");
				
				java.util.Date utilDate = new java.util.Date(); //FECHA ACTUAL
				long lnMilisegundos = utilDate.getTime();
				java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
				aux.setXX_VCN_CancellationDate(sqlTimestamp);
				
				aux.setXX_VCN_CausesofCancellation("4");
				aux.setIsActive(false);
				aux.save();
		
	}

}
