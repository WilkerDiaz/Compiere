package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.intf.*;

public class AssignRankHighestSaleByValue implements RankAnalysisInterface {

	private static final CLogger s_log = CLogger.getCLogger( AssignRankHighestSaleByValue.class );
	public Integer[] rankAnalysis(MABCAnalysisGroup group, int M_PriceList_Version_ID,int p_AnalysisDays)
	{
		ArrayList<Integer> products = new ArrayList<Integer>();
		String sql = " SELECT p.M_Product_ID, "   
			       + " Sum (CASE "
				        + " WHEN (i.C_Currency_ID <> acct.C_Currency_ID AND il.LineNetAmt IS NOT NULL "
				              + " AND il.LineNetAmt <>0) "
					    + " THEN CurrencyConvert(il.LineNetAmt,i.C_Currency_ID,acct.C_Currency_ID, "
					                         + " i.DateInvoiced,o.C_ConversionType_ID,?,?) "
					    + " ELSE COALESCE(il.LineNetAmt,0) "
					    + " END * (CASE i.IsReturnTrx WHEN 'N' THEN 1 WHEN 'Y' THEN -1 END)) AS amt "  
		           + " FROM M_Product p "  
		           + " LEFT OUTER JOIN C_InvoiceLine il ON (il.M_Product_ID = p.M_Product_ID) "
		           + " LEFT OUTER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID " 
		           		                          + " AND i.IsSOTrx = 'Y' "
		           		                          + " AND i.C_Order_ID IS NOT NULL "
		           		                          + " AND i.AD_Org_ID = ? "
		           		                          + " AND i.DocStatus IN ('CO') "
		           		                          + " AND i.DateInvoiced>=(SYSDATE-?))"
		          + " LEFT OUTER JOIN C_Order o ON (o.C_Order_ID = i.C_Order_ID "
		                                        + " AND o.M_Warehouse_ID = ? "
		                                        + " AND o.IsSOTrx = 'Y' "
		                                        + " AND o.AD_Org_ID = i.AD_Org_ID), "
                  + " AD_ClientInfo info,C_AcctSchema acct "		                                        
		          + " WHERE p.IsActive = 'Y' "  
		          + " AND p.IsStocked = 'Y' "  
		          + " AND p.AD_Client_ID = ? "  
		          + " AND p.IsSummary = 'N' "
		          + " AND p.ProductType = 'I' "
                  + " AND info.AD_Client_ID = ? "
                  + " AND info.C_AcctSchema1_ID = acct.C_AcctSchema_ID "
		          + " GROUP BY p.M_Product_ID "
		          + " ORDER BY amt DESC  ";
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql,group.get_Trx());
			pstmt.setInt(1,group.getAD_Client_ID());
			pstmt.setInt(2,group.getAD_Org_ID());
			pstmt.setInt(3,group.getAD_Org_ID());
			pstmt.setInt(4,p_AnalysisDays);
			pstmt.setInt(5,group.getM_Warehouse_ID());
			pstmt.setInt(6,group.getAD_Client_ID());
			pstmt.setInt(7,group.getAD_Client_ID());
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
