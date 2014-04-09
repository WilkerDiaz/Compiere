package compiere.model.cds.processes;
 
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import org.compiere.apps.ProcessCtl;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPInstance;
import org.compiere.model.MProduct;
import org.compiere.model.X_M_InOut;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VMR_Order;
import compiere.model.cds.forms.XX_PackageLoad_Form;

public class XX_CompleteReception extends SvrProcess {

	static CLogger 			log = CLogger.getCLogger(XX_PackageLoad_Form.class);
	static int PU=0;
	static int PS=0;
	
	@Override
	protected String doIt() throws Exception {
	
		int M_InOut_ID = getRecord_ID();
		int M_Locator_ID = 0;
		int locator_activos = 0;
		int locator_suminis = 0;
		MInOut inOut = new MInOut(Env.getCtx(), M_InOut_ID, null);
		MOrder p_order = new MOrder(Env.getCtx(), inOut.getC_Order_ID(), null);
		
		if(p_order.getXX_POType().equalsIgnoreCase("POM")){
			
			//Busco el locator para asignarlo a las lineas de recepcion
			String SQL = "Select M_Locator_ID FROM M_LOCATOR " +
						 "WHERE M_WAREHOUSE_ID="+inOut.getM_Warehouse_ID()+" AND ISDEFAULT='Y' AND ISACTIVE='Y' " +
					     "AND AD_CLIENT_ID IN (0,"+getAD_Client_ID()+")";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
				ResultSet rs = pstmt.executeQuery();

				while(rs.next()){
					M_Locator_ID = rs.getInt("M_Locator_ID");
				}
				
				rs.close();
				pstmt.close();
			}
			catch (Exception e) {
				log.log(Level.SEVERE, SQL, e.getMessage());
			}
			
		}else{
			//SOLO SE CONTEMPLA BYS EN CD
			String SQL = "Select M_Locator_ID FROM M_LOCATOR " +
						 "WHERE M_WAREHOUSE_ID="+inOut.getM_Warehouse_ID()+" AND value like '%ACTIVOS FIJOS%' AND ISACTIVE='Y' " +
					     "AND AD_CLIENT_ID IN (0,"+getAD_Client_ID()+")";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
				ResultSet rs = pstmt.executeQuery();

				while(rs.next()){
					locator_activos = rs.getInt("M_Locator_ID");
				}
				
				rs.close();
				pstmt.close();
			}
			catch (Exception e) {
				log.log(Level.SEVERE, SQL, e.getMessage());
			}
			
			SQL = "Select M_Locator_ID FROM M_LOCATOR " +
					 "WHERE M_WAREHOUSE_ID="+inOut.getM_Warehouse_ID()+" AND value like '%Y MATERIAL%' AND ISACTIVE='Y' " +
				     "AND AD_CLIENT_ID IN (0,"+getAD_Client_ID()+")";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
				ResultSet rs = pstmt.executeQuery();
	
				while(rs.next()){
					locator_suminis = rs.getInt("M_Locator_ID");
				}
				
				rs.close();
				pstmt.close();
			}
			catch (Exception e) {
				log.log(Level.SEVERE, SQL, e.getMessage());
			}
			
			
			if(p_order.getXX_PurchaseType().equalsIgnoreCase("FA"))
				M_Locator_ID = locator_activos;
			else if(p_order.getXX_PurchaseType().equalsIgnoreCase("SU"))
				M_Locator_ID = locator_suminis;
			else
				return "ERROR: TIPO DE O/C";
		}
		
		//Guardo las lineas
		if(saveData( Env.getCtx(), p_order, null, M_Locator_ID, M_InOut_ID )){
			MOrder order = new MOrder( getCtx(), inOut.getC_Order_ID(), null);
			
			String esTienda = "";
			//Busco el locator para asignarlo a las lineas de recepcion
			String SQL = "Select XX_IsStore from m_warehouse where m_warehouse_id="+inOut.getM_Warehouse_ID();
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
				ResultSet rs = pstmt.executeQuery();

				while(rs.next()){
					esTienda = rs.getString(1);
				}
				
				rs.close();
				pstmt.close();
			}
			catch (Exception e) {
				log.log(Level.SEVERE, SQL, e.getMessage());
			}
			
			//Si la recepcion la realizó en Tienda (Es decir, no la realizó en algún CD)
		    if(esTienda.equals("Y"))
		    {
		    	int placedOrderID = placedOrderID(p_order.get_ID());
		    	if(placedOrderID!=0){
		    		X_XX_VMR_Order placedOrder = new X_XX_VMR_Order( Env.getCtx(), placedOrderID, null);
		    		placedOrder.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_TIENDA.getValue());
		    		placedOrder.save();
		    	}
		    	
				inOut.setDocAction(X_M_InOut.DOCACTION_Complete);
				DocumentEngine.processIt(inOut, X_M_InOut.DOCACTION_Complete);
			    inOut.setXX_InOutStatus("CH");
			    inOut.save();

			    order.setXX_OrderStatus("CH");
			    order.save();		    

		    	//Envio el correo al Gerente de Administracion
		    	sendMailAdminManager(inOut);
		    	
		    	/**
				 * Avisos de Credito y debito Ayuso
				 */
				if(order.getXX_InvoicingStatus().equals("AP") && order.getXX_OrderType().compareTo(X_Ref_XX_OrderType.NACIONAL.getValue()) == 0){
					Utilities util = new Utilities();
					util.generateDebtCreditNotifies(order.get_ID(), get_Trx());
				}
				
				//Jessica Mendoza
				if(order.getXX_POType().equals("POM")){
			    	/**
					 * Jorge Pires / Evaluacion de la O/C (Entro a tiempo en tienda - 4)
					 **/
			    	Utilities aux = new Utilities();		   
					aux.ejecutarWeigth(inOut.getC_Order_ID(), inOut.get_ID());
					order.setXX_Evaluated(true);
					order.save();
					aux=null;
				}
		    }
		    else{
		    	
		    	if(noCheckBenefit(order)) {
			    	inOut.setDocAction(X_M_InOut.DOCACTION_Complete);
			    	DocumentEngine.processIt(inOut, X_M_InOut.DOCACTION_Complete);
				    inOut.setXX_InOutStatus("CH");
				    inOut.setXX_WithNoCheckBenefit(true);
				    inOut.save();
				    
				    order.setXX_OrderStatus("CH");
				    if(order.getXX_OrderType().equalsIgnoreCase("Importada")){
					    order.setXX_StatusWhenReceipt(order.getXX_OrderStatus());
				    }
				    order.save();
				    
				    /**
				     * Javier Pino 
				     * Aprobar la distribucion si todo está bien				     * 
				     */
				    int header = 
				    	Utilities.approvedCheckedHeader(order.get_ID());
				    if (header != 0 ) {
				    	MPInstance mpi = new MPInstance( getCtx(), getCtx().getContextAsInt("#XX_L_PROCESSAPPROVEDIST_ID"), header);
					    mpi.save();
					    ProcessInfo pi = new ProcessInfo("", getCtx().getContextAsInt("#XX_L_PROCESSAPPROVEDIST_ID"));
					    pi.setRecord_ID(mpi.getRecord_ID());
					    pi.setAD_PInstance_ID(mpi.get_ID());
					    pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_PROCESSAPPROVEDIST_ID"));
					    pi.setClassName("");
					    pi.setTitle("");
					    ProcessCtl pc = new ProcessCtl(null ,pi, get_Trx());
					    pc.start();
					    pc.join(); //Se hizo que se esperera por la aprobacion
				    }
				    
				    //Jessica Mendoza
				    if(order.getXX_POType().equals("POM")){
				    	/**
						 * Jorge Pires / Evaluacion de la O/C (Cuando no tengo que chequear - 2)
						 **/
						Utilities aux = new Utilities();
						aux.ejecutarWeigth(inOut.getC_Order_ID(), inOut.get_ID());
						order.setXX_Evaluated(true);
						order.save();
						aux=null;
				    }
			    }
		    	else{
		 			
					inOut.setXX_WithNoCheckBenefit(false);
		    		inOut.save();
		    		
		    		if(order.getXX_OrderType().equalsIgnoreCase("Importada")){
		    			order.setXX_StatusWhenReceipt(order.getXX_OrderStatus());
					    order.save();
					}
		    	}
		    }
		    
		    /****Jessica Mendoza****/
		    //Setea la fecha de la autorización de entrada para que se puedan ejecutar otros procesos
		    if (order.getXX_POType().equals("POA")){
		    	inOut.setXX_PrintCheckup("Y");
		    	inOut.save();
		    	order.setXX_EntranceDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		    	order.save();
		    }
		    /****Fin código - Jessica Mendoza****/
		    
		  //AGREGADO GHUCHET - Actualiza Multiplos de empaque de las referencia de proveedor de la OC.
		    updatePackageMultiple(p_order);
		    //FIN AGREGADO GHUCHET
		}

		return "";
	}
	
	//AGREGADO GHUCHET - Actualiza Multiplos de empaque de las referencia de proveedor de la OC.
	private void updatePackageMultiple(MOrder orden) {
		
		PreparedStatement pstmt = null;
		String sql =  "\nUPDATE XX_VMR_VENDORPRODREF V " +
			"\nSET UPDATED = SYSDATE, UPDATEDBY = "+orden.getCreatedBy()+", V.XX_PACKAGEMULTIPLE = (SELECT L.XX_PACKAGEMULTIPLE " +
			"\nFROM  XX_VMR_PO_LINEREFPROV L  " +
			"\nWHERE L.C_ORDER_ID = "+orden.get_ID()+
			"\nAND V.XX_VMR_VENDORPRODREF_ID = L.XX_VMR_VENDORPRODREF_ID) " +
			"\nWHERE V.XX_VMR_VENDORPRODREF_ID IN ( " +
			"\nSELECT XX_VMR_VENDORPRODREF_ID " +
			"\nFROM XX_VMR_PO_LINEREFPROV L " +
			"\nWHERE L.C_ORDER_ID  = "+orden.get_ID()+
			"\nAND V.XX_VMR_VENDORPRODREF_ID = L.XX_VMR_VENDORPRODREF_ID " +
			"\nAND L.XX_PACKAGEMULTIPLE != V.XX_PACKAGEMULTIPLE) ";
		
		try {
			DB.executeUpdate(get_Trx(),sql);
		} catch (Exception e) {	
			e.printStackTrace();
		}
		
	}
	//FIN AGREGADO GHUCHET  Actualiza Multiplos de empaque de las referencia de proveedor de la OC.
	
	@Override
	protected void prepare(){
	}
	
	private int placedOrderID(int orderID){
		
		String SQL = "SELECT XX_VMR_ORDER_ID " +
					 "FROM XX_VMR_ORDER " +
					 "WHERE C_ORDER_ID=" + orderID;

		int placedOrderID=0;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				placedOrderID = rs.getInt("XX_VMR_ORDER_ID");
			} //while
			
			rs.close();
			pstmt.close();
		
		} //try
		catch (Exception e) {
			log.log(Level.SEVERE, SQL, e.getMessage());
		}
		
		return placedOrderID;
	}
	
	public static boolean saveData(Ctx ctx, MOrder p_order, MInvoice m_invoice, int M_Locator_ID, int M_InOut_ID )
	{
		MInOut inout = new MInOut (ctx, M_InOut_ID, null);
	
		String SQL = "SELECT l.QtyOrdered-SUM(COALESCE(m.Qty,0)) as Qty, l.C_UOM_ID as UOM, COALESCE(l.M_Product_ID,0) as Product, l.C_OrderLine_ID as Line " +
				     "FROM C_OrderLine l LEFT OUTER JOIN M_MatchPO m ON (l.C_OrderLine_ID=m.C_OrderLine_ID AND m.M_InOutLine_ID IS NOT NULL) LEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID) LEFT OUTER JOIN C_Charge c ON (l.C_Charge_ID=c.C_Charge_ID) LEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID) " +
					 "WHERE l.PROCESSED = 'Y' AND l.C_Order_ID="+p_order.getC_Order_ID()+ " " +
					 "GROUP BY l.QtyOrdered,CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name), l.M_Product_ID,COALESCE(p.Name,c.Name), l.Line,l.C_OrderLine_ID ORDER BY l.Line";
		
		try
		{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
		    
		    while(rs.next()){
		    	
				Number d = rs.getInt("Qty");      //  1-Qty
				BigDecimal QtyEntered = new BigDecimal(d .doubleValue());
		
				int C_UOM_ID = rs.getInt("UOM");                    //  2-UOM
				int M_Product_ID = rs.getInt("Product");                          //  3-Product
				int C_OrderLine_ID = rs.getInt("Line");      //  4-OrderLine
				int C_InvoiceLine_ID = 0;                                  //  6-InvoiceLine
				
				MInvoiceLine il = null;
				if (C_InvoiceLine_ID != 0)
					il = new MInvoiceLine (ctx, C_InvoiceLine_ID, null);
				//	Precision of Qty UOM
				int precision = 2;
				if (M_Product_ID != 0)
				{
					MProduct product = MProduct.get(ctx, M_Product_ID);
					precision = product.getUOMPrecision();
				}
				QtyEntered = QtyEntered.setScale(precision, BigDecimal.ROUND_HALF_DOWN);
						
				//	Create new InOut Line
				MInOutLine iol = new MInOutLine (inout);
				iol.setM_Product_ID(M_Product_ID, C_UOM_ID);	//	Line UOM
		

				//Le seteo a la cantidad que va a inventario la cantidad Recibida * unidad de compra/unidad de venta
				getPUPS(C_OrderLine_ID);
				Integer auxQty = ((QtyEntered.intValue()*PU)/PS);
				iol.setTargetQty(new BigDecimal(auxQty));
				iol.setPickedQty(new BigDecimal(auxQty));
				iol.setQty(new BigDecimal(auxQty));
				iol.setMovementQty(new BigDecimal(auxQty));
				QtyEntered = new BigDecimal(auxQty);
				
				//
				MOrderLine ol = null;
				if (C_OrderLine_ID != 0)
				{
					iol.setC_OrderLine_ID(C_OrderLine_ID);
					ol = new MOrderLine (ctx, C_OrderLine_ID, null);
					//	iol.setOrderLine(ol, M_Locator_ID, QtyEntered);
					if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0)
					{
						iol.setMovementQty(QtyEntered
							.multiply(ol.getQtyOrdered())
							.divide(ol.getQtyEntered(), 12, BigDecimal.ROUND_HALF_UP));
						iol.setC_UOM_ID(ol.getC_UOM_ID());
					}
					iol.setM_AttributeSetInstance_ID(ol.getM_AttributeSetInstance_ID());
					iol.setDescription(ol.getDescription());
					//
					iol.setC_Project_ID(ol.getC_Project_ID());
					iol.setC_ProjectPhase_ID(ol.getC_ProjectPhase_ID());
					iol.setC_ProjectTask_ID(ol.getC_ProjectTask_ID());
					iol.setC_Activity_ID(ol.getC_Activity_ID());
					iol.setC_Campaign_ID(ol.getC_Campaign_ID());
					iol.setAD_OrgTrx_ID(ol.getAD_OrgTrx_ID());
					iol.setUser1_ID(ol.getUser1_ID());
					iol.setUser2_ID(ol.getUser2_ID());
				}
				else if (il != null)
				{
				//	iol.setInvoiceLine(il, M_Locator_ID, QtyEntered);
					if (il.getQtyEntered().compareTo(il.getQtyInvoiced()) != 0)
					{
						iol.setQtyEntered(QtyEntered
							.multiply(il.getQtyInvoiced())
							.divide(il.getQtyEntered(), 12, BigDecimal.ROUND_HALF_UP));
						iol.setC_UOM_ID(il.getC_UOM_ID());
					}
					iol.setDescription(il.getDescription());
					iol.setC_Project_ID(il.getC_Project_ID());
					iol.setC_ProjectPhase_ID(il.getC_ProjectPhase_ID());
					iol.setC_ProjectTask_ID(il.getC_ProjectTask_ID());
					iol.setC_Activity_ID(il.getC_Activity_ID());
					iol.setC_Campaign_ID(il.getC_Campaign_ID());
					iol.setAD_OrgTrx_ID(il.getAD_OrgTrx_ID());
					iol.setUser1_ID(il.getUser1_ID());
					iol.setUser2_ID(il.getUser2_ID());
				}
				//	Charge
				if (M_Product_ID == 0)
				{
					if (ol != null && ol.getC_Charge_ID() != 0)			//	from order
						iol.setC_Charge_ID(ol.getC_Charge_ID());
					else if (il != null && il.getC_Charge_ID() != 0)	//	from invoice
						iol.setC_Charge_ID(il.getC_Charge_ID());
				}
				//
				iol.setM_Locator_ID(M_Locator_ID);
				if (!iol.save()){
		
				}
				//	Create Invoice Line Link
				else if (il != null)
				{
					il.setM_InOutLine_ID(iol.getM_InOutLine_ID());
					il.save();
				}
		    } //while
		    
			 rs.close();
			 pstmt.close();
		
		} //try
		catch (Exception e) {
			log.log(Level.SEVERE, SQL, e.getMessage());
		}
		
		
		/**
		 *  Update Header
		 *  - if linked to another order/invoice - remove link
		 *  - if no link set it
		 */
		if (p_order != null && p_order.getC_Order_ID() != 0)
		{
			inout.setC_Order_ID (p_order.getC_Order_ID());
			inout.setAD_OrgTrx_ID(p_order.getAD_OrgTrx_ID());
			inout.setC_Project_ID(p_order.getC_Project_ID());
			inout.setC_Campaign_ID(p_order.getC_Campaign_ID());
			inout.setC_Activity_ID(p_order.getC_Activity_ID());
			inout.setUser1_ID(p_order.getUser1_ID());
			inout.setUser2_ID(p_order.getUser2_ID());
			inout.setDateOrdered(p_order.getDateOrdered());
		}
		if (m_invoice != null && m_invoice.getC_Invoice_ID() != 0)
		{
			if (inout.getC_Order_ID() == 0)
				inout.setC_Order_ID (m_invoice.getC_Order_ID());
			inout.setC_Invoice_ID (m_invoice.getC_Invoice_ID());
			inout.setAD_OrgTrx_ID(m_invoice.getAD_OrgTrx_ID());
			inout.setC_Project_ID(m_invoice.getC_Project_ID());
			inout.setC_Campaign_ID(m_invoice.getC_Campaign_ID());
			inout.setC_Activity_ID(m_invoice.getC_Activity_ID());
			inout.setUser1_ID(m_invoice.getUser1_ID());
			inout.setUser2_ID(m_invoice.getUser2_ID());			
		}
		
		inout.setXX_CompleteReception("Y");
		inout.setXX_InOutStatus("RE");
		inout.save();
		
		p_order.setXX_OrderStatus("RE");
		p_order.save();
		
		//Realizado por RArvelo y RPrincipal 
		//Genera alerta Órdenes de compra pendientes de chequear
		if((p_order.getXX_OrderStatus().equals("RE")) && (p_order.get_ValueAsBoolean("XX_Alert4")==false))
		{
			try
			{
				Env.getCtx().setContext("#XX_TypeAlertCO","CO");
				Env.getCtx().setContext("#XX_InOutCT",inout.get_ID());
				
				MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID"),inout.get_ID()); 
				mpi.save();
				
				ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
				pi.setRecord_ID(mpi.getRecord_ID());
				pi.setAD_PInstance_ID(mpi.get_ID());
				pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
				pi.setClassName(""); 
				pi.setTitle(""); 
				
				ProcessCtl pc = new ProcessCtl(null ,pi,null); 
				pc.start();
			}
			catch(Exception e)
			{
				log.log(Level.SEVERE,e.getMessage());
			}
			
		}
		
		//Realizado por Rosmaira Arvelo
		//Genera la alerta Cierre de expediente
		if((p_order.getXX_Alert6()==false)&&(p_order.getXX_OrderStatus().equals("RE"))&&(p_order.getC_Country_ID()!=339))
		{
			try
			{
				Env.getCtx().setContext("#XX_TypeAlertCE","CE");
				Env.getCtx().setContext("#XX_OrderCECT",p_order.get_ID());
				
				MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID"),p_order.get_ID()); 
				mpi.save();
				
				ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
				pi.setRecord_ID(mpi.getRecord_ID());
				pi.setAD_PInstance_ID(mpi.get_ID());
				pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
				pi.setClassName(""); 
				pi.setTitle(""); 
				
				ProcessCtl pc = new ProcessCtl(null ,pi,null); 
				pc.start();
			}
			catch(Exception e)
			{
				log.log(Level.SEVERE,e.getMessage());
			}
			
		}//fin RArvelo y RPrincipal*/
		
		//Realizado por Rosmaira Arvelo				
		MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(p_order.get_ID(),"OR"),null);
				
		//Llama al proceso cerrar alerta O/C por recibirse
		if ((p_order.getXX_OrderStatus().equals("RE"))&&(p_order.getXX_Alert5()==true)&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("OR")))
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
				log.log(Level.SEVERE,e.getMessage());
			}
			
		}//fin RArvelo
		
		return true;
	}   //  save
	
	private void sendMailAdminManager(MInOut inOut)
	{
		MOrder order = new MOrder(Env.getCtx(), new Integer(inOut.getC_Order_ID()), null);
		
		Integer adminManagerBP = getAdminManager(inOut);
		Integer adminManagerUser = getAD_User_ID(adminManagerBP);
		
		String Mensaje = " <"+order.getDocumentNo()+"> fue recibida, debe hacer llegar la factura original a Boleíta.";
		
		Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_SENDINVOICE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, adminManagerUser,null);

		try { 
			f.ejecutarMail();    
		} catch (Exception e) { 
			e.printStackTrace();
		}
		f = null;
	}
	
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner + " " +
					 "AND ISACTIVE='Y' " +
					 "AND AD_CLIENT_ID IN (0,"+getAD_Client_ID()+")";
		
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
	
	private Integer getAdminManager(MInOut inOut)
	{
		Integer adminManagerID=0;
		
		String SQL = "Select C_BPartner_ID FROM C_BPartner " +
					 "WHERE AD_ORG_ID=" + inOut.getAD_Org_ID() + " "+
					 "AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_STOREMAN_ID")+ " "+
					 "AND ISACTIVE='Y' " +
					 "AND AD_CLIENT_ID IN (0,"+getAD_Client_ID()+")";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				adminManagerID = rs.getInt("C_BPartner_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return adminManagerID;
	}
	
	
	/*
	 *	returns true si el proveedor tiene el beneficio de no chequeo de las O/C
	 */
	private boolean noCheckBenefit(MOrder order){
				
		String sql = retornaSqlBenefitVendor(order.getC_BPartner_ID());
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BigDecimal percentage = BigDecimal.ZERO;
		try {
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				if(rs.getInt("XX_VCN_BENEFITS_ID") == Env.getCtx().getContextAsInt("#XX_L_BENEFITNOCHECK_ID")){
					percentage = rs.getBigDecimal("ValueNumber");
				}
			}	
						
		}catch (SQLException e) {
			return false;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		//Ahora si tiene porcentaje mayor a 0 veo si el average del cumplimiento de (calidad, surtido y cantidad) 
		//cumple este porcentaje
		if(percentage.compareTo(BigDecimal.ZERO)>0)
		{
			String sql_AVG = "SELECT ROUND(SUM(AVG(XX_POINTS))/SUM(EC.XX_POINT) * 100, 2) AVG " +
							 "FROM XX_VCN_VENDORRATING VR, XX_VCN_EVALUATIONCRITERIA EC  " +
						     "WHERE VR.C_BPARTNER_ID=" + order.getC_BPartner_ID() + " " +
						     "AND VR.XX_VCN_EVALUATIONCRITERIA_ID IN " +
						     "(" +
						     Env.getCtx().getContext("#XX_L_EC_COMPIDELIVEQUANTI_ID") + "," +
						     Env.getCtx().getContext("#XX_L_EC_COMPIDELIVEQUALI_ID") + "," +
						     Env.getCtx().getContext("#XX_L_EC_COMPIDELIVEASSORT_ID") +
						     ") " +
						     "AND VR.XX_VCN_EVALUATIONCRITERIA_ID = EC.XX_VCN_EVALUATIONCRITERIA_ID " +
			"GROUP BY VR.XX_VCN_EVALUATIONCRITERIA_ID, EC.XX_POINT";
			
			BigDecimal AVG = BigDecimal.ZERO;
			try {
				pstmt = DB.prepareStatement(sql_AVG, null);
				rs = pstmt.executeQuery();
				
				if (rs.next()){
					AVG = rs.getBigDecimal("AVG");
					
					//Si el promedio arrojado es
					if(percentage.compareTo(AVG)<=0){
						return true;
					}
				}	
							
			}catch (SQLException e) {
				return false;
			}finally{
				
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}	
		}
		
		return false;
	}
	
	/**
	 * Calcula los beneficios del Proveedor
	 */
	public String retornaSqlBenefitVendor(Integer m_C_BPartner_ID) {
		String sql = new String();
		sql = 	"select (select a.Name from XX_VCN_Benefits a where a.XX_VCN_Benefits_ID = XX_VCN_BENEFITSMATRIX.XX_VCN_Benefits_ID) Name, XX_VCN_BENEFITSMATRIX.XX_VCN_Benefits_ID , ValueNumber " +
				"FROM XX_VCN_BENEFITSMATRIX " +
				"WHERE XX_VCN_BENEFITSMATRIX.XX_VENDORTYPE_ID = (Select bp.XX_VENDORTYPE_ID from C_BPartner bp where bp.C_BPartner_ID = "+m_C_BPartner_ID+") and " +
				"         (XX_VCN_BENEFITSMATRIX.XX_ISALLVENDOR = 'Y' AND " +
				"         (SELECT AVG(venRa.XX_POINTS) FROM XX_VCN_VENDORRATING venRa " +
				"          WHERE venRa.C_BPARTNER_ID = "+m_C_BPartner_ID+" and " +
				"                venRa.XX_VCN_EvaluationCriteria_ID = "+Env.getCtx().getContext("#XX_L_EC_TOTALSCOREOC_ID")+" and " +
				"                venRa.XX_C_ORDER_ID in (select C_ORDER_ID " +
				"                                  from C_ORDER " +
				"                                  where C_ORDER_ID is not null " +
				"								   AND XX_EVALUATED = 'Y'" +
				"                                  and round(sysdate-XX_ENTRANCEDATE,2) < "+Env.getCtx().getContext("#XX_L_DAYSCALCULAVENDORRATING")+") ) between (XX_VCN_BENEFITSMATRIX.XX_LOWERLIMIT*100) and (XX_VCN_BENEFITSMATRIX.XX_HIGHERLIMIT*100) ) or " +
				"         (XX_VCN_BENEFITSMATRIX.XX_ISALLVENDOR = 'N' AND " +
				"         XX_VCN_BENEFITSMATRIX.C_BPARTNER_ID = "+m_C_BPartner_ID+" and " +
				"         (SELECT AVG(venRa2.XX_POINTS) FROM XX_VCN_VENDORRATING venRa2 " +
				"          WHERE venRa2.C_BPARTNER_ID = "+m_C_BPartner_ID+" and " +
				"                venRa2.XX_VCN_EvaluationCriteria_ID = "+Env.getCtx().getContext("#XX_L_EC_TOTALSCOREOC_ID")+" and " +
				"                venRa2.XX_C_ORDER_ID in (select C_ORDER_ID " +
				"                                  from C_ORDER " +
				"                                  where C_ORDER_ID is not null " +
				"								   AND XX_EVALUATED = 'Y'" +
				"                                  and round(sysdate-XX_ENTRANCEDATE,2) < "+Env.getCtx().getContext("#XX_L_DAYSCALCULAVENDORRATING")+") ) between (XX_VCN_BENEFITSMATRIX.XX_LOWERLIMIT * 100) and (XX_VCN_BENEFITSMATRIX.XX_HIGHERLIMIT * 100) )";
		return sql;
	}	
	
	//Realizado por Rosmaira Arvelo
	/*
	 *	Obtengo el ID de la tarea critica segun la orden
	 */
	private static Integer getCriticalTaskForClose(Integer order, String typeTask){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTaskForClose_ID FROM XX_VMR_CriticalTaskForClose "
			   + "WHERE XX_ActualRecord_ID="+order+" AND XX_TypeTask='"+typeTask+"'";
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
	
	private static void getPUPS(int C_OrderLine_ID){
		
		//se obtienen la unidad de compra y la unidad de venta para el CV
		String SQL =
			"Select uc.XX_UNITCONVERSION, us.XX_UNITCONVERSION "+
			"from XX_VMR_PO_LINEREFPROV lp, XX_VMR_UnitConversion uc, XX_VMR_UnitConversion us "+
			"where "+
			"lp.XX_PiecesBySale_ID = us.XX_VMR_UnitConversion_ID "+
			"and lp.XX_VMR_UnitConversion_ID = uc.XX_VMR_UnitConversion_ID "+
			"and lp.XX_VMR_PO_LINEREFPROV_ID=" +
			"(SELECT XX_VMR_PO_LINEREFPROV_ID FROM C_ORDERLINE WHERE C_ORDERLINE_ID="+C_OrderLine_ID+")";
		
		PU=1;
		PS=1;
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();	
			
			while(rs.next()) //compruebo que ya exista la matriz
			{	
				PU=rs.getInt(1);
				PS=rs.getInt(2);
			}
						
			rs.close();
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			e.getMessage();
		}
	}
}
