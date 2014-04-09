package compiere.model.dynamic.forms;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
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
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_VendorProdRef;
import compiere.model.dynamic.MVMABrochurePage;
import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_XX_VMA_MarketingActivity;
import compiere.model.dynamic.X_XX_VME_Elements;
import compiere.model.dynamic.X_XX_VME_Reference;

/** XX_OrderConsultForm (Funcion 14)
 * Forma que permite consultar órdenes de compra y pedidos de acuerdo a departamento,
 * línea, sección y producto, desde un elemento de un folleto.
 * Con el resultado de la consulta se pueden agregar productos a un elemento del
 * folleto al seleccionar uno o más productos.
 * @author María Vintimilla
 * @version 1.0
 * */

public class XX_VME_ConsultPO_Form extends CPanel implements FormPanel,
		ActionListener, ListSelectionListener, TableModelListener {
	/** Window No */
	private int m_WindowNo = 0;
	/** FormFrame */
	private FormFrame m_frame;
	
	/** Logger */
	static CLogger log = CLogger.getCLogger(XX_VME_ConsultPO_Form.class);

	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();
	
	/** Model Index of Key Column */
	private int m_keyColumnIndex = -1;
	
	// Variable
	public boolean SearchType = true;

	private final int h_id = 0, h_order = 1, h_status = 2, h_product = 3,
			h_date = 4;
	private static final long serialVersionUID = 1L;
	Integer last_header = -1;

	// Panel
	private CPanel mainPanel = new CPanel();
	private CPanel northPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	
	// Botones
	private CButton bSearch = new CButton();
	private CButton insert = new CButton();
	
	//Radio Buttons
	private JRadioButton C_Order = 
		new JRadioButton(Msg.translate(Env.getCtx(), "XX_PO_C_OrderSearch"),true);
	private JRadioButton P_Order = 
		new JRadioButton(Msg.translate(Env.getCtx(), "XX_PO_X_OrderSearch"),false);
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	private CLabel C_OrderLabel = new CLabel();
	private CLabel P_OrderLabel = new CLabel();
	
	//StatusBar
	private StatusBar statusBar = new StatusBar();
	
	//BorderLayout	
	private BorderLayout mainLayout = new BorderLayout();
	
	//GridBagLayout
	private GridBagLayout northLayout = new GridBagLayout();
	private GridBagLayout southLayout = new GridBagLayout();
	
	// VLookup
	protected CLabel productLabel = new CLabel(Msg.translate(Env.getCtx(), "M_Product_ID"));
	protected VLookup lookupProduct = null;	
	
	// ComboBox
	private CLabel departmentLabel = new CLabel();
	private CComboBox departmentCombo = new CComboBox();
	private CLabel lineLabel = new CLabel();
	private CComboBox lineCombo = new CComboBox();
	private CLabel sectionLabel = new CLabel();
	private CComboBox sectionCombo = new CComboBox();
	private CLabel orderLabel = new CLabel();
	private CComboBox orderCombo = new CComboBox();
	private CComboBox productCombo = new CComboBox();
	private CPanel southPanel = new CPanel();
	private CLabel xMatchedLabel = new CLabel();
	private CLabel xMatchedToLabel = new CLabel();
	private CLabel isFromPOLabel = new CLabel();
	private CLabel distributionLabel = new CLabel();
	private CLabel typeLabel = new CLabel();
	private CComboBox typeCombo = new CComboBox();
	private CLabel numLabel = new CLabel();
	private CTextField orderNum = new CTextField();
	private CTextField percentage = new CTextField();
	private CLabel percentageLabel = new CLabel();
	private CCheckBox brochure = new CCheckBox();
	private CLabel brochureLabel = new CLabel();
	private BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
	private JScrollPane xMatchedScrollPane = new JScrollPane();
	private TitledBorder xMatchedBorder = new TitledBorder("xMatched");
	private MiniTable xTableHeader = new MiniTable();
	private JScrollPane xMatchedToScrollPane = new JScrollPane();
	private TitledBorder xMatchedToBorder = new TitledBorder("xMatchedTo");
	private MiniTable xTableDetail = new MiniTable();

	// Seleccionar todos manual en la tabla
	private CLabel labelManualAll = new CLabel("Seleccionar Manual Todos");
	private CCheckBox checkManualAll = new CCheckBox();
	
	// Seleccionar todos en la tabla
	private CLabel labelSelectAll = new CLabel("Seleccionar Todos");
	private CCheckBox checkSelectAll = new CCheckBox();
	
	// Seleccionar todos mantener en la tabla
	private CLabel labelMantainAll = new CLabel("Seleccionar Manual Todos");
	private CCheckBox checkMantainAll = new CCheckBox();
	
	// Elemento y Folleto
	private Integer elementID = new Integer(0);
	private Integer groupID = new Integer(0);
	private Integer cabecera = new Integer(0);
	private Integer corderid = new Integer(0);
	private Integer pedidoid = new Integer(0);
	private String estado = "";
	Ctx aux = Env.getCtx();	
	private X_XX_VME_Elements element = null;
	private MVMABrochurePage brochurePage = null;
	private boolean prodInsert = false;

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
			// UI
			// Se requiere un keynamepair para traer el id de la ventana
			lookupProduct = VLookup.createProduct(m_WindowNo);
			lookupProduct.setVerifyInputWhenFocusTarget(false);
			aux = Env.getCtx();	
			elementID = aux.getContextAsInt("#Element_ID");
			element = new X_XX_VME_Elements(aux, elementID, null);
			brochurePage = new MVMABrochurePage(aux, element.getXX_VMA_BrochurePage_ID(), null);
			jbInit();
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		} 
		catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	} // init
	
	/** jbInit
	 * Dibuja la forma
	 * */
	private void jbInit() throws Exception {
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		southPanel.setLayout(southLayout);
		centerPanel.setLayout(centerLayout);
		
		C_Order.addActionListener(this);
		P_Order.addActionListener(this);
		checkManualAll.addActionListener(this);
		checkSelectAll.addActionListener(this);
		checkMantainAll.addActionListener(this);
		brochure.addActionListener(this);
		
		distributionLabel.setText(Msg.translate(Env.getCtx(), "XX_DistributionHeader"));
		departmentLabel.setText(Msg.translate(Env.getCtx(), "XX_Department_I"));
		lineLabel.setText(Msg.translate(Env.getCtx(), "XX_Line_I"));
		sectionLabel.setText(Msg.translate(Env.getCtx(), "XX_Section_I"));
		productLabel.setText(Msg.translate(Env.getCtx(), "XX_PO_Product"));
		isFromPOLabel.setText(Msg.translate(Env.getCtx(), "XX_PurchaseOrder"));
		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		insert.setText(Msg.translate(Env.getCtx(), "XX_PO_Insert"));
		orderLabel.setText(Msg.translate(Env.getCtx(), "XX_OrderStatus"));
		typeLabel.setText(Msg.translate(Env.getCtx(), "XX_TypeElement"));
		numLabel.setText(Msg.translate(Env.getCtx(), "XX_OrderNumber"));
		percentageLabel.setText(Msg.translate(Env.getCtx(), "XX_Percentage"));
		labelMantainAll.setText(Msg.translate(Env.getCtx(), "XX_Mantain"));
		brochureLabel.setText(Msg.translate(Env.getCtx(), "XX_Brochure"));
		
		xMatchedLabel.setText(Msg.translate(Env.getCtx(), "ToBeMatched"));
		xMatchedToLabel.setText(Msg.translate(Env.getCtx(), "Matching"));
		
		xMatchedScrollPane.setBorder(xMatchedBorder);
		xMatchedScrollPane.setMinimumSize(new Dimension(1280, 200));
		mainPanel.setMinimumSize(new Dimension(1280, 600));

		xMatchedToScrollPane.setBorder(xMatchedToBorder);
		xMatchedScrollPane.setPreferredSize(new Dimension(1280, 200));
		
		// Radio Buttons
		northPanel.add(C_OrderLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		
		northPanel.add(P_OrderLabel, new GridBagConstraints(2, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		// Departamento
		northPanel.add(departmentLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(departmentCombo, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Línea
		northPanel.add(lineLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(lineCombo, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Sección
		northPanel.add(sectionLabel, new GridBagConstraints(4, 1, 1, 1,0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(sectionCombo, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Producto 
		northPanel.add(productLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(lookupProduct, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		
		// Estado de O/C
		northPanel.add(orderLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(orderCombo, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Número de O/C
		northPanel.add(numLabel, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(orderNum, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Seleccionar todos
		northPanel.add(labelSelectAll, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(checkSelectAll, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// SELECCIONAR TODAS
		northPanel.add(labelManualAll, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(checkManualAll, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// SELECCIONAR TODAS
		northPanel.add(labelMantainAll, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(checkMantainAll, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// Search
		northPanel.add(bSearch, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Insert
		northPanel.add(insert, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Discount
		northPanel.add(percentageLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(percentage, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Brochure
		northPanel.add(brochureLabel, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(brochure, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		xMatchedScrollPane.getViewport().add(xTableHeader);
		xMatchedToScrollPane.getViewport().add(xTableDetail);
		
		// Radio Buttons
		buttonGroup.add(C_Order);
		buttonGroup.add(P_Order);
		northPanel.add(C_Order);
		northPanel.add(P_Order);
		//northPanel.add(bClear);

		centerPanel.add(xMatchedScrollPane, BorderLayout.NORTH);
		centerPanel.add(xMatchedToScrollPane, BorderLayout.SOUTH);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		m_frame.pack();
		m_frame.setExtendedState(m_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH); 
	} // jbInit

	/** disable_dept
	 * Se desactivan línea y sección que se haya elegido previamente cuando se
	 * elimina el departamento
	 * */
	void disable_dept() {
		lineCombo.removeAllItems();
		lineCombo.setEnabled(false);
		lineCombo.setEditable(false);
		
		sectionCombo.removeAllItems();
		sectionCombo.setEnabled(false);
		sectionCombo.setEditable(false);
	} // disable_dept
	
	/** disable_line
	 * Se desactiva la sección seleccionada cuando la línea se desactiva
	 * */
	void disable_line() {
		sectionCombo.removeAllItems();
		sectionCombo.setEnabled(false);
		sectionCombo.setEditable(false);
	} // disable_line
	
	/** dynDepartament 
	 * Se muestra un combobox para que el usuario escoga departamento
	 * */
	void dynDepartament() {
		departmentCombo.removeActionListener(this);
		departmentCombo.removeAllItems();
		disable_dept();

		String sql = " SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME "
				+ " FROM XX_VMR_DEPARTMENT "
				+ " WHERE XX_VMR_DEPARTMENT_ID IN (" 
				+ " SELECT XX_VMR_DEPARTMENT_ID DEP " 
				+ " FROM XX_VMA_PAGEDEPT_V " 
				+ " WHERE XX_VMA_BROCHUREPAGE_ID = " + brochurePage.get_ID()
				+ " )"
				+ " ORDER BY VALUE||'-'||NAME ";	
		//System.out.println("SQL Dept: "+sql);
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			departmentCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				departmentCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			departmentCombo.addActionListener(this);
			departmentCombo.setEnabled(true);
			departmentCombo.setEditable(true);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // dynDepartment

	/** dynLine 
	 * Se muestra un combobox para que el usuario escoga línea
	 * */
	void dynLine() {
		KeyNamePair dep = (KeyNamePair) departmentCombo.getSelectedItem();
		lineCombo.removeActionListener(this);
		disable_line();

		if (dep == null || dep.getKey() == -1) {
			disable_dept();
		} 
		else {
			lineCombo.removeAllItems();
			String sql = " SELECT XX_VMR_LINE_ID, VALUE||'-'||NAME "
					+ " FROM XX_VMR_LINE "
					+ " WHERE XX_VMR_DEPARTMENT_ID = " 	+ dep.getKey()
					+ " ORDER BY VALUE||'-'||NAME ";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
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
			} 
			catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		} //else
	}// dynLine

	/** dynSection 
	 * Se muestra un combobox para que el usuario escoga sección
	 * */
	void dynSection() {
		KeyNamePair lin = (KeyNamePair) lineCombo.getSelectedItem();
		sectionCombo.removeActionListener(this);
		if (lin == null || lin.getKey() == -1) {
			disable_line();
		} 
		else {
			sectionCombo.removeAllItems();
			String sql = " SELECT XX_VMR_SECTION_ID, VALUE||'-'||NAME "
					+ " FROM XX_VMR_SECTION "
					+ " WHERE XX_VMR_LINE_ID = " + lin.getKey()
					+ " ORDER BY VALUE||'-'||NAME";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				sectionCombo.addItem(new KeyNamePair(-1, null));
				while (rs.next()) {
					sectionCombo.addItem(new KeyNamePair(rs.getInt(1), rs
							.getString(2)));
				}
				sectionCombo.setEnabled(true);
				sectionCombo.setEditable(true);
				sectionCombo.addActionListener(this);

			} 
			catch (SQLException e) { log.log(Level.SEVERE, sql, e); }
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
	} // dynSection

	/** dynOrderStatus 
	 * Se muestra un combobox para que el usuario escoga el estado de la O/C
	 * */
	void dynOrderStatus() {
		orderCombo.removeActionListener(this);
		orderCombo.removeAllItems();

		String sql = " SELECT DISTINCT L.NAME, L.AD_REF_LIST_ID ID " +
					" FROM C_ORDER C INNER JOIN AD_REF_LIST L " +
					" ON (C.XX_ORDERSTATUS = L.VALUE)" +
					" WHERE L.AD_REFERENCE_ID = 1000103 " +
					" AND L.ISACTIVE='Y' " +
					" AND C.XX_ORDERSTATUS <> 'AN'";
		//System.out.println("SQL OrderStatus: "+sql);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			orderCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				orderCombo.addItem(new KeyNamePair(rs.getInt(2), rs
						.getString(1)));
			}
			orderCombo.addActionListener(this);
			orderCombo.setEnabled(true);
			orderCombo.setEditable(true);
		} 
		catch (SQLException e) { log.log(Level.SEVERE, sql, e); }
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // dynOrderStatus
	
	/** dynType 
	 * Se muestra un combobox para que el usuario escoga el tipo de elemento
	 * que desea crear en el folleto
	 * */
	void dynType() {
		typeCombo.removeActionListener(this);
		typeCombo.removeAllItems();

		String sql = " SELECT DISTINCT L.NAME, L.AD_REF_LIST_ID ID " +
					" FROM AD_REFERENCE R INNER JOIN AD_REF_LIST L " +
					" ON (L.AD_REFERENCE_ID = R.AD_REFERENCE_ID)" +
					" WHERE R.NAME = 'XX_VME_Type' " +
					" AND L.ISACTIVE='Y' " +
					" AND VALUE IN ('P', 'G')";
		//System.out.println("SQL OrderStatus: "+sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			typeCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				typeCombo.addItem(new KeyNamePair(rs.getInt(2), rs
						.getString(1)));
			}
			typeCombo.addActionListener(this);
			typeCombo.setEnabled(true);
			typeCombo.setEditable(true);
		} 
		catch (SQLException e) { log.log(Level.SEVERE, sql, e); }
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // dynType
	
	/** dynInit
	 * Se definen las columnas de las tablas a ser presentadas en la forma, 
	 * así como los botones y comboboxs
	 * */
	private void dynInit() {
		m_frame.getRootPane().setDefaultButton(bSearch);
		xTableHeader.getTableHeader().setReorderingAllowed(false);
		xTableDetail.getTableHeader().setReorderingAllowed(false);		
		xTableHeader.autoSize(true);
		brochure.setEnabled(true);

		/* Add the column definition for the table */
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo("",".",IDColumn.class, false, true, ""),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PO_Id"), ".",
						String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PO_Estado"),
						".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PO_Date"),
						".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PO_Amount"),
						".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Department_I"),
						".",String.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "VendorProductRef"), 
						".", String.class, "."),
				};
		
		ColumnInfo[] layoutDetail = new ColumnInfo[] {
				new ColumnInfo("",".",IDColumn.class, false, true, ""),
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Category"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Department_I"),".", 
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "VendorProductRef"), ".",
						KeyNamePair.class, "."),
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Price"), ".",
						BigDecimal.class, "."),
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Quantity"), ".",
						BigDecimal.class, "."),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Manual"),".", 
						Boolean.class, false, true, ""),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Mantain"),".", 
						Boolean.class, false, true, ""),
		};

		xTableHeader.prepareTable(layout, "", "", false, "");
		xTableDetail.prepareTable(layoutDetail, "", "", true, "");		
		xTableHeader.getTableHeader().setFocusable(false);
		xTableHeader.setSortEnabled(true);
		xMatchedToScrollPane.repaint();
		xMatchedToBorder.setTitle(Msg.getMsg(Env.getCtx(), "XX_PO_OrderDetail"));
		xTableDetail.setSortEnabled(true);
		xMatchedBorder.setTitle(Msg.translate(Env.getCtx(), "XX_PO_Order"));
		xMatchedScrollPane.repaint();
		
		// Visual
		CompiereColor.setBackground(this);

		// Agregar los Listeners necesarios
		bSearch.addActionListener(this);
		insert.addActionListener(this);
		C_Order.addActionListener(this);
		P_Order.addActionListener(this);
		xTableHeader.getSelectionModel().addListSelectionListener(this);
		
		// Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);
		
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

		// Agregar las acciones
		dynDepartament();
		dynLine();
		dynSection();
		disable_dept();
		dynOrderStatus();
		dynType();
		//dynGroup();
	} // dynInit

	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	} // dispose

	/** tableHeaderSearch
	 * Se arma el string de búsqueda de acuerdo a la selección del usuario
	 * (O/C o Pedido)
	 * @return sql SQL con la búsqueda
	 * */
	public String tableHeaderSearch() {
		//Date's From and To
		Timestamp dateTo = null;
		Timestamp dateFrom = null;
		String sql = "";
		String sql_pedido = "";
		String sql_orden = "";
		
		// Estado de la OC 
		KeyNamePair estado = (KeyNamePair) orderCombo.getSelectedItem();
		
		String SQLFechas = " SELECT (add_months(SYSDATE, -8)) as dateFrom, " +
						" SYSDATE as dateTo " +
						" FROM Dual ";
		
		PreparedStatement psSQLFechas = null;
		ResultSet rsSQLFechas = null;
		try {
			psSQLFechas = DB.prepareStatement(SQLFechas, null);
			rsSQLFechas = psSQLFechas.executeQuery();
			while (rsSQLFechas.next()) {
				dateTo = rsSQLFechas.getTimestamp("dateTo");
				dateFrom = rsSQLFechas.getTimestamp("dateFrom");
			}//While
		}// try 
		catch (SQLException e) { e.printStackTrace(); }
		finally{
			DB.closeResultSet(rsSQLFechas);
			DB.closeStatement(psSQLFechas);
		}
		
		// Definir el sql a devolver (O/C o Pedido)
		if (SearchType){
			sql_orden = " SELECT C.C_ORDER_ID, "+ 							//1
						" C.DOCUMENTNO, "+									//2
						" L.NAME,"+											//3
						" TO_CHAR(C.XX_ESTIMATEDDATE, 'DD/MM/YYYY'), "+ 	//4
						" SUM(OO.LINENETAMT)," +							//5
						" D.VALUE||'-'||D.NAME," +							//6
						" V.NAME " +										//7	
						" FROM C_ORDER C " +
						" INNER JOIN AD_REF_LIST L " +
						" ON (C.XX_ORDERSTATUS = L.VALUE)" +
						" INNER JOIN AD_REFERENCE R " +
						" ON (R.AD_REFERENCE_ID = L.AD_REFERENCE_ID)" +
						" INNER JOIN XX_VMR_DEPARTMENT D " +
						" ON (C.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID)" +
						" INNER JOIN C_BPARTNER V " +
						" ON (C.C_BPARTNER_ID = V.C_BPARTNER_ID)" ;

			// Query Orden de compra dependiendo de si se eligió un estado de la misma
			if(!estado.getName().equals("") && ((estado.getName()).equals("AP") || 
					(estado.getName()).equals("CH") || (estado.getName()).equals("RE"))){
					sql_orden += " INNER JOIN C_ORDERLINE OO " +
								" ON (C.C_ORDER_ID = OO.C_ORDER_ID) " +
								" INNER JOIN M_PRODUCT P " +
								" ON (OO.M_PRODUCT_ID = P.M_PRODUCT_ID)";
					sql_orden += MRole.getDefault().addAccessSQL(sql_orden, "C",
							MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
			} // usuario colocó estado de la OC
			else {
				sql_orden += " INNER JOIN XX_VMR_PO_LINEREFPROV OO " +
						" ON (C.C_ORDER_ID = OO.C_ORDER_ID) "+
						" INNER JOIN XX_VMR_VENDORPRODREF P " +
						" ON (OO.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID) " ;
			} // no tiene lineas

			// Permisologia para la consulta
			sql_orden = MRole.getDefault().addAccessSQL(sql_orden, "C",
					MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
			sql_orden += " AND TRUNC(C.CREATED) >= "+ DB.TO_DATE(dateFrom)
					+ " AND TRUNC(C.CREATED)  <= "+ DB.TO_DATE(dateTo)
					+ " AND R.AD_REFERENCE_ID = 1000103 ";
			sql = sql_orden;
		}
		else {
			// Query Pedido
			sql_pedido = " SELECT O.XX_VMR_ORDER_ID,  " + 					//1
						" O.XX_ORDERBECOCORRELATIVE, " +					//2
						" L.NAME,"+											//3 
						" TO_CHAR(C.XX_ESTIMATEDDATE, 'DD/MM/YYYY'), "+ 	//4
						" SUM(OO.LINENETAMT)," +							//5
						" D.VALUE||'-'||D.NAME," +							//6
						" V.NAME " +										//7	
						" FROM XX_VMR_ORDER O INNER JOIN C_ORDERLINE OO ON (O.C_ORDER_ID = OO.C_ORDER_ID)"+
						" INNER JOIN AD_REF_LIST L ON (O.XX_ORDERREQUESTSTATUS = L.VALUE)" +
						" INNER JOIN AD_REFERENCE R ON (R.AD_REFERENCE_ID = L.AD_REFERENCE_ID)" +
						" INNER JOIN C_ORDER C ON (C.C_ORDER_ID = O.C_ORDER_ID)" +
						" INNER JOIN M_PRODUCT P ON (OO.M_PRODUCT_ID = P.M_PRODUCT_ID)" +
						" INNER JOIN XX_VMR_DEPARTMENT D " +
						" ON (C.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID)" +
						" INNER JOIN C_BPARTNER V " +
						" ON (C.C_BPARTNER_ID = V.C_BPARTNER_ID)" ;
			sql_pedido = MRole.getDefault().addAccessSQL(sql_pedido, "O",MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
			sql_pedido += " AND TRUNC(O.CREATED) >= "+ DB.TO_DATE(dateFrom)
					+ " AND TRUNC(O.CREATED)  <= "+ DB.TO_DATE(dateTo);
			sql = sql_pedido;
		}
		
		sql += " AND C.ISSOTRX = 'N' "
			+ " AND C.XX_OrderStatus <> 'AN' ";
		
		// Se toman los parámetros de la OC o pedido elegidos por el usuario

		// El usuario desea OC asociadas al folleto
		if((Boolean)brochure.getValue()) {
			sql += " AND C.XX_VMA_BROCHURE_ID = " + brochurePage.getXX_VMA_Brochure_ID();
		}
		
		// Departamento definido por el usuario o el(los) de la página del folleto
		if (departmentCombo.getValue() != null) {
			KeyNamePair dep = (KeyNamePair) departmentCombo.getValue();
			if (dep.getKey() != -1) {
				sql += " AND C.XX_VMR_DEPARTMENT_ID = " + dep.getKey() + " ";
			}
			else {
				sql += " AND C.XX_VMR_DEPARTMENT_ID IN (" 
				+ " SELECT XX_VMR_DEPARTMENT_ID DEP " 
				+ " FROM XX_VMA_PAGEDEPT_V " 
				+ " WHERE XX_VMA_BROCHUREPAGE_ID = " + brochurePage.get_ID()
				+ " )";
			}
		}// department
		
		if (lineCombo.getValue() != null) {
			KeyNamePair lin = (KeyNamePair) lineCombo.getValue();
			if (lin.getKey() != -1){
				sql += " AND P.XX_VMR_LINE_ID = "+ lin.getKey() + " ";
			}
		}//line
		
		if (sectionCombo.getValue() != null) {
			KeyNamePair sec = (KeyNamePair) sectionCombo.getValue();
			if (sec.getKey() != -1){
				sql += " AND P.XX_VMR_SECTION_ID = " + sec.getKey() + " ";
			}
		}//section
		
		if(orderCombo.getValue() != null){
			KeyNamePair ord = (KeyNamePair) orderCombo.getValue();
			if (ord.getKey() != -1){
				sql += " AND L.NAME = '" + ord.getName() + "' ";
			}
		} // status
		
		if (orderNum.getValue() != null && !orderNum.getValue().equals("")) {
			sql += " AND C.DOCUMENTNO = " + orderNum.getValue();
		}
	
		if (lookupProduct.getValue() != null) {
			Integer product_id = (Integer) lookupProduct.getValue();
			sql += " AND OO.M_PRODUCT_ID = "+product_id;
		}
		
		if (SearchType){
			sql += " GROUP BY C.C_ORDER_ID, " 
				+ " C.DOCUMENTNO, "
				+ " L.NAME, "
				+ " TO_CHAR(C.XX_ESTIMATEDDATE, 'DD/MM/YYYY'),"
				+ " D.VALUE||'-'||D.NAME,"
				+ " V.NAME "
				+ " ORDER BY C.C_ORDER_ID ";
			System.out.println("Sql Total O/C es: "+sql);
		}
		else {
			sql += " GROUP BY O.XX_VMR_ORDER_ID,  "
				+ " O.XX_ORDERBECOCORRELATIVE, "
				+ " L.NAME," 
				+ " TO_CHAR(C.XX_ESTIMATEDDATE, 'DD/MM/YYYY'), "
				+ " D.VALUE||'-'||D.NAME,"
				+ " V.NAME "
				+ " ORDER BY O.XX_VMR_ORDER_ID ";
//			System.out.println("Sql Total Pedido es: "+sql);
		}
		
		return sql;
	
	}// tableHeaderSearch

	/** fillTableHeader
	 * De acuerdo al string que se haya pasado se llena la tabla cabecera 
	 * con los resultados
	 * @param sql SQL que se ejecuta para poder realizar la búsqueda y 
	 * llenar la cabecera 
	 * */
	void fillTableHeader(String sql) {
		// LLenar la tabla
		xTableHeader.setRowCount(0);
		xTableDetail.setRowCount(0);
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();			
			int row = 0;
			while (rs.next()) {
				xTableHeader.setRowCount(row + 1);
				IDColumn id = new IDColumn(rs.getInt(1));
				id.setSelected(false);
				xTableHeader.setValueAt(id, row, 0);
				if(SearchType){
					xTableHeader.setValueAt(rs.getString(2), row, 1);
				}
				else{
					xTableHeader.setValueAt(rs.getString(2), row, 1);
				}
				xTableHeader.setValueAt(rs.getString(3), row, 2);
				xTableHeader.setValueAt(rs.getString(4), row, 3);
				xTableHeader.setValueAt(rs.getInt(5), row, 4);
				xTableHeader.setValueAt(rs.getString(6), row, 5);
				xTableHeader.setValueAt(rs.getString(7), row, 6);
				row++;
			}
			
			xTableHeader.autoSize(true);
			xTableHeader.setMultiSelection(true);

		} // try 
		catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		xTableHeader.repaint();
	} // fillTableHeader

	/** clearFilter 
	 * Limpia el filtro
	 * */
	void clearFilter() {
		productCombo.setSelectedIndex(0);
		departmentCombo.setSelectedIndex(0);
		lineCombo.setSelectedIndex(0);
		sectionCombo.setSelectedIndex(0);
		orderCombo.setSelectedIndex(0);
		typeCombo.setSelectedIndex(0);
	} // clearFilter
	
	/** loadDetail
	 * Carga el detalle de acuerdo a la fila resultado seleccionada
	 * @param matchedRow Fila resultado a la que se desea ampliar el detalle
	 * */
	void loadDetail(int matchedRow) {
		xTableHeader.repaint();
		last_header = matchedRow;	
		checkManualAll.setEnabled(true);
		checkManualAll.setValue(false);
		checkMantainAll.setEnabled(true);
		checkMantainAll.setValue(false);
		
		String sql = "";
		if (matchedRow != -1) {
			IDColumn column_id = (IDColumn) xTableHeader.getValueAt(matchedRow,	0);
			cabecera = column_id.getRecord_ID();
			estado = (String)xTableHeader.getModel().getValueAt(matchedRow,2);
			if(!SearchType){ // Pedido
				pedidoid = cabecera;
				sql = " SELECT  "
					+ " P.XX_VMR_VENDORPRODREF_ID, "			//1
					+ " CA.XX_VMR_CATEGORY_ID, "				//2
					+ " CA.VALUE||'-'||CA.NAME, "				//3
					+ " DE.XX_VMR_DEPARTMENT_ID, "				//4
					+ " DE.VALUE||'-'||DE.NAME, "				//5
					+ " P.XX_VMR_VENDORPRODREF_ID, "			//6
					+ " VR.VALUE||'-'||VR.NAME, "				//7
					+ " DET.PRICEACTUAL, "						//8
					+ " DET.XX_PRODUCTQUANTITY, "				//9
					+ " 0, "									//10
					+ " 0 "										//11
					+ " FROM "
					+ " XX_VMR_ORDERREQUESTDETAIL DET "
					+ " INNER JOIN XX_VMR_CATEGORY CA ON (DET.XX_VMR_CATEGORY_ID = CA.XX_VMR_CATEGORY_ID) "
					+ " INNER JOIN XX_VMR_DEPARTMENT DE ON (DET.XX_VMR_DEPARTMENT_ID = DE.XX_VMR_DEPARTMENT_ID ) "
					+ " INNER JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = DET.M_PRODUCT_ID) " 
					+ " INNER JOIN XX_VMR_VENDORPRODREF VR ON (P.XX_VMR_VENDORPRODREF_ID = VR.XX_VMR_VENDORPRODREF_ID)"
					+ " WHERE DET.XX_VMR_ORDER_ID = " + column_id.getRecord_ID()
					+ " ORDER BY P.VALUE||'-'||P.NAME";
			}
			else { // Orden
				/*if(estado.equals("APROBADA") || estado.equals("CHEQUEADA")
						|| estado.equals("RECIBIDA")){
					sql = " SELECT P.XX_VMR_VENDORPRODREF_ID, "		//1
						+ " CA.XX_VMR_CATEGORY_ID, "				//2
						+ " CA.VALUE||'-'||CA.NAME, "				//3
						+ " DE.XX_VMR_DEPARTMENT_ID, "				//4
						+ " DE.VALUE||'-'||DE.NAME, "				//5
						+ " P.XX_VMR_VENDORPRODREF_ID, "			//6
						+ " VR.VALUE||'-'||VR.NAME, "				//7
						+ " DET.PRICEACTUAL, "						//8
						+ " DET.QTYORDERED, "						//9
						+ " 0, "									//10
						+ " 0 "										//11
						+ " FROM "
						+ " C_ORDER C "
						+ " INNER JOIN C_ORDERLINE DET ON (C.C_ORDER_ID = DET.C_ORDER_ID)"
						+ " INNER JOIN  XX_VMR_CATEGORY CA ON (C.XX_VMR_CATEGORY_ID = CA.XX_VMR_CATEGORY_ID) "
						+ " INNER JOIN  XX_VMR_DEPARTMENT DE ON (C.XX_VMR_DEPARTMENT_ID = DE.XX_VMR_DEPARTMENT_ID ) "
						+ " INNER JOIN  M_PRODUCT P ON (P.M_PRODUCT_ID = DET.M_PRODUCT_ID) "
						+ " INNER JOIN XX_VMR_VENDORPRODREF VR ON (P.XX_VMR_VENDORPRODREF_ID = VR.XX_VMR_VENDORPRODREF_ID)"
						+ " WHERE DET.C_ORDER_ID = " + column_id.getRecord_ID()
						+ " ORDER BY P.VALUE||'-'||P.NAME";
				} // tiene lineas
				else {*/
				corderid = cabecera;
					sql = " SELECT P.XX_VMR_VENDORPRODREF_ID,	"  	//1
						+ " CA.XX_VMR_CATEGORY_ID, "				//2
						+ " CA.VALUE||'-'||CA.NAME, "				//3
						+ " DE.XX_VMR_DEPARTMENT_ID, "				//4
						+ " DE.VALUE||'-'||DE.NAME, "				//5
						+ " P.XX_VMR_VENDORPRODREF_ID, "			//6
						+ " P.VALUE||'-'||P.NAME,	" 				//7
						+ " DET.PRICEACTUAL, "						//8
						+ " DET.QTY, "								//9
						+ " 0, "									//10
						+ " 0 "										//11
						+ " FROM "
						+ " C_ORDER C "
						+ " INNER JOIN XX_VMR_PO_LINEREFPROV DET ON " 
						+ " (C.C_ORDER_ID = DET.C_ORDER_ID)"
						+ " LEFT OUTER JOIN  XX_VMR_CATEGORY CA ON "
						+ " (C.XX_VMR_CATEGORY_ID = CA.XX_VMR_CATEGORY_ID) "
						+ " LEFT OUTER JOIN  XX_VMR_DEPARTMENT DE "
						+ " ON (C.XX_VMR_DEPARTMENT_ID = DE.XX_VMR_DEPARTMENT_ID ) "
						+ " LEFT OUTER JOIN  XX_VMR_VENDORPRODREF P "
						+ " ON (P.XX_VMR_VENDORPRODREF_ID = DET.XX_VMR_VENDORPRODREF_ID) "
						+ " INNER JOIN XX_VMR_VENDORPRODREF VR " 
						+ " ON (P.XX_VMR_VENDORPRODREF_ID = VR.XX_VMR_VENDORPRODREF_ID)"
						+ " WHERE DET.C_ORDER_ID = " + column_id.getRecord_ID()
						+ " ORDER BY P.VALUE||'-'||P.NAME";
				/*}// no tiene lineas*/
			} // Orden de compra
				
			//System.out.println("Sql detalle: "+sql);
			int row = 0;
			xTableDetail.setRowCount(0);
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = DB.prepareStatement(sql, null);
				rs = ps.executeQuery();
				
				while (rs.next()) {
					xTableDetail.setRowCount(row + 1);
					IDColumn id = new IDColumn(rs.getInt(1));
					id.setSelected(false);
					xTableDetail.setValueAt(id, row, 0);												// ID
					xTableDetail.setValueAt(new KeyNamePair(rs.getInt(2),rs.getString(3)), row, 1);		// CATEGORY							
					xTableDetail.setValueAt(new KeyNamePair(rs.getInt(4),rs.getString(5)), row, 2);		// DEPARTMENT NAME
					xTableDetail.setValueAt(new KeyNamePair(rs.getInt(6), rs.getString(7)), row, 3);	// VENDOR
					xTableDetail.setValueAt(rs.getBigDecimal(8), row, 4);								// PRICE
					xTableDetail.setValueAt(rs.getBigDecimal(9), row, 5);								// QUANTITY
					xTableDetail.setValueAt(rs.getBoolean(10), row, 6);									// MANUAL
					xTableDetail.setValueAt(rs.getBoolean(11), row, 7);									// MANTAIN
					row++;
				}
				xTableDetail.setMultiSelection(true);
			}//try 
			catch (SQLException e1) { 
				e1.printStackTrace();
				ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
		}//if matchedrow		
		
		xTableDetail.autoSize(true);
		xTableDetail.repaint();
		xMatchedToScrollPane.repaint();

	} // loadDetail
	
	/** getSelected
	 * Se encarga de crear un elemento en la página del folleto por cada producto
	 * que el usuario haya seleccionado previamente
	 */
	private void getSelected(){
		String msj = "";
		String msjQty = "";
		String referencesQty = "";
		String references = "";
		String SQLProds = "";
		boolean manual = false;
		boolean mantener = false;
		int lineSelected = 0;
		Integer refID = new Integer(0);
		Integer idSelected = new Integer(0);
		BigDecimal qtyOC = new BigDecimal(0);
		BigDecimal qtyOCProcessed = new BigDecimal(0);
		BigDecimal ocPercentage = new BigDecimal(100);
		X_XX_VMR_VendorProdRef reference = null;
		X_XX_VMR_Department dept = null;
		Vector<BigDecimal> qtyAvailable = new Vector<BigDecimal>();
		Vector<Integer> prods = new Vector<Integer>();
		X_XX_VME_Elements element = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// Se obtiene la acción de mercadeo asociada
		int actionID = XX_VME_GeneralFunctions.obtainAM(brochurePage.getXX_VMA_Brochure_ID());
		X_XX_VMA_MarketingActivity activity = new X_XX_VMA_MarketingActivity(Env.getCtx(),actionID, null);
		
		// Se inicia el porcentaje de acuerdo a lo indicado por el usuario
		if(!percentage.getText().equals("")){
			ocPercentage = new BigDecimal(percentage.getText());
		}
		
		// Se recorre la tabla verificando filas seleccionadas
		for(int j = 0; j < xTableDetail.getRowCount(); j++){
			IDColumn id = (IDColumn)xTableDetail.getModel().getValueAt(j, 0);

			// Fila seleccionada para tomar los datos
			if (id.isSelected()){
				lineSelected++;
				idSelected = id.getRecord_ID();
				manual = (Boolean)xTableDetail.getModel().getValueAt(j, 6);
				mantener = (Boolean)xTableDetail.getModel().getValueAt(j, 7);
				
				// Se obtiene la referencia a ser insertada
				reference = new X_XX_VMR_VendorProdRef(aux, idSelected, null);
				
				// Se obtiene el departamento de la referencia
				dept = new X_XX_VMR_Department(aux, reference.getXX_VMR_Department_ID(), null);
				
				// Se verifica si la referencia ya existe en el elemento
				refID = XX_VME_GeneralFunctions.obtainReference(reference.get_ID(), element);
				
				// Cantidad disponible a ser asignada en la inserción			
				qtyAvailable = XX_VME_GeneralFunctions.getQtyAvailable(element);
				
				// Se toma la cantidad si el check de manual está en verdadero
				if(manual){
					qtyOC = (BigDecimal)xTableDetail.getModel().getValueAt(j, 5);
				}
				
				// Se toma el porcentaje definido por el usuario
				if(manual && ocPercentage.compareTo(new BigDecimal(0)) > 0){
					qtyOCProcessed = (qtyOC.multiply(ocPercentage)).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
				}
				
				// La referencia no esta asociada al elemento
				if(refID == 0) {
					// No se sobrepasa de la cantidad a ser asignada
					if(qtyAvailable.get(0).compareTo(qtyOCProcessed) >= 0 ){
						/*XX_VME_GeneralFunctions.createElemRef(element, activity, 
								qtyOC, reference.get_ID(), dept.getXX_VMR_Category_ID(), 
								"", manual, mantener, 0, false);*/
						
						 XX_VME_GeneralFunctions.createElemRef(element, activity, 
								 qtyOCProcessed, reference.get_ID(), dept.getXX_VMR_Category_ID(), 
								"", manual, mantener, corderid, pedidoid);
	
						// Obtener el id de la referencia creada
						int refNew = XX_VME_GeneralFunctions.
									obtainReference(reference.get_ID(), element);
						
						// Se coloca el valor de manual como corresponde
						X_XX_VME_Reference ref = new X_XX_VME_Reference(Env.getCtx(), refNew, null);
						if(manual){
							ref.setXX_VME_Manual(true);
							ref.save();
						}
						else {
							ref.setXX_VME_Manual(false);
							ref.save();
						}
						
	
					}
					// Se sobrepasa de la cantidad a ser asignada
					else {
						referencesQty += reference.getName()+" ";	
					}
				} // no existe la referencia
				// Si existe la referencia
				else {
					if(!references.contains(reference.getName()))
						references += reference.getName() + " ";
				} 
			} // if ID Selected
		} // for
		
		// Configurar la relacion de marca con elemento
		XX_VME_GeneralFunctions.setElemBrand(elementID);
		
		// Redistribuir cantidades
		XX_VME_GeneralFunctions.redefineQuantities(element);
		
		m_frame.setCursor(Cursor.getDefaultCursor());
		// Si existían referencias a ser agregados que sobrepasan la cantidad disponible
		if(referencesQty.length() > 0){
			msjQty = Msg.getMsg(Env.getCtx(), "XX_QtyAvailable", new String [] {
				referencesQty }); 
			// The products {0} exceeded the qty available for the element and weren't added
			// Los productos {0} excedieron la cantidad disponible para el elemento y no fueron agregados
			ADialog.info(m_WindowNo, m_frame, msjQty); 
		}
		
		// Si existian referencias a ser agragadas en elemento
		if(references.length() > 0){
			msj = Msg.getMsg(Env.getCtx(), "XX_RefExists", new String [] {
				references });
			ADialog.info(m_WindowNo, m_frame, msj);
		}
		
		// Se debe seleccionar una linea para ser insertado
		if(lineSelected == 0) {
			ADialog.error(m_WindowNo, m_frame, "Debe seleccionar al menos una referencia para insertar"); 
		} 
		else {
			element.setXX_VME_Validated(false);
			element.save();
			// Ventana de salida
			ADialog.info(m_WindowNo, m_frame, "Proceso completado");
			dispose();
		}
		
	} // getSelected
	
	/**
	 * Action Listener
	 * @param e event
	 */
	public void actionPerformed(ActionEvent e) {
		
		// Buscar
		if (e.getSource() == bSearch) {
			fillTableHeader(tableHeaderSearch());
		} 
		// Borrar
		else if (e.getSource() == insert) {			
			getSelected();
		} 
		// Departamento
		else if (e.getSource() == departmentCombo) {
			dynLine();
		} 
		// Linea
		else if (e.getSource() == lineCombo) {
			dynSection();
		} 
		
		// Orden 
		else if (e.getSource() == C_Order){
			if(C_Order.isSelected()){
				SearchType = true;
				dynOrderStatus();
			}
		}	
		// Pedido
		else if (e.getSource() == P_Order ){
			if(P_Order.isSelected()){
				SearchType = false;
			}
		}
		
		// Seleccionar todos manual
		else if (e.getSource() == checkManualAll ){
			manualAll((Boolean)checkManualAll.getValue());
		}
		
		// Seleccionar todos 
		else if (e.getSource() == checkSelectAll ){
			selectAll((Boolean)checkSelectAll.getValue());
		}
		
		// Seleccionar todos mantener
		else if (e.getSource() == checkMantainAll ){
			mantenerAll((Boolean)checkMantainAll.getValue());
		}
	
	} // actionPerformed
	
	/** manualAll
	 * Selecciona/Deselecciona todos los elementos de la tabla segúna opción
	 * del usuario
	 * @param selected Opción del usuario
	 * */
	private void manualAll(Boolean selected) {
		for(int j = 0; j < xTableDetail.getRowCount(); j++){
			xTableDetail.getModel().setValueAt(selected, j, 6);	
		}
		xTableDetail.repaint();
	} // selectAll	
	
	/** mantainAll
	 * Selecciona/Deselecciona todos los elementos de la tabla segúna opción
	 * del usuario
	 * @param selected Opción del usuario
	 * */
	private void mantenerAll(Boolean selected) {
		for(int j = 0; j < xTableDetail.getRowCount(); j++){
			xTableDetail.getModel().setValueAt(selected, j, 7);	
		}
		xTableDetail.repaint();
	} // selectAll	
	
	/** selectAll
	 * Selecciona/Deselecciona todos los elementos de la tabla segúna opción
	 * del usuario
	 * @param selected Opción del usuario
	 * */
	private void selectAll(Boolean selected) {
		for(int j = 0; j < xTableDetail.getRowCount(); j++){
			IDColumn id = (IDColumn)xTableDetail.getModel().getValueAt(j, 0);
			id.setSelected(selected);
		}
		xTableDetail.repaint();
	} // selectAll
	

	/**
	 * List Selection Listener - get Info and fill xMatchedTo
	 * @param e event
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		int matchedRow = xTableHeader.getSelectedRow();		
		loadDetail(matchedRow);
	} // valueChanged

	@Override
	public void tableChanged(TableModelEvent e) {
		/*int row = e.getFirstRow();
        int column = e.getColumn();             
        TableModel model = (TableModel)e.getSource();  
                
        if ((row == -1) || (column == -1)) return;        
        Object obj = model.getValueAt(row, column);
    	try {     		
    		if (column == 4) {
	        	if (obj != null) {
	        		KeyNamePair type = (KeyNamePair)obj;
	        	}
    		} 
    	}  
    	catch (NullPointerException nul) {
    		
    	} 
		*/
	} // tableChanged
		    	
}// Fin XX_VME_ConsulPO_Form
