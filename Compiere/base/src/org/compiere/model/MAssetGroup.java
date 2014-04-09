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

import org.compiere.util.*;

/**
 * 	Asset Group Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAssetGroup.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MAssetGroup extends X_A_Asset_Group
{
    /** Logger for class MAssetGroup */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAssetGroup.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get from Cache
	 *	@param ctx context
	 *	@param A_Asset_Group_ID id
	 *	@return group
	 */
	public static MAssetGroup get (Ctx ctx, int A_Asset_Group_ID)
	{
		Integer ii = Integer.valueOf (A_Asset_Group_ID);
		MAssetGroup pc = s_cache.get(ctx, ii);
		if (pc == null)
			pc = new MAssetGroup (ctx, A_Asset_Group_ID, null);
		return pc;
	}	//	get

	/**
	 * 	Get all Groups of Client
	 *	@param ctx context
	 *	@return list of groups
	 */
	public static ArrayList<MAssetGroup> getAll (Ctx ctx)
	{
		String sql = "SELECT * FROM A_Asset_Group "
			+ "WHERE AD_Client_ID=?";
		ArrayList<MAssetGroup> list = new ArrayList<MAssetGroup>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

        try
        {
	        pstmt = DB.prepareStatement(sql, (Trx) null);
	        pstmt.setInt(1, ctx.getAD_Client_ID());
	        rs = pstmt.executeQuery();
	        while (rs.next())
	        	list.add(new MAssetGroup(ctx, rs, null));
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE, sql, e);
        }
        finally
        {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }
        return list;
	}	//	get

	/**	Category Cache				*/
	private static final CCache<Integer,MAssetGroup>	
		s_cache = new CCache<Integer,MAssetGroup> ("A_Asset_Group", 10);

	/**	Logger	*/
    private static CLogger s_log = CLogger.getCLogger(MAssetGroup.class);
    
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param A_Asset_Group_ID id
	 *	@param trx p_trx
	 */
	public MAssetGroup (Ctx ctx, int A_Asset_Group_ID, Trx trx)
	{
		super (ctx, A_Asset_Group_ID, trx);
		if (A_Asset_Group_ID == 0)
		{
		//	setName (null);
			setIsDepreciated (false);
			setIsOneAssetPerUOM (false);
			setIsOwned (false);
			setIsCreateAsActive(true);
			setIsTrackIssues(false);
			setSupportLevel (SUPPORTLEVEL_Unsupported);
		}
	}	//	MAssetGroup

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MAssetGroup (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MAssetGroup
	
	/**
	 * 	Get SupportLevel
	 *	@return support level or Unsupported
	 */
	@Override
	public String getSupportLevel()
	{
		String ss = super.getSupportLevel();
		if (ss == null)
			ss = SUPPORTLEVEL_Unsupported;
		return ss;
	}	//	getSupportLevel
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
    public String toString()
    {
	    StringBuffer sb = new StringBuffer("MAssetGroup[")
	    	.append(get_ID()).append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
	
}	//	MAssetGroup
