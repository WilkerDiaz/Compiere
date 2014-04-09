package org.compiere.vos;

import java.io.Serializable;

import org.compiere.common.DashboardDataTable;

public class DashboardDataVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DashboardFormatVO m_dFormat_VO;
	private DashboardDataTable m_dataTable;
	private String m_error;
	private int AD_RoleWidget_ID;
	private int AD_Menu_ID;
	
	public DashboardDataVO() {
	}
	
	public DashboardFormatVO getDashboardFormatVO () {
		return m_dFormat_VO;
	}
	
	public void setDashboardFormatVO (DashboardFormatVO df_VO) {
		this.m_dFormat_VO = df_VO;
	}

	public void setError(String msg) {
		m_error = msg;
	}
	
	public String getError() {
		return m_error;
	}

	public void setDataTable(DashboardDataTable dataTable) {
		m_dataTable = dataTable;
	}
	
	public DashboardDataTable getDataTable() {
		return m_dataTable;
	}
	
	public void setAD_RoleWidget_ID (int AD_RoleWidget_ID) {
		this.AD_RoleWidget_ID = AD_RoleWidget_ID; 
	}
	
	public int getAD_RoleWidget_ID() {
		return this.AD_RoleWidget_ID;
	}

	public void setAD_Menu_ID (int AD_Menu_ID) {
		this.AD_Menu_ID = AD_Menu_ID; 
	}
	
	public int getAD_Menu_ID() {
		return this.AD_Menu_ID;
	}

}
