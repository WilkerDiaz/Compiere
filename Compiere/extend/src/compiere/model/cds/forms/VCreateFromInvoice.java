package compiere.model.cds.forms;

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

import java.awt.event.*;
import java.beans.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.table.*;

import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.*;
import org.compiere.framework.PO;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Create Invoice Transactions from PO Orders or Receipt
 *
 *  @author Jorg Janke
 *  @version  $Id: VCreateFromInvoice.java,v 1.4 2006/07/30 00:51:28 jjanke Exp $
 */
public class VCreateFromInvoice extends VCreateFrom implements VetoableChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	VCreateFromInvoice(GridTab mTab)
	{
		super (mTab);
		log.info(mTab.toString());
	}   //  VCreateFromInvoice

	private boolean 	m_actionActive = false;
	private MInOut		m_inout = null;

	/**	Static Logger	*/
	private static CLogger 	s_log = CLogger.getCLogger (VCreateFromInvoice.class);
	
	private static final boolean TESTMODE = false;
	/** Commit every 100 entities	*/
	private static final int	COMMITCOUNT = TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));
	
	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	@Override
	protected boolean dynInit() throws Exception
	{
		log.config("");
		setTitle(Msg.getElement(Env.getCtx(), "C_Invoice_ID", false) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));

		parameterBankPanel.setVisible(false);
		invoiceLabel.setVisible(false);
		invoiceField.setVisible(false);
		locatorLabel.setVisible(false);
		locatorField.setVisible(false);

		initBPartner(true);
		bPartnerField.addVetoableChangeListener(this);
		return true;
	}   //  dynInit

	
	
	/**
	 * Get list of invoices for a particular business partner
	 * @param ctx
	 * @param C_BPartner_ID
	 * @return
	 */
	public static List< NamePair > getShipments( Ctx ctx, int C_BPartner_ID )
	{
		List< NamePair > pairs = new ArrayList< NamePair >();

		//	Display
		StringBuffer display = new StringBuffer("s.DocumentNo||' - '||")
			.append(DB.TO_CHAR("s.MovementDate", DisplayTypeConstants.Date, Env.getAD_Language(Env.getCtx())));
		//
		StringBuffer sql1 = new StringBuffer("SELECT s.M_InOut_ID,").append(display)
			.append(" FROM M_InOut s "
			+ "WHERE s.C_BPartner_ID=? AND s.IsSOTrx='N' AND s.DocStatus IN ('CL','CO')"
			+ " AND s.M_InOut_ID IN "
				+ "(SELECT sl.M_InOut_ID FROM M_InOutLine sl"
				+ " LEFT OUTER JOIN M_MatchInv mi ON (sl.M_InOutLine_ID=mi.M_InOutLine_ID) "
				+ "GROUP BY sl.M_InOut_ID,mi.M_InOutLine_ID,sl.MovementQty "
				+ "HAVING (sl.MovementQty<>SUM(mi.Qty) AND mi.M_InOutLine_ID IS NOT NULL)"
				+ " OR mi.M_InOutLine_ID IS NULL) "
			+ "ORDER BY s.MovementDate");
		
		MRole role = MRole.get( ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false );
		String sql = role.addAccessSQL( sql1.toString(), "M_InOut", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO );
  
		PreparedStatement pstmt = DB.prepareStatement(sql.toString(), (Trx)null);
		ResultSet rs = null;
		try
		{
			pstmt.setInt(1, C_BPartner_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				pairs.add( new KeyNamePair( rs.getInt( 1 ), rs.getString( 2 ) ) );
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return pairs;
	}
	
	
	
	/**
	 *  Init Details - load receipts not invoiced
	 *  @param C_BPartner_ID BPartner
	 */
	@Override
	protected void initBPDetails(int C_BPartner_ID)
	{
		log.config("C_BPartner_ID" + C_BPartner_ID);

		//  load Shipments (Receipts) - Completed, Closed
		shipmentField.removeActionListener(this);
		shipmentField.removeAllItems();
		//	None
		KeyNamePair pp = new KeyNamePair(0,"");
		shipmentField.addItem(pp);

		List< NamePair > shipments = getShipments( Env.getCtx(), C_BPartner_ID );
		for( NamePair pair : shipments )
		{
			shipmentField.addItem( pair );
		}
		
		shipmentField.setSelectedIndex(0);
		shipmentField.addActionListener(this);
	}   //  initDetails

	/**
	 *  Action Listener
	 *  @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		if (m_actionActive)
			return;
		m_actionActive = true;
		log.config("Action=" + e.getActionCommand());
		//  Order
		if (e.getSource().equals(orderField))
		{
			KeyNamePair pp = (KeyNamePair)orderField.getSelectedItem();
			int C_Order_ID = 0;
			if (pp != null)
				C_Order_ID = pp.getKey();
			//  set Invoice and Shipment to Null
			invoiceField.setSelectedIndex(-1);
			shipmentField.setSelectedIndex(-1);
			loadOrder(C_Order_ID, true);
		}
		//  Shipment
		else if (e.getSource().equals(shipmentField))
		{
			KeyNamePair pp = (KeyNamePair)shipmentField.getSelectedItem();
			int M_InOut_ID = 0;
			if (pp != null)
				M_InOut_ID = pp.getKey();
			//  set Order and Invoice to Null
			orderField.setSelectedIndex(-1);
			invoiceField.setSelectedIndex(-1);
			loadShipment(M_InOut_ID);
		}
		m_actionActive = false;
	}   //  actionPerformed

	/**
	 *  Change Listener
	 *  @param e event
	 */
	public void vetoableChange (PropertyChangeEvent e)
	{
		log.config(e.getPropertyName() + "=" + e.getNewValue());

		//  BPartner - load Order/Invoice/Shipment
		if (e.getPropertyName() == "C_BPartner_ID")
		{
			int C_BPartner_ID = ((Integer)e.getNewValue()).intValue();
			initBPartnerOIS (C_BPartner_ID, true);
		}
		tableChanged(null);
	}   //  vetoableChange



	
	/**
	 *  Load Data - Shipment not invoiced
	 *  @param M_InOut_ID InOut
	 */
	private void loadShipment (int M_InOut_ID)
	{
		log.config("M_InOut_ID=" + M_InOut_ID);
		m_inout = new MInOut(Env.getCtx(), M_InOut_ID, null);
		p_order = null;
		if (m_inout.getC_Order_ID() != 0)
			p_order = new MOrder (Env.getCtx(), m_inout.getC_Order_ID(), null);

		Vector<Vector< Object >> data = getShipmentData( Env.getCtx(), M_InOut_ID );
		//
		loadTableOIS (data);
	}   //  loadShipment

	
	/**
	 * Get the table data for a particular shipment
	 * 
	 * @param M_InOut_ID
	 * @return
	 */
	public static Vector< Vector< Object > > getShipmentData( Ctx ctx, int M_InOut_ID )
	{
		
		Vector< Vector< Object > > data = new Vector< Vector< Object > >();
		StringBuffer sql = new StringBuffer( "SELECT " // QtyEntered
				+ "l.MovementQty-SUM(NVL(mi.Qty,0)),l.QtyEntered,l.MovementQty," 
				+ " l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name)," // 4..5
				+ " l.M_Product_ID,p.Name, l.M_InOutLine_ID,l.Line," // 6..9
				+ " l.C_OrderLine_ID " ); // 10

		if( Env.isBaseLanguage( ctx, "C_UOM" ) )
		{
			sql.append( "FROM C_UOM uom " ).append( "INNER JOIN M_InOutLine l ON (l.C_UOM_ID=uom.C_UOM_ID) " );
		}
		else
		{
			// nnayak : Fix for bug 1722916
			sql.append( "FROM C_UOM_Trl uom " ).append(
					"INNER JOIN M_InOutLine l ON (l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='" ).append(
					Env.getAD_Language( ctx ) ).append( "') " );
		}
		sql.append( "INNER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID) " ).append(
				"LEFT OUTER JOIN M_MatchInv mi ON (l.M_InOutLine_ID=mi.M_InOutLine_ID) " ).append( "WHERE l.M_InOut_ID=? " ) // #1
				.append(
						"GROUP BY l.MovementQty,l.QtyEntered,l.MovementQty," + "l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),"
								+ "l.M_Product_ID,p.Name, l.M_InOutLine_ID,l.Line,l.C_OrderLine_ID " ).append( "ORDER BY l.Line" );

		PreparedStatement pstmt = DB.prepareStatement( sql.toString(), (Trx) null);
		ResultSet rs = null;
		try
		{			
			pstmt.setInt( 1, M_InOut_ID );
			rs = pstmt.executeQuery();
			while( rs.next() )
			{
				Vector< Object > line = new Vector< Object >( 7 );
				line.add( Boolean.valueOf( false ) ); // 0-Selection
				BigDecimal qtyMovement = rs.getBigDecimal( 1 );
				BigDecimal qtyEnt = rs.getBigDecimal( 2 );
				BigDecimal qtyMov = rs.getBigDecimal( 3 );
				BigDecimal multiplier = BigDecimal.ZERO;
				if(qtyMov.compareTo(BigDecimal.ZERO) != 0){
					multiplier = qtyEnt.divide(qtyMov,10,BigDecimal.ROUND_HALF_UP);
				}
				BigDecimal qtyEntered = qtyMovement.multiply( multiplier );
				line.add( new Double( qtyEntered.doubleValue() ) ); // 1-Qty
				KeyNamePair pp = new KeyNamePair( rs.getInt( 4 ), rs.getString( 5 ).trim() );
				line.add( pp ); // 2-UOM
				pp = new KeyNamePair( rs.getInt( 6 ), rs.getString( 7 ) );
				line.add( pp ); // 3-Product
				int C_OrderLine_ID = rs.getInt( 10 );
				if( rs.wasNull() )
					line.add( null ); // 4-Order
				else
					line.add( new KeyNamePair( C_OrderLine_ID, "." ) );
				pp = new KeyNamePair( rs.getInt( 8 ), rs.getString( 9 ) );
				line.add( pp ); // 5-Ship
				line.add( null ); // 6-Invoice
				data.add( line );
			}
			rs.close();
			pstmt.close();
		}
		catch( SQLException e )
		{
			s_log.log( Level.SEVERE, sql.toString(), e );
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return data;
	}
	
	

	/**
	 * List number of rows selected
	 */
	@Override
	protected void info()
	{
		TableModel model = dataTable.getModel();
		int rows = model.getRowCount();
		int count = 0;
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)model.getValueAt(i, 0)).booleanValue())
				count++;
		}
		statusBar.setStatusLine(String.valueOf(count));
	}   //  infoInvoice

	/**
	 *  Save - Create Invoice Lines
	 *  @return true if saved
	 */
	@Override
	protected boolean save()
	{
		log.config("");
		TableModel model = dataTable.getModel();

		//  Invoice
		//int C_Invoice_ID = ((Integer)p_mTab.getValue("C_Invoice_ID")).intValue();
		int C_Invoice_ID = super.c_InvoiceID;

		return saveData( Env.getCtx(), model, p_order, m_inout, C_Invoice_ID );

	}		
		
	public static boolean saveData( Ctx ctx, TableModel model, MOrder p_order, MInOut m_inout, int C_Invoice_ID )
	{
		int[] aux = MInOut.getAllIDs("M_InOut", "C_ORDER_ID = " + p_order.get_ID(), null);

		if(aux.length>0)
			m_inout = new MInOut( Env.getCtx(), aux[0], null);
		
		List<MInvoiceLine> invoiceLinesToSave = new ArrayList<MInvoiceLine>();
		boolean isBatch = ctx.isBatchMode();
		ctx.setBatchMode(true);
		Trx trx = Trx.get("MInvoiceLine");
		int rows = model.getRowCount();
		if (rows == 0)
			return false;

		String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM C_InvoiceLine WHERE C_Invoice_ID=?";
		int lineNo = QueryUtil.getSQLValue ((Trx)null, sql, C_Invoice_ID);
		
		/** Modify by Patricia Ayuso */	
		//MInvoice invoice = new MInvoice (ctx, C_Invoice_ID, null);
		compiere.model.cds.MInvoice invoice = new compiere.model.cds.MInvoice (ctx, C_Invoice_ID, null);
		s_log.config(invoice.toString());

		if (p_order != null)
		{
			invoice.setOrder(p_order);	//	overwrite header values
			invoice.setM_Warehouse_ID(p_order.getM_Warehouse_ID());
			invoice.save();
		}
		if (m_inout != null && m_inout.getM_InOut_ID() != 0 
			&& m_inout.getC_Invoice_ID() == 0)	//	only first time
		{
			m_inout.setC_Invoice_ID(C_Invoice_ID);
			m_inout.save();
		}
		
		//  Lines
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)model.getValueAt(i, 0)).booleanValue())
			{
				//  variable values
				Double d = (Double)model.getValueAt(i, 1);              //  1-Qty
				BigDecimal QtyEntered = new BigDecimal(d.doubleValue());
				KeyNamePair pp = (KeyNamePair)model.getValueAt(i, 2);   //  2-UOM
				int C_UOM_ID = pp.getKey();
				//
				pp = (KeyNamePair)model.getValueAt(i, 3);               //  3-Product
				int M_Product_ID = 0;
				if (pp != null)
					M_Product_ID = pp.getKey();
			//	int C_Charge_ID = 0;
				//
				int C_OrderLine_ID = 0;
				pp = (KeyNamePair)model.getValueAt(i, 4);               //  4-OrderLine
				if (pp != null)
					C_OrderLine_ID = pp.getKey();
				int M_InOutLine_ID = 0;
				//pp = (KeyNamePair)model.getValueAt(i, 5);               //  5-Shipment
				pp = (KeyNamePair)model.getValueAt(i, 8);               //  8-Shipment
				if (pp != null)
					M_InOutLine_ID = pp.getKey();
				//	Precision of Qty UOM
				QtyEntered = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_ID), BigDecimal.ROUND_HALF_UP);
				//
				s_log.fine("Line QtyEntered=" + QtyEntered
					+ ", Product_ID=" + M_Product_ID 
					+ ", OrderLine_ID=" + C_OrderLine_ID + ", InOutLine_ID=" + M_InOutLine_ID);

				//	Create new Invoice Line
				/** Modify by Patricia Ayuso */
				//MInvoiceLine invoiceLine = new MInvoiceLine (invoice);				
				compiere.model.cds.MInvoiceLine invoiceLine = new compiere.model.cds.MInvoiceLine (invoice);
				invoiceLine.setM_Product_ID(M_Product_ID, C_UOM_ID);	//	Line UOM
				invoiceLine.setQty(QtyEntered);							//	Invoiced/Entered
			

				//  Info
				MOrderLine orderLine = null;
				if (C_OrderLine_ID != 0)
					orderLine = new MOrderLine (ctx, C_OrderLine_ID, null);
				MInOutLine inoutLine = null;
				if (M_InOutLine_ID != 0)
				{
					inoutLine = new MInOutLine (ctx, M_InOutLine_ID, null);
					if (orderLine == null && inoutLine.getC_OrderLine_ID() != 0)
					{
						C_OrderLine_ID = inoutLine.getC_OrderLine_ID();
						orderLine = new MOrderLine (ctx, C_OrderLine_ID, null);
					}
				}
				else
				{
					//Mejora de tiempo BECO
					String where = "";
					if(m_inout!=null)
						where = "M_INOUT_ID = " + m_inout.get_ID();
					MInOutLine[] lines = MInOutLine.getOfOrderLine(ctx, 
								C_OrderLine_ID, where, null);
					s_log.fine ("Receipt Lines with OrderLine = #" + lines.length);
					if (lines.length > 0)
					{
						for (MInOutLine line : lines) {
							if (line.getQtyEntered().compareTo(QtyEntered) == 0)
							{
								inoutLine = line;
								M_InOutLine_ID = inoutLine.getM_InOutLine_ID();
								break;
							}
						}
						if (inoutLine == null)
						{
							inoutLine = lines[0];	//	first as default
							M_InOutLine_ID = inoutLine.getM_InOutLine_ID();
						}
					}
				}	//	get Ship info

				//	Shipment Info
				if (inoutLine != null)
				{
					invoiceLine.setShipLine(inoutLine);		//	overwrites
					
					BigDecimal QtyInvoiced = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
							invoiceLine.getC_UOM_ID(), QtyEntered);
					if (QtyInvoiced == null)
						QtyInvoiced = QtyEntered;
					
					if (inoutLine.getQtyEntered().compareTo(inoutLine.getMovementQty()) != 0)
						invoiceLine.setQtyInvoiced(QtyInvoiced);
				}
				else
					s_log.fine("No Receipt Line");
					
				//	Order Info
				if (orderLine != null)
				{
					invoiceLine.setOrderLine(orderLine);	//	overwrites
					
					/* nnayak - Bug 1567690. The organization from the Orderline can be different from the organization 
					on the header */
					invoiceLine.setClientOrg(orderLine.getAD_Client_ID(), orderLine.getAD_Org_ID());

					BigDecimal QtyInvoiced = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
							invoiceLine.getC_UOM_ID(), QtyEntered);
					if (QtyInvoiced == null)
						QtyInvoiced = QtyEntered;
					
					if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
						invoiceLine.setQtyInvoiced(QtyInvoiced);
					
				}
				else
				{
					s_log.fine("No Order Line");
					
					/* nnayak - Bug 1567690. The organization from the Receipt can be different from the organization 
					on the header */
					if(inoutLine != null)
						invoiceLine.setClientOrg(inoutLine.getAD_Client_ID(), inoutLine.getAD_Org_ID());
					
					invoiceLine.setPrice();
					invoiceLine.setTax();
				}
				invoiceLine.setLine(lineNo);
				lineNo += 10;
				invoiceLinesToSave.add(invoiceLine);
				if(invoiceLinesToSave.size() >= COMMITCOUNT){
					if(!PO.saveAll(trx, invoiceLinesToSave)){
						if(trx != null)
							trx.close();
						throw new CompiereStateException("Could not save invoice lines");
					}
					else{
						trx.commit();
						invoiceLinesToSave.clear();
					}
				}

			}   //   if selected
		}   //  for all rows

		if(invoiceLinesToSave != null && invoiceLinesToSave.size() > 0){
			if(!PO.saveAll(trx, invoiceLinesToSave)){
				if(trx != null)
					trx.close();
				throw new CompiereStateException("Could not save invoice lines");
			}
			else {
				trx.commit();
				invoiceLinesToSave.clear();
			}	
		}
		invoice.setIsSOTrx(invoice.isSOTrx());
		invoice.save();
		if(trx != null)
			trx.close();
		ctx.setBatchMode(isBatch);
		return true;
	}   //  saveInvoice

}   //  VCreateFromInvoice
