package compiere.model.dynamic.processes;

import java.awt.Container;
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.compiere.apps.ADialog;
import org.compiere.model.X_M_Warehouse;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.MVMABrochure;
import compiere.model.dynamic.XX_VME_BrochureItemIndepabis;

	/** Proceso que genera el reporte de un folleto para Indepabis (Función 2) 
	 * Permitir crear un reporte que contiene todas las especificaciones del 
	 * folleto necesarios para la aprobación del mismo por el organismo INDEPABIS.
	 * @author mvintimilla
	 * */

	public class XX_VME_indepabisReport extends SvrProcess{
		
		// Variables
		private boolean errorArchivo = false;
		private boolean errorPrecios = false;
		private int m_Brochure_ID = 0;
		private MVMABrochure brochure = null;
		private String nombreArchivo = "";
		//private Vector<BigDecimal> percentages = new Vector<BigDecimal>();
		//private Vector<String> warehouses = new Vector<String>();
		
		//hash que relaciona ID de la tienda con porcentaje de distribucion
		private HashMap<String, BigDecimal> porcentajes = new HashMap<String, BigDecimal>();
		private Vector<String> tiendasOrdenadas = new Vector();
		//id de la tienda con mayor porcentaje de distribución
		private BigDecimal mayorPorcentaje = new BigDecimal(0);
		private String idTiendaMayorPorc = "";
		//Cantidad asignada por elemento y tienda
		//(ID_Tienda+ID_Elemento)=>cantidad
		HashMap<String, BigDecimal> cantidadAsignada = new HashMap<String, BigDecimal>();	

		@Override
		protected String doIt() throws Exception {
			Vector<XX_VME_BrochureItemIndepabis> vectorItems = 
				new Vector<XX_VME_BrochureItemIndepabis>();
			brochure = new MVMABrochure(Env.getCtx(), m_Brochure_ID, null);
			Date fechaActual = new Date();
			int mes = fechaActual.getMonth()+1; //11;
			int anio = fechaActual.getYear()+1900;//2010;
			String productos = " ";
			BigDecimal subtotal = new BigDecimal (0);
			Vector<BigDecimal> quantities = new Vector<BigDecimal>();
			
						
			// Query para obtener todos los productos de un folleto determinado
			String SQLProducts = "SELECT B.NAME NOMBRE,  BP.XX_VMA_PAGENUMBER PAGINA, E.XX_VME_ELEMENTS_ID ELEMENTO,  " +
					" TO_CHAR(M.STARTDATE, 'DD/MM/YYYY') INICIO,  TO_CHAR(M.ENDDATE, 'DD/MM/YYYY') FIN,   " +
					" CASE WHEN XX_VME_QTYREFASSOCIATED = 0 THEN VEN.VALUE  " +
					" ELSE CAST(P.M_PRODUCT_ID||' '||' ' AS NVARCHAR2(80))  END AS PRODUCTO," +
					" CASE WHEN (SELECT COUNT(PROD.XX_VME_PRODUCT_ID) FROM XX_VME_PRODUCT PP " +
					" WHERE PP.XX_VME_PRODUCT_ID = PROD.XX_VME_PRODUCT_ID )IS NULL OR " +
					" (SELECT COUNT(PROD.XX_VME_PRODUCT_ID) FROM XX_VME_PRODUCT PP " +
					" WHERE PP.XX_VME_PRODUCT_ID = PROD.XX_VME_PRODUCT_ID ) = 0 THEN 'R' " +
					" ELSE 'P' END TIPO, " +
					//" CASE WHEN P.VALUE IS NULL THEN VEN.VALUE ELSE P.VALUE END ID, " +
					" NVL(P.VALUE, VEN.VALUE) ID, " +
					" REF.XX_VMR_VENDORPRODREF_ID REFERENCIA, " +
					" ref.VALUE SEARCHKEY, REF.NAME, NVL(REF.C_ORDER_ID,0) POID, " +
					" CASE WHEN " +
						" (SELECT COUNT(PROD.XX_VME_PRODUCT_ID) FROM XX_VME_PRODUCT PP " +
						" WHERE PP.XX_VME_PRODUCT_ID = PROD.XX_VME_PRODUCT_ID )IS NULL /*OR " +
						" (SELECT COUNT(PROD.XX_VME_PRODUCT_ID) FROM XX_VME_PRODUCT PP " +
						" WHERE PP.XX_VME_PRODUCT_ID = PROD.XX_VME_PRODUCT_ID ) = 0*/ THEN " +
							" (CASE WHEN REF.C_ORDER_ID IS NOT NULL THEN (SELECT " +
							//" CASE WHEN XX_SALEPRICE IS NULL THEN 88888888 ELSE XX_SALEPRICE END " +
							" NVL(XX_SALEPRICE, 88888888)*1.12 " +
							" FROM XX_VMR_PO_LINEREFPROV PROV " +
							" WHERE REF.XX_VMR_VENDORPRODREF_ID = PROV.XX_VMR_VENDORPRODREF_ID AND C_ORDER_ID = REF.C_ORDER_ID) " +
							//" ELSE (SELECT CASE WHEN MAX(XX_SALEPRICE) IS NULL THEN 99999999 ELSE MAX(XX_SALEPRICE) END " +
							" ELSE (SELECT NVL(MAX(XX_SALEPRICE), 99999999)*1.12 " +
							" FROM XX_VMR_PO_LINEREFPROV PROV " +
							" WHERE REF.XX_VMR_VENDORPRODREF_ID = PROV.XX_VMR_VENDORPRODREF_ID) END)" +
							"  ELSE " +
							" (CASE WHEN E.XX_VME_DYNAMICPRICE = 0 OR  E.XX_VME_DYNAMICPRICE IS NULL THEN " +
							" 		(CASE WHEN PROD.XX_VME_ACTUALPRICE = 0 " +
							" 			OR PROD.XX_VME_ACTUALPRICE IS NULL THEN 99999999 " +
							" 		ELSE ROUND(PROD.XX_VME_ACTUALPRICE,2) END) " +
							" ELSE ROUND(E.XX_VME_DYNAMICPRICE,2) END) END PRECIO, " +
					" CASE WHEN E.XX_VME_PROMODYNPRICE = 0 OR E.XX_VME_PROMODYNPRICE IS NULL THEN  		" +
					" (CASE WHEN PROD.XX_VME_PROMOTIONALPRICE = 0 			 " +
					" OR PROD.XX_VME_PROMOTIONALPRICE IS NULL  THEN 		" +
					" (CASE WHEN E.XX_VME_DYNAMICPRICE = 0  OR E.XX_VME_DYNAMICPRICE IS NULL THEN 			" +
					" (CASE WHEN PROD.XX_VME_ACTUALPRICE = 0 				 " +
					" OR PROD.XX_VME_ACTUALPRICE IS NULL THEN 99999999  			" +
					" ELSE ROUND(PROD.XX_VME_ACTUALPRICE - (PROD.XX_VME_ACTUALPRICE*(E.XX_VME_DISCOUNTPERCENTAGE/100)),2) END) 		" +
					" ELSE ROUND(E.XX_VME_DYNAMICPRICE - (E.XX_VME_DYNAMICPRICE*(E.XX_VME_DISCOUNTPERCENTAGE/100)),2) END)		" +
					" ELSE ROUND(PROD.XX_VME_PROMOTIONALPRICE, 2) END)  ELSE E.XX_VME_PROMODYNPRICE END PRECIODESC,  " +
					" CASE WHEN E.XX_VME_DISCOUNTPERCENTAGE <> 0  THEN 	E.XX_VME_DISCOUNTPERCENTAGE  ELSE     " +
					" (CASE WHEN E.XX_VME_DYNAMICPRICE <> 0 THEN             " +
					" (CASE WHEN E.XX_VME_PROMODYNPRICE <> 0 THEN                 " +
					" ROUND((((E.XX_VME_DYNAMICPRICE - E.XX_VME_PROMODYNPRICE)*100)/E.XX_VME_DYNAMICPRICE),2)            " +
					" ELSE (CASE WHEN PROD.XX_VME_PROMOTIONALPRICE <> 0 THEN                	" +
					" ROUND((((E.XX_VME_DYNAMICPRICE - PROD.XX_VME_PROMOTIONALPRICE)*100)/E.XX_VME_DYNAMICPRICE),2)                " +
					" ELSE 99999999 END)            END)    ELSE        " +
					" (CASE WHEN PROD.XX_VME_ACTUALPRICE <> 0 THEN            " +
					" (CASE WHEN E.XX_VME_PROMODYNPRICE <> 0 THEN                " +
					" ROUND((((PROD.XX_VME_ACTUALPRICE - E.XX_VME_PROMODYNPRICE)*100)/PROD.XX_VME_ACTUALPRICE),2)             " +
					" ELSE                (CASE WHEN PROD.XX_VME_PROMOTIONALPRICE <> 0 THEN                      " +
					" ROUND((((PROD.XX_VME_ACTUALPRICE - PROD.XX_VME_PROMOTIONALPRICE)*100)/PROD.XX_VME_ACTUALPRICE),2)               " +
					" ELSE 99999999 END)             END)         ELSE 99999999         END)    END) END AS DESCUENTO,  " +
					//" CASE WHEN E.NAME IS NULL THEN CAST(' ' AS NVARCHAR2(80)) ELSE E.NAME END DESCRIPCION,  " +
					" CASE WHEN PROD.NAME IS NULL THEN REF.NAME ELSE PROD.NAME END DESCRIPCION, " +
					" CASE WHEN PROD.XX_VME_INDEPABISQTY IS NULL 	THEN (CASE WHEN REF.XX_VME_INDEPABISQTY IS NULL THEN 0 ELSE REF.XX_VME_INDEPABISQTY END)  " +
					" ELSE PROD.XX_VME_INDEPABISQTY END QTY" +
					" FROM XX_VMA_MARKETINGACTIVITY M INNER JOIN XX_VMA_BROCHURE B ON (M.XX_VMA_BROCHURE_ID = B.XX_VMA_BROCHURE_ID) " +
					" LEFT OUTER JOIN XX_VMA_BROCHUREPAGE BP ON (BP.XX_VMA_BROCHURE_ID = B.XX_VMA_BROCHURE_ID) " +
					" LEFT OUTER JOIN XX_VME_ELEMENTS E ON (E.XX_VMA_BROCHUREPAGE_ID = BP.XX_VMA_BROCHUREPAGE_ID) " +
					" LEFT OUTER JOIN XX_VME_REFERENCE REF   ON (REF.XX_VME_ELEMENTS_ID = E.XX_VME_ELEMENTS_ID)" +
					" LEFT OUTER JOIN XX_VMR_VENDORPRODREF VEN  ON (REF.XX_VMR_VENDORPRODREF_ID = VEN.XX_VMR_VENDORPRODREF_ID) " +
					" LEFT OUTER JOIN XX_VME_PRODUCT PROD   ON (PROD.XX_VME_REFERENCE_ID = REF.XX_VME_REFERENCE_ID)" +
					" LEFT OUTER JOIN M_PRODUCT P   ON (P.M_PRODUCT_ID = PROD.M_PRODUCT_ID)" +
					" WHERE B.XX_VMA_BROCHURE_ID  = " + m_Brochure_ID +
					" AND E.XX_VME_TYPE IN ('P')  AND E.ISACTIVE = 'Y'  AND E.XX_VME_VALIDATED = 'Y' " +
					" ORDER BY BP.XX_VMA_PAGENUMBER, E.XX_VME_ELEMENTS_ID, precio asc";
			
			/*String SQLCount = "select ELEMENTO, COUNT(*) AS CANTIDAD FROM ( "+SQLProducts+" ) GROUP BY ELEMENTO ";
//			System.out.println("SQL: " + SQLProducts);
			
			//Obtengo la cuenta de cantidad de items por elemento
			//Asocia <elemento, cantidad>
			HashMap<BigDecimal, BigDecimal> cantidades = new HashMap<BigDecimal, BigDecimal>();
			Vector<BigDecimal> elementosOrdenados = new Vector<BigDecimal>();
			PreparedStatement psQueryCount = null;
			ResultSet rsQueryCount = null;
			try{
				psQueryCount = DB.prepareStatement(SQLProducts, null);
				rsQueryCount = psQueryCount.executeQuery();
				while(rsQueryCount.next()){
					BigDecimal elemento = rsQueryCount.getBigDecimal("ELEMENTO");
					BigDecimal cantidad = rsQueryCount.getBigDecimal("CANTIDAD");
					cantidades.put(elemento, cantidad);
					elementosOrdenados.add(elemento);
				}
				
			} 
			catch(SQLException e){	
				e.printStackTrace();
			}
			finally{
				DB.closeResultSet(rsQueryCount);
				DB.closeStatement(psQueryCount);
			}
			*/
			
			// Creo los objetos del folleto con valores iniciales
			PreparedStatement psQueryProducts = null;
			ResultSet rsQueryProducts = null;
			String IDProduct = "";

			try{
				psQueryProducts = DB.prepareStatement(SQLProducts, null);
				rsQueryProducts = psQueryProducts.executeQuery();
				while(rsQueryProducts.next()){
					IDProduct = rsQueryProducts.getString("PRODUCTO");
					// Si alguno de los precios o porcentaje es incorrecto, se le 
					// notifica al usuario
					if(rsQueryProducts.getBigDecimal("PRECIO").compareTo(new BigDecimal(99999999)) == 0 ||
							rsQueryProducts.getBigDecimal("DESCUENTO").compareTo(new BigDecimal(99999999)) == 0 ||
							rsQueryProducts.getBigDecimal("PRECIODESC").compareTo(new BigDecimal(99999999)) == 0)  {
						errorPrecios = true;
					}
					
					XX_VME_BrochureItemIndepabis item = new XX_VME_BrochureItemIndepabis(
							rsQueryProducts.getString("NOMBRE"),
							rsQueryProducts.getString("PAGINA"), 
							rsQueryProducts.getString("INICIO"),
							rsQueryProducts.getString("FIN"),
							rsQueryProducts.getString("ID"),
							IDProduct, 
							rsQueryProducts.getString("TIPO"),
							rsQueryProducts.getString("DESCRIPCION"),
							rsQueryProducts.getBigDecimal("PRECIO"), 
							rsQueryProducts.getBigDecimal("DESCUENTO"),
							rsQueryProducts.getBigDecimal("PRECIODESC"),
							rsQueryProducts.getBigDecimal("QTY"),
							/*new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), 
							new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), 
							new BigDecimal(0), new BigDecimal(0), */
							rsQueryProducts.getBigDecimal("QTY"),
							rsQueryProducts.getString("ELEMENTO"));

					vectorItems.add(item);
				} // while
			}//try		
			catch(SQLException e){	
				e.printStackTrace();
			}
			finally{
				DB.closeResultSet(rsQueryProducts);
				DB.closeStatement(psQueryProducts);
			}
			
			/* Obtener los procentajes de distribución para las tiendas */
			String SQLPercentage = " SELECT XX_VMA_DISTPERCENTBROCHURE DIST," +
								" M_WAREHOUSE_ID ID " +
								" FROM M_WAREHOUSE WHERE XX_VMA_DISTPERCENTBROCHURE<>0 " +
								" ORDER BY value ASC";
			//System.out.println("SQL Warehouse: "+SQLPercentage);
			
			PreparedStatement psQueryDist = null;
			ResultSet rsQueryDist = null;
			
			try{
				psQueryDist = DB.prepareStatement(SQLPercentage, null);
				rsQueryDist = psQueryDist.executeQuery();
				while(rsQueryDist.next()){
					//percentages.add(rsQueryDist.getBigDecimal("DIST"));
					//warehouses.add(rsQueryDist.getString("ID"));
					porcentajes.put(rsQueryDist.getString("ID"),rsQueryDist.getBigDecimal("DIST"));
					tiendasOrdenadas.add(rsQueryDist.getString("ID"));
					if(mayorPorcentaje.compareTo(rsQueryDist.getBigDecimal("DIST"))<0 ){
						mayorPorcentaje = rsQueryDist.getBigDecimal("DIST");
						idTiendaMayorPorc = rsQueryDist.getString("ID");
					}
					
				} // while
			}//try		
			catch(SQLException e){	
				e.printStackTrace();
			}
			finally{
				DB.closeResultSet(rsQueryDist);
				DB.closeStatement(psQueryDist);
			}
			
			/* Query para las cantidades en el inventario por producto.
			 * La cantidad a publicar dinámica tiene prioridad sobre la cantidad 
			 * de inventario. Si no hay cantidad en el folleto se procede a hacer
			 * el calculo del inventario */
			for(int i = 0; i < vectorItems.size(); i++){
				calculateInventory(vectorItems.get(i));
			} // for
			
			//Asignando las cantidades a publicar por cada elemento
			
			
			crearCSV(vectorItems, nombreArchivo);
			
			return "";

		}// Fin doIt
		
		/**
		 * calculateInventory
		 * Calcula el inventario total y por tienda para un producto del folleto
		 * @param item Item del folleto
		 * */
		public void calculateInventory(XX_VME_BrochureItemIndepabis item){
			//Vector<BigDecimal> quantities = new Vector<BigDecimal>();
			BigDecimal subtotal = new BigDecimal (0);
			BigDecimal temp = new BigDecimal (0);
			BigDecimal resto = new BigDecimal (0);
			
			// Si la cantidad del producto es mayor a cero se calculan las 
			// cantidades para las tiendas en base a los % asociados a las
			// mismas
			if(item.Quantity.compareTo(new BigDecimal(0)) > 0) {
				/*for (int j = 0; j < 11; j++){
					quantities.insertElementAt(new BigDecimal (0), j);
				}*/
				
				// Se calculan las cantidades para las tiendas de acuerdo a los
				// % de las mismas			
				for(int k=0;k<tiendasOrdenadas.size();k++){
					String idTienda = tiendasOrdenadas.get(k);
					subtotal = (porcentajes.get(idTienda).multiply(item.Quantity)).
							divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR).
							setScale(BigDecimal.ROUND_FLOOR);
					item.tiendaCantidad.put(idTienda, subtotal.setScale(0, BigDecimal.ROUND_UNNECESSARY));
					temp = temp.add(subtotal);
					BigDecimal  cantidadAnterior = cantidadAsignada.get(idTienda+item.Elemento);
					cantidadAsignada.put(idTienda+item.Elemento, ((cantidadAnterior==null?new BigDecimal(0):cantidadAnterior)).add(subtotal));
				}
				/*for(int i = 1; i < percentages.size(); i++){
					subtotal = (percentages.get(i).multiply(item.Quantity)).
								divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR).
								setScale(BigDecimal.ROUND_FLOOR);
					quantities.set(i, subtotal.setScale(0, BigDecimal.ROUND_UNNECESSARY));
					temp = temp.add(subtotal);
				} // for*/
				
				// Se colocan las cantidades en el item
				/*item.Chacaito = item.Chacaito.add(quantities.get(1));
				item.LaGranja = item.LaGranja.add(quantities.get(2));
				item.LasTrinitarias = item.LasTrinitarias.add(quantities.get(3));
				item.Maracaibo = item.Maracaibo.add(quantities.get(4));
				item.Millenium = item.Millenium.add(quantities.get(5));
				item.PuenteYanes = item.PuenteYanes.add(quantities.get(6));
				item.Tamanaco = item.Tamanaco.add(quantities.get(7));
				item.LaTrinidad = item.LaTrinidad.add(quantities.get(8));*/
								
				
				// Se verifican los subtotales de las cantidades calculadas con la del producto
				if(item.Quantity.compareTo(temp) > 0){
					resto = item.Quantity.subtract(temp);
					//El resto se le asigna a la tienda con mayor porcentaje de distribucion
					//TODO: esto es lo que hay que cambiar, si sobra dividirlo en partes iguales con las tiendas que tienen menos piezas
					//ordenar tiendas por cantidad
					Vector<String> tiendasAOrdenar = new Vector<String>();
					for(int i=0;i<tiendasOrdenadas.size();i++){
						tiendasAOrdenar.addElement(tiendasOrdenadas.get(i));
					}
					System.out.println("******  ANTES tiendasAOrdenar: "+tiendasAOrdenar.get(0)+"="+cantidadAsignada.get(tiendasAOrdenar.get(0)+item.Elemento)+" - "+tiendasAOrdenar.get(1)+"="+cantidadAsignada.get(tiendasAOrdenar.get(1)+item.Elemento)+" - "+tiendasAOrdenar.get(2)+"="+cantidadAsignada.get(tiendasAOrdenar.get(2)+item.Elemento)+" - "+tiendasAOrdenar.get(3)+"="+cantidadAsignada.get(tiendasAOrdenar.get(3)+item.Elemento)+" - "+tiendasAOrdenar.get(4)+"="+cantidadAsignada.get(tiendasAOrdenar.get(4)+item.Elemento)+" - "+tiendasAOrdenar.get(5)+"="+cantidadAsignada.get(tiendasAOrdenar.get(5)+item.Elemento)+" - "+tiendasAOrdenar.get(6)+"="+cantidadAsignada.get(tiendasAOrdenar.get(6)+item.Elemento)+" - "+tiendasAOrdenar.get(7)+"="+cantidadAsignada.get(tiendasAOrdenar.get(7)+item.Elemento));
					quicksort(tiendasAOrdenar,  item.Elemento);
					System.out.println("******DESPUES tiendasAOrdenar: "+tiendasAOrdenar.get(0)+"="+cantidadAsignada.get(tiendasAOrdenar.get(0)+item.Elemento)+" - "+tiendasAOrdenar.get(1)+"="+cantidadAsignada.get(tiendasAOrdenar.get(1)+item.Elemento)+" - "+tiendasAOrdenar.get(2)+"="+cantidadAsignada.get(tiendasAOrdenar.get(2)+item.Elemento)+" - "+tiendasAOrdenar.get(3)+"="+cantidadAsignada.get(tiendasAOrdenar.get(3)+item.Elemento)+" - "+tiendasAOrdenar.get(4)+"="+cantidadAsignada.get(tiendasAOrdenar.get(4)+item.Elemento)+" - "+tiendasAOrdenar.get(5)+"="+cantidadAsignada.get(tiendasAOrdenar.get(5)+item.Elemento)+" - "+tiendasAOrdenar.get(6)+"="+cantidadAsignada.get(tiendasAOrdenar.get(6)+item.Elemento)+" - "+tiendasAOrdenar.get(7)+"="+cantidadAsignada.get(tiendasAOrdenar.get(7)+item.Elemento));
					int i=0;
					int restoInt=resto.intValue();
					while (restoInt>0){
						item.tiendaCantidad.put(tiendasAOrdenar.get(i), item.tiendaCantidad.get(tiendasAOrdenar.get(i)).add(new BigDecimal(1)));
						restoInt--;
						i++;
					}
					
					//Asignar 1 pieza a las tiendas que les queda menos
					item.tiendaCantidad.put(idTiendaMayorPorc, item.tiendaCantidad.get(idTiendaMayorPorc).add(resto).setScale(0,BigDecimal.ROUND_UNNECESSARY));
				}//if resto
			} // if cd	
		} // calculateInventory
		
		
		/** Crear un archivo xls
		 * Se crea el archivo con el contenido del reporte de acuerdo a los valores
		 * obtenidos previamente
		 * @param items Vector que contiene los items del reporte
		 * @param nombre Nombre del archivo a generar
		 */
		public void crearCSV (Vector<XX_VME_BrochureItemIndepabis> items, String nombre) throws Exception {
			File archivo = new File(nombre);
			WritableWorkbook workbook = Workbook.createWorkbook(archivo);
			WritableSheet s = workbook.createSheet("Reporte Indepabis",0);
			PreparedStatement psQueryCliente = null;
			ResultSet rsQueryCliente = null;
			String footer = "";
			String SQLCliente = " SELECT NAME, XX_CI_RIF " +
					" FROM AD_CLIENT " +
					" WHERE AD_CLIENT_ID = "+getCtx().getContext("#XX_L_CLIENTCENTROBECO_ID");
			//System.out.println(SQLCliente);
			try{
				psQueryCliente = DB.prepareStatement(SQLCliente, null);
				rsQueryCliente = psQueryCliente.executeQuery();
				while(rsQueryCliente.next()){
					footer = rsQueryCliente.getString("NAME");
					footer += " " + rsQueryCliente.getString("XX_CI_RIF");
				} // while
			}//try		
			catch(SQLException e){	
				e.printStackTrace();
			}
			finally{
				DB.closeResultSet(rsQueryCliente);
				DB.closeStatement(psQueryCliente);
			}
			
			// Footer de las páginas con nombre de empresa y RIF
			s.setFooter("", "", footer);

			// Se define el ancho de algunas columnas 
			s.setColumnView(0, 30);	s.setColumnView(1, 10);
			s.setColumnView(2, 12);	s.setColumnView(3, 12);
			s.setColumnView(4, 20);	s.setColumnView(5, 70);
			s.setColumnView(6, 20);	s.setColumnView(7, 15);
			s.setColumnView(8, 15);	s.setColumnView(9, 20);
			s.setColumnView(10, 20); s.setColumnView(11, 20);
			s.setColumnView(12, 20); s.setColumnView(13, 20);
			s.setColumnView(14, 20); s.setColumnView(15, 20);
			s.setColumnView(16, 20); s.setColumnView(17, 15);
			
			// Definición del formato de las celdas cabecera y contenido
			WritableFont boldf = new WritableFont(WritableFont.ARIAL,12, WritableFont.BOLD);
			boldf.setColour(Colour.WHITE);
			WritableCellFormat cf1 = new WritableCellFormat(boldf);
			cf1.setBackground(Colour.BLUE_GREY);
			cf1.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
			cf1.setWrap(true);
			WritableFont noboldf = new WritableFont(WritableFont.ARIAL,11, WritableFont.NO_BOLD);
			WritableCellFormat cf2 = new WritableCellFormat(noboldf);
			cf2.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
			cf2.setWrap(true);
			WritableCellFormat cf3 = new WritableCellFormat(noboldf);
			cf3.setBackground(Colour.YELLOW);
			cf3.setWrap(true);
			int fila = 2;

			brochure = new MVMABrochure(Env.getCtx(), m_Brochure_ID, null);
			
			// Se agregan las celdas de la cabecera
			Label brochureName = new Label(0, 0, "Folleto: " + brochure.getName(), cf1);
			s.addCell(brochureName); 
			Label vigencia = new Label(2,0, "Vigencia ", cf1); s.addCell(vigencia); 
			Label tiendas = new Label(13, 0, "Tiendas ", cf1); s.addCell(tiendas);
			Label name = new Label(0, 1, "Nombre de la Promoción", cf1); s.addCell(name); 
			Label page = new Label(1, 1, "Página", cf1); s.addCell(page);
			Label from = new Label(2, 1, "Desde", cf1); s.addCell(from); 
			Label to = new Label(3, 1, "Hasta", cf1); s.addCell(to); 
			Label cod = new Label(4, 1, "Código de Producto", cf1); s.addCell(cod); 
			Label desc = new Label(5, 1, "Descripción del Artículo", cf1); s.addCell(desc); 
			Label pvpiva = new Label(6, 1, "PVP Regular C/IVA", cf1); s.addCell(pvpiva); 
			Label percentage = new Label(7, 1, "% Descuento", cf1); s.addCell(percentage); 
			Label pvpdesc = new Label(8, 1, "PVP con Descuento", cf1); s.addCell(pvpdesc);
			
			
			
			int j=9;
			//Iterator<String> iteraTiendas = porcentajes.keySet().iterator();
			for(int k=0;k<tiendasOrdenadas.size();k++){
				String idTienda = tiendasOrdenadas.get(k);
				X_M_Warehouse tda = new X_M_Warehouse(getCtx(), Integer.parseInt(idTienda), null);
				String nombreTda = tda.getName();
				Label etiqueta = new  Label(j, 1, nombreTda, cf1); 
				s.addCell(etiqueta); 
				j++;
			}
			/*Label py = new Label(9, 1, "Puente Yanes", cf1); s.addCell(py); 
			Label ch = new Label(10, 1, "Chacaito (CCS)", cf1); s.addCell(ch); 
			Label t = new Label(11, 1, "Tamanaco (CCS)", cf1); s.addCell(t); 
			Label lg = new Label(12, 1, "La Granja (Valencia)", cf1); s.addCell(lg); 
			Label lts = new Label(13, 1, "Las Trinitarias (Barquisimeto)", cf1); s.addCell(lts); 
			Label lt = new Label(14, 1, "La Trinidad (CCS)", cf1); s.addCell(lt); 
			Label m = new Label(15, 1, "Maracaibo (Sambil Maracaibo)", cf1); s.addCell(m); 
			Label mi = new Label(16, 1, "Millenium (CCS)", cf1); s.addCell(mi); 
			*/
			
			Label total = new Label(j, 1, "Total en Inventario", cf1); s.addCell(total); 
			
			// Escribir los detalles del folleto
			for(int i = 0; i < items.size(); i++){
				// Verificación de precios y descuento
//				if(items.get(i).PVPRegular.compareTo(new BigDecimal(99999999)) == 0 || 
//						items.get(i).PorcentajeDescuento.compareTo(new BigDecimal(99999999)) == 0 ||
//						items.get(i).PVPDescuento.compareTo(new BigDecimal(99999999)) == 0 ||
//						items.get(i).Quantity.compareTo(new BigDecimal(0)) == 0 ) {
//					continue;
//				} // precios/descuentos
				
				// Cantidad indepabis cero, los productos no aparecen en el folleto
				if(items.get(i).Quantity.compareTo(new BigDecimal(0)) == 0 ) {
					continue;
				}
				
				Label contentName = new Label(0, fila, items.get(i).NombrePromocion,cf2);
				s.addCell(contentName); 
				Label contentPage = new Label(1, fila, items.get(i).Pagina,cf2);
				s.addCell(contentPage); 
				Label contentFrom = new Label(2, fila, items.get(i).Desde,cf2);
				s.addCell(contentFrom);
				Label contentTo = new Label(3, fila, items.get(i).Hasta,cf2);
				s.addCell(contentTo);
				/** Codigo del producto
				 * Cuando es un elemento sin productos se escribe en el reporte 
				 * el código de la referencia
				 */
				Label contentcod = new Label(4, fila, items.get(i).Codigo,cf2);
				s.addCell(contentcod);
				Label contentDes= new Label(5, fila, items.get(i).Descripcion,cf2);
				s.addCell(contentDes);
				
				if(items.get(i).PVPRegular.compareTo(new BigDecimal(99999999)) == 0 ) {
					Label contentPVP = new Label(6, fila, (items.get(i).PVPRegular).toString(),cf3);
					s.addCell(contentPVP);
				} 
				else{
					Label contentPVP = new Label(6, fila, (items.get(i).PVPRegular).toString(),cf2);
					s.addCell(contentPVP);
				}
				
				if(items.get(i).PorcentajeDescuento.compareTo(new BigDecimal(99999999)) == 0 ) {
					Label contentPerc = new Label(7, fila, (items.get(i).PorcentajeDescuento).toString(),cf3);
					s.addCell(contentPerc);
				} 
				else{
					Label contentPerc = new Label(7, fila, (items.get(i).PorcentajeDescuento).toString(),cf2);
					s.addCell(contentPerc);
				}
				
				if(items.get(i).PVPDescuento.compareTo(new BigDecimal(99999999)) == 0 ) {
					Label contentPVPDesc = new Label(8, fila, (items.get(i).PVPDescuento).toString(),cf3);
					s.addCell(contentPVPDesc);
				} 
				else{
					Label contentPVPDesc = new Label(8, fila, (items.get(i).PVPDescuento).toString(),cf2);
					s.addCell(contentPVPDesc);
				}
				// Verificación de cantidad indepabis, si es cero no va para el reporte
				/*if(items.get(i).Quantity.compareTo(new BigDecimal(0)) == 0) {
					continue;
				}*/ // cantidad
				
				// Cantidades en tiendas
				int k = 9;
				//Iterator<String> iteraCantidades = items.get(i).tiendaCantidad.keySet().iterator();
				for(int l=0;l<tiendasOrdenadas.size();l++){
					BigDecimal cantidad = items.get(i).tiendaCantidad.get(tiendasOrdenadas.get(l));
					s.addCell(new Label(k, fila, (cantidad).toString(),cf2));
					k++;
				}
				
				/*
				Label contentPY = new Label(9, fila, (items.get(i).PuenteYanes).toString(),cf2);
				s.addCell(contentPY);
				Label contentCH = new Label(10, fila, (items.get(i).Chacaito).toString(),cf2);
				s.addCell(contentCH);
				Label contentT = new Label(11, fila, (items.get(i).Tamanaco).toString(),cf2);
				s.addCell(contentT);
				Label contentLG = new Label(12, fila, (items.get(i).LaGranja).toString(),cf2);
				s.addCell(contentLG);
				Label contentLTS = new Label(13, fila, (items.get(i).LasTrinitarias).toString(),cf2);
				s.addCell(contentLTS);
				Label contentLT = new Label(14, fila, (items.get(i).LaTrinidad).toString(),cf2);
				s.addCell(contentLT);
				Label contentM = new Label(15, fila, (items.get(i).Maracaibo).toString(),cf2);
				s.addCell(contentM);
				Label contentMI = new Label(16, fila, (items.get(i).Millenium).toString(),cf2);
				s.addCell(contentMI);*/
				Label contentTotal = new Label(k, fila, (items.get(i).TotalInventario).toString(),cf2);
				s.addCell(contentTotal);
				fila++;
			}//for
			workbook.write();
			workbook.close();
				
			//El archivo fue creado
			String msg = Msg.getMsg(Env.getCtx(), "XX_FileCreated", new String [] {
				nombre
			});
				
			if(errorPrecios){
				ADialog.info(1, new Container(), "Error con precios o descuento en productos");
			}
			ADialog.info(1, new Container(), msg);	

		}// Fin crearCSV
		
		@Override
		protected void prepare() {
			m_Brochure_ID = getRecord_ID();
			ProcessInfoParameter[] parameter = getParameter();

			
			for (ProcessInfoParameter element : parameter) {
				String name = element.getParameterName();
				String extension = "";
				if (element.getParameter() == null) ;
				else if (name.equals("File")) { 
					if (element.getParameter() != null) {					
						String archivo = (String)element.getParameter();
						
						// Verifica si el archivo existe
						File archivoFisico = new File(archivo);
						if (archivoFisico.exists()){
							ADialog.error(1, new Container(), "XX_FileExist" );
							errorArchivo = true;
							return;	
						}
						
						// Se obtiene la extensión del archivo seleccionado
						extension = archivo.substring(archivo.length()-4, archivo.length());
						
						// Verifica la extensión del archivo seleccionado
						if (!extension.equals(".xls")) {
							ADialog.error(1, new Container(), "Not Excel" );
							errorArchivo = true;
							return;		
						}	
						
						nombreArchivo = (String) element.getParameter();
					}//getparameter 
				}//else if Archivo
			}// For
			
		}// Fin prepare	
		
		/***********************************************************************
		*  Quicksort code from Sedgewick 7.1, 7.2.
		***********************************************************************/
		public  void quicksort(Vector<String> a, String idElemento) {
	        shuffle(a);                        // to guard against worst-case
	        quicksort(a, 0, a.size() - 1, idElemento);
	    }

	    // quicksort a[left] to a[right]
	    public  void quicksort(Vector<String> a, int left, int right, String idElemento) {
	        if (right <= left) return;
	        int i = partition(a, left, right, idElemento);
	        quicksort(a, left, i-1, idElemento);
	        quicksort(a, i+1, right, idElemento);
	    }

	    // partition a[left] to a[right], assumes left < right
	    private  int partition(Vector<String> a, int left, int right, String idElemento) {
	        int i = left - 1;
	        int j = right;
	        while (true) {
	            while (less(a.elementAt(++i), a.elementAt(right), idElemento))      // find item on left to swap
	                ;                               // a[right] acts as sentinel
	            while (less(a.elementAt(right), a.elementAt(--j), idElemento))      // find item on right to swap
	                if (j == left) break;           // don't go out-of-bounds
	            if (i >= j) break;                  // check if pointers cross
	            exch(a, i, j);                      // swap two elements into place
	        }
	        exch(a, i, right);                      // swap with partition element
	        return i;
	    }

	    // is x < y ?
	    private  boolean less(String x, String y, String idElemento) {
	       // comparisons++;
	        return (cantidadAsignada.get(x+idElemento).compareTo(cantidadAsignada.get(y+idElemento))  < 0);
	    }

	    // exchange a[i] and a[j]
	    private static void exch(Vector<String> a, int i, int j) {
	       // exchanges++;
	        String swap = a.elementAt(i);
	        a.set(i, a.elementAt(j));
	        a.set(j, swap);
	    }

	    // shuffle the array a[]
	    private static void shuffle(Vector<String> a) {
	        int N = a.size();
	        for (int i = 0; i < N; i++) {
	            int r = i + (int) (Math.random() * (N-i));   // between i and N-1
	            exch(a, i, r);
	        }
	    }

		/***********************************************************************
		*  Quicksort code from Sedgewick 7.1, 7.2.
		***********************************************************************/
		
			
} // Fin XX_VME_IndepabisReport
