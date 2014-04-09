package compiere.model.payments.processes;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;



import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MPayment;
import compiere.model.cds.Utilities;
import compiere.model.payments.pmgBx014;

/**
 * Proceso que se encarga de generar la interfaz de las 
 * Ordenes de Pago con el Sistema de Banco (Bx014)
 * Nombre del proceso en compiere: Interface Payment Order
 * @author Jessica Mendoza
 *
 */
public class XX_InterfacePayOrder  extends SvrProcess{

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
	
	private String[][] ESTREG = {{"OK","Operación exitosa sobre el registro."},
			 {"01","Operación Cancelada, no posee autorización para utilizar esta interfaz."},
			 {"02","Operación Cancelada, no se puede agregar el registro."},
			 {"03","Operación Cancelada, no se puede sustituir el registro."},
			 {"04","Operación Cancelada, no se puede eliminar el registro."},
			 {"05","Operación Cancelada, la compañia está en cero o no es correcta."},
			 {"06","Operación Cancelada, la sucursal/oficina está en cero o no es correcta."},
			 {"07","Operación Cancelada, el número de orden de pago está en ceo o no es correcta."},
			 {"08","Operación Cancelada, la fecha de la orden de pago no es correcta."},
			 {"09","Operación Cancelada, el concepto de la orden de pago está en blanco."},
			 {"10","Operación Cancelada, el monto de la orden de pago está en cero."},
			 {"11","Operación Cancelada, el tipo de pago está en blanco o no es válido."},
			 {"12","Operación Cancelada, el nombre del ordenante está en blanco."},
			 {"13","Operación Cancelada, el nombre del beneficiario está en blanco."},
			 {"14","Operación Cancelada, el código del proveedor está en cero o no es válido."},
			 {"15","Operación Cancelada, la línea del asiento está en cero."},
			 {"16","Operación Cancelada, la cuenta contable del asiento es incorrecta."},
			 {"17","Operación Cancelada, el código de sucursal/oficina del asiento no es correcta."},
			 {"18","Operación Cancelada, el código de división no es correcto."},
			 {"19","Operación Cancelada, el código de departamento no es correcto."},
			 {"20","Operación Cancelada, el código de sección no es correcta."},
			 {"21","Operación Cancelada, el número de auxiliar no es correcto."},
			 {"22","Operación Cancelada, el tipo de documento está en blanco."},
			 {"23","Operación Cancelada, el número de documento está en blanco."},
			 {"24","Operación Cancelada, la fecha del documento no es correcta."},
			 {"25","Operación Cancelada, la fecha de vencimiento no es correcta."},
			 {"26","Operación Cancelada, los días de vencimiento están en blanco."},
			 {"27","Operación Cancelada, la descripción del asiento está en blanco."},
			 {"28","Operación Cancelada, el monto del asiento está en cero."},
			 {"29","Operación Cancelada, el monto del asiento es menor que cero."},
			 {"30","Operación Cancelada, la orden de pago no posee líneas de detalle."},
			 {"31","Operación Cancelada, no se puede acceder al archivo.txt."},
			 {"32","Operación Cancelada, no se puede acceder a la interfaz."},
			 {"33","Operación Cancelada, se ha producido un error al tratar de grabar la orden de pago en la interfaz."},
			 {"34","Operación Cancelada, se ha producido un error al tratar de actualizar la orden de pago en la interfaz."},
			 {"35","Operación Cancelada, se ha producido un error al tratar de eliminar la orden de pago en la interfaz."},
			 {"36","Operación Cancelada, se ha producido un error al tratar de grabar los asientos de la orden de pago en la interfaz."},
			 {"37","Operación Cancelada, el nombre del archivo.txt es incorrecto."},
			 {"38","Operación Cancelada, la operación del registro no está definida."},
			 {"39","Error al eliminar el detalle de la orden anterior."},
			 {"40","El archivo de interfaz BA0100TXT no tiene el encabezado de la orden de pago."},
			 {"41","Operación Cancelada, el usuario no posee autorización para el uso de la interfaz CX003 CONTABILIDAD V3."},
			 {"42","Operación Cancelada, el usuario no posee autorización para el uso de la interfaz CX011 CONTABILIDAD V4."},
			 {"43","Operación Cancelada, el nombre del proveedor está en blanco."},
			 {"44","Operación Cancelada, el tipo de persona es distinto de 'J' Juridica, 'V' Venezolano, 'E' Extranjero, 'G' Gobierno, 'P' Pasaporte."},
			 {"45","Operación Cancelada, la cuenta bancaria está en blanco."},
			 {"46","Operación Cancelada, el número de rif o cédula del proveedor está en blanco."},
			 {"47","Operación Cancelada, el correo electrónico es distinto de blanco y no posee el carácter @."},
			 {"48","Operación Cancelada, si la cuenta del proveedor tiene valor el banco debe ser distinto de cero y debe existir en el Sistema."},
			 {"49","Operación Cancelada, usuario sin autorización a la cuenta contable."},
			 {"50","Operación Cancelada, usuario sin autorización a usar la interfaz CX014."},
			 {"51","Operación Cancelada, usuario sin autorización al número de auxiliar."},
			 {"52","Operación Cancelada, la fecha del documento no puede ser mneor a la fecha de vencimiento del mismo."},
			 {"53","El tipo de moneda es inválida."},
			 {"54", "Hubo Órdenes que no se procesaron."},
			 {"55", "Operación Cancelada, la operación del registro es diferente de S, A y E."},
			 {"ER","Operación Cancelada, la cuenta contable del asiento es incorrecta."}};
	
	int idPaySelection = 0;
	int idPay = 0;
	ArrayList<Integer> listPaySelection;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_PaySelection_ID")){
				listPaySelection = (ArrayList<Integer>) element.getParameter();
			}else if (name.equals("C_Payment_ID")){
				
				idPay = ((ArrayList<Integer>) element.getParameter()).get(0); //element.getParameter_ToAsInt();
				MPayment pago = new MPayment(getCtx(), idPay, get_Trx());

			}
		}
	}
	
	@Override
	protected String doIt() throws Exception {		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date sysdate = new Date();	
		String respuesta = "";
		
		/****Se crea la ruta donde se guardan los archivos .txt si no existe la carpeta****/
		String ruta = System.getProperty("user.home") + "\\InterfazPagos\\";
		boolean success;
		
		File file=new File(ruta);
		if (!file.exists()){	
			success = (new File(ruta)).mkdir();
		}		
		ruta = ruta + dateFormat.format(sysdate)+"\\"; //verificar si se quiere que se cree una carpeta con la fecha del servidor y dentro los archivos
		File carpeta_fecha = new File(ruta);
		if (!carpeta_fecha.exists()){
			success = (new File(ruta)).mkdir();
		}

		/*La orden de pago viene de la selección de pago, a través de la planificación de pago*/
		if ( idPay == 0){
			/****Se aplica el resulSet para el sql de la cabecera****/
			List lista2 = new ArrayList(listPaySelection);
			Iterator it = lista2.iterator();
			while (it.hasNext()){
				idPaySelection = (Integer) it.next();
				//Se encarga de construir y armar la cabecera del txt para la interfaz
				respuesta = builHeadLine(constructHeadPaySel(), ruta); 
				
			}
		}else{ /*La orden de pago es un anticipo, que viene a través del Pago*/
			//Se encarga de construir y armar la cabecera del txt para la interfaz
			respuesta = builHeadLine(constructHeadPay(), ruta); 
		    
			
		}
		return "";
	}

	/**
	 * Construcción de la cabecera requerida por la interfaz, 
	 * con la orden de pago desde la selección de pago
	 * @return sql
	 */
	private String constructHeadPaySel(){
		String sql = "select cli.XX_Value CODCIA, pay.C_PaySelection_ID NROORD, " +
					 "to_char(pay.CREATED,'dd.mm.yyyy') FECORD, SUBSTR(pay.Name, 0, 50) CONORD, " +
					 "pay.TotalAmt MONORD, cli.name as NOMORD, " +
					 "(case when rbpa.C_BPartner_ID is not null then rbpa.name "+
					 "else par.name end) as NOMBEN, "+
					 "par.value NROPRO, " +
					 "par.XX_TypePerson TIPPER, par.XX_CI_RIF RIFIDE, " +
					 "pba.AccountNo CTABAN, par.XX_VendorEmail CORREO, " +
					 "ban.value CODBAN " +
					 "from C_PaySelection pay " +
					 "inner join AD_Client cli on (cli.AD_Client_ID = pay.AD_Client_ID) " +
					 "inner join C_BPartner par on (pay.name = par.name) " +
					 "inner join C_BP_BankAccount pba on (par.C_BPartner_ID = pba.C_BPartner_ID) " +
					 "inner join C_Bank ban on (pba.C_Bank_ID = ban.C_Bank_ID) " +
					 //Verifica si un BPartner esta relacionado con otro (Ej. Banco activo) RC -14102013
					 "left join C_BP_Relation rbp on (par.C_Bpartner_ID = rbp.C_BPartner_ID and rbp.isactive ='Y') " +
					 "left join C_BPartner rbpa on (rbpa.C_Bpartner_ID = rbp.C_BPartnerrelation_ID) " +
					 "where pba.XX_IsPrimary = 'Y' and pba.ISACTIVE ='Y' and pay.C_PaySelection_ID = " + idPaySelection + " ";
		return sql;
	}
	
	/**
	 * Construcción de la cabecera requerida por la interfaz,
	 * con la orden de pago como un anticipo 
	 * @return sql
	 */
	private String constructHeadPay(){ 
		String sql = "select ord.documentno ORDEN, con.xx_Contractvalue, cli.name NOMCIA, cli.XX_Value CODCIA, pay.DocumentNo NROORD, " +
					 "to_char(pay.DateTrx,'dd.mm.yyyy') FECORD, pay.XX_SynchronizationBank sync, pay.PayAmt MONORD, " +
					 	"(" +
						 	"select pa.name " +
						 	 "from AD_User_Roles ur " +
						 	 "inner join AD_User us on (ur.AD_User_ID = us.AD_User_ID) " +
						 	 "inner join AD_Role ro on (ur.AD_Role_ID = ro.AD_Role_ID) " +
						 	 "inner join C_BPartner pa on (us.C_BPartner_ID = pa.C_BPartner_ID) " +
						 	 "where ro.AD_Role_ID = " + Env.getCtx().getContextAsInt("#XX_L_ROLECONTROL_ID") + " " +
					 	") as NOMORD, " +
					 "(case when rbpa.C_BPartner_ID is not null then rbpa.name "+
					 "else par.name end) NOMBEN, par.value NROPRO, " +
					 "par.XX_TypePerson TIPPER, par.XX_CI_RIF RIFIDE, " +
					 "pba.AccountNo CTABAN, par.XX_VendorEmail CORREO, " +
					 "ban.value CODBAN, pay.C_Payment_ID, pay.XX_IsAdvance FACANT " +
					 "from C_Payment pay " +
					 "inner join AD_Client cli on (cli.AD_Client_ID = pay.AD_Client_ID ) " +
					 "inner join C_BPartner par on (pay.C_BPartner_ID = par.C_BPartner_ID) " +
					 "left join C_Order ord on (pay.c_order_id = ord.c_order_id) " + 
					 "left join XX_Contract con on (pay.xx_contract_id = con.xx_Contract_ID) "+
					 "inner join C_BP_BankAccount pba on (par.C_BPartner_ID = pba.C_BPartner_ID) " +
					 "inner join C_Bank ban on (pba.C_Bank_ID = ban.C_Bank_ID) " +
					 //Verifica si un BPartner esta relacionado con otro, y que esa relacion este activa (Ej. Banco activo) RC -14102013
					 "left join C_BP_Relation rbp on (par.C_Bpartner_ID = rbp.C_BPartner_ID and rbp.isactive ='Y') " +
					 "left join C_BPartner rbpa on (rbpa.C_Bpartner_ID = rbp.C_BPartnerrelation_ID) " +
					 
					 "where pay.DocStatus = 'CO'  " +
					 "and pay.XX_SynchronizationBank = 'N'  " +
					 "and pba.XX_IsPrimary = 'Y' and pba.ISACTIVE ='Y' " +
					 "and pay.C_Currency_ID = 205 " +
					 "and pay.C_Payment_ID = " + idPay + " ";
		return sql;
	}
	
	/**
	 * Se encarga de armar la cabecera del txt
	 * @param head query de la cabecera
	 * @return
	 */
	public String builHeadLine(String head, String ruta){		
		String descripcion, nomOrd, nomBen, nomPro, 
			rifIde, ctaBan, correo, linea, id = "", tipo = "",
			nombreArchivoSExt = "", cabecera = "", nombreArchivoCExt = "",
			txtErr = "", valorRetorna = "", respuesta = "";
		String user = Env.getCtx().getContext("#XX_L_USERBANK");
		String password = Env.getCtx().getContext("#XX_L_PASSWORDBANK");
		String host = Env.getCtx().getContext("#XX_L_HOST");
		NumberFormat formatoTres = new DecimalFormat("#000");
		NumberFormat formatoSiete = new DecimalFormat("#0000000");
		NumberFormat formatoDiez = new DecimalFormat("#0000000000");
		
		String sisOri = "COMPIERE";
		for (int i = sisOri.length()+1; i <= 10; i++){
			sisOri = sisOri + " ";
		}
		String estReg = "";
		for (int i = estReg.length()+1; i <= 2; i++){
			estReg = estReg + " ";
		}
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
	
			System.out.println(head);
			
			pstmt = DB.prepareStatement(head, null); 
		    rs = pstmt.executeQuery(); 
		    
		    if (rs.equals(null)){
		    	System.out.println("rs nulo");
		    }
			while(rs.next()){
				System.out.println("entre a crear cabecera");
				
				cabecera = "C" + formatoTres.format(rs.getInt("CODCIA")) + "001";
				
				if ( idPay == 0){
					
					id = formatoDiez.format(rs.getInt("NROORD"));
					tipo = "S"; //tipo selección de pago
				}else{
					id = formatoDiez.format(Integer.parseInt(rs.getString("NROORD")));
					tipo = "A"; //tipo anticipo
				}
				cabecera = cabecera + id + rs.getString("FECORD");    

				if (idPay == 0){
					descripcion = rs.getString("CONORD");					
					for (int i = descripcion.length()+1; i <= 50; i++){
						descripcion = descripcion + " ";
					}
				}
				else{
					descripcion = "Anticipo de la OC No. " + rs.getString("ORDEN");
					for (int i = descripcion.length()+1; i <= 50; i++){
						descripcion = descripcion + " ";
					}
				}

				cabecera = cabecera + descripcion + 
				   formatearMonto(rs.getString("MONORD"),rs.getBigDecimal("MONORD"));
				
				//Se asigna el nombre de la Compañia que genera la Orden de pago como Ordenante (Seleccion de pago o Anticipo)
				if (idPay == 0)
					nomOrd = rs.getString("NOMORD");
				else 
					nomOrd = rs.getString("NOMCIA");

				for (int i = nomOrd.length()+1; i <= 40; i++) 
					nomOrd = nomOrd + " ";
				cabecera = cabecera + nomOrd;
					
				
				nomBen = rs.getString("NOMBEN");
				for (int i = nomBen.length()+1; i <= 70; i++){
					nomBen = nomBen + " ";
				}
				cabecera = cabecera + nomBen + formatoSiete.format(rs.getInt("NROPRO"));
					
				nomPro = rs.getString("NOMBEN");
				for (int i = nomPro.length()+1; i <= 70; i++){
					nomPro = nomPro + " ";
				}
				cabecera = cabecera + nomPro + rs.getString("TIPPER");
					
				rifIde = rs.getString("TIPPER") + rs.getString("RIFIDE");
				for (int i = rifIde.length()+1; i <= 12; i++){
					rifIde = rifIde + " ";
				}
				cabecera = cabecera + rifIde;
					
				ctaBan = rs.getString("CTABAN");
				for (int i = ctaBan.length()+1; i <= 20; i++){
					ctaBan = ctaBan + " ";
				}
				cabecera = cabecera + ctaBan;
					
				correo = rs.getString("CORREO");
				for (int i = correo.length()+1; i <= 50; i++){
					correo = correo + " ";
				}
				cabecera = cabecera + correo + formatoTres.format(rs.getInt("CODBAN")) + "Bs " + "A";	
				
				//Cambio para generar lineas cuando sea anticipo..idPay == 0
				if (idPay == 0)
					linea = sqlLines(Integer.parseInt(id), tipo);
				else 
					linea = sqlLines(idPay, tipo);
				
				//Cambio para generar lineas cuando sea anticipo..idPay == 0
				if (idPay == 0){
					
					nombreArchivoSExt = "BX014" + user + idPaySelection;
					nombreArchivoCExt = nombreArchivoSExt + ".txt";
					FileWriter fw = new FileWriter(ruta + nombreArchivoCExt);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter salArch = new PrintWriter(bw);
					salArch.println(cabecera);
					salArch.print(linea);
					salArch.close();
					bw.close();
					fw.close();
				}
				else{
					
					nombreArchivoSExt = "BX014" + user + idPay;
					nombreArchivoCExt = nombreArchivoSExt + ".txt";
					FileWriter fw = new FileWriter(ruta + nombreArchivoCExt);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter salArch = new PrintWriter(bw);
					salArch.println(cabecera);
					salArch.print(linea);
					salArch.close();
					bw.close();
					fw.close();
				}
				new XX_Test_FTP(ruta, nombreArchivoSExt + ".txt", host, user, password);
				
				for (int i = nombreArchivoSExt.length()+1; i <= 20; i++){
					nombreArchivoSExt = nombreArchivoSExt + " ";
				}
				for (int i = txtErr.length()+1; i <= 20; i++){
					txtErr = txtErr + " ";
				}
									
				/****Correr la interfaz de contabilidad BX014 en AS400****/
				String[] parametros = new String[] { 
						formatoTres.format(rs.getInt("CODCIA")),
						formatoTres.format(1),
						nombreArchivoSExt,
						//"A",
						sisOri,
						estReg,
						formatoTres.format(0),
						txtErr};
						
				pmgBx014 objRunBx014 = new pmgBx014();
					
				try{
					valorRetorna = objRunBx014.runI5Program(new BigDecimal(parametros[0]), 
						new BigDecimal(parametros[1]),
						parametros[2],
						parametros[3],
						parametros[4],
						new BigDecimal(parametros[5]),
						parametros[6],
						user,
						password,
						host);
									
					for (int i = 0; i < ESTREG.length; i++){
						if (ESTREG[i][0].equals(valorRetorna)){
							respuesta = ESTREG[i][1];
							break;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					respuesta = "ERROR en la importación de la interfaz.";
				}
				
				if (respuesta.equalsIgnoreCase(ESTREG[0][0])){
					//LLAMAR A LA INTERFAZ DE MOVIMIENTO
					
				}else{
					//Enviar correo con el error que arrojó la interfaz y el número de la órden de pago
					System.out.println(valorRetorna);
					String mensaje = Msg.getMsg(Env.getCtx(), "XX_ContextIniInterfaceOPay") + 
							"\r\n" + 
							Msg.getMsg(Env.getCtx(), "XX_ContextInterfaceOPayOne") +
							id + " " + Msg.getMsg(Env.getCtx(), "XX_ContextInterfaceOPayTwo") +
							respuesta +
							"\r\n";					
					Utilities util = new Utilities(Env.getCtx(), null,
						Env.getCtx().getContextAsInt("#XX_L_MAILSRINTERFACE_ID"), 
						mensaje, -1, 
						Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,
						-1, Env.getCtx().getAD_User_ID(), null);					
					util.ejecutarMail(); 
					util = null;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
				setXX_SynchronizationBank(idPay);
			
		}
		return respuesta;
	}
	
	/**
	 * 
	 * @param idPayment
	 * @param anticipo
	 * @return
	 */
	private String sqlLines(int id, String tipo){
		String sqlDetail = "";
		String linea = "";
		
		if (tipo.equals("A")){
			sqlDetail = "select bp.value VALUE, ord.XX_POType, ord.XX_PurchaseType, " +
						"ord.DocumentNo NRODOC, to_char(ord.Created,'dd.mm.yyyy') FECDOC, " +
						"to_char(sysdate,'dd.mm.yyyy') FECVEN, pay.PayAmt MONASI, to_char('ANT') TIPDOC " +
						"from C_Payment pay " +
						"inner join C_Order ord on (pay.C_Order_ID = ord.C_Order_ID) " +
						"inner join C_bpartner bp on (ord.c_bpartner_id = bp.c_bpartner_id) "+
						"where pay.C_Payment_ID = " + id;
			linea = detailLinesAnt(sqlDetail);
		}else{
			sqlDetail = "select ord.XX_POType, ord.XX_PurchaseType, con.XX_Contract_ID, doc.C_DocType_ID TIPDOC, " +
						"SUBSTR(inv.DocumentNo, 0, 10) NRODOC, to_char(inv.DateInvoiced,'dd.mm.yyyy') FECDOC, " +
						"(case when inv.XX_DueDate > inv.DateInvoiced then " +
						"to_char(inv.XX_DueDate,'dd.mm.yyyy') else to_char(inv.DateInvoiced,'dd.mm.yyyy') " +
						"end) FECVEN,  "+
						"ord.DocumentNo, psl.PayAmt MONASI, con.XX_Lease, par.value, XX_INVOICETYPE  " +
						"from C_PaySelection pay  " +
						"inner join C_PaySelectionLine psl on (pay.C_PaySelection_ID = psl.C_PaySelection_ID ) " +
						"inner join C_Invoice inv on (psl.C_Invoice_ID = inv.C_Invoice_ID) " +
						"inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
						"inner join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
						"left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " +
						"left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
						"where pay.C_PaySelection_ID = " + id;
			linea = detailLinesFac(sqlDetail);
		}
		
		return linea;
	}
	
	/**
	 * Construye el detalle de las líneas para las órdenes de pago 
	 * que se encuentran en la selección de pago
	 * @param sql query con el detalle del txt
	 * @return
	 */
	public String detailLinesFac(String sql){
		int cantLineas = 1;
		int tipoCategoria = 0;
		String linea = "", codCta = "", tipDoc = "";
		String conaux = "", cuenta = "FAC";
		String proveedor = "0000000000";
		String[] accountaux = {};
		NumberFormat formatoTres = new DecimalFormat("#000");
		NumberFormat formatoDiecisiete = new DecimalFormat("#0000000");
		StringBuffer m_where = new StringBuffer();
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery(); 
			while(rs.next()){
				String descripcion = "Factura de";
				
				if (rs.getString("XX_POType") == null && (!rs.getString("XX_INVOICETYPE").equals("I")))
					if ((rs.getString("XX_LEASE")==null) || rs.getString("XX_LEASE").equals("N"))
						//Para Servicios que no sean Arrendadmientos se pasa la categoria de producto Standard (CCAPOTE)
						tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID");
					else {
						//Para Servicios de tipo Arrendadmientos se pasa la categoria de producto Servicio con la marca de Arrendamiento (CCAPOTE)
						tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
						m_where.append(" and XX_Lease = 'Y' ");
					}
				else{
					// Para Mercancia para la Venta
					if (rs.getString("XX_PurchaseType") == null){
						tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID");
						// Para ubicar la cuenta que le corresponde a Mercancia (CCAPOTE)  
						m_where.append(" and XX_ElementType = 'Nacional' " +
									   "and XX_Transitional <> 'Y' ");
					}
					else if ((rs.getString("XX_PurchaseType").equals("SU")) || (rs.getString("XX_PurchaseType").equals("SE")))
						//Se pasa servicios porque es la misma cuenta tanto la servicios como para sumistros y materiales
						tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID"); 					
					else if (rs.getString("XX_PurchaseType").equals("FA")){
						//Para Activo Fijo se le pasa categoría del producto “Servicio” y marcada como “Cuentas por Pagar” 
						tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
						m_where.append(" and XX_AccountPayable = 'Y' ");
					}
				}
				accountaux = accounts(tipoCategoria, m_where, cuenta);
				codCta = accountaux[0];
				conaux    = accountaux[1];
				
				for (int i = codCta.length()+1; i <= 14; i++){
					codCta = codCta + " ";
				}
				
				if(conaux.equals("Y")){
					proveedor = rs.getString("VALUE");
					for (int i = proveedor.length()+1; i <= 10; i++){
						proveedor = "0"  + proveedor;
					}
				}
				else
					proveedor = "0000000000";
				
				if(cantLineas != 1){
					linea = linea  + "\r\n";
				}
				
				linea = linea + "D" + formatoTres.format(cantLineas) + 
					codCta + formatoTres.format(1) +
					formatoDiecisiete.format(0) +
					proveedor;

				//
				
				if (rs.getInt("TIPDOC") == Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID"))
					tipDoc = "FAC"; 
				else if (rs.getInt("TIPDOC") == Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID"))
					tipDoc = "NCR";
				else if (rs.getInt("TIPDOC") == Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEDEBIT_ID"))
					tipDoc = "NDB";
				else if (rs.getInt("TIPDOC") == Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTNEG_ID"))
					tipDoc = "AST";
				else if (rs.getInt("TIPDOC") == Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTPOS_ID"))
					tipDoc = "AST";
				else{
					tipDoc = "";
					for (int i = tipDoc.length()+1; i <= 14; i++){
						tipDoc = tipDoc + " ";
					}
				}					
				linea = linea + tipDoc;
				
				String nroDoc = rs.getString("NRODOC");
				//Comentado por CCapote
				/*if (nroDoc.contains("-")){
					nroDoc = nroDoc.replace("-", "");
				}*/
				for (int i = nroDoc.length()+1; i <= 10; i++){
					nroDoc = nroDoc + " ";
				}				
				linea = linea + nroDoc + rs.getString("FECDOC") + rs.getString("FECVEN") + 
					formatoTres.format(1);
				
				if (rs.getInt("XX_Contract_ID") == 0)
					descripcion = descripcion + " la O/C N°: " + rs.getString("DocumentNo");
				else
					descripcion = descripcion + "l contrato N°: " + rs.getString("XX_Contract_ID");
				for (int i = descripcion.length()+1; i <= 40; i++){
					descripcion =  descripcion + " ";
				}				
				linea = linea + descripcion + 
					formatearMonto(rs.getString("MONASI"),rs.getBigDecimal("MONASI")); 
				
				cantLineas++;
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
		
		return linea;
	}
	
	/**
	 * Construye el detalle de las líneas para los pagos de anticipos
	 * @param sql query con el detalle del txt
	 * @return
	 */
	public String detailLinesAnt(String sql){
		int cantLineas = 1;
		String linea = "";
		String codCta = "";
		String conaux = "", cuenta= "ANT";
		String proveedor = "";
		String[] accountaux = {};
		int tipoCategoria = 0;
		NumberFormat formatoTres = new DecimalFormat("#000");
		NumberFormat formatoDiecisiete = new DecimalFormat("#0000000");
		String descripcion = "Anticipo de la O/C N°: ";
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		StringBuffer m_where = new StringBuffer();

		try{
			//Imprimir sql de las lineas
			System.out.println(sql);
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery(); 
			while(rs.next()){

				if (rs.getString("XX_PurchaseType") == null)
					tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID");					
				else 
					tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
				accountaux = accounts(tipoCategoria, m_where, cuenta);
				codCta = accountaux[0];
				conaux    = accountaux[1];
				
				for (int i = codCta.length()+1; i <= 14; i++){
					codCta = codCta + " ";
				}
				
				if(conaux.equals("Y")){
					proveedor = rs.getString("VALUE");
					for (int i = proveedor.length()+1; i <= 10; i++){
						proveedor = "0"  + proveedor;
					}
				}
				else
					proveedor = "0000000000";
				
				if(cantLineas != 1){
					linea = linea  + "\r\n";
				}
				
				linea = linea + "D" + formatoTres.format(cantLineas) + codCta + 
					formatoTres.format(1) + formatoDiecisiete.format(0) + proveedor + rs.getString("TIPDOC");

				String nroDoc = rs.getString("NRODOC");
				for (int i = nroDoc.length()+1; i <= 10; i++){
					nroDoc = nroDoc + " ";
				}				
				linea = linea + nroDoc + rs.getString("FECDOC") + rs.getString("FECVEN") + formatoTres.format(1);
				
				descripcion = descripcion + rs.getString("NRODOC");
				for (int i = descripcion.length()+1; i <= 40; i++){
					descripcion =  descripcion + " ";
				}				
				linea = linea + descripcion + 
					formatearMonto(rs.getString("MONASI"),rs.getBigDecimal("MONASI")); 
				
				cantLineas++;
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
		
		return linea;
	}
	
	/**
	 * Se encarga de buscar la cuenta contable, a través de los parámetros de entrada
	 * @param tipoOC Tipo de la Orden de Compra
	 * @param tipoBien Tipo de Bien
	 * @return La cuenta contable
	 */
	public String[] accounts(int tipoCategoria, StringBuffer m_where, String cuenta){
		String sqlAccount = "";
		if(cuenta.equals("FAC")){
			sqlAccount = "select cev.value, cev.xx_aux " +
								"from C_ElementValue cev " +
								"inner join M_Product_Category mpc on (cev.M_Product_Category_ID = mpc.M_Product_Category_ID) " +
								"where cev.AccountType = 'L' " +
								"and mpc.M_Product_Category_ID = " + tipoCategoria + m_where; 
		}else {
			sqlAccount = "select cev.value, cev.xx_aux " +
								"from C_ElementValue cev " +
								"inner join M_Product_Category mpc on (cev.M_Product_Category_ID = mpc.M_Product_Category_ID) " +
								"where cev.AccountType = 'A' and cev.XX_Transitional ='Y' "+
								"and mpc.M_Product_Category_ID = " + tipoCategoria + m_where; 
		}
		
		String[] account = new String[2];
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
			pstmt = DB.prepareStatement(sqlAccount, null); 
			rs = pstmt.executeQuery(); 
			if(rs.next()){
				account[0] = rs.getString(1); 
				account[1] = rs.getString(2);
			}
		}catch(Exception e){
			log.log(Level.SEVERE, sqlAccount);	
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
		return account;
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
	
	public void setXX_SynchronizationBank (int pay){
		String sql = "update c_payment set XX_SynchronizationBank ='Y' where c_payment_id = " + pay; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery(); 
			if(rs.next()){
				
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
	}
}
