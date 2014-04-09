package compiere.model.dynamic.forms;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
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

import compiere.model.cds.MOrder;
import compiere.model.cds.X_Ref_XX_OrderStatus;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_VendorProdRef;
import compiere.model.dynamic.MVMABrochure;
import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_Ref_XX_VME_Type;
import compiere.model.dynamic.X_XX_VMA_MarketingActivity;
import compiere.model.dynamic.X_XX_VME_Elements;

/** XX_VME_ImportFromPO_Form (Funcion 1XX)
 * Forma que se encarga de improtar productos desde una orden de compra 
 * hacia un folleto especificado por el usuario.
 * @author María Vintimilla
 * @version 1.0
 * */

public class XX_VME_ImportFromPO_Form extends CPanel implements FormPanel,
		ActionListener, ListSelectionListener, TableModelListener {
	/** Window No */
	private int m_WindowNo = 0;
	/** FormFrame */
	private FormFrame m_frame;
	
	/** Logger */
	static CLogger log = CLogger.getCLogger(XX_VME_ImportFromPO_Form.class);

	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();
	
	private static final long serialVersionUID = 1L;

	// Panel
	private CPanel northPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private CPanel mainPanel = new CPanel();
	
	//StatusBar
	private StatusBar statusBar = new StatusBar();
	
	//BorderLayout	
	private BorderLayout mainLayout = new BorderLayout();
	
	//GridBagLayout
	private GridBagLayout northLayout = new GridBagLayout();
	private GridBagLayout southLayout = new GridBagLayout();
	
	// Botones
	private CButton bSearch = new CButton();
	private CButton bInsert = new CButton();
	
	// CTextFields
	private CLabel brochureLabel = new CLabel();
	private CTextField brochureText = new CTextField();
	private CLabel pageLabel = new CLabel();
	private CComboBox pageCombo = new CComboBox();
	private CLabel elementNameLabel = new CLabel();
	private CTextField elementNameText = new CTextField();
	private CLabel elementCharactLabel = new CLabel();
	private CTextField elementCharactText = new CTextField();
	private CLabel elementPriceLabel = new CLabel();
	private CTextField elementPriceText = new CTextField();
	private CLabel elementPromLabel = new CLabel();
	private CTextField elementPromText = new CTextField();
	private CLabel percentageLabe = new CLabel();
	private CTextField percentageText = new CTextField();
	private CPanel southPanel = new CPanel();
	private CLabel marketLabel = new CLabel();
	private CTextField marketText = new CTextField();
	private CLabel qtyDinLabel = new CLabel();
	private CTextField qtyDinText = new CTextField();
	private CTextField qtyEstimText = new CTextField();
	private CLabel qtyEstimLabel = new CLabel();
	private CLabel outstandingLabel = new CLabel();
	
	//Radio Buttons
	private JRadioButton outstandingRadio = 
		new JRadioButton("",false);
	private CLabel basicLabel = new CLabel();
	private JRadioButton basicRadio = 
		new JRadioButton("",false);
	private CLabel starLabel = new CLabel();
	private JRadioButton starRadio = 
		new JRadioButton("",false);
	private CLabel oportunityLabel = new CLabel();
	private JRadioButton oportunityRadio = 
		new JRadioButton("",false);
	private JRadioButton tendenceRadio = 
		new JRadioButton("",false);
	private CLabel tendenceLabel = new CLabel();
	private BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
	private JScrollPane xMatchedScrollPane = new JScrollPane();
	private TitledBorder xMatchedBorder = new TitledBorder("xMatched");
	private MiniTable xTableHeader = new MiniTable();

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
	Ctx aux = Env.getCtx();	
	private Integer elementID = new Integer(0);
	private Integer corderid = aux.getContextAsInt("#XX_COrder_ID");
	private MVMABrochure brochure = null;
	private Integer brochurePage = new Integer(0);

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
			aux = Env.getCtx();	
			brochure = new MVMABrochure(aux, aux.getContextAsInt("#Brochure_ID"), null);
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
		
		bSearch.addActionListener(this);
		bInsert.addActionListener(this);
		starRadio.addActionListener(this);
		oportunityRadio.addActionListener(this);
		basicRadio.addActionListener(this);
		outstandingRadio.addActionListener(this);
		tendenceRadio.addActionListener(this);
		checkManualAll.addActionListener(this);
		checkSelectAll.addActionListener(this);
		checkMantainAll.addActionListener(this);
		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bInsert.setText(Msg.translate(Env.getCtx(), "XX_PO_Insert"));
		pageLabel.setText(Msg.translate(Env.getCtx(), "XX_pageLabel"));
		elementNameLabel.setText(Msg.translate(Env.getCtx(), "XX_elementNameLabel"));
		elementCharactLabel.setText(Msg.translate(Env.getCtx(), "XX_elementCharactLabel"));
		elementPriceLabel.setText(Msg.translate(Env.getCtx(), "XX_elementPriceLabel"));
		elementPromLabel.setText(Msg.translate(Env.getCtx(), "XX_elementPromLabel"));
		qtyEstimLabel.setText(Msg.translate(Env.getCtx(), "XX_qtyEstimLabel"));
		outstandingLabel.setText(Msg.translate(Env.getCtx(), "XX_outstandingLabel"));
		basicLabel.setText(Msg.translate(Env.getCtx(), "XX_basicLabel"));
		starLabel.setText(Msg.translate(Env.getCtx(), "XX_starLabel"));
		oportunityLabel.setText(Msg.translate(Env.getCtx(), "XX_oportunityLabel"));
		tendenceLabel.setText(Msg.translate(Env.getCtx(), "XX_tendenceLabel"));
		percentageLabe.setText(Msg.translate(Env.getCtx(), "XX_percentageLabe"));
		marketLabel.setText(Msg.translate(Env.getCtx(), "XX_MarketLabel"));
		qtyDinLabel.setText(Msg.translate(Env.getCtx(), "XX_QtyDinLabel"));
		labelMantainAll.setText(Msg.translate(Env.getCtx(), "XX_Mantain"));
		brochureLabel.setText(Msg.translate(Env.getCtx(), "XX_Brochure"));
		
		xMatchedScrollPane.setBorder(xMatchedBorder);
		xMatchedScrollPane.setMinimumSize(new Dimension(1280, 300));
		mainPanel.setMinimumSize(new Dimension(1280, 600));

		xMatchedScrollPane.setPreferredSize(new Dimension(1280, 300));
		
		// Folleto
		northPanel.add(brochureLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,0.0, 
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(brochureText, new GridBagConstraints(1, 0, 1, 1, 0.0,0.0, 
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5), 0, 0));
		
		// Pagina
		northPanel.add(pageLabel, new GridBagConstraints(4, 0, 1, 1, 0.0,0.0, 
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(pageCombo, new GridBagConstraints(5, 0, 1, 1, 0.0,0.0, 
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5), 0, 0));
		
		// Search
		northPanel.add(bSearch, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,	new Insets(5, 5, 5, 5), 0, 0));
		
		// Nombre
		northPanel.add(elementNameLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(elementNameText, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
//		
//		// Caracteristica
		northPanel.add(elementCharactLabel, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(elementCharactText, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
//		
//		// Precio dinamica
		northPanel.add(elementPriceLabel, new GridBagConstraints(0, 3, 1, 1,0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(elementPriceText, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
//		// Precio PRomocional 
		northPanel.add(elementPromLabel, new GridBagConstraints(4, 3, 1, 1,0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(elementPromText, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Porcentaje
		northPanel.add(percentageLabe, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(percentageText, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Precio mercado
		northPanel.add(marketLabel, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(marketText, new GridBagConstraints(5, 4, 3, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Catn Dyn Pub
		northPanel.add(qtyDinLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(qtyDinText, new GridBagConstraints(1, 5, 3, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Catn estim venta
		northPanel.add(qtyEstimLabel, new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(qtyEstimText, new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// destacado
		northPanel.add(outstandingLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(outstandingRadio, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// basico
		northPanel.add(basicLabel, new GridBagConstraints(4, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(basicRadio, new GridBagConstraints(5, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// estrella
		northPanel.add(starLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(starRadio, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// oportunidad
		northPanel.add(oportunityLabel, new GridBagConstraints(4, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(oportunityRadio, new GridBagConstraints(5, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// tendencia
		northPanel.add(tendenceLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		northPanel.add(tendenceRadio, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		
		// Seleccionar todos
		northPanel.add(labelSelectAll, new GridBagConstraints(4, 8, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(checkSelectAll, new GridBagConstraints(5, 8, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
//		// Manual todos
		northPanel.add(labelManualAll, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(checkManualAll, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
//		// Mantener todos
		northPanel.add(labelMantainAll, new GridBagConstraints(4, 9, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(checkMantainAll, new GridBagConstraints(5, 9, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// Insert
		northPanel.add(bInsert, new GridBagConstraints(8, 9, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		
		xMatchedScrollPane.getViewport().add(xTableHeader);
		
		centerPanel.add(xMatchedScrollPane, BorderLayout.NORTH);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		m_frame.pack();
		m_frame.setExtendedState(m_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH); 
	} // jbInit
	
	/** dynPage
	 * Se muestra un combobox para que el usuario escoga departamento
	 * */
	void dynPage() {
		pageCombo.removeAllItems();
		pageCombo.setEnabled(true);
		
		KeyNamePair page = (KeyNamePair)pageCombo.getSelectedItem();
		KeyNamePair loadKNP;
		loadKNP = new KeyNamePair(0,"");
		pageCombo.addItem(loadKNP);

		String sql = " SELECT XX_VMA_BROCHUREPAGE_ID, NAME "
				+ " FROM XX_VMA_BROCHUREPAGE "
				+ " WHERE XX_VMA_BROCHURE_ID  = " + brochure.get_ID()
				+ " ORDER BY VALUE||'-'||NAME ";	
		//System.out.println("SQL Dept: "+sql);
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				page = new KeyNamePair(rs.getInt(1), rs.getString(2));
				pageCombo.addItem(page);
				
			}
//			pageCombo.addActionListener(this);
//			pageCombo.setEnabled(true);
//			pageCombo.setEditable(true);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // dynPage
	
	/** dynInit
	 * Se definen las columnas de las tablas a ser presentadas en la forma, 
	 * así como los botones y comboboxs
	 * */
	private void dynInit() {
		m_frame.getRootPane().setDefaultButton(bSearch);
		xTableHeader.getTableHeader().setReorderingAllowed(false);
		xTableHeader.autoSize(true);

		// Se muestra el folleto asociado a la OC
		brochureText.setEditable(false);
		brochureText.setText(brochure.getName());
		
		// No se permite ingresar datos hasta tener referencias asociadas
		elementNameText.setEditable(false);
		elementCharactText.setEditable(false);
		elementPriceText.setEditable(false);
		elementPromText.setEditable(false);
		percentageText.setEditable(false);
		qtyDinText.setEditable(false);
	
		pageCombo.setEnabled(true);
		pageCombo.addActionListener(this);
		
		// Estabecer campos obligatorios
		brochureText.setBackground(Color.LIGHT_GRAY);
		brochureText.setPreferredSize(new Dimension(250, 25));
		brochureText.setHorizontalAlignment(JTextField.CENTER);
		elementNameText.setBackground(Color.PINK);
		elementNameText.setPreferredSize(new Dimension(80, 25));
		elementNameText.setHorizontalAlignment(JTextField.LEFT);
		elementCharactText.setBackground(Color.PINK);
		elementCharactText.setPreferredSize(new Dimension(300, 25));
		elementCharactText.setHorizontalAlignment(JTextField.LEFT);
		elementPriceText.setBackground(Color.PINK);
		elementPriceText.setPreferredSize(new Dimension(80, 25));
		elementPriceText.setHorizontalAlignment(JTextField.CENTER);
		elementPromText.setBackground(Color.PINK);
		elementPromText.setPreferredSize(new Dimension(80, 25));
		elementPromText.setHorizontalAlignment(JTextField.CENTER);
		percentageText.setBackground(Color.PINK);
		percentageText.setPreferredSize(new Dimension(80, 25));
		percentageText.setHorizontalAlignment(JTextField.CENTER);
		qtyDinText.setBackground(Color.PINK);
		qtyDinText.setPreferredSize(new Dimension(80, 25));
		qtyDinText.setHorizontalAlignment(JTextField.CENTER);

		ColumnInfo[] layout = new ColumnInfo[] {
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

		xTableHeader.prepareTable(layout, "", "", true, "");
		xTableHeader.getTableHeader().setFocusable(false);
		xTableHeader.setSortEnabled(true);
		xMatchedBorder.setTitle(Msg.translate(Env.getCtx(), "XX_PO_Order"));
		xMatchedScrollPane.repaint();
		
		// Visual
		CompiereColor.setBackground(this);

		// Agregar los Listeners necesarios
		bSearch.addActionListener(this);
		bInsert.addActionListener(this);
		pageCombo.addActionListener(this);
		xTableHeader.getSelectionModel().addListSelectionListener(this);
		
		// Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);
		
		xTableHeader.getModel().addTableModelListener(this);
		xTableHeader.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				int column = xTableHeader.getSelectedColumn();
				int row = xTableHeader.getSelectedRow();
				xTableHeader.editCellAt(row, column);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {		
			}
			
		});

		// Agregar las acciones
		dynPage();
		//dynGroup();
	} // dynInit

	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	} // dispose

	/** tableHeaderSearch
	 * Se arma el string de búsqueda de acuerdo a la selección del usuario
	 * @return sql SQL con la búsqueda
	 * */
	public String tableHeaderSearch() {
		String sql = "";
		String sql_order = "";
		
		// Estado de la OC 
		KeyNamePair page = (KeyNamePair) pageCombo.getSelectedItem();
		brochurePage = page.getKey();
				
		// Definir el sql a devolver (O/C o Pedido)
		sql_order = " SELECT P.XX_VMR_VENDORPRODREF_ID,	"  	//1
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
			+ " ON (P.XX_VMR_VENDORPRODREF_ID = VR.XX_VMR_VENDORPRODREF_ID)";
		sql += MRole.getDefault().addAccessSQL(sql_order, "C",MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);


		// Departamento definido por la página del folleto
		sql += " AND C.C_ORDER_ID = " + corderid 
			+ " AND C.XX_VMR_DEPARTMENT_ID IN ("  
			+ " SELECT XX_VMR_DEPARTMENT_ID DEP " 
			+ " FROM XX_VMA_PAGEDEPT_V " 
			+ " WHERE XX_VMA_BROCHUREPAGE_ID = " + brochurePage
			+ " )"
			+ " ORDER BY P.VALUE||'-'||P.NAME";;
		
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
				xTableHeader.setValueAt(id, row, 0);												// ID
				xTableHeader.setValueAt(new KeyNamePair(rs.getInt(2),rs.getString(3)), row, 1);		// CATEGORY							
				xTableHeader.setValueAt(new KeyNamePair(rs.getInt(4),rs.getString(5)), row, 2);		// DEPARTMENT NAME
				xTableHeader.setValueAt(new KeyNamePair(rs.getInt(6), rs.getString(7)), row, 3);	// VENDOR
				xTableHeader.setValueAt(rs.getBigDecimal(8), row, 4);								// PRICE
				xTableHeader.setValueAt(rs.getBigDecimal(9), row, 5);								// QUANTITY
				xTableHeader.setValueAt(rs.getBoolean(10), row, 6);									// MANUAL
				xTableHeader.setValueAt(rs.getBoolean(11), row, 7);									// MANTAIN
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
		
		// Se permite ingresar los datos del elemento ssi hay resultados en la busqueda
		if(xTableHeader.getModel().getRowCount() > 0){
			elementNameText.setEditable(true);
			elementCharactText.setEditable(true);
			elementPriceText.setEditable(true);
			elementPromText.setEditable(true);
			percentageText.setEditable(true);
			qtyDinText.setEditable(true);
		}
	} // fillTableHeader

	/** clearFilter 
	 * Limpia el filtro
	 * */
	void clearFilter() {
		pageCombo.setSelectedIndex(0);
		checkManualAll.setEnabled(true);
		checkManualAll.setValue(false);
		checkMantainAll.setEnabled(true);
		checkMantainAll.setValue(false);
		outstandingRadio.setEnabled(true);
		basicRadio.setEnabled(true);
		starRadio.setEnabled(true);
		oportunityRadio.setEnabled(true);
		tendenceRadio.setEnabled(true);
	} // clearFilter
	
	/** getSelected
	 * Se encarga de crear un elemento en la página del folleto por cada producto
	 * que el usuario haya seleccionado previamente
	 */
	private void getSelected(){
		String msj = "";
		String msjerror = " Los siguientes campos son obligatorios: Nombre, caracteristica a publicar, precio dinamica, precio promocional, descuento, cantidad dinamica a publicar";
		String msjQty = "";
		String referencesQty = "";
		String references = "";
		boolean manual = false;
		boolean mantener = false;
		int lineSelected = 0;
		Integer refID = new Integer(0);
		Integer idSelected = new Integer(0);
		BigDecimal qtyOC = new BigDecimal(0);
		BigDecimal qtyOCProcessed = new BigDecimal(0);
		BigDecimal ocPercentage = new BigDecimal(0);
		X_XX_VMR_VendorProdRef reference = null;
		X_XX_VMR_Department dept = null;
		Vector<BigDecimal> qtyAvailable = new Vector<BigDecimal>();
		Vector<BigDecimal> cantidades = new Vector<BigDecimal>();
		Vector<Integer> referencias = new Vector<Integer>();
		Vector<Integer> productos = new Vector<Integer>();
		Vector<Boolean> manualV = new Vector<Boolean>();
		Vector<Boolean> mantenerV = new Vector<Boolean>();
		Vector<Boolean> manualProd = new Vector<Boolean>();
		Vector<Integer> order = new Vector<Integer>();
		Vector<Integer> pedido = new Vector<Integer>();
		X_XX_VME_Elements element = null;
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		// Se obtiene la acción de mercadeo asociada
		int actionID = XX_VME_GeneralFunctions.obtainAM(brochure.getXX_VMA_Brochure_ID());
		X_XX_VMA_MarketingActivity activity = new X_XX_VMA_MarketingActivity(Env.getCtx(),actionID, null);
		
		// Se inicia el porcentaje de acuerdo a lo indicado por el usuario
		if(!percentageText.getText().equals("")){
			ocPercentage = new BigDecimal(percentageText.getText());
		}
		else {
			ocPercentage = new BigDecimal(100);
		}
		
		// Se crea el elemento con los datos proporcionados por el usuario
		element = new X_XX_VME_Elements(Env.getCtx(), 0, null);
		// Se valida informacion obligatoria
		if(elementNameText.getText().equals("") || elementCharactText.getText().equals("") ||
			(elementPriceText.getText().equals("") && elementPromText.getText().equals("") &&
			percentageText.getText().equals("")) || qtyDinText.getText().equals("")){
			ADialog.error(m_WindowNo, m_frame, msjerror);
			return;
		}
		else{
			element.setName(elementNameText.getText());
			element.setDescription(elementNameText.getText());
			element.setXX_VME_CharactPublished(elementCharactText.getText());
			element.setXX_VMA_BrochurePage_ID(brochurePage);
			element.setXX_VME_Type("P");
			if(!percentageText.getText().equals(""))
				element.setXX_VME_DiscountPercentage(new BigDecimal(percentageText.getText()));
			if(!elementPriceText.getText().equals(""))
				element.setXX_VME_DynamicPrice(new BigDecimal(elementPriceText.getText()));
			if(!elementPromText.getText().equals(""))
				element.setXX_VME_PromoDynPrice(new BigDecimal(elementPromText.getText()));
			if(!qtyDinText.getText().equals(""))
				element.setXX_VME_QTYPUBLISHED(new BigDecimal(qtyDinText.getText()));
			if(!qtyEstimText.getText().equals(""))
				element.setXX_VME_EstimatedQty(new BigDecimal(qtyEstimText.getText()));
			if(!marketText.getText().equals(""))
				element.setXX_VME_MarketPrice(new BigDecimal(marketText.getText()));
			element.setXX_VME_IsBasic(basicRadio.isSelected());
			element.setXX_VME_IsOportunity(oportunityRadio.isSelected());
			element.setXX_VME_IsStar(starRadio.isSelected());
			element.setXX_VME_IsTendence(tendenceRadio.isSelected());
			element.setXX_VME_Outstanding(outstandingRadio.isSelected());
			element.save();	
		}
		
		// Se recorre la tabla verificando filas seleccionadas
		for(int j = 0; j < xTableHeader.getRowCount(); j++){
			IDColumn id = (IDColumn)xTableHeader.getModel().getValueAt(j, 0);

			// Fila seleccionada para tomar los datos
			if (id.isSelected()){
				lineSelected++;
				idSelected = id.getRecord_ID();
				manual = (Boolean)xTableHeader.getModel().getValueAt(j, 6);
				mantener = (Boolean)xTableHeader.getModel().getValueAt(j, 7);
				
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
					qtyOC = (BigDecimal)xTableHeader.getModel().getValueAt(j, 5);
				}
				
				// Se toma el porcentaje definido por el usuario
				if(manual && ocPercentage.compareTo(new BigDecimal(0)) > 0){
					qtyOCProcessed = (qtyOC.multiply(ocPercentage)).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
				}
				else if(manual && ocPercentage.compareTo(new BigDecimal(0)) == 100) {
					qtyOCProcessed = qtyOC;
				}

				
				// La referencia no esta asociada al elemento
				if(refID == 0) {
					
					// No se sobrepasa de la cantidad a ser asignada
					if(qtyAvailable.get(0).compareTo(qtyOCProcessed) >= 0 ){
						referencias.add(reference.get_ID());
						cantidades.add(qtyOCProcessed);
						manualV.add(manual);
						mantenerV.add(mantener);
						manualProd.add(false);
						// Si la orden es chequeada o recibida se deben agregar los productos asociados a la referencia
						MOrder Corder = new MOrder(aux, corderid, null);
						if(Corder.getXX_OrderStatus().equals(X_Ref_XX_OrderStatus.APROBADA) ||
								Corder.getXX_OrderStatus().equals(X_Ref_XX_OrderStatus.CHEQUEADA) ||
								Corder.getXX_OrderStatus().equals(X_Ref_XX_OrderStatus.RECIBIDA)){
							String POProducts = "SELECT M_PRODUCT_ID ID" +
									" FROM C_ORDERLINE" +
									" WHERE C_ORDER_ID = " + corderid;
							
							PreparedStatement ps = null;
							ResultSet rs = null;
							
							try{
								ps = DB.prepareStatement(POProducts, null);
								rs = ps.executeQuery();
								while(rs.next()){
									productos.add(rs.getInt("ID"));
								}
							} //try
							catch(Exception e){ e.printStackTrace(); }
							finally {
								DB.closeResultSet(rs);
								DB.closeStatement(ps);
							}// finally
							
						} // RE, CH, AP
						else{
							productos.add(0);
						}
						order.add(corderid);
						pedido.add(0);
					}
					// Se sobrepasa de la cantidad a ser asignada
					else {
						referencesQty += reference.getName()+" ";	
					}
				} // no existe la referencia
//				 Si existe la referencia
				else {
					if(!references.contains(reference.getName()))
						references += reference.getName() + " ";
				} 
			} // if ID Selected
		} // for
		
		// Se crean las referencias asociadas al elemento
		XX_VME_GeneralFunctions.createElemRefNew(element, activity, cantidades,	
				referencias,"R", manualV, mantenerV, productos, manualProd, order, pedido);
		
		// Se asociab las referencias al elemento
//		String Elemenefs = " UPDATE XX_VME_ELEMENTS SET  XX_VME_QtyRefAssociated = " + referencias.size() +
//						" WHERE XX_VME_ELEMENTS_ID = " + element.get_ID();
//		System.out.println(Elemenefs);
		// Configurar la relacion de marca con elemento
		XX_VME_GeneralFunctions.setElemBrand(element.get_ID());
		
		element.setXX_VME_QtyRefAssociated(new BigDecimal(referencias.size())); 
//		try {
//			DB.executeUpdateEx(Elemenefs, null);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
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
//		Date fin = new Date();
//		System.out.println("inicio: "+inicio+" fin: "+fin);
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
		pageCombo.removeActionListener(this);
		
		// Buscar
		if (e.getSource() == bSearch) {
			fillTableHeader(tableHeaderSearch());
		} 
		// Borrar
		else if (e.getSource() == bInsert) {			
			getSelected();
		} 
		// Departamento
		else if (e.getSource() == pageCombo) {
			dynPage();
//			pageCombo.addActionListener(this);
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
		for(int j = 0; j < xTableHeader.getRowCount(); j++){
			xTableHeader.getModel().setValueAt(selected, j, 6);	
		}
		xTableHeader.repaint();
	} // selectAll	
	
	/** mantainAll
	 * Selecciona/Deselecciona todos los elementos de la tabla segúna opción
	 * del usuario
	 * @param selected Opción del usuario
	 * */
	private void mantenerAll(Boolean selected) {
		for(int j = 0; j < xTableHeader.getRowCount(); j++){
			xTableHeader.getModel().setValueAt(selected, j, 7);	
		}
		xTableHeader.repaint();
	} // selectAll	
	
	/** selectAll
	 * Selecciona/Deselecciona todos los elementos de la tabla segúna opción
	 * del usuario
	 * @param selected Opción del usuario
	 * */
	private void selectAll(Boolean selected) {
		for(int j = 0; j < xTableHeader.getRowCount(); j++){
			IDColumn id = (IDColumn)xTableHeader.getModel().getValueAt(j, 0);
			id.setSelected(selected);
		}
		xTableHeader.repaint();
	} // selectAll
	

	/**
	 * List Selection Listener - get Info and fill xMatchedTo
	 * @param e event
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		int matchedRow = xTableHeader.getSelectedRow();		
//		loadDetail(matchedRow);
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
