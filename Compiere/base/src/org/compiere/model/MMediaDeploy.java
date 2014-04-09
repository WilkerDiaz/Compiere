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
 * 	Media Deployment Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MMediaDeploy.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MMediaDeploy extends X_CM_MediaDeploy
{
    /** Logger for class MMediaDeploy */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMediaDeploy.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param CM_MediaDeploy_ID id
	 *	@param trx transaction
	 */
	public MMediaDeploy (Ctx ctx, int CM_MediaDeploy_ID, Trx trx)
	{
		super (ctx, CM_MediaDeploy_ID, trx);
	}	//	MMediaDeploy

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx tansaction
	 */
	public MMediaDeploy (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MMediaDeploy
	
	/**
	 * 	Deployment Parent Constructor
	 *	@param server server
	 *	@param media media
	 */
	public MMediaDeploy (MMediaServer server, MMedia media)
	{
		this (server.getCtx(), 0, server.get_Trx());
		setCM_Media_Server_ID(server.getCM_Media_Server_ID());
		setCM_Media_ID(media.getCM_Media_ID());
		setClientOrg(server);
		//
		setIsDeployed(true);
		setLastSynchronized(new Timestamp(System.currentTimeMillis()));
	}	//	MMediaDeploy
	
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MMedia.class);
	
	/**
	 * 	getByMediaAndProject Get All deployers by Media ID and WebProject
	 *	@param ctx Context
	 *	@param CM_Media_ID Media ID
	 *	@param thisProject WebProject
	 *	@param createIfMissing Whether we create or not
	 *	@param trx Transaction
	 *	@return Array of MMediaDeploy
	 */
	public static MMediaDeploy [] getByMediaAndProject(Ctx ctx, int CM_Media_ID, MWebProject thisProject, boolean createIfMissing, Trx trx) 
	{
		ArrayList<MMediaDeploy> list = new ArrayList<MMediaDeploy>();
		MMediaServer[] theseServers = MMediaServer.getMediaServer (thisProject);
		if (theseServers!=null && theseServers.length>0)
			for (MMediaServer element : theseServers) {
				list.add (getByMedia(ctx,CM_Media_ID,element.get_ID (),createIfMissing, trx));
			}
		MMediaDeploy[] retValue = new MMediaDeploy[list.size ()];
		list.toArray (retValue);
		return retValue;
	}
	
	/** m_mserver 	contains current MMediaServer */
	private MMediaServer m_mserver = null;
	
	/**
	 * 	getServer getCurrentMediaServer
	 *	@return MMediaServer
	 */
	public MMediaServer getServer() 
	{
		if (m_mserver==null)
			m_mserver = new MMediaServer(getCtx(), getCM_Media_Server_ID (), get_Trx());
		return m_mserver;
	}
	
	/**
	 * 	getByMedia returns MMediaDeploy Object corresponding to an MMedia Item
	 *	@param ctx
	 *	@param CM_Media_ID ID of Media Item
	 *	@param CM_Media_Server_ID Server ID
	 *  @param createIfMissing Add Missing Entries
	 *	@param trx
	 *	@return MMediaDeploy Object or NULL if not existant
	 */
	public static MMediaDeploy getByMedia(Ctx ctx, int CM_Media_ID, int CM_Media_Server_ID, boolean createIfMissing, Trx trx) {
		MMediaDeploy thisMMediaDeploy = null;
		String sql = "SELECT * FROM CM_MediaDeploy WHERE CM_Media_ID=? AND CM_Media_Server_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt (1, CM_Media_ID);
			pstmt.setInt (2, CM_Media_Server_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				thisMMediaDeploy = (new MMediaDeploy(ctx, rs, trx));
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
		if (thisMMediaDeploy == null && createIfMissing)
		{
			thisMMediaDeploy = new MMediaDeploy(ctx, 0, trx);
			thisMMediaDeploy.setCM_Media_Server_ID (CM_Media_Server_ID);
			thisMMediaDeploy.setCM_Media_ID (CM_Media_ID);
			thisMMediaDeploy.setIsDeployed (false);
			thisMMediaDeploy.setLastSynchronized (null);
			thisMMediaDeploy.save ();
		}	
		return thisMMediaDeploy;
	}
	
}	//	MMediaDeploy
