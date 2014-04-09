package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.intf.*;
import org.compiere.model.MABCRank;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class LeastOnHand implements RankSortInterface {

	private static final CLogger s_log = CLogger.getCLogger( LeastOnHand.class );
	
	public Integer[] Sort(int M_Warehouse_ID ,MABCRank line, int cycleCount)
	{
		ArrayList<Integer> products = new ArrayList<Integer>();
		
		String sql    = " SELECT M_ABCProductAssignment_ID, SUM(COALESCE(Qty,0)) qty " 
				      + " FROM M_ABCProductAssignment p "
				      + " INNER JOIN M_Product pr ON (pr.M_Product_ID = p.M_Product_ID AND pr.IsActive = 'Y') "
				      + " LEFT OUTER JOIN M_StorageDetail s ON (s.M_Product_ID = p.M_Product_ID "
				                                 + " AND   s.QtyType = 'H' "
				                                 + " AND EXISTS(SELECT 1 "
				                                 + " FROM M_Locator lo "
				                                 + " WHERE lo.M_Warehouse_ID = ? "
				                                 + " AND lo.M_Locator_ID = s.M_Locator_ID )) "
		       		  + " WHERE p.M_ABCAnalysisGroup_ID = ? "
  			          + " AND   p.M_ABCRank_ID = ? "
				      + " AND   p.AD_Client_ID = ? "
				      + " AND   p.IsCounted = 'N' "
				      + " AND   p.Processing = 'N' "
				      + " AND   p.IsActive = 'Y' "
				      + " GROUP BY M_ABCProductAssignment_ID "
				      + " ORDER BY qty ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql.toString(),line.get_Trx());
			pstmt.setInt(1, M_Warehouse_ID);
			pstmt.setInt(2, line.getM_ABCAnalysisGroup_ID());
			pstmt.setInt(3, line.getM_ABCRank_ID());
			pstmt.setInt(4, line.getAD_Client_ID());
			rs = pstmt.executeQuery();
			int tempCount = 0;
			while (rs.next() && tempCount <cycleCount)
			{
				products.add(rs.getInt(1));
				tempCount++;
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}	
		Integer[] retVal = new Integer[products.size ()];
		products.toArray (retVal);
		return retVal;
	}
}
