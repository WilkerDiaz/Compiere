package compiere.model.dynamic.forms;

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
import java.math.BigDecimal;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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
import compiere.model.cds.X_XX_VMR_Brand;
import compiere.model.cds.forms.XX_ProductFilter;
import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_XX_VME_Elements;
import compiere.model.dynamic.X_XX_VME_Product;
import compiere.model.dynamic.X_XX_VME_Reference;

public class XX_VME_ModifyRefsForm extends CPanel implements FormPanel,
	ActionListener, ListSelectionListener, TableModelListener  {
	////

	private static final long serialVersionUID = 1L;
	/** Inicializar la forma
	 *  @param WindowNo window
	 *  @param frame frame
	 */ 
	public void init (int WindowNo, FormFrame frame){
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
	
	private Ctx ctx = Env.getCtx();
	/**	Window No */
	private int m_WindowNo = 0;
	/**	FormFrame */
	private FormFrame m_frame;
	/** Format */
	private DecimalFormat m_format = DisplayType.getNumberFormat(DisplayTypeConstants.Amount);
	/** SQL for Query */
	private StringBuilder m_sql;
	/** Number of selected rows */
	private int m_noSelected = 0;
	
	private Vector<String> selected = new Vector<String>();

	/**	Logger */
	private static CLogger log = CLogger.getCLogger(XX_ProductFilter.class);
		
	//PANEL
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private CPanel commandPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private JScrollPane dataPane = new JScrollPane();
	private FlowLayout commandLayout = new FlowLayout();
	
	//BOTONES
	private JButton bGenerate = ConfirmPanel.createProcessButton(true);	
	private CButton bReset = new CButton(Msg.translate(Env.getCtx(), "XX_ClearFilter"));
	private CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "XX_Search"));
	private CButton bDelete = new CButton(Msg.translate(Env.getCtx(), "XX_DeleteElements"));
	
	//CATEGORIA
	private CLabel labelCategory = new CLabel();
	private VComboBox comboCategory = new VComboBox();
	
	//DEPARTAMENTO
	private VCheckBox checkDepartment = new VCheckBox();
	private CLabel labelDepartment = new CLabel();
	private VComboBox comboDepartment = new VComboBox();
	
	//LINEA
	private VCheckBox checkLine = new VCheckBox();
	private CLabel labelLine = new CLabel();
	private VComboBox comboLine = new VComboBox();
	
	//SECCION
	private VCheckBox checkSection = new VCheckBox();
	private CLabel labelSection = new CLabel();
	private VComboBox comboSection = new VComboBox();
	
	//MARCA
	private VCheckBox checkBrand = new VCheckBox();
	private CLabel labelBrand = new CLabel();
	private VComboBox comboBrand = new VComboBox();
	
	//BUSINESS PARTNER
	private VCheckBox checkBPartner = new VCheckBox();
	private CLabel labelBPartner = new CLabel();
	private VComboBox comboBPartner = new VComboBox();
	
	//PRODUCTO
	private VCheckBox checkProduct = new VCheckBox();
	private CLabel labelProduct = new CLabel();
	private VLookup lookupProduct = null;
	
	//REFERENCIA
	private VCheckBox checkReference = new VCheckBox();
	private CLabel labelReference = new CLabel();
	private VComboBox comboReference =  new VComboBox();
	
	//USUARIO
	private CLabel labelUser = new CLabel();
	private VComboBox comboUser = new VComboBox();
	
	//FECHA CREACION
	private CLabel labelCreated = new CLabel();
	private VDate Date = new VDate(); 
	
	//OTROS
	private JLabel dataStatus = new JLabel();
	private int indx = 0;
	
	// Seleccionar todos en la tabla
	private CLabel labelSelectAll = new CLabel("Seleccionar Todos");
	private CCheckBox checkSelectAll = new CCheckBox();

	// Vectores que contiene los resultados del filtro
	private Vector<KeyNamePair> Category = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> Department = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> Line = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> Section = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> Reference = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> Brand = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> Vendor = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> Partner = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> Product = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> User = new Vector<KeyNamePair>();
	private Vector<KeyNamePair> Created = new Vector<KeyNamePair>();
	
	// Columnas (Referencia)
	private ColumnInfo[] columnsRef = new ColumnInfo[] { 
			new ColumnInfo("",".",IDColumn.class, false, true, ""),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"),".", KeyNamePair.class, "."), 
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Section_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Manual"),".", Boolean.class, false, true, ""),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Quantity"),".", BigDecimal.class, false, true, ""),
	};
	
	// Columnas (Producto)
	private ColumnInfo[] columnsProd = new ColumnInfo[] {
			new ColumnInfo("",".",IDColumn.class, false, true, ""),
			new ColumnInfo(Msg.translate(Env.getCtx(), "M_PRODUCT_ID"),".", KeyNamePair.class, "."), 
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Section_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Manual"),".", Boolean.class, false, true, ""),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Quantity"),".", BigDecimal.class, false, true, ""),
	};
	
	// Elemento sobre el cuál se realizan las modificaciones
	private int elementID = Integer.parseInt(Env.getCtx().getContext("#XX_ModifyQtyForm_ID"));

	// La tabla donde se guardarán los datos
	protected MiniTablePreparator tableResults = new MiniTablePreparator();
	
	// Determina si la búsqueda es por producto 
	private boolean product = false;
	
	// Determina si la búsqueda es por referencia
	private boolean reference = false;
	
	// Otros
	private Vector<BigDecimal> quantities = new Vector<BigDecimal>();
	private Vector<BigDecimal> availableQty = new Vector<BigDecimal>();//XX_VME_GeneralFunctions.obtainQtyProduct(ref, manual)
	private CLabel disponQty = new CLabel(Msg.translate(Env.getCtx(), "XX_Available"));
	private CTextField disponible = new CTextField();//new CTextField(availableQty.get(0).toString());
	
	/** Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception {
		bSearch.setPreferredSize(new Dimension(100, 25));
		bReset.setPreferredSize(new Dimension(100, 25));
		m_frame.getRootPane().setDefaultButton(bSearch);
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
		labelUser.setText(Msg.translate(Env.getCtx(), "XX_UserCreated"));
		labelCreated.setText(Msg.translate(Env.getCtx(), "XX_Created"));

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
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboReference,  new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkReference,  new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// USER
		parameterPanel.add(labelUser,  new GridBagConstraints(8, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboUser,  new GridBagConstraints(9, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		// CREATED
		parameterPanel.add(labelCreated,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(Date,  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		// SELECCIONAR TODAS
		parameterPanel.add(labelSelectAll, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkSelectAll, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// BUSCAR
		parameterPanel.add(bSearch, new GridBagConstraints(9, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// RESET
		parameterPanel.add(bReset,    new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// DISPONIBLES
		parameterPanel.add(disponQty,  new GridBagConstraints(8, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(disponible,  new GridBagConstraints(9, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	
		tableResults.setRowHeight(tableResults.getRowHeight() + 2);
		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(tableResults, null);
		dataPane.setPreferredSize(new Dimension(1280, 500));

		//PANEL INFERIOR
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(bGenerate, null);
		commandPanel.add(bDelete, null);
		
		bReset.addActionListener(this);
		bSearch.addActionListener(this);
		bGenerate.addActionListener(this);
		bDelete.addActionListener(this);
	}   //  jbInit
	
	/** Dynamic Init.
	 *  - Load Department Info
	 *  - Load BPartner Info
	 *  - Load Brand Info
	 *  - Init Table
	 */
	private void dynInit() {
		desactivarActionListeners();
		loadBasicInfo();
		activarActionListeners();
	}   // Fin dynInit
	
	/** Activa todos los listeners creados */
	private final void activarActionListeners () {
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
		comboUser.addActionListener(this);
		Date.addActionListener(this);
	}//addActionListeners
	
	/** Deshabilitar Action Listeners */
	private final void desactivarActionListeners () {
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
		comboUser.removeActionListener(this);
		Date.removeActionListener(this);
	} // removeActionListeners
	
	/** loadBasicInfo
	 *  Table initial state 
	 */
	public final void loadBasicInfo() {
		desactivarActionListeners();
		//Restaura los ComboBoxes y CheckBoxes		
		comboCategory.setEnabled(true);
		comboDepartment.setEnabled(true);
		comboLine.setEnabled(true);
		comboSection.setEnabled(true);
		comboBrand.setEnabled(true);
		comboBPartner.setEnabled(true);
		comboReference.setEnabled(true);
		comboUser.setEnabled(true);
		Date.setEnabled(true);
		bGenerate.setEnabled(true);
		bDelete.setEnabled(true);
		comboCategory.removeAllItems();
		comboDepartment.removeAllItems();
		comboLine.removeAllItems();
		comboSection.removeAllItems();
		comboBrand.removeAllItems();
		comboBPartner.removeAllItems();
		comboReference.removeAllItems();
		comboUser.removeAllItems();
		lookupProduct.setValue(null);
		lookupProduct.setEnabled(true);
		checkDepartment.setEnabled(true);
		checkDepartment.setValue(false);
		checkLine.setValue(false);
		checkLine.setEnabled(true);
		checkSection.setValue(false);
		checkSection.setEnabled(true);
		checkBrand.setEnabled(true);
		checkBPartner.setEnabled(true);
		checkProduct.setEnabled(true);
		checkProduct.setValue(false);
		checkReference.setEnabled(true);
		checkReference.setValue(false);
		
		//Setting Default Items
		KeyNamePair loadKNP = new KeyNamePair(0, "");
		comboCategory.addItem(loadKNP);
		comboDepartment.addItem(loadKNP);
		comboLine.addItem(loadKNP);
		comboSection.addItem(loadKNP);
		comboBrand.addItem(loadKNP);
		comboBPartner.addItem(loadKNP);
		comboReference.addItem(loadKNP);
		comboUser.addItem(loadKNP);
		
		// Cantidad disponible para ser asignada
		disponible.setEditable(false);
		disponible.setBackground(Color.lightGray);
		disponible.setPreferredSize(new Dimension(80, 25));
		disponible.setHorizontalAlignment(JTextField.CENTER);
		
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
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllUsers") );
		comboUser.addItem(loadKNP);
		
		// CARGA DE CATEGORIAS
		String sql = " SELECT DISTINCT CAT.XX_VMR_CATEGORY_ID, CAT.VALUE||'-'||CAT.NAME " +
				" FROM XX_VMR_CATEGORY CAT INNER JOIN XX_VME_REFERENCE REF " +
				" ON (CAT.XX_VMR_CATEGORY_ID = REF.XX_VMR_CATEGORY_ID) " +
				" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID +
				" AND CAT.ISACTIVE = 'Y' " +
				" ORDER BY CAT.VALUE||'-'||CAT.NAME";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCategory.addItem(loadKNP);	
			}
			comboCategory.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE DEPARTAMENTOS
		sql = " SELECT DISTINCT DEP.XX_VMR_DEPARTMENT_ID, DEP.VALUE||'-'||DEP.NAME " +
				" FROM XX_VMR_DEPARTMENT DEP INNER JOIN XX_VME_REFERENCE REF " +
				" ON (DEP.XX_VMR_DEPARTMENT_ID = REF.XX_VMR_DEPARTMENT_ID) " +
				" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID + 
				" AND DEP.ISACTIVE = 'Y'  ORDER BY DEP.VALUE||'-'||DEP.NAME";
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);	
			}
			comboDepartment.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE LINEAS
		sql = " SELECT DISTINCT LIN.XX_VMR_LINE_ID, LIN.VALUE||'-'||LIN.NAME " +
				" FROM XX_VMR_LINE LIN INNER JOIN XX_VME_REFERENCE REF " +
				" ON (LIN.XX_VMR_LINE_ID = REF.XX_VMR_LINE_ID) " +
				" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID + 
				" AND LIN.ISACTIVE = 'Y'  ORDER BY LIN.VALUE||'-'||LIN.NAME";
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboLine.addItem(loadKNP);	
			}
			comboLine.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE SECCIONES
		sql = " SELECT DISTINCT SEC.XX_VMR_SECTION_ID, SEC.VALUE||'-'||SEC.NAME " +
				" FROM XX_VMR_SECTION SEC INNER JOIN XX_VME_REFERENCE REF " +
				" ON (SEC.XX_VMR_SECTION_ID = REF.XX_VMR_SECTION_ID) " +
				" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID + 
				" AND SEC.ISACTIVE = 'Y'  ORDER BY SEC.VALUE||'-'||SEC.NAME";
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboSection.addItem(loadKNP);	
			}
			comboSection.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE REFERENCIAS
		sql = " SELECT DISTINCT VR.XX_VMR_VENDORPRODREF_ID, VR.VALUE||'-'||VR.NAME " +
				" FROM XX_VMR_VENDORPRODREF VR INNER JOIN XX_VME_REFERENCE REF " +
				" ON (VR.XX_VMR_VENDORPRODREF_ID = REF.XX_VMR_VENDORPRODREF_ID) " +
				" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID + 
				" AND VR.ISACTIVE = 'Y'  ORDER BY VR.VALUE||'-'||VR.NAME";
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboReference.addItem(loadKNP);	
			}
			comboReference.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE MARCAS
		sql = " SELECT DISTINCT BR.XX_VMR_BRAND_ID, BR.VALUE||'-'||BR.NAME " +
				" FROM XX_VMR_BRAND BR INNER JOIN XX_VME_REFERENCE REF " +
				" ON (BR.XX_VMR_BRAND_ID = REF.XX_VMR_BRAND_ID) " +
				" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID +
				" AND BR.ISACTIVE = 'Y'  ORDER BY BR.VALUE||'-'||BR.NAME";
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBrand.addItem(loadKNP);
			}
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE BPS
		sql = " SELECT DISTINCT BP.C_BPARTNER_ID, BP.NAME " +
				" FROM C_BPARTNER BP INNER JOIN XX_VME_PRODUCT PROD " +
				" ON (BP.C_BPARTNER_ID = PROD.C_BPARTNER_ID) " +
				" INNER JOIN XX_VME_REFERENCE REF " +
				" ON (PROD.XX_VME_REFERENCE_ID = REF.XX_VME_REFERENCE_ID) " +
				" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID +
				" AND BP.ISVENDOR = 'Y' AND BP.XX_ISVALID = 'Y' " +
				" AND BP.ISACTIVE = 'Y'" +
				" ORDER BY BP.NAME ";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE USUARIOS
		//TODO DEFINIR EL SQL CORRECTAMENTE
		sql = " SELECT DISTINCT U.AD_USER_ID, U.NAME " +
				" FROM AD_USER U INNER JOIN XX_VME_ELEMENTS E" +
				" ON(U.AD_USER_ID = E.CREATEDBY) " +
				" WHERE XX_VME_ELEMENTS_ID = " + elementID +
				" ORDER BY U.NAME ";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboUser.addItem(loadKNP);
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		activarActionListeners();
	} // loadBasicInfo
	
	/** loadDepartmentInfo
	 * Cargar información de departamentos
	 * */
	private final void loadDepartmentInfo(){
		comboDepartment.removeAllItems();
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();		
		KeyNamePair loadKNP;
		String sql = "";
				
		if(bpar == null || bpar.getKey() == 0 || bpar.getKey() == 99999999){ 
			comboDepartment.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboDepartment.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
			comboDepartment.addItem(loadKNP);	
						
			sql = " SELECT DEP.XX_VMR_DEPARTMENT_ID, DEP.VALUE||'-'||DEP.NAME " +
					" FROM XX_VMR_DEPARTMENT DEP INNER JOIN XX_VME_REFERENCE REF " +
					" ON (DEP.XX_VMR_DEPARTMENT_ID = REF.XX_VMR_DEPARTMENT_ID) " +
					" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID +
					" AND DEP.ISACTIVE = 'Y'";
			
			// HAY UNA CATEORIA SELECCIONADA
			if(cat!= null && cat.getKey() != 99999999 && cat.getKey()!= 0){
				sql += " AND DEP.XX_VMR_CATEGORY_ID = " + cat.getKey();
			}
			
			sql += " ORDER BY DEP.VALUE||'-'||DEP.NAME";			
		} // IF BPAR 
		else {
				comboDepartment.removeAllItems();
				loadKNP = new KeyNamePair(0,"");
				comboDepartment.addItem(loadKNP);
				loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
				comboDepartment.addItem(loadKNP);	
				sql = " SELECT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||'-'||dp.NAME" 
					+ " FROM XX_VMR_VENDORDEPARTASSOCI ve, XX_VMR_DEPARTMENT dp" 
					+ " WHERE ve.XX_VMR_DEPARTMENT_ID = dp.XX_VMR_DEPARTMENT_ID"
					+ " AND ve.C_BPARTNER_ID = " + bpar.getKey();	
				
				if(cat != null && cat.getKey() != 99999999){
					sql += " AND dp.XX_VMR_CATEGORY_ID = " + cat.getKey();
				}

		} // ELSE BPAR
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);			
			}			
			comboDepartment.setSelectedItem(dept);
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadDepartmentInfo
	
	/** loadLineInfo
	 *  Cargar información de linea
	 */
	private final void loadLineInfo(){
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
		
		sql = " SELECT li.XX_VMR_LINE_ID, li.VALUE||'-'||li.NAME " +
				" FROM XX_VMR_LINE li INNER JOIN XX_VME_REFERENCE REF " +
				" ON (li.XX_VMR_LINE_ID = REF.XX_VMR_LINE_ID) " +
				" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID  +
				" AND li.isactive = 'Y' ";
		if((dpto != null ) && dpto.getKey() != 0 && dpto.getKey() != 99999999){
			sql += " AND li.XX_VMR_DEPARTMENT_ID = " + dpto.getKey();
		}
		
		sql += " ORDER BY li.VALUE ";				
		PreparedStatement pstmt = null; 
		ResultSet rs =  null;
			
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
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadLineInfo
	
	/** loadSectionInfo
	 * Cargar informacion de seccion
	 * */
	private final void loadSectionInfo(){
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

		sql = " SELECT se.XX_VMR_SECTION_ID, se.VALUE||'-'||se.NAME " +
			 " FROM XX_VMR_SECTION se INNER JOIN XX_VME_REFERENCE REF " +
			" ON (se.XX_VMR_SECTION_ID = REF.XX_VMR_SECTION_ID) " +
			" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID  +
			" AND se.IsActive = 'Y' ";
		
		if((line != null ) && line.getKey() != 0 && line.getKey() != 99999999){
			sql += "AND se.XX_VMR_LINE_ID = " + line.getKey();
		}
		PreparedStatement pstmt =  null;
		ResultSet rs =  null;
		
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
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
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadSectionInfo
	
	/** loadReference
	 * Cargar informacion de las referencias de proveedor
	 * */
	private final void loadReference(){
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
		comboReference.setEnabled(true);
		
		sql = " SELECT VR.XX_VMR_VENDORPRODREF_ID, VR.VALUE||'-'||VR.NAME " + 
			" FROM XX_VMR_VENDORPRODREF VR INNER JOIN XX_VME_REFERENCE REF " +
			" ON (VR.XX_VMR_VENDORPRODREF_ID = REF.XX_VMR_VENDORPRODREF_ID) " +
			" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID;
		
		// HAY UN DEPARTAMENTO SELECCIONADO
		if((dept != null) && dept.getKey() != 0 && dept.getKey() != 99999999) {			
			sql += " AND VR.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
		}
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
		//System.out.println(sql);
		PreparedStatement pstmt = null;
		ResultSet rs =  null;
		
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
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadReference
	
	/** loadBPartnerInfo
	 * Cargar informacion de Socio de negocio 
	 * */
	private final void loadBPartnerInfo(){
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
				
			sql = " SELECT BP.C_BPARTNER_ID, BP.NAME " +
				" FROM C_BPARTNER BP INNER JOIN XX_VME_PRODUCT PROD " +
				" ON (BP.C_BPARTNER_ID = PROD.C_BPARTNER_ID) " +
				" INNER JOIN XX_VME_REFERENCE REF " +
				" ON (PROD.XX_VME_REFERENCE_ID = REF.XX_VME_REFERENCE_ID) " +
				" WHERE REF.XX_VME_ELEMENTS_ID = " + elementID +
				" AND BP.ISVENDOR = 'Y' AND BP.XX_ISVALID = 'Y' AND BP.ISACTIVE = 'Y'" + 
				" ORDER BY BP.NAME ";	
		} // IF DEPT 
		else {
			comboBPartner.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboBPartner.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllBusinessPartner"));
			comboBPartner.addItem(loadKNP);	
			sql = " SELECT bp.C_BPARTNER_ID, bp.NAME " + 
				" FROM XX_VMR_VENDORDEPARTASSOCI ve, C_BPARTNER bp " + 
				" WHERE ve.C_BPARTNER_ID = bp.C_BPARTNER_ID " +
				" AND ve.XX_VMR_DEPARTMENT_ID = "+ dept.getKey(); 						
		} // ELSE DEPT	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
			}
			comboBPartner.setSelectedItem(bpar);
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadBPartnerInfo
	
	/** loadUserInfo
	 * Cargar informacion de seccion
	 * */
	private final void loadUserInfo(){
		KeyNamePair loadKNP;
		KeyNamePair user;
		String sql = "";
		
		comboUser.removeAllItems();
		comboUser.setEnabled(true);
		loadKNP = new KeyNamePair(0,"");
		comboUser.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllUsers"));
		comboUser.addItem(loadKNP);	

		//TODO arreglar el SQL correctamente
		comboUser.setEnabled(true);
		sql = " SELECT U.AD_USER_ID, U.NAME " +
			 " FROM AD_USER U INNER JOIN XX_VME_ELEMENTS E " +
			 " ON (E.CREATEDBY = U.AD_USER_ID) " +
			 " WHERE XX_VME_ELEMENTS_ID = " + elementID +
			 " AND U.ISACTIVE = 'Y' ";
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboUser.addItem(user);
			}
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadUserInfo


	/** loadTableInfo
	 *  Busqueda para llenar la TableInfo de acuersdo con las especificaciones 
	 *  del usuario
	 */
	private void loadTableInfo(){
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		tableResults.setRowCount(0);
		tableResults = new MiniTablePreparator();
		tableResults.setRowHeight(tableResults.getRowHeight() + 2 );
		dataPane.getViewport().add(tableResults, null);
		
		tableResults.getSelectionModel().addListSelectionListener(this);
		tableResults.getModel().addTableModelListener(this);
		
		checkSelectAll.setEnabled(true);
		checkSelectAll.setValue(false);
		Timestamp date = (Timestamp)Date.getValue();
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair bran = (KeyNamePair)comboBrand.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();
		KeyNamePair refe = (KeyNamePair)comboReference.getSelectedItem();
		KeyNamePair user = (KeyNamePair)comboUser.getSelectedItem();
		
		String select = "";
		String from = "";
		String where = "";
		String SQLTotal = "";
		
		// Si es producto/referencia lo que se busca
		if((lookupProduct.getValue() != null && refe == null) || 
				(lookupProduct.getValue() != null && refe != null) || 
				((Boolean)checkProduct.getValue() == true  && refe.getKey() == 0) ||
				((Boolean)checkProduct.getValue() == true  && refe.getKey() != 0)) {
			select += " SELECT XX_VME_PRODUCT_ID, " +							//1
					" CAT.VALUE||'-'||CAT.NAME, CAT.XX_VMR_CATEGORY_ID, " +		//2,3
					" DP.VALUE||'-'||DP.NAME, DP.XX_VMR_DEPARTMENT_ID, " +		//4,5
					" li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID, " +			//6,7
					" se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID, " +			//8,9
					" VR.VALUE||'-'||VR.NAME, VR.XX_VMR_VENDORPRODREF_ID, " +	//10,11
					" P.VALUE||'-'||PR.DESCRIPTION, P.M_PRODUCT_ID, " +			//12,13
					" CASE WHEN PR.XX_VME_MANUAL = 'Y' THEN 1 ELSE 0 END, " +	//14
					" PR.XX_VME_INDEPABISQTY ";									//15
			from +=  " FROM  XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R" +
					" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
					" INNER JOIN XX_VME_PRODUCT PR " +
					" ON (R.XX_VME_REFERENCE_ID = PR.XX_VME_REFERENCE_ID) " +
					" INNER JOIN XX_VMR_VENDORPRODREF VR " +
					" ON (VR.XX_VMR_VENDORPRODREF_ID = R.XX_VMR_VENDORPRODREF_ID) " +
					" INNER JOIN M_PRODUCT P  " +
					" ON (P.M_PRODUCT_ID = PR.M_PRODUCT_ID) " +
					" INNER JOIN XX_VMR_DEPARTMENT DP " +
					" ON (DP.XX_VMR_DEPARTMENT_ID = P.XX_VMR_DEPARTMENT_ID) " +
					" INNER JOIN XX_VMR_CATEGORY CAT  " +
					" ON (CAT.XX_VMR_CATEGORY_ID = DP.XX_VMR_CATEGORY_ID)" +
					" INNER JOIN XX_VMR_LINE li  " +
					" ON (LI.XX_VMR_LINE_ID = P.XX_VMR_LINE_ID) " +
					" INNER JOIN XX_VMR_SECTION SE  " +
					" ON (SE.XX_VMR_SECTION_ID = P.XX_VMR_SECTION_ID) " +
					" INNER JOIN XX_VMR_BRAND BRAN " +
					" ON (BRAN.XX_VMR_BRAND_ID = P.XX_VMR_BRAND_ID)"; 
			where += " WHERE P.IsActive = 'Y'" ;
		} //if
		else if((refe != null && lookupProduct.getValue() == null 
				&& (Boolean)checkProduct.getValue() == false) || 
				((Boolean)checkReference.getValue() == true 
				&& lookupProduct.getValue() == null && (Boolean)checkProduct.getValue() == false)){
			select += " SELECT XX_VME_REFERENCE_ID, " +											//1
					" CAT.VALUE||'-'||CAT.NAME, CAT.XX_VMR_CATEGORY_ID, " +						//2,3
					" DP.VALUE||'-'||DP.NAME, DP.XX_VMR_DEPARTMENT_ID, " +						//4,5
					" li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID, " +							//6,7
					" se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID, " +							//8,9
					" VR.VALUE||'-'||VR.NAME||' - '||LC.NAME, VR.XX_VMR_VENDORPRODREF_ID, " +	//10,11
					"CASE WHEN R.XX_VME_MANUAL = 'Y' THEN 1 ELSE 0 END, " +						//12
					" R.XX_VME_INDEPABISQTY ";													//13
			from +=  " FROM  XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R" +
					" ON (E.XX_VME_ELEMENTS_ID= R.XX_VME_ELEMENTS_ID) " +
					" INNER JOIN XX_VMR_VENDORPRODREF VR " +
					" ON (VR.XX_VMR_VENDORPRODREF_ID = R.XX_VMR_VENDORPRODREF_ID) " +
					" INNER JOIN XX_VMR_DEPARTMENT DP " +
					" ON (DP.XX_VMR_DEPARTMENT_ID = VR.XX_VMR_DEPARTMENT_ID) " +
					" INNER JOIN XX_VMR_CATEGORY CAT  " +
					" ON (CAT.XX_VMR_CATEGORY_ID = DP.XX_VMR_CATEGORY_ID)" +
					" INNER JOIN XX_VMR_LINE li  " +
					" ON (LI.XX_VMR_LINE_ID = VR.XX_VMR_LINE_ID) " +
					" INNER JOIN XX_VMR_SECTION SE  " +
					" ON (SE.XX_VMR_SECTION_ID = VR.XX_VMR_SECTION_ID) " +
					" INNER JOIN XX_VMR_BRAND BRAN " +
					" ON (BRAN.XX_VMR_BRAND_ID = VR.XX_VMR_BRAND_ID)" +
					" LEFT OUTER JOIN XX_VMR_LONGCHARACTERISTIC LC" + 
					" ON (VR.XX_VMR_LONGCHARACTERISTIC_ID = LC.XX_VMR_LONGCHARACTERISTIC_ID)"; 
			where += " WHERE VR.IsActive = 'Y'" ;
		} //if

		// Se define el elemento del que se tomarán las referencias/productos
		where += " AND E.XX_VME_ELEMENTS_ID = " + elementID;
		
		// Leer Categoria
		boolean category = false;
		if(cat.getKey()!= 0 ){
			if (cat.getKey()!= 99999999) 
				where += " AND CAT.XX_VMR_CATEGORY_ID = " + cat.getKey();
		} // if categoria
		
		//Leer Departamento
		if(dept!=null){
			if((Boolean)checkDepartment.getValue()==true || dept.getKey()==99999999 ||  dept.getKey()!= 0 ){	
				if(!category){
					//groupBy += ", DP.VALUE||'-'||DP.Name, DP.XX_VMR_DEPARTMENT_ID";
				} // if !category
				
				if (dept.getKey()!= 99999999 ) 
					where += " AND DP.XX_VMR_DEPARTMENT_ID = " + dept.getKey();				
			} // if department		
		} // if dept != null
		
		//Leer Linea
		if((Boolean)checkLine.getValue() == true || line.getKey() == 99999999 || line.getKey()!= 0){
			//groupBy += ", LI.VALUE||'-'||LI.NAME, LI.XX_VMR_LINE_ID";
			if (line.getKey()!= 99999999)
				where += " AND LI.XX_VMR_LINE_ID = " + line.getKey();
		} // if linea
		
		//Leyendo seccion 
		if ((Boolean)checkSection.getValue()==true || sect.getKey() == 99999999 || sect.getKey()!= 0){
			//groupBy += ", SE.VALUE||'-'||SE.NAME, SE.XX_VMR_SECTION_ID";			
			
			if (sect.getKey()!= 99999999 ) 
				where += " AND SE.XX_VMR_SECTION_ID = " + sect.getKey();
		} // if seccion
		
		//Leyendo marca
		if ((Boolean)checkBrand.getValue()== true || bran.getKey() == 99999999 || bran.getKey()!= 0){
			//groupBy += ", BR.VALUE||'-'||BR.Name, BR.XX_VMR_BRAND_ID";			
			
			if (bran.getKey()!= 99999999) 
				where += " AND BRAN.XX_VMR_BRAND_ID = " + bran.getKey();	
		} // if marca
		
		//Leyendo referencia
		if((Boolean)checkReference.getValue() == true  || refe.getKey() == 99999999 || refe.getKey()!= 0 ){
			//groupBy += ", VR.VALUE||'-'||VR.NAME, VR.XX_VMR_VENDORPRODREF_ID";
			
			if (refe.getKey()!= 99999999) 
				where += " AND VR.XX_VMR_VENDORPRODREF_ID = " + refe.getKey();
	
		} //Leyendo Partner
		
		//Leyendo proveedor
		if((Boolean)checkBPartner.getValue() == true || bpar.getKey() == 99999999 || bpar.getKey() != 0){
			//groupBy += ", BP.NAME, BP.C_BPARTNER_ID";			
			
			if (bpar.getKey()!= 99999999) {
				if(product){
					where += " AND P.C_BPARTNER_ID = " + bpar.getKey();	
				}
				else {
					where += " AND VR.C_BPARTNER_ID = " + bpar.getKey();	
				}
			}
		} // if proveedor

		//Leyendo producto
		if((Boolean)checkProduct.getValue() == true || lookupProduct.getValue() != null ){
			//groupBy += ", P.VALUE||'-'||P.NAME, P.M_PRODUCT_ID";
			
			if (lookupProduct.getValue() != null && checkProduct.getValue().equals(false)){
				where += " AND P.M_PRODUCT_ID = " + lookupProduct.getValue();	
			}
		} // if producto
		
		//Leyendo Usuario
		if(user.getKey() != 0){		
			if (bpar.getKey()!= 99999999) 
				where += " AND E.CREATEDBY = " + user.getKey();	

		} // if Usuario
		
		// Leyendo fecha
		if(Date.getValue() != null ){
			if((lookupProduct.getValue() != null && refe == null) || 
					(lookupProduct.getValue() != null && refe != null) || 
					((Boolean)checkProduct.getValue() == true  && refe.getKey() == 0) ||
					((Boolean)checkProduct.getValue() == true  && refe.getKey() != 0)){
				where += " AND TRUNC(PR.CREATED )  = " +DB.TO_DATE(date,true);		
			}
			else {
				where += " AND TRUNC(R.CREATED )  = " +DB.TO_DATE(date,true);
			}
		} // date
		
		SQLTotal = select + from + where;
		//System.out.println("SQLTotal: "+ SQLTotal);
				
		//Preparar la tabla	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if((lookupProduct.getValue() != null && refe == null) || 
				(lookupProduct.getValue() != null && refe != null) || 
				((Boolean)checkProduct.getValue() == true  && refe.getKey() == 0) ||
				((Boolean)checkProduct.getValue() == true  && refe.getKey() != 0)){
			String selectFinal = tableResults.prepareTable(columnsProd, "", "", true, "");
		}
		else if((refe != null && lookupProduct.getValue() == null 
				&& (Boolean)checkProduct.getValue() == false) || 
				((Boolean)checkReference.getValue() == true 
				&& lookupProduct.getValue() == null && (Boolean)checkProduct.getValue() == false)){
			String selectFinal = tableResults.prepareTable(columnsRef, "", "", true, "");
		}
		try {
			pstmt = DB.prepareStatement(SQLTotal, null);
			rs = pstmt.executeQuery();		

			int row = 0;
			int rows = tableResults.getColumnCount();
			while (rs.next()) {
				if((lookupProduct.getValue() != null && refe == null) || 
						(lookupProduct.getValue() != null && refe != null) || 
						((Boolean)checkProduct.getValue() == true  && refe.getKey() == 0) ||
						((Boolean)checkProduct.getValue() == true  && refe.getKey() != 0)){
					tableResults.setRowCount(row + 1);
					IDColumn id = new IDColumn(rs.getInt(1));
					id.setSelected(false);
					tableResults.setValueAt(id, row, 0);												//Identificador
					tableResults.setValueAt(new KeyNamePair(rs.getInt(13), rs.getString(12)), row, 1);	// Producto
					tableResults.setValueAt(new KeyNamePair(rs.getInt(11), rs.getString(10)), row, 2);	// Referenca
					tableResults.setValueAt(new KeyNamePair(rs.getInt(3), rs.getString(2)), row, 3);	// Categoria
					tableResults.setValueAt(new KeyNamePair(rs.getInt(5), rs.getString(4)), row, 4);	// Departamento
					tableResults.setValueAt(new KeyNamePair(rs.getInt(7), rs.getString(6)), row, 5);	// Linea
					tableResults.setValueAt(new KeyNamePair(rs.getInt(9), rs.getString(8)), row, 6);	// Seccion
					tableResults.setValueAt(rs.getBoolean(14), row, 7);									// Manual
					tableResults.setValueAt(rs.getBigDecimal(15), row, 8);								// Cantidad
					row++;
				} // producto
				if((refe != null && lookupProduct.getValue() == null 
						&& (Boolean)checkProduct.getValue() == false) || 
						((Boolean)checkReference.getValue() == true 
						&& lookupProduct.getValue() == null && (Boolean)checkProduct.getValue() == false)) {
					tableResults.setRowCount(row + 1);
					IDColumn id = new IDColumn(rs.getInt(1));
					id.setSelected(false);
					tableResults.setValueAt(id, row, 0);												//Identificador
					tableResults.setValueAt(new KeyNamePair(rs.getInt(11), rs.getString(10)), row, 1);	// Referenca
					tableResults.setValueAt(new KeyNamePair(rs.getInt(3), rs.getString(2)), row, 2);	// Categoria
					tableResults.setValueAt(new KeyNamePair(rs.getInt(5), rs.getString(4)), row, 3);	// Departamento
					tableResults.setValueAt(new KeyNamePair(rs.getInt(7), rs.getString(6)), row, 4);	// Linea
					tableResults.setValueAt(new KeyNamePair(rs.getInt(9), rs.getString(8)), row, 5);	// Seccion
					tableResults.setValueAt(rs.getBoolean(12), row, 6);									// Manual
					tableResults.setValueAt(rs.getBigDecimal(13), row, 7);								// Cantidad
					row++;
				} // referencia
			}
			
			tableResults.autoSize(true);
			tableResults.setMultiSelection(true);
		} // try 
		catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		tableResults.repaint();		
		TableColumn col = tableResults.getColumnModel().getColumn(indx+1);
		tableResults.setMultiSelection(true);
		tableResults.setRowSelectionAllowed(false);
		
		////
		if(product && refe == null ){
			bGenerate.setEnabled(false);
		}
		else if(refe.getKey() != 99999999 && refe.getKey() != 0){
			bGenerate.setEnabled(true);
		}
		
		////
		
		tableResults.autoSize();
		tableResults.setRowHeight(tableResults.getRowHeight() + 2 );
		tableResults.getTableHeader().setReorderingAllowed(false);
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
	
	/** processDelete
	 * Se eliminan las referencias/productos seleccionados por el usuario
	 * */
	private void processDelete(){
		String elemBrand = "";
		String refBrand = "";
		String SQLBrand = "";
		String part1 = "";
		String part2 = "";
		String msj = "";
		boolean delete = false;
		int count = 0;
		int refSelected = 0;
		X_XX_VME_Elements element = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
		tableResults.stopEditor(true);
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		// Se cuentan las referencias, si no se selecciono alguna se da un mensaje de error
		for (int i = 0; i < tableResults.getRowCount(); i++) {
			IDColumn id = (IDColumn)tableResults.getModel().getValueAt(i, 0);
			if (id.isSelected()){
				refSelected++;
			}
		}
		if(refSelected == 0){
			ADialog.error(m_WindowNo, m_frame, "Para eliminar debe seleccionar productos/referencias");
		}
		else {
			msj = Msg.getMsg(Env.getCtx(), "XX_DeleteElement");
			delete = ADialog.ask(m_WindowNo, m_frame, msj);	
		}
		
		// El usuario ha decidido eliminar lo soleccionado
		if (delete) {

			// Se recorren las filas seleccionadas para eliminarlas 
			for (int i = 0; i < tableResults.getRowCount(); i++) {
				IDColumn id = (IDColumn)tableResults.getModel().getValueAt(i, 0);
				
				// Fila seleccionada para tomar los datos
				if (id.isSelected()){
					
					// Eliminando productos
					if(product){
						X_XX_VME_Product product = 
							new X_XX_VME_Product(Env.getCtx(), id.getRecord_ID(),null);
						X_XX_VME_Reference reference = 
							new X_XX_VME_Reference(Env.getCtx(), product.getXX_VME_Reference_ID(),null);
						elementID = reference.getXX_VME_Elements_ID();
						product.delete(true);
					} // product
					else {
						X_XX_VME_Reference reference = 
							new X_XX_VME_Reference(Env.getCtx(), id.getRecord_ID(),null);
						
						// Eliminar la marca relacionada con la referencia a ser eliminada
						/*SQLBrand = " SELECT COUNT(XX_VMR_BRAND_ID) COUNTER" +
								" FROM XX_VME_REFERENCE " +
								" WHERE XX_VMR_BRAND_ID = " + reference.getXX_VMR_Brand_ID() +
								" AND XX_VME_ELEMENTS_ID = " + element.get_ID();
						PreparedStatement pstmt = null;
						ResultSet rs = null;
						try {
							pstmt = DB.prepareStatement(SQLBrand, null);
							rs = pstmt.executeQuery();	
							while(rs.next()){
								count = rs.getInt("COUNTER");
							}
						} // try 
						catch (SQLException e) {
							e.printStackTrace();
							ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
						}
						finally{
							DB.closeResultSet(rs);
							DB.closeStatement(pstmt);
						}
						*/
						/*elemBrand = element.getXX_VME_RelBrand();
						X_XX_VMR_Brand brand = new X_XX_VMR_Brand(Env.getCtx(), reference.getXX_VMR_Brand_ID(), null);
						refBrand = brand.getName();
						
						if(count < 2) {
							int index = elemBrand.indexOf(refBrand,0);
							if(elemBrand.equals(refBrand)){
								element.setXX_VME_RelBrand("N");
							} 
							else{
								part1 = (String)elemBrand.subSequence(0, index);
								if((index + refBrand.length() + 2) > elemBrand.length()){
									part2 = (String)elemBrand.subSequence(index+refBrand.length(),elemBrand.length());
								}
								else {
									part2 = (String)elemBrand.subSequence(index+refBrand.length() + 2,elemBrand.length());	
								}
								element.setXX_VME_RelBrand(part1+part2);	
							} // elembrand
						}
						
						if(element.getXX_VME_RelBrand().equals("")){
							element.setXX_VME_RelBrand("N");
						}
						
						element.save();*/
						reference.delete(true);
						
						
						
						// Se setea la cantidad de referencias asociadas al elemento
						int resta = element.getXX_VME_QtyRefAssociated().intValue()-1;
						element.setXX_VME_QtyRefAssociated(new BigDecimal(resta));
						element.save();
					} // reference
				} // if selected
			} // for filas
			
			//ACTUALIZAR MARCAS DEL ELEMENTO
			SQLBrand = " SELECT distinct NAME " +
					" FROM XX_VMR_BRAND " +
					" WHERE XX_VMR_BRAND_ID IN (" +
					" 	SELECT XX_VMR_BRAND_ID " +
					"	FROM XX_VMR_VENDORPRODREF " +
					"	WHERE XX_VMR_VENDORPRODREF_ID in ( select XX_VMR_VendorProdRef_ID from xx_vme_reference where xx_vme_elements_id="+element.get_ID()+"))";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String brands="";
			try {
				pstmt = DB.prepareStatement(SQLBrand, null);
				rs = pstmt.executeQuery();	
				while(rs.next()){
					if(!brands.equalsIgnoreCase("")){
						brands+=", ";
					}
					brands += rs.getString("NAME");
				}
			} // try 
			catch (SQLException e) {
				e.printStackTrace();
				ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			if(brands.equalsIgnoreCase("")){
				element.setXX_VME_RelBrand("N");
			} else{
				element.setXX_VME_RelBrand(brands);
			}
			element.save();
			System.out.println(element.getXX_VME_QtyRefAssociated());
			
			// Se redefinen las cantidades una vez eliminadas las filas
			if((element.getXX_VME_QtyRefAssociated()).compareTo(new BigDecimal(0)) == 1){
				XX_VME_GeneralFunctions.redefineQuantities(element);
			}
		} // delete
		
		m_frame.setCursor(Cursor.getDefaultCursor());
		// Ventana de salida
		String msjDeleted = Msg.getMsg(Env.getCtx(), "XX_DeletedElement");
		ADialog.info(m_WindowNo, m_frame, msjDeleted);
		dispose();
		
	} // Fin processDelete
	
	/** processSelection
	 *  Configura las cantidades de acuerdo a lo definido por el usuario
	 * */
	public void processSelection () {
		int refSelected = 0;
		Integer elementID = new Integer(0);
		tableResults.stopEditor(true);
		X_XX_VME_Product producto = null;
		X_XX_VME_Reference reference = null;
		Vector<X_XX_VME_Reference> references = new Vector<X_XX_VME_Reference>();  
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		
		/* Se recorren las filas seleccionadas para inicializar los vectores con 
		 * la información de cada uno de ellos */
		for (int i = 0; i < tableResults.getRowCount(); i++) {
			IDColumn id = (IDColumn)tableResults.getModel().getValueAt(i, 0);
			
			// Fila seleccionada para tomar los datos
			if(product){
				producto = new X_XX_VME_Product(Env.getCtx(), id.getRecord_ID(),null);
				producto.setXX_VME_Manual((Boolean)tableResults.getModel().getValueAt(i, 7));
				if((Boolean)tableResults.getModel().getValueAt(i, 7)){
					producto.setXX_VME_IndepabisQty((BigDecimal)tableResults.getModel().getValueAt(i, 8));
					reference = new X_XX_VME_Reference(Env.getCtx(), producto.getXX_VME_Reference_ID(),null);
					if(!references.contains(reference)){
						references.add(reference);
					}
					elementID = reference.getXX_VME_Elements_ID();
				}
				producto.save();
			} // product
			else {
				reference = new X_XX_VME_Reference(Env.getCtx(), id.getRecord_ID(),null);
				reference.setXX_VME_Manual((Boolean)tableResults.getModel().getValueAt(i, 6));
				if((Boolean)tableResults.getModel().getValueAt(i, 6)){
					reference.setXX_VME_IndepabisQty((BigDecimal)tableResults.getModel().getValueAt(i, 7));
					XX_VME_GeneralFunctions.redefineQtyProd(reference.get_ID(), 
							(BigDecimal)tableResults.getModel().getValueAt(i, 7),
							(Boolean)tableResults.getModel().getValueAt(i, 6));
				}
				reference.save();
				elementID = reference.getXX_VME_Elements_ID();
			} // reference
			
		} // for minitanble
		
		X_XX_VME_Elements elemento = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
		elemento.setXX_VME_Validated(false);
		elemento.save();
		
		// Se redefinen las cantidades una vez eliminadas las filas dependiendo si 
		// las cantidades modificadas se correspondían con productos o referencias
		if(product){
			for(int j = 0; j < references.size(); j++){
				XX_VME_GeneralFunctions.redefineQtyProd(references.get(j).get_ID(), 
						references.get(j).getXX_VME_IndepabisQty(),
						references.get(j).isXX_VME_Manual());	
			}
		}
		else {
			XX_VME_GeneralFunctions.redefineQuantities(elemento);	
		}
		m_frame.setCursor(Cursor.getDefaultCursor());
		// Ventana de salida
		String msjModified = Msg.getMsg(Env.getCtx(), "XX_ModifiedElement");
		ADialog.info(m_WindowNo, m_frame, msjModified);
		dispose();
	} // Fin processSelection
	
	/** selectAll
	 * Selecciona/Deselecciona todos los elementos de la tabla segúna opción
	 * del usuario
	 * @param selected Opción del usuario
	 * */
	private void selectAll(Boolean selected) {
		if(selected){
			for(int j = 0; j < tableResults.getRowCount(); j++){
				IDColumn id = (IDColumn)tableResults.getModel().getValueAt(j, 0);
				id.setSelected(selected);	
			}
		}// if selected
		else if(!selected){
			for(int j = 0; j < tableResults.getRowCount(); j++){
				IDColumn id = (IDColumn)tableResults.getModel().getValueAt(j, 0);
				id.setSelected(selected);	
			}
		}// else selected
		tableResults.repaint();
	} // selectAll	
	
	/**
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e) {
		desactivarActionListeners();
		// Category Combo 
		if (e.getSource() == comboCategory){
			if(comboCategory.getValue() != null){
				if(!comboCategory.getValue().equals(Integer.valueOf(0))){
					comboDepartment.removeActionListener(this);
					loadDepartmentInfo();
					comboDepartment.addActionListener(this);
				}
			}
		}//Category Combo 
		
		// Department combo 
		else if (e.getSource() == comboDepartment){			
			if(comboDepartment.getValue() != null){
				if(!comboDepartment.getValue().equals(Integer.valueOf(0))) {
					loadReference();
					loadLineInfo();
					loadBPartnerInfo();
				}
			}// dep null
		}// Department combo
		
		// Department CheckBox  
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
				loadLineInfo();
				loadSectionInfo();
				loadBPartnerInfo();
			}

		} //Department CheckBox 
		
		// Line ComboBox 
		else if (e.getSource() == comboLine ){
			if(comboLine.getValue() != null){
				if(!comboLine.getValue().equals(Integer.valueOf(0))) {
					loadSectionInfo();
					loadReference();
				}
			}// lin null
		}
		
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
		
		// Section combo
		else if (e.getSource() == comboSection ){
			if(comboSection.getValue() != null){
				if(!comboSection.getValue().equals(Integer.valueOf(0))) {
					loadReference();
				}
			}// sec null
		}// Section combo
		
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
					loadReference();	
				}
			}
		} // comboBox brand
		
		//Brand CheckBox
		else if(e.getSource() == checkBrand){
			if((Boolean)checkBrand.getValue() == true){
				comboBrand.setValue(99999999);
				comboBrand.setEnabled(false);
			}
			else{
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
		
		//Product CheckBox
		else if(e.getSource() == lookupProduct){
			if(lookupProduct.getValue() != new Integer(0)){
				product = true;
			}
		} // lookproduct
		
		//Product CheckBox
		else if(e.getSource() == checkProduct){
			if((Boolean) checkProduct.getValue() == true){
				lookupProduct.setEnabled(false);
				product = true;
				if((Boolean)checkReference.getValue() == true){
					ADialog.error(m_WindowNo, this, Msg.getMsg(Env.getCtx(), "XX_ProdRef"));
					bGenerate.setEnabled(false);
				}
				KeyNamePair refe = (KeyNamePair)comboReference.getSelectedItem();
				if(refe.getKey() == 0 || refe.getKey() == 99999999) {
					bGenerate.setEnabled(false);
				}
				else {
					bGenerate.setEnabled(true);
				}
			} // checkProduct
			else{
				lookupProduct.setValue(null);
				lookupProduct.setEnabled(true);
			}
		} //Product CheckBox
		
		// Combobox Reference
		else if(e.getSource() == comboReference) {
			comboReference.removeActionListener(this);
			comboReference.addActionListener(this);
			reference = true;
		}
		
		//Reference CheckBox
		else if(e.getSource() == checkReference){
			if((Boolean)checkReference.getValue() == true){
				comboReference.setValue(99999999);
				comboReference.setEnabled(false);
				reference = true;
				if((Boolean)checkProduct.getValue() == true){
					ADialog.error(m_WindowNo, this, Msg.getMsg(Env.getCtx(), "XX_ProdRef"));
				}
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
		
		//Delete Selection
		else if (e.getSource() == bDelete) {
			processDelete();
		} // bGenerate

		//Execute Search
		else if(e.getSource() == bSearch) {
			try {
				loadTableInfo();
				disponible.setText("-");
			} 
			catch (NullPointerException n) {
				n.printStackTrace();
			}
			if(tableResults.getRowCount()>0){
				labelSelectAll.setVisible(true);
				checkSelectAll.setEnabled(true);
				checkSelectAll.setValue(false);
			}
			else{
				labelSelectAll.setVisible(false);
				checkSelectAll.setVisible(false);
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
	private void limpiarFiltro() { 
		//desactivarActionListeners();
		Date.setValue(null);
		loadBasicInfo();
		//activarActionListeners();
	}

	/**
	*  Table Model Listener - calculate matched Qty
	*  @param e event
	*/
	@Override
	public void tableChanged (TableModelEvent e){
		KeyNamePair refe = null;
		BigDecimal suma = new BigDecimal(0);
		BigDecimal resta = new BigDecimal(0);
		BigDecimal qtyRef = new BigDecimal(0);
		BigDecimal qtyTotal = new BigDecimal(0);
		Integer id = new Integer(0);
		X_XX_VME_Elements elemento = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
		
		// Se toman los cambios que ocurren en la tabla por parte del usuario
		// referente a las cantidades
		if(tableResults.isEditing()){
			if(tableResults.getRowCount() > 0){
				// Si es por producto
				if(product){
					if(e.getColumn() == 8){
						// Se coloca manual en verdadero
						//miniTable2.setValueAt(true, e.getFirstRow(), 5);
						tableResults.setValueAt(true, e.getFirstRow(), 
								tableResults.getEditingColumn()-1);
					}// if Column
					// Cantidad editada por el usuario
					qtyRef = (BigDecimal)tableResults.getValueAt(tableResults.getEditingRow(),8);

					// Se obtiene la referencia asociada
					refe = (KeyNamePair)tableResults.getModel().
									getValueAt(tableResults.getEditingRow(), 2);
					int referID = XX_VME_GeneralFunctions.obtainReference(refe.getKey(), elemento);
					// Se calcula la cantidad disponible para la referencia
					qtyTotal = XX_VME_GeneralFunctions.getQtyRefAvailable(referID);//, false).get(1);
					disponible.setText(qtyTotal.toString());
					if(tableResults.getRowCount() > 0){
						// Se recorren las filas para obtener las cantidades
						for (int i = 0; i < tableResults.getRowCount(); i++) {
							if ( tableResults.getValueAt(i, 8) != null && 
									((Boolean)tableResults.getModel().getValueAt(i, 7) == true)){
								suma = suma.add((BigDecimal)tableResults.getValueAt(i, 8));
							}
						} // for suma
						resta = qtyTotal.subtract(suma);
					} // if rowcount
				}// product
				
				// Si es por referencia
				else {
					if(e.getColumn() == 7){		
						// Cantidad editada por el usuario
						qtyRef = (BigDecimal)tableResults.getValueAt(tableResults.getEditingRow(),7);
						// Se calcula la cantidad disponible para la referencia
						qtyTotal = elemento.getXX_VME_QTYPUBLISHED(); 
						for (int i = 0; i < tableResults.getRowCount(); i++) {
							if ( tableResults.getValueAt(i, 7) != null && 
									((Boolean)tableResults.getModel().getValueAt(i, 6) == true)){
								suma = suma.add((BigDecimal)tableResults.getValueAt(i, 7));
							}
						} // for suma
						disponible.setText((XX_VME_GeneralFunctions.getQtyAvailable(elemento).get(0)).toString());
						resta = qtyTotal.subtract(suma);
						// Se coloca manual en verdadero
						tableResults.setValueAt(true, tableResults.getEditingRow(), 
								tableResults.getEditingColumn()-1);
					}// if Column
				} // reference
				
				// Si las nuevas cantidades se corresponden con la del elemento, se
				// habilita el boton de calcular, de lo contrario se deshabilita
				if (resta.compareTo(new BigDecimal(0)) == 0 ){
					disponible.setValue(0);
					disponible.setBackground(Color.GREEN);
					bGenerate.setEnabled(true);
				}//If Suma
				else if(resta.compareTo(new BigDecimal(0)) < 0){
					disponible.setValue(resta);
					disponible.setBackground(Color.RED);
					bGenerate.setEnabled(false);
				}
				else {
					disponible.setValue(resta);
					disponible.setBackground(Color.GREEN);
					bGenerate.setEnabled(true);
				}
			} // rowcount
		} // if editing
	}   //  tableChanged
	
} // Fin XX_VME_ModifyReferencesForm
