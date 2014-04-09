package compiere.model.bank.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import org.compiere.model.MBankAccount;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.model.X_C_BankAccountDoc;
import org.compiere.framework.*;
import org.compiere.util.*;
import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;

import compiere.model.bank.MBankAccountDoc;
import compiere.model.bank.MVCNCheck;
import compiere.model.bank.X_XX_VCN_Check;

public class XX_VCN_DeactiveCheckBooks extends SvrProcess {
	
		String numero;
		String causa;
		String fecha;
		
		

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_VCN_CheckNumber"))
				numero = element.getParameter().toString();
			else if (name.equals("XX_VCN_CausesofCancellation"))
				causa = element.getParameter().toString();
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
			String Mensaje="";
			
			
			//CREO EL OBJETO
			MBankAccountDoc aux = new MBankAccountDoc(Env.getCtx(),getRecord_ID(),get_Trx());
			//OBTENGO EL ID DE LA CHEQUERA
			int idchequera=aux.getC_BankAccountDoc_ID();
				
				if(aux.get_ValueAsInt("XX_VCN_Stateofchecksbook")==1){
					//LA CHEQUERA ESTA EN USO
					
					//Busco los cheques que son de esa chequera y tienen por ubicacion en oficina	   	
					String sql2=
				    "SELECT XX_VCN_CHECK_ID"
				   	+" FROM XX_VCN_CHECK"
				   	+" WHERE C_BANKACCOUNTDOC_ID = "+idchequera+" AND XX_VCN_CHECK_ID >= "+numero+" AND XX_VCN_LOCATIONOFCHECKS ='1'";
					PreparedStatement prst2 = DB.prepareStatement(sql2,null);
				   	ResultSet rs2 = null;
				   	//ejecuto la sentencia SQL
				   	try {
				   		rs2 = prst2.executeQuery();
				   	while (rs2.next()){
				   		//ACTUALIZO LOS CHEQUES

				   				//Hora y fecha en TimeStamp
								  java.util.Date utilDate = new java.util.Date(); //fecha actual
								  long lnMilisegundos = utilDate.getTime();
								  java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
								  
				   		
								//ID DEL CHEQUE A DESACTIVAR
								int cheque = rs2.getInt("XX_VCN_CHECK_ID");
						   		//CREO EL OBJETO
								MVCNCheck desactivarcheque = new MVCNCheck(Env.getCtx(),cheque,get_Trx());
						   		
						   		desactivarcheque.setXX_VCN_CausesofCancellation(causa);
						   		desactivarcheque.setIsActive(false);
						   		desactivarcheque.setXX_VCN_CancellationDate(sqlTimestamp);
						   		desactivarcheque.setXX_VCN_LocationofChecks("6");
						   		desactivarcheque.save();
						   		get_Trx().commit();
					}//WHILE
				   	}//TRY
				   	catch (Exception e){ 
					System.out.println(e); }
					finally {
					   	//CERRAR CONEXION
						DB.closeResultSet(rs2);
						DB.closeStatement(prst2);
					}
				   	
				   	//DESACTIVO LA CHEQUERA
				   	aux.setIsActive(false);
				   	aux.setXX_VCN_Stateofchecksbook("4");
				   	aux.save();
				   	
				   	Mensaje =  "Se desactivo la chequera";
		   			ADialog.info(1, new Container(), Mensaje);
		   			return Mensaje;
					
					
				}
				//SI LA CHEQUERA NO ESTA EN USO
				else{
			   		Mensaje =  "Esta Chequera no tiene Status en uso";
		   			ADialog.info(1, new Container(), Mensaje);
		   			return Mensaje;
				}
		
	}
	
	
}
