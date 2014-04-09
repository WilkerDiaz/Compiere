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
package compiere.model;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.controller.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 *	User Callout Example.
 *
 *  @author Jorg Janke
 *  @version  $Id: CalloutUser.java,v 1.2 2006/07/30 00:51:57 jjanke Exp $
 */
public class CalloutUser extends CalloutEngine
{
	/** Logger					*/
	private final CLogger		log = CLogger.getCLogger(getClass());

	/**
	 *	JustAnExample - Swing.
	 *	The string in the Callout field is:
	 *  <code>compiere.model.CalloutUser.justAnExample</code>
	 *  Implemented in Test.T_Integer
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *	@return error message or "" if OK
	 */
	public String justAnExample (Ctx ctx, int WindowNo,
		GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		log.info("JustAnExample - Swing UI");
		if (!mField.getColumnName().equals("T_Integer"))
			return "";
		//	Set a Value
		mTab.setValue("T_Number", new BigDecimal(12.34));
		//	Set something to Null
		mTab.setValue("T_Qty", null);
		return "";
	}	//	justAnExample

	/**
	 *	JustAnExample - Swing.
	 *	The string in the Callout field is:
	 *  <code>compiere.model.CalloutUser.justAnExample</code>
	 *  Implemented in Test.T_Integer
	 *
	 *	@param ctx context
	 *	@param windowNo window no
	 *	@param po the object
	 *	@param field the field of the callout
	 *	@param oldValue old value
	 *	@param newValue new value
	 *	@return Change VO or null;
	 */
	public void justAnExample (PO po, UIField field, String oldValue, String newValue)
	{
		log.info("JustAnExample - Web UI");
		if (!field.getColumnName().equals("T_Integer"))
			return;
		//	Set a Value
		po.set_Value("T_Number", new BigDecimal(12.34));
		MTest test = (MTest)po;
		BigDecimal testValue = test.getT_Number();
		assert testValue.equals(new BigDecimal(12.34));

		//	Example to set a mandatory column to null (although here Qty is not mandatory)
		ChangeVO change = po.get_ChangeVO();
		change.addChangedValue("T_Qty", null);
		//
	//	change.addWarning("Example warning");
	}	//	justAnExample

	/**
	 *	BPartner - Swing
	 *		- C_BPartner_Location_ID
	 *		- AD_User_ID
	 *	compiere.model.CalloutUser.bPartner
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *  @return error message
	 */
	public String bPartner (Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		Integer C_BPartner_ID = (Integer)value;
		if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
			return "";

		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
			+ " COALESCE(p.M_PriceList_ID,g.M_PriceList_ID) AS M_PriceList_ID, p.PaymentRule,p.POReference,"
			+ " p.SO_Description,p.IsDiscountPrinted,"
			+ " p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ " l.C_BPartner_Location_ID,c.AD_User_ID,"
			+ " COALESCE(p.PO_PriceList_ID,g.PO_PriceList_ID) AS PO_PriceList_ID, p.PaymentRulePO,p.PO_PaymentTerm_ID "
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID)"
			+ " LEFT OUTER JOIN C_BPartner_Location l ON (p.C_BPartner_ID=l.C_BPartner_ID AND l.IsBillTo='Y' AND l.IsActive='Y')"
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID) "
			+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'";		//	#1

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			//
			if (rs.next())
			{
				//	Location
				int locID = rs.getInt("C_BPartner_Location_ID");
				//	overwritten by InfoBP selection - works only if InfoWindow
				//	was used otherwise creates error (uses last value, may belong to differnt BP)
				if (C_BPartner_ID.toString().equals(ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID")))
				{
					String loc = ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_Location_ID");
					if (loc.length() > 0)
						locID = Integer.parseInt(loc);
				}
				if (locID == 0)
					mTab.setValue("C_BPartner_Location_ID", null);
				else
					mTab.setValue("C_BPartner_Location_ID", new Integer(locID));

				//	Contact - overwritten by InfoBP selection
				int contID = rs.getInt("AD_User_ID");
				if (C_BPartner_ID.toString().equals(ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_BPartner_ID")))
				{
					String cont = ctx.getContext( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contID = Integer.parseInt(cont);
				}
				if (contID == 0)
					mTab.setValue("AD_User_ID", null);
				else
					mTab.setValue("AD_User_ID", new Integer(contID));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}

		return "";
	}	//	bPartner



	/**************************************************************************
	 *	Frie Value - convert to standardized Name
	 *
	 * @param value Name
	 * @return Name
	 */
	public String Frie_Name (String value)
	{
		if (value == null || value.length() == 0)
			return "";
		//
		String retValue = value;
		String SQL = "SELECT FRIE_Name(?) FROM DUAL";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, (Trx) null);
			pstmt.setString(1, value);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getString(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		return retValue;
	}	//	Frie_Name


	/**
	 *	Frie Value - convert Name to Value
	 *
	 *  @param value Name
	 *  @return Value of Name
	 */
	public String Frie_Value (String value)
	{
		if (value == null || value.length() == 0)
			return "";
		//
		String retValue = value;
		String SQL = "SELECT FRIE_Value(FRIE_Name(?)) FROM DUAL";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, (Trx) null);
			pstmt.setString(1, value);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getString(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		return retValue;
	}	//	Frie_Value


	/**
	 *	Frie Status - convert to Status.
	 *
	 *  @param value value
	 *  @return Status
	 */
	public String Frie_Status (String value)
	{
		String retValue = "N";		//	default
		if (value != null && value.equals("A"))		//	Auslaufartikel
			retValue = "Y";			//
		return retValue;
	}	//	Frie_Status

}	//	CalloutUser
