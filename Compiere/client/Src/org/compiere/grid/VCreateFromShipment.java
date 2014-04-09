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
package org.compiere.grid;

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
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.util.*;

/**
 *  Create Shipments Transactions - from PO Orders or AP Invoices
 *
 *  @author Jorg Janke
 *  @version  $Id: VCreateFromShipment.java,v 1.4 2006/07/30 00:51:28 jjanke Exp $
 */
public class VCreateFromShipment extends VCreateFrom implements VetoableChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	VCreateFromShipment(GridTab mTab)
	{
		super (mTab);
		//	log.info( "VCreateFromShipment");
	}   //  VCreateFromShipment

	/**  Loaded Invoice             */
	private MInvoice		m_invoice = null;

	/**	Static Logger	*/
	private static CLogger 	s_log = CLogger.getCLogger (VCreateFromShipment.class);

	private static final boolean TESTMODE = false;
	/** Commit every 100 entities	*/
	private static final int	COMMITCOUNT = TESTMODE?11:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));

	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	@Override
	protected boolean dynInit() throws Exception
	{
		log.config("");
		setTitle(Msg.getElement(Env.getCtx(), "M_InOut_ID", false) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));

		parameterBankPanel.setVisible(false);
		shipmentLabel.setVisible(false);
		shipmentField.setVisible(false);

		MLocatorLookup locator = new MLocatorLookup(Env.getCtx(), p_WindowNo);
		locatorField = new VLocator ("M_Locator_ID", true, false, true,	locator, p_WindowNo);

		initBPartner(false);
		bPartnerField.addVetoableChangeListener(this);
		return true;
	}   //  dynInit



	/**
	 * Get list of invoices for a particular business partner
	 * @param ctx
	 * @param C_BPartner_ID
	 * @return
	 */
	public static List< NamePair > getInvoices( Ctx ctx, int C_BPartner_ID, boolean isReturnTrx )
	{
		List< NamePair > pairs = new ArrayList< NamePair >();


		StringBuffer display = new StringBuffer( "i.DocumentNo||' - '||" ).append(
				DB.TO_CHAR( "DateInvoiced", DisplayTypeConstants.Date, Env.getAD_Language( ctx ) ) ).append( "|| ' - ' ||" )
				.append( DB.TO_CHAR( "GrandTotal", DisplayTypeConstants.Amount, Env.getAD_Language( ctx ) ) );
		//
		StringBuffer sql1 = new StringBuffer( "SELECT i.C_Invoice_ID," ).append( display ).append(
				" FROM C_Invoice i INNER JOIN C_DocType d ON (i.C_DocType_ID = d.C_DocType_ID) "
				+ "WHERE i.C_BPartner_ID=? AND i.IsSOTrx='N' " 
				+ "AND d.IsReturnTrx='" + (isReturnTrx?"Y":"N") +"' " 
				+ "AND i.DocStatus IN ('CL','CO')"
				+ " AND i.C_Invoice_ID IN " + "(SELECT il.C_Invoice_ID FROM C_InvoiceLine il"
				+ " LEFT OUTER JOIN M_MatchInv mi ON (il.C_InvoiceLine_ID=mi.C_InvoiceLine_ID) "
				+ "GROUP BY il.C_Invoice_ID,mi.C_InvoiceLine_ID,il.QtyInvoiced "
				+ "HAVING (il.QtyInvoiced<>SUM(mi.Qty) AND mi.C_InvoiceLine_ID IS NOT NULL)"
				+ " OR mi.C_InvoiceLine_ID IS NULL) " + "ORDER BY i.DateInvoiced" );

		MRole role = MRole.get( ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false );
		String sql = role.addAccessSQL( sql1.toString(), "C_Invoice", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO );

		PreparedStatement pstmt = DB.prepareStatement( sql.toString(), (Trx) null);
		ResultSet rs = null;
		try
		{
			pstmt.setInt( 1, C_BPartner_ID );
			rs = pstmt.executeQuery();
			while( rs.next() )
			{
				pairs.add( new KeyNamePair( rs.getInt( 1 ), rs.getString( 2 ) ) );
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
		return pairs;
	}



	/**
	 *  Init Details - load invoices not shipped
	 *  @param C_BPartner_ID BPartner
	 */
	@Override
	protected void initBPDetails(int C_BPartner_ID)
	{
		log.config("C_BPartner_ID=" + C_BPartner_ID);

		//  load AP Invoice closed or complete
		invoiceField.removeActionListener(this);
		invoiceField.removeAllItems();
		//	None
		KeyNamePair pp = new KeyNamePair(0,"");
		invoiceField.addItem(pp);

		//
		boolean isReturnTrx = "Y".equals(Env.getCtx().getContext(p_WindowNo, "IsReturnTrx") );
		List< NamePair > invoices = getInvoices( Env.getCtx(), C_BPartner_ID, isReturnTrx );
		for( NamePair pair : invoices )
		{
			invoiceField.addItem( pair );
		}

		invoiceField.setSelectedIndex(0);
		invoiceField.addActionListener(this);
	}   //  initBPDetails


	/**
	 *  Action Listener
	 *  @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		log.config("Action=" + e.getActionCommand());

		//  Order
		if (e.getSource().equals(orderField))
		{
			KeyNamePair pp = (KeyNamePair)orderField.getSelectedItem();
			if (pp == null || pp.getKey() == 0)
				;
			else
			{
				int C_Order_ID = pp.getKey();
				//  set Invoice and Shipment to Null
				invoiceField.setSelectedIndex(-1);
				shipmentField.setSelectedIndex(-1);
				loadOrder(C_Order_ID, false);
				m_invoice = null;
			}
		}
		//  Invoice
		else if (e.getSource().equals(invoiceField))
		{
			KeyNamePair pp = (KeyNamePair)invoiceField.getSelectedItem();
			if (pp == null || pp.getKey() == 0)
				;
			else
			{
				int C_Invoice_ID = pp.getKey();
				//  set Order and Shipment to Null
				orderField.setSelectedIndex(-1);
				shipmentField.setSelectedIndex(-1);
				loadInvoice(C_Invoice_ID);
			}
		}
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
			initBPartnerOIS (C_BPartner_ID, false);
		}
		tableChanged(null);
	}   //  vetoableChange




	/**
	 *  Load Data - Invoice
	 *  @param C_Invoice_ID Invoice
	 */
	private void loadInvoice (int C_Invoice_ID)
	{
		m_invoice = new MInvoice (Env.getCtx(), C_Invoice_ID, null);    //  save
		p_order = null;

		Vector<Vector< Object >> data = getInvoiceData( Env.getCtx(), C_Invoice_ID );		

		loadTableOIS (data);
	}


	/**
	 * Get the table data for a particular invoice
	 * @param C_Invoice_ID
	 * @return
	 */
	public static Vector< Vector< Object > > getInvoiceData( Ctx ctx, int C_Invoice_ID )
	{

		s_log.config("C_Invoice_ID=" + C_Invoice_ID);

		Vector<Vector< Object >> data = new Vector<Vector< Object >>();		
		StringBuffer sql = new StringBuffer("SELECT "	//	Entered UOM
				+ "l.QtyInvoiced-SUM(NVL(mi.Qty,0)),l.QtyEntered,l.QtyInvoiced,"
				+ " l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),"			//  4..5
				+ " l.M_Product_ID,p.Name, l.C_InvoiceLine_ID,l.Line,"      //  6..9
				+ " l.C_OrderLine_ID ");                   					//  10

		if (Env.isBaseLanguage(ctx, "C_UOM"))
		{
			sql.append("FROM C_UOM uom ")
			.append("INNER JOIN C_InvoiceLine l ON (l.C_UOM_ID=uom.C_UOM_ID) ");

		}
		else
		{
			sql.append("FROM C_UOM_Trl uom ")
			.append("INNER JOIN C_InvoiceLine l ON (l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='")
			.append(Env.getAD_Language(ctx)).append("') ");
		}
		sql.append("INNER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID) ")
		.append("LEFT OUTER JOIN M_MatchInv mi ON (l.C_InvoiceLine_ID=mi.C_InvoiceLine_ID) ")
		.append("WHERE l.C_Invoice_ID=? " 									//  #1
				+ "GROUP BY l.QtyInvoiced,l.QtyEntered,l.QtyInvoiced,"
				+ "l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),"
				+ "l.M_Product_ID,p.Name, l.C_InvoiceLine_ID,l.Line,l.C_OrderLine_ID "
				+ "ORDER BY l.Line");
		PreparedStatement pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
		ResultSet rs = null;
		try
		{
			pstmt.setInt(1, C_Invoice_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(7);
				line.add(Boolean.valueOf(false));           //  0-Selection
				BigDecimal qtyInvoiced = rs.getBigDecimal(1);
				BigDecimal qtyEnt = rs.getBigDecimal( 2 );
				BigDecimal qtyInv = rs.getBigDecimal( 3 );
				BigDecimal multiplier = BigDecimal.ZERO;
				if(qtyInv.compareTo(BigDecimal.ZERO) != 0){
					multiplier = qtyEnt.divide(qtyInv,10,BigDecimal.ROUND_HALF_UP);
				}
				BigDecimal qtyEntered = qtyInvoiced.multiply(multiplier);
				line.add(new Double(qtyEntered.doubleValue()));  //  1-Qty
				KeyNamePair pp = new KeyNamePair(rs.getInt(4), rs.getString(5).trim());
				line.add(pp);                           //  2-UOM
				pp = new KeyNamePair(rs.getInt(6), rs.getString(7));
				line.add(pp);                           //  3-Product
				int C_OrderLine_ID = rs.getInt(10);
				if (rs.wasNull())
					line.add(null);                     //  4-Order
				else
					line.add(new KeyNamePair(C_OrderLine_ID,"."));
				line.add(null);                     	//  5-Ship
				pp = new KeyNamePair(rs.getInt(8), rs.getString(9));
				line.add(pp);                           //  6-Invoice
				data.add(line);
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
		return data;
	}   //  loadInvoice


	/**
	 *  List number of rows selected
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
	}   //  info


	/**
	 *  Save - create Shipments
	 *  @return true if saved
	 */
	@Override
	protected boolean save()
	{
		log.config("");
		TableModel model = dataTable.getModel();
		//
		Integer loc = (Integer)locatorField.getValue();
		if (loc == null || loc.intValue() == 0)
		{
			locatorField.setBackground(CompierePLAF.getFieldBackground_Error());
			return false;
		}
		int M_Locator_ID = loc.intValue();
		//	Get Shipment
		int M_InOut_ID = ((Integer)p_mTab.getValue("M_InOut_ID")).intValue();

		return saveData( Env.getCtx(), model, p_order, m_invoice, M_Locator_ID, M_InOut_ID );
	}


	static int getID( Object obj )
	{
		int id = 0;
		if( obj instanceof KeyNamePair )
			id = ((KeyNamePair) obj).getKey();
		else if( obj instanceof Number )
			id = ((Number) obj).intValue();
		return id;
	}


	public static boolean saveData( Ctx ctx, TableModel model, MOrder p_order, MInvoice m_invoice, int M_Locator_ID, int M_InOut_ID )
	{
		Map<MInOutLine,MInvoiceLine> inoutLineMap = new HashMap<MInOutLine,MInvoiceLine>();
		int rows = model.getRowCount();
		if (rows == 0)
			return false;
		MInOut inout = new MInOut (ctx, M_InOut_ID, null);
		s_log.config(inout + ", C_Locator_ID=" + M_Locator_ID);

		boolean fromInvoice = false;
		String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM M_InOutLine WHERE M_InOut_ID=?";
		int lineNo = QueryUtil.getSQLValue ((Trx)null, sql, M_InOut_ID);
		
		/**
		 *  Selected        - 0
		 *  QtyEntered      - 1
		 *  C_UOM_ID        - 2
		 *  M_Product_ID    - 3
		 *  OrderLine       - 4
		 *  ShipmentLine    - 5
		 *  InvoiceLine     - 6
		 */

		//  Lines
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)model.getValueAt(i, 0)).booleanValue())
			{
				//  variable values
				Double d = (Double) model.getValueAt(i, 1);              //  1-Qty
				BigDecimal QtyEntered = new BigDecimal(d .doubleValue());

				int C_UOM_ID = getID( model.getValueAt(i, 2) );   //  2-UOM
				int M_Product_ID = getID( model.getValueAt(i, 3) );               //  3-Product
				int C_OrderLine_ID = getID( model.getValueAt(i, 4) );               //  4-OrderLine
				int C_InvoiceLine_ID = getID( model.getValueAt(i, 6) );               //  6-InvoiceLine
				MInvoiceLine il = null;
				if (C_InvoiceLine_ID != 0)
					il = new MInvoiceLine (ctx, C_InvoiceLine_ID, null);
				//	Precision of Qty UOM
				QtyEntered = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_ID), BigDecimal.ROUND_HALF_UP);
				//
				s_log.fine("Line QtyEntered=" + QtyEntered
						+ ", UOM=" + C_UOM_ID  
						+ ", Product=" + M_Product_ID 
						+ ", OrderLine=" + C_OrderLine_ID + ", InvoiceLine=" + C_InvoiceLine_ID);

				//	Credit Memo - negative Qty
				/*if (m_invoice != null && m_invoice.isCreditMemo() )
					QtyEntered = QtyEntered.negate();*/

				//	Create new InOut Line
				MInOutLine iol = new MInOutLine (inout);
				iol.setM_Product_ID(M_Product_ID, C_UOM_ID);	//	Line UOM
				iol.setQty(QtyEntered);							//	Movement/Entered
				//
				MOrderLine ol = null;
				if (C_OrderLine_ID != 0)
				{
					iol.setC_OrderLine_ID(C_OrderLine_ID);
					ol = new MOrderLine (ctx, C_OrderLine_ID, null);
					//	iol.setOrderLine(ol, M_Locator_ID, QtyEntered);
					if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0)
					{
						iol.setMovementQty(QtyEntered
								.multiply(ol.getQtyOrdered())
								.divide(ol.getQtyEntered(), 12, BigDecimal.ROUND_HALF_UP));
						iol.setC_UOM_ID(ol.getC_UOM_ID());
					}
					iol.setM_AttributeSetInstance_ID(ol.getM_AttributeSetInstance_ID());
					iol.setDescription(ol.getDescription());
					//
					iol.setC_Project_ID(ol.getC_Project_ID());
					iol.setC_ProjectPhase_ID(ol.getC_ProjectPhase_ID());
					iol.setC_ProjectTask_ID(ol.getC_ProjectTask_ID());
					iol.setC_Activity_ID(ol.getC_Activity_ID());
					iol.setC_Campaign_ID(ol.getC_Campaign_ID());
					iol.setAD_OrgTrx_ID(ol.getAD_OrgTrx_ID());
					iol.setUser1_ID(ol.getUser1_ID());
					iol.setUser2_ID(ol.getUser2_ID());
				}
				else if (il != null)
				{
					//	iol.setInvoiceLine(il, M_Locator_ID, QtyEntered);
					if (il.getQtyEntered().compareTo(il.getQtyInvoiced()) != 0)
					{
						iol.setQtyEntered(QtyEntered
								.multiply(il.getQtyInvoiced())
								.divide(il.getQtyEntered(), 12, BigDecimal.ROUND_HALF_UP));
						iol.setC_UOM_ID(il.getC_UOM_ID());
					}
					iol.setDescription(il.getDescription());
					iol.setC_Project_ID(il.getC_Project_ID());
					iol.setC_ProjectPhase_ID(il.getC_ProjectPhase_ID());
					iol.setC_ProjectTask_ID(il.getC_ProjectTask_ID());
					iol.setC_Activity_ID(il.getC_Activity_ID());
					iol.setC_Campaign_ID(il.getC_Campaign_ID());
					iol.setAD_OrgTrx_ID(il.getAD_OrgTrx_ID());
					iol.setUser1_ID(il.getUser1_ID());
					iol.setUser2_ID(il.getUser2_ID());
				}
				//	Charge
				if (M_Product_ID == 0)
				{
					if (ol != null && ol.getC_Charge_ID() != 0)			//	from order
						iol.setC_Charge_ID(ol.getC_Charge_ID());
					else if (il != null && il.getC_Charge_ID() != 0)	//	from invoice
						iol.setC_Charge_ID(il.getC_Charge_ID());
				}
				//
				iol.setM_Locator_ID(M_Locator_ID);
				iol.setLine(lineNo);
				lineNo += 10;
				inoutLineMap.put(iol, il);
				fromInvoice = il != null;
				if(inoutLineMap.size() >= COMMITCOUNT){
					saveInOutLines(inoutLineMap,fromInvoice);
					inoutLineMap.clear();
				}
			}   //   if selected
		}   //  for all rows

		if(inoutLineMap != null && inoutLineMap.size() > 0)
			saveInOutLines(inoutLineMap,fromInvoice);

		/**
		 *  Update Header
		 *  - if linked to another order/invoice - remove link
		 *  - if no link set it
		 */
		if (p_order != null && p_order.getC_Order_ID() != 0)
		{
			inout.setC_Order_ID (p_order.getC_Order_ID());
			inout.setAD_OrgTrx_ID(p_order.getAD_OrgTrx_ID());
			inout.setC_Project_ID(p_order.getC_Project_ID());
			inout.setC_Campaign_ID(p_order.getC_Campaign_ID());
			inout.setC_Activity_ID(p_order.getC_Activity_ID());
			inout.setUser1_ID(p_order.getUser1_ID());
			inout.setUser2_ID(p_order.getUser2_ID());
			inout.setDateOrdered(p_order.getDateOrdered());
			inout.setPOReference(p_order.getPOReference());//setting PO reference from order
		}
		if (m_invoice != null && m_invoice.getC_Invoice_ID() != 0)
		{
			if (inout.getC_Order_ID() == 0)
				inout.setC_Order_ID (m_invoice.getC_Order_ID());
			inout.setC_Invoice_ID (m_invoice.getC_Invoice_ID());
			inout.setAD_OrgTrx_ID(m_invoice.getAD_OrgTrx_ID());
			inout.setC_Project_ID(m_invoice.getC_Project_ID());
			inout.setC_Campaign_ID(m_invoice.getC_Campaign_ID());
			inout.setC_Activity_ID(m_invoice.getC_Activity_ID());
			inout.setUser1_ID(m_invoice.getUser1_ID());
			inout.setUser2_ID(m_invoice.getUser2_ID());			
			inout.setPOReference(m_invoice.getPOReference());//setting PO reference from invoice
		}
		inout.save();
		return true;
	}   //  save



	private static void saveInOutLines(Map<MInOutLine,MInvoiceLine> inoutLineMap, boolean fromInvoice) {

		if(inoutLineMap == null)
			return;

		Trx trx = Trx.get("MInOutLine");

		if(inoutLineMap.size() > 0)
			if(!PO.saveAll(trx, new ArrayList<MInOutLine>(inoutLineMap.keySet()))){
				if(trx != null)
					trx.close();
				throw new CompiereStateException("Could not save MInOutLine (s)");
			}
			else if(fromInvoice){
				for(Map.Entry<MInOutLine,MInvoiceLine> entry : inoutLineMap.entrySet())
					entry.getValue().setM_InOutLine_ID(entry.getKey().getM_InOutLine_ID());
				if(!PO.saveAll(trx, new ArrayList<MInvoiceLine>(inoutLineMap.values()))){
					if(trx != null)
						trx.close();
					throw new CompiereStateException("Could not create link to MInOutLine (s)");
				}
				else
					trx.commit();	
			}

		if(trx != null){
			trx.commit();
			trx.close();
		}
	}

}   //  VCreateFromShipment
