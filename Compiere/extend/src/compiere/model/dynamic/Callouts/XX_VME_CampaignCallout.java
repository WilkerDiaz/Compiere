package compiere.model.dynamic.Callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_XX_VMA_Brochure;
import compiere.model.dynamic.X_XX_VMA_MarketingActivity;
import compiere.model.dynamic.X_XX_VMA_Season;

/**
 * 
 * Esta clase contiene todos los callouts que se ejecutan al momento de
 * aplicar cambios a los campos de una campaña comercial.
 * Estos callouts son validaciones dinámicas necesarias.
 * 
 * @author Alejandro Prieto
 * @author mvintimilla Actualizacion para su adaptacion a CDS
 * 
 * @version 1.0
 */

public class XX_VME_CampaignCallout extends CalloutEngine {

	/**
	 * validateEndDate
	 * Se valida que la fecha de culminación de la campaña comercial no sea
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
		
		if(ctx.getContext(WindowNo,"EndDate").equals("")){
			return "";
		}
		if(ctx.getContextAsTime(WindowNo,"EndDate") < ctx.getContextAsTime(WindowNo, "StartDate")){
			mTab.setValue("EndDate",null);
			return Msg.translate(Env.getCtx(), "XX_DateEnd");
		}
		return "";
	} // fin validateEndDate
	
	/**
	 * verifyApprovement
	 * Revisa que los campos olbigatorios están completados antes de
	 * realizar la aprobación de la campaña comercial.
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
		
		//se verifican que los campos obligatorios tengan valores antes de poder aprobar
		if(ctx.getContext(WindowNo,"XX_VMA_IsCampaignApproved").equals("Y")){
			if(ctx.getContext(WindowNo,"Name").equals("")){
				mTab.setValue("XX_VMA_IsCampaignApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_CampaignName");
			}
			if(ctx.getContext(WindowNo,"StartDate").equals("")){
				mTab.setValue("XX_VMA_IsCampaignApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_StartDate");
			}
			if(ctx.getContext(WindowNo,"EndDate").equals("")){
				mTab.setValue("XX_VMA_IsCampaignApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_EndDate");
			}
			//Se pregunta al usuario si está seguro de aprobar.
			if(ADialog.ask(WindowNo, null, Msg.translate(Env.getCtx(), "XX_DeleteCampaign"))){
				mTab.setValue("DocStatus","AP");
				mTab.dataSave(true);
			}else{
				mTab.setValue("XX_VMA_IsCampaignApproved","N");
			}
		} // if campaign approved
		return "";
	} // Fin verifyApprovement
	
	/**
	 * startDateInSeason
	 * Se verifica que la fecha de inicio de la campaña comercial
	 * este dentro de las fechas que limitan la temporada a la cual 
	 * pertenece.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String startDateInSeason (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){

		if(value!=null){
			int season_ID = ctx.getContextAsInt(WindowNo,"XX_VMA_Season_ID");
			/* Se buscan los datos de la temporada a la cual pertenece la 
			 *  campaña comercial */
			X_XX_VMA_Season season = new X_XX_VMA_Season(ctx,season_ID,null);
			Timestamp timeSS = (Timestamp)season.get_Value("StartDate");
			Timestamp timeSE = (Timestamp)season.get_Value("EndDate");
			long startDateSS = timeSS.getTime();
			long startDateSE = timeSE.getTime();
			long startDateC = ctx.getContextAsTime(WindowNo, "StartDate");
			
			//se verifica que la fecha de inicio este dentro de las fechas de 
			// inicio y fin de la temporada
			if(startDateC<startDateSS){
				mTab.setValue("StartDate", null);
				return Msg.translate(Env.getCtx(), "XX_StartSeason");
			}
			if(startDateC>startDateSE){
				mTab.setValue("StartDate", null);
				return Msg.translate(Env.getCtx(), "XX_StartDateSeason");
			}
		} // if value 
		return "";
	} // Fin startDateInSeason
	
	/**
	 * endDateInSeason
	 * Se verifica que la fecha en que culmina la campaña comercial este 
	 * dentro de los límites de las fechas de la temporada a la cual 
	 * pertenece.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String endDateInSeason (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){

		if(value!=null){
			int season_ID = ctx.getContextAsInt(WindowNo,"XX_VMA_Season_ID");
			/* Se buscan los datos de la temporada a la cual pertenece la campaña
			comercial */
			X_XX_VMA_Season season = new X_XX_VMA_Season(ctx,season_ID,null);
			Timestamp timeEE = (Timestamp)season.get_Value("EndDate");
			Timestamp timeES = (Timestamp)season.get_Value("StartDate");
			long endDateSE = timeEE.getTime();
			long endDateSS = timeES.getTime();
			long endDateC = ctx.getContextAsTime(WindowNo, "EndDate");

			/* Se verifica que la fecha de finalización este dentro de las fechas 
			de inicio y fin de la temporada */
			if(endDateC>endDateSE){
				mTab.setValue("EndDate", null);
				return Msg.translate(Env.getCtx(), "XX_EndSeason");
			}
			if(endDateC<endDateSS){
				mTab.setValue("EndDate", null);
				return Msg.translate(Env.getCtx(), "XX_EndDateSeason");
			}
		}// if value
		
		return "";
	} // Fin endDateInSeason
	
	/**
	 * deactivateMarketingA
	 * Se encarga de desactivar las acciones de mercadeo 
	 * asociadas. Esto se hace cuando se desactiva la campaña
	 * comercial.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String deactivateMarketingA (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){

		Integer campana = ctx.getContextAsInt(WindowNo, "C_Campaign_ID");

		// Se comprueba que existe la campaña comercial para poder ejecutar 
		// dicho callout 
		if(campana!=0){
			if(ctx.getContext(WindowNo, "IsActive").equals("N")){
				XX_VME_GeneralFunctions.processCampaign (campana, false, true);
			} // if active
			else {
				XX_VME_GeneralFunctions.processCampaign (campana, true, true);
			} // else
		} // campaña
		return "";
	} // Fin deactivateMarketingA
	
	
	/**
	 * desaprobarCampanaC
	 * Se desaprueba la campaña comercial para que se puedan modificar datos
	 * como las fechas de inicio y fin, el nombre, entre otros.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String desaprobarCampanaC(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//query para encontrar las acciones de mercadeo asociadas a la campaña 
		//comercial
		String sql = " Select xx_vma_marketingactivity_id " +
				" From xx_vma_marketingactivity " +
				" Where c_campaign_id = " +
				ctx.getContextAsInt(WindowNo, "C_Campaign_ID") + 
				" and IsApproved = 'Y'";
		if(ctx.getContext(WindowNo, "XX_VMA_NotApproved").equals("Y")){
			//se pregunta al usuario si esta seguro de desaprobar la campaña 
			//comercial
			if(ADialog.ask(WindowNo, null, Msg.translate(Env.getCtx(), "XX_DelCampaign"))){
				//se desaprueba la campaña comercial
				mTab.setValue("XX_VMA_IsCampaignApproved", "N");
				mTab.setValue("DocStatus","??");
				mTab.setValue("XX_VMA_NotApproved","N");
				mTab.dataSave(true);
				try{
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();
					/* se recorren las acciones de mercadeo asociadas a la 
					campaña para desaprobarlas de igual manera */
					while(rs.next()){
						X_XX_VMA_MarketingActivity accion = 
							new X_XX_VMA_MarketingActivity(Env.getCtx(),rs.getInt(1),null);
						//se desaprueba la acción de mercadeo asociada
						accion.setIsApproved(false);
						accion.setXX_VMA_AprobIni(false);
						if(accion.isXX_VMA_IsActivityApproved())
							accion.setXX_VMA_IsActivityApproved(false);
						accion.setDocStatus("??");
						accion.save();
						/* se bloquea la posibilidad de agregar elementos a las 
						acciones de mercadeo asociadas, que sean de tipo folleto */
						if(accion.getXX_VMA_ActivityType().equals("B")){
							X_XX_VMA_Brochure folleto = 
								new X_XX_VMA_Brochure(Env.getCtx(),accion.getXX_VMA_Brochure_ID(),null);
							folleto.setXX_VMA_MAApproved(false);
							folleto.save();
						}// if folleto
					} // while
				} // try
				catch(Exception e){ e.printStackTrace();}
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				} // finally
			} // if del campaign
			else{
				mTab.setValue("XX_VMA_NotApproved","N");
			}
		} // if notapproved
		
		return "";
	} // Fin desaprobarCampanaC
	
} // Fin XX_VMA_CampaignCallout
