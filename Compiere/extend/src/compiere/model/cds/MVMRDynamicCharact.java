package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRDynamicCharact extends X_XX_VMR_DynamicCharact{
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRDynamicCharact.class);
    
	public MVMRDynamicCharact(Ctx ctx, ResultSet rs, Trx trxName) {
		super(ctx, rs, trxName);
	
	}

	public MVMRDynamicCharact(Ctx ctx, int DynamicCharact_ID,
			Trx trxName) {
		super(ctx, DynamicCharact_ID, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){
		//int att = getM_AttributeSet_ID();
		int dep= getXX_VMR_Department_ID();
		int lin = getXX_VMR_Line_ID();
		int sec = getXX_VMR_Section_ID();
		String sql = "";
		sql = "SELECT M_AttributeSet_ID " +
			   "FROM XX_VMR_DynamicCharact "+
			   "WHERE XX_VMR_Department_ID = "+dep+" and "+
			   "XX_VMR_Line_ID = "+lin+" and "+
			   "XX_VMR_Section_ID = "+sec+"";
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_AttributeSetR"));
					return false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}
			return true;
	}//beforeSave
	
}
