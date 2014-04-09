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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Import Currency Conversion Rates 
 *	
 *  @author Jorg Janke
 *  @version $Id: ImportConversionRate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ImportConversionRate extends SvrProcess
{
	
	/**	Client to be imported to			*/
	private int				p_AD_Client_ID = 0;
	/**	Organization to be imported to		*/
	private int				p_AD_Org_ID = 0;
	/**	Conversion Type to be imported to	*/
	private int				p_C_ConversionType_ID = 0;
	/**	Default Date					*/
	private Timestamp		p_ValidFrom = null;
	/** Default Reciprocal				*/
	private boolean 		p_CreateReciprocalRate = false;
	/**	Delete old Imported				*/
	private boolean			p_DeleteOldImported = false;

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
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_ConversionType_ID"))
				p_C_ConversionType_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("ValidFrom"))
				p_ValidFrom = (Timestamp)element.getParameter();
			else if (name.equals("CreateReciprocalRate"))
				p_CreateReciprocalRate = "Y".equals(element.getParameter());
			else if (name.equals("DeleteOldImported"))
				p_DeleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("doIt - AD_Client_ID=" + p_AD_Client_ID
			+ ",AD_Org_ID=" + p_AD_Org_ID
			+ ",C_ConversionType_ID=" + p_C_ConversionType_ID
			+ ",ValidFrom=" + p_ValidFrom
			+ ",CreateReciprocalRate=" + p_CreateReciprocalRate);
		//
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID= ? ";
		//	****	Prepare	****

		//	Delete Old Imported
		if (p_DeleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_Conversion_Rate "
				  + "WHERE I_IsImported='Y'").append (clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, Location, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_Conversion_Rate "
			  + "SET AD_Client_ID = COALESCE (AD_Client_ID, ?),"
			  + " AD_Org_ID = COALESCE (AD_Org_ID,?),");
		if (p_C_ConversionType_ID != 0)
			sql.append(" C_ConversionType_ID = COALESCE (C_ConversionType_ID,?),");
		if (p_ValidFrom != null)
			sql.append(" ValidFrom = COALESCE (ValidFrom, ?),");
		else
			sql.append(" ValidFrom = COALESCE (ValidFrom,SysDate),");
		sql.append(" CreateReciprocalRate = COALESCE (CreateReciprocalRate,'").append (p_CreateReciprocalRate ? "Y" : "N").append ("'),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = ? , "
			+ " I_ErrorMsg = NULL,"
			+ " Processed = 'N',"	
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		
		List<Object> params = new ArrayList<Object>();
		params.add(p_AD_Client_ID);
		params.add(p_AD_Org_ID);
		if(p_C_ConversionType_ID != 0)
			params.add(p_C_ConversionType_ID);
		if(p_ValidFrom != null)
			params.add(p_ValidFrom);
		params.add(getAD_User_ID());
		
		no = DB.executeUpdate(get_TrxName(), sql.toString(),params.toArray());
		log.info ("Reset =" + no);

		//	Org
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = new StringBuffer ("UPDATE I_Conversion_Rate o "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Org, '"
			+ "WHERE (AD_Org_ID IS NULL"
			+ " OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Org =" + no);
			
		//	Conversion Type
		sql = new StringBuffer ("UPDATE I_Conversion_Rate i "
			+ "SET C_ConversionType_ID = (SELECT C_ConversionType_ID FROM C_ConversionType c"
			+ " WHERE c.Value=i.ConversionTypeValue AND c.AD_Client_ID IN (0,i.AD_Client_ID) AND c.IsActive='Y') "
			+ "WHERE C_ConversionType_ID IS NULL AND ConversionTypeValue IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no > 0)
			log.fine("Set ConversionType =" + no);
		sql = new StringBuffer ("UPDATE I_Conversion_Rate i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid ConversionType, ' "
			+ "WHERE (C_ConversionType_ID IS NULL"
			+ " OR NOT EXISTS (SELECT * FROM C_ConversionType c "
				+ "WHERE i.C_ConversionType_ID=c.C_ConversionType_ID AND c.IsActive='Y'"
				+ " AND c.AD_Client_ID IN (0,i.AD_Client_ID)))"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid ConversionType =" + no);
		
		//	Currency
		sql = new StringBuffer ("UPDATE I_Conversion_Rate i "
			+ "SET C_Currency_ID = (SELECT C_Currency_ID FROM C_Currency c"
			+ "	WHERE c.ISO_Code=i.ISO_Code AND c.AD_Client_ID IN (0,i.AD_Client_ID) AND c.IsActive='Y') "
			+ "WHERE C_Currency_ID IS NULL AND ISO_Code IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no > 0)
			log.fine("Set Currency =" + no);
		sql = new StringBuffer ("UPDATE I_Conversion_Rate i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Currency, ' "
			+ "WHERE (C_Currency_ID IS NULL"
			+ " OR NOT EXISTS (SELECT * FROM C_Currency c "
				+ "WHERE i.C_Currency_ID=c.C_Currency_ID AND c.IsActive='Y'"
				+ " AND c.AD_Client_ID IN (0,i.AD_Client_ID)))"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Currency =" + no);

		//	Currency To
		sql = new StringBuffer ("UPDATE I_Conversion_Rate i "
			+ "SET C_Currency_To_ID = (SELECT C_Currency_ID FROM C_Currency c"
			+ "	WHERE c.ISO_Code=i.ISO_Code_To AND c.AD_Client_ID IN (0,i.AD_Client_ID) AND c.IsActive='Y') "
			+ "WHERE C_Currency_To_ID IS NULL AND ISO_Code_To IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no > 0)
			log.fine("Set Currency To =" + no);
		sql = new StringBuffer ("UPDATE I_Conversion_Rate i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Currency To, ' "
			+ "WHERE (C_Currency_To_ID IS NULL"
			+ " OR NOT EXISTS (SELECT * FROM C_Currency c "
				+ "WHERE i.C_Currency_To_ID=c.C_Currency_ID AND c.IsActive='Y'"
				+ " AND c.AD_Client_ID IN (0,i.AD_Client_ID)))"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Currency To =" + no);
			
		//	Rates
		sql = new StringBuffer ("UPDATE I_Conversion_Rate i "
			+ "SET MultiplyRate = 1 / DivideRate "
			+ "WHERE (MultiplyRate IS NULL OR MultiplyRate = 0) AND DivideRate IS NOT NULL AND DivideRate<>0"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no > 0)
			log.fine("Set MultiplyRate =" + no);
		sql = new StringBuffer ("UPDATE I_Conversion_Rate i "
			+ "SET DivideRate = 1 / MultiplyRate "
			+ "WHERE (DivideRate IS NULL OR DivideRate = 0) AND MultiplyRate IS NOT NULL AND MultiplyRate<>0"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no > 0)
			log.fine("Set DivideRate =" + no);
		sql = new StringBuffer ("UPDATE I_Conversion_Rate i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Rates, ' "
			+ "WHERE (MultiplyRate IS NULL OR MultiplyRate = 0 OR DivideRate IS NULL OR DivideRate = 0)"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Rates =" + no);

		commit();
		/*********************************************************************/

		int noInsert = 0;
		sql = new StringBuffer ("SELECT * FROM I_Conversion_Rate "
			+ "WHERE I_IsImported='N'").append (clientCheck)
			.append(" ORDER BY C_Currency_ID, C_Currency_To_ID, ValidFrom");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			pstmt.setInt(1, p_AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_I_Conversion_Rate imp = new X_I_Conversion_Rate (getCtx(), rs, get_TrxName());
				MConversionRate rate = new MConversionRate (imp, 
					imp.getC_ConversionType_ID(), 
					imp.getC_Currency_ID(), imp.getC_Currency_To_ID(),
					imp.getMultiplyRate(), imp.getValidFrom());
				if (imp.getValidTo() != null)
					rate.setValidTo(imp.getValidTo());
				if (rate.save())
				{
					imp.setC_Conversion_Rate_ID(rate.getC_Conversion_Rate_ID());
					imp.setI_IsImported(X_I_Conversion_Rate.I_ISIMPORTED_Yes);
					imp.setProcessed(true);
					imp.save();
					noInsert++;
					//
					if (imp.isCreateReciprocalRate())
					{
						rate = new MConversionRate (imp, 
							imp.getC_ConversionType_ID(), 
							imp.getC_Currency_To_ID(), imp.getC_Currency_ID(),
							imp.getDivideRate(), imp.getValidFrom());
						if (imp.getValidTo() != null)
							rate.setValidTo(imp.getValidTo());
						if (rate.save())					
							noInsert++;
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
		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_Conversion_Rate "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		//
		addLog (0, null, new BigDecimal (noInsert), "@C_Conversion_Rate_ID@: @Inserted@");
		return "";
	}	//	doIt

}	//	ImportConversionRate
