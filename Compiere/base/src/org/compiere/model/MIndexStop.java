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
 * 	Text Search Stop Keyword Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MIndexStop.java,v 1.3 2006/08/09 16:38:25 jjanke Exp $
 */
public class MIndexStop extends X_K_IndexStop
{
    /** Logger for class MIndexStop */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MIndexStop.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param K_IndexStop_ID id
	 *	@param trx transaction
	 */
	public MIndexStop (Ctx ctx, int K_IndexStop_ID, Trx trx)
	{
		super (ctx, K_IndexStop_ID, trx);
	}	//	MIndexStop

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MIndexStop (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MIndexStop
	
	/**
	 * 	Set Keyword & standardize
	 *	@param Keyword
	 */
	@Override
	public void setKeyword (String Keyword)
	{
		String kw = MIndex.standardizeKeyword(Keyword);
		if (kw == null)
			kw = "?";
		super.setKeyword (kw);
	}	//	setKeyword
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord || is_ValueChanged("Keyword"))
			setKeyword(getKeyword());
		if (getKeyword().equals("?"))
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "Keyword"));
			return false;
		}
		return true;
	}	//	beforeSave

}	//	MIndexStop
