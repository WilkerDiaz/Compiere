package compiere.model.promociones.process;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.compiere.grid.ed.VCheckBox;
import org.compiere.model.MSequence;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CPreparedStatement;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;


/**
 *  Permite cargar promociones clásicas precio a precio / porcentaje
 *  desde un excel
 *
 *  @author     Jessica Pérez
 */

public class XX_LoadPromotionProducts extends SvrProcess{

	
	private String file = null;
	ArrayList<String> warehouses;
	@Override
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("File") ) {
					file = element.getParameter().toString();
				}
			}			
		}
	}

	@Override
	protected String doIt() throws Exception {
		String msg = "";
		if (file == null) {
			msg =  Msg.translate( getCtx(), "File Not Loaded");
		} else {
			
			if(!file.substring(file.length()-4, file.length()).equals(".xls")){
				if(file.substring(file.length()-5, file.length()).equals(".xlsx")){
					msg = Msg.translate( getCtx(), "Excel Earlier Format");;	
				}else{
					msg =Msg.translate( getCtx(), "Not Excel");
				}
			}else{	
				msg = readFile();
				if (msg.isEmpty()) {
					saveDetails();
					saveConditions();
					updateDetails();
					updateDetailPromotionSequence();
					updatePromoConditionSequence();
					validate();
					//validateConditions();		

				}
			}
		}
		return msg;
	}
	
	public String readFile()  throws IOException  {
		File inputWorkbook = new File(file);
		Workbook w;
		int rows = 0;
		double price_value = 0;
		String price,product_value, percent, wh;
		double percent_value = 0;
		ArrayList<Object> params;
		int num_warehouses = 0, j=0;
		warehouses = new ArrayList<String>();
		try {
			String msg = "";
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the sheet
			Sheet sheet = w.getSheet(0);
			rows = sheet.getRows();
			num_warehouses = sheet.getRow(0).length;
			//Si la cantidad de columnas es 3
			if(sheet.getRow(1).length==3 && sheet.getRows() > 2 && num_warehouses>0){
				//Capturo las tiendas a las que se dirige la promocion
			while(j<num_warehouses){
				wh = sheet.getCell(j, 0).getContents();		
				if (wh.isEmpty() || wh == null ) {
					return "Error al cargar las tiendas";
				}else{
					if(wh.length()==1) wh = "00"+wh;
					else if(wh.length()==2) wh = "0"+wh;
					else return "Error al cargar las tiendas, valores de entrada incorrectos. Revisar archivo de carga.";
					try{
						Integer.parseInt(wh);
					}catch(Exception e){
						return "Error al cargar las tiendas, los valores de entrada deben ser enteros. Revisar archivo de carga.";
					}
					warehouses.add(wh);
				}
				j++;
			}
		
				
				
				//Valido que las cabeceras tengan el formato correcto	
				if(!sheet.getCell(0, 1).getContents().equals("PRODUCTO") 
						|| !sheet.getCell(1, 1).getContents().equals("PRECIO")
						|| !sheet.getCell(2, 1).getContents().equals("PORCENTAJE")
				) {
					msg = Msg.translate( getCtx(), "Column Names");
					return msg;
				}
				
				String sql = "INSERT INTO XX_TEMP_DETAILPROMOTION "
						+ "(VALUE, XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE)"
						+ " VALUES (?,?,?) ";
				List<Object[]> bulkParams = new ArrayList<Object[]>();
			
				//leer en bloque de a 500
				for( int i=2; i < rows ; i++){
					 params = new ArrayList<Object>();	
					//Capturo el value de producto
					product_value = sheet.getCell(0, i).getContents();
					if (product_value.isEmpty()){
						break;
						//return "Error al leer codigo de producto en la fila "+(i+1) + " " + sheet.getCell(0, i).getContents();	
					}
					
					//Capturo el precio
					price = sheet.getCell(1, i).getContents();		
					if (price.isEmpty() || price == null ) {
						price_value = 0.0;
					}else{
						price = price.replace(',', '.');
						price_value = Double.parseDouble(price);
					}
					
					//Capturo el porcentaje
					percent = sheet.getCell(2, i).getContents();		
					if (percent.isEmpty() || percent == null ) {
						percent_value = 0;
					}else{
						percent = percent.replace(',', '.');
						percent_value = Double.parseDouble(percent);
					}	
					params.add(product_value);
					params.add(price_value);
					params.add(percent_value);
					bulkParams.add(params.toArray());
				}
				//Inserto los productos en la tabla temporal
				DB.executeBulkUpdate(get_Trx(), sql, bulkParams, true, true);				
				
				sql = "DELETE FROM XX_TEMP_DETAILPROMOTION WHERE VALUE IN " +
						"(SELECT P.VALUE FROM XX_TEMP_DETAILPROMOTION T INNER JOIN M_PRODUCT P " +
						"ON (T.VALUE = P.VALUE) WHERE P.ISACTIVE = 'N')";
				DB.executeUpdate(get_Trx(), sql, new ArrayList());
			}
	
		} catch (BiffException e){		
			log.log(Level.SEVERE, e.getMessage());
		}
		return "";
	}
	

	//Función que busca el último código de la tabla detalle promoción,
		private Integer idDetailPromotionExt()
		{
			String query= "select max(XX_VMR_DetailPromotionExt_ID) from XX_VMR_DetailPromotionExt";
			PreparedStatement pstmt = DB.prepareStatement(query, get_Trx());
			ResultSet rs = null;
			Integer lastDetailPromotion = 0;
			try {
				rs = pstmt.executeQuery();
				while (rs.next()) {
					lastDetailPromotion = rs.getInt(1); 
					System.out.println("Ultimo Detalle de la tabla promociones: "+lastDetailPromotion);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			return lastDetailPromotion;
		}
		
		//Función que busca el último código de la tabla detalle promoción,
		private Integer idPromoConditionValue()
		{
			String query= "select max(XX_VMR_PromoConditionValue_ID) from XX_VMR_PromoConditionValue";
			PreparedStatement pstmt = DB.prepareStatement(query, get_Trx());
			ResultSet rs = null;
			Integer lastConditionValue = 0;
			try {
				rs = pstmt.executeQuery();
				while (rs.next()) {
					lastConditionValue = rs.getInt(1); 
					System.out.println("Ultima condicion de la tabla PromoConditionValue: " +lastConditionValue);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			return lastConditionValue;
		}
		
		private void saveDetails(){
			//Parametros de la consulta
			Integer cliente = Env.getCtx().getAD_Client_ID(), 
					org = Env.getCtx().getAD_Org_ID(), 
					creadoPor = Env.getCtx().getAD_User_ID(),
					actualizadoPor = Env.getCtx().getAD_User_ID(),
					promotion = getRecord_ID(),
					detailPromotion = idDetailPromotionExt();

			
			/*
			 * 
			 * INSERT INTO XX_VMR_DETAILPROMOTIONEXT(AD_CLIENT_ID, AD_ORG_ID, CREATEDBY,UPDATEDBY,
			 * XX_VMR_PROMOTION_ID, XX_VMR_DETAILPROMOTIONEXT_ID, XX_WAREHOUSEBECONUMBER, M_PRODUCT_ID,
			 * XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE)
			 * 
			 * (SELECT CLIENTE AS CLIENTE, ORG AS ORG, CREADOPOR AS CREADOPOR, ACTUALIZADOPOR AS ACTUALIZADOPOR, 
			 * PROMOTION AS PROMOCION, DETAILPROMOTION+ROWNUM AS DETAILPROMOTION, TIENDA AS TIENDA,
			 * P.M_PRODUCTO_ID AS PRODUCTO, t.XX_DISCOUNTAMOUNT AS PRECIO, T.XX_DISCOUNTRATE AS PORCENTAJE
			 * FROM M_PRODUCT P INNER JOIN XX_TEMP_DETAILPROMOTION T ON P.VALUE = T.VALUE)
			 * 
			 * 
			 * */
			//Columnas obligatorias del insert
			String insertColumns = "AD_Client_ID, AD_Org_ID, CreatedBy, UpdatedBy, XX_VMR_Promotion_ID" +
									", XX_VMR_DetailPromotionExt_ID, M_PRODUCT_ID "+
									", XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID, XX_VMR_SECTION_ID," +
									" XX_VMR_VENDORPRODREF_ID ";
			//Columnas obligatorias del select
			String selectColumns =cliente+" as cliente, "+org+" as org, "+creadoPor+" as creadoPor, "
								+actualizadoPor+" as actualizadoPor, "+promotion+" as promocion,"+
								detailPromotion+ " + rownum as detallepromocion, P.M_PRODUCT_ID as producto "+
								", P.XX_VMR_CATEGORY_ID, P.XX_VMR_DEPARTMENT_ID, P.XX_VMR_LINE_ID, P.XX_VMR_SECTION_ID," +
								" P.XX_VMR_VENDORPRODREF_ID ";
	
		
			//De acuerdo a la promoción estos strings cambiarían dinámicamente
			String insertColumnsPlus = ", XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE ";
			String selectColumnsPlus = ",  T.XX_DISCOUNTAMOUNT as precio, T.XX_DISCOUNTRATE as porcentaje ";
			String fromSelect = "M_PRODUCT P INNER JOIN XX_TEMP_DETAILPROMOTION T ON P.VALUE = T.VALUE";
			String productSql = "(SELECT M_PRODUCT_ID FROM XX_VMR_DETAILPROMOTIONEXT WHERE XX_VMR_PROMOTION_ID="+promotion+" and M_PRODUCT_ID is not null AND ISACTIVE <> 'N')";
			String where = " P.ISACTIVE='Y' AND P.M_PRODUCT_ID NOT IN "+productSql;
			
			String consulta = "INSERT INTO XX_VMR_DETAILPROMOTIONEXT("+insertColumns+insertColumnsPlus+")"+
								"(SELECT "+selectColumns+selectColumnsPlus+" FROM "+fromSelect+" WHERE "+where+ ")";	
			
			System.out.println("Consulta: "+consulta);		
			int x;
			x = DB.executeUpdate(get_Trx(), consulta);
			//comprobacion archivo vs insertados
			System.out.println("Insertando en detallePromocionExt: "+x+" registros");	
			//updateDetailPromotionSequence();
			//MSequence.updateSequenceID(cliente, "XX_VMR_DetailPromotionExt");
			
		}
		
		private void saveConditions(){
			//Parametros de la consulta
			Integer cliente = Env.getCtx().getAD_Client_ID(), 
					org = Env.getCtx().getAD_Org_ID(), 
					creadoPor = Env.getCtx().getAD_User_ID(),
					actualizadoPor = Env.getCtx().getAD_User_ID(),
					promotion = getRecord_ID(),
					firstpromoconditionvalue = idPromoConditionValue(),
					promoconditionvalue = idPromoConditionValue(),
					lastpromoconditionvalue;

			//Actualizando la tabla promoConditionValue para adaptarla a los detalles insertados.
			
			
			//Columnas obligatorias del insert
			String insertColumns = "AD_Client_ID, AD_Org_ID, CreatedBy, UpdatedBy, XX_VMR_Promotion_ID" +
									", XX_VMR_PromoConditionValue_ID";
			//Columnas obligatorias del select
			String selectColumns =cliente+" as cliente, "+org+" as org, "+creadoPor+" as creadoPor, "
							+actualizadoPor+" as actualizadoPor, "+promotion+" as promocion ";
			String select2Columns = "cliente, org,creadoPor,actualizadoPor,promocion, "+
							promoconditionvalue+" + rownum as promoconditionvalue ";
		
			//De acuerdo a la promoción estos strings cambiarían dinámicamente
			String insertColumnsPlus = ", XX_DISCOUNTAMOUNT, XX_DISCOUNTRATE ";
			String selectColumnsPlus = ",  T.XX_DISCOUNTAMOUNT as precio ";
			String select2ColumnsPlus = ", precio, 0 ";
			String fromSelect = "M_PRODUCT P INNER JOIN XX_TEMP_DETAILPROMOTION T ON P.VALUE = T.VALUE";
			String conditionSql = "(SELECT XX_DISCOUNTAMOUNT FROM XX_VMR_PROMOCONDITIONVALUE WHERE XX_VMR_PROMOTION_ID="+promotion+" AND XX_DISCOUNTAMOUNT>0)";
			String where = " XX_DISCOUNTAMOUNT > 0 AND XX_DISCOUNTAMOUNT NOT IN "+conditionSql;
			
			

			
			String consulta = "INSERT INTO XX_VMR_PROMOCONDITIONVALUE("+insertColumns+insertColumnsPlus+")"+
							  "(SELECT "+select2Columns+select2ColumnsPlus+"FROM (SELECT DISTINCT "+selectColumns+
							  selectColumnsPlus+" FROM "+fromSelect+" WHERE "+where+"))";
			
			System.out.println("Insert PromoCondValue "+consulta);
			int x;
			x = DB.executeUpdate(get_Trx(), consulta);
			System.out.println("Insertando en PROMOCONDITIONVALUE: "+x+" registros");	
			promoconditionvalue = idPromoConditionValue();	
		
			//Columnas obligatorias del insert
			insertColumns = "AD_Client_ID, AD_Org_ID, CreatedBy, UpdatedBy, XX_VMR_Promotion_ID" +
									", XX_VMR_PromoConditionValue_ID";
			//Columnas obligatorias del select
			selectColumns =cliente+" as cliente, "+org+" as org, "+creadoPor+" as creadoPor, "
							+actualizadoPor+" as actualizadoPor, "+promotion+" as promocion ";
			select2Columns = "cliente, org,creadoPor,actualizadoPor,promocion, "+
							promoconditionvalue+" + rownum as promoconditionvalue ";
		
			//De acuerdo a la promoción estos strings cambiarían dinámicamente
			insertColumnsPlus = ", XX_DISCOUNTRATE, XX_DISCOUNTAMOUNT ";
			selectColumnsPlus = ",  T.XX_DISCOUNTRATE as porcentaje ";
			select2ColumnsPlus = ", porcentaje, 0 ";
			fromSelect = "M_PRODUCT P INNER JOIN XX_TEMP_DETAILPROMOTION T ON P.VALUE = T.VALUE";
			conditionSql = "(SELECT XX_DISCOUNTRATE FROM XX_VMR_PROMOCONDITIONVALUE WHERE XX_VMR_PROMOTION_ID="+promotion+" AND XX_DISCOUNTRATE>0)";
			where = " XX_DISCOUNTRATE > 0 AND XX_DISCOUNTRATE NOT IN "+conditionSql;;
			
			consulta = "INSERT INTO XX_VMR_PROMOCONDITIONVALUE("+insertColumns+insertColumnsPlus+")"+
							  "(SELECT "+select2Columns+select2ColumnsPlus+"FROM (SELECT DISTINCT "+selectColumns+
							  selectColumnsPlus+" FROM "+fromSelect+" WHERE "+where+"))";
			System.out.println("Insert PromoCondValue "+consulta);	
			x = DB.executeUpdate(get_Trx(), consulta);
			System.out.println("Insertando en PROMOCONDITIONVALUE: "+x+" registros");	
			
			lastpromoconditionvalue = idPromoConditionValue();
			saveWarehouses(firstpromoconditionvalue, lastpromoconditionvalue);
			//MSequence.updateSequenceID(cliente, "XX_VMR_PromoConditionValue");
		}
		
		private void updateDetails(){
			double price,percent;
			int promoConditionValue;
			int promotion = getRecord_ID();
			PreparedStatement pstmt;
			ResultSet rs;
			ArrayList<Object> params;
			List<Object[]> bulkParams = new ArrayList<Object[]>();
			String consulta = "SELECT DISTINCT XX_VMR_PROMOCONDITIONVALUE_ID, XX_DISCOUNTAMOUNT"+
					  " FROM XX_VMR_PROMOCONDITIONVALUE WHERE XX_VMR_PROMOTION_ID=? "+
					  "AND XX_DISCOUNTAMOUNT > 0";
			try {
				pstmt = DB.prepareStatement(consulta, get_Trx());
				pstmt.setInt(1, promotion);
				rs = pstmt.executeQuery();
				consulta = "UPDATE XX_VMR_DETAILPROMOTIONEXT set XX_VMR_PROMOCONDITIONVALUE_ID=? WHERE " +
						"XX_VMR_PROMOTION_ID=? AND XX_DISCOUNTAMOUNT=?";
				while (rs.next()) {
					params = new ArrayList<Object>();	
					price = rs.getDouble("XX_DISCOUNTAMOUNT");
					promoConditionValue = rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID");				
					params.add(promoConditionValue);
					params.add(promotion);
					params.add(price);
					bulkParams.add(params.toArray());
	
				}
				//Actualizo los detalles con su respectiva condicion
				DB.executeBulkUpdate(get_Trx(), consulta, bulkParams, true, true);
				
				
			/*	while (rs.next()) {
					price = rs.getDouble("XX_DISCOUNTAMOUNT");
					promoConditionValue = rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID");
					consulta = "UPDATE XX_VMR_DETAILPROMOTIONEXT set XX_VMR_PROMOCONDITIONVALUE_ID="+promoConditionValue+
					" WHERE XX_VMR_PROMOTION_ID="+promotion+" AND XX_DISCOUNTAMOUNT="+price;
					x = DB.executeUpdate(get_Trx(),consulta);
					System.out.println("Consulta: "+consulta);
					System.out.println("Actualizados: "+x);
					
				}
				*/
				
				rs.close();
				pstmt.close();

			}		
			catch (SQLException e) 	{
				log.log(Level.SEVERE, consulta, e);
			}
			consulta = "SELECT DISTINCT XX_VMR_PROMOCONDITIONVALUE_ID, XX_DISCOUNTRATE"+
					  " FROM XX_VMR_PROMOCONDITIONVALUE WHERE XX_VMR_PROMOTION_ID=? "+
					  "AND XX_DISCOUNTRATE > 0";
			
			
			try {
				pstmt = DB.prepareStatement(consulta,get_Trx());
				pstmt.setInt(1, promotion);
				rs = pstmt.executeQuery();
				bulkParams = new ArrayList<Object[]>();
				
				consulta = "UPDATE XX_VMR_DETAILPROMOTIONEXT set XX_VMR_PROMOCONDITIONVALUE_ID=? WHERE " +
						"XX_VMR_PROMOTION_ID=? AND XX_DISCOUNTRATE=?";
				while (rs.next()) {
					params = new ArrayList<Object>();	
					percent = rs.getDouble("XX_DISCOUNTRATE");
					promoConditionValue = rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID");
					
					params.add(promoConditionValue);
					params.add(promotion);
					params.add(percent);
					bulkParams.add(params.toArray());
				}
				//Actualizo los detalles con su respectiva condicion
				DB.executeBulkUpdate(get_Trx(), consulta, bulkParams, true, true);
				
				/*while (rs.next()) {
					percent = rs.getDouble("XX_DISCOUNTRATE");
					promoConditionValue = rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID");
					consulta = "UPDATE XX_VMR_DETAILPROMOTIONEXT set XX_VMR_PROMOCONDITIONVALUE_ID="+promoConditionValue+
					" WHERE XX_VMR_PROMOTION_ID="+promotion+" AND XX_DISCOUNTRATE="+percent;
					x = DB.executeUpdate(get_Trx(),consulta);
					System.out.println("Consulta: "+consulta);
					System.out.println("Actualizados: "+x);
					
				}*/
				rs.close();
				pstmt.close();

			}		
			catch (SQLException e) 	{
				log.log(Level.SEVERE, consulta, e);
			}
			get_Trx().commit();
			
			
		}
		
		private void validate(){
			int promotion = getRecord_ID();
			int x = 0;
			PreparedStatement pstmt;
			ResultSet rs;
			int category = 0, department = 0, line = 0, section = 0, brand = 0, reference = 0;
			String params = "";
			Integer updatedBy = Env.getCtx().getAD_User_ID();
			boolean haveParams = false;
			String consulta = "SELECT XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID," +
					  "XX_VMR_SECTION_ID, XX_VMR_BRAND_ID, XX_VMR_VENDORPRODREF_ID "+
					  " FROM XX_VMR_DETAILPROMOTIONEXT WHERE XX_VMR_PROMOTION_ID=? "+
					  "AND M_PRODUCT_ID IS NULL";
			try {
				
				pstmt = DB.prepareStatement(consulta, null);
				pstmt.setInt(1, promotion);
				rs = pstmt.executeQuery();
				/*consulta = "SELECT XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID," +
						  "XX_VMR_SECTION_ID, XX_VMR_BRAND_ID, XX_VMR_VENDORPRODREF_ID "+
						  " FROM XX_VMR_DETAILPROMOTIONEXT WHERE XX_VMR_PROMOTION_ID=? "+
						  "AND M_PRODUCT_ID IS NOT NULL ";*/
				
				consulta = "UPDATE XX_VMR_DETAILPROMOTIONEXT SET ISACTIVE='N', UPDATEDBY="+updatedBy+"  WHERE XX_VMR_PROMOTION_ID=? "+
						  "AND M_PRODUCT_ID IS NOT NULL";
				while (rs.next()) {
					//capturo los parametros hasta encontrar los null... 
					category = rs.getInt("XX_VMR_CATEGORY_ID");
					department = rs.getInt("XX_VMR_DEPARTMENT_ID");
					brand = rs.getInt("XX_VMR_BRAND_ID");	
					line = rs.getInt("XX_VMR_LINE_ID");
					section = rs.getInt("XX_VMR_SECTION_ID");
					reference = rs.getInt("XX_VMR_VENDORPRODREF_ID");
					if(department==0){
						if(category != 0){
							if(x == 0) consulta+=" AND ("; else params+= " OR ";
							params += "( XX_VMR_CATEGORY_ID = "+category;
							x++;
							haveParams = true;
						}
					}else if(line == 0){
						if(x == 0) consulta+=" AND ("; else params+= " OR ";
						params += "( XX_VMR_CATEGORY_ID = "+category+" AND XX_VMR_DEPARTMENT_ID = "+department;
						x++;
						haveParams = true;
					}else if(section == 0){
						if(x == 0) consulta+=" AND ("; else params+= " OR ";
						params += "( XX_VMR_CATEGORY_ID = "+category+" AND XX_VMR_DEPARTMENT_ID = "+department+" AND XX_VMR_LINE_ID = "+line ;
						x++;
						haveParams = true;
					}else if(reference == 0){
						if(x == 0) consulta+=" AND ("; else params+= " OR ";
						params += " (XX_VMR_CATEGORY_ID = "+category+" AND XX_VMR_DEPARTMENT_ID = "+department+" AND XX_VMR_LINE_ID = "+line +
									" AND XX_VMR_SECTION_ID = "+section;
						x++;
						haveParams = true;
					}else{
						if(x == 0) consulta+=" AND ("; else params+= " OR ";
						params += " (XX_VMR_CATEGORY_ID = "+category+" AND XX_VMR_DEPARTMENT_ID = "+department+" AND XX_VMR_LINE_ID = "+line +
									" AND XX_VMR_SECTION_ID = "+section+" AND XX_VMR_VENDORPRODREF_ID = "+reference;
						x++;
						haveParams = true;
					}
					if(brand !=0){
						if(haveParams) params += " AND "; else params += "("; 
						params += " XX_VMR_BRAND_ID = "+brand+")";
					}else{
						params += ")";
					}
				}
				if (x>0){
					consulta += params;
					consulta += ")";
				
					rs.close();
					pstmt.close();
					pstmt = DB.prepareStatement(consulta,null);
					pstmt.setInt(1, promotion);
					x = pstmt.executeUpdate();
					System.out.println("Se actualizaron :"+x+" registros");
				}
				rs.close();
				pstmt.close();

			}		
			catch (SQLException e) 	{
				log.log(Level.SEVERE, consulta, e);
			}
		}
		
	
		
		private void updatePromoConditionSequence(){
			Integer lastPromoCondition = idPromoConditionValue()+1;
			int user =Env.getCtx().getAD_User_ID();
			int client =Env.getCtx().getAD_Client_ID();
			String sql = "UPDATE AD_SEQUENCE SET CURRENTNEXT="+lastPromoCondition+"," +
					" UPDATEDBY="+user+" WHERE NAME='XX_VMR_PromoConditionValue'";
			DB.executeUpdate(null, sql);
			int idSequence =  DB.getNextID(client, "XX_VMR_PromoConditionValue",null);
			while(idSequence<lastPromoCondition){
				idSequence =  DB.getNextID(client, "XX_VMR_PromoConditionValue",null);
			}
		}
		
		private void updateDetailPromotionSequence(){
			Integer lastDetail = idDetailPromotionExt()+1;
			int user =Env.getCtx().getAD_User_ID();
			int client =Env.getCtx().getAD_Client_ID();
			String sql = "UPDATE AD_SEQUENCE SET CURRENTNEXT="+lastDetail+"," +
					" UPDATEDBY="+user+" WHERE NAME='XX_VMR_DetailPromotionExt'";
			DB.executeUpdate(null, sql);
			int idSequence = DB.getNextID(client, "XX_VMR_DetailPromotionExt",null);
			while(idSequence<lastDetail){
				idSequence =  DB.getNextID(client, "XX_VMR_DetailPromotionExt",null);
			}
		}
		
		private void saveWarehouses(int conditionID,List<Object[]> bulkParams){
			String warehouse;
			Integer cliente = Env.getCtx().getAD_Client_ID(), 
					org = Env.getCtx().getAD_Org_ID(), 
					user = Env.getCtx().getAD_User_ID(),
					ids = idPromoCondWarehouse();
			ArrayList<Object> params;
			String delete = "DELETE FROM XX_VMR_PROMOCONDWAREHOUSE WHERE XX_VMR_PROMOCONDITIONVALUE_ID = "+conditionID;
			try {
				DB.executeUpdateEx(delete, null);
				Iterator<String> it = warehouses.iterator();
				while(it.hasNext()){
					warehouse = it.next();
					params = new ArrayList<Object>();
					params.add(cliente);
					params.add(org);
					params.add(user);
					params.add(user);
					params.add(conditionID);
					params.add(warehouse);
					params.add(ids);
					bulkParams.add(params.toArray());
					ids++;
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
				
		}
		
		private void saveWarehouses(int inic, int fin){
			String sql;
			sql = "";
			ResultSet rs;
			
			sql = "INSERT INTO XX_VMR_PROMOCONDWAREHOUSE "
					+ "(AD_CLIENT_ID, AD_ORG_ID, CREATEDBY, UPDATEDBY, XX_VMR_PROMOCONDITIONVALUE_ID, XX_WAREHOUSEBECONUMBER, XX_VMR_PROMOCONDWAREHOUSE_ID)"
					+ " VALUES (?,?,?,?,?,?,?)";
			
			String sql2 = "SELECT XX_VMR_PROMOCONDITIONVALUE_ID FROM  XX_VMR_PROMOCONDITIONVALUE WHERE  XX_VMR_PROMOCONDITIONVALUE_ID >"+inic+" AND  XX_VMR_PROMOCONDITIONVALUE_ID <="+fin;
			CPreparedStatement pstmt;
			
			List<Object[]> bulkParams = new ArrayList<Object[]>();
			pstmt = DB.prepareStatement(sql2,get_Trx());

			try {
				rs = pstmt.executeQuery();
				while(rs.next()){
					saveWarehouses(rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID"),bulkParams);
				}
				DB.executeBulkUpdate(null, sql, bulkParams, true, true);
			}catch(Exception e){
				e.printStackTrace();
			}
			
				
		}
		
			//Función que busca el último código de la tabla detalle promoción,
			private Integer idPromoCondWarehouse()
			{
				String query= "select max(XX_VMR_PromoCondWarehouse_ID) from XX_VMR_PromoCondWarehouse";
				PreparedStatement pstmt = DB.prepareStatement(query, get_Trx());
				ResultSet rs = null;
				Integer lastConditionValue = 0;
				try {
					rs = pstmt.executeQuery();
					while (rs.next()) {
						lastConditionValue = rs.getInt(1); 
						System.out.println("Ultima condicion de la tabla Warehouse: " +lastConditionValue);
					}
					if(lastConditionValue == 0) lastConditionValue = 1000000;
					else lastConditionValue++;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				finally{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
				return lastConditionValue;
			}
			
			private boolean validateConditions(){
				CPreparedStatement pstmt = null;
				ResultSet rs = null;
				int promotion = getRecord_ID();
				boolean valid = true;
				String sql = "DELETE FROM XX_VMR_PROMOCONDITIONVALUE WHERE XX_VMR_PROMOCONDITIONVALUE_ID IN ("+
						"SELECT XX_VMR_PROMOCONDITIONVALUE_ID FROM( "+
							"SELECT PCV.XX_VMR_PROMOCONDITIONVALUE_ID, COUNT(DPE.XX_VMR_DETAILPROMOTIONEXT_ID) AS cuenta "+
							"FROM XX_VMR_PROMOCONDITIONVALUE PCV LEFT OUTER JOIN XX_VMR_DETAILPROMOTIONEXT DPE ON "+
							"PCV.XX_VMR_PROMOCONDITIONVALUE_ID = DPE.XX_VMR_PROMOCONDITIONVALUE_ID "+
							 "WHERE PCV.XX_VMR_PROMOTION_ID=? GROUP BY PCV.XX_VMR_PROMOCONDITIONVALUE_ID) WHERE cuenta = 0 )";
				try {
					
					pstmt = DB.prepareStatement(sql, null);
					pstmt.setInt(1, promotion);
					valid = pstmt.execute();
				
				}catch (SQLException e) 	{
					log.log(Level.SEVERE, sql, e);
				}finally{
					DB.closeStatement(pstmt);
					DB.closeResultSet(rs);
				}
				return valid;
			}
}
