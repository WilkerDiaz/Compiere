package compiere.model.cds.distribution;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_XX_VMR_DistribProductDetail;
import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.X_XX_VMR_PO_ProductDistrib;
import compiere.model.cds.X_XX_VMR_VendorProdRef;

/** Clase abstracta padre que almacena los metodos genericos de distribucion
 *  y será luego extendida por cada clase hija 
 *  
 *  @author Javier Pino
 *  @author Gabrielle Huchet
 */
public abstract class XX_Distribution {
	
	protected boolean esOrdenDeCompra; //Indica si la distribucion es por orden de compra
	protected Trx nombreTrx;
	protected X_XX_VMR_DistributionHeader cabecera;
	protected MOrder orden;
	protected Ctx ctx ;
	protected boolean ignorarMultiplo = false, usarVentasAñoPasado = false; 
	protected boolean productosConMultiplosYCurvas = false, productosConCurvaNoRespetada = false;  
	protected boolean inicializado = false, procesado = false, finalizado = false;
	protected int mes, año ;
	protected PreparedStatement psProductos = null;
	protected ResultSet rsProductos = null;
	
	//Aquí se almacena la curva de tallas calculada
	private Hashtable<Integer, Hashtable<Integer, Integer> > 
		curvaDeTallas = new Hashtable<Integer, Hashtable<Integer,Integer>>();
	
	//Aqui se almacenan los detalles de productos distribuidos  
	private Hashtable<Integer, Integer> productosViejos = new Hashtable<Integer, Integer>();
	
	//Usados para realizar los calculos
	protected Vector<Integer> tiendaID = null;
	protected Vector<Float> tiendaPorcentaje = null;
	
	//Indica si la distribución es por ajuste automático
	private int autoAdjustment = 0;
	//Indica si la distribucion es por ajuste automático y por piezas
	protected boolean isPiecesAuto = false;

	/** Constructor por defecto */
	protected XX_Distribution(int cabeceraID, Ctx contexto, Trx trxNombre, int mes, int año) {
		
		ctx = contexto;
		nombreTrx = trxNombre;
		cabecera = new X_XX_VMR_DistributionHeader(contexto, cabeceraID, null);
		if (cabecera.getC_Order_ID() > 0) {
			orden = new MOrder(Env.getCtx(), cabecera.getC_Order_ID(), null);
		} else { 
			orden = null;
		}
		this.mes = mes;
		this.año = año;
		isPiecesAuto = false;
	}
	
	//Todos deben inicialziarse
	protected abstract void inicializar() ;
	
	//Todos deben cerrarse en caso
	protected abstract void finalizar(); 
		
	/**
	 * Con este metodo se calcula el porcentaje de cada producto de acuerdo a la distribucion usada 
	 */
	protected abstract XX_StoreAmounts obtenerPorcentaje(ResultSet rsProductos) throws SQLException;
	
	/** Metodo general de procesamiento*/
	public final void procesar () {

		//AGREGADO GHUCHET 
		autoAdjustment = Env.getCtx().getContextAsInt("#ADJUSTMENT_AUTOMATIC");
		(Env.getCtx()).remove("#ADJUSTMENT_AUTOMATIC");
		//FIN AGREGADO GHUCHET
		
		//Inicializar
		if (finalizado)
			return;
		
		inicializar();
		if (!inicializado) {
			finalizado = false;
			return;
		}
		//Comenzó el procesamiento correctamente
		procesado = true;
		//Remover tipo de distribución aplicado anteriormente a los detalles
		removeTypeDetail();
		ignorarMultiplo = cabecera.isXX_IgnorePackageMultiple();		
		XX_DistributionUtilities.prepararCurvaDeTallas(cabecera.get_ID(), curvaDeTallas);
		
		/*Cuando es ajuste autómatico por piezas solo se eliminan los detalles de tienda que presentan alguna inconsistencia*/
		if(!isPiecesAuto){ 
			//Antes de distribuir nuevamente eliminar las distribuciones pasadas
			XX_DistributionUtilities.eliminarDetallesDistribucion(cabecera.get_ID(), cabecera.getC_Order_ID(), nombreTrx);
		}
		//Tambien se obtiene un hash con los productos de la dist si ya se ha distribuido
		XX_DistributionUtilities.buscarDetallesDeProductos(cabecera.get_ID(), orden, productosViejos);

		 /*Cuando es  ajuste autómatico por piezas solo se distribuyen los detalles que presentan alguna inconsistencia*/
		if (isPiecesAuto){
			distribuirInconsistentes();
		}else {
			try {
				//Buscar los productos 
				String sql = productosADistribuir();
				psProductos = DB.prepareStatement(sql, null);
				rsProductos = psProductos.executeQuery(); 
				while (rsProductos.next() && procesado ) {									
					distribuirProducto(rsProductos);
				}
			} catch (Exception e) {
					e.printStackTrace();
					procesado = false;		
			} finally {
				try {
					if (rsProductos != null)
						rsProductos.close();
					if (psProductos != null)
						psProductos.close();
				} catch (SQLException e) {}
			}		
		}
		//Cerrar el objeto
		if (procesado) {					
			
			//En el caso de predistribuida
			if (orden != null) { 	
				//Indicar que se calcularon los porcentajes
				cabecera.setXX_CalculatedPOSPercentages(true);	
				
				//Modificar OC
				orden.setXX_StoreDistribution("Y");			
				orden.save(nombreTrx);	
				{
					String update_withchar = " UPDATE XX_VMR_PO_LINEREFPROV " +
							" SET XX_GENERATEMATRIX = 'N' , " +
							" XX_DELETEMATRIX = 'N' WHERE C_ORDER_ID = "
							+ orden.get_ID() + " AND XX_WITHCHARACTERISTIC = 'Y'";	
					
				    try {
				    	DB.executeUpdate(nombreTrx, update_withchar);
				    } catch(Exception e)
				    {
						e.printStackTrace();
						ADialog.error(1, new Container(), "XX_DatabaseAccessError");
						finalizado = false;
				    }
					
					//Si la distribucion estaba alterada por la distribucion, esto debio corregir la situacion
					if (cabecera.getXX_DistributionStatus().equals(
							X_Ref_XX_DistributionStatus.PENDIENTE_POR_REDISTRIBUCION_Y_APROBACION.getValue())) {				
						cabecera.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.LISTA_PARA_APROBAR.getValue());
						cabecera.save(nombreTrx);
					}				
				}								
			}
			
			//Debe indicarse que se distribuyo 	
			cabecera.setXX_AllCalculated(true);
			cabecera.save(nombreTrx);			
			finalizar();
		}
		if (finalizado) {
			//Todo estuvo correcto así que hacer commit
			nombreTrx.commit();
			if(autoAdjustment == 0){ //AGREGADO GHUCHET SOLO EL IF
				if (productosConMultiplosYCurvas) {
					ADialog.error(1, new Container(), "XX_BothSizeAndPackageMultiple");				
				} else if (productosConCurvaNoRespetada) { 
					ADialog.info(1, new Container(), "XX_UnMatchedProductSizeCurve");
				}
			}else if(autoAdjustment == 1) Env.getCtx().setContext("#DISTRIBUTION_OK",1);	
		} else {
			if(autoAdjustment == 1) Env.getCtx().setContext("#DISTRIBUTION_OK",0);	
			nombreTrx.rollback();
		}		
	}
	

	private void removeTypeDetail() {
		String sql = null;
		X_XX_VMR_DistributionDetail detail;
		sql= "SELECT XX_VMR_DISTRIBUTIONDETAIL_ID  FROM XX_VMR_DISTRIBUTIONDETAIL  " +
				"WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " +cabecera.get_ID();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//Buscar si todos los detalles de la cabecera han sido distribuidos
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery(); 
			while (rs.next()) {	
				detail = new X_XX_VMR_DistributionDetail(ctx, rs.getInt(1), nombreTrx);
				detail.setXX_VMR_DistributionType_ID(0);
				detail.save(nombreTrx);
			}
		} catch (Exception e) {
				e.printStackTrace();
				procesado = false;		
		} finally {
			try {
				if (rs!= null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {}
		}		
	}

	/**
	 * Este metodo devuelve un String con los productos y e informacion que necesita para distribuir 
	 */
	public String productosADistribuir () {
		
		//Si es predistribuida el query es diferente		
		String sql = null;
		if (orden != null) {
			
			boolean tieneChequeo =false; 
			if(autoAdjustment == 0 )  //AGREGADO POR GHUCHET EL IF
				tieneChequeo = XX_DistributionUtilities.verificaPOTieneRecepcion(orden.get_ID());
			 //AGREGADO POR GHUCHET
			else if(autoAdjustment == 1)
				tieneChequeo = XX_DistributionUtilities.verificaPOTieneRecepcionAuto(orden.get_ID());
			//FIN AGREGADO POR GHUCHET
			if(tieneChequeo){
			   sql = "SELECT PO_REF.XX_VMR_PO_LINEREFPROV_ID, PO_REF.XX_PACKAGEMULTIPLE, " +
					   "PO_REF.XX_VMR_VENDORPRODREF_ID, M_IOL.M_PRODUCT_ID M_PRODUCT," +
					   "REF_MATRIX.XX_VALUE1, REF_MATRIX.XX_VALUE2, MOVEMENTQTY XX_QTY, " +
					   "PO_REF.XX_SALEPRICE, PO_REF.XX_TaxAmount, PO_REF.XX_SALEPRICEPLUSTAX, " +
					   "PO_REF.C_TaxCategory_ID, PO_REF.XX_Margin, PO_REF.PRICEACTUAL, " +
					   "PO_REF.XX_ISDEFINITIVE, PO_REF.XX_UnitPurchasePrice, " +
					   "PROD.XX_VMR_DEPARTMENT_ID, PROD.XX_VMR_LINE_ID, PROD.XX_VMR_SECTION_ID " +
					   "FROM XX_VMR_PO_LINEREFPROV PO_REF, M_INOUTLINE M_IOL, C_ORDERLINE C_OL, XX_VMR_REFERENCEMATRIX REF_MATRIX," +
					   "M_PRODUCT PROD, C_ORDER CO WHERE CO.C_ORDER_ID = C_OL.C_ORDER_ID AND M_IOL.C_ORDERLINE_ID = C_OL.C_ORDERLINE_ID " +
					   "AND PROD.M_PRODUCT_ID = M_IOL.M_PRODUCT_ID " +
					   "AND C_OL.XX_VMR_PO_LINEREFPROV_ID = PO_REF.XX_VMR_PO_LINEREFPROV_ID " +
					   "AND PO_REF.XX_VMR_PO_LINEREFPROV_ID = REF_MATRIX.XX_VMR_PO_LINEREFPROV_ID " +
					   "AND REF_MATRIX.M_PRODUCT = C_OL.M_PRODUCT_ID " +
					   "AND PO_REF.C_ORDER_ID = "+ orden.get_ID() +" "
					   + "AND MOVEMENTQTY > 0  AND CO.Issotrx = 'N' ";
			} else { 
				sql = "SELECT PO_REF.XX_VMR_PO_LINEREFPROV_ID, PO_REF.XX_PACKAGEMULTIPLE, " +
						" PO_REF.XX_VMR_VENDORPRODREF_ID, " +
						" M_PRODUCT, XX_VALUE1, XX_VALUE2, (XX_QUANTITYV + XX_QUANTITYO) XX_QTY, " +
						" XX_SALEPRICE, XX_TaxAmount, XX_SALEPRICEPLUSTAX, PO_REF.C_TaxCategory_ID, XX_Margin, PRICEACTUAL," +
						" XX_ISDEFINITIVE, XX_UnitPurchasePrice, " +
						" PROD.XX_VMR_DEPARTMENT_ID, PROD.XX_VMR_LINE_ID, PROD.XX_VMR_SECTION_ID " +
						" FROM XX_VMR_PO_LINEREFPROV PO_REF LEFT JOIN XX_VMR_REFERENCEMATRIX REF_MATRIX " +						
						" ON (PO_REF.XX_VMR_PO_LINEREFPROV_ID = REF_MATRIX.XX_VMR_PO_LINEREFPROV_ID) " +
						" LEFT JOIN M_PRODUCT PROD ON (REF_MATRIX.M_PRODUCT = PROD.M_PRODUCT_ID)" +
						" WHERE PO_REF.C_ORDER_ID = " +  orden.get_ID() + " AND (XX_QUANTITYV + XX_QUANTITYO) > 0 ";				
			}
			return sql;
		} else {
			
			sql= "SELECT T.M_PRODUCT_ID M_PRODUCT, T.XX_DESIREDQUANTITY XX_QTY, T.XX_VMR_DEPARTMENT_ID, T.XX_VMR_LINE_ID, T.XX_VMR_SECTION_ID," +
			" T.XX_VMR_DISTRIBUTIONDETAIL_ID, VP.XX_PACKAGEMULTIPLE " +
			" FROM XX_VMR_DISTRIBDETAILTEMP T JOIN XX_VMR_DISTRIBUTIONDETAIL D ON (D.XX_VMR_DISTRIBUTIONDETAIL_ID = T.XX_VMR_DISTRIBUTIONDETAIL_ID)" +
			" JOIN M_PRODUCT P ON (T.M_PRODUCT_ID = P.M_PRODUCT_ID)" +
			" JOIN XX_VMR_VENDORPRODREF VP ON (VP.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID)"+
			" WHERE D.XX_VMR_DISTRIBUTIONHEADER_ID =  " + cabecera.get_ID() + " AND t.M_PRODUCT_ID IS NOT NULL ";
		}		
		return sql;
	}
	
	
	/** Distribuye un producto */
	private void distribuirProducto (ResultSet rs_a) throws SQLException { 
		
		XX_StoreAmounts porcentajesTiendas = obtenerPorcentaje(rs_a);		
		if (porcentajesTiendas == null) {
			ADialog.error(1, new Container(), Msg.translate(ctx, "XX_ThereAreNoPercentages")); 
			procesado = false;
			return ;
		}	
		 
		//Variables de Distribucion 
		int linea = 0, referencia = 0, producto = 0, valor1 = 0, valor2 = 0, valorCurva = 0,  paquete = 1, distribucionID = 0;
		Hashtable<Integer, Integer> size_curve;
		//Verifica si hay una distribucion previa que reutilizar
		int idDistribucionProducto = 0;
		boolean existeDistribucion = false;
		
		producto = rs_a.getInt("M_PRODUCT");
		if (productosViejos.containsKey(producto)) {					
			idDistribucionProducto = productosViejos.get(producto);
			existeDistribucion = true;
		} 
		
		if (orden != null) { //Caso de Orden de Compra		
			
			referencia = rs_a.getInt("XX_VMR_VENDORPRODREF_ID");
			valor1 = rs_a.getInt("XX_VALUE1");
			valor2 = rs_a.getInt("XX_VALUE2");
			
			if (ignorarMultiplo)				
				paquete = 1;
			else 
				paquete = rs_a.getInt("XX_PACKAGEMULTIPLE");
			
			//Verificar que la referencia tenga productos
			if (producto == 0 ) {
				X_XX_VMR_VendorProdRef vendor = new X_XX_VMR_VendorProdRef(Env.getCtx(), referencia, null); 							
				String mss = Msg.getMsg(Env.getCtx(), "XX_UnAssociatedProducts", 
						//reference
						//line
						new String[] { vendor.getValue()+ "-" + vendor.getName(),"" + linea});
				ADialog.error(1, new Container(), mss);						
				procesado = false;
				return; 
			}
			
			size_curve = curvaDeTallas.get(referencia);				
			if (size_curve != null) {					
				if (size_curve.containsKey(valor1)) {
					valorCurva = size_curve.get(valor1);						
				} else if (size_curve.containsKey(valor2)) {
					valorCurva = size_curve.get(valor2);					
				}
			}	

			//Crea una distribucion de productos y la configura si no existia
			X_XX_VMR_PO_ProductDistrib distribution = new X_XX_VMR_PO_ProductDistrib(ctx, idDistribucionProducto, null);
			if (!existeDistribucion) {
				XX_DistributionUtilities.configurarDistribucionOC(cabecera.get_ID(), rs_a, distribution);
			}			
			//Valor de la curva			
			if (valorCurva > 0) 
				distribution.setXX_UsedSizeCurve(true);
			else 
				distribution.setXX_UsedSizeCurve(false);				
			distribution.save(nombreTrx);
			distribucionID = distribution.get_ID();

		} else {
			
			//Caso Redistribucion de Inventario
			producto = rs_a.getInt("M_PRODUCT");									

			MProduct productoObj = new MProduct(Env.getCtx(), producto, null);				
			X_XX_VMR_DistribProductDetail productoDet = new X_XX_VMR_DistribProductDetail(ctx, idDistribucionProducto , null);						
			if (!existeDistribucion) {
				if (!XX_DistributionUtilities.configurarDistribucionInv(rs_a, productoDet, productoObj)) {	
					procesado = false;
					return;
				}						
			}

			//Recalcular la curva
			String SQL_b = " SELECT I.M_ATTRIBUTEVALUE_ID FROM M_ATTRIBUTEINSTANCE I WHERE M_ATTRIBUTESETINSTANCE_ID = "
				+ productoObj.getM_AttributeSetInstance_ID() + " AND I.M_ATTRIBUTEVALUE_ID IS NOT NULL ORDER BY I.M_ATTRIBUTEVALUE_ID";
			PreparedStatement pstmt_att = DB.prepareStatement(SQL_b, null);
			ResultSet rs_att = pstmt_att.executeQuery();				

			//Verificar la curva de tallas				
			size_curve = curvaDeTallas.get(productoObj.getXX_VMR_VendorProdRef_ID());				
			if (size_curve != null) {
				if (rs_att.next()) { 
					if (size_curve.containsKey(rs_att.getInt(1))) 
						valorCurva = size_curve.get(rs_att.getInt(1));												
				}
				if (rs_att.next()) { 
					if (size_curve.containsKey(rs_att.getInt(1))) 
						valorCurva = size_curve.get(rs_att.getInt(1));												
				}
				if (rs_att.next()) { 
					if (size_curve.containsKey(rs_att.getInt(1))) 
						valorCurva = size_curve.get(rs_att.getInt(1));											
				}					
			}			
			pstmt_att.close();
			rs_att.close();	
							
			//Verificar si hay curva
			if (valorCurva > 0) 				
				productoDet.setXX_UseSizeCurve(true);
			else 
				productoDet.setXX_UseSizeCurve(false);
			productoDet.save(nombreTrx);
			distribucionID = productoDet.get_ID();
			
			//Colocar el detalle como distribuido
			X_XX_VMR_DistributionDetail detalle = new X_XX_VMR_DistributionDetail(ctx, rs_a.getInt("XX_VMR_DISTRIBUTIONDETAIL_ID"), null);
			detalle.setXX_CalculatedPER(true);
			detalle.setXX_CalculatedQTY(false);
			detalle.setXX_DistributionApplied(true);
			detalle.save(nombreTrx);

		}
		
		if (ignorarMultiplo)				
			paquete = 1;
		else 
			paquete = rs_a.getInt("XX_PACKAGEMULTIPLE");
		
		Vector<Integer> warehouses = porcentajesTiendas.tiendas;
		Vector<Float> percentages =  porcentajesTiendas.cantidades;	
		Vector<Integer> priorityToAdd = porcentajesTiendas.obtPrioridadesAgregar();
		
		//SE USA PARA PRUEBAS
//		for (int i = 0; i <percentages.size(); i++) {
//			System.out.println("porc: "+percentages.elementAt(i).floatValue()+" - tienda:"+warehouses.elementAt(i));
//		}
		
		if (!isPiecesAuto) //Si diferente a ajuste automático por piezas se distribuye obligatoriamente un paquete a cada tienda al comienzo de la distribución
			XX_DistributionUtilities.aplicarPorcentajes(distribucionID, orden, warehouses, 
					percentages, rs_a.getInt("XX_QTY"), priorityToAdd, nombreTrx, ctx, 1, paquete, 1);
		else if (isPiecesAuto) //Si es ajuste automático por piezas no se distribuye obligatoriamente un paquete a cada tienda al comienzo de la distribución
			XX_DistributionUtilities.aplicarPorcentajes(distribucionID, orden, warehouses, 
					percentages, rs_a.getInt("XX_QTY"), priorityToAdd, nombreTrx, ctx, 1, paquete, 0);	

	}	
	
	/** Distribuye los productos que presentan inconsistencia entre 
	 * lo distribuido previamente y lo chequeado
	 * 
	 * @author GHUCHET
	 * */
	private void distribuirInconsistentes() {

		int prodDistID, prodID= 0;
		String sql, sql2, sql3 = "";
		sql = "\nSELECT XX_VMR_PO_PRODUCTDISTRIB_ID, XX_DISTRIBUTEDQTY, M_PRODUCT_ID" +
			"\nFROM XX_VMR_PO_PRODUCTDISTRIB WHERE " +
			"\nXX_VMR_DISTRIBUTIONHEADER_ID =  " +cabecera.get_ID(); 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs3 = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				prodDistID = rs.getInt("XX_VMR_PO_PRODUCTDISTRIB_ID");
				prodID = rs.getInt("M_PRODUCT_ID");
				sql2 = "\nSELECT  CASE WHEN SUM(XX_PRODUCTQUANTITY) = "+rs.getInt("XX_DISTRIBUTEDQTY")+ " THEN 1 ELSE 0 END "+
				"\nFROM XX_VMR_PO_DISTRIBDETAIL" +
				"\nWHERE XX_VMR_PO_PRODUCTDISTRIB_ID = "+prodDistID;
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();
				if(rs2.next()) {
					if(rs2.getInt(1) == 0){
						   sql3 = "\nSELECT PO_REF.XX_VMR_PO_LINEREFPROV_ID, PO_REF.XX_PACKAGEMULTIPLE, " +
						   "\nPO_REF.XX_VMR_VENDORPRODREF_ID, M_IOL.M_PRODUCT_ID M_PRODUCT," +
//						   "\nREF_MATRIX.XX_VALUE1, REF_MATRIX.XX_VALUE2, "+rs.getInt("XX_DISTRIBUTEDQTY")+ " XX_QTY, " + //USADO PARA PRUEBAS
						   "\nREF_MATRIX.XX_VALUE1, REF_MATRIX.XX_VALUE2, MOVEMENTQTY XX_QTY, " +
						   "\nPO_REF.XX_SALEPRICE, PO_REF.XX_TaxAmount, PO_REF.XX_SALEPRICEPLUSTAX, " +
						   "\nPO_REF.C_TaxCategory_ID, PO_REF.XX_Margin, PO_REF.PRICEACTUAL, " +
						   "\nPO_REF.XX_ISDEFINITIVE, PO_REF.XX_UnitPurchasePrice, " +
						   "\nPROD.XX_VMR_DEPARTMENT_ID, PROD.XX_VMR_LINE_ID, PROD.XX_VMR_SECTION_ID " +
						   "\nFROM XX_VMR_PO_LINEREFPROV PO_REF, M_INOUTLINE M_IOL, C_ORDERLINE C_OL, XX_VMR_REFERENCEMATRIX REF_MATRIX," +
						   "\nM_PRODUCT PROD, C_ORDER CO WHERE CO.C_ORDER_ID = C_OL.C_ORDER_ID AND M_IOL.C_ORDERLINE_ID = C_OL.C_ORDERLINE_ID " +
						   "\nAND PROD.M_PRODUCT_ID = M_IOL.M_PRODUCT_ID " +
						   "\nAND C_OL.XX_VMR_PO_LINEREFPROV_ID = PO_REF.XX_VMR_PO_LINEREFPROV_ID " +
						   "\nAND PO_REF.XX_VMR_PO_LINEREFPROV_ID = REF_MATRIX.XX_VMR_PO_LINEREFPROV_ID " +
						   "\nAND REF_MATRIX.M_PRODUCT = C_OL.M_PRODUCT_ID " +
						   "\nAND PO_REF.C_ORDER_ID = "+ cabecera.getC_Order_ID() +
						   "\nAND M_IOL.M_PRODUCT_ID = "+ prodID +
						   "\nAND PO_REF.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
						   "\nAND MOVEMENTQTY > 0 ";
							pstmt3 = DB.prepareStatement(sql3, null);
							rs3 = pstmt3.executeQuery();
							//Se elimina el detalle de la distribución si el producto tiene inconsistencia
							XX_DistributionUtilities.eliminarDetalleOC(prodDistID, nombreTrx);
							//Se distribuye el producto con inconsistencia
							
							while (rs3.next() && procesado) {									
								distribuirProducto(rs3);
							}
							DB.closeResultSet(rs3);
							DB.closeStatement(pstmt3);
					}
				}
				DB.closeResultSet(rs2);
				DB.closeStatement(pstmt2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			procesado = false;		
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}	
	}
	
	
	
	
}



