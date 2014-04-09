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
package org.compiere.framework;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.MWindow;
import org.compiere.model.X_M_Product;
import org.compiere.util.*;

/**
 *	Zoom Target identifier.
 *  Used in Zoom across (Where used) and Zoom into
 *	
 *  @author Jorg Janke
 *  @version $Id: ZoomTarget.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class ZoomTarget {

	/**	Static Logger	*/
	private static CLogger	log	= CLogger.getCLogger (ZoomTarget.class);

	/**
	 *  Parse String and add columnNames to the list.
	 *  String should be of the format ColumnName=<Value> AND ColumnName2=<Value2>
	 *  @param list list to be added to
	 *  @param parseString string to parse for variables
	 */
	public static void parseColumns (ArrayList<String> list, String parseString)
	{
		if (parseString == null || parseString.length() == 0)
			return;

		//	log.fine(parseString);
		String s = parseString;
		
		// Currently parsing algorithm does not handle parenthesis, IN clause or EXISTS clause
		if (s.contains(" EXISTS ") || s.contains(" IN ") || s.contains("(") || s.contains(")"))
				return;
		
		//  while we have columns
		while (s.indexOf("=") != -1)
		{
			int endIndex = s.indexOf("=");
			String clause = s.substring(0,endIndex);
			clause = clause.trim();
			
			int beginIndex = clause.lastIndexOf(' ', clause.length());
			String variable = clause.substring(beginIndex+1);
			
			if(variable.indexOf(".")!=-1)
			{
				beginIndex = variable.indexOf(".")+1;
				variable = variable.substring(beginIndex, variable.length());
			}
			
			if(!list.contains(variable))
				list.add(variable);
			
			s = s.substring(endIndex+1);
		}
	}   //  parseDepends

	/**
	 *  Evaluate where clause
	 *  @param columnValues columns with the values
	 *  @param whereClause where clause
	 *  @return true if where clause evaluates to true
	 */
	public static boolean evaluateWhereClause (ArrayList<ValueNamePair>	columnValues, String whereClause)
	{
		if(whereClause == null || whereClause.length()==0)
			return true;
		
		
		String s=whereClause;
		boolean result=true;

		// Currently parsing algorithm does not handle parenthesis, IN clause or EXISTS clause
		if (s.contains(" EXISTS ") || s.contains(" IN ") || s.contains("(") || s.contains(")"))
				return false;

		//  while we have variables
		while (s.indexOf("=") != -1)
		{
			int endIndex = s.indexOf("=");
			int beginIndex = s.lastIndexOf(' ', endIndex);
			
			String variable = s.substring(beginIndex+1, endIndex);
			String operand1="";
			String operand2="";
			String operator="=";
			
			if(variable.indexOf(".")!=-1)
			{
				beginIndex = variable.indexOf(".")+1;
				variable = variable.substring(beginIndex, variable.length());
			}
			
			for(int i=0; i<columnValues.size(); i++)
			{						
				if(variable.equals(columnValues.get(i).getName()))
				{
					operand1 = columnValues.get(i).getValue();
					break;
				}
			
			}

			s=s.substring(endIndex+1);
			beginIndex = 0;
			endIndex = s.indexOf(' ');
			if(endIndex==-1)
				operand2 = s.substring(beginIndex);
			else
				operand2=s.substring(beginIndex, endIndex);
			
			/* log.fine("operand1:"+operand1+ 
					" operator:"+ operator +
					" operand2:"+operand2); */
			if(!Evaluator.evaluateLogicTuple(operand1, operator, operand2))
			{
				result=false;
				break;
			}
		}

		return result;
	}
	
	/**
	 * 	Get the Zoom Into Target for a table.
	 *  
	 *  @param targetTableName for Target Table for zoom
	 *  @param curWindow_ID Window from where zoom is invoked
	 * 	@param targetWhereClause Where Clause in the format "Record_ID=<value>"
	 *  @param isSOTrx Sales contex of window from where zoom is invoked
	 */
	public static int getZoomAD_Window_ID (String targetTableName, int curWindow_ID, String targetWhereClause, boolean isSOTrx)
	{

		int zoomWindow_ID = 0;
		int PO_zoomWindow_ID = 0;

   	// Hard code for zooming to Production Resource Window		
		if (targetTableName.equals("M_Product"))
		{
			String ProductType = null;
			String ResourceGroup = null;
			if(targetWhereClause!=null && !targetWhereClause.equals(""))
			{
				String sql1 = " SELECT ProductType, ResourceGroup FROM M_Product "
					        +" WHERE " + targetWhereClause;
		
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				try
				{
					pstmt = DB.prepareStatement(sql1, (Trx) null);
					rs = pstmt.executeQuery();
					if (rs.next())
					{
						ProductType = rs.getString(1);
						ResourceGroup = rs.getString(2);
					}
				}
				catch (SQLException e) {
					log.log(Level.SEVERE, sql1, e);
				}
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}

				if(ProductType != null && ResourceGroup != null &&
						ProductType.equals(X_M_Product.PRODUCTTYPE_Resource)&& 
						(ResourceGroup.equals(X_M_Product.RESOURCEGROUP_Person) ||
								ResourceGroup.equals(X_M_Product.RESOURCEGROUP_Equipment)))
				{
					int windowID = 0;
				    String sql11 = " SELECT AD_Window_ID FROM AD_Window WHERE NAME LIKE 'Production Resource'";
				    try
				    {
				    	pstmt = DB.prepareStatement(sql11, (Trx) null);
				    	rs = pstmt.executeQuery();
				    	while(rs.next())
				    	{
				    		windowID= rs.getInt(1);
				    	}
				    }
				    catch (SQLException e) {
				    	log.log(Level.SEVERE, sql11, e);
				    }
					finally {
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}

				    if(windowID !=0)
				    	return windowID;
				}
				
			}
		}
		
		// END hard code for Zooming into Production resource.
		
		// Find windows where the first tab is based on the table
		String sql = "SELECT DISTINCT AD_Window_ID, PO_Window_ID "
			+ "FROM AD_Table t "
			+ "WHERE TableName = ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			int index = 1;
			pstmt.setString (index++, targetTableName);
			rs = pstmt.executeQuery();
	
			if (rs.next())
			{
				zoomWindow_ID= rs.getInt(1);
				PO_zoomWindow_ID = rs.getInt(2);
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if(PO_zoomWindow_ID == 0)
			return zoomWindow_ID;

       if(PO_zoomWindow_ID !=0 && zoomWindow_ID !=0 && targetWhereClause !=null && targetWhereClause.length()!=0)
       {
			String ParentTable = null;
			int ind = targetTableName.indexOf("Line");
			if(ind != -1)
				ParentTable = targetTableName.substring(0, ind);			
			if(ParentTable != null)
			{
				StringBuffer sql3 = new StringBuffer(" SELECT p.IsSOTrx FROM ");
				sql3.append(ParentTable).append(" p, ").append(targetTableName).append(" c ");
				sql3.append(" WHERE ").append(targetWhereClause);
				sql3.append(" AND p.").append(ParentTable).append("_ID = c.").append(ParentTable).append("_ID");
				try
				{
					pstmt = DB.prepareStatement(sql3.toString(), (Trx) null);
					rs = pstmt.executeQuery();
			
					if (rs.next())
						isSOTrx = rs.getString(1).equals("Y");
				}
				catch (SQLException e) {
					log.log(Level.SEVERE, sql3.toString(), e);
				}
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
			}
       }
		
		int AD_Window_ID=0;
		
		if(targetWhereClause!=null && targetWhereClause.length() !=0)
		{
			ArrayList<KeyNamePair>	zoomList = new ArrayList<KeyNamePair>();
			zoomList= ZoomTarget.getZoomTargets(targetTableName, curWindow_ID, targetWhereClause);
			if(zoomList != null && zoomList.size()>0)
				AD_Window_ID=zoomList.get(0).getKey();
		}
		
		if (AD_Window_ID != 0)
			return AD_Window_ID;
	
		if(isSOTrx)
			return zoomWindow_ID;
			
		return PO_zoomWindow_ID;

	}

	/**
	 * 	Get the Zoom Across Targets for a table.
	 *  
	 *  @param targetTableName for Target Table for zoom
	 *  @param curWindow_ID Window from where zoom is invoked
	 * 	@param targetWhereClause Where Clause in the format "WHERE Record_ID=?"
	 *  @param params[] parameter to whereClause. Should be the Record_ID
	 */
	public static ArrayList<KeyNamePair> getZoomTargets (String targetTableName, int curWindow_ID, String targetWhereClause, Object[] params)
	{
		if (params.length != 1)
			return null;
		
		Integer record_ID = (Integer) params[0];
		String whereClause = targetWhereClause.replace("?", record_ID.toString());
		whereClause = whereClause.replace("WHERE ", " ");
		
		log.fine("WhereClause : " + whereClause);
		return getZoomTargets(targetTableName, curWindow_ID, whereClause);

	}
	
	/**
	 * 	Get the Zoom Across Targets for a table.
	 *  
	 *  @param targetTableName for Target Table for zoom
	 *  @param curWindow_ID Window from where zoom is invoked
	 * 	@param targetWhereClause Where Clause in the format "Record_ID=<value>"
	 */
	public static ArrayList<KeyNamePair> getZoomTargets (String targetTableName, int curWindow_ID, String targetWhereClause)
	{
		/**
		 * 	Window WhereClause 
		 */
		class WindowWhereClause
		{
			/**
			 * 	Org Access constructor
			 *	@param ad_Window_ID window
			 *	@param name Window Name
			 *	@param where Where Clause on the first tab of the window 
			 */
			public WindowWhereClause (int ad_Window_ID, String name, String where)
			{
				this.AD_Window_ID = ad_Window_ID;
				this.windowName = name;
				this.whereClause = where;
			}
			/** Window				*/
			public int AD_Window_ID = 0;
			/** Window Name			*/
			public String windowName = "";
			/** Window Where Clause	*/
			public String whereClause = "";
			
			
			/**
			 * 	Extended String Representation
			 *	@return extended info
			 */
			@Override
			public String toString ()
			{
				StringBuffer sb = new StringBuffer();
				sb.append(Msg.translate(Env.getCtx(), "AD_Window_ID")).append("=")
					.append(windowName).append(" - ")
					.append(whereClause);
				return sb.toString();
			}	//	toString

		}	//	WindowWhereClause

		/**	The Option List					*/
		ArrayList<KeyNamePair>	zoomList = new ArrayList<KeyNamePair>();
		ArrayList<WindowWhereClause> windowList = new ArrayList<WindowWhereClause> ();
		ArrayList<String> columns = new ArrayList<String>();
		int zoom_Window_ID = 0;
		int PO_Window_ID=0;
		String zoom_WindowName = "";
		String whereClause = "";
		boolean windowFound = false;

		/** Start Hard code for adding Maintain cost windows in the zoom across for product and work center */
		String WorkCenterCostWindowName = null;
	    int WorkCenterWindowID = 0;
	    int WorkCenterCostWindowID = 0;
	    int ProductionResourceWindowID = 0;
    
	    String sql11 = " SELECT AD_Window_ID, Name FROM AD_Window WHERE Name LIKE 'Work Center%' "
	    		     + " OR NAME LIKE 'Production Resource'";
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql11, (Trx) null);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				int windowID = rs.getInt(1);
				String windowName = rs.getString(2);
				if(windowName.equals("Work Center"))
				{
					WorkCenterWindowID = windowID;
				}
				else if(windowName.equals("Work Center Costs"))
				{
					WorkCenterCostWindowID = windowID;
					WorkCenterCostWindowName = windowName;
				}
				else if (windowName.equals("Production Resource"))
				{
					ProductionResourceWindowID = windowID;
				}
					
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql11, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	    
	 	MWindow costWindow = MWindow.get(Env.getCtx(), 344);
	    KeyNamePair cost = new KeyNamePair(costWindow.getAD_Window_ID(),costWindow.getName());
	    KeyNamePair WcCost = new KeyNamePair(WorkCenterCostWindowID,WorkCenterCostWindowName);

    	/** End Hard Code for product and work center window */
		
		// Find windows where the first tab is based on the table
		String sql = "SELECT DISTINCT w.AD_Window_ID, w.Name, tt.WhereClause, t.TableName, " +
				"wp.AD_Window_ID, wp.Name, ws.AD_Window_ID, ws.Name "
			+ "FROM AD_Table t "
			+ "INNER JOIN AD_Tab tt ON (tt.AD_Table_ID = t.AD_Table_ID) ";
		boolean baseLanguage = Env.isBaseLanguage(Env.getCtx(), "AD_Window"); 
		if (baseLanguage)
		{
			sql += "INNER JOIN AD_Window w ON (tt.AD_Window_ID=w.AD_Window_ID)";
			sql += " LEFT OUTER JOIN AD_Window ws ON (t.AD_Window_ID=ws.AD_Window_ID)"
				+  " LEFT OUTER JOIN AD_Window wp ON (t.PO_Window_ID=wp.AD_Window_ID)";
		}
		else
		{
			sql += "INNER JOIN AD_Window_Trl w ON (tt.AD_Window_ID=w.AD_Window_ID AND w.AD_Language=?)";
			sql += " LEFT OUTER JOIN AD_Window_Trl ws ON (t.AD_Window_ID=ws.AD_Window_ID AND ws.AD_Language=?)"
				+  " LEFT OUTER JOIN AD_Window_Trl wp ON (t.PO_Window_ID=wp.AD_Window_ID AND wp.AD_Language=?)";
		}
		sql	+= "WHERE t.TableName = ?"
			+ " AND w.AD_Window_ID <> ? AND w.isActive='Y'"
			+ " AND tt.SeqNo=10"
			+ " AND (wp.AD_Window_ID IS NOT NULL "
					+ "OR EXISTS (SELECT 1 FROM AD_Tab tt2 WHERE tt2.AD_Window_ID = ws.AD_Window_ID AND tt2.AD_Table_ID=t.AD_Table_ID AND tt2.SeqNo=10))"
			+ " ORDER BY 2";
	
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			int index = 1;
			if (!baseLanguage)
			{
				pstmt.setString (index++, Env.getAD_Language(Env.getCtx()));
				pstmt.setString (index++, Env.getAD_Language(Env.getCtx()));
				pstmt.setString (index++, Env.getAD_Language(Env.getCtx()));
			}
			pstmt.setString (index++, targetTableName);
			pstmt.setInt (index++, curWindow_ID);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				windowFound = true;
				zoom_Window_ID= rs.getInt(7);
				zoom_WindowName = rs.getString(8);
				PO_Window_ID = rs.getInt(5);
				whereClause= rs.getString(3);
				
				// Multiple window support only for Order, Invoice, Shipment/Receipt which have PO windows
				if (PO_Window_ID == 0)
					break;

				WindowWhereClause windowClause = new WindowWhereClause (rs.getInt(1), rs.getString(2), whereClause);
				windowList.add(windowClause);
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}


	
		String sql1="";
		
		if (!windowFound || (windowList.size() <=1 && zoom_Window_ID == 0))
			return zoomList;
		
		//If there is a single window for the table, no parsing is necessary
		if(windowList.size() <= 1)
		{
			
			//Check if record exists in target table
			sql1 = "SELECT count(*) FROM " +targetTableName + " WHERE "
						+ targetWhereClause;
			if(whereClause != null && whereClause.length() !=0)
				sql1 += " AND " + Evaluator.replaceVariables(whereClause,Env.getCtx(), null);
			
		}
		else if (windowList.size() > 1)
		{
			// Get the columns used in the whereClause
			for (int i=0; i< windowList.size();i++)
				parseColumns(columns,windowList.get(i).whereClause);				
	
			// Get the distinct values of the columns from the table if record exists
			sql1 = "SELECT DISTINCT ";
			for(int i=0; i<columns.size();i++)
			{
				if(i!=0)
					sql1 +=",";
				sql1 += columns.get(i);
			}
			
			if(columns.size()==0)
				sql1 += "count(*) ";
			sql1 += " FROM " +targetTableName + " WHERE "
					+ targetWhereClause;
		}
	
		
		log.fine(sql1);

		ArrayList<ValueNamePair>	columnValues = new ArrayList<ValueNamePair>();		
		try
		{
			pstmt = DB.prepareStatement(sql1, (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				if (columns.size() > 0)
				{
					columnValues.clear();
					for(int i=0; i<columns.size();i++)
					{
						String columnName = columns.get(i);
						Class<?> clazz = rs.getObject(columnName).getClass();
						String columnValue ="";
						
						if (clazz == Integer.class)
							columnValue = String.valueOf(rs.getInt(columnName));
						else if (clazz == BigDecimal.class)
							columnValue = String.valueOf(rs.getDouble(columnName));
						else 
							columnValue = "'"+(String)rs.getString(columnName)+"'";
						
						log.fine(columnName + " = "+columnValue);
						columnValues.add(new ValueNamePair(columnValue,columnName));
					}
					
					// Find matching windows
					for (int i=0; i<windowList.size(); i++)
					{
						log.fine("Window : "+windowList.get(i).windowName + " WhereClause : " + windowList.get(i).whereClause);
						if(evaluateWhereClause(columnValues,windowList.get(i).whereClause))
						{
							log.fine("MatchFound : "+windowList.get(i).windowName );
							KeyNamePair pp = new KeyNamePair (windowList.get(i).AD_Window_ID, windowList.get(i).windowName);
							zoomList.add(pp);
							// Use first window found. Ideally there should be just one matching
							break;
						}
					}
				}
				else
				{
					int rowCount = rs.getInt(1);
					if(rowCount != 0)
					{
						KeyNamePair pp = new KeyNamePair (zoom_Window_ID, zoom_WindowName);
						zoomList.add(pp);
					}
				}
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql1, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// Add the windows for Product Production resource and work center.
		if(curWindow_ID == 140)  // Product window
			zoomList.add(cost);
		if(curWindow_ID == WorkCenterWindowID) // Work Center Window
			zoomList.add(WcCost);
		if(curWindow_ID == ProductionResourceWindowID) // Production Resource
			zoomList.add(cost);		
		return zoomList;

	}// getZoomTargets 


}// ZoomTarget
