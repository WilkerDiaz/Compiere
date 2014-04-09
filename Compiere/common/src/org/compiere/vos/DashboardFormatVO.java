package org.compiere.vos;

import java.io.Serializable;
import java.util.ArrayList;

public class DashboardFormatVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name, description, isActive, dashboardType, style, form, dataPoint, dataLabel;
	
	private int AD_DashboardFormat_ID=0, AD_ReportView_ID=0, AD_Client_ID=-1;
	
	private int width=500, height=300;
	
	private String region=null;
	
	/* Dashboard dimensions */
	private ArrayList<org.compiere.vos.DashboardFormatVO.Dimension> dimensions;
	
	public ArrayList<org.compiere.vos.DashboardFormatVO.Dimension> getDimensions() {
		return dimensions;
	}

	public void setDimensions(
			ArrayList<org.compiere.vos.DashboardFormatVO.Dimension> dimensions) {
		this.dimensions = dimensions;
	}

	public DashboardFormatVO() {
		dimensions = new ArrayList<Dimension>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	public String getDashboardType() {
		return dashboardType;
	}

	public void setDashboardType(String dashboardType) {
		this.dashboardType = dashboardType;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getDataPoint() {
		return dataPoint;
	}

	public void setDataPoint(String dataPoint) {
		this.dataPoint = dataPoint;
	}

	public String getDataLabel() {
		return dataLabel;
	}

	public void setDataLabel(String dataLabel) {
		this.dataLabel = dataLabel;
	}

	public int getAD_Client_ID() {
		return AD_Client_ID;
	}

	public void setAD_Client_ID(int AD_Client_ID) {
		this.AD_Client_ID = AD_Client_ID;
	}

	public int getAD_DashboardFormat_ID() {
		return AD_DashboardFormat_ID;
	}

	public void setAD_DashboardFormat_ID(int AD_DashboardFormat_ID) {
		this.AD_DashboardFormat_ID = AD_DashboardFormat_ID;
	}


	public int getAD_ReportView_ID() {
		return AD_ReportView_ID;
	}

	public void setAD_ReportView_ID(int AD_ReportView_ID) {
		this.AD_ReportView_ID = AD_ReportView_ID;
	}
	
	public String getRegion() {
		return region ;
	}

	public void setRegion(String region) {		
		this.region = region;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width){
		this.width = width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public void addDimension(int AD_Column_ID, String Name, String ColumnName, Dimension.ColumnType ColumnType, int AD_Reference_ID, int AD_Reference_Value_ID, boolean isGroupBy){
		dimensions.add(new Dimension(AD_Column_ID, Name, ColumnName, ColumnType, AD_Reference_ID, AD_Reference_Value_ID, isGroupBy));
	}
	
	public static class Dimension implements Serializable{

		/**
		  * The type of a column.
		  */
		public enum ColumnType {
		    BOOLEAN, DATE, DATETIME, NUMBER, STRING, TIMEOFDAY;
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/* Column ID */
		int AD_Column_ID = 0;
		 
		/* Name */
		private String Name;
		
		/* Column Name */
		private String ColumnName;
		
		/* Dashboard dimensions - ColumnTypes */
		private ColumnType columnType;

		private boolean isGroupBy = true;
		
		public ArrayList<String> Categories;
		
		/* Dashboard dimensions - Column Reference */
		private int AD_Reference_ID;

		private int AD_Reference_Value_ID;

		public Dimension() {
		}
		
		public Dimension(int AD_Column_ID, String Name, String ColumnName, ColumnType ColumnType, int AD_Reference_ID, int AD_Reference_Value_ID, boolean isGroupBy){
			this.AD_Column_ID = AD_Column_ID;
			this.Name = Name;
			this.ColumnName = ColumnName;
			this.columnType = ColumnType;
			this.AD_Reference_ID = AD_Reference_ID;
			this.AD_Reference_Value_ID = AD_Reference_Value_ID;
			this.isGroupBy = isGroupBy;
			this.Categories = new ArrayList<String>();
		}
		
		public int getAD_Column_ID() {
			return AD_Column_ID;
		}
		
		public String getColumnName()	{
			return ColumnName;
		}
		
		public String getName()	{
			return Name;
		}
		
		public ColumnType getColumnType() {
			return columnType;
		}
		
		public int getAD_Reference_ID() {
			return AD_Reference_ID;
		}

		public int getAD_Reference_Value_ID() {
			return AD_Reference_Value_ID;
		}
		
		public boolean contains(String category) {
			return Categories.contains(category);
		}
		
		public void addCategory(String category) {
			if(!Categories.contains(category))
				Categories.add(category);
		}
		
		public boolean isGroupBy() {
			return isGroupBy;
		}
	}
}
