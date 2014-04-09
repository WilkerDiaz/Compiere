package compiere.model.bank.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.compiere.model.MFactAcct;
import org.compiere.model.MInvoice;
import org.compiere.model.MPaySelection;
import org.compiere.model.MPaySelectionCheck;
import org.compiere.model.MPaySelectionLine;
import org.compiere.model.X_C_Payment;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.bank.X_XX_VCN_BankTransfer;
import compiere.model.bank.X_XX_VCN_BankTransferDetailP;
import compiere.model.bank.X_XX_VCN_BankTransferDetailS;
import compiere.model.cds.MPayment;
import compiere.model.payments.X_XX_VCN_AccoutingEntry;

public class XX_VCN_TransferReverse extends SvrProcess{

	//VARIABLES GLOBALES
	private int			transfer_ID = 0;
	
	
	@Override
	protected void prepare() {
		// TODO  Auto-generated method stub
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub

		//CREO EL OBJETO TRANSFERENCIA 
		X_XX_VCN_BankTransfer aux = new X_XX_VCN_BankTransfer(getCtx(),getRecord_ID(),get_TrxName());
		//OBTENGO EL ID DE LA TRANSFERENCIA
		transfer_ID= aux.getXX_VCN_BankTransfer_ID();
//--------------------SELECCIONES DE PAGO----------------------------------------------------------		
//BUSCAR LAS SELECCIONES DE PAGO
				String sql= "SELECT XX_VCN_BANKTRANSFERDETAILS_ID, C_PAYSELECTION_ID"
				+" FROM    XX_VCN_BANKTRANSFERDETAILS"
				+" WHERE ISACTIVE= 'Y' AND XX_VCN_BANKTRANSFER_ID = "+transfer_ID;
				
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		while (rs.next()){

				   			X_XX_VCN_BankTransferDetailS detalleS = new X_XX_VCN_BankTransferDetailS(getCtx(),rs.getInt("XX_VCN_BANKTRANSFERDETAILS_ID"),get_TrxName());
				   			detalleS.setXX_VCN_StateTransferDetail("3");
				   			detalleS.save();
				   			
				   			//OBTENGO EL ID DE LAS SELECCIONES DE PAGO
				   			int seleccion= rs.getInt("C_PAYSELECTION_ID");
				   			//REVERSO LAS SELECCIONES
				   			ReversarSeleccion(seleccion);
				   		}// Fin WHILE
				   	} 
				   	catch (Exception e){
						System.out.println(e);
					}
				   	finally {
				   	//CERRAR CONEXION
						DB.closeResultSet(rs);
						DB.closeStatement(prst);
					}
//-------------------------ANTICIPO-----------------------------------------------------------
//BUSCO TODOS LOS ANTICIPOS PARA HACER LA TRANSFERENCIA
					String sql1= 
					"SELECT XX_VCN_BANKTRANSFERDETAILP_ID, C_PAYMENT_ID"
					+" FROM    XX_VCN_BANKTRANSFERDETAILP"
					+" WHERE XX_VCN_BANKTRANSFER_ID = "+transfer_ID;
					
					PreparedStatement prst1 = DB.prepareStatement(sql1,null);
				   	ResultSet rs1 = null;
				   	//EJECUTO LA SENTENCIA SQL
					   	try {
					   		rs1 = prst1.executeQuery();
					   		while (rs1.next()){
					   			//REVERSAR EL ANTICIPO
					   			X_XX_VCN_BankTransferDetailP detalleA = new X_XX_VCN_BankTransferDetailP(getCtx(),rs1.getInt("XX_VCN_BANKTRANSFERDETAILP_ID"),get_TrxName());
					   			detalleA.setXX_VCN_StateTransferDetail("3");
					   			detalleA.save();
					   			
					   			reversarAnticipo(rs1.getInt("C_PAYMENT_ID"));
					   		}// Fin WHILE
					   	} 
					   	catch (Exception e){
							System.out.println(e);
						}
					   	finally {
					   	//CERRAR CONEXION
							DB.closeResultSet(rs1);
							DB.closeStatement(prst1);
						}
				   	
				   	// LA TRANSFERENCIA PASA A ESTADO PENDIENTE Y SIN FECHA DE EJECUCION
				   	aux.setXX_VCN_ExecutionDate(null);
					aux.setXX_VCN_TransferState("1");
					aux.save();

					return null;
	}
	
/*
* METODO QUE SE ENCARGARA DE REVERSAR UN ANTICIPO
*/
private void reversarAnticipo(int Id) {
		// TODO Auto-generated method stub
	
	// TODO Auto-generated method stub
	//CREO EL OBJETO QUE ES EL ANTICIPO
	MPayment pay = new MPayment (getCtx(), Id, get_TrxName());
			
	//ASIGNO LA FORMA EN QUE LA CANCELE (TRANSFERENCIA)
	pay.setTenderType("T");
	
	pay.set_Value ("XX_VCN_BankTransfer_ID", null);
	
	//ASIGNO LA FECHA DE PAGO
	pay.setXX_DateFinalPay(null);
	
	//ASIGNO LA CUENTA A LA QUE SE HIZO LA TRANSFERENCIA
	pay.setAccountNo(null);
	
	//ASIGNAR A QUIEN SE LE HIZO EL PAGO
	pay.setA_Name(null);
	
	//CUANDO EL PROCESSING SEA TRUE EL NO VUELVE A BUSCAR ESTE ANTICIPO
	pay.setProcessing(false);
	
	//GUARDO LOS CAMBIOS
	pay.save();
	
	//GENERO LA CONTABILIDAD
	generarContabilidad(pay);
}

/*
 * METODO QUE SE ENCARGARA DE REVERSAR UNA SELECCION DE PAGO
 */
public void ReversarSeleccion(int Id){
		
		//CREO EL OBJETO SELECCION DE PAGO
		MPaySelection psel = new MPaySelection (getCtx(), Id, get_TrxName());
		
		//LLAMO AL METODO QUE SE VA A ENCARGAR DE BUSCAR EL PAGO DE ESA SELECCION DE PAGO Y REVERSARLO
		//BUSCO SI TIENE PAGOS RELACIONADOS 
				int pagoAsociado=0;
		
				String sql1= 
				"SELECT PSC.C_PAYSELECTIONCHECK_ID, PS.C_PAYSELECTION_ID"
				+" FROM C_PAYSELECTIONLINE PSL, C_PAYSELECTION PS, C_PAYSELECTIONCHECK PSC"
				+" WHERE PS.C_PAYSELECTION_ID = "+Id
				+" AND PS.C_PAYSELECTION_ID = PSL.C_PAYSELECTION_ID"
				+" AND PSL.C_PAYSELECTIONCHECK_ID = PSC.C_PAYSELECTIONCHECK_ID" 
				+" GROUP BY PSC.C_PAYSELECTIONCHECK_ID, PS.C_PAYSELECTION_ID";
										
								PreparedStatement prst1 = DB.prepareStatement(sql1,null);
							   	ResultSet rs1 = null;
							   	//EJECUTO LA SENTENCIA SQL
								   	try {
								   		rs1 = prst1.executeQuery();
								   		if(rs1.next()){
								   			//OBTENGO EL ID DEL PAGO QUE ES EL DE LA TABLA-> C_PAYSELECTIONCHECK
								   			pagoAsociado= rs1.getInt("C_PAYSELECTIONCHECK_ID");
								   			//METODO QUE REVERSO EL PAGO DE ESA SELECCION DE PAGO
								   			ReversarPagoSeleccion(pagoAsociado);
								   		}// Fin if
								   	} 
								   	catch (Exception e){
										System.out.println(e);
									}
								   	finally {
								   	//CERRAR CONEXION
										DB.closeResultSet(rs1);
										DB.closeStatement(prst1);
									}
		
			
			//BUSCO Y REVERSO LAS LINEAS DE PAGO DE ESA SELECCION DE PAGO
			String sql= "SELECT C_PAYSELECTIONLINE_ID"
			+" FROM C_PAYSELECTIONLINE"
			+" WHERE ISACTIVE = 'Y' AND C_PAYSELECTION_ID= "+ psel.getC_PaySelection_ID();
					
					PreparedStatement prst = DB.prepareStatement(sql,null);
				   	ResultSet rs = null;
				   	//EJECUTO LA SENTENCIA SQL
					   	try {
					   		rs = prst.executeQuery();
					   		while (rs.next()){
	
					   			//OBTENGO EL ID DE LAS SELECCIONES DE PAGO
					   			int seleccion= rs.getInt("C_PAYSELECTIONLINE_ID");
					   			//LLAMO AL METODO REVERSAR LINEAS DE PAGO
					   			ReversarLineasSeleccion(seleccion);
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
		
				   //REVERSO LA SELECCION DE PAGO
				   	psel.setProcessing(false);
				   	psel.save();
	}
/*
 * METODO QUE SE ENCARGA DE REVERSAR LAS LINEAS DE SELECCION DE PAGO	
 */
public void ReversarLineasSeleccion(int Id){

		//LOAD LA LINEA DE PAGO
		MPaySelectionLine line = new MPaySelectionLine (getCtx(), Id, get_TrxName());
		line.setProcessed(true);
		line.setPaymentRule("T");
		//DEVUELVO LA FACTURA A STATUS "A"
		ReversarFactura(line.getC_Invoice_ID(),get_TrxName());

		line.save();
	}
/*
 * METODO QUE SE ENCARGA DE RESERSAR EL PAGO DE UNA SELECCION DE PAGO	
 */
public void ReversarPagoSeleccion(int Id){

	//CREO EL OBJETO QUE HACE REFERENCIA AL PAGO DE LA SELECCION DE PAGO
	MPaySelectionCheck cheque = new MPaySelectionCheck (getCtx(), Id, get_TrxName());
	//SETEO SUS VALORES
		cheque.setPaymentRule("T");
		cheque.setCheckNo(null);
		cheque.setProcessed(false);
			//ESE CHEQUE O TRANSFERENCIA TIENE UN PAGO GENERADO
			//BUSCAMOS ESE PAGO Y LO ANULAMOS Y LO DESVINCULAMOS DE LA FORANEA DE C_PAYSELECTIONCHECK
			int pago= cheque.getC_Payment_ID();
				MPayment pay = new MPayment (getCtx(), pago, get_Trx());
				pay.set_Value ("XX_VCN_BankTransfer_ID", null);
				pay.setDocAction(X_C_Payment.DOCACTION_Void);
				DocumentEngine.processIt(pay, X_C_Payment.DOCACTION_Void);
				pay.save();
				
				
				//GENERO LA CONTABILIDAD
				generarContabilidad(pay);
				
		//QUITAMOS LA FORANEA EN C_PAYSELECTIONCHECK
		cheque.setC_Payment_ID(0);
		cheque.save();
}

/*
 * METODO QUE SE ENCARGA DE REVERSAR LAS FACTURAS	
 */
	public void ReversarFactura(int Id,Trx trans){

		MInvoice factura = new MInvoice (Env.getCtx(), Id, trans);
		factura.set_Value ("XX_AccountPayableStatus", "A");
		factura.save();
	}

	/*
	 * METODO QUE SE ENCARGARA DE GENERAR LA CONTABILIDAD DE CUANDO SE DESACTIVA UN CHEQUE
	 */
	public void generarContabilidad(MPayment pay){
		//APLICO LA CONTABILIDAD
		//CREO LA CABECERA
		
		//FECHA ACTUAL
		java.util.Date utilDate = new java.util.Date(); 
		long lnMilisegundos = utilDate.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
		
		//CABECERA
		X_XX_VCN_AccoutingEntry cabecera = new X_XX_VCN_AccoutingEntry(Env.getCtx(), 0, get_TrxName());
		cabecera.setDateFrom(sqlTimestamp);
		cabecera.setDateTo(sqlTimestamp);
		cabecera.setDateTrx(sqlTimestamp);
		cabecera.setDescription("COMPROBANTE DIARIO");
		cabecera.setXX_TotalHave(pay.getPayAmt());
		cabecera.setXX_TotalShould(pay.getPayAmt());
		cabecera.setXX_ListCX017("BA");
		cabecera.setIsTransferred(false);
		String numberControl = String.valueOf(5) + "0" + 0
		+ String.valueOf(numberControl());
		cabecera.setXX_ControlNumber(numberControl);
		cabecera.save();
		
		crearDebeYHaber(pay,cabecera);
		
		
	}

	/*
	 * 
	 */
	private void crearDebeYHaber(MPayment pay, X_XX_VCN_AccoutingEntry cabecera) {
		// TODO Auto-generated method stub
		
		//CREO EL DEBE
		
		String sql=
		"SELECT *"
		+" FROM FACT_ACCT"
		+" WHERE AMTACCTCR=0 AND RECORD_ID= "+pay.getC_Payment_ID();
		int line=1;
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		while (rs.next()){
		   			MFactAcct debe = new MFactAcct(Env.getCtx(), 0, get_TrxName());
					debe.setAD_Table_ID(318);
					debe.setAccount_ID(rs.getInt("Account_ID"));
					java.math.BigDecimal cero = new java.math.BigDecimal("0");
					debe.setAmtAcctCr(rs.getBigDecimal("AmtAcctDr"));
					debe.setAmtAcctDr(cero);
					debe.setAmtSourceCr(rs.getBigDecimal("AmtSourceDr"));
					debe.setAmtSourceDr(cero);
					debe.setC_AcctSchema_ID(1000009);
					debe.setC_BPartner_ID(pay.getC_BPartner_ID());
					debe.setC_Currency_ID(205);
					debe.set_ValueNoCheck ("XX_VCN_Line", line);
					line++;
					debe.set_ValueNoCheck ("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
					debe.setC_Period_ID(buscarPeriodoContable());
					java.util.Date utilDate = new java.util.Date(); //FECHA ACTUAL
					long lnMilisegundos = utilDate.getTime();
					java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
					debe.setDateAcct(sqlTimestamp);
					debe.setDateTrx(sqlTimestamp);
					debe.setPostingType("A");
					debe.setRecord_ID(pay.getC_Payment_ID());
					debe.setDescription(rs.getString("Description"));
					//CREO EL DETALLE DEL DEBE
					debe.set_Value ("XX_Aux", rs.getString("XX_Aux"));
					debe.set_Value ("XX_Departament", rs.getString("XX_Departament"));
					debe.set_Value ("XX_Division", rs.getString("XX_Division"));
				if(rs.getDate("XX_DocumentDate") != null){
						java.sql.Timestamp sqlDate = new java.sql.Timestamp(rs.getDate("XX_DocumentDate").getTime());
						debe.set_Value ("XX_DocumentDate", sqlDate);
				}
					debe.set_Value ("XX_DocumentType", rs.getString("XX_DocumentType"));
				if(rs.getDate("XX_DueDate") != null){	
					java.sql.Timestamp sqlDate1 = new java.sql.Timestamp(rs.getDate("XX_DueDate").getTime());
					debe.set_Value ("XX_DueDate", sqlDate1);
				}
					debe.set_Value ("DocumentNo", rs.getString("DocumentNo"));
					debe.set_Value ("XX_Office", rs.getString("XX_Office"));
					debe.set_Value ("XX_SectionCode", rs.getString("XX_SectionCode"));
					debe.save();
					get_TrxName().commit();
		   			
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
		   	
		   	//CREO EL HABER
			String sql1=
					"SELECT *"
					+" FROM FACT_ACCT"
					+" WHERE AMTACCTCR != 0 AND RECORD_ID= "+pay.getC_Payment_ID() +" and XX_VCN_ACCOUTINGENTRY_ID != "+ cabecera.getXX_VCN_AccoutingEntry_ID();
					
					PreparedStatement prst1 = DB.prepareStatement(sql1,null);
				   	ResultSet rs1 = null;
				   	//EJECUTO LA SENTENCIA SQL
					   	try {
					   		rs1 = prst1.executeQuery();
					   		while (rs1.next()){
					   			MFactAcct haber = new MFactAcct(Env.getCtx(), 0, get_TrxName());
								haber.setAD_Table_ID(318);
								haber.setAccount_ID(rs1.getInt("Account_ID"));
								java.math.BigDecimal cero = new java.math.BigDecimal("0");
								haber.setAmtAcctCr(cero);
								haber.setAmtAcctDr(rs1.getBigDecimal("AmtAcctCr"));
								haber.setAmtSourceCr(cero);
								haber.setAmtSourceDr(rs1.getBigDecimal("AmtSourceCr"));
								haber.setC_AcctSchema_ID(1000009);
								haber.setC_BPartner_ID(pay.getC_BPartner_ID());
								haber.setC_Currency_ID(205);
								haber.set_ValueNoCheck ("XX_VCN_Line", line);
								line++;
								haber.set_ValueNoCheck ("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
								haber.setC_Period_ID(buscarPeriodoContable());
								java.util.Date utilDate = new java.util.Date(); //FECHA ACTUAL
								long lnMilisegundos = utilDate.getTime();
								java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
								haber.setDateAcct(sqlTimestamp);
								haber.setDateTrx(sqlTimestamp);
								haber.setPostingType("A");
								haber.setRecord_ID(pay.getC_Payment_ID());
								haber.setDescription(rs1.getString("Description"));
								//CREO EL DETALLE DEL HABER
								haber.set_Value ("XX_Aux", rs1.getString("XX_Aux"));
								haber.set_Value ("XX_Departament", rs1.getString("XX_Departament"));
								haber.set_Value ("XX_Division", rs1.getString("XX_Division"));
							if(rs1.getDate("XX_DocumentDate") != null){
								java.sql.Timestamp sqlDate = new java.sql.Timestamp(rs1.getDate("XX_DocumentDate").getTime());
								haber.set_Value ("XX_DocumentDate", sqlDate);
							}
								haber.set_Value ("XX_DocumentType", rs1.getString("XX_DocumentType"));
							if(rs1.getDate("XX_DueDate") != null){
								java.sql.Timestamp sqlDate1 = new java.sql.Timestamp(rs1.getDate("XX_DueDate").getTime());
								haber.set_Value ("XX_DueDate", sqlDate1);
							}
								haber.set_Value ("DocumentNo", rs1.getString("DocumentNo"));
								haber.set_Value ("XX_Office", rs1.getString("XX_Office"));
								haber.set_Value ("XX_SectionCode", rs1.getString("XX_SectionCode"));
								
								haber.save();
					   			
					   		}// Fin if
					   	} 
					   	catch (Exception e){
							System.out.println(e);
						}
					   	finally {
					   	//CERRAR CONEXION
							DB.closeResultSet(rs1);
							DB.closeStatement(prst1);
						}
	}
	
	/*
	 * METODO QUE SE ENCARGA DE BUSCAR EL PERIODO ACTUAL
	 */
	private int buscarPeriodoContable() {
		// TODO Auto-generated method stub
		int id=0;
		Calendar c1 = Calendar.getInstance();
		int mes = c1.get(Calendar.MONTH);
		String mesletra="";
		int year = c1.get(Calendar.YEAR);
		
		if (mes == 1) mesletra="Enero"; else if (mes == 2) mesletra="Febrero"; else if (mes == 3) mesletra="Marzo";
		else if (mes == 4) mesletra="Abril";else if (mes == 5) mesletra="Mayo";else if (mes == 6) mesletra="Junio";
		else if (mes == 7) mesletra="Julio";else if (mes == 8) mesletra="Agosto";else if (mes == 9) mesletra="Septiembre";
		else if (mes == 10) mesletra="Octubre";else if (mes == 11) mesletra="Noviembre";else if (mes == 12) mesletra="Diciembre";
		
		mesletra=mesletra+" "+year;
		String mesletraMayuscula= mesletra.toUpperCase();
		
		String sql=
		"select c_period_id"
		+" from c_period"
		+" where name = '"+mesletra+"' OR name= '"+mesletraMayuscula+"'";
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			id = rs.getInt(1);
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
		
		return id;
	}
	/*
	 * GENERA EL NUMERO DE CONTROL DE LA CABECERA
	 */
	public int numberControl() {
		int number = 0;
		String sql_comprobante = "select XX_NUMCOMPROBANTECX017.NEXTVAL from dual";
		PreparedStatement ps_comprobante = null;
		ResultSet rs_comprobante = null;
		try {
			ps_comprobante = DB.prepareStatement(sql_comprobante, null);
			rs_comprobante = ps_comprobante.executeQuery();
			rs_comprobante.next();
			number = rs_comprobante.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs_comprobante.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps_comprobante.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return number;
	}
	
	
}
