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
 *	Invoice Tax Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInvoiceTax.java,v 1.5 2006/10/06 00:42:24 jjanke Exp $
 */
public class MInvoiceTax extends X_C_InvoiceTax
{
    /** Logger for class MInvoiceTax */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInvoiceTax.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Tax Line for Invoice Line
	 *	@param line invoice line
	 *	@param precision currency precision
	 *	@param oldTax if true old tax is returned
	 *	@param trx transaction name
	 *	@return existing or new tax
	 */
	@Deprecated
	public static MInvoiceTax get (MInvoiceLine line, int precision, 
		boolean oldTax, Trx trx)
	{
		if (line == null || line.getC_Invoice_ID() == 0 || line.isDescription())
			return null;
		int C_Tax_ID = line.getC_Tax_ID();
		if (oldTax && line.is_ValueChanged("C_Tax_ID"))
		{
			Object old = line.get_ValueOld("C_Tax_ID");
			if (old == null)
				return null;
			C_Tax_ID = ((Integer)old).intValue();
		}
		if (C_Tax_ID == 0)
		{
			s_log.warning("C_Tax_ID=0");
			return null;
		}
		
		return get(line.getCtx(), line.getC_Invoice_ID(), C_Tax_ID, line
				.getPrecision(), line.get_Trx(), line.getAD_Client_ID(), line
				.getAD_Org_ID(), line.isTaxIncluded());
	}		
	
	
	/**
	 * 	Get Tax Line for Invoice Line
	 * @param ctx
	 * @param C_Invoice_ID
	 * @param C_Tax_ID
	 * @param precision
	 * @param trx
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @param isTaxIncluded
	 * @return
	 */
	public static MInvoiceTax get(Ctx ctx, int C_Invoice_ID, int C_Tax_ID, int precision, Trx trx, int AD_Client_ID,
			int AD_Org_ID, boolean isTaxIncluded) {
		
		MInvoiceTax retValue = null;
		
		String sql = "SELECT * FROM C_InvoiceTax WHERE C_Invoice_ID=? AND C_Tax_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_Invoice_ID);
			pstmt.setInt (2, C_Tax_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MInvoiceTax (ctx, rs, trx);
			}
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
			retValue.set_Trx(trx);
			retValue.setPrecision(precision);
			s_log.fine("(old) " + retValue);
			return retValue;
		}
		
		//	Create New
		retValue = new MInvoiceTax(ctx, 0, trx);
		retValue.set_Trx(trx);
		retValue.setClientOrg(AD_Client_ID, AD_Org_ID);
		retValue.setC_Invoice_ID(C_Invoice_ID);
		retValue.setC_Tax_ID(C_Tax_ID);
		retValue.setPrecision(precision);
		retValue.setIsTaxIncluded(isTaxIncluded);
		s_log.fine("(new) " + retValue);
		return retValue;
	}	//	get
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInvoiceTax.class);
	
	
	/**************************************************************************
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MInvoiceTax (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		setTaxAmt (Env.ZERO);
		setTaxBaseAmt (Env.ZERO);
		setIsTaxIncluded(false);
	}	//	MInvoiceTax

	/**
	 * 	Load Constructor.
	 * 	Set Precision and TaxIncluded for tax calculations!
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MInvoiceTax (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MInvoiceTax
	
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
	 * 	Calculate/Set Tax Base Amt from Invoice Lines
	 * 	@return true if tax calculated
	 */
	public boolean calculateTaxFromLines ()
	{
		BigDecimal taxBaseAmt = Env.ZERO;
		BigDecimal taxAmt = Env.ZERO;
		//
		boolean documentLevel = getTax().isDocumentLevel();
		MTax tax = getTax();
		//
		String sql = "SELECT il.LineNetAmt, COALESCE(il.TaxAmt,0), i.IsSOTrx "
			+ "FROM C_InvoiceLine il"
			+ " INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID) "
			+ "WHERE il.C_Invoice_ID=? AND il.C_Tax_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_Invoice_ID());
			pstmt.setInt (2, getC_Tax_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				//	BaseAmt
				BigDecimal baseAmt = rs.getBigDecimal(1);
				taxBaseAmt = taxBaseAmt.add(baseAmt);
				//	TaxAmt
				BigDecimal amt = rs.getBigDecimal(2);
				if (amt == null)
					amt = Env.ZERO;
				boolean isSOTrx = "Y".equals(rs.getString(3));
				//
				if (documentLevel || baseAmt.signum() == 0)
					amt = Env.ZERO;
				else if (amt.signum() != 0 && !isSOTrx)	//	manually entered
					;
				else	// calculate line tax
					amt = tax.calculateTax(baseAmt, isTaxIncluded(), getPrecision());
				//
				taxAmt = taxAmt.add(amt);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "setTaxBaseAmt", e);
			taxBaseAmt = null;
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		if (taxBaseAmt == null)
			return false;
		
		//	Calculate Tax
		if (documentLevel || taxAmt.signum() == 0)
			taxAmt = tax.calculateTax(taxBaseAmt, isTaxIncluded(), getPrecision());
		setTaxAmt(taxAmt);

		//	Set Base
		if (isTaxIncluded())
			setTaxBaseAmt (taxBaseAmt.subtract(taxAmt));
		else
			setTaxBaseAmt (taxBaseAmt);
		return true;
	}	//	calculateTaxFromLines

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInvoiceTax[");
		sb.append("C_Invoice_ID=").append(getC_Invoice_ID())
			.append(",C_Tax_ID=").append(getC_Tax_ID())
			.append(", Base=").append(getTaxBaseAmt()).append(",Tax=").append(getTaxAmt())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MInvoiceTax
