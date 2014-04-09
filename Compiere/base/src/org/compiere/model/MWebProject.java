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

import org.compiere.util.*;

/**
 * 	Web Project Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MWebProject.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MWebProject extends X_CM_WebProject
{
    /** Logger for class MWebProject */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWebProject.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MWebProject from Cache
	 *	@param ctx context
	 *	@param CM_WebProject_ID id
	 *	@return MWebProject
	 */
	public static MWebProject get (Ctx ctx, int CM_WebProject_ID)
	{
		Integer key = Integer.valueOf (CM_WebProject_ID);
		MWebProject retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MWebProject (ctx, CM_WebProject_ID, null);
		if (retValue.get_ID () == CM_WebProject_ID)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static final CCache<Integer, MWebProject> s_cache 
		= new CCache<Integer, MWebProject> ("CM_WebProject", 5);
	
	
	/**************************************************************************
	 * 	Web Project
	 *	@param ctx context
	 *	@param CM_WebProject_ID id
	 *	@param trx transaction
	 */
	public MWebProject (Ctx ctx, int CM_WebProject_ID, Trx trx)
	{
		super (ctx, CM_WebProject_ID, trx);
	}	//	MWebProject

	/**
	 * 	Web Project
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MWebProject (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MWebProject
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Create Trees
		if (newRecord)
		{
			MTree tree = new MTree (getCtx(), 
				getName()+X_AD_Tree.TREETYPE_CMContainer, X_AD_Tree.TREETYPE_CMContainer, get_Trx());
			if (!tree.save())
				return false;
			setAD_TreeCMC_ID(tree.getAD_Tree_ID());
			//
			tree = new MTree (getCtx(), 
				getName()+X_AD_Tree.TREETYPE_CMContainerStage, X_AD_Tree.TREETYPE_CMContainerStage, get_Trx());
			if (!tree.save())
				return false;
			setAD_TreeCMS_ID(tree.getAD_Tree_ID());
			//
			tree = new MTree (getCtx(), 
				getName()+X_AD_Tree.TREETYPE_CMTemplate, X_AD_Tree.TREETYPE_CMTemplate, get_Trx());
			if (!tree.save())
				return false;
			setAD_TreeCMT_ID(tree.getAD_Tree_ID());
			//
			tree = new MTree (getCtx(), 
				getName()+X_AD_Tree.TREETYPE_CMMedia, X_AD_Tree.TREETYPE_CMMedia, get_Trx());
			if (!tree.save())
				return false;
			setAD_TreeCMM_ID(tree.getAD_Tree_ID());
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Insert
	 * 	- create tree
	 *	@param newRecord insert
	 *	@param success save success
	 *	@return true if saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (!newRecord)
		{
			// Clean Web Project Cache
		}
		return success;
	}	//	afterSave
	
}	//	MWebProject
