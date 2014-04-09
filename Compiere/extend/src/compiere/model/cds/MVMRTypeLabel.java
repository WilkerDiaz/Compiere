package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRTypeLabel extends X_XX_VMR_TypeLabel {

	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRTypeLabel.class);

	public MVMRTypeLabel(Ctx ctx, int XX_VMR_TypeLabel_ID,	Trx trx) 
	{
		super(ctx, XX_VMR_TypeLabel_ID, trx);
	}//MVMRTypeLabel

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRTypeLabel(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVMRTypeLabel
	
	protected boolean beforeSave(boolean newRecord)
	{
		PreparedStatement priceRulePstmt = null;
		ResultSet rs = null;
		try
		{
		//if(newRecord)
	//	{
			String Nombre = getName().trim();
			String sql = "select * from XX_VMR_TypeLabel where " +
					     "upper(name) = upper('"+Nombre+"') and " +
					     "AD_CLIENT_ID = "+getAD_Client_ID()+" and "+
						 "XX_VMR_TypeLabel_ID <> "+getXX_VMR_TypeLabel_ID();
			priceRulePstmt = DB.prepareStatement(sql, null);
			rs = priceRulePstmt.executeQuery();
			if (rs.next())
			{
				log.saveError("Warning", Msg.getMsg(getCtx(), "XX_LabelType"));
				return false;
			}
	//	}	
		setName(get_ValueAsString("Name").toUpperCase());
		}catch (Exception e) {
			return false;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {priceRulePstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
	return true;
	}
}
