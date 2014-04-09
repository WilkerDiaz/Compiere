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
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;


/**
 *	Inventory Movement Confirmation
 *
 *  @author Jorg Janke
 *  @version $Id: MMovementConfirm.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MMovementConfirm extends X_M_MovementConfirm implements DocAction
{
    /** Logger for class MMovementConfirm */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMovementConfirm.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Create Confirmation or return existing one
	 *	@param move movement
	 *	@param checkExisting if false, new confirmation is created
	 *	@return Confirmation
	 */
	public static MMovementConfirm create (MMovement move, boolean checkExisting)
	{
		if (checkExisting)
		{
			MMovementConfirm[] confirmations = move.getConfirmations(false);
			for (MMovementConfirm confirm : confirmations) {
				return confirm;
			}
		}

		MMovementConfirm confirm = new MMovementConfirm (move);
		confirm.save(move.get_Trx());
		MMovementLine[] moveLines = move.getLines(false);
		for (MMovementLine mLine : moveLines) {
			MMovementLineConfirm cLine = new MMovementLineConfirm (confirm);
			cLine.setMovementLine(mLine);
			cLine.save(move.get_Trx());
		}
		return confirm;
	}	//	MInOutConfirm


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_MovementConfirm_ID id
	 *	@param trx transaction
	 */
	public MMovementConfirm (Ctx ctx, int M_MovementConfirm_ID, Trx trx)
	{
		super (ctx, M_MovementConfirm_ID, trx);
		if (M_MovementConfirm_ID == 0)
		{
		//	setM_Movement_ID (0);
			setDocAction (DOCACTION_Complete);
			setDocStatus (DOCSTATUS_Drafted);
			setIsApproved (false);	// N
			setProcessed (false);
		}
	}	//	MMovementConfirm

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MMovementConfirm (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MMovementConfirm

	/**
	 * 	Parent Constructor
	 *	@param move movement
	 */
	public MMovementConfirm (MMovement move)
	{
		this (move.getCtx(), 0, move.get_Trx());
		setClientOrg(move);
		setM_Movement_ID(move.getM_Movement_ID());
	}	//	MInOutConfirm

	/**	Confirm Lines					*/
	private MMovementLineConfirm[]	m_lines = null;

	/**	Physical Inventory From	*/
	private MInventory				m_inventoryFrom = null;
	/**	Physical Inventory To	*/
	private MInventory				m_inventoryTo = null;
	/**	Physical Inventory Info	*/
	private String					m_inventoryInfo = null;

	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return array of lines
	 */
	public MMovementLineConfirm[] getLines (boolean requery)
	{
		if ((m_lines != null) && !requery)
			return m_lines;
		String sql = "SELECT * FROM M_MovementLineConfirm "
			+ "WHERE M_MovementConfirm_ID=?";
		ArrayList<MMovementLineConfirm> list = new ArrayList<MMovementLineConfirm>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_MovementConfirm_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MMovementLineConfirm(getCtx(), rs, get_Trx()));
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
		m_lines = new MMovementLineConfirm[list.size ()];
		list.toArray (m_lines);
		return m_lines;
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
	 * 	Set Approved
	 *	@param IsApproved approval
	 */
	@Override
	public void setIsApproved (boolean IsApproved)
	{
		if (IsApproved && !isApproved())
		{
			int AD_User_ID = getCtx().getAD_User_ID();
			MUser user = MUser.get(getCtx(), AD_User_ID);
			String info = user.getName()
				+ ": "
				+ Msg.translate(getCtx(), "IsApproved")
				+ " - " + new Timestamp(System.currentTimeMillis());
			addDescription(info);
		}
		super.setIsApproved (IsApproved);
	}	//	setIsApproved


	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		return Msg.getElement(getCtx(), "M_MovementConfirm_ID") + " " + getDocumentNo();
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


	/**	Process Message 			*/
	private String		m_processMsg = null;

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
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
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt

	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		MMovement move = new MMovement (getCtx(), getM_Movement_ID(), get_Trx());
		MMovementLineConfirm[] lines = getLines(true);
		for (MMovementLineConfirm confirm : lines)
		{
			confirm.set_Trx(get_Trx());
			if (!confirm.processLine ())
			{
				m_processMsg = "ShipLine not saved - " + confirm;
				return DocActionConstants.STATUS_Invalid;
			}
			if (confirm.isFullyConfirmed())
			{
				confirm.setProcessed(true);
				confirm.save(get_Trx());
			}
			else
			{
				if (createDifferenceDoc (move, confirm))
				{
					confirm.setProcessed(true);
					confirm.save(get_Trx());
				}
				else
				{
					log.log(Level.SEVERE, "completeIt - Scrapped=" + confirm.getScrappedQty()
						+ " - Difference=" + confirm.getDifferenceQty());

					m_processMsg = "Differnce Doc not created";
					return DocActionConstants.STATUS_Invalid;
				}
			}
		}	//	for all lines

		if (m_inventoryInfo != null)
		{
			m_processMsg = " @M_Inventory_ID@: " + m_inventoryInfo;
			addDescription(Msg.translate(getCtx(), "M_Inventory_ID")
				+ ": " + m_inventoryInfo);
		}

		return DocActionConstants.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Create Difference Document.
	 * 	Creates one or two inventory lines
	 * 	@param move movement
	 *	@param confirm confirm line
	 *	@return true if created
	 */
	private boolean createDifferenceDoc (MMovement move, MMovementLineConfirm confirm)
	{
		MMovementLine mLine = confirm.getLine();

		//	Difference - Create Inventory Difference for Source Location
		if (Env.ZERO.compareTo(confirm.getDifferenceQty()) != 0)
		{
			//	Get Warehouse for Source
			MLocator loc = MLocator.get(getCtx(), mLine.getM_Locator_ID());
			if ((m_inventoryFrom != null)
				&& (m_inventoryFrom.getM_Warehouse_ID() != loc.getM_Warehouse_ID()))
				m_inventoryFrom = null;

			if (m_inventoryFrom == null)
			{
				MWarehouse wh = MWarehouse.get(getCtx(), loc.getM_Warehouse_ID());
				m_inventoryFrom = new MInventory (wh);
				m_inventoryFrom.setDescription(Msg.translate(getCtx(), "M_MovementConfirm_ID") + " " + getDocumentNo());
				if (!m_inventoryFrom.save(get_Trx()))
				{
					m_processMsg += "Inventory not created";
					return false;
				}
				//	First Inventory
				if (getM_Inventory_ID() == 0)
				{
					setM_Inventory_ID(m_inventoryFrom.getM_Inventory_ID());
					m_inventoryInfo = m_inventoryFrom.getDocumentNo();
				}
				else
					m_inventoryInfo += "," + m_inventoryFrom.getDocumentNo();
			}

			log.info("createDifferenceDoc - Difference=" + confirm.getDifferenceQty());
			MInventoryLine line = new MInventoryLine (m_inventoryFrom,
					mLine.getM_Locator_ID(), mLine.getM_Product_ID(), mLine.getM_AttributeSetInstance_ID(),
					confirm.getDifferenceQty(), Env.ZERO);
			line.setDescription(Msg.translate(getCtx(), "DifferenceQty"));
			if (!line.save(get_Trx()))
			{
				m_processMsg += "Inventory Line not created";
				return false;
			}
			confirm.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
		}	//	Difference

		//	Scrapped - Create Inventory Difference for Target Location
		if (Env.ZERO.compareTo(confirm.getScrappedQty()) != 0)
		{
			//	Get Warehouse for Target
			MLocator loc = MLocator.get(getCtx(), mLine.getM_LocatorTo_ID());
			if ((m_inventoryTo != null)
				&& (m_inventoryTo.getM_Warehouse_ID() != loc.getM_Warehouse_ID()))
				m_inventoryTo = null;

			if (m_inventoryTo == null)
			{
				MWarehouse wh = MWarehouse.get(getCtx(), loc.getM_Warehouse_ID());
				m_inventoryTo = new MInventory (wh);
				m_inventoryTo.setDescription(Msg.translate(getCtx(), "M_MovementConfirm_ID") + " " + getDocumentNo());
				if (!m_inventoryTo.save(get_Trx()))
				{
					m_processMsg += "Inventory not created";
					return false;
				}
				//	First Inventory
				if (getM_Inventory_ID() == 0)
				{
					setM_Inventory_ID(m_inventoryTo.getM_Inventory_ID());
					m_inventoryInfo = m_inventoryTo.getDocumentNo();
				}
				else
					m_inventoryInfo += "," + m_inventoryTo.getDocumentNo();
			}

			log.info("createDifferenceDoc - Scrapped=" + confirm.getScrappedQty());
			MInventoryLine line = new MInventoryLine (m_inventoryTo,
				mLine.getM_LocatorTo_ID(), mLine.getM_Product_ID(), mLine.getM_AttributeSetInstance_ID(),
				confirm.getScrappedQty(), Env.ZERO);
			line.setDescription(Msg.translate(getCtx(), "ScrappedQty"));
			if (!line.save(get_Trx()))
			{
				m_processMsg += "Inventory Line not created";
				return false;
			}
			confirm.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
		}	//	Scrapped

		return true;
	}	//	createDifferenceDoc

	/**
	 * 	Void Document.
	 * 	@return false
	 */
	public boolean voidIt()
	{
		log.info("voidIt - " + toString());
		return false;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info("closeIt - " + toString());

		//	Close Not delivered Qty
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction
	 * 	@return false
	 */
	public boolean reverseCorrectIt()
	{
		log.info("reverseCorrectIt - " + toString());
		return false;
	}	//	reverseCorrectionIt

	/**
	 * 	Reverse Accrual - none
	 * 	@return false
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		return false;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate
	 * 	@return false
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
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
		return MDocBaseType.DOCBASETYPE_MaterialMovement;
	}


	@Override
	public Timestamp getDocumentDate() {
		return getUpdated();
	}


	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return new QueryParams("SELECT DISTINCT AD_Org_ID FROM M_MovementLineConfirm WHERE M_MovementConfirm_ID = ?",
				new Object[] { getM_MovementConfirm_ID() });
	}
	
}	//	MMovementConfirm
