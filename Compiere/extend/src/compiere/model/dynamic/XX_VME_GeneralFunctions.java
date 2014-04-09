package compiere.model.dynamic;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MProduct;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_Package;
import compiere.model.cds.X_XX_VMR_VendorProdRef;
import compiere.model.cds.callouts.VME_PriceProductCallout;

/**
 * 
 * Esta clase contiene funciones de uso general, es decir, que pueden utilizadas 
 * en varias clases, preferiblemente para aquellas relacionadas con dinámica 
 * comercial.
 * @author mvintimilla
 * @version 1.0
 */

public class XX_VME_GeneralFunctions {
	
	// Query utilizado para buscar los elementos de tipo producto en un folleto 
	// determinado
	static String query_busqueda_elemento = " SELECT E.XX_VME_Elements_ID " +
							" FROM XX_VMA_BrochurePage BP INNER JOIN XX_VME_Elements E " +
							" ON (E.XX_VMA_BrochurePage_ID = BP.XX_VMA_BrochurePage_ID)" +
							" WHERE  (E.XX_VME_Type = 'P' or E.XX_VME_Type = 'M') " +
							" AND BP.XX_VMA_Brochure_ID = ";
	
	// Query utilizado para buscar los productos en un folleto 
	// determinado
	static String query_busqueda_producto = " SELECT P.XX_VME_PRODUCT_ID " +
							" FROM XX_VMA_MarketingActivity M INNER JOIN XX_VMA_Brochure B " +
							" on (M.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
							" INNER JOIN XX_VMA_BROCHUREPAGE BP " +
							" on (BP.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
							" INNER JOIN XX_VME_ELEMENTS E " +
							" ON (E.XX_VMA_BROCHUREPAGE_ID = BP.XX_VMA_BROCHUREPAGE_ID) " +
							" INNER JOIN XX_VME_REFERENCE R " +
							" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
							" INNER JOIN XX_VME_PRODUCT P " +
							" ON (R.XX_VME_REFERENCE_ID = P.XX_VME_REFERENCE_ID) " +
							" WHERE  (E.XX_VME_TYPE = 'P' OR E.XX_VME_TYPE = 'M') " +
							" AND BP.XX_VMA_BROCHURE_ID = ";
	
	/** promotionalPriceFinder
	 * Busca el precio promocional de un producto que se agrega a una acción
	 * de mercadeo
	 * @param price		precio actual del producto
	 * @param codpro	codigo beco del producto
	 * @param cat		categoría
	 * @param dept		departamento
	 * @param linea		línea
	 * @param seccion	sección
	 * @param brand 	marca
	 * @param fechaI	fecha en que inicia una acción de mercadeo de tipo folleto
	 * @param fechaF	fecha em que culmina una acción de mercadeo de tipo folleto
	 * @return			precio de promoción del producto.
	 */
	public static BigDecimal promotionalPriceFinder (BigDecimal price, int codpro, 
			int cat, int dept, int linea, int seccion, int brand, 
			Timestamp fechaI, Timestamp fechaF){
		int index = 0;
		int prio = 0;
		BigDecimal preciodesc = new BigDecimal(0);
		BigDecimal cien = new BigDecimal(100);
		Vector<Integer> promocionesID = new Vector<Integer>();
		Vector<Integer> promocionesPrio = new Vector<Integer>();
		Vector<BigDecimal> promocionesDesc = new Vector<BigDecimal>();
		Vector<BigDecimal> promocionesPrecio = new Vector<BigDecimal>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQLPromAS = " SELECT " +
				" DISTINCT XX_VMR_PROMPRODAS_ID PROMO, " +
				" PRICEPROM FINAL, " +
				" CASE WHEN DISCPROM IS NULL THEN 0 " +
				" ELSE DISCPROM END DISCOUNT, " +
				" 1 PRIO" +
				" FROM XX_VMR_PROMPRODAS P " +
				" WHERE P.CODPROD = " + codpro +
				" AND (P.INITIALDATE >= " + DB.TO_DATE(fechaI) + 
				" AND P.FINALDATEP <= " + DB.TO_DATE(fechaF) + ")";
		
		String SQLPromComp = " SELECT " +
				" DISTINCT P.XX_VMR_PROMOTION_ID PROMO, " +
				" CASE WHEN D.XX_DISCOUNTRATE IS NULL THEN 0 " +
				" ELSE D.XX_DISCOUNTRATE END DISCOUNT," +
				" XX_DISCOUNTAMOUNT FINAL, " +
				" PRIORITY PRIO " +
				" FROM XX_VMR_PROMOTION P JOIN XX_VMR_DETAILPROMOTIONEXT D " +
				" ON (P.XX_VMR_PROMOTION_ID = D.XX_VMR_PROMOTION_ID)" +
				" INNER JOIN AD_REF_LIST R ON (P.XX_TYPEPROMOTION = R.VALUE)" +
				" WHERE (D.M_PRODUCT_ID = " + codpro +
				" OR D.XX_VMR_CATEGORY_ID = " + cat +
				" OR D.XX_VMR_DEPARTMENT_ID = " + dept +
				" OR D.XX_VMR_LINE_ID =" + linea +
				" OR D.XX_VMR_SECTION_ID = " + seccion + 
				" OR D.XX_VMR_BRAND_ID = " + brand + ")" +
				" AND (P.XX_APPROVEMER= 'Y' OR P.XX_APPROVEMAR= 'Y') " +
				" AND (P.DATEFROM <= " + DB.TO_DATE(fechaI) +
				" AND P.DATEFINISH >= " + DB.TO_DATE(fechaF) + ") " +
				" AND D.XX_WAREHOUSEBECONUMBER='000'" +
				" AND R.AD_REFERENCE_ID = 1000013 " +
				" AND R.NAME LIKE 'E3%' " +
				" ORDER BY PRIORITY ASC ";

		// 	Primero se busca en las promociones que se encuentran en el AS/400
		try{
			pstmt = DB.prepareStatement(SQLPromAS, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				promocionesDesc.add(rs.getBigDecimal("DISCOUNT"));
				promocionesPrecio.add(rs.getBigDecimal("FINAL"));
				promocionesID.add(rs.getInt("PROMO"));
				promocionesPrio.add(rs.getInt("PRIO"));
			}//while
		}//try
		catch (Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// Promociones en compiere
		try{
			pstmt = DB.prepareStatement(SQLPromComp, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				promocionesDesc.add(rs.getBigDecimal("DISCOUNT"));
				promocionesPrecio.add(rs.getBigDecimal("FINAL"));
				promocionesID.add(rs.getInt("PROMO"));
				promocionesPrio.add(rs.getInt("PRIO"));
			}//while
		}//try
		catch (Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			pstmt = null;
		}
		// Se busca la promocion con mayor prioridad
		for(int i = 0; i < promocionesPrio.size(); i++){
			if(promocionesPrio.get(i) > prio){
				prio = promocionesPrio.get(i);
				index = i;
			}	
		}
		
		// Se calcula el precio con el descuento
		if(promocionesDesc.size() > 0 ){
			if(promocionesDesc.get(index).compareTo(new BigDecimal(0)) > 0){
				preciodesc = price.subtract(price.multiply((promocionesDesc.get(index)).divide(cien)));
			}
			else {
				preciodesc = promocionesPrecio.get(index);
			}
		}
		
		return preciodesc;
	} // Fin promotionalPriceFinder
	
	/** precioPromoCompiere
	 * Se busca el precio promocional de un producto dentro del conjunto de 
	 * promociones existentes en Compiere.
	 * 
	 * @param price			precio regular del producto
	 * @param finalprice	precio promocional más bajo que se ha conseguido 
	 * 						aplicando las promociones del AS/400
	 * @param sql			query que permite buscar al producto dentro de las 
	 * 						promociones de compiere
	 * @return				precio de promoción aplacando las promociones de 
	 * 						compiere sobre el producto
	 */
	/*
	public static double precioPromoCompiere (double price, double finalprice, 
			String sql ){
		double preciofinal = finalprice;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double descuentoC,priceWD;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				descuentoC = rs.getDouble(1);
				priceWD = price-(price*(descuentoC/100));
				// Se compara los precios aplicando los porcentajes de descuento 
				// contra el precio final y el precio regular
				if(preciofinal > priceWD){
					preciofinal = priceWD;
				}
			}//while

		}
		catch (Exception e){ e.printStackTrace(); }	
		finally {
			try { rs.close(); } 
			catch (SQLException e) { e.printStackTrace(); }
			try { pstmt.close(); } 
			catch (SQLException e) { e.printStackTrace(); }
			pstmt = null;
		}
		return preciofinal;
	} // Fin precioPromoCompiere
	*/
	
	/** actualizarPrecioPromocion
	 * Esta función actualiza los precios de productos del folleto, que todavía 
	 * no ha sido publicado.
	 * 
	 * @param ctx		contexto
	 * @param Brochure	folleto al cual se le van a actualizar los precios de 
	 * 					promoción de los productos
	 * @param fechaI	fecha de inicio de la acción de mercadeo relacionada 
	 * 					al folleto
	 * @param fechaF	fecha de culminación de la acción de mercadeo
	 * @param category	Identificador de la categoria
	 * @throws Exception
	 */
	
	public static void actualizarPrecioPromocion (int Brochure, Timestamp 
			fechaI, Timestamp fechaF)throws Exception{
		BigDecimal precioPromocion = new BigDecimal(0);	
		BigDecimal precioActual = new BigDecimal(0);
		String sql = query_busqueda_producto + Brochure;
//		System.out.println("SQL: "+sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			try {
				while(rs.next()){
					X_XX_VME_Product product = 
						new X_XX_VME_Product(Env.getCtx(),rs.getInt(1),null);
					X_XX_VMR_Department dep = 
						new X_XX_VMR_Department(Env.getCtx(),product.getXX_VMR_Department_ID(), null);
					
					// Se busca el precio de promoción y se verifica si es menor 
					// al que tiene actualmente, de ser menor se sustituye en el 
					// elemento del folleto				
					precioPromocion = promotionalPriceFinder(product.getXX_VME_ActualPrice(), 
							product.getM_Product_ID(),
							dep.getXX_VMR_Category_ID(),
							product.getXX_VMR_Department_ID(), 
							product.getXX_VMR_Line_ID(), 
							product.getXX_VMR_Section_ID(),
							product.getXX_VMR_Brand_ID(),
							fechaI,fechaF);
					
					
					// Actuaizar precio actual del producto
					precioActual = setPriceBPVendorR(product.getM_Product_ID(),product.getXX_VMR_VendorProdRef_ID());
					product.setXX_VME_ActualPrice(precioActual);
					
					if(precioPromocion.compareTo(product.getXX_VME_PromotionalPrice()) < 0){
						product.setXX_VME_PromotionalPrice(precioPromocion);
						
						
					}
					product.save();
				} // while rs
			} // try
			catch(Exception e){ e.printStackTrace(); }
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		} // try
		catch(SQLException e){
			throw new Exception("error actualizando los precios de promocion "+e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	
	} // actualizarPrecioPromocion
	
	/** setCountry
	 * Esta función se encarga de buscar el identificador del país de un socio 
	 * del negocio de tipo proveedor.
	 * @param bpartner identificador del socio de negocio
	 * @return 	identificador del país del proveedor
	 */
	public static int setCountry (int bpartner){
		String sql = " SELECT C.C_Country_ID " +
				" FROM C_BPartner_location BP INNER JOIN C_Location L" +
				" ON (BP.C_Location_ID = L.C_Location_ID)" +
				" INNER JOIN C_Country C " +
				" ON (L.C_Country_ID = C.C_Country_ID)" +
				" WHERE BP.C_Bpartner_ID = " + bpartner;
		
		int country = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				country = rs.getInt(1);
			}
		} // try
		catch (Exception e){ e.printStackTrace(); }	
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return country;
	} // Fin setCountry

	/** setPriceBPVendorR
	 * Esta función busca los datos referentes a un producto determinado como precio,
	 * proveedor y la referencia que este le da al producto.
	 * @param productId	Identificador del producto
	 * @param referenceId Identificador de la referencia del producto		
	 * @return el precio del producto.
	 */
	public static BigDecimal setPriceBPVendorR (int productId, int referenceID){
		Vector<BigDecimal> consecutives = new Vector<BigDecimal>();
		BigDecimal salePrice = new BigDecimal(0);
		int index = 0;
		
		// Definir el precio de venta
		// Si el producto tiene consecutivo de precio se toma de allí, en caso 
		// contrario se toma de la O/C y viene a ser un precio de venta estimado
		String sqlPrice = " SELECT XX_SalePrice Price " +
				" FROM XX_VMR_PriceConsecutive " +
				" WHERE M_Product_ID = "+productId +
				" ORDER BY CREATED DESC";
		//System.out.println("Consecutivo: "+sqlPrice);
		ResultSet rs3 = null;
		PreparedStatement pstmt3 = null;

		try{
			pstmt3 = DB.prepareStatement(sqlPrice, null);
			rs3 = pstmt3.executeQuery();
			while(rs3.next()){
				consecutives.add(rs3.getBigDecimal("Price"));
			}// while
		
		}//try
		catch (Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs3);
			DB.closeStatement(pstmt3);
			pstmt3 = null;
		}

		// No tiene precio en consecutivo, se busca en PO_LineRefProv
		if(consecutives.size() == 0){
			String sqlPricePO = " SELECT L.XX_SALEPRICE PRECIO " +
					" FROM XX_VMR_PO_LINEREFPROV L " +
					" WHERE L.XX_VMR_VENDORPRODREF_ID = " + referenceID + " AND L.CREATED = " +
					" 	(SELECT MAX(LL.CREATED)" +
					" 	FROM XX_VMR_PO_LINEREFPROV LL " +
					" 	WHERE L.XX_VMR_VENDORPRODREF_ID = LL.XX_VMR_VENDORPRODREF_ID " +
					" 	AND LL.XX_VMR_VENDORPRODREF_ID = " + referenceID + ")";
			//System.out.println("Orden: "+sqlPricePO);
			ResultSet rs4 = null;
			PreparedStatement pstmt4 = null;
			
			try{
				pstmt4 = DB.prepareStatement(sqlPricePO, null);
				rs4 = pstmt4.executeQuery();
				if(rs4.next()){
					salePrice = rs4.getBigDecimal("PRECIO");
				}
				else {
					salePrice = new BigDecimal(0);
				}
				DB.closeResultSet(rs4);
				DB.closeStatement(pstmt4);
				pstmt4 = null;
			}//try
			catch(Exception e){ e.printStackTrace(); }
		}// if consecutivos
		else {
			//Se verifican los consecutivos de precio
			for(int i = 0; i < consecutives.size(); i++){
				if(consecutives.get(i).equals(new BigDecimal(0))){
					continue;
				}
				else {
					salePrice = consecutives.get(i);
					break;
				}
			}
		} // else consecutivos
		
		return salePrice;
	} // Fin


	/** verifyProductInBrochure
	 * Se verifica que el producto que se quiere agregar no exista dentro
	 * del folleto.
	 * @param elemID 		identificador del elemento
	 * @param prodID 		identificador del product
	 * @return boolean 	true si el producto existe, false si no.	
	 */	
	public static boolean verifyProductInBrochure (int elemID, int prodID) {
		String sql = "";
		boolean result = false;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
	
		sql = " SELECT * " +
				" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
				" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
				" INNER JOIN XX_VME_PRODUCT P " +
				" ON (R.XX_VME_REFERENCE_ID = P.XX_VME_REFERENCE_ID) " +
				" WHERE E.XX_VME_ELEMENTS_ID = " + elemID +
				" AND P.M_PRODUCT_ID = " + prodID;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
		
			if(rs.next()){
				result = true;
			}
		} // try
		catch (Exception e){ e.printStackTrace(); }
		finally {
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return result;
	} // Fin verifyProductInBrochure

	/** verifyProductInPage
	 * Se verifica que el producto que se quiere agregar no exista dentro
	 * de la pagina.
	 * @param prodID 		identificador del product
	 * @param pageD 		identificador de la pagina
	 * @return boolean 	true si el producto existe, false si no.	
	 */	
	public static boolean verifyProductInPage (int prodID, int pageID) {
		String sql = "";
		boolean result = false;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
	
		sql = " SELECT * " +
				" FROM XX_VMA_BROCHUREPAGE BP INNER JOIN XX_VME_ELEMENTS E " +
				" ON (BP.XX_VMA_BROCHUREPAGE_ID = E.XX_VMA_BROCHUREPAGE_ID)" +
				" INNER JOIN XX_VME_REFERENCE R " +
				" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
				" INNER JOIN XX_VME_PRODUCT P " +
				" ON (R.XX_VME_REFERENCE_ID = P.XX_VME_REFERENCE_ID) " +
				" WHERE BP.XX_VMA_BROCHUREPAGE_ID = " + pageID +
				" AND P.M_PRODUCT_ID = " + prodID;
		//System.out.println(sql);
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
		
			if(rs.next()){
				result = true;
			}
		} // try
		catch (Exception e){ e.printStackTrace(); }
		finally {
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return result;
	} // Fin verifyProductInBrochure
	
	/** agregarDept
	 * Función que agrega un nuevo departamento a una página a la hora de mover 
	 * un elemento a otra página diferente del folleto en la que no este asociado 
	 * dicho departamento.
	 * 
	 * @param Page			página a la cual se le va a asociar el departamento
	 * @param departamento	departamento que se va a asociar
	 * @param ctx			contexto
	 * @return				true si se agregó, false si no porque ya existe el 
	 * 						departamento en la página.
	 * @throws Exception
	 */
	// TODO NO SE USA ??
	public static boolean agregarDept(int Page, int departamento, Ctx ctx) 
		throws Exception{
		/* Query que se utiliza para comprobar si el departamento existe o 
		 * no en la página a la cual se va a mover el elemento*/
		String sql = " SELECT * " +
				" FROM XX_VMA_PageDepartment " +
				" WHERE XX_VMA_BrochurePage_ID = " + Page + 
				" AND XX_VMR_Department_ID = " + departamento;
		//System.out.println("SQL: "+sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql,null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return false;
			}
			else{
				// Se crea la asociación página-departamento a la página 
				X_XX_VMA_PageDepartment pd = 
					new X_XX_VMA_PageDepartment(ctx, 0, null);
				pd.setXX_VMR_Department_ID(departamento);
				pd.setXX_VMA_BrochurePage_ID(Page);
				pd.save();
				return true;
			} // else
		} // try
		catch(SQLException e){
			throw new Exception("error al agregar departamento "+e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // Fin agregarDept
	
	/** setIVA
	 * Extrae el impuesto que se le aplica a los productos que salen a la venta
	 * @param CategoryTax
	 * @return Impuesto al valor agregado (IVA)
	 */
	public static BigDecimal setIVA(int CategoryTax){
		BigDecimal iva = new BigDecimal(0);
		// Busca entre los diferentes impuestos que puedan existir y que sea 
		// aplicado para las ventas
		String sqlIva = " SELECT RATE " +
				" FROM C_TAX " +
				" WHERE SOPOTYPE='B' " +
				" AND C_TAXCATEGORY_ID = " + CategoryTax;
		//System.out.println("SQL IVA: "+sqlIva);
		PreparedStatement pstmtIva = null;
		ResultSet rsIva = null;
		try{
			pstmtIva = DB.prepareStatement(sqlIva, null);
			rsIva = pstmtIva.executeQuery();
			if(rsIva.next()){
				iva = rsIva.getBigDecimal("rate");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}	
		finally {
			DB.closeResultSet(rsIva);
			DB.closeStatement(pstmtIva);
		}

		return (iva.divide(new BigDecimal(100))).add(new BigDecimal(1));
	} // setIVA

	/** verifyDepartment
	 * Se verifica que el departamento de un elemento, que se va a mover de página, 
	 * sea del mismo departamento de la página a la cual se va a mover el mismo
	 * 
	 * @param Dept	departamento de la página a la que se va a mover el elemento
	 * @param Page	departamento del elemento que se quiere mover
	 * @return		false si se puede mover el elemento, true si los departamentos 
	 * 				son diferentes
	 */
	public static boolean verifyDepartment(Integer Dept, int Page){
		boolean bool = false;
		int paginaDept = 0;
		Vector<Integer> depts = new Vector<Integer>();
		
		String SQLDept = " SELECT XX_VMR_DEPARTMENT_ID IdDep" +
				" FROM XX_VMA_PAGEDEPT_V " +
				" WHERE XX_VMA_BrochurePage_ID = " + Page;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQLDept, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				paginaDept = rs.getInt("IdDep");
				depts.add(paginaDept);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}	
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		for (int i = 0; i < depts.size(); i++){
			if(depts.elementAt(i).equals(Dept)){
				bool = true;
			}
		} // for
		return bool;
	}//VerifyDepartment

	/** Proceso que se encarga de calcular el inventario de los productos en un
	 * folleto, para el cálculo de inventario inicial, precio actual de los 
	 * productos de un folleto 
	 * @throws SQLException */
	public static void calculateInventory(Timestamp InitialDate, Timestamp EndDate, 
			Integer BrochureID, Integer ActivityID) throws SQLException{
		Vector <Integer> products = new Vector<Integer>();
		Vector <Integer> cantidades = new Vector<Integer>();
		String SQLActual = " Select trunc(sysdate) actualDate from dual";
		String update = "";
		PreparedStatement psQueryProducts = null;
		PreparedStatement psQueryActual = null;
		ResultSet rsQueryProducts = null;
		ResultSet rsQueryActual = null;
		Timestamp dateActual = null;
		
		try{
			psQueryActual = DB.prepareStatement(SQLActual, null);
			rsQueryActual = psQueryActual.executeQuery();
			while(rsQueryActual.next()){	
				dateActual = rsQueryActual.getTimestamp("actualDate");
			}//WHILE		
		} //try	elementos
		catch(SQLException e){	
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rsQueryActual);
			DB.closeStatement(psQueryActual);
		}

		/** Query para obtener todos los elementos de un folleto asociado a
		 a la actividad de mercadeo anterior */
		String SQLProducts = " SELECT P.XX_VME_PRODUCT_ID PROD, " +
				" CASE WHEN  SUM(S.QTY) IS NULL THEN 0 ELSE SUM(S.QTY) END QTY " +
				" FROM XX_VME_PRODUCT P INNER JOIN XX_VME_REFERENCE R " +
				" ON (P.XX_VME_REFERENCE_ID = R.XX_VME_REFERENCE_ID) " +
				" INNER JOIN XX_VME_ELEMENTS E " +
				" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
				" INNER JOIN XX_VMA_BROCHUREPAGE BP " +
				" ON (E.XX_VMA_BROCHUREPAGE_ID = BP.XX_VMA_BROCHUREPAGE_ID) " +
				" INNER JOIN XX_VMA_BROCHURE B " +
				" ON (BP.XX_VMA_BROCHURE_ID = B.XX_VMA_BROCHURE_ID)" +
				" INNER JOIN XX_VMA_MARKETINGACTIVITY M " +
				" ON (M.XX_VMA_BROCHURE_ID = B.XX_VMA_BROCHURE_ID) " +
				" LEFT OUTER JOIN M_STORAGEDETAIL S " +
				" ON (P.M_PRODUCT_ID = S.M_PRODUCT_ID) " +
				" INNER JOIN M_LOCATOR L ON(L.M_LOCATOR_ID = S.M_LOCATOR_ID) " +
				" WHERE B.XX_VMA_Brochure_ID = " + BrochureID +
				" AND L.ISDEFAULT = 'Y' AND S.QTYTYPE = 'H' " +
				" GROUP BY P.XX_VME_PRODUCT_ID";
		//System.out.println("Query PRODUCTS: " + SQLProducts);
		
		/** Para cada uno de los productos del folleto se setea el valor de 
		 * los campos de inventario */
		try{
			psQueryProducts = DB.prepareStatement(SQLProducts, null);
			rsQueryProducts = psQueryProducts.executeQuery();
			while(rsQueryProducts.next()){	
				products.add(rsQueryProducts.getInt("PROD"));
				cantidades.add(rsQueryProducts.getInt("QTY"));
			}//WHILE		
		} //try	elementos
		catch(SQLException e){	
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rsQueryProducts);
			DB.closeStatement(psQueryProducts);
		}
		
		// Para cada producto se procede al calculo
		for(int i = 0; i < products.size(); i ++){
			if(dateActual.equals(InitialDate)){
				update = " UPDATE XX_VME_PRODUCT SET XX_VME_INVINI = " + cantidades.get(i) + 
						", XX_VME_INVENTORYQTY = "+ cantidades.get(i) +
						" WHERE XX_VME_PRODUCT_ID = " + products.get(i);
			}// inicio
			if(dateActual.after(InitialDate) && dateActual.before(EndDate)){
				update = " UPDATE XX_VME_PRODUCT SET XX_VME_INVENTORYQTY = " + cantidades.get(i) +
						", XX_VME_INVFIN = " + cantidades.get(i) +
						" WHERE XX_VME_PRODUCT_ID = " + products.get(i);
			}//medio
			if(dateActual.equals(EndDate)){
				update = " UPDATE XX_VME_PRODUCT SET XX_VME_INVFIN = " + cantidades.get(i) +
						" WHERE XX_VME_PRODUCT_ID = " +  products.get(i);
			}//final
			if(update!=null && update.length()!=0){
				int updated = DB.executeUpdateEx(update, null);
			}
		} // for productos
		
	} // calculateInventory

	/** setInventory
	 * Setea el inventario inicial y final para los productos de un folleto 
	 * @param ID ID del producto
	 * @param dateActual
	 * @param InitialAM
	 * @param FinalAM
	 * */
	public static void setInventory(Integer product_ID, Timestamp dateActual, 
			Timestamp InitialAM, Timestamp FinalAM){
		// Producto
		X_XX_VME_Product producto = 
			new X_XX_VME_Product(Env.getCtx(), product_ID, null);
		// Referencia asociada al producto
		X_XX_VME_Reference referencia = 
			new X_XX_VME_Reference(Env.getCtx(), producto.getXX_VME_Reference_ID(), null);
		// Elemento asociado a la referencia
		X_XX_VME_Elements elemento = 
			new X_XX_VME_Elements(Env.getCtx(), referencia.getXX_VME_Elements_ID(), null);
		
		Vector <String> products = new Vector<String>();
		Vector cantidades = new Vector();
		
		// Se calculan las cantidades
		cantidades = obtainQuantities(producto.getM_Product_ID(),dateActual, 
					InitialAM, FinalAM);
		BigDecimal cant = (BigDecimal)cantidades.get(0);
		if(dateActual.equals(InitialAM)){
			producto.setXX_VMA_InitialAmount((BigDecimal)cantidades.get(1));
			producto.setXX_VME_InvIni(cant);
			producto.setXX_VME_InventoryQty(cant);
			//TODO Reestructurar esta cantidad estimada
			elemento.setXX_VME_EstimatedQty(cant.
					divide(new BigDecimal(2), BigDecimal.ROUND_HALF_UP));
		}// inicio
		if(dateActual.after(InitialAM) && dateActual.before(FinalAM)){
			producto.setXX_VME_InventoryQty(cant);
			//TODO Reestructurar esta cantidad a publicar
			elemento.setXX_VME_QTYPUBLISHED(cant.
						divide(new BigDecimal(2), BigDecimal.ROUND_HALF_UP));
		}//medio
		if(dateActual.equals(FinalAM)){
			producto.setXX_VME_InvFin(cant);
		}//final	
		// Es una referencia
	} // setInventory

	/** Se verifica la fecha actual contra las de la actividad de mercadeo
	 * para definir los inventarios (inicial/final) respectivos 
	 * @param Product_ID
	 * @param dateActual
	 * @param InitialAM
	 * @param FinalAM*/
	public static Vector obtainQuantities(Integer Product_ID, Timestamp dateActual, 
			Timestamp InitialAM, Timestamp FinalAM){
		Vector cant = new Vector();
		//System.out.println("Inicia: "+InitialAM+" Finaliza: "+FinalAM);
		/** Fecha actual es igual a la fecha de inicio de la actividad de 
		 * mercadeo, se calculan cantidad y monto inicial */
		if(dateActual.equals(InitialAM)){
			String SQLInvIni = " SELECT "+
						" CASE WHEN SUM(XX_INITIALINVENTORYQUANTITY) IS NULL THEN 0 " +
						" ELSE SUM(XX_INITIALINVENTORYQUANTITY) END CANTIDAD," +
						" CASE WHEN SUM(XX_INITIALINVENTORYAMOUNT)IS NULL THEN 0 " +
						" ELSE SUM(XX_INITIALINVENTORYAMOUNT) END MONTO " +
						" FROM XX_VCN_INVENTORY " +
						" WHERE M_PRODUCT_ID IN (" + Product_ID + ")" + 
						" AND XX_INVENTORYMONTH = " + InitialAM.getMonth() +   
			    		" AND XX_INVENTORYYEAR = " + (InitialAM.getYear()+1900) + " ";
			//System.out.println("SQL INVINI: "+SQLInvIni);
			PreparedStatement psQueryIni= null;
			ResultSet rsQueryIni = null;

			BigDecimal cantidadIni = new BigDecimal(0);
			BigDecimal montoIni = new BigDecimal(0);
			try{
				psQueryIni = DB.prepareStatement(SQLInvIni, null);
				rsQueryIni = psQueryIni.executeQuery();
				if(rsQueryIni.next()){	
					cantidadIni = rsQueryIni.getBigDecimal("CANTIDAD");
					montoIni = rsQueryIni.getBigDecimal("MONTO");
				}//if querytotal	
				cant.add(cantidadIni);
				cant.add(montoIni);
			}//try		
			catch(SQLException e){	
				e.getMessage();
			}
			finally {
				DB.closeResultSet(rsQueryIni);
				DB.closeStatement(psQueryIni);	
			}
		}// Inventario inicial
		
		/** Fecha actual esta entre el inicio de la actividad y el final, 
		 * se calcula la cantidad diaria */
		else if(dateActual.after(InitialAM) && dateActual.before(FinalAM)){
			String SQLInvFin = " SELECT "+
							" CASE WHEN SUM(XX_INITIALINVENTORYQUANTITY + XX_SHOPPINGQUANTITY - " +
							" XX_SALESQUANTITY + XX_MOVEMENTQUANTITY + XX_ADJUSTMENTSQUANTITY" +
							" + XX_PREVIOUSADJUSTMENTSQUANTITY) IS NULL THEN 0" +
							" ELSE SUM(XX_INITIALINVENTORYQUANTITY + XX_SHOPPINGQUANTITY - " +
							" XX_SALESQUANTITY + XX_MOVEMENTQUANTITY + XX_ADJUSTMENTSQUANTITY" +
							" + XX_PREVIOUSADJUSTMENTSQUANTITY)" +
							" END CANTIDAD " +
							" FROM XX_VCN_INVENTORY " + 
							" WHERE M_PRODUCT_ID IN (" + Product_ID + ")" +
							" AND XX_INVENTORYMONTH = " + dateActual.getMonth() +   
							" AND XX_INVENTORYYEAR = " + (dateActual.getYear()+1900) +" ";
			//System.out.println("SQL Inv: "+SQLInvFin);
			PreparedStatement psQueryFin = null;
			ResultSet rsQueryFin = null;
			BigDecimal cantidadFin = new BigDecimal(0);
			try{
				psQueryFin = DB.prepareStatement(SQLInvFin, null);
				rsQueryFin = psQueryFin.executeQuery();
				if(rsQueryFin.next()){	
					cantidadFin = rsQueryFin.getBigDecimal("CANTIDAD");
				}//while querytotal
				cant.add(cantidadFin);					
			}//try		
			catch(SQLException e){	
				e.getMessage();
			}
			finally {
				DB.closeResultSet(rsQueryFin);
				DB.closeStatement(psQueryFin);
			}
		}
		
		/** Fecha actual es igual a la fecha de fin de la actividad de 
		 * mercadeo, se calcula la cantidad final */
		else if(dateActual.equals(FinalAM)){
			String SQLInvFin = " SELECT "+
						" CASE WHEN SUM(XX_INITIALINVENTORYQUANTITY + XX_SHOPPINGQUANTITY - " +
						" XX_SALESQUANTITY + XX_MOVEMENTQUANTITY + XX_ADJUSTMENTSQUANTITY" +
						" + XX_PREVIOUSADJUSTMENTSQUANTITY) IS NULL THEN 0" +
						" ELSE SUM(XX_INITIALINVENTORYQUANTITY + XX_SHOPPINGQUANTITY - " +
						" XX_SALESQUANTITY + XX_MOVEMENTQUANTITY + XX_ADJUSTMENTSQUANTITY" +
						" + XX_PREVIOUSADJUSTMENTSQUANTITY)" +
						" END CANTIDAD " +
						" FROM XX_VCN_INVENTORY " + 
						" WHERE M_PRODUCT_ID IN (" + Product_ID + ")" +
						" AND XX_INVENTORYMONTH = " + FinalAM.getMonth() +   
						" AND XX_INVENTORYYEAR = " + (FinalAM.getYear()+1900) +" ";
			//System.out.println("SQL INNFIN: "+SQLInvFin);
			PreparedStatement psQueryFin = null;
			ResultSet rsQueryFin = null;
			BigDecimal cantidadFin = new BigDecimal(0);
			try{
				psQueryFin = DB.prepareStatement(SQLInvFin, null);
				rsQueryFin = psQueryFin.executeQuery();
				if(rsQueryFin.next()){	
					cantidadFin = rsQueryFin.getBigDecimal("CANTIDAD");
				}//while querytotal
				cant.add(cantidadFin);					
			}//try		
			catch(SQLException e){	
				e.getMessage();
			}
			finally {
				DB.closeResultSet(rsQueryFin);
				DB.closeStatement(psQueryFin);
			}
		}// Inventario final
		else {
			cant.add(new BigDecimal(1));
			cant.add(new BigDecimal(1));
		}
		return cant;
	} // obtainQuantities

	/** getInventory 
	 * Se obtiene la cantidad que están en las OC y en M_Storage
	 * @param ProductID Identificador del producto
	 * @param StartDate Fecha de inicio de la actividad de mercadeo
	 * @param EndDate Fecha fin de la actividad de mercadeo
	 * @return quantity */
	public static Integer getInventory(Integer ProductID, Timestamp StartDate, 
			Timestamp EndDate){
		Integer quantity = new Integer(0);
		/*String SQLQuantity = " SELECT SUM(L.QTYORDERED) as CANTIDAD " +
							" FROM C_ORDERLINE L JOIN C_ORDER O " +
							" ON (L.C_ORDER_ID=O.C_ORDER_ID) " +
							" WHERE L.M_PRODUCT_ID = " + ProductID +
							" AND TRUNC(O.XX_ESTIMATEDDATE) >= " + DB.TO_DATE(StartDate) +
							" AND TRUNC(O.XX_ESTIMATEDDATE) <= " + DB.TO_DATE(EndDate) +
							" AND O.ISSOTRX = 'N' AND O.XX_ORDERSTATUS = 'AP' ";
		//System.out.println("SQL Cantidad: "+SQLQuantity);
		
		PreparedStatement psQueryQuantity = null;
		ResultSet rsQueryQuantity = null;
		// Obtener cantidad de ordenes de compra
		try{
			psQueryQuantity = DB.prepareStatement(SQLQuantity, null);
			rsQueryQuantity = psQueryQuantity.executeQuery();
			while(rsQueryQuantity.next()){	
				quantity = rsQueryQuantity.getInt("CANTIDAD");
			}//while querytotal			
		}//try		
		catch(SQLException e){	
			e.getMessage();
			e.printStackTrace();
		}
		finally {
			try { rsQueryQuantity.close(); } 
			catch (SQLException e) {  e.printStackTrace(); }
			try { psQueryQuantity.close(); } 
			catch (SQLException e) { e.printStackTrace(); }	
		}
	*/
		String SQLStorage = " SELECT " +
							" CASE WHEN  SUM(S.QTY) IS NULL THEN 0 " +
							" ELSE SUM(S.QTY) END QTY " +
							" FROM M_STORAGEDETAIL S JOIN M_LOCATOR L " +
							" ON(L.M_LOCATOR_ID = S.M_LOCATOR_ID) " +
							" WHERE S.M_PRODUCT_ID = " + ProductID +
							" AND L.ISDEFAULT = 'Y' " +
							" AND S.QTYTYPE = 'H' ";
		//System.out.println("SQL Storage: "+SQLStorage);
		
		PreparedStatement psQueryStorage = null;
		ResultSet rsQueryStorage = null;

		// Obtener cantidad de Storage
		try{
			psQueryStorage = DB.prepareStatement(SQLStorage, null);
			rsQueryStorage = psQueryStorage.executeQuery();
			if(rsQueryStorage.next()){	
				quantity = quantity + rsQueryStorage.getInt("QTY");
			}//while querytotal			
		}//try		
		catch(SQLException e){	
			e.getMessage();
		}
		finally {
			DB.closeResultSet(rsQueryStorage);
			DB.closeStatement(psQueryStorage);
		}
//		System.out.println("La cantidad es "+quantity);
		return quantity;
	} // getInventory
	
	/**
	 * Calcula el precio Beco al producto
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String priceBeco(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		BigDecimal price = (BigDecimal)mTab.getValue("Price");
		if (!price.equals(new BigDecimal(0))){
			VME_PriceProductCallout priceBecoG = new VME_PriceProductCallout();		
			String priceNew = priceBecoG.priceBecoGlobal(ctx, WindowNo, mTab, mField, value, oldValue, 0, price);
			mTab.setValue("Price", new BigDecimal(priceNew));
		}
		return "";
	} // priceBeco
	
	//TODO Probar y verificar si conviene hacer updates
	/** processSeason
	 * Se activa/desactiva la temporada seleccionada
	 * @param season Identificador de la campaña a activar/desactivar
	 */
	public static void processSeason(Integer season, boolean value, 
			boolean callout){
		X_XX_VMA_Season temporada = 
			new X_XX_VMA_Season(Env.getCtx(), season, null);
		if(!callout){
			temporada.setIsActive(value);
		}
		temporada.setXX_VMA_IsSeasonActive(value);
		temporada.setXX_VMA_NotApproved(value);
		temporada.save();
		
		// Se procede a activar/desactivar las colecciones y campañas asociadas
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQLDeactivate = " SELECT CAM.C_CAMPAIGN_ID CAMID," +
				" COL.XX_VMR_COLLECTION_ID COLID" +
				" FROM C_CAMPAIGN CAM JOIN XX_VMR_COLLECTION COL " +
				" ON (COL.XX_VMA_SEASON_ID = CAM.XX_VMA_SEASON_ID)" +
				" WHERE COL.XX_VMA_SEASON_ID = " + season;
		try{
			pstmt = DB.prepareStatement(SQLDeactivate, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				 processCampaign(rs.getInt("CAMID"), value, callout);
				 processCollection(rs.getInt("COLID"), value);
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		
	} // Fin processSeason
	
	/** processCollection
	 * Se activa/desactiva la coleccion seleccionada
	 * @param collection Identificador de la coleccion a activar/desactivar
	 */
	public static void processCollection(Integer collection, boolean value){
		X_XX_VMR_Collection coleccion = 
			new X_XX_VMR_Collection(Env.getCtx(), collection, null);
		coleccion.setIsActive(value);
		coleccion.save();
		
		// Se procede a activar/desactivar los elementos asociados a la página
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQLDeactivate = " SELECT XX_VMR_PACKAGE_ID ID" +
				" FROM XX_VMR_PACKAGE " +
				" WHERE XX_VMR_COLLECTION_ID = " + collection;
		try{
			pstmt = DB.prepareStatement(SQLDeactivate, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				processPackage(rs.getInt("ID"), value);
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		
	} // Fin processCollection
	
	/** processPackage
	 * Se activa/desactiva el packete seleccionado
	 * @param package Identificador del paquete a activar/desactivar
	 */
	public static void processPackage(Integer pack, boolean value){
		X_XX_VMR_Package paquete = 
			new X_XX_VMR_Package(Env.getCtx(), pack, null);
		paquete.setIsActive(value);
		paquete.save();
	} // Fin processPackage
	
	/** processCampaign
	 * Se activa/desactiva la campaña seleccionada
	 * @param campaign Identificador de la campaña a activar/desactivar
	 */
	public static void processCampaign (Integer campaign, boolean value, 
			boolean callout){
		X_C_Campaign camp = 
			new X_C_Campaign(Env.getCtx(), campaign, null);
		if(!callout){
			camp.setIsActive(value);
		}
		camp.setXX_VMA_IsCampaignActive(value);
		camp.save();
		
		// Se procede a activar/desactivar los elementos asociados a la página
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQLDeactivate = " SELECT XX_VMA_MARKETINGACTIVITY_ID ID" +
				" FROM XX_VMA_MARKETINGACTIVITY " +
				" WHERE C_CAMPAIGN_ID = " + campaign;
		try{
			pstmt = DB.prepareStatement(SQLDeactivate, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				processAM(rs.getInt("ID"), value, callout);
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		
	} // Fin processCampaign
	
	/**processAM
	 * Se activa/desactiva el folleto seleccionado
	 * @param action Identificador del folleto a activar/desactivar
	 */
	public static void processAM (Integer action, boolean value, 
			boolean callout){
		X_XX_VMA_MarketingActivity accion = 
			new X_XX_VMA_MarketingActivity(Env.getCtx(), action, null);
		if(!callout){
			accion.setIsActive(value);
		}
		accion.setXX_VMA_IsActivityActive(value);
		accion.save();
		
		// Se procede a activar/desactivarlos elementos asociados a la página
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQLDeactivate = " SELECT XX_VMA_BROCHUREPAGE_ID ID" +
				" FROM XX_VMA_BROCHUREPAGE " +
				" WHERE XX_VMA_BROCHURE_ID = " + accion.getXX_VMA_Brochure_ID();
		try{
			pstmt = DB.prepareStatement(SQLDeactivate, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				processBrochure(rs.getInt("ID"), value, callout);
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		
	} // Fin processAM
	
	/** processBrochure
	 * Se activa/desactiva el folleto seleccionado
	 * @param brochure Identificador del folleto a activar/desactivar
	 */
	public static void processBrochure (Integer brochure, boolean value, 
			boolean callout){
		X_XX_VMA_Brochure folleto = new X_XX_VMA_Brochure(Env.getCtx(), brochure, null);
		if(!callout) {
			folleto.setIsActive(value);
		}
		folleto.setXX_VMA_IsMAActive(value);
		folleto.setXX_VMA_IsBrochureActive(value);
		folleto.save();
		
		// Se procede a activar/desactivar los elementos asociados a la página
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQLDeactivate = " SELECT XX_VMA_BROCHUREPAGE_ID ID" +
				" FROM XX_VMA_BROCHUREPAGE " +
				" WHERE XX_VMA_BROCHURE_ID = " + brochure;
		try{
			pstmt = DB.prepareStatement(SQLDeactivate, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				processBrochurePage(rs.getInt("ID"), true, callout);
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		
	} // FinprocessBrochure
	
	/** processBrochurePage
	 * Se activa/desactiva la página seleccionada
	 * @param brochurePage Identificador de la página a activar/desactivar
	 */
	public static void processBrochurePage (Integer brochurePage, boolean value, 
			boolean callout){
		X_XX_VMA_BrochurePage pagina = 
			new X_XX_VMA_BrochurePage(Env.getCtx(), brochurePage, null);
		if(!callout) {
			pagina.setIsActive(value);
		}
		pagina.setXX_VMA_IsBrochurePActive(value);
		pagina.save();
		
		// Se procede a activar/desactivar los elementos asociados a la página
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQLDeactivate = " SELECT XX_VME_ELEMENTS_ID ID" +
				" FROM XX_VME_ELEMENTS " +
				" WHERE XX_VMA_BROCHUREPAGE_ID = " + brochurePage;
		try{
			pstmt = DB.prepareStatement(SQLDeactivate, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				processElement(rs.getInt("ID"), value, callout);
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		
	} // Fin processBrochurePage
	
	/** processElement
	 * Se activa/desactiva el elemento seleccionado.
	 * @param element Identificador del elemento a activar/desactivar
	 */
	public static void processElement (Integer element, boolean value, boolean
			callout){
		X_XX_VME_Elements elemento = 
			new X_XX_VME_Elements(Env.getCtx(), element, null);
		if(!callout) {
			elemento.setIsActive(value);
			elemento.save();
		}
		
		// Se procede a activar/desactivar las referencias asociadas al elemento
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQLDeactivate = " SELECT XX_VME_REFERENCE_ID ID" +
				" FROM XX_VME_REFERENCE " +
				" WHERE XX_VME_ELEMENTS_ID = " + element;
		try{
			pstmt = DB.prepareStatement(SQLDeactivate, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				processReference(rs.getInt("ID"), value);
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally

	} // Fin processElement
	
	/** processReference
	 * Se activa/desactiva la referencia seleccionada.
	 * @param reference Identificador de la referencia a activar/desactivar
	 */
	public static void processReference (Integer reference, boolean value){
		X_XX_VME_Reference referencia = 
			new X_XX_VME_Reference(Env.getCtx(), reference, null);
		referencia.setIsActive(value);
		referencia.save();
		
		// Se procede a activar/desactivar los productos asociados a la referencia
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQLDeactivate = " SELECT XX_VME_PRODUCT_ID ID" +
				" FROM XX_VME_PRODUCT " +
				" WHERE XX_VME_REFERENCE_ID = " + reference;
		try{
			pstmt = DB.prepareStatement(SQLDeactivate, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				processProduct(rs.getInt("ID"), value);
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally

	} // Fin processReference
	
	/** processProduct
	 * Se activa/desactiva el producto seleccionado.
	 * @param product Identificador del producto a activar/desactivar
	 */
	public static void processProduct (Integer product, boolean value){
		X_XX_VME_Product producto = 
			new X_XX_VME_Product(Env.getCtx(), product, null);
		producto.setIsActive(value);
		producto.save();
	} // Fin processProduct
	
	/** obtainQtyCD
	  * Se coloca la cantidad de piezas en CD del producto
	  * @param productID Id del producto
	  * @return Qty cantidad en CD del producto 
	*/
	public static Integer obtainQtyCD(Integer product_ID){
		Integer Qty = new Integer(0);
		String SQLCD = "SELECT " +
					" CASE WHEN  SUM(S.QTY) IS NULL THEN 0 " +
					" ELSE SUM(S.QTY) END QTY " +
					" FROM M_STORAGEDETAIL S JOIN M_LOCATOR L " +
					" ON(L.M_LOCATOR_ID = S.M_LOCATOR_ID) " +
					" WHERE S.M_PRODUCT_ID = "+ product_ID +
					" AND L.ISDEFAULT = 'Y' " +
					" AND S.QTYTYPE = 'H' " +
					" AND L.M_WAREHOUSE_ID = " + 1000053;
					//Integer.parseInt(Env.getCtx().getContext("#XX_L_WAREHOUSECENTRODIST_ID"));
		//System.out.println("SQL CD: "+SQLCD);
		PreparedStatement psQueryCD= null;
		ResultSet rsQueryCD = null;
		try{
			psQueryCD = DB.prepareStatement(SQLCD, null);
			rsQueryCD = psQueryCD.executeQuery();
			if(rsQueryCD.next()){
				Qty = rsQueryCD.getInt("QTY");
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rsQueryCD);
			DB.closeStatement(psQueryCD);
		}// finally
		return Qty;
	} // obtainQtyCD

	
	/** obtainAM
	 * Se obtiene la acción de mercadeo asociada a un folleto en específico
	 * @param brochure Identificador del folleto
	 * @return action Identificador de la acción
	 * */
	public static int obtainAM(int brochure){
		int action = 0;
		String SQLAction = " SELECT XX_VMA_MARKETINGACTIVITY_ID ID " +
				" FROM XX_VMA_MARKETINGACTIVITY " +
				"WHERE XX_VMA_BROCHURE_ID = "+ brochure;
		//System.out.println("SQLAction: "+SQLAction);
		
		PreparedStatement psAction = null;
		ResultSet rsAction = null;
		try{
			psAction = DB.prepareStatement(SQLAction, null);
			rsAction = psAction.executeQuery();
			if(rsAction.next()){
				action = rsAction.getInt("ID");
			}
		}
		catch(Exception e){ e.printStackTrace(); }
		finally{
			DB.closeResultSet(rsAction);
			DB.closeStatement(psAction);
		}
		
		return action;
	} // Fin obtainAM
	
	/** obtainQtyReference
	 * Obtiene el número de referencias asociadas a un elemento
	 * @param elem Elemento al que se le desea calcular la cantidad de referecias
	 * @param manual Si las referencias a ser contadas son las que tienen el campo
	 * de manual en False/True dependiendo de lo que indique el usuario
	 * @return qty Número de referencias asociadas, sumatoria de cantidades de 
	 * las referencias
	 * */
	public static Vector<BigDecimal> obtainQtyReference(X_XX_VME_Elements elem, boolean manual){
		Vector<BigDecimal> qty = new Vector<BigDecimal>();
		String SQLQty = "";
		
		if(manual){
			SQLQty = " SELECT COUNT(*) SUMA," +
					" CASE WHEN SUM(XX_VME_INDEPABISQTY) IS NULL THEN 0 " +
					" ELSE SUM(XX_VME_INDEPABISQTY) END QTY " +
					" FROM XX_VME_REFERENCE " +
					" WHERE XX_VME_ELEMENTS_ID = " + elem.get_ID() +
					" AND XX_VME_MANUAL = 'Y'";
			//System.out.println("SQLQty: "+ SQLQty);
		}
		else {
			SQLQty = " SELECT COUNT(*) SUMA, " +
					" CASE WHEN SUM(XX_VME_INDEPABISQTY) IS NULL THEN 0 " +
					" ELSE SUM(XX_VME_INDEPABISQTY) END QTY " +
					" FROM XX_VME_REFERENCE " +
					" WHERE XX_VME_ELEMENTS_ID = " + elem.get_ID()+
					" AND XX_VME_MANUAL = 'N'";
			//System.out.println("SQLQty: "+ SQLQty);
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			pstmt = DB.prepareStatement(SQLQty, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				qty.add(rs.getBigDecimal("SUMA"));
				qty.add(rs.getBigDecimal("QTY"));
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		
		return qty;
	} // Fin obtainQtyReference

	/** obtainQtyProduct
	 * Obtiene el número de productos asociados a una referencia
	 * @param ref Referencia al que se le desea calcular la cantidad de productos
	 * @param manual Indica si el producto tiene la cantidad modificada manualmente 
	 * o no
	 * @return Número de productos asociados, qty suma de cantidades de productos 
	 * asociados
	 * */
	public static Vector<BigDecimal> obtainQtyProduct(int ref, boolean manual){
		Vector<BigDecimal> qty = new Vector<BigDecimal>();
		String SQLQty = "";
		if(manual){
			SQLQty = " SELECT COUNT(*) SUMA, " +
					" CASE WHEN SUM(XX_VME_INDEPABISQTY) IS NULL" +
					" THEN 0 ELSE SUM(XX_VME_INDEPABISQTY) END QTY " +
					" FROM XX_VME_PRODUCT " +
					" WHERE XX_VME_REFERENCE_ID = " + ref+
					" AND XX_VME_MANUAL = 'Y'";
			//System.out.println("SQLQty: "+ SQLQty);
		} // if true
		else {
			SQLQty = " SELECT COUNT(*) SUMA, " +
					" CASE WHEN SUM(XX_VME_INDEPABISQTY) IS NULL" +
					" THEN 0 ELSE SUM(XX_VME_INDEPABISQTY) END QTY " +
					" FROM XX_VME_PRODUCT " +
					" WHERE XX_VME_REFERENCE_ID = " + ref+
					" AND XX_VME_MANUAL = 'N'";
			//System.out.println("SQLQty: "+ SQLQty);
		} // else false

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		qty.clear();
		try{
			pstmt = DB.prepareStatement(SQLQty, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				qty.add(rs.getBigDecimal("SUMA"));
				qty.add(rs.getBigDecimal("QTY"));
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		
		return qty;
	} // Fin obtainQtyProduct
	
	/** createElemRef
	 * Crea las referencias al elemento seleccionado
	 * @param element Elemento al que se le asociarán las referencias
	 * @param action Identificador de la actividad de mercadeo relacionada
	 * @param cantIndep Cantidad indepabis asociada a la referencia
	 * @param referenceID Identificador de la referencia
	 * @param Category_ID Identificador de la categoría
	 * @param type Tipo de referencia a ser creada
	 * @param manual Determina si se modificará manualmente la cantidad
	 * @param mantener Determina si se mantienen los productos
	 * @param prodID Identificador del producto
	 * @param manualProd Determina si se modificará manualmente la cantidad
	 * del producto
	 * */
	public static void createElemRef(X_XX_VME_Elements element, X_XX_VMA_MarketingActivity action, 
			BigDecimal cantIndep, int referenceID, int Category_ID, String type, 
			boolean manual, boolean mantener, int prodID, boolean manualProd){
		BigDecimal qty = new BigDecimal(0);
		String SQLProd = "";
		String relBrand = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement psBrand = null;
		ResultSet rsBrand = null;
		int id = 0;
		
		String SQLBrand = " SELECT NAME " +
				" FROM XX_VMR_BRAND " +
				" WHERE XX_VMR_BRAND_ID IN (" +
				" 	SELECT XX_VMR_BRAND_ID " +
				"	FROM XX_VMR_VENDORPRODREF " +
				"	WHERE XX_VMR_VENDORPRODREF_ID = " + referenceID +
				")";
		try{
			psBrand = DB.prepareStatement(SQLBrand, null);
			rsBrand = psBrand.executeQuery();
			while(rsBrand.next()){
				relBrand = rsBrand.getString("NAME");
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rsBrand);
			DB.closeStatement(psBrand);
		}// finally
		
		// Se aumenta el contador de las referencias asociadas al elemento
		//X_XX_VME_Elements elem = new X_XX_VME_Elements(Env.getCtx(), element, null);
		element.setXX_VME_QtyRefAssociated(element.getXX_VME_QtyRefAssociated().add(new BigDecimal(1)));

		// Se toma el campo de Relación entre marcas del elemento para 
		// agregar la nueva marca
		if(element.getXX_VME_RelBrand().length() > 0 && !element.getXX_VME_RelBrand().contains(relBrand)) {
			if(element.getXX_VME_RelBrand().equals("N")){
				element.setXX_VME_RelBrand(relBrand);
			}
			else {
				element.setXX_VME_RelBrand(element.getXX_VME_RelBrand()+", "+relBrand);
			}
		}
		// Se guardan los cambios en el elemento	
		element.save();
		
		// Se obtienen los datos de la referencia de proveedor
		X_XX_VMR_VendorProdRef prodRef = new X_XX_VMR_VendorProdRef(Env.getCtx(), referenceID, null);
		X_XX_VME_Reference reference = new X_XX_VME_Reference(Env.getCtx(), 0, null);
		reference.setXX_VME_Elements_ID(element.get_ID());
		reference.setAD_Client_ID(element.getAD_Client_ID());
		reference.setAD_Org_ID(element.getAD_Org_ID());
		reference.setName(prodRef.getName());
		reference.setDescription(prodRef.getDescription());
		reference.setXX_VMR_Category_ID(Category_ID);
		reference.setXX_VMR_Department_ID(prodRef.getXX_VMR_Department_ID());
		reference.setXX_VMR_Line_ID(prodRef.getXX_VMR_Line_ID());
		reference.setXX_VMR_Section_ID(prodRef.getXX_VMR_Section_ID());
		reference.setXX_VMR_VendorProdRef_ID(referenceID);
		reference.setXX_VMR_Brand_ID(prodRef.getXX_VMR_Brand_ID());
		reference.setXX_VME_IndepabisQty(cantIndep);
		reference.setXX_VME_Mantain(mantener);
		reference.setXX_VME_Manual(manual);
		reference.save();

		// Se agrega la referencia y todos los productos asociados a ella
/*		if (type.equals("R")){
			// Asociar los productos a la referencia
			associateProdToRef(reference, referenceID, action, manualProd);
			redefineQuantities(element);
		} // referencia
		else if(type.equals("P")){
			// Se asocia el producto solicitado a la referencia
			associateProductNew(prodID, reference, action, cantIndep, manualProd);
		} // producto
		*/
	} // Fin createElemRef
	
	/** associateProdToRef
	 * Asocia productos a la referencia seleccionada
	 * @param reference Referencia a la que se le asociarán los productos
	 * @param action Identificador de la actividad de mercadeo relacionada
	 * @param elementID Identificador del elemento
	 * @param Determina si la cantidad es manual o no
	 * */
	public static void associateProdToRef(X_XX_VME_Reference reference, int vendor, 
			X_XX_VMA_MarketingActivity action, boolean manual){
		String CategoryTax = "";
		int[] updateCounts;
		Vector<Integer> prodIds = new Vector<Integer>();
		
		String SQLPROD = " SELECT M_PRODUCT_ID ID, " +
				" C_TaxCategory_ID TAX " +
				" FROM M_PRODUCT P " +
				" WHERE ISACTIVE = 'Y' " +
				" AND XX_VMR_VENDORPRODREF_ID = " + vendor;
		//System.out.println("SQLQty: "+ SQLPROD);

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			pstmt = DB.prepareStatement(SQLPROD, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				prodIds.add(rs.getInt("ID"));
				CategoryTax = rs.getString("TAX");
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		
		// Se recorre el vector con los ids de productos para asociarlos 
		for(int i = 0; i < prodIds.size(); i++){
			//MProduct product = new MProduct(Env.getCtx(), prodIds.get(i), null);
			associateProduct(prodIds.get(i), reference, action, new BigDecimal(0), manual);
		} // for
		
	} // Fin associateProdToRef
	
	/** calculateQuantities
	 * Algoritmo que permite calcular las cantidades a asignar a las referencias y 
	 * productos asociados a un elemento, partiendo de la cantidad indicada a nivel
	 * de elemento o del parámetro en modificar cantidades en el proceso de agregar
	 * referencia a elemento
	 * @param int elementID Identificador del elementos
	 * @param idProdRef Identificador del elemento o referencia
	 * @param type Tipo que define si es un producto o referencia
	 * @param qtyNew Cantidades nuevas en el caso de agregar referencia 
	 * @return quantities Cantidades a ser asignadas (cantidad y el restante)
	 * */
	public static Vector<BigDecimal> calculateQuantities(X_XX_VME_Elements elementID, Integer 
			idProdRef, String type, BigDecimal qtyNew){
		Vector<BigDecimal> quantities = new Vector<BigDecimal>();
		Vector<BigDecimal> qtyObtained = new Vector<BigDecimal>();
		BigDecimal qtyTotal = new BigDecimal(0);
		BigDecimal qtyResta = new BigDecimal(0);
		BigDecimal qtyDividir = new BigDecimal(0);
		BigDecimal qtyAsignar = new BigDecimal(0);
		BigDecimal qtyResto = new BigDecimal(0);
		BigDecimal qtyAsigWResto = new BigDecimal(0);
		
		// El proceso se realiza a nivel de referencias
		if(type.equals("R")){
			//X_XX_VME_Elements element = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
			
			// Cantidad a publicar del elemento
			qtyTotal = elementID.getXX_VME_QTYPUBLISHED();
			
			// Obtener el número de referencias y la sumatoria de sus cantidades
			// asociadas al elemento que fueron modificadas manualmente y no pueden
			// ser modificadas, dejando el restante para ser asignadas 
			qtyObtained = obtainQtyReference(elementID, true);
			
			// Número total de referencias asociadas al elemento
			//numDiv = qtyObtained.get(0);
			
			// Suma de las Cantidades de las referencias
			qtyResta = qtyObtained.get(1);
			
			qtyDividir = qtyTotal.subtract(qtyResta);
		} //if reference
		// El proceso se realiza a nivel de productos
		else if(type.equals("P")){
			X_XX_VME_Product product = new X_XX_VME_Product(Env.getCtx(),idProdRef, null);
			X_XX_VME_Reference reference = 
				new X_XX_VME_Reference(Env.getCtx(), product.getXX_VME_Reference_ID(), null);
			
			// Cantidad indepabis de la referencia asociada
			qtyTotal = reference.getXX_VME_IndepabisQty();
			
			// Obtener el número de productos asociados a la referencia y la sumatoria
			// que fueron modificadas manualmente y no pueden ser tomadas en cuenta para 
			// la redistribucion
			qtyObtained = obtainQtyProduct(reference.get_ID(), true);
	
			// Suma de las Cantidades de las referencias
			qtyResta = qtyObtained.get(1);
			
			qtyDividir = qtyTotal.subtract(qtyResta);
		} // else product
		else if(type.equals("S")){
			//X_XX_VME_Elements element = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
			
			// Cantidad a publicar del elemento
			qtyTotal = elementID.getXX_VME_QTYPUBLISHED();
			
			// Cantidad disponible para ser asignada
			Vector<BigDecimal> availableQty =  getQtyAvailable(elementID);
			qtyDividir = availableQty.get(0); 
			qtyResta = qtyNew.add(elementID.getXX_VME_QtyRefAssociated().subtract(availableQty.get(1)));
		}
		
		/* Cantidad a publicar menos la cantidad de las referencias que son 
		 * manual = 'T'
		 */
		
		// Cantidad indepabis para las referencias
		if(qtyNew.equals(new BigDecimal(0))){
			if(type.equals("S")){
				qtyAsignar = qtyDividir.divide(qtyResta, BigDecimal.ROUND_HALF_DOWN);	
			}
			else {
				qtyAsignar = qtyDividir;
			}
		} // qtyNew
		else {
			if(type.equals("S")){
				qtyAsignar = qtyDividir.divide(qtyResta, BigDecimal.ROUND_HALF_DOWN);
			}
			else{
				qtyAsignar = qtyDividir.divide(qtyNew, BigDecimal.ROUND_HALF_DOWN);
			}
		} // else qtynew
		if(type.equals("S")){
			qtyResto = qtyDividir.subtract(qtyResta.multiply(qtyAsignar));
		}
		else {
			qtyResto = qtyDividir.subtract(qtyNew.multiply(qtyAsignar));
		}
		
		if(!qtyResto.equals(new BigDecimal(0)))
			qtyAsigWResto = qtyAsignar.add(qtyResto);
		else 
			qtyAsigWResto = qtyResto;

		quantities.add(qtyAsignar);
		quantities.add(qtyAsigWResto);
		
		return quantities;
		
	}// Fin calculateQuantities
	
	/** obtainReference
	 * Obtiene la referencia asociada a un elemento
	 * @param referenceId Identificador de la referencia
	 * @param elementId Identificador del elemento
	 * @return referenceID Identificador de la referencia ya existente en el elemento
	 * */
	public static int obtainReference(Integer referenceID, X_XX_VME_Elements elementID){
		int elemReference = 0;
		String SQLRef = " SELECT  XX_VME_REFERENCE_ID REFID " +
				" FROM XX_VME_REFERENCE  " +
				" WHERE XX_VMR_VENDORPRODREF_ID = " + referenceID +
				" AND XX_VME_ELEMENTS_ID = " + elementID.get_ID();
		//System.out.println("SQLQty: "+ SQLRef);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(SQLRef, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				elemReference = rs.getInt("REFID");
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
		return elemReference;
	} // Fin obtainReferences
	
	/** associateProduct
	 * Asocia un producto a la referencia seleccionada
	 * @param productID Identificador del producto a ser asociado
	 * @param referenceID Referencia a la que se asocian los productos
	 * @param action Identificador de la actividad de mercadeo relacionada
	 * @param cantIndep Cantidad indepabis a ser configurada para el producto
	 * @param manual Determina si será manual
	 * */
	public static void associateProduct(Integer productID, X_XX_VME_Reference referenceID, 
			X_XX_VMA_MarketingActivity action, BigDecimal cantIndep, boolean manual){
		BigDecimal priceActemp = new BigDecimal(0);
		BigDecimal salePrice = new BigDecimal(0);
		MProduct prod = new MProduct(Env.getCtx(), productID, null);
		
		// Se verifica si existe el precio actual para agregar o no el elemento
		salePrice  = setPriceBPVendorR(prod.getM_Product_ID(), prod.getXX_VMR_VendorProdRef_ID());
		if(!salePrice.equals(new BigDecimal(0))){
			X_XX_VME_Product productoNuevo = new X_XX_VME_Product(Env.getCtx(), 0, null);
			if(prod.getM_AttributeSetInstance_ID() != 0){
				MAttributeSetInstance atribSet = new MAttributeSetInstance(Env.getCtx(), prod.getM_AttributeSetInstance_ID(), null);
				productoNuevo.setDescription(prod.getName()+" "+atribSet.getDescription());	
			}
			else{
				productoNuevo.setDescription(prod.getName());
			}
			productoNuevo.setXX_VME_Reference_ID(referenceID.get_ID());
			productoNuevo.setName(prod.getName());
			productoNuevo.setXX_VMR_Category_ID(prod.getXX_VMR_Category_ID());
			productoNuevo.setXX_VMR_Department_ID(prod.getXX_VMR_Department_ID());
			productoNuevo.setXX_VMR_Line_ID(prod.getXX_VMR_Line_ID());
			productoNuevo.setXX_VMR_Section_ID(prod.getXX_VMR_Section_ID());
			productoNuevo.setXX_VMR_VendorProdRef_ID(prod.getXX_VMR_VendorProdRef_ID());
			productoNuevo.setM_Product_ID(prod.getM_Product_ID());
			productoNuevo.setXX_VMR_Brand_ID(prod.getXX_VMR_Brand_ID());
			productoNuevo.setC_BPartner_ID(prod.getC_BPartner_ID());
			productoNuevo.setC_Country_ID(setCountry(productoNuevo.getC_BPartner_ID()));
			productoNuevo.setXX_VME_QtyCD(new BigDecimal(obtainQtyCD(prod.getM_Product_ID())));
			productoNuevo.setXX_VME_Manual(manual);
			// Se coloca la cantidad que hay en inventario
			//qtyavailable = setInventory(product.get_ID(),(Integer)values.get("mes"), (Integer)values.get("ano"));
			Integer qtyavailable = getInventory(prod.get_ID(),action.getStartDate(), action.getEndDate());
			productoNuevo.setXX_VME_InventoryQty(new BigDecimal(qtyavailable));
			
			// Se busca y coloca el precio promocional del producto.
			BigDecimal finalprice = ((promotionalPriceFinder(salePrice,
					prod.getM_Product_ID(), prod.getXX_VMR_Category_ID(),
					prod.getXX_VMR_Department_ID(),prod.getXX_VMR_Line_ID(),
					prod.getXX_VMR_Section_ID(), prod.getXX_VMR_Brand_ID(), 
					action.getStartDate(), action.getEndDate()))
						.multiply(setIVA(prod.getC_TaxCategory_ID())))
						.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			// Se calcula el precio actual, si es cero se coloca el del elemento
			/*if(salePrice.equals(new BigDecimal(0))){
				X_XX_VME_Elements elemento = new X_XX_VME_Elements(Env.getCtx(), referenceID.getXX_VME_Elements_ID(), null);
				priceActemp = elemento.getXX_VME_DynamicPrice();
			}
			else {*/
				priceActemp = salePrice.multiply(setIVA(prod.getC_TaxCategory_ID()))
					.setScale(2, BigDecimal.ROUND_HALF_UP);	
			//}
			
			productoNuevo.setXX_VME_ActualPrice(priceActemp);
			productoNuevo.setXX_VME_PromotionalPrice(finalprice);
	
			// Se configura la cantidad indepabis
			if(!cantIndep.equals(new BigDecimal(0))){
				productoNuevo.setXX_VME_IndepabisQty(cantIndep);
			}
			productoNuevo.save();
		}
		
	} // Fin associateProduct
	
	/** redefineQuantities
	 * Redefine las cantidades a ser asociadas a las referencias y sus productos
	 * de un elemento determinado
	 * @param elementID Identificador del elemento 
	 * */
	public static void redefineQuantities(X_XX_VME_Elements elementID){
		Vector<Integer> references = new Vector<Integer>();
		Vector<Integer> manuales = new Vector<Integer>();
		Integer asignar = new Integer(0);
		Integer numberRefs = new Integer(0);
		Integer qtyAsignar = new Integer(0);
		Integer qtyResto = new Integer(0);
		String SQLRefs = "";
		String SQLManuales = "";
		String sqlUpdate = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
//		PreparedStatement pstmtQty = null;
		//X_XX_VME_Elements element = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
		
		// Se obtiene la cantidad disponible a ser distribuida y cantidad de 
		// referencias manuales para restarla a la total del elemento
		asignar = getQtyAvailable(elementID).get(0).intValueExact();
//		System.out.println(elementID.getXX_VME_QtyRefAssociated().intValueExact());
//		System.out.println(getQtyAvailable(elementID).get(1).intValueExact());
		 
		
		numberRefs = elementID.getXX_VME_QtyRefAssociated().intValueExact() - 
							getQtyAvailable(elementID).get(1).intValueExact();

		// Cantidad indepabis para las referencias		
		if(numberRefs != 0){
			qtyAsignar = asignar / numberRefs;
			qtyResto = asignar - (numberRefs * qtyAsignar);
		}
		else{
			qtyAsignar = asignar;
		}
			
		// Obtener las referencias asociadas al elemento
		SQLRefs = " SELECT XX_VME_REFERENCE_ID REFID" +
				" FROM XX_VME_REFERENCE " +
				" WHERE XX_VME_MANUAL = 'N' " +
				" AND XX_VME_ELEMENTS_ID = " + elementID.get_ID();
		//System.out.println("SQLRefs: "+SQLRefs);
		try{
			pstmt = DB.prepareStatement(SQLRefs, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				references.add(rs.getInt("REFID"));
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
			
		// Asignar la cantidad indepabis a cada referencia asociada al producto
		for(int i =0; i < references.size(); i++){
//			X_XX_VME_Reference ref = new X_XX_VME_Reference(Env.getCtx(), references.get(i), null);
			if(qtyResto == 0) {
//				ref.setXX_VME_IndepabisQty(new BigDecimal(qtyAsignar));
				sqlUpdate = " UPDATE XX_VME_REFERENCE SET XX_VME_INDEPABISQTY = " + qtyAsignar +
							" WHERE XX_VME_REFERENCE_ID = " + references.get(i) ;
				DB.executeUpdate(null, sqlUpdate); 
				redefineQtyProd(references.get(i), new BigDecimal(qtyAsignar),false);
			}	
			else {
				if(i == references.size()-1 && qtyResto > 0){
//					ref.setXX_VME_IndepabisQty(new BigDecimal(qtyAsignar+qtyResto));
					sqlUpdate = " UPDATE XX_VME_REFERENCE SET XX_VME_INDEPABISQTY = " + (qtyAsignar+qtyResto) +
								" WHERE XX_VME_REFERENCE_ID = " + references.get(i) ;
					DB.executeUpdate(null, sqlUpdate); 
					redefineQtyProd(references.get(i), new BigDecimal(qtyAsignar+qtyResto),false);
				}
				else {
//					ref.setXX_VME_IndepabisQty(new BigDecimal(qtyAsignar+1));
					sqlUpdate = " UPDATE XX_VME_REFERENCE SET XX_VME_INDEPABISQTY = " + (qtyAsignar+1) +
								" WHERE XX_VME_REFERENCE_ID = " + references.get(i) ;
					DB.executeUpdate(null, sqlUpdate); 
					qtyResto = qtyResto -1;
					redefineQtyProd(references.get(i), new BigDecimal(qtyAsignar+1),false);
				}
			}
			
//			ref.save();
		} // for references
		
		// Se redistribuye entre las manuales para el caso de agregar referencias
		// con cantidades manuales desde un principio
		// Obtener las referencias asociadas al elemento
		pstmt = null;
		rs = null;
		SQLManuales = " SELECT XX_VME_REFERENCE_ID REFID" +
				" FROM XX_VME_REFERENCE " +
				" WHERE XX_VME_MANUAL = 'Y' " +
				" AND XX_VME_ELEMENTS_ID = " + elementID.get_ID();
		//System.out.println("SQLManuales: "+SQLManuales);
		try{
			pstmt = DB.prepareStatement(SQLManuales, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				manuales.add(rs.getInt("REFID"));
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}// finally
			
		// Redistribuye a nivel de producto
		for(int j = 0; j < manuales.size(); j++){
			X_XX_VME_Reference ref = 
				new X_XX_VME_Reference(Env.getCtx(), manuales.get(j), null);
			redefineQtyProd(ref.get_ID(), ref.getXX_VME_IndepabisQty(), true);
		} // for references
		
	} // redefineQuantities

	/** getQtyAvailable
	 * Obtiene la cantidad disponible a ser asignada de un elemento determinado
	 * @param elementID Identificador del elemento 
	 * @return Cantidad para ser asignada y número de referencias manuales
	 * */
	public static Vector<BigDecimal> getQtyAvailable(X_XX_VME_Elements elementID){
		BigDecimal qtyRefAss = new BigDecimal(0);
		Vector<BigDecimal> available = new Vector<BigDecimal>();
		BigDecimal manuales = obtainQtyReference(elementID, true).get(1);
		PreparedStatement ps = null;
		ResultSet rs = null;
//		X_XX_VME_Elements element = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
		qtyRefAss = elementID.getXX_VME_QtyRefAssociated();
		
		// No se tienen referencias asociadas, se coloca como disponible
		// el total de la cantidad a publicar del elemento
		if(qtyRefAss.equals(new BigDecimal(0))){
			available.add(elementID.getXX_VME_QTYPUBLISHED());
			available.add(new BigDecimal(0));
		}
		// Hay referencias asociadas al elemento, se calculan las disponibles
		else {
			available.add(elementID.getXX_VME_QTYPUBLISHED().
							subtract(manuales));
			available.add(obtainQtyReference(elementID, true).get(0));
		}

		return available;
	} // getQtyAvailable
	
	/** redefineQtyProd
	 * Redefine las cantidades de los productos de una referencia actualizada
	 * @param refID Identificador de la referencia 
	 * @param cantIndep Cantidad indepabis actualizada de la referencia
	 * */
	public static void redefineQtyProd(Integer refID, BigDecimal cantIndep, 
			boolean manual){
		Vector<BigDecimal> qtyManual = new Vector<BigDecimal>();
		Vector<BigDecimal> qtyNoManual = new Vector<BigDecimal>();
		Vector<Integer> prods = new Vector<Integer>();
		String SQLProd = "";
		Integer qtyTotal = new Integer(0);
		Integer qtyAsignar = new Integer(0);
		Integer qtyDividir = new Integer(0);
		Integer qtyResto = new Integer(0);
		Integer qty = new Integer(0);
		
		// Cantidad y sumatoria de manuales
		qtyManual = obtainQtyProduct(refID, true);

		// Cantidad y sumatoria de no manuales
		qtyNoManual = obtainQtyProduct(refID, false);
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// calcular la cantidad a ser redistribuida entre los productos no manuales
		qtyDividir = cantIndep.intValueExact() - (qtyManual.get(1)).intValueExact();
		if(qtyNoManual.get(0).intValueExact() != 0) {
			qtyAsignar = qtyDividir/qtyNoManual.get(0).intValueExact();
		}
		else {
			qtyAsignar = qtyDividir;
		}
		qtyResto = qtyDividir - (qtyNoManual.get(0).intValueExact()* qtyAsignar);
		
		if(manual){
			SQLProd = " SELECT XX_VME_PRODUCT_ID ID " +
					" FROM XX_VME_PRODUCT P INNER JOIN XX_VME_REFERENCE R " +
					" ON (P.XX_VME_REFERENCE_ID = R.XX_VME_REFERENCE_ID) " +
					" WHERE P.XX_VME_REFERENCE_ID = " + refID +
					" AND P.XX_VME_MANUAL = 'N' " +
					" AND R.XX_VME_MANUAL = 'Y' ";
		}
		else {
			SQLProd = " SELECT XX_VME_PRODUCT_ID ID " +
					" FROM XX_VME_PRODUCT P INNER JOIN XX_VME_REFERENCE R " +
					" ON (P.XX_VME_REFERENCE_ID = R.XX_VME_REFERENCE_ID) " +
					" WHERE P.XX_VME_REFERENCE_ID = " + refID +
					" AND P.XX_VME_MANUAL = 'N' " +
					" AND R.XX_VME_MANUAL = 'N' ";
		}
		//System.out.println("SQLProd: "+SQLProd);
		try{
			ps = DB.prepareStatement(SQLProd, null);
			rs = ps.executeQuery();
			while(rs.next()){
				prods.add(rs.getInt("ID"));
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}// finally
		
		ps = null;
		rs = null;
		
		// Si hay productos
		if(prods.size() > 0) {
			for(int i = 0; i < prods.size(); i++){
				if(qtyResto == 0){
					qty = qtyAsignar;
				}
				else {
					// Si se llega al final y hay resto, asignar todo al último
					if(i == prods.size()-1 && qtyResto > 0) {
						qty = qtyAsignar + qtyResto;	
					}
					else {
						qty = qtyAsignar + 1;
						qtyResto = qtyResto - 1;	
					}
				}
/*				X_XX_VME_Product product = new X_XX_VME_Product(Env.getCtx(), prods.get(i), null);
				product.setXX_VME_IndepabisQty(new BigDecimal(qty));
				System.out.println("antes: "+product.getXX_VME_IndepabisQty());
				product.save();
				System.out.println("despues: "+product.getXX_VME_IndepabisQty());*/
				String sqlUpdate = " UPDATE XX_VME_PRODUCT " +
						" SET XX_VME_INDEPABISQTY = " + qty +
						" WHERE XX_VME_PRODUCT_ID = " + prods.get(i) ;
				//System.out.println("Sqlupdte:"+sqlUpdate);
				DB.executeUpdate(null, sqlUpdate);
			} // for
		} // if prods
	} // getQtyAvailable
	
	/** getQtyRefAvailable
	 * Obtiene la cantidad disponible a ser asignada de una referencia determinada
	 * @param referenceID Identificador de la referncia 
	 * @return Cantidad disponible para ser asignada 
	 * */
	public static BigDecimal getQtyRefAvailable(Integer referenceID){
		BigDecimal qtyReference = new BigDecimal(0);
		BigDecimal available = new BigDecimal(0);
		X_XX_VME_Reference reference = new X_XX_VME_Reference(Env.getCtx(), referenceID, null);
		
		qtyReference = reference.getXX_VME_IndepabisQty();
		available = qtyReference.subtract(obtainQtyProduct(referenceID, true).get(1));
		
		return available;
	} // getQtyRefAvailable
	
	/** createElemRef
	 * Crea las referencias al elemento seleccionado
	 * @param element Elemento al que se le asociarán las referencias
	 * @param action Identificador de la actividad de mercadeo relacionada
	 * @param cantIndep Cantidad indepabis asociada a la referencia
	 * @param referenceID Identificador de la referencia
	 * @param Category_ID Identificador de la categoría
	 * @param type Tipo de referencia a ser creada
	 * @param manual Determina si se modificará manualmente la cantidad
	 * @param mantener Determina si se mantienen los productos
	 * @param orderid Identificador de la OC
	 * @param pedidoid Identificador del pedido
	 * del producto
	 * */
	public static void createElemRef(X_XX_VME_Elements element, X_XX_VMA_MarketingActivity action, 
			BigDecimal cantIndep, int referenceID, int Category_ID, String type, 
			boolean manual, boolean mantener, int orderid, int pedidoid){
		BigDecimal qty = new BigDecimal(0);
		String relBrand = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement psBrand = null;
		ResultSet rsBrand = null;
		int id = 0;
		
		String SQLBrand = " SELECT NAME " +
				" FROM XX_VMR_BRAND " +
				" WHERE XX_VMR_BRAND_ID IN (" +
				" 	SELECT XX_VMR_BRAND_ID " +
				"	FROM XX_VMR_VENDORPRODREF " +
				"	WHERE XX_VMR_VENDORPRODREF_ID = " + referenceID +
				")";
		try{
			psBrand = DB.prepareStatement(SQLBrand, null);
			rsBrand = psBrand.executeQuery();
			while(rsBrand.next()){
				relBrand = rsBrand.getString("NAME");
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rsBrand);
			DB.closeStatement(psBrand);
		}// finally
		
		// Se aumenta el contador de las referencias asociadas al elemento
		//X_XX_VME_Elements elem = new X_XX_VME_Elements(Env.getCtx(), element, null);
		element.setXX_VME_QtyRefAssociated(element.getXX_VME_QtyRefAssociated().add(new BigDecimal(1)));

		// Se toma el campo de Relación entre marcas del elemento para 
		// agregar la nueva marca
		if(element.getXX_VME_RelBrand().length() > 0 && !element.getXX_VME_RelBrand().contains(relBrand)) {
			if(element.getXX_VME_RelBrand().equals("N")){
				element.setXX_VME_RelBrand(relBrand);
			}
			else {
				element.setXX_VME_RelBrand(element.getXX_VME_RelBrand()+", "+relBrand);
			}
		}
		// Se guardan los cambios en el elemento	
		element.save();
		
		// Se obtienen los datos de la referencia de proveedor
		X_XX_VMR_VendorProdRef prodRef = new X_XX_VMR_VendorProdRef(Env.getCtx(), referenceID, null);
		X_XX_VME_Reference reference = new X_XX_VME_Reference(Env.getCtx(), 0, null);
		reference.setXX_VME_Elements_ID(element.get_ID());
		reference.setAD_Client_ID(element.getAD_Client_ID());
		reference.setAD_Org_ID(element.getAD_Org_ID());
		reference.setName(prodRef.getName());
		reference.setDescription(prodRef.getDescription());
		reference.setXX_VMR_Category_ID(Category_ID);
		reference.setXX_VMR_Department_ID(prodRef.getXX_VMR_Department_ID());
		reference.setXX_VMR_Line_ID(prodRef.getXX_VMR_Line_ID());
		reference.setXX_VMR_Section_ID(prodRef.getXX_VMR_Section_ID());
		reference.setXX_VMR_VendorProdRef_ID(referenceID);
		reference.setXX_VMR_Brand_ID(prodRef.getXX_VMR_Brand_ID());
		reference.setXX_VME_IndepabisQty(cantIndep);
		reference.setXX_VME_Mantain(mantener);
		reference.setXX_VME_Manual(manual);
		reference.setC_Order_ID(orderid);
		reference.setXX_VMR_Order_ID(pedidoid);
		reference.save();
		
	} // Fin createElemRef
	
	/** verifyProductInRef
	 * Se verifica que el producto que se quiere agregar no exista dentro
	 * de la referencia.
	 * @param prodID 		identificador del product
	 * @param refD 		identificador de la referencia
	 * @return boolean 	true si el producto existe, false si no.	
	 */	
	public static boolean verifyProductInRef (int prodID, int refID) {
		String sql = "";
		boolean result = false;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
	
		sql = " SELECT * " +
				" FROM XX_VME_REFERENCE R INNER JOIN XX_VME_PRODUCT P " +
				" ON (R.XX_VME_REFERENCE_ID = P.XX_VME_REFERENCE_ID) " +
				" WHERE R.XX_VME_REFERENCE_ID = " + refID +
				" AND P.M_PRODUCT_ID = " + prodID;
		//System.out.println(sql);
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
		
			if(rs.next()){
				result = true;
			}
		} // try
		catch (Exception e){ e.printStackTrace(); }
		finally {
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return result;
	} // Fin verifyProductInBrochure
	
	/** updateActualPrice
	 * Actualiza el precio actual de los productos de un determinado folleto
	 * @param brochureID Identificador del folleto a actualizar
	 * */
	public static void updateActualPrice(Integer brochureID){
		BigDecimal precioActual = new BigDecimal(0);
		String sql = " SELECT P.XX_VME_PRODUCT_ID " +
					" FROM XX_VMA_MarketingActivity M INNER JOIN XX_VMA_Brochure B " +
					" on (M.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
					" INNER JOIN XX_VMA_BROCHUREPAGE BP " +
					" on (BP.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
					" INNER JOIN XX_VME_ELEMENTS E " +
					" ON (E.XX_VMA_BROCHUREPAGE_ID = BP.XX_VMA_BROCHUREPAGE_ID) " +
					" INNER JOIN XX_VME_REFERENCE R " +
					" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
					" INNER JOIN XX_VME_PRODUCT P " +
					" ON (R.XX_VME_REFERENCE_ID = P.XX_VME_REFERENCE_ID) " +
					" WHERE  (E.XX_VME_TYPE = 'P' OR E.XX_VME_TYPE = 'M') " +
					" AND BP.XX_VMA_BROCHURE_ID = " + brochureID;
		//System.out.println("sql: "+sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			try {
				while(rs.next()){
					X_XX_VME_Product product = 
						new X_XX_VME_Product(Env.getCtx(),rs.getInt(1),null);								
					precioActual =  setPriceBPVendorR(product.getM_Product_ID(), 
										product.getXX_VMR_VendorProdRef_ID());
					product.setXX_VME_ActualPrice(precioActual);
					product.save();
				} // while rs
			} // try
			catch(Exception e){ e.printStackTrace(); }
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		} // try
		catch(SQLException e){
			e.printStackTrace();
		}
		finally {
			try { rs.close(); } 
			catch (SQLException e) { e.printStackTrace(); }
			try { pstmt.close(); } 
			catch (SQLException e) { e.printStackTrace(); }
		}
		//System.out.println("actual");
	} // updateActualPrice
	
	/** updateInventory
	 * Obtiene la cantidad disponible de inventario de acuerdo a los pedidos 
	 * realizados durante la actividad de mercadeo
	 * @param brochure Identificador del folleto
	 * ²param activity actividad de mercadeo
	 * */
	public static void updateInventory(Integer brochure, X_XX_VMA_MarketingActivity activity){
		Calendar newDate = Calendar.getInstance();
		Date aux = newDate.getTime();
		Timestamp dateActual = new Timestamp(aux.getTime());
		Vector<Integer> products = new Vector<Integer>();
		Vector<Integer> prods = new Vector<Integer>();
		ResultSet rsQueryResults = null; 
		ResultSet rsQueryPedido = null;
		PreparedStatement psQueryResults = null;
		PreparedStatement psQueryPedido = null;
//		PreparedStatement pstmtProds = null;
		
		String sqlElements = " SELECT XX_VME_Product_ID Prod, " +
					" P.M_Product_ID Producto " +
					" FROM XX_VMA_MarketingActivity M " +
					" INNER JOIN XX_VMA_Brochure B on (M.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
					" INNER JOIN XX_VMA_BrochurePage BP on (BP.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
					" INNER JOIN XX_VME_Elements E on (E.XX_VMA_BrochurePage_ID = BP.XX_VMA_BrochurePage_ID) " +
					" INNER JOIN XX_VME_Reference R on (R.XX_VME_Elements_ID = E.XX_VME_Elements_ID) " +
					" INNER JOIN XX_VME_Product P on (R.XX_VME_Reference_ID = P.XX_VME_Reference_ID) " +
					" WHERE B.XX_VMA_Brochure_ID = " + brochure +
					" AND E.ISACTIVE = 'Y' ";
//		System.out.println("SQLElements: " + sqlElements);
		
		try{
			psQueryResults = DB.prepareStatement(sqlElements, null);
			rsQueryResults = psQueryResults.executeQuery();
			while(rsQueryResults.next()){	
					products.add(rsQueryResults.getInt("Producto"));
					prods.add(rsQueryResults.getInt("Prod"));
			}//WHILE		
		}//try		
		catch(SQLException e){ e.printStackTrace(); }	
		finally {
			DB.closeResultSet(rsQueryResults);
			DB.closeStatement(psQueryResults);
		}
		
		// Para todos los productos
		for(int i = 0; i < products.size(); i++){
			// Se busca en los pedidos que estén en tránsito
			String SQLPedidos = " SELECT CASE WHEN SUM(DP.XX_PRODUCTQUANTITY) IS NULL THEN 0" +
					" ELSE SUM(DP.XX_PRODUCTQUANTITY) END QTY" +
					" FROM XX_VMR_ORDER P INNER JOIN XX_VMR_ORDERREQUESTDETAIL DP " +
					" ON (P.XX_VMR_ORDER_ID = DP.XX_VMR_ORDER_ID)" +
					" WHERE DP.M_PRODUCT_ID = " + products.get(i) +
					" AND P.XX_ORDERREQUESTSTATUS = 'TI' " + 
					" AND TRUNC(P.XX_DATESTATUSONSTORE) = TRUNC(" + DB.TO_DATE(dateActual) +") ";
			 
			try{
				psQueryPedido = DB.prepareStatement(SQLPedidos, null);
				rsQueryPedido = psQueryPedido.executeQuery();
				while(rsQueryPedido.next()){	
					X_XX_VME_Product product = new X_XX_VME_Product(Env.getCtx(), prods.get(i), null);
					if(product.getXX_VME_InvUpdate().compareTo(new BigDecimal(0)) == 0){
						String update = " UPDATE XX_VME_PRODUCT SET XX_VME_InvUpdate = " + rsQueryPedido.getBigDecimal("QTY") +
										" WHERE XX_VME_PRODUCT_ID = " + product.get_ID();
						int updated = DB.executeUpdateEx(update, null);
					}
					else{
						String update = " UPDATE XX_VME_PRODUCT SET XX_VME_InvUpdate = " +
										product.getXX_VME_InvUpdate().add(rsQueryPedido.getBigDecimal("QTY")) +
										" WHERE XX_VME_PRODUCT_ID = " + product.get_ID();
						int updated = DB.executeUpdateEx(update, null);
					}
				}//WHILE		
			}//try		
			catch(SQLException e){ e.printStackTrace(); }	
			finally {
				DB.closeResultSet(rsQueryPedido);
				DB.closeStatement(psQueryPedido);
			}
		} // for products
		
		// Se ejecutan los updates asociados al batch
//		try { pstmtProds.addBatch();} 
//		catch (SQLException e1) {e1.printStackTrace();} 
		
	} // updateInventory
	
	
	/** associateProduct
	 * Asocia un producto a la referencia seleccionada
	 * @param productID Identificador del producto a ser asociado
	 * @param referenceID Referencia a la que se asocian los productos
	 * @param action Identificador de la actividad de mercadeo relacionada
	 * @param cantIndep Cantidad indepabis a ser configurada para el producto
	 * @param manual Determina si será manual
	 * */
	/*public static void associateProductNew(Integer productID, X_XX_VME_Reference referenceID, 
			X_XX_VMA_MarketingActivity action, BigDecimal cantIndep, boolean manual, Trx trx){
		String sqlUpdate = "";
		X_XX_VME_Product productoNuevo = new X_XX_VME_Product(Env.getCtx(), 0, null);
		productoNuevo.setXX_VME_IndepabisQty(cantIndep);
		productoNuevo.setM_Product_ID(productID);
		productoNuevo.setXX_VME_Reference_ID(referenceID.get_ID());
		productoNuevo.save();
		
		sqlUpdate = " UPDATE XX_VME_PRODUCT (NAME, DESCRIPTION, XX_VMR_CATEGORY_ID, " +
				" XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID, XX_VMR_VENDORPRODREF_ID, XX_VMR_BRAND_ID, " +
				" M_PRODUCT_ID, C_BPARTNER_ID, C_COUNTRY_ID, XX_VME_QTYCD, XX_VME_INDEPABISQTY, " +
				" XX_VME_REFERENCE_ID, XX_VME_MANUAL) = ( " +
				" SELECT "+ productoNuevo.get_ID() + "," + productoNuevo.getValue() + ", " +
				" P.NAME, P.NAME, P.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID, " +
				" P.XX_VMR_VENDORPRODREF_ID, P.XX_VMR_BRAND_ID, P.M_PRODUCT_ID, P.C_BPARTNER_ID, " +
				" (SELECT C.C_Country_ID FROM C_BPartner_location BP " +
				" INNER JOIN C_Location L ON (BP.C_Location_ID = L.C_Location_ID) " +
				" INNER JOIN C_Country C ON (L.C_Country_ID = C.C_Country_ID) " +
				" WHERE BP.C_Bpartner_ID = P.C_BPARTNER_ID ), " +
				" (SELECT CASE WHEN  SUM(S.QTY) IS NULL THEN 0 ELSE SUM(S.QTY) END QTY " + 
				" FROM M_STORAGEDETAIL S JOIN M_LOCATOR L ON(L.M_LOCATOR_ID = S.M_LOCATOR_ID) " +
				" WHERE S.M_PRODUCT_ID = P.M_PRODUCT_ID AND L.ISDEFAULT = 'Y' AND S.QTYTYPE = 'H' " +
				" AND L.M_WAREHOUSE_ID = 1000053) QTYCD, " + cantIndep + "'N' " +
				" FROM M_PRODUCT P INNER JOIN XX_VMR_VENDORPRODREF VPR ON (P.XX_VMR_VENDORPRODREF_ID = VPR.XX_VMR_VENDORPRODREF_ID) " +
				" WHERE P.M_PRODUCT_ID = " + productID +")" +
				" WHERE XX_VME_PRODUCT_ID = " + productoNuevo.get_ID();
		
		PreparedStatement pstmt = DB.prepareStatement(sqlUpdate, trx);
		try { pstmt.addBatch();} 
		catch (SQLException e1) {e1.printStackTrace();} 
	} // Fin associateProduct
	*/
	/** createElemRef
	 * Crea las referencias al elemento seleccionado
	 * @param element Elemento al que se le asociarán las referencias
	 * @param action Identificador de la actividad de mercadeo relacionada
	 * @param cantIndep Cantidad indepabis asociada a la referencia
	 * @param referenceID Identificador de la referencia
	 * @param Category_ID Identificador de la categoría
	 * @param type Tipo de referencia a ser creada
	 * @param manual Determina si se modificará manualmente la cantidad
	 * @param mantener Determina si se mantienen los productos
	 * @param prodID Identificador del producto
	 * @param manualProd Determina si se modificará manualmente la cantidad
	 * del producto
	 * 
	 * */
	public static void createElemRefNew(X_XX_VME_Elements element, X_XX_VMA_MarketingActivity action, 
			Vector<BigDecimal> cantIndep, Vector<Integer> referenceID, String type, 
			Vector<Boolean> manualT, Vector<Boolean> mantenerT, Vector<Integer> prodID, Vector<Boolean> manualProdT,
			Vector<Integer> orders, Vector<Integer> pedidos) {
		int limiteInf = 0;
		int limiteSup = 0;
		BigDecimal numbloques = new BigDecimal(0);
		BigDecimal tambloques = new BigDecimal(0);
	
		// Si la cantidad de referencias es mayor a 1000, se debe dividir esa cantidad 
		// de manera que permita hacer el insert masivo en bloques menores a 1000
		if(referenceID.size() > 1000) {
			numbloques = new BigDecimal(referenceID.size()).divide(new BigDecimal(1000), RoundingMode.CEILING);
			tambloques = new BigDecimal(referenceID.size()).divide(numbloques, RoundingMode.CEILING);
			
			// Se calculan los bloques de inserts en referencia y producto
			for(int bloque = 1; bloque <= numbloques.intValue(); bloque++){
				if(bloque != numbloques.intValue()){
					limiteSup = limiteInf + tambloques.intValue();	
				}
				else{
					limiteSup = referenceID.size();
				}
//				System.out.println("Bloque: "+ bloque + " LimInf: "+limiteInf+" LimSup: "+limiteSup);
				insertRefAndProd(limiteInf, limiteSup, element, action, cantIndep,
						referenceID, type, manualT, mantenerT, prodID, manualProdT, orders, pedidos);
				limiteInf = limiteInf + tambloques.intValue();
			}
		}
		else {
			limiteInf = 0;
			limiteSup = referenceID.size();
//			System.out.println("LimInf: "+limiteInf+" LimSup: "+limiteSup);
			insertRefAndProd(limiteInf, limiteSup, element, action, cantIndep,
					referenceID, type, manualT, mantenerT, prodID, manualProdT, orders, pedidos);
		}
			
	} // Fin createElemRef
	

	/** findIDProduct
	 * Funcion auxiliar que permite determinar el id maximo para poder ser asignado
	 * en el insert de productos el valor siguiente 
	 * ²return IDMax Máximo id de productos */	
	public static Integer findIDProduct() {
		String ultimoIdProduct= " SELECT MAX(XX_VME_PRODUCT_ID) FROM XX_VME_PRODUCT";
		PreparedStatement ultDetProm = DB.prepareStatement(ultimoIdProduct, null);
		ResultSet maximo = null;
		Integer idProduct = 0;
		try {
			maximo = ultDetProm.executeQuery();
			while (maximo.next()) {
				idProduct = maximo.getInt(1); 
			}
		} 
		catch (SQLException e1) { e1.printStackTrace(); }
		finally{
			DB.closeResultSet(maximo);
			DB.closeStatement(ultDetProm);
		}
		return idProduct;
	} // findIDProduct

	/** findIDReference
	 * Funcion auxiliar que permite determinar el id maximo para poder ser asignado
	 * en el insert de referencias el valor siguiente 
	 * return IDMax Máximo id de productos*/	
	public static Integer findIDReference() {
		String ultimoIdRef = " SELECT MAX(XX_VME_REFERENCE_ID) FROM XX_VME_REFERENCE";
		PreparedStatement ultDetProm = DB.prepareStatement(ultimoIdRef, null);
		ResultSet maximo = null;
		Integer idReference = 0;
		try {
			maximo = ultDetProm.executeQuery();
			while (maximo.next()) {
				idReference = maximo.getInt(1); 
			}
		} 
		catch (SQLException e1) { e1.printStackTrace(); }
		finally{
			DB.closeResultSet(maximo);
			DB.closeStatement(ultDetProm);
		}
		return idReference;
	} // findIDProduct

	
	/** insertRefAndProd
	 * */
	public static void insertRefAndProd(int limiteInf, int limiteSup, X_XX_VME_Elements element, 
			X_XX_VMA_MarketingActivity action, Vector<BigDecimal> cantIndep, Vector<Integer> referenceID, 
			String type, Vector<Boolean> manualT, Vector<Boolean> mantenerT, 
			Vector<Integer> prodID, Vector<Boolean> manualProdT, Vector<Integer> orders, Vector<Integer> pedidos) {
		String refs = "";
		String refManual = "";
		String refMantener = "";
		String refManualMantener = "";
		String refTotal = "";
		String  sqlUpdateQty = "";
		String products = "";
		String sqlUpdateRef = "";
		String sqlUpdateProd = "";
		boolean updated = false;
		boolean execBatch = false;
		boolean isManualNoMantain = false;
		boolean IsMantainNoManual = false;
		boolean isManualMantain = false;
		boolean IsNoManualNoMantain = false;
		Statement pstmtRefs = DB.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, null);
		Statement pstmtProds = DB.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, null);
		Statement pstmtUpdateRefs = DB.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, null);

		// Se obtienen las combianciones de mantener y manual para las referencias
		for(int ref = limiteInf; ref < limiteSup; ref++){
			if(type.equals("P") || type.equals("A")){
				products += prodID.get(ref) + ", ";	
				if (referenceID.get(ref) != 0)
					refs += referenceID.get(ref) + ", ";
			}
			else {
				// Sólo se agregan las referencias distintas de cero
				if (referenceID.get(ref) != 0) {
					if(manualT.get(ref) && !mantenerT.get(ref)){
						refManual += referenceID.get(ref) + ", ";
					}
					if(manualT.get(ref) && mantenerT.get(ref)){
						refManualMantener += referenceID.get(ref) + ", ";
					}
					if(!manualT.get(ref) && !mantenerT.get(ref)){
						refs += referenceID.get(ref) + ", ";
					}
					if(!manualT.get(ref) && mantenerT.get(ref)){
						refMantener += referenceID.get(ref) + ", ";
					}
				} // ref != 0	
			}
		}
		
		if(type.equals("P") || type.equals("A")){
			if(products.length() > 0 )
				products = products.substring(0, products.length()-2);
//			System.out.println("Productos a ser agregados al folleto");
//			refs = refs.substring(0, refs.length()-2);
			refTotal += refs;
		}
		else {
			if(refs.length() > 7){
				refTotal += refs;
				refs = refs.substring(0, refs.length()-2);
				IsNoManualNoMantain = true;
			}
			if(refMantener.length() > 7){	
				IsMantainNoManual = true;
				refTotal += refMantener;
				refMantener = refMantener.substring(0, refMantener.length()-2);
			}
			if(refManual.length() > 7){
				isManualNoMantain = true;
				refTotal += refManual;
				refManual = refManual.substring(0, refManual.length()-2);	
			}
			if(refManualMantener.length() > 7){
				isManualMantain = true;
				refTotal += refManualMantener;
				refManualMantener = refManualMantener.substring(0, refManualMantener.length()-2);
			}	
		}
	
		// Total de referencias
		if(refTotal.length() > 7){
			refTotal = refTotal.substring(0, refTotal.length()-2);
		}
		
		if(isManualMantain || isManualNoMantain) {
			// Solo manuales
			if(isManualNoMantain){
				sqlUpdateRef = " INSERT INTO XX_VME_REFERENCE (XX_VME_REFERENCE_ID, AD_CLIENT_ID, AD_ORG_ID, " +
					" CREATED, CREATEDBY, ISACTIVE, UPDATED, UPDATEDBY, VALUE, NAME, DESCRIPTION, " +
					" XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID, " +
					" XX_VMR_BRAND_ID, XX_VME_MANTAIN, XX_VME_MANUAL, XX_VME_ELEMENTS_ID, XX_VMR_VENDORPRODREF_ID) " +
					" ( SELECT " + findIDReference() + " + rownum, " + Env.getCtx().getAD_Client_ID() + 
					", " + Env.getCtx().getAD_Org_ID()+"," + " SYSDATE, " + Env.getCtx().getAD_User_ID() + 
					", " + "'Y', " + " SYSDATE, " + Env.getCtx().getAD_User_ID() + ", " + findIDReference() + " + rownum, " +
					" VPR.NAME, VPR.NAME, D.XX_VMR_CATEGORY_ID, VPR.XX_VMR_DEPARTMENT_ID, VPR.XX_VMR_LINE_ID, " +
					" VPR.XX_VMR_SECTION_ID, VPR.XX_VMR_BRAND_ID, 'N', 'Y', " +
					element.get_ID() + ", VPR.XX_VMR_VENDORPRODREF_ID " +
					" FROM XX_VMR_VENDORPRODREF VPR " +
					" INNER JOIN XX_VMR_DEPARTMENT D ON (VPR.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID)" +
					" WHERE VPR.XX_VMR_VENDORPRODREF_ID IN ( " + refManual +") AND" +
					" VPR.XX_VMR_VENDORPRODREF_ID NOT IN " +
					" (SELECT R.XX_VMR_VENDORPRODREF_ID  " +
					" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
					" ON (R.XX_VME_ELEMENTS_ID = E.XX_VME_ELEMENTS_ID) " +
					" WHERE E.XX_VME_ELEMENTS_ID = "+ element.get_ID() +"))";
//				System.out.println(sqlUpdateRef);
				DB.executeUpdate(null, sqlUpdateRef);
			}
			
			// Manual y Mantener
			if(isManualMantain){
				sqlUpdateRef = " INSERT INTO XX_VME_REFERENCE (XX_VME_REFERENCE_ID, AD_CLIENT_ID, AD_ORG_ID, " +
					" CREATED, CREATEDBY, ISACTIVE, UPDATED, UPDATEDBY, VALUE, NAME, DESCRIPTION, " +
					" XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID, " +
					" XX_VMR_BRAND_ID, XX_VME_MANTAIN, XX_VME_MANUAL, XX_VME_ELEMENTS_ID, XX_VMR_VENDORPRODREF_ID) " +
					" (SELECT " + findIDReference() + " + rownum, " + Env.getCtx().getAD_Client_ID() + 
					", " + Env.getCtx().getAD_Org_ID()+"," + " SYSDATE, " + Env.getCtx().getAD_User_ID() + 
					", " + "'Y', " + " SYSDATE, " + Env.getCtx().getAD_User_ID() + ", " + findIDReference() + " + rownum, " +
					" VPR.NAME, VPR.NAME, D.XX_VMR_CATEGORY_ID, VPR.XX_VMR_DEPARTMENT_ID, VPR.XX_VMR_LINE_ID, " +
					" VPR.XX_VMR_SECTION_ID, VPR.XX_VMR_BRAND_ID, 'Y', 'Y', " +
					element.get_ID() + ", VPR.XX_VMR_VENDORPRODREF_ID " +
					" FROM XX_VMR_VENDORPRODREF VPR " +
					" INNER JOIN XX_VMR_DEPARTMENT D ON (VPR.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID)" +
					" WHERE VPR.XX_VMR_VENDORPRODREF_ID IN ( " + refManualMantener +") AND" +
					" VPR.XX_VMR_VENDORPRODREF_ID NOT IN " +
					" (SELECT R.XX_VMR_VENDORPRODREF_ID  " +
					" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
					" ON (R.XX_VME_ELEMENTS_ID = E.XX_VME_ELEMENTS_ID) " +
					" WHERE E.XX_VME_ELEMENTS_ID = "+ element.get_ID() +"))";
//				System.out.println(sqlUpdateRef);
				DB.executeUpdate(null, sqlUpdateRef);
		}
			
		} // cantManuales
		
		// Ni Manual, Ni Mantener
		if(IsNoManualNoMantain){
			sqlUpdateRef = " INSERT INTO XX_VME_REFERENCE (XX_VME_REFERENCE_ID, AD_CLIENT_ID, AD_ORG_ID, " +
				" CREATED, CREATEDBY, ISACTIVE, UPDATED, UPDATEDBY, VALUE, NAME, DESCRIPTION, " +
				" XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID, " +
				" XX_VMR_BRAND_ID, XX_VME_MANTAIN, XX_VME_MANUAL, XX_VME_ELEMENTS_ID, " +
				" XX_VMR_VENDORPRODREF_ID)  ( " +
				" SELECT " + findIDReference() + " + rownum, " + Env.getCtx().getAD_Client_ID() + 
				", " + Env.getCtx().getAD_Org_ID()+"," + " SYSDATE, " + Env.getCtx().getAD_User_ID() + 
				", " + "'Y', " + " SYSDATE, " + Env.getCtx().getAD_User_ID() + ", " + findIDReference() + " + rownum, " +
				" VPR.NAME, VPR.NAME, D.XX_VMR_CATEGORY_ID, VPR.XX_VMR_DEPARTMENT_ID, VPR.XX_VMR_LINE_ID, " +
				" VPR.XX_VMR_SECTION_ID, VPR.XX_VMR_BRAND_ID, 'N', 'N', " + 
				element.get_ID() + ", VPR.XX_VMR_VENDORPRODREF_ID " +
				" FROM XX_VMR_VENDORPRODREF VPR " +
				" INNER JOIN XX_VMR_DEPARTMENT D ON (VPR.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID)" +
				" WHERE VPR.XX_VMR_VENDORPRODREF_ID IN ( " + refs +") AND" +
				" VPR.XX_VMR_VENDORPRODREF_ID NOT IN " +
				" (SELECT R.XX_VMR_VENDORPRODREF_ID  " +
				" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
				" ON (R.XX_VME_ELEMENTS_ID = E.XX_VME_ELEMENTS_ID) " +
				" WHERE E.XX_VME_ELEMENTS_ID = "+ element.get_ID() +"))";
//			System.out.println(sqlUpdateRef);
			DB.executeUpdate(null, sqlUpdateRef);
		}
		
		// Sólo Mantener
		if(IsMantainNoManual){
			sqlUpdateRef = " INSERT INTO XX_VME_REFERENCE (XX_VME_REFERENCE_ID, AD_CLIENT_ID, AD_ORG_ID, " +
				" CREATED, CREATEDBY, ISACTIVE, UPDATED, UPDATEDBY, VALUE, NAME, DESCRIPTION, " +
				" XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID, " +
				" XX_VMR_BRAND_ID, XX_VME_MANTAIN, XX_VME_MANUAL, XX_VME_ELEMENTS_ID, " +
				" XX_VMR_VENDORPRODREF_ID)  ( " +
				" SELECT " + findIDReference() + " + rownum, " + Env.getCtx().getAD_Client_ID() + 
				", " + Env.getCtx().getAD_Org_ID()+"," + " SYSDATE, " + Env.getCtx().getAD_User_ID() + 
				", " + "'Y', " + " SYSDATE, " + Env.getCtx().getAD_User_ID() + ", " + findIDReference() + " + rownum, " +
				" VPR.NAME, VPR.NAME, D.XX_VMR_CATEGORY_ID, VPR.XX_VMR_DEPARTMENT_ID, VPR.XX_VMR_LINE_ID, " +
				" VPR.XX_VMR_SECTION_ID, VPR.XX_VMR_BRAND_ID, 'Y', 'N', " + 
				element.get_ID() + ", VPR.XX_VMR_VENDORPRODREF_ID " +
				" FROM XX_VMR_VENDORPRODREF VPR " +
				" INNER JOIN XX_VMR_DEPARTMENT D ON (VPR.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID)" +
				" WHERE VPR.XX_VMR_VENDORPRODREF_ID IN ( " + refMantener +") AND" +
				" VPR.XX_VMR_VENDORPRODREF_ID NOT IN " +
				" (SELECT R.XX_VMR_VENDORPRODREF_ID  " +
				" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
				" ON (R.XX_VME_ELEMENTS_ID = E.XX_VME_ELEMENTS_ID) " +
				" WHERE E.XX_VME_ELEMENTS_ID = "+ element.get_ID() +"))";
//			System.out.println(sqlUpdateRef);
			DB.executeUpdate(null, sqlUpdateRef);
		}
		
		// Es por productos
		if(prodID.size() > 0 && prodID.get(0) != 0 && type.equals("P")){
			sqlUpdateRef = " INSERT INTO XX_VME_REFERENCE (XX_VME_REFERENCE_ID, AD_CLIENT_ID, AD_ORG_ID, " +
				" CREATED, CREATEDBY, ISACTIVE, UPDATED, UPDATEDBY, VALUE, NAME, DESCRIPTION, " +
				" XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID, " +
				" XX_VMR_BRAND_ID, XX_VME_MANTAIN, XX_VME_MANUAL, XX_VME_ELEMENTS_ID, " +
				" XX_VMR_VENDORPRODREF_ID)  ( " +
				" SELECT " + findIDReference() + " + rownum, " + Env.getCtx().getAD_Client_ID() + 
				", " + Env.getCtx().getAD_Org_ID()+"," + " SYSDATE, " + Env.getCtx().getAD_User_ID() + 
				", " + "'Y', " + " SYSDATE, " + Env.getCtx().getAD_User_ID() + ", " + findIDReference() + " + rownum, " +
				" VPR.NAME, VPR.NAME, D.XX_VMR_CATEGORY_ID, VPR.XX_VMR_DEPARTMENT_ID, VPR.XX_VMR_LINE_ID, " +
				" VPR.XX_VMR_SECTION_ID, VPR.XX_VMR_BRAND_ID, 'N', 'N', " + 
				element.get_ID() + ", VPR.XX_VMR_VENDORPRODREF_ID " +
				" FROM XX_VMR_VENDORPRODREF VPR " +
				" INNER JOIN XX_VMR_DEPARTMENT D ON (VPR.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID)" +
				" WHERE VPR.XX_VMR_VENDORPRODREF_ID IN ( " + refTotal +") AND" +
				" VPR.XX_VMR_VENDORPRODREF_ID NOT IN " +
				" (SELECT R.XX_VMR_VENDORPRODREF_ID  " +
				" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
				" ON (R.XX_VME_ELEMENTS_ID = E.XX_VME_ELEMENTS_ID) " +
				" WHERE E.XX_VME_ELEMENTS_ID = "+ element.get_ID() +"))";
//			System.out.println(sqlUpdateRef);
			
			DB.executeUpdate(null, sqlUpdateRef);
		}
		
		// Se ejecuta el batch
//		try { 
//			int[] updCnt = pstmtRefs.executeBatch(); 
////			System.out.println("Total de referencias insertadas: "+ updCnt[0]);
//			pstmtRefs.close();
//		} 
//		catch (SQLException e) { e.printStackTrace(); }
		
		if(type.equals("R")){
			// Asociar productos a las referncias
			sqlUpdateProd = " INSERT INTO XX_VME_PRODUCT (XX_VME_PRODUCT_ID, AD_CLIENT_ID, AD_ORG_ID, " +
				" CREATED, CREATEDBY, ISACTIVE, UPDATED, UPDATEDBY, VALUE, NAME, DESCRIPTION, XX_VMR_CATEGORY_ID, " +
				" XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID, XX_VMR_VENDORPRODREF_ID, XX_VMR_BRAND_ID, " +
				" M_PRODUCT_ID, C_BPARTNER_ID, C_COUNTRY_ID, XX_VME_QTYCD, XX_VME_REFERENCE_ID) " +
				" ( SELECT " + findIDProduct() + " + rownum, " + Env.getCtx().getAD_Client_ID() + 
				", " + Env.getCtx().getAD_Org_ID()+"," + " SYSDATE, " + Env.getCtx().getAD_User_ID() + 
				", " + "'Y', " + " SYSDATE, " + Env.getCtx().getAD_User_ID() + ", " + findIDProduct() + " + rownum, " +
				" P.NAME, P.NAME, P.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID, " +
				" P.XX_VMR_VENDORPRODREF_ID, P.XX_VMR_BRAND_ID, P.M_PRODUCT_ID, P.C_BPARTNER_ID, " +
				" (SELECT C.C_Country_ID FROM C_BPartner_location BP " +
				" INNER JOIN C_Location L ON (BP.C_Location_ID = L.C_Location_ID) " +
				" INNER JOIN C_Country C ON (L.C_Country_ID = C.C_Country_ID) " +
				" WHERE BP.C_Bpartner_ID = P.C_BPARTNER_ID ), " +
				" (SELECT CASE WHEN  SUM(S.QTY) IS NULL THEN 0 ELSE SUM(S.QTY) END QTY " + 
				" FROM M_STORAGEDETAIL S JOIN M_LOCATOR L ON(L.M_LOCATOR_ID = S.M_LOCATOR_ID) " +
				" WHERE S.M_PRODUCT_ID = P.M_PRODUCT_ID AND L.ISDEFAULT = 'Y' AND S.QTYTYPE = 'H' " +
				" AND L.M_WAREHOUSE_ID = 1000053) QTYCD, " +
				" (SELECT XX_VME_REFERENCE_ID FROM XX_VME_REFERENCE WHERE XX_VME_ELEMENTS_ID = "+ element.get_ID()+
				" AND XX_VMR_VENDORPRODREF_ID = VPR.XX_VMR_VENDORPRODREF_ID)" + 
				" FROM M_PRODUCT P INNER JOIN XX_VMR_VENDORPRODREF VPR " +
				" ON (P.XX_VMR_VENDORPRODREF_ID = VPR.XX_VMR_VENDORPRODREF_ID) " +
				" WHERE VPR.XX_VMR_VENDORPRODREF_ID IN (" + refTotal + "))";
//				System.out.println(sqlUpdateProd);
				try { pstmtProds.addBatch(sqlUpdateProd); } 
				catch (SQLException e) { e.printStackTrace(); }
		} // referencias
		if(type.equals("P") || type.equals("A")){
			// Se asocia el producto solicitado a la referencia
			sqlUpdateProd = " INSERT INTO XX_VME_PRODUCT (XX_VME_PRODUCT_ID, AD_CLIENT_ID, AD_ORG_ID, " +
				" CREATED, CREATEDBY, ISACTIVE, UPDATED, UPDATEDBY, VALUE, NAME, DESCRIPTION, XX_VMR_CATEGORY_ID, " +
				" XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID, XX_VMR_VENDORPRODREF_ID, XX_VMR_BRAND_ID, " +
				" M_PRODUCT_ID, C_BPARTNER_ID, C_COUNTRY_ID, XX_VME_QTYCD, XX_VME_REFERENCE_ID) ( " +
				" SELECT " + findIDProduct() + " + rownum, " + Env.getCtx().getAD_Client_ID() + 
				", " + Env.getCtx().getAD_Org_ID()+"," + " SYSDATE, " + Env.getCtx().getAD_User_ID() + 
				", " + "'Y', " + " SYSDATE, " + Env.getCtx().getAD_User_ID() + ", " + findIDProduct() + " + rownum, " +
				" P.NAME, P.NAME, P.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID, " +
				" P.XX_VMR_VENDORPRODREF_ID, P.XX_VMR_BRAND_ID, P.M_PRODUCT_ID, P.C_BPARTNER_ID, " +
				" (SELECT C.C_Country_ID FROM C_BPartner_location BP " +
				" INNER JOIN C_Location L ON (BP.C_Location_ID = L.C_Location_ID) " +
				" INNER JOIN C_Country C ON (L.C_Country_ID = C.C_Country_ID) " +
				" WHERE BP.C_Bpartner_ID = P.C_BPARTNER_ID ), " +
				" (SELECT CASE WHEN  SUM(S.QTY) IS NULL THEN 0 ELSE SUM(S.QTY) END QTY " + 
				" FROM M_STORAGEDETAIL S JOIN M_LOCATOR L ON(L.M_LOCATOR_ID = S.M_LOCATOR_ID) " +
				" WHERE S.M_PRODUCT_ID = P.M_PRODUCT_ID AND L.ISDEFAULT = 'Y' AND S.QTYTYPE = 'H' " +
				" AND L.M_WAREHOUSE_ID = 1000053) QTYCD, " +
				" (SELECT XX_VME_REFERENCE_ID FROM XX_VME_REFERENCE WHERE XX_VME_ELEMENTS_ID = "+ element.get_ID()+
				" AND XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID)" +
				" FROM M_PRODUCT P INNER JOIN XX_VMR_VENDORPRODREF VPR " +
				" ON (P.XX_VMR_VENDORPRODREF_ID = VPR.XX_VMR_VENDORPRODREF_ID) " +
				" WHERE P.M_PRODUCT_ID IN (" + products +") AND P.M_PRODUCT_ID NOT IN " +
				" ( SELECT M_PRODUCT_ID FROM XX_VME_PRODUCT WHERE XX_VME_REFERENCE_ID IN " +
				" (SELECT XX_VME_REFERENCE_ID " +
				" FROM XX_VME_REFERENCE " +
				" WHERE XX_VME_ELEMENTS_ID = "+ element.get_ID()+
				" AND XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID)" +
				" ))";
//			System.out.println(sqlUpdateProd);
			try { pstmtProds.addBatch(sqlUpdateProd); } 
			catch (SQLException e) { e.printStackTrace(); }
		} // productos
		
		// Se ejecuta el batch
		try { 
			int[] updCnt = pstmtProds.executeBatch();
			pstmtProds.close();
		} 
		catch (SQLException e) { e.printStackTrace(); }
	
		// Si vienen de OC o pedido
		for(int manual = 0; manual < referenceID.size(); manual++){
			sqlUpdateQty = " UPDATE XX_VME_REFERENCE SET ";
			if(orders.get(manual) != 0 ) {
				updated = true;
				sqlUpdateQty += " C_ORDER_ID = " + orders.get(manual);
			}
			if(pedidos.get(manual) != 0){
				updated = true;
				sqlUpdateQty += " XX_VMR_ORDER_ID = " + pedidos.get(manual);
			}
			// Se actualizan las referencias manuales con sus cantidades respectiva
			if(manualT.get(manual) || cantIndep.get(manual).compareTo(new BigDecimal(0)) > 0){
				updated = true;
				if(sqlUpdateQty.length() > 29){
					sqlUpdateQty += ", ";
				}
				sqlUpdateQty += " XX_VME_INDEPABISQTY = " + cantIndep.get(manual);
			}
			sqlUpdateQty += " WHERE XX_VMR_VENDORPRODREF_ID = " + referenceID.get(manual);
//			System.out.println(sqlUpdateQty);
			if(updated){
				try { 
					pstmtUpdateRefs.addBatch(sqlUpdateQty);
					execBatch = true;
				} 
				catch (SQLException e) { e.printStackTrace(); }	
			}
			updated = false;
		}// for
		
		// Si habian referencias manuales, se ejecuta el batch
		if(execBatch){
			try { 
				int[] updCnt = pstmtUpdateRefs.executeBatch(); 
//				System.out.println("Total de referencias actualizadas: "+ updCnt.length);
			} 
			catch (SQLException e) { e.printStackTrace(); }
		}
	} //Fin insertRefAndProd
	
	/** setElemBrand
	 * Establece la relacion de un elemento y las marcas de las referencias asociadas
	 * @param elementID Identificador del elemento
	 * */
	public static void setElemBrand(Integer elementID) {
		PreparedStatement psBrand = null;
		ResultSet rsBrand = null;
		String relBrand = "";
		String sqlUpdateElem = "";
		
		String SQLBrand = " SELECT B.NAME FROM XX_VMR_BRAND B WHERE B.XX_VMR_BRAND_ID IN " +
				" (SELECT DISTINCT XX_VMR_BRAND_ID FROM XX_VME_REFERENCE " +
				" WHERE XX_VME_ELEMENTS_ID = " + elementID + ")";
		try{
			psBrand = DB.prepareStatement(SQLBrand, null);
			rsBrand = psBrand.executeQuery();
			while(rsBrand.next()){
				relBrand += " " + rsBrand.getString("NAME");
			}
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rsBrand);
			DB.closeStatement(psBrand);
		}// finally
		
		// Se toma el campo de Relación entre marcas del elemento para 
		// agregar la nueva marca
		sqlUpdateElem = " UPDATE XX_VME_ELEMENTS SET XX_VME_RELBRAND = '" + relBrand +"' " +
						" WHERE XX_VME_ELEMENTS_ID = " + elementID;
		try { int updated = DB.executeUpdate(null, sqlUpdateElem); }
		catch (Exception e) {e.printStackTrace();}
	}	 // Fin setElemBrand
	
} // Fin redefineQtyProd
