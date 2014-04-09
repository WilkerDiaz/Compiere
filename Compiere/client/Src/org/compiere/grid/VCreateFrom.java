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

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.compiere.apps.*;
import org.compiere.common.constants.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 *  CreateFrom (Called from GridController.startProcess)
 *
 *  @author  Jorg Janke
 *  @version $Id: VCreateFrom.java,v 1.4 2006/10/11 09:52:23 comdivision Exp $
 */
public abstract class VCreateFrom extends CDialog
	implements ActionListener, TableModelListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Factory - called from APanel
	 *  @param  mTab        Model Tab for the p_trx
	 *  @return JDialog
	 */
	public static VCreateFrom create (GridTab mTab)
	{
		//	dynamic init preparation
		int AD_Table_ID = Env.getCtx().getContextAsInt( mTab.getWindowNo(), "BaseTable_ID");

		VCreateFrom retValue = null;
		if (AD_Table_ID == 392)             //  C_BankStatement
			retValue = new VCreateFromStatement (mTab);
		else if (AD_Table_ID == 318)        //  C_Invoice
			retValue = new VCreateFromInvoice (mTab);
		else if (AD_Table_ID == 319)        //  M_InOut
			retValue = new VCreateFromShipment (mTab);
		else if (AD_Table_ID == 426)		//	C_PaySelection
			return null;	//	ignore - will call process C_PaySelection_CreateFrom
		else    //  Not supported CreateFrom
		{
			s_log.info("Unsupported AD_Table_ID=" + AD_Table_ID);
			return null;
		}
		return retValue;
	}   //  create

	
	/**************************************************************************
	 *  Protected super class Constructor
	 *  @param mTab MTab
	 */
	VCreateFrom (GridTab mTab)
	{
		super(Env.getWindow(mTab.getWindowNo()), true);
		log.info(mTab.toString());
		p_WindowNo = mTab.getWindowNo();
		p_mTab = mTab;

		try
		{
			if (!dynInit())
				return;
			jbInit();
			confirmPanel.addActionListener(this);
			//  Set status
			statusBar.setStatusDB("");
			tableChanged(null);
			p_initOK = true;
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			p_initOK = false;
		}
		AEnv.positionCenterWindow(Env.getWindow(p_WindowNo), this);
	}   //  VCreateFrom

	/** Window No               */
	protected int               p_WindowNo;
	/** Model Tab               */
	protected GridTab         		p_mTab;

	private boolean             p_initOK = false;

	/** Loaded Order            */
	protected MOrder 			p_order = null;
	/**	Logger			*/
	protected CLogger 		log = CLogger.getCLogger(getClass());
	/**	Static Logger	*/
	private static CLogger 	s_log = CLogger.getCLogger (VCreateFrom.class);
	
	//
	private CPanel parameterPanel = new CPanel();
	protected CPanel parameterBankPanel = new CPanel();
	private BorderLayout parameterLayout = new BorderLayout();
	private JLabel bankAccountLabel = new JLabel();
	protected CPanel parameterStdPanel = new CPanel();
	private JLabel bPartnerLabel = new JLabel();
	protected VLookup bankAccountField;
	private GridBagLayout parameterStdLayout = new GridBagLayout();
	private GridBagLayout parameterBankLayout = new GridBagLayout();
	protected VLookup bPartnerField;
	private JLabel orderLabel = new JLabel();
	protected JComboBox orderField = new JComboBox();
	protected JLabel invoiceLabel = new JLabel();
	protected JComboBox invoiceField = new JComboBox();
	protected JLabel shipmentLabel = new JLabel();
	protected JComboBox shipmentField = new JComboBox();
	private JScrollPane dataPane = new JScrollPane();
	private CPanel southPanel = new CPanel();
	private BorderLayout southLayout = new BorderLayout();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	protected StatusBar statusBar = new StatusBar();
	protected MiniTable dataTable = new MiniTable();
	protected JLabel locatorLabel = new JLabel();
	protected VLocator locatorField = new VLocator();
	protected AppsAction selectAllAction =
		new AppsAction (SELECT_DESELECT_ALL, null, Msg.getMsg(Env.getCtx(), SELECT_DESELECT_ALL), true);
	
	private static final String	SELECT_DESELECT_ALL = "SelectDeselectAll";
	
	/**
	 *  Static Init.
	 *  <pre>
	 *  parameterPanel
	 *      parameterBankPanel
	 *      parameterStdPanel
	 *          bPartner/order/invoice/shopment/licator Label/Field
	 *  dataPane
	 *  southPanel
	 *      confirmPanel
	 *      statusBar
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		parameterPanel.setLayout(parameterLayout);
		parameterStdPanel.setLayout(parameterStdLayout);
		parameterBankPanel.setLayout(parameterBankLayout);
		//
		bankAccountLabel.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
		bPartnerLabel.setText(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
		orderLabel.setText(Msg.getElement(Env.getCtx(), "C_Order_ID", false));
		invoiceLabel.setText(Msg.getElement(Env.getCtx(), "C_Invoice_ID", false));
		//Non useful field for us - Patricia Ayuso
		//shipmentLabel.setText(Msg.getElement(Env.getCtx(), "M_InOut_ID", false));
		locatorLabel.setText(Msg.translate(Env.getCtx(), "M_Locator_ID"));
		//
		this.getContentPane().add(parameterPanel, BorderLayout.NORTH);
		parameterPanel.add(parameterBankPanel, BorderLayout.NORTH);
		parameterBankPanel.add(bankAccountLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		if (bankAccountField != null)
			parameterBankPanel.add(bankAccountField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(parameterStdPanel, BorderLayout.CENTER);
		parameterStdPanel.add(bPartnerLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		if (bPartnerField != null)
			parameterStdPanel.add(bPartnerField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterStdPanel.add(orderLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterStdPanel.add(orderField,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterStdPanel.add(invoiceLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterStdPanel.add(invoiceField,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterStdPanel.add(shipmentLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//Non useful field for us - Patricia Ayuso
		//parameterStdPanel.add(shipmentField,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
		//	,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterStdPanel.add(locatorLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterStdPanel.add(locatorField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		this.getContentPane().add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(dataTable, null);
		//
		
		CToggleButton selectAllButton = (CToggleButton)selectAllAction.getButton();
		selectAllButton.setMargin(ConfirmPanel.s_insets);
		selectAllButton.addActionListener(this);
		confirmPanel.addComponent(selectAllButton);
		
		//
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(southLayout);
		southPanel.add(confirmPanel, BorderLayout.CENTER);
		// Trifon End
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(southLayout);
		southPanel.add(confirmPanel, BorderLayout.CENTER);
		southPanel.add(statusBar, BorderLayout.SOUTH);
	}   //  jbInit

	/**
	 *	Init OK to be able to make changes?
	 *  @return on if initialized
	 */
	public boolean isInitOK()
	{
		return p_initOK;
	}	//	isInitOK

	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	abstract boolean dynInit() throws Exception;

	/**
	 *  Init Business Partner Details
	 *  @param C_BPartner_ID BPartner
	 */
	abstract void initBPDetails(int C_BPartner_ID);

	/**
	 *  Add Info
	 */
	abstract void info();

	/**
	 *  Save & Insert Data
	 *  @return true if saved
	 */
	abstract boolean save();

	/*************************************************************************/

	/**
	 *  Action Listener
	 *  @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		log.config("Action=" + e.getActionCommand());
	//	if (m_action)
	//		return;
	//	m_action = true;

		//  OK - Save
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			if (save())
				dispose();
		}
		//  Cancel
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
		}
		// Select All
		// Trifon
		else if (e.getActionCommand().equals(SELECT_DESELECT_ALL)) 
		{
			TableModel model = dataTable.getModel();
			model.removeTableModelListener(this);
			
			// select or deselect all as required
			int rows = model.getRowCount();
			Boolean selectAll = selectAllAction.isPressed() ?
				Boolean.FALSE : Boolean.TRUE;
			for (int i = 0; i < rows; i++)
				model.setValueAt(selectAll, i, 0);
			
			model.addTableModelListener(this);
			
			info();
		}
	//	m_action = false;
	}   //  actionPerformed

	/**
	 *  Table Model Listener.
	 *  @param tme event
	 */
	public void tableChanged (TableModelEvent tme)
	{
		int type = -1;
		if (tme != null)
		{
			type = tme.getType();
			if (type != TableModelEvent.UPDATE)
				return;
			
			if (tme.getColumn() == 0)
			{
				// need to ensure the pressed or nor pressed is properly checked
				// if all have been manually selected or deselected
				TableModel model = dataTable.getModel();
				Boolean isPressed = (Boolean)model.getValueAt(0, 0);
				int rows = model.getRowCount();
				boolean equals = true;
				for (int i = 1; equals && i < rows; i++)
				{
					equals = isPressed.equals(model.getValueAt(i, 0));
				}
				
				if (equals) {
					selectAllAction.setPressed(isPressed);
				}
			}
		}
		
		log.config("Type=" + type);
		info();
	}   //  tableChanged

	
	/**************************************************************************
	 *  Load BPartner Field
	 *  @param forInvoice true if Invoices are to be created, false receipts
	 *  @throws Exception if Lookups cannot be initialized
	 */
	protected void initBPartner (boolean forInvoice) throws Exception
	{
		//  load BPartner
		int AD_Column_ID = 3499;        //  C_Invoice.C_BPartner_ID
		MLookup lookup = MLookupFactory.get (Env.getCtx(), p_WindowNo, AD_Column_ID, DisplayTypeConstants.Search);
		bPartnerField = new VLookup ("C_BPartner_ID", true, false, true, lookup);
		//
		int C_BPartner_ID = Env.getCtx().getContextAsInt( p_WindowNo, "C_BPartner_ID");
		bPartnerField.setValue(Integer.valueOf(C_BPartner_ID));

		//  initial loading
		initBPartnerOIS(C_BPartner_ID, forInvoice);
	}   //  initBPartner

	

	/**
	 * Get the list of orders for a given business partner
	 * @param ctx
	 * @param C_BPartner_ID
	 * @param isReturnTrx
	 * @param forInvoice <code>true</code> for orders matched to invoices, <code>false</code> for orders matched to shipments 
	 * @return
	 */
	public static ArrayList< NamePair > getOrders( Ctx ctx, int C_BPartner_ID, boolean isReturnTrx, boolean forInvoice, int winNo )
	{
		ArrayList< NamePair > pairs = new ArrayList< NamePair >();
		// Display
		StringBuffer display = new StringBuffer( "o.DocumentNo||' - ' ||" ).append(
				DB.TO_CHAR( "o.DateOrdered", DisplayTypeConstants.Date, Env.getAD_Language( ctx ) ) ).append( "||' - '||" ).append(
				DB.TO_CHAR( "o.GrandTotal", DisplayTypeConstants.Amount, Env.getAD_Language( ctx ) ) );

		String column = "m.M_InOutLine_ID";
		if( forInvoice )
			column = "m.C_InvoiceLine_ID";

		
		
		StringBuffer sql1 = new StringBuffer( "SELECT o.C_Order_ID," ).append( display ).append(
				" FROM C_Order o " + "WHERE " +
						"o.C_BPartner_ID=? AND o.IsSOTrx='N' AND o.DocStatus IN ('CL','CO') "
						+ "AND o.IsReturnTrx='" + (isReturnTrx?"Y":"N") + "' AND o.C_Order_ID IN "
						+ "(SELECT ol.C_Order_ID FROM C_OrderLine ol"
						+ " LEFT OUTER JOIN M_MatchPO m ON (ol.C_OrderLine_ID=m.C_OrderLine_ID) "
						+ " LEFT OUTER JOIN C_Order z ON (z.C_Order_ID = ol.C_Order_ID) WHERE  z.ISSOTRx='N' "
						+ "GROUP BY ol.C_Order_ID,ol.C_OrderLine_ID, ol.QtyOrdered," ).append( column ).append(
				" HAVING (ol.QtyOrdered <> SUM(m.Qty) AND " ).append( column ).append( " IS NOT NULL) OR " ).append( column )
				.append( " IS NULL) " + "ORDER BY o.DateOrdered" );
		
		MRole role = MRole.get( ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false );
		String sql = role.addAccessSQL( sql1.toString(), "C_Order", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO );
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement( sql.toString(), (Trx) null);
			pstmt.setInt( 1, C_BPartner_ID );
			rs = pstmt.executeQuery();
			while( rs.next() )
			{
				pairs.add( new KeyNamePair( rs.getInt( 1 ), rs.getString( 2 ) ) );
			}
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
	 *  Load PBartner dependent Order/Invoice/Shipment Field.
	 *  @param C_BPartner_ID BPartner
	 *  @param forInvoice for invoice
	 */
	protected void initBPartnerOIS (int C_BPartner_ID, boolean forInvoice)
	{
		log.config("C_BPartner_ID=" + C_BPartner_ID);
		KeyNamePair pp = new KeyNamePair(0,"");

		//  load PO Orders - Closed, Completed
		orderField.removeActionListener(this);
		orderField.removeAllItems();
		orderField.addItem(pp);
		//
		boolean isReturnTrx = "Y".equals( Env.getCtx().getContext( p_WindowNo, "IsReturnTrx") );
		
		ArrayList< NamePair > orders = getOrders( Env.getCtx(), C_BPartner_ID, isReturnTrx, forInvoice, p_WindowNo);
		for( NamePair pair : orders )
		{
			orderField.addItem( pair );
		}

		orderField.setSelectedIndex(0);
		orderField.addActionListener(this);

		// Non useful Field for us - Patricia Ayuso
		//initBPDetails(C_BPartner_ID);
	}   //  initBPartnerOIS

	/**
	 *  Load Data - Order
	 *  @param C_Order_ID Order
	 *  @param forInvoice true if for invoice vs. delivery qty
	 */
	protected void loadOrder (int C_Order_ID, boolean forInvoice)
	{
		p_order = new MOrder (Env.getCtx(), C_Order_ID, null);      //  save
		Vector<Vector< Object >> data = getOrderData( Env.getCtx(), C_Order_ID, forInvoice );
		loadTableOIS (data);
	}
	
	
	public static Vector< Vector< Object > > getOrderData( Ctx ctx, int C_Order_ID, boolean forInvoice)
	{
		/**
		 *  Selected        - 0
		 *  Qty             - 1
		 *  C_UOM_ID        - 2
		 *  M_Product_ID    - 3
		 *  OrderLine       - 4
		 *  ShipmentLine    - 5
		 *  InvoiceLine     - 6
		 */
		s_log.config("C_Order_ID=" + C_Order_ID);

		Vector<Vector< Object >> data = new Vector<Vector< Object >>();
		StringBuffer sql = new StringBuffer("SELECT "
			+ "l.QtyOrdered-SUM(COALESCE(m.Qty,0)),"					//	1
			+ "CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END,"	//	2
			+ " l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),"			//	3..4
			+ " COALESCE(l.M_Product_ID,0),COALESCE(p.Name,c.Name),"	//	5..6
			+ " l.C_OrderLine_ID,l.Line "								//	7..8
			+ ", p.Value, p.XX_VMR_VendorProdRef_ID, p.M_ATTRIBUTESETINSTANCE_ID "					//  9..10..11 Modificado por WDiaz
			+ "FROM C_OrderLine l"
			+ " LEFT OUTER JOIN M_MatchPO m ON (l.C_OrderLine_ID=m.C_OrderLine_ID AND ");
		sql.append(forInvoice ? "m.C_InvoiceLine_ID" : "m.M_InOutLine_ID");
		sql.append(" IS NOT NULL)")
			.append(" LEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID)"
			+ " LEFT OUTER JOIN C_Charge c ON (l.C_Charge_ID=c.C_Charge_ID)");
		if (Env.isBaseLanguage( ctx, "C_UOM"))
			sql.append(" LEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID)");
		else
			sql.append(" LEFT OUTER JOIN C_UOM_Trl uom ON (l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='")
				.append( Env.getAD_Language( ctx ) ).append( "')" );
		//
		sql.append(" WHERE l.C_Order_ID=? AND l.C_OrderLine_ID NOT IN (SELECT ilAux.C_OrderLine_ID FROM C_InvoiceLine ilAux, C_Invoice iAux  WHERE ilAux.C_Invoice_ID = iAux.C_Invoice_ID and  iAux.C_ORDER_ID = l.C_Order_ID and C_OrderLine_ID is not null )"			//	#1
			+ "GROUP BY l.QtyOrdered,CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, "
			+ "l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name), "
				+ "l.M_Product_ID,COALESCE(p.Name,c.Name), l.Line,l.C_OrderLine_ID, p.Value, p.XX_VMR_VendorProdRef_ID, p.M_ATTRIBUTESETINSTANCE_ID " +
				  " HAVING l.QtyOrdered-SUM(COALESCE(m.Qty,0)) <> 0 "
			//+ " ORDER BY l.Line");
				+ " ORDER BY p.XX_VMR_VendorProdRef_ID");
		//
		s_log.finer(sql.toString());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			
			MAttributeSetInstance atributo = null;
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt(1, C_Order_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{

				
				atributo = new MAttributeSetInstance(ctx, rs.getInt(11), null);
				Vector<Object> line = new Vector<Object>();
				line.add(Boolean.valueOf(false));           //  0-Selection
				BigDecimal qtyOrdered = rs.getBigDecimal(1);
				BigDecimal multiplier = rs.getBigDecimal(2);
				BigDecimal qtyEntered = qtyOrdered.multiply(multiplier);
				line.add(new Double(qtyEntered.doubleValue()));  //  1-Qty
				KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(4).trim());
				line.add(pp);                           //  2-UOM
				pp = new KeyNamePair(rs.getInt(5), rs.getString(6));
				line.add(pp);                           //  3-Product
				pp = new KeyNamePair(rs.getInt(7), rs.getString(8));
				line.add(pp);                           //  4-OrderLine
				line.add(rs.getString(9));				// Value del Producto WDiaz	//5

				if (rs.getInt(10) == 0){ //Modificado por Jessica Mendoza
					line.add(null);	
				}else{
					line.add(pp);
				}
								// Referencia WDiaz
				if (atributo.getM_AttributeSetInstance_ID() > 0){
					pp = new KeyNamePair(rs.getInt(11), (atributo != null)? atributo.getDescription().trim() : null); //WDiaz // 6.5
					line.add(pp);				// Attribute WDiaz
				}else
					line.add(null);				// Attribute WDiaz
				line.add(null);                         //  5-Ship      ahora 7
				line.add(null);                         //  6-Invoice	ahora 8
				data.add(line);
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return data;
	}   //  LoadOrder


	
	
	public static ArrayList< FieldVO > getTableFieldVOs( Ctx ctx )
	{
		ArrayList< FieldVO > fieldVOs = new ArrayList< FieldVO >();
		fieldVOs.add( new FieldVO( "Quantity", Msg.translate( ctx, "Quantity"), DisplayTypeConstants.Quantity ) );
		fieldVOs.add( new FieldVO( "C_UOM_ID", Msg.translate( ctx, "C_UOM_ID"), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "M_Product_ID", Msg.translate( ctx, "M_Product_ID"), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "C_Order_ID", Msg.getElement( ctx, "C_Order_ID", false), DisplayTypeConstants.String ));

		fieldVOs.add( new FieldVO( "Value", Msg.translate( ctx, "ProductKey"), DisplayTypeConstants.String )); //WDiaz
		fieldVOs.add( new FieldVO( "XX_VMR_VendorProdRef_ID", Msg.translate( ctx, "XX_RefVendor"), DisplayTypeConstants.String )); //WDiaz
		fieldVOs.add( new FieldVO( "M_AttributeSetInstance_ID", Msg.translate( ctx, "M_AttributeSetInstance_ID"), DisplayTypeConstants.String )); //WDiaz
		fieldVOs.add( new FieldVO( "M_InOut_ID", Msg.getElement( ctx, "M_InOut_ID", false), DisplayTypeConstants.String ));
		fieldVOs.add( new FieldVO( "C_Invoice_ID", Msg.getElement( ctx, "C_Invoice_ID", false), DisplayTypeConstants.String ));
		
		for( FieldVO f : fieldVOs )
			f.IsReadOnly = true;
		
		for( int i = 2; i < fieldVOs.size(); ++i )
			fieldVOs.get( i ).IsKey = true;
		
		return fieldVOs;
	}
	
	
	/**
	 *  Load Order/Invoice/Shipment data into Table
	 *  @param data data
	 */
	protected void loadTableOIS (Vector<Vector<Object>> data)
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(7);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		for( FieldVO vo : getTableFieldVOs( Env.getCtx() ) )
		{
			columnNames.add( vo.name );
		}

		//  Remove previous listeners
		dataTable.getModel().removeTableModelListener(this);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		model.addTableModelListener(this);
		dataTable.setModel(model);
		//
		dataTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		dataTable.setColumnClass(1, Double.class, true);        //  1-Qty
		dataTable.setColumnClass(2, String.class, true);        //  2-UOM
		dataTable.setColumnClass(3, String.class, true);        //  3-Product
		dataTable.setColumnClass(4, String.class, true);        //  4-Order
		dataTable.setColumnClass(8, String.class, true);        //  5-Ship		ahora 8  antes 5
		dataTable.setColumnClass(9, String.class, true);        //  6-Invoice   ahora 9  antes 6
		
		dataTable.setColumnClass(5, String.class, true);        //  7-Codigo Producto (Value) WDiaz	
		dataTable.setColumnClass(6, String.class, true);        //  8-Referencia del Product WDiaz
		dataTable.setColumnClass(7, String.class, true);
		//  Table UI
		dataTable.autoSize();
		
		// ensure select all button is only available when there is data
		selectAllAction.setEnabled(dataTable.getRowCount() > 0);
	}   //  loadOrder

}   //  VCreateFrom
