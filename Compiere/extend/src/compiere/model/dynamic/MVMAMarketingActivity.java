package compiere.model.dynamic;

import java.math.BigDecimal;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;


/**
 * 
 * Esta clase permite realizar las validaciones respectivas
 * al momento de introducir, modificar o eliminar una acción de mercadeo.
 * Estas validaciones correponden a triggers de base de datos que
 * se subieron a la capa del negocio.
 * 
 * @author Alejandro Prieto
 * @version 1.0
 *
 */

public class MVMAMarketingActivity extends X_XX_VMA_MarketingActivity {

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(X_XX_VMA_MarketingActivity.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1893685976813748281L;

    /** Constructor Standard
    @param ctx context
    @param XX_VMA_Season_ID id
    @param trxName transaction
    */
	public MVMAMarketingActivity(Ctx ctx, int XX_VMA_MarketingActivity_ID,
			Trx trxName) {
		super(ctx, XX_VMA_MarketingActivity_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
    /** Constructor por carga de datos
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public MVMAMarketingActivity (Ctx ctx, ResultSet rs, Trx trxName)
    {
        super (ctx, rs, trxName);
        
    }
    
	
    /**
     * Si el tipo de acción de mercadeo es Folleto, se mandá a colocar al 
     * mismo como asociado a una actividad. Así se evita que sea relacionado con
     * otra acción de mercadeo.
     * 
     * @param newRecord	permite saber si el registro es nuevo o no.
     * @param success 	permite saber si el registro fue salvado correctamente.
     * @return boolean	true si se ejecuto bien el afterSave, false si no.
     */
	//@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
    	int folleto1 = get_ValueOldAsInt("XX_VMA_Brochure_ID");
    	int folleto2 = get_ValueAsInt("XX_VMA_Brochure_ID");
    	BigDecimal oldValue = (BigDecimal)get_ValueOld("Costs");
    	X_C_Campaign campana = new X_C_Campaign(getCtx(), get_ValueAsInt("C_Campaign_ID"),null);
    	//System.out.println("el nuevo folleto "+folleto2+"el viejo folleto es "+folleto1);
    	BigDecimal costs = campana.getCosts();
    	if (success){
	    	if(newRecord){
		    	//campana.setCosts(campana.getCosts().add(get_ValueAsBigDecimal("Costs")));
		    	//campana.save();
		    	
		    	costs = costs.add(get_ValueAsBigDecimal("Costs"));
		    	String updateCosto = "UPDATE C_CAMPAIGN SET COSTS="+costs+" WHERE C_CAMPAIGN_ID="+get_ValueAsInt("C_Campaign_ID");
		    	DB.executeUpdate(null, updateCosto );
				
	    	}
	     	//campana.setCosts((campana.getCosts().subtract(oldValue)).add(get_ValueAsBigDecimal("Costs")));
	     	//campana.save();
	     	
	     	costs = (costs.subtract(oldValue)).add(get_ValueAsBigDecimal("Costs"));
	    	String updateCosto = "UPDATE C_CAMPAIGN SET COSTS="+costs+" WHERE C_CAMPAIGN_ID="+get_ValueAsInt("C_Campaign_ID");
	    	DB.executeUpdate(null, updateCosto );
			
	     	
	    	if (get_ValueAsString("XX_VMA_ActivityType").equals("B")&& (newRecord || folleto1==0)){
				//System.out.println("IF THE NEW RECORD...................");
				X_XX_VMA_Brochure Brochure= new X_XX_VMA_Brochure(getCtx(),get_ValueAsInt("XX_VMA_Brochure_ID"),null); 
				Brochure.setXX_VMA_Assigned(true);
				Brochure.save();
				
				return true;
			}else if (get_ValueAsString("XX_VMA_AprobIni").equals("N")){
				if((!get_ValueAsString("XX_VMA_ActivityType").equals("B"))&& folleto1!=0){
					//System.out.println("IF THE CAMBIO DE TIPO DE ACTIVIDAD............");
					X_XX_VMA_Brochure Brochure= new X_XX_VMA_Brochure(getCtx(),folleto1,null); 
					Brochure.setXX_VMA_Assigned(false);			
					Brochure.save();
					set_Value("XX_VMA_Brochure_ID", null);
					
					return true;
				}else if(folleto1!=folleto2 && folleto2!=0){
					//System.out.println("IF THE CAMBIO DE FOLLETO.........................");
			    	X_XX_VMA_Brochure brochure1 = new X_XX_VMA_Brochure(getCtx(),folleto1,null);
			    	X_XX_VMA_Brochure brochure2 = new X_XX_VMA_Brochure(getCtx(),folleto2,null);
			    	brochure1.setXX_VMA_Assigned(false);
			    	brochure2.setXX_VMA_Assigned(true);
			    	brochure1.save();
			    	brochure2.save();
			    	
			    	return true;
				}
		    }
    	}
		return true;
	}	
	
	/**
	 * Se encarga de realizar las operaciones necesarias antes de salvar el registro
	 * o acción de mercadeo. Por ahora sólo se encarga de verificar si no hay ningún
	 * folleto asociada a la misma.
	 * 
	 * @return boolean	true si no hay ningún folleto asociado, false si hay.
	 */
	protected boolean beforeDelete() {
		
		if(get_ValueAsString("DocStatus").equals("AP")){
			log.saveError("Error", Msg.getMsg(getCtx(), "No puede borrar esta Acción de Mercadeo porque " +
					"\nya ha sido aprobada"));
			/*ADialog.error(Env.WINDOW_INFO, null, "No puede borrar esta Acción de Mercadeo porque " +
					"\nya tiene un Folleto asociado");
			*/
			return false;
		}else if (get_ValueAsString("DocStatus").equals("IP")){
			log.saveError("Error", Msg.getMsg(getCtx(), "No puede borrar esta Acción de Mercadeo porque " +
			"\nse encuentra en progreso"));
			
			return false;
		}else if (get_ValueAsString("DocStatus").equals("CL")){
			log.saveError("Error", Msg.getMsg(getCtx(), "No puede borrar esta Acción de Mercadeo porque " +
			"\nya ha sido cerrada"));
			
			return false;
		}
		if (get_ValueAsString("XX_VMA_ActivityType").equals("B")){
			X_XX_VMA_Brochure Brochure= new X_XX_VMA_Brochure(getCtx(),get_ValueAsInt("XX_VMA_Brochure_ID"),null); 
			Brochure.setXX_VMA_Assigned(false);
			Brochure.save();
		}
		return true;
	}
	
	protected boolean afterDelete(boolean success) {
		
    	BigDecimal oldValue = (BigDecimal)get_ValueOld("Costs");
    	X_C_Campaign campana = new X_C_Campaign(getCtx(), get_ValueAsInt("C_Campaign_ID"),null);
		if(success){
	     	campana.setCosts(campana.getCosts().subtract(get_ValueAsBigDecimal("Costs")));
	     	campana.save();
		}else{
			log.saveError("Error", Msg.getMsg(getCtx(), "No pudo actualizar el costo de la campana comercial"));
			
			return false;
		}	
		return true;
	}
	
	

}
