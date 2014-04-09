package compiere.model.cds.processes;


import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import jxl.*;
import jxl.read.biff.BiffException;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.model.MRole;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_M_Movement;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MLocator;
import compiere.model.cds.MMovement;
import compiere.model.cds.MMovementLine;
import compiere.model.cds.MProduct;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Department;

public class XX_ImportPTransfer extends SvrProcess {

	static CLogger log = CLogger.getCLogger(XX_ImportPTransfer.class);
	private String archivo = null;
	private Vector<MWarehouse> stores = new Vector<MWarehouse>();
	
		@Override
	protected String doIt() throws Exception {
		
			String msg = "";
		if (archivo == null) {
			msg =  Msg.translate( getCtx(), "File Not Loaded");
		} else {
			
			if(!archivo.substring(archivo.length()-4, archivo.length()).equals(".xls")){
				if(archivo.substring(archivo.length()-5, archivo.length()).equals(".xlsx")){
					msg = Msg.translate( getCtx(), "Excel Earlier Format");;	
				}else{
					msg =Msg.translate( getCtx(), "Not Excel");
				}
			}else{	
				getAllStores();
				msg = readFile();
			}
		}
		return msg;
	}
		
	@Override
	/** Obtener los parametros */
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("Open") ) {
					archivo = element.getParameter().toString();
				}
			}			
		}
		
	}
	
	public String readFile()  throws IOException  {
		
		File inputWorkbook = new File(archivo);
		Workbook w;
		try {
			String msg = "";
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			
			//int defaultRows = sheet.getRows();
			
			//Si la cantidad de columnas es 4
			if(sheet.getColumns()>=4 && sheet.getRows() > 1){
		
				//Valido que las cabeceras tengan el formato correcto	
				if(!sheet.getCell(0, 0).getContents().equals("ORIGEN") 
						|| !sheet.getCell(1, 0).getContents().equals("DESTINO")
						|| !sheet.getCell(2, 0).getContents().equals("PRODUCTO") 
						|| !sheet.getCell(3, 0).getContents().equals("PIEZAS")						
				) {
					msg = Msg.translate( getCtx(), "Column Names");
					return msg;
				}	
			
				//El vector que almacena la estructura de traspasos
				Vector<Traspaso> traspasos = new Vector<Traspaso>();
				Iterator<Traspaso> iterator_traspasos = null;
				Traspaso traspaso_temporal = null, traspaso_nuevo = null;
				for (int i = 1; i < sheet.getRows(); i++) {
					
					//Capturo el origen
					MWarehouse origen = getStore(sheet.getCell(0, i).getContents());
					if (origen == null)
						return Msg.translate( getCtx(), "Cell A Error")+(i+1) + " " + sheet.getCell(0, i).getContents();
					
					//Capturo el destino
					MWarehouse destino = getStore(sheet.getCell(1, i).getContents());
					if (destino == null)
						return Msg.translate( getCtx(), "Cell B Error")+(i+1) + " " + sheet.getCell(1, i).getContents();
					
					//Capturo el producto
					String p_id = sheet.getCell(2, i).getContents();
					MProduct producto = getProduct(p_id);					
					if (producto == null ) {
						return Msg.translate( getCtx(), "Cell C Error")+(i+1) + " " + sheet.getCell(2, i).getContents();						
					}					

					//Capturo las piezas
					Double piezas = null;
					try {
						if (sheet.getCell(3, i) != null)
							piezas = Double.parseDouble(sheet.getCell(3, i).getContents());
					} catch (NumberFormatException e) {
						return Msg.translate( getCtx(), "Cell D Error")+(i+1) + " " + sheet.getCell(3, i).getContents();
					}
					if (piezas == null) {
						return Msg.translate( getCtx(), "Cell D Error")+(i+1) + " " + sheet.getCell(3, i).getContents();						
					}
					
					//Agregar cada fila al vector de traspasos
					iterator_traspasos = traspasos.iterator();
					boolean encontrado = false;
					while (iterator_traspasos.hasNext()) {
						traspaso_temporal = iterator_traspasos.next();
						
						//Si un traspaso anterior va a la misma tienda, departamento, etc
						if (traspaso_temporal.origen == origen.get_ID()  ) {
							if (traspaso_temporal.destino == destino.get_ID()) {
								if (traspaso_temporal.departamento == producto.getXX_VMR_Department_ID()) {
									//Entonces agregar algo al objeto traspaso
									
									traspaso_temporal.agregarCantidades( producto.get_ID(), piezas);
									encontrado = true;
									break;
								}
							}
						} 						
					}
					
					//Si no se encontró en el vector de traspasos agregarlo
					if (!encontrado) {
						traspaso_nuevo = 
							new Traspaso(origen.get_ID(), destino.get_ID(),	producto.getXX_VMR_Department_ID());
						traspaso_nuevo.agregarCantidades( producto.get_ID(), piezas);
						traspasos.add(traspaso_nuevo);
					}
				}
				
				//Una vez leido todo el archivo se procede a crear los traspasos
				//System.out.println(traspasos);
				return procesarTraspasos(traspasos).toString();				

			} else {
				return Msg.translate( getCtx(), "4 Columns");
			}			
		} catch (BiffException e){			
			log.log(Level.SEVERE, e.getMessage());
		}
		return "";
	}

	/** Retorna un producto de acuerdo a un value */
	private MProduct getProduct (String cellValue) {
		
		MProduct retornado = null;		
		if (cellValue == null) 
			return retornado;
		
		String sql_prod = "SELECT M_PRODUCT_ID FROM M_PRODUCT WHERE VALUE = ?";		
		PreparedStatement ps = DB.prepareStatement(sql_prod, null);
		try {
			ps.setString(1, cellValue);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				retornado = new MProduct(Env.getCtx(), rs.getInt(1), null);			
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retornado;
	}
	

	/** Dado un codigo de tienda retorna un m_warehouse_id */
	private MWarehouse getStore(String cellValue){
				
		Iterator<MWarehouse> storesIt = stores.iterator();
		MWarehouse returned = null, actual = null;
		
		if (cellValue == null)
			return returned; 
		
		while (storesIt.hasNext()) {
			actual = storesIt.next();
			if (actual.getValue().equals(cellValue)) {
				returned = actual;
			}
		}
		return returned; 
	}
		
	/** Obtiene todos los almacenes disponibles*/
	private void getAllStores(){
		
		String sql = "SELECT M_Warehouse_ID FROM M_WAREHOUSE";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		PreparedStatement prst = DB.prepareStatement(sql, null);
		try {
			ResultSet rs = prst.executeQuery();		
			while (rs.next()){
				MWarehouse store = new MWarehouse(Env.getCtx(), rs.getInt(1), null);
				stores.add(store);
			}
			rs.close();
			prst.close();
		} catch (SQLException e){			
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	/**  Procesa los traspasos de el archivo excel */
	public StringBuffer procesarTraspasos (Vector<Traspaso> traspasos) {
		
		//Iterar sobre cada uno de los traspasos
		Iterator<Traspaso> itr = traspasos.iterator();
		Traspaso trasp = null;
		
		MMovement movimiento = null;
		MMovementLine linea = null;
		StringBuffer buffer = new StringBuffer();
		
		String nl = " ----- ";
		
		//Usando el priceconsecutive y el precio deberia calcularse el costo
		String sql = " SELECT PR.XX_PRICECONSECUTIVE, PR.XX_SALEPRICE, PR.XX_UNITPURCHASEPRICE " +
				" FROM XX_VMR_PRICECONSECUTIVE PR " +
				" WHERE PR.M_PRODUCT_ID =  ? " +
				" AND PR.M_ATTRIBUTESETINSTANCE_ID IS NOT NULL " +
				" AND XX_PRICECONSECUTIVE IS NOT NULL " +
				" GROUP BY PR.XX_PRICECONSECUTIVE, PR.XX_SALEPRICE, PR.XX_UNITPURCHASEPRICE " +
				" ORDER BY PR.XX_PRICECONSECUTIVE ";	
		//System.out.println(sql);
		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		
		while (itr.hasNext()) {
			trasp = itr.next();
			movimiento = new MMovement(Env.getCtx(), 0, get_TrxName());
						
			//Se deben llenar los campos necesarios
			movimiento.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_DOCTYPETRANSFER_ID")); //Traspaso
			String mss = Msg.getMsg(Env.getCtx(), "XX_ImportedTransfer", 
					new String[] { archivo });	
						
			movimiento.setDescription(mss);
			movimiento.setMovementDate(
					new Timestamp (Calendar.getInstance().getTimeInMillis()));
			
			//Si el origen es centro de distribucion, entonces es el locator chequeado
			MLocator desde = Utilities.obtenerLocatorEnTienda( trasp.origen );
			movimiento.setM_Locator_ID(desde.get_ID());
						
			//El locator hasta, que es el locator en transito del almacen destino
			MLocator hasta = Utilities.obtenerLocatorEnTransito( trasp.destino );
			movimiento.setM_LocatorTo_ID(hasta.get_ID());
			
			//El resto de los campos
			movimiento.setXX_VMR_Department_ID( trasp.departamento);
			movimiento.setXX_TransferMotive_ID( 
					Env.getCtx().getContextAsInt("#XX_L_AUTOMATICTRANSFER_ID"));
						
			movimiento.setM_WarehouseFrom_ID(trasp.origen);
			movimiento.setM_WarehouseTo_ID( trasp.destino);
	
			try {								
				if (!movimiento.save())
					continue;
				commit();
				movimiento.load(get_TrxName());
				
		    	//Se copian cada una de las lineas al movimiento
				MProduct producto = null;
				BigDecimal precioVenta = null, costoCompra = null;
				Integer consecutivo = null;
				BigDecimal cantidadTraspaso = null, cantidadDisponible = null, cantidadDisponibleTotal = null; 
				
				int created_lines = 0;				
				for (int i = 0; i < trasp.productos.size() ; i++) {
		
					producto = new MProduct(Env.getCtx(), trasp.productos.get(i), get_TrxName());
					//System.out.println("Producto: "+producto.get_ID());
					//Calcular los consecutivos en funcion del inventario
					try {
						
						ps.setInt(1, producto.get_ID() );						
						cantidadTraspaso = trasp.cantidades.get(i);
						cantidadDisponibleTotal = Env.ZERO;
						
						rs = ps.executeQuery();
						precioVenta = null;
						costoCompra = null;
						while (rs.next() && cantidadTraspaso.compareTo(Env.ZERO) != 0 ) {
							
							consecutivo = rs.getInt("XX_PRICECONSECUTIVE");
							costoCompra = rs.getBigDecimal("XX_UNITPURCHASEPRICE");
							precioVenta = rs.getBigDecimal("XX_SALEPRICE");
							
				    		//Calcular la cantidad disponible
				    		XX_BatchNumberInfo lote_info = new XX_BatchNumberInfo();
				    		XX_BatchNumberInfo.Informacion info = lote_info.crearInfo();

				    		//Pasar parametros
				    		info.setConsecutivo( consecutivo );
				    		info.setLocator(movimiento.getM_Locator_ID());
				    		info.setProducto(producto.get_ID());
				    		info.setAlmacen(movimiento.getM_WarehouseFrom_ID());
				    		
				    		
				    		//Buscar la cantidad disponible
				    		info.cantidadProductoConsecutivo();
				    		
				    		if (info.isCorrecto()) {
				    			cantidadDisponible = info.getCantidadDisponible();				    				
				    		} else {
				    			cantidadDisponible = Env.ZERO;
				    		}
				    		cantidadDisponibleTotal = cantidadDisponibleTotal.add(cantidadDisponible);

							//Crear la linea - si la cantidad disponible es positiva
							if (cantidadDisponible.compareTo(Env.ZERO) == 1) {
								
								linea = new MMovementLine(Env.getCtx(), 0, get_TrxName()) ;
					    		linea.setM_LocatorTo_ID(movimiento.getM_LocatorTo_ID());				    		
					    		linea.setM_Locator_ID(movimiento.getM_Locator_ID());
					    		linea.setM_Movement_ID(movimiento.get_ID());
					    		linea.setM_Product_ID(producto.get_ID());
					    		linea.setXX_PriceConsecutive(consecutivo);
					    		
					    		if (producto.getXX_VMR_Brand_ID() != 0)
									linea.setXX_VMR_Brand_ID(producto.getXX_VMR_Brand_ID());
								if (producto.getXX_VMR_Line_ID() != 0)
									linea.setXX_VMR_Line_ID(producto.getXX_VMR_Line_ID());
								if (producto.getXX_VMR_Section_ID() != 0)
									linea.setXX_VMR_Section_ID(producto.getXX_VMR_Section_ID());
										
								linea.setXX_SalePrice( precioVenta );
								linea.setPriceActual(costoCompra);
								if (producto.getC_TaxCategory_ID() != 0) {		
									linea.setC_TaxCategory_ID(producto.getC_TaxCategory_ID());
									linea.setTaxAmt(obtenerValorImpuesto(
											linea, producto.getC_TaxCategory_ID(), costoCompra));									
								}

								//Si es mayor que la cantidad solicitada
								if (cantidadDisponible.compareTo(cantidadTraspaso) != -1) {

						    		linea.setQtyRequired(cantidadTraspaso);
						    		linea.setMovementQty(cantidadTraspaso);
						    		cantidadTraspaso = Env.ZERO;
						    		 
								} else {
									
									linea.setQtyRequired(cantidadDisponible);
						    		linea.setMovementQty(cantidadDisponible);
						    		cantidadTraspaso = cantidadTraspaso.subtract(cantidadDisponible);
						    		
								}
								//En este caso se almacena
								if ( linea.save() )
				    				created_lines ++;
							}
						}
						//No fueron suficientes piezas
						if (cantidadTraspaso.compareTo(Env.ZERO) == 1) {
							
							if (trasp.cantidades.get(i).compareTo(cantidadDisponibleTotal) == 1) {
			    				
			    				buffer.append(producto.getValue());
			    				buffer.append(" " + producto.getName() + " ");
			    				String msg = Msg.getMsg(Env.getCtx(), "XX_ReqLessThanAvail", 
										new String[] {
											"" + trasp.cantidades.get(i), 
											"" + cantidadDisponibleTotal,
											Msg.translate(getCtx(), "All")
											});	
			    				buffer.append(msg);
			    				buffer.append(nl);
			    			}
						} 
						
					} catch (SQLException e) {					
						buffer.append(Msg.translate(Env.getCtx(), "XX_ProductPConsecNotFound"));						
						buffer.append(" : ");						
						buffer.append(producto.getValue() + "-" + producto.getName());
						buffer.append(" - ");	
						buffer.append(nl);					
					} finally {
						
						//Cerrar el resulset
						try {
							if (rs != null) 
								rs.close();
							rs = null;
								
						} catch (Exception e2) {}
					}
				}
				//System.out.println("MOVIMIENTO: "+movimiento.getM_Movement_ID());
				//Si no se creo ninguna linea
				if (created_lines == 0) {
					movimiento.delete(true, get_TrxName());
					buffer.append(Msg.translate(Env.getCtx(), "XX_MovementIgnored") + " ");
					buffer.append(trasp.toString());
					buffer.append(nl);
				//AGREGADO POR GHUCHET	
				}else {
					completarMovimiento(movimiento);
				}
				//HASTA AQUI AGREGADO POR GHUCHET	
			} catch (Exception e) {
				log.log(Level.SEVERE, Msg.translate(Env.getCtx(), "XX_DatabaseAccessError"), e );
				buffer.append(Msg.translate(Env.getCtx(), "XX_DatabaseAccessError"));
				buffer.append(nl);				
			}		
		}
		return buffer;
	}

	/** Dada una linea y un precio de venta, obtener su impuesto */
	private BigDecimal obtenerValorImpuesto (MMovementLine linea , Integer tax_id, BigDecimal precioVenta ) {
		
		BigDecimal tax = null;
		String sql_rate = " SELECT (RATE/100) FROM C_TAX " +
		" WHERE C_TaxCategory_ID= ? AND  ValidFrom <= sysdate  and rownum = 1 " +
		" order by ValidFrom desc ";					
		try {
			PreparedStatement prst_tax = DB.prepareStatement(sql_rate,null);
			prst_tax.setInt(1, tax_id );
			ResultSet rs_tax = prst_tax.executeQuery();
			if (rs_tax.next()){
											
				tax = rs_tax.getBigDecimal(1);
				tax = tax.multiply(precioVenta);
				tax = tax.setScale(2, RoundingMode.HALF_EVEN);
				
			}
			rs_tax.close();
			prst_tax.close();
		} catch (SQLException e){
			
		}
		return tax;
	}
	
	private class Traspaso {
		int origen = 0;
		int destino = 0;
		int departamento = 0;
		
		Vector<Integer> productos = null;
		Vector<BigDecimal> cantidades = null;
		
		
		private Traspaso (int origen, int destino, int departamento) {
			this.origen = origen;
			this.destino = destino;
			this.departamento = departamento;
			
			cantidades = new Vector<BigDecimal>();
			productos = new Vector<Integer>();
		}
		
		private void agregarCantidades(Integer producto, Double cantidad) {
			
			productos.add(producto);
			cantidades.add(new BigDecimal(cantidad));
		}

		@Override
		public String toString() {
			StringBuilder resultado = new StringBuilder();
			resultado.append("Traspaso\n");
			MWarehouse warehouse = new MWarehouse(Env.getCtx(), origen, null);
			resultado.append( " " + warehouse.getValue()) ;
			warehouse = new MWarehouse(Env.getCtx(), destino, null);
			resultado.append( " -> " + warehouse.getValue() + "\n");  
			MProduct product = null;
			resultado.append("[");
			for (int i = 0; i < cantidades.size() ; i++) {				
				product = new MProduct(Env.getCtx(), this.productos.get(i), null);
				resultado.append(product.getValue() + "-" + product.getName());
				resultado.append( " - " + cantidades.get(i) + "\n");			
			}
			resultado.append("]");
			return resultado.toString();
		}

	}
	
	//DESDE AQUI AGREGADO POR GHUCHET	
	private void completarMovimiento(MMovement move) {
		//Preparar el movimiento
		move.setDocAction(X_M_Movement.DOCACTION_Prepare);
		DocumentEngine.processIt(move, X_M_Movement.DOCACTION_Prepare);	
		move.save();
		commit();
		move.load(get_TrxName());
		if (move.getDocStatus().equals("IN")) {
			ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "NoLines");
		} else {
			
			//Aca debo actualizar las cantidades aprobadas
			String sql = "SELECT QtyRequired, XX_ApprovedQty FROM M_MOVEMENTLINE WHERE M_MOVEMENT_ID = " + move.getM_Movement_ID();
			PreparedStatement ps_1 = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, null);
			ResultSet rs_1 = null;
			//System.out.println(sql);
			int qty_required = 0; 
			try {
				rs_1 = ps_1.executeQuery();
				while (rs_1.next()) {							
					qty_required = rs_1.getInt("QtyRequired");
					rs_1.updateInt("XX_ApprovedQty", qty_required);
					rs_1.updateRow();
				}
			} catch (Exception e) {
				e.printStackTrace();
			
			} finally {
				try {
					rs_1.close();
					ps_1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}

			move.setXX_Status("PE");
			move.setXX_RequestDate(move.getUpdated());	
			move.save();		
			
			//Se envian correos dependiendo del tipo de documento
			enviarCorreoATienda(move,  Env.getCtx().getContextAsInt("#XX_L_MT_PTRANSFERAPPROVAL_ID"));
		}
		
	}
	
	/** Envia correos al personal de tienda en caso de la aprobación de un traspaso */
	private void enviarCorreoATienda( MMovement movimiento , int plantillaDeCorreo){
				
		MWarehouse almaSalida = new MWarehouse(getCtx(), movimiento.getM_WarehouseFrom_ID(), null);
		MWarehouse almaLleada = new MWarehouse(getCtx(), movimiento.getM_WarehouseTo_ID(), null);
		X_XX_VMR_Department departamento = new X_XX_VMR_Department(getCtx(), movimiento.getXX_VMR_Department_ID(), null);
		
		//Mensaje debe indicar departamento, origen, destino y traspaso		
		String mensaje = Msg.getMsg( getCtx(), "XX_PTransferApproval", 
				 new String[]{movimiento.getDocumentNo(),
							  departamento.getValue() + "-" + departamento.getName(),								
							  almaSalida.getValue()+"-"+almaSalida.getName(),
							  almaLleada.getValue()+"-"+almaLleada.getName(),
							  movimiento.getXX_StatusName()
		});
		
		//Al Gerente de Tienda
		//Selecciono el o los gerentes de Tienda
		String SQL = "SELECT AD_USER_ID FROM AD_USER WHERE ISACTIVE='Y' " +
					" AND C_BPARTNER_ID IN "+
					"("+
						"SELECT C_BPARTNER_ID " +
						"FROM C_BPARTNER WHERE isActive='Y' "+
						"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_STOREMAN_ID")+" " +
						"AND (M_WAREHOUSE_ID = "+ almaLleada.get_ID()+" " +
						"OR M_WAREHOUSE_ID = "+ almaSalida.get_ID() +") " +
						"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
					") "+
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";		
		
    	Vector<Integer> storeManagers = new Vector<Integer>();
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){	
				storeManagers.add(rs.getInt("AD_USER_ID"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a){
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a los gerentes
		Utilities f = null;
		for(int i=0; i<storeManagers.size();i++){
	
			f = new Utilities(Env.getCtx(), null, plantillaDeCorreo, mensaje, -1,
					Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, storeManagers.get(i),null);
			try {
				f.ejecutarMail(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			f = null;
		}
		
		//*********************************
		//Selecciono los asesores de almacen
    	SQL = "SELECT AD_USER_ID FROM AD_USER WHERE  ISACTIVE='Y' " +
    			"AND C_BPARTNER_ID IN "+
				"("+
					"SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE isActive='Y' "+
					"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_DEPASE_ID")+" " +
					"AND (M_WAREHOUSE_ID = "+ almaLleada.get_ID()+" " +
					"OR M_WAREHOUSE_ID = "+ almaSalida.get_ID() +") " +
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
				") "+
				"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";			
		
    	Vector<Integer> warehouseAsessors = new Vector<Integer>();
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				warehouseAsessors.add(rs.getInt("AD_USER_ID"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a los asesores
		Utilities m = null;
		for(int i=0; i<warehouseAsessors.size();i++){
			
			m = new Utilities(Env.getCtx(), null, plantillaDeCorreo, mensaje, -1,
					Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, warehouseAsessors.get(i),null);
			try {
				m.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			m = null;
		}	
		
		
		//*********************************
		//Selecciono los Gerentes de Area ADMIN y MERCA, Asesor de Inventario
    	SQL = "SELECT AD_USER_ID FROM AD_USER WHERE  ISACTIVE='Y' " +
    			"AND C_BPARTNER_ID IN "+
				"("+
					"SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE isActive='Y' "+
					"AND C_JOB_ID IN (" +
					" " + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_ADMINMANAG_ID")+"," +
					" " + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_GAMERC_ID")+"," +
					" " + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_INVASSES_ID")+
					") " +
					"AND (M_WAREHOUSE_ID = "+ almaLleada.get_ID()+" " +
					"OR M_WAREHOUSE_ID = "+ almaSalida.get_ID() +") " +
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
				") "+
				"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";			
		
    	Vector<Integer> managerArea = new Vector<Integer>();
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				managerArea.add(rs.getInt("AD_USER_ID"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a gerentes de area
		Utilities u = null;
		for(int i=0; i<managerArea.size();i++){
			
			u = new Utilities(Env.getCtx(), null, plantillaDeCorreo, mensaje, -1,
					Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, managerArea.get(i),null);
			try {
				u.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			u = null;
		}	
	}
	//HASTA AQUI AGREGADO POR GHUCHET
}
