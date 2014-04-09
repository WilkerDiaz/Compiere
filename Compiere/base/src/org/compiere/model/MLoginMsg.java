/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.util.*;

/**
 * 	Login Message Model
 *	@author Jorg Janke
 */
public class MLoginMsg extends X_AD_LoginMsg
{
    /** Logger for class MLoginMsg */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLoginMsg.class);

	/** */
    private static final long serialVersionUID = -7307490192415584016L;


    /**
     * 	Get the message for the user
     * 	@param ctx context
     *	@param AD_User_ID user id
     *	@return array of messages
     */
    public static ArrayList<MLoginMsg> getForUser (Ctx ctx, int AD_User_ID)
    {
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID());
		boolean isAdmin = ((role!=null) && role.isAdministrator());

    	StringBuffer sql = new StringBuffer("SELECT * FROM AD_LoginMsg m WHERE IsActive='Y'"
    		+ " AND (ValidFrom IS NULL OR ValidFrom <= SysDate)"
    		+ " AND (ValidTo IS NULL OR SysDate <= ValidTo)"
    		+ " AND AD_Client_ID IN (0,?)"						// #1
    		//  LOGINMSGFREQUENCY_Once
    		+ "	AND ( (LoginMsgFrequency='O' AND NOT EXISTS (SELECT * FROM AD_LoginMsgLog l "
    			+ "WHERE m.AD_LoginMsg_ID=l.AD_LoginMsg_ID AND l.AD_User_ID=? AND l.IsActive='Y' AND IsUserAccepted='Y'))"	//	#2
    		//	LOGINMSGFREQUENCY_Daily;
    		+ " OR (LoginMsgFrequency='D' AND NOT EXISTS (SELECT * FROM AD_LoginMsgLog l "
    			+ "WHERE m.AD_LoginMsg_ID=l.AD_LoginMsg_ID AND l.AD_User_ID=? AND l.IsActive='Y' AND IsUserAccepted='Y'"
    			+ " AND TRUNC(SysDate,'DD')=TRUNC(l.Created,'DD')))"
       		//	LOGINMSGFREQUENCY_OncePerWeek;
    		+ " OR (LoginMsgFrequency='W' AND NOT EXISTS (SELECT * FROM AD_LoginMsgLog l "
    			+ "WHERE m.AD_LoginMsg_ID=l.AD_LoginMsg_ID AND l.AD_User_ID=? AND l.IsActive='Y' AND IsUserAccepted='Y'"
    			+ " AND TRUNC(SysDate,'DAY')=TRUNC(l.Created,'DAY')))"
       	    //	LOGINMSGFREQUENCY_OncePerMonth;
    		+ " OR (LoginMsgFrequency='M' AND NOT EXISTS (SELECT * FROM AD_LoginMsgLog l "
    			+ "WHERE m.AD_LoginMsg_ID=l.AD_LoginMsg_ID AND l.AD_User_ID=? AND l.IsActive='Y' AND IsUserAccepted='Y'"
    			+ " AND TRUNC(SysDate,'MM')=TRUNC(l.Created,'MM')))"
    		+ ")"
    	);
    	ArrayList<MLoginMsg> list = new ArrayList<MLoginMsg>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
	        pstmt.setInt(1, ctx.getAD_Client_ID());
	        pstmt.setInt(2, AD_User_ID);
	        pstmt.setInt(3, AD_User_ID);
	        pstmt.setInt(4, AD_User_ID);
	        pstmt.setInt(5, AD_User_ID);
	        rs = pstmt.executeQuery();
	        while (rs.next())
	        {
	        	MLoginMsg lm = new MLoginMsg(ctx, rs, null);
	        	if (!lm.isDisplayMsg(AD_User_ID, isAdmin))
	        		continue;
		        list.add(lm);
	        }
        }
        catch (Exception e)
        {
	        s_log.log(Level.SEVERE, sql.toString(), e);
        }
        finally
        {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }
    	return list;
    }	//	getForUser

    /**
     * 	Get MLoginMsg from Cache
     *	@param ctx context
     *	@param AD_LoginMsg_ID id
     *	@return MLoginMsg
     */
    public static MLoginMsg get(Ctx ctx, int AD_LoginMsg_ID)
    {
	    Integer key = new Integer(AD_LoginMsg_ID);
	    MLoginMsg retValue = s_cache.get(ctx, key);
	    if (retValue != null)
		    return retValue;
	    retValue = new MLoginMsg(ctx, AD_LoginMsg_ID, null);
	    if (retValue.get_ID() != 0)
		    s_cache.put(key, retValue);
	    return retValue;
    }	//	get

    /**	Cache						*/
    private static final CCache<Integer, MLoginMsg> s_cache
    	= new CCache<Integer, MLoginMsg>("AD_LoginMsg", 20);

    /**	Logger	*/
    private static final CLogger s_log = CLogger.getCLogger(MLoginMsg.class);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_LoginMsg_ID id
	 *	@param trx transaction
	 */
	public MLoginMsg(Ctx ctx, int AD_LoginMsg_ID, Trx trx)
	{
		super(ctx, AD_LoginMsg_ID, trx);
	}	//	MLoginMsg

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MLoginMsg(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MLoginMsg

	/** Implementation				*/
	private LoginMsgInterface	m_impl = null;

	/**
	 * 	Send the login message to the user
	 *	@param int AD_LoginMsg_ID login message id
	 *	@param AD_User_ID user id
	 *	@return should that message be sent to that user?
	 */
	public boolean isDisplayMsg(int AD_User_ID, boolean isAdministrator)
	{
		LoginMsgInterface impl = getImplementation();
		if (impl != null)
			return impl.isDisplayMsg(getAD_LoginMsg_ID(), AD_User_ID, isAdministrator);
		return true;
	}	//	isDisplayMsg

	/**
	 * 	Get optional additional text added to the message
	 *	@param int AD_LoginMsg_ID login message id
	 *	@param AD_User_ID user id
	 *	@return null or message
	 */
	public String getAdditionalText(int AD_User_ID)
	{
		LoginMsgInterface impl = getImplementation();
		if (impl != null)
			return impl.getAdditionalText(getAD_LoginMsg_ID(), AD_User_ID);
		return null;
	}	//	getAdditionalText

	/**
	 * 	Get Login Msg Implementation
	 *	@return implementation or null
	 */
	private LoginMsgInterface getImplementation()
	{
		if (m_impl != null)
			return m_impl;
		String className = getClassname();
		if (Util.isEmpty(className))
			return null;
		try
		{
			Class<?> clazz = Class.forName(className);
			m_impl = (LoginMsgInterface)clazz.newInstance();
		}
		catch (Exception e)
		{
			log.warning(className + ": " + e.getMessage());
			return null;
		}
		return m_impl;
	}	//	getImplementation

	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MLoginMsg[")
	    	.append(get_ID()).append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString

}	//	MLoginMsg
