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
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDiscountRequest;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_DiscountAppliDetail;
import compiere.model.cds.X_XX_VMR_DiscountType;
import compiere.model.cds.X_XX_VMR_PriceConsecutive;
import compiere.model.cds.callouts.VME_PriceProductCallout;

public class XX_CreateDiscountPDA extends SvrProcess {

	static CLogger log = CLogger.getCLogger(XX_CreateDiscountPDA.class);
	private Integer lote = null;
	private Integer M_WarehouseD_ID = null;
	private Integer XX_VMR_DiscountType_ID = null;
	private String M_WarehouseD_Value = null;
	private Vector<MWarehouse> stores = new Vector<MWarehouse>();
	
	@Override
	/** Obtener los parametros */
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("M_WarehouseD_ID") ) {
					M_WarehouseD_ID = new BigDecimal(element.getParameterAsInt()).intValue();
				}else if (element.getParameterName().equals("XX_Lot") ) {
					lote = new BigDecimal(element.getParameterAsInt()).intValue();
				} else if (element.getParameterName().equals("XX_VMR_DiscountType_ID") ) {
					XX_VMR_DiscountType_ID = new BigDecimal(element.getParameterAsInt()).intValue();
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

			//El vector que almacena la estructura de rebajas
			Vector<Rebaja> rebajas = new Vector<Rebaja>();
			Iterator<Rebaja> iterator_rebajas = null;
			Rebaja rebaja_temporal = null, rebaja_nuevo = null;
			try {
				while(rsAS400.next()) {
					cantidad = cantidad+1;
					//Capturo el origen
					MWarehouse origen = getStore(rsAS400.getString("TNDPTC"));
					//if (origen == null)
					
					//Capturo el producto
					String p_idAux = rsAS400.getString("PRDPTC");
					
					p_idAux = p_idAux.trim();
					
					String p_id = p_idAux.substring(0, p_idAux.length()-3);
					MProduct producto = getProduct(new Integer(p_id).toString());					
					if (producto == null ) {
						return "Codigo de producto no encontrado: " + new Integer(p_id).toString();						
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
					
					//Agregar cada fila al vector de rebajas
					iterator_rebajas = rebajas.iterator();
					boolean encontrado = false;
					while (iterator_rebajas.hasNext()) {
						rebaja_temporal = iterator_rebajas.next();
						
						//Si una rebaja anterior va a la misma tienda, departamento, etc
						if (rebaja_temporal.origen == origen.get_ID()  ) {
							if (rebaja_temporal.departamento == producto.getXX_VMR_Department_ID()) {
								//Entonces agregar algo al objeto rebaja
								
								rebaja_temporal.agregarCantidades(consecutivo, producto.get_ID(), piezas);
								encontrado = true;
								break;
							}
						} 						
					}
					
					//Si no se encontró en el vector de rebajas agregarlo
					if (!encontrado) {
						rebaja_nuevo = 
							new Rebaja(origen.get_ID(), producto.getXX_VMR_Department_ID());
						rebaja_nuevo.agregarCantidades(consecutivo, producto.get_ID(), piezas);
						rebajas.add(rebaja_nuevo);
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

			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				DB.closeResultSet(rsAS400);
				DB.closeStatement(ps_s);
			}

			
			if(cantidad == 0){
				MWarehouse almaAux = new MWarehouse(getCtx(), M_WarehouseD_ID, null);
				return Msg.getMsg(Env.getCtx(), "XX_NotHaveLot", 
						new String[] { lote.toString() , M_WarehouseD_Value + " - " + almaAux.getName()});	
			}else{
				//Una vez leido todo el archivo se procede a crear las rebajas
				return procesarRebajas(rebajas).toString();	
			}
	}

	/** Retorna el X_XX_VMR_PriceConsecutive(Modelo de la clase) de acuerdo a un producto y su XX_PRICECONSECUTIVE*/
	private X_XX_VMR_PriceConsecutive getPriceConsecutive (Integer XX_PRICECONSECUTIVE, Integer M_Product_ID) {
		
		X_XX_VMR_PriceConsecutive retornado = null;		
		if (XX_PRICECONSECUTIVE == null) 
			return retornado;
		if (M_Product_ID == null) 
				return retornado;
		
		String sql_prod = "SELECT XX_VMR_PRICECONSECUTIVE_ID FROM XX_VMR_PRICECONSECUTIVE WHERE XX_PRICECONSECUTIVE = ? AND M_Product_ID = ? ";		
		PreparedStatement ps = DB.prepareStatement(sql_prod, null);
		ResultSet rs = null;
		try {
			ps.setInt(1, XX_PRICECONSECUTIVE);
			ps.setInt(2, M_Product_ID);
			rs = ps.executeQuery();
			if (rs.next()) {
				retornado = new X_XX_VMR_PriceConsecutive(Env.getCtx(), rs.getInt(1), null);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return retornado;
	}
	
	/** Retorna un producto de acuerdo a un value */
	private MProduct getProduct (String cellValue) {
		
		MProduct retornado = null;		
		if (cellValue == null) 
			return retornado;
		
		String sql_prod = "SELECT M_PRODUCT_ID FROM M_PRODUCT WHERE VALUE = ?";		
		PreparedStatement ps = DB.prepareStatement(sql_prod, null);
		ResultSet rs = null;
		try {
			ps.setString(1, cellValue);
			rs = ps.executeQuery();
			if (rs.next()) {
				retornado = new MProduct(Env.getCtx(), rs.getInt(1), null);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
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
		ResultSet rs = null;	
		try {
			rs = prst.executeQuery();		
			while (rs.next()){
				MWarehouse store = new MWarehouse(Env.getCtx(), rs.getInt(1), null);
				stores.add(store);
			}
		} catch (SQLException e){			
			log.log(Level.SEVERE, e.getMessage());
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}	
	}
	
	/**  Procesa las rebajas */
	public StringBuffer procesarRebajas (Vector<Rebaja> rebajas) {
		
		//Iterar sobre cada una de las rebajas
		Iterator<Rebaja> itr = rebajas.iterator();
		Rebaja reb = null;
		
		MVMRDiscountRequest movimiento = null;
		X_XX_VMR_DiscountAppliDetail linea = null;
		X_XX_VMR_Department departamentoAux = null;
		X_XX_VMR_DiscountType discountTypeAux = null;
		VME_PriceProductCallout priceBecoG = new VME_PriceProductCallout();
		StringBuffer buffer = new StringBuffer();
		
		String nl = " ----- ";
		
		while (itr.hasNext()) {
			reb = itr.next();
			movimiento = new MVMRDiscountRequest(Env.getCtx(), 0, get_TrxName());
			discountTypeAux = new X_XX_VMR_DiscountType(getCtx(), XX_VMR_DiscountType_ID, null);
			
			//Se deben llenar los campos necesarios
			String mss = Msg.getMsg(Env.getCtx(), "XX_CreatePdaDiscount", 
					new String[] { lote.toString(), M_WarehouseD_Value, discountTypeAux.getName()});	
						
			movimiento.setDescription(mss);
			movimiento.setXX_DateRequest(
					new Timestamp (Calendar.getInstance().getTimeInMillis()));
			
			//Coloco el campo almacen
			movimiento.setM_Warehouse_ID(reb.origen);
			
			//El resto de los campos
			departamentoAux = new X_XX_VMR_Department(getCtx(), reb.departamento, null);
			movimiento.setXX_VMR_Department_ID( reb.departamento);
					
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
				for (int i = 0; i < reb.productos.size() ; i++) {
					
					producto = new MProduct(Env.getCtx(), reb.productos.get(i), get_TrxName());
					linea = new X_XX_VMR_DiscountAppliDetail(Env.getCtx(), 0, get_TrxName()) ;
		    		linea.setXX_VMR_DiscountRequest_ID(movimiento.get_ID());
		    		
		    		//SET PRODUCTO Y CONSECUTIVO
		    		linea.setM_Product_ID(producto.get_ID());
		    		linea.setXX_VMR_PriceConsecutive_ID(getPriceConsecutive(reb.consecutivos.get(i), producto.get_ID()).get_ID());
		    		
		    		//SET CANTIDAD
		    		linea.setXX_LoweringQuantity(reb.cantidades.get(i).intValue());
		    		
		    		//SET EL TIPO DE PORCENTAJE A 50% O A CERO
		    		linea.setXX_VMR_DiscountType_ID(XX_VMR_DiscountType_ID);
		    		
		    		//SET LINEA Y SECTION
					if (producto.getXX_VMR_Line_ID() != 0)
						linea.setXX_VMR_Line_ID(producto.getXX_VMR_Line_ID());
					if (producto.getXX_VMR_Section_ID() != 0)
						linea.setXX_VMR_Section_ID(producto.getXX_VMR_Section_ID());

					//Calcular los lotes en funcion del consecutivo
					
					//Usando el priceconsecutive y el precio deberia calcularse el costo
					String sql = " SELECT M_ATTRIBUTESETINSTANCE_ID, XX_SALEPRICE " +
								 " FROM XX_VMR_PRICECONSECUTIVE " + 
								 " WHERE M_PRODUCT_ID = " + producto.get_ID() + 
								 " AND XX_PRICECONSECUTIVE = " + reb.consecutivos.get(i) ;
					
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					
					try {
						pstmt = DB.prepareStatement(sql, null);
						rs = pstmt.executeQuery();
						
						precioventa = null;
						lote = null;									
						if (rs.next()) {
							lote = rs.getInt(1);
							precioventa = rs.getBigDecimal(2);		
						} 
					}catch (Exception e) {					
						buffer.append(Msg.translate(Env.getCtx(), "XX_ProductPConsecNotFound"));						
						buffer.append(" : ");						
						buffer.append(producto.getValue() + "-" + producto.getName());
						buffer.append(" - ");	
						buffer.append(reb.consecutivos.get(i));
						buffer.append(nl);					
					} finally{
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}			
					
					if (lote != null ) {
						
						//SET PRECIO SIN IVA VIEJO (LO SACO DEL CONSECUTIVO)
						linea.setXX_PriceBeforeDiscount( precioventa );
						
						BigDecimal nuevoPrecio = precioventa;
						nuevoPrecio = nuevoPrecio.multiply(new BigDecimal(discountTypeAux.getPorcDescuento()));
						nuevoPrecio = nuevoPrecio.divide(new BigDecimal(100));
						nuevoPrecio = precioventa.subtract(nuevoPrecio);
						//linea.setXX_DiscountPrice(nuevoPrecio);
						
						//BUSCO IVA VIGENTE LO GUARDO EN tax
						BigDecimal taxPerc = null;
						if (producto.getC_TaxCategory_ID() != 0) {
							String sql_rate = " SELECT (RATE/100) FROM C_TAX " +
							" WHERE C_TaxCategory_ID= ? " + 
							" AND  ValidFrom <= sysdate " +
							" and rownum = 1 " +
							" order by ValidFrom desc ";			
							PreparedStatement prst_tax = null;
							ResultSet rs_tax = null;
							try {
								prst_tax = DB.prepareStatement(sql_rate,null);
								prst_tax.setInt(1, producto.getC_TaxCategory_ID() );
								rs_tax = prst_tax.executeQuery();
								if (rs_tax.next()){																
									taxPerc = rs_tax.getBigDecimal(1);
								}
							} catch (SQLException e){
								System.out.println("Error en el proceso XX_CreateDiscountPDA linea 446");
							} finally{
								DB.closeResultSet(rs_tax);
								DB.closeStatement(prst_tax);
							}	
						}
						
						BigDecimal montoTax = taxPerc.multiply(nuevoPrecio);
						taxPerc = taxPerc.setScale(2, RoundingMode.HALF_EVEN);
						montoTax = montoTax.setScale(2, RoundingMode.HALF_EVEN);
						BigDecimal precioConTax = montoTax.add(nuevoPrecio);
						
												
						// aca veo si el XX_SalePricePlusTax es precio beco y lo transformo a precio beco
						// si no es una rebaja a cero
						BigDecimal nuevoPrecioConIva = new BigDecimal(0);
						if(discountTypeAux.getPorcDescuento() == 100){
							nuevoPrecioConIva = precioConTax;
						}else{
							nuevoPrecioConIva = new BigDecimal(priceBecoG.priceBecoGlobal(getCtx(), 0, null, null, null, null, 5, precioConTax)); new BigDecimal(priceBecoG.priceBecoGlobal(getCtx(), 0, null, null, null, null, 5, precioConTax));
						}
						linea.setXX_SalePricePlusTax(nuevoPrecioConIva);
						linea.setXX_Tax(nuevoPrecioConIva.multiply(taxPerc));
						linea.setXX_DiscountPrice(nuevoPrecioConIva.subtract(nuevoPrecioConIva.multiply(taxPerc)));
						linea.setXX_AmountRebated(precioventa.subtract(nuevoPrecio)) ;
						
		    			//En este caso se almacena
		    			if ( linea.save() )
		    				created_lines ++;
		    			else {
		    				buffer.append(Msg.translate(Env.getCtx(), "SaveIgnored"));								
		    				buffer.append(nl);
		    			} 
			    		
					} else {
						buffer.append(Msg.translate(Env.getCtx(), "XX_ProductPConsecNotFound"));						
						buffer.append(" : ");						
						buffer.append(producto.getValue()+ "-" + producto.getName() );						
						buffer.append(": " + reb.consecutivos.get(i));
						buffer.append(nl);	
					}
		    	}
				
				//Si no se creo ninguna linea
				if (created_lines == 0) {
					movimiento.delete(true, get_TrxName());
					buffer.append(Msg.translate(Env.getCtx(), "XX_MovementIgnored") + " ");
					buffer.append(reb.toString());
					buffer.append(nl);
				}else{
					buffer.append(Msg.getMsg(Env.getCtx(), "XX_CreateMovementTranfer", 
							new String[] { movimiento.getValue(), departamentoAux.getValue()+" - "+departamentoAux.getName()}));
					buffer.append(nl);
				}
				
			} catch (Exception e) {
				log.log(Level.SEVERE, Msg.translate(Env.getCtx(), "XX_DatabaseAccessError"), e );
				buffer.append(Msg.translate(Env.getCtx(), "XX_DatabaseAccessError"));
				buffer.append(nl);
				
			}
		}
		return buffer;
	}
	
	private class Rebaja {
		int origen = 0;
		int departamento = 0;
		
		Vector<Integer> productos = null;
		Vector<Integer> consecutivos = null;		
		Vector<BigDecimal> cantidades = null;
		
		
		private Rebaja (int origen , int departamento) {
			this.origen = origen;
			this.departamento = departamento;
			
			
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
			resultado.append("Rebaja\n");
			MWarehouse warehouse = new MWarehouse(Env.getCtx(), origen, null);
			resultado.append( " -> " + warehouse.getValue()+ "\n") ;
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
