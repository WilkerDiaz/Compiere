package compiere.model.bank;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MVCNCheck extends compiere.model.bank.X_XX_VCN_Check{

	//VARIABLES GLOBALES
	int cuentaban= 0;
	int cheques = 0;
	
	
	/** Standard Constructor
    @param ctx context
    @param XX_VCN_Check_ID id
    @param trx transaction
    */
    public MVCNCheck (Ctx ctx, int XX_VCN_Check_ID, Trx trx)
    {
        super (ctx, XX_VCN_Check_ID, trx);
    }
    
	protected boolean beforeSave (boolean newRecord)
	{	
						//OBTENGO EL ID DE LA CHEQUERA
						int checkbook= get_ValueAsInt("C_BankAccountDoc_ID");
						//LLAMO AL METODO QUE VA A COLOCAR EN LA VARIABLE CHEQUE(EL NUMERO TOTAL DE CHEQUES) Y CUENTA BANCARIA LOS VALORES CORRESPONDIENTES
						obtenerChequeCuenta(checkbook);					
												
						//BUSCO LOS CHEQUES USADOS 
								String sql2=
								"SELECT COUNT(*) AS USADO"
								+" FROM XX_VCN_CHECK"
								+" WHERE C_BANKACCOUNTDOC_ID = "+checkbook+" AND XX_VCN_LOCATIONOFCHECKS != '1'"; 
										PreparedStatement prst2 = DB.prepareStatement(sql2,null);
									   	ResultSet rs2 = null;
									   	int usado= 0;
								
									   	//ejecuto la sentencia SQL
									   	try {
									   		rs2 = prst2.executeQuery();
											   		if (rs2.next()){
											   			usado = (Integer) rs2.getInt("USADO");
											   		}// Fin if
									   	} 
									   	catch (Exception e){ System.out.println(e); }
									   	finally {
										   	//CERRAR CONEXION
												DB.closeResultSet(rs2);
												DB.closeStatement(prst2);
										}

									   	//Si se va a guardar el ultimo cheque actualizo el estado de la chequera a finalizada
									   	if(usado+1 == cheques){
									   		//CHEQUERA EN ESTADO EN USO ACTUAL PASA A FINALIZADA
									   		MBankAccountDoc chequera = new MBankAccountDoc(getCtx(), checkbook, get_Trx());
									   		chequera.setXX_VCN_Stateofchecksbook("2");
									   		chequera.save();

									   		//BUSCO SI HAY OTRA CHEQUERA PARA PONER EN STATUS EN USO
													   	String sql4=
													   	"SELECT C.C_BANKACCOUNTDOC_ID AS IDCH"
													   	+" FROM C_BANKACCOUNTDOC C"
													   	+" WHERE C.C_BANKACCOUNT_ID = "+cuentaban+" AND C.XX_VCN_STATEOFCHECKSBOOK='3'"
													   	+" ORDER BY (C.C_BANKACCOUNT_ID)";
													   	PreparedStatement prst4 = DB.prepareStatement(sql4,null);
													   	ResultSet rs4 = null;
													   	
													   	int chequerauso= 0;
													   	//ejecuto la sentencia SQL
													   	try {
													   		rs4 = prst4.executeQuery();
															   		if (rs4.next()){
															   			//CHEQUERA EN STATUS ACTIVA PASA A STATUS EN USO
															   			chequerauso = (Integer) rs4.getInt("IDCH");
															   			MBankAccountDoc chequerau = new MBankAccountDoc(getCtx(), chequerauso, get_Trx());
															   			chequerau.setXX_VCN_Stateofchecksbook("1");
															   			chequerau.save();
															   			get_Trx().commit();
															   		}// Fin if
													   	} 
													   	catch (Exception e){ System.out.println(e); }
													   	finally {
														   	//CERRAR CONEXION
																DB.closeResultSet(rs4);
																DB.closeStatement(prst4);
														}
									   	}//FIN DEL IF DE if(usado+1 == cheques)
		return true;
	}
	
	/*
	 * METODO QUE SE ENCARGA DE DEVOLVER LA CUENTA BANCARIA Y EL NUMERO DE CHEQUES
	 */
	private void obtenerChequeCuenta (Integer checkbook){
		
		//BUSCO LA CANTIDAD DE CHEQUES DE LA CHEQUERA Y LA CUENTA BANCARIA 
		String sql=
		"SELECT XX_VCN_CHECKNUMBER AS NUMBERS, C_BankAccount_ID AS CUENTAB"
		+" FROM C_BANKACCOUNTDOC"
		+" WHERE C_BANKACCOUNTDOC_ID = "+checkbook; 
			
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
		
			   	//ejecuto la sentencia SQL
			   	try {
			   		rs = prst.executeQuery();
					   		if (rs.next()){
					   			cheques = (Integer) rs.getInt("NUMBERS");
					   			cuentaban=(Integer) rs.getInt("CUENTAB");
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
		
	} 

}
