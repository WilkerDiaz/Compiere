package compiere.model.cds.forms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.logging.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;

import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

import compiere.model.cds.MOrder;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.processes.XX_PercentualDistribution;

import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.border.TitledBorder;

/**
 * @author Javier Pino
 * @version
 */

public class XX_DistributionOCReplacement_Form extends CPanel implements
		FormPanel, ActionListener, ListSelectionListener {

	private class XX_WarehouseInfo {
		int inventory = 0;
		int budget = 0;
		int initial_pieces = 0 ;
		int current_pieces = 0;
	}

	private Hashtable<Integer, XX_WarehouseInfo> warehouses_info 
		= new Hashtable<Integer, XX_WarehouseInfo>();

	private X_XX_VMR_DistributionHeader header = null;
	private MOrder header_order = null;
	private Trx trans = null;
	private double total_oc = 0;
	int position_cd = -1;

	private static final long serialVersionUID = 1L;

	/**
	 * Initialize Panel
	 * 
	 * @param WindowNo
	 *            window
	 * @param frame
	 *            frame
	 */
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo + " - AD_Client_ID=" + m_AD_Client_ID
				+ ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);

		Ctx aux = Env.getCtx();
		int header_id = aux
				.getContextAsInt("#XX_VMR_Replacement_DistributionHeader_ID");
		trans = Trx.get("XX_CHANGE_PO_REPLACEMENT");
		// Remove the no longer necessary items on the context
		aux.remove("XX_VMR_Replacement_DistributionHeader_ID");

		// Creates the Header with the necessary information
		header = new X_XX_VMR_DistributionHeader(aux, header_id, trans);
		header_order = new MOrder(aux, header.getC_Order_ID(), trans);

		try {
			// UI
			jbInit();
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);

		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	} // init

	/** Window No */
	private int m_WindowNo = 0;
	/** FormFrame */
	private FormFrame m_frame;
	/** Logger */
	static CLogger log = CLogger.getCLogger(XX_DistributionOCSales_Form.class);

	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();

	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CButton calculate = new CButton();
	private CButton generate = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();

	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5, 5);
	private JScrollPane xProductScrollPane = new JScrollPane();
	private TitledBorder xProductBorder = new TitledBorder(Msg.translate(Env
			.getCtx(), "XX_StoreProductDistribution"));
	private MiniTable xProductTable = new MiniTable();
	private CPanel xPanel = new CPanel();

	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);

	/**
	 * Static Init.
	 * 
	 * <pre>
	 *  mainPanel
	 *      northPanel
	 *      centerPanel
	 *          xMatched
	 *          xPanel
	 *          xMathedTo
	 *      southPanel
	 * </pre>
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);

		calculate.setText(Msg.translate(Env.getCtx(),
				"XX_CalculateDistribution"));
		calculate.setEnabled(true);

		southPanel.setLayout(southLayout);
		generate.setText(Msg.translate(Env.getCtx(), "XX_GeneratePieces"));
		generate.setEnabled(true);

		centerPanel.setLayout(centerLayout);
		xProductScrollPane.setBorder(xProductBorder);
		xProductScrollPane.setPreferredSize(new Dimension(470, 250));

		xPanel.setLayout(xLayout);

		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xProductScrollPane, BorderLayout.CENTER);
		xProductScrollPane.getViewport().add(xProductTable, null);

		southPanel.add(calculate, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						12, 5, 12), 0, 0));
		southPanel.add(generate, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						12, 5, 12), 0, 0));
	} // jbInit

	/**
	 * Dynamic Init. Table Layout, Visual, Listener
	 */
	private void dynInit() {
		// Add the column definition for the table
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), ""), "",
						Boolean.class, false, false, ""),// 1
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Store"), ".",
						KeyNamePair.class), // 2
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AssignedQTY"),
						".", Integer.class), // 2
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Chart"), ".",
						Double.class), // 2

		};
		xProductTable.prepareTable(layout, "", "", false, "");
		// Visual
		CompiereColor.setBackground(this);

		// Listener
		xProductTable.getSelectionModel().addListSelectionListener(this);
		xProductTable.getColumnModel().getColumn(1).setPreferredWidth(130);
		calculate.addActionListener(this);
		generate.addActionListener(this);

		// Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);

		TableModel modelProduct = xProductTable.getModel();
		Object[][] data = dataWarehouse();
		if (data != null) {
			for (int i = 0; i < warehouses_info.size(); i++) {
				xProductTable.setRowCount(i + 1);
				modelProduct.setValueAt(data[i][0], i, 0);
				modelProduct.setValueAt(data[i][1], i, 1);
				modelProduct.setValueAt(data[i][2], i, 2);
				modelProduct.setValueAt(data[i][3], i, 3);
			}
			xProductTable.getColumnModel().getColumn(1).setPreferredWidth(220);			
			XX_IndicatorCellRenderer renderer = new XX_IndicatorCellRenderer(0, 100);
	        renderer.setStringPainted(false);

	        // set limit value and fill color
	        Hashtable<Integer, Color> limitColors = new Hashtable<Integer, Color>();
	        	limitColors.put(new Integer(0), Color.green);
	        	limitColors.put(new Integer(60), Color.yellow);
	        	limitColors.put(new Integer(80), Color.red);
	        renderer.setLimits(limitColors);
	        
	        xProductTable.setSortEnabled(false);
	        xProductTable.getTableHeader().setFocusable(false);
	        xProductTable.getColumnModel().getColumn(3).setCellRenderer(renderer);
	        xProductTable.autoSize();
		}
	} // dynInit

	private Object[][] dataWarehouse() {

		Object[][] data = null;
		double total_budget = 0;
		double total_inventory = 0.0;

		X_XX_VMR_Department order_department = new X_XX_VMR_Department(Env
				.getCtx(), header_order.getXX_VMR_DEPARTMENT_ID(), trans);

		Calendar actualDate = Calendar.getInstance();
		int year = actualDate.get(Calendar.YEAR);
		int month = actualDate.get(Calendar.MONTH) + 1;
		String yearmonth = "";
		if (month < 10)
			yearmonth = year + "0" + month;
		else
			yearmonth = "" + year + month;

		//Jorge Pires - Ordenar tiendas!
		// ***********The Distinct SQL String to be used
		String sql_warehouses = "SELECT M_WAREHOUSE_ID, VALUE||'-'||NAME FROM M_WAREHOUSE WHERE ISACTIVE = 'Y' ";
		sql_warehouses = MRole.getDefault().addAccessSQL(
			sql_warehouses.toString(), "", MRole.SQL_NOTQUALIFIED,
			MRole.SQL_RO)
				+ " ORDER BY VALUE";
		System.out.println(sql_warehouses);

		// ***********The Store Inventory
		String sql_inventory = "SELECT M_WAREHOUSE_ID AS \"XX_STORE\", SUM(IV.XX_INITIALINVENTORYQUANTITY + IV.XX_SHOPPINGQUANTITY - IV.XX_SALESQUANTITY +"
				+ " IV.XX_MOVEMENTQUANTITY + IV.XX_ADJUSTMENTSQUANTITY) AS \"QUANTITY\" FROM XX_VCN_INVENTORY IV "
				+ " WHERE IV.XX_VMR_DEPARTMENT_ID =  "
				+ order_department.get_ID();
		sql_inventory = MRole.getDefault().addAccessSQL(sql_inventory, "IV",
				MRole.SQL_NOTQUALIFIED, MRole.SQL_RO)
				+ " GROUP BY M_WAREHOUSE_ID";

		//System.out.println(sql_inventory);

		// ********** The Purchase Order Qty
		String sql_po_qty = "SELECT COALESCE(XX_PRODUCTQUANTITY, 0) "
				+ " FROM C_ORDER PO WHERE PO.C_ORDER_ID = "
				+ header.getC_Order_ID();

		// **********The Store Budget
		String sql_budget = "SELECT IP.M_WAREHOUSE_ID, SUM(IP.XX_SALESAMOUNTBUD2) "
				+ " FROM XX_VMR_PRLD01 IP ";
		sql_budget = MRole.getDefault().addAccessSQL(sql_budget, "IP",
				MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);

		sql_budget += " AND IP.XX_BUDGETYEARMONTH = " + yearmonth
				+ " AND IP.XX_VMR_DEPARTMENT_ID = " + order_department.get_ID()
				+ " GROUP BY IP.M_WAREHOUSE_ID";
		//System.out.println(sql_budget);

		PreparedStatement pstmt_warehouses = null;
		ResultSet rs_warehouses = null;
		PreparedStatement pstmt_inventory = null;
		ResultSet rs_inventory = null;
		PreparedStatement pstmt_po_qty = null;
		ResultSet rs_po_qty = null;
		PreparedStatement pstmt_budget = null;
		ResultSet rs_budget = null;
		try {
			// Going through the warehouses
			Vector<KeyNamePair> warehouses = new Vector<KeyNamePair>();
			pstmt_warehouses = DB.prepareStatement(
					sql_warehouses, null);
			rs_warehouses = pstmt_warehouses.executeQuery();
			int iterations = 0;
			while (rs_warehouses.next()) {
				XX_WarehouseInfo warehouse = new XX_WarehouseInfo();
				warehouses_info.put(rs_warehouses.getInt(1), warehouse);
				if (rs_warehouses.getInt(1) == Env.getCtx().getContextAsInt(
						"#XX_L_WAREHOUSECENTRODIST_ID")) {
					position_cd = iterations;
				}
				warehouses.add(new KeyNamePair(rs_warehouses.getInt(1),
						rs_warehouses.getString(2)));
				iterations++;
			}
			data = new Object[warehouses.size()][4];
			for (int i = 0; i < warehouses.size(); i++) {
				data[i][0] = new Boolean(true);
				data[i][1] = warehouses.elementAt(i);
				data[i][2] = 0;
				data[i][3] = 0;
			}
			data[position_cd][0] = false;			
			DB.closeResultSet(rs_warehouses);
			DB.closeStatement(pstmt_warehouses);

			// Fetching the inventory
			pstmt_inventory = null;
			rs_inventory = null;
			while (rs_inventory.next()) {
				if (rs_inventory.getInt("XX_STORE") == 
					Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID")) {
					continue;
				}
				if (warehouses_info
						.containsKey(rs_inventory.getInt("XX_STORE"))) {
					XX_WarehouseInfo rs_warehouse = warehouses_info
							.get(rs_inventory.getInt("XX_STORE"));
					rs_warehouse.inventory = rs_inventory
								.getInt("QUANTITY");
					total_inventory += rs_inventory.getDouble("QUANTITY");					
				}
			}
			System.out.println("total i" + total_inventory);
			DB.closeResultSet(rs_inventory);
			DB.closeStatement(pstmt_inventory);

			// Fetching the OC Qty
			pstmt_po_qty = DB.prepareStatement(sql_po_qty,
					null);
			rs_po_qty = pstmt_po_qty.executeQuery();
			if (rs_po_qty.next()) {
				total_oc = rs_po_qty.getDouble(1);
			}
			// Fetching the budgets
			pstmt_budget = DB.prepareStatement(sql_budget,
					null);
			rs_budget = pstmt_budget.executeQuery();
			while (rs_budget.next()) {
				if (rs_budget.getInt(1) == 
					Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID")) {
					continue;
				}				
				if (warehouses_info.containsKey(rs_budget.getInt(1))) {
					XX_WarehouseInfo rs_warehouse = warehouses_info
							.get(rs_budget.getInt(1));
					rs_warehouse.budget = rs_budget.getInt(2);
						total_budget += rs_budget.getDouble(2);					
				}
			}
			System.out.println("total b" + total_budget);
			DB.closeResultSet(rs_budget);
			DB.closeStatement(pstmt_budget);

			// The Store Budgets does not allow
			if (total_budget == 0) {
				ADialog.error(m_WindowNo, m_frame, "XX_TotalBudgetZero");
				return null;
			}
			total_inventory += total_oc;
			System.out.println("total i" + total_inventory);
		
			double positive_amount = 0;
			for (int j = 0; j < warehouses.size(); j++) {
				KeyNamePair warehouse = (KeyNamePair) data[j][1];
				XX_WarehouseInfo temporal = warehouses_info.get(warehouse
						.getKey());
				double percentage = 0;
				percentage = temporal.budget/total_budget;
				Double qty = Math.floor(total_inventory * percentage);
				qty -= temporal.inventory;
				
				temporal.initial_pieces = qty.intValue();
				if (temporal.initial_pieces < 0) {					
					data[j][2] = 0;						
				} 	else {					
					data[j][2] = temporal.initial_pieces;
					positive_amount += temporal.initial_pieces;
				}				
			}
			if (total_oc > positive_amount) {
				Double dif = total_oc - positive_amount;
				data[position_cd][2] = dif.intValue();
				data[position_cd][0] = true;
				for (int j = 0; j < warehouses.size(); j++) { 
					Integer qty = (Integer)data[j][2];
					data[j][3] = (qty / total_oc)*100;					
				}					
			} else if (total_oc == positive_amount) {
				for (int j = 0; j < warehouses.size(); j++) { 
					Integer qty = (Integer)data[j][2];
					data[j][3] = (qty / total_oc) *100;					
				}				
			} else {				
				double downQty = 0;
				for (int j = 0; j < warehouses.size(); j++) {
					if (j == position_cd) continue;
					KeyNamePair warehouse = (KeyNamePair) data[j][1];
					XX_WarehouseInfo temporal = warehouses_info.get(warehouse.getKey());
					if (temporal.initial_pieces > 0) {						
						Double percentage = temporal.initial_pieces/positive_amount;
						percentage *= total_oc;
						Long qt = Math.round(percentage);
						if (qt.intValue() < temporal.initial_pieces ) {
							data[j][2] = qt.intValue();
							downQty += qt.intValue();
						} else downQty += temporal.initial_pieces;
					}				
				}
				if (total_oc > downQty) {	
					Double dif = total_oc - downQty;
					data[position_cd][2] = dif.intValue();
					data[position_cd][0] = true;			
				}
				for (int j = 0; j < warehouses.size(); j++) { 
					Integer qty = (Integer)data[j][2];
					data[j][3] = (qty / total_oc)*100;	
				}				
			}			
		} catch (Exception E) {
			E.printStackTrace();
		} finally {
			DB.closeStatement(pstmt_warehouses);
			DB.closeResultSet(rs_warehouses);
			DB.closeStatement(pstmt_inventory);
			DB.closeResultSet(rs_inventory);
			DB.closeStatement(pstmt_po_qty);
			DB.closeResultSet(rs_po_qty);
			DB.closeStatement(pstmt_budget);
			DB.closeResultSet(rs_budget);
		}
		return data;
	}

	// Boton B Generar Distribucion

	public void generateDistribution() {
		Vector<Integer> codes = new Vector<Integer>();
		Vector<BigDecimal> qtys = new Vector<BigDecimal>();

		BigDecimal total_qty = new BigDecimal(0);
		for (int j = 0; j < xProductTable.getRowCount(); j++) {
			total_qty = total_qty.add(			
					new BigDecimal(((Number)xProductTable.getValueAt(j,2)).intValue()));
		}
		if (total_qty.compareTo(Env.ZERO) == 0) {
			ADialog.error(1, new Container(), "XX_SumQtyIsZero");
			return;
		}
		BigDecimal qty = null;
		for (int j = 0; j < xProductTable.getRowCount(); j++) {			
			qty = new BigDecimal(((Number)xProductTable.getValueAt(j,2)).intValue());
			if (qty.compareTo(Env.ZERO) == 1) {
				Integer code = ((KeyNamePair) xProductTable.getValueAt(j, 1))
						.getKey();
				qtys.add(qty);
				codes.add(code);
			}
		}
		System.out.println(qtys.toString());
		System.out.println(codes.toString());
		if (XX_PercentualDistribution.applyPOQuantityDistribution(header, qtys,
				codes, m_WindowNo, m_frame)) {
			dispose();
				}
	}

	// Metodo del Boton A o Calcular Distribución

	public void generatePercentages() {
		double positive_amount = 0;
		for (int j = 0; j < xProductTable.getRowCount(); j++) {
			if (j == position_cd) continue;
			if ((Boolean) xProductTable.getValueAt(j, 0)) {
				KeyNamePair warehouse = (KeyNamePair) xProductTable.getValueAt(j, 1);
				XX_WarehouseInfo temporal = warehouses_info.get(warehouse.getKey());
				if (temporal.initial_pieces > 0) {
					positive_amount += temporal.initial_pieces;
					xProductTable.setValueAt(temporal.initial_pieces, j, 2);
				} else xProductTable.setValueAt(0, j, 2); 				
			} else {				
				xProductTable.setValueAt(0, j, 2);
			}				
		}
		if (total_oc > positive_amount) {
			Double dif = total_oc - positive_amount;
			xProductTable.setValueAt(dif, position_cd, 2);
			xProductTable.setValueAt(true, position_cd, 0);
			for (int j = 0; j < xProductTable.getRowCount(); j++) { 
				Integer qty = ((Number)xProductTable.getValueAt(j, 2)).intValue();
				xProductTable.setValueAt((qty / total_oc)*100, j, 3);					
			}					
		} else if (total_oc == positive_amount) {
				for (int j = 0; j < xProductTable.getRowCount(); j++) { 
					Integer qty = ((Number)xProductTable.getValueAt(j, 2)).intValue();
					xProductTable.setValueAt((qty / total_oc)*100, j, 3);					
				}									
		} else {				
			double downQty = 0;
			for (int j = 0; j < xProductTable.getRowCount(); j++) {
					if (j == position_cd) continue;
					if ((Boolean) xProductTable.getValueAt(j, 0)) {
						KeyNamePair warehouse = (KeyNamePair) xProductTable.getValueAt(j, 1);
						XX_WarehouseInfo temporal = warehouses_info.get(warehouse.getKey());
						if (temporal.initial_pieces > 0) {						
							Double percentage = temporal.initial_pieces/positive_amount;
							percentage *= total_oc;
							Long qt = Math.round(percentage);							
							if (qt.intValue() < temporal.initial_pieces ) {							
								downQty += qt.intValue();							
								xProductTable.setValueAt(qt.intValue(), j, 2);							
							} else { 
								downQty += temporal.initial_pieces;							
							}
						}
					}
			}		
			if (total_oc == downQty) {
				xProductTable.setValueAt(0, position_cd, 2);
				xProductTable.setValueAt(false, position_cd, 0);				
			}			
			if (total_oc > downQty) {	
				Double dif = total_oc - downQty;				
				xProductTable.setValueAt(dif.intValue(), position_cd, 2);
				xProductTable.setValueAt(true, position_cd, 0);
			}
			for (int j = 0; j <  xProductTable.getRowCount(); j++) { 				
				Integer qty = ((Number)xProductTable.getValueAt(j, 2)).intValue();
				xProductTable.setValueAt((qty / total_oc)*100, j, 3);				
			}				
		}
	}

	/**
	 * Dispose
	 */
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	} // dispose

	/**
	 * Action Listener
	 * 
	 * @param e
	 *            event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == calculate)
			generatePercentages();
		else if (e.getSource() == generate)
			generateDistribution();
	} // actionPerformed

	/**************************************************************************
	 * List Selection Listener
	 * 
	 * @param e
	 *            event
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
	} // valueChanged

}
