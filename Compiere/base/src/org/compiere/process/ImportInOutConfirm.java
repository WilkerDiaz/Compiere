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
 *	Import Confirmations
 *	
 *  @author Jorg Janke
 *  @version $Id: ImportInOutConfirm.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class ImportInOutConfirm extends SvrProcess
{
	/**	Client to be imported to		*/
	private int 			p_AD_Client_ID = 0;
	/**	Delete old Imported			*/
	private boolean			p_DeleteOldImported = false;
	/**	Import						*/
	private int				p_I_InOutLineConfirm_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				p_DeleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_I_InOutLineConfirm_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	doIt
	 *	@return info
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("I_InOutLineConfirm_ID=" + p_I_InOutLineConfirm_ID);
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID= ? ";
		
		//	Delete Old Imported
		if (p_DeleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_InOutLineConfirm "
				  + "WHERE I_IsImported='Y'").append (clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
			log.fine("Delete Old Impored =" + no);
		}

		//	Set IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm "
			+ "SET IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.info ("Reset=" + no);

		//	Set Client from Name
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm i "
			+ "SET AD_Client_ID=COALESCE (AD_Client_ID, ? ) "
			+ "WHERE (AD_Client_ID IS NULL OR AD_Client_ID=0)"
			+ " AND I_IsImported<>'Y'");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		log.fine("Set Client from Value=" + no);

		//	Error Confirmation Line
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Confirmation Line, '"
			+ "WHERE (M_InOutLineConfirm_ID IS NULL OR M_InOutLineConfirm_ID=0"
			+ " OR NOT EXISTS (SELECT * FROM M_InOutLineConfirm c WHERE i.M_InOutLineConfirm_ID=c.M_InOutLineConfirm_ID))"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid InOutLineConfirm=" + no);

		//	Error Confirmation No
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Missing Confirmation No, '"
			+ "WHERE (ConfirmationNo IS NULL OR ConfirmationNo='')"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid ConfirmationNo=" + no);
		
		//	Qty
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Target<>(Confirmed+Difference+Scrapped), ' "
			+ "WHERE EXISTS (SELECT * FROM M_InOutLineConfirm c "
				+ "WHERE i.M_InOutLineConfirm_ID=c.M_InOutLineConfirm_ID"
				+ " AND c.TargetQty<>(i.ConfirmedQty+i.ScrappedQty+i.DifferenceQty))"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Qty=" + no);
		
		commit();
		
		/*********************************************************************/
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql = new StringBuffer ("SELECT * FROM I_InOutLineConfirm "
			+ "WHERE I_IsImported='N'").append (clientCheck)
			.append(" ORDER BY I_InOutLineConfirm_ID");
		no = 0;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_TrxName());
			pstmt.setInt(1, p_AD_Client_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				X_I_InOutLineConfirm importLine = new X_I_InOutLineConfirm (getCtx(), rs, get_TrxName());
				MInOutLineConfirm confirmLine = new MInOutLineConfirm (getCtx(), 
					importLine.getM_InOutLineConfirm_ID(), get_TrxName());
				if (confirmLine.get_ID() == 0
					|| confirmLine.get_ID() != importLine.getM_InOutLineConfirm_ID())
				{
					importLine.setI_IsImported(X_I_InOutLineConfirm.I_ISIMPORTED_No);
					importLine.setI_ErrorMsg("ID Not Found");
					importLine.save();
				}
				else
				{
					confirmLine.setConfirmationNo(importLine.getConfirmationNo());
					confirmLine.setConfirmedQty(importLine.getConfirmedQty());
					confirmLine.setDifferenceQty(importLine.getDifferenceQty());
					confirmLine.setScrappedQty(importLine.getScrappedQty());
					confirmLine.setDescription(importLine.getDescription());
					if (confirmLine.save())
					{
						//	Import
						importLine.setI_IsImported(X_I_InOutLineConfirm.I_ISIMPORTED_Yes);
						importLine.setProcessed(true);
						if (importLine.save())
							no++;
					}
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "@Updated@ #" + no;
	}	//	doIt

}	//	ImportInOutConfirm
