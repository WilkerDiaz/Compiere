package compiere.model.cds.processes;

import org.compiere.model.X_M_InOut;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MWarehouse;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;


import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.MVMROrder;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_M_Transaction_Movement_Type;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_Ref_XX_OrderRequestType;
import compiere.model.cds.X_Ref_XX_OrderStatus;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VMR_Order;
import compiere.model.cds.X_XX_VMR_OrderRequestDetail;

/**
 * Proceso de impresión de Etiquetas de Despacho Directo. Antes de imprimir las etiquetas crea
 * la recepción, chequeo, cabecera de distribución y pedido y completa todos los procesos pertinentes para
 * completar la recepción y chequeo y para aprobar la distribución y pedido.
 * @author Gabrielle Huchet
 *
 */


public class XX_PrintLabelsDirectDispatch extends SvrProcess {

	static CLogger  log = CLogger.getCLogger(XX_PrintLabelsDirectDispatch.class);
	static int PU=0;
	static int PS=0;
	static Object m_readLock = new Object();
	static int glued = -1, hanging = -1;
	static int check_assistant = 0;
	private static Boolean finalizado;
	private static Boolean recibido;
	private static Boolean chequeado;
	private static Boolean pedidos;
	private static Trx Transaccion = Trx.get("Transaccion");

	/**
	 * 	Is Process Active
	 *	@return true if active
	 */
	protected static boolean isProcessActive()
	{
		int  aux = Env.getCtx().getContextAsInt("#XX_VMR_PRINTDDACTIVE");
		if(aux == 1){
			return true;
		}else return false;

	}
	
	/**
	 * 	Set Process (in)active
	 *	@param active active
	 */
	protected static void setProcessActive (int active)
	{
		Env.getCtx().setContext("#XX_VMR_PRINTDDACTIVE",active);
	}

	@Override
	protected String doIt() throws Exception {

		finalizado =false;
		recibido = true;
		chequeado = true;
		pedidos = true;
		//Si se llama el proceso desde una forma y no desde una ventana con parametros
			if(hanging == -1)
				hanging = Env.getCtx().getContextAsInt("#XX_VMR_PRINTLABEL_Hanging");
			if(glued == -1)
				glued = Env.getCtx().getContextAsInt("#XX_VMR_PRINTLABEL_Glued");

			//Remove the no longer necessary items on the context				
		Env.getCtx().remove("#XX_VMR_PRINTLABEL_Hanging");
		Env.getCtx().remove("#XX_VMR_PRINTLABEL_Glued");
		Env.getCtx().remove("#XX_VMR_PRINTLABEL_Reprint");
		String msg = "";
		try{	
			int C_Order_ID = getRecord_ID();
		
			//Busco si ya la orden de compra tiene recepción
			String SQL = "\nSELECT M_INOUT_ID FROM M_INOUT " +
						 "\nWHERE C_ORDER_ID="+C_Order_ID+" "+
					     "\nAND AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID();
			
			PreparedStatement pstmt= null;
			ResultSet rs= null;
			Integer M_InOut = 0;
			try
			{
				pstmt = DB.prepareStatement(SQL, null); 
				rs = pstmt.executeQuery();
	
				while(rs.next()){

					M_InOut = rs.getInt("M_INOUT_ID");
				}
			}
			catch (Exception e) {
				log.log(Level.SEVERE, SQL, e.getMessage());
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			int pedidoAntes = getOrderRequest(C_Order_ID);
			
			completeProcessBeforePrintLabels(C_Order_ID, getAD_Client_ID(), Transaccion, M_InOut);
				
			if (finalizado) {
				Transaccion.commit();
			} else {
				Transaccion.rollback();
				msg = "El proceso no se pudo completar con éxito. Contacte al administrador del sistema. ";
				ADialog.error(1, new Container(), msg);
				setProcessActive(0);
				return "";
			}
			if(!recibido){
				ADialog.warn(1, new Container(), "El proceso no se pudo completar con éxito. No se completo recepción. " +
						"\nVuelva a intentar o contacte al administrador del sistema.");
				setProcessActive(0);
				return "";
			}		
			if(!chequeado){
				ADialog.warn(1, new Container(), "El proceso no se pudo completar con éxito. No se completo chequeo." +
						"\nVuelva a intentar o contacte al administrador del sistema.");
				setProcessActive(0);
				return "";
			}		
			if(!pedidos){
				ADialog.warn(1, new Container(), "El proceso no se pudo completar con éxito. No se completo la creación del pedido" +
						"\nVuelva a intentar o contacte al administrador del sistema.");
				setProcessActive(0);
				return "";
			}		
			else {
				boolean imprimir = true;
				if (pedidoAntes != 0) {
					msg = "Tenga en cuenta que las etiquetas de esta orden de compra han sido impresas anteriormente." +
							"\n¿Desea Continuar?";
					imprimir = ADialog.ask(1, new Container(), msg);
					}
				if(imprimir){
					//Busco ID del pedido asociado a la 0/C
					int XX_VMR_Order_ID = getOrderRequest(C_Order_ID);
					if(XX_VMR_Order_ID!=0){
						printLabels(XX_VMR_Order_ID);
						msg = "Proceso Completado.";
						ADialog.info(1, new Container(), msg);
					}else {
						msg = "El proceso no se pudo completar con éxito. " +
								"\nLa O/C seleccionada no tiene pedido asociado. " +
								"\nContacte al administrador del sistema. ";
						ADialog.error(1, new Container(), msg);
						setProcessActive(0);
						return "";
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			msg = "El proceso no se pudo completar con éxito. Contacte al administrador del sistema. ";
			ADialog.error(1, new Container(), msg);
			setProcessActive(0);
			return "";
		}
		setProcessActive(0);
		return msg;
	}

	
	private void printLabels(int XX_VMR_Order_ID) throws Exception {
		
		FormFrame form = new FormFrame();
		int form_id = 0;
		
		X_XX_VMR_Order placedOrder = new X_XX_VMR_Order(getCtx(),XX_VMR_Order_ID, null);
		
		//Generar el Consecutivo de Precios a los productos de detalles de pedido que no lo tengan creado
		try {
			new Utilities().GenerarConsecutivo(placedOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		placedOrder.setXX_AssignmentDate(placedOrder.getUpdated());
		placedOrder.save();
	
		//Seteo variables de contexto del proceso de imprimir etiquetas
		synchronized( m_readLock ) {

			//Context Variables are created, they are to be removed in the Form
			
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_PlacedOrder_ID", XX_VMR_Order_ID);
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Hanging",hanging);
			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Glued", glued);
			
		//	Displays the Form
			form.setName("Print Product Labels");
			form_id = Env.getCtx().getContextAsInt("#XX_L_FORMPRINTPRODUCTLABEL_ID");			
			form.openForm(form_id);
		}
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		
	}

	/**Este método crea la recepción, completa el chequeo y crea la cabecera de distribución y el pedido correspondiente a la orde de compra
	 * */
	public static synchronized String completeProcessBeforePrintLabels(int C_Order_ID, int AD_Client_ID, Trx trx, int inOutID){
		
			MInOut inOut = new MInOut(Env.getCtx(), inOutID, trx);
			MOrder p_order = new MOrder(Env.getCtx(), C_Order_ID, trx);
			boolean isOk = true;
			String sql = "";
			PreparedStatement pstmt= null;
			ResultSet rs= null;
			try{
				if(inOutID == 0){
					recibido = false;
					chequeado = false;
					pedidos=false;
					
					inOut.setC_Order_ID(C_Order_ID); 
					inOut.setM_Warehouse_ID(p_order.getM_Warehouse_ID());
					inOut.setC_BPartner_ID(p_order.getC_BPartner_ID());
					inOut.setC_BPartner_Location_ID(p_order.getC_BPartner_Location_ID());
					inOut.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPERECEIPT_ID"));
					inOut.setMovementType(X_Ref_M_Transaction_Movement_Type.VENDOR_RECEIPTS.getValue());
					inOut.setAD_Client_ID(AD_Client_ID);
					inOut.set_Value("XX_POType",p_order.getXX_POType());
					inOut.save(trx);
					//Busco el locator de la tienda en tránsito para asignarlo a las lineas de recepcion
					sql = "\nSELECT M_LOCATOR_ID FROM M_LOCATOR " +
								 "\nWHERE M_WAREHOUSE_ID="+inOut.getM_Warehouse_ID()+" AND ISDEFAULT='N' AND ISACTIVE='Y' " +
							 	 "\nAND AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID();
					
					
					int M_Locator_ID = 0;
					try
					{
						pstmt = DB.prepareStatement(sql, null); 
						rs = pstmt.executeQuery();
						while(rs.next()){
							M_Locator_ID = rs.getInt("M_Locator_ID");
						}
						
						
					}
					catch (Exception e) {
						log.log(Level.SEVERE, sql, e.getMessage());
						finalizado= false;
					}
					finally {
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}
					//Creo y guardo las lineas de recepción
					isOk = saveData(Env.getCtx(), p_order, M_Locator_ID, inOut ,trx);
					if (isOk) {
						trx.commit();
					}
					recibido = isOk;
				}
			
				//Si se creó la recepción se completa el chequeo
				if(isOk && inOut.getXX_InOutStatus().compareTo("RE")==0){
					chequeado = false;
					pedidos=false;
					isOk = completeCheckup(inOut.get_ID(), C_Order_ID, trx);				  
					chequeado = isOk;
				}
				

				inOut = new MInOut(Env.getCtx(),inOut.get_ID(), null);
				System.out.println("A: "+inOut.get_ID());
				System.out.println("B: "+inOut.getXX_InOutStatus());
				System.out.println("C: "+inOut.getXX_CompleteCheckup());
				if(isOk && inOut.getXX_InOutStatus().compareTo("CH")==0 && inOut.getXX_CompleteCheckup().compareTo("Y")==0){
					
					pedidos =false;
					int distHeaderID = getDistribution(C_Order_ID);
					int pedidoID = 0;

					pedidoID = getOrderRequest(C_Order_ID);
					
					if( distHeaderID == 0){
						//Se crea cabecera de Distribución
						distHeaderID = createDistribution(C_Order_ID,trx);
					}

					if(pedidoID == 0 && distHeaderID > 0){
						//Se crea el Pedido y se coloca el estado en tránsito
						pedidoID = createOrderRequest(C_Order_ID, distHeaderID, trx);
					}
					
					if (pedidoID > 0){
						//Se llena los Detalles del Pedido
						isOk = createOrderRequestDetail(C_Order_ID, pedidoID, trx);
					}else isOk =false;
					
					pedidos = isOk;
				}
				
			}catch (Exception e) {
				e.printStackTrace();
				finalizado= false;
			}
			if(!isOk){
				finalizado = false;
			}else finalizado = true;
		return "";
	}

	/**Crea la recepción de la O/C*/
	public static boolean saveData(Ctx ctx, MOrder p_order, int M_Locator_ID, MInOut inout, Trx trx)
	{
		    
		String sql = "\nSELECT l.QtyOrdered-SUM(COALESCE(m.Qty,0)) as Qty, l.C_UOM_ID as UOM, " +
				"\nCOALESCE(l.M_Product_ID,0) as Product, l.C_OrderLine_ID as Line " +
				"\nFROM C_OrderLine l LEFT OUTER JOIN M_MatchPO m ON (l.C_OrderLine_ID=m.C_OrderLine_ID AND m.M_InOutLine_ID IS NOT NULL) " +
				"\nLEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID) LEFT OUTER JOIN C_Charge c ON (l.C_Charge_ID=c.C_Charge_ID) " +
				"\nLEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID) " +
				"\nWHERE l.C_Order_ID="+p_order.getC_Order_ID()+ " " +
				"\nAND l.AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID()+" "+
				"\nGROUP BY l.QtyOrdered,CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, " +
				"\nl.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name), l.M_Product_ID,COALESCE(p.Name,c.Name), l.Line,l.C_OrderLine_ID " +
				"\nORDER BY l.Line";
		
		PreparedStatement pstmt = null;
	    ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null); 
		    rs = pstmt.executeQuery();
		    
		    while(rs.next()){
		    	
				Number d = rs.getInt("Qty");     							  //  1-Qty
				BigDecimal QtyEntered = new BigDecimal(d.doubleValue());
				int C_UOM_ID = rs.getInt("UOM");                  			  //  2-UOM
				int M_Product_ID = rs.getInt("Product");                      //  3-Product
				int C_OrderLine_ID = rs.getInt("Line");     				  //  4-OrderLine  
				
				//	Precision of Qty UOM
				int precision = 2;
				if (M_Product_ID != 0)
				{
					MProduct product = MProduct.get(ctx, M_Product_ID);
					precision = product.getUOMPrecision();
				}
				QtyEntered = QtyEntered.setScale(precision, BigDecimal.ROUND_HALF_DOWN);
						
				//Se crea un nuevo M_InOutLine por cada producto diferente en la O/C
				MInOutLine iol = new MInOutLine (inout);
				iol.setM_Product_ID(M_Product_ID, C_UOM_ID);	//	Line UOM
				iol.setAD_Client_ID(p_order.getAD_Client_ID());

				//Se agrega a la cantidad que va a inventario la cantidad Recibida * unidad de compra/unidad de venta
				getPUPS(C_OrderLine_ID);
				iol.setM_Locator_ID(M_Locator_ID);
				Integer auxQty = ((QtyEntered.intValue()*PU)/PS);
				iol.setTargetQty(new BigDecimal(auxQty));
				iol.setPickedQty(new BigDecimal(auxQty));
				iol.setQty(new BigDecimal(auxQty));
				iol.setMovementQty(new BigDecimal(auxQty));
				QtyEntered = new BigDecimal(auxQty);
				
				MOrderLine ol = null;
				
				if (C_OrderLine_ID != 0){
					iol.setC_OrderLine_ID(C_OrderLine_ID);
					ol = new MOrderLine (ctx, C_OrderLine_ID, trx);
					
					if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0)
					{
						iol.setMovementQty(QtyEntered
							.multiply(ol.getQtyOrdered())
							.divide(ol.getQtyEntered(), 12, BigDecimal.ROUND_HALF_UP));
						iol.setC_UOM_ID(ol.getC_UOM_ID());
					}
					
					iol.setM_AttributeSetInstance_ID(ol.getM_AttributeSetInstance_ID());
					iol.setDescription(ol.getDescription());
					iol.setC_Project_ID(ol.getC_Project_ID());
					iol.setC_ProjectPhase_ID(ol.getC_ProjectPhase_ID());
					iol.setC_ProjectTask_ID(ol.getC_ProjectTask_ID());
					iol.setC_Activity_ID(ol.getC_Activity_ID());
					iol.setC_Campaign_ID(ol.getC_Campaign_ID());
					iol.setAD_OrgTrx_ID(ol.getAD_OrgTrx_ID());
					iol.setUser1_ID(ol.getUser1_ID());
					iol.setUser2_ID(ol.getUser2_ID());
				}
			
				//	Charge
				if (M_Product_ID == 0)
				{
					if (ol != null && ol.getC_Charge_ID() != 0)			//	from order
						iol.setC_Charge_ID(ol.getC_Charge_ID());
				}
				//
				iol.setM_Locator_ID(M_Locator_ID);
				iol.save(trx);

		    } //fin del while
		    
		    inout.setAD_OrgTrx_ID(p_order.getAD_OrgTrx_ID());
			inout.setC_Project_ID(p_order.getC_Project_ID());
			inout.setC_Campaign_ID(p_order.getC_Campaign_ID());
			inout.setC_Activity_ID(p_order.getC_Activity_ID());
			inout.setUser1_ID(p_order.getUser1_ID());
			inout.setUser2_ID(p_order.getUser2_ID());
			inout.setDateOrdered(p_order.getDateOrdered());
			
			//Se completa la Recepción
			inout.setXX_CompleteReception("Y");
			inout.setXX_InOutStatus("RE");
			inout.save(trx);
			
			p_order.setXX_OrderStatus("RE");
			p_order.save(trx);
		} //try
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e.getMessage());
			finalizado =false;
			return false;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			
		}
		return true;
	}   //  save

	private static boolean completeCheckup(int inOutID, int orderID, Trx trx) {
		
		MInOut inOut = new MInOut(Env.getCtx(), inOutID, trx);
		MOrder p_order = new MOrder(Env.getCtx(), orderID,  trx);
		
		try{
			//Completar Chequeo
			inOut.setDocAction(X_M_InOut.DOCACTION_Complete);
			DocumentEngine.processIt(inOut, X_M_InOut.DOCACTION_Complete);
		    inOut.setXX_InOutStatus("CH");
		    inOut.setXX_CompleteCheckup("Y");
		    
		    //Agregar información de chequeo a la orden de compra
		    p_order.setXX_OrderStatus(X_Ref_XX_OrderStatus.CHEQUEADA.getValue());
			p_order.setXX_ReceptionDate(inOut.getCreated());
			p_order.setXX_CheckupDate(inOut.getCreated());
			p_order.save(trx);
		    inOut.save(trx);
		    trx.commit();
		}catch (Exception e) {
			return false;
		}

		return true;
	}
	
	private static int createDistribution(int orderID, Trx trx) {

		MOrder p_order = new MOrder(Env.getCtx(), orderID, trx);

		String sql = "";
		PreparedStatement pstmt= null;
		ResultSet rs= null;
		
		//Se crea cabecera de Distribución
		MVMRDistributionHeader header = new MVMRDistributionHeader(Env.getCtx(), 0, trx);
		header.setC_Order_ID(orderID);
		header.setAD_Client_ID(p_order.getAD_Client_ID());
		
		sql = "\nSELECT D.VALUE||'-'||D.NAME, " +
		"\nC.NAME, PA.NAME " +
		"\nFROM C_ORDER O " +
		"\nINNER JOIN XX_VMR_DEPARTMENT D ON (O.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID) " +
		"\nINNER JOIN XX_VMR_PACKAGE PA ON (O.XX_VMR_PACKAGE_ID = PA.XX_VMR_PACKAGE_ID) " +
		"\nINNER JOIN XX_VMR_COLLECTION C ON (PA.XX_VMR_COLLECTION_ID = C.XX_VMR_COLLECTION_ID) " +
		"\nWHERE O.C_ORDER_ID = "+orderID+" "+
 		"\nAND O.AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID();
		
		pstmt = null;
		rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				header.setXX_Department_Name(rs.getString(1));
				header.setXX_CollectionName(rs.getString(2));
				header.setXX_PackageName(rs.getString(3));
			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//Se pasa a pendiente y aprobada la cabecera de distribución
		header.setXX_HasTextilProducts(true);
		header.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.PENDIENTE.getValue());	
		header.save(trx);
		header.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.APROBADA.getValue());
		header.save(trx);
		
		return (header.get_ID());

	}
	
	private static int createOrderRequest(int orderID, int distHeaderID, Trx trx) {
		
		int pedidoID = 0;
		MOrder p_order = new MOrder(Env.getCtx(), orderID, trx);
		
		String sql = "";
		PreparedStatement pstmt= null;
		ResultSet rs= null;

		sql = "SELECT MAX(XX_CORRELATIVEGENERATOR) FROM XX_VMR_ORDER " +
				"WHERE AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID();
		int correlativo= 1;
		
		try {
			pstmt= DB.prepareStatement(sql,null);
			rs = pstmt.executeQuery();
			if(rs.next())
				correlativo= correlativo + rs.getInt("MAX(XX_CORRELATIVEGENERATOR)");			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql, e.getMessage());
			return 0;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		try{
			DecimalFormat formato = new DecimalFormat("0000000");
			MWarehouse tienda=new MWarehouse(Env.getCtx(),p_order.getM_Warehouse_ID(),trx);
			
			//Se crea el Pedido y se coloca el estado en tránsito
			MVMROrder pedido = new MVMROrder(Env.getCtx(),0,trx);
			pedido.setXX_OrderBecoCorrelative(X_Ref_XX_OrderRequestType.DESPACHO_DIRECTO.getValue()+tienda.getValue()+""+distHeaderID);
			pedido.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_TRÁNSITO.getValue());//En tránsito
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			//calendar.setTime(p_order.getXX_EstimatedDate());
			//pedido.set_ValueNoCheck("Created", p_order.getXX_EstimatedDate());
			pedido.setXX_OrderRequestType(X_Ref_XX_OrderRequestType.DESPACHO_DIRECTO.getValue());
			pedido.setXX_WeekCreated(Utilities.getWeekOfYear(calendar));
			pedido.setC_Order_ID(p_order.getC_Order_ID());
			pedido.setXX_CorrelativeGenerator(Integer.parseInt(formato.format(correlativo)));
			pedido.setM_Warehouse_ID(tienda.get_ID());
			pedido.setAD_Client_ID(p_order.getAD_Client_ID());
			pedido.setXX_VMR_DistributionHeader_ID(distHeaderID);
			pedido.save(trx);
			pedidoID = pedido.get_ID();
		}catch (Exception e) {
			return 0;
		}
		
		return pedidoID;
	}

	
	private static boolean createOrderRequestDetail(int orderID, int pedidoID, Trx trx) {
		
		MOrder p_order = new MOrder(Env.getCtx(), orderID, trx);
		
		String sql = "";
		
		 //Se obtienen las referencias de proveedor correspondientes a la orden de compra para mandarlos al pedido
		sql = "\nSELECT XX_VMR_PO_LINEREFPROV_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID," +
				"\nROUND(XX_SALEPRICE,2), ROUND(XX_TAXAMOUNT,2), ROUND(XX_SALEPRICEPLUSTAX,2), " +
				"\nC_TAXCATEGORY_ID, PRICEACTUAL, XX_UNITPURCHASEPRICE " +
				"\nFROM XX_VMR_PO_LINEREFPROV " +
				"\nWHERE C_ORDER_ID="+orderID+" "+
				"\nAND AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID();
	
		PreparedStatement prst2 = null;
		ResultSet rs2 = null;
		
		try {
			prst2 = DB.prepareStatement(sql,null);
			rs2 = prst2.executeQuery();
			while(rs2.next()){
				
				//Se obtienen las referencia de producto beco correspondientes a las referencias de proveedor obtenidas previamente
				String sql3 = "\nSELECT P.M_PRODUCT, P.XX_QUANTITYV, IOL.M_ATTRIBUTESETINSTANCE_ID " + 
							  "\nFROM XX_VMR_REFERENCEMATRIX P, M_INOUT IO, M_INOUTLINE IOL  "+
							  "\nWHERE P.XX_QUANTITYV<>0 AND " +
							  "\nP.XX_VMR_PO_LINEREFPROV_ID ="+rs2.getInt(1) +
							  "\n AND IO.C_ORDER_ID ="+orderID+" AND IOL.M_PRODUCT_ID = P.M_PRODUCT " +
							  "\nAND IOL.M_INOUT_ID = IO.M_INOUT_ID " +
							  "\nAND IO.AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID();
						
				PreparedStatement prst3= DB.prepareStatement(sql3,null);
				ResultSet rs3 = prst3.executeQuery();
				try {			
					//Se llena los Detalles del Pedido
					while(rs3.next()){
						
							int detalleID = getOrderRequestDetail(rs3.getInt(1), pedidoID);
							if(detalleID == 0){
								X_XX_VMR_OrderRequestDetail detalle_pedido = new X_XX_VMR_OrderRequestDetail(Env.getCtx(), detalleID, trx);
										
								detalle_pedido.setXX_VMR_Order_ID(pedidoID);
								detalle_pedido.setXX_VMR_Category_ID(p_order.getXX_Category_ID());
								detalle_pedido.setXX_VMR_Department_ID(p_order.getXX_VMR_DEPARTMENT_ID());
								detalle_pedido.setXX_VMA_Season_ID(p_order.getXX_Season_ID());
								detalle_pedido.setXX_VMR_Package_ID(p_order.getXX_VMR_Package_ID());
								detalle_pedido.setC_Campaign_ID(p_order.getC_Campaign_ID());
										
								detalle_pedido.setXX_VMR_Line_ID(rs2.getInt(2));
								detalle_pedido.setXX_VMR_Section_ID(rs2.getInt(3));				
								detalle_pedido.setXX_SalePrice(rs2.getBigDecimal(4));
								detalle_pedido.setXX_TaxAmount(rs2.getBigDecimal(5));
								detalle_pedido.setXX_SalePricePlusTax(rs2.getBigDecimal(6));
								detalle_pedido.setC_TaxCategory_ID(rs2.getInt(7));
								detalle_pedido.setPriceActual(rs2.getBigDecimal(8));
								detalle_pedido.setXX_UnitPurchasePrice(rs2.getBigDecimal(9));
							
								detalle_pedido.setM_Product_ID(rs3.getInt(1));
								detalle_pedido.setXX_ProductQuantity(rs3.getInt(2));
								detalle_pedido.setQtyReserved(rs3.getInt(2)); //Se almacena dos veces para tener la cantidad original
								detalle_pedido.setXX_ProductBatch_ID(rs3.getInt(3));
								detalle_pedido.setAD_Client_ID(p_order.getAD_Client_ID());
				
								detalle_pedido.save(trx);
							}
					}

				}catch (Exception e) {
					log.log(Level.SEVERE, sql3, e.getMessage());
					return false;
				}
				finally {
					DB.closeResultSet(rs3);
					DB.closeStatement(prst3);
				}
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql, e.getMessage());
			return false;
		}
		finally {
		
			DB.closeResultSet(rs2);
			DB.closeStatement(prst2);
		}
		
		return true;
			
	}

	/**Devuelve el nro de distribución asociada a la orden de compra si existe, en caso contrario devuelve 0
	 * */
	private static int getDistribution(int c_Order_ID) {
		
		int distID = 0;
		String sql = "\nselect XX_VMR_DISTRIBUTIONHEADER_ID FROM XX_VMR_DISTRIBUTIONHEADER WHERE C_ORDER_ID ="+c_Order_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
			{
				pstmt = DB.prepareStatement(sql,null); 
				 rs = pstmt.executeQuery();
				    
				 if(rs.next()){		
					 distID = rs.getInt("XX_VMR_DISTRIBUTIONHEADER_ID");
				   }
				    
		}catch (Exception e) {
			log.log(Level.SEVERE, sql, e.getMessage());
		}
		finally { 
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return distID;
	}

	/**Devuelve el nro de pedido asociado a una distribución si existe, en caso contrario devuelve 0
	 * */
	private static int getOrderRequest(int orderID) {
		
		int pedidoID = 0;
		String sql = "\nSELECT MAX(XX_VMR_ORDER_ID) FROM XX_VMR_ORDER WHERE C_ORDER_ID ="+orderID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
			{
				pstmt = DB.prepareStatement(sql,null); 
				 rs = pstmt.executeQuery();
				    
				 if(rs.next()){		
					 pedidoID = rs.getInt(1);
				   }
				    
		}catch (Exception e) {
			log.log(Level.SEVERE, sql, e.getMessage());
		}
		finally { 
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return pedidoID;
	}
	
	/**Devuelve el nro de detalle de pedido asociado a un pedido y producto si existe, en caso contrario devuelve 0
	 * */
	private static int getOrderRequestDetail(int prodID, int pedidoID) {
		
		int detalleID = 0;
		String sql = "\nselect XX_VMR_ORDERREQUESTDETAIL_ID FROM XX_VMR_ORDERREQUESTDETAIL WHERE XX_VMR_ORDER_ID ="+pedidoID+
				"\nAND M_PRODUCT_ID ="+prodID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
			{
				pstmt = DB.prepareStatement(sql,null); 
				 rs = pstmt.executeQuery();
				    
				 if(rs.next()){		
					 detalleID = rs.getInt("XX_VMR_ORDERREQUESTDETAIL_ID");
				   }
				    
		}catch (Exception e) {
			log.log(Level.SEVERE, sql, e.getMessage());
		}
		finally { 
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return detalleID;
	}
	
	private static void getPUPS(int C_OrderLine_ID){
		
		//se obtienen la unidad de compra y la unidad de venta para el CV
		String sql =
			"\nSelect uc.XX_UNITCONVERSION, us.XX_UNITCONVERSION "+
			"\nFrom XX_VMR_PO_LINEREFPROV lp, XX_VMR_UnitConversion uc, XX_VMR_UnitConversion us "+
			"\nWhere "+
			"\nlp.XX_PiecesBySale_ID = us.XX_VMR_UnitConversion_ID "+
			"\nand lp.XX_VMR_UnitConversion_ID = uc.XX_VMR_UnitConversion_ID "+
			"\nand lp.XX_VMR_PO_LINEREFPROV_ID=" +
			"\n(SELECT XX_VMR_PO_LINEREFPROV_ID FROM C_ORDERLINE WHERE C_ORDERLINE_ID="+C_OrderLine_ID+" " +
					"\nAND AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID()+")";
		
		PU=1;
		PS=1;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		try 
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			
			while(rs.next()) //compruebo que ya exista la matriz
			{	
				PU=rs.getInt(1);
				PS=rs.getInt(2);
			}

		}
		catch(Exception e)
		{	
			e.getMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}

	protected void prepare() {
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("Printer_Hanging")) {
				for(int i=0; i<services.length; i++){
					if(services[i].getName().equals(element.getParameter())){
						hanging = i;
						break;
					}
				}
			} else if (name.equals("Printer_Glued")) {
				for(int i=0; i<services.length; i++){
					if(services[i].getName().equals(element.getParameter())){
						glued = i;
						break;
					}
				}
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}
	}
	
	
}