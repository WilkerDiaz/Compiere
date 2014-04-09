package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRTypeInventory extends X_XX_VMR_TypeInventory {

	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRTypeInventory.class);

	public MVMRTypeInventory(Ctx ctx, int XX_VMR_TypeInventory_ID,	Trx trx) 
	{
		super(ctx, XX_VMR_TypeInventory_ID, trx);
	}//MVMRTypeInventory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRTypeInventory(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVMRTypeInventory
	
	protected boolean beforeSave(boolean newRecord)
	{
		PreparedStatement priceRulePstmt = null;
		ResultSet rs = null;
		try
		{
			//if(newRecord)
			//{
				String Nombre = getName().trim();
				String sql = "select * from XX_VMR_TypeInventory where " +
						     "upper(name) = upper('"+Nombre+"') and " +
						     "AD_CLIENT_ID = "+getAD_Client_ID()+" and "+
							 "XX_VMR_TypeInventory_ID <> "+getXX_VMR_TypeInventory_ID();
				System.out.println(sql);
				priceRulePstmt = DB.prepareStatement(sql, null);
				rs = priceRulePstmt.executeQuery();
				if (rs.next())
				{
					log.saveError("Warning", Msg.getMsg(getCtx(), "XX_InventoryTypeCreated"));
					return false;
				}
			//}	
			setName(get_ValueAsString("Name").toUpperCase());
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {priceRulePstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
	return true;
	}
}
