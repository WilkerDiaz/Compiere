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

import javax.swing.table.*;

import org.compiere.apps.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Search Product and return selection
 *
 *  @author     Jorg Janke
 *  @version    $Id: InfoProduct.java,v 1.4 2006/07/30 00:51:27 jjanke Exp $
 */
public final class InfoProduct extends Info implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(InfoProduct.class);

	/**
	 *	Standard Constructor
	 * 	@param frame frame
	 * 	@param modal modal
	 * 	@param WindowNo window no
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param M_PriceList_ID price list
	 * 	@param value    Query Value or Name if enclosed in @
	 * 	@param multiSelection multiple selections
	 * 	@param whereClause where clause
	 */
	public InfoProduct(Frame frame, boolean modal, int WindowNo,
		int M_Warehouse_ID, int M_PriceList_ID, String value,
		boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "p", "M_Product_ID", multiSelection, whereClause);
		log.info(value + ", Wh=" + M_Warehouse_ID + ", PL=" + M_PriceList_ID + ", WHERE=" + whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoProduct"));
		//
		statInit();
		initInfo (value, M_Warehouse_ID, M_PriceList_ID);
		m_C_BPartner_ID = Env.getCtx().getContextAsInt( WindowNo, "C_BPartner_ID");

		//
		int no = p_table.getRowCount();
		setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		setStatusDB(Integer.toString(no));
		//	AutoQuery
		if (value != null && value.length() > 0)
			executeQuery();
		p_loadedOK = true;
		//	Focus
		fieldValue.requestFocus();

		AEnv.positionCenterWindow(frame, this);
	}	//	InfoProduct

	/** SQL From				*/
	private static final String s_productFrom =
		"M_Product p"
		+ " LEFT OUTER JOIN M_ProductPrice pr ON (p.M_Product_ID=pr.M_Product_ID AND pr.IsActive='Y')"
		+ " LEFT OUTER JOIN M_PriceList_Version plv ON (pr.M_PriceList_Version_ID=plv.M_PriceList_Version_ID)"
		+ " LEFT OUTER JOIN M_AttributeSet pa ON (p.M_AttributeSet_ID=pa.M_AttributeSet_ID),"
		+ " M_Warehouse w";

	/**  Array of Column Info    */
	private static Info_Column[] s_productLayout = null;
	private static int INDEX_NAME = 0;
	private static int INDEX_PATTRIBUTE = 0;
	/** Header for Price List Version 	*/
	private static String		s_headerPriceList = "";
	/** Header for Warehouse 		*/
	private static String		s_headerWarehouse = "";

	//
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(10);
	private CLabel labelName = new CLabel();
	private CTextField fieldName = new CTextField(10);
	private CLabel labelUPC = new CLabel();
	private CTextField fieldUPC = new CTextField(10);
	private CLabel labelSKU = new CLabel();
	private CTextField fieldSKU = new CTextField(10);
	private CLabel labelPriceList = new CLabel();
	private VComboBox pickPriceList = new VComboBox();
	private CLabel labelWarehouse = new CLabel();
	private VComboBox pickWarehouse = new VComboBox();

	/**	Search Button				*/
	private CButton		m_InfoPAttributeButton = new CButton(Env.getImageIcon("PAttribute16.gif"));
	/** Instance Button				*/
	private CButton		m_PAttributeButton = null;
	/** ASI							*/
	private int			m_M_AttributeSetInstance_ID = -1;
	/** Locator						*/
	private int			m_M_Locator_ID = 0;
	/** Display Price List Column	*/
	private boolean		m_displayPriceList = false;
	/** Display Warehouse Column	*/
	private boolean		m_displayWarehouse = false;
	/** Security for Price List Version 	*/
	private String		m_securityPriceList = null;
	/** Security for Warehouse 		*/
	private String		m_securityWarehouse = null;
	private String		m_pAttributeWhere = null;
	private int			m_C_BPartner_ID = 0;

	/**
	 *	Static Setup - add fields to parameterPanel
	 */
	private void statInit()
	{
		labelValue.setText(Msg.getMsg(Env.getCtx(), "Value"));
		fieldValue.setBackground(CompierePLAF.getInfoBackground());
		fieldValue.addActionListener(this);
		
		labelName.setText(Msg.getMsg(Env.getCtx(), "Name"));
		fieldName.setBackground(CompierePLAF.getInfoBackground());
		fieldName.addActionListener(this);

		labelUPC.setText(Msg.translate(Env.getCtx(), "UPC"));
		fieldUPC.setBackground(CompierePLAF.getInfoBackground());
		fieldUPC.addActionListener(this);

		labelSKU.setText(Msg.translate(Env.getCtx(), "SKU"));
		fieldSKU.setBackground(CompierePLAF.getInfoBackground());
		fieldSKU.addActionListener(this);
		
		labelWarehouse.setText(Msg.getMsg(Env.getCtx(), "Warehouse"));
		pickWarehouse.setBackground(CompierePLAF.getInfoBackground());
		
		labelPriceList.setText(Msg.getMsg(Env.getCtx(), "PriceListVersion"));
		pickPriceList.setBackground(CompierePLAF.getInfoBackground());

		m_InfoPAttributeButton.setMargin(new Insets(2,2,2,2));
		m_InfoPAttributeButton.setToolTipText(Msg.getMsg(Env.getCtx(), "InfoPAttribute"));
		m_InfoPAttributeButton.addActionListener(this);

		//	Line 1
		parameterPanel.setLayout(new ALayout());
		parameterPanel.add(labelValue, new ALayoutConstraint(0,0));
		parameterPanel.add(fieldValue, null);
		parameterPanel.add(labelUPC, null);
		parameterPanel.add(fieldUPC, null);
		parameterPanel.add(labelWarehouse, null);
		parameterPanel.add(pickWarehouse, null);
		parameterPanel.add(m_InfoPAttributeButton);
		//	Line 2
		parameterPanel.add(labelName, new ALayoutConstraint(1,0));
		parameterPanel.add(fieldName, null);
		parameterPanel.add(labelSKU, null);
		parameterPanel.add(fieldSKU, null);
		parameterPanel.add(labelPriceList, null);
		parameterPanel.add(pickPriceList, null);
		
		//	Product Attribute Instance
		m_PAttributeButton = ConfirmPanel.createPAttributeButton(true);
		confirmPanel.addButton(m_PAttributeButton);
		m_PAttributeButton.addActionListener(this);
		m_PAttributeButton.setEnabled(false);
	}	//	statInit

	/**
	 *	Dynamic Init
	 *
	 * @param value value
	 * @param M_Warehouse_ID warehouse
	 * @param M_PriceList_ID price list
	 */
	private void initInfo (String value, int M_Warehouse_ID, int M_PriceList_ID)
	{
		//	Pick init
		fillPicks(M_PriceList_ID);
		int M_PriceList_Version_ID = findPLV (Env.getCtx(), p_WindowNo, M_PriceList_ID);
		//	Set Value or Name
		if (value.startsWith("@") && value.endsWith("@"))
			fieldName.setText(value.substring(1,value.length()-1));
		else
			fieldName.setText(value+"%");
		//	Set Warehouse
		if (M_Warehouse_ID == 0){
			if(Env.getCtx().getContextAsInt( "#M_Warehouse_ID")!=0)
				M_Warehouse_ID = Env.getCtx().getContextAsInt( "#M_Warehouse_ID");
			else
				M_Warehouse_ID = Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"); //BECO - CD POR DEFECTO CUANDO NO HAY WH
		}
		if (M_Warehouse_ID != 0)
			setWarehouse (M_Warehouse_ID);
		// 	Set PriceList Version
		if (M_PriceList_Version_ID != 0)
			setPriceListVersion (M_PriceList_Version_ID);

		//	Create Grid
		StringBuffer where = new StringBuffer();
		where.append("p.IsActive='Y'");
		if (M_Warehouse_ID != 0)
			where.append(" AND p.IsSummary='N'");
		//  dynamic Where Clause
		if (p_whereClause != null && p_whereClause.length() > 0)
			where.append(" AND ")   //  replace fully qalified name with alias
				.append(Util.replace(p_whereClause, "M_Product.", "p."));
		//
		prepareTable(s_productFrom,
			where.toString(),
			"QtyAvailable DESC, Margin DESC");

		//
		pickWarehouse.addActionListener(this);
		pickPriceList.addActionListener(this);
	}	//	initInfo

	
	
	public static ArrayList< KeyNamePair > getPriceListVersions( Ctx ctx, int M_PriceList_ID ) throws SQLException
	{
		ArrayList< KeyNamePair > result = new ArrayList< KeyNamePair >();

		String SQL = "SELECT M_PriceList_Version.M_PriceList_Version_ID,"
				+ " M_PriceList_Version.Name || ' (' || c.ISO_Code || ')' AS ValueName "
				+ "FROM M_PriceList_Version, M_PriceList pl, C_Currency c "
				+ "WHERE M_PriceList_Version.M_PriceList_ID=pl.M_PriceList_ID" + " AND pl.C_Currency_ID=c.C_Currency_ID"
				+ " AND M_PriceList_Version.IsActive='Y' AND pl.IsActive='Y'";
		// Same PL currency as original one
		if( M_PriceList_ID != 0 )
			SQL += " AND EXISTS (SELECT * FROM M_PriceList xp WHERE xp.M_PriceList_ID= ? "
					+ ")";
		// Add Access & Order
		SQL = MRole.getDefault(ctx, false).addAccessSQL( SQL, "M_PriceList_Version", true, false ) // fully
																							// qualidfied
																							// - RO
				+ " ORDER BY M_PriceList_Version.Name";

		StringBuffer security = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement( SQL, (Trx) null);
			if( M_PriceList_ID != 0 )
				pstmt.setInt(1, M_PriceList_ID);
			rs = pstmt.executeQuery();
			while( rs.next() )
			{
				int M_PriceList_Version_ID = rs.getInt( 1 );
				if( security.length() > 0 )
					security.append( "," );
				security.append( M_PriceList_Version_ID );
				KeyNamePair kn = new KeyNamePair( M_PriceList_Version_ID, rs.getString( 2 ) );
				result.add( kn );
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getLines", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return result;

	}
	
	public static ArrayList< KeyNamePair > getWarehouses(Ctx ctx) throws SQLException
	{
		ArrayList< KeyNamePair > result = new ArrayList< KeyNamePair >();

		// Warehouse
		String SQL = MRole.getDefault(ctx, false).addAccessSQL(
				"SELECT M_Warehouse_ID, Value || ' - ' || Name AS ValueName " + "FROM M_Warehouse " + "WHERE IsActive='Y'",
				"M_Warehouse", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO )
				+ " ORDER BY Value";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement( SQL, (Trx) null);
			rs = pstmt.executeQuery();
			while( rs.next() )
			{
				int M_Warehouse_ID = rs.getInt( 1 );
				KeyNamePair kn = new KeyNamePair( M_Warehouse_ID, rs.getString( 2 ) );
				result.add( kn );
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getLines", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return result;
	}
	
	/**
	 * Fill Picks with values
	 * 
	 * @param M_PriceList_ID
	 *            price list
	 */
	private void fillPicks( int M_PriceList_ID )
	{
		StringBuffer security = null;

		
		
		ArrayList< KeyNamePair > priceListVersions = null;
		try
		{
			priceListVersions = getPriceListVersions( Env.getCtx(), M_PriceList_ID );
		}
		catch (SQLException e)
		{
			log.severe( e.getMessage() );
			setStatusLine(e.getLocalizedMessage(), true);
		}
		security = new StringBuffer();
		pickPriceList.addItem( new KeyNamePair( 0, "" ) );
		for( KeyNamePair pair : priceListVersions )
		{
			if( security.length() > 0 )
				security.append( "," );
			security.append( pair.getKey() );
			pickPriceList.addItem( pair );
		}
		m_securityPriceList = " AND plv.IsActive = 'Y' ";
		
		ArrayList< KeyNamePair > warehouses = null; 
		try
		{
			warehouses = getWarehouses(Env.getCtx());
		}
		catch (SQLException e)
		{
			log.severe( e.getMessage() );
			setStatusLine(e.getLocalizedMessage(), true);
		}
		
		security = new StringBuffer();
		pickWarehouse.addItem( new KeyNamePair( 0, "" ) );
		for( KeyNamePair pair : warehouses )
		{
			if( security.length() > 0 )
				security.append( "," );
			security.append( pair.getKey() );
			pickWarehouse.addItem( pair );
		}
		m_securityWarehouse = " AND w.M_Warehouse_ID IN (" + security.toString() + ")";

	} // fillPicks

	/**
	 * Set Warehouse
	 * 
	 * @param M_Warehouse_ID
	 *            warehouse
	 */
	private void setWarehouse(int M_Warehouse_ID)
	{
		for (int i = 0; i < pickWarehouse.getItemCount(); i++)
		{
			KeyNamePair kn = (KeyNamePair)pickWarehouse.getItemAt(i);
			if (kn.getKey() == M_Warehouse_ID)
			{
				pickWarehouse.setSelectedIndex(i);
				return;
			}
		}
	}	//	setWarehouse

	/**
	 *	Set PriceList
	 *
	 * @param M_PriceList_Version_ID price list
	 */
	private void setPriceListVersion(int M_PriceList_Version_ID)
	{
		log.config("M_PriceList_Version_ID=" + M_PriceList_Version_ID);
		for (int i = 0; i < pickPriceList.getItemCount(); i++)
		{
			KeyNamePair kn = (KeyNamePair)pickPriceList.getItemAt(i);
			if (kn.getKey() == M_PriceList_Version_ID)
			{
				pickPriceList.setSelectedIndex(i);
				return;
			}
		}
		log.fine("NOT found");
	}	//	setPriceList

	/**
	 *	Find Price List Version and update context
	 *
	 * @param M_PriceList_ID price list
	 * @return M_PriceList_Version_ID price list version
	 */
	public static int findPLV ( Ctx ctx, int p_WindowNo, int M_PriceList_ID)
	{
		Timestamp priceDate = null;
		//	Sales Order Date
		String dateStr = ctx.getContext(p_WindowNo, "DateOrdered"); 
		if (dateStr != null && dateStr.length() > 0)
			priceDate = new Timestamp(Env.getCtx().getContextAsTime(p_WindowNo, "DateOrdered"));
		else	//	Invoice Date
		{
			dateStr = ctx.getContext(p_WindowNo, "DateInvoiced");
			if (dateStr != null && dateStr.length() > 0)
				priceDate = new Timestamp(Env.getCtx().getContextAsTime(p_WindowNo, "DateInvoiced"));
		}
		//	Today
		if (priceDate == null) 
			priceDate = new Timestamp(System.currentTimeMillis());
		//
		s_log.config("M_PriceList_ID=" + M_PriceList_ID + " - " + priceDate);
		int retValue = 0;
		String sql = "SELECT plv.M_PriceList_Version_ID, plv.ValidFrom "
			+ "FROM M_PriceList pl, M_PriceList_Version plv "
			+ "WHERE pl.M_PriceList_ID=plv.M_PriceList_ID"
			+ " AND plv.IsActive='Y'"
			+ " AND pl.M_PriceList_ID=? "					//	1
			+ "ORDER BY plv.ValidFrom DESC";
		//	find newest one
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, M_PriceList_ID);
			rs = pstmt.executeQuery();
			while (rs.next() && retValue == 0)
			{
				Timestamp plDate = rs.getTimestamp(2);
				if (!priceDate.before(plDate))
					retValue = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		ctx.setContext(p_WindowNo, "M_PriceList_Version_ID", retValue);
		return retValue;
	}	//	findPLV

	
	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return SQL WHERE clause
	 */
	@Override
	String getSQLWhere()
	{
		StringBuffer where = new StringBuffer();

		//  Optional Warehouse
		int M_Warehouse_ID = 0;
		KeyNamePair wh = (KeyNamePair)pickWarehouse.getSelectedItem();
		if (wh != null)
			M_Warehouse_ID = wh.getKey();
		m_displayWarehouse = M_Warehouse_ID == 0;
		if (M_Warehouse_ID != 0)
			where.append(" AND w.M_Warehouse_ID=?");
		else
			where.append(m_securityWarehouse);
		
		//	Optional PLV
		int M_PriceList_Version_ID = 0;
		KeyNamePair pl = (KeyNamePair)pickPriceList.getSelectedItem();
		if (pl != null)
			M_PriceList_Version_ID = pl.getKey();
		m_displayPriceList = M_PriceList_Version_ID == 0; 
		if (M_PriceList_Version_ID != 0)
			where.append(" AND pr.M_PriceList_Version_ID=?");
		else
			where.append(m_securityPriceList);
		
		//	Product Attribute Search
		if (m_pAttributeWhere != null)
		{
			where.append(m_pAttributeWhere);
			return where.toString();
		}

		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			where.append(" AND UPPER(p.Value) LIKE ?");

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			where.append(" AND UPPER(p.Name) LIKE ?");

		//  => UPC
		String upc = fieldUPC.getText().toUpperCase();
		if (!(upc.equals("") || upc.equals("%")))
			where.append(" AND UPPER(p.UPC) LIKE ?");

		//  => SKU
		String sku = fieldSKU.getText().toUpperCase();
		if (!(sku.equals("") || sku.equals("%")))
			where.append(" AND UPPER(p.SKU) LIKE ?");

		//	Show/hide columns
		TableModel model = p_table.getModel();
		int size = model.getColumnCount();
		for (int col = 0; col < size; col++)
		{
			TableColumn tc = p_table.getColumnModel().getColumn(col);
			Object hv = tc.getHeaderValue();
			if ((!m_displayPriceList && s_headerPriceList.equals(hv))
				|| (!m_displayWarehouse && s_headerWarehouse.equals(hv)))
			{
			//	log.warning ("Hide column #" + col);
				tc.setResizable(false);
			}
			else
				tc.setResizable(true);
		}
		return where.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query
	 *  (as defined in getSQLWhere)
	 *
	 *	@param pstmt pstmt
	 *  @param forCount for counting records only
	 * @throws SQLException
	 */
	@Override
	void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;

		//  => Warehouse
		int M_Warehouse_ID = 0;
		KeyNamePair wh = (KeyNamePair)pickWarehouse.getSelectedItem();
		if (wh != null)
			M_Warehouse_ID = wh.getKey();
		if (M_Warehouse_ID != 0)
		{
			pstmt.setInt(index++, M_Warehouse_ID);
			log.fine("M_Warehouse_ID=" + M_Warehouse_ID);
		}

		//  => PriceList
		int M_PriceList_Version_ID = 0;
		KeyNamePair pl = (KeyNamePair)pickPriceList.getSelectedItem();
		if (pl != null)
			M_PriceList_Version_ID = pl.getKey();
		if (M_PriceList_Version_ID != 0)
		{
			pstmt.setInt(index++, M_PriceList_Version_ID);
			log.fine("M_PriceList_Version_ID=" + M_PriceList_Version_ID);
		}
		
		//	Rest of Parameter in Query for Attribute Search
		if (m_pAttributeWhere != null)
			return;

		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
		{
			if (!value.endsWith("%"))
				value += "%";
			pstmt.setString(index++, value);
			log.fine("Value: " + value);
		}

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
		{
			if (!name.endsWith("%"))
				name += "%";
			pstmt.setString(index++, name);
			log.fine("Name: " + name);
		}

		//  => UPC
		String upc = fieldUPC.getText().toUpperCase();
		if (!(upc.equals("") || upc.equals("%")))
		{
			if (!upc.endsWith("%"))
				upc += "%";
			pstmt.setString(index++, upc);
			log.fine("UPC: " + upc);
		}

		//  => SKU
		String sku = fieldSKU.getText().toUpperCase();
		if (!(sku.equals("") || sku.equals("%")))
		{
			if (!sku.endsWith("%"))
				sku += "%";
			pstmt.setString(index++, sku);
			log.fine("SKU: " + sku);
		}

	}   //  setParameters

	
	/**************************************************************************
	 *  Action Listner
	 *	@param e event
	 */
	@Override
	public void actionPerformed (ActionEvent e)
	{
		//  don't requery if fieldValue and fieldName are empty
		if ((e.getSource() == pickWarehouse || e.getSource() == pickPriceList)
			&& (fieldValue.getText().length() == 0 && fieldName.getText().length() == 0))
			return;
			
		//	Product Attribute Search
		if (e.getSource().equals(m_InfoPAttributeButton))
		{
			cmd_InfoPAttribute();
			return;		
		}
		m_pAttributeWhere = null;
		
		//	Query Product Attribure Instance
		int row = p_table.getSelectedRow();
		if (e.getSource().equals(m_PAttributeButton) && row != -1)
		{
			Integer productInteger = getSelectedRowKey();
			String productName = (String)p_table.getModel().getValueAt(row, INDEX_NAME);
			KeyNamePair warehouse = (KeyNamePair)pickWarehouse.getSelectedItem();
			if (productInteger == null || productInteger.intValue() == 0 || warehouse == null)
				return;
			String title = warehouse.getName() + " - " + productName;
			PAttributeInstance pai = new PAttributeInstance (this, title, 
				warehouse.getKey(), 0, productInteger.intValue(), m_C_BPartner_ID);
			m_M_AttributeSetInstance_ID = pai.getM_AttributeSetInstance_ID();
			m_M_Locator_ID = pai.getM_Locator_ID();
			if (m_M_AttributeSetInstance_ID != -1)
				dispose(true);
			return;			
		}
		//
		super.actionPerformed(e);
	}   //  actionPerformed

	/**
	 *  Enable PAttribute if row selected/changed
	 */
	@Override
	void enableButtons ()
	{
		m_M_AttributeSetInstance_ID = -1;
		if (m_PAttributeButton != null)
		{
			int row = p_table.getSelectedRow();
			boolean enabled = false;
			if (row >= 0)
			{
				Object value = p_table.getModel().getValueAt(row, INDEX_PATTRIBUTE);
				enabled = Boolean.TRUE.equals(value);
			}
			m_PAttributeButton.setEnabled(enabled);
		}
		super.enableButtons();
	}   //  enableButtons

	/**
	 * 	Query per Product Attribute.
	 *  <code>
	 * 	Available synonyms:
	 *		M_Product p
	 *		M_ProductPrice pr
	 *		M_AttributeSet pa
	 *	</code>
	 */
	private void cmd_InfoPAttribute()
	{
		InfoPAttribute ia = new InfoPAttribute(this);
		m_pAttributeWhere = ia.getWhereClause();
		if (m_pAttributeWhere != null)
			executeQuery();
	}	//	cmdInfoAttribute

	/**
	 *	Show History
	 */
	@Override
	void showHistory()
	{
		log.info("");
		Integer M_Product_ID = getSelectedRowKey();
		if (M_Product_ID == null)
			return;
		KeyNamePair kn = (KeyNamePair)pickWarehouse.getSelectedItem();
		int M_Warehouse_ID = kn.getKey();
		int M_AttributeSetInstance_ID = m_M_AttributeSetInstance_ID;
		if (m_M_AttributeSetInstance_ID < -1)	//	not selected
			M_AttributeSetInstance_ID = 0;
		//
		InvoiceHistory ih = new InvoiceHistory (this, 0, 
			M_Product_ID.intValue(), M_Warehouse_ID, M_AttributeSetInstance_ID);
		ih.setVisible(true);
		ih = null;
	}	//	showHistory

	/**
	 *	Has History
	 *
	 * @return true (has history)
	 */
	@Override
	boolean hasHistory()
	{
		return true;
	}	//	hasHistory

	/**
	 *	Zoom
	 */
	@Override
	void zoom()
	{
		log.info("");
		Integer M_Product_ID = getSelectedRowKey();
		if (M_Product_ID == null)
			return;
	//	AEnv.zoom(MProduct.Table_ID, M_Product_ID.intValue(), true);	//	SO
		
		Query query = new Query("M_Product");
		query.addRestriction("M_Product_ID", Query.EQUAL, M_Product_ID);
		query.setRecordCount(1);
		int AD_WindowNo = getAD_Window_ID("M_Product", true);	//	SO
		zoom (AD_WindowNo, query);
	}	//	zoom

	/**
	 *	Has Zoom
	 *  @return (has zoom)
	 */
	@Override
	boolean hasZoom()
	{
		return true;
	}	//	hasZoom

	/**
	 *	Customize
	 */
	@Override
	void customize()
	{
		log.info("");
	}	//	customize

	/**
	 *	Has Customize
	 *  @return false (no customize)
	 */
	@Override
	boolean hasCustomize()
	{
		return false;	//	for now
	}	//	hasCustomize

	/**
	 *	Save Selection Settings for PriceList
	 */
	@Override
	void saveSelectionDetail()
	{
		Ctx ctx = Env.getCtx();
		//  publish for Callout to read
		Integer ID = getSelectedRowKey();
		ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID", ID == null ? "0" : ID.toString());
		KeyNamePair kn = (KeyNamePair)pickPriceList.getSelectedItem();
		ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_PriceList_Version_ID", kn.getID());
		kn = (KeyNamePair)pickWarehouse.getSelectedItem();
		ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Warehouse_ID", kn.getID());
		//
		if (m_M_AttributeSetInstance_ID == -1)	//	not selected
		{
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID", "0");
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Locator_ID", "0");
		}
		else
		{
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID", 
				String.valueOf(m_M_AttributeSetInstance_ID));
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Locator_ID", 
				String.valueOf(m_M_Locator_ID));
		}
	}	//	saveSelectionDetail

	/**
	 *  Get Product Layout
	 * @return array of Column_Info
	 */
	@Override
	protected Info_Column[] getInfoColumns()
	{
		if (s_productLayout != null)
			return s_productLayout;
		//
		s_headerPriceList = Msg.translate(Env.getCtx(), "M_PriceList_Version_ID");
		s_headerWarehouse = Msg.translate(Env.getCtx(), "M_Warehouse_ID"); 

		//  Euro 13
		MClient client = MClient.get(Env.getCtx());
		if ("FRIE".equals(client.getValue()))
		{
			final Info_Column[] frieLayout = {
				new Info_Column(" ", "p.M_Product_ID", IDColumn.class).seq(10),
				new Info_Column(Msg.translate(Env.getCtx(), "Name"), "p.Name", String.class).seq(20),
				new Info_Column(s_headerWarehouse, "w.Name", String.class).seq(30),
				new Info_Column(Msg.translate(Env.getCtx(), "QtyAvailable"), 
					"bomQtyAvailable(p.M_Product_ID,w.M_Warehouse_ID,0) AS QtyAvailable", Double.class, true, true, null).seq(40),
				new Info_Column(s_headerPriceList, "plv.Name", String.class).seq(50),
				new Info_Column(Msg.translate(Env.getCtx(), "PriceList"), 
					"bomPriceList(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceList",  BigDecimal.class).seq(60),
				new Info_Column(Msg.translate(Env.getCtx(), "PriceStd"), 
					"bomPriceStd(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceStd", BigDecimal.class).seq(70),
				new Info_Column("Einzel MWSt", 
					"pr.PriceStd * 1.19", BigDecimal.class).seq(80),
				new Info_Column("Einzel kompl", 
					"(pr.PriceStd+13) * 1.19", BigDecimal.class).seq(90),
				new Info_Column("Satz kompl", 
					"((pr.PriceStd+13) * 1.19) * 4", BigDecimal.class).seq(100),
				new Info_Column(Msg.translate(Env.getCtx(), "QtyOnHand"), 
					"bomQtyOnHand(p.M_Product_ID,w.M_Warehouse_ID,0) AS QtyOnHand", Double.class).seq(110),
				new Info_Column(Msg.translate(Env.getCtx(), "QtyReserved"), 
					"bomQtyReserved(p.M_Product_ID,w.M_Warehouse_ID,0) AS QtyReserved", Double.class).seq(120),
				new Info_Column(Msg.translate(Env.getCtx(), "QtyOrdered"), 
					"bomQtyOrdered(p.M_Product_ID,w.M_Warehouse_ID,0) AS QtyOrdered", Double.class).seq(130),
				new Info_Column(Msg.translate(Env.getCtx(), "Discontinued").substring(0, 1), 
					"p.Discontinued", Boolean.class).seq(140),
				new Info_Column(Msg.translate(Env.getCtx(), "Margin"), 
					"bomPriceStd(p.M_Product_ID, pr.M_PriceList_Version_ID)-bomPriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS Margin", BigDecimal.class).seq(150),
				new Info_Column(Msg.translate(Env.getCtx(), "PriceLimit"), 
					"bomPriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceLimit", BigDecimal.class).seq(160),
				new Info_Column(Msg.translate(Env.getCtx(), "IsInstanceAttribute"), 
					"pa.IsInstanceAttribute", Boolean.class).seq(170)
			};
			INDEX_NAME = 1;
			INDEX_PATTRIBUTE = frieLayout.length - 1;	//	last item
			s_productLayout = frieLayout; 
			return s_productLayout;
		}
		//
		if (s_productLayout == null)
		{
			ArrayList<Info_Column> list = new ArrayList<Info_Column>();
			list.add(new Info_Column(" ", 
				"p.M_Product_ID", 
				IDColumn.class).seq(10));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "Discontinued").substring(0, 1), 
				"p.Discontinued", 
				Boolean.class).seq(20));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "Value"), 
				"p.Value", 
				String.class).seq(30));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "Name"), 
				"p.Name", 
				String.class).seq(40));
			list.add(new Info_Column(s_headerWarehouse, 
				"w.Name", 
				String.class).seq(50));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "QtyAvailable"), 
				"bomQtyAvailable(p.M_Product_ID,w.M_Warehouse_ID,0) AS QtyAvailable", 
				Double.class, true, true, null).seq(60));
			list.add(new Info_Column(s_headerPriceList, 
				"plv.Name", 
				String.class).seq(70));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "PriceList"), 
				"bomPriceList(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceList",  
				BigDecimal.class).seq(80));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "PriceStd"), 
				"bomPriceStd(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceStd", 
				BigDecimal.class).seq(90));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "QtyOnHand"), 
				"bomQtyOnHand(p.M_Product_ID,w.M_Warehouse_ID,0) AS QtyOnHand", 
				Double.class).seq(100));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "QtyReserved"), 
				"bomQtyReserved(p.M_Product_ID,w.M_Warehouse_ID,0) AS QtyReserved", 
				Double.class).seq(110));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "QtyOrdered"), 
				"bomQtyOrdered(p.M_Product_ID,w.M_Warehouse_ID,0) AS QtyOrdered", 
				Double.class).seq(120));
			if (isUnconfirmed())
			{
				list.add(new Info_Column(Msg.translate(Env.getCtx(), "QtyUnconfirmed"), 
					"(SELECT SUM(c.TargetQty) FROM M_InOutLineConfirm c INNER JOIN M_InOutLine il ON (c.M_InOutLine_ID=il.M_InOutLine_ID) INNER JOIN M_InOut i ON (il.M_InOut_ID=i.M_InOut_ID) WHERE c.Processed='N' AND i.M_Warehouse_ID=w.M_Warehouse_ID AND il.M_Product_ID=p.M_Product_ID) AS QtyUnconfirmed", 
					Double.class).seq(130));
				list.add(new Info_Column(Msg.translate(Env.getCtx(), "QtyUnconfirmedMove"), 
					"(SELECT SUM(c.TargetQty) FROM M_MovementLineConfirm c INNER JOIN M_MovementLine ml ON (c.M_MovementLine_ID=ml.M_MovementLine_ID) INNER JOIN M_Locator l ON (ml.M_LocatorTo_ID=l.M_Locator_ID) WHERE c.Processed='N' AND l.M_Warehouse_ID=w.M_Warehouse_ID AND ml.M_Product_ID=p.M_Product_ID) AS QtyUnconfirmedMove", 
					Double.class).seq(140));
			}
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "Margin"), 
				"bomPriceStd(p.M_Product_ID, pr.M_PriceList_Version_ID)-bomPriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS Margin", 
				BigDecimal.class).seq(150));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "PriceLimit"), 
				"bomPriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceLimit", 
				BigDecimal.class).seq(160));
			list.add(new Info_Column(Msg.translate(Env.getCtx(), "IsInstanceAttribute"), 
				"pa.IsInstanceAttribute", 
				Boolean.class).seq(170));
			s_productLayout = new Info_Column[list.size()];
			list.toArray(s_productLayout);
			INDEX_NAME = 3;
			INDEX_PATTRIBUTE = s_productLayout.length - 1;	//	last item
		}
		return s_productLayout;
	}   //  getProductLayout
	
	/**
	 * 	System has Unforfirmed records
	 *	@return true if unconfirmed
	 */
	private boolean isUnconfirmed()
	{
		int no = QueryUtil.getSQLValue(null, 
			"SELECT COUNT(*) FROM M_InOutLineConfirm WHERE AD_Client_ID=?", 
			Env.getCtx().getAD_Client_ID());
		if (no > 0)
			return true;
		no = QueryUtil.getSQLValue(null, 
			"SELECT COUNT(*) FROM M_MovementLineConfirm WHERE AD_Client_ID=?", 
			Env.getCtx().getAD_Client_ID());
		return no > 0;
	}	//	isUnconfirmed

}	//	InfoProduct
