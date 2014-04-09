package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRBrand extends X_XX_VMR_Brand {
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);

	public MVMRBrand(Ctx ctx, int XX_VCN_Brand_ID,	Trx trx) 
	{
		super(ctx, XX_VCN_Brand_ID, trx);
		// TODO Auto-generated constructor stub
	}//MVMRBrand

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRBrand(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVMRBrand
	
	protected boolean beforeSave(boolean newRecord)
	{
		PreparedStatement priceRulePstmt = null;
		ResultSet rs = null;
		try
		{
		 //if(newRecord)
		// {
			String Nombre = getName().trim();
			String sql = "select * from XX_VMR_BRAND where " +
					     "upper(name) = upper('"+Nombre+"') " +
					     "AND AD_CLIENT_ID = "+getAD_Client_ID()+ 
					     " and XX_VMR_BRAND_ID <> "+getXX_VMR_Brand_ID() ;
			priceRulePstmt = DB.prepareStatement(sql, null);
			rs = priceRulePstmt.executeQuery();
			System.out.println("BRAND:"+sql);
			if (rs.next())
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "XX_BrandCreated"));
				return false;
			}
		//}
		 setComments("");
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
