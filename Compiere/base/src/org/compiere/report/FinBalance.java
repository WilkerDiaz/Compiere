/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.report;

import java.math.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  Financial Balance Maintenance Engine
 *
 *  @author Jorg Janke
 *  @version $Id: FinBalance.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class FinBalance extends SvrProcess
{
	/**	Logger						*/
	protected static final CLogger	s_log = CLogger.getCLogger (FinBalance.class);

	/** Acct Schema					*/
	private int			p_C_AcctSchema_ID = 0;
	/** Date From					*/
	private Timestamp	p_DateFrom = null;
	/** Balance Aggregation         */
	private int         p_Fact_Accumulation_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		//	Parameter
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DateFrom"))
				p_DateFrom = (Timestamp)element.getParameter();
			else if (name.equals("Fact_Accumulation_ID"))
				p_Fact_Accumulation_ID = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message to be translated
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		log.fine("C_AcctSchema_ID=" + p_C_AcctSchema_ID
			+ ",DateFrom=" + p_DateFrom);

		String msg = "";
		if (p_C_AcctSchema_ID != 0)
			msg = updateBalance(getCtx(), p_C_AcctSchema_ID,
				 p_DateFrom, get_TrxName(), p_Fact_Accumulation_ID, this);
		else
			msg = updateBalanceClient(getCtx(),
				 p_DateFrom, get_TrxName(), p_Fact_Accumulation_ID, this);
		return msg;
	}	//	doIt

	/**
	 * 	Delete Balances
	 * 	@param AD_Client_ID client
	 * 	@param C_AcctSchema_ID	accounting schema 0 for all
	 * 	@param dateFrom null for all or first date to delete
	 * 	@param trx transaction
	 * 	@param svrPrc optional server process
	 *  @return Message to be translated
	 */
	public static String deleteBalance (int AD_Client_ID, int C_AcctSchema_ID,
		Timestamp dateFrom, Trx trx,int Fact_Accumulation_ID,SvrProcess svrPrc)
	{
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer ("DELETE FROM Fact_Acct_Balance WHERE AD_Client_ID=?");
		params.add(new Integer(AD_Client_ID));
		if (C_AcctSchema_ID != 0)
		{
			sql.append (" AND C_AcctSchema_ID=?");
			params.add(new Integer(C_AcctSchema_ID));
		}
		if (dateFrom != null)
		{
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String finalDate = df.format(dateFrom);
			sql.append(" AND DateAcct>= TO_DATE('" + finalDate + "','DD-MM-YYYY')");
		}
		if(Fact_Accumulation_ID !=0)
		{
			sql.append(" AND Fact_Accumulation_ID = ?");
			params.add(new Integer(Fact_Accumulation_ID));
		}
		//
		int no = DB.executeUpdate(trx, sql.toString(), params);
		String msg = "@Deleted@=" + no;
		s_log.info("C_AcctSchema_ID=" + C_AcctSchema_ID
			+ ",DateAcct=" + dateFrom
			+ " #=" + no);
		if (svrPrc != null)
			svrPrc.addLog(0, dateFrom, new BigDecimal(no), "Deleted");
		trx.commit();
		//
		return msg;
	}	//	deleteBalance

	/**
	 * 	Update / Create Balances.
	 * 	Called from FinReport, FactAcctReset (indirect)
	 * 	@param AD_Client_ID client
	 * 	@param C_AcctSchema_ID	accounting schema 0 for all
	 * 	@param deleteFirst delete balances first
	 * 	@param dateFrom null for all or first date to delete/calculate
	 * 	@param trx transaction
	 * 	@param svrPrc optional server process
	 *  @return Message to be translated
	 */
	public static String updateBalance (Ctx ctx, int C_AcctSchema_ID,
		Timestamp dateFrom, Trx trx, int Fact_Accumulation_ID,
		SvrProcess svrPrc)
	{
		s_log.info("C_AcctSchema_ID=" + C_AcctSchema_ID
			+ "DateFrom=" + dateFrom);
		long start = System.currentTimeMillis();
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<MFactAccumulation> accums = null;
		int no = 0;
		
		if(Fact_Accumulation_ID == 0)
		{
			accums = MFactAccumulation.getAll(ctx, C_AcctSchema_ID);
			if (accums.size() ==0)
			{
				// Create a Balance aggregation of type Daily.
				
				MFactAccumulation defaultAccum = new MFactAccumulation(ctx,0, trx);
				defaultAccum.setAD_Client_ID(ctx.getAD_Client_ID());
				defaultAccum.setAD_Org_ID(ctx.getAD_Org_ID());
				defaultAccum.setC_AcctSchema_ID(C_AcctSchema_ID);
				defaultAccum.setBalanceAccumulation(X_Fact_Accumulation.BALANCEACCUMULATION_Daily);
				defaultAccum.setIsActive(true);
				defaultAccum.setIsDefault(true);
				defaultAccum.setIsActivity(true);
				defaultAccum.setIsBudget(true);
				defaultAccum.setIsBusinessPartner(true);
				defaultAccum.setIsCampaign(true);
				defaultAccum.setIsLocationFrom(true);
				defaultAccum.setIsLocationTo(true);
				defaultAccum.setIsProduct(true);
				defaultAccum.setIsProject(true);
				defaultAccum.setIsSalesRegion(true);
				defaultAccum.setIsUserList1(true);
				defaultAccum.setIsUserList2(true);
				defaultAccum.setIsUserElement1(true);
				defaultAccum.setIsUserElement2(true);
				if(!defaultAccum.save(trx))
				{
					s_log.log(Level.SEVERE, "Unable to create Default Balance Aggregation");
					return "Unable to create Default Balance Aggregation";
				}
				else
					accums.add(defaultAccum);					
			}
		}
		else
		{
			MFactAccumulation selectAccum = new MFactAccumulation(ctx,Fact_Accumulation_ID,trx);
			accums = new ArrayList<MFactAccumulation>();
			accums.add(selectAccum);
		}
		
		for (MFactAccumulation accum : accums)
		{
			dateFrom = MFactAccumulation.getDateFrom(accum, dateFrom);
			String type = accum.getBalanceAccumulation();
			String trunc = null;
			
			if (X_Fact_Accumulation.BALANCEACCUMULATION_Daily.equals(type))
				trunc = TimeUtil.TRUNC_DAY;
			else if (X_Fact_Accumulation.BALANCEACCUMULATION_CalendarWeek.equals(type))
			    trunc = TimeUtil.TRUNC_WEEK;
			else if (X_Fact_Accumulation.BALANCEACCUMULATION_CalendarMonth.equals(type))
				trunc = TimeUtil.TRUNC_MONTH;
			
			if(X_Fact_Accumulation.BALANCEACCUMULATION_PeriodOfACompiereCalendar.equals(type) &&
					!checkPeriod(accum,dateFrom))
			{
				s_log.log(Level.SEVERE, "Necessary Period doesn't exist for the calendar");
				return "Necessary Period doesn't exist for the calendar";
			}

			String dateClause = null;
		    if(X_Fact_Accumulation.BALANCEACCUMULATION_PeriodOfACompiereCalendar.equals(type)) 
		    {
		    	dateClause = " Period.StartDate ";

		    }
		    else
		    {
		    	dateClause = " TRUNC(a.DateAcct, '" + trunc + "' ) ";
		    }
			
		    // Delete the balances
			deleteBalance(ctx.getAD_Client_ID(), C_AcctSchema_ID,
						dateFrom, trx, accum.getFact_Accumulation_ID(),svrPrc);

			/** Insert		**/
			params = new ArrayList<Object>();
			String insert = "INSERT INTO Fact_Acct_Balance "
				+ "(AD_Client_ID, AD_Org_ID, AD_OrgTrx_ID, C_AcctSchema_ID, DateAcct,"
				+ " Account_ID, PostingType, Fact_Accumulation_ID, M_Product_ID, C_BPartner_ID,"
				+ "	C_Project_ID,	C_SalesRegion_ID,C_Activity_ID,"
				+ " C_Campaign_ID, C_LocTo_ID, C_LocFrom_ID, User1_ID, User2_ID, GL_Budget_ID,"
				+ " UserElement1_ID, UserElement2_ID, "
				+ " AmtAcctDr, AmtAcctCr, Qty) ";

			String select = " SELECT AD_Client_ID, AD_Org_ID, AD_OrgTrx_ID, C_AcctSchema_ID, " ;
			select = select +  dateClause ;	    
			select = select + " ,Account_ID, PostingType, " + accum.getFact_Accumulation_ID();
			if(accum.isProduct())
				select = select + " ,M_Product_ID ";
			else
				select = select + " , NULL ";
			
			if(accum.isBusinessPartner())
				select = select + " , C_BPartner_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isProject())
				select = select + " ,C_Project_ID ";
			else
				select = select + " , NULL ";
			
			if(accum.isSalesRegion())
				select = select + ", C_SalesRegion_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isActivity())
				select = select + " ,C_Activity_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isCampaign())
				select = select + " ,C_Campaign_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isLocationTo())
				select = select + " ,C_LocTo_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isLocationFrom())
				select = select + " ,C_LocFrom_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isUserList1())
				select = select + " ,User1_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isUserList2())
				select = select + " ,User2_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isBudget())
				select = select + " ,GL_Budget_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isUserElement1())
				select = select + " ,UserElement1_ID ";
			else
				select = select + " ,NULL ";
			
			if(accum.isUserElement2())
				select = select + " , UserElement2_ID ";
			else
				select = select + " ,NULL ";
			
			select = select + " ,COALESCE(SUM(AmtAcctDr),0), COALESCE(SUM(AmtAcctCr),0), COALESCE(SUM(Qty),0) ";
				
			String from =   " FROM Fact_Acct a ";
			if(X_Fact_Accumulation.BALANCEACCUMULATION_PeriodOfACompiereCalendar.equals(type))
			{
				from +=  " ,(SELECT StartDate,EndDate FROM C_Period " 
					+ " WHERE C_Year_ID IN (SELECT C_Year_ID " 
					+ " FROM C_Year WHERE C_Calendar_ID= ? ) "
					+ " AND IsActive='Y' AND PeriodType='S')  Period";
				params.add(new Integer(accum.getC_Calendar_ID()));
			}
			String where =  " WHERE C_AcctSchema_ID=? ";
			if(X_Fact_Accumulation.BALANCEACCUMULATION_PeriodOfACompiereCalendar.equals(type))
			{
				where += " AND a.DateAcct BETWEEN TRUNC(Period.StartDate,'DD') AND TRUNC(Period.EndDate,'DD') ";
			}
			params.add(new Integer(C_AcctSchema_ID));
			if (dateFrom != null)
			{
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				String finalDate = df.format(dateFrom);
				where += " AND DateAcct>= TO_DATE('" + finalDate + "','DD-MM-YYYY') ";
			}
			
			String groupBy = " GROUP BY AD_Client_ID,C_AcctSchema_ID, AD_Org_ID, "
				+ " AD_OrgTrx_ID,Account_ID, PostingType," + dateClause ;
			if (accum.isProduct())
				groupBy = groupBy + " ,M_Product_ID ";
			if(accum.isBusinessPartner())
				groupBy = groupBy + " ,C_BPartner_ID ";
			if(accum.isProject())
				groupBy = groupBy + " ,C_Project_ID ";
			if(accum.isSalesRegion())
				groupBy = groupBy + " ,C_SalesRegion_ID ";
			if(accum.isActivity())
				groupBy = groupBy + ", C_Activity_ID ";
			if(accum.isCampaign())
				groupBy = groupBy + " ,C_Campaign_ID ";
			if(accum.isLocationTo())
				groupBy = groupBy + ", C_LocTo_ID ";
			if(accum.isLocationFrom())
				groupBy = groupBy + ", C_LocFrom_ID ";
			if(accum.isUserList1())
				groupBy = groupBy + ", User1_ID" ;
			if(accum.isUserList2())
				groupBy = groupBy + ", User2_ID ";
			if(accum.isBudget())
				groupBy = groupBy + ", GL_Budget_ID ";
			if(accum.isUserElement1())
				groupBy = groupBy + ", UserElement1_ID ";
			if(accum.isUserElement2())
				groupBy = groupBy + ", UserElement2_ID ";

			String sql = insert + select + from + where + groupBy;
			no = DB.executeUpdate(trx, sql, params);
			s_log.config("Inserts=" + no);
			if (svrPrc != null)
				svrPrc.addLog(0, dateFrom, new BigDecimal(no), "Inserts in "+accum.toString());
			trx.commit();
		}

		start = System.currentTimeMillis() - start;
		s_log.info((start/1000) + " sec");
		return "#" + no;
	}	//	updateBalance

	/**
	 * 	Update Balance of Client
	 *	@param ctx context
	 *	@param deleteFirst delete first
	 * 	@param dateFrom null for all or first date to delete/calculate
	 * 	@param trx transaction
	 * 	@param svrPrc optional server process
	 *	@return info
	 */
	public static String updateBalanceClient (Ctx ctx,
		 Timestamp dateFrom, Trx trx, int Fact_Accumulation_ID, SvrProcess svrPrc)
	{
		int AD_Client_ID = ctx.getAD_Client_ID();
		StringBuffer info = new StringBuffer();
		MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(ctx, AD_Client_ID);
		for (MAcctSchema as : ass)
		{
			if (info.length() > 0)
				info.append(" - ");
			String msg = updateBalance(ctx, as.getC_AcctSchema_ID(),
				 dateFrom, trx, Fact_Accumulation_ID, svrPrc);
			info.append(as.getName()).append(":").append(msg);
		}
		return info.toString();
	}	//	updateBalanceClient
	
	private static boolean checkPeriod(MFactAccumulation accum, Timestamp dateFrom)
	{
		boolean retVal = true;
		String sql = "  SELECT 1 FROM fact_acct a "
		            + " WHERE NOT EXISTS( SELECT 1 " +
		            		            " FROM C_Period " +
										" WHERE C_Year_ID IN (SELECT C_Year_ID " +
											                " FROM C_Year " +
											                " WHERE C_Calendar_ID= ?) " +
										" AND a.dateacct BETWEEN TRUNC(StartDate,'DD') AND TRUNC(EndDate,'DD')+ 0.999988 " +
									    " AND c_period.IsActive='Y' AND PeriodType='S') ";
		if(dateFrom != null)
			sql += "  AND DateAcct >= ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, accum.get_Trx());
			pstmt.setInt (1, accum.getC_Calendar_ID());
			if(dateFrom !=null)
				pstmt.setTimestamp(2, dateFrom);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retVal = false;
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retVal;
	}


}	//	FinBalance
