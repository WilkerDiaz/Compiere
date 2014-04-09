package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.dynamic.XX_VME_ProductFilterResults;

/** XX_ProductFilter
 * Forma que permite al usuario filtrar productos de acuerdo a ciertos campos
 * disponibles. 
 * @author Maria Vintimilla
 * @author Wilker Diaz
 * @version 1.0
 * */
public abstract class XX_ProductFilter extends CPanel implements FormPanel, 
	ActionListener, TableModelListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	/** Inicializar la forma
	 *  @param WindowNo window
	 *  @param frame frame
	 */ 
	public final void init (int WindowNo, FormFrame frame){
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try	{
			lookupProduct = VLookup.createProduct(m_WindowNo);
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		}
		catch(Exception e)	{
			log.log(Level.SEVERE, "", e);
		}
	}	// Fin init
	
	protected Ctx ctx = Env.getCtx();
	/**	Window No */
	private int m_WindowNo = 0;
	/**	FormFrame */
	protected FormFrame m_frame;
	/** Format */
	protected DecimalFormat m_format = DisplayType.getNumberFormat(DisplayTypeConstants.Amount);
	/** SQL for Query */
	protected StringBuilder m_sql;
	/** Number of selected rows */
	protected int m_noSelected = 0;
	
	protected Vector<String> selected = new Vector<String>();

	/**	Logger */
	protected static CLogger log = CLogger.getCLogger(XX_ProductFilter.class);
		
	//PANEL
	protected CPanel mainPanel = new CPanel();
	protected BorderLayout mainLayout = new BorderLayout();
	protected CPanel parameterPanel = new CPanel();
	protected MiniTablePreparator miniTable = new MiniTablePreparator();
	protected CPanel commandPanel = new CPanel();
	protected GridBagLayout parameterLayout = new GridBagLayout();
	protected JScrollPane dataPane = new JScrollPane();
	protected FlowLayout commandLayout = new FlowLayout();
	
	//BOTONES
	protected JButton bCancel = ConfirmPanel.createCancelButton(true);
	protected JButton bGenerate = ConfirmPanel.createProcessButton(true);	
	protected CButton bReset = new CButton(Msg.translate(Env.getCtx(), "XX_ClearFilter"));
	protected CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "XX_Search"));
	
	//CATEGORIA
	protected CLabel labelCategory = new CLabel();
	protected VComboBox comboCategory = new VComboBox();
	
	//DEPARTAMENTO
	protected VCheckBox checkDepartment = new VCheckBox();
	protected CLabel labelDepartment = new CLabel();
	protected VComboBox comboDepartment = new VComboBox();
	
	//LINEA
	protected VCheckBox checkLine = new VCheckBox();
	protected CLabel labelLine = new CLabel();
	protected VComboBox comboLine = new VComboBox();
	
	//SECCION
	protected VCheckBox checkSection = new VCheckBox();
	protected CLabel labelSection = new CLabel();
	protected VComboBox comboSection = new VComboBox();
	
	//MARCA
	protected VCheckBox checkBrand = new VCheckBox();
	protected CLabel labelBrand = new CLabel();
	protected VComboBox comboBrand = new VComboBox();
	
	//BUSINESS PARTNER
	protected VCheckBox checkBPartner = new VCheckBox();
	protected CLabel labelBPartner = new CLabel();
	protected VComboBox comboBPartner = new VComboBox();
	
	//PRODUCTO
	protected VCheckBox checkProduct = new VCheckBox();
	protected CLabel labelProduct = new CLabel();
	protected VLookup lookupProduct = null;
	
	//REFERENCIA
	protected VCheckBox checkReference = new VCheckBox();
	protected CLabel labelReference = new CLabel();
	protected VComboBox comboReference =  new VComboBox();
	protected CLabel labelCodRef = new CLabel();
	protected CTextField textReference = new CTextField();
	
	// FECHAS 
	protected VDate fromDate = new VDate();
	protected VDate toDate = new VDate();
	protected CLabel fromLabel = new CLabel();
	protected CLabel toLabel = new CLabel();
	
	//OTROS
	protected JLabel dataStatus = new JLabel();
	protected int indx = 0;
	protected boolean product = false;
	protected boolean reference = false;
	protected boolean brand = false;
	protected CLabel labelResults = new CLabel();
	protected CTextField textResults = new CTextField();
	
	// Seleccionar todos en la tabla
	protected CLabel labelSelectAll = new CLabel("Seleccionar Todos");
	protected CCheckBox checkSelectAll = new CCheckBox();

	// Form ID
	protected Integer FormID = Integer.parseInt(Env.getCtx().getContext("#XX_SearchFilterForm_ID"));
	// Tabla ID
	protected Integer TableID = Integer.parseInt(Env.getCtx().
								getContext( "#XX_VME_Table_ID"));
	// Proceso ID
	protected Integer ProcessID = Integer.parseInt(Env.getCtx().
								getContext( "#XX_VME_Process_ID"));

	// Vectores que contiene los resultados del filtro
	protected Vector<KeyNamePair> Category = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Department = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Line = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Section = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Reference = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Brand = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Vendor = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Partner = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Product = new Vector<KeyNamePair>();
	protected int refSelected = 0;
	
	// Resultados del Filtro
	protected XX_VME_ProductFilterResults results = 
		new XX_VME_ProductFilterResults(Category, Department, Line, Section,
				Reference, Brand, Vendor, Partner, Product);
	/** Static Init
	 *  @throws Exception
	 */
	private final void jbInit() throws Exception {
		bSearch.setPreferredSize(new Dimension(100, 25));
		bReset.setPreferredSize(new Dimension(100, 25));
		//m_frame.getRootPane().setDefaultButton(bSearch);
		CompiereColor.setBackground(this);		
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);	
	
		labelCategory.setText(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"));
		labelDepartment.setText(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"));
		labelLine.setText(Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"));
		labelSection.setText(Msg.translate(Env.getCtx(), "XX_VMR_Section_ID"));
		labelBrand.setText(Msg.translate(Env.getCtx(), "XX_VMR_Brand"));
		labelBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		labelProduct.setText(Msg.translate(Env.getCtx(), "M_PRODUCT_ID"));
		labelReference.setText(Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"));
		labelCodRef.setText(Msg.translate(Env.getCtx(), "XX_CodRef"));
		labelResults.setText(Msg.translate(Env.getCtx(), "XX_Results"));
		// Fechas
		fromLabel.setText(Msg.translate(Env.getCtx(), "From"));
		toLabel.setText(Msg.translate(Env.getCtx(), "XX_To"));

		// PANEL CREATION
		mainPanel.add(parameterPanel, BorderLayout.NORTH);		
		
		// MARCA
		parameterPanel.add(labelBrand,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboBrand,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkBrand,  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// BP
		parameterPanel.add(labelBPartner,  new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboBPartner,  new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkBPartner,  new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// CATEGORIA
		parameterPanel.add(labelCategory,  new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboCategory,  new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// DEPARTAMENTO
		parameterPanel.add(labelDepartment,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboDepartment,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkDepartment,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));	
		// LINEA
		parameterPanel.add(labelLine,   new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboLine,    new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkLine,  new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// SECCION	
		parameterPanel.add(labelSection,  new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboSection,   new GridBagConstraints(9, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkSection,  new GridBagConstraints(10, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));	
		// PRODUCTO	
		parameterPanel.add(labelProduct,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(lookupProduct,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkProduct,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// REFERENCIA
		parameterPanel.add(labelReference,  new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboReference,  new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkReference,  new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelCodRef,  new GridBagConstraints(8, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(textReference,  new GridBagConstraints(9, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// FROM
		parameterPanel.add(fromLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fromDate, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		// TO
		parameterPanel.add(toLabel, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(toDate, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		// SELECCIONAR TODAS
		parameterPanel.add(labelSelectAll, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkSelectAll, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// BUSCAR
		parameterPanel.add(bSearch, new GridBagConstraints(9, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// RESET
		parameterPanel.add(bReset,    new GridBagConstraints(8, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// RESULTADOS
		// RESET
		parameterPanel.add(labelResults, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// RESET
		parameterPanel.add(textResults, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	
		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		dataPane.setPreferredSize(new Dimension(1280, 600));
		textReference.setPreferredSize(new Dimension(150, 25));

		//PANEL INFERIOR
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(bCancel, null);
		commandPanel.add(bGenerate, null);
		
		bReset.addActionListener(this);
		bSearch.addActionListener(this);
		bCancel.addActionListener(this);
		bGenerate.addActionListener(this);
	}   //  jbInit
	
	/** Dynamic Init.
	 *  - Load Department Info
	 *  - Load BPartner Info
	 *  - Load Brand Info
	 *  - Init Table
	 */
	protected void dynInit() {
		desactivarActionListeners();
		loadBasicInfo();
		activarActionListeners();
	}   //  dynInit
	
	/** loadBasicInfo
	 *  Table initial state 
	 */
	public final void loadBasicInfo() {
		desactivarActionListeners();
		//Restaura los ComboBoxes y CheckBoxes		
		comboCategory.setEnabled(true);
		comboDepartment.setEnabled(false);
		comboLine.setEnabled(false);
		comboSection.setEnabled(false);
		comboBrand.setEnabled(true);
		comboBPartner.setEnabled(true);
		comboReference.setEnabled(false);
		bGenerate.setEnabled(true);
		comboCategory.removeAllItems();
		comboDepartment.removeAllItems();
		comboLine.removeAllItems();
		comboSection.removeAllItems();
		comboBrand.removeAllItems();
		comboBPartner.removeAllItems();
		comboReference.removeAllItems();
		lookupProduct.setValue(null);
		lookupProduct.setEnabled(false);
		checkDepartment.setEnabled(false);
		checkDepartment.setValue(false);
		checkLine.setValue(false);
		checkLine.setEnabled(false);
		checkSection.setValue(false);
		checkSection.setEnabled(false);
		checkBrand.setEnabled(true);
		checkBPartner.setEnabled(true);
		checkProduct.setEnabled(false);
		checkProduct.setValue(false);
		checkReference.setEnabled(false);
		checkReference.setValue(false);
		textReference.setEditable(false);
		textReference.setValue("");
		fromDate.setEnabled(false);
		toDate.setEnabled(false);
		
		//Setting Default Items
		KeyNamePair loadKNP = new KeyNamePair(0, "");
		comboCategory.addItem(loadKNP);
		comboDepartment.addItem(loadKNP);
		comboLine.addItem(loadKNP);
		comboSection.addItem(loadKNP);
		comboBrand.addItem(loadKNP);
		comboBPartner.addItem(loadKNP);
		comboReference.addItem(loadKNP);
		
		// Cantidad del elemento
		textResults.setEditable(false);
		textResults.setBackground(Color.LIGHT_GRAY);
		textResults.setPreferredSize(new Dimension(80, 25));
		textResults.setHorizontalAlignment(JTextField.CENTER);
		
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllDepartments"));
		comboDepartment.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllLines"));
		comboLine.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllSections") );
		comboSection.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllBrands") );
		comboBrand.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllBusinessPartner") );
		comboBPartner.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllReferences") );
		comboReference.addItem(loadKNP);
		
		// CARGA DE CATEGORIAS
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||' - '||NAME FROM XX_VMR_CATEGORY WHERE ISACTIVE='Y' ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
			+ " AND ISACTIVE = 'Y' ORDER BY VALUE||' - '||NAME";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCategory.addItem(loadKNP);	
			}
			rs.close();
			pstmt.close();
			comboCategory.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		
		// CARGA DE DEPARTAMENTOS
		sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||' - '||NAME FROM XX_VMR_DEPARTMENT ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
			+ " AND ISACTIVE = 'Y'  ORDER BY VALUE||' - '||NAME";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);	
			}
			rs.close();
			pstmt.close();
			comboDepartment.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		
		// CARGA DE MARCAS
		sql = "SELECT XX_VMR_BRAND_ID, VALUE||' - '||NAME FROM XX_VMR_BRAND " ;
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) +
				" AND ISACTIVE = 'Y'  ORDER BY VALUE||' - '||NAME";
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBrand.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		
		// CARGA DE BPS
		sql = "SELECT C_BPARTNER_ID, VALUE||' - '||NAME FROM C_BPARTNER WHERE " +
				" ISVENDOR = 'Y' AND XX_ISVALID = 'Y' AND ISACTIVE = 'Y'";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) + 
			  " ORDER BY NAME ";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
			comboBPartner.setSelectedIndex(0);
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}	
		
		activarActionListeners();
	} // loadBasicInfo
	
	/** loadDepartmentInfo
	 * Cargar información de departamentos
	 * */
	protected final void loadDepartmentInfo(){
		comboDepartment.removeAllItems();
		comboDepartment.setEnabled(true);
		checkDepartment.setEnabled(true);
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();		
		KeyNamePair loadKNP;
		String sql = "";
		
		if(bpar == null || bpar.getKey() == 0 || bpar.getKey() == 99999999){ 
			loadKNP = new KeyNamePair(0,"");
			comboDepartment.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
			comboDepartment.addItem(loadKNP);	
				
			sql = " SELECT XX_VMR_DEPARTMENT_ID, VALUE||' - '||NAME FROM XX_VMR_DEPARTMENT " +
				" WHERE ISACTIVE = 'Y'";
			
			// HAY UNA CATEORIA SELECCIONADA
			if(cat!= null && cat.getKey() != 99999999 && cat.getKey()!= 0){
				sql += " AND XX_VMR_CATEGORY_ID = " + cat.getKey();
			}
			
			sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
					+ " ORDER BY VALUE||' - '||NAME";			
		} // IF BPAR 
		else {
				loadKNP = new KeyNamePair(0,"");
				comboDepartment.addItem(loadKNP);
				loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
				comboDepartment.addItem(loadKNP);	
				sql = " SELECT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||' - '||dp.NAME" 
					+ " FROM XX_VMR_VENDORDEPARTASSOCI ve, XX_VMR_DEPARTMENT dp" 
					+ " WHERE ve.XX_VMR_DEPARTMENT_ID = dp.XX_VMR_DEPARTMENT_ID"
					+ " AND ve.C_BPARTNER_ID = " + bpar.getKey();	
				
				if(cat != null && cat.getKey() != 99999999){
					sql += " AND dp.XX_VMR_CATEGORY_ID = " + cat.getKey();
				}
				sql += " ORDER BY dp.VALUE||' - '||dp.NAME";	
		} // ELSE BPAR
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				dept = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(dept);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}		
	} // loadDepartmentInfo
	
	/** loadLineInfo
	 *  Cargar información de linea
	 */
	protected final void loadLineInfo(){
		KeyNamePair dpto = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair loadKNP;
		KeyNamePair line;
		String sql = "";
		if(dpto == null)
			return;
		
		comboLine.removeAllItems();
		comboLine.setEnabled(true);
		checkLine.setEnabled(true);
		loadKNP = new KeyNamePair(0,"");
		comboLine.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllLines"));
		comboLine.addItem(loadKNP);
		
		if (dpto.getKey() == 0 || dpto.getKey() == 99999999){
			return;
		}
		else{
			sql = " SELECT li.XX_VMR_LINE_ID, li.VALUE||' - '||li.NAME FROM XX_VMR_LINE li " 
				+ " WHERE li.isactive = 'Y' and li.XX_VMR_DEPARTMENT_ID = " + dpto.getKey() 
				+ " ORDER BY li.VALUE ";				
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			 pstmt = DB.prepareStatement(sql, null);
			    rs = pstmt.executeQuery();
			while (rs.next()) {
				line = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboLine.addItem(line);
			}
		}
		catch (SQLException e){
			log.log(Level.SEVERE, sql, e);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadLineInfo
	
	/** loadSectionInfo
	 * Cargar informacion de seccion
	 * */
	protected final void loadSectionInfo(){
		KeyNamePair dpto = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair loadKNP;
		KeyNamePair sect;
		String sql = "";
		
		if(dpto == null || line == null )
			return;
		
		comboSection.removeAllItems();
		checkSection.setEnabled(true);
		comboSection.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboSection.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllSections"));
		comboSection.addItem(loadKNP);	
		
		if(line.getKey() == 0 || line.getKey() == 99999999){
			return;
		}
		else {
			comboSection.setEnabled(true);
			sql = "SELECT se.XX_VMR_SECTION_ID, se.VALUE||' - '||se.NAME" 
				+ " FROM XX_VMR_SECTION se WHERE se.IsActive = 'Y'"
				+ " and se.XX_VMR_LINE_ID = " + line.getKey();
		}		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				sect = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboSection.addItem(sect);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
	} // loadSectionInfo
	
	/** loadReference
	 * Cargar informacion de las referencias de proveedor
	 * */
	protected final void loadReference(){
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair bp = (KeyNamePair)comboBPartner.getSelectedItem();
		KeyNamePair brand = (KeyNamePair)comboBrand.getSelectedItem();
		KeyNamePair loadKNP;
		KeyNamePair reference = null;
		String sql = "";
		
		comboReference.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboReference.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllReferences"));
		comboReference.addItem(loadKNP);
		
		// HAY UN DEPARTAMENTO SELECCIONADO
		if(dept == null || dept.getKey() == 0 || dept.getKey() == 99999999) {	
			return;
			
		} // IF DEPT 
		else {	
			comboReference.setEnabled(true);
			sql = " SELECT VR.XX_VMR_VENDORPRODREF_ID, VR.VALUE||' - '||VR.NAME" 
				+ " FROM XX_VMR_VENDORPRODREF VR "  
				+ " WHERE IsActive = 'Y' "
				+ " AND VR.XX_VMR_DEPARTMENT_ID = 0";
			if((line != null ) && line.getKey() != 0 && line.getKey() != 99999999) {
				sql += " AND VR.XX_VMR_LINE_ID = " + line.getKey();
			}
			if((sect != null) && sect.getKey() != 0 && sect.getKey() != 99999999) {
				sql += " AND VR.XX_VMR_SECTION_ID = " + sect.getKey();
			}
			if((bp != null) && bp.getKey() != 0 && bp.getKey() != 99999999) {
				sql += " AND VR.C_BPARTNER_ID = " + bp.getKey();
			}
			if((brand != null) && brand.getKey() != 0 && brand.getKey() != 99999999) {
				sql += " AND VR.XX_VMR_BRAND_ID = " + brand.getKey();
			}
		} // ELSE DEPT
		//System.out.println(sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			 pstmt = DB.prepareStatement(sql, null);
			    rs = pstmt.executeQuery();
			while (rs.next()) {
				reference = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboReference.addItem(reference);
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadReference
	
	/** loadBPartnerInfo
	 * Cargar informacion de Socio de negocio 
	 * */
	protected final void loadBPartnerInfo(){
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();
		KeyNamePair loadKNP;
		String sql = "";
		
		// HAY UN DEPARTAMENTO SELECCIONADO
		if(dept == null || dept.getKey() == 0 || dept.getKey() == 99999999) {			
			comboBPartner.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboBPartner.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllBusinessPartner"));
			comboBPartner.addItem(loadKNP);
				
			sql = "SELECT C_BPARTNER_ID, VALUE||' - '||NAME FROM C_BPARTNER WHERE " +
				" ISVENDOR = 'Y' AND XX_ISVALID = 'Y' AND ISACTIVE = 'Y'";
			sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) + 
				" ORDER BY VALUE||' - '||NAME ";							//
		} // IF DEPT 
		else {
			comboBPartner.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboBPartner.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllBusinessPartner"));
			comboBPartner.addItem(loadKNP);	
			sql = " SELECT bp.C_BPARTNER_ID, bp.NAME" 
				+ " FROM XX_VMR_VENDORDEPARTASSOCI ve, C_BPARTNER bp " 
				+ " WHERE ve.C_BPARTNER_ID = bp.C_BPARTNER_ID AND ve.XX_VMR_DEPARTMENT_ID = "+ dept.getKey(); 						
		} // ELSE DEPT	
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
			comboBPartner.setSelectedItem(bpar);
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
	} // loadBPartnerInfo
	
	/** Activa todos los listeners creados */
	protected final void activarActionListeners () {
		comboCategory.addActionListener(this);
		comboDepartment.addActionListener(this);
		checkDepartment.addActionListener(this);
		comboLine.addActionListener(this);
		checkLine.addActionListener(this);
		comboSection.addActionListener(this);
		checkSection.addActionListener(this);
		comboBrand.addActionListener(this);
		checkBrand.addActionListener(this);
		comboBPartner.addActionListener(this);
		checkBPartner.addActionListener(this);
		checkProduct.addActionListener(this);
		lookupProduct.addActionListener(this);
		comboReference.addActionListener(this);
		checkReference.addActionListener(this);
		checkSelectAll.addActionListener(this);
	}//addActionListeners
	
	/** Deshabilitar Action Listeners */
	protected final void desactivarActionListeners () {
		comboCategory.removeActionListener(this);
		comboDepartment.removeActionListener(this);
		checkDepartment.removeActionListener(this);
		comboLine.removeActionListener(this);
		checkLine.removeActionListener(this);
		comboSection.removeActionListener(this);
		checkSection.removeActionListener(this);
		comboBrand.removeActionListener(this);
		checkBrand.removeActionListener(this);
		comboBPartner.removeActionListener(this);
		checkBPartner.removeActionListener(this);
		checkProduct.removeActionListener(this);
		lookupProduct.removeActionListener(this);
		comboReference.removeActionListener(this);
		checkReference.removeActionListener(this);
	} // removeActionListeners

	/** loadTableInfo
	 *  Busqueda para llenar la TableInfo de acuerdo con las especificaciones 
	 *  del usuario
	 */
	protected void loadTableInfo(){
		log.config("");
		//  not yet initialized
		dataPane.getViewport().remove(miniTable);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable, null);
		checkSelectAll.setEnabled(true);
		checkSelectAll.setValue(false);
		m_frame.setCursor(Cursor.WAIT_CURSOR);
		
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair bran = (KeyNamePair)comboBrand.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();
		KeyNamePair refe = (KeyNamePair)comboReference.getSelectedItem();
		
		// Columnas
		ColumnInfo colCat = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), "cat.VALUE||' - '||cat.Name", KeyNamePair.class, true, false, "cat.XX_VMR_CATEGORY_ID");
		ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"), "dp.VALUE||' - '||dp.Name", KeyNamePair.class, true, false, "dp.XX_VMR_DEPARTMENT_ID");
		ColumnInfo colLine = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Line_ID"), "li.VALUE||' - '||li.NAME", KeyNamePair.class, true, false, "li.XX_VMR_LINE_ID");
		ColumnInfo colSect = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Section_ID"), "se.VALUE||' - '||se.NAME", KeyNamePair.class, true, false, "se.XX_VMR_SECTION_ID");
		ColumnInfo colBran = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Brand"), "br.VALUE||' - '||br.Name", KeyNamePair.class, true, false, "br.XX_VMR_BRAND_ID");
		ColumnInfo colBPar = new ColumnInfo(Msg.translate(ctx, "C_BPARTNER_ID"), "bp.VALUE||' - '||bp.NAME", KeyNamePair.class, true, false, "bp.C_BPARTNER_ID");
		ColumnInfo colProd = new ColumnInfo(Msg.translate(ctx, "M_PRODUCT_ID"), "P.VALUE||' - '||P.NAME||' - '||I.DESCRIPTION", KeyNamePair.class, true, false, "P.M_PRODUCT_ID");
		ColumnInfo colRef = new ColumnInfo(Msg.translate(ctx, "XX_VMR_vendorProdRef_ID"), "vr.VALUE||' - '||vr.Name||' - '||LC.NAME", KeyNamePair.class, true, false, "vr.XX_VMR_VENDORPRODREF_ID");
		
		Vector<ColumnInfo> columns = new Vector<ColumnInfo>();
		String from = new String(" FROM ");
		String where = new String(" WHERE ");	
		String groupby = new String();
		String group_by_ID = "";
		String tableName = "";
		
		if(lookupProduct.getValue() != null || (Boolean)checkProduct.getValue()){
			product = true;
		}
			
		// Si es producto o referencia lo que se busca
		//if(product) {
		from += " XX_VMR_VENDORPRODREF VR  LEFT OUTER JOIN ";
			from += " M_PRODUCT P ON (VR.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID) " +
					" LEFT OUTER JOIN M_ATTRIBUTESETINSTANCE I " +
					" ON (P.M_ATTRIBUTESETINSTANCE_ID = I.M_ATTRIBUTESETINSTANCE_ID) "; 
			//if(fromDate.getValue() != null || toDate.getValue() != null){
			from += " LEFT OUTER JOIN XX_VMR_PRICECONSECUTIVE PC" +
					" ON (PC.M_PRODUCT_ID = P.M_PRODUCT_ID)";
			from += " LEFT OUTER JOIN XX_VMR_PO_LINEREFPROV POLINE " +
					" ON (P.XX_VMR_VENDORPRODREF_ID = POLINE.XX_VMR_VENDORPRODREF_ID)";
			from+=  " LEFT OUTER JOIN XX_VMR_LONGCHARACTERISTIC LC " +
					" ON (VR.XX_VMR_LONGCHARACTERISTIC_ID = LC.XX_VMR_LONGCHARACTERISTIC_ID) ";
			where += " (p.IsActive is null or p.IsActive='Y') AND VR.IsActive = 'Y' AND "+
					" (p.IsActive is null or PC.XX_VMR_PRICECONSECUTIVE_ID IS NOT NULL OR POLINE.XX_VMR_PO_LINEREFPROV_ID IS NOT NULL)";
			//}
				
		/*} //if
		else {
			from += " XX_VMR_VENDORPRODREF VR LEFT OUTER JOIN XX_VMR_LONGCHARACTERISTIC LC" 
				+ " ON (VR.XX_VMR_LONGCHARACTERISTIC_ID = LC.XX_VMR_LONGCHARACTERISTIC_ID)"; 
			from += " LEFT OUTER JOIN M_PRODUCT P " 
				+ " ON (P.XX_VMR_VENDORPRODREF_ID = VR.XX_VMR_VENDORPRODREF_ID)" ;
			from += " LEFT OUTER JOIN XX_VMR_PRICECONSECUTIVE PC" +
				" ON (PC.M_PRODUCT_ID = P.M_PRODUCT_ID)";
			from += " LEFT OUTER JOIN XX_VMR_PO_LINEREFPROV POLINE " +
				" ON (VR.XX_VMR_VENDORPRODREF_ID = POLINE.XX_VMR_VENDORPRODREF_ID)";
			where += " VR.IsActive = 'Y' " +
				" AND (PC.XX_VMR_PRICECONSECUTIVE_ID IS NOT NULL OR POLINE.XX_VMR_PO_LINEREFPROV_ID IS NOT NULL)";
		} //if*/

		// Leer Categoria
		boolean category = false;
		
		//Leyendo referencia
		if((Boolean)checkReference.getValue() == true  || refe.getKey() == 99999999 || refe.getKey()!= 0 ){
			columns.add(colRef);
			groupby += ", vr.VALUE||' - '||vr.Name||' - '||LC.NAME, VR.XX_VMR_VENDORPRODREF_ID";
			group_by_ID = " VR.XX_VMR_VENDORPRODREF_ID";		
			tableName = "VR";
			
			if (refe.getKey()!= 99999999) 
				where += " AND VR.XX_VMR_VENDORPRODREF_ID = " + refe.getKey();
	
		} //Leyendo referencia
		
		//Leyendo producto
		if((Boolean)checkProduct.getValue() == true || lookupProduct.getValue() != null ){
			columns.add(colProd);
			group_by_ID = "P.M_PRODUCT_ID";
			groupby += ", P.VALUE||' - '||P.NAME||' - '||I.DESCRIPTION, P.M_PRODUCT_ID";
			tableName = "P";
			
			if (lookupProduct.getValue() != null && checkProduct.getValue().equals(false)){
				where += " AND P.M_PRODUCT_ID = " + lookupProduct.getValue();	
			}
			
			////
			if(fromDate.getValue() != null)
				where += " AND PC.CREATED >= "+ DB.TO_DATE((Timestamp)fromDate.getValue());
			if(toDate.getValue() != null)
				where += " AND PC.CREATED <= "+ DB.TO_DATE((Timestamp)toDate.getValue());
			////
		} // if producto
		
		// Leyendo codigo de referencia
		if(!product && textReference.getValue() != null){
			where += " AND VR.VALUE LIKE UPPER ('%" + textReference.getValue() + "%') ";
		}// Leyendo codigo de referencia
		
		if(cat.getKey()!= 0 ){
			category = true;
			if(product) {
				from += " INNER JOIN XX_VMR_CATEGORY CAT " +
					" ON (CAT.XX_VMR_CATEGORY_ID = P.XX_VMR_CATEGORY_ID) ";
				from += " INNER JOIN XX_VMR_DEPARTMENT DP " +
					" ON (DP.XX_VMR_DEPARTMENT_ID = P.XX_VMR_DEPARTMENT_ID) ";	
			}
			else {
				from += " INNER JOIN XX_VMR_DEPARTMENT DP " +
					" ON (DP.XX_VMR_DEPARTMENT_ID = VR.XX_VMR_DEPARTMENT_ID) ";
				from += " INNER JOIN XX_VMR_CATEGORY CAT " +
					" ON (CAT.XX_VMR_CATEGORY_ID = DP.XX_VMR_CATEGORY_ID) ";
			}
			
			columns.add(colCat);
			columns.add(colDept);
			group_by_ID = " DP.XX_VMR_DEPARTMENT_ID";
			groupby += ", CAT.VALUE||' - '||CAT.Name, CAT.XX_VMR_CATEGORY_ID, " +
					" DP.VALUE||' - '||DP.Name, DP.XX_VMR_DEPARTMENT_ID";
			tableName = "CAT";
		
			if (cat.getKey()!= 99999999) 
				where += " AND CAT.ISACTIVE = 'Y' AND CAT.XX_VMR_CATEGORY_ID = " + cat.getKey();
		} // if categoria
		
		//Leer Departamento
		if(dept!=null){
			if((Boolean)checkDepartment.getValue()==true || dept.getKey()==99999999 ||  dept.getKey()!= 0 ){	
				if(!category){
					if(product) {
						from += " INNER JOIN XX_VMR_DEPARTMENT DP " +
							" ON (DP.XX_VMR_DEPARTMENT_ID = P.XX_VMR_DEPARTMENT_ID) ";
					}
					else {
						from += " INNER JOIN XX_VMR_DEPARTMENT DP " +
							" ON (DP.XX_VMR_DEPARTMENT_ID = VR.XX_VMR_DEPARTMENT_ID) ";
					}
					columns.add(colDept);
					group_by_ID = " DP.XX_VMR_DEPARTMENT_ID";
					groupby += ", DP.VALUE||' - '||DP.Name, DP.XX_VMR_DEPARTMENT_ID";
					tableName = "DP";
				} // if !category
				
				if (dept.getKey()!= 99999999 ) 
					where += " AND DP.ISACTIVE = 'Y' AND DP.XX_VMR_DEPARTMENT_ID = " + dept.getKey();				
			} // if department		
		} // if dept != null
		
		//Leer Linea
		if((Boolean)checkLine.getValue() == true || line.getKey() == 99999999 || line.getKey()!= 0){
			if(product) {
				from += " INNER JOIN XX_VMR_LINE li " +
						" ON (LI.XX_VMR_LINE_ID = P.XX_VMR_LINE_ID)" ;
			}
			else{
				from += " INNER JOIN XX_VMR_LINE li " +
					" ON (LI.XX_VMR_LINE_ID = VR.XX_VMR_LINE_ID)" ;
			}
			columns.add(colLine);
			group_by_ID = " LI.XX_VMR_LINE_ID ";
			groupby += ", LI.VALUE||' - '||LI.NAME, LI.XX_VMR_LINE_ID";			
			tableName = "LI";
			
			if (line.getKey()!= 99999999)
				where += " AND LI.ISACTIVE = 'Y' AND LI.XX_VMR_LINE_ID = " + line.getKey();

		} // if linea
		
		//Leyendo seccion 
		if ((Boolean)checkSection.getValue()==true || sect.getKey() == 99999999 || sect.getKey()!= 0){
			if(product){
				from += " INNER JOIN XX_VMR_SECTION SE " +
						" ON (SE.XX_VMR_SECTION_ID = P.XX_VMR_SECTION_ID) ";
			}
			else{
				from += " INNER JOIN XX_VMR_SECTION SE " +
						" ON (SE.XX_VMR_SECTION_ID = VR.XX_VMR_SECTION_ID) ";
			}
			
			columns.add(colSect);
			group_by_ID = " SE.XX_VMR_SECTION_ID ";
			groupby += ", SE.VALUE||' - '||SE.NAME, SE.XX_VMR_SECTION_ID";			
			tableName = "SE";
			
			if (sect.getKey()!= 99999999 ) 
				where += " AND SE.ISACTIVE = 'Y' AND SE.XX_VMR_SECTION_ID = " + sect.getKey();

		} // if seccion
		
		//Leyendo marca
		if ((Boolean)checkBrand.getValue()== true || bran.getKey() == 99999999 || bran.getKey()!= 0){
			if(product){
				from += " INNER JOIN XX_VMR_BRAND BR " +
					" ON (P.XX_VMR_BRAND_ID = BR.XX_VMR_BRAND_ID) ";
			}
			else {
				from += " INNER JOIN XX_VMR_BRAND BR " +
					" ON (BR.XX_VMR_BRAND_ID = VR.XX_VMR_BRAND_ID) ";
			}
			columns.add(colBran);
			group_by_ID = " BR.XX_VMR_BRAND_ID";
			groupby += ", BR.VALUE||' - '||BR.Name, BR.XX_VMR_BRAND_ID";			
			tableName = "BR";
			
			if (!(Boolean)checkBrand.getValue()== true && bran.getKey()!= 99999999 && bran.getKey()!= 0) { 
				where += " AND BR.ISACTIVE = 'Y' AND BR.XX_VMR_BRAND_ID = " + bran.getKey();
			}

		} // if marca
		
		
		//Leyendo proveedor
		if((Boolean)checkBPartner.getValue() == true || bpar.getKey() == 99999999 || bpar.getKey() != 0){
			if(product){
				from += " INNER JOIN C_BPARTNER BP " +
					" ON (BP.C_BPARTNER_ID = P.C_BPARTNER_ID) ";
			}
			else {
				from += " INNER JOIN C_BPARTNER BP " +
				" ON (BP.C_BPARTNER_ID = VR.C_BPARTNER_ID) ";
			}
			columns.add(colBPar);
			group_by_ID = "BP.C_BPARTNER_ID";
			groupby += ", BP.VALUE||' - '||BP.NAME, BP.C_BPARTNER_ID";			
			tableName = "BP";
			
			if (bpar.getKey()!= 99999999) 
				where += " AND BP.ISACTIVE = 'Y' AND BP.C_BPARTNER_ID = " + bpar.getKey();	

		} // if proveedor
	
		//Si se intenta buscar sin seleccionar filtro, debe irse
		if (columns.size() == 0) {
			m_frame.setCursor(Cursor.getDefaultCursor());
			return;
		}

		//Preparar el columninfo
		ColumnInfo [] columns_array = new ColumnInfo[columns.size() + 1];
		columns.insertElementAt(new ColumnInfo("", group_by_ID, IDColumn.class, false, false, ""), 0);
		
		for (int i = 0; i < columns.size() ; i++) {
			columns_array[i] = columns.elementAt(i);
		}		
		indx = columns_array.length - 2;
				
		//Preparar la tabla				
		try {
			String select = miniTable.prepareTable(columns_array, "", "", true, tableName);
			m_sql = new StringBuilder(where.length() + from.length() + select.length() );
			m_sql.append(select);
			m_sql.append(from);
			m_sql.append(where);
			m_sql.append(" GROUP BY " + group_by_ID);
			m_sql.append(groupby);
			m_sql.append(" ORDER BY " + group_by_ID);
			//System.out.println("SQL TOTAL (Product Filter): " +m_sql.toString());
		} 
		catch (Exception e) {
			m_frame.setCursor(Cursor.getDefaultCursor());
			return ;
		}	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {		
			pstmt = DB.prepareStatement(m_sql.toString(), null);			
			rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
		}
		catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.SEVERE, m_sql.toString(), e);
			m_frame.setCursor(Cursor.getDefaultCursor());
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		miniTable.getColumnModel().getColumn(indx+1);
		miniTable.repaint();
		miniTable.setMultiSelection(true);
		miniTable.setRowSelectionAllowed(false);
		miniTable.autoSize();
		miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
		miniTable.getTableHeader().setReorderingAllowed(false);
		
		textResults.setText(String.valueOf(miniTable.getRowCount()));
		m_frame.setCursor(Cursor.getDefaultCursor());
		
	}   //  loadTableInfo
	
	/**
	 * 	Dispose
	 */
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose
	
	/**
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e) {
		desactivarActionListeners();
		//Category Combo 
		if (e.getSource() == comboCategory){			
			if(comboCategory.getValue() != null){
				if(!comboCategory.getValue().equals(Integer.valueOf(0))){
					checkDepartment.setEnabled(true);
					comboDepartment.setEnabled(true);
					comboLine.setEnabled(false);
					checkLine.setEnabled(false);
					comboSection.setEnabled(false);
					checkSection.setEnabled(false);
					comboReference.setEnabled(false);
					checkReference.setEnabled(false);
					comboDepartment.removeActionListener(this);
					loadDepartmentInfo();
					comboDepartment.addActionListener(this);
				}
				else{
					comboDepartment.setEnabled(false);
					checkDepartment.setEnabled(false);
					comboLine.setEnabled(false);
					checkLine.setEnabled(false);
					comboSection.setEnabled(false);
					checkSection.setEnabled(false);
					comboReference.setEnabled(false);
					checkReference.setEnabled(false);
				}
			}
			
		}//Category Combo 
		
		//Updating Line ComboBox after a Department is selected 
		else if (e.getSource() == comboDepartment){			
			checkBPartner.setValue(false);
			checkReference.setValue(false);
			lookupProduct.setEnabled(true);
			checkProduct.setEnabled(true);
			
			if(comboDepartment.getValue() != null){
				if(comboDepartment.getValue().equals(Integer.valueOf(0))) {
					comboLine.setEnabled(false);
					checkLine.setEnabled(false);
					comboSection.setEnabled(false);
					checkSection.setEnabled(false);
					comboReference.setEnabled(false);
					checkReference.setEnabled(false);
				}
				else {
					comboLine.setEnabled(true);
					checkLine.setEnabled(true);
					comboSection.setEnabled(false);
					checkSection.setEnabled(false);
					comboReference.setEnabled(true);
					checkReference.setEnabled(true);
					comboReference.removeActionListener(this);
					//loadReference();
					comboReference.addActionListener(this);
				}
			}// dep null
			comboLine.removeActionListener(this);
			loadLineInfo();
			comboLine.removeActionListener(this);
			comboBPartner.removeActionListener(this);
			loadBPartnerInfo();
			comboBPartner.addActionListener(this);
		}// line and dept
		
		//Department CheckBox  
		else if(e.getSource() == checkDepartment){
			if((Boolean)checkDepartment.getValue() == true){
				comboDepartment.setValue(99999999);
				comboDepartment.setEnabled(false);
				comboLine.setValue(0);
				comboLine.setEnabled(false);
				comboSection.setValue(0);
				comboSection.setEnabled(false);
				comboReference.setValue(0);
				comboReference.setEnabled(false);
			}
			else{	
				comboDepartment.setEnabled(true);
				comboLine.setEnabled(true);
				checkLine.setValue(false);
				comboSection.setEnabled(true);
				checkSection.setValue(false);
				comboReference.setEnabled(true);
				checkReference.setValue(false);
				lookupProduct.setEnabled(true);
				checkProduct.setValue(false);
			}

		} //Department CheckBox 
		
		//Updating Section ComboBox after a Line is selected
		else if (e.getSource() == comboLine ){
			if(comboLine.getValue() != null){
				if(!comboLine.getValue().equals(Integer.valueOf(0))) {
					comboSection.setEnabled(true);
					checkSection.setValue(false);
					loadSectionInfo();
					loadReference();
				}
				else{
					comboSection.setEnabled(false);
					checkSection.setValue(false);
				}
			}

		} // Line combobox
		
		//Line CheckBox
		else if(e.getSource() == checkLine){
			if((Boolean)checkLine.getValue() == true){
				if((Boolean)checkDepartment.getValue() == true || 
						((KeyNamePair)comboDepartment.getSelectedItem()).getKey() != 0){
					comboLine.setValue(99999999);
					comboLine.setEnabled(false);
				}
				else{
					ADialog.error(m_WindowNo, this, "No es posible seleccionar todas " +
							" las Líneas sin especificar el Departamento", 
							Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"));
					checkLine.setValue(false);
				}
			} // if checkline
			else{
				comboLine.setEnabled(true);
				loadLineInfo();
				comboLine.setValue(0); 
			}
		} //Line CheckBox
		
		//Section CheckBox
		else if(e.getSource() == checkSection){
			if((Boolean)checkSection.getValue() == true){
				if((Boolean)checkLine.getValue() == true || 
						((KeyNamePair)comboLine.getSelectedItem()).getKey() != 0){
					comboSection.setValue(99999999);
					comboSection.setEnabled(false);
				}
				else{
					ADialog.error(m_WindowNo, this, "No es posible seleccionar todas " +
							" las secciones sin especificar una linea", 
							Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"));
					checkSection.setValue(false);
				}
			} // checkSection
			else{	
				comboSection.setEnabled(true);
				loadSectionInfo();
				comboSection.setValue(0);
				loadReference();
			}
		} //Section CheckBox
		
		//Brand ComboBox
		else if(e.getSource() == comboBrand){
			if(comboBrand.getValue() != null){
				if(!comboBrand.getValue().equals(Integer.valueOf(0))) {
					comboReference.setEnabled(true);
					checkReference.setEnabled(true);
					lookupProduct.setEnabled(true);
					checkProduct.setEnabled(true);
					loadReference();
				}
			}
		} // comboBox brand
		
		//Brand CheckBox
		else if(e.getSource() == checkBrand){
			if((Boolean)checkBrand.getValue() == true){
				brand = true;
				comboBrand.setValue(99999999);
				comboBrand.setEnabled(false);
			}
			else{
				brand = false;
				comboBrand.setEnabled(true);
				comboBrand.setValue(0);
				comboReference.setEnabled(true);
				checkReference.setEnabled(true);
				lookupProduct.setEnabled(true);
				checkProduct.setEnabled(true);
				loadReference();
			}
		} //Brand CheckBox
		
		//Updating Department ComboBox when a Business Partner is selected
		else if(e.getSource() == comboBPartner) {
			comboDepartment.removeActionListener(this);
			loadDepartmentInfo();
			comboDepartment.addActionListener(this);
		}
		
		//Business Partner CheckBox
		else if(e.getSource() == checkBPartner){
			if((Boolean)checkBPartner.getValue() == true){
				comboBPartner.setValue(99999999);
				comboBPartner.setEnabled(false);
			}
			else{
				comboBPartner.setEnabled(true);
				comboBPartner.setValue(0);			
				loadBPartnerInfo();
			}
		}//Business Partner CheckBox
		
		//Product Lookup
		else if(e.getSource() == lookupProduct){
			if(lookupProduct.getValue() != null ){
				product = true;
			}
		} //Product Lookup
		
		//Product CheckBox
		else if(e.getSource() == checkProduct){
			if((Boolean) checkProduct.getValue() == true){
				product = true;
				lookupProduct.setEnabled(false);
				textReference.setEditable(false);
				fromDate.setEnabled(true);
				toDate.setEnabled(true);
			} // checkProduct
			else{
				product = false;
				lookupProduct.setValue(null);
				lookupProduct.setEnabled(true);
				fromDate.setEnabled(false);
				toDate.setEnabled(false);
				fromDate.setValue(null);
				toDate.setValue(null);
			}
		} //Product CheckBox
		
		// Combobox Reference
		else if(e.getSource() == comboReference) {
			textReference.setEditable(true);
		}
		
		//Reference CheckBox
		else if(e.getSource() == checkReference){
			if((Boolean)checkReference.getValue() == true){
				comboReference.setValue(99999999);
				comboReference.setEnabled(false);
				textReference.setEditable(true);
			}
			else{
				comboReference.setEnabled(true);			
				loadReference();
				comboReference.setValue(0);
			}
		}//Reference CheckBox
			
		//Clean Form
		else if(e.getSource() == bReset){
			limpiarFiltro();
		} // bReset
		
		//Generate Selection
		else if (e.getSource() == bGenerate ) {
			processSelection();
		} // bGenerate
		
		//Cancel Button Action
		else if (e.getSource() == bCancel) {
			dispose();
		} // bCancel

		//Execute Search
		else if(e.getSource() == bSearch) {
			try {
				loadTableInfo();
			} 
			catch (NullPointerException n) {
				n.printStackTrace();
			}
			if(miniTable.getRowCount()>0){
				labelSelectAll.setVisible(true);
				checkSelectAll.setEnabled(true);
				checkSelectAll.setValue(false);
			}
		} // bSearch
		
		// Seleccionar todos los elementos de la tabla
		else if(e.getSource() == checkSelectAll) {
			if((Boolean)checkSelectAll.getValue()){
				selectAll(true);
				bGenerate.setEnabled(true);
			}
			else 	selectAll(false);	
		} // checkSelectAll
		else {}

		activarActionListeners();
	}   //  actionPerformed
	
	/** Selecciona todos los elementos de la tabla */
	protected void selectAll(Boolean selected) {
		if(selected){
			for(int j = 0; j < miniTable.getRowCount(); j++){
				IDColumn id = (IDColumn)miniTable.getModel().getValueAt(j, 0);
				id.setSelected(selected);
				miniTable.repaint();
			}
		}// if selected
		else if(!selected){
			for(int j = 0; j < miniTable.getRowCount(); j++){
				IDColumn id = (IDColumn)miniTable.getModel().getValueAt(j, 0);
				id.setSelected(selected);
				miniTable.repaint();
			}
		}// else selected
	} // selectAll	

	// **************** Métodos que deben ser implementados por los hijos
	
	/** Debe sobreescribirse en cualquier clase que instancie a esta
	 * processSelection
	 *  Procesa la seleccion del usuario
	 * */
	public void processSelection () {
		 Category = new Vector<KeyNamePair>();
		 Department = new Vector<KeyNamePair>();
		 Line = new Vector<KeyNamePair>();
		 Section = new Vector<KeyNamePair>();
		 Reference = new Vector<KeyNamePair>();
		 Brand = new Vector<KeyNamePair>();
		 Vendor = new Vector<KeyNamePair>();
		 Partner = new Vector<KeyNamePair>();
		 Product = new Vector<KeyNamePair>();
		 
		miniTable.stopEditor(true);
		int rows = miniTable.getRowCount();
		miniTable.setRowSelectionInterval(0,0);
		
		// Se cuentan las referencias, si no se selecciono alguna se da un mensaje de error
		for (int i = 0; i < rows; i++) {
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
			if (id.isSelected()){
				refSelected++;
			}
		}
		// Si no se ah seleccionado, error
		if(refSelected == 0){
			ADialog.error(m_WindowNo, m_frame, "Para seguir con el siguiente paso debe seleccionar productos/referencias");
		} 
		else {
			/* Se recorren las filas seleccionadas para inicializar los vectores con 
			 * la información de cada uno de ellos */
			for (int i = 0; i < rows; i++) {
				IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
		results = 
			new XX_VME_ProductFilterResults(Category, Department, Line, Section,
					Reference, Brand, Vendor, Partner, Product);	
				// Fila seleccionada para tomar los datos
				if (id.isSelected()){
					/* Se recorren las columnas para setear los valores de acuerdo al
					 * grano de seleción del usuario
					 */
					for(int j = 1; j < miniTable.getColumnCount(); j++){
						if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Category_ID"))){
							Category.add((KeyNamePair)miniTable.getModel().getValueAt(i, j));
							continue;
						}
						if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Department_ID"))){
							Department.add((KeyNamePair)miniTable.getModel().getValueAt(i, j));
							continue;
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Line_ID"))){
							Line.add((KeyNamePair)miniTable.getModel().getValueAt(i, j));
							continue;
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Section_ID"))){
							Section.add((KeyNamePair)miniTable.getModel().getValueAt(i, j));			
							continue;
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_VendorProdRef_ID"))){
							Vendor.add((KeyNamePair)miniTable.getModel().getValueAt(i, j));	
							continue;
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Brand"))){
							Brand.add((KeyNamePair)miniTable.getModel().getValueAt(i, j));				
							continue;
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "C_BPARTNER_ID"))){
							Partner.add((KeyNamePair)miniTable.getModel().getValueAt(i, j));
							continue;
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "M_PRODUCT_ID"))){
							Product.add((KeyNamePair)miniTable.getModel().getValueAt(i, j));
							continue;
						}					
					}// for
				}// if id selected
			} // for minitanble
		} // else refselected
		
	} // Fin processSelection
	
	/**	getSelected
	 * Devuelve las O/C o Productos seleccionados en la minitable */
	protected void getSelected(){
		
		for(int j = 0; j < miniTable.getRowCount() && j < 999; j++){
			if(new Boolean(miniTable.getModel().getValueAt(j, 0).toString())){
				KeyNamePair item = (KeyNamePair)miniTable.getModel().getValueAt(j, 1);
				item.getKey();
			} // if
		} // for
	} // getSelected
	
	/** fillSecondTable
	 * Se encarga de llenar una segunda tabla con el resultado obtenido del filtro
	 * Se sobreescribe de acuerdo a las necesidades del siguiente proceso **/
	protected abstract void fillSecondTable();
	
	public class ComboBoxRenderer extends JComboBox implements TableCellRenderer {
		private static final long serialVersionUID = 1L;
		public ComboBoxRenderer(Vector<KeyNamePair> items) {
            super(items);
        }
    
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } 
            else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
    
            // Select the current value
            setSelectedItem(value);
            return this;
        }
    } // ComboBoxRenderer

	
	public class ComboBoxEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;
		public ComboBoxEditor(Vector<KeyNamePair> items) {
            super(new JComboBox(items));
        }
    } // ComboBoxEditor


	/**
	 * List Selection Listener - get Info and fill xMatchedTo
	 * @param e event
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
	} // valueChanged
	

	/** Método invocado al presionar el botón de limpiar filtro */
	protected void limpiarFiltro() { 
		//desactivarActionListeners();		
		loadBasicInfo();
		//activarActionListeners();
	}
} //XX_ProductFilter