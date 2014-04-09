package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRTypeExhibition extends X_XX_VMR_TypeExhibition {
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRTypeExhibition.class);

	public MVMRTypeExhibition(Ctx ctx, int XX_VMR_TypeExhibition_ID,	Trx trx) 
	{
		super(ctx, XX_VMR_TypeExhibition_ID, trx);
	}//MVMRTypeExhibition

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRTypeExhibition(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVMRTypeExhibition
	
	protected boolean beforeSave(boolean newRecord)
	{
		PreparedStatement priceRulePstmt = null;
		ResultSet rs = null;
		try
		{
		//if(newRecord)
		//{
			String Nombre = getName().trim();
			String sql = "select * from XX_VMR_TypeExhibition where" +
					     " upper(name) = upper('"+Nombre+"') and " +
					     "AD_CLIENT_ID = "+getAD_Client_ID()+" and "+
					     "XX_VMR_TypeExhibition_ID <> "+getXX_VMR_TypeExhibition_ID();
			priceRulePstmt = DB.prepareStatement(sql, null);
			rs = priceRulePstmt.executeQuery();
			if (rs.next())
			{
				log.saveError("Warning", Msg.getMsg(getCtx(), "XX_InventoryType"));
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
