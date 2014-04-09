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
import org.compiere.util.*;

/**
 *  Process Instance Parameter Model
 *
 *  @author Jorg Janke
 *  @version $Id: MPInstancePara.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MPInstancePara extends X_AD_PInstance_Para
{
    /** Logger for class MPInstancePara */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPInstancePara.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MPInstancePara (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MPInstance_Para
	
	/**
	 * 	Parent Constructor
	 *	@param ctx
	 *	@param AD_PInstance_ID id
	 *	@param SeqNo sequence
	 */
	public MPInstancePara (Ctx ctx, int AD_PInstance_ID, int SeqNo)
	{
		super(ctx, 0, null);
		setAD_PInstance_ID (AD_PInstance_ID);
		setSeqNo (SeqNo);
	}	//	MPInstance_Para

	/**
	 * 	Parent Constructor
	 *	@param instance instance
	 *	@param SeqNo sequence
	 */
	public MPInstancePara (MPInstance instance, int SeqNo)
	{
		super (instance.getCtx(), 0, instance.get_Trx());
		setAD_PInstance_ID (instance.getAD_PInstance_ID());
		setSeqNo (SeqNo);
	}	//	MPInstance_Para

	
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPInstancePara (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPInstance_Para

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPInstancePara[")
			.append (get_ID ()).append("-").append(getParameterName());
		if (getP_String() != null)
		{
			sb.append("(s)=").append(getP_String());
			if (getP_String_To() != null)
				sb.append(" - ").append(getP_String_To());
		}
		BigDecimal bd = (BigDecimal)get_Value("P_Number");
		if (bd != null)
		{
			sb.append("(n)=").append(bd);
			BigDecimal bd2 = (BigDecimal)get_Value("P_Number_To");
			if (bd2 != null)
				sb.append(" - ").append(bd2);
		}
		if (getP_Date() != null)
		{
			sb.append("(d)=").append(getP_Date());
			if (getP_Date_To() != null)
				sb.append(" - ").append(getP_Date_To());
		}
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Set P_Number
	 *	@param P_Number no
	 */
	public void setP_Number (int P_Number)
	{
		setP_Number (new BigDecimal(P_Number));
	}	//	setP_Number

	/**
	 * 	Set P_Number
	 *	@param P_Number no
	 */
	public void setP_Number (Integer P_Number)
	{
		if (P_Number == null)
			setP_Number(0);
		else
			setP_Number ((P_Number).intValue());
	}	//	setP_Number
	
	/**
	 * 	Set P_Number To
	 *	@param P_Number_To no to
	 */
	public void setP_Number_To (int P_Number_To)
	{
		setP_Number_To (new BigDecimal(P_Number_To));
	}	//	setP_Number_To

	/**
	 * 	Set P_Number To
	 *	@param P_Number_To no to
	 */
	public void setP_Number_To (Integer P_Number_To)
	{
		if (P_Number_To == null)
			setP_Number_To(0);
		else
			setP_Number_To ((P_Number_To).intValue());
	}	//	setP_Number_To

	
	/**
	 * 	Set String Parameter
	 *	@param parameterName name
	 *	@param stringParameter value
	 */
	public void setParameter (String parameterName, String stringParameter)
	{
		setParameterName(parameterName);
		setP_String(stringParameter);
	}	//	setParameter
	
	/**
	 * 	Set Number Parameter
	 *	@param parameterName name
	 *	@param bdParameter value
	 */
	public void setParameter (String parameterName, BigDecimal bdParameter)
	{
		setParameterName(parameterName);
		setP_Number(bdParameter);
	}	//	setParameter
	
	/**
	 * 	Set Number Parameter
	 *	@param parameterName name
	 *	@param iParameter value
	 */
	public void setParameter (String parameterName, int iParameter)
	{
		setParameterName(parameterName);
		setP_Number(new BigDecimal(iParameter));
	}	//	setParameter
	
}	//	MPInstance_Para
