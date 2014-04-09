package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.intf.*;

public class AssignRankHighestPrice implements RankAnalysisInterface {

	private static final CLogger s_log = CLogger.getCLogger( AssignRankHighestPrice.class );
	public Integer[] rankAnalysis(MABCAnalysisGroup group, int M_PriceList_Version_ID,int p_AnalysisDays)
	{
		ArrayList<Integer> products = new ArrayList<Integer>();
		String sql = " SELECT p.M_Product_ID, " 
	               + " MAX(COALESCE(pp.PriceStd,0)) price "
	               + " FROM M_Product p "
	               + " LEFT OUTER JOIN M_ProductPrice pp ON (pp.M_Product_ID = p.M_Product_ID "
	                                                     + " AND pp.IsActive = 'Y' "
	                                                     + " AND pp.M_PriceList_Version_ID = ? )"
	               + " WHERE p.IsActive = 'Y' "
	               + " AND p.IsStocked = 'Y' "
	               + " AND p.AD_Client_ID = ? "
	               + " AND p.IsSummary = 'N' "
	               + " AND p.ProductType = 'I' "
	               + " GROUP BY p.M_Product_ID "
	               + " ORDER BY price DESC ";

			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql,group.get_Trx());
			pstmt.setInt(1,M_PriceList_Version_ID);
			pstmt.setInt(2, group.getAD_Client_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				products.add(rs.getInt(1));
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
