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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;

/**
 *  Physical Inventory Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInventory.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MInventory extends X_M_Inventory implements DocAction
{
    /** Logger for class MInventory */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInventory.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Inventory from Cache
	 *	@param ctx context
	 *	@param M_Inventory_ID id
	 *	@return MInventory
	 */
	public static MInventory get (Ctx ctx, int M_Inventory_ID)
	{
		Integer key = Integer.valueOf (M_Inventory_ID);
		MInventory retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MInventory (ctx, M_Inventory_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static final CCache<Integer,MInventory> s_cache = new CCache<Integer,MInventory>("M_Inventory", 5, 5);


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Inventory_ID id
	 *	@param trx transaction
	 */
	public MInventory (Ctx ctx, int M_Inventory_ID, Trx trx)
	{
		super (ctx, M_Inventory_ID, trx);
		if (M_Inventory_ID == 0)
		{
		//	setName (null);
		//  setM_Warehouse_ID (0);		//	FK
			setMovementDate (new Timestamp(System.currentTimeMillis()));
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
			setIsApproved (false);
			setMovementDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
			setPosted (false);
			setProcessed (false);
		}
	}	//	MInventory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MInventory (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MInventory

	/**
	 * 	Warehouse Constructor
	 *	@param wh warehouse
	 */
	public MInventory (MWarehouse wh)
	{
		this (wh.getCtx(), 0, wh.get_Trx());
		setClientOrg(wh);
		setM_Warehouse_ID(wh.getM_Warehouse_ID());
	}	//	MInventory


	/**	Lines						*/
	private MInventoryLine[]	m_lines = null;

	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return array of lines
	 */
	public MInventoryLine[] getLines (boolean requery)
	{
		if ((m_lines != null) && !requery)
			return m_lines;
		//
		ArrayList<MInventoryLine> list = new ArrayList<MInventoryLine>();
		String sql = "SELECT * FROM M_InventoryLine WHERE M_Inventory_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_Inventory_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MInventoryLine (getCtx(), rs, get_Trx()));
		} catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		m_lines = new MInventoryLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines

	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return array of lines
	 */
	public MInventoryLine[] getLinesByProduct ()
	{
		//getLines
		ArrayList<MInventoryLine> list = new ArrayList<MInventoryLine>();
		String sql = "SELECT * FROM M_InventoryLine WHERE M_Inventory_ID=? " +
					 "ORDER BY M_Product_ID, M_Locator_ID, M_AttributeSetInstance_ID ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_Inventory_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MInventoryLine (getCtx(), rs, get_Trx()));
		} 
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MInventoryLine[] lines = new MInventoryLine[list.size ()];
		list.toArray (lines);
		return lines;
	}	//	getLines

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
	 * 	Overwrite Client/Org - from Import.
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	@Override
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInventory[");
		sb.append (get_ID())
			.append ("-").append (getDocumentNo())
			.append (",M_Warehouse_ID=").append(getM_Warehouse_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getC_DocType_ID() == 0)
		{
			MDocType types[] = MDocType.getOfDocBaseType(getCtx(), MDocBaseType.DOCBASETYPE_MaterialPhysicalInventory);
			if (types.length > 0)	//	get first
				setC_DocType_ID(types[0].getC_DocType_ID());
			else
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @C_DocType_ID@"));
				return false;
			}
		}

		//	Warehouse Org
		if (newRecord
			|| is_ValueChanged("AD_Org_ID") || is_ValueChanged("M_Warehouse_ID"))
		{
			MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
			if (wh.getAD_Org_ID() != getAD_Org_ID())
			{
				log.saveError("WarehouseOrgConflict", "");
				return false;
			}
		}

		return true;
	}	//	beforeSave
	
	protected boolean beforeDelete()
	{
		MInventoryLine [] lines = getLines(true);
		for(MInventoryLine line :lines)
		{
			if(line.getM_ABCAnalysisGroup_ID()!=0)
			{
				MABCProductAssignment p = MABCProductAssignment.getForProduct(getCtx(), line.getM_Product_ID(), 
						                                            line.getM_ABCAnalysisGroup_ID(), get_Trx());
				p.setProcessing(false);
				if(!p.save(get_Trx()))
					return false;
				MCycleCountLock.unLock(this, line);
				MCycleCountLock.delete(this,line);
			}
		}
		return true;
	}


	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	@Override
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String sql = "UPDATE M_InventoryLine SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE M_Inventory_ID=? ";
		int noLine = DB.executeUpdate(get_Trx(), sql,getM_Inventory_ID());
		m_lines = null;
		log.fine("Processed=" + processed + " - Lines=" + noLine);
	}	//	setProcessed


	/**	Process Message 			*/
	private String		m_processMsg = null;

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt()
	{
		log.info(toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		//	TODO: Add up Amounts
	//	setApprovalAmt();


		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocActionConstants.STATUS_InProgress;
	}	//	prepareIt

	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt

	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		// Release the lock on the document to allow changes in the inventory.
		if(this.getIsLocked().equals("Y"))
			MCycleCountLock.unLock(this,null);
		
		MInventoryLine[] lines = getLinesByProduct();
		for (MInventoryLine line : lines) {
			
			// Check for lock by other physical inventory document
			MCycleCountLock lock = MCycleCountLock.getLock(getCtx(), this, line.getM_Product_ID(), 
					                                       line.getM_Locator_ID(), get_Trx());
			if(lock==null)
			{
				if(MCycleCountLock.lockExists(getCtx(), line.getM_Product_ID(), line.getM_Locator_ID(),get_Trx()))
				{
					m_processMsg = Msg.getMsg(getCtx(), "LocatorLocked");
					return DocActionConstants.STATUS_Invalid;
				}
			}
			
			if (!line.isActive())
				continue;

			line.createMA(false);

			MTransaction p_trx = null;
			if (line.getM_AttributeSetInstance_ID() == 0)
			{
				BigDecimal qtyDiff = line.getQtyInternalUse().negate();

				if (qtyDiff.signum() == 0)
					qtyDiff = line.getQtyCount().subtract(line.getQtyBook());
				//
				if (qtyDiff.signum() > 0)
				{
					//	Storage
					MStorageDetail storageOnHand = MStorageDetail.getCreate(getCtx(), line.getM_Locator_ID(),
							line.getM_Product_ID(), 0, X_Ref_Quantity_Type.ON_HAND, get_Trx());
					BigDecimal qtyNew = storageOnHand.getQty().add(qtyDiff);
					log.fine("Diff=" + qtyDiff
						+ " - OnHand=" + storageOnHand.getQty() + "->" + qtyNew);
					storageOnHand.setQty(qtyNew);
					storageOnHand.setDateLastInventory(getMovementDate());
					if (!storageOnHand.save(get_Trx()))
					{
						ValueNamePair pp = CLogger.retrieveError();
						if (pp != null)
							m_processMsg = pp.getName();
						else
							m_processMsg = "Storage not updated(1)";
						return DocActionConstants.STATUS_Invalid;
					}
					log.fine(storageOnHand.toString());
					//	Transaction
					p_trx = new MTransaction (getCtx(), line.getAD_Org_ID(),
						X_M_Transaction.MOVEMENTTYPE_InventoryIn,
						line.getM_Locator_ID(), line.getM_Product_ID(), 0,
						qtyDiff, getMovementDate(), get_Trx());
					p_trx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
					if (!p_trx.save())
					{
						m_processMsg = "Transaction not inserted(1)";
						return DocActionConstants.STATUS_Invalid;
					}
				}
				else	//	negative qty
				{
					MInventoryLineMA mas[] = MInventoryLineMA.get(getCtx(),
						line.getM_InventoryLine_ID(), get_Trx());
					for (MInventoryLineMA ma : mas) {
						//	Storage
						MStorageDetail storageOnHand = MStorageDetail.getCreate(getCtx(), line.getM_Locator_ID(),
								line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), X_Ref_Quantity_Type.ON_HAND, get_Trx());
						//
						BigDecimal maxDiff = qtyDiff;
						if ((maxDiff.signum() < 0)
							&& (ma.getMovementQty().compareTo(maxDiff.negate()) < 0))
							maxDiff = ma.getMovementQty().negate();
						BigDecimal qtyNew = ma.getMovementQty().add(maxDiff);	//	Storage+Diff
						log.fine("MA Qty=" + ma.getMovementQty()
							+ ",Diff=" + qtyDiff + "|" + maxDiff
							+ " - OnHand=" + storageOnHand.getQty() + "->" + qtyNew
							+ " {" + ma.getM_AttributeSetInstance_ID() + "}");
						//
						storageOnHand.setQty(qtyNew);
						storageOnHand.setDateLastInventory(getMovementDate());
						if (!storageOnHand.save(get_Trx()))
						{
							ValueNamePair pp = CLogger.retrieveError();
							if (pp != null)
								m_processMsg = pp.getName();
							else
								m_processMsg = "Storage not updated (MA)";
							return DocActionConstants.STATUS_Invalid;
						}
						log.fine(storageOnHand.toString());

						//	Transaction
						p_trx = new MTransaction (getCtx(), line.getAD_Org_ID(),
							X_M_Transaction.MOVEMENTTYPE_InventoryIn,
							line.getM_Locator_ID(), line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
							maxDiff, getMovementDate(), get_Trx());
						p_trx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
						if (!p_trx.save())
						{
							m_processMsg = "Transaction not inserted (MA)";
							return DocActionConstants.STATUS_Invalid;
						}
						//
						qtyDiff = qtyDiff.subtract(maxDiff);
						if (qtyDiff.signum() == 0)
							break;
					}
					// nnayak - if the quantity issued was greator than the quantity onhand, we need to create a transaction
					// for the remaining quantity
					if (qtyDiff.signum() != 0)
					{
						MStorageDetail storageOnHand = MStorageDetail.getCreate(getCtx(), line.getM_Locator_ID(),
								line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), X_Ref_Quantity_Type.ON_HAND, get_Trx());
						BigDecimal qtyNew = storageOnHand.getQty().add(qtyDiff);
						log.fine("Count=" + line.getQtyCount()
							+ ",Book=" + line.getQtyBook() + ", Difference=" + qtyDiff
							+ " - OnHand=" + storageOnHand.getQty() + "->" + qtyNew);
						//
						storageOnHand.setQty(qtyNew);

						storageOnHand.setDateLastInventory(getMovementDate());
						if (!storageOnHand.save(get_Trx()))
						{
							ValueNamePair pp = CLogger.retrieveError();
							if (pp != null)
								m_processMsg = pp.getName();
							else
								m_processMsg = "Storage not updated (MA)";
							return DocActionConstants.STATUS_Invalid;
						}
						log.fine(storageOnHand.toString());

						//	Transaction
						p_trx = new MTransaction (getCtx(), line.getAD_Org_ID(),
							X_M_Transaction.MOVEMENTTYPE_InventoryIn,
							line.getM_Locator_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							qtyDiff, getMovementDate(), get_Trx());
						p_trx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
						if (!p_trx.save())
						{
							m_processMsg = "Transaction not inserted (MA)";
							return DocActionConstants.STATUS_Invalid;
						}
						//
					}
				}	//	negative qty
			}

			//	Fallback
			if (p_trx == null)
			{
				//	Storage
				MStorageDetail storageOnHand = MStorageDetail.getCreate(getCtx(), line.getM_Locator_ID(),
						line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), X_Ref_Quantity_Type.ON_HAND, get_Trx());
				//
				BigDecimal qtyDiff = line.getQtyInternalUse().negate();
				if (Env.ZERO.compareTo(qtyDiff) == 0)
					qtyDiff = line.getQtyCount().subtract(line.getQtyBook());
				BigDecimal qtyNew = storageOnHand.getQty().add(qtyDiff);
				log.fine("Count=" + line.getQtyCount()
					+ ",Book=" + line.getQtyBook() + ", Difference=" + qtyDiff
					+ " - OnHand=" + storageOnHand.getQty() + "->" + qtyNew);
				//
				storageOnHand.setQty(qtyNew);
				storageOnHand.setDateLastInventory(getMovementDate());
				if (!storageOnHand.save(get_Trx()))
				{
					ValueNamePair pp = CLogger.retrieveError();
					if (pp != null)
						m_processMsg = pp.getName();
					else
						m_processMsg = "Storage not updated(2)";
					return DocActionConstants.STATUS_Invalid;
				}
				log.fine(storageOnHand.toString());

				//	Transaction
				p_trx = new MTransaction (getCtx(), line.getAD_Org_ID(),
					X_M_Transaction.MOVEMENTTYPE_InventoryIn,
					line.getM_Locator_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
					qtyDiff, getMovementDate(), get_Trx());
				p_trx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
				if (!p_trx.save())
				{
					m_processMsg = "Transaction not inserted(2)";
					return DocActionConstants.STATUS_Invalid;
				}
			}	//	Fallback
			
			if(line.getM_ABCAnalysisGroup_ID()!=0)
			{
				MABCProductAssignment p = MABCProductAssignment.getForProduct(getCtx(), line.getM_Product_ID(),
						                                                       line.getM_ABCAnalysisGroup_ID(), get_Trx());
				p.setProcessing(false);
				p.setIsCounted(true);
				p.setDates(this);
				p.save(get_Trx());
			}

		}	//	for all lines
		
		return DocActionConstants.STATUS_Completed;
	}	//	completeIt


	/**
	 * 	Void Document.
	 * 	@return false
	 */
	public boolean voidIt()
	{
		log.info(toString());
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			return false;
		}
		MInventoryLine[] lines = getLines(false);
		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			//	Set lines to 0
			for (MInventoryLine line : lines) {
				BigDecimal oldCount = line.getQtyCount();
				BigDecimal oldInternal = line.getQtyInternalUse();
				if ((oldCount.compareTo(line.getQtyBook()) != 0)
					|| (oldInternal.signum() != 0))
				{
					line.setQtyInternalUse(Env.ZERO);
					line.setQtyCount(line.getQtyBook());
					line.addDescription("Void (" + oldCount + "/" + oldInternal + ")");
					line.save(get_Trx());
				}
			}
		}
		else
		{
			return DocumentEngine.processIt(this, DocActionConstants.ACTION_Reverse_Correct);
		}

		for (MInventoryLine line : lines) {
			if(line.getM_ABCAnalysisGroup_ID()!=0)
			{
				MABCProductAssignment p = MABCProductAssignment.getForProduct(getCtx(), line.getM_Product_ID(),
                        line.getM_ABCAnalysisGroup_ID(),get_Trx());
				p.setProcessing(false);
				p.setIsCounted(false);
				p.setDateNextCount(null);
				if (!p.save(get_Trx()))
					return false;
			}
		}
		MCycleCountLock.unLock(this,null);
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info(toString());

		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction
	 * 	@return false
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());
		//
		m_processMsg = DocumentEngine.isPeriodOpen(this);
		if (m_processMsg != null)
			return false;

		//	Deep Copy
		MInventory reversal = new MInventory(getCtx(), 0, get_Trx());
		copyValues(this, reversal, getAD_Client_ID(), getAD_Org_ID());
		reversal.setDocStatus(DOCSTATUS_Drafted);
		reversal.setDocAction(DOCACTION_Complete);
		reversal.set_ValueNoCheck("DocumentNo", null);
		reversal.setIsApproved (false);
		reversal.setPosted(false);
		reversal.setProcessed(false);
		reversal.addDescription("{->" + getDocumentNo() + ")");
		if (!reversal.save(get_Trx()))
		{
			m_processMsg = "Could not create Inventory Reversal";
			return false;
		}

		//	Reverse Line Qty
		MInventoryLine[] oLines = getLines(true);
		for (MInventoryLine oLine : oLines) {
			MInventoryLine rLine = new MInventoryLine(getCtx(), 0, get_Trx());
			copyValues(oLine, rLine, oLine.getAD_Client_ID(), oLine.getAD_Org_ID());
			rLine.setM_Inventory_ID(reversal.getM_Inventory_ID());
			rLine.setParent(reversal);
			//
			rLine.setQtyBook (oLine.getQtyCount());		//	switch
			rLine.setQtyCount (oLine.getQtyBook());
			rLine.setQtyInternalUse (oLine.getQtyInternalUse().negate());
			if (!rLine.save(get_Trx()))
			{
				m_processMsg = "Could not create Inventory Reversal Line";
				return false;
			}
		}
		//
		if (!DocumentEngine.processIt(reversal, DocActionConstants.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return false;
		}
		DocumentEngine.processIt(reversal, DocActionConstants.ACTION_Close);
		reversal.setProcessing(false);
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.save(get_Trx());
		m_processMsg = reversal.getDocumentNo();

		//	Update Reversed (this)
		MCycleCountLock.unLock(this,null);
		for (MInventoryLine oLine : oLines) {
			if(oLine.getM_ABCAnalysisGroup_ID()!=0)
			{
				MABCProductAssignment p = MABCProductAssignment.getForProduct(getCtx(), oLine.getM_Product_ID(),
                        oLine.getM_ABCAnalysisGroup_ID(),get_Trx());
				p.setProcessing(false);
				p.setIsCounted(false);
				p.setDateNextCount(null);
				if(!p.save(get_Trx()))
					return false;
			}

		}
		addDescription("(" + reversal.getDocumentNo() + "<-)");
		setProcessed(true);
		setDocStatus(DOCSTATUS_Reversed);	//	may come from void
		setDocAction(DOCACTION_None);

		return true;
	}	//	reverseCorrectionIt

	/**
	 * 	Reverse Accrual
	 * 	@return false
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		return false;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate
	 * 	@return false
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		return false;
	}	//	reActivateIt


	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"ApprovalAmt")).append("=").append(getApprovalAmt())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if ((getDescription() != null) && (getDescription().length() > 0))
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg

	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getUpdatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
	//	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
	//	return pl.getC_Currency_ID();
		return 0;
	}	//	getC_Currency_ID

	@Override
	public void setProcessMsg(String processMsg) {
		m_processMsg = processMsg;
	}

	@Override
	public String getDocBaseType() {
		return MDocBaseType.DOCBASETYPE_MaterialPhysicalInventory;
	}

	@Override
	public Timestamp getDocumentDate() {
		return getMovementDate();
	}

	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return new QueryParams("SELECT DISTINCT AD_Org_ID FROM M_InventoryLine WHERE M_Inventory_ID = ?",
				new Object[] { getM_Inventory_ID() });
	}
	
}	//	MInventory
