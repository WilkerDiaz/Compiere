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
 *	(Disk) Tree Node Model CM Container
 *	
 *  @author Jorg Janke
 *  @version $Id: MTreeNodeCMC.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MTreeNodeCMC extends X_AD_TreeNodeCMC
{
    /** Logger for class MTreeNodeCMC */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTreeNodeCMC.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Tree
	 *	@param ctx context
	 *	@param AD_Tree_ID tree
	 *	@param trx transaction
	 *	@return array of nodes
	 */
	public static MTreeNodeCMC[] getTree (Ctx ctx, int AD_Tree_ID, Trx trx)
	{
		ArrayList<MTreeNodeCMC> list = new ArrayList<MTreeNodeCMC>();
		String sql = "SELECT * FROM AD_TreeNodeCMC WHERE AD_Tree_ID=? ORDER BY Node_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, AD_Tree_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MTreeNodeCMC (ctx, rs, trx));
			}
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MTreeNodeCMC[] retValue = new MTreeNodeCMC[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getTree
	
	
	/**
	 * 	Get Tree Node
	 *	@param tree tree
	 *	@param Node_ID node
	 *	@return node or null
	 */
	public static MTreeNodeCMC get (MTree tree, int Node_ID)
	{
		MTreeNodeCMC retValue = null;
		String sql = "SELECT * FROM AD_TreeNodeCMC WHERE AD_Tree_ID=? AND Node_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, tree.get_Trx());
			pstmt.setInt (1, tree.getAD_Tree_ID());
			pstmt.setInt (2, Node_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MTreeNodeCMC (tree.getCtx(), rs, tree.get_Trx());
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, "get", e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MTreeNodeCMC.class);

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MTreeNodeCMC (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MTreeNodeCMC

	/**
	 * 	Full Constructor
	 *	@param tree tree
	 *	@param Node_ID node
	 */
	public MTreeNodeCMC (MTree tree, int Node_ID)
	{
		super (tree.getCtx(), 0, tree.get_Trx());
		setClientOrg(tree);
		setAD_Tree_ID (tree.getAD_Tree_ID());
		setNode_ID(Node_ID);
		//	Add to root
		setParent_ID(0);
		setSeqNo (0);
	}	//	MTreeNodeCMC
	
	/**
	 * 	setParent_ID overwrite as Tree's need to allow 0 parents
	 *	@see org.compiere.model.X_AD_TreeNodeCMC#setParent_ID(int)
	 *	@param Parent_ID
	 */
	@Override
	public void setParent_ID (int Parent_ID)
	{
		set_Value ("Parent_ID", Integer.valueOf(Parent_ID));
	}
}	//	MTreeNodeCMC
