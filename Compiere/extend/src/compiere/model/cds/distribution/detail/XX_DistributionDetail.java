package compiere.model.cds.distribution.detail;

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

import compiere.model.cds.MProduct;
import compiere.model.cds.X_XX_VMR_DistribProductDetail;
import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.distribution.XX_DistributionUtilities;
import compiere.model.cds.distribution.XX_StoreAmounts;


/** Clase abstracta padre que almacena los metodos genericos de distribucion por detalle
 *  y será luego extendida por cada clase hija 
 *  (modificación de código de Javier Pino para distribuciones completas)
 *  
 *  @author Gabrielle Huchet
 */
public abstract class XX_DistributionDetail {

	protected boolean esOrdenDeCompra; //Indica si la distribucion es por orden de compra
	protected Trx nombreTrx;
	protected X_XX_VMR_DistributionHeader cabecera;
	protected X_XX_VMR_DistributionDetail detalle;
//	protected MOrder orden;
	protected Ctx ctx ;
	protected boolean ignorarMultiplo = false, usarVentasAñoPasado = false, isAllDist  = false;
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
	

	/** Constructor por defecto */
	protected XX_DistributionDetail(int detalleID, Ctx contexto, Trx trxNombre, int mes, int año) {
		
		ctx = contexto;
		nombreTrx = trxNombre;
		detalle = new X_XX_VMR_DistributionDetail(contexto, detalleID, null);
		cabecera = new X_XX_VMR_DistributionHeader(contexto, detalle.getXX_VMR_DistributionHeader_ID(), null);
		
		this.mes = mes;
		this.año = año;
	}
	
	//Todos deben inicializarse
	protected abstract void inicializar() ;
	
	//Todos deben cerrarse en caso
	protected abstract void finalizar(); 
		
	/**
	 * Con este metodo se calcula el porcentaje de cada producto de acuerdo a la distribucion usada 
	 */
	protected abstract XX_StoreAmounts obtenerPorcentaje(ResultSet rsProductos) throws SQLException;
	
	/** Metodo general de procesamiento*/
	public final void procesar () {

		//Inicializar
		if (finalizado)
			return;
		
		inicializar();
		if (!inicializado) {
			finalizado = false;
			return;
		}

		//Comenzó el procesamiento correctamiento
		procesado = true;
		ignorarMultiplo = cabecera.isXX_IgnorePackageMultiple();		
		XX_DistributionUtilities.prepararCurvaDeTallas(cabecera.get_ID(), curvaDeTallas);
		
		//Antes de distribuir nuevamente eliminar la distribucion pasada
		XX_DistributionUtilities.eliminarDetalle(detalle.get_ID(), nombreTrx);
		
		//Tambien se obtiene un hash con los productos de la dist si ya se ha distribuido
		XX_DistributionUtilities.buscarDetallesDeProductos(cabecera.get_ID(),null, productosViejos);
		 	
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
		//Cerrar el objeto
		if (procesado) {					
			finalizar();
		}
		if (finalizado) {
			//Todo estuvo correcto así que hacer commit
			nombreTrx.commit();
			if (productosConMultiplosYCurvas) {
				ADialog.error(1, new Container(), "XX_BothSizeAndPackageMultiple");				
			} else if (productosConCurvaNoRespetada) { 
				ADialog.info(1, new Container(), "XX_UnMatchedProductSizeCurve");
			}
		} else {
			nombreTrx.rollback();
		}		
	}

	/**
	 * Este metodo devuelve un String con los productos y e informacion que necesita para distribuir 
	 */
	public String productosADistribuir () {
				
		String sql = null;
			sql= "SELECT T.M_PRODUCT_ID M_PRODUCT, T.XX_DESIREDQUANTITY XX_QTY, T.XX_VMR_DEPARTMENT_ID, T.XX_VMR_LINE_ID, T.XX_VMR_SECTION_ID," +
			" T.XX_VMR_DISTRIBUTIONDETAIL_ID, VP.XX_PACKAGEMULTIPLE " +
			" FROM XX_VMR_DISTRIBDETAILTEMP T JOIN XX_VMR_DISTRIBUTIONDETAIL D ON (D.XX_VMR_DISTRIBUTIONDETAIL_ID = T.XX_VMR_DISTRIBUTIONDETAIL_ID)" +
			" JOIN M_PRODUCT P ON (T.M_PRODUCT_ID = P.M_PRODUCT_ID)" +
			" JOIN XX_VMR_VENDORPRODREF VP ON (VP.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID)"+
			" WHERE D.XX_VMR_DISTRIBUTIONDETAIL_ID =  " + detalle.get_ID() + " AND t.M_PRODUCT_ID IS NOT NULL ";
			
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
		int  producto = 0,  valorCurva = 0,  paquete = 1, distribucionID = 0;
		
		//Verifica si hay una distribucion previa que reutilizar
		int idDistribucionProducto = 0;
		boolean existeDistribucion = false;
		
		producto = rs_a.getInt("M_PRODUCT");
		if (productosViejos.containsKey(producto)) {					
			idDistribucionProducto = productosViejos.get(producto);
			existeDistribucion = true;
		} 
		
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
			Hashtable<Integer, Integer> curva = curvaDeTallas.get(productoObj.getXX_VMR_VendorProdRef_ID());				
			if (curva != null) {
				if (rs_att.next()) { 
					if (curva.containsKey(rs_att.getInt(1))) 
						valorCurva = curva.get(rs_att.getInt(1));												
				}
				if (rs_att.next()) { 
					if (curva.containsKey(rs_att.getInt(1))) 
						valorCurva = curva.get(rs_att.getInt(1));												
				}
				if (rs_att.next()) { 
					if (curva.containsKey(rs_att.getInt(1))) 
						valorCurva = curva.get(rs_att.getInt(1));											
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

		
		
		if (ignorarMultiplo)				
			paquete = 1;
		else 
			paquete = rs_a.getInt("XX_PACKAGEMULTIPLE");
				
		Vector<Integer> warehouses = porcentajesTiendas.tiendas;
		Vector<Float> percentages =  porcentajesTiendas.cantidades;	
		Vector<Integer> priorityToAdd = porcentajesTiendas.obtPrioridadesAgregar();
		
		//USADO PARA PRUEBAS
//		for (int i = 0; i <percentages.size(); i++) {
//			System.out.println("porc: "+percentages.elementAt(i).floatValue()+" - tienda:"+warehouses.elementAt(i));
//		}

		XX_DistributionUtilities.aplicarPorcentajes(distribucionID, null, warehouses, 
					percentages, rs_a.getInt("XX_QTY"), priorityToAdd, nombreTrx, ctx, 1, paquete, 1);
		
	}	
	
}
