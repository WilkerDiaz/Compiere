package compiere.model.cds.processes;
 
import java.awt.Container;
import java.io.File;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.apps.form.FormFrame;
import org.compiere.common.constants.EnvConstants;
import org.compiere.excel.Excel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.MClient;
import org.compiere.model.MPInstance;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_M_InOut;
import org.compiere.model.X_M_Warehouse;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRCategory;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.MVMROrder;
import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_Ref_XX_OrderRequestType;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.cds.X_Ref_XX_Ref_TypeDelivery;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VLO_NewsReport;
import compiere.model.cds.X_XX_VLO_PlacedOrderAs;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_DistributionType;
import compiere.model.cds.X_XX_VMR_HeaderAssociation;
import compiere.model.cds.X_XX_VMR_Order;
import compiere.model.cds.X_XX_VMR_OrderRequestDetail;
import compiere.model.cds.X_XX_VMR_PO_ProductDistrib;
import compiere.model.cds.X_XX_VMR_StoredProduct;
import compiere.model.cds.distribution.XX_Budget;
import compiere.model.cds.distribution.XX_Distribution;
import compiere.model.cds.distribution.XX_PiecesAutomatic;
import compiere.model.cds.distribution.XX_Replacement;
import compiere.model.cds.distribution.XX_Sales;
import compiere.model.cds.distribution.XX_SalesBudget;

public class XX_CompleteCheckup extends SvrProcess {

	static Integer m_readLock = new Integer(0); //Object that is in charge of locking
	
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
	}

	@Override
	protected String doIt() throws Exception {
		
		if(isProcessActive()){
			setProcessActive(false);
			return "";
	    }
			
		setProcessActive(true);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQL = "SELECT PICKEDQTY " +
					 "FROM M_INOUTLINE " +
					 "WHERE M_INOUT_ID="+ getRecord_ID() +" "+
					 "AND C_ORDERLINE_ID IS NULL " +
					 "AND AD_CLIENT_ID IN (0,"+getCtx().getAD_Client_ID()+")";

		boolean unsolicitedIsZero = false;
		try {
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();			
			while (rs.next()) {				
				if(rs.getInt("PICKEDQTY")==0){
					unsolicitedIsZero = true;
				}
			} 
		}catch (Exception e) {
			setProcessActive(false);
			log.log(Level.SEVERE, SQL, e);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		if(unsolicitedIsZero){
			setProcessActive(false);
			return Msg.getMsg(getCtx(), "UnsolProdPickedQty");
		}
		
		
	    int header = 0; //Cabecera de distribucion en caso de ser predistribuida 
	    	
		if(!codeToValidate()){
			
			//Cambios agregados por Javier Pino				
			MInOut inOut = new MInOut( getCtx(), getRecord_ID(), get_Trx());
			MOrder order = new MOrder( getCtx(), inOut.getC_Order_ID(), get_Trx());
			
			//Verificar antes de chequear si es predistribuida y si tiene una cabecera aprobada esperando por chequeo		
	    	if (order.getXX_VLO_TypeDelivery().equals(X_Ref_XX_Ref_TypeDelivery.PRE__DISTRIBUIDA.getValue())){
	    		
	    	    // Javier Pino. Buscar la distribucion, si esta existe y esta esperando por chequeo 
	    		header = Utilities.approvedCheckedHeader(order.get_ID());
	    		if (header == 0) {
	    			
	    			// Jorge Pires - Envio de correos aviso
		    		String Attachment = null;
			    	MBPartner JefePlanificacion = new MBPartner(getCtx(), ConsultarJefePlanificacion(), null);		    	
			    	EnviarCorreoNotificarNoDistribucion(JefePlanificacion.get_ID(), order.get_ID(), Attachment,1);
			    	
			    	X_XX_VMR_Department Departamento = new X_XX_VMR_Department(getCtx(), order.getXX_Department(), null);
			    	MBPartner PlanificadorInventario = new MBPartner(getCtx(), Departamento.getXX_InventorySchedule_ID(), null);	    	
			    	EnviarCorreoNotificarNoDistribucion(PlanificadorInventario.get_ID(), order.get_ID(), Attachment,0);

			    	//En este caso no se debe chequear
					ADialog.warn(EnvConstants.WINDOW_INFO, new Container(), "waitForDistribution");	
					setProcessActive(false);
					return Msg.translate(getCtx(), "waitForDistribution");					
	    		}
	    		
	    	}

	    	//En caso de no ser predistrbuida o serlo y tener distribucion seguir con el chequeo
			inOut.setDocAction(X_M_InOut.DOCACTION_Complete);
			DocumentEngine.processIt(inOut, X_M_InOut.DOCACTION_Complete);
		    inOut.setXX_InOutStatus("CH");
		    inOut.setXX_CompleteCheckup("Y");

		    //si pude guardar el chequeo
		    if(inOut.save()){
		    	 //Las devoluciones de esta O/C las coloco como DPR (pendiente por retirar)
			    changeReturnStatus(order.get_ID());
			    changeNewsReportStatus(order.get_ID());
			    
			    //Coloco la O/C en estado chequeada
			    order.setXX_OrderStatus("CH");
			    Calendar now = Calendar.getInstance();
			    order.setXX_CheckupDate(new Timestamp(now.getTimeInMillis()));
			    order.save();
			    if (order.getXX_POType().equals("POM")){
			    	//Manda correo a planificacion
				    sendEmailPlanif(order.get_ID());
			    }
		    } else {
		    	//Ocurrio un error
		    	ADialog.warn(EnvConstants.WINDOW_INFO, new Container(), "XX_IncompleteCheckUp");	
		    	setProcessActive(false);
		    	return Msg.translate(getCtx(), "XX_IncompleteCheckUp");
		    }
		    
		    boolean generoPedido = true;
		    
		    //La distribucion de una predistribuida esta esperando por chequeo
		    if (header > 0) {
		    	
		    	boolean algunos_diferentes = false;
			    String sql_diferencias = "SELECT P.XX_VMR_PO_PRODUCTDISTRIB_ID, M.MOVEMENTQTY, P.XX_DISTRIBUTEDQTY, M.M_PRODUCT_ID " +
			    " FROM C_ORDERLINE C JOIN M_INOUTLINE M ON ( C.C_ORDERLINE_ID = M.C_ORDERLINE_ID ) " +
			    " JOIN XX_VMR_PO_PRODUCTDISTRIB P on (P.M_PRODUCT_ID = M.M_PRODUCT_ID )" +
			    " WHERE C.C_ORDER_ID = " + order.get_ID() + " AND M.MOVEMENTQTY != P.XX_DISTRIBUTEDQTY " +
			    " AND P.XX_VMR_DISTRIBUTIONHEADER_ID = " +  header;					   
			    try {
				    PreparedStatement ps_diferencias = DB.prepareStatement(sql_diferencias, null);
				    ResultSet rs_diferencias = ps_diferencias.executeQuery();
				    int po_distrib = 0, moveqty = 0, distrqty = 0;
				    while (rs_diferencias.next()) {
				    	po_distrib = rs_diferencias.getInt(1);
				    	moveqty = rs_diferencias.getInt(2);
				    	distrqty = rs_diferencias.getInt(3);
				    	
				    	//Se crea el objeto de tipo detalle de producto
				    	X_XX_VMR_PO_ProductDistrib po_productdistrib = 
			    			new X_XX_VMR_PO_ProductDistrib(getCtx(), po_distrib, null);
				    	if (moveqty == 0) {		
							String sql_delete = "DELETE FROM XX_VMR_PO_ProductDistrib WHERE XX_VMR_PO_ProductDistrib_ID = " + po_distrib;
							DB.executeUpdate(null,sql_delete);						
				    	}
				    	else if (moveqty != distrqty) {
				    		algunos_diferentes = true;
				    		po_productdistrib.setXX_DistributedQTY(moveqty);				    		
				    		po_productdistrib.save();
				    	}				    
				    }
				    rs_diferencias.close();
				    ps_diferencias.close();				    

				}catch (Exception e) {
					setProcessActive(false);
					log.log(Level.SEVERE, SQL, e);
				}
				
				//MODIFICADO POR GHUCHET
				//Si hubo al menos una diferencia el planificador tiene que redistribuir manualmente
				//o se redistribuye automaticamente dependiendo de si la opción de ajuste automático está activa para esa distribución
				if (algunos_diferentes) {
					MVMRDistributionHeader header_changed = 
						new MVMRDistributionHeader(getCtx(), header, null);
					boolean isOk = true;
					//Si está activo el ajuste automático
					if(header_changed.isXX_AutoAdjustment()){
						Integer typeDistribution = header_changed.getXX_DistributionTypeApplied();
						Integer month = header_changed.getXX_Month();
						Integer year = header_changed.getXX_Year();
						boolean previousSales = false;
						X_XX_VMR_DistributionType distType;
						Boolean isGeneral;
						
						if(typeDistribution != null && typeDistribution > 0){
							distType = new X_XX_VMR_DistributionType(getCtx(),typeDistribution, get_Trx());
							isGeneral = distType.isXX_General();

							if(isGeneral || typeDistribution == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPIECES_ID")){
					
								//La distribucion a utilizar
								XX_Distribution distribution = null;
									
								//Verificar el tipo de distribucion a utilizar y los parametros
								if (typeDistribution == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPIECES_ID")) {	
									//Distribucion por piezas
									distribution = new XX_PiecesAutomatic(header, getCtx(), get_TrxName(), month, year);
								} 
								else if (typeDistribution == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPESALES_ID")) {	
									//Distribucion por ventas del producto
									distribution = new XX_Sales(header, getCtx(), get_TrxName(), month, year);									
								} else if (typeDistribution == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEBUDGET_ID")) {											
									//Distribucion por presupuesto
									distribution = new XX_Budget(header, getCtx(), get_TrxName(), month, year);									
								} else if (typeDistribution == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPESALBUD_ID")) {									
									//Distribucion por ventas/presupuesto del producto
									previousSales = false;
									distribution = new XX_SalesBudget(header, getCtx(), get_TrxName(), month, year, previousSales);									
								} else if (typeDistribution == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEREPLAC_ID")) {								
									//Distribucion por reposicion del producto
									distribution = new XX_Replacement(header, getCtx(), get_TrxName(), month, year);			
								} else {
									isOk = false;
								}
								Env.getCtx().setContext("#ADJUSTMENT_AUTOMATIC",1);								
								//Verificada la distribucion, entonces ejecutarla
								distribution.procesar();								
								int distOk = Env.getCtx().getContextAsInt("#DISTRIBUTION_OK");		
							  	(Env.getCtx()).remove("#DISTRIBUTION_OK");
							  	if(distOk == 0){
							  		isOk =false;
							  	}
							}
							else {
								//PROCESAR DISTRIBUCION CUANDO SEA DE TIPO PORCENTUAL
								if (typeDistribution == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPERCEN_ID")) {
									int form_id = 0;
									FormFrame form = new FormFrame();
									synchronized( m_readLock ) {
										Env.getCtx().setContext("#XX_VMR_DISTRIBUTIONHEADER_ID",header);	
										Env.getCtx().setContext("#ADJUSTMENT_AUTOMATIC",1);	
										/*compiere.model.cds.forms.XX_ChangeDistriPercen_Form*/
										form_id = Env.getCtx().getContextAsInt("#XX_L_FORMDIST_OCPERCENT_ID");		
										form.openForm(form_id);	
										int distOk = Env.getCtx().getContextAsInt("#DISTRIBUTION_OK");		
									  	(Env.getCtx()).remove("#DISTRIBUTION_OK");
									  	if(distOk == 0){
									  		isOk =false;
									  	}
									}
								}else {
									isOk = false;
								}
								
							}
						//OJO COMENTADO HASTA QUE SE HAGAN LAS PRUEBAS Y SE CONSIDERE ADECUADO EL AJUSTE AUTOMATICO
//							//Si se completó con éxito la distribución se procede a generar los pedidos
//							if (isOk){
//								order.setXX_StoreDistribution("Y");
//								//Evita que se generen pedidos dos veces si ya está aprobada.
//								if (header_changed.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.APROBADA.getValue())) {
//									return "";
//								}
//								//Evita que se generen pedidos si ya existen
//								if (Utilities.hasAssociatedPlacedOrders(header_changed.get_ID())) {
//									header_changed.setXX_DistributionStatus(
//											X_Ref_XX_DistributionStatus.APROBADA.getValue());
//									header_changed.save();
//									return "";
//								}
//								//Esta chequeada por lo tanto es posible crear los pedidos
//								generoPedido = generatePlacedOrdersFromCheckedPurchaseOrder (header_changed);
//							}
						}
						//HASTA AQUI PENDIENTE POR DESCOMENTAR
					
						/*AGREGADO POR GHUCHET - eliminar este bloque cuando se descomente el de arriba*/
						if (isOk){
							header_changed.setXX_DistributionStatus(
									X_Ref_XX_DistributionStatus.LISTA_PARA_APROBAR.getValue());
							header_changed.save();										
							
							//Envia Correos avisando que hay q revisar y aprobar la distribucion
					    	X_XX_VMR_Department Departamento = new X_XX_VMR_Department(getCtx(), order.getXX_Department(), null);
					    	MBPartner PlanificadorInventario = new MBPartner(getCtx(), Departamento.getXX_InventorySchedule_ID(), null);	
					    	EnviarCorreoNotificarRevisarAprobarDist(PlanificadorInventario.get_ID(), order.get_ID());
						}
						   /*HASTA AQUI AGREGADO POR GHUCHET - eliminar este bloque cuando se descomente el de arriba */
					}
					//Si está desactivado el ajuste automático o no se pudo distribuir con el ajuste automático
					if(!header_changed.isXX_AutoAdjustment() || !isOk){
						header_changed.setXX_DistributionStatus(
								X_Ref_XX_DistributionStatus.PENDIENTE_POR_REDISTRIBUCION_Y_APROBACION.getValue());
						header_changed.setXX_VMR_DistributionType_ID(getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPIECES_ID"));
						header_changed.save();										
						// Jorge Pires - Envia Correos avisando que hay q redistribuir por piezas
						String Attachment = null;
				    	MBPartner JefePlanificacion = new MBPartner(getCtx(), ConsultarJefePlanificacion(), null);		    	
				    	EnviarCorreoNotificarRedistribuirPiezas(JefePlanificacion.get_ID(), order.get_ID(), Attachment,1);
				    	
				    	X_XX_VMR_Department Departamento = new X_XX_VMR_Department(getCtx(), order.getXX_Department(), null);
				    	MBPartner PlanificadorInventario = new MBPartner(getCtx(), Departamento.getXX_InventorySchedule_ID(), null);	    	
				    	EnviarCorreoNotificarRedistribuirPiezas(PlanificadorInventario.get_ID(), order.get_ID(), Attachment,0);
					}  			
				//FIN GHUCHET
				//Si hubo no hubo diferencia entre lo chequeado y lo distribuido 
				} else {					
					MVMRDistributionHeader distribucion_aprobada = new MVMRDistributionHeader(getCtx(),header,get_TrxName());
					
					//Evita que se generen pedidos dos veces si ya está aprobada.
					if (distribucion_aprobada.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.APROBADA.getValue())) {
						setProcessActive(false);
						return "";
					}
					//Evita que se generen pedidos si ya existen
					if (Utilities.hasAssociatedPlacedOrders(distribucion_aprobada.get_ID())) {
						distribucion_aprobada.setXX_DistributionStatus(
								X_Ref_XX_DistributionStatus.APROBADA.getValue());
						distribucion_aprobada.save();
						setProcessActive(false);
						return "";
					}
					
					//Esta chequeada por lo tanto es posible crear los pedidos
					generoPedido = generatePlacedOrdersFromCheckedPurchaseOrder (distribucion_aprobada);

				} 
		    }
		    
		    if (generoPedido)
		    {
			    if(order.getXX_InvoicingStatus().equals("AP") && order.getXX_OrderType().compareTo(X_Ref_XX_OrderType.NACIONAL.getValue()) == 0){
					Utilities util = new Utilities();
					try {
						util.generateDebtCreditNotifies(order.get_ID(),get_TrxName());
					} catch (Exception e) {
						setProcessActive(false);
						e.printStackTrace();
					}
				}
			    
			    
			    //Realizado por Rosmaira Arvelo
			    MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(inOut.get_ID()),null);
				
				//Llama al proceso cerrar alerta de Órdenes de compra pendientes de chequear
				if((order.getXX_OrderStatus().equals("CH"))&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("CO")))
				{	
					try
					{
						MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID"), task.get_ID()); 
						mpi.save();
							
						ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
						pi.setRecord_ID(mpi.getRecord_ID());
						pi.setAD_PInstance_ID(mpi.get_ID());
						pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
						pi.setClassName(""); 
						pi.setTitle(""); 
							
						ProcessCtl pc = new ProcessCtl(null ,pi,null); 
						pc.start();
					}
					catch(Exception e)
					{
						setProcessActive(false);
						log.log(Level.SEVERE,e.getMessage());
					}
					
				} //Fin RArvelo
		    
			    		    
				//Jessica Mendoza
				if (order.getXX_POType().equals("POM")){
					/**
					 * Jorge Pires / Evaluacion de la O/C (Cuando realizo el chequeo - 1)
					 */
					Utilities aux = new Utilities();
					aux.ejecutarWeigth(order.get_ID(), getRecord_ID());
					order.setXX_Evaluated(true);
					order.save();
					aux=null;
				} 
		    }else{
		    	setProcessActive(false);
		    	return "Diferencia en Inventario. No se puede Aprobar";
		    }
			
			/**
			 * Avisos de Credito y debito Ayuso
			 */
			/*if(order.getXX_InvoicingStatus().equals("AP") && order.getXX_OrderType().compareTo(X_Ref_XX_OrderType.NACIONAL.getValue()) == 0){
				Utilities util = new Utilities();
				util.generateDebtCreditNotifies(order.get_ID());
			}*/
		    
		    if (order.getXX_POType().equals("POM")){
		    	//Manda correo a planificacion
			    sendEmailPlanif(order.get_ID());
			    //Agregado por GHUCHET
			    //Manda correo a comprador 
			    if(order.getXX_OrderType().compareTo(X_Ref_XX_OrderType.NACIONAL.getValue())==0)
			    	sendEmailBuyer(order.get_ID(), inOut.get_ID());
			    //Hasta aqui agregado por GHUCHET
		    }
		    setProcessActive(false);
			return "Completed";
			
		}else{
			ADialog.info(1,null,Msg.getMsg(getCtx(), "CodeToValidate"));
			setProcessActive(false);
			return "";
		}
	}

	@Override
	protected void prepare() {

	}
	
	/**
	 * Jorge E. Pires G. --> Envio de Correos avisa que tiene que distribuir. 
	 * 						 La PO era predistribuida y los usuarios no han distribuido
	 * 						 esta PO, y ya llego a CD 
	 * 
	 **/	
	/**
	 * 	Consulta el Jefe de Planificacion
	 */
	public Integer ConsultarJefePlanificacion(){
		Integer aux = null;
		
		String sql = "select C_BPartner_ID " +
					 "from C_BPartner " +
				     "where C_JOB_ID = "+getCtx().getContext("#XX_L_JOBPOSITION_PLANMAN_ID")+" and " +
				     "IsActive = 'Y' ";
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getInt("C_BPartner_ID");
				rs.close();
				pstmt.close();
			}
			return aux;
		}
		catch (SQLException e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return -1;
		}
	}//ConsultarJefePlanificacion
	
	private boolean EnviarCorreoNotificarNoDistribucion(Integer BPartner, Integer PO, String Attachment, int indicador) {
		MOrder po = new MOrder(getCtx(), PO, get_TrxName());
		
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_NeedDistribPO", 
				 new String[]{ po.getDocumentNo()
							 });
		if(indicador == 0){
			String sql = "SELECT u.AD_User_ID "
				   + "FROM AD_User u "
				   + "where IsActive = 'Y' and "
				   + "C_BPARTNER_ID = " + BPartner ;
		
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					Integer UserAuxID = rs.getInt("AD_User_ID");
					Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_NEEDDISTRIBPO_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, Attachment);
					f.ejecutarMail(); 
					f = null;
				}
				rs.close();
				pstmt.close();
			}
			catch (Exception e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}
		}else if(indicador == 1){
			String sql = "SELECT AD_USER_ID FROM AD_User_Roles WHERE AD_ROLE_ID="+getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID")+"";
		
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					Integer UserAuxID = rs.getInt("AD_User_ID");
					Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_NEEDDISTRIBPO_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
				}
				rs.close();
				pstmt.close();
			}
			catch (Exception e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}
		}
		
		return true;
	}

	private boolean EnviarCorreoNotificarRedistribuirPiezas(Integer BPartner, Integer PO, String Attachment, int indicador) {
		MOrder po = new MOrder(getCtx(), PO, get_TrxName());
		
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_NeedRedistribPO", 
				 new String[]{ po.getDocumentNo()
							 });
		if(indicador == 0){
			String sql = "SELECT u.AD_User_ID "
				   + "FROM AD_User u "
				   + "where IsActive = 'Y' and "
				   + "C_BPARTNER_ID = " + BPartner ;
		
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					Integer UserAuxID = rs.getInt("AD_User_ID");
					Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_NEEDREDISTRIBPO_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
				}
				rs.close();
				pstmt.close();
			}
			catch (Exception e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}
		}else if(indicador == 1){
			String sql = "SELECT AD_USER_ID FROM AD_User_Roles WHERE AD_ROLE_ID="+getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID")+"";
			
		
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					Integer UserAuxID = rs.getInt("AD_USER_ID");
					Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_NEEDREDISTRIBPO_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, Attachment);
					f.ejecutarMail(); 
					f = null;
				}
				rs.close();
				pstmt.close();
			}
			catch (Exception e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}
		}
		
		return true;
	}
	/**
	 * Fin Jorge E. Pires G.
	 * */
	
	/**GHuchet - Envía un correo al jefe de planificación y planificador indicado, que indica que una distribución 
	 * ha sido ajustada automaticamente y debe revisarla y aprobarla */
	private boolean EnviarCorreoNotificarRevisarAprobarDist(Integer BPartner, Integer PO) {
		MOrder po = new MOrder(getCtx(), PO, get_TrxName());
		
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_NeedApproveDistribAuto", 
				 new String[]{ po.getDocumentNo()
							 });
		
		String sql = "SELECT AD_User_ID "
				   + "FROM AD_User "
				   + "where IsActive = 'Y' and "
				   + "C_BPARTNER_ID = " + BPartner ;
			System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = null;
			try{
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					Integer UserAuxID = rs.getInt("AD_User_ID");
					Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_NEEDAPPROVEDISTAUTO_ID"), 
							Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, null);
					f.ejecutarMail();
					f = null;
				}
			}
			catch (Exception e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
    	
			sql = "SELECT AD_USER_ID FROM AD_User_Roles " +
					"WHERE AD_ROLE_ID = "+getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID");
			
		  	System.out.println(sql);
			pstmt = DB.prepareStatement(sql, null);
			rs = null;
			try{
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					Integer UserAuxID = rs.getInt("AD_USER_ID");
					Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_NEEDAPPROVEDISTAUTO_ID"), 
							Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, null);
					f.ejecutarMail(); 
					f = null;
				}
				rs.close();
				pstmt.close();
			}
			catch (Exception e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		
		return true;
	}
	
	
	private boolean codeToValidate(){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQL = "SELECT XX_CodeToValidate " +
		 			 "FROM M_InOutLine " +
		 			 "WHERE C_OrderLine_ID IS NULL and M_InOut_ID="+getRecord_ID();
					
		try {
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				if(rs.getString("XX_CodeToValidate").equals("Y")){
					return true;
				}
			} 
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL, e);
			return true;
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return false;
	}
	
	//Realizado por Rosmaira Arvelo
    /*
	 *	Obtengo el ID de la tarea critica segun la orden
	 */
	private Integer getCriticalTaskForClose(Integer inout){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTaskForClose_ID FROM XX_VMR_CriticalTaskForClose "
			   + "WHERE XX_ActualRecord_ID="+inout;
	try
	{
		PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
	    ResultSet rs = pstmt.executeQuery();			
	    
		while (rs.next())
		{				
			criticalTask = rs.getInt("XX_VMR_CriticalTaskForClose_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return criticalTask;
	}//fin getCriticalTask RArvelo
	
	
	/*
	 * Cambia el estado de las devoluciones de esta O/C
	 */
	private void changeReturnStatus(int orderID){
				
		String sql = "UPDATE XX_VLO_RETURNOFPRODUCT SET XX_STATUS='DPR' where C_ORDER_ID="+ orderID +" AND XX_STATUS='PRE'";
		
		int i = DB.executeUpdate(get_Trx(), sql);
		
		//Se envia correo al proveedor solo si se realizo el update
		if(i>0)
			sendMailToVendor(orderID);
		
	}
	
	/*
	 * Cambia el estado de los reportes de novedad de esta O/C
	 */
	private void changeNewsReportStatus(int orderID){
		/*
		String sql = "UPDATE XX_VLO_NEWSREPORT SET XX_STATUS='AO' where C_ORDER_ID = "+ orderID +" AND XX_STATUS='PR'";
		
		int i = DB.executeUpdate(get_Trx(), sql);
			
		//Se envia correo a los responsables
		if(i>0){
//			sendEmailNewsReport(orderID);
		}*/
		
		
		// MODIFICADO GMARQUES:
		// Se necesita la instrucción SAVE para que arranquen los flujos de trabajo
		String sql = "SELECT XX_VLO_NEWSREPORT_ID FROM XX_VLO_NEWSREPORT " +
				" where C_ORDER_ID = "+ orderID +" AND XX_STATUS='PR' ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next()) {
				X_XX_VLO_NewsReport newsReport = 
						new X_XX_VLO_NewsReport( Env.getCtx(), rs.getInt("XX_VLO_NEWSREPORT_ID"), null);
				newsReport.setXX_Status("AO");
				newsReport.save();
				// Se envia correo a los responsables
				//sendEmailNewsReport(orderID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		
		
	}
	
	private void sendMailToVendor(int orderID){
	
		MOrder order = new MOrder( getCtx(), orderID, null);
		
		int NDev = 0;
		int pieces = 0;
		int dev = 0;
		
		String sql = "SELECT a.XX_VLO_RETURNOFPRODUCT_ID,a.Value,SUM(XX_TOTALPIECES) as pieces " +
					 "FROM XX_VLO_RETURNOFPRODUCT a, XX_VLO_RETURNDETAIL b " +
					 "WHERE a.XX_VLO_RETURNOFPRODUCT_ID=b.XX_VLO_RETURNOFPRODUCT_ID " +
					 "AND a.C_ORDER_ID=" + orderID +
					 " GROUP BY a.VALUE,a.XX_VLO_RETURNOFPRODUCT_ID";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx()); 
		    rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				NDev = rs.getInt("Value");
				pieces = rs.getInt("pieces");
				dev = rs.getInt("XX_VLO_RETURNOFPRODUCT_ID");
			}
				
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		MBPartner proveedor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
		
		String Mensaje= "\n " + proveedor.getName() + " \n \n Estimados Señores \n \n"
		  +" Por medio de la presente comunicación, le informamos que tiene un total de "+ pieces
		  + " Piezas Devueltas " +"bajo la Devolucion No- "+NDev + ". El plazo para el retiro de la mercancía es de quince (15) días hábiles contados a partir de la fecha de hoy. " 
		  + "\n \n La entrega se realizará en las instalaciones de CENTROBECO, de acuerdo a lo estipulado en las condiciones impresas en la Orden de Compra correspondiente."
		  + "\n \n La persona que retire la mercancía deberá presentar Carta de Autorización que contenga la información que a continuación se indica: Membrete, firma y sello de " 
		  +	"la Compañía, nombre y cédula de identidad del autorizado y de quien autoriza, fecha idéntica al día del retiro."
		  + "\n \n Vencido el plazo para el retiro de la mercancía, CENTROBECO, C.A. descontará un cinco por ciento (5%) del valor total de la factura de los productos devueltos " 
		  +	"por concepto de gastos de almacenaje. "
		  + "\n \n Nota: Éste es un mensaje automático, por favor no responder.";
		
		int buyer = getAD_User_ID(order.getXX_UserBuyer_ID());
		
		//Envio correo al comprador
		Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_VENDORRETURN_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, buyer,null);
		try {
			f.ejecutarMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		f = null;
		
		int saleContact = order.getAD_User_ID();
		
		//Envio correo al contacto del proveedor 
		f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_VENDORRETURN_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, saleContact,null);
		try {
			f.ejecutarMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		f = null;
		
		//Indicamos la fecha de la primera devolucion
		String sqlupd = "UPDATE XX_VLO_RETURNOFPRODUCT SET XX_NOTIFICATIONDATE = SYSDATE where XX_VLO_RETURNOFPRODUCT_ID="+dev;
	
		try {
			DB.executeUpdate(get_Trx(), sqlupd);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		
	}
	
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner + " "+
					 "AND ISACTIVE='Y'";
		
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
	}
	/**
	 * Genera los pedidos asociados a una distribucion por orden de compra 
	 * La misma tiene que estar CHEQUEADA
	 */
	public boolean generatePlacedOrdersFromCheckedPurchaseOrder (
			MVMRDistributionHeader distribucion_aprobada) {
				
		//Si la distribucion puede continuar generar los pedidos
		Vector<Integer> numeros_tiendas = new Vector<Integer>();				
		Vector<Integer> numero_pedido = new Vector<Integer>();
		
		//Almacena la informacion de lotes y productos
		XX_BatchNumberInfo info = new XX_BatchNumberInfo();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		MOrder orden = new MOrder(Env.getCtx(), distribucion_aprobada.getC_Order_ID(), null);
		
		boolean worked = true;

		//Para cada uno de los productos DE LA DISTRIBUCION
		String sql4= "SELECT D.M_PRODUCT_ID, DD.M_WAREHOUSE_ID, D.C_TaxCategory_ID, D.XX_TaxAmount," +
				" D.XX_SalePrice, D.XX_SalePricePlusTax, D.PriceActual, D.XX_UnitPurchasePrice, DD.XX_ProductQuantity " +
				" FROM XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_PO_DISTRIBDETAIL DD ON " +
				" (DD.XX_VMR_PO_PRODUCTDISTRIB_ID = D.XX_VMR_PO_PRODUCTDISTRIB_ID) " +
				" WHERE D.XX_VMR_DISTRIBUTIONHEADER_ID= " + distribucion_aprobada.getXX_VMR_DistributionHeader_ID();
		
		PreparedStatement prst4= DB.prepareStatement(sql4,get_TrxName());
		ResultSet rsProducts;
		try {			
			rsProducts = prst4.executeQuery();
			while(rsProducts.next()){ 
				
				info.get_put(rsProducts.getInt("M_Product_ID"), 0, get_Trx(), distribucion_aprobada.getM_Warehouse_ID());
									
				XX_BatchNumberInfo.helperClass whereTO = 
					info.data.get(rsProducts.getInt("M_Product_ID"));
														
				
				//GHUCHET - MODIFICADO PARA QUE REVISE SI EL ALMACEN ES CUALQUIER CD - PROYECTO CD VALENCIA
				if (Utilities.esCD(rsProducts.getInt("M_Warehouse_ID"))) {
												
					//Estos productos no generan pedido pq van a centro de distribucion van a la tabla stored product	
					X_XX_VMR_StoredProduct stored_p = new X_XX_VMR_StoredProduct(getCtx(), 0, null);
					stored_p.setC_Order_ID(distribucion_aprobada.getC_Order_ID());
					if (rsProducts.getInt("C_TaxCategory_ID")> 0)
						stored_p.setC_TaxCategory_ID(rsProducts.getInt("C_TaxCategory_ID"));
					if (rsProducts.getBigDecimal("XX_TaxAmount") != null)
						stored_p.setXX_TaxAmount(
							rsProducts.getBigDecimal("XX_TaxAmount").setScale(2, RoundingMode.HALF_EVEN));
					if (rsProducts.getBigDecimal("XX_SalePrice") != null)
						stored_p.setXX_SalePrice(
							rsProducts.getBigDecimal("XX_SalePrice").setScale(2, RoundingMode.HALF_EVEN));
					if (rsProducts.getBigDecimal("XX_SalePricePlusTax") != null)
						stored_p.setXX_SalePricePlusTax(
							rsProducts.getBigDecimal("XX_SalePricePlusTax").setScale(2, RoundingMode.HALF_EVEN));
														
					stored_p.setXX_ProductQuantity(rsProducts.getInt("XX_ProductQuantity"));
					stored_p.setM_Product_ID(rsProducts.getInt("M_Product_ID"));
					stored_p.setXX_VMR_DistributionHeader_ID(distribucion_aprobada.get_ID());
					stored_p.save(get_TrxName());																	
					continue;
				}		
				
					X_M_Warehouse tienda = new X_M_Warehouse(getCtx(), rsProducts.getInt("M_Warehouse_ID"), null);
					//Verificar cuantos productos hay para distribuir
					int distributed = rsProducts.getInt("XX_ProductQuantity"), useful;						
					boolean insertar_tienda = true;
					for (int i = 0; i < whereTO.remaining_quantity.size(); i++) {							
						
						//Obtener la tienda a la que pertenece el pedido 
						int index = -1;							
						for(int j=0; j< numeros_tiendas.size();j++){
							if(numeros_tiendas.elementAt(j).equals(tienda.get_ID())){
								insertar_tienda = false;
								index = j;
								break;
							}
						}

						//Verificar de donde obtener las cantidades de productos y sus lotes
						useful = whereTO.remaining_quantity.elementAt(i);		
						if (useful == 0) continue;
						if (distributed == 0) break;														

						//Crear los detalles del pedido
						X_XX_VMR_OrderRequestDetail detalle_pedido = new X_XX_VMR_OrderRequestDetail(getCtx(), 0 ,null);
						detalle_pedido.setM_Product_ID(rsProducts.getInt("M_Product_ID"));						
						if(insertar_tienda){	
							
							//Si no se ha creado un pedido previo para esa tienda, crearlo
							MVMROrder pedido = new MVMROrder(getCtx(),0, null);
							pedido.setXX_OrderBecoCorrelative(X_Ref_XX_OrderRequestType.PRE__DISTRIBUCIÓN.getValue()+tienda.getValue()+""+distribucion_aprobada.getXX_VMR_DistributionHeader_ID());							
							pedido.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.getValue());
							pedido.setXX_OrderRequestType(X_Ref_XX_OrderRequestType.PRE__DISTRIBUCIÓN.getValue());
							pedido.setC_Order_ID(distribucion_aprobada.getC_Order_ID());
							pedido.setM_Warehouse_ID(tienda.get_ID());
							pedido.setXX_VMR_DistributionHeader_ID(distribucion_aprobada.get_ID());
							pedido.setXX_WeekCreated(calendar.get(Calendar.WEEK_OF_YEAR));							
							pedido.save(get_TrxName());
							numeros_tiendas.add(tienda.get_ID());								
							numero_pedido.add(pedido.get_ID());
							detalle_pedido.setXX_VMR_Order_ID( pedido.get_ID());							
						} else {
							//sino usar uno creado previamente
							detalle_pedido.setXX_VMR_Order_ID(numero_pedido.get(index));							
						} 
						
						//colocar los atributos del pedido
						detalle_pedido.setXX_VMR_Category_ID(orden.getXX_Category_ID());
						detalle_pedido.setXX_VMR_Department_ID(orden.getXX_VMR_DEPARTMENT_ID());						
						detalle_pedido.setXX_VMA_Season_ID(orden.getXX_Season_ID());
						detalle_pedido.setXX_VMR_Collection_ID(orden.getXX_Collection_ID());
						detalle_pedido.setXX_VMR_Package_ID(orden.getXX_VMR_Package_ID());

						MProduct product = new MProduct(getCtx(), rsProducts.getInt("M_Product_ID") , null);
						
						detalle_pedido.setXX_VMR_Line_ID(product.getXX_VMR_Line_ID());
						detalle_pedido.setXX_VMR_Section_ID(product.getXX_VMR_Section_ID());
						
						if (rsProducts.getInt("C_TaxCategory_ID")> 0)
							detalle_pedido.setC_TaxCategory_ID(rsProducts.getInt("C_TaxCategory_ID"));
						if (rsProducts.getBigDecimal("XX_TaxAmount") != null)
							detalle_pedido.setXX_TaxAmount(
								rsProducts.getBigDecimal("XX_TaxAmount").setScale(2, RoundingMode.HALF_EVEN));
						if (rsProducts.getBigDecimal("XX_SalePrice") != null)
							detalle_pedido.setXX_SalePrice(
								rsProducts.getBigDecimal("XX_SalePrice").setScale(2, RoundingMode.HALF_EVEN));
						if (rsProducts.getBigDecimal("XX_SalePricePlusTax") != null)
							detalle_pedido.setXX_SalePricePlusTax(
								rsProducts.getBigDecimal("XX_SalePricePlusTax").setScale(2, RoundingMode.HALF_EVEN));						
						if (rsProducts.getBigDecimal("PriceActual") != null)
							detalle_pedido.setPriceActual(
								rsProducts.getBigDecimal("PriceActual").setScale(2, RoundingMode.HALF_EVEN));					
						if (rsProducts.getBigDecimal("XX_UnitPurchasePrice") != null)
							detalle_pedido.setXX_UnitPurchasePrice(
								rsProducts.getBigDecimal("XX_UnitPurchasePrice").setScale(2, RoundingMode.HALF_EVEN));
											
						//Determinar de donde se calculan los lotes, y que cantidades se usaran de cada uno
						if (useful >= distributed ) {								
							detalle_pedido.setXX_ProductQuantity( distributed );
							detalle_pedido.setQtyReserved(distributed); //Se almacena dos veces para tener la cantidad original
							detalle_pedido.setXX_ProductBatch_ID(whereTO.batchnumbers.get(i));
							whereTO.remaining_quantity.set(i, useful - distributed); 							
							detalle_pedido.save(get_TrxName());
							distributed = 0;
							break;								
						} else {
							detalle_pedido.setXX_ProductQuantity( useful );
							detalle_pedido.setQtyReserved(useful ); //Se almacena dos veces para tener la cantidad original
							detalle_pedido.setXX_ProductBatch_ID(whereTO.batchnumbers.get(i));									
							detalle_pedido.save(get_TrxName());
							whereTO.remaining_quantity.set(i, 0);
							distributed -= useful;
						}							
					}
					if (distributed > 0 ) {
						worked = false;
					}						
				}				
			rsProducts.close();
			prst4.close();

			if (!worked) {
				rollback();
				return worked;
			} else {
				distribucion_aprobada.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.APROBADA.getValue());
				distribucion_aprobada.save(get_TrxName());
				commit();
			}
			
			//Verificar si la distribucion tiene distribuciones hermanas para crear las asociaciones de pedidos
			String sql_asociadas = 
				" SELECT XX_VMR_HEADERASSOCIATION_ID, XX_ASSOCIATIONNUMBER FROM XX_VMR_HEADERASSOCIATION " +
				" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + distribucion_aprobada.get_ID();  
			int grupo_no = 0;					
			try {
				PreparedStatement ps = DB.prepareStatement(sql_asociadas, null);
				ResultSet rs = ps.executeQuery();
				X_XX_VMR_HeaderAssociation header_asoc = null;
				if (rs.next() ) { 
					
					//Indicar que esta distribucion fue correctamente aprobada
					header_asoc  = new X_XX_VMR_HeaderAssociation(getCtx(), rs.getInt(1), null);
					grupo_no = rs.getInt(2);
					header_asoc.setIsApproved(true);
					header_asoc.save(get_TrxName());	
				}
				rs.close();
				ps.close();
			} catch (SQLException e){
				e.printStackTrace();
				ADialog.error(1, new Container(), "XX_DatabaseAccessError");				
			}
			
			//Si la cabecera está asociada a alguna otra distribucion
			if (grupo_no > 0) {
				
				//Agregar todos los pedidos a mi grupo de asociacion
				for (int i = 0 ; i < numero_pedido.size() ; i++) {
					X_XX_VLO_PlacedOrderAs asociacion = 
						new X_XX_VLO_PlacedOrderAs(Env.getCtx(), 0 , null);
					asociacion.setXX_VMR_Order_ID(numero_pedido.get(i));
					asociacion.setXX_AssociationNumber(grupo_no);
					asociacion.save(get_TrxName());							
				}
				
				//Advertir al usuario que debe distribuir el resto
				String sql_asociadasaprobadas = 
					" SELECT XX_VMR_DISTRIBUTIONHEADER_ID, ISAPPROVED FROM " +
					" XX_VMR_HEADERASSOCIATION WHERE XX_ASSOCIATIONNUMBER = " + grupo_no +
					" AND XX_VMR_DISTRIBUTIONHEADER_ID <> " + distribucion_aprobada.get_ID();
				
				PreparedStatement ps = DB.prepareStatement(sql_asociadas, null);
				ps = DB.prepareStatement(sql_asociadasaprobadas, null);
				ResultSet rs = ps.executeQuery();
				Vector<String> distribuciones_faltantes = new Vector<String>();  					
				while (rs.next()) {
					if ( rs.getString(2) == null || !rs.getString(2).equals("Y")) {
						distribuciones_faltantes.add("" + rs.getInt(1));
					} 							
				}					
				rs.close();
				ps.close();					
			}						
			commit();
						
			//Realizado por Rosmaira Arvelo
			MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(distribucion_aprobada.get_ID()),null);
				
			//Llama al proceso cerrar alerta de Asignación de precios de venta
			if(((distribucion_aprobada.getXX_DistributionStatus().equals("AC"))||(distribucion_aprobada.getXX_DistributionStatus().equals("AP")))&&(task.get_ID()!=0)&&(task.isActive()==true)&&((task.getXX_TypeTask().equals("AP"))||(task.getXX_TypeTask().equals("AP1"))))
			{							
				try
				{
					Utilities.closeAlert(task.get_ID());
				}
				catch(Exception e)
				{
					log.log(Level.SEVERE,e.getMessage());
				}
			}			
			
		} catch (SQLException e) {				
			e.printStackTrace();
			ADialog.error(1, new Container(), "XX_DatabaseAccessError");
		}		
		
		//Realizado por Rosmaira Arvelo
		Integer count = 0;
		
		if(distribucion_aprobada.getC_Order_ID()!=0)
		{	
			count = Utilities.associatedPlacedOrders(distribucion_aprobada.get_ID());
			
		    if((count!=0)&&(distribucion_aprobada.isXX_Alert3()==false))
			{
		    	try
		    	{
					//Generacion de Alerta de Tarea Critica para Pedidos pendientes por etiquetar		
					Env.getCtx().setContext("#XX_TypeAlertEP1","EP1");
					Env.getCtx().setContext("#XX_OrderEP1CT",distribucion_aprobada.get_ID());
					
					Utilities.generatedAlert(distribucion_aprobada.get_ID());
		    	}
				catch(Exception e)
				{
					log.log(Level.SEVERE,e.getMessage());
				}
				
			}
		}//fin RArvelo
		
		return worked;
	}
		
	
	/**
	 * Correo que notifica sobre los reportes de novedad
	 * @param 
	 * @return
	 */
	public boolean sendEmailNewsReport(int orderID)
	{			
		MOrder order = new MOrder( Env.getCtx(), orderID, null);
		
		String emailTo = "cuentasporpagar@beco.com.ve";
		
		MClient m_client = MClient.get(Env.getCtx());
		
		String orderDoc = order!= null ?  order.getDocumentNo() : "" ;
		
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		
		String subject = "Reporte de Novedad O/C: " + orderDoc;
		String msg = "Se ha registrado un reporte de novedad para la O/C: " + orderDoc + "\n\nProveedor: "+vendor.getName()+"\nDepartamento: "+dep.getValue();
		
		EMail email = m_client.createEMail(null, emailTo, "Cuentas por Pagar", subject, msg);
	
		if (email != null)
		{			
			MVMRCategory category = new MVMRCategory( Env.getCtx(), order.getXX_Category_ID(), null);
			int categoryMan = getAD_User_ID(category.getXX_CategoryManager_ID());
			X_AD_User categoryChief = new X_AD_User( Env.getCtx(), categoryMan, null);
			
			int buyerID = getAD_User_ID(order.getXX_UserBuyer_ID());
			X_AD_User buyer = new X_AD_User( Env.getCtx(), buyerID, null);
			
			//Comprador
			if(buyer.getEMail().contains("@")){
				email.addTo(buyer.getEMail(), "Comprador");
			}

			//Jefe de Categoria
			if(categoryChief.getEMail().contains("@")){
				email.addTo(categoryChief.getEMail(), "Jefe de Categoria");
			}
			
			//Revisoria
			email.addTo("revisoria@beco.com.ve", "Revisoria");
			
			String status = email.send();
		
			log.info("Email Send status: " + status);
			
			if (email.isSentOK())
			{
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	/**
	 * Correo que notifica a planificacion que se completo un chequeo y la mercancia esta disponible
	 * @param 
	 * @return
	 */
	public boolean sendEmailPlanif(int orderID)
	{			
		MOrder order = new MOrder( Env.getCtx(), orderID, null);
		
		MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		
		int planif = getAD_User_ID(dep.getXX_InventorySchedule_ID());
		X_AD_User planifUser = new X_AD_User( Env.getCtx(), planif, null);
		
		String emailTo = planifUser.getEMail();
		
		if(emailTo.contains("@")){
			
			MClient m_client = MClient.get(Env.getCtx());
			
			String orderDoc = order!= null ?  order.getDocumentNo() : "" ;
			
			String subject = "Chequeo de O/C: " + orderDoc;
			String msg = "Se ha completado el chequeo para la O/C: " + orderDoc + "\n\nProveedor: "+vendor.getName()+"\nDepartamento: "+dep.getValue();
			
			Vector<String> brands = getBrands(order);
			
			msg += "\nMarca(s):";
			
			for(int i=0; i<brands.size(); i++){
				
				if(i==0)
					msg += " " + brands.get(i);
				else
					msg += ", " + brands.get(i);
			}
			
			EMail email = m_client.createEMail(null, emailTo, "Planificador", subject, msg);	
			
			if (email != null)
			{
				String status = email.send();
				
				log.info("Email Send status: " + status);
				
				if (email.isSentOK())
				{
					return true;
				}
				else
					return false;
			}
		}
		
		return false;
	}
	
	private Vector<String> getBrands(MOrder order){
		
		Vector<String> brands = new Vector<String>();
		
		String SQL = "SELECT DISTINCT c.NAME FROM C_OrderLine a, M_product b, XX_VMR_Brand c " +
					 "WHERE a.m_product_id = b.m_product_id AND b.XX_VMR_Brand_ID = c.XX_VMR_Brand_ID AND a.C_Order_ID = " + order.get_ID();

		try{
		
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			while(rs.next())
			{
				brands.add( rs.getString(1));
			}
			
			rs.close();
			pstmt.close();
		  
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		}
		
		return brands;
	}
	
	/**GHUCHET
	 * Correo que notifica al comprador, con copia al planificador en caso de O/C predistribuida, indicando si la O/C tiene excedentes o faltantes o devoluciones 
	 * @param 
	 * @return
	 */
	public boolean sendEmailBuyer(int orderID, int inoutID)
	{			
		MOrder order = new MOrder( Env.getCtx(), orderID, null);
		
		MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
	
		X_AD_User buyerUser = new X_AD_User( Env.getCtx(), getAD_User_ID(order.getXX_UserBuyer_ID()), null);
		String emailTo = buyerUser.getEMail();
		
		String emailPlan = null;
		if(order.getXX_VLO_TypeDelivery().compareTo(X_Ref_XX_Ref_TypeDelivery.PRE__DISTRIBUIDA.getValue())==0){
			X_AD_User planUser = new X_AD_User( Env.getCtx(), getAD_User_ID(dep.getXX_InventorySchedule_ID()), null);
			emailPlan = planUser.getEMail();
		}
		
		String emailJefCat = null;
		MVMRCategory cat = new MVMRCategory(Env.getCtx(), dep.getXX_VMR_Category_ID(), null);
		X_AD_User JefCatUser = new X_AD_User( Env.getCtx(), getAD_User_ID(cat.getXX_CategoryManager_ID()), null);
		emailJefCat = JefCatUser.getEMail();
		
		cat.getXX_CategoryManager_ID();
		
		
		
		String sql = "SELECT sum(iol.XX_ExtraPieces), sum(iol.XX_MissingPieces), sum(iol.ScrappedQty) "+
				" From M_InoutLine iol Where iol.M_Inout_ID ="+ inoutID;
		int extra =0, missing=0, scrapped=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		try{
			
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
				 
			while(rs.next())
			{
				extra = rs.getInt(1);
				missing = rs.getInt(2);
				scrapped = rs.getInt(3);
			}
			
		  
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(emailTo!= null && emailTo.contains("@") && (extra>0 || missing >0 || scrapped >0)){
			
			MClient m_client = MClient.get(Env.getCtx());
			String orderDoc = order!= null ?  order.getDocumentNo() : "" ;
			String vendorName = vendor.getValue()+" - "+vendor.getName();
			File attachmentExtra = null;
			File attachmentMissing = null;
			File attachmentScrapped = null;
			String subject = "Faltantes/Sobrantes/Devoluciones en O/C: " + orderDoc;
			String msg = "Se adjunta los archivos con los productos faltantes, sobrantes y devueltos por control de calidad de la \nO/C: " + orderDoc + 
			"\nProveedor: "+vendorName+"\nDepartamento: "+dep.getValue();
			if(missing > 0){
				try {
					attachmentMissing = File.createTempFile("Piezas Faltantes ", ".xls");
				} 
				catch (Exception e) {
					log.log(Level.SEVERE, "", e);
				}
				Excel excelMissing = new Excel(attachmentMissing);
				MiniTablePreparator tableMissing = fillTableMissing(inoutID,orderDoc,vendorName);
				Utilities.createEXCEL(excelMissing, tableMissing);
			}
			if(extra > 0){
				try {
					attachmentExtra = File.createTempFile("Piezas Sobrantes ", ".xls");
				} 
				catch (Exception e) {
					log.log(Level.SEVERE, "", e);
				}
				Excel excelExtra = new Excel(attachmentExtra);
				MiniTablePreparator tableExtra =fillTableExtra(inoutID,orderDoc,vendorName);
				Utilities.createEXCEL(excelExtra, tableExtra);
			}
			
			if(scrapped > 0){
				try {
					attachmentScrapped= File.createTempFile("Piezas Devueltas ", ".xls");
				} 
				catch (Exception e) {
					log.log(Level.SEVERE, "", e);
				}
				Excel excelScrapped = new Excel(attachmentScrapped);
				MiniTablePreparator tableScrapped = fillTableScrapped(orderID,orderDoc,vendorName);
				Utilities.createEXCEL(excelScrapped, tableScrapped);
			}
			
			EMail email = m_client.createEMail(null, emailTo, "Comprador", subject, msg);	

			if(emailPlan!=null && !emailPlan.isEmpty()){
				email.addTo(emailPlan, "Planificador");
			}
			
			if(emailJefCat!=null && !emailJefCat.isEmpty()){
				email.addTo(emailJefCat, "Jefe de Categoría");
			}
			
			if (attachmentMissing != null && attachmentMissing.exists())
				email.addAttachment(attachmentMissing);
			
			if (attachmentExtra != null && attachmentExtra.exists())
				email.addAttachment(attachmentExtra);
			
			if (attachmentScrapped != null && attachmentScrapped.exists())
				email.addAttachment(attachmentScrapped);
		
			if (email != null)
			{
				String status = email.send();
				
				log.info("Email Send status: " + status);
				
				if (email.isSentOK())
				{
					return true;
				}
				else
					return false;
			}
		}
		
		return false;
	}
	
	private MiniTablePreparator fillTableExtra(int inoutID, String documentNo, String vendor) {
		
		MiniTablePreparator table = new MiniTablePreparator();
		String sql = null;
		
		ColumnInfo[] columns = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"), ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Vendor"), ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"), ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Description"), ".", String.class),
				new ColumnInfo( Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"),".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Quantity")+" Piezas Sobrantes",".", Integer.class),
		};
		table.prepareTable(columns, null, null, false, null);
		sql = "SELECT '"+documentNo+"', '"+vendor+"', p.value, p.name, ref.value, iol.XX_ExtraPieces"+
		" From M_InoutLine iol JOIN M_Product p ON (p.M_Product_ID = iol.M_Product_ID) " +
		"JOIN XX_VMR_VendorProdRef ref ON (p.XX_VMR_VendorProdRef_ID = ref.XX_VMR_VendorProdRef_ID)" +
		" Where iol.M_Inout_ID ="+ inoutID+" and iol.XX_ExtraPieces > 0 "+
		" Order By p.value, p.name, ref.value ";

			System.out.println(sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			table.loadTable(rs);
			table.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		}  finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (ps != null)
				try {
					ps.close();
					ps = null;
				} catch (SQLException e) {}		
		}
	
	return table;	
	}
	
	private MiniTablePreparator fillTableMissing(int inoutID, String documentNo, String vendor) {
		
		MiniTablePreparator table = new MiniTablePreparator();
		String sql = null;
		
		ColumnInfo[] columns = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"), ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Vendor"), ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"), ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Description"), ".", String.class),
				new ColumnInfo( Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"),".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Quantity")+" Piezas Faltantes",".", Integer.class),
		};
		table.prepareTable(columns, null, null, false, null);
		sql = "SELECT  '"+documentNo+"', '"+vendor+"', p.value, p.name, ref.value, iol.XX_MissingPieces"+
		" From M_InoutLine iol JOIN M_Product p ON (p.M_Product_ID = iol.M_Product_ID) " +
		"JOIN XX_VMR_VendorProdRef ref ON (p.XX_VMR_VendorProdRef_ID = ref.XX_VMR_VendorProdRef_ID)" +
		" Where iol.M_Inout_ID ="+ inoutID+" and iol.XX_MissingPieces > 0 "+
		" Order By p.value, p.name, ref.value ";

		System.out.println(sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			table.loadTable(rs);
			table.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		}  finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (ps != null)
				try {
					ps.close();
					ps = null;
				} catch (SQLException e) {}		
		}
	
	return table;	
	}
	
	private MiniTablePreparator fillTableScrapped(int orderID, String documentNo, String vendor) {
		
		MiniTablePreparator table = new MiniTablePreparator();
		String sql = null;
		
		ColumnInfo[] columns = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"), ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Vendor"), ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"), ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Description"), ".", String.class),
				new ColumnInfo( Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"),".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Quantity")+" Piezas Devueltas por Control de Calidad",".", Integer.class),
				new ColumnInfo("Motivo de Devolución",".", String.class),
		};
		table.prepareTable(columns, null, null, false, null);
		sql = "\nSELECT  '"+documentNo+"', '"+vendor+"' , P.VALUE, P.NAME, REF.VALUE, D.XX_TOTALPIECES, CM.VALUE||'-'||CM.NAME " + 
	    "\nFROM XX_VLO_RETURNDETAIL D JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = D.M_PRODUCT_ID) " +
		"\nJOIN XX_VMR_VENDORPRODREF REF ON (P.XX_VMR_VENDORPRODREF_ID = REF.XX_VMR_VENDORPRODREF_ID)" +
		"\nJOIN XX_VMR_CANCELLATIONMOTIVE CM ON (CM.XX_VMR_CANCELLATIONMOTIVE_ID = D.XX_VMR_CANCELLATIONMOTIVE_ID) "+
	    "\nWHERE D.XX_VMR_CANCELLATIONMOTIVE_ID <>" + Env.getCtx().getContext("#XX_L_SURPLUSCANCELMOTIVE_ID") +
	    "\nAND D.XX_VLO_RETURNOFPRODUCT_ID IN (SELECT R.XX_VLO_RETURNOFPRODUCT_ID FROM XX_VLO_RETURNOFPRODUCT R WHERE C_ORDER_ID= "+orderID+")" +
	    "\nORDER BY  P.VALUE, P.NAME, REF.VALUE ";
		System.out.println(sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			table.loadTable(rs);
			table.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		}  finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (ps != null)
				try {
					ps.close();
					ps = null;
				} catch (SQLException e) {}		
		}
	
	return table;	
	}
	
}
