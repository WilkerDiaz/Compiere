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
package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.excel.Excel;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VFile;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MProduct;
import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_Ref_XX_OrderRequestType;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VMR_Order;
import compiere.model.cds.X_XX_VMR_OrderRequestDetail;
import compiere.model.cds.X_XX_VMR_TypeLabel;
import compiere.model.cds.X_XX_VMR_VendorProdRef;
import compiere.model.cds.forms.indicator.XX_Indicator;

public class XX_OrderSearchForm extends CPanel implements FormPanel,
		ActionListener, ListSelectionListener, TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PrintService flat = null;
	PrintService glued = null;
	Integer last_header = -1;
	Integer reimpresion = 0;
	int orgCD = 0;

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
		System.out.println("WinNo=" + m_WindowNo + " - AD_Client_ID=" + m_AD_Client_ID
				+ ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by+ "ROL= " +Env.getCtx().getAD_Role_ID());
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		
		PrintService [] services = PrintServiceLookup.lookupPrintServices(null, null);		
		flat = services[Env.getCtx().getContextAsInt("#XX_VMR_PRINTLABEL_Hanging")];
		glued = services[Env.getCtx().getContextAsInt("#XX_VMR_PRINTLABEL_Glued")];
		reimpresion = Env.getCtx().getContextAsInt("#XX_VMR_PRINTLABEL_Reprint");

			//Remove the no longer necessary items on the context				
		Env.getCtx().remove("#XX_VMR_PRINTLABEL_Hanging");
		Env.getCtx().remove("#XX_VMR_PRINTLABEL_Glued");
		Env.getCtx().remove("#XX_VMR_PRINTLABEL_Reprint");
		

		try {
			// UI
			productSearch = VLookup.createProduct(m_WindowNo);
			productSearch.setVerifyInputWhenFocusTarget(false);
			// Se requiere un keynamepair para traer el id de la ventana

			Lookup l = MLookupFactory.get(Env.getCtx(), m_WindowNo, Env
					.getCtx().getContextAsInt("#XX_L_C_ORDERCOLUMN_ID"),
					DisplayTypeConstants.Search);
			purchaseSearch = new VLookup("C_Order_ID", false, false, true, l);
			purchaseSearch.setVerifyInputWhenFocusTarget(false);

			jbInit();
			//
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			//frame.setMaximize(true);

		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	} // init

	/** Window No */
	private int m_WindowNo = 0;
	/** FormFrame */
	private FormFrame m_frame;
	/** Logger */
	static CLogger log = CLogger.getCLogger(XX_OrderSearchForm.class);

	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();

	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();

	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CLabel categoryLabel = new CLabel();
	private CComboBox categoryCombo = new CComboBox();
	private CLabel packageLabel = new CLabel();
	private CComboBox packageCombo = new CComboBox();
	private CLabel brandLabel = new CLabel();
	private CComboBox brandCombo = new CComboBox();
	private CLabel departmentLabel = new CLabel();
	private CComboBox departmentCombo = new CComboBox();
	private CLabel seasonLabel = new CLabel();
	private CComboBox seasonCombo = new CComboBox();
	private CLabel lineLabel = new CLabel();
	private CComboBox lineCombo = new CComboBox();
	private CLabel sectionLabel = new CLabel();
	private CComboBox sectionCombo = new CComboBox();
	private CLabel storeLabel = new CLabel();
	private CComboBox storeCombo = new CComboBox();
	private VLookup productSearch = null;
	private CLabel collectionLabel = new CLabel();
	private CComboBox collectionCombo = new CComboBox();
	private CLabel statusLabel = new CLabel();
	private CComboBox statusCombo = new CComboBox();
	private CLabel typeLabel = new CLabel();
	private CComboBox typeCombo = new CComboBox();
	private CLabel placedOrderLabel = new CLabel();
	private CTextField placedOrderText = new CTextField();

	private CLabel vendorLabel = new CLabel();
	private CComboBox vendorCombo = new CComboBox();
	
	private CLabel onlyProductLabel = new CLabel();
	private CLabel orderDateLabel = new CLabel();

	private CLabel datefromlabel = new CLabel();
	private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo"));
	private CButton bSearch = new CButton();
	private CButton bClear = new CButton();
	private CButton bPrint = new CButton();
	private CButton bPrintPlacedOrders = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CLabel xMatchedLabel = new CLabel();
	private CLabel xMatchedToLabel = new CLabel();
	private CLabel differenceLabel = new CLabel();
	private CButton bProcess = new CButton();
	private CPanel centerPanel = new CPanel();
	private BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
	private JScrollPane xMatchedScrollPane = new JScrollPane();
	private TitledBorder xMatchedBorder = new TitledBorder("xMatched");
	private MiniTable xTableHeader = new MiniTable();
	private JScrollPane xMatchedToScrollPane = new JScrollPane();
	private TitledBorder xMatchedToBorder = new TitledBorder("xMatchedTo");
	private MiniTable xTableDetail = new MiniTable();
	private CLabel isFromPOLabel = new CLabel();
	private VLookup purchaseSearch = null;	
	private CLabel distributionLabel = new CLabel();
	private CComboBox distributionCombo = new CComboBox();

	private final int h_id = 0, h_correlative = 1, h_store = 2, h_po = 3,
			h_header = 4, h_type = 5, h_status = 6, h_pack = 7, h_rec = 8, h_date = 9,
			h_week = 10, h_creat = 11, h_assig = 12, h_guide = 13;

	private Vector<String> status_name = new Vector<String>();
	private Vector<String> type_name = new Vector<String>();

	private MiniTablePreparator exportTable = new MiniTablePreparator();
	private	CButton bExport = new CButton(Msg.translate(Env.getCtx(), "ExportExcel"));
	private VFile bFile = new VFile("File", Msg.getMsg(Env.getCtx(), "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	private CLabel labelFile = new CLabel(Msg.getMsg(Env.getCtx(), "File"));
	
	//PROYECTO CD VALENCIA
	private CLabel locatorLabel = new CLabel();
	private CComboBox locatorCombo = new CComboBox();
	
	void disable_line() {

		sectionCombo.removeAllItems();
		sectionCombo.setEnabled(false);
		sectionCombo.setEditable(false);

	}

	void disable_collection() {

		packageCombo.removeAllItems();
		packageCombo.setEnabled(false);
		packageCombo.setEditable(false);
	}

	void disable_dept() {

		lineCombo.removeAllItems();
		sectionCombo.removeAllItems();

		lineCombo.setEnabled(false);
		lineCombo.setEditable(false);

		sectionCombo.setEnabled(false);
		sectionCombo.setEditable(false);
	}

	void disable_season() {
		collectionCombo.removeAllItems();
		packageCombo.removeAllItems();

		collectionCombo.setEnabled(false);
		collectionCombo.setEditable(false);

		packageCombo.setEnabled(false);
		packageCombo.setEditable(false);
	}

	void disable_cat() {
		departmentCombo.removeAllItems();
		disable_dept();
	}

	void dynLine() {
		KeyNamePair dep = (KeyNamePair) departmentCombo.getSelectedItem();
		lineCombo.removeActionListener(this);

		if (dep == null || dep.getKey() == -1) {
			disable_dept();
		} else {
			lineCombo.removeAllItems();
			String sql = " SELECT XX_VMR_LINE_ID, VALUE||'-'||NAME FROM XX_VMR_LINE "
					+ " WHERE XX_VMR_DEPARTMENT_ID = "
					+ dep.getKey()
					+ " ORDER BY VALUE||'-'||NAME ";
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				lineCombo.addItem(new KeyNamePair(-1, null));
				while (rs.next()) {
					lineCombo.addItem(new KeyNamePair(rs.getInt(1), rs
							.getString(2)));
				}
				rs.close();
				pstmt.close();

				lineCombo.setEnabled(true);
				lineCombo.setEditable(true);
				lineCombo.addActionListener(this);
			} catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			}
		}
	}

	void dynCollection() {
		KeyNamePair sea = (KeyNamePair) seasonCombo.getSelectedItem();
		collectionCombo.removeActionListener(this);

		if (sea == null || sea.getKey() == -1) {
			disable_season();
		} else {
			collectionCombo.removeAllItems();
			String sql = " SELECT XX_VMR_Collection_ID, NAME " +
					" FROM XX_VMR_Collection "
					+ " WHERE XX_VMA_SEASON_ID = "
					+ sea.getKey()
					+ " ORDER BY NAME ";
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				collectionCombo.addItem(new KeyNamePair(-1, null));
				while (rs.next()) {
					collectionCombo.addItem(new KeyNamePair(rs.getInt(1), rs
							.getString(2)));
				}
				rs.close();
				pstmt.close();
				collectionCombo.setEnabled(true);
				collectionCombo.setEditable(true);
				collectionCombo.addActionListener(this);
			} catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			}
		}

	}

	void dynSection() {
		KeyNamePair lin = (KeyNamePair) lineCombo.getSelectedItem();
		sectionCombo.removeActionListener(this);
		if (lin == null || lin.getKey() == -1) {
			disable_line();
		} else {
			sectionCombo.removeAllItems();
			String sql = " SELECT XX_VMR_SECTION_ID, VALUE||'-'||NAME FROM XX_VMR_SECTION "
					+ " WHERE XX_VMR_LINE_ID = "
					+ lin.getKey()
					+ " ORDER BY VALUE||'-'||NAME";
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				sectionCombo.addItem(new KeyNamePair(-1, null));
				while (rs.next()) {
					sectionCombo.addItem(new KeyNamePair(rs.getInt(1), rs
							.getString(2)));
				}
				rs.close();
				pstmt.close();
				sectionCombo.setEnabled(true);
				sectionCombo.setEditable(true);
				sectionCombo.addActionListener(this);

			} catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			}
		}
	}

	void dynPackage() {
		KeyNamePair col = (KeyNamePair) collectionCombo.getSelectedItem();
		packageCombo.removeActionListener(this);
		if (col == null || col.getKey() == -1) {
			disable_collection();
		} else {
			packageCombo.removeAllItems();
			String sql = " SELECT XX_VMR_PACKAGE_ID, NAME FROM XX_VMR_PACKAGE "
					+ " WHERE XX_VMR_Collection_ID = " + col.getKey()
					+ " ORDER BY NAME";
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				packageCombo.addItem(new KeyNamePair(-1, null));
				while (rs.next()) {
					packageCombo.addItem(new KeyNamePair(rs.getInt(1), rs
							.getString(2)));
				}
				rs.close();
				pstmt.close();
				packageCombo.setEnabled(true);
				packageCombo.setEditable(true);
				packageCombo.addActionListener(this);

			} catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			}
		}
	}

	void dynDepartament() {
		KeyNamePair cat = (KeyNamePair) categoryCombo.getSelectedItem();
		departmentCombo.removeActionListener(this);
		departmentCombo.removeAllItems();
		disable_dept();

		String sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME "
				+ " FROM XX_VMR_DEPARTMENT ";

		if (cat != null && cat.getKey() != -1) {
			sql += " WHERE XX_VMR_CATEGORY_ID = " + cat.getKey();
		}
		sql += " ORDER BY VALUE||'-'||NAME ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			departmentCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				departmentCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			rs.close();
			pstmt.close();

			departmentCombo.addActionListener(this);
			departmentCombo.setEnabled(true);
			departmentCombo.setEditable(true);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
	}

	void dynSeason() {

		seasonCombo.removeActionListener(this);
		seasonCombo.removeAllItems();
		String sql = "SELECT XX_VMA_SEASON_ID, NAME FROM XX_VMA_SEASON ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		sql += " ORDER BY NAME ";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			seasonCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				seasonCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			rs.close();
			pstmt.close();

			seasonCombo.addActionListener(this);
			seasonCombo.setEnabled(true);
			seasonCombo.setEditable(true);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}

	}

	void dynCategory() {

		categoryCombo.removeActionListener(this);
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||'-'||NAME "
				+ " FROM XX_VMR_CATEGORY ";
		sql += " ORDER BY VALUE||'-'||NAME ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			categoryCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				categoryCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			rs.close();
			pstmt.close();
			categoryCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}

	}

	void dynBrand() {
		String sql = " SELECT XX_VMR_BRAND_ID, VALUE||'-'||NAME FROM XX_VMR_BRAND ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		sql += " ORDER BY VALUE||'-'||NAME ";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			brandCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				brandCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
	}

	void dynWarehouse() {
		String sql = " SELECT M_WAREHOUSE_ID, VALUE||'-'||NAME FROM M_WAREHOUSE ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		sql += " ORDER BY VALUE||'-'||NAME ";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			storeCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				storeCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
	}

	void dynHeaders() {
		String sql = " SELECT XX_VMR_DISTRIBUTIONHEADER_ID FROM XX_VMR_DISTRIBUTIONHEADER" +
				" WHERE XX_DISTRIBUTIONSTATUS = '" + X_Ref_XX_DistributionStatus.APROBADA.getValue() + "'";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		sql += " ORDER BY XX_VMR_DISTRIBUTIONHEADER_ID ";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			distributionCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				distributionCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(1)));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	private boolean dynLocators() {
		
		String sql = "\nSELECT  W.AD_ORG_ID, W.VALUE||'-'||W.NAME " +
				"\nFROM M_WAREHOUSE W WHERE   W.XX_ISSTORE = 'N' ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		locatorCombo.removeAllItems();
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			locatorCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				locatorCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {ps.close();}
				catch (Exception e) {}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {}				
			} 
		}		
		locatorCombo.setSelectedIndex(0);
		return (locatorCombo.getItemCount() > 1);

	}

	private void dynVendor(){
		
		KeyNamePair dept = (KeyNamePair)departmentCombo.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)vendorCombo.getSelectedItem();
		
		KeyNamePair loadKNP;
		String sql = "";
		
		//Department selected
		if(dept == null || dept.getKey() == -1) {			
			vendorCombo.removeAllItems();
			loadKNP = new KeyNamePair(-1,null);
			vendorCombo.addItem(loadKNP);
			
			sql = "SELECT C_BPARTNER_ID, NAME FROM C_BPARTNER  " +
				"WHERE ISVENDOR = 'Y' AND XX_ISVALID = 'Y' AND ISACTIVE = 'Y'";
				sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) + 
				" ORDER BY NAME ";							//
		} else {
			vendorCombo.removeAllItems();
			loadKNP = new KeyNamePair(-1,null);
			vendorCombo.addItem(loadKNP);	
			sql = " SELECT bp.C_BPARTNER_ID, bp.NAME" 
					+ " FROM XX_VMR_VENDORDEPARTASSOCI ve, C_BPARTNER bp " 
					+ " WHERE ve.C_BPARTNER_ID = bp.C_BPARTNER_ID AND ve.XX_VMR_DEPARTMENT_ID = " 
					+ " (SELECT dp.XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT dp " +
							" WHERE dp.XX_VMR_DEPARTMENT_ID = " + dept.getKey() + ")";						
		}		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				vendorCombo.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
			vendorCombo.setSelectedItem(bpar);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	private void dynInit() {
	
		m_frame.getRootPane().setDefaultButton(bSearch);

		xTableHeader.getTableHeader().setReorderingAllowed(false);
		xTableDetail.getTableHeader().setReorderingAllowed(false);		
		xTableHeader.autoSize(true);
		

		// LLenar los combo de listas
		statusCombo.addItem("");
		status_name.add("");
		for (X_Ref_XX_VMR_OrderStatus v : X_Ref_XX_VMR_OrderStatus.values()) {
			statusCombo.addItem(v.getValue() + "-" + v);
			status_name.add(v.getValue());
		}

		typeCombo.addItem("");
		type_name.add("");
		for (X_Ref_XX_OrderRequestType v : X_Ref_XX_OrderRequestType.values()) {
			typeCombo.addItem(v.getValue() + "-" + v);
			type_name.add(v.getValue());
		}

		java.util.Calendar now = java.util.Calendar.getInstance();
		dateTo.setValue(now.getTime());

		/* Add the column definition for the table */
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(" ", ".", IDColumn.class, false, false, ""),
				new ColumnInfo(Msg
						.translate(Env.getCtx(), "XX_BecoCorrelative"), ".",
						String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Store"), ".",
						KeyNamePair.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PurchaseOrder"),
						".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DistributionHeader"),
						".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PlacedOrderType"), ".",
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PlacedOrderStatus"), ".", 
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PackageQuantity"), ".",
						Integer.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReceived"), ".",
						Integer.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Date")+" de Estado", ".",
								String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_WeekCreated"),
								".", String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Date")+ " de Creación",
										".", String.class, "."), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AssigmentDate"),
								".", String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DispatchGuide"),
						".", String.class, ".")
				};
		
		
		ColumnInfo[] layoutDetailReprint = new ColumnInfo[] {
				
				new ColumnInfo(Msg.translate(Env.getCtx(), "VendorProductRef"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"),
						".", KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Qty"), ".",
								Integer.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_LabelQty"), ".",
								Integer.class, false, false, ""),
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "Print"), ".", IDColumn.class, false, false, ""),
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Category"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Department_I"),
						".", KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Line_I"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Section_I"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Season"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Collection"),
						".", KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Package"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Brand"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Batch"), ".",Integer.class),
		};
		
		ColumnInfo[] layoutDetailSearch = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "VendorProductRef"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"),
						".", KeyNamePair.class, "."),
						
				//REMODIFICAR PONER FALSE EN READONLY - Si se quiere modificar
				new ColumnInfo(Msg.translate(Env.getCtx(), "Qty"), ".",	Integer.class, true, false, "." ),	
				
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Category"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Department_I"),
						".", KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Line_I"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Section_I"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Season"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Collection"),
						".", KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Package"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Brand"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Batch"), ".",Integer.class),
				
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".",String.class),
				
				//Agregar una columna ID para almacenar el id del detalle
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "Print"), ".", IDColumn.class, true, false, ""),
		};

		if (reimpresion > 0) {
			xTableHeader.prepareTable(layout, "", "", true, "");
			xTableDetail.prepareTable(layoutDetailReprint, "", "", true, "");
		} else {
			
			//REMODIFICAR PONER EN TRUE-PARA EDITAR
			xTableHeader.prepareTable(layout, "", "", false, "");
			xTableDetail.prepareTable(layoutDetailSearch, "", "", false, "");
		}
		
		xTableHeader.getTableHeader().setFocusable(false);
		xTableHeader.setSortEnabled(true);
				
		xMatchedToBorder
				.setTitle(Msg.getMsg(Env.getCtx(), "XX_OrderDetail"));
		xMatchedToScrollPane.repaint();
		xTableDetail.setSortEnabled(true);
		xMatchedBorder.setTitle(Msg.translate(Env.getCtx(), "Order"));
		xMatchedScrollPane.repaint();
		// Visual
		CompiereColor.setBackground(this);

		// Listener
		bSearch.addActionListener(this);
		bClear.addActionListener(this);
		bPrint.addActionListener(this);
		bPrintPlacedOrders.addActionListener(this);
		
		bExport.addActionListener(this);
		xTableHeader.getSelectionModel().addListSelectionListener(this);
		
		xTableDetail.getModel().addTableModelListener(this);
		xTableDetail.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				int column = xTableDetail.getSelectedColumn();
				int row = xTableDetail.getSelectedRow();
				xTableDetail.editCellAt(row, column);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {		
			}
			
		});
		
		bProcess.addActionListener(this);

		// Init
		// cmd_departmentCombo();
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);

		// Agregar las acciones
		dynLocators();
		dynHeaders();
		dynCategory();
		dynDepartament();
		dynSeason();
		dynWarehouse();
		dynBrand();
		disable_dept();
		disable_season();
		dynVendor();

	}

	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	} // dispose

	String tableHeaderSearch() {
		
		// String sql= "SELECT * FROM "
		StringBuilder sql = new StringBuilder();
		sql.append(
			" SELECT ORD.XX_VMR_ORDER_ID FROM XX_VMR_ORDER ORD INNER JOIN XX_VMR_ORDERREQUESTDETAIL DET ")
			.append(
			" ON (ORD.XX_VMR_ORDER_ID = DET.XX_VMR_ORDER_ID)" +
			" JOIN M_PRODUCT PR ON (PR.M_PRODUCT_ID = DET.M_PRODUCT_ID)");
		
		if(vendorCombo.getValue() != null){
			sql.append(" JOIN  C_BPARTNER VEN ON (PR.C_BPARTNER_ID = VEN.C_BPARTNER_ID) ");
			KeyNamePair vendor = (KeyNamePair) vendorCombo.getValue();
			if (vendor.getKey() != -1)
				sql.append(" AND VEN.C_BPARTNER_ID =").append(vendor.getKey()).append(" ");;
		}
		
		sql.append(" WHERE 1=1 ");

	if(locatorCombo.getValue() != null){
		KeyNamePair locator = (KeyNamePair) locatorCombo.getValue();
		if (locator.getKey() != -1)
			sql.append(" AND P.AD_ORG_ID =").append(locator.getKey()).append(" ");;
	}
	

		boolean noSearch = true;
		
		if(locatorCombo.getValue() != null){
			KeyNamePair locator = (KeyNamePair) locatorCombo.getValue();
			if (locator.getKey() != -1){
				noSearch = false;
				sql.append(" AND ORD.AD_ORG_ID =").append(locator.getKey()).append(" ");
			}
		}

		if (categoryCombo.getValue() != null) {
			KeyNamePair cat = (KeyNamePair) categoryCombo.getValue();
			if (cat.getKey() != -1){
				noSearch = false;
				sql.append(" AND DET.XX_VMR_CATEGORY_ID = ").append(cat.getKey()).append(" ");
			}
		}
		if (departmentCombo.getValue() != null) {
			KeyNamePair dep = (KeyNamePair) departmentCombo.getValue();
			if (dep.getKey() != -1){
				noSearch = false;
				sql.append(" AND DET.XX_VMR_DEPARTMENT_ID = ").append(dep.getKey()).append(" ");
			}
		}
		if (lineCombo.getValue() != null) {
			KeyNamePair lin = (KeyNamePair) lineCombo.getValue();
			if (lin.getKey() != -1){
				noSearch = false;
				sql.append(" AND DET.XX_VMR_LINE_ID = ").append(lin.getKey()).append(" ");
			}
		}
		if (sectionCombo.getValue() != null) {
			KeyNamePair sec = (KeyNamePair) sectionCombo.getValue();
			if (sec.getKey() != -1){
				noSearch = false;
				sql.append(" AND DET.XX_VMR_SECTION_ID = ").append(sec.getKey()).append(" ");
			}
		}
		if (seasonCombo.getValue() != null) {
			KeyNamePair sea = (KeyNamePair) seasonCombo.getValue();
			if (sea.getKey() != -1){
				noSearch = false;
				sql.append(" AND DET.XX_VMA_SEASON_ID = ").append(sea.getKey()).append(" ");
			}
		}
		if (collectionCombo.getValue() != null) {
			KeyNamePair col = (KeyNamePair) collectionCombo.getValue();
			if (col.getKey() != -1){
				noSearch = false;
				sql.append(" AND DET.XX_VMR_Collection_ID = ").append(col.getKey()).append(" ");
			}
		}
		if (packageCombo.getValue() != null) {
			KeyNamePair pac = (KeyNamePair) packageCombo.getValue();
			if (pac.getKey() != -1){
				noSearch = false;
				sql.append(" AND DET.XX_VMR_PACKAGE_ID = ").append(pac.getKey()).append(" ");
			}
		}
		if (brandCombo.getValue() != null) {
			KeyNamePair bra = (KeyNamePair) brandCombo.getValue();
			if (bra.getKey() != -1){
				noSearch = false;
				sql.append(" AND PR.XX_VMR_BRAND_ID = ").append(bra.getKey()).append(" ");
			}
		}
		if (productSearch.getValue() != null) {
			noSearch = false;
			Integer p_id = (Integer) productSearch.getValue();
			sql.append(" AND DET.M_PRODUCT_ID = ").append(p_id).append(" ");
		}
		if (purchaseSearch.getValue() != null) {
			noSearch = false;
			Integer po_id = (Integer) purchaseSearch.getValue();
			sql.append(" AND ORD.C_ORDER_ID = ").append(po_id).append(" ");
		}
		if (storeCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) storeCombo.getValue();
			if (sto.getKey() != -1){
				noSearch = false;
				sql.append(" AND ORD.M_WAREHOUSE_ID = ").append(sto.getKey()).append(" ");
			}
		}
		if (statusCombo.getSelectedIndex() != -1
				&& statusCombo.getSelectedIndex() != 0) {
			noSearch = false;
			sql.append(" AND ORD.XX_ORDERREQUESTSTATUS = ").append(
					"'" + status_name.elementAt(statusCombo.getSelectedIndex())
							+ "'").append(" ");
		}
		if (typeCombo.getSelectedIndex() != -1
				&& typeCombo.getSelectedIndex() != 0) {
			noSearch = false;
			sql.append(" AND ORD.XX_ORDERREQUESTTYPE = ").append(
					"'" + type_name.elementAt(typeCombo.getSelectedIndex())
							+ "'").append(" ");
		}
		if (distributionCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) distributionCombo.getValue();
			if (sto.getKey() != -1){
				noSearch = false;
				sql.append(" AND ORD.XX_VMR_DISTRIBUTIONHEADER_ID = ").append(sto.getKey()).append(" ");
			}
		}
		if (!placedOrderText.getText().equals("")) {
			noSearch = false;
			sql.append(" AND ORD.XX_ORDERBECOCORRELATIVE = ").append(
					"'" + placedOrderText.getText().trim() + "'").append(" ");
		}

		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		if (from != null && to != null) {
			noSearch = false;
			sql.append(" AND TRUNC(ORD.CREATED) ").append(" >= ").append(
					DB.TO_DATE(from)).append(" ");
			sql.append(" AND TRUNC(ORD.CREATED) ").append(" <= ").append(
					DB.TO_DATE(to)).append(" ");
		} else if (from != null)
		{	
			noSearch = false;
			sql.append(" AND TRUNC(ORD.CREATED) ").append(" >= ").append(
					DB.TO_DATE(from, true)).append(" ");
		}
		else if (to != null){
			sql.append(" AND TRUNC(ORD.CREATED) ").append(" <= ").append(
					DB.TO_DATE(to)).append(" ");
		}
		
		//Si no hay ningun elemento de busqueda se evita mostrar toda la data porque se tarda en exceso
		if(noSearch){
			sql.append(" AND ROWNUM < 15001 ");
		}
			

		// Query mayor
		String sql_mayor = " SELECT P.XX_VMR_ORDER_ID, P.XX_ORDERBECOCORRELATIVE, S.M_WAREHOUSE_ID, "
				+ " S.VALUE||'-'||S.NAME, O.DOCUMENTNO, P.XX_VMR_DISTRIBUTIONHEADER_ID, P.XX_ORDERREQUESTTYPE , P.XX_ORDERREQUESTSTATUS,"
				+ " P.XX_PACKAGEQUANTITY, " 
				//+ " TO_CHAR(P.CREATED, 'DD/MM/YYYY') " // COMENTADO GMARQUES
				// AGREGADO GMARQUES:
				+ " TO_CHAR (case when xx_orderrequeststatus = 'TI' then xx_datestatusonstore" +
						"            when xx_orderrequeststatus = 'ET' then xx_datestatustransit" +
						"            when xx_orderrequeststatus = 'EB' then xx_datestatusatbay" +
						"            when xx_orderrequeststatus = 'PE' then xx_datestatuspending" +
						"            else P.updated end, 'DD/MM/YYYY'), "
				// FIN GMARQUES
				+ " P.XX_WEEKCREATED, TO_CHAR(P.CREATED, 'DD/MM/YYYY'), TO_CHAR(P.XX_ASSIGNMENTDATE, 'DD/MM/YYYY')" 				
				+ " FROM (XX_VMR_ORDER P LEFT OUTER JOIN C_ORDER O ON (P.C_ORDER_ID = O.C_ORDER_ID) "
				+ " INNER JOIN M_WAREHOUSE S ON (P.M_WAREHOUSE_ID = S.M_WAREHOUSE_ID))" ;				
		sql_mayor = MRole.getDefault().addAccessSQL(sql_mayor, "P",
				MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		sql_mayor += " AND P.XX_VMR_ORDER_ID IN ( " + sql.toString() + " )" +
				" ORDER BY  S.VALUE||'-'||S.NAME ";		
		return sql_mayor;
	}

	/** Limpia el filtro*/
	void clearFilter() {
		
		categoryCombo.setSelectedIndex(0);
		brandCombo.setSelectedIndex(0);
		productSearch.setValue(null);
		typeCombo.setSelectedIndex(0);
		departmentCombo.setSelectedIndex(0);
		//creo que no hay que actualizar linea y seccion
		statusCombo.setSelectedIndex(0);
		seasonCombo.setSelectedIndex(0);
		storeCombo.setSelectedIndex(0);
		purchaseSearch.setValue(null);
		dateFrom.setValue(null);
		dateTo.setValue(null);
		placedOrderText.setText("");
		distributionCombo.setSelectedIndex(0);
		locatorCombo.setSelectedIndex(0);
		vendorCombo.setSelectedIndex(0);
	}
	
	void fillTableHeader(String sql) {

		// LLenar la tabla
		xTableHeader.setRowCount(0);
		xTableDetail.setRowCount(0);
		PreparedStatement ps = DB.prepareStatement(sql, null);
		
		//Crear un Prepared Statement para la consulta interna de guias de despacho
		String sql_guiasdespacho = "SELECT G.DOCUMENTNO FROM XX_VLO_DISPATCHGUIDE G JOIN XX_VLO_DETAILDISPATCHGUIDE D " +
				" ON (G.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID) " + 				
				" WHERE G.XX_DISPATCHGUIDESTATUS IN ('TIE','TRA') " +
				" AND D.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' " +
				" AND D.XX_VMR_ORDER_ID = ? ORDER BY G.DOCUMENTNO";
		
		PreparedStatement ps_guiasdespacho = DB.prepareStatement(sql_guiasdespacho, null);
		ResultSet rs_guiasdespacho = null;
		try {
			ResultSet rs = ps.executeQuery();			
			int row = 0;
			while (rs.next()) {
				//System.out.println(row);
				xTableHeader.setRowCount(row + 1);
				IDColumn id = new IDColumn(rs.getInt(1));
				xTableHeader.setValueAt(id, row, h_id);
				xTableHeader.setValueAt(rs.getString(2), row, h_correlative);
				xTableHeader.setValueAt(new KeyNamePair(rs.getInt(3), rs.getString(4)), row, h_store);
				xTableHeader.setValueAt(rs.getString(5), row, h_po);
				xTableHeader.setValueAt(rs.getString(6), row, h_header);				
				for (X_Ref_XX_OrderRequestType v : X_Ref_XX_OrderRequestType.values()) {
					if (v.getValue().equals(rs.getString(7))) {
						xTableHeader.setValueAt(v, row, h_type);
						break;
					}
				}
				for (X_Ref_XX_VMR_OrderStatus v : X_Ref_XX_VMR_OrderStatus
						.values()) {
					if (v.getValue().equals(rs.getString(8))) {
						xTableHeader.setValueAt(v, row, h_status);
						break;
					}
				}
				xTableHeader.setValueAt(rs.getInt(9), row, h_pack);
				xTableHeader.setValueAt(rs.getString(10), row, h_date);

				xTableHeader.setValueAt(rs.getInt(11), row, h_week);
				xTableHeader.setValueAt(rs.getString(12), row, h_creat);
				xTableHeader.setValueAt(rs.getString(13), row, h_assig);
				
				//Obtener los numero de documentos de la guia de despacho
				ps_guiasdespacho.setInt(1, rs.getInt(1));
				try {
					rs_guiasdespacho = ps_guiasdespacho.executeQuery();
					
					//Colocar los numero de documento en el campo 
					String guia_despacho = "";					
					int i = 0;
					while (rs_guiasdespacho.next()) {
						if (rs_guiasdespacho.getString(1) != null)
							if (i++ > 0)
								guia_despacho += " / ";
							guia_despacho += rs_guiasdespacho.getString(1);						
					}
					xTableHeader.setValueAt(guia_despacho, row, h_guide);					
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					if (rs_guiasdespacho != null)
						try { 
							rs_guiasdespacho.close();
						} catch (SQLException e) {}
					rs_guiasdespacho = null;
				}
				

				int bultos_recibidos = 0;
				// TODO Calcular los bultos recibidos
				xTableHeader.setValueAt(bultos_recibidos, row, h_rec);
				row++;
			}
			rs.close();
			ps.close();
			ps_guiasdespacho.close();
			
			xTableHeader.autoSize(true);
			xTableHeader.setMultiSelection(true);
			if (xTableHeader.getRowCount() > 0) {
				loadDetail(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
		}
		xTableHeader.repaint();
	}

	/**************************************************************************
	 * Action Listener
	 * 
	 * @param e
	 *            event
	 */
	public void actionPerformed(ActionEvent e) {
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));		
		if (e.getSource() == bSearch) {
			fillTableHeader(tableHeaderSearch());
		} else if (e.getSource() == bClear) {			
			clearFilter();
		} else if (e.getSource() == bPrint) {									
			if (last_header != -1) {
				IDColumn id = (IDColumn)xTableHeader.getValueAt(last_header, h_id);
				try {
					X_XX_VMR_Order header = new X_XX_VMR_Order(Env.getCtx(), id.getRecord_ID(), null);
					new Utilities().GenerarConsecutivo(header);
					printSelectedLabels(header);					
				} catch (NullPointerException h) {
					ADialog.error(1,  m_frame, "XX_PriceConsecutiveError");					
				}										
			}
			m_frame.setCursor(Cursor.getDefaultCursor());		
		} else if (e.getSource() == bPrintPlacedOrders) {
			
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);    	
	    	m_frame.setCursor(hourglassCursor);
			//Aqui se imprime el pedido completo 
			try {				
				boolean all_good = true, some = false;
				for (int i = 0 ; i < xTableHeader.getRowCount() ; i++) {					
					IDColumn id = (IDColumn)xTableHeader.getValueAt(i, h_id);
					if (id.isSelected()) {
						some = true;
						X_XX_VMR_Order header = new X_XX_VMR_Order(Env.getCtx(), id.getRecord_ID(), null);
						new Utilities().GenerarConsecutivo(header);
						all_good &= printSelectedPlacedOrder(header);
					}
				}
				if (all_good && some) {
					ADialog.info(m_WindowNo, m_frame, "XX_PrintedLabels");
				} 
			} catch (Exception h) {
				ADialog.error(m_WindowNo,  m_frame, "XX_PriceConsecutiveError");
			}
	    	m_frame.setCursor(Cursor.getDefaultCursor());
		} else if (e.getSource() == departmentCombo) {
			dynLine();
			dynVendor();
		} else if (e.getSource() == lineCombo) {
			dynSection();
		} else if (e.getSource() == categoryCombo) {
			dynDepartament();
		} else if (e.getSource() == seasonCombo) {
			dynCollection();
		} else if (e.getSource() == collectionCombo) {
			dynPackage();
		}else if (e.getSource() == bExport) {
			Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			Cursor defaultCursor = Cursor.getDefaultCursor();
			m_frame.setCursor(waitCursor);
			try {
				createReport();
				XX_Indicator.imprimirArchivo(exportTable, bFile, m_WindowNo, m_frame); 
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}			
			m_frame.setCursor(defaultCursor);
		}
		m_frame.setCursor(Cursor.getDefaultCursor());
	}

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
		loadDetail(matchedRow);
	}

	void loadDetail(int matchedRow) {
		
		//REMODIFICAR COLOCAR este codigo para EDITAR		
		/*if (last_header > -1) {
			for (int i = 0 ; i < xTableHeader.getRowCount(); i++) {
				((IDColumn)xTableHeader.getValueAt(i, 0)).setSelected(false);
			}			
			if (matchedRow > -1) 
				((IDColumn)xTableHeader.getValueAt(matchedRow, 0)).setSelected(true);			
		}*/
		xTableHeader.repaint();
		
		last_header = matchedRow;		
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (matchedRow != -1) {
			IDColumn column_id = (IDColumn) xTableHeader.getValueAt(matchedRow,	0);
			String sql = " SELECT P.XX_VMR_VENDORPRODREF_ID, P.M_PRODUCT_ID, P.VALUE||'-'||P.NAME, CA.XX_VMR_CATEGORY_ID, CA.VALUE||'-'||CA.NAME, "
					+ " DE.XX_VMR_DEPARTMENT_ID, DE.VALUE||'-'||DE.NAME, "
					+ " LI.XX_VMR_LINE_ID, LI.VALUE||'-'||LI.NAME, "
					+ " SE.XX_VMR_SECTION_ID, SE.VALUE||'-'||SE.NAME , "
					+ " SEA.XX_VMA_SEASON_ID, SEA.NAME,"
					+ " CO.XX_VMR_Collection_ID, CO.NAME, "
					+ " PA.XX_VMR_PACKAGE_ID, PA.NAME, "
					+ " BR.XX_VMR_BRAND_ID, BR.VALUE||'-'||BR.NAME, "
					+ " DET.XX_PRODUCTBATCH_ID,  DET.XX_PRODUCTQUANTITY, XX_VMR_ORDERREQUESTDETAIL_ID, VEN.VALUE||'-'||VEN.NAME FROM "
					+ " XX_VMR_ORDERREQUESTDETAIL DET "
					+ " LEFT OUTER JOIN  XX_VMR_CATEGORY CA ON (DET.XX_VMR_CATEGORY_ID = CA.XX_VMR_CATEGORY_ID) "
					+ " LEFT OUTER JOIN  XX_VMR_DEPARTMENT DE ON (DET.XX_VMR_DEPARTMENT_ID = DE.XX_VMR_DEPARTMENT_ID ) "
					+ " LEFT OUTER JOIN  XX_VMR_LINE LI ON (DET.XX_VMR_LINE_ID = LI.XX_VMR_LINE_ID) "
					+ " LEFT OUTER JOIN  XX_VMR_SECTION SE ON (DET.XX_VMR_SECTION_ID = SE.XX_VMR_SECTION_ID) "
					+ " LEFT OUTER JOIN  XX_VMA_SEASON SEA ON (DET.XX_VMA_SEASON_ID = SEA.XX_VMA_SEASON_ID ) "
					+ " LEFT OUTER JOIN  XX_VMR_Collection CO ON (DET.XX_VMR_Collection_ID = CO.XX_VMR_Collection_ID) "
					+ " LEFT OUTER JOIN  XX_VMR_PACKAGE PA ON (DET.XX_VMR_PACKAGE_ID = PA.XX_VMR_PACKAGE_ID) "
					+ " LEFT OUTER JOIN  M_PRODUCT P ON (P.M_PRODUCT_ID = DET.M_PRODUCT_ID) "
					+ " LEFT OUTER JOIN  XX_VMR_BRAND BR ON (P.XX_VMR_BRAND_ID = BR.XX_VMR_BRAND_ID) "
					+ " LEFT OUTER JOIN  C_BPARTNER VEN ON (P.C_BPARTNER_ID = VEN.C_BPARTNER_ID) "
					+ " WHERE DET.XX_VMR_ORDER_ID = "
					+ column_id.getRecord_ID()
					+ "ORDER BY P.VALUE||'-'||P.NAME";
			

			int row = 0;
			xTableDetail.setRowCount(row);
			PreparedStatement ps = DB.prepareStatement(sql, null);
			ResultSet rs = null;
			try {
				rs = ps.executeQuery();				
				while (rs.next()) {
					xTableDetail.setRowCount(row + 1);
					X_XX_VMR_VendorProdRef ref = 
						new X_XX_VMR_VendorProdRef(Env.getCtx(), rs.getInt(1), null);
					xTableDetail
							.setValueAt(new KeyNamePair(ref.get_ID(),ref.getDescription()), row, 0);
					xTableDetail.setValueAt(new KeyNamePair(rs.getInt(2), rs
							.getString(3)), row, 1);
					
					xTableDetail.setValueAt(rs.getInt(21), row, 2);
					
					if (reimpresion > 0) {
						xTableDetail.setValueAt(rs.getInt(21), row, 3);					
						IDColumn id = new IDColumn(rs.getInt(22));
						xTableDetail.setValueAt(id, row, 4);	
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(4), rs.getString(5)), row, 5);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(6), rs.getString(7)), row, 6);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(8), rs.getString(9)), row, 7);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(10), rs.getString(11)), row, 8);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(12), rs.getString(13)), row, 9);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(14), rs.getString(15)), row, 10);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(16), rs.getString(17)), row, 11);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(18), rs.getString(19)), row, 12);
						xTableDetail.setValueAt(rs.getInt(20), row, 11);
					} else {
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(4), rs.getString(5)), row, 3);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(6), rs.getString(7)), row, 4);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(8), rs.getString(9)), row, 5);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(10), rs.getString(11)), row, 6);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(12), rs.getString(13)), row, 7);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(14), rs.getString(15)), row, 8);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(16), rs.getString(17)), row, 9);
						xTableDetail.setValueAt(new KeyNamePair(rs.getInt(18), rs.getString(19)), row, 10);
						xTableDetail.setValueAt(rs.getInt(20), row, 11);
						/*AGREGADO POR GHUCHET*/
						xTableDetail.setValueAt(rs.getString(23), row, 12);
						/*HASTA AQUI POR GHUCHET*/
						IDColumn ID = new IDColumn(rs.getInt(22));
						xTableDetail.setValueAt(ID, row, 13);
					}
					row++;
				}
				rs.close();
				ps.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
		}		
		m_frame.setCursor(Cursor.getDefaultCursor());
		xTableDetail.autoSize(true);
		xTableDetail.repaint();
		xMatchedToScrollPane.repaint();

	}

	private void jbInit() throws Exception {
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);

		distributionLabel.setText(Msg.translate(Env.getCtx(), "XX_DistributionHeader"));
		brandLabel.setText(Msg.getMsg(Env.getCtx(), "XX_Brand"));
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_Category"));
		packageLabel.setText(Msg.translate(Env.getCtx(), "Package"));
		departmentLabel.setText(Msg.translate(Env.getCtx(), "XX_Department_I"));
		seasonLabel.setText(Msg.translate(Env.getCtx(), "XX_Season"));
		storeLabel.setText(Msg.translate(Env.getCtx(), "XX_Store"));
		lineLabel.setText(Msg.translate(Env.getCtx(), "XX_Line_I"));
		sectionLabel.setText(Msg.translate(Env.getCtx(), "XX_Section_I"));
		isFromPOLabel.setText(Msg.translate(Env.getCtx(), "XX_PurchaseOrder"));
		collectionLabel.setText(Msg.translate(Env.getCtx(), "XX_Collection"));
		onlyProductLabel.setText(Msg.translate(Env.getCtx(), "M_Product_ID"));
		orderDateLabel.setText(Msg.getMsg(Env.getCtx(), "DateFrom"));
		datefromlabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
		statusLabel.setText(Msg.translate(Env.getCtx(), "XX_PlacedOrderStatus"));
		typeLabel.setText(Msg.translate(Env.getCtx(), "XX_PlacedOrderType"));
		placedOrderLabel
				.setText(Msg.getMsg(Env.getCtx(), "XX_BecoCorrelative"));
		
		locatorLabel.setText(Msg.translate(Env.getCtx(), "M_Locator_ID"));
		vendorLabel.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		
		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bClear.setText(Msg.translate(Env.getCtx(), "Clear"));
		bPrint.setText(Msg.translate(Env.getCtx(), "XX_PrintSelectedLabels"));
		bPrintPlacedOrders.setText(Msg.translate(Env.getCtx(), "XX_PrintPlacedOrder"));
		southPanel.setLayout(southLayout);
		xMatchedLabel.setText(Msg.translate(Env.getCtx(), "ToBeMatched"));
		xMatchedToLabel.setText(Msg.translate(Env.getCtx(), "Matching"));
		differenceLabel.setText(Msg.translate(Env.getCtx(), "Difference"));
		centerPanel.setLayout(centerLayout);
		xMatchedScrollPane.setBorder(xMatchedBorder);
		xMatchedScrollPane.setMinimumSize(new Dimension(800, 160));

		xMatchedToScrollPane.setBorder(xMatchedToBorder);
		xMatchedToScrollPane.setMinimumSize(new Dimension(800, 250));
		placedOrderText.setPreferredSize(new Dimension(100, 20));

		northPanel.add(categoryLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(categoryCombo, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(brandLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(brandCombo, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(onlyProductLabel, new GridBagConstraints(4, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(productSearch, new GridBagConstraints(5, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(typeLabel, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(typeCombo, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
						12, 0, 5, 12), 0, 0));

		northPanel.add(departmentLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(departmentCombo, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(lineLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(lineCombo, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(sectionLabel, new GridBagConstraints(4, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(sectionCombo, new GridBagConstraints(5, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(statusLabel, new GridBagConstraints(6, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(statusCombo, new GridBagConstraints(7, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(12, 0, 5, 12), 0, 0));

		northPanel.add(seasonLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(seasonCombo, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(collectionLabel, new GridBagConstraints(2, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(collectionCombo, new GridBagConstraints(3, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(packageLabel, new GridBagConstraints(4, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(packageCombo, new GridBagConstraints(5, 2, 2, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 150), 0, 0));
		northPanel.add(storeLabel, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(storeCombo, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
						12, 0, 5, 12), 0, 0));

		northPanel.add(isFromPOLabel, new GridBagConstraints(0, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(purchaseSearch, new GridBagConstraints(1, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(orderDateLabel, new GridBagConstraints(2, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(dateFrom, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(datefromlabel, new GridBagConstraints(4, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(dateTo, new GridBagConstraints(5, 3, 2, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 150), 0, 0));
		northPanel.add(placedOrderLabel, new GridBagConstraints(6, 3, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(placedOrderText, new GridBagConstraints(7, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 12), 0, 0));
		northPanel.add(distributionLabel, new GridBagConstraints(0, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(distributionCombo, new GridBagConstraints(1, 4, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(vendorLabel, new GridBagConstraints(0, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(vendorCombo, new GridBagConstraints(1, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(locatorLabel, new GridBagConstraints(2, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(locatorCombo, new GridBagConstraints(3, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		
		Box box = Box.createHorizontalBox();
		box.add(bClear);
		box.add(Box.createHorizontalStrut(30));
		box.add(bSearch);
		
		if (reimpresion > 0) {
			box.add(Box.createHorizontalStrut(30));
			box.add(bPrintPlacedOrders);
			box.add(Box.createHorizontalStrut(30));
			box.add(bPrint);
			
			northPanel.add(box, new GridBagConstraints(3, 4, 5, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 0, 0, 12), 0, 0));
		} else {
		//ADD GHUCHET
			box.add(Box.createHorizontalStrut(60));
			box.add(labelFile);
			box.add(Box.createHorizontalStrut(5));
			box.add(bFile);
			box.add(Box.createHorizontalStrut(5));
			box.add(bExport);
			northPanel.add(box, new GridBagConstraints(5, 4, 5, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 0, 0, 12), 0, 0));
		//FIN GHUCHET
			//northPanel.add(box, new GridBagConstraints(7, 4, 2, 1, 0.0, 0.0,
			//		GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 0, 0, 12), 0, 0));
		}		
		
		xMatchedScrollPane.getViewport().add(xTableHeader);
		xMatchedToScrollPane.getViewport().add(xTableDetail);

		centerPanel.add(xMatchedScrollPane, BorderLayout.NORTH);
		centerPanel.add(xMatchedToScrollPane, BorderLayout.SOUTH);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		// mainPanel.add(southPanel, BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		m_frame.pack();
		m_frame.setExtendedState(m_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH); 
		//m_frame.setResizable(false);

	} // jbInit

	public void printSelectedLabels(X_XX_VMR_Order header) {
		xTableDetail.stopEditor(true);
		
		//Se quito el calculo del consecutivo, se agrego al proceso
		
    	Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);    	
    	m_frame.setCursor(hourglassCursor);
		
		int id_label_glued = Env.getCtx().getContextAsInt("#XX_L_TYPELABELENGOMADA_ID");		
		int id_label_flat = Env.getCtx().getContextAsInt("#XX_L_TYPELABELCOLGANTE_ID");
       	
		int glued = 0, flats = 0;
    	for (int row = 0; row < xTableDetail.getRowCount() ; row++) {
    		//En el detalle el id column esta en la columna 4
    		IDColumn idcol = (IDColumn)xTableDetail.getValueAt(row, 4); 
    		if (idcol != null && idcol.isSelected()) {
    			KeyNamePair product_kp = (KeyNamePair)xTableDetail.getValueAt(row, 1);
    			MProduct product = new MProduct(Env.getCtx(), product_kp.getKey(), null);
    			
    			if (product.getXX_VMR_TypeLabel_ID() == id_label_glued ){
    				glued++;
    			}  else if ( product.getXX_VMR_TypeLabel_ID() == id_label_flat ) {
    				flats++;    				
    			} else {
    				X_XX_VMR_TypeLabel label_type = 
    					new X_XX_VMR_TypeLabel(Env.getCtx(), product.getXX_VMR_TypeLabel_ID(), null);
    				
    				String mss = Msg.getMsg(Env.getCtx(), "XX_WrongLabelType", 
    						//Order					
    						new String[] {label_type.getValue(),label_type.getName()});    				  				
    				ADialog.error(m_WindowNo, m_frame, mss); 
    		    	m_frame.setCursor(Cursor.getDefaultCursor());  				
    				return;
    			}    			
    		}
    	}
    	
    	//Se verificaron las etiquetas    	
		for (int row = 0; row < xTableDetail.getRowCount() ; row++) {
    		IDColumn idcol = (IDColumn)xTableDetail.getValueAt(row, 4); 
    		if (idcol != null && idcol.isSelected()) {
    			//El product es el keyName pair de la columna 1
    			KeyNamePair product_kp = (KeyNamePair)xTableDetail.getValueAt(row, 1);
    			MProduct product = new MProduct(Env.getCtx(), product_kp.getKey(), null);
    			
    			if (product.getXX_VMR_TypeLabel_ID() == id_label_glued ){
    				print_labels (this.glued, row, true);
    			}  else {
    				print_labels (this.flat, row, false);
    			} 
    		}
    	}	
		m_frame.setCursor(Cursor.getDefaultCursor());
		if (flats + glued > 0 ) {
			ADialog.info(m_WindowNo, m_frame, "XX_PrintedLabels");
    	}	
		m_frame.setCursor(Cursor.getDefaultCursor());
    }

	
	/** Imprime un pedido completo */
	public boolean printSelectedPlacedOrder(X_XX_VMR_Order header) {
				
		//Se quito el calculo del consecutivo, se agrego al proceso
			
		int id_label_glued = Env.getCtx().getContextAsInt("#XX_L_TYPELABELENGOMADA_ID");		
		int id_label_flat = Env.getCtx().getContextAsInt("#XX_L_TYPELABELCOLGANTE_ID");
       	
		int glued = 0, flats = 0;
    	
		String sql = 
			" SELECT P.M_PRODUCT_ID, P.VALUE||'-'||P.NAME, DET.XX_PRODUCTQUANTITY, XX_VMR_ORDERREQUESTDETAIL_ID FROM " +
			" XX_VMR_ORDERREQUESTDETAIL DET JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DET.M_PRODUCT_ID) " +
			" WHERE DET.XX_VMR_ORDER_ID = " + header.get_ID() + " ORDER BY P.VALUE||'-'||P.NAME";

		PreparedStatement ps = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY, null);
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
	
    			MProduct product = new MProduct(Env.getCtx(), rs.getInt(1), null);
    			if (product.getXX_VMR_TypeLabel_ID() == id_label_glued ){
    				glued++;
    			}  else if ( product.getXX_VMR_TypeLabel_ID() == id_label_flat ) {
    				flats++;    				
    			} else {
    				X_XX_VMR_TypeLabel label_type = 
    					new X_XX_VMR_TypeLabel(Env.getCtx(), product.getXX_VMR_TypeLabel_ID(), null);
    				
    				String mss = Msg.getMsg(Env.getCtx(), "XX_WrongLabelType", 
    						//Order					
    						new String[] {label_type.getValue(),label_type.getName()});    				  				
    				ADialog.error(m_WindowNo, m_frame, mss); 
    		    	m_frame.setCursor(Cursor.getDefaultCursor()); 
    		    	
    		    	rs.close();
    		    	ps.close();
    				return false;
    			}  				
			}
			
			//Se itera de nuevo
			rs.beforeFirst();
			while (rs.next()) {
				MProduct product = new MProduct(Env.getCtx(), rs.getInt(1), null);
				KeyNamePair knp_producto = new KeyNamePair(rs.getInt(1), rs.getString(2));
				Integer product_qty = rs.getInt(3);
				X_XX_VMR_OrderRequestDetail detalle = 
					new X_XX_VMR_OrderRequestDetail(Env.getCtx(), rs.getInt(4), null);
				if (product.getXX_VMR_TypeLabel_ID() == id_label_glued ){
    				print_labels (this.glued, detalle, product, knp_producto, product_qty, true);
    			}  else {
    				print_labels (this.flat, detalle, product, knp_producto, product_qty, true);
    			} 
			}
			rs.close();
			ps.close();
    	} catch (Exception e) {
			return false;
		}			
		if (flats + glued > 0 ) {
			return true;
    	}	
		return false;
    }

	/** Imprime las etiquetas de los detalles de un pedido, que estan en la pantalla*/
	public void print_labels (PrintService psZebra, int row, boolean glued) {
			
		try {  			
			IDColumn idcol = (IDColumn)xTableDetail.getValueAt(row, 4);
			KeyNamePair knp_producto = (KeyNamePair)xTableDetail.getValueAt(row, 1);
			MProduct producto = new MProduct(Env.getCtx(), knp_producto.getKey(), null);
					
			MAttributeSetInstance attr = new MAttributeSetInstance(Env.getCtx(), producto.getM_AttributeSetInstance_ID(), null);
			KeyNamePair knp_att = new KeyNamePair(attr.get_ID(), attr.getDescription());			
			X_XX_VMR_OrderRequestDetail detail =
				new X_XX_VMR_OrderRequestDetail(Env.getCtx(), idcol.getRecord_ID(), null);
			int cantidadEtiquetas = ((Number)xTableDetail.getValueAt(row, 3)).intValue();
			DecimalFormat formato = new DecimalFormat("000");	
			String correlativo = formato.format(detail.getXX_PriceConsecutive());
			Utilities.print_labels(psZebra, knp_producto, knp_att, correlativo, detail, cantidadEtiquetas, 0, glued);
		}
		catch (Exception e) {
			e.printStackTrace();  
		}
	}
	
	/** Imprime las etiquetas de los detalles de un pedido */
	public void print_labels (PrintService psZebra, X_XX_VMR_OrderRequestDetail detail,  
			MProduct producto, KeyNamePair knp_producto, int cantidadEtiquetas,
			boolean glued
	) {
		try {  					
			MAttributeSetInstance attr = new MAttributeSetInstance(Env.getCtx(), producto.getM_AttributeSetInstance_ID(), null);
			KeyNamePair knp_att = new KeyNamePair(attr.get_ID(), attr.getDescription());			
			DecimalFormat formato = new DecimalFormat("000");	
			String correlativo = formato.format(detail.getXX_PriceConsecutive());
			Utilities.print_labels(psZebra, knp_producto, knp_att, correlativo, detail, cantidadEtiquetas, 0, glued);
		}
		catch (Exception e) {
			e.printStackTrace();  
		}
	
	}

	@Override
	public void tableChanged(TableModelEvent e) {

		if (reimpresion > 0) {
			//Solo escucha los eventos de la tabla detalle
			//3 es la columna de la cantidad de etiquetas
			if (e.getColumn() != -1 && e.getColumn() == 3) {
				try {
					Integer valor = (Integer)xTableDetail.getValueAt(e.getFirstRow(), 3);
					Integer tope = (Integer)xTableDetail.getValueAt(e.getFirstRow(), 2);
					if (valor < 0) {
						ADialog.error(m_WindowNo, m_frame, "XX_PostiveLesserValuesOnly");
						xTableDetail.setValueAt(tope, e.getFirstRow(), 3);
					} else if (valor > tope) {
						ADialog.error(m_WindowNo, m_frame, "XX_PostiveLesserValuesOnly");
						xTableDetail.setValueAt(tope, e.getFirstRow(), 3);					
					}				
				} catch (NumberFormatException h) {}
			}
		} else {
			
			/*REMODIFICAR Este codigo garantiza la edicion, pero esta inactivo
			if (e.getFirstRow() != -1 && e.getColumn() != -1 && e.getColumn() == 2) {
				try {					
					Integer valor = (Integer)xTableDetail.getValueAt(e.getFirstRow(), 2);
					IDColumn detalle = (IDColumn)xTableDetail.getValueAt(e.getFirstRow(), 12);
					
					//Actualiza la cantidad de piezas del detalle
					if (detalle != null) {
						
						//Crea el detalle
						X_XX_VMR_OrderRequestDetail detail = new X_XX_VMR_OrderRequestDetail(Env.getCtx(), detalle.getRecord_ID(), null);
						
						//Solo si la cantidad es positiva
						if (valor < 0) {
							ADialog.error(m_WindowNo, m_frame, "XX_PostiveLesserValuesOnly");
							xTableDetail.setValueAt(detail.getXX_ProductQuantity(), e.getFirstRow(), 2);
						} else {
							detail.setXX_ProductQuantity(valor);
							detail.save();
						} 	
					}
				} catch (NumberFormatException h) {}
			}	*/		
		} 
		
	}
	   
	private void createReport(){
		
		exportTable = new MiniTablePreparator();
		/* Add the column definition for the table */
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg
						.translate(Env.getCtx(), "XX_BecoCorrelative"), ".",
						String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Store"), ".",
						String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PurchaseOrder"),
						".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DistributionHeader"),
						".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PlacedOrderType"), ".",
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PlacedOrderStatus"), ".", 
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PackageQuantity"), ".",
						Integer.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReceived"), ".",
						Integer.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Date")+" de Estado", ".",
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_WeekCreated"),
						".", String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Date")+ " de Creación",
								".", String.class, "."), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AssigmentDate"),
						".", String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DispatchGuide"),
						".", String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "VendorProductRef"), 
						".", KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"),
						".", KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Qty"),
						".",	Integer.class, true, false, "." ),	
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_SalePrice"), ".",
								Double.class, "."),
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Category"), ".",
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Department_I"),
						".", String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Line_I"), ".",
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Section_I"), ".",
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Season"), ".",
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Collection"),
						".", String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Package"), ".",
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Brand"), ".",
						String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Batch"), ".",Integer.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".",Integer.class),
		};
	
		exportTable.prepareTable(layout, "", "", false, "");
		
		StringBuilder sql = new StringBuilder();
		sql.append("\nSELECT P.XX_VMR_ORDER_ID, P.XX_ORDERBECOCORRELATIVE,  "
			+ "\nS.VALUE||'-'||S.NAME, O.DOCUMENTNO, P.XX_VMR_DISTRIBUTIONHEADER_ID, P.XX_ORDERREQUESTTYPE , P.XX_ORDERREQUESTSTATUS, "
			+ "\nP.XX_PACKAGEQUANTITY, " 
			+ "\nTO_CHAR (case when xx_orderrequeststatus = 'TI' then xx_datestatusonstore "
			+ "            when xx_orderrequeststatus = 'ET' then xx_datestatustransit "
			+ "            when xx_orderrequeststatus = 'EB' then xx_datestatusatbay "
			+ "            when xx_orderrequeststatus = 'PE' then xx_datestatuspending "
			+ "            else P.updated end, 'DD/MM/YYYY'), "
			+ "\nP.XX_WEEKCREATED, TO_CHAR(P.CREATED, 'DD/MM/YYYY'), TO_CHAR(P.XX_ASSIGNMENTDATE, 'DD/MM/YYYY'), " 					
			+ "\nPR.XX_VMR_VENDORPRODREF_ID, "
			+ "\nPR.VALUE||'-'||PR.NAME, DET.XX_PRODUCTQUANTITY, DET.XX_SALEPRICE,  "
			+ "\nCA.VALUE||'-'||CA.NAME, "
			+ "\nDE.VALUE||'-'||DE.NAME, "
			+ "\nLI.VALUE||'-'||LI.NAME, "
			+ "\nSE.VALUE||'-'||SE.NAME , "
			+ "\nSEA.NAME, CO.NAME, PA.NAME, "
			+ "\nBR.VALUE||'-'||BR.NAME, "
			+ "\nDET.XX_PRODUCTBATCH_ID, "
			+ "\nVEN.VALUE||'-'||VEN.NAME "
			+ "\nFROM XX_VMR_ORDER P "
			+ "\nINNER JOIN XX_VMR_ORDERREQUESTDETAIL DET ON (P.XX_VMR_ORDER_ID = DET.XX_VMR_ORDER_ID)" 
			+ "\nLEFT OUTER JOIN C_ORDER O ON (P.C_ORDER_ID = O.C_ORDER_ID ) "
			+ "\nINNER JOIN M_WAREHOUSE S ON (P.M_WAREHOUSE_ID = S.M_WAREHOUSE_ID)"
			+ "\nLEFT OUTER JOIN  XX_VMR_CATEGORY CA ON (DET.XX_VMR_CATEGORY_ID = CA.XX_VMR_CATEGORY_ID) "
			+ "\nLEFT OUTER JOIN  XX_VMR_DEPARTMENT DE ON (DET.XX_VMR_DEPARTMENT_ID = DE.XX_VMR_DEPARTMENT_ID ) "
			+ "\nLEFT OUTER JOIN  XX_VMR_LINE LI ON (DET.XX_VMR_LINE_ID = LI.XX_VMR_LINE_ID) "
			+ "\nLEFT OUTER JOIN  XX_VMR_SECTION SE ON (DET.XX_VMR_SECTION_ID = SE.XX_VMR_SECTION_ID) "
			+ "\nLEFT OUTER JOIN  XX_VMA_SEASON SEA ON (DET.XX_VMA_SEASON_ID = SEA.XX_VMA_SEASON_ID ) "
			+ "\nLEFT OUTER JOIN  XX_VMR_Collection CO ON (DET.XX_VMR_Collection_ID = CO.XX_VMR_Collection_ID) "
			+ "\nLEFT OUTER JOIN  XX_VMR_PACKAGE PA ON (DET.XX_VMR_PACKAGE_ID = PA.XX_VMR_PACKAGE_ID) "
			+ "\nLEFT OUTER JOIN  M_PRODUCT PR ON (PR.M_PRODUCT_ID = DET.M_PRODUCT_ID) "
			+ "\nLEFT OUTER JOIN  XX_VMR_BRAND BR ON (PR.XX_VMR_BRAND_ID = BR.XX_VMR_BRAND_ID) "
			+ "\nLEFT OUTER JOIN  C_BPARTNER VEN ON (PR.C_BPARTNER_ID = VEN.C_BPARTNER_ID) "
			+ "\nWHERE P.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() );

		if(locatorCombo.getValue() != null){
			KeyNamePair locator = (KeyNamePair) locatorCombo.getValue();
			if (locator.getKey() != -1)
				sql.append(" AND P.AD_ORG_ID =").append(locator.getKey()).append(" ");;
		}
		
		if(vendorCombo.getValue() != null){
			KeyNamePair vendor = (KeyNamePair) vendorCombo.getValue();
			if (vendor.getKey() != -1)
				sql.append(" AND VEN.C_BPARTNER_ID =").append(vendor.getKey()).append(" ");;
		}
		
		if (categoryCombo.getValue() != null) {
			KeyNamePair cat = (KeyNamePair) categoryCombo.getValue();
			if (cat.getKey() != -1)
				sql.append(" AND DET.XX_VMR_CATEGORY_ID = ").append(
						cat.getKey()).append(" ");
		}
		if (departmentCombo.getValue() != null) {
			KeyNamePair dep = (KeyNamePair) departmentCombo.getValue();
			if (dep.getKey() != -1)
				sql.append(" AND DET.XX_VMR_DEPARTMENT_ID = ").append(
						dep.getKey()).append(" ");
		}
		if (lineCombo.getValue() != null) {
			KeyNamePair lin = (KeyNamePair) lineCombo.getValue();
			if (lin.getKey() != -1)
				sql.append(" AND DET.XX_VMR_LINE_ID = ").append(lin.getKey())
						.append(" ");
		}
		if (sectionCombo.getValue() != null) {
			KeyNamePair sec = (KeyNamePair) sectionCombo.getValue();
			if (sec.getKey() != -1)
				sql.append(" AND DET.XX_VMR_SECTION_ID = ")
						.append(sec.getKey()).append(" ");
		}
		if (seasonCombo.getValue() != null) {
			KeyNamePair sea = (KeyNamePair) seasonCombo.getValue();
			if (sea.getKey() != -1)
				sql.append(" AND DET.XX_VMA_SEASON_ID = ").append(sea.getKey())
						.append(" ");
		}
		if (collectionCombo.getValue() != null) {
			KeyNamePair col = (KeyNamePair) collectionCombo.getValue();
			if (col.getKey() != -1)
				sql.append(" AND DET.XX_VMR_Collection_ID = ").append(
						col.getKey()).append(" ");
		}
		if (packageCombo.getValue() != null) {
			KeyNamePair pac = (KeyNamePair) packageCombo.getValue();
			if (pac.getKey() != -1)
				sql.append(" AND DET.XX_VMR_PACKAGE_ID = ")
						.append(pac.getKey()).append(" ");
		}
		if (brandCombo.getValue() != null) {
			KeyNamePair bra = (KeyNamePair) brandCombo.getValue();
			if (bra.getKey() != -1)
				sql.append(" AND PR.XX_VMR_BRAND_ID = ").append(bra.getKey())
						.append(" ");
		}
		if (productSearch.getValue() != null) {
			Integer p_id = (Integer) productSearch.getValue();
			sql.append(" AND DET.M_PRODUCT_ID = ").append(p_id).append(" ");
		}
		if (purchaseSearch.getValue() != null) {
			Integer po_id = (Integer) purchaseSearch.getValue();
			sql.append(" AND P.C_ORDER_ID = ").append(po_id).append(" ");
		}
		if (storeCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) storeCombo.getValue();
			if (sto.getKey() != -1)
				sql.append(" AND  P.M_WAREHOUSE_ID = ").append(sto.getKey())
						.append(" ");
		}
		if (statusCombo.getSelectedIndex() != -1
				&& statusCombo.getSelectedIndex() != 0) {
			sql.append(" AND P.XX_ORDERREQUESTSTATUS = ").append(
					"'" + status_name.elementAt(statusCombo.getSelectedIndex())
							+ "'").append(" ");
		}
		if (typeCombo.getSelectedIndex() != -1
				&& typeCombo.getSelectedIndex() != 0) {
			sql.append(" AND P.XX_ORDERREQUESTTYPE = ").append(
					"'" + type_name.elementAt(typeCombo.getSelectedIndex())
							+ "'").append(" ");
		}
		if (distributionCombo.getValue() != null) {
			KeyNamePair sto = (KeyNamePair) distributionCombo.getValue();
			if (sto.getKey() != -1)
				sql.append(" AND P.XX_VMR_DISTRIBUTIONHEADER_ID = ").append(sto.getKey()).append(" ");
		}
		if (!placedOrderText.getText().equals("")) {
			sql.append(" AND P.XX_ORDERBECOCORRELATIVE = ").append(
					"'" + placedOrderText.getText().trim() + "'").append(" ");
		}

		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		if (from != null && to != null) {
			sql.append(" AND TRUNC(P.CREATED) ").append(" >= ").append(
					DB.TO_DATE(from)).append(" ");
			sql.append(" AND TRUNC(P.CREATED) ").append(" <= ").append(
					DB.TO_DATE(to)).append(" ");
		} else if (from != null)
			sql.append(" AND TRUNC(P.CREATED) ").append(" >= ").append(
					DB.TO_DATE(from, true)).append(" ");
		else if (to != null)
			sql.append(" AND TRUNC(P.CREATED) ").append(" <= ").append(
					DB.TO_DATE(to)).append(" ");

		sql.append("\nORDER BY S.VALUE||'-'||S.NAME, P.XX_ORDERBECOCORRELATIVE, PR.VALUE||'-'||PR.NAME ");
		
		int row = 0;
			exportTable.setRowCount(row);
			PreparedStatement ps = DB.prepareStatement(sql.toString(), null);
			
		//Crear un Prepared Statement para la consulta interna de guias de despacho
		String sql_guiasdespacho = "SELECT G.DOCUMENTNO FROM XX_VLO_DISPATCHGUIDE G JOIN XX_VLO_DETAILDISPATCHGUIDE D " +
				" ON (G.XX_VLO_DISPATCHGUIDE_ID = D.XX_VLO_DISPATCHGUIDE_ID) " + 				
				" WHERE G.XX_DISPATCHGUIDESTATUS IN ('TIE','TRA') " +
				" AND D.XX_TYPEDETAILDISPATCHGUIDE = 'MCD' " +
				" AND D.XX_VMR_ORDER_ID = ? ORDER BY G.DOCUMENTNO";
		
		ResultSet rs = null;
		PreparedStatement ps_guiasdespacho = DB.prepareStatement(sql_guiasdespacho, null);
		ResultSet rs_guiasdespacho = null;

		try {
			rs = ps.executeQuery();				
			while (rs.next()) {
				exportTable.setRowCount(row + 1);
					
				exportTable.setValueAt(rs.getString(2), row, 0); //Beco Correlative
				exportTable.setValueAt(rs.getString(3), row, 1); //Store
				exportTable.setValueAt(rs.getString(4), row, 2); //Document nro O/C
				exportTable.setValueAt(rs.getString(5), row, 3); //Distribution Header				
				for (X_Ref_XX_OrderRequestType v : X_Ref_XX_OrderRequestType.values()) {
					if (v.getValue().equals(rs.getString(6))) {
						exportTable.setValueAt(v, row, 4); //Placed Order Type
						break;
					}
				}
				for (X_Ref_XX_VMR_OrderStatus v : X_Ref_XX_VMR_OrderStatus.values()) {
					if (v.getValue().equals(rs.getString(7))) {
						exportTable.setValueAt(v, row, 5); //Placed Order Status
						break;
					}
				}
				exportTable.setValueAt(rs.getInt(8), row, 6); //Package Qty
					
				int bultos_recibidos = 0;
				// TODO Calcular los bultos recibidos
				exportTable.setValueAt(bultos_recibidos, row, 7);
				
				exportTable.setValueAt(rs.getString(9), row, 8); //Date
				exportTable.setValueAt(rs.getInt(10), row, 9); //Week Created
				exportTable.setValueAt(rs.getString(11), row, 10); // Assigment Date
				exportTable.setValueAt(rs.getString(12), row, 11); //Approval Date
				
				//Obtener los numeros de documentos de la guia de despacho
				ps_guiasdespacho.setInt(1, rs.getInt(1));
				try {
					rs_guiasdespacho = ps_guiasdespacho.executeQuery();
				
					//Colocar numero de documento en el campo 
					String guia_despacho = "";					
					int i = 0;
					while (rs_guiasdespacho.next()) {
						if (rs_guiasdespacho.getString(1) != null)
						if (i++ > 0)
							guia_despacho += " / ";
						guia_despacho += rs_guiasdespacho.getString(1);						
					}
					exportTable.setValueAt(guia_despacho, row, 12);					
				} catch (SQLException e) {
				e.printStackTrace();
				}finally {
					if (rs_guiasdespacho != null)
						try { 
							rs_guiasdespacho.close();
						} catch (SQLException e) {}
					rs_guiasdespacho = null;
				}
				//Detail
				X_XX_VMR_VendorProdRef ref = 
					new X_XX_VMR_VendorProdRef(Env.getCtx(), rs.getInt(13), null);
				exportTable.setValueAt(ref.getDescription(), row, 13); //Vendor Product Ref
				exportTable.setValueAt(rs.getString(14), row, 14); //Product Description
				exportTable.setValueAt(rs.getInt(15), row, 15); //Product Qty
				exportTable.setValueAt(rs.getInt(16), row, 16); //Sale Price
				exportTable.setValueAt(rs.getString(17), row, 17); //Category
				exportTable.setValueAt(rs.getString(18), row, 18); //Department
				exportTable.setValueAt(rs.getString(19), row, 19); //Line
				exportTable.setValueAt(rs.getString(20), row, 20); //Section	
				exportTable.setValueAt(rs.getString(21), row, 21); //Season
				exportTable.setValueAt(rs.getString(22), row, 22); //Collection
				exportTable.setValueAt(rs.getString(23), row, 23); //Package
				exportTable.setValueAt(rs.getString(24), row, 24); //Brand
				exportTable.setValueAt(rs.getInt(25), row, 25); //Batch		
				exportTable.setValueAt(rs.getString(26), row, 26); //Brand
				row++;	
			}
		} catch (SQLException e) {
				System.out.println(sql.toString());
				e.printStackTrace();
		}finally {
			try {
				rs.close();
				ps.close();
				ps_guiasdespacho.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		exportTable.repaint();
	}
	
}
