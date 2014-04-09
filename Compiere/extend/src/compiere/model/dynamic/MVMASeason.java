package compiere.model.dynamic;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.util.Trx;


/**
 * 
 * Esta clase permite realizar las validaciones respectivas
 * al momento de introducir, modificar o eliminar una temporada.
 * Estas validaciones correponden a triggers de base de datos que
 * se subieron a la capa del negocio.
 * 
 * @author Alejandro Prieto
 * @version 1.0
 *
 */

public class MVMASeason extends X_XX_VMA_Season{

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(X_XX_VMA_Season.class);
	
    /** Constructor Standard
    @param ctx context
    @param XX_VMA_Season_ID id
    @param trxName transaction
    */
	public MVMASeason(Ctx ctx, int XX_VMA_Season_ID, Trx trxName) {
		super(ctx, XX_VMA_Season_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
    /** Constructor por carga de datos
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public MVMASeason (Ctx ctx, ResultSet rs, Trx trxName)
    {
        super (ctx, rs, trxName);
        
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = 4395825114281855685L;

	protected boolean beforeDelete() {
		String docStatus = getDocStatus();
		if(docStatus.equals("AP")){
			log.saveError("Error", Msg.getMsg(getCtx(), "No puede borrar esta Temporada porque " +
					"\n está aprobada"));
			/*ADialog.error(Env.WINDOW_INFO, null, "No puede borrar esta Temporada porque " +
					"\n está aprobada");
			*/
			return false;
		}else if (docStatus.equals("IP")){
			log.saveError("Error", Msg.getMsg(getCtx(), "No puede borrar esta Temporada porque " +
			"\n está en progreso"));
			
			return false;
		}else if (docStatus.equals("CL")){
			log.saveError("Error", Msg.getMsg(getCtx(), "No puede borrar esta Temporada porque " +
			"\n ya fue cerrada"));
			
			return false;
		}
		return true;
	}
}
