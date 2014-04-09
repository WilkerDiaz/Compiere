package compiere.model.dynamic.Callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_C_Campaign;
import compiere.model.dynamic.X_XX_VMA_Brochure;
import compiere.model.dynamic.X_XX_VMA_MarketingActivity;


/**
 * 
 * Esta clase contiene todos los callouts que se ejecutan al momento de
 * aplicar cambios a los campos de una temporada.
 * Estos callouts son validaciones dinámicas necesarias.
 * 
 * @author Alejandro Prieto
 * @version 1.0
 */

public class XX_VME_SeasonCallout extends CalloutEngine{
	
	/**
	 * validateEndDate
	 * Se valida que la fecha de culminación de la temporada no sea
	 * menor que la fecha de inicio de la misma.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String validateEndDate  (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		/*
		if(ctx.getContext(WindowNo,"EndDate").equals("")){
			return "";
		}*/
		if(ctx.getContextAsTime(WindowNo,"EndDate") < ctx.getContextAsTime(WindowNo, "StartDate")){
			mTab.setValue("EndDate",null);
			return Msg.translate(Env.getCtx(), "XX_EndStartDate");
		}
		return "";
	} // fin validateEndDate
	
	/**
	 * verifyApprovement
	 * Revisa que los campos olbigatorios están completados antes de
	 * realizar la aprobación de la temporada.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String verifyApprovement (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		//se verifica que los campos obligatorios tengan valores antes de aprobar
		if(ctx.getContext(WindowNo,"XX_VMA_IsSeasonApproved").equals("Y")){
			if(ctx.getContext(WindowNo,"Name").equals("")){
				mTab.setValue("XX_VMA_IsSeasonApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_SeasonName");
			}
			if(ctx.getContext(WindowNo,"StartDate").equals("")){
				mTab.setValue("XX_VMA_IsSeasonApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_StartDate");
			}
			if(ctx.getContext(WindowNo,"EndDate").equals("")){
				mTab.setValue("XX_VMA_IsSeasonApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_EndDate");
			}
			//Se pregunta al usuario si está seguro de aprobar.
			if(ADialog.ask(WindowNo, null, Msg.translate(Env.getCtx(), "XX_ApproveSeason"))){
				mTab.setValue("DocStatus","AP");
				mTab.dataSave(true);
			}
			else{
				mTab.setValue("XX_VMA_IsSeasonApproved","N");
			}
		} // if season approved
		return "";
	} // fin verifyApprovement
	
	/**
	 * deactivateMarketingC
	 * Se encarga de desactivar las campañas comerciales y acciones de mercadeo 
	 * asociadas. Esto se hace cuando se desactiva la temporada.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String deactivateMarketingC (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){

		Integer season=ctx.getContextAsInt(WindowNo, "XX_VMA_Season_ID");
		
		//se verifica que la temporada exista
		if (season !=0){
			if(ctx.getContext(WindowNo, "IsActive").equals("N")){
				XX_VME_GeneralFunctions.processSeason(season, false, true);
			}// if active
			else {
				XX_VME_GeneralFunctions.processSeason(season, true, true);
			}
		} // if season

		return "";
	} // fin deactivateMarketingC
	
	
	/**
	 * desaprobarTemporada
	 * Esta función permite desaprobar una temporara, y en consecuencia las 
	 * campañas comerciales y acciones
	 * de mercadeo asociadas.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String desaprobarTemporada(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//query para encontrar las campañas comerciales a desaprobar
		String sql = " select c_campaign_id " +
				" from c_campaign " +
				" where xx_vma_season_id = " +
				" "+ctx.getContextAsInt(WindowNo, "XX_VMA_Season_ID")+
				" and xx_vma_IsCampaignApproved = 'Y'";
		// System.out.println("SQL:"+sql);
		if(ctx.getContext(WindowNo, "XX_VMA_NotApproved").equals("Y")){
			if(ADialog.ask(WindowNo, null, Msg.translate(Env.getCtx(), "XX_DisapproveSeason"))){
				//se desaprueba la temporada
				mTab.setValue("XX_VMA_IsSeasonApproved", "N");
				mTab.setValue("DocStatus","??");
				mTab.setValue("XX_VMA_NotApproved","N");
				mTab.dataSave(true);
				try{
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();
					while(rs.next()){
						//se desaprueba la camapaña comercial asociada
						X_C_Campaign campana = 
							new X_C_Campaign(Env.getCtx(),rs.getInt(1),null);
						campana.setXX_VMA_IsCampaignApproved(false);
						campana.setDocStatus("??");
						campana.save();
					} // while
					rs.close();
					pstmt.close();
					// query para buscar las acciones de mercadeo relacionadas 
					// con la campaña comercial
					// que se desaprobó
					sql=" select xx_vma_marketingactivity_id " +
							" from xx_vma_marketingactivity " +
							" where c_campaign_id = " +
							ctx.getContextAsInt(WindowNo, "C_Campaign_ID")+
							" and IsApproved='Y'";
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();
					while(rs.next()){
						//se desaprueba la acción de mercadeo asociada
						X_XX_VMA_MarketingActivity accion = 
							new X_XX_VMA_MarketingActivity(Env.getCtx(),rs.getInt(1),null);
						accion.setIsApproved(false);
						accion.setXX_VMA_AprobIni(false);
						if(accion.isXX_VMA_IsActivityApproved())
							accion.setXX_VMA_IsActivityApproved(false);
						accion.setDocStatus("??");
						accion.save();
						// En caso de que la acción de mercadeo sea de tipo 
						// folleto, se deshabilita la posibilidad de poder 
						// agregar más elementos o realizar modificaciones 
						// sobre el mismo
						if(accion.getXX_VMA_ActivityType().equals("B")){
							X_XX_VMA_Brochure folleto = 
								new X_XX_VMA_Brochure(Env.getCtx(),accion.getXX_VMA_Brochure_ID(),null);
							folleto.setXX_VMA_MAApproved(false);
							folleto.save();
						} // if activity B
					} // while
				} // try
				catch(Exception e){ e.printStackTrace(); }
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
			} // if window
			else{
				mTab.setValue("XX_VMA_NotApproved","N");
			}
		} // if NotApproved
		
		return "";
	} // fin desaprobarTemporada
	
} // fin XX_VMA_SeasonCallout
