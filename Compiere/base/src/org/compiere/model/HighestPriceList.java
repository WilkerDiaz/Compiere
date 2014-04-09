package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.intf.*;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class HighestPriceList implements RankSortInterface{

	private static final CLogger s_log = CLogger.getCLogger( HighestPriceList.class );
	
	public Integer[] Sort(int M_Warehouse_ID ,MABCRank line, int cycleCount)
	{
		ArrayList<Integer> products = new ArrayList<Integer>();
		
		if(line.getM_PriceList_Version_ID() == 0)
		{
			s_log.log(Level.WARNING,"Price List is not defined for the Rank ");
			return null;
		}
		
		String sql = " SELECT p.M_ABCProductAssignment_ID, " 
	               + " MAX(COALESCE(pp.PriceStd,0)) price "
	               + " FROM M_ABCProductAssignment p "
	               + " INNER JOIN M_Product pr ON (pr.M_Product_ID = p.M_Product_ID AND pr.IsActive = 'Y') "
	               + " LEFT OUTER JOIN M_ProductPrice pp ON (pp.M_Product_ID = p.M_Product_ID "
	                                                     + " AND pp.IsActive = 'Y' "
	                                                     + " AND pp.M_PriceList_Version_ID = ? )"
	               + " WHERE p.M_ABCAnalysisGroup_ID = ? "
	               + " AND p.M_ABCRank_ID = ? "
	               + " AND p.AD_Client_ID = ? "
	               + " AND p.IsCounted = 'N' "
	               + " AND p.Processing = 'N' "
	               + " AND p.IsActive = 'Y' "
	               + " GROUP BY p.M_ABCProductAssignment_ID "
	               + " ORDER BY price DESC ";

			
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql.toString(),line.get_Trx());
			pstmt.setInt(1, line.getM_PriceList_Version_ID());
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
