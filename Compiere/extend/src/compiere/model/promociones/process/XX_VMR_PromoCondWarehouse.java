package compiere.model.promociones.process;

import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.promociones.X_XX_VMR_PromoConditionValue;
import compiere.model.promociones.X_XX_VMR_Promotion;

public class XX_VMR_PromoCondWarehouse extends SvrProcess {

	private static Object m_readLock = new Object();
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		FormFrame form = new FormFrame();
		synchronized( m_readLock ) {
			X_XX_VMR_PromoConditionValue detalle = new X_XX_VMR_PromoConditionValue(getCtx(), getRecord_ID(), null);
			Env.getCtx().setContext( "#XX_VMR_Promotion_ID", detalle.getXX_VMR_Promotion_ID());
			Env.getCtx().setContext( "#XX_VMR_Condition_ID", detalle.getXX_VMR_PromoConditionValue_ID());
			//Displays the Form
			form.setName("Selección de Tiendas");
			form.openForm(getCtx().getContextAsInt("#XX_L_FORMPROMOCONDWAREHOUSE_ID"));
		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		return "";
	}

}
