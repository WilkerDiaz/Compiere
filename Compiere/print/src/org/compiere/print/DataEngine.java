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
package org.compiere.print;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.CompiereStateException;
import org.compiere.common.FieldType;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Data Engine.
 * 	Creates SQL and laods data into PrintData (including totals/etc.)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: DataEngine.java,v 1.3 2006/07/30 00:53:02 jjanke Exp $
 */
public class DataEngine
{
	/**
	 *	Constructor
	 *	@param language Language of the data (for translation)
	 */
	public DataEngine (Language language)
	{
		if (language != null)
			m_language = language;
	}	//	DataEngine

	/**	Logger							*/
	private static CLogger	log = CLogger.getCLogger (DataEngine.class);

	/**	Synonym							*/
	private	String			m_synonym = "A";

	/**	Default Language				*/
	private Language		m_language = Language.getLoginLanguage();
	/** Break & Column Funcations		*/
	private PrintDataGroup 	m_group = new PrintDataGroup();
	/**	Start Time						*/
	private long			m_startTime = System.currentTimeMillis();
	/** Running Total after .. lines	*/
	private int				m_runningTotalLines = -1;
	/** Key Indicator in Report			*/
	public static final String KEY = "*";


	/**************************************************************************
	 * 	Load Data
	 *
	 * 	@param format print format
	 * 	@param query query
	 * 	@param ctx context
	 * 	@return PrintData or null
	 */
	public PrintData getPrintData (Ctx ctx, MPrintFormat format, Query query)
	{
		if (format == null)
			throw new CompiereStateException ("No print format");
		String tableName = null;
		String reportName = format.getName();
		String whereClause = null;
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if (format.getAD_ReportView_ID() != 0) {
			String sql = "SELECT t.AD_Table_ID, t.TableName, rv.Name, rv.WhereClause "
				+ "FROM AD_Table t"
				+ " INNER JOIN AD_ReportView rv ON (t.AD_Table_ID=rv.AD_Table_ID) "
				+ "WHERE rv.AD_ReportView_ID=?";	//	1
			try {
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(1, format.getAD_ReportView_ID());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					tableName = rs.getString(2);  	//	TableName
					reportName = rs.getString(3);
					whereClause = rs.getString(4);
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
				return null;
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			// SR 10021766: If a report view has a where clause, add it to the query restriction.
			if (whereClause != null && query.getWhereClause() != null && !query.getWhereClause().equalsIgnoreCase(whereClause))
				query.addRestriction(whereClause);
		} else {
			String sql = "SELECT TableName FROM AD_Table WHERE AD_Table_ID=?";	//	#1
			try {
				pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
				pstmt.setInt(1, format.getAD_Table_ID());
				rs = pstmt.executeQuery();
				if (rs.next())
					tableName = rs.getString(1);		//  TableName
			} catch (SQLException e1) {
				log.log(Level.SEVERE, sql, e1);
				return null;
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		if (tableName == null) {
			log.log(Level.SEVERE, "Not found Format=" + format);
			return null;
		}
		if (format.isTranslationView() && tableName.toLowerCase().endsWith("_v"))	//	_vt not just _v
			tableName += "t";
		format.setTranslationViewQuery (query);
		//
		PrintData pd = getPrintDataInfo (ctx, format, query, reportName, tableName);
		if (pd == null)
			return null;
		loadPrintData(pd, format);
		return pd;
	}
	
	private String getKeySQL (Language language,String tableName)
	{
		//	get display column name (first identifier column)
		String sql = "SELECT c.ColumnName,c.IsTranslated,c.AD_Reference_ID,c.AD_Reference_Value_ID "
			+ "FROM AD_Table t INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
			+ "WHERE TableName=?"
			+ " AND c.IsIdentifier='Y' "
			+ "ORDER BY c.SeqNo";
		
		ArrayList<LookupDisplayColumn> list = new ArrayList<LookupDisplayColumn>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, tableName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				LookupDisplayColumn ldc = new LookupDisplayColumn (rs.getString(1),
					"Y".equals(rs.getString(2)), rs.getInt(3), rs.getInt(4));
				list.add (ldc);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
			return "";
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//  Do we have columns ?
		if (list.size() == 0)
			return "";

		StringBuffer embedSQL = new StringBuffer("");

		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (i > 0)
				embedSQL.append("||' - '||" );
			LookupDisplayColumn ldc = list.get(i);

			//  date, number
			if (FieldType.isDate(ldc.DisplayType) || FieldType.isNumeric(ldc.DisplayType)) {
				embedSQL.append(DB.TO_CHAR(tableName + "." + ldc.ColumnName, ldc.DisplayType, language.getAD_Language()));
			//  String
			} else {
				//jz EDB || problem
				if (DB.isPostgreSQL())
					embedSQL.append("COALESCE(TO_CHAR(").append(tableName).append(".").append(ldc.ColumnName).append("),'')");
				else
					embedSQL.append(tableName).append(".").append(ldc.ColumnName);
			}
		}
		return embedSQL.toString();
	}
	
	/**************************************************************************
	 * 	Create Load SQL and update PrintData Info
	 *
	 * 	@param ctx context
	 * 	@param format print format
	 * 	@param query query
	 *  @param reportName report name
	 *  @param tableName table name
	 * 	@return PrintData or null
	 */
	private PrintData getPrintDataInfo (Ctx ctx, MPrintFormat format, Query query,
		String reportName, String tableName)
	{
		m_startTime = System.currentTimeMillis();
		log.info(reportName + " - " + m_language.getAD_Language());
		log.fine("TableName=" + tableName + ", Query=" + query);
		log.fine("Format=" + format);
		ArrayList<PrintDataColumn> columns = new ArrayList<PrintDataColumn>();
		m_group = new PrintDataGroup();

		//	Order Columns (identifed by non zero/null SortNo)
		ArrayList<NamePair> orderByColumns = format.getOrderByColumns();
		ArrayList<String> orderColumnNames = new ArrayList<String>(orderByColumns.size());
		for (int i=0; i<orderByColumns.size(); i++) {
			orderColumnNames.add("");		//	initial value overwritten with fully qualified name
		}

		//	Direct SQL w/o Reference Info
		StringBuffer sqlSELECT = new StringBuffer("SELECT ");
		StringBuffer sqlFROM = new StringBuffer(" FROM ");
		sqlFROM.append(tableName);
		StringBuffer sqlGROUP = new StringBuffer(" GROUP BY ");
		//
		boolean IsGroupedBy = false;
		//
		String sql = "SELECT c.AD_Column_ID,c.ColumnName,"				//	1..2
			+ "c.AD_Reference_ID,c.AD_Reference_Value_ID,"				//	3..4
			+ "c.FieldLength,c.IsMandatory,c.IsKey,c.IsParent,"			//	5..8
			+ "COALESCE(rvc.IsGroupFunction,'N'),rvc.FunctionColumn,"	//	9..10
			+ "pfi.IsGroupBy,pfi.IsSummarized,pfi.IsAveraged,pfi.IsCounted, "	//	11..14
			+ "pfi.IsPrinted,pfi.SortNo,pfi.IsPageBreak, "				//	15..17
			+ "pfi.IsMinCalc,pfi.IsMaxCalc, "							//	18..19
			+ "pfi.IsRunningTotal,pfi.RunningTotalLines, "				//	20..21
			+ "pfi.IsVarianceCalc, pfi.IsDeviationCalc, "				//	22..23
			+ "c.ColumnSQL "											//	24
			+ "FROM AD_PrintFormat pf"
			+ " INNER JOIN AD_PrintFormatItem pfi ON (pf.AD_PrintFormat_ID=pfi.AD_PrintFormat_ID)"
			+ " INNER JOIN AD_Column c ON (pfi.AD_Column_ID=c.AD_Column_ID)"
			+ " LEFT OUTER JOIN AD_ReportView_Col rvc ON (pf.AD_ReportView_ID=rvc.AD_ReportView_ID AND c.AD_Column_ID=rvc.AD_Column_ID) "
			+ "WHERE pf.AD_PrintFormat_ID=?"					//	#1
			+ " AND pfi.IsActive='Y' AND (pfi.IsPrinted='Y' OR c.IsKey='Y' OR pfi.SortNo > 0) "
			+ "ORDER BY pfi.IsPrinted DESC, pfi.SeqNo";			//	Functions are put in first column
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, format.get_ID());
			rs = pstmt.executeQuery();
			m_synonym = "A";		//	synonym
			while (rs.next()) {
				//	get Values from record
				int AD_Column_ID = rs.getInt(1);
				String ColumnName = rs.getString(2);
				String ColumnSQL = rs.getString(24);
				if (ColumnSQL == null)
					ColumnSQL = "";
				int AD_Reference_ID = rs.getInt(3);
				int AD_Reference_Value_ID = rs.getInt(4);
				//  ColumnInfo
				int FieldLength = rs.getInt(5);
				boolean IsMandatory = "Y".equals(rs.getString(6));
				boolean IsKey = "Y".equals(rs.getString(7));
				boolean IsParent = "Y".equals(rs.getString(8));
				//  SQL GroupBy
				boolean IsGroupFunction = "Y".equals(rs.getString(9));
				if (IsGroupFunction)
					IsGroupedBy = true;
				String FunctionColumn = rs.getString(10);
				if (FunctionColumn == null)
					FunctionColumn = "";
				//	Breaks/Column Functions
				if ("Y".equals(rs.getString(11)))
					m_group.addGroupColumn(ColumnName);
				if ("Y".equals(rs.getString(12)))
					m_group.addFunction(ColumnName, PrintDataFunction.F_SUM);
				if ("Y".equals(rs.getString(13)))
					m_group.addFunction(ColumnName, PrintDataFunction.F_MEAN);
				if ("Y".equals(rs.getString(14)))
					m_group.addFunction(ColumnName, PrintDataFunction.F_COUNT);
				if ("Y".equals(rs.getString(18)))	//	IsMinCalc
					m_group.addFunction(ColumnName, PrintDataFunction.F_MIN);
				if ("Y".equals(rs.getString(19)))	//	IsMaxCalc
					m_group.addFunction(ColumnName, PrintDataFunction.F_MAX);
				if ("Y".equals(rs.getString(22)))	//	IsVarianceCalc
					m_group.addFunction(ColumnName, PrintDataFunction.F_VARIANCE);
				if ("Y".equals(rs.getString(23)))	//	IsDeviationCalc
					m_group.addFunction(ColumnName, PrintDataFunction.F_DEVIATION);
				if ("Y".equals(rs.getString(20)))	//	isRunningTotal
					//	RunningTotalLines only once - use max
					m_runningTotalLines = Math.max(m_runningTotalLines, rs.getInt(21));	

				//	General Info
				boolean IsPrinted = "Y".equals(rs.getString(15));				
				boolean isPageBreak = "Y".equals(rs.getString(17));

				//	Fully qualified Table.Column for ordering
				String orderName = tableName + "." + ColumnName;
				PrintDataColumn pdc = null;

				//  -- Key --
				if (IsKey) {
					String keySQL = getKeySQL(m_language, tableName);
					String alias = KEY;
					if(keySQL != null && keySQL.length()>0) {
						sqlSELECT.append(keySQL).append(" AS ").append(m_synonym).append(ColumnName).append(",");
						alias = m_synonym + ColumnName;
					}
					//	=>	Table.Column,
					sqlSELECT.append(tableName).append(".").append(ColumnName).append(",");
					sqlGROUP.append(tableName).append(".").append(ColumnName).append(",");
					
					pdc = new PrintDataColumn(AD_Column_ID, ColumnName, AD_Reference_ID, FieldLength, alias, isPageBreak);	//	KeyColumn
				}
				else if (!IsPrinted)	//	not printed Sort Columns
					;
				//	-- Parent, TableDir (and unqualified Search) --
				else if (IsParent 
						|| AD_Reference_ID == DisplayTypeConstants.TableDir
						|| (AD_Reference_ID == DisplayTypeConstants.Search && AD_Reference_Value_ID == 0)) {
					String eSql = "";
					if (ColumnSQL.length() > 0) {
						log.fine(" Virtual Column " + ColumnName + "Column SQL : " + ColumnSQL);
						eSql = MLookupFactory.getLookup_TableDirVirtualEmbed(m_language, ColumnName, ColumnSQL);
					} else {
						//  Creates Embedded SQL in the form
						//  SELECT ColumnTable.Name FROM ColumnTable WHERE TableName.ColumnName=ColumnTable.ColumnName
						eSql = MLookupFactory.getLookup_TableDirEmbed(m_language, ColumnName, tableName);
					}
					
					//	TableName
					String table = ColumnName;
					if (table.endsWith("_ID"))
						table = table.substring(0, table.length()-3);
					//  DisplayColumn
					String display = ColumnName;
					//	=> (..) AS AName, Table.ID,
					
					// isParent could still be a Table reference with isValueDisplayed
					if((AD_Reference_ID == DisplayTypeConstants.Table || AD_Reference_ID == DisplayTypeConstants.Search)
					   && AD_Reference_Value_ID != 0) {
						TableReference tr = getTableReference(AD_Reference_Value_ID, m_language);
						if (tr.IsValueDisplayed) {
							// Replace the beginning of the Embedded SQL
							// SELECT ColumnTable.Name ...
							// with the following
							// SELECT ColumnTable.Value||'-'||ColumnTable.Name ...
							eSql = "SELECT " + table + ".Value||'-'||" + table + eSql.substring(eSql.indexOf("."), eSql.length());
						}
					}
					sqlSELECT.append("(").append(eSql).append(") AS ").append(m_synonym).append(display).append(",");
					if (ColumnSQL.length()>0)
						sqlSELECT.append(ColumnSQL).append(" AS ").append(ColumnName).append(",");
					else
						sqlSELECT.append(tableName).append(".").append(ColumnName).append(",");
					sqlGROUP.append(m_synonym).append(display).append(",")
						.append(tableName).append(".").append(ColumnName).append(",");
					orderName = m_synonym + display;
					//
					pdc = new PrintDataColumn(AD_Column_ID, ColumnName, AD_Reference_ID, FieldLength, orderName, isPageBreak);
					synonymNext();
				}

				//	-- Table --
				else if (AD_Reference_ID == DisplayTypeConstants.Table
						|| (AD_Reference_ID == DisplayTypeConstants.Search && AD_Reference_Value_ID != 0)) {
					String eSql = "";
					TableReference tr = getTableReference(AD_Reference_Value_ID, m_language);
					String display = tr.DisplayColumn;

					if (ColumnSQL.length() > 0) {
						log.fine(" Virtual Column " + ColumnName + "Column SQL : " + ColumnSQL);
						eSql = MLookupFactory.getLookup_TableVirtualEmbed(m_language, AD_Reference_Value_ID, ColumnSQL);
						sqlSELECT.append("(").append(eSql).append(") AS ").append(m_synonym).append(display).append(",")
							.append(ColumnSQL).append(" AS ").append(ColumnName).append(",");
						sqlGROUP.append(m_synonym).append(display).append(",")
							.append(tableName).append(".").append(ColumnName).append(",");
						orderName = m_synonym + display;
					} else {
						String baseTableName = tr.TableName;
						String baseTableSynonym = m_synonym;
						if (tr.TableName!=null && tr.TableName.toUpperCase().endsWith("_TRL")){
							baseTableName = tr.TableName.substring(0, tr.TableName.length()-4);
							baseTableSynonym = m_synonym+"_BASE";
						}
						if (tr.IsValueDisplayed)
							sqlSELECT.append(baseTableSynonym).append(".Value||'-'||");
						sqlSELECT.append(m_synonym).append(".").append(display);
						sqlSELECT.append(" AS ").append(m_synonym).append(display).append(",")
							.append(tableName).append(".").append(ColumnName).append(",");
						sqlGROUP.append(m_synonym).append(".").append(display).append(",")
							.append(tableName).append(".").append(ColumnName).append(",");
						orderName = m_synonym + display;
	
						//	=> x JOIN table A ON (x.KeyColumn=A.Key)
						if (IsMandatory)
							sqlFROM.append(" INNER JOIN ");
						else
							sqlFROM.append(" LEFT OUTER JOIN ");
						sqlFROM.append(tr.TableName).append(" ").append(m_synonym).append(" ON (")
							.append(tableName).append(".").append(ColumnName).append("=")
							.append(m_synonym).append(".").append(tr.KeyColumn);
						if (baseTableSynonym.equals(m_synonym)){
							// synonym is the BASE table, no need to join to Language
							sqlFROM.append(")"); 
						} else {
							// synonym is the TRL table, need to join to Language
							sqlFROM.append(" AND ").append(m_synonym).append(".AD_Language='").append(m_language.getAD_Language()).append("')");
							
							if (IsMandatory)
								sqlFROM.append(" INNER JOIN ");
							else
								sqlFROM.append(" LEFT OUTER JOIN ");
							sqlFROM.append(baseTableName).append(" ").append(baseTableSynonym).append(" ON (")
								.append(tableName).append(".").append(ColumnName).append("=")
								.append(baseTableSynonym).append(".").append(tr.KeyColumn).append(")");
						}
					}
					//
					pdc = new PrintDataColumn(AD_Column_ID, ColumnName, AD_Reference_ID, FieldLength, orderName, isPageBreak);
					synonymNext();
				}

				//	-- List or Button with ReferenceValue --
				else if (AD_Reference_ID == DisplayTypeConstants.List 
					|| (AD_Reference_ID == DisplayTypeConstants.Button && AD_Reference_Value_ID != 0)) {
					if (ColumnSQL.length() > 0) {
						log.fine(" Virtual Column " + ColumnName + "Column SQL : " + ColumnSQL);
						String eSQL = MLookupFactory.getLookup_ListVirtualEmbed(m_language,AD_Reference_Value_ID,ColumnSQL);
						sqlSELECT.append("(").append(eSQL).append(") AS ").append(m_synonym).append("Name,");
						sqlSELECT.append(ColumnSQL).append(" AS ").append(ColumnName).append(",");
						sqlGROUP.append(m_synonym).append(".Name,");
						orderName = m_synonym + "Name";
						
					} else {
						if (Env.isBaseLanguage(m_language, "AD_Ref_List")) {
							//	=> A.Name AS AName,
							sqlSELECT.append(m_synonym).append(".Name AS ").append(m_synonym).append("Name,");
							sqlGROUP.append(m_synonym).append(".Name,");
							orderName = m_synonym + "Name";
							//	=> x JOIN AD_Ref_List A ON (x.KeyColumn=A.Value AND A.AD_Reference_ID=123)
							if (IsMandatory)
								sqlFROM.append(" INNER JOIN ");
							else
								sqlFROM.append(" LEFT OUTER JOIN ");
							sqlFROM.append("AD_Ref_List ").append(m_synonym).append(" ON (")
								.append(tableName).append(".").append(ColumnName).append("=").append(m_synonym).append(".Value")
								.append(" AND ").append(m_synonym).append(".AD_Reference_ID=").append(AD_Reference_Value_ID).append(")");
						} else {
							//	=> A.Name AS AName,
							sqlSELECT.append(m_synonym).append(".Name AS ").append(m_synonym).append("Name,");
							sqlGROUP.append(m_synonym).append(".Name,");
							orderName = m_synonym + "Name";
	
							//	LEFT OUTER JOIN AD_Ref_List XA ON (AD_Table.EntityType=XA.Value AND XA.AD_Reference_ID=245)
							//	LEFT OUTER JOIN AD_Ref_List_Trl A ON (XA.AD_Ref_List_ID=A.AD_Ref_List_ID AND A.AD_Language='de_DE')
							if (IsMandatory)
								sqlFROM.append(" INNER JOIN ");
							else
								sqlFROM.append(" LEFT OUTER JOIN ");
							sqlFROM.append(" AD_Ref_List X").append(m_synonym).append(" ON (")
								.append(tableName).append(".").append(ColumnName).append("=X")
								.append(m_synonym).append(".Value AND X").append(m_synonym).append(".AD_Reference_ID=").append(AD_Reference_Value_ID)
								.append(")");
							if (IsMandatory)
								sqlFROM.append(" INNER JOIN ");
							else
								sqlFROM.append(" LEFT OUTER JOIN ");
							sqlFROM.append(" AD_Ref_List_Trl ").append(m_synonym).append(" ON (X")
								.append(m_synonym).append(".AD_Ref_List_ID=").append(m_synonym).append(".AD_Ref_List_ID")
								.append(" AND ").append(m_synonym).append(".AD_Language='").append(m_language.getAD_Language()).append("')");
						}
						// 	TableName.ColumnName,
						sqlSELECT.append(tableName).append(".").append(ColumnName).append(",");
					}
					pdc = new PrintDataColumn(AD_Column_ID, ColumnName, AD_Reference_ID, FieldLength, orderName, isPageBreak);
					synonymNext();
				}

				//  -- Special Lookups --
				else if (AD_Reference_ID == DisplayTypeConstants.Location
					|| AD_Reference_ID == DisplayTypeConstants.Account
					|| AD_Reference_ID == DisplayTypeConstants.Locator
					|| AD_Reference_ID == DisplayTypeConstants.PAttribute) {
					//	TableName, DisplayColumn
					String table = ""; 
					String key = ""; 
					String display = ""; 
					String synonym = null;
					//
					if (AD_Reference_ID == DisplayTypeConstants.Location) {
						table = "C_Location";
						key = "C_Location_ID";
						display = "City||'.'";	//	in case City is empty
						synonym = "Address";
					} else if (AD_Reference_ID == DisplayTypeConstants.Account) {
						table = "C_ValidCombination";
						key = "C_ValidCombination_ID";
						display = "Combination";
					} else if (AD_Reference_ID == DisplayTypeConstants.Locator) {
						table = "M_Locator";
						key = "M_Locator_ID";
						display = "Value";
					} else if (AD_Reference_ID == DisplayTypeConstants.PAttribute) {
						table = "M_AttributeSetInstance";
						key = "M_AttributeSetInstance_ID";
						display = "Description";
						if (CLogMgt.isLevelFine())
							//jz display += "||'{'||" + m_synonym + ".M_AttributeSetInstance_ID||'}'";
							display += "||'{'||" + DB.TO_CHAR(m_synonym + ".M_AttributeSetInstance_ID", DisplayTypeConstants.Number, Env.getAD_Language(Env.getCtx())) + "||'}'";
						synonym = "Description";
					}
					if (synonym == null)
						synonym = display;
					
					if(ColumnSQL.length()>0) {
						String eSQL="SELECT " + display + " FROM "
										+ table + " WHERE "
										+ key + "=" + ColumnSQL;
						sqlSELECT.append("(").append(eSQL).append(") AS ").append(m_synonym).append(synonym).append(",")
									.append(ColumnSQL).append(" AS ").append(ColumnName).append(",");
						sqlGROUP.append(m_synonym).append(synonym).append(",")
							.append(tableName).append(".").append(ColumnName).append(",");
						orderName = m_synonym + synonym;
					} else {
						//	=> A.Name AS AName, table.ID,
						sqlSELECT.append(m_synonym).append(".").append(display).append(" AS ")
							.append(m_synonym).append(synonym).append(",")
							.append(tableName).append(".").append(ColumnName).append(",");
						sqlGROUP.append(m_synonym).append(".").append(synonym).append(",")
							.append(tableName).append(".").append(ColumnName).append(",");
						orderName = m_synonym + synonym;
						//	=> x JOIN table A ON (table.ID=A.Key)
						if (IsMandatory)
							sqlFROM.append(" INNER JOIN ");
						else
							sqlFROM.append(" LEFT OUTER JOIN ");
						sqlFROM.append(table).append(" ").append(m_synonym).append(" ON (")
							.append(tableName).append(".").append(ColumnName).append("=")
							.append(m_synonym).append(".").append(key).append(")");
					}
					//
					pdc = new PrintDataColumn(AD_Column_ID, ColumnName, AD_Reference_ID, FieldLength, orderName, isPageBreak);
					synonymNext();
				}

				//	-- Standard Column --
				else {
					int index = FunctionColumn.indexOf("@");
					StringBuffer sb = new StringBuffer();
					if (ColumnSQL != null && ColumnSQL.length() > 0) {
					//	=> ColumnSQL AS ColumnName
						sb.append(ColumnSQL);
						sqlSELECT.append(sb).append(" AS ").append(ColumnName).append(",");
						if (!IsGroupFunction)
							sqlGROUP.append(sb).append(",");
						orderName = ColumnName;		//	no prefix for synonym
					} else if (index == -1) {
					//	=> Table.Column,
						sb.append(tableName).append(".").append(ColumnName).append(",");
						sqlSELECT.append(sb);
						if (!IsGroupFunction)
							sqlGROUP.append(sb).append(",");
					} else {
					//  => Function(Table.Column) AS Column   -- function has @ where column name goes
						sb.append(FunctionColumn.substring(0, index))
						//	If I eg entered sum(amount)  as function column in the report view the query would look like:
						//	Tablename.amountsum(amount), after removing the line below I get the wanted result. The original query column (tablename.column) is replaced by the function column entered in the report view window.
						//	.append(tableName).append(".").append(ColumnName)	// xxxxxx
							.append(FunctionColumn.substring(index+1));
						sqlSELECT.append(sb).append(" AS ").append(ColumnName).append(",");
						if (!IsGroupFunction)
							sqlGROUP.append(sb).append(",");
						orderName = ColumnName;		//	no prefix for synonym
					}
					pdc = new PrintDataColumn(AD_Column_ID, ColumnName, 
						AD_Reference_ID, FieldLength, ColumnName, isPageBreak);
				}
				
				//	Order Sequence - Overwrite order column name
				for (int i = 0; i < orderByColumns.size(); i++) {
					if (AD_Column_ID == Integer.parseInt(orderByColumns.get(i).getID())) {
						orderColumnNames.set(i, orderName);
						break;
					}
				}

				//
				if (pdc == null || (!IsPrinted && !IsKey))
					continue;

				columns.add(pdc);
			}	//	for all Fields in Tab
		} catch (SQLException e) {
			log.log(Level.SEVERE, "SQL=" + sql + " - ID=" + format.get_ID(), e);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (columns.size() == 0) {
			log.log(Level.SEVERE, "No Colums - Delete Report Format " + reportName + " and start again");
			log.finest("No Colums - SQL=" + sql + " - ID=" + format.get_ID());
			return null;
		}

		boolean hasLevelNo = false;
		if (tableName.startsWith("T_Report")) {	//	needed for LayoutEngine
			hasLevelNo = true;
			if (sqlSELECT.indexOf("LevelNo") == -1)
				sqlSELECT.append("LevelNo,");
		}

		/**
		 *	Assemble final SQL - delete last SELECT ','
		 */
		StringBuffer finalSQL = new StringBuffer();
		finalSQL.append(sqlSELECT.substring(0, sqlSELECT.length()-1))
			.append(sqlFROM);

		//	WHERE clause
		if (tableName.startsWith("T_Report")) {
			finalSQL.append(" WHERE ");
			for (int i = 0; i < query.getRestrictionCount(); i++)
			{
				String q = query.getWhereClause (i);
				if (q.indexOf("AD_PInstance_ID") != -1)	//	ignore all other Parameters
					finalSQL.append (q);
			}	//	for all restrictions
		} else {
			//	User supplied Where Clause
			if (query != null && query.isActive ()) {
				finalSQL.append (" WHERE ");
				if (!query.getTableName ().equals (tableName))
					query.setTableName (tableName);
				finalSQL.append (query.getWhereClause (true));
			}
			//	Access Restriction
			MRole role = MRole.getDefault(ctx, false);
			if (role.getAD_Role_ID() == 0 && !Ini.isClient())
				;	//	System Access
			else
				finalSQL = new StringBuffer (role.addAccessSQL (finalSQL.toString (), 
					tableName, MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO));
		}

		//  Group By
		if (IsGroupedBy)
			finalSQL.append(sqlGROUP.substring(0, sqlGROUP.length()-1));    //  last ,

		//	Add ORDER BY clause
		if (orderColumnNames != null) {
			for (int i = 0; i < orderColumnNames.size(); i++) {
				if (i == 0)
					finalSQL.append(" ORDER BY ");
				else
					finalSQL.append(",");
				String by = orderColumnNames.get(i);
				if (by == null || by.length() == 0)
					by = String.valueOf(i+1);
				else if (by.equals("T_Report.Record_ID"))	//	Fin Report Details First
					by = "CASE WHEN Record_ID=0 AND LevelNo=-1 THEN 99999 ELSE Record_ID END";
				finalSQL.append(by);
				
				Boolean isAscending = orderByColumns.get(i).getName().equals("Y");
				if(!isAscending)
					finalSQL.append(" DESC ");
			}
		}	//	order by

		//	Print Data
		PrintData pd = new PrintData (ctx, reportName);
		PrintDataColumn[] info = new PrintDataColumn [columns.size()];
		columns.toArray(info);		//	column order is is m_synonymc with SELECT column position
		pd.setColumnInfo(info);
		pd.setTableName(tableName);
		pd.setSQL(finalSQL.toString());
		pd.setHasLevelNo(hasLevelNo);

		log.finest (finalSQL.toString ());
		log.finest ("Group=" + m_group);
		return pd;
	}	//	getPrintDataInfo

	/**
	 *	Next Synonym.
	 * 	Creates next synonym A..Z AA..ZZ AAA..ZZZ
	 */
	private void synonymNext()
	{
		int length = m_synonym.length();
		char cc = m_synonym.charAt(0);
		if (cc == 'Z')
		{
			cc = 'A';
			length++;
		}
		else
			cc++;
		//
		m_synonym = String.valueOf(cc);
		if (length == 1)
			return;
		m_synonym += String.valueOf(cc);
		if (length == 2)
			return;
		m_synonym += String.valueOf(cc);
	}	//	synonymNext

	/**
	 *	Get TableName and ColumnName for Reference Tables.
	 *  @param AD_Reference_Value_ID reference value
	 *  @param language Language
	 *	@return 0=TableName, 1=KeyColumn, 2=DisplayColumn
	 */
	public static TableReference getTableReference (int AD_Reference_Value_ID, Language language)
	{
		TableReference tr = new TableReference();
		//
		String SQL = "SELECT t.TableName, ck.ColumnName AS KeyColumn,"	//	1..2
			+ " cd.ColumnName AS DisplayColumn, rt.IsValueDisplayed, cd.IsTranslated "
			+ "FROM AD_Ref_Table rt"
			+ " INNER JOIN AD_Table t ON (rt.AD_Table_ID = t.AD_Table_ID)"
			+ " INNER JOIN AD_Column ck ON (rt.Column_Key_ID = ck.AD_Column_ID)"
			+ " INNER JOIN AD_Column cd ON (rt.Column_Display_ID = cd.AD_Column_ID) "
			+ "WHERE rt.AD_Reference_ID=?"			//	1
			+ " AND rt.IsActive = 'Y' AND t.IsActive = 'Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, (Trx) null);
			pstmt.setInt (1, AD_Reference_Value_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				tr.TableName = rs.getString(1);
				tr.KeyColumn = rs.getString(2);
				tr.DisplayColumn = rs.getString(3);
				tr.IsValueDisplayed = "Y".equals(rs.getString(4));
				tr.IsTranslated = "Y".equals(rs.getString(5));
			}
		} catch (SQLException ex) {
			log.log(Level.SEVERE, SQL, ex);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//if translated then use the translate table name
		if (tr.IsTranslated && !Env.isBaseLanguage(language, tr.TableName)) {
			tr.TableName = tr.TableName + "_Trl";
		}
		return tr;
	}	//  getTableReference

	
	/**************************************************************************
	 * 	Load Data into PrintData
	 * 	@param pd print data with SQL and ColumnInfo set
	 *  @param format print format
	 */
	private void loadPrintData (PrintData pd, MPrintFormat format)
	{
		//	Translate Spool Output
		boolean translateSpool = pd.getTableName().equals("T_Spool");
		Msg.getMsg(format.getLanguage(), "RunningTotal");
		int rowNo = 0;
		PrintDataColumn pdc = null;
		boolean hasLevelNo = pd.hasLevelNo();
		int levelNo = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(pd.getSQL(), (Trx) null);
			rs = pstmt.executeQuery();
			//	Row Loop
			while (rs.next()) {
				if (hasLevelNo)
					levelNo = rs.getInt("LevelNo");
				else
					levelNo = 0;
				
				boolean groupChange = false;
				
				if(format.isSuppressDupGroupBy()) {
					if(rs.isFirst())
						groupChange= true;
				}
				
				
				//	Check Group Change ----------------------------------------
				if (m_group.getGroupColumnCount() > 1) {	//	one is GRANDTOTAL_
					// Find highest grouping column with a change
					int highestGrpColumn = -1;
					for (int i = pd.getColumnInfo().length-1; i >= 0; i--) {	//	backwards (leaset group first)
						PrintDataColumn group_pdc = pd.getColumnInfo()[i];
						if (!m_group.isGroupColumn(group_pdc.getColumnName()))
							continue;
						
						//	Group change
						if(m_group.checkChange(group_pdc.getColumnName(), rs.getObject(group_pdc.getAlias()))){
							highestGrpColumn = i;
							if(!groupChange)
								groupChange = true;
						} else if(groupChange)
							break;
						
					}
					
					if(highestGrpColumn >= 0){
						//	Check Columns for Function Columns
						for (int i = pd.getColumnInfo().length-1; i >= highestGrpColumn; i--) {	//	backwards (least group first)
							PrintDataColumn group_pdc = pd.getColumnInfo()[i];
							if (!m_group.isGroupColumn(group_pdc.getColumnName()))
								continue;
							
							//	Group change
							Object oldValue = m_group.getGroupColumnValue(group_pdc.getColumnName());
							Object newValue = rs.getObject(group_pdc.getAlias());
							m_group.setGroupColumnValue(group_pdc.getColumnName(), newValue);
							
							char[] functions = m_group.getFunctions(group_pdc.getColumnName());
							for (char element : functions) {
								printRunningTotal(pd, levelNo, rowNo++);
								pd.addRow(true, levelNo);
								//	get columns
								for (int c = 0; c < pd.getColumnInfo().length; c++) {
									pdc = pd.getColumnInfo()[c];
								//	log.fine("loadPrintData - PageBreak = " + pdc.isPageBreak());
									if (group_pdc.getColumnName().equals(pdc.getColumnName())) {
										//SR 10019983:Check for null value
										if (oldValue != null) {
											String valueString = oldValue.toString();
											if (oldValue instanceof Timestamp)
												valueString = DisplayType.getDateFormat(pdc.getDisplayType(), m_language).format(oldValue);
											if (format.getTableFormat().isPrintFunctionSymbols())		//	Translate Sum, etc.
												valueString	+= PrintDataFunction.getFunctionSymbol(element);
											pd.addNode(new PrintDataElement(pdc.getColumnName(),
												valueString, DisplayTypeConstants.String, false, pdc.isPageBreak()));
										}
									} else if (m_group.isFunctionColumn(pdc.getColumnName(), element)) {
										pd.addNode(new PrintDataElement(pdc.getColumnName(),
											m_group.getValue(group_pdc.getColumnName(), 
												pdc.getColumnName(), element), 
											PrintDataFunction.getFunctionDisplayType(element, pdc.getDisplayType()), 
												false, pdc.isPageBreak()));
									}
								}
								//	Reset Group Values
								for (int c = 0; c < pd.getColumnInfo().length; c++) {
									pdc = pd.getColumnInfo()[c];
									m_group.reset(group_pdc.getColumnName(), pdc.getColumnName());
								}
							}	//	Group change
						}	//	for all columns
					}	//	group change
				}
				
				//	new row ---------------------------------------------------
				printRunningTotal(pd, levelNo, rowNo++);
				
				if(!format.isTotalsOnly()) {
					pd.addRow(false, levelNo);
				}
				
				int counter = 1;
				//	get columns
				for (int i = 0; i < pd.getColumnInfo().length; i++) {
					pdc = pd.getColumnInfo()[i];
					PrintDataElement pde = null;
					//	Key Column - No DisplayColumn
					if (pdc.getAlias().equals(KEY)) {
						if (pdc.getColumnName().endsWith("_ID")) {
						//	int id = rs.getInt(pdc.getColumnIDName());
							int id = rs.getInt(counter++);
							if (!rs.wasNull()) {
								KeyNamePair pp = new KeyNamePair(id, KEY);	//	Key
								pde = new PrintDataElement(pdc.getColumnName(), pp, pdc.getDisplayType(), true, pdc.isPageBreak());
							}
						} else {
						//	String id = rs.getString(pdc.getColumnIDName());
							String id = rs.getString(counter++);
							if (!rs.wasNull()) {
								ValueNamePair pp = new ValueNamePair(id, KEY);	//	Key
								pde = new PrintDataElement(pdc.getColumnName(), pp, pdc.getDisplayType(), true, pdc.isPageBreak());
							}
						}
					} else {
						//	Non-Key Column
						//	Display and Value Column
						if (pdc.hasAlias()) {
							//	DisplayColumn first
							String display = rs.getString(counter++);
							if (pdc.getColumnName().endsWith("_ID")) {
								int id = rs.getInt(counter++);
								if (display != null && !rs.wasNull()) {
									KeyNamePair pp = new KeyNamePair(id, display);
									pde = new PrintDataElement(pdc.getColumnName(), pp, pdc.getDisplayType());
								}
							} else {
								String id = rs.getString(counter++);
								if (display != null && !rs.wasNull()) {
									ValueNamePair pp = new ValueNamePair(id, display);
									pde = new PrintDataElement(pdc.getColumnName(), pp, pdc.getDisplayType());
								}
							}
						} else {
							//	Display Value only
							//	Transformation for Booleans
							if (pdc.getDisplayType() == DisplayTypeConstants.YesNo) {
								String s = rs.getString(counter++);
								if (!rs.wasNull()) {
									boolean b = s.equals("Y");
									pde = new PrintDataElement(pdc.getColumnName(), new Boolean(b), pdc.getDisplayType());
								}
							} else if (pdc.getDisplayType() == DisplayTypeConstants.TextLong) {
								Clob clob = rs.getClob(counter++);
								String value = "";
								if (clob != null) {
									long length = clob.length();
									value = clob.getSubString(1, (int)length);
								}
								pde = new PrintDataElement(pdc.getColumnName(), value, pdc.getDisplayType());
							} else if (pdc.getDisplayType()== DisplayTypeConstants.DateTime) {
								Timestamp time = rs.getTimestamp(counter++);
								if (time != null)
									pde = new PrintDataElement(pdc.getColumnName(), time, pdc.getDisplayType());
							} else {
								//	The general case
								Object obj = rs.getObject(counter++);
								if (obj != null && obj instanceof String) {
									obj = ((String)obj).trim();
									if (((String)obj).length() == 0)
										obj = null;
								}
								if (obj != null) {
									//	Translate Spool Output
									if (translateSpool  && obj instanceof String) {
										String s = (String)obj;
										s = Msg.parseTranslation(pd.getCtx(), s);
										pde = new PrintDataElement(pdc.getColumnName(), s, pdc.getDisplayType());
									}
									else
										pde = new PrintDataElement(pdc.getColumnName(), obj, pdc.getDisplayType());
								}
							}
						}	//	Value only
					}	//	Non-Key Column
					if (pde != null) {
						if(!format.isTotalsOnly()
								&& (!format.isSuppressDupGroupBy() 
										|| groupChange 
										|| !m_group.isGroupColumn(pdc.getColumnName()))) {
							pd.addNode(pde);
							if ( pde.isNumeric() ) {
								pdc.setMinFractionDigits( pde.getValue(), pde.getDisplayType() );
							}
						}
						m_group.addValue(pde.getColumnName(), pde.getFunctionValue());
					}
				}	//	for all columns
			}	//	for all rows
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, pdc + " - " + e.getMessage() + "\nSQL=" + pd.getSQL());
		} finally { 
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	--	we have all rows - finish
		//	Check last Group Change
		if (m_group.getGroupColumnCount() > 1) {	//	one is TOTAL
			for (int i = pd.getColumnInfo().length-1; i >= 0; i--) {	//	backwards (leaset group first)
				PrintDataColumn group_pdc = pd.getColumnInfo()[i];
				if (!m_group.isGroupColumn(group_pdc.getColumnName()))
					continue;
				Object value = m_group.groupChange(group_pdc.getColumnName(), new Object());
				if (value != null) {	//	Group change
					char[] functions = m_group.getFunctions(group_pdc.getColumnName());
					for (char element : functions) {
						printRunningTotal(pd, levelNo, rowNo++);
						pd.addRow(true, levelNo);
						//	get columns
						for (int c = 0; c < pd.getColumnInfo().length; c++) {
							pdc = pd.getColumnInfo()[c];
							if (group_pdc.getColumnName().equals(pdc.getColumnName())) {
								String valueString = value.toString();
								if (value instanceof Timestamp)
									valueString = DisplayType.getDateFormat(pdc.getDisplayType(), m_language).format(value);
								if (format.getTableFormat().isPrintFunctionSymbols())		//	Translate Sum, etc.
									valueString	+= PrintDataFunction.getFunctionSymbol(element);
								pd.addNode(new PrintDataElement(pdc.getColumnName(),
									valueString, DisplayTypeConstants.String));
							} else if (m_group.isFunctionColumn(pdc.getColumnName(), element)) {
								pd.addNode(new PrintDataElement(pdc.getColumnName(),
									m_group.getValue(group_pdc.getColumnName(), 
										pdc.getColumnName(), element),
									PrintDataFunction.getFunctionDisplayType(element, pdc.getDisplayType())));
							}
						}
					}	//	for all functions
					//	No Need to Reset
				}	//	Group change
			}
		}	//	last group change

		//	Add Total Lines
		if (m_group.isGroupColumn(PrintDataGroup.TOTAL)) {
			char[] functions = m_group.getFunctions(PrintDataGroup.TOTAL);
			for (char element : functions) {
				printRunningTotal(pd, levelNo, rowNo++);
				pd.addRow(true, levelNo);
				//	get columns
				for (int c = 0; c < pd.getColumnInfo().length; c++) {
					pdc = pd.getColumnInfo()[c];
					if (c == 0) {		//	put Function in first Column
						String name = "";
						if (!format.getTableFormat().isPrintFunctionSymbols())		//	Translate Sum, etc.
							name = Msg.getMsg(format.getLanguage(), PrintDataFunction.getFunctionName(element));
						else
							name += PrintDataFunction.getFunctionSymbol(element);	//	Symbol
						pd.addNode(new PrintDataElement(pdc.getColumnName(), name.trim(), DisplayTypeConstants.String));
					} else if (m_group.isFunctionColumn(pdc.getColumnName(), element)) {
						pd.addNode(new PrintDataElement(pdc.getColumnName(),
							m_group.getValue(PrintDataGroup.TOTAL, 
								pdc.getColumnName(), element),
							PrintDataFunction.getFunctionDisplayType(element, pdc.getDisplayType())));
					}
				}	//	for all columns
			}	//	for all functions
			//	No Need to Reset
		}	//	TotalLine

		if (pd.getRowCount() == 0) {
			if (CLogMgt.isLevelFiner())
				log.warning("NO Rows - ms=" + (System.currentTimeMillis()-m_startTime) 
					+ " - " + pd.getSQL());
			else
				log.warning("NO Rows - ms=" + (System.currentTimeMillis()-m_startTime)); 
		} else
			log.info("Rows=" + pd.getRowCount()
				+ " - ms=" + (System.currentTimeMillis()-m_startTime));
	}	//	loadPrintData

	/**
	 * 	Print Running Total
	 *	@param pd Print Data to add lines to
	 *	@param levelNo level no
	 *	@param rowNo row no
	 */
	private void printRunningTotal (PrintData pd, int levelNo, int rowNo)
	{
		if (m_runningTotalLines < 1)	//	-1 = none
			return;
		log.fine("(" + m_runningTotalLines + ") - Row=" + rowNo 
			+ ", mod=" + rowNo % m_runningTotalLines);
		if (rowNo % m_runningTotalLines != 0)
			return;
			
		log.fine("Row=" + rowNo);
		PrintDataColumn pdc = null;
		int start = 0;
		if (rowNo == 0)	//	no page break on page 1
			start = 1;
		for (int rt = start; rt < 2; rt++) {
			pd.addRow (true, levelNo);
			//	get sum columns
			for (int c = 0; c < pd.getColumnInfo().length; c++) {
				pdc = pd.getColumnInfo()[c];
				if (c == 0) {
					String title = "RunningTotal";
					pd.addNode(new PrintDataElement(pdc.getColumnName(),
						title, DisplayTypeConstants.String, false, rt==0));		//	page break
				} else if (m_group.isFunctionColumn(pdc.getColumnName(), PrintDataFunction.F_SUM)) {
					pd.addNode(new PrintDataElement(pdc.getColumnName(),
						m_group.getValue(PrintDataGroup.TOTAL, pdc.getColumnName(), PrintDataFunction.F_SUM),
						PrintDataFunction.getFunctionDisplayType(PrintDataFunction.F_SUM, pdc.getDisplayType()), false, false));
				}
			}	//	for all sum columns
		}	//	 two lines
	}	//	printRunningTotal

	
	/*************************************************************************
	 * 	Test
	 * 	@param args args
	 */
	public static void main(String[] args)
	{
		org.compiere.Compiere.startup(true);

		new DataEngine(Language.getLanguage("de_DE"));
		Query query = new Query();
		query.addRestriction("AD_Table_ID", Query.LESS, 105);
	//	PrintData pd = de.load_fromTable(100, query, null, null, false);
	//	pd.dump();
	//	pd.createXML(new javax.xml.transform.stream.StreamResult(System.out));
	}
		
}	//	DataEngine

/**
 *	Table Reference Info
 */
class TableReference
{
	/** Table Name		*/
	public String 	TableName;
	/** Key Column		*/
	public String 	KeyColumn;
	/** Display Column	*/
	public String 	DisplayColumn;
	/** Displayed		*/
	public boolean 	IsValueDisplayed = false;
	/** Translated		*/
	public boolean	IsTranslated = false;
}	//	TableReference
