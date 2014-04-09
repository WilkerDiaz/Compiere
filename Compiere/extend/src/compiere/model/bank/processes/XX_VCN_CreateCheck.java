package compiere.model.bank.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.bank.MBankAccountDoc;
import compiere.model.bank.X_XX_VCN_Check;

public class XX_VCN_CreateCheck extends SvrProcess{
	
	//VARIABLE GLOBAL
	MBankAccountDoc aux = null;

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		aux = new MBankAccountDoc (getCtx(),getRecord_ID(),get_TrxName());		
		
		int idchequera = aux.getC_BankAccountDoc_ID();
		
		int total=0;
		
		String sql= "SELECT COUNT(*) AS CUENTA"
					+" FROM XX_VCN_CHECK"
					+" WHERE C_BANKACCOUNTDOC_ID= "+idchequera;
		
					//BUSCO EL NUMERO DE CHEQUES DE LA CHEQUERA
					PreparedStatement prst = DB.prepareStatement(sql,null);
				   	ResultSet rs = null;
				   	//ejecuto la sentencia SQL
					   	try {
					   		rs = prst.executeQuery();
					   		if (rs.next()){
					   			total= rs.getInt("CUENTA");
					   			//TOTAL TIENE EL NUMERO DE CHEQUES ASOCIADOS A ESA CHEQUERA
					   			
					   		}// Fin if
					   	} 
					   	catch (Exception e){
							System.out.println(e);
						}
					   	finally {
					   	//CERRAR CONEXION
							DB.closeResultSet(rs);
							DB.closeStatement(prst);
						}

					   		//Si existen cheques a correspondientes a esa chequera
					   		//ESTO ES POR SI QUIERE MODIFICAR NO CREARA MAS CHEQUES
					   	
						   	if(total>0){
						   		ADialog.info(1, new Container(),  Msg.getMsg(Env.getCtx(), "XX_VCN_CheckbookHasChecks"));
					   			return Msg.getMsg(Env.getCtx(), "XX_VCN_CheckbookHasChecks");
						   	}
						   	else{
						   	//Si no tengo cheques creados
									
								int numCheck = (Integer) aux.getXX_VCN_CheckNumber();
								
								//Inicio de los cheques
								int Inicio = (Integer) aux.get_ValueAsInt("CurrentNext");
								int Primero=0;
								
								//while que guarda los cheques
								while( Primero <  numCheck )
								{
									CrearCheque(Inicio,Primero,aux.get_ValueAsInt("C_BankAccountDoc_ID"));
									Primero=Primero+1;
								}

								ADialog.info(1, new Container(),  Msg.getMsg(Env.getCtx(), "XX_VCN_CreateChecks"));
					   			return Msg.getMsg(Env.getCtx(), "XX_VCN_CreateChecks");
						   		
							}//fin del else

	}

	/*
	 PROCEDIMIENTO PARA CREAR CHEQUES
	 */
		public void CrearCheque(int Inicio, int Primero, int chequera){
			
			X_XX_VCN_Check cheque = new X_XX_VCN_Check(getCtx(), 0, get_Trx());
			
			//Seteo los valores del cheque
			cheque.setAD_Client_ID(aux.getAD_Client_ID());
			cheque.setAD_Org_ID(aux.getAD_Org_ID());
			cheque.setC_BankAccountDoc_ID(chequera);
			cheque.setIsActive(true);
			cheque.setXX_VCN_CancellationDate(null);
			cheque.setXX_VCN_CausesofCancellation(null);
			cheque.setXX_VCN_CheckNumber(Inicio+Primero);
			
				  //Hora y fecha en TimeStamp
				  java.util.Date utilDate = new java.util.Date(); //fecha actual
				  long lnMilisegundos = utilDate.getTime();
				  java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
			
			cheque.setXX_VCN_DateofIssuance(sqlTimestamp);
			cheque.setXX_VCN_LocationofChecks("1");
			cheque.setXX_VCN_VoidCheck(null);
			cheque.save();
		}
}
