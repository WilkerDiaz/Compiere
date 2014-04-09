package compiere.model.dynamic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;


/**
 * 
 * Esta clase permite realizar las validaciones respectivas
 * al momento de introducir, modificar o eliminar un folleto.
 * Estas validaciones correponden a triggers de base de datos que
 * se subieron a la capa del negocio.
 * 
 * @author Alejandro Prieto
 * @version 1.0
 */

public class MVMABrochure extends X_XX_VMA_Brochure{
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(X_XX_VMA_Brochure.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 5867347281567892103L;
	
    /** Constructor Standard
    @param ctx context
    @param XX_VMA_Brochure_ID id
    @param trxName transaction
    */
    public MVMABrochure (Ctx ctx, int XX_VMA_Brochure_ID, Trx trxName)
    {
        super (ctx, XX_VMA_Brochure_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMA_Brochure_ID == 0)
        {
            setName (null);
            setXX_VMA_Assigned (false);	// N
            setXX_VMA_Brochure_ID (0);
            setXX_VMA_Expired (false);	// N
            setXX_VMA_Topic (null);
            
        }
        */
        
    }
    /** Constructor por carga de datos
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public MVMABrochure (Ctx ctx, ResultSet rs, Trx trxName)
    {
        super (ctx, rs, trxName);
        
    }
    
	protected boolean beforeDelete() {
		
		int brochure=getXX_VMA_Brochure_ID();
		boolean can=true;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String docstatus;
		String sql="select DocStatus from XX_VMA_MarketingActivity where XX_VMA_Brochure_ID="+brochure;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				docstatus=rs.getString(1);
				if(docstatus.equals("AP") | docstatus.equals("IP") | docstatus.equals("CL")){
					log.saveError("Advertencia", Msg.getMsg(getCtx(), "No puede borrar este Folleto porque " +
					"\nestá asociado a una Acción de Mercadeo"));
					/*ADialog.error(Env.WINDOW_INFO, null, "No puede borrar este Folleto porque " +
					"\nestá asociado a una Acción de Mercadeo");*/
					can=false;
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return can;
	}

}
