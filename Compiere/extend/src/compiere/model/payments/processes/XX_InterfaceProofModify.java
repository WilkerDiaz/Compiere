package compiere.model.payments.processes;

import java.awt.Container;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.Utilities;
import compiere.model.payments.pmgCx017;


/**
 * Proceso que se encarga de recibir la información del pago, de un documento específico
 * Nombre del proceso en compiere: Interface Proof Modify
 * @author Jessica Mendoza
 *
 */
public class XX_InterfaceProofModify  extends SvrProcess{
	
	private char[][] simbolo = {{'0','}'},
			{'1','J'},
			{'2','K'},
			{'3','L'},
			{'4','M'},
			{'5','N'},
			{'6','O'},
			{'7','P'},
			{'8','Q'},
			{'9','R'}};
	
	private String[][] ESTREG = {{"OK","Operación exitosa, se agregó el registro."},
		 	 {"00","Operación exitosa, se sustituyó el registro."},
			 {"01","Operación exitosa, se eliminó el registro."},
			 {"02","Operación Cancelada, no posee autorización para utilizar esta interfaz."},
			 {"03","Operación Cancelada, la compañía está en cero o no es correcta."},
			 {"04","Operación Cancelada, el número de comprobante está en cero."},
			 {"05","Operación Cancelada, el mes/año es inferior al último mes/año cerrado."},
			 {"06","Operación Cancelada, la descripción del comprobante está en blanco."},
			 {"07","Operación Cancelada, la fecha del comprobante está en cero."},
			 {"08","Operación Cancelada, la cuenta contable del asiento es incorrecta."},
			 {"09","Operación Cancelada, el código de sucursal/oficina no es correcto."},
			 {"10","Operación Cancelada, el código de división no es correcto."},
			 {"11","Operación Cancelada, el código de departamento no es correcto."},
			 {"12","Operación Cancelada, el código de la sección no es correcto."},
			 {"13","Operación Cancelada, el número de auxiliar no es correcto."},
			 {"14","Operación Cancelada, el número de documento está en cero."},
			 {"15","Operación Cancelada, el tipo de documento está en blanco."},
			 {"16","Operación Cancelada, la fecha del documento no es correcta."},
			 {"17","Operación Cancelada, la descripción del asiento está en blanco."},
			 {"18","Operación Cancelada, el monto del debe y el monto del haber está en cero."},
			 {"19","Operación Cancelada, la suma de los asientos del debe y del haber es diferente de cero."},
			 {"20","Operación Cancelada, la cuenta contable no admite fecha de vencimiento."},
			 {"21","Operación Cancelada, la fecha de vencimiento está en cero o no es correcta."},
			 {"22","Operación Cancelada, no se puede acceder al archivo.txt."},
			 {"23","Operación Cancelada, no se puede acceder a la interfaz."},
			 {"24","Operación Cancelada, se ha producido un error al tratar de grabar el comprobante en la interfaz."},
			 {"25","Operación Cancelada, se ha producido un error al tratar de actualizar el comprobante en la interfaz."},
			 {"26","Operación Cancelada, se ha producido un error al tratar de eliminar el comprobante en la interfaz."},
			 {"27","Operación Cancelada, el número de la línea del asiento no es correcto o está repetido."},
			 {"28","Operación Cancelada, el nombre del archivo.txt es incorrecto."},
			 {"29","Operación Cancelada, el tipo de ITF no es válido."},
			 {"30","Operación Cancelada, el valor del impuesto automático no es válido."},
			 {"31","Operación Cancelada, la fecha del comprobante no es válida."},
			 {"32","Operación Cancelada, la cuenta contable no admite tipo de documento."},
			 {"33","Operación Cancelada, la cuenta contable no admite número de documento."},
			 {"34","Operación Cancelada, la cuenta contable no admite fecha de documento."},
			 {"35","Operación Cancelada, existe a lo sumo una cuenta que no maneja documento y que posee información innecesaria en el asiento respectivo."},
			 {"36","Operación Cancelada, el mes/año supera el lapso permitido para grabar comprobantes."},
			 {"37","Operación Cancelada, el monto total del debe del comprobante es diferente a la suma de los montos deudores de los asientos."},
			 {"38","Operación Cancelada, el monto totoal del haber del comprobante es diferente a la suma de los montos acreedores de loa asientos."}};

	NumberFormat formatoDos = new DecimalFormat("#00");
	NumberFormat formatoTres = new DecimalFormat("#000");
	NumberFormat formatoCuatro = new DecimalFormat("#0000");
	NumberFormat formatoCinco = new DecimalFormat("#00000");
	NumberFormat formatoSeis = new DecimalFormat("#000000");
	NumberFormat formatoDiez = new DecimalFormat("#0000000000");
	Utilities util = new Utilities();
	int accoutingEntryID;
	int typeTransfer = 0;
	int m_user = Env.getCtx().getContextAsInt("#XX_L_USERACCOUNT_ID");
	String userName = "";
	String host = Env.getCtx().getContext("#XX_L_HOST");
		
	@Override
	protected String doIt() throws Exception {
		userName = nameUser();
		String respuesta = "";
		String sisOri = "COMPIERE";
		for (int i = sisOri.length()+1; i <= 10; i++){
			sisOri = sisOri + " ";
		}
		String estReg = "";
		for (int i = estReg.length()+1; i <= 2; i++){
			estReg = estReg + " ";
		}
		
		String valorRetorna = "";
		String cabecera = "";
		String nombreArchivoSExt = "";
		String desCom = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date sysdate = new Date();
		
		/****Se aplica el resulSet para el sql de la cabecera****/
		String head = constructHead();
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
			pstmt = DB.prepareStatement(head, null); 
			rs = pstmt.executeQuery(); 
			while(rs.next()){

				/****Se crea la ruta donde se guardan los archivos .txt si no existe la carpeta****/
				String ruta = System.getProperty("user.home") + "\\InterfazComprobantes\\";
				File file = new File(ruta);
				if (!file.exists()){	
					(new File(ruta)).mkdir();
				}
				ruta = ruta + dateFormat.format(sysdate)+"\\"; //verificar si se quiere que se cree una carpeta con la fecha del servidor y dentro los archivos
				File carpeta_fecha = new File(ruta);
				if (!carpeta_fecha.exists()){
					(new File(ruta)).mkdir();
				}

				/****Se asigna el nombre y la extension del archivo****/
				nombreArchivoSExt = "CX017" + userName; // + rs.getString("nrocom");

				cabecera = formatoTres.format(rs.getInt("codcia")) + 
					formatoTres.format(rs.getInt("codsuc")) + 
					formatoSeis.format(Integer.parseInt(rs.getString("nrocom"))) + 
					rs.getString("añocom") + 
					rs.getString("mescom") + 
				   	rs.getString("diacom");
				
				desCom = rs.getString("descom");
				for (int i = desCom.length()+1; i <= 20; i++){
					desCom = desCom + " ";
				}				
				cabecera = cabecera +
					desCom +
					formatearMonto(rs.getString("debcom") ,rs.getBigDecimal("debcom")) + 
					formatearMonto(rs.getString("habcom") ,rs.getBigDecimal("habcom"));
					
				String user = userName;
				for (int i = user.length()+1; i <= 10; i++){
					user = user + " ";
				}	
				cabecera = cabecera +
					user;

				/****Construcción del archivo txt****/
				String nombreArchivoCExt = nombreArchivoSExt + ".txt";
				FileWriter fw = new FileWriter(ruta + nombreArchivoCExt);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter salArch = new PrintWriter(bw);
				salArch.println(cabecera);
				salArch.print(executeDetail());
				salArch.close();
				bw.close();
				fw.close();
				
				/****Envío del archivo vía ftp al servidor de contabilidad AS400****/
				new XX_Test_FTP(ruta, nombreArchivoCExt, host, userName, userName); 
				
				/****Correr la interfaz de contabilidad CX017 en AS400****/
				
				for (int i = nombreArchivoSExt.length()+1; i <= 20; i++){
					nombreArchivoSExt = nombreArchivoSExt + " ";
				}
				
				String[] parametros = new String[] { 
						formatoTres.format(rs.getInt("codcia")),
						formatoTres.format(rs.getInt("codsuc")),
						nombreArchivoSExt,
						sisOri,
						rs.getString("nrocom"),
						formatoCinco.format(0),
						estReg};
				
				pmgCx017 objRunCx017 = new pmgCx017();
				
				try{
					valorRetorna = objRunCx017.runI5Program(new BigDecimal(parametros[0]), 
							new BigDecimal(parametros[1]),
							parametros[2],
							parametros[3],
							new BigDecimal(parametros[4]),
							new BigDecimal(parametros[5]),
							parametros[6],
							userName, 
							host); 
					
					for (int i = 0; i < ESTREG.length; i++){
						if (ESTREG[i][0].equals(valorRetorna)){
							respuesta = respuesta + ESTREG[i][1];
							if (valorRetorna.equals(ESTREG[0][0])){																
								if (typeTransfer == 1){ //Cuentas por Pagar Aprobadas									
									//Actualiza la sincronización de contabilidad en las facturas 
									//asociadas a dicho asiento contable
									String sql = "update C_Invoice " +
										 		 "set XX_SynchronizationAccount = 'Y' " +
										 		 "where C_Invoice_ID in " +
										 		 	"(select distinct(C_Invoice_ID) " +
										 		 	"from Fact_Acct " +
										 		 	"where XX_VCN_AccoutingEntry_ID = " + accoutingEntryID + ") ";
									DB.executeUpdate(null, sql);
									
									//Actualiza el chechk de transferencia del asiento contable
									sql = "update XX_VCN_AccoutingEntry set IsTransferred = 'Y' " +
										  "where XX_VCN_AccoutingEntry_ID = " + accoutingEntryID;
									DB.executeUpdate(null, sql);									
								}else if (typeTransfer == 2){ //Cuentas por Pagar Estimadas
									
								}else if (typeTransfer == 3){ //Compra/Venta
									
								}else if (typeTransfer == 4 || typeTransfer == 5){ //Diario de Caja o Banco
									//Actualiza el chechk de transferencia del asiento contable
									String sql = "update XX_VCN_AccoutingEntry set IsTransferred = 'Y' " +
										  "where XX_VCN_AccoutingEntry_ID = " + accoutingEntryID;
									DB.executeUpdate(null, sql);
								}
								
							}else{
								//Enviar correo con el error que arrojó la interfaz y el número del asiento
								String mensaje = Msg.getMsg(Env.getCtx(), "XX_ContextInterfaceCxPA") +
									"\r\n" + 
									Msg.getMsg(Env.getCtx(), "XX_ContextInterfaceCxPAOne") + 
									rs.getString("nrocom") + 
									Msg.getMsg(Env.getCtx(), "XX_ContextInterfaceOPayTwo") + 
									respuesta +
									"\r\n";		
								Utilities mail = new Utilities(Env.getCtx(), null,
									Env.getCtx().getContextAsInt("#XX_L_MAILSRINTERFACE_ID"), 
									mensaje, -1, 
									Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,
									-1, Env.getCtx().getAD_User_ID(), null);					
								mail.ejecutarMail(); 
								mail = null;
							}
							break;
						}
					}
					
					if(valorRetorna.equalsIgnoreCase("ok"))
						ADialog.info(1, new Container(), respuesta);
					else
						ADialog.error(1, new Container(), respuesta);
					
				}catch(Exception e){
					e.printStackTrace();
					respuesta = "ERROR en la importación de la interfaz.";
				}
			}
		}catch(Exception e1){
			e1.printStackTrace();
		}
		finally{
			rs.close();
			pstmt.close();
		}
		
		return respuesta;
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_VCN_AccoutingEntry_ID"))
				accoutingEntryID = element.getParameterAsInt();
			else if (name.equals("XX_TypeTransfer"))
				typeTransfer = element.getParameterAsInt();
		}	
	}
	
	/**
	 * Se encarga de buscar la información pertinentes a la cabecera requerida por la interfaz
	 * @return el sql
	 */
	private String constructHead(){
		String sql = "select cli.XX_Value as codcia, ace.XX_Office codsuc, ace.XX_ControlNumber nrocom, " +
					 "to_char(ace.DateTrx,'yyyy') as añocom, to_char(ace.DateTrx,'MM') as mescom, " +
					 "to_char(ace.DateTrx,'dd') as diacom, substr(ace.Description, 0, 20) as descom, " + 
					 "round(ace.XX_TotalShould, 2) as debcom, round(ace.XX_TotalHave, 2) as habcom " +
					 "from XX_VCN_AccoutingEntry ace " +
					 "inner join AD_Client cli on (ace.AD_Client_ID = cli.AD_Client_ID) " +
					 "where ace.XX_VCN_AccoutingEntry_ID = " + accoutingEntryID + " ";		
		return sql;
	}

	/**
	 * Se encarga de buscar y construir la información perteneciente a 
	 * las líneas de los asientos contables 
	 * @return lineas del txt
	 */
	private String executeDetail(){
		String detail = "", codCta, nroDoc, monAsi, desAsi, codDep, codDiv, 
			codSec, numAux, tipDoc, fecVen, fecDoc;
		String sql = "";
		
			sql = "select dae.XX_VCN_Line as linasi, ele.value as codcta, dae.XX_Office as codsuc, " +
			 "dae.XX_Division as coddiv, dae.XX_Departament as coddep, " +
			 "dae.XX_SectionCode as codsec, dae.XX_Aux as numaux, " +
			 "(case when ele.XX_DOC ='Y' then dae.DocumentNo else NULL end) NRODOC, " +
			 "dae.XX_DocumentType as tipdoc, to_char(dae.XX_DocumentDate, 'dd.MM.yyyy') as fecdoc, " +
			 "to_char(dae.XX_DueDate, 'dd.MM.yyyy') as fecven, " +
			 "substr(dae.Description, 0, 40) as desasi, round(dae.AmtAcctDr, 2) as debe, " +
			 "round((dae.AmtAcctCr * -1), 2) as haber " +
			 "from Fact_Acct dae " +
			 "inner join C_ElementValue ele on (dae.Account_ID = ele.C_ElementValue_ID) " +
			 "where dae.XX_VCN_AccoutingEntry_ID = " + accoutingEntryID + " " +
			 "order by dae.XX_VCN_Line asc ";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				detail = detail + 
						 formatoCinco.format(rs.getInt("linasi"));
				
				codCta = rs.getString("codcta");
				for (int j = codCta.length(); j < 14; j++){
					codCta = codCta + " ";
				}	 
				
				detail = detail + 
						 codCta +
						 rs.getString("codsuc");
				
				codDiv = rs.getString("coddiv"); 
				if (codDiv != null && !codDiv.trim().equals(""))
					codDiv = formatoDos.format(Integer.parseInt(codDiv));
				else
					codDiv = formatoDos.format(0);
				
				codDep = rs.getString("coddep");
				if (codDep != null && !codDep.trim().equals(""))
					codDep = formatoTres.format(Integer.parseInt(codDep));
				else 
					codDep = formatoTres.format(0);
				
				codSec = rs.getString("codsec");
				if (codSec != null && !codSec.trim().equals(""))
					codSec = formatoDos.format(Integer.parseInt(codSec));
				else
					codSec = formatoDos.format(0);
				
				numAux = rs.getString("numaux");
				if (numAux != null && !numAux.trim().equals(""))
					numAux = formatoDiez.format(Integer.parseInt(numAux));
				else
					numAux = formatoDiez.format(0);

				nroDoc = rs.getString("nrodoc");
				if (nroDoc != null  && !nroDoc.trim().equals("")){
					for (int j = nroDoc.length(); j < 10; j++){
						nroDoc = nroDoc + " ";
					}
				}else{
					nroDoc = "";
					for (int j = nroDoc.length(); j < 10; j++){
						nroDoc = nroDoc + " ";
					}
				}

				tipDoc = rs.getString("tipdoc");
				if (tipDoc == null){
					tipDoc = "";
					for (int j = tipDoc.length(); j < 3; j++){
						tipDoc = tipDoc + " ";
					}
				}
				
				fecDoc = rs.getString("fecdoc");
				if (fecDoc == null)
					fecDoc = "01.01.0001";
				
				fecVen = rs.getString("fecven");
				if (fecVen == null)
					fecVen = "01.01.0001";
				
				detail = detail +
					codDiv +
					codDep +
					codSec +
					numAux +
					nroDoc +
					tipDoc + 
					fecDoc +
					fecVen +
					"000";
						 
				desAsi = rs.getString("desasi"); 
				for (int j = desAsi.length(); j < 40; j++){
					desAsi = desAsi + " ";
				}
				
				if (rs.getBigDecimal("debe").compareTo(new BigDecimal(0)) == 0)
					monAsi = formatearMonto(rs.getString("haber") ,rs.getBigDecimal("haber"));
				else 
					monAsi = formatearMonto(rs.getString("debe") ,rs.getBigDecimal("debe"));

				detail = detail + 
						 desAsi + 
						 monAsi +
						 "\r\n";
			}
		}catch(Exception e){
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
		
		return detail;
	}

	/**
	 * Se encarga de quitarle la coma al monto, y en caso de ser negativo, 
	 * colocarle el símbolo correspondiente
	 * @param monto monto como tipo de dato String
	 * @param m monto como tipo de dato BigDecimal
	 * @return
	 */
	protected String formatearMonto(String monto,BigDecimal m){
		int posicion = 0;
		if (monto.contains(".")){
			posicion = monto.indexOf(".") + 1;
			if ((monto.length() - posicion) < 2){
				monto = monto + "0";
			}else{
				if ((monto.length() - posicion) > 2){
					monto = monto.substring(0, posicion + 2);
				}
			}
			monto = monto.replace(".", "");
		}else{
			monto = monto + "00";
		}
		
		if (monto.contains("-")){
			monto = monto.replace("-", "");
			int i = Character.digit(monto.charAt(monto.length()-1),10); 
			monto = monto.substring(0, monto.length()-1) + simbolo[i][1];
		}
		
		for (int j = monto.length(); j < 17; j++){
			monto = "0" + monto;
		}
		
		return monto;
	}
	
	/**
	 * Busca la clave de búsqueda del usuario de contabilidad
	 * @return
	 */
	public String nameUser(){
		String name = "";
		String sql = "select upper(value) from AD_User where AD_User_ID = " + m_user;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				name = rs.getString(1);
			}
		}catch(Exception e){
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
		return name;
	}
}
