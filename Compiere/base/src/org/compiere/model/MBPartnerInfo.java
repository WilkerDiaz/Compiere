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
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.util.*;

/**
 *	Business Partner Info Model for Query
 *	
 *  @author Jorg Janke
 *  @version $Id: MBPartnerInfo.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MBPartnerInfo extends X_RV_BPartner
{
    /** Logger for class MBPartnerInfo */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartnerInfo.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Find BPartners
	 *	@param ctx context for sql security
	 *	@param Value Business Partner Value
	 *	@param Name Business Partner Name
	 *	@param Contact Contact/User Name
	 *	@param EMail Contact/User EMail
	 *	@param Phone phone
	 *	@param City city
	 *	@return array if of info
	 */
	public static ArrayList<MBPartnerInfo> findAll (Ctx ctx, 
		String Value, String Name, String Contact, String EMail, String Phone, String City)
	{
		StringBuffer sql = new StringBuffer ("SELECT * FROM RV_BPartner WHERE IsActive='Y'");
		StringBuffer sb = new StringBuffer();
		Value = getFindParameter (Value);
		if (Value != null)
			sb.append("UPPER(Value) LIKE ?");
		Name = getFindParameter (Name);
		if (Name != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(Name) LIKE ?");
		}
		Contact = getFindParameter (Contact);
		if (Contact != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(ContactName) LIKE ?");
		}
		EMail = getFindParameter (EMail);
		if (EMail != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(EMail) LIKE ?");
		}
		Phone = getFindParameter (Phone);
		if (Phone != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(Phone) LIKE ?");
		}
		City = getFindParameter (City);
		if (City != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(City) LIKE ?");
		}
		if (sb.length() > 0)
			sql.append(" AND (").append(sb).append(")");
		sql.append(" ORDER BY Value");
		//
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
		String finalSQL = role.addAccessSQL(sql.toString(), 
			"RV_BPartner", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		ArrayList<MBPartnerInfo> list = new ArrayList<MBPartnerInfo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(finalSQL, (Trx) null);
			int index = 1;
			if (Value != null)
				pstmt.setString(index++, Value);
			if (Name != null)
				pstmt.setString(index++, Name);
			if (Contact != null)
				pstmt.setString(index++, Contact);
			if (EMail != null)
				pstmt.setString(index++, EMail);
			if (Phone != null)
				pstmt.setString(index++, Phone);
			if (City != null)
				pstmt.setString(index++, City);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPartnerInfo (ctx, rs, null));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "find - " + finalSQL, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	Return
		return list;
	}	//	find
	
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MBPartnerInfo.class);
	
	/**************************************************************************
	 * 	Temporary Info Constructor - You cannot SAVE!
	 *	@param ctx context
	 */
	public MBPartnerInfo (Ctx ctx)
	{
		super(ctx, 0, null);
	}	//	MBPartnerInfo

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MBPartnerInfo (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBPartnerInfo
	
	
	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main(String[] args)
    {
	    Compiere.startup(true);
	    Ctx ctx = Env.getCtx();
		ctx.setAD_Client_ID(11);	//	GardenWorld
		ctx.setAD_Role_ID(102);		//	GardenAdmin
		ctx.setAD_User_ID(101);		//	GardenAdmin
	    String bpValue = "%";
	    String bpName = "%";
		String city = "%"; 
		String uName = "%";
		String uPhone = "%";
		String email = "%";
	    ArrayList<MBPartnerInfo> bpis = findAll(ctx, bpValue, bpName, uName, email, uPhone, city);
	    //
	    MBPartnerInfo bpi = bpis.get(0);
	    org.w3c.dom.Document doc = bpi.get_xmlDocument(false, false);
	    System.out.println(doc);
	    StringBuffer bpXml = bpi.get_xmlString(null, false);
	    System.out.println(bpXml.toString());
    }	//	main

}	//	MBPartnerInfo
