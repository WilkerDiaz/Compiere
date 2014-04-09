package compiere.model.promociones.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CPreparedStatement;
import org.compiere.util.DB;

import compiere.model.birt.BIRTReport;
import compiere.model.cds.As400DbManager;

public class XX_PromotionReports  extends SvrProcess{
	
	private static int idPromotion;
	BigDecimal meta = new BigDecimal(0);
	
	private void promotionReport(){
		//Estos datos entran al proceso como parámetros
		
		
		int headerReportID =0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String reportID = "SELECT h.XX_VMR_HeaderReport_ID, h.ReportDate, p.DateFrom FROM XX_VMR_HeaderReport h " +
						  "INNER JOIN XX_VMR_Promotion p ON h.XX_VMR_Promotion_ID = p.XX_VMR_Promotion_ID " +
						  "WHERE h.XX_VMR_Promotion_ID="+idPromotion;	
		pstmt = DB.prepareStatement(reportID,get_Trx());

		try {
			rs = pstmt.executeQuery();
			if(rs.next()){
				headerReportID = rs.getInt("XX_VMR_HeaderReport_ID");
				//Comparo fechas
				if(getActualDate().after(rs.getDate("DateFrom"))){
					//Usar los datos existentes para crear el reporte
					//Actualizar el header con la fecha actual
					String update = "UPDATE XX_VMR_HeaderReport SET ReportDate = to_date('" +getActualDate(getActualDate())+"','YYYYMMdd')"+
							" WHERE XX_VMR_HeaderReport_ID = "+headerReportID;
					DB.executeUpdate(get_Trx(),update);
					
				}else{
					
					String update = "UPDATE XX_VMR_HeaderReport SET ReportDate = to_date('" +getActualDate(getActualDate())+"','YYYYMMdd')"+
							" WHERE XX_VMR_HeaderReport_ID = "+headerReportID;
					DB.executeUpdate(get_Trx(),update);
					//Eliminar detalles y crearlos nuevamente
					String delete = "DELETE FROM XX_VMR_PromotionReportTable " +
							"WHERE XX_VMR_HeaderReport_ID = "+headerReportID;
					DB.executeUpdate(get_Trx(),delete);
					// Realizar el insert de select.
					insertDetails(headerReportID);
					setWarehouses(headerReportID);
					//Setear el inventario en la tabla de detalles
					setInventary();
					
				}
			}else{// --> No existe el reporte, crearlo.
				headerReportID = getNext("XX_VMR_HeaderReport","XX_VMR_HeaderReport_ID");
				//Crear Detalles y cabecera
				String header = "INSERT INTO XX_VMR_HeaderReport(XX_VMR_HeaderReport_ID, XX_VMR_Promotion_ID) " +
						"VALUES("+headerReportID+","+ idPromotion+")";
	
				DB.executeUpdate(get_Trx(),header);
				
				//Realizar el insert de select
				insertDetails(headerReportID);
				setWarehouses(headerReportID);
				setInventary();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			get_TrxName().rollback();
		} catch (Exception e) {
			e.printStackTrace();
			get_TrxName().rollback();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		get_Trx().commit();
		createReport(headerReportID);
	
	}

	private void createReport(int reportID) {
		ArrayList<Integer> warehouses = getWarehouses();
		BIRTReport report = null;
		for(int warehouse:warehouses){
			report = new BIRTReport();
			
			//Agregar parametros
			report.parameterName.add("IDReporte");
			report.parameterValue.add(reportID);
			report.parameterName.add("IDPromocion");
			report.parameterValue.add(idPromotion);
			report.parameterName.add("Tienda");
			report.parameterValue.add(warehouse);
			report.parameterName.add("Meta");
			report.parameterValue.add(meta);
			
			//Correr Reporte
			if(warehouse!=0)
				report.runReport("promotion", "xls");
			//else
				//report.sendReport("promotion", "xls");
		}
		
	}

	private static Date getActualDate(){
		Calendar cal1 = Calendar.getInstance();
		return cal1.getTime();
	}
	private static String getActualDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	private static String getActualMonth(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(date);
	}
	private static String getActualYear(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(date);
	}
	private void setInventary() throws SQLException{
		//Busco los productos de los cuales necesito inventario
		String select = "SELECT M_Product_Value FROM XX_VMR_HeaderReport h INNER JOIN " +
				"XX_VMR_PromotionReportTable d ON h.XX_VMR_HeaderReport_ID = d.XX_VMR_HeaderReport_ID " +
				"WHERE XX_VMR_Promotion_ID="+idPromotion;
		PreparedStatement pstmt = null,pstmt2=null;
		ResultSet rs = null,rs2 = null;
		String products = "";
		String  update = "UPDATE XX_VMR_PromotionReportTable set inventary=? "+
				" WHERE M_Product_Value =? AND XX_PriceConsecutive =? AND warehouse=?";
		boolean execute = false;
		pstmt = DB.prepareStatement(select,get_Trx());
		int batch = 3000;
		int count = 0;
		Statement statementAS400 = null;
		String sql;
		pstmt2 = DB.prepareStatement(update, get_Trx());
		try {
			rs = pstmt.executeQuery();
			As400DbManager As = new As400DbManager();
			As.conectar();
			while(true){
				products = "";
				count = 0;
				while(rs.next() && count<batch){
					products += rs.getInt(1)+", ";
					count++;
				}
				if(products.length()>1){
					products = products.substring(0,(products.length()-2));
					 
					statementAS400 = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					sql = "SELECT (SUM(CANTINVINI)+SUM(CANTCOMPRA)-SUM(CANTVENTAS)+SUM(CANTMOVIMI) )INV," +
							" CODPRO, CONPRE, TIENDA  FROM BECOFILE.INVM14 WHERE AÑOINV = "+getActualYear(getActualDate())+
							" AND MESINV = "+getActualMonth(getActualDate())+" AND TIENDA!=1 AND CODPRO  IN  ("+products+") " +
							"GROUP BY TIENDA,CODPRO, CONPRE";
					
					rs2 = statementAS400.executeQuery(sql);

					while(rs2.next()){
						pstmt2.setInt(1, rs2.getInt("INV"));
						pstmt2.setInt(2, rs2.getInt("CODPRO"));
						pstmt2.setInt(3, rs2.getInt("CONPRE"));
						pstmt2.setInt(4, rs2.getInt("TIENDA"));
						pstmt2.addBatch();
						execute = true;
					}	
				}else{
					//Ya no hay mas productos
					break;
				}	
			}
			if(execute){
				int[] success = pstmt2.executeBatch();
			}
		} catch (SQLException e) {
			throw e;
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
		}

	}
	
	//Función que busca el ultimo entero mas uno dado una tabla y un campo
	//El campo debe ser numerico en la BD
	private Integer getNext(String table,String field)
	{
		String query= "select max(XX_VMR_HeaderReport_ID) from "+table;
		PreparedStatement pstmt = DB.prepareStatement(query,get_Trx());
		ResultSet rs = null;
		Integer lastID = 0;
		try {
			rs = pstmt.executeQuery();
			while (rs.next()) {
				lastID = rs.getInt(1); 
				System.out.println("Ultima condicion de la tabla HeaderReport: " +lastID);
			}
			if(lastID == 0) lastID = 1000000;
			else lastID++;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return lastID;
	}
	
	private  void insertDetails(int reportID){
		String insert = "INSERT INTO XX_VMR_PromotionReportTable (XX_VMR_HeaderReport_ID, M_PRODUCT_VALUE, XX_VMR_VENDORPRODREF_ID," +
				"XX_VMR_Department_ID, XX_VMR_Brand_ID, XX_DiscountRate, XX_InitPrice, XX_EndPrice, ProductCreated, " +
				"XX_PriceConsecutive, XX_VMR_PriceConsecutive_ID, Warehouse) ";
		String select = "SELECT "+reportID+", P.Value, XX_VMR_VENDORPRODREF_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_BRAND_ID, "+
						"XX_DISCOUNTRATE,XX_UnitPurchasePrice, "+
						"CASE "+
						" WHEN "+
						" XX_TYPEPROMOTION <> 1001100 THEN null "+
						"WHEN "+
						" (XX_DISCOUNTAMOUNT = 0 AND XX_TYPEPROMOTION = 1001100)  " +//OJO!! La promo...
						"THEN (XX_UnitPurchasePrice-((XX_UnitPurchasePrice*XX_DISCOUNTRATE)/100)) "+
						"ELSE (XX_UnitPurchasePrice-XX_DISCOUNTAMOUNT)  END ENDPRICE, "+
						"A.CREATED,XX_PRICECONSECUTIVE, XX_VMR_PRICECONSECUTIVE_ID, XX_WarehouseBecoNumber "+
						"FROM M_PRODUCT P INNER JOIN XX_VMR_PRICECONSECUTIVE PC ON P.M_PRODUCT_ID = PC.M_PRODUCT_ID "+
						"INNER JOIN m_attributesetinstance A ON PC.M_ATTRIBUTESETINSTANCE_ID = A.M_ATTRIBUTESETINSTANCE_ID "+
						"INNER JOIN ( "+
						//--Si tiene producto seteado
						"SELECT M_PRODUCT_ID, XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE, XX_TYPEPROMOTION, w.XX_WarehouseBecoNumber "+
						"FROM xx_vmr_promotion pr inner join xx_vmr_detailpromotionext dv on "+
						"pr.xx_vmr_promotion_id = dv.xx_vmr_promotion_id  inner join xx_vmr_promocondwarehouse w  "+
						"on dv.xx_vmr_promoconditionvalue_id = w.xx_vmr_promoconditionvalue_id  "+
						"WHERE dv.xx_vmr_promotion_id="+idPromotion+" "+
						"UNION "+
						//--Sólo categoría
						"SELECT p.M_PRODUCT_ID , XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE, XX_TYPEPROMOTION, w.XX_WarehouseBecoNumber "+
						"FROM xx_vmr_promotion pr inner join xx_vmr_detailpromotionext dv on "+
						"pr.xx_vmr_promotion_id = dv.xx_vmr_promotion_id inner join xx_vmr_promocondwarehouse w  "+
						"on dv.xx_vmr_promoconditionvalue_id = w.xx_vmr_promoconditionvalue_id inner join M_Product p  "+
						"on (dv.xx_vmr_category_ID = p.xx_vmr_category_id)  "+
						"WHERE dv.xx_vmr_department_ID is null AND dv.xx_vmr_line_id is null  "+
						"AND dv.xx_vmr_section_id is null AND dv.xx_vmr_vendorprodref_id is null "+ 
						"AND dv.xx_vmr_brand_id is null AND dv.m_product_id is null "+
						"AND dv.xx_vmr_promotion_id="+idPromotion+" "+
						"UNION "+
						//--Categoría+departamento 
						"SELECT p.M_PRODUCT_ID , XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE, XX_TYPEPROMOTION, w.XX_WarehouseBecoNumber "+
						"FROM xx_vmr_promotion pr inner join xx_vmr_detailpromotionext dv on "+
						"pr.xx_vmr_promotion_id = dv.xx_vmr_promotion_id inner join xx_vmr_promocondwarehouse w  "+
						"on dv.xx_vmr_promoconditionvalue_id = w.xx_vmr_promoconditionvalue_id inner join M_Product p  "+
						"on (dv.xx_vmr_category_ID = p.xx_vmr_category_id AND dv.xx_vmr_department_ID=p.xx_vmr_department_id)  "+
						"WHERE dv.xx_vmr_line_id is null AND dv.xx_vmr_section_id is null  "+
						"AND dv.xx_vmr_vendorprodref_id is null AND dv.xx_vmr_brand_id is null AND dv.m_product_id is null "+
						"AND dv.xx_vmr_promotion_id="+idPromotion+" "+
						"UNION "+
						//--Categoría+departamento+línea
						"SELECT p.M_PRODUCT_ID , XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE, XX_TYPEPROMOTION, w.XX_WarehouseBecoNumber "+
						"FROM xx_vmr_promotion pr inner join xx_vmr_detailpromotionext dv on "+
						"pr.xx_vmr_promotion_id = dv.xx_vmr_promotion_id inner join xx_vmr_promocondwarehouse w "+ 
						"on dv.xx_vmr_promoconditionvalue_id = w.xx_vmr_promoconditionvalue_id inner join M_Product p  "+
						"on (dv.xx_vmr_category_ID = p.xx_vmr_category_id AND dv.xx_vmr_department_ID=p.xx_vmr_department_id "+
						"AND dv.xx_vmr_line_id=p.xx_vmr_line_id)   "+
						"WHERE dv.xx_vmr_section_id is null AND dv.xx_vmr_vendorprodref_id is null  "+
						"AND dv.xx_vmr_brand_id is null AND dv.m_product_id is null "+
						"AND  dv.xx_vmr_promotion_id="+idPromotion+" "+
						"UNION "+
						//--Categoría+departamento+línea+sección
						"SELECT p.M_PRODUCT_ID , XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE, XX_TYPEPROMOTION, w.XX_WarehouseBecoNumber "+
						"FROM xx_vmr_promotion pr inner join xx_vmr_detailpromotionext dv on "+
						"pr.xx_vmr_promotion_id = dv.xx_vmr_promotion_id inner join xx_vmr_promocondwarehouse w  "+
						"on dv.xx_vmr_promoconditionvalue_id = w.xx_vmr_promoconditionvalue_id inner join M_Product p  "+
						"on (dv.xx_vmr_category_ID = p.xx_vmr_category_id AND dv.xx_vmr_department_ID=p.xx_vmr_department_id "+
						"AND dv.xx_vmr_line_id=p.xx_vmr_line_id AND dv.xx_vmr_section_id=p.xx_vmr_section_id)  "+ 
						"WHERE dv.xx_vmr_vendorprodref_id is null AND dv.xx_vmr_brand_id is null AND dv.m_product_id is null "+
						"AND  dv.xx_vmr_promotion_id="+idPromotion+" "+
						"UNION "+
						//--Categoría+departamento+línea+sección+referencia
						"SELECT p.M_PRODUCT_ID , XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE, XX_TYPEPROMOTION, w.XX_WarehouseBecoNumber "+
						"FROM xx_vmr_promotion pr inner join xx_vmr_detailpromotionext dv on "+
						"pr.xx_vmr_promotion_id = dv.xx_vmr_promotion_id inner join xx_vmr_promocondwarehouse w  "+
						"on dv.xx_vmr_promoconditionvalue_id = w.xx_vmr_promoconditionvalue_id inner join M_Product p "+ 
						"on (dv.xx_vmr_category_ID = p.xx_vmr_category_id AND dv.xx_vmr_department_ID=p.xx_vmr_department_id "+
						"AND dv.xx_vmr_line_id=p.xx_vmr_line_id AND dv.xx_vmr_section_id=p.xx_vmr_section_id "+
						"AND dv.xx_vmr_vendorprodref_id=p.xx_vmr_vendorprodref_id)  "+ 
						"WHERE dv.xx_vmr_brand_id is null AND dv.m_product_id is null AND dv.xx_vmr_promotion_id="+idPromotion+" "+
						"UNION "+
						//--Marca
						"SELECT p.M_PRODUCT_ID , XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE, XX_TYPEPROMOTION, w.XX_WarehouseBecoNumber "+
						"FROM xx_vmr_promotion pr inner join xx_vmr_detailpromotionext dv on "+
						"pr.xx_vmr_promotion_id = dv.xx_vmr_promotion_id inner join xx_vmr_promocondwarehouse w  "+
						"on dv.xx_vmr_promoconditionvalue_id = w.xx_vmr_promoconditionvalue_id inner join M_Product p  "+
						"on (dv.xx_vmr_category_ID = p.xx_vmr_category_id AND dv.xx_vmr_brand_id=p.xx_vmr_brand_id "+
						"AND dv.xx_vmr_department_ID is null AND dv.xx_vmr_line_id is null  "+
						"AND dv.xx_vmr_section_id is null AND dv.xx_vmr_vendorprodref_id is null AND dv.m_product_id is null)  "+
						"OR "+
						"(dv.xx_vmr_category_ID = p.xx_vmr_category_id AND dv.xx_vmr_department_ID=p.xx_vmr_department_id "+
						"AND dv.xx_vmr_brand_id=p.xx_vmr_brand_id AND dv.xx_vmr_line_id is null  "+
						"AND dv.xx_vmr_section_id is null AND dv.xx_vmr_vendorprodref_id is null AND dv.m_product_id is null) "+
						"OR "+
						"(dv.xx_vmr_category_ID = p.xx_vmr_category_id AND dv.xx_vmr_department_ID=p.xx_vmr_department_id "+
						"AND dv.xx_vmr_line_id=p.xx_vmr_line_id AND dv.xx_vmr_brand_id=p.xx_vmr_brand_id  "+
						"AND dv.xx_vmr_section_id is null AND dv.xx_vmr_vendorprodref_id is null AND dv.m_product_id is null)   "+
						"OR  "+
						"(dv.xx_vmr_category_ID = p.xx_vmr_category_id AND dv.xx_vmr_department_ID=p.xx_vmr_department_id "+
						"AND dv.xx_vmr_line_id=p.xx_vmr_line_id AND dv.xx_vmr_section_id=p.xx_vmr_section_id "+
						"AND dv.xx_vmr_brand_id=p.xx_vmr_brand_id AND dv.xx_vmr_vendorprodref_id is null AND dv.m_product_id is null)   "+
						"OR "+
						"(dv.xx_vmr_category_ID = p.xx_vmr_category_id AND dv.xx_vmr_department_ID=p.xx_vmr_department_id "+
						"AND dv.xx_vmr_line_id=p.xx_vmr_line_id AND dv.xx_vmr_section_id=p.xx_vmr_section_id "+
						"AND dv.xx_vmr_vendorprodref_id=p.xx_vmr_vendorprodref_id AND dv.xx_vmr_brand_id=p.xx_vmr_brand_id AND dv.m_product_id is null)  "+ 
						"WHERE dv.xx_vmr_promotion_id="+idPromotion+") DET  "+
						"ON DET.M_PRODUCT_ID = P.M_PRODUCT_ID "; 

			DB.executeUpdate(get_Trx(),insert+select);
		
	}

	private ArrayList<Integer> getWarehouses(){
		String selectWarehouses = "SELECT VALUE FROM M_Warehouse WHERE VALUE = '003'"; 
		CPreparedStatement pstmt = DB.prepareStatement(selectWarehouses, get_Trx());
		ResultSet rs = null ;
		ArrayList<Integer> warehouses= new ArrayList<Integer>();
		try {
			rs =pstmt.executeQuery();
			while(rs.next()){
				warehouses.add(rs.getInt("VALUE"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return warehouses;
		
	}
	
	private void setWarehouses(int reportID) throws Exception{
		String insert = "INSERT INTO XX_VMR_PromotionReportTable (XX_VMR_HeaderReport_ID, M_PRODUCT_VALUE, XX_VMR_VENDORPRODREF_ID," +
				"XX_VMR_Department_ID, XX_VMR_Brand_ID, XX_DiscountRate, XX_InitPrice, XX_EndPrice, ProductCreated, " +
				"XX_PriceConsecutive, XX_VMR_PriceConsecutive_ID, Warehouse) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		String select = "SELECT * FROM XX_VMR_PromotionReportTable WHERE warehouse='000' AND XX_VMR_HeaderReport_ID="+reportID; 
		String selectWarehouses = "SELECT VALUE FROM M_Warehouse WHERE VALUE<> '000' AND VALUE<>'001'"; 
		CPreparedStatement pstmt = DB.prepareStatement(select,get_Trx());
		CPreparedStatement pstmt2 = DB.prepareStatement(selectWarehouses, get_Trx());
		ResultSet rs = null,rs2 = null ;
		CPreparedStatement pstmt3 = DB.prepareStatement(insert, get_Trx());
		ArrayList<Integer> warehouses= new ArrayList<Integer>();
		try {
			rs2 =pstmt2.executeQuery();
			while(rs2.next()){
				warehouses.add(rs2.getInt("VALUE"));
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				for(Integer warehouse:warehouses){
					pstmt3.setInt(1, reportID);
					pstmt3.setInt(2, rs.getInt("M_Product_Value"));
					pstmt3.setInt(3, rs.getInt("XX_VMR_VENDORPRODREF_ID"));
					pstmt3.setInt(4, rs.getInt("XX_VMR_Department_ID"));
					pstmt3.setInt(5, rs.getInt("XX_VMR_Brand_ID"));
					pstmt3.setInt(6, rs.getInt("XX_DiscountRate"));
					pstmt3.setInt(7, rs.getInt("XX_InitPrice"));
					pstmt3.setInt(8, rs.getInt("XX_EndPrice"));
					pstmt3.setDate(9, rs.getDate("ProductCreated"));
					pstmt3.setInt(10, rs.getInt("XX_PriceConsecutive"));
					pstmt3.setInt(11, rs.getInt("XX_VMR_PriceConsecutive_ID"));
					pstmt3.setInt(12, warehouse);
					pstmt3.addBatch();
				}
			}
			pstmt3.executeBatch();
			String delete = "DELETE FROM XX_VMR_PromotionReportTable WHERE warehouse=0 AND XX_VMR_HeaderReport_ID="+reportID;
			DB.executeUpdate(get_Trx(),delete);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeStatement(pstmt3);
		}
		
		
	}
	@Override
	protected void prepare() {
		// Llenar idPromotion y tienda
		idPromotion = getRecord_ID();
		ProcessInfoParameter[] parameter = getParameter();
			
			for (ProcessInfoParameter element : parameter) {
				String name = element.getParameterName();			
				
				if(name.equalsIgnoreCase("Goal")){
					
					meta = (BigDecimal) element.getParameter();
					
					if(meta.equals(0))
						return;
				}
				else
					return;
			} // for parameter
			
	}

	@Override
	protected String doIt() throws Exception {
		promotionReport();
		
		return "";
	}
	
	
}
