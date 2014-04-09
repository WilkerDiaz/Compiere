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

import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	(Mass) Update Requests
 *	@author Jorg Janke
 */
public class RequestUpdate extends SvrProcess
{
	/** Query Request Type			*/
	private int 	p_R_RequestType_ID = 0;
	/** Query Request Status		*/
	private int 	p_R_Status_ID = 0;
	/** Query Request Group			*/
	private int		p_R_Group_ID = 0;
	/** Query Request Category		*/
	private int		p_R_Category_ID = 0;
	/** Query BPartner				*/
	private int		p_C_BPartner_ID = 0;
	/** Query Sales Rep				*/
	private int		p_SalesRep_ID = 0;
	/** Query Role					*/
	private int		p_AD_Role_ID = 0;

	/** Summary						*/
	private String	p_Result = null;
	/** New RequestType				*/
	private int 	p_New_RequestType_ID = 0;
	/** New Request Status		*/
	private int 	p_New_Status_ID = 0;
	/** New Request Group			*/
	private int		p_New_Group_ID = 0;
	/** New Request Category		*/
	private int		p_New_Category_ID = 0;
	/** New Sales Rep				*/
	private int		p_New_SalesRep_ID = 0;
	/** New Role					*/
	private int		p_New_AD_Role_ID = 0;
	
	/** Query AND					*/
	private static final String		AND = " AND ";
	
	/**
	 * 	Get Parameters
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("R_RequestType_ID"))
				p_R_RequestType_ID = element.getParameterAsInt();
			else if (name.equals("R_Status_ID"))
				p_R_Status_ID = element.getParameterAsInt();
			else if (name.equals("R_Group_ID"))
				p_R_Group_ID = element.getParameterAsInt();
			else if (name.equals("R_Category_ID"))
				p_R_Category_ID = element.getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("SalesRep_ID"))
				p_SalesRep_ID = element.getParameterAsInt();
			else if (name.equals("AD_Role_ID"))
				p_AD_Role_ID = element.getParameterAsInt();
			else if (name.equals("Result"))
				p_Result = (String)element.getParameter();

			else if (name.equals("New_RequestType_ID"))
				p_New_RequestType_ID = element.getParameterAsInt();
			else if (name.equals("New_Status_ID"))
				p_New_Status_ID = element.getParameterAsInt();
			else if (name.equals("New_Group_ID"))
				p_New_Group_ID = element.getParameterAsInt();
			else if (name.equals("New_Category_ID"))
				p_New_Category_ID = element.getParameterAsInt();
			else if (name.equals("New_SalesRep_ID"))
				p_New_SalesRep_ID = element.getParameterAsInt();
			else if (name.equals("New_AD_Role_ID"))
				p_New_AD_Role_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	
	/**
	 * 	Process
	 * 	@return info
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("R_RequestType_ID=" + p_R_RequestType_ID
			+ ",R_Status_ID=" + p_R_Status_ID
			+ ",R_Group_ID=" + p_R_Group_ID
			+ ",R_Category_ID=" + p_R_Category_ID
			+ ",C_BPartner_ID=" + p_C_BPartner_ID
			+ ",SalesRep_ID=" + p_SalesRep_ID
			+ ",AD_Role_ID=" + p_AD_Role_ID
			+ ", Result=" + p_Result
			+ ", New_RequestType_ID=" + p_New_RequestType_ID
			+ ",New_Status_ID=" + p_New_Status_ID
			+ ",New_Group_ID=" + p_New_Group_ID
			+ ",New_Category_ID=" + p_New_Category_ID
			+ ",New_SalesRep_ID=" + p_New_SalesRep_ID
		);
		//	Some query criteria needed
		if (p_R_RequestType_ID == 0 && p_R_Status_ID == 0 && p_R_Group_ID == 0
			&& p_R_Category_ID == 0 && p_C_BPartner_ID == 0 
			&& p_SalesRep_ID == 0 && p_AD_Role_ID == 0)
			throw new CompiereUserException("@NotFound@ @Query@");
		//	Check Status in Request Type
		if (p_R_Status_ID != 0)	
		{
			MStatus sta = MStatus.get(getCtx(), p_R_Status_ID);
			if (sta.getR_Status_ID() != p_R_Status_ID)
				throw new CompiereUserException("@NotFound@ @R_Status_ID@ - ID=" + p_R_Status_ID);
			if (!sta.isRequestType(p_R_RequestType_ID))
				throw new CompiereUserException("@R_Status_ID@ <> @R_RequestType_ID@ " + sta.toStringX());
		}
		
		if (Util.isEmpty(p_Result))
			throw new CompiereUserException("@NotFound@ @Result@");
			
		//	Check New Status in New Request Type
		if (p_New_Status_ID != 0)	
		{
			MStatus sta = MStatus.get(getCtx(), p_New_Status_ID);
			if (sta.getR_Status_ID() != p_New_Status_ID)
				throw new CompiereUserException("@NotFound@ @R_Status_ID@ - ID=" + p_R_Status_ID);
			if (!sta.isRequestType(p_New_RequestType_ID))
				throw new CompiereUserException("@R_Status_ID@ <> @R_RequestType_ID@ " + sta.toStringX());
		}
		
		
		//	Query
		StringBuffer sb = new StringBuffer("SELECT * FROM R_Request WHERE ");
		if (p_R_RequestType_ID != 0)
			sb.append("R_RequestType_ID= ? ").append(AND);
		if (p_R_Status_ID != 0)
			sb.append("R_Status_ID= ? ").append(AND);
		if (p_R_Group_ID != 0)
			sb.append("R_Group_ID= ? ").append(AND);
		if (p_R_Category_ID != 0)
			sb.append("R_Category_ID= ? ").append(AND);
		if (p_C_BPartner_ID != 0)
			sb.append("C_BPartner_ID= ? ").append(AND);
		if (p_SalesRep_ID != 0)
			sb.append("SalesRep_ID= ? ").append(AND);
		if (p_AD_Role_ID != 0)
			sb.append("AD_Role_ID= ? ").append(AND);
		sb.append("IsActive='Y'");
		//
		int changes = 0;
		int errors = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int index = 1;
        try
        {
	        pstmt = DB.prepareStatement(sb.toString(), get_TrxName());
			if (p_R_RequestType_ID != 0)
				pstmt.setInt(index++,p_R_RequestType_ID);
			if (p_R_Status_ID != 0)
				pstmt.setInt(index++,p_R_Status_ID);
			if (p_R_Group_ID != 0)
				pstmt.setInt(index++,p_R_Group_ID);
			if (p_R_Category_ID != 0)
				pstmt.setInt(index++,p_R_Category_ID);
			if (p_C_BPartner_ID != 0)
				pstmt.setInt(index++,p_C_BPartner_ID);
			if (p_SalesRep_ID != 0)
				pstmt.setInt(index++,p_SalesRep_ID);
			if (p_AD_Role_ID != 0)
				pstmt.setInt(index++,p_AD_Role_ID);

	        rs = pstmt.executeQuery();
	        while (rs.next())
	        {
	        	MRequest request = new MRequest(getCtx(), rs, get_TrxName());
	        	if (p_New_RequestType_ID != 0)
	        	{
	        		request.setR_RequestType_ID(p_New_RequestType_ID);
	        		if (p_New_Status_ID != 0)
	        			request.setR_Status_ID(p_New_Status_ID);
	        		else
	        			request.setR_Status_ID();
	        	}
	        	else if (p_New_Status_ID != 0)
        			request.setR_Status_ID(p_New_Status_ID);
	        	if (p_New_Group_ID != 0)
	        		request.setR_Group_ID(p_New_Group_ID);
	        	if (p_New_Category_ID != 0)
	        		request.setR_Category_ID(p_New_Category_ID);
	        	if (p_New_SalesRep_ID != 0)
	        		request.setSalesRep_ID(p_New_SalesRep_ID);
	        	if (p_New_AD_Role_ID != 0)
	        		request.setAD_Role_ID(p_New_AD_Role_ID);
	        	request.setResult(p_Result);
	        	//
	        	if (request.save())
	        	{
	        		addLog(request.toStringX());
	        		changes++;
	        	}
	        	else
	        	{
	        		addLog("Error: " + request.toString());
	        		errors++;
	        	}
	        }
        }
        catch (Exception e)
        {
	        log.log(Level.SEVERE, sb.toString(), e);
        }
        finally
        {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }
		return "@Changed@ #" + changes + ", @Errors@ # " + errors;
	}	//	doIt

	
}	//	RequestUpdate
