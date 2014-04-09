package compiere.model.cds.processes;
 
import java.awt.Container;
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.MClient;
import org.compiere.model.MCountry;
import org.compiere.model.MCurrency;
import org.compiere.model.MPInstance;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_C_Order;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.cds.MPaymentTerm;
import compiere.model.cds.MProduct;
import compiere.model.cds.MUser;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.cds.X_Ref_XX_Ref_ContactType;
import compiere.model.cds.X_XX_VMR_PO_ApprovalRole;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Package;


public class XX_ApprovePO extends SvrProcess{
	
	private static boolean processActive = false;

	/**
	 * 	Is Process Active
	 *	@return true if active
	 */
	protected static boolean isProcessActive()
	{
		return processActive;
	}

	/**
	 * 	Set Process (in)active
	 *	@param active active
	 */
	protected static void setProcessActive (boolean active)
	{
		processActive = active;
		if(!active) Env.getCtx().setContext("#WAITPROCESS",-1); //AGREGADO POR GHUCHET
		else Env.getCtx().setContext("#WAITPROCESS",1);
	}
	
	@Override
	protected String doIt() throws Exception {
		
		if(isProcessActive()){
			setProcessActive(false);
			return "";
	    }
			
		setProcessActive(true);
		
		MOrder orden = new MOrder(getCtx(),getRecord_ID(),get_TrxName());
		
		if (alreadyPreaprobed(orden)){
			setProcessActive(false);
			return "";
		}
		
		if (sitmeConAsociadas(orden)){
			setProcessActive(false);
			return "";
		}
				
		if(orden.getXX_OrderStatus().equalsIgnoreCase("PRO") && !orden.getXX_ComesFromSITME()){
			
			//Calcula los limites siempre que intenta aprobar la OC
			XX_PO_OrderReadyEmail.POLimits(orden);
			
			/*
			 * Victor Lo Monaco	
			 */
			// en primer lugar buscamos en todos los limites de la orden su mayor porcentaje de exceso
			String SQL_limits = "SELECT max(XX_PercentageExcess) " +
								"FROM XX_VMR_PO_Approval " +
								"WHERE C_ORDER_ID = "+orden.get_ID();
	
			PreparedStatement pstmt_aux2 = DB.prepareStatement(SQL_limits, null); 
			ResultSet rs_aux2 = pstmt_aux2.executeQuery();
		
			BigDecimal porcentajeExcesoMax = BigDecimal.ZERO; //
	
			// aca busco el mayor de todos los porcentajes en exceso
			if(rs_aux2.next())
			{
				if((rs_aux2.getBigDecimal(1))!=null)
					porcentajeExcesoMax = rs_aux2.getBigDecimal(1);
			}
			rs_aux2.close();
			pstmt_aux2.close();
			
			if(porcentajeExcesoMax.compareTo(BigDecimal.ZERO)<0)
				porcentajeExcesoMax = BigDecimal.ZERO;
			
			
			/*
			 * Roles de Aprobación Reloaded by: JTrias
			 */
			
			X_XX_VMR_PO_ApprovalRole approvalRole = getApprovalRole();
	
			if(approvalRole!=null){ //Si el rol que intenta aprobar se encuentra en la tabla de roles de aprobacion
				
				if(approvalRole.getXX_RankLow().compareTo(porcentajeExcesoMax)<=0 && approvalRole.getXX_RankHigh().compareTo(porcentajeExcesoMax)>=0){ //Si esta dentro del rango de aprobacion
					
					if(!approvalRole.isXX_NeedsOtherRole()){ // Si no necesito otro Rol, aprueba
						
						String SQL_Update = "UPDATE XX_VMR_PO_Approval " +
											"SET IsApproved='Y'" +
											"WHERE C_ORDER_ID = "+orden.get_ID();
					

					    DB.executeUpdate(null,SQL_Update);

					    //Cierra tarea critica de O/C con sobregiro de haber
					    closeCriticalTask(orden,"AO1");
						
					  //GMARQUES - Se coloca en la OC que no espera por nadie
//						orden.setXX_CT_Responsible_ID(0);
//						orden.save();
					    String respCT = " UPDATE C_ORDER set XX_CT_Responsible_ID=null  " +
								"WHERE C_ORDER_ID = "+orden.get_ID();
					    DB.executeUpdate(null, respCT);
					}else
					{ //Si necesito otro Rol
						
						//Si no se ha aprobado por primera vez le asigno el primer gerente y luego digo que necesita otro gerente
						if(orden.getXX_FirstAppManager_ID()==0){
							
							//Mensaje para indicar intento de aprobacion previo
							X_AD_User user = new X_AD_User( getCtx(), getCtx().getAD_User_ID(), null);
							orden.setXX_Alert2(Msg.getMsg(Env.getCtx(), "XX_OverdrawnOrder", new String[] {user.getName()}));
							orden.setXX_FirstAppManager_ID(getAD_User_ID());
														
							//GHUCHET Si el rol responsable es igual al rol actual, se actualiza el siguiente responsable
							//en caso contrario no se modifica el responsable. 
							if(Env.getCtx().getAD_Role_ID()==orden.getXX_CT_Responsible_ID() || orden.getXX_CT_Responsible_ID() == 0  ){
								//GMARQUES - Se coloca en la OC por quién está esperando
								orden.setXX_CT_Responsible_ID(approvalRole.getXX_NotifyingRole_ID());
								orden.save();
								commit();
							}
							//FIN GHUCHET
							
							ADialog.info(1, new Container(), "XX_NotifyOtherManager");
							setProcessActive(false);
							return "";
						}
						else{
							//Si no es la primera vez que aprueba un gerente verifico si es el mismo que la volvio a aprobar
							
							if(orden.getXX_FirstAppManager_ID()!=getAD_User_ID() || orden.getXX_OrderStatus().equals("PEN")){ // Si no es el mismo, aprueba
								String SQL_Update = "UPDATE XX_VMR_PO_Approval " +
								 "SET IsApproved='Y' " +
								 "WHERE C_ORDER_ID = "+orden.get_ID();
				
						    	DB.executeUpdate(null, SQL_Update);
							    
							    //Cierra tarea critica de O/C con y sin sobregiro de haber
							    closeCriticalTask(orden,"AO1");
							    
							    //GMARQUES - Se coloca en la OC que no espera por nadie
//								orden.setXX_CT_Responsible_ID(0);
//								orden.save(); 
							    String respCT = " UPDATE C_ORDER set XX_CT_Responsible_ID=null  " +
								"WHERE C_ORDER_ID = "+orden.get_ID();
							    DB.executeUpdate(null, respCT);
						    }
							else{
								//Si es el mismo primer gerente le vuelve a decir que necesita otro gerente
						    	ADialog.info(1, new Container(), "XX_NotifyOtherManager");
						    	setProcessActive(false);
								return "";
						    }	
						}
						
					}
					
				}else{ //Si no esta dentro de su rango de aprobacion, le enviamos un correo al siguiente rol
						
				    //Crea la tarea critica de O/C con sobregiro
				    createCriticalTaskAO1(orden);

					X_AD_User user = new X_AD_User( getCtx(), getCtx().getAD_User_ID(), null);
					orden.setXX_Alert2(Msg.getMsg(Env.getCtx(), "XX_OverdrawnOrder", new String[] {user.getName()}));
					
					
					//GHUCHET Si el rol responsable es igual al rol actual, se actualiza el siguiente responsable
					//en caso contrario no se modifica el responsable. 
					if(Env.getCtx().getAD_Role_ID()==orden.getXX_CT_Responsible_ID()  || orden.getXX_CT_Responsible_ID() == 0  ){
						//GMARQUES - Se coloca en la OC por quién está esperando
						orden.setXX_CT_Responsible_ID(approvalRole.getXX_NotifyingRole_ID());
						orden.save();
						commit();
					}
					//FIN GHUCHET
					
					ADialog.info(1, new Container(), "XX_AccesDenied");
					setProcessActive(false);
					return "Acces Denied";
				}
				
			}else{
				ADialog.info(1, new Container(), "XX_NotPermissionApprovePO");
				setProcessActive(false);
				return Msg.getMsg(Env.getCtx(), "XX_NotPermissionApprovePO");
			}			
		}
		
		//Fin de Roles de Aprobacion
		
		boolean allDefinitive=true;
		if(orden.getXX_VLO_TypeDelivery().compareTo("DD")==0){
			
			String SQL_aux = "SELECT XX_IsDefinitive " +
							 "FROM XX_VMR_PO_LINEREFPROV " +
							 "WHERE C_ORDER_ID = "+orden.get_ID();
	
			PreparedStatement pstmt_aux = DB.prepareStatement(SQL_aux, null); 
		    ResultSet rs_aux = pstmt_aux.executeQuery();
		   
		    while(rs_aux.next())
		    {
		    	if(rs_aux.getString("XX_IsDefinitive").equals("N")){
		    		allDefinitive=false;
		    	}
			    
		    }
		    rs_aux.close();
		    pstmt_aux.close();
	    }
	    
	    boolean enter = true;
	    if(orden.getXX_VLO_TypeDelivery().compareTo("DD")==0 && !allDefinitive){
	    	enter=false;
	    }
	    
		if(enter==true){
		
			Integer orderID = getRecord_ID();
			int limitTotal = 0; 
			int dpto = 0;
			int line = 0;
			int sec = 0;
			int dptoid = 0;
			int lineid = 0;
			int secid = 0;
			int conteo = 0;
			int conteoY = 0;
			int cantidad = 0;
			int montoComNac = 0;
			int montoComInt = 0;
			int cantCompNac = 0;
			int cantCompInt = 0;
			int montoTotalNac = 0;
			int montoTotalInt = 0;
			int cantidaTotalNac = 0;
			int cantidaTotalInt = 0;
			int cantidaLine = 0;
			int cantidaRef = 0;
			
			String yearActual = null;
			
			/*SimpleDateFormat formatoYa = new SimpleDateFormat("dd/MM/yyyy");
			yearActual = formatoYa.format(fechaActual);*/
			
			Calendar cal = Calendar.getInstance();
	        
	        int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DATE);
			int hour = cal.get(Calendar.HOUR);
			int min = cal.get(Calendar.MINUTE);
			int sec1 = cal.get(Calendar.SECOND);
			int nano = cal.get(Calendar.SECOND);
	        
	        Timestamp XXExitDate = new Timestamp(year-1900,month,day,hour,min,sec1,nano);
			
			//BigDecimal priceListPO = new BigDecimal(1);
		
			
			String SQL12 = ("SELECT COUNT (*) AS CONTEO FROM XX_VMR_PO_APPROVAL WHERE C_ORDER_ID = '"+orderID+"'"); 
			
			System.out.println(SQL12);
			PreparedStatement pstmt12 = DB.prepareStatement(SQL12, null); 
		    ResultSet rs12 = pstmt12.executeQuery();
	
		    if(rs12.next())
		    {
		    	conteo = rs12.getInt("CONTEO");
	
		    	String SQL13 = ("SELECT COUNT (*) AS CONTEOY FROM XX_VMR_PO_APPROVAL WHERE C_ORDER_ID = '"+orderID+"' AND ISAPPROVED = 'Y'"); 
				PreparedStatement pstmt13 = DB.prepareStatement(SQL13, null); 
			    ResultSet rs13 = pstmt13.executeQuery();
			    
			    if(rs13.next())
			    {
			    	conteoY = rs13.getInt("CONTEOY");
			    }
			    rs13.close();
			    pstmt13.close();
			    
		    }
		    rs12.close();
		    pstmt12.close();
		    
		    
		    String SQL21 = ("SELECT COUNT (A.XX_VMR_PO_LINEREFPROV_ID) AS CONTEOLINE " +
		    		"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B " +
		    		"WHERE A.C_ORDER_ID = '"+orderID+"' " +
		    		"AND A.C_ORDER_ID = B.C_ORDER_ID ");
		    
		    PreparedStatement pstmt21 = DB.prepareStatement(SQL21, null); 
		    ResultSet rs21 = pstmt21.executeQuery();
		    
		    if(rs21.next())
		    {
		    	cantidaLine = rs21.getInt("CONTEOLINE");
		    	
		    	String SQL22 = ("SELECT COUNT (A.XX_VMR_PO_LINEREFPROV_ID) AS CONTEOREF " +
		    			"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B " +
		    			"WHERE A.C_ORDER_ID = '"+orderID+"' " +
		    			"AND  A.XX_ReferenceIsAssociated = 'Y' " +
		    			"AND A.C_ORDER_ID = B.C_ORDER_ID ");
		    	
		    	PreparedStatement pstmt22 = DB.prepareStatement(SQL22, null); 
			    ResultSet rs22 = pstmt22.executeQuery();
			    
			    if(rs22.next())
			    {
			    	cantidaRef = rs22.getInt("CONTEOREF");
			    }
			    rs22.close();
			    pstmt22.close();
		    	
		    }
		    rs21.close();
		    pstmt21.close();
	 
	
		    if((conteo == conteoY || orden.getXX_ComesFromSITME()) && (conteo !=0 || orden.getXX_ComesFromSITME()) && cantidaLine == cantidaRef && cantidaLine !=0 && orden.getXX_OrderType().equals("Nacional"))
		    {
		    			    		
		    		String SQL16 = ("SELECT DISTINCT C.VALUE AS XX_DEPARTMENTCODE, D.VALUE AS XX_LINECODE, E.VALUE AS XX_SECTIONCODE, " +
		    				"C.XX_VMR_DEPARTMENT_ID AS DPTO, D.XX_VMR_LINE_ID AS LINE, E.XX_VMR_SECTION_ID AS SEC " +
				    		"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B, XX_VMR_DEPARTMENT C, XX_VMR_LINE D, XX_VMR_SECTION E, XX_VMR_PO_APPROVAL F " +
				    		"WHERE A.C_ORDER_ID = B.C_ORDER_ID " +
				    		"AND A.XX_VMR_DEPARTMENT_ID = C.XX_VMR_DEPARTMENT_ID " +
				    		"AND B.XX_VMR_LINE_ID = D.XX_VMR_LINE_ID " +
				    		"AND B.XX_VMR_SECTION_ID = E.XX_VMR_SECTION_ID " +
				    		"AND A.C_ORDER_ID = F.C_ORDER_ID " +
				    		"AND A.C_ORDER_ID = '"+orderID+"'");
			    
				    PreparedStatement pstmt16 = DB.prepareStatement(SQL16, null); 
				    ResultSet rs16 = pstmt16.executeQuery();
				    
				   		    
			    while(rs16.next())
			    {
			    	dpto = rs16.getInt("XX_DEPARTMENTCODE");
			    	line = rs16.getInt("XX_LINECODE");
			    	//sec1 = rs16.getInt("XX_SECTIONCODE");
			    	sec = rs16.getInt("XX_SECTIONCODE");
			    	
			    	dptoid = rs16.getInt("DPTO");
			    	lineid = rs16.getInt("LINE");
			    	secid = rs16.getInt("SEC");
			    	
					// Busco el valor del limite de la compra (OJO LUEGO CAMBIAR XX_LIMIT POR XX_LIMITTOTAL)
					// por departamento linea y seccion de la orden, ese limite hay que sumarlo al monto  de compras colocadas Nac o Int 
					String SQL10 = ("SELECT DISTINCT (C.XX_LIMITTOTAL) AS SUMA " +
							"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B, XX_VMR_PO_APPROVAL C " +
							"WHERE A.C_ORDER_ID = B.C_ORDER_ID AND A.C_ORDER_ID = C.C_ORDER_ID " +
							"AND A.C_ORDER_ID = '"+orderID+"' AND C.XX_VMR_DEPARTMENT_ID = '"+dptoid+"' " +
							"AND C.XX_VMR_LINE_ID = '"+lineid+"' AND C.XX_VMR_SECTION_ID = '"+secid+"'");
					
					PreparedStatement pstmt10 = DB.prepareStatement(SQL10, null); 
				    ResultSet rs10 = pstmt10.executeQuery();
				    
				
				    if (rs10.next())
				    {
				    	limitTotal = rs10.getInt("SUMA");
	
				    	
				    	//Busco la cantidad de de los producto asociados a esa orden para sumarlos a la cantidad de
				    	// compra colocadas Nac o Int por departamento linea y seccion
				    	String SQL11 = ("SELECT SUM(DISTINCT(B.QTY)) AS CANTIDAD " +
				    			"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B, XX_VMR_PO_APPROVAL C " +
				    			"WHERE A.C_ORDER_ID = B.C_ORDER_ID AND A.C_ORDER_ID = C.C_ORDER_ID " +
				    			"AND A.C_ORDER_ID = '"+orderID+"' AND C.XX_VMR_DEPARTMENT_ID = '"+dptoid+"' " +
				    			"AND C.XX_VMR_LINE_ID = '"+lineid+"' AND C.XX_VMR_SECTION_ID = '"+secid+"'");
				    
				    	PreparedStatement pstmt11 = DB.prepareStatement(SQL11, null); 
					    ResultSet rs11 = pstmt11.executeQuery();
					    
					    if(rs11.next())
					    {
					    	cantidad = rs11.getInt("CANTIDAD");
					    	
					    }
					    rs11.close();
					    pstmt11.close();
				    }
				    rs10.close();
				    pstmt10.close();
		
		
				        // Busco las cantidades viejas nac para despues sumarle el limite y la cantidad
					    String SQL17 = ("SELECT DISTINCT XX_NUMNACSHOPPINGPLACED AS CANT, XX_AMOUNTPLACEDNACCAMPRA AS MONTO " +
					    		"FROM XX_VMR_PRLD01 " +
					    		"WHERE XX_VMR_DEPARTMENT_ID = '"+dptoid+"' AND XX_VMR_LINE_ID = '"+lineid+"' " +
					    		"AND XX_VMR_SECTION_ID = '"+secid+"' ");
					    
					    PreparedStatement pstmt17 = DB.prepareStatement(SQL17, null); 
					    ResultSet rs17 = pstmt17.executeQuery();
					    
					    if(rs17.next())
					    {
					    	montoComNac = rs17.getInt("MONTO");
					    	cantCompNac = rs17.getInt("CANT");
					    	cantidaTotalNac = cantidad + cantCompNac;
					    	montoTotalNac = limitTotal + montoComNac;
					    	
					    }
					    rs17.close();
					    pstmt17.close(); 	
			    
					    SimpleDateFormat formatoYM = new SimpleDateFormat("yyyyMM");
						String yearMonth = formatoYM.format(orden.getXX_EstimatedDate());
					    
					    //Modifico las cantidades y los montos en el presupuesto al respectivo dep, linea y sec
				    	String SQL14 = ("UPDATE XX_VMR_PRLD01 " +
				    			"SET XX_NUMNACSHOPPINGPLACED = '"+cantidaTotalNac+"' , " +
				    			"XX_AMOUNTPLACEDNACCAMPRA = '"+montoTotalNac+"' " +
				    			"WHERE XX_VMR_DEPARTMENT_ID = '"+dptoid+"' AND XX_VMR_LINE_ID = '"+lineid+"' " +
				    			"AND XX_VMR_SECTION_ID = '"+secid+"' " +
				    			"AND XX_BUDGETYEARMONTH=" + yearMonth);
				    	
					    DB.executeUpdate(get_Trx(),SQL14);
					    				    				    
				    } // end while Nacional
			    	rs16.close();
			    	pstmt16.close();			    
				    

			    if (OrdenAprobada (orderID, XXExitDate)){
			    	
			    	//Realizado por Rosmaira Arvelo			    
				    Integer count = getPendingLabelCount(orden.get_ID());
				    			    	
				    if((orden.isXX_Alert8()==false)&&(count!=0))
					{
				    	try
				    	{
							//Generacion de Alerta de Tarea Critica para Pedidos pendientes por etiquetar		
							Env.getCtx().setContext("#XX_TypeAlertEP","EP");
							Env.getCtx().setContext("#XX_OrderEPCT",orden.get_ID());
							
							generatedAlert(orden.get_ID());
				    	}
						catch(Exception e)
						{
							log.log(Level.SEVERE,e.getMessage());
						}
				
		
						// Envio el Correo de que la orden de compra esta Aprobada
						    
						String Attachment = null;
						//EnviarCorreosCambioStatusBPartner(orden.getC_BPartner_ID(),orden.getDocumentNo(), Attachment);
						EnviarCorreosCambioStatusCoor(orden, Attachment);
						
					}//fin RArvelo
				    
				    // Envia el correo indicando que la orden de despacho directo a sido aprobada y esta pendiente imprimir etiquetas
				    if(orden.getXX_VLO_TypeDelivery().compareTo("DD")==0){ //DIRECTO A TIENDA
				    	enviarCorreoCoordChequeo(orden);
				    	enviarCorreoPlanificacion(orden);
				    }
					// Envio el Correo de que la orden de compra esta Aprobada
					    
					String Attachment = null;
					//EnviarCorreosCambioStatusBPartner(orden.getC_BPartner_ID(),orden.getDocumentNo(), Attachment);
					EnviarCorreosCambioStatusCoor(orden, Attachment);
					
					/*
					 * JPIRES - ENVIA CORREO AL PROVEEDOR CON EL ADJUNTO DE OC
					 * */
	/**				if(!orden.getXX_ComesFromSITME())
						sendEmailPOtoVendor(orden);
		*/			
					orden.load(get_Trx()); //Jessica Mendoza
					commit();
					//ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_POApprove", new String[] {orden.getDocumentNo()}));
					
					/****Jessica Mendoza****/
					/****Orden de Compra Nacional****/
					/****Proceso para crear la OC en la tabla de Estimacion de Cuentas por Pagar, una vez aprobada****/
				    if (orden.getXX_OrderStatus().equals("AP") && orden.getXX_ApprovalDate() != null){
						Utilities util = new Utilities();
				    	int diasBeneficio = 0;
				    	int diasTotalesI = 0;
				    	int diasTotalesII = 0;
				    	int diasTotalesIII = 0;
				    	Vector<Integer> condicionPago = new Vector<Integer>(6);
				    	Calendar calendarEstimadaI = Calendar.getInstance();
				    	Calendar calendarEstimadaII = Calendar.getInstance();
				    	Calendar calendarEstimadaIII = Calendar.getInstance();
				    	Timestamp fechaEstimada = new Timestamp(0);
						Timestamp fechaEstimadaS = new Timestamp(0);
						Timestamp fechaEstimadaT = new Timestamp(0);
						Vector<Timestamp> vector = new Vector<Timestamp>(3);
				    	MBPartner vendor = new MBPartner(Env.getCtx(), orden.getC_BPartner_ID(), null);	    	
							
						String sqlEst = "select * " +
										"from XX_VCN_EstimatedAPayable where C_Order_ID = " + orden.getC_Order_ID();
						PreparedStatement pstmtEst = null;
						ResultSet rsEst = null;
						try{
							pstmtEst = DB.prepareStatement(sqlEst, null);
							rsEst = pstmtEst.executeQuery();
							
							if (!rsEst.next()){
								BigDecimal subTotal1 = new BigDecimal(0);
								BigDecimal subTotal2 = new BigDecimal(0);
								diasBeneficio = util.benefitVendorDayAdvance(orden.getC_BPartner_ID());
								condicionPago = util.infoCondicionPago(orden.getC_PaymentTerm_ID());					
	
								if (orden.getXX_OrderType().equals("Nacional")){
									diasTotalesI = condicionPago.get(1) - diasBeneficio;
									diasTotalesII = condicionPago.get(4) - diasBeneficio;
									diasTotalesIII = condicionPago.get(7) - diasBeneficio;
								}else{
									diasTotalesI = condicionPago.get(1);
									diasTotalesII = condicionPago.get(4);
									diasTotalesIII = condicionPago.get(7);
								}
								
								vector = util.calcularFecha(orden.getC_PaymentTerm_ID(),orden.getC_Order_ID(),"estimada");						
								if (vector.get(0) != null){
									fechaEstimada = vector.get(0);
									calendarEstimadaI.setTimeInMillis(fechaEstimada.getTime());
								    calendarEstimadaI.add(Calendar.DATE, diasTotalesI);
								    fechaEstimada = new Timestamp(calendarEstimadaI.getTimeInMillis());	
								}
								if (vector.get(1) != null){
									fechaEstimadaS = vector.get(1);
									calendarEstimadaII.setTimeInMillis(fechaEstimadaS.getTime());
								    calendarEstimadaII.add(Calendar.DATE, diasTotalesII);
								    fechaEstimadaS = new Timestamp(calendarEstimadaII.getTimeInMillis());
								}
								if (vector.get(2) != null){
									fechaEstimadaT = vector.get(3);
									calendarEstimadaIII.setTimeInMillis(fechaEstimadaT.getTime());
								    calendarEstimadaIII.add(Calendar.DATE, diasTotalesIII);
								    fechaEstimadaT = new Timestamp(calendarEstimadaIII.getTimeInMillis());
								}

								Vector<Integer> vecI = new Vector<Integer>(9);
								Vector<String> vecS = new Vector<String>(2);
								vecI.add(orden.getC_Order_ID());
								vecI.add(orden.getC_BPartner_ID());
								vecI.add(orden.getC_PaymentTerm_ID());
								vecI.add(orden.getC_Currency_ID());
								if (vendor.getXX_VendorType_ID() != null)
									vecI.add(vendor.getXX_VendorType_ID());
								else
									vecI.add(0);
								vecS.add(orden.getXX_POType());
								vecI.add(orden.getXX_VMR_DEPARTMENT_ID());
								vecS.add(orden.getXX_OrderType());
								vecI.add(orden.getC_Country_ID());
								vecI.add(orden.getXX_Category_ID()); 
								vecI.add(orden.getXX_ImportingCompany_ID());
										
								if (condicionPago.get(0).compareTo(100) == 0){ 
									util.crearEstimacion(vecI, vecS, orden.getGrandTotal(),fechaEstimada,"Orden","100% - Mercancia", 0);
								}else{
									BigDecimal total = new BigDecimal(0);
									if (orden.getXX_POType().equals("POM")){
										total = orden.getTotalLines();
									}else{
										total = orden.getGrandTotal();
									}
									if (condicionPago.get(6).compareTo(0) != 0){
										subTotal1 = orden.getTotalLines().multiply(new BigDecimal(condicionPago.get(0))).divide(new BigDecimal(100));
										subTotal2 = orden.getTotalLines().multiply(new BigDecimal(condicionPago.get(3))).divide(new BigDecimal(100));
										util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",condicionPago.get(0).toString()+"% - Mercancia", 0);
										util.crearEstimacion(vecI, vecS, subTotal2,fechaEstimadaS,"Orden",condicionPago.get(3).toString()+"% - Mercancia", 0);
										util.crearEstimacion(vecI, vecS, total.subtract(subTotal1.add(subTotal2)),fechaEstimadaT,"Orden",
												condicionPago.get(6).toString()+"% - Mercancia", 0);
									}else{
										subTotal1 = orden.getTotalLines().multiply(new BigDecimal(condicionPago.get(0))).divide(new BigDecimal(100));
										util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",condicionPago.get(0).toString()+"% - Mercancia", 0);
										util.crearEstimacion(vecI, vecS, total.subtract(subTotal1),fechaEstimadaS,"Orden",
												condicionPago.get(3).toString()+"% - Mercancia", 0);
									}
								}
							}
						}catch (SQLException e){
							log.log(Level.SEVERE, sqlEst, e);
						}finally{
							try {
								rsEst.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								pstmtEst.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
				    }
					/****Fin codigo - Jessica Mendoza***/
					ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_POApprove", new String[] {orden.getDocumentNo()}));

			    }else{ //Jessica Mendoza
			    	get_TrxName().rollback();
			    	setProcessActive(false);
			    	return Msg.getMsg(Env.getCtx(), "XX_InvalidPurchaseOrder");
			    }
		    }// end gran if  
		    
		    //VERIFICO QUE TODOS LOS LIMITES ENTEN APROVADOS SOLAMENTE (APLICA SOLO PARA LAS INTERNACIONALES)
	
		    else if(conteo == conteoY && orden.getXX_OrderType().equals("Importada") && orden.getXX_OrderStatus().equals("PRO") && conteo != 0)
		    {
			    	//System.out.println("para las internacionales");
			  			    	
			    	String SQL16 = ("SELECT DISTINCT C.VALUE AS XX_DEPARTMENTCODE, D.VALUE AS XX_LINECODE, E.VALUE AS XX_SECTIONCODE, " +
		    				"C.XX_VMR_DEPARTMENT_ID AS DPTO, D.XX_VMR_LINE_ID AS LINE, E.XX_VMR_SECTION_ID AS SEC " +
				    		"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B, XX_VMR_DEPARTMENT C, XX_VMR_LINE D, XX_VMR_SECTION E, XX_VMR_PO_APPROVAL F " +
				    		"WHERE A.C_ORDER_ID = B.C_ORDER_ID " +
				    		"AND A.XX_VMR_DEPARTMENT_ID = C.XX_VMR_DEPARTMENT_ID " +
				    		"AND B.XX_VMR_LINE_ID = D.XX_VMR_LINE_ID " +
				    		"AND B.XX_VMR_SECTION_ID = E.XX_VMR_SECTION_ID " +
				    		"AND A.C_ORDER_ID = F.C_ORDER_ID " +
				    		"AND A.C_ORDER_ID = '"+orderID+"'");
			
				    
				    PreparedStatement pstmt16 = DB.prepareStatement(SQL16, null); 
				    ResultSet rs16 = pstmt16.executeQuery();
				    
				    while(rs16.next())
				    {
				    	dpto = rs16.getInt("XX_DEPARTMENTCODE");
				    	line = rs16.getInt("XX_LINECODE");
				    	//sec1 = rs16.getInt("XX_SECTIONCODE");
				    	sec = rs16.getInt("XX_SECTIONCODE");
				    	
				    	dptoid = rs16.getInt("DPTO");
				    	lineid = rs16.getInt("LINE");
				    	secid = rs16.getInt("SEC");
				    
						// Busco el valor del limite de la compra (OJO LUEGO CAMBIAR XX_LIMIT POR XX_LIMITTOTAL)
						// por departamento linea y seccion de la orden, ese limite hay que sumarlo al monto  de compras colocadas Nac o Int 
						String SQL10 = ("SELECT DISTINCT (C.XX_LIMITTOTAL) AS SUMA " +
								"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B, XX_VMR_PO_APPROVAL C " +
								"WHERE A.C_ORDER_ID = B.C_ORDER_ID AND A.C_ORDER_ID = C.C_ORDER_ID " +
								"AND A.C_ORDER_ID = '"+orderID+"' AND C.XX_VMR_DEPARTMENT_ID = '"+dptoid+"' " +
								"AND C.XX_VMR_LINE_ID = '"+lineid+"' AND C.XX_VMR_SECTION_ID = '"+secid+"'");
						
						PreparedStatement pstmt10 = DB.prepareStatement(SQL10, null); 
					    ResultSet rs10 = pstmt10.executeQuery();
					    
					    //System.out.println(SQL10);
					    
					    if (rs10.next())
					    {
					    	limitTotal = rs10.getInt("SUMA");
		
					    	
					    	//Busco la cantidad de de los producto asociados a esa orden para sumarlos a la cantidad de
					    	// compra colocadas Nac o Int por departamento linea y seccion
					    	String SQL11 = ("SELECT SUM(DISTINCT(B.QTY)) AS CANTIDAD " +
					    			"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B, XX_VMR_PO_APPROVAL C " +
					    			"WHERE A.C_ORDER_ID = B.C_ORDER_ID AND A.C_ORDER_ID = C.C_ORDER_ID " +
					    			"AND A.C_ORDER_ID = '"+orderID+"' AND C.XX_VMR_DEPARTMENT_ID = '"+dptoid+"' " +
					    			"AND C.XX_VMR_LINE_ID = '"+lineid+"' AND C.XX_VMR_SECTION_ID = '"+secid+"'");
					    
					    	PreparedStatement pstmt11 = DB.prepareStatement(SQL11, null); 
						    ResultSet rs11 = pstmt11.executeQuery();
						    
						    if(rs11.next())
						    {
						    	cantidad = rs11.getInt("CANTIDAD");
						    	
						    }
						    rs11.close();
						    pstmt11.close();
					    }
					    rs10.close();
					    pstmt10.close();
		
			    	
			    	 // Busco las cantidades viejas Int para despues sumale el limite y la cantidad
				    String SQL18 = ("SELECT DISTINCT XX_PURCHQUANTIMPDPLACED AS CANTINT, XX_PURCHAMOUNTPLACEDIMPD AS MONTOINT " +
				    		"FROM XX_VMR_PRLD01 " +
				    		"WHERE XX_VMR_DEPARTMENT_ID = '"+dptoid+"' AND XX_VMR_LINE_ID = '"+lineid+"' " +
				    		"AND XX_VMR_SECTION_ID = '"+secid+"' ");
				    
				    PreparedStatement pstmt18 = DB.prepareStatement(SQL18, null); 
				    ResultSet rs18 = pstmt18.executeQuery();
				    
				    if(rs18.next())
				    {
				    	montoComInt = rs18.getInt("MONTOINT");
				    	cantCompInt = rs18.getInt("CANTINT");
				    	cantidaTotalInt = cantidad + cantCompInt;
				    	montoTotalInt = limitTotal + montoComInt;
				    	
				    }
				    rs18.close();
				    pstmt18.close();
			    	
			    	
			    	String SQL15 = ("UPDATE XX_VMR_PRLD01 " +
			    			"SET XX_PURCHQUANTIMPDPLACED = '"+cantidaTotalInt+"' , XX_PURCHAMOUNTPLACEDIMPD = '"+montoTotalInt+"' " +
			    			"WHERE XX_VMR_DEPARTMENT_ID = '"+dptoid+"' AND XX_VMR_LINE_ID = '"+lineid+"' AND XX_VMR_SECTION_ID = '"+secid+"'  ");
				    
				    DB.executeUpdate(get_Trx(),SQL15);
		
		
			    } // end while16
				rs16.close();
				pstmt16.close(); 
				
					//Jessica Mendoza -> Se comentó ese código, debido a que que necesitaba que actualizara automáticamente el estado de la O/C
				    //orden.setXX_Void(true);
				    orden.setXX_OrderStatus("EP");
				    orden.setXX_InsertedStatusDate(XXExitDate);
				    orden.setXX_Alert2(null);
				    orden.setXX_EPDate(XXExitDate);
		    		orden.save();
		    		commit();
				    
		    		// Envio de correo cuando las O/C Internacionales pasan a Produccion 
				    String Attachment = null;
		    		//EnviarCorreosCambioStatusBPartnerProd(orden.getC_BPartner_ID(),orden.getDocumentNo(), Attachment);
					X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), orden.getXX_VMR_Package_ID(), null);
					X_XX_VMR_Collection collection = 
						new X_XX_VMR_Collection(Env.getCtx(), paquete.getXX_VMR_Collection_ID(), null);
					MBPartner vendor = new MBPartner(getCtx(), orden.getC_BPartner_ID(), null);
					MVMRDepartment dep = new MVMRDepartment( getCtx(), orden.getXX_VMR_DEPARTMENT_ID(), null);
		    		EnviarCorreosCambioStatusCoorProd(orden.getDocumentNo(), vendor.getName(), dep.getValue(), collection.getName(),Attachment);
		    		EnviarCorreosCambioStatusImportadasProd(orden.getDocumentNo(), vendor.getName(), dep.getValue(), collection.getName(), Attachment);
		    		EnviarCorreoPlanificadorYComprador(orden.getDocumentNo(), orden.getXX_VMR_DEPARTMENT_ID(), vendor.getName(), dep.getValue(), collection.getName());
		    		
/**		    		if(!orden.getXX_ComesFromSITME())
		    			sendEmailPOtoVendor(orden);
		    			*/
		    		
				    ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_OCProduccion"));
				    
				    /****Jessica Mendoza****/
				    /****Orden de Compra Importada****/
					/****Proceso para crear la OC en la tabla de Estimacion de Cuentas por Pagar, una vez aprobada****/
				    if (orden.getXX_OrderStatus().equals("EP")){
				    	Utilities util = new Utilities();					    
				    	int diasBeneficio = 0;
				    	int diasTotalesI = 0;
				    	int diasTotalesII = 0;
				    	int diasTotalesIII = 0;
				    	Vector<Integer> condicionPago = new Vector<Integer>(6);
				    	Calendar calendarEstimadaI = Calendar.getInstance();
				    	Calendar calendarEstimadaII = Calendar.getInstance();
				    	Calendar calendarEstimadaIII = Calendar.getInstance();
				    	Timestamp fechaEstimada = new Timestamp(0);
						Timestamp fechaEstimadaS = new Timestamp(0);
						Timestamp fechaEstimadaT = new Timestamp(0);
						Vector<Timestamp> vector = new Vector<Timestamp>(3);
				    	MBPartner vendorI = new MBPartner(Env.getCtx(), orden.getC_BPartner_ID(), null);	    	
							
						String sqlEst = "select * " +
										"from XX_VCN_EstimatedAPayable where C_Order_ID = " + orden.getC_Order_ID();
						PreparedStatement pstmtEst = null;
						ResultSet rsEst = null;
						try{
							pstmtEst = DB.prepareStatement(sqlEst, null);
							rsEst = pstmtEst.executeQuery();
							
							if (!rsEst.next()){
								BigDecimal subTotal1 = new BigDecimal(0);
								BigDecimal subTotal2 = new BigDecimal(0);
								diasBeneficio = util.benefitVendorDayAdvance(orden.getC_BPartner_ID());
								condicionPago = util.infoCondicionPago(orden.getC_PaymentTerm_ID());					

								if (orden.getXX_OrderType().equals("Nacional")){
									diasTotalesI = condicionPago.get(1) - diasBeneficio;
									diasTotalesII = condicionPago.get(4) - diasBeneficio;
									diasTotalesIII = condicionPago.get(7) - diasBeneficio;
								}else{
									diasTotalesI = condicionPago.get(1);
									diasTotalesII = condicionPago.get(4);
									diasTotalesIII = condicionPago.get(7);
								}
								
								vector = util.calcularFecha(orden.getC_PaymentTerm_ID(),orden.getC_Order_ID(),"estimada");
								if (vector.get(0) != null){
									fechaEstimada = vector.get(0);
									calendarEstimadaI.setTimeInMillis(fechaEstimada.getTime());
								    calendarEstimadaI.add(Calendar.DATE, diasTotalesI);
								    fechaEstimada = new Timestamp(calendarEstimadaI.getTimeInMillis());	
								}
								if (vector.get(1) != null){
									fechaEstimadaS = vector.get(1);
									calendarEstimadaII.setTimeInMillis(fechaEstimadaS.getTime());
								    calendarEstimadaII.add(Calendar.DATE, diasTotalesII);
								    fechaEstimadaS = new Timestamp(calendarEstimadaII.getTimeInMillis());
								}
								if (vector.get(2) != null){
									fechaEstimadaT = vector.get(2);
									calendarEstimadaIII.setTimeInMillis(fechaEstimadaT.getTime());
								    calendarEstimadaIII.add(Calendar.DATE, diasTotalesIII);
								    fechaEstimadaT = new Timestamp(calendarEstimadaIII.getTimeInMillis());
								}
								fechaEstimada = vector.get(0);
								fechaEstimadaS = vector.get(1);
								fechaEstimadaT = vector.get(2);

							    calendarEstimadaI.setTimeInMillis(fechaEstimada.getTime());
							    calendarEstimadaI.add(Calendar.DATE, diasTotalesI);
							    fechaEstimada = new Timestamp(calendarEstimadaI.getTimeInMillis());					        
							    if (fechaEstimadaS != null){
							       	calendarEstimadaII.setTimeInMillis(fechaEstimadaS.getTime());
								    calendarEstimadaII.add(Calendar.DATE, diasTotalesII);
								    fechaEstimadaS = new Timestamp(calendarEstimadaII.getTimeInMillis());
							    }
							    if (fechaEstimadaT != null){
							       	calendarEstimadaIII.setTimeInMillis(fechaEstimadaT.getTime());
								    calendarEstimadaIII.add(Calendar.DATE, diasTotalesIII);
								    fechaEstimadaT = new Timestamp(calendarEstimadaIII.getTimeInMillis());
							    }
							        
								Vector<Integer> vecI = new Vector<Integer>(9);
								Vector<String> vecS = new Vector<String>(2);
								vecI.add(orden.getC_Order_ID());
								vecI.add(orden.getC_BPartner_ID());
								vecI.add(orden.getC_PaymentTerm_ID());
								vecI.add(orden.getC_Currency_ID());
								if (vendorI.getXX_VendorType_ID() != null)
									vecI.add(vendorI.getXX_VendorType_ID());
								else
									vecI.add(0);
								vecS.add(orden.getXX_POType());
								vecI.add(orden.getXX_VMR_DEPARTMENT_ID());
								vecS.add(orden.getXX_OrderType());
								vecI.add(orden.getC_Country_ID());
								vecI.add(orden.getXX_Category_ID()); 
								vecI.add(orden.getXX_ImportingCompany_ID());
										
								if (condicionPago.get(0).compareTo(100) == 0){ 
									util.crearEstimacion(vecI, vecS, orden.getGrandTotal(),fechaEstimada,"Orden","100% - Mercancia", 0);
								}else{
									BigDecimal total = new BigDecimal(0);
									if (orden.getXX_POType().equals("POM")){
										total = orden.getTotalLines();
									}else{
										total = orden.getGrandTotal();
									}
									if (condicionPago.get(6).compareTo(0) != 0){
										subTotal1 = orden.getTotalLines().multiply(new BigDecimal(condicionPago.get(0))).divide(new BigDecimal(100));
										subTotal2 = orden.getTotalLines().multiply(new BigDecimal(condicionPago.get(3))).divide(new BigDecimal(100));
										util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",condicionPago.get(0).toString()+"% - Mercancia", 0);
										util.crearEstimacion(vecI, vecS, subTotal2,fechaEstimadaS,"Orden",condicionPago.get(3).toString()+"% - Mercancia", 0);
										util.crearEstimacion(vecI, vecS, total.subtract(subTotal1.add(subTotal2)),fechaEstimadaT,"Orden",
												condicionPago.get(6).toString()+"% - Mercancia", 0);
									}else{
										subTotal1 = orden.getTotalLines().multiply(new BigDecimal(condicionPago.get(0))).divide(new BigDecimal(100));
										util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",condicionPago.get(0).toString()+"% - Mercancia", 0);
										util.crearEstimacion(vecI, vecS, total.subtract(subTotal1),fechaEstimadaS,"Orden",
												condicionPago.get(3).toString()+"% - Mercancia", 0);
									}
								}
							}
						}catch (SQLException e){
							log.log(Level.SEVERE, sqlEst, e);
						}finally{
							try {
								rsEst.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								pstmtEst.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
				    }
					/****Fin codigo - Jessica Mendoza***/
				    
		    }// end if internacional
		    else if((orden.getXX_OrderStatus().equals("PEN") && cantidaLine == cantidaRef))
	    	{
	    		if (!OrdenAprobada (orderID, XXExitDate)){ 
	    			get_TrxName().rollback();
	    			setProcessActive(false);
	    			return Msg.translate(getCtx(), "XX_InvalidPurchaseOrder");
	    		}else{
	    			//orden.setXX_Void(true);
		    		orden.save();
		    		
		    		// Envia el correo indicando que la orden de despacho directo a sido aprobada y esta pendiente imprimir etiquetas
		    	    if(orden.getXX_VLO_TypeDelivery().compareTo("DD")==0){ //DIRECTO A TIENDA
				    	enviarCorreoCoordChequeo(orden);
				    }
		
					 // Envio el Correo de que la orden de compra esta Aprobada
					    
		    		String Attachment = null;
		    		//EnviarCorreosCambioStatusBPartner(orden.getC_BPartner_ID(),orden.getDocumentNo(), Attachment);
					MBPartner vendor = new MBPartner(getCtx(), orden.getC_BPartner_ID(), null);
					MVMRDepartment dep = new MVMRDepartment( getCtx(), orden.getXX_VMR_DEPARTMENT_ID(), null);
					X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), orden.getXX_VMR_Package_ID(), null);
					X_XX_VMR_Collection collection = 
						new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
		    		EnviarCorreosCambioStatusCoor(orden, Attachment);
		    		EnviarCorreosCambioStatusImportadas(orden.getDocumentNo(), vendor.getName(), dep.getValue(), collection.getName(), Attachment);
		    		
		    		ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_POApprove", new String[] {orden.getDocumentNo()}));
	    		}
	    	}
		    else
		    {
		    	if(orden.getXX_OrderStatus().equals("PRO") && orden.getXX_OrderType().equals("Importada"))
		    	
		    		ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_NoApproveImp"));
		    	
		    	else if(orden.getXX_OrderStatus().equals("PEN") && orden.getXX_OrderType().equals("Importada"))
		    		
		    		ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_NoApproveImpRef"));
		    	
		    	else if(orden.getXX_OrderType().equals("Nacional"))
		    	
		    		ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_NoApprove"));
		    	
		    	return "";
		 
		    }
		    
		commit();    
		if(!orden.getXX_ComesFromSITME()){
			SendMailThread myThread = new SendMailThread();
			myThread.orden = orden;
			myThread.start();
			//sendEmailPOtoVendor(orden);
		}
	    
	    //return "Error";
		setProcessActive(false);
	    return "Proceso Realizado";
	}
	else{
		
		ADialog.error(1, new Container(), Msg.getMsg(getCtx(), "XX_CantApproveDefinitve"));
		setProcessActive(false);
		return "";
	}


	}



	@Override
	protected void prepare() {
	}
	
	
	/**
	 * Daniel Pellegrino --> Funcion 164. 
	 * Modificado por JTrias
	 * 
	 **/	
	private boolean EnviarCorreosCambioStatusCoor(MOrder order,String Attachment) {
					
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				 "AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Cuentas por Pagar') and AD_Client_ID = "+getAD_Client_ID()+")";
		
		MCurrency currency = new MCurrency( getCtx(), order.getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( getCtx(), order.getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( getCtx(), order.getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( getCtx(), order.getC_Country_ID(), null);
		
		String Mensaje = order.getDocumentNo()+" was approved. \n\n" +
						 "Vendor: "+vendor.getName()+" \n"+
						 "Amount: "+order.getTotalLines()+" \n"+
						 "Currency: "+currency.getDescription()+"\n"+
						 "Payment Term: "+paymentTerm.getName()+"\n"+
						 "Country: "+country.getDescription()+"\n";
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		
		try{
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
				ResultSet rs2 = pstmt2.executeQuery();
			
			
				while(rs2.next()){
					
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					//Utilities f = new Utilities(getCtx(), get_TrxName(),1000007, Mensaje, -1, 1019149 , -1, UserAuxID, Attachment);
					Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
					
				}
				
				rs2.close();
				pstmt2.close();
			}
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			
		} finally{			
			DB.closeResultSet(rs);
            DB.closeStatement(pstmt);
		}
		return true;
	}//Enviar
	
	
	 /**
     * Inicio Gabrielle Huchet
     * Llamada a la funcion para enviar el correo indicando que la orden de despacho directo a sido aprobada 
     * y esta pendiente imprimir etiquetas
     * Envia correo al coordinador de chequeo
     */
	private boolean enviarCorreoCoordChequeo(MOrder order) {
		String sql1 = "SELECT AD_USER_ID FROM AD_User_Roles " +
		"WHERE AD_ROLE_ID=(SELECT AD_Role_ID " +
		"FROM AD_Role " +
		"WHERE name = 'BECO Coordinador de Chequeo') and IsActive='Y'";
		
//		System.out.println(sql1);
		
		PreparedStatement pstmt1 =null;
		ResultSet rs1 = null;
		try{
		
			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			
			while(rs1.next())
			{
				X_AD_User coord = new X_AD_User( Env.getCtx(), rs1.getInt(1), null);
				MWarehouse tienda = new MWarehouse(Env.getCtx(), order.getM_Warehouse_ID(), null);
				String emailTo = coord.getEMail();
//				System.out.println("coordinador de chequeo: "+coord.getEMail());
				MClient m_client = MClient.get(Env.getCtx());
				
				String subject = "O/C de Despacho Directo Pendiente Imprimir Etiquetas" ;
				
//				String msg = "La O/C nro: "+order.getDocumentNo() +" de Despacho Directo a la tienda "+tienda.getValue()+"-"+tienda.getName()+" ha sido aprobada." +
//						"Tiene pendiente la impresión de las etiquetas correspondientes. ";
		
				String msg = Msg.getMsg(getCtx(), "DispatchDirectOCApproved", 
						new String[] { "" + order.getDocumentNo(), "" +tienda.getValue()+"-"+tienda.getName()});
				
				EMail email = m_client.createEMail(null, emailTo, "Coordinador de Chequeo", subject, msg);
				
				if (email != null)
				{			
					String status = email.send();
				
					log.info("Email Send status: " + status);
					
					if (email.isSentOK()){}
					else
						return false;
				}
				else
					return false;
				
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return false;
		}finally{
			try {rs1.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt1.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return true;
	}
	/*** Fin Gabrielle Huchet
	 * 
	 */
	
	 /**
     * Inicio Gabrielle Huchet
     * Llamada a la funcion para enviar el correo indicando que la orden de despacho directo a sido aprobada 
     * Envia correo a Jefe de Planificación y Planificadores
     */
	private boolean enviarCorreoPlanificacion(MOrder order) {
		String sql1 = "SELECT DISTINCT AD_USER_ID FROM AD_User_Roles " +
		"WHERE AD_ROLE_ID IN (SELECT AD_Role_ID " +
		"FROM AD_Role " +
		"WHERE name IN ('BECO Jefe de Planificacion', 'BECO Planificador')) and IsActive='Y'";
		
//		System.out.println(sql1);
		
		PreparedStatement pstmt1 =null;
		ResultSet rs1 = null;
		try{
		
			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			
			while(rs1.next())
			{
				X_AD_User plan = new X_AD_User( Env.getCtx(), rs1.getInt(1), null);
				MWarehouse tienda = new MWarehouse(Env.getCtx(), order.getM_Warehouse_ID(), null);
				String emailTo = plan.getEMail();
//				System.out.println("Planificación: "+plan.getEMail());
				MClient m_client = MClient.get(Env.getCtx());
				
				String subject = "O/C de Despacho Directo Aprobada" ;
				
//				String msg = "La Orden de Compra de Despacho Directo Nro: "+order.getDocumentNo() +" de la tienda "+tienda.getValue()+"-"+tienda.getName()+" ha sido aprobada." +
		
				String msg = Msg.getMsg(getCtx(), "XX_ApproveOCDD", 
						new String[] { "" + order.getDocumentNo(), "" +tienda.getValue()+"-"+tienda.getName()});
				
				EMail email = m_client.createEMail(null, emailTo, "Planificación", subject, msg);
				
				if (email != null)
				{			
					String status = email.send();
				
					log.info("Email Send status: " + status);
					
					if (email.isSentOK()){}
					else
						return false;
				}
				else
					return false;
				
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return false;
		}finally{
			try {rs1.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt1.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return true;
	}
	
	/*** Fin Gabrielle Huchet
	 * 
	 */
	
	private boolean EnviarCorreosCambioStatusBPartner(Integer BPartner, String NumeroOC, String proveedor,String departamento, String coleccion ,String Attachment) {
				
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_ApprovePO", 
				 new String[]{NumeroOC, 
							 "Aprobada", proveedor,departamento, coleccion							  
							 });
		
		try{
			
			//Utilities f = new Utilities(getCtx(), get_TrxName(),1000007, Mensaje, -1, 1019149 , BPartner, -1, Attachment);
			Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , BPartner, -1, Attachment);
			//getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID")
			f.ejecutarMail();
			f = null;
	
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		
		/**
		 * JTrias Enviar correo al contacto del Proveedor
		 */
		MOrder order = new MOrder(getCtx(), getRecord_ID(), null);
		
		if(order.getAD_User_ID()!=0){
			
			Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, order.getAD_User_ID(), Attachment);
			try {
				f.ejecutarMail();
			} catch (Exception e) {
				log.log(Level.SEVERE,e.getMessage());
			}
			f = null;
		}
	
		/** Fin JTrias */
		
		return true;
	}//Enviar
	
	/**
	 * Fin Daniel Pellegrino Fun 164
	 * */
		
	/**
	 * Daniel Pellegrino 
	 * 
	 **/	
	
	
	/**
	 * Inicio Victor Lo Monaco
	 * Envio de correo para notificar que la orden requiere ser distribuida
	 */
	private boolean EnviarCorreoRequiereDistribucion(Integer BPartner, String NumeroOC ,String Attachment) {
			
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_MailPORequiresDistribution", 
				 new String[]{NumeroOC});
		//System.out.println("Mensaje " + Mensaje);
		return false;
/**		
		try{
			
			Utilities f = new Utilities(getCtx(), get_TrxName(),1000007, Mensaje, -1, 1019149 , BPartner, -1, Attachment);
			f.ejecutarMail();
			f = null;
	
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
		*/
	}//Enviar
	/**
	 * fin Victor Lo Monaco
	 */
	
	
	private boolean OrdenAprobada (Integer orderID, Timestamp XXExitDate)
	{

		Utilities util = new Utilities();
		BigDecimal priceListPO = new BigDecimal(1);
		MOrder orden = new MOrder(getCtx(),orderID,get_TrxName());
		MVMRDepartment department = new MVMRDepartment(getCtx(),orden.getXX_VMR_DEPARTMENT_ID(),get_TrxName());
		
		boolean bool = false;
		
	    PreparedStatement pstmt4 = null; 
	    ResultSet rs4 = null;
		try
		{

		    // genero el PO_line para las O/C internacionales

		    String SQL4 = ("SELECT DISTINCT A.XX_VMR_PO_LINEREFPROV_ID AS IDLINE, B.C_BPARTNER_ID AS PROV, TO_NUMBER (TO_CHAR (B.DATEPROMISED, 'YYYYMMDD')) AS DATEPRO, TO_NUMBER (TO_CHAR (B.DATEORDERED, 'YYYYMMDD')) AS DATEOR, " +
		    		"C.M_PRODUCT AS PRODUCT, A.QTY AS CANTIDAD, A.XX_VMR_UNITPURCHASE_ID AS UOM, " +
		    		"A.XX_COSTWITHDISCOUNTS AS PRECIO, " +
		    		"( SELECT C_TAX_ID FROM C_Tax WHERE ValidFrom = (SELECT MAX(ValidFrom) FROM C_Tax WHERE C_TaxCategory_ID = A.C_TAXcategory_ID)) as TAX, C.XX_QUANTITYC AS CC " +
		    		"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B, XX_VMR_REFERENCEMATRIX C " +
		    		"WHERE A.C_ORDER_ID = B.C_ORDER_ID " +
		    		"AND C.XX_QUANTITYV <> 0 " +
		    		"AND C.XX_VMR_PO_LINEREFPROV_ID = A.XX_VMR_PO_LINEREFPROV_ID " +
		    		"AND C.M_PRODUCT IS NOT NULL " +
		    		"AND B.C_ORDER_ID = '"+orderID+"'");
		    
		    
		    //System.out.println(SQL4);
		    pstmt4 = DB.prepareStatement(SQL4, null); 
		    rs4 = pstmt4.executeQuery();
		   
		    while(rs4.next()){
		    	MOrderLine orderLine = new MOrderLine(getCtx(), 0,get_TrxName());
		    			    	
		    	bool = util.cuentaContable(rs4.getInt("PRODUCT")); //Jessica Mendoza
		    	MProduct product = new MProduct(getCtx(), rs4.getInt("PRODUCT"), null);		    
		    		    	
		    	if ((((product.getProductType().equals("A")) || (product.getProductType().equals("S")) 
		    			|| (product.getProductType().equals("I"))) && (bool == true))){ //se agrega el producto a la linea de la O/C
		    		BigDecimal PriceEntered, PriceActual, LineAmount;
			    	
			    	PriceEntered = MUOMConversion.convertProductFrom(getCtx(), rs4.getInt("PRODUCT"), 100, rs4.getBigDecimal("PRECIO"));
			    	if(PriceEntered != null){
			    		orderLine.setPriceEntered(PriceEntered);
				       	PriceActual = PriceEntered;
				       	log.fine("PriceEntered=" + PriceEntered 
								+ " -> PriceActual=" + PriceActual);
				       	orderLine.setPriceActual(PriceActual);
				       	LineAmount = rs4.getBigDecimal("CANTIDAD").multiply(PriceActual); 
				       	orderLine.setLineNetAmt(LineAmount);	
			    	}else{
			       		return false;
			       	}
			    	
			    	orderLine.setAD_Client_ID(orden.getAD_Client_ID());
			    	orderLine.setAD_Org_ID(orden.getAD_Org_ID());
			    	orderLine.setC_Order_ID(orderID);
			       	orderLine.setXX_VMR_PO_LineRefProv_ID(rs4.getInt("IDLINE"));
			       	orderLine.setC_BPartner_ID(rs4.getInt("PROV"));
			       	orderLine.setM_Product_ID(rs4.getInt("PRODUCT"));
			       	orderLine.setQtyEntered(rs4.getBigDecimal("CC"));
			       	orderLine.setC_UOM_ID(100);
			       	orderLine.setC_Tax_ID(rs4.getInt("TAX"));
			       	orderLine.setPriceList(PriceEntered);
			       	//orderLine.set_CustomColumn("PriceStd", PriceEntered);
			       	orderLine.save();
			       	bool = true;
			       	
		    	}else if (bool == false){/****Jessica Mendoza****/
		    		ADialog.error(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_ProductElementVOne") + " " +
		    				product.getValue() + " - " + product.getName() + " " +
		    				Msg.getMsg(Env.getCtx(), "XX_ProductElementVTwo"));
		    		break;
		    		//colocar el ADialog para que aparezca el mensaje por pantalla
		    	}	
		    	/****Fin código - Jessica Mendoza****/
		    } // end while PO_Line
		    rs4.close();
		    pstmt4.close();
		    /**
		     * Inicio Victor Lo Monaco
		     * Llamada a la funcion para enviar el correo indicando que la orden ha cambiado a estado aprobada
		     * Envia correo al jefe de categoria de la cat, planificador del dep, creador de la orden y comprador
		     */		    
//		    EnviarCorreoRequiereDistribucion(5, docNumber.toString() ,"");	    
		    // Enviar correo a los jefes de categoria, planificador, comprador y 
		    /**
		     * Fin Victor Lo Monaco
		     */		
		    
		    if (bool == true){ //Jessica Mendoza
		    	orden.setDocAction(X_C_Order.DOCACTION_Prepare);
			    DocumentEngine.processIt(orden, X_C_Order.DOCACTION_Prepare);
			    orden.save();
			    orden.load(get_TrxName()); 

			    if (orden.getDocStatus().equals("IN")) {
			    	rollback();
			    	ADialog.error(1, new Container(), "XX_InvalidPurchaseOrder");
			    	return false;
			    } 
			    
			    orden.setDocAction(X_C_Order.DOCACTION_Complete);
			    DocumentEngine.processIt(orden, X_C_Order.DOCACTION_Complete);
			    //orden.setXX_ApprovalDate(yearActual);
			    orden.setXX_ApprovalDate(XXExitDate);
			    orden.setXX_OrderStatus("AP");
			    orden.setXX_Alert2(null); 
			    orden.save();

				MBPartner vendor = new MBPartner(getCtx(), orden.getC_BPartner_ID(), null);
				MVMRDepartment dep = new MVMRDepartment( getCtx(), orden.getXX_VMR_DEPARTMENT_ID(), null);
				X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), orden.getXX_VMR_Package_ID(), null);
				X_XX_VMR_Collection collection = 
					new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
			    EnviarCorreoJefedeCategoria(orden.getDocumentNo(), department.getXX_VMR_Category_ID(), vendor.getName(), dep.getValue(), collection.getName());	
				EnviarCorreoPlanificadorYComprador(orden.getDocumentNo(), orden.getXX_VMR_DEPARTMENT_ID(), vendor.getName(), dep.getValue(), collection.getName());
			    
			    //Realizado por Rosmaira Arvelo
			    MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(orden.get_ID(),"AO"),null);
					
				//Llama al proceso cerrar alerta de Aprobación de O/C
				if(!(orden.getXX_ApprovalDate().equals(null))&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("AO")))
				{							
					try
					{
						closeAlert(task.get_ID());
					}
					catch(Exception e)
					{
						log.log(Level.SEVERE,e.getMessage());
					}
					
				} 
				
				task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(orden.get_ID(),"AO1"),null);
				
				//Llama al proceso cerrar alerta de Aprobación de O/C
				if(!(orden.getXX_ApprovalDate().equals(null))&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("AO1")))
				{	
					try
					{
						closeAlert(task.get_ID());
					}
					catch(Exception e)
					{
						log.log(Level.SEVERE,e.getMessage());
					}
					
				} //Fin RArvelo//Fin RArvelo
		    }else{
		    	get_TrxName().rollback(); //Jessica Mendoza
		    }
    
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		} finally {
			DB.closeResultSet(rs4);
			DB.closeStatement(pstmt4);
		}
		
		return bool;
	}
	
	/**
	 * Fin Daniel Pellegrino 
	 * 
	 **/
	

	/**
	 * Victor Lo Monaco--> correo al jefe de categoria correspondiente
	 * 
	 **/	
	private boolean EnviarCorreoJefedeCategoria(String NumeroOC , int categoria_id, String proveedor, String departamento, String coleccion) {
	
		String SQL1 = "SELECT xx_categorymanager_ID AS JEFE " +
		"FROM xx_vmr_category " +
		"WHERE xx_vmr_category_id=" + categoria_id;

		try
		{
						
			PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
			ResultSet rs1 = pstmt1.executeQuery();

			String Mensaje = Msg.getMsg( getCtx(), 
					 "XX_ApprovePO", 
					 new String[]{NumeroOC, 
								 "Aprobada", proveedor, departamento,coleccion							  
								 });
			
			if(rs1.next())
			{
				Integer jefeImport = rs1.getInt("JEFE");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + jefeImport;
				
				PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
				ResultSet rs2 = pstmt2.executeQuery();

				while(rs2.next())
				{
					Integer UserAuxID = rs2.getInt("AD_User_ID");									
					Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
					f.ejecutarMail();
					f = null;
				}
				rs2.close();
			    pstmt2.close();
			}
			rs1.close();
			pstmt1.close();

		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}//Enviar
	/**
	 * Fin Victor Lo Monaco
	 * */
	
	/**
	 * Victor Lo Monaco--> correo al planificador y al comprador correspondiente
	 * 
	 **/	
	private boolean EnviarCorreoPlanificadorYComprador(String NumeroOC , int departamento_id, String proveedor, String departamento, String coleccion) {
		
		String SQL1 = "SELECT xx_inventoryschedule_ID AS planificador,  xx_userbuyer_id as comprador " +
		"FROM xx_vmr_department " +
		"WHERE xx_vmr_department_id=" + departamento_id;

		try
		{
						
			PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
			ResultSet rs1 = pstmt1.executeQuery();

			String Mensaje = Msg.getMsg( getCtx(), 
					 "XX_ApprovePO", 
					 new String[]{NumeroOC, 
								 "Aprobada", proveedor, departamento, coleccion							  
								 });
			
			if(rs1.next())
			{
				Integer Planificador = rs1.getInt("planificador");
				Integer Comprador = rs1.getInt("comprador");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + Planificador + " or C_BPARTNER_ID =" + Comprador;
				
				PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
				ResultSet rs2 = pstmt2.executeQuery();

				while(rs2.next())
				{
					Integer UserAuxID = rs2.getInt("AD_User_ID");									
					Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
					f.ejecutarMail();
					f = null;
				}
				rs2.close();
			    pstmt2.close();
			}
			rs1.close();
			pstmt1.close();

		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}//Enviar
	/**
	 * Fin Victor Lo Monaco
	 * */
	
	
	
	
	
	
	/**
	 * Daniel Pellegrino --> Funcion 164. Status Aprobada  Envia a Jefe de Importaciones
	 * 
	 **/	
	private boolean EnviarCorreosCambioStatusImportadas(String NumeroOC ,  String proveedor, String departamento, String coleccion, String Attachment) {
		
		String SQL1 = "SELECT B.C_BPARTNER_ID AS JEFE " +
		"FROM C_BPARTNER B " +
		"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
		"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Jefe de Importaciones'))";

		try
		{
						
			PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
			ResultSet rs1 = pstmt1.executeQuery();

			String Mensaje = Msg.getMsg( getCtx(), 
					 "XX_ApprovePO", 
					 new String[]{NumeroOC, 
								 "Aprobada", proveedor, departamento, coleccion							  
								 });
			
			if(rs1.next())
			{
				Integer jefeImport = rs1.getInt("JEFE");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + jefeImport;
				
				PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
				ResultSet rs2 = pstmt2.executeQuery();

				while(rs2.next())
				{
					Integer UserAuxID = rs2.getInt("AD_User_ID");									
					//Utilities f = new Utilities(getCtx(), get_TrxName(), 1000007, Mensaje, -1, 1019149 , -1, UserAuxID, Attachment);
					Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
				}
				rs2.close();
			    pstmt2.close();
			}
			rs1.close();
			pstmt1.close();

		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}//Enviar
	/**
	 * Fin Daniel Pellegrino
	 * */
	
	/**
	 * Daniel Pellegrino --> Funcion 164. Aprobada Envia a Proveedor
	 * 
	 **/	
	private boolean EnviarCorreosCambioStatusBPartnerProd(Integer BPartner, String NumeroOC, String proveedor, String departamento, String coleccion, String Attachment) {
			
		
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_ApprovePOImp", 
				 new String[]{NumeroOC, 
							 "Producción", proveedor, departamento, coleccion								  
							 });
		
		try{
			
			//Utilities f = new Utilities(getCtx(), get_TrxName(),1000007, Mensaje, -1, 1019149 , BPartner, -1, Attachment);
			Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , BPartner, -1, Attachment);
			f.ejecutarMail();
			f = null;
	
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		/**
		 * JTrias Enviar correo al contacto del Proveedor
		 */
		MOrder order = new MOrder(getCtx(), getRecord_ID(), null);
		
		if(order.getAD_User_ID()!=0){
			
			Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, order.getAD_User_ID(), Attachment);
			try {
				f.ejecutarMail();
			} catch (Exception e) {
				log.log(Level.SEVERE,e.getMessage());
			}
			f = null;
		}
	
		/** Fin JTrias */
		
		return true;
	}//Enviar
	/**
	 * Fin Daniel Pellegrino
	 * */
	
	
	/**
	 * Daniel Pellegrino --> Funcion 164. Status Aprobada  Envia a analista de importaciones y a jefe de importaciones
	 * 
	 **/	
	private boolean EnviarCorreosCambioStatusImportadasProd(String NumeroOC, String proveedor, String departamento, String coleccion, String Attachment) {
			

			String Mensaje = Msg.getMsg( getCtx(), 
					 "XX_ApprovePOImp", 
					 new String[]{NumeroOC, 
								 "Producción", proveedor, departamento, coleccion							  
								 });

				
			String sql2 = "select ad_user_id from AD_User_Roles r where ad_user_id<>100 and (ad_role_id=1000110 or AD_Role_ID=1000117)";
			
			PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
			ResultSet rs2 = null;
			try
			{
				rs2 = pstmt2.executeQuery();
				while(rs2.next())
				{
					Integer UserAuxID = rs2.getInt("AD_User_ID");									
					//Utilities f = new Utilities(getCtx(), get_TrxName(), 1000007, Mensaje, -1, 1019149 , -1, UserAuxID, Attachment);
					Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
				}
			}
			catch (Exception e){
					log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));

			} finally{			
				try {rs2.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt2.close();} catch (SQLException e) {e.printStackTrace();}
			}
			

		return true;
	}//Enviar
	/**
	 * Fin Daniel Pellegrino
	 * */
	
	
	/**
	 * Daniel Pellegrino --> Funcion 164. Aprobada Envia a Coodinador
	 * 
	 **/	
	private boolean EnviarCorreosCambioStatusCoorProd(String NumeroOC, String proveedor, String departamento, String coleccion, String Attachment) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Cuentas por Pagar'))";
		
			
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_ApprovePOImp", 
				 new String[]{NumeroOC, 
							 "Producción", proveedor, departamento, coleccion								  
							 });
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
				ResultSet rs2 = pstmt2.executeQuery();
			
			
				while(rs2.next()){
					
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					//Utilities f = new Utilities(getCtx(), get_TrxName(),1000007, Mensaje, -1, 1019149 , -1, UserAuxID, Attachment);
					Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
					
				}
				rs2.close();
			    pstmt2.close();
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}//Enviar
	
	/*
	 * Obtiene el AD_USER_ID del CBParter Indicado
	 */
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner;
		
		PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		ResultSet rs = null;
		
		try{
			
			rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				AD_User_ID = rs.getInt("AD_USER_ID");
			}
			
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			} finally{			
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		
		return AD_User_ID;
	}
	
	/**
	 * Fin Daniel Pellegrino
	 * */
	
	//Realizado por Rosmaira Arvelo
    /*
	 *	Obtengo el ID de la tarea critica segun la orden
	 */
	private Integer getCriticalTaskForClose(Integer order, String typeTask){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTaskForClose_ID FROM XX_VMR_CriticalTaskForClose "
			   + "WHERE XX_ActualRecord_ID="+order+" AND XX_TypeTask='"+typeTask+"'";
		
		PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
	    ResultSet rs = null;
	try
	{
		rs = pstmt.executeQuery();			
	    
		while (rs.next())
		{				
			criticalTask = rs.getInt("XX_VMR_CriticalTaskForClose_ID");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		} finally{			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return criticalTask;
	}
	
	/*
	 *	Obtengo la cantidad de pedidos por etiquetar segun la orden
	 */
	private Integer getPendingLabelCount(Integer order){
		
		Integer count=0;
		String SQL = "SELECT COUNT(XX_VMR_Order_ID) FROM XX_VMR_Order "
			   + "WHERE C_Order_ID="+order;
		
		PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
	    ResultSet rs = null;
	try
	{
		rs = pstmt.executeQuery();			
	    
		while (rs.next())
		{				
			count = rs.getInt("COUNT(XX_VMR_Order_ID)");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		} finally{			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return count;
	}//fin 
	
	/*
	 * Genera alerta crítica
	 */
	private void generatedAlert(Integer id)
	{
		MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID"), id); 
		mpi.save();
			
		ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
		pi.setClassName(""); 
		pi.setTitle(""); 
			
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
		
	}//fin generatedAlert
	
	/* VLOMONACO
	 * Si ya esta aprobada por el primer gerente, bloque el botón para que solo otro gerente le pueda dar
	 */
	private boolean alreadyPreaprobed(MOrder orden)
	{
		String role = getCtx().getContext("#AD_Role_Name");
		if (orden.getXX_FirstAppManager_ID()!=0 & !role.contains("BECO Gerente"))
		{
			ADialog.info(1, new Container(), "XX_NotifyOtherManager");
			return true;
		}
		return false;	
	}
	
	// VLOMONACO
	// Si la orden es Sitme y tiene referencias asociadas, debemos rechazar porque de lo contrario en la guía de embarque pasará a aprobada
	private boolean sitmeConAsociadas(MOrder orden)
	{
		if (orden.getXX_OrderType().equals("Importada") && orden.getXX_ImportingCompany_ID() != Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID"))
		{
			String SQL_Asociadas = "select XX_referenceisassociated from xx_vmr_po_linerefprov where XX_referenceisassociated='Y' and C_Order_ID= "+orden.get_ID();
	
			PreparedStatement pstmt_aux2 = null;
			ResultSet rs_aux2 = null;
			
			try {
				pstmt_aux2 = DB.prepareStatement(SQL_Asociadas, null); 
				rs_aux2 = pstmt_aux2.executeQuery();
		
				if(rs_aux2.next())
				{
					ADialog.info(1, new Container(), "XX_SitmeAssociated");
					return true;					
				}
			} catch (Exception e)
			{
				System.out.println("Error " + e);
			} finally {
				DB.closeResultSet(rs_aux2);
				DB.closeStatement(pstmt_aux2);
			}
			
		}
		return false;
	}
	
	/*
	 * Cierra tareas críticas 
	 */
	private void closeAlert(Integer task)
	{
		MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID"), task); 
		mpi.save();
			
		ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
		pi.setClassName(""); 
		pi.setTitle(""); 
			
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
	}//fin closeAlert
	//RArvelo
	
	
	
	
private X_XX_VMR_PO_ApprovalRole getApprovalRole(){
		
		String sqlRole = "SELECT XX_VMR_PO_ApprovalRole_ID FROM XX_VMR_PO_ApprovalRole " +
		 "WHERE AD_ROLE_ID = " + getCtx().getAD_Role_ID();

		PreparedStatement pstmtRole = null;
		ResultSet rsRole = null;
		X_XX_VMR_PO_ApprovalRole approvalRole = null;
		try{
		
			pstmtRole = DB.prepareStatement(sqlRole, null); 
			rsRole = pstmtRole.executeQuery();
			
			if(rsRole.next())
			{
				approvalRole = new X_XX_VMR_PO_ApprovalRole( getCtx(), rsRole.getInt(1), null);
			}
			
			rsRole.close();
			pstmtRole.close();
		
		}catch (Exception e) {
		
			try {
				rsRole.close();
				pstmtRole.close();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, e1.getMessage());
			}
			
			log.log(Level.SEVERE, e.getMessage());
		}
		
		return approvalRole;
	}
		
	/*
	 * @param: 0= No especifico, 1= Jefe Categoría, 2= Planificador
	 */
	private void sendMailToNextRole(int role, MOrder orden, int specific){
		
		String sql="";
		if(specific==1){
			sql = "SELECT AD_USER_ID FROM XX_VMR_CATEGORY CAT, AD_USER USE " +
			 	  "WHERE CAT.XX_CATEGORYMANAGER_ID  = USE.C_BPARTNER_ID " +
			 	  "AND XX_VMR_CATEGORY_ID = " + orden.getXX_Category_ID(); //CABLEADO JEFE DE CATEGORIA
		}
		else if(specific==2){
			sql = "SELECT AD_USER_ID FROM XX_VMR_DEPARTMENT DEP, AD_USER USE " +
				"WHERE DEP.XX_INVENTORYSCHEDULE_ID  = USE.C_BPARTNER_ID " +
				"AND XX_VMR_DEPARTMENT_ID = " + orden.getXX_VMR_DEPARTMENT_ID(); //CABLEADO PLANIFICADOR
		}
		else
			sql = "SELECT AD_USER_ID FROM AD_User_Roles WHERE ISACTIVE = 'Y' AND AD_ROLE_ID = " + role;
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				// A partir de aca enviamos el correo
				Integer UserAuxID = rs.getInt("AD_USER_ID");

				//Proveedor y Departamento:
				X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), orden.getXX_VMR_Package_ID(), null);
				X_XX_VMR_Collection collection = 
					new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
				MBPartner vendor = new MBPartner(getCtx(), orden.getC_BPartner_ID(), null);
				MVMRDepartment dep = new MVMRDepartment( getCtx(), orden.getXX_VMR_DEPARTMENT_ID(), null);
				String Mensaje = Msg.getMsg( getCtx(), "XX_OrderLimit", new String[]{orden.getDocumentNo(),vendor.getName(),dep.getValue(), collection.getName()});
				
				Utilities f = new Utilities(getCtx(), get_Trx().getTrxName(),getCtx().getContextAsInt("#XX_L_MT_OVERDRAWNORDER_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
				f.ejecutarMail(); 
				f = null;
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e1) {
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e1.getMessage()));
			}
			
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
	}
	
	private void closeCriticalTask(MOrder orden,String type){
		
		MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(orden.get_ID(),type),null);
						
		if(((orden.getXX_OrderStatus().equals("PRO"))||(orden.getXX_OrderStatus().equals("PEN")))&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals(type)))
		{
			try
			{
				closeAlert(task.get_ID());
			}
			catch(Exception e)
			{
				log.log(Level.SEVERE,e.getMessage());
			}				
		}
	}
	
	private void createCriticalTaskAO1(MOrder orden){
		
		//Generacion de Alerta de Tarea Critica para Aprobación de O/C
		if(((orden.getXX_OrderStatus().equals("PRO"))||(orden.getXX_OrderStatus().equals("PEN")))&&(orden.isXX_Alert7()==false))
		{	
			try{
				Env.getCtx().setContext("#XX_TypeAlertAO1","AO1");
				Env.getCtx().setContext("#XX_OrderACT",orden.get_ID());
				generatedAlert(orden.get_ID());
			}
			catch(Exception e){
				log.log(Level.SEVERE,e.getMessage());
			}
		}
	}
	
	public class SendMailThread extends Thread{
		
		MOrder orden = null;
		
		public void run(){
			try {
				sendEmailPOtoVendor(orden);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Jorge Pires
		 * 
		 * Este metodo crea la O/C en PDF y la envia al
		 * proveedor
		 * 
		 */
		public String sendEmailPOtoVendor(MOrder ordenCompra){
			
			MPrintFormat f = null;
			int C_Order_ID = ordenCompra.getC_Order_ID();
			log.info("C_Order_ID" + C_Order_ID);
			if (C_Order_ID < 1)
				throw new IllegalArgumentException("@NotFound@ @C_Order_ID@");
			
			// Obtain the Active Record of M_Order Table
			Query q = new Query("C_Order_ID");
			q.addRestriction("C_Order_ID", Query.EQUAL, Integer.valueOf(C_Order_ID));
			int table_ID = X_C_Order.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Orden de Compra", table_ID, C_Order_ID, 0);
			 if (ordenCompra.getXX_OrderType().equals(X_Ref_XX_OrderType.IMPORTADA.getValue()))
				 f = MPrintFormat.get (Env.getCtx(), 1000296, false);
			 else
				 f = MPrintFormat.get (Env.getCtx(), 1000334, false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i); 
			//new Viewer(re);
			
			// Generate the PDF file
			File attachment = null;		
			try
			{
				attachment = File.createTempFile("Purchase Order", ".pdf");
				re.getPDF(attachment);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "", e);
			}

			// Send the PDF File by E-Mail 
			MBPartner socioComprador = new MBPartner(Env.getCtx(), ordenCompra.getXX_UserBuyer_ID(), null);
			/* Busco los usuarios de los Socio de Negocio
			 * */
			MUser userComprador = null;
			String SQL = "SELECT AD_USER_ID " +
						 "FROM AD_USER " +
						 "WHERE C_BPartner_ID = "+socioComprador.get_ID() +" ";
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = null;
		    	
			try{
				rs = pstmt.executeQuery();			
			    
				if (rs.next()){				
					userComprador = new MUser(Env.getCtx(), rs.getInt("AD_USER_ID"), null);
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			} finally{			
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
			
			String comprador = userComprador.getEMail();

			//COMENTADO POR GHUCHET ya que se ahora se envía a todos los representante del proveedor, excepto Legal. 
//			org.compiere.model.MUser userToSale = new org.compiere.model.MUser(Env.getCtx(), ordenCompra.getAD_User_ID(), null);
//			String toSale = userToSale.getEMail();
			//FIN COMENTADO POR GHUCHET
			MBPartner vendorAux = new MBPartner(getCtx(), ordenCompra.getC_BPartner_ID(), null);
			String toVendor = vendorAux.getXX_VendorEmail();	
			MClient m_client = MClient.get(Env.getCtx());
			
			EMail email = m_client.createEMail(null, toVendor, vendorAux.getName(),
					Msg.getMsg(Env.getCtx(), "XX_SentPOSubject", 
							new String[] { ordenCompra.getDocumentNo() }), 
					Msg.getMsg(Env.getCtx(), "XX_SentPurchaseOrder", 
							new String[] { ordenCompra.getDocumentNo(), vendorAux.getName()}));
			if (email != null){
				//COMENTADO POR GHUCHET			
//				if(!toSale.isEmpty()){
//					email.addTo(toSale, userToSale.getName());
//				}
				//FIN COMENTADO POR GHUCHET
				
				if(!comprador.isEmpty()){
					email.addTo(comprador, userComprador.getName());
				}
				
				if(ordenCompra.getXX_OrderType().equals("Importada")){
					email.addTo("cuentasporpagar@beco.com.ve", "Cuentas Por Pagar");
				}
				
				//AGREGADO GHUCHET
				//Se envia correo a representantes comerciales, ventas y administrativos
				 MUser userRepre = null;
				SQL = "SELECT AD_USER_ID " +
					  "FROM AD_USER " +
					  "WHERE C_BPartner_ID = "+ordenCompra.getC_BPartner_ID() +" ";
				
				//Para O/C Importada se envia correo a representantes comerciales
				if (ordenCompra.getXX_OrderType().equals(X_Ref_XX_OrderType.IMPORTADA.getValue())){
					SQL +="AND XX_ContactType IN ("+X_Ref_XX_Ref_ContactType.COMERCIAL.getValue()+","+
					 	X_Ref_XX_Ref_ContactType.SALES.getValue()+","+X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue()+")";
				//Para O/C Importada NO se envia correo a representantes comerciales
				}else if(ordenCompra.getXX_OrderType().equals(X_Ref_XX_OrderType.NACIONAL.getValue())){
					SQL +="AND XX_ContactType IN ("+X_Ref_XX_Ref_ContactType.SALES.getValue()+
						","+X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue()+")";
				}
				
				try{
					pstmt = DB.prepareStatement(SQL, null); 
					rs = pstmt.executeQuery();			
						    
					while (rs.next()){				
						userRepre = new MUser(Env.getCtx(), rs.getInt("AD_USER_ID"), null);
						if(!userRepre.getEMail().isEmpty())
							email.addTo(userRepre.getEMail(), userRepre.getName());
						}
					}
				catch (SQLException e){
					e.printStackTrace();
				} finally{			
					try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
					try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
				}
				if (ordenCompra.getXX_OrderType().equals(X_Ref_XX_OrderType.IMPORTADA.getValue())){		
				//PARA O/C SITME Se envía correo a representantes administrativo y de ventas de la compañía importadora
					MUser userSITME = null;
					SQL = "SELECT U.AD_USER_ID " +
						  "FROM AD_USER U JOIN XX_VSI_CLIENT C ON (C.C_BPartner_ID = U.C_BPartner_ID) " +
						  "WHERE C.XX_VSI_CLIENT_ID = "+ordenCompra.getXX_ImportingCompany_ID() +" " +
						  "AND U.XX_ContactType IN ("+X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue()+", "+
						  X_Ref_XX_Ref_ContactType.SALES.getValue()+")";
							
					pstmt = DB.prepareStatement(SQL, null); 
					rs = null;
						    	
					try{
						rs = pstmt.executeQuery();			
							    
						while (rs.next()){				
							userSITME = new MUser(Env.getCtx(), rs.getInt("AD_USER_ID"), null);
							if(!userSITME.getEMail().isEmpty())
								email.addTo(userSITME.getEMail(), userSITME.getName());
							}
						}
					catch (SQLException e){
						e.printStackTrace();
					} finally{			
						try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
						try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
					}
				 }
				//FIN GHUCHET
				//	Attachment
				if (attachment != null && attachment.exists())
					email.addAttachment(attachment);
				else
				{  
					String mensaje = "No se pudo adjuntar el PDF al enviarle el correo al proveedor en la orden número " + ordenCompra.getDocumentNo() + 
									  ". Por favor envie manualmente el adjunto";
					EMail email2 = m_client.createEMail(null, comprador, "Compras", "Fallo al adjuntar OC " + ordenCompra.getDocumentNo(), mensaje);
					
					if (email2 != null)
					{			
						email2.send();
					
					}
					ADialog.info(1, new Container(), "No se pudo adjuntar la orden de compra en el correo al proveedor. Por favor envíela por correo manualmente");
				}
					
				email.send();
				//log.info("Email Send status: " + status);
			
				if (email.isSentOK())
				{
					return "Mail Sent sucessfully";
				}
				else
					return "Mail cannot be Sent";
			}
			else
				return "Cannot create mail";
		}
	}
}
