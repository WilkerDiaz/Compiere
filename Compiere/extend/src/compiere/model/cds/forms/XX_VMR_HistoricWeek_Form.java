package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
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

/** XX_VMR_HistoricWeek_Form
 * Forma que permite al usuario filtrar las ventas. 
 * @author Maria Vintimilla
 * @version 1.0
 * */
public class XX_VMR_HistoricWeek_Form extends CPanel implements FormPanel, 
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
	
	private int indx = 0;

	/**	Logger */
	protected static CLogger log = CLogger.getCLogger(XX_VMR_HistoricWeek_Form.class);
		
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
	protected CButton bReset = new CButton(Msg.translate(Env.getCtx(), "XX_ClearFilter"));
	protected CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "XX_Search"));
	
	//CATEGORIA
	protected CLabel labelCategory = new CLabel();
	protected VComboBox comboCategory = new VComboBox();
	
	//DEPARTAMENTO
	protected CLabel labelDepartment = new CLabel();
	protected VComboBox comboDepartment = new VComboBox();
	
	//TIENDA
	protected CLabel labelTienda = new CLabel();
	protected VComboBox comboTienda = new VComboBox();
	
	// COLECCION 
	protected CLabel labelCollection = new CLabel();
	protected VComboBox comboCollection = new VComboBox();
	
	// SECCION
	protected CLabel labelSection = new CLabel();
	protected VComboBox comboSection = new VComboBox();
	
	// PRODUCTO 
	protected CLabel labelProduct = new CLabel();
	protected VLookup lookupProduct = null;
	
	// REFERENCIA
	protected CLabel labelReference = new CLabel();
	protected VComboBox comboReference = new VComboBox();
	
	// MARCA
	protected CLabel labelBrand = new CLabel();
	protected VComboBox comboBrand = new VComboBox();
	
	// LINEA
	protected CLabel labelLine = new CLabel();
	protected VComboBox comboLine = new VComboBox();
	
	//OTROS
	protected CLabel lastWeekLabel = new CLabel();
	protected CTextField lastWeekText = new CTextField();
	
	//OTROS
	protected CLabel labelResults = new CLabel();
	protected CTextField textResults = new CTextField();
	
	// La tabla donde se guardarán los datos
	protected MiniTablePreparator tableResultsTop = new MiniTablePreparator();
	protected MiniTablePreparator tableResultsBottom = new MiniTablePreparator();
	
	// Vectores que contiene los resultados del filtro
	protected Vector<KeyNamePair> Category = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Department = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Line = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Section = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Brand = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Reference = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Collection = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Tienda = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Product = new Vector<KeyNamePair>();
	
	// Columnas 
	private ColumnInfo[] columnsRef = new ColumnInfo[] { 
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Line"),".", String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_CodRef"),".", String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Brand"),".", String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ProdName"),".", String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_SumCantVtas"),".",  String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_SumCantInv"),".",  String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_SumST"),".", String.class, ".")
	};
	
	/** Static Init
	 *  @throws Exception
	 */
	private final void jbInit() throws Exception {
		bSearch.setPreferredSize(new Dimension(100, 25));
		bReset.setPreferredSize(new Dimension(100, 25));
		CompiereColor.setBackground(this);		
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);	
	
		labelCategory.setText(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"));
		labelDepartment.setText(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"));
		labelTienda.setText(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
		labelLine.setText(Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"));
		labelResults.setText(Msg.translate(Env.getCtx(), "XX_Results"));
		labelCollection.setText(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"));
		labelSection.setText(Msg.translate(Env.getCtx(), "XX_VMR_Section_ID"));
		labelProduct.setText(Msg.translate(Env.getCtx(), "M_Product_ID"));
		labelReference.setText(Msg.translate(Env.getCtx(), "XX_VMR_VENDORPRODREF_ID"));
		labelBrand.setText(Msg.translate(Env.getCtx(), "XX_VMR_Brand_ID"));

		// PANEL CREATION
		mainPanel.add(parameterPanel, BorderLayout.NORTH);		
		
		// CATEGORIA
		parameterPanel.add(labelCategory,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboCategory,  new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// DEPARTAMENTO
		parameterPanel.add(labelDepartment,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboDepartment,   new GridBagConstraints(4, 0, 2, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// LINE
		parameterPanel.add(labelLine,  new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboLine,   new GridBagConstraints(7, 0, 2, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// SECTION
		parameterPanel.add(labelSection, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboSection, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,	new Insets(5, 5, 5, 5), 0, 0));
		
		// BRAND
		parameterPanel.add(labelBrand, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,	new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboBrand, new GridBagConstraints(4, 1, 2, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,	new Insets(5, 5, 5, 5), 0, 0));
		
		// REFERENCE
		parameterPanel.add(labelReference, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,	new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboReference, new GridBagConstraints(7, 1, 2, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		// PRODUCT
		parameterPanel.add(labelProduct, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(lookupProduct,new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// COLLECTION
		parameterPanel.add(labelCollection, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboCollection, new GridBagConstraints(4, 2, 2, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// WAREHOUSE
		parameterPanel.add(labelTienda, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboTienda, new GridBagConstraints(7, 2, 2, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// BUSCAR
		parameterPanel.add(bSearch, new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// RESET
		parameterPanel.add(bReset, new GridBagConstraints(7, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	
		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		dataPane.setPreferredSize(new Dimension(1280, 600));

		//PANEL INFERIOR
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.CENTER);
		commandLayout.setHgap(10);
		
		bReset.addActionListener(this);
		bSearch.addActionListener(this);
		lookupProduct.addActionListener(this);
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		desactivarActionListeners();
		//Restaura los ComboBoxes y CheckBoxes		
		comboCategory.setEnabled(true);
		comboDepartment.setEnabled(false);
		comboTienda.setEnabled(true);
		comboCategory.removeAllItems();
		comboDepartment.removeAllItems();
		comboTienda.removeAllItems();
		comboLine.removeAllItems();
		comboSection.removeAllItems();
		comboCollection.removeAllItems();
		comboBrand.removeAllItems();
		lookupProduct.setValue(null);
		lookupProduct.setEnabled(false);
		
		//Setting Default Items
		KeyNamePair loadKNP = new KeyNamePair(0, "");
		comboCategory.addItem(loadKNP);
		comboTienda.addItem(loadKNP);
		comboCollection.addItem(loadKNP);
		comboBrand.addItem(loadKNP);
		
		// Cantidad del elemento
		textResults.setEditable(false);
		textResults.setBackground(Color.LIGHT_GRAY);
		textResults.setPreferredSize(new Dimension(80, 25));
		textResults.setHorizontalAlignment(JTextField.CENTER);
		lastWeekText.setEditable(false);
		lastWeekText.setBackground(Color.LIGHT_GRAY);
		lastWeekText.setPreferredSize(new Dimension(80, 25));
		lastWeekText.setHorizontalAlignment(JTextField.CENTER);
		
		// CARGA DE CATEGORIAS
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||' - '||NAME FROM XX_VMR_CATEGORY WHERE ISACTIVE='Y' ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
			+ " AND ISACTIVE = 'Y' ORDER BY VALUE||' - '||NAME";
		
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
		
		
		// CARGA DE TIENDAS
		sql = "SELECT M_WAREHOUSE_ID, VALUE||' - '||NAME FROM M_WAREHOUSE " ;
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) +
				" AND ISACTIVE = 'Y'  ORDER BY VALUE||' - '||NAME";
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboTienda.addItem(loadKNP);
			}
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE COLECIONES
		sql = "SELECT XX_VMR_COLLECTION_ID, VALUE||' - '||NAME FROM XX_VMR_COLLECTION " ;
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) +
				" AND ISACTIVE = 'Y'  ORDER BY VALUE||' - '||NAME";
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCollection.addItem(loadKNP);
			}
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		

		// CARGA DE MARCAS
		sql = "SELECT XX_VMR_BRAND_ID, VALUE||' - '||NAME FROM XX_VMR_BRAND " ;
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) +
				" AND ISACTIVE = 'Y'  ORDER BY VALUE||' - '||NAME";
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
		activarActionListeners();
	} // loadBasicInfo
	

	/** loadDepartmentInfo
	 * Cargar información de departamentos
	 * */
	protected final void loadDepartmentInfo(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		comboDepartment.removeAllItems();
		comboDepartment.setEnabled(true);
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair loadKNP;
		String sql = "";
		
		loadKNP = new KeyNamePair(0,"");
		comboDepartment.addItem(loadKNP);
		sql = " SELECT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||' - '||dp.NAME" 
			+ " FROM XX_VMR_DEPARtMENT dp";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);

		if(cat != null && cat.getKey() != 0){
			sql += " AND dp.XX_VMR_CATEGORY_ID = " + cat.getKey();
		}
		sql += " ORDER BY dp.VALUE||' - '||dp.NAME";	
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dept = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(dept);			
			}			
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
	protected final void loadLineInfo(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		comboLine.removeAllItems();
		comboLine.setEnabled(true);
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair loadKNP;
		String sql = "";
		
		loadKNP = new KeyNamePair(0,"");
		comboLine.addItem(loadKNP);
		sql = " SELECT li.XX_VMR_LINE_ID, li.VALUE||' - '||li.NAME" 
			+ " FROM XX_VMR_LINE li";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);

		if(dept != null && dept.getKey() != 0){
			sql += " AND li.XX_VMR_department_ID = " + dept.getKey();
		}
		sql += " ORDER BY li.VALUE||' - '||li.NAME";	
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dept = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboLine.addItem(dept);			
			}			
		}
		catch (SQLException e) 	{
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
	protected final void loadSectionInfo(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		comboSection.removeAllItems();
		comboSection.setEnabled(true);
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair loadKNP;
		String sql = "";
		
		loadKNP = new KeyNamePair(0,"");
		comboSection.addItem(loadKNP);
		sql = " SELECT XX_VMR_section_ID, VALUE||' - '||NAME" 
			+ " FROM XX_VMR_section";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);

		if(line != null && line.getKey() != 0){
			sql += " AND XX_VMR_line_ID = " + line.getKey();
		}
		sql += " ORDER BY VALUE||' - '||NAME";	
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboSection.addItem(loadKNP);			
			}			
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}	
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadSectionInfo
	
	/** loadBrandInfo
	 * Cargar información de marcas
	 * */
	protected final void loadBrandInfo(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		comboBrand.removeAllItems();
		comboBrand.setEnabled(true);
		KeyNamePair loadKNP;
		KeyNamePair brand;
		String sql = "";
		
		loadKNP = new KeyNamePair(0,"");
		comboBrand.addItem(loadKNP);
		sql = " SELECT BR.XX_VMR_BRAND_ID, BR.VALUE||' - '||BR.NAME" 
			+ " FROM XX_VMR_BRAND BR";	
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
			+ " AND BR.ISACTIVE = 'Y'";

		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				brand = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(brand);			
			}			
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}	
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadDepartmentInfo
	
	
	/** loadReference
	 * Cargar informacion de las referencias de proveedor
	 * */
	protected final void loadReference(){
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair brand = (KeyNamePair)comboBrand.getSelectedItem();
		KeyNamePair loadKNP;
		KeyNamePair reference = null;
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		comboReference.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboReference.addItem(loadKNP);
		
		// HAY UN DEPARTAMENTO SELECCIONADO
		if(dept == null || dept.getKey() == 0 ) {	
			return;
			
		} // IF DEPT 
		else {	
			comboReference.setEnabled(true);
			sql = " SELECT VR.XX_VMR_VENDORPRODREF_ID, VR.VALUE||' - '||VR.NAME" 
				+ " FROM XX_VMR_VENDORPRODREF VR ";
			
			sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO)
				+ " AND VR.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
			
			if((line != null ) && line.getKey() != 0) {
				sql += " AND VR.XX_VMR_LINE_ID = " + line.getKey();
			}
			
			if((sect != null) && sect.getKey() != 0) {
				sql += " AND VR.XX_VMR_SECTION_ID = " + sect.getKey();
			}
			
			if((brand != null) && brand.getKey() != 0) {
				sql += " AND VR.XX_VMR_BRAND_ID = " + brand.getKey();
			}
		} // ELSE DEPT
		//System.out.println(sql);
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
	
	
	
	/** Activa todos los listeners creados */
	protected final void activarActionListeners () {
		comboCategory.addActionListener(this);
		comboDepartment.addActionListener(this);
		comboLine.addActionListener(this);
		comboSection.addActionListener(this);
		comboReference.addActionListener(this);
		lookupProduct.addActionListener(this);
		comboBrand.addActionListener(this);
		comboCollection.addActionListener(this);
		comboTienda.addActionListener(this);
	}//addActionListeners
	
	/** Deshabilitar Action Listeners */
	protected final void desactivarActionListeners () {
		comboCategory.removeActionListener(this);
		comboDepartment.removeActionListener(this);
		comboLine.removeActionListener(this);
		comboSection.removeActionListener(this);
		comboReference.removeActionListener(this);
		lookupProduct.removeActionListener(this);
		comboBrand.removeActionListener(this);
		comboCollection.removeActionListener(this);
		comboTienda.removeActionListener(this);
	} // removeActionListeners

	/** loadTableInfo
	 *  Busqueda para llenar la TableInfo de acuerdo con las especificaciones 
	 *  del usuario
	 */
	protected void loadTableInfo(){
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		// 5 mejores
		tableResultsTop.setRowCount(0);
		tableResultsTop = new MiniTablePreparator();
		tableResultsTop.setRowHeight(tableResultsTop.getRowHeight() + 2 );
		dataPane.getViewport().add(tableResultsTop, null);
		tableResultsTop.getSelectionModel().addListSelectionListener(this);
		tableResultsTop.getModel().addTableModelListener(this);
		
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair section = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair brand = (KeyNamePair)comboBrand.getSelectedItem();
//		KeyNamePair prod = (KeyNamePair)lookupProduct.get();
		KeyNamePair reference = (KeyNamePair)comboReference.getSelectedItem();
		KeyNamePair collection = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair tienda = (KeyNamePair)comboTienda.getSelectedItem();
		
		String select = "";
		String from = "";
		String where = " where XX_INITIALINVENTORY > 0 ";
		String and = " and ";
		String top5 = " GROUP BY LIN.VALUE, R.VALUE, B.NAME, P.NAME "+
				" ORDER BY  CASE WHEN SUM(XX_INITIALINVENTORY) >=1  THEN ROUND((SUM(XX_SALES)/SUM(XX_INITIALINVENTORY)*100),2) ELSE 0 END DESC";
		String bottom5 = " GROUP BY LIN.VALUE, R.VALUE, B.NAME, P.NAME "+
				" ORDER BY  CASE WHEN SUM(XX_INITIALINVENTORY) >=1 THEN ROUND((SUM(XX_SALES)/SUM(XX_INITIALINVENTORY)*100),2) ELSE 0 END ASC";
		String SQLTotalTop5 = "";
		String SQLTotalBottom5 = "";

		if(cat.getKey()!= 0 ){
			if(where.length() > 7)
				where += and;
			where += " H.XX_VMR_CATEGORY_ID = " + cat.getKey();
		} // if categoria
		
		//Leer Departamento
		if(dept != null && dept.getKey()!= 0){
			if(where.length() > 7)
				where += and;
			where += " H.XX_VMR_DEPARTMENT_ID = " + dept.getKey();				
		} // if dept != null
		
		//Leer Linea
		if(line != null && line.getKey() != 0){
			if(where.length() > 7)
				where += and;
			where += "  H.XX_VMR_LINE_ID = " + line.getKey();				
		} // if line != null
		
		//Leer Seccion
		if(section != null && section.getKey() != 0){
			if(where.length() > 7)
				where += and;
			where += "  H.XX_VMR_SECTION_ID = " + section.getKey();				
		} // if dept != null
		
		//Leer Marca
		if(brand != null && brand.getKey() != 0){
			if(where.length() > 7)
				where += and;
			where += "  H.XX_VMR_BRAND_ID = " + brand.getKey();				
		} // if dept != null
		
		//Leer Producto
		//Leyendo producto
		if(lookupProduct.getValue() != null ){
			if(where.length() > 7)
				where += and;
			where += "  H.M_PRODUCT_ID = " + lookupProduct.getValue();
		} // if producto
		
		//Leer Referencia
		if(reference != null && reference.getKey() != 0){
			if(where.length() > 7)
				where += and;
			where += "  H.XX_VMR_VENDORPRODREF_ID = " + reference.getKey();				
		} // if dept != null
		
		//Leer Coleccion
		if(collection != null && collection.getKey() != 0){
			if(where.length() > 7)
				where += and;
			where += "  H.XX_VMR_COLLECTION_ID = " + collection.getKey();				
		} // if dept != null
		
		//Leer Tienda
		if(tienda != null && tienda.getKey() != 0){
			if(where.length() > 7)
				where += and;
			where += "  H.M_WAREHOUSE_ID = " + tienda.getKey();				
		} // if tienda != null
		
		select += " SELECT " +
				" LIN.VALUE CODLIN, " +
				" R.VALUE REFPROV, " +
				" B.NAME MARCA, " +
				" P.NAME NOMPROD, " +
				" TO_CHAR(ROUND(SUM(XX_SALES),2),'999G999G990D99') VENTAS, " +
				" TO_CHAR(ROUND(SUM(XX_INITIALINVENTORY),2),'999G999G990D99') INV, " +
				" TO_CHAR(CASE WHEN SUM(XX_INITIALINVENTORY) >=1  THEN ROUND((SUM(XX_SALES)/SUM(XX_INITIALINVENTORY)*100),2) ELSE 0 END,'999G999G990D99') ST " +
				" FROM  XX_VMR_HISTORICWEEKSALES H " +
				" LEFT OUTER JOIN XX_VMR_LINE LIN ON (H.XX_VMR_LINE_ID=LIN.XX_VMR_LINE_ID)" +
				" LEFT OUTER JOIN XX_VMR_BRAND B ON (H.XX_VMR_BRAND_ID=B.XX_VMR_BRAND_ID)" +
				" LEFT OUTER JOIN XX_VMR_VENDORPRODREF R ON (H.XX_VMR_VENDORPRODREF_ID=R.XX_VMR_VENDORPRODREF_ID)" +
				" LEFT OUTER JOIN M_PRODUCT P ON (H.M_PRODUCT_ID=P.M_PRODUCT_ID)" ;

		SQLTotalTop5 = select + from + where + top5;
		SQLTotalBottom5 = select + from + where + bottom5;
		
		System.out.println("SQLTotalTop5: "+ SQLTotalTop5);
		System.out.println("SQLTotalBottom5: "+ SQLTotalBottom5);
		
		//Preparar la tabla	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		String selectFinalTop = tableResultsTop.prepareTable(columnsRef, "", "", true, "");

		int row = 0;
		
		// 5 mejores productos	
		try {
			pstmt = DB.prepareStatement(SQLTotalTop5, null);
			rs = pstmt.executeQuery();		

			while (rs.next() && row < 6) {
				tableResultsTop.setRowCount(row + 1);
				// 5 mejores
				if(row == 0){
					tableResultsTop.setValueAt("5 Mejores", row, 0);				// Titulo
					row++;
				}
				tableResultsTop.setRowCount(row + 1);
				tableResultsTop.setValueAt(rs.getString(1), row, 0);			// Linea
				tableResultsTop.setValueAt(rs.getString(2), row, 1);			// Referencia
				tableResultsTop.setValueAt(rs.getString(3), row, 2);			// Marca
				tableResultsTop.setValueAt(rs.getString(4), row, 3);			// Producto
				tableResultsTop.setValueAt(rs.getString(5), row, 4);		// Sum ventas
				tableResultsTop.setValueAt(rs.getString(6), row, 5);		// Sum inventario
				tableResultsTop.setValueAt(rs.getString(7), row, 6);		// Sum ST
				row++;
			}
			tableResultsTop.autoSize(true);
			tableResultsTop.setMultiSelection(true);
		} // try 
		catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// 5 peores productos
		try {
			pstmt2 = DB.prepareStatement(SQLTotalBottom5, null);
			rs2 = pstmt2.executeQuery();		

			while (rs2.next() && row < 12) {
				tableResultsTop.setRowCount(row + 1);
				// 5 mejores productos
				if(row == 6){
					tableResultsTop.setValueAt("5 Peores", row, 0);				// Titulo
					row++;
				}
				tableResultsTop.setRowCount(row + 1);
				tableResultsTop.setValueAt(rs2.getString(1), row, 0);			// Linea
				tableResultsTop.setValueAt(rs2.getString(2), row, 1);			// Referencia
				tableResultsTop.setValueAt(rs2.getString(3), row, 2);			// Marca
				tableResultsTop.setValueAt(rs2.getString(4), row, 3);			// Producto
				tableResultsTop.setValueAt(rs2.getString(5), row, 4);		// Sum ventas
				tableResultsTop.setValueAt(rs2.getString(6), row, 5);		// Sum inventario
				tableResultsTop.setValueAt(rs2.getString(7), row, 6);		// Sum ST
				row++;
			}
			tableResultsTop.autoSize(true);
			tableResultsTop.setMultiSelection(true);
		} // try 
		catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
		}
		finally{
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
		}
		tableResultsTop.getColumnModel().getColumn(0).setCellRenderer(render); 
		tableResultsTop.getColumnModel().getColumn(1).setCellRenderer(render); 
		tableResultsTop.getColumnModel().getColumn(2).setCellRenderer(render); 
		tableResultsTop.getColumnModel().getColumn(3).setCellRenderer(render); 
		tableResultsTop.getColumnModel().getColumn(4).setCellRenderer(render); 
		tableResultsTop.getColumnModel().getColumn(5).setCellRenderer(render); 
		tableResultsTop.getColumnModel().getColumn(6).setCellRenderer(render); 
		// 5 mejores
		System.out.println(tableResultsTop.getRowCount());
		tableResultsTop.repaint();		
		TableColumn col5m = tableResultsTop.getColumnModel().getColumn(indx+1);
		tableResultsTop.setMultiSelection(true);
		tableResultsTop.setRowSelectionAllowed(false);
		tableResultsTop.setSortEnabled(false);
		tableResultsTop.autoSize();
		tableResultsTop.setRowHeight(tableResultsTop.getRowHeight() + 2 );
		tableResultsTop.getTableHeader().setReorderingAllowed(false);
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
					comboDepartment.removeAllItems();
					comboReference.removeAllItems();
					comboLine.removeAllItems();
					comboSection.removeAllItems();
					comboDepartment.setEnabled(true);
					comboLine.setEnabled(false);
					comboSection.setEnabled(false);
					comboReference.setEnabled(false);
					comboDepartment.removeActionListener(this);
					loadDepartmentInfo();
					comboDepartment.addActionListener(this);
				}
				else{
					comboDepartment.setEnabled(false);
					comboLine.setEnabled(false);
					comboSection.setEnabled(false);
					comboReference.setEnabled(false);
				}
			}//combo != null
			
		}//Category Combo 
		
		//Updating Line ComboBox after a Department is selected 
		else if (e.getSource() == comboDepartment){			
			lookupProduct.setEnabled(true);
			
			if(comboDepartment.getValue() != null){
				if(!comboDepartment.getValue().equals(Integer.valueOf(0))){
					comboLine.removeAllItems();
					comboSection.removeAllItems();
					comboReference.removeAllItems();
					comboSection.setEnabled(false);
					comboReference.setEnabled(true);
					comboLine.removeActionListener(this);
					loadLineInfo();
					comboLine.addActionListener(this);
					comboReference.removeActionListener(this);
					loadReference();
					comboReference.addActionListener(this);
				}
				else{
					comboLine.setEnabled(false);
					comboSection.setEnabled(false);
					comboReference.setEnabled(false);
				}
			}// dep null
		}// line and dept
		
		//Updating Section ComboBox after a Line is selected
		else if (e.getSource() == comboLine ){
			if(comboLine.getValue() != null){
				if(!comboLine.getValue().equals(Integer.valueOf(0))){
					comboSection.removeAllItems();
					comboReference.removeAllItems();
					comboReference.setEnabled(true);
					comboSection.removeActionListener(this);
					loadSectionInfo();
					comboSection.addActionListener(this);
					comboReference.removeActionListener(this);
					loadReference();
					comboReference.addActionListener(this);
				}
				else{
					comboSection.setEnabled(false);
					comboReference.setEnabled(false);
				}
			}
		} // Line combobox
		
		//Brand ComboBox
		else if(e.getSource() == comboBrand){
			if(comboBrand.getValue() != null){
				if(!comboBrand.getValue().equals(Integer.valueOf(0))) {
					comboReference.removeAllItems();
					comboReference.setEnabled(true);
					comboReference.removeActionListener(this);
					loadReference();
					comboReference.addActionListener(this);
				}
			}
		} // comboBox brand
		
		//Clean Form
		else if(e.getSource() == bReset){
			limpiarFiltro();
		} // bReset
		
		//Execute Search
		else if(e.getSource() == bSearch) {
			try {
				loadTableInfo();
			} 
			catch (NullPointerException n) {
				n.printStackTrace();
			}
		} // bSearch
		
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
	protected void limpiarFiltro() { 
		loadBasicInfo();
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	TableCellRenderer render = new TableCellRenderer() { 
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) { 
			//Si values es nulo dara problemas de renderizado, por lo tanto se pone como vacio 
			JLabel lbl = new JLabel(value == null? "": value.toString()); 
			lbl.setBackground(Color.LIGHT_GRAY);
			lbl.setOpaque(true); 
		
			if(row == 0 || row == 6) 
				{
				//lbl.setHorizontalAlignment(SwingConstants.RIGHT); //alina a la izquierda 
				lbl.setForeground(Color.BLACK); //fuente azul 
				lbl.setFont(new Font("Arial", Font.BOLD, 12));
				lbl.setBackground(Color.GRAY);
				} 
			if(column == 4 || column == 5 || column == 6){ 
				lbl.setHorizontalAlignment(SwingConstants.RIGHT);
			} 
			return lbl; 
		} 
	}; 
} //XX_ProductFilter