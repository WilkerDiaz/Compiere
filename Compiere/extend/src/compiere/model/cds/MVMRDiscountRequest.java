package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;


public class MVMRDiscountRequest extends X_XX_VMR_DiscountRequest {

	/**
	*  Realizado por Rosmaira Arvelo 
	*/
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRDiscountRequest.class);
		
	public MVMRDiscountRequest(Ctx ctx, int XX_VMR_DiscountRequest_ID,
			Trx trx) {
		super(ctx, XX_VMR_DiscountRequest_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRDiscountRequest (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}

	@Override
	protected boolean beforeDelete()
	{
		boolean delete = super.beforeDelete();
		
		if(delete){
			// borramos el cm_chat para que no de error
			
			if (getXX_Status().equals("AP"))
			{
				log.saveError("", Msg.getMsg(getCtx(), "CanNotDeleteRequest"));
				return false;
			}
			
			String SQL5 =
				"delete from xx_vmr_DiscountAppliDetail where xx_vmr_discountrequest_id=" + getXX_VMR_DiscountRequest_ID();
			
			try {
				DB.executeUpdate(get_Trx(),SQL5);
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage());
				System.out.println("No se pudo borrar el detalle de la solicitud de rebaja");
				return false;
			}
						
			// borramos el cm_chat para que no de error
			
			SQL5 =
				"delete from cm_chat where AD_Table_ID=1000329 AND Record_ID=" + getXX_VMR_DiscountRequest_ID();
			    
			try {
				DB.executeUpdate(get_Trx(),SQL5);
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage());
				System.out.println("entra al catch");
			}
			
			
		}
		
		return delete;
		
	}	//	beforeDelete
}
