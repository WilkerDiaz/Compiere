package compiere.model.promociones.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.compiere.util.CPreparedStatement;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.promociones.X_Ref_XX_TypePromotion;
import compiere.model.promociones.X_XX_VMR_Promotion;
import compiere.model.promociones.forms.XX_PromoCondWarehouseForm;

public class XX_ApproveMethods {
	private String msg ="";

	
	public boolean checkCollisions(int promotion){

		int x = 0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
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
			}

		}		
		catch (SQLException e) 	{
			msg += "No se pudo chequear colisiones, contacte con el administrador";
			//log.log(Level.SEVERE, consulta, e);+
			return false;
		}finally{
			
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
			
		}
		return true;
	}
	
	public boolean validateProducts(int promotion){
	
		CPreparedStatement pstmt = null;
		ResultSet rs = null;
		int promoConditionValue=0,productid=0,productid2=0, i=0;
		String product;
		boolean valid=true;
		String sql = "SELECT D.M_PRODUCT_ID, D.XX_VMR_PROMOCONDITIONVALUE_ID, P.NAME " +
				"FROM XX_VMR_DETAILPROMOTIONEXT D INNER JOIN M_PRODUCT P" +
				" ON D.M_PRODUCT_ID = P.M_PRODUCT_ID" +
				" WHERE XX_VMR_PROMOTION_ID=? AND D.M_PRODUCT_ID IN "+
				"(SELECT M_PRODUCT_ID FROM XX_VMR_DETAILPROMOTIONEXT " +
				"WHERE XX_VMR_PROMOTION_ID=? and IsActive = 'Y' GROUP BY M_PRODUCT_ID HAVING COUNT(*)>1 )" +
				" ORDER BY D.M_PRODUCT_ID ";
		try {
			
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, promotion);
			pstmt.setInt(2, promotion);
			rs = pstmt.executeQuery();
			if(rs.next()){
				msg+="No se puede aprobar la promoción: \n";
				product = rs.getString("NAME");
				promoConditionValue = rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID");
				productid = rs.getInt("M_PRODUCT_ID");
				msg += "El producto "+productid+"-"+product+" se repite en las condiciones: " +
						promoConditionValue;
				valid = false;
			}
			while(rs.next()){
				product = rs.getString("NAME");
				if(promoConditionValue!=rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID")){
					promoConditionValue = rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID");
					productid2 = rs.getInt("M_PRODUCT_ID");
					if(productid2==productid){
						msg+= ", "+promoConditionValue;
					}else{
						msg +="\n";
						i++;
						if(i == 5) return false;
						msg += "El producto "+productid+"-"+product+" se repite en las condiciones: " +
								promoConditionValue;
						productid=productid2;
						
					}				
				}
			}
		}catch (SQLException e) 	{
			//log.log(Level.SEVERE, sql, e);
		}finally{
			try {
				pstmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valid;
	}
	
	public boolean validateConditions(int promotion){
		CPreparedStatement pstmt= null,pstmt2 = null;
		ResultSet rs= null,rs2 = null;
		boolean valid = true;
		String sql = "SELECT XX_VMR_PROMOTION_ID FROM( "+
				"SELECT P.XX_VMR_PROMOTION_ID, COUNT(DPE.XX_VMR_PROMOCONDITIONVALUE_ID) AS cuenta "+
				"FROM XX_VMR_PROMOTION P LEFT OUTER JOIN XX_VMR_PROMOCONDITIONVALUE DPE ON "+
				"P.XX_VMR_PROMOTION_ID = DPE.XX_VMR_PROMOTION_ID "+
				 "WHERE P.XX_VMR_PROMOTION_ID=? and DPE.ISACTIVE = 'Y' GROUP BY P.XX_VMR_PROMOTION_ID) WHERE cuenta = 0 ";
		try {

			pstmt2 = DB.prepareStatement(sql, null);
			pstmt2.setInt(1, promotion);
			rs2 = pstmt2.executeQuery();
			if(rs2.next()){
				msg+= "No es posible aprobar la promoción porque no tiene ninguna condición asociada.\n";
				valid = false;
			}else{
				
				sql = "SELECT XX_VMR_PROMOCONDITIONVALUE_ID FROM( "+
						"SELECT PCV.XX_VMR_PROMOCONDITIONVALUE_ID, COUNT(DPE.XX_VMR_DETAILPROMOTIONEXT_ID) AS cuenta "+
						"FROM XX_VMR_PROMOCONDITIONVALUE PCV LEFT OUTER JOIN XX_VMR_DETAILPROMOTIONEXT DPE ON "+
						"PCV.XX_VMR_PROMOCONDITIONVALUE_ID = DPE.XX_VMR_PROMOCONDITIONVALUE_ID "+
						 "WHERE PCV.XX_VMR_PROMOTION_ID=? and PCV.ISACTIVE = 'Y' GROUP BY PCV.XX_VMR_PROMOCONDITIONVALUE_ID) WHERE cuenta = 0 ";
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, promotion);
				rs = pstmt.executeQuery();
				if(rs.next()){
					msg+= "No es posible aprobar la promoción, hay condiciones sin ningún detalle asociado.\n";
					//msg+= "Las condiciones sin detalles son las siguientes: "+rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID");
					
					valid = false;
				}
			}
			
			/*while(rs.next()){
				msg+= ", "+rs.getInt("XX_VMR_PROMOCONDITIONVALUE_ID");
				
			}*/

			
		}catch (SQLException e) 	{
			//log.log(Level.SEVERE, sql, e);
		}finally{
	
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs2);
		}
		return valid;
	}	

	/*Valida que no coincidad promociones del mismo tipo, misma prioridad con productos iguales*/
	public boolean validatePriority(X_XX_VMR_Promotion promotion){
		Timestamp dateFinish = promotion.getDateFinish();
		Timestamp dateFrom = promotion.getDateFrom();
		Timestamp dateFinish2;
		Timestamp dateFrom2;
		CPreparedStatement pstmt = null;
		ResultSet rs = null;
		String where="";
		boolean ret = true;
		int detener = 15;
		int promoMostrada = 0;
		String sql = "SELECT XX_VMR_PROMOTION_ID,DATEFINISH,DATEFROM FROM XX_VMR_PROMOTION WHERE XX_TYPEPROMOTION=? " +
				"AND PRIORITY=? AND ISACTIVE='Y' AND XX_VMR_PROMOTION_ID != ? AND (XX_APPROVEMAR='Y' OR XX_APPROVEMER='Y')";
		
		
		try {
			
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, promotion.getXX_TypePromotion());
			pstmt.setInt(2, promotion.getPriority());
			pstmt.setInt(3, promotion.get_ID());
			rs = pstmt.executeQuery();
			sql = "SELECT XX_VMR_PROMOTION_ID, m.value FROM XX_VMR_DETAILPROMOTIONEXT dp inner join M_Product m on (dp.M_Product_ID = m.M_Product_ID) ";//, M_PRODUCT_ID
			where = "WHERE dp.M_PRODUCT_ID IN (SELECT M_PRODUCT_ID FROM XX_VMR_DETAILPROMOTIONEXT WHERE" +
					" IsActive = 'Y' AND XX_VMR_PROMOTION_ID = "+promotion.get_ID()+") AND ( ";
			while(rs.next()){
				dateFinish2 = rs.getTimestamp("DATEFINISH");
				dateFrom2 = rs.getTimestamp("DATEFROM");
				if((dateFrom2.after(dateFrom)&&dateFrom2.before(dateFinish))
						||(dateFinish2.before(dateFinish)&&dateFinish2.after(dateFrom))
						||(dateFinish2.equals(dateFrom)||dateFinish2.equals(dateFinish)
								||dateFrom2.equals(dateFinish)||dateFrom2.equals(dateFrom))
						||(dateFrom.after(dateFrom2)&&dateFinish.before(dateFinish2))
						){
					if(ret)
						where += " XX_VMR_PROMOTION_ID = "+rs.getInt("XX_VMR_PROMOTION_ID");
					else
						where += " OR XX_VMR_PROMOTION_ID = "+rs.getInt("XX_VMR_PROMOTION_ID");
					ret = false;
				}
			}
			where +=")";
			if (!ret){
				sql = sql + where + "GROUP BY XX_VMR_PROMOTION_ID, m.value";
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				if (!rs.next())
					return true;
				msg += "Algunos productos de la promoción coinciden con otras promociones de misma fecha y prioridad.\n";
				msg += "Solo se mostrará una promoción por Vez con un máximo de 15 detalles.";
				msg += " Revisar promoción con ID: "+rs.getInt("XX_VMR_PROMOTION_ID")+" ...Listado de Productos:\n";
				promoMostrada = rs.getInt("XX_VMR_PROMOTION_ID");
				do{
					msg += ", "+rs.getInt("value");
					detener--;
					if (detener==0 || promoMostrada != rs.getInt("XX_VMR_PROMOTION_ID"))
						break;
				}while(rs.next());
				
				return ret;
				
			}
		}catch (SQLException e) 	{
			e.printStackTrace();
		}finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return true;
	}
	
	public boolean validateSponsoredProducts(X_XX_VMR_Promotion promotion){
		boolean valid = true;
		
		String sql = "SELECT XX_VMR_PromoConditionValue_ID, XX_QuantityPurchase, XX_DiscountRate, XX_DiscountAmount FROM XX_VMR_PromoConditionValue WHERE XX_VMR_Promotion_ID="+promotion.get_ID();
		ResultSet rs=null;
		if(promotion.getXX_TypePromotion().equals(X_Ref_XX_TypePromotion.A3__DESCUENTO_EN_PRODUCTOS_PUBLICADOS.getValue())){
			CPreparedStatement pstmt = DB.prepareStatement(sql, null);
			try {
				rs = pstmt.executeQuery();
				while (rs.next()){
					if(!(rs.getBigDecimal("XX_QuantityPurchase").compareTo(new BigDecimal(0))==1)){
						msg += "Esta promoción requiere monto mínimo de compra mayor a 0.\n";
						valid = false;
					}
					if(!(rs.getBigDecimal("XX_DiscountRate").compareTo(new BigDecimal(0))==1) &&  !(rs.getBigDecimal("XX_DiscountAmount").compareTo(new BigDecimal(0))==1)){
						msg += "Esta promoción requiere porcentaje de descuento o precio final mayor a 0.\n";
						valid = false;
					}
					if(!valid){
						msg += "Revise las condiciones de la promoción.\n";
						break;
					}
				}

			} catch (SQLException e) {
				msg += "Error al validar condiciones. Contactar administrador del sistema.\n";
				return false;
			}
			finally{
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
				
			}
		}
		return valid;
	}
	
	public boolean checkWarehouse(int idPromotion){
		boolean x = true;
		String sql = "SELECT pcv.XX_VMR_PromoConditionValue_ID,XX_Warehousebeconumber  FROM  XX_VMR_PROMOCONDITIONVALUE pcv " +
				"FULL OUTER JOIN XX_VMR_PROMOCONDWAREHOUSE pcw" +
				" ON pcv.XX_VMR_PromoConditionValue_ID=pcw.XX_VMR_PromoConditionValue_ID " +
				" WHERE pcv.XX_VMR_Promotion_ID="+idPromotion+" GROUP BY  pcv.XX_VMR_PromoConditionValue_ID,XX_Warehousebeconumber ";
		String sql2 = "INSERT INTO XX_VMR_PROMOCONDWAREHOUSE "
				+ "(AD_CLIENT_ID, AD_ORG_ID, CREATEDBY, UPDATEDBY, XX_VMR_PROMOCONDITIONVALUE_ID, " +
				"XX_WAREHOUSEBECONUMBER, XX_VMR_PROMOCONDWAREHOUSE_ID)"
				+ " VALUES (?,?,?,?,?,?,?)";
		ResultSet rs=null;
		CPreparedStatement pstmt = DB.prepareStatement(sql, null), pstmt2 = DB.prepareStatement(sql2, null);
		Integer cliente = Env.getCtx().getAD_Client_ID(), 
				org = Env.getCtx().getAD_Org_ID(), 
				user = Env.getCtx().getAD_User_ID(),
				id =  XX_PromoCondWarehouseForm.idPromoCondWarehouse();
		try {
			rs = pstmt.executeQuery();
			while (rs.next()){
				if(rs.getString("XX_Warehousebeconumber")==null){
					pstmt2.setInt(1,cliente);
					pstmt2.setInt(2, org);
					pstmt2.setInt(3, user);
					pstmt2.setInt(4, user);
					pstmt2.setInt(5, rs.getInt(1));
					pstmt2.setInt(6,0);
					pstmt2.setInt(7,id+1);
					pstmt2.addBatch();
				}
			}
			pstmt2.executeBatch();

		} catch (SQLException e) {
			msg += "Error al validar tiendas. Contactar administrador del sistema.\n";
			return false;
		}
		finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
			
		}
		
		
		return x;
	}

	public String getMsg() {

		return msg;
	}
	
}
