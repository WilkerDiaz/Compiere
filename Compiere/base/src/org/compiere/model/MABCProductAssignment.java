package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;

public class MABCProductAssignment extends X_M_ABCProductAssignment{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static CLogger	s_log	= CLogger.getCLogger (MABCProductAssignment.class);

	/**
	 * @param ctx
	 * @param M_Routing_ID
	 * @param trx
	 */
	public MABCProductAssignment(Ctx ctx, int MABCProductAssignment_ID, Trx trx) {
		super(ctx, MABCProductAssignment_ID, trx);

	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trx
	 */
	public MABCProductAssignment(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}
	
	public static MABCProductAssignment getForProduct(Ctx ctx, int product, int M_ABCAnalysisGroup_ID, Trx trx)
	{
		MABCProductAssignment retVal = null;
		String sql = " SELECT p.M_Product_ID, COALESCE(M_ABCProductAssignment_ID,0) "
			       + " FROM M_Product p "
			       + " LEFT OUTER JOIN M_ABCProductAssignment pa ON (pa.M_Product_ID = p.M_Product_ID "
			                                                     + " AND pa.M_ABCAnalysisGroup_ID = ? "
			                                                     + " AND pa.AD_Client_ID = p.AD_Client_ID) "
		           + " WHERE p.AD_Client_ID = ? "
		           + " AND p.M_Product_ID = ? ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql,trx);
			pstmt.setInt(1, M_ABCAnalysisGroup_ID);
			pstmt.setInt(2, ctx.getAD_Client_ID());
			pstmt.setInt(3, product);
			rs = pstmt.executeQuery();
			if (rs.next())
				retVal = new MABCProductAssignment(ctx,rs.getInt(2),trx);
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
		return retVal;
	}
	
	public static boolean verifyProducts(int count,MABCAnalysisGroup group, MABCRank line)
	{
		s_log.log(Level.FINE,"Get the number of products still left to be counted for rank line '"
				+ line.getABCRank()+"'");
		boolean retVal = false;
		
		String sql = " SELECT COUNT(1) "
			       + " FROM M_ABCProductAssignment pa "
			       + " INNER JOIN M_Product p ON (pa.M_Product_ID = p.M_Product_ID AND p.IsActive = 'Y') "
			       + " WHERE pa.M_ABCAnalysisGroup_ID = ? "
			       + " AND   pa.M_ABCRank_ID = ? "
			       + " AND   pa.AD_Client_ID = ? "
			       + " AND   pa.IsCounted = 'N' "
			       + " AND   pa.IsActive = 'Y' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int tempCount = 0;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(),line.get_Trx());
			pstmt.setInt(1, line.getM_ABCAnalysisGroup_ID());
			pstmt.setInt(2, line.getM_ABCRank_ID());
			pstmt.setInt(3, line.getAD_Client_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				tempCount = rs.getInt(1);
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
		
		if (tempCount == 0)
		{
			s_log.log(Level.FINE,"Reset the flag as counting will begin again for rank line '" 
					+ line.getABCRank()+"'");
			
			sql = " UPDATE M_ABCProductAssignment SET IsCounted = 'N' "
			    + " WHERE M_ABCAnalysisGroup_ID = ? "
			    + " AND   M_ABCRank_ID = ? "
			    + " AND   AD_Client_ID = ? "
			    + " AND   IsActive = 'Y' ";
			
			ArrayList< Object > params = new ArrayList< Object >();
			params.add(line.getM_ABCAnalysisGroup_ID());
			params.add(line.getM_ABCRank_ID());
			params.add(line.getAD_Client_ID());
			
			int updated = DB.executeUpdate(line.get_Trx(), sql,params);
			if(updated <0)
			{
				s_log.log(Level.WARNING,"Unable to update the records for rank line '" + line.getABCRank()+"'");
				retVal= false;
			}
			else
			{
				s_log.log(Level.FINE,"Updated: "+ updated + "for rank line '"+ line.getABCRank()+ "'");
				retVal= true;
			}
		
		}
		else 
		{
			s_log.log(Level.FINE,"Products left for counting: "+ tempCount);
			retVal= true;
		}
		
		return retVal;
	}
	
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if(newRecord)
		{
			MABCRank rankline = new MABCRank(getCtx(),getM_ABCRank_ID(),get_Trx());
			setM_ABCAnalysisGroup_ID(rankline.getM_ABCAnalysisGroup_ID());
			if(getAD_Org_ID()!=rankline.getAD_Org_ID())
				setAD_Org_ID(rankline.getAD_Org_ID());
		}

		return true;
	}
	
	public void setDates(MInventory inv)
	{
		setDateLastCounted(inv.getUpdated());
		MABCRank line = new MABCRank(getCtx(),getM_ABCRank_ID(),get_Trx());
		int multiplier = 0;
		
		//calculate frequency of cycle count
		if(line.getFrequency().equals("D"))
			multiplier = 1;
		else if(line.getFrequency().equals("W"))
			multiplier = 7;
		else if (line.getFrequency().equals("M"))
			multiplier = 30;
		else if(line.getFrequency().equals("Q"))
			multiplier = 90;
		else if(line.getFrequency().equals("Y"))
			multiplier = 365;
		
		int daysAdd = new BigDecimal(multiplier).divide(line.getDuration(),0,BigDecimal.ROUND_CEILING).intValue();
		Timestamp DateNextCount = TimeUtil.addDays(getDateLastCounted(),daysAdd);
		setDateNextCount(DateNextCount);
		
	}
	
	protected boolean beforeDelete ()
	{
		boolean retVal = true;
		MABCAnalysisGroup group = new MABCAnalysisGroup(getCtx(),getM_ABCAnalysisGroup_ID(),get_Trx());
		
		//check if there are any open documents for the product being deleted
		String sql = " SELECT 1 FROM M_Inventory inv "
			       + " INNER JOIN M_InventoryLine invline ON (inv.M_Inventory_ID = invline.M_Inventory_ID) "
			       + " WHERE invline.M_ABCAnalysisGroup_ID IS NOT NULL "
			       + " AND invline.M_Product_ID = ? "
			       + " AND inv.M_Warehouse_ID = ? "
			       + " AND inv.DocStatus IN ('DR','IP') ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_Product_ID());
			pstmt.setInt (2, group.getM_Warehouse_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retVal = false;
				s_log.saveError("Open Cycle Count Document for the product", "Open Cycle Count Document for the product");
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
		return retVal;
	}
}
