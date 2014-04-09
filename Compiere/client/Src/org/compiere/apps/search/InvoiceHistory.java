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
package org.compiere.apps.search;

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
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.vos.*;

import com.compiere.client.*;

/**
 *	Price History for BPartner/Product
 *
 *  @author Jorg Janke
 *  @version  $Id: InvoiceHistory.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class InvoiceHistory extends CDialog
	implements ActionListener, ChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Show History
	 *	@param C_BPartner_ID partner
	 *	@param M_Product_ID product
	 *	@param M_Warehouse_ID warehouse
	 *	@param M_AttributeSetInstance_ID ASI
	 */
	public InvoiceHistory (Dialog frame, 
		int C_BPartner_ID, int M_Product_ID, int M_Warehouse_ID, int M_AttributeSetInstance_ID)
	{
		super(frame, Msg.getMsg(Env.getCtx(), "PriceHistory"), true);
		log.config("C_BPartner_ID=" + C_BPartner_ID
			+ ", M_Product_ID=" + M_Product_ID
			+ ", M_Warehouse_ID=" + M_Warehouse_ID
			+ ", M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID);
		m_C_BPartner_ID = C_BPartner_ID;
		m_M_Product_ID = M_Product_ID;
		m_M_Warehouse_ID = M_Warehouse_ID;
		m_M_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
		SysEnv se = SysEnv.get("CMFG", false);
        if (!(se==null) && se.checkLicense())
        {
        	m_CMFGLicensed = true;
        }

		try
		{
			jbInit();
			dynInit();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		}
		mainPanel.setPreferredSize(new Dimension(700,400));
		AEnv.positionCenterWindow(frame, this);
	}	//	InvoiceHistory

	private int		m_C_BPartner_ID;
	private int		m_M_Product_ID;
	private int		m_M_Warehouse_ID;
	private int		m_M_AttributeSetInstance_ID;
	private boolean m_CMFGLicensed = false;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(InvoiceHistory.class);

	private CPanel 			mainPanel = new CPanel();
	private BorderLayout 	mainLayout = new BorderLayout();
	private CPanel 			northPanel = new CPanel();
	private JLabel 			label = new JLabel();
	private FlowLayout 		northLayout = new FlowLayout();
	//
	private ConfirmPanel 	confirmPanel = new ConfirmPanel();
	private JTabbedPane 	centerTabbedPane = new JTabbedPane();
	//
	private JScrollPane 		pricePane = new JScrollPane();
	private MiniTable 			m_tablePrice = new MiniTable();
	private DefaultTableModel 	m_modelPrice = null;
	
	private JScrollPane 		reservedPane = new JScrollPane();
	private MiniTable 			m_tableReserved = new MiniTable();
	private DefaultTableModel 	m_modelReserved = null;
	
	private JScrollPane 		orderedPane = new JScrollPane();
	private MiniTable 			m_tableOrdered = new MiniTable();
	private DefaultTableModel 	m_modelOrdered = null;
	
	private JScrollPane 		wipPane = new JScrollPane();
	private MiniTable 			m_tableWIP = new MiniTable();
	private DefaultTableModel 	m_modelWIP = null;

	private JScrollPane 		unconfirmedPane = new JScrollPane();
	private MiniTable 			m_tableUnconfirmed = new MiniTable();
	private DefaultTableModel 	m_modelUnconfirmed = null;

	private JScrollPane 		atpPane = new JScrollPane();
	private MiniTable 			m_tableAtp = new MiniTable();
	private DefaultTableModel 	m_modelAtp = null;
	
	/**
	 *	Ststic Init
	 */
	void jbInit() throws Exception
	{
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainPanel.setLayout(mainLayout);
		label.setText("Label");
		northPanel.setLayout(northLayout);
		northLayout.setAlignment(FlowLayout.LEFT);
		getContentPane().add(mainPanel);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		northPanel.add(label, null);
		mainPanel.add(confirmPanel, BorderLayout.SOUTH);
		mainPanel.add(centerTabbedPane, BorderLayout.CENTER);
		centerTabbedPane.addChangeListener(this);
		centerTabbedPane.add(pricePane,   Msg.getMsg(Env.getCtx(), "PriceHistory"));
		centerTabbedPane.add(reservedPane, Msg.translate(Env.getCtx(), "QtyReserved"));
		centerTabbedPane.add(orderedPane, Msg.translate(Env.getCtx(), "QtyOrdered"));
		if (m_CMFGLicensed)
			centerTabbedPane.add(wipPane, Msg.translate(Env.getCtx(), "QtyWIP"));
		centerTabbedPane.add(unconfirmedPane, Msg.getMsg(Env.getCtx(), "QtyUnconfirmed"));
		if (m_M_Product_ID != 0)
			centerTabbedPane.add(atpPane, Msg.getMsg(Env.getCtx(), "ATP"));
		//
		pricePane.getViewport().add(m_tablePrice, null);
		reservedPane.getViewport().add(m_tableReserved, null);
		orderedPane.getViewport().add(m_tableOrdered, null);
		if (m_CMFGLicensed)
			wipPane.getViewport().add(m_tableWIP, null);
		unconfirmedPane.getViewport().add(m_tableUnconfirmed, null);
		if (m_M_Product_ID != 0)
			atpPane.getViewport().add(m_tableAtp, null);
		//
		confirmPanel.addActionListener(this);
	}	//	jbInit


	private void dynInit()
	{
		initPriceTab();
	}	
	
	
	/**
	 * 
	 * @param ctx
	 * @param isProduct If True, called from Product info window.  If False, called from Business Partner info window 
	 * @return
	 */
	public static ArrayList< FieldVO > getPriceTabFieldVOs( Ctx ctx, boolean isProduct )
	{
		ArrayList< FieldVO > fieldVOs = new ArrayList< FieldVO >();
		if( isProduct )
			fieldVOs.add( new FieldVO( "M_Product_ID", Msg.translate( ctx, "M_Product_ID" ), DisplayTypeConstants.String ) );
		else
			fieldVOs.add( new FieldVO( "C_BPartner_ID", Msg.translate( ctx, "C_BPartner_ID" ), DisplayTypeConstants.String ) );

		fieldVOs.add( new FieldVO( "PriceActual", Msg.translate( ctx, "PriceActual" ), DisplayTypeConstants.Amount ) );
		fieldVOs.add( new FieldVO( "QtyInvoiced", Msg.translate( ctx, "QtyInvoiced" ), DisplayTypeConstants.Quantity ) );
		fieldVOs.add( new FieldVO( "Discount", Msg.translate( ctx, "Discount" ), DisplayTypeConstants.Amount ) );
		fieldVOs.add( new FieldVO( "DocumentNo", Msg.translate( ctx, "DocumentNo" ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "DateInvoiced", Msg.translate( ctx, "DateInvoiced" ), DisplayTypeConstants.Date ) );
		fieldVOs.add( new FieldVO( "AD_Org_ID", Msg.getElement( ctx, "AD_Org_ID", false ), DisplayTypeConstants.String ) );
		
		for( FieldVO f : fieldVOs )
			f.IsReadOnly = true;
		
		for( int i = 2; i < fieldVOs.size(); ++i )
			fieldVOs.get( i ).IsKey = true;
		
		return fieldVOs;
	}
	
	
	/**
	 *	Dynamic Init for Price Tab
	 */
	private void initPriceTab()
	{
		//	Header
		Vector<String> columnNames = new Vector<String>();
		for( FieldVO vo : getPriceTabFieldVOs( Env.getCtx(), m_C_BPartner_ID != 0 ) )
		{
			columnNames.add( vo.name );
		}

		//	Fill Data
		Vector<Vector<Object>> data = null;
		if (m_C_BPartner_ID == 0)
		{
			data = queryBPartner( m_M_Product_ID );		//	BPartner of Product
			String sql = "SELECT Name from M_Product WHERE M_Product_ID=?";
			fillLabel (sql, m_M_Product_ID);
		}
		else
		{
			data = queryProduct( m_C_BPartner_ID );		//	Product of BPartner
			String sql = "SELECT Name from C_BPartner WHERE C_BPartner_ID=?";
			fillLabel (sql, m_C_BPartner_ID);
		}

		//  Table
		m_modelPrice = new DefaultTableModel(data, columnNames);
		m_tablePrice.setModel(m_modelPrice);
		//
		m_tablePrice.setColumnClass(0, String.class, true);      //  Product/Partner
		m_tablePrice.setColumnClass(1, Double.class, true);  	 //  Price
		m_tablePrice.setColumnClass(2, Double.class, true);      //  Quantity
		m_tablePrice.setColumnClass(3, BigDecimal.class, true);  //  Discount (%) to limit precision
		m_tablePrice.setColumnClass(4, String.class, true);      //  DocNo
		m_tablePrice.setColumnClass(5, Timestamp.class, true);   //  Date
		m_tablePrice.setColumnClass(6, String.class, true);   	 //  Org
		//
		m_tablePrice.autoSize();
		//
	}	


	/**
	 *	Get Info for Product for given Business Parner
	 */
	public static Vector<Vector<Object>> queryProduct ( int m_C_BPartner_ID )
	{
		String sql = "SELECT p.Name,l.PriceActual,l.PriceList,l.QtyInvoiced,"
			+ "i.DateInvoiced,dt.PrintName || ' ' || i.DocumentNo As DocumentNo,"
			+ "o.Name "
			+ "FROM C_Invoice i"
			+ " INNER JOIN C_InvoiceLine l ON (i.C_Invoice_ID=l.C_Invoice_ID)"
			+ " INNER JOIN C_DocType dt ON (i.C_DocType_ID=dt.C_DocType_ID)"
			+ " INNER JOIN AD_Org o ON (i.AD_Org_ID=o.AD_Org_ID)"
			+ " INNER JOIN M_Product p  ON (l.M_Product_ID=p.M_Product_ID) "
			+ "WHERE i.C_BPartner_ID=? "
			+ "ORDER BY i.DateInvoiced DESC";

		Vector<Vector<Object>> data = fillTable (sql, m_C_BPartner_ID);

		return data;
	}   //  queryProduct

	/**
	 *	Get Info for Business Partners for given Product
	 */
	public static Vector<Vector<Object>> queryBPartner ( int m_M_Product_ID )
	{
		String sql = "SELECT bp.Name,l.PriceActual,l.PriceList,l.QtyInvoiced,"		//	1,2,3,4
			+ "i.DateInvoiced,dt.PrintName || ' ' || i.DocumentNo As DocumentNo,"	//	5,6
			+ "o.Name "
			+ "FROM C_Invoice i"
			+ " INNER JOIN C_InvoiceLine l ON (i.C_Invoice_ID=l.C_Invoice_ID)"
			+ " INNER JOIN C_DocType dt ON (i.C_DocType_ID=dt.C_DocType_ID)"
			+ " INNER JOIN AD_Org o ON (i.AD_Org_ID=o.AD_Org_ID)"
			+ " INNER JOIN C_BPartner bp ON (i.C_BPartner_ID=bp.C_BPartner_ID) "
			+ "WHERE l.M_Product_ID=? " 
			+ "ORDER BY i.DateInvoiced DESC";

		Vector<Vector<Object>> data = fillTable (sql, m_M_Product_ID);

		return data;
	}	//	queryBPartner

	/**
	 *	Fill Table
	 */
	private static Vector<Vector<Object>> fillTable (String sql, int parameter)
	{
		return fillTable(sql, parameter, 0);
	}
	
	/**
	 *	Fill Table
	 */
	private static Vector<Vector<Object>> fillTable (String sql, int parameter, int parameter2)
	{
		log.fine(sql + "; Parameter=" + parameter);
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, parameter);
			
			if(parameter2 !=0)
				pstmt.setInt(2, parameter2);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(6);
				//	0-Name, 1-PriceActual, 2-QtyInvoiced, 3-Discount, 4-DocumentNo, 5-DateInvoiced
				line.add(rs.getString(1));      //  Name
				line.add(rs.getBigDecimal(2));  //	Price
				line.add(new Double(rs.getDouble(4)));      //  Qty
				BigDecimal discountBD = Env.ZERO;
				try //  discoint can be indefinate
				{
					double discountD = (rs.getDouble(3)-rs.getDouble(2)) / rs.getDouble(3) * 100;
					discountBD = new BigDecimal(discountD);
				}
				catch (Exception e)
				{
					discountBD = Env.ZERO;
				}
				line.add(discountBD);           //  Discount
				line.add(rs.getString(6));      //  DocNo
				line.add(rs.getTimestamp(5));   //  Date
				line.add(rs.getString(7));		//	Org/Warehouse
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		log.fine("#" + data.size());
		return data;
	}	//	fillTable

	/**
	 *	Set Label
	 *  to product or bp name
	 */
	private void fillLabel (String sql, int parameter)
	{
		log.fine(sql + "; Parameter=" + parameter);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, parameter);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				label.setText(rs.getString(1));
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}	//	fillLabel


	/**
	 *	Action Listener
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
			dispose();
	}	//	actionPerformed


	/**
	 * 	Tab Changed
	 * 	@param e event
	 */
	public void stateChanged(ChangeEvent e)
	{
		int selectedIndex = centerTabbedPane.getSelectedIndex();
		if (!m_CMFGLicensed)
		{
			if (selectedIndex >= 3)
				selectedIndex++;
		}
			
		if (selectedIndex == 1)
			initReservedOrderedTab(true);
		else if (selectedIndex == 2)
			initReservedOrderedTab(false);
		else if (selectedIndex == 3)
			initWIPTab();
		else if (selectedIndex == 4)	
			initUnconfirmedTab();
		else if (selectedIndex == 5)
			initAtpTab();
	}	//	stateChanged


	
	
	/**
	 * 
	 * @param ctx
	 * @param isProduct If True, called from Product info window.  If False, called from Business Partner info window 
	 * @return
	 */
	public static ArrayList< FieldVO > getReservedOrderedTabFieldVOs( Ctx ctx, boolean isProduct, boolean reserved )
	{
		ArrayList< FieldVO > fieldVOs = new ArrayList< FieldVO >();
		if( isProduct )
			fieldVOs.add( new FieldVO( "M_Product_ID", Msg.translate( ctx, "M_Product_ID" ), DisplayTypeConstants.String ) );
		else
			fieldVOs.add( new FieldVO( "C_BPartner_ID", Msg.translate( ctx, "C_BPartner_ID" ), DisplayTypeConstants.String ) );

		fieldVOs.add( new FieldVO( "PriceActual", Msg.translate( ctx, "PriceActual" ), DisplayTypeConstants.Amount ) );
		
		if( reserved )
			fieldVOs.add( new FieldVO( "QtyReserved", Msg.translate( ctx, "QtyReserved" ), DisplayTypeConstants.Quantity ) );
		else
			fieldVOs.add( new FieldVO( "QtyOrdered", Msg.translate( ctx, "QtyOrdered" ), DisplayTypeConstants.Quantity ) );
		
		fieldVOs.add( new FieldVO( "Discount", Msg.translate( ctx, "Discount" ), DisplayTypeConstants.Amount ) );
		fieldVOs.add( new FieldVO( "DocumentNo", Msg.translate( ctx, "DocumentNo" ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "DateOrdered", Msg.translate( ctx, "DateOrdered" ), DisplayTypeConstants.Date ) );
		fieldVOs.add( new FieldVO( "M_Warehouse_ID", Msg.getElement( ctx, "M_Warehouse_ID", false ), DisplayTypeConstants.String ) );
		
		for( FieldVO f : fieldVOs )
			f.IsReadOnly = true;
		
		for( int i = 2; i < fieldVOs.size(); ++i )
			fieldVOs.get( i ).IsKey = true;
		
		return fieldVOs;
	}

	public static Vector<Vector<Object>> queryReservedOrderBPartner( int m_M_Product_ID, boolean reserved,
																			int m_M_Warehouse_ID)
	{
		String sql = "SELECT bp.Name, ol.PriceActual,ol.PriceList,ol.QtyReserved,"
			+ "o.DateOrdered,dt.PrintName || ' ' || o.DocumentNo As DocumentNo, "
			+ "w.Name "
			+ "FROM C_Order o"
			+ " INNER JOIN C_OrderLine ol ON (o.C_Order_ID=ol.C_Order_ID)"
			+ " INNER JOIN C_DocType dt ON (o.C_DocType_ID=dt.C_DocType_ID)"
			+ " INNER JOIN M_Warehouse w ON (ol.M_Warehouse_ID=w.M_Warehouse_ID)"
			+ " INNER JOIN C_BPartner bp  ON (o.C_BPartner_ID=bp.C_BPartner_ID) "
			+ "WHERE ol.QtyReserved<>0"
			+ " AND ol.M_Product_ID=?"
			+ " AND o.IsSOTrx=" + (reserved ? "'Y'" : "'N'");
		
		if (m_M_Warehouse_ID != 0)
			sql += " AND ol.M_Warehouse_ID=?";

				
			
		sql += " ORDER BY o.DateOrdered";
		return fillTable (sql, m_M_Product_ID, m_M_Warehouse_ID);	//	Product By BPartner
	}

	
	public static Vector<Vector<Object>> queryReservedOrderProduct( int m_C_BPartner_ID, boolean reserved, int m_M_Warehouse_ID )
	{
		String sql = "SELECT p.Name, ol.PriceActual,ol.PriceList,ol.QtyReserved,"
			+ "o.DateOrdered,dt.PrintName || ' ' || o.DocumentNo As DocumentNo, " 
			+ "w.Name "
			+ "FROM C_Order o"
			+ " INNER JOIN C_OrderLine ol ON (o.C_Order_ID=ol.C_Order_ID)"
			+ " INNER JOIN C_DocType dt ON (o.C_DocType_ID=dt.C_DocType_ID)"
			+ " INNER JOIN M_Warehouse w ON (ol.M_Warehouse_ID=w.M_Warehouse_ID)"
			+ " INNER JOIN M_Product p  ON (ol.M_Product_ID=p.M_Product_ID) "
			+ "WHERE ol.QtyReserved<>0"
			+ " AND o.C_BPartner_ID=?"
			+ " AND o.IsSOTrx=" + (reserved ? "'Y'" : "'N'");
		
		if (m_M_Warehouse_ID != 0)
			sql += " AND ol.M_Warehouse_ID=?";
			
		sql += " ORDER BY o.DateOrdered";
		return fillTable (sql, m_C_BPartner_ID, m_M_Warehouse_ID);//	Product of BP
	}	
	
	/**
	 *	Query Reserved/Ordered
	 *	@param reserved po/so
	 */
	private void initReservedOrderedTab (boolean reserved)
	{
		//	Done already
		if (reserved && m_modelReserved != null)
			return;
		if (!reserved && m_modelOrdered != null)
			return;
			
		//	Header
		Vector<String> columnNames = new Vector<String>();
		for( FieldVO vo : getReservedOrderedTabFieldVOs( Env.getCtx(), m_C_BPartner_ID != 0, reserved ) )
		{
			columnNames.add( vo.name );
		}

		//	Fill Data
		Vector<Vector<Object>> data = null;
		if (m_C_BPartner_ID == 0)
		{
			data = queryReservedOrderBPartner( m_M_Product_ID, reserved, m_M_Warehouse_ID );
		}
		else
		{
			data = queryReservedOrderProduct( m_C_BPartner_ID, reserved, m_M_Warehouse_ID );
		}

		//  Table
		MiniTable table = null;
		if (reserved)
		{
			m_modelReserved = new DefaultTableModel(data, columnNames); 
			m_tableReserved.setModel(m_modelReserved);
			table = m_tableReserved;
		}
		else
		{
			m_modelOrdered = new DefaultTableModel(data, columnNames); 
			m_tableOrdered.setModel(m_modelOrdered);
			table = m_tableOrdered;
		}
		//
		table.setColumnClass(0, String.class, true);      //  Product/Partner
		table.setColumnClass(1, BigDecimal.class, true);  //  Price
		table.setColumnClass(2, Double.class, true);      //  Quantity
		table.setColumnClass(3, BigDecimal.class, true);  //  Discount (%)
		table.setColumnClass(4, String.class, true);      //  DocNo
		table.setColumnClass(5, Timestamp.class, true);   //  Date
		table.setColumnClass(6, String.class, true);   	  //  Warehouse
		//
		table.autoSize();
	}	//	initReservedOrderedTab

	
	/**
	 * 
	 * @param ctx
	 * @param isProduct If True, called from Product info window.  If False, called from Business Partner info window 
	 * @return
	 */
	public static ArrayList< FieldVO > getWIPTabFieldVOs( Ctx ctx, boolean isProduct )
	{
		ArrayList< FieldVO > fieldVOs = new ArrayList< FieldVO >();
		if( isProduct )
			fieldVOs.add( new FieldVO( "M_Product_ID", Msg.translate( ctx, "M_Product_ID" ), DisplayTypeConstants.String ) );
		else
			fieldVOs.add( new FieldVO( "C_BPartner_ID", Msg.translate( ctx, "C_BPartner_ID" ), DisplayTypeConstants.String ) );

		fieldVOs.add( new FieldVO( "Qty", Msg.translate( ctx, "Qty" ), DisplayTypeConstants.Quantity ) );
		fieldVOs.add( new FieldVO( "DocumentNo", Msg.translate( ctx, "DocumentNo" ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "QtyAssembled", Msg.translate( ctx, "QtyAssembled" ), DisplayTypeConstants.Quantity ) );
		fieldVOs.add( new FieldVO( "QtyScrapped", Msg.translate( ctx, "QtyScrapped" ), DisplayTypeConstants.Quantity ) );
		
		fieldVOs.add( new FieldVO( "DateScheduleFrom", Msg.translate( ctx, "DateScheduleFrom" ), DisplayTypeConstants.Date ) );
		fieldVOs.add( new FieldVO( "DateScheduleTo", Msg.translate( ctx, "DateScheduleTo" ), DisplayTypeConstants.Date ) );
		fieldVOs.add( new FieldVO( "M_Warehouse_ID", Msg.getElement( ctx, "M_Warehouse_ID", false ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "WOType", Msg.getElement( ctx, "WOType", false ), DisplayTypeConstants.String ) );
		
		for( FieldVO f : fieldVOs )
			f.IsReadOnly = true;
		
		for( int i = 2; i < fieldVOs.size(); ++i )
			fieldVOs.get( i ).IsKey = true;
		
		return fieldVOs;
	}


	public static Vector<Vector<Object>> queryWIP( int m_M_Product_ID, int m_C_BPartner_ID, int m_M_Warehouse_ID)
	{
		//	Fill Data
		String sql = null;
		int parameter = 0;
		if (m_C_BPartner_ID == 0)	
		{
			sql = "SELECT COALESCE(bp.Name, NULL), wo.QtyEntered, "
				+ "dt.PrintName || ' ' || wo.DocumentNo As DocumentNo, "
				+ "wo.QtyAssembled, wo.QtyScrapped, wo.DateScheduleFrom, wo.DateScheduleTo, w.Name, rl.Name "
				+ "FROM M_WorkOrder wo"
				+ " INNER JOIN AD_Ref_List rl ON (wo.WOType = rl.Value and rl.AD_Reference_ID = 450) "
				+ " INNER JOIN C_DocType dt ON (wo.C_DocType_ID=dt.C_DocType_ID)"
				+ " INNER JOIN M_Warehouse w ON (wo.M_Warehouse_ID=w.M_Warehouse_ID)"
				+ " LEFT OUTER JOIN C_BPartner bp  ON (wo.C_BPartner_ID=bp.C_BPartner_ID) "
				+ "WHERE wo.docstatus = 'IP'"
				+ " AND wo.M_Product_ID=?";

			if (m_M_Warehouse_ID != 0)
				sql += " AND wo.M_Warehouse_ID=?";

			sql += " ORDER BY wo.DateScheduleFrom, wo.DocumentNo";
			parameter = m_M_Product_ID;
		}
		else
		{
			sql = "SELECT p.Name, wo.QtyEntered, "
				+ "dt.PrintName || ' ' || wo.DocumentNo As DocumentNo, "
				+ "wo.QtyAssembled, wo.QtyScrapped, wo.DateScheduleFrom, wo.DateScheduleTo, w.Name, rl.Name "
				+ "FROM M_WorkOrder wo"
				+ " INNER JOIN AD_Ref_List rl ON (wo.WOType = rl.Value and rl.AD_Reference_ID = 450) "
				+ " INNER JOIN C_DocType dt ON (wo.C_DocType_ID=dt.C_DocType_ID)"
				+ " INNER JOIN M_Warehouse w ON (wo.M_Warehouse_ID=w.M_Warehouse_ID)"
				+ " INNER JOIN M_Product p  ON (wo.M_Product_ID=p.M_Product_ID) "
				+ "WHERE wo.docstatus = 'IP'"
				+ " AND wo.C_BPartner_ID=?";

			if (m_M_Warehouse_ID != 0)
				sql += " AND wo.M_Warehouse_ID=?";

			sql += " ORDER BY wo.DateScheduleFrom, wo.DocumentNo";

			parameter = m_C_BPartner_ID;
		}

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, parameter);
			if (m_M_Warehouse_ID != 0)
				pstmt.setInt(2, m_M_Warehouse_ID);
		
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(6);
				//	1-Name, 2-Qty, 3-DocumentNo, 4-QtyAssembled, 5-QtyScrapped, 6-ScheduleDateFrom, 7-ScheduleDateTo, 8-Warehouse
				line.add(rs.getString(1));      		//  Name
				line.add(new Double(rs.getDouble(2)));  //  Qty
				line.add(rs.getString(3));				//  DocNo
				line.add(new Double(rs.getDouble(4)));  //  Qty
				line.add(new Double(rs.getDouble(5)));  //  Qty
				line.add(rs.getTimestamp(6));   		//  Date
				line.add(rs.getTimestamp(7));   		//  Date
				line.add(rs.getString(8));				//  Warehouse
				line.add(rs.getString(9));				//  WO Type			
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		log.fine("#" + data.size());
		return data;
	}

	
	/**
	 *	Query WIP
	 */
	private void initWIPTab ()
	{
		//	Done already
		if (m_modelWIP != null)
			return;
			
		//	Header
		Vector<String> columnNames = new Vector<String>();
		for( FieldVO vo : getWIPTabFieldVOs( Env.getCtx(), m_C_BPartner_ID != 0 ) )
		{
			columnNames.add( vo.name );
		}


		Vector<Vector<Object>> data = queryWIP( m_M_Product_ID, m_C_BPartner_ID, m_M_Warehouse_ID );
		//  Table
		m_modelWIP = new DefaultTableModel(data, columnNames); 
		m_tableWIP.setModel(m_modelWIP);
		MiniTable table = m_tableWIP;
		//
		table.setColumnClass(0, String.class, true);      //  Product/Partner
		table.setColumnClass(1, Double.class, true);  	  //  Qty
		table.setColumnClass(2, String.class, true);      //  DocNo
		table.setColumnClass(3, Double.class, true);  	  //  QtyAssembled
		table.setColumnClass(4, Double.class, true);  	  //  QtyScrapped
		table.setColumnClass(5, Timestamp.class, true);   //  DateScheduleFrom
		table.setColumnClass(6, Timestamp.class, true);   //  DateScheduleTo
		table.setColumnClass(7, String.class, true);   	  //  Warehouse
		table.setColumnClass(8, String.class, true);   	  //  WO Type
		//
		table.autoSize();
	}	//	initUnconfirmedTab

	
	/**
	 * 
	 * @param ctx
	 * @param isProduct If True, called from Product info window.  If False, called from Business Partner info window 
	 * @return
	 */
	public static ArrayList< FieldVO > getUnconfirmedTabFieldVOs( Ctx ctx, boolean isProduct )
	{
		ArrayList< FieldVO > fieldVOs = new ArrayList< FieldVO >();
		if( isProduct )
			fieldVOs.add( new FieldVO( "M_Product_ID", Msg.translate( ctx, "M_Product_ID" ), DisplayTypeConstants.String ) );
		else
			fieldVOs.add( new FieldVO( "C_BPartner_ID", Msg.translate( ctx, "C_BPartner_ID" ), DisplayTypeConstants.String ) );

		fieldVOs.add( new FieldVO( "MovementQty", Msg.translate( ctx, "MovementQty" ), DisplayTypeConstants.Quantity ) );
		fieldVOs.add( new FieldVO( "MovementDate", Msg.translate( ctx, "MovementDate" ), DisplayTypeConstants.Date ) );
		fieldVOs.add( new FieldVO( "IsSOTrx", Msg.translate( ctx, "IsSOTrx" ), DisplayTypeConstants.YesNo ) );
		fieldVOs.add( new FieldVO( "DocumentNo", Msg.translate( ctx, "DocumentNo" ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "M_Warehouse_ID", Msg.getElement( ctx, "M_Warehouse_ID", false ), DisplayTypeConstants.String ) );
		
		for( FieldVO f : fieldVOs )
			f.IsReadOnly = true;
		
		for( int i = 2; i < fieldVOs.size(); ++i )
			fieldVOs.get( i ).IsKey = true;
		
		return fieldVOs;
	}
	
	public static Vector<Vector<Object>> queryUnconfirmed( int m_M_Product_ID, int m_C_BPartner_ID, int m_M_Warehouse_ID )
	{
		//	Fill Data
		String sql = null;
		int parameter = 0;
		if (m_C_BPartner_ID == 0)	
		{
			sql = "SELECT bp.Name,"
				+ " CASE WHEN io.IsSOTrx='Y' THEN iol.MovementQty*-1 ELSE iol.MovementQty END AS MovementQty,"
				+ " io.MovementDate,io.IsSOTrx,"
				+ " dt.PrintName || ' ' || io.DocumentNo As DocumentNo,"
				+ " w.Name "
				+ "FROM M_InOutLine iol"
				+ " INNER JOIN M_InOut io ON (iol.M_InOut_ID=io.M_InOut_ID)"
				+ " INNER JOIN C_BPartner bp  ON (io.C_BPartner_ID=bp.C_BPartner_ID)"
				+ " INNER JOIN C_DocType dt ON (io.C_DocType_ID=dt.C_DocType_ID)"
				+ " INNER JOIN M_Warehouse w ON (io.M_Warehouse_ID=w.M_Warehouse_ID)"
				+ " INNER JOIN M_InOutLineConfirm lc ON (iol.M_InOutLine_ID=lc.M_InOutLine_ID) "
				+ "WHERE iol.M_Product_ID=?";
			
			if (m_M_Warehouse_ID != 0)
				sql += " AND io.M_Warehouse_ID=?";
					
			sql	+= " AND lc.Processed='N' "
					+ "ORDER BY io.MovementDate,io.IsSOTrx";
			
			parameter = m_M_Product_ID;
		}
		else
		{
			sql = "SELECT p.Name,"
				+ " CASE WHEN io.IsSOTrx='Y' THEN iol.MovementQty*-1 ELSE iol.MovementQty END AS MovementQty,"
				+ " io.MovementDate,io.IsSOTrx,"
				+ " dt.PrintName || ' ' || io.DocumentNo As DocumentNo,"
				+ " w.Name "
				+ "FROM M_InOutLine iol"
				+ " INNER JOIN M_InOut io ON (iol.M_InOut_ID=io.M_InOut_ID)"
				+ " INNER JOIN M_Product p  ON (iol.M_Product_ID=p.M_Product_ID)"
				+ " INNER JOIN C_DocType dt ON (io.C_DocType_ID=dt.C_DocType_ID)"
				+ " INNER JOIN M_Warehouse w ON (io.M_Warehouse_ID=w.M_Warehouse_ID)"
				+ " INNER JOIN M_InOutLineConfirm lc ON (iol.M_InOutLine_ID=lc.M_InOutLine_ID) "
				+ "WHERE io.C_BPartner_ID=?";
			
			if (m_M_Warehouse_ID != 0)
				sql += " AND io.M_Warehouse_ID=?";
				
			sql += " AND lc.Processed='N' "
				+ "ORDER BY io.MovementDate,io.IsSOTrx";
			parameter = m_C_BPartner_ID;
		}
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, parameter);
			if (m_M_Warehouse_ID != 0)
				pstmt.setInt(2, m_M_Warehouse_ID);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(6);
				//	1-Name, 2-MovementQty, 3-MovementDate, 4-IsSOTrx, 5-DocumentNo
				line.add(rs.getString(1));      		//  Name
				line.add(new Double(rs.getDouble(2)));  //  Qty
				line.add(rs.getTimestamp(3));   		//  Date
				line.add(Boolean.valueOf("Y".equals(rs.getString(4))));	//  IsSOTrx
				line.add(rs.getString(5));				//  DocNo
				line.add(rs.getString(6));				//  Warehouse
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		log.fine("#" + data.size());
		return data;
	}
	
	
	/**
	 *	Query Unconfirmed
	 */
	private void initUnconfirmedTab ()
	{
		//	Done already
		if (m_modelUnconfirmed != null)
			return;
			
		//	Header
		Vector<String> columnNames = new Vector<String>();
		for( FieldVO vo : getUnconfirmedTabFieldVOs( Env.getCtx(), m_C_BPartner_ID != 0 ) )
		{
			columnNames.add( vo.name );
		}


		Vector<Vector<Object>> data = queryUnconfirmed( m_M_Product_ID, m_C_BPartner_ID, m_M_Warehouse_ID );
		//  Table
		m_modelUnconfirmed = new DefaultTableModel(data, columnNames); 
		m_tableUnconfirmed.setModel(m_modelUnconfirmed);
		MiniTable table = m_tableUnconfirmed;
		//
		table.setColumnClass(0, String.class, true);      //  Product/Partner
		table.setColumnClass(1, Double.class, true);  	  //  MovementQty
		table.setColumnClass(2, Timestamp.class, true);   //  MovementDate
		table.setColumnClass(3, Boolean.class, true);  	  //  IsSOTrx
		table.setColumnClass(4, String.class, true);      //  DocNo
		//
		table.autoSize();
	}	//	initUnconfirmedTab


	
	/**
	 * 
	 * @param ctx
	 * @param isProduct If True, called from Product info window.  If False, called from Business Partner info window 
	 * @return
	 */
	public static ArrayList< FieldVO > getAtpTabFieldVOs( Ctx ctx, boolean isProduct )
	{
		ArrayList< FieldVO > fieldVOs = new ArrayList< FieldVO >();
		fieldVOs.add( new FieldVO( "Date", Msg.translate( ctx, "Date" ), DisplayTypeConstants.Date ) );
		fieldVOs.add( new FieldVO( "QtyOnHand", Msg.translate( ctx, "QtyOnHand" ), DisplayTypeConstants.Quantity ) );
		fieldVOs.add( new FieldVO( "C_BPartner_ID", Msg.getElement( ctx, "C_BPartner_ID", false ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "QtyOrdered", Msg.translate( ctx, "QtyOrdered" ), DisplayTypeConstants.Quantity ) );
		fieldVOs.add( new FieldVO( "QtyReserved", Msg.translate( ctx, "QtyReserved" ), DisplayTypeConstants.Quantity ) );
		

		// Show WIP quantity only if MFG is installed
		SysEnv se = SysEnv.get("CMFG", false);
        if (!(se==null) && se.checkLicense())
        {
        	fieldVOs.add( new FieldVO( "QtyWIP", Msg.translate( ctx, "QtyWIP" ), DisplayTypeConstants.Quantity ) );
        }

        // Show quantity allocated only if WMS is installed
		se = SysEnv.get("CWMS", false);
        if (!(se==null) && se.checkLicense())
        {
        	//fieldVOs.add( new FieldVO( "QtyDedicated", Msg.translate( ctx, "QtyDedicated" ), FieldType.Quantity ) );
        	fieldVOs.add( new FieldVO( "QtyAllocated", Msg.translate( ctx, "QtyExpectedOut" ), DisplayTypeConstants.Quantity ) );
        	fieldVOs.add( new FieldVO( "QtyExpected", Msg.translate( ctx, "QtyExpectedIn" ), DisplayTypeConstants.Quantity ) );
        }

        fieldVOs.add( new FieldVO( "M_Locator_ID", Msg.getElement( ctx, "M_Locator_ID", false ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "M_AttributeSetInstance_ID", Msg.getElement( ctx, "M_AttributeSetInstance_ID", false ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "DocumentNo", Msg.translate( ctx, "DocumentNo" ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "M_Warehouse_ID", Msg.getElement( ctx, "M_Warehouse_ID", false ), DisplayTypeConstants.String ) );
		
		for( FieldVO f : fieldVOs )
			f.IsReadOnly = true;
		
		for( int i = 2; i < fieldVOs.size(); ++i )
			fieldVOs.get( i ).IsKey = true;
		
		return fieldVOs;
	}
	
	
	public static Vector<Vector<Object>> queryAtp( int m_M_Product_ID, int m_C_BPartner_ID, int m_M_Warehouse_ID, int m_M_AttributeSetInstance_ID )
	{

		boolean isCMFGLicensed = false;
		SysEnv se = SysEnv.get("CMFG", false);
        if (!(se==null) && se.checkLicense())
        {
        	isCMFGLicensed = true;
        }

        boolean isCWMSLicensed = false;
		se = SysEnv.get("CWMS", false);
        if (!(se==null) && se.checkLicense())
        {
        	isCWMSLicensed = true;
        }

        
        //	Fill Storage Data
		boolean showDetail = CLogMgt.isLevelFine();
		String sql = "SELECT IsStocked FROM M_Product"
			+ " WHERE M_Product_ID=?"
			+ " AND IsBOM= 'Y' AND IsVerified='Y'";
		String isStocked = QueryUtil.getSQLValueString(null, sql, m_M_Product_ID);

		if ((isStocked != null) && ("N".equals(isStocked)))
		{
			sql = "SELECT bomQtyOnHandByLocator(p.M_Product_ID,l.M_Locator_ID) AS QtyOnHand,"
				+ " bomQtyReserved(p.M_Product_ID,null,l.M_Locator_ID) AS QtyReserved,"
				+ " bomQtyOrdered(p.M_Product_ID,null,l.M_Locator_ID) AS QtyOrdered,"
				+ " NULL, NULL, NULL,"
				+ " NULL, 0,";
			if (!showDetail)
				sql = "SELECT SUM(bomQtyOnHandByLocator(p.M_Product_ID,l.M_Locator_ID)) AS QtyOnHand,"
					+ " SUM(bomQtyReserved(p.M_Product_ID,null,l.M_Locator_ID)) AS QtyReserved,"
					+ " SUM(bomQtyOrdered(p.M_Product_ID,null,l.M_Locator_ID)) AS QtyOrdered,"
					+ " NULL, NULL, NULL,"
					+ " NULL, 0,";
			sql += "w.Name, l.Value "
				+ "FROM M_Product p";
			if (m_M_Warehouse_ID != 0)
				sql += " INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = ?)"
					+ " INNER JOIN M_Locator l ON (l.M_Warehouse_ID=w.M_Warehouse_ID)";
			else
				sql += " INNER JOIN M_Warehouse w ON (w.AD_Client_ID = p.AD_Client_ID AND (p.AD_Org_ID = 0 OR w.AD_Org_ID=p.AD_Org_ID))"
					+ " INNER JOIN M_Locator l ON (l.M_Warehouse_ID=w.M_Warehouse_ID)";
			sql += " WHERE p.M_Product_ID=?";
			if (!showDetail)
				sql += " GROUP BY p.M_Product_ID, w.Name, l.Value";
			sql += " ORDER BY l.Value";
		}
		else
		{
			sql = "SELECT s.QtyOnHand, s.QtyReserved, s.QtyOrdered,"
			+ " 0,"  // WIP Qty
			+ " s.QtyDedicated+s.QtyAllocated,s.QtyExpected, "
			+ " productAttribute(s.M_AttributeSetInstance_ID), s.M_AttributeSetInstance_ID,";
			if (!showDetail)
				sql = "SELECT SUM(s.QtyOnHand), SUM(s.QtyReserved), SUM(s.QtyOrdered),"
					+ " MAX(qtyWIP(s.M_Product_ID, s.M_Locator_ID)),"  // WIP Qty
					+ " SUM(s.QtyDedicated)+SUM(s.QtyAllocated), SUM(s.QtyExpected),"
					+ " productAttribute(s.M_AttributeSetInstance_ID), 0,";
			sql += " w.Name, l.Value "
				+ "FROM M_STORAGE_V s"
				+ " INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID)"
				+ " INNER JOIN M_Warehouse w ON (l.M_Warehouse_ID=w.M_Warehouse_ID) "
				+ "WHERE M_Product_ID=?";
			if (m_M_Warehouse_ID != 0)
				sql += " AND l.M_Warehouse_ID=?";
			if (m_M_AttributeSetInstance_ID > 0)
				sql += " AND s.M_AttributeSetInstance_ID=?";
			sql += " AND (s.QtyOnHand<>0 OR s.QtyReserved<>0 OR s.QtyOrdered<>0 "
				+ " OR s.QtyDedicated <> 0 OR s.QtyAllocated <> 0 OR s.QtyExpected <> 0)";
			if (!showDetail)
				sql += " GROUP BY productAttribute(s.M_AttributeSetInstance_ID), w.Name, l.Value";
			sql += " ORDER BY l.Value";
		}

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		double qty = 0;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			
			if ((isStocked != null) && ("N".equals(isStocked)))
			{
				if (m_M_Warehouse_ID != 0)
				{
					pstmt.setInt(1, m_M_Warehouse_ID);
					pstmt.setInt(2, m_M_Product_ID);
				}
				else
					pstmt.setInt(1, m_M_Product_ID);						
			}
			else
			{
				pstmt.setInt(1, m_M_Product_ID);
				if (m_M_Warehouse_ID != 0)
					pstmt.setInt(2, m_M_Warehouse_ID);
				if (m_M_AttributeSetInstance_ID > 0)
					pstmt.setInt(3, m_M_AttributeSetInstance_ID);
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(12);
				line.add(null);							//  Date
				double qtyOnHand = rs.getDouble(1);
				qty += qtyOnHand;
				line.add(new Double(qtyOnHand));  		//  Qty
				line.add(null);							//  BPartner
				line.add(new Double(rs.getDouble(3)));  //  QtyOrdered
				line.add(new Double(rs.getDouble(2)));  //  QtyReserved
				
				// Show WIP quantity only if MFG is installed
		        if (isCMFGLicensed)
		        {
		        	line.add(new Double(rs.getDouble(4)));	// WIP Qty  
		        }

		        // Show Qty Allocated only if WMS is installed
		        if (isCWMSLicensed)
		        {
		        	//line.add(new Double(rs.getDouble(4)));  //  QtyDedicated
		        	line.add(new Double(rs.getDouble(5)));  //  QtyAllocated
		        	line.add(new Double(rs.getDouble(6)));  //  QtyExpected
		        }
		        
				line.add(rs.getString(10));      		//  Locator
				String asi = rs.getString(7);
				if (showDetail && (asi == null || asi.length() == 0))
					asi = "{" + rs.getInt(8) + "}";
				line.add(asi);							//  ASI
				line.add(null);							//  DocumentNo
				line.add(rs.getString(9));  			//	Warehouse
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		//	Orders
		sql = "SELECT COALESCE(ol.DatePromised, o.DatePromised), ol.QtyReserved,ol.QtyAllocated,"
			+ " productAttribute(ol.M_AttributeSetInstance_ID), ol.M_AttributeSetInstance_ID,"
			+ " dt.DocBaseType, NULL, bp.Name,"
			+ " dt.PrintName || ' ' || o.DocumentNo As DocumentNo, w.Name, 'O' "
			+ "FROM C_Order o"
			+ " INNER JOIN C_OrderLine ol ON (o.C_Order_ID=ol.C_Order_ID)"
			+ " INNER JOIN C_DocType dt ON (o.C_DocType_ID=dt.C_DocType_ID)"
			+ " INNER JOIN M_Warehouse w ON (ol.M_Warehouse_ID=w.M_Warehouse_ID)"
			+ " INNER JOIN C_BPartner bp  ON (o.C_BPartner_ID=bp.C_BPartner_ID) "
			+ "WHERE ol.QtyReserved<>0"
			+ " AND ol.M_Product_ID=?";
		if (m_M_Warehouse_ID != 0)
			sql += " AND ol.M_Warehouse_ID=?";
		if (m_M_AttributeSetInstance_ID > 0)
			sql += " AND ol.M_AttributeSetInstance_ID=?";

        if (isCMFGLicensed)
        {
    		sql += " UNION ALL "
    			+ "SELECT wo.DateScheduleTo, wo.QtyEntered - wo.QtyAssembled - wo.QtyScrapped, NULL,"
    			+ " productAttribute(wo.M_AttributeSetInstance_ID), wo.M_AttributeSetInstance_ID,"
    			+ " NULL, l.Value, COALESCE(bp.Name, NULL),"
    			+ " dt.PrintName || ' ' || wo.DocumentNo As DocumentNo, w.Name, 'WO' "
    			+ "FROM M_WorkOrder wo"
    			+ " INNER JOIN C_DocType dt ON (wo.C_DocType_ID=dt.C_DocType_ID)"
    			+ " INNER JOIN M_Warehouse w ON (wo.M_Warehouse_ID=w.M_Warehouse_ID)"
    			+ " INNER JOIN M_Locator l ON (wo.M_Locator_ID=l.M_Locator_ID)"
    			+ " LEFT OUTER JOIN C_BPartner bp  ON (wo.C_BPartner_ID=bp.C_BPartner_ID) "
    			+ "WHERE (wo.QtyEntered - wo.QtyAssembled - wo.QtyScrapped) <> 0"
    			+ " AND wo.docstatus = 'IP'"
    			+ " AND wo.WOType = 'S'"
    			+ " AND wo.M_Product_ID=?";
    		if (m_M_Warehouse_ID != 0)
    			sql += " AND wo.M_Warehouse_ID=?";
    		if (m_M_AttributeSetInstance_ID > 0)
    			sql += " AND wo.M_AttributeSetInstance_ID=?";	
        }
        	
        	
		sql += " ORDER BY 1, 8";
		try
		{
			int paramIndex = 1;
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(paramIndex, m_M_Product_ID);
			paramIndex++;
			if (m_M_Warehouse_ID != 0)
				pstmt.setInt(paramIndex++, m_M_Warehouse_ID);
			if (m_M_AttributeSetInstance_ID > 0)
				pstmt.setInt(paramIndex++, m_M_AttributeSetInstance_ID);
			if (isCMFGLicensed)
			{
				pstmt.setInt(paramIndex++, m_M_Product_ID);
				if (m_M_Warehouse_ID != 0)
					pstmt.setInt(paramIndex++, m_M_Warehouse_ID);
				if (m_M_AttributeSetInstance_ID > 0)
					pstmt.setInt(paramIndex++, m_M_AttributeSetInstance_ID);			
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(12);
				line.add(rs.getTimestamp(1));			//  Date
				String DocBaseType = rs.getString(6);
				boolean isWO = ("WO".equals(rs.getString(11))) ? true : false; 
				
				double oq = rs.getDouble(2);
				if (!isWO)
				{
					Double qtyReserved = null;
					Double qtyOrdered = null;
					if (MDocBaseType.DOCBASETYPE_PurchaseOrder.equals(DocBaseType))
					{
						qtyOrdered = new Double(oq);
						qty += oq;
					}
					else
					{
						qtyReserved = new Double(oq);
						qty -= oq;
					}
					line.add(new Double(qty)); 		 		//  Qty (Order)
					line.add(rs.getString(8));				//  BPartner
					line.add(qtyOrdered);					//  QtyOrdered (Order)
					line.add(qtyReserved);					//  QtyReserved (Order)
		        	if (isCMFGLicensed)
		        		line.add(null);							//  QtyWIP (Order)  
				}
				else
				{
					qty += oq;
					line.add(new Double(qty)); 		 		//  Qty (Work Order)				
					line.add(rs.getString(8));				//  BPartner
					line.add(null);							//  QtyOrdered (Work Order)
					line.add(null);							//  QtyReserved (Work Order)
		        	if (isCMFGLicensed)
		        		line.add(new Double(oq)); 		 		//  QtyWIP (Work Order)								
				}
				

		        // Show Quantity Allocated for orders only if WMS is installed
		        if (isCWMSLicensed)
		        {
		        	if (!isWO)
		        	{
		        		//line.add(null);						// Qty Dedicated
		        		Double QtyExpectedOut=null;
		        		if (MDocBaseType.DOCBASETYPE_SalesOrder.equals(DocBaseType))
		        			QtyExpectedOut = new Double(rs.getDouble(3));
		        		line.add(QtyExpectedOut);				// Expected Out (Order)
		        		line.add(null);							// Expected In (Order)
		        	}
		        	else
		        	{
			        	line.add(null);                 // Expected Out (Work Order)
			        	line.add(null);                 // Expected In	(Work Order)	
		        	}
		        }
		        
		        line.add(rs.getString(7));				//  Locator
				String asi = rs.getString(4);
				if (showDetail && (asi == null || asi.length() == 0))
					asi = "{" + rs.getInt(5) + "}";
				line.add(asi);							//  ASI
				line.add(rs.getString(9));				//  DocumentNo
				line.add(rs.getString(10));  			//	Warehouse
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	
		return data;
	}
	
	/**
	 *	Query ATP
	 */
	private void initAtpTab ()
	{
		//	Done already
		if (m_modelAtp != null)
			return;
			
		//	Header
		Vector<String> columnNames = new Vector<String>();
		for( FieldVO vo : getAtpTabFieldVOs( Env.getCtx(), m_C_BPartner_ID != 0 ) )
		{
			columnNames.add( vo.name );
		}

		Vector<Vector<Object>> data = queryAtp( m_M_Product_ID, m_C_BPartner_ID, m_M_Warehouse_ID, m_M_AttributeSetInstance_ID );

		//  Table
		MiniTable table = null;
		m_modelAtp = new DefaultTableModel(data, columnNames); 
		m_tableAtp.setModel(m_modelAtp);
		table = m_tableAtp;
		//
		
		int i=0;
		table.setColumnClass(i++, Timestamp.class, true);   //  Date
		table.setColumnClass(i++, Double.class, true);      //  Quantity
		table.setColumnClass(i++, String.class, true);      //  Partner
		table.setColumnClass(i++, Double.class, true);      //  Quantity
		table.setColumnClass(i++, Double.class, true);      //  Quantity
		
		// Show WIP quantity only if MFG is installed
		SysEnv se = SysEnv.get("CMFG", false);
        if (!(se==null) && se.checkLicense())
        {
        	table.setColumnClass(i++, Double.class, true);      //  Quantity
        }

        // Show quantity Allocated only if WMS is installed
		se = SysEnv.get("CWMS", false);
        if (!(se==null) && se.checkLicense())
        {
        	table.setColumnClass(i++, Double.class, true);      //  Quantity
        	table.setColumnClass(i++, Double.class, true);      //  Quantity
        }
        
		table.setColumnClass(i++, String.class, true);   	  //  Locator
		table.setColumnClass(i++, String.class, true);   	  //  ASI
		table.setColumnClass(i++, String.class, true);      //  DocNo
		table.setColumnClass(i++, String.class, true);   	  //  Warehouse
		//
		table.autoSize();
	}	//	initAtpTab
	
}	//	InvoiceHistory
