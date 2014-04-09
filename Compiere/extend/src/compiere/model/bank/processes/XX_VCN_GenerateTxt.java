package compiere.model.bank.processes;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.bank.X_XX_VCN_BankTransfer;

public class XX_VCN_GenerateTxt extends SvrProcess{
	
	private int			bank_ID = 0;
	private String nombre_Bank = "";
	public boolean Error= false;
	public String mjsError="";
	//Banco Venezolano de credito
	public String codigoempresa=null;
	public String numerolote=null;
	//Array list para el archivo de texto
	public ArrayList<String> lineasList = new ArrayList<String>();

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		X_XX_VCN_BankTransfer aux = new X_XX_VCN_BankTransfer(getCtx(),getRecord_ID(),get_TrxName());
		//OBTENGO EL ID DEL BANCO
		bank_ID= aux.getC_Bank_ID();
				String sql= "SELECT NAME"
				+" FROM    C_BANK"
				+" WHERE C_BANK_ID = "+bank_ID;
				
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		if (rs.next()){

				   			//OBTENGO EL NOMBRE DEL BANCO
				   			nombre_Bank = rs.getString("NAME");
				   						   			
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
	
				   	if (nombre_Bank.equals("Banco Exterior")){
				   		generarTXTExterior(aux);
				   	}
				   	if (nombre_Bank.equals("Banco Mercantil")){
				   		generarTXTMercantil(aux);
				   	}
				   	if (nombre_Bank.equals("Banesco Banco Universal")){
				   		generarTXTBanesco(aux);
				   	}
				   	if (nombre_Bank.equals("Venezolano de  Crédito")){
				   		generarTXTVenezolanoCredito(aux);
				   	}
				   	if (nombre_Bank.equals("BBVA Banco Provincial")){
				   		generarTXTProvincial(aux);
				   	}
		
		return null;
	}
	
	
	private void generarTXTProvincial(X_XX_VCN_BankTransfer aux) {
		// TODO Auto-generated method stub
		otrasLineasProvincialSeleccion(aux);
		otrasLineasProvincialAnticipo(aux);
		//CREO EL ARCHIVO
		Date fecha = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("ddMMyyHmmss");
		String nombreArchivo = "Provincial"+ formato.format(fecha);
		escribirArchivo(lineasList,nombreArchivo, Error, mjsError);
			System.out.println("Provincial");
			System.out.println("Error->"+mjsError);
		
	}

	private void generarTXTVenezolanoCredito(X_XX_VCN_BankTransfer aux) {
		// TODO Auto-generated method stub

		//AGREGO LAS OTRAS LINEAS
		otrasLineasVenezolanoCreditoSeleccion(aux);
		otrasLineasVenezolanoCreditoAnticipo(aux);
		//AGREGO LA ULTIMA LINEA
		ultimalineaVenezolanoCredito(codigoempresa, numerolote, aux);
		
			//CREO EL ARCHIVO
			Date fecha = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("ddMMyyHmmss");
			String nombreArchivo = "VenezolanoCredito"+ formato.format(fecha);
			escribirArchivo(lineasList,nombreArchivo, Error, mjsError);
				System.out.println("Venezolano de Credito");
				System.out.println("Error->"+mjsError);
		
		
		
	}

	private void generarTXTBanesco(X_XX_VCN_BankTransfer aux) {
		// TODO Auto-generated method stub
		int empresa = Env.getCtx().getAD_Client_ID();
		
		//PRIMERA LINEA
		String uno="HDRBANESCO        ED 95B PAYMULP";
		//SEGUNDA LINEA
		String numOrdenPago= Integer.toString(aux.getXX_VCN_BankTransfer_ID());
			while(numOrdenPago.length() < 35) numOrdenPago= numOrdenPago+" ";
		String fechaCreacion="";
		Calendar calendario = Calendar.getInstance();
		int hora =calendario.get(Calendar.HOUR_OF_DAY);
		if (hora<10){
			Date fecha = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
			fechaCreacion = formato.format(fecha);
		}
		else{
			GregorianCalendar workingDate = new GregorianCalendar();
			workingDate.add(Calendar.DAY_OF_MONTH, 1);
			Date myDate = workingDate.getTime();
			SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
			fechaCreacion = formato.format(myDate);
		}
		String dos="01SAL                                9  "+numOrdenPago+fechaCreacion;
		//TERCERA LINEA
		String tres="02";
			//NUMERO DE REFERENCIA DE CREDITO
			String referenciaCredito= Integer.toString(aux.getXX_VCN_BankTransfer_ID());
				if(referenciaCredito.length()>8){
			        	Error = true;
			        	mjsError = mjsError + "El numero de referencia de credito que es el id de la transferencia debe ser menor a 8 caracteres";
			     }
				else{
					while(referenciaCredito.length()<8) referenciaCredito= referenciaCredito+"0";
					while(referenciaCredito.length()<30) referenciaCredito= referenciaCredito+" ";
				}
		tres= tres+referenciaCredito;
			//RIF
			String tipoper =buscarRifCompañia(empresa);
			char primero = tipoper.charAt(0);
			String segundo = quitarGuionDerecha(tipoper,9);
			String rif=primero+segundo;
			while(rif.length()<17) rif=rif+" ";
		tres= tres+rif;
			//NOMBRE DEL ORDENANTE
			String nombre=buscarNombreCompañia(empresa);
			if(nombre.length()>35){
				nombre= nombre.substring(0,34);
				//Error = true;
	        	mjsError = mjsError + "El nombre del ordenante debe ser menor a 35 caracteres, se recorto el nombre";
			}
			while(nombre.length()<35) nombre=nombre+" ";
		tres= tres+nombre;
			//MONTO TOTAL A ABONAR
			String monto= aux.getXX_VCN_Amount().toString();
			DecimalFormat formateador = new DecimalFormat("#.00");
			monto=formateador.format(Double.parseDouble(monto));
			
			String parteEntera= monto.substring(0,monto.length()-3);
				while(parteEntera.length()<13)
					parteEntera= "0"+parteEntera;
			String parteDecimal = monto.substring(monto.length()-2,monto.length());
			monto= parteEntera+parteDecimal;
		tres=tres+monto;
			//MONEDA
		tres= tres+"VEF";
			//INSTRUCCION DEL ORDENANTE
		tres= tres+" ";
			//NUMERO DE CUENTA A ACREDITAR
			String cuenta= numeroDeCuenta(aux.getC_BankAccount_ID());
			while(cuenta.length()<34) cuenta= cuenta+" ";
		tres= tres+cuenta;	
			//CODIGO DEL BANCO ORDENANDO
		tres= tres+"BANESCO    ";
			//FECHA VALOR DE LA ORDEN
			String fechaValor="";
			Calendar calendario1 = Calendar.getInstance();
			int hora1 =calendario1.get(Calendar.HOUR_OF_DAY);
				if (hora1<10){
					Date fecha = new Date();
					SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");
					fechaValor = formato.format(fecha);
				}
				else{
					GregorianCalendar workingDate = new GregorianCalendar();
					workingDate.add(Calendar.DAY_OF_MONTH, 1);
					Date myDate = workingDate.getTime();
					SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");
					fechaValor = formato.format(myDate);
				}
		tres=tres+fechaValor;

			//AGREGO LA PRIMERA, SEGUNDA Y TERCERA LINEA
			lineasList.add(uno);
			lineasList.add(dos);
			lineasList.add(tres);

			//AGREGO LAS OTRAS LINEAS
			otrasLineasBanescoAnticipo(aux);
			otrasLineasBanescoSeleccion(aux);
			
			//AGREGO LA LINEA DE REGISTRO DOCUMENTO
						//Tipo de Registro
						String lineaRegistroDoc="04";
						//Monto Original del Documento
						lineaRegistroDoc=lineaRegistroDoc+monto;
						//Monto a Pagar
						lineaRegistroDoc=lineaRegistroDoc+monto;
						//Fecha del Documento
						lineaRegistroDoc=lineaRegistroDoc+fechaValor;
						//Número del Documento
						lineaRegistroDoc=lineaRegistroDoc+numOrdenPago.substring(0,30);
						//Tipo de Documento (Desc.)
						String tipoDoc="TRANSFERENCIA BANCARIA";
							while (tipoDoc.length()<70) tipoDoc=tipoDoc+" ";
							lineaRegistroDoc=lineaRegistroDoc+tipoDoc;
						//Tipo de Documento (Cod.)
							lineaRegistroDoc=lineaRegistroDoc+"380";
						//Moneda Monto a Pagar (Cod.)
							lineaRegistroDoc=lineaRegistroDoc+"VEF";
						//Moneda Monto Original
							lineaRegistroDoc=lineaRegistroDoc+"VEF";
						
						lineasList.add(lineaRegistroDoc);	
						
			//REGISTRO AJUSTES DEL CREDITO
						//Tipo de Registro
						String lineaRegistroAjustCred="05";
						//Tipo de Ajuste
						lineaRegistroAjustCred=lineaRegistroAjustCred+"211";
						//Moneda (Cod.)
						lineaRegistroAjustCred=lineaRegistroAjustCred+"VEF";
						//Monto del Ajuste
						lineaRegistroAjustCred=lineaRegistroAjustCred+monto;
						//Descripción del Ajuste (Cod.)
						lineaRegistroAjustCred=lineaRegistroAjustCred+"000";
						//Descripción del Ajuste
						lineaRegistroAjustCred=lineaRegistroAjustCred+"TRANSFERENCIA BANCARIA                                                ";
						
						lineasList.add(lineaRegistroAjustCred);
			
			//REGISTRO DE TOTALES
						//Tipo de Registro
						String lineaRegistroTotales="06";
						//Total de Débitos
						lineaRegistroTotales=lineaRegistroTotales+"000000000000001";
						//Total de Créditos
						String totalDebito= cantidadPagos(aux.getXX_VCN_BankTransfer_ID());
						while(totalDebito.length()<15) totalDebito= "0"+totalDebito;
						lineaRegistroTotales=lineaRegistroTotales+totalDebito;
						//Monto Total a Pagar
						lineaRegistroTotales=lineaRegistroTotales+monto;
						
						lineasList.add(lineaRegistroTotales);

						/*
						//CANTIDAD TOTAL REGISTROS DE DETALLE
						String totalCredito= "000000000000001";
					
						String totalDebito= cantidadPagos(aux.getXX_VCN_BankTransfer_ID());
							while(totalDebito.length()<15) totalDebito= "0"+totalDebito;
					ultimaLinea=ultimaLinea+totalCredito+totalDebito;
						//MONTO TOTAL A COBRAR
						String montoTotal= aux.getXX_VCN_Amount().toString();
						montoTotal=formateador.format(Double.parseDouble(montoTotal));
						
						String parteEntera1= montoTotal.substring(0,montoTotal.length()-3);
						
							while(parteEntera1.length()<13)
								parteEntera1= "0"+parteEntera1;

						String parteDecimal1 = montoTotal.substring(montoTotal.length()-2,montoTotal.length());

			ultimaLinea= ultimaLinea+parteEntera1+parteDecimal1;
			
			lineasList.add(ultimaLinea);
			*/
			//CREO EL ARCHIVO
			Date fecha = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("ddMMyyHmmss");
			String nombreArchivo = "Banesco"+ formato.format(fecha);
			escribirArchivo(lineasList,nombreArchivo, Error, mjsError);
				System.out.println("Banesco");
				System.out.println("Error->"+mjsError);
	}

	private void generarTXTMercantil(X_XX_VCN_BankTransfer aux) {
		// TODO Auto-generated method stub
		
		int empresa = Env.getCtx().getAD_Client_ID();
		
		//TIPO DE REGISTRO
		String uno = "1";
		//IDENTIFICACION DEL PAGO
		String dos= "BAMRVECA    ";
		//NUMERO DE ARCHIVO DEL CLIENTE
		String tres= Integer.toString(aux.getXX_VCN_BankTransfer_ID());
				while(tres.length()<15) tres= "0"+tres;
		//TIPO DE PRODUCTO
		String cuatro= "PROVE";
		//TIPO DE PAGO
		String cinco="0000000062";
		//TIPO DE IDENTIFICACION
		String tipoper =buscarRifCompañia(empresa);
		char seis = tipoper.charAt(0);
		//NUMERO DE IDENTIFICACION
		String siete = quitarGuionIzquierda(tipoper,15);
		//CANTIDAD TOTAL REGISTROS DE DETALLE
		String ocho= cantidadPagos(aux.getXX_VCN_BankTransfer_ID());
			while(ocho.length()<8) ocho= "0"+ocho;
		//MONTO TOTAL
		String nueve= aux.getXX_VCN_Amount().toString();
		DecimalFormat formateador = new DecimalFormat("#.00");
		nueve=formateador.format(Double.parseDouble(nueve));

		String parteEntera= nueve.substring(0,nueve.length()-3);
			while(parteEntera.length()<15)
				parteEntera= "0"+parteEntera;
		String parteDecimal = nueve.substring(nueve.length()-2,nueve.length());
		nueve= parteEntera+parteDecimal;
		//FECHA VALOR
		String diez="";
		Calendar calendario = Calendar.getInstance();
		
		int hora =calendario.get(Calendar.HOUR_OF_DAY);
		if (hora<10){
			Date fecha = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
			diez = formato.format(fecha);
		}
		else{
			GregorianCalendar workingDate = new GregorianCalendar();
			workingDate.add(Calendar.DAY_OF_MONTH, 1);
			Date myDate = workingDate.getTime();
			SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
			diez = formato.format(myDate);
		}
		//CODIGO CUENTA CLIENTE
		String once= numeroDeCuenta(aux.getC_BankAccount_ID());
		//AREA RESERVADA
		String doce="0000000";
		//NUMERO SERIAL NOTA EMPRESA
		String trece= Integer.toString(aux.getXX_VCN_BankTransfer_ID());
			if(trece.length() > 8){ 
	        	Error = true;
	        	mjsError = mjsError + "El numero serial nota empresa tiene mas de 8 caracteres";
	        }
			while (trece.length() < 8) trece="0"+trece;
		//CODIGO RESPUESTA
		String catorce= "0000";
		//FECHA PROCESO
		String quince= "00000000";
		//AREA RESERVADA
		String dieciseis="000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
			
		//PRIMERA LINEA
		String primeraLinea= uno+dos+tres+cuatro+cinco+seis+siete+ocho+nueve+diez+once+doce+trece+catorce+quince+dieciseis;
		
		//AGREGO LA PRIMERA LINEA
		lineasList.add(primeraLinea);
		
		//AGREGO LAS OTRAS LINEAS
		otrasLineasMercantilAnticipo(aux);
		otrasLineasMercantilSeleccion(aux);
		//CREO EL ARCHIVO
		Date fecha = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("ddMMyyHmmss");
		String nombreArchivo = "Mercantil"+ formato.format(fecha);
		escribirArchivo(lineasList,nombreArchivo, Error, mjsError);
			System.out.println("Mercantil");
			System.out.println("Error->"+mjsError);
	}


	public void generarTXTExterior(X_XX_VCN_BankTransfer aux){
		//GENERO LA PRIMERA LINEA DEL ARCHIVO
		
			int empresa = Env.getCtx().getAD_Client_ID();
			
			//TIPO DE PERSONA
			String tipoper =buscarRifCompañia(empresa);
			char uno = tipoper.charAt(0);
			
			//NUMERO DE EMPRESA
			String dos = quitarGuionDerecha(tipoper,9);
			
			//NUMERO CUENTA A DEBITAR
			String tres= numeroDeCuenta(aux.getC_BankAccount_ID());
			
			//CANTIDAD DE PAGOS
			String cuatro= cantidadPagos(aux.getXX_VCN_BankTransfer_ID());
			
			//MONTO DEL PAGO
			String cinco= aux.getXX_VCN_Amount().toString();
			DecimalFormat formateador = new DecimalFormat("#.00");
			cinco=formateador.format(Double.parseDouble(cinco));

				String parteEntera= cinco.substring(0,cinco.length()-3);
				while(parteEntera.length()<11)
					parteEntera= "0"+parteEntera;
				String parteDecimal = cinco.substring(cinco.length()-2,cinco.length());
			cinco= parteEntera+parteDecimal;
			
			//FECHA DEL PAGO
			String seis="";
			Calendar calendario = Calendar.getInstance();
			
			int hora =calendario.get(Calendar.HOUR_OF_DAY);
			if (hora<10){
				Date fecha = new Date();
				SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
				seis = formato.format(fecha);
			}
			else{
				GregorianCalendar workingDate = new GregorianCalendar();
				workingDate.add(Calendar.DAY_OF_MONTH, 1);
				Date myDate = workingDate.getTime();
				SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
				seis = formato.format(myDate);
			}

			//CODIGO DEL SERVICIO
			String siete = "01";
			
			//CONCATENO LA PRIMERA LINEA
			String primeralinea= Character.toString(uno)+dos+tres+cuatro+cinco+seis+siete;
						
			if(primeralinea.length() != 57){ 
	        	Error = true;
	        	mjsError = mjsError + "LA PRIMERA LINEA TIENE MAS DE 57 CARACTERES";
	        }
			
		//AGREGO LA PRIMERA LINEA A LA LISTA
		lineasList.add(primeralinea);
		//AGREGO LAS OTRAS LINEAS
		otrasLineasExteriorAnticipo(aux);
		otrasLineasExteriorSeleccion(aux);
		//CREO EL ARCHIVO
		Date fecha = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("ddMMyyHmmss");
		String nombreArchivo = "Exterior"+ formato.format(fecha);
		escribirArchivo(lineasList,nombreArchivo, Error, mjsError);
			System.out.println("Exterior");
			System.out.println("Error->"+mjsError);
	}
	//---------------OTRAS LINEAS PROVINCIAL-------------------------
			private void otrasLineasProvincialSeleccion(X_XX_VCN_BankTransfer aux) {
				// TODO Auto-generated method stub
				String sql=
						"SELECT CBP.NAME BENEFICIARIO, CPS.TOTALAMT MONTO, CB.VALUE CODIGO, CBPBA.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA"
						+" FROM C_BPARTNER CBP, C_INVOICE CI, C_PAYSELECTION CPS, C_PAYSELECTIONLINE CPSL, XX_VCN_BANKTRANSFERDETAILS BTDS, XX_VCN_BANKTRANSFER BT, C_BANK CB, C_BP_BANKACCOUNT CBPBA"
						+" WHERE "
						+" CPS.C_PAYSELECTION_ID=CPSL.C_PAYSELECTION_ID"
						+" AND CPSL.C_INVOICE_ID=CI.C_INVOICE_ID"
						+" AND CBP.C_BPARTNER_ID=CI.C_BPARTNER_ID"
						+" AND CPS.C_PAYSELECTION_ID=BTDS.C_PAYSELECTION_ID"
						+" AND BT.XX_VCN_BANKTRANSFER_ID=BTDS.XX_VCN_BANKTRANSFER_ID"
						+" AND CB.C_BANK_ID=CBPBA.C_BANK_ID"
						+" AND CBP.C_BPARTNER_ID=CBPBA.C_BPARTNER_ID"
						+" AND CBPBA.XX_ISPRIMARY='Y'"
						+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID()
						+" GROUP BY CBP.NAME, CPS.TOTALAMT, CB.VALUE,CBPBA.ACCOUNTNO,CBP.XX_TYPEPERSON, CBP.XX_CI_RIF";
				
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		while (rs.next()){

				   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
				   			monto = monto.setScale(2, RoundingMode.CEILING);
				   			String beneficiario = rs.getString("BENEFICIARIO");
				   			String codigo= rs.getString("CODIGO");
				   			String cuenta= rs.getString("CUENTA");
				   			String tipoPersona= rs.getString("TIPOPERSONA");
				   			String cedula= rs.getString("CEDULA");
				   			
				   			validarSegProvincial(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,aux);			   						   			
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
			
			public void otrasLineasProvincialAnticipo(X_XX_VCN_BankTransfer aux){
				String sql= 
				"SELECT  P.PAYAMT MONTO, CBP.NAME BENEFICIARIO, B.VALUE CODIGO, BP.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA, BTP.XX_VCN_BANKTRANSFERDETAILP_ID DETALLE, BT.XX_VCN_BANKTRANSFER_ID TRANSFERENCIA"
				+" FROM C_PAYMENT P, C_BPARTNER CBP, C_BANK B, C_BP_BANKACCOUNT BP, XX_VCN_BANKTRANSFER BT, XX_VCN_BANKTRANSFERDETAILP BTP"
				+" WHERE P.C_BPARTNER_ID = CBP.C_BPARTNER_ID"
				+" AND B.C_BANK_ID = BP.C_BANK_ID"
				+" AND BP.C_BPARTNER_ID=CBP.C_BPARTNER_ID"
				+" AND BT.XX_VCN_BANKTRANSFER_ID = BTP.XX_VCN_BANKTRANSFER_ID"
				+" AND BTP.C_PAYMENT_ID = P.C_PAYMENT_ID"
				+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID();
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		while (rs.next()){

				   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
				   			monto = monto.setScale(2, RoundingMode.CEILING);
				   			String beneficiario = rs.getString("BENEFICIARIO");
				   			String codigo= rs.getString("CODIGO");
				   			String cuenta= rs.getString("CUENTA");
				   			String tipoPersona= rs.getString("TIPOPERSONA");
				   			String cedula= rs.getString("CEDULA");
				   			
				   			validarSegProvincial(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,aux);
				   						   			
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

	
	
	//---------------OTRAS LINEAS VENEZOLANO DE CREDITO-------------------------
		private void otrasLineasVenezolanoCreditoSeleccion(X_XX_VCN_BankTransfer aux) {
			// TODO Auto-generated method stub
			String sql=
					"SELECT CBP.NAME BENEFICIARIO, CPS.TOTALAMT MONTO, CB.VALUE CODIGO, CBPBA.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA"
					+" FROM C_BPARTNER CBP, C_INVOICE CI, C_PAYSELECTION CPS, C_PAYSELECTIONLINE CPSL, XX_VCN_BANKTRANSFERDETAILS BTDS, XX_VCN_BANKTRANSFER BT, C_BANK CB, C_BP_BANKACCOUNT CBPBA"
					+" WHERE "
					+" CPS.C_PAYSELECTION_ID=CPSL.C_PAYSELECTION_ID"
					+" AND CPSL.C_INVOICE_ID=CI.C_INVOICE_ID"
					+" AND CBP.C_BPARTNER_ID=CI.C_BPARTNER_ID"
					+" AND CPS.C_PAYSELECTION_ID=BTDS.C_PAYSELECTION_ID"
					+" AND BT.XX_VCN_BANKTRANSFER_ID=BTDS.XX_VCN_BANKTRANSFER_ID"
					+" AND CB.C_BANK_ID=CBPBA.C_BANK_ID"
					+" AND CBP.C_BPARTNER_ID=CBPBA.C_BPARTNER_ID"
					+" AND CBPBA.XX_ISPRIMARY='Y'"
					+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID()
					+" GROUP BY CBP.NAME, CPS.TOTALAMT, CB.VALUE,CBPBA.ACCOUNTNO,CBP.XX_TYPEPERSON, CBP.XX_CI_RIF";
			
			PreparedStatement prst = DB.prepareStatement(sql,null);
		   	ResultSet rs = null;
		   	//EJECUTO LA SENTENCIA SQL
			   	try {
			   		rs = prst.executeQuery();
			   		while (rs.next()){

			   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
			   			monto = monto.setScale(2, RoundingMode.CEILING);
			   			String beneficiario = rs.getString("BENEFICIARIO");
			   			String codigo= rs.getString("CODIGO");
			   			String cuenta= rs.getString("CUENTA");
			   			String tipoPersona= rs.getString("TIPOPERSONA");
			   			String cedula= rs.getString("CEDULA");
			   			
			   			validarSegVenezolanoCredito(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,aux);			   						   			
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
		
		
		public void otrasLineasVenezolanoCreditoAnticipo(X_XX_VCN_BankTransfer aux){
			String sql= 
			"SELECT  P.PAYAMT MONTO, CBP.NAME BENEFICIARIO, B.VALUE CODIGO, BP.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA, BTP.XX_VCN_BANKTRANSFERDETAILP_ID DETALLE, BT.XX_VCN_BANKTRANSFER_ID TRANSFERENCIA"
			+" FROM C_PAYMENT P, C_BPARTNER CBP, C_BANK B, C_BP_BANKACCOUNT BP, XX_VCN_BANKTRANSFER BT, XX_VCN_BANKTRANSFERDETAILP BTP"
			+" WHERE P.C_BPARTNER_ID = CBP.C_BPARTNER_ID"
			+" AND B.C_BANK_ID = BP.C_BANK_ID"
			+" AND BP.C_BPARTNER_ID=CBP.C_BPARTNER_ID"
			+" AND BT.XX_VCN_BANKTRANSFER_ID = BTP.XX_VCN_BANKTRANSFER_ID"
			+" AND BTP.C_PAYMENT_ID = P.C_PAYMENT_ID"
			+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID();
			PreparedStatement prst = DB.prepareStatement(sql,null);
		   	ResultSet rs = null;
		   	//EJECUTO LA SENTENCIA SQL
			   	try {
			   		rs = prst.executeQuery();
			   		while (rs.next()){

			   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
			   			monto = monto.setScale(2, RoundingMode.CEILING);
			   			String beneficiario = rs.getString("BENEFICIARIO");
			   			String codigo= rs.getString("CODIGO");
			   			String cuenta= rs.getString("CUENTA");
			   			String tipoPersona= rs.getString("TIPOPERSONA");
			   			String cedula= rs.getString("CEDULA");
			   			
			   			validarSegVenezolanoCredito(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,aux);
			   						   			
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

//--------------------------OTRAS LINEAS EXTERIOR-------------------------------
	private void otrasLineasExteriorSeleccion(X_XX_VCN_BankTransfer aux) {
		// TODO Auto-generated method stub
		String sql=
				"SELECT CBP.NAME BENEFICIARIO, CPS.TOTALAMT MONTO, CB.VALUE CODIGO, CBPBA.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA, CBP.XX_EMAIL EMAIL"
				+" FROM C_BPARTNER CBP, C_INVOICE CI, C_PAYSELECTION CPS, C_PAYSELECTIONLINE CPSL, XX_VCN_BANKTRANSFERDETAILS BTDS, XX_VCN_BANKTRANSFER BT, C_BANK CB, C_BP_BANKACCOUNT CBPBA"
				+" WHERE "
				+" CPS.C_PAYSELECTION_ID=CPSL.C_PAYSELECTION_ID"
				+" AND CPSL.C_INVOICE_ID=CI.C_INVOICE_ID"
				+" AND CBP.C_BPARTNER_ID=CI.C_BPARTNER_ID"
				+" AND CPS.C_PAYSELECTION_ID=BTDS.C_PAYSELECTION_ID"
				+" AND BT.XX_VCN_BANKTRANSFER_ID=BTDS.XX_VCN_BANKTRANSFER_ID"
				+" AND CB.C_BANK_ID=CBPBA.C_BANK_ID"
				+" AND CBP.C_BPARTNER_ID=CBPBA.C_BPARTNER_ID"
				+" AND CBPBA.XX_ISPRIMARY='Y'"
				+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID()
				+" GROUP BY CBP.NAME, CPS.TOTALAMT, CB.VALUE,CBPBA.ACCOUNTNO,CBP.XX_TYPEPERSON, CBP.XX_CI_RIF, CBP.XX_EMAIL";
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		while (rs.next()){

		   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
		   			monto = monto.setScale(2, RoundingMode.CEILING);
		   			String beneficiario = rs.getString("BENEFICIARIO");
		   			String codigo= rs.getString("CODIGO");
		   			String cuenta= rs.getString("CUENTA");
		   			String tipoPersona= rs.getString("TIPOPERSONA");
		   			String cedula= rs.getString("CEDULA");
		   			String email= rs.getString("EMAIL");
		   			
		   			validarSegExterior(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,email);
		   						   			
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

	public void otrasLineasExteriorAnticipo(X_XX_VCN_BankTransfer aux){
				String sql= 
				"SELECT  P.PAYAMT MONTO, CBP.NAME BENEFICIARIO, B.VALUE CODIGO, BP.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA, BTP.XX_VCN_BANKTRANSFERDETAILP_ID DETALLE, BT.XX_VCN_BANKTRANSFER_ID TRANSFERENCIA, CBP.XX_EMAIL EMAIL"
				+" FROM C_PAYMENT P, C_BPARTNER CBP, C_BANK B, C_BP_BANKACCOUNT BP, XX_VCN_BANKTRANSFER BT, XX_VCN_BANKTRANSFERDETAILP BTP"
				+" WHERE P.C_BPARTNER_ID = CBP.C_BPARTNER_ID"
				+" AND B.C_BANK_ID = BP.C_BANK_ID"
				+" AND BP.C_BPARTNER_ID=CBP.C_BPARTNER_ID"
				+" AND BT.XX_VCN_BANKTRANSFER_ID = BTP.XX_VCN_BANKTRANSFER_ID"
				+" AND BTP.C_PAYMENT_ID = P.C_PAYMENT_ID"
				+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID();
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		while (rs.next()){

				   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
				   			monto = monto.setScale(2, RoundingMode.CEILING);
				   			String beneficiario = rs.getString("BENEFICIARIO");
				   			String codigo= rs.getString("CODIGO");
				   			String cuenta= rs.getString("CUENTA");
				   			String tipoPersona= rs.getString("TIPOPERSONA");
				   			String cedula= rs.getString("CEDULA");
				   			String email= rs.getString("EMAIL");
				   			
				   			validarSegExterior(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,email);
				   						   			
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
//--------------------------OTRAS LINEAS MERCANTIL-------------------------------	
	private void otrasLineasMercantilSeleccion(X_XX_VCN_BankTransfer aux) {
		// TODO Auto-generated method stub
		String sql=
				"SELECT CBP.NAME BENEFICIARIO, CPS.TOTALAMT MONTO, CB.VALUE CODIGO, CBPBA.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA, BTDS.XX_VCN_BANKTRANSFERDETAILS_ID DETALLE"
				+" FROM C_BPARTNER CBP, C_INVOICE CI, C_PAYSELECTION CPS, C_PAYSELECTIONLINE CPSL, XX_VCN_BANKTRANSFERDETAILS BTDS, XX_VCN_BANKTRANSFER BT, C_BANK CB, C_BP_BANKACCOUNT CBPBA"
				+" WHERE "
				+" CPS.C_PAYSELECTION_ID=CPSL.C_PAYSELECTION_ID"
				+" AND CPSL.C_INVOICE_ID=CI.C_INVOICE_ID"
				+" AND CBP.C_BPARTNER_ID=CI.C_BPARTNER_ID"
				+" AND CPS.C_PAYSELECTION_ID=BTDS.C_PAYSELECTION_ID"
				+" AND BT.XX_VCN_BANKTRANSFER_ID=BTDS.XX_VCN_BANKTRANSFER_ID"
				+" AND CB.C_BANK_ID=CBPBA.C_BANK_ID"
				+" AND CBP.C_BPARTNER_ID=CBPBA.C_BPARTNER_ID"
				+" AND CBPBA.XX_ISPRIMARY='Y'"
				+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID()
				+" GROUP BY CBP.NAME, CPS.TOTALAMT, CB.VALUE,CBPBA.ACCOUNTNO,CBP.XX_TYPEPERSON, CBP.XX_CI_RIF,BTDS.XX_VCN_BANKTRANSFERDETAILS_ID";
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		while (rs.next()){

		   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
		   			monto = monto.setScale(2, RoundingMode.CEILING);
		   			String beneficiario = rs.getString("BENEFICIARIO");
		   			String codigo= rs.getString("CODIGO");
		   			String cuenta= rs.getString("CUENTA");
		   			String tipoPersona= rs.getString("TIPOPERSONA");
		   			String cedula= rs.getString("CEDULA");
		   			String detalle= Integer.toString(rs.getInt("DETALLE"));
		   			
		   			validarSegMercantil(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,detalle);
		   						   			
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

	private void otrasLineasMercantilAnticipo(X_XX_VCN_BankTransfer aux) {
		// TODO Auto-generated method stub
		String sql= 
		"SELECT  P.PAYAMT MONTO, CBP.NAME BENEFICIARIO, B.VALUE CODIGO, BP.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA, BTP.XX_VCN_BANKTRANSFERDETAILP_ID DETALLE, BT.XX_VCN_BANKTRANSFER_ID TRANSFERENCIA"
		+" FROM C_PAYMENT P, C_BPARTNER CBP, C_BANK B, C_BP_BANKACCOUNT BP, XX_VCN_BANKTRANSFER BT, XX_VCN_BANKTRANSFERDETAILP BTP"
		+" WHERE P.C_BPARTNER_ID = CBP.C_BPARTNER_ID"
		+" AND B.C_BANK_ID = BP.C_BANK_ID"
		+" AND BP.C_BPARTNER_ID=CBP.C_BPARTNER_ID"
		+" AND BT.XX_VCN_BANKTRANSFER_ID = BTP.XX_VCN_BANKTRANSFER_ID"
		+" AND BTP.C_PAYMENT_ID = P.C_PAYMENT_ID"
		+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID();
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		while (rs.next()){

		   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
		   			monto = monto.setScale(2, RoundingMode.CEILING);
		   			String beneficiario = rs.getString("BENEFICIARIO");
		   			String codigo= rs.getString("CODIGO");
		   			String cuenta= rs.getString("CUENTA");
		   			String tipoPersona= rs.getString("TIPOPERSONA");
		   			String cedula= rs.getString("CEDULA");
		   			String detalle= Integer.toString(rs.getInt("DETALLE"));
		   			
		   			validarSegMercantil(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,detalle);
		   						   			
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
	
	//--------------------------OTRAS LINEAS BANESCO-------------------------------

	private void otrasLineasBanescoSeleccion(X_XX_VCN_BankTransfer aux) {
		// TODO Auto-generated method stub
		String sql=
				"SELECT CBP.NAME BENEFICIARIO, CPS.TOTALAMT MONTO, CB.VALUE CODIGO, CBPBA.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA, BTDS.XX_VCN_BANKTRANSFERDETAILS_ID DETALLE"
				+" FROM C_BPARTNER CBP, C_INVOICE CI, C_PAYSELECTION CPS, C_PAYSELECTIONLINE CPSL, XX_VCN_BANKTRANSFERDETAILS BTDS, XX_VCN_BANKTRANSFER BT, C_BANK CB, C_BP_BANKACCOUNT CBPBA"
				+" WHERE "
				+" CPS.C_PAYSELECTION_ID=CPSL.C_PAYSELECTION_ID"
				+" AND CPSL.C_INVOICE_ID=CI.C_INVOICE_ID"
				+" AND CBP.C_BPARTNER_ID=CI.C_BPARTNER_ID"
				+" AND CPS.C_PAYSELECTION_ID=BTDS.C_PAYSELECTION_ID"
				+" AND BT.XX_VCN_BANKTRANSFER_ID=BTDS.XX_VCN_BANKTRANSFER_ID"
				+" AND CB.C_BANK_ID=CBPBA.C_BANK_ID"
				+" AND CBP.C_BPARTNER_ID=CBPBA.C_BPARTNER_ID"
				+" AND CBPBA.XX_ISPRIMARY='Y'"
				+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID()
				+" GROUP BY CBP.NAME, CPS.TOTALAMT, CB.VALUE,CBPBA.ACCOUNTNO,CBP.XX_TYPEPERSON, CBP.XX_CI_RIF,BTDS.XX_VCN_BANKTRANSFERDETAILS_ID";
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		while (rs.next()){

		   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
		   			monto = monto.setScale(2, RoundingMode.CEILING);
		   			String beneficiario = rs.getString("BENEFICIARIO");
		   			String codigo= rs.getString("CODIGO");
		   			String cuenta= rs.getString("CUENTA");
		   			String tipoPersona= rs.getString("TIPOPERSONA");
		   			String cedula= rs.getString("CEDULA");
		   			String detalle= Integer.toString(rs.getInt("DETALLE"));
		   			
		   			validarSegBanesco(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,detalle);
		   						   			
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
	
	private void otrasLineasBanescoAnticipo(X_XX_VCN_BankTransfer aux) {
		// TODO Auto-generated method stub
		String sql= 
		"SELECT  P.PAYAMT MONTO, CBP.NAME BENEFICIARIO, B.VALUE CODIGO, BP.ACCOUNTNO CUENTA, CBP.XX_TYPEPERSON TIPOPERSONA, CBP.XX_CI_RIF CEDULA, BTP.XX_VCN_BANKTRANSFERDETAILP_ID DETALLE, BT.XX_VCN_BANKTRANSFER_ID TRANSFERENCIA"
		+" FROM C_PAYMENT P, C_BPARTNER CBP, C_BANK B, C_BP_BANKACCOUNT BP, XX_VCN_BANKTRANSFER BT, XX_VCN_BANKTRANSFERDETAILP BTP"
		+" WHERE P.C_BPARTNER_ID = CBP.C_BPARTNER_ID"
		+" AND B.C_BANK_ID = BP.C_BANK_ID"
		+" AND BP.C_BPARTNER_ID=CBP.C_BPARTNER_ID"
		+" AND BT.XX_VCN_BANKTRANSFER_ID = BTP.XX_VCN_BANKTRANSFER_ID"
		+" AND BTP.C_PAYMENT_ID = P.C_PAYMENT_ID"
		+" AND BT.XX_VCN_BANKTRANSFER_ID = "+aux.getXX_VCN_BankTransfer_ID();
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		while (rs.next()){

		   			java.math.BigDecimal monto = rs.getBigDecimal("MONTO");
		   			monto = monto.setScale(2, RoundingMode.CEILING);
		   			String beneficiario = rs.getString("BENEFICIARIO");
		   			String codigo= rs.getString("CODIGO");
		   			String cuenta= rs.getString("CUENTA");
		   			String tipoPersona= rs.getString("TIPOPERSONA");
		   			String cedula= rs.getString("CEDULA");
		   			String detalle= Integer.toString(rs.getInt("DETALLE"));
		   			
		   			validarSegBanesco(monto,beneficiario,codigo,cuenta,tipoPersona,cedula,detalle);
		   						   			
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
	
	//----------------------VALIDAR Provincial---------------------------
	
			private void validarSegProvincial(BigDecimal monto, String beneficiario,
					String codigo, String cuenta, String tipoPersona, String cedula, X_XX_VCN_BankTransfer aux) {
				// TODO Auto-generated method stub
				
				//TIPO DE IDENTIFICACION
				String uno=tipoPersona;
					if(uno.length() != 1){ 
			        	Error = true;
			        	mjsError = mjsError + "El tipo de persona debe tener 1 caracter";
			        }
				//NUMERO DE IDENTIFICACION
				String dos= cedula;
					if(dos.length() > 15){ 
			        	Error = true;
			        	mjsError = mjsError + "El rif debe tener menos de 15 caracteres";
			        }
					
					dos=uno+dos;
					while(dos.length()<16) dos="0"+dos;
					
				//Nombre del Beneficiario
				beneficiario= BuscarRelation(beneficiario);
					if (beneficiario.length()>35) beneficiario=beneficiario.substring(0,34);
					String tres= quitarcaracteresespeciales(beneficiario,35);
					while(tres.length()<35) tres= tres+" ";

					
				//Nro. Cuenta del Beneficiario
					String cuatro= cuenta;
					if(cuatro.length() != 20){ 
			        	Error = true;
			        	mjsError = mjsError + "El numero de cuenta debe tener 20 caracteres";
			        }
					
				//Número de Instrucción
					String cinco="02";

				//Monto Neto a Pagar
					String montofinal= monto.toString();
					DecimalFormat formateador = new DecimalFormat("#.00");
					montofinal=formateador.format(Double.parseDouble(montofinal));

					String parteEntera= montofinal.substring(0,montofinal.length()-3);
					String parteDecimal= montofinal.substring(montofinal.length()-2,montofinal.length());
					while(parteEntera.length()<13) parteEntera= "0"+parteEntera;
					
					String seis= parteEntera;
					String siete= parteDecimal;
				

				//AGREGO LAS OTRAS LINEAS A LA LISTA
					lineasList.add(dos+tres+cuatro+cinco+seis+siete);
			}

	
	//----------------------VALIDAR VENEZOLANO CREDITO---------------------------
	
		private void validarSegVenezolanoCredito(BigDecimal monto, String beneficiario,
				String codigo, String cuenta, String tipoPersona, String cedula, X_XX_VCN_BankTransfer aux) {
			// TODO Auto-generated method stub
			
			//CODIGO POR COMPAÑIA
				String uno="";
				if(codigoempresa == null){
					uno = codigoCompañia(aux.getC_BankAccount_ID());
					codigoempresa=uno;
				}
				else
					uno=codigoempresa;
			//NRO LOTE
				String dos="";
				if(numerolote==null){
					Date fecha = new Date();
					SimpleDateFormat formato = new SimpleDateFormat("yyMMddmm");
					dos = formato.format(fecha);
					numerolote=dos;
				}
				else
					dos=numerolote;
			//NRO DEL PAGO
				String tres= Integer.toString(aux.getXX_VCN_BankTransfer_ID());
				while(tres.length()<8) tres= "0"+tres;
			//INTERNO DEL BVC
				String cuatro= "00000000";
			//Tipo de Registro
				String cinco="5";
			//Monto Neto a Pagar
				String montofinal= monto.toString();
				DecimalFormat formateador = new DecimalFormat("#.00");
				montofinal=formateador.format(Double.parseDouble(montofinal));

				
				String parteEntera= montofinal.substring(0,montofinal.length()-3);
				String parteDecimal= montofinal.substring(montofinal.length()-2,montofinal.length());
				while(parteEntera.length()<13) parteEntera= "0"+parteEntera;
				String seis= parteEntera+parteDecimal;
			//Nombre del Beneficiario
				beneficiario= BuscarRelation(beneficiario);
				String siete= quitarcaracteresespeciales(beneficiario,60);
				while(siete.length()<60) siete= siete+" ";
			//Tipo de Pago
				String ocho="1";
			//FILLER
				String nueve="                 ";
			//Codigo de Oficina del Banco
				String diez="   ";
			//FILLER
				String once="    ";
			//Nro. Cuenta del Beneficiario
				String doce= cuenta;
				if(doce.length() != 20){ 
		        	Error = true;
		        	mjsError = mjsError + "El numero de cuenta debe tener 20 caracteres";
		        }
				while(doce.length()<35) doce= doce + " ";
			//Codigo de Moneda - Cta. Debito
				String trece="VEF";
			//Codigo de Moneda - Cta. Credito
				String catorce="VEF";
			//FILLER
				String quince="                            ";
			//Concepto del Pago
				String dieciseis="                                   ";
			//Concepto del Pago
				String diecisiete="                                   ";
			//Concepto del Pago
				String dieciocho="                                   ";
			//FILLER
				String diecinueve="      ";
			//Codigo del Banco del Beneficiario
				String veinte=codigo;
				while(veinte.length()<12) veinte= veinte+" ";
			//Tipo de Codigo de Banco
				String veintiuno="BIC";
			//Nombre del Banco Beneficiario
				String veintidos="                                   ";
			//Direccion 1 del banco Beneficiario
				String veintitres="                                   ";
			//Direccion 2 del banco Beneficiario
				String veinticuatro="                                   ";
			//FILLER
				String veinticinco="                           ";
			//Actividad Economica
				String veintiseis="  ";
			//Motivo de la Operación
				String veintisiete="   ";
			//Cuenta del debito
				String veintiocho=numeroDeCuenta(aux.getC_BankAccount_ID());
				while(veintiocho.length()<35) veintiocho= veintiocho+" ";
			//FILLER
				String veintinueve="    ";
			//VALOR FIJO
				String treinta="1";
			//LINEA COMPLETA
			String otraLinea=uno+dos+tres+cuatro+cinco+seis+siete+ocho+nueve+diez+once+doce+trece+catorce+quince+dieciseis+diecisiete;
			otraLinea= otraLinea+dieciocho+diecinueve+veinte+veintiuno+veintidos+veintitres+veinticuatro+veinticinco;
			otraLinea= otraLinea+veintiseis+veintisiete+veintiocho+veintinueve+treinta;
			
			//AGREGO LAS OTRAS LINEAS A LA LISTA
			lineasList.add(otraLinea);
		}

		private void ultimalineaVenezolanoCredito(String codigo, String lote, X_XX_VCN_BankTransfer aux){
			
			//Codigo de compañía
			String uno=codigo;
			//Nro. del Lote
			String dos=lote;
			//INTERNO DEL BVC
			String tres="0000000000000000";
			//Tipo de Registro
			String cuatro="9";
			//Fecha Valor del Lote
			Date fecha = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");
			String cinco= formato.format(fecha);			
			//Monto Total Expresado en la Moneda del Credito
			BigDecimal big = aux.getXX_VCN_Amount();
			DecimalFormat formateador = new DecimalFormat("#.00");
			String seis =formateador.format(big);

			int tamano= seis.length();
			if(seis.charAt(tamano-2)== '.') seis=seis+0;
			String parteEntera= seis.substring(0,seis.length()-3);
				while(parteEntera.length()<13)
					parteEntera= "0"+parteEntera;
			String parteDecimal = seis.substring(seis.length()-2,seis.length());
			seis= parteEntera+parteDecimal;
			//Nro. de Pagos del Lote
			String siete=cantidadPagos(aux.getXX_VCN_BankTransfer_ID());
			while(siete.length()<15) siete="0"+siete;
			//Codigo de Moneda - Cta. Credito
			String ocho="VEF";
			//Cuenta del debito
			String nueve=numeroDeCuenta(aux.getC_BankAccount_ID());
			if(nueve.length() != 20){ 
	        	Error = true;
	        	mjsError = mjsError + "El numero de cuenta a debitar debe tener 20 caracteres";
	        }
			while(nueve.length() < 35) nueve= nueve+" ";
			//FILLER
			String diez="00000000000000000000000000000000000";
			//Monto Total Expresado en la Moneda del Debito
			String once=seis;
			//Codigo de Moneda - Cta. Debito
			String doce="VEF";
			//FILLER
			String trece="000000000000000000000000000000";
			//Actividad Economica
			String catorce="  ";
			//Motivo de la Operación
			String quince="   ";
			//FILLER
			String dieciseis="                                                                                                                                                                                                                                                                                                                   ";
			//VALOR FIJO
			String diecisiete="X";
			
			String ultimaLinea=uno+dos+tres+cuatro+cinco+seis+siete+ocho+nueve+diez+once+doce+trece+catorce+quince+dieciseis+diecisiete;
			lineasList.add(ultimaLinea);
			
		}

	//--------------------------VALIDAR MERCANTIL----------------------------------
	private void validarSegMercantil(BigDecimal monto, String beneficiario,
			String codigo, String cuenta, String tipoPersona, String cedula, String detalle) {
		// TODO Auto-generated method stub
		//TIPO REGISTRO
		String uno="2";
		//TIPO DE IDENTIFICACION
		String dos=tipoPersona;
			if(dos.length() != 1){ 
	        	Error = true;
	        	mjsError = mjsError + "El tipo de identificacion debe tener 1 caractere";
	        }
		//NUMERO DE IDENTIFICACION
		String tres= cedula;
			if(tres.length() > 15){ 
	        	Error = true;
	        	mjsError = mjsError + "El numero de identificacion debe tener menos de 15 caracteres";
	        }
			while(tres.length()<15) tres= "0"+tres;
		//FORMA DE PAGO
		String cuatro="3";
		//AREA RESERVADA
		String cinco= "000000000000";
		String seis= "               ";
		String siete= "000000000000000";
		//CODIGO CUENTA CLIENTE
		String ocho= cuenta;
			if(ocho.length() != 20){ 
	        	Error = true;
	        	mjsError = mjsError + "El numero de cuenta debe tener 20 caracteres";
	        }
		//MONTO OPERACION	
		String montofinal= monto.toString();
		DecimalFormat formateador = new DecimalFormat("#.00");
		montofinal=formateador.format(Double.parseDouble(montofinal));

		
			String parteEntera= montofinal.substring(0,montofinal.length()-3);
			String parteDecimal= montofinal.substring(montofinal.length()-2,montofinal.length());
					while(parteEntera.length()<15)
						parteEntera= "0"+parteEntera;
		String nueve= parteEntera+parteDecimal;
		//IDENTIFICACION CLIENTE EMPRESA
		String diez= cedula;
		if(diez.length() > 16){ 
        	Error = true;
        	mjsError = mjsError + "El numero de identificacion debe tener menos de 15 caracteres";
        }
		while(diez.length()<16) diez= "0"+diez;
		//TIPO PAGO
		String once="0000000062";
		//AREA RESERVADA
		String doce="000";
		//NOMBRE DEL PROVEEDOR
		beneficiario= BuscarRelation(beneficiario);
		String trece= quitarcaracteresespeciales(beneficiario,60);
			while(trece.length()<60) trece= trece+" ";
		//AREARESERVADA
		String catorce="0000000";
		//NUMERO DE RECIBO
		String quince= detalle;
			while(quince.length()<8) quince= "0"+quince;
		//EMAIL
		String deiciseis="                                                  ";
		//CODIGO RESPUESTA
		String diecisiete="0000";
		//MENSAJE RESPUESTA
		String dieciocho="                              ";
		//CONCEPTO PAGO
		String diecinueve="                                                                                ";
		//AREA RESERVADA
		String veinte="00000000000000000000000000000000000";
		
		//LINEA COMPLETA
		String otraLinea=uno+dos+tres+cuatro+cinco+seis+siete+ocho+nueve+diez+once+doce+trece+catorce+quince+deiciseis+diecisiete;
		otraLinea= otraLinea+dieciocho+diecinueve+veinte;
		
		//AGREGO LAS OTRAS LINEAS A LA LISTA
		lineasList.add(otraLinea);
	}

	//--------------------------VALIDAR EXTERIOR----------------------------------	
	private void validarSegExterior(BigDecimal monto, String beneficiario,
			String codigo, String cuenta, String tipoPersona, String cedula, String email) {
		// TODO Auto-generated method stub
		
		//VALIDAR QUE EL BENEFICIARIO NO TENGA CARACTERES ESPECIALES
		beneficiario= BuscarRelation(beneficiario);
		String uno = quitarcaracteresespeciales(beneficiario,60);
		
		//MONTO
		String montofinal= monto.toString();
		DecimalFormat formateador = new DecimalFormat("#.00");
		montofinal=formateador.format(Double.parseDouble(montofinal));

			String parteEntera= montofinal.substring(0,montofinal.length()-3);
			String parteDecimal= montofinal.substring(montofinal.length()-2,montofinal.length());
				while(parteEntera.length()<10)
					parteEntera= "0"+parteEntera;
		String dos= parteEntera+parteDecimal;
		
		//DETALLE DEL PAGO
		String tres ="Pago por Transferencia                                                                                                  ";
		
		//CODIGO DEL BANCO
		if(codigo.length() != 3){ 
        	Error = true;
        	mjsError = mjsError + "El codigo del banco no tiene 3 caracteres";
        }
		String cuatro=codigo;
		
		//CUENTA A ABONAR
		if(cuenta.length() != 20){
        	Error = true;
        	mjsError = mjsError + "La cuenta a abonar no tiene 20 caracteres";
		}
		String cinco=cuenta;
		
		//CORREO DEL BENEFICIARIO
		String seis="";
		if(email == null){
			seis="                                                            ";
		}
		else{
			while (email.length()< 60) email=email+" ";
			seis=email;
		}
		//REFERENCIA
		String siete="00000000";
		
		//CEDULA RIF
		if(cedula.length() >9){
        	Error = true;
        	mjsError = mjsError + "La cedula o rif tiene mas de 9 caracteres";
		}
			while(cedula.length()<9)
			cedula="0"+cedula;
		String ocho= tipoPersona+cedula;
		
		String otraLinea= uno+dos+tres+cuatro+cinco+seis+siete+ocho;
		//AGREGO LAS OTRAS LINEAS A LA LISTA
		lineasList.add(otraLinea);
	}

	//--------------------------VALIDAR BANESCO----------------------------------
	private void validarSegBanesco(BigDecimal monto, String beneficiario,
			String codigo, String cuenta, String tipoPersona, String cedula,
			String detalle) {
		// TODO Auto-generated method stub

		//TIPO DE REGISTRO
		String uno = "03";
		//NUMERO DE RECIBO
		String dos = detalle;
				if(dos.length()> 8){ 
		        	Error = true;
		        	mjsError = mjsError + "El numero de recibo que es el id del detalle debe tener maximo 8 caracteres";
		        }
				while(dos.length()< 8) dos= dos+"0";
				while(dos.length()< 30) dos= dos+" ";
		//MONTO A COBRAR
		String montofinal= monto.toString();
		DecimalFormat formateador = new DecimalFormat("#.00");
		montofinal=formateador.format(Double.parseDouble(montofinal));

		String parteEntera= montofinal.substring(0,montofinal.length()-3);
		String parteDecimal= montofinal.substring(montofinal.length()-2,montofinal.length());
					while(parteEntera.length()<13)
						parteEntera= "0"+parteEntera;
		String tres= parteEntera+parteDecimal;		
		//MONEDA
		String cuatro = "VEF";
		//NUMERO CUENTA CLIENTE
		if(cuenta.length() != 20){
        	Error = true;
        	mjsError = mjsError + "La cuenta a abonar no tiene 20 caracteres";
		}
			while(cuenta.length()<30) cuenta= cuenta+" ";
		String cinco=cuenta;
		//BANCO
				codigo = "0"+codigo;
				if(codigo.length() > 11){
		        	Error = true;
		        	mjsError = mjsError + "El codigo del banco debe tener menos de 11 caracteres";
				}
				while(codigo.length()<11) codigo= codigo+" ";
		String seis = codigo;		
		//Código de Agencia
		String siete="   ";
		//RIF O CEDULA
		if(tipoPersona.length() > 1){
        	Error = true;
        	mjsError = mjsError + "El tipo de persona debe tener 1 caracter";
		}
		if(cedula.length() > 9){
        	Error = true;
        	mjsError = mjsError + "La cedula o rif debe tener maximo 9 caracteres";
		}
		while(cedula.length()<16) cedula= cedula+" ";
		String ocho = tipoPersona+cedula;
		//NOMBRE DEL CLIENTE
		beneficiario= BuscarRelation(beneficiario);
		String nueve = quitarcaracteresespeciales(beneficiario,70);
		
			if(nueve.length() > 70){
				nueve=nueve.substring(0, 70);
				//Error = true;
	        	mjsError = mjsError + "El nombre del cliente debe tener maximo 70 caracteres, se recorto el nombre";
			}
		//CAMPO LIBRE
		String diez = "  ";
		while(diez.length() < 201)
			diez=diez+" ";

		//Forma de Pago (Cod.)
		String once = "425";

		String otraLinea= uno+dos+tres+cuatro+cinco+seis+siete+ocho+nueve+diez+once;
		//AGREGO LAS OTRAS LINEAS A LA LISTA
		lineasList.add(otraLinea);
		
	}
	
	
//----------------------FUNCIONES O OPERACIONES--------------------------------------
	private String quitarcaracteresespeciales(String beneficiario,int limite) {
		// TODO Auto-generated method stub
		
		if(beneficiario.startsWith(" "))
			beneficiario=beneficiario.substring(1, beneficiario.length());
		
		int c= beneficiario.length(); 
        for (int contador =0; contador<c; contador++){ 
            if (beneficiario.charAt(contador)=='ñ'){ 
            	beneficiario=beneficiario.replace('ñ','n'); } 
            if (beneficiario.charAt(contador)=='á'){ 
            	beneficiario=beneficiario.replace('á','a'); } 
            if (beneficiario.charAt(contador)=='é'){ 
            	beneficiario=beneficiario.replace('é','e'); } 
            if (beneficiario.charAt(contador)=='í'){ 
            	beneficiario=beneficiario.replace('í','i'); } 
            if (beneficiario.charAt(contador)=='ó'){ 
            	beneficiario=beneficiario.replace('ó','o'); } 
            if (beneficiario.charAt(contador)=='ú'){ 
            	beneficiario=beneficiario.replace('ú','u'); } 
            if (beneficiario.charAt(contador)=='Á'){ 
            	beneficiario=beneficiario.replace('Á','A'); } 
            if (beneficiario.charAt(contador)=='É'){ 
            	beneficiario=beneficiario.replace('É','E'); } 
            if (beneficiario.charAt(contador)=='Í'){ 
            	beneficiario=beneficiario.replace('Í','I'); } 
            if (beneficiario.charAt(contador)=='Ó'){ 
            	beneficiario=beneficiario.replace('Ó','O'); } 
            if (beneficiario.charAt(contador)=='Ú'){ 
            	beneficiario=beneficiario.replace('Ú','U'); } 
            if (beneficiario.charAt(contador)=='-'){ 
            	beneficiario=beneficiario.replace('-',' '); } 
            if (beneficiario.charAt(contador)=='.'){ 
            	beneficiario=beneficiario.replace('.',' '); } 
            if (beneficiario.charAt(contador)==','){ 
            	beneficiario=beneficiario.replace(',',' '); } 
            if (beneficiario.charAt(contador)==';'){ 
            	beneficiario=beneficiario.replace(';',' '); }
            if (beneficiario.charAt(contador)=='-'){ 
            	beneficiario=beneficiario.replace('-',' '); } 

        } 
        if(beneficiario.length()> limite){ 
        	beneficiario=beneficiario.substring(0, limite);
        	//Error = true;
        	mjsError = mjsError + "El nombre del Beneficiario tiene mas de "+limite+" caracteres, se recorto";
        }
        
        while(beneficiario.length() < limite)
        	beneficiario= beneficiario + " ";
        
        
		return beneficiario;
	}

	public String buscarRifCompañia (int id_Client){
		
		String rif="";
		
		String sql= "SELECT XX_CI_RIF"
				+" FROM AD_CLIENT"
				+" WHERE AD_CLIENT_ID = "+ id_Client;
		
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		if (rs.next()){
				   			rif = rs.getString("XX_CI_RIF");
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
		
		return rif;
	}
	
	public String buscarNombreCompañia (int id_Client){
		
		String rif="";
		
		String sql= "SELECT NAME"
				+" FROM AD_CLIENT"
				+" WHERE AD_CLIENT_ID = "+ id_Client;
		
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		if (rs.next()){
				   			rif = rs.getString("NAME");
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
		quitarcaracteresespeciales(rif,rif.length());
		return rif;
	}
	
	
	 public String quitarGuionDerecha(String Nombre1, int limite){ 
	        int c= Nombre1.length(); 
	         
	          for (int contador =0; contador<Nombre1.length(); contador++){ 
	        	  
	        	  
	            if (Nombre1.charAt(contador)=='-'){ 
	            	String antes = Nombre1.substring(0,contador);
	                String despues = Nombre1.substring(contador+1,Nombre1.length());
	                Nombre1= antes+despues;
 	            } 
	        }
	        //QUITO LA PRIMERA LETRA  
	        Nombre1= Nombre1.substring(1,Nombre1.length());
	        
	        //ASIGNO LOS 0 JUSTIFICADOS A LA DERECHA
	        if(Nombre1.length()> 9){ 
	        	Error = true;
	        	mjsError = mjsError + "El rif tiene mas de 9 caracteres";
	        }
	        else{
		        while(Nombre1.length()< limite)
		        Nombre1= Nombre1+"0";
	        }
	 return Nombre1;
	 }
	 
	 public String quitarGuionIzquierda(String Nombre1, int limite){ 
	        int c= Nombre1.length(); 
	         
	          for (int contador =0; contador<Nombre1.length(); contador++){ 
	        	  
	        	  
	            if (Nombre1.charAt(contador)=='-'){ 
	            	String antes = Nombre1.substring(0,contador);
	                String despues = Nombre1.substring(contador+1,Nombre1.length());
	                Nombre1= antes+despues;
	            } 
	        }
	        //QUITO LA PRIMERA LETRA  
	        Nombre1= Nombre1.substring(1,Nombre1.length());
	        
	        //ASIGNO LOS 0 JUSTIFICADOS A LA DERECHA
	        if(Nombre1.length()> 9){ 
	        	Error = true;
	        	mjsError = mjsError + "El rif tiene mas de 9 caracteres";
	        }
	        else{
		        while(Nombre1.length()< limite)
		        Nombre1= "0"+Nombre1;
	        }
	 return Nombre1;
	 }
	 public String numeroDeCuenta(int cuenta){
		 
		 String numCuenta="";
		 
		 String sql= "SELECT ACCOUNTNO"
					+" FROM C_BANKACCOUNT"
					+" WHERE C_BANKACCOUNT_ID = "+ cuenta;
		 						
					PreparedStatement prst = DB.prepareStatement(sql,null);
				   	ResultSet rs = null;
				   	//EJECUTO LA SENTENCIA SQL
					   	try {
					   		rs = prst.executeQuery();
					   		if (rs.next()){
					   			numCuenta = rs.getString("ACCOUNTNO");
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
		 
		 
		   if(numCuenta.length() != 20){ 
	        	Error = true;
	        	mjsError = mjsError + "El numero de cuenta debe tener 20 caracteres";
	        }
		   return numCuenta;
	 }

	 
	 public String cantidadPagos(int transferencia){
		 
		 int anticipo=0;
		 //BUSCO LOS ANTICIPOS
		 String sql= "SELECT COUNT(*) AS ANTICIPO"
					+" FROM XX_VCN_BANKTRANSFERDETAILP"
					+" WHERE XX_VCN_BANKTRANSFER_ID = "+ transferencia;
		 			
					PreparedStatement prst = DB.prepareStatement(sql,null);
				   	ResultSet rs = null;
				   	//EJECUTO LA SENTENCIA SQL
					   	try {
					   		rs = prst.executeQuery();
					   		if (rs.next()){
					   			anticipo = rs.getInt("ANTICIPO");
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
					   	
					   	int seleccion=0;
					  //BUSCO LAS SELECCIONES
						 String sql1= "SELECT COUNT(*) AS SELECCION"
									+" FROM XX_VCN_BANKTRANSFERDETAILS"
									+" WHERE XX_VCN_BANKTRANSFER_ID = "+ transferencia;
						 			
									PreparedStatement prst1 = DB.prepareStatement(sql1,null);
								   	ResultSet rs1 = null;
								   	//EJECUTO LA SENTENCIA SQL
									   	try {
									   		rs1 = prst1.executeQuery();
									   		if (rs1.next()){
									   			seleccion = rs1.getInt("SELECCION");
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
									   	
									   	int total= anticipo + seleccion;
									   	if(total == 0 ){ 
								        	Error = true;
								        	mjsError = mjsError + "No se puede hacer una transferencia de 0 pagos";
								        }
									   	if(total > 9999 ){ 
								        	Error = true;
								        	mjsError = mjsError + "No se puede hacer una transferencia de mas de 9999 pagos";
								        }

				   	String totalpago= Integer.toString(total);
					while(totalpago.length() < 4)
						totalpago= "0"+totalpago;
		 
		return totalpago;
	 }
	 
	 /*
	  *METODO QUE SE ENCARGA DE BUSCAR LA RELACION DE UN SOCIO DE NEGOCIO 
	  */
	 private String BuscarRelation(String nombre){
	 	String beneficiario="";
	 	String sql=
	 			"SELECT CBPR.NAME AS NAME"
	 			+" FROM C_BPARTNER CBP, C_BP_RELATION CBPR"
	 			+" WHERE CBP.C_BPARTNER_ID=CBPR.C_BPARTNER_ID"
	 			+" AND CBP.NAME = '"+nombre+" '";
	 	PreparedStatement prst1 = DB.prepareStatement(sql,null);
	    	ResultSet rs1 = null;
	    	//EJECUTO LA SENTENCIA SQL
	 	   	try {
	 	   		rs1 = prst1.executeQuery();
	 	   		if (rs1.next()){
	 	   			beneficiario = rs1.getString("NAME");
	 	   		}// Fin if
	 	   		else{
	 	   			beneficiario=nombre;
	 	   		}
	 	   	} 
	 	   	catch (Exception e){
	 			System.out.println(e);
	 		}
	 	   	finally {
	 	   	//CERRAR CONEXION
	 			DB.closeResultSet(rs1);
	 			DB.closeStatement(prst1);
	 		}
	 	
	 	return beneficiario;
	 }
	 
	 /*
	  *METODO QUE SE ENCARGA DE BUSCAR LA RELACION DE UN SOCIO DE NEGOCIO 
	  */
	 private String codigoCompañia(int Id){
	 	String codigo="";
	 	String sql=
	 			"select XX_VCN_TRANSFERCODE"
	 			+" from C_BANKACCOUNT"
	 			+" where C_BANKACCOUNT_ID = "+Id;
	 	
	 		PreparedStatement prst1 = DB.prepareStatement(sql,null);
	    	ResultSet rs1 = null;
	    	//EJECUTO LA SENTENCIA SQL
	 	   	try {
	 	   		rs1 = prst1.executeQuery();
	 	   		if (rs1.next()){
	 	   			codigo = rs1.getString("XX_VCN_TRANSFERCODE");
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
 	   		if(codigo==null){
	 	   		Error = true;
	        	mjsError = mjsError + "FALTA EL CODIGO DE COMPAÑIA BANCO PROVINCIAL";
	        	codigo="0000";
 	   		}

	 	   	if(codigo.length() != 3){
		 	   	Error = true;
	        	mjsError = mjsError + "EL CODIGO DE COMPAÑIA BANCO PROVINCIAL DEBE TENER 3 CARACTERES";
	 	   	}
	 	
	 	return codigo;
	 }	 
	 
//-------------------------------OPERACIONES DE ARCHIVO-----------------------------------
		private void escribirArchivo(ArrayList<String> lineasList2, String Nombre,boolean Error, String mjsError) {
			// TODO Auto-generated method stub
			 /***Se crea la ruta donde se guardan los archivos .txt si no existe la carpeta***/
		     String ruta = System.getProperty("user.home") + "\\TransferenciasBancarias\\";
		     boolean success;
		     
		     File file=new File(ruta);
		     if (!file.exists()){        
		             success = (new File(ruta)).mkdir();
		     }                
			
			try
			{
				//Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
				File archivo=new File(ruta+Nombre+".txt");
				
				if (archivo.exists()) archivo.delete();

				//Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
				FileWriter escribir=new FileWriter(archivo,true);
				if(Error==true){
					escribir.write(mjsError);
				}
				else{
					//Escribimos en el archivo con el metodo write 
					for(int i = 0;i<lineasList2.size();i++){
			            escribir.write(lineasList2.get(i));escribir.write("\r\n");
					}
				}
				//Cerramos la conexion
				escribir.close();
				}

				//Si existe un problema al escribir cae aqui
				catch(Exception e)
				{
				System.out.println("Error al escribir");
				}
		}

}
