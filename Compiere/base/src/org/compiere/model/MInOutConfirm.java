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

import org.compiere.common.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;

/**
 *	Shipment Confirmation Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInOutConfirm.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MInOutConfirm extends X_M_InOutConfirm implements DocAction
{
    /** Logger for class MInOutConfirm */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInOutConfirm.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Create Confirmation or return existing one
	 *	@param ship shipment
	 *	@param confirmType confirmation type
	 *	@param checkExisting if false, new confirmation is created
	 *	@return Confirmation
	 */
	public static MInOutConfirm create (MInOut ship, String confirmType, boolean checkExisting)
	{
		boolean confirmExist = false;
		MInOutConfirm cHeader = null;
		
		if (checkExisting)
		{
			MInOutConfirm[] confirmations = ship.getConfirmations(true);
			MInOutLine[] lines = ship.getLines();
			for (MInOutLine line : lines)
			{
				confirmExist = false;
				for (MInOutConfirm confirmation:confirmations)
				{
					if (confirmType.equals(confirmation.getConfirmType()))
					{
						cHeader = confirmation;
						MInOutLineConfirm[] cLines = confirmation.getLines(true);
						for (MInOutLineConfirm confirmLine : cLines)
						{
							if (confirmLine.getM_InOutLine_ID() == line.getM_InOutLine_ID())
							{
								confirmExist = true;
								break;
							}
						}
					}	
			    }
				if (confirmExist == false)
				{
					break;
				}
			}
			
			if(confirmExist == true)
			{
				s_log.info("create - existing: " + cHeader);
				return cHeader;
			}
		}

		if (cHeader == null)
		{
			cHeader = new MInOutConfirm (ship, confirmType);
			cHeader.save(ship.get_Trx());
		}
		MInOutLine[] shipLines = ship.getLines(true);
		for (MInOutLine sLine : shipLines) 
		{
			MInOutLineConfirm cLine = MInOutLineConfirm.get(cHeader, sLine);
			if(cLine == null)
			{
				cLine = new MInOutLineConfirm (cHeader);
				cLine.setInOutLine(sLine);
				cLine.save(ship.get_Trx());
			}
		}
		s_log.info("New: " + cHeader);
		return cHeader;
	}	//	MInOutConfirm

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInOutConfirm.class);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOutConfirm_ID id
	 *	@param trx transaction
	 */
	public MInOutConfirm (Ctx ctx, int M_InOutConfirm_ID, Trx trx)
	{
		super (ctx, M_InOutConfirm_ID, trx);
		if (M_InOutConfirm_ID == 0)
		{
		//	setConfirmType (null);
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
			setIsApproved (false);
			setIsCancelled (false);
			setIsInDispute(false);
			super.setProcessed (false);
		}
	}	//	MInOutConfirm

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MInOutConfirm (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MInOutConfirm

	/**
	 * 	Parent Constructor
	 *	@param ship shipment
	 *	@param confirmType confirmation type
	 */
	public MInOutConfirm (MInOut ship, String confirmType)
	{
		this (ship.getCtx(), 0, ship.get_Trx());
		setClientOrg(ship);
		setM_InOut_ID (ship.getM_InOut_ID());
		setConfirmType (confirmType);
	}	//	MInOutConfirm

	/**	Confirm Lines					*/
	private MInOutLineConfirm[]	m_lines = null;
	/** Credit Memo to create			*/
	private MInvoice			m_creditMemo = null;
	/**	Physical Inventory to create	*/
	private MInventory			m_inventory = null;

	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return array of lines
	 */
	public MInOutLineConfirm[] getLines (boolean requery)
	{
		if ((m_lines != null) && !requery)
			return m_lines;
		String sql = "SELECT * FROM M_InOutLineConfirm "
			+ "WHERE M_InOutConfirm_ID=?";
		ArrayList<MInOutLineConfirm> list = new ArrayList<MInOutLineConfirm>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_InOutConfirm_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MInOutLineConfirm(getCtx(), rs, get_Trx()));
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
		m_lines = new MInOutLineConfirm[list.size ()];
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
	 * 	Get Name of ConfirmType
	 *	@return confirm type
	 */
	public String getConfirmTypeName ()
	{
		return MRefList.getListName (getCtx(), X_Ref_M_InOutConfirm_Type.AD_Reference_ID, getConfirmType());
	}	//	getConfirmTypeName

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInOutConfirm[");
		sb.append(get_ID()).append("-").append(getSummary())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		return Msg.getElement(getCtx(), "M_InOutConfirm_ID") + " " + getDocumentNo();
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
		/**
		MDocType dt = MDocType.get(getCtx(), getC_DocTypeTarget_ID());

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
		**/

		MInOutLineConfirm[] lines = getLines(true);
		//	Set dispute if not fully confirmed
		boolean difference = false;
		for (int i = 0; i < lines.length; i++)
		{
			if (!lines[i].isFullyConfirmed())
			{
				difference = true;
				break;
			}
		}
		setIsInDispute(difference);

		//
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
		MInOut inout = new MInOut (getCtx(), getM_InOut_ID(), get_Trx());
		MInOutLineConfirm[] lines = getLines(true);

		//	Check if we need to split Shipment
		if (isInDispute())
		{
			MDocType dt = MDocType.get(getCtx(), inout.getC_DocType_ID());
			if (dt.isSplitWhenDifference())
			{
				if (dt.getC_DocTypeDifference_ID() == 0)
				{
					m_processMsg = "No Split Document Type defined for: " + dt.getName();
					return DocActionConstants.STATUS_Invalid;
				}
				splitInOut (inout, dt.getC_DocTypeDifference_ID(), lines);
				m_lines = null;
			}
		}

		//	All lines
		for (MInOutLineConfirm confirmLine : lines) {
			confirmLine.set_Trx(get_Trx());
			if (!confirmLine.processLine (inout.isSOTrx(), getConfirmType()))
			{
				m_processMsg = "ShipLine not saved - " + confirmLine;
				return DocActionConstants.STATUS_Invalid;
			}
			if (confirmLine.isFullyConfirmed())
			{
				confirmLine.setProcessed(true);
				confirmLine.save(get_Trx());
			}
			else
			{
				if (createDifferenceDoc (inout, confirmLine))
				{
					confirmLine.setProcessed(true);
					confirmLine.save(get_Trx());
				}
				else
				{
					log.log(Level.SEVERE, "Scrapped=" + confirmLine.getScrappedQty()
						+ " - Difference=" + confirmLine.getDifferenceQty());
					return DocActionConstants.STATUS_Invalid;
				}
			}
		}	//	for all lines
		
	// if the shipment/receipt document requires mandatory confirmation documents, 
	//	check that the confirmation has been created for all the lines of the base document
		
		MDocType dt = MDocType.get(getCtx(), inout.getC_DocType_ID());
		boolean pick = dt.isPickQAConfirm();
		boolean ship = dt.isShipConfirm();

		if((pick && this.getConfirmType().equals(X_M_InOutConfirm.CONFIRMTYPE_PickQAConfirm)) 
				|| (ship && this.getConfirmType().equals(X_M_InOutConfirm.CONFIRMTYPE_ShipReceiptConfirm)))
		{
			MInOutLine[] sLines = inout.getLines();
			for (MInOutLine line : sLines)
			{
				if (MInOutLineConfirm.get(this, line)== null)
				{
					m_processMsg = "Confirmations for all the lines of Base document are not cretaed";
					log.saveError("Confirmation not complete","Confirmations for all the lines of Base document are not created");
					return DocActionConstants.STATUS_Invalid;
				}
			}
		}

		if (m_creditMemo != null)
			m_processMsg += " @C_Invoice_ID@=" + m_creditMemo.getDocumentNo();
		if (m_inventory != null)
			m_processMsg += " @M_Inventory_ID@=" + m_inventory.getDocumentNo();


		//	Try to complete Shipment
	//	if (DocumentEngine.processIt(inout, DocAction.ACTION_Complete))
	//		m_processMsg = "@M_InOut_ID@ " + inout.getDocumentNo() + ": @Completed@";

		return DocActionConstants.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Split Shipment into confirmed and dispute
	 *	@param original original shipment
	 *	@param C_DocType_ID target DocType
	 *	@param confirmLines confirm lines
	 */
	private void splitInOut (MInOut original, int C_DocType_ID, MInOutLineConfirm[] confirmLines)
	{
		boolean differenceQtyExists = false;
		for (MInOutLineConfirm confirmLine : confirmLines) {
			if(confirmLine.getDifferenceQty().compareTo(Env.ZERO) != 0) {
				differenceQtyExists = true;
				break;
			}
		}
		
		if(!differenceQtyExists)
			return;
		
		MInOut split = new MInOut (original, C_DocType_ID, original.getMovementDate());
		split.addDescription("Splitted from " + original.getDocumentNo());
		split.setIsInDispute(true);

		//nnayak : Change for bug 1431337
		split.setRef_InOut_ID(original.get_ID());

		if (!split.save(get_Trx()))
			throw new CompiereStateException("Cannot save Split");
		original.addDescription("Split: " + split.getDocumentNo());
		if (!original.save(get_Trx()))
			throw new CompiereStateException("Cannot update original Shipment");

		//	Go through confirmations
		for (MInOutLineConfirm confirmLine : confirmLines) {
			BigDecimal differenceQty = confirmLine.getDifferenceQty();
			if (differenceQty.compareTo(Env.ZERO) == 0)
				continue;
			//
			MInOutLine oldLine = confirmLine.getLine();
			log.fine("Qty=" + differenceQty + ", Old=" + oldLine);
			//
			MInOutLine splitLine = new MInOutLine (split);
			splitLine.setC_OrderLine_ID(oldLine.getC_OrderLine_ID());
			splitLine.setC_UOM_ID(oldLine.getC_UOM_ID());
			splitLine.setDescription(oldLine.getDescription());
			splitLine.setIsDescription(oldLine.isDescription());
			splitLine.setLine(oldLine.getLine());
			splitLine.setM_AttributeSetInstance_ID(oldLine.getM_AttributeSetInstance_ID());
			splitLine.setM_Locator_ID(oldLine.getM_Locator_ID());
			splitLine.setM_Product_ID(oldLine.getM_Product_ID());
			splitLine.setM_Warehouse_ID(oldLine.getM_Warehouse_ID());
			splitLine.setRef_InOutLine_ID(oldLine.getRef_InOutLine_ID());
			splitLine.addDescription("Split: from " + oldLine.getMovementQty());
			//	Qtys
			splitLine.setQty(differenceQty);		//	Entered/Movement
			if (!splitLine.save(get_Trx()))
				throw new CompiereStateException("Cannot save Split Line");
			//	Old
			oldLine.addDescription("Splitted: from " + oldLine.getMovementQty());
			oldLine.setQty(oldLine.getMovementQty().subtract(differenceQty));
			if (!oldLine.save(get_Trx()))
				throw new CompiereStateException("Cannot save Splited Line");
			//	Update Confirmation Line
			confirmLine.setTargetQty(confirmLine.getTargetQty().subtract(differenceQty));
			confirmLine.setDifferenceQty(Env.ZERO);
			if (!confirmLine.save(get_Trx()))
				throw new CompiereStateException("Cannot save Split Confirmation");
		}	//	for all confirmations

		m_processMsg = "Split @M_InOut_ID@=" + split.getDocumentNo()
			+ " - @M_InOutConfirm_ID@=";

		//	Create Dispute Confirmation
		DocumentEngine.processIt(split, DocActionConstants.ACTION_Prepare);
	//	split.createConfirmation();
		split.save(get_Trx());
		MInOutConfirm[] splitConfirms = split.getConfirmations(true);
		if (splitConfirms.length > 0)
		{
			int index = 0;
			if (splitConfirms[index].isProcessed())
			{
				if (splitConfirms.length > 1)
					index++;	//	try just next
				if (splitConfirms[index].isProcessed())
				{
					m_processMsg += splitConfirms[index].getDocumentNo() + " processed??";
					return;
				}
			}
			splitConfirms[index].setIsInDispute(true);
			splitConfirms[index].save(get_Trx());
			m_processMsg += splitConfirms[index].getDocumentNo();
			//	Set Lines to unconfirmed
			MInOutLineConfirm[] splitConfirmLines = splitConfirms[index].getLines(false);
			for (MInOutLineConfirm splitConfirmLine : splitConfirmLines) {
				splitConfirmLine.setScrappedQty(Env.ZERO);
				splitConfirmLine.setConfirmedQty(Env.ZERO);
				splitConfirmLine.save(get_Trx());
			}
		}
		else
			m_processMsg += "??";

	}	//	splitInOut


	/**
	 * 	Create Difference Document
	 * 	@param inout shipment/receipt
	 *	@param confirm confirm line
	 *	@return true if created
	 */
	private boolean createDifferenceDoc (MInOut inout, MInOutLineConfirm confirm)
	{
		if (m_processMsg == null)
			m_processMsg = "";
		else if (m_processMsg.length() > 0)
			m_processMsg += "; ";
		//	Credit Memo if linked Document
		if ((confirm.getDifferenceQty().signum() != 0)
			&& !inout.isSOTrx() && !inout.isReturnTrx() && (inout.getRef_InOut_ID() != 0))
		{
			log.info("Difference=" + confirm.getDifferenceQty());
			if (m_creditMemo == null)
			{
				m_creditMemo = new MInvoice (inout, null);
				m_creditMemo.setDescription(Msg.translate(getCtx(), "M_InOutConfirm_ID") + " " + getDocumentNo());
				m_creditMemo.setC_DocTypeTarget_ID(MDocBaseType.DOCBASETYPE_APCreditMemo);
				if (!m_creditMemo.save(get_Trx()))
				{
					m_processMsg += "Credit Memo not created";
					return false;
				}
				setC_Invoice_ID(m_creditMemo.getC_Invoice_ID());
			}
			MInvoiceLine line = new MInvoiceLine (m_creditMemo);
			line.setShipLine(confirm.getLine());
			line.setQty(confirm.getDifferenceQty());	//	Entered/Invoiced
			if (!line.save(get_Trx()))
			{
				m_processMsg += "Credit Memo Line not created";
				return false;
			}
			confirm.setC_InvoiceLine_ID(line.getC_InvoiceLine_ID());
		}

		//	Create Inventory Difference
		if (confirm.getScrappedQty().signum() != 0)
		{
			log.info("Scrapped=" + confirm.getScrappedQty());
			if (m_inventory == null)
			{
				MWarehouse wh = MWarehouse.get(getCtx(), inout.getM_Warehouse_ID());
				m_inventory = new MInventory (wh);
				m_inventory.setDescription(Msg.translate(getCtx(), "M_InOutConfirm_ID") + " " + getDocumentNo());
				if (!m_inventory.save(get_Trx()))
				{
					m_processMsg += "Inventory not created";
					return false;
				}
				setM_Inventory_ID(m_inventory.getM_Inventory_ID());
			}
			MInOutLine ioLine = confirm.getLine();
			MInventoryLine line = new MInventoryLine (m_inventory,
				ioLine.getM_Locator_ID(), ioLine.getM_Product_ID(), ioLine.getM_AttributeSetInstance_ID(),
				confirm.getScrappedQty(), Env.ZERO);
			if (!line.save(get_Trx()))
			{
				m_processMsg += "Inventory Line not created";
				return false;
			}
			confirm.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
		}

		//
		if (!confirm.save(get_Trx()))
		{
			m_processMsg += "Confirmation Line not saved";
			return false;
		}
		return true;
	}	//	createDifferenceDoc

	/**
	 * 	Void Document.
	 * 	@return false
	 */
	public boolean voidIt()
	{
		log.info(toString());
		return false;
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
		return false;
	}	//	reverseCorrectionIt

	/**
	 * 	Reverse Accrual - none
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
		return null;
	}

	@Override
	public Timestamp getDocumentDate() {
		return null;
	}

	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return new QueryParams("SELECT DISTINCT AD_Org_ID FROM M_InOutLineConfirm WHERE M_InOutConfirm_ID = ?",
				new Object[] { getM_InOutConfirm_ID() });
	}
	
}	//	MInOutConfirm
