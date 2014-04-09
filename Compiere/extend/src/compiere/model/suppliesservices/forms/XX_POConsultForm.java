package compiere.model.suppliesservices.forms;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.*;
import org.compiere.framework.Lookup;
import org.compiere.framework.Query;
import org.compiere.grid.GridController;
import org.compiere.grid.VTabbedPane;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.apps.search.Info;

public class XX_POConsultForm extends CPanel implements FormPanel, ActionListener, 
		ListSelectionListener {

	private static final long serialVersionUID = 1L;
	/**
	 * Initialize Panel
	 * @param WindowNo window
	 * @param frame frame
	 */
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo + " - AD_Client_ID=" + m_AD_Client_ID
				+ ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		try {
			// Se requiere un keynamepair para traer el id de la ventana
			Lookup l = MLookupFactory.get(Env.getCtx(), m_WindowNo, Env
					.getCtx().getContextAsInt("#XX_L_C_ORDERCOLUMN_ID"),
					DisplayTypeConstants.Search);
			//purchaseSearch = new VLookup("C_Order_ID", false, false, true, l);
			jbInit();
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		} // try
		catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	} // Fin init

	/** Window No */
	private int m_WindowNo = 0;
	/** FormFrame */
	private FormFrame m_frame;
	/** Logger */
	static CLogger log = CLogger.getCLogger(XX_POConsultForm.class);
	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();
	/** Model Index of Key Column */
	private int m_keyColumnIndex = -1;
	/**	PO Zoom Window */
	private int m_PO_Window_ID = -1;
	private Vector<String> status_name = new Vector<String>();
	private Vector<String> type_name = new Vector<String>();
	private final int h_id = 0, h_doc = 1, h_asset = 2, h_type = 3,
	h_center = 4, h_bp = 5, h_cost = 6, h_status = 7, h_date = 8;
	//Panel
	private CPanel mainPanel = new CPanel();
	private CPanel northPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	//Label
	private CLabel datefromlabel = new CLabel();
	private CLabel datetolabel = new CLabel();
	private CLabel docTypeLabel = new CLabel();
	private CLabel assetsTypeLabel = new CLabel();
	private CLabel costCenterLabel = new CLabel();
	private CLabel bPLabel = new CLabel();
	private CLabel orderTypeLabel = new CLabel();
	private CLabel statusLabel = new CLabel();
	private CLabel purchaseLabel = new CLabel();
	//CComboBox
	private CComboBox docTypeCombo = new CComboBox();
	private CComboBox assetsCombo = new CComboBox();
	private CComboBox centerCombo = new CComboBox();
	private CComboBox bpCombo = new CComboBox();
	private CComboBox orderCombo = new CComboBox();
	private CComboBox statusCombo = new CComboBox();
	//StatusBar
	private StatusBar statusBar = new StatusBar();
	//BorderLayout	
	private BorderLayout mainLayout = new BorderLayout();
	//GridBagLayout
	private GridBagLayout northLayout = new GridBagLayout();
	private GridBagLayout southLayout = new GridBagLayout();
	//CButton
	private CButton bSearch = new CButton();
	private CButton bProcess = new CButton();
	private CButton bPurchase = new CButton(Msg.translate(Env.getCtx(), "Zoom"));//zoom
	// VLookup
	private VLookup purchaseSearch = null;
	//TitledBorder
	private TitledBorder xMatchedBorder = new TitledBorder("xMatched");
	private TitledBorder xMatchedToBorder = new TitledBorder("xMatchedTo");
	//MiniTable
	private MiniTable xTableHeader = new MiniTable();
	// JScrollPane
	private JScrollPane xMatchedScrollPane = new JScrollPane();
	private JScrollPane xMatchedToScrollPane = new JScrollPane();
	// CTextField
	private CTextField placedOrderText = new CTextField();
	private CTextField TotalPOText = new CTextField("Total: ");
	private CTextField placedOrderValueText = new CTextField();
	// BoxLayout
	private BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
	// VDate (From, To)
	private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
			.getCtx(), "DateFrom"));
	private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
			.getCtx(), "DateTo"));

	// Document Type
	void dynDocType() {
		docTypeCombo.removeActionListener(this);
		String sql = " SELECT Distinct L.AD_Ref_List_ID, T.Name  " +
					" From AD_Ref_List L Join AD_Reference R " +
					" On (R.AD_Reference_ID = L.AD_Reference_ID) "+
					" join AD_Ref_List_Trl T " +
					" On (L.AD_Ref_List_ID = T.AD_Ref_List_ID)" +
					" Join C_Order C on (C.XX_POType = L.value) ";
		sql = MRole.getDefault().addAccessSQL(sql, "R", MRole.SQL_FULLYQUALIFIED,
					MRole.SQL_RO);
		sql += " ORDER BY T.Name ";
		//System.out.println("Assets: "+sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			docTypeCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				docTypeCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			docTypeCombo.addActionListener(this);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}//dynDocType
	
	// Order Type
	void dynAssetsType() {
		assetsCombo.removeActionListener(this);
		String sql = " SELECT Distinct L.AD_Ref_List_ID, T.Name  " +
					" From AD_Ref_List L Join AD_Reference R " + 
					" On (R.AD_Reference_ID = L.AD_Reference_ID) " +
					" join AD_Ref_List_Trl T " +
					" On (L.AD_Ref_List_ID = T.AD_Ref_List_ID)" +
					" Join C_Order C on (C.XX_PurchaseType = L.value) ";
		sql = MRole.getDefault().addAccessSQL(sql, "R", MRole.SQL_FULLYQUALIFIED,
					MRole.SQL_RO);
		sql += " ORDER BY T.Name ";
		//System.out.println("Assets: "+sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			assetsCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				assetsCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			assetsCombo.addActionListener(this);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//dynAssetsType
	
	// Warehouse
	void dynWarehouse() {
		centerCombo.removeActionListener(this);
		String sql = " SELECT M_WAREHOUSE_ID, NAME " +
				"FROM M_WAREHOUSE ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		sql += " ORDER BY NAME ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			centerCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				centerCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			centerCombo.addActionListener(this);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//dynWarehouse
	
	// Business Partner
	void dynBP() {
		bpCombo.removeActionListener(this);
		String sql = " SELECT C_BPartner_ID, NAME " +
				"FROM C_BPartner ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		sql += " AND IsVendor = 'Y' ";
		sql += " ORDER BY NAME ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			bpCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				bpCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			bpCombo.addActionListener(this);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//dynBP
	
	// Order Status
	void dynStatus() {
		statusCombo.removeActionListener(this);
		String sql = " SELECT Distinct L.Name, L.AD_Ref_List_ID  " +
					" From AD_Ref_List L Join AD_Reference R " +
					" On (R.AD_Reference_ID = L.AD_Reference_ID) " +
					" Join C_Order C on (C.XX_ORDERSTATUS= L.value)";
		sql = MRole.getDefault().addAccessSQL(sql, "C", MRole.SQL_FULLYQUALIFIED,
				MRole.SQL_RO);
		sql += " AND C.XX_ORDERSTATUS IS NOT NULL ";
		//System.out.println("Order status: "+sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			statusCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				statusCombo.addItem(new KeyNamePair(rs.getInt(2), rs.getString(1)));
			}
			statusCombo.addActionListener(this);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//dynStatus
	
	// Order Type
	void dynOrderType() {
		orderCombo.removeActionListener(this);
		String sql = " SELECT Distinct L.AD_Ref_List_ID, L.VALUE " +
			" From AD_Ref_List L join AD_Reference R on " +
			"(R.AD_Reference_ID = L.AD_Reference_ID) " +
			" join C_Order C on (C.XX_OrderType = L.value AND isSOTRX='N')";
		sql = MRole.getDefault().addAccessSQL(sql, "R", MRole.SQL_FULLYQUALIFIED,
				MRole.SQL_RO);
		sql += " ORDER BY L.VALUE ";
		//System.out.println("Doc Type: "+sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			orderCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				orderCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			orderCombo.addActionListener(this);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//dynOrderType
	
	private void dynInit() {
		xTableHeader.getTableHeader().setReorderingAllowed(false);
		xTableHeader.autoSize(true);
		java.util.Calendar now = java.util.Calendar.getInstance();
		dateTo.setValue(now.getTime());

		// Add the column definition for the Order
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(" ", ".", IDColumn.class, true, false, ""),
			new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_DocType"), ".",String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AssetType"), ".",String.class, "."),
			new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_POType"), ".",String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_CostCenter"), ".",String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_BusinessPartner"),".", String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TotalCost"), ".",Integer.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_POStatus"), ".", String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "Date"), ".",String.class, ".")
		};//layout.
		xTableHeader.prepareTable(layout, "", "", false, "");
		xTableHeader.getTableHeader().setFocusable(false);
		xTableHeader.setSortEnabled(true);
		xMatchedToScrollPane.repaint();
		xMatchedBorder.setTitle(Msg.translate(Env.getCtx(), "Order"));
		xMatchedScrollPane.repaint();
		// Visual
		CompiereColor.setBackground(this);
		// Listener
		bSearch.addActionListener(this);
		xTableHeader.getSelectionModel().addListSelectionListener(this);
		bProcess.addActionListener(this);
		// Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);
		// Agregar las acciones
		dynDocType();
		dynAssetsType();
		dynWarehouse();
		dynBP();
		dynStatus();
		dynOrderType();
	}//dynInit
	
	// Zoom the PO
	private void cmd_PurchaseOrder(int row){			
		IDColumn ID = (IDColumn)xTableHeader.getValueAt(row, 0);		
		Integer purchase = ID.getRecord_ID();
		//System.out.println("Purchase: "+purchase);
		AWindow window_purchase = new AWindow();
		Query query = Query.getEqualQuery("C_Order_ID", purchase);
		String wind = Env.getCtx().getContext("#XX_L_W_PURCHASE_ID");
		Integer win = Integer.parseInt(wind);
		window_purchase.initWindow(win, query);
		AEnv.showCenterScreen(window_purchase);     	
	}// fin cmd_newVendorProdRef
	
	// dispose
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	} // dispose

	String tableHeaderSearch() {
		String sqlAux = "";
		String POType = "";
		String Assets = "";
		String Type = "";
		String Status = "";
		String TotalPO = "";
		BigDecimal Sum = new BigDecimal(0);
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT DISTINCT ORD.C_Order_ID " +
				" FROM C_Order ORD, C_OrderLine DET " +
				" WHERE ISSOTRX='N' AND ORD.C_ORDER_ID = DET.C_ORDER_ID");

		if (docTypeCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) docTypeCombo.getValue();
			if (sto.getKey() != -1){
				sqlAux = " Select L.Value  " +
					" From AD_Ref_List L Join AD_Reference R " +
					" On (R.AD_Reference_ID = L.AD_Reference_ID) " +
					" Join C_Order C on (C.XX_POType = L.value) " +
					" And L.AD_Ref_List_ID = "+sto.getKey();
				//System.out.println("Tipo de compra: "+sqlAux);
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(sqlAux, null);
					rs = pstmt.executeQuery();
					statusCombo.addItem(new KeyNamePair(-1, null));
					if (rs.next()) {
						POType = rs.getString("value");
						sql.append(" AND ORD.XX_POType = '").append(POType)
						.append("' ");
					}
					statusCombo.addActionListener(this);
				} 
				catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}// if
		}//assetsCombo
		
		if (assetsCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) assetsCombo.getValue();
			if (sto.getKey() != -1){
				sqlAux = " Select L.Value  " +
					" From AD_Ref_List L Join AD_Reference R " +
					" On (R.AD_Reference_ID = L.AD_Reference_ID) " +
					" Join C_Order C on (C.XX_PurchaseType = L.value) " +
					" And L.AD_Ref_List_ID = "+sto.getKey();
				//System.out.println("Assets: "+sqlAux);
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(sqlAux, null);
					rs = pstmt.executeQuery();
					statusCombo.addItem(new KeyNamePair(-1, null));
					if (rs.next()) {
						Assets = rs.getString("value");
						sql.append(" AND ORD.XX_PurchaseType = '").append(Assets)
						.append("' ");
					}
					statusCombo.addActionListener(this);
				} 
				catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}// if
		}//assetsCombo
		if (orderCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) orderCombo.getValue();
			if (sto.getKey() != -1){
				sqlAux = " Select L.Value  " +
				" From AD_Ref_List L Join AD_Reference R " +
				" On (R.AD_Reference_ID = L.AD_Reference_ID) " +
				" Join C_Order C on (C.XX_OrderType = L.value AND isSOTrx='N') " +
				" And L.AD_Ref_List_ID = "+sto.getKey();
				//System.out.println("Sql Tipo: "+sqlAux);
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(sqlAux, null);
					rs = pstmt.executeQuery();
					statusCombo.addItem(new KeyNamePair(-1, null));
					if (rs.next()) {
						Type = rs.getString("value");
						sql.append(" AND ORD.XX_OrderType = '").append(Type)
						.append("' ");
					}
					statusCombo.addActionListener(this);
				} 
				catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}//if
		}//orderCombo
		if (centerCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) centerCombo.getValue();
			if (sto.getKey() != -1)
				sql.append(" AND ORD.M_Warehouse_ID = ").append(sto.getKey())
					.append(" ");
		}//centerCombo
		if (bpCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) bpCombo.getValue();
			if (sto.getKey() != -1)
				sql.append(" AND ORD.C_BPartner_ID = ").append(sto.getKey())
					.append(" ");
		}//bpCombo 
		if (statusCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) statusCombo.getValue();
			if (sto.getKey() != -1){
				sqlAux = " Select L.Value  " +
				" From AD_Ref_List L Join AD_Reference R " +
				" On (R.AD_Reference_ID = L.AD_Reference_ID) " +
				" Join C_Order C on (C.XX_OrderStatus = L.value) " +
				" And L.AD_Ref_List_ID = "+sto.getKey();
				//System.out.println("Sql Status: "+sqlAux);
				PreparedStatement pstmt = null;
				ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sqlAux, null);
				rs = pstmt.executeQuery();
				statusCombo.addItem(new KeyNamePair(-1, null));
				if (rs.next()) {
					Status = rs.getString("value");
					sql.append(" AND ORD.XX_OrderStatus = '").append(Status)
					.append("' ");
				}
				statusCombo.addActionListener(this);
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
				
			}// if
				
				/*sql.append(" AND ORD.XX_OrderStatus = '").append(sto.getName())
					.append("' ");*/
		}//statusCombo
		
		// Date's From and To
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		if (from != null && to != null) {
			sql.append(" AND TRUNC(ORD.CREATED) ").append(" >= ").append(
				DB.TO_DATE(from)).append(" ");
			sql.append(" AND TRUNC(ORD.CREATED) ").append(" <= ").append(
				DB.TO_DATE(to)).append(" ");
		}//from  
		else if (from != null)
			sql.append(" AND TRUNC(ORD.CREATED) ").append(" >= ").append(
					DB.TO_DATE(from, true)).append(" ");
		else if (to != null)
			sql.append(" AND TRUNC(ORD.CREATED) ").append(" <= ").append(
				DB.TO_DATE(to)).append(" ");
		System.out.println("SQL: "+sql);
		// Query mayor
		String sql_mayor = " SELECT O.C_ORDER_ID, "+ 				//1
					" O.XX_POTYPE,"+								//2
					" O.XX_PURCHASETYPE,"+ 							//3
					" O.XX_ORDERTYPE,"+								//4
					" W.M_WAREHOUSE_ID,"+							//5
					" W.DESCRIPTION, "+								//6
					" B.NAME,"+										//7
					" O.GRANDTOTAL,"+								//8
					" O.XX_ORDERSTATUS,"+							//9
					" TO_CHAR(O.CREATED, 'DD/MM/YYYY')"+			//10
					" FROM C_ORDER O JOIN C_BPARTNER B ON (O.C_BPARTNER_ID=B.C_BPARTNER_ID) "+
					" JOIN M_WAREHOUSE W ON (O.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID) " 
					/*+ " JOIN C_DOCTYPE C ON (O.C_DOCTYPE_ID = C.C_DOCTYPE_ID) "*/;
		sql_mayor = MRole.getDefault().addAccessSQL(sql_mayor, "O",
				MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		sql_mayor += " AND O.C_ORDER_ID IN ( " + sql.toString() + " )" 
				+" ORDER BY O.C_ORDER_ID ";
		
		TotalPO = "SELECT SUM(O.GRANDTOTAL) SUM" +
				" FROM C_ORDER O" +
				" WHERE O.C_ORDER_ID IN ( " + sql.toString() + " )";
		//System.out.println("TotalPO: "+TotalPO);
		PreparedStatement pstmt9 = null;
		ResultSet rs9 = null;
		try {
			pstmt9 = DB.prepareStatement(TotalPO, null);
			rs9 = pstmt9.executeQuery();
			while (rs9.next()) {
				Sum = rs9.getBigDecimal("SUM");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				rs9.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt9.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		statusBar.setStatusLine("TotalPO: "+Sum.toString());
		//System.out.println("SQL MAYOR: "+sql_mayor);
		return sql_mayor;
	}// Fin tableHeaderSearch

	void fillTableHeader(String sql) {
		// LLenar la tabla
		xTableHeader.setRowCount(0);
		PreparedStatement ps = DB.prepareStatement(sql, null);
		try {
			ResultSet rs = ps.executeQuery();
			int row = 0;
			while (rs.next()) {
				xTableHeader.setRowCount(row + 1);
				IDColumn id = new IDColumn(rs.getInt(1));
				xTableHeader.setValueAt(id, row, h_id);						//1
				if(rs.getString(2).equals("POA")){
					xTableHeader.setValueAt("Orden de Compra " + 
							"Bienes y Servicios", row, h_doc);				//2
				} 
				else {
					xTableHeader.setValueAt("Orden de Compra " + 
							"Mercancía", row, h_doc);						//2
				}
				if(rs.getString(3).equals("SE")){
					xTableHeader.setValueAt("Servicios", row, h_asset);		//3
				}
				else if(rs.getString(3).equals("FA")){
					xTableHeader.setValueAt("Activos Fijos", row, h_asset);	//3
				}
				else if (rs.getString(3).equals("SU")){
					xTableHeader.setValueAt("Suministros y " + 
							"Materiales ", row, h_asset);					//3
				}
				xTableHeader.setValueAt(rs.getString(4), row, h_type);		//4
				xTableHeader.setValueAt(new KeyNamePair(rs.getInt(5), rs	//5
						.getString(6)), row, h_center);						//6
				xTableHeader.setValueAt(rs.getString(7), row, h_bp);		//7
				xTableHeader.setValueAt(rs.getBigDecimal(8), row, h_cost);	//8
				xTableHeader.setValueAt(rs.getString(9), row, h_status);	//9
				xTableHeader.setValueAt(rs.getString(10), row, h_date);		//10
				row++;
			}//While
			rs.close();
			ps.close();
			xTableHeader.autoSize(true);
		}// try 
		catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
		}
		xTableHeader.repaint();
	}// Fin fillTableHeader

	/**************************************************************************
	 * Action Listener
	 * @param e
	 *            event
	 */
	public void actionPerformed(ActionEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		int row = xTableHeader.getSelectedRow();
		if (e.getSource() == bSearch) {
			fillTableHeader(tableHeaderSearch());
		} 
		else if (e.getSource() == bPurchase && row != -1)
			cmd_PurchaseOrder(row);
		setCursor(Cursor.getDefaultCursor());
	}// Fin actionPerformed

	/**************************************************************************
	 * List Selection Listener - get Info and fill xMatchedTo
	 * 
	 * @param e
	 *            event
	 */
	public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
			return;
		int matchedRow = xTableHeader.getSelectedRow();
		//loadDetail(matchedRow);
	}// Fin valueChanged

	private void jbInit() throws Exception {
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		// Labels
		datefromlabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
		datetolabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
		docTypeLabel.setText(Msg.getMsg(Env.getCtx(), "XX_DocType"));
		assetsTypeLabel.setText(Msg.translate(Env.getCtx(), "XX_AssetType"));
		costCenterLabel.setText(Msg.translate(Env.getCtx(), "XX_CostCenter"));
		bPLabel.setText(Msg.translate(Env.getCtx(), "XX_BusinessPartner"));
		orderTypeLabel.setText(Msg.getMsg(Env.getCtx(), "XX_POType"));
		statusLabel.setText(Msg.translate(Env.getCtx(), "XX_POStatus"));
		purchaseLabel.setText(Msg.translate(Env.getCtx(), "XX_PurchaseOrder"));
		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bPurchase.setEnabled(true);
		bPurchase.addActionListener(this);
		southPanel.setLayout(southLayout);
		centerPanel.setLayout(centerLayout);
		xMatchedScrollPane.setBorder(xMatchedBorder);
		xMatchedScrollPane.setMinimumSize(new Dimension(800, 160));
		xMatchedToScrollPane.setBorder(xMatchedToBorder);
		xMatchedToScrollPane.setMinimumSize(new Dimension(800, 250));
		placedOrderText.setPreferredSize(new Dimension(100, 20));
		placedOrderValueText.setBackground(Color.green);
		//Total PO
		TotalPOText.setPreferredSize(new Dimension(100, 20));
		// Document Type
		northPanel.add(docTypeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(docTypeCombo, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		// Assets Type
		northPanel.add(assetsTypeLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(assetsCombo, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		// Status
		northPanel.add(statusLabel, new GridBagConstraints(4, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(statusCombo, new GridBagConstraints(5, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		// Business Partner
		northPanel.add(bPLabel, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(bpCombo, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
						12, 0, 5, 0), 0, 0));
		// Order Type
		northPanel.add(orderTypeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(orderCombo, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		// Cost Center
		northPanel.add(costCenterLabel,new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(centerCombo, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		// Date From
		northPanel.add(datefromlabel, new GridBagConstraints(4, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(dateFrom,new GridBagConstraints(5, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0)); 
		// Date To
		northPanel.add(datetolabel, new GridBagConstraints(6, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(dateTo, new GridBagConstraints(7, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		// Search
		northPanel.add(bSearch, new GridBagConstraints(3, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		// Purchase
		northPanel.add(bPurchase, new GridBagConstraints(5, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		xMatchedScrollPane.getViewport().add(xTableHeader);
		centerPanel.add(xMatchedScrollPane, BorderLayout.NORTH);
		//centerPanel.add(xMatchedToScrollPane, BorderLayout.SOUTH);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		m_frame.pack();
	} // jbInit
	
	/**
	 *  Get the key of currently selected row
	 *  @return selected key
	 */
	protected Integer getSelectedRowKey(){
		int row = xTableHeader.getSelectedRow();
		if (row != -1 && m_keyColumnIndex != -1){
			Object data = xTableHeader.getModel().getValueAt(row, m_keyColumnIndex);
			if (data instanceof IDColumn)
				data = ((IDColumn)data).getRecord_ID();
			if (data instanceof Integer)
				return (Integer)data;
		}
		return null;
	}//  getSelectedRowKey
}//Fin XX_POConsultForm
