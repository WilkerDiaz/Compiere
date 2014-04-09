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
import java.util.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.*;
import org.compiere.util.*;


/**
 *	Invoice Line Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInvoiceLine.java,v 1.5 2006/07/30 00:51:03 jjanke Exp $
 */
public class MInvoiceLine extends X_C_InvoiceLine
{
    /** Logger for class MInvoiceLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInvoiceLine.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Invoice Line referencing InOut Line
	 *	@param sLine shipment line
	 *	@return (first) invoice line
	 */
	public static MInvoiceLine getOfInOutLine (MInOutLine sLine)
	{
		if (sLine == null)
			return null;
		MInvoiceLine retValue = null;
		String sql = "SELECT * FROM C_InvoiceLine WHERE M_InOutLine_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, sLine.get_Trx());
			pstmt.setInt (1, sLine.getM_InOutLine_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MInvoiceLine (sLine.getCtx(), rs, sLine.get_Trx());
				if (rs.next())
					s_log.warning("More than one C_InvoiceLine of " + sLine);
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
		return retValue;
	}	//	getOfInOutLine

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInvoiceLine.class);


	/**************************************************************************
	 * 	Invoice Line Constructor
	 * 	@param ctx context
	 * 	@param C_InvoiceLine_ID invoice line or 0
	 * 	@param trx transaction name
	 */
	public MInvoiceLine (Ctx ctx, int C_InvoiceLine_ID, Trx trx)
	{
		super (ctx, C_InvoiceLine_ID, trx);
		if (C_InvoiceLine_ID == 0)
		{
			setIsDescription(false);
			setIsPrinted (true);
			setLineNetAmt (Env.ZERO);
			setPriceEntered (Env.ZERO);
			setPriceActual (Env.ZERO);
			setPriceLimit (Env.ZERO);
			setPriceList (Env.ZERO);
			setM_AttributeSetInstance_ID(0);
			setTaxAmt(Env.ZERO);
			//
			setQtyEntered(Env.ZERO);
			setQtyInvoiced(Env.ZERO);
		}
	}	//	MInvoiceLine

	/**
	 * 	Parent Constructor
	 * 	@param invoice parent
	 */
	public MInvoiceLine (MInvoice invoice)
	{
		this (invoice.getCtx(), 0, invoice.get_Trx());
		if (invoice.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setClientOrg(invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
		setC_Invoice_ID (invoice.getC_Invoice_ID());
		setInvoice(invoice);
	}	//	MInvoiceLine

	private int I_Invoice_ID=0;
	
	public MInvoiceLine (MInvoice invoice, int p_X_I_Invoice_ID)
	{
		this (invoice.getCtx(), 0, invoice.get_Trx());
		setClientOrg(invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
		if (invoice.get_ID() != 0)
			setC_Invoice_ID (invoice.getC_Invoice_ID());
		setInvoice(invoice);
		I_Invoice_ID = p_X_I_Invoice_ID;
	}
	
	public int getI_Invoice_ID() {
		return I_Invoice_ID;
	}

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trx transaction
	 */
	public MInvoiceLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MInvoiceLine

	private int			m_M_PriceList_ID = 0;
	private Timestamp	m_DateInvoiced = null;
	private int			m_C_BPartner_ID = 0;
	private int			m_C_BPartner_Location_ID = 0;
	private boolean		m_IsSOTrx = true;
	private boolean		m_priceSet = false;
	private boolean		m_priceFound = false;
	private MProduct	m_product = null;

	/**	Cached Name of the line		*/
	private String		m_name = null;
	/** Cached Precision			*/
	private Integer		m_precision = null;
	/** Product Pricing				*/
	private MProductPricing	m_productPricing = null;
	/** Parent						*/
	private MInvoice	m_parent = null;
	/** Order Line					*/
	private MOrderLine 	m_oLine = null;

	/**
	 * 	Set Defaults from Order.
	 * 	Called also from copy lines from invoice
	 * 	Does not set Parent !!
	 * 	@param invoice invoice
	 */
	public void setInvoice (MInvoice invoice)
	{
		m_parent = invoice;
		m_M_PriceList_ID = invoice.getM_PriceList_ID();
		m_DateInvoiced = invoice.getDateInvoiced();
		m_C_BPartner_ID = invoice.getC_BPartner_ID();
		m_C_BPartner_Location_ID = invoice.getC_BPartner_Location_ID();
		m_IsSOTrx = invoice.isSOTrx();
		m_precision = Integer.valueOf(invoice.getPrecision());
	}	//	setOrder

	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MInvoice getParent()
	{
		if (m_parent == null)
			m_parent = new MInvoice(getCtx(), getC_Invoice_ID(), get_Trx());
		return m_parent;
	}	//	getParent

	/**
	 * 	Set values from Order Line.
	 * 	Does not set quantity!
	 *	@param oLine line
	 */
	public void setOrderLine (MOrderLine oLine)
	{
		m_oLine = oLine;
		setC_OrderLine_ID(oLine.getC_OrderLine_ID());
		//
		setLine(oLine.getLine());
		setIsDescription(oLine.isDescription());
		setDescription(oLine.getDescription());
		//
		setC_Charge_ID(oLine.getC_Charge_ID());
		//
		setM_Product_ID(oLine.getM_Product_ID());
		setM_AttributeSetInstance_ID(oLine.getM_AttributeSetInstance_ID());
		setS_ResourceAssignment_ID(oLine.getS_ResourceAssignment_ID());
		setC_UOM_ID(oLine.getC_UOM_ID());
		//
		setPriceEntered(oLine.getPriceEntered());
		setPriceActual(oLine.getPriceActual());
		setPriceLimit(oLine.getPriceLimit());
		setPriceList(oLine.getPriceList());
		//
		setC_Tax_ID(oLine.getC_Tax_ID());
		setLineNetAmt(oLine.getLineNetAmt());
		//
		setAD_Org_ID(oLine.getAD_Org_ID());
		setC_Project_ID(oLine.getC_Project_ID());
		setC_ProjectPhase_ID(oLine.getC_ProjectPhase_ID());
		setC_ProjectTask_ID(oLine.getC_ProjectTask_ID());
		setC_Activity_ID(oLine.getC_Activity_ID());
		setC_Campaign_ID(oLine.getC_Campaign_ID());
		setAD_OrgTrx_ID(oLine.getAD_OrgTrx_ID());
		setUser1_ID(oLine.getUser1_ID());
		setUser2_ID(oLine.getUser2_ID());
		//
		setRRAmt(oLine.getRRAmt());
		setRRStartDate(oLine.getRRStartDate());
	}	//	setOrderLine

	/**
	 * 	Set values from Shipment Line.
	 * 	Does not set quantity!
	 *	@param sLine ship line
	 */
	public void setShipLine (MInOutLine sLine)
	{
		setM_InOutLine_ID(sLine.getM_InOutLine_ID());
		setC_OrderLine_ID(sLine.getC_OrderLine_ID());
		//
		setLine(sLine.getLine());
		setIsDescription(sLine.isDescription());
		setDescription(sLine.getDescription());
		//
		setM_Product_ID(sLine.getM_Product_ID());
		setC_UOM_ID(sLine.getC_UOM_ID());
		setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
	//	setS_ResourceAssignment_ID(sLine.getS_ResourceAssignment_ID());
		setC_Charge_ID(sLine.getC_Charge_ID());
		//
		int C_OrderLine_ID = sLine.getC_OrderLine_ID();
		if (C_OrderLine_ID != 0)
		{
			getOrderLine();
			setS_ResourceAssignment_ID(m_oLine.getS_ResourceAssignment_ID());
			//
			setPriceEntered(m_oLine.getPriceEntered());
			setPriceActual(m_oLine.getPriceActual());
			setPriceLimit(m_oLine.getPriceLimit());
			setPriceList(m_oLine.getPriceList());
			//
			setC_Tax_ID(m_oLine.getC_Tax_ID());
			setLineNetAmt(m_oLine.getLineNetAmt());
			setC_Project_ID(m_oLine.getC_Project_ID());
		}
		else
		{
			setPrice();
			setTax();
		}
		//
		setAD_Org_ID(sLine.getAD_Org_ID());
		setC_Project_ID(sLine.getC_Project_ID());
		setC_ProjectPhase_ID(sLine.getC_ProjectPhase_ID());
		setC_ProjectTask_ID(sLine.getC_ProjectTask_ID());
		setC_Activity_ID(sLine.getC_Activity_ID());
		setC_Campaign_ID(sLine.getC_Campaign_ID());
		setAD_OrgTrx_ID(sLine.getAD_OrgTrx_ID());
		setUser1_ID(sLine.getUser1_ID());
		setUser2_ID(sLine.getUser2_ID());
	}	//	setShipLine

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 * 	Set M_AttributeSetInstance_ID
	 *	@param M_AttributeSetInstance_ID id
	 */
	@Override
	public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
	{
		if (M_AttributeSetInstance_ID == 0)		//	 0 is valid ID
			set_Value("M_AttributeSetInstance_ID", Integer.valueOf(0));
		else
			super.setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
	}	//	setM_AttributeSetInstance_ID


	/**************************************************************************
	 * 	Set Price for Product and PriceList.
	 * 	Uses standard SO price list of not set by invoice constructor
	 */
	public void setPrice()
	{
		if ((getM_Product_ID() == 0) || isDescription())
		{
			m_priceSet = true;
			return;
		}
		if ((m_M_PriceList_ID == 0) || (m_C_BPartner_ID == 0))
			setInvoice(getParent());
		if ((m_M_PriceList_ID == 0) || (m_C_BPartner_ID == 0))
			throw new CompiereStateException("setPrice - PriceList unknown!");
		setPrice (m_M_PriceList_ID, m_C_BPartner_ID);
	}	//	setPrice

	/**
	 * 	Set Price for Product and PriceList
	 * 	@param M_PriceList_ID price list
	 * 	@param C_BPartner_ID business partner
	 */
	public void setPrice (int M_PriceList_ID, int C_BPartner_ID)
	{
		if ((getM_Product_ID() == 0) || isDescription())
		{
			m_priceSet = true;
			return;
		}
		//
		log.fine("M_PriceList_ID=" + M_PriceList_ID);
		m_productPricing = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
			getM_Product_ID(), C_BPartner_ID, getQtyInvoiced(), m_IsSOTrx);
		m_productPricing.setM_PriceList_ID(M_PriceList_ID);
		m_productPricing.setPriceDate(m_DateInvoiced);

		m_productPricing.calculatePrice();
		if (!m_productPricing.isCalculated())
			return;


		//
		setPriceActual (m_productPricing.getPriceStd());
		setPriceList (m_productPricing.getPriceList());
		setPriceLimit (m_productPricing.getPriceLimit());
		//
		if (getQtyEntered().compareTo(getQtyInvoiced()) == 0)
			setPriceEntered(getPriceActual());
		else
			setPriceEntered(getPriceActual().multiply(getQtyInvoiced()
				.divide(getQtyEntered(), 6, BigDecimal.ROUND_HALF_UP)));	//	precision
		//
		if (getC_UOM_ID() == 0)
			setC_UOM_ID(m_productPricing.getC_UOM_ID());
		//
		m_priceSet = true;
	}	//	setPrice

	/**
	 * 	Set Price Entered/Actual.
	 * 	Use this Method if the Line UOM is the Product UOM
	 *	@param PriceActual price
	 */
	public void setPrice (BigDecimal PriceActual)
	{
		setPriceEntered(PriceActual);
		setPriceActual (PriceActual);
	}	//	setPrice

	/**
	 * 	Set Price Actual.
	 * 	(actual price is not updateable)
	 *	@param PriceActual actual price
	 */
	@Override
	public void setPriceActual (BigDecimal PriceActual)
	{
		if (PriceActual == null)
			throw new IllegalArgumentException ("PriceActual is mandatory");
		set_ValueNoCheck("PriceActual", PriceActual);
	}	//	setPriceActual


	public void checkPrice ()
	{
		if ((getM_Product_ID() == 0) || isDescription())
		{
			m_priceFound = true;
			return;
		}

		MInvoice parent = getParent();
		m_productPricing = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
			getM_Product_ID(), parent.getC_BPartner_ID(), getQtyInvoiced(), parent.isSOTrx());
		m_productPricing.setM_PriceList_ID(parent.getM_PriceList_ID());
		m_productPricing.setPriceDate(m_DateInvoiced);

		m_productPricing.calculatePrice();
		if (!m_productPricing.isCalculated())
			return;

		m_priceFound = true;
	}

	/**
	 *	Set Tax - requires Warehouse
	 *	@return true if found
	 */
	public boolean setTax()
	{
		if (isDescription())
			return true;
		//
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		int M_Warehouse_ID = org.getM_Warehouse_ID();
		//
		int C_Tax_ID = Tax.get(getCtx(), getM_Product_ID(), getC_Charge_ID() , m_DateInvoiced, m_DateInvoiced,
			getAD_Org_ID(), M_Warehouse_ID,
			m_C_BPartner_Location_ID,		//	should be bill to
			m_C_BPartner_Location_ID, m_IsSOTrx);
		if (C_Tax_ID == 0)
		{
			log.log(Level.SEVERE, "No Tax found");
			return false;
		}
		setC_Tax_ID (C_Tax_ID);
		if (m_IsSOTrx)
		{
		}
		return true;
	}	//	setTax


	/**
	 * 	Calculare Tax Amt.
	 * 	Assumes Line Net is calculated
	 */
	public void setTaxAmt ()
	{
		BigDecimal TaxAmt = Env.ZERO;
		if (getC_Tax_ID() == 0)
			return;
	//	setLineNetAmt();
		MTax tax = MTax.get (getCtx(), getC_Tax_ID());
		if (tax.isDocumentLevel() && m_IsSOTrx)		//	AR Inv Tax
			return;
		//
		TaxAmt = tax.calculateTax(getLineNetAmt(), isTaxIncluded(), getPrecision());
		if (isTaxIncluded())
			setLineTotalAmt(getLineNetAmt());
		else
			setLineTotalAmt(getLineNetAmt().add(TaxAmt));
		super.setTaxAmt (TaxAmt);
	}	//	setTaxAmt

	/**
	 * 	Calculate Extended Amt.
	 * 	May or may not include tax
	 */
	public void setLineNetAmt ()
	{
		//	Calculations & Rounding
		BigDecimal net = getPriceActual().multiply(getQtyInvoiced());
		if (net.scale() > getPrecision())
			net = net.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP);
		super.setLineNetAmt (net);
	}	//	setLineNetAmt

	/**
	 * 	Set Qty Invoiced/Entered.
	 *	@param Qty Invoiced/Ordered
	 */
	public void setQty (int Qty)
	{
		setQty(new BigDecimal(Qty));
	}	//	setQtyInvoiced

	/**
	 * 	Set Qty Invoiced
	 *	@param Qty Invoiced/Entered
	 */
	public void setQty (BigDecimal Qty)
	{
		setQtyEntered(Qty);
		setQtyInvoiced(getQtyEntered());
	}	//	setQtyInvoiced

	/**
	 * 	Set Qty Entered - enforce entered UOM
	 *	@param QtyEntered
	 */
	@Override
	public void setQtyEntered (BigDecimal QtyEntered)
	{
		if ((QtyEntered != null) && (getC_UOM_ID() != 0))
		{
			int precision = MUOM.getPrecision(getCtx(), getC_UOM_ID());
			QtyEntered = QtyEntered.setScale(precision, BigDecimal.ROUND_HALF_UP);
		}
		super.setQtyEntered (QtyEntered);
	}	//	setQtyEntered

	/**
	 * 	Set Qty Invoiced - enforce Product UOM
	 *	@param QtyInvoiced
	 */
	@Override
	public void setQtyInvoiced (BigDecimal QtyInvoiced)
	{
		MProduct product = getProduct();
		if ((QtyInvoiced != null) && (product != null))
		{
			int precision = product.getUOMPrecision();
			QtyInvoiced = QtyInvoiced.setScale(precision, BigDecimal.ROUND_HALF_UP);
		}
		super.setQtyInvoiced(QtyInvoiced);
	}	//	setQtyInvoiced

	/**
	 * 	Set Product
	 *	@param product product
	 */
	public void setProduct (MProduct product)
	{
		m_product = product;
		if (m_product != null)
		{
			setM_Product_ID(m_product.getM_Product_ID());
			setC_UOM_ID (m_product.getC_UOM_ID());
		}
		else
		{
			setM_Product_ID(0);
			setC_UOM_ID (0);
		}
		setM_AttributeSetInstance_ID(0);
	}	//	setProduct


	/**
	 * 	Set M_Product_ID
	 *	@param M_Product_ID product
	 *	@param setUOM set UOM from product
	 */
	public void setM_Product_ID (int M_Product_ID, boolean setUOM)
	{
		if (setUOM)
			setProduct(MProduct.get(getCtx(), M_Product_ID));
		else
			super.setM_Product_ID (M_Product_ID);
		setM_AttributeSetInstance_ID(0);
	}	//	setM_Product_ID

	/**
	 * 	Set Product and UOM
	 *	@param M_Product_ID product
	 *	@param C_UOM_ID uom
	 */
	public void setM_Product_ID (int M_Product_ID, int C_UOM_ID)
	{
		super.setM_Product_ID (M_Product_ID);
		super.setC_UOM_ID(C_UOM_ID);
		setM_AttributeSetInstance_ID(0);
	}	//	setM_Product_ID

	/**
	 * 	Get Product
	 *	@return product or null
	 */
	public MProduct getProduct()
	{
		if ((m_product == null) && (getM_Product_ID() != 0))
			m_product =  MProduct.get (getCtx(), getM_Product_ID());
		return m_product;
	}	//	getProduct

	/**
	 * 	Get C_Project_ID
	 *	@return project
	 */
	@Override
	public int getC_Project_ID()
	{
		int ii = super.getC_Project_ID ();
		if (ii == 0)
			ii = getParent().getC_Project_ID();
		return ii;
	}	//	getC_Project_ID

	/**
	 * 	Get C_Activity_ID
	 *	@return Activity
	 */
	@Override
	public int getC_Activity_ID()
	{
		int ii = super.getC_Activity_ID ();
		if (ii == 0)
			ii = getParent().getC_Activity_ID();
		return ii;
	}	//	getC_Activity_ID

	/**
	 * 	Get C_Campaign_ID
	 *	@return Campaign
	 */
	@Override
	public int getC_Campaign_ID()
	{
		int ii = super.getC_Campaign_ID ();
		if (ii == 0)
			ii = getParent().getC_Campaign_ID();
		return ii;
	}	//	getC_Campaign_ID

	/**
	 * 	Get User2_ID
	 *	@return User2
	 */
	@Override
	public int getUser1_ID ()
	{
		int ii = super.getUser1_ID ();
		if (ii == 0)
			ii = getParent().getUser1_ID();
		return ii;
	}	//	getUser1_ID

	/**
	 * 	Get User2_ID
	 *	@return User2
	 */
	@Override
	public int getUser2_ID ()
	{
		int ii = super.getUser2_ID ();
		if (ii == 0)
			ii = getParent().getUser2_ID();
		return ii;
	}	//	getUser2_ID

	/**
	 * 	Get AD_OrgTrx_ID
	 *	@return p_trx org
	 */
	@Override
	public int getAD_OrgTrx_ID()
	{
		int ii = super.getAD_OrgTrx_ID();
		if (ii == 0)
			ii = getParent().getAD_OrgTrx_ID();
		return ii;
	}	//	getAD_OrgTrx_ID

	/**
	 * 	Get Order Line
	 *	@return order line
	 */
	public MOrderLine getOrderLine()
	{
		if (getC_OrderLine_ID() != 0)
		{
			if ((m_oLine == null) || (m_oLine.getC_OrderLine_ID() != getC_OrderLine_ID()))
				m_oLine = new MOrderLine (getCtx(), getC_OrderLine_ID(), get_Trx());
		}
		return m_oLine;
	}

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInvoiceLine[")
			.append(get_ID()).append(",").append(getLine())
			.append(",QtyInvoiced=").append(getQtyInvoiced())
			.append(",LineNetAmt=").append(getLineNetAmt())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get (Product/Charge) Name
	 * 	@return name
	 */
	public String getName ()
	{
		if (m_name == null)
		{
			String sql = "SELECT COALESCE (p.Name, c.Name) "
				+ "FROM C_InvoiceLine il"
				+ " LEFT OUTER JOIN M_Product p ON (il.M_Product_ID=p.M_Product_ID)"
				+ " LEFT OUTER JOIN C_Charge C ON (il.C_Charge_ID=c.C_Charge_ID) "
				+ "WHERE C_InvoiceLine_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getC_InvoiceLine_ID());
				rs = pstmt.executeQuery();
				if (rs.next())
					m_name = rs.getString(1);
				if (m_name == null)
					m_name = "??";
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "getName", e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		return m_name;
	}	//	getName

	/**
	 * 	Set Temporary (cached) Name
	 * 	@param tempName Cached Name
	 */
	public void setName (String tempName)
	{
		m_name = tempName;
	}	//	setName

	/**
	 * 	Get Description Text.
	 * 	For jsp access (vs. isDescription)
	 *	@return description
	 */
	public String getDescriptionText()
	{
		return super.getDescription();
	}	//	getDescriptionText

	/**
	 * 	Get Currency Precision
	 *	@return precision
	 */
	public int getPrecision()
	{
		if (m_precision != null)
			return m_precision.intValue();

		String sql = "SELECT c.StdPrecision "
			+ "FROM C_Currency c INNER JOIN C_Invoice x ON (x.C_Currency_ID=c.C_Currency_ID) "
			+ "WHERE x.C_Invoice_ID=?";
		int i = QueryUtil.getSQLValue(get_Trx(), sql, getC_Invoice_ID());
		if (i < 0)
		{
			log.warning("Precision=" + i + " - set to 2");
			i = 2;
		}
		m_precision = Integer.valueOf(i);
		return m_precision.intValue();
	}	//	getPrecision

	/**
	 *	Is Tax Included in Amount
	 *	@return true if tax is included
	 */
	public boolean isTaxIncluded()
	{
		if (m_M_PriceList_ID == 0)
		{
			m_M_PriceList_ID = QueryUtil.getSQLValue(get_Trx(),
				"SELECT M_PriceList_ID FROM C_Invoice WHERE C_Invoice_ID=?",
				getC_Invoice_ID());
		}
		MPriceList pl = MPriceList.get(getCtx(), m_M_PriceList_ID, get_Trx());
		return pl.isTaxIncluded();
	}	//	isTaxIncluded

	/**
	 * 	Create Lead/Request
	 */
	public void createLeadRequest(MInvoice invoice)
	{
		if ((getProduct() == null) || (m_product.getR_Source_ID() == 0))
			return;
		String summary = "Purchased: " + m_product.getName()
			+ " - " + getQtyEntered() + " * " + getPriceEntered();
		//
		MSource source = MSource.get(getCtx(), m_product.getR_Source_ID());
		//	Create Request
		if (X_R_Source.SOURCECREATETYPE_Both.equals(source.getSourceCreateType())
			|| X_R_Source.SOURCECREATETYPE_Request.equals(source.getSourceCreateType()))
		{
			MRequest request = new MRequest(getCtx(), 0, get_Trx());
			request.setClientOrg(this);
			request.setSummary(summary);
			request.setAD_User_ID(invoice.getAD_User_ID());
			request.setC_BPartner_ID(invoice.getC_BPartner_ID());
			request.setC_Invoice_ID(invoice.getC_Invoice_ID());
			request.setC_Order_ID(invoice.getC_Order_ID());
			request.setC_Activity_ID(invoice.getC_Activity_ID());
			request.setC_Campaign_ID(invoice.getC_Campaign_ID());
			request.setC_Project_ID(invoice.getC_Project_ID());
			//
			request.setM_Product_ID(getM_Product_ID());
			request.setR_Source_ID(source.getR_Source_ID());
			request.save();
		}
		//	Create Lead
		if (X_R_Source.SOURCECREATETYPE_Both.equals(source.getSourceCreateType())
			|| X_R_Source.SOURCECREATETYPE_Lead.equals(source.getSourceCreateType()))
		{
			MLead lead = new MLead(getCtx(), 0, get_Trx());
			lead.setClientOrg(this);
			lead.setDescription(summary);
			lead.setAD_User_ID(invoice.getAD_User_ID());
			lead.setC_BPartner_Location_ID(invoice.getC_BPartner_Location_ID());
			lead.setC_BPartner_ID(invoice.getC_BPartner_ID());
			lead.setC_Campaign_ID(invoice.getC_Campaign_ID());
			lead.setC_Project_ID(invoice.getC_Project_ID());
			//
			MBPartnerLocation bpLoc = new MBPartnerLocation (getCtx(), invoice.getC_BPartner_Location_ID(), null);
			MLocation loc = bpLoc.getLocation(false);
			lead.setAddress1(loc.getAddress1());
			lead.setAddress2(loc.getAddress2());
			lead.setCity(loc.getCity());
			lead.setPostal(loc.getPostal());
			lead.setPostal_Add(loc.getPostal_Add());
			lead.setRegionName(loc.getRegionName(false));
			lead.setC_Region_ID(loc.getC_Region_ID());
			lead.setC_City_ID(loc.getC_City_ID());
			lead.setC_Country_ID(loc.getC_Country_ID());
			//
			lead.setR_Source_ID(source.getR_Source_ID());
			lead.save();
		}
	}	//	createLeadRequest


	/**
	 * 	Set Resource Assignment - Callout
	 *	@param oldS_ResourceAssignment_ID old value
	 *	@param newS_ResourceAssignment_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setS_ResourceAssignment_ID (String oldS_ResourceAssignment_ID,
			String newS_ResourceAssignment_ID, int windowNo) throws Exception
	{
		if ((newS_ResourceAssignment_ID == null) || (newS_ResourceAssignment_ID.length() == 0))
			return;
		int S_ResourceAssignment_ID = Integer.parseInt(newS_ResourceAssignment_ID);
		if (S_ResourceAssignment_ID == 0)
			return;
		//
		super.setS_ResourceAssignment_ID(S_ResourceAssignment_ID);

		int M_Product_ID = 0;
		String Name = null;
		String Description = null;
		BigDecimal Qty = null;
		String sql = "SELECT p.M_Product_ID, ra.Name, ra.Description, ra.Qty "
			+ "FROM S_ResourceAssignment ra"
			+ " INNER JOIN M_Product p ON (p.S_Resource_ID=ra.S_Resource_ID) "
			+ "WHERE ra.S_ResourceAssignment_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, S_ResourceAssignment_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				M_Product_ID = rs.getInt (1);
				Name = rs.getString(2);
				Description = rs.getString(3);
				Qty = rs.getBigDecimal(4);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		log.fine("S_ResourceAssignment_ID=" + S_ResourceAssignment_ID
				+ " - M_Product_ID=" + M_Product_ID);
		if (M_Product_ID != 0)
		{
			setM_Product_ID(M_Product_ID);
			if (Description != null)
				Name += " (" + Description + ")";
			if (!".".equals(Name))
				setDescription(Name);
			if (Qty != null)
				setQtyInvoiced(Qty);
		}
	}	//	setS_ResourceAssignment_ID


	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if save
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		log.fine("New=" + newRecord);
		//	Charge
		if (getC_Charge_ID() != 0)
		{
			if (getM_Product_ID() != 0)
				setM_Product_ID(0);
			setC_UOM_ID(MUOM.Each_ID);
		}
		else	//	Set Product Price
		{
			if (!m_priceSet)
			{
				if( (Env.ZERO.compareTo(getPriceActual()) == 0)
						&&  (Env.ZERO.compareTo(getPriceList()) == 0))
					setPrice();
				else
					checkPrice();

				if (!m_priceSet && !m_priceFound)
				{
					if(((Env.ZERO.compareTo(getPriceActual()) == 0)
							&&  (Env.ZERO.compareTo(getPriceList()) == 0)) ||
							getParent().isSOTrx())
					{
						log.saveError("Error", Msg.getMsg(getCtx(), "ProductNotOnPriceList"));
						return false;
					}
				}
			}
		}

		//	Set Tax
		if (getC_Tax_ID() == 0)
			setTax();

		//	Get Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM C_InvoiceLine WHERE C_Invoice_ID=?";
			int ii = QueryUtil.getSQLValue (get_Trx(), sql, getC_Invoice_ID());
			setLine (ii);
		}
		//	UOM
		if (getC_UOM_ID() == 0)
		{
			int C_UOM_ID = MUOM.getDefault_UOM_ID(getCtx());
			if (C_UOM_ID > 0)
				setC_UOM_ID (C_UOM_ID);
		}
		//	Qty Precision
		if (newRecord || is_ValueChanged("QtyEntered"))
		{
			setQtyEntered(getQtyEntered());
			BigDecimal QtyInvoiced = MUOMConversion.convertProductFrom(getCtx(), getM_Product_ID(), getC_UOM_ID(), getQtyEntered());
			if( QtyInvoiced == null )
				QtyInvoiced = getQtyEntered();
			setQtyInvoiced(QtyInvoiced);
		}

		if (newRecord || is_ValueChanged("QtyInvoiced"))
			setQtyInvoiced(getQtyInvoiced());

		//	Calculations & Rounding
		setLineNetAmt();
		if (getTaxAmt().compareTo(Env.ZERO) == 0)
			setTaxAmt();
		//
		return true;
	}	//	beforeSave


	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		/* If the invoice lines are created/updated via a process i.e. called from a doIt() method,
		 * then the taxes and totals will not be recomputed using the logic below. Instead you must
		 * either explicitly call MInvoice.recomputeTaxesAndTotals or save the invoice header after 
		 * adding the lines. Most places, the invoice is prepared and then saved after adding lines
		 * so MInvoice.recomputeTaxesAndTotals is called automatically in MInvoice.afterSave().
		 */
		if (!success)
			return success;

		if (!isProcessed () && !getCtx().isBatchMode())
		{
			if (!newRecord && is_ValueChanged("C_Tax_ID"))
			{
				//	Recalculate Tax for old Tax
				MInvoiceTax tax = MInvoiceTax.get (this, getPrecision(),
						true, get_Trx());	//	old Tax
				if (tax != null)
				{
					if (!tax.calculateTaxFromLines())
						return false;
					if (!tax.save(get_Trx()))
						return true;
				}
			}
			return updateHeaderTax();
		}
		return true;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return updateHeaderTax();
	}	//	afterDelete

	/**
	 *	Update Tax & Header
	 *	@return true if header updated with tax
	 */
	boolean updateHeaderTax()
	{
		//	Recalculate Tax for this Tax
		MInvoiceTax tax = MInvoiceTax.get (this, getPrecision(),
			false, get_Trx());	//	current Tax
		if (tax != null)
		{
			if (!tax.calculateTaxFromLines())
				return false;
			if (!tax.save(get_Trx()))
				return false;
		}

		//	Update Invoice Header
		String sql = "UPDATE C_Invoice i"
			+ " SET TotalLines="
				+ "(SELECT COALESCE(SUM(LineNetAmt),0) FROM C_InvoiceLine il WHERE i.C_Invoice_ID=il.C_Invoice_ID) "
			+ "WHERE C_Invoice_ID=?";
		int no = DB.executeUpdate(get_Trx(), sql, new Object[]{getC_Invoice_ID()});
		if (no != 1)
			log.warning("(1) #" + no);

		//SE RESTA LA RETEBNCION DE ISLR (BECO)
		if (isTaxIncluded())
			sql = "UPDATE C_Invoice i "
				+ "SET GrandTotal=TotalLines "
				+ "- (SELECT COALESCE(SUM(XX_RetainedAmount),0) FROM C_InvoiceLine it WHERE i.C_Invoice_ID=it.C_Invoice_ID) "
				+ "WHERE C_Invoice_ID=?";
		else
			sql = "UPDATE C_Invoice i "
				+ "SET GrandTotal=TotalLines+"
					+ "(SELECT COALESCE(SUM(TaxAmt),0) FROM C_InvoiceTax it WHERE i.C_Invoice_ID=it.C_Invoice_ID) "
					+ "- (SELECT COALESCE(SUM(XX_RetainedAmount),0) FROM C_InvoiceLine it WHERE i.C_Invoice_ID=it.C_Invoice_ID) "
					+ "WHERE C_Invoice_ID=?";
		no = DB.executeUpdate(get_Trx(), sql, new Object[]{getC_Invoice_ID()});
		if (no != 1)
			log.warning("(2) #" + no);
		m_parent = null;

		return no == 1;
	}	//	updateHeaderTax


	/**************************************************************************
	 * 	Allocate Landed Costs
	 *	@return error message or ""
	 */
	public String allocateLandedCosts()
	{
		if (isProcessed())
			return "Processed";
		MLandedCost[] lcs = MLandedCost.getLandedCosts(this);
		if (lcs.length == 0)
			return "";
		String sql = "DELETE FROM C_LandedCostAllocation WHERE C_InvoiceLine_ID=? ";
		int no = DB.executeUpdate(get_Trx(), sql,getC_InvoiceLine_ID());
		if (no != 0)
			log.info("Deleted #" + no);

		int inserted = 0;
		//	*** Single Criteria ***
		if (lcs.length == 1)
		{
			MLandedCost lc = lcs[0];
			if (lc.getM_InOut_ID() != 0)
			{
				//	Create List
				ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
				MInOut ship = new MInOut (getCtx(), lc.getM_InOut_ID(), get_Trx());
				MInOutLine[] lines = ship.getLines();
				for (MInOutLine element : lines) {
					if (element.isDescription() || (element.getM_Product_ID() == 0))
						continue;
					if ((lc.getM_Product_ID() == 0)
						|| (lc.getM_Product_ID() == element.getM_Product_ID()))
						list.add(element);
				}
				if (list.size() == 0)
					return "No Matching Lines (with Product) in Shipment";
				//	Calculate total & base
				BigDecimal total = Env.ZERO;
				for (int i = 0; i < list.size(); i++)
				{
					MInOutLine iol = list.get(i);
					total = total.add(iol.getBase(lc.getLandedCostDistribution()));
				}
				if (total.signum() == 0)
					return "Total of Base values is 0 - " + lc.getLandedCostDistribution();
				//	Create Allocations
				for (int i = 0; i < list.size(); i++)
				{
					MInOutLine iol = list.get(i);
					MLandedCostAllocation lca = new MLandedCostAllocation (this, lc.getM_CostElement_ID());
					lca.setM_Product_ID(iol.getM_Product_ID());
					lca.setM_AttributeSetInstance_ID(iol.getM_AttributeSetInstance_ID());
					BigDecimal base = iol.getBase(lc.getLandedCostDistribution());
					BigDecimal qty = iol.getMovementQty();
					lca.setBase(base);
					lca.setQty(qty);
					if (base.signum() != 0)
					{
						double result = getLineNetAmt().multiply(base).doubleValue();
						result /= total.doubleValue();
						lca.setAmt(result, getPrecision());
					}
					if (!lca.save())
						return "Cannot save line Allocation = " + lca;
					inserted++;
				}
				log.info("Inserted " + inserted);
				allocateLandedCostRounding();
				return "";
			}
			//	Single Line
			else if (lc.getM_InOutLine_ID() != 0)
			{
				MInOutLine iol = new MInOutLine (getCtx(), lc.getM_InOutLine_ID(), get_Trx());
				if (iol.isDescription() || (iol.getM_Product_ID() == 0))
					return "Invalid Receipt Line - " + iol;
				MLandedCostAllocation lca = new MLandedCostAllocation (this, lc.getM_CostElement_ID());
				lca.setM_Product_ID(iol.getM_Product_ID());
				lca.setM_AttributeSetInstance_ID(iol.getM_AttributeSetInstance_ID());
				lca.setAmt(getLineNetAmt());
				lca.setQty(iol.getMovementQty());
				if (lca.save())
					return "";
				return "Cannot save single line Allocation = " + lc;
			}
			//	Single Product
			else if (lc.getM_Product_ID() != 0)
			{
				MLandedCostAllocation lca = new MLandedCostAllocation (this, lc.getM_CostElement_ID());
				lca.setM_Product_ID(lc.getM_Product_ID());	//	No ASI
				lca.setAmt(getLineNetAmt());
				lca.setQty(getQtyInvoiced());
				if (lca.save())
					return "";
				return "Cannot save Product Allocation = " + lc;
			}
			else
				return "No Reference for " + lc;
		}

		//	*** Multiple Criteria ***
		String LandedCostDistribution = lcs[0].getLandedCostDistribution();
		int M_CostElement_ID = lcs[0].getM_CostElement_ID();
		for (MLandedCost lc : lcs) {
			if (!LandedCostDistribution.equals(lc.getLandedCostDistribution()))
				return "Multiple Landed Cost Rules must have consistent Landed Cost Distribution";
			if ((lc.getM_Product_ID() != 0) && (lc.getM_InOut_ID() == 0) && (lc.getM_InOutLine_ID() == 0))
				return "Multiple Landed Cost Rules cannot directly allocate to a Product";
			if (M_CostElement_ID != lc.getM_CostElement_ID())
				return "Multiple Landed Cost Rules cannot different Cost Elements";
		}
		//	Create List
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		for (MLandedCost lc : lcs) {
			if ((lc.getM_InOut_ID() != 0) && (lc.getM_InOutLine_ID() == 0))		//	entire receipt
			{
				MInOut ship = new MInOut (getCtx(), lc.getM_InOut_ID(), get_Trx());
				MInOutLine[] lines = ship.getLines();
				for (MInOutLine element : lines) {
					if (element.isDescription()		//	description or no product
						|| (element.getM_Product_ID() == 0))
						continue;
					if ((lc.getM_Product_ID() == 0		//	no restriction or product match
)
						|| (lc.getM_Product_ID() == element.getM_Product_ID()))
						list.add(element);
				}
			}
			else if (lc.getM_InOutLine_ID() != 0)	//	receipt line
			{
				MInOutLine iol = new MInOutLine (getCtx(), lc.getM_InOutLine_ID(), get_Trx());
				if (!iol.isDescription() && (iol.getM_Product_ID() != 0))
					list.add(iol);
			}
		}
		if (list.size() == 0)
			return "No Matching Lines (with Product)";
		//	Calculate total & base
		BigDecimal total = Env.ZERO;
		for (int i = 0; i < list.size(); i++)
		{
			MInOutLine iol = list.get(i);
			total = total.add(iol.getBase(LandedCostDistribution));
		}
		if (total.signum() == 0)
			return "Total of Base values is 0 - " + LandedCostDistribution;
		//	Create Allocations
		for (int i = 0; i < list.size(); i++)
		{
			MInOutLine iol = list.get(i);
			MLandedCostAllocation lca = new MLandedCostAllocation (this, lcs[0].getM_CostElement_ID());
			lca.setM_Product_ID(iol.getM_Product_ID());
			lca.setM_AttributeSetInstance_ID(iol.getM_AttributeSetInstance_ID());
			BigDecimal base = iol.getBase(LandedCostDistribution);
			BigDecimal qty = iol.getMovementQty();
			lca.setBase(base);
			lca.setQty(qty);
			if (base.signum() != 0)
			{
				double result = getLineNetAmt().multiply(base).doubleValue();
				result /= total.doubleValue();
				lca.setAmt(result, getPrecision());
			}
			if (!lca.save())
				return "Cannot save line Allocation = " + lca;
			inserted++;
		}

		log.info("Inserted " + inserted);
		allocateLandedCostRounding();
		return "";
	}	//	allocate Costs

	/**
	 * 	Allocate Landed Cost - Enforce Rounding
	 */
	private void allocateLandedCostRounding()
	{
		MLandedCostAllocation[] allocations = MLandedCostAllocation.getOfInvoiceLine(
			getCtx(), getC_InvoiceLine_ID(), get_Trx());
		MLandedCostAllocation largestAmtAllocation = null;
		BigDecimal allocationAmt = Env.ZERO;
		for (MLandedCostAllocation allocation : allocations) {
			if ((largestAmtAllocation == null)
				|| (allocation.getAmt().compareTo(largestAmtAllocation.getAmt()) > 0))
				largestAmtAllocation = allocation;
			allocationAmt = allocationAmt.add(allocation.getAmt());
		}
		BigDecimal difference = getLineNetAmt().subtract(allocationAmt);
		if (difference.signum() != 0)
		{
			largestAmtAllocation.setAmt(largestAmtAllocation.getAmt().add(difference));
			largestAmtAllocation.save();
			log.config("Difference=" + difference
				+ ", C_LandedCostAllocation_ID=" + largestAmtAllocation.getC_LandedCostAllocation_ID()
				+ ", Amt" + largestAmtAllocation.getAmt());
		}
	}	//	allocateLandedCostRounding




	/**
	 *	Invoice Line - Quantity.
	 *		- called from C_UOM_ID, QtyEntered, QtyInvoiced
	 *		- enforces qty UOM relationship
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	private boolean setQty (int WindowNo, String columnName)
	{
		int M_Product_ID = getM_Product_ID();
	//	log.log(Level.WARNING,"qty - init - M_Product_ID=" + M_Product_ID);
		BigDecimal QtyInvoiced, QtyEntered, PriceActual, PriceEntered;

		//	No Product
		/*if (M_Product_ID == 0)
		{
			QtyEntered = getQtyEntered();
			setQtyInvoiced( QtyEntered);
		}*/
		//	UOM Changed - convert from Entered -> Product
		/*else*/ if (columnName.equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = getC_UOM_ID();
			QtyEntered = getQtyEntered();
			
			// Do rounding on Qty Entered only if UOM is specified
			if(C_UOM_To_ID != 0)
			{
				BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(getCtx(), C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
				if (QtyEntered.compareTo(QtyEntered1) != 0)
				{
					log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
						+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
					QtyEntered = QtyEntered1;
					setQtyEntered( QtyEntered);
				}
			}
			
			// UOM Conversion
			QtyInvoiced = MUOMConversion.convertProductFrom (getCtx(), M_Product_ID,
				C_UOM_To_ID, QtyEntered);
			if (QtyInvoiced == null)
				QtyInvoiced = QtyEntered;
			
			boolean conversion = QtyEntered.compareTo(QtyInvoiced) != 0;
			PriceActual = getPriceActual();
			PriceEntered = MUOMConversion.convertProductFrom (getCtx(), M_Product_ID,
				C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			log.fine("qty - UOM=" + C_UOM_To_ID
				+ ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
				+ " -> " + conversion
				+ " QtyInvoiced/PriceEntered=" + QtyInvoiced + "/" + PriceEntered);
			setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			setQtyInvoiced( QtyInvoiced);
			setPriceEntered( PriceEntered);
		}
		//	QtyEntered changed - calculate QtyInvoiced
		else if (columnName.equals("QtyEntered"))
		{
			int C_UOM_To_ID = getC_UOM_ID();
			QtyEntered = getQtyEntered();
			
			// Do rounding on Qty Entered only if UOM is specified
			if(C_UOM_To_ID != 0)
			{
				BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(getCtx(), C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
				if (QtyEntered.compareTo(QtyEntered1) != 0)
				{
					log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
						+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
					QtyEntered = QtyEntered1;
					setQtyEntered( QtyEntered);
				}
			}
			
			// UOM Conversion
			QtyInvoiced = MUOMConversion.convertProductFrom (getCtx(), M_Product_ID,
				C_UOM_To_ID, QtyEntered);
			if (QtyInvoiced == null)
				QtyInvoiced = QtyEntered;
			
			boolean conversion = QtyEntered.compareTo(QtyInvoiced) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion
				+ " QtyInvoiced=" + QtyInvoiced);
			setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			setQtyInvoiced( QtyInvoiced);
		}
		//	QtyInvoiced changed - calculate QtyEntered (should not happen)
		else if (columnName.equals("QtyInvoiced"))
		{
			int C_UOM_To_ID = getC_UOM_ID();
			QtyInvoiced = getQtyInvoiced();
			int precision = MProduct.get(getCtx(), M_Product_ID).getUOMPrecision();
			BigDecimal QtyInvoiced1 = QtyInvoiced.setScale(precision, BigDecimal.ROUND_HALF_UP);
			if (QtyInvoiced.compareTo(QtyInvoiced1) != 0)
			{
				log.fine("Corrected QtyInvoiced Scale "
					+ QtyInvoiced + "->" + QtyInvoiced1);
				QtyInvoiced = QtyInvoiced1;
				setQtyInvoiced( QtyInvoiced);
			}
			QtyEntered = MUOMConversion.convertProductTo (getCtx(), M_Product_ID,
				C_UOM_To_ID, QtyInvoiced);
			if (QtyEntered == null)
				QtyEntered = QtyInvoiced;
			boolean conversion = QtyInvoiced.compareTo(QtyEntered) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID
				+ ", QtyInvoiced=" + QtyInvoiced
				+ " -> " + conversion
				+ " QtyEntered=" + QtyEntered);
			setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			setQtyEntered( QtyEntered);
		}
		//
		return true;
	}	//	qty


	/**
	 *	Invoice - Amount.
	 *		- called from QtyInvoiced, PriceActual
	 *		- calculates LineNetAmt
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	private boolean setAmt(int WindowNo, String columnName)
	{

	//	log.log(Level.WARNING,"amt - init");
		int C_UOM_To_ID = getC_UOM_ID();
		int M_Product_ID = getM_Product_ID();
		int M_PriceList_ID = getCtx().getContextAsInt( WindowNo, "M_PriceList_ID");
		// int StdPrecision = MPriceList.getPricePrecision(getCtx(), M_PriceList_ID);
		// Using Currency Precision. Not PriceList precision
		int StdPrecision = getPrecision();
		BigDecimal QtyEntered, QtyInvoiced, PriceEntered, PriceActual, PriceLimit, Discount, PriceList;
		//	get values
		QtyEntered = getQtyEntered();
		QtyInvoiced = getQtyInvoiced();
		log.fine("QtyEntered=" + QtyEntered + ", Invoiced=" + QtyInvoiced + ", UOM=" + C_UOM_To_ID);
		//
		PriceEntered = getPriceEntered();
		PriceActual = getPriceActual();
	//	Discount = (BigDecimal)mTab.getValue("Discount");
		PriceLimit = getPriceLimit();
		PriceList = getPriceList();
		log.fine("PriceList=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
		log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual);// + ", Discount=" + Discount);

		//	Qty changed - recalc price
		if ((columnName.equals("QtyInvoiced")
			|| columnName.equals("QtyEntered")
			|| columnName.equals("M_Product_ID"))
			&& !"N".equals(getCtx().getContext( WindowNo, "DiscountSchema")))
		{
			int C_BPartner_ID = getCtx().getContextAsInt( WindowNo, "C_BPartner_ID");
			if (columnName.equals("QtyEntered"))
				QtyInvoiced = MUOMConversion.convertProductTo (getCtx(), M_Product_ID,
					C_UOM_To_ID, QtyEntered);
			if (QtyInvoiced == null)
				QtyInvoiced = QtyEntered;
			boolean IsSOTrx = getCtx().isSOTrx(WindowNo);
			MProductPricing pp = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
					M_Product_ID, C_BPartner_ID, QtyInvoiced, IsSOTrx);
			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = getCtx().getContextAsInt( WindowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			Timestamp date = new Timestamp( getCtx().getContextAsTime( WindowNo, "DateInvoiced" ) );
			pp.setPriceDate(date);
			//
			PriceEntered = MUOMConversion.convertProductFrom (getCtx(), M_Product_ID,
				C_UOM_To_ID, pp.getPriceStd());
			if (PriceEntered == null)
				PriceEntered = pp.getPriceStd();
			//
			log.fine("amt - QtyChanged -> PriceActual=" + pp.getPriceStd()
				+ ", PriceEntered=" + PriceEntered + ", Discount=" + pp.getDiscount());
			PriceActual = pp.getPriceStd();
			setPriceActual( PriceActual);
		//	mTab.setValue("Discount", pp.getDiscount());
			setPriceEntered( PriceEntered);
			setContext( WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		}
		else if (columnName.equals("PriceActual"))
		{
			PriceActual = getPriceActual();
			PriceEntered = MUOMConversion.convertProductFrom (getCtx(), M_Product_ID,
				C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			//
			log.fine("amt - PriceActual=" + PriceActual
				+ " -> PriceEntered=" + PriceEntered);
			setPriceEntered( PriceEntered);
		}
		else if (columnName.equals("PriceEntered"))
		{
			PriceEntered = getPriceEntered();
			PriceActual = MUOMConversion.convertProductTo (getCtx(), M_Product_ID,
				C_UOM_To_ID, PriceEntered);
			if (PriceActual == null)
				PriceActual = PriceEntered;
			//
			log.fine("amt - PriceEntered=" + PriceEntered
				+ " -> PriceActual=" + PriceActual);
			setPriceActual(PriceActual);
		}

		//	Check PriceLimit
		String epl = getCtx().getContext( WindowNo, "EnforcePriceLimit");
		boolean enforce = getCtx().isSOTrx(WindowNo) && (epl != null) && epl.equals("Y");
		if (enforce && MRole.getDefault(getCtx(), false).isOverwritePriceLimit())
			enforce = false;
		//	Check Price Limit?
		if (enforce && (PriceLimit.doubleValue() != 0.0)
		  && (PriceActual.compareTo(PriceLimit) < 0))
		{
			PriceActual = PriceLimit;
			PriceEntered = MUOMConversion.convertProductFrom (getCtx(), M_Product_ID,
				C_UOM_To_ID, PriceLimit);
			if (PriceEntered == null)
				PriceEntered = PriceLimit;
			log.fine("amt =(under) PriceEntered=" + PriceEntered + ", Actual" + PriceLimit);
			setPriceActual( PriceLimit);
			setPriceEntered( PriceEntered);
			addError(Msg.getMsg(getCtx(), "UnderLimitPrice"));
			//	Repeat Discount calc
			if (PriceList.intValue() != 0)
			{
				Discount = new BigDecimal ((PriceList.doubleValue () - PriceActual.doubleValue ()) / PriceList.doubleValue () * 100.0);
				if (Discount.scale () > 2)
					Discount = Discount.setScale (2, BigDecimal.ROUND_HALF_UP);
			//	mTab.setValue ("Discount", Discount);
			}
		}

		//	Line Net Amt
		BigDecimal LineNetAmt = QtyInvoiced.multiply(PriceActual);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		log.info("amt = LineNetAmt=" + LineNetAmt);
		setLineNetAmt( LineNetAmt);

		//	Calculate Tax Amount for PO
		boolean IsSOTrx = getCtx().isSOTrx( WindowNo );
		if (!IsSOTrx)
		{
			BigDecimal TaxAmt = Env.ZERO;
			if (columnName.equals("TaxAmt"))
			{
				TaxAmt = getTaxAmt();
			}
			else
			{
				Integer taxID = getC_Tax_ID();
				if (taxID != null)
				{
					int C_Tax_ID = taxID.intValue();
					MTax tax = new MTax (getCtx(), C_Tax_ID, null);
					TaxAmt = tax.calculateTax(LineNetAmt, isTaxIncluded(), StdPrecision);
					setTaxAmt( TaxAmt);
				}
			}
			//	Add it up
			setLineTotalAmt( LineNetAmt.add(TaxAmt));
		}

		return true;
	}	//	amt


	/**
	 * 	Set UOM - Callout
	 *	@param oldC_UOM_ID old value
	 *	@param newC_UOM_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setC_UOM_ID (String oldC_UOM_ID,
			String newC_UOM_ID, int windowNo) throws Exception
	{
		if ((newC_UOM_ID == null) || (newC_UOM_ID.length() == 0))
			return;
		int C_UOM_ID = Integer.parseInt(newC_UOM_ID);
		if (C_UOM_ID == 0)
			return;
		//
		super.setC_UOM_ID(C_UOM_ID);
		setQty(windowNo, "C_UOM_ID");
		setAmt(windowNo, "C_UOM_ID");
	}	//	setC_UOM_ID


	/**
	 * 	Set QtyEntered - Callout
	 *	@param oldQtyEntered old value
	 *	@param newQtyEntered new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setQtyEntered (String oldQtyEntered,
			String newQtyEntered, int windowNo) throws Exception
	{
		if ((newQtyEntered == null) || (newQtyEntered.length() == 0))
			return;
		BigDecimal QtyEntered = new BigDecimal(newQtyEntered);
		super.setQtyEntered(QtyEntered);
		setQty(windowNo, "QtyEntered");
		setAmt(windowNo, "QtyEntered");
	}	//	setQtyEntered

	/**
	 * 	Set QtyOrdered - Callout
	 *	@param oldQtyInvoiced old value
	 *	@param newQtyInvoiced new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setQtyInvoiced (String oldQtyInvoiced,
			String newQtyInvoiced, int windowNo) throws Exception
	{
		if ((newQtyInvoiced == null) || (newQtyInvoiced.length() == 0))
			return;
		BigDecimal qtyInvoiced = new BigDecimal(newQtyInvoiced);
		super.setQtyInvoiced(qtyInvoiced);
		setQty(windowNo, "QtyInvoiced");
		setAmt(windowNo, "QtyInvoiced");
	}	//	setQtyOrdered



	/**
	 * 	Set C_Tax_ID - Callout
	 *	@param oldC_Tax_ID old value
	 *	@param newC_Tax_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setC_Tax_ID (String oldC_Tax_ID,
			String newC_Tax_ID, int windowNo) throws Exception
	{
		if ((newC_Tax_ID == null) || (newC_Tax_ID.length() == 0))
			return;
		int C_Tax_ID = Integer.parseInt(newC_Tax_ID) ;
		super.setC_Tax_ID(C_Tax_ID);
		setAmt(windowNo, "C_Tax_ID");
	}	//	setC_Tax_ID


	/**
	 * 	Set PriceActual - Callout
	 *	@param oldPriceActual old value
	 *	@param newPriceActual new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setPriceActual (String oldPriceActual,
			String newPriceActual, int windowNo) throws Exception
	{
		if ((newPriceActual == null) || (newPriceActual.length() == 0))
			return;
		BigDecimal PriceActual = new BigDecimal(newPriceActual);
		super.setPriceActual(PriceActual);
		setAmt(windowNo, "PriceActual");
	}	//	setPriceActual

	/**
	 * 	Set PriceEntered - Callout
	 *	@param oldPriceEntered old value
	 *	@param newPriceEntered new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setPriceEntered (String oldPriceEntered,
			String newPriceEntered, int windowNo) throws Exception
	{
		if ((newPriceEntered == null) || (newPriceEntered.length() == 0))
			return;
		BigDecimal PriceEntered = new BigDecimal(newPriceEntered);
		super.setPriceEntered(PriceEntered);
		setAmt(windowNo, "PriceEntered");
	}	//	setPriceEntered


	/**
	 * 	Set TaxAmt - Callout
	 *	@param oldTaxAmt old value
	 *	@param newTaxAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setTaxAmt (String oldTaxAmt,
			String newTaxAmt, int windowNo) throws Exception
	{
		if ((newTaxAmt == null) || (newTaxAmt.length() == 0))
			return;
		BigDecimal taxAmt = new BigDecimal(newTaxAmt);
		super.setTaxAmt( taxAmt);
		setAmt(windowNo, "TaxAmt");
	}	//	setTaxAmt


	/**************************************************************************
	 *	Invoice Line - Product.
	 *		- reset C_Charge_ID / M_AttributeSetInstance_ID
	 *		- PriceList, PriceStd, PriceLimit, C_Currency_ID, EnforcePriceLimit
	 *		- UOM
	 *	Calls Tax
	 *	@param oldM_Product_ID old value
	 *	@param newM_Product_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setM_Product_ID (String oldM_Product_ID,
			String newM_Product_ID, int WindowNo) throws Exception
	{
		if( (newM_Product_ID == null) || (newM_Product_ID.length() == 0) )
			return;
		int M_Product_ID = Integer.parseInt( newM_Product_ID );
		if( M_Product_ID == 0 )
			return;

		setC_Charge_ID( 0 );

		//	Set Attribute
		if ((getCtx().getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID") == M_Product_ID)
			&& (getCtx().getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID") != 0))
			setM_AttributeSetInstance_ID( Integer.valueOf(getCtx().getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID")));
		else
			setM_AttributeSetInstance_ID( -1 );

		/*****	Price Calculation see also qty	****/
		boolean IsSOTrx = getCtx().isSOTrx( WindowNo );
		int C_BPartner_ID = getCtx().getContextAsInt(WindowNo, "C_BPartner_ID");
		BigDecimal Qty = getQtyInvoiced();
		MProductPricing pp = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
				M_Product_ID, C_BPartner_ID, Qty, IsSOTrx);
		//
		int M_PriceList_ID = getCtx().getContextAsInt( WindowNo, "M_PriceList_ID");
		pp.setM_PriceList_ID(M_PriceList_ID);
		int M_PriceList_Version_ID = getCtx().getContextAsInt( WindowNo, "M_PriceList_Version_ID");
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		long time = getCtx().getContextAsTime(WindowNo, "DateInvoiced");
		pp.setPriceDate(time);

		pp.calculatePrice();
		if (!pp.isCalculated())
		{
			setPriceList( BigDecimal.ZERO);
			setPriceLimit( BigDecimal.ZERO);
			setPriceActual( BigDecimal.ZERO);
			setPriceEntered( BigDecimal.ZERO);

			p_changeVO.addError(Msg.getMsg(getCtx(), "ProductNotOnPriceList"));
			return;

		}

		//
		setPriceList( pp.getPriceList());
		setPriceLimit( pp.getPriceLimit());
		setPriceActual( pp.getPriceStd());
		setPriceEntered( pp.getPriceStd());
		setContext( WindowNo, "C_Currency_ID", Integer.toString(pp.getC_Currency_ID()));
	//	mTab.setValue("Discount", pp.getDiscount());
		int newC_UOM_ID = Integer.valueOf(pp.getC_UOM_ID());
		setC_UOM_ID( newC_UOM_ID );
		
		// since UOM specified is changed (as a result of tab out from product), recalculate QtyEntered and QtyInvoiced based on new UOM
		BigDecimal QtyEntered = getQtyEntered();
		if(newC_UOM_ID != 0)
		{
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(getCtx(), newC_UOM_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + newC_UOM_ID 
					+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);  
				QtyEntered = QtyEntered1;
				setQtyEntered( QtyEntered);
			}
		}
		
		// UOM Conversion
		BigDecimal QtyInvoiced = MUOMConversion.convertProductFrom (getCtx(), M_Product_ID,	newC_UOM_ID, QtyEntered);
		if (QtyInvoiced == null)
			QtyInvoiced = QtyEntered;
		setQtyInvoiced( QtyInvoiced);
		
		setContext( WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");
		setContext( WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		//
		setTax( WindowNo, "M_Product_ID");

		return;
	}	//	product

	/**
	 * 	Set Charge - Callout
	 *	@param oldC_Charge_ID old value
	 *	@param newC_Charge_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setC_Charge_ID (String oldC_Charge_ID,
			String newC_Charge_ID, int WindowNo) throws Exception
	{
		if( (newC_Charge_ID == null) || (newC_Charge_ID.length() == 0) )
			return;
		int C_Charge_ID = Integer.parseInt( newC_Charge_ID );
		if( C_Charge_ID == 0 )
			return;

		//	No Product defined
		if (getM_Product_ID() != 0)
		{
			setC_Charge_ID( 0 );

			addError( Msg.getMsg( getCtx(), "ChargeExclusively" ) );
		}
		setM_AttributeSetInstance_ID( -1 );
		setS_ResourceAssignment_ID( 0 );
		setC_UOM_ID( Integer.valueOf(100));	//	EA

		setContext( WindowNo, "DiscountSchema", "N");
		String sql = "SELECT ChargeAmt FROM C_Charge WHERE C_Charge_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_Charge_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				setPriceEntered( rs.getBigDecimal (1));
				setPriceActual( rs.getBigDecimal (1));
				setPriceLimit( Env.ZERO);
				setPriceList( Env.ZERO);
				setContext( WindowNo, "Discount", Env.ZERO.toString() );
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql + e);
			addError( e.getLocalizedMessage() );
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		setTax(WindowNo, "C_Charge_ID");
	}	//	charge



	/**
	 *	Invoice Line - Tax.
	 *		- basis: Product, Charge, BPartner Location
	 *		- sets C_Tax_ID
	 *  Calles Amount
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	private boolean setTax (int WindowNo, String columnName )
	{
		//	Check Product
		int M_Product_ID = getM_Product_ID();
		int C_Charge_ID = getC_Charge_ID();
		log.fine("Product=" + M_Product_ID + ", C_Charge_ID=" + C_Charge_ID);
		if ((M_Product_ID == 0) && (C_Charge_ID == 0))
			return setAmt( WindowNo, columnName );

		//	Check Partner Location
		int shipC_BPartner_Location_ID = getCtx().getContextAsInt( WindowNo, "C_BPartner_Location_ID");
		if (shipC_BPartner_Location_ID == 0)
			return setAmt( WindowNo, columnName );
		log.fine("Ship BP_Location=" + shipC_BPartner_Location_ID);
		int billC_BPartner_Location_ID = shipC_BPartner_Location_ID;
		log.fine("Bill BP_Location=" + billC_BPartner_Location_ID);

		//	Dates
		Timestamp billDate = new Timestamp(getCtx().getContextAsTime(WindowNo, "DateInvoiced"));
		log.fine("Bill Date=" + billDate);
		Timestamp shipDate = billDate;
		log.fine("Ship Date=" + shipDate);

		int AD_Org_ID = getAD_Org_ID();
		log.fine("Org=" + AD_Org_ID);

		int M_Warehouse_ID = getCtx().getContextAsInt( "#M_Warehouse_ID");
		log.fine("Warehouse=" + M_Warehouse_ID);

		//
		int C_Tax_ID = Tax.get(getCtx(), M_Product_ID, C_Charge_ID, billDate, shipDate,
			AD_Org_ID, M_Warehouse_ID, billC_BPartner_Location_ID, shipC_BPartner_Location_ID,
			getCtx().isSOTrx( WindowNo ));
		log.info("Tax ID=" + C_Tax_ID);
		//
		if (C_Tax_ID == 0)
		{
			ValueNamePair pp = CLogger.retrieveError();
			if (pp != null)
				addError(pp.getValue());
			else
				addError( Msg.getMsg( getCtx(), "Tax Error" ) );
		}
		else
			setC_Tax_ID( Integer.valueOf(C_Tax_ID));
		//
		return setAmt( WindowNo, columnName );
	}	//	tax



}	//	MInvoiceLine
