package compiere.model.dynamic.Callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.XX_VME_GeneralFunctions;

/**
 * 
 * Esta clase contiene todos los callouts que se ejecutan al momento de
 * aplicar cambios a los campos de una página perteneciente a un folleto.
 * Estos callouts son validaciones dinámicas necesarias.
 * 
 * @author Alejandro Prieto
 * Modificado por: Maria Vintimilla para adaptarlo a CDS
 * @version 1.0
 */

public class XX_VME_BrochurePageCallout extends CalloutEngine {
	
	/**
	 * isEnablePage
	 * Se revisa que la página esté asociado a un folleto, de lo contrario
	 * no se crea.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String isEnablePage (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		if(ctx.getContext(WindowNo, "XX_VMA_Brochure_ID").equals("")){
			ADialog.info(WindowNo, null, Msg.translate(Env.getCtx(), "XX_Brochure_Page") );
			mTab.dataIgnore();
		}
		return "";
	} // isEnablePage
	
	/**
	 * pageExists
	 * Se verifica que el número de la página no existe dentro del mismo folleto.
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String pageExists (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		int page = ((Integer)value).intValue();
		String sql = " Select XX_VMA_PageNumber " +
				" From XX_VMA_BrochurePage " +
				" Where XX_VMA_Brochure_ID = "
					+ctx.getContextAsInt(WindowNo,"XX_VMA_Brochure_ID");
		
		String message = Msg.translate(Env.getCtx(), "XX_Page_Exist");
		boolean is = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if(page != 0){
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				while(rs.next()){ 
					if(page==rs.getInt(1)){
						is=true;
						break;
					}
				}// while
				if(is){
					mTab.setValue("XX_VMA_PageNumber", new Integer (0));
					return message;
				}
			} //try
			catch (SQLException e){ e.printStackTrace(); }
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		} // if page		
		return "";
	} // Fin pageExists
	
	/**
	 * deactivatePage
	 * Se encarga de desactivar los elementos asociados
	 * a las páginas de folleto. Esto se hace cuando se desactiva el
	 * Folleto.
	 * 
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String deactivatePage (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		Integer pagina = ctx.getContextAsInt(WindowNo, "XX_VMA_BrochurePage_ID");
		
		if(pagina!=0){
			if(ctx.getContext(WindowNo, "IsActive").equals("N")){
				XX_VME_GeneralFunctions.processBrochurePage(pagina, false, true);
			} // if active
			else {
				XX_VME_GeneralFunctions.processBrochurePage(pagina, true, true);
			} // else
		} // if pagina

		return "";
	} // Fin deactivatePage

} // Fin XX_VMA_BrochurePageCallout
