package org.compiere.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.compiere.model.MPeriod;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Language;

public class AcctDistribution extends SvrProcess {

	/** AcctSchame Parameter			*/
	private int					p_C_AcctSchema_ID = 0;
	/** Posting Type					*/
	private String				p_PostingType = "A";
	/**	Period Parameter				*/
	private int					p_C_Period_ID = 0;
	private Timestamp			p_DateAcct_From = null;
	private Timestamp			p_DateAcct_To = null;
	/**	Org Parameter					*/
	private int					p_AD_Org_ID = 0;
	/**	Account Parameter				*/
	private int					p_Account_ID = 0;
	/**	BPartner Parameter				*/
	private int					p_C_BPartner_ID = 0;
	/**	SalesRegion Parameter			*/
	private int					p_C_SalesRegion_ID = 0;

	/**	Parameter Where Clause			*/
	private StringBuffer		m_parameterWhere = new StringBuffer();
	
	/**
	 * 	Set Start/End Date of Report - if not defined current Month
	 */
	private void setDateAcct()
	{
		//	Date defined
		if (p_DateAcct_From != null)
		{
			if (p_DateAcct_To == null)
				p_DateAcct_To = new Timestamp (System.currentTimeMillis());
			return;
		}
		//	Get Date from Period
		if (p_C_Period_ID == 0)
		{
			GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);		//	set to first of month
			p_DateAcct_From = new Timestamp (cal.getTimeInMillis());
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_YEAR, -1);		//	last of month
			p_DateAcct_To = new Timestamp (cal.getTimeInMillis());
		}
		else
		{
			MPeriod period = MPeriod.get(getCtx(), p_C_Period_ID);
			p_DateAcct_From = period.getStartDate();
			p_DateAcct_To = period.getEndDate();
		}
	}	//	setDateAcct

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		StringBuffer sb = new StringBuffer ("Record_ID=")
			.append(getRecord_ID());
		//	Parameter
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("PostingType"))
				p_PostingType = (String)element.getParameter(); 
			else if (name.equals("C_Period_ID"))
				p_C_Period_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DateAcct"))
			{
				p_DateAcct_From = (Timestamp)element.getParameter();
				p_DateAcct_To = (Timestamp)element.getParameter_To();
			}
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("Account_ID"))
				p_Account_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_SalesRegion_ID"))
				p_C_SalesRegion_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("#AD_PrintFormat_ID"))
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		setDateAcct();
		sb.append(" - DateAcct ").append(p_DateAcct_From).append("-").append(p_DateAcct_To);
		sb.append(" - Where=").append(m_parameterWhere);
		log.fine(sb.toString());
	}	//	prepare

	

	
	
	/**************************************************************************
	 *  Perform process.
	 *  @return Message to be translated
	 */
	@Override
	protected String doIt()
	{
		createDetailLines();
		return "";
	}	//	doIt

	private void createDetailLines()
	{
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer ("INSERT INTO T_Fact_Acct (AD_PInstance_ID,"
				+ " AD_Client_ID, AD_Org_ID, "
				+ " C_AcctSchema_ID, Account_ID, AccountValue, AccountName, DateAcct, " 
				+ " C_Period_ID, M_Product_ID, ProductValue, ProductName, C_SalesRegion_ID, " 
				+ " C_BPartner_ID, BPartnerValue, BPartnerName, Description, Fact_Acct_ID, " 
				+ " AmtAcctDr, AmtAcctCr, Name, Qty, AccountType, PostingType, "
				+ " AD_Table_ID, Record_ID) ");

		sb.append("SELECT ?")
			 	.append(", f.AD_Client_ID, f.AD_Org_ID, " 
			 	+ "f.C_AcctSchema_ID, f.Account_ID, ev.Value, ev.Name, f.DateAcct,"	
			 	+ "f.C_Period_ID, f.M_Product_ID,p.Value, p.Name, f.C_SalesRegion_ID,"
			 	+ "f.C_BPartner_ID, bp.Value, bp.Name, f.Description, f.Fact_Acct_ID, "
			 	+ "f.AmtAcctDr, f.AmtAcctCr, e.Name, Qty, ev.AccountType, f.PostingType, "
			 	+ "f.AD_Table_ID, f.Record_ID "
			 	+ "FROM Fact_Acct f "
			 		+ " INNER JOIN C_ElementValue ev ON (f.Account_ID=ev.C_ElementValue_ID)"
					+ " INNER JOIN AD_Table t ON (f.AD_Table_ID=t.AD_Table_ID)"
					+ " INNER JOIN AD_Element e ON (t.TableName||'_ID'=e.ColumnName) "
					+ " LEFT OUTER JOIN C_BPartner bp ON (bp.C_BPartner_ID=f.C_BPartner_ID) "
					+ " LEFT OUTER JOIN M_Product p ON (p.M_Product_ID=f.M_Product_ID) ");
					

		params.add(getAD_PInstance_ID());
		
		//	Mandatory C_AcctSchema_ID, PostingType
		m_parameterWhere.append(" WHERE C_AcctSchema_ID=? ")
						.append(" AND PostingType=? ");
		params.add(p_C_AcctSchema_ID);
		params.add(p_PostingType);

		if(p_DateAcct_From!=null && p_DateAcct_To!=null)
		{
			m_parameterWhere.append("AND DateAcct BETWEEN ? AND ? ");
			params.add(p_DateAcct_From);
			params.add(p_DateAcct_To);
		}
		else if(p_DateAcct_From!=null)
		{
			m_parameterWhere.append("AND DateAcct >= ? ");
			params.add(p_DateAcct_From);
		}
		else if(p_DateAcct_To!=null)
		{
			m_parameterWhere.append("AND DateAcct <= ? ");
			params.add(p_DateAcct_To);			
		}
		
		//	Optional Account_ID
		if (p_Account_ID != 0)
		{
			m_parameterWhere.append(" AND f.Account_ID=? ");
			params.add(p_Account_ID);
		}
		
		//	Optional Org
		if (p_AD_Org_ID != 0)
		{
			m_parameterWhere.append(" AND f.AD_Org_ID=? ");
			params.add(p_AD_Org_ID);
		}
		
		//	Optional BPartner
		if (p_C_BPartner_ID != 0)
		{
			m_parameterWhere.append(" AND f.C_BPartner_ID=? ");
			params.add(p_C_BPartner_ID);
		}

		if (p_C_SalesRegion_ID != 0)
		{	
			m_parameterWhere.append(" AND f.C_SalesRegion_ID=? ");
			params.add(p_C_SalesRegion_ID);
		}
		
		if(p_C_Period_ID != 0)
		{	
			m_parameterWhere.append(" AND f.C_Period_ID=? ");
			params.add(p_C_Period_ID);
		}

		sb.append(m_parameterWhere);
		//
		int no = DB.executeUpdate(get_TrxName(), sb.toString(), params);
		log.fine("#" + no);
		log.finest(sb.toString());

	   String sql = "SELECT DISTINCT t.AD_Table_ID, t.TableName, c.AD_Column_ID" 
		   			+ " FROM T_Fact_Acct tfa "
					+ " INNER JOIN AD_Table t ON (tfa.AD_Table_ID=t.AD_Table_ID) "
					+ " LEFT OUTER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID AND c.ColumnName = 'C_DocType_ID') "
		   			+ "WHERE AD_PInstance_ID=?  ";
	   
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAD_PInstance_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				int AD_Table_ID = rs.getInt(1);
				String tableName = rs.getString(2);
				boolean hasDocType = rs.getInt(3) != 0;
				updateDocumentDetails(AD_Table_ID, tableName, hasDocType);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
	   
	}	//	createDetailLines

	private void updateDocumentDetails(int AD_Table_ID, String tableName, boolean hasDocType)
	{
		int no = 0;
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer sb;
		
		if(!hasDocType)
		{	
			String sql_select = "SELECT DocumentNo "
				+ "FROM " + tableName +" tn "
				+ "WHERE tfa.Record_ID=tn." + tableName + "_ID";
			
			sb = new StringBuffer ("UPDATE T_Fact_Acct tfa SET DocumentNo=( ")
								.append(sql_select).append(") "
								+ "WHERE AD_PInstance_ID=? ")
								.append("AND tfa.AD_Table_ID= ?");
	
		}
		else
		{
			String sql_select = "SELECT tn.DocumentNo, d.C_DocType_ID, d.PrintName "
									+ "FROM " + tableName +" tn "
										+ "INNER JOIN C_DocType d ON (tn.C_DocType_ID=d.C_DocType_ID)"
									+ "WHERE tfa.Record_ID=tn." + tableName + "_ID";
		
			sb = new StringBuffer ("UPDATE T_Fact_Acct tfa SET (DocumentNo, C_DocType_ID, Name)=(")
								.append(sql_select).append(") "
								+ "WHERE AD_PInstance_ID=?")
								.append("AND tfa.AD_Table_ID=?");
		}

		
		params.add(getAD_PInstance_ID());
		params.add(AD_Table_ID);
		
		no = DB.executeUpdate(get_TrxName(), sb.toString(), params);
		log.finest("Update - " + sb);		
		log.fine("AD_Table_ID : " + AD_Table_ID + " Table Name : " + tableName + " : Updated # : "+no);
	}
}
