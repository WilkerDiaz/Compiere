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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.CompiereStateException;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Standard Cost Update
 *	
 *  @author Jorg Janke
 *  @version $Id: CostUpdate.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class CostUpdate extends SvrProcess
{
	/**	Product Category		*/
	private int		p_M_Product_Category_ID = 0;
	/** Future Costs			*/
	private String	p_SetFutureCostTo = null;
	/** Standard Costs			*/
	private String	p_SetStandardCostTo = null;
	/** PLV						*/
	private int 	p_M_PriceList_Version_ID = 0;
	/** Cost Type               */
	private int     p_M_CostType_ID = 0;
	
	
	private static final String	TO_AveragePO = "A";
	private static final String	TO_AverageInvoiceHistory = "DI";
	private static final String	TO_AveragePOHistory = "DP";
	private static final String	TO_FiFo = "F";
	private static final String	TO_AverageInvoice = "I";
	private static final String	TO_LiFo = "L";
	private static final String	TO_PriceListLimit = "LL";
	private static final String	TO_StandardCost = "S";
	private static final String	TO_FutureStandardCost = "f";
	private static final String	TO_LastInvoicePrice = "i";
	private static final String	TO_LastPOPrice = "p";
	private static final String	TO_OldStandardCost = "x";
	private static final String TO_CostType = "CT";

	/** Standard Cost Element		*/
	private MCostElement 	m_ce = null;
	/** Client Accounting SChema	*/
	private MAcctSchema[]	m_ass = null;
	/** Map of Cost Elements		*/
	private HashMap<String,MCostElement>	m_ces = new HashMap<String,MCostElement>();
	
	
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
		//	log.fine("prepare - " + para[i]);
			if (element.getParameter() == null)
				;
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = element.getParameterAsInt();
			else if (name.equals("SetFutureCostTo"))
				p_SetFutureCostTo = (String)element.getParameter();
			else if (name.equals("SetStandardCostTo"))
				p_SetStandardCostTo = (String)element.getParameter();
			else if (name.equals("M_PriceList_Version_ID"))
				p_M_PriceList_Version_ID = element.getParameterAsInt();
			else if (name.equals("M_CostType_ID"))
				p_M_CostType_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);		
		}

	}	//	prepare	

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		boolean success = false;
		log.info("M_Product_Category_ID=" + p_M_Product_Category_ID
			+ ", Future=" + p_SetFutureCostTo
			+ ", Standard=" + p_SetStandardCostTo
			+ "; M_PriceList_Version_ID=" + p_M_PriceList_Version_ID);
		if (p_SetFutureCostTo == null)
			p_SetFutureCostTo = "";
		if (p_SetStandardCostTo == null)
			p_SetStandardCostTo = "";
		//	Nothing to Do
		if (p_SetFutureCostTo.length() == 0 && p_SetStandardCostTo.length() == 0)
		{
			return "-";
		}
		//	PLV required
		if (p_M_PriceList_Version_ID == 0
			&& (p_SetFutureCostTo.equals(TO_PriceListLimit) || p_SetStandardCostTo.equals(TO_PriceListLimit)))
			throw new CompiereUserException ("@FillMandatory@  @M_PriceList_Version_ID@");
		
		// Cost Type Required
		if(p_M_CostType_ID == 0 
			&& (p_SetFutureCostTo.equals(TO_CostType) || p_SetStandardCostTo.equals(TO_CostType)))
			throw new CompiereUserException("@FillMandatory@  Cost type");
		
		//	Validate Source
		if (!isValid(p_SetFutureCostTo))
			throw new CompiereUserException ("@NotFound@ @M_CostElement_ID@ (Future) " + p_SetFutureCostTo);
		if (!isValid(p_SetStandardCostTo))
			throw new CompiereUserException ("@NotFound@ @M_CostElement_ID@ (Standard) " + p_SetStandardCostTo);
		
		// Check for unprocessed Cost Update transactions. If one exists, then error out
		if (!isUnprocessedExist())
			throw new CompiereUserException (" Unprocessed/unposted Cost Update transaction exists. Please process them first");

		//	Prepare
		MClient client = MClient.get(getCtx());
		m_ce = MCostElement.getMaterialCostElement(client, X_C_AcctSchema.COSTINGMETHOD_StandardCosting);
		if (m_ce.get_ID() == 0)
			throw new CompiereUserException ("@NotFound@ @M_CostElement_ID@ (StdCost)");
		log.config(m_ce.toString());
		m_ass = MAcctSchema.getClientAcctSchema(getCtx(), client.getAD_Client_ID());
		for (MAcctSchema element : m_ass)
			createNew(element);
		commit();
		
		int counter = 0;
		//	Update Cost
		 counter = update();	
		
		// Create Document 
		if (counter !=0)
		{
			success = createDoc();
		}
				
		// Commit or Roll back the changes.
		if (success)
				get_Trx().commit();
		else {
				get_Trx().rollback();
				throw new CompiereUserException("Error in updating standard cost");
		}
		
		log.info("#" + counter);
		addLog(0, null, new BigDecimal(counter), "@Updated@");

		return "#" + counter;
	}	//	doIt
	
	/**
	 * 	Costing Method must exist
	 *	@param to test
	 *	@return true valid
	 */
	private boolean isValid(String to)
	{
		if (p_SetFutureCostTo.length() == 0)
			return true;
		
		if (to.equals(TO_AverageInvoiceHistory))
			to = TO_AverageInvoice;
		if (to.equals(TO_AveragePOHistory))
			to = TO_AveragePO;
		if (to.equals(TO_FutureStandardCost))
			to = TO_StandardCost;
		//
		if (to.equals(TO_AverageInvoice)
			|| to.equals(TO_AveragePO)
			|| to.equals(TO_FiFo)
			|| to.equals(TO_LiFo)
			|| to.equals(TO_StandardCost))
		{
			MCostElement ce = getCostElement(p_SetFutureCostTo);
			return ce != null;
		}
		return true;
	}	//	isValid
	
	/**************************************************************************
	 * 	Create New Standard Costs
	 * 	@param as accounting schema
	 */
	private void createNew (MAcctSchema as)
	{
		if (!as.getCostingLevel().equals(X_C_AcctSchema.COSTINGLEVEL_Tenant))
		{
			String txt = "Costing Level prevents creating new Costing records for " + as.getName();
			log.warning(txt);
			addLog(0, null, null, txt);
			return;
		}
		
		String sql = "SELECT * FROM M_Product p "
			+ "WHERE NOT EXISTS (SELECT * FROM M_Cost c WHERE c.M_Product_ID=p.M_Product_ID"
			+ " AND c.M_CostType_ID=? AND c.C_AcctSchema_ID=? AND c.M_CostElement_ID=?"
			+ " AND c.M_AttributeSetInstance_ID=0) "
			+ " AND AD_Client_ID=?"
			+ " AND ProductType <> 'R'";
		if (p_M_Product_Category_ID != 0)
			sql += " AND M_Product_Category_ID=?"; 
		int counter = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, as.getM_CostType_ID());
			pstmt.setInt (2, as.getC_AcctSchema_ID());
			pstmt.setInt (3, m_ce.getM_CostElement_ID());
			pstmt.setInt (4, as.getAD_Client_ID());
			if (p_M_Product_Category_ID != 0)
				pstmt.setInt (5, p_M_Product_Category_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if (createNew (new MProduct (getCtx(), rs, get_Trx()), as))
					counter++;
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		log.info("#" + counter);
		addLog(0, null, new BigDecimal(counter), "Created for " + as.getName());
	}	//	createNew
	
	/**
	 * 	Create New Client level Costing Record
	 *	@param product product
	 *	@param as acct schema
	 *	@return true if created
	 */
	private boolean createNew (MProduct product, MAcctSchema as)
	{
		MCost cost = MCost.get(product, 0, as, 0, m_ce.getM_CostElement_ID());
		if (cost.is_new())
			return cost.save();
		return false;
	}	//	createNew

	/**************************************************************************
	 * 	Update Cost Records
	 * 	@return no updated
	 */
	private int update()
	{
		int counter = 0;
		String sql = "SELECT * FROM M_Cost c WHERE M_CostElement_ID=?" 
			       + " AND AD_Client_ID = ?";
		if (p_M_Product_Category_ID != 0)
			sql += " AND EXISTS (SELECT * FROM M_Product p "
				+ " WHERE c.M_Product_ID=p.M_Product_ID AND p.M_Product_Category_ID=? AND p.AD_Client_ID = c.AD_Client_ID)";
		if((p_SetStandardCostTo.length()>0 && p_SetStandardCostTo.equals(TO_PriceListLimit)&& p_SetFutureCostTo.length()==0)||
		   (p_SetFutureCostTo.length()>0 && p_SetFutureCostTo.equals(TO_PriceListLimit)&& p_SetStandardCostTo.length()==0)||
		   (p_SetFutureCostTo.length()>0 && p_SetFutureCostTo.equals(TO_PriceListLimit)&& p_SetStandardCostTo.length()>0 && p_SetStandardCostTo.equals(TO_PriceListLimit)  ))
		{
			sql += " AND EXISTS (SElECT * FROM M_ProductPrice pp "
				+  " WHERE c.M_Product_ID = pp.M_Product_ID AND c.AD_Client_ID = pp.AD_Client_ID "
				+  " AND pp.M_PriceList_Version_ID = ? ) ";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int count =1;
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (count++, m_ce.getM_CostElement_ID());
			pstmt.setInt (count++, getAD_Client_ID());
			if (p_M_Product_Category_ID != 0)
				pstmt.setInt (count++, p_M_Product_Category_ID);
			if((p_SetStandardCostTo.length()>0 && p_SetStandardCostTo.equals(TO_PriceListLimit)&& p_SetFutureCostTo.length()==0)||
					   (p_SetFutureCostTo.length()>0 && p_SetFutureCostTo.equals(TO_PriceListLimit)&& p_SetStandardCostTo.length()==0)||
					   (p_SetFutureCostTo.length()>0 && p_SetFutureCostTo.equals(TO_PriceListLimit)&& p_SetStandardCostTo.length()>0 && p_SetStandardCostTo.equals(TO_PriceListLimit)))
				pstmt.setInt(count++, p_M_PriceList_Version_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				boolean result = false;
				MCost cost = new MCost (getCtx(), rs, get_TrxName());
				for (MAcctSchema element : m_ass) {
					//	Update Costs only for default Cost Type
					if (element.getC_AcctSchema_ID() == cost.getC_AcctSchema_ID() 
						&& element.getM_CostType_ID() == cost.getM_CostType_ID())
					{
						if (p_SetFutureCostTo.equals(TO_CostType) || p_SetStandardCostTo.equals(TO_CostType))
							result = updateFromCostType(cost);
						if(!p_SetFutureCostTo.equals(TO_CostType) || !p_SetStandardCostTo.equals(TO_CostType))
							result = update (cost);
						
						if (result)
							counter++;
						else
							return 0;
						
					}
				}
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return counter;
	}	//	update

	/**
	 * 	Update Cost Records
	 *	@param cost cost
	 *	@return true if updated
	 *	@throws Exception
	 */
	private boolean update (MCost cost) throws Exception
	{
		boolean updated = false;
		
		// if neither standard or future cost are to be set return
		if((p_SetStandardCostTo.length() == 0 || p_SetStandardCostTo.equals(TO_CostType))&&
				(p_SetFutureCostTo.length() == 0 || p_SetFutureCostTo.equals(TO_CostType)))
				return true;
		
		// update Last cost price if not from cost type
		if(!p_SetStandardCostTo.equals(TO_CostType))
		{
			BigDecimal lastCostPrice = cost.getCurrentCostPrice();
			cost.setLastCostPrice(lastCostPrice);
		}
				
		// get current standard cost
		if (p_SetFutureCostTo.equals(p_SetStandardCostTo))
		{
			BigDecimal costs = getCosts(cost, p_SetFutureCostTo);
			if (costs != null)
			{
				cost.setFutureCostPrice(costs);
				cost.setCurrentCostPrice(costs);
				updated = true;
			}
			else
				updated = true;
		}
		else
		{
			if (p_SetStandardCostTo.length() > 0 && !p_SetStandardCostTo.equals(TO_CostType))
			{
				BigDecimal costs = getCosts(cost, p_SetStandardCostTo);
				if (costs != null)
				{   cost.setCurrentCostPrice(costs);
					updated = true;
				}
				else
					updated = true;
			}
			if (p_SetFutureCostTo.length() > 0 && !p_SetFutureCostTo.equals(TO_CostType))
			{
				BigDecimal costs = getCosts(cost, p_SetFutureCostTo);
				if (costs != null)
				{
					cost.setFutureCostPrice(costs);
					updated = true;
				}
				else
					updated = true;
			}
		}
		if (updated)
			updated = cost.save(get_TrxName());
			
		return updated;
	}	//	update
	
	/**
	 * 	Get Costs
	 *	@param cost cost
	 *	@param to where to get costs from 
	 *	@return costs (could be 0) or null if not found
	 *	@throws Exception
	 */
	private BigDecimal getCosts (MCost cost, String to) throws Exception
	{
		BigDecimal retValue = null;
		
		//	Average Invoice
		if (to.equals(TO_AverageInvoice))
		{
			MCostElement ce = getCostElement(TO_AverageInvoice);
			if (ce == null)
				throw new CompiereSystemException("CostElement not found: " + TO_AverageInvoice);
			MCost xCost = MCost.get(getCtx(), cost.getAD_Client_ID(), cost.getAD_Org_ID(), cost.getM_Product_ID(), cost.getM_CostType_ID(), cost.getC_AcctSchema_ID(), ce.getM_CostElement_ID(), cost.getM_AttributeSetInstance_ID());
			if (xCost != null)
				retValue = xCost.getCurrentCostPrice();
		}
		//	Average Invoice History
		else if (to.equals(TO_AverageInvoiceHistory))
		{
			MCostElement ce = getCostElement(TO_AverageInvoice);
			if (ce == null)
				throw new CompiereSystemException("CostElement not found: " + TO_AverageInvoice);
			MCost xCost = MCost.get(getCtx(), cost.getAD_Client_ID(), cost.getAD_Org_ID(), cost.getM_Product_ID(), cost.getM_CostType_ID(), cost.getC_AcctSchema_ID(), ce.getM_CostElement_ID(), cost.getM_AttributeSetInstance_ID());
			if (xCost != null) 
				retValue = xCost.getHistoryAverage();
		}
		
		//	Average PO
		else if (to.equals(TO_AveragePO))
		{
			MCostElement ce = getCostElement(TO_AveragePO);
			if (ce == null)
				throw new CompiereSystemException("CostElement not found: " + TO_AveragePO);
			MCost xCost = MCost.get(getCtx(), cost.getAD_Client_ID(), cost.getAD_Org_ID(), cost.getM_Product_ID(), cost.getM_CostType_ID(), cost.getC_AcctSchema_ID(), ce.getM_CostElement_ID(), cost.getM_AttributeSetInstance_ID());
			if (xCost != null)
				retValue = xCost.getCurrentCostPrice();
		}
		//	Average PO History
		else if (to.equals(TO_AveragePOHistory))
		{
			MCostElement ce = getCostElement(TO_AveragePO);
			if (ce == null)
				throw new CompiereSystemException("CostElement not found: " + TO_AveragePO);
			MCost xCost = MCost.get(getCtx(), cost.getAD_Client_ID(), cost.getAD_Org_ID(), cost.getM_Product_ID(), cost.getM_CostType_ID(), cost.getC_AcctSchema_ID(), ce.getM_CostElement_ID(), cost.getM_AttributeSetInstance_ID());
			if (xCost != null) 
				retValue = xCost.getHistoryAverage();
		}
		
		//	FiFo
		else if (to.equals(TO_FiFo))
		{
			MCostElement ce = getCostElement(TO_FiFo);
			if (ce == null)
				throw new CompiereSystemException("CostElement not found: " + TO_FiFo);
			MCost xCost = MCost.get(getCtx(), cost.getAD_Client_ID(), cost.getAD_Org_ID(), cost.getM_Product_ID(), cost.getM_CostType_ID(), cost.getC_AcctSchema_ID(), ce.getM_CostElement_ID(), cost.getM_AttributeSetInstance_ID());
			if (xCost != null)
				retValue = xCost.getCurrentCostPrice();
		}

		//	Future Std Costs
		else if (to.equals(TO_FutureStandardCost))
			retValue = cost.getFutureCostPrice();
		
		//	Last Inv Price
		else if (to.equals(TO_LastInvoicePrice))
		{
			MCostElement ce = getCostElement(TO_LastInvoicePrice);
			if (ce != null)
			{
				MCost xCost = MCost.get(getCtx(), cost.getAD_Client_ID(), cost.getAD_Org_ID(), cost.getM_Product_ID(), cost.getM_CostType_ID(), cost.getC_AcctSchema_ID(), ce.getM_CostElement_ID(), cost.getM_AttributeSetInstance_ID());
				if (xCost != null)
					retValue = xCost.getCurrentCostPrice();
			}
			if (retValue == null)
			{
				MProduct product = MProduct.get(getCtx(), cost.getM_Product_ID());
				MAcctSchema as = MAcctSchema.get(getCtx(), cost.getC_AcctSchema_ID());
				retValue = MCost.getLastInvoicePrice(product, 
					cost.getM_AttributeSetInstance_ID(), cost.getAD_Org_ID(), as.getC_Currency_ID());				
			}
		}
		
		//	Last PO Price
		else if (to.equals(TO_LastPOPrice))
		{
			MCostElement ce = getCostElement(TO_LastPOPrice);
			if (ce != null)
			{
				MCost xCost = MCost.get(getCtx(), cost.getAD_Client_ID(), cost.getAD_Org_ID(), cost.getM_Product_ID(), cost.getM_CostType_ID(), cost.getC_AcctSchema_ID(), ce.getM_CostElement_ID(), cost.getM_AttributeSetInstance_ID());
				if (xCost != null)
					retValue = xCost.getCurrentCostPrice();
			}
			if (retValue == null)
			{
				MProduct product = MProduct.get(getCtx(), cost.getM_Product_ID());
				MAcctSchema as = MAcctSchema.get(getCtx(), cost.getC_AcctSchema_ID());
				retValue = MCost.getLastPOPrice(product, 
					cost.getM_AttributeSetInstance_ID(), cost.getAD_Org_ID(), as.getC_Currency_ID());				
			}
		}
	
		//	FiFo
		else if (to.equals(TO_LiFo))
		{
			MCostElement ce = getCostElement(TO_LiFo);
			if (ce == null)
				throw new CompiereSystemException("CostElement not found: " + TO_LiFo);
			MCost xCost = MCost.get(getCtx(), cost.getAD_Client_ID(), cost.getAD_Org_ID(), cost.getM_Product_ID(), cost.getM_CostType_ID(), cost.getC_AcctSchema_ID(), ce.getM_CostElement_ID(), cost.getM_AttributeSetInstance_ID());
			if (xCost != null)
				retValue = xCost.getCurrentCostPrice();
		}
		
		//	Old Std Costs
		else if (to.equals(TO_OldStandardCost))
			retValue = getOldCurrentCostPrice(cost);
		
		//	Price List
		else if (to.equals(TO_PriceListLimit))
			retValue = getPrice(cost);
		
		//	Standard Costs
		else if (to.equals(TO_StandardCost))
			retValue = cost.getCurrentCostPrice();
		
/*		if(retValue == null)
			retValue = Env.ZERO;*/
		
		return retValue;
	}	//	getCosts
	
	
	/**
	 * 	Get Cost Element
	 *	@param CostingMethod method
	 *	@return costing element or null
	 */
	private MCostElement getCostElement (String CostingMethod)
	{
		MCostElement ce = m_ces.get(CostingMethod);
		if (ce == null)
		{
			ce = MCostElement.getMaterialCostElement(getCtx(), CostingMethod);
			m_ces.put(CostingMethod, ce);
		}
		return ce;
	}	//	getCostElement

	/**
	 * 	Get Old Current Cost Price
	 *	@param cost costs
	 *	@return price if found
	 */
	private BigDecimal getOldCurrentCostPrice(MCost cost)
	{
		BigDecimal retValue = null;
		String sql = "SELECT CostStandard, CurrentCostPrice "
			+ "FROM M_Product_Costing "
			+ "WHERE M_Product_ID=? AND C_AcctSchema_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, cost.getM_Product_ID());
			pstmt.setInt (2, cost.getC_AcctSchema_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = rs.getBigDecimal(1);
				if (retValue == null || retValue.signum() == 0)
					retValue = rs.getBigDecimal(2);
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	//	getOldCurrentCostPrice

	/**
	 * 	Get Price from Price List
	 * 	@param cost cost record
	 *	@return price or null
	 */
	private BigDecimal getPrice (MCost cost)
	{
		BigDecimal retValue = null;
		String sql = "SELECT PriceLimit "
			+ "FROM M_ProductPrice "
			+ "WHERE M_Product_ID=? AND M_PriceList_Version_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, cost.getM_Product_ID());
			pstmt.setInt (2, p_M_PriceList_Version_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = rs.getBigDecimal(1);
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return retValue;
	}	//	getPrice
	
	private boolean createDoc(){

		// Create Header for Cost Update Doc.
		MCostUpdate CostUpdate = new MCostUpdate(getCtx(),0,get_TrxName());
		CostUpdate.setM_Product_Category_ID(p_M_Product_Category_ID);
		CostUpdate.setDateAcct(CostUpdate.getCreated());
		CostUpdate.save(get_TrxName());
		
		// Create Lines for Cost Update Doc
		String sql = "SELECT M_Product_ID FROM M_Product p "
			       + " WHERE p.AD_Client_ID = ? "
			       + " AND EXISTS (SELECT 1 FROM M_Cost c WHERE c.M_Product_ID = p.M_Product_ID"
			                     + " AND c.AD_Client_ID = p.AD_Client_ID"
			                     + " AND c.M_CostElement_ID = ?)";
		if (p_M_Product_Category_ID !=0)
			sql += " AND M_Product_Category_ID=?";
		if((p_SetStandardCostTo.length()>0 && p_SetStandardCostTo.equals(TO_PriceListLimit)&& p_SetFutureCostTo.length()==0)||
				   (p_SetFutureCostTo.length()>0 && p_SetFutureCostTo.equals(TO_PriceListLimit)&& p_SetStandardCostTo.length()==0)||
				   (p_SetFutureCostTo.length()>0 && p_SetFutureCostTo.equals(TO_PriceListLimit)&& p_SetStandardCostTo.length()>0 && p_SetStandardCostTo.equals(TO_PriceListLimit)  ))
				{
					sql += " AND EXISTS (SElECT * FROM M_ProductPrice pp "
						+  " WHERE p.M_Product_ID = pp.M_Product_ID AND p.AD_Client_ID = pp.AD_Client_ID "
						+  " AND pp.M_PriceList_Version_ID = ? ) ";
				}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int count =1;
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (count++, getAD_Client_ID());
			pstmt.setInt (count++, m_ce.getM_CostElement_ID());
			
			if (p_M_Product_Category_ID != 0)
				pstmt.setInt (count++, p_M_Product_Category_ID);
			if((p_SetStandardCostTo.length()>0 && p_SetStandardCostTo.equals(TO_PriceListLimit)&& p_SetFutureCostTo.length()==0)||
					   (p_SetFutureCostTo.length()>0 && p_SetFutureCostTo.equals(TO_PriceListLimit)&& p_SetStandardCostTo.length()==0)||
					   (p_SetFutureCostTo.length()>0 && p_SetFutureCostTo.equals(TO_PriceListLimit)&& p_SetStandardCostTo.length()>0 && p_SetStandardCostTo.equals(TO_PriceListLimit)  ))
				pstmt.setInt(count++, p_M_PriceList_Version_ID);
			
			rs = pstmt.executeQuery ();
			while (rs.next())
			{
			    int M_Product_ID = rs.getInt(1);
				MProduct p = MProduct.get(getCtx(), M_Product_ID);
				MCostUpdateLine l = new MCostUpdateLine(getCtx(),CostUpdate,get_TrxName());
				l.setM_Product_ID(M_Product_ID);
				l.setC_UOM_ID(p.getC_UOM_ID());
				l.setProcessed(true);
				l.save(get_TrxName());
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
			return false;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;	
	}
	
	private boolean isUnprocessedExist()
	{
		boolean retVal = true;
		String sql = " SELECT count(*) FROM M_CostUpdate"
			       + " WHERE (processed <> 'Y' OR posted <> 'Y')"
			       + " AND AD_Client_ID = ? ";
		if (p_M_Product_Category_ID !=0)
			sql += " AND (M_Product_Category_ID = ? OR M_Product_Category_ID IS NULL)";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getAD_Client_ID());
			if (p_M_Product_Category_ID != 0)
				pstmt.setInt (2, p_M_Product_Category_ID);
			rs = pstmt.executeQuery ();
			if (rs.next())
			    retVal= (rs.getInt(1)==0);
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
			return false;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retVal;
	}
	
	private boolean updateFromCostType(MCost cost)
	{
		MAcctSchema as = MAcctSchema.get(getCtx(), cost.getC_AcctSchema_ID());
		int precision = as.getCostingPrecision();
		if(as.getM_CostType_ID()!= cost.getM_CostType_ID())
			return true;
		MProduct product = new MProduct(getCtx(),cost.getM_Product_ID(),get_TrxName());
		
		//Process only manufactured items.
		if(!product.isManufactured())
			return true;
		int materialCostElement = MCostElement.getMfgCostElement(getAD_Client_ID());
		
		if(materialCostElement == 0)
			return false;
		
		BigDecimal nonPercentCost = Env.ZERO;
		BigDecimal percentCost = Env.ZERO;
		int BatchSize = product.getBatchSize();
		
		int org_id = 0;
		int attributeInstanceid = 0;
		String CostingLevel = getCostingLevel(product, as);
		
		if(CostingLevel.equals(X_C_AcctSchema.COSTINGLEVEL_BatchLot))
		{
			attributeInstanceid = cost.getM_AttributeSetInstance_ID();
			org_id = cost.getAD_Org_ID();
		}
		else if(CostingLevel.equals(X_C_AcctSchema.COSTINGLEVEL_Organization)) // use org stamped on product
		{
			org_id = cost.getAD_Org_ID();
			attributeInstanceid = 0;
		}
		else if (CostingLevel.equals(X_C_AcctSchema.COSTINGLEVEL_Tenant))
		{
			org_id = 0;
			attributeInstanceid = 0;
		}

		MCost [] sourceCosts = MCost.get(attributeInstanceid, as,p_M_CostType_ID,org_id,product);

		if (sourceCosts == null || sourceCosts.length ==0) // nothing to do
		{
		    log.log(Level.FINE,product.getName() + " :- "
		    		+ " Cost not found" );
			return true;
		}
		
		// get all costs for the accounting cost type and delete old records except material cost.
		MCost[] toCosts = MCost.get(attributeInstanceid, as, as.getM_CostType_ID(),
				                    cost.getAD_Org_ID(), product);
		
		BigDecimal oldMaterialCost = BigDecimal.ZERO;
		for(MCost toCost :toCosts)
		{
			//Sum up the material cost and delete the rows.
			if(toCost.getAD_Org_ID() == cost.getAD_Org_ID() &&
					toCost.getM_AttributeSetInstance_ID() == cost.getM_AttributeSetInstance_ID())
			{
				{
					if(toCost.getM_CostElement_ID() == cost.getM_CostElement_ID())
						oldMaterialCost.add(toCost.getCurrentCostPrice());
					if(!toCost.delete(false,get_TrxName()))
						return false;
				}
			}
		}
		
		for (MCost sourceCost : sourceCosts)
		{
			if(sourceCost.getAD_Org_ID() == cost.getAD_Org_ID() &&
					sourceCost.getM_AttributeSetInstance_ID() == cost.getM_AttributeSetInstance_ID())
			{
				MCost cc = new MCost(getCtx(),0,get_TrxName());
				cc.setAD_Client_ID(sourceCost.getAD_Client_ID());
				cc.setAD_Org_ID(org_id);
				cc.setM_Product_ID(product.getM_Product_ID());
				cc.setM_CostType_ID(as.getM_CostType_ID());
				
				if(sourceCost.getM_CostElement_ID() == m_ce.getM_CostElement_ID())
					cc.setM_CostElement_ID(materialCostElement);
				else
					cc.setM_CostElement_ID(sourceCost.getM_CostElement_ID());
				
				cc.setC_AcctSchema_ID(as.getC_AcctSchema_ID());
				cc.setM_AttributeSetInstance_ID(attributeInstanceid);
				cc.setBasisType(sourceCost.getBasisType());
				
				if(sourceCost.getIsThisLevel().equals("Y") && sourceCost.getIsUserDefined().equals("Y"))
					cc.setIsUserDefined("N");
				else if (sourceCost.getIsThisLevel().equals("Y") && sourceCost.getIsUserDefined().equals("N"))
					cc.setIsUserDefined("Y");
				else if (sourceCost.getIsThisLevel().equals("N") && sourceCost.getIsUserDefined().equals("N"))
					cc.setIsUserDefined("N");
				
				cc.setIsThisLevel(sourceCost.getIsThisLevel());
				cc.setPercentCost(sourceCost.getPercentCost());

				if(p_SetStandardCostTo.equals(TO_CostType))
					cc.setCurrentCostPrice(sourceCost.getCurrentCostPrice());
				else
					cc.setCurrentCostPrice(BigDecimal.ZERO);
				
				if(p_SetFutureCostTo.equals(TO_CostType))
					cc.setFutureCostPrice(sourceCost.getFutureCostPrice());
				else
					cc.setFutureCostPrice(BigDecimal.ZERO);
				
				if(!cc.save(get_TrxName()))
					return false;
				
				if(sourceCost.getCurrentCostPrice() != null && sourceCost.getCurrentCostPrice().signum()!=0)
				{
					if(sourceCost.getBasisType().equals(X_M_Cost.BASISTYPE_PerItem))
					{
						nonPercentCost = nonPercentCost.add(sourceCost.getCurrentCostPrice());
					}
					else
					{
						nonPercentCost = nonPercentCost.add(sourceCost.getCurrentCostPrice().divide(new BigDecimal(BatchSize),precision,BigDecimal.ROUND_HALF_UP));
					}
				}
				if(sourceCost.getPercentCost() != null && sourceCost.getPercentCost().signum()!=0)
				{
					if(sourceCost.getBasisType().equals(X_M_Cost.BASISTYPE_PerItem))
					{
						percentCost = percentCost.add(sourceCost.getPercentCost());
					}
					else
					{
						percentCost = percentCost.add(sourceCost.getPercentCost().divide(new BigDecimal(BatchSize),precision,BigDecimal.ROUND_HALF_UP));
					}

				}

			}
		}
		
		BigDecimal totalCost = nonPercentCost.add(percentCost.multiply(nonPercentCost).divide(Env.ONEHUNDRED));

		MCost cc = new MCost(getCtx(),0,get_TrxName());
		cc.setAD_Client_ID(cost.getAD_Client_ID());
		cc.setAD_Org_ID(org_id);
		cc.setM_Product_ID(product.getM_Product_ID());
		cc.setM_CostType_ID(as.getM_CostType_ID());
		cc.setM_CostElement_ID(cost.getM_CostElement_ID());
		cc.setC_AcctSchema_ID(as.getC_AcctSchema_ID());
		cc.setM_AttributeSetInstance_ID(attributeInstanceid);
		cc.setIsThisLevel("Y");
		cc.setIsUserDefined("N");
		cc.setBasisType(X_M_Cost.BASISTYPE_PerItem);
		cc.setCurrentCostPrice(totalCost);
		cc.setLastCostPrice(cost.getCurrentCostPrice());
		cc.setCurrentQty(cost.getCurrentQty());
		cc.setPercentCost(Env.ZERO);
		cc.setCumulatedAmt(cost.getCumulatedAmt());
        cc.setCumulatedQty(cost.getCumulatedQty());
        cc.setDescription(cost.getDescription());
		if(!cc.save(get_TrxName()))
			return false;

		return true;
	}
	
	private String getCostingLevel(MProduct product,MAcctSchema as)
	{
		log.log(Level.FINE,"Get costing level for product " + product.getName() + 
				" in accounting schema " + as.getName());
		String CostingLevel = as.getCostingLevel();
		MProductCategoryAcct pca = MProductCategoryAcct.get (product.getCtx(),
			product.getM_Product_Category_ID(), as.getC_AcctSchema_ID(), get_TrxName());	
		if (pca == null)
			throw new CompiereStateException("Cannot find Acct for M_Product_Category_ID=" 
				+ product.getM_Product_Category_ID() 
				+ ", C_AcctSchema_ID=" + as.getC_AcctSchema_ID());
		//	Costing Level
		if (pca.getCostingLevel() != null)
			CostingLevel = pca.getCostingLevel();

		return CostingLevel;
	}
}	//	CostUpdate
