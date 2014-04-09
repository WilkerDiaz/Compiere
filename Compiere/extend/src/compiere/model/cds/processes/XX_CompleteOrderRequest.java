package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_C_Order;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MMovement;
import compiere.model.cds.MMovementLine;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderRequestType;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VLO_OrderDetailPackage;
import compiere.model.cds.X_XX_VLO_PlacedOrderAs;
import compiere.model.cds.X_XX_VMR_DistribDetailTemp;
import compiere.model.cds.X_XX_VMR_Order;
import compiere.model.cds.X_XX_VMR_OrderRequestDetail;


public class XX_CompleteOrderRequest extends SvrProcess{

	static String printer = "";
	//static int packageQty = 1;
	int packageQtyTO = 1;
	int packageQtyFROM = 1;
	boolean all_new = false;
	static Integer m_readLock= 0; 
	
	//FormFrame form = new FormFrame(); //A form

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
		
		//int form_id = 0;
		X_XX_VMR_Order order = new X_XX_VMR_Order(getCtx(), getRecord_ID(), get_TrxName());		
		
		//Si algún detalle del pedido no tiene consecutivo de precio no deja continuar el proceso.
		if(!isPriceConsecutiveOk(order.get_ID())){
			ADialog.error(1, new Container(), "XX_PrintBeforeCompletePlacedOrder");
			return "";
		}
		
		//Si la orden ya fue aprobada, este proceso no puede hacer nada		
		if (!order.getXX_OrderRequestStatus().equals(X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.getValue())) {
			ADialog.info(1, new Container(), "XX_OrderRequestApproved");
			setProcessActive(false);
			return Msg.translate(getCtx(), "XX_OrderRequestApproved");
		}
		
		if (order.getXX_OrderRequestType().equals("D")) 
		{
			order.setXX_OrderRequestStatus("ET");	
			order.save();
			
			//Realizado por Rosmaira Arvelo
			MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(order.getC_Order_ID(),"EP"),null);
				
			//Llama al proceso cerrar alerta de Pedidos pendientes por etiquetar de DD
			if((order.getXX_OrderRequestStatus().equals("ET"))&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("EP")))
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
				
			} //Fin RArvelo
			
			setProcessActive(false);
			return "";
		}
		
 		if (packageQtyFROM == 0 || packageQtyTO == 0) {
 			ADialog.error(1, new Container(), "XX_FromToSmallerThanZero");
 			setProcessActive(false);
			return Msg.translate(Env.getCtx(), "XX_FromToSmallerThanZero");
		}
				
		//Que la cantidades tengan sentido
		if (packageQtyFROM > packageQtyTO) {
			ADialog.error(1, new Container(), "XX_FromLargerThanTo");
			setProcessActive(false);
			return Msg.translate(Env.getCtx(), "XX_FromLargerThanTo");
		}
				
		//Verificar, si la cantidad de paquetes existía previamente 
		if (order.getXX_PackageQuantity() == 0) {
			
			//No se había impreso las etiquetas de bulto previamente
			//Verificar que desde sea 1
			if (packageQtyFROM != 1) {
				ADialog.error(1, new Container(), "XX_NoLabelPrintedBefore");
				setProcessActive(false);
				return Msg.translate(Env.getCtx(), "XX_NoLabelPrintedBefore");
			}
							
			//No se habia impreso, entonces se imprimirá el rango completo
			//Se asigna el valor mas grande como el numero de etiquetas
			all_new = true;
			order.setXX_PackageQuantity(packageQtyTO);		
			order.save();		
		} else {
			//La cantidad existía
			//Verificar que la cantidad a imprimir no intente modificar la cantidad previa
			if (packageQtyTO > order.getXX_PackageQuantity()) {
				boolean ok = ADialog.ask(1, new Container(), "XX_PackageUpgrade");
				if (ok) {
					packageQtyFROM = 1;
					order.setXX_PackageQuantity(packageQtyTO);		
					order.save();
					
					// Se elimina cualquier numeraje de paquetes previo Pno hace falta
					String sql_eliminar = "DELETE FROM XX_VLO_ORDERDETAILPACKAGE WHERE XX_VMR_ORDER_ID = "
							+ order.get_ID();
					int eliminados = DB.executeUpdate(get_TrxName(), sql_eliminar );
					if (eliminados == -1) {
						ADialog.error(1, new Container(), "XX_DatabaseAccessError");
					}
					all_new = true;					
				} else{
					setProcessActive(false);
					return "";		
				}		
			}		
		}
		order.save();
		cmd_Generate(order.get_ID());
		setProcessActive(false);
		return "";	
			
	}

	@Override
	protected void prepare() {

		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("Package_Quantity")) {
				packageQtyFROM = Integer.parseInt(element.getParameter().toString());					
				packageQtyTO = Integer.parseInt(element.getParameter_To().toString());  
			} else if (name.equals("Printer_Name")) {
				printer = element.getParameter().toString();				
			} else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}

	}
	
	//Realizado por Rosmaira Arvelo
    /* Obtengo el ID de la tarea critica segun la orden
	 */
	private Integer getCriticalTaskForClose(Integer order, String typeTask){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTaskForClose_ID FROM XX_VMR_CriticalTaskForClose "
			       + "WHERE XX_ActualRecord_ID="+order
			       + " AND XX_TypeTask='"+typeTask+"'";
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

	
	
	//Se trajo el metodo del completar de la forma
	private void cmd_Generate(int orderID) {
		
		X_XX_VMR_Order placedOrder = new X_XX_VMR_Order(getCtx(), getRecord_ID(), get_TrxName());
		PrintService service = null;
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null,
				null);
		for (int i = 0; i < services.length; i++) {
			if (services[i].getName().equals(printer)) {
				service = services[i];
			}
		}
		if (service == null) {
			ADialog.error(0, new Container(), "XX_PrintingError");
			rollback();
			setProcessActive(false);
		}

		DocPrintJob job = service.createPrintJob();
		String s = "";
		DecimalFormat formato = new DecimalFormat("0000000000");
		int totalPieces = 0;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for (int i = packageQtyFROM; i <= packageQtyTO; i++) {

			// Se crea un detalle de bulto para cada linea
			if (all_new) {
				X_XX_VLO_OrderDetailPackage packageDetail = 
					new X_XX_VLO_OrderDetailPackage( Env.getCtx(), 0, get_TrxName());
				packageDetail.setXX_VMR_Order_ID(getRecord_ID());
				packageDetail.save();
			}
			MWarehouse warehouse = new MWarehouse(Env.getCtx(), placedOrder
					.getM_Warehouse_ID(), get_TrxName());
			String barcode = placedOrder.getXX_OrderBecoCorrelative()
					+ formato.format(i);
			try {
				
				String sql = " SELECT SUM( XX_PRODUCTQUANTITY) FROM XX_VMR_ORDERREQUESTDETAIL " +
						" WHERE XX_VMR_ORDER_ID =  " + placedOrder.get_ID();				
				PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					totalPieces = rs.getInt(1);
				}
				rs.close();
				ps.close();
				
			} catch (SQLException e) {

			}			
			s += "^XA^PRD^XZ\n" + "^XA^JMA^\n" + "^LH07,38^FS\n"
					+ "^FO80,10^B2N,1,N^BY3, 0.5,120^FD"
					+ barcode
					+ "^FS\n"
					+ "^FO210,135^AB,35,15^FD"
					+ barcode
					+ " ^FS\n"
					+ "^FO06,210^AB,90,30^FDPEDIDO : "
					+ placedOrder.getXX_OrderBecoCorrelative()
					+ "^FS\n"
					+ "^FO06,320^AB,90,30^FDTIENDA : "
					+ warehouse.getValue()
					+ "-"
					+ warehouse.getName()
					+ "^FS\n"
					+ "^FO06,430^AB,90,30^FDF.APRB : "
					+ sdf.format(cal.getTime())
					+ " ^FS\n"
					+ "^FO06,540^AB,90,30^FD#TOT PZ: " +				
					+ totalPieces
					+ " ^FS\n"
					+ "^FO06,650^AB,90,30^FD# BULTO: "
					+ i
					+ " / "
					+ placedOrder.getXX_PackageQuantity()
					+ "^FS\n"
					+ "^PQ1,0,1,^XZ\n";
		}
		byte[] by = s.getBytes();
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		Doc doc = new SimpleDoc(by, flavor, null);
		try {
			job.print(doc, null);
			String mss = Msg.getMsg(Env.getCtx(), "XX_IsThePrintLabelOK?", 
					new String[] {
						"" + packageQtyFROM,  "" + packageQtyTO});			
			
			commit();
			boolean ok = ADialog.ask(1, new Container(), mss );
			if (ok) {	
					completeOrderRequest(placedOrder);	
			} else {				
				/*String sql_eliminar = "DELETE FROM XX_VLO_ORDERDETAILPACKAGE WHERE XX_VMR_ORDER_ID = "
					+ orderID;
				int eliminados = DB.executeUpdate(sql_eliminar, get_TrxName());
				if (eliminados == -1) {
					ADialog.error(1, new Container(), "XX_DatabaseAccessError");
				}		*/		
				return;
			}
			
		} catch (Exception e) {
			log.saveError("XX_PrintingError", e);
			ADialog.error(1, new Container(), "XX_PrintingError");
			rollback();
			
			return;
		}

	}

	/**True si todos los detalles de un pedido tienen consecutivo de precio, False en caso contrario.
	 * @author ghuchet
	 * */
	private boolean isPriceConsecutiveOk(int placedOrder) {
		
		PreparedStatement ps =null; 
		ResultSet rs =null;
		int resultado = 0;
		try {
			String sql = "SELECT count(*) " +
					"\nFROM XX_VMR_ORDERREQUESTDETAIL " +
					"\nWHERE XX_VMR_ORDER_ID = " + placedOrder + " and XX_PRICECONSECUTIVE is null ";
			
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if(rs.next()){
				resultado =rs.getInt(1);
			}
				
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			 DB.closeResultSet(rs);
			 DB.closeStatement(ps);	 
		}
		if(resultado == 0 )
			return true;
		else return false;
	}

	public void completeOrderRequest (X_XX_VMR_Order placedOrder) {
		
		boolean act = false;
		
		try {
		
			//Verificar las distribuciones hermanas
			String sql_get_no = "SELECT A.XX_ASSOCIATIONNUMBER, A.XX_VLO_PLACEDORDERAS_ID " +
					" FROM XX_VLO_PLACEDORDERAS A WHERE A.XX_VMR_ORDER_ID = " + getRecord_ID();					
			PreparedStatement ps = DB.prepareStatement(sql_get_no, null);
			ResultSet rs = ps.executeQuery();
			int grupo_no = 0, po_association = 0;
			if (rs.next() ) {
				grupo_no  = rs.getInt(1);
				po_association  = rs.getInt(2);
			}
			rs.close();
			ps.close();
		
			if (grupo_no == 0) {
				//Verificar los cambios de estado
				placedOrder.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue());
				// agregado por vlomonaco
				// actualiza la fecha del estado cuando se pone en pendiente
				Calendar cal = Calendar.getInstance();
			    Timestamp t = new Timestamp(cal.getTime().getTime());									
			    placedOrder.setXX_DateStatusAtBay(t);
				// fin vlomonaco				
				placedOrder.setXX_OrderReady(true);
				placedOrder.save();
				
				//RArvelo
				try
				{
					closeAlert();
				}
				catch(Exception e)
				{
					log.log(Level.SEVERE,e.getMessage());
				}
				//Fin RArvelo
				
			} else {
				//Este pedido esta asociado a un grupo
				
				X_XX_VLO_PlacedOrderAs aso = 
					new X_XX_VLO_PlacedOrderAs(Env.getCtx(), po_association, get_TrxName());
				
				String sql_associated = " SELECT SUM( CASE " +
						" H.ISAPPROVED WHEN 'Y' THEN 1 " +
						" ELSE 0 END) APP, COUNT(*) TUP " +
						" FROM XX_VMR_HEADERASSOCIATION H WHERE H.XX_ASSOCIATIONNUMBER = " + grupo_no;
				ps = DB.prepareStatement(sql_associated, get_TrxName());
				rs = ps.executeQuery();
				
				boolean all_approved_headers = false;
				if (rs.next()) {
					if (rs.getInt(1) == rs.getInt(2)) {
						all_approved_headers = true;								
					}							
				} 
				rs.close();
				ps.close();	
										
				//Se verifica si todas estan aprobadas
				X_XX_VLO_PlacedOrderAs pl = 
					new X_XX_VLO_PlacedOrderAs(Env.getCtx(), po_association , null);
				pl.setIsComplete(true);
				pl.save(get_TrxName());
																
				if (all_approved_headers) {
					
					//Verificar si los pedidos hermanos estan listos
					String sql_hermanos = " SELECT SUM( CASE " +
						" P.ISCOMPLETE WHEN 'Y' THEN 1 " +
						" ELSE 0 END) APP, COUNT(*) TUP " +
						" FROM XX_VLO_PLACEDORDERAS P WHERE P.XX_ASSOCIATIONNUMBER = " + grupo_no;
					ps = DB.prepareStatement(sql_hermanos, get_TrxName());
					rs = ps.executeQuery();

					boolean all_ready = false;
					if (rs.next() ) {
						if (rs.getInt(1) == rs.getInt(2)) {
							all_ready = true;
						}
					}
					rs.close();
					ps.close();
						
					//Si todos estan listos entonces se pueden colocar como listos
					if (all_ready) {
						//Colocar todas las distribuciones como listas
						
						placedOrder.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue());								
						placedOrder.save();
						placedOrder.load(get_TrxName());		
						placedOrder.setXX_ApprovalDate(placedOrder.getUpdated());
						// agregado por vlomonaco
						// actualiza la fecha del estado cuando se pone en pendiente
						Calendar cal = Calendar.getInstance();
					    Timestamp t = new Timestamp(cal.getTime().getTime());									
					    placedOrder.setXX_DateStatusAtBay(t);
						// fin vlomonaco		
						placedOrder.save();
						
						String sql_cambiar = " UPDATE XX_VMR_ORDER SET XX_ORDERREADY = 'Y' " +
							 " WHERE XX_VMR_ORDER_ID IN ( " +
							 " SELECT I.XX_VMR_ORDER_ID FROM XX_VLO_PLACEDORDERAS I " +
							 " WHERE I.XX_ASSOCIATIONNUMBER = " +
							 grupo_no + ")";								
						DB.executeUpdate(get_TrxName(), sql_cambiar );								
						
						//RArvelo
						try
						{
							closeAlert();
						}
						catch(Exception e)
						{
							log.log(Level.SEVERE,e.getMessage());
						}
						//Fin RArvelo
						
					} else {
						placedOrder.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue());
						placedOrder.setXX_OrderReady(false);
						placedOrder.save();
						placedOrder.load(get_TrxName());		
						placedOrder.setXX_ApprovalDate(placedOrder.getUpdated());
						// agregado por vlomonaco
						// actualiza la fecha del estado cuando se pone en pendiente
						Calendar cal = Calendar.getInstance();
					    Timestamp t = new Timestamp(cal.getTime().getTime());									
					    placedOrder.setXX_DateStatusAtBay(t);
						// fin vlomonaco		
						placedOrder.save();
						
						//RArvelo
						try
						{
							closeAlert();
						}
						catch(Exception e)
						{
							log.log(Level.SEVERE,e.getMessage());
						}
						//Fin RArvelo
					}
				} else {							
					//Verificar los cambios de estado
					placedOrder.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue());
					placedOrder.setXX_OrderReady(false);
					placedOrder.save();
					placedOrder.load(get_TrxName());		
					placedOrder.setXX_ApprovalDate(placedOrder.getUpdated());
					// agregado por vlomonaco
					// actualiza la fecha del estado cuando se pone en pendiente
					Calendar cal = Calendar.getInstance();
				    Timestamp t = new Timestamp(cal.getTime().getTime());									
				    placedOrder.setXX_DateStatusAtBay(t);
					// fin vlomonaco		
					placedOrder.save();
					
					//RArvelo
					try
					{
						closeAlert();
					}
					catch(Exception e)
					{
						log.log(Level.SEVERE,e.getMessage());
					}
					//Fin RArvelo
				}	
			}
			
			/** Verificar si hay diferencias en la distribucion con respecto al pedido que sean por merma */
			String sqlDiferencias = "SELECT * FROM XX_VMR_ORDERREQUESTDETAIL " +
					" WHERE XX_VMR_ORDER_ID = " + placedOrder.get_ID() +
					" AND XX_VMR_CancellationMotive_ID != " +  Env.getCtx().getContextAsInt("#XX_L_MOTIVESTAYSCD_ID") +
					" AND XX_ProductQuantity < QtyReserved ";					
			PreparedStatement psDiferencias = DB.prepareStatement(sqlDiferencias, null);
			ResultSet rsDiferencias = psDiferencias.executeQuery();
			X_XX_VMR_OrderRequestDetail detalle = null;
			int diferencia = 0;
			if (rsDiferencias.next()) {
				
				//Crear un movimiento y completarlo. Las piezas van al locator merma
				MMovement movimiento = new Utilities().GenerarMovimiento(
						Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID"),
						Env.getCtx().getContextAsInt("#XX_L_LOCATORCDMERMA_ID"), get_TrxName());
				MMovementLine lineaMovimiento = null;
				detalle = new X_XX_VMR_OrderRequestDetail(getCtx(), rsDiferencias, null);
				diferencia = detalle.getQtyReserved() - detalle.getXX_ProductQuantity();
				
				//Crear la linea					
				lineaMovimiento = new Utilities().GenerarLineaMov(movimiento.getM_Locator_ID(),
						movimiento.getM_LocatorTo_ID(), movimiento.get_ID(), detalle, get_TrxName());
				
				//Este movimiento espera crearse con las piezas de diferencia no con las piezas del pedido 
				lineaMovimiento.setMovementQty(new BigDecimal(diferencia));
				lineaMovimiento.save();				
				while (rsDiferencias.next()) {				
					detalle = new X_XX_VMR_OrderRequestDetail(getCtx(), rsDiferencias, null);
					diferencia = detalle.getQtyReserved() - detalle.getXX_ProductQuantity();
					lineaMovimiento = new Utilities().GenerarLineaMov(movimiento.getM_Locator_ID(),
							movimiento.getM_LocatorTo_ID(), movimiento.get_ID(), detalle, get_TrxName());
					lineaMovimiento.setMovementQty(new BigDecimal(diferencia));
					lineaMovimiento.save();
				}				
				//Completar el movimiento
				movimiento.setDocAction(MMovement.DOCACTION_Complete);
				DocumentEngine.processIt(movimiento, MMovement.DOCACTION_Complete);
				movimiento.save();				
			} 			
			rsDiferencias.close();
			psDiferencias.close();
			
			/*AGREGADO GHUCHET Para generar los movimientos se obtiene el locator chequeado y en transito del CD asociado a la distribución
			 * del pedido -PROYECTO CD VALENCIA*/
			int warehouseCD =Utilities.obtenerDistribucionCD(placedOrder.getXX_VMR_DistributionHeader_ID());
			int locatorDesde = Utilities.obtenerLocatorChequeado(warehouseCD).get_ID();
			int locatorHacia = Utilities.obtenerLocatorEnTransito(warehouseCD).get_ID();
			
			//Se hacen los movimientos
			act = new Utilities().ActuaInvPedido(placedOrder, 0,locatorDesde, locatorHacia, get_Trx());
			/*FIN  GHUCHET -PROYECTO CD VALENCIA*/		
			
			// JPINO: Cambiar las cantidades de la tabla temporal a medida que se aprueben pedidos
			if (act && placedOrder.getXX_OrderRequestType().equals(X_Ref_XX_OrderRequestType.RE__DISTRIBUCIÓN.getValue())) {
				String correlative = placedOrder.getXX_OrderBecoCorrelative();
				correlative = correlative.substring(4);
				String sql_temp_details = 
					" SELECT XX_VMR_DISTRIBDETAILTEMP_ID AS ID, D.QTYRESERVED AS QTY" +
					" FROM XX_VMR_DISTRIBUTIONDETAIL D1, " +
					" XX_VMR_DISTRIBDETAILTEMP T, XX_VMR_ORDER O, XX_VMR_ORDERREQUESTDETAIL D " +
					" WHERE O.XX_VMR_ORDER_ID = " + getRecord_ID() +
					" AND O.XX_VMR_ORDER_ID = D.XX_VMR_ORDER_ID " +
					" AND T.XX_VMR_DISTRIBUTIONDETAIL_ID = D1.XX_VMR_DISTRIBUTIONDETAIL_ID " +
					" AND D1.XX_VMR_DISTRIBUTIONHEADER_ID = O.XX_VMR_DISTRIBUTIONHEADER_ID " +
					" AND T.M_PRODUCT_ID = D.M_PRODUCT_ID ";	
				PreparedStatement ps_details = DB.prepareStatement(sql_temp_details, null);
				ResultSet rs_details = ps_details.executeQuery();
				detalle = null;
				BigDecimal temp; 
				while (rs_details.next()) {
					
					//Para cada detalle de distribucion temporal, disminuir la cantidad o borrarlo si no hay cantidad
					X_XX_VMR_DistribDetailTemp detail_temp = 
						new X_XX_VMR_DistribDetailTemp(getCtx(), rs_details.getInt("ID"), get_TrxName());
					temp = detail_temp.getXX_DesiredQuantity();										
								//Del temporal se libera lo reservado de este pedido
					temp = temp.subtract(rs_details.getBigDecimal("QTY"));
					if (temp.compareTo(Env.ZERO) > 0) {								
						detail_temp.setXX_DesiredQuantity(temp);
						detail_temp.save();
					} else {								
						detail_temp.delete(true);
					}
				}
				rs_details.close();
				ps_details.close();
			}

		} catch (Exception e){
			e.printStackTrace();
			get_Trx().rollback();
			ADialog.error(1, new Container(), "XX_DatabaseAccessError");				
		}
		
		if(act){
			commit();
		
			//Verificacion de paquetes (Error de paquetes faltantes)
			int missingPQ = 0;
			int realPQ = 0;
			
				PreparedStatement ps_realPQ = null;
				ResultSet rs_realPQ = null;
			
			try
			{
				String sql_realPQ = "SELECT count(*) FROM XX_VLO_ORDERDETAILPACKAGE WHERE XX_VMR_Order_ID=" + getRecord_ID();
						
				ps_realPQ = DB.prepareStatement(sql_realPQ, null);
				rs_realPQ = ps_realPQ.executeQuery();
			
				if(rs_realPQ.next()) {
					realPQ = rs_realPQ.getInt(1);
				}
			}
			catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage());
				ADialog.error(1, new Container(), "XX_DatabaseAccessError");
			}finally
			{
				DB.closeResultSet(rs_realPQ);
				DB.closeStatement(ps_realPQ);
			}
			
			missingPQ = placedOrder.getXX_PackageQuantity() - realPQ;
			
			//Si hay paquetes faltantes los creamos
			for( int i=0; i<missingPQ; i++){
				
				X_XX_VLO_OrderDetailPackage packageDetail = 
					new X_XX_VLO_OrderDetailPackage( Env.getCtx(), 0, null);
				packageDetail.setXX_VMR_Order_ID(getRecord_ID());
				packageDetail.save();
			}
			
			ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(),
					"XX_CompleteOrderRequestSuccess2"));
		}
		else
			rollback();
		
		return;
	}
	
	public void closeAlert()
	{
		//Obtengo el ID de la distribucion
		X_XX_VMR_Order placedOrder = new X_XX_VMR_Order(getCtx(), getRecord_ID(), get_TrxName());
		String distriPD = placedOrder.getXX_OrderBecoCorrelative().substring(4);
		MVMRDistributionHeader distri = new MVMRDistributionHeader(Env.getCtx(),Integer.parseInt(distriPD),null);
		
		if(distri.get_ID()!=0)//verifica que la distribucion exista
		{
			if(placedOrder.getC_Order_ID()!=0)//si tiene O/C es una Pre-Distribucion
			{
				MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(distri.get_ID(),"EP1"),null);
				
				//Llama al proceso cerrar alerta de Pedidos pendientes por etiquetar de PD
				if((placedOrder.getXX_OrderRequestStatus().equals("EB"))&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("EP1")))
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
			}
			else //es una Re-Distribucion
			{
				MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(distri.get_ID(),"EP2"),null);
				
				//Llama al proceso cerrar alerta de Pedidos pendientes por etiquetar de CD
				if((placedOrder.getXX_OrderRequestStatus().equals("EB"))&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("EP2")))
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
			}
		}		
				
	}//fin closeAlert
	//fin RArvelo	
	
	
}




	