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
 *	Accounting Element Model.
 *	
 *  @author Jorg Janke
 *  @version $Id: MElement.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MElement extends X_C_Element
{
    /** Logger for class MElement */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MElement.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Accounting Element from Cache
	 *	@param ctx context
	 *	@param _ID id
	 *	@return MElement
	 */
	public static MElement get(Ctx ctx, int AD_Element_ID)
	{
		Integer key = Integer.valueOf (AD_Element_ID);
		MElement retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MElement (ctx, AD_Element_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static final CCache<Integer, MElement> s_cache 
		= new CCache<Integer, MElement> ("AD_Element", 20);
	
	/**************************************************************************
	 * 	Standard Accounting Element Constructor
	 *	@param ctx context
	 *	@param C_Element_ID id
	 *	@param trx transaction
	 */
	public MElement (Ctx ctx, int C_Element_ID, Trx trx)
	{
		super(ctx, C_Element_ID, trx);
		if (C_Element_ID == 0)
		{
		//	setName (null);
		//	setAD_Tree_ID (0);
		//	setElementType (null);	// A
			setIsBalancing (false);
			setIsNaturalAccount (false);
		}
	}	//	MElement

	/**
	 * 	Accounting Element Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MElement (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MElement

	/**
	 * 	Full Constructor
	 *	@param client client
	 *	@param Name name
	 *	@param ElementType type
	 *	@param AD_Tree_ID tree
	 */
	public MElement (MClient client, String Name, String ElementType, int AD_Tree_ID)
	{
		this (client.getCtx(), 0, client.get_Trx());
		setClientOrg(client);
		setName (Name);
		setElementType (ElementType);	// A
		setAD_Tree_ID (AD_Tree_ID);
		setIsNaturalAccount(ELEMENTTYPE_Account.equals(ElementType));
	}	//	MElement

	/** Tree Used			*/
	private X_AD_Tree	m_tree = null;
	
	/**
	 * 	Get Tree
	 *	@return tree
	 */
	public X_AD_Tree getTree ()
	{
		if (m_tree == null)
			m_tree = new X_AD_Tree (getCtx(), getAD_Tree_ID(), get_Trx());
		return m_tree;
	}	//	getTree
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		String elementType = getElementType();
		//	Natural Account
		if (ELEMENTTYPE_UserDefined.equals(elementType) && isNaturalAccount())
			setIsNaturalAccount(false);
		//	Tree validation
		X_AD_Tree tree = getTree();
		if (tree == null)
			return false;
		String treeType = tree.getTreeType();
		if (ELEMENTTYPE_UserDefined.equals(elementType))
		{
			if (X_AD_Tree.TREETYPE_User1.equals(treeType) || X_AD_Tree.TREETYPE_User2.equals(treeType))
				;
			else
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@TreeType@ <> @ElementType@ (U)"), false);
				return false;
			}
		}
		else
		{
			if (!X_AD_Tree.TREETYPE_ElementValue.equals(treeType))
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@TreeType@ <> @ElementType@ (A)"), false);
				return false;
			}
		}
		return true;
	}	//	beforeSave
	
}	//	MElement
