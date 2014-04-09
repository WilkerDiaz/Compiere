package org.compiere.model;

import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;

public class MReturnPolicy extends X_M_ReturnPolicy {
    /** Logger for class MReturnPolicy */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MReturnPolicy.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MReturnPolicy(Ctx ctx, int M_ReturnPolicy_ID, Trx trx) {
		super(ctx, M_ReturnPolicy_ID, trx);
	}

	public MReturnPolicy(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);

	}

	/**************************************************************************
	 * 	Get Lines of Return Policy for a product
	 * 	@param whereClause where clause or null (starting with AND)
	 * 	@param orderClause order clause
	 * 	@return lines
	 */
	int getProductLine (int M_Product_ID)
	{
		StringBuffer sql = new StringBuffer("SELECT M_ReturnPolicyLine_ID FROM M_ReturnPolicyLine WHERE M_ReturnPolicy_ID =? ");
		int M_ReturnPolicyLine_ID = 0;
		
		if (M_Product_ID != 0)
			sql.append("AND M_Product_ID = ?  ");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, getM_ReturnPolicy_ID());
	
			if (M_Product_ID != 0)
				pstmt.setInt(2, M_Product_ID);
			
			rs = pstmt.executeQuery();
			if (rs.next())
				M_ReturnPolicyLine_ID = rs.getInt(1);				
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
		//
		return M_ReturnPolicyLine_ID;
	}	//	getProductLine

	/**************************************************************************
	 * 	Get Lines of Return Policy for a product
	 * 	@param whereClause where clause or null (starting with AND)
	 * 	@param orderClause order clause
	 * 	@return lines
	 */
	int getProductCategoryLine (int M_Product_Category_ID)
	{
		StringBuffer sql = new StringBuffer("SELECT M_ReturnPolicyLine_ID FROM M_ReturnPolicyLine WHERE M_ReturnPolicy_ID =? ");
		int M_ReturnPolicyLine_ID = 0;
		
		if (M_Product_Category_ID != 0)
			sql.append("AND M_Product_ID IS NULL AND M_Product_Category_ID = ?  ");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, getM_ReturnPolicy_ID());
			
			if (M_Product_Category_ID != 0)
				pstmt.setInt(2, M_Product_Category_ID);
			
			rs = pstmt.executeQuery();
			if (rs.next())
				M_ReturnPolicyLine_ID = rs.getInt(1);				
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
		//
		return M_ReturnPolicyLine_ID;
	}	//	getProductCategoryLine

	/**************************************************************************
	 * 	Get Lines of Return Policy for a product
	 * 	@param whereClause where clause or null (starting with AND)
	 * 	@param orderClause order clause
	 * 	@return lines
	 */
	boolean policyHasLines ()
	{
		StringBuffer sql = new StringBuffer("SELECT count(*) FROM M_ReturnPolicyLine WHERE M_ReturnPolicy_ID =? ");
		int lineCount=0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, getM_ReturnPolicy_ID());
			
			rs = pstmt.executeQuery();
			if (rs.next())
				lineCount = rs.getInt(1);				
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
		//
		return lineCount != 0;
	}	//	getProductLine

	public boolean checkReturnPolicy(Timestamp shipDate, Timestamp returnDate)
	{
		if(policyHasLines())
			return true;

		long timeFrame = getTimeFrame()* 24 * 60 * 60 ; // Timeframe in milliseconds
		// If timeFrame is 0, returns are not allowed
		if(timeFrame == 0)
			return false;
		
		log.fine("ShipDate : "+shipDate.toString() + " ReturnDate : "+returnDate.toString() + " TimeFrame : " +timeFrame);
		Timestamp allowedDate = new Timestamp(timeFrame * 1000 + shipDate.getTime() );
		
		log.fine("Allowed Date : "+allowedDate.toString());
		if(returnDate.after(allowedDate))
			return false;
		return true;
	}

	public boolean checkReturnPolicy(Timestamp shipDate, Timestamp returnDate, int M_Product_ID)
	{

		int M_ReturnPolicyLine_ID = getProductLine(M_Product_ID);
		if (M_ReturnPolicyLine_ID == 0)
		{
			MProduct product = new MProduct (getCtx(), M_Product_ID, get_Trx());
			M_ReturnPolicyLine_ID = getProductCategoryLine(product.getM_Product_Category_ID());
		}
		
		long timeFrame; 
		if (M_ReturnPolicyLine_ID == 0)
			timeFrame = getTimeFrame()* 24 * 60 * 60 ; // Timeframe in milliseconds
		else
		{
			MReturnPolicyLine rpolicyLine = new MReturnPolicyLine (getCtx(), M_ReturnPolicyLine_ID, get_Trx());
			timeFrame = rpolicyLine.getTimeFrame()* 24 * 60 * 60 ; // Timeframe in milliseconds
		}

		if(timeFrame == 0)
			return false;

		log.fine("ShipDate : "+shipDate.toString() + " ReturnDate : "+returnDate.toString() + " TimeFrame : " +timeFrame);
		Timestamp allowedDate = new Timestamp(timeFrame * 1000 + shipDate.getTime() );
		
		log.fine("Allowed Date : "+allowedDate.toString());
		if(returnDate.after(allowedDate))
			return false;
		return true;
	}
	
	/**
	 * 	Get Default MReturnPolicy
	 *	@param ctx context
	 *	@return MReturnPolicy
	 */
	public static int getDefault (Ctx ctx)
	{
		int AD_Client_ID = ctx.getAD_Client_ID();
		int rPolicy_ID = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT M_ReturnPolicy_ID FROM M_ReturnPolicy"
			+ " WHERE IsDefault='Y' AND IsActive='Y' AND AD_Client_ID=? ";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				rPolicy_ID = rs.getInt(1);
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
		
		return rPolicy_ID;
	}	//	get

	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MReturnPolicy[").append(get_ID())
	        .append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    } //	toString
	
}
