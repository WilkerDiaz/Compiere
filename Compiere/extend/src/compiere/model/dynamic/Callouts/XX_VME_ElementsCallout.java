package compiere.model.dynamic.Callouts;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;

import compiere.model.dynamic.XX_VME_GeneralFunctions;

/**
 * Esta clase contiene todos los callouts que se ejecutan al momento de
 * aplicar cambios a los campos de un elemento perteneciente a una página
 * de un folleto.
 * Estos callouts son validaciones dinámicas necesarias.
 * @author Alejandro Prieto
 * @author María Vintimilla
 * @version 1.0
 */
public class XX_VME_ElementsCallout extends CalloutEngine {
	/**
	 * checkPageTypoWElementType
	 * Se chequea que no se coloquen elementos de tipo imagen en una página de
	 * productos.
	 * @author Alejandro Prieto
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String checkPageTypoWElementType (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		/*
		if(value != null){
			if(ctx.getContext(WindowNo, "XX_VMA_PageType").equals("P") & value.equals("I")){
				mTab.setValue("XX_VME_Type", null);
				return Msg.translate(Env.getCtx(), "XX_ProductPage");
			}
		} // value*/
		setCalloutActive(false);
		return "";
	} // Fin checkPageTypoWElementType
	

	/** setImage
	 * Cuando la página es sólo de imágenes o publicidad, se coloca de manera predeterminada
	 * al elemento como de tipo imagen.
	 * @author Alejandro Prieto
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String setImage(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		String pageType = ctx.getContext(WindowNo, "XX_VMA_PageType");
		if(value==null & pageType.equals("I")){
			mTab.setValue("XX_VME_Type", "I");
		}
		setCalloutActive(false);
		return "";
	} // Fin setImage
	
	
	/** deactivateElement
	 * Se encarga de desactivar los elementos asociados a las páginas de folleto. 
	 * @author Maria Vintimilla
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String deactivateElement (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int element = ctx.getContextAsInt(WindowNo, "XX_VME_Element_ID");
		
		// Se verifica que exista el elemento antes de desactivar
		if(element != 0){
			if(ctx.getContext(WindowNo, "IsActive").equals("N")){
				//TODO referencia o elemento
				XX_VME_GeneralFunctions.processElement (element, false, true);
			}//if
			else{
				//TODO referencia o elemento
				XX_VME_GeneralFunctions.processElement (element, true, true);
			}//else
			
		} // Element
		
		setCalloutActive(false);
		return "";
	} // Fin deactivateElement
	
	/** setPrice
	 * Se coloca por defecto procio promocional igual a precio dinámica.
	 * @author Alejandro Prieto
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String setPrice(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		BigDecimal precio = (BigDecimal)value;
		
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		if(value != null){
			if(precio.compareTo(BigDecimal.valueOf(0)) == 1){
				mTab.setValue("XX_VME_PromoDynPrice", precio);	
			}
		}
		
		setCalloutActive(false);
		return "";
	} // Fin setImage
	
	/** setDescription
	 * Se coloca por defecto en descripción el nombre del elemento.
	 * @author Maria Vintimilla
	 * @param ctx contexto
	 * @param WindowNo número de la ventana
	 * @param mTab pestaña de la ventana
	 * @param mField campo al cual se le aplica el callout
	 * @param value valor que se está introdución
	 * @param oldValue valor que había anteriormente
	 * @return String mensaje de error si hubo un problema, nulo si no.
	 */
	public String setDescription(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		String descripcion = (String)value;
		
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		if(!descripcion.equals("")){
			mTab.setValue("Description", descripcion);	
		}
		
		setCalloutActive(false);
		return "";
	} // Fin setImage

} // Fin XX_VME_ElementsCallout
