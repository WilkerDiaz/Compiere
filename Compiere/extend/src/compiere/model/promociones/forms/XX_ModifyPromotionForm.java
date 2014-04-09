package compiere.model.promociones.forms;

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
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
import compiere.model.cds.forms.XX_ProductFilter;
import compiere.model.promociones.X_XX_VMR_DetailPromotionExt;

public class XX_ModifyPromotionForm extends CPanel implements FormPanel,
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
	
	//Cambiar entre busqueda por condicion y por promocion
	private CLabel labelCondition = new CLabel();
	private VCheckBox checkCondition = new VCheckBox();

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
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Section_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"),".", KeyNamePair.class, "."), 
			new ColumnInfo(Msg.translate(Env.getCtx(), "M_PRODUCT_ID"),".", KeyNamePair.class, "."), 
			//new ColumnInfo(Msg.translate(Env.getCtx(), "M_PRODUCT_ID"),".", Boolean.class, false, true, ""),
			//new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Quantity"),".", BigDecimal.class, false, true, ""),
	};

	// Columnas (Producto)
	private ColumnInfo[] columnsProd = new ColumnInfo[] {
			new ColumnInfo("",".",IDColumn.class, false, true, ""),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Section_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "M_PRODUCT_ID"),".", KeyNamePair.class, "."), 
			new ColumnInfo("Precio",".", BigDecimal.class, false, true, ""),
			//JESSI BUSCAR CONTEXTO
			new ColumnInfo("Porcentaje",".", BigDecimal.class, false, true, ""),
			new ColumnInfo("",".", Boolean.class, true, true, "check"),
	};
	
	// Elemento sobre el cuál se realizan las modificaciones
	 private int promotionID = Integer.parseInt(Env.getCtx().getContext("#XX_VMR_Promotion_ID"));
	 private int conditionID = Integer.parseInt(Env.getCtx().getContext("#XX_VMR_Condition_ID"));
	 private int typePromotion  = Integer.parseInt(Env.getCtx().getContext("#XX_TypePromotion"));
	 private boolean pricePercent = false;
	
	// La tabla donde se guardarán los datos
	protected MiniTablePreparator tableResults = new MiniTablePreparator();
	
	// Determina si la búsqueda es por producto 
	private boolean product = false;
	
	// Determina si la búsqueda es por referencia
	private boolean reference = false;
	
	// Otros
	private CLabel disponQty = new CLabel("Resultados");
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
		
		labelCondition.setText("Filtro por condición");

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
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//Condicion/Promocion
		parameterPanel.add(labelCondition, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkCondition, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// BUSCAR
		parameterPanel.add(bSearch, new GridBagConstraints(9, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// RESET
		parameterPanel.add(bReset,    new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// DISPONIBLES
		parameterPanel.add(disponQty,  new GridBagConstraints(8, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(disponible,  new GridBagConstraints(9, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	
		tableResults.setRowHeight(tableResults.getRowHeight() + 2);
		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(tableResults, null);
		dataPane.setPreferredSize(new Dimension(1280, 200));

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
		checkCondition.addActionListener(this);
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
		checkCondition.removeActionListener(this);
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
		checkCondition.setEnabled(true);
		checkCondition.setValue(true);
		
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
		String sql = "SELECT DISTINCT CAT.XX_VMR_CATEGORY_ID, CAT.VALUE||'-'||CAT.NAME " +
				" FROM XX_VMR_CATEGORY CAT INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
				" ON (CAT.XX_VMR_CATEGORY_ID = PROM.XX_VMR_CATEGORY_ID) " +
				" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID ;
			if(checkCondition.isSelected()){
				sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
			}
			sql += " AND CAT.ISACTIVE = 'Y' ORDER BY CAT.VALUE||'-'||CAT.NAME";
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
		sql = " SELECT DISTINCT DEP.XX_VMR_DEPARTMENT_ID, DEP.VALUE||'-'||DEP.NAME " +
				" FROM XX_VMR_DEPARTMENT DEP INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
				" ON (DEP.XX_VMR_DEPARTMENT_ID = PROM.XX_VMR_DEPARTMENT_ID) " +
				" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID ;				
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		sql += " AND DEP.ISACTIVE = 'Y'  ORDER BY DEP.VALUE||'-'||DEP.NAME";
		
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
		
		// CARGA DE LINEAS
		sql = " SELECT DISTINCT LIN.XX_VMR_LINE_ID, LIN.VALUE||'-'||LIN.NAME " +
				" FROM XX_VMR_LINE LIN INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
				" ON (LIN.XX_VMR_LINE_ID = PROM.XX_VMR_LINE_ID) " +
				" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID  ;
				
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		sql += " AND LIN.ISACTIVE = 'Y'  ORDER BY LIN.VALUE||'-'||LIN.NAME";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboLine.addItem(loadKNP);	
			}
			rs.close();
			pstmt.close();
			comboLine.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		
		// CARGA DE SECCIONES
		sql = " SELECT DISTINCT SEC.XX_VMR_SECTION_ID, SEC.VALUE||'-'||SEC.NAME " +
				" FROM XX_VMR_SECTION SEC INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
				" ON (SEC.XX_VMR_SECTION_ID = PROM.XX_VMR_SECTION_ID) " +
				" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID ;
				
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		sql += " AND SEC.ISACTIVE = 'Y'  ORDER BY SEC.VALUE||'-'||SEC.NAME";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboSection.addItem(loadKNP);	
			}
			rs.close();
			pstmt.close();
			comboSection.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		
		// CARGA DE REFERENCIAS
		sql = " SELECT DISTINCT VR.XX_VMR_VENDORPRODREF_ID, VR.VALUE||'-'||VR.NAME " +
				" FROM XX_VMR_VENDORPRODREF VR INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
				" ON (VR.XX_VMR_VENDORPRODREF_ID = PROM.XX_VMR_VENDORPRODREF_ID) " +
				" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID ;		
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		sql += " AND VR.ISACTIVE = 'Y'  ORDER BY VR.VALUE||'-'||VR.NAME";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboReference.addItem(loadKNP);	
			}
			rs.close();
			pstmt.close();
			comboReference.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		
		// CARGA DE MARCAS
		sql = " SELECT DISTINCT BR.XX_VMR_BRAND_ID, BR.VALUE||'-'||BR.NAME " +
				" FROM XX_VMR_BRAND BR INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
				" ON (BR.XX_VMR_BRAND_ID = PROM.XX_VMR_BRAND_ID) " +
				" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID;
				
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		sql += " AND BR.ISACTIVE = 'Y'  ORDER BY BR.VALUE||'-'||BR.NAME";
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
		sql = " SELECT DISTINCT BP.C_BPARTNER_ID, BP.NAME " +
				" FROM C_BPARTNER BP INNER JOIN M_PRODUCT PROD " +
				" ON (BP.C_BPARTNER_ID = PROD.C_BPARTNER_ID) " +
				" INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
				" ON (PROD.M_PRODUCT_ID = PROM.M_PRODUCT_ID) " +
				" WHERE PROM.XX_VMR_PROMOTION_ID  = " + promotionID ;
				
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		sql += " AND BP.ISVENDOR = 'Y' AND BP.XX_ISVALID = 'Y' " +
				" AND BP.ISACTIVE = 'Y'" +
				" ORDER BY BP.NAME ";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}	
		
		// CARGA DE USUARIOS
		//TODO DEFINIR EL SQL CORRECTAMENTE
		sql = " SELECT DISTINCT U.AD_USER_ID, U.NAME " +
				" FROM AD_USER U INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM" +
				" ON(U.AD_USER_ID = PROM.CREATEDBY) " +
				" WHERE XX_VMR_PROMOTION_ID = " + promotionID ;
				
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		sql += " ORDER BY U.NAME ";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboUser.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
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
			loadKNP = new KeyNamePair(0,"");
			comboDepartment.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
			comboDepartment.addItem(loadKNP);	
			
			sql = " SELECT DISTINCT DEP.XX_VMR_DEPARTMENT_ID, DEP.VALUE||'-'||DEP.NAME " +
					" FROM XX_VMR_DEPARTMENT DEP INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
					" ON (DEP.XX_VMR_DEPARTMENT_ID = PROM.XX_VMR_DEPARTMENT_ID) " +
					" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID ;
					
			if(checkCondition.isSelected()){
				sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
			}
			sql += " AND DEP.ISACTIVE = 'Y'";
			// HAY UNA CATEGORIA SELECCIONADA
			if(cat!= null && cat.getKey() != 99999999 && cat.getKey()!= 0){
				sql += " AND DEP.XX_VMR_CATEGORY_ID = " + cat.getKey();
			}
			
			sql += " ORDER BY DEP.VALUE||'-'||DEP.NAME";			
		} // IF BPAR 
		else {
				loadKNP = new KeyNamePair(0,"");
				comboDepartment.addItem(loadKNP);
				loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
				comboDepartment.addItem(loadKNP);	
				sql = " SELECT DISTINCT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||'-'||dp.NAME" 
					+ " FROM XX_VMR_VENDORDEPARTASSOCI ve, XX_VMR_DEPARTMENT dp" 
					+ " WHERE ve.XX_VMR_DEPARTMENT_ID = dp.XX_VMR_DEPARTMENT_ID"
					+ " AND ve.C_BPARTNER_ID = " + bpar.getKey();	
				
				if(cat != null && cat.getKey() != 99999999){
					sql += " AND dp.XX_VMR_CATEGORY_ID = " + cat.getKey();
				}

		} // ELSE BPAR
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
			comboDepartment.setSelectedItem(dept);
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}		
	} // loadDepartmentInfo
	
	/** loadLineInfo
	 *  Cargar información de linea
	 */
	private final void loadLineInfo(){
		KeyNamePair dpto = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair loadKNP;
		KeyNamePair line;
		String sql = "";
		if(dpto == null && cat == null)
			return;
		
		comboLine.removeAllItems();
		comboLine.setEnabled(true);
		checkLine.setEnabled(true);
		loadKNP = new KeyNamePair(0,"");
		comboLine.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllLines"));
		comboLine.addItem(loadKNP);
		
		sql = " SELECT DISTINCT li.XX_VMR_LINE_ID, li.VALUE||'-'||li.NAME " +
				" FROM XX_VMR_LINE li INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
				" ON (li.XX_VMR_LINE_ID = PROM.XX_VMR_LINE_ID) " +
				" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID ;
				
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		sql += " AND li.isactive = 'Y' ";
		if((dpto != null ) && dpto.getKey() != 0 && dpto.getKey() != 99999999){
			sql += " AND li.XX_VMR_DEPARTMENT_ID = " + dpto.getKey();
		}
		// HAY UNA CATEGORIA SELECCIONADA
		if(cat!= null && cat.getKey() != 99999999 && cat.getKey()!= 0){
			sql += " AND PROM.XX_VMR_CATEGORY_ID = " + cat.getKey();
		}
		sql += " ORDER BY li.VALUE||'-'||li.NAME  ";				
							
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				line = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboLine.addItem(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e){
			log.log(Level.SEVERE, sql, e);
		}
	} // loadLineInfo
	
	/** loadSectionInfo
	 * Cargar informacion de seccion
	 * */
	private final void loadSectionInfo(){
		KeyNamePair dpto = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair loadKNP;
		KeyNamePair sect;
		String sql = "";
		
		if(dpto == null && line == null && cat == null )
			return;
		
		comboSection.removeAllItems();
		checkSection.setEnabled(true);
		comboSection.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboSection.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllSections"));
		comboSection.addItem(loadKNP);	

		sql = " SELECT DISTINCT se.XX_VMR_SECTION_ID, se.VALUE||'-'||se.NAME " +
			 " FROM XX_VMR_SECTION se INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
			" ON (se.XX_VMR_SECTION_ID = PROM.XX_VMR_SECTION_ID) " +
			" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID  ;
			
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		sql += " AND se.IsActive = 'Y' ";
		if((line != null ) && line.getKey() != 0 && line.getKey() != 99999999){
			sql += "AND se.XX_VMR_LINE_ID = " + line.getKey();
		}	
		// HAY UN DPTo SELECCIONADA
		if(dpto!= null && dpto.getKey() != 99999999 && dpto.getKey()!= 0){
			sql += " AND PROM.XX_VMR_DEPARTMENT_ID = " + dpto.getKey();
		}
		// HAY UNA CATEGORIA SELECCIONADA
		if(cat!= null && cat.getKey() != 99999999 && cat.getKey()!= 0){
			sql += " AND PROM.XX_VMR_CATEGORY_ID = " + cat.getKey();
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
			" FROM XX_VMR_VENDORPRODREF VR INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
			" ON (VR.XX_VMR_VENDORPRODREF_ID = PROM.XX_VMR_VENDORPRODREF_ID) " +
			" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID;
		if(checkCondition.isSelected()){
			sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
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
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				reference = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboReference.addItem(reference);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
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
				" FROM C_BPARTNER BP INNER JOIN M_PRODUCT PROD " +
				" ON (BP.C_BPARTNER_ID = PROD.C_BPARTNER_ID) " +
				" INNER JOIN XX_VMR_DETAILPROMOTIONEXT PROM " +
				" ON (PROD.M_PRODUCT_ID = PROM.M_PRODUCT_ID) " +
				" WHERE PROM.XX_VMR_PROMOTION_ID = " + promotionID;
				
			if(checkCondition.isSelected()){
				sql += " AND PROM.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
			}
			sql += " AND BP.ISVENDOR = 'Y' AND BP.XX_ISVALID = 'Y' AND BP.ISACTIVE = 'Y'" + 
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
			 " WHERE XX_VME_ELEMENTS_ID = " + promotionID +
			 " AND U.ISACTIVE = 'Y' ";
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboUser.addItem(user);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
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
		String SQLCount = "";
		String select2 = "";
		
		// Si es producto/referencia lo que se busca
		/*if((lookupProduct.getValue() != null && refe == null) || 
				(lookupProduct.getValue() != null && refe != null) || 
				((Boolean)checkProduct.getValue() == true  && refe.getKey() == 0) ||
				((Boolean)checkProduct.getValue() == true  && refe.getKey() != 0))*/
		select2 += "SELECT COUNT(*) ";
		if(typePromotion==1001100){
			
			select += "SELECT CAT.VALUE||'-'||CAT.NAME, CAT.XX_VMR_CATEGORY_ID, "+
					"DP.VALUE||'-'||DP.NAME, DP.XX_VMR_DEPARTMENT_ID, "+
					"li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID, "+
					"se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID, " +
					"VR.VALUE||'-'||VR.NAME, VR.XX_VMR_VENDORPRODREF_ID, " +
					"PR.VALUE, PR.M_PRODUCT_ID, DPE.XX_DISCOUNTAMOUNT, DPE.XX_DISCOUNTRATE, "+
					"DPE.XX_VMR_DETAILPROMOTIONEXT_ID "
			/*select += //" SELECT M_PRODUCT_ID, " +
					" CAT.VALUE||'-'||CAT.NAME, CAT.XX_VMR_CATEGORY_ID, " +
					" DP.VALUE||'-'||DP.NAME, DP.XX_VMR_DEPARTMENT_ID " 
					" li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID, " +
					" se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID, " +
					" VR.VALUE||'-'||VR.NAME, VR.XX_VMR_VENDORPRODREF_ID, " +
					" P.VALUE||'-'||PR.DESCRIPTION, P.M_PRODUCT_ID, " +
					" CASE WHEN PR.PROCESSING = 'Y' THEN 1 ELSE 0 END, " +
					" R.XX_QUANTITYPURCHASE "*/;
			from +=  " FROM  XX_VMR_PROMOTION PROMO INNER JOIN XX_VMR_DETAILPROMOTIONEXT DPE" +
					" ON (PROMO.XX_VMR_PROMOTION_ID = DPE.XX_VMR_PROMOTION_ID) " +
					" FULL OUTER JOIN XX_VMR_VENDORPRODREF VR " +
					" ON (VR.XX_VMR_VENDORPRODREF_ID = DPE.XX_VMR_VENDORPRODREF_ID) " +
					" FULL OUTER JOIN XX_VMR_DEPARTMENT DP " +
					" ON (DP.XX_VMR_DEPARTMENT_ID = DPE.XX_VMR_DEPARTMENT_ID) " +
					" FULL OUTER JOIN XX_VMR_CATEGORY CAT  " +
					" ON (CAT.XX_VMR_CATEGORY_ID = DPE.XX_VMR_CATEGORY_ID)" +
					" FULL OUTER JOIN XX_VMR_LINE li  " +
					" ON (LI.XX_VMR_LINE_ID = DPE.XX_VMR_LINE_ID) " +
					" FULL OUTER JOIN XX_VMR_SECTION SE  " +
					" ON (SE.XX_VMR_SECTION_ID = DPE.XX_VMR_SECTION_ID) " +
					" FULL OUTER JOIN XX_VMR_BRAND BRAN " +
					" ON (BRAN.XX_VMR_BRAND_ID = DPE.XX_VMR_BRAND_ID) " +
					" FULL OUTER JOIN M_PRODUCT PR " +
					" ON (DPE.M_PRODUCT_ID = PR.M_PRODUCT_ID) " ; 
			where += " WHERE DPE.IsActive = 'Y'" ;
		} //if
		else {
			
			/*select += "SELECT CAT.name, R.XX_VMR_CATEGORY_ID, DP.name, R.XX_VMR_DEPARTMENT_ID"
					" SELECT M_PRODUCT_ID, " +
					" CAT.VALUE||'-'||CAT.NAME, CAT.XX_VMR_CATEGORY_ID, " +
					" DP.VALUE||'-'||DP.NAME, DP.XX_VMR_DEPARTMENT_ID, " +
					" li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID, " +
					" se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID, " +
					" VR.VALUE||'-'||VR.NAME||' - '||LC.NAME, VR.XX_VMR_VENDORPRODREF_ID, " +
					"CASE WHEN R.XX_SYNCHRONIZED = 'Y' THEN 1 ELSE 0 END, " +
					" R.XX_QUANTITYPURCHASE "*/;
			/*from +=  " FROM  XX_VMR_PROMOTION E INNER JOIN XX_VMR_DETAILPROMOTIONEXT R" +
					" ON (E.XX_VMR_PROMOTION_ID= R.XX_VMR_PROMOTION_ID) " +
					//" INNER JOIN XX_VMR_VENDORPRODREF VR " +
					//" ON (VR.XX_VMR_VENDORPRODREF_ID = R.XX_VMR_VENDORPRODREF_ID) " +
					" INNER JOIN XX_VMR_DEPARTMENT DP " +
					" ON (DP.XX_VMR_DEPARTMENT_ID = R.XX_VMR_DEPARTMENT_ID) " +
					" INNER JOIN XX_VMR_CATEGORY CAT  " +
					" ON (CAT.XX_VMR_CATEGORY_ID = DP.XX_VMR_CATEGORY_ID)" 
					/*" INNER JOIN XX_VMR_LINE li  " +
					" ON (LI.XX_VMR_LINE_ID = VR.XX_VMR_LINE_ID) " +
					" INNER JOIN XX_VMR_SECTION SE  " +
					" ON (SE.XX_VMR_SECTION_ID = VR.XX_VMR_SECTION_ID) " +
					" INNER JOIN XX_VMR_BRAND BRAN " +
					" ON (BRAN.XX_VMR_BRAND_ID = VR.XX_VMR_BRAND_ID)" +
					" LEFT OUTER JOIN XX_VMR_LONGCHARACTERISTIC LC" + 
					" ON (VR.XX_VMR_LONGCHARACTERISTIC_ID = LC.XX_VMR_LONGCHARACTERISTIC_ID)"*/; 
			/*where += " WHERE E.IsActive = 'Y'" ;*/
					select += "SELECT CAT.VALUE||'-'||CAT.NAME, CAT.XX_VMR_CATEGORY_ID, "+
							"DP.VALUE||'-'||DP.NAME, DP.XX_VMR_DEPARTMENT_ID, "+
							"li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID, "+
							"se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID, " +
							"VR.VALUE||'-'||VR.NAME, VR.XX_VMR_VENDORPRODREF_ID, " +
							"PR.VALUE, PR.M_PRODUCT_ID, DPE.XX_VMR_DETAILPROMOTIONEXT_ID "
					/*select += //" SELECT M_PRODUCT_ID, " +
							" CAT.VALUE||'-'||CAT.NAME, CAT.XX_VMR_CATEGORY_ID, " +
							" DP.VALUE||'-'||DP.NAME, DP.XX_VMR_DEPARTMENT_ID " 
							" li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID, " +
							" se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID, " +
							" VR.VALUE||'-'||VR.NAME, VR.XX_VMR_VENDORPRODREF_ID, " +
							" P.VALUE||'-'||PR.DESCRIPTION, P.M_PRODUCT_ID, " +
							" CASE WHEN PR.PROCESSING = 'Y' THEN 1 ELSE 0 END, " +
							" R.XX_QUANTITYPURCHASE "*/;
					from +=  " FROM  XX_VMR_PROMOTION PROMO INNER JOIN XX_VMR_DETAILPROMOTIONEXT DPE" +
							" ON (PROMO.XX_VMR_PROMOTION_ID = DPE.XX_VMR_PROMOTION_ID) " +
							" FULL OUTER JOIN XX_VMR_VENDORPRODREF VR " +
							" ON (VR.XX_VMR_VENDORPRODREF_ID = DPE.XX_VMR_VENDORPRODREF_ID) " +
							" FULL OUTER JOIN XX_VMR_DEPARTMENT DP " +
							" ON (DP.XX_VMR_DEPARTMENT_ID = DPE.XX_VMR_DEPARTMENT_ID) " +
							" FULL OUTER JOIN XX_VMR_CATEGORY CAT  " +
							" ON (CAT.XX_VMR_CATEGORY_ID = DPE.XX_VMR_CATEGORY_ID)" +
							" FULL OUTER JOIN XX_VMR_LINE li  " +
							" ON (LI.XX_VMR_LINE_ID = DPE.XX_VMR_LINE_ID) " +
							" FULL OUTER JOIN XX_VMR_SECTION SE  " +
							" ON (SE.XX_VMR_SECTION_ID = DPE.XX_VMR_SECTION_ID) " +
							" FULL OUTER JOIN XX_VMR_BRAND BRAN " +
							" ON (BRAN.XX_VMR_BRAND_ID = DPE.XX_VMR_BRAND_ID) "+
							" FULL OUTER JOIN M_PRODUCT PR " +
							" ON (DPE.M_PRODUCT_ID = PR.M_PRODUCT_ID) " ; 
					where += " WHERE DPE.IsActive = 'Y'" ;
		} //if

		// Se define el elemento del que se tomarán las referencias/productos
		where += " AND DPE.XX_VMR_PROMOTION_ID = " + promotionID;
		if(checkCondition.isSelected()){
			where += " AND DPE.XX_VMR_PROMOCONDITIONVALUE_ID="+conditionID;
		}
		
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
				where += " AND DPE.CREATEDBY = " + user.getKey();	

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
		SQLCount = select2 + from +" WHERE DPE.XX_VMR_PROMOTION_ID = " + promotionID  ;
				
		//Preparar la tabla	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(SQLCount, null);
			rs = pstmt.executeQuery();	
			int resultSize = 0;
			if(rs.next()){
				resultSize = rs.getInt(1);
			}
			pstmt.close();
			rs.close();
			rs = null;
			pstmt = null;
			pstmt = DB.prepareStatement(SQLTotal, null);
			rs = pstmt.executeQuery();	
			

			if(typePromotion==1001100 && resultSize>10){
				pricePercent = true;
				/*if((lookupProduct.getValue() != null && refe == null) || 
						(lookupProduct.getValue() != null && refe != null) || 
						((Boolean)checkProduct.getValue() == true  && refe.getKey() == 0) ||
						((Boolean)checkProduct.getValue() == true  && refe.getKey() != 0)){*/
				
					String selectFinal = tableResults.prepareTable(columnsProd, "", "", true, "");

					
					
					
				}
				else if((refe != null && lookupProduct.getValue() == null 
						&& (Boolean)checkProduct.getValue() == false) || 
						((Boolean)checkReference.getValue() == true 
						&& lookupProduct.getValue() == null && (Boolean)checkProduct.getValue() == false)){
					pricePercent = false;
					String selectFinal = tableResults.prepareTable(columnsRef, "", "", true, "");
				}
			int row = 0;
			int rows = tableResults.getColumnCount();
			IDColumn id;
			while (rs.next()) {
				if(typePromotion==1001100 && resultSize>10){
					
				/*if((lookupProduct.getValue() != null && refe == null) || 
						(lookupProduct.getValue() != null && refe != null) || 
						((Boolean)checkProduct.getValue() == true  && refe.getKey() == 0) ||
						((Boolean)checkProduct.getValue() == true  && refe.getKey() != 0)){*/
					tableResults.setRowCount(row + 1);
					id = new IDColumn(rs.getInt(15));
					id.setSelected(false);
					tableResults.setValueAt(id, row, 0);												//Identificador
					tableResults.setValueAt(new KeyNamePair(rs.getInt(2), rs.getString(1)), row, 1);	// Categoria
					tableResults.setValueAt(new KeyNamePair(rs.getInt(4), rs.getString(3)), row, 2);	// Departamento
					tableResults.setValueAt(new KeyNamePair(rs.getInt(6), rs.getString(5)), row, 3);	// Linea
					tableResults.setValueAt(new KeyNamePair(rs.getInt(8), rs.getString(7)), row, 4);	// Seccion
					tableResults.setValueAt(new KeyNamePair(rs.getInt(10), rs.getString(9)), row, 5);	// Referenca
					tableResults.setValueAt(new KeyNamePair(rs.getInt(12), rs.getString(11)), row, 6);	// Producto
					tableResults.setValueAt(rs.getBigDecimal(13), row, 7);									// Precio
					tableResults.setValueAt(rs.getBigDecimal(14), row, 8);	       						            // Porcentaje
					tableResults.setValueAt(false, row, 9);	
					
					row++;
				} else {
					tableResults.setRowCount(row + 1);
					if(typePromotion==1001100) id = new IDColumn(rs.getInt(15));
					else id = new IDColumn(rs.getInt(13));
					id.setSelected(false);
					tableResults.setValueAt(id, row, 0);												//Identificador
					tableResults.setValueAt(new KeyNamePair(rs.getInt(2), rs.getString(1)), row, 1);	// Categoria
					tableResults.setValueAt(new KeyNamePair(rs.getInt(4), rs.getString(3)), row, 2);	// Departamento
					tableResults.setValueAt(new KeyNamePair(rs.getInt(6), rs.getString(5)), row, 3);	// Linea
					tableResults.setValueAt(new KeyNamePair(rs.getInt(8), rs.getString(7)), row, 4);	// Seccion
					tableResults.setValueAt(new KeyNamePair(rs.getInt(10), rs.getString(9)), row, 5);	// Referenca
					tableResults.setValueAt(new KeyNamePair(rs.getInt(12), rs.getString(11)), row, 6);	                                // Producto
					row++;
				} // referencia
			}
			rs.close();
			pstmt.close();
			disponible.setValue(row);
			tableResults.autoSize(true);
			tableResults.setMultiSelection(true);
		} // try 
		catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
		}
		finally{
			try { rs.close(); } 
			catch (SQLException e) { e.printStackTrace(); }
			try { pstmt.close(); } 
			catch (SQLException e) { e.printStackTrace(); }
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
		tableResults.stopEditor(true);
		ArrayList<Object> params;
		List<Object[]> bulkParams = new ArrayList<Object[]>();
		String msj = Msg.getMsg(Env.getCtx(), "XX_DeleteElement");
		boolean delete = ADialog.ask(m_WindowNo, m_frame, msj);
		/*String sql = "DELETE FROM XX_VMR_DETAILPROMOTIONEXT WHERE "+
				"XX_VMR_DETAILPROMOTIONEXT_ID = ? ";*/
		String sql = "Update XX_VMR_DETAILPROMOTIONEXT set ISACTIVE = 'N' WHERE XX_VMR_DETAILPROMOTIONEXT_ID = ? ";
		// El usuario ha decidido eliminar lo seleccionado
		if (delete) {
			
			/* Se recorren las filas seleccionadas para eliminarlas */
			for (int i = 0; i < tableResults.getRowCount(); i++) {
				IDColumn id = (IDColumn)tableResults.getModel().getValueAt(i, 0);
				// Fila seleccionada para tomar los datos
				if (id.isSelected()){
					// Eliminando productos
					//X_XX_VMR_DetailPromotionExt  dpe = new X_XX_VMR_DetailPromotionExt(Env.getCtx(),id.getRecord_ID(),null);
					params = new ArrayList<Object>();
					params.add(id.getRecord_ID());
					bulkParams.add(params.toArray());
				//	X_XX_VMR_DetailPromotionExt  dpe = new X_XX_VMR_DetailPromotionExt(Env.getCtx(),id.getRecord_ID(),null);
				//    deleteDetailPromotionExt(dpe);
				/*	dpe.setXX_VMR_Category_ID((Integer)tableResults.getModel().getValueAt(i, 1));
				    dpe.setXX_VMR_Department_ID((Integer)tableResults.getModel().getValueAt(i, 2));
				    dpe.setXX_VMR_Line_ID((Integer)tableResults.getModel().getValueAt(i, 3));
				    dpe.setXX_VMR_Section_ID((Integer)tableResults.getModel().getValueAt(i, 4));*/
					
				} // if selected
			} // for filas
			DB.executeBulkUpdate(null, sql, bulkParams, true, true);
		
		} // delete
		loadTableInfo();
		// Ventana de salida
		String msjDeleted = Msg.getMsg(Env.getCtx(), "XX_DeletedElement");
		ADialog.info(m_WindowNo, m_frame, msjDeleted);
		
		//dispose();
		
	} // Fin processDelete
	
	/** processSelection
	 *  Configura las cantidades de acuerdo a lo definido por el usuario
	 * */
	public void processSelection () {
		try{
			tableResults.stopEditor(true);
		
			ArrayList<Object> params;
			List<Object[]> bulkParams = new ArrayList<Object[]>();
			String sqlUpdate = " UPDATE XX_VMR_DETAILPROMOTIONEXT " +
					" SET XX_DISCOUNTAMOUNT = ?, XX_DISCOUNTRATE=?"+
					" WHERE M_PRODUCT_ID = ? AND XX_VMR_DETAILPROMOTIONEXT_ID=?" ;
			/* Se recorren las filas seleccionadas para inicializar los vectores con 
			 * la información de cada uno de ellos */
			for (int i = 0; i < tableResults.getRowCount(); i++) {
				IDColumn id = (IDColumn)tableResults.getModel().getValueAt(i, 0);
				
				if(typePromotion==1001100){
					if((Boolean)tableResults.getModel().getValueAt(i, 9)){
						X_XX_VMR_DetailPromotionExt  dpe = new X_XX_VMR_DetailPromotionExt(Env.getCtx(),id.getRecord_ID(),null);
						params = new ArrayList<Object>();	
						params.add((BigDecimal)tableResults.getModel().getValueAt(i, 7));
						params.add((BigDecimal)tableResults.getModel().getValueAt(i, 8));
						params.add(((KeyNamePair)tableResults.getValueAt(i, 6)).getID());
						params.add(dpe.get_ID());
						bulkParams.add(params.toArray());
						
					}
				}	
			} // for minitanble
			
			//Actualizo los detalles 
			int act = DB.executeBulkUpdate(null, sqlUpdate, bulkParams, true, true);
			
			if(act < 0)  ADialog.info(m_WindowNo, m_frame, "No fue posible modificar los detalles. Contactar al administrador del sistema.");
			else{
			// Ventana de salida
			String msjModified = Msg.getMsg(Env.getCtx(), "XX_ModifiedElement")+"\n Se modificaron "+act+" registros.";
			ADialog.info(m_WindowNo, m_frame, msjModified);
			dispose();
			}
		}catch(Exception e){
			ADialog.info(m_WindowNo, m_frame, "No fue posible modificar los detalles.\n" +
					"Debe realizar una búsqueda de productos antes de procesar modificaciones.");
		}
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
			//if(comboCategory.getValue() != null){
			//	if(!comboCategory.getValue().equals(Integer.valueOf(0))){
					comboDepartment.removeActionListener(this);
					loadDepartmentInfo();
					loadLineInfo();
					loadSectionInfo();
					comboDepartment.addActionListener(this);
			//	}
			//}
		}//Category Combo 
		
		// Department combo 
		else if (e.getSource() == comboDepartment){			
			if(comboDepartment.getValue() != null){
				if(!comboDepartment.getValue().equals(Integer.valueOf(0))) {
					loadReference();
					loadLineInfo();
					comboBPartner.removeActionListener(this);
					loadBPartnerInfo();
					comboBPartner.addActionListener(this);
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
				//disponible.setText("-");
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
		else if(e.getSource() == checkCondition){
			/*try {
				loadTableInfo();
				//disponible.setText("-");
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
			}*/
			loadDepartmentInfo();
			loadLineInfo();
			loadSectionInfo();
			loadReference();
			loadBPartnerInfo();
			loadUserInfo();
		
		}
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
		Integer detail;
		int field;
		BigDecimal price;;
		BigDecimal percent;
		//tableResults.stopEditor(true);
		if(typePromotion==1001100 && pricePercent){
			// Se toman los cambios que ocurren en la tabla por parte del usuario
			// referente a las cantidades
			if(tableResults.isEditing()){
				if(tableResults.getRowCount() > 0){
					// Se obtiene el detalle asociado
					field = e.getColumn();
					detail = ((IDColumn)tableResults.getModel().
							getValueAt(tableResults.getEditingRow(), 0)).getRecord_ID();
					percent = (BigDecimal)tableResults.getModel().
							getValueAt(tableResults.getEditingRow(), 8);
					price = (BigDecimal)tableResults.getModel().
							getValueAt(tableResults.getEditingRow(), 7);
					if(field==7){
						if(price.floatValue()>0 && percent.compareTo(new BigDecimal(0))==1){
							//ERROR... o setear a 0 el campo 8
						}else{
							tableResults.setValueAt(true, e.getFirstRow(), 9);
							
						//	for (int i =  e.getFirstRow()+1; i < tableResults.getRowCount(); i++) {
						//		tableResults.setValueAt(price,i, 7);
						//		tableResults.setValueAt(true, i,9);
						//	}
						}
					}else if (field==8){
						if(price.floatValue()>0 && percent.compareTo(new BigDecimal(0))==1){
							//ERROR... o setear a 0 el campo 7
						}else{
							tableResults.setValueAt(true, e.getFirstRow(),9);
							//for (int i =  e.getFirstRow()+1; i < tableResults.getRowCount(); i++) {
							//	tableResults.setValueAt(percent,i, 8);
							//	tableResults.setValueAt(true, i,9);
							//}
						}
					}
				}
			}
		}
	}   //  tableChanged
	
	/*private void deleteDetailPromotionExt(X_XX_VMR_DetailPromotionExt detail){
	// CARGA DE CATEGORIAS
		String sql = "DELETE FROM XX_VMR_DETAILPROMOTIONEXT WHERE "+
				"XX_VMR_DETAILPROMOTIONEXT_ID = "+detail.getXX_VMR_DetailPromotionExt_ID();
		DB.executeUpdate(null,sql);

	}*/
	
} // Fin XX_VME_ModifyReferencesForm
