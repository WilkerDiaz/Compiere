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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.common.constants.EnvConstants;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Trx;


/**
 *	Shipment/Receipt Callouts
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutInOut.java,v 1.7 2006/07/30 00:51:05 jjanke Exp $
 */
public class CalloutInOut extends CalloutEngine
{
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());

	/**
	 * 	C_Order - Order Defaults.
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String order (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_Order_ID = (Integer)value;
		if (C_Order_ID == null || C_Order_ID.intValue() == 0)
			return "";
		//	No Callout Active to fire dependent values
		if (isCalloutActive())	//	prevent recursive
			return "";


		MOrder order = new MOrder (ctx, C_Order_ID.intValue(), null);

		if (order.get_ID() != 0)
		{
			mTab.setValue("DateOrdered", order.getDateOrdered());
			mTab.setValue("POReference", order.getPOReference());
			mTab.setValue("Description", order.getDescription());
			//
			mTab.setValue("M_Warehouse_ID", Integer.valueOf(order.getM_Warehouse_ID()));
			mTab.setValue("DeliveryRule", order.getDeliveryRule());
			mTab.setValue("DeliveryViaRule", order.getDeliveryViaRule());
			mTab.setValue("M_Shipper_ID", Integer.valueOf(order.getM_Shipper_ID()));
			mTab.setValue("FreightCostRule", order.getFreightCostRule());
			mTab.setValue("FreightAmt", order.getFreightAmt());

			mTab.setValue("C_BPartner_ID", Integer.valueOf(order.getC_BPartner_ID()));
			//sraval: source forge bug # 1503219 - added to default ship to location
			mTab.setValue("C_BPartner_Location_ID", Integer.valueOf(order.getC_BPartner_Location_ID()));

			mTab.setValue("AD_OrgTrx_ID", Integer.valueOf(order.getAD_OrgTrx_ID()));
			mTab.setValue("C_Activity_ID", Integer.valueOf(order.getC_Activity_ID()));
			mTab.setValue("C_Campaign_ID", Integer.valueOf(order.getC_Campaign_ID()));
			mTab.setValue("C_Project_ID", Integer.valueOf(order.getC_Project_ID()));
			mTab.setValue("User1_ID", Integer.valueOf(order.getUser1_ID()));
			mTab.setValue("User2_ID", Integer.valueOf(order.getUser2_ID()));

			boolean isReturnTrx = (Boolean)mTab.getValue("IsReturnTrx");
			if(isReturnTrx)
			{
				mTab.setValue("Orig_Order_ID", order.getOrig_Order_ID());
				mTab.setValue("Orig_InOut_ID", order.getOrig_InOut_ID());
			}
		}


		return "";
	}	//	order

	/**
	 *	InOut - DocType.
	 *			- sets MovementType
	 *			- gets DocNo
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String docType (Ctx ctx, int WindowNo, GridTab mTab, GridField mField,
			Object value, Object oldValue)
	{
		Integer C_DocType_ID = (Integer)value;
		if (C_DocType_ID == null || C_DocType_ID.intValue() == 0)
			return "";

		//	Re-Create new DocNo, if there is a doc number already
		//	and the existing source used a different Sequence number
		String oldDocNo = (String)mTab.getValue("DocumentNo");
		boolean newDocNo = oldDocNo == null;
		if (!newDocNo && oldDocNo.startsWith("<") && oldDocNo.endsWith(">"))
			newDocNo = true;
		Integer oldC_DocType_ID = (Integer)mTab.getValue("C_DocType_ID");
		if ((oldC_DocType_ID == null || oldC_DocType_ID.intValue() == 0)
				&& oldValue != null)	//	get old Target
			oldC_DocType_ID = (Integer)oldValue;

		String sql = "SELECT d.DocBaseType, d.IsDocNoControlled,"
			+ " s.CurrentNext, d.IsReturnTrx, s.CurrentNextSys, s.AD_Sequence_ID "
			+ "FROM C_DocType d"
			+ " LEFT OUTER JOIN AD_Sequence s ON (d.DocNoSequence_ID=s.AD_Sequence_ID)"
			+ "WHERE C_DocType_ID=?";		//	1
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int AD_Sequence_ID = 0;
			//	Get old AD_SeqNo for comparison
			if (oldC_DocType_ID.intValue() != 0)
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(1, oldC_DocType_ID.intValue());
				rs = pstmt.executeQuery();
				if (rs.next())
					AD_Sequence_ID = rs.getInt(6);
				rs.close();
				pstmt.close();
			}

			ctx.setContext( WindowNo, "C_DocTypeTarget_ID", C_DocType_ID.intValue());
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_DocType_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Set Movement Type
				String DocBaseType = rs.getString("DocBaseType");
				Boolean isReturnTrx = rs.getString("IsReturnTrx").equals("Y");
				if (DocBaseType.equals("MMS") && !isReturnTrx)					//	Material Shipments
					mTab.setValue("MovementType", "C-");				//	Customer Shipments
				else if (DocBaseType.equals("MMS") && isReturnTrx)				//	Material Shipments
					mTab.setValue("MovementType", "C+");				//	Customer Returns
				else if (DocBaseType.equals("MMR") && !isReturnTrx)				//	Material Receipts
					mTab.setValue("MovementType", "V+");				//	Vendor Receipts
				else if (DocBaseType.equals("MMR") && isReturnTrx)				//	Material Receipts
				mTab.setValue("MovementType", "V-");					//	Return to Vendor

				//	DocumentNo
				if (rs.getString(2).equals("Y"))			//	IsDocNoControlled
				{
					if (AD_Sequence_ID != rs.getInt(6))
						newDocNo = true;
					if (newDocNo)
						if (Ini.isPropertyBool(Ini.P_COMPIERESYS) && Env.getCtx().getAD_Client_ID() < 1000000)
							mTab.setValue("DocumentNo", "<" + rs.getString(5) + ">");
						else
							mTab.setValue("DocumentNo", "<" + rs.getString(3) + ">");
				}

			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "";
	}	//	docType


	/**
	 *	M_InOut - Defaults for BPartner.
	 *			- Location
	 *			- Contact
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String bpartner (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_BPartner_ID = (Integer)value;
		if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
			return "";

		boolean isReturnTrx = (Boolean)mTab.getValue("IsReturnTrx");

		//	sraval: source forge bug # 1503219
		Integer order = (Integer)mTab.getValue("C_Order_ID");

		String sql = "SELECT p.AD_Language, p.POReference,"
			+ "SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ "l.C_BPartner_Location_ID, c.AD_User_ID "
			+ "FROM C_BPartner p"
			+ " LEFT OUTER JOIN C_BPartner_Location l ON (p.C_BPartner_ID=l.C_BPartner_ID AND l.IsActive='Y')"
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID AND c.IsActive='Y') "
			+ "WHERE p.C_BPartner_ID=?"		//	1
			+ " AND COALESCE(l.IsShipTo,'Y') = 'Y' "
			+ " ORDER BY l.Name ASC ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID.intValue());
			rs = pstmt.executeQuery();

			if (rs.next())
			{
				//	Location
				Integer ii = Integer.valueOf(rs.getInt("C_BPartner_Location_ID"));
				// sraval: source forge bug # 1503219 - default location for material receipt
				if (order == null){
					if (rs.wasNull())
						mTab.setValue("C_BPartner_Location_ID", null);
					else
						mTab.setValue("C_BPartner_Location_ID", ii);
				}
				//	Contact
				ii = Integer.valueOf(rs.getInt("AD_User_ID"));
				if (rs.wasNull())
					mTab.setValue("AD_User_ID", null);
				else
					mTab.setValue("AD_User_ID", ii);

				// Skip credit check for returns
				if(!isReturnTrx)
				{
					//	CreditAvailable
					double CreditAvailable = rs.getDouble("CreditAvailable");
					if (!rs.wasNull() && CreditAvailable < 0)
						mTab.fireDataStatusEEvent("CreditLimitOver",
								DisplayType.getNumberFormat(DisplayTypeConstants.Amount).format(CreditAvailable),
								false);
				}
				//
				mTab.setValue("C_Project_ID", null);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "";
	}	//	bpartner

	/**
	 *	M_Warehouse.
	 *		Set Organization and Default Locator
	 *	@param ctx
	 *	@param WindowNo
	 *	@param mTab
	 *	@param mField
	 *	@param value
	 *	@return error message or ""
	 */
	public String warehouse (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		Integer M_Warehouse_ID = (Integer)value;
		if (M_Warehouse_ID == null || M_Warehouse_ID.intValue() == 0)
			return "";
		setCalloutActive(true);


		String sql = "SELECT w.AD_Org_ID, l.M_Locator_ID, w.IsDisallowNegativeInv, "
			+ "w.IsWMSEnabled, w.M_RCVLocator_ID "
			+ "FROM M_Warehouse w"
			+ " LEFT OUTER JOIN M_Locator l ON (l.M_Warehouse_ID=w.M_Warehouse_ID AND l.IsDefault='Y') "
			+ "WHERE w.M_Warehouse_ID=?";		//	1

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, M_Warehouse_ID.intValue());
			rs = pstmt.executeQuery();

			if (rs.next())
			{
				//	Org
				Integer ii = Integer.valueOf(rs.getInt(1));
				int AD_Org_ID = ctx.getContextAsInt( WindowNo, "AD_Org_ID");
				if (AD_Org_ID != ii.intValue())
					mTab.setValue("AD_Org_ID", ii);
				
				//	Locator
				Boolean isWMSEnabled = rs.getString(4).equals("Y");
				int M_Locator_ID = 0;
				if (isWMSEnabled)
					M_Locator_ID = rs.getInt(5);

				if(M_Locator_ID == 0 )
					M_Locator_ID = rs.getInt(2);
				
				if (M_Locator_ID == 0)
					ctx.setContext( WindowNo, 0, "M_Locator_ID", null);
				else
				{
					log.config("M_Locator_ID=" + M_Locator_ID);
					ctx.setContext( WindowNo, "M_Locator_ID", M_Locator_ID);
				}

				Boolean disallowNegInv = rs.getString(3).equals("Y");
				String DeliveryRule = mTab.get_ValueAsString("DeliveryRule");
				if(disallowNegInv && DeliveryRule.equals(X_C_Order.DELIVERYRULE_Force) ||
						DeliveryRule == null || DeliveryRule.length()==0)
					mTab.setValue("DeliveryRule",X_C_Order.DELIVERYRULE_Availability);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			setCalloutActive(false);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		setCalloutActive(false);
		return "";
	}	//	warehouse


	/**************************************************************************
	 * 	OrderLine Callout
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab model
	 *	@param mField field model
	 *	@param value new value
	 *	@return error message or ""
	 */
	public String orderLine (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_OrderLine_ID = (Integer)value;
		if (C_OrderLine_ID == null || C_OrderLine_ID.intValue() == 0)
			return "";
		setCalloutActive(true);

		//	Get Details
		MOrderLine ol = new MOrderLine (ctx, C_OrderLine_ID.intValue(), null);

		//	Get Details
		if (ol.get_ID() != 0)
		{
			mTab.setValue("Description", ol.getDescription());
			
			//*** Modified for SR# 10020653
			if (Integer.valueOf(ol.getM_Product_ID())<=0){
				mTab.setValue("M_Product_ID", null);
			    mTab.setValue("M_AttributeSetInstance_ID", null);
			}

			else{
				mTab.setValue("M_Product_ID", Integer.valueOf(ol.getM_Product_ID()));
				mTab.setValue("M_AttributeSetInstance_ID", Integer.valueOf(ol.getM_AttributeSetInstance_ID()));
			}
			
			if (Integer.valueOf(ol.getC_Charge_ID())>0)
				mTab.setValue("C_Charge_ID", Integer.valueOf(ol.getC_Charge_ID()));
			else
				mTab.setValue("C_Charge_ID", null);
				
			//***
			
			mTab.setValue("C_UOM_ID", Integer.valueOf(ol.getC_UOM_ID()));
			BigDecimal MovementQty = ol.getQtyOrdered().subtract(ol.getQtyDelivered());
			mTab.setValue("MovementQty", MovementQty);
			BigDecimal QtyEntered = MovementQty;
			if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0)
				QtyEntered = QtyEntered.multiply(ol.getQtyEntered())
					.divide(ol.getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP);
			mTab.setValue("QtyEntered", QtyEntered);
		
			mTab.setValue("C_Activity_ID", Integer.valueOf(ol.getC_Activity_ID()));
			mTab.setValue("C_Campaign_ID", Integer.valueOf(ol.getC_Campaign_ID()));
			mTab.setValue("C_Project_ID", Integer.valueOf(ol.getC_Project_ID()));
			mTab.setValue("C_ProjectPhase_ID", Integer.valueOf(ol.getC_ProjectPhase_ID()));
			mTab.setValue("C_ProjectTask_ID", Integer.valueOf(ol.getC_ProjectTask_ID()));
			mTab.setValue("AD_OrgTrx_ID", Integer.valueOf(ol.getAD_OrgTrx_ID()));
			mTab.setValue("User1_ID", Integer.valueOf(ol.getUser1_ID()));
			mTab.setValue("User2_ID", Integer.valueOf(ol.getUser2_ID()));

			if(ol.getParent().isReturnTrx())
			{
				mTab.setValue("Orig_OrderLine_ID", Integer.valueOf(ol.getOrig_OrderLine_ID()));
				MInOutLine ioLine = new MInOutLine (ctx, ol.getOrig_InOutLine_ID(), null);
				mTab.setValue("M_Locator_ID", ioLine.getM_Locator_ID());
			}

		}
		setCalloutActive(false);
		return "";
	}	//	orderLine

	/**
	 *	M_InOutLine - Default UOM/Locator for Product.
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab model
	 *	@param mField field model
	 *	@param value new value
	 *	@return error message or ""
	 */
	public String product (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		setCalloutActive(true);

		//	Set Attribute & Locator
		int M_Locator_ID = 0;
		if (ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
			&& ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
		{
			mTab.setValue("M_AttributeSetInstance_ID",
				Integer.valueOf(ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID")));
			//	Locator from Info Window - ASI
			M_Locator_ID = ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Locator_ID");
			if (M_Locator_ID != 0)
				mTab.setValue("M_Locator_ID", Integer.valueOf(M_Locator_ID));
		}
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);
		//
		boolean IsSOTrx = ctx.isSOTrx(WindowNo);
		if (IsSOTrx)
		{
			setCalloutActive(false);
			return "";
		}

		//	PO - Set UOM/Locator/Qty
		MProduct product = MProduct.get(ctx, M_Product_ID.intValue());
		mTab.setValue("C_UOM_ID", Integer.valueOf (product.getC_UOM_ID()));
		BigDecimal QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
		mTab.setValue("MovementQty", QtyEntered);
		if (M_Locator_ID != 0)
			;		//	already set via ASI
		else
		{
			int M_Warehouse_ID = ctx.getContextAsInt( WindowNo, "M_Warehouse_ID");
			M_Locator_ID = MProductLocator.getFirstM_Locator_ID (product, M_Warehouse_ID);
			if (M_Locator_ID != 0)
				mTab.setValue("M_Locator_ID", Integer.valueOf (M_Locator_ID));
			else
			{
				MWarehouse wh = MWarehouse.get (ctx, M_Warehouse_ID);
				if(wh.isWMSEnabled())
					M_Locator_ID = wh.getM_RcvLocator_ID();
				
				if(M_Locator_ID == 0)
					M_Locator_ID = wh.getDefaultM_Locator_ID();
				
				mTab.setValue("M_Locator_ID", Integer.valueOf (M_Locator_ID));
			}
		}
		setCalloutActive(false);
		return "";
	}	//	product

	/**
	 *	InOut Line - Quantity.
	 *		- called from C_UOM_ID, QtyEntered, MovementQty
	 *		- enforces qty UOM relationship
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab model
	 *	@param mField field model
	 *	@param value new value
	 *	@return error message or ""
	 */
	public String qty (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int M_Product_ID = ctx.getContextAsInt( WindowNo, "M_Product_ID");
		//	log.log(Level.WARNING,"qty - init - M_Product_ID=" + M_Product_ID);
		BigDecimal MovementQty, QtyEntered;

		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			mTab.setValue("MovementQty", QtyEntered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
					+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			MovementQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
				C_UOM_To_ID, QtyEntered);
			if (MovementQty == null)
				MovementQty = QtyEntered;
			boolean conversion = QtyEntered.compareTo(MovementQty) != 0;
			log.fine("UOM=" + C_UOM_To_ID
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion
				+ " MovementQty=" + MovementQty);
			ctx.setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("MovementQty", MovementQty);
		}
		//	No UOM defined
		else if (ctx.getContextAsInt( WindowNo, "C_UOM_ID") == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			mTab.setValue("MovementQty", QtyEntered);
		}
		//	QtyEntered changed - calculate MovementQty
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
			QtyEntered = (BigDecimal)value;
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
					+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			MovementQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
				C_UOM_To_ID, QtyEntered);
			if (MovementQty == null)
				MovementQty = QtyEntered;
			boolean conversion = QtyEntered.compareTo(MovementQty) != 0;
			log.fine("UOM=" + C_UOM_To_ID
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion
				+ " MovementQty=" + MovementQty);
			ctx.setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("MovementQty", MovementQty);
		}
		//	MovementQty changed - calculate QtyEntered (should not happen)
		else if (mField.getColumnName().equals("MovementQty"))
		{
			int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
			MovementQty = (BigDecimal)value;
			int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
			BigDecimal MovementQty1 = MovementQty.setScale(precision, BigDecimal.ROUND_HALF_UP);
			if (MovementQty.compareTo(MovementQty1) != 0)
			{
				log.fine("Corrected MovementQty "
					+ MovementQty + "->" + MovementQty1);
				MovementQty = MovementQty1;
				mTab.setValue("MovementQty", MovementQty);
			}
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID,
				C_UOM_To_ID, MovementQty);
			if (QtyEntered == null)
				QtyEntered = MovementQty;
			boolean conversion = MovementQty.compareTo(QtyEntered) != 0;
			log.fine("UOM=" + C_UOM_To_ID
				+ ", MovementQty=" + MovementQty
				+ " -> " + conversion
				+ " QtyEntered=" + QtyEntered);
			ctx.setContext( WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}

		// Check for RMA
		boolean isReturnTrx = "Y".equals(ctx.getContext(WindowNo, "IsReturnTrx"));
		if(M_Product_ID != 0
			&& isReturnTrx)
		{
			Integer C_OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
			Integer M_InOut_ID = (Integer)mTab.getValue("M_InOut_ID");
			Integer M_InOutLine_ID = (Integer)mTab.getValue("M_InOutLine_ID");
			if (M_InOutLine_ID == null)
				M_InOutLine_ID = Integer.valueOf(0);
			
			if(C_OrderLine_ID > 0)
			{
				BigDecimal openQty = MInOutLine.getInOutOpenQty(ctx, M_InOut_ID, M_InOutLine_ID, C_OrderLine_ID);
				MovementQty = (BigDecimal)mTab.getValue("MovementQty");
				if(openQty.compareTo(MovementQty)<0)
				{
					if(ctx.isSOTrx(WindowNo))
						mTab.fireDataStatusEEvent ("QtyShippedLessThanQtyReturned", openQty.toString(), false);
					else
						mTab.fireDataStatusEEvent ("QtyReceivedLessThanQtyReturned", openQty.toString(), false);
					mTab.setValue ("MovementQty",openQty);
					MovementQty = openQty;
	
					int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
					QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID,
							C_UOM_To_ID, MovementQty);
					if (QtyEntered == null)
						QtyEntered = MovementQty;
					mTab.setValue ("QtyEntered",QtyEntered);
					mTab.setValue ("MovementQty",MovementQty);
					log.fine("QtyEntered : "+ QtyEntered.toString() +
								"MovementQty : " + MovementQty.toString());
				}
			}
		}

		//
		setCalloutActive(false);
		return "";
	}	//	qty

	/**
	 *	M_InOutLine - ASI.
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab model
	 *	@param mField field model
	 *	@param value new value
	 *	@return error message or ""
	 */
	public String asi (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		Integer M_ASI_ID = (Integer)value;
		if (M_ASI_ID == null || M_ASI_ID.intValue() == 0)
			return "";
		setCalloutActive(true);
		//
		int M_Product_ID = ctx.getContextAsInt( WindowNo, "M_Product_ID");
		int M_Warehouse_ID = ctx.getContextAsInt( WindowNo, "M_Warehouse_ID");
		int M_Locator_ID = ctx.getContextAsInt( WindowNo, "M_Locator_ID");
		log.fine("M_Product_ID=" + M_Product_ID
			+ ", M_ASI_ID=" + M_ASI_ID
			+ " - M_Warehouse_ID=" + M_Warehouse_ID
			+ ", M_Locator_ID=" + M_Locator_ID);
		//	Check Selection
		int M_AttributeSetInstance_ID =	Env.getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID");
		if (M_ASI_ID.intValue() == M_AttributeSetInstance_ID)
		{
			int selectedM_Locator_ID = Env.getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Locator_ID");
			if (selectedM_Locator_ID != 0)
			{
				log.fine("Selected M_Locator_ID=" + selectedM_Locator_ID);
				mTab.setValue("M_Locator_ID", Integer.valueOf (selectedM_Locator_ID));
			}
		}
		setCalloutActive(false);
		return "";
	}	//	asi

}	//	CalloutInOut
