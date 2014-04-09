package org.compiere.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.compiere.model.MCalendar;
import org.compiere.model.MRole;
import org.compiere.model.MYear;
import org.compiere.model.MPeriod;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.QueryUtil;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;


public class CalendarCheck extends SvrProcess {
	private int         p_C_Calendar_ID = 0;
	private int         p_AD_Org_ID = 0;
	private List<Integer> calendarIDs = new ArrayList<Integer>();

	public CalendarCheck() {

	}


	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {

		log.info("C_Calendar_ID=" +  p_C_Calendar_ID);		

		//  Delete (just to be sure)
		String  sql = new String  ("DELETE FROM T_CalendarReview WHERE AD_PInstance_ID=?");
		DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());

		String stmt = "Select  C_Calendar_ID FROM C_Calendar WHERE IsActive='Y'  ";

		if (p_C_Calendar_ID != 0)
			stmt=stmt.concat(" AND C_Calendar_ID=? ");

		if(p_AD_Org_ID!=0)			 
			stmt= stmt.concat(" AND  AD_Org_ID =?");

		stmt= stmt.concat(" ORDER BY   C_Calendar_ID");
		MRole role = MRole.getDefault(getCtx(), false);
		stmt = role.addAccessSQL(stmt, "C_Calendar", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		if(stmt!=null)
		{
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(stmt,get_Trx());
				if (p_C_Calendar_ID != 0)
				{
					pstmt.setInt(1, p_C_Calendar_ID );
					if (p_AD_Org_ID!=0 )
						pstmt.setInt(2, p_AD_Org_ID );
				}
				else
				{
					if(p_AD_Org_ID!=0)
						pstmt.setInt(1, p_AD_Org_ID );	
				}					 
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					if(!calendarIDs.contains(rs.getInt(1)))						
						calendarIDs.add(rs.getInt(1));
				}

			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql.toString(), e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}

		for(Integer calendarID :  calendarIDs )
		{  

			//Get all the standard periods ordered by the start date in this Calendar
			MPeriod[] stdperiods1 =MPeriod.getAllPeriodsInCalendar(calendarID, "S", getCtx(),get_Trx());
			Timestamp currEndDate;
			Timestamp nextStartDate;

			//Check for gaps between std. periods within an year and between years 	

			for (int j=0; j< stdperiods1.length-1;j++)
			{

				currEndDate=stdperiods1[j].getEndDate();
				nextStartDate=stdperiods1[j+1].getStartDate();
				float days =(nextStartDate.getTime() - currEndDate.getTime())/(float)(1000*24*60*60);

				if (days > 1.157407407E-5)

				{

					if(stdperiods1[j].getC_Year_ID() != stdperiods1[j+1].getC_Year_ID()) 
						addTo_T_CalendarReview(calendarID,stdperiods1[j+1].getC_Year_ID(),stdperiods1[j+1].getC_Period_ID(),Msg.getMsg(getCtx(), "CalYearGap"));
					else
						addTo_T_CalendarReview(calendarID,stdperiods1[j].getC_Year_ID(),stdperiods1[j+1].getC_Period_ID(),Msg.getMsg(getCtx(), "CalPeriodGap"));
				}	
			} 		

			//check for non-overlapping standard periods in the Calendar
			for (MPeriod stdperiod1: stdperiods1)
			{			  

				for (MPeriod stdperiod : stdperiods1)
				{
					if (stdperiod.getC_Period_ID()!= stdperiod1.getC_Period_ID()&& (TimeUtil.isValid(stdperiod.getStartDate(),stdperiod.getEndDate(),stdperiod1.getStartDate()) ||
							TimeUtil.isValid(stdperiod.getStartDate(),stdperiod.getEndDate(),stdperiod1.getEndDate())) )	
					{
						addTo_T_CalendarReview(calendarID,stdperiod1.getC_Year_ID(), stdperiod1.getC_Period_ID(),Msg.getMsg(getCtx(), "CalStdPeriodOverlap"));
						break;					 
					} 
				}
			} 

			//get all the years in the calendar
			MYear[] years= MPeriod.getAllYearsInCalendar(calendarID, getCtx(),get_Trx());

			for (MYear year : years)
			{
				// Get all the periods, Std. periods and Adj. periods in an year ordered by the start date
				MPeriod[] allperiods=MPeriod.getAllPeriodsInYear( year.getC_Year_ID(),null,getCtx(),(Trx) null);
				MPeriod[] stdperiods=MPeriod.getAllPeriodsInYear( year.getC_Year_ID(),"S",getCtx(),(Trx) null);
				MPeriod[] adjperiods= MPeriod.getAllPeriodsInYear( year.getC_Year_ID(),"A" , getCtx(),get_Trx());

				//Check if the first period in this year has period number 1 or 0
				if ( allperiods.length>0 && allperiods[0].getPeriodNo() != 1 && allperiods[0].getPeriodNo() != 0)
					addTo_T_CalendarReview(calendarID,stdperiods[0].getC_Year_ID(),stdperiods[0].getC_Period_ID(),Msg.getMsg(getCtx(), "CalFirstPeriod"));

				//Check if all the periods in the year have periods numbers in ascending order and consecutive
				for (int i=0; i<allperiods.length-1;i++)
				{

					int diff= allperiods[i+1].getPeriodNo()-allperiods[i ].getPeriodNo();
					if (diff != 1)
						addTo_T_CalendarReview(calendarID,allperiods[i].getC_Year_ID(),allperiods[i+1].getC_Period_ID(),Msg.getMsg(getCtx(), "CalPeriodNoAsc"));

				} 

				//check for overlapping adjusting periods in a year
				for (MPeriod adjperiod : adjperiods)	 
				{ 	 
					boolean  startflag= false;
					boolean  endflag= false;
					for (MPeriod stdperiod : stdperiods)
					{
						if (TimeUtil.isValid(stdperiod.getStartDate(),stdperiod.getEndDate(),adjperiod.getStartDate()) )
							startflag= true;
						if (TimeUtil.isValid(stdperiod.getStartDate(),stdperiod.getEndDate(),adjperiod.getEndDate()))
							endflag= true;
						if (startflag == true && endflag == true)
							break;
					}
					if (startflag == false  || endflag == false) 
						addTo_T_CalendarReview(calendarID,adjperiod.getC_Year_ID(), adjperiod.getC_Period_ID(),Msg.getMsg(getCtx(), "CalAdjPeriodOverlap"));

				} 
			} 
		} 	

		return null;	
	}

	/**
	 * Add the Error Details to to T_CalendarReview table
	 * @param calendarID  calendar
	 * @param yearID      year
	 * @param periodID    period 
	 * @param errorMsg    Error Message
	 */
	private void addTo_T_CalendarReview(int calendarID, int yearID, int periodID, String errorMsg)
	{
		int T_CalendarReview_ID = QueryUtil.getSQLValue(get_Trx(), "SELECT T_CalendarReview_ID_Seq.NEXTVAL FROM DUAL");
		MCalendar calendar = MCalendar.get(getCtx(), calendarID);
		String  sql =  "INSERT INTO T_CalendarReview "
			+ "(AD_PInstance_ID,T_CalendarReview_ID, C_Calendar_ID, C_Year_ID, C_Period_ID ,"
			+ " AD_Client_ID, AD_Org_ID,ErrorMsg) VALUES ( " + getAD_PInstance_ID()+ " , " + T_CalendarReview_ID+ " , "+calendarID+" , "+ yearID
			+ " , " + periodID +" , "+ calendar.getAD_Client_ID() + " , " + p_AD_Org_ID +" , "+ " '"+errorMsg + " '" + " ) ";			
		DB.executeUpdate(get_Trx(), sql);	
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null);
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = element.getParameterAsInt();
			else if (name.equals("C_Calendar_ID"))
				p_C_Calendar_ID = element.getParameterAsInt();	
			else if (name.equals("#AD_PrintFormat_ID"))
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}

}