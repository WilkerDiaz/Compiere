package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.X_Ref_AD_User_NotificationType;

/**
 * Forma para consultar el Inventario de los Productos asociados a una Referencia de Proveedor dada.
 * @author Gabriela Marques
 *
 */

public class XX_InventoryByReferenceForm  extends CPanel 
	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener {
		

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);

		try {
			//	UI
			jbInit(); // Layouts
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_InventoryByReferenceForm.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static StringBuffer    m_sqlDetail = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	String		   m_selectT1 = ""; //Tabla1
	String		   m_selectT2 = ""; //Tabla detalle
	static StringBuilder sql;
	//panel - tablas
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private TitledBorder xTableBorder = new TitledBorder(Msg.getMsg(Env.getCtx(), "Product"));
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private MiniTablePreparator xTable = new MiniTablePreparator();
	private JScrollPane xScrollPane = new JScrollPane();
	private MiniTablePreparator xTable2 = new MiniTablePreparator();
	private TitledBorder xTableBorder2 = new TitledBorder(Msg.getMsg(Env.getCtx(),"ProductDetail"));
	private JScrollPane xScrollPane2 = new JScrollPane();
	//private CPanel xPanel = new CPanel();
	/** Calls   = C */
	public static final String NOTIFICATIONTYPE_Calls = X_Ref_AD_User_NotificationType.CALLS.getValue();
	/** EMail = E */
	public static final String NOTIFICATIONTYPE_EMail = X_Ref_AD_User_NotificationType.E_MAIL.getValue();


	//BUSINESS PARTNER
	private CLabel labelBPartner = new CLabel(Msg.getMsg(Env.getCtx(), "BPartner"));
	private static CComboBox comboBPartner = new CComboBox();
	//Código de la Referencia
	private CLabel LabelRefNumber = new CLabel(Msg.getMsg(Env.getCtx(), "Reference No"));
	private static CComboBox comboReference = new CComboBox();
	//Almacén
	private CLabel labelWarehouse = new CLabel(Msg.getMsg(Env.getCtx(), "Warehouse"));
	private static CComboBox comboWarehouse = new CComboBox();
	//Departamento
	private CLabel labelDepartment = new CLabel(Msg.getMsg(Env.getCtx(), "XX_Department"));
	private static CComboBox comboDepartment = new CComboBox();
	//Línea
	private CLabel labelLine = new CLabel(Msg.getMsg(Env.getCtx(), "XX_Line_I"));
	private static CComboBox comboLine = new CComboBox();
	//Sección
	private CLabel labelSection = new CLabel(Msg.getMsg(Env.getCtx(), "XX_Section_I"));
	private static CComboBox comboSection = new CComboBox();
	
	//buttons
	private CButton bSearch = new CButton();
	private JButton bReset = ConfirmPanel.createResetButton(Msg.getMsg(Env.getCtx(),"Clear"));

	static KeyNamePair productKey = null; //variable para el id de la columna id de producto seleccionda
	static KeyNamePair warehouseKey = null; //variable para el id de la columna Almacén del producto selecciondo
	
	static int ReturnID=0;

	private static int tableInit_option;

	KeyNamePair loadKNP = new KeyNamePair(0, "");
	/**
	 *  Static Init.
	 *  <pre>
	 *  mainPanel
	 *      northPanel
	 *      centerPanel
	 *          xMatched
	 *          xPanel
	 *          xMathedTo
	 *      southPanel
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception {

		removeActionListeners();
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);

		//  Visual
		CompiereColor.setBackground (this);
		
		//buttons
		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bSearch.setPreferredSize(new Dimension(80,30));	
		bSearch.setEnabled(true);
		
		addActionListeners();

		xScrollPane.setBorder(xTableBorder);
		xScrollPane.setPreferredSize(new Dimension(800, 600));
		
		xScrollPane2.setBorder(xTableBorder2);
		xScrollPane2.setVisible(false);
		xScrollPane2.setPreferredSize(new Dimension(800, 200));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		centerPanel.add(xScrollPane2,  BorderLayout.SOUTH);

		xScrollPane.getViewport().add(xTable, null);
		xScrollPane2.getViewport().add(xTable2, null);	
		
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "XX_Product"));
		statusBar.setStatusDB(0);
		
		// Vendedor	
		northPanel.add(labelBPartner,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(comboBPartner,        new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		// Departamento	
		northPanel.add(labelDepartment,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(comboDepartment,        new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		// Línea
		northPanel.add(labelLine,      new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(comboLine,        new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		// Sección
		northPanel.add(labelSection,      new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(comboSection,        new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		// Referencia
		northPanel.add(LabelRefNumber,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(comboReference,        new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));

		// Almacén
		northPanel.add(labelWarehouse,      new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(comboWarehouse,        new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		
		// Botones de búsqueda
		northPanel.add(bSearch,   new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		northPanel.add(bReset,    new GridBagConstraints(7, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		
		southPanel.validate();
			
	}   //  jbInit


	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit() 	{	
		//Llenar los filtros de busquedas
		loadBasicInfo();
	}   //  dynInit

	
	

	/**
	 *  Table initial state 
	 */
	private void loadBasicInfo() {
		removeActionListeners();
		
		//Restaurar ComboBoxes y CheckBoxes		
		comboBPartner.setEnabled(true);
		comboReference.setEnabled(false); // Depende de la selección de BPartner
		comboDepartment.setEnabled(false); // Depende de la selección de BPartner
		comboLine.setEnabled(false); // Depende de la selección de BPartner
		comboSection.setEnabled(false); // Depende de la selección de BPartner
		comboWarehouse.setEnabled(true);

		comboBPartner.removeAllItems();
		comboReference.removeAllItems();
		comboDepartment.removeAllItems();
		comboLine.removeAllItems();
		comboSection.removeAllItems();
		comboWarehouse.removeAllItems();

		//Llenar los filtros de busquedas
		llenarcombos();
		
		addActionListeners();
		
	}

	/**
	 * 	Dispose
	 */
	public void dispose() 	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
	}	//	dispose

	

	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		comboBPartner.addActionListener(this);
		comboReference.addActionListener(this);
		comboWarehouse.addActionListener(this);
		comboDepartment.addActionListener(this);
		comboLine.addActionListener(this);
		comboSection.addActionListener(this);
		bReset.addActionListener(this);
		bSearch.addActionListener(this);
	} //addActionListeners
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		comboBPartner.removeActionListener(this);
		comboReference.removeActionListener(this);
		comboWarehouse.removeActionListener(this);
		comboDepartment.removeActionListener(this);
		comboLine.removeActionListener(this);
		comboSection.removeActionListener(this);
		bReset.removeActionListener(this);
		bSearch.removeActionListener(this);
	} // removeActionListeners


	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e) {		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// Cargar lista de referencias si se ha seleccionado BPartner
		if(e.getSource() == comboBPartner) {
			removeActionListeners();
			//Cargar combo de Departamentos
			comboDepartment.setEnabled(true);
			loadDepartmentInfo();
			// Cargar referencias
			comboReference.setEnabled(true);
			loadReferenceInfo();
			// Resetear los demás campos
			comboLine.removeAllItems();
			comboSection.removeAllItems();
			comboLine.setEnabled(false);
			comboSection.setEnabled(false);
			addActionListeners();
		} else if(e.getSource() == comboDepartment) {
			removeActionListeners();
			//Cargar combo de Lineas
			comboLine.setEnabled(true);
			loadLineInfo();
			// Cargar referencias
			comboReference.setEnabled(true);
			loadReferenceInfo();
			// Resetear combo de secciones
			comboSection.removeAllItems();
			comboSection.setEnabled(false);
			addActionListeners();
		} else if(e.getSource() == comboLine) {
			removeActionListeners();
			//Cargar combo de Secciones
			comboSection.setEnabled(true);
			loadSectionInfo();
			// Recargar referencias 
			comboReference.setEnabled(true);
			loadReferenceInfo();
			addActionListeners();
		} else if(e.getSource() == comboSection) {
			removeActionListeners();
			// Recargar referencias 
			comboReference.setEnabled(true);
			loadReferenceInfo();
			addActionListeners();
		}
		else if (e.getSource() == bSearch) {
			removeActionListeners();
			cmd_Search();
			addActionListeners();
		} 
		else if (e.getSource() == bReset) {
			loadBasicInfo();
		} else {}
		
	}   //  actionPerformed

	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()  {
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();	
		KeyNamePair ref = (KeyNamePair)comboReference.getSelectedItem();
		// Sólo buscar si se seleccionó BPartner y Referencia
		if (bpar == null || bpar.getKey() == 0) {
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(),"XX_SelectVendor"));
		} else if (ref == null || ref.getKey() == 0) {
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(),"XX_SelectReference"));
		} else {
			tableInit_option = 0;		
			table1Init(); // Inicializar tabla
			tableLoad (xTable); // Ejecutar sql y poblar la tabla
			xScrollPane2.setVisible(false); // Escondo el panel de detalles (refrescar)
			if(xTable.getRowCount()!=0) {
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "XX_Product"));
				statusBar.setStatusDB(xTable.getRowCount());
			} else {
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"FindZeroRecords"));
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(),"FindZeroRecords"));	
			}	
		}
		
	}   //  cmd_Search
	  
	/* Inicializa tabla1*/
	private void table1Init () { //static

		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		log.config("");
		// Limpiar y crear nuevamente
		xScrollPane.getViewport().remove(xTable);
		xTable = new MiniTablePreparator();
		xScrollPane.getViewport().add(xTable, null);
		
		statusBar.setStatusDB(xTable.getRowCount());
		xTable.setAutoResizeMode(MiniTable.AUTO_RESIZE_OFF);
		
		
		// Columnas tabla 1
		ColumnInfo colPCod = new ColumnInfo(Msg.translate(Env.getCtx(), "ProductCode"), "p.Value", KeyNamePair.class, true, false, "M_PRODUCT_ID");
		ColumnInfo colPName = new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "p.Name ||' - '||(case p.M_ATTRIBUTESET_ID when 1002355 then C.name else A.description end)", String.class);
		ColumnInfo colQty = new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_InventoryQty"), "sum(QTY)", String.class);
		ColumnInfo colWare = new ColumnInfo(Msg.getMsg(Env.getCtx(), "Warehouse"), "W.Name", KeyNamePair.class, true, false, "M_WAREHOUSE_ID"); 
		
		Vector<ColumnInfo> colT1 = new Vector<ColumnInfo>(); // Tabla productos
		// Columnas fijas
		colT1.add(colPCod);
		colT1.add(colPName);
		colT1.add(colQty);
		if(comboWarehouse.getSelectedIndex()==0){
			colT1.add(colWare);
		}
				
		//  Listener
		xTable.getSelectionModel().removeListSelectionListener(this);
		xTable.getModel().removeTableModelListener(this);
		xTable.getSelectionModel().addListSelectionListener(this);
		xTable.getModel().addTableModelListener(this);
		for (int i = 0; i < (xTable.getMouseListeners()).length ; i++) {
			xTable.removeMouseListener(xTable.getMouseListeners()[i]);
		}		
		xTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
				for(int i=0; i<1; i++){
					for(int j=0; j<xTable2.getRowCount(); j++){
						if(new Boolean(xTable2.getModel().getValueAt(j, i).toString())){
						}
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				for(int i=0; i<1; i++){
					for(int j=0; j<xTable2.getRowCount(); j++){
						if(new Boolean(xTable2.getModel().getValueAt(j, i).toString())){
						}
					}
				}
			}
		});
		
		
		//Preparar el columninfo
		ColumnInfo [] colT1_array = new ColumnInfo[colT1.size()];
		colT1.toArray(colT1_array);

		//Preparar las tablas
		try {
			m_selectT1 = xTable.prepareTable(colT1_array, "", "", false, "");
		} catch (Exception e) {
			e.printStackTrace();
			m_frame.setCursor(Cursor.getDefaultCursor());
			return ;
		}	
		xTable.setAutoResizeMode(3);
		xTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		m_frame.setCursor(Cursor.getDefaultCursor());
		
				
	}   //  table1Init
	
	
	
	/* Inicializa tabla2*/
	private void table2Init () { //static

		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		//  not yet initialized
		xScrollPane2.getViewport().remove(xTable2);
		xTable2 = new MiniTablePreparator();
		xScrollPane2.getViewport().add(xTable2, null);
		
		// Columnas tabla 2
		ColumnInfo colLoc = new ColumnInfo(Msg.translate(Env.getCtx(), "Location"), "L.Value", String.class);
		ColumnInfo colLQty = new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_InventoryQty"), "sum(QTY)", String.class);

		Vector<ColumnInfo> colT2 = new Vector<ColumnInfo>(); // Tabla detalles

		// Columnas fijas
		colT2.add(colLoc);
		colT2.add(colLQty);
				
		//Preparar el columninfo
		ColumnInfo [] colT2_array = new ColumnInfo[colT2.size()];
		colT2.toArray(colT2_array);

		//Preparar las tablas
		try {
			m_selectT2 = xTable2.prepareTable(colT2_array, "", "", false, "");
		} catch (Exception e) {
			e.printStackTrace();
			m_frame.setCursor(Cursor.getDefaultCursor());
			return ;
		}	
		xTable2.setAutoResizeMode(3);
		m_frame.setCursor(Cursor.getDefaultCursor());
		
				
	} //table2Init
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e) {

		int tableRow = xTable.getSelectedRow();
		if(tableRow!=-1) { 
			if (comboReference.getSelectedIndex()!=0 && comboBPartner.getSelectedIndex()!=0) {
				int orderRow = xTable.getSelectedRow();
				productKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
				if(comboWarehouse.getSelectedIndex()==0){
					warehouseKey = (KeyNamePair)xTable.getValueAt(orderRow, 3);
				}
				xScrollPane2.setVisible(true); //Muestro el panel de detalle (tabla2)
				//cargar la tabla 2
				tableInit_option=1;
				table2Init();
				tableLoad (xTable2);
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"ProductDetail"));
				statusBar.setStatusDB(productKey.getName());
			}
		}
		if (e.getValueIsAdjusting())
			return;
	}   //  valueChanged

	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private void tableLoad (MiniTable table) { //static
		// Preparar el query
		m_sql = new StringBuffer ();
		
		KeyNamePair ref = (KeyNamePair)comboReference.getSelectedItem();
		
		// Busqueda. Referencia y Vendedor obligatorios
		if(tableInit_option==0 && comboReference.getSelectedIndex()!=0 && comboBPartner.getSelectedIndex()!=0) {  
			m_sql.append(//"SELECT DISTINCT p.Value, p.NAME, SUM(QTY) as QTY, W.NAME, W.Value " +
					" FROM  M_Product p join M_STORAGEDETAIL SD using (M_PRODUCT_ID) " +
					"    join M_LOCATOR L on (SD.M_LOCATOR_ID = L.M_LOCATOR_ID) " +
					"    join M_WAREHOUSE W using (M_WAREHOUSE_ID) " +
					"    left join XX_VMR_LONGCHARACTERISTIC C using (XX_VMR_LONGCHARACTERISTIC_ID)" +
					"    left join M_ATTRIBUTESETINSTANCE A on (P.M_ATTRIBUTESETINSTANCE_ID = a.M_ATTRIBUTESETINSTANCE_ID)" +
					" WHERE SD.QTYTYPE='H' and p.XX_VMR_VENDORPRODREF_ID = "+ ref.getKey() +" AND SD.ISACTIVE='Y' ");
			m_groupBy = " GROUP BY p.Value, M_PRODUCT_ID, p.name||' - '||(case p.M_ATTRIBUTESET_ID when 1002355 then C.name else A.description end)"; 
			m_orderBy = " ORDER BY p.Value ASC";
			
		} else if(tableInit_option==1) 	{ // Detalle
			m_sql.append(//"SELECT L.Value " +
					" FROM  M_Product p join M_STORAGEDETAIL SD using (M_PRODUCT_ID) " +
					"     join M_LOCATOR L on (SD.M_LOCATOR_ID = L.M_LOCATOR_ID) " +
					" WHERE SD.QTYTYPE='H' and p.XX_VMR_VENDORPRODREF_ID = "+ ref.getKey() +" AND SD.ISACTIVE='Y' " +
					"	  AND M_PRODUCT_ID='"+ productKey.getKey() +"' ");
			m_groupBy = " GROUP BY L.Value"; 
			m_orderBy = " ORDER BY L.Value ASC";
		}
		
		// Busqueda por Almacén
		if(comboWarehouse.getSelectedIndex()!=0){
			KeyNamePair ware = (KeyNamePair)comboWarehouse.getSelectedItem();
			m_sql.append(" AND ").append("M_WAREHOUSE_ID=").append(ware.getID());
		} else {
			if (tableInit_option==0) {
				m_groupBy += ", W.Name, M_WAREHOUSE_ID ";
				m_orderBy += ", W.NAME ASC";
			} else {
				m_sql.append(" AND ").append("M_WAREHOUSE_ID='").append(warehouseKey.getKey()).append("' ");
			}
		}

		String sql = "";
		if(tableInit_option==0) {
			//System.out.print("m_selectt1: "+m_selectT1);
			sql = MRole.getDefault().addAccessSQL(
				 (m_selectT1 + m_sql), "p", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
				+ m_groupBy + m_orderBy;
			
		} else {
			//System.out.print("m_selectt2: "+m_selectT2);
			sql = MRole.getDefault().addAccessSQL(
				 (m_selectT2 + m_sql.toString()), "p", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
			    + m_groupBy + m_orderBy;
		}
		log.finest(sql);
		//System.out.println(m_sql+m_groupBy+m_orderBy);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {   
			pstmt =  DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			table.loadTable(rs);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) 	{
			e.printStackTrace();
			log.log(Level.SEVERE, sql, e);
			m_frame.setCursor(Cursor.getDefaultCursor());
		}
	}   //  tableLoad


	@Override
	public void tableChanged(TableModelEvent e) {
	}
	public class ComboBoxEditor extends DefaultCellEditor {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ComboBoxEditor(Vector<KeyNamePair> items) {
	        super(new JComboBox(items));
	    }
	}
	
	/**
	 * 
	 */
	private  void llenarcombos(){
		// Socio del negocio (sólo Vendedores)
		String sql = "SELECT DISTINCT part.C_BPARTNER_ID, part.VALUE, part.NAME " +
				" FROM C_BPARTNER part, XX_VMR_VENDORPRODREF ref " +
				" WHERE part.ISVENDOR='Y' AND part.C_BPartner_ID = ref.C_BPartner_ID  " +
				" ORDER BY part.Value ASC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try  {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String()); //En blanco
			comboBPartner.addItem(loadKNP);//
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2)+" - "+rs.getString(3));
				comboBPartner.addItem(loadKNP);//agrego los Proveedores
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		} finally {			
			//Cerrar los statements
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {}
		}
		
		// Cargar combo Almacén
		sql = "SELECT M_WAREHOUSE_ID, name from M_Warehouse order by Value asc";
		try  {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			loadKNP = new KeyNamePair(0, new String());
			comboWarehouse.addItem(loadKNP);//
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboWarehouse.addItem(loadKNP); //Almacenes
			}
			/*rs.close();
			pstmt.close();*/
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}  finally {			
			//Cerrar los statements
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {}
		}
		
	}
	

	private  void loadDepartmentInfo() {
		comboDepartment.removeAllItems();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();	

		// Sólo cargar si se seleccionó BPartner
		if (bpar != null && bpar.getKey() != 0 ) {
			//Cargar informacion de Departamentos
			String sql = "SELECT DISTINCT XX_VMR_DEPARTMENT_ID, d.Value, d.NAME" +
					" FROM  XX_VMR_VENDORDEPARTASSOCI A JOIN XX_VMR_DEPARTMENT D using (XX_VMR_DEPARTMENT_ID)" +
					" WHERE d.ISACTIVE = 'Y' AND C_BPARTNER_ID = " + bpar.getKey() +
					" ORDER BY d.Value ASC";
			loadKNP = new KeyNamePair(0, new String()); //Blanco
			comboDepartment.addItem(loadKNP); //
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2)+" - "+rs.getString(3));
					comboDepartment.addItem(loadKNP); // Referencias
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			} 
		
		} else {
			comboDepartment.setEnabled(false);
		}
	}
	
	private  void loadLineInfo() {
		comboLine.removeAllItems();
		KeyNamePair depart = (KeyNamePair)comboDepartment.getSelectedItem();	

		// Sólo cargar si se seleccionó Departamento
		if (depart != null && depart.getKey() != 0 ) {
			String sql = "SELECT DISTINCT XX_VMR_LINE_ID, Value, NAME " +
					" FROM  XX_VMR_LINE  " +
					" WHERE ISACTIVE = 'Y' AND XX_VMR_DEPARTMENT_ID = " + depart.getKey() +
					" ORDER BY Value ASC" ;
			loadKNP = new KeyNamePair(0, new String()); //Blanco
			comboLine.addItem(loadKNP); //
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2)+" - "+rs.getString(3));
					comboLine.addItem(loadKNP); // Referencias
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			} 
		
		}  else {
			comboLine.setEnabled(false);
		}
	}
	
	private  void loadSectionInfo() {
		comboSection.removeAllItems();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();	

		// Sólo cargar si se seleccionó Línea
		if (line != null && line.getKey() != 0 ) {
			String sql = "SELECT DISTINCT XX_VMR_SECTION_ID, Value, NAME " +
					" FROM  XX_VMR_SECTION" +
					" WHERE ISACTIVE = 'Y' AND XX_VMR_LINE_ID = "+ line.getKey() +
					" ORDER BY Value ASC  " ;
			loadKNP = new KeyNamePair(0, new String()); //Blanco
			comboSection.addItem(loadKNP); //
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2)+" - "+rs.getString(3));
					comboSection.addItem(loadKNP); // Referencias
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			} 
		
		} else {
			comboSection.setEnabled(false);
		}
	}
	
	private  void loadReferenceInfo() {
		comboReference.removeAllItems();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();	
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();	
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();	
		// Sólo cargar si se seleccionó BPartner
		if (bpar != null && bpar.getKey() != 0 ) {
			//Cargar informacion de Referencias
			String sql = "SELECT DISTINCT XX_VMR_VENDORPRODREF_ID, Value, DESCRIPTION " +
					" FROM  XX_VMR_VENDORPRODREF " +
					" WHERE ISACTIVE = 'Y' AND C_BPARTNER_ID = " + bpar.getKey() ;
			
			if (dept != null && dept.getKey() != 0 ) {
				sql += " AND XX_VMR_DEPARTMENT_ID = " + dept.getKey() ;
			}
			if (line != null && line.getKey() != 0 ) {
				sql += " AND XX_VMR_LINE_ID = " + line.getKey() ;
			}
			if (sect != null && sect.getKey() != 0 ) {
				sql += " AND XX_VMR_SECTION_ID = " + sect.getKey() ;
			}
			
			sql +=	" ORDER BY Value ASC";
			loadKNP = new KeyNamePair(0, new String()); //Blanco
			comboReference.addItem(loadKNP); //
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2)+" - "+rs.getString(3));
					comboReference.addItem(loadKNP); // Referencias
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			} 
		
		} else {
			comboReference.setEnabled(false);
		}
	}

	

}
