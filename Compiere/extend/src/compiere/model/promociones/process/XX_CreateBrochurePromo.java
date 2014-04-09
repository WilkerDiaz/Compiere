package compiere.model.promociones.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.dynamic.MVMABrochure;
import compiere.model.dynamic.MVMABrochurePage;
import compiere.model.dynamic.MVMEElements;
import compiere.model.dynamic.X_XX_VME_Product;
import compiere.model.dynamic.X_XX_VME_Reference;
import compiere.model.promociones.MVMRDetailPromotionExt;
import compiere.model.promociones.X_Ref_XX_OBJETICOCOMERCIAL;
import compiere.model.promociones.X_Ref_XX_OBJETIVOTACTICO;
import compiere.model.promociones.X_Ref_XX_TypePromotion;
import compiere.model.promociones.X_XX_VMR_Promotion;
import compiere.model.promociones.MVMRPromoConditionValue;


/** Proceso que genera las promociones relacionadas con un determinado folleto, 
 * según las referencias de proveedores susceptibles a promoción publicadas en el mismo.
 * @author ghuchet*/

public class XX_CreateBrochurePromo extends SvrProcess {

    private Timestamp start = null;
    private Timestamp end = null;
    private MVMABrochure brochure = null;
	private int brochure_ID = 0;
    private Trx trx = null;
	private ArrayList<Integer> promoHeader_ID = new ArrayList<Integer>();
    
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("XX_VMA_Brochure_ID")) {
				brochure_ID = element.getParameterAsInt();
			}else log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}
	}

	@Override
	protected String doIt() throws Exception {
		boolean promoCreated =  false;
		brochure = new MVMABrochure(getCtx(), brochure_ID, null);

		getBrochureDates(brochure.get_ID());
		if(start == null || end == null ) {
			return "Parametros para la Promoción Incompletos, Comuniquese con Soporte";
		}
		
		//Para cada pagina del folleto se crea una promoción  (excepto para los de tipo imagen)
		String sql = " SELECT bp.XX_VMA_BrochurePage_ID FROM XX_VMA_BrochurePage bp WHERE bp.XX_VMA_Brochure_ID = " + brochure.get_ID()+
				" AND bp.XX_VMA_PAGETYPE <> 'I' AND   EXISTS (SELECT  e.XX_VME_Elements_ID FROM XX_VME_Elements e WHERE e.XX_VMA_BrochurePage_ID = bp.XX_VMA_BrochurePage_ID " +
						" AND e.XX_VME_TYPE <> 'I')";

		PreparedStatement ps = null;
		ResultSet rs = null;
		int brochurePage_ID = 0;
		try{
			ps = DB.prepareStatement(sql, null);
		    rs = ps.executeQuery();

			while (rs.next()){
				brochurePage_ID = rs.getInt(1);
				if(!existsPagPromo(brochurePage_ID)){
					createPagPromo(brochurePage_ID);
					promoCreated = true;
				}
	
			}		
		}catch (Exception e) {
			//rollback();
			deleteAll();
			e.printStackTrace();
			return  "Error al intentar crear promociones.";
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}	
		commit();
		
		if(promoCreated){
			return "Se crearon las promociones asociadas al folleto.";
		}else return "El folleto tiene asociada una promoción para cada página. Si desea sobreescribir las promociones debe borrar las mismas primero.";
	}
	
	private boolean existsPagPromo(int brochurePage_ID) {
		String sql = " SELECT XX_VMR_Promotion_ID FROM XX_VMR_Promotion WHERE XX_VMA_BrochurePage_ID = " + brochurePage_ID;
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = DB.prepareStatement(sql, null);
		    rs = ps.executeQuery();

			while (rs.next()){
				return true;
			}		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return false;
	}

	private void deleteAll() throws Exception {
		
		for (int i = 0; i < promoHeader_ID.size(); i++) {
			int promoID = promoHeader_ID.get(i);
			deleteDetails(promoID);
			deleteConditions(promoID);
			deleteHeader(promoID);
			System.out.println("Borrando detalle de promo "+promoID);
		}
			
	}

	private void deleteDetails(int promoID) throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VMR_DetailPromotionExt WHERE XX_VMR_Promotion_ID = " +promoID;
	
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error borrando los detalle de la promoción " +e.getMessage());
		}finally{
			psDelete.close();
		}
		
	}
	
	private void deleteConditions(int promoID) throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VMR_PromoConditionValue WHERE XX_VMR_Promotion_ID = " +promoID;
	
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error borrando las condiciones de la promoción " +e.getMessage());
		}finally{
			psDelete.close();
		}
		
	}
	
	private void deleteHeader(int promoID) throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VMR_Promotion WHERE XX_VMR_Promotion_ID = " +promoID;
	
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error borrando cabecera de la promoción " +e.getMessage());
		}finally{
			psDelete.close();
		}
		
	}


	private void createPagPromo(int brochurePage_ID) throws Exception {
		
		MVMABrochurePage brochurePage = new MVMABrochurePage(getCtx(), brochurePage_ID, null);
		
		X_XX_VMR_Promotion promoHeader = new X_XX_VMR_Promotion(getCtx(), 0,  trx);
		
		promoHeader.setXX_TacticalObjective(X_Ref_XX_OBJETIVOTACTICO.AUMENTO_DEL_TICKET_PROMEDIO.getValue());
		promoHeader.setXX_CommercialObjetive(X_Ref_XX_OBJETICOCOMERCIAL.PROMOCIONES_DE_TEMPORADAS_COMERCIALES.getValue());
		promoHeader.setXX_TypePromotion(X_Ref_XX_TypePromotion.E3__PROMOCIONES_CLÁSICAS.getValue());
		promoHeader.setName(brochure.getName()+" - "+brochurePage.getName());
		promoHeader.setDescription(brochurePage.getDescription());
		promoHeader.setPriority(1);
		promoHeader.setDateFrom(start);
		promoHeader.setDateFinish(end);
		promoHeader.set_Value("XX_VMA_Brochure_ID", brochure_ID);
		promoHeader.set_Value("XX_VMA_BrochurePage_ID", brochurePage_ID);
		promoHeader.save();
		promoHeader_ID.add(promoHeader.get_ID());
		//Para cada elemento de una página del folleto se crea una condición de promoción (excepto para los de tipo imagen)
		String sql = " SELECT XX_VME_Elements_ID FROM XX_VME_Elements WHERE XX_VMA_BrochurePage_ID = " + brochurePage.get_ID()+
				" AND XX_VME_TYPE <> 'I'";
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		int elements_ID = 0;
		try{
			ps = DB.prepareStatement(sql, null);
		    rs = ps.executeQuery();

			while (rs.next()){
				elements_ID = rs.getInt(1);
				createElemPromo(elements_ID, promoHeader);
			}			
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}	
	}

	private void createElemPromo(int elements_ID, X_XX_VMR_Promotion promoHeader) throws Exception {
	
		MVMEElements elements = new MVMEElements(getCtx(), elements_ID, null);
		
		//Para cada referencia de un elemento de una página del folleto se crea una detalle de promoción 
		//(en caso de no tener el check de mantener, el detalle se crea por producto)
		String sql = " SELECT XX_VME_Reference_ID FROM XX_VME_Reference WHERE XX_VME_Elements_ID = " + elements.get_ID();
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		int reference_ID = 0;
		boolean refExempt = false;
		int promoCondition_ID = 0;
		try{
			ps = DB.prepareStatement(sql, null);
		    rs = ps.executeQuery();

			while (rs.next()){
				reference_ID = rs.getInt(1);
				X_XX_VME_Reference reference =  new X_XX_VME_Reference(getCtx(), reference_ID, trx);
				refExempt = isExempt(reference_ID);
				promoCondition_ID = getPromoCondition(elements, promoHeader, refExempt);
				MVMRPromoConditionValue promoCondition = new MVMRPromoConditionValue(getCtx(),promoCondition_ID, trx);
				if(reference.isXX_VME_Mantain()){
					//Para esta referencia del folleto se crea un detalle de promoción
					createRefPromo(reference, promoCondition, promoHeader);
				}else {
					//Para cada producto de una referencia  del folleto se crea un de detalle de promoción
					String sql2 = " SELECT XX_VME_Product_ID FROM XX_VME_Product WHERE XX_VME_Reference_ID = " + reference.get_ID();
							
					PreparedStatement ps2 = null;
					ResultSet rs2 = null;
					int product_ID = 0;
					int i = 0;
					try{
						ps2 = DB.prepareStatement(sql2, null);
					    rs2 = ps2.executeQuery();

						while (rs2.next()){
							product_ID = rs2.getInt(1);
							createProdPromo(product_ID, promoCondition, promoHeader);
							i++;
						}	
					}catch (Exception e) {
						e.printStackTrace();
					}finally{
						DB.closeResultSet(rs2);
						DB.closeStatement(ps2);
					}	
					//Si la referencia no tiene productos asociados se crea el detalle de promoción por referencia
					if(i==0){
						//Para esta referencia del folleto se crea un detalle de promoción
						createRefPromo(reference, promoCondition, promoHeader);
					}
				}
			}			
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}	
	}

	private boolean isExempt(int reference_ID) {
		
		//Se verifica si existen referencia exentas de IVA 
		String sql = " SELECT R.XX_VME_Reference_ID " +
				" FROM XX_VME_Reference R JOIN  XX_VMR_VendorProdRef PR ON (P.XX_VMR_VendorProdRef_ID =PR.XX_VMR_VendorProdRef_ID) " +
				" WHERE R.XX_VME_Reference_ID  = " + reference_ID+ 
				" AND PR.C_TaxCategory_ID = " + Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID");	
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = DB.prepareStatement(sql, null);
		    rs = ps.executeQuery();

			if (rs.next()){
				return true;
			}	
		}catch (Exception e) {
				// TODO: handle exception
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}	
		return false;
	}

	private void createProdPromo(int product_ID, MVMRPromoConditionValue promoCondition, X_XX_VMR_Promotion promoHeader) {
		
		X_XX_VME_Product product = new X_XX_VME_Product(getCtx(),product_ID, trx);
		
		MVMRDetailPromotionExt detailPromo = new MVMRDetailPromotionExt(getCtx(), 0, trx);
		
		detailPromo.setM_Product_ID(product.getM_Product_ID());
		detailPromo.setXX_VMR_VendorProdRef_ID(product.getXX_VMR_VendorProdRef_ID());
		detailPromo.setXX_VMR_Category_ID(product.getXX_VMR_Category_ID());
		detailPromo.setXX_VMR_Department_ID(product.getXX_VMR_Department_ID());
		detailPromo.setXX_VMR_Section_ID(product.getXX_VMR_Section_ID());
		detailPromo.setXX_VMR_Promotion_ID(promoCondition.getXX_VMR_Promotion_ID());
		detailPromo.setXX_VMR_PromoConditionValue_ID(promoCondition.getXX_VMR_PromoConditionValue_ID());
		if(promoCondition.getXX_DiscountAmount()!=null && promoCondition.getXX_DiscountAmount().compareTo(new BigDecimal(0))==1){
			detailPromo.setXX_DiscountAmount(promoCondition.getXX_DiscountAmount());
		}else if (promoCondition.getXX_DiscountRate()!=null && promoCondition.getXX_DiscountRate().compareTo(new BigDecimal(0))==1){
			detailPromo.setXX_DiscountRate(promoCondition.getXX_DiscountRate());
		}
		detailPromo.save();
	}

	private void createRefPromo(X_XX_VME_Reference reference, MVMRPromoConditionValue promoCondition, X_XX_VMR_Promotion promoHeader) {
		
		MVMRDetailPromotionExt detailPromo = new MVMRDetailPromotionExt(getCtx(), 0, trx);
		
		detailPromo.setXX_VMR_VendorProdRef_ID(reference.getXX_VMR_VendorProdRef_ID());
		detailPromo.setXX_VMR_Category_ID(reference.getXX_VMR_Category_ID());
		detailPromo.setXX_VMR_Department_ID(reference.getXX_VMR_Department_ID());
		detailPromo.setXX_VMR_Section_ID(reference.getXX_VMR_Section_ID());
		detailPromo.setXX_VMR_Promotion_ID(promoCondition.getXX_VMR_Promotion_ID());
		detailPromo.setXX_VMR_PromoConditionValue_ID(promoCondition.getXX_VMR_PromoConditionValue_ID());
		if(promoCondition.getXX_DiscountAmount()!=null && promoCondition.getXX_DiscountAmount().compareTo(new BigDecimal(0))==1){
			detailPromo.setXX_DiscountAmount(promoCondition.getXX_DiscountAmount());
		}else if (promoCondition.getXX_DiscountRate()!=null && promoCondition.getXX_DiscountRate().compareTo(new BigDecimal(0))==1){
			detailPromo.setXX_DiscountRate(promoCondition.getXX_DiscountRate());
		}
		detailPromo.save();
	}

	private int getPromoCondition(MVMEElements elements, X_XX_VMR_Promotion promoHeader, boolean isExempt)  throws Exception {
	
		int promoConditionValue_ID = 0;
		
		//Se busca si existe una condicion de promoción que cumpla con el descuento dado al elemento del 
		String sql = " SELECT XX_VMR_PromoConditionValue_ID " +
				" FROM XX_VMR_PromoConditionValue" +
				" WHERE XX_VMR_Promotion_ID = "+promoHeader.get_ID();
				if(elements.getXX_VME_PromoDynPrice().compareTo(elements.getXX_VME_DynamicPrice()) != 0) {
				sql += 	" AND XX_DiscountAmount = " + getPriceWithOutIva(elements.getXX_VME_PromoDynPrice());
				}else sql += " AND XX_DiscountRate <> 0 AND XX_DiscountRate = " + elements.getXX_VME_DiscountPercentage();
				
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			ps = DB.prepareStatement(sql, null);
		    rs = ps.executeQuery();

			if(rs.next()){
				promoConditionValue_ID = rs.getInt(1);
			}			
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}	

		//Si la condición no existe para la promoción se crea
		if(promoConditionValue_ID == 0){
			MVMRPromoConditionValue promoCondition = new MVMRPromoConditionValue(getCtx(),0, trx);
			promoCondition.setXX_VMR_Promotion_ID(promoHeader.get_ID());
			if(elements.getXX_VME_PromoDynPrice()!=null && elements.getXX_VME_PromoDynPrice().compareTo(new BigDecimal(0))==1){
				BigDecimal pricePromo =new BigDecimal(0);
				if(!isExempt) {
					pricePromo =  getPriceWithOutIva(elements.getXX_VME_PromoDynPrice());
				}else {
					pricePromo = elements.getXX_VME_PromoDynPrice();
				}
				promoCondition.setXX_DiscountAmount(pricePromo);
			}else if (elements.getXX_VME_DiscountPercentage()!=null && elements.getXX_VME_DiscountPercentage().compareTo(new BigDecimal(0))==1){
				promoCondition.setXX_DiscountRate(elements.getXX_VME_DiscountPercentage());
			}
			promoCondition.save();
			promoConditionValue_ID = promoCondition.get_ID();
		}
		
		return promoConditionValue_ID;
	}

	//Obtiene precio promocional sin IVA
	private BigDecimal getPriceWithOutIva(BigDecimal price) {

		BigDecimal result = new BigDecimal(0);
		//Calcular el impuesto		
		String sql = "SELECT rate"
					+ " FROM C_Tax"
					+ " WHERE ValidFrom="			
					+ " (SELECT MAX(ValidFrom)"											
					+ " FROM C_Tax"
					+ " WHERE C_TaxCategory_ID=" + Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_IVA_ID")+")";	
		PreparedStatement prst = DB.prepareStatement(sql,null);
		BigDecimal tax = new BigDecimal(1);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				tax = rs.getBigDecimal("rate");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){}
		
		tax = tax.multiply(new BigDecimal("0.01"));

		result = price.divide((tax.add(new BigDecimal(1))),2, BigDecimal.ROUND_HALF_UP);
		//result = result.setScale(2,BigDecimal.ROUND_HALF_UP);
		return result;
	}

	/**Obtiene fecha inicial y final del folleto que se encuentra en la tabla de Acción de Mercadeo (XX_VMA_MarketingActivity)*/
	private void getBrochureDates(int brochure_ID)  throws Exception {
	
		String sql = " SELECT StartDate, EndDate FROM  XX_VMA_MarketingActivity WHERE XX_VMA_Brochure_ID = " + brochure_ID;
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = DB.prepareStatement(sql, null);
		    rs = ps.executeQuery();

			while (rs.next()){
				start = rs.getTimestamp("StartDate");
				end = rs.getTimestamp("EndDate");
			}			
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}		
	}

}
