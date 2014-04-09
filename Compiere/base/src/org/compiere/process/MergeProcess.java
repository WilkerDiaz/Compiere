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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Merge Process
 *	
 *  @author Jorg Janke
 *  @version $Id: MergeProcess.java $
 */
public class MergeProcess extends SvrProcess
{
	/**	Merge Parameter		*/
	private int from_ID = 0;
	private int to_ID = 0;
	
	static private String	AD_ORG_ID = "AD_Org_ID";
	static private String	C_BPARTNER_ID = "C_BPartner_ID";
	static private String	AD_USER_ID = "AD_User_ID";
	static private String	M_PRODUCT_ID = "M_Product_ID";
	
	private String columnName = null;
	
	/** Tables to delete (not update) for AD_Org	*/
	static private String[]	s_delete_Org = new String[]
		{"AD_OrgInfo", "AD_Role_OrgAccess"};
	/** Tables to delete (not update) for AD_User	*/
	static private String[]	s_delete_User = new String[]
		{"AD_User_Roles"};
	/** Tables to delete (not update) for C_BPartner	*/
	static private String[]	s_delete_BPartner = new String[]
		{"C_BP_Employee_Acct", "C_BP_Vendor_Acct", "C_BP_Customer_Acct", 
		"T_Aging"};
	/** Tables to delete (not update) for M_Product		*/
	static private String[]	s_delete_Product = new String[]
		{"M_Product_PO", "M_Replenish", "T_Replenish", 
		"M_ProductPrice", "M_Product_Costing",
		"M_Product_Trl", "M_Product_Acct", "M_Cost"};		
	/**	Total Count			*/
	private int				m_totalCount = 0;
	/** Error Log			*/
	private StringBuffer	m_errorLog = new StringBuffer();
	/**	Connection			*/
	private Connection		m_con = null;
	/**	Logger			*/
	
	private String[]	m_deleteTables = null;
	private String entity = null;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private String fromValue = null;
	private String toValue = null;
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();

		for (int i = 0; (i < para.length); i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Org_ID")){				
				from_ID = ((BigDecimal)para[i].getParameter()).intValue();				
			}				
			else if (name.equals("AD_Org_To_ID")){
				to_ID = ((BigDecimal)para[i].getParameter()).intValue();
				columnName = AD_ORG_ID;
				m_deleteTables = s_delete_Org;
				entity = "Organization";
			}
			else if (name.equals("AD_User_ID")){
				from_ID = ((BigDecimal)para[i].getParameter()).intValue();					
			}				
			else if (name.equals("AD_User_To_ID")){
				to_ID = ((BigDecimal)para[i].getParameter()).intValue();
				m_deleteTables = s_delete_User;
				columnName = AD_USER_ID;
				entity = "User";
			}
			else if (name.equals("C_BPartner_ID")){
				from_ID = ((BigDecimal)para[i].getParameter()).intValue();					
			}				
			else if (name.equals("C_BPartner_To_ID")){
				to_ID = ((BigDecimal)para[i].getParameter()).intValue();
				m_deleteTables = s_delete_BPartner;
				columnName = C_BPARTNER_ID;
				entity = "BusPartner";
			}			
			else if (name.equals("M_Product_ID")){
				from_ID = ((BigDecimal)para[i].getParameter()).intValue();					
			}				
			else if (name.equals("M_Product_To_ID")){
				to_ID = ((BigDecimal)para[i].getParameter()).intValue();
				m_deleteTables = s_delete_Product;
				columnName = M_PRODUCT_ID;
				entity="Product";
			}			
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{		
		log.info ("doIt ");
		
		if (!preMerge(columnName, from_ID, to_ID))
			return m_errorLog.toString();
		
		String msg =   Msg.translate( getCtx(), "MergeFrom") + " = " + fromValue
		+ "\n" + Msg.translate(getCtx(), "MergeTo") + " = " + toValue +"\n";
		boolean success = merge (columnName, from_ID, to_ID);
		postMerge(columnName, to_ID);
			
		if (success){
			return msg +" #"+m_totalCount;
		}
		else{
			throw new CompiereSystemException(" "+m_errorLog.toString());
		}		
	}	//	doIt
	
	private boolean preMerge(String ColumnName,int from_ID, int to_ID){
		 
		
		if (from_ID < 0) throw new IllegalArgumentException(getTranslatedMessage("Invalid")+" "+getTranslatedMessage("From")+" "+getTranslatedMessage("Parameter"));
		if (to_ID < 0 ) throw new IllegalArgumentException(getTranslatedMessage("Invalid")+" "+getTranslatedMessage("To")+" "+getTranslatedMessage("Parameter"));
		
		if (from_ID == to_ID){
			String entityMessage=Msg.getMsg(getCtx(), entity).replace("&", "");
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), "MergeInvalidParameter",new Object[]{entityMessage,entityMessage}));
		}			
		
		String fromSql = " SELECT Name FROM "+columnName.substring(0, columnName.length()-3)+" WHERE "+columnName+" = ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{			
			pstmt = DB.prepareStatement(fromSql, get_Trx());
			pstmt.setString(1, String.valueOf(from_ID));
			
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				fromValue = rs.getString(1);				
			}
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, columnName, ex);
			return false;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		String toSql = " SELECT Name FROM "+columnName.substring(0, columnName.length()-3)+" WHERE "+columnName+" =  ?";
		pstmt = null;
		
		try
		{		
			pstmt = DB.prepareStatement(toSql, get_Trx());
			pstmt.setString(1, String.valueOf(to_ID));
			
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				toValue = rs.getString(1);				
			}
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, columnName, ex);
			return false;
		}	
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		if (columnName.equals(M_PRODUCT_ID)){
			try {
				String sql="SELECT 1 FROM M_StorageDetail WHERE M_Product_ID = ? AND M_AttributeSetInstance_ID = 0 " + 
				           "AND (Qty > 0) AND (QtyType='A' OR QtyType='O' OR QtyType='R') ";
				pstmt = DB.prepareStatement(sql,get_Trx());
				pstmt.setInt(1, from_ID);
				rs = pstmt.executeQuery();
				
				if (rs.next()){
					m_errorLog.append(getTranslatedMessage("MergeProductAllocated"));
					return false;
				}
				rs.close();
				pstmt.close();
				pstmt = null;
				
				sql = "SELECT 1 FROM Fact_Acct WHERE M_Product_ID = ?  ";				
				pstmt = DB.prepareStatement(sql,get_Trx());
				pstmt.setInt(1, from_ID);
				rs = pstmt.executeQuery();
				
				if (rs.next()){
					m_errorLog.append(Msg.getMsg(getCtx(), "MergeAccountingTransactionsPosted",new Object[]{entity,fromValue}));					
					return false;
				}
			} 
			catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}		
		return true;
	}

	private boolean merge (String ColumnName, int from_ID, int to_ID)
	{
		String TableName = ColumnName.substring(0, ColumnName.length()-3);
		log.config(ColumnName
			+ " - From=" + from_ID + ",To=" + to_ID);

		boolean success = true;
		m_totalCount = 0;
		m_errorLog = new StringBuffer();
		String sql = "SELECT t.TableName, c.ColumnName "
			+ "FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
			+ "WHERE t.IsView='N'"
				+ " AND c.ColumnSQL IS NULL "
				+ " AND t.TableName NOT IN ('C_TaxDeclarationAcct')"
				+ " AND ("
				+ "(c.ColumnName=? AND c.IsKey='N')"		//	#1 - direct
			+ " OR "
				+ "c.AD_Reference_Value_ID IN "				//	Table Reference
					+ "(SELECT rt.AD_Reference_ID FROM AD_Ref_Table rt"
					+ " INNER JOIN AD_Column cc ON (rt.AD_Table_ID=cc.AD_Table_ID AND rt.Column_Key_ID=cc.AD_Column_ID) "
					+ "WHERE cc.IsKey='Y' AND cc.ColumnName=?)"	//	#2
			+ ") "
			+ "ORDER BY t.LoadSeq DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Savepoint sp = null;
		try
		{
			m_con = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
			sp = m_con.setSavepoint("merge");
			//
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setString(1, ColumnName);
			pstmt.setString(2, ColumnName);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String tName = rs.getString(1);
				String cName = rs.getString(2);
				if (!TableName.equals(tName))	//	to be sure - sql should prevent it
				{
					int count = mergeTable (tName, cName, from_ID, to_ID);
					if (count < 0)
						success = false;
					else
						m_totalCount += count;
				}
			}
			rs.close();
			pstmt.close();
			pstmt = null;
			//
			log.config("Success=" + success
				+ " - " + ColumnName + " - From=" + from_ID + ",To=" + to_ID);
			if (success)
			{
				//SR10018367: Cannot delete Organization, instead make Inactive
				if (TableName.equals("AD_Org"))
					sql = "UPDATE "+TableName+" SET IsActive = 'N' WHERE " + ColumnName + "=" + from_ID;
				else
					sql = "DELETE FROM " + TableName + " WHERE " + ColumnName + "=" + from_ID;
				Statement stmt = m_con.createStatement();
				int count = 0;
				try
				{
					count = stmt.executeUpdate (sql);
					if (count != 1)
					{
						m_errorLog.append(Env.NL).append(sql)
							.append(" - Count=").append(count);
						success = false;
					}
				}
				catch (SQLException ex1)
				{
					m_errorLog.append(Env.NL).append(sql)
						.append(" - ").append(ex1.toString());
					success = false;
				}
				stmt.close();
				stmt = null;
			}
			//
			if (success)
				m_con.commit();
			else
				m_con.rollback(sp);
			m_con.close();
			m_con = null;
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, ColumnName, ex);
		}
		//	Cleanup
		try
		{
			if (pstmt != null)
				pstmt.close();
			if (m_con != null)
				m_con.close();
		}
		catch (Exception ex)
		{
		}
		pstmt = null;
		m_con = null;
		//
		return success;
	}	//	merge
	
	private int mergeTable (String TableName, String ColumnName, int from_ID, int to_ID)
	{
		log.fine(TableName + "." + ColumnName + " - From=" + from_ID + ",To=" + to_ID);
		String sql = "UPDATE " + TableName
			+ " SET " + ColumnName + "=" + to_ID
			+ " WHERE " + ColumnName + "=" + from_ID;
		boolean delete = false;
		for (String element : m_deleteTables) {
			if (element.equals(TableName))
			{
				delete = true;
				sql = "DELETE FROM " + TableName + " WHERE " + ColumnName + "=" + from_ID;
			}
		}

		int count = -1;

		try
		{
			Statement stmt = m_con.createStatement ();
			try
			{
				count = stmt.executeUpdate (sql);
				log.fine(count
					+ (delete ? " -Delete- " : " -Update- ") + TableName);
			}
			catch (SQLException ex1)
			{				
				if (!delete){					
					if (	(columnName.equals(M_PRODUCT_ID) && TableName.equals("M_StorageDetail")) //if UPDATE M_StorageDetail SET M_Product_ID = to_ID fails with ORA-0001: Unique Constraint, then delete the row in M_StorageDetail for the FROM Product 
							||(columnName.equals(AD_USER_ID) && TableName.equals("R_ContactInterest")) //if UPDATE R_ContactInterest SET AD_User_ID = to_ID fails with ORA-001: Unique Constraint, then delete the row in AD_User for the FROM User 
					)					
					{
						String deleteSql="DELETE FROM "+TableName+" WHERE "+ColumnName+" = "+from_ID;
						Statement delStmt= m_con.createStatement();
						try{
							count = delStmt.executeUpdate(deleteSql);
							log.fine(count + "Delete: "+TableName);
						}
						catch (SQLException ex2){
							count = -1;
							m_errorLog.append(Env.NL)
							.append("DELETE FROM " )
							.append(TableName).append(" - ").append(ex2.toString())
							.append(" - ").append(deleteSql);
						}
					}					  
					else{
						count = -1;
						m_errorLog.append(Env.NL)
							.append(delete ? "DELETE FROM " : "UPDATE ")
							.append(TableName).append(" - ").append(ex1.toString())
							.append(" - ").append(sql);
					}
				}
				else{
					count = -1;
					m_errorLog.append(Env.NL)
						.append(delete ? "DELETE FROM " : "UPDATE ")
						.append(TableName).append(" - ").append(ex1.toString())
						.append(" - ").append(sql);
				}
			}
			stmt.close();
			stmt = null;
		}
		catch (SQLException ex)
		{
			count = -1;
			m_errorLog.append(Env.NL)
				.append(delete ? "DELETE FROM " : "UPDATE ")
				.append(TableName).append(" - ").append(ex.toString())
				.append(" - ").append(sql);
		}
		return count;
	}	//	mergeTable
	
	/**
	 * 	Post Merge
	 *	@param ColumnName column name
	 *	@param to_ID ID
	 */
	private void postMerge (String ColumnName, int to_ID)
	{
		if (ColumnName.equals(AD_ORG_ID))
		{
			
		}
		else if (ColumnName.equals(AD_USER_ID))
		{
			
		}
		else if (ColumnName.equals(C_BPARTNER_ID))
		{
			MBPartner bp = new MBPartner (Env.getCtx(), to_ID, null);
			if (bp.get_ID() != 0)
			{
				MPayment[] payments = MPayment.getOfBPartner(Env.getCtx(), bp.getC_BPartner_ID(), null);
				for (MPayment payment : payments) {
					if (payment.testAllocation())
						payment.save();
				}
				MInvoice[] invoices = MInvoice.getOfBPartner(Env.getCtx(), bp.getC_BPartner_ID(), null);
				for (MInvoice invoice : invoices) {
					if (invoice.testAllocation())
						invoice.save();
				}
				bp.setTotalOpenBalance();
				bp.setActualLifeTimeValue();
				bp.save();
			}
		}
		else if (ColumnName.equals(M_PRODUCT_ID))
		{
			
		}
	}	//	postMerge
	
	private String getTranslatedMessage(String text){
		return Msg.translate(getCtx(), text);
	}
}