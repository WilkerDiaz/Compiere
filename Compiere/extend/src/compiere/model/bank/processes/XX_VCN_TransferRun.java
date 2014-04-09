package compiere.model.bank.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.apps.ProcessCtl;
import org.compiere.model.MFactAcct;
import org.compiere.model.MInvoice;
import org.compiere.model.MPInstance;
import org.compiere.model.MPaySelection;
import org.compiere.model.MPaySelectionCheck;
import org.compiere.model.MPaySelectionLine;
import org.compiere.model.X_C_Payment;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.bank.XX_VCN_DetalleContable;
import compiere.model.bank.X_XX_VCN_BankTransfer;
import compiere.model.bank.X_XX_VCN_BankTransferDetailP;
import compiere.model.bank.X_XX_VCN_BankTransferDetailS;
import compiere.model.cds.MPayment;
import compiere.model.payments.X_XX_VCN_AccoutingEntry;

public class XX_VCN_TransferRun extends SvrProcess {

	private int transfer_ID = 0;
	private int bankAccount_ID = 0;
	private java.math.BigDecimal montoTotal;
	private int clienteID = 0;
	private int orgID = 0;
	private Trx trans = Trx.get("XX_VCN_CONTABILIDAD");

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub

		X_XX_VCN_BankTransfer aux = new X_XX_VCN_BankTransfer(getCtx(),
				getRecord_ID(), get_TrxName());
		// OBTENGO EL ID DE LA TRANSFERENCIA
		transfer_ID = aux.getXX_VCN_BankTransfer_ID();
		// OBTENGO EL BANCO DEL CUAL VOY A HACER LA TRANSFERENCIA
		bankAccount_ID = aux.getC_BankAccount_ID();
		// OBTENGO EL CLIENTE ID
		clienteID = aux.getAD_Client_ID();
		// OBTENGO LA ORGANIZACION
		orgID = aux.getAD_Org_ID();

		// ASIGNO LA FECHA DE EJECUCION
		java.util.Date utilDate = new java.util.Date(); // fecha actual
		long lnMilisegundos = utilDate.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
		aux.setXX_VCN_ExecutionDate(sqlTimestamp);

		// LA VENTANA PASA A ESTADO GENERADA
		aux.setXX_VCN_TransferState("2");
		aux.save();

		// -------------------------------------------PAGAR LOS ANTICIPOS-----------------------------------------------
		// BUSCO TODOS LOS ANTICIPOS PARA HACER LA TRANSFERENCIA

		String sql = "SELECT XX_VCN_BANKTRANSFERDETAILP_ID, C_PAYMENT_ID"
				+ " FROM    XX_VCN_BANKTRANSFERDETAILP"
				+ " WHERE XX_VCN_BANKTRANSFER_ID = " + transfer_ID;

		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			while (rs.next()) {
				// PAGO EL ANTICIPO
				X_XX_VCN_BankTransferDetailP detalleA = new X_XX_VCN_BankTransferDetailP(
						getCtx(), rs.getInt("XX_VCN_BANKTRANSFERDETAILP_ID"),
						get_TrxName());
				detalleA.setXX_VCN_StateTransferDetail("1");
				detalleA.save();
				PagarAnticipo(rs.getInt("C_PAYMENT_ID"));

			}// Fin WHILE
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
		// -------------------------------------------PAGAR LAS SELECCIONES-----------------------------------------------
		// BUSCO LAS SELECCIONES PARA HACER LA TRANSFERENCIA
		String sql1 = "SELECT XX_VCN_BANKTRANSFERDETAILS_ID, C_PAYSELECTION_ID"
				+ " FROM XX_VCN_BANKTRANSFERDETAILS"
				+ " WHERE XX_VCN_BANKTRANSFER_ID = " + transfer_ID;

		PreparedStatement prst1 = DB.prepareStatement(sql1, null);
		ResultSet rs1 = null;

		// EJECUTO LA SENTENCIA SQL
		try {
			rs1 = prst1.executeQuery();
			while (rs1.next()) {
				// PAGO LA SELECCION DE PAGO
				X_XX_VCN_BankTransferDetailS detalleS = new X_XX_VCN_BankTransferDetailS(
						getCtx(), rs1.getInt("XX_VCN_BANKTRANSFERDETAILS_ID"),
						get_TrxName());
				detalleS.setXX_VCN_StateTransferDetail("1");
				detalleS.save();

				PagarSeleccion(rs1.getInt("C_PAYSELECTION_ID"));
			}// Fin while
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs1);
			DB.closeStatement(prst1);
		}

		// HAGO COMMIT
		trans.commit();

		// LLAMO AL PROCESO QUE GENERA EL TXT
		MPInstance mpi = new MPInstance(Env.getCtx(), Env.getCtx()
				.getContextAsInt("#XX_VCN_GENERATETXTPROC"), getRecord_ID());
		mpi.save();

		ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt(
				"#XX_VCN_GENERATETXTPROC"));
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(Env.getCtx().getContextAsInt(
				"#XX_VCN_GENERATETXTPROC"));
		pi.setClassName("");
		pi.setTitle("");

		ProcessCtl pc = new ProcessCtl(null, pi, null);
		pc.start();

		return null;
	}

	/*
	 * METODO QUE SE ENCARGA DE PAGAR LOS ANTICIPOS DE PAGO
	 */
	private void PagarAnticipo(int Id) throws Exception {

		// TODO Auto-generated method stub
		// CREO EL OBJETO QUE ES EL ANTICIPO
		MPayment pay = new MPayment(getCtx(), Id, get_TrxName());

		// ASIGNO LA FORMA EN QUE LA CANCELE (TRANSFERENCIA)
		pay.setTenderType("T");

		// ASIGNO LA FECHA DE PAGO
		java.util.Date utilDate = new java.util.Date(); // fecha actual
		long lnMilisegundos = utilDate.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
		pay.setXX_DateFinalPay(sqlTimestamp);

		// ASIGNO LA CUENTA BANCARIA DE LA CUAL HICE LA TRANSFERENCIA
		pay.setC_BankAccount_ID(bankAccount_ID);

		// ASIGNO LA CUENTA A LA QUE SE HIZO LA TRANSFERENCIA
		String cuenta = buscarCuenta(Id);
		pay.setAccountNo(cuenta);

		// ASIGNAR A QUIEN SE LE HIZO EL PAGO
		pay.setA_Name(buscarRelacion(Id));

		// SI EL ANTICIPO ESTA EN ESTADO BORRADOR LO COMPLETO
		if (pay.getDocStatus().compareTo("CO") != 0) {
			// COMPLETO EL ANTICIPO
			pay.setDocAction(X_C_Payment.DOCACTION_Complete);
			DocumentEngine.processIt(pay, X_C_Payment.DOCACTION_Complete);
		}

		// ASIGNO LA TRANSFERENCIA
		pay.set_Value("XX_VCN_BankTransfer_ID", Integer.valueOf(transfer_ID));

		// CUANDO EL PROCESSING SEA TRUE EL NO VUELVE A BUSCAR ESTE ANTICIPO
		pay.setProcessing(true);

		// GUARDO LOS CAMBIOS
		pay.save();

		// APLICO LA CONTABILIDAD
		// CREO LA CABECERA
		X_XX_VCN_AccoutingEntry cabecera = new X_XX_VCN_AccoutingEntry(
				Env.getCtx(), 0, trans);
		cabecera.setDateFrom(sqlTimestamp);
		cabecera.setDateTo(sqlTimestamp);
		cabecera.setDateTrx(sqlTimestamp);
		cabecera.setDescription("COMPROBANTE DIARIO");
		cabecera.setXX_TotalHave(pay.getPayAmt());
		cabecera.setXX_TotalShould(pay.getPayAmt());
		cabecera.setXX_ListCX017("BA");
		// cabecera.setXX_ProcessType("");
		// cabecera.setXX_TypeTransferSP("");
		cabecera.setIsTransferred(false);
		String numberControl = String.valueOf(5) + "0" + 0
				+ String.valueOf(numberControl());
		cabecera.setXX_ControlNumber(numberControl);
		cabecera.save();
		// CREO EL DETALLE DE LA CUENTA CONTABLE *DEBE*
		buscarCuentaAnticipo(pay, cabecera, trans);
		// CREO EL HABER
		MFactAcct haber = new MFactAcct(Env.getCtx(), 0, trans);
		haber.setAD_Table_ID(318);
		haber.setAccount_ID(buscarElementoContableBanco(pay.getC_BankAccount_ID()));
		haber.setAmtAcctCr(pay.getPayAmt());
		java.math.BigDecimal cero = new java.math.BigDecimal("0");
		haber.setAmtAcctDr(cero);
		haber.setAmtSourceCr(pay.getPayAmt());
		haber.setAmtSourceDr(cero);
		haber.setC_AcctSchema_ID(1000009);
		haber.setC_Currency_ID(205);
		haber.setC_BPartner_ID(pay.getC_BPartner_ID());
		haber.set_ValueNoCheck("XX_VCN_Line", buscarCuantasLineas(pay.getC_Payment_ID(), cabecera) + 1);
		haber.set_ValueNoCheck("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
		haber.setC_Period_ID(buscarPeriodoContable());
		haber.setDateAcct(sqlTimestamp);
		haber.setDateTrx(sqlTimestamp);
		haber.setPostingType("A");
		haber.setRecord_ID(pay.getC_Payment_ID());
		haber.setDescription(pay.getA_Name() + " " + buscarNombreElementoContableBanco(pay.getC_BankAccount_ID()));
		
		// CREO EL DETALLE DEL HABER
		XX_VCN_DetalleContable contable = new XX_VCN_DetalleContable();
		ArrayList<String> detalle = new ArrayList<String>();
		detalle = contable.DetalleContabilidad(pay,
				buscarElementoContableBanco(pay.getC_BankAccount_ID()));

		if(detalle.get(0) != null)
			haber.set_Value("XX_Aux", detalle.get(0));
		if(detalle.get(1) != null)
			haber.set_Value("XX_Departament", detalle.get(1));
		if(detalle.get(2) != null)
			haber.set_Value("XX_Division", detalle.get(2));
		haber.set_Value("XX_DocumentDate", pay.getXX_DateFinalPay());
		if(detalle.get(4) != null)
			haber.set_Value("XX_DocumentType", detalle.get(4));
		haber.set_Value("XX_DueDate", pay.getXX_DateFinalPay());
		haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
		if(detalle.get(7) != null)
		haber.set_Value("XX_Office", detalle.get(7));
		if(detalle.get(8) != null)
			haber.set_Value("XX_SectionCode", detalle.get(8));

		haber.save();
		//Actualizo el Document No en el debe
		haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
		haber.save();

	}

	/*
	 * BUSCO LA CUENTA A LA CUAL VOY A HACER LA TRANSFERENCIA, SIRVE PARA LOS
	 * ANTICIPOS
	 */
	private String buscarCuenta(int id) {
		// TODO Auto-generated method stub
		String cuenta = "";

		String sql = "select BA.ACCOUNTNO"
				+ " from C_Payment P, C_BPartner BP, C_BP_BANKACCOUNT BA"
				+ " where P.C_Payment_ID = " + id
				+ " and BP.C_BPartner_ID = P.C_BPartner_ID"
				+ " and BA.C_BPartner_ID = BP.C_BPartner_ID"
				+ " and BA.XX_ISPRIMARY = 'Y'";
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				cuenta = rs.getString("ACCOUNTNO");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return cuenta;
	}

	/*
	 * BUSCO SI EL SOCIO DE NEGOCIO TIENE UNA RELACION ACTIVA
	 */
	private String buscarRelacion(int id) {

		// TODO Auto-generated method stub
		String Socio = "";

		String sql = "select R.NAME AS RELATION, BP.NAME AS SOCIO"
				+ " from C_Payment P, C_BPartner BP, C_BP_RELATION R"
				+ " where P.C_Payment_ID = " + id
				+ " and BP.C_BPartner_ID = P.C_BPartner_ID"
				+ " and R.C_BPartner_ID = BP.C_BPartner_ID"
				+ " and R.ISACTIVE = 'Y'";
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				Socio = rs.getString("RELATION");
			}// Fin if
			else {
				Socio = buscarSocio(id);
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return Socio;
	}

	/*
	 * BUSCO EL NOMBRE DEL SOCIO DE NEGOCIO, SOLO SE LLAMA SI EL SOCIO DE
	 * NEGOCIO NO TIENE UNA RELACION ACTIVA
	 */
	private String buscarSocio(int id) {

		// TODO Auto-generated method stub
		String Socio = "";

		String sql = "select BP.NAME AS SOCIO"
				+ " from C_Payment P, C_BPartner BP"
				+ " where P.C_Payment_ID = " + id
				+ " and BP.C_BPartner_ID = P.C_BPartner_ID";
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				Socio = rs.getString("SOCIO");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return Socio;
	}

	/*
	 * METODO QUE PAGA LAS SELECCIONES DE PAGO
	 */
	public void PagarSeleccion(int Id) {

		// CREO EL OBJETO C_PAYSELECTION
		MPaySelection psel = new MPaySelection(getCtx(), Id, get_TrxName());

		// GUARDO EL VALOR DEL MONTO DE LA SELECCION DE PAGO
		montoTotal = psel.getTotalAmt();

		// LLAMO AL METODO QUE SE VA A ENCARGAR DE BUSCAR EL PAGO QUE NO HA SIDO
		// PROCESADO DE LAS SELECCIONES DE PAGO
		// BUSCO SI TIENE PAGOS RELACIONADOS
		int pagoAsociado = 0;

		String sql = "SELECT PSC.C_PAYSELECTIONCHECK_ID, PS.C_PAYSELECTION_ID"
				+ " FROM C_PAYSELECTIONLINE PSL, C_PAYSELECTION PS, C_PAYSELECTIONCHECK PSC"
				+ " WHERE PS.C_PAYSELECTION_ID = "
				+ Id
				+ " AND PS.C_PAYSELECTION_ID = PSL.C_PAYSELECTION_ID"
				+ " AND PSL.C_PAYSELECTIONCHECK_ID = PSC.C_PAYSELECTIONCHECK_ID"
				+ " GROUP BY PSC.C_PAYSELECTIONCHECK_ID, PS.C_PAYSELECTION_ID";

		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				// OBTENGO EL ID DEL PAGO QUE ES EL DE LA TABLA->
				// C_PAYSELECTIONCHECK
				pagoAsociado = rs.getInt("C_PAYSELECTIONCHECK_ID");
				// METODO QUE CREO EL PAGO DE ESA SELECCION DE PAGO
				PagarLineasCheque(pagoAsociado, Id);
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		// BUSCO Y CANCELO LAS LINEAS DE PAGO DE ESA SELECCION DE PAGO
		String sql1 = "SELECT C_PAYSELECTIONLINE_ID"
				+ " FROM C_PAYSELECTIONLINE"
				+ " WHERE ISACTIVE = 'Y' AND C_PAYSELECTION_ID= "
				+ psel.getC_PaySelection_ID();

		PreparedStatement prst1 = DB.prepareStatement(sql1, null);
		ResultSet rs1 = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs1 = prst1.executeQuery();
			while (rs1.next()) {

				// OBTENGO EL ID DE LAS SELECCIONES DE PAGO
				int seleccion = rs1.getInt("C_PAYSELECTIONLINE_ID");
				// PAGO LAS LINEAS DE PAGO DE ESA SELECCION
				PagarLineasSeleccion(seleccion);
			}// Fin WHILE
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs1);
			DB.closeStatement(prst1);
		}

		// ASIGNO LA CUENTA BANCARIA DE LA CUAL ESTOY PAGANDO
		psel.setC_BankAccount_ID(bankAccount_ID);
		// LA COLOCO COMO YA PROCESADA, CUANDO ESTE VALOR SEA TRUE EL NO LA
		// VUELVE A BUSCAR
		psel.setProcessing(true);

		psel.save();
	}

	/*
	 * METODO QUE SE ENCARGA DE HACER LAS TRANSFERENCIAS A LAS SELECCION DE PAGO
	 */
	public void PagarLineasSeleccion(int Id) {

		// CREO UN OBJETO C_PAYSELECTIONLINE Y ASIGNO QUE SU FORMA DE PAGO ES
		// TRANSFERENCIA
		MPaySelectionLine line = new MPaySelectionLine(getCtx(), Id,
				get_TrxName());
		line.setPaymentRule("T");
		line.setProcessed(true);
		PagarFactura(line.getC_Invoice_ID(), get_TrxName());
		line.save();
	}

	/*
	 * METODO QUE SE ENCARGA DE REALIZAR EL PAGO DE LA SELECCION DE PAGO
	 */
	public void PagarLineasCheque(int Id, int idSeleccion) {

		// CREO EL OBJETO QUE HACE REFERENCIA AL PAGO DE LA SELECCION DE PAGO
		MPaySelectionCheck cheque = new MPaySelectionCheck(getCtx(), Id,
				get_TrxName());
		// SETEO SUS VALORES
		cheque.setPaymentRule("T");
		cheque.setCheckNo("");
		cheque.setProcessed(true);

		java.util.Date utilDate = new java.util.Date(); // fecha actual
		long lnMilisegundos = utilDate.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);

		// CREO EL PAGO PARA ASIGNARSELO AL PAYSELECTIONCHECK
		MPayment pay = new MPayment(getCtx(), 0, get_Trx());

		// ASIGNO LA TRANSFERENCIA
		pay.set_Value("XX_VCN_BankTransfer_ID", Integer.valueOf(transfer_ID));

		// ASINGO LA ORGANIZACION
		pay.setAD_Org_ID(Env.getCtx().getAD_Org_ID());

		// ASIGNO EL CLIENTE
		pay.setAD_Client_ID(clienteID);

		// ASIGNO EL TIPO DE DOCUMENTO
		pay.setC_DocType_ID(buscarTipoDocumento());

		// ASIGNO LA CUENTA BANCARIA DE LA CUAL HICE LA TRANSFERENCIA
		pay.setC_BankAccount_ID(bankAccount_ID);

		// ASIGNO LA MONEDA
		pay.setC_Currency_ID(buscarTipoMoneda());

		// ASIGNO LA FECHA DE LA TRANSACCION
		pay.setDateTrx(sqlTimestamp);

		// ASIGNO LA FECHA CONTABLE
		pay.setDateAcct(sqlTimestamp);

		// ASIGNO PROCESSED
		pay.setProcessed(true);

		// DESTILDO LOS CAMPOS DE ANTICIPO Y COBRO
		pay.setXX_IsAdvance(false);
		pay.setIsReceipt(false);

		// ASIGNO EL SOCIO DE NEGOCIO
		pay.setC_BPartner_ID(cheque.getC_BPartner_ID());

		// ASIGNO EL NOMBRE DEL SOCIO DE NEGOCIO
		pay.setA_Name(buscarRelacionSelecion(cheque.getC_BPartner_ID()));

		// ASIGNO EL TOTAL DEL PAGO
		pay.setPayAmt(montoTotal);

		// ASIGNO LA FORMA EN QUE LA CANCELE (TRANSFERENCIA)
		pay.setTenderType("T");

		// TOTAL DE DESCUENTO
		java.math.BigDecimal cero = new java.math.BigDecimal("0.00");
		pay.setDiscountAmt(cero);

		// ASIGNO LA FECHA DE PAGO
		pay.setXX_DateFinalPay(sqlTimestamp);

		// GUARDO LOS CAMBIOS
		pay.save();

		// APLICO LA CONTABILIDAD
		// CREO LA CABECERA
		X_XX_VCN_AccoutingEntry cabecera = new X_XX_VCN_AccoutingEntry(
				Env.getCtx(), 0, trans);
		cabecera.setDateFrom(sqlTimestamp);
		cabecera.setDateTo(sqlTimestamp);
		cabecera.setDateTrx(sqlTimestamp);
		cabecera.setDescription("COMPROBANTE DIARIO");
		cabecera.setXX_TotalHave(pay.getPayAmt());
		cabecera.setXX_TotalShould(pay.getPayAmt());
		cabecera.setXX_ListCX017("BA");
		cabecera.setIsTransferred(false);
		String numberControl = String.valueOf(5) + "0" + 0 + String.valueOf(numberControl());
		cabecera.setXX_ControlNumber(numberControl);

		cabecera.save();

		// CREO EL DETALLE DE LA CUENTA CONTABLE *DEBE*
		buscarCuentaSeleccion(pay, cabecera, trans, idSeleccion);
		// CREO EL HABER
		MFactAcct haber = new MFactAcct(Env.getCtx(), 0, trans);
		haber.setAD_Table_ID(318);
		haber.setAccount_ID(buscarElementoContableBanco(pay.getC_BankAccount_ID()));

		haber.setAmtAcctCr(pay.getPayAmt());
		java.math.BigDecimal zero = new java.math.BigDecimal("0");
		haber.setAmtAcctDr(zero);
		haber.setAmtSourceCr(pay.getPayAmt());
		haber.setAmtSourceDr(zero);
		haber.setC_AcctSchema_ID(1000009);
		haber.setC_Currency_ID(205);
		haber.setC_BPartner_ID(pay.getC_BPartner_ID());
		haber.set_ValueNoCheck("XX_VCN_Line",
				buscarCuantasLineas(pay.getC_Payment_ID(), cabecera) + 1);
		haber.set_ValueNoCheck("XX_VCN_AccoutingEntry_ID",
				cabecera.getXX_VCN_AccoutingEntry_ID());
		haber.setC_Period_ID(buscarPeriodoContable());
		haber.setDateAcct(sqlTimestamp);
		haber.setDateTrx(sqlTimestamp);
		haber.setPostingType("A");
		haber.setRecord_ID(pay.getC_Payment_ID());
		haber.setDescription(pay.getA_Name() + " "
				+ buscarNombreElementoContableBanco(pay.getC_BankAccount_ID()));

		// CREO EL DETALLE DEL HABER
		XX_VCN_DetalleContable contable = new XX_VCN_DetalleContable();
		ArrayList<String> detalle = new ArrayList<String>();
		detalle = contable.DetalleContabilidad(pay,
				buscarElementoContableBanco(pay.getC_BankAccount_ID()));
		if(detalle.get(0) != null)
			haber.set_Value("XX_Aux", detalle.get(0));
		if(detalle.get(1) != null)
		haber.set_Value("XX_Departament", detalle.get(1));
		if(detalle.get(2) != null)
			haber.set_Value("XX_Division", detalle.get(2));
		haber.set_Value("XX_DocumentDate", pay.getXX_DateFinalPay());
		if(detalle.get(4) != null)
			haber.set_Value("XX_DocumentType", detalle.get(4));
		haber.set_Value("XX_DueDate", pay.getXX_DateFinalPay());
		haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
		if(detalle.get(7) != null)
		haber.set_Value("XX_Office", detalle.get(7));
		if(detalle.get(8) != null)
		haber.set_Value("XX_SectionCode", detalle.get(8));

		haber.save();
		//Actualizo el Nro de documento
		haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
		haber.save();

		// ASIGNO EL ID DEL PAGO QUE ACABO DE CREAR AL C_PAYSELECTIONCHECK
		cheque.setC_Payment_ID(pay.getC_Payment_ID());
		// GUARDO EL CHEQUE
		cheque.save();

		// COMPLETO EL PAGO QUE ACABO DE CREAR
		pay.setDocAction(X_C_Payment.DOCACTION_Complete);
		DocumentEngine.processIt(pay, X_C_Payment.DOCACTION_Complete);
		pay.save();

		trans.commit();

	}

	/*
	 * METODO QUE SE ENCARGA DE BUSCAR LA CUENTA CONTABLE DE UNA SELECCION
	 */
	private void buscarCuentaSeleccion(MPayment pay,
			X_XX_VCN_AccoutingEntry cabecera, Trx trans, int idSeleccion) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String sql = "select ord.XX_POType, ord.XX_PurchaseType, con.XX_Contract_ID, doc.C_DocType_ID TIPDOC, "
				+ "inv.DocumentNo NRODOC, inv.DateInvoiced FECDOC, "
				+ "(case when inv.XX_DueDate > inv.DateInvoiced then "
				+ "to_char(inv.XX_DueDate,'dd.mm.yyyy') else to_char(inv.DateInvoiced,'dd.mm.yyyy') "
				+ "end) FECVEN,  "
				+ "ord.DocumentNo, psl.PayAmt MONASI, to_char(psl.PayAmt, '999G999G999G999D99') FORMATO, con.XX_Lease, par.value, par.C_BPartner_ID as socio  "
				+ "from C_PaySelection pay  "
				+ "inner join C_PaySelectionLine psl on (pay.C_PaySelection_ID = psl.C_PaySelection_ID ) "
				+ "inner join C_Invoice inv on (psl.C_Invoice_ID = inv.C_Invoice_ID) "
				+ "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) "
				+ "inner join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) "
				+ "left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) "
				+ "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) "
				+ "where pay.C_PaySelection_ID = " + idSeleccion;
		int tipoCategoria = 0;
		int line = 1;
		StringBuffer m_where = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String nroDoc=rs.getString("NRODOC");
				Timestamp fecha= rs.getTimestamp("FECDOC");
				String tipo=buscarTipo(rs.getString("TIPDOC"));
				
				if (rs.getString("XX_POType") == null)
					if ((rs.getString("XX_LEASE") == null)
							|| rs.getString("XX_LEASE").equals("N"))
						// Para Servicios que no sean Arrendadmientos se pasa la
						// categoria de producto Standard (CCAPOTE)
						tipoCategoria = Env.getCtx().getContextAsInt(
								"#M_Product_Category_ID");
					else {
						// Para Servicios de tipo Arrendadmientos se pasa la
						// categoria de producto Servicio con la marca de
						// Arrendamiento (CCAPOTE)
						tipoCategoria = Env.getCtx().getContextAsInt(
								"#XX_L_CATEPRODUCTSERVICES_ID");
						m_where.append(" and XX_Lease = 'Y' ");
					}
				else {
					// Para Mercancia para la Venta
					if (rs.getString("XX_PurchaseType") == null) {
						tipoCategoria = Env.getCtx().getContextAsInt(
								"#XX_L_CATEGORYPRODUCTITEM_ID");
						// Para ubicar la cuenta que le corresponde a Mercancia
						// (CCAPOTE)
						m_where.append(" and XX_ElementType = 'Nacional' "
								+ "and XX_Transitional <> 'Y' ");
					} else if ((rs.getString("XX_PurchaseType").equals("SU"))
							|| (rs.getString("XX_PurchaseType").equals("SE")))
						// Se pasa servicios porque es la misma cuenta tanto la
						// servicios como para sumistros y materiales
						tipoCategoria = Env.getCtx().getContextAsInt(
								"#M_Product_Category_ID");
					else if (rs.getString("XX_PurchaseType").equals("FA")) {
						// Para Activo Fijo se le pasa categoría del producto
						// “Servicio” y marcada como “Cuentas por Pagar”
						tipoCategoria = Env.getCtx().getContextAsInt(
								"#XX_L_CATEPRODUCTSERVICES_ID");
						m_where.append(" and XX_AccountPayable = 'Y' ");
					}
				}
				accounts(tipoCategoria, m_where, pay, cabecera, trans,
						rs.getBigDecimal("MONASI"), rs.getInt("socio"), line,nroDoc,tipo, fecha);
				line = line + 1;
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, sql);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

	}


	/*
	 * BUSCO SI EL SOCIO DE NEGOCIO TIENE UNA RELACION ACTIVA
	 */
	private String buscarRelacionSelecion(int id) {

		// TODO Auto-generated method stub
		String Socio = "";

		String sql = "select R.NAME AS RELATION, BP.NAME AS SOCIO"
				+ " from C_BPartner BP, C_BP_RELATION R"
				+ " where BP.C_BPartner_ID = " + id
				+ " and R.C_BPartner_ID = BP.C_BPartner_ID"
				+ " and R.ISACTIVE = 'Y'";

		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				Socio = rs.getString("RELATION");
			}// Fin if
			else {
				Socio = buscarSocioDeNegocios(id);
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return Socio;
	}

	/*
	 * METODO QUE BUSCA EL NOMBRE DE UN SOCIO DE NEGOCIO
	 */
	private String buscarSocioDeNegocios(int id) {

		// TODO Auto-generated method stub
		String Socio = "";

		String sql = "select BP.NAME AS SOCIO" + " from C_BPartner BP"
				+ " where BP.C_BPartner_ID = " + id;

		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				Socio = rs.getString("SOCIO");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return Socio;
	}

	/*
	 * METODO QUE BUSCA EL ID DE LA MONEDA "BOLIVAR FUERTE"
	 */
	private Integer buscarTipoMoneda() {

		// TODO Auto-generated method stub
		int moneda = 0;

		String sql = "SELECT C_CURRENCY_ID" + " FROM C_CURRENCY"
				+ " WHERE ISO_CODE='VEB'";

		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				moneda = rs.getInt("C_CURRENCY_ID");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return moneda;
	}

	/*
	 * METODO QUE EL ID DEL TIPO DE DOCUMENTO APP PAYMENT
	 */
	private Integer buscarTipoDocumento() {

		// TODO Auto-generated method stub
		int documento = 0;

		String sql = "SELECT C_DOCTYPE_ID" + " FROM C_DOCTYPE"
				+ " WHERE NAME='AP Payment'";

		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				documento = rs.getInt("C_DOCTYPE_ID");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return documento;
	}

	/*
	 * METODO QUE SE ENCARGA DE PAGAR LAS FACTURAS
	 */
	public void PagarFactura(int Id, Trx trans) {

		// CREO UN OBJETO C_PAYSELECTIONLINE Y ASIGNO QUE SU FORMA DE PAGO ES
		// TRANSFERENCIA
		MInvoice factura = new MInvoice(Env.getCtx(), Id, trans);
		factura.set_Value("XX_AccountPayableStatus", "P");
		factura.save();
	}

	/*
	 * METODO QUE SE ENCARGA DE BUSCAR LA CUENTA CONTABLE DE UN ANTICIPO
	 */
	private void buscarCuentaAnticipo(MPayment pay,
			X_XX_VCN_AccoutingEntry cabecera, Trx trans) {
		// TODO Auto-generated method stub
		String sql = "select bp.c_bpartner_id as socio, bp.value VALUE, ord.XX_POType, ord.XX_PurchaseType, pay.XX_VCN_ELEMENTVALUE, "
				+ " (case when pay.C_order_id is null then pay.DocumentNo else ord.DocumentNo end) NRODOC, ord.Created FECDOC, "
				+ "to_char(sysdate,'dd.mm.yyyy') FECVEN, pay.PayAmt MONASI,to_char(pay.PayAmt, '999G999G999G999D99') FORMATO, to_char('ANT') TIPDOC "
				+ "from C_Payment pay "
				+ "left join C_Order ord on (pay.C_Order_ID = ord.C_Order_ID) "
				+ "inner join C_bpartner bp on (pay.c_bpartner_id = bp.c_bpartner_id) "
				+ "where pay.C_Payment_ID = " + pay.getC_Payment_ID();
		int tipoCategoria = 0;
		int line = 1;
		StringBuffer m_where = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				String tipo= rs.getString("TIPDOC");
				String nroDoc= rs.getString("NRODOC");
				Timestamp fecha= rs.getTimestamp("FECDOC");
				int account= rs.getInt("XX_VCN_ELEMENTVALUE");
				
				if (rs.getString("XX_VCN_ELEMENTVALUE") == null){
						// Para Mercancia para la Venta
						if (rs.getString("XX_PurchaseType") == null) {
							tipoCategoria = Env.getCtx().getContextAsInt(
									"#XX_L_CATEGORYPRODUCTITEM_ID");
							// Para ubicar la cuenta que le corresponde a Mercancia
							// (CCAPOTE)
							m_where.append(" and XX_ElementType = 'Nacional' "
									+ "and XX_Transitional <> 'Y' ");
						} else if ((rs.getString("XX_PurchaseType").equals("SU"))
								|| (rs.getString("XX_PurchaseType").equals("SE")))
							// Se pasa servicios porque es la misma cuenta tanto la
							// servicios como para sumistros y materiales
							tipoCategoria = Env.getCtx().getContextAsInt(
									"#M_Product_Category_ID");
						else if (rs.getString("XX_PurchaseType").equals("FA")) {
							// Para Activo Fijo se le pasa categoría del producto
							// “Servicio” y marcada como “Cuentas por Pagar”
							tipoCategoria = Env.getCtx().getContextAsInt(
									"#XX_L_CATEPRODUCTSERVICES_ID");
							m_where.append(" and XX_AccountPayable = 'Y' ");
						}
					
					accounts(tipoCategoria, m_where, pay, cabecera, trans, rs.getBigDecimal("MONASI"), rs.getInt("socio"), line, nroDoc, tipo, fecha);
				}
				else
					accounts1(account, pay, cabecera, trans, rs.getBigDecimal("MONASI"), rs.getInt("socio"), line, nroDoc, tipo, fecha);
				
				line = line + 1;
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, sql);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

	}

	/**
	 * Se encarga de buscar la cuenta contable, a través de los parámetros de
	 * entrada
	 * 
	 * @param cabecera
	 * @param pay
	 * @param trans
	 * @param monto
	 * @param socio
	 * @param tipo 
	 * @param nroDoc 
	 * @param fecha 
	 * @param tipoOC
	 *            Tipo de la Orden de Compra
	 * @param tipoBien
	 *            Tipo de Bien
	 * @return La cuenta contable
	 */
	public void accounts(int tipoCategoria, StringBuffer m_where, MPayment pay,
			X_XX_VCN_AccoutingEntry cabecera, Trx trans, BigDecimal monto,
			int socio, int line, String nroDoc, String tipo, Timestamp fecha) {
		String sqlAccount = "select cev.c_elementvalue_id "
				+ "from C_ElementValue cev "
				+ "inner join M_Product_Category mpc on (cev.M_Product_Category_ID = mpc.M_Product_Category_ID) "
				+ "where cev.AccountType = 'L' "
				+ "and mpc.M_Product_Category_ID = " + tipoCategoria + m_where;

		int account = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlAccount, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				account = rs.getInt(1);
				MFactAcct debe = new MFactAcct(Env.getCtx(), 0, trans);
				debe.setAD_Table_ID(318);
				debe.setAccount_ID(account);
				java.math.BigDecimal cero = new java.math.BigDecimal("0");

				// VERIFICA QUE EN EL DEBE NO EXISTAN MONTOS NEGATIVOS EN EL
				// DEBE
				if (monto.compareTo(BigDecimal.ZERO) < 0) {
					debe.setAmtAcctCr(monto.abs());
					debe.setAmtAcctDr(cero);
					debe.setAmtSourceCr(monto.abs());
					debe.setAmtSourceDr(cero);
				} else {
					debe.setAmtAcctCr(cero);
					debe.setAmtAcctDr(monto);
					debe.setAmtSourceCr(cero);
					debe.setAmtSourceDr(monto);
				}
				debe.setC_AcctSchema_ID(1000009);
				debe.setC_BPartner_ID(socio);
				debe.setC_Currency_ID(205);
				debe.set_ValueNoCheck("XX_VCN_Line", line);
				debe.set_ValueNoCheck("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
				debe.setC_Period_ID(buscarPeriodoContable());
				java.util.Date utilDate = new java.util.Date(); // FECHA ACTUAL
				long lnMilisegundos = utilDate.getTime();
				java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
				debe.setDateAcct(sqlTimestamp);
				debe.setDateTrx(sqlTimestamp);
				debe.setPostingType("A");
				debe.setRecord_ID(pay.getC_Payment_ID());
				debe.setDescription(pay.getA_Name());
				// CREO EL DETALLE DEL DEBE
				XX_VCN_DetalleContable contable = new XX_VCN_DetalleContable();
				ArrayList<String> detalle = new ArrayList<String>();
				detalle = contable.DetalleContabilidad(pay, account);

				if(detalle.get(0) != null)
					debe.set_Value("XX_Aux", detalle.get(0));
				if(detalle.get(1) != null)
					debe.set_Value("XX_Departament", detalle.get(1));
				if(detalle.get(2) != null)
					debe.set_Value("XX_Division", detalle.get(2));
				debe.set_Value("XX_DocumentDate", fecha);
				if(detalle.get(4) != null)
					debe.set_Value("XX_DocumentType", tipo);
				
					debe.set_Value("XX_DueDate", fecha);
					debe.set_Value("DocumentNo", nroDoc);
					
				if(detalle.get(7) != null)
					debe.set_Value("XX_Office", detalle.get(7));
				if(detalle.get(8) != null)
				debe.set_Value("XX_SectionCode", detalle.get(8));

				debe.save();
				//Actualizo el Nro de documento
				debe.set_Value("DocumentNo", nroDoc.substring(0,10));
				debe.save();
				trans.commit();

			}
		} catch (Exception e) {
			log.log(Level.SEVERE, sqlAccount);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

	}
	/**
	 * Se encarga de buscar la cuenta contable, a través de los parámetros de
	 * entrada
	 * 
	 * @param cabecera
	 * @param pay
	 * @param trans
	 * @param monto
	 * @param socio
	 * @param tipo 
	 * @param nroDoc 
	 * @param fecha 
	 * @param tipoOC
	 *            Tipo de la Orden de Compra
	 * @param tipoBien
	 *            Tipo de Bien
	 * @return La cuenta contable
	 */
	public void accounts1 (int account, MPayment pay,
			X_XX_VCN_AccoutingEntry cabecera, Trx trans, BigDecimal monto,
			int socio, int line, String nroDoc, String tipo, Timestamp fecha) {
	
				MFactAcct debe = new MFactAcct(Env.getCtx(), 0, trans);
				debe.setAD_Table_ID(318);
				debe.setAccount_ID(account);
				java.math.BigDecimal cero = new java.math.BigDecimal("0");

				// VERIFICA QUE EN EL DEBE NO EXISTAN MONTOS NEGATIVOS EN EL
				// DEBE
				if (monto.compareTo(BigDecimal.ZERO) < 0) {
					debe.setAmtAcctCr(monto.abs());
					debe.setAmtAcctDr(cero);
					debe.setAmtSourceCr(monto.abs());
					debe.setAmtSourceDr(cero);
				} else {
					debe.setAmtAcctCr(cero);
					debe.setAmtAcctDr(monto);
					debe.setAmtSourceCr(cero);
					debe.setAmtSourceDr(monto);
				}
				debe.setC_AcctSchema_ID(1000009);
				debe.setC_BPartner_ID(socio);
				debe.setC_Currency_ID(205);
				debe.set_ValueNoCheck("XX_VCN_Line", line);
				debe.set_ValueNoCheck("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
				debe.setC_Period_ID(buscarPeriodoContable());
				java.util.Date utilDate = new java.util.Date(); // FECHA ACTUAL
				long lnMilisegundos = utilDate.getTime();
				java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
				debe.setDateAcct(sqlTimestamp);
				debe.setDateTrx(sqlTimestamp);
				debe.setPostingType("A");
				debe.setRecord_ID(pay.getC_Payment_ID());
				debe.setDescription(pay.getA_Name());
				// CREO EL DETALLE DEL DEBE
				XX_VCN_DetalleContable contable = new XX_VCN_DetalleContable();
				ArrayList<String> detalle = new ArrayList<String>();
				detalle = contable.DetalleContabilidad(pay, account);

				if(detalle.get(0) != null)
					debe.set_Value("XX_Aux", detalle.get(0));
				if(detalle.get(1) != null)
					debe.set_Value("XX_Departament", detalle.get(1));
				if(detalle.get(2) != null)
					debe.set_Value("XX_Division", detalle.get(2));
				debe.set_Value("XX_DocumentDate", fecha);
				if(detalle.get(4) != null)
					debe.set_Value("XX_DocumentType", tipo);
				
					debe.set_Value("XX_DueDate", fecha);
					debe.set_Value("DocumentNo", nroDoc);
					
				if(detalle.get(7) != null)
					debe.set_Value("XX_Office", detalle.get(7));
				if(detalle.get(8) != null)
				debe.set_Value("XX_SectionCode", detalle.get(8));

				debe.save();
				//Actualizo el Nro de documento
				String numDoc1=nroDoc;
				if(numDoc1.length()>10)
					numDoc1=numDoc1.substring(numDoc1.length() - 9, numDoc1.length());
			
				debe.set_Value("DocumentNo", numDoc1);
				debe.save();
				trans.commit();

			}
		


	/*
	 * METODO QUE SE ENCARGA DE BUSCAR EL PERIODO ACTUAL
	 */
	private int buscarPeriodoContable() {
		// TODO Auto-generated method stub
		int id = 0;
		Calendar c1 = Calendar.getInstance();
		int mes = c1.get(Calendar.MONTH);
		mes=mes+1;
		String mesletra = "";
		int year = c1.get(Calendar.YEAR);

		if (mes == 1)
			mesletra = "Enero";
		else if (mes == 2)
			mesletra = "Febrero";
		else if (mes == 3)
			mesletra = "Marzo";
		else if (mes == 4)
			mesletra = "Abril";
		else if (mes == 5)
			mesletra = "Mayo";
		else if (mes == 6)
			mesletra = "Junio";
		else if (mes == 7)
			mesletra = "Julio";
		else if (mes == 8)
			mesletra = "Agosto";
		else if (mes == 9)
			mesletra = "Septiembre";
		else if (mes == 10)
			mesletra = "Octubre";
		else if (mes == 11)
			mesletra = "Noviembre";
		else if (mes == 12)
			mesletra = "Diciembre";

		mesletra = mesletra + " " + year;
		String mesletraMayuscula = mesletra.toUpperCase();

		String sql = "select c_period_id" + " from c_period"
				+ " where name = '" + mesletra + "' OR name= '"
				+ mesletraMayuscula + "'";
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return id;
	}

	/*
	 * METODO QUE SE ENCARGA DE BUSCAR EL ELEMENTO CONTABLE DE UN BANCO
	 */
	private int buscarElementoContableBanco(int c_BankAccount_ID) {
		// TODO Auto-generated method stub
		int cuenta = 0;
		String sql = "select c_elementValue.c_elementValue_id as cuenta,c_elementValue.name, c_elementValue.value value"
				+ " from C_BankAccount_Acct, c_validCombination, c_elementvalue"
				+ " where C_BankAccount_Acct.C_BankAccount_ID="
				+ c_BankAccount_ID
				+ " and C_BankAccount_Acct.C_AcctSchema_ID=1000009"
				+ " and C_BankAccount_Acct.B_INTRANSIT_ACCT= c_validCombination.c_validCombination_id"
				+ " and c_validCombination.ACCOUNT_ID= c_elementvalue.c_elementvalue_id";
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				cuenta = rs.getInt("cuenta");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return cuenta;
	}

	/*
	 * METODO QUE BUSCA CUANTAS LINEAS CONTABLES TIENE UN PAGO
	 */
	private Integer buscarCuantasLineas(int c_Payment_ID,
			X_XX_VCN_AccoutingEntry cabecera) {
		// TODO Auto-generated method stub
		int cuenta = 0;

		String sql = "select COUNT(*) as cuenta" + " from Fact_acct"
				+ " where Record_id = " + c_Payment_ID
				+ " and XX_VCN_ACCOUTINGENTRY_ID = "
				+ cabecera.getXX_VCN_AccoutingEntry_ID();

		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				cuenta = rs.getInt("cuenta");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return cuenta;
	}

	/*
	 * METODO QUE SE ENCARGA DE BUSCAR NOMBRE DE UN EL ELEMENTO CONTABLE DE UN
	 * BANCO
	 */
	private String buscarNombreElementoContableBanco(int c_BankAccount_ID) {
		// TODO Auto-generated method stub
		String nombre = "";
		String sql = "select c_elementValue.c_elementValue_id as cuenta,c_elementValue.name as name"
				+ " from C_BankAccount_Acct, c_validCombination, c_elementvalue"
				+ " where C_BankAccount_Acct.C_BankAccount_ID="
				+ c_BankAccount_ID
				+ " and C_BankAccount_Acct.C_AcctSchema_ID=1000009"
				+ " and C_BankAccount_Acct.B_INTRANSIT_ACCT= c_validCombination.c_validCombination_id"
				+ " and c_validCombination.ACCOUNT_ID= c_elementvalue.c_elementvalue_id";
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				nombre = rs.getString("name");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return nombre;
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
	
	/*
	 * METODO QUE BUSCA EN EL C_DOCTYPE EL TIPO DE DOCUMENTO
	 */
	private String buscarTipo(String id) {
		// TODO Auto-generated method stub
		String tipo="";
		String sql = "select XX_CODDOC from C_DOCTYPE where C_DOCTYPE_ID = "+id;
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				tipo = rs.getString("XX_CODDOC");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return tipo;
	}	
	
}
