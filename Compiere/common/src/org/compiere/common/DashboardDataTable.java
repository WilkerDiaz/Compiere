package org.compiere.common;

import java.io.Serializable;
import java.util.ArrayList;

import org.compiere.util.CLoggerSimple;
import org.compiere.util.CompiereLogger;
import org.compiere.vos.DashboardFormatVO;

public class DashboardDataTable implements Serializable{
	
	static public class DashboardRowData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private ArrayList<String> values = new ArrayList<String>();
		private String countryCode;
		
		/* Row restriction */
		private QueryRestrictionVO restriction;
		
		public ArrayList<String> getValues() {
			return values;
		}

		public void setValues(ArrayList<String> values) {
			this.values = values;
		}

		public String getCountryCode() {
			return countryCode;
		}

		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}

		public QueryRestrictionVO getRestriction() {
			return restriction;
		}

		public void setRestriction(QueryRestrictionVO restriction) {
			this.restriction = restriction;
		}

	}

	static public class DashboardColumnData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String label;
		private DashboardFormatVO.Dimension.ColumnType ColumnType;
		
		/* Column restriction */
		private QueryRestrictionVO restriction;
		
		
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public DashboardFormatVO.Dimension.ColumnType getColumnType() {
			return ColumnType;
		}

		public void setColumnType(DashboardFormatVO.Dimension.ColumnType columnType) {
			ColumnType = columnType;
		}

		public QueryRestrictionVO getRestriction() {
			return restriction;
		}

		public void setRestriction(QueryRestrictionVO restriction) {
			this.restriction = restriction;
		}

		public DashboardColumnData() {
		}
		
		public DashboardColumnData(DashboardFormatVO.Dimension.ColumnType ColumnType, String label) {
			this.ColumnType = ColumnType;
			this.label = label;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final CompiereLogger	log = new CLoggerSimple(TableModel.class);
	protected ArrayList<DashboardRowData> rows = new ArrayList<DashboardRowData>();
	protected ArrayList<DashboardColumnData> columns = new ArrayList<DashboardColumnData>();
	public int AD_Window_ID=0;
	public String WindowName;
	public String tableName;
	public String baseTableName;
	public String baseKeyColumnName;
	public String whereClause;
	public ArrayList<QueryRestrictionVO> parameterRestrictions;
	
	public DashboardDataTable() {
	}
	
	public void setAD_Window_ID(int AD_Window_ID) {
		this.AD_Window_ID = AD_Window_ID;
	}
	
	public void setWindowName(String WindowName) {
		this.WindowName = WindowName;
	}
	
	public String get(int row, int col) {
		if(rows.size() > row && row >= 0){
			return rows.get(row).values.get(col);
		}else{
			return null;
		}
	}
	
	public int getRowIndex (int col, String value) {
		for(int i=0; i<rows.size(); i++) {
			DashboardRowData row = rows.get(i);
			if(row.values.get(col).equals(value)){
				return i;
			}
		}
		return -1;
	}
	
	public void set(int row, int col, String v) {
		DashboardRowData dataRow = rows.get(row);
		for(int i=dataRow.values.size(); i<=col; i++)
			dataRow.values.add(i,"0");
		dataRow.values.set(col, v);
	}
	
	public void setRowRestriction (int row, QueryRestrictionVO restriction) {
		rows.get(row).restriction = restriction;
	}
	
	public void setRowCountryCode (int row, String countryCode) {
		rows.get(row).countryCode = countryCode;
	}


	public void addRow() {
		DashboardRowData row = new DashboardRowData();
		rows.add(row);
	}
	
	public void addColumn(DashboardFormatVO.Dimension.ColumnType ColumnType, String label) {
		DashboardColumnData column = new DashboardColumnData();
		column.ColumnType = ColumnType;
		column.label = label;
		columns.add(column);
	}

	public void addColumn(DashboardColumnData column) {
		columns.add(column);
	}
	public int getColumnIndex(String Label) {
		for(int i=0; i<columns.size(); i++)
			if(columns.get(i).label.equals(Label))
				return i;
		
		return -1;
	}
	
	public ArrayList<DashboardColumnData> getColumns() {
		return columns;
	}

	public ArrayList<DashboardRowData> getRows() {
		return rows;
	}
	
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getWhereClause() {
		return whereClause;
	}
	
	public void setParameterRestrictions(ArrayList<QueryRestrictionVO> restrictions) {
		this.parameterRestrictions = restrictions;
	}
	
	public ArrayList<QueryRestrictionVO> getParameterRestrictions() {
		return parameterRestrictions;
	}

}
