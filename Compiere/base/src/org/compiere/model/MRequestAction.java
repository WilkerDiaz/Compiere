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

import java.sql.*;
import java.util.ArrayList;

import org.compiere.util.*;

/**
 *	Request History Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequestAction.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MRequestAction extends X_R_RequestAction
{
    /** Logger for class MRequestAction */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRequestAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param R_RequestAction_ID id
	 */
	public MRequestAction (Ctx ctx, int R_RequestAction_ID, Trx trx)
	{
		super (ctx, R_RequestAction_ID, trx);
	}	//	MRequestAction

	/**
	 * 	Load Construtor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequestAction(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MRequestAction

	/**
	 * 	Parent Action Constructor
	 *	@param request parent
	 *	@param newRecord new (copy all)
	 */
	public MRequestAction (MRequest request, boolean newRecord)
	{
		this (request.getCtx(), 0, request.get_Trx());
		setClientOrg(request);
		setR_Request_ID (request.getR_Request_ID());
	}	//	MRequestAction

	/**
	 * 	Add Null Column
	 *	@param columnName
	 */
	public void addNullColumn (String columnName)
	{
		String nc = getNullColumns();
		if (nc == null)
			setNullColumns(columnName);
		else
			setNullColumns(nc + ";" + columnName);
	}	//	addNullColumn
	

	/**
	 * 	Get Name of creator
	 *	@return name
	 */
	public String getCreatedByName()
	{
		MUser user = MUser.get(getCtx(), getCreatedBy());
		return user.getName();
	}	//	getCreatedByName

	/**
	 * 	Get Changes as HTML string
	 *	@return changes
	 */
	public String getChangesHTML()
	{
		StringBuffer sb = new StringBuffer();
		getChangeHTML(sb, "Priority");
		getChangeHTML(sb, "PriorityUser");
		getChangeHTML(sb, "R_Category_ID");
		getChangeHTML(sb, "R_Group_ID");
		getChangeHTML(sb, "R_RequestType_ID");
		getChangeHTML(sb, "R_Resolution_ID");
		getChangeHTML(sb, "R_Status_ID");
		getChangeHTML(sb, "SalesRep_ID");
		getChangeHTML(sb, "Summary");
		//
	//	getChangeHTML(sb, "AD_Org_ID");		//	always stored
		getChangeHTML(sb, "AD_Role_ID");
		getChangeHTML(sb, "AD_User_ID");
		getChangeHTML(sb, "C_Activity_ID");
		getChangeHTML(sb, "C_BPartner_ID");
		getChangeHTML(sb, "C_Invoice_ID");
		getChangeHTML(sb, "C_Order_ID");
		getChangeHTML(sb, "C_Payment_ID");
		getChangeHTML(sb, "C_Project_ID");
		getChangeHTML(sb, "DateNextAction");
		getChangeHTML(sb, "IsEscalated");
		getChangeHTML(sb, "IsInvoiced");
		getChangeHTML(sb, "IsSelfService");
		getChangeHTML(sb, "M_InOut_ID");
		getChangeHTML(sb, "M_Product_ID");
		getChangeHTML(sb, "A_Asset_ID");
		
		if (sb.length() == 0)
			sb.append("./.");
		//	Unicode check
		char[] chars = sb.toString().toCharArray();
		sb = new StringBuffer(chars.length);
		for (char c : chars) {
			if (c > 255)
				sb.append("&#").append(c).append(";");
			else
				sb.append(c);
		}
		return sb.toString();
	}	//	getChangesHTML
	
	/**
	 * 	Get individual Change HTML
	 *	@param sb string to append to
	 *	@param columnName column name
	 */
	private void getChangeHTML (StringBuffer sb, String columnName)
	{
		if (get_Value(columnName) != null)
		{
			if (sb.length() > 0)
				sb.append("<br>");
			sb.append(Msg.getElement(getCtx(), columnName))
				.append(": ").append(get_DisplayValue(columnName, true));
		}
		else
		{
			String nc = getNullColumns();
			if (nc != null && nc.indexOf(columnName) != -1)
			{
				if (sb.length() > 0)
					sb.append("<br>");
				sb.append("(")
					.append(Msg.getElement(getCtx(), columnName))
					.append(")");
			}
		}
	}	//	getChangeHTML
	
	public static MRequestAction[] getActions(MRequest request)
	{
		ArrayList<MRequestAction> retVal = new ArrayList<MRequestAction>();
		String sql = "SELECT * FROM R_RequestAction WHERE R_Request_ID = ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql,request.get_Trx());
			pstmt.setInt(1, request.getR_Request_ID());
			rs = pstmt.executeQuery();
			while(rs.next())
				retVal.add(new MRequestAction(request.getCtx(),rs,request.get_Trx()));
		}
		catch (Exception e)
		{
			log.severe(e.toString());
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		MRequestAction[] actions = new MRequestAction[retVal.size()];
		retVal.toArray(actions);
		return actions;
	}
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		return true;
	}	//	beforeSave
}	//	MRequestAction
