package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MVMEDayPricing extends X_XX_VME_DayPricing {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MVMEDayPricing(Ctx ctx, int XX_VME_DayPricing_ID, Trx trx) {
		super(ctx, XX_VME_DayPricing_ID, trx);
		// TODO Auto-generated constructor stub
	}
	public MVMEDayPricing(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	public boolean afterSave(boolean newRecord, boolean success)
	{
		try
		{
			String SQL = "update XX_VME_DAYPRICING set ISACTIVE = 'N' where XX_VME_DAYPRICING_ID <> "+get_ID() +" ";
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			rs.close();
			pstmt.close();
			
		}
		catch(Exception e)
		{
			return false;
		}
	
		return true;
	}

}
