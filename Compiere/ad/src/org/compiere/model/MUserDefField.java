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

import java.math.*;
import java.sql.*;

import org.compiere.common.CompiereStateException;
import org.compiere.util.*;


/**
 *	User Defined Field Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MUserDefField.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MUserDefField extends X_AD_UserDef_Field
{
    /** Logger for class MUserDefField */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MUserDefField.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_UserDef_Field_ID ix
	 *	@param trx p_trx
	 */
	public MUserDefField(Ctx ctx, int AD_UserDef_Field_ID, Trx trx)
	{
		super (ctx, AD_UserDef_Field_ID, trx);
	}	//	MUserDefField

	/**
	 * 	Load Constructpr
	 *	@param ctx ctx
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MUserDefField(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MUserDefField
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MUserDefField (MUserDefTab parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg (parent);
		setAD_UserDef_Tab_ID (parent.getAD_UserDef_Tab_ID());
	}	//	MUserDefField
	
	/**
	 * 	Create from Field
	 *	@param field field
	 *	@return true if created
	 */
	protected boolean create (MField field)
	{
		if (getAD_UserDef_Field_ID() != 0)
			throw new CompiereStateException("Needs to be new");
		
		setAD_Field_ID(field.getAD_Field_ID());
		//	Sorting
		BigDecimal sn = field.getSortNo();
		if (sn.signum() != 0)
			setSortNo(sn.intValue());
		//	Sequence: SingleRow - Multi-Row 
		setIsDisplayed (field.isDisplayed() ? "Y" : "N");
		int seqNo = field.getSeqNo();
		setSeqNo(seqNo);
		if (field.getMRSeqNo() != 0)
			setMRSeqNo (field.getMRSeqNo());
		else
			setMRSeqNo (seqNo);
		
		//	
		MColumn column = field.getColumn();
		if (column.isSelectionColumn())
		{
			setIsSelectionColumn ("Y");
			setSelectionSeqNo (column.getSelectionSeqNo());
		}
		if (column.isSummaryColumn())
		{
			setIsSummaryColumn ("Y");
			setSummarySeqNo (column.getSummarySeqNo());
		}
		
		return save();
	}	//	create
	
}	//	MUserDefField
