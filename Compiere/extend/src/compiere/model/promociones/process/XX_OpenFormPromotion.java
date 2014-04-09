package compiere.model.promociones.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.promociones.X_Ref_XX_TypeSelection;
import compiere.model.promociones.X_XX_VMR_PromoConditionValue;
import compiere.model.promociones.X_XX_VMR_Promotion;

/** Proceso que llama a la forma de Filtro de Busqueda y seleccion de los detalles de la promocion que se desea montar
 * @author Wilker Díaz
 * @version 1.0
 * */

public class XX_OpenFormPromotion extends SvrProcess {

	private static Object m_readLock = new Object();

	@Override
	protected String doIt() throws Exception {	
		FormFrame form = new FormFrame();
		
		synchronized( m_readLock ) {
			X_XX_VMR_PromoConditionValue detalle = new X_XX_VMR_PromoConditionValue(getCtx(), getRecord_ID(), null);
			X_XX_VMR_Promotion promocion = new X_XX_VMR_Promotion(getCtx(), detalle.getXX_VMR_Promotion_ID(),  null);
			Env.getCtx().setContext( "#XX_VMR_Promotion_ID", detalle.getXX_VMR_Promotion_ID());
			Env.getCtx().setContext( "#XX_VMR_PromoConditionValue_ID", getRecord_ID());
			String tipoPromocion = promocion.getXX_TypePromotion();
			Env.getCtx().setContext( "#XX_TypePromotion", tipoPromocion);
			Env.getCtx().setContext( "#M_WareHouse_ID", detalle.getM_Warehouse_ID());
			ArrayList<String> nombreColumnas = new ArrayList<String>();
			String in = "";
			//Montar Condiciones en el Contexto dependiendo del tipo de promocion, Se busca en la BD
			String sql = "select b.columnName from XX_VMR_ConditionPromotion a inner join AD_Column b on (a.AD_Column_ID = b.AD_Column_ID) " +
					     "where a.XX_TypePromotion = "+tipoPromocion+" and XX_TypeSelection = "+X_Ref_XX_TypeSelection.PARAMETER.getValue()+"";
			
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				ps = DB.prepareStatement(sql, null);
			    rs = ps.executeQuery();
				while (rs.next())
				{
					nombreColumnas.add(rs.getString(1));
					in += rs.getString(1) + ",";
				}
				if(nombreColumnas.size()> 0 ) {
					in = in.substring(0, (in.length())-1);
				}
			}catch (Exception e) {
				return "Parametros para el Tipo de Promoción Incompletos, Comuniquese a Soporte";
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
			
			
			String sql1 = "select "+in+" from XX_VMR_PromoConditionValue where XX_VMR_Promotion_ID="+detalle.getXX_VMR_Promotion_ID()+" and XX_VMR_PromoConditionValue_ID = "+detalle.getXX_VMR_PromoConditionValue_ID()+"  group by "+in+"";
			
			PreparedStatement ps1 = null;
			ResultSet rs1 = null;
			try{
				ps1 = DB.prepareStatement(sql1, null);
				rs1 = ps1.executeQuery();
				while (rs1.next())
				{
					for (int i=0; i<nombreColumnas.size(); i++)
					        Env.getCtx().setContext( "#"+nombreColumnas.get(i), rs1.getString(i+1).replace(".", ","));
				}
			}catch (Exception e) {
				return "Parametros para el Tipo de Promoción Incompletos, Comuniquese a Soporte";
			}finally{
				DB.closeResultSet(rs1);
				DB.closeStatement(ps1);
			}
			
			
			Env.getCtx().setContext( "#XX_SearchFilterForm_ID", getRecord_ID());
			Env.getCtx().setContext( "#XX_VME_Table_ID", getTable_ID());
			ProcessInfo process =  getProcessInfo();
			Env.getCtx().setContext( "#XX_VME_Process_ID", process.getAD_Process_ID());
			//Displays the Form
			form.setName(Msg.translate(Env.getCtx(), "XX_VME_SearchFilterForm"));
			//form.openForm(getCtx().getContextAsInt("#XX_L_SEARCHFILTERFORM_ID"));
			form.openForm(getCtx().getContextAsInt("#XX_L_FORMPRODUCTSELECT_ID"));
		}							
		AEnv.showCenterScreen(form);
		while (form.isVisible())
			Thread.sleep(500);
		
		return "Proceso Ejecutado Sin Errores";
	}//doIt

	@Override
	protected void prepare() {
		
	}//prepare	
	
} // Fin 
