package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.api.UICallout;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MABCRank extends X_M_ABCRank{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static CLogger	s_log	= CLogger.getCLogger (MABCRank.class);

	/**
	 * @param ctx
	 * @param M_ABCRankLine_ID
	 * @param trx
	 */
	public MABCRank(Ctx ctx, int MABCRank_ID, Trx trx) {
		super(ctx, MABCRank_ID, trx);

	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trx
	 */
	public MABCRank(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}
	
	public static MABCRank[] getLines (Ctx ctx, MABCAnalysisGroup group, Trx trx)
	{
		ArrayList<MABCRank> list = new ArrayList<MABCRank> ();
		String sql = " SELECT * FROM M_ABCRank WHERE M_ABCAnalysisGroup_ID = ? AND IsActive = 'Y' " 
			       + " AND AD_Client_ID = ? Order By SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trx);
			pstmt.setInt(1, group.getM_ABCAnalysisGroup_ID());
			pstmt.setInt(2,group.getAD_Client_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MABCRank rl = new MABCRank(ctx, rs, trx);
				list.add(rl);
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
		//
		MABCRank[] lines = new MABCRank[list.size ()];
		list.toArray (lines);
		return lines;
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if ((this.is_Changed() || newRecord)&& this.getM_ABCRankSort_ID() !=0)
		{
			X_M_ABCRankSort sort = new X_M_ABCRankSort(getCtx(),this.getM_ABCRankSort_ID(),get_Trx());
			this.setIsBasedOnPriceList(sort.isBasedOnPriceList());
			if(isBasedOnPriceList() && getM_PriceList_Version_ID()==0)
			{
				s_log.saveError("PriceListMissing", "Select Price List Version for the Rank Sort Criteria" );
				return false;
			}
		}
		
		// set the correct organization
		MABCAnalysisGroup group = new MABCAnalysisGroup(getCtx(),getM_ABCAnalysisGroup_ID(),get_Trx());
		if(getAD_Org_ID() != group.getAD_Org_ID())
			setAD_Org_ID(group.getAD_Org_ID());
		
		if(getFrequency().equals(X_M_ABCRank.FREQUENCY_Daily))
		{
			if(getDuration().compareTo(BigDecimal.ONE)>0)
			{
				s_log.saveError("CycleCountFrequency"," Cycle count frequency can't be more then One per Day");
				return false;
			}
		}
		else if (getFrequency().equals(X_M_ABCRank.FREQUENCY_Weekly))
		{
			if(getDuration().compareTo(new BigDecimal(7))>0)
			{
				s_log.saveError("CycleCountFrequency"," Cycle count frequency can't be more then One per Day");
				return false;
			}
		}
		else if (getFrequency().equals(X_M_ABCRank.FREQUENCY_Monthly))
		{
			if(getDuration().compareTo(new BigDecimal(31))>0)
			{
				s_log.saveError("CycleCountFrequency"," Cycle count frequency can't be more then One per Day");
				return false;
			}
		}
		
		else if(getFrequency().equals(X_M_ABCRank.FREQUENCY_Quarterly))
		{
			if(getDuration().compareTo(new BigDecimal(92))>0)
			{
				s_log.saveError("CycleCountFrequency"," Cycle count frequency can't be more then One per Day");
				return false;
			}
		}
		
		else if(getFrequency().equals(X_M_ABCRank.FREQUENCY_Yearly))
		{
			if(getDuration().compareTo(new BigDecimal(366))>0)
			{
				s_log.saveError("CycleCountFrequency"," Cycle count frequency can't be more then One per Day");
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		MABCAnalysisGroup group = new MABCAnalysisGroup(getCtx(),getM_ABCAnalysisGroup_ID(),get_Trx());
		if(group.isValid())
		{
			group.setIsValid(false);
			return group.save(get_Trx());
		}
		return true;
		
	}
	
	@UICallout public void setM_ABCRankSort_ID (String oldM_ABCRankSort_ID,
			String newM_ABCRankSort_ID, int windowNo) throws Exception
			{
		if(newM_ABCRankSort_ID == null)
			return;
		Integer old_M_ABCRankSort_ID = 0;
		Integer M_ABCRankSort_ID = 0;
		
		if(oldM_ABCRankSort_ID != null && oldM_ABCRankSort_ID.length()!=0)
			old_M_ABCRankSort_ID = Integer.parseInt(oldM_ABCRankSort_ID);
		
		if(newM_ABCRankSort_ID!=null && newM_ABCRankSort_ID.length() !=0)
			M_ABCRankSort_ID = Integer.parseInt(newM_ABCRankSort_ID);
		
		if(M_ABCRankSort_ID == 0)
			return;
		
		if(M_ABCRankSort_ID.equals(old_M_ABCRankSort_ID))
			return;
		
		boolean IsBasedOnPriceList = false;
		if(M_ABCRankSort_ID.intValue()!=0)
		{
			String sql = "SELECT IsBasedOnPriceList FROM M_ABCRankSort WHERE M_ABCRankSort_ID = ? ";

			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try
			{
				pstmt = DB.prepareStatement (sql.toString(),get_Trx());
				pstmt.setInt(1,M_ABCRankSort_ID.intValue());
				rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					IsBasedOnPriceList = "Y".equals(rs.getString(1));	
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
		}
		setIsBasedOnPriceList(IsBasedOnPriceList);
		return;

			}

}
