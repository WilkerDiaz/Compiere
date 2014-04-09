package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class CalloutRank extends CalloutEngine {

	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());
	
	public String M_ABCRankSort_ID (Ctx ctx, int WindowNo, GridTab mTab, GridField mField,
			Object value, Object oldValue)
	{
		Integer oldM_ABCRankSort_ID = (Integer)oldValue;
		Integer M_ABCRankSort_ID = (Integer)value;
		if(M_ABCRankSort_ID == null)
			return "";
		if(M_ABCRankSort_ID.equals(oldM_ABCRankSort_ID))
			return "";
		
		String IsBasedOnPriceList = "N";
		if(M_ABCRankSort_ID.intValue()!=0)
		{
			String sql = "SELECT IsBasedOnPriceList FROM M_ABCRankSort WHERE M_ABCRankSort_ID = ? ";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement (sql.toString(),(Trx) null);
				pstmt.setInt(1,M_ABCRankSort_ID.intValue());
				rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					IsBasedOnPriceList = rs.getString(1);	
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql.toString(), e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		mTab.setValue("IsBasedOnPriceList", IsBasedOnPriceList);
		return "";
	}

}
