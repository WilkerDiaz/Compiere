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
package org.compiere.model;

import java.beans.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.event.*;

import org.compiere.controller.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Tab Model.
 *  - a combination of AD_Tab (the display attributes) and AD_Table information.
 *  <p>
 *  The Tab owns also it's Table model
 *  and listens to data changes to update the Field values.
 *
 *  <p>
 *  The Tab maintains the bound property: CurrentRow
 *
 *  <pre>
 *  Event Hierarchies:
 *      - dataChanged (from MTable)
 *          - setCurrentRow
 *              - Update all Field Values
 *
 *      - setValue
 *          - Update Field Value
 *          - Callout
 *  </pre>
 *  @author 	Jorg Janke
 *  @version 	$Id: GridTab.java,v 1.10 2006/10/02 05:18:39 jjanke Exp $
 */
public class GridTab implements DataStatusListener, Evaluatee, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *	Create Tab (Model) from Value Object.
	 *  <p>
	 *  MTab provides a property listener for changed rows and a
	 *  DataStatusListener for communicating changes of the underlying data
	 *  @param vo Value Object
	 *  @param onlyCurrentDays only current days
	 *  @param w Window
	 */
	public GridTab(GridTabVO vo, int onlyCurrentDays, GridWindow w)
	{
		m_vo = vo;
		m_window = w;
		m_vo.onlyCurrentDays = onlyCurrentDays;
		//  Create MTable
		m_mTable = new GridTable (m_vo.ctx, m_vo.AD_Table_ID, m_vo.TableName, m_vo.WindowNo, m_vo.TabNo, true);
		m_mTable.setReadOnly(m_vo.IsReadOnly || m_vo.IsView);
		m_mTable.setDeleteable(m_vo.IsDeleteable);
		initTab(false);
	}	//	GridTab

	/** Value Object                    */
	private GridTabVO          	m_vo;

	// The window of this tab
	private GridWindow			m_window;


	/** The Table Model for Query   */
	private GridTable          	m_mTable = null;

	private String 				m_keyColumnName = "";
	private String 				m_linkColumnName = "";
	private String				m_extendedWhere;
	/** Attachments         */
	private HashMap<Integer,Integer>	m_Attachments = null;
	/** Chats				*/
	private HashMap<Integer,Integer>	m_Chats = null;
	/** Locks		        */
	private ArrayList<Integer>	m_Lock = null;

	/** Current Row         */
	private int					m_currentRow = -1;

	/** Property Change     */
	private final PropertyChangeSupport m_propertyChangeSupport = new PropertyChangeSupport(this);
	/** Property Change Type    */
	public static final String  PROPERTY = "CurrentRow";
	/** A list of event listeners for this component.	*/
	protected EventListenerList m_listenerList = new EventListenerList();
	/** Current Data Status Event						*/
	private DataStatusEvent 	m_DataStatusEvent = null;
	/**	Query							*/
	private Query 				m_query = new Query();
	private String 				m_oldQuery = "0=9";
	private boolean 			oldCreated = false;
	private String 				m_linkValue = "999999";

	/** Order By Array if SortNo 1..3   */
	private String[]	    	m_OrderBys = new String[3];
	/** List of Key Parents     */
	private ArrayList<String>	m_parents = new ArrayList<String>(2);

	/** Map of ColumnName of source field (key) and the dependant field (value) */
	private MultiMap<String,GridField>	m_depOnField = new MultiMap<String,GridField>();

	/** Is Tab Included in other Tab  */
	private boolean    			m_included = false;

	/**	Logger			*/
	private static CLogger	log = CLogger.getCLogger(GridTab.class);


	/**************************************************************************
	 *  Tab loader for Tabs > 0
	 */
	class Loader extends Thread
	{
		/**
		 *  Async Loading of Tab > 0
		 */
		@Override
		public void run()
		{
			initTab (true);
		}   //  run
	}   //  Loader

	/**
	 *	Initialize Tab with record from AD_Tab_v
	 *  @param async async
	 *	@return true, if correctly initialized (ignored)
	 */
	protected boolean initTab (boolean async)
	{
		log.fine("#" + m_vo.TabNo + " - Async=" + async + " - Where=" + m_vo.WhereClause);
		m_extendedWhere = m_vo.WhereClause;

		//	Get Field Data
		if (!loadFields())
		{
			return false;
		}

		//  Order By
		m_mTable.setOrderClause(getOrderByClause(m_vo.onlyCurrentDays));

		if (async)
			log.fine("#" + m_vo.TabNo + " - Async=" + async + " - fini");
		return true;
	}	//	initTab

	/**
	 *	Dispose - clean up resources
	 */
	protected void dispose()
	{
		log.fine("#" + m_vo.TabNo);
		m_OrderBys = null;
		//
		m_parents.clear();
		m_parents = null;
		//
		m_mTable.close (true);  //  also disposes Fields
		m_mTable = null;
		//
		m_depOnField.clear();
		m_depOnField = null;
		if (m_Attachments != null)
			m_Attachments.clear();
		m_Attachments = null;
		if (m_Chats != null)
			m_Chats.clear();
		m_Chats = null;
		//
		m_vo.Fields.clear();
		m_vo.Fields = null;
		m_vo = null;
	}	//	dispose


	/**
	 *	Get Field data and add to MTable, if it's required or displayed.
	 *  Reqiored fields are keys, parents, or standard Columns
	 *  @return true if fields loaded
	 */
	private boolean loadFields()
	{
		log.fine("#" + m_vo.TabNo);

		if (m_vo.Fields == null)
			return false;

		//  Add Fields
		for (int f = 0; f < m_vo.Fields.size(); f++)
		{
			GridFieldVO voF = m_vo.Fields.get(f);
			//	Add Fields to Table
			if (voF != null)
			{
				GridField field = new GridField (voF);
				String columnName = field.getColumnName();
				//	Record Info
				if (field.isKey())
					m_keyColumnName = columnName;
				//	Parent Column(s)
				if (field.isParentColumn())
					m_parents.add(columnName);
				//	Order By
				int sortNo = field.getSortNo();
				if (sortNo == 0)
					;
				else if (Math.abs(sortNo) == 1)
				{
					m_OrderBys[0] = columnName;
					if (sortNo < 0)
						m_OrderBys[0] += " DESC";
				}
				else if (Math.abs(sortNo) == 2)
				{
					m_OrderBys[1] = columnName;
					if (sortNo < 0)
						m_OrderBys[1] += " DESC";
				}
				else if (Math.abs(sortNo) == 3)
				{
					m_OrderBys[2] = columnName;
					if (sortNo < 0)
						m_OrderBys[2] += " DESC";
				}
				//  Add field
				m_mTable.addField(field);

				//  List of ColumnNames, this field is dependent on
				ArrayList<String> list = field.getDependentOn();
				for (int i = 0; i < list.size(); i++)
					m_depOnField.put(list.get(i), field);   //  ColumnName, Field
				//  Add fields all fields are dependent on
				if (columnName.equals("IsActive")
						|| columnName.equals("Processed")
						|| columnName.equals("Processing"))
					m_depOnField.put(columnName, null);
			}
		}   //  for all fields

		//  Add Standard Fields
		if (!m_vo.IsView && m_mTable.getField("Created") == null)
		{
			GridField created = new GridField (GridFieldVO.createStdField(m_vo.ctx,
					m_vo.WindowNo, m_vo.TabNo,
					m_vo.AD_Window_ID, m_vo.AD_Tab_ID, false, true, true));
			m_mTable.addField(created);
		}
		if (!m_vo.IsView && m_mTable.getField("CreatedBy") == null)
		{
			GridField createdBy = new GridField (GridFieldVO.createStdField(m_vo.ctx,
					m_vo.WindowNo, m_vo.TabNo,
					m_vo.AD_Window_ID, m_vo.AD_Tab_ID, false, true, false));
			m_mTable.addField(createdBy);
		}
		if (!m_vo.IsView && m_mTable.getField("Updated") == null)
		{
			GridField updated = new GridField (GridFieldVO.createStdField(m_vo.ctx,
					m_vo.WindowNo, m_vo.TabNo,
					m_vo.AD_Window_ID, m_vo.AD_Tab_ID, false, false, true));
			m_mTable.addField(updated);
		}
		if (!m_vo.IsView && m_mTable.getField("UpdatedBy") == null)
		{
			GridField updatedBy = new GridField (GridFieldVO.createStdField(m_vo.ctx,
					m_vo.WindowNo, m_vo.TabNo,
					m_vo.AD_Window_ID, m_vo.AD_Tab_ID, false, false, false));
			m_mTable.addField(updatedBy);
		}
		return true;
	}	//	loadFields

	/**
	 *  Get a list of variables, this tab is dependent on.
	 *  - for display purposes
	 *  @return ArrayList
	 */
	public ArrayList<String> getDependentOn()
	{
		ArrayList<String> list = new ArrayList<String>();
		//  Display
		Evaluator.parseDepends(list, m_vo.DisplayLogic);
		//
		if (list.size() > 0 && CLogMgt.isLevelFiner())
		{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++)
				sb.append(list.get(i)).append(" ");
			log.finer("(" + m_vo.Name + ") " + sb.toString());
		}
		return list;
	}   //  getDependentOn

	/**
	 * 	Get Display Logic
	 *	@return display logic
	 */
	public String getDisplayLogic()
	{
		return m_vo.DisplayLogic;
	}	//	getDisplayLogic

	/**
	 *  Get TableModel.
	 *  <B>Do not directly communicate with the table model,
	 *  but through the methods of this class</B>
	 *  @return Table Model
	 */
	public GridTable getTableModel()
	{
		return m_mTable;
	}   //  getTableModel

	/**
	 *  Get Tab Icon
	 *  @return Icon
	 */
	public javax.swing.Icon getIcon()
	{
		if (m_vo.AD_Image_ID == 0)
			return null;
		//
		/** @todo Load Image */
		return null;
	}   //  getIcon


	/**************************************************************************
	 *  Has this field dependents ?
	 *  @param columnName column name
	 *  @return true if column has dependent
	 */
	public boolean hasDependants (String columnName)
	{
		//	m_depOnField.printToLog();
		return m_depOnField.containsKey(columnName);
	}   //  isDependentOn

	/**
	 *  Get dependents fields of columnName
	 *  @param columnName column name
	 *  @return ArrayList with GridFields dependent on columnName
	 */
	public ArrayList<GridField> getDependantFields (String columnName)
	{
		return m_depOnField.getValues(columnName);
	}   //  getDependentFields


	/**************************************************************************
	 *	Set Query
	 *  @param query query
	 */
	public void setQuery(Query query)
	{
		if (query == null)
			m_query = new Query();
		else
		{
			m_query = query;
			m_vo.onlyCurrentDays = 0;
		}
	}	//	setQuery

	/**
	 *	Get Query
	 *  @return query
	 */
	public Query getQuery()
	{
		return m_query;
	}	//	getQuery

	/**
	 *	Is Query Active
	 *  @return true if query active
	 */
	public boolean isQueryActive()
	{
		if (m_query != null)
			return m_query.isActive();
		return false;
	}	//	isQueryActive

	/**
	 *	Is Query New Record
	 *  @return true if query active
	 */
	public boolean isQueryNewRecord()
	{
		if (m_query != null)
			return m_query.isNewRecordQuery();
		return false;
	}	//	isQueryNewRecord

	/**
	 *  Enable Events - enable data events of tabs (add listeners)
	 */
	public void enableEvents()
	{
		//  Setup Events
		m_mTable.addDataStatusListener(this);
		//	m_mTable.addTableModelListener(this);
	}   //  enableEvents

	/**
	 *	Assemble whereClause and query MTable and position to row 0.
	 *  <pre>
	 *		Scenarios:
	 *		- Never opened 					(full query)
	 *		- query changed 				(full query)
	 *		- Detail link value changed		(full query)
	 *		- otherwise 					(refreshAll)
	 *  </pre>
	 *  @param onlyCurrentDays only current rows
	 */
	public void query (int onlyCurrentDays)
	{
		query (onlyCurrentDays, 0, false);	//	updated
	}	//	query

	/**
	 *	Assemble whereClause and query MTable and position to row 0.
	 *  <pre>
	 *		Scenarios:
	 *		- Never opened 					(full query)
	 *		- query changed 				(full query)
	 *		- Detail link value changed		(full query)
	 *		- otherwise 					(refreshAll)
	 *  </pre>
	 *  @param onlyCurrentDays if only current row, how many days back
	 *  @param maxRows maximum rows or 0 for all
	 *  @param created query based on created if true otherwise updated
	 *  @return true if queried successfully
	 */
	public boolean query (int onlyCurrentDays, int maxRows, boolean created)
	{
		log.fine("#" + m_vo.TabNo
				+ " - OnlyCurrentDays=" + onlyCurrentDays + ", Detail=" + isDetail());
		boolean success = true;
		//	is it same query?
		boolean refresh = m_oldQuery.equals(m_query.getWhereClause())
		&& m_vo.onlyCurrentDays == onlyCurrentDays
		&& oldCreated == created;
		m_oldQuery = m_query.getWhereClause();
		m_vo.onlyCurrentDays = onlyCurrentDays;
		oldCreated = created;

		/**
		 *	Set Where Clause
		 */
		//	Tab Where Clause
		StringBuffer where = new StringBuffer(m_vo.WhereClause);
		if (m_vo.onlyCurrentDays > 0)
		{
			if (where.length() > 0)
				where.append(" AND ");

			boolean showNotProcessed = findColumn ("Processed") != -1;
			//	Show only unprocessed or the one updated within x days
			if (showNotProcessed)
				where.append("(Processed='N' OR ");
			if (created)
				where.append("Created>=");
			else
				where.append("Updated>=");
			//	where.append("addDays(current_timestamp, -");
			where.append("addDays(SysDate, -")
			.append(m_vo.onlyCurrentDays).append(")");
			if (showNotProcessed)
				where.append(")");
		}
		//	Detail Query
		if (isDetail() && getTabNo() != 0)	//	first tab
		{
			String lc = getLinkColumnName();
			if (lc.equals(""))
			{
				log.warning ("No link column");
				if (where.length() > 0)
					where.append(" AND ");
				where.append (" 2=3");
				success = false;
			}
			else
			{
				String value = m_vo.ctx.getContext( m_vo.WindowNo, lc);
				//	Same link value?
				if (refresh)
					refresh = m_linkValue.equals(value);
				m_linkValue = value;
				//	Check validity
				if (value.length() == 0)
				{
					log.warning ("No value for link column " + lc);
					if (where.length() > 0)
						where.append(" AND ");
					where.append (" 2=4");
					success = false;
				}
				else
				{
					//	we have column and value
					if (where.length() > 0)
						where.append(" AND ");
					if ("NULL".equals(value.toUpperCase()))
					{
						where.append(lc).append(" IS NULL ");
						log.severe("Null Value of link column " + lc);
					}
					else
					{
						where.append(lc).append("=");
						if (lc.endsWith("_ID"))
							where.append(value);
						else
							where.append("'").append(value).append("'");
					}
				}
			}
		}	//	isDetail

		m_extendedWhere = where.toString();

		//	Final Query
		if (m_query.isActive())
		{
			String q = validateQuery(m_query);
			if (q != null)
			{
				if (where.length() > 0 )
					where.append(" AND ");
				where.append(q);
			}
		}

		/**
		 *	Query
		 */
		log.fine("#" + m_vo.TabNo + " - " + where);
		if (m_mTable.isOpen())
		{
			if (refresh)
				m_mTable.dataRefreshAll();
			else
				m_mTable.dataRequery(where.toString());
		}
		else
		{
			m_mTable.setSelectWhereClause(where.toString());
			m_mTable.open(maxRows);
		}
		//  Go to Record 0
		setCurrentRow(0, true);
		return success;
	}	//	query

	/**
	 * 	Validate Query.
	 *  If query column is not a tab column create EXISTS query
	 * 	@param query query
	 * 	@return where clause
	 */
	private String validateQuery (Query query)
	{
		if (query == null || query.getRestrictionCount() == 0)
			return null;

		//	Check: only one restriction
		if (query.getRestrictionCount() != 1)
		{
			log.fine("Ignored(More than 1 Restriction): " + query);
			return query.getWhereClause();
		}

		String colName = query.getColumnName(0);
		if (colName == null)
		{
			log.fine("Ignored(No Column): " + query);
			return query.getWhereClause();
		}
		//	a '(' in the name = function - don't try to resolve
		if (colName.indexOf('(') != -1)
		{
			log.fine("Ignored(Function): " + colName);
			return query.getWhereClause();
		}
		//	OK - Query is valid

		//	Zooms to the same Window (Parents, ..)
		String refColName = null;
		if (colName.equals("R_RequestRelated_ID"))
			refColName = "R_Request_ID";
		else if (colName.startsWith("C_DocType"))
			refColName = "C_DocType_ID";
		/*else if (colName.equals("CreatedBy") || colName.equals("UpdatedBy"))
			refColName = "CreatedBy";*/
		else if (colName.equals("Orig_Order_ID"))
			refColName = "C_Order_ID";
		else if (colName.equals("Orig_InOut_ID"))
			refColName = "M_InOut_ID";
		if (refColName != null)
		{
			query.setColumnName(0, refColName);
			if (getField(refColName) != null)
			{
				log.fine("Column " + colName + " replaced with synonym " + refColName);
				return query.getWhereClause();
			}
			refColName = null;
		}

		//	Simple Query.
		if (getField(colName) != null)
		{
			log.fine("Field Found: " + colName);
			return query.getWhereClause();
		}

		//	Find Refernce Column e.g. BillTo_ID -> C_BPartner_Location_ID
		String sql = "SELECT cc.ColumnName "
			+ "FROM AD_Column c"
			+ " INNER JOIN AD_Ref_Table r ON (c.AD_Reference_Value_ID=r.AD_Reference_ID)"
			+ " INNER JOIN AD_Column cc ON (r.Column_Key_ID=cc.AD_Column_ID) "
			+ "WHERE c.AD_Reference_ID IN (18,30)" 	//	Table/Search
			+ " AND c.ColumnName=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, colName);
			rs = pstmt.executeQuery();
			if (rs.next())
				refColName = rs.getString(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "(ref) - Column=" + colName, e);
			return query.getWhereClause();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	Reference Column found
		if (refColName != null)
		{
			query.setColumnName(0, refColName);
			if (getField(refColName) != null)
			{
				log.fine("Column " + colName + " replaced with " + refColName);
				return query.getWhereClause();
			}
			colName = refColName;
		}

		//	Column NOT in Tab - create EXISTS subquery
		String tableName = null;
		String tabKeyColumn = getKeyColumnName();
		//	Column=SalesRep_ID, Key=AD_User_ID, Query=SalesRep_ID=101

		sql = "SELECT t.TableName "
			+ "FROM AD_Column c"
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
			+ "WHERE c.ColumnName=? AND IsKey='Y'"		//	#1 Link Column
			+ " AND EXISTS (SELECT * FROM AD_Column cc"
			+ " WHERE cc.AD_Table_ID=t.AD_Table_ID AND cc.ColumnName=?)";	//	#2 Tab Key Column
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, colName);
			pstmt.setString(2, tabKeyColumn);
			rs = pstmt.executeQuery();
			if (rs.next())
				tableName = rs.getString(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "Column=" + colName + ", Key=" + tabKeyColumn, e);
			return null;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Special Reference Handling
		if (tabKeyColumn.equals("AD_Reference_ID"))
		{
			//	Column=AccessLevel, Key=AD_Reference_ID, Query=AccessLevel='6'
			sql = "SELECT AD_Reference_ID FROM AD_Column WHERE ColumnName=?";
			int AD_Reference_ID = QueryUtil.getSQLValue(null, sql, colName);
			return "AD_Reference_ID=" + AD_Reference_ID;
		}

		//	Causes could be functions in query
		//	e.g. Column=UPPER(Name), Key=AD_Element_ID, Query=UPPER(AD_Element.Name) LIKE '%CUSTOMER%'
		if (tableName == null)
		{
			log.info ("Not successfull - Column="
					+ colName + ", Key=" + tabKeyColumn
					+ ", Query=" + query);
			return query.getWhereClause();
		}
		else if(tableName.equals("M_WorkOrderComponent") && tabKeyColumn.equals("M_WorkOrder_ID")){
			Object[][] results = QueryUtil.executeQuery((Trx)null, "SELECT M_WorkOrderOperation_ID FROM M_WorkOrderComponent WHERE "+query.getWhereClause(),new Object[]{});
			int M_WorkOrderOperation_ID = 0;
			if(results != null && results.length>0 && results[0].length>0)
				if(results[0][0] instanceof BigDecimal)
					M_WorkOrderOperation_ID = ((BigDecimal)results[0][0]).intValue();
				else if(results[0][0] instanceof Integer)
					M_WorkOrderOperation_ID = (Integer)results[0][0];
			query.setTableName("xx");
			String result = "EXISTS (SELECT * FROM M_WorkOrderOperation xx WHERE xx.M_WorkOrderOperation_ID="+M_WorkOrderOperation_ID+" AND xx.M_WorkOrder_ID=M_WorkOrder.M_WorkOrder_ID)";
			return result;
		}

		query.setTableName("xx");
		StringBuffer result = new StringBuffer ("EXISTS (SELECT * FROM ")
		.append(tableName).append(" xx WHERE ")
		.append(query.getWhereClause(true))
		.append(" AND xx.").append(tabKeyColumn).append("=")
		.append(getTableName()).append(".").append(tabKeyColumn).append(")");
		log.fine(result.toString());
		return result.toString();
	}	//	validateQuery


	/**************************************************************************
	 *  Refresh all data
	 */
	public void dataRefreshAll ()
	{
		log.fine("#" + m_vo.TabNo);
		/** @todo does not work with alpha key */
		int keyNo = m_mTable.getKeyID(m_currentRow);
		m_mTable.dataRefreshAll();
		//  Should use RowID - not working for tables with multiple keys
		if (keyNo != -1)
		{
			if (keyNo != m_mTable.getKeyID(m_currentRow))   //  something changed
			{
				int size = getRowCount();
				for (int i = 0; i < size; i++)
				{
					if (keyNo == m_mTable.getKeyID(i))
					{
						m_currentRow = i;
						break;
					}
				}
			}
		}
		setCurrentRow(m_currentRow, true);
	}   //  dataRefreshAll

	/**
	 *  Refresh current row data
	 */
	public void dataRefresh ()
	{
		dataRefresh (m_currentRow);
	}   //  dataRefresh

	/**
	 *  Refresh row data
	 *  @param row index
	 */
	public void dataRefresh (int row)
	{
		log.fine("#" + m_vo.TabNo + " - row=" + row);
		m_mTable.dataRefresh(row);
		setCurrentRow(row, true);
	}   //  dataRefresh


	/**************************************************************************
	 *  Uncoditionally Save data
	 *  @param manualCmd if true, no vetoable PropertyChange will be fired for save confirmation from MTable
	 *  @return true if save complete (or nor required)
	 */
	public boolean dataSave(boolean manualCmd)
	{
		log.fine("#" + m_vo.TabNo + " - row=" + m_currentRow);
		try
		{
			//check if current tab and its parents have changed.
			if (hasChangedCurrentTabAndParents())
				return false;

			boolean retValue = (m_mTable.dataSave(manualCmd) == GridTable.SAVE_OK);
			if (manualCmd)
				setCurrentRow(m_currentRow, false);
			if (retValue) 
			{

				// If the parent of the tab is also based on the same table, then we
				// need to refresh the parent tab. 
				// an example is BPartner->Vendor->Location tab. 
				// When Vendor is changed, the BPartner needs to be refreshed,
				// otherwise, the resultSet of BPartner and its DB will be out of Sync 
				// when Location is being updated.
				refreshParentsSameTable();
			}
			return retValue;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "#" + m_vo.TabNo + " - row=" + m_currentRow, e);
		}
		return false;
	}   //  dataSave

	/**
	 * Refresh the Parent if it is on the same table.
	 */
	private void refreshParentsSameTable() 
	{
		if (isDetail()) 
		{
			// get parent tab
			// the parent tab is the first tab above with level = this_tab_level-1
			int level = m_vo.TabLevel;
			for (int i = m_window.getTabIndex(this) - 1; i >= 0; i--) 
			{
				GridTab parentTab = m_window.getTab(i);
				if (parentTab.m_vo.TabLevel == level-1) 
				{
					if (parentTab.getAD_Table_ID() == getAD_Table_ID()) 
					{
						parentTab.dataRefresh();
					}
					// search for the next parent
					if (parentTab.isDetail()) 
					{
						level = parentTab.m_vo.TabLevel;
					}
					else 
					{
						break;
					}
				}
			}
		}
	}



	/**
	 * Validate if the current tab record or any parent record has changed in database 
	 * @return true if there are changes
	 */
	public boolean hasChangedCurrentTabAndParents() 
	{
		String msg = null;		
		// Validate that current record has not changed and validate that every parent above has not changed
		if (m_mTable.hasChanged(m_currentRow)) 
		{
			// return error stating that current record has changed and it cannot be saved
			msg = Msg.getMsg(Env.getCtx(), "CurrentRecordModified");
			log.saveError("CurrentRecordModified", msg, false);
			return true;
		}
		if (isDetail()) 
		{
			// get parent tab
			// the parent tab is the first tab above with level = this_tab_level-1
			int level = m_vo.TabLevel;
			for (int i = m_window.getTabIndex(this) - 1; i >= 0; i--) 
			{
				GridTab parentTab = m_window.getTab(i);
				if (parentTab.m_vo.TabLevel == level-1) 
				{
					// hack for SR 10021921 to work around fix for SR 10020916
					if(getTableName().equals("C_InvoicePaySchedule")){
						return false;
					}

					// this is parent tab					
					if (parentTab.m_mTable.hasChanged(parentTab.m_currentRow)) 
					{
						// return error stating that current record has changed and it cannot be saved
						msg = Msg.getMsg(Env.getCtx(), "ParentRecordModified") + ": " + parentTab.getName();
						log.saveError("ParentRecordModified", msg, false);
						return true;
					} 
					else 
					{
						// search for the next parent
						if (parentTab.isDetail()) 
						{
							level = parentTab.m_vo.TabLevel;
						} 
						else 
						{
							break;
						}
					}
				}
			}
		}
		return false;
	}



	/**
	 *  Do we need to Save?
	 *  @param rowChange row change
	 *  @param  onlyRealChange if true the value of a field was actually changed
	 *  (e.g. for new records, which have not been changed) - default false
	 *	@return true it needs to be saved
	 */
	public boolean needSave (boolean rowChange, boolean onlyRealChange)
	{
		if (rowChange)
		{
			return m_mTable.needSave(-2, onlyRealChange);
		}
		else
		{
			if (onlyRealChange)
				return m_mTable.needSave();
			else
				return m_mTable.needSave(onlyRealChange);
		}
	}   //  isDataChanged

	/**
	 *  Ignore data changes
	 */
	public void dataIgnore()
	{
		log.fine("#" + m_vo.TabNo);
		m_mTable.dataIgnore();
		setCurrentRow(m_currentRow, false);    //  re-load data
		log.fine("#" + m_vo.TabNo + "- fini");
	}   //  dataIgnore


	/**
	 *  Create (copy) new Row
	 *  and process Callouts
	 *  @param copy copy
	 *  @return true if copied/new
	 */
	public boolean dataNew (boolean copy)
	{
		log.fine("#" + m_vo.TabNo);
		if (!isInsertRecord())
		{
			log.warning ("Insert Not allowed in TabNo=" + m_vo.TabNo);
			return false;
		}
		//	Prevent New Where Main Record is processed
		if (m_vo.TabNo > 0)
		{

			boolean processed = "Y".equals(m_vo.ctx.getContext( m_vo.WindowNo, "Processed"));
			//	boolean active = "Y".equals(m_vo.ctx.getContext( m_vo.WindowNo, "IsActive"));
			if (processed)
			{
				log.warning ("Not allowed in TabNo=" + m_vo.TabNo + " -> Processed=" + processed);
				return false;
			}
			log.finest("Processed=" + processed);
		}
		boolean retValue = m_mTable.dataNew (m_currentRow, copy);
		if (!retValue)
			return retValue;
		setCurrentRow(m_currentRow + 1, true);
		//  process all Callouts (no dependency check - assumed that settings are valid)
		for (int i = 0; i < getFieldCount(); i++)
			processCallout(getField(i));
		//  check validity of defaults
		for (int i = 0; i < getFieldCount(); i++)
		{
			getField(i).refreshLookup();
			getField(i).validateValue();
			getField(i).setError(false);
		}
//		m_mTable.setChanged(false);
		return retValue;
	}   //  dataNew

	/**
	 *  Delete current Row
	 *  @return true if deleted
	 */
	public boolean dataDelete()
	{
		log.fine("#" + m_vo.TabNo + " - row=" + m_currentRow);
		boolean retValue = m_mTable.dataDelete(m_currentRow);
		setCurrentRow(m_currentRow, true);
		return retValue;
	}   //  dataDelete


	/**
	 *	Get Name of Tab
	 *  @return name
	 */
	public String getName()
	{
		return m_vo.Name;
	}	//	getName

	/**
	 *	Get Description of Tab
	 *  @return description
	 */
	public String getDescription()
	{
		return m_vo.Description;
	}	//	getDescription

	/**
	 *	Get Help of Tab
	 *  @return help
	 */
	public String getHelp()
	{
		return m_vo.Help;
	}	//	getHelp

	/**
	 *  Get Tab Level
	 *  @return tab level
	 */
	public int getTabLevel()
	{
		return m_vo.TabLevel;
	}   //  getTabLevel

	/**
	 *  Get Commit Warning
	 *  @return commit warning
	 */
	public String getCommitWarning()
	{
		return m_vo.CommitWarning;
	}   //  getCommitWarning

	/**
	 *	Return Table Model
	 *  @return MTable
	 */
	protected GridTable getMTable()
	{
		return m_mTable;
	}	//	getMTable

	/**
	 *	Return the name of the key column - may be ""
	 *  @return key column name
	 */
	public String getKeyColumnName()
	{
		return m_keyColumnName;
	}	//	getKeyColumnName

	/**
	 *	Return Name of link column
	 *  @return link column name
	 */
	public String getLinkColumnName()
	{
		return m_linkColumnName;
	}	//	getLinkColumnName

	/**
	 *	Set Name of link column.
	 * 	Set from MWindow.loadTabData
	 *  Used in MTab.isCurreny, (.setCurrentRow) .query - APanel.cmd_report
	 *    and MField.isEditable and .isDefault via context
	 *	@param linkColumnName	name of column - or sets name to AD_Column_ID, if exists
	 */
	public void setLinkColumnName (String linkColumnName)
	{
		if (linkColumnName != null)
			m_linkColumnName = linkColumnName;
		else
		{
			if (m_vo.AD_Column_ID == 0)
				return;
			//	we have a link column identified (primary parent column)
			else
			{
				String SQL = "SELECT ColumnName FROM AD_Column WHERE AD_Column_ID=?";

				PreparedStatement pstmt = null;
				ResultSet rs = null;

				try
				{
					pstmt = DB.prepareStatement(SQL, (Trx) null);
					pstmt.setInt(1, m_vo.AD_Column_ID);		//	Parent Link Column
					rs = pstmt.executeQuery();
					if (rs.next())
						m_linkColumnName = rs.getString(1);
				}
				catch (SQLException e)
				{
					log.log(Level.SEVERE, "", e);
				}
				finally
				{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
				log.fine("AD_Column_ID=" + m_vo.AD_Column_ID + " - " + m_linkColumnName);
			}
		}
		m_vo.ctx.setContext(m_vo.WindowNo, m_vo.TabNo, "LinkColumnName", m_linkColumnName);
	}	//	setLinkColumnName

	/**
	 *	Is the tab current?.
	 *  <pre>
	 *	Yes 	- Table must be open
	 *			- Query String is the same
	 *			- Not Detail
	 *			- Old link column value is same as current one
	 *  </pre>
	 *  @return true if current
	 */
	public boolean isCurrent()
	{
		//	Open?
		if (!m_mTable.isOpen())
			return false;
		//	Same Query
		if (!m_oldQuery.equals(m_query.getWhereClause()))
			return false;
		//	Detail?
		if (!isDetail())
			return true;
		//	Same link column value
		String value = m_vo.ctx.getContext( m_vo.WindowNo, getLinkColumnName());
		return m_linkValue.equals(value);
	}	//	isCurrent

	/**
	 *	Is the tab/table currently open
	 *  @return true if open
	 */
	public boolean isOpen()
	{
		//	Open?
		if (m_mTable != null)
			return m_mTable.isOpen();
		return false;
	}	//	isCurrent


	/**
	 *  Is Tab Incluced in other Tab
	 *  @return true if included
	 */
	public boolean isIncluded()
	{
		return m_included;
	}   //  isIncluded

	/**
	 *  Is Tab Incluced in other Tab
	 *  @param isIncluded true if included
	 */
	public void setIncluded(boolean isIncluded)
	{
		m_included = isIncluded;
	}   //  setIncluded

	/**
	 * 	Get Only Current Days
	 *	@return history days
	 */
	public int getOnlyCurrentDays()
	{
		return m_vo.onlyCurrentDays;
	}	//	getOnlyCurrentDays

	/**
	 *	Return Parent ArrayList
	 *  @return parent column names
	 */
	public ArrayList<String> getParentColumnNames()
	{
		return m_parents;
	}	//	getParentColumnNames

	/**
	 *	Returns true if this is a detail record
	 *  @return true if not parent tab
	 */
	public boolean isDetail()
	{
		//	We have IsParent columns and/or a link column
		if (m_parents.size() > 0 || m_vo.AD_Column_ID != 0)
			return true;
		return false;
	}	//	isDetail

	/**
	 *	Is Printed (Document can be printed)
	 *  @return true if printing
	 */
	public boolean isPrinted()
	{
		return m_vo.AD_Process_ID != 0;
	}	//	isPrinted

	/**
	 *	Get WindowNo
	 *  @return window no
	 */
	public int getWindowNo()
	{
		return m_vo.WindowNo;
	}	//	getWindowNo

	/**
	 *	Get TabNo
	 *  @return tab no
	 */
	public int getTabNo()
	{
		return m_vo.TabNo;
	}	//	getTabNo

	/**
	 *	Get Process ID
	 *  @return Process ID
	 */
	public int getAD_Process_ID()
	{
		return m_vo.AD_Process_ID;
	}	//	getAD_Process_ID

	/**
	 *	Is High Volume?
	 *  @return true if high volume table
	 */
	public boolean isHighVolume()
	{
		return m_vo.IsHighVolume;
	}	//	isHighVolume

	/**
	 *	Is Read Only?
	 *  @return true if read only
	 */
	public boolean isReadOnly()
	{
		if (m_vo.IsReadOnly)
			return true;

		//  no restrictions
		if (m_vo.ReadOnlyLogic == null || m_vo.ReadOnlyLogic.equals(""))
			return m_vo.IsReadOnly;

		//  ** dynamic content **  uses get_ValueAsString
		boolean retValue = Evaluator.evaluateLogic(this, m_vo.ReadOnlyLogic);
		log.finest(m_vo.Name
				+ " (" + m_vo.ReadOnlyLogic + ") => " + retValue);
		return retValue;
	}	//	isReadOnly

	/**
	 * 	Tab contains Always Update Field
	 *	@return true if field with always updateable
	 */
	public boolean isAlwaysUpdateField()
	{
		for (int i = 0; i < m_mTable.getColumnCount(); i++)
		{
			GridField field = m_mTable.getField(i);
			if (field.isAlwaysUpdateable())
				return true;
		}
		return false;
	}	//	isAlwaysUpdateField

	/**
	 *	Can we Insert Records?
	 *  @return true not read only and allowed
	 */
	public boolean isInsertRecord()
	{
		if (isReadOnly())
			return false;
		return m_vo.IsInsertRecord;
	}	//	isInsertRecord

	/**
	 *	Is the Tab Visible.
	 *	Called when constructing the window.
	 *	@param initialSetup return false only if not to be displayed
	 *  @return true, if displayed
	 */
	public boolean isDisplayed (boolean initialSetup)
	{
		//  no restrictions
		String dl = m_vo.DisplayLogic;
		if (dl == null || dl.equals(""))
			return true;

		if (initialSetup)
		{
			if (dl.indexOf("@#") != -1)		//	global variable
			{
				String parsed = Env.parseContext (m_vo.ctx, 0, dl, false, false).trim();
				if (parsed.length() != 0)	//	variable defined
					return Evaluator.evaluateLogic(this, dl);
			}
			return true;
		}
		//
		boolean retValue = Evaluator.evaluateLogic(this, dl);
		log.config(m_vo.Name + " (" + dl + ") => " + retValue);
		return retValue;
	}	//	isDisplayed

	/**
	 * 	Get Variable Value (Evaluatee)
	 *	@param variableName name
	 *	@return value
	 */
	public String get_ValueAsString (String variableName)
	{
		return m_vo.ctx.getContext(m_vo.WindowNo, variableName, true);
	}	//	get_ValueAsString

	/**
	 *  Is Single Row
	 *  @return true if single row
	 */
	public boolean isSingleRow()
	{
		return m_vo.IsSingleRow;
	}   //  isSingleRow;

	/**
	 *  Set Single Row.
	 *  Temporary store of current value
	 *  @param isSingleRow toggle
	 */
	public void setSingleRow (boolean isSingleRow)
	{
		m_vo.IsSingleRow = isSingleRow;
	}   //  setSingleRow


	/**
	 *  Has Tree
	 *  @return true if tree exists
	 */
	public boolean isTreeTab()
	{
		return m_vo.HasTree;
	}   //  isTreeTab

	/**
	 *	Get Tab ID
	 *  @return Tab ID
	 */
	public int getAD_Tab_ID()
	{
		return m_vo.AD_Tab_ID;
	}	//	getAD_Tab_ID

	/**
	 *	Get Table ID
	 *  @return Table ID
	 */
	public int getAD_Table_ID()
	{
		return m_vo.AD_Table_ID;
	}	//	getAD_Table_ID

	/**
	 *	Get Window ID
	 *  @return Window ID
	 */
	public int getAD_Window_ID()
	{
		return m_vo.AD_Window_ID;
	}	//	getAD_Window_ID

	/**
	 *	Get Included Tab ID
	 *  @return Included_Tab_ID
	 */
	public int getIncluded_Tab_ID()
	{
		return m_vo.Included_Tab_ID;
	}	//	getIncluded_Tab_ID

	/**
	 *	Get TableName
	 *  @return Table Name
	 */
	public String getTableName()
	{
		return m_vo.TableName;
	}	//	getTableName

	/**
	 *	Get Tab Where Clause
	 *  @return where clause
	 */
	public String getWhereClause()
	{
		return m_vo.WhereClause;
	}	//	getWhereClause

	/**
	 * 	Is Sort Tab
	 * 	@return true if sort tab
	 */
	public boolean isSortTab()
	{
		return m_vo.IsSortTab;
	}	//	isSortTab

	/**
	 * 	Get Order column for sort tab
	 * 	@return AD_Column_ID
	 */
	public int getAD_ColumnSortOrder_ID()
	{
		return m_vo.AD_ColumnSortOrder_ID;
	}	//	getAD_ColumnSortOrder_ID

	/**
	 * 	Get Yes/No column for sort tab
	 * 	@return AD_Column_ID
	 */
	public int getAD_ColumnSortYesNo_ID()
	{
		return m_vo.AD_ColumnSortYesNo_ID;
	}	//	getAD_ColumnSortYesNo_ID


	/**************************************************************************
	 *	Get extended Where Clause (parent link)
	 *  @return parent link
	 */
	public String getWhereExtended()
	{
		return m_extendedWhere;
	}	//	getWhereExtended

	/**
	 *	Get Order By Clause
	 *  @param onlyCurrentDays only current rows
	 *  @return Order By Clause
	 */
	private String getOrderByClause(int onlyCurrentDays)
	{
		//	First Prio: Tab Order By
		if (m_vo.OrderByClause.length() > 0)
			return m_vo.OrderByClause;

		//	Second Prio: Fields (save it)
		m_vo.OrderByClause = "";
		for (int i = 0; i < 3; i++)
		{
			String order = m_OrderBys[i];
			if (order != null && order.length() > 0)
			{
				if (m_vo.OrderByClause.length() > 0)
					m_vo.OrderByClause += ",";
				m_vo.OrderByClause += order;
			}
		}
		if (m_vo.OrderByClause.length() > 0)
			return m_vo.OrderByClause;

		//	Third Prio: onlyCurrentRows
		m_vo.OrderByClause = "Created";
		if (onlyCurrentDays > 0)
			m_vo.OrderByClause += " DESC";
		return m_vo.OrderByClause;
	}	//	getOrderByClause


	/**************************************************************************
	 *	Transaction support.
	 *	Depending on Table returns transaction info
	 *  @return info
	 */
	public static String getTrxInfo(String tableName, Ctx ctx, int windowNo, int tabNo)
	{
		//	InvoiceBatch
		if (tableName.startsWith("C_InvoiceBatch"))
		{
			int Record_ID = ctx.getContextAsInt( windowNo, "C_InvoiceBatch_ID");
			log.fine(tableName + " - " + Record_ID);
			MessageFormat mf = null;
			try
			{
				mf = new MessageFormat(Msg.getMsg(Env.getAD_Language(ctx), "InvoiceBatchSummary"));
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "InvoiceBatchSummary=" + Msg.getMsg(Env.getAD_Language(ctx), "InvoiceBatchSummary"), e);
			}
			if (mf == null)
				return " ";
			/**********************************************************************
			 *	** Message: ExpenseSummary **
			 *	{0} Line(s) {1,number,#,##0.00}  - Total: {2,number,#,##0.00}
			 *
			 *	{0} - Number of lines
			 *	{1} - Toral
			 *	{2} - Currency
			 */
			Object[] arguments = new Object[3];
			boolean filled = false;
			//
			String sql = "SELECT COUNT(*), NVL(SUM(LineNetAmt),0), NVL(SUM(LineTotalAmt),0) "
				+ "FROM C_InvoiceBatchLine "
				+ "WHERE C_InvoiceBatch_ID=? AND IsActive='Y'";
			//
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(1, Record_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					//	{0} - Number of lines
					Integer lines = Integer.valueOf(rs.getInt(1));
					arguments[0] = lines;
					//	{1} - Line net
					Double net = new Double(rs.getDouble(2));
					arguments[1] = net;
					//	{2} - Line net
					Double total = new Double(rs.getDouble(3));
					arguments[2] = total;
					filled = true;
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, tableName + "\nSQL=" + sql, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			if (filled)
				return mf.format (arguments);
			return " ";
		}	//	InvoiceBatch

		//	Order || Invoice
		else if (tableName.startsWith("C_Order") || tableName.startsWith("C_Invoice"))
		{
			int Record_ID;
			boolean isOrder = tableName.startsWith("C_Order");
			//
			StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS Lines,c.ISO_Code,o.TotalLines,o.GrandTotal,"
					+ "currencyBase(o.GrandTotal,o.C_Currency_ID,o.DateAcct, o.AD_Client_ID,o.AD_Org_ID) AS ConvAmt ");
			if (isOrder)
			{
				Record_ID = ctx.getContextAsInt( windowNo, "C_Order_ID");
				sql.append("FROM C_Order o"
						+ " INNER JOIN C_Currency c ON (o.C_Currency_ID=c.C_Currency_ID)"
						+ " INNER JOIN C_OrderLine l ON (o.C_Order_ID=l.C_Order_ID) "
						+ "WHERE o.C_Order_ID=? ");
			}
			else
			{
				Record_ID = ctx.getContextAsInt( windowNo, "C_Invoice_ID");
				sql.append("FROM C_Invoice o"
						+ " INNER JOIN C_Currency c ON (o.C_Currency_ID=c.C_Currency_ID)"
						+ " INNER JOIN C_InvoiceLine l ON (o.C_Invoice_ID=l.C_Invoice_ID) "
						+ "WHERE o.C_Invoice_ID=? ");
			}
			sql.append("GROUP BY o.C_Currency_ID, c.ISO_Code, o.TotalLines, o.GrandTotal, o.DateAcct, o.AD_Client_ID, o.AD_Org_ID");

			log.fine(tableName + " - " + Record_ID);
			MessageFormat mf = null;
			MessageFormat mfMC = null;
			try
			{
				mf = new MessageFormat(Msg.getMsg(Env.getAD_Language(ctx), "OrderSummary"));
				mfMC = new MessageFormat(Msg.getMsg(Env.getAD_Language(ctx), "OrderSummaryMC"));
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "OrderSummary/MC", e);
			}
			if (mf == null || mfMC == null)
				return " ";
			/**********************************************************************
			 *	** Message: OrderSummary/MC **
			 *	{0} Line(s) - {1,number,#,##0.00} - Total: {3}{2,number,#,##0.00} = {5}{4,number,#,##0.00}
			 *	{0} Line(s) - {1,number,#,##0.00} - Total: {3}{2,number,#,##0.00}
			 *
			 *	{0} - Number of lines
			 *	{1} - Line toral
			 *	{2} - Grand total (including tax, etc.)
			 *	{3} - Source Currency
			 *	(4) - Grand total converted to local currency
			 *	{5} - Base Currency
			 */
			Object[] arguments = new Object[6];
			boolean filled = false;
			//
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try
			{
				pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
				pstmt.setInt(1, Record_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					//	{0} - Number of lines
					Integer lines = Integer.valueOf(rs.getInt(1));
					arguments[0] = lines;
					//	{1} - Line toral
					Double lineTotal = new Double(rs.getDouble(3));
					arguments[1] = lineTotal;
					//	{2} - Grand total (including tax, etc.)
					Double grandTotal = new Double(rs.getDouble(4));
					arguments[2] = grandTotal;
					//	{3} - Currency
					String currency = rs.getString(2);
					arguments[3] = currency;
					//	(4) - Grand total converted to Base
					Double grandBase = new Double(rs.getDouble(5));
					arguments[4] = grandBase;
					arguments[5] = ctx.getContext("$CurrencyISO");
					filled = true;
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, tableName + "\nSQL=" + sql, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			if (filled)
			{
				if (arguments[2].equals(arguments[4]))
					return mf.format (arguments);
				else
					return mfMC.format (arguments);
			}
			return " ";
		}	//	Order || Invoice

		//	Expense Report
		else if (tableName.startsWith("S_TimeExpense") && tabNo == 0)
		{
			int Record_ID = ctx.getContextAsInt( windowNo, "S_TimeExpense_ID");
			log.fine(tableName + " - " + Record_ID);
			MessageFormat mf = null;
			try
			{
				mf = new MessageFormat(Msg.getMsg(Env.getAD_Language(ctx), "ExpenseSummary"));
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "ExpenseSummary=" + Msg.getMsg(Env.getAD_Language(ctx), "ExpenseSummary"), e);
			}
			if (mf == null)
				return " ";
			/**********************************************************************
			 *	** Message: ExpenseSummary **
			 *	{0} Line(s) - Total: {1,number,#,##0.00} {2}
			 *
			 *	{0} - Number of lines
			 *	{1} - Toral
			 *	{2} - Currency
			 */
			Object[] arguments = new Object[3];
			boolean filled = false;
			//
			String SQL = "SELECT COUNT(*) AS Lines, SUM(ConvertedAmt*Qty) "
				+ "FROM S_TimeExpenseLine "
				+ "WHERE S_TimeExpense_ID=?";

			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try
			{
				pstmt = DB.prepareStatement(SQL, (Trx) null);
				pstmt.setInt(1, Record_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					//	{0} - Number of lines
					Integer lines = Integer.valueOf(rs.getInt(1));
					arguments[0] = lines;
					//	{1} - Line toral
					Double total = new Double(rs.getDouble(2));
					arguments[1] = total;
					//	{3} - Currency
					arguments[2] = " ";
					filled = true;
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, tableName + "\nSQL=" + SQL, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			if (filled)
				return mf.format (arguments);
			return " ";
		}	//	S_TimeExpense


		//	Default - No Trx Info
		return null;
	}	//	getTrxInfo


	/**************************************************************************
	 *	Load Attachments for this table
	 */
	public void loadAttachments()
	{
		log.fine("#" + m_vo.TabNo);
		if (!canHaveAttachment())
			return;

		String SQL = "SELECT AD_Attachment_ID, Record_ID FROM AD_Attachment "
			+ "WHERE AD_Table_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			if (m_Attachments == null)
				m_Attachments = new HashMap<Integer,Integer>();
			else
				m_Attachments.clear();
			pstmt = DB.prepareStatement(SQL, (Trx) null);
			pstmt.setInt(1, m_vo.AD_Table_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Integer key = Integer.valueOf(rs.getInt(2));
				Integer value = Integer.valueOf(rs.getInt(1));
				m_Attachments.put(key, value);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "loadAttachments", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		log.config("#" + m_Attachments.size());
	}	//	loadAttachment

	/**
	 *	Can this tab have Attachments?.
	 *  <p>
	 *  It can have an attachment if it has a key column ending with _ID.
	 *  The key column is empty, if there is no single identifying key.
	 *  @return true if record can have attachment
	 */
	public boolean canHaveAttachment()
	{
		if (getKeyColumnName().toUpperCase().endsWith("_ID"))
			return true;
		return false;
	}   //	canHaveAttachment

	/**
	 *	Returns true, if current row has an Attachment
	 *  @return true if record has attchment
	 */
	public boolean hasAttachment()
	{
		if (m_Attachments == null)
			loadAttachments();
		if (m_Attachments == null || m_Attachments.isEmpty())
			return false;
		//
		Integer key = Integer.valueOf(m_mTable.getKeyID (m_currentRow));
		return m_Attachments.containsKey(key);
	}	//	hasAttachment

	/**
	 *	Get Attachment_ID for current record.
	 *	@return ID or 0, if not found
	 */
	public int getAD_AttachmentID()
	{
		if (m_Attachments == null)
			loadAttachments();
		if (m_Attachments.isEmpty())
			return 0;
		//
		Integer key = Integer.valueOf(m_mTable.getKeyID (m_currentRow));
		Integer value = m_Attachments.get(key);
		if (value == null)
			return 0;
		else
			return value.intValue();
	}	//	getAttachmentID

	/**************************************************************************
	 *	Load Chats for this table
	 */
	public void loadChats()
	{
		log.fine("#" + m_vo.TabNo);
		if (!canHaveAttachment())
			return;

		String sql = "SELECT CM_Chat_ID, Record_ID FROM CM_Chat "
			+ "WHERE AD_Table_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			if (m_Chats == null)
				m_Chats = new HashMap<Integer,Integer>();
			else
				m_Chats.clear();
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, m_vo.AD_Table_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Integer key = Integer.valueOf(rs.getInt(2));	//	Record_ID
				Integer value = Integer.valueOf(rs.getInt(1));	//	CM_Chat_ID
				m_Chats.put(key, value);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		log.config("#" + m_Chats.size());
	}	//	loadChats

	/**
	 *	Returns true, if current row has a Chat
	 *  @return true if record has chat
	 */
	public boolean hasChat()
	{
		if (m_Chats == null)
			loadChats();
		if (m_Chats == null || m_Chats.isEmpty())
			return false;
		//
		Integer key = Integer.valueOf(m_mTable.getKeyID (m_currentRow));
		return m_Chats.containsKey(key);
	}	//	hasChat

	/**
	 *	Get Chat_ID for this record.
	 *	@return ID or 0, if not found
	 */
	public int getCM_ChatID()
	{
		if (m_Chats == null)
			loadChats();
		if (m_Chats.isEmpty())
			return 0;
		//
		Integer key = Integer.valueOf(m_mTable.getKeyID (m_currentRow));
		Integer value = m_Chats.get(key);
		if (value == null)
			return 0;
		else
			return value.intValue();
	}	//	getCM_ChatID


	/**************************************************************************
	 * 	Load Locks for Table and User
	 */
	public void loadLocks()
	{
		int AD_User_ID = Env.getCtx().getAD_User_ID();
		log.fine("#" + m_vo.TabNo + " - AD_User_ID=" + AD_User_ID);
		if (!canHaveAttachment())
			return;

		String sql = "SELECT Record_ID "
			+ "FROM AD_Private_Access "
			+ "WHERE AD_User_ID=? AND AD_Table_ID=? AND IsActive='Y' "
			+ "ORDER BY Record_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			if (m_Lock == null)
				m_Lock = new ArrayList<Integer>();
			else
				m_Lock.clear();
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_User_ID);
			pstmt.setInt(2, m_vo.AD_Table_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Integer key = Integer.valueOf(rs.getInt(1));
				m_Lock.add(key);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		log.fine("#" + m_Lock.size());
	}	//	loadLooks

	/**
	 * 	Record Is Locked
	 * 	@return true if locked
	 */
	public boolean isLocked()
	{
		if (!MRole.getDefault(m_vo.ctx, false).isPersonalLock())
			return false;
		if (m_Lock == null)
			loadLocks();
		if (m_Lock == null || m_Lock.isEmpty())
			return false;
		//
		Integer key = Integer.valueOf(m_mTable.getKeyID (m_currentRow));
		return m_Lock.contains(key);
	}	//	isLocked

	/**
	 * 	Lock Record
	 * 	@param ctx context
	 *	@param Record_ID id
	 *	@param lock true if lock, otherwise unlock
	 */
	public void lock (Ctx ctx, int Record_ID, boolean lock)
	{
		int AD_User_ID = ctx.getAD_User_ID();
		log.fine("Lock=" + lock + ", AD_User_ID=" + AD_User_ID
				+ ", AD_Table_ID=" + m_vo.AD_Table_ID + ", Record_ID=" + Record_ID);
		MPrivateAccess access = MPrivateAccess.get (ctx, AD_User_ID, m_vo.AD_Table_ID, Record_ID);
		if (access == null)
			access = new MPrivateAccess (ctx, AD_User_ID, m_vo.AD_Table_ID, Record_ID);
		access.setIsActive(lock);
		access.save();
		//
		loadLocks();
	}	//	lock


	/**************************************************************************
	 *	Data Status Listener from MTable.
	 *  - get raw info and add current row information
	 *  - update the current row
	 *  - redistribute (fire) Data Status event
	 *  @param e event
	 */
	public void dataStatusChanged (DataStatusEvent e)
	{
		log.fine("#" + m_vo.TabNo + " - " + e.toString());
		int oldCurrentRow = e.getCurrentRow();
		m_DataStatusEvent = e;          //  save it
		//  when sorted set current row to 0
		String msg = m_DataStatusEvent.getAD_Message();
		if (msg != null && msg.equals("Sorted"))
			setCurrentRow(0, true);
		//  set current row
		m_DataStatusEvent.setCurrentRow(m_currentRow);
		//  Same row - update value
		if (oldCurrentRow == m_currentRow)
		{
			GridField field = m_mTable.getField(e.getChangedColumn());
			if (field != null)
			{
				Object value = m_mTable.getValueAt(m_currentRow, e.getChangedColumn());
				field.setValue(value, m_mTable.isInserting());
			}
		}
		else    //  Redistribute Info with current row info
			fireDataStatusChanged(m_DataStatusEvent);
		//	log.fine("dataStatusChanged #" + m_vo.TabNo + "- fini", e.toString());
	}	//	dataStatusChanged

	/**
	 *	Inform Listeners and build WHO info
	 *  @param e event
	 */
	private void fireDataStatusChanged (DataStatusEvent e)
	{
		DataStatusListener[] listeners = m_listenerList.getListeners(DataStatusListener.class);
		if (listeners.length == 0)
			return;
		log.fine(e.toString());
		//  WHO Info
		if (e.getCurrentRow() >= 0)
		{
			e.Created = (Timestamp)getValue("Created");
			e.CreatedBy = (Integer)getValue("CreatedBy");
			e.Updated = (Timestamp)getValue("Updated");
			e.UpdatedBy = (Integer)getValue("UpdatedBy");
			e.Record_ID = getValue(m_keyColumnName);
			//  Info
			StringBuffer info = new StringBuffer(getTableName());
			//  We have a key column
			if (m_keyColumnName != null && m_keyColumnName.length() > 0)
			{
				info.append(" - ")
				.append(m_keyColumnName).append("=").append(e.Record_ID);
			}
			else    //  we have multiple parents
			{
				for (int i = 0; i < m_parents.size(); i++)
				{
					String keyCol = m_parents.get(i);
					info.append(" - ")
					.append(keyCol).append("=").append(getValue(keyCol));
				}
			}
			e.Info = info.toString();
		}
		e.setInserting(m_mTable.isInserting());
		//  Distribute/fire it
		for (DataStatusListener element : listeners)
			element.dataStatusChanged(e);
		//	log.fine("fini - " + e.toString());
	}	//	fireDataStatusChanged

	/**
	 *  Create and fire Data Status Error Event
	 *  @param AD_Message message
	 *  @param info info
	 *  @param isError if not true, it is a Warning
	 */
	public void fireDataStatusEEvent(String AD_Message, String info, boolean isError)
	{
		m_mTable.fireDataStatusEEvent(AD_Message, info, isError);
	}   //  fireDataStatusEvent

	/**
	 *  Create and fire Data Status Error Event (from Error Log)
	 *  @param errorLog log
	 */
	public void fireDataStatusEEvent (ValueNamePair errorLog)
	{
		if (errorLog != null)
			m_mTable.fireDataStatusEEvent(errorLog);
	}   //  fireDataStatusEvent

	/**
	 *  Get Current Row
	 *  @return current row
	 */
	public int getCurrentRow()
	{
		if (m_currentRow != verifyRow(m_currentRow))
			setCurrentRow(m_mTable.getRowCount()-1, true);
		return m_currentRow;
	}   //  getCurrentRow

	/**
	 *  Get Current Table Key ID
	 *  @return Record_ID
	 */
	public int getRecord_ID()
	{
		return m_mTable.getKeyID(m_currentRow);
	}   //  getRecord_ID

	/**
	 *  Get Key ID of row
	 *  @param  row row number
	 *  @return The Key ID of the row or -1 if not found
	 */
	public int getKeyID (int row)
	{
		return m_mTable.getKeyID (row);
	}   //  getCurrentKeyID

	/**
	 *  Get Current Table AD_Client_ID
	 *  @return AD_Client_ID or -1 if not found
	 */
	public int getAD_Client_ID()
	{
		Object oo = getValue("AD_Client_ID");
		if (oo instanceof Integer)
			return (Integer)oo;
		return -1;
	}   //  getAD_Client_ID

	/**
	 *  Navigate absolute - goto Row - (zero based).
	 *  - does nothing, if in current row
	 *  - saves old row if required
	 *  @param targetRow target row
	 *  @return current row
	 */
	public int navigate (int targetRow)
	{
		//  nothing to do
		if (targetRow == m_currentRow)
			return m_currentRow;
		log.info ("Row=" + targetRow);

		//  Row range check
		int newRow = verifyRow(targetRow);
		int row = m_currentRow;

		//  Check, if we have old uncommitted data
		if (m_mTable.dataSave(newRow, false)) {
			//  new position
			row =  setCurrentRow(newRow, true);
		}

		return row;
	}   //  navigate

	/**
	 *  Navigate relatively - i.e. plus/minus from current position
	 *  @param rowChange row change
	 *  @return current row
	 */
	public int navigateRelative (int rowChange)
	{
		return navigate (m_currentRow + rowChange);
	}   //  navigateRelative

	/**
	 *  Navigate to current now (reload)
	 *  @return current row
	 */
	public int navigateCurrent()
	{
		log.info("Row=" + m_currentRow);
		return setCurrentRow(m_currentRow, true);
	}   //  navigateCurrent

	/**
	 *  Row Range check
	 *  @param targetRow target row
	 *  @return checked row
	 */
	private int verifyRow (int targetRow)
	{
		int newRow = targetRow;
		//  Table Open?
		if (!m_mTable.isOpen())
		{
			log.severe ("Table " + m_mTable.getTableName() + " not open (Tab " + m_vo.AD_Tab_ID + ")");
			return -1;
		}
		//  Row Count
		int rows = getRowCount();
		if (rows == 0)
		{
			log.fine("No Rows");
			return -1;
		}
		if (newRow >= rows)
		{
			newRow = rows-1;
			log.fine("Set to max Row: " + newRow);
		}
		else if (newRow < 0)
		{
			newRow = 0;
			log.fine("Set to first Row");
		}
		return newRow;
	}   //  verifyRow

	/**
	 *  Set current row and load data into fields.
	 *  If there is no row - load nulls
	 *  @param newCurrentRow new current row
	 *  @param fireEvents fire events
	 *  @return current row
	 */
	private int setCurrentRow (int newCurrentRow, boolean fireEvents)
	{
		int oldCurrentRow = m_currentRow;
		m_currentRow = verifyRow (newCurrentRow);
		log.fine("Row=" + m_currentRow + " - fire=" + fireEvents);

		//  Update Field Values
		int size = m_mTable.getColumnCount();
		for (int i = 0; i < size; i++)
		{
			GridField mField = m_mTable.getField(i);
			//  get Value from Table
			if (m_currentRow >= 0)
			{
				Object value = m_mTable.getValueAt(m_currentRow, i);
				mField.setValue(value, m_mTable.isInserting());

				// gwu: now always validated, not just when inserting
				mField.validateValue();
			}
			else
			{   
				//  no rows - set to a reasonable value - not updateable
				//				Object value = null;
				//				if (mField.isKey() || mField.isParent() || mField.getColumnName().equals(m_linkColumnName))
				//				value = mField.getDefault();
				if (mField.isKey()) //refresh the context if the field is Key. This makes sure that Tab Level 2 and above works fine.
					mField.setValue(null,false);
				else
					mField.setValue();						
			}
		}

		if (!fireEvents)    //  prevents informing twice
			return m_currentRow;

		//  inform VTable/..    -> rowChanged
		m_propertyChangeSupport.firePropertyChange(PROPERTY, oldCurrentRow, m_currentRow);

		//  inform APanel/..    -> dataStatus with row updated
		if (m_DataStatusEvent == null)
			m_DataStatusEvent = new DataStatusEvent(this, getRowCount(),
					m_mTable.isInserting(),		//	changed
					Env.getCtx().isAutoCommit(m_vo.WindowNo), m_mTable.isInserting());
		//
		m_DataStatusEvent.setCurrentRow(m_currentRow);
		String status = m_DataStatusEvent.getAD_Message();
		if (status == null || status.length() == 0)
			m_DataStatusEvent.setInfo("NavigateOrUpdate", null, false,false);
		fireDataStatusChanged(m_DataStatusEvent);
		return m_currentRow;
	}   //  setCurrentRow


	/**************************************************************************
	 *  Get RowCount
	 *  @return row count
	 */
	public int getRowCount()
	{
		int count = m_mTable.getRowCount();
		//  Wait a bit if currently loading
		if (count == 0 && m_mTable.isLoading())
		{
			try
			{
				Thread.sleep(100);      //  .1 sec
			}
			catch (InterruptedException e) {}
			count = m_mTable.getRowCount();
		}
		return count;
	}   //  getRowCount

	/**
	 *  Get Column/Field Count
	 *  @return field count
	 */
	public int getFieldCount()
	{
		return m_mTable.getColumnCount();
	}   //  getFieldCount

	/**
	 *  Get Field by index
	 *  @param index index
	 *  @return MField
	 */
	public GridField getField (int index)
	{
		return m_mTable.getField(index);
	}   //  getField

	/**
	 * 	Find Column
	 *	@param columnName column
	 *	@return index of column/field
	 */
	public int findColumn (String columnName)
	{
		return m_mTable.findColumn(columnName);
	}	//	findColumn

	/**
	 *  Get Field by DB column name
	 *  @param columnName column name
	 *  @return MField
	 */
	public GridField getField (String columnName)
	{
		return m_mTable.getField(columnName);
	}   //  getField

	/**
	 *  Get all Fields
	 *  @return MFields
	 */
	public GridField[] getFields ()
	{
		return m_mTable.getFields();
	}   //  getField

	/**
	 *  Set New Value & call Callout
	 *  @param columnName database column name
	 *  @param value value
	 *  @return error message or ""
	 */
	public String setValue (String columnName, Object value)
	{
		if (columnName == null)
			return "NoColumn";
		return setValue(m_mTable.getField(columnName), value);
	}   //  setValue

	/**
	 *  Set New Value & call Callout
	 *  @param field field
	 *  @param value value
	 *  @return error message or ""
	 */
	public String setValue (GridField field, Object value)
	{
		if (field == null)
			return "NoField";

		log.fine(field.getColumnName() + "=" + value + " - Row=" + m_currentRow);

		int col = m_mTable.findColumn(field.getColumnName());
		m_mTable.setValueAt(value, m_currentRow, col, false);
		//
		return processFieldChange (field);
	}   //  setValue

	/**
	 * 	Is Processed
	 *	@return true if current record is processed
	 */
	public boolean isProcessed()
	{
		int index = m_mTable.findColumn("Processed");
		if (index != -1)
		{
			Object oo = m_mTable.getValueAt(m_currentRow, index);
			if (oo instanceof String)
				return "Y".equals(oo);
			if (oo instanceof Boolean)
				return ((Boolean)oo).booleanValue();
		}
		return "Y".equals(m_vo.ctx.getContext( m_vo.WindowNo, "Processed"));
	}	//	isProcessed

	/**
	 *  Process Field Change - evaluate Dependencies and process Callouts.
	 *
	 *  called from MTab.setValue or GridController.dataStatusChanged
	 *  @param changedField changed field
	 *  @return error message or ""
	 */
	public String processFieldChange (GridField changedField)
	{
		processDependencies (changedField);
		return processCallout (changedField);
	}   //  processFieldChange

	/**
	 *  Evaluate Dependencies
	 *  @param changedField changed field
	 */
	private void processDependencies (GridField changedField)
	{
		String columnName = changedField.getColumnName();
		//	log.trace(log.l4_Data, "Changed Column", columnName);

		//  when column name is not in list of DependentOn fields - fini
		if (!hasDependants(columnName))
			return;

		//  Get dependent MFields (may be because of display or dynamic lookup)
		ArrayList<GridField> list = getDependantFields(columnName);
		for (int i = 0; i < list.size(); i++)
		{
			GridField dependentField = list.get(i);
			//	log.trace(log.l5_DData, "Dependent Field", dependentField==null ? "null" : dependentField.getColumnName());
			//  if the field has a lookup
			if (dependentField != null && dependentField.getLookup() instanceof MLookup)
			{
				MLookup mLookup = (MLookup)dependentField.getLookup();
				//	log.trace(log.l6_Database, "Lookup Validation", mLookup.getValidation());
				//  if the lookup is dynamic (i.e. contains this columnName as variable)
				if (mLookup.getValidation().indexOf("@"+columnName+"@") != -1)
				{
					log.fine(columnName + " changed - "
							+ dependentField.getColumnName() + " set to null");
					//  invalidate current selection
					setValue(dependentField, null);
				}
			}
			if (dependentField != null && dependentField.getLookup() instanceof MLocatorLookup)
			{
				// gwu: invalidate currently selected locator if any dependent fields changed
				MLocatorLookup locLookup = (MLocatorLookup) dependentField.getLookup();
				int valueAsInt = 0;
				if( changedField.getValue() != null && changedField.getValue() instanceof Number )
					valueAsInt = ((Number) changedField.getValue()).intValue();
				if( columnName.equals( "M_Warehouse_ID" ) )
				{
					locLookup.setOnly_Warehouse_ID( valueAsInt );
				}
				if( columnName.equals( "M_Product_ID" ) )
				{
					locLookup.setOnly_Product_ID( valueAsInt );
				}
				locLookup.setOnly_Outgoing(Env.getCtx().isSOTrx(m_vo.WindowNo ));
				locLookup.refresh();
				if( !locLookup.isValid( dependentField.getValue() ) )
					setValue(dependentField, null);
			}
		}   //  for all dependent fields
	}   //  processDependencies


	/**************************************************************************
	 *  Process Callout(s).
	 *  <p>
	 *  The Callout is in the string of
	 *  "class.method;class.method;"
	 * If there is no class name, i.e. only a method name, the class is regarded
	 * as CalloutSystem.
	 * The class needs to comply with the Interface Callout.
	 *
	 * For a limited time, the old notation of Sx_matheod / Ux_menthod is maintained.
	 *
	 * @param field field
	 * @return error message or ""
	 * @see org.compiere.model.Callout
	 */
	private String processCallout (GridField field)
	{
		String callout = field.getCallout();
		if (callout.length() == 0)
			return "";
		//
		if (isProcessed())		//	only active records
			return "";			//	"DocProcessed";

		Object value = field.getValue();
		Object oldValue = field.getOldValue();
		log.fine(field.getColumnName() + "=" + value
				+ " (" + callout + ") - old=" + oldValue);

		StringTokenizer st = new StringTokenizer(callout, ";,", false);
		while (st.hasMoreTokens())      //  for each callout
		{
			String cmd = st.nextToken().trim();
			Callout call = null;
			String method = null;
			int methodStart = cmd.lastIndexOf(".");
			try
			{
				if (methodStart != -1)      //  no class
				{
					Class<?> cClass = Class.forName(cmd.substring(0,methodStart));
					call = (Callout)cClass.newInstance();
					method = cmd.substring(methodStart+1);
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "class", e);
				return "Callout Invalid: " + cmd + " (" + e.toString() + ")";
			}

			if (call == null || method == null || method.length() == 0)
				return "Callout Invalid: " + method;

			String retValue = "";
			try
			{
				retValue = call.start(m_vo.ctx, method, m_vo.WindowNo, this, field, value, oldValue);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "start", e);
				retValue = 	"Callout Invalid: " + e.toString();
				return retValue;
			}
			if (!retValue.equals(""))		//	interrupt on first error
			{
				log.warning (retValue);
				return retValue;
			}
		}   //  for each callout
		return "";
	}	//	processCallout


	/**
	 *  Get Value of Field with columnName
	 *  @param columnName column name
	 *  @return value
	 */
	public Object getValue (String columnName)
	{
		if (columnName == null)
			return null;
		GridField field = m_mTable.getField(columnName);
		return getValue(field);
	}   //  getValue

	/**
	 *  Get Value of Field
	 *  @param field field
	 *  @return value
	 */
	public Object getValue (GridField field)
	{
		if (field == null)
			return null;
		return field.getValue();
	}   //  getValue

	/**
	 *  Get Value of Field in row
	 *  @param row row
	 *  @param columnName column name
	 *  @return value
	 */
	public Object getValue (int row, String columnName)
	{
		int col = m_mTable.findColumn(columnName);
		if (col == -1)
			return null;
		return m_mTable.getValueAt(row, col);
	}   //  getValue

	/**
	 *  toString
	 *  @return String representation
	 */
	@Override
	public String toString()
	{
		String retValue = "MTab #" + m_vo.TabNo;
		if (m_vo != null)
		{
			retValue += " " + m_vo.Name + " (" + m_vo.AD_Tab_ID
			+ ") QueryActive=" + (m_query != null && m_query.isActive())
			+ ", CurrentDays=" + m_vo.onlyCurrentDays;
		}
		return retValue;
	}   //  toString


	/**************************************************************************
	 *  @param l listener
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener l)
	{
		m_propertyChangeSupport.removePropertyChangeListener(l);
	}
	/**
	 *  @param l listener
	 */
	public synchronized void addPropertyChangeListener(PropertyChangeListener l)
	{
		m_propertyChangeSupport.addPropertyChangeListener(l);
	}

	/**
	 *  @param l listener
	 */
	public synchronized void removeDataStatusListener(DataStatusListener l)
	{
		m_listenerList.remove(DataStatusListener.class, l);
	}
	/**
	 *  @param l listener
	 */
	public synchronized void addDataStatusListener(DataStatusListener l)
	{
		m_listenerList.add(DataStatusListener.class, l);
	}

}	//	MTab
