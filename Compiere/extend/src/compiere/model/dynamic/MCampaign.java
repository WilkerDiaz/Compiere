package compiere.model.dynamic;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

/**
*
*    Esta clase es una extensión de MCampaign ubicada en el paquete
*    org.compiere.model.
* @author Alejandro Prieto
* @version 1.0
*/
public class MCampaign extends org.compiere.model.MCampaign {

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCampaign.class);
	
	/**
	 * 	Constructor Standard
	 *	@param ctx context
	 *	@param C_Campaign_ID id
	 *	@param trxName transaction
	 */

	private static final long serialVersionUID = -7170387307023343617L;
	public MCampaign (Ctx ctx, int C_Campaign_ID, Trx trxName)
	{
		super (ctx, C_Campaign_ID, trxName);
		if (C_Campaign_ID == 0)
		{
			setCosts(Env.ZERO);
			setIsSummary(false);
		}
	}	//	MCampaign

	/**
	 * 	Constructor por carga de datos
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MCampaign (Ctx ctx, ResultSet rs, Trx trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCampaign
	
    /** Set XX_VMA_RelCampaign.
    @param XX_VMA_RelCampaign_ID ID de Relacion con coleccion */
    public void setXX_VMA_RelCampaign_ID (int XX_VMA_RelCampaign_ID)
    {
        if (XX_VMA_RelCampaign_ID <= 0) set_Value ("XX_VMR_Collection_ID", null);
        else
        set_Value ("XX_VMA_RelCampaign_ID", Integer.valueOf(XX_VMA_RelCampaign_ID));
        
    }
    
    /** Get XX_VMA_RelCampaignv.
    @return ID de relacion con coleccion */
    public int getXX_VMA_RelCampaign_ID() 
    {
        return get_ValueAsInt("XX_VMA_RelCampaign_ID");
        
    }
	


	protected boolean beforeDelete() {
		String docStatus = get_ValueAsString("DocStatus");
		if(docStatus.equals("AP")){
			log.saveError("Advertencia", Msg.getMsg(getCtx(), "No puede borrar esta Campaña Comercial porque " +
					"\n está aprobada"));
			//ADialog.error(Env.WINDOW_INFO, null, "No puede borrar esta Campaña Comercial porque " +
			//	"\n está aprobada");
			return false;
		}else if(docStatus.equals("IP")){
			log.saveError("Advertencia", Msg.getMsg(getCtx(), "No puede borrar esta Campaña Comercial porque " +
			"\n está en progreso"));
			return false;
		}else if(docStatus.equals("CL")){
			log.saveError("Advertencia", Msg.getMsg(getCtx(), "No puede borrar esta Campaña Comercial porque " +
			"\n ya fue cerrada"));
			return false;
		}
		return true;
	}
}
