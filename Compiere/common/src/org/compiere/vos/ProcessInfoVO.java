/**
 * 
 */
package org.compiere.vos;

import java.io.*;
import java.util.*;

/**
 * @author gwu
 *
 */
public class ProcessInfoVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int AD_Process_ID;
	public String Name;
	public String Description;
	public String Help;
	public boolean IsReport;
	public boolean IsDirectPrint;
	public boolean IsDashboard;
	public String DashboardType;
	public String Form;
	public String Style;
	public String DataPoint;
	public String DataLabel;
	public int AD_ReportView_ID;
	public int AD_Workflow_ID;
	public boolean IsServerProcess;
	public String ProcedureName;
	public String ClassName;
	
	public ArrayList<FieldVO> Parameters;
	public int AD_PInstance_ID;
	public String Summary;
	public boolean isError;
	public String FileName;
	public String logInfo;
	public int[] IDs;
	public int AD_BView_ID=0;
	public int AD_ReportTemplate_ID=0;
	public boolean IsSOTrx=true;	
	public ArrayList<String> files;
}
