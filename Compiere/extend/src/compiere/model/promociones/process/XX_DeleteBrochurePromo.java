package compiere.model.promociones.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.dynamic.MVMABrochure;

/** Proceso que borra las promociones relacionadas con un determinado folleto, 
 * siempre y cuando las mismas no esten aprobadas
 * @author ghuchet*/

public class XX_DeleteBrochurePromo extends SvrProcess {

    private MVMABrochure brochure = null;
	private int brochure_ID = 0;
	
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
		brochure = new MVMABrochure(getCtx(), brochure_ID, null);
		deleteAll();
		return "";
	}

	private void deleteAll() throws Exception {
		//Se borran las promociones asociadas al folleto que no esten aprobadas
				String sql = " SELECT XX_VMR_Promotion_ID FROM XX_VMR_Promotion WHERE " +
						" XX_ApproveMer = 'N' AND XX_ApproveMar = 'N' AND XX_VMA_Brochure_ID = " + brochure.get_ID();
						
				PreparedStatement ps = null;
				ResultSet rs = null;
				int promoID = 0;
				try{
					ps = DB.prepareStatement(sql, null);
				    rs = ps.executeQuery();

					while (rs.next()){
						promoID = rs.getInt(1);
						deleteDetails(promoID);
						deleteConditions(promoID);
						deleteHeader(promoID);
						System.out.println("Borrando detalle de promo "+promoID);
					}		
				}catch (Exception e) {
					e.printStackTrace();
				}finally{
					DB.closeResultSet(rs);
					DB.closeStatement(ps);
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


}
