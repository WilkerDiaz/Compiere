package compiere.model.dynamic.Callouts;

import java.sql.Timestamp;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.framework.Query;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_C_Campaign;
import compiere.model.dynamic.X_XX_VMA_Brochure;

/**
 * 
 * Esta clase contiene todos los callouts que se ejecutan al momento de
 * aplicar cambios a los campos de una acción de mercadeo.
 * Estos callouts son validaciones dinámicas necesarias.
 * 
 * @author Alejandro Prieto
 * Modificado por: Maria Vintimilla para adaptarlo a CDS
 * @version 1.0
 */

public class XX_VME_MACallout extends CalloutEngine {

	/**
	 * validateEndDate
	 * Se verifica que la fecha en que culmina la acción de mercadeo no sea
	 * antes de la fecha de inicio de la misma.
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
	 * Se verifica que los campos obligatorios para crear una acción de mercadeo
	 * esten rellenados antes de realizar la aprobación de la misma.
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
		
		String activitytype = ctx.getContext(WindowNo, "XX_VMA_ActivityType");
		int brochure = ctx.getContextAsInt(WindowNo, "XX_VMA_Brochure_ID");
		//se verifica que los campos obligatorios tengan valores para poder aprobar
		if(ctx.getContext(WindowNo,"IsApproved").equals("Y")){
			if(ctx.getContext(WindowNo,"Name").equals("")){
				mTab.setValue("IsApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_ActivityName");
			}
			if(ctx.getContext(WindowNo,"StartDate").equals("")){
				mTab.setValue("IsApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_StartDate");
			}
			if(ctx.getContext(WindowNo,"EndDate").equals("")){
				mTab.setValue("IsApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_EndDate");
			}
			if(activitytype.equals("")){
				mTab.setValue("IsApproved", oldValue);
				return Msg.translate(Env.getCtx(), "XX_SelectAction");
			}
			if(activitytype.equals("B")){
				if(ctx.getContext(WindowNo,"XX_VMA_Brochure_ID").equals("")){
					mTab.setValue("IsApproved", oldValue);
					return Msg.translate(Env.getCtx(), "XX_SelectBrochure");
				}
			}
			//Se pregunta al usuario si está seguro de aprobar.
			if(ADialog.ask(WindowNo, null, Msg.translate(Env.getCtx(), "XX_ApproveAction") )){
				mTab.setValue("DocStatus","AP");
				mTab.setValue("XX_VMA_AprobIni", "Y");
				mTab.setValue("XX_VMA_UserAprobIni", new Integer (ctx.getContextAsInt(WindowNo, "UpdatedBy")));
				//una vez que se aprueba inicialmente se pueden empezar a agregar elementos al folleto
				if(activitytype.equals("B")){
					X_XX_VMA_Brochure folleto = new X_XX_VMA_Brochure(ctx,brochure,null);
					folleto.setXX_VMA_MAApproved(true);
					folleto.save();
				}
				mTab.dataSave(true);
			} // fin XX_ApproveAction
			else{
				mTab.setValue("IsApproved","N");
			}
		} // fin IsApproved
		return "";
	} // fin verifyApprovement
	
	/**
	 * startDateInCampaign
	 * Se verifica que la fecha de inicio de la campaña comercial está dentro
	 * de las fechas límites de la campaña comercial a la cual pertenece.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String startDateInCampaign (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){

		if(value!=null){
			int campaign_ID = ctx.getContextAsInt(WindowNo,"C_Campaign_ID");
			//Se extraen los datos de la campaña comercial a la cual pertenece
			X_C_Campaign campaign = new X_C_Campaign(Env.getCtx(),campaign_ID,null);
			Timestamp timeCS = (Timestamp)campaign.get_Value("StartDate");
			Timestamp timeCE = (Timestamp)campaign.get_Value("EndDate");
			long startDateCS = timeCS.getTime();
			long startDateCE = timeCE.getTime();
			long startDateM = ctx.getContextAsTime(WindowNo, "StartDate");

			if(startDateM<startDateCS){
				mTab.setValue("StartDate", null);
				return Msg.translate(Env.getCtx(), "XX_StartCampaign");
			}
			if(startDateM>startDateCE){
				mTab.setValue("StartDate", null);
				return Msg.translate(Env.getCtx(), "XX_StartDateCampaign");
			}
		} // if value
		
		return "";
	} // fin startDateInCampaign
	
	/**
	 * endDateInCampaign
	 * Se verifica que la fecha de culminación de la acción de mercadeo esté dentro
	 * de las fechas límites de la campaña comercial a la cual pertnece.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String endDateInCampaign (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){

		if(value!=null){
			int campaign_ID = ctx.getContextAsInt(WindowNo,"C_Campaign_ID");
			//Se extraen los datos de la campaña comercial a la cual pertenece
			X_C_Campaign campaign = new X_C_Campaign(ctx,campaign_ID,null);
			Timestamp timeCE = (Timestamp)campaign.get_Value("EndDate");
			Timestamp timeCS = (Timestamp)campaign.get_Value("StartDate");
			long startDateCE = timeCE.getTime();
			long startDateCS = timeCS.getTime();
			long startDateM = ctx.getContextAsTime(WindowNo, "EndDate");

			if(startDateCE<startDateM){
				mTab.setValue("EndDate", null);
				return Msg.translate(Env.getCtx(), "XX_EndCampaign");
			}
			if(startDateCS>startDateM){
				mTab.setValue("EndDate", null);
				return Msg.translate(Env.getCtx(), "XX_EndDateCampaign");
			}
		} // if value
		
		return "";
	} // fin endDateInCampaign
	
	/**
	 * showBrochure
	 * Se obliga al usuario a asociar un folleto a la hora de crear una acción
	 * de mercadeo del tipo folleto.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String showBrochure(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
	
		AWindow frame = new AWindow();
		Query query= new Query("XX_VMA_Brochure");
		if(ctx.getContext(WindowNo, "XX_VMA_ActivityType").equals("B")){
			Integer VentanaB = Env.getCtx().getContextAsInt("#XX_L_W_BROCHURE_ID");
			AWindow window_brochure_wc = new AWindow();
	    	Query queryb = Query.getNoRecordQuery("XX_VMA_Brochure", true);
	    	window_brochure_wc.initWindow(VentanaB.intValue(), queryb);
	    	AEnv.showCenterScreen(window_brochure_wc);
		} // if
		else{
			mTab.setValue("XX_VMA_Brochure_ID", null);
		}
		return "";
	} // fin showBrochure
	
	/**
	 * generalReportAvailable
	 * Se verifica si el reporte general ya está disponible para
	 * una acción de mercadeo determinada. Si la diferencia de días
	 * entre el inicio de la acción y la fecha actual es menor o igual
	 * a 60 días.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String generalReportAvailable (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		long startD;
		long sysD;
		long dif;
		if(value!=null){
			startD=ctx.getContextAsTime(WindowNo, "StartDate")/86400000;
			sysD=ctx.getContextAsTime(WindowNo, "#Date")/86400000;
			dif=(startD-sysD);

			if(dif>60){
				mTab.setValue("XX_VMA_ReportType", null);
				return Msg.translate(Env.getCtx(), "XX_ReportAction");
			}
		} // if value
		
		return "";
	} // generalReportAvailable
	
	
	/**
	 * afterGenerateReport
	 * Se coloca el campo de tipo de reporte en null luego de que se
	 * generó el reporte.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String afterGenerateReport (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		mTab.setValue("XX_VMA_ReportType", null);
		return "";
	} // fin afterGenerateReport
	
	/**
	 * finalApprovement
	 * Se aprueba finalmente la acción de mercadeo y se cierran las
	 * posibilidades de cambiar el folleto relacionado a ésta.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String finalApprovement (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		if(ctx.getContext(WindowNo, "XX_VMA_IsActivityApproved").equals("Y")){
			if(ADialog.ask(WindowNo, null, Msg.translate(Env.getCtx(), "XX_ApproveAM"))){
				//una vez que se aprueba finalmente, se coloca el campo XX_VMA_Expired en true para que no se puedan hacer
				//modificaciones sobre la estructura del folleto
				X_XX_VMA_Brochure Brochure = 
					new X_XX_VMA_Brochure(Env.getCtx(),ctx.getContextAsInt(WindowNo, "XX_VMA_Brochure_ID"),null);
				Brochure.setXX_VMA_Expired(true);
				Brochure.save();
				//al colocar el campo XX_VMA_AprobFin en Y se habilita el envío de correos a los usuarios involucrados,
				//notificando que la acción de mercadeo ha sido aprobada para su publicación
				mTab.setValue("XX_VMA_AprobFin", "Y");
				mTab.setValue("XX_VMA_UserAprobFin", new Integer (ctx.getContextAsInt(WindowNo, "UpdatedBy")));
				mTab.dataSave(true);
			}
			else{
				mTab.setValue("XX_VMA_IsActivityApproved","N");
			}
		} // if XX_VMA_IsActivityApproved
		return "";
	} // fin finalApprovement
	
	/**
	 * deactivateBrochure
	 * Se encarga de desactivar los folletos 
	 * asociados. Esto se hace cuando se desactiva la acción
	 * de mercadeo.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String deactivateBrochure (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		Integer folleto;
	
		//primero se verifica si la acción de mercadeo es de tipo folleto
		if(ctx.getContext(WindowNo, "XX_VMA_ActivityType").equals("B")){
			folleto = ctx.getContextAsInt(WindowNo, "XX_VMA_Brochure_ID");
			//luego se verifica de que haya un folleto asociado
			if(folleto!=0){
				if(ctx.getContext(WindowNo, "IsActive").equals("N")){
					// se procede a desactivar el folleto, así como las páginas 
					// y elementos asociados al mismo
					XX_VME_GeneralFunctions.processBrochure(folleto, false, true);
				} // if active
				else {
					XX_VME_GeneralFunctions.processBrochure(folleto, true, true);
				}
			} // if folleto
				
		} // if activity B
			
		return "";
	} // fin deactivateBrochure
	
	/**
	 * desaprobarAccionM
	 * Este callout se encarga de desaprobar una acción de mercadeo, en el caso 
	 * de que ésta sea un folleto
	 * se cierra la posibilidad de seguir agregando elementos al mismo.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String desaprobarAccionM(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		if(ctx.getContext(WindowNo, "XX_VMA_NotApproved").equals("Y")){
			if(ADialog.ask(WindowNo, null, Msg.translate(Env.getCtx(), "XX_DisapproveAction"))){
				//se desaprueba tanto la aprobación final como la aprobación inicial
				mTab.setValue("XX_VMA_AprobFin", "N");
				mTab.setValue("XX_VMA_IsActivityApproved", "N");
				mTab.setValue("XX_VMA_AprobIni", "N");
				mTab.setValue("IsApproved","N");
				mTab.setValue("DocStatus","??");
				mTab.setValue("XX_VMA_NotApproved","N");
				//mTab.setValue("XX_VMA_UserAprobFin", new Integer (ctx.getContextAsInt(WindowNo, "UpdatedBy")));
				mTab.dataSave(true);
				//se cierra la posibilidad de agregar elementos al folleto
				if(ctx.getContext(WindowNo, "XX_VMA_ActivityType").equals("B")){
					X_XX_VMA_Brochure Brochure = 
						new X_XX_VMA_Brochure(Env.getCtx(),ctx.getContextAsInt(WindowNo, "XX_VMA_Brochure_ID"),null);
					Brochure.setXX_VMA_Expired(false);
					Brochure.setXX_VMA_MAApproved(false);
					Brochure.save();
				} // if XX_VMA_ActivityType
				mTab.dataSave(true);
			} // if XX_DisapproveAction
			else{
				mTab.setValue("XX_VMA_NotApproved","N");
			}
		} // if XX_VMA_NotApproved
		
		return "";
	} // fin desaprobarAccionM
	
	
	/*
	public String actualizarCostoCampana(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		double ov,nv;
		ov=((Double)oldValue).doubleValue();
		nv=((Double)value).doubleValue();
		
		X_XX_VMA_MarketingActivity m = new X_XX_VMA_MarketingActivity(ctx,ctx.getContextAsInt("X_XX_VMA_MarketingActivity"),null);
		m.get
		
		return "";
	}*/
	/*
	public String publicationAlert (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		Calendar sysdate = Calendar.getInstance();
		long startD = ctx.getContextAsTime(WindowNo, "StartDate");
		long sysD = sysdate.getTime().getTime();
		
		System.out.println("fecha de inicio  ............."+startD);
		System.out.println("fecha de inicio  ............."+sysD);
		
		return "";
	}*/
	
	/*public String agregarMedio(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		//X_XX_VMA_MediaType medio = new X_XX_VMA_MediaType(ctx, 0, null);
		//int canal = (()value);
		System.out.println("el canal es : "+value);
		//boolean bool = true;
		if(canal!=0){
			System.out.println("paso ................. 1");
			medio.setXX_VMA_MarketingActivity_ID(ctx.getContextAsInt(WindowNo, "XX_VMA_MarketingActivity_ID"));
			System.out.println("paso ................. 2");
			medio.setC_Channel_ID(canal);
			System.out.println("paso ................. 3");
			bool= medio.save();
			System.out.println("paso ................. 4");
			if(!bool){
				return "no se asoció el medio de publicación de manera correcta";
			}
			System.out.println("paso ................. 5");
			mTab.setValue("C_Channel_ID", 0);
		}
		return "";
	}*/
} // Fin XX_VMA_MarketingActivityCallout
