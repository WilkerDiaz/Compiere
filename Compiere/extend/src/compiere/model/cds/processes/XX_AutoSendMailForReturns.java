package compiere.model.cds.processes;
 
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.MClient;
import org.compiere.model.MUser;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MCreditNotifyReturn;
import compiere.model.cds.MVLOReturnOfProduct;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_NotificationType;
import compiere.model.cds.X_XX_CreditNotifyReturn;

public class XX_AutoSendMailForReturns extends SvrProcess{
	 Ctx ctx = Env.getCtx();
	 // Crear una instancia de calendario
	 Calendar calendario = Calendar.getInstance();
	 java.util.Date now = calendario.getTime(); 
	 java.sql.Timestamp fechaActual = new java.sql.Timestamp(now.getTime()); 
	 String fechaAux = ""; 
	 Date FechaReg=new Date();
	 private static int Date_option=0;
	 private String Desc;
	 //Numero de la devolucion
	 Integer IDRegAct=0,IDRegORden=0,IDRegCurren=0, IDtaxC=0,taxID=0;
	
	//Total de Productos a regresar por Devolucion
	 Integer TotalPiecesReturn=0;
	//Numeros de dias disponibles
	 int Ndias=0;
	 //numero del mensaje
	 int menj=0;
	 //Monto del aviso de credito 
	 float amountAC=0,amountIVAAC=0,auxiva=0,rate=0;
	 String Mensaje="";
	
	@Override
	protected String doIt() throws Exception {
		boolean sendmail=false;
		sendmail=SendMailForReturn();
		if(sendmail){
			return "Correo Enviados";
		}
		return"";
	}

	@Override
	protected void prepare() {
		
	}
	
	/**
	 * Funcion 031 clausula F. Envio de Correos a Proveedores por estaus de orden de compra pendiente
	 * De existir devoluciones de chequeo pendientes por retirar: Deberán emitirse diariamente correos al 
	 * proveedor y al comprador indicando que se tienen devoluciones pendientes por retirar, en este momento se genera 
	 * un primer aviso.  Si a los 5 días no se ha retirado la mercancía se envía un segundo aviso y 
	 * un tercer aviso será enviado a los 10 días. Se debe indicar el número de piezas devueltas.
	 * @uthor Rebecca Principal
	 * Fecha: 06/03/2010
	 * 
	 **/	
	private boolean SendMailForReturn() {
		boolean ok=false;
	//	PreparedStatement pstmt=null;ResultSet rs=null;
		
		//Buscar en tabla XX_VLO_ReturnOfProduct las Devoluciones pendientes por retirar
		String SQL = "SELECT tab.VALUE, tab.XX_VLO_RETURNOFPRODUCT_ID, tab.Created as XX_DATEDEV, tab.XX_STATUS, tab.C_BPARTNER_ID, bp.Name, tab.C_ORDER_ID, " +
					 "tab.NOTIFICATIONTYPE,tab.XX_NOTIFICATIONDATE, tab.XX_NOTIFICATIONDATE2, tab.XX_NOTIFICATIONDATE3,tab.XX_DISCOUNT,  sum(det.XX_TOTALPIECES) as TotalPiecesReturn "+
					 "FROM XX_VLO_ReturnOfProduct tab, C_BPARTNER bp, XX_VLO_RETURNDETAIL det "+
					 "WHERE tab.XX_STATUS='DPR' And tab.NOTIFICATIONTYPE='E' AND tab.XX_VLO_RETURNOFPRODUCT_ID=det.XX_VLO_RETURNOFPRODUCT_ID "+
					 "AND tab.C_BPARTNER_ID= bp.C_BPARTNER_ID "+
					 //"AND tab.XX_DISCOUNT='N' "+
					 //"and tab.XX_VLO_RETURNOFPRODUCT_ID = 1000408 "+
					 //"AND tab.XX_VLO_RETURNOFPRODUCT_ID "+
					 "Group by tab.VALUE, tab.XX_VLO_RETURNOFPRODUCT_ID, tab.Created, tab.XX_STATUS, tab.C_BPARTNER_ID, bp.Name,tab.C_ORDER_ID,tab.NOTIFICATIONTYPE,tab.XX_NOTIFICATIONDATE,tab.XX_NOTIFICATIONDATE2, tab.XX_NOTIFICATIONDATE3,tab.XX_DISCOUNT";
		
		//System.out.println(SQL);
		//SQL = MRole.getDefault().addAccessSQL(SQL, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RW);
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL,null);
			ResultSet	 rs = pstmt.executeQuery(SQL);
				int i=0;
				//colocar rol pendiente  
				while(rs.next()){
					//Capturo el numero de la Devolucion 
					 IDRegAct=rs.getInt("XX_VLO_RETURNOFPRODUCT_ID");
					 //Capturo el numero de la Orden de Compra Asociada a la Devolucion
					 IDRegORden=rs.getInt("C_ORDER_ID");
					 //crear registro para Devoluciones Auxiliar
					 MVLOReturnOfProduct RegRetAct = new MVLOReturnOfProduct(getCtx(), IDRegAct, null);
					 //Capturo la cantidad de Productos total a regresar por Devolucion Asociada a una orden de Compra
					 TotalPiecesReturn=rs.getInt("TotalPiecesReturn");
					//Capturo el id del proveedor
					 int IDBP=rs.getInt("C_BPARTNER_ID");
					//tipo de contacto a buscar  
					 int Cont=0;
					//consultar el contacto de ventas del proveedor
					int User_ID_Prov =this.getAD_User_ID(IDBP,Cont);
					//Consultar departamento que realiza orden de compra
					Integer User_ID_DepOrd =this.getAD_UserDepartament_Orden(rs.getInt("C_ORDER_ID"));
					//comprador al que se le enviara el correo informandole de la devolucion
					int Buyer_ID= this.getAD_User_ID_Buyer(User_ID_DepOrd);
					//Cambiar Fecha actual para comparar con la fecha de registro de la Devolucion
					java.util.Date utilDate = new java.util.Date(); long lnMilisegundos = utilDate.getTime();  java.sql.Date date = new java.sql.Date(lnMilisegundos);
					if(rs.getDate("XX_NOTIFICATIONDATE")!=null){
						FechaReg=rs.getDate("XX_NOTIFICATIONDATE");
					}else{
						FechaReg=rs.getDate("XX_DATEDEV");
					}
					String fechactual=date.toString(),fechregistro=FechaReg.toString();
					//Envio de primer Aviso
					if((rs.getTimestamp("XX_NOTIFICATIONDATE")==null)&&(fechactual.equals(fechregistro))){
						//System.out.println("Entre aviso  1");
						Ndias=15;
						Mensaje=showSendMesagesMail(menj,Ndias,rs.getInt("Value"),IDRegORden,TotalPiecesReturn, IDBP);
						//enviar correo a proveedor y al  contacto de ventas
						sendMailIndividual(IDBP,User_ID_Prov,Mensaje,menj); 
						//Enviar Correo al contactos de Compras que genero la Orden
						sendMailIndividual(-1,Buyer_ID,Mensaje,menj); 
						ok=true;
						//actualizar fecha de Notificacion 1 
						RegRetAct.setXX_NotificationDate(fechaActual);
						//Guardos los cambios realizados
						RegRetAct.save();
					}else{
						//Envio de Segundo Aviso sumar a las fecha Notificacion 1 5 dias 
						String Notify2;Date_option=0;
						if(rs.getDate("XX_NOTIFICATIONDATE")!=null){
							Notify2=CambiarFechas(rs.getDate("XX_NOTIFICATIONDATE"),Date_option);
						}else{
							Notify2=CambiarFechas(rs.getDate("XX_DATEDEV"),Date_option);
						}
						if((rs.getTimestamp("XX_NOTIFICATIONDATE2")==null)&&(fechactual.equals(Notify2))){
							//System.out.println("Entre aviso 2");
								Ndias=10;
								Mensaje=showSendMesagesMail(menj,Ndias,rs.getInt("Value"),IDRegORden,TotalPiecesReturn, IDBP);
								//enviar correo a proveedor y al  contacto de ventas
								sendMailIndividual(IDBP,User_ID_Prov,Mensaje,menj); 
								//Enviar Correo al contactos de Compras que genero la Orden
								sendMailIndividual(-1,Buyer_ID,Mensaje,menj); 
								//actualizar fecha de Notificacion 2
								RegRetAct.setXX_NotificationDate2(fechaActual);
								//Guardos los cambios realizados
								RegRetAct.save();
								ok=true;
						}else{
							//Envio de Tercer Aviso Sumar a las fecha Notificacion 2 5 dias
							String Notify3;Date_option=0;
							if(rs.getDate("XX_NOTIFICATIONDATE2")!=null){
								Notify3=CambiarFechas(rs.getDate("XX_NOTIFICATIONDATE2"),Date_option);
							}else{
								Notify3=CambiarFechas(rs.getDate("XX_DATEDEV"),Date_option);
							}
							if((rs.getTimestamp("XX_NOTIFICATIONDATE3")==null)&&(fechactual.equals(Notify3))){
								//System.out.println("Entre aviso 3");
								Ndias=5;
								Mensaje=showSendMesagesMail(menj,Ndias,rs.getInt("Value"),IDRegORden,TotalPiecesReturn, IDBP);
								//enviar correo a proveedor y al  contacto de ventas
								sendMailIndividual(IDBP,User_ID_Prov,Mensaje,menj); 
								//Enviar Correo al contactos de Compras que genero la Orden
								sendMailIndividual(-1,Buyer_ID,Mensaje,menj); 
								//actualizar fecha de Notificacion 3
								RegRetAct.setXX_NotificationDate3(fechaActual);
								//Guardos los cambios realizados
								RegRetAct.save();
							ok=true;
							}else{
									//Validar Aplicacion de descuento por Gastos de Almacenaje  agregar a la Fecha Devolucion +15 dias
									Date_option=1;String FechDesc;
									if(rs.getDate("XX_NOTIFICATIONDATE")==null){
										FechDesc=CambiarFechas(rs.getDate("XX_DATEDEV"),Date_option);
									}else{
										FechDesc=CambiarFechas(rs.getDate("XX_NOTIFICATIONDATE"),Date_option);
										}
									Desc=rs.getString("XX_DISCOUNT");
									if((fechactual.equals(FechDesc))&&(Desc.equals("N"))){
										//System.out.println("Entre aviso descuento");
										menj=1;
										Cont=1;
										//consultar el Representante legal del proveedor
										//User_ID_Prov =this.getAD_User_ID(IDBP,Cont);
										//Cambiar el campo de Aplicacion de Descuento a Yes
										Desc="Y";
										RegRetAct.setXX_Discount(true);
										//Guardos los cambios realizados
										RegRetAct.save();
										//Generar Asiento en la tabla de Avisos de Penalizacion
										int avisoID=generaravisocredito(IDRegAct,IDRegORden);//avisoID
										//System.out.println(avisoID);
										MCreditNotifyReturn AvisoCredit= new MCreditNotifyReturn(getCtx(),avisoID, null);
										Mensaje=showSendMesagesMail(menj,Ndias,rs.getInt("Value"),IDRegORden,TotalPiecesReturn, IDBP);
										//enviar correo a proveedor y al  Representante Administrativo  del Proveedor 
										sendEmail(AvisoCredit,Mensaje,null,IDBP);
										//consultar el contacto administrativo del proveedor
										User_ID_Prov =this.getAD_User_ID(IDBP,Cont=2);
										sendEmail(AvisoCredit,Mensaje,User_ID_Prov,null);
										//Enviar Correo a cuentas por pagar
										//sendEmail(AvisoCredit,Mensaje, null,null);
										//System.out.println("Mensaje Enviado "+ Mensaje);
									}else{
										//Verificar si son 30 dias cambiar el estatus de la orden de compra a abandonada
										//Envio de Tercer Aviso Sumar a las fecha Notificacion 2 5 dias
										Date_option=2;
										String FechaAband;
										if(rs.getDate("XX_NOTIFICATIONDATE")==null){
											 FechaAband=CambiarFechas(rs.getDate("XX_DATEDEV"),Date_option);
										}else{
											FechaAband=CambiarFechas(rs.getDate("XX_NOTIFICATIONDATE"),Date_option);
											}
										Desc=rs.getString("XX_DISCOUNT");
										if((fechactual.equals(FechaAband))&&(Desc.equals("Y"))){
											//Cambiar el estado de la devolucion 
											//System.out.println("fecha Devolucion  "+fechactual+" Fecha tope Merc. Abandona "+FechaAband);
											//System.out.println("Entre aviso abandonada");
											RegRetAct.setXX_Status("ABA");
											ok=true;
											//Guardos los cambios realizados
											RegRetAct.save();
										}
								}
						}
					}
				
			}
				i++;
		  }//FIN DEL MIENTRAS
		rs.close();pstmt.close(); 		
		}
		//cierre de Resultset Devoluciones pendientes por retirar
		catch (Exception e){
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		}/**finally{
			try {
				rs.close();pstmt.close(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}*/
		return ok;
	}//fin de Envio de los Correos por Devoluciones

	
	
	/**
	 * Obtiene el AD_USER_ID Del contacto de Ventas/Administrativo/Representante Legal Traido por la Devolucion
	 */
	private Integer getAD_User_ID(Integer CBPartner, Integer TCont)
	{
		Integer AD_User_ID=0;
		String SQL="";
		// "WHERE C_BPartner_ID IN "+CBPartner;
		if(TCont.equals(0)){
			//Contacto de ventas 
			SQL = "Select AD_USER_ID FROM AD_USER " +
						 "WHERE IsActive = 'Y' AND XX_CONTACTTYPE= "+ getCtx().getContextAsInt("#XX_L_CONTACTTYPESALES")+ " AND C_BPartner_ID = "+CBPartner;
		}else{
			if(TCont.equals(1)){
				//Representante Legal 
				SQL = "Select AD_USER_ID FROM AD_USER " +
							 "WHERE IsActive = 'Y' AND XX_CONTACTTYPE= "+ getCtx().getContextAsInt("#XX_L_CONTACTTYPELEGAL")+ " AND C_BPartner_ID = "+CBPartner;
			}else if(TCont.equals(2))
			{
				//Representante Administrativo
				SQL = "Select AD_USER_ID FROM AD_USER " +
				"WHERE IsActive = 'Y' AND XX_CONTACTTYPE= "+ getCtx().getContextAsInt("#XX_L_CONTACTTYPEADMINISTRATIVE")+ " AND C_BPartner_ID = "+CBPartner;
				}
		}
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
			{
				AD_User_ID = rs.getInt("AD_USER_ID");
			}
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return AD_User_ID;
	}//fin de AD_USER de CBPartnet de la Devolucion
	
	/**
	 * * Obtiene el AD_USER_ID del Departamento que generara la Orden registrada en la Devolucion
	 * @param Orden
	 * @return
	 */
	private Integer getAD_UserDepartament_Orden(Integer Orden){
		Integer AD_User_ID_Buyer=0;
		String SQL = "SELECT ord.XX_USERBUYER_ID "+
					  "FROM C_ORDER ord "+
					  "WHERE ord.C_ORDER_ID = "+Orden;

		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				AD_User_ID_Buyer=rs.getInt("XX_USERBUYER_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
				e.printStackTrace();
			}
		
		return AD_User_ID_Buyer;
	}// fin de busqueda del comprador
	
	/**
	 * Obtiene el AD_USER_ID del CBParter del Departamento comprador que emite la orden que generola Devolucion
	  * @param User_ID_DepOrd Departamento Comprador
	 * @return AD_User_ID_Buyer Comprador
	 */
	private Integer getAD_User_ID_Buyer(Integer User_ID_DepOrd)
	{
		Integer AD_User_ID = 0;
		 
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID = "+User_ID_DepOrd;
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
			{
				AD_User_ID=rs.getInt("AD_USER_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return AD_User_ID;
	}//fin de getAD_User_ID_Buyer del Comprador de la orden de compra que genera la Devolucion
	/**
	 * Cambiar Fechas 
	 * @param dateToModify
	 * @return
	 */
	private String CambiarFechas(java.sql.Date dateToModify,int Date_option){
		String fecha = dateToModify.toString();//.getValue().toString();
		//se trunca la fecha a 10 caracteres
		fecha=fecha.substring(0, 10);
		
		  try
		    {    
	          	Date date ; 
	          	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	          	date = (Date)formatter.parse(fecha);
	          	//Calendar cal=Calendar.getInstance();
	          	//cal.setTime(date);
	          	Calendar cal = new GregorianCalendar(); 
	            cal.setTimeInMillis(dateToModify.getTime()); 
	           

	          	//Devolucion Activa Aviso 1,2,3
	          	if(Date_option==0){
	          		cal.add(Calendar.DATE,5);
	          		
	          	}else{
	          		//Devolucion Activa Aviso De Penalizacion por Gastos de Almacenaje
	          		if(Date_option==1){
	          			cal.add(Calendar.DATE,15);
	          		}else{
	          		//Cambio de Estado de la Devolucion: Abandonada 
	          			if(Date_option==2){
	          				cal.add(Calendar.DATE,30);
	          			}
	          		}
	          	}
	          	//
	          	formatter = new SimpleDateFormat("yyyy-MM-dd");
	          	// Calendar to Date Conversion
	          	int year = cal.get(Calendar.YEAR);
	          	int month = cal.get(Calendar.MONTH);
	          	int day = cal.get(Calendar.DATE);
	          	Date auxDate = new Date((year-1900), month, day);
	          	fecha=formatter.format(auxDate);
		    }
		    catch (ParseException e)
		    {
		    	log.log(Level.SEVERE, "Fecha", e);   
		    }
		return fecha;    
	}
	/**
	 * Envia correos a Proveedores, contacto y comprador
	 * @param ADUser_ID
	 * @param Mensaje
	 * @throws Exception
	 */
	private void sendMailIndividual(int BP, int ADUser_ID, String Mensaje, int msj) throws Exception
	{

		try{
			//Envios de Correos por Devoluciones pendientes por Retirar
			if(msj==0){
				Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_SENDMAILRETURNS_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , BP, ADUser_ID, null);	
				f.ejecutarMail(); 
				f = null;
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
	}
	
/**
 *  Contenido del Mensaje enviado en el Mail segun sea el caso
 */
private String showSendMesagesMail(int Nmensj, int Ndias, int NDev, int NOrden, int pieces, int vendor_id)
{
	
	//envio de mensajes por Devolucion
	if(Nmensj==0){
		MBPartner proveedor = new MBPartner(getCtx(), vendor_id, null);
		
		String Mensaje= "\n " + proveedor.getName() + " \n \n Estimados Señores \n \n"
		  +" Por medio de la presente comunicación, le informamos que tiene un total de "+ pieces
		  + " Piezas Devueltas " +"bajo la Devolucion No- "+NDev + ". El plazo para el retiro de la mercancía es de quince (15) días hábiles contados a partir de la fecha de hoy. " 
		  + "\n \n La entrega se realizará en las instalaciones de CENTROBECO, de acuerdo a lo estipulado en las condiciones impresas en la Orden de Compra correspondiente."
		  + "\n \n La persona que retire la mercancía deberá presentar Carta de Autorización que contenga la información que a continuación se indica: Membrete, firma y sello de " 
		  +	"la Compañía, nombre y cédula de identidad del autorizado y de quien autoriza, fecha idéntica al día del retiro."
		  + "\n \n Vencido el plazo para el retiro de la mercancía, CENTROBECO, C.A. descontará un cinco por ciento (5%) del valor total de la factura de los productos devueltos " 
		  +	"por concepto de gastos de almacenaje. "
		  + "\n \n Nota: Éste es un mensaje automático, por favor no responder.";
	}else{
		//envio de mensajes por generacion de una nota de aviso
		//AVISO DE CREDITO POR DEVOLUCION # del aviso           
		if(Nmensj==1){
			Mensaje=" Adjunto enviamos avisos de créditos correspondientes a retraso en retiro de devoluciones, " +
					"agradecemos nos hagan llegar las notas de créditos lo mas pronto posible. Cualquier duda estamos a la orden en cuentasporpagar@beco.com.ve.";
			
		}
	}
	return Mensaje;
} 


public int generaravisocredito(Integer IDRegAct, Integer IDRegORden ){

	PreparedStatement pstmt2 =null;ResultSet rs2=null;
	int avisoID=0;
	//System.out.println("Entre en generar aviso");
	//NewAvisoCredit.setAD_Client_ID(AD_Client_ID);  NewAvisoCredit.setAD_Org_ID(AD_Org_ID); pendiente cliente y organizacion de donde lo saco?
	//Buscar el Precio unitario de cada producto regresado
	String SQLDetailReturn = "SELECT Distinct det.M_Product_ID,det.XX_TOTALPIECES, lin.PriceActual, lin.C_TAXCATEGORY_ID, tax.C_TAX_ID, tax.RATE, tax.VALIDFROM, ord.CREATED,ord.C_CURRENCY_ID  " +
	 "FROM XX_VLO_ReturnDetail det, XX_VLO_ReturnOfProduct tab,XX_VMR_PO_LINEREFPROV lin, XX_VMR_REFERENCEMATRIX mat, C_TAXCATEGORY cat,C_Order ord, C_TAX tax  "+
	 "WHERE lin.C_TAXCATEGORY_ID=tax.C_TAXCATEGORY_ID " 
	 + "AND det.XX_VLO_RETURNOFPRODUCT_ID= "+IDRegAct
	 +" AND lin.C_Order_ID= "+IDRegORden
	 +" AND lin.C_Order_ID=ord.C_Order_ID "
	 +" AND lin.XX_VMR_PO_LINEREFPROV_ID = mat.XX_VMR_PO_LINEREFPROV_ID"
	 +" AND det.M_Product_ID = mat.M_Product "
	 +" AND lin.C_TAXCATEGORY_ID=tax.C_TAXCATEGORY_ID "
	 +" AND VALIDFROM=(SELECT MAX(VALIDFROM) FROM C_TAX  WHERE C_TAXCATEGORY_ID=lin.C_TAXCATEGORY_ID)";
	//SQL = MRole.getDefault().addAccessSQL(SQL, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RW);
	//System.out.println("Generar Aviso de credito "+SQLDetailReturn);
	try{
		MCreditNotifyReturn NewAvisoCredit= new MCreditNotifyReturn(getCtx(),0, null);
		NewAvisoCredit.setC_Order_ID(IDRegORden);
		pstmt2 = DB.prepareStatement(SQLDetailReturn,null);
		rs2 = pstmt2.executeQuery(SQLDetailReturn);
		float auxM=1;
		//colocar rol pendiente 
		while(rs2.next()){
			rate=rs2.getFloat("RATE");
			//calcular el monto neto de la devolucion 
			amountAC+=rs2.getFloat("PriceActual")*rs2.getInt("XX_TOTALPIECES") ;
			//monto auxiliar
			 auxM=rs2.getFloat("PriceActual")*rs2.getInt("XX_TOTALPIECES") ;
			 IDRegCurren=rs2.getInt("C_CURRENCY_ID");
			 IDtaxC=rs2.getInt("C_TAXCATEGORY_ID");
			 taxID=rs2.getInt("C_TAX_ID");
		}
		rs2.close();
		pstmt2.close();
		 BigDecimal MontoD;

		 MontoD = new BigDecimal(amountAC).setScale(2, BigDecimal.ROUND_UP);
		 double MontoNetoD = MontoD.doubleValue();

		/**System.out.println("Tasa  "+rate);
		System.out.println("Monto Neto Devolucion  "+amountAC);
		System.out.println("Monto Aviso  "+auxM);
		System.out.println("Moneda "+IDRegCurren);
		System.out.println("Categoria Impuesto "+IDtaxC);
		System.out.println("Impuesto "+taxID);*/
		//Setearle los valores generados el nuevo aviso de credito
		//Monto de costo de origen monto neto de los productos devueltos pendiente si  es con iva sumarle 'auxiva'
		//float amountAC+=auxiva;
		//DecimalFormat formateador = new DecimalFormat("####.###");
		//String montoD=formateador.format (amountAC);
		NewAvisoCredit.setXX_UnitPurchasePrice(new BigDecimal(MontoNetoD));
		//Monto del aviso de credito 5% del monto neto de la Devolucion 
		BigDecimal MontoAC = new BigDecimal(amountAC*0.05).setScale(2, BigDecimal.ROUND_UP); double MontoNAC = MontoAC.doubleValue();
		NewAvisoCredit.setXX_Amount(new BigDecimal(MontoNAC)); 
		//Monto del iva a partir del monto del aviso de credito
		double iva= ((amountAC*0.05)*rate/100);
		BigDecimal MontoIAC = new BigDecimal(iva).setScale(2,BigDecimal.ROUND_UP); double MontoNIva = MontoIAC.doubleValue();
		NewAvisoCredit.setXX_Amount_IVA(new BigDecimal(MontoNIva));
		//estatus
		NewAvisoCredit.setXX_Status("ACT");
		//factor de cambio
		NewAvisoCredit.setXX_ChangeFactor(new BigDecimal(1));
		//moneda
		NewAvisoCredit.setC_Currency_ID(IDRegCurren);		
		//categoria del impuesto
		NewAvisoCredit.setC_TaxCategory_ID(IDtaxC);
		//impuesto
		NewAvisoCredit.setC_Tax_ID(taxID);
		//tipo de aviso
		NewAvisoCredit.setXX_NotificationType(X_Ref_XX_NotificationType.GASTOSDEALMACENAJE.getValue()); 
		//tipo de aviso
		NewAvisoCredit.setDocumentType(new Integer(1));
		//descripcion del aviso de credito
		NewAvisoCredit.setDescription("Descuento por Gastos de Almacenaje Correspodiente a Devolucion");
		NewAvisoCredit.save();
		if(NewAvisoCredit.save()){
				//boolean save=true;
			    //System.out.println("Aviso de Credito Generado "+save);
				avisoID=NewAvisoCredit.getXX_CreditNotifyReturn_ID();}
		}catch (Exception e){
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
			//System.out.println("error" +e.getMessage());
		}finally{
			try {
				rs2.close();pstmt2.close();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	return avisoID;
	
}// finde generar aviso
/**
 * Method that generate the Debt or Credit Notify Report
 * and send an email to BPartner Vendor
 * @return
 */
private String sendEmail(MCreditNotifyReturn NewAvisoCredit, String Mensaje, Integer AD_User_ID, Integer BP)
{
	int CreditNotifyReturn_ID = NewAvisoCredit.getXX_CreditNotifyReturn_ID();//getC_Invoice_ID();
	log.info("CreditNotifyReturn_ID=" + CreditNotifyReturn_ID);
	if (CreditNotifyReturn_ID < 1)
		throw new IllegalArgumentException("@NotFound@ @MCreditNotifyReturn_ID@");
	
	// Obtain the Active Record of M_Invoice Table preguntar a Paty 
	Query q = new Query("XX_CreditNotifyReturn");
	q.addRestriction("XX_CREDITNOTIFYRETURNVIEW_ID", Query.EQUAL, Integer.valueOf(CreditNotifyReturn_ID));
	int table_ID = X_XX_CreditNotifyReturn.Table_ID;
	String subject="Aviso  Credito";
	MPrintFormat f = null ;
	//System.out.println("Asunto "+subject);
	// Create the Process Info Instance to generate the report
	PrintInfo i = new PrintInfo("Aviso  Credito", table_ID, CreditNotifyReturn_ID, 0);
	//realizar keiname info para el reporte si descripcion almacenaje
	if(NewAvisoCredit.getDocumentType()==1){
		//getDescription().compareTo("Descuento por Gastos de Almacenaje Correspodiente a Devolucion")==0){
		//generar reporte por gastos de almacenaje	1000349
		f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_REPORT_CNGASTALMDEV_ID"), false);
	}
	// Create the report
	ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
	// Generate the PDF file
	// Generate the PDF file
	File attachment = null;		
	try
	{
		attachment = File.createTempFile("aviso Credito", ".pdf");
		re.getPDF(attachment);
	}
	catch (Exception e)
	{
		log.log(Level.SEVERE, "", e);
	}
	String to ="";
	// Send the PDF File by E-Mail proveedor
	if(AD_User_ID!=null){
			org.compiere.model.MUser userTo = MUser.get(getCtx(),AD_User_ID );//get(ctx, ctx.getAD_User_ID());//User
			//Integer ejemplo=1019249;
			to = userTo.getEMail();
			//System.out.println("Correo a "+to);
	}
	if(BP!=null){
		to=getMail_ID_BP(BP);
		//System.out.println("Correo a "+to);
	}
	if(AD_User_ID==null &&BP==null){
		to="cuentasporpagar@beco.com.ve";
	}
	MClient m_client = MClient.get(ctx);
	EMail email = m_client.createEMail(to, null,subject,Mensaje);
	if (email != null)
	{

		//	Attachment
		if (attachment != null && attachment.exists())
			email.addAttachment(attachment);
		
		String status = email.send();
		log.info("Email Send status: " + status);
		
		//if (m_user != null)
		//	new MUserMail(m_user, m_user.getAD_User_ID(), email).save();
		if (email.isSentOK())
		{
			//System.out.println("Mail enviado");
			return "Mail Sent sucessfully";
		}
		else
			return "Mail cannot be Sent";
	}
	else
		return "Cannot create mail";
}//Fin del envio de Correos con Parametros
	
	//Buscar Correo Proveedor y Cuentas por Cobrar
	private String getMail_ID_BP(Integer BP)
	{
		String Mail_ID_BP ="";
		 
		String SQL = "Select XX_VENDOREMAIL FROM C_BPARTNER " +
					 "WHERE C_BPartner_ID = "+BP;
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
			{
				Mail_ID_BP=rs.getString("XX_VENDOREMAIL");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return Mail_ID_BP;
	}//fin de getMail_ID_BP 
	//Cuentas por Cobrar
}
