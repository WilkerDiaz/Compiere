/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.acct;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.report.core.*;
import org.compiere.util.*;
import org.compiere.vos.*;


/**
 *  Account Viewer State - maintaines State information for the Account Viewer
 *
 *  @author Jorg Janke
 *  @version  $Id: AcctViewerData.java,v 1.3 2006/08/10 01:00:27 jjanke Exp $
 */
public class AcctViewerData
{
	/**
	 *  Constructor
	 *	@param ctx context
	 *  @param windowNo window no
	 *  @param ad_Client_ID client
	 * 	@param ad_Table_ID table
	 */
	public AcctViewerData (Ctx ctx, int windowNo, int ad_Client_ID, int ad_Table_ID)
	{
		WindowNo = windowNo;
		AD_Client_ID = ad_Client_ID;
		if (AD_Client_ID == 0)
			AD_Client_ID = ctx.getContextAsInt( WindowNo, "AD_Client_ID");
		if (AD_Client_ID == 0)
			AD_Client_ID = ctx.getContextAsInt( "AD_Client_ID");
		AD_Table_ID = ad_Table_ID;
		//
		ASchemas = MAcctSchema.getClientAcctSchema(ctx, AD_Client_ID);
		ASchema = ASchemas[0];
		

		// initialize the sort by and group field values
		for( int i = 0; i < s_numSortByFields; ++i )
		{
			sortBy[i] = "";
			group[i] = false;
		}
		
	}   //  AcctViewerData

	/** Window              */
	public int              WindowNo;
	/** Client				*/
	public int              AD_Client_ID;
	/** All Acct Schema		*/
	public MAcctSchema[]    ASchemas = null;
	/** This Acct Schema	*/
	public MAcctSchema      ASchema = null;

	//  Selection Info
	/** Document Query		*/
	public boolean          documentQuery = false;
	/** Acct Schema			*/
	public int              C_AcctSchema_ID = 0;
	/** Posting Type		*/
	public String			PostingType = "";
	/** Organization		*/
	public int              AD_Org_ID = 0;
	/** Date From			*/
	public Timestamp        DateFrom = null;
	/** Date To				*/
	public Timestamp        DateTo = null;

	//  Dodument Table Selection Info
	/** Table ID			*/
	public int              AD_Table_ID;
	/** Record				*/
	public int              Record_ID;

	/** Containing Column and Query     */
	public HashMap<String,String>	whereInfo = new HashMap<String,String>();
	/** Containing TableName and AD_Table_ID    */
	public HashMap<String,Integer>	tableInfo = new HashMap<String,Integer>();

	//  Display Info
	/** Display Qty			*/
	public boolean          displayQty = false;
	/** Display Source Surrency	*/
	public boolean          displaySourceAmt = false;
	/** Display Document info	*/
	public boolean          displayDocumentInfo = false;
	
	/** Number of Sort By Fields */
	public static final int s_numSortByFields = 4;
	
	public String[] sortBy = new String[s_numSortByFields];
	public boolean[] group = new boolean[s_numSortByFields];

	/** Leasing Columns		*/
	private int				m_leadingColumns = 0;
	/** UserElement1 Reference	*/
	private String 			m_ref1 = null;
	/** UserElement2 Reference	*/
	private String 			m_ref2 = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AcctViewerData.class);

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		ASchemas = null;
		ASchema = null;
		//
		whereInfo.clear();
		whereInfo = null;
		//
		Env.clearWinContext(WindowNo);
	}   //  dispose

	



	/**
	 * Get Accounting Schema
	 * 
	 * @return the accounting schema
	 */
	public ListBoxVO getAcctSchema()
	{
		ArrayList< NamePair > options = new ArrayList< NamePair >();
		for (MAcctSchema element : ASchemas)
			options.add( new KeyNamePair( element.getC_AcctSchema_ID(), element.getName() ) );
		return new ListBoxVO( options, null );
	} // getAcctSchema


	/**
	 * Get Posting Type
	 * 
	 * @return
	 */
	public ListBoxVO getPostingType()
	{
		int AD_Reference_ID = 125;
		ArrayList< NamePair > options = new ArrayList< NamePair >( Arrays.asList( MRefList.getList(Env.getCtx(), AD_Reference_ID, true ) ) );
		return new ListBoxVO( options, null );
	} // getPostingType
	

	/**
	 * Get Table with ValueNamePair (TableName, translatedKeyColumnName) and
	 * tableInfo with (TableName, AD_Table_ID) and select the entry for
	 * AD_Table_ID
	 * 
	 * @return
	 */
	public ListBoxVO getTable( Ctx ctx )
	{
		ArrayList< NamePair > options = new ArrayList< NamePair >();
		String defaultKey = null;
		//
		String sql = "SELECT AD_Table_ID, TableName FROM AD_Table t "
			+ "WHERE EXISTS (SELECT * FROM AD_Column c"
			+ " WHERE t.AD_Table_ID=c.AD_Table_ID AND c.ColumnName='Posted')"
			+ " AND IsView='N' ORDER BY TableName";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt(1);
				String tableName = rs.getString(2);
				String name = Msg.translate( ctx, tableName+"_ID");
				//
				ValueNamePair pp = new ValueNamePair(tableName, name);
				options.add(pp);
				tableInfo.put (tableName, Integer.valueOf(id));
				if (id == AD_Table_ID)
					defaultKey = pp.getValue();
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		return new ListBoxVO( options, defaultKey );
	}   //  getTable

	/**
	 *  Get Org
	 *
	 *  @param cb JComboBox to be filled
	 */
	public ListBoxVO getOrg()
	{
		ArrayList< NamePair > options = new ArrayList< NamePair >();
		KeyNamePair pp = new KeyNamePair(0, "");
		options.add( pp );
		String sql = "SELECT AD_Org_ID, Name FROM AD_Org WHERE AD_Client_ID=? ORDER BY Value";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				options.add( new KeyNamePair( rs.getInt( 1 ), rs.getString( 2 ) ) );
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return new ListBoxVO( options, null );
	}   //  getOrg

	/**
	 *  Get Button Text
	 *
	 *  @param tableName table
	 *  @param columnName column
	 *  @param selectSQL sql
	 *  @return Text on button
	 */
	protected String getButtonText (String tableName, String columnName, String selectSQL)
	{
		//  SELECT (<embedded>) FROM tableName avd WHERE avd.<selectSQL>
		StringBuffer sql = new StringBuffer ("SELECT (");
		Language language = Env.getLanguage(Env.getCtx());
		sql.append(MLookupFactory.getLookup_TableDirEmbed(language, columnName, "avd"))
			.append(") FROM ").append(tableName).append(" avd WHERE avd.").append(selectSQL);
		String retValue = "<" + selectSQL + ">";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getString(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		return retValue;
	}   //  getButtonText

	/**************************************************************************

	/**
	 *  Create Query and submit
	 *  @return Report Model
	 */
	public RModel query( Ctx ctx )
	{
		//  Set Where Clause
		StringBuffer whereClause = new StringBuffer();
		//  Add Organization
		if (C_AcctSchema_ID != 0)
			whereClause.append(RModel.TABLE_ALIAS)
				.append(".C_AcctSchema_ID=").append(C_AcctSchema_ID);

		//	Posting Type Selected
		if (PostingType != null && PostingType.length() > 0)
		{
			if (whereClause.length() > 0)
				whereClause.append(" AND ");
			whereClause.append(RModel.TABLE_ALIAS)
				.append(".PostingType='").append(PostingType).append("'");
		}
		
		//
		if (documentQuery)
		{
			if (whereClause.length() > 0)
				whereClause.append(" AND ");
			whereClause.append(RModel.TABLE_ALIAS).append(".AD_Table_ID=").append(AD_Table_ID)
				.append(" AND ").append(RModel.TABLE_ALIAS).append(".Record_ID=").append(Record_ID);
		}
		else
		{
			//  get values (Queries)
			Iterator<String> it = whereInfo.values().iterator();
			while (it.hasNext())
			{
				String where = it.next();
				if (where != null && where.length() > 0)    //  add only if not empty
				{
					if (whereClause.length() > 0)
						whereClause.append(" AND ");
					whereClause.append(RModel.TABLE_ALIAS).append(".").append(where);
				}
			}
			if (DateFrom != null || DateTo != null)
			{
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				if (DateFrom != null && DateTo != null)
					whereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct,'DD') BETWEEN ")
						.append(DB.TO_DATE(DateFrom)).append(" AND ").append(DB.TO_DATE(DateTo));
				else if (DateFrom != null)
					whereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct,'DD') >= ")
						.append(DB.TO_DATE(DateFrom));
				else    //  DateTo != null
					whereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct,'DD') <= ")
						.append(DB.TO_DATE(DateTo));
			}
			//  Add Organization
			if (AD_Org_ID != 0)
			{
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(RModel.TABLE_ALIAS).append(".AD_Org_ID=").append(AD_Org_ID);
			}
		}

		//  Set Order By Clause
		StringBuffer orderClause = new StringBuffer();
		boolean first = true;
		for( int i = 0; i < s_numSortByFields; ++i )
		{
			if( sortBy[i] != null && sortBy[i].length() > 0 )
			{
				if( !first )
					orderClause.append( "," );
				first = false;
				orderClause.append( RModel.TABLE_ALIAS ).append( "." ).append( sortBy[i] );
			}
		}
		if (orderClause.length() == 0)
			orderClause.append(RModel.TABLE_ALIAS).append(".Fact_Acct_ID");

		RModel rm = getRModel( ctx );

		//  Groups
		// Always group everything until the last selected one, as it's
		// functionally undefined to group by the second column without also
		// grouping by the first

		int lastGroup = -1;
		for( int i = 0; i < s_numSortByFields; ++i )
		{
			if( group[i] )
				lastGroup = i;
		}
		
		for( int i = 0; i <= lastGroup; ++i )
		{
			if( sortBy[i] != null && sortBy[i].length() > 0 )
				rm.setGroup( sortBy[i] );
		}

		//  Totals
		rm.setFunction("AmtAcctDr", RModel.FUNCTION_SUM);
		rm.setFunction("AmtAcctCr", RModel.FUNCTION_SUM);

		rm.query( ctx, whereClause.toString(), orderClause.toString() );

		return rm;
	}   //  query

	/**
	 *  Create Report Model (Columns)
	 *  @return Report Model
	 */
	public RModel getRModel( Ctx ctx )
	{
		RModel rm = new RModel("Fact_Acct");
		//  Add Key (Lookups)
		ArrayList<String> keys = createKeyColumns();
		int max = m_leadingColumns;
		if (max == 0)
			max = keys.size();
		for (int i = 0; i < max; i++)
		{
			String column = keys.get(i);
			if (column != null && column.startsWith("Date"))
				rm.addColumn(new RColumn(ctx, column, DisplayTypeConstants.Date));
			else if (column != null && column.toUpperCase().endsWith("_ID"))
				rm.addColumn(new RColumn(ctx, column, DisplayTypeConstants.TableDir));
		}
		//  Main Info
		rm.addColumn(new RColumn(ctx, "AmtAcctDr", DisplayTypeConstants.Amount));
		rm.addColumn(new RColumn(ctx, "AmtAcctCr", DisplayTypeConstants.Amount));
		if (displaySourceAmt)
		{
			if (!keys.contains("DateTrx"))
				rm.addColumn(new RColumn(ctx, "DateTrx", DisplayTypeConstants.Date));
			rm.addColumn(new RColumn(ctx, "C_Currency_ID", DisplayTypeConstants.TableDir));
			rm.addColumn(new RColumn(ctx, "AmtSourceDr", DisplayTypeConstants.Amount));
			rm.addColumn(new RColumn(ctx, "AmtSourceCr", DisplayTypeConstants.Amount));
			rm.addColumn(new RColumn(ctx, "Rate", DisplayTypeConstants.Amount,
				"CASE WHEN (AmtSourceDr + AmtSourceCr) = 0 THEN 0"
				+ " ELSE (AmtAcctDr + AmtAcctCr) / (AmtSourceDr + AmtSourceCr) END"));
		}
		//	Remaining Keys
		for (int i = max; i < keys.size(); i++)
		{
			String column = keys.get(i);
			if (column != null && column.startsWith("Date"))
				rm.addColumn(new RColumn(ctx, column, DisplayTypeConstants.Date));
			else if (column.startsWith("UserElement"))
			{
				if (column.indexOf("1") != -1)
					rm.addColumn(new RColumn(ctx, column, DisplayTypeConstants.TableDir, null, 0, m_ref1));
				else
					rm.addColumn(new RColumn(ctx, column, DisplayTypeConstants.TableDir, null, 0, m_ref2));
			}
			else if (column != null && column.toUpperCase().endsWith("_ID"))
				rm.addColumn(new RColumn(ctx, column, DisplayTypeConstants.TableDir));
		}
		//	Info
		if (!keys.contains("DateAcct"))
			rm.addColumn(new RColumn(ctx, "DateAcct", DisplayTypeConstants.Date));
		if (!keys.contains("C_Period_ID"))
			rm.addColumn(new RColumn(ctx, "C_Period_ID", DisplayTypeConstants.TableDir));
		if (displayQty)
		{
			rm.addColumn(new RColumn(ctx, "C_UOM_ID", DisplayTypeConstants.TableDir));
			rm.addColumn(new RColumn(ctx, "Qty", DisplayTypeConstants.Quantity));
		}
		if (displayDocumentInfo)
		{
			rm.addColumn(new RColumn(ctx, "AD_Table_ID", DisplayTypeConstants.TableDir));
			rm.addColumn(new RColumn(ctx, "Record_ID", DisplayTypeConstants.ID));
			rm.addColumn(new RColumn(ctx, "Description", DisplayTypeConstants.String));
		}
		//if (PostingType == null || PostingType.length() == 0)
			rm.addColumn(new RColumn(ctx, "PostingType", DisplayTypeConstants.List, 
					X_Ref__Posting_Type.AD_Reference_ID));
		return rm;
	}   //  createRModel

	/**
	 *  Create the key columns in sequence
	 *  @return List of Key Columns
	 */
	private ArrayList<String> createKeyColumns()
	{
		ArrayList<String> columns = new ArrayList<String>();
		m_leadingColumns = 0;
		//  Sorting Fields
		for( int i = 0; i < AcctViewerData.s_numSortByFields; ++i )
		{
			if( !columns.contains( sortBy[i] ) )
				columns.add( sortBy[i] );
		}

		//  Add Account Segments
		MAcctSchemaElement[] elements = ASchema.getAcctSchemaElements();
		for (MAcctSchemaElement ase : elements) {
			if (m_leadingColumns == 0 && columns.contains("AD_Org_ID") && columns.contains("Account_ID"))
				m_leadingColumns = columns.size();
			String columnName = ase.getColumnName();
			if (columnName.startsWith("UserElement"))
			{
				if (columnName.indexOf("1") != -1)
					m_ref1 = ase.getDisplayColumnName();
				else
					m_ref2 = ase.getDisplayColumnName();
			}
			if (!columns.contains(columnName))
				columns.add(columnName);
		}
		if (m_leadingColumns == 0 && columns.contains("AD_Org_ID") && columns.contains("Account_ID"))
			m_leadingColumns = columns.size();
		return columns;
	}   //  createKeyColumns

	
}   //  AcctViewerData
