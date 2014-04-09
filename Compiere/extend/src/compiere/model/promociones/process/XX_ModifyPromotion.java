package compiere.model.promociones.process;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.promociones.X_XX_VMR_DetailPromotionExt;
import compiere.model.promociones.X_XX_VMR_PromoConditionValue;
import compiere.model.promociones.X_XX_VMR_Promotion;

/** XX_VMR_ModifyPromotion
 * Proceso que llama a la forma de modifiación y eliminación de
 * detalles de una promocion
 * @author jperez
 * @version 1.0 
 * */
public class XX_ModifyPromotion extends SvrProcess {

	private static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {	
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {
			X_XX_VMR_PromoConditionValue detalle = new X_XX_VMR_PromoConditionValue(getCtx(), getRecord_ID(), null);
			X_XX_VMR_Promotion promocion = new X_XX_VMR_Promotion(getCtx(), detalle.getXX_VMR_Promotion_ID(),  null);
			Env.getCtx().setContext( "#XX_VMR_Promotion_ID", detalle.getXX_VMR_Promotion_ID());
			Env.getCtx().setContext( "#XX_VMR_Condition_ID", detalle.getXX_VMR_PromoConditionValue_ID());
			String tipoPromocion = promocion.getXX_TypePromotion();
			Env.getCtx().setContext( "#XX_TypePromotion", tipoPromocion);

			//Displays the Form
			form.setName("Modificar Mensaje");
			form.openForm(getCtx().getContextAsInt("#XX_L_FORMMODIFYPROMOTION_ID"));
		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return "Proceso Ejecutado Sin Errores";
	}//doIt

	@Override
	protected void prepare() {
		
	}//prepare	
	
} // Fin XX_VME_ModifyVendorReference
