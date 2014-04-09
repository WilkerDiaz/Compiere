package compiere.model.cds.processes;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.model.MWarehouse;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MLocator;
import compiere.model.cds.MMovement;
import compiere.model.cds.MMovementLine;
import compiere.model.cds.MProduct;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Department;

public class XX_CreateReturnPDA extends SvrProcess {

	static CLogger log = CLogger.getCLogger(XX_CreateReturnPDA.class);
	private Integer lote = null;
	private Integer M_WarehouseA_ID = null;
	private Integer M_WarehouseD_ID = null;
	private Integer XX_ReturnMotive_ID = null;
	private String M_WarehouseA_Value = null;
	private String M_WarehouseD_Value = null;
	private Vector<MWarehouse> stores = new Vector<MWarehouse>();
	
	@Override
	/** Obtener los parametros */
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("XX_Lot") ) {
					lote = new BigDecimal(element.getParameterAsInt()).intValue();
				}else if (element.getParameterName().equals("M_WarehouseD_ID") ) {
					M_WarehouseD_ID = new BigDecimal(element.getParameterAsInt()).intValue();
				}else if (element.getParameterName().equals("XX_ReturnMotive_ID") ) {
					XX_ReturnMotive_ID = new BigDecimal(element.getParameterAsInt()).intValue();
				}else if (element.getParameterName().equals("M_WarehouseA_ID") ) {
					M_WarehouseA_ID = new BigDecimal(element.getParameterAsInt()).intValue();
				}
			}			
		}
		
	}
	
	@Override
	protected String doIt() throws Exception {
		String msg = "";
		
		getAllStores();
		msg = readFile();
		
		return msg;
	}
	
	public String readFile()  throws IOException  {
		M_WarehouseA_Value = getStoreValue(M_WarehouseA_ID);
		M_WarehouseD_Value = getStoreValue(M_WarehouseD_ID);
		
		/*
		 * Creo conexion con AS400
		 */
		ResultSet rsAS400 = null;
		As400DbManager as400 = new As400DbManager();
		as400.conectar();
		Statement ps_s = null;
		int cantidad = 0;
		try {
			ps_s = as400.conexion.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		/*
		 */
		String sql = "SELECT FECPTC, TNDPTC, CANPTC, PRDPTC, LOTPTC, MOVPTC " +
					 "FROM ICTFILE.SOLPTC " +
					 "WHERE INTEGER(LOTPTC) = "+new Integer(lote)+ " " +
					 "AND TNDPTC = '"+M_WarehouseD_Value+"' " +
					 "AND STSPTC <> '1' ";

		rsAS400 = as400.realizarConsulta(sql, ps_s);

			//El vector que almacena la estructura de devoluciones
			Vector<Devolucion> devoluciones = new Vector<Devolucion>();
			Iterator<Devolucion> iterator_devolucion = null;
			Devolucion devolucion_temporal = null, devolucion_nuevo = null;
			try {
				while(rsAS400.next()) {
					cantidad = cantidad + 1;
					
					//Capturo el origen
					MWarehouse origen = getStore(rsAS400.getString("TNDPTC"));
					
					//Capturo el destino
					MWarehouse destino = new MWarehouse(getCtx(), M_WarehouseA_ID, null);
					
					//Capturo el producto
					String p_idAux = rsAS400.getString("PRDPTC");
					
					String p_id = p_idAux.substring(0, p_idAux.length()-3);
					MProduct producto = getProduct(new Integer(p_id).toString());					
					if (producto == null ) {
						return Msg.getMsg(Env.getCtx(), "XX_ProductError");   
					}					
					
					//Capturo el consecutivo
					Integer consecutivo = null;
					try {
						consecutivo = Integer.parseInt(p_idAux.substring(p_idAux.length()-3, p_idAux.length()));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					if (consecutivo == null) {
												
					}

					//Capturo las piezas
					Double piezas = null;
					try {
						piezas = rsAS400.getDouble("CANPTC");
						
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					if (piezas == null) {
						
					}
					
					//Agregar cada fila al vector de devoluciones
					iterator_devolucion = devoluciones.iterator();
					boolean encontrado = false;
					while (iterator_devolucion.hasNext()) {
						devolucion_temporal = iterator_devolucion.next();
						
						//Si un devolucion anterior va a la misma tienda, departamento, etc
						if (devolucion_temporal.destino == destino.get_ID()) {
							if (devolucion_temporal.departamento == producto.getXX_VMR_Department_ID()) {
								if (devolucion_temporal.proveedor == producto.getC_BPartner_ID()) {
									//Entonces agregar algo al objeto devolucion
									
									devolucion_temporal.agregarCantidades(consecutivo, producto.get_ID(), piezas);
									encontrado = true;
									break;
								}
							}
						} 						
					}
					
					//Si no se encontró en el vector de devoluciones agregarlo
					if (!encontrado) {
						devolucion_nuevo = 
							new Devolucion(origen.get_ID(), destino.get_ID(), producto.getXX_VMR_Department_ID(),producto.getC_BPartner_ID());
						devolucion_nuevo.agregarCantidades(consecutivo, producto.get_ID(), piezas);
						devoluciones.add(devolucion_nuevo);
					}
				}
				
				
				/*
				 * Actualizo el campo STSPTC en '1' para no volver a seleccionar ese lote
				 */
				String sql_update= "UPDATE ICTFILE.SOLPTC " +
								   "SET STSPTC = '1' " +
								   "WHERE INTEGER(LOTPTC) = "+new Integer(lote)+ " " +
								   "AND TNDPTC = '"+M_WarehouseD_Value+"' ";

				/*	Ejecuto la sentencia
				* */
				Integer numRegUpdated = as400.realizarSentencia(sql_update, ps_s);
				System.out.println(numRegUpdated);
								
				rsAS400.close();
				ps_s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if(cantidad == 0){
				MWarehouse almaAux = new MWarehouse(getCtx(), M_WarehouseD_ID, null);
				return Msg.getMsg(Env.getCtx(), "XX_NotHaveLot", 
						new String[] { lote.toString() , M_WarehouseD_Value + " - " + almaAux.getName()});	
			}else{
				//Una vez leido todo el archivo se procede a crear los devoluciones
				return procesarDevolucion(devoluciones).toString();
			}
			


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

	/** Dado un m_warehouse_id retorna un codigo de tienda */
	private String getStoreValue(Integer cellValue){
				
		Iterator<MWarehouse> storesIt = stores.iterator();
		MWarehouse actual = null;
		String value = null;
		
		if (cellValue == null)
			return value; 
		
		while (storesIt.hasNext()) {
			actual = storesIt.next();
			if (cellValue.equals(actual.get_ID())) {
				value = actual.getValue();
			}
		}
		return value; 
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
	
	/**  Procesa los devoluciones del Vector de Devoluciones */
	public StringBuffer procesarDevolucion (Vector<Devolucion> devoluciones) {
		
		//Iterar sobre cada uno de los devoluciones
		Iterator<Devolucion> itr = devoluciones.iterator();
		Devolucion dev = null;
		
		MMovement movimiento = null;
		MMovementLine linea = null;
		MBPartner proveedorAux = null;
		X_XX_VMR_Department departamentoAux = null;
		StringBuffer buffer = new StringBuffer();
		
		String nl = " ----- ";
		
		while (itr.hasNext()) {
			dev = itr.next();
			proveedorAux = new MBPartner(getCtx(), dev.proveedor, null);
			departamentoAux = new X_XX_VMR_Department(getCtx(), dev.departamento, null);
			
			if (proveedorAux.getXX_VendorClass().equals("" + getCtx().getContextAsInt("XX_L_VENDCLASSIMP"))) {
				log.saveError("Error", Msg.translate(Env.getCtx(), "XX_InternationalReturn"));
				buffer.append(Msg.translate(Env.getCtx(), "XX_InternationalReturn"));
				buffer.append(nl);
				//dev = itr.next();
				//int created_lines = 0;	
			}else{		
				movimiento = new MMovement(Env.getCtx(), 0, get_TrxName());
							
				//Se deben llenar los campos necesarios
				movimiento.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_DOCTYPERETURN_ID")); //devolucion
				String mss = Msg.getMsg(Env.getCtx(), "XX_CreatePdaReturn", 
						new String[] { lote.toString() , M_WarehouseD_Value, M_WarehouseA_Value});	
							
				movimiento.setDescription(mss);
				movimiento.setMovementDate(
						new Timestamp (Calendar.getInstance().getTimeInMillis()));
				
				//Si el origen es centro de distribucion, entonces es el locator chequeado
				MLocator desde = Utilities.obtenerLocatorEnTienda( dev.origen );
				movimiento.setM_Locator_ID(desde.get_ID());
							
				//El locator hasta, que es el locator en transito del almacen destino
				MLocator hasta = Utilities.obtenerLocatorDevolucion( dev.destino );
				movimiento.setM_LocatorTo_ID(hasta.get_ID());
				
				//fijo el valor del proveedor
				movimiento.setC_BPartner_ID(dev.proveedor);
				
				//El resto de los campos
				movimiento.setXX_VMR_Department_ID(dev.departamento);
	
				movimiento.setM_WarehouseFrom_ID(dev.origen);
				movimiento.setM_WarehouseTo_ID( dev.destino);
						
				try {								
					if (!movimiento.save())
						continue;
					commit();
					movimiento.load(get_TrxName());
					
			    	//Se copian cada una de las lineas al movimiento
					MProduct producto = null;
					BigDecimal precioventa = null;
					Integer lote = null;
					int created_lines = 0;				
					for (int i = 0; i < dev.productos.size() ; i++) {
						
						producto = new MProduct(Env.getCtx(), dev.productos.get(i), get_TrxName());
						linea = new MMovementLine(Env.getCtx(), 0, get_TrxName()) ;
			    		linea.setM_LocatorTo_ID(movimiento.getM_LocatorTo_ID());				    		
			    		linea.setM_Locator_ID(movimiento.getM_Locator_ID());
			    		linea.setM_Movement_ID(movimiento.get_ID());
			    		linea.setM_Product_ID(producto.get_ID());
			    		linea.setXX_PriceConsecutive(dev.consecutivos.get(i));
			    		linea.setQtyRequired(dev.cantidades.get(i));
			    		linea.setMovementQty(dev.cantidades.get(i));
			    		linea.setXX_ReturnMotive_ID(XX_ReturnMotive_ID);
			    		
			    		if (producto.getXX_VMR_Brand_ID() != 0)
							linea.setXX_VMR_Brand_ID(producto.getXX_VMR_Brand_ID());
						if (producto.getXX_VMR_Line_ID() != 0)
							linea.setXX_VMR_Line_ID(producto.getXX_VMR_Line_ID());
						if (producto.getXX_VMR_Section_ID() != 0)
							linea.setXX_VMR_Section_ID(producto.getXX_VMR_Section_ID());
	
						//Calcular los lotes en funcion del consecutivo
						
						//Usando el priceconsecutive y el precio deberia calcularse el costo
						String sql = " SELECT M_ATTRIBUTESETINSTANCE_ID, XX_SALEPRICE " +
							" FROM XX_VMR_PRICECONSECUTIVE " + 
							" WHERE M_PRODUCT_ID = " + producto.get_ID() + 
							" AND XX_PRICECONSECUTIVE = " + dev.consecutivos.get(i) ;
						
						try {
							PreparedStatement pstmt = DB.prepareStatement(sql, null);
							ResultSet rs = pstmt.executeQuery();
							
							precioventa = null;
							lote = null;									
							if (rs.next()) {
								lote = rs.getInt(1);
								precioventa = rs.getBigDecimal(2);		
							} 
							rs.close();
							pstmt.close();
						}catch (Exception e) {					
							buffer.append(Msg.translate(Env.getCtx(), "XX_ProductPConsecNotFound"));						
							buffer.append(" : ");						
							buffer.append(producto.getValue() + "-" + producto.getName());
							buffer.append(" - ");	
							buffer.append(dev.consecutivos.get(i));
							buffer.append(nl);					
						}			
						
						linea.setTaxAmt(Env.ZERO);
						//Solo si el consecutivo se encontró
						if (lote != null ) {
							linea.setXX_SalePrice( precioventa );
							
							if (producto.getC_TaxCategory_ID() != 0) {		
								
								linea.setC_TaxCategory_ID(producto.getC_TaxCategory_ID());							
								String sql_rate = " SELECT (RATE/100) FROM C_TAX " +
								" WHERE C_TaxCategory_ID= ? " + 
								" AND  ValidFrom <= sysdate " +
								" and rownum = 1 " +
								" order by ValidFrom desc ";					
								try {
									PreparedStatement prst_tax = DB.prepareStatement(sql_rate,null);
									prst_tax.setInt(1, producto.getC_TaxCategory_ID() );
									ResultSet rs_tax = prst_tax.executeQuery();
									if (rs_tax.next()){
																	
										BigDecimal tax = rs_tax.getBigDecimal(1);
										tax = tax.multiply(precioventa);
										tax = tax.setScale(2, RoundingMode.HALF_EVEN);
										if (tax.compareTo(Env.ZERO) > 0) {
											linea.setTaxAmt(tax);
										}
									}
									rs_tax.close();
									prst_tax.close();
								} catch (SQLException e){
									
								}
							}			
	
							linea.setM_AttributeSetInstanceTo_ID( lote);
				    		linea.setM_AttributeSetInstance_ID( lote);
				    					    		
				    		//Calcular la cantidad disponible
				    		//Verificar que las cantidades requeridas sean menores que las disponibles
				    		
				    		XX_BatchNumberInfo lote_info = new XX_BatchNumberInfo();
				    		XX_BatchNumberInfo.Informacion info = lote_info.crearInfo();
				    		
				    		info.setConsecutivo( linea.getXX_PriceConsecutive() );
				    		info.setLocator(linea.getM_Locator_ID());
				    		info.setLote(linea.getM_AttributeSetInstance_ID());
				    		info.setProducto(linea.getM_Product_ID());
				    		info.setAlmacen(movimiento.getM_WarehouseFrom_ID());
	
				    		//Buscar la cantidad disponible
				    		String respuesta = info.cantidadProductoConsecutivo();
				    		if (info.isCorrecto()) {
				    			
				    			if (linea.getQtyRequired().compareTo(info.getCantidadDisponible()) == 1) {
				    				buffer.append(producto.getValue());
				    				buffer.append(" " + producto.getName() + " ");
				    				String msg = Msg.getMsg(Env.getCtx(), "XX_ReqLessThanAvail", 
											new String[] {
												"" + linea.getQtyRequired().setScale(0), 
												"" + info.getCantidadDisponible(),							
												"" + linea.getXX_PriceConsecutive()});	
				    				buffer.append(msg);
				    				buffer.append(nl);
				    				linea.setQtyRequired(info.getCantidadDisponible());
				    				linea.setMovementQty(info.getCantidadDisponible());
				    			} 
				    				
				    			//En este caso se almacena
				    			if ( linea.save() )
				    				created_lines ++;
				    			else {
				    				buffer.append(Msg.translate(Env.getCtx(), "SaveIgnored"));								
				    				buffer.append(nl);
				    			} 
				    		
				    		} else {
				    			buffer.append(respuesta);
			    				buffer.append(nl);
				    		}
						} else {
							buffer.append(Msg.translate(Env.getCtx(), "XX_ProductPConsecNotFound"));						
							buffer.append(" : ");						
							buffer.append(producto.getValue()+ "-" + producto.getName() );						
							buffer.append(": " + dev.consecutivos.get(i));
							buffer.append(nl);	
						}
			    	}
					
					//Si no se creo ninguna linea
					if (created_lines == 0) {
						movimiento.delete(true, get_TrxName());
						buffer.append(Msg.translate(Env.getCtx(), "XX_MovementIgnored") + " ");
						buffer.append(dev.toString());
						buffer.append(nl);
					}else{
						buffer.append(Msg.getMsg(Env.getCtx(), "XX_CreateMovementReturn", 
								new String[] { movimiento.getDocumentNo(), departamentoAux.getValue()+" - "+departamentoAux.getName(), proveedorAux.getValue()+" - "+proveedorAux.getName()}));
						buffer.append(nl);
					}
					
				} catch (Exception e) {
					log.log(Level.SEVERE, Msg.translate(Env.getCtx(), "XX_DatabaseAccessError"), e );
					buffer.append(Msg.translate(Env.getCtx(), "XX_DatabaseAccessError"));
					buffer.append(nl);
					
				}
			}	
		}
		return buffer;
	}
	
	private class Devolucion {
		int origen = 0;
		int destino = 0;
		int departamento = 0;
		int proveedor = 0;
		
		Vector<Integer> productos = null;
		Vector<Integer> consecutivos = null;		
		Vector<BigDecimal> cantidades = null;
		
		
		private Devolucion (int origen, int destino, int departamento, int proveedor) {
			this.origen = origen;
			this.destino = destino;
			this.departamento = departamento;
			this.proveedor = proveedor;
			
			
			consecutivos = new Vector<Integer>();
			cantidades = new Vector<BigDecimal>();
			productos = new Vector<Integer>();
		}
		
		private void agregarCantidades(Integer consecutivo, Integer producto, Double cantidad) {
			
			productos.add(producto);
			consecutivos.add(consecutivo);
			cantidades.add(new BigDecimal(cantidad));
		}

		@Override
		public String toString() {
			StringBuilder resultado = new StringBuilder();
			resultado.append("Devoluciones\n");
			MWarehouse warehouse = new MWarehouse(Env.getCtx(), origen, null);
			resultado.append( " " + warehouse.getValue()) ;
			warehouse = new MWarehouse(Env.getCtx(), destino, null);
			resultado.append( " -> " + warehouse.getValue() + "\n");  
			MBPartner proveedor = new MBPartner(Env.getCtx(), this.proveedor, null);
			resultado.append( "Proveedor#" + proveedor.getValue() +" ("+proveedor.getName()+")\n");  
			
			MProduct product = null;
			resultado.append("[");
			for (int i = 0; i < cantidades.size() ; i++) {				
				product = new MProduct(Env.getCtx(), this.productos.get(i), null);
				resultado.append(product.getValue() + "-" + product.getName());
				resultado.append(" : " + consecutivos.get(i) + " - " + cantidades.get(i) + "\n");			
			}
			resultado.append("]");
			return resultado.toString();
		}

	}
}
