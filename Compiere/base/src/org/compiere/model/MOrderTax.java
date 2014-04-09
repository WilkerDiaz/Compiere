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
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Order Tax Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MOrderTax.java,v 1.4 2006/07/30 00:51:04 jjanke Exp $
 */
public class MOrderTax extends X_C_OrderTax
{
    /** Logger for class MOrderTax */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MOrderTax.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Tax Line 
	 * @param ctx
	 * @param C_Order_ID
	 * @param C_Tax_ID
	 * @param precision currency precision
	 * @param trx transaction
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @param isTaxIncluded
	 * @return existing or new tax
	 */	
	public static MOrderTax get(Ctx ctx, int C_Order_ID, int C_Tax_ID,
			int precision, Trx trx, int AD_Client_ID, int AD_Org_ID,
			boolean isTaxIncluded)	{
		MOrderTax retValue = null;
		if (C_Tax_ID == 0)
		{
			s_log.fine("No Tax");
			return null;
		}
		
		String sql = "SELECT * FROM C_OrderTax WHERE C_Order_ID=? AND C_Tax_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_Order_ID);
			pstmt.setInt (2, C_Tax_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MOrderTax (ctx, rs, trx);
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
		if (retValue != null)
		{
			retValue.setPrecision(precision);
			retValue.set_Trx(trx);
			s_log.fine("(old) " + retValue);
			return retValue;
		}
		
		//	Create New
		retValue = new MOrderTax(ctx, 0, trx);
		retValue.set_Trx(trx);
		retValue.setClientOrg(AD_Client_ID, AD_Org_ID);
		retValue.setC_Order_ID(C_Order_ID);
		retValue.setC_Tax_ID(C_Tax_ID);
		retValue.setPrecision(precision);
		retValue.setIsTaxIncluded(isTaxIncluded);
		s_log.fine("(new) " + retValue);
		return retValue;
	}	//	get
	
	
	/**
	 * 	Get Tax Line for Order Line
	 *	@param line Order line
	 *	@param precision currenct precision
	 *	@param oldTax get old tax
	 *	@param trx transaction
	 *	@return existing or new tax
	 */
	@Deprecated
	public static MOrderTax get (MOrderLine line, int precision, 
		boolean oldTax, Trx trx)
	{
		if (line == null || line.getC_Order_ID() == 0)
		{
			s_log.fine("No Order");
			return null;
		}
		int C_Tax_ID = line.getC_Tax_ID();
		if (oldTax && line.is_ValueChanged("C_Tax_ID"))
		{
			Object old = line.get_ValueOld("C_Tax_ID");
			if (old == null)
			{
				s_log.fine("No Old Tax");
				return null;
			}
			C_Tax_ID = ((Integer)old).intValue();
		}
		return get(line.getCtx(), line.getC_Order_ID(), C_Tax_ID, line
				.getPrecision(), line.get_Trx(), line.getAD_Client_ID(), line
				.getAD_Org_ID(), line.isTaxIncluded());
	}	//	get
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MOrderTax.class);
	
	
	/**************************************************************************
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MOrderTax (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		setTaxAmt (Env.ZERO);
		setTaxBaseAmt (Env.ZERO);
		setIsTaxIncluded(false);
	}	//	MOrderTax

	/**
	 * 	Load Constructor.
	 * 	Set Precision and TaxIncluded for tax calculations!
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MOrderTax (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MOrderTax
	
	/** Tax							*/
	private MTax 		m_tax = null;
	/** Cached Precision			*/
	private Integer		m_precision = null;

	/**
	 * 	Get Precision
	 * 	@return Returns the precision or 2
	 */
	private int getPrecision ()
	{
		if (m_precision == null)
			return 2;
		return m_precision.intValue();
	}	//	getPrecision

	/**
	 * 	Set Precision
	 *	@param precision The precision to set.
	 */
	protected void setPrecision (int precision)
	{
		m_precision = Integer.valueOf(precision);
	}	//	setPrecision

	/**
	 * 	Get Tax
	 *	@return tax
	 */
	protected MTax getTax()
	{
		if (m_tax == null)
			m_tax = MTax.get(getCtx(), getC_Tax_ID());
		return m_tax;
	}	//	getTax

	
	/**************************************************************************
	 * 	Calculate/Set Tax Amt from Order Lines
	 * 	@return true if aclculated
	 */
	public boolean calculateTaxFromLines ()
	{
		BigDecimal taxBaseAmt = Env.ZERO;
		BigDecimal taxAmt = Env.ZERO;
		//
		boolean documentLevel = getTax().isDocumentLevel();
		MTax tax = getTax();
		//
		String sql = "SELECT LineNetAmt FROM C_OrderLine WHERE C_Order_ID=? AND C_Tax_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_Order_ID());
			pstmt.setInt (2, getC_Tax_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				BigDecimal baseAmt = rs.getBigDecimal(1);
				taxBaseAmt = taxBaseAmt.add(baseAmt);
				//
				if (!documentLevel)		// calculate line tax
					taxAmt = taxAmt.add(tax.calculateTax(baseAmt, isTaxIncluded(), getPrecision()));
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, get_Trx().toString(), e);
			taxBaseAmt = null;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (taxBaseAmt == null)
			return false;
		
		//	Calculate Tax
		if (documentLevel)		//	document level
			taxAmt = tax.calculateTax(taxBaseAmt, isTaxIncluded(), getPrecision());
		setTaxAmt(taxAmt);

		//	Set Base
		if (isTaxIncluded())
			setTaxBaseAmt (taxBaseAmt.subtract(taxAmt));
		else
			setTaxBaseAmt (taxBaseAmt);
		log.fine(toString());
		return true;
	}	//	calculateTaxFromLines

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MOrderTax[");
		sb.append("C_Order_ID=").append(getC_Order_ID())
			.append(",C_Tax_ID=").append(getC_Tax_ID())
			.append(", Base=").append(getTaxBaseAmt()).append(",Tax=").append(getTaxAmt())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MOrderTax
