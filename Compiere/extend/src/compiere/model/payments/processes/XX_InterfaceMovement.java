package compiere.model.payments.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.compiere.model.MClient;
import org.compiere.model.MPaySelection;
import org.compiere.model.MPaySelectionCheck;
import org.compiere.model.MPaymentBatch;
import org.compiere.model.X_C_PaySelection;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.cds.As400DbManager;
import compiere.model.cds.MAllocationLine;
import compiere.model.cds.MPayment;


/**
 * Proceso que se encarga de generar la interfaz para obtener el 
 * registro del movimiento creado por el Sistema de Banco (Bx006)
 * Nombre del proceso en compiere: Interface Regist Movement
 * @author Jessica Mendoza
 *
 */
public class XX_InterfaceMovement  extends SvrProcess{
	
	@Override
	protected String doIt() throws Exception {		
		
		String respuesta = "";

		try{
			
			String tipMov = "";
			int idOrderPay = 0;
			int docPay = 0;
			String bank = "";
			String dateAux = null;
			String tenderType= null;
			String montoT = "";

			As400DbManager As = new As400DbManager();
			
			ResultSet rsAS = null;
			PreparedStatement statementAs = null;
			
			rsAS=getASValues(statementAs, As);
			
			while(rsAS.next()){
				 
				idOrderPay = rsAS.getInt("NROODP"); //Número de Orden de Pago
				docPay = rsAS.getInt("NROREF"); //Número de cheque o transferencia
				dateAux = rsAS.getString("FECVAL"); //Fecha cheque o transferencia
				bank = rsAS.getString("CTABAN"); //Código de cuenta donde fue emitido el cheque o la transferencia
				montoT = rsAS.getString("MONTOT"); //Monto del cheque o la transferencia
				tipMov = "S";
				
				
				//IF valida si es un anticipo que queremos actualizar				
				if(esAnticipo(idOrderPay,montoT) != 0){
					
						//Actualizo el Banco
						Integer bankAcc = getBankAcc(bank);
							if(bankAcc!=0)
							{
								MPayment pay = new MPayment( Env.getCtx(), esAnticipo(idOrderPay,montoT), null);
								pay.setC_BankAccount_ID( bankAcc);
								pay.setCheckNo(Integer.toString(docPay));
								if(!pay.save())
									continue;
							}
							else{
								System.out.println("No existe la cuenta bancaria de la OP: " + idOrderPay);
								continue;
							}
						
						//No se usa
						tipMov = rsAS.getString("TIPMOV"); //Código de tipo de movimiento 
						
							if(tipMov.equals("CH ")){
								tenderType = "K";
								tipMov = "S";
							}else{
								tenderType = "T"; 
								tipMov = "T";
							}
						
						//Actualiza el campo anticipo (colocandolo en N) y sincroniza el pago con banco
						/*OJO: Faltaría actulizar algunos otros datos que requiera el usuario, pero se debe hacer juston 
						después que se tenga la información exacta de la interfaz*/
						String update = "update C_Payment " +
										"set XX_SynchronizationBank = 'Y', XX_DATEFINALPAY =  TO_DATE('"+ dateAux +"','dd/MM/yyyy'), "+
										"TENDERTYPE = '" + tenderType + "' "+
										"where C_Payment_ID = "+esAnticipo(idOrderPay,montoT);
						DB.executeUpdate(get_Trx(), update);
	
						//Estado en blanco para la tabla del AS Indicando
						updatePayment(idOrderPay);
						commit();

						
						//Paso a la siguiente iteracion 
						continue;
				}

				//Verificamos si ya generó pago
				if(idPayment(idOrderPay)!=0){
					System.out.println("Ya se generó el pago de la OP: " + idOrderPay);
					
					//Actualizo la cuenta bancaria del proveedor. Dato necesario para el correo del pago. MVALERO
					String update = "update C_Payment " +
					"set C_BP_BankAccount_ID =  (select ba.C_BP_BankAccount_ID from C_BP_BankAccount ba where ba.C_BPartner_ID = C_Payment.C_BPartner_ID and XX_IsPrimary = 'Y')" +
					"where C_Payment_ID = " + idPayment(idOrderPay);
					DB.executeUpdate(get_Trx(), update);
					commit();
					
					//Si el movimiento es transferencia envío el correo. MVALERO
					if(!rsAS.getString("TIPMOV").equalsIgnoreCase("CH ")){
						XX_SendMailPayDoc sm = new XX_SendMailPayDoc();
						sm.payS = new X_C_PaySelection(getCtx(), idOrderPay, get_Trx());
						sm.doIt();
					}
					
					
					updatePayment(idOrderPay);
					continue;
				}
				
				Integer bankAcc = getBankAcc(bank);
				
				if(bankAcc!=0)
				{
					MPaySelection paySel = new MPaySelection( Env.getCtx(), idOrderPay, null);
					paySel.setC_BankAccount_ID( bankAcc);
					if(!paySel.save())
						continue;
				}
				else{
					System.out.println("No existe la cuenta bancaria de la OP: " + idOrderPay);
					continue;
				}
				
				//Buscar el pago generado y el tipo de pago, asociado a una orden de pago específica y número de cheque
				MPaySelectionCheck[] m_checks = MPaySelectionCheck.get(idOrderPay, tipMov, docPay, null);
				MPaymentBatch m_batch = MPaymentBatch.getForPaySelection (Env.getCtx(), idOrderPay, null);		 
				
				//Si no tiene PaySelectionCheck
				if(m_checks.length==0){
					System.out.println("No tiene registro asociado en la tabla C_PaySelectionCheck: " + idOrderPay);
					continue;
				}
				
				//Genera el pago de la orden y tipo de pago encontrados anteriormente
				if(MPaySelectionCheck.confirmPrint (m_checks, m_batch)==0){
					System.out.println("No se generó pudo general el pago correspondiente: " + idOrderPay);
					continue;
				}
				
				//No se usa
				tipMov = rsAS.getString("TIPMOV"); //Código de tipo de movimiento 
				
				if(tipMov.equals("CH ")){
					tenderType = "K";
					tipMov = "S";
				}else{
					tenderType = "T"; 
					tipMov = "T";
				}
				
				//Actualiza el campo anticipo (colocandolo en N) y sincroniza el pago con banco
				/*OJO: Faltaría actulizar algunos otros datos que requiera el usuario, pero se debe hacer juston 
				después que se tenga la información exacta de la interfaz*/
				String update = "update C_Payment " +
								"set XX_IsAdvance = 'N', XX_SynchronizationBank = 'Y', XX_DATEFINALPAY =  TO_DATE('"+ dateAux +"','dd/MM/yyyy'), "+
								"TENDERTYPE = '" + tenderType + "' "+
								"where C_Payment_ID = (select max(C_Payment_ID) from C_Payment) ";
				DB.executeUpdate(get_Trx(), update);
				
				//Verificar si el pago tiene asignacion, de lo contrario crear la asignacion
				String sql = "select ali.C_AllocationLine_ID, inv.XX_Contract_ID " +
							 "from C_AllocationLine ali " +
							 "inner join C_Invoice inv on (ali.C_Invoice_ID = inv.C_Invoice_ID) " +
							 "where ali.C_Payment_ID = (select max(C_Payment_ID) from C_Payment) ";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {	
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();			
					while (rs.next()){
						if (rs.getInt(2) != 0){
							MAllocationLine allocationLine = new MAllocationLine(Env.getCtx(), rs.getInt(1), get_Trx());
							allocationLine.setXX_Contract_ID(rs.getInt(2));
							allocationLine.save();
						}
					}	
				}

				catch(SQLException e){	
					e.getMessage();			
				} finally{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
				
				//Actualiza el estado de las cuentas por pagar de las facturas asociadas a la orden de pago
				update = "update C_Invoice " +
						 "set XX_AccountPayableStatus = 'P', " +
						 "XX_SynchronizationBank = 'Y' " +
						 "where C_Invoice_ID in " +
						 	"(select C_Invoice_ID " +
						 	"from C_PaySelectionLine " +
						 	"where C_PaySelection_ID = " + idOrderPay + ")";
				
				DB.executeUpdate(get_Trx(), update);
				
				//Estado en blanco para la tabla del AS Indicando
				updatePayment(idOrderPay);
				commit();
			}
			
			rsAS.close();
			As.desconectar();

		}catch (Exception e){
			e.printStackTrace();
			respuesta = "ERROR en la importación de la interfaz.";
			System.out.println(e + respuesta);
		}

		return respuesta;
	}

	@Override
	protected void prepare() {
	
	}
		
	private void updatePayment(int idPayment){
    	
    	As400DbManager As = new As400DbManager();
		String SQL;
        
        //Conexión con el AS/400
        As.conectar();
        	
        try
        {    
        	//Actualiza el estado de la tabla puente en el AS/400 para la transferencia.
        	SQL = "UPDATE interfaces.ban013 SET ESTADO = 'OK' WHERE NROODP = " + idPayment;
        	Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            As.realizarSentencia(SQL, sentencia);
        	sentencia.close();
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		}
	
        As.desconectar();    	
	}
	
	/*
	 * Sentencia AS
	 */
	private ResultSet getASValues(Statement sentencia, As400DbManager As)
	{
		String sql;
        ResultSet r = null;
        
        MClient client = new MClient( Env.getCtx(), getAD_Client_ID(), null);        
        
        As.conectar();
        	 	
        try{

            sql = "SELECT  * " +
            	  "FROM interfaces.ban013 where ESTADO = ' ' AND CODCIA = " + client.get_ValueAsInt("XX_Value");

            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarConsulta(sql, sentencia);

		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return r;
	}
	
	/**
	 * Se encarga de buscar el identificador de la linea de la orden de pago
	 * @param idOrderPay id de la orden de pago
	 * @return
	 */
	public int idSelectionLine(int idOrderPay){
		int id = 0;
		String sql = "select C_PaySelectionLine_ID " +
				  	 "from C_PaySelectionLine " +
				  	 "where C_PaySelection_ID = " + idOrderPay;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, get_Trx()); 
			rs = pstmt.executeQuery();
			if(rs.next()){		
				id = rs.getInt(1);
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
			rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	
	/**
	 * Se encarga de buscar el identificador del pago
	 * @param idOrderPay id de la orden de pago
	 * @return
	 */
	public int idPayment(int idOrderPay){
		int id = 0;
		String sql = "select pay.C_Payment_ID " +
					 "from C_PaySelection pas " +
					 "inner join C_PaySelectionCheck psc on (psc.C_PaySelection_ID = pas.C_PaySelection_ID) " +
					 "inner join C_Payment pay on (psc.C_Payment_ID = pay.C_Payment_ID) " +
					 "where pas.C_PaySelection_ID = " + idOrderPay;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, get_Trx()); 
			rs = pstmt.executeQuery();
			if(rs.next()){		
				id = rs.getInt(1);
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
			rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	
	private Integer getBankAcc(String acc){
		
		Integer bAcc = 0;
		String sql = "select C_BANKACCOUNT_ID FROM C_BANKACCOUNT WHERE ACCOUNTNO = '" + acc + "'";
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			
			if(rs.next())
				bAcc = rs.getInt(1);
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return bAcc;
	}
	/*
	 * Metodo que valida si es el Id pertenece a un anticipo
	 */
	private int esAnticipo(int id, String montoT){
		
		montoT = montoT.substring(1, montoT.length());
		int idAnticipo=0;
		String sql = "SELECT C_PAYMENT_ID FROM C_PAYMENT WHERE XX_ISADVANCE='Y' AND DOCUMENTNO= '"+id+"' AND PAYAMT = "+montoT;
		
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			
			if(rs.next())
				idAnticipo = rs.getInt(1);;
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return idAnticipo;
	}
}
