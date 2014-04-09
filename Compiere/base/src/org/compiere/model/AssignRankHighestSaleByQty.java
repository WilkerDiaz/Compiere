package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.intf.*;

public class AssignRankHighestSaleByQty implements RankAnalysisInterface {

	private static final CLogger s_log = CLogger.getCLogger( AssignRankHighestSaleByQty.class );
	public Integer[] rankAnalysis(MABCAnalysisGroup group, int M_PriceList_Version_ID,int p_AnalysisDays)
	{
		ArrayList<Integer> products = new ArrayList<Integer>();
		String sql = " SELECT p.M_Product_ID, "   
		           + " SUM((CASE io.IsReturnTrx "
                   + " WHEN 'Y' THEN -1*COALESCE(iol.MovementQty,0) "
                   + " WHEN 'N' THEN COALESCE(iol.MovementQty,0) END)) AS qty "  
		           + " FROM M_Product p "  
		           + " LEFT OUTER JOIN M_InOutLine iol ON (iol.M_Product_ID = p.M_Product_ID) "
		           + " LEFT OUTER JOIN M_InOut io ON (io.M_InOut_ID = iol.M_InOut_ID " 
		           		                          + " AND io.IsSOTrx = 'Y' "
		           		                          + " AND io.M_Warehouse_ID = ? "
		           		                          + " AND io.AD_Org_ID = ? "
		           		                          + " AND io.DocStatus IN ('CO') "
		           		                          + " AND io.MovementDate>=(SYSDATE-?))"
		          + " WHERE p.IsActive = 'Y' "  
		          + " AND p.IsStocked = 'Y' "  
		          + " AND p.AD_Client_ID = ? "  
		          + " AND p.IsSummary = 'N' "
		          + " AND p.ProductType = 'I' "
		          + " GROUP BY p.M_Product_ID "
		          + " ORDER BY qty DESC  ";
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql,group.get_Trx());
			pstmt.setInt(1,group.getM_Warehouse_ID());
			pstmt.setInt(2,group.getAD_Org_ID());
			pstmt.setInt(3,p_AnalysisDays);
			pstmt.setInt(4,group.getAD_Client_ID());
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
